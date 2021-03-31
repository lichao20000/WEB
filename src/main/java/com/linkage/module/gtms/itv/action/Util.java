package com.linkage.module.gtms.itv.action;

import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.experimental.chart.plot.dial.StandardDialRange;
import org.jfree.ui.Layer;

public class Util {

	private static Logger log = Logger.getLogger(Util.class);
	private double yMax;// Y轴的最大值
	private double yMin;// Y轴的最小值
	private List<Marker> valuemarkers = new ArrayList<Marker>();// valuemark标签
	private List<Marker> intervalMarkers = new ArrayList<Marker>();// range的标记带
	private List<Marker> vvaluemarkers = new ArrayList<Marker>();// domain的标记值
	private List<Marker> vintervalMarkers = new ArrayList<Marker>();// domain的标记带
	private Font font = new Font("宋体", Font.PLAIN, 12);// 字体
	private DateAxis da = null;
	private String noDataMessage = "暂时无数据绘制!";
	private Date XStartDate = null;// X轴的起始值，为X轴是时间的图提供
	private Date XEndDate = null;// X轴的结束时间，为X轴是时间的图提供
	private double cLablePosition = 0D;// 分类柱状图的label的旋转角度
	private boolean tickLabelsVisible = true;// X轴的坐标刻度是否可见
	private List<StandardDialRange> dialRange = new ArrayList<StandardDialRange>();
	private int pieFractionDigits = 2;
	private Font markFont = new Font("宋体", Font.PLAIN, 12);
	private String yAxisFormat = "#0.00";// x轴的默认精度
	
	
	
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
	public JFreeChart createCategoryBarP(String title, String xAname, String yAName,
			List<Map> dataSet, String valueKey, String rowKey, String colKey,
			boolean legend)
	{
		try
		{
			JFreeChart chart = ChartFactory.createBarChart(title, xAname, yAName,
					createCategoryDataSet(dataSet, valueKey, rowKey, colKey),
					PlotOrientation.VERTICAL, legend, false, false);
			chart.setBackgroundPaint(Color.WHITE);
			chart.setTitle(new TextTitle(title, font));
			CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
			// categoryplot.setDrawingSupplier(getSupplier());
			categoryplot.setBackgroundPaint(Color.WHITE);
			categoryplot.setNoDataMessage(noDataMessage);
			categoryplot.getDomainAxis().setTickLabelsVisible(tickLabelsVisible);
			categoryplot.getDomainAxis()
					.setCategoryLabelPositions(
							CategoryLabelPositions
									.createUpRotationLabelPositions(cLablePosition));
			((BarRenderer) categoryplot.getRenderer()).setMaximumBarWidth(0.05);
			addCategoryMarker(categoryplot);
			setYAxisRange(categoryplot.getRangeAxis());
			return chart;
		}
		catch (Exception e)
		{
			System.out.println("绘制单组、多组别的平铺3D柱状图出错，请检查使用格式是否正确，异常如下:");
			e.printStackTrace();
			return createErrorChart();
		}
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
	private CategoryDataset createCategoryDataSet(List<Map> dataSource, String valueKey,
			String rowKey, String colKey)
	{
		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
		if ((dataSource != null) && (dataSource.size() != 0))
		{
			for (Map map : dataSource)
			{
				try
				{
					defaultcategorydataset.addValue(Double.parseDouble(map.get(valueKey)
							.toString()), map.get(colKey).toString(), map.get(rowKey)
							.toString());
				}
				catch (Exception e)
				{
					log.error("创建柱状图的数据出错，跳过该数据集合，继续", e);
					continue;
				}
			}
		}
		else
		{
			log.error("创建平铺柱状图时，数据源为空或者长度为零");
		}
		return defaultcategorydataset;
	}
	
	private void addCategoryMarker(CategoryPlot plot)
	{
		if (valuemarkers.size() != 0)
		{
			for (Marker m : valuemarkers)
			{
				plot.addRangeMarker(m, Layer.BACKGROUND);
			}
		}
		if (intervalMarkers.size() != 0)
		{
			for (Marker m : intervalMarkers)
			{
				plot.addRangeMarker(m, Layer.BACKGROUND);
			}
		}
	}
	
	// 设置Y轴的区间范围
	private void setYAxisRange(ValueAxis va)
	{
		double yxmin = va.getLowerBound();
		double yxmax = va.getUpperBound();
		if (yxmax < this.yMax)
		{
			yxmax = this.yMax;
		}
		if (yxmin > this.yMin)
		{
			yxmin = this.yMin;
		}
		va.setUpperBound(yxmax + yxmax / 6);
		va.setLowerBound(yxmin < 0 ? (yxmin + (yxmin / 6D)) : (yxmin - (yxmin / 6D)));
	}
	
	/**
	 * 设置X轴的显示标尺日期方法
	 *
	 * @param unit，单位
	 *            该类参考类单位DateTickUnit中的常量设置，例如小时 DateTickUnit.HOUR
	 * @param cnt
	 *            颗粒度的次数，根据单位显示的密集程度来定
	 * @param pattern
	 *            模板，小时报表为:HH(24小时)或者hh(12小时) am 代表上下午 天的为 dd(一个月中的几号) E为一周的星期几 M 月 y 年
	 *            <br>
	 *            例如:setXAxisUnit(DateTickUnit.DAY,1,"am hh") <b>下边是模板的具体信息</b> <table
	 *            border=0 cellspacing=3 cellpadding=0 summary="Chart shows pattern
	 *            letters, date/time component, presentation, and examples.">
	 *            <tr bgcolor="#ccccff">
	 *            <th align=left>Letter
	 *            <th align=left>Date or Time Component
	 *            <th align=left>Presentation
	 *            <th align=left>Examples
	 *            <tr>
	 *            <td><code>G</code>
	 *            <td>Era designator
	 *            <td><a href="#text">Text</a>
	 *            <td><code>AD</code>
	 *            <tr bgcolor="#eeeeff">
	 *            <td><code>y</code>
	 *            <td>Year
	 *            <td><a href="#year">Year</a>
	 *            <td><code>1996</code>; <code>96</code>
	 *            <tr>
	 *            <td><code>M</code>
	 *            <td>Month in year
	 *            <td><a href="#month">Month</a>
	 *            <td><code>July</code>; <code>Jul</code>; <code>07</code>
	 *            <tr bgcolor="#eeeeff">
	 *            <td><code>w</code>
	 *            <td>Week in year
	 *            <td><a href="#number">Number</a>
	 *            <td><code>27</code>
	 *            <tr>
	 *            <td><code>W</code>
	 *            <td>Week in month
	 *            <td><a href="#number">Number</a>
	 *            <td><code>2</code>
	 *            <tr bgcolor="#eeeeff">
	 *            <td><code>D</code>
	 *            <td>Day in year
	 *            <td><a href="#number">Number</a>
	 *            <td><code>189</code>
	 *            <tr>
	 *            <td><code>d</code>
	 *            <td>Day in month
	 *            <td><a href="#number">Number</a>
	 *            <td><code>10</code>
	 *            <tr bgcolor="#eeeeff">
	 *            <td><code>F</code>
	 *            <td>Day of week in month
	 *            <td><a href="#number">Number</a>
	 *            <td><code>2</code>
	 *            <tr>
	 *            <td><code>E</code>
	 *            <td>Day in week
	 *            <td><a href="#text">Text</a>
	 *            <td><code>Tuesday</code>; <code>Tue</code>
	 *            <tr bgcolor="#eeeeff">
	 *            <td><code>a</code>
	 *            <td>Am/pm marker
	 *            <td><a href="#text">Text</a>
	 *            <td><code>PM</code>
	 *            <tr>
	 *            <td><code>H</code>
	 *            <td>Hour in day (0-23)
	 *            <td><a href="#number">Number</a>
	 *            <td><code>0</code>
	 *            <tr bgcolor="#eeeeff">
	 *            <td><code>k</code>
	 *            <td>Hour in day (1-24)
	 *            <td><a href="#number">Number</a>
	 *            <td><code>24</code>
	 *            <tr>
	 *            <td><code>K</code>
	 *            <td>Hour in am/pm (0-11)
	 *            <td><a href="#number">Number</a>
	 *            <td><code>0</code>
	 *            <tr bgcolor="#eeeeff">
	 *            <td><code>h</code>
	 *            <td>Hour in am/pm (1-12)
	 *            <td><a href="#number">Number</a>
	 *            <td><code>12</code>
	 *            <tr>
	 *            <td><code>m</code>
	 *            <td>Minute in hour
	 *            <td><a href="#number">Number</a>
	 *            <td><code>30</code>
	 *            <tr bgcolor="#eeeeff">
	 *            <td><code>s</code>
	 *            <td>Second in minute
	 *            <td><a href="#number">Number</a>
	 *            <td><code>55</code>
	 *            <tr>
	 *            <td><code>S</code>
	 *            <td>Millisecond
	 *            <td><a href="#number">Number</a>
	 *            <td><code>978</code>
	 *            <tr bgcolor="#eeeeff">
	 *            <td><code>z</code>
	 *            <td>Time zone
	 *            <td><a href="#timezone">General time zone</a>
	 *            <td><code>Pacific Standard Time</code>; <code>PST</code>;
	 *            <code>GMT-08:00</code>
	 *            <tr>
	 *            <td><code>Z</code>
	 *            <td>Time zone
	 *            <td><a href="#rfc822timezone">RFC 822 time zone</a>
	 *            <td><code>-0800</code> </table>
	 */
	public void setXAxisUnit(int unit, int cnt, String pattern)
	{
		DateFormat df = new SimpleDateFormat(pattern);
		DateTickUnit dateTickUnit = new DateTickUnit(unit, cnt, df);
		da = new DateAxis();
		da.setTickUnit(dateTickUnit);
	}
	
	/**
	 * 设置出错图片
	 *
	 * @return
	 */
	private JFreeChart createErrorChart()
	{
		setNoDataMessage("绘制图形出错，请检查后台的数据集合是否符合绘图要求规范");
		JFreeChart chart = ChartFactory.createTimeSeriesChart("", "", "",
				new DefaultXYDataset(), false, false, false);
		chart.getXYPlot().setNoDataMessage(noDataMessage);
		chart.setBackgroundPaint(java.awt.Color.white);
		chart.getXYPlot().getDomainAxis().setVisible(false);
		chart.getXYPlot().getRangeAxis().setVisible(false);
		return chart;
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

	public double getyMax() {
		return yMax;
	}

	public void setyMax(double yMax) {
		this.yMax = yMax;
	}

	public double getyMin() {
		return yMin;
	}

	public void setyMin(double yMin) {
		this.yMin = yMin;
	}

	public List<Marker> getValuemarkers() {
		return valuemarkers;
	}

	public void setValuemarkers(List<Marker> valuemarkers) {
		this.valuemarkers = valuemarkers;
	}

	public List<Marker> getIntervalMarkers() {
		return intervalMarkers;
	}

	public void setIntervalMarkers(List<Marker> intervalMarkers) {
		this.intervalMarkers = intervalMarkers;
	}

	public List<Marker> getVvaluemarkers() {
		return vvaluemarkers;
	}

	public void setVvaluemarkers(List<Marker> vvaluemarkers) {
		this.vvaluemarkers = vvaluemarkers;
	}

	public List<Marker> getVintervalMarkers() {
		return vintervalMarkers;
	}

	public void setVintervalMarkers(List<Marker> vintervalMarkers) {
		this.vintervalMarkers = vintervalMarkers;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public DateAxis getDa() {
		return da;
	}

	public void setDa(DateAxis da) {
		this.da = da;
	}

	public String getNoDataMessage() {
		return noDataMessage;
	}

	public void setNoDataMessage(String noDataMessage) {
		this.noDataMessage = noDataMessage;
	}

	public Date getXStartDate() {
		return XStartDate;
	}

	public void setXStartDate(Date xStartDate) {
		XStartDate = xStartDate;
	}

	public Date getXEndDate() {
		return XEndDate;
	}

	public void setXEndDate(Date xEndDate) {
		XEndDate = xEndDate;
	}

	public double getcLablePosition() {
		return cLablePosition;
	}

	public void setcLablePosition(double cLablePosition) {
		this.cLablePosition = cLablePosition;
	}

	public boolean isTickLabelsVisible() {
		return tickLabelsVisible;
	}

	public void setTickLabelsVisible(boolean tickLabelsVisible) {
		this.tickLabelsVisible = tickLabelsVisible;
	}

	public List<StandardDialRange> getDialRange() {
		return dialRange;
	}

	public void setDialRange(List<StandardDialRange> dialRange) {
		this.dialRange = dialRange;
	}

	public int getPieFractionDigits() {
		return pieFractionDigits;
	}

	public void setPieFractionDigits(int pieFractionDigits) {
		this.pieFractionDigits = pieFractionDigits;
	}

	public Font getMarkFont() {
		return markFont;
	}

	public void setMarkFont(Font markFont) {
		this.markFont = markFont;
	}

	public String getyAxisFormat() {
		return yAxisFormat;
	}

	public void setyAxisFormat(String yAxisFormat) {
		this.yAxisFormat = yAxisFormat;
	}
	
}
