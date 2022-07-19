package com.hodo.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hodo.bean.CFile;
import com.hodo.bean.PartPath;
import com.hodo.bean.Trans;
import com.hodo.bean.User;
import com.hodo.common.base.Json;
import com.hodo.common.base.LayuiDataGrid;
import com.hodo.common.util.DesUtils;
import com.hodo.common.util.Util;
import com.hodo.service.CFileService;
import com.hodo.service.CFileServiceI;
import com.hodo.service.TransServiceI;

@Controller
@RequestMapping(value = "/trans")
public class TransController {

	@Autowired
	private TransServiceI transService;
	@Autowired
	private CFileServiceI cFileService;
	/**
	 * 传输-跳转
	 */
	@RequestMapping(value = "/toTrans")
	public String toTrans(Trans trans, Model model) {
		return "/system/trans/trans";
	}

	/**
	 * 查询传输列表
	 */
	@RequestMapping(value = "/transList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public LayuiDataGrid transList(Trans trans, Model model) {
		LayuiDataGrid dataGrid = transService.datagrid(trans);
		return dataGrid;
	}

	/*
     * 查询传输列表
     */
    @RequestMapping(value="/transListA",produces="application/json;charset=UTF-8")
    @ResponseBody
	public Json transListA(Trans trans, Model model) {
		Json j = new Json();

		List<Trans> reqList = transService.findAll();

		if (!Util.isEmpty(reqList)) {
			j.setSuccess(true);
			j.setObj(reqList);
			j.setMsg("获取成功！");
		} else {
			j.setSuccess(false);
			j.setMsg("获取失败！");
		}

		return j;
	}

	/**
	 * 传输搜索-跳转
	 */
	@RequestMapping(value = "/transSearch")
	public String transSearch(Trans trans, Model model) {
		return "/system/trans/transSearch";
	}

	/**
	 * 传输搜索结果-跳转
	 */
	@RequestMapping(value = "/transSearchResult")
	public String transSearchResult(Trans trans, Model model) {
		LayuiDataGrid dataGrid = transService.datagrid(trans);
		model.addAttribute("dataGrid", dataGrid);
		return "/system/trans/transSearchResult";
	}

	/**
	 * 传输详情
	 */
	@RequestMapping(value = "/transDetail")
	public String transDetail(Trans trans, Model model) {
		Trans u = transService.findByCid(trans.getCid());
		model.addAttribute("trans", u);
		return "/system/trans/transDetail";
	}

	/**
	 * 新增/修改用Form
	 */
	@RequestMapping(value = "/transForm")
	public String transForm(Trans trans, Model model) {
		String cid = trans.getCid();
		if (Util.isNotEmpty(cid)) {
			Trans u = transService.findByCid(cid);
			model.addAttribute("trans", u);
		}
		return "/system/trans/transForm";
	}

	/**
	 * 新增-跳转
	 * @return
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd(Trans trans, Model model) {
		return "/system/trans/add";
	}

	/**
	 * 新增-处理
	 */
	@RequestMapping(value = "/add", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json add(Trans trans, Model model) {
		Json j = new Json();
		boolean b = false;
		if (Util.isEmpty(trans.getCid())) {
		
			trans.setCid(UUID.randomUUID().toString());

			transService.addTrans(trans);

			b = true;

		} else {
			b = false;
		}
		if (b) {
			j.setSuccess(true);
			j.setMsg("新增成功！");
		} else {
			j.setSuccess(false);
			j.setMsg("新增失败！");
		}
		return j;
	}
	/**
	 * 发送-处理
	 */
	@RequestMapping(value = "/send", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json send(HttpServletRequest req,Trans trans, Model model) {
		Json j = new Json();
		boolean b = false;
		
		String fromUserCid =trans.getFromUserCid();
		String toUserCid =trans.getToUserCid();
		String oldFileCid =trans.getOldFileCid();
		
		if (Util.isNotEmpty(fromUserCid) && Util.isNotEmpty(toUserCid) && Util.isNotEmpty(oldFileCid) ) {
			
			try {
				String key = UUID.randomUUID().toString();
				DesUtils desUtils = new DesUtils(key);
				
				CFile cf= cFileService.findByCid(oldFileCid);
				
				//父亲路径
				CFile c= cf;
				List<PartPath> ppList = new ArrayList<PartPath>();
				while(!Util.isEmpty(c.getPartDir())){
					c = cFileService.findByCid(c.getPartDir().getCid());
					PartPath pp = new PartPath();
					pp.setUrl(c.getUrl());
					ppList.add(pp);
				}
				//反转
				Collections.reverse(ppList);
				// 获取存取路径
				String filePath = req.getRealPath("/") + "files\\";
				for (PartPath partPath : ppList) {
					filePath=filePath+partPath.getUrl();
				}
				
				String oldfilePath=filePath+cf.getUrl();
								
				String path = req.getRealPath("/") + "files\\"+"temp";
				String toFilePath =filePath+"mi-"+cf.getUrl();
				String toFileName = "mi-"+cf.getUrl();
				desUtils.doEncryptFile(oldfilePath, path);//加密
				desUtils.doEncryptFile(oldfilePath, req.getRealPath("/") + "files");//加密	
				trans.setCid(UUID.randomUUID().toString());
				trans.setKeyNo(key);
				trans.setSendDate(new Date());
				trans.setOldFile(cf);
				
				CFile miFile = new CFile();
				miFile.setCid(UUID.randomUUID().toString());
				miFile.setName("已加密-"+cf.getName());
				miFile.setSize(cf.getSize());
				//文件类型 （1：pic 2：doc 3：video 4：audio 5: other  0：dir 6：pdf 7:txt 8:mi）
				miFile.setType(cf.getType());
				if(Util.isNotEmpty(cf.getOfficeType())){
					miFile.setOfficeType(cf.getOfficeType());
				}
				
				miFile.setUrl(toFileName);
				miFile.setCrtDate(new Date());
				miFile.setCrtUser(fromUserCid);
				cFileService.addCFile(miFile);
				
				trans.setMiFile(miFile);
				
				User fromUser = new User();
				fromUser.setCid(fromUserCid);
				trans.setFromUser(fromUser);
				
				User toUser = new User();
				toUser.setCid(toUserCid);
				trans.setToUser(toUser);
				
				transService.addTrans(trans);
				
				b = true;
			} catch (Exception e) {
				b = false;
				j.setSuccess(false);
				j.setMsg("发送失败！");
				return j;
			} 
			
		} else {
			b = false;
		}
		if (b) {
			j.setSuccess(true);
			j.setMsg("发送成功！");
		} else {
			j.setSuccess(false);
			j.setMsg("发送失败！");
		}
		return j;
	}

	/**
	 * 修改-跳转
	 * @return
	 */
	@RequestMapping(value = "/toEdit")
	public String toEdit(Trans trans, Model model) {
		Trans u = transService.findByCid(trans.getCid());
		model.addAttribute("trans", u);
		return "/system/trans/edit";
	}

	/**
	 * 修改-处理
	 */
	@RequestMapping(value = "/edit", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json edit(Trans trans, Model model) {
		Json j = new Json();
		boolean b = false;
		if (!Util.isEmpty(trans.getCid())) {
			
			int r = transService.update(trans);
			if (r == 1)
				b = true;
		}
		if (b) {
			j.setSuccess(true);
			j.setMsg("修改成功！");
		} else {
			j.setSuccess(false);
			j.setMsg("修改失败！");
		}
		return j;
	}

	/**
	 * 删除-处理
	 */
	@RequestMapping(value = "/del", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json del(Trans trans, Model model) {
		Json j = new Json();
		try {
			transService.delete(trans);
			j.setSuccess(true);
			j.setMsg("删除成功!");
		} catch (Exception e) {
			j.setMsg("删除失败!");
		}
		return j;
	}
	
	/*
     * 查询传输列表
     */
	@RequestMapping(value = "/count")
	public String count(Trans trans, Model model) {
		List<Trans> transList = transService.count();
		model.addAttribute("transList", transList);
		List<Trans> transList2 = transService.count2();
		model.addAttribute("transList2", transList2);
		return "/system/count";
	}

}
