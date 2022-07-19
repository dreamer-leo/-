package com.hodo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hodo.bean.Trans;
import com.hodo.common.base.LayuiDataGrid;
import com.hodo.common.util.Util;
import com.hodo.dao.BaseDaoI;

@Service(value = "transService")
public class TransService implements TransServiceI {
	
	@Autowired
	private BaseDaoI<Trans> transDao;
	
	@Override
	public LayuiDataGrid datagrid(Trans trans) {
		String hql=" from Trans t where 1=1";
		LayuiDataGrid j = new LayuiDataGrid();
		j.setData(edit(find(trans,hql)));
		j.setCount(total(trans,hql));
		j.setCode(0L);
		return j;
		
	}
	private List<Trans> edit(List<Trans> transList) {
		if (transList != null && transList.size() > 0) {
			for (Trans u : transList) {
			}
		}
		return transList;
	}
	private List<Trans> find(Trans trans,String hql) {
		hql = addWhere(trans, hql);
		List<Trans> transList = transDao.find(hql, trans.getPage(), trans.getLimit());
		return transList;
	}
	
	private Long total(Trans trans,String hql) {
		hql = addWhere(trans,hql);
		hql = "select count(*) "+hql;
		return transDao.count(hql);
	}
	
	private String addWhere(Trans trans, String hql) {
		return hql;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addTrans(Trans trans) {
		transDao.save(trans);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Trans trans) {
		String ids = trans.getIds();
		if (!Util.isEmpty(ids)) {
			String stringArray[] = ids.split(",");
			for (String str : stringArray) {
				if (!Util.isEmpty(str)) {
					String cid = str.trim();
					Trans transDb = transDao.get(Trans.class,cid); 
					transDao.delete(transDb);
				}
			}
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int update(Trans trans) {	
		transDao.update(trans);
		return 1;
	}
	
	@Override
	public List<Trans> findAll() {
		List<Trans> transList = transDao.find("from Trans");
		return transList;
	}
	
	@Override
	public Trans findByCid(String cid) {
		if (!Util.isEmpty(cid)) {
			Trans trans = transDao.get(Trans.class,cid); 
			if (Util.isEmpty(trans)) {
				return null;
			} else {
				return trans;
			}
		}
		return null;
	}
	@Override
	public Trans findByToUserCidAndMiFileCid(String toUserCid, String miFileCid) {
		if (!Util.isEmpty(toUserCid) && !Util.isEmpty(miFileCid) ) {
			Trans trans = transDao.get("from Trans t where t.toUser.cid='"+toUserCid+"' and t.miFile.cid='"+miFileCid+"' and t.recDate is null ", new String[]{});
			if (Util.isEmpty(trans)) {
				return null;
			} else {
				return trans;
			}
		}
		return null;
	}
	@Override
	public List<Trans> count() {
		List<Trans> transList = new ArrayList<Trans>();
		String sql = "select MONTH(t.recDate) mt"
					+",count(t.cid) num"  
					+" from trans t"
					+" where YEAR(t.recDate)=YEAR(NOW())"
					+" group by MONTH(t.recDate)"
					+" order by MONTH(t.recDate) asc";
		List<Object[]> objList = transDao.executeCountSql(sql);
		for (Object[] a : objList) {
			Trans trans = new Trans();
			trans.setMt(String.valueOf(a[0]));
			trans.setNum(String.valueOf(a[1]));
			transList.add(trans);
		}
		return transList;
	}
	@Override
	public List<Trans> count2() {
		List<Trans> transList = new ArrayList<Trans>();
		String sql = "select " 
				+" CASE "
				+" WHEN t2.type = '1' THEN '图片' "
				+" WHEN t2.type = '2' THEN 'office文档' "
				+" WHEN t2.type = '3' THEN '视频' "
				+" WHEN t2.type = '4' THEN '音频' "
				+" WHEN t2.type = '6' THEN 'PDF' "
				+" WHEN t2.type = '7' THEN '文本' "
				+" WHEN t2.type = '7' THEN '文本' "
				+" END type "
				+" ,COUNT(t.cid) num "
				+" FROM "
				+" trans t "
				+" inner join cfile t2 on t.oldFilecid=t2.cid "
				+" GROUP BY "
				+" t2.type ";

		List<Object[]> objList = transDao.executeCountSql(sql);
		for (Object[] a : objList) {
			Trans trans = new Trans();
			trans.setType(String.valueOf(a[0]));
			trans.setNum(String.valueOf(a[1]));
			transList.add(trans);
		}
		return transList;
	}

	@Override
	public List<Trans> findByMiFileCidOrReMiFileeCid(String id) {
		List<Trans> transList = transDao.find("from Trans t where t.miFile.cid='"+id+"' or t.reMiFile.cid='"+id+"' ");
		return transList;
	}
}
