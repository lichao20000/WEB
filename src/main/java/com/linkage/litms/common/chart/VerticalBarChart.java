/**
 * @(#)VerticalBarChart.java 2006-2-24
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.common.chart;

import java.awt.Color;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.database.Cursor;

/**
 * <p>
 * Title: 图表
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Linkage Corporation.
 * </p>
 * 
 * @author Yanhj, Network Management Product Department, ID Card No.5126
 * @version 2.0
 */
public class VerticalBarChart extends CommonChart
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(VerticalBarChart.class);
	private CategoryDataset dataset; // 数据集

	private double dLowerMargin = 0.01; // x轴左边间隔

	private double dUpperMargin = 0.01; // x轴右边间隔

	private double dCategoryMargin = 0.3; // x轴组之间间隔

	private double dItemMargin = 0.0; // x轴组内间间隔

	private double dyAxisMax = 0; // y轴最大坐标值

	private HashMap categoryMap = null;

	private int width = 590;
	private int height = 380;

	public void setWidth(int width)
	{
		this.width = width;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public VerticalBarChart()
	{
		super();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.linkagesoftware.network.chart.CommonChart#createDataset()
	 */
	protected void createDataset()
	{
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		int count = cursors.length;
		Cursor cursor;
		Map fields;
		String categoryStr, valueStr, xValueStr;
		for (int i = 0; i < count; i++)
		{
			cursor = cursors[i];
			cursor.Reset();
			// logger.debug(cursor.getRecordSize());
			fields = cursor.getNext();
			// if(fields != null)
			// logger.debug(fields.get(yField.toLowerCase()));
			// else
			// logger.debug("error");
			while (fields != null)
			{
				valueStr = (String) fields.get(yField.toLowerCase());
				xValueStr = (String) fields.get(xField.toLowerCase());

				if (valueStr == null || valueStr.equals("")
						|| valueStr.equals("null"))
					valueStr = "0";
				if (categoryMap != null)
					categoryStr = (String) categoryMap.get(xValueStr);
				else
					categoryStr = xValueStr;

				if (valueType == 1)
				{
					dataset.addValue(Integer.parseInt(formart("#", valueStr)),
							rowKeys[i], categoryStr);
				} else if (valueType == 2)
				{
					dataset.addValue(Long.parseLong(formart("#", valueStr)),
							rowKeys[i], categoryStr);
				} else
				{
					dataset.addValue(Double.parseDouble(formart(valueStr)),
							rowKeys[i], categoryStr);
				}

				fields = cursor.getNext();
			}
		}

		this.dataset = dataset;
	}

	private String formart(String v)
	{
		return formart("#.##", v);
	}

	private String formart(String patten, String v)
	{
		DecimalFormat df = new DecimalFormat(patten);
		String s = "0";
		try
		{
			s = df.format(df.parse(v));
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return s;
	}

	public JFreeChart createChart()
	{
		createDataset();
		JFreeChart chart = ChartFactory.createBarChart(title, xAxisLabel,
				yAxisLabel, dataset, PlotOrientation.VERTICAL, true, true,
				false);
		chart.setBackgroundPaint(java.awt.Color.white);

		CategoryPlot plot = chart.getCategoryPlot();

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLowerMargin(dLowerMargin);
		domainAxis.setUpperMargin(dUpperMargin);
		domainAxis.setCategoryMargin(dCategoryMargin);
		// domainAxis.setSkipCategoryLabelsToFit(false); // x轴Label不自动，可以换行

		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setItemMargin(dItemMargin);
		renderer.setDrawBarOutline(false);
		renderer.setSeriesPaint(0, Color.BLUE);

		return chart;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.linkagesoftware.network.chart.CommonChart#createChart(javax.servlet.http.HttpSession,
	 *      java.io.PrintWriter)
	 */
	public String createChart(HttpSession session, PrintWriter pw)
	{
		String filename = null;
		try
		{
			createDataset();
			JFreeChart chart = ChartFactory.createBarChart(title, xAxisLabel,
					yAxisLabel, dataset, PlotOrientation.VERTICAL, true, true,
					false);
			chart.setBackgroundPaint(java.awt.Color.white);

			CategoryPlot plot = chart.getCategoryPlot();

			// x轴参数设置
			CategoryAxis xAxis = plot.getDomainAxis();
			xAxis.setLowerMargin(dLowerMargin);
			xAxis.setUpperMargin(dUpperMargin);
			xAxis.setCategoryMargin(dCategoryMargin);
			// xAxis.setSkipCategoryLabelsToFit(false);
			// domainAxis.setVerticalCategoryLabels(true);

			// y轴参数设置
			NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
			// if (dyAxisMax != 0)
			// yAxis.setMaximumAxisValue(dyAxisMax);

			BarRenderer renderer = (BarRenderer) plot.getRenderer();
			renderer.setItemMargin(dItemMargin);
			renderer.setDrawBarOutline(false);
			// renderer.setSeriesPaint(0, Color.BLUE);

			ChartRenderingInfo info = new ChartRenderingInfo(
					new StandardEntityCollection());
			filename = ServletUtilities.saveChartAsPNG(chart, width, height,
					info, session);

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

	public void setCategoryMap(HashMap map)
	{
		categoryMap = map;
	}

	/**
	 * @param d
	 */
	public void setDCategoryMargin(double d)
	{
		dCategoryMargin = d;
	}

	/**
	 * @param d
	 */
	public void setDLowerMargin(double d)
	{
		dLowerMargin = d;
	}

	/**
	 * @param d
	 */
	public void setDUpperMargin(double d)
	{
		dUpperMargin = d;
	}

	/**
	 * @return
	 */
	public double getDyAxisMax()
	{
		return dyAxisMax;
	}

	/**
	 * @param d
	 */
	public void setDyAxisMax(double d)
	{
		dyAxisMax = d;
	}

}