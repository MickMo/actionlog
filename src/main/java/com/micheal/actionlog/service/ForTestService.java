package com.micheal.actionlog.service;

import com.micheal.actionlog.bean.ForTestBean;
import com.micheal.actionlog.dao.ForTestDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SKU 数据权限 Service
 *
 * @author Monan
 * created on 2/27/2019 9:55 AM
 */
@Service
public class ForTestService {
    private Logger logger = LoggerFactory.getLogger(ForTestService.class);

    @Autowired
    ForTestDao forTestDao;

    public void insert(ForTestBean forTestBean) {
        forTestDao.insert(forTestBean);
    }


    public void Update(ForTestBean forTestBean) {
        forTestDao.update(forTestBean);
    }

}
