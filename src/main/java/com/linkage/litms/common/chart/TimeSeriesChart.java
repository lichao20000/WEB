/*
 * 创建日期 2004-10-25
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package com.linkage.litms.common.chart;

import java.awt.Color;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Month;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.database.Cursor;

/**
 * @author dolphin
 * 
 * 更改所生成类型注释的模板为 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
public class TimeSeriesChart extends CommonChart
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(TimeSeriesChart.class);

	private int step = 3;
	private int timeType = 1;
	private XYDataset dataset;
	private boolean blnVerticalTick = false;
	private boolean blnShowTick = true;
	private String pattern = null;
	private boolean blnShowShape = true;
	private boolean blnFill = true;
	private long space = 0;
	private DateAxis domainAxis = null; // 用于设置时间刻度
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

	public TimeSeriesChart()
	{
		super();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see )
	 */

	protected void createDataset()
	{
		int count = cursors.length;
		Class curClass = getTimeStepToClass();
		Cursor cursor;
		RegularTimePeriod o;
		Map fields;
		String timeStr, valueStr;
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		long time;
		ArrayList list = new ArrayList();
		for (int i = 0; i < count; i++)
		{
			TimeSeries ts = new TimeSeries(rowKeys[i], curClass);
			cursor = cursors[i];
			// m_logger.debug("cursor.getRecordSize() = "+cursor+"||");
			if (cursor == null)
			{
				continue;
			}
			fields = cursor.getNext();

			while (fields != null)
			{
				// 避免出现重新时间对象
				// if (list.contains(field.get("time"))) {
				if (list.contains(fields.get(xField)))
				{
					fields = cursor.getNext();
					continue;
				}
				timeStr = (String) fields.get(xField);
				valueStr = (String) fields.get(yField);
				if (valueStr == null || valueStr.equals("")
						|| valueStr.equals("null"))
					valueStr = "0";
				if (timeType == 1)
				{
					time = Long.parseLong(timeStr);
					if (i > 0 && count == 2)
					{
						time -= space;
						if (time < 0)
							time = 0;
					}

					o = (RegularTimePeriod) getTimeObject(time);
				} else
				{
					o = (RegularTimePeriod) getTimeObject(timeStr);
				}
				// logger.debug(o);

				if (valueType == 1)
				{
					ts.add(o, Integer.parseInt(formart("#", valueStr)));
				} else if (valueType == 2)
				{
					ts.add(o, Long.parseLong(formart("#", valueStr)));
				} else
				{
					ts.add(o, Double.parseDouble(formart(valueStr)));
				}

				fields = cursor.getNext();
			}
			dataset.addSeries(ts);
		}
		this.dataset = dataset;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.linkagesoftware.network.chart.CommonChart#createChart(org.jfree.data.Dataset)
	 */
	public String createChart(HttpSession session, PrintWriter pw)
	{
		String filename = null;
		try
		{
			createDataset();
			JFreeChart chart = ChartFactory.createTimeSeriesChart(title,
					xAxisLabel, yAxisLabel, dataset, true, true, false);

			// StandardLegend legend = (StandardLegend) chart.getLegend();
			// legend.setDisplaySeriesShapes(true);
			XYPlot plot = chart.getXYPlot();
			plot.setBackgroundPaint(new Color(232, 232, 232));
			plot.setDomainGridlinePaint(Color.BLACK);
			plot.setRangeGridlinePaint(Color.black);
			plot.setDomainCrosshairVisible(true);
			plot.setRangeCrosshairVisible(true);
			plot.setNoDataMessage("数据库中暂无数据！");
			// 测试Plot属性
			// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0,
			// 5.0));//是刻度与图分离
			DateAxis axis = (DateAxis) plot.getDomainAxis(); // 得到x轴
			// 格式化x轴坐标上的没个点，所以它的AutoTickUnitSelection为false
			// axis.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 1,new
			// SimpleDateFormat("MM-dd")));
			axis.setVerticalTickLabels(blnVerticalTick); // 是否x轴坐标竖向写
			axis.setAutoTickUnitSelection(blnShowTick); // 是否自动显示x轴的点
			// 格式化x轴坐标，这时AutoTickUnitSelection为true
			if (pattern != null)
				axis.setDateFormatOverride(new SimpleDateFormat(pattern));
			// axis.setLabel("Demo1"); //设置x轴上的Label

			// 设置横轴上的时间刻度的显示格式
			if (domainAxis != null)
			{
				plot.setDomainAxis(domainAxis);
			}

			NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
			yAxis.setAutoRange(true);
			// yAxis.setLabelFont(new Font("宋体",Font.PLAIN,12));
			XYItemRenderer renderer = plot.getRenderer();

			if (renderer instanceof StandardXYItemRenderer)
			{
				StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
				// rr.setPlotShapes(blnShowShape);
				rr.setShapesFilled(blnFill);
			}

			chart.setBackgroundPaint(java.awt.Color.white);

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

	public JFreeChart createChart()
	{
		createDataset();
		JFreeChart chart = ChartFactory.createTimeSeriesChart(title,
				xAxisLabel, yAxisLabel, dataset, true, true, false);

		// StandardLegend legend = (StandardLegend) chart.getLegend();
		// legend.setDisplaySeriesShapes(true);
		XYPlot plot = chart.getXYPlot();
		// 测试Plot属性
		// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0,
		// 5.0));//是刻度与图分离
		DateAxis axis = (DateAxis) plot.getDomainAxis(); // 得到x轴
		// 格式化x轴坐标上的没个点，所以它的AutoTickUnitSelection为false
		// axis.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 1,new
		// SimpleDateFormat("MM-dd")));
		axis.setVerticalTickLabels(blnVerticalTick); // 是否x轴坐标竖向写
		axis.setAutoTickUnitSelection(blnShowTick); // 是否自动显示x轴的点

		// 格式化x轴坐标，这时AutoTickUnitSelection为true
		// y轴属性
		// NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
		// yAxis.setLabelFont(new Font("dialog",Font.PLAIN,12));
		if (pattern != null)
			axis.setDateFormatOverride(new SimpleDateFormat(pattern));
		// axis.setLabel("Demo1"); //设置x轴上的Label

		XYItemRenderer renderer = plot.getRenderer();

		if (renderer instanceof StandardXYItemRenderer)
		{
			StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
			// logger.debug(blnShowShape);
			// rr.setPlotShapes(blnShowShape); // 是否画点－是
			rr.setShapesFilled(blnFill); // 是否填充点－否
			/*
			 * 设置Series的颜色 rr.setSeriesPaint(0,Color.RED);
			 * rr.setSeriesPaint(1,Color.GREEN);
			 */
		}

		chart.setBackgroundPaint(java.awt.Color.white);

		return chart;
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

	public void setTimeStep(int step)
	{
		this.step = step;
	}

	public void setTimeType(int type)
	{
		this.timeType = type;
	}

	public Class getTimeStepToClass()
	{
		switch (step)
		{
		case 1:
			return Year.class;
		case 2:
			return Month.class;
		case 3:
			return Day.class;
		case 4:
			return Hour.class;
		case 5:
			return Minute.class;
		default:
			return null;
		}
	}

	public Object getTimeObject(Date dt)
	{
		switch (step)
		{
		case 1:
			return new Year(dt);
		case 2:
			return new Month(dt);
		case 3:
			return new Day(dt);
		case 4:
			return new Hour(dt);
		case 5:
			return new Minute(dt);
		default:
			return null;
		}
	}

	public Object getTimeObject(long sec)
	{
		long lms = sec * 1000;
		Date dt = new Date(lms);
		return getTimeObject(dt);
	}

	public Object getTimeObject(String dtStr)
	{
		Date dt = new Date();
		String pattern = "yyyy-MM-dd";
		if (step > 3)
			pattern = "yyyy-MM-dd HH:mm:ss";

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try
		{
			dt = sdf.parse(dtStr);
			// logger.debug(dt.getTime());
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return getTimeObject(dt);
	}

	/**
	 * @param b
	 */
	public void setBlnShowTick(boolean b)
	{
		blnShowTick = b;
	}

	/**
	 * @param b
	 */
	public void setBlnVerticalTick(boolean b)
	{
		blnVerticalTick = b;
	}

	/**
	 * @param string
	 */
	public void setPattern(String string)
	{
		pattern = string;
	}

	/**
	 * @param b
	 */
	public void setBlnFill(boolean b)
	{
		blnFill = b;
	}

	/**
	 * @param b
	 */
	public void setBlnShowShape(boolean b)
	{
		blnShowShape = b;
	}

	/**
	 * @param i
	 */
	public void setSpace(long i)
	{
		space = i;
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
		domainAxis = new DateAxis();
		DateFormat df = new SimpleDateFormat(formatter);
		DateTickUnit dateTickUnit = new DateTickUnit(unit, count, df);
		domainAxis.setTickUnit(dateTickUnit);
		domainAxis.setVerticalTickLabels(true);
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
}
