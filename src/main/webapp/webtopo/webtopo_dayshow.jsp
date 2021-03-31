<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.flux.*,
				 com.linkage.litms.flux.GeneralFluxPerf,
				 com.linkage.litms.report.*,
				 com.linkage.litms.common.database.Cursor,
				 com.linkage.litms.LipossGlobals,
				 com.linkage.litms.common.chart.*,
				 com.linkage.litms.webtopo.*,
				 java.util.*,
				 java.sql.*,
				 java.io.*,
				 java.lang.*,
                 com.linkage.litms.common.util.*"%>
<%
request.setCharacterEncoding("GBK");
String kind = request.getParameter("radio_kind");
Map fields;
String gathertime= null;
//int start_time = Integer.parseInt(request.getParameter("hidday"));
//String str_type = request.getParameter("type");
//int type = Integer.parseInt(str_type);
String str_ports = request.getParameter("ports");
String[] prots = str_ports.split(",");

FluxReport obj_fluxReport = new FluxReport();

//String strKindName = obj_fluxReport.getFluxKindName(kind);

Cursor cursor = obj_fluxReport.getFluxReport(request);
fields = cursor.getNext();

int portNum = obj_fluxReport.getPortsNum(request);

String old_id="",cur_id;

String tmp;

Cursor[] cursors = new Cursor[portNum];
String[] rowKeys = new String[portNum];

int ln = -1;
while(fields != null){

	gathertime  = (String)fields.get("collecttime");

	cur_id = (String)fields.get("ifindex");
	
	if(!old_id.equals(cur_id)){
		ln++;	//实例不同，实例Cursor数组下移
		cursors[ln] = new Cursor();
	}
	cursors[ln].add(fields);
	
	old_id = cur_id;
	fields = cursor.getNext();
}

for(int i=0;i<portNum;i++) {
	rowKeys[i] = "端口索引：" + String.valueOf(i+1);
}
String filename="";
String graphURL="";


//通过DeviceResourceInfo从数据库获得设备信息
String device_id = request.getParameter("dev_id");
DeviceResourceInfo deviceInfo = new DeviceResourceInfo();
Cursor cursorinfo = deviceInfo.getDeviceResource(device_id);
Map myMap = cursorinfo.getNext();
String deviceName= (String)myMap.get("device_name");
String loopbackip= (String)myMap.get("loopback_ip");


String strTitle = deviceName + "端口流量";
//String strUnit = strKindName;
TimeSeriesChart chart = new TimeSeriesChart();
chart.setChartBaseinfo(strTitle,"时间","xx","collecttime",kind,3);

chart.setTimeStep(5);	
chart.setTimeType(1);

chart.setChartDataset(cursors,rowKeys,null);
filename = chart.createChart(session, new PrintWriter(out));
graphURL = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;

%>
<HTML>
<HEAD>
<TITLE><%//=NetworkGlobals.getNetworkName()%> </TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb_2312">
<LINK REL="stylesheet" HREF="style.css" TYPE="text/css">
</HEAD>

<BODY scrolling="no">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
	<img src='<%=graphURL%>'  border=0 usemap='#<%=filename%>'>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
</BODY>
</HTML>
