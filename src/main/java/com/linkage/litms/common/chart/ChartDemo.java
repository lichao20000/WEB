package com.linkage.litms.common.chart;

import java.awt.Color;
import java.awt.Paint;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.other.TableStruct;

/**
 * <p>
 * Title: 主机监控
 * </p>
 * <p>
 * Description: 检索和报表(年报,月报,周报,日报)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: 联创集团
 * </p>
 * 
 * @author 苏智荣
 * @version 1.0
 */

public class ChartDemo
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(ChartDemo.class);
	public static void main(String[] args)
	{
	}

	public ChartDemo()
	{
	}

	public String generateMovingAverageHourDemo(ArrayList list,
			HttpSession session, PrintWriter pw)
	{

		String filename = null;

		try
		{
			// create a title...
			// String chartTitle = "Legal & General Unit Trust Prices";
			String chartTitle = "小 时 报 表";
			XYDataset dataset = createMovingAverageHourDemoDataset(list);

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
	public XYDataset createMovingAverageHourDemoDataset(ArrayList list)
	{

		TimeSeriesCollection dataset = new TimeSeriesCollection();

		try
		{
			ArrayList temp_list = new ArrayList();
			temp_list.clear();

			double y = 0.0;

			// 分类实例数目
			for (int i = 0; i < list.size(); i++)
			{
				if (!temp_list
						.contains(((TableStruct) (list.get(i))).getSlmc()))
				{
					temp_list.add((((TableStruct) (list.get(i))).getSlmc()));
				}
			}

			// 初始化时间处理类
			// String temp_time=null;
			Second second = null;
			Second temp_second = null;

			// 初始化折线
			TimeSeries[] timeseries = new TimeSeries[temp_list.size()];

			for (int s = 0; s < temp_list.size(); s++)
			{
				timeseries[s] = new TimeSeries(
						String.valueOf(temp_list.get(s)), Second.class);

				for (int f = 0; f < list.size(); f++)
				{
					if ((((TableStruct) (list.get(f))).getSlmc())
							.equals(temp_list.get(s)))
					{
						// 为时间类赋值
						second = new Second(new Date((Long
								.parseLong(((TableStruct) (list.get(f)))
										.getCysj())) * 1000));

						// 获得坐标值
						y = Double.parseDouble(((TableStruct) (list.get(f)))
								.getCsz());

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

	public String generateMovingAverageMonthDemo(ArrayList list,
			HttpSession session, PrintWriter pw)
	{

		String filename = null;

		try
		{
			// create a title...
			// String chartTitle = "Legal & General Unit Trust Prices";
			String chartTitle = "主机性能月报表";
			XYDataset dataset = createMovingAverageMonthDemoDataset(list);

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
	public XYDataset createMovingAverageMonthDemoDataset(ArrayList list)
	{

		TimeSeriesCollection dataset = new TimeSeriesCollection();

		try
		{
			ArrayList temp_list = new ArrayList();
			temp_list.clear();

			double y = 0.0;

			// 分类实例数目
			for (int i = 0; i < list.size(); i++)
			{
				if (!temp_list
						.contains(((TableStruct) (list.get(i))).getSlmc()))
				{
					temp_list.add((((TableStruct) (list.get(i))).getSlmc()));
				}
			}

			// 初始化时间处理类
			Minute hour = null;
			Minute temp_hour = null;

			// 初始化折线
			TimeSeries[] timeseries = new TimeSeries[temp_list.size()];

			for (int s = 0; s < temp_list.size(); s++)
			{
				timeseries[s] = new TimeSeries(
						String.valueOf(temp_list.get(s)), Minute.class);

				for (int f = 0; f < list.size(); f++)
				{
					if ((((TableStruct) (list.get(f))).getSlmc())
							.equals(temp_list.get(s)))
					{
						// 为时间类赋值
						hour = new Minute(new Date(Long
								.parseLong(((TableStruct) (list.get(f)))
										.getCysj()) * 1000));

						// 获得坐标值
						y = Double.parseDouble(((TableStruct) (list.get(f)))
								.getAvgvalue());

						if (hour != temp_hour)
						{
							// 向折线图time[s]中添加数据
							timeseries[s].add(hour, y);

							// 为临时变量赋值
							temp_hour = hour;
						}
					}
				}

				temp_hour = null;
			}

			for (int d = 0; d < temp_list.size(); d++)
			{
				dataset.addSeries(timeseries[d]);
			}

			// cancel cache
			temp_hour = null;
			hour = null;
			temp_list.clear();
			list.clear();
			timeseries = null;
		} catch (Exception e)
		{
			logger.error("错误：" + e.getMessage());
		}

		return dataset;
	}

	public String generateMovingAverageWeekDemo(ArrayList list,
			HttpSession session, PrintWriter pw)
	{

		String filename = null;

		try
		{
			// create a title...
			// String chartTitle = "Legal & General Unit Trust Prices";
			String chartTitle = "主机性能周报表";
			XYDataset dataset = createMovingAverageMonthDemoDataset(list);

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
	public XYDataset createMovingAverageWeekDemoDataset(ArrayList list)
	{

		TimeSeriesCollection dataset = new TimeSeriesCollection();

		try
		{
			ArrayList temp_list = new ArrayList();
			temp_list.clear();

			double y = 0.0;

			// 分类实例数目
			for (int i = 0; i < list.size(); i++)
			{
				if (!temp_list
						.contains(((TableStruct) (list.get(i))).getSlmc()))
				{
					temp_list.add((((TableStruct) (list.get(i))).getSlmc()));
				}
			}

			// 初始化时间处理类
			Hour hour = null;
			Hour temp_hour = null;

			// 初始化折线
			TimeSeries[] timeseries = new TimeSeries[temp_list.size()];

			for (int s = 0; s < temp_list.size(); s++)
			{
				timeseries[s] = new TimeSeries(
						String.valueOf(temp_list.get(s)), Hour.class);

				for (int f = 0; f < list.size(); f++)
				{
					if ((((TableStruct) (list.get(f))).getSlmc())
							.equals(temp_list.get(s)))
					{
						// 为时间类赋值
						hour = new Hour(new Date(Long
								.parseLong(((TableStruct) (list.get(f)))
										.getCysj()) * 1000));

						// 获得坐标值
						y = Double.parseDouble(((TableStruct) (list.get(f)))
								.getAvgvalue());

						if (hour != temp_hour)
						{
							// 向折线图time[s]中添加数据
							timeseries[s].add(hour, y);

							// 为临时变量赋值
							temp_hour = hour;
						}
					}
				}

				temp_hour = null;
			}

			for (int d = 0; d < temp_list.size(); d++)
			{
				dataset.addSeries(timeseries[d]);
			}

			// cancel cache
			temp_hour = null;
			hour = null;
			temp_list.clear();
			list.clear();
			timeseries = null;
		} catch (Exception e)
		{
			logger.error("错误：" + e.getMessage());
		}

		return dataset;
	}

	public String generateMovingAverageDayDemo(ArrayList list,
			HttpSession session, PrintWriter pw)
	{

		String filename = null;

		try
		{
			// create a title...
			// String chartTitle = "Legal & General Unit Trust Prices";
			String chartTitle = "主机性能日报表";
			XYDataset dataset = createMovingAverageDayDemoDataset(list);

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

	public String generateMovingAverageMonthLineDemo(ArrayList list,
			HttpSession session, PrintWriter pw)
	{

		String filename = null;

		try
		{
			// create a title...
			// String chartTitle = "Legal & General Unit Trust Prices";
			String chartTitle = "主机性能月报表";
			XYDataset dataset = createMovingAverageMonthLineDemoDataset(list);

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

	public String generateMovingAverageDayLineDemo(ArrayList list,
			HttpSession session, PrintWriter pw)
	{

		String filename = null;

		try
		{
			// create a title...
			// String chartTitle = "Legal & General Unit Trust Prices";
			String chartTitle = "主机性能日报表";
			XYDataset dataset = createMovingAverageDayLineDemoDataset(list);

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

	public String generateMovingAverageHourLineDemo(ArrayList list,
			HttpSession session, PrintWriter pw)
	{

		String filename = null;

		try
		{
			// create a title...
			// String chartTitle = "Legal & General Unit Trust Prices";
			String chartTitle = "主机性能小时报表";
			XYDataset dataset = createMovingAverageHourLineDemoDataset(list);

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
	public XYDataset createMovingAverageDayDemoDataset(ArrayList list)
	{

		TimeSeriesCollection dataset = new TimeSeriesCollection();

		try
		{
			ArrayList temp_list = new ArrayList();
			temp_list.clear();

			double y = 0.0;

			// 分类实例数目
			for (int i = 0; i < list.size(); i++)
			{
				if (!temp_list
						.contains(((TableStruct) (list.get(i))).getSlmc()))
				{
					temp_list.add((((TableStruct) (list.get(i))).getSlmc()));
				}
			}

			// 初始化时间处理类
			Minute minute = null;
			long temp_minute = 0;
			long tranfer_minute = 0;

			// 初始化折线
			TimeSeries[] timeseries = new TimeSeries[temp_list.size()];

			for (int s = 0; s < temp_list.size(); s++)
			{
				timeseries[s] = new TimeSeries(
						String.valueOf(temp_list.get(s)), Minute.class);

				for (int f = 0; f < list.size(); f++)
				{
					if ((((TableStruct) (list.get(f))).getSlmc())
							.equals(temp_list.get(s)))
					{
						// 为时间类赋值
						tranfer_minute = Long.parseLong(((TableStruct) (list
								.get(f))).getCysj()) * 1000;
						minute = new Minute(new Date(tranfer_minute));

						// 获得坐标值
						y = Double.parseDouble(((TableStruct) (list.get(f)))
								.getAvgvalue());

						if (temp_minute != tranfer_minute)
						{
							// 向折线图time[s]中添加数据
							timeseries[s].add(minute, y);

							// 为临时变量赋值
							temp_minute = tranfer_minute;
						}
					}
				}
			}

			for (int d = 0; d < temp_list.size(); d++)
			{
				dataset.addSeries(timeseries[d]);
			}

			// cancel cache
			temp_list.clear();
			minute = null;
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
	public XYDataset createMovingAverageMonthLineDemoDataset(ArrayList list)
	{

		TimeSeriesCollection dataset = new TimeSeriesCollection();

		try
		{
			ArrayList temp_list = new ArrayList();
			temp_list.clear();

			ArrayList time_list = new ArrayList();
			time_list.clear();

			ArrayList ip_list = new ArrayList();
			ip_list.clear();

			ArrayList ipIndex = new ArrayList();
			ipIndex.clear();

			double y = 0.0;

			boolean flag = false;

			// 分类设备IP数目
			for (int i = 0; i < list.size(); i++)
			{
				if (!ip_list.contains(((TableStruct) (list.get(i))).getIp()))
				{
					ip_list.add((((TableStruct) (list.get(i))).getIp()));
				}
			}

			// 分类各自IP下cpu数目
			for (int e = 0; e < ip_list.size(); e++)
			{ // ip iterator
				for (int w = 0; w < list.size(); w++)
				{
					if (ip_list.get(e).equals(
							((TableStruct) (list.get(w))).getIp()))
					{
						// 相同IP
						if (!temp_list.contains(((TableStruct) (list.get(w)))
								.getSlmc()))
						{
							temp_list.add(((TableStruct) (list.get(w)))
									.getSlmc());
							ipIndex.add(ip_list.get(e) + "$$"
									+ ((TableStruct) (list.get(w))).getSlmc());
						}
					}
				}

				temp_list.clear();
			}

			// 初始化时间处理类
			Minute minute = null;
			long temp_minute = 0;
			// long tranfer_minute = 0;

			// 初始化折线
			TimeSeries[] timeseries = new TimeSeries[ipIndex.size()];

			String[] arr = new String[2];
			arr = null;

			for (int s = 0; s < ipIndex.size(); s++)
			{ // IP和Index循环
				arr = this.split(String.valueOf(ipIndex.get(s)), "$$");
				for (int f = 0; f < list.size(); f++)
				{
					if ((((TableStruct) (list.get(f))).getIp()).equals(arr[0])
							&& (((TableStruct) (list.get(f))).getSlmc())
									.equals(arr[1]))
					{
						if (!flag)
						{ // flag---false;timeseries[s]未初始化
							timeseries[s] = new TimeSeries(arr[0] + " 实例："
									+ arr[1], Minute.class);
							flag = true; // timeseries[s]初始化完成
						}

						// 为时间类赋值
						temp_minute = (Long.parseLong(((TableStruct) (list
								.get(f))).getCysj())) * 1000;

						minute = new Minute(new Date(temp_minute));

						// 获得坐标值
						y = Double.parseDouble(((TableStruct) (list.get(f)))
								.getAvgvalue());

						// if (temp_minute != transfer_minute) {
						if (!time_list.contains(minute))
						{

							// 向折线图time[s]中添加数据
							timeseries[s].add(minute, y);

							time_list.add(minute);
						}

						temp_list.add(list.get(f));
					}
				}

				// 删除已经选择的元素
				for (int k = 0; k < temp_list.size(); k++)
				{
					list.remove(temp_list.get(k));
				}

				// transfer_minute = -1;

				// flag复位,temp_list复位
				flag = false;
				temp_list.clear();
				time_list.clear();
			}

			for (int d = 0; d < ipIndex.size(); d++)
			{
				dataset.addSeries(timeseries[d]);
			}

			// cancel cache
			temp_list.clear();
			minute = null;
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
	public XYDataset createMovingAverageDayLineDemoDataset(ArrayList list)
	{

		TimeSeriesCollection dataset = new TimeSeriesCollection();

		try
		{
			ArrayList temp_list = new ArrayList();
			temp_list.clear();

			ArrayList time_list = new ArrayList();
			time_list.clear();

			ArrayList ip_list = new ArrayList();
			ip_list.clear();

			ArrayList ipIndex = new ArrayList();
			ipIndex.clear();

			double y = 0.0;

			boolean flag = false;

			// 分类设备IP数目
			for (int i = 0; i < list.size(); i++)
			{
				if (!ip_list.contains(((TableStruct) (list.get(i))).getIp()))
				{
					ip_list.add((((TableStruct) (list.get(i))).getIp()));
				}
			}

			// 分类各自IP下cpu数目
			for (int e = 0; e < ip_list.size(); e++)
			{ // ip iterator
				for (int w = 0; w < list.size(); w++)
				{
					if (ip_list.get(e).equals(
							((TableStruct) (list.get(w))).getIp()))
					{
						// 相同IP
						if (!temp_list.contains(((TableStruct) (list.get(w)))
								.getSlmc()))
						{
							temp_list.add(((TableStruct) (list.get(w)))
									.getSlmc());
							ipIndex.add(ip_list.get(e) + "$$"
									+ ((TableStruct) (list.get(w))).getSlmc());
						}
					}
				}

				temp_list.clear();
			}

			// 初始化时间处理类
			Minute minute = null;
			long temp_minute = 0;
			// long tranfer_minute = 0;

			// 初始化折线
			TimeSeries[] timeseries = new TimeSeries[ipIndex.size()];

			String[] arr = new String[2];
			arr = null;

			for (int s = 0; s < ipIndex.size(); s++)
			{ // IP和Index循环
				arr = this.split(String.valueOf(ipIndex.get(s)), "$$");
				for (int f = 0; f < list.size(); f++)
				{
					if ((((TableStruct) (list.get(f))).getIp()).equals(arr[0])
							&& (((TableStruct) (list.get(f))).getSlmc())
									.equals(arr[1]))
					{
						if (!flag)
						{ // flag---false;timeseries[s]未初始化
							timeseries[s] = new TimeSeries(arr[0] + " 实例："
									+ arr[1], Minute.class);
							flag = true; // timeseries[s]初始化完成
						}

						// 为时间类赋值
						temp_minute = (Long.parseLong(((TableStruct) (list
								.get(f))).getCysj())) * 1000;

						minute = new Minute(new Date(temp_minute));

						// 获得坐标值
						y = Double.parseDouble(((TableStruct) (list.get(f)))
								.getAvgvalue());

						// if (temp_minute != transfer_minute) {
						if (!time_list.contains(minute))
						{

							// 向折线图time[s]中添加数据
							timeseries[s].add(minute, y);

							time_list.add(minute);
						}

						temp_list.add(list.get(f));
					}
				}

				// 删除已经选择的元素
				for (int k = 0; k < temp_list.size(); k++)
				{
					list.remove(temp_list.get(k));
				}

				// transfer_minute = -1;

				// flag复位,temp_list复位
				flag = false;
				temp_list.clear();
				time_list.clear();
			}

			for (int d = 0; d < ipIndex.size(); d++)
			{
				dataset.addSeries(timeseries[d]);
			}

			// cancel cache
			temp_list.clear();
			minute = null;
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
	public XYDataset createMovingAverageHourLineDemoDataset(ArrayList list)
	{
		TimeSeriesCollection dataset = new TimeSeriesCollection();

		try
		{
			ArrayList temp_list = new ArrayList();
			temp_list.clear();

			ArrayList time_list = new ArrayList();
			time_list.clear();

			ArrayList ip_list = new ArrayList();
			ip_list.clear();

			ArrayList ipIndex = new ArrayList();
			ipIndex.clear();

			double y = 0.0;

			boolean flag = false;

			// 分类设备IP数目
			for (int i = 0; i < list.size(); i++)
			{
				if (!ip_list.contains(((TableStruct) (list.get(i))).getIp()))
				{
					ip_list.add((((TableStruct) (list.get(i))).getIp()));
				}
			}

			// 分类各自IP下cpu数目
			for (int e = 0; e < ip_list.size(); e++)
			{ // ip iterator
				for (int w = 0; w < list.size(); w++)
				{
					if (ip_list.get(e).equals(
							((TableStruct) (list.get(w))).getIp()))
					{
						// 相同IP
						if (!temp_list.contains(((TableStruct) (list.get(w)))
								.getSlmc()))
						{
							temp_list.add(((TableStruct) (list.get(w)))
									.getSlmc());
							ipIndex.add(ip_list.get(e) + "$$"
									+ ((TableStruct) (list.get(w))).getSlmc());
						}
					}
				}

				temp_list.clear();
			}

			// 初始化时间处理类
			Second minute = null;
			long temp_minute = 0;
			// long tranfer_minute = 0;

			// 初始化折线
			TimeSeries[] timeseries = new TimeSeries[ipIndex.size()];

			String[] arr = new String[2];
			arr = null;

			for (int s = 0; s < ipIndex.size(); s++)
			{ // IP和Index循环
				arr = this.split(String.valueOf(ipIndex.get(s)), "$$");
				for (int f = 0; f < list.size(); f++)
				{
					if ((((TableStruct) (list.get(f))).getIp()).equals(arr[0])
							&& (((TableStruct) (list.get(f))).getSlmc())
									.equals(arr[1]))
					{
						if (!flag)
						{ // flag---false;timeseries[s]未初始化
							timeseries[s] = new TimeSeries(arr[0] + " 实例："
									+ arr[1], Second.class);
							flag = true; // timeseries[s]初始化完成
						}

						// 为时间类赋值
						temp_minute = (Long.parseLong(((TableStruct) (list
								.get(f))).getCysj())) * 1000;

						minute = new Second(new Date(temp_minute));

						// 获得坐标值
						y = Double.parseDouble(((TableStruct) (list.get(f)))
								.getCsz());

						// if (temp_minute != transfer_minute) {
						if (!time_list.contains(minute))
						{

							// 向折线图time[s]中添加数据
							timeseries[s].add(minute, y);

							time_list.add(minute);
						}

						temp_list.add(list.get(f));
					}
				}

				// 删除已经选择的元素
				for (int k = 0; k < temp_list.size(); k++)
				{
					list.remove(temp_list.get(k));
				}

				// transfer_minute = -1;

				// flag复位,temp_list复位
				flag = false;
				temp_list.clear();
				time_list.clear();
			}

			for (int d = 0; d < ipIndex.size(); d++)
			{
				dataset.addSeries(timeseries[d]);
			}

			// cancel cache
			temp_list.clear();
			minute = null;
			timeseries = null;
		} catch (Exception e)
		{
			logger.error("错误：" + e.getMessage());
		}

		return dataset;
	}

	// generate Bar Chart
	public String generateMovingAverageDayTubeDemo(ArrayList list,
			HttpSession session, PrintWriter pw)
	{
		String filename = null;

		try
		{
			// Create and populate a CategoryDataset
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();

			// 向缓冲区填充数据
			for (int i = 0; i < list.size(); i++)
			{
				dataset.addValue(Double
						.parseDouble(((TableStruct) (list.get(i)))
								.getAvgvalue()), null, ((TableStruct) (list
						.get(i))).getIp()
						+ "/" + ((TableStruct) (list.get(i))).getSlmc());
			}

			// create the chart...
			JFreeChart chart = ChartFactory.createBarChart("主机性能示意图", // chart
					// title
					"实例呈现", // domain axis label
					"采样值", // range axis label
					dataset, // data
					PlotOrientation.VERTICAL, false, // include legend
					true, false);

			// set the background color for the chart...
			// chart.setBackgroundPaint(Color.lightGray);
			chart.setBackgroundPaint(Color.WHITE);

			// get a reference to the plot for further customisation...
			CategoryPlot plot = chart.getCategoryPlot();
			plot.setNoDataMessage("NO DATA!");
			// plot.setBackgroundImage(JFreeChart.INFO.getLogo());

			CategoryItemRenderer renderer = new CustomRenderer(new Paint[]
			{ Color.red, Color.blue, Color.green, Color.yellow, Color.orange,
					Color.cyan, Color.magenta, });
			renderer
					.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			renderer.setItemLabelsVisible(true);
			plot.setRenderer(renderer);

			// change the category labels to vertical...
			CategoryAxis domainAxis = plot.getDomainAxis();
			// domainAxis.setSkipCategoryLabelsToFit(true);

			// change the margin at the top of the range axis...
			ValueAxis rangeAxis = plot.getRangeAxis();
			rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			rangeAxis.setUpperMargin(0.15);

			// Write the chart image to the temporary directory
			ChartRenderingInfo info = new ChartRenderingInfo(
					new StandardEntityCollection());
			filename = ServletUtilities.saveChartAsPNG(chart, 750, 450, info,
					session);

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

	/**
	 * A custom renderer that returns a different color for each item in a
	 * single series.
	 */
	class CustomRenderer extends BarRenderer
	{

		/** The colors. */
		private Paint[] colors;

		/**
		 * Creates a new renderer.
		 * 
		 * @param colors
		 *            the colors.
		 */
		public CustomRenderer(Paint[] colors)
		{
			this.colors = colors;
		}

		/**
		 * Returns the paint for an item. Overrides the default behaviour
		 * inherited from AbstractRenderer.
		 * 
		 * @param row
		 *            the series.
		 * @param column
		 *            the category.
		 * 
		 * @return The item color.
		 */
		public Paint getItemPaint(int row, int column)
		{
			return colors[column % colors.length];
		}
	}

	// 静态方法，用于分割字符串，返回数组
	public String[] split(String s, String separator)
	{
		if (s == null)
			throw new NullPointerException("source String cannot be null");
		if (separator == null)
			throw new NullPointerException("separator cannot be null");
		if (separator.length() == 0)
			throw new IllegalArgumentException("separator cannot be empty");

		ArrayList tmp = new ArrayList();
		int start = 0;
		int separatorLen = separator.length();
		int end = s.indexOf(separator);

		while (end != -1)
		{
			tmp.add(s.substring(start, end));
			start = end + separatorLen;
			end = s.indexOf(separator, start);
		}

		tmp.add(s.substring(start, s.length()));
		String[] result = new String[tmp.size()];
		tmp.toArray(result);

		return result;
	}

}
