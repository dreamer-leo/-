package com.hodo.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hodo.bean.CFile;
import com.hodo.bean.Cls;
import com.hodo.bean.Login;
import com.hodo.bean.PartPath;
import com.hodo.bean.User;
import com.hodo.common.base.Const;
import com.hodo.common.base.Json;
import com.hodo.common.base.LayuiDataGrid;
import com.hodo.common.base.SessionInfo;
import com.hodo.common.util.Util;
import com.hodo.common.util.security.ShiroUtil;
import com.hodo.service.CFileServiceI;
import com.hodo.service.ClsService;
import com.hodo.service.ClsServiceI;
import com.hodo.service.UserServiceI;

@Controller
@RequestMapping(value = "/back")
public class BackstageController {
	@Autowired
	private UserServiceI userService;
	@Autowired
	private CFileServiceI cFileService;
	@Autowired
	private ClsServiceI clsService;
	/**
	 * 请求登录，验证用户
	 */
	@RequestMapping(value = "/doLogin")
	public String doLogin(Login login, Model model, HttpSession httpSession) {
		// 检验用户名
		if (Util.isEmpty(login.getCname())) {
			model.addAttribute("msg", "提示：请输入用户名！");
			return "/system/login";
		}
		// 检验密码
		if (Util.isEmpty(login.getCpwd())) {
			model.addAttribute("msg", "提示：请输入用户密码！");
			return "/system/login";
		}
		// 检验验证码
		String codeString = login.getValidCode();
		String kaptcha = (String) httpSession.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		if (!Util.isEmpty(kaptcha)) {
			if (!kaptcha.equals(codeString)) {
				model.addAttribute("msg", "验证码输入错误，请重新输入");
				return "/system/login";
			} else {
				httpSession.removeAttribute("com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY");
			}
		} else {
			return "/system/login";
		}
		// shiro加入身份验证
		// shiro管理的session
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(login.getCname(), login.getCpwd());
		if ("on".equals(login.getRememberMe())) {
			token.setRememberMe(true);
		} else {
			token.setRememberMe(false);
		}
		try {
			if (!currentUser.isAuthenticated()) {
				currentUser.login(token);
			}
		} catch (UnknownAccountException e) {
			model.addAttribute("msg", "提示：用户名或密码有误！");
		} catch (IncorrectCredentialsException ice) {
			model.addAttribute("msg", "提示：密码错误！");
		} catch (LockedAccountException lae) {
			model.addAttribute("msg", "提示：未激活！");
		} catch (ExcessiveAttemptsException eae) {
			model.addAttribute("msg", "提示：错误次数过多！");
		} catch (AuthenticationException ae) {
			model.addAttribute("msg", "提示：验证未通过！");
		}

		// 登录未成功
		if (!currentUser.isAuthenticated()) {
			token.clear();
			return "/system/login";
		} else {
			// return "forward:/back/home";
			// return "redirect:/index";
			return "forward:/back/home";
		}
	}

	// 跳转到主页，获取菜单并授权
	@RequestMapping(value = "/home")
	public String home(Model model, CFile cf,HttpSession session) {
		Login user = ShiroUtil.getCurrentUser();
		SessionInfo sessionInfo = ShiroUtil.getCurrentInfo();
		if (!Util.isEmpty(user)) {
			session.setAttribute(Const.SESSION_USER, user);
			session.setAttribute(Const.SESSION_INFO, sessionInfo);
			
			//父亲路径
			List<PartPath> ppList = new ArrayList<PartPath>();
			
			//当前路径
			String nowPath = Util.isEmpty(sessionInfo.getNowPath())?"":sessionInfo.getNowPath();
			sessionInfo.setNowPath(nowPath);
			
			//当前路径
			if(!Util.isEmpty(cf.getCid())) {
				//点击的文件夹
				CFile c= cFileService.findByCid(cf.getCid());
				sessionInfo.setPartPathCid(cf.getCid());
				sessionInfo.setNowPath(c.getUrl());
				model.addAttribute("nowDirName", c.getName());
				
				while(!Util.isEmpty(c.getPartDir())){
					c = cFileService.findByCid(c.getPartDir().getCid());
					PartPath pp = new PartPath();
					pp.setCid(c.getCid());pp.setName(c.getName());
					ppList.add(pp);
				}
				
			} else {
				sessionInfo.setPartPathCid("");
				sessionInfo.setNowPath("");
			}
			//反转
			Collections.reverse(ppList);
			model.addAttribute("ppList", ppList);

			//用户名
			User u = userService.findUserByLoginCid(user.getCid());
			model.addAttribute("user", u);
			sessionInfo.setRealName(u.getCrealname());
			sessionInfo.setIcon(u.getIcon());
			
			//列表数据
			cf.setCrtUser(u.getCid());
			LayuiDataGrid dataGrid = cFileService.datagrid(cf,session);
			model.addAttribute("dataGrid", dataGrid);
			
			//分类
			List<Cls> clsList = clsService.findAll();
			model.addAttribute("clsList", clsList);
			
			//用户
			List<User> userList = userService.findAll();
			model.addAttribute("userList", userList);
		}
		return "/system/main/main";
	}
	
	// 搜索
	@RequestMapping(value = "/search")
	public String search(Model model, CFile cf,HttpSession session) {
		Login user =ShiroUtil.getCurrentUser();
		SessionInfo sessionInfo = ShiroUtil.getCurrentInfo();
		if (!Util.isEmpty(user)) {
			session.setAttribute(Const.SESSION_USER, user);
			session.setAttribute(Const.SESSION_INFO, sessionInfo);
			//列表数据
			LayuiDataGrid dataGrid = cFileService.datagrid(cf,session);
			model.addAttribute("dataGrid", dataGrid);
		}
		//用户名
		User u = userService.findUserByLoginCid(user.getCid());
		model.addAttribute("user", u);
		sessionInfo.setRealName(u.getCrealname());
		
		return "/system/main/main";
	}
	// 类别
	@RequestMapping(value = "/searchByType")
	public String searchByType(Model model, CFile cf,HttpSession session) {
			Login user =ShiroUtil.getCurrentUser();
			SessionInfo sessionInfo = ShiroUtil.getCurrentInfo();
			if (!Util.isEmpty(user)) {
				session.setAttribute(Const.SESSION_USER, user);
				session.setAttribute(Const.SESSION_INFO, sessionInfo);
				//列表数据
				LayuiDataGrid dataGrid = cFileService.datagrid(cf,session);
				model.addAttribute("dataGrid", dataGrid);
			}
			//用户名
			User u = userService.findUserByLoginCid(user.getCid());
			model.addAttribute("user", u);
			sessionInfo.setRealName(u.getCrealname());
			
			//分类
			List<Cls> clsList = clsService.findAll();
			model.addAttribute("clsList", clsList);
			
			//用户
			List<User> userList = userService.findAll();
			model.addAttribute("userList", userList);
			return "/system/main/main";
	}
	//分类
	@RequestMapping(value = "/searchByClsId")
	public String searchByClsId(Model model, CFile cf,HttpSession session) {
		
		Login user =ShiroUtil.getCurrentUser();
		SessionInfo sessionInfo = ShiroUtil.getCurrentInfo();
		if (!Util.isEmpty(user)) {
			session.setAttribute(Const.SESSION_USER, user);
			session.setAttribute(Const.SESSION_INFO, sessionInfo);
			//列表数据
			LayuiDataGrid dataGrid = cFileService.datagrid(cf,session);
			model.addAttribute("dataGrid", dataGrid);
		}
		//用户名
		User u = userService.findUserByLoginCid(user.getCid());
		model.addAttribute("user", u);
		sessionInfo.setRealName(u.getCrealname());
		
		//分类
		List<Cls> clsList = clsService.findAll();
		model.addAttribute("clsList", clsList);
		
		//用户
		List<User> userList = userService.findAll();
		model.addAttribute("userList", userList);
		return "/system/main/main";
	}
	/**
	 * 改变排序
	 */
	@RequestMapping(value = "/chgOrder", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json chgOrder(HttpSession session) {
		Json j = new Json();
		boolean b = false;
		
		Object obj = session.getAttribute(Const.SESSION_INFO);
		if(Util.isNotEmpty(obj)){
			SessionInfo si=(SessionInfo)obj;
			String order = si.getOrder();
			if("asc".equals(order)){
				si.setOrder("desc");
			} else if("desc".equals(order)) {
				si.setOrder("asc");
			} else {
				si.setOrder("asc");
			}
			b = true;
		}
		if (b) {
			j.setSuccess(true);
			j.setMsg("创建成功！");
		} else {
			j.setSuccess(false);
			j.setMsg("创建失败！");
		}
		return j;
	}
	
	/**
	 * 设置显示session
	 */
	@RequestMapping(value = "/setShowType", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json setShowType(HttpSession session,String type) {
		Json j = new Json();
		boolean b = false;
		
		Object obj = session.getAttribute(Const.SESSION_INFO);
		
		if(Util.isNotEmpty(obj) && Util.isNotEmpty(type)){
			SessionInfo si=(SessionInfo)obj;
			si.setShowType(type);
			b = true;
		}
		if (b) {
			j.setSuccess(true);
			j.setMsg("设置成功！");
		} else {
			j.setSuccess(false);
			j.setMsg("设置失败！");
		}
		return j;
	}
	@RequestMapping("/toEditMyInfo")
	public String toEditMyInfo(Login login, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		SessionInfo sessionInfo = ShiroUtil.getCurrentInfo();
		String loginId = sessionInfo.getUserId();
		User r = userService.findUserByLoginCid(loginId);
		model.addAttribute("user", r);
		return "/system/myInfoEdit";
	}
	@RequestMapping("/toUploadWin")
	public String toUploadWin(Login login, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		SessionInfo sessionInfo = ShiroUtil.getCurrentInfo();
		String loginId = sessionInfo.getUserId();
		User r = userService.findUserByLoginCid(loginId);
		
		model.addAttribute("user", r);
		
		List<Cls> clsList = clsService.findAll();
		model.addAttribute("clsList", clsList);
		
		return "/system/uploadWin";
	}
	@RequestMapping("/toMusicAndVideo")
	public String toMusicAndVideo(CFile cf, Model model, HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("p", cf.getFilePath());
		return "/system/main/mvDetail";
	}
	@RequestMapping("/toWordDetail")
	public String toWordDetail(CFile cf, Model model, HttpServletRequest request, HttpServletResponse response) {
		//路径
		String path = cf.getFilePath();
		path = path.replaceAll("\\\\", "/");
		model.addAttribute("p", path);
		//文件类型
		String officeType = cf.getOfficeType();
		model.addAttribute("officeType", officeType);
		return "/system/main/wordDetail";
	}
	@RequestMapping("/toSaveFile")
	public String toSaveFile(CFile cf, Model model, HttpServletRequest request, HttpServletResponse response) {
		
		model.addAttribute("p", cf.getFilePath());
		
		return "/system/main/SaveFile";
	}
}
