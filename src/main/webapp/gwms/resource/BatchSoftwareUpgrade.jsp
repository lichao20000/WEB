<%--
Author      : 朱征东
Date		: 2016-10-12
Desc		: 批量软件升级任务定制
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
	<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">
function dyniframesize()
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block"
     			//如果用户的浏览器是NetScape
     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
      			//如果用户的浏览器是IE
     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight)
      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
   			 }
   		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
    		tempobj.style.display="block"
		}
	}
}
var deviceIds = "";
var devicetypeIds = "";
$(function(){
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	gwShare_setGaoji();
	gwShare_setImport();
});

function CheckForm(){
	if($("input[@name='bsuBean.deviceIds']").val()==""){
		alert("请选择设备！");
		return false;
	}
	if($("input[@name='bsuBean.up_begin']").val()==""){
		alert("请选择升级开始时间！");
		$("input[@name='bsuBean.up_begin']").focus();
		return false;
	}
	if($("input[@name='bsuBean.up_end']").val()==""){
		alert("请选择升级结束时间！");
		$("input[@name='bsuBean.up_end']").focus();
		return false;
	}
	if($("input[@name='bsuBean.expire_t']").val()==""){
		alert("请选择升级失效时间！");
		$("input[@name='bsuBean.expire_t']").focus();
		return false;
	}

	if($("input[@name='bsuBean.task_name']").val()==""){
		alert("请填写任务名称！");
		$("input[@name='bsuBean.task_name']").focus();
		return false;
	} 

		var up_begin_check = $("input[@name='bsuBean.up_begin']").val().replace(":","");
		var up_end_check = $("input[@name='bsuBean.up_end']").val().replace(":","");
		if(up_begin_check>up_end_check){
			alert("升级开始时间应小于升级结束时间");
			return false;
		}
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/gwms/resource/batchSoftUpACT!batchSoftwareUp.action'/>";
	form.submit();
	document.getElementById("civiButton").style.display="none";
}

function deviceResult(returnVal){	
	
	
	deviceIds="";
	devicetypeIds="";
	if(returnVal[0]==0){
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[2].length+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";
			devicetypeIds = devicetypeIds + returnVal[2][i][11]+",";		
		}
		deviceIds = deviceIds.substring(0,deviceIds.lastIndexOf(",") );
		devicetypeIds = devicetypeIds.substring(0,devicetypeIds.lastIndexOf(","));
		$("input[@name='bsuBean.deviceIds']").val(deviceIds);
		$("input[@name='bsuBean.devicetypeIds']").val(devicetypeIds);
	}else{
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
		$("input[@name='bsuBean.deviceIds']").val(deviceIds);
		$("input[@name='bsuBean.devicetypeIds']").val(devicetypeIds);
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
									批量软件升级任务定制
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
						<%@ include file="/gwms/share/gwShareDeviceQuery_upgrade.jsp"%>
					</td>
				</TR>
				<TR>
					<TH colspan="4" align="center">
						配置升级策略
					</TH>
				</TR>
				<tr>
					<td>
						<FORM NAME="mainForm" id="mainForm" METHOD="post"
							ACTION=""  target="dataForm">
							<input type="hidden" name="bsuBean.deviceIds" value="" />
							<input type="hidden" name="bsuBean.devicetypeIds" value="" />
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
											<tr bgcolor="#FFFFFF">

												<td align="right" width="15%">
													软件升级策略方式：
												</td>
												<td align="left" width="30%">
													立即执行
												</td>

												<td align="right" width="15%">
													软件升级目标版本：
												</td>
												<td align="left" width="30%">
													根据设备自动匹配
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">

												<td align="right" width="15%">
													升级开始时间：
												</td>
												<td align="left" width="30%">
													<input type="text"  id="up_begin" name="bsuBean.up_begin" readonly class=bk >
														<img name="shortDateimg"
									 						onClick="WdatePicker({el:document.getElementById('up_begin'),dateFmt:'HH:mm',skin:'whyGreen'})"
									 							src="../../images/dateButton.png" width="15" height="12"
									 								border="0" alt="选择">&nbsp;<font color="#FF0000">*</font>
												</td>

												<td align="right" width="15%">
													升级结束时间：
												</td>
												<td align="left" width="30%">
													<input type="text" id="up_end" name="bsuBean.up_end" readonly class=bk >
														<img name="shortDateimg"
									 						onClick="WdatePicker({el:document.getElementById('up_end'),dateFmt:'HH:mm',skin:'whyGreen'})"
									 							src="../../images/dateButton.png" width="15" height="12"
									 								border="0" alt="选择">&nbsp;<font color="#FF0000">*</font>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">

												<td align="right" width="15%">
													升级失效时间：
												</td>
												<td align="left" width="30%">
													<input type="text" id="expire_time" name="bsuBean.expire_t" readonly class=bk >
														<img name="shortDateimg"
									 						onClick="WdatePicker({el:document.getElementById('expire_time'),dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									 							src="../../images/dateButton.png" width="15" height="12"
									 								border="0" alt="选择">&nbsp;<font color="#FF0000">*</font>
												</td>

												<td align="right" width="15%">
													是否执行升级：
												</td>
												<TD align="left" width="30%"><select name="bsuBean.status" class="bk">
													<option value="1">==是==</option>
													<option value="0">==否==</option>
													</select>&nbsp;<font color="#FF0000">*</font></TD>
											</tr>
											<tr bgcolor="#FFFFFF">

												<td align="right" width="15%">
													定制任务名称：
												</td>
												<td align="left" width="30%">
													<INPUT TYPE="text" NAME="bsuBean.task_name"
														maxlength=60 class=bk size=20>&nbsp;<font color="#FF0000">*</font>
												</td>
												<td align="right" width="15%">
													
												</td>
												<td align="left" width="30%">
													
												</td>
												
											</tr>
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">
													<INPUT TYPE="button" id="civiButton" onclick="javascript:CheckForm()" value=" 定 制 " class=btn>
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</FORM>
						<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center" id="query_Form">
			<TR>
				<TD bgcolor=#999999 id="idData"><iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="100%" src=""></iframe></TD>
			</TR>
		</TABLE>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
	<TR>
		
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
