 <%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>layuiAdmin 网站用户 iframe 框</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugins/layuiadmin/layui/css/layui.css" media="all">
</head>
<body>

  <div class="layui-form" lay-filter="layuiadmin-form-useradmin" id="layuiadmin-form-useradmin" style="padding: 20px 0 0 0;">
  	<div class="layui-form-item" style="display:none">
      <label class="layui-form-label">CID</label>
      <div class="layui-input-inline">
        <input type="text" name="cid" value="${user.cid}" class="layui-input">
      </div>
    </div>
    <div class="layui-form-item">
      <label class="layui-form-label">头像</label>
      <div class="layui-input-inline">
      	<c:if test="${!empty user.icon}">
      		 <img id="img" src="${pageContext.request.contextPath}/${user.icon}" style="width:40px;height:40px;box-shadow: 0 0 0 4px rgba(0,0,0,0.1);border-radius: 50%;">
      	</c:if>
      	<c:if test="${empty user.icon}">
      		 <img id="img" src="${pageContext.request.contextPath}/upload/icon/noIcon.jpg" style="width:40px;height:40px;box-shadow: 0 0 0 4px rgba(0,0,0,0.1);border-radius: 50%;">
      	</c:if>
      </div>
    </div>
    <div class="layui-form-item">
      <label class="layui-form-label">选择图片</label>
      <div class="layui-input-inline">
        <input id="icon" type="text" name="icon" value="${user.icon}" lay-verify="required" placeholder="请上传图片" autocomplete="off" class="layui-input"  readonly="readonly">
      </div>
      <button style="float: left;" type="button" class="layui-btn" id="layuiadmin-upload-useradmin">上传图片</button> 
    </div>
    <div class="layui-form-item">
      <label class="layui-form-label">用户名</label>
      <div class="layui-input-inline">
        <input type="text" name="cname" value="${user.login.cname}" lay-verify="required" placeholder="请输入用户名" autocomplete="off" class="layui-input">
      </div>
    </div>
    <div class="layui-form-item">
      <label class="layui-form-label">密码<font color="red">(注:不修改留空)</font></label>
      <div class="layui-input-inline">
        <input type="password" name="cpwd"  autocomplete="off" class="layui-input">
      </div>
    </div>
    <div class="layui-form-item">
      <label class="layui-form-label">真实姓名</label>
      <div class="layui-input-inline">
        <input type="text" name="crealname" value="${user.crealname}" lay-verify="required" placeholder="请输入真实姓名" autocomplete="off" class="layui-input">
      </div>
    </div>
    <div class="layui-form-item">
      <label class="layui-form-label">学号</label>
      <div class="layui-input-inline">
        <input type="text" name="studentId" value="${user.studentId}" lay-verify="required" placeholder="请输入学号" autocomplete="off" class="layui-input">
      </div>
    </div>
    <div class="layui-form-item">
      <label class="layui-form-label">院系</label>
      <div class="layui-input-inline">
        <input type="text" name="dept" value="${user.dept}" lay-verify="required" placeholder="请输入院系" autocomplete="off" class="layui-input">
      </div>
    </div>
    <div class="layui-form-item">
      <label class="layui-form-label">专业</label>
      <div class="layui-input-inline">
        <input type="text" name="major" value="${user.major}" lay-verify="required" placeholder="请输入学号" autocomplete="off" class="layui-input">
      </div>
    </div>
    
    <div class="layui-form-item">
      <label class="layui-form-label">邮箱</label>
      <div class="layui-input-inline">
        <input type="text" name="email" value="${user.email}" lay-verify="email" placeholder="请输入邮箱" autocomplete="off" class="layui-input">
      </div>
    </div>
    
    <div class="layui-form-item">
      <label class="layui-form-label">出生日期</label>
      <div class="layui-input-inline">
        <input type="text" id="birthday" name="birthday" value="${user.birthdayString}" lay-verify="required" placeholder="请输入选择出生日期" autocomplete="off" class="layui-input">
      </div>
    </div>
    
    <div class="layui-form-item">
      <label class="layui-form-label">兴趣爱好</label>
      <div class="layui-input-inline">
        <input type="text" name="hobby" value="${user.hobby}" lay-verify="required" placeholder="请输入兴趣爱好" autocomplete="off" class="layui-input">
      </div>
    </div>
  
    <div class="layui-form-item" lay-filter="userGroup">
      <label class="layui-form-label">选择角色</label>
	      <div class="layui-input-block">
	        <input type="radio" name="type" value="会员" title="会员" checked>
	        <input type="radio" name="type" value="管理员" title="管理员">
	      </div>
    </div>
    <div class="layui-form-item layui-hide">
      <input type="button" lay-submit lay-filter="LAY-user-front-submit" id="LAY-user-front-submit" value="确认">
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/static/plugins/layuiadmin/layui/layui.js"></script>  
  <script>
  layui.config({
    base: '${pageContext.request.contextPath}/static/plugins/layuiadmin/' //静态资源所在路径
  }).extend({
    index: 'lib/index' //主入口模块
  }).use(['jquery','index', 'form', 'upload', 'laydate'], function(){
	  
	  
    var $ = layui.$
    ,form = layui.form
    ,upload = layui.upload
    ,laydate = layui.laydate;
    //选中单选
    var type='${user.type}';
    if(typeof type == "undefined" || type == null || type == ""){
  	 
    } else {
  	  // 给radio 赋值, 选中值为sex的radio：
  	  $("input[name='type'][value='"+type+"']").attr("checked",true); 
    }
    
    
    laydate.render({
        elem: '#birthday' //指定元素
        //,type: 'datetime' //选择时间 默认不含时间
        ,trigger: 'click'
    });
    
    layui.form.render();//刷新表单
    
    
    //上传事件
    upload.render({
      elem: '#layuiadmin-upload-useradmin'
      ,url: '${pageContext.request.contextPath}/user/uploadIcon?cid=${user.cid}'
   	  ,before: function(obj){
           //预读本地文件示例，不支持ie8
           obj.preview(function(index, file, result){
             $('#img').attr('src', result); //图片链接（base64）
           });
      }
      ,accept: 'images'
      ,method: 'get'
      ,acceptMime: 'image/*'
      ,done: function(res){
    	//alert(res.success);
    	//alert(res.obj);
    	if(res.obj){
    		$("#icon").val(res.obj);
    		form.render();
    	}
    	layer.msg(res.msg);
      }
    });
    
    
   
    
    
  });
  
 
  
  </script>
</body>
</html>