<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.corba.interfacecontrol.*"%>
<%@ page import="org.omg.CORBA.StringHolder"%>
<%@ page import="java.text.NumberFormat"%>
<%@ page import="com.linkage.litms.common.chart.VerticalBarChart" %>
<%@ page import="java.io.PrintWriter"%>

<%request.setCharacterEncoding("GBK");
			String graphURL = null;
			String filename = null;
			String deviceID = request.getParameter("device_id");
			String deviceName = request.getParameter("device_name");
			String loopbackip = request.getParameter("loopback_ip");
			String type = request.getParameter("type");

			FluxControl.FluxData[] fluxDatas = null;
			fluxDatas = FluxManagerInterface.GetInstance().getPortData(
					deviceID, new StringHolder());

			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(2);
			nf.setMinimumFractionDigits(2);

			Cursor inoctetsCur = new Cursor();
			Cursor outoctetsCur = new Cursor();

			for (int i = 0; i < fluxDatas.length; i++) {
				String key = null;
				String valuein = null;
				String valueout = null;

				Map inFild = new HashMap();
				Map outFild = new HashMap();
				key = fluxDatas[i].index;
				valuein = fluxDatas[i].ifinoctetsbps;
				valueout = fluxDatas[i].ifoutoctetsbps;

				if ((!"0".equals(valuein.trim()))
						|| (!"0".equals(valueout.trim()))) {
					inFild.put("xvalue", key);
					inFild.put("yvalue", valuein);
					inoctetsCur.add(inFild);

					outFild.put("xvalue", key);
					outFild.put("yvalue", valueout);
					outoctetsCur.add(outFild);
				}

			}

			Cursor[] cursors = new Cursor[2];
			cursors[0] = inoctetsCur;
			cursors[1] = outoctetsCur;
			String[] rowKeys = new String[2];
			String strTitle = "端口流量显示";
			rowKeys[0] = "流入流量";
			rowKeys[1] = "流出流量";

			VerticalBarChart chart = new VerticalBarChart();

			int len1 = inoctetsCur.getRecordSize();
			int len2 = outoctetsCur.getRecordSize();
			int max1 = len1;
			int max2 = len2;

			double dUpperMargin = 0.3;

			if (max1 < 10) {
				dUpperMargin = 0.9 * ((10 - max1) / 9);
				if (dUpperMargin < 0.3) {
					dUpperMargin = 0.3;
				}
				chart.setDUpperMargin(dUpperMargin);
			}

			if (max2 < 10) {
				dUpperMargin = 0.9 * ((10 - max2) / 9);
				if (dUpperMargin < 0.3) {
					dUpperMargin = 0.3;
				}
				chart.setDUpperMargin(dUpperMargin);
			}

			chart.setChartBaseinfo(strTitle, "设备端口", "流量值（单位bps）", "xvalue",
					"yvalue", 3);

			chart.setChartDataset(cursors, rowKeys, null);

			filename = chart.createChart(session, new PrintWriter(out));

			graphURL = request.getContextPath()
					+ "/servlet/DisplayChart?filename=" + filename;

			String strData = "<img src='" + graphURL + "' border=0 usemap='#"
					+ filename + "'>";

			%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>图表显示</title>

</head>
<body>
<table width="750" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td bgcolor="#FFFFFF"><br>
		<br>
		<table width="95%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="blue_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">网络实时显示</td>
				<td align="left"><span class="text">&nbsp;&nbsp; </span></td>
				<td width="111" align="left" bordercolor="0">
				<div align="center"><a
					href="webtop_liuliang.jsp?device_id=<%= deviceID %>&type=<%=type %>"><img
					src="../images/baobiao2.gif" width="15" height="15" border="0">
				报表显示</a></div>
				</td>
			</tr>
		</table>
		<table width="95%" border=0 align="center" cellpadding="1"
			cellspacing="1" bgcolor="#999999" class="text">
			<tr bgcolor="#FFFFFF" class="blue_title">
				<td>
				<div align="center">设备名:<%=deviceName%>&nbsp;&nbsp;&nbsp;&nbsp;
				设备IP地址:<%=loopbackip%>&nbsp;&nbsp;&nbsp;&nbsp; <!--Y轴最大值
              <select name="select" class="form_kuang" onchange="javascript:typeChange();">
                <option value="0" selected>100</option>
                <option value="1">200</option>
                                                        </select> --></div>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td bgcolor="#E6E1DB"><%=strData%></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>
</html>
