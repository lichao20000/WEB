<%--
FileName	: softUpgrade.jsp
Date		: 2007年5月10日
Desc		: 软件升级.
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage" />
<%@ include file="../../head.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	gwShare_setGaoji();
	gwShare_setImport();
});
	
var deviceIds = "";
var deviceNum = "";	
function deviceResult(returnVal){	
	deviceIds="";
	if(returnVal[0]==0){
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[2].length+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";			
		}
		$("input[@name='deviceIds']").val(deviceIds);
		deviceNum = returnVal[0];
	}else{
		if(returnVal[0]>400000){
			alert("定制数量不可以超过40万！");
			return;
		}
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
		$("input[@name='deviceIds']").val("0");
		$("input[@name='param']").val(returnVal[1]);
		deviceNum = returnVal[0];
	}
}

function doQuery(){
	var deviceIds = $("input[@name='deviceIds']").val();
	var strategy_type = $("select[@name='strategy_type']").val();
	
	var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
	var param = $("input[@name='param']").val();
	
	if(deviceIds == ""){
		alert("请选择设备");
		return;
	}
		
		$("button[@id='btn']").attr("disabled",true);
		$("tr[@id='trData']").show();
		$("div[@id='QueryData']").html("正在执行批量定制操作，请稍等....");
		url = "<s:url value='/gtms/config/BatchAddIptvNodeACT!addTaskInfoHBLT.action'/>";
		$.post(url,{
			strategy_type : strategy_type,
			deviceIds : deviceIds,
			param : param
		},function(ajax){
			$("div[@id='QueryData']").html(ajax);
			$("button[@id='btn']").attr("disabled",true);
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
									批量新增IPTV路由节点业务
								</td>
								<td nowrap>
									<!-- <img src="../images/attention_2.gif" width="15" height="12">
									单次最大10000台 -->
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery_httpDownloadInfo4NX.jsp"%>
					</td>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post" ACTION="/gtms/config/BatchAddIptvNodeACT!addTaskInfoHBLT.action" target="childFrm" >
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="param" value="" />
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
					<tr bgcolor=#ffffff>
						<td class=column align=right width="15%">策略方式：</td>
						<td class=column align=left colspan="3">
						<select name="strategy_type">
						<option value="3">终端重启</option>
						<option value="4">下次连接到系统</option>
						</select>
						</td>
					</tr>
					
						
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button id="btn" onclick="doQuery();">&nbsp;定&nbsp;制&nbsp;</button>
						</td>
					</tr>
											
					<tr id="trData" style="display: none" bgcolor=#ffffff>
						<td colspan=4>
							<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
								正在努力为您查询，请稍等....</div>
						</td>
					</tr>						
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
			<IFRAME ID=childFrm name=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../../foot.jsp"%>
