<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../timelater.jsp"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<html>
<head>
<title>�豸�ͺŹ���ҳ��</title>

<script language="javascript">
<!--

var sback = "<s:property value="strBack"/>";
if(sback != "")
	alert(sback);

function init(){
	DeviceTypeLabel.innerHTML = "�༭";
	frm.actionType.value="edit";
}

function showChild(obj){
	frm.oui.value = frm.all(obj).value;
}

function checkForm(){
	var deviceModel = frm.deviceModelName;
	var vendorModel = frm.deviceVendorName;
	if(vendorModel.value==""){
		alert("��ѡ����");
		vendorModel.focus();
		return false;
	}
	if(deviceModel.value==""){
		alert("�������ͺ�����");
		deviceModel.focus();
		return false;
	}
	var num = $("#ethernum").val();
	var rate = $("#etherrate").val();
	var rates = rate.split(",");
	if(num != "" && rate!=""){
		if(num != rates.length){
			alert("�����������������ʸ�����һ�£�");
			return false;
		}
	}
	return true;
}

function doEdit(s){
	var str = s.split("|")
	frm.deviceVendorName.value = str[1];
	frm.deviceModelName.value = str[3];
	frm.deviceModelId.value = str[0];
	frm.ethernum.value = str[4];
	frm.etherrate.value = str[5];
	frm.actionType.value="edit";
	DeviceTypeLabel.innerHTML = "�༭[�ͺ�ID��"+frm.deviceModelId.value+"]";
}

function doDetail(modelid,ethernum,etherrate,vendorid,modelname){
	var url="<s:url value="/Resource/deviceModelInfo!getDetail.action"></s:url>"+"?etherrate="+etherrate+"&deviceModelName="+modelname+"&vendorId="+vendorid;
	//$.open(url,"740px","400px","200px","200px","true");
	window.open(url,"etherDetail","height=600,width=800,top=50,left=200,toolbar=no,menubar=no,location = no,status = yes,resizable=yes,scrollbars=yes");
}

//��ҳ��������
function doQuerySubmit(){
	frm.actionType.value="";
	frm.submit();
}

function ToExcel() {
	var page='<s:url value="/Resource/deviceModelInfo!getExcel.action"></s:url>';
	document.all("childFrm").src=page;
}
//-->
</script>

</head>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<body onload="init()">
<form name="frm" action="deviceModelInfo!editEther4jl.action" method="post" onsubmit="return checkForm();">
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
						<TH colspan="5" align="center">�豸�ͺ�</TH>
					</TR>
					<tr CLASS=green_title2>
						<td width="15%">�ͺ�ID</td>
						<td width="25%">��������</td>
						<td width="20%">�ͺ�����</td>
						<td width="20%">��������</td>
						<td width="20%">����</td>
					</tr>
				    <s:iterator value="deviceModelList">
						<TR bgcolor="#FFFFFF">
							<td class=column1 nowrap><s:property value="modelid"/></td>
							<td class=column1 nowrap><s:property value="vendorname"/></td>
							<td class=column1 nowrap><s:property value="modelname"/></td>
							<td class=column1 nowrap><s:property value="ethernum"/></td>
							<td align="center" class=column1 nowrap>
								<a href="#" onclick="doEdit('<s:property value="modelid"/>|<s:property value="vendorname"/>|<s:property value="ouiname"/>|<s:property value="modelname"/>|<s:property value="ethernum"/>|<s:property value="etherrate"/>')">�༭</a>&nbsp;&nbsp;
								<a href="#" onclick="doDetail('<s:property value="modelid"/>','<s:property value="ethernum"/>','<s:property value="etherrate"/>','<s:property value="ouiname"/>','<s:property value="modelname"/>')">����</a>&nbsp;&nbsp;
							</td>
						</TR>
					</s:iterator>
					<tr>
						<td align="right" CLASS=green_foot colspan="5"><%@ include file="/PageFoot.jsp"%></td>
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
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center" id="idTable">
	 <TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center"><SPAN id="DeviceTypeLabel"></SPAN>�豸�ͺ�</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >�豸��Ӧ��</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="deviceVendorName" maxlength=32 class=bk value="" disabled="disabled">&nbsp;
							<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >�豸�ͺ�</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="deviceModelName" maxlength=32 class=bk value="" disabled="disabled">&nbsp;
							<font color="#FF0000">*</font></TD>
					</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" >��������</TD>
							<TD colspan=3><INPUT TYPE="text" id="ethernum" name="ethernum" maxlength=20 class=bk value="">&nbsp;</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" >��������</TD>
							<TD colspan=3><textarea id="etherrate" name="etherrate" cols=70 rows=3 ></textarea>
							<font color="#FF0000">������������������(M/S)�����뱣��һ�£������磺��������Ϊ��2       ��������Ϊ��1000,1000��</font></TD>
						</TR>
					<TR >
						<TD colspan="4" align="right" CLASS=green_foot>
							<INPUT TYPE="submit" value=" �� �� " class=btn>&nbsp;&nbsp;
							<INPUT TYPE="hidden" name="actionType" value="">
							<input type="hidden" name="deviceModelId" value="">
							<input type="hidden" name="vendorAlias" value='<s:property value="vendorAlias"/>'>
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
