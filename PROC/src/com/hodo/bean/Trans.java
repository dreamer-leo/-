package com.hodo.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.transaction.Transactional;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hodo.common.base.LayuiBaseObject;


/**
 * 传输
 */
@Entity
@Table(name = "trans")
@Cache(region="all",usage=CacheConcurrencyStrategy.READ_WRITE)
//@JsonIgnoreProperties(value={"login"})
public class Trans extends LayuiBaseObject implements java.io.Serializable {

	private static final long serialVersionUID = 7392172463778159218L;

	private String cid;//ID
	
	private String keyNo;       //密钥

	private Date sendDate;    //发送日期
	private Date recDate;     //接受日期
	
	private CFile oldFile;  //原始文件
	private CFile miFile;   //加密后文件
	private CFile reMiFile; //解密后文件
	
	private User fromUser;  //发送人
	private User toUser;    //接受人
	
	//非数据库
	private String oldFileCid;	     //原始文件ID
	private String miFileCid;	     //加密后ID
	private String reMiFileeCid;	 //原解密后ID
	private String fromUserCid;     //发送人ID
	private String toUserCid;       //接受人ID
	private String mt;       //统计-月
	private String num;       //统计-传输数
	private String type;	 //文件类型 （1：pic 2：doc 3：video 4：audio 5: other  0：dir 6：pdf 7:txt 8:mi）
	public Trans() {}

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
	@Column(name="keyNo")
	public String getKeyNo() {
		return keyNo;
	}

	public void setKeyNo(String keyNo) {
		this.keyNo = keyNo;
	}

	@Column(name="sendDate")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	
	@Column(name="recDate")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getRecDate() {
		return recDate;
	}

	public void setRecDate(Date recDate) {
		this.recDate = recDate;
	}

	@ManyToOne
	@JoinColumn(name = "oldFileCid")
	public CFile getOldFile() {
		return oldFile;
	}

	public void setOldFile(CFile oldFile) {
		this.oldFile = oldFile;
	}

	@ManyToOne
	@JoinColumn(name = "miFileCid")
	public CFile getMiFile() {
		return miFile;
	}

	public void setMiFile(CFile miFile) {
		this.miFile = miFile;
	}

	@ManyToOne
	@JoinColumn(name = "reMiFileCid")
	public CFile getReMiFile() {
		return reMiFile;
	}

	public void setReMiFile(CFile reMiFile) {
		this.reMiFile = reMiFile;
	}

	@ManyToOne
	@JoinColumn(name = "fromUserCid")
	public User getFromUser() {
		return fromUser;
	}

	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}

	@ManyToOne
	@JoinColumn(name = "toUserCid")
	public User getToUser() {
		return toUser;
	}

	public void setToUser(User toUser) {
		this.toUser = toUser;
	}

	@Transient
	public String getOldFileCid() {
		return oldFileCid;
	}

	public void setOldFileCid(String oldFileCid) {
		this.oldFileCid = oldFileCid;
	}
	@Transient
	public String getMiFileCid() {
		return miFileCid;
	}

	public void setMiFileCid(String miFileCid) {
		this.miFileCid = miFileCid;
	}
	@Transient
	public String getReMiFileeCid() {
		return reMiFileeCid;
	}

	public void setReMiFileeCid(String reMiFileeCid) {
		this.reMiFileeCid = reMiFileeCid;
	}

	@Transient
	public String getFromUserCid() {
		return fromUserCid;
	}

	public void setFromUserCid(String fromUserCid) {
		this.fromUserCid = fromUserCid;
	}
	
	@Transient
	public String getToUserCid() {
		return toUserCid;
	}

	public void setToUserCid(String toUserCid) {
		this.toUserCid = toUserCid;
	}
	@Transient
	public String getMt() {
		return mt;
	}

	public void setMt(String mt) {
		this.mt = mt;
	}
	@Transient
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}
	@Transient
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
