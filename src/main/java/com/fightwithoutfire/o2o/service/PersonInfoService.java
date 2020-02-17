package com.fightwithoutfire.o2o.service;

import com.fightwithoutfire.o2o.entity.PersonInfo;

/**
 * @author xxx
 * @create 2020-02-17 20:39
 */
public interface PersonInfoService {
    Long insertPersonInfo(PersonInfo personInfo);

    PersonInfo selectPersonInfoByUserId(Long userId);
}
