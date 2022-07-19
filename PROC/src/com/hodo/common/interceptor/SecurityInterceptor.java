package com.hodo.common.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonArray;
import com.hodo.common.base.Json;
import com.hodo.common.util.Util;

public class SecurityInterceptor implements HandlerInterceptor {  
	  
    private static final String LOGIN_URL = "/login"; 
    
    @Override  
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {  
        HttpSession session = req.getSession(true);  
        // 从session 里面获取用户名的信息  
        Object sessionInfo = session.getAttribute("sessionInfo");
        
        //判断是否为ajax请求，默认不是  
        boolean isAjaxRequest = false;  
        if(!Util.isEmpty(req.getHeader("x-requested-with")) && req.getHeader("x-requested-with").equals("XMLHttpRequest")){  
            isAjaxRequest = true;  
        }  
        
        // 判断如果没有取到用户信息，就跳转到登陆页面，提示用户进行登陆  
        if (Util.isEmpty(sessionInfo)) { 
        	 if(isAjaxRequest){// 如果是ajax请求，则不是跳转页面，使用response返回结果  
                 res.setHeader("noAuthentication", "true"); 
                 Json j = new Json();
                 j.setSuccess(false);
                 j.setMsg("登录已失效，请刷新页面或重新登录！");
                 String jsonString = com.alibaba.fastjson.JSON.toJSONString(j);
                 res.setContentType("application/json;charset=UTF-8");  
                 PrintWriter writer = res.getWriter();  
                 writer.write(jsonString);  
                 writer.close();  
                 res.flushBuffer();  
             }else{  
            		String proUrl = "http://" + req.getServerName() + ":"
            			+ req.getServerPort()
            			+ req.getContextPath();
                    res.sendRedirect(proUrl+LOGIN_URL);
             }  
            return false;
        }
        return true;  
    }  
  
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

  
}  