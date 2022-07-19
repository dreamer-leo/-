package com.hodo.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hodo.common.base.LayuiBaseObject;


/**
 * 文件
 */
@Entity
@Table(name = "cfile")
@Cache(region="all",usage=CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value={"partDir","cls","partPathList"})
public class CFile extends LayuiBaseObject implements java.io.Serializable {

	private static final long serialVersionUID = 7910038281184051261L;
	
	private String cid;	
	private String name;     //文件名 
	private Long size;       //文件名 
	private String type;	 //文件类型 （1：pic 2：doc 3：video 4：audio 5: other  0：dir 6：pdf 7:txt 8:mi）
	private String officeType;//文件类型 （doc、docx、xls、xlsx、ppt、pptx、pdf）
	private String url;		 //路径
	private Date crtDate;    //创建日期
	private Date uptDate;    //修改日期
	private Date delDate;    //删除日期
	private String crtUser;  //创建用户
	private String uptUser;  //修改用户
	private String delFlg;  //删除标志(1:已删除)
	
	private Cls cls;     //分类
	
	private CFile partDir;     //父目录
	
	//非数据库
	private String key;             //搜索
	private String filePath;        //相对路径
	private String sizeString;      //文件名 
	private String crtDateString;   //创建日期
	private String partPathCid;    //当前路径
	private String nowPath;    //当前路径
	private List<PartPath> partPathList;    //父路径List
	
	private String clsCid;   //分类ID
	private String clsNm;   //分类名称
	
	private String fromUserCid;   //传输-发送者ID
	private String transCid;   //传输ID
	public CFile() {}
	
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
	@Column(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(name="size")
	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}
	@Column(name="type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@Column(name="officeType")
	public String getOfficeType() {
		return officeType;
	}

	public void setOfficeType(String officeType) {
		this.officeType = officeType;
	}
	@Column(name="url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name="crtDate")
	public Date getCrtDate() {
		return crtDate;
	}

	public void setCrtDate(Date crtDate) {
		this.crtDate = crtDate;
	}
	@Column(name="uptDate")
	public Date getUptDate() {
		return uptDate;
	}

	public void setUptDate(Date uptDate) {
		this.uptDate = uptDate;
	}
	@Column(name="delDate")
	public Date getDelDate() {
		return delDate;
	}

	public void setDelDate(Date delDate) {
		this.delDate = delDate;
	}

	@Column(name="crtUser")
	public String getCrtUser() {
		return crtUser;
	}

	public void setCrtUser(String crtUser) {
		this.crtUser = crtUser;
	}
	@Column(name="uptUser")
	public String getUptUser() {
		return uptUser;
	}

	public void setUptUser(String uptUser) {
		this.uptUser = uptUser;
	}
	@Column(name="delFlg")
	public String getDelFlg() {
		return delFlg;
	}

	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}
	
	@ManyToOne
	@JoinColumn(name = "clsCid")
	public Cls getCls() {
		return cls;
	}

	public void setCls(Cls cls) {
		this.cls = cls;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "partDirCid")
	public CFile getPartDir() {
		return partDir;
	}

	public void setPartDir(CFile partDir) {
		this.partDir = partDir;
	}

	@Transient
	public String getSizeString() {
		return sizeString;
	}

	public void setSizeString(String sizeString) {
		this.sizeString = sizeString;
	}
	@Transient
	public String getCrtDateString() {
		return crtDateString;
	}

	public void setCrtDateString(String crtDateString) {
		this.crtDateString = crtDateString;
	}
	@Transient
	public String getNowPath() {
		return nowPath;
	}

	public void setNowPath(String nowPath) {
		this.nowPath = nowPath;
	}
	@Transient
	public String getPartPathCid() {
		return partPathCid;
	}

	public void setPartPathCid(String partPathCid) {
		this.partPathCid = partPathCid;
	}
	@Transient
	public List<PartPath> getPartPathList() {
		return partPathList;
	}

	public void setPartPathList(List<PartPath> partPathList) {
		this.partPathList = partPathList;
	}
	@Transient
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	@Transient
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	@Transient
	public String getClsCid() {
		return clsCid;
	}

	public void setClsCid(String clsCid) {
		this.clsCid = clsCid;
	}
	@Transient
	public String getClsNm() {
		return clsNm;
	}

	public void setClsNm(String clsNm) {
		this.clsNm = clsNm;
	}
	@Transient
	public String getTransCid() {
		return transCid;
	}

	public void setTransCid(String transCid) {
		this.transCid = transCid;
	}
	@Transient
	public String getFromUserCid() {
		return fromUserCid;
	}

	public void setFromUserCid(String fromUserCid) {
		this.fromUserCid = fromUserCid;
	}
	
}
