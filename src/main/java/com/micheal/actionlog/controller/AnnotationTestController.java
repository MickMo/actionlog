package com.micheal.actionlog.controller;

import com.micheal.actionlog.annotation.log.UserActionLogger;
import com.micheal.actionlog.bean.ForTestBean;
import com.micheal.actionlog.service.ForTestService;
import com.micheal.actionlog.service.UserActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AnnotationTestController {
    @Autowired
    UserActionLogService userActionLogService;

    @Autowired
    ForTestService forTestService;

    @UserActionLogger(description = "用户日志测试")
    @RequestMapping(value = "/userActionTest")
    @ResponseBody
    public String annotationExample() {

        ForTestBean forTestBean = new ForTestBean();
        forTestBean.setDeleteFlag(0);
        forTestService.insert(forTestBean);

        forTestBean.setDeleteFlag(1);
        forTestService.Update(forTestBean);
        return "finished";
    }
}



