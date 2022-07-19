package com.hodo.common.base;

import java.util.List;

/**
 * Layui的datagrid模型
 * 
 * @author 郭东升
 * 
 */
public class LayuiDataGrid implements java.io.Serializable {

	private Long code;
	private String msg;
	private Long count; //总记录数
	private List data;// 记录
	
	public Long getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	public Long getCount() {
		return count;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public List getData() {
		return data;
	}
	public void setData(List data) {
		this.data = data;
	}


}
