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
		title = "��������";
	}
	else if (type.equals("ifindiscardspps")) {
		title = "���붪����";
	}
	else if (type.equals("ifinerrors")) {
		title = "����������";
	}
	else if (type.equals("ifinoctetsbpsmax")) {
		title = "�����ֵ";
	}
	else if (type.equals("ifinucastpktspps")) {
		title = "ÿ�����뵥������";
	}
	else if (type.equals("ifoutoctetsbps")) {
		title = "��������(bps)";
	}
	else if (type.equals("ifoutdiscardspps")) {
		title = "����������(bps)";
	}
	else if (type.equals("ifouterrors")) {
		title = "�����������(bps)";
	}
	else if (type.equals("ifoutoctetsbpsmax")) {
		title = "������ֵ(bps)";
	}
	else if (type.equals("ifinnucastpktspps")) {
		title = "ÿ������ǵ�������(bps)";
	}
	else if (type.equals("ifinerrorspps")) {
		title = "��������(bps)";
	}
	else if (type.equals("ifinoctets")) {
		title = "�����ֽ���(bps)";
	}
	else if (type.equals("ifindiscards")) {
		title = "���붪������(bps)";
	}
	else if (type.equals("ifoutucastpktspps")) {
		title = "ÿ��������������(bps)";
	}
	else if (type.equals("ifouterrorspps")) {
		title = "���������(bps)";
	}
	else if (type.equals("ifoutoctets")) {
		title = "�����ֽ���(bps)";
	}
	else if (type.equals("ifoutdiscards")) {
		title = "������������(bps)";
	}
	else if (type.equals("ifoutnucastpktspps")) {
		title = "ÿ�������ǵ�������(bps)";
	}
	else if (type.equals("ifinunknownprotospps")) {
		title = "ÿ������δ֪Э�����(bps)";
	}
	else if (type.equals("ifoutqlenpps")) {
		title = "ÿ���������д�С(bps)";
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
	chart.setChartBaseinfo("","ʱ��", title + "��λ��G��", "collecttime","value",3);
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
