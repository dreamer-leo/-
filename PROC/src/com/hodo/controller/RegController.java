package com.hodo.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hodo.bean.Login;
import com.hodo.bean.User;
import com.hodo.common.base.SessionInfo;
import com.hodo.common.interceptor.shiro.ShiroRealm;
import com.hodo.common.util.DateUtil;
import com.hodo.common.util.Util;
import com.hodo.common.util.security.Md5Util;
import com.hodo.service.LoginServiceI;
import com.hodo.service.UserServiceI;


@Controller
@RequestMapping(value="")
public class RegController {
	
	@Autowired
	private LoginServiceI loginService;
	@Autowired
	private UserServiceI userService;
	/**
	 * 访问注册页面
	 * @return
	 */
	@RequestMapping(value="/toReg")
	public String toReg(Model model) {
		return "/system/reg";
	}
	
	/**
	 * 注册用户
	 */
	@RequestMapping(value="/doReg")
	public String doReg(Login login,Model model,HttpSession httpSession) {
		Login u = loginService.findLoginByCname(login.getCname());
		if(Util.isEmpty(u)) {
			Login loginDb = new Login();
			loginDb.setCname(login.getCname());
			
			Properties properties = new Properties();
	        try {
				properties.load(ShiroRealm.class.getResourceAsStream("/config.properties"));
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
	        String saltString = properties.getProperty("salt");
	        loginDb.setCpwd(Md5Util.md5(login.getCpwd(), saltString));
	        loginService.addLogin(loginDb);
	        
	        User userDb = new User();
			BeanUtils.copyProperties(login, userDb,"birthday");
			userDb.setCid(null);
			try {
				userDb.setBirthday(DateUtil.stringToDate(login.getBirthday(),"yyyy-MM-dd"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			userDb.setType("会员");
			userDb.setLogin(loginDb);
			userService.addUser(userDb);
		
			model.addAttribute("msg","注册成功!");			
			return "forward:/login";
		}
		return "/system/reg";
	}
	/**
	 * 注册-验证用户名是否已经存在
	 */
	@RequestMapping(value="/checkUserName",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String,Boolean> checkUserName(User user,Model model) {
		Map<String,Boolean> map = new HashMap<String,Boolean>();
		boolean bl = false;
		bl = userService.isValidUser(user.getCname());
		if (bl) {
			map.put("valid", false);
		} else {
			map.put("valid", true);
		}
		return map;
	}
}
