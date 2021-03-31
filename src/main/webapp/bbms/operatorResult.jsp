<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>客户资料录入/编辑结果信息界面</title>
<%
	/**
		 * 客户资料录入/编辑结果信息界面
		 * 
		 * @author 陈仲民(5243)
		 * @version 1.0
		 * @since 2008-06-04
		 * @category
		 */
%>
<link href="<s:url value='/css/css_green.css'/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript">
var showtype = "<s:property value='showtype'/>";

if (showtype == '1'){
	var customer_id = "<s:property value='customer_id'/>";
	//window.location.replace("EGWUserInfoList.jsp?customer_id="+customer_id);
}

function returnAdd(){
	window.location.replace("CustomerInfo!addForm.action");
}
function returnList(){
	window.location.replace("customerFrame.jsp");
}
</script>
</head>

<body>

<TABLE border=0 cellspacing=0 cellpadding=0 width="80%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">操作结果</TH>
					</TR>
						<TR  height="80">
							<TD align="center" valign="middle" class="column"><s:property value="msg"/></TD>
						</TR>
					<TR>
						<TD class=green_foot align="right">
                        <INPUT TYPE="button" NAME="cmdJump" onclick="returnAdd();" value="继续添加" class=btn >
						<INPUT TYPE="button" NAME="cmdBack" onclick="returnList();" value="客户列表" class=btn>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
</body>
</html>