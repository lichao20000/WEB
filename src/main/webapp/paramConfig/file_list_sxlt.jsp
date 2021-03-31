<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="com.linkage.litms.common.util.SetCharacterEncodingFilter"%>
<%@page import="com.linkage.litms.common.database.*"%>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage" />
<jsp:useBean id="fileManage" scope="request" class="com.linkage.litms.filemanage.FileManage" />

<%
	com.linkage.litms.resource.DeviceAct deviceAct = new com.linkage.litms.resource.DeviceAct();
	String strVendorList = deviceAct.getVendorList(true, "", "");
	String str_VendorList = "";
	String gw_type = request.getParameter("gw_type");
	if("4".equals(gw_type)){
		str_VendorList = deviceAct.getStbVendorList(true, "","_vendor_id");
	}
	else{
		str_VendorList = deviceAct.getVendorList(true, "","_vendor_id");
	}

	int fileType = 2;
	Cursor cursor = (Cursor) fileManage.getCursor(fileType);
	Map fields = cursor.getNext();
%>

<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">


$(function(){
	queryfile();
});

function openwin(dir,name)
{
	name = name.replace("+","%2B");
	name = name.replace("&","%26");
	name = name.replace("#","%23");

	window.open("./showFile.jsp?dir="+dir+"&name="+name,"ShowFile","height=1000,width=800,top=10,left=10,toolbar=no,menubar=no,location = no,status = yes,resizable=yes,scrollbars=yes");
}
	
function doSave(dir_id,name)
{
	var page="doFilsSave.jsp?dir_id=" + dir_id + "&name=" + name;
	document.all("childFrm").src=page;
}
	
function showChild(param)
{
	if(param == "fileserver"){
		var serValue = document.frm.fileserver.value;
		if(serValue == -1){
			document.frm.serUser.value="";
			document.frm.serPass.value="";
		}else{
			var arrServer = serValue.split("|");
			document.frm.serUser.value=arrServer[3];
			document.frm.serPass.value=arrServer[4];
		}		
	}else if(param =="devicetype_id"){
		var page="showDeviceBox.jsp?devicetype_id=" + encodeURIComponent(document.frm1.devicetype_id.value);
		document.all("childFrm").src=page;
	}
	
	if(param == "vendor_id"){
		page = "showDeviceModel.jsp?vendor_id="+document.frm.vendor_id.value;
		document.all("childFrm").src = page;		
	}
	if(param == "_vendor_id"){
		page = "showDeviceModel.jsp?vendor_id="+document.frm1._vendor_id.value + "&flag=1";
		document.all("childFrm").src = page;
	}
	if(param == "_devicetype_id"){
		page = "getDeviceBox.jsp?vendor_id="+document.frm1._vendor_id.value + "&devicetype_id=" + document.frm1._devicetype_id.value;
		document.all("childFrm").src = page;
	}
}

function showChild(param1,param2)
{
	if(param1 == "_vendor_id"){
		page = "showDeviceModel.jsp?vendor_id="+document.frm1._vendor_id.value + "&flag=1&gw_type="+<%=gw_type%>;
		document.all("childFrm").src = page;		
	}
	
	if(param1 == "_devicetype_id"){
		page = "getDeviceBox.jsp?vendor_id="+document.frm1._vendor_id.value + "&devicetype_id=" + document.frm1._devicetype_id.value+"&gw_type="+<%=gw_type%>;
		document.all("childFrm").src = page;
	}
}
	
function checkform()
{
	var filetype = 0;
	for(var i=0;i< document.frm.filestatus.length;i++)
	{
		if(document.frm.filestatus[i].checked)
		{
			filetype=document.frm.filestatus[i].value;
			break;
		}
	}
	document.frm.file_status.value=filetype;
	if(document.frm.devicetype_id.value == -1){
		alert("请选择设备型号！");
		document.frm.devicetype_id.focus();
		return false;
	}
	if(!IsNull(document.frm.filename.value,"文件名")){
		document.frm.filename.focus();
		document.frm.filename.select();
		return false;
	}
}

function GoList(page)
{
	this.location = page;
}

var show = 0;
//根据特定条件搜索
function showSearch()
{
	if(show == 0){
		show = 1;
		document.all("Button_1").value = "隐藏添加";
		document.all("searchLayer").style.display="";
	}else{
		show = 0;
		document.all("Button_1").value = "添加文件";
		document.all("searchLayer").style.display="none";
	}
}
	
function queryfile()
{
	if(document.frm1._vendor_id.value != -1){
		if(document.frm1._devicetype_id.value == -1){
			alert("请选择设备型号!");
			document.frm1._devicetype_id.focus();
			return false;
		}
	}
	
	document.all("userTable").innerHTML="<TABLE border=0 cellspacing=1 cellpadding=2 width='100%'><tr><td bgcolor='#ffffff'>正在统计,请稍后......</td></tr></table>";
	
	var page="file_listdata.jsp?vendor_id="+document.frm1._vendor_id.value
			+ "&devicetype_id="+ document.frm1._devicetype_id.value
			+ "&filestatus=" + 1
			+ "&filename="+document.frm1.filename.value
			+ "&device_id=" + document.frm1.device_id.value 
			+ "&s_area_id=" + 1 + "&refresh="+Math.random()+"&gw_type="+<%=gw_type%>;
	document.all("childFrm").src = page;
}

function delWarn()
{
	if(confirm("真的要删除该文件吗？\n本操作所删除的不能恢复！！！")){
		return true;
	}
	return false;
}

function areaSelect(_name,_id)
{
	var $ = document.all;
	var page = "../system/AreaSelect.jsp?area_pid="+<%=user.getAreaId()%>+"&width=360";
	var returnObj = window.showModalDialog(page,"","left=20,top=20,width=360,height=450,resizable=no,scrollbars=no");
	if(returnObj != null){
		$(_name).value = returnObj.area_name;
		$(_id).value = returnObj.area_id;
	}
}
</SCRIPT>
<style>
NOBR.BT
{
	padding-right: 6px;
	padding-left: 6px;
	padding-top: 2px;
	border-top: #ffffff 1px solid;
	border-bottom: #ffffff 1px solid;
	border-right: #ffffff 1px solid;
	border-left: #ffffff 1px solid;
	cursor:hand;
}

NOBR.BTOver{
	padding-right: 6px;
	padding-left: 6px;
	padding-top: 2px;
	border-top: #316AC5 1px solid;
	border-bottom: #316AC5 1px solid;
	border-right: #316AC5 1px solid;
	border-left: #316AC5 1px solid;
	cursor:hand;
	background-color: #C1D2EE;
}
</style>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
	<TR>
		<TD valign=top>
			<FORM NAME="frm" METHOD="post" ACTION="fileSave.jsp"
				onsubmit="return checkform()">
				<TABLE id="searchLayer" width="98%" border=0 cellspacing=0
					cellpadding=0 align="center" style="display:none">
					<TR><TD HEIGHT=20>&nbsp;</TD></TR>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR>
									<TH colspan="4" align="center"><B>添加配置文件</B></TH>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right" width="15%" nowrap>名称</TD>
									<TD class=column width="35%">
										<INPUT TYPE="text" NAME="filename" class="bk">&nbsp;
									</TD>
									<TD class=column align="right" width="15%">状态</TD>
									<TD class=column width="35%">
										<input type="radio" name="filestatus" value="1" checked>已审核
										<input type="radio" name="filestatus" value="2">未审核
										<input type="hidden" name="file_status" value="">
									</TD>
								</TR>
								
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right">设备厂商</TD>
									<TD class=column><%=strVendorList%></TD>
									<TD class=column align="right">设备型号</TD>
									<TD class=column>
										<div id="div_devicetype">
											<select name="devicetype_id" class="bk">
												<option value="-1">--请先选择厂商--</option>
											</select>
										</div>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right" nowrap>存储路径</TD>
									<TD class=column colspan="3" nowrap>
										<select name="fileserver" class="bk" onchange="showChild('fileserver')">
											<%if (fields == null) {%>
												<option value="-1">==此项没有记录==</option>
											<%} else {%>
												<option value="-1">=====请选择=====</option>
												<%while (fields != null) {%>
													<option value="<%=fields.get("inner_url")%>|<%=fields.get("server_dir")%>|<%=fields.get("dir_id")%>|<%=fields.get("access_user")%>|<%=fields.get("access_passwd")%>|<%=fields.get("outter_url")%>">
														<%=fields.get("server_name")%>
													</option>
	
													<%fields = cursor.getNext();
												}
											}
											%>
										</select>&nbsp;&nbsp;
										<input type="radio" value="0" name="inORout" checked>内网上传&nbsp;&nbsp;
										<input type="radio" value="1" name="inORout">外网上传&nbsp;&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right">用户名</TD>
									<TD class=column>
										<input type="text" name="serUser" class="bk" value="">
									</TD>
									<TD class=column align="right">密码</TD>
									<TD class=column>
										<input type="password" name="serPass" class="bk" value="">
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right">描述</TD>
									<TD class=column colspan="3">
										<textarea name="remark" cols="70" class="bk" rows="4"></textarea>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right">管理域</TD>
									<TD class=column colspan=3>
										<input type="text" name="area_name" class="bk" value="">
										<input type="hidden" name="area_id" value="-1">
										<input type=hidden name="button1" value="选择域" onclick=areaSelect('area_name','area_id')>
										<nobr class="BT" onmouseover="this.className='BTOver'"
											onmouseout="this.className='BT'" onclick=areaSelect('area_name','area_id')>
											<IMG SRC="../system/images/search.gif" WIDTH="15" HEIGHT="12"
												BORDER="0" ALT="选择域" valign="middle">
											&nbsp;选择
										</nobr>
									</TD>
								</TR>
								
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right">选择文件</TD>
									<TD colspan="3" class=column>
										<iframe name=uploadFrm FRAMEBORDER=0 SCROLLING=NO
											src="FileUpload.jsp?type=office" height="25" width=600>
										</iframe>
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</FORM>
		</TD>
	</TR>

	<TR>
		<TD valign=top>
			<FORM NAME="frm1" METHOD="post" ACTION="" onsubmit="">
				<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
					<TR><TD HEIGHT=20>&nbsp;</TD></TR>
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite" nowrap>
										配置文件查询
									</td>
									<td nowrap>
										<img src="../images/attention_2.gif" width="15" height="12">
										查询配置文件服务器上的配置文件。&nbsp;&nbsp;
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR>
									<TH colspan="4" align="center"><B>配置文件查询</B></TH>
								</TR>
								<TR bgcolor="#FFFFFF">
										<TD class=column align="right" width="10%" nowrap>唯一标识</TD>
									<TD width="30%">
										<INPUT TYPE="text" name="filename" class="bk">&nbsp;
									</TD>
									<TD class=column align="right" width="10%" nowrap>设备厂商</TD>
									<TD width="30%"><%=str_VendorList%></TD>
								</TR>

								<TR bgcolor="#FFFFFF">
									<TD class=column align="right">设备型号</TD>
									<TD>
										<div id="_div_devicetype">
											<select name="_devicetype_id" class="bk">
												<option value="-1">--请先选择厂商--</option>
											</select>
										</div>
									</TD>
									<TD class=column align="right">设备</TD>
									<TD class=column>
										<div id="id_device">
											<select name="device_id" class="bk">
												<option value="-1">--请先选择设备型号--</option>
											</select>
										</div>
									</TD>
								</TR>
								
								<TR class="green_foot">
									<TD colspan="4" align="right" >
										<INPUT TYPE="button" value=" 查 询 " onclick="queryfile()">
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
					<TR height="20" nowrap><TD></TD></TR>
					<TR style="display:none" id="dispTr">
						<TD><span id="userTable"></span></TD>
					</TR>
					<TR>
						<TD HEIGHT=20>
							&nbsp;<IFRAME ID="childFrm" SRC="" STYLE="display:none"></IFRAME>
						</TD>
						<TD HEIGHT=20>
							&nbsp;<IFRAME ID="childFrm1" SRC="" STYLE="display:none"></IFRAME>
						</TD>
					</TR>
				</TABLE>
			</FORM>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
