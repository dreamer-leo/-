package com.hodo.controller;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hodo.bean.User;
import com.hodo.common.util.Util;

@Controller
@RequestMapping(value = "")
public class LoginController {

	/**
	 * 访问登录页
	 * @return
	 */
	@RequestMapping(value = "/login")
	public String toLogin(Model model) {
		return "/system/login";
	}

	
}
