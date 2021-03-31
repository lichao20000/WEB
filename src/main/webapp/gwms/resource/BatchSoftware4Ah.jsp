<%--
Author      : 王森博
Date		: 2010-4-15
Desc		: 手动的批量软件升级
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<%
 String gwType = request.getParameter("gw_type");
 %>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">


$(function(){
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	gwShare_setGaoji();
	gwShare_setImport();
	gwShare_setUpImport();
	getTargetHtml();
});
var deviceIds = "";
function CheckForm(){
	var filename = $("input[@name='fileName']").val();
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

	url ="<s:url value="/gwms/resource/software!checkTmpData.action"/>";

	$.post(url,{

	},function(ajax){
			if (ajax > 0)
			{
				alert("有上次未完成的任务,请等待");
			} else
			{
				document.frm.action='<s:url value="/gwms/resource/software!batchUp.action"/>';
				document.frm.submit();
			}
	});
}


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

function deviceUpResult(returnVal){
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
		var gw_type = $("input[@name='gw_type']").val();
		$("input[@name='fileName']").val(returnVal[1]);
		$("input[@name='gwShare_queryType']").val(returnVal[2]);
	}



function getTargetHtml(){
	var url = "showSoftwareversion4Ah.jsp";
	$.post(url,{
		type:1
	},function(msg){
		$("div[@id='div_goal_softwareversion']").html("");
		$("div[@id='div_goal_softwareversion']").append(msg);
	});

}
function selectGoalSoftFile()
{

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
													软件升级策略方式：
												</TD>
												<TD width="30%">
													<s:property value="softStrategyHTML" escapeHtml="false" />
												</TD>

												<td align="right" width="15%">
													软件升级目标版本：
												</td>
												<td align="left" width="30%">
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
