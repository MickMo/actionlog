package com.micheal.actionlog.service;

import com.micheal.actionlog.bean.log.UserActionLog;
import com.micheal.actionlog.dao.log.UserActionLogDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户操作日志 服务
 *
 * @author Monan
 * created on 2018/12/10 16:53
 */
@Service
public class UserActionLogService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private UserActionLogDao userActionLogDao;

    /**
     * getAllLogs
     *
     * @return User Action Logs
     */
    public List<UserActionLog> getAllLogs() {
        return userActionLogDao.select();
    }

    /**
     * insert a Logs
     *
     * @return affected row number
     */
    public int insert(UserActionLog log) {
        return userActionLogDao.insert(log);
    }

    /**
     * insert a Logs
     *
     * @return effected rows number
     */
    public void update(UserActionLog log) {
        userActionLogDao.update(log);
    }

    /**
     * getAllLogs
     *
     * @return User Action Logs
     */
    public List<UserActionLog> getAllLogs(Date startDate, Date endDate) {
        return userActionLogDao.select();
    }


    /**
     * getLogsByUserId
     *
     * @param userId User Id
     * @return User Action Logs
     */
    public List<UserActionLog> getLogsByUserId(String userId) {
        return userActionLogDao.select(userId);
    }

    /**
     * getLogsByUserId
     *
     * @param userId User Id
     * @return User Action Logs
     */
    public List<UserActionLog> getLogsByUserId(String userId, Date startDate, Date endDate) {
        return userActionLogDao.select(userId, startDate, endDate);
    }
}
