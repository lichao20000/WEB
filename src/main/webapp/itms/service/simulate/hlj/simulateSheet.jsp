<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>黑龙江BSS模拟工单</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	//显示工单参数信息
	function showSheet(){
		var servTypeId = $.trim($("select[@name='servTypeId']").val());
		var operateType = $.trim($("select[@name='operateType']").val());
		if ('-1' == servTypeId) {
			alert("请选择业务类型");
			$("select[@name='servTypeId']").focus();
			return;
		}
		if ('-1' == operateType) {
			alert("请选择操作类型");
			$("select[@name='operateType']").focus();
			return;
		}
		$("tr[@id='trData']").show();
	    $("div[@id='QueryData']").html("正在模拟，请稍等....");
	    $("button[@name='button']").attr("disabled", true);
		var url = "<s:url value='/itms/service/simulateHLJSheet!showSheet.action'/>";
		$.post(url, {
			servTypeId:servTypeId,
			operateType:operateType
		}, function(ajax) {
			$("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
			$("button[@name='button']").attr("disabled", false);
		});
	} 
</script>
</head>
<body>
	<!-- <form name="mainfrm" target="dataForm"> -->
	<form action="<s:url value="/itms/service/simulateHLJSheet!showSheet.action"/>" method="post" target="dataForm">
		<table width="100%" align="center">
			<tr>
				<td style="height: 20px;">&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">BSS模拟工单</td>
							<td align="left"><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /> BSS模拟工单</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">
						<tr>
							<th colspan="4">BSS模拟工单</th>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td class=column align="right" width="20%">业务类型</td>
							<td width="30%">
								<select name="servTypeId" class=bk>
									<option value="-1">==请选择业务类型==</option>
									<option value="20" selected="selected">建设流程</option>
									<option value="22">上网业务</option>
									<option value="15">VOIP业务(H248)</option>
								</select>&nbsp; 
								<font color="#FF0000">* </font>
							</td>
							<td class="column" align="right" width="20%">操作类型</td>
							<td width="30%">
								<select name="operateType" class=bk onchange="">
									<option value="-1">==请选择操作类型==</option>
									<option value="1" selected="selected">开户</option>
									<option value="3">销户</option>
								</select>&nbsp; <font color="#FF0000">* </font>
							</td>
						</tr>
						<tr>
							<td colspan="5" align="right" class="foot">
								<button name="button" onclick="showSheet();">&nbsp;模拟工单&nbsp;</button>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="30"></td>
			</tr>
			<tr id="trData" style="display: none">
				<td class="colum">
					<div id="QueryData" style="width: 100%; z-index: 1; top: 100px;text-align: left;">
						正在模拟，请稍等....
					</div>
				</td>
			</tr>
			<tr>
				<td height="20"></td>
			</tr>
			<tr>
				<td>
					<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
<%@ include file="/foot.jsp"%>