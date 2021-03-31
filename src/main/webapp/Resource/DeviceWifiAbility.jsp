<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../timelater.jsp"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<html>
<head>
<title>�豸WIFI��������ҳ��</title>

<script language="javascript">
<!--

var sback = "<s:property value="strBack"/>";
if(sback != "")
	alert(sback);

function init(){
	DeviceTypeLabel.innerHTML = "";
	frm.actionType.value="";
}

function showChild(obj){
	frm.oui.value = frm.all(obj).value;
}

function checkForm(){
	var oui = frm.oui;
	var deviceModel = frm.deviceModelName;
	if(oui.value==""){
		alert("��ѡ����");
		oui.focus();
		return false;
	}
	if(deviceModel.value==""){
		alert("�������ͺ�����");
		deviceModel.focus();
		return false;
	}
	return true;
}

function doEdit(s){
	var editTable= document.getElementById("editTable");
	editTable.style.display="block"
	var str = s.split("|")
	var element = frm.all('<s:property value="vendorAlias"/>');
	var size = element.length;
	element.options[element.selectedIndex].selected = false;
	var i = 1;
	while(size > i){
		if(str[2] == element.options[i].value){
			element.options[i].selected = true;
			frm.oui.value = element.value;
			break;
		}
		i++;
	}
	frm.deviceModelName.value = str[3];
	var wifi_ability = str[4];
	if (wifi_ability == 0) {
		document.getElementsByName("wifi_ability")[0].checked = "checked";
	} else if (wifi_ability == 1) {
		document.getElementsByName("wifi_ability")[1].checked = "checked";
	} else if (wifi_ability == 2) {
		document.getElementsByName("wifi_ability")[2].checked = "checked";
	} else  if (wifi_ability == 3){
		document.getElementsByName("wifi_ability")[3].checked = "checked";
	}else{
		document.getElementsByName("wifi_ability")[4].checked = "checked";
	}
	frm.deviceModelId.value = str[0];
	frm.actionType.value="edit";

	DeviceTypeLabel.innerHTML = "�༭[�ͺ�ID��"+frm.deviceModelId.value+"]";
}

function doDel(s){
	if(window.confirm("ȷʵҪɾ��������¼�� �ͺ�ID��" + s)){
		frm.deviceModelId.value = s;
		frm.actionType.value="del";
		frm.submit();
	}
}

function ToExcel() {
	var page='<s:url value="/Resource/deviceModelWifi!getExcel.action"></s:url>';
	document.all("childFrm").src=page;
}
//��ҳ��������
function doQuerySubmit(){
	frm.actionType.value="";
	frm.submit();
}
//-->
</script>

</head>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<body onload="init()">
<form name="frm" action="deviceModelWifi.action" method="post" onsubmit="return checkForm();">
<br>
	<table width="98%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162">
				<div align="center" class="title_bigwhite">������Դ</div>
				</td>
				<td align="right">
					<a href="#" onclick="ToExcel()">����</a>&nbsp;&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
		</table>

	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center" id="idTable">
	  <TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="6" align="center">�豸�ͺ�</TH>
					</TR>
					<tr CLASS=green_title2>
						<td width="10%">�ͺ�ID</td>
						<td width="20%">��������</td>
						<td width="20%">�ͺ�����</td>
						<td width="20%">WIFI�����ȼ�</td>
						<td width="20%">WIFI��������</td>
						<td width="10%">����</td>
					</tr>
				    <s:iterator value="deviceModelList">
						<TR bgcolor="#FFFFFF">
							<td class=column1 nowrap><s:property value="modelid"/></td>
							<td class=column1 nowrap><s:property value="vendorname"/></td>
							<td class=column1 nowrap><s:property value="modelname"/></td>
							<td class=column1 nowrap><s:property value="wifiability"/></td>
							<td class=column1 nowrap><s:property value="wifiabilitydesc"/></td>
							<td align="center" class=column1 nowrap>
								<a href="#" onclick="doEdit('<s:property value="modelid"/>|<s:property value="vendorname"/>|<s:property value="ouiname"/>|<s:property value="modelname"/>|<s:property value="wifiability"/>')">�༭</a>&nbsp;&nbsp;
							</td>
						</TR>
					</s:iterator>
					<tr>
						<td align="right" CLASS=green_foot colspan="6"><%@ include file="/PageFoot.jsp"%></td>
					</tr>

				</TABLE>
			</TD>
		</TR>
		<tr STYLE="display: none">
			<td>
				<iframe id="childFrm" src=""></iframe>
			</td>
		</tr>
	</TABLE>
<br>
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center" id="editTable" style="display: none">
	  <TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center"><SPAN id="DeviceTypeLabel"></SPAN>�豸�ͺ�</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >�豸��Ӧ��</TD>
						<TD colspan=3 disabled="disabled"><s:property value="strVendorList" escapeHtml="false"  />&nbsp;
							<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >�豸�ͺ�</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="deviceModelName" maxlength=32 class=bk value="" disabled="disabled">&nbsp;
							<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">wifi����</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="wifi_ability" checked="checked" value="0" >��
							<input type="radio" width="23%" name="wifi_ability" value="1" >802.11b
							<input type="radio" width="23%" name="wifi_ability" value="2" >802.11b/g
							<input type="radio" width="23%" name="wifi_ability" value="3" >802.11b/g/n
							<input type="radio" width="23%" name="wifi_ability" value="4" >802.11b/g/n/ac
						</TD>
					</TR>
					<TR >
						<TD colspan="4" align="right" CLASS=green_foot>
							<INPUT TYPE="submit" value=" �� �� " class=btn>&nbsp;&nbsp;
							<INPUT TYPE="hidden" name="actionType" value="">
							<input type="hidden" name="deviceModelId" value="">
							<input type="hidden" name="vendorAlias" value='<s:property value="vendorAlias"/>'>
							<input type="hidden" name="oui">
							<INPUT TYPE="reset" value=" �� д " class=btn>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</form>
<%@ include file="../foot.jsp"%>
</body>
</html>
