package com.micheal.actionlog.dao;

import com.micheal.actionlog.bean.ForTestBean;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Monan
 * created on 2/27/2019 9:55 AM
 */
@Repository
public interface ForTestDao {

    void insert(ForTestBean skuPermission);

    void update(ForTestBean skuPermission);

}
