<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<meta http-equiv="X-UA-Compatible" content="IE=7,9,10,11">
<meta name="renderer" content="webkit">
<title>docSec</title>

<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
<meta content="" name="keywords">
<meta content="" name="description">

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/main/css/alpha.css">

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/main/css/function.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/main/css/context-all_52aae01.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/main/css/home-all_dc4f991.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/main/css/all_1c34261.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/main/css/all_5560f2c.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/main/css/main-all_1804208.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/main/css/quota-all_707c5f8.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/main/css/disk.css">

<%--jq--%>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrap-3.3.7-dist/js/jquery/jquery-1.12.4.min.js"></script>
<!-- bootstrap & fontawesome -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrap-3.3.7-dist/css/bootstrap.css" />
<script src="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrap-3.3.7-dist/js/bootstrap.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/plugins/font-awesome-4.7.0/css/font-awesome.css" />
<%--layui--%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/plugins/layui/css/layui.css" />
<script src="${pageContext.request.contextPath}/static/plugins/layui/layui.all.js"></script>
        
<script src="${pageContext.request.contextPath}/static/plugins/jq/jqueryForm/jquery.form.js"></script>

 <!--PageOffice.js和jquery.min.js文件一定要引用-->
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/jquery.min.js"></script>--%>
<script type="text/javascript" src="${pageContext.request.contextPath}/pageoffice.js" id="po_js_main"></script>

<script type="text/javascript">
	
	$(function(){
		//初始化文件显示样式
		var type ='${sessionScope.sessionInfo.showType}';
		if(typeof type == "undefined" || type == null || type == "") {
			$('#big').hide();$('#small').hide();$('#detail').show();
		} else if(type=='big') {
			$('#small').hide();$('#detail').hide();$('#big').show();
		} else if(type=='small') {
			$('#big').hide();$('#detail').hide();$('#small').show();
		} else if(type=='detail') {
			$('#big').hide();$('#small').hide();$('#detail').show();
		}
		
		
		$(".DIcOFyb").hover(function() {
			$(this).addClass("mouseon");
			$(".fa-chevron-down").addClass("fa-rotate-180");
		}, function() {
			 setTimeout( function(){
				 $(".DIcOFyb").removeClass("mouseon")
				 $(".fa-chevron-down").removeClass("fa-rotate-180");
        	 },4* 1000 );//延迟5000毫米
		});
		
		//全选 
		$('#checkboxAll').change(function(){  
			$('.vdAfKMb').find(':checkbox').prop('checked',$(this).is(':checked')?true:false);
			var obj = $('.vdAfKMb').find(':checkbox');
			var check_val = [];
		    for(k in obj){
		        if(obj[k].checked)
		            check_val.push(obj[k].value);
		    }
		}); 
		
	});
	//创建文件夹
	function crtDir(){
		layer.prompt({
			formType: 0,
		  	value: '',
		  	title: '请输入文件夹名'
		}, function(value,index){
		     $.ajax({
		            type : "post",
		            url : "${pageContext.request.contextPath}/cfile/crtDir",
		            data:{'name':value,'cid':'${user.cid}','type':'0'},
		            async : true,
		            cache : false,
		            dataType : 'json',
		            success : function(data) {
		                layer.msg(data.msg);
		                setTimeout( function(){
                            window.location.reload();
                    	}, 1.5 * 1000 );//延迟5000毫米
		            },
		            error : function(data) {
		              alert("error:" + data.responseText);
		            }
		    });
			
		  	layer.close(index);
		});
	}
	
	//获得checkBox-通用
	function getCheckBox(showType){
		var showType ='${sessionScope.sessionInfo.showType}';
		if(typeof showType == "undefined" || showType == null || showType == "") {
			showType='detail';
		}
		var obj;
		if("big"==showType){
			obj = $('#big .badge').find(':checkbox');
		} else if("small"==showType){
			obj = $('#small .badge').find(':checkbox');
		} else if("detail"==showType){
			obj = $('.vdAfKMb').find(':checkbox');
		} else {
			obj = $('.vdAfKMb').find(':checkbox');
		}
		var check_val = [];
		var type_name = [];
		var file_name = [];
	    for(k in obj){
	        if(obj[k].checked) {
	        	check_val.push(obj[k].value);
	        	type_name.push(obj[k].name);
	        	file_name.push($(obj[k]).attr('filename'));
	        }
	    }
	    var names=new Array(check_val,type_name,file_name);
	    return names;
	}
	//重新命名
	function rename(name){
		var names=getCheckBox();
	    var check_val=names[0];
	    var type_name=names[1];
	    var file_name=names[2];
	    if(check_val.length!=1 || typeof check_val[0] == "undefined" || check_val[0] == null || check_val[0] == ""){
	    	layer.msg('请选择一个文件或文件夹!');
	    } else {
	    	//if("0"!=type_name[0]) {
	    	//	layer.msg('只能重命名文件夹!');
	    	//	return;
	    	//}
	    	layer.prompt({
    			formType: 0,
    		  	value: file_name[0],
    		  	title: '请输入新名称'
    		}, function(value,index){
 		    	$.ajax({
   		            type : "post",
   		            url : "${pageContext.request.contextPath}/cfile/rename",
   		            data:{'name':value,'cid':check_val[0]},
   		            async : true,
   		            cache : false,
   		            dataType : 'json',
   		            success : function(data) {
   		                layer.msg(data.msg);
   		                setTimeout( function(){
                               window.location.reload();
                       	}, 1.5 * 1000 );//延迟5000毫米
   		            },
   		            error : function(data) {
   		              alert("error:" + data.responseText);
   		            }
   		    	});
    		    	  
    		  	layer.close(index);
    		});
	    }
	}
	//下载
	function down(){
		var names=getCheckBox();
	    var check_val=names[0];
	    var type_name=names[1];
	    
	    if(check_val.length!=1 || typeof check_val[0] == "undefined" || check_val[0] == null || check_val[0] == ""){
	    	layer.msg('请选择一个文件!');
	    } else {
	    	if("0"==type_name[0]) {
	    		layer.msg('只能下载文件!');
	    		return;
	    	}
	    	 window.location.href="${pageContext.request.contextPath}/cfile/download?cid="+check_val[0];
	    	  
	    }
	}
	//删除
	function delAll(){
		var names=getCheckBox();
	    var check_val=names[0];
	    var type_name=names[1];
	
	    if(check_val.length>0){
	    	  $.ajax({
	              type : "post",
	              url : "${pageContext.request.contextPath}/cfile/delAll",
	              data:{'ids':check_val.join(',')},
	              async : true,
	              cache : false,
	              dataType : 'json',
	              success : function(data) {
	                  layer.msg(data.msg);
	                  setTimeout( function(){
                          window.location.reload();
                  	  }, 1.5 * 1000 );//延迟5000毫米
	              },
	              error : function(data) {
	                alert("error:" + data.responseText);
	              }
	         });
	    	
	    } else {
	    	layer.msg('请选择文件或文件夹!');
	    }
	}
	//删除
	function del(){
		var names=getCheckBox();
	    var check_val=names[0];
	    var type_name=names[1];
	
	    if(check_val.length>0){
	    	  $.ajax({
	              type : "post",
	              url : "${pageContext.request.contextPath}/cfile/del",
	              data:{'ids':check_val.join(',')},
	              async : true,
	              cache : false,
	              dataType : 'json',
	              success : function(data) {
	                  layer.msg(data.msg);
	                  setTimeout( function(){
                          window.location.reload();
                  	  }, 1.5 * 1000 );//延迟5000毫米
	              },
	              error : function(data) {
	                alert("error:" + data.responseText);
	              }
	         });
	    	
	    } else {
	    	layer.msg('请选择文件或文件夹!');
	    }
	}
	//取回
	function recover(){
		var names=getCheckBox();
	    var check_val=names[0];
	    var type_name=names[1];
	
	    if(check_val.length>0){
	    	  $.ajax({
	              type : "post",
	              url : "${pageContext.request.contextPath}/cfile/recover",
	              data:{'ids':check_val.join(',')},
	              async : true,
	              cache : false,
	              dataType : 'json',
	              success : function(data) {
	                  layer.msg(data.msg);
	                  if(data.success){
	                	  setTimeout( function(){
	                          window.location.reload();
	                  	  }, 1.5 * 1000 );//延迟5000毫米
	                  }
	              },
	              error : function(data) {
	                alert("error:" + data.responseText);
	              }
	         });
	    	
	    } else {
	    	layer.msg('请选择文件或文件夹!');
	    }
	}
	
	//发送
	function send(){
		var names=getCheckBox();
	    var check_val=names[0];
	    var type_name=names[1];
	    
	    if(check_val.length!=1 || typeof check_val[0] == "undefined" || check_val[0] == null || check_val[0] == ""){
	    	layer.msg('请选择一个文件!');
	    } else {
	    	if("0"==type_name[0]) {
	    		layer.msg('只能传输文件!');
	    		return;
	    	}
	    	
    	 	layer.open({
                 title: '用户选择'
                 , btn: ['确定']
                 , content: '<div class="layui-form-item">' +
                     '<label class="layui-form-label">用户</label>' +
                     '<div class="layui-input-block">' +
                     '<select name="range" style="line-height: 20px;padding: 9px 15px;" id="range" lay-filter="range">' +
                     <c:forEach items="${userList}" var="user" varStatus="status">
                     '<option value="${user.cid}">${user.crealname}</option>' +
                     </c:forEach>
                     '</select>' +
                     '</div>' +
                     '</div>',
                     yes: function (index,layero) {
                         var fromUserCid = '${user.cid}';
                         var toUserCid = $('#range').val();
                         var oldFileCid=check_val[0];
                         
                         $.ajax({
	           	              type : "post",
	           	              url : "${pageContext.request.contextPath}/trans/send",
	           	              data:{'fromUserCid':fromUserCid,'toUserCid':toUserCid,'oldFileCid':oldFileCid},
	           	              async : true,
	           	              cache : false,
	           	              dataType : 'json',
	           	              success : function(data) {
	           	                  layer.msg(data.msg);
	           	                  if(data.success){
	           	                	  setTimeout( function(){
	           	                          window.location.reload();
	           	                  	  }, 1.5 * 1000 );//延迟5000毫米
	           	                  }
	           	              },
	           	              error : function(data) {
	           	                alert("error:" + data.responseText);
	           	              }
	           	         });
                         
                         
             		}
             });
	    	  
	    	
	    	  
	    }
	    
	}
	
	
	//接收
	function rec(){
		var names=getCheckBox();
	    var check_val=names[0];
	    var type_name=names[1];
	    
	    if(check_val.length!=1 || typeof check_val[0] == "undefined" || check_val[0] == null || check_val[0] == ""){
	    	layer.msg('请选择一个文件!');
	    } else {
	    	if("0"==type_name[0]) {
	    		layer.msg('只能传输文件!');
	    		return;
	    	}
	    	
    	 	layer.open({
                 title: '类型选择'
                 , btn: ['确定']
                 , content: '<div class="layui-form-item">' +
                     '<label class="layui-form-label">类型</label>' +
                     '<div class="layui-input-block">' +
                     '<select name="range" style="line-height: 20px;padding: 9px 15px;" id="range" lay-filter="range">' +
                     <c:forEach items="${clsList}" var="cls" varStatus="status">
                     '<option value="${cls.cid}">${cls.nm}</option>' +
                     </c:forEach>
                     '</select>' +
                     '</div>' +
                     '</div>',
                     yes: function (index,layero) {
                         var fromUserCid = '${user.cid}';
                         var clsCid = $('#range').val();
                         var cid=check_val[0];
                         
                    
                         $.ajax({
	           	              type : "post",
	           	              url : "${pageContext.request.contextPath}/cfile/rec",
	           	              data:{'clsCid':clsCid,'cid':cid},
	           	              async : true,
	           	              cache : false,
	           	              dataType : 'json',
	           	              success : function(data) {
	           	                  layer.msg(data.msg);
	           	                  if(data.success){
	           	                	  setTimeout( function(){
	           	                          window.location.reload();
	           	                  	  }, 1.5 * 1000 );//延迟5000毫米
	           	                  }
	           	              },
	           	              error : function(data) {
	           	                alert("error:" + data.responseText);
	           	              }
	           	         });
                         
                         
             		}
             });
	    }
	    
	}
	//图片预览
	function previewImg(path) {
		var p = "${pageContext.request.contextPath}/"+path;
		p=p.replace("\\", "/");
        var img = new Image();  
        img.src = p;
        var height = img.height; //获取图片高度
        var width = img.width; //获取图片宽度
        var imgHtml = "<img src='" + p + "' />";  
        //弹出层
        layer.open({  
            type: 1,  
            maxmin:true,
            //shade: 0.8,
            //offset: 'auto',
            area: ['80%','80%'],
            shadeClose:true,//点击外围关闭弹窗
            scrollbar: false,//不现实滚动条
            title: "图片预览", //不显示标题  
            content: imgHtml, //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响  
            cancel: function () {  
                //layer.msg('捕获就是从页面已经存在的元素上，包裹layer的结构', { time: 5000, icon: 6 });  
            }  
        }); 
    }
	//Pdf预览
	function previewPdf(path) {
		//var p = "${pageContext.request.contextPath}/cfile/showPdf?path="+path;
		//p=p.replace("\\", "/");
        //var img = new Image();  
        //img.src = p;
        //var height = img.height; //获取图片高度
        //var width = img.width; //获取图片宽度
        //var imgHtml = "<img src='" + p + "' />";  
        
	     var url='${pageContext.request.contextPath}/cfile/showPdf?path='+path;   //转向网页的地址;    
         var openwin="PDF预览";                           //网页名称，可为空;    
         var iWidth=600;                          //弹出窗口的宽度;    
         var iHeight=400;                        //弹出窗口的高度;    
         var iTop = (window.screen.availHeight-30-iHeight)/2;   //获得窗口的垂直位置;    
         var iLeft = (window.screen.availWidth-10-iWidth)/2;           //获得窗口的水平位置;    
         window.open(url,name,'height='+iHeight+',,innerHeight='+iHeight+',width='+iWidth+',innerWidth='+iWidth+',top='+iTop+',left='+iLeft+',toolbar=yes,menubar=no,scrollbars=auto,resizeable=yes,location=no,status=no');   
    }
	//txt预览
	function previewTxt(path) {
	     var url='${pageContext.request.contextPath}/cfile/showTxt?path='+path;   //转向网页的地址;    
         var openwin="Txt预览";                           //网页名称，可为空;    
         var iWidth=600;                          //弹出窗口的宽度;    
         var iHeight=400;                        //弹出窗口的高度;    
         var iTop = (window.screen.availHeight-30-iHeight)/2;   //获得窗口的垂直位置;    
         var iLeft = (window.screen.availWidth-10-iWidth)/2;           //获得窗口的水平位置;    
         window.open(url,name,'height='+iHeight+',,innerHeight='+iHeight+',width='+iWidth+',innerWidth='+iWidth+',top='+iTop+',left='+iLeft+',toolbar=yes,menubar=no,scrollbars=auto,resizeable=yes,location=no,status=no');   
    }
	//预览音乐和视频
	function previewMusicAndVideo(path) {
		
		var p = "${pageContext.request.contextPath}/"+path;
		p=p.replace("\\", "/");
	     var url='${pageContext.request.contextPath}/back/toMusicAndVideo?filePath='+path;   //转向网页的地址;    
         var openwin="修改密码";                           //网页名称，可为空;    
         var iWidth=600;                          //弹出窗口的宽度;    
         var iHeight=400;                        //弹出窗口的高度;    
         var iTop = (window.screen.availHeight-30-iHeight)/2;   //获得窗口的垂直位置;    
         var iLeft = (window.screen.availWidth-10-iWidth)/2;           //获得窗口的水平位置;    
         window.open(url,name,'height='+iHeight+',,innerHeight='+iHeight+',width='+iWidth+',innerWidth='+iWidth+',top='+iTop+',left='+iLeft+',toolbar=yes,menubar=no,scrollbars=auto,resizeable=yes,location=no,status=no');   
	}
	//排序
	function order(){
		$.ajax({
            type : "post",
            url : "${pageContext.request.contextPath}/back/chgOrder",
            async : true,
            cache : false,
            dataType : 'json',
            success : function(data) {
            	if(data.success){
            		 window.location.reload();
            	}
            },
            error : function(data) {
              alert("error:" + data.responseText);
            }
        });
	}
	//设置显示session
	function setSession(type){
		 $.ajax({
	            type : "post",
	            url : "${pageContext.request.contextPath}/back/setShowType",
	            data:{'type':type},
	            async : true,
	            cache : false,
	            dataType : 'json',
	            success : function(data) {
	            	window.location.reload();
	            },
	            error : function(data) {
	              alert("error:" + data.responseText);
	            }
	     });
	}
	//显示大图标
	function showBig(){
		$('#small').hide();$('#detail').hide();$('#big').show();
		setSession("big");
	}
	//显示小图标
	function showSmall(){
		$('#big').hide();$('#detail').hide();$('#small').show();
		setSession("small");
	}
	//显示明细
	function showDetail(){
		$('#big').hide();$('#small').hide();$('#detail').show();
		setSession("detail");
	}
	
</script>

</head>
<body>
	<div id="btnDiv">
		 <button type="button" style="display:none;" class="layui-btn uploadHiddenBtn" id="test6"><i class="layui-icon"></i>上传音频</button> 
	</div>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/main/css/cover.css">
	<link rel="stylesheet" node-type="theme-link" type="text/css" href="${pageContext.request.contextPath}/static/main/css/diskSystem-theme.css">
	<div class="frame-all" id="layoutApp">
		<div class="skin-main"></div>
		<div class="L5TkEz" node-type="L5TkEz">
			<div class="5bKX3H3U" node-type="5bKX3H3U">
				<div class="2jApzH" node-type="2jApzH">
					<div class="cnkcOl" node-type="cnkcOl">
						<div class="frame-aside" id="layoutAside">
							<div class="sshW2ylf" node-type="sshW2ylf">
								<div class="wtI8JlIv" node-type="wtI8JlIv">
									<div node-type="kgv16Ka" class="module-aside DtJtsC">
										<div class="KHbQCub"></div>
										<ul class="fOHAbxb">
											<li node-type="yqvhr7Mj" data-key="list" class="tmabWe0">
<%--											bHzsaPb--%>
												<a node-type="fcfnrawx" path="/" class="voQevj cprXmj" hidefocus="true" href="javascript:void(0);"> 
												<span class="text"> 
												<span node-type="zbsa1EKd" class="fa fa-bars fa-lg"></span> 
												<span node-type="cxcrBV2">菜单</span> 
												</span> </a>
											</li>
											<c:if test="${user.type=='管理员'}">
											<li node-type="yqvhr7Mj" data-key="pic" class="tmabWe0">
												<a node-type="fcfnrawx" class="cprXmj" hidefocus="true" href="${pageContext.request.contextPath}/user/toUser" target="_blank"> 
													<span class="text"> 
														<span node-type="cxcrBV2" >用户管理</span> 
													</span> 
												</a>
											</li>
											<li node-type="yqvhr7Mj" data-key="pic" class="tmabWe0">
												<a node-type="fcfnrawx" class="cprXmj" hidefocus="true" href="${pageContext.request.contextPath}/cls/toCls" target="_blank"> 
													<span class="text"> 
														<span node-type="cxcrBV2" >分类管理</span> 
													</span> 
												</a>
											</li>
											<li node-type="yqvhr7Mj" data-key="other" class="tmabWe0">
												<a node-type="fcfnrawx" cat="6" class="cprXmj" hidefocus="true" href="${pageContext.request.contextPath}/trans/count" target="_blank"> 
													<span class="text"> 
														<span node-type="cxcrBV2">文件统计</span> 
													</span> 
												</a>
											</li>
											</c:if>
											<li node-type="yqvhr7Mj" data-key="list" class="tmabWe0">
												<a node-type="fcfnrawx" path="/" class="voQevj cprXmj" hidefocus="true" href="javascript:void(0);"> 
												<span class="text"> 
												<span node-type="zbsa1EKd" class="fa fa-bars fa-lg"></span> 
												<span node-type="cxcrBV2">分类</span> 
												</span> </a>
											</li>
											 <c:forEach items="${clsList}" var="cls" varStatus="status">
												<li node-type="yqvhr7Mj" data-key="pic" class="tmabWe0" style="margin-left:20px;">
													<a node-type="fcfnrawx" class="cprXmj" hidefocus="true" href="${pageContext.request.contextPath}/back/searchByClsId?clsCid=${cls.cid}&crtUser=${user.cid}"> 
														<span class="text"> 
															<span node-type="cxcrBV2" >${cls.nm}</span> 
														</span> 
													</a>
												</li>
											</c:forEach>
											
<%--											<li node-type="yqvhr7Mj" data-key="pic" class="tmabWe0">--%>
<%--												<a node-type="fcfnrawx" class="cprXmj" hidefocus="true" href="${pageContext.request.contextPath}/back/searchByType?type=1&crtUser=${user.cid}"> --%>
<%--													<span class="text"> --%>
<%--														<span node-type="cxcrBV2" >图片</span> --%>
<%--													</span> --%>
<%--												</a>--%>
<%--											</li>--%>
<%--											<li node-type="yqvhr7Mj" data-key="doc" class="tmabWe0">--%>
<%--												<a node-type="fcfnrawx" cat="4" class="cprXmj" hidefocus="true" href="${pageContext.request.contextPath}/back/searchByType?type=2&crtUser=${user.cid}"> --%>
<%--													<span class="text"> --%>
<%--														<span node-type="cxcrBV2">文档</span> --%>
<%--													</span> --%>
<%--												</a>--%>
<%--											</li>--%>
<%--											<li node-type="yqvhr7Mj" data-key="video" class="tmabWe0">--%>
<%--												<a node-type="fcfnrawx" cat="1" class="cprXmj" hidefocus="true" href="${pageContext.request.contextPath}/back/searchByType?type=3&crtUser=${user.cid}"> --%>
<%--												<span class="text"> --%>
<%--													<span node-type="cxcrBV2">视频</span> --%>
<%--												</span> --%>
<%--												</a>--%>
<%--											</li>--%>
<%--											<li node-type="yqvhr7Mj" data-key="music" class="tmabWe0 vgKGgu">--%>
<%--												 <a node-type="fcfnrawx" cat="2" class="cprXmj" hidefocus="true" href="${pageContext.request.contextPath}/back/searchByType?type=4&crtUser=${user.cid}"> --%>
<%--													 <span class="text"> --%>
<%--													 	<span node-type="cxcrBV2">音乐</span>--%>
<%--													 </span> --%>
<%--												 </a>--%>
<%--											 </li>--%>
<%--											<li node-type="yqvhr7Mj" data-key="other" class="tmabWe0">--%>
<%--												<a node-type="fcfnrawx" cat="6" class="cprXmj" hidefocus="true" href="${pageContext.request.contextPath}/back/searchByType?type=5&crtUser=${user.cid}"> --%>
<%--													<span class="text"> --%>
<%--														<span node-type="cxcrBV2">其它</span> --%>
<%--													</span> --%> 
<%--												</a>--%>
<%--											</li>--%>
											<li node-type="yqvhr7Mj" data-key="other" class="tmabWe0">
												<a node-type="fcfnrawx" cat="6" class="cprXmj" hidefocus="true" href="${pageContext.request.contextPath}/back/searchByType?delFlg=1&crtUser=${user.cid}"> 
													<span class="text"> 
														<span node-type="cxcrBV2">回收站</span> 
													</span> 
												</a>
											</li>
										
										</ul>
<%--										<div class="aside-absolute-container" style="visibility: visible; position: absolute; width: 100%; height: 150px;">--%>
<%--											<div data-index="0" node-type="layout-absolute-box" style="width: 100%; height: 50px; background:none; z-index: 2; "></div>--%>
<%--											<div data-index="1" node-type="layout-absolute-box" style="width: 100%; height: 50px; background:none; z-index: 3; ">--%>
<%--												<div node-type="eukRjQk" class="QGOvsxb" style="_visibility:visible;">--%>
<%--													<ul class="tDuODs">--%>
<%--														<li class="g-clearfix bar">--%>
<%--															<div node-type="vmoL4o" class="remainingSpaceUi">--%>
<%--																<span node-type="avuo7VR" class="remainingSpaceUi_span" style="background: rgb(252, 146, 89) none repeat scroll 0% 0%; transition-duration: 1.48025s; width: 98.6834%;"></span>--%>
<%--															</div>--%>
<%--															<div class="DIeHPCb remaining-space">--%>
<%--																<span node-type="esoNXk" class="bold">2036G</span>/<span node-type="tzefrzJr">2063G</span>--%>
<%--															</div>--%>
<%--														</li>--%>
<%--													</ul>--%>
<%--												</div>--%>
<%--											</div>--%>
<%--										</div>--%>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="YrzaXaV" node-type="YrzaXaV">
					<div class="LIOTbC" node-type="LIOTbC">
						<div class="frame-main" id="layoutMain" style="display: block;">
							<div class="0NzcTCn4" node-type="0NzcTCn4">
								<div class="6FvcigY" node-type="6FvcigY">
									<div class="0dyg35" node-type="0dyg35">
										<div class="DxdbeCb g-clearfix">
											<div class="hoxQDRZ">
												<div class="nkoe1lZ8 nsfQDDg">
													<ul>
														<li class="dropdown">
														<a style="display: none;" data-type="list" class="sjbrxdx" href="javascript:void(0)"> 
															<span class="icon icon-list">
															</span> 
														</a> 
														</li>
														<li class="dropdown">
															<a data-type="grid" class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0)"> 
																<span class="fa fa-th fa-lg"></span> 
															</a>
															<ul class="dropdown-menu" style="min-width:100px;">
																<li><a href="javascript:void(0);" onclick="showBig();" style="width:100%">大图标</a></li>
																<li><a href="javascript:void(0);" onclick="showSmall();" style="width:100%">小图标</a></li>
																<li><a href="javascript:void(0);" onclick="showDetail();" style="width:100%">详细信息</a></li>
															</ul>
														</li>
													</ul>
													
												</div>
												<div class="EzLavy">
													<div node-type="jsb2XL" class="hhxQe4j" onclick="order();">
														<span class="fa fa-sort-amount-asc fa-lg"></span>
													</div>
													<div node-type="sDwvAgb" class="sDwvAgb" style="display: none;">
														<span data-key="name" class="vAFAFF"> <em class="icon icon-sort-select"></em> 文件名</span> <span data-key="size" class="vAFAFF"> <em class="icon icon-sort-select"></em> 大小</span> <span data-key="time" class="vAFAFF ugcOHtb"> <em class="icon icon-sort-select"></em> 修改日期</span>
													</div>
												</div>
												<div class="OFaPaO">
													<div class="yukQqZp" node-type="mcgbRq4">
														<form node-type="zyybAMy" class="tur7p9 vgtrPwa" action="${pageContext.request.contextPath}/back/search" method="post">
															<input node-type="wlraQpM2 xbzr4ay" data-key="SEARCH_QUERY" autocomplete="off" class="bbgrbvde" name="key" spellchecking="off" type="text"> 
															<input type="hidden" value="${user.cid}" name="crtUser" />
															<span node-type="xlbGVy" class="osbyQyd2 icon icon-search-del" style="display: none;"></span> 
															<span node-type="xbzr4ay" data-key="SEARCH_BUTTON" class="gHHsaL"> 
																<span class="fa fa-search" onclick="document.getElementById('searchBtnId').click()"><input id="searchBtnId" type="submit" name="searchBtn" value="searchBtn" style="display:none"  /></span>
															</span> 
														</form>
													</div>
												</div>
												<div class="me1JKg" style="white-space: nowrap; position: relative;">
													<div class="button-box-mark" style="display:inline-block;*display:inline;*zoom:1;width:1px;height:1px;line-height:0;"></div>
													<div class="tcuLAu" style="position: absolute; top: 0px; line-height: normal; padding-top: 11px; padding-left: 0px; width: auto;">
														<div style="display:none;width:100%;height:100%;z-index:30;position:absolute;top:0;left:0;"></div>
														<span class="g-dropdown-button">
														 <a id="uploadFile1"  onclick="openUploadWin();" class="g-button g-button-blue blue-upload upload-wrapper" data-button-id="b31" data-button-index="1" href="javascript:;" title="上传">
														<span class="g-button-right">
														<em class="fa fa-upload" title="上传" style="color:white;" ></em>
														<span class="text " style="width: 40px;">上传</span> 
														</span>
																<form class="h5-uploader-form" action="javascript:void(0);">
<%--																	<input title="点击选择文件" class="fa fa-upload" id="fileUpId" multiple="" accept="*/*" type="file" name="html5uploader" style="position:absolute;opacity:0;top:0;left:0;width:100%;height:100%;cursor:pointer;">--%>
																</form> </a> <span class="menu" style="width: 84px;"><a data-menu-id="b-menu0" class="g-button-menu  upload-wrapper" href="javascript:;">上传文件
																	<form class="h5-uploader-form" action="javascript:void(0);">
																		<input title="点击选择文件" id="h5Input1" multiple="" accept="*/*" type="file" name="html5uploader" style="position:absolute;opacity:0;top:0;left:0;width:100%;height:100%;cursor:pointer;">
																	</form> </a><a data-menu-id="b-menu1" class="g-button-menu  upload-wrapper" href="javascript:;">上传文件夹
																	<form class="h5-uploader-form" action="javascript:void(0);">
																		<input title="点击选择文件夹" id="h5Input2" multiple="" webkitdirectory="" accept="*/*" type="file" name="html5uploader" style="position:absolute;opacity:0;top:0;left:0;width:100%;height:100%;cursor:pointer;">
																	</form> </a> </span> </span>
																	<a class="g-button" title="新建文件夹" onclick="crtDir();" data-button-id="b33" data-button-index="2" href="javascript:;" style="display: inline-block;">
																		<span class="g-button-right">
																			<em class="fa fa-file" title="新建文件夹"></em>
																			<span class="text" style="width: auto;">新建文件夹</span> 
																		</span> 
																	</a> 
																	<a class="g-button" title="重命名" onclick="rename();" data-button-id="b33" data-button-index="2" href="javascript:;" style="display: inline-block;">
																		<span class="g-button-right">
																			<em class="fa fa-pencil-square-o" title="重命名"></em>
																			<span class="text" style="width: auto;">重命名</span> 
																		</span> 
																	</a> 
																	<a class="g-button" title="下载" onclick="down();" data-button-id="b33" data-button-index="2" href="javascript:;" style="display: inline-block;">
																		<span class="g-button-right">
																			<em class="fa fa-arrow-down" title="下载"></em>
																			<span class="text" style="width: auto;">下载</span> 
																		</span> 
																	</a> 
																	<a class="g-button" title="删除" onclick="del();" data-button-id="b33" data-button-index="2" href="javascript:;" style="display: inline-block;">
																		<span class="g-button-right">
																			<em class="fa fa-close" title="删除"></em>
																			<span class="text" style="width: auto;">删除</span> 
																		</span> 
																	</a> 
																	<a class="g-button" title="彻底删除" onclick="delAll();" data-button-id="b33" data-button-index="2" href="javascript:;" style="display: inline-block;">
																		<span class="g-button-right">
																			<em class="fa fa-close" title="彻底删除"></em>
																			<span class="text" style="width: auto;">彻底删除</span> 
																		</span> 
																	</a> 
																	<a class="g-button" title="取回" onclick="recover();" data-button-id="b33" data-button-index="2" href="javascript:;" style="display: inline-block;">
																		<span class="g-button-right">
																			<em class="fa fa-reply" title="取回"></em>
																			<span class="text" style="width: auto;">取回</span> 
																		</span> 
																	</a> 
																	<a class="g-button" title="加密发送" onclick="send();" data-button-id="b33" data-button-index="2" href="javascript:;" style="display: inline-block;">
																		<span class="g-button-right">
																			<em class="fa fa-long-arrow-right" title="加密发送"></em>
																			<span class="text" style="width: auto;">加密发送</span> 
																		</span> 
																	</a> 
																	<a class="g-button" title="接收并解密" onclick="rec();" data-button-id="b33" data-button-index="2" href="javascript:;" style="display: inline-block;">
																		<span class="g-button-right">
																			<em class="fa fa-long-arrow-right" title="接收并解密"></em>
																			<span class="text" style="width: auto;">接收并解密</span> 
																		</span> 
																	</a> 
																	<span class="g-dropdown-button tools-more" style="display: none;"><a class="g-button" data-button-id="b39" data-button-index="" href="javascript:;" title="更多"><span class="g-button-right"><em class="icon icon-more" title="更多"></em><span class="text" style="width: auto;">更多</span> </span> </a> <span class="menu" style="width: 70px;"> <a style="display:none;" data-menu-id="b-menu0" class="g-button-menu g-menu-hasIcon" href="javascript:;"><em class="icon icon-upload"></em>上传</a> <a style="display:none;" data-menu-id="b-menu1" class="g-button-menu g-menu-hasIcon" href="javascript:;"><em
																	class="icon icon-newfolder"></em>新建文件夹</a> </span> </span>
													</div>
													<div class="button-box-mark" style="display:inline-block;*display:inline;*zoom:1;width:1px;height:1px;line-height:0;"></div>
												</div>
											</div>
											<div class="fwcvbAAD"></div>
											<div class="pbr7qj"></div>
											<div node-type="EQBfLM" class="EQBfLM g-clearfix">
												<a href="https://pan.baidu.com/disk/timeline" class="AbMfvg">时光轴</a> <a href="javascript:void(0);" class="AbMfvg selected">最近上传</a>
											</div>
										</div>
										<div class="1qsW1lYI" node-type="1qsW1lYI">
											<!--[if IE]><iframe id="historyIFrameEmulator" style="display: none"></iframe><![endif]-->
											<div node-type="KPDwCE" class="KPDwCE" style="height: 723px;">
												<div node-type="JDeHdxb" class="JDeHdxb">
													<span class="EgMMec"><a href="${pageContext.request.contextPath}/back/home">全部</a></span>
													<c:forEach items="${ppList}" var="pp" varStatus="status">
														<span class="EgMMec">/<a href="${pageContext.request.contextPath}/back/home?cid=${pp.cid}">${pp.name}</a></span>
													</c:forEach>
<%--													<span class="EgMMec"><a href="www.baidu.com">11</a>/</span>--%>
<%--													<span class="EgMMec"><a href="www.baidu.com">11</a>/</span>--%>
<%--													<span class="EgMMec"><a href="www.baidu.com">11</a>/</span>--%>
<%--													<span class="EgMMec"><a href="www.baidu.com">11</a>/</span>--%>
<%--													<span class="EgMMec"><a href="www.baidu.com">全部</a>/</span>--%>
													<c:if test="${!empty nowDirName}">
														<span class="EgMMec">/${nowDirName}</span>
													</c:if>
													<span class="FcucHsb">已加载${dataGrid.count}个</span>
													<ul class="FuIxtL" node-type="FuIxtL" style="display: black;">
														<li><a data-deep="-1" href="javascript:;">返回上一级</a><span class="EKIHPEb">|</span>
														</li>
														<li node-type="tbAudfb"></li>
													</ul>
												</div>
												<%--明细--%>
												<div id="detail">
													<div class="QxJxtg">
														<div class="xGLMIab">
															<ul class="QAfdwP tvPMvPb" node-type="tvPMvPb">
																<li data-key="name" class="fufHyA yfHIsP" style="width:50%;"><div node-type="fydGNC" class="Qxyfvg fydGNC">
																		<span class="zbyDdwb"><input type="checkbox" name="all" id="checkboxAll" /></span>
																		<span class="MIMvNNb">全选</span>
																		<span class="icon NbKJexb icon-checksmall">
																		</span>
																	</div> <span class="text">文件名</span><span class="xEuDywb"></span><span class="icon aHEytd icon-up"></span><span class="icon sFxCFbb icon-downtitle"></span>
																</li>
																<li data-key="size" class="fufHyA" style="width:16%;"><span class="text">大小</span><span class="xEuDywb"></span><span class="icon aHEytd icon-up"></span><span class="icon sFxCFbb icon-downtitle"></span>
																</li>
																<li data-key="type" class="fufHyA" style="width:10%;"><span class="text">类型</span><span class="xEuDywb"></span><span class="icon aHEytd icon-up"></span><span class="icon sFxCFbb icon-downtitle"></span>
																</li>
																<li data-key="time" class="fufHyA gObdAzb MCGAxG" style="width:23%;"><span class="text">修改日期</span><span class="xEuDywb"></span><span class="icon aHEytd icon-up"></span><span class="icon sFxCFbb icon-downtitle"></span>
																</li>
															</ul>
															<ul class="vwCPvP tvPMvPb" node-type="tvPMvPb" style="display: none;">
																<li class="fufHyA yfHIsP"><div node-type="fydGNC" class="Qxyfvg fydGNC">
																		<span class="zbyDdwb"></span>
																		<span class="MIMvNNb">全选</span>
																		<span class="icon NbKJexb icon-checksmall"></span>
																	</div>
																</li>
															</ul>
															<div class="FcQMwt global-clearfix">
																<span class="MdLxwM"></span>
																<div class="KKtwaH"></div>
															</div>
														</div>
													</div>
													<div class="zJMtAEb" style="">
														<div node-type="NHcGw" class="NHcGw" style="overflow-y: auto; height: 665px;">
															<div class="vdAfKMb" style="height: auto;">
																<c:forEach items="${dataGrid.data}" var="cf" varStatus="status">
																	<dd class="g-clearfix AuPKyz open-enable" _position="2" _cmd_installed="1" _installed="1">
																		<span node-type="EOGexf" class="EOGexf">
																			<input type="checkbox" value="${cf.cid}" name="${cf.type}" filename="${cf.name}" />
																		</span>
																		<c:if test="${'1'==cf.type}">
																			<div class="wlob1Ea1" style="background:url('${pageContext.request.contextPath}/static/main/img/pic.jpg');"></div>
																		</c:if>
																		<c:if test="${'2'==cf.type}">
																			<div class="wlob1Ea1" style="background:url('${pageContext.request.contextPath}/static/main/img/w.png');"></div>
																		</c:if>
																		<c:if test="${'3'==cf.type}">
																			<div class="wlob1Ea1" style="background:url('${pageContext.request.contextPath}/static/main/img/video.png');"></div>
																		</c:if>
																		<c:if test="${'4'==cf.type}">
																			<div class="wlob1Ea1" style="background:url('${pageContext.request.contextPath}/static/main/img/music.png');"></div>
																		</c:if>
																		<c:if test="${'5'==cf.type}">
																			<div class="wlob1Ea1" style="background:url('${pageContext.request.contextPath}/static/main/img/other.png');"></div>
																		</c:if>
																		<c:if test="${'6'==cf.type}">
																			<div class="wlob1Ea1" style="background:url('${pageContext.request.contextPath}/static/main/img/pdf.jpg');"></div>
																		</c:if>
																		<c:if test="${'7'==cf.type}">
																			<div class="wlob1Ea1" style="background:url('${pageContext.request.contextPath}/static/main/img/txt.jpg');"></div>
																		</c:if>
																		<%--文件夹--%>
																		<c:if test="${'0'==cf.type}">
																			<div class="wlob1Ea1" style="background:url('${pageContext.request.contextPath}/static/main/img/dir.jpg');"></div>
																		</c:if>
																		
																		<div class="file-name" style="width:50%">
																			<div class="text">
																				<%--文件夹--%>
																				<c:if test="${'0'==cf.type}">
																					<a href="${pageContext.request.contextPath}/back/home?cid=${cf.cid}"  class="io1g5G" title="${cf.name}">${cf.name}</a>	
																				</c:if>		
																				<%--非文件夹--%>
																				<c:if test="${'0'!=cf.type}">	
																				    <%--图片--%>
																					<c:if test="${'1'==cf.type}">
																						<a href="javascript:void(0);" onclick="previewImg('${cf.filePath}');" class="io1g5G" title="${cf.name}">${cf.name}</a>																	
																					</c:if>	
																					<%--非图片--%>	
																					<c:if test="${'1'!=cf.type}">
																						<%--视频，音乐--%>	
																						<c:if test="${'3'==cf.type or '4'==cf.type}">
																							<a href="javascript:void(0);" onclick="previewMusicAndVideo('${cf.filePath}');" class="io1g5G" title="${cf.name}">${cf.name}</a>																	
																						</c:if>	
																						<%--非视频，音乐--%>	
																						<c:if test="${'3'!=cf.type and '4'!=cf.type}">
																							<c:if test="${'6'==cf.type}">
																								<a href="javascript:void(0);" onclick="previewPdf('${cf.filePath}');" class="io1g5G" title="${cf.name}">${cf.name}</a>				
																							</c:if>
																							<c:if test="${'6'!=cf.type}">
																								<c:if test="${'7'==cf.type}">
																									<a href="javascript:void(0);" onclick="previewTxt('${cf.filePath}');" class="io1g5G" title="${cf.name}">${cf.name}</a>
																								</c:if>
																								<c:if test="${'7'!=cf.type}">
																									<c:if test="${'8'==cf.type}">
																									<a href="javascript:void(0);" onclick="previewTxt('${cf.filePath}');" class="io1g5G" title="${cf.name}">${cf.name}</a>
																									</c:if>
																									<a href="javascript:POBrowser.openWindow('${pageContext.request.contextPath}/back/toWordDetail?filePath=${cf.filePath}&officeType=${cf.officeType}','width=1200px;height=800px;');" class="io1g5G" title="${cf.name}">${cf.name}</a>	
																								</c:if>
																							</c:if>
																						</c:if>	
																					</c:if>												
																				</c:if>
																			</div>
																			
																		</div>
																		<div class="sobWnA" style="width:16%">${cf.sizeString}</div>
																		<div class="sobWnA" style="width:10%">${cf.cls.nm}</div>
																		<div class="agrcrXek" style="width:23%">${cf.crtDateString}</div>
																		<div class="of1EAa" style="width:0%">
																			<span class="owrB3v" node-type="ropb2Xk"></span>
																		</div>
																	</dd>
																</c:forEach>
															</div>
														</div>
													</div>
												</div>
												<%--明细--%>
												
												<%--大图标--%>
												<div id="big" style="padding:5px;display:none">
													<div class="row">
													  <c:forEach items="${dataGrid.data}" var="cf" varStatus="status">
														  <div class="col-sm-6 col-md-2">
																	<div class="thumbnail" style="height:115px;overflow: hidden;">
																		<span class="badge" style="background:none;"><input type="checkbox" value="${cf.cid}"name="${cf.type}"  filename="${cf.name}" /></span>
																		
																			<c:if test="${'1'==cf.type}">
																				<img src="${pageContext.request.contextPath}/static/main/img/pic.jpg" alt="...">
																			</c:if>
																			<c:if test="${'2'==cf.type}">
																				<img src="${pageContext.request.contextPath}/static/main/img/w.png" alt="...">
																			</c:if>
																			<c:if test="${'3'==cf.type}">
																				<img src="${pageContext.request.contextPath}/static/main/img/video.png" alt="...">
																			</c:if>
																			<c:if test="${'4'==cf.type}">
																				<img src="${pageContext.request.contextPath}/static/main/img/music.png" alt="...">
																			</c:if>
																			<c:if test="${'5'==cf.type}">
																				<img src="${pageContext.request.contextPath}/static/main/img/other.png" alt="...">
																			</c:if>
																			<c:if test="${'0'==cf.type}">
																				<img src="${pageContext.request.contextPath}/static/main/img/dir.jpg" alt="...">
																			</c:if>
																			
																			<%--文件夹--%>
																			<c:if test="${'0'==cf.type}">
																				<a href="${pageContext.request.contextPath}/back/home?cid=${cf.cid}"  title="${cf.name}">
																			</c:if>		
																			<%--非文件夹--%>
																			<c:if test="${'0'!=cf.type}">	
																			    <%--图片--%>
																				<c:if test="${'1'==cf.type}">
																					<a href="javascript:void(0);" onclick="previewImg('${cf.filePath}');" title="${cf.name}">														
																				</c:if>	
																				<%--非图片--%>	
																				<c:if test="${'1'!=cf.type}">
																					<%--视频，音乐--%>	
																					<c:if test="${'3'==cf.type or '4'==cf.type}">
																						<a href="javascript:void(0);" onclick="previewMusicAndVideo('${cf.filePath}');" title="${cf.name}">																
																					</c:if>	
																					<%--非视频，音乐--%>	
																					<c:if test="${'3'!=cf.type and '4'!=cf.type}">
																						<c:if test="${'6'==cf.type}">
																							<a href="javascript:void(0);" onclick="previewPdf('${cf.filePath}');" class="io1g5G" title="${cf.name}">${cf.name}</a>				
																						</c:if>
																						<c:if test="${'6'!=cf.type}">
																							<c:if test="${'7'==cf.type}">
																								<a href="javascript:void(0);" onclick="previewTxt('${cf.filePath}');" class="io1g5G" title="${cf.name}">${cf.name}</a>
																							</c:if>
																							<c:if test="${'7'!=cf.type}">
																								<a href="javascript:POBrowser.openWindow('${pageContext.request.contextPath}/back/toWordDetail?filePath=${cf.filePath}&officeType=${cf.officeType}','width=1200px;height=800px;');" class="io1g5G" title="${cf.name}">${cf.name}</a>	
																							</c:if>
																						</c:if>
																					</c:if>	
																				</c:if>												
																			</c:if>
																			
																			<div class="caption" style="text-align: center">
		<%--																	<h3>Thumbnail label</h3>--%>
																				<p style="word-break:break-all;display:-webkit-box;-webkit-line-clamp:2;-webkit-box-orient:vertical;overflow:hidden;">${cf.name}</p>
		<%--																		<p>--%>
		<%--																			<a href="#" class="btn btn-primary" role="button">Button</a> --%>
		<%--																			<a href="#" class="btn btn-default" role="button">Button</a>--%>
		<%--																		</p>--%>
																			</div>
																			
																		</a>
																		
																	</div>
															  
															</div>
													  </c:forEach>
													</div>
												</div>
												<%--大图标--%>
												
												<%--小图标--%>
												<div id="small" style="margin-left:10px;padding:5px;display:none">
													<div class="row">
													  <c:forEach items="${dataGrid.data}" var="cf" varStatus="status">
														  <div class="col-sm-2 col-md-1" style="padding-left:3px;padding-right:3px">
																	<div class="thumbnail" style="height:115px;overflow: hidden;">
																		<span class="badge" style="background:none;"><input type="checkbox" value="${cf.cid}" name="${cf.type}" filename="${cf.name}" /></span>
																			<c:if test="${'1'==cf.type}">
																				<img src="${pageContext.request.contextPath}/static/main/img/pic.jpg" alt="...">
																			</c:if>
																			<c:if test="${'2'==cf.type}">
																				<img src="${pageContext.request.contextPath}/static/main/img/w.png" alt="...">
																			</c:if>
																			<c:if test="${'3'==cf.type}">
																				<img src="${pageContext.request.contextPath}/static/main/img/video.png" alt="...">
																			</c:if>
																			<c:if test="${'4'==cf.type}">
																				<img src="${pageContext.request.contextPath}/static/main/img/music.png" alt="...">
																			</c:if>
																			<c:if test="${'5'==cf.type}">
																				<img src="${pageContext.request.contextPath}/static/main/img/other.png" alt="...">
																			</c:if>
																			<c:if test="${'0'==cf.type}">
																				<img src="${pageContext.request.contextPath}/static/main/img/dir.jpg" alt="...">
																			</c:if>
																			
																			<%--文件夹--%>
																			<c:if test="${'0'==cf.type}">
																				<a href="${pageContext.request.contextPath}/back/home?cid=${cf.cid}"  title="${cf.name}">
																			</c:if>		
																			<%--非文件夹--%>
																			<c:if test="${'0'!=cf.type}">	
																			    <%--图片--%>
																				<c:if test="${'1'==cf.type}">
																					<a href="javascript:void(0);" onclick="previewImg('${cf.filePath}');" title="${cf.name}">														
																				</c:if>	
																				<%--非图片--%>	
																				<c:if test="${'1'!=cf.type}">
																					<%--视频，音乐--%>	
																					<c:if test="${'3'==cf.type or '4'==cf.type}">
																						<a href="javascript:void(0);" onclick="previewMusicAndVideo('${cf.filePath}');" title="${cf.name}">																
																					</c:if>	
																					<%--非视频，音乐--%>	
																					<c:if test="${'3'!=cf.type and '4'!=cf.type}">
																						<c:if test="${'6'==cf.type}">
																							<a href="javascript:void(0);" onclick="previewPdf('${cf.filePath}');" class="io1g5G" title="${cf.name}">${cf.name}</a>				
																						</c:if>
																						<c:if test="${'6'!=cf.type}">
																							<c:if test="${'7'==cf.type}">
																								<a href="javascript:void(0);" onclick="previewTxt('${cf.filePath}');" class="io1g5G" title="${cf.name}">${cf.name}</a>
																							</c:if>
																							<c:if test="${'7'!=cf.type}">
																								<a href="javascript:POBrowser.openWindow('${pageContext.request.contextPath}/back/toWordDetail?filePath=${cf.filePath}&officeType=${cf.officeType}','width=1200px;height=800px;');" class="io1g5G" title="${cf.name}">${cf.name}</a>	
																							</c:if>
																						</c:if>
																					</c:if>	
																				</c:if>												
																			</c:if>
																			
																			<div class="caption" style="text-align: center">
		<%--																	<h3>Thumbnail label</h3>--%>
																				<p style="word-break:break-all;display:-webkit-box;-webkit-line-clamp:2;-webkit-box-orient:vertical;overflow:hidden;">${cf.name}</p>
		<%--																		<p>--%>
		<%--																			<a href="#" class="btn btn-primary" role="button">Button</a> --%>
		<%--																			<a href="#" class="btn btn-default" role="button">Button</a>--%>
		<%--																		</p>--%>
																			</div>
																			
																		</a>
																		
																	</div>
															  
															</div>
													  </c:forEach>
													</div>
												</div>
												<%--小图标--%>
												
												
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="rrJYgDR4" node-type="rrJYgDR4">
					<div class="PVCxqaE" node-type="PVCxqaE">
						<div class="frame-main" id="layoutServiceHolder" style="display: none;"></div>
					</div>
				</div>
			</div>
		</div>
		<div id="layoutHeader">
			<div node-type="module" class="module-header">
				<div node-type="module-header-wrapper" class="module-header-wrapper" style="height: 62px;">
					<dl class="xtJbHcb">
						<dt class="EHazOI">
							<a href="https://pan.baidu.com/" target="_self" title=""></a>
						</dt>
<%--						<dd class="vyQHNyb" node-type="header-link">--%>
<%--							<span class="cMEMEF wGMtMgb" node-type="disk-home"><a href="https://pan.baidu.com/disk/home" target="_self" title="网盘" node-type="item-title">网盘</a><span class="gICyHO"></span> </span>--%>
<%--						</dd>--%>
						<dd class="CDaavKb" node-type="header-apps">
<%--							<a href="https://pan.baidu.com/pcloud/msg" target="_blank" class="fa fa-envelope-open-o" title="系统通知"> --%>
<%--								<span class="badge">4</span>--%>
<%--							</a>--%>
<%--							<i class="Ebdyswb"></i> --%>
							<span class="DIcOFyb" node-type="app-user-info">
								<span class="user-photo-box"> 
									<i class="user-photo" style="background-image:url(${pageContext.request.contextPath}/${sessionInfo.icon});filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='${pageContext.request.contextPath}/${sessionInfo.icon}', sizingMethod='scale');"></i> 
								</span>
								<span class="user-name">${sessionInfo.realName}</span>
								<span class="animate-start animate-level-0">
									<i class="star start-left"></i>
									<i class="bar-bling"></i>
									<i class="bar-bling2"></i>
									<i class="star start-right"></i> 
								</span>
								<i node-type="img-ico" class="fa fa-chevron-down"></i>
								<dl class="PvsOgyb" node-type="app-user-box">
									<dt class="OMDFeH level-0">
										<i class="desc-arrow"></i><i class="desc-layer"></i><span class="desc-header">
										<span class="user-photo-box">
											<i class="user-photo" style="background-image:url(${pageContext.request.contextPath}/${sessionInfo.icon});filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='${pageContext.request.contextPath}/${sessionInfo.icon}', sizingMethod='scale');">
											</i> 
										</span>
										<span class="user-info" title="gdszm"><span class="username">${sessionInfo.realName}</span>
											<a class="JS-user-level level-0" node-type="app-user-level" href="/buy/center?tag=8&amp;from=hicon" target="_blank"></a> 
										</span> </span>
									</dt>
									<dd class="desc-box">
										<ul class="QgxQAN">
											<li class="cMEMEF"><a href="javascript:void(0);"  node-type="user-info-btn" onclick="openUserInfoWin();" >个人资料</a></li>
											<li class="cMEMEF"><a href="${pageContext.request.contextPath}/logout" node-type="login-out">退出</a></li>
										</ul>
									</dd>
								</dl> </span>
						</dd>
					</dl>
				</div>
			</div>
			
			
			<%--个人资料--%>
            <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" style="top:0;">
			  <div class="modal-dialog" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title" id="exampleModalLabel">个人信息修改</h4>
			      </div>
			      <div class="modal-body">
			      	<iframe id="if">
			      	
			      	</iframe>
<%--			        <form>--%>
<%--			          <div class="form-group">--%>
<%--			            <label for="recipient-name" class="control-label">Recipient:</label>--%>
<%--			            <input type="text" class="form-control" id="recipient-name">--%>
<%--			          </div>--%>
<%--			          <div class="form-group">--%>
<%--			            <label for="message-text" class="control-label">Message:</label>--%>
<%--			            <textarea class="form-control" id="message-text"></textarea>--%>
<%--			          </div>--%>
<%--			        </form>--%>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-primary">确定</button>
			        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			      </div>
			    </div>
			  </div>
			</div>
			<%--个人资料--%>
			<%--上传--%>
            <div class="modal fade" id="uploadModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" style="top:0;">
			  <div class="modal-dialog" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title" id="exampleModalLabel">个人信息修改</h4>
			      </div>
			      <div class="modal-body">
			      	<iframe id="if">
			      	
			      	</iframe>
<%--			        <form>--%>
<%--			          <div class="form-group">--%>
<%--			            <label for="recipient-name" class="control-label">Recipient:</label>--%>
<%--			            <input type="text" class="form-control" id="recipient-name">--%>
<%--			          </div>--%>
<%--			          <div class="form-group">--%>
<%--			            <label for="message-text" class="control-label">Message:</label>--%>
<%--			            <textarea class="form-control" id="message-text"></textarea>--%>
<%--			          </div>--%>
<%--			        </form>--%>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-primary">确定</button>
			        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			      </div>
			    </div>
			  </div>
			</div>
			<%--个人资料--%>
		</div>
	</div>
	
	<script>
    		function openUserInfoWin() {
    			var window = $("#exampleModal").modal({
    				//加载远程页面
    				remote: "${pageContext.request.contextPath}/back/toEditMyInfo",
    	            //点击背景不关闭
    	            backdrop:"static",
    	            //触发键盘esc事件时不关闭
    	            keyboard: false
    	        });
    			//模态框弹出前 修改模态框的标题及内容
    		    $('#exampleModal').on('show.bs.modal', function (event) {
    		    	//$("#if").load("${pageContext.request.contextPath}/back/myInfoEdit");
		    		
    		      // alert("模态框弹出前做些操作");
    		       
    		    });
    			 //模态框 关闭前事件
    		    $('#exampleModal').on('hide.bs.modal', function () {
    		    	
		    		
    		      //  alert("模态框 关闭前做些操作");
    		    });
    			
    			//每次隐藏时，清除数据。确保点击时，重新加载
    		    $("#exampleModal").on("hidden.bs.modal", function() {
    		        $(this).removeData("bs.modal");
    		    });
    			
    			
    		    $("#okBtn").click(function(){
    		    	alert(1);
 	  			});
    		}
    		function openUploadWin() {
    			var window = $("#uploadModal").modal({
    				//加载远程页面
    				remote: "${pageContext.request.contextPath}/back/toUploadWin",
    	            //点击背景不关闭
    	            backdrop:"static",
    	            //触发键盘esc事件时不关闭
    	            keyboard: false
    	        });
    			//模态框弹出前 修改模态框的标题及内容
    		    $('#uploadModal').on('show.bs.modal', function (event) {
    		    	//$("#if").load("${pageContext.request.contextPath}/back/myInfoEdit");
		    		
    		      // alert("模态框弹出前做些操作");
    		       
    		    });
    			 //模态框 关闭前事件
    		    $('#uploadModal').on('hide.bs.modal', function () {
    		    	
		    		
    		      //  alert("模态框 关闭前做些操作");
    		    });
    			
    			//每次隐藏时，清除数据。确保点击时，重新加载
    		    $("#uploadModal").on("hidden.bs.modal", function() {
    		        $(this).removeData("bs.modal");
    		    });
    			
    			
    		    //$("#okBtn").click(function(){
    		    //	alert(1);
 	  			//});
    		}
    	
	</script>
</body>
</html>

