<%@ include file="../timelater.jsp"%>
<%@page import="java.util.List" %>
<jsp:useBean id="ServiceSpeedAct" scope="request"
	class="com.linkage.litms.resource.ServiceSpeedAct" />
<%@ page contentType="text/html;charset=GBK"%>
<%
String InstArea=LipossGlobals.getLipossProperty("InstArea.ShortName");
int size = 0;
size = ServiceSpeedAct.getSpeedList().size() + 2;
%>
<SCRIPT LANGUAGE="JavaScript">

//查看详细信息
function detail(city_id,speed){
	//alert(city_id + " " + service_id);
	window.open("./DeviceListBySpeed.jsp?city_id="+city_id+"&gw_type=1&speed="+speed,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}

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
							按速率统计用户数量
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<%=ServiceSpeedAct.getHtmlDeviceOnSpeedService(request,1)%>
					<TR>
						<TD class=column colspan="<%=size%>" height="30">
							<a href="DeviceOnServiceSpeed_prt.jsp?isPrt=1" alt="导出当前页数据到Excel中">&nbsp;&nbsp;&nbsp;导出到Excel</a>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
<%@ include file="../foot.jsp"%>
<%
ServiceSpeedAct = null;
%>