<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String strSQL = null;
String strMsg="";
String strAction = request.getParameter("action");
String type = request.getParameter("type");
String tablename,title;
if(type.equals("97")){
    tablename = "tab_97_legal_client";
    title = "�Ϸ�97ϵͳIP��ַ";
}
else{
    tablename = "tab_web_legal_client";
    title = "�Ϸ�WEB��IP��ַ";
}
String systemid;

if(strAction.equals("delete")){	//ɾ������
	systemid = request.getParameter("systemid");
	strSQL = "delete from "+tablename+" where systemid='"+systemid+"'";
}
else{
    systemid = request.getParameter("systemid");
    String ipaddr = request.getParameter("ipaddr");
    if(strAction.equals("add")){	//���Ӳ���
		strSQL = "insert into "+tablename+" (systemid,ipaddr) values ('"+systemid+"','"+ipaddr+"')";

		strSQL = StringUtils.replace(strSQL,",,",",null,");
		strSQL = StringUtils.replace(strSQL,",,",",null,");
		strSQL = StringUtils.replace(strSQL,",)",",null)");
	}
	else{
		strSQL = "update "+tablename+" set ipaddr='"+ipaddr+"' where systemid='"+systemid+"'";

		strSQL = StringUtils.replace(strSQL,"=,","=null,");
		strSQL = StringUtils.replace(strSQL,"= where","=null where");
	}
}

//out.print(strSQL);
if(!strSQL.equals("")){
	int iCode = DataSetBean.executeUpdate(strSQL);
	if(iCode > 0){
		strMsg = title+"�����ɹ���";
	}
	else{
		strMsg = title+"����ʧ�ܣ��뷵�����Ի��Ժ����ԣ�";
	}
}
%>
<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function GoList(page){
	this.location = page;
}

function create(){
	document.all("childFrm").src = "createCodeXML.jsp";
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR><TD valign=top>
  <TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" width="65%" align="center" ONSELECTSTART="return false;" nowrap>
	<TR height="10" nowrap><TD></TD></TR>
	<TR>
	  <TD bgcolor="#999999">
		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			<TR>
				<TH align="center">������ʾ��Ϣ</TH>
			</TR>
				<TR  height="50">
					<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
				</TR>
			
			<TR>
				<TD class=foot align="right">
				<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('Legal_clientList.jsp?type=<%=type%>')" value=" �� �� " class=btn>
				</TD>
			</TR>
		</TABLE>	  
	  </TD>
	</TR>
	<TR height="20" nowrap>
	  <TD>&nbsp;<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME></TD>
	</TR>
  </TABLE>
</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
