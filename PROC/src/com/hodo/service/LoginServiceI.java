package com.hodo.service;

import java.util.List;

import com.hodo.bean.Login;
import com.hodo.common.base.LayuiDataGrid;

public interface LoginServiceI {
	public Login findLoginOne(Login login);
	public void addLogin(Login login);
	public boolean isValidLogin(String cname);
	public Login findLoginByCname(String cname);
	public int update(Login login);
	public boolean chgPwd(Login login);
	public void delete(Login login);
	public Login findByCid(String login);
	public List<Login> findAll();
	public LayuiDataGrid datagrid(Login login);
}
