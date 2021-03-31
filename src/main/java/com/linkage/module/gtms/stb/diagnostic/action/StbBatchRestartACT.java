
package com.linkage.module.gtms.stb.diagnostic.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.diagnostic.serv.StbBatchRestartBIO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Administrator
 */
public class StbBatchRestartACT extends splitPageAction implements SessionAware
{

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory
			.getLogger(StbBatchRestartACT.class);
	// 传参
	private String deviceIds = "";
	// Session
	private Map<String, Object> session;
	private String gwShare_fileName = "";
	private String gwShare_cityId = "";
	// 查询方式
	private String gwShare_queryField = "";
	private int total;
	// 查询参数
	private String gwShare_queryParam = "";
	private String gwShare_msg = "";
	// 查询宽带帐号
	private List<HashMap<String, String>> deviceList;
	private StbBatchRestartBIO bio;
	private String ajax = "";
	/** 宽带帐号 **/
	private String username;
	private long userId;
	private String gw_type = "";
	// 属地
	private String cityId = "00";
	// 符合条件的LOID
	private static String isSuccess = "1";
	private String strategy_type = "";
	// 状态
	private String status = "";
	// 开始时间
	private String starttime;
	// 结束时间
	private String endtime;
	// 查询列表
	private List<Map> list = new ArrayList<Map>();
	/** 获取device_id */
	private List<Map> device_idList = null;
	/** 条件内容 */
	private String condition = "";
	/** 开通状态 */
	private String openState = "";
	/** 条件 */
	private String con = "";
	/** 宽带账号 */
	private String netusernames = "";
	/** 属地 */
	private String city_ids = "";
	/** 上网个数 */
	private String maxNetNum = "";
	/** 上网方式 */
	private String wan_types = "";
	/** 业务账号 */
	private String serv_account = "";
	/** 设备序列号 */
	private String device_serialnumber = "";
	/** 设备属地 */
	private String city_id = "";
	/** 返回信息 */
	private String resMessage = "";
	/**重启方式*/
	private String type = "";
	/**业务账号**/
	private String fileUsername = "";
	/**间隔时间**/
	private String intervalTime = "";
	public void setTime()
	{
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate(); // 获取当前时间
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000); // 精确到当前时间的23点59分59秒
		endtime = dt.getLongDate();
		starttime = dt.getFirtDayOfMonth(); // 获取开始时间，为当月时间的第一天
		dt = new DateTimeUtil(starttime);
		long start_time = dt.getLongTime();
		dt = new DateTimeUtil((start_time) * 1000);
		starttime = dt.getLongDate();
	}

	/**
	 * 查询设备（带列表）
	 * 
	 * @return
	 */
	public String queryDeviceList()
	{
		logger.warn("StbBatchRestartACT=>getDeviceList()");
		UserRes curUser = (UserRes) session.get("curUser");
		logger.warn("StbBatchRestartACT=>gwShare_cityId...:" + gwShare_cityId);
		if (!StringUtil.IsEmpty(gwShare_cityId))
		{
			gwShare_cityId.trim();
		}
		else
		{
			this.gwShare_cityId = curUser.getCityId();
		}
		if (!StringUtil.IsEmpty(gwShare_cityId))
		{
			gwShare_fileName.trim();
		}
		logger.warn("StbBatchRestartACT=>gwShare_fileName...:" + gwShare_fileName);
		this.deviceList = bio.getDeviceList(curUser, gwShare_cityId, gwShare_fileName);
		if (null == this.deviceList || this.deviceList.size() < 1)
		{
			this.gwShare_msg = bio.getMsg();
		}
		total = deviceList == null ? 0 : this.deviceList.size();
		return "shareList";
	}

	public String execStbBatchRestart()
	{
		logger.warn("StbBatchRestartACT=>execStbBatchRestart()");
		logger.warn("deviceIds:*********************" + deviceIds);
		logger.warn("serv_account:*******************" + serv_account);
		logger.warn("intervalTime:*********************" + intervalTime);
		logger.warn("device_serialnumber:***********************" + device_serialnumber);
		logger.warn("starttime:*********************" + starttime);
		ArrayList<String> devlist = stringToList();
		if(StringUtil.getIntegerValue(intervalTime) != 0 && devlist.size() > 2000)
		{
			this.ajax = "间隔时间不为空时，可重启设备不能大于2000台！";
			return "ajax";
		}
		logger.warn("devlist:*********************" + devlist);
		UserRes curUser = (UserRes) session.get("curUser");
		this.ajax = bio.insertRestartDev(devlist, dateToTimestamp(starttime),
				StringUtil.getIntegerValue(intervalTime), curUser);
		return "ajax";
	}

	/**
	 * 将传入的String数组转为List
	 * @return
	 */
	public ArrayList<String> stringToList()
	{
		deviceIds = deviceIds.replace("\n", "").trim();
		serv_account = serv_account.replace("\n", "").trim();
		device_serialnumber = device_serialnumber.replace("\n", "").trim();
		ArrayList<String> devlist = new ArrayList<String>();
		if (deviceIds.contains(",") && serv_account.contains(",")
				&& device_serialnumber.contains(",")) { // 多个设备
			String[] deviceIdArr = deviceIds.split(",");
			String[] servAccountArr = serv_account.split(",");
			String[] deviceSnArr = device_serialnumber.split(",");

			for (int i = 0; i < deviceIdArr.length; i++) {
				deviceIdArr[i] = StringUtil.getStringValue(deviceIdArr[i]).replace("\n", "").trim();
				servAccountArr[i] = StringUtil.getStringValue(servAccountArr[i]).replace("\n", "").trim();
				deviceSnArr[i] = StringUtil.getStringValue(deviceSnArr[i]).replace("\n", "").trim();
				devlist.add(deviceIdArr[i]+ "##" +servAccountArr[i] + "##" +deviceSnArr[i]);
			}
		} else { // 单个设备
			devlist.add(deviceIds + "##" + serv_account + "##" + device_serialnumber );
		}
		return devlist;
	}
	
	/**
	 * 日期转成时间戳
	 * @param time
	 * @return
	 */
	
	public Long dateToTimestamp(String time)
	{
		long unixTimestamp = 0;
		try
		{
			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
			unixTimestamp = date.getTime()/1000;
		}
		catch (Exception e)
		{
			logger.error("StbBatchRestartACT->dateToTimestamp error");
		}
		return unixTimestamp;
	}

	public void setSession(Map<String, Object> session)
	{
		this.session = session;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	public static Logger getLogger()
	{
		return logger;
	}

	public Map<String, Object> getSession()
	{
		return session;
	}

	public String getDeviceIds()
	{
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds)
	{
		this.deviceIds = deviceIds;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public StbBatchRestartBIO getBio()
	{
		return bio;
	}

	public void setBio(StbBatchRestartBIO bio)
	{
		this.bio = bio;
	}

	public String getGwShare_queryField()
	{
		return gwShare_queryField;
	}

	public void setGwShare_queryField(String gwShare_queryField)
	{
		this.gwShare_queryField = gwShare_queryField;
	}

	public String getGwShare_queryParam()
	{
		return gwShare_queryParam;
	}

	public void setGwShare_queryParam(String gwShare_queryParam)
	{
		this.gwShare_queryParam = gwShare_queryParam;
	}

	public List<HashMap<String, String>> getDeviceList()
	{
		return deviceList;
	}

	public void setDeviceList(List<HashMap<String, String>> deviceList)
	{
		this.deviceList = deviceList;
	}

	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public String getGw_type()
	{
		return gw_type;
	}

	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public static String getIsSuccess()
	{
		return isSuccess;
	}

	public static void setIsSuccess(String isSuccess)
	{
		StbBatchRestartACT.isSuccess = isSuccess;
	}

	public String getStrategy_type()
	{
		return strategy_type;
	}

	public void setStrategy_type(String strategy_type)
	{
		this.strategy_type = strategy_type;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getStarttime()
	{
		return starttime;
	}

	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}

	public String getEndtime()
	{
		return endtime;
	}

	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	public List<Map> getList()
	{
		return list;
	}

	public void setList(List<Map> list)
	{
		this.list = list;
	}

	public List<Map> getDevice_idList()
	{
		return device_idList;
	}

	public void setDevice_idList(List<Map> device_idList)
	{
		this.device_idList = device_idList;
	}

	public String getCondition()
	{
		return condition;
	}

	public void setCondition(String condition)
	{
		this.condition = condition;
	}

	public String getOpenState()
	{
		return openState;
	}

	public void setOpenState(String openState)
	{
		this.openState = openState;
	}

	public String getCon()
	{
		return con;
	}

	public void setCon(String con)
	{
		this.con = con;
	}

	public String getGwShare_fileName()
	{
		return gwShare_fileName;
	}

	public void setGwShare_fileName(String gwShare_fileName)
	{
		this.gwShare_fileName = gwShare_fileName;
	}

	public String getGwShare_msg()
	{
		return gwShare_msg;
	}

	public void setGwShare_msg(String gwShare_msg)
	{
		this.gwShare_msg = gwShare_msg;
	}

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}

	public String getGwShare_cityId()
	{
		return gwShare_cityId;
	}

	public void setGwShare_cityId(String gwShare_cityId)
	{
		this.gwShare_cityId = gwShare_cityId;
	}

	public String getNetusernames()
	{
		return netusernames;
	}

	public void setNetusernames(String netusernames)
	{
		this.netusernames = netusernames;
	}

	public String getCity_ids()
	{
		return city_ids;
	}

	public void setCity_ids(String city_ids)
	{
		this.city_ids = city_ids;
	}

	public String getMaxNetNum()
	{
		return maxNetNum;
	}

	public void setMaxNetNum(String maxNetNum)
	{
		this.maxNetNum = maxNetNum;
	}

	public String getWan_types()
	{
		return wan_types;
	}

	public void setWan_types(String wan_types)
	{
		this.wan_types = wan_types;
	}

	public String getServ_account()
	{
		return serv_account;
	}

	public void setServ_account(String serv_account)
	{
		this.serv_account = serv_account;
	}

	public String getDevice_serialnumber()
	{
		return device_serialnumber;
	}

	public void setDevice_serialnumber(String device_serialnumber)
	{
		this.device_serialnumber = device_serialnumber;
	}

	public String getCity_id()
	{
		return city_id;
	}

	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}

	public String getUsername()
	{
		return username;
	}

	public String getResMessage()
	{
		return resMessage;
	}

	public void setResMessage(String resMessage)
	{
		this.resMessage = resMessage;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getFileUsername()
	{
		return fileUsername;
	}

	public void setFileUsername(String fileUsername)
	{
		this.fileUsername = fileUsername;
	}

	public String getIntervalTime()
	{
		return intervalTime;
	}

	public void setIntervalTime(String intervalTime)
	{
		this.intervalTime = intervalTime;
	}
	
}
