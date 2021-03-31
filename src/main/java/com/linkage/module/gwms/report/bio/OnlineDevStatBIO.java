package com.linkage.module.gwms.report.bio;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

import com.linkage.module.gwms.report.bio.interf.I_OnlineDevStatBIO;
import com.linkage.module.gwms.report.dao.interf.I_OnlineDevStatDAO;

/**
 * @author Jason(3412)
 * @date 2009-4-28
 */
public class OnlineDevStatBIO implements I_OnlineDevStatBIO{

	I_OnlineDevStatDAO onlineStatDao;

	/**
	 * @author qxq
	 * @category 
	 * 
	 * @param chartType
	 * @param city_id
	 * @param startTime
	 * @param startEnd
	 * @return
	 */
	public String getDataOnLine(String isReport,String reportType,String chartType,String city_id, long startTime,
			long startEnd){
		
		//图片
		String filename = "";
		
		StringBuffer strbuilder = new StringBuffer();
		strbuilder.append("<table width='95%'>");
		
		if("print".equals(isReport)){
			strbuilder.append("<tr><td  colspan='3' width='100%'>");
			strbuilder.append("<td height=\"20\" align=\"right\"><a href=\"javascript:window.print()\">打印</a></td>");
			strbuilder.append("</d></r>");
		}else{
			//打印
			strbuilder.append("<tr><td  colspan='3' width='100%'>");
			strbuilder.append("<a href=\"javascript:queryDataForPrint(");
			strbuilder.append(startTime);
			strbuilder.append(",'");
			strbuilder.append(city_id);
			strbuilder.append("',");
			strbuilder.append(reportType);
			strbuilder.append(",");
			strbuilder.append(chartType);
			strbuilder.append(",''");
			strbuilder.append(");\"><img src=\"../../images/print.gif\" border=\"0\" width=\"16\" height=\"16\"></img></a>&nbsp;&nbsp;&nbsp;");
			
			//导出Excel
			strbuilder.append("<a href=\"javascript:queryDataForExcel(");
			strbuilder.append(startTime);
			strbuilder.append(",'");
			strbuilder.append(city_id);
			strbuilder.append("',");
			strbuilder.append(reportType);
			strbuilder.append(",");
			strbuilder.append(chartType);
			strbuilder.append(",''");
			strbuilder.append(");\"><img src=\"../../images/excel.gif\" border=\"0\" width=\"16\" height=\"16\"></img></a></a>&nbsp;&nbsp;&nbsp;");
			//导出到pdf
			strbuilder.append("<a href=\"javascript:queryDataForPdf(");
			strbuilder.append(startTime);
			strbuilder.append(",'");
			strbuilder.append(city_id);
			strbuilder.append("',");
			strbuilder.append(reportType);
			strbuilder.append(",");
			strbuilder.append(chartType);
			strbuilder.append(",''");
			strbuilder.append(");\"><img src=\"../../images/pdf.gif\" border=\"0\" width=\"16\" height=\"16\"></img></a>");
			strbuilder.append("</d></r>");
		}
		
		// 标题行
		strbuilder.append("<tr><th width='35%'>");
		strbuilder.append("设备属地");
		strbuilder.append("</th><th width='30%'>");
		strbuilder.append("采集时间点");
		strbuilder.append("</th><th width='30%'>");
		strbuilder.append("在线设备数量");
		strbuilder.append("</th></tr>");
		
		String[] Y_coordinate = new String[24];
		
		for(int i=0;i<24;i++){
			Y_coordinate[i] = i+"点";
		}
		
		if("0".equals(chartType)){
			
			String[] X_coordinate = new String[1];
			List cityList = onlineStatDao.getCityBySelf(city_id);
			X_coordinate[0] = String.valueOf(((Map)cityList.get(0)).get("city_name")).toString();
			
			List listData = onlineStatDao.getDataOnLineDev(city_id, startTime, startEnd, chartType);
			
			double data[][] = new double[1][24];
			
			for(int i=0;i<24;i++){
				data[0][i] = 0;
			}
			
			for(int i=0;i<listData.size();i++){
				
				Map oneDate = (Map)listData.get(i);
				String oneDateTimePoint = String.valueOf(oneDate.get("r_timepoint")).toString();
				String oneDateCount = String.valueOf(oneDate.get("r_count")).toString();
				data[0][Integer.parseInt(oneDateTimePoint)] = Double.parseDouble(oneDateCount);
				
				strbuilder.append("<tr><td>");
				strbuilder.append(X_coordinate[0]);

				strbuilder.append("</td><td>");
				strbuilder.append(oneDateTimePoint+"点");

				strbuilder.append("</td><td>");
				strbuilder.append(oneDateCount);
				strbuilder.append("</td></tr>");
				
			}
			
			filename = createLineChartByLinkage("设备在线月统计表","时间","数量",X_coordinate,Y_coordinate,data);
			
		}else{
			
			List cityList = onlineStatDao.getChildCity(city_id);
			String[] X_coordinate = new String[cityList.size()];
			X_coordinate[0] = String.valueOf(((Map)cityList.get(0)).get("city_name")).toString();
			
			double data[][] = new double[cityList.size()][24];
			
			for(int i=0;i<cityList.size();i++){
				for(int j=0;j<24;j++){
					data[i][j] = 0;
				}
			}
			
			for(int i=0;i<cityList.size();i++){		
				Map oneDateCity = (Map)cityList.get(i);
				String oneDateCityId = String.valueOf(oneDateCity.get("city_id")).toString();
				String oneDateCityName = String.valueOf(oneDateCity.get("city_name")).toString();
				
				List listData = onlineStatDao.getDataOnLineDev(oneDateCityId, startTime, startEnd, chartType);
				X_coordinate[i] = oneDateCityName;
				
				for(int j=0;j<listData.size();j++){
					
					Map oneDate = (Map)listData.get(j);
					String oneDateTimePoint = String.valueOf(oneDate.get("r_timepoint")).toString();
					String oneDateCount = String.valueOf(oneDate.get("r_count")).toString();
					data[i][Integer.parseInt(oneDateTimePoint)] = Double.parseDouble(oneDateCount);
					
					strbuilder.append("<tr><td>");
					strbuilder.append(oneDateCityName);

					strbuilder.append("</td><td>");
					strbuilder.append(oneDateTimePoint+"点");

					strbuilder.append("</td><td>");
					strbuilder.append(oneDateCount);
					strbuilder.append("</td></tr>");
					
					
				}
			}
			filename = createLineChartByLinkage("设备在线月统计表","时间","数量",X_coordinate,Y_coordinate,data);
			
		}
		
		//获取request对象
		HttpServletRequest request = ServletActionContext.getRequest();
		if (filename != null) {
			String graphURL = request.getContextPath()
					+ "/servlet/DisplayChart?filename=" + filename;
			strbuilder
					.append("<tr bgcolor=#ffffff><td colspan='3' align=center><img src='"
							+ graphURL
							+ "' border=0 usemap=\'#"
							+ filename
							+ "\'></td></tr>");
		}

		strbuilder.append("<tr class=green_foot><td colspan='3'>&nbsp;</td></tr>");

		strbuilder.append("</table>");
		
		return strbuilder.toString();
		
	}
	
	/**
	 * @author qxq
	 * @category 
	 * 
	 * @param chartType
	 * @param city_id
	 * @param startTime
	 * @param startEnd
	 * @return
	 */
	public ArrayList<Map> getDataOnLineData(String reportType,String chartType,String city_id, long startTime,
			long startEnd){
		
		ArrayList<Map> rs = new ArrayList<Map>();
		
		if("0".equals(chartType)){
			
			String[] X_coordinate = new String[1];
			List cityList = onlineStatDao.getCityBySelf(city_id);
			X_coordinate[0] = String.valueOf(((Map)cityList.get(0)).get("city_name")).toString();
			List listData = onlineStatDao.getDataOnLineDev(city_id, startTime, startEnd, chartType);
			
			for(int i=0;i<listData.size();i++){
				
				Map oneDate = (Map)listData.get(i);
				String oneDateTimePoint = String.valueOf(oneDate.get("r_timepoint")).toString();
				String oneDateCount = String.valueOf(oneDate.get("r_count")).toString();
				
				Map<String, String> one = new HashMap<String, String>();
				one.put("city_name", X_coordinate[0]);
				one.put("r_time", oneDateTimePoint+"点");
				one.put("r_count", oneDateCount);
				rs.add(one);
			}
			
		}else{
			
			List cityList = onlineStatDao.getChildCity(city_id);
			String[] X_coordinate = new String[cityList.size()];
			X_coordinate[0] = String.valueOf(((Map)cityList.get(0)).get("city_name")).toString();

			for(int i=0;i<cityList.size();i++){
				
				Map oneDateCity = (Map)cityList.get(i);
				String oneDateCityId = String.valueOf(oneDateCity.get("city_id")).toString();
				String oneDateCityName = String.valueOf(oneDateCity.get("city_name")).toString();
				
				List listData = onlineStatDao.getDataOnLineDev(oneDateCityId, startTime, startEnd, chartType);
				X_coordinate[i] = oneDateCityName;
				
				for(int j=0;j<listData.size();j++){
					
					Map oneDate = (Map)listData.get(i);
					String oneDateTimePoint = String.valueOf(oneDate.get("r_timepoint")).toString();
					String oneDateCount = String.valueOf(oneDate.get("r_count")).toString();
					
					Map<String, String> one = new HashMap<String, String>();
					one.put("city_name", X_coordinate[0]);
					one.put("r_time", oneDateTimePoint+"点");
					one.put("r_count", oneDateCount);
					rs.add(one);
					
					
				}
			}
			
		}
		
		return rs;
	}
	
	/**
	 * @author qxq
	 * @category 
	 * 
	 * @param chartType
	 * @param city_id
	 * @param startTime
	 * @param startEnd
	 * @param r_timepoint
	 * @return
	 */
	public String getMonthOnLine(String isReport,String reportType,String chartType,String city_id, long startTime,
			long startEnd, String r_timepoint){
		
		//图片
		String filename = "";
		
		StringBuffer strbuilder = new StringBuffer();
		strbuilder.append("<table width='95%'>");
		
		if("print".equals(isReport)){
			strbuilder.append("<tr><td  colspan='3' width='100%'>");
			strbuilder.append("<td height=\"20\" align=\"right\"><a href=\"javascript:window.print()\">打印</a></td>");
			strbuilder.append("</d></r>");
		}else{
			
			//打印
			strbuilder.append("<tr><td  colspan='3' width='100%'>");
			strbuilder.append("<a href=\"javascript:queryDataForPrint(");
			strbuilder.append(startTime);
			strbuilder.append(",'");
			strbuilder.append(city_id);
			strbuilder.append("',");
			strbuilder.append(reportType);
			strbuilder.append(",");
			strbuilder.append(chartType);
			strbuilder.append(",");
			strbuilder.append(r_timepoint);
			strbuilder.append(");\"><img src=\"../../images/print.gif\" border=\"0\" width=\"16\" height=\"16\"></img></a>&nbsp;&nbsp;&nbsp;");
			
			//导出Excel
			strbuilder.append("<a href=\"javascript:queryDataForExcel(");
			strbuilder.append(startTime);
			strbuilder.append(",'");
			strbuilder.append(city_id);
			strbuilder.append("',");
			strbuilder.append(reportType);
			strbuilder.append(",");
			strbuilder.append(chartType);
			strbuilder.append(",");
			strbuilder.append(r_timepoint);
			strbuilder.append(");\"><img src=\"../../images/excel.gif\" border=\"0\" width=\"16\" height=\"16\"></img></a></a>&nbsp;&nbsp;&nbsp;");
			
			//导出到pdf
			strbuilder.append("<a href=\"javascript:queryDataForPdf(");
			strbuilder.append(startTime);
			strbuilder.append(",'");
			strbuilder.append(city_id);
			strbuilder.append("',");
			strbuilder.append(reportType);
			strbuilder.append(",");
			strbuilder.append(chartType);
			strbuilder.append(",");
			strbuilder.append(r_timepoint);
			strbuilder.append(");\"><img src=\"../../images/pdf.gif\" border=\"0\" width=\"16\" height=\"16\"></img></a>&nbsp;&nbsp;&nbsp;");
			strbuilder.append("</d></r>");
		}
			
		// 标题行
		strbuilder.append("<tr><th width='35%'>");
		strbuilder.append("设备属地");
		strbuilder.append("</th><th width='30%'>");
		strbuilder.append("采集时间点");
		strbuilder.append("</th><th width='30%'>");
		strbuilder.append("在线设备数量");
		strbuilder.append("</th></tr>");
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(startEnd*1000);
		cal.set(Calendar.DATE,cal.get(Calendar.DATE)-1);
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DATE);
		
		String[] Y_coordinate = new String[day];
		
		for(int i=0;i<day;i++){
			int tempInt = i +1;
			Y_coordinate[i] = year+"-"+month+"-"+tempInt+" "+r_timepoint+"点";
		}
		
		if("0".equals(chartType)){
			
			String[] X_coordinate = new String[1];
			List cityList = onlineStatDao.getCityBySelf(city_id);
			X_coordinate[0] = String.valueOf(((Map)cityList.get(0)).get("city_name")).toString();
			List listData = onlineStatDao.getMonthOnLineDevAll(city_id, startTime, startEnd, r_timepoint, chartType);
			
			double data[][] = new double[1][day];

			for(int j=0;j<day;j++){
				data[0][j] = 0;
			}
			
			for(int i=0;i<listData.size();i++){
				
				Map oneDate = (Map)listData.get(i);
				String oneDateTime = String.valueOf(oneDate.get("r_time")).toString();
				String oneDateCount = String.valueOf(oneDate.get("r_count")).toString();
				Calendar cal_all = Calendar.getInstance();
				cal_all.setTimeInMillis(Long.parseLong(oneDateTime)*1000);
				data[0][cal_all.get(Calendar.DATE)-1] = Double.parseDouble(oneDateCount);
				
				strbuilder.append("<tr><td>");
				strbuilder.append(X_coordinate[0]);

				strbuilder.append("</td><td>");
				strbuilder.append(cal_all.get(Calendar.YEAR)+"-"+(
						cal_all.get(Calendar.MONTH)+1)+"-"+cal_all.get(Calendar.DATE));

				strbuilder.append("</td><td>");
				strbuilder.append(oneDateCount);
				strbuilder.append("</td></tr>");
				
			}
			
			filename = createLineChartByLinkage("设备在线月统计表","时间","数量",X_coordinate,Y_coordinate,data);
			
		}else{
			
			List cityList = onlineStatDao.getChildCity(city_id);
			String[] X_coordinate = new String[cityList.size()];
			X_coordinate[0] = String.valueOf(((Map)cityList.get(0)).get("city_name")).toString();
			
			double data[][] = new double[cityList.size()][day];
			
			for(int i=0;i<cityList.size();i++){
				for(int j=0;j<day;j++){
					data[i][j] = 0;
				}
			}
			
			for(int i=0;i<cityList.size();i++){
				
				Map oneDateCity = (Map)cityList.get(i);
				String oneDateCityId = String.valueOf(oneDateCity.get("city_id")).toString();
				String oneDateCityName = String.valueOf(oneDateCity.get("city_name")).toString();
				List listData = onlineStatDao.getMonthOnLineDevAll(oneDateCityId, startTime, startEnd, r_timepoint, chartType);
				X_coordinate[i] = oneDateCityName;
				
				for(int j=0;j<listData.size();j++){
					
					Map oneDate = (Map)listData.get(j);
					String oneDateTime = String.valueOf(oneDate.get("r_time")).toString();
					String oneDateCount = String.valueOf(oneDate.get("r_count")).toString();
					Calendar cal_all = Calendar.getInstance();
					cal_all.setTimeInMillis(Long.parseLong(oneDateTime)*1000);
					data[i][cal_all.get(Calendar.DATE)-1] = Double.parseDouble(oneDateCount);
					
					strbuilder.append("<tr><td>");
					strbuilder.append(oneDateCityName);

					strbuilder.append("</td><td>");
					strbuilder.append(cal_all.get(Calendar.YEAR)+"-"+(
							cal_all.get(Calendar.MONTH)+1)+"-"+cal_all.get(Calendar.DATE));

					strbuilder.append("</td><td>");
					strbuilder.append(oneDateCount);
					strbuilder.append("</td></tr>");
					
				}
			}
			filename = createLineChartByLinkage("设备在线月统计表","时间","数量",X_coordinate,Y_coordinate,data);
			
		}
		
		//获取request对象
		HttpServletRequest request = ServletActionContext.getRequest();
		if (filename != null) {
			String graphURL = request.getContextPath()
					+ "/servlet/DisplayChart?filename=" + filename;
			strbuilder
					.append("<tr bgcolor=#ffffff><td colspan='3' align=center><img src='"
							+ graphURL
							+ "' border=0 usemap=\'#"
							+ filename
							+ "\'></td></tr>");
		}

		strbuilder.append("<tr class=green_foot><td colspan='3'>&nbsp;</td></tr>");

		strbuilder.append("</table>");
		
		return strbuilder.toString();
	}
	
	/**
	 * @author qxq
	 * @category 
	 * 
	 * @param chartType
	 * @param city_id
	 * @param startTime
	 * @param startEnd
	 * @param r_timepoint
	 * @return
	 */
	public ArrayList<Map> getMonthOnLineData(String reportType,String chartType,String city_id, long startTime,
			long startEnd, String r_timepoint){

		ArrayList<Map> rs = new ArrayList<Map>();
		
		if("0".equals(chartType)){
			
			String[] X_coordinate = new String[1];
			List cityList = onlineStatDao.getCityBySelf(city_id);
			X_coordinate[0] = String.valueOf(((Map)cityList.get(0)).get("city_name")).toString();
			List listData = onlineStatDao.getMonthOnLineDevAll(city_id, startTime, startEnd, r_timepoint, chartType);
			
			for(int i=0;i<listData.size();i++){
				
				Map oneDate = (Map)listData.get(i);
				String oneDateTime = String.valueOf(oneDate.get("r_time")).toString();
				String oneDateCount = String.valueOf(oneDate.get("r_count")).toString();
				Calendar cal_all = Calendar.getInstance();
				cal_all.setTimeInMillis(Long.parseLong(oneDateTime)*1000);
				
				Map<String, String> one = new HashMap<String, String>();
				one.put("city_name", X_coordinate[0]);
				one.put("r_time", cal_all.get(Calendar.YEAR)+"-"+(cal_all.get(Calendar.MONTH)+1)+"-"+cal_all.get(Calendar.DATE));
				one.put("r_count", oneDateCount);
				rs.add(one);
				
			}
			
		}else{
			
			List cityList = onlineStatDao.getChildCity(city_id);
			String[] X_coordinate = new String[cityList.size()];
			X_coordinate[0] = String.valueOf(((Map)cityList.get(0)).get("city_name")).toString();
			
			for(int i=0;i<cityList.size();i++){
				
				Map oneDateCity = (Map)cityList.get(i);
				String oneDateCityId = String.valueOf(oneDateCity.get("city_id")).toString();
				List listData = onlineStatDao.getMonthOnLineDevAll(oneDateCityId, startTime, startEnd, r_timepoint, chartType);
								
				for(int j=0;j<listData.size();j++){
					
					Map oneDate = (Map)listData.get(i);
					String oneDateTime = String.valueOf(oneDate.get("r_time")).toString();
					String oneDateCount = String.valueOf(oneDate.get("r_count")).toString();
					Calendar cal_all = Calendar.getInstance();
					cal_all.setTimeInMillis(Long.parseLong(oneDateTime)*1000);
					
					Map<String, String> one = new HashMap<String, String>();
					one.put("city_name", X_coordinate[0]);
					one.put("r_time", cal_all.get(Calendar.YEAR)+"-"+(cal_all.get(Calendar.MONTH)+1)+"-"+cal_all.get(Calendar.DATE));
					one.put("r_count", oneDateCount);
					rs.add(one);
					
				}
			}
		}
		
		return rs;
	}
	
	/**
	 * @category 折线图
	 * 
	 * @param title 标题
	 * @param X_coordinate_name 纵坐标名称
	 * @param Y_coordinate_name 横坐标名称
	 * @param X_coordinate 纵坐标
	 * @param Y_coordinate 横坐标
	 * @param data 数据
	 * 
	 * @return
	 */
	public String createLineChartByLinkage(String title,
			String X_coordinate_name, String Y_coordinate_name,
			String X_coordinate[], String Y_coordinate[], double[][] data) {

		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(
				X_coordinate, Y_coordinate, data);
		
		JFreeChart chart = ChartFactory.createLineChart(title, X_coordinate_name,
				Y_coordinate_name, dataset, PlotOrientation.VERTICAL, true, true,
				false);

		//背景色
		chart.setBackgroundPaint(Color.WHITE);

		CategoryPlot plot = chart.getCategoryPlot();
		
		NumberAxis na= (NumberAxis)plot.getRangeAxis();   
		na.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		
		plot.setBackgroundPaint(Color.CYAN);
		CategoryAxis domainAxis = plot.getDomainAxis();

		/*------设置X轴坐标上的文字-----------*/
		domainAxis.setTickLabelFont(new Font("simsun", Font.PLAIN, 12));

		/*------设置X轴的标题文字------------*/
		domainAxis.setLabelFont(new Font("simsun", Font.PLAIN, 12));
		
		/*------这句代码解决了底部汉字乱码的问题-----------*/
		chart.getLegend().setItemFont(new Font("simsun", Font.PLAIN, 12));
		chart.getTitle().setFont(new Font("simsun", Font.PLAIN, 12));
		
		//设置横坐标节点的角度45度或90度等
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		plot.setDomainAxis(domainAxis);

		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot
				.getRenderer();
		//series 点（即数据点）可见
		renderer.setShapesVisible(true);

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String filename = "12345.jpg";
		try {
			filename = ServletUtilities.saveChartAsPNG(chart, 780, 550, null,
					session);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return filename;
	}

	public List getChildCity(String city_id) {

		return onlineStatDao.getChildCity(city_id);
	}

	/**
	 * @return the onlineStatDao
	 */
	public I_OnlineDevStatDAO getOnlineStatDao() {
		return onlineStatDao;
	}

	/**
	 * @param onlineStatDao the onlineStatDao to set
	 */
	public void setOnlineStatDao(I_OnlineDevStatDAO onlineStatDao) {
		this.onlineStatDao = onlineStatDao;
	}
	
}
