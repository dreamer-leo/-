<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.zhuozhengsoft.pageoffice.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.pageoffice.cn" prefix="po" %>

<%
String basePath=request.getContextPath();
PDFCtrl poCtrl1 = null;

PageOfficeCtrl poCtrl=new PageOfficeCtrl(request);
//设置服务器页面
poCtrl.setServerPage(request.getContextPath()+"/poserver.zz");
poCtrl.addCustomToolButton("保存", "Save()", 1);
//poCtrl.addCustomToolButton("打印", "PrintFile()", 6);
poCtrl.addCustomToolButton("全屏/还原", "IsFullScreen()", 4);
poCtrl.addCustomToolButton("关闭", "CloseFile()", 21);

String url=(String)request.getAttribute("p");//路径
String officeType=(String)request.getAttribute("officeType");//文件类型

//设置保存页面
poCtrl.setSaveFilePage(basePath+"/back/toSaveFile?filePath="+url);
//打开Word文档

if("doc".equals(officeType)){
	poCtrl.webOpen(basePath+"/"+url,OpenModeType.docNormalEdit,"张佚名");
}
if("docx".equals(officeType)){
	poCtrl.webOpen(basePath+"/"+url,OpenModeType.docNormalEdit,"张佚名");
}
if("xls".equals(officeType)){
	poCtrl.webOpen(basePath+"/"+url,OpenModeType.xlsNormalEdit,"张佚名");
}
if("xlsx".equals(officeType)){
	poCtrl.webOpen(basePath+"/"+url,OpenModeType.xlsNormalEdit,"张佚名");
}
if("ppt".equals(officeType)){
	poCtrl.webOpen(basePath+"/"+url,OpenModeType.pptNormalEdit,"张佚名");
}
if("pptx".equals(officeType)){
	poCtrl.webOpen(basePath+"/"+url,OpenModeType.pptNormalEdit,"张佚名");
}
if("pdf".equals(officeType)){
	
	poCtrl1 = new PDFCtrl(request);
	poCtrl1.setServerPage(request.getContextPath()+"/poserver.zz"); //此行必须

	// Create custom toolbar
	poCtrl1.addCustomToolButton("打印", "Print()", 6);
	poCtrl1.addCustomToolButton("隐藏/显示书签", "SetBookmarks()", 0);
	poCtrl1.addCustomToolButton("-", "", 0);
	poCtrl1.addCustomToolButton("实际大小", "SetPageReal()", 16);
	poCtrl1.addCustomToolButton("适合页面", "SetPageFit()", 17);
	poCtrl1.addCustomToolButton("适合宽度", "SetPageWidth()", 18);
	poCtrl1.addCustomToolButton("-", "", 0);
	poCtrl1.addCustomToolButton("首页", "FirstPage()", 8);
	poCtrl1.addCustomToolButton("上一页", "PreviousPage()", 9);
	poCtrl1.addCustomToolButton("下一页", "NextPage()", 10);
	poCtrl1.addCustomToolButton("尾页", "LastPage()", 11);
	poCtrl1.addCustomToolButton("-", "", 0);
	poCtrl1.webOpen(basePath+"/"+url);

}

//poCtrl.webOpen(basePath+"/files/02212215113320000.doc",OpenModeType.docNormalEdit,"张佚名");
%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>office</title>

<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta http-equiv="Cache-Control" content="no-siteapp">
<meta name="keywords" content="">
<meta name="description" content="">
</head>
<body style="background: rgb(244, 244, 244); margin-top: 0px;">
<!-- PageOffice.js文件一定要引用 -->
 <script type="text/javascript" src="${pageContext.request.contextPath}/jquery.min.js"></script>
 <script type="text/javascript" src="${pageContext.request.contextPath}/pageoffice.js"  id="po_js_main"></script>
    <script type="text/javascript">
        function Save() {
             document.getElementById("PageOfficeCtrl1").WebSave();
             
           }
        //function PrintFile(){
        //     document.getElementById("PageOfficeCtrl1").ShowDialog(4); 
             
        //   }
        function IsFullScreen(){
             document.getElementById("PageOfficeCtrl1").FullScreen = !document.getElementById("PageOfficeCtrl1").FullScreen;
             
           }
        function CloseFile(){
             POBrowser.closeWindow(); 
             
           }
        
        //pdf
        function AfterDocumentOpened() {
	        //alert(document.getElementById("PDFCtrl1").Caption);
	    }
	    function SetBookmarks() {
	        document.getElementById("PDFCtrl1").BookmarksVisible = !document.getElementById("PDFCtrl1").BookmarksVisible;
	    }
	    
	    function PrintFile() {
	        document.getElementById("PDFCtrl1").ShowDialog(4);
	    }
	    function SwitchFullScreen() {
	        document.getElementById("PDFCtrl1").FullScreen = !document.getElementById("PDFCtrl1").FullScreen;
	    }
	    function SetPageReal() {
	        document.getElementById("PDFCtrl1").SetPageFit(1);
	    }
	    function SetPageFit() {
	        document.getElementById("PDFCtrl1").SetPageFit(2);
	    }
	    function SetPageWidth() {
	        document.getElementById("PDFCtrl1").SetPageFit(3);
	    }
	    function ZoomIn() {
	        document.getElementById("PDFCtrl1").ZoomIn();
	    }
	    function ZoomOut() {
	        document.getElementById("PDFCtrl1").ZoomOut();
	    }
	    function FirstPage() {
	        document.getElementById("PDFCtrl1").GoToFirstPage();
	    }
	    function PreviousPage() {
	        document.getElementById("PDFCtrl1").GoToPreviousPage();
	    }
	    function NextPage() {
	        document.getElementById("PDFCtrl1").GoToNextPage();
	    }
	    function LastPage() {
	        document.getElementById("PDFCtrl1").GoToLastPage();
	    }
	    function RotateRight() {
	        document.getElementById("PDFCtrl1").RotateRight();
	    }
	    function RotateLeft() {
	        document.getElementById("PDFCtrl1").RotateLeft();
	    }
	    //pdf
    </script>
    <% 
     	if(poCtrl1==null){
     %>		
	 <div style="position: absolute;width:100%; height:100%;">
        <%=poCtrl.getHtmlCode("PageOfficeCtrl1")%>
     </div>
     <%  }%>	
     
     <% 
     	if(poCtrl1!=null){
     %>		
	     <div id="content"  style="height:768px;width:100%;overflow-y:auto;">
		 	 <%=poCtrl1.getHtmlCode("PDFCtrl1")%>
		  </div>
     <%  }%>	
	 	
	 
</body>
</html>