<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../timelater.jsp"%>
<html>
<head>
<title>设备型号管理页面</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script language="javascript">
<!--

var sback = "<s:property value="strBack"/>";
if(sback != "")
	alert(sback);

function init(){
	DeviceTypeLabel.innerHTML = "添加";
	frm.actionType.value="add";
}

function showChild(obj,event){
	var targetObj = event.target||event.srcElement;
	frm.oui.value = targetObj.value;
}

function checkForm(){

  	frm.deviceModelName = $("[name='deviceModelName']");
	var oui = frm.oui;
	var deviceModel = frm.deviceModelName;
	if(oui.value==""){
		alert("请选择厂商");
		oui.focus();
		return false;
	}
	if(deviceModel.value==""){
		alert("请输入型号名称");
		deviceModel.focus();
		return false;
	}
	frm.submit();
}

function doEdit(s){
	var str = s.split("|")
	var element = document.getElementsByName('<s:property value="vendorAlias"/>');
	var size = element["0"].length;
	// element.options[element.selectedIndex].selected = false;
	var i = 1;
	while(size > i){
		if(str[2] == element["0"][i].value){
          	element["0"][i].selected = true;
			frm.oui.value = element["0"][i].value;
			break;
		}
		i++;
	}
	frm.deviceModelName.value = str[3];
	frm.deviceModelId.value = str[0];
	frm.actionType.value="edit";
	DeviceTypeLabel.innerHTML = "编辑[型号ID："+frm.deviceModelId.value+"]";
}

function doDel(s){
	if(window.confirm("确实要删除该条纪录吗？ 型号ID：" + s)){
		frm.deviceModelId.value = s;
		frm.actionType.value="del";
		frm.submit();
	}
}

function doAdd(){
	var element = document.getElementsByName('<s:property value="vendorAlias"/>');
	// element.options[element.selectedIndex].selected = false;
	frm.oui.value = "";
	frm.deviceModelName.value = "";
	frm.actionType.value="add";
	DeviceTypeLabel.innerHTML = "添加";
}
//翻页方法调用
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
<form name="frm" action="deviceModelInfo.action" method="post" id="frm2" onsubmit="return checkForm();">
<br>
	<table width="98%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162">
				<div align="center" class="title_bigwhite">基础资源</div>
				</td>
				<td align="right">
					<a href="#" onclick="doAdd()">增加</a>&nbsp;&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
		</table>

	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center" id="idTable">
	  <TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="5" align="center">设备型号</TH>
					</TR>
					<tr CLASS=green_title2>
						<td width="15%">型号ID</td>
						<td width="25%">厂商名称</td>
						<td width="30%">型号名称</td>
						<td width="30%">操作</td>
					</tr>
				    <s:iterator value="deviceModelList">
						<TR bgcolor="#FFFFFF">
							<td class=column1 nowrap><s:property value="modelid"/></td>
							<td class=column1 nowrap><s:property value="vendorname"/></td>
							<td class=column1 nowrap><s:property value="modelname"/></td>
							<td align="center" class=column1 nowrap>
								<a href="#" onclick="doEdit('<s:property value="modelid"/>|<s:property value="vendorname"/>|<s:property value="ouiname"/>|<s:property value="modelname"/>')">编辑</a>&nbsp;&nbsp;
								<a href="#" onclick="doDel('<s:property value="modelid"/>')">删除</a>&nbsp;&nbsp;
							</td>
						</TR>
					</s:iterator>
					<tr>
						<td align="right" CLASS=green_foot colspan="5"><%@ include file="/PageFoot.jsp"%></td>
					</tr>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
<br>
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center" id="idTable">
	  <TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center"><SPAN id="DeviceTypeLabel"></SPAN>设备型号</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >设备供应商</TD>
						<TD colspan=3><s:property value="strVendorList" escapeHtml="false"/>&nbsp;
							<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >设备型号</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="deviceModelName" maxlength=32 class=bk value="">&nbsp;
							<font color="#FF0000">*</font></TD>
					</TR>
					<TR >
						<TD colspan="4" align="right" CLASS=green_foot>
							<INPUT TYPE="submit" name = "save" value=" 保 存 " class=btn>&nbsp;&nbsp;
							<INPUT TYPE="hidden" name="actionType" value="">
							<input type="hidden" name="deviceModelId" value="">
							<input type="hidden" name="vendorAlias" value='<s:property value="vendorAlias"/>'>
							<input type="hidden" name="oui">
							<INPUT TYPE="reset" value=" 重 置 " class=btn>
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
