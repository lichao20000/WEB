<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage" />
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<%
	//String gatherList = versionManage.getDeviceTypeList("",false);
	
	//设备厂商
	String strVendorList = deviceAct.getVendorList(true, "", "");
%>
<SCRIPT LANGUAGE="JavaScript">
function queryfile()
{
	if (document.frm.vendor_id.value != "-1" && document.frm.devicetype_id.value == "-1"){
		alert("请选择设备型号");
		return false;
	}
	
	document.all("userTable").innerHTML="<TABLE border=0 cellspacing=1 cellpadding=2 width='100%'><tr><td bgcolor='#ffffff'>正在统计,请稍后......</td></tr></table>";
	var page="model_listdata.jsp?template_id="+ document.frm.template_id.value+"&template_name="+document.frm.template_name.value
				+"&devicetype_id="+document.frm.devicetype_id.value;		
	document.all("childFrm").src = page;
}
function delWarn(){
	if(confirm("真的要删除该配置模板吗？\n本操作所删除的不能恢复！！！")){
		return true;
	}
	else{
		return false;
	}
}
function showChild(parname){
	if(parname == "vendor_id")
	{
		page = "showDeviceModel.jsp?vendor_id="+document.frm.vendor_id.value;
		document.all("childFrm1").src = page;
	}
}
</SCRIPT>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR>
	<TD valign=top>
	<FORM NAME="frm" METHOD="post" ACTION="UserSave.jsp" onsubmit="return CheckForm()">
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<BR>
		<BR>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							模板管理
						</td>
						<td>
							<img src="../images/attention_2.gif" width="15" height="12">
								查询当前系统中存在的模板。
						</td>
					</tr>
				</table>
			</td>
		</tr>				
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=s2 width="100%">
				<TR>
					<TH  colspan="4" align="center"><B>配置模板查询</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="20%" nowrap>模板ID：</TD>
					<TD width="30%">
						<INPUT TYPE="text" name="template_id" class="bk">&nbsp;
					</TD>
					<TD class=column align="right" width="20%" nowrap>模板名称：</TD>
					<TD width="30%">
						<INPUT TYPE="text" name="template_name" class="bk">
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>厂商：</TD>
						<TD align="left"><div id="div_vendor"><%=strVendorList %></div></TD>
						<TD class=column align="right" nowrap>设备型号：</TD>
						<TD>
							<div id="div_device_model_id">
								<select name=devicetype_id class="bk">
									<option value="-1">--请先选择厂商--</option>
								</select>
							</div>
						</TD>
				</TR>
				<TR height="23">
					<TD colspan="4" align="right" class=foot>
						<INPUT TYPE="button" value=" 查 询 " class=jianbian onclick="queryfile()">&nbsp;&nbsp;
					</TD>
				</TR>
			</TABLE>
			</TD>
		</TR>
		<TR height="23" nowrap><TD></TD></TR>
		<TR style="display:none" id="dispTr">
			<TD><span id="userTable"></span></TD>
		</TR>
		<TR>
			<TD HEIGHT=20>&nbsp; 
				<IFRAME ID="childFrm" SRC="" STYLE="display:none">
				</IFRAME>
			</TD>
			<TD HEIGHT=20>&nbsp; 
				<IFRAME ID="childFrm1" SRC="" STYLE="display:none">
				</IFRAME>
			</TD>
		</TR>		
	</TABLE>
	</FORM>
	</TD>
</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
