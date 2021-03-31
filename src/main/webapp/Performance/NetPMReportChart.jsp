<%-- 
	性能报表 图片生成页
	可针对封装在Cursor数组中 以每个设备为一个Cursor的结果集
	批量生成设备性能曲线图
	现考虑效率问题 每次只生成一个设备的图片
--%>

<%@ include file="../timelater.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="com.linkage.litms.common.database.*"%>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@ page import="com.linkage.litms.common.util.StringUtils"%>
<%@ page import="com.linkage.litms.performance.GeneralNetPerf"%>
<%@ page import="com.linkage.litms.common.chart.LineVolumeChart"%>
<%@ page import="java.io.PrintWriter"%>

<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
			Cursor[] cursors;
			Map fields;
			//
			String start = request.getParameter("hidstart");
			String hour = request.getParameter("hour");
			String device_id = request.getParameter("device_id");			
			String device_ip = request.getParameter("loopback_ip");
			String device_name = request.getParameter("device_name");
			device_name = Encoder.toGBKFromUTF(device_name);
			String stype = request.getParameter("SearchType");
			

			//图片所要画的数据列 列名
			String sortcolumn = "avgvalue";
			String title = "";
			//生成图片地址
			ArrayList graphURL = new ArrayList();
			graphURL.clear();
			
			long end = new Date().getTime()/1000;

			//
			int iStart = 0;
			int iEnd = 0;
			int type = Integer.parseInt(stype);
			switch (type) {
				case 1 : {//小时
					int iHour = Integer.parseInt(hour);
					iStart = Integer.parseInt(start) + iHour * 3600;
					iEnd = iStart + 3600;

					sortcolumn = "value";
					title = "性能小时报表";
					break;
				}
				case 2 : {//日
					iStart = Integer.parseInt(start);
					iEnd = iStart + 3600 * 24;

					sortcolumn = "value";
					title = "性能日报表";
					break;
				}
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

			
			
			GeneralNetPerf netPmMore = new GeneralNetPerf(iStart, iEnd, type);
			//获得用户选择的设备的数据结果集数组 并已根据设备分组
			cursors = netPmMore.getAllDeviceGeneralChartData(request);
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
			
			String old_id="",cur_id;
			String rowKeys = "";
			
			//遍历设备结果集 将图片路径放在graphURL中
			for (int i = 0; i < cursors.length; i++) {
				//开始新的设备遍历循环 清空相应变量
				rowKeys = "";
				old_id = "";
				
				// 遍历每个设备的cursor
				fields = cursors[i].getNext();				
				
				//获取基本设备信息
				//_DeviceName = (String) fields.get("device_name");
				//_DeviceIp = (String) fields.get("loopback_ip");
				_ExpDescr = Encoder.toGBKFromUTF(request.getParameter("descr"));
				_unit = Encoder.toGBKFromUTF(request.getParameter("unit"));
				
				
				//一个实例一个Cursor
				ArrayList indexCursors = new ArrayList();	
				Cursor[] indexData = null;
				if(null!=fields)
				{
					while(fields != null){
						cur_id = (String)fields.get("id");
						//新的索引
						if(!old_id.equals(cur_id)){
							if(rowKeys.equals(""))
								rowKeys += fields.get("indexDesc".toLowerCase());
							else
								rowKeys += ","+fields.get("indexDesc".toLowerCase());
							//在索引Cursor列表最后追加新的Cursor 用来记录新的索引数据
							indexCursors.add(new Cursor());							
							old_id = cur_id;
						}
						
						//将记录加入到索引Cursor中
						((Cursor)indexCursors.get(indexCursors.size()-1)).add(fields);
						
						fields = cursors[i].getNext();
					}
					//将Cursor ArrayList转换为Cursor数组
					indexData =new Cursor[indexCursors.size()];
					indexCursors.toArray(indexData);
				}
				else
				{
					indexData = new Cursor[1];
					indexData[0] =new Cursor();					
				}	
				
				
				//mrtg图
				//LineVolumeChart
				//设置鼠标悬停
				chart.setToolTip(true);
				//chart.setTimeType(5);
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
				chart.setChartDataset(indexData,rowKeys.split(","),null);
				String filename = chart.createChart(session,
						new PrintWriter(out));
				graphURL.add(new String[]{request.getContextPath()
						+ "/servlet/DisplayChartLinkage?filename=" + filename,filename});					
				
				indexData = null;
				indexCursors = null;
				
			}

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

