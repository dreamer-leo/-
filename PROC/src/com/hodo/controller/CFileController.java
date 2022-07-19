package com.hodo.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.hodo.bean.CFile;
import com.hodo.bean.Cls;
import com.hodo.bean.Login;
import com.hodo.bean.PartPath;
import com.hodo.bean.Trans;
import com.hodo.bean.User;
import com.hodo.common.base.Const;
import com.hodo.common.base.Json;
import com.hodo.common.base.LayuiDataGrid;
import com.hodo.common.base.SessionInfo;
import com.hodo.common.interceptor.shiro.ShiroRealm;
//import com.hodo.common.util.FileUtils;
import com.hodo.common.util.DateUtil;
import com.hodo.common.util.DesUtils;
import com.hodo.common.util.FileUtils;
import com.hodo.common.util.IdUtil;
import com.hodo.common.util.Util;
import com.hodo.common.util.mail.SendMail;
import com.hodo.common.util.security.Md5Util;
import com.hodo.common.util.security.ShiroUtil;
import com.hodo.service.CFileServiceI;
import com.hodo.service.ClsService;
import com.hodo.service.ClsServiceI;
import com.hodo.service.LoginServiceI;
import com.hodo.service.TransServiceI;
import com.hodo.service.UserServiceI;

@Controller
@RequestMapping(value = "/cfile")
public class CFileController {
	
	@Autowired
	private CFileServiceI cFileService;
	@Autowired
	private ClsServiceI clsService;
	@Autowired
	private UserServiceI userService;
	@Autowired
	private TransServiceI transService;

	/**
	 * 文件上传
	 */
	@RequestMapping(value = "/upload", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json upload(@RequestParam(value = "file", defaultValue = "") MultipartFile file, CFile cf, HttpSession httpSession,HttpServletRequest request, HttpServletResponse response) throws Exception {
		Json j = new Json();
		boolean b = false;
		String userId = cf.getCid();
		if (!Util.isEmpty(userId)) {
			if (!file.isEmpty()) {
				try {
					// 上传的文件名
					String oriName = file.getOriginalFilename();
					// 文件扩展名
					String suffix = oriName.substring(oriName.lastIndexOf(".") + 1);
					
					String type = "";//文件类型 
					//图片类型 png、jpg、gif、bmp、jpeg
					//文档类型 doc、docx、xls、xlsx、ppt、pdf、txt
					//视频类型 AVI、mov、rmvb、rm、FLV、mp4、3GP
					//音频类型MP3、AAC、WAV、WMA;
					 //文件类型 （1：pic 2：doc 3：video 4：audio 5: other  0：dir 6：pdf 7:txt）
					if(!Util.isEmpty(suffix)){
						if( "png".toLowerCase().equals(suffix.toLowerCase()) || 
							"jpg".toLowerCase().equals(suffix.toLowerCase()) || 
							"gif".toLowerCase().equals(suffix.toLowerCase()) || 
							"bmp".toLowerCase().equals(suffix.toLowerCase()) || 
							"jpeg".toLowerCase().equals(suffix.toLowerCase())
							) {
							type="1";
						} else if( "doc".toLowerCase().equals(suffix.toLowerCase()) || 
									"docx".toLowerCase().equals(suffix.toLowerCase()) || 
									"xls".toLowerCase().equals(suffix.toLowerCase()) || 
									"xlsx".toLowerCase().equals(suffix.toLowerCase()) || 
									"ppt".toLowerCase().equals(suffix.toLowerCase()) || 
									"pptx".toLowerCase().equals(suffix.toLowerCase())  
									
							) {
							type="2";
						} else if( "AVI".toLowerCase().equals(suffix.toLowerCase()) || 
							"mov".toLowerCase().equals(suffix.toLowerCase()) || 
							"rmvb".toLowerCase().equals(suffix.toLowerCase()) || 
							"rm".toLowerCase().equals(suffix.toLowerCase()) || 
							"FLV".toLowerCase().equals(suffix.toLowerCase()) || 
							"mp4".toLowerCase().equals(suffix.toLowerCase()) || 
							"wmv".toLowerCase().equals(suffix.toLowerCase()) || 
							"3GP".toLowerCase().equals(suffix.toLowerCase())
							) {
							type="3";
						} else if( "MP3".toLowerCase().equals(suffix.toLowerCase()) || 
							"AAC".toLowerCase().equals(suffix.toLowerCase()) || 
							"WAV".toLowerCase().equals(suffix.toLowerCase()) || 
							"WMA".toLowerCase().equals(suffix.toLowerCase())
							) {
							type="4";
						} else if( "pdf".toLowerCase().equals(suffix.toLowerCase())
							) {
							type="6";
						} else if( "txt".toLowerCase().equals(suffix.toLowerCase())
							) {
							type="7";
						}  else {
							type="5";
						}
					} else {
						j.setSuccess(false);
						j.setMsg("不能上传扩展名为空的文件！");
						return j;
					}
					SessionInfo s = (SessionInfo)httpSession.getAttribute(Const.SESSION_INFO);
					String noewPath = Util.isEmpty(s.getNowPath())?"":s.getNowPath();
					String partPathCid = Util.isEmpty(s.getPartPathCid())?"":s.getPartPathCid();
					// 获取存取路径-源文件
					String filePath = httpSession.getServletContext().getRealPath("/") + "files\\"+noewPath;
					
					// 文件夹
					File pathFile = new File(filePath);
					if (!pathFile.exists()) {
						pathFile.mkdirs();
					}
					
					// 获取存取路径
					String fileAndPath = filePath + oriName;

					// 新文件名
					String fileNameNew = IdUtil.generateSequenceNo() + "." + suffix;

					// 获取存取路径+名称
					String fileAndPathNew = filePath + fileNameNew;

					// 转存文件
					File newFile = new File(fileAndPathNew);
					if (!newFile.exists()) {
						// 如果文件不存在
						// 上传文件到指定路径
						file.transferTo(newFile);
													
						CFile cFile = new CFile();
						cFile.setName(oriName);
						cFile.setSize(file.getSize());
						cFile.setType(type);
						if(!Util.isEmpty(partPathCid)){
							CFile pcFile = new CFile(); pcFile.setCid(partPathCid);
							cFile.setPartDir(pcFile);
						}
						cFile.setOfficeType(suffix);
						cFile.setUrl(fileNameNew);
						cFile.setCrtDate(new Date());
						cFile.setCrtUser(userId);
						if(Util.isNotEmpty(cf.getClsCid())){
							Cls cls = clsService.findByCid(cf.getClsCid());
							cFile.setCls(cls);
						}	
						cFileService.addCFile(cFile);
						b = true;
						j.setMsg("上传成功！");
					} else {
						b = false;
						j.setMsg("该图片已经存在！");
					}
				} catch (IOException e) {
					b = false;
					j.setMsg("上传失败！");
				}
			}

			b = true;
		}
		if (b) {
			j.setSuccess(true);
			j.setMsg("上传成功！");
		} else {
			j.setSuccess(false);
			j.setMsg("上传失败！");
		}
		return j; 
	}
	
	/**
	 * 创建目录
	 */
	@RequestMapping(value = "/crtDir", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json crtDir(CFile cf, HttpSession session) {
		Json j = new Json();
		boolean b = false;
		String dirName = cf.getName();
		String type = cf.getType();
		if (!Util.isEmpty(cf.getCid()) && !Util.isEmpty(type) &&  !Util.isEmpty(dirName) && "0".equals(type)) {
			
			SessionInfo s = (SessionInfo)session.getAttribute(Const.SESSION_INFO);
			String noewPath = Util.isEmpty(s.getNowPath())?"":s.getNowPath();
			String partPathCid = Util.isEmpty(s.getPartPathCid())?"":s.getPartPathCid();
			
			// 获取存取路径-源文件
			String filePath = session.getServletContext().getRealPath("/") + "files\\"+noewPath;
			
			File newFile = new File(filePath+dirName);
			
			if (!newFile.exists()) {
				newFile.mkdirs();
				CFile cFile = new CFile();
				cFile.setName(dirName);
				//文件类型 （1：pic 2：doc 3：video 4：audio 5: other 0：dir）
				cFile.setType("0");
				cFile.setUrl(dirName+"\\");
				if(!Util.isEmpty(partPathCid)){
					CFile pcFile = new CFile(); pcFile.setCid(partPathCid);
					cFile.setPartDir(pcFile);
				}
				cFile.setCrtDate(new Date());
				cFile.setCrtUser(cf.getCid());
				
				cFileService.addCFile(cFile);
			}
			b = true;
		}
		if (b) {
			j.setSuccess(true);
			j.setMsg("创建成功！");
		} else {
			j.setSuccess(false);
			j.setMsg("创建失败！");
		}
		return j;
	}
	/**
	 * 重命名
	 */
	@RequestMapping(value = "/rename", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json rename(CFile cf, HttpSession session) {
		Json j = new Json();
		boolean b = false;
		String dirName = cf.getName();
		String cid = cf.getCid();
		
		if (!Util.isEmpty(cid) && !Util.isEmpty(dirName)) {
			CFile cfDb=cFileService.findByCid(cid);
			cfDb.setName(dirName);
			cFileService.update(cfDb);
			b = true;
		}
		if (b) {
			j.setSuccess(true);
			j.setMsg("重命名成功！");
		} else {
			j.setSuccess(false);
			j.setMsg("重命名失败！");
		}
		return j;
	}
	/**
	 * 删除目录或文件
	 */
	@RequestMapping(value = "/del", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json del(CFile cf, HttpSession session) {
		Json j = new Json();
		boolean b = true;
		String ids= cf.getIds();
		if(!Util.isEmpty(ids)){
			String[] str=ids.split(",");
			for (int i = 0; i < str.length; i++) {
				String id = str[i];
				CFile cFile=cFileService.findByCid(id);
				Date d = new Date();
				//文件类型 （1：pic 2：doc 3：video 4：audio 5: other 0：dir
				if(!"0".equals(cFile.getType())){
				//文件
					cFile.setDelFlg("1");
					cFile.setDelDate(d);
					cFileService.update(cFile);
					
				} else {
				//文件夹
					
					//临时删除文件夹
					cFile.setDelFlg("1");
					cFile.setDelDate(d);
					cFileService.update(cFile);
				}
			}
			b=true;
		}
		
		if (b) {
			j.setSuccess(true);
			j.setMsg("删除成功！");
		} else {
			j.setSuccess(false);
			j.setMsg("删除失败！");
		}
		return j;
	}
	/**
	 * 删除目录或文件
	 */
	@RequestMapping(value = "/delAll", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json delAll(CFile cf, HttpSession session) {
		Json j = new Json();
		boolean b = true;
		String ids= cf.getIds();
		if(!Util.isEmpty(ids)){
			String[] str=ids.split(",");
			for (int i = 0; i < str.length; i++) {
				String id = str[i];
				CFile cFile=cFileService.findByCid(id);
				
				
				//父亲路径
				List<PartPath> ppList = new ArrayList<PartPath>();
				CFile c= cFile;
				while(!Util.isEmpty(c.getPartDir())){
					c = cFileService.findByCid(c.getPartDir().getCid());
					PartPath pp = new PartPath();
					pp.setUrl(c.getUrl());
					ppList.add(pp);
				}
				//反转
				Collections.reverse(ppList);
				// 获取存取路径
				String filePath = session.getServletContext().getRealPath("/") + "files\\";
				for (PartPath partPath : ppList) {
					filePath=filePath+partPath.getUrl();
				}
				filePath=filePath+cFile.getUrl();
				
				 //文件类型 （1：pic 2：doc 3：video 4：audio 5: other  0：dir 6：pdf 7:txt 8:mi）
				if("0".equals(cFile.getType())){
					//文件夹
					
					//删除发送记录
					List<Trans> tranList = transService.findByMiFileCidOrReMiFileeCid(id);
					for (Trans trans : tranList) {
						trans.setIds(trans.getCid());
						transService.delete(trans);
					}
					
					//删除目录数据
					cFileService.delete(cFile);
					
					//删除目录及以下的文件夹
					FileUtils.deleteDirectory(filePath);
					
					
				} else {
					//文件夹
					
					//删除发送记录
					List<Trans> tranList = transService.findByMiFileCidOrReMiFileeCid(id);
					for (Trans trans : tranList) {
						trans.setIds(trans.getCid());
						transService.delete(trans);
					}
					//删除数据
					cFileService.delete(cFile);
					//删除文件
					FileUtils.deleteFile(filePath);
				}
			}
			b=true;
		}
		
		if (b) {
			j.setSuccess(true);
			j.setMsg("删除成功！");
		} else {
			j.setSuccess(false);
			j.setMsg("删除失败！");
		}
		return j;
	}
	/**
	 *取回目录或文件
	 */
	@RequestMapping(value = "/recover", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json recover(CFile cf, HttpSession session) {
		Json j = new Json();
		boolean b = true;
		String ids= cf.getIds();
		if(!Util.isEmpty(ids)){
			String[] str=ids.split(",");
			for (int i = 0; i < str.length; i++) {
				String id = str[i];
				CFile cFile=cFileService.findByCid(id);
				if(!"1".equals(cFile.getDelFlg())){
					j.setSuccess(false);
					j.setMsg("不是删除的文件不可以取回！");
					return j;
				}
				Date delDate=cFile.getDelDate();
				Date nowDate=new Date();
				
			   Calendar cal = Calendar.getInstance();
			   cal.setTime(delDate);
			   cal.add(Calendar.DATE, 30);
			   Date rDate = cal.getTime();
		
			   if(nowDate.after(rDate)){
				    j.setSuccess(false);
					j.setMsg("只能取回30天内删除的文件！");
					return j;
			   }
				//文件类型 （1：pic 2：doc 3：video 4：audio 5: other 0：dir
				if(!"0".equals(cFile.getType())){
				//文件
					//临时删除文件
					cFile.setDelFlg(null);
					cFile.setDelDate(null);
					cFileService.update(cFile);
					
				} else {
				//文件夹
					//临时删除文件夹
					cFile.setDelFlg(null);
					cFile.setDelDate(null);
					cFileService.update(cFile);
				}
			}
			b=true;
		}
		
		if (b) {
			j.setSuccess(true);
			j.setMsg("取回成功！");
		} else {
			j.setSuccess(false);
			j.setMsg("取回失败！");
		}
		return j;
	}
	/**
	 * 下载文件
	 */
	@RequestMapping("/download")
	public ResponseEntity<byte[]> download(CFile cf,HttpSession session) throws IOException {
		String cid=cf.getCid();
		if(Util.isNotEmpty(cid)){
			CFile cfile= cFileService.findByCid(cid);
			
			//父亲路径
			List<PartPath> ppList = new ArrayList<PartPath>();
			CFile c= cfile;
			while(!Util.isEmpty(c.getPartDir())){
				c = cFileService.findByCid(c.getPartDir().getCid());
				PartPath pp = new PartPath();
				pp.setUrl(c.getUrl());
				ppList.add(pp);
			}
			//反转
			Collections.reverse(ppList);
			// 获取存取路径
			String filePath = session.getServletContext().getRealPath("/") + "files\\";
			for (PartPath partPath : ppList) {
				filePath=filePath+partPath.getUrl();
			}
			filePath=filePath+cfile.getUrl();
			
			File file=new File(filePath);
			byte[] body = null;
	        //获取文件
	        InputStream is = new FileInputStream(file);
	        body = new byte[is.available()];
	        is.read(body);
	        HttpHeaders headers = new HttpHeaders();
	        //设置文件类型
	        headers.add("Content-Disposition", "attchement;filename=" + URLEncoder.encode(cfile.getName(),"UTF-8"));
	        //设置Http状态码
	    	HttpStatus statusCode = HttpStatus.OK;
	        //返回数据
	    	ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, statusCode);
	    	return entity;
		} else {
			ResponseEntity<byte[]> entity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    	return entity;
		}
		
	}
	
	/**
	 * 显示pdf文件
	 */
	@RequestMapping("/showPdf")
	public String showPdf(String path, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String realPath=request.getRealPath("/"); 
		path = path.replace("--", "\\");
		path = path.replace("=", "\\");
		File file = new File(realPath+path);
		if (!file.exists()) {
			request.setAttribute("error", "附件已删除或不存在");
			// return "/error";
		}
		InputStream in = null;
		OutputStream os = null;
		try {
			response.setContentType("application/pdf"); // 设置返回内容格式
			in = new FileInputStream(file); // 用该文件创建一个输入流
			os = response.getOutputStream();// 创建输出流
			byte[] b = new byte[1024];
			while (in.read(b) != -1) {
				os.write(b);
			}
			in.close();
			os.flush();
			os.close();
		} catch (Exception e) {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				if (null != os) {
					os.close();
				}
			} catch (IOException e2) {
				e2.printStackTrace();
			}

		}
		return null;
	 }
	
	/**
	 * 显示Txt文件
	 */
	@RequestMapping("/showTxt")
	public void showTxt(String path, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String realPath=request.getRealPath("/");  
		path = path.replace("--", "\\");
		path = path.replace("=", "\\");
		File file = new File(realPath+path);
		if (!file.exists()) {
			request.setAttribute("error", "附件已删除或不存在");
			// return "/error";
		}
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter pw = null;
		
		try {
			pw = response.getWriter();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			//解决中文乱码
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(realPath+path), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				pw.println(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		pw.flush();
	 }
	
	/**
	 * 接收并解密
	 */
	@RequestMapping(value = "/rec", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json rec(CFile cfile,HttpServletRequest req, HttpSession session) {
		Json j = new Json();
		boolean b = true;
		
		String cid= cfile.getCid();
		String clsCid= cfile.getClsCid();
		String userCid = userService.findUserByLoginCid(ShiroUtil.getCurrentInfo().getUserId()).getCid();
		
		if(!Util.isEmpty(cid) && !Util.isEmpty(clsCid) && !Util.isEmpty(userCid)){
		
			CFile cFileDb=cFileService.findByCid(cid);
			
			Trans transDb =transService.findByToUserCidAndMiFileCid(userCid,cid);
			
			//解密
			String fromUserCid =transDb.getFromUser().getCid();
			String toUserCid =transDb.getToUser().getCid();
			String oldFileCid =transDb.getOldFile().getCid();
			
			if (Util.isNotEmpty(cFileDb) && Util.isNotEmpty(transDb) && Util.isNotEmpty(fromUserCid) && Util.isNotEmpty(toUserCid) && Util.isNotEmpty(oldFileCid) ) {
				
					try {
						String key = transDb.getKeyNo();
						DesUtils desUtils = new DesUtils(key);
						
						CFile cf= cFileDb;
						
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
						String path = req.getRealPath("/")+"files";
						String toFilePath =path+"\\"+"temp\\"+cf.getUrl();
						String toFileName = "re-"+cf.getUrl();
						desUtils.doDecryptFile(toFilePath,path);  //解密
						transDb.setRecDate(new Date());
						
						//新增机密文件
						CFile reMiFile = new CFile();
						reMiFile.setCid(UUID.randomUUID().toString());
						reMiFile.setName(toFileName);
						reMiFile.setSize(cf.getSize());
						//文件类型 （1：pic 2：doc 3：video 4：audio 5: other  0：dir 6：pdf 7:txt 8:mi）
						reMiFile.setType(transDb.getOldFile().getType());
						if(Util.isNotEmpty(transDb.getOldFile().getOfficeType())){
							reMiFile.setOfficeType(transDb.getOldFile().getOfficeType());
						}
						reMiFile.setUrl(toFileName);
						reMiFile.setCrtDate(new Date());
						reMiFile.setCrtUser(toUserCid);
						Cls cls = new Cls();
						cls.setCid(clsCid);
						reMiFile.setCls(cls);
						cFileService.addCFile(reMiFile);
						
						transDb.setReMiFile(reMiFile);
						
						//修改传输
						transService.update(transDb);
						
						//删除加密文件
						 //删除标志(1:已删除)
						cFileDb.setDelFlg("1");
						cFileService.update(cFileDb);
						
						b = true;
					} catch (Exception e) {
						b = false;
						j.setSuccess(false);
						j.setMsg("接收并解密失败！");
						return j;
					} 
				
				} else {
					b = false;
				}
		}
		
		if (b) {
			j.setSuccess(true);
			j.setMsg("接收并解密成功！");
		} else {
			j.setSuccess(false);
			j.setMsg("接收并解密失败！");
		}
		return j;
	}
}
