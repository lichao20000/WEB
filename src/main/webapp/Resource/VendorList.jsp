<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%@page import="java.util.ArrayList"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<jsp:useBean id="vendor" scope="request" class="com.linkage.litms.resource.VendorAct"/>
<%
request.setCharacterEncoding("GBK");

String strClr = "";
String strData = "";
int offset;
ArrayList list = new ArrayList();
list.clear();
list = vendor.getVendorList(request);

String stroffset = request.getParameter("offset");
int pagelen = 15;
if(stroffset == null) offset = 1;
else offset = Integer.parseInt(stroffset);

String strBar = String.valueOf(list.get(0));
Cursor cursor = (Cursor)list.get(1);
Map fields = cursor.getNext();

if(fields == null){
	strData = "<TR><TD COLSPAN=5 HEIGHT=30 class=column>��ϵͳû�г�����Դ</TD></TR>";
}
else{
	int i=1;
	while(fields != null){
		if((i%2)==0) strClr="#e7e7e7";
		else strClr = "#FFFFFF";

		strData += "<TR>";
		strData += "<TD class=column1>"+ (String)fields.get("vendor_id") + "</TD>";
		strData += "<TD class=column2>"+ (String)fields.get("vendor_name") + "</TD>";
		strData += "<TD class=column1>"+ (String)fields.get("vendor_add") + "</TD>";
		strData += "<TD class=column2>"+ (String)fields.get("telephone") + "</TD>";
		strData += "<TD class=column1 align=center><A HREF=UpdateVendorForm.jsp?vendor_id="+ (String)fields.get("vendor_id") +" onclick='return Edit(this.href);'>�༭</A> | <A HREF=VendorSave.jsp?action=delete&vendor_id="+ (String)fields.get("vendor_id") +" onclick='return delWarn();'>ɾ��</A></TD>";
		strData += "</TR>";
		i++;
		fields = cursor.getNext();
	}
	strData += "<TR><TD class=column COLSPAN=5 align=right>" + strBar + "</TD></TR>";
}

//clear
fields = null;
cursor = null;
list.clear();
strBar = null;
%>
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript">

function Add(){
  document.getElementById('add-div').style.display = "";
  document.frm.reset();
	document.frm.action.value="add";
	actLabel.innerHTML = '���';
	VendorLabel.innerHTML="";
}

</SCRIPT>

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
    <FORM NAME="frm" METHOD="post" ACTION="VendorSave.jsp" onsubmit="return CheckForm()">
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH bgcolor="#ffffff" colspan="5" >�豸����</TH>
					</TR>
					<tr CLASS=green_title2>
						<td width="20%">���̱��</td>
						<td width="20%">��������</td>
						<td width="20%">���̱���</td>
						<td width="20%">������ϵ�绰</td>
						<td width="20%">����</td>
					</tr>
					<%=strData%>
				</TABLE>
			</TD>
		</TR>

	</TABLE>
	<br>
	<TABLE id = "add-div" width="98%" border=0 cellspacing=0 cellpadding=0 align="center" style="display:none">
	  <TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center"><SPAN id="actLabel">���</SPAN><SPAN id="VendorLabel"></SPAN>�豸����</TH>
					</TR>
					<INPUT TYPE="hidden" NAME="vendor_id" maxlength=6 class=bk size=10>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >��������</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="vendor_name" maxlength=32 class=bk size=40>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >���̱���</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="vendor_add" maxlength=25 class=bk size=30>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >������ϵ�绰</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="telephone" maxlength=10 class=bk size=20></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >Ա��ID</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="staff_id" maxlength=15 class=bk size=30></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >��ע</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="remark" maxlength=60 class=bk size=80></TD>
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
<TR><TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
function Edit(page){
    //alert(page);
	document.all("childFrm").src = page;
	return false;
}

function delWarn(){
	if(confirm("���Ҫɾ���ó�����\n��������ɾ���Ĳ��ָܻ�������")){
		return true;
	}
	else{
		return false;
	}
}

function CheckForm(){
   temp =document.all("vendor_name").value;
   if(temp=="")
   {
     alert("����д�豸�������ƣ�");
     return false;
   }
   temp =document.all("vendor_add").value;
   if(temp=="")
   {
     alert("����д�豸������ϵ��ַ��");
     return false;
   }   
   return true;
}
</SCRIPT>