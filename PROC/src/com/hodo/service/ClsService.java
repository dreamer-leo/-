package com.hodo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageHelper;
import com.hodo.bean.Cls;
import com.hodo.bean.User;
import com.hodo.common.base.LayuiDataGrid;
import com.hodo.common.util.Util;
import com.hodo.dao.BaseDaoI;

@Service(value = "clsService")
public class ClsService implements ClsServiceI {
	
	@Autowired
	private BaseDaoI<Cls> clsDao;
	
	@Override
	public LayuiDataGrid datagrid(Cls cls) {
		String hql=" from Cls t where 1=1";
		LayuiDataGrid j = new LayuiDataGrid();
		j.setData(edit(find(cls,hql)));
		j.setCount(total(cls,hql));
		j.setCode(0L);
		return j;
		
	}
	private List<Cls> edit(List<Cls> clsList) {
		if (clsList != null && clsList.size() > 0) {
			for (Cls u : clsList) {
			}
		}
		return clsList;
	}
	private List<Cls> find(Cls cls,String hql) {
		hql = addWhere(cls, hql);
//		if (user.getSort() != null && user.getOrder() != null) {
//			hql += " order by " + user.getSort() + " " + user.getOrder();
//		}else {
//			hql+=" order by t.pubDate desc,t.creatDate desc";
//		}
		List<Cls> clsList = clsDao.find(hql, cls.getPage(), cls.getLimit());
		return clsList;
	}
	
	private Long total(Cls cls,String hql) {
		hql = addWhere(cls,hql);
		hql = "select count(*) "+hql;
		return clsDao.count(hql);
	}
	
	private String addWhere(Cls cls, String hql) {
		if (Util.isNotEmpty(cls.getNm())) {
			hql += " and t.nm like '%"+cls.getNm()+"%' ";
		}
		return hql;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addCls(Cls cls) {
		clsDao.save(cls);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Cls cls) {
		String ids = cls.getIds();
		if (!Util.isEmpty(ids)) {
			String stringArray[] = ids.split(",");
			for (String str : stringArray) {
				if (!Util.isEmpty(str)) {
					String cid = str.trim();
					Cls clsDb = clsDao.get(Cls.class,cid); 
					clsDao.delete(clsDb);
				}
			}
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int update(Cls cls) {	
		clsDao.update(cls);
		return 1;
	}
	
	@Override
	public List<Cls> findAll() {
		List<Cls> clsList = clsDao.find("from Cls");
		return clsList;
	}
	
	@Override
	public Cls findByCid(String cid) {
		if (!Util.isEmpty(cid)) {
			Cls cls = clsDao.get(Cls.class,cid); 
			if (Util.isEmpty(cls)) {
				return null;
			} else {
				return cls;
			}
		}
		return null;
	}
	
	
}
