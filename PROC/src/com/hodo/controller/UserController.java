package com.hodo.controller;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hodo.bean.Login;
import com.hodo.bean.User;
import com.hodo.common.base.Json;
import com.hodo.common.base.LayuiDataGrid;
import com.hodo.common.base.SessionInfo;
import com.hodo.common.interceptor.shiro.ShiroRealm;
import com.hodo.common.util.DateUtil;
import com.hodo.common.util.IdUtil;
import com.hodo.common.util.Util;
import com.hodo.common.util.mail.SendMail;
import com.hodo.common.util.security.Md5Util;
import com.hodo.service.LoginServiceI;
import com.hodo.service.UserServiceI;


@Controller
@RequestMapping(value="/user")
public class UserController {
	
	@Autowired
	private LoginServiceI loginService;
	@Autowired
	private UserServiceI userService;
	
	/**
	 * 新增/修改用Form
	 */
	@RequestMapping(value="/userform")
	public String userform(User user,Model model) {
		String cid=user.getCid();
		if(Util.isNotEmpty(cid)){
			User u=userService.findByCid(cid);
			if(Util.isNotEmpty(u.getBirthday())){
				u.setBirthdayString(DateUtil.dateToString(u.getBirthday(), "yyyy-MM-dd"));
			}
			model.addAttribute("user", u);
		}
		return "/system/user/userform";
	}
	
	/**
	 * 取回密码页面
	 * @return
	 */
	@RequestMapping(value="/toGetpwd")
	public String toReg(Model model) {
		return "/system/chgPwd";
	}

	
	/**
	 * 发送验证码
	 * @return
	 */
	@RequestMapping(value="/sentCode",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Json sentCode(Model model,Login login,HttpSession httpSession) {
		
		Json j = new Json();
		boolean b = false;
		String cname = login.getCname();
		try {
			if(!Util.isEmpty(cname)){
				User user = userService.findUserByLoginCname(cname);
				if(!Util.isEmpty(user)){
					String email = user.getEmail();
					SendMail s =new SendMail();
					//产生1000-9999随机数
					int a = (int)(Math.random()*(9999-1000+1))+1000;
					s.sendMail(email,String.valueOf(a));
					httpSession.setAttribute("code",String.valueOf(a));
					b=true;
				}
			}
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("您注册时的邮箱地址不正确，发送失败！");
			return j;
		}
		if(b) {
			j.setSuccess(true);
			j.setMsg("发送成功！请去您注册时的邮箱查看验证码并填写验证码，点击取回！");
		} else {
			j.setSuccess(false);
			j.setMsg("发送失败！");
		}
		return j;
	}
	
	/**
	 * 取回密码页面
	 * @return
	 */
	@RequestMapping(value="/doGetCode",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Json doGetCode(Model model,Login login,HttpSession httpSession) {
		Json j = new Json();
		boolean b = false;
		String cname = login.getCname();
		String cpwd = login.getCpwd();
		String validCode = login.getValidCode();
		if(!Util.isEmpty(cname) && !Util.isEmpty(cpwd) && !Util.isEmpty(validCode) && !Util.isEmpty(httpSession.getAttribute("code")) ){
			if(validCode.equals((String)httpSession.getAttribute("code"))) {
				Login loginDb = loginService.findLoginByCname(cname);
				
				Properties properties = new Properties();
	            try {
					properties.load(ShiroRealm.class.getResourceAsStream("/config.properties"));
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
	            String saltString = properties.getProperty("salt");
	            
	            loginDb.setCpwd(Md5Util.md5(cpwd, saltString));
				
				loginService.update(loginDb);
				
				b=true;
			}
		} else {
			j.setSuccess(false);
		}
		
		if(b) {
			j.setSuccess(true);
			j.setMsg("取回成功！");
		} else {
			j.setMsg("验证码不正确或者用户名不正确！");
			j.setSuccess(false);
		}
		return j;
	}
	/**
	 * 查询用户列表-跳转
	 */
	@RequestMapping(value="/toUser")
	public String user(User user,Model model) {
		return "/system/user/user";
	}
	/**
	 * 查询用户列表
	 */
	@RequestMapping(value="/userList",produces="application/json;charset=UTF-8")
	@ResponseBody
	public LayuiDataGrid userList(User user,Model model) {
		LayuiDataGrid dataGrid = userService.datagrid(user);
		return dataGrid;
	}
	/**
	 * 用户选择页面
	 */
	@RequestMapping(value="/userSelect")
	public String userSelect(){
		return "/system/user/userSelect";
	}
	
	/**
	 * 修改密码-跳转
	 * @return
	 */
	@RequestMapping(value="/toChgPwd")
	public String toChgPwd(Model model) {
		return "/system/user/chgPwd";
	}
	/**
	 * 修改密码-处理
	 */
	@RequestMapping(value="/chgPwd",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Json chgPwd(User user,Model model) {
		Json j = new Json();
		boolean b = false;
		if(!Util.isEmpty(user.getCid()) && !Util.isEmpty(user.getCpwd())) {
			//user.setCpwd(Encrypt.e(user.getCpwd()));
			user.setCpwd(user.getCpwd());
			b = userService.chgPwd(user);
		}
		if(b) {
			j.setSuccess(true);
			j.setMsg("修改成功！");
		} else {
			j.setSuccess(false);
			j.setMsg("修改失败！");
		}
		return j;
	}
	/**
	 * 
	 */
	@RequestMapping(value="/getStaffCombox",produces="application/json;charset=UTF-8")
	@ResponseBody
	public List<User> getStaffCombox() {
		List<User> userList = userService.findAll();
		return userList;
	}
	/**
	 * 修改-跳转
	 * @return
	 */
	@RequestMapping(value="/toEdit")
	public String toEdit(User user,Model model) {
		User u = userService.findByCid(user.getCid());
		model.addAttribute("user", u);
		return "/system/user/userform";
	}
	
	/**
	 * 修改-处理
	 */
	@RequestMapping(value="/edit",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Json edit(User user,Model model) {
		Json j = new Json();
		boolean b = false;
		
		String cid = user.getCid();
		if(!Util.isEmpty(cid)){
			User userDb = userService.findByCid(cid);
			if(!Util.isEmpty(user.getCpwd())){
				Login loginDb= userDb.getLogin();
				Properties properties = new Properties();
	            
				try {
					properties.load(ShiroRealm.class.getResourceAsStream("/config.properties"));
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
	            String saltString = properties.getProperty("salt");
	            
				loginDb.setCpwd(Md5Util.md5(user.getCpwd(), saltString));
				
				loginService.update(loginDb);
				
				userDb.setLogin(loginDb);
			}
			userDb.setCrealname(user.getCrealname());
			userDb.setStudentId(user.getStudentId());
			userDb.setDept(user.getDept());
			userDb.setMajor(user.getMajor());
			userDb.setEmail(user.getEmail());
			userDb.setBirthday(user.getBirthday());
			userDb.setHobby(user.getHobby());
			userDb.setType(user.getType());
			
			userService.update(userDb);
			b = true;
		}
		if(b) {
			j.setSuccess(true);
			j.setMsg("修改成功！");
		} else {
			j.setSuccess(false);
			j.setMsg("修改失败！");
		}
		return j;
	}
	
	
	/**
	 * 新增-跳转
	 * @return
	 */
	@RequestMapping(value="/toAdd")
	public String toAdd(User user,Model model) {
		return "/system/user/add";
	}
	/**
	 * 新增-处理
	 */
	@RequestMapping(value="/add",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Json add(User user,Model model) {
		Json j = new Json();
		boolean b = false;
		
		if(Util.isEmpty(user.getCid())){
			
		
			Login loginDb= new Login();
			String loginCid = UUID.randomUUID().toString();
			loginDb.setCid(loginCid);
			loginDb.setCname(user.getCname());
			
			Properties properties = new Properties();
            
			try {
				properties.load(ShiroRealm.class.getResourceAsStream("/config.properties"));
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
            String saltString = properties.getProperty("salt");
            
			loginDb.setCpwd(Md5Util.md5(user.getCpwd(), saltString));
			
			loginService.addLogin(loginDb);
			
			
			User userDb = new User();
			userDb.setCid(UUID.randomUUID().toString());
			userDb.setLogin(loginDb);
			userDb.setIcon(user.getIcon());
			userDb.setCrealname(user.getCrealname());
			userDb.setStudentId(user.getStudentId());
			userDb.setDept(user.getDept());
			userDb.setMajor(user.getMajor());
			userDb.setEmail(user.getEmail());
			userDb.setBirthday(user.getBirthday());
			userDb.setHobby(user.getHobby());
			userDb.setType(user.getType());
			
			userService.addUser(userDb);
			b = true;
		}
		if(b) {
			j.setSuccess(true);
			j.setMsg("修改成功！");
		} else {
			j.setSuccess(false);
			j.setMsg("修改失败！");
		}
		return j;
	}
	/**
	 * 删除-处理
	 */
	@RequestMapping(value="/del",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Json del(User user,Model model) {
		Json j = new Json();
		try {
			userService.delete(user);
			
			j.setSuccess(true);
			j.setMsg("删除成功!");
		} catch (Exception e) {
			j.setMsg("删除失败!");
		}
		return j;
	}
	// 修改我的信息
			@RequestMapping(value = "/myInfoEdit",produces="application/json;charset=UTF-8")
			@ResponseBody
			public Json myInfoDdit(User user,HttpServletRequest request, HttpServletResponse response) throws Exception {
				Json j = new Json();
				boolean b = false;
				
				String cid = user.getCid();
				if(!Util.isEmpty(cid)){
					User userDb = userService.findByCid(cid);
					if(!Util.isEmpty(user.getCpwd())){
						Login loginDb= userDb.getLogin();
						Properties properties = new Properties();
			            
						try {
							properties.load(ShiroRealm.class.getResourceAsStream("/config.properties"));
						} catch (IOException e) {
							System.out.println(e.getMessage());
						}
			            String saltString = properties.getProperty("salt");
			            
						loginDb.setCpwd(Md5Util.md5(user.getCpwd(), saltString));
						
						loginService.update(loginDb);
						
						userDb.setLogin(loginDb);
					}
					userDb.setCrealname(user.getCrealname());
					userDb.setStudentId(user.getStudentId());
					userDb.setDept(user.getDept());
					userDb.setMajor(user.getMajor());
					userDb.setEmail(user.getEmail());
					userDb.setBirthday(user.getBirthday());
					userDb.setHobby(user.getHobby());
					
					userService.update(userDb);
					b = true;
				}
				if(b) {
					j.setSuccess(true);
					j.setMsg("修改成功！");
				} else {
					j.setSuccess(false);
					j.setMsg("修改失败！");
				}
				return j;
			}
		//头像上传
		@RequestMapping(value = "/uploadIcon",produces="application/json;charset=UTF-8")
		@ResponseBody
		public Json uploadIcon(@RequestParam(value="file",defaultValue="") MultipartFile file,User user,HttpSession httpSession,HttpServletResponse response
			) throws Exception {
			Json j = new Json();
			boolean b = false;
			
			String cid = user.getCid();
			if(!Util.isEmpty(cid)){
				// 获取存取路径-源文件
				String filePath = httpSession.getServletContext().getRealPath("/") + "icon/";
				if (!file.isEmpty()) {
					try {
						//上传的文件名
						String oriName = file.getOriginalFilename();
						//文件扩展名
						String suffix = oriName.substring(oriName.lastIndexOf(".") + 1);
						
						// 获取存取路径
						String fileAndPath = filePath + oriName;
						
						//新文件名
						String fileNameNew = IdUtil.generateSequenceNo()+"."+suffix;
						
						// 获取存取路径
						String fileAndPathNew = filePath + fileNameNew;
						
						// 转存文件
						File newFile = new File(fileAndPathNew);
						if(!newFile.exists()) {
						//如果文件不存在
							//上传文件到指定路径
							file.transferTo(new File(fileAndPathNew));
							
							User userDB = userService.findByCid(cid);
							//删除原来文件
							if(!Util.isEmpty(userDB.getIcon())){
								// 获取存取路径
								String fileAndPathDel = httpSession.getServletContext().getRealPath("/") + userDB.getIcon();
								// 转存文件
								File fileDel = new File(fileAndPathDel);
								if(fileDel.isFile()){
									fileDel.delete();
								}
							}
							userDB.setIcon("icon"+"/"+fileNameNew);
							//歌曲状态（2:未上传 1:已上传 0:下架）
							userService.update(userDB);
							b=true;
							j.setObj("icon"+"/"+fileNameNew);
							j.setMsg("上传成功！");
						}  else {
							b=false;
							j.setMsg("该图片已经存在！");
						}
					} catch (IOException e) {
						b=false;
						j.setMsg("上传失败！");
					}
				}
				
				b = true;
			} else {
				// 获取存取路径-源文件
				String filePath = httpSession.getServletContext().getRealPath("/") + "icon/";
				if (!file.isEmpty()) {
					try {
						//上传的文件名
						String oriName = file.getOriginalFilename();
						//文件扩展名
						String suffix = oriName.substring(oriName.lastIndexOf(".") + 1);
						
						// 获取存取路径
						String fileAndPath = filePath + oriName;
						
						//新文件名
						String fileNameNew = IdUtil.generateSequenceNo()+"."+suffix;
						
						// 获取存取路径
						String fileAndPathNew = filePath + fileNameNew;
						
						// 转存文件
						File newFile = new File(fileAndPathNew);
						if(!newFile.exists()) {
						//如果文件不存在
							//上传文件到指定路径
							file.transferTo(new File(fileAndPathNew));
							
							b=true;
							j.setMsg("上传成功！");
							j.setObj("icon"+"/"+fileNameNew);
						}  else {
							b=false;
							j.setMsg("该图片已经存在！");
						}
					} catch (IOException e) {
						b=false;
						j.setMsg("上传失败！");
					}
				}
				
				b = true;
			}
			if(b) {
				j.setSuccess(true);
				j.setMsg("上传成功！");
			} else {
				j.setSuccess(false);
				j.setMsg("上传失败！");
			}
			return j;
		}
}
