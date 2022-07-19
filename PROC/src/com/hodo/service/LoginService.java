package com.hodo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hodo.bean.Login;

import com.hodo.common.base.Encrypt;
import com.hodo.common.base.LayuiDataGrid;
import com.hodo.common.util.Util;
import com.hodo.dao.BaseDaoI;

@Service(value = "loginService")
public class LoginService implements LoginServiceI {


	@Autowired
	private BaseDaoI<Login> loginDao;
	
	@Override
	public LayuiDataGrid datagrid(Login login) {
		String hql=" from Login t where 1=1";
		LayuiDataGrid j = new LayuiDataGrid();
		j.setData(editLogins(find(login,hql)));
		j.setCount(total(login,hql));
		j.setCode(0L);
		return j;
	}
	private List<Login> editLogins(List<Login> loginList) {
		if (loginList != null && loginList.size() > 0) {
			for (Login logins: loginList) {
			}
		}
		return loginList;
	}
	private List<Login> find(Login login,String hql) {
		hql = addWhere(login, hql);
		List<Login> loginList = loginDao.find(hql, login.getPage(), login.getLimit());
		return loginList;
	}
	private Long total(Login login,String hql) {
		hql = addWhere(login, hql);
		hql = "select count(*) "+hql;
		return loginDao.count(hql);
	}
	private String addWhere(Login login, String hql) {
		return hql;
	}

	@Override
	public void addLogin(Login login) {
		loginDao.save(login);
	}
	@Override
	public Login findLoginOne(Login login) {
		login.setCpwd(Encrypt.e(login.getCpwd()));
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(login.getCname());
		paramList.add(login.getCpwd());
		Login loginDb = loginDao.get("from Login where cname=? and cpwd=? ", paramList);
		return loginDb;
	}
	@Override
	public boolean isValidLogin(String cname) {
		if (!Util.isEmpty(cname)) {
			List<Object> paramList = new ArrayList<Object>();
			paramList.add(cname);
			Login login = loginDao.get("from Login where cname=? ", paramList);
			if (Util.isEmpty(login)) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public Login findLoginByCname(String cname) {
		if (!Util.isEmpty(cname)) {
			List<Object> paramList = new ArrayList<Object>();
			paramList.add(cname);
			Login login = loginDao.get("from Login where cname=? ", paramList);
			if (Util.isEmpty(login)) {
				return null;
			} else {
				return login;
			}
		}
		return null;
	}

	@Override
	public boolean chgPwd(Login login) {
		Login loginDb = loginDao.get(Login.class, login.getCid());
		if(!Util.isEmpty(loginDb)){
			loginDb.setCpwd(Encrypt.e(login.getCpwd()));
			loginDao.update(loginDb);
			return true;
		} else {
			return false;
		}
		
	}

	@Override
	public int update(Login login) {
		loginDao.update(login);
		int r= 1;
		return r;
	}

	@Override
	public void delete(Login login) {
		String ids = login.getIds();
		if (!Util.isEmpty(ids)) {
			String stringArray[] = ids.split(",");
			for (String str : stringArray) {
				if (!Util.isEmpty(str)) {
					String cid = str.trim();
					Login loginDb = loginDao.get(Login.class, cid);
					loginDao.delete(loginDb);
				}
			}
		}
	}

	@Override
	public Login findByCid(String cid) {
		if (!Util.isEmpty(cid)) {
			Login login = loginDao.get(Login.class, cid);
			if (Util.isEmpty(login)) {
				return null;
			} else {
				return login;
			}
		}
		return null;
	}

	@Override
	public List<Login> findAll() {
		List<Login> loginList = loginDao.find("from Login where loginGroup='2'");
		return loginList;
	}
}
