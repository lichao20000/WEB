<%--
FileName	: softUpgrade.jsp
Date		: 2007��5��10��
Desc		: �������.
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%
request.setCharacterEncoding("gbk");
String gwType = request.getParameter("gw_type");
%>
<%@ include file="../head.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">

$(function(){
	gwShare_setGaoji();
});
var device_id = 0;
function CheckForm(){

    var __device_id = $("input[@name='device_id']").val();

	if(__device_id == null || __device_id == ""){
		alert("���Ȳ�ѯ�豸!");
		return false;
	}
	//alert($("select[@name='goal_softwareversion']"));
	if($("select[@name='goal_softwareversion']").val() == -1){
		alert("��ѡ��汾�ļ���");
		return false;
	}
	//document.frm.action="updatedevice.jsp";
	//document.frm.submit();
	//return false;
	
	/**
	if(protType=="tr069" && !IsNull(obj.file_size_2.value,'�ļ���С')){
		obj.file_size_2.focus();
		obj.file_size_2.select();
		return false;
	}
	if(protType=="tr069" && !IsNull(obj.filename_2.value,'�ļ���')){
		obj.filename_2.focus();
		obj.filename_2.select();
		return false;
	}
	if(protType=="tr069" && !IsNull(obj.delay_time_2.value,'ʱ��')){
		obj.delay_time_2.focus();
		obj.delay_time_2.select();
		return false;
	}
	if(protType=="snmp" && !IsNull(obj.version.value,'�汾')){
		obj.version.focus();
		obj.version.select();
		return false;
	}
	**/
}

function selectGoalSoftFile(){
	var url = "showSoftwareversion.jsp";
	var goal_softvn = $("select[@name='goal_softwareversion']").val();
	//alert(goal_softvn);
	$.post(url,{
		type:2,
		goal_softwareversion:goal_softvn
	},function(msg){
		//alert(msg);
		var temp = "";
		var filename_2 = "";
		var file_size_2 ="";
		var sbf = "";
		if(msg != null && msg.indexOf("|")>-1){
			temp = msg.substring(msg.indexOf("|") + 1);
			filename_2 = temp.substring(0,temp.indexOf("|"));
			file_size_2 = temp.substring(temp.indexOf("|") + 1,temp.indexOf("'"));
		}
		document.getElementById("div_file_path").innerHTML = msg;
		document.all("file_size_2").value = file_size_2;
		document.all("filename_2").value = filename_2;
	});
}

function getValueFromList(oselect){
        var t = '';
        if(!obj){
                t = '';
        }
        if(typeof(oselect.length)=="undefined"){
                if(oselect.checked){
                        t = oselect.value;
                }
        }else{
                for(var i=0;i<oselect.length;i++){
                        if(oselect[i].checked){
                                device_id = oselect[i].value;
                                break;
                        }
                }
        }
        return t;
}

function deviceResult(returnVal){
		
	$("tr[@id='trDeviceResult']").css("display","");

	$("td[@id='tdDeviceSn']").html("");
	$("td[@id='tdDeviceCityName']").html("");
	
	$("input[@name='device_id']").val(returnVal[2][0][0]);
	$("td[@id='tdDeviceSn']").append(returnVal[2][0][1]+" -"+returnVal[2][0][2]);
	$("td[@id='tdDeviceCityName']").append(returnVal[2][0][5]);	
	
	var url = "showSoftwareversion.jsp";
	$.post(url,{
		type:1,
		device_id:returnVal[2][0][0]
	},function(msg){
		//alert(msg);
		$("div[@id='div_goal_softwareversion']").html("");
		$("div[@id='div_goal_softwareversion']").append(msg);
	});	
	
}

</SCRIPT>
<%@ include file="../toolbar.jsp"%>



<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									���������
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									&nbsp;��̨�豸�������
								</td>
							</tr>
						</table>
					</td>
				</tr>

				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</TR>

				<TR  bgcolor="#FFFFFF">
					<TD colspan="4">
						<FORM NAME="frm" METHOD="post" ACTION="updatedevice.jsp"
							onSubmit="return CheckForm();">
							<input type="hidden" name="prot_type" value="tr069" />
							<TABLE id="tb1" border=0 cellspacing=1 cellpadding=2 width="100%" bgcolor=#999999>
								<TR>
									<TH colspan="4">
										��������
									</TH>
								</TR>
								<TR bgcolor="#FFFFFF" id="trDeviceResult" style="display: none">
									<td nowrap align="right" class=column width="15%">
										�豸����
									</td>
									<td id="tdDeviceCityName" width="35%">
									</td>
									<td nowrap align="right" class=column width="15%">
										�豸���к�
										<input type="hidden" name="device_id" value="">
									</td>
									<td id="tdDeviceSn" width="35%">
									</td>

								</TR>
								<TR bgcolor="#FFFFFF">
									<TD align="right" width="15%">
										<font color="red">*</font>&nbsp;Ŀ��汾
									</TD>
									<TD width="" colspan="3">
										<div id="div_goal_softwareversion">

										</div>
									</TD>
								</TR>
								<TR id=filepath bgcolor="#FFFFFF" STYLE="display: none">
									<TD align="right" width="15%">
										�ļ�·��
									</TD>
									<TD width="" colspan="3">
										<div id="div_file_path" name="div_file_path">

										</div>
									</TD>
								</TR>
								<TR isShow="snmp" STYLE="display: none" bgcolor="#FFFFFF">
									<TD align="right" width="15%">
										�汾
									</TD>
									<TD width="" colspan="3">
										<input type="text" name="version" maxlength=255 class=bk
											size=20>
									</TD>
								</TR>
								<TR isShow="tr069" STYLE="display: none" bgcolor="#FFFFFF">
									<TD align="right" width="15%">
										�ؼ���:
									</TD>
									<TD width="35%">
										<input type="text" name="keyword_2" maxlength=255 class=bk
											size=20 value="soft">
									</TD>
									<TD align="right" width="15%">
										�ļ�����:
									</TD>
									<TD width="35%">
										<select name="filetype_2" class="bk" readOnly>
											<option value="1 Firmware Upgrade Image">
												1 Firmware Upgrade Image
											</option>
										</select>
									</TD>
								</TR>
								<TR isShow="tr069" STYLE="display: none" bgcolor="#FFFFFF">
									<TD align="right" width="15%">
										�û���:
									</TD>
									<TD width="35%">
										<input type="text" name="username_2" maxlength=255 class=bk
											size=20>
									</TD>
									<TD align="right" width="15%">
										����:
									</TD>
									<TD width="35%">
										<input type="text" name="passwd_2" maxlength=255 class=bk
											size=20>
									</TD>
								</TR>
								<TR isShow="tr069" STYLE="display: none" bgcolor="#FFFFFF">
									<TD align="right" width="15%">
										�ļ���С:
									</TD>
									<TD width="35%">
										<input type="text" name="file_size_2" maxlength=255 class=bk
											size=20 readOnly>
									</TD>
									<TD align="right" width="15%">
										�ļ���:
									</TD>
									<TD width="35%">
										<input type="text" name="filename_2" maxlength=255 class=bk
											size=20 readOnly>
									</TD>
								</TR>
								<TR isShow="tr069" STYLE="display: none" bgcolor="#FFFFFF">
									<TD align="right" width="15%">
										ʱ��:
									</TD>
									<TD colspan="3">
										<input type="text" name="delay_time_2" maxlength=255 class=bk
											size=20 value="0">
										&nbsp;&nbsp;(��λ:s)
									</TD>
								</TR>
								<TR isShow="tr069" STYLE="display: none" bgcolor="#FFFFFF">
									<TD align="right" width="15%">
										�ɹ�URL:
									</TD>
									<TD colspan="3">
										<input type="text" name="sucess_url_2" maxlength=255 class=bk
											size=80>
									</TD>
								</TR>
								<TR isShow="tr069" STYLE="display: none" bgcolor="#FFFFFF">
									<TD align="right" width="15%">
										ʧ��URL:
									</TD>
									<TD colspan="3">
										<input type="text" name="fail_url_2" maxlength=255 class=bk
											size=80>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD colspan="4" align="right" CLASS="green_foot">

										<INPUT TYPE="submit" value=" �� �� " class=jianbian>
										&nbsp;&nbsp;
										<INPUT TYPE="reset" value=" ȡ �� " class=jianbian>
										<INPUT TYPE="hidden" name="action" value="add">
										<input type="hidden" name="auto_type" value="1">
										<input type="hidden" name="service_id" value="5" />
										<input type="hidden" name="excute_type" value="0"/>
										<input type="hidden" name="gw_type" value="<%=gwType%>"/>
									</TD>
								</TR>
							</TABLE>
						</FORM>
					</TD>
				</TR>
			</table>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm3 SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>
</TABLE>


<%@ include file="../foot.jsp"%>
