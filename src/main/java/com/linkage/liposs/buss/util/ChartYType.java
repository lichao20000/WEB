package com.linkage.liposs.buss.util;


/**
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2007-10-08
 * @category Util
 * 
 * 
 */
public interface ChartYType
{
	/**
	 * 流量图表类型
	 */
	final static int FLUX = 1;
	/**
	 * 流量最大值200
	 */
	final static double fluxMax = 200D;
	/**
	 * 流量最小值0
	 */
	final static double fluxMin = 0D;
	/**
	 * 百分比类型 0~100
	 */
	final static int PERCENT = 2;
	/**
	 * 百分比最大值
	 */
	final static double perMax = 100D;
	/**
	 * 百分比最小值
	 */
	final static double perMin = 0D;
	/**
	 * ping时延类型
	 */
	final static int PING = 3;
	/**
	 * ping的最大值2000
	 */
	final static double pingMax = 2000D;
	/**
	 * ping的最小值0
	 */
	final static double pingMin = 0D;
	/**
	 * 秒
	 */
	final static int Second = 0;
	/**
	 * 分钟
	 */
	final static int Minute = 1;
	/**
	 * 小时
	 */
	final static int Hour = 2;
	/**
	 * 天
	 */
	final static int Day = 3;
	/**
	 * 周
	 */
	final static int Week = 4;
	/**
	 * 月
	 */
	final static int Month = 5;
	/**
	 * 年
	 */
	final static int Year = 6;
	/**
	 * 标题的字体，大14px
	 */
	final static int BIG = 14;
	/**
	 * 标题的字体，小12px
	 */
	final static int SMALL = 12;
	/**
	 * 向下旋转30度
	 */
	final static double DOWN_30 = Math.PI / 6;
	/**
	 * 向下旋转45度
	 */
	final static double DOWN_45 = Math.PI / 4;
	/**
	 * 向下旋转60度
	 */
	final static double DOWN_60 = Math.PI / 3;
	/**
	 * 向下旋转90度
	 */
	final static double DOWN_90 = Math.PI / 2;
	/**
	 * 向上旋转30度
	 */
	final static double UP_30 = -Math.PI / 6;
	/**
	 * 向上旋转45度
	 */
	final static double UP_45 = -Math.PI / 4;
	/**
	 * 向上旋转60度
	 */
	final static double UP_60 = -Math.PI / 3;
	/**
	 * 向上旋转90度
	 */
	final static double UP_90 = -Math.PI / 2;
}
