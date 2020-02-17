package com.fightwithoutfire.o2o.interceptor.shopadmin;

import com.fightwithoutfire.o2o.entity.PersonInfo;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class ShopLoginInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handle) 
			throws Exception{
		Object userObj=request.getSession().getAttribute("user");
		if(userObj!=null) {
			PersonInfo user=(PersonInfo)userObj;
			if(user!=null&&user.getUserId()!=null&&user.getUserId()>0&&user.getEnableStatus()==1) {
				return true;
			}
		}
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<script>");
		out.println("window.open("+request.getContextPath()+"/local/login?userType=2','_self')");
		out.println("</script>");
		out.println("</html>");
		return false;
	}
}
