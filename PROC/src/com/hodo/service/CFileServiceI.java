package com.hodo.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.hodo.bean.CFile;
import com.hodo.bean.Trans;
import com.hodo.common.base.LayuiDataGrid;

public interface CFileServiceI {
	public void addCFile(CFile cFile);
	public int update(CFile cFile);
	public void delete(CFile cFile);
	public CFile findByCid(String cFile);
	public List<CFile> findAll();
	public LayuiDataGrid datagrid(CFile cFile,HttpSession httpsession);
	public CFile findByPartCid(String cid);

}
