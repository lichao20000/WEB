<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>

<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<%@page import="java.util.List"%>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<jsp:useBean id="office" scope="request" class="com.linkage.litms.resource.OfficeAct"/>
<%
request.setCharacterEncoding("GBK");
//��ȡ������Ϣ
String city_id = user.getCityId();

List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
String m_CityIdQuery = StringUtils.weave(cityIdList);
/**
String CitySQL = "select * from tab_city where city_id in( " + m_CityIdQuery + ")";

Cursor cityCursor = DataSetBean.getCursor(CitySQL);
String cityStr = FormUtil.createListBox(cityCursor, "city_id",
		"city_name", true, "", "");
*/
String strClr = "";
String strData = "";
int offset;
ArrayList list = new ArrayList();
list.clear();
list = office.getOfficeList(request);

String stroffset = request.getParameter("offset");
int pagelen = 15;
if(stroffset == null) offset = 1;
else offset = Integer.parseInt(stroffset);

String strBar = String.valueOf(list.get(0));
Cursor cursor = (Cursor)list.get(1);
Map fields = cursor.getNext();

if(fields == null){
	strData = "<TR><TD CLASS=column COLSPAN=5 HEIGHT=30>ϵͳû�о�����Դ</TD></TR>";
}
else{
	int i=1;
	while(fields != null){
		if((i%2)==0) strClr="#e7e7e7";
		else strClr = "#FFFFFF";

		strData += "<TR>";
		strData += "<TD CLASS=column1>"+ (String)fields.get("office_id") + "</TD>";
		strData += "<TD CLASS=column2>"+ (String)fields.get("office_name") + "</TD>";
		strData += "<TD CLASS=column1  align='right'>"+ (String)fields.get("staff_id") + "</TD>";
		//strData += "<TD>"+ (String)fields.get("remark") + "</TD>";
		strData += "<TD CLASS=column2  align='center'> <A HREF=UpdateOfficeForm.jsp?office_id="+ (String)fields.get("office_id") +" onclick='return Edit(this.href);'>�༭</A> | <A HREF=OfficeSave.jsp?action=delete&office_id="+ (String)fields.get("office_id") +" onclick='return delWarn();'>ɾ��</A></TD>";
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
	document.all("officeLabel").innerHTML = "";
}

function Edit(page){
	document.all("childFrm").src = page;
	return false;
}

function delWarn(){
	if(confirm("���Ҫɾ���þ�����\n��������ɾ���Ĳ��ָܻ�������")){
		return true;
	}
	else{
		return false;
	}
}

function CheckForm(){
	var obj = document.frm;
	if(!IsNull(obj.office_name.value,'��������')){
		obj.office_name.focus();
		obj.office_name.select();
		return false;
	}
//	if(obj.city_id.value == "-1")
//	{
//		alert("�û����ز���Ϊ�գ�");
//		obj.city_id.focus();
//		return false;
//	}
//	else{
//		return true;
//	}	
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
	<FORM NAME="frm" METHOD="post" ACTION="OfficeSave.jsp" onsubmit="return CheckForm()">
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" >������Դ</TH>
					</TR>
					<TR>
						<!-- <TD>�豸���</TD> -->
						<TD class=green_title2>�����ʶ</TD>
						<TD class=green_title2>��������</TD>
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
						<TH colspan="4" align="center"><SPAN id="actLabel">���</SPAN><SPAN id="officeLabel"></SPAN>������Դ</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�����ʶ</TD>
						<input type="hidden" name="office_id_old" value="">
						<TD colspan=3><INPUT TYPE="text" NAME="office_id" maxlength=20 class=bk size=20 >&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >��������</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="office_name" maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<!-- 
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" >����
							<input type="hidden" name="citylist" value=""></TD>
							<TD colspan=3></TD>
						</TR>
					-->
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >����ԱID</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="staff_id" maxlength=30 class=bk size=20 value="<%=user.getAccount()%>"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >��ע</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="remark" maxlength=50 class=bk size=50></TD>
					</TR>
					<TR>
						<TD colspan="4" align="right" class=green_foot>
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