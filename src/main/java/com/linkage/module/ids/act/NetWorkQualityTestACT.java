package com.linkage.module.ids.act;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.ids.bio.NetWorkQualityTestBIO;


/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-11-1
 * @category com.linkage.module.ids.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class NetWorkQualityTestACT extends splitPageAction
{
	
	private static Logger logger = LoggerFactory.getLogger(NetWorkQualityTestACT.class);
	
	private  NetWorkQualityTestBIO bio;
	
	//延迟数
	private  String avg_delay; 
	
	/**
	 * 丢包率
	 */
	private String loss_pp;
	
	//出现次数
	private String appear_count;
	
	// 开始时间
	private String startOpenDate = "";
	// 开始时间
	private String startOpenDate1 = "";
	// 结束时间
	private String endOpenDate = "";
	// 结束时间
	private String endOpenDate1 = "";
	
	private String device_sn = "";
	
	private List<Map> netWorkQualityList;
	
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	
	@Override
	public String execute() throws Exception
	{
		logger.debug("init()");
		startOpenDate = getStartDate();
		endOpenDate = getEndDate();
		return "init";
	}
	
	public String netWorkQualityInfo(){
		logger.debug("netWorkQualityInfo()");
		this.setTime();	
		netWorkQualityList = bio.netWorkQualityInfo(startOpenDate1, endOpenDate1, avg_delay, appear_count, loss_pp, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countNetWorkQualityInfo(startOpenDate1, endOpenDate1, avg_delay, appear_count, loss_pp, curPage_splitPage, num_splitPage);
		return "list";
	}
	
	public String netWorkQualityExcel(){
		this.setTime();
		netWorkQualityList = bio.netWorkQualityExcel(startOpenDate1, endOpenDate1, avg_delay, appear_count, loss_pp);
		String excelCol = "device_serialnumber#loid#address#avg_delay#appear_count#loss_pp";
		String excelTitle = "设备序列号#LOID#装机地址#平均时延#出现次数#丢包率";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "netWorkQuality";
		data = netWorkQualityList;
		return "excel";
	}
	
	public String netWorkQualityTestInfo(){
		logger.debug("netWorkQualityTestInfo()");
		this.setTime();	
		netWorkQualityList = bio.netWorkQualityTestInfo(startOpenDate1, endOpenDate1, device_sn,curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countNetWorkQualityTestInfo(startOpenDate1, endOpenDate1, device_sn, curPage_splitPage, num_splitPage);
		return "listinfo";
	}
	
	public String voiceOrderQueryExcel(){
		this.setTime();
		netWorkQualityList = bio.netWorkQualityTestExcel(startOpenDate1, endOpenDate1, device_sn);
		String excelCol = "device_serialnumber#loid#address#avg_delay#loss_pp#update_time";
		String excelTitle = "设备序列号#LOID#装机地址#时延数#丢包率#检测时间";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "netWorkQualityInfo";
		data = netWorkQualityList;
		return "excel";
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + startOpenDate);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startOpenDate == null || "".equals(startOpenDate))
		{
			startOpenDate1 = null;
		}
		else
		{
			dt = new DateTimeUtil(startOpenDate);
			startOpenDate1 = String.valueOf(dt.getLongTime());
		}
		if (endOpenDate == null || "".equals(endOpenDate))
		{
			endOpenDate1 = null;
		}
		else
		{
			dt = new DateTimeUtil(endOpenDate);
			endOpenDate1 = String.valueOf(dt.getLongTime());
		}
	}

	// 当前年的1月1号
	private String getStartDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		now.set(GregorianCalendar.DATE, 1);
		now.set(GregorianCalendar.MONTH, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	


	public NetWorkQualityTestBIO getBio()
	{
		return bio;
	}

	
	public void setBio(NetWorkQualityTestBIO bio)
	{
		this.bio = bio;
	}

	
	public String getAvg_delay()
	{
		return avg_delay;
	}

	
	public void setAvg_delay(String avg_delay)
	{
		this.avg_delay = avg_delay;
	}

	
	public String getAppear_count()
	{
		return appear_count;
	}

	
	public void setAppear_count(String appear_count)
	{
		this.appear_count = appear_count;
	}

	
	public String getStartOpenDate()
	{
		return startOpenDate;
	}

	
	public void setStartOpenDate(String startOpenDate)
	{
		this.startOpenDate = startOpenDate;
	}

	
	public String getEndOpenDate()
	{
		return endOpenDate;
	}

	
	public void setEndOpenDate(String endOpenDate)
	{
		this.endOpenDate = endOpenDate;
	}

	
	public List<Map> getNetWorkQualityList()
	{
		return netWorkQualityList;
	}

	
	public void setNetWorkQualityList(List<Map> netWorkQualityList)
	{
		this.netWorkQualityList = netWorkQualityList;
	}

	
	public List<Map> getData()
	{
		return data;
	}

	
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	
	public String[] getColumn()
	{
		return column;
	}

	
	
	public String getDevice_sn()
	{
		return device_sn;
	}

	
	public void setDevice_sn(String device_sn)
	{
		this.device_sn = device_sn;
	}

	public void setColumn(String[] column)
	{
		this.column = column;
	}

	
	public String[] getTitle()
	{
		return title;
	}

	
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	
	public String getFileName()
	{
		return fileName;
	}

	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	
	public String getLoss_pp()
	{
		return loss_pp;
	}

	
	public void setLoss_pp(String loss_pp)
	{
		this.loss_pp = loss_pp;
	}
}
