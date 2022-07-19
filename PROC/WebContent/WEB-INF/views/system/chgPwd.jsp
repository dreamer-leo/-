<%@ page contentType="text/html;charset=utf-8" %> 
<!DOCTYPE html>
<html lang="zh">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>忘记密码</title>
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
	<%--layui--%>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugins/layui/css/layui.css" />
	<script src="${pageContext.request.contextPath}/static/plugins/layui/layui.all.js"></script>
	
	<script type="text/javascript">
		function sentCode(){
			 var cname = $("#username").val();
			 var cpwd = $("#password").val();
			 if(cname == "undefined" || cname == null || cname == "") {
		    	layer.msg("用户名不能为空，请填写完全！");
			 } else if(cpwd == "undefined" || cpwd == null || cpwd == ""){
			 	layer.msg("新密码不能为空，请填写完全！");
			 } else {
			 	 layer.msg("正在发送请稍等。。。");
			 	 $("#sendBtn").attr("disabled","disabled");
				 $.ajax({
		            type : "post",
		            url : "${pageContext.request.contextPath}/user/sentCode",
		            data:{'cname':cname},
		            async : true,
		            cache : false,
		            dataType : 'json',
		            success : function(data) {
		            	if(data.success) {
		            		 $("#AntRegUserCode").removeAttr("disabled");
		            	}
		                layer.alert(data.msg);
		            },
		            error : function(data) {
		            	layer.msg(data.responseText);
		            }
		    	});
			 }
			 
		}
		function getPwd(){
			 var cname = $("#username").val();
			 var cpwd = $("#password").val();
			 var validCode = $("#AntRegUserCode").val();
			 if((cname == "undefined" || cname == null || cname == "")||(cpwd == "undefined" || cpwd == null || cpwd == "")||(validCode == "undefined" || validCode == null || validCode == "")) {
			 	layer.alert("用户名，新密码，验证码都不能为空，请填写完全！");
			 } else {
			 	$.ajax({
		            type : "post",
		            url : "${pageContext.request.contextPath}/user/doGetCode",
		            data:{'cname':cname,'cpwd':cpwd,'validCode':validCode},
		            async : true,
		            cache : false,
		            dataType : 'json',
		            success : function(data) {
		            	if(data.success) {
		            	   layer.alert(data.msg+"请点击，跳转到登录页面");
		            	    setTimeout( function(){
                              window.location.href="${pageContext.request.contextPath}/login";
                      		}, 5*1000 );//延迟5000毫米
		                } else {
		                	layer.msg(data.msg);
		                }
		            },
		            error : function(data) {
		            	layer.msg(data.responseText);
		            }
		    	});
			 }
		}
	</script>
</head>
<body>
	<div class="htmleaf-container">
		<div class="demo form-bg">
	        <div class="container">
	            <div class="row">
	                <div class="col-md-offset-3 col-md-6">
	                    <form class="form-horizontal" id="loginFormId"  name="form" action="${pageContext.request.contextPath}/user/doGetCode" method="post">
	                        <span class="heading">取回密码</span>
	                        <div class="form-group">
	                            <input type="text" id="username" name="cname" class="form-control" placeholder="用户名">
	                            <i class="fa fa-user"></i>
	                        </div>
	                        <div class="form-group">
	                            <input type="password" id="password" name="cpwd" class="form-control"  placeholder="新密码">
	                            <i class="fa fa-lock"></i>
	                        </div>
	                       <div class="form-group">
	                            <input id="AntRegUserCode" name="validCode" type="text" style="width:80%;display:inline;" disabled="disabled" class="form-control" placeholder="验证码">
	                            <input type="button"  id="sendBtn" class="btn btn-info" onclick="sentCode();" value="发送" />
	                            <i class="fa fa-barcode"></i>
	                        </div>
	                        <div class="form-group">
	                        	<input type="button" name="getBtn" value="取回" onclick="getPwd();" class="btn btn-default" style="float:none">
<!-- 	                            <button type="submit" class="btn btn-default" style="float:none;">取回</button> -->
<!-- 	                            <button type="submit" class="btn btn-default" style="float:none;">取回</button> -->
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