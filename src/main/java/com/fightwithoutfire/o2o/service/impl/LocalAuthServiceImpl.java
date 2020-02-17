package com.fightwithoutfire.o2o.service.impl;

import com.fightwithoutfire.o2o.dao.LocalAuthDao;
import com.fightwithoutfire.o2o.dao.PersonInfoDao;
import com.fightwithoutfire.o2o.dto.LocalAuthExecution;
import com.fightwithoutfire.o2o.entity.LocalAuth;
import com.fightwithoutfire.o2o.entity.PersonInfo;
import com.fightwithoutfire.o2o.enums.LocalAuthStateEnum;
import com.fightwithoutfire.o2o.exceptions.LocalAuthOperationException;
import com.fightwithoutfire.o2o.service.LocalAuthService;
import com.fightwithoutfire.o2o.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {

	@Autowired
	private LocalAuthDao localAuthDao;

	@Autowired
	private PersonInfoDao personInfoDao;

	@Override
	public LocalAuth getLocalAuthByUsernameAndPwd(String username, String password) {
		// TODO Auto-generated method stub
		//return localAuthDao.queryLocalByUserNameAndPwd(username, MD5.getMd5(password));
		return localAuthDao.queryLocalByUserNameAndPwd(username, password);
	}

	@Override
	public LocalAuth getLocalAuthByUserId(long userId) {
		// TODO Auto-generated method stub
		return localAuthDao.queryLocalByUserId(userId);
	}

	@Override
	@Transactional
	public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
		// TODO Auto-generated method stub
		if(localAuth==null||localAuth.getPassword()==null||localAuth.getUserName()==null
				||localAuth.getPersonInfo()==null||localAuth.getPersonInfo().getUserId()==null) {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		LocalAuth tempLocalAuth = localAuthDao.queryLocalByUserId(localAuth.getPersonInfo().getUserId());
		if(tempLocalAuth!=null) {
			return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
		}
		try {
			localAuth.setCreateTime(new Date());
			localAuth.setLastEditTime(new Date());
			localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
			int effectedNum=localAuthDao.insertLocalAuth(localAuth);
			if(effectedNum <= 0) {
				throw new LocalAuthOperationException("账号绑定失败");
			}else {
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS,localAuth);
			}
		}catch (Exception e) {
			throw new LocalAuthOperationException("insertLocalAuth error:"+e.getMessage());
		}
			
	}

	@Override
	@Transactional
	public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword) throws LocalAuthOperationException {
		if(userId!=null&&username!=null&&password!=null&&newPassword!=null
				&&!password.equals(newPassword)) {
			try {
				int effectedNum=localAuthDao.updateLocalAuth(userId, username, MD5.getMd5(password), MD5.getMd5(newPassword), new Date());
				if(effectedNum<=0) {
					throw new LocalAuthOperationException("更新密码失败");
				}else {
					return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
				}
			}catch(Exception e){
				throw new LocalAuthOperationException("更新密码失败:"+e.getMessage());
			}
		}else {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
	}

	@Override
	public LocalAuthExecution insertLocalAuth(LocalAuth localAuth, int userType) throws LocalAuthOperationException {
		if(localAuth != null && localAuth.getPassword() != null && localAuth.getUserName() != null) {
			try {

				PersonInfo personInfo = new PersonInfo();
				personInfo.setName(localAuth.getUserName());
				if(userType == 1){
					personInfo.setCustomerFlag(1);
				}else if(userType == 2){
					personInfo.setShopOwnerFlag(1);
				}
				personInfo.setCreateTime(new Date());
				personInfo.setLastEditTime(new Date());
				personInfo.setEnableStatus(1);
				personInfoDao.insertPersonInfo(personInfo);
				localAuth.setUserId(personInfo.getUserId());
				localAuth.setCreateTime(new Date());
				localAuth.setLastEditTime(new Date());
				int effectedNum=localAuthDao.insertLocalAuth(localAuth);
				if(effectedNum<=0) {
					throw new LocalAuthOperationException("注册失败");
				}else {
					return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
				}
			}catch(Exception e){
				throw new LocalAuthOperationException("注册失败:"+e.getMessage());
			}
		}else {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
	}

}
