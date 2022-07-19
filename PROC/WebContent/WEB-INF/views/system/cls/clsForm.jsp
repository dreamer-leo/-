<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>分类表单</title>
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
        <input type="text" name="cid" value="${cls.cid}" class="layui-input">
      </div>
    </div>
	<div class="layui-form-item">
      <label class="layui-form-label">名称</label>
      <div class="layui-input-inline">
        <input type="text" name="nm" value="${cls.nm}" lay-verify="required" placeholder="请输入名称" autocomplete="off" class="layui-input">
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
  }).use(['jquery','index', 'form', 'upload','laydate'], function(){
	  
	  
    var $ = layui.$
    ,form = layui.form
    ,upload = layui.upload ;
 	var laydate = layui.laydate;

    
	//选中外键实体
    layui.form.render();//刷新表单
    
    
    
  });
  
  </script>
</body>
</html>