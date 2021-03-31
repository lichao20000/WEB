<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
var $E = document.getElementById;
function CheckForm(){
	if(!IsNull($E("parameter").value,"参数不能为空!")){
		$E("parameter").focus();
		return false;
	}
	
	$E("tableView").innerHTML = "正在上报数据到后台.....";
	return true;
}
function ClearProcess(){
	$E("tableView").innerHTML = "";
}
//是否通知复选框触发事件
function ChangeNotify(){
	var notifyWay = $E("NotifyWay");
	var isNotify = $E("isNotify");
	//复选框选中,通知。
	if(isNotify.checked){
		notifyWay.value = "1";
	}else{//不通知
		notifyWay.value = "0";
	}
}
//下拉框通知方式触发事件
function ChangeNotifyWay(_value){
	var isNotify = $E("isNotify");
	//选择了不通知
	isNotify.checked = (_value != "0");
}
//属性是否通知
function ChangeAttr(_bool){
	var f = _bool ? Form.Element.enable : Form.Element.disable;
	["myTable","AttrNum"].each(f);
}
//属性数目输入框值变化触发事件
function ChangeAttrNum(_value){
	if(!IsNumber(_value,"属性数目")) return false;
	var objTab = $E("myTable");
	var _len = objTab.rows.length;
	//如果输入的参数大于表格行数，则需要增加
	if(_value >= _len){
		CreateRows(objTab,_value - _len);
	}else{
		DelRows(objTab,_len - _value);
	}
	var checked = objTab.rows.length == 0;
	$E("attrChange").checked = !checked;
}
/**
 *添加多行
 *objTable：表格对象
 *length：需要创建的行数
 */
function CreateRows(objTable,_length){
	for(var i=0;i<_length;i++){
		AddRow(objTable);
	}
}
/**
 *删除多行
 *objTable：表格对象
 *length：需要创建的行数
 */
function DelRows(objTable,_length){
	for(var i=0;i<_length;i++){
		DelRow(objTable);
	}
}
/*
 *添加一行
 *objTable：表格对象
 */
function AddRow(objTable){
	var oRow = objTable.insertRow();
	oRow.bgColor = "#FFFFFF";
	oRow.align = "left";
	var index = objTable.rows.length;
	var oCell = oRow.insertCell();
	oCell.width = 40;
	oCell.align = "right";
	oCell.innerHTML = index;
	oCell = oRow.insertCell();
	oCell.innerHTML = "<input type=\"text\" readonly value=\"Subscriber\" name=\"attrName\" style=\" width='80%' \">";
}
/*
 *删除一行
 *objTable：表格对象
 */
function DelRow(objTable){
	objTable.deleteRow();
}
//-->
</SCRIPT>
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR>
	<TD valign=top>
	<FORM NAME="frm" METHOD="post" ACTION="ParamChangeConfigSubmit.jsp" onsubmit="return CheckForm()" target="childFrm">
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">参数监视</div>
						</td>
						<td><img src="../images/attention_2.gif" width="15" height="12">
						注：带<font color="#FF0000">*</font>号为必选项。</td>
					</tr>
				</table>
			</TD>
		</TR>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH  colspan="4" align="center"><B>参数变化上报</B></TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="10%" nowrap>参数</TD>
						<TD width="40%" colspan=3>
							<INPUT TYPE="text" NAME="parameter" class="bk" style="width:350px;">&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="10%" nowrap>是否通知</TD>
						<TD width="40%">
							<INPUT TYPE="checkbox" NAME="isNotify" id="isNotify" value="true" onclick="ChangeNotify()">
						</TD>
						<TD class=column align="right" width="10%" nowrap>通知方式</TD>
						<TD width="40%">
							<SELECT NAME="NotifyWay" id="NotifyWay" onchange="ChangeNotifyWay(this.value)">
								<option value="0" >不通知</option>
								<option value="1">被动通知</option>
								<option value="2">主动通知</option>
							</SELECT>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="10%" nowrap>属性通知</TD>
						<TD width="40%">
							<INPUT TYPE="checkbox" NAME="attrChange" id="attrChange" value="true" onclick="ChangeAttr(this.checked)">
						</TD>
						<TD class=column align="right" width="10%" nowrap>属性数目</TD>
						<TD width="40%">
							<INPUT TYPE="text" NAME="AttrNum" id="AttrNum" value="0" onchange="ChangeAttrNum(this.value)">
						</TD>						
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD width="10%" nowrap colspan=4>
							<table id="myTable" width="100%" bgcolor="#999999" border=0 cellspacing=1 cellpadding=2>
							</table>
						</TD>
					</TR>
					<TR >
						<TD colspan="4" align="right" class=foot>
							<INPUT TYPE="submit" name="getPara" value=" 获 取 " class=btn>
							<INPUT TYPE="hidden" name="device_id" value="<%=device_id%>">
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR><TD>
		<DIV id="tableView" style="overflow:auto;display:none;" align="center"></DIV>
		</TD></TR>
	</TABLE>
	</FORM>
	</TD>
</TR>
</TABLE>
<IFRAME id="childFrm" name="childFrm" align="center" style="display:none"></IFRAME>