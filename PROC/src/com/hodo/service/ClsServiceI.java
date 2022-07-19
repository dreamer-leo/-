package com.hodo.service;

import java.util.List;

import com.hodo.bean.Cls;
import com.hodo.common.base.LayuiDataGrid;

public interface ClsServiceI {
	public void addCls(Cls cls);
	public void delete(Cls cls);
	public int update(Cls cls);
	public LayuiDataGrid datagrid(Cls cls);
	public List<Cls> findAll();
	public Cls findByCid(String cls);
}
