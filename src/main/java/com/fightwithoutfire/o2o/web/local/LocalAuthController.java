package com.fightwithoutfire.o2o.web.local;

import com.fightwithoutfire.o2o.dto.LocalAuthExecution;
import com.fightwithoutfire.o2o.entity.LocalAuth;
import com.fightwithoutfire.o2o.entity.PersonInfo;
import com.fightwithoutfire.o2o.enums.LocalAuthStateEnum;
import com.fightwithoutfire.o2o.exceptions.LocalAuthOperationException;
import com.fightwithoutfire.o2o.service.LocalAuthService;
import com.fightwithoutfire.o2o.service.PersonInfoService;
import com.fightwithoutfire.o2o.util.CodeUtil;
import com.fightwithoutfire.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/local")
public class LocalAuthController {

	@Autowired
	private LocalAuthService localAuthService;

	@Autowired
	private PersonInfoService personInfoService;
	
	@PostMapping(value = "/registeruser")
	private Map<String,Object> bindLocalAuth(HttpServletRequest request) throws LocalAuthOperationException{
		Map<String, Object> modelMap=new HashMap<String, Object>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success",false);
			modelMap.put("errMsg", "输入了错误的验证码");
		}
		String userName = HttpServletRequestUtil.getString(request, "userName");
		String password = HttpServletRequestUtil.getString(request, "password");
		int userType = HttpServletRequestUtil.getInt(request, "userType");
		if(userName != null && password != null) {
			LocalAuth localAuth=new LocalAuth();   
			localAuth.setUserName(userName);
			localAuth.setPassword(password);
			Date data= new Date();
			localAuth.setCreateTime(data);
			localAuth.setLastEditTime(data);
			LocalAuthExecution le = null;
			try {
				le = localAuthService.insertLocalAuth(localAuth, userType);
			if(userType == 1){
				modelMap.put("userType", 1);
			}else if(userType == 2){
				modelMap.put("userType", 2);
			}
			if(le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
				modelMap.put("success",true);
				request.getSession().setAttribute("user",
						personInfoService.selectPersonInfoByUserId(localAuth.getUserId()));
			}else {
				modelMap.put("success",false);
				modelMap.put("errMsg",le.getStateInfo());
			}
			}catch (LocalAuthOperationException e){
				modelMap.put("success",false);
				modelMap.put("errMsg",e.getMessage());
			}
		}else {
			modelMap.put("success",false);
			modelMap.put("errMsg", "用户名和密码均不能为空");
		}

		return modelMap;
		
	}
	
	@PostMapping(value = "/changelocalpwd")
	private Map<String,Object> changeLocalPwd(HttpServletRequest request){
		Map<String,Object> modelMap=new HashMap<String,Object>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success",false);
			modelMap.put("errMsg","输入了错误的验证码");
			return modelMap;
		}
		String userName=HttpServletRequestUtil.getString(request, "userName");
		String password=HttpServletRequestUtil.getString(request, "password");
		String newPassword=HttpServletRequestUtil.getString(request, "newPassword");
		PersonInfo user= (PersonInfo) request.getSession().getAttribute("user");
		if(userName!=null&&password!=null&&newPassword!=null&&user!=null&&user.getUserId()!=null
				&&!password.equals(newPassword)) {
			try {
				LocalAuth localAuth=localAuthService.getLocalAuthByUserId(user.getUserId());
				if(localAuth==null||!localAuth.getUserName().equals(userName)) {
					modelMap.put("success",false);
					modelMap.put("errMsg","输入的账号非本次登录的账号");
					return modelMap;
				}
				LocalAuthExecution le=localAuthService.modifyLocalAuth(user.getUserId(), userName, password, 
						newPassword);
				if(le.getState()==LocalAuthStateEnum.SUCCESS.getState()) {
					modelMap.put("success",true);
				}else {
					modelMap.put("success",false);
					modelMap.put("errMsg",le.getStateInfo());
				}
			}catch (LocalAuthOperationException e) {
				modelMap.put("success",false);
				modelMap.put("errMsg",e.toString());
				return modelMap;
			}
		}else {
			modelMap.put("success",false);
			modelMap.put("errMsg","请输入密码");
		}
		return modelMap;
	}
	
	@PostMapping(value = "/logincheck")
	private Map<String,Object> logincheck(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String, Object>();
		boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
		if(needVerify && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success",false);
			modelMap.put("errMsg","输入了错误的验证码");
			return modelMap;
		}
		String userName = HttpServletRequestUtil.getString(request, "userName");
		String password	= HttpServletRequestUtil.getString(request, "password");
		int userType = HttpServletRequestUtil.getInt(request, "userType");
		if(userName != null && password != null) {
			LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(userName, password);
			if(localAuth != null) {
				modelMap.put("success",true);
				PersonInfo personInfo = localAuth.getPersonInfo();
				if(userType == 1 && personInfo.getCustomerFlag() == 1){
					modelMap.put("userType" , 1);
				}else if(userType == 2 && personInfo.getShopOwnerFlag() == 1){
					modelMap.put("userType" , 2);
				}
				request.getSession().setAttribute("user", localAuth.getPersonInfo());
				return modelMap;
			}else {
				modelMap.put("success",false);
				modelMap.put("errMsg","用户名或密码错误");
			}
		}else {
			modelMap.put("success",false);
			modelMap.put("errMsg","用户名和密码均不能为空");
		}
		return modelMap;
	}
	
	@PostMapping(value = "/logout")
	private Map<String,Object> logout(HttpServletRequest request){
		Map<String,Object> modelMap=new HashMap<String, Object>();
		request.getSession().setAttribute("user", null);
		modelMap.put("success",true);
		return modelMap;
		
	}

	@GetMapping(value = "/getUserName")
	private Map<String, Object> getUserName(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<>();
		LocalAuth localAuth = (LocalAuth) request.getSession().getAttribute("user");
		if( localAuth != null)
			modelMap.put("userName", localAuth.getUserName());
		return modelMap;
	}
}
