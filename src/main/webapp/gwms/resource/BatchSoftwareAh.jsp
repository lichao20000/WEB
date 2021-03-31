<%--
Author      : 张四辈
Date		: 2013-6-5
Desc		: 批量软件升级
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<%
 String gwType = request.getParameter("gw_type");
 %>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<script type="text/javascript"
			src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">

/**
*将高级查询，导入查询和升级导入查询显示出来
**/
$(function(){
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	gwShare_setGaoji();
	gwShare_setImport();
	getTargetHtml();
});
var deviceIds = "";
/**
* 验证并提交
**/
function CheckForm(){
	var filename = $("input[@name='fileName']").val();
	var starttime =$("#starttime").val();
	var endtime = $("#endtime").val();
	if (null == filename || filename == "")
	{
		if($("input[@name='deviceIds']").val()==""){
		alert("请选择设备！");
		return false;
		}
	}
	
	if($("select[@name='goal_devicetype_id']").val()=="-1")
	{
		alert("请选择目标版本！");
		return  false;
	}
	if(null == starttime || ""==starttime){
		alert("请选择起始时间");
		return  false;
	}
	if(null == endtime || ""==endtime){
		alert("请选择结束时间");
		return  false;
	}
	document.frm.action='<s:url value="/gwms/resource/software!batchUpAh.action"/>';
	document.frm.submit();	
}

/**
* 查询出设备
**/
function deviceResult(returnVal){	
	deviceIds="";
	if(returnVal[0]==0){
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[2].length+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";			
		}
		$("input[@name='deviceIds']").val(deviceIds);
	}else{
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
		$("input[@name='deviceIds']").val("0");
		$("input[@name='param']").val(returnVal[1]);
	}
	
}
/**
*查询出设备
**/
function deviceUpResult(returnVal){	
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
		var gw_type = $("input[@name='gw_type']").val();
		$("input[@name='fileName']").val(returnVal[1]);
		$("input[@name='gwShare_queryType']").val(returnVal[2]);
	}
/**
*获取目标版本信息
**/	
function getTargetHtml(){
	var url = "showSoftwareversion4Ah.jsp";
	$.post(url,{
		type:1
	},function(msg){
		$("div[@id='div_goal_softwareversion']").html("");
		$("div[@id='div_goal_softwareversion']").append(msg);
	});	
	
}
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
									批量软件升级
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
						配置升级策略
					</TH>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post">
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="fileName" value="" />
							<input type="hidden" name="param" value="" />
							<input type="hidden" name="gw_type" value="<%=gwType%>" />
							<input type="hidden" name="gwShare_queryType" value="" />
							
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<div id="selectedDev">
														请查询设备！
													</div>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF" id="softUp">

												<TD align="right" width="15%">
													起始时间：
												</TD>
												<TD width="30%">
													<input type="text" id="starttime" name="starttime" class='bk' readonly>
													<img name="shortDateimg"
														 onClick="WdatePicker({el:document.frm.starttime,dateFmt:'H:mm',skin:'whyGreen'})"
														 src="../../images/dateButton.png" width="15" height="12"
														 border="0" alt="选择">
												</TD>
												<td align="right" width="15%">
													结束时间：
												</td>
												<td align="left" width="30%">
													<input type="text" id="endtime" name="endtime" vaule="2:00">
													<img name="shortDateimg"
														 onClick="WdatePicker({el:document.frm.endtime,dateFmt:'H:mm',skin:'whyGreen'})"
														 src="../../images/dateButton.png" width="15" height="12"
														 border="0" alt="选择">
												</td>
											</tr>
											<tr bgcolor="#FFFFFF" id="softUp">
												<td align="right" width="15%">
													软件升级目标版本：
												</td>
												<td align="left" width="85%"  colspan="3">
													<div id="div_goal_softwareversion">
													</div>
												</td>
											</tr>
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">
													<INPUT TYPE="button" value=" 执 行 " class=btn" onclick="CheckForm()">
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
<%@ include file="../foot.jsp"%>
