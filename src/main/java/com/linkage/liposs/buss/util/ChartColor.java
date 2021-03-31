package com.linkage.liposs.buss.util;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.plot.DefaultDrawingSupplier;

/**
 * 
 * JfreeChart的color提供者，该类去处了原先的随机红色线条，因为红色代表告警色
 * <p>
 * 
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2007-10-09
 * @category Util
 * 
 * 
 */
public class ChartColor
{
	/**
	 * 返回线条的颜色提供类。
	 * 
	 * @return DefaultDrawingSupplier
	 */
	public static DefaultDrawingSupplier getSupplier()
	{
		return new DefaultDrawingSupplier(new Paint[]
			{ 		
					org.jfree.chart.ChartColor.VERY_LIGHT_BLUE,
					org.jfree.chart.ChartColor.yellow,
					org.jfree.chart.ChartColor.VERY_LIGHT_CYAN,
					org.jfree.chart.ChartColor.VERY_LIGHT_GREEN,
					org.jfree.chart.ChartColor.VERY_DARK_CYAN,
					org.jfree.chart.ChartColor.VERY_LIGHT_MAGENTA,
					
					
					org.jfree.chart.ChartColor.VERY_LIGHT_YELLOW,
					org.jfree.chart.ChartColor.VERY_DARK_BLUE,
					org.jfree.chart.ChartColor.VERY_LIGHT_RED,
					new Color(74, 115, 53), 
					
					org.jfree.chart.ChartColor.green,
					new Color(83, 10, 114),
					org.jfree.chart.ChartColor.orange,
					org.jfree.chart.ChartColor.blue,
					org.jfree.chart.ChartColor.magenta,
					org.jfree.chart.ChartColor.cyan,
					org.jfree.chart.ChartColor.pink,
					org.jfree.chart.ChartColor.VERY_DARK_YELLOW,
					org.jfree.chart.ChartColor.DARK_GREEN,
					org.jfree.chart.ChartColor.DARK_YELLOW,
					org.jfree.chart.ChartColor.DARK_MAGENTA,
					org.jfree.chart.ChartColor.DARK_CYAN,
					org.jfree.chart.ChartColor.DARK_BLUE,
					org.jfree.chart.ChartColor.lightGray,
					org.jfree.chart.ChartColor.LIGHT_BLUE,
					org.jfree.chart.ChartColor.LIGHT_GREEN,
					org.jfree.chart.ChartColor.LIGHT_YELLOW,
					org.jfree.chart.ChartColor.LIGHT_MAGENTA,
					org.jfree.chart.ChartColor.LIGHT_CYAN,
					org.jfree.chart.ChartColor.VERY_DARK_MAGENTA,
					new Color(196, 183, 21),
					new Color(74, 115, 53), new Color(196, 183, 21),
					new Color(224, 167, 47) },

				DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE);
	}
}
