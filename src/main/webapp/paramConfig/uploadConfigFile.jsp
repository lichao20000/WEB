<%--
FileName	: softUpgrade.jsp
Date		: 2007��5��10��
Desc		: �������.
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<jsp:useBean id="versionManage" scope="request"
	class="com.linkage.litms.software.VersionManage" />
<%
	request.setCharacterEncoding("gbk");
	String file_path = versionManage.getURLPath(2);
	String gw_type1 = request.getParameter("gw_type");
	String actionUrl = "deviceUploadFile.jsp?gw_type=" + gw_type1;
%>
<%@ include file="../head.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	//$("input[@name='gwShare_queryResultType']").val("checkbox");
	gwShare_setGaoji();
	//gwShare_setImport();
	gw_type = '<%=request.getParameter("gw_type")%>';
});

	function CheckForm(){
		//�����ļ������ļ���
		/**
		var tmp_names = all_ver_con_files.split(",");
		var tmp_filename = document.all("filename").value;
		for (var i = 0; i < tmp_names.length; i++) {
			if ((tmp_filename+".config.bin") == tmp_names[i]) {
				alert("�ļ����Ѵ���!");
				return false;
			}
		}
		*/
		/**
		if(deviceNum!="0"){
			alert("ѡ����豸����50!�����²�ѯ");
			return false;
		}
		**/
		if($("input[@name='deviceIds']").val()==""){
			alert("��ѡ���豸��");
			return false;
		}
//-------------------------------------------------------------------

		if($("select[@name='url_path']").val() == -1){
			alert("��ѡ���ļ�·����");
			return false;
		}
		if(!IsNull($("input[@name='filename']").val(),'�ļ���')){
			$("input[@name='filename']").focus();
			return false;
		}
		if(!IsNull($("input[@name='delay_time']").val(),'ʱ��')){
			$("input[@name='delay_time']").focus();
			return false;
		}else{
			if(!IsNumber($("input[@name='delay_time']").val(),"ʱ��")){
				$("input[@name='delay_time']").focus();
				return false;
			}
		}	
	}
	
	function selectChg() {
		if ($("select[@name='url_path']").val() == -1) {
			$("input[@name='dir_id']").val("");
			$("input[@name='hid_url_path']").val("");
		} else {
			var _url = $("select[@name='url_path']").val();			
			var _urlArr = _url.split("|");
			$("input[@name='dir_id']").val(_urlArr[1]);
			$("input[@name='hid_url_path']").val(_urlArr[0]);		
		}
	}

var deviceIds = "";
var deviceNum = "";	
function deviceResult(returnVal){
	deviceIds="";
	if(returnVal[0]==0){
		//$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[2].length+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";			
		}
		$("input[@name='deviceIds']").val(deviceIds);
		deviceNum = returnVal[0];
	}else{
		//$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[0]+"</strong></font>");
		$("input[@name='deviceIds']").val("0");
		$("input[@name='param']").val(returnVal[1]);
		deviceNum = returnVal[0];
	}
	
	$("tr[@id='trDeviceResult']").css("display","");

	$("td[@id='tdDeviceSn']").html("");
	$("td[@id='tdDeviceCityName']").html("");
	
	for(var i=0;i<returnVal[2].length;i++){
		$("input[@name='textDeviceId']").val(returnVal[2][i][0]);
		$("td[@id='tdDeviceSn']").append(returnVal[2][i][1]+"-"+returnVal[2][i][2]);
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
}

</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
			<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									�����ļ�����
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									ѡ����豸���������ļ����ļ���������
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<% if("4".equals(gw_type1)){ %>
							<%@ include file="/gtms/stb/share/gwShareDeviceQuery.jsp"%>
						<% }else{ %>
							<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
						<% } %>
					</td>
				</TR>
				<TR>
					<TH colspan="4" align="center">�ϴ������ļ�</TH>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post" ACTION="<%=actionUrl%>"
							target="childFrm" onsubmit="return CheckForm()">
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="param" value="" />
							<input type="hidden" name="gw_type" value="<%=gw_type1%>">
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF" id="trDeviceResult" style="display: none">
												<td nowrap align="right" class=column width="15%">
														�豸����
												</td>
												<td id="tdDeviceCityName"></td>
												<td nowrap align="right" class=column width="15%">
													�豸���к�<input type="hidden" name="textDeviceId" value="">
												</td>
												<td id="tdDeviceSn"></td>
											</TR>
											<tr id="trUserData" style="display: none" bgcolor="#FFFFFF">
												<td class="colum" colspan="4">
													<div id="UserData" style="width: 100%; z-index: 1; top: 100px"></div>
												</td>
											</tr>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">ִ�з�ʽ</TD>
												<TD width="85%" colspan="3">
													<input type="radio" name="excute_type" value="0" checked>
													����ִ��
													<input type="radio" name="excute_type" value="4">
													�´�����ϵͳ
												</TD>
											</TR>

											<!-- <TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">�ؼ���:</TD>
												<TD width="35%">
													<input type="text" name="keyword"  class=bk size=20>
												</TD>
												<TD align="right" width="15%">�ļ�����:</TD>
												<TD width="35%">
													<select name="filetype" class="bk">
														<option value="1 Vendor Configuration File">
															1 Vendor Configuration File
														</option>
													</select>
												</TD>
											</TR> -->
											<TR bgcolor="#FFFFFF">
												<TD align="right">�ļ�·��</TD>
												<TD width="" colspan="3"><%=file_path%></TD>
											</TR>
											
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">�û���</TD>
												<TD width="35%">
													<input type="text" name="username" class=bk size=20>
												</TD>
												<TD align="right" width="15%">����</TD>
												<TD width="35%">
													<input type="text" name="passwd" class=bk size=20>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right">ʱ��</TD>
												<TD>
													<input type="text" name="delay_time" class=bk size=20
														value="0">&nbsp;(��λ:��)
												</TD>
												<TD align="right">�ļ���</TD>
												<TD nowrap>
													ʱ��.�豸���к�.
													<input type="text" name="filename" class=bk size=10>
													&nbsp;.bin
												</TD>
											</TR>
											
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">
													<INPUT TYPE="reset" value=" �� �� " class=jianbian>
													&nbsp;
													<input type="hidden" name="service_id" value="2">
													&nbsp;
													<INPUT TYPE="submit" value=" �� �� " class=jianbian>
													<input type='hidden' name='dir_id' value="">
													<input type="hidden" name="hid_url_path" value="">
													<input type='hidden' name='keyword' value="upload_config">
													<input type='hidden' name='filetype'
														value="1 Vendor Configuration File">
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
			<IFRAME ID=childFrm name=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
