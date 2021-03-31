<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="VPNAct" scope="request" class="com.linkage.litms.resource.VPNAct"/>
<%
request.setCharacterEncoding("GBK");
String strAction = request.getParameter("action");

int iCode = 0;
if(strAction!=null){
	iCode = VPNAct.deviceServCMDEdit(request);
}else
	iCode = VPNAct.deviceServCMDAdd(request);

%>
<script language="JavaScript">
<!--
var flag = '<%=iCode%>';
var strAction = '<%=strAction%>';

if(flag==0){
	if(strAction=="edit")
		alert("编辑操作成功");
	else
		alert("新增操作成功");
		
}else{
	if(strAction=="edit")
		alert("编辑操作失败");
	else
		alert("新增操作失败");
}

//parent.document.all("userinfo").innerHTML="dbUpdate";
//-->
</script>