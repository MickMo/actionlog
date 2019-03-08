package com.micheal.actionlog.dao.log;


import com.micheal.actionlog.bean.log.UserActionLog;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * <内容说明>
 *
 * @author Monan
 * created on 2018/12/18 14:50
 */
@Repository
public interface UserActionLogDao {
    /**
     * Select By ID
     *
     * @param id id
     * @return UserActionLog
     */
    UserActionLog selectById(String id);

    /**
     * update a log
     *
     * @param userActionLog data
     */
    void update(UserActionLog userActionLog);

//    /**
//     * Delete By ID
//     * @param id id
//     * @return affected row number
//     */
//    int delete(String id);

    /**
     * Insert new UserActionLog
     *
     * @param userActionLog data
     * @return affected row number
     */
    int insert(UserActionLog userActionLog);

    /**
     * Select All
     *
     * @return List of UserActionLog
     */
    List<UserActionLog> select();

    /**
     * Select All Logs in a specific date range
     *
     * @return List of UserActionLog
     */
    List<UserActionLog> select(Date startDate, Date endDate);

    /**
     * select Logs By UserId
     *
     * @return List of UserActionLog
     */
    List<UserActionLog> select(String UserId);

    /**
     * Select logs By UserId in a specific date range
     *
     * @return List of UserActionLog
     */
    List<UserActionLog> select(String UserId, Date startDate, Date endDate);

}
