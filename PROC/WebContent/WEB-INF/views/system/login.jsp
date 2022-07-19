<%@ page contentType="text/html;charset=utf-8" %> 
<!DOCTYPE html>
<html lang="zh">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>登录</title>
	 <script src="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrap-3.3.7-dist/js/jquery/jquery-1.12.4.min.js"></script>
	<link href="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/static/plugins/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/static/login/css/htmleaf-demo.css" rel="stylesheet" >
	<link href="${pageContext.request.contextPath}/static/login/css/login.css" rel="stylesheet" >
<style type="text/css">
	body {
	    background: url(${pageContext.request.contextPath}/static/login/img/bg.jpg) no-repeat fixed;
	    background-size: 100% 100%;
	    width: 100%;
	    height: 100%;
	}
</style>
</head>

<body>
	<div class="htmleaf-container">
		<div class="demo form-bg">
	        <div class="container">
	            <div class="row">
	                <div class="col-md-offset-3 col-md-6">
	                    <form class="form-horizontal" id="loginFormId"  name="form" action="${pageContext.request.contextPath}/back/doLogin#page/homepage" method="post">
	                        <span class="heading">用户登录</span>
	                        <div class="form-group">
	                            <input type="text" id="username" name="cname" class="form-control" placeholder="用户名">
	                            <i class="fa fa-user"></i>
	                        </div>
	                        <div class="form-group">
	                            <input type="password" id="password" name="cpwd" class="form-control"  placeholder="密码">
	                            <i class="fa fa-lock"></i>
	                        </div>
	                        <div class="form-group">
	                            <input id="AntRegUserCode" type="text" name="validCode" class="form-control" placeholder="验证码">
	                            <i class="fa fa-barcode"></i>
	                        </div>
	                         <div class="form-group">
	                            <img src="${pageContext.request.contextPath}/kaptcha.jpg" id="regCodeImage" title="看不清楚？换一张" style="cursor: pointer;" onclick="this.src='${pageContext.request.contextPath}/kaptcha.jpg'+'?noCache='+Math.random();" />
	                        </div>
	                        <div class="form-group">
<!-- 	                            <div class="main-checkbox"> -->
<!-- 	                                <input type="checkbox" value="" name="check"/> -->
<!-- 	                                <label for="checkbox1"></label> -->
<!-- 	                            </div> -->
 								<span class="text"><input type="checkbox" name="rememberMe"/></span>
	                            <span class="text">记住我 </span>
	                            <span class="text" >
	                            <a style="color:gray" href="${pageContext.request.contextPath}/user/toGetpwd" target="_blank">忘了密码？</a> </span>
	                            <button type="submit" class="btn btn-default">立刻登录</button>
								<a class="btn btn-default" href="${pageContext.request.contextPath}/toReg" target="_blank">注册</a>
	                        </div>
	                    </form>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
	<script type="text/javascript">
	$(document).ready(function() {
		var	msg = '${msg}';
		if(typeof msg == "undefined" || msg == null || msg == ""){
		} else {
			alert(msg);
		}
	});
	</script>
</body>
</html>