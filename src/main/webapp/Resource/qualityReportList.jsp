<%@ include file="../timelater.jsp"%>
<%@ page
	import="com.linkage.litms.common.database.*,java.util.List,java.util.ArrayList"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<jsp:useBean id="qualityAct" scope="request" class="com.linkage.litms.resource.QualityServiceAction" />
<%@ page contentType="text/html;charset=GBK"%>
<%
	List cityList = new ArrayList();
	Cursor cursor = qualityAct.getCityList(request);
	
	String startTime = qualityAct.getString(request.getParameter("startTime"));
	String endTime = qualityAct.getString(request.getParameter("endTime"));
	String start_Time = qualityAct.getString(request.getParameter("start_Time"));
	String end_Time = qualityAct.getString(request.getParameter("end_Time"));

	if ("".equals(start_Time) || null == start_Time) {
		startTime = "";
	}
	if ("".equals(end_Time) || null == end_Time) {
		endTime = "";
	}
	String recentStartTime = request.getParameter("recentStartTime");
	if (null == recentStartTime || "null".equals(recentStartTime)) {
		recentStartTime = "";
	}
	String recentEndTime = request.getParameter("recentEndTime");
	if (null == recentEndTime || "null".equals(recentEndTime)) {
		recentEndTime = "";
	}
	String recent_start_Time = request.getParameter("recent_start_Time");
	if (null == recent_start_Time || "null".equals(recent_start_Time)) {
		recent_start_Time = "";
	}
	String recent_end_Time = request.getParameter("recent_end_Time");
	if (null == recent_end_Time || "null".equals(recent_end_Time)) {
		recent_end_Time = "";
	}
	
	String citynext = request.getParameter("citynext");
	String cityIdSelect=request.getParameter("city_id");//修改处
	// rela_dev_type_id
	String vendorId = request.getParameter("vendorId"); 
	//修改
	String devicetype = request.getParameter("devicetypeid"); 
	String modelId = request.getParameter("modelId");
	String deviceTypeId = request.getParameter("deviceTypeId");
	String gw_type = request.getParameter("gw_type");
	String sqlid = request.getParameter("sqlid");
	//String strVendorId = qualityAct.getVendorHtml();
	//String strModelId = qualityAct.getModelHtml(vendorId, modelId,gw_type);
	//String strDeviceTypeId = qualityAct.getDeviceTypeHtml(modelId, deviceTypeId,gw_type);
	String is_normal = request.getParameter("is_normal");
	String is_esurfing = request.getParameter("is_esurfing");//天翼网关  jianngkun
	String isBind = request.getParameter("isBind");
	String city_id = request.getParameter("city_id");//修改处
	String rela_dev_type_id = request.getParameter("rela_dev_type_id");
	/* JLDX-REQ-20180606-JIANGHAO6-002 增加在用情况 */
	String use_state = request.getParameter("use_state");
	int count = cursor.getRecordSize() + 4;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<meta http-equiv="x-ua-compatible" content="IE=7" >
<title>按版本统计结果列表</title>
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
function detail(city_id,devicetype_id, gw_type, isBind){
	var vendorId = $("#vendorId",parent.document).val();
    var rela_dev_type_id = <%=rela_dev_type_id%>;
    var deviceModelId = $("#modelId",parent.document).val();
    var devicetypeid=<%=devicetype %>;
   // var is_normal=$("select[@name='is_normal']").val();
   var is_normal=<%=is_normal%>;
   if(is_normal==null){
	   is_normal="";
   }
    var page = "";
   if ("-1" != deviceModelId) {
		page="DeviceListByEditionSpeedNoCustomer.jsp?city_id="+city_id+"&noCustomer=1&devicetype_id="+devicetype_id + "&rela_dev_type_id=" + rela_dev_type_id +"&gw_type=" + gw_type + "&vendor_id="+vendorId + "&isBind="+isBind + "&deviceModel_id="+deviceModelId+"&devicetype="+<%=devicetype%>+"&is_normal="+is_normal+"&startTime=<%=startTime%>&endTime=<%=endTime%>&sqlid=<%=sqlid%>&use_state=<%=use_state%>&citynext=<%=citynext%>&cityIdSelect=<%=cityIdSelect%>&recentStartTime=<%=recentStartTime%>&recentEndTime=<%=recentEndTime%>"
	}else{
   		page="DeviceListByEditionSpeedNoCustomer.jsp?city_id="+city_id+"&noCustomer=1&devicetype_id="+devicetype_id + "&rela_dev_type_id=" + rela_dev_type_id +"&gw_type=" + gw_type + "&vendor_id="+vendorId+"&is_normal="+is_normal +"&devicetype="+<%=devicetype%>+ "&isBind="+isBind +"&startTime=<%=startTime%>&endTime=<%=endTime%>&sqlid=<%=sqlid%>&use_state=<%=use_state%>&citynext=<%=citynext%>&cityIdSelect=<%=cityIdSelect%>&recentStartTime=<%=recentStartTime%>&recentEndTime=<%=recentEndTime%>";
	}
    window.open(page + "&quality=1","","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}


</script>
</head>
<body>
	<TABLE border=1 cellspacing=0 cellpadding=2 width="100%" style="word-wrap:break-word;">
		<%=qualityAct.getHtmlDeviceByEdition(request)%> 
	</TABLE>
</body>
</html>
