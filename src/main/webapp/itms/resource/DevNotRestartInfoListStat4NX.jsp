<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<jsp:useBean id="DevBatchRestartQueryACT" scope="request" class="com.linkage.module.itms.resource.act.DevBatchRestartQueryACT" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<%
	request.setCharacterEncoding("GBK");
	String gw_type = request.getParameter("gw_type");
%>
<%if("1".equals(gw_type)){ %>
	<title>超过30天未重启的用户终端数量统计</title>
<%}else if("4".equals(gw_type)){ %>
	<title>超过30天未重启的机顶盒终端数量统计</title>
<%}%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
$(function() {
	parent.dyniframesize();
	parent.isButn(true);
});

function detail(gw_type,startOpenDate1,endOpenDate1,vendor_id,model_id,deviceType_id)
{
	var page="<s:url value='/itms/resource/DevBatchRestartQuery!detail.action'/>"
			+ "?gw_type=" + gw_type 
			+ "&startOpenDate1=" +startOpenDate1
			+ "&endOpenDate1="+endOpenDate1
			+ "&gwShare_vendorId=" + vendor_id
			+ "&gwShare_deviceModelId="+model_id
			+ "&gwShare_devicetypeId="+deviceType_id;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

function detail4NX(gw_type,startOpenDate1,endOpenDate1,vendor_id,model_id,deviceType_id,cityId)
{
	var page="<s:url value='/itms/resource/DevBatchRestartQuery!detail4NoResult.action'/>"
			+ "?gw_type=" + gw_type
			+ "&startOpenDate1=" +startOpenDate1
			+ "&endOpenDate1="+endOpenDate1
			+ "&gwShare_vendorId=" + vendor_id
			+ "&gwShare_deviceModelId="+model_id
			+ "&gwShare_devicetypeId="+deviceType_id
			+ "&city_id="+cityId;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}
</script>
</head>
<body>
	<%=DevBatchRestartQueryACT.createNotRestartResult(request)%>
</body>
</html>