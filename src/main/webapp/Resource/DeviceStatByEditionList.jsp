<%@ include file="../timelater.jsp"%>
<%@ page
	import="com.linkage.litms.common.database.*,java.util.List,java.util.ArrayList"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<jsp:useBean id="ServiceAct" scope="request"
	class="com.linkage.litms.resource.ServiceAct" />
<%@ page contentType="text/html;charset=GBK"%>
<%
	List cityList = new ArrayList();
	Cursor cursor = ServiceAct.getCityList(request);
	String startTime = request.getParameter("startTime");
	if (null == startTime || "null".equals(startTime)) {
		startTime = "";
	}
	String endTime = request.getParameter("endTime");
	if (null == endTime || "null".equals(endTime)) {
		endTime = "";
	}
	String start_Time = request.getParameter("start_Time");
	if (null == start_Time || "null".equals(start_Time)) {
		start_Time = "";
	}
	String end_Time = request.getParameter("end_Time");
	if (null == end_Time || "null".equals(end_Time)) {
		end_Time = "";
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
	String strVendorId = ServiceAct.getVendorHtml(vendorId,gw_type);
	String strModelId = ServiceAct.getModelHtml(vendorId, modelId,gw_type);
	String strDeviceTypeId = ServiceAct.getDeviceTypeHtml(modelId,
			deviceTypeId,gw_type);
	String is_normal=request.getParameter("is_normal");
	String is_esurfing=request.getParameter("is_esurfing");//天翼网关  jianngkun
	String isBind = request.getParameter("isBind");
	String city_id=request.getParameter("city_id");//修改处
	String rela_dev_type_id = request.getParameter("rela_dev_type_id");
	/* JLDX-REQ-20180606-JIANGHAO6-002 增加在用情况 */
	String use_state = request.getParameter("use_state");
	
	String ip_type = request.getParameter("ip_type");
	String gbbroadband = request.getParameter("gbbroadband");
	String is_speedTest = request.getParameter("is_speedTest");
	String is_ableUser = request.getParameter("is_ableUser");
	
	
	int count = 0;
	if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) && !city_id.isEmpty())
	{
		Cursor cursor_hblt = ServiceAct.getCity(city_id);
		count = cursor_hblt.getRecordSize() + 3;
	}
	else
	{
		count = cursor.getRecordSize() + 4;
	}	
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
    var deviceTypeIdSelect = $("#deviceTypeId",parent.document).val();
    if("-1"!=deviceTypeIdSelect){
    	devicetype_id=deviceTypeIdSelect;
    }
    var page = "";
   if ("-1" != deviceModelId) {
		page="DeviceListByEdition.jsp?city_id="+city_id+"&devicetype_id="+devicetype_id + "&rela_dev_type_id=" + rela_dev_type_id +"&gw_type=" + gw_type + "&vendor_id="+vendorId + "&isBind="+isBind + "&deviceModel_id="+deviceModelId+"&devicetype="+<%=devicetype%>+"&is_normal="+is_normal+"&startTime=<%=startTime%>&endTime=<%=endTime%>&sqlid=<%=sqlid%>&use_state=<%=use_state%>&citynext=<%=citynext%>&cityIdSelect=<%=cityIdSelect%>&recentStartTime=<%=recentStartTime%>&recentEndTime=<%=recentEndTime%>&ip_type=<%=ip_type%>&gbbroadband=<%=gbbroadband%>&is_speedTest=<%=is_speedTest%>&is_ableUser=<%=is_ableUser%>"
	}else{
   		page="DeviceListByEdition.jsp?city_id="+city_id+"&devicetype_id="+devicetype_id + "&rela_dev_type_id=" + rela_dev_type_id +"&gw_type=" + gw_type + "&vendor_id="+vendorId+"&is_normal="+is_normal +"&devicetype="+<%=devicetype%>+ "&isBind="+isBind +"&startTime=<%=startTime%>&endTime=<%=endTime%>&sqlid=<%=sqlid%>&use_state=<%=use_state%>&citynext=<%=citynext%>&cityIdSelect=<%=cityIdSelect%>&recentStartTime=<%=recentStartTime%>&recentEndTime=<%=recentEndTime%>&ip_type=<%=ip_type%>&gbbroadband=<%=gbbroadband%>&is_speedTest=<%=is_speedTest%>&is_ableUser=<%=is_ableUser%>";
	}
    window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}

function detailId(city_id,devicetype_id, gw_type){
	
	var page="DeviceListByEdition.jsp?city_id="+city_id+"&devicetype_id="+devicetype_id + "&gw_type=" + gw_type + "&startTime=<%=startTime%>&endTime=<%=endTime%>&use_state=<%=use_state%>";
	
	window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}

</script>
</head>
<body>
	<TABLE border=1 cellspacing=0 cellpadding=2 width="100%" style="word-wrap:break-word;">
		<%=ServiceAct.getHtmlDeviceByEdition(request)%> 
		<TR>
			<TD class=column colspan="<%=count+2%>" height="30"><a
				href="DeviceStatByEdition_prt.jsp?isPrt=1&gw_type=<%=gw_type%>&isBind=<%=isBind %>&startTime=<%=startTime%>&endTime=<%=endTime%>&vendorId=<%=vendorId%>&modelId=<%=modelId%>&deviceTypeId=<%=deviceTypeId%>&is_normal=<%=is_normal %>&rela_dev_type_id=<%=rela_dev_type_id%>&is_esurfing=<%=is_esurfing%>&city_id=<%=city_id%>&citynext=<%=citynext%>&recentStartTime=<%=recentStartTime%>&recentEndTime=<%=recentEndTime%>&sqlid=<%=sqlid%>&use_state=<%=use_state%>&ip_type=<%=ip_type%>&gbbroadband=<%=gbbroadband%>&is_speedTest=<%=is_speedTest%>&is_ableUser=<%=is_ableUser%>"
				alt="导出当前页数据到Excel中">&nbsp;&nbsp;&nbsp;导出到Excel</a></TD>
		</TR> 
	</TABLE>
</body>
</html>
