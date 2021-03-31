<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>终端综合管理系统-ITMS</title>
<link href="../css/inmp/master.css" rel="stylesheet" type="text/css" />
<link href="../css/inmp/ITMScss.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../Js/inmp/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../Js/inmp/jquery.tabso_yeso.js"></script>
<script type="text/jscript" src="../Js/inmp/tab.js"></script>
<script type="text/javascript" src="../Js/inmp/echarts-plain.js"></script>
<script type="text/javascript" src="<s:url value="../Js/inmp/commFunction.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/inmp/jquery.blockUI.js"></SCRIPT>
<script type="text/javascript">
	function block(){   
		$.blockUI({
			overlayCSS:{ 
		        backgroundColor:'#CCCCCC', 
		        opacity:0.6 
	    	},
			message:"<font size=3>正在操作，请稍后...</font>"
		});      
	}
	
	function unblock(){
		$.unblockUI();
	} 

	$(function() {
		$("#Inav li:has('ul')").mouseover(function() {
			$(this).children("ul").show();
		})
		$("#Inav li:has('ul')").mouseout(function() {
			$(this).children("ul").hide();
		})
	})
	$(function() {

		$(window).scroll(
				function() { //只要窗口滚动,就触发下面代码 

					var scrollt = document.documentElement.scrollTop
							+ document.body.scrollTop; //获取滚动后的高度 

					if (scrollt > 200) { //判断滚动后高度超过200px,就显示  

						$("#toTOP").fadeIn(400); //淡出     

					} else {

						$("#toTOP").stop().fadeOut(400); //如果返回或者没有超过,就淡入.必须加上stop()停止之前动画,否则会出现闪动   

					}

				});

		$("#toTOP").click(function() { //当点击标签的时候,使用animate在200毫秒的时间内,滚到顶部

			$("html,body").animate({
				scrollTop : "0px"
			}, 200);

		});

	});
	var iframeSrc=["<s:url value='/inmp/resource/QueryDevice.jsp?gw_type=1'/>","<s:url value='/inmp/resource/itmsRelease.jsp?gw_type=1'/>","<s:url value='/inmp/resource/itmsInst!init.action?gw_type=1'/>",
				   "<s:url value='/inmp/bss/ChangeConnectionType.jsp?gw_type=1'/>","<s:url value='/inmp/bss/bssSheetServ!init.action?gw_type=1&netServUp=true'/>","<s:url value='/inmp/bss/ServiceDone.jsp?gw_type=1'/>",
				   "<s:url value='/inmp/diagnostics/jt_device_zendan_from3.jsp?gw_type=1'/>","<s:url value='/inmp/diagnostics/jt_device_zendan_from4.jsp?gw_type=1'/>","<s:url value='/inmp/diagnostics/diagnosticsProcess.jsp?gw_type=1'/>"	   
			];
	function changeNav(index){
		//alert(index);
		//$("div[@id='div_other']").html("");
		//$("div[@id='div_other']").append("/inmp/resource/QueryDevice.jsp?gw_type=1");
		if(index == 11){
			this.div_pie.style.display="";
			this.div_bar.style.display="";
			this.myiframe.style.display = "none";
			window.location.href = "/itms/inmp/index.jsp";
		}else{
			this.div_pie.style.display="none";
			this.div_bar.style.display="none";
			this.myiframe.style.display = "";
			
			document.all("myiframe1").src = iframeSrc[index];
			unblock();
			dyniframesize();
		}
		//document.all("tr_other").style.display="";
	}

</script>
</head>
<body>
	<div class="mainBox" style="z-index:2;">
		<!-----------页面主体部分开始---------->
		<div class="main" id="main" style="margin-left: 0;">
			<!--itms nav开始-->
			<div class="itmsNav">
				<ul class="itmsNav_list" id="Inav">
					<li><span class="split_line"><a href="javascript:changeNav(11);">首页</a></span></li>
					<li><span class="split_line"><a>设备管理</a></span>
						<ul class="itms_sunbav">
							<li><a href="javascript:changeNav(0);">家庭网关设备查询</a></li>
							<li><a href="javascript:changeNav(1);">用户设备解绑</a></li>
							<li><a href="javascript:changeNav(2);">家庭网关手工安装</a></li>
						</ul></li>
					<li><span class="split_line"><a>业务查询</a></span><span
						class="icon"></span>
						<ul class="itms_sunbav">
							<li><a href="javascript:changeNav(3);">变更上网方式</a></li>
							<li><a href="javascript:changeNav(4);">BSS业务查询</a></li>
							<li><a href="javascript:changeNav(5);">手工业务下发</a></li>
						</ul></li>
					<li><span class="split_line"><a>故障管理</a></span><span
						class="icon"></span>
						<ul class="itms_sunbav">
							<li><a href="javascript:changeNav(6);">远程重启</a></li>
							<li><a href="javascript:changeNav(7);">远程恢复出厂设置</a></li>
							<li><a href="javascript:changeNav(8);">故障处理</a></li>
						</ul></li>
					<li><span class="split_line" style="background: none;"><a>软件升级</a></span><span
						class="icon"></span>
						<ul class="itms_sunbav">
							<li><a href="javascript:changeNav(9);">批量软件升级</a></li>
							<li><a href="javascript:changeNav(10);">简单软件升级</a></li>
						</ul></li>
				</ul>
			</div>
			<!--itms nav结束-->
		</div>
	</div>
</body>
</html>