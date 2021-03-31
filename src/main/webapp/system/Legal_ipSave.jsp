<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String strSQL = null;
String strMsg="";
String strAction = request.getParameter("action");
String legal_ip;

if(strAction.equals("delete")){	//删除操作
	legal_ip = request.getParameter("legal_ip");
	strSQL = "delete from snmp_conf_legal_ip where legal_ip='"+legal_ip+"'";
}
else{
    legal_ip = request.getParameter("legal_ip");
    String ip_pin = request.getParameter("ip_pin");
	String ip_desc = request.getParameter("ip_desc");
    if(strAction.equals("add")){	//增加操作
		strSQL = "insert into snmp_conf_legal_ip (legal_ip,ip_pin,ip_desc) values ('"+legal_ip+"','"+ip_pin+"','"+ip_desc+"')";

		strSQL = StringUtils.replace(strSQL,",,",",null,");
		strSQL = StringUtils.replace(strSQL,",,",",null,");
		strSQL = StringUtils.replace(strSQL,",)",",null)");
	}
	else{
		legal_ip = request.getParameter("legal_ip");

		strSQL = "update snmp_conf_legal_ip set ip_pin='"+ip_pin+"',ip_desc='"+ip_desc+"' where legal_ip='"+legal_ip+"'";

		strSQL = StringUtils.replace(strSQL,"=,","=null,");
		strSQL = StringUtils.replace(strSQL,"= where","=null where");
	}
}

//out.print(strSQL);
if(!strSQL.equals("")){
	int iCode = DataSetBean.executeUpdate(strSQL);
	if(iCode > 0){
		strMsg = "合法SnmpConf的客户IP操作成功！";
	}
	else{
		strMsg = "合法SnmpConf的客户IP操作失败，请返回重试或稍后再试！";
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
				<TH align="center">操作提示信息</TH>
			</TR>
				<TR  height="50">
					<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
				</TR>
			
			<TR>
				<TD class=foot align="right">
				<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('Legal_ipList.jsp')" value=" 列 表 " class=btn>
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
