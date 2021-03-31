package com.linkage.liposs.buss.util;

import static com.linkage.liposs.buss.util.ChartColor.getSupplier;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Month;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.ui.Layer;
import org.jfree.ui.TextAnchor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * JFreeChart的公共封装类，主要提供jfreechart的时序图、曲线图、柱状图、饼图、区域图的公共方法<br>
 * 每个类别的方法都提供单list（生成单条线）和list数组的方法（同时生成多条线）<br>
 * <p>
 * 该类提供类生成各种图形的方法。同时提供定制Y轴范围的方式。<br>
 * 如果不设置Y轴的设定，则采用默认的jfreechart的自动调整Y轴的范围<br>
 * <b>自定义范围有三种方式：</b><br>
 * <ol>
 * <li>直接设置Y轴的最大值和最小值</li>
 * <li>直接设置Y轴的类型，设置后，Y轴会自动根据设置的类型进行调整</li>
 * <li>类型加Y轴的调整值，在某些时候，y的默认类型的max和min可能无法满足用户需求，可以通过误差值来修订达到需要</li>
 * </ol>
 * <b>所有预设值属性例如字体，markvalue等都需要在调用create方法之前调用，否则不起作用</b>
 * 
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2007-08-07
 * @category Util
 * 
 * 
 */
public class ChartUtil
{
	private static Logger log = LoggerFactory.getLogger(ChartUtil.class);
	private boolean cy = false;// 是否进行了Y轴值的定制,默认false
	private double yMax;// Y轴的最大值
	private double yMin;// Y轴的最小值
	private ValueMarker valuemarker = null;// mark标签
	private Font font = new Font("宋体", Font.PLAIN, 12);// 字体
	private DateAxis da = null;
	private String noDataMessage = "暂时无数据绘制!";
	private Date XStartDate = null;// X轴的起始值，为X轴是时间的图提供
	private Date XEndDate = null;// X轴的结束时间，为X轴是时间的图提供
	private double cLablePosition = 0D;// 分类柱状图的label的旋转角度
	private boolean tickLabelsVisible=true;//X轴的坐标刻度是否可见
	/**
	 * 该方法为产生单条线的时序图
	 * 
	 * @param title：
	 *            图片的标题
	 * @param xAName：
	 *            X轴的名称
	 * @param yAName：
	 *            y轴的名称
	 * @param lineName：
	 *            时序图对应曲线的图例名称
	 * @param list：
	 *            数据源,<b>注意:</b>该参数为spring查询的原装list，内部封装的必须是map,并且由于该方法是画时序图，因此，x轴值必须为时间值
	 * @param xName:
	 *            数据源中，x轴的列命
	 * @param yName:
	 *            数据源中，y轴的列命
	 * @param timeType：
	 *            X轴的时间间隔类型:0-秒;1-分钟;2-小时;3-日;4-周;5-月;6-年
	 * @return jfreechart的对象
	 */
	public JFreeChart createSTP(String title, String xAName, String yAName,
			String lineName, List<Map> list, String xName, String yName, int timeType)
	{
		return createSTP(title, xAName, yAName, new String[]
			{ lineName }, new List[]
			{ list }, xName, yName, timeType);
	}
	/**
	 * 该方法为产生多条线的时序图
	 * 
	 * @param title：
	 *            图片的标题
	 * @param xAName：
	 *            X轴的名称
	 * @param yAName：
	 *            y轴的名称
	 * @param lineName：
	 *            时序图对应曲线的图例名称
	 * @param list：
	 *            数据源,<b>注意:</b>该参数为spring查询的原装list，内部封装的必须是map,并且由于该方法是画时序图，因此，x轴值必须为时间值
	 * @param xName:
	 *            数据源中，x轴的列命
	 * @param yName:
	 *            数据源中，y轴的列命
	 * @param timeType：
	 *            X轴的时间间隔类型:0-秒;1-分钟;2-小时;3-日;4-周;5-月;6-年
	 * @return jfreechart的对象
	 */
	public JFreeChart createSTP(String title, String xAName, String yAName,
			String[] lineName, List<Map>[] list, String xName, String yName, int timeType)
	{
		return createSTP(title, xAName, yAName, lineName, list, xName, yName, timeType,
				true);
	}
	/**
	 * 该方法为产生多条线的时序图
	 * 
	 * @param title：
	 *            图片的标题
	 * @param xAName：
	 *            X轴的名称
	 * @param yAName：
	 *            y轴的名称
	 * @param lineName：
	 *            时序图对应曲线的图例名称
	 * @param list：
	 *            数据源,<b>注意:</b>该参数为spring查询的原装list，内部封装的必须是map,并且由于该方法是画时序图，因此，x轴值必须为时间值
	 * @param xName:
	 *            数据源中，x轴的列命
	 * @param yName:
	 *            数据源中，y轴的列命
	 * @param timeType：
	 *            X轴的时间间隔类型:0-秒;1-分钟;2-小时;3-日;4-周;5-月;6-年
	 * @param lenged
	 *            是否显示图例
	 * @return jfreechart的对象
	 */
	public JFreeChart createSTP(String title, String xAName, String yAName,
			String[] lineName, List<Map>[] list, String xName, String yName,
			int timeType, boolean lenged)
	{
		TimeSeriesCollection dataset = new TimeSeriesCollection();// 时序集合
		int ind = 0;// 标记标题位置
		for (List<Map> subList : list)
			{
				TimeSeries ts = createTS(subList, lineName[ind++], timeType, xName, yName);
				dataset.addSeries(ts);
			}
		JFreeChart chart = ChartFactory.createTimeSeriesChart(title, xAName, yAName,
				dataset, lenged, false, false);
		chart.setTitle(new TextTitle(title, font));
		chart.setBackgroundPaint(java.awt.Color.white);
		XYPlot xyplot = chart.getXYPlot();
		xyplot.setDrawingSupplier(getSupplier());
		xyplot.getDomainAxis().setLowerMargin(0.0D);
		xyplot.getDomainAxis().setUpperMargin(0.0D);
		xyplot.getDomainAxis().setTickLabelsVisible(tickLabelsVisible);
		if (da == null)
			{
				getDefaultUnit();
			}
		if (XStartDate != null)
			{
				da.setMinimumDate(XStartDate);
			}
		if (XEndDate != null)
			{
				da.setMaximumDate(XEndDate);
			}
		da.setLabel(xAName);
		xyplot.setDomainAxis(da);
		if (valuemarker != null)
			{
				xyplot.addRangeMarker(valuemarker, Layer.BACKGROUND);
			}
		if (cy)
			{
				ValueAxis va = xyplot.getRangeAxis();
				va.setUpperBound(yMax);
				va.setLowerBound(yMin);
			}
		else
			{
				ValueAxis va = xyplot.getRangeAxis();
				double min = va.getLowerBound();
				if (min < 0D)
					{
						va.setLowerBound(min + (min / 6D));
					}
				else
					{
						va.setLowerBound(0D);
					}
				va.setUpperBound(va.getUpperBound() + (va.getUpperBound() / 6D));
			}
		xyplot.setBackgroundPaint(Color.WHITE);
		xyplot.setDomainGridlinePaint(Color.BLACK);
		xyplot.setRangeGridlinePaint(Color.black);
		xyplot.setDomainCrosshairVisible(true);
		xyplot.setRangeCrosshairVisible(true);
		xyplot.setForegroundAlpha(0.7F);
		xyplot.setNoDataMessage(noDataMessage);
		return chart;
	}
	/**
	 * 生成折线图
	 * 
	 * @return org.jfree.chart.JFreeChart
	 */
	public JFreeChart createPLP()
	{
		return null;
	}
	/**
	 * 生成单组、多组别的平铺3D柱状图<br>
	 * ,该方法用于分类的柱状图，x轴应该为类别而非时间
	 * 
	 * @param title
	 *            该柱状图的标题
	 * @param xAname
	 *            X轴的名称
	 * @param yAName
	 *            Y轴的名称
	 * @param dataSet
	 *            数据集
	 * @param valueKey
	 *            获取具体数值的key名称,该值被用来从List<Map>中获取具体值，需要和Map中的key名相同
	 * @param rowKey
	 *            大组别的key名称，
	 * @param colKey
	 *            小组别的key名称，该值会被当作生成图例的名称
	 * @param legend
	 *            是否生成图例
	 * @return org.jfree.chart.JFreeChart
	 */
	public JFreeChart createCategoryBar3DP(String title, String xAname, String yAName,
			List<Map> dataSet, String valueKey, String rowKey, String colKey,
			boolean legend)
	{
		JFreeChart chart = ChartFactory.createBarChart3D(title, xAname, yAName,
				createBarDataSet(dataSet, valueKey, rowKey, colKey),
				PlotOrientation.VERTICAL, legend, false, false);
		chart.setBackgroundPaint(Color.WHITE);
		chart.setTitle(new TextTitle(title, font));
		CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
		categoryplot.setDrawingSupplier(getSupplier());
		categoryplot.setBackgroundPaint(Color.WHITE);
		categoryplot.setNoDataMessage(noDataMessage);
		categoryplot.getDomainAxis().setTickLabelsVisible(tickLabelsVisible);
		categoryplot.getDomainAxis().setCategoryLabelPositions(
				CategoryLabelPositions.createUpRotationLabelPositions(cLablePosition));
		((BarRenderer3D) categoryplot.getRenderer()).setMaximumBarWidth(0.05);
		// 增加标记值带
		if (valuemarker != null)
			{
				categoryplot.addRangeMarker(valuemarker, Layer.BACKGROUND);
			}
		return chart;
	}
	/**
	 * 生成竖向堆叠的3D柱状图
	 * 
	 * @param title
	 *            图的标题
	 * @param xAname
	 *            X轴的名称
	 * @param yAname
	 *            Y轴的名称
	 * @param dataSet
	 *            <List<Map>> map中字段解释：valueKey (值的的键名)，colKey（小类别的键名）,
	 *            rowKey(大类别的键名) 数据集合
	 * @param valueKey
	 *            获取值得键名
	 * @param rowKey
	 *            大组别的key名称，
	 * @param colKey
	 *            小组别的key名称，该值会被当作生成图例的名称
	 * @param legend
	 *            是否生成图例
	 * @return org.jfree.chart.JFreeChart
	 */
	public JFreeChart createCategoryStackedBar3DP(String title, String xAname,
			String yAname, List<Map> dataSet, String valueKey, String rowKey,
			String colKey, boolean legend)
	{
		JFreeChart jfreechart = ChartFactory.createStackedBarChart3D(title, xAname,
				yAname, createBarDataSet(dataSet, valueKey, rowKey, colKey),
				PlotOrientation.VERTICAL, legend, false, false);
		jfreechart.setTitle(new TextTitle(title, font));
		jfreechart.setBackgroundPaint(Color.white);
		CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
		categoryplot.setDrawingSupplier(getSupplier());
		categoryplot.setBackgroundPaint(Color.white);
		categoryplot.setRangeGridlinePaint(Color.BLACK);
		categoryplot.getDomainAxis().setTickLabelsVisible(tickLabelsVisible);
		categoryplot.getDomainAxis().setCategoryLabelPositions(
				CategoryLabelPositions.createUpRotationLabelPositions(cLablePosition));
		((BarRenderer3D) categoryplot.getRenderer()).setMaximumBarWidth(0.05);
		// 增加标记值带
		if (valuemarker != null)
			{
				categoryplot.addRangeMarker(valuemarker, Layer.BACKGROUND);
			}
		categoryplot.setNoDataMessage(noDataMessage);
		StackedBarRenderer3D stackedbarrenderer = (StackedBarRenderer3D) categoryplot
				.getRenderer();
		stackedbarrenderer.setDrawBarOutline(false);// 是否画柱子的棱角边框
		stackedbarrenderer
				.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		return jfreechart;
	}
	/**
	 * 生成时序的柱状图，单类别
	 * 
	 * @param title
	 *            图的标题
	 * @param xAName
	 *            X轴的标题名称
	 * @param yAName
	 *            Y轴的标题名称
	 * @param CategoryName
	 *            分类名称（会被作为图例出现）
	 * @param list
	 *            数据源
	 * @param xName
	 *            获取时间的key名，单位：秒
	 * @param yName
	 *            获取值得key名
	 * @param timeType
	 *            X轴的时间间隔类型:0-秒;1-分钟;2-小时;3-日;4-周;5-月;6-年
	 * @param legend
	 *            是否显示图例
	 * @return org.jfree.chart.JFreeChart
	 */
	public JFreeChart createXYCategoryBarP(String title, String xAName, String yAName,
			String CategoryName, List<Map> list, String xName, String yName,
			int timeType, boolean legend)
	{
		return createXYCategoryBarP(title, xAName, yAName, new String[]
			{ CategoryName }, new List[]
			{ list }, xName, yName, timeType, legend);
	}
	/**
	 * 生成多个图例的时序柱状图
	 * 
	 * @param title
	 *            图的标题
	 * @param xAName
	 *            X轴的标题
	 * @param yAName
	 *            Y轴的标题
	 * @param CategoryName
	 *            分类的类别（会作为图例）
	 * @param list
	 *            数据集合
	 * @param xName
	 *            获取X轴时间的key值名称
	 * @param yName
	 *            获取Y轴值得key值名称
	 * @param timeType
	 *            X轴的时间间隔类型:0-秒;1-分钟;2-小时;3-日;4-周;5-月;6-年
	 * @param legend
	 *            是否生成图例
	 * @return org.jfree.chart.JFreeChart
	 */
	public JFreeChart createXYCategoryBarP(String title, String xAName, String yAName,
			String[] CategoryName, List<Map>[] list, String xName, String yName,
			int timeType, boolean legend)
	{
		TimeSeriesCollection tc = new TimeSeriesCollection();
		int ind = 0;// 标记标题位置
		for (List<Map> lt : list)
			{
				TimeSeries timeseries = createTS(lt, CategoryName[ind++], timeType,
						xName, yName);
				tc.addSeries(timeseries);
			}
		JFreeChart jfreechart = ChartFactory.createXYBarChart(title, xAName, true,
				yAName, tc, PlotOrientation.VERTICAL, legend, false, false);
		jfreechart.setBackgroundPaint(Color.WHITE);
		jfreechart.setTitle(new TextTitle(title, font));
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setDrawingSupplier(getSupplier());
		xyplot.setBackgroundPaint(Color.WHITE);
		if (da == null)
			{
				getDefaultUnit();
			}
		if (XStartDate != null)
			{
				da.setMinimumDate(XStartDate);
			}
		if (XEndDate != null)
			{
				da.setMaximumDate(XEndDate);
			}
		da.setLabel(xAName);
		xyplot.setDomainAxis(da);
		xyplot.getDomainAxis().setTickLabelsVisible(tickLabelsVisible);
		ClusteredXYBarRenderer clusteredxybarrenderer = new ClusteredXYBarRenderer(0.08D,
				false);
		xyplot.setRenderer(clusteredxybarrenderer);
		// 增加标记值带
		if (valuemarker != null)
			{
				xyplot.addRangeMarker(valuemarker, Layer.BACKGROUND);
			}
		xyplot.setNoDataMessage(noDataMessage);
		clusteredxybarrenderer.setDrawBarOutline(false);
		return jfreechart;
	}
	/**
	 * 生成时序的堆叠柱状图
	 * 
	 * @param title
	 *            图的标题
	 * @param xAname
	 *            X轴的名称
	 * @param yAname
	 *            Y轴的名称
	 * @param dataSet
	 *            数据集
	 * @param valueKey
	 *            获取值得键名
	 * @param timeKey
	 *            获取时间的键名，单位：秒
	 * @param CategoryName
	 *            获取分类的键名（会被生成图例）
	 * @param legend
	 *            是否显示图例
	 * @param timeType
	 *            时间类型： X轴的时间间隔类型:0-秒;1-分钟;2-小时;3-日;4-周;5-月;6-年
	 * @return
	 */
	public JFreeChart createXYStackedBarP(String title, String xAname, String yAname,
			List<Map> dataSet, String valueKey, String timeKey, String CategoryName,
			int timeType, boolean legend)
	{
		DateAxis dateaxis = new DateAxis(xAname);
		dateaxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
		dateaxis.setLowerMargin(0.01D);
		dateaxis.setUpperMargin(0.01D);
		NumberAxis numberaxis = new NumberAxis(yAname);
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		numberaxis.setUpperMargin(0.1D);
		StackedXYBarRenderer stackedxybarrenderer = new StackedXYBarRenderer(
				0.14999999999999999D);
		stackedxybarrenderer.setDrawBarOutline(false);
		stackedxybarrenderer
				.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
		stackedxybarrenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER));
		XYPlot xyplot = new XYPlot(createXYStackedDataSet(dataSet, valueKey, timeKey,
				CategoryName, timeType), dateaxis, numberaxis, stackedxybarrenderer);
		xyplot.getDomainAxis().setTickLabelsVisible(tickLabelsVisible);
		xyplot.setDrawingSupplier(getSupplier());
		xyplot.setBackgroundPaint(Color.WHITE);
		if (da == null)
			{
				getDefaultUnit();
			}
		if (XStartDate != null)
			{
				da.setMinimumDate(XStartDate);
			}
		if (XEndDate != null)
			{
				da.setMaximumDate(XEndDate);
			}
		da.setLabel(xAname);
		xyplot.setDomainAxis(da);
		if (valuemarker != null)
			{
				xyplot.addRangeMarker(valuemarker, Layer.BACKGROUND);
			}
		xyplot.setNoDataMessage(noDataMessage);
		JFreeChart jfreechart = new JFreeChart(title, font, xyplot, legend);
		jfreechart.setBackgroundPaint(Color.WHITE);
		return jfreechart;
	}
	/**
	 * 设置标记值，设置了该值后，会在bar图中生成一个水平的值标记带
	 * 
	 * @param markValue
	 *            需要设置的值
	 */
	public void setValuemarker(double markValue)
	{
		this.valuemarker = new ValueMarker(markValue, new Color(200, 200, 255),
				new BasicStroke(1.0F), new Color(200, 200, 255), new BasicStroke(1.0F),
				1.0F);
	}
	/**
	 * 清楚标记值
	 */
	public void cleanValuemarker()
	{
		this.valuemarker = null;
	}
	/**
	 * 生成饼图
	 * 
	 * @param title
	 *            图形的标题
	 * @param dataSet
	 *            数据集
	 * @param legend
	 *            是否显示图例
	 * @param percent
	 *            是否以百分比显示，如果为true，则值会被转换为百分比显示
	 * @param xName
	 *            获取分类（会被作为图例）的键名
	 * @param yName
	 *            获取具体值得键名
	 * @return
	 */
	public JFreeChart createPieP(String title, List<Map> dataSet, String xName,
			String yName, boolean legend, boolean percent)
	{
		DefaultPieDataset dpd = createPieDataSet(dataSet, xName, yName);
		JFreeChart jfreechart = ChartFactory.createPieChart(title, dpd, legend, false,
				false);
		jfreechart.setTitle(new TextTitle(title, font));
		jfreechart.setBackgroundPaint(Color.WHITE);
		PiePlot pieplot = (PiePlot) jfreechart.getPlot();
		pieplot.setDrawingSupplier(getSupplier());
		pieplot.setBackgroundPaint(Color.WHITE);
		pieplot.setNoDataMessage(noDataMessage);
		if (percent)
			{
				pieplot
						.setLabelGenerator(new StandardPieSectionLabelGenerator(
								"{0}[{2}]"));
			}
		return jfreechart;
	}
	/**
	 * 生成区域图
	 * 
	 * @return org.jfree.chart.JFreeChart
	 */
	public JFreeChart createAreaP()
	{
		return null;
	}
	/**
	 * 自定义Y轴的最大值、最小值
	 * 
	 * @param max
	 *            Y轴的最大值
	 * @param min
	 *            Y轴的最小值
	 */
	public void setYMM(double max, double min)
	{
		cy = true;
		this.yMax = max;
		this.yMin = min;
	}
	/**
	 * 指定Y轴的范围类型，类型应该通过接口com.linkage.liposs.buss.util.ChartYType里面的常量来指定
	 * 
	 * @param type
	 *            ChartYType里面的常量值
	 */
	public void setYMMT(int type)
	{
		cy = true;
		switch (type)
			{
			case ChartYType.FLUX:
				this.yMax = ChartYType.fluxMax;
				this.yMin = ChartYType.fluxMin;
				break;
			case ChartYType.PERCENT:
				this.yMax = ChartYType.perMax;
				this.yMin = ChartYType.perMin;
				break;
			case ChartYType.PING:
				this.yMax = ChartYType.pingMax;
				this.yMin = ChartYType.pingMin;
				break;
			default:
				break;
			}
	}
	/**
	 * 指定Y轴的范围类型，类型应该通过接口com.linkage.liposs.buss.util.ChartYType里面的常量来指定
	 * 
	 * @param type
	 *            ChartYType里面的常量值
	 * @param maxFix
	 *            Y轴需要调整的最大值的差值
	 * @param minFix
	 *            Y轴需要调整的最小值的差值
	 */
	public void setYMMT(int type, double maxFix, double minFix)
	{
		cy = true;
		switch (type)
			{
			case ChartYType.FLUX:
				this.yMax = ChartYType.fluxMax + maxFix;
				this.yMin = ChartYType.fluxMin + minFix;
				break;
			case ChartYType.PERCENT:
				this.yMax = ChartYType.perMax + maxFix;
				this.yMin = ChartYType.perMin + minFix;
				break;
			case ChartYType.PING:
				this.yMax = ChartYType.pingMax + maxFix;
				this.yMin = ChartYType.pingMin + minFix;
				break;
			default:
				break;
			}
	}
	/**
	 * 提取TimeSeries的方法，内部使用
	 * 
	 * @param list
	 *            数据源
	 * @param lineName
	 *            画线时的标记名
	 * @param type
	 *            时间刻度类型
	 * @param xName
	 *            数据源的x轴的列名
	 * @param yName
	 *            数据源的y轴的列名
	 * @return
	 */
	private TimeSeries createTS(List<Map> list, String lineName, int type, String xName,
			String yName)
	{
		// 时间类型
		Class typeClass = type == 0 ? Second.class : type == 1 ? Minute.class
				: type == 2 ? Hour.class : type == 3 ? Day.class : type == 4 ? Week.class
						: type == 5 ? Month.class : Year.class;
		TimeSeries ts = new TimeSeries(lineName, typeClass);
		if ((list != null) && (list.size() != 0))
			{
				switch (type)
					{
					case ChartYType.Second:
						// 秒
						for (Map map : list)
							{
								try
									{
										ts.addOrUpdate(new Second(
												new Date(Long.parseLong(map.get(xName)
														.toString()) * 1000L)), Double
												.parseDouble(map.get(yName).toString()));
									} catch (Exception e)
									{
										log
												.error(
														"时序图增加时序点出现错误，可能是传进的y值有非法的字符串，无法解析为double,或者x无法解析为date（应该传入秒数）,跳过该点，继续执行,异常为:",
														e);
										continue;
									}
							}
						break;
					case ChartYType.Minute:
						// 分钟
						for (Map map : list)
							{
								try
									{
										ts.addOrUpdate(new Minute(
												new Date(Long.parseLong(map.get(xName)
														.toString()) * 1000L)), Double
												.parseDouble(map.get(yName).toString()));
									} catch (Exception e)
									{
										log
												.error(
														"时序图增加时序点出现错误，可能是传进的y值有非法的字符串，无法解析为double,或者x无法解析为date（应该传入秒数）,跳过该点，继续执行,异常为:",
														e);
										continue;
									}
							}
						break;
					case ChartYType.Hour:
						// 小时
						for (Map map : list)
							{
								try
									{
										ts.addOrUpdate(new Hour(
												new Date(Long.parseLong(map.get(xName)
														.toString()) * 1000L)), Double
												.parseDouble(map.get(yName).toString()));
									} catch (Exception e)
									{
										log
												.error(
														"时序图增加时序点出现错误，可能是传进的y值有非法的字符串，无法解析为double,或者x无法解析为date（应该传入秒数）,跳过该点，继续执行,异常为:",
														e);
										continue;
									}
							}
						break;
					case ChartYType.Day:
						// 天
						for (Map map : list)
							{
								try
									{
										ts.addOrUpdate(new Day(
												new Date(Long.parseLong(map.get(xName)
														.toString()) * 1000L)), Double
												.parseDouble(map.get(yName).toString()));
									} catch (Exception e)
									{
										log
												.error(
														"时序图增加时序点出现错误，可能是传进的y值有非法的字符串，无法解析为double,或者x无法解析为date（应该传入秒数）,跳过该点，继续执行,异常为:",
														e);
										continue;
									}
							}
						break;
					case ChartYType.Week:
						// 周
						for (Map map : list)
							{
								try
									{
										ts.addOrUpdate(new Week(
												new Date(Long.parseLong(map.get(xName)
														.toString()) * 1000L)), Double
												.parseDouble(map.get(yName).toString()));
									} catch (Exception e)
									{
										log
												.error(
														"时序图增加时序点出现错误，可能是传进的y值有非法的字符串，无法解析为double,或者x无法解析为date（应该传入秒数）,跳过该点，继续执行,异常为:",
														e);
										continue;
									}
							}
						break;
					case ChartYType.Month:
						// 月
						for (Map map : list)
							{
								try
									{
										ts.addOrUpdate(new Month(
												new Date(Long.parseLong(map.get(xName)
														.toString()) * 1000L)), Double
												.parseDouble(map.get(yName).toString()));
									} catch (Exception e)
									{
										log
												.error(
														"时序图增加时序点出现错误，可能是传进的y值有非法的字符串，无法解析为double,或者x无法解析为date（应该传入秒数）,跳过该点，继续执行,异常为:",
														e);
										continue;
									}
							}
						break;
					case ChartYType.Year:
						// 年
						for (Map map : list)
							{
								try
									{
										ts.addOrUpdate(new Year(
												new Date(Long.parseLong(map.get(xName)
														.toString()) * 1000L)), Double
												.parseDouble(map.get(yName).toString()));
									} catch (Exception e)
									{
										log
												.error(
														"时序图增加时序点出现错误，可能是传进的y值有非法的字符串，无法解析为double,或者x无法解析为date（应该传入秒数）,跳过该点，继续执行,异常为:",
														e);
										continue;
									}
							}
						break;
					default:
						break;
					}
			}
		return ts;
	}
	/**
	 * 平铺柱状图内部私有方法
	 * 
	 * @param dataSource
	 *            数据源
	 * @param valueKey
	 *            获取值得key
	 * @param colKey
	 *            小类别图例
	 * @param rowKey
	 *            列类别（大类别）分类
	 * @return CategoryDataset
	 */
	private CategoryDataset createBarDataSet(List<Map> dataSource,
			String valueKey, String rowKey, String colKey) {
		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
		Object value = null, col = null, row = null;
		if ((dataSource != null) && (dataSource.size() != 0)) {
			for (Map map : dataSource) {
				if (map == null)
					continue;
				value = map.get(valueKey);
				col = map.get(colKey);
				row = map.get(rowKey);
				if (value == null || col == null || row == null) {
					continue;
				}
				defaultcategorydataset.addValue(Double.parseDouble(value
						.toString()), col.toString(), row.toString());
			}
		} else {
			log.error("创建平铺柱状图时，数据源为空或者长度为零");
		}
		return defaultcategorydataset;
	}

	/**
	 * 创建时序堆叠柱状图的时间集合
	 * 
	 * @param dataSource
	 * @param valueKey
	 * @param timeKey
	 * @param CategoryName
	 * @param timeType
	 * @return org.jfree.data.xy.TableXYDataset
	 */
	private TableXYDataset createXYStackedDataSet(List<Map> dataSource, String valueKey,
			String timeKey, String CategoryName, int timeType)
	{
		TimeTableXYDataset timetablexydataset = new TimeTableXYDataset();
		for (Map map : dataSource)
			{
				timetablexydataset.add(createTime(timeType, Long.parseLong(map.get(
						timeKey).toString())), Double.parseDouble(map.get(valueKey)
						.toString()), map.get(CategoryName).toString());
			}
		return timetablexydataset;
	}
	// 创建柱状图时间点
	private TimePeriod createTime(int type, long time)
	{
		switch (type)
			{
			case ChartYType.Second:
				return new Second(new Date(time * 1000L));
			case ChartYType.Minute:
				return new Minute(new Date(time * 1000L));
			case ChartYType.Hour:
				return new Hour(new Date(time * 1000L));
			case ChartYType.Day:
				return new Day(new Date(time * 1000L));
			case ChartYType.Week:
				return new Week(new Date(time * 1000L));
			case ChartYType.Month:
				return new Month(new Date(time * 1000L));
			case ChartYType.Year:
				return new Year(new Date(time * 1000L));
			default:
				return new Day(new Date(time * 1000L));
			}
	}
	/**
	 * 产生饼图的数据集合
	 * 
	 * @param dataSet
	 *            外数据集
	 * @param xName
	 *            获取类别的字段名称
	 * @param yName
	 *            获取y值得字段名称
	 * @return 返回标准的饼图数据集合
	 */
	private DefaultPieDataset createPieDataSet(List<Map> dataSet, String xName,
			String yName)
	{
		DefaultPieDataset dpd = new DefaultPieDataset();
		for (Map m : dataSet)
			{
				dpd.setValue(m.get(xName).toString(), Double.parseDouble(m.get(yName)
						.toString()));
			}
		return dpd;
	}
	/**
	 * 设置图形的顶级标题的字体，该方法对所有的图形均有效，该方法和setFontSize只要调用一个就可以
	 * 
	 * @param font
	 */
	public void setFont(Font font)
	{
		this.font = font;
	}
	/**
	 * 设置字体的大小，该方法和setFont只要调用一个就可以
	 * 
	 * @param fontSize
	 */
	public void setFontSize(int fontSize)
	{
		this.font = new Font("宋体", Font.PLAIN, fontSize);
	}
	/**
	 * 设置X轴的显示标尺日期方法
	 * 
	 * @param unit，单位
	 *            该类参考类单位DateTickUnit中的常量设置，例如小时 DateTickUnit.HOUR
	 * @param cnt
	 *            颗粒度的次数，根据单位显示的密集程度来定
	 * @param pattern
	 *            模板，小时报表为:HH(24小时)或者hh(12小时) am 代表上下午 天的为 dd(一个月中的几号) E为一周的星期几
	 *            m 月 Y 年 <br>
	 *            例如:setXAxisUnit(DateTickUnit.DAY,1,"am hh")
	 */
	public void setXAxisUnit(int unit, int cnt, String pattern)
	{
		DateFormat df = new SimpleDateFormat(pattern);
		DateTickUnit dateTickUnit = new DateTickUnit(unit, cnt, df);
		da = new DateAxis();
		da.setTickUnit(dateTickUnit);
	}
	/**
	 * 如果没有设置坐标尺则调用此方法
	 */
	private void getDefaultUnit()
	{
		DateFormat df = new SimpleDateFormat("HH:mm");
		DateTickUnit dateTickUnit = new DateTickUnit(DateTickUnit.HOUR, 1, df);
		da = new DateAxis();
		da.setTickUnit(dateTickUnit);
	}
	/**
	 * 指定没有数据的时候显示的信息，默认是“暂时无数据绘制!”
	 * 
	 * @param noDataMessage
	 *            没有数据的时候显示的信息
	 */
	public void setNoDataMessage(String noDataMessage)
	{
		this.noDataMessage = noDataMessage;
	}
	/**
	 * 设置X轴的起始时间
	 * 
	 * @param startDate
	 *            起始时间，日期Date
	 * @param endDate
	 *            结束时间，日期Date
	 */
	public void setXDate(Date startDate, Date endDate)
	{
		XStartDate = startDate;
		XEndDate = endDate;
	}
	/**
	 * 设置分类图的X轴标签的旋转角度，当X轴为时序图的时候该值无效
	 * 
	 * @param lablePosition
	 *            旋转的值，可以使用ChartYType中的常量
	 */
	public void setCLablePosition(double lablePosition)
	{
		cLablePosition = lablePosition;
	}
	/**
	 * 设置X轴的刻度是否显示，默认true
	 * @param tickLabelsVisible boolean值
	 */
	public void setTickLabelsVisible(boolean tickLabelsVisible)
	{
		this.tickLabelsVisible = tickLabelsVisible;
	}
}
