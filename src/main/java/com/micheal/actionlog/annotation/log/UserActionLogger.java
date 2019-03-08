package com.micheal.actionlog.annotation.log;


import com.micheal.actionlog.constant.SQLTypeConstant;
import com.micheal.actionlog.constant.log.LogLevelConstant;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 用户操作记录<p>
 * 可记录所有对数据库进行操作的SQL<p>
 * 并记录调用方法,调用人,被修改的数据等<p>
 *
 * @author Monan
 * created on 2018/12/10 12:01
 */
@Target({METHOD})
@Retention(RUNTIME)
@Documented
public @interface UserActionLogger {
    /**
     * 业务描述
     *
     * @return
     */
    String description() default "";

    /**
     * 拦截的SQL类型<p>
     * 默认为 {@code Update},{@code Delete},{@code Insert} 三个SQL类型
     *
     * @return
     * @see SQLTypeConstant
     */
    String sqlType() default SQLTypeConstant.Effected;

    /**
     * 记录等级<p>
     * 默认为记录到数据库
     *
     * @return
     * @see LogLevelConstant
     */
    String logLevel() default LogLevelConstant.DB;

    /**
     * 对应的Select语句,在复杂修改语句下需要指定
     *
     * @return
     */
    String selectSQLName() default "";
}
