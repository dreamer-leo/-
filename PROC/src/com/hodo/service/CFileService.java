package com.hodo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hodo.bean.CFile;
import com.hodo.bean.CFile;
import com.hodo.bean.PartPath;

import com.hodo.common.base.Const;
import com.hodo.common.base.Encrypt;
import com.hodo.common.base.LayuiDataGrid;
import com.hodo.common.base.SessionInfo;
import com.hodo.common.util.DateUtil;
import com.hodo.common.util.FileSizeUtil;
import com.hodo.common.util.Util;
import com.hodo.dao.BaseDaoI;

@Service(value = "cFileService")
public class CFileService implements CFileServiceI {

//	@Autowired
//	private CFileDao cFileDao;

	@Autowired
	private BaseDaoI<CFile> cFileDao;
	@Override
	public LayuiDataGrid datagrid(CFile cFile,HttpSession httpsession) {
		String hql=" from CFile t where 1=1 and (" +
				"(t.crtUser='"+cFile.getCrtUser()+"' and substring(t.name,1,3)!='已加密') " +
				" or " +
				" exists(select t2.cid from Trans t2 where t2.toUser.cid='"+cFile.getCrtUser()+"' and t2.recDate is null and t.cid=t2.miFile.cid) " +
				") ";
		
		LayuiDataGrid j = new LayuiDataGrid();
		j.setData(editCFiles(find(cFile,hql,httpsession),httpsession));
		j.setCount(total(cFile,hql));
		j.setCode(0L);
		return j;
	}
	private List<CFile> editCFiles(List<CFile> cFileList,HttpSession session) {
		if (cFileList != null && cFileList.size() > 0) {
			SessionInfo s = (SessionInfo)session.getAttribute(Const.SESSION_INFO);
			String noewPath = Util.isEmpty(s.getNowPath())?"":s.getNowPath();
			for (CFile cf: cFileList) {
				cf.setCrtDateString(DateUtil.dateToString(cf.getCrtDate()));
				
				//大小
				if(!Util.isEmpty(cf.getSize())){
					// 获取存取路径-源文件
					String filePath = session.getServletContext().getRealPath("/") + "files\\"+noewPath+cf.getUrl();
					cf.setSizeString(FileSizeUtil.getAutoFileOrFilesSize(filePath));
				}
				//路径
				if(!Util.isEmpty(cf.getSize())){
					
					//父亲路径
					String partPath = "";
									
					if("6".equals(cf.getType()) || "7".equals(cf.getType())){
						// 获取存取路径
						String filePath = "files-";
						//有父亲路径
						if(!Util.isEmpty(cf.getPartDir())){
							partPath = cf.getPartDir().getUrl();
							filePath=filePath+"-"+partPath+"-"+cf.getUrl();
						} else {
							filePath=filePath+"-"+cf.getUrl();
						}
						cf.setFilePath(filePath);
					} else {
						// 获取存取路径
						String filePath = "files\\";

						//有父亲路径
						if(!Util.isEmpty(cf.getPartDir())){
							partPath = cf.getPartDir().getUrl();
							filePath=filePath+"\\"+partPath+"\\"+cf.getUrl();
						} else {
							filePath=filePath+"\\"+cf.getUrl();
						}
						cf.setFilePath(filePath);
					}	
				}
				
				if("8".equals(cf.getType())) {
					
				}
				
			}
		}
		return cFileList;
	}
	private List<CFile> find(CFile cFile,String hql,HttpSession session) {
		hql = addWhere(cFile, hql);
		
		Object obj = session.getAttribute(Const.SESSION_INFO);
		if(Util.isNotEmpty(obj)){
			SessionInfo si=(SessionInfo)obj;
			String order = si.getOrder();
			if("asc".equals(order)){
				hql += " order by t.name asc ";
			} else if("desc".equals(order)) {
				hql += " order by t.name desc ";
			} else {
				hql += " order by t.name asc ";
			}
		}
		
		cFile.setLimit(10000);
		List<CFile> cFileList = cFileDao.find(hql, cFile.getPage(), cFile.getLimit());
		return cFileList;
	}
	private Long total(CFile cFile,String hql) {
		hql = addWhere(cFile, hql);
		hql = "select count(*) "+hql;
		return cFileDao.count(hql);
	}
	private String addWhere(CFile cFile, String hql) {
		
		if("1".equals(cFile.getDelFlg())){
			hql += " and t.delFlg='" + cFile.getDelFlg() + "' ";
		} else {
			hql += " and t.delFlg is null";
			if(!Util.isEmpty(cFile.getKey())) {
				hql += " and t.name like '%"+cFile.getKey()+"%' ";
			} else if(!Util.isEmpty(cFile.getType())) {
				hql += " and t.type='" + cFile.getType() + "' ";
			}  else if(!Util.isEmpty(cFile.getClsCid())) {
				hql += " and t.cls.cid='" + cFile.getClsCid() + "' ";
			}  else {
				if(!Util.isEmpty(cFile.getCid())){
					hql += " and t.partDir.cid='" + cFile.getCid() + "' ";
				} else {
					hql += " and t.partDir.cid is null";
				}
			}
		}
		return hql;
	}

	@Override
	public void addCFile(CFile cFile) {
		cFileDao.save(cFile);
	}

	@Override
	public int update(CFile cFile) {
		cFileDao.update(cFile);
		int r= 1;
		return r;
	}

	@Override
	public void delete(CFile cFile) {
		String cid = cFile.getCid();
		CFile cFileDb = cFileDao.get(CFile.class, cid);
		cFileDao.delete(cFileDb);
	}

	@Override
	public CFile findByCid(String cid) {
		if (!Util.isEmpty(cid)) {
			CFile cFile = cFileDao.get(CFile.class, cid);
			if (Util.isEmpty(cFile)) {
				return null;
			} else {
				return cFile;
			}
		}
		return null;
	}

	@Override
	public List<CFile> findAll() {
		List<CFile> cFileList = cFileDao.find("from CFile where cFileGroup='2'");
		return cFileList;
	}
	@Override
	public CFile findByPartCid(String cid) {
		if (!Util.isEmpty(cid)) {
			List<Object> paramList = new ArrayList<Object>();
			paramList.add(cid);
			CFile cFileDb = cFileDao.get("from CFile t where t.partDir.cid=? ", paramList);
			if (Util.isEmpty(cFileDb)) {
				return null;
			} else {
				return cFileDb;
			}
		}
		return null;
	}
}
