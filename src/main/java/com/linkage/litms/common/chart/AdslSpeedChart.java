/*
 * Created on 2003-12-17
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.linkage.litms.common.chart;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.database.Cursor;

/**
 * @author yuht
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AdslSpeedChart
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(AdslSpeedChart.class);
	public AdslSpeedChart()
	{

	}

	public String RawChart(Cursor cursor, HttpSession session, PrintWriter pw,
			String kind, String cominfo)
	{
		String filename = null;
		try
		{
			// create a title...
			String chartTitle = "ADSL上网通道速率达标率";
			XYDataset dataset = createRawChartDataset(cursor, kind, cominfo);

			JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle,
					"日期", "单位：百分比", dataset, true, true, false);

			// StandardLegend legend = (StandardLegend) chart.getLegend();
			// LegendTitle legend = chart.getLegend(0);
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

		} catch (Exception e)
		{
			logger.warn("Exception - " + e.toString());
			e.printStackTrace();
			filename = "public_error_500x300.png";
		}
		return filename;
	}

	private XYDataset createRawChartDataset(Cursor cursor, String kind,
			String cominfo)
	{
		double value1 = 0.0;
		Map fields;
		long lms = 0L;
		// Minute m,oldm=null;
		Hour h;
		TimeSeries ts;
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		ts = new TimeSeries(cominfo, Hour.class);
		fields = cursor.getNext();
		while (fields != null)
		{
			lms = Long.parseLong((String) fields
					.get("STATISTIME".toLowerCase())) * 1000;
			value1 = Double
					.parseDouble((String) fields.get(kind.toUpperCase())) * 100.00;
			h = new Hour(new Date(lms));

			// if(oldh==null || h.getHour()!=oldh.getHour()){
			ts.add(h, value1);
			// oldh = h;
			// }
			fields = cursor.getNext();
		}
		dataset.addSeries(ts);
		return dataset;
	}

	public String DetailComChart(String title, Cursor cursor,
			HttpSession session, PrintWriter pw, String kind, String[] cominfo,
			String timepoints)
	{
		String filename = null;
		try
		{
			// create a title...
			String chartTitle = title;
			XYDataset dataset = createDetailComChartDataset(cursor, kind,
					cominfo, timepoints);

			JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle,
					"日期", "单位：百分比", dataset, true, true, false);

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

		} catch (Exception e)
		{
			logger.warn("Exception - " + e.toString());
			e.printStackTrace();
			filename = "public_error_500x300.png";
		}
		return filename;
	}

	private XYDataset createDetailComChartDataset(Cursor cursor, String kind,
			String[] cominfo, String timepoints)
	{
		double value1 = 0.0;
		Map fields;
		long lms = 0L;
		Hour h, oldh = new Hour(new Date());
		TimeSeries ts, ts2;
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		ts = new TimeSeries(cominfo[0], Hour.class);
		ts2 = new TimeSeries(cominfo[1], Hour.class);
		fields = cursor.getNext();
		Date dt;
		String[] col = new String[2];
		String tmp;
		while (fields != null)
		{
			lms = Long.parseLong((String) fields
					.get("STATISTIME".toLowerCase())) * 1000;
			dt = new Date(lms);
			if (timepoints.length() > 0 && timepoints != null)
			{
				if (timepoints.indexOf("," + dt.getHours() + ",") == -1)
				{
					fields = cursor.getNext();
					continue;
				}
				col[0] = "UPVALUE".toLowerCase();
				col[1] = "DNVALUE".toLowerCase();
			}

			h = new Hour(dt);
			if (oldh.equals(h))
			{
				fields = cursor.getNext();
				continue;
			}

			oldh = h;
			if (kind.toLowerCase().equals("up"))
			{
				tmp = (String) fields.get(col[0]);
				if (tmp.length() == 0)
					tmp = "0.00";
				value1 = Double.parseDouble(tmp) * 100.00;
				ts.add(h, value1);
			} else if (kind.toLowerCase().equals("down"))
			{
				tmp = (String) fields.get(col[1]);
				if (tmp.length() == 0)
					tmp = "0.00";
				value1 = Double.parseDouble(tmp) * 100.00;
				ts2.add(h, value1);
			} else
			{
				tmp = (String) fields.get(col[0]);
				if (tmp.length() == 0)
					tmp = "0.00";
				value1 = Double.parseDouble(tmp) * 100.00;
				ts.add(h, value1);
				tmp = (String) fields.get(col[1]);
				if (tmp.length() == 0)
					tmp = "0.00";
				value1 = Double.parseDouble(tmp) * 100.00;
				ts2.add(h, value1);
			}

			fields = cursor.getNext();
		}
		if (kind.toLowerCase().equals("up"))
		{
			dataset.addSeries(ts);
		} else if (kind.toLowerCase().equals("down"))
		{
			dataset.addSeries(ts2);
		} else
		{
			dataset.addSeries(ts);
			dataset.addSeries(ts2);
		}

		return dataset;
	}

	public String DayComChart(String title, Cursor cursor, HttpSession session,
			PrintWriter pw, String kind, String[] cominfo, String timepoint)
	{
		String filename = null;
		try
		{
			// create a title...
			String chartTitle = title;
			XYDataset dataset = createDayComChartDataset(cursor, kind, cominfo,
					timepoint);

			JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle,
					"日期", "单位：百分比", dataset, true, true, false);

			// StandardLegend legend = (StandardLegend) chart.getLegend();
			// legend.setDisplaySeriesShapes(true);
			XYPlot plot = chart.getXYPlot();
			XYItemRenderer renderer = plot.getRenderer();
			DateAxis axis = (DateAxis) plot.getDomainAxis();
			axis.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 1,
					new SimpleDateFormat("MM-dd")));

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

		} catch (Exception e)
		{
			logger.warn("Exception - " + e.toString());
			e.printStackTrace();
			filename = "public_error_500x300.png";
		}
		return filename;
	}

	private XYDataset createDayComChartDataset(Cursor cursor, String kind,
			String[] cominfo, String timepoints)
	{
		double value1 = 0.0;
		Map fields;
		long lms = 0L;
		Day d, oldd = new Day(new Date());
		TimeSeries ts, ts2;
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		ts = new TimeSeries(cominfo[0], Day.class);
		ts2 = new TimeSeries(cominfo[1], Day.class);
		fields = cursor.getNext();
		Date dt;
		String[] col = new String[2];
		String tmp;
		while (fields != null)
		{
			lms = Long.parseLong((String) fields
					.get("STATISTIME".toLowerCase())) * 1000;
			dt = new Date(lms);
			if (timepoints.indexOf("," + dt.getDate() + ",") == -1)
			{
				fields = cursor.getNext();
				continue;
			}
			col[0] = "DAYUPVALUE".toLowerCase();
			col[1] = "DAYDNVALUE".toLowerCase();

			d = new Day(dt);
			if (oldd.equals(d))
			{
				fields = cursor.getNext();
				continue;
			}

			oldd = d;

			if (kind.toLowerCase().equals("up"))
			{
				tmp = (String) fields.get(col[0]);
				if (tmp.length() == 0)
					tmp = "0.00";
				value1 = Double.parseDouble(tmp) * 100.00;
				ts.add(d, value1);
			} else if (kind.toLowerCase().equals("down"))
			{
				tmp = (String) fields.get(col[1]);
				if (tmp.length() == 0)
					tmp = "0.00";
				value1 = Double.parseDouble(tmp) * 100.00;
				ts2.add(d, value1);
			} else
			{
				tmp = (String) fields.get(col[0]);
				if (tmp.length() == 0)
					tmp = "0.00";
				value1 = Double.parseDouble(tmp) * 100.00;
				ts.add(d, value1);
				tmp = (String) fields.get(col[1]);
				if (tmp.length() == 0)
					tmp = "0.00";
				value1 = Double.parseDouble(tmp) * 100.00;
				ts2.add(d, value1);
			}

			fields = cursor.getNext();
		}
		if (kind.toLowerCase().equals("up"))
		{
			dataset.addSeries(ts);
		} else if (kind.toLowerCase().equals("down"))
		{
			dataset.addSeries(ts2);
		} else
		{
			dataset.addSeries(ts);
			dataset.addSeries(ts2);
		}

		return dataset;
	}

	public String MonthComChart(String title, Cursor cursor,
			HttpSession session, PrintWriter pw, String kind, String[] cominfo,
			String timepoints)
	{
		String filename = null;
		try
		{
			// create a title...
			String chartTitle = title;
			XYDataset dataset = createMonthComChartDataset(cursor, kind,
					cominfo, timepoints);

			JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle,
					"日期", "单位：百分比", dataset, true, true, false);

			// StandardLegend legend = (StandardLegend) chart.getLegend();
			// legend.setDisplaySeriesShapes(true);
			XYPlot plot = chart.getXYPlot();
			DateAxis axis = (DateAxis) plot.getDomainAxis();
			axis.setTickUnit(new DateTickUnit(DateTickUnit.MONTH, 1,
					new SimpleDateFormat("yyyy-MM")));
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

		} catch (Exception e)
		{
			logger.warn("Exception - " + e.toString());
			e.printStackTrace();
			filename = "public_error_500x300.png";
		}
		return filename;
	}

	private XYDataset createMonthComChartDataset(Cursor cursor, String kind,
			String[] cominfo, String timepoints)
	{
		double value1 = 0.0;
		Map fields;
		long lms = 0L;
		Month m, oldm = new Month(new Date());
		TimeSeries ts, ts2;
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		ts = new TimeSeries(cominfo[0], Month.class);
		ts2 = new TimeSeries(cominfo[1], Month.class);
		fields = cursor.getNext();
		Date dt;
		String[] col = new String[2];
		String tmp;
		while (fields != null)
		{
			lms = Long.parseLong((String) fields
					.get("STATISTIME".toLowerCase())) * 1000;
			dt = new Date(lms);
			if (timepoints.indexOf("," + dt.getMonth() + ",") == -1)
			{
				fields = cursor.getNext();
				continue;
			}
			col[0] = "MONTHUPVALUE".toLowerCase();
			col[1] = "MONTHDNVALUE".toLowerCase();

			m = new Month(dt);
			if (oldm.equals(m))
			{
				fields = cursor.getNext();
				continue;
			}

			oldm = m;
			if (kind.toLowerCase().equals("up"))
			{
				tmp = (String) fields.get(col[0]);
				if (tmp.length() == 0)
					tmp = "0.00";
				value1 = Double.parseDouble(tmp) * 100.00;
				ts.add(m, value1);
			} else if (kind.toLowerCase().equals("down"))
			{
				tmp = (String) fields.get(col[1]);
				if (tmp.length() == 0)
					tmp = "0.00";
				value1 = Double.parseDouble(tmp) * 100.00;
				ts2.add(m, value1);
			} else
			{
				tmp = (String) fields.get(col[0]);
				if (tmp.length() == 0)
					tmp = "0.00";
				value1 = Double.parseDouble(tmp) * 100.00;
				ts.add(m, value1);
				tmp = (String) fields.get(col[1]);
				if (tmp.length() == 0)
					tmp = "0.00";
				value1 = Double.parseDouble(tmp) * 100.00;
				ts2.add(m, value1);
			}

			fields = cursor.getNext();
		}
		if (kind.toLowerCase().equals("up"))
		{
			dataset.addSeries(ts);
		} else if (kind.toLowerCase().equals("down"))
		{
			dataset.addSeries(ts2);
		} else
		{
			dataset.addSeries(ts);
			dataset.addSeries(ts2);
		}

		return dataset;
	}

	public static void main(String[] args)
	{

	}
}
