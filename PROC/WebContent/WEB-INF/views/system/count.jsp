<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <meta charset="UTF-8">
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/jq/jquery-1.8.2.min.js"></script> 
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/jq/echarts.min.js"></script> 

    <style type="text/css">
        body {
            padding-bottom: 40px;
        }
        .sidebar-nav {
            padding: 9px 0;
        }

        @media (max-width: 980px) {
            /* Enable use of floated navbar text */
            .navbar-text.pull-right {
                float: none;
                padding-left: 5px;
                padding-right: 5px;
            }
        }


    </style>
</head>
<body>
<div id="main" style="width: 100%;height:750px;margin-top:10px;"></div>

<div id="main2" style="width: 100%;height:750px;margin-top:10px;"></div>
</body>
<script type="text/javascript">
	// 基于准备好的dom，初始化echarts实例
	var myChart = echarts.init(document.getElementById('main'));
	var myChart2 = echarts.init(document.getElementById('main2'));
	
	option = {
			title: {
		        text: '                        传输文件统计图-柱状'
		    },
		    legend: {},
		    tooltip: {},
		    toolbox: {
		        feature: {
		            saveAsImage: {}
		        }
		    },
		    dataset: {
		    	 dimensions: ['product', '传输文件数'],
			        source: [
						<c:forEach items="${transList}" var="trans" varStatus="status">
							{product: '${trans.mt}月', '传输文件数': ${trans.num}},
						</c:forEach>
			        ]
		    },
		    xAxis: {type: 'category'},
		    yAxis: {},
		    // Declare several bar series, each will be mapped
		    // to a column of dataset.source by default.
		    series: [
		        {type: 'bar'}
		    ]
		};

	option2 = {
		    title: {
		        text: '       传输文件统计图-饼图'
		    },
		    tooltip: {
		        trigger: 'item',
		        position:'right',
		        formatter: '{b}({d}%)'
		    },
		 
		    toolbox: {
		        feature: {
		            saveAsImage: {}
		        }
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    series : [
		              {
		                  name: '传输文件',
		                  type: 'pie',    // 设置图表类型为饼图
		                  radius: '55%',  // 饼图的半径，外半径为可视区尺寸（容器高宽中较小一项）的 55% 长度。
		                //重点
			           label : {
			                normal : {
			                formatter: '{b}({d}%)',
			                textStyle : {
				               fontWeight : 'normal',
				               fontSize : 15
							}
							}
			     		},
		             	 data: [
								<c:forEach items="${transList2}" var="trans2" varStatus="status">
								 {value:${trans2.num}, name:'${trans2.type}'},
								</c:forEach>
							]
		              }
		   	]
	
		};

	// 使用刚指定的配置项和数据显示图表。
	myChart.setOption(option);
	myChart2.setOption(option2);
	
</script>
</html>