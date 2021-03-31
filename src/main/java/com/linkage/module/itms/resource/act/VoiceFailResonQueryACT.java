
package com.linkage.module.itms.resource.act;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.bio.VoiceFailResonQueryBIO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-30
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class VoiceFailResonQueryACT extends splitPageAction
{

	private static Logger logger = LoggerFactory
			.getLogger(VoiceFailResonQueryACT.class);
	/**
	 * 开始时间
	 */
	private String startOpenDate = "";
	// 开始时间
	private String startOpenDate1 = "";
	// 结束时间
	private String endOpenDate = "";
	// 结束时间
	private String endOpenDate1 = "";
	// 属地
	private String city_id = null;
	
	private String reason= "";
	// 属地列表
	private List<Map<String, String>> cityList = null;
	// 查询出语音信息列表
	private List<Map> voicefailMap;
	// 查询出语音设备信息列表
	private List<Map> voicefailDeviceMap;
	private VoiceFailResonQueryBIO bio;
	
	
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）

	@Override
	public String execute() throws Exception
	{
		logger.debug("init()");
		UserRes curUser = WebUtil.getCurrentUser();
		city_id = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		startOpenDate = endOpenDate = DateUtil.getNowTime("yyyy-MM-dd");
		return "init";
	}

	public String voiceFailReasonQueryInfo()
	{
		long startTime = System.currentTimeMillis();
		this.setTime();
		voicefailMap = bio.voiceFailResonQueryInfo(startOpenDate1, endOpenDate1, city_id);
		if (logger.isInfoEnabled())
		{
			logger.info("query voice fail resson list cost time[{}] ms", System.currentTimeMillis() - startTime);
		}
		return "list";
	}

	public String voiceFailReasonDevQueryInfo()
	{
		long startTime = System.currentTimeMillis();
		this.setTime();
		voicefailDeviceMap = bio.voiceFailDeviceQueryInfo(city_id, reason,startOpenDate1,
				endOpenDate1, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countVoiceFailDeviceQueryInfo(city_id, reason, startOpenDate1,
				endOpenDate1, curPage_splitPage, num_splitPage);
		if (logger.isInfoEnabled())
		{
			logger.info("query voice fail resson for device detail cost time[{}] ms", System.currentTimeMillis() - startTime);
		}
		return "devicelist";
	}
	
	public String voiceFailReasonQueryExcel(){
		logger.debug("voiceFailReasonQueryExcel()");
		this.setTime();
		voicefailMap = bio.voiceFailResonQueryInfo(startOpenDate1, endOpenDate1, city_id);
		String excelCol = "city_name#oneNum#twoNum#threeNum#fourNum#fiveNum";
		String excelTitle = "区域#IAD模块错误#访问路由不通#访问服务器无响应#帐号、密码错误#未知错误";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "voicefailMap";
		data = voicefailMap;
		return "excel";
	}
	
	public String voiceFailReasonDevQueryExcel(){
		this.setTime();
		voicefailDeviceMap = bio.voiceFailDeviceQueryExcel(city_id, reason, startOpenDate1, endOpenDate1);
		String excelCol = "city_name#loid#device_serialnumber#device_type#enabled#voip_phone#status#reason#add_time";
		String excelTitle = "区域#LOID#终端序列号#终端型号#语音端口是否启用#语音端口号码#语音注册成功状态#语音注册失败原因#日期";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "voicefailDeviceMap";
		data = voicefailDeviceMap;
		return "excel";
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		// start Time
		if (StringUtil.IsEmpty(startOpenDate))
		{
			startOpenDate1 = null;
		}
		else
		{
			String start = startOpenDate + " 00:00:00";
			DateTimeUtil st = new DateTimeUtil(start);
			startOpenDate1 = String.valueOf(st.getLongTime());
		}
		// endTime
		if (StringUtil.IsEmpty(endOpenDate))
		{
			endOpenDate1 = null;
		}
		else
		{
			String end = endOpenDate + " 23:59:59";
			DateTimeUtil et = new DateTimeUtil(end);
			endOpenDate1 = String.valueOf(et.getLongTime());
		}
	}

	public String getEndOpenDate()
	{
		return endOpenDate;
	}

	public void setEndOpenDate(String endOpenDate)
	{
		this.endOpenDate = endOpenDate;
	}

	public String getCity_id()
	{
		return city_id;
	}

	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public List<Map> getVoicefailMap()
	{
		return voicefailMap;
	}

	public void setVoicefailMap(List<Map> voicefailMap)
	{
		this.voicefailMap = voicefailMap;
	}

	public List<Map> getVoicefailDeviceMap()
	{
		return voicefailDeviceMap;
	}

	public void setVoicefailDeviceMap(List<Map> voicefailDeviceMap)
	{
		this.voicefailDeviceMap = voicefailDeviceMap;
	}

	public VoiceFailResonQueryBIO getBio()
	{
		return bio;
	}

	public void setBio(VoiceFailResonQueryBIO bio)
	{
		this.bio = bio;
	}

	
	public String getReason()
	{
		return reason;
	}

	
	public void setReason(String reason)
	{
		this.reason = reason;
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
	
	public String getStartOpenDate()
	{
		return startOpenDate;
	}

	public void setStartOpenDate(String startOpenDate)
	{
		this.startOpenDate = startOpenDate;
	}
}
