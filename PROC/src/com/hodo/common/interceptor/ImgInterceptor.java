package com.hodo.common.interceptor;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class ImgInterceptor implements HandlerInterceptor {

	//响应用户请求（调用控制器对应方法）之前执行
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res,
			Object obj) throws Exception {
		// TODO Auto-generated method stub
		if(req instanceof MultipartHttpServletRequest){
			MultipartHttpServletRequest multipartReq=(MultipartHttpServletRequest)req;
			MultiValueMap<String, MultipartFile> fileMap=multipartReq.getMultiFileMap();
			Iterator<String> mapNameIt=fileMap.keySet().iterator();
			while(mapNameIt.hasNext()){
				String imgKey=mapNameIt.next();
				List<MultipartFile> currFileList=fileMap.get(imgKey);
				for(int i=0;i<currFileList.size();i++){
					MultipartFile currFile=currFileList.get(i);
					if(currFile.getSize()>204800){
						req.setAttribute("errorObjName", imgKey);
						req.setAttribute("errorMsg", "文件大小超标");
						req.getRequestDispatcher("/jsp/error/error.jsp").forward(req, res);
						return false;
					}
					if(!currFile.getContentType().startsWith("image/")){
						req.setAttribute("errorObjName", imgKey);
						req.setAttribute("errorMsg", "文件不是图片");
						req.getRequestDispatcher("/jsp/error/error.jsp").forward(req, res);
						return false;
					}
				}
			}
			return true;
		}else{
			return true;
		}
		
	}
	//当控制器返回结果时（请求转发到某个页面前）执行
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub

	}
	
	//当View将结果返回给用户之前执行
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	



}
