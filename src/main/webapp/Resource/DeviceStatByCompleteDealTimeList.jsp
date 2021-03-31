<%@ include file="../timelater.jsp"%>
<%@ page
	import="com.linkage.litms.common.database.*,java.util.List,java.util.ArrayList"%>
<jsp:useBean id="DeviceStatByCompleteDealTimeAct" scope="request"
	class="com.linkage.litms.resource.DeviceStatByCompleteDealTimeAct" />
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<%@ page contentType="text/html;charset=GBK"%>
<%
	String completeStartTime = request.getParameter("completeStartTime");
	if (null == completeStartTime || "null".equals(completeStartTime)) {
		completeStartTime = "";
	}
	String completeEndTime = request.getParameter("completeEndTime");
	if (null == completeEndTime || "null".equals(completeEndTime)) {
		completeEndTime = "";
	}
	String gw_type = request.getParameter("gw_type");
	int count = 0;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<meta http-equiv="x-ua-compatible" content="IE=7" >
<title>按用户上线的时间统计设备版本信息</title>
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
function detail(vendor_id,device_model_id, completeStartTime, completeEndTime, gw_type){
    var page = "";
	page="DeviceListByCompleteDealTime.jsp?vendor_id="+vendor_id+"&device_model_id="+device_model_id + "&completeStartTime=" + completeStartTime +"&completeEndTime=" + completeEndTime + "&gw_type="+gw_type;
    window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}

</script>
</head>
<body>
	<TABLE border=1 cellspacing=0 cellpadding=2 width="100%" style="word-wrap:break-word;">
		<%=DeviceStatByCompleteDealTimeAct.createResult(request)%> 
		<TR>
			<TD class=column colspan="3" height="30"><a
				href="DeviceStatByCompleteDealTime_prt.jsp?gw_type=<%=gw_type%>&completeStartTime=<%=completeStartTime %>&completeEndTime=<%=completeEndTime%>"
				alt="导出当前页数据到Excel中">&nbsp;&nbsp;&nbsp;导出到Excel</a></TD>
		</TR> 
	</TABLE>
</body>
</html>
