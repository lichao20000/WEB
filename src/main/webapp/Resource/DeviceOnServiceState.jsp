<%@ include file="../timelater.jsp"%>
<%@page import="java.util.List" %>
<jsp:useBean id="ServiceAct" scope="request"
	class="com.linkage.litms.resource.ServiceAct" />
<%@ page contentType="text/html;charset=GBK"%>

<%
List listServiceId = ServiceAct.getServiceIdNameList(1)[0];
 %>
<SCRIPT LANGUAGE="JavaScript">
<!--
//查看详细信息
function detail(city_id,service_id){
	//alert(city_id + " " + service_id);
	window.open("./DeviceListByService.jsp?city_id="+city_id+"&gw_type=1&service_id="+service_id,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}
//-->
</SCRIPT>
<%@ include file="../head.jsp"%>
<BR>
<BR>
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							已开通特定业务设备统计
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<%=ServiceAct.getHtmlDeviceOnService(request,1)%>
					<TR>
						<TD class=column colspan="<%=listServiceId.size()+1 %>" height="30">
							<a href="DeviceOnServiceState_prt.jsp?isPrt=1" alt="导出当前页数据到Excel中">&nbsp;&nbsp;&nbsp;导出到Excel</a>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
<%@ include file="../foot.jsp"%>
<%
ServiceAct = null;
%>