<%@ include file="../timelater.jsp"%>
<%@ page
	import="com.linkage.litms.common.database.*,java.util.List,java.util.ArrayList"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<jsp:useBean id="ServiceSpeedAct" scope="request"
	class="com.linkage.litms.resource.ServiceSpeedAct" />
<%@ page contentType="text/html;charset=GBK"%>
<%
String InstArea=LipossGlobals.getLipossProperty("InstArea.ShortName");
int size = 0;
size = ServiceSpeedAct.getSpeedList().size() + 2;
String gbbroadband = request.getParameter("gbbroadband");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<meta http-equiv="x-ua-compatible" content="IE=7" >
<title>按速率统计用户数量结果列表</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<style>
    a{ text-decoration:none}
</style>
<script type="text/javascript">
$(function(){
	parent.dyniframesize();
	$("#QueryData",parent.document).html("");
	$('#queryButGrey',parent.document).attr("disabled", false);
});
//查看详细信息
function detail(city_id,speed){
	window.open("./DeviceListBySpeedXJ.jsp?city_id="+city_id+"&gbbroadband=<%=gbbroadband%>"+"&gw_type=1&speed="+speed,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}

</script>
</head>
<body>
	<TABLE border=1 cellspacing=0 cellpadding=2 width="100%" style="word-wrap:break-word;">
		<%=ServiceSpeedAct.getHtmlDeviceOnSpeedService(request,1)%>
		<TR>
			<TD class=column colspan="<%=size%>" height="30">
				<a href="DeviceOnServiceSpeedXJ_prt.jsp?isPrt=1" alt="导出当前页数据到Excel中">&nbsp;&nbsp;&nbsp;导出到Excel</a>
			</TD>
		</TR>
	</TABLE>
</body>
</html>
