<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="/timelater.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ include file="/toolbar.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="x-ua-compatible" content="IE=8" />
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>回收终端使用率</title>
<link href="<s:url value="/css/css_writeOff.css"/>" rel="stylesheet"
	type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/slide.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/echarts-all.js"/>"></script>
<script type="text/javascript">
	function doQuery() {
		$("#toExcel").show();
		var barScript = document.createElement("script");
		barScript.type = "text/javascript";
		barScript.src = "../../Js/echarts/recycleDevBycity.js";
		document.body.appendChild(barScript);
	}
	
	function exportReport() {
		var form = document.getElementById("frm");
		form.action = "<s:url value='/itms/resource/recycleDevRate!parseExcel.action'/>";
		form.submit();
	}
	
	function exportDetail() {
		var form = document.getElementById("frm");
		form.action = "<s:url value='/itms/resource/recycleDevRate!parseDetail.action'/>";
		form.submit();
	}
</script>
</head>
<body>
	<div class="it_main">
		<h3 class="main-ttl">回收终端使用率</h3>
		<div class="search-container">
			<form name="frm" id="frm" >
				<div class="search-item">
					<div class="search-label">属地：</div>
					<div class="search-con">
						<s:select list="cityList" name="cityId" listKey="city_id"
							listValue="city_name" value="cityId" cssClass="select"
							style="width:100px;"></s:select>
					</div>
				</div>
				<div class="search-item">
					<div class="search-label">回收时间：</div>
					<div class="search-con">
						<input type="text" name="starttime" readonly
							value="<s:property value='starttime'/>" /> <img
							name="shortDateimg"
							onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="选择" /> &nbsp;-&nbsp; <input type="text"
							name="endtime" readonly value="<s:property value='endtime'/>" />
						<img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="选择" />
					</div>
					<!--按钮放在最后一个search-item中，保证按钮不会单独折行-->
					<div class="searchbtn-box">
						<a href="javascript:doQuery()" class="search-btn"> 查 询 </a>
					</div>
				</div>
			</form>
		</div>
			<!--内容区域-->
			<div class="chart-container">
				<div class="right-chart">
					<div id="pie" style="height: 270px;"></div>
				</div>
				<div class="left-chart">
					<div id="bar" style="height: 320px;"></div>
				</div>
				<div id="toExcel" class="chart-toolbar" style="display: none;">
					<a href="javascript:void(0);" onclick="exportReport()">
						<i class="iconstyle icon-excel">&nbsp;</i>导出报表</a>
							&nbsp;&nbsp;|&nbsp;&nbsp;
					<a href="javascript:void(0);" onclick="exportDetail()">
						<i class="iconstyle icon-excel">&nbsp;</i>导出清单</a>
				</div>
			</div>
	</div>
</body>

</html>