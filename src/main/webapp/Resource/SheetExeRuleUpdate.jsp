<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*"%>
<!--ȡ����һ��ҳ���ύ�Ĳ���-->

<%
request.setCharacterEncoding("GBK");
String task_id = request.getParameter("task_id");
String strSQL  = "select * from tab_sche_plan where task_id="+ task_id + "";
// teledb
if (DBUtil.GetDB() == 3) {
  strSQL  = "select acc_loginname, execute_time, active, plan_desc, task_id from tab_sche_plan where task_id="+ task_id + "";
}
Map fields = DataSetBean.getRecord(strSQL);
strSQL = "select * from tab_sche_sheet where  task_id="+ task_id + "";
// teledb
if (DBUtil.GetDB() == 3) {
  strSQL = "select sheet_id from tab_sche_sheet where  task_id="+ task_id + "";
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(strSQL);
psql.getSQL();
Cursor cursor = DataSetBean.getCursor(strSQL);
Map _field = cursor.getNext();

%>
<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<SCRIPT LANGUAGE="JavaScript" src="../Js/LayWriter.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
function onSelect() {
}
function CheckForm() {
	var obj = document.frm;

    if(!IsNull(obj.execute_time.value,'ִ��ʱ��')) {
		obj.execute_time.focus();
		obj.execute_time.select();
		return false;
	}
	if (obj.sheet_id.value == null || obj.sheet_id.value =="") {
		alert("��ѡ�񹤵�!");
		obj.sheet_id.focus();
		return false;
	}
	
	var delSheet = "";
	if (obj.sheet_id != null && obj.sheet_id.length>0){
		for (var i=0;i<obj.sheet_id.length;i++){
			if (!obj.sheet_id[i].selected){
				delSheet += "#" + obj.sheet_id[i].value;
			}
		}
	}
	
	obj.delSheet.value = delSheet;
	return true;
}

function OnAppointTime() {
	var winattr="center:yes;dialogWidth:800px;dialogHeight:550px;"
	var StringPara="",pos1=0,pos2=0;
	StringPara=window.showModalDialog("./RulerAppointTimeForm.jsp?"+ new Date().getTime(),"ָ��ʱ��",winattr);
	if(StringPara!=null) {
		if(StringPara!="")
		document.frm.execute_time.value=StringPara;
	}
}

function OnEditTime() {
	var winattr="center:yes;dialogWidth:800px;dialogHeight:550px;"
	var StringPara="",pos1=0,pos2=0;
	StringPara=window.showModalDialog("./RulerTimeForm.jsp?"+ new Date().getTime(),"�༭ʱ��",winattr);
	if(StringPara!=null) {
		if(StringPara!="")
		document.frm.execute_time.value=StringPara;
	}
}

function returnBrowse() {
}

//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<br>
<form action="SheetExeRuleSave.jsp" method="post" name="frm" onSubmit="return CheckForm()" target="childFrm2">
  <input type="hidden" name="delSheet" value="">
  <TABLE bgcolor="#666666" width="75%" border=0 cellspacing="1" cellpadding="0" align="center">
    <TR> 
      <TD colspan="4" class=column2> <TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
          <TR class="green_title"> 
            <TH colspan="2" align="center">�ƻ���������(��*������д)</TH>
          </TR>
        </TABLE></TD>
    </TR>
    <TR BGCOLOR=#ffffff > 
      <TD nowrap width=80  align=center class=column>�ƻ��ƶ���</TD>
      <TD colspan="3">&nbsp; <INPUT name="acc_loginname" type="text" value="<%= (String)fields.get("acc_loginname".toLowerCase())%>" size="25" readonly>
        &nbsp;<font color="#FF0000">*</font></TD>
    </TR>
    <TR BGCOLOR=#ffffff > 
      <TD  nowrap width=80 align=center class=column>�������</TD>
      <TD colspan="3">&nbsp;
            <SELECT name="sheet_id" size="6" style="width:300;height:100" multiple>
			 <%
				if(_field != null){
					while(_field != null){
			 %>
						<option value="<%= _field.get("sheet_id")%>"><%= _field.get("sheet_id")%></option>
			 <%
						_field = cursor.getNext();
					}
				}
			 %>
      </SELECT>&nbsp;<font color="#FF0000">*</font>
      </TD>
    </TR>
    <TR bgcolor=#ffffff > 
      <TD  nowrap class=column noWrap align=center> ִ��ʱ�� </TD>
      <TD colspan="3" align="left" noWrap  valign="middle">&nbsp; 
      <TEXTAREA name="execute_time" cols="65" readonly><%=(String)fields.get("execute_time".toLowerCase())%></TEXTAREA>&nbsp;&nbsp;<font color="#FF0000">*</font>
        <INPUT name="EditTime" type="button" onClick="OnEditTime();" value="�༭ʱ��" class=btn>
        <INPUT name="appointTime" type="button" onClick="OnAppointTime();" value="ָ��ʱ��" class=btn>
      </TD>
    </TR>
    <TR BGCOLOR=#ffffff> 
      <TD   nowrap align=center class=column>�Ƿ�����</TD>
      <TD colspan="3">&nbsp;&nbsp;
      ����<INPUT name="active" type="radio" value="1" <% if (((String)fields.get("active".toLowerCase())).equals("1")) {%>checked<%}%>> &nbsp;&nbsp;&nbsp;&nbsp;
      ֹͣ<INPUT name="active" type="radio" value="0" <% if (((String)fields.get("active".toLowerCase())).equals("0")) {%>checked<%}%>></TD>
    </TR>
    
    <TR bgcolor=#ffffff> 
      <TD  nowrap align=center class=column>�ƻ���������</TD>
      <TD colspan="3">&nbsp; <TEXTAREA name="plan_desc" cols="65" size="55"><%= (String)fields.get("plan_desc".toLowerCase())%></TEXTAREA>
      </TD>
    </TR>

    <TR height="23" > 
      <TD class=green_foot colspan=4 > <div align="right"> 
          <input name="submit1" type=submit class=btn value=" �� �� ">
          <INPUT TYPE="hidden" name="action" value="update">
		  <INPUT TYPE="hidden" name="task_id" value="<%=(String)fields.get("task_id".toLowerCase())%>">
          &nbsp;&nbsp;&nbsp; 
          <input name="submit2" type=reset class=btn  value=" �� �� ">
		   &nbsp;&nbsp;&nbsp; 
		  <input name="close" type=button class=btn  value="�������" onclick="javascript:window.navigate('./SheetExeRuleList.jsp');">
        </div>
      </TD>
    </TR>
    <TR BGCOLOR=#ffffff> 
      <TD colspan="4" class=column><font color=blue>ע��</font>�ƻ�����ִ��ʱ�䰴��unixϵͳCronʱ�������б���</TD>
    </TR>    
  </table>
</form>
<iframe id="childFrm2" name="childFrm2" style="display:none"></iframe>
<%@ include file="../foot.jsp"%>