<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.util.CommonMap" %>
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>
<%@ page import="com.linkage.litms.common.database.Cursor" %>
<%@ page import="java.util.Map" %>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />

<%
String gatherList = deviceAct.getGatherList(session, "", "gather_id", true);

%>


<%@ include file="../head.jsp"%>
<form name="form1" action="access_submit.jsp" method="post">
<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
	<TR><TD HEIGHT="20">&nbsp;</TD></TR>
	<TR>
			<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							认证方式
						</TD>
					</TR>
				</TABLE>
			</TD>
	</TR>
	<TR>
		<TD>
			<TABLE width="100%" border=0 cellspacing=1 cellpadding=2 bgcolor=#999999>
				<TR>
					<!-- <TD width="10%" class=column>采集点</TD>
					<TD width="20%" class=column>
						<%//=gatherList %>
					</TD> -->
					<TD class=column width="50%">
						<input type="radio" name="isAccess" value="-1">不认证
						<input type="radio" name="isAccess" value="0" checked>Basic认证
						<input type="radio" name="isAccess" value="1">Digest认证
					</TD>
				</TR>
				<TR> 
					<TD colspan="1" class="green_foot" align="right"><input type="button" onclick="checkForm()" value=" 提 交 " class="btn"></TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
</TABLE>


<script language="javascript">
function showChild(parname){
	if(parname == "gather_id")
	{
		page = "getAccess.jsp?gather_id="+document.form1.gather_id.value;
		document.all("childFrm").src = page;
	}
}

function checkForm(){
	//if (document.form1.gather_id.value == "-1"){
	//	alert('请选择采集点！');
	//	return false;
	//}
	document.form1.submit();
	
}

</script>

<%@ include file="../foot.jsp"%>