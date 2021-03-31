
package com.linkage.module.itms.resource.act;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.bio.VoiceOrderQueryBIO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-28
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class VoiceOrderQueryACT extends splitPageAction implements SessionAware,
		ServletRequestAware
{

	private static Logger logger = LoggerFactory
			.getLogger(VoiceOrderQueryACT.class);
	private HttpServletRequest request;
	private Map session;
	// 开始时间
	private String startOpenDate1 = "";
	// 结束时间
	private String endOpenDate = "";
	// 结束时间
	private String endOpenDate1 = "";
	// 属地
	private String city_id = null;
	// 属地列表
	private List<Map<String, String>> cityList = null;
	// 查询出语音信息列表
	private List<Map> voiceMap;
	// 查询出语音设备信息列表
	private List<Map> voiceDeviceMap;
	// 设备型号
	private String dev_type;
	
	private String numInfo;
	
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	
	private VoiceOrderQueryBIO bio;

	@Override
	public String execute() throws Exception
	{
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		endOpenDate = getEndDate();
		return "init";
	}

	public String voiceOrderQueryInfo()
	{
		logger.debug("voiceOrderQueryInfo()");
		this.setTime();
		voiceMap = bio.voiceOrderQueryInfo(city_id, startOpenDate1, endOpenDate1);
		return "list";
	}

	public String voiceDeviceQueryInfo()
	{
		logger.debug("voiceDeviceQueryInfo()");
		this.setTime();
		voiceDeviceMap = bio.voiceDeviceQueryInfo(city_id, startOpenDate1, endOpenDate1, numInfo,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countVoiceDeviceQueryInfo(city_id, startOpenDate1, endOpenDate1, numInfo,curPage_splitPage, num_splitPage);
		return "devicelist";
	}
	
	public String voiceOrderQueryExcel(){
		this.setTime();
		voiceMap = bio.voiceOrderQueryInfo(city_id, startOpenDate1, endOpenDate1);
		String excelCol = "city_name#lineOneNum#lineOneNoNum#lineTwoNum#lineTwoNoNum#lineOneTwoNoNum";
		String excelTitle = "区域#语音端口1总数#语音端口1未启用总数#语音端口2总数#语音端口2未启用总数#语音端口1和语音端口2同时未启用总数";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "voiceOrder";
		data = voiceMap;
		return "excel";
	}
	
	public String voiceDeviceQueryExcel(){
		this.setTime();
		voiceMap = bio.voiceDeviceQueryExcel(city_id, startOpenDate1, endOpenDate1, numInfo);
		String excelCol = "city_name#loid#device_serialnumber#device_type#enabled#voip_phone#status#reason";
		String excelTitle = "区域#LOID#终端序列号#终端型号#语音端口是否启用#语音端口号码#语音注册成功状态#语音注册失败原因";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "voiceOrder";
		data = voiceMap;
		return "excel";
	}
	

	// / 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + endOpenDate);
		if (endOpenDate == null || "".equals(endOpenDate))
		{
			startOpenDate1 = null;
			endOpenDate1 = null;
		}
		else
		{
			String start = endOpenDate + " 00:00:00";
			DateTimeUtil st = new DateTimeUtil(start);
			startOpenDate1 = String.valueOf(st.getLongTime());
			String end = endOpenDate + " 23:59:59";
			DateTimeUtil et = new DateTimeUtil(end);
			endOpenDate1 = String.valueOf(et.getLongTime());
			logger.warn("开始时间：" + start + "  结束时间：" + end);
		}
	}

	@Override
	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	@Override
	public void setSession(Map<String, Object> session)
	{
		this.session = session;
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

	public List<Map> getVoiceMap()
	{
		return voiceMap;
	}

	public void setVoiceMap(List<Map> voiceMap)
	{
		this.voiceMap = voiceMap;
	}

	public List<Map> getVoiceDeviceMap()
	{
		return voiceDeviceMap;
	}

	public void setVoiceDeviceMap(List<Map> voiceDeviceMap)
	{
		this.voiceDeviceMap = voiceDeviceMap;
	}

	public String getDev_type()
	{
		return dev_type;
	}

	public void setDev_type(String dev_type)
	{
		this.dev_type = dev_type;
	}

	public VoiceOrderQueryBIO getBio()
	{
		return bio;
	}

	public void setBio(VoiceOrderQueryBIO bio)
	{
		this.bio = bio;
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

	
	public String getNumInfo()
	{
		return numInfo;
	}

	
	public void setNumInfo(String numInfo)
	{
		this.numInfo = numInfo;
	}
}
