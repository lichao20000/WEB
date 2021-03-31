<%@ include file="../timelater.jsp"%>
<meta http-equiv="refresh" content="1000">
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%
request.setCharacterEncoding("GBK");
String strData = DeviceAct.getDeviceState(request);
%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<FORM NAME="frm" METHOD="post" ACTION="DeviceSave.jsp" onsubmit="return CheckForm()">
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							设备资源
						</td>
						<td>
							<img src="../images/attention_2.gif" width="15" height="12">
							以下显示的是设备的统计信息
						</td>
					</tr>
				</table>
				</td>
			</tr>
		
			<TR>
				<TD bgcolor=#999999>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outTable">
						<TR>
							<TH width=30%>统计方式</TH>
							<TH colspan=2>统计数量</TH>
						</TR>
						<%=strData%>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</FORM>	
</TD></TR>
<TR><TD HEIGHT=20><IFRAME name=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
