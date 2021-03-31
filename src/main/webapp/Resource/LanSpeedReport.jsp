<%@ include file="../timelater.jsp"%>
<%@page import="java.util.List" %>
<jsp:useBean id="LanSpeedReportAct" scope="request"
	class="com.linkage.litms.resource.LanSpeedReportAct" />
<%@ page contentType="text/html;charset=GBK"%>

<SCRIPT LANGUAGE="JavaScript">
<!--
//查看详细信息
function detail(city_id){
	window.open("./LanSpeedReportByCity.jsp?city_id="+city_id, "", "left=20,top=20,width=900,height=600,resizable=yes,scrollbars=yes");
}


//-->
</SCRIPT>
<%@ include file="../head.jsp"%>
<BR>
<BR>
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
	<tr>
  <td>
  <table width="100%" align=center  height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
			<tr>
				<td width="162" align="left" class="title_bigwhite">
					光猫协商速率不匹配月变化
				</td>
			</tr>
 </table>
  </td>
  </tr>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<%=LanSpeedReportAct.getHtmlLanSpeedChange(request)%>
					<TR>
						<TD class=column colspan="2" height="30">
							<a href="LanSpeedReportExl.jsp?isPrt=1" alt="导出当前页数据到Excel中">&nbsp;&nbsp;&nbsp;导出到Excel</a>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
<%@ include file="../foot.jsp"%>
<%
LanSpeedReportAct = null;
%>