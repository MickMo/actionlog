package com.micheal.actionlog.aspect;

import com.google.gson.Gson;
import com.micheal.actionlog.bean.log.UserActionLog;
import com.micheal.actionlog.bean.log.UserActionLoggerInfo;
import com.micheal.actionlog.bean.user.BaseUserInfo;
import com.micheal.actionlog.constant.log.LogLevelConstant;
import com.micheal.actionlog.dao.log.UserActionLogDao;
import com.micheal.actionlog.enums.sql.SQLTypeEnum;
import com.micheal.actionlog.util.WhereYouGetUserInfoService;
import com.micheal.actionlog.util.log.UserActionLoggerUtil;
import com.micheal.actionlog.util.sqlutil.ResultSetUtil;
import com.micheal.actionlog.util.sqlutil.SQLTypeUtil;
import com.micheal.actionlog.util.sqlutil.SQLUtil;
import com.micheal.actionlog.util.sqlutil.SimpleSQL2SelectSQLUtil;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户操作记录 服务
 *
 * @author Monan
 * created on 2018/12/10 16:53
 */
@Aspect
@Service
@Transactional
@Intercepts({@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class, Integer.class})
})
public class UserActionLogAspect implements Interceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * log和用户信息<p>
     * key:Request's Hashcode<p>
     * value:UserActionLoggerInfo
     */
    private static volatile Map<Integer, UserActionLoggerInfo> dataMap = new ConcurrentHashMap<>();

    @Autowired
    private UserActionLogDao userActionLogDao;
    @Autowired
    private WhereYouGetUserInfoService whereYouGetUserInfoService;

    /**
     * 获取用户信息,注解信息
     *
     * @param joinPoint
     */
    @Before("logPointCut()")
    public void getUserInfo(JoinPoint joinPoint) {
        StopWatch timer = new StopWatch();
        timer.start();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return;
        }
        //获取用户信息
        BaseUserInfo loginUser = whereYouGetUserInfoService.getLoginUser();
        try {
            if (loginUser != null) {
                //转换用户信息
                String userInfoJson = new Gson().toJson(loginUser);
                BaseUserInfo userInfo = new Gson().fromJson(userInfoJson, BaseUserInfo.class);
                //获取注解信息
                UserActionLoggerInfo userActionLoggerInfo = new UserActionLoggerInfo(userInfo);
                UserActionLoggerUtil.getAnnotationInfo(joinPoint, userActionLoggerInfo);
                //缓存数据,供Mybatis拦截器线程使用
                HttpServletRequest request = requestAttributes.getRequest();
                dataMap.put(request.hashCode(), userActionLoggerInfo);
            } else {
                logger.error("Could not get login User Info!Unauthorized Action! May be you are using annotation 'UserActionLogger' in a wrong way?");
            }
        } catch (Exception e) {
            logger.error("Could not get info from annotation 'UserActionLogger'!,cause:", e);
        } finally {
            logger.info("UserAction Logging Before finished in:{} ms,with TimerID:{}.", timer.getTime(), timer.hashCode());
        }
    }

    @Pointcut("@annotation(com.micheal.actionlog.annotation.log.UserActionLogger)")
    public void logPointCut() {

    }

    /**
     * 记录日志
     */
    @After("logPointCut()")
    public void logAction() {
        StopWatch timer = new StopWatch();
        timer.start();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return;
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            //获取到插入数据后的ID
            UserActionLoggerInfo userActionLoggerInfo = dataMap.get(request.hashCode());
            if (null != userActionLoggerInfo && null != userActionLoggerInfo.getUserInfo()) {
                //check loglevel and sqlType
                if (userActionLoggerInfo.getUserActionLogsSize() < 1 || !userActionLoggerInfo.isValid()) {
                    return;
                }
                List<UserActionLog> logs = userActionLoggerInfo.getLogs();
                //记录到Log
                if (userActionLoggerInfo.getLogLevel().contains(LogLevelConstant.Log)) {
                    for (UserActionLog log : logs) {
                        logger.info("Description:'{}',LogLevel:'{}',SQLType:'{}'.Detail:{}",
                                userActionLoggerInfo.getDescription(),
                                userActionLoggerInfo.getLogLevel(),
                                userActionLoggerInfo.getSqlType(),
                                log.toString().replaceAll("\n", ""));
                    }
                }
                //记录到DB
                if (userActionLoggerInfo.getLogLevel().contains(LogLevelConstant.DB)) {
                    for (UserActionLog log : logs) {
                        userActionLogDao.insert(log);
                    }
                }
            }
        } finally {
            timer.reset();
            dataMap.remove(request.hashCode());
            logger.info("UserAction Logging After finished in:{} ms,with TimerID:{}.", timer.getTime(), timer.hashCode());
        }
    }


    /**
     * Mybatis 拦截类<p>
     * 即使处理异常,也不会影响原有SQL执行
     *
     * @param invocation Invocation
     * @return 继续执行
     * @throws Throwable Exception
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StopWatch internalTimer = new StopWatch();
        internalTimer.start();
        /**
         * 1.放行 UserActionLogger自己 的SQL请求
         * 2.放行 没有获取到登陆用户信息 的SQL请求
         */
        if (UserActionLoggerUtil.isUserActionLoggersSQLRequest(invocation) || dataMap.size() < 1) {
            return invocation.proceed();
        }
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            if (requestAttributes == null) {
                //does not mean this function will return null,see finally block
                return null;
            }

            HttpServletRequest request = requestAttributes.getRequest();
            UserActionLoggerInfo userActionLoggerInfo = dataMap.get(request.hashCode());
            //check User Info
            if (null == userActionLoggerInfo || null == userActionLoggerInfo.getUserInfo()) {
                return null;
            }

            //get Original SQL
            String modifySQLStr = UserActionLoggerUtil.getSQLFromMybatisInterceptor(invocation);
            String modifySQLStrBackup = modifySQLStr;
            //check SQL type
            boolean checkSQLType = UserActionLoggerUtil.checkSQLType(modifySQLStr, userActionLoggerInfo.getSqlType());
            if (!checkSQLType) {
                return null;
            }

            //获取JDBC连接
            connection = (Connection) invocation.getArgs()[0];
            Statement stmt = CCJSqlParserUtil.parse(modifySQLStr);
            SQLTypeEnum sqlType = SQLTypeUtil.getSqlType(stmt);

            //填充ModifySQL
            modifySQLStr = UserActionLoggerUtil.boundSQL(invocation, modifySQLStr);

            //数据表数量校验
            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
            List<String> tables = tablesNamesFinder.getTableList(stmt);
            if (tables == null || tables.size() < 1) {
                logger.error("No User Action will be record due SQL Statement '{}' is way too complex.Maybe you can specify a SELECT SQL?", modifySQLStr);
                return null;
            }

            //获取修改数据
            Map<String, String> modifiedDatasMap = UserActionLoggerUtil.getBoundParameter(invocation);


            //根据ModifySQL的复杂程度选择不同的select语句生成策略
            String originalDataSQL = null;
            String priKeyColumnNameStrInsertSQL = null;
            if (tables.size() > 1 && StringUtils.isNotBlank(userActionLoggerInfo.getSelectSQLName())) {
                //复杂Update语句,寻找与之对应的Select语句
                originalDataSQL = UserActionLoggerUtil.getSelectSQL(invocation, modifiedDatasMap, userActionLoggerInfo);
            } else {
                //自动生成SQL
                if (sqlType == null) {
                    logger.error("No User Action will be record due to Could Not Judge SQL Type with Statement:{}", modifySQLStr);
                    return null;
                } else if (SQLTypeEnum.Insert.equals(sqlType)) {
                    //查询主键字段名
                    String getPrimaryKeyByTableNameSQLStr = SQLUtil.generateGetPrimaryColumnsByTableNameSQL(tables.get(0));
                    ps = connection.prepareStatement(getPrimaryKeyByTableNameSQLStr);
                    ResultSet resultSet = ps.executeQuery();
                    List<Map<String, String>> maps = ResultSetUtil.resultSetToStringList(resultSet);
                    Map<String, String> stringObjectMap = maps.get(0);
                    Set<Map.Entry<String, String>> entries = stringObjectMap.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        priKeyColumnNameStrInsertSQL=entry.getValue();
                    }
                } else if (SQLTypeEnum.Update.equals(sqlType)) {
                    //对于update语句中的set子句,如果赋值是直接写在SQL中的特殊处理
                    UserActionLoggerUtil.processSetClauseInSQL(modifySQLStrBackup, modifiedDatasMap);
                    //查询主键字段名
                    String getPrimaryKeyByTableNameSQLStr = SQLUtil.generateGetPrimaryColumnsByTableNameSQL(tables.get(0));
                    ps = connection.prepareStatement(getPrimaryKeyByTableNameSQLStr);
                    ResultSet resultSet = ps.executeQuery();
                    List<Map<String, String>> maps = ResultSetUtil.resultSetToStringList(resultSet);
                    Map<String, String> stringObjectMap = maps.get(0);
                    String priKeyColumnNameStr = null;
                    Set<Map.Entry<String, String>> entries = stringObjectMap.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        priKeyColumnNameStr=entry.getValue();
                    }
                    if (StringUtils.isNotBlank(priKeyColumnNameStr)) {
                        //添加主键字段
                        originalDataSQL = SimpleSQL2SelectSQLUtil.toSelectSQL((Update) stmt, priKeyColumnNameStr);
                        //绑定参数
                        originalDataSQL = UserActionLoggerUtil.bindParameterstoSQL(modifiedDatasMap, originalDataSQL);
                    }
                } else if (SQLTypeEnum.Delete.equals(sqlType)) {
                    //查询所有普通字段名
                    String getAllCommonColumnsByTableNameSQLStr = SQLUtil.generateGetAllCommonColumnsByTableNameSQL(tables.get(0));
                    ps = connection.prepareStatement(getAllCommonColumnsByTableNameSQLStr);
                    ResultSet commonRS1 = ps.executeQuery();
                    List<Map<String, String>> maps = ResultSetUtil.resultSetToStringList(commonRS1);
                    Map<String, String> stringObjectMap = maps.get(0);
                    String commonColumnsName = null;
                    Set<Map.Entry<String, String>> entries = stringObjectMap.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        commonColumnsName=entry.getValue();
                    }

                    //查询主键字段名
                    String getPrimaryKeyByTableNameSQLStr = SQLUtil.generateGetPrimaryColumnsByTableNameSQL(tables.get(0));
                    ps = connection.prepareStatement(getPrimaryKeyByTableNameSQLStr);
                    ResultSet priKeyRS = ps.executeQuery();
                    String priKeyColumnsName = ResultSetUtil.resultSetColumnToString(priKeyRS);

                    if (StringUtils.isBlank(priKeyColumnsName) && StringUtils.isBlank(commonColumnsName)) {
                        String allColumnsNames = priKeyColumnsName + "," + commonColumnsName;
                        //构建原始数据查询SQL
                        originalDataSQL = SimpleSQL2SelectSQLUtil.toSelectSQL((Delete) stmt, allColumnsNames);
                        //绑定参数
                        originalDataSQL = UserActionLoggerUtil.bindParameterstoSQL(modifiedDatasMap, originalDataSQL);
                    }
                } else if (SQLTypeEnum.Select.equals(sqlType)) {
                    logger.info("No User Action will be record,Due to User Action Type is :{}.Not yet supported (2019-2-26)", sqlType.getValue(), modifySQLStr);
                    return null;
                } else {
                    logger.error("No User Action will be record due to Could Not Deal with SQL Type:{}, with Statement", sqlType.getValue(),
                            modifySQLStr);
                    return null;
                }
            }

            if (SQLTypeEnum.Select.equals(sqlType)) {
                //记录Select 语句 目前不生效
                UserActionLoggerUtil.generateUserActionLogs(userActionLoggerInfo, modifySQLStr);
            } else if (SQLTypeEnum.Insert.equals(sqlType)) {
                //构建记录
                UserActionLoggerUtil.generateUserActionLogs(userActionLoggerInfo, modifiedDatasMap, modifySQLStr, priKeyColumnNameStrInsertSQL);
            } else {
                //获取修改前数据
                List<Map<String, Object>> originalDatasList = null;
                if (StringUtils.isNotBlank(originalDataSQL)) {
                    ps = connection.prepareStatement(originalDataSQL);
                    ResultSet resultSet = ps.executeQuery();
                    originalDatasList = ResultSetUtil.resultSetToList(resultSet);
                } else {
                    logger.warn("Could not find/convert Select SQL due to empty 'originalDataSQL' base on Target SQL:{}." +
                                    "Could NOT record any Original Data.",
                            modifySQLStr);
                }
                //构建记录
                UserActionLoggerUtil.generateUserActionLogs(originalDatasList, userActionLoggerInfo, modifiedDatasMap, modifySQLStr);
            }

        } catch (Exception e) {
            logger.error("UserActionLoggingService ERROR :{}", e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            logger.info("UserActionLoggingService intercept internalTimer:{} ms,with TimerID:{}.", internalTimer.getTime(), internalTimer.hashCode());
            return invocation.proceed();
        }
    }


    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }


}
