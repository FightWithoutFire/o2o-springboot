package com.fightwithoutfire.o2o.dao;

import com.fightwithoutfire.o2o.entity.PersonInfo;

/**
 * @author xxx
 * @create 2020-02-17 20:23
 */
public interface PersonInfoDao {
    Long insertPersonInfo(PersonInfo PersonInfo);

    PersonInfo selectPersonInfoByUserId(Long userId);
}
