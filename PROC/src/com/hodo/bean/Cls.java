package com.hodo.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hodo.common.base.LayuiBaseObject;


/**
 * 分类
 */
@Entity
@Table(name = "cls")
@Cache(region="all",usage=CacheConcurrencyStrategy.READ_WRITE)
//@JsonIgnoreProperties(value={"login"})
public class Cls extends LayuiBaseObject implements java.io.Serializable {

	private static final long serialVersionUID = 7392172463778159218L;

	private String cid;//ID

	private String nm;
	
	public Cls() {}

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	@Column(name="cid")
	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}
	
	@Column(name="nm")
	public String getNm() {
		return nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
	}
	

}
