<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.ArrayList"%>
<jsp:useBean id="zone" scope="request" class="com.linkage.litms.resource.ZoneAct"/>
<%
request.setCharacterEncoding("GBK");
String strData = "";
int offset;
ArrayList list = new ArrayList();
list.clear();
list = zone.getZoneList(request);

String stroffset = request.getParameter("offset");
int pagelen = 15;
if(stroffset == null) offset = 1;
else offset = Integer.parseInt(stroffset);

String strBar = String.valueOf(list.get(0));
String strClr = "";
Cursor cursor = (Cursor)list.get(1);
Map fields = cursor.getNext();

if(fields == null){
	strData = "<TR ><TD COLSPAN=5 HEIGHT=30 class=column>ϵͳû��С����Դ</TD></TR>";
}
else{
	int i=1;
	while(fields != null){
		if((i%2)==0) strClr="#e7e7e7";
		else strClr = "#FFFFFF";

		strData += "<TR>";
		strData += "<TD class=column1>"+ (String)fields.get("zone_id") + "</TD>";
		strData += "<TD class=column2>"+ (String)fields.get("zone_name") + "</TD>";
		strData += "<TD class=column1 align=right>"+ (String)fields.get("staff_id") + "</TD>";
		strData += "<TD class=column2 align=center><A HREF=UpdateZoneForm.jsp?zone_id="+ (String)fields.get("zone_id") +" onclick='return Edit(this.href);'>�༭</A> | <A HREF=ZoneSave.jsp?action=delete&zone_id="+ (String)fields.get("zone_id") +" onclick='return delWarn();'>ɾ��</A></TD>";
		strData += "</TR>";
		i++;
		fields = cursor.getNext();
	}
	strData += "<TR BGCOLOR=#FFFFFF><TD COLSPAN=4 align=right class=green_foot>" + strBar + "</TD></TR>";
}

//clear
fields = null;
cursor = null;
list.clear();
strBar = null;

%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" src="../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
function Add(){
	document.frm.reset();
	document.frm.action.value="add";
	actLabel.innerHTML = '���';
	document.all("zoneLabel").innerHTML = "";
}

function Edit(page){
	document.all("childFrm").src = page;
	return false;
}

function delWarn(){
	if(confirm("���Ҫɾ����С����\n��������ɾ���Ĳ��ָܻ�������")){
		return true;
	}
	else{
		return false;
	}
}

function CheckForm(){
	var obj = document.frm;
	if(!IsNull(obj.zone_name.value,'С������')){
		obj.zone_name.focus();
		obj.zone_name.select();
		return false;
	}
	else{
		return true;
	}	
}

//-->
</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<tr>
		<td>
		<table width="98%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd" align="center">
			<tr>
				<td width="162" align="center" class="title_bigwhite">
					������Դ
				</td>
				<td>
					<img src="../images/attention_2.gif" width="15" height="12">
					��'<font color="#FF0000">*</font>'�ı�������д��ѡ��
				</td>
				<td align="right">
					<A HREF='javascript:Add();'>����&nbsp;&nbsp;</A>
				</td>
			</tr>
		</table>
		</td>
	</tr>
<TR><TD>
	<FORM NAME="frm" METHOD="post" ACTION="ZoneSave.jsp" onsubmit="return CheckForm()">
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" >С����Դ</TH>
					</TR>
					<TR>
						<!-- <TD>�豸���</TD> -->
						<TD class=green_title2>С����ʶ</TD>
						<TD class=green_title2>С������</TD>
						<TD class=green_title2>����ԱID</TD>
						<TD class=green_title2>����</TD>
					</TR>
					<%=strData%>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	<BR>
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center"><SPAN id="actLabel">���</SPAN><SPAN id="zoneLabel"></SPAN>С����Դ</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">С����ʶ</TD>
						<input type="hidden" name="zone_id_old" value="">
						<TD colspan=3><INPUT TYPE="text" NAME="zone_id" maxlength=6 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >С������</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="zone_name" maxlength=30 class=bk size=20></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >����ԱID</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="staff_id" maxlength=30 class=bk size=20 value="<%=user.getAccount()%>"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >��ע</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="remark" maxlength=50 class=bk size=50></TD>
					</TR>
					<TR >
						<TD colspan="4" align="right" CLASS=green_foot>
							<INPUT TYPE="submit" value=" �� �� " class=btn>&nbsp;&nbsp;
							<INPUT TYPE="hidden" name="action" value="add">
							<INPUT TYPE="reset" value=" �� д " class=btn>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	</FORM>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
