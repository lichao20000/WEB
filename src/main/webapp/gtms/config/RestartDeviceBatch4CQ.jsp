<%--
Author      : 王森博
Date		: 2010-4-15
Desc		: 手动的批量软件升级
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<%
 	String gwType = request.getParameter("gw_type");

	String type = "2";

	if("1".equals(type)){
	}else if("2".equals(type)){
	}else{
		response.sendRedirect("../../login.jsp");
	}

 %>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	gwShare_setGaoji();
	gwShare_setImport();
});
var deviceIds = "";
var returnValthis ="init";
var max = '<s:property value="maxActive" escapeHtml="false" />';

function CheckForm(){

	if($("input[@name='deviceIds']").val()==""){
		alert("请选择设备！");
		return false;
	}

	if($.trim($("input[@name='taskName']").val()).length==0){
		alert("请输入任务名称！");
		return false;
	}

	if($.trim($("input[@name='restartNum']").val()).length==0){
		alert("请输入每次重启数量！");
		return false;
	}

	if($.trim($("input[@name='restartInterval']").val()).length==0){
		alert("请输入重启间隔时间！");
		return false;
	}

	var reg = /^[0-9]+$/;
	var restartNumStr = $.trim($("input[@name='restartNum']").val());
	var restartIntervalStr = $.trim($("input[@name='restartInterval']").val());

	for(var i=0;i<restartNumStr.length;i++){
		if(!reg.test(restartNumStr.substring(i,i+1))){
			alert("每次重启数量必须是整数！");
			return false;
		}
	}

	for(var i=0;i<restartIntervalStr.length;i++){
		if(!reg.test(restartIntervalStr.substring(i,i+1))){
			alert("重启间隔时间必须是整数！");
			return false;
		}
	}

	if(parseInt($.trim($("input[@name='restartNum']").val()))>=10000 || parseInt($.trim($("input[@name='restartNum']").val()))==0){
		alert("每次重启数量大于0小于10000！");
		return false;
	}

	if(parseInt($.trim($("input[@name='restartInterval']").val()))>=1000 || parseInt($.trim($("input[@name='restartInterval']").val()))==0){
		alert("重启间隔时间大于0小于1000分钟！");
		return false;
	}

 	$("input[id='submitId']").attr("disabled", true);

	return true;
}

function deviceResult(returnVal){
	returnValthis = returnVal;
	deviceIds="";
	if(returnVal[0]==0){
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[2].length+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";
		}
		$("input[@name='deviceIds']").val(deviceIds);
	}else{

		if(returnVal[0]>200000){
			alert("定制数量不可以超过20万！");
			return;
		}

		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
		$("input[@name='deviceIds']").val("0");
		$("input[@name='param']").val(returnVal[1]);
	}
	var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
	$("#gwShare_queryType_this").val(gwShare_queryType);
}


var iscqsoft = true;

//点击增加的时候增加一组
var flag = 0;

</SCRIPT>
<%@ include file="../../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>

			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									批量重启任务定制
								</td>
								<td nowrap>
									<img src="<s:url value='/images/attention_2.gif'/>" width="15"
										height="12">

								</td>
							</tr>
						</table>
					</td>
				</tr>
				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</TR>
				<TR>
					<TH colspan="4" align="center">
						批量重启任务定制
					</TH>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post"
							ACTION="<s:url value="/gtms/config/restartDeviceBatch!addTaskInfo4CQ.action"/>"
							onsubmit="return CheckForm()">
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="param" value="" />
							<input type="hidden" name="gw_type" value="<%=gwType%>" />
							<input type="hidden" name="maxActive" id="maxActive"  />
							<input type="hidden" name="gwShare_queryType_this" id="gwShare_queryType_this"  />
							<input type="hidden" name="type" id="type"  value="<%=type %>"/>

							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<div id="selectedDev" >
														请查询设备！
													</div>
												</td>
											</tr>
											<!-- add begin -->
											<tr bgcolor="#FFFFFF">
											 <td align="right" width="20%">任务名称:</td>
											 <TD width="80%" colspan="3">
											  <input type="text" name='taskName' id="taskName" style="width:25%"/>
											 </TD>
											</tr>

											<tr bgcolor="#FFFFFF">
											 <td align="right" width="20%">每次重启数量:</td>
											 <TD width="80%" colspan="3">
											  <input type="text" name='restartNum' id="restartNum" style="width:25%"/>
											 </TD>
											</tr>

											<tr bgcolor="#FFFFFF">
											 <td align="right" width="20%">重启间隔时间:</td>
											 <TD width="80%" colspan="3">
											  <input type="text" name='restartInterval' id="restartInterval" style="width:25%"/>
											 </TD>
											</tr>

											<tr>
								<td colspan="4" align="right" class=column>
								</td>
							</tr>
							<TR>
									<TD class=column colspan="4">
									</TD>
								</TR>
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">
													<INPUT TYPE="submit" value=" 执 行 " class=btn id="submitId">
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</FORM>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../../foot.jsp"%>
