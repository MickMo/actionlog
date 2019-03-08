package com.micheal.actionlog.util.date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple Date Util
 *
 * @author Monan
 * created on 3/8/2019 10:36 AM
 */

@SuppressWarnings("CheckStyle")
public class DateUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);


    /**
     * getDateStr get a string with format YYYY-MM-DD HH:mm:ss from a Date
     * object
     *
     * @param date date
     * @return String
     */
    static public String getDateTimeStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    /**
     * getDateStr get a string with format YYYY-MM-DD HH:mm:ss from a Date
     * object
     *
     * @param timestamp date
     * @return String
     */
    static public String getDateTimeStr(Timestamp timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(timestamp);
    }

}
