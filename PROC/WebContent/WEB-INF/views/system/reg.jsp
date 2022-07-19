<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<!--让部分国产浏览器默认采用高速模式渲染页面 -->
    <meta name="renderer" content="webkit">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>用户注册</title>
    <!-- Bootstrap -->
    <link href="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrapvalidator-master/css/bootstrapValidator.min.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/static/reg/css/reg.css" rel="stylesheet" />
    <script src="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrap-3.3.7-dist/js/jquery/jquery-1.12.4.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrapvalidator-master/js/bootstrapValidator.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/plugins/bootstrap/bootstrapvalidator-master/js/zh_CN.js"></script>
    <!--[if lt IE 9]>
      <script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <style>  
	    .col-center-block {  
	        float: none;  
	        display: block;  
	        margin-left: auto;  
	        margin-right: auto;  
	    }  
	    body {
			    background: url(${pageContext.request.contextPath}/static/login/img/bg.jpg) no-repeat fixed;
			    background-size: 100% 100%;
			    width: 100%;
			    height: 100%;
		}
    </style>  
  </head>
  <body>
  		<div class="container-fluid">
  			<div class="row">
  				 <section>
  				 	<div class="col-lg-8 col-lg-offset-2">
  				 		<div class="mainBox">
	  				 		<div class="page-header">
	  				 			<h2><span class="glyphicon glyphicon-user" aria-hidden="true" style="color: #1da02b"></span>&nbsp;&nbsp;用户注册</h2>
	  				 		</div>
	  				 		<form id="defaultForm" action="${pageContext.request.contextPath}/doReg" method="post" class="form-horizontal" >
	  				 			<div class="form-group">
	                            <label class="col-lg-4 control-label">用户名：</label>
	                            <div class="col-lg-7">
	                                <input type="text" class="form-control" name="cname" />
		                            </div>
		                        </div>
		                        <div class="form-group">
		                            <label class="col-lg-4 control-label">密码：</label>
		                            <div class="col-lg-7">
		                                <input type="password" class="form-control" name="cpwd" />
		                            </div>
		                        </div>
		                        <div class="form-group">
		                            <label class="col-lg-4 control-label">重复密码：</label>
		                            <div class="col-lg-7">
		                                <input type="password" class="form-control" name="confirmPassword" />
		                            </div>
		                        </div>
		                        <div class="form-group">
		                            <label class="col-lg-4 control-label">真实姓名：</label>
		                            <div class="col-lg-7">
		                           	 	<input type="text" class="form-control" name="crealname" />
		                            </div>
		                        </div>
		                        <div class="form-group">
		                            <label class="col-lg-4 control-label">学号：</label>
		                            <div class="col-lg-7">
		                           	 	<input type="text" class="form-control" name="studentId" />
		                            </div>
		                        </div>
		                        <div class="form-group">
		                            <label class="col-lg-4 control-label">院系：</label>
		                            <div class="col-lg-7">
		                           	 	<input type="text" class="form-control" name="dept" />
		                            </div>
		                        </div>
		                        <div class="form-group">
		                            <label class="col-lg-4 control-label">专业：</label>
		                            <div class="col-lg-7">
		                           	 	<input type="text" class="form-control" name="major" />
		                            </div>
		                        </div>
		                        <div class="form-group">
		                            <label class="col-lg-4 control-label">email：</label>
		                            <div class="col-lg-7">
		                           	 	<input type="text" class="form-control" name="email" />
		                            </div>
		                        </div>
		                        <div class="form-group">
		                            <label class="col-lg-4 control-label">出生日期(例如：2018-09-05)：</label>
		                            <div class="col-lg-7">
		                           	 	<input type="text" class="form-control" name="birthday" />
		                            </div>
		                        </div>
		                        <div class="form-group">
		                            <label class="col-lg-4 control-label">兴趣爱好：</label>
		                            <div class="col-lg-7">
		                           	 	<input type="text" class="form-control" name="hobby" />
		                            </div>
		                        </div>
		                        <div class="form-group">
		                            <label class="col-lg-4 control-label" id="captchaOperation"></label>
		                            <div class="col-lg-7">
		                                <input type="text" class="form-control" name="captcha" />
		                            </div>
		                      	</div>
		                        <div class="form-group">
		                            <div class="col-lg-9 col-lg-offset-3">
		                                <button type="submit" class="btn btn-warning" name="signup" value="Sign up">提交注册</button>
		                                <button type="reset" class="btn btn-info" id="resetBtn">重置</button>
		                            </div>
	                      	    </div>
	  				 		</form>
	  				 	</div>
  				 	</div>
  				 </section>
  			</div>
  		</div>
		<script type="text/javascript">
		$(document).ready(function() {
		    // Generate a simple captcha
		    function randomNumber(min, max) {
		        return Math.floor(Math.random() * (max - min + 1) + min);
		    };
		    $('#captchaOperation').html([randomNumber(1, 50), '+', randomNumber(1, 50), '='].join(' '));
		
		    $('#defaultForm').bootstrapValidator({
		//        live: 'disabled',
		        message: 'This value is not valid',
		        feedbackIcons: {
		            valid: 'glyphicon glyphicon-ok',
		            invalid: 'glyphicon glyphicon-remove',
		            validating: 'glyphicon glyphicon-refresh'
		        },
		        fields: {
		            cname: {
		                message: '帐号不合法！',
		                validators: {
		                    notEmpty: {
		                        message: '帐号为必填项！'
		                    },
		                    stringLength: {
		                        min: 2,
		                        max: 20,
		                        message: '帐号长度必须为2-20位'
		                    },
		                    regexp: {
		                        regexp: /^[a-zA-Z0-9_\.]+$/,
		                        message: '帐号不能含有特殊字符！'
		                    },
		                    remote: {
		                        type: 'POST',
		                        url: '${pageContext.request.contextPath}/checkUserName',
		                        delay :  2000,
		                        message: '帐号已经被注册过，请更换！'
		                    },
		                    different: {
		                        field: 'cpwd,confirmPassword',
		                        message: '帐号和密码不能相同！'
		                    }
		                }
		            },
		            cpwd: {
		                validators: {
		                    notEmpty: {
		                        message: '密码为必填项！'
		                    },
		                    regexp: {
		                        regexp: /^[A-Za-z]+$/,
		                        message: '密码必须为大小写字母！'
		                    },
		                    identical: {
		                        field: 'confirmPassword',
		                        message: '密码和重复密码不一致！'
		                    },
		                    different: {
		                        field: 'cname',
		                        message: '用户名和密码不能相同！'
		                    }
		                }
		            },
		            confirmPassword: {
		                validators: {
		                    notEmpty: {
		                        message: '重复密码为必填项！'
		                    },
		                    regexp: {
		                        regexp: /^[A-Za-z]+$/,
		                        message: '密码必须为大小写字母！'
		                    },
		                    identical: {
		                        field: 'cpwd',
		                        message: '密码和重复密码不一致！'
		                    },
		                    different: {
		                        field: 'cname',
		                        message: '用户名和密码不能相同!'
		                    }
		                }
		            },
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
		            },
		            
		            captcha: {
		                validators: {
		                    callback: {
		                        message: '计算错误！',
		                        callback: function(value, validator) {
		                            var items = $('#captchaOperation').html().split(' '), sum = parseInt(items[0]) + parseInt(items[2]);
		                            return value == sum;
		                        }
		                    }
		                }
		            }
		        }
		    });
		     $('#resetBtn').click(function() {
	       		$('#defaultForm').data('bootstrapValidator').resetForm(true);
	  		});
		});
		</script>

  </body>
</html>