<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.common.util.DateTimeUtil"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%
request.setCharacterEncoding("GBK");

String start_time = new DateTimeUtil().getDate();
String end_time = new DateTimeUtil().getDate();

%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function delplan(plan_id){
	if(confirm("���Ҫɾ���üƻ�������\n��������ɾ���Ĳ��ָܻ�������")){
		document.all("childFrm").src="sys_planSave.jsp?plan_id=" + plan_id + "&action=delete";
		return true;
	}else{
		return false;
	}		
}
function reload(){
	this.location ="sysplan_list.jsp";
}
//-->
</SCRIPT>
<form name="frm" action="sysplan_listdata.jsp" target="childFrm" method="post">
<table width="95%" border="0" cellspacing="0" cellpadding="0" align=center>
  <tr><td HEIGHT=20>&nbsp;&nbsp;</td></tr>
  <TR><TD>
		<table width="95%" align=center  height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">
					ά���ƻ����Բ�ѯ
				</td>
			</tr>
		</table>
	</TD></TR>
	<TR><TD>
		<TABLE border=0 cellspacing=1 cellpadding=2 width="95%" align=center valign=middle bgcolor="#999999">
			<tr bgcolor="#FFFFFF">
				<TD nowrap class=column align="center">�ƻ��ƶ���</TD>
				<TD><INPUT name="acc_loginname" type="text" class="bk" 
					value="" size="25">
				</TD>
				<TD>�Ƿ�����</TD>
				<TD>
				      <INPUT name="active" type="radio" value="1">����&nbsp;&nbsp;&nbsp;&nbsp;
					  <INPUT name="active" type="radio" value="0" checked>ֹͣ
				</TD>
			</tr>	
			<tr bgcolor="#FFFFFF">
				<td nowrap class=column align="center">
					ά����Ŀ
				</td>
				<td>
					  <SELECT name="sys_item" class="bk">
						<option value="-1">==��ѡ��==</option>
						<option value="0">WEB������ά��</option>
						<option value="1">ACSά��</option>
						<option value="2">���ݿ�ά��</option>
					  </SELECT>
				</td>
				<td nowrap class=column>
					�Ƿ����
				</td>
				<td>
					<INPUT name="is_check" type="radio" value="1">���&nbsp;&nbsp;&nbsp;&nbsp;
					<INPUT name="is_check" type="radio" value="0" checked>δ���
				</td>
			</tr>	
			<tr bgcolor="#FFFFFF" class=green_foot>
				<td nowrap align=right colspan=4>
					<input type="submit" value=" �� ѯ ">
				</td>
			</tr>
		</TABLE>
	
	</TD></TR>
	<TR><TD height="20">&nbsp;
</TD></TR>
	<TR><TD>
			<div id="div_sysplan"></div>
  </TD></TR>
</TABLE>

<BR>

<iframe id="childFrm" name="childFrm" style="display:none"></iframe>
<%@ include file="../foot.jsp"%>