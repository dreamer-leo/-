package com.hodo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hodo.bean.Cls;
import com.hodo.common.base.Json;
import com.hodo.common.base.LayuiDataGrid;
import com.hodo.common.util.Util;
import com.hodo.service.ClsServiceI;

@Controller
@RequestMapping(value = "/cls")
public class ClsController {

	@Autowired
	private ClsServiceI clsService;
	/**
	 * 分类-跳转
	 */
	@RequestMapping(value = "/toCls")
	public String toCls(Cls cls, Model model) {
		return "/system/cls/cls";
	}

	/**
	 * 查询分类列表
	 */
	@RequestMapping(value = "/clsList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public LayuiDataGrid clsList(Cls cls, Model model) {
		LayuiDataGrid dataGrid = clsService.datagrid(cls);
		return dataGrid;
	}

	/*
     * 查询分类列表
     */
    @RequestMapping(value="/clsListA",produces="application/json;charset=UTF-8")
    @ResponseBody
	public Json clsListA(Cls cls, Model model) {
		Json j = new Json();

		List<Cls> reqList = clsService.findAll();

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
	 * 分类搜索-跳转
	 */
	@RequestMapping(value = "/clsSearch")
	public String clsSearch(Cls cls, Model model) {
		return "/system/cls/clsSearch";
	}

	/**
	 * 分类搜索结果-跳转
	 */
	@RequestMapping(value = "/clsSearchResult")
	public String clsSearchResult(Cls cls, Model model) {
		LayuiDataGrid dataGrid = clsService.datagrid(cls);
		model.addAttribute("dataGrid", dataGrid);
		return "/system/cls/clsSearchResult";
	}

	/**
	 * 分类详情
	 */
	@RequestMapping(value = "/clsDetail")
	public String clsDetail(Cls cls, Model model) {
		Cls u = clsService.findByCid(cls.getCid());
		model.addAttribute("cls", u);
		return "/system/cls/clsDetail";
	}

	/**
	 * 新增/修改用Form
	 */
	@RequestMapping(value = "/clsForm")
	public String clsForm(Cls cls, Model model) {
		String cid = cls.getCid();
		if (Util.isNotEmpty(cid)) {
			Cls u = clsService.findByCid(cid);
			model.addAttribute("cls", u);
		}
		return "/system/cls/clsForm";
	}

	/**
	 * 新增-跳转
	 * @return
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd(Cls cls, Model model) {
		return "/system/cls/add";
	}

	/**
	 * 新增-处理
	 */
	@RequestMapping(value = "/add", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json add(Cls cls, Model model) {
		Json j = new Json();
		boolean b = false;
		if (Util.isEmpty(cls.getCid())) {
		
			cls.setCid(UUID.randomUUID().toString());

			clsService.addCls(cls);

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
	 * 修改-跳转
	 * @return
	 */
	@RequestMapping(value = "/toEdit")
	public String toEdit(Cls cls, Model model) {
		Cls u = clsService.findByCid(cls.getCid());
		model.addAttribute("cls", u);
		return "/system/cls/edit";
	}

	/**
	 * 修改-处理
	 */
	@RequestMapping(value = "/edit", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json edit(Cls cls, Model model) {
		Json j = new Json();
		boolean b = false;
		if (!Util.isEmpty(cls.getCid())) {
			
			int r = clsService.update(cls);
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
	public Json del(Cls cls, Model model) {
		Json j = new Json();
		try {
			clsService.delete(cls);
			j.setSuccess(true);
			j.setMsg("删除成功!");
		} catch (Exception e) {
			j.setMsg("删除失败!");
		}
		return j;
	}

}
