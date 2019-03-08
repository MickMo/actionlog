package com.micheal.actionlog.util.log;

import com.google.gson.Gson;
import com.micheal.actionlog.annotation.log.UserActionLogger;
import com.micheal.actionlog.bean.log.UserActionLog;
import com.micheal.actionlog.bean.log.UserActionLoggerInfo;
import com.micheal.actionlog.util.common.Object2String;
import com.micheal.actionlog.util.sqlutil.SimpleSQL2SelectSQLUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;

/**
 * UserActionLoggerUtil
 *
 * @author Monan
 * created on 2018/12/18 16:30
 */
@SuppressWarnings("UnnecessaryLocalVariable")
public class UserActionLoggerUtil {

    private static final Logger LOG = LoggerFactory.getLogger(UserActionLoggerUtil.class);

    /**
     * Generate User Action Logs and set it to UserActionLoggerInfo's logs List
     * <p>(for Insert SQL Only)
     *
     * @param userActionLoggerInfo User Action Logger Info
     * @param modifySQLStr         SQL(Insert) String that will be execute later
     */
    public static void generateUserActionLogs(UserActionLoggerInfo userActionLoggerInfo, String modifySQLStr) {
        UUID recordId = UUID.randomUUID();

        UserActionLog userActionLog = new UserActionLog();
        userActionLog.setRecordId(recordId.toString());
        userActionLog.setUserId(userActionLoggerInfo.getUserInfo().getUserid());
        userActionLog.setSQLStatement(modifySQLStr);
        userActionLog.setDescription(userActionLoggerInfo.getDescription());
        userActionLog.setMethodName(userActionLoggerInfo.getMethodName());
        userActionLog.setModifyDate(new Date());

        userActionLoggerInfo.addUserActionLog(userActionLog);

    }

    /**
     * Generate User Action Logs and set it to UserActionLoggerInfo's logs List
     * <p>(for Insert SQL Only)
     *
     * @param userActionLoggerInfo User Action Logger Info
     * @param modifiedDatasMap     Modified Data Map
     * @param modifySQLStr         SQL(Insert) String that will be execute later
     * @param priKeyStr            Primary key Column Name
     */
    public static void generateUserActionLogs(UserActionLoggerInfo userActionLoggerInfo,
                                              Map<String, String> modifiedDatasMap, String modifySQLStr, String priKeyStr) {
        UUID recordId = UUID.randomUUID();
        //convert modified data's key to lower case for more convince use
        Map<String, String> lowerModifiedDatasMap = new HashMap<>();
        for (String key : modifiedDatasMap.keySet()) {
            lowerModifiedDatasMap.put(key.toLowerCase(), modifiedDatasMap.get(key).replaceAll("'", ""));
        }

        for (String key : modifiedDatasMap.keySet()) {
            if (key.contains(SimpleSQL2SelectSQLUtil.Pri_Key_Suffix)) {
                //Primary Key
                continue;
            }
            String[] split = key.split("\\.");
            String columnName = split[1];
            String tableName = split[0];
            String modifyValueStr = lowerModifiedDatasMap.get(columnName.toLowerCase());

            if (StringUtils.isBlank(modifyValueStr)) {
                continue;
            }

            UserActionLog userActionLog = new UserActionLog();
            //Could not get insert primary key's value before this SQL executed
            //userActionLog.setEffectedPrimeKey(priKeyValueStr);
            userActionLog.setEffectedPrimeKeyName(priKeyStr);
            userActionLog.setRecordId(recordId.toString());
            userActionLog.setUserId(userActionLoggerInfo.getUserInfo().getUserid());
            userActionLog.setSQLStatement(modifySQLStr);
            userActionLog.setDescription(userActionLoggerInfo.getDescription());
            userActionLog.setMethodName(userActionLoggerInfo.getMethodName());
            userActionLog.setModifyDate(new Date());
            userActionLog.setTableName(tableName);
            userActionLog.setColumnName(columnName);
            //Insert operation does not have any original value
            //userActionLog.setOriginalValue(originalValueStr);
            userActionLog.setModifiedValue(modifyValueStr);

            userActionLoggerInfo.addUserActionLog(userActionLog);
        }
    }


    /**
     * Process the Set Clause In SQL when the parameter(s) is direct write in sql.<p>
     * like this {@code UPDATE table SET columnA=1 WHERE columnB=?}
     *
     * @param sql              SQL String
     * @param modifiedDatasMap a map that store original parameters
     */
    public static void processSetClauseInSQL(String sql, Map<String, String> modifiedDatasMap) {
        sql = sql.replaceAll("\r\n", "");
        sql = sql.replaceAll("\n", "");
        sql = sql.replaceAll("(?i)set", " SET ");
        sql = sql.replaceAll("(?i)where", " WHERE ");

        String[] sets = sql.split("SET");
        if (StringUtils.isBlank(sets[1])) {
            return;
        }
        String[] split = sets[1].split("WHERE");
        if (StringUtils.isBlank(split[0])) {
            return;
        }
        String[] split2 = split[0].split(",");
        for (String setClausePart : split2) {
            if (setClausePart.contains("?")) {
                continue;
            }
            String[] split1 = setClausePart.split("=");
            if (split1.length < 2 || StringUtils.isBlank(split1[0]) || StringUtils.isBlank(split1[1])) {
                continue;
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : modifiedDatasMap.keySet()) {
                stringBuilder.append(s.trim().toLowerCase() + " ");
            }
            if (StringUtils.isBlank(split1[0])) {
                continue;
            }
            if (stringBuilder.toString().contains(split1[0].trim().toLowerCase())) {
                continue;
            }
            modifiedDatasMap.put(split1[0].trim(), split1[1].trim());
        }
    }

    /**
     * Generate User Action Logs and set it to UserActionLoggerInfo's logs List
     *
     * @param originalDatasList    Original Data List
     * @param userActionLoggerInfo User Action Logger Info
     * @param modifiedDatasMap     Modified Data Map
     * @param modifySQLStr         SQL(Update,Delete) String that will be execute later
     */
    public static void generateUserActionLogs(List<Map<String, Object>> originalDatasList,
                                              UserActionLoggerInfo userActionLoggerInfo,
                                              Map<String, String> modifiedDatasMap, String modifySQLStr) {
        UUID recordId = UUID.randomUUID();
        //convert modified data's key to lower case for more convince use
        Map<String, String> lowerModifiedDatasMap = new HashMap<>();
        for (String key : modifiedDatasMap.keySet()) {
            lowerModifiedDatasMap.put(key.toLowerCase(), modifiedDatasMap.get(key).replaceAll("'", ""));
        }

        if (originalDatasList == null || originalDatasList.isEmpty()) {
            LOG.warn("originalDatasList is empty,No Original Data will be record.");
            UserActionLog userActionLog = new UserActionLog();
            userActionLog.setRecordId(recordId.toString());
            userActionLog.setUserId(userActionLoggerInfo.getUserInfo().getUserid());
            userActionLog.setSQLStatement(modifySQLStr);
            userActionLog.setDescription(userActionLoggerInfo.getDescription());
            userActionLog.setMethodName(userActionLoggerInfo.getMethodName());
            userActionLog.setModifyDate(new Date());

            userActionLoggerInfo.addUserActionLog(userActionLog);
        } else {

            for (Map<String, Object> originalDatas : originalDatasList) {
                String priKeyStr = null;
                String priKeyValueStr = null;

                //try to get primary Key Column Name and it's value
                for (String key : originalDatas.keySet()) {
                    if (key.contains(SimpleSQL2SelectSQLUtil.Pri_Key_Suffix)) {
                        //Primary Key
                        priKeyStr = key.substring(0, key.length() - SimpleSQL2SelectSQLUtil.Pri_Key_Suffix.length()).split("\\.")[1];
                        Object value = originalDatas.get(key);
                        priKeyValueStr = Object2String.toString(value);
                        break;
                    }
                }

                //No primary Key Column Name ,or primary Key Value,Skip
                if (StringUtils.isBlank(priKeyValueStr) || StringUtils.isBlank(priKeyStr)) {
                    continue;
                }

                //building log data
//                List<UserActionLog> logsTemp = new ArrayList<>();
                for (String originalKey : originalDatas.keySet()) {
                    if (originalKey.contains(SimpleSQL2SelectSQLUtil.Pri_Key_Suffix)) {
                        //Primary Key
                        continue;
                    }
                    String[] split = originalKey.split("\\.");
                    String columnName = split[1];
                    String tableName = split[0];
                    String modifyValueStr = lowerModifiedDatasMap.get(columnName.toLowerCase());
                    if (StringUtils.isBlank(modifyValueStr)) {
                        continue;
                    }
                    Object originalValue = originalDatas.get(originalKey);
                    String originalValueStr = Object2String.toString(originalValue);
                    if (modifyValueStr.equals(originalValueStr)) {
                        continue;
                    }

                    UserActionLog userActionLog = new UserActionLog();
                    userActionLog.setEffectedPrimeKey(priKeyValueStr);
                    userActionLog.setEffectedPrimeKeyName(priKeyStr);
                    userActionLog.setRecordId(recordId.toString());
                    userActionLog.setUserId(userActionLoggerInfo.getUserInfo().getUserid());
                    userActionLog.setSQLStatement(modifySQLStr);
                    userActionLog.setDescription(userActionLoggerInfo.getDescription());
                    userActionLog.setMethodName(userActionLoggerInfo.getMethodName());
                    userActionLog.setModifyDate(new Date());
                    userActionLog.setTableName(tableName);
                    userActionLog.setColumnName(columnName);
                    userActionLog.setOriginalValue(originalValueStr);
                    userActionLog.setModifiedValue(modifyValueStr);

//                    logsTemp.add(userActionLog);
                    userActionLoggerInfo.addUserActionLog(userActionLog);
                }

                // Data Compress
//                for (UserActionLog log : logsTemp) {
//
//                    UserActionLog userActionLog = new UserActionLog();
//                    userActionLog.setEffectedPrimeKey(priKeyValueStr);
//                    userActionLog.setEffectedPrimeKeyName(priKeyStr);
//                    userActionLog.setRecordId(recordId.toString());
//                    userActionLog.setUserId(userActionLoggerInfo.getUserInfo().getUserid());
//                    userActionLog.setSQLStatement(modifySQLStr);
//                    userActionLog.setDescription(userActionLoggerInfo.getDescription());
//                    userActionLog.setMethodName(userActionLoggerInfo.getMethodName());
//                    userActionLog.setModifyDate(new date());
//                    userActionLog.setTableName(tableName);
//                    userActionLog.setColumnName(columnName);
//                    userActionLog.setOriginalValue(originalValueStr);
//                    userActionLog.setModifiedValue(modifyValueStr);
//
//                    userActionLoggerInfo.addUserActionLog(userActionLog);
//                }
            }
        }
    }

    /**
     * get SQL From Mybatis Interceptor
     *
     * @param invocation Invocation
     * @return SQL
     */
    public static String getSQLFromMybatisInterceptor(Invocation invocation) {
        if (invocation == null) {
            return null;
        }
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        return sql;
    }

    /**
     * get parameterMappings
     *
     * @param invocation Invocation
     * @return parameterMappings List
     */
    public static List<ParameterMapping> getMappingData(Invocation invocation) {
        if (invocation == null) {
            return null;
        }
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        return parameterMappings;
    }


    /**
     * get MappedStatement from mybatis Interceptor's Invocation
     *
     * @param invocation Invocation
     * @return MappedStatement
     */
    public static MappedStatement getMappedStatement(Invocation invocation) {
        if (invocation == null) {
            return null;
        }
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject
                .DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        return mappedStatement;
    }

    /**
     * Judge whether a SQL request is form UserActionLogger
     *
     * @param invocation Invocation
     * @return return {@code true} if it is UserActionLogger's own SQL Request,otherwise return {@code false};
     */
    public static boolean isUserActionLoggersSQLRequest(Invocation invocation) {
        MappedStatement mappedStatement = getMappedStatement(invocation);
        if (mappedStatement == null) {
            return false;
        }
        if (mappedStatement.getId().toLowerCase().contains("UserActionLogDao".toLowerCase())) {
            return true;
        }
        return false;
    }

    /**
     * get corresponding Select SQL Base on Update SQL
     *
     * @param invocation           Invocation
     * @param boundParameter       Original SQL parameters map
     * @param userActionLoggerInfo UserActionLoggerInfo
     * @return corresponding Select SQL
     */
    public static String getSelectSQL(Invocation invocation, Map<String, String> boundParameter, UserActionLoggerInfo userActionLoggerInfo) {
        if (invocation == null) {
            return null;
        }
        //get corresponding SQL
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject
                .DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        String selectSQLName = userActionLoggerInfo.getSelectSQLName();
        Configuration configuration = (Configuration) metaObject.getValue("delegate.configuration");
        MappedStatement selectMappedStatement = configuration.getMappedStatement(selectSQLName);

        //get parameter mapping
        BoundSql selectMappedBoundSq = statementHandler.getBoundSql();
        List<ParameterMapping> parameterMappings = selectMappedBoundSq.getParameterMappings();
        //bind parameters
        String selectSQLStr = selectMappedStatement.getBoundSql(parameterMappings).getSql();
        String boundSelectSQLStr = bindParameterstoSQL(boundParameter, selectSQLStr);
        return boundSelectSQLStr;
    }


    /**
     * bind parameters to SQL
     *
     * @param boundParameter Original SQL parameters map
     * @param sql            New Select SQL
     * @return Bound Sql
     */
    public static String bindParameterstoSQL(Map<String, String> boundParameter, String sql) {
        if (boundParameter == null || boundParameter.size() == 0) {
            return null;
        }
        String sqlTemp = sql.toLowerCase();
        if (!sqlTemp.contains("where")) {
            return sql;
        }

        Map<String, String> tempBoundParameter = new HashMap<>();
        int idCounter = 0;
        for (String key : boundParameter.keySet()) {
            String value = boundParameter.get(key);
            String lowerCaseKey = key.toLowerCase();
            if (lowerCaseKey.contains("id")) {
                idCounter++;
            }
            tempBoundParameter.put(lowerCaseKey, value);
        }

        //将where条件拆分为and子句
        String[] wheres = sqlTemp.split("where");
        String[] ands = wheres[1].split("and");
        for (String and : ands) {
            String[] condition = and.split("=");
            if (condition.length > 2) {
                LOG.error("Could not handle condition:'{}' due to too many parameter,in SQL:'{}' with Parameter:{}." +
                                "May be you need to specify a select SQL for this SQL?",
                        and, sql, new Gson().toJson(tempBoundParameter));
                return null;
            } else if (condition.length == 2) {
                //deal with 'xxxx=XXXX' condition
                for (String key : condition) {
                    if (!"?".equals(key.trim()) && !"1".equals(key.trim())) {
                        String boundParameterKey = key.trim().toLowerCase();
                        String value = null;
                        //如果SQL中只有一个包含"ID"的字段,允许直接遍历获取参数值,否则严格按照字段名来获取
                        if (idCounter > 1) {
                            value = tempBoundParameter.get(boundParameterKey);
                        } else {
                            for (String tempBoundParameterKey : tempBoundParameter.keySet()) {
                                if (tempBoundParameterKey.contains(boundParameterKey) || boundParameterKey.contains(tempBoundParameterKey)) {
                                    value = tempBoundParameter.get(tempBoundParameterKey);
                                }
                            }
                        }
                        //异常.参数为空
                        if (StringUtils.isBlank(value)) {
                            LOG.warn("Could not find parameter:'{}',in SQL:'{}' with Parameter:{}", key, sql, new Gson().toJson(tempBoundParameter));
                            return null;
                        } else {
                            sql = sql.replaceFirst("\\?", value);
                            continue;
                        }
                    }
                }
            } else if (condition.length == 1 && and.contains("in")) {
                //deal with 'in' condition
                String[] insCondition = and.split("in");
                if (insCondition.length == 2) {
                    String inColumnName = insCondition[0];
                    String inValue = insCondition[1];
                    for (String key : tempBoundParameter.keySet()) {
                        String value = tempBoundParameter.get(key).trim();
                        boolean contains = key.contains(inColumnName.trim());
                        boolean contains1 = value.contains("[");
                        boolean contains2 = value.contains("]");
                        boolean isThereOnly1Parameter = inValue.trim().replaceAll(" ", "").equals("(?)");
                        if (contains && contains1 && contains2) {
                            String substring = value.substring(1, value.length() - 1);
                            sql = sql.replace(inValue.trim(), "(" + substring + ")");
                            continue;
                        } else if (contains && isThereOnly1Parameter) {
                            sql = sql.replace(inValue.trim(), "(" + value + ")");
                            continue;
                        }
                    }
                } else {
                    LOG.error("Could not handle 'in' condition:'{}',in SQL:'{}' with Parameter:{}", and, sql, new Gson().toJson(tempBoundParameter));
                    return null;
                }
            } else {
                LOG.error("Could not handle condition:'{}' due to unknown reason,in SQL:'{}' with Parameter:{}",
                        and, sql, new Gson().toJson(tempBoundParameter));
                return null;
            }

        }
        if (sql.indexOf("?") > 0) {
            LOG.error("Could not replace all in SQL:'{}' with Parameter:{}", sql, new Gson().toJson(boundParameter));
            return null;
        }
        return sql;
    }


    /**
     * get Annotation Infos
     *
     * @param joinPoint JoinPoint
     * @throws ClassNotFoundException ClassNotFoundException
     */
    public static void getAnnotationInfo(JoinPoint joinPoint, UserActionLoggerInfo userActionLoggerInfo) throws ClassNotFoundException {
        if (null == joinPoint || null == userActionLoggerInfo) {
            return;
        }
        String classname = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        Class<?> targetClazz = Class.forName(classname);
        Method[] methods = targetClazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (args.length == parameterTypes.length) {
                    userActionLoggerInfo.setSqlType(method.getAnnotation(UserActionLogger.class).sqlType());
                    userActionLoggerInfo.setLogLevel(method.getAnnotation(UserActionLogger.class).logLevel());
                    userActionLoggerInfo.setSelectSQLName(method.getAnnotation(UserActionLogger.class).selectSQLName());
                    userActionLoggerInfo.setDescription(method.getAnnotation(UserActionLogger.class).description());
                    userActionLoggerInfo.setMethodName(method.getName());
                }
            }
        }
    }

    /**
     * Check Sql Type
     *
     * @param sql         SQL Statement
     * @param sqlTypesStr DB Operation Type
     * @return return {@code true} if SQL contains certain DB Operation({@code SELECT},{@code UPDATE},etc.),return false otherwise.
     */
    public static boolean checkSQLType(String sql, String sqlTypesStr) {
        String[] sqlTypes = sqlTypesStr.split(",");
        if (StringUtils.isBlank(sql) || sqlTypes.length < 1) {
            return false;
        }

        for (String type : sqlTypes) {
            String[] split = sql.trim().split(" ");
            if (split[0].toLowerCase().contains(type.toLowerCase().trim())) {
                return true;
            }
        }
        return false;
    }


    /**
     * Bind Parameters to SQL
     *
     * @param invocation   Invocation
     * @param modifySQLStr modify SQL
     * @return Bound SQL
     */
    public static String boundSQL(Invocation invocation, String modifySQLStr) {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject
                .DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        Configuration configuration = (Configuration) metaObject.getValue("delegate.configuration");
        BoundSql selectMappedBoundSq = statementHandler.getBoundSql();
        modifySQLStr = UserActionLoggerUtil.configNewSQL(configuration, selectMappedBoundSq, modifySQLStr);
        return modifySQLStr;
    }


    /**
     * Config a new SQL base on other bound SQL's Configuration
     *
     * @param configuration Original SQL Configuration
     * @param boundSql      Bound SQL,here is the parameters come from.
     * @param sql           New SQL
     * @return bound  Sql
     */
    public static String configNewSQL(Configuration configuration, BoundSql boundSql, String sql) {
        if (StringUtils.isBlank(sql)) {
            return null;
        }
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        sql = sql.replaceAll("[\\s]+", " ");
        if (!CollectionUtils.isEmpty(parameterMappings) && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else {
                        sql = sql.replaceFirst("\\?", "MISSING");
                    }
                }
            }
        }
        return sql;
    }


    /**
     * get Bound Parameter Map
     *
     * @param invocation Invocation
     * @return Parameter Map
     */
    public static Map<String, String> getBoundParameter(Invocation invocation) {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        Object parameterObject = statementHandler.getParameterHandler().getParameterObject();
        Map<String, String> parametersMap = Object2SimpleMap(parameterObject);
        return parametersMap;
    }


    /**
     * Object2SimpleMap <p>
     * Only those Object could convert to String will be processed.
     *
     * @param obj Bean
     * @return Map
     */
    private static Map<String, String> Object2SimpleMap(Object obj) {
        if (obj == null) {
            return null;
        }

        if (obj instanceof Map) {
            Map<String, String> tempMap = new HashMap<>();
            Map<String, Object> map = (Map<String, Object>) obj;
            for (String s : map.keySet()) {
                //跳过没有意义的参数
                if (s.contains("param")) {
                    continue;
                }
                Object o = map.get(s);
                if (o instanceof ArrayList) {
                    ArrayList<String> list = (ArrayList<String>) o;
                    String valueStr = Object2String.toSingleString(list);
                    tempMap.put(s, valueStr);
                } else {
                    String valueStr = Object2String.toString(o);
                    if (o instanceof Date || o instanceof Timestamp) {
                        valueStr = "str_to_date('" + valueStr + "','%Y-%m-%d %H:%i:%s')";
                    }
                    tempMap.put(s, valueStr);
                }
            }
            return tempMap;
        }

        Map<String, String> map = new HashMap<String, String>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // class propertis will be filtrated
                if (!key.equals("class")) {
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    String valueStr = Object2String.toString(value);
                    if (StringUtils.isBlank(valueStr)) {
                        valueStr = getParameterValue(value);
                    } else {
                        valueStr = "'" + valueStr + "'";
                    }
                    if (StringUtils.isNotBlank(valueStr)) {
                        map.put(key, valueStr);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Object2SimpleMap Error " + e);
        }
        return map;
    }

    /**
     * get string Value form metaObject
     *
     * @param obj metaObject
     * @return String Value
     */
    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }

}
