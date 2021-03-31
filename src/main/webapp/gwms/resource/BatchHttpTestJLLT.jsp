<%--
Author      : fanjiaming
Date		: 2017-6-14
Desc		: 批量HTTP下载业务质量检测
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ include file="../head.jsp"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
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
});
var deviceIds = "";
function CheckForm(){
	if($("input[@name='deviceIds']").val()==""){
		alert("请选择设备！");
		return false;
	}
	
	/* if($("input[@name='http_url']").val()==""){
		alert("请输入测速平台url！");
		return false;
	}
	
	if($("input[@name='report_url']").val()==""){
		alert("请输入测试上报url！");
		return false;
	} */

	$("#excutebutton").attr("disabled","disabled");

	return true;
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
									<ms:inArea areaCode="hb_lt" notInMode="true">
									批量HTTP下载测速
									</ms:inArea>
									<ms:inArea areaCode="hb_lt" notInMode="false">
									批量HTTP上下行测速
									</ms:inArea>
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
						<%@ include file="/gwms/share/gwShareDeviceQuery_batchHttpTestJllt.jsp"%>
					</td>
				</TR>
				<TR>
					<TH colspan="4" align="center">
						
						<ms:inArea areaCode="hb_lt" notInMode="true">
						配置HTTP下载业务质量检测策略
						</ms:inArea>
						<ms:inArea areaCode="hb_lt" notInMode="false">
						配置HTTP上下行业务质量检测策略
						</ms:inArea>
					</TH>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post"
							ACTION="<s:url value="/gwms/resource/batchHttpTest!speedTest.action"/>"
							onsubmit="return CheckForm()">
							<input type="hidden" name="hblt_SpeedTest_flg" value="true" />
							<input type="hidden" name="hblt_BatchSpeedTest_flag" value="true" />
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="param" value="" />
							<input type="hidden" name="gw_type" value="1" />
							<input type="hidden" name="fileName_st" value="" />
							<input type="hidden" name="gw_type" value="<%=gwType%>" />
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
											<ms:inArea areaCode="hb_lt" notInMode="false">
											<tr bgcolor="#FFFFFF">
												<TD colspan="1" class="column" >
													批量测速模式：
												</TD>
												<TD colspan="3" class="column" >
													<input type="radio" name="type" size="20" value="1" checked="checked" />下行测速
													<input type="radio" name="type" size="20" value="2" />上行测速
												</TD>
											</tr>
											</ms:inArea>
											<%if (!("ah_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")))){%>
											<tr bgcolor="#FFFFFF" id="softUp">

												<TD colspan="1" class="column" >
													批量测速策略方式：
												</TD>
												<TD colspan="3" class="column" >
													<SELECT name="softStrategy_type" class="bk" style='width:150px'>
														<OPTION value='下次连接到系统' selected>下次连接到系统</OPTION>
														<OPTION value='终端启动' >终端启动</OPTION>
													</SELECT>
												</TD>
											</tr>
											<tr bgcolor="#FFFFFF" id="softUp">

												<TD colspan="1" class="column" >
													任务描述：
												</TD>
												<TD colspan="3" class="column" >
													<input type="text" name="task_desc" class="bk" value=""/>
												</TD>
											</tr>
											 <%}%>
											<!-- <tr bgcolor="#FFFFFF" id="softUp" >
												<TD colspan="1" class="column"  >
													测速url：
												</TD>
												<TD colspan="1" class="column">
													<input type="text" name="http_url" class="bk" value="http://121.28.169.98:8084/services/contractrate/contract/rate/"/>
												</TD>
												<TD colspan="1" class="column" >
													上报url：
												</TD>
												<TD colspan="1" class="column" >
													<input type="text" name="report_url" class="bk" value="http://121.28.169.98:8084/services/contractrate/results/insert/"/>
												</TD>
											</tr> -->
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">
													<INPUT id='excutebutton' TYPE="submit" value=" 执 行 " class=btn>
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
