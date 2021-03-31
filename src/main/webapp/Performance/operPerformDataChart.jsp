<%@ include file="../timelater.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="com.linkage.litms.common.database.*"%>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@ page import="com.linkage.litms.common.util.StringUtils"%>
<%@ page import="com.linkage.litms.performance.GeneralOperPerf"%>
<%@ page import="com.linkage.litms.common.chart.LineVolumeChart"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page contentType="text/html;charset=GBK"%>
<%   
            Cursor[] cursors= new Cursor[1];
			//Map fields;			
			String start = request.getParameter("hidstart");			
			//String device_id = request.getParameter("device_id");			
			String device_ip = request.getParameter("loopback_ip");
			String device_name = request.getParameter("device_name");
			device_name = Encoder.toGBKFromUTF(device_name);
			String stype = request.getParameter("SearchType");
			
			//图片所要画的数据列 列名
			String sortcolumn = "oid_value";
			String title = "";
			//生成图片地址
			ArrayList graphURL = new ArrayList();
			graphURL.clear();
			
			long end = new Date().getTime()/1000;
			int iStart = 0;
			int iEnd = 0;
			int type = Integer.parseInt(stype);
			switch (type) {					
				case 3 : {//周
					iStart = Integer.parseInt(start);
					DateTimeUtil du = new DateTimeUtil(((long) iStart) * 1000);
					du = new DateTimeUtil(du.getFirstDayOfWeek("US"));
					iStart = Integer.valueOf(du.getLongTime() + "").intValue();
					du = new DateTimeUtil(du.getLastDayOfWeek("US"));
					iEnd = Integer.valueOf(du.getLongTime()+3600 * 24 + "").intValue();
					title = "性能周报表";
					break;
				}
				case 4 : {//月
					iStart = Integer.parseInt(start);
					iStart = Integer.valueOf(
							StringUtils.getNowMonthDay(iStart) + "").intValue();
					iEnd = Integer.valueOf(
							(StringUtils.getNextMonthDay(iStart))
									+ "").intValue();

					title = "性能月报表";
					break;
				}
			}

			
			GeneralOperPerf netPmMore = new GeneralOperPerf(iStart, iEnd, type);
			//获得用户选择的设备的数据结果集数组 并已根据设备分组
			cursors[0] = netPmMore.getAllDeviceGeneralChartData(request);
			String dateFormatStr=netPmMore.getDateFormat();

			//String _DeviceIp = "";
			//String _DeviceName = "";
			//性能表达式描述
			String _ExpDescr = "";
			//数据单位
			String _unit = "";
			//曲线图
			//TimeSeriesChart chart = new TimeSeriesChart();
			//mrtg图
			LineVolumeChart chart = new LineVolumeChart();
			
			String temp_oidtype;
			String oid_type="";
			String rowKeys = "";	
			
			//fields = cursors[0].getNext();
			_ExpDescr = Encoder.toGBKFromUTF(request.getParameter("descr"));
			_unit = Encoder.toGBKFromUTF(request.getParameter("unit"));

            chart.setToolTip(true);			
			chart.setWidth(600);
			chart.setHeight(250);
			chart.setChartBaseinfo(device_name + "【" + device_ip + "】 "+title, "日期", _ExpDescr + ": 单位("
						+ _unit + ")", "gathertime", sortcolumn, 3);
			chart.setDomainAxis(dateFormatStr);
			chart.setHourMarker(end);
			chart.setVertical(false);
			//chart.setInverted(true);
			chart.setPositiveArrowVisible(true);
			//设置对每条曲线说明是否显示
			chart.setLegend(false);
			chart.setChartDataset(cursors,rowKeys.split(","),null);
			String filename = chart.createChart(session,new PrintWriter(out));
			graphURL.add(new String[]{request.getContextPath()
						+ "/servlet/DisplayChartLinkage?filename=" + filename,filename});
			
			//clear
			cursors = null;
			%>
<!--<DIV id="idLayer" style="overflow:auto;width:'800px';height:'400px'">-->
<table width="100%" border="0" cellpadding="2" align="center"
	cellspacing="3">
	<tr align="center">
		<td align="center">
		<table border="0" cellspacing="0" cellpadding="0">
			<%
			for(int i=0;i<graphURL.size();i++){				
				%>
				<tr>
					<td><img src="<%=((String[])graphURL.get(i))[0]%>" border=0 usemap="#<%=((String[])graphURL.get(i))[1]%>"></td>
				</tr>
				<%				
			}
			%>
		</table>
		</td>
	</tr>
</table>

