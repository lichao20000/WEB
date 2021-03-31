<%@ include file="../timelater.jsp"%>
<%@ page import="com.linkage.litms.common.database.*,java.util.List,java.util.ArrayList" %>
<jsp:useBean id="OnlineNetAct" scope="request"
	class="com.linkage.litms.resource.OnlineNetAct" />
<%@ page contentType="text/html;charset=GBK"%>
<%

 %>
<SCRIPT LANGUAGE="JavaScript">
<!--
function detail(city_id,devicetype_id, gw_type){
	var page="DeviceListByEdition.jsp?city_id="+city_id+"&devicetype_id="+devicetype_id + "&gw_type=2";
	window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}
//-->
</SCRIPT>
<%@ include file="../head.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							在网网元统计
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<%=OnlineNetAct.getHtmlDeviceByEdition(request)%>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR></TABLE>
<%@ include file="../foot.jsp"%>