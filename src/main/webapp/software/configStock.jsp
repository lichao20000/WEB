<%--
FileName	: configStock.jsp
Author		: lizhaojun
Date		: 2007��5��10��
Desc		: �汾����.
since       : V1.0, 2007��5��10��

modify record
------------------------------------------------------
desc		: modify URL, add a type para.
author		: Alex.Yan (yanhj@lianchuang.com)
version		: V1.2.0003,2007-8-30
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<jsp:useBean id="versionManage" scope="request"
	class="com.linkage.litms.software.VersionManage" />
<%
	request.setCharacterEncoding("gb2312");
	String file_path = versionManage.getFilePath_1("file_path_1");
	String file_path2 = versionManage.getVersionFilePathToConfigStock("");
	//String file_path3 = versionManage.getFilePath_2("file_path_3","","");
%>
<%@ include file="../head.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	gwShare_setGaoji();
	gwShare_setImport();
});

	function showChild(param){
		var page ="";
		if(param=="filename_1")
		{
			$("input[@name='filename_3']").val($("input[@name='filename_1']").val());
		}
		if(param=="file_path_1")
		{		  
		  var filePath=$("select[@name='file_path_1']").val();
		  if(filePath!="-1")
		  {
		     $("input[@name='file_path_3']").val($("select[@name='file_path_1']").val());
		     $("input[@name='hidden_url_path']").val(filePath.split("|")[0]+"/FILE?dir_id="+filePath.split("|")[1])
		   }
		   else
		   {
		     $("input[@name='file_path_3']").val("");
		     $("input[@name='hidden_url_path']").val("");
		   }	    
		}
		if(param == "file_path_2"){
			if($("select[@name='file_path_2']").val() != -1){
				var file = $("select[@name='file_path_2']").val();
				var arrFile = file.split("|");
				$("input[@name='file_size_2']").val(arrFile[2]);
				$("input[@name='filename_2']").val(arrFile[1]);
//				document.frm.username_2.value=arrFile[3];
//				document.frm.passwd_2.value=arrFile[4];
				
			}else{
				$("input[@name='file_size_2']").val("");
				$("input[@name='filename_2']").val("");
//				document.frm.username_2.value="";
//				document.frm.passwd_2.value="";
			}
		}		
	}
	function ShowUser(Index){
		switch(Index){
	    case '1':
	    	$("input[@name='username_3']").val($("input[@name='username_1']").val());
	    	break;
	    case '2':
	    	$("input[@name='passwd_3']").val($("input[@name='passwd_1']").val()); 
	    	break;
	    case '3':
	    	$("input[@name='username_1']").val($("input[@name='username_3']").val()); 
	    	break;
	    case '4':
	    	$("input[@name='passwd_1']").val($("input[@name='passwd_3']").val());	
	    	break;	   	
	   }   
	}
	
	function CheckForm(){
		if(deviceNum!="0"){
			alert("ѡ����豸����50!�����²�ѯ");
			return false;
		}
		if($("input[@name='deviceIds']").val()==""){
			alert("��ѡ���豸��");
			return false;
		}
		if($("select[@name='file_path_1']").val() == -1){
			alert("��ѡ�񱸷������ļ�·����");
			return false;
		}
		if(!IsNull($("input[@name='filename_1']").val(),'���������ļ�����')){
			$("input[@name='filename_1']").focus();
			return false;
		}
		if(!IsNull($("input[@name='delay_time_1']").val(),'��������ʱ��')){
			$("input[@name='delay_time_1']").focus();
			return false;
		}		
//-------------------------------------------------------------------
		if($("select[@name='file_path_2']").val() == -1){
			alert("��ѡ����������ļ�·����");
			return false;
		}
		if(!IsNull($("input[@name='file_size_2']").val(),'��������ļ���С')){
			$("input[@name='file_size_2']").focus();
			return false;
		}	
		if(!IsNull($("input[@name='filename_2']").val(),'��������ļ���')){
			$("input[@name='filename_2']").focus();
			return false;
		}
		if(!IsNull($("input[@name='delay_time_2']").val(),'�������ʱ��')){
			$("input[@name='delay_time_2']").focus();
			return false;
		}			
//-------------------------------------------------------------------

		if($("input[@name='file_path_3']").val() == -1||$("input[@name='file_path_3']").val() == ""){
			alert("��ѡ�����ûָ��ļ�·����");
			$("input[@name='file_path_3']").focus();
			return false;
		}
		/**
		if(!IsNull(obj.file_size_3.value,'���ûָ��ļ���С')){
			obj.file_size_3.focus();
			obj.file_size_3.select();
			return false;
		}	**/
		if(!IsNull($("input[@name='filename_3']").val(),'���ûָ��ļ���')){
			$("input[@name='filename_3']").focus();
			return false;
		}
		
		if($("input[@name='filename_3']").val()!=$("input[@name='filename_1']").val())
		{
		    alert("���ûָ��Ǳߵ������ļ����ƣ����������ñ����Ǳ�һ�£�");
		    $("input[@name='filename_3']").val().focus();
			return false;
		}
		if(!IsNull($("input[@name='delay_time_3']").val(),'���ûָ�ʱ��')){
			$("input[@name='delay_time_3']").focus();
			return false;
		}			
	}
	
var deviceIds = "";
var deviceNum = "";	
function deviceResult(returnVal){	
	deviceIds="";
	if(returnVal[0]==0){
		$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[2].length+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";			
		}
		$("input[@name='deviceIds']").val(deviceIds);
		deviceNum = returnVal[0];
	}else{
		$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[0]+"</strong></font>");
		$("input[@name='deviceIds']").val("0");
		$("input[@name='param']").val(returnVal[1]);
		deviceNum = returnVal[0];
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

			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" class="title_bigwhite" align="center" nowrap>
									�ļ���������
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									��ѡ����豸������������������á�
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<div id="selectDevice">
							<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
						</div>
					</td>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post" ACTION="configStock_send.jsp"
							onsubmit="return CheckForm()">
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="param" value="" />
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<div id="selectedDev">
														���ѯ�豸��
													</div>
												</td>
											</tr>
											<TR bgcolor="#FFFFFF" style="display: none">
												<TD align="right" width="15%" nowrap>
													ҵ������:
												</TD>
												<TD width="35%">
													<select name="service_id" class="bk" disabled="disabled">
														<option value="6">
															���������������
														</option>
													</select>
												</TD>
												<TD align="right" width="15%">
													ִ�з�ʽ:
												</TD>
												<TD width="35%">
													<input type="radio" name="excute_type" value="0" checked>
													����ִ��
													<input type="radio" name="excute_type" value="1">
													�ƻ�ִ��
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TH colspan="4">
													��������
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													�ؼ���:
												</TD>
												<TD width="30%">
													<input type="text" name="keyword_1" maxlength=255 class=bk
														size=20>
												</TD>
												<TD align="right" width="15%">
													�ļ�����:
												</TD>
												<TD width="30%">
													<select name="filetype_1" class="bk" disabled="disabled">
														<option value="2 Vendor Configuration File">
															2 Vendor Configuration File
														</option>
													</select>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													�ļ�·��
												</TD>
												<TD width="" colspan="3">
													<div id="path_1">
														<%=file_path%>
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													�ļ���:
												</TD>
												<TD width="30%">
													�豸ID.ʱ��.
													<input type="text" name="filename_1" maxlength=255 class=bk
														size=15 onchange="showChild('filename_1')">
													&nbsp;.bin
												</TD>
												<TD align="right" width="15%">
													ʱ��:
												</TD>
												<TD width="30%">
													<input type="text" name="delay_time_1" maxlength=255
														class=bk size=20 value="0">
													&nbsp;&nbsp;(��λ:s)
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													�û���:
												</TD>
												<TD width="30%">
													<input type="text" name="username_1" maxlength=255 class=bk
														size=20 onkeyup="ShowUser('1')">
												</TD>
												<TD align="right" width="15%">
													����:
												</TD>
												<TD width="30%">
													<input type="text" name="passwd_1" maxlength=255 class=bk
														size=20 onkeyup="ShowUser('2')">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4">
													<BR>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TH colspan="4">
													�������
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													�ؼ���:
												</TD>
												<TD width="30%">
													<input type="text" name="keyword_2" maxlength=255 class=bk
														size=20>
												</TD>
												<TD align="right" width="15%">
													�ļ�����:
												</TD>
												<TD width="30%">
													<select name="filetype_2" class="bk" disabled="disabled">
														<option value="1 Firmware Upgrade Image">
															1 Firmware Upgrade Image
														</option>
													</select>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													�ļ�·��
												</TD>
												<TD width="" colspan="3">
													<div id="path_2">
														<%=file_path2%>
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													�û���:
												</TD>
												<TD width="30%">
													<input type="text" name="username_2" maxlength=255 class=bk
														size=20>
												</TD>
												<TD align="right" width="15%">
													����:
												</TD>
												<TD width="30%">
													<input type="text" name="passwd_2" maxlength=255 class=bk
														size=20>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													�ļ���С:
												</TD>
												<TD width="30%">
													<input type="text" name="file_size_2" maxlength=255
														class=bk size=20 readOnly>
												</TD>
												<TD align="right" width="15%">
													�ļ���:
												</TD>
												<TD width="30%">
													<input type="text" name="filename_2" maxlength=255 class=bk
														size=20>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													ʱ��:
												</TD>
												<TD colspan="3">
													<input type="text" name="delay_time_2" maxlength=255
														class=bk size=20 value="0">
													&nbsp;&nbsp;(��λ:s)
												</TD>

											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													�ɹ�URL:
												</TD>
												<TD colspan="3">
													<input type="text" name="sucess_url_2" class=bk size=80>
												</TD>

											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													ʧ��URL:
												</TD>
												<TD colspan="3">
													<input type="text" name="fail_url_2" class=bk size=80>
												</TD>

											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4">
													<BR>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TH colspan="4">
													���ûָ�
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													�ؼ���:
												</TD>
												<TD width="30%">
													<input type="text" name="keyword_3" maxlength=255 class=bk
														size=20>
												</TD>
												<TD align="right" width="15%">
													�ļ�����:
												</TD>
												<TD width="30%">
													<input type="text" name="filetype_3" maxlength=255 class=bk
														size=30 value="3 Vendor Configuration File" readOnly>
													<!-- <select name="filetype_3" class="bk" readOnly>
														<option value="3 Vendor Configuration File">
															3 Vendor Configuration File
														</option> -->
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													�ļ�·��
												</TD>
												<TD width="" colspan="3">
													<input type="text" name="file_path_3" maxlength=255
														class=bk size=80 readOnly value="">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													�û���:
												</TD>
												<TD width="30%">
													<input type="text" name="username_3" maxlength=255 class=bk
														size=20>
													<!--<input type="text" name="username_3" maxlength=255 class=bk size=20 onkeyup="ShowUser('3')">-->
												</TD>
												<TD align="right" width="15%">
													����:
												</TD>
												<TD width="30%">
													<input type="text" name="passwd_3" maxlength=255 class=bk
														size=20>
													<!--<input type="text" name="passwd_3" maxlength=255 class=bk size=20 onkeyup="ShowUser('4')"> -->
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													�ļ���С:
												</TD>
												<TD width="30%">
													<input type="text" name="file_size_3" maxlength=255
														class=bk size=20>
												</TD>
												<TD align="right" width="15%">
													�ļ���:
												</TD>
												<TD width="35%">
													<input type="text" name="filename_3" maxlength=255 class=bk
														size=20 readOnly>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													ʱ��:
												</TD>
												<TD colspan="3">
													<input type="text" name="delay_time_3" maxlength=255
														class=bk size=20 value="0">
													&nbsp;&nbsp;(��λ:s)
												</TD>

											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													�ɹ�URL:
												</TD>
												<TD colspan="3">
													<input type="text" name="sucess_url_3" class=bk size=80>
												</TD>

											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													ʧ��URL:
												</TD>
												<TD colspan="3">
													<input type="text" name="fail_url_3" class=bk size=80>
												</TD>

											</TR>
											<TR>
												<TD colspan="4" align="right" class="green_foot">

													<INPUT TYPE="reset" value=" �� �� " class=btn>
													&nbsp;
													<INPUT TYPE="submit" value=" �� �� " class=btn>
													<INPUT NAME="type" TYPE="hidden" value="1">
													<input type="hidden" name="hidden_url_path" value="">
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
