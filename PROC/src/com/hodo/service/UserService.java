package com.hodo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hodo.bean.Login;
import com.hodo.bean.User;
import com.hodo.common.base.LayuiDataGrid;
import com.hodo.common.interceptor.shiro.ShiroRealm;
import com.hodo.common.util.Util;
import com.hodo.common.util.security.Md5Util;
import com.hodo.dao.BaseDaoI;

@Service(value = "userService")
public class UserService implements UserServiceI {


	@Autowired
	private BaseDaoI<User> userDao;
	@Autowired
	private BaseDaoI<Login> loginDao;
	
	@Override
	public LayuiDataGrid datagrid(User user) {
		String hql=" from User t where 1=1";
		LayuiDataGrid j = new LayuiDataGrid();
		j.setData(editUsers(find(user,hql)));
		j.setCount(total(user,hql));
		j.setCode(0L);
		return j;
	}
	private List<User> editUsers(List<User> userList) {
		if (userList != null && userList.size() > 0) {
			for (User u: userList) {
				u.setCname(u.getLogin().getCname());

			}
		}
		return userList;
	}
	private List<User> find(User user,String hql) {
		hql = addWhere(user, hql);
		List<User> userList = userDao.find(hql, user.getPage(), user.getLimit());
		return userList;
	}
	private Long total(User user,String hql) {
		hql = addWhere(user, hql);
		hql = "select count(*) "+hql;
		return userDao.count(hql);
	}
	private String addWhere(User user, String hql) {
		if(!Util.isEmpty(user.getType())){
			hql += " and t.type='" + user.getType() + "' ";
		}
		if(!Util.isEmpty(user.getCname())){
			hql += " and t.login.cname like '%"+user.getCname()+"%' ";
		}
		if(!Util.isEmpty(user.getCrealname())){
			hql += " and t.crealname like '%"+user.getCrealname()+"%' ";
		}
		return hql;
	}

	@Override
	public void addUser(User user) {
		userDao.save(user);
	}

	@Override
	public boolean isValidUser(String cname) {
		if (!Util.isEmpty(cname)) {
			List<Object> paramList = new ArrayList<Object>();
			paramList.add(cname);
			User user = userDao.get("from User t where t.login.cname=? ", paramList);
			if (Util.isEmpty(user)) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public User findUserByLoginCid(String loginCid) {
		if (!Util.isEmpty(loginCid)) {
			List<Object> paramList = new ArrayList<Object>();
			paramList.add(loginCid);
			User user = userDao.get("from User t where t.login.cid=? ", paramList);
			if (Util.isEmpty(user)) {
				return null;
			} else {
				return user;
			}
		}
		return null;
	}

	@Override
	public boolean chgPwd(User user) {
		User userDb = userDao.get(User.class, user.getCid());
		if(!Util.isEmpty(userDb)){
			Properties properties = new Properties();
	        try {
				properties.load(ShiroRealm.class.getResourceAsStream("/config.properties"));
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
	        String saltString = properties.getProperty("salt");
			
			userDb.setCpwd(Md5Util.md5(user.getCpwd(), saltString));
			userDao.update(userDb);
			return true;
		} else {
			return false;
		}
		
	}

	@Override
	public int update(User user) {
		User userDb = userDao.get(User.class, user.getCid());
		userDao.update(userDb);
		int r= 1;
		return r;
	}

	@Override
	public void delete(User user) {
		String ids = user.getIds();
		if (!Util.isEmpty(ids)) {
			String stringArray[] = ids.split(",");
			for (String str : stringArray) {
				if (!Util.isEmpty(str)) {
					String cid = str.trim();
					User userDb = userDao.get(User.class, cid);
					Login login = loginDao.get(Login.class,userDb.getLogin().getCid());
					userDao.delete(userDb);
					loginDao.delete(login);
				}
			}
		}
	}

	@Override
	public User findByCid(String cid) {
		if (!Util.isEmpty(cid)) {
			User user = userDao.get(User.class, cid);
			if (Util.isEmpty(user)) {
				return null;
			} else {
				return user;
			}
		}
		return null;
	}

	@Override
	public List<User> findAll() {
		List<User> userList = userDao.find("from User");
		return userList;
	}
	@Override
	public User findUserByLoginCname(String cname) {
		if (!Util.isEmpty(cname)) {
			List<Object> paramList = new ArrayList<Object>();
			paramList.add(cname);
			User user = userDao.get("from User t where t.login.cname=? ", paramList);
			if (Util.isEmpty(user)) {
				return null;
			} else {
				return user;
			}
		}
		return null;
	}
}
