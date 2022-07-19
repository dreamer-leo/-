<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>歌曲MV</title>

<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta http-equiv="Cache-Control" content="no-siteapp">
<meta name="keywords" content="">
<meta name="description" content="">
</head>
<body style="background: rgb(244, 244, 244); margin-top: 0px;">
<video src="${pageContext.request.contextPath}/${p}" controls="controls" autoplay="autoplay" controlslist="nodownload" height="100%" width="100%">
您的浏览器不支持 video 标签。
</video>
</body>
</html>