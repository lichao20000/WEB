<%@ include file="../timelater.jsp"%>
<%@page import="java.util.List" %>
<jsp:useBean id="ServiceAct" scope="request"
	class="com.linkage.litms.resource.ServiceAct" />
<%@ page contentType="text/html;charset=GBK"%>

<%
List []listServiceId = ServiceAct.getServiceIdNameList(1);
 %>
<SCRIPT LANGUAGE="JavaScript">
<!--
//�鿴��ϸ��Ϣ
function detail(city_id,service_id){
	//alert(city_id + " " + service_id);
	window.open("./DeviceListByService.jsp?city_id="+city_id+"&service_id="+service_id+"&isOut=1&gw_type=1","","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}
//-->
</SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/edittable.js"></SCRIPT>
<%@ include file="../head.jsp"%>
<BR>
<BR>
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							δ��ͨ�ض�ҵ���豸ͳ��
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<%=ServiceAct.getHtmlDeviceOutService(request, 1)%>
					<TR>
						<TD class=column colspan="13" height="30">
							<a href="DeviceOutServiceState_prt.jsp?isPrt=1" alt="������ǰҳ���ݵ�Excel��">&nbsp;&nbsp;&nbsp;������Excel</a>
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