/**
 * @(#)ChartDemo.java 2006-2-7
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.common.chart;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.database.Cursor;

/**
 * javaBean:获取数据分布图.
 * 
 * @author yanhj
 * @version 1.00
 * @since Liposs 2.1
 */
public class PieChartDemo
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(PieChartDemo.class);
	/** 数据 */
	private Cursor cursor;

	/** session */
	private HttpSession session;

	/** out */
	private PrintWriter pw;

	/** 分布图标题 */
	private String title = "分布图";

	/** 长 */
	private int width = 500;

	/** 宽 */
	private int height = 270;

	/** 背景色 */
	private Color color = Color.yellow;

	/**
	 * 
	 */
	public PieChartDemo()
	{
		super();
	}

	/**
	 * @return Returns the color.
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * @param color
	 *            The color to set.
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}

	/**
	 * @return Returns the cursor.
	 */
	public Cursor getCursor()
	{
		return cursor;
	}

	/**
	 * Cursor中的sql必须包含("PIE_KEY","PIE_NUMBER") 即：(具体哪些数据项，数量)
	 * 
	 * @param cursor
	 *            The cursor to set.
	 */
	public void setCursor(Cursor cursor)
	{
		this.cursor = cursor;
	}

	/**
	 * @return Returns the hEIGHT.
	 */
	public int getHEIGHT()
	{
		return height;
	}

	/**
	 * @param height
	 *            The hEIGHT to set.
	 */
	public void setHEIGHT(int height)
	{
		this.height = height;
	}

	/**
	 * @return Returns the pw.
	 */
	public PrintWriter getPw()
	{
		return pw;
	}

	/**
	 * @param pw
	 *            The pw to set.
	 */
	public void setPw(PrintWriter pw)
	{
		this.pw = pw;
	}

	/**
	 * @return Returns the session.
	 */
	public HttpSession getSession()
	{
		return session;
	}

	/**
	 * @param session
	 *            The session to set.
	 */
	public void setSession(HttpSession session)
	{
		this.session = session;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return Returns the wIDTH.
	 */
	public int getWIDTH()
	{
		return width;
	}

	/**
	 * @param width
	 *            The wIDTH to set.
	 */
	public void setWIDTH(int width)
	{
		this.width = width;
	}

	/**
	 * 获取分布图
	 * 
	 * @return
	 */
	public String generatePieDemo()
	{
		String filename = null;

		try
		{
			// create a dataset...
			DefaultPieDataset data = new DefaultPieDataset();

			Map map = cursor.getNext();

			logger.debug("1=============" + map);

			while (map != null)
			{
				// logger.debug(map.get("pie_key"));
				logger.debug("3=============" + map.get("num"));
				// logger.debug("4=============" + map.get("vendor_id"));

				data.setValue((String) map.get("vendor_name"), Integer
						.parseInt((String) map.get("num")));

				map = cursor.getNext();
			}

			// create a dataset...
			// DefaultPieDataset data = new DefaultPieDataset();
			// data.setValue("One", new Double(43.2));
			// data.setValue("Two", new Double(10.0));
			// data.setValue("Three", new Double(27.5));
			// data.setValue("Four", new Double(17.5));
			// data.setValue("Five", new Double(11.0));
			// data.setValue("Six", new Double(19.4));

			// create the chart...
			JFreeChart chart = ChartFactory.createPieChart(title, // chart
					// title
					data, // data
					true, // include legend
					true, false);
			logger.debug("2=============");
			// set the background color for the chart...
			chart.setBackgroundPaint(color);
			PiePlot plot = (PiePlot) chart.getPlot();
			plot.setNoDataMessageFont(new Font("SansSerif", Font.PLAIN, 10));
			// plot.setSectionLabelType(PiePlot.NAME_AND_PERCENT_LABELS);
			plot.setNoDataMessage("No data available");
			plot.setToolTipGenerator(new StandardPieToolTipGenerator());
			// plot.setPaint(0, new Color(0xCC, 0xCC, 0xFF));
			// plot.setPaint(1, new Color(0xFF, 0xCC, 0xCC));
			// plot.setPaint(2, new Color(0xCC, 0xFF, 0xCC));
			// plot.setPaint(3, new Color(0xFF, 0x99, 0x99));
			// plot.setPaint(4, new Color(0x99, 0xFF, 0x99));
			// plot.setPaint(5, new Color(0x99, 0x99, 0xFF));
			//
			// plot.setDefaultOutlinePaint(null);

			// Write the chart image to the temporary directory
			ChartRenderingInfo info = new ChartRenderingInfo(
					new StandardEntityCollection());
			filename = ServletUtilities.saveChartAsPNG(chart, width, height,
					info, session);

			logger.debug("3=============");

			// Write the image map to the PrintWriter
			ChartUtilities.writeImageMap(pw, filename, info, false);
			pw.flush();
		} catch (Exception e)
		{
			logger.warn("Exception - " + e.toString());
			e.printStackTrace();
			filename = "public_error_500x300.png";
		}
		return filename;
	}
	
	public String jfreeTest() {

		String[] rowKeys= null;//柱数据
		String[] columnKeys=null;//刻度数据

		String test = "";
		for (int i = 0; i < 24; i++) {
			test += i + "点" + ",";
		}
		rowKeys = new String[]{"南京市区"};
		//columnKeys = new String[]{"1点","2点","3点","10点","11点","12点"};
		columnKeys = test.split(",");

		double[][] data = new double[rowKeys.length][columnKeys.length];
//		data = new double[][]{{100,20,30,40,50,60}};
		
		for (int j=0; j<columnKeys.length; j++) {
			data[0][j] = 20 + j;
		}
		
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);
		JFreeChart chart = ChartFactory.createLineChart( 
		           "在线设备数统计图",
		            "时间",					  //横坐标
		            "在线设备数",                 //纵坐标
		           dataset,                   // data 
		           PlotOrientation.VERTICAL,  // orientation 
		           true,                      // 是否有下面的说明
		           true,                      // tooltips ???
		           false                      // urls ???
		       ); 
		chart.setBackgroundPaint(Color.WHITE);  //背景色
		
//		chart.setBorderVisible(true);  //设置边框是否可见
//		chart.setBorderPaint(Color.BLUE);  //设置边框颜色setBorderVisible()必须为true

		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.CYAN);
		CategoryAxis domainAxis = plot.getDomainAxis();
		
		//设置横坐标节点的角度45度或90度等
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        plot.setDomainAxis(domainAxis);
        
        // customise the range axis 设置统计图中只显示整数
//        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis(); 
//        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); 
//        rangeAxis.setAutoRangeIncludesZero(true); 
//        rangeAxis.setUpperMargin(0.20);
//        rangeAxis.setLabelAngle(Math.PI / 2.0);  
        
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();   
        renderer.setShapesVisible(true);//series 点（即数据点）可见
//        renderer.setSeriesPaint(0, new Color(0, 0, 255));
//        renderer.setSeriesPaint(1, new Color(255, 0, 255));
//        renderer.setSeriesPaint(2, new Color(0, 255, 255));
//        renderer.setSeriesPaint(3, new Color(0,125,0));
		
		String filename = "12345.jpg";
		try {
			filename = ServletUtilities.saveChartAsPNG(chart, 850, 550, null, session);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return filename;
	}
	
}





















