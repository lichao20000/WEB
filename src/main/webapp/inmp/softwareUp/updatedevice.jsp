<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="versionManage" scope="request"
	class="com.linkage.litms.software.VersionManage" />
<jsp:useBean id="sheetManage" scope="request"
	class="com.linkage.litms.netcutover.SheetManage" />
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<%@page import="java.util.HashMap"%>
<%
	request.setCharacterEncoding("GBK");
	String prot_type = request.getParameter("prot_type");
	if (prot_type == null)
		prot_type = "";
	String[] device_list = request.getParameterValues("device_id");
	//由数据库中查询获得采集点，而不是从页面中
	DeviceAct act = new DeviceAct();
	HashMap deviceInfo = act.getDeviceInfo(device_list[0]);
	String strGatherId = "";
	String devicetype_id = "";
	if (deviceInfo != null && !deviceInfo.isEmpty())
	{
		strGatherId = (String) deviceInfo.get("gather_id");
		devicetype_id = (String) deviceInfo.get("devicetype_id");
	}
	String execu_type = request.getParameter("excute_type");
	String strSql = "";
	String errorSheet = "";
	//策略定制,判断这个策略是否合法
	UserRes current_user = (UserRes) session.getAttribute("curUser");
	long user_id = current_user.getUser().getId();
	//入库成功，返回sheet数组
	//boolean result = versionManage.softup(request, devicetype_id, user_id);
	String strategyId = versionManage.softup(request, devicetype_id, user_id);
%>
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/inmp/jquery.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
	function showMsgDlg(){
		w = document.body.clientWidth;
		h = document.body.clientHeight;
	
		l = (w-250)/2;
		t = (h-60)/2;
		PendingMessage.style.left = l;
		PendingMessage.style.top  = t;
		PendingMessage.style.display="";
	}
	
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD  align="center" >
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR bgcolor="#FFFFFF" id="tr002" style="display: none">
			<td colspan="4" valign="top" class=column>
				<div id="div_strategy" style="width: 100%; z-index: 1; top: 100px">
				</div>
			</td>
		</TR>
	</TABLE>
</TD></TR>
</TABLE>
<SCRIPT LANGUAGE="JavaScript">

$("tr[@id='tr002']").show();

var strategyId = "<%=strategyId%>";

var strategyurl = "<s:url value='/inmp/servStrategy/ServStrategy!getStrategy.action'/>";

$.post(strategyurl,{
	strategyId:strategyId
},function(ajax){
	$("div[@id='div_strategy']").html("");
	$("div[@id='div_strategy']").append(ajax);
});

</SCRIPT>
<%@ include file="../toolbar.jsp"%>