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
	if(!IsNull($E("parameter").value,"��������Ϊ��!")){
		$E("parameter").focus();
		return false;
	}
	
	$E("tableView").innerHTML = "�����ϱ����ݵ���̨.....";
	return true;
}
function ClearProcess(){
	$E("tableView").innerHTML = "";
}
//�Ƿ�֪ͨ��ѡ�򴥷��¼�
function ChangeNotify(){
	var notifyWay = $E("NotifyWay");
	var isNotify = $E("isNotify");
	//��ѡ��ѡ��,֪ͨ��
	if(isNotify.checked){
		notifyWay.value = "1";
	}else{//��֪ͨ
		notifyWay.value = "0";
	}
}
//������֪ͨ��ʽ�����¼�
function ChangeNotifyWay(_value){
	var isNotify = $E("isNotify");
	//ѡ���˲�֪ͨ
	isNotify.checked = (_value != "0");
}
//�����Ƿ�֪ͨ
function ChangeAttr(_bool){
	var f = _bool ? Form.Element.enable : Form.Element.disable;
	["myTable","AttrNum"].each(f);
}
//������Ŀ�����ֵ�仯�����¼�
function ChangeAttrNum(_value){
	if(!IsNumber(_value,"������Ŀ")) return false;
	var objTab = $E("myTable");
	var _len = objTab.rows.length;
	//�������Ĳ������ڱ������������Ҫ����
	if(_value >= _len){
		CreateRows(objTab,_value - _len);
	}else{
		DelRows(objTab,_len - _value);
	}
	var checked = objTab.rows.length == 0;
	$E("attrChange").checked = !checked;
}
/**
 *��Ӷ���
 *objTable��������
 *length����Ҫ����������
 */
function CreateRows(objTable,_length){
	for(var i=0;i<_length;i++){
		AddRow(objTable);
	}
}
/**
 *ɾ������
 *objTable��������
 *length����Ҫ����������
 */
function DelRows(objTable,_length){
	for(var i=0;i<_length;i++){
		DelRow(objTable);
	}
}
/*
 *���һ��
 *objTable��������
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
 *ɾ��һ��
 *objTable��������
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
						<div align="center" class="title_bigwhite">��������</div>
						</td>
						<td><img src="../images/attention_2.gif" width="15" height="12">
						ע����<font color="#FF0000">*</font>��Ϊ��ѡ�</td>
					</tr>
				</table>
			</TD>
		</TR>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH  colspan="4" align="center"><B>�����仯�ϱ�</B></TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="10%" nowrap>����</TD>
						<TD width="40%" colspan=3>
							<INPUT TYPE="text" NAME="parameter" class="bk" style="width:350px;">&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="10%" nowrap>�Ƿ�֪ͨ</TD>
						<TD width="40%">
							<INPUT TYPE="checkbox" NAME="isNotify" id="isNotify" value="true" onclick="ChangeNotify()">
						</TD>
						<TD class=column align="right" width="10%" nowrap>֪ͨ��ʽ</TD>
						<TD width="40%">
							<SELECT NAME="NotifyWay" id="NotifyWay" onchange="ChangeNotifyWay(this.value)">
								<option value="0" >��֪ͨ</option>
								<option value="1">����֪ͨ</option>
								<option value="2">����֪ͨ</option>
							</SELECT>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="10%" nowrap>����֪ͨ</TD>
						<TD width="40%">
							<INPUT TYPE="checkbox" NAME="attrChange" id="attrChange" value="true" onclick="ChangeAttr(this.checked)">
						</TD>
						<TD class=column align="right" width="10%" nowrap>������Ŀ</TD>
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
							<INPUT TYPE="submit" name="getPara" value=" �� ȡ " class=btn>
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