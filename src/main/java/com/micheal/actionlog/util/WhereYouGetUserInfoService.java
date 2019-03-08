package com.micheal.actionlog.util;

import com.micheal.actionlog.bean.user.BaseUserInfo;
import org.springframework.stereotype.Service;

/**
 * <内容说明>
 *
 * @author Monan
 * created on 3/8/2019 2:18 PM
 */
@Service
public class WhereYouGetUserInfoService {

    public BaseUserInfo getLoginUser(){
        BaseUserInfo userInfo = new BaseUserInfo();
        userInfo.setUserid(00001);
        userInfo.setLoginname("michael");
        return userInfo;
    }
}
