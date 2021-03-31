<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>终端核销不一致清单统计</title>
<link href="<s:url value="/css/css_writeOff.css"/>" rel="stylesheet"
	type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">

	function queryDevVerification() {
			var cityId = $("select[@name='cityId']").val();
		document.getElementById("queryButton").disabled = true;
		var frm = document.getElementById("frm");
		frm.action = "<s:url value='/itms/resource/devVerificationDisList!queryDevVerification.action'/>";
		frm.submit();
	}
	
	function exportQueryDevVerification() {
		var form = document.getElementById("frm");
		form.action = "<s:url value='/itms/resource/devVerificationDisList!parseExcel.action'/>";
		form.submit();
	}

	//** iframe自动适应页面 **//
	//输入你希望根据页面高度自动调整高度的iframe的名称的列表
	//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
	//定义iframe的ID
	var iframeids = [ "dataForm" ]

	//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
	var iframehide = "yes"

	function dyniframesize() {
		var dyniframe = new Array()
		for (i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//自动调整iframe高度
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block"
					//如果用户的浏览器是NetScape
					if (dyniframe[i].contentDocument
							&& dyniframe[i].contentDocument.body.offsetHeight)
						dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
					//如果用户的浏览器是IE
					else if (dyniframe[i].Document
							&& dyniframe[i].Document.body.scrollHeight)
						dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
				}
			}
			//根据设定的参数来处理不支持iframe的浏览器的显示问题
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i])
				tempobj.style.display = "block"
			}
		}
	}

	$(window).resize(function() {
		dyniframesize();
	});
</script>
</head>

<body>
	<div class="it_main">
		<h3 class="main-ttl">终端核销不一致清单统计</h3>
		<div class="search-container">
			<form name="frm" id="frm" target="dataForm">
				<div class="search-item">
					<div class="search-label-blue">属地：</div>
					<div class="search-con">
						<s:select list="cityList" name="cityId" listKey="city_id"
							listValue="city_name" value="1234" cssClass="select"
							style="width:100px;"></s:select>
					</div>
				</div>
				<div class="search-item">
					<div class="search-label-blue">核销日期：</div>
					<div class="search-con">
						<input type="text" name="starttime" readonly
							value="<s:property value='starttime'/>"/> 
							<img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="选择" /> &nbsp;-&nbsp; <input type="text"
							name="endtime" readonly value="<s:property value='endtime'/>"/>
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" />
					</div>
				</div>
				<div class="search-item">
					<div class="search-label-blue">核销方式：</div>
					<div class="search-con">
						<select name = 'useType' class="select" style="width: 85px;">
							<option value="-1" selected>全部</option>
							<option value="1">自动核销</option>
							<option value="2">人工核销</option>
						</select>
					</div>
				</div>
				<div>
					<div class="search-item">
						<div class="search-label-blue">是否一致：</div>
						<div class="search-con">
							<select name = 'isMatch' class="select" style="width: 65px;">
								<option value="-1" selected>全部</option>
								<option value="1">是</option>
								<option value="0" >否</option>
							</select>
						</div>
						<!--按钮放在最后一个search-item中，保证按钮不会单独折行-->
						<div class="searchbtn-box">
							<a href="javascript:void(0);" class="search-btn" onclick="queryDevVerification()" id="queryButton"> 查 询 </a> 
							<a href="javascript:void(0);" class="search-btn" onclick="exportQueryDevVerification()"> 导 出 </a>
						</div>
					</div>
				</div>
			</form>
			<div class="content">
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
				scrolling="no" width="100%" src=""></iframe>
		</div>
		</div>
	</div>
</body>
<%@ include file="/foot.jsp"%>
</html>
