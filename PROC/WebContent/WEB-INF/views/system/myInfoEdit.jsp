<%@ page contentType="text/html;charset=utf-8"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugins/date-time/datepicker.css" />
<link href="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrapvalidator-master/css/bootstrapValidator.min.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrapfileinput/css/fileinput.min.css" rel="stylesheet" />

<script src="${pageContext.request.contextPath}/static/plugins/date-time/bootstrap-datepicker.js"></script>
<script src="${pageContext.request.contextPath}/static/plugins/date-time/locales/bootstrap-datepicker.zh-CN.js"></script>
<script src="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrapvalidator-master/js/bootstrapValidator.min.js"></script>
<script src="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrapvalidator-master/js/zh_CN.js"></script>
<script src="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrapfileinput/js/fileinput.min.js"></script>
<script src="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrapfileinput/js/fileinput_locale_zh.js"></script>

<script>
	jQuery(function($) {
		//初始化头像上传
		initFileInput("iconFile", "${pageContext.request.contextPath}/user/uploadIcon?cid=${user.cid}");
		
		$('#birthdayId').datepicker({
			format : 'yyyy-mm-dd',
			autoclose : true,
		    language: 'zh-CN'
		});
		
		  // Generate a simple captcha
	    function randomNumber(min, max) {
	        return Math.floor(Math.random() * (max - min + 1) + min);
	    };
	    $('#myForm').bootstrapValidator({
	//        live: 'disabled',
	        message: 'This value is not valid',
	        feedbackIcons: {
	            valid: 'glyphicon glyphicon-ok',
	            invalid: 'glyphicon glyphicon-remove',
	            validating: 'glyphicon glyphicon-refresh'
	        },
	        fields: {
	            crealname: {
	                validators: {
	                    stringLength: {
	                        min: 2,
	                        max: 8,
	                        message: '真实姓名必须为2-8汉字！'
	                    },
	                    regexp: {
	                        regexp:  '[\u4e00-\u9fa5]',
	                        message: '真实姓名必须为汉字！'
	                    }
	                }
	            },
	            studentId: {
	                validators: {
	                    stringLength: {
	                        min: 1,
	                        max: 10,
	                        message: '学号长度必须为1-10位'
	                    }
	                }
	            },
	            email: {
	                validators: {
	                    emailAddress: {
	                        message: '无效的邮箱地址！'
	                    }
	                }
	            },
	            birthday: {
	                validators: {
	                	date: {
	                		format: 'YYYY-MM-DD',
	                        message: '生日必须为日期'
	                    }
	                }
	            }
	        }
	    });
	    $('#resetBtn').click(function() {
       		$('#myForm').data('bootstrapValidator').resetForm(true);
  		});
	    
	});
	function saveReport() { 
		var bootstrapValidator = $("#myForm").data('bootstrapValidator');
        bootstrapValidator.validate();
        if (bootstrapValidator.isValid()){
    		// jquery 表单提交 
			$("#myForm").ajaxSubmit(function(message) { 
			// 对于表单提交成功后处理，message为提交页面saveReport.htm的返回内容 
				if(message.success){
					$('#closeBtn').click();
				} 
				layer.msg(message.msg);
			}); 
			return false; // 必须返回false，否则表单会自己再做一次提交操作，并且页面跳转 
         } else {
        	 return;
         }
	}
	//初始化fileinput控件（第一次初始化）
	function initFileInput(ctrlName, uploadUrl) {
	    var control = $('#' + ctrlName); 
	    control.fileinput({
	        language: 'zh', //设置语言
	        uploadUrl: uploadUrl, //上传的地址
	        allowedFileExtensions : ['jpg', 'png','gif'],//接收的文件后缀
	        showUpload: false, //是否显示上传按钮
	        showCaption: false,//是否显示标题
	        showRemove:false,//是否显示标题
	        browseClass: "btn btn-primary", //按钮样式             
	        previewFileIcon: "<i class='glyphicon glyphicon-king'></i>", 
	    });
	}
</script>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title" id="exampleModalLabel">个人信息修改</h4>
</div>
<form id="myForm" method="POST" class="form-horizontal" action="${pageContext.request.contextPath}/user/myInfoEdit">
<input type="hidden" name="cid" value="${user.cid}"/>
<div class="modal-body">
		<div class="row">
  				 <section>
  				 	<div class="col-lg-12">
  				 			<div class="form-group">
                          	   <label class="col-lg-3 control-label">原来头像：</label>
                            	<div class="col-lg-7">
                            		<img class="img-circle img-responsive"  src="${pageContext.request.contextPath}/${user.icon}" style="width:100%" />
	                            </div>
	                        </div>
  				 			<div class="form-group">
                          	   <label class="col-lg-3 control-label">现在头像：</label>
                            	<div class="col-lg-7">
                            		<input id="iconFile" type="file" name="file">
	                            </div>
	                        </div>
  				 			<div class="form-group">
                            <label class="col-lg-3 control-label">用户名：</label>
                            	<div class="col-lg-7">
                              	  <input type="text" class="form-control" name="cname" value="${user.login.cname}" r />
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <label class="col-lg-3 control-label">密码：</label>
	                            <div class="col-lg-7">
	                                <input type="password" class="form-control" name="cpwd" />
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <label class="col-lg-3 control-label">真实姓名：</label>
	                            <div class="col-lg-7">
	                           	 	<input type="text" class="form-control" name="crealname" value="${user.crealname}"  />
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <label class="col-lg-3 control-label">学号：</label>
	                            <div class="col-lg-7">
	                           	 	<input type="text" class="form-control" name="studentId" value="${user.studentId}" />
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <label class="col-lg-3 control-label">院系：</label>
	                            <div class="col-lg-7">
	                           	 	<input type="text" class="form-control" name="dept" value="${user.dept}"  />
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <label class="col-lg-3 control-label">专业：</label>
	                            <div class="col-lg-7">
	                           	 	<input type="text" class="form-control" name="major"  value="${user.major}" />
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <label class="col-lg-3 control-label">Email：</label>
	                            <div class="col-lg-7">
	                           	 	<input type="text" class="form-control" name="email" value="${user.email}"  />
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <label class="col-lg-3 control-label">出生日期：</label>
	                            <div class="col-lg-7">
	                           	 	<input id="birthdayId" type="text" class="form-control" name="birthday" value="${user.birthday}"  readonly="readonly" />
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <label class="col-lg-3 control-label">兴趣爱好：</label>
	                            <div class="col-lg-7">
	                           	 	<input type="text" class="form-control" name="hobby" value="${user.hobby}"  />
	                            </div>
	                        </div>
  				 	</div>
  				 </section>
  			</div>
</div>
<div class="modal-footer">
	<input type="button" id="okBtn" onclick="saveReport();" class="btn btn-primary" value="提交修改" />
	<button id="closeBtn" type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
</div>
</form>
