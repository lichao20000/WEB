<%@ include file="../timelater.jsp"%>
<jsp:useBean id="ServiceAct" scope="request"
	class="com.linkage.litms.resource.ServiceAct" />
<%@ page contentType="text/html;charset=GBK"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
//查看详细信息
function detail(city_id,vendor_id,gw_type,isBind){
	var page="DeviceListByVendor.jsp?city_id="+city_id+"&vendor_id="+vendor_id+ "&gw_type=" + gw_type + "&isBind=" + isBind;
	//alert(page);
	window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}
function customize(vendor_id) {
	//alert(vendor_id);
	var returnObj = window.showModalDialog("customizePage.jsp?vendor_id="+ vendor_id +"&t="+new Date().getTime(),window,"status:yes;resizable:yes;edge:raised;scroll:no;help:no;center:yes;dialogHeight:300px;dialogWidth:400px");	
}
//-->
</SCRIPT>
<%@ include file="../head.jsp"%>
<TABLE boder=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							按厂商统计
						</TD>
						<td></td>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<%=ServiceAct.getHtmlDeviceByVendor(request)%>
					<TR>
						<TD class=column colspan="19" height="30">
							<a href="DeviceStatByVendor_prt.jsp?isPrt=1&gw_type=<%= request.getParameter("gw_type")%>&isBind=<%= request.getParameter("isBind")%>" alt="导出当前页数据到Excel中">&nbsp;&nbsp;&nbsp;导出到Excel</a>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR></TABLE>
<br>
<br>
<%@ include file="../foot.jsp"%>
<%
ServiceAct = null;
%>