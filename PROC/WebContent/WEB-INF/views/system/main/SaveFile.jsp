<%@ page language="java" import="java.util.*,com.zhuozhengsoft.pageoffice.*" pageEncoding="utf-8"%>
<%
String basePath=request.getContextPath();
FileSaver fs=new FileSaver(request,response);

String url=(String)request.getAttribute("p");

fs.saveToFile(request.getSession().getServletContext().getRealPath("/")+url);
//fs.saveToFile(request.getSession().getServletContext().getRealPath("/")+fs.getFileName());
//fs.saveToFile(request.getSession().getServletContext().getRealPath("files/02212215113320000.doc")+"/"+fs.getFileName());
fs.close();
%>

