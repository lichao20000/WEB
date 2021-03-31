package com.linkage.litms.common.chart;

import java.awt.Font;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;

/**
 * <p>
 * Title:
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

public class ChartDemo_2004
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(ChartDemo_2004.class);
	public ChartDemo_2004()
	{
	}

	public static void main(String[] args)
	{
		ChartDemo_2004 chartDemo_20041 = new ChartDemo_2004();
	}

	public String generateHourDemo(ArrayList list, HttpSession session,
			PrintWriter pw)
	{

		String filename = null;

		try
		{
			// create a title...
			// String chartTitle = "Legal & General Unit Trust Prices";
			String chartTitle = "明 细 图 表";
			XYDataset dataset = createHourDemoDataset(list);
			// Log.out("ssssss");
			JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle,
					"时 间", "采样值", dataset, true, true, false);
			// Log.out("ffffff");

			// 设置图片标题的字体和大小
			Font font = new Font("SIMSUN", Font.CENTER_BASELINE, 20);
			TextTitle _title = new TextTitle(chartTitle);
			_title.setFont(font);
			chart.setTitle(_title);
			// chart.setAntiAlias(true);

			// StandardLegend legend = (StandardLegend) chart.getLegend();
			// legend.setDisplaySeriesShapes(true);
			XYPlot plot = chart.getXYPlot();
			// Log.out("ffffff");
			XYItemRenderer renderer = plot.getRenderer();

			if (renderer instanceof StandardXYItemRenderer)
			{
				StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
				// rr.setPlotShapes(true);
				rr.setShapesFilled(true);
				// rr.setBaseItemLabelFont(new Font("宋体",0,13));
			}
			// Log.out("gggg");
			chart.setBackgroundPaint(java.awt.Color.white);

			// Write the chart image to the temporary directory
			ChartRenderingInfo info = new ChartRenderingInfo(
					new StandardEntityCollection());
			// Log.out("hhhh");
			filename = ServletUtilities.saveChartAsPNG(chart, 500, 300, info,
					session);

			// Write the image map to the PrintWriter
			ChartUtilities.writeImageMap(pw, filename, info, false);
			pw.flush();
			// Log.out("iiii");
			// time out, delete cache bufferImage
			// session.setMaxInactiveInterval(30*60*20);

		} catch (Exception e)
		{
			logger.warn("Exception - " + e.toString());
			e.printStackTrace();
			filename = "public_error_500x300.png";
		}
		return filename;

	}

	public String generateDayDemo(ArrayList list, HttpSession session,
			PrintWriter pw)
	{

		String filename = null;

		try
		{
			// create a title...
			// String chartTitle = "Legal & General Unit Trust Prices";
			String chartTitle = "日 健 康 图 表";
			XYDataset dataset = createDayDemoDataset(list);

			JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle,
					"时 间", "采样值", dataset, true, true, false);

			// StandardLegend legend = (StandardLegend) chart.getLegend();
			// legend.setDisplaySeriesShapes(true);
			XYPlot plot = chart.getXYPlot();
			XYItemRenderer renderer = plot.getRenderer();

			if (renderer instanceof StandardXYItemRenderer)
			{
				StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
				// rr.setPlotShapes(true);
				rr.setShapesFilled(true);
			}

			chart.setBackgroundPaint(java.awt.Color.white);

			// Write the chart image to the temporary directory
			ChartRenderingInfo info = new ChartRenderingInfo(
					new StandardEntityCollection());
			filename = ServletUtilities.saveChartAsPNG(chart, 500, 300, info,
					session);

			// Write the image map to the PrintWriter
			ChartUtilities.writeImageMap(pw, filename, info, false);
			pw.flush();

			// time out, delete cache bufferImage
			// session.setMaxInactiveInterval(30*60*20);

		} catch (Exception e)
		{
			logger.warn("Exception - " + e.toString());
			e.printStackTrace();
			filename = "public_error_500x300.png";
		}
		return filename;

	}

	public String generateMonthDemo(ArrayList list, HttpSession session,
			PrintWriter pw)
	{

		String filename = null;

		try
		{
			// create a title...
			// String chartTitle = "Legal & General Unit Trust Prices";
			String chartTitle = "月 健 康 图 表";
			XYDataset dataset = createMonthDemoDataset(list);

			JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle,
					"时 间", "采样值", dataset, true, true, false);

			// StandardLegend legend = (StandardLegend) chart.getLegend();
			// legend.setDisplaySeriesShapes(true);
			XYPlot plot = chart.getXYPlot();
			XYItemRenderer renderer = plot.getRenderer();

			if (renderer instanceof StandardXYItemRenderer)
			{
				StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
				// rr.setPlotShapes(true);
				rr.setShapesFilled(true);
			}

			chart.setBackgroundPaint(java.awt.Color.white);

			// Write the chart image to the temporary directory
			ChartRenderingInfo info = new ChartRenderingInfo(
					new StandardEntityCollection());
			filename = ServletUtilities.saveChartAsPNG(chart, 500, 300, info,
					session);

			// Write the image map to the PrintWriter
			ChartUtilities.writeImageMap(pw, filename, info, false);
			pw.flush();

			// time out, delete cache bufferImage
			// session.setMaxInactiveInterval(30*60*20);

		} catch (Exception e)
		{
			logger.warn("Exception - " + e.toString());
			e.printStackTrace();
			filename = "public_error_500x300.png";
		}
		return filename;

	}

	/**
	 * Creates a dataset, one series containing unit trust prices, the other a
	 * moving average.
	 * 
	 * @return the dataset.
	 */
	public XYDataset createHourDemoDataset(ArrayList list)
	{

		TimeSeriesCollection dataset = new TimeSeriesCollection();

		try
		{
			ArrayList temp_list = new ArrayList();
			temp_list.clear();

			double y = 0.0;

			// 分类实例数目
			for (int i = 0; i < list.size() / 5; i++)
			{
				if (!temp_list.contains(list.get(i * 5 + 0)))
				{
					temp_list.add(list.get(i * 5 + 0));
				}
			}
			// Log.out(temp_list.size());
			// 初始化时间处理类
			DateTimeUtil system = null;
			// String temp_time=null;
			Second second = null;
			Second temp_second = null;

			// 初始化折线
			TimeSeries[] timeseries = new TimeSeries[temp_list.size()];
			// Log.out("aaaa");
			for (int s = 0; s < temp_list.size(); s++)
			{
				timeseries[s] = new TimeSeries(
						String.valueOf(temp_list.get(s)), Second.class);
				// Log.out("bbbb");
				for (int f = 0; f < list.size() / 5; f++)
				{
					if ((list.get(f * 5 + 0)).equals(temp_list.get(s)))
					{
						// 为时间类赋值
						second = new Second(new Date((Long.parseLong(String
								.valueOf(list.get(f * 5 + 3))) * 1000)));
						// Log.out("cccc" + second);
						// 获得坐标值
						y = Double.parseDouble(String.valueOf(list
								.get(f * 5 + 4)));

						if (temp_second != second)
						{
							// 向折线图time[s]中添加数据
							timeseries[s].add(second, y);

							// 为临时变量赋值
							temp_second = second;
						}
					}
				}

				temp_second = null;
			}
			// Log.out("dddd");
			for (int d = 0; d < temp_list.size(); d++)
			{
				dataset.addSeries(timeseries[d]);
			}
			// Log.out("eeee");
			// cancel cache
			temp_list.clear();
			temp_second = null;
			second = null;
			timeseries = null;
		} catch (Exception e)
		{
			logger.error("错误：" + e.getMessage());
		}

		return dataset;
	}

	/**
	 * Creates a dataset, one series containing unit trust prices, the other a
	 * moving average.
	 * 
	 * @return the dataset.
	 */
	public XYDataset createMonthDemoDataset(ArrayList list)
	{

		TimeSeriesCollection dataset = new TimeSeriesCollection();

		int group = 7;

		try
		{
			ArrayList temp_list = new ArrayList();
			temp_list.clear();

			double y = 0.0;

			// 分类实例数目
			for (int i = 0; i < list.size() / group; i++)
			{
				if (!temp_list.contains(list.get(i * group + 0)))
				{
					temp_list.add(list.get(i * group + 0));
				}
			}

			// 初始化时间处理类
			DateTimeUtil system = null;
			// String temp_time=null;
			Minute second = null;
			Minute temp_second = null;

			// 初始化折线
			TimeSeries[] timeseries = new TimeSeries[temp_list.size()];

			for (int s = 0; s < temp_list.size(); s++)
			{
				timeseries[s] = new TimeSeries(
						String.valueOf(temp_list.get(s)), Minute.class);

				for (int f = 0; f < list.size() / group; f++)
				{
					if ((list.get(f * group + 0)).equals(temp_list.get(s)))
					{
						// 为时间类赋值
						second = new Minute(new Date((Long.parseLong(String
								.valueOf(list.get(f * 5 + 3))) * 1000)));

						// 获得坐标值
						y = Double.parseDouble(String.valueOf(list.get(f
								* group + group - 1)));

						if (temp_second != second)
						{
							// 向折线图time[s]中添加数据
							timeseries[s].add(second, y);

							// 为临时变量赋值
							temp_second = second;
						}
					}
				}

				temp_second = null;
			}

			for (int d = 0; d < temp_list.size(); d++)
			{
				dataset.addSeries(timeseries[d]);
			}

			// cancel cache
			temp_list.clear();
			temp_second = null;
			second = null;
			timeseries = null;
		} catch (Exception e)
		{
			logger.error("错误：" + e.getMessage());
		}

		return dataset;
	}

	/**
	 * Creates a dataset, one series containing unit trust prices, the other a
	 * moving average.
	 * 
	 * @return the dataset.
	 */
	public XYDataset createDayDemoDataset(ArrayList list)
	{

		TimeSeriesCollection dataset = new TimeSeriesCollection();

		int group = 7;

		try
		{
			ArrayList temp_list = new ArrayList();
			temp_list.clear();

			double y = 0.0;

			// 分类实例数目
			for (int i = 0; i < list.size() / group; i++)
			{
				if (!temp_list.contains(list.get(i * group + 0)))
				{
					temp_list.add(list.get(i * group + 0));
				}
			}

			// 初始化时间处理类
			DateTimeUtil system = null;
			// String temp_time=null;
			Second second = null;
			Second temp_second = null;

			// 初始化折线
			TimeSeries[] timeseries = new TimeSeries[temp_list.size()];

			for (int s = 0; s < temp_list.size(); s++)
			{
				timeseries[s] = new TimeSeries(
						String.valueOf(temp_list.get(s)), Second.class);

				for (int f = 0; f < list.size() / group; f++)
				{
					if ((list.get(f * group + 0)).equals(temp_list.get(s)))
					{
						// 为时间类赋值
						second = new Second(new Date((Long.parseLong(String
								.valueOf(list.get(f * group + 3))) * 1000)));

						// 获得坐标值
						y = Double.parseDouble(String.valueOf(list.get(f
								* group + group - 1)));

						if (temp_second != second)
						{
							// 向折线图time[s]中添加数据
							timeseries[s].add(second, y);

							// 为临时变量赋值
							temp_second = second;
						}
					}
				}

				temp_second = null;
			}

			for (int d = 0; d < temp_list.size(); d++)
			{
				dataset.addSeries(timeseries[d]);
			}

			// cancel cache
			temp_list.clear();
			temp_second = null;
			second = null;
			timeseries = null;
		} catch (Exception e)
		{
			logger.error("错误：" + e.getMessage());
		}

		return dataset;
	}

}