<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugins/date-time/datepicker.css" />
<link href="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrapvalidator-master/css/bootstrapValidator.min.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrapfileinput/css/fileinput.min.css" rel="stylesheet" />

<script src="${pageContext.request.contextPath}/static/plugins/date-time/bootstrap-datepicker.js"></script>
<script src="${pageContext.request.contextPath}/static/plugins/date-time/locales/bootstrap-datepicker.zh-CN.js"></script>
<script src="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrapvalidator-master/js/bootstrapValidator.min.js"></script>
<script src="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrapvalidator-master/js/zh_CN.js"></script>
<script src="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrapfileinput/js/fileinput.min.js"></script>
<script src="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrapfileinput/js/fileinput_locale_zh.js"></script>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title" id="exampleModalLabel">文件上传</h4>
</div>
<form id="myForm" method="POST" class="form-horizontal" action="${pageContext.request.contextPath}/user/myInfoEdit">
<input type="hidden" name="cid" value="${user.cid}"/>
	<div class="modal-body">
		<div class="row">
			<section>
				<div class="col-lg-12">
					<div class="layui-form-item">
				      <label class="layui-form-label" style="width:100px">文档类型</label>
				      <div class="layui-input-inline" style="width: 80%;">
				       	  <select id="clsCid" name="clsCid" lay-search>
				              <c:forEach items="${clsList}" var="cls" varStatus="status">
								 <option value="${cls.cid}">${cls.nm}</option>
							  </c:forEach>
						   </select>
				      </div>
				    </div>
				</div>
				<div class="col-lg-12">
					<div class="layui-upload">
						<button type="button" class="layui-btn layui-btn-normal" id="testList">选择多文件</button>
						<div class="layui-upload-list">
							<table class="layui-table">
								<thead>
									<tr>
										<th>文件名</th>
										<th>大小</th>
										<th>状态</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody id="demoList"></tbody>
							</table>
						</div>
<%--						<button type="button" class="layui-btn" id="testListAction">开始上传</button>--%>
					</div>
				</div>
			</section>
		</div>
	</div>

	<div class="modal-footer">
	<input type="button" id="testListAction" class="btn btn-primary" value="开始上传" />
	<button id="closeBtn" type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
</div>
</form>
	<script>
    		
    		layui.use('upload', function(){
   			var $ = layui.jquery
   			  ,upload = layui.upload;
   			
   			//多文件列表示例
   			  var demoListView = $('#demoList')
   			  ,uploadListIns = upload.render({
	   			    elem: '#testList'
	   			    ,url: '${pageContext.request.contextPath}/cfile/upload?cid=${user.cid}&clsCid='+clsCid
	   			    ,accept: 'file'
	   			    ,multiple: true
	   			    ,auto: false
	   			    ,bindAction: '#testListAction'
	   			    ,choose: function(obj){   
	   			     var clsCid = $('#clsCid').val();
	   			     uploadListIns.config.url = '${pageContext.request.contextPath}/cfile/upload?cid=${user.cid}&clsCid='+clsCid;
	   			      var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
	   			      //读取本地文件
	   			      obj.preview(function(index, file, result){
	   			        var tr = $(['<tr id="upload-'+ index +'">'
	   			          ,'<td>'+ file.name +'</td>'
	   			          ,'<td>'+ (file.size/1014).toFixed(1) +'kb</td>'
	   			          ,'<td>等待上传</td>'
	   			          ,'<td>'
	   			            ,'<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
	   			            ,'<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
	   			          ,'</td>'
	   			        ,'</tr>'].join(''));
	   			        
	   			        //单个重传
	   			        tr.find('.demo-reload').on('click', function(){
	   			          obj.upload(index, file);
	   			        });
	   			        
	   			        //删除
	   			        tr.find('.demo-delete').on('click', function(){
	   			          delete files[index]; //删除对应的文件
	   			          tr.remove();
	   			          uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
	   			        });
	   			        
	   			        demoListView.append(tr);
	   			      });
	   			    }
	   			    ,done: function(res, index, upload){
	   			      if(res.success){ //上传成功
	   			        var tr = demoListView.find('tr#upload-'+ index)
	   			        ,tds = tr.children();
	   			        tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
	   			        tds.eq(3).html(''); //清空操作
	   			        return delete this.files[index]; //删除文件队列已经上传成功的文件
	   			      } else {
	   			       	 this.error(index, upload);
	   			      }
	   			    }
	   			 	,allDone: function(obj){ //当文件全部被提交后，才触发
		   			    console.log(obj.total); //得到总文件数
		   			    console.log(obj.successful); //请求成功的文件数
		   			    console.log(obj.aborted); //请求失败的文件数
		   			    if(obj.aborted==0){
		   			    	$("#closeBtn").click();
			   			     setTimeout( function(){
	                             window.location.reload();
	                     	}, 1 * 1000 );//延迟5000毫米
		   			   	}
		   			    
	   			  	}
	   			    ,error: function(index, upload){
	   			      var tr = demoListView.find('tr#upload-'+ index)
	   			      ,tds = tr.children();
	   			      tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
	   			      tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
	   			    }
	   			  });
   			

		});
	</script>