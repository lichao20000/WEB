/*
 * 创建日期 2005-11-9
 * modify by hemc 2006-9/2006-10
 *
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.linkage.litms.common.chart;

import java.awt.Color;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Month;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.util.DateTimeUtil;

public class LineVolumeChart extends CommonChart
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(LineVolumeChart.class);
	private Cursor dataset_V = null; // 柱状图数据源

	private Cursor dataset_H = null; // 折线图数据源

	private String object_V = "流出流量"; // MRTG图中曲线title名称

	private String object_H = "流入流量"; // MRTG图中区域title名称

	private String _title = null; // 图示大标题

	private int timeType = 0; // 时间类型，缺省为分钟；1-小时；2-天；3-月；4-秒

	private Map timeFormat = null;

	private DateAxis domainAxis = null; // 用于设置时间刻度

	private XYDataset timeSeriesCollection;

	private boolean vertical = true; // 是否x轴坐标竖向写

	private boolean inverted = false; // 数据轴是否反向

	private boolean positiveArrowVisible = false; // 是否显示正向箭头

	private boolean negativeArrowVisible = false; // 是否显示反向箭头

	private boolean legend = false;

	private int width = 500;

	private int height = 250;

	private boolean average = false; // 是否取曲线的平均值 默认为不去平均值

	// private int suffix = 1; //用于createMovingAverage函数中参数suffix

	private int period = 1;// 用于createMovingAverage函数中参数period

	private int skip = 0;// 用于createMovingAverage函数中参数skip

	private boolean toolTip = false; // 是否需要鼠标悬停提示 默认为false

	/**
	 * 用于设置MRTG图上的垂直标尺
	 */
	private ValueMarker valueMarker = null;

	/**
	 * 用于设置MRTG图上的水平标尺(流入)
	 */
	private ValueMarker inlevelMarker = null;
	/**
	 * 用于设置MRTG图上的水平标尺(流出)
	 */
	private ValueMarker outlevelMarker = null;
	private SimpleDateFormat dateFormate = null;

	public void setHeight(int height)
	{
		this.height = height;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public LineVolumeChart()
	{
		timeFormat = new HashMap();
		timeFormat.put("0", "yyyy-MMM-d hh-mm");
		timeFormat.put("1", "yyyy-MMM-d hh");
		timeFormat.put("2", "yyyy-MMM-d");
		timeFormat.put("3", "yyyy-MMM");
		timeFormat.put("4", "yyyy-MMM-d hh-mm-ss");

		// 初始化
	}

	public String showChart(HttpSession session, PrintWriter pw)
	{
		String filename = null;

		try
		{
			JFreeChart chart = createChart();
			// chart.setBackgroundPaint(java.awt.Color.white);

			// Write the chart image to the temporary directory
			ChartRenderingInfo info = new ChartRenderingInfo(
					new StandardEntityCollection());
			// logger.debug("测试LineVolumeChart" + session.toString());
			filename = ServletUtilities.saveChartAsPNG(chart, width, height,
					info, session);
			logger.debug(filename);
			// Write the image map to the PrintWriter
			ChartUtilities.writeImageMap(pw, filename, info, true);
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
	 * 创建平均值曲线(达到曲线平滑的效果)
	 * 
	 * @param xyDataSet
	 * @param valueAxisLabel
	 * @return
	 */
	public XYDataset getMovingAverage(XYDataset xyDataSet, String suffix,
			long period, long skip)
	{
		// logger.debug("getMovingAverage. getItemCount= " +
		// xyDataSet.getItemCount(0));
		int count = xyDataSet.getItemCount(0);
		if (count < 1)
			return xyDataSet;
		return MovingAverage.createMovingAverage(xyDataSet, suffix, period,
				skip);
	}

	/**
	 * 创建平均值曲线(达到曲线平滑的效果)
	 * 
	 * @param timeSeries
	 * @param valueLable
	 * @return
	 */
	public TimeSeries getMovingAverage(TimeSeries timeSeries, String suffix,
			int period, int skip)
	{
		int count = timeSeries.getItemCount();
		if (count < 1)
			return timeSeries;
		return MovingAverage.createMovingAverage(timeSeries, suffix, period,
				skip);
	}

	public XYDataset getAvgDataSetByConfig(XYDataset dataset, String suffix)
	{
		if (average)
			return this.getMovingAverage(dataset, suffix, this.period,
					this.skip);
		else
			return dataset;
	}

	/**
	 * 用于创建MRTG图 请参考山东网通webtopo链路流量 生成后的MRTG图效果
	 * 
	 * @return
	 */
	private JFreeChart createChart()
	{
		JFreeChart jfreechart = null;
		try
		{
			XYDataset xydataset = createDirectionDataset();// getAvgDataSetByConfig(tmpXYDataSet,"AVG1");
			jfreechart = ChartFactory.createTimeSeriesChart(this._title, "时间",
					null, xydataset, legend, toolTip, false);// legend
			// tooltip
			// url
			// jfreechart = ChartFactory.createTimeSeriesChart(this._title,"时间",
			// this.object_V,xydataset,false,false,false);
			// jfreechart.setBackgroundPaint(Color.lightGray);
			XYPlot xyplot = jfreechart.getXYPlot();
			// 设置背景
			jfreechart.setBackgroundPaint(java.awt.Color.white);
			// XYItemRenderer xyItemRenderer = xyplot.getRenderer();
			xyplot.getDomainAxis().setLowerMargin(0.0D);
			xyplot.getDomainAxis().setUpperMargin(0.0D);
			xyplot.setBackgroundPaint(new Color(232, 232, 232));
			xyplot.setDomainGridlinePaint(Color.BLACK);
			xyplot.setRangeGridlinePaint(Color.black);
			xyplot.setDomainCrosshairVisible(true);
			xyplot.setRangeCrosshairVisible(true);
			xyplot.setForegroundAlpha(0.7F);
			/** ************** hemc 设置垂直标尺***************** */
			if (this.valueMarker != null)
			{
				// logger.debug("设置标尺 start");
				xyplot.addDomainMarker(this.valueMarker);
				valueMarker = null;
				// logger.debug("设置标尺 end");
			}
			/** ********************************************* */

			/** ************** xiaoxf 设置水平标尺***************** */
			if (this.inlevelMarker != null)
			{
				// logger.debug("设置水平标尺 start");
				xyplot.addRangeMarker(this.inlevelMarker);
				inlevelMarker = null;
				// logger.debug("设置水平标尺 end");
			}
			if (this.outlevelMarker != null)
			{
				// logger.debug("设置水平标尺 start");
				xyplot.addRangeMarker(this.outlevelMarker);
				outlevelMarker = null;
				// logger.debug("设置水平标尺 end");
			}
			/** ********************************************* */
			// 背景方格
			// 5,设置水平网格线颜色
			xyplot.setDomainGridlinesVisible(true);
			// 6,设置是否显示水平网格线
			xyplot.setRangeGridlinesVisible(true);
			// 无数据时现实信息
			xyplot.setNoDataMessage("数据库中暂无数据！");
			/** *****************设置X轴(domain)*********************** */
			// 设置时间刻度
			if (domainAxis != null)
			{
				// logger.debug("设置时间轴刻度.");
				xyplot.setDomainAxis(domainAxis);
				domainAxis = null;
			}
			// 设置是否x轴坐标竖向写
			DateAxis axis = (DateAxis) xyplot.getDomainAxis(); // 得到x轴
			axis.setVerticalTickLabels(this.vertical); // 是否x轴坐标竖向写
			setVertical(true);// 复位vertical值
			/** ****************设置坐标轴参数****************** */
			axis.setInverted(this.inverted);
			axis.setAxisLinePaint(Color.red);
			axis.setPositiveArrowVisible(this.positiveArrowVisible);
			axis.setNegativeArrowVisible(this.negativeArrowVisible);

			/** ******************************************************* */

			/** *****************设置Y轴(Range)*********************** */
			NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
			numberaxis.setAutoRangeIncludesZero(false);
			DecimalFormat format = new DecimalFormat("00.00 M");
			numberaxis.setNumberFormatOverride(format);
			xyplot.setRangeAxis(numberaxis);
			XYAreaRenderer xyarearenderer = new XYAreaRenderer();
			// NumberAxis numberaxis1 = new NumberAxis(this.object_H);
			// numberaxis1.setNumberFormatOverride(format);
			/** ******************************************************* */

			/** ********suzr******* */
			StandardXYItemRenderer xYLineAndShapeRenderer = new StandardXYItemRenderer();
			// 是曲线显示点
			// xYLineAndShapeRenderer.setPlotShapes(true);
			xYLineAndShapeRenderer.setSeriesPaint(0, new Color(2, 1, 252));
			/** **给曲线增加提示*** */
			if (toolTip)
			{// 当需要提示时
				dateFormate = dateFormate == null ? new SimpleDateFormat(
						"yyyy-MM-dd") : dateFormate;
				StandardXYToolTipGenerator standardxytooltipgenerator = new StandardXYToolTipGenerator(
						"({0},{1})= {2}", dateFormate, new DecimalFormat(
								"0.000000 M"));
				xYLineAndShapeRenderer
						.setToolTipGenerator(standardxytooltipgenerator);
				xyarearenderer.setToolTipGenerator(standardxytooltipgenerator);
			}
			/** ******************* */
			xyplot.setRenderer(0, xYLineAndShapeRenderer);
			xyarearenderer.setSeriesPaint(0, new Color(6, 209, 7));
			/** *************** */

			xyplot.setDataset(1, createForceDataset());
			xyplot.setRenderer(1, xyarearenderer);
			// xyplot.setRangeAxis(1,numberaxis);
			// xyplot.mapDatasetToRangeAxis(1,1);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return jfreechart;
	}

	/**
	 * 用于创建MRTG图中曲线图形的数据源
	 * 
	 * @return
	 */
	private XYDataset createDirectionDataset()
	{
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		try
		{
			TimeSeries timeseries = new TimeSeries(object_V,
					(timeType == 0) ? Minute.class
							: ((timeType == 1) ? Hour.class
									: ((timeType == 2) ? Day.class
											: ((timeType == 3) ? Month.class
													: Second.class))));

			Date date = null;
			// ArrayList list = new ArrayList();
			// list.clear();
			Map field = null;
			if (dataset_V != null)
			{
				field = dataset_V.getNext();
			}
			while (field != null)
			{
				// 避免出现重新时间对象
				// if (list.contains(field.get("time"))) {
				/*
				 * if (list.contains(field.get(xField))) { field =
				 * dataset_V.getNext(); continue; }
				 */
				// 添加时间对象到链表中
				// list.add(field.get("time"));
				// list.add(field.get(xField));
				// date = new Date(Long.parseLong((String) field.get("time")) *
				// 1000l);
				date = new Date(
						Long.parseLong((String) field.get(xField)) * 1000l);
				if (timeType == 0)
				{// Minute
					// timeseries.add(new
					// Minute(date),Double.parseDouble((String)
					// field.get("value")));
					timeseries.add(new Minute(date), Double
							.parseDouble((String) field.get(yField)));
				}

				if (timeType == 1)
				{// Hour
					// timeseries.add(new Hour(date),
					// Double.parseDouble((String) field.get("value")));
					timeseries.addOrUpdate(new Hour(date), Double
							.parseDouble((String) field.get(yField)));
				}

				if (timeType == 2)
				{// Day
					timeseries.addOrUpdate(new Day(date), Double
							.parseDouble((String) field.get(yField)));
				}

				if (timeType == 3)
				{// Month
					// timeseries.add(new Month(date),
					// Double.parseDouble((String) field.get("value")));
					timeseries.addOrUpdate(new Month(date), Double
							.parseDouble((String) field.get(yField)));
				}
				if (timeType == 4)
				{// Second
					// timeseries.add(new Second(date),
					// Double.parseDouble((String) field.get("value")));
					timeseries.addOrUpdate(new Second(date), Double
							.parseDouble((String) field.get(yField)));
				}

				// getNext
				field = dataset_V.getNext();
			}

			date = null;
			// list.clear();
			// list = null;
			timeseriescollection.addSeries(timeseries);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return timeseriescollection;
	}

	/**
	 * 用于创建MRTG图中区域图形的数据源
	 * 
	 * @return
	 */
	private XYDataset createForceDataset()
	{
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		try
		{
			TimeSeries timeseries = new TimeSeries(object_H,
					(timeType == 0) ? Minute.class
							: ((timeType == 1) ? Hour.class
									: ((timeType == 2) ? Day.class
											: ((timeType == 3) ? Month.class
													: Second.class))));

			Date date = null;
			// ArrayList list = new ArrayList();
			// list.clear();

			Map field = null;
			if (dataset_H != null)
			{
				field = dataset_H.getNext();
			}
			while (field != null)
			{
				// logger.debug((field.get("TIME")));
				// 避免出现重新时间对象
				// logger.debug(list.contains(field.get("TIME")));
				// if (list.contains(field.get("time"))) {xField
				/*
				 * if (list.contains(field.get(xField))) { field =
				 * dataset_H.getNext();
				 * 
				 * continue; }
				 */
				// 添加时间对象到链表中
				// list.add(field.get("time"));
				// list.add(field.get(xField));
				date = new Date(
				// Long.parseLong((String) field.get("time")) * 1000l);
						Long.parseLong((String) field.get(xField)) * 1000l);
				// logger.debug(date.before(tmp_date));
				// tmp_date = new Date(Long.parseLong((String)
				// field.get("TIME")));;
				if (timeType == 0)
				{// Minute
					// timeseries.add(new
					// Minute(date),Double.parseDouble((String)
					// field.get("value")));
					timeseries.addOrUpdate(new Minute(date), Double
							.parseDouble((String) field.get(yField)));
				}

				if (timeType == 1)
				{// Hour
					// timeseries.add(new Hour(date),
					// Double.parseDouble((String) field.get("value")));
					timeseries.addOrUpdate(new Hour(date), Double
							.parseDouble((String) field.get(yField)));
				}

				if (timeType == 2)
				{// Day
					// timeseries.add(new Day(date), Double.parseDouble((String)
					// field.get("value")));
					timeseries.addOrUpdate(new Day(date), Double
							.parseDouble((String) field.get(yField)));
				}

				if (timeType == 3)
				{// Month
					// timeseries.add(new Month(date),
					// Double.parseDouble((String) field.get("value")));
					timeseries.addOrUpdate(new Month(date), Double
							.parseDouble((String) field.get(yField)));
				}
				if (timeType == 4)
				{// Second
					// timeseries.add(new Second(date),
					// Double.parseDouble((String) field.get("value")));
					timeseries.addOrUpdate(new Second(date), Double
							.parseDouble((String) field.get(yField)));
				}
				// getNext
				field = dataset_H.getNext();
			}

			// series1.add(new Day(2, SerialDate.JANUARY, 2002), 41020);
			//
			date = null;
			/*
			 * list.clear(); list = null;
			 */
			timeseriescollection.addSeries(timeseries);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return timeseriescollection;
	}

	/**
	 * @return 返回 dataset_H。
	 */
	public Cursor getDataset_H()
	{
		return dataset_H;
	}

	/**
	 * @param dataset_H
	 *            要设置的 dataset_H。
	 */
	public void setDataset_H(Cursor dataset_H)
	{
		this.dataset_H = dataset_H;
	}

	/**
	 * @return 返回 dataset_V。
	 */
	public Cursor getDataset_V()
	{
		return dataset_V;
	}

	/**
	 * @param dataset_V
	 *            要设置的 dataset_V。
	 */
	public void setDataset_V(Cursor dataset_V)
	{
		this.dataset_V = dataset_V;
	}

	/**
	 * @return 返回 object_H。
	 */
	public String getObject_H()
	{
		return object_H;
	}

	/**
	 * @param object_H
	 *            要设置的 object_H。
	 */
	public void setObject_H(String object_H)
	{
		this.object_H = object_H;
	}

	/**
	 * @return 返回 object_V。
	 */
	public String getObject_V()
	{
		return object_V;
	}

	/**
	 * @param object_V
	 *            要设置的 object_V。
	 */
	public void setObject_V(String object_V)
	{
		this.object_V = object_V;
	}

	/**
	 * @return 返回 title。
	 */
	public String getTitle()
	{
		return _title;
	}

	/**
	 * @param title
	 *            要设置的 title。
	 */
	public void setTitle(String title)
	{
		this._title = title;
	}

	/**
	 * @return 返回 timeType。
	 */
	public int getTimeType()
	{
		return timeType;
	}

	/**
	 * 设置标尺 天(小时)
	 * 
	 * @param dateTime
	 *            用于设置标尺 传入时间点
	 */
	public void setHourMarker(long second)
	{
		DateTimeUtil dateTimeUtil = new DateTimeUtil(second * 1000);
		// YYYY-MM-dd 的凌晨零点零分为分界线
		Hour hour = new Hour(0, new Day(dateTimeUtil.getDateTime()));
		double d = hour.getFirstMillisecond();
		this.valueMarker = new ValueMarker(d);
		valueMarker.setPaint(Color.red);
		valueMarker.setLabelAnchor(RectangleAnchor.TOP_LEFT);
		valueMarker.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
	}

	/**
	 * 设置标尺
	 * 
	 * @param number
	 *            用于设置水平标尺 传入Y轴的值 流入=流出设置一个标尺
	 */
	public void setLevelMarker(double value)
	{
		setLevelMarker(value, value);
	}

	/**
	 * @param 流入
	 * @param 流出
	 *            用于设置水平标尺 传入Y轴的值
	 */
	public void setLevelMarker(double invalue, double outvalue)
	{
		// 流入=流出 只设置一个 水平标尺
		if (invalue == outvalue)
		{
			this.inlevelMarker = new ValueMarker(invalue);
			inlevelMarker.setPaint(Color.RED);
			inlevelMarker.setLabelAnchor(RectangleAnchor.TOP_LEFT);
			inlevelMarker.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
		} else
		{
			this.inlevelMarker = new ValueMarker(invalue);
			inlevelMarker.setPaint(Color.GREEN);
			inlevelMarker.setLabelAnchor(RectangleAnchor.TOP_LEFT);
			inlevelMarker.setLabelTextAnchor(TextAnchor.TOP_RIGHT);

			this.outlevelMarker = new ValueMarker(outvalue);
			outlevelMarker.setPaint(Color.BLUE);
			outlevelMarker.setLabelAnchor(RectangleAnchor.TOP_LEFT);
			outlevelMarker.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
		}

	}

	/**
	 * @param timeType
	 *            要设置的 timeType。
	 */
	public void setTimeType(int timeType)
	{
		this.timeType = timeType;
	}

	/**
	 * 是否x轴坐标竖向写
	 * 
	 * @param arg0
	 */
	public void setVertical(boolean arg0)
	{
		this.vertical = arg0;
	}

	/**
	 * 设置时间刻度
	 * 
	 * @author hemc
	 * @param formatter
	 *            yyyy-MM-dd HH:mi:ss
	 * @param type
	 *            1:year 2:month 3:day 4:hour 5:minute 6:second
	 * @param count
	 *            the unit count
	 */
	public void setDomainAxis(String formatter, int type, int count)
	{
		int unit = getUnit(type);
		logger.debug("setDomainAxis begin.");
		try
		{
			domainAxis = new DateAxis();
			logger.debug("setDomainAxis construct");
			domainAxis.setLowerMargin(0.0D);
			domainAxis.setUpperMargin(0.0D);
			logger.debug("ccccccccccccccccc");
			DateFormat df = new SimpleDateFormat(formatter);
			// dateFormate = new SimpleDateFormat(formatter);
			DateTickUnit dateTickUnit = new DateTickUnit(unit, count, df);
			domainAxis.setTickUnit(dateTickUnit);
		} catch (RuntimeException e)
		{
			e.printStackTrace();
			domainAxis = null;
		}
	}

	public void setDomainAxis(String formatter)
	{
		try
		{
			domainAxis = new DateAxis();
			domainAxis.setLowerMargin(0.0D);
			domainAxis.setUpperMargin(0.0D);
			DateFormat df = new SimpleDateFormat(formatter);
			// dateFormate = new SimpleDateFormat(formatter);
			domainAxis.setDateFormatOverride(df);
		} catch (Exception e)
		{
			e.printStackTrace();
			domainAxis = null;
		}
	}

	/**
	 * 设置 DateTickUnit.YEAR 用于setDomainAxis方法中
	 * 
	 * @author hemc
	 * @param type
	 *            时间类型
	 * @return 返回DateTickUnit的静态变量
	 */
	private int getUnit(int type)
	{
		int returnValue = DateTickUnit.YEAR;
		switch (type)
		{
		case 1:
			returnValue = DateTickUnit.YEAR;
			break;
		case 2:
			returnValue = DateTickUnit.MONTH;
			break;
		case 3:
			returnValue = DateTickUnit.DAY;
			break;
		case 4:
			returnValue = DateTickUnit.HOUR;
			break;
		case 5:
			returnValue = DateTickUnit.MINUTE;
			break;
		case 6:
			returnValue = DateTickUnit.SECOND;
			break;
		}
		return returnValue;
	}

	/**
	 * @author hemc 使用已经传入的cursor数组
	 */
	protected void createDataset()
	{
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		int count = cursors.length;
		String timeStr, valueStr;
		Cursor cursor = null;
		Map fields = null;
		Date date = null;
		// ArrayList list = new ArrayList();
		Class curClass = (timeType == 0) ? Minute.class
				: ((timeType == 1) ? Hour.class : ((timeType == 2) ? Day.class
						: ((timeType == 3) ? Month.class : Second.class)));
		try
		{
			for (int i = 0; i < count; i++)
			{
				TimeSeries timeseries = new TimeSeries(rowKeys[i], curClass);
				// list.clear();
				cursor = cursors[i];
				if (cursor == null)
				{
					continue;
				}
				fields = cursor.getNext();
				while (fields != null)
				{
					timeStr = (String) fields.get(xField);
					valueStr = (String) fields.get(yField);
					if (valueStr == null || valueStr.equals("")
							|| valueStr.equals("null"))
						valueStr = "0";
					// 避免出现重新时间对象
					// logger.debug(list.contains(field.get("TIME")));
					// if(list.contains(fields.get("time")))
					// {
					// fields = dataset_H.getNext();
					//
					// continue;
					// }
					// 添加时间对象到链表中
					// list.add(fields.get("time"));

					date = new Date(Long.parseLong(timeStr) * 1000l);
					if (timeType == 0)
					{// Minute
						timeseries.addOrUpdate(new Minute(date), Double
								.parseDouble(valueStr));
					}

					if (timeType == 1)
					{// Hour
						timeseries.addOrUpdate(new Hour(date), Double
								.parseDouble(valueStr));
					}

					if (timeType == 2)
					{// Day
						timeseries.addOrUpdate(new Day(date), Double
								.parseDouble(valueStr));
					}

					if (timeType == 3)
					{// Month
						timeseries.addOrUpdate(new Month(date), Double
								.parseDouble(valueStr));
					}
					if (timeType == 4)
					{// Second
						timeseries.addOrUpdate(new Second(date), Double
								.parseDouble(valueStr));
					}
					// getNext
					fields = cursor.getNext();
				}
				timeseriescollection.addSeries(timeseries);
			}
			date = null;
			fields = null;
			// list = null;
			cursor = null;
			this.timeSeriesCollection = timeseriescollection;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 用于创建图形 多条曲线
	 * 
	 * @author hemc
	 */
	public String createChart(HttpSession session, PrintWriter pw)
	{
		JFreeChart jfreechart = null;
		String filename = null;
		try
		{
			this.createDataset();
			XYDataset xydataset = this.timeSeriesCollection;
			jfreechart = ChartFactory.createTimeSeriesChart(title, xAxisLabel,
					yAxisLabel, xydataset, legend, toolTip, false);
			XYPlot xyplot = jfreechart.getXYPlot();
			xyplot.getDomainAxis().setLowerMargin(0.0D);
			xyplot.getDomainAxis().setUpperMargin(0.0D);
			xyplot.setBackgroundPaint(new Color(232, 232, 232));
			xyplot.setDomainGridlinePaint(Color.BLACK);
			xyplot.setRangeGridlinePaint(Color.black);
			xyplot.setDomainCrosshairVisible(true);
			xyplot.setRangeCrosshairVisible(true);
			xyplot.setNoDataMessage("数据库中暂无数据！");
			// 设置前景alpha值
			xyplot.setForegroundAlpha(1.0F);

			/** ************** hemc 设置标尺***************** */
			if (this.valueMarker != null)
			{
				// logger.debug("设置标尺 start");
				xyplot.addDomainMarker(this.valueMarker);
				valueMarker = null;
				// logger.debug("设置标尺 end");
			}
			/** ********************************************* */
			NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
			numberaxis.setAutoRangeIncludesZero(false);
			DecimalFormat format = new DecimalFormat("00.0");
			numberaxis.setNumberFormatOverride(format);
			xyplot.setRangeAxis(numberaxis);
			// 设置时间刻度
			if (domainAxis != null)
			{
				xyplot.setDomainAxis(domainAxis);
				domainAxis = null;
			}
			// 设置是否x轴坐标竖向写
			DateAxis axis = (DateAxis) xyplot.getDomainAxis(); // 得到x轴
			axis.setVerticalTickLabels(this.vertical); // 是否x轴坐标竖向写
			setVertical(true);// 复位vertical值
			/** ****************设置坐标轴参数****************** */
			axis.setInverted(this.inverted);
			axis.setPositiveArrowVisible(this.positiveArrowVisible);
			axis.setNegativeArrowVisible(this.negativeArrowVisible);

			/** **给曲线增加提示*** 性能暂时不提示 */
			/*
			 * if(toolTip){//当需要提示时 dateFormate = dateFormate == null ? new
			 * SimpleDateFormat("yyyy-MM-dd") : dateFormate;
			 * StandardXYToolTipGenerator standardxytooltipgenerator = new
			 * StandardXYToolTipGenerator("({0},{1})= {2}", dateFormate,format);
			 * xYLineAndShapeRenderer.setToolTipGenerator(standardxytooltipgenerator); }
			 */
			jfreechart.setBackgroundPaint(java.awt.Color.white);
			// Write the chart image to the temporary directory
			ChartRenderingInfo info = new ChartRenderingInfo(
					new StandardEntityCollection());
			filename = ServletUtilities.saveChartAsPNG(jfreechart, width,
					height, info, session);
			logger.debug(filename);
			// Write the image map to the PrintWriter
			ChartUtilities.writeImageMap(pw, filename, info, true);
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
	 * 数据轴是否反向（默认为false）
	 * 
	 * @param inverted
	 */
	public void setInverted(boolean inverted)
	{
		this.inverted = inverted;
	}

	/***************************************************************************
	 * 是否显示正向箭头（3D轴无效） 默认为false
	 * 
	 * @param negativeArrowVisible
	 */
	public void setNegativeArrowVisible(boolean negativeArrowVisible)
	{
		this.negativeArrowVisible = negativeArrowVisible;
	}

	/**
	 * 是否显示反向箭头（3D轴无效）默认为false
	 * 
	 * @param positiveArrowVisible
	 */
	public void setPositiveArrowVisible(boolean positiveArrowVisible)
	{
		this.positiveArrowVisible = positiveArrowVisible;
	}

	/**
	 * 设置是否取曲线的平均值
	 * 
	 * @param average
	 *            true/false
	 * @param suffix
	 *            {@link}createMovingAverage
	 * @param skip
	 *            {@link}createMovingAverage
	 */
	public void setAverage(boolean average, int period, int skip)
	{
		this.average = average;
		this.period = period;
		this.skip = skip;
	}

	/**
	 * 设置是否取曲线的平均值
	 * 
	 * @param average
	 */
	public void setAverage(boolean average)
	{
		setAverage(average, 1, 0);
	}

	public static void main(String[] args)
	{
	}

	/**
	 * 设置MRTG图是否产生提示信息
	 * 
	 * @param toolTip
	 * @param format
	 *            时间格式 yyyy-MM-dd HH:mm:ss
	 */
	public void setToolTip(boolean toolTip, String format)
	{
		this.toolTip = toolTip;
		if (toolTip)
		{
			format = "".equals(format) || format == null ? "yyyy-MM-dd" : format;
			this.dateFormate = new SimpleDateFormat(format);
		}
	}

	/**
	 * 设置MRTG图是否产生提示信息
	 * 
	 * @param toolTip
	 *            需提示则为true,否则为false. 如果为true,则默认时间格式为yyy-MM-dd
	 */
	public void setToolTip(boolean toolTip)
	{
		setToolTip(toolTip, null);
	}

	/**
	 * 设置MRTG图下方是否曲线图列(legend)
	 * 
	 * @param legend
	 */
	public void setLegend(boolean legend)
	{
		this.legend = legend;
	}
}
