<%@ include file="../timelater.jsp"%>
<%@page import="java.util.List" %>
<jsp:useBean id="LanSpeedReportAct" scope="request"
	class="com.linkage.litms.resource.LanSpeedReportAct" />
<%@ page contentType="text/html;charset=GBK"%>

<SCRIPT LANGUAGE="JavaScript">
<!--
//�鿴��ϸ��Ϣ
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
					��èЭ�����ʲ�ƥ���±仯
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
							<a href="LanSpeedReportExl.jsp?isPrt=1" alt="������ǰҳ���ݵ�Excel��">&nbsp;&nbsp;&nbsp;������Excel</a>
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