<%--
FileName	: softUpgrade.jsp
Date		: 2007��5��10��
Desc		: �������.
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage" />
<%
			request.setCharacterEncoding("gbk");
			DeviceAct deviceAct = new DeviceAct();
			String gatherList = deviceAct.getGatherList(session, "", "", true);
			String deviceModel = versionManage.getDeviceTypeList("", true);

			String file_path = versionManage.getFilePath_1("file_path_1");

%> 
<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
	function selectAll(elmID){
		t_obj = document.all(elmID);
		if(!t_obj) return;
		obj = event.srcElement;
		if(obj.checked){
			if(typeof(t_obj) == "object" ) {
				if(typeof(t_obj.length) != "undefined") {
					for(var i=0; i<t_obj.length; i++){
						t_obj[i].checked = true;
					}
				} else {
					t_obj.checked = true;
				}
			}
		
		}else{
			if(typeof(t_obj) == "object" ) {
				if(typeof(t_obj.length) != "undefined") {
					for(var i=0; i<t_obj.length; i++){
						t_obj[i].checked = false;
					}
				} else {
					t_obj.checked = false;
				}
			}
		}
	}
	function showChild(param){
		var page ="";
		if(param == "gather_id"){
			document.all("div_gather").innerHTML = "<%=deviceModel%>";	
		}
		if(param == "devicetype_id"){
			//alert(document.frm.devicetype_id.value);
			page = "showDeviceList.jsp?gather_id="+document.frm.gather_id.value + "&devicetype_id=" +document.frm.devicetype_id.value;
			document.all("childFrm").src = page;
		}
	}
	function CheckForm(){
		if(document.frm.gather_id.value == -1){
			alert("��ѡ��ɼ��㣡");
			document.frm.gather_id.focus();
			return false;
		}
		if(document.frm.devicetype_id.value == -1){
			alert("��ѡ���豸�ͺţ�");
			document.frm.devicetype_id.focus();
			return false;
		}
				var oselect = document.all("device_id");
		if(oselect == null){
			alert("��ѡ���豸��");
			return false;
		}
		var num = 0;
		if(typeof(oselect.length)=="undefined"){
			if(oselect.checked){
				num = 1;
			}
		}else{
			for(var i=0;i<oselect.length;i++){
				if(oselect[i].checked){
					num++;
				}
			}

		}
		if(num ==0){
			alert("��ѡ���豸��");
			return false;
		}
		var obj = document.frm;	
//-------------------------------------------------------------------
		if(!IsNull(obj.keyword_1.value,'�ؼ���')){
			obj.keyword_2.focus();
			obj.keyword_2.select();
			return false;
		}
		if(obj.filetype_1.value == -1){
			alert("��ѡ���ļ�·����");
			bj.filetype_2.focus();
			return false;
		}
		if(!IsNull(obj.username_1.value,'�û���')){
			obj.username_2.focus();
			obj.username_2.select();
			return false;
		}	
		if(!IsNull(obj.passwd_1.value,'����')){
			obj.passwd_2.focus();
			obj.passwd_2.select();
			return false;
		}
		if(!IsNull(obj.delay_time_1.value,'�������ʱ��')){
			obj.delay_time_2.focus();
			obj.delay_time_2.select();
			return false;
		}			

	}
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="updatedevice.jsp" onsubmit="return CheckForm()">
				<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite">
									���ñ���
								</td>
								<td>
									<img src="../images/attention_2.gif" width="15" height="12">
									��ѡ����豸�������ñ��ݡ�
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													ҵ������:
												</TD>
												<TD width="30%">
													<select name="service_id" class="bk">
														<option value="14">
															���ñ���
														</option>
													</select>
												</TD>
												<TD align="right" width="20%">
													ִ�з�ʽ:
												</TD>
												<TD width="30%">
													<input type="radio" name="excute_type" value="0" checked>
													����ִ��
													<input type="radio" name="excute_type" value="1">
													�ƻ�ִ��
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													�ɼ���:
												</TD>
												<TD width="30%">
													<%=gatherList%>
												</TD>
												<TD align="right" width="20%">
													�豸�ͺ�:
												</TD>
												<TD width="30%">
													<div id="div_gather">
														<select name="device_id" class="bk">
															<option value="-1">
																--����ѡ��ɼ���--
															</option>
														</select>
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right">
													�豸�б�:
													<br>
													<INPUT TYPE="checkbox" onclick="selectAll('device_id')" name="device">
													ȫѡ
												</TD>
												<TD colspan="3">
													<div id="div_device" style="width:95%; height:100px; z-index:1; top: 100px; overflow:scroll">
														<span>����ѡ���豸�ͺţ�</span>
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													�ؼ���:
												</TD>
												<TD width="30%">
													<input type="text" name="keyword_1" maxlength=20 class=bk size=20>
												</TD>
												<TD align="right" width="20%">
													�ļ�����:
												</TD>
												<TD width="30%">
													<select name="filetype_2" class="bk">
														<option value="2 Vendor Configuration File">
															2 Vendor Configuration File
														</option>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													�ļ�·��
												</TD>
												<TD width="" colspan="3">
													<%=file_path%>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													�û���:
												</TD>
												<TD width="30%">
													<input type="text" name="username_1" maxlength=20 class=bk size=20>
												</TD>
												<TD align="right" width="20%">
													����:
												</TD>
												<TD width="30%">
													<input type="text" name="passwd_1" maxlength=20 class=bk size=20>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													ʱ��:
												</TD>
												<TD colspan="3">
													<input type="text" name="delay_time_1" maxlength=20 class=bk size=20>
													&nbsp;&nbsp;(��λ:s)
												</TD>

											</TR>
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">

													<INPUT TYPE="reset" value=" �� �� " class=btn>
													&nbsp;
													<INPUT TYPE="hidden" name="action" value="add">
													&nbsp;
													<INPUT TYPE="submit" value=" �� �� " class=btn>
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<%
	deviceAct = null;
%>
