<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage" />
<%
	request.setCharacterEncoding("gbk");
	String file_path2 = versionManage.getVersionFilePath("");
%>
<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">

	function showChild(param){
		if(param == "file_path_2"){
			if(document.frm.file_path_2.value != -1){
				var file = document.frm.file_path_2.value;
				var arrFile = file.split("\|");
				document.frm.file_size_2.value=arrFile[2];
				document.frm.filename_2.value=arrFile[1];
			}else{
				document.frm.file_size_2.value="";
				document.frm.filename_2.value="";			
			}
		}
		if(param == "file_path_3"){
			if(document.frm.file_path_3.value != -1){
				var file = document.frm.file_path_3.value;
				var arrFile = file.split("\|");
				document.frm.file_size_3.value=arrFile[2];
				document.frm.filename_3.value=arrFile[1];
			}else{
				document.frm.file_size_3.value="";
				document.frm.filename_3.value="";			
			}
		}
	}
	function ShowUser(Index){
		switch(Index){
	    case '1':
	    	document.frm.username_3.value= document.frm.username_1.value;  
	    	break;
	    case '2':
	    	document.frm.passwd_3.value= document.frm.passwd_1.value;  
	    	break;
	    case '3':
	    	document.frm.username_1.value= document.frm.username_3.value;  
	    	break;
	    case '4':
	    	document.frm.passwd_1.value= document.frm.passwd_3.value;  
	    	break;	   	
	   }   
	}
	function CheckForm(){
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
		if(obj.file_path_2.value == -1){
			alert("��ѡ���ļ�·����");
			obj.file_path_2.focus();
			return false;
		}
		if(!IsNull(obj.file_size_2.value,'�ļ���С')){
			obj.file_size_2.focus();
			obj.file_size_2.select();
			return false;
		}	
		if(!IsNull(obj.filename_2.value,'�ļ���')){
			obj.filename_2.focus();
			obj.filename_2.select();
			return false;
		}
		if(!IsNull(obj.delay_time_2.value,'ʱ��')){
			obj.delay_time_2.focus();
			obj.delay_time_2.select();
			return false;
		}
		var cmdParamObjs = document.getElementsByName("cmdParam");
		var __flag = false;
		for(var i=0;i<cmdParamObjs.length;i++){
			if(cmdParamObjs[i].checked){
				__flag = true;
			}
		}
		if(!__flag){
			alert("��ѡ��Ҫ���ݵ��������!");
			return false;
		}
	}

function displayType(param)
{
   //���ѡ������ִ�л�ƻ�ִ������
   if(1==param||0==param)
   {
      tr0.style.display="none";
   }
   
   //ѡ���Զ�ִ������
   if(2==param)
   {
      tr0.style.display="";
   }
}
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<jsp:include page="/share/selectDeviceJs.jsp">
	<jsp:param name="selectType" value="checkbox"/>
	<jsp:param name="jsFunctionName" value=""/>
</jsp:include>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="cmdUpgradeExecute.jsp" onsubmit="return CheckForm()">
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									�����������
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									��ѡ����豸�������������
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<TR>
						<TH colspan="4" align="center">
							���������
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td colspan="4">
							<div id="selectDevice"><span>������....</span></div>
						</td>
					</TR>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF" style="display:none">
												<TD align="right" width="15%">
													ҵ������:
												</TD>
												<TD width="35%">
													<select name="service_id" class="bk" readOnly>
														<option value="5">
															�������
														</option>
													</select>
												</TD>
												<TD align="right" width="15%">
													 &nbsp;
												</TD>
												<TD width="35%">
													 &nbsp;
												</TD>								
											</TR>
											<TR bgcolor="#FFFFFF">
											    <TD align="right" width="15%">
													��������
												</TD>
												<TD colspan=3>
													<div><input type="checkbox" name="cmdParam" id="cmdParam1" value="InternetGatewayDevice.Time.NTPServer1"><label for="cmdParam1">InternetGatewayDevice.Time.NTPServer1</label></div>
													<div><input type="checkbox" name="cmdParam" id="cmdParam2" value="InternetGatewayDevice.ManagementServer.PeriodicInformEnable"><label for="cmdParam2">InternetGatewayDevice.ManagementServer.PeriodicInformEnable</label></div>
													<div><input type="checkbox" name="cmdParam" id="cmdParam3" value="InternetGatewayDevice.ManagementServer.PeriodicInformInterval"><label for="cmdParam3">InternetGatewayDevice.ManagementServer.PeriodicInformInterval</label></div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													�ؼ���:
												</TD>
												<TD width="35%">
													<input type="text" name="keyword_2" maxlength=255 class=bk size=20>
												</TD>
												<TD align="right" width="15%">
													�ļ�����:
												</TD>
												<TD width="35%">
													<select name="filetype_2" class="bk" readOnly>
														<option value="1 Firmware Upgrade Image">
															1 Firmware Upgrade Image
														</option>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													�ļ�·��
												</TD>
												<TD width="" colspan="3">
													<div id="div_path" >
														<%=file_path2%>
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													�û���:
												</TD>
												<TD width="35%">
													<input type="text" name="username_2" maxlength=255 class=bk size=20>
												</TD>
												<TD align="right" width="15%">
													����:
												</TD>
												<TD width="35%">
													<input type="text" name="passwd_2" maxlength=255 class=bk size=20>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													�ļ���С:
												</TD>
												<TD width="35%">
													<input type="text" name="file_size_2" maxlength=255 class=bk size=20 readOnly>
												</TD>
												<TD align="right" width="15%">
													�ļ���:
												</TD>
												<TD width="35%">
													<input type="text" name="filename_2" maxlength=255 class=bk size=20>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													ʱ��:
												</TD>
												<TD colspan="3">
													<input type="text" name="delay_time_2" maxlength=255 class=bk size=20 value="0">
													&nbsp;&nbsp;(��λ:s)
												</TD>

											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													�ɹ�URL:
												</TD>
												<TD colspan="3">
													<input type="text" name="sucess_url_2" maxlength=255 class=bk size=80>
												</TD>

											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													ʧ��URL:
												</TD>
												<TD colspan="3">
													<input type="text" name="fail_url_2" maxlength=255 class=bk size=80>
												</TD>

											</TR>
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">

													<INPUT TYPE="reset" value=" �� �� " class=btn>
													&nbsp;
													<INPUT TYPE="hidden" name="action" value="add">
													<input type ="hidden" name="auto_type" value="1">
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
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
