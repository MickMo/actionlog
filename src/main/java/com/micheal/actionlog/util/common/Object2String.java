package com.micheal.actionlog.util.common;

import com.micheal.actionlog.util.date.DateUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Object 2 String common Util
 *
 * @author Monan
 * created on 2018/12/20 11:01
 */
public class Object2String {
    /**
     * 将对象数组转换为字符串数组
     * <p>支持 {@code String},{@code date},{@code Timestamp},{@code Integer},{@code Long},{@code Double},{@code Short},{@code Float}
     *
     * @param objects
     * @return
     */
    public static List<String> toString(List<Object> objects) {
        if (objects == null || objects.size() < 1) {
            return null;
        }
        List<String> strings = new ArrayList<>();
        for (Object object : objects) {
            strings.add(toString(object));
        }
        return strings;
    }


    /**
     * 将Object转换为String
     * <p>支持 {@code String},{@code date},{@code Timestamp},{@code Integer},{@code Long},{@code Double},{@code Short},{@code Float}
     *
     * @param object
     * @return
     */
    public static String toString(Object object) {
        if (object == null) {
            return null;
        } else if (object instanceof String) {
            return object.toString();
        } else if (object instanceof Date) {
            return DateUtil.getDateTimeStr((Date) object);
        } else if (object instanceof Timestamp) {
            return DateUtil.getDateTimeStr((Timestamp) object);
        } else if (object instanceof Integer || object instanceof Long || object instanceof Double || object instanceof Short || object instanceof Float) {
            return object + "";
        } else if (object instanceof Boolean) {
            return object + "";
        } else {
            return null;
        }
    }

    /**
     * 将对象数组转换为字符串,用','分割
     * <p>支持 {@code String},{@code date},{@code Timestamp},{@code Integer},{@code Long},{@code Double},{@code Short},{@code Float}
     *
     * @param objects 对象数组
     * @return 拼接好的字符串DateUtil
     */
    public static String toSingleString(List objects) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object object : objects) {
            stringBuilder.append(toString(object));
            stringBuilder.append(",");
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
