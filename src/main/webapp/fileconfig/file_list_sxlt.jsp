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
	
	//����
	String cityList = deviceAct.getCityListAll(false,"","city_id",request);
	
	//ҵ��
	String serviceList = FileManage.getServiceList(false,"","service_id",request);
	
	String area = LipossGlobals.getLipossProperty("InstArea.ShortName");
%>
<SCRIPT LANGUAGE="JavaScript">

var area = '<%=area%>'

function queryfile()
{

	if (document.frm._vendor_id.value == "-1")
	{
		alert("��ѡ����");
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
	
	document.all("operResult").innerHTML="����ͳ��,���Ժ�......";
	var page="file_listdata.jsp?device_model_id="+ document.frm.device_model_id.value
			+ "&filestatus=" + status 
			+ "&filename="+document.frm.filename.value
			+ "&vendor_id="+document.frm._vendor_id.value
			+ "&city_id=" + document.frm.city_id.value + "&service_id=" + document.frm.service_id.value;		
	document.all("childFrm").src = page;
}

function delWarn(){
	if(confirm("���Ҫɾ�����ļ���\n��������ɾ���Ĳ��ָܻ�������")){
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

	window.open(page,"�汾�ļ�","width=800,height=520,resizable=no,scrollbars=yes");
}

function editFile(param) {
	
	param = eval("("+param+")");
	
	var page = "fileEdit.jsp";
	page += "?softwarefile_id=" + param.softwarefile_id;

	window.open(page,"�汾�ļ�","width=800,height=520,resizable=no,scrollbars=yes");
}

function deleteFile(param) {

	if (delWarn() == false)
	{
		return;
	}

	var page = "fileSave.jsp";
	page += "?action=delete";
	page += "&softwarefile_id=" + param;
	// window.open(page,"�汾�ļ�","width=800,height=520,resizable=no,scrollbars=yes");
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
							�汾�ļ�����
						</td>
						<td>
							&nbsp;<img src="../images/attention_2.gif" width="15" height="12">
								&nbsp;��ѯ�汾�ļ��������ϵİ汾�ļ�����<font color="red">*</font>�ı���ѡ�������.
						</td>
					</tr>
				</table>
			</td>
		</tr>		
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="4" align="center"><B>�汾�ļ���ѯ</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="10%" nowrap>����</TD>
					<ms:inArea areaCode="sx_lt" notInMode="false">
						<TD width="40%" colspan="3">
							<INPUT TYPE="text" name="filename" class="bk">&nbsp;
						</TD>
					</ms:inArea>
					<ms:inArea areaCode="sx_lt" notInMode="true">
						<TD width="40%">
							<INPUT TYPE="text" name="filename" class="bk">&nbsp;
						</TD>
						<TD class=column align="right">״̬</TD>
						<TD >
							<input type="radio" name="filestatus" value="1" checked >�����
							<input type="radio" name="filestatus" value="2">δ���								
						</TD>
					</ms:inArea>
				</TR>

				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">
						<font color="red">*</font>&nbsp;�豸����
					</TD>
					<TD>
						<%=str_VendorList%>
					<TD class=column align="right">
						�豸�ͺ�
					</TD>
					<TD>
						<div id="div_device_model_id">
							<select name="device_model_id" class="bk">
								<option value="-1">
									--����ѡ����--
								</option>
							</select>
						</div>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">����</TD>
					<TD>
						<%=cityList %>
					</TD>
					<TD class=column align="right">ҵ��</TD>
					<TD>
						<%=serviceList %>
					</TD>
				</TR>
				
				<TR class=green_foot>
					<TD colspan="4" align="right" >
						<INPUT TYPE="button" value=" �� ѯ " class=btn onclick="queryfile()">&nbsp;&nbsp;
						<!-- <INPUT TYPE="button" value=" �� �� " class=btn onclick="addFile()">&nbsp;&nbsp; -->
						<INPUT TYPE="reset" value=" �� �� " class=btn>
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
