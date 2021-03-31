<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />

<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css" />

<title>ACS查询页面</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
function queryAcs() {
	
	var devSn = $.trim($("input[@name='devSn']").val());
	var deviceId = $.trim($("input[@name='deviceId']").val());
	if(devSn == "" && deviceId == "") {
		alert("请至少输入设备序列号和设备ID中的一项！");
		return false;
	}
	if (deviceId == "" && devSn.length<6){
		alert("请输入至少最后6位设备序列号 !");
		return false;
	}
	var url = "<s:url value='/itms/resource/queryAcs!getGatherId.action'/>"; 
	$.post(url,{
		devSn : devSn,
		deviceId : deviceId
	},function(ajax){
		if("1"== ajax){
			alert(ajax);
		}else{
			alert(ajax);
		}
	});
}
</script>
</head>
<body>
	
	<form name="addfrm" id="addfrm" target="dataForm">
		<table width="100%" border="0" cellspacing="1" cellpadding="2"
			bgcolor="#999999">
			<tr>
				<td colspan="4" align="center" class="green_title">ACS查询</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td width="15%" align="right" class=column>设备序列号：</td>
				<td width="35%" align="left"><input name="devSn"
					type="text" class="bk" /></td>
				<td width="15%" align="right" class=column>设备ID：</td>
				<td width="35%" align="left"><input name=deviceId type="text"
					class="bk" /></td>
			</tr>

			<tr bgcolor="#ffffff">
				<td colspan="4" align="right"><input type="button" value="查询"
					onclick="queryAcs()" /></td>
			</tr>

		</table>
	</form>
	<div class="content">
		<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
			scrolling="no" width="100%" src=""></iframe>
	</div>
</body>
<%@ include file="/foot.jsp"%>
</html>