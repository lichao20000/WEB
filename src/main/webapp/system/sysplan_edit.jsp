<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="systemMaintenance" scope="request" class="com.linkage.litms.system.SystemMaintenance"/>

<%
request.setCharacterEncoding("GBK");
curUser = (UserRes) session.getAttribute("curUser");
String account = curUser.getUser().getAccount();
Map fields = (Map)systemMaintenance.getDetail(request);
String sys_item = "";
String execu_time="";
String active = "";
String is_check = "";
String plan_desc = "";
String plan_id = "";
if(fields != null ){
	plan_id = (String)fields.get("plan_id");
	sys_item = (String)fields.get("sys_item");
	execu_time = (String)fields.get("execute_time");
	active = (String)fields.get("active");
	is_check = (String)fields.get("is_check");
	plan_desc = (String)fields.get("plan_desc");
}


%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" src="../Js/LayWriter.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
function CheckForm() {

	var obj = document.frm;
	if(obj.sys_item.value=="-1"){
		alert("��ѡ��ά����Ŀ��");
		obj.sys_item.focus();
		return false;
	}
    if(!IsNull(obj.execute_time.value,'ִ��ʱ��')) {
		obj.execute_time.focus();
		obj.execute_time.select();
		return false;
	}

	return true;
}

function OnEditTime() {
	var winattr="right:yes;dialogWidth:800px;dialogHeight:550px;"
	var StringPara="",pos1=0,pos2=0;
	StringPara=window.showModalDialog("./RulerTimeForm.jsp","�༭ʱ��",winattr);
	if(StringPara!=null) {
		if(StringPara!="")
		document.frm.execute_time.value=StringPara;
	}
}

function OnAppointTime() {
	var winattr="right:yes;dialogWidth:800px;dialogHeight:550px;"
	var StringPara="",pos1=0,pos2=0;
	StringPara=window.showModalDialog("./RulerAppointTimeForm.jsp","ָ��ʱ��",winattr);
	if(StringPara!=null) {
		if(StringPara!="")
		document.frm.execute_time.value=StringPara;
	}
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<FORM action="sys_planSave.jsp" method="post" name="frm" onSubmit="return CheckForm()" target="childFrm">
<TABLE border=0 cellspacing=0 cellpadding=0 width="95%">

<TR>
		<TD height="20">&nbsp;</TD>
</TR>
<TR>
		<TD>
			<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<TR>
					<TD width="164" align="center" class="title_bigwhite">
						�༭ά���ƻ�
					</TD>
					<TD>
						<img src="../images/attention_2.gif" width="15" height="12">
							�༭ϵͳ��ʱά���ƻ����ԡ�
					</TD>
				</TR>
			</TABLE>
		</TD>
</TR>
<TR><TD>
  <TABLE bgcolor="#666666" width="100%" border=0 cellspacing="1" cellpadding="2">

	<TR BGCOLOR=#ffffff > 
      <TH colspan="4">�ƻ�������ϸ��Ϣ</TH>
    </TR>
	<TR BGCOLOR=#ffffff > 
      <TD width=80  align=right class=column nowrap>�ƻ��ƶ���</TD>
      <TD colspan="3"><INPUT name="acc_loginname" type="text" class="bk" value="<%= account%>" size="25" readonly>
        &nbsp;<font color="#FF0000">*</font></TD>
    </TR>
    <%
    	String  _select0 = "";
    	String  _select1 = "";
    	String  _select2 = "";
    	if(sys_item.equals("0")){
    		 _select0 = "selected";
    	} else if(sys_item.equals("1")){
    		 _select1 = "selected";
    	} else {
    		 _select2 = "selected";
    	}
    
    %>
    <TR BGCOLOR=#ffffff > 
      <TD width=80 align=right class=column>ά����Ŀ</TD>
      <TD colspan="3">
      <SELECT name="sys_item" class="bk">
				<option value="-1">==��ѡ��==</option>
				<option value="0" <%=_select0%>>WEB������ά��</option>
				<option value="1" <%=_select1%>>ACSά��</option>
				<option value="2" <%=_select2%>>���ݿ�ά��</option>
      </SELECT>&nbsp;<font color="#FF0000">*</font>

    </TR>
    <TR bgcolor=#ffffff > 
      <TD class=column noWrap align=right> ִ��ʱ�� </TD>
      <TD colspan="3" align="left" noWrap  valign="middle"> 
      <TEXTAREA name="execute_time" cols="65" readonly class="bk"><%=execu_time%></TEXTAREA>&nbsp;&nbsp;<font color="#FF0000">*</font>
        <INPUT name="EditTime" type="button" onClick="OnEditTime();" value="�༭ʱ��" class=btn>
        <INPUT name="appointTime" type="button" onClick="OnAppointTime();" value="ָ��ʱ��" class=btn>
      </TD>
    </TR>
    <TR BGCOLOR=#ffffff> 
      <TD  align=right class=column>�Ƿ�����</TD>
      <%
      	String check_1 = "";
      	String check_2 = "";
      	if(active.equals("1")){
      		check_1 = "checked";
     		} else {
      		check_2 ="checked";
      	}
      
      	String _check_1 = "";
      	String _check_2 = "";
      	
      	if(is_check.equals("1")){
      		_check_1 = "checked";
      	} else {
      		_check_2 = "checked";
      	}
      %>
      <TD colspan=""> 
        <INPUT name="active" type="radio" value="1" <%=check_1%>>����&nbsp;&nbsp;&nbsp;&nbsp;
        <INPUT name="active" type="radio" value="0" <%=check_2%>>ֹͣ</TD>
      <TD  align=right class=column>�Ƿ����</TD>
      <TD colspan=""> 
        <INPUT name="is_check" type="radio" value="1" <%=_check_1%>>��� &nbsp;&nbsp;&nbsp;&nbsp;
        <INPUT name="is_check" type="radio" value="0" <%=_check_2%>>δ���</TD>
    </TR>
    
    <TR bgcolor=#ffffff> 
      <TD align=right class=column>�ƻ���������</TD>
      <TD colspan="3"> <TEXTAREA name="plan_desc" class="bk" cols="65" size="55"><%=plan_desc%></TEXTAREA>
      </TD>
    </TR>

    <TR height="23" > 
      <TD class=green_foot colspan=4 > <div align="right"> 
          <input name="submit1" type=submit class=btn value=" �� �� ">
          <INPUT TYPE="hidden" name="action" value="update">
          &nbsp;&nbsp;&nbsp; 
          <input type="hidden" name="plan_id" value="<%=plan_id%>">
          <input name="submit2" type=reset class=btn  value=" �� �� ">
		   &nbsp;&nbsp;&nbsp; 
		  <input name="close" type=button class=btn  value="�������" onclick="javascript:window.navigate('sysplan_list.jsp');">
        </div>
      </TD>
    </TR>
  </TABLE>
</TD>
</TR>
</TABLE>
</FORM>
<iframe id="childFrm" name="childFrm" align="right" style="display:none"></iframe>
<%@ include file="../foot.jsp"%>