<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage" />
<%@page import="com.linkage.litms.LipossGlobals"%>

<%
	request.setCharacterEncoding("gbk");
	String file_path = versionManage.getFilePath_2("file_path", "", "0", false);
	//�������
	String opeResult = request.getParameter("operResult");
	String gwType = request.getParameter("gw_type");
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">

<SCRIPT LANGUAGE="JavaScript">
$(function(){
	gwShare_setGaoji();
});

var isOnlyOne = false;
function showChild(param){
	var page ="";
	if(param == "file_path"){
		if($("select[@name='file_path']").val() != -1){
			var file = $("select[@name='file_path']").val();
			var arrFile = file.split("\|");
			$("input[@name='file_size']").val(arrFile[2]);
			$("input[@name='filename']").val(arrFile[1]);
		}else{
			$("input[@name='file_size']").val("");
			$("input[@name='filename']").val("");
		}
	}
	var options=$("#filepath option:selected");
	var pathSelected = options.text();
	if(pathSelected!="===============��ѡ��==============="&&pathSelected!="==����û�м�¼=="){
	$("#download").css('display',''); 
	}else{
		$("#download").css('display','none'); 
	}
}
	
function CheckForm(){
	var excute_type = $("input[@name='excute_type'][@checked]").val();
    if(excute_type==2&&$("input[@name='task_name']").val()=="")
    {
       alert("����д�������ƣ�");
       $("input[@name='task_name']").focus();
       return false;
    }  
    
	var __device_id = $("input[@name='device_id']").val();
	if(__device_id == null || __device_id == ""){
		alert("���Ȳ�ѯ�豸!");
		return false;
	}
//-------------------------------------------------------------------
	if($("select[@name='file_path']").val() == -1){
		alert("��ѡ���ļ�·����");
		$("select[@name='file_path']").focus();
		return false;
	}
//	if(!IsNull(obj.file_size.value,'�ļ���С')){
//		obj.file_size.focus();
//		obj.file_size.select();
//		return false;
//	}	
	if(!IsNull($("input[@name='filename']").val(),'�ļ�����')){
		$("input[@name='filename']").focus();
		return false;
	}
	if(!IsNull($("input[@name='delay_time']").val(),'����ʱ��')){
		$("input[@name='delay_time']").focus();
		return false;
	}else{
		if(!IsNumber($("input[@name='delay_time']").val(),"����ʱ��")){
			$("input[@name='delay_time']").focus();
			return false;
		}
	}			
}

//ѡ��ĳһ�豸,���˶�Ӧ�Ļָ��ļ�
function filterByDevID(devicetype_id) {
	page = "showFilePath.jsp?device_id=" + $("input[@name='device_id']").val() + "&needFilter=true&isFromLocal=false&refresh="+Math.random();
	document.all("childFrm").src = page;
}

//filterByDevIDAndDevTypeID
function filterByDevIDAndDevTypeID(device_id, devicetype_id) {
	page = "showFilePath.jsp?device_id="+$("input[@name='device_id']").val() + "&devicetype_id="+devicetype_id + "&needFilter=true&isFromLocal=false&refresh="+Math.random();
	document.all("childFrm").src = page;
}

function displayType(param)
{
   //���ѡ������ִ�л�ƻ�ִ������
   if(1==param||0==param){
      tr0.style.display="none";
   }
   
   //ѡ���Զ�ִ������
   if(2==param){
      tr0.style.display="";
   }
}

//---------------------------------------
//�����豸ID��ʾ��������ļ��б�
//---------------------------------------
function selFileByDev (devId) {
	page = "showFilePath.jsp?device_id=" + devId + "&isFromLocal=false&refresh=" + Math.random();
	document.all("childFrm").src = page;
}

function selFileByDevs(devId,devicetype_id) {
	page = "showFilePath.jsp?device_id=" + devId 
			+ "&devicetype_id="+devicetype_id
			+ "&isFromLocal=false&refresh=" + Math.random()+"&gw_type="+<%=gwType%>;
	document.all("childFrm").src = page;
}

function deviceResult(returnVal){
	$("input[@id='download']").css("display","none");
	var gw_type = <%=gwType%>;
	
	$("tr[@id='trDeviceResult']").css("display","");

	$("td[@id='tdDeviceSn']").html("");
	$("td[@id='tdDeviceCityName']").html("");
	
	for(var i=0;i<returnVal[2].length;i++){
		$("input[@name='device_id']").val(returnVal[2][i][0]);
		$("td[@id='tdDeviceSn']").append(returnVal[2][i][1]+" -"+returnVal[2][i][2]);
		$("td[@id='tdDeviceCityName']").append(returnVal[2][i][5]);	
	}
	
	if(gw_type=='2'){
		$("tr[@id='trUserData']").show();
		var url = '<s:url value='/gwms/blocTest/QueryCustomerInfo!query.action'/>'; 
		$.post(url,{
			device_id:returnVal[2][0][0]
		},function(ajax){	
	    	$("div[@id='UserData']").html("");
			$("div[@id='UserData']").append(ajax);
		});
	}
	
	<%if("sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
		selFileByDevs(returnVal[2][0][0],returnVal[2][0][11]);
	<%}else{%>
		selFileByDev(returnVal[2][0][0]);
	<%}%>
}

function openwin() { 
	var options=$("#filepath option:selected").text();
	var n=options.lastIndexOf("/")+1;
	var name=options.substring(n);
	var dir = 2;
	<%if("sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
		dir=3;
	<%}%>
	
	window.location.href="saveFile.jsp?dir="+dir+"&name="+name+"&gwType="+<%=gwType%>;
}
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD HEIGHT=20>&nbsp;</TD></TR>
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
									�豸���ûָ�
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									�Ե����豸�������ļ���ʽ���лָ���
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<TR bgcolor="#FFFFFF" id="tr1" STYLE="display: ">
					<td colspan="4">
						<% if("4".equals(gwType)){ %>
							<%@ include file="/gtms/stb/share/gwShareDeviceQuery.jsp"%>
						<% }else{ %>
							<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
						<% } %>
					</td>
				</TR>
				<TH colspan="4" align="center">�豸����</TH>

				<tr>
					<td>
						<FORM NAME="frm" METHOD="post" ACTION="recoverSend.jsp" target="childFrm"
							onsubmit="return CheckForm()">
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF" id="trDeviceResult"
												style="display: none">
												<td nowrap align="right" class=column width="15%">
													�豸����
												</td>
												<td id="tdDeviceCityName" width="35%">
												</td>
												<td nowrap align="right" class=column width="15%">
													�豸���к� <input type="hidden" name="device_id" value="">
												</td>
												<td id="tdDeviceSn" width="35%">
												</td>

											</TR>
											<tr id="trUserData" style="display: none" bgcolor="#FFFFFF">
												<td class="colum" colspan="4">
													<div id="UserData"
														style="width: 100%; z-index: 1; top: 100px">
													</div>
												</td>
											</tr>
											<TR bgcolor="#FFFFFF" STYLE="display: none">
												<!-- <TD align="right" width="20%">
													ҵ������:
												</TD>
												<TD width="30%">
													<select name="service_id" class="bk">
														<option value="1">���ûָ�</option>
													</select>
												</TD> -->
												<TD align="right" width="20%" nowrap>
													ִ�з�ʽ:
												</TD>
												<TD colspan="3">
													<input type="radio" name="excute_type" value="0"
														onclick="displayType(this.value)" checked>
													����ִ��
													<input type="radio" name="excute_type" value="1"
														onclick="displayType(this.value)">
													�ƻ�ִ��
													<input type="radio" name="excute_type" value="2"
														onclick="displayType(this.value)">
													���Ʋ���
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr0" STYLE="display: none">
												<TD align="right" width="15%">�������ƣ�</TD>
												<TD width="35%">
													<input type=text name="task_name" value="" class="bk">
												</TD>
												<TD align="right" width="15%">����ִ��ʱ��:</TD>
												<TD width="35%">
													<select name="auto_excutetime_type" class="bk">
														<option value="1">�豸��ʼ��װ��һ������ʱ�Զ�����</option>
														<option value="2">�豸Periodic Inform�Զ�����</option>
														<option value="3">�豸��������ʱ�Զ�����</option>
														<option value="4">�豸�´����ӵ�ITMSʱ�Զ�����</option>
													</select>
												</TD>
											</TR>
											<!-- <TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">�ؼ���:</TD>
												<TD width="30%">
													<input type="text" name="keyword" maxlength=255 class=bk
														size=20>
												</TD>
												<TD align="right" width="20%">�ļ�����:</TD>
												<TD width="30%">
													<select name="filetype" class="bk" readOnly>
														<option value="3 Vendor Configuration File">
															3 Vendor Configuration File
														</option>
													</select>
												</TD>
											</TR> -->
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">�ļ�·��</TD>
												<TD width="85%" colspan="3">
													<div id="filepath"><%=file_path%></div>
												</TD>
											</TR>
											<!-- 
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													�û���:
												</TD>
												<TD width="30%">
													<input type="text" name="username" maxlength=255 class=bk
														size=20>
												</TD>
												<TD align="right" width="20%">
													����:
												</TD>
												<TD width="30%">
													<input type="text" name="passwd" maxlength=255 class=bk
														size=20>
												</TD>
											</TR>
											 -->
											<input type="hidden" name="username" maxlength=255 class=bk
												size=20>
											<input type="hidden" name="passwd" maxlength=255 class=bk
												size=20>
											<TR bgcolor="#FFFFFF">
												<!-- <TD align="right" width="20%">
													�ļ���С
												</TD>
												<TD width="30%">
													<input type="text" name="file_size" maxlength=255 class=bk
														size=20 readOnly>
												</TD> -->
												<TD align="right" width="15%">�ļ�����</TD>
												<ms:inArea areaCode="sx_lt" notInMode="false">
												<TD width="35%" colspan="3">
													<input type="text" name="filename" maxlength=255 class=bk size=100>
												</TD>
												</ms:inArea>
												<ms:inArea areaCode="sx_lt" notInMode="true">
												<TD width="35%">
													<input type="text" name="filename" maxlength=255 class=bk size=20>
												</TD>
												<TD align="right" width="15%">����ʱ��</TD>
												<TD width="35%">
													<input type="text" name="delay_time" maxlength=255 class=bk
														size=20 value="0">&nbsp;(/��)
												</TD>
												</ms:inArea>
											</TR>
											<!--  
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">�ɹ�URL:</TD>
												<TD colspan="3">
													<input type="text" name="sucess_url" class=bk size=80>
												</TD>

											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">ʧ��URL:</TD>
												<TD colspan="3">
													<input type="text" name="fail_url" class=bk size=80>
												</TD>

											</TR>
											-->
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">
													<INPUT TYPE="reset" value=" �� �� " class=jianbian>
													<INPUT TYPE="submit" value=" �� �� " class=jianbian>
												    <INPUT TYPE="button" style="display:none" id="download" onclick="openwin()" value=" �� �� " class=jianbian>
													<input type="hidden" name="file_size" class=bk>
													<input type="hidden" name="sucess_url" class=bk size=80>
													<input type="hidden" name="fail_url" class=bk size=80>
													<INPUT TYPE="hidden" name="action" value="add">
													<input type="hidden" name="auto_type" value="2">
													<input type='hidden' name='service_id' value="1">
													<input type='hidden' name='keyword' value="download_config">
													<input type='hidden' name='filetype'
														value="3 Vendor Configuration File">
													<input type="hidden" name="gw_type" value="<%=gwType%>">	
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
			<IFRAME ID=childFrm name="childFrm" SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm3 SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
