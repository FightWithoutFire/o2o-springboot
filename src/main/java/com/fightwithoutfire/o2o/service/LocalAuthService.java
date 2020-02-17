package com.fightwithoutfire.o2o.service;

import com.fightwithoutfire.o2o.dto.LocalAuthExecution;
import com.fightwithoutfire.o2o.entity.LocalAuth;
import com.fightwithoutfire.o2o.exceptions.LocalAuthOperationException;

public interface LocalAuthService {
	LocalAuth getLocalAuthByUsernameAndPwd(String username, String password);
	LocalAuth getLocalAuthByUserId(long userId);
	LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;

	LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword)
			throws LocalAuthOperationException;

	LocalAuthExecution insertLocalAuth(LocalAuth localAuth, int userType)
			throws LocalAuthOperationException;
}
