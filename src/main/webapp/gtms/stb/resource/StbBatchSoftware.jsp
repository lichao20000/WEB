<%--
Author      : 赵子森
Date		: 2018-10-16
Desc		: 机顶盒批量软件升级
--%>
<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	$("input[@name='deviceIds']").val("");
	$("input[@name='taskDesc']").val("");
	gwShare_setGaoji();
});
var deviceIds = "";
function CheckForm(){
	if($("input[@name='deviceIds']").val()==""){
		alert("请选择设备！");
		return false;
	}
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
<%@ include file="../../../toolbar.jsp"%>

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
						<%@ include file="/gtms/stb/share/gwShareDeviceQuery_StbSoftUp.jsp"%>
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
							ACTION="<s:url value="/gtms/stb/resource/Stbsoftware!batchUp.action"/>"
							onsubmit="return CheckForm()">
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="param" value="" />
							<input type="hidden" name="gw_type" value=4 />
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
											<tr bgcolor="#FFFFFF" id="softUp">
											<TD nowrap align="right" width="15%">
													<font color="red">*</font>&nbsp;软件升级目标版本：
												</TD>
												<TD width="">
													根据设备自动匹配
												</TD>
												<TD align="right" width="15%" style="display:none">
													软件升级策略方式：
												</TD>
												<TD width="30%" style="display:none">
													<SELECT name="softStrategy_type" >
                          							 <option value="5">
                               							终端启动
                      								 </option>
                      								 <option value="5">
                               								 下次连接到终端
                      								 </option>
                     								</SELECT>
												</TD>
												 <TD colspan='2'/>
											</TR>
                                   <TR bgcolor="#FFFFFF">
                                        <TD align="right" width="15%">任务描述：</TD>
					                     <TD width="30%" >
					                     <input type="text" name="taskDesc" />
					                     </TD>
					                     <TD colspan='2'/>
                                   	</TR>
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
<%@ include file="../../../foot.jsp"%>
