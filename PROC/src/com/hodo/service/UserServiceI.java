package com.hodo.service;

import java.util.List;

import com.hodo.bean.User;
import com.hodo.common.base.LayuiDataGrid;

public interface UserServiceI {
	public void addUser(User user);
	public boolean isValidUser(String cname);
	public User findUserByLoginCid(String loginCid);
	public int update(User user);
	public boolean chgPwd(User user);
	public void delete(User user);
	public User findByCid(String user);
	public List<User> findAll();
	public LayuiDataGrid datagrid(User user);
	public User findUserByLoginCname(String cname);
}
