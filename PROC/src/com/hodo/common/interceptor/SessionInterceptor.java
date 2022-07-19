package com.hodo.common.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class SessionInterceptor implements HandlerInterceptor {

	//响应用户请求（调用控制器对应方法）之前执行
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res,
			Object obj) throws Exception {
		// TODO Auto-generated method stub
		HttpSession session=req.getSession();
		if(session.getAttribute("session_admin")==null&&session.getAttribute("session_user")==null){
			req.setAttribute("errorObjName", "");
			req.setAttribute("errorMsg", "用户未登录");
			req.getRequestDispatcher("/error/error.jsp").forward(req, res);
			return false;
		}else{
			return true;
		}
	}
	//当控制器返回结果时（请求转发到某个页面前）执行
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub

	}
	
	//当View将结果返回给用户之前执行
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	



}
