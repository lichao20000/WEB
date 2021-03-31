<%--
	�豸����
	author:hemc
	email:hemc@lianchuang.com
	date:2006-09-01
--%>

<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page import="com.linkage.litms.webtopo.GetPm_Expreesion" %>
<%@ page import="com.linkage.litms.common.chart.LineVolumeChart" %>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.io.PrintWriter" %>
<%
	request.setCharacterEncoding("GBK");
	String expressionid = request.getParameter("expID");
	String expName = request.getParameter("expName");
	String expDescr = request.getParameter("expDescr");
	String device_id = request.getParameter("device_id");
	String device_name = request.getParameter("deviceName");
	String loopback_ip = request.getParameter("loopback_ip");
	//����ҳ���indexidΪ 2|3|4|5��ʽ
	String str_indexid = request.getParameter("indexid");
	//������ʽ����
	List indexids = StringUtils.split(str_indexid,"|");
	//����ʹ��  StringUtils.partOff(str_indexid,"|"); ��������
	Cursor[] cursor = null;
	String graphURL = null;
	String filename = null;
	GetPm_Expreesion getPmExpression = new GetPm_Expreesion(request);
	List list_id = getPmExpression.getIDFromPm_Map_Instance(device_id,expressionid,indexids);
	//TimeSeriesChart chart = new TimeSeriesChart();
    LineVolumeChart chart = new LineVolumeChart();
	chart.setToolTip(true);
    DateTimeUtil dateTimeUtil = null;
    String strTime_start = null;
	String strTime_end = null;
%>
<table width="100%" border="0" align="center" cellpadding="2"
	cellspacing="3">
	<tr align="center">
		<td height="10" class="text"><span><strong>�豸����</strong></span></td>
	</tr>
</table>
<%
	long end = new Date().getTime()/1000;
	//dateTimeUtil = new DateTimeUtil(end*1000);
	//���ñ�������
	getPmExpression.setReportType(1);
	getPmExpression.setValueColumn("value");
	cursor = getPmExpression.getCursorReport(list_id,new DateTimeUtil(end*1000));
	strTime_end = getPmExpression.getStrTime_end();
	strTime_start = getPmExpression.getStrTime_start();
	chart.setTimeType(4);
	chart.setWidth(600);
    chart.setHeight(250);
	chart.setChartBaseinfo(device_name + "/" + loopback_ip + "/" + expName,"ʱ��","valueֵ","gathertime","value",3);
	//chart.setDomainAxis("H",5,1);
	chart.setDomainAxis("H:mm");
	chart.setHourMarker(end);
	chart.setVertical(false);
	chart.setInverted(true);
	chart.setPositiveArrowVisible(true);
	chart.setChartDataset(cursor,getPmExpression.getRowKeys(),null);
	filename = chart.createChart(session, new PrintWriter(out));
	graphURL = request.getContextPath()
			+ "/servlet/DisplayChartLinkage?filename=" + filename;

%>
<table width="100%" border="0" align="center" cellpadding="2"
	cellspacing="3">
	<tr align="center">
		<td align="center">
			<table width="500" border="0" cellspacing="0" cellpadding="0"
				align="center">
				<tr>
					<td colspan="4" ><strong>ÿ�� ͼ��(<%=strTime_start%> �� <%=strTime_end%>)</strong></td>
				</tr>
				<tr>
					<td colspan="4"><img src='<%=graphURL%>' border=0 usemap="#<%=filename%>">&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

<%
	//���ñ�������
	getPmExpression.setReportType(2);
	getPmExpression.setValueColumn("value");
	cursor = getPmExpression.getCursorReport(list_id,new DateTimeUtil(end*1000));
	strTime_start = getPmExpression.getStrTime_start();
	chart.setChartBaseinfo(device_name + "/" + loopback_ip + "/" + expName,"ʱ��","ƽ��ֵ","gathertime","value",3);
	chart.setDomainAxis("EEE",3,1);
	//chart.setDomainAxis("E H:mm");
	chart.setHourMarker(getPmExpression.getMarker());
	chart.setVertical(false);
	chart.setTimeType(4);
	chart.setChartDataset(cursor,getPmExpression.getRowKeys(),null);
	filename = chart.createChart(session, new PrintWriter(out));
	graphURL = request.getContextPath()
				+ "/servlet/DisplayChartLinkage?filename=" + filename;

%>
<table width="100%" border="0" align="center" cellpadding="2"
	cellspacing="3">
	<tr align="center">
		<td align="center">
			<table width="500" border="0" cellspacing="0" cellpadding="0"
				align="center">
				<tr>
					<td colspan="4" ><strong>ÿ�� ͼ��(<%=strTime_start%> �� <%=strTime_end%>)</strong></td>
				</tr>
				<tr>
					<td colspan="4"><img src='<%=graphURL%>' border=0 usemap="#<%=filename%>">&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

<% 
	getPmExpression.setReportType(3);
	getPmExpression.setValueColumn("avgvalue");
	cursor = getPmExpression.getCursorReport(list_id,new DateTimeUtil(end*1000));
	strTime_start = getPmExpression.getStrTime_start();
	chart.setChartBaseinfo(device_name + "/" + loopback_ip + "/" + expName,"ʱ��","ƽ��ֵ","gathertime","avgvalue",3);
	chart.setTimeType(4);
	chart.setDomainAxis("M��d��",3,1);
	chart.setHourMarker(getPmExpression.getMarker());
	//chart.setDomainAxis("��w",3,7);
	chart.setChartDataset(cursor,getPmExpression.getRowKeys(),null);
	filename = chart.createChart(session, new PrintWriter(out));
	graphURL = request.getContextPath()
     				+ "/servlet/DisplayChartLinkage?filename=" + filename;
%>
<table width="100%" border="0" align="center" cellpadding="2"
	cellspacing="3">
	<tr align="center">
		<td align="center">
			<table width="500" border="0" cellspacing="0" cellpadding="0"
				align="center">
				<tr>
					<td colspan="4" ><strong>ÿ�� ͼ��(<%=strTime_start%> �� <%=strTime_end%>)</strong></td>
				</tr>
				<tr>
					<td colspan="4"><img src='<%=graphURL%>' border=0 usemap="#<%=filename%>">&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

<%@ include file="../foot.jsp"%>