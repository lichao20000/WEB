<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags" %>
<%@ page import="com.linkage.litms.filemanage.FileManage" %>
<%@page import="com.linkage.litms.LipossGlobals"%>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<%
	//String deviceTypeList = versionManage.getDeviceTypeList("",false);
	
	String str_VendorList = deviceAct.getVendorList(true, "",
			"_vendor_id");
	
	//属地
	String cityList = deviceAct.getCityListAll(false,"","city_id",request);
	
	//业务
	String serviceList = FileManage.getServiceList(false,"","service_id",request);
	
	String area = LipossGlobals.getLipossProperty("InstArea.ShortName");
%>
<SCRIPT LANGUAGE="JavaScript">

var area = '<%=area%>'

function queryfile()
{

	if (document.frm._vendor_id.value == "-1")
	{
		alert("请选择厂商");
		return;
	}

	var status = 0;
	
	if(area != 'sx_lt'){
		for(var i=0;i< document.frm.filestatus.length ;i++){
			if(document.frm.filestatus[i].checked){
			
				status = document.frm.filestatus[i].value;
				break;
			}
		}
	} else {
		status = 1;
	}
	
	document.all("operResult").innerHTML="正在统计,请稍后......";
	var page="file_listdata.jsp?device_model_id="+ document.frm.device_model_id.value
			+ "&filestatus=" + status 
			+ "&filename="+document.frm.filename.value
			+ "&vendor_id="+document.frm._vendor_id.value
			+ "&city_id=" + document.frm.city_id.value + "&service_id=" + document.frm.service_id.value;		
	document.all("childFrm").src = page;
}

function delWarn(){
	if(confirm("真的要删除该文件吗？\n本操作所删除的不能恢复！！！")){
		return true;
	}
	else{
		return false;
	}
}

function showChild(param){

	if(param == "_vendor_id"){
		page = "showDeviceModel.jsp?vendor_id="+document.frm._vendor_id.value + "&flag=1";
		document.all("childFrm").src = page;
	}
}

function addFile() {
	var page = "file_uploadform.jsp";

	window.open(page,"版本文件","width=800,height=520,resizable=no,scrollbars=yes");
}

function editFile(param) {
	
	param = eval("("+param+")");
	
	var page = "fileEdit.jsp";
	page += "?softwarefile_id=" + param.softwarefile_id;

	window.open(page,"版本文件","width=800,height=520,resizable=no,scrollbars=yes");
}

function deleteFile(param) {

	if (delWarn() == false)
	{
		return;
	}

	var page = "fileSave.jsp";
	page += "?action=delete";
	page += "&softwarefile_id=" + param;
	// window.open(page,"版本文件","width=800,height=520,resizable=no,scrollbars=yes");
  location.href = page;
}
</SCRIPT>

<style type="text/css">
<!--
select {
	position:relative;
	font-size:12px;
	width:160px;
	line-height:18px;border:0px;
	color:#909993;
}
-->
</style>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<FORM NAME="frm" METHOD="post" ACTION="" onsubmit="return CheckForm()">

<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="98%" BORDER="0" align="center">
<TR>
	<TD valign=top>
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
		<TR>
			<TD HEIGHT=20><div id="operResult" style='width:20%;display:none;background-color:#33CC00'></div></TD>
		</TR>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							版本文件管理
						</td>
						<td>
							&nbsp;<img src="../images/attention_2.gif" width="15" height="12">
								&nbsp;查询版本文件服务器上的版本文件。带<font color="red">*</font>的必须选择或输入.
						</td>
					</tr>
				</table>
			</td>
		</tr>		
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="4" align="center"><B>版本文件查询</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="10%" nowrap>名称</TD>
					<ms:inArea areaCode="sx_lt" notInMode="false">
						<TD width="40%" colspan="3">
							<INPUT TYPE="text" name="filename" class="bk">&nbsp;
						</TD>
					</ms:inArea>
					<ms:inArea areaCode="sx_lt" notInMode="true">
						<TD width="40%">
							<INPUT TYPE="text" name="filename" class="bk">&nbsp;
						</TD>
						<TD class=column align="right">状态</TD>
						<TD >
							<input type="radio" name="filestatus" value="1" checked >已审核
							<input type="radio" name="filestatus" value="2">未审核								
						</TD>
					</ms:inArea>
				</TR>

				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">
						<font color="red">*</font>&nbsp;设备厂商
					</TD>
					<TD>
						<%=str_VendorList%>
					<TD class=column align="right">
						设备型号
					</TD>
					<TD>
						<div id="div_device_model_id">
							<select name="device_model_id" class="bk">
								<option value="-1">
									--请先选择厂商--
								</option>
							</select>
						</div>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">属地</TD>
					<TD>
						<%=cityList %>
					</TD>
					<TD class=column align="right">业务</TD>
					<TD>
						<%=serviceList %>
					</TD>
				</TR>
				
				<TR class=green_foot>
					<TD colspan="4" align="right" >
						<INPUT TYPE="button" value=" 查 询 " class=btn onclick="queryfile()">&nbsp;&nbsp;
						<!-- <INPUT TYPE="button" value=" 增 加 " class=btn onclick="addFile()">&nbsp;&nbsp; -->
						<INPUT TYPE="reset" value=" 重 置 " class=btn>
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
	</TD>
</TR>
</TABLE>

</FORM>

<%@ include file="../foot.jsp"%>
