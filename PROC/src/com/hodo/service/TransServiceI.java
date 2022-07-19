package com.hodo.service;

import java.util.List;

import com.hodo.bean.Trans;
import com.hodo.common.base.LayuiDataGrid;

public interface TransServiceI {
	public void addTrans(Trans trans);
	public void delete(Trans trans);
	public int update(Trans trans);
	public LayuiDataGrid datagrid(Trans trans);
	public List<Trans> findAll();
	public Trans findByCid(String trans);
	public Trans findByToUserCidAndMiFileCid(String toUserCid, String miFileCid);
	public List<Trans> count();
	public List<Trans> count2();
	public List<Trans> findByMiFileCidOrReMiFileeCid(String id);
}
