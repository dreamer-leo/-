 <%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>用户管理</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugins/layuiadmin/layui/css/layui.css" media="all">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugins/layuiadmin/style/admin.css" media="all">
</head>
<body>

  <div class="layui-fluid">
    <div class="layui-card">
      <div class="layui-form layui-card-header layuiadmin-card-header-auto">
        <div class="layui-form-item">
          <div class="layui-inline">
            <label class="layui-form-label">用户名</label>
            <div class="layui-input-block">
              <input type="text" name="cname" placeholder="请输入" autocomplete="off" class="layui-input">
            </div>
          </div>
          <div class="layui-inline">
            <label class="layui-form-label">真实姓名</label>
            <div class="layui-input-block">
              <input type="text" name="crealname" placeholder="请输入" autocomplete="off" class="layui-input">
            </div>
          </div>
          <div class="layui-inline">
            <label class="layui-form-label">类型</label>
            <div class="layui-input-block">
              <select name="type" lay-search>
                <option value="">不限</option>
                <option value="会员">会员</option>
                <option value="管理员">管理员</option>
              </select>
            </div>
          </div>
          <div class="layui-inline">
            <button class="layui-btn layuiadmin-btn-useradmin" lay-submit lay-filter="LAY-user-front-search">
              <i class="layui-icon layui-icon-search layuiadmin-button-btn"></i>搜索
            </button>
          </div>
        </div>
      </div>
      
      <div class="layui-card-body">
        <div style="padding-bottom: 10px;">
          <button class="layui-btn layuiadmin-btn-useradmin" data-type="batchdel">删除</button>
          <button class="layui-btn layuiadmin-btn-useradmin" data-type="add">添加</button>
        </div>
        
        <table id="dg" lay-filter="dg"></table>
        <script type="text/html" id="imgTpl"> 
          <img style="display: inline-block; width: 50%; height: 100%;" src= {{ d.avatar }}>
        </script> 
        <script type="text/html" id="line-btn">
          <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="edit"><i class="layui-icon layui-icon-edit"></i>编辑</a>
         <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del"><i class="layui-icon layui-icon-delete"></i>删除</a>
        </script>
      </div>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/static/jq/jquery-1.8.2.min.js"></script>  
  <script src="${pageContext.request.contextPath}/static/plugins/layuiadmin/layui/layui.js"></script>  
  <script>
  
  layui.use(['table', 'form'], function(){
	  var form = layui.form;
	  var table = layui.table;
	  //实例
	  table.render({
	    elem: '#dg'
	    ,url: '${pageContext.request.contextPath}/user/userList' //数据接口
	    ,cols: [[ //表头
	      {type: 'checkbox', fixed: 'left'}
	      ,{title: '序号', width:80, type:'numbers',fixed: 'left'}
	      ,{field: 'cid', title: 'ID', width:80, sort: true, fixed: 'left',hide:true}
	      ,{field: 'cname', title: '用户名 ', width:100,fixed: 'left'}
	      ,{field: 'crealname', title: '真实姓名', width:120, sort: true,fixed: 'left'}
	      ,{field: 'studentId', title: '学号', width:80}
	      ,{field: 'dept', title: '院系', width:200}
	      ,{field: 'major', title: '专业', width:200}
	      ,{field: 'email', title: '电子邮箱', width: 250}
	      ,{field: 'type', title: '角色名称', width: 150}
	      ,{title: '操作', width: 180, align:'center', fixed: 'right', toolbar: '#line-btn'}
	    ]]
	    //,skin:'nob'//line （行边框风格）row （列边框风格）  nob （无边框风格）
	    ,even:true //若不开启隔行背景，不设置该参数即可
	    ,page: true //开启分页
	    ,limit: 10
	    ,height: 'full-220'
	    ,text: {none: '一条数据也没有^_^'}
	  });

	  //监听搜索
	  form.on('submit(LAY-user-front-search)', function(data) {
		var field = data.field;
		
		//执行重载
		table.reload('dg', {
			method:'POST',
			where : field
		});
      });
	  
	  //事件
	  var active = {
		  //删除
	      batchdel: function(){
	        var checkStatus = table.checkStatus('dg')
	        ,checkData = checkStatus.data; //得到选中的数据
			//console.info(checkData);
	        if(checkData.length === 0){
	          return layer.msg('请选择数据');
	        }
	        var ids = "";
	    	for(var i=0;i<checkStatus.data.length;i++){
	    		ids += checkStatus.data[i].cid+",";
	    	}
	        //layer.prompt({
	        //  formType: 1
	        //  ,title: '敏感操作，请验证口令'
	        //}, function(value, index){
	        //  layer.close(index);
	          
	         layer.confirm('确定删除吗？', function(index) {
	            
	         	 $.ajax({
			            type : "post",
			            url : "${pageContext.request.contextPath}/user/del",
			            data:{'ids':ids},
			            async : true,
			            cache : false,
			            dataType : 'json',
			            success : function(data) {
			                table.reload('dg'); //数据刷新
				            layer.close(index); //关闭弹层
				            layer.msg(data.msg);
			            },
			            error : function(data) {
			              alert("error:" + data.responseText);
			            }
			            
			    });

	            table.reload('dg');
	            layer.msg('已删除');
	          });
	        // });
	      }
		 //添加
	      ,add: function(){
	        layer.open({
	          type: 2
	          ,title: '添加用户'
	          ,content: '${pageContext.request.contextPath}/user/userform'
	          ,maxmin: true
	          ,area: ['700px', '650px']
	          ,btn: ['确定', '取消']
	          ,yes: function(index, layero){
	            var iframeWindow = window['layui-layer-iframe'+ index]
	            ,submitID = 'LAY-user-front-submit'
	            ,submit = layero.find('iframe').contents().find('#'+ submitID);

	            //监听提交
	            iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
	              var field = data.field; //获取提交的字段
	              console.info(field);
	              //提交 Ajax 成功后，静态更新表格中的数据
	              //$.ajax({});
	              $.ajax({
		            type : "post",
		            url : "${pageContext.request.contextPath}/user/add",
		            data:data.field,
		            async : true,
		            cache : false,
		            dataType : 'json',
		            success : function(data) {
		                table.reload('dg'); //数据刷新
			            layer.close(index); //关闭弹层
			            layer.msg(data.msg);
		            },
		            error : function(data) {
		              alert("error:" + data.responseText);
		            }
		            
		         });
	            
	            });
	            submit.trigger('click');
	          }
	        }); 
	      }
		 
		 
		};
		//事件
	  
		//处理事件
	    $('.layui-btn.layuiadmin-btn-useradmin').on('click', function(){
	        var type = $(this).data('type');
	        active[type] ? active[type].call(this) : '';
	    });
	
	
	    //监听行工具条
	    table.on('tool(dg)', function(obj){
	      var data = obj.data;
	      if(obj.event === 'del'){
			  layer.confirm('真的删除行么', function(index){
	
	            $.ajax({
		            type : "post",
		            url : "${pageContext.request.contextPath}/user/del",
		            data:{'ids':data.cid},
		            async : true,
		            cache : false,
		            dataType : 'json',
		            success : function(data) {
		            	layer.close(index); //关闭弹层
		                table.reload('dg'); //数据刷新
			            layer.msg(data.msg);
		            },
		            error : function(data) {
		              alert("error:" + data.responseText);
		            }
		            
		    	});
	            
	          });
	      } else if(obj.event === 'edit'){
	        var tr = $(obj.tr);
	        layer.open({
	          type: 2
	          ,title: '编辑用户'
	          ,content: '${pageContext.request.contextPath}/user/userform?cid='+data.cid
	          ,maxmin: true
	          ,area: ['500px', '450px']
	          ,btn: ['确定', '取消']
	          ,yes: function(index, layero){
	            var iframeWindow = window['layui-layer-iframe'+ index]
	            ,submitID = 'LAY-user-front-submit'
	            ,submit = layero.find('iframe').contents().find('#'+ submitID);
	           
	            //监听提交
	            iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
	            	
	              //刷新弹出框的表单，保证动态设置的值生效
	              iframeWindow.layui.form.render();
	              
	              var field = data.field; //获取提交的字段
	              //提交 Ajax 成功后，静态更新表格中的数据
	               $.ajax({
			            type : "post",
			            url : "${pageContext.request.contextPath}/user/edit",
			            data:data.field,
			            async : true,
			            cache : false,
			            dataType : 'json',
			            success : function(data) {
			                table.reload('dg'); //数据刷新
				            layer.close(index); //关闭弹层
				            layer.msg(data.msg);
			            },
			            error : function(data) {
			              alert("error:" + data.responseText);
			            }
			            
			         });
	            });  
	            
	            submit.trigger('click');
	          }
	          ,success: function(layero, index){
	            
	          }
	        });
	      }
	      
	      
	    });
	  	//监听行工具条
	  
	});
		</script>
</body>
</html>
