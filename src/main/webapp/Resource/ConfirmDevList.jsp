<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<jsp:useBean id="HGWUserInfoAct" scope="request" class="com.linkage.litms.resource.HGWUserInfoAct" />
<SCRIPT LANGUAGE="JavaScript">
<!--
var flag = true;
//-->
</SCRIPT>
<%
request.setCharacterEncoding("GBK");

ArrayList list = new ArrayList();
list.clear();
//����ȷ���豸�б�
list = HGWUserInfoAct.getConfirmDevCursor(request);
//ȡ������OUI�ͳ�������Ӧ��MAP
Map ouiMap = HGWUserInfoAct.getOUIDevMap();
//ȡ�����е�ǰ�û������ض�Ӧ��MAP
Map cityMap = HGWUserInfoAct.getCityMap(request);
String strData = "";
String rtnMsg = "";
String strBar = String.valueOf(list.get(0));
Cursor cursor = (Cursor)list.get(1);
StringBuffer confirmDevLine = new StringBuffer();
StringBuffer allCDLines = new StringBuffer();

Map fields = cursor.getNext();
if(fields == null){
	strData = "<TR><TD class=column COLSPAN=8 HEIGHT=30>û�м�����ȷ���豸!</TD></TR>";
}
else{
	while(fields != null){
		strData += "<TR>";
		strData += "<TD class=column1>"+ ouiMap.get((String)fields.get("oui")) + "</TD>";
		confirmDevLine.append((String)fields.get("oui")+"^");
		strData += "<TD class=column2>"+ (String)fields.get("device_serialnumber") + "</TD>";
		confirmDevLine.append((String)fields.get("device_serialnumber")+"^");
		strData += "<TD class=column1>"+ cityMap.get((String)fields.get("city_id")) + "</TD>";
		confirmDevLine.append((String)fields.get("city_id"));
		String urlEdit = "AddConfirmDev.jsp?action=update&oui="+ (String)fields.get("oui") 
		+"&device_serialnumber="+URLEncoder.encode((String)fields.get("device_serialnumber"))
		+"&city_id="+(String)fields.get("city_id");
		
		String urlDel = "ConfirmDevSave.jsp?action=delete&oui="
			+(String)fields.get("oui")+"&device_serialnumber="+URLEncoder.encode((String)fields.get("device_serialnumber"));
//		String urlEditEncode = response.encodeURL(URLEncoder.encode(urlEdit,"GBK"));
//		String urlDelEncode = response.encodeURL(URLEncoder.encode(urlDel,"GBK"));
		strData += "<TD class=column1><A HREF="+urlEdit+">�༭</A> | <A HREF="+urlDel+ " onclick='return delWarn();'>ɾ��</A></TD>";
		strData += "</TR>";
		allCDLines.append(confirmDevLine+",");
		
		fields = cursor.getNext();
	}
	strData += "<TR><TD class=column COLSPAN=8 align=right>" + strBar + "</TD></TR>";
}

fields = null;
list.clear();
list = null;
ouiMap.clear();
ouiMap = null;
cityMap.clear();
cityMap = null;
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/edittable.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
var allCDLines = "<%= allCDLines.toString()%>"
function go(){
	v = document.all("txtpage").value;
	if(parseInt(v) && parseInt(v)>0){
		this.location = "HGWUserInfoList.jsp?offset="+ ((eval(v)-1)*15+1);
	}
}
//ɾ��ʱ����
function delWarn(){
	if(confirm("���Ҫɾ����ȷ���豸��\n��������ɾ���Ĳ��ָܻ�������")){
		return true;
	}
	else{
		return false;
	}
}

var show = 0;
//�����ض���������
function showSearch(){
	if(show == 0){
		show = 1;
		document.all("Button_1").value = "����ȷ���豸";
		document.all("searchLayer").style.display="";
	}else{
		show = 0;
		document.all("Button_1").value = "����ȷ���豸";
		document.all("searchLayer").style.display="none";
	}
}

//�鿴�û���ص���Ϣ
function GoContent(user_id, gather_id){
	var strpage="HGWUserRelatedInfo.jsp?user_id=" + user_id + "&gather_id=" + gather_id;
	window.open(strpage,"","left=20,top=20,width=800,height=600,resizable=yes,scrollbars=yes");
}
//����ض��û�ʱ��FORM
function CheckFormForm(){
	var adslflag,accountflag;
	if(KillSpace(document.frm.username.value).length != 0){
		accountflag = true;
	}
	if(KillSpace(document.frm.realname.value).length != 0){
		adslflag = true;
	}
	if(accountflag || adslflag){
		return true;
	}else{
		alert("�������ѯ����");
		document.frm.username.focus();
		document.frm.username.select();
		return false;		
	}
}

//�����ļ���EXCEL
function ToExcel() {
	var page="HGWUserInfoToExcel.jsp?title='������excel'&filename=filename";
	document.all("childFrm").src=page;
	//window.open(page);
}

//����csv�ļ�
function openUploadWin() {
//	var fvalue = document.all.file1.value;
//	fvalue = fvalue.replace(/\\/g,'/');

//	var page = "ConfirmDevList.jsp?fvalue="+fvalue;
	var page = "ConfirmDevUploadForm.jsp";
	window.open(page);
	//document.all("childFrm").src=page;
}

//����CSV�ļ�
function exportCSV() {
}
//-->
</SCRIPT>
<link rel="stylesheet" href="../css/listview.css" type="text/css">
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<tr>
	<td>
	<table width="95%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd" align="center">
		<tr>
			<td width="162" align="center" class="title_bigwhite">
				ȷ���豸��Դ
			</td>
		</tr>
	</table>
	</td>
</tr>
<TR><TD>
<FORM NAME="frm" METHOD="post" ACTION="ConfirmDevList.jsp">
<TABLE id="searchLayer" width="95%" border=0 cellspacing=0 cellpadding=0 align="center" style="display:none">
		<TR>
			<TD bgcolor=#000000>
			  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR> 
                  <TH colspan="4" align="center">��ѯ�û���Ϣ</TH>
                </TR>
                <TR bgcolor="#FFFFFF"> 
                  <TD class=column>����</TD>
                  <TD>
                    <INPUT TYPE="text" NAME="v_name" class=bk>
                  </TD>
                  <TD class=column width=180>�豸���к�</TD>
                  <TD>
                    <INPUT TYPE="text" NAME="device_serialnumber" class=bk>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF">
                  <TD align=left colspan=8> 
                    <font color="red">ע:�綼Ϊ��,�����ȫ��!</font>
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF">
                  <TD class=foot align=right colspan=8> 
                    <input type="submit" name="submit" value=" �� ѯ " class=btn>
                  </TD>
                </TR>
              </TABLE>
			</TD>
		</TR>
	</TABLE>
</FORM>
<FORM>
<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
	<TR>
		<TD bgcolor=#000000>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outTable">
				<TR>
					<TD bgcolor="#ffffff" colspan="9" >
					��ϵͳ��������ȷ���豸&nbsp;<a href="AddConfirmDev.jsp">����ȷ���豸</a>
					&nbsp;&nbsp;<input type="button" name="Button_1" value="����ȷ���豸" class="btn" onclick="showSearch()">
					</TD>
				</TR>
				<TR>
					<TH>����</TH>
					<TH>�豸���к�</TH>
					<TH>����</TH>
					<TH width=150>����</TH>
				</TR>
				<%=strData%>
				<TR>
                  <TD colspan="8" align="right" class=foot> 
					<INPUT TYPE="button" value=" �����ļ� " class=btn onclick="openUploadWin()">
					&nbsp;&nbsp;
					<INPUT TYPE="button" value=" ����Excel " class=btn onclick="initialize('outTable',1,0)">
					&nbsp;&nbsp;
					<!-- <INPUT TYPE="button" value=" ����CSV " class=btn onclick="exportCSV()">
					&nbsp;&nbsp; -->
                  </TD>
                </TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm name=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>

