package com.fightwithoutfire.o2o.service.impl;

import com.fightwithoutfire.o2o.dao.PersonInfoDao;
import com.fightwithoutfire.o2o.entity.PersonInfo;
import com.fightwithoutfire.o2o.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xxx
 * @create 2020-02-17 20:39
 */
@Service
public class PersonInfoServiceImpl implements PersonInfoService {

    @Autowired
    private PersonInfoDao personInfoDao;

    @Override
    public Long insertPersonInfo(PersonInfo personInfo) {
        return personInfoDao.insertPersonInfo(personInfo);
    }

    @Override
    public PersonInfo selectPersonInfoByUserId(Long userId) {
        return personInfoDao.selectPersonInfoByUserId(userId);
    }
}
