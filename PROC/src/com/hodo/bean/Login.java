package com.hodo.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hodo.common.base.LayuiBaseObject;


/**
 * 登录表
 */
@Entity
@Table(name = "login")
@Cache(region="all",usage=CacheConcurrencyStrategy.READ_WRITE)
//@JsonIgnoreProperties(value={"user"})
public class Login extends LayuiBaseObject implements java.io.Serializable {
	private static final long serialVersionUID = 3170838709334253532L;
	
	private String cid;					
	private String cname;    //用户名 
	private String cpwd;	 //密码
	
	//非数据库
	private String validCode; //验证码图片
	private String rememberMe; //记住我
	
	private String crealname;//姓名(会员名)
	private String studentId;//学号
	private String dept;     //院系
	private String major;    //专业
	private String email;    //Email
	private String birthday;   //出生日期
	private String hobby;    //兴趣爱好
	private String type;     //类型(会员:会员 管理员:管理员)
	
	public Login() {}
	
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
	@Column(name="cname")
	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}
	@Column(name="cpwd")
	public String getCpwd() {
		return cpwd;
	}

	public void setCpwd(String cpwd) {
		this.cpwd = cpwd;
	}

	@Transient
	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}
	@Transient
	public String getRememberMe() {
		return rememberMe;
	}
	public void setRememberMe(String rememberMe) {
		this.rememberMe = rememberMe;
	}
	@Transient
	public String getCrealname() {
		return crealname;
	}

	public void setCrealname(String crealname) {
		this.crealname = crealname;
	}
	@Transient
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	@Transient
	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}
	@Transient
	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	@Transient
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Transient
	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	@Transient
	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	@Transient
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
