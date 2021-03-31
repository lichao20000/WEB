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
});
var deviceIds = "";
var returnValthis ="init";
var max = '<s:property value="maxActive" escapeHtml="false" />';
function CheckForm(){
	if($("input[@name='deviceIds']").val()==""){
		alert("请选择设备！");
		return false;
	}
	var mode = $("#mode").val();
	if(mode=="2"){
		if((returnValthis[0]==0&&returnValthis[2].length>max)||(returnValthis[0]!=0&&returnValthis[0]>max)){
			alert("设备数量大于"+max+"只能选择被动模式！");
			$("#mode").val("1");
			$("#softUp").show();
			return false;
		}
	}
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
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
		$("input[@name='deviceIds']").val("0");
		$("input[@name='param']").val(returnVal[1]);
	}
	var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
	$("#gwShare_queryType_this").val(gwShare_queryType);
}

function change_mode(){
	var mode = $("#mode").val();
	if(mode=="1"){
		$("#softUp").show();
	}
	else{
		if($("input[@name='deviceIds']").val()!=""){//已经选择了设备
			if((returnValthis[0]==0&&returnValthis[2].length>max)||(returnValthis[0]!=0&&returnValthis[0]>max)){
				alert("设备数量大于"+max+"只能选择被动模式！");
				$("#mode").val("1");
				$("#softUp").show();
				return;
			}
		}
		$("#softUp").hide();
	}
}

var iscqsoft = true;

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
						<jsp:include page="/gwms/share/gwShareDeviceQuery.jsp" flush="false">
						<jsp:param name="CQ_softUp" value="1"/>
						</jsp:include>
					</td>
				</TR>
				<TR>
					<TH colspan="4" align="center">
						配置升级策略
					</TH>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post"
							ACTION="<s:url value="/gwms/resource/software!batchUp4cq.action"/>"
							onsubmit="return CheckForm()">
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="param" value="" />
							<input type="hidden" name="gw_type" value="<%=gwType%>" />
							<input type="hidden" name="maxActive" id="maxActive"  />
							<input type="hidden" name="gwShare_queryType_this" id="gwShare_queryType_this"  />

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
											 <td align="right" width="15%">任务名称:</td>
											 <TD width="85%" colspan="3">
											  <input type="text" name='taskName' id="taskName" style="width:25%"/>
											 </TD>
											</tr>
											<!-- add end -->
											<tr bgcolor="#FFFFFF" >
												<TD align="right" width="15%">
													模式：
												</TD>
												<TD width="85%" colspan="3">
													<select name="mode" id="mode" class=bk onchange="change_mode()">
														<option value="1">被动模式</option>
														<option value="2">主动模式</option>
														<!-- <option value="26">STB业务</option> -->
													</select>
												</TD>
											</tr>
											<tr bgcolor="#FFFFFF" id="softUp">

												<TD align="right" width="15%">
													升级策略方式(默认重启触发)：
												</TD>
												<TD width="30%">
													<s:property value="softStrategyHTML" escapeHtml="false" />
												</TD>

												<td align="right" width="15%">
													软件升级目标版本：
												</td>
												<td align="left" width="30%">
													根据设备自动匹配
												</td>
											</tr>
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">
													<INPUT TYPE="submit" value=" 执 行 " class=btn>
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
