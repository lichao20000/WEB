<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.text.DecimalFormat"%>
<jsp:useBean id="umd" scope="request" class="com.linkage.litms.flux.UserManagerDev" />
<%@ page import="com.linkage.litms.common.chart.TimeSeriesChart" %>
<%@ page import="java.io.PrintWriter" %>
<%@ include file="../head.jsp"%>

<%
	request.setCharacterEncoding("GBK");
	String type = request.getParameter("type");
	String title = null;

	if (type.equals("ifinoctetsbps")) {
		title = "流入速率";
	}
	else if (type.equals("ifindiscardspps")) {
		title = "流入丢包率";
	}
	else if (type.equals("ifinerrors")) {
		title = "流入错误包数";
	}
	else if (type.equals("ifinoctetsbpsmax")) {
		title = "流入峰值";
	}
	else if (type.equals("ifinucastpktspps")) {
		title = "每秒流入单播包数";
	}
	else if (type.equals("ifoutoctetsbps")) {
		title = "流出速率(bps)";
	}
	else if (type.equals("ifoutdiscardspps")) {
		title = "流出丢包率(bps)";
	}
	else if (type.equals("ifouterrors")) {
		title = "流出错误包数(bps)";
	}
	else if (type.equals("ifoutoctetsbpsmax")) {
		title = "流出峰值(bps)";
	}
	else if (type.equals("ifinnucastpktspps")) {
		title = "每秒流入非单播包数(bps)";
	}
	else if (type.equals("ifinerrorspps")) {
		title = "流入错包率(bps)";
	}
	else if (type.equals("ifinoctets")) {
		title = "流入字节数(bps)";
	}
	else if (type.equals("ifindiscards")) {
		title = "流入丢弃包数(bps)";
	}
	else if (type.equals("ifoutucastpktspps")) {
		title = "每秒流出单播包数(bps)";
	}
	else if (type.equals("ifouterrorspps")) {
		title = "流出错包率(bps)";
	}
	else if (type.equals("ifoutoctets")) {
		title = "流出字节数(bps)";
	}
	else if (type.equals("ifoutdiscards")) {
		title = "流出丢弃包数(bps)";
	}
	else if (type.equals("ifoutnucastpktspps")) {
		title = "每秒流出非单播包数(bps)";
	}
	else if (type.equals("ifinunknownprotospps")) {
		title = "每秒流入未知协议包数(bps)";
	}
	else if (type.equals("ifoutqlenpps")) {
		title = "每秒流出队列大小(bps)";
	}

	Cursor cursor = (Cursor)session.getAttribute("Cursors");
	HashMap map = (HashMap)session.getAttribute("FluxInfo");
	
	umd.setRequest(request);
	ArrayList obj = umd.getGrapCursor(cursor,map);

	Cursor[] cursors = (Cursor[])obj.get(0);
	String[] rowsKey = (String[])obj.get(1);
	TimeSeriesChart chart = new TimeSeriesChart();
	String filename = null;
	String graphURL = null;

	chart.setWidth(600);
	chart.setHeight(400);
	chart.setBlnShowShape(false);
	chart.setTimeType(1);
	chart.setChartBaseinfo("","时间", title + "单位（G）", "collecttime","value",3);
	chart.setTimeStep(5);
	
	chart.setChartDataset(cursors,rowsKey,null);
	filename = chart.createChart(session, new PrintWriter(out));
	graphURL = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
	String strData ="<img src='"+ graphURL+"' border=0 usemap='#"+filename+"'>";
%>

<DIV id="idLayer2">
<table width="98%"  border="0" cellspacing="0" cellpadding="0" align="center">
	<tr>
	  <td align="center">
			<%=strData%>
	<td>
	</tr>
</table>
</DIV>
<SCRIPT LANGUAGE="JavaScript">
<!--
if(typeof(parent.reportGraphicslView) == "object"){
	parent.reportGraphicslView.innerHTML = idLayer2.innerHTML;
}
var s = document.all("<%=filename%>").outerHTML;
parent.document.body.insertAdjacentHTML("beforeEnd",s);
//-->
</SCRIPT>
