
package com.linkage.module.gwms.resource.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.gw.DeviceDAO;

public class QueryDeviceBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(QueryDeviceBIO.class);
	private DeviceDAO deviceDao;
	private int total;
	/**
	 * 返回需要导出的excel文件内容标题
	 * 
	 * @author wangsenbo
	 * @date Nov 16, 2009
	 * @return String[]
	 */
	public String[] getTitle(String infoType, String bindState)
	{
		logger.debug("getTitle({},{})", infoType, bindState);
		String[] title;
		if ("1".equals(infoType))
		{
			if ("1".equals(bindState))
			{
				title = new String[6];
				title[0] = "属地";
				title[1] = "设备厂商";
				title[2] = "型号";
				title[3] = "设备序列号";
				title[4] = "域名或IP";
				title[5] = "上报时间";
			}
			else if("3".equals(bindState))
			{
				title = new String[7];
				title[0] = "属地";
				title[1] = "设备厂商";
				title[2] = "型号";
				title[3] = "软件版本";
				title[4] = "设备序列号";
				title[5] = "域名或IP";
				title[6] = "上报时间";
			}
			else
			{
				title = new String[8];
				title[0] = "属地";
				title[1] = "设备厂商";
				title[2] = "型号";
				title[3] = "设备序列号";
				title[4] = "域名或IP";
				title[5] = "用户帐号";
				title[6] = "绑定时间";
				title[7] = "上报时间";
				
			}
		}
		else
		{
			if ("1".equals(bindState))
			{
				title = new String[6];
				title[0] = "属地";
				title[1] = "设备厂商";
				title[2] = "型号";
				title[3] = "设备序列号";
				title[4] = "域名或IP";
				title[5] = "上报时间";
			}
			else if("3".equals(bindState))
			{
				title = new String[7];
				title[0] = "属地";
				title[1] = "设备厂商";
				title[2] = "型号";
				title[3] = "软件版本";
				title[4] = "设备序列号";
				title[5] = "域名或IP";
				title[6] = "上报时间";
			}
			else 
			{
				title = new String[8];
				title[0] = "属地";
				title[1] = "设备厂商";
				title[2] = "型号";
				title[3] = "设备序列号";
				title[4] = "域名或IP";
				title[5] = "用户帐号";
				title[6] = "绑定时间";
				title[7] = "上报时间";
				
			}
		}
		return title;
	}

	/**
	 * 返回需要导出的excel文件字段
	 * 
	 * @author wangsenbo
	 * @date Nov 16, 2009
	 * @return String[]
	 */
	public String[] getColumn(String infoType, String bindState)
	{
		logger.debug("getColumn({},{})", infoType, bindState);
		String[] column;
		if ("1".equals(infoType))
		{
			if ("1".equals(bindState))
			{
				column = new String[6];
				column[0] = "city_name";
				column[1] = "vendor_add";
				column[2] = "device_model";
				column[3] = "device";
				column[4] = "loopback_ip";
				column[5] = "complete_time";
			}else if("3".equals(bindState))
			{
				column = new String[7];
				column[0] = "city_name";
				column[1] = "vendor_add";
				column[2] = "device_model";
				column[3] = "software";
				column[4] = "device";
  				column[5] = "loopback_ip";
  				column[6] = "complete_time";
			}
			else
			{
				column = new String[8];
				column[0] = "city_name";
				column[1] = "vendor_add";
				column[2] = "device_model";
				column[3] = "device";
				column[4] = "loopback_ip";
				column[5] = "username";
				column[6] = "binddate";
				column[7] = "complete_time";
				
			}
		}
		else
		{
			if ("1".equals(bindState))
			{
				column = new String[6];
				column[0] = "city_name";
				column[1] = "vendor_add";
				column[2] = "device_model";
				column[3] = "device";
				column[4] = "loopback_ip";
				column[5] = "complete_time";
			}else if("3".equals(bindState))
			{
				column = new String[7];
				column[0] = "city_name";
				column[1] = "vendor_add";
				column[2] = "device_model";
				column[3] = "software";
				column[4] = "device";
  				column[5] = "loopback_ip";
  				column[6] = "complete_time";
			}
			else
			{
				column = new String[8];
				column[0] = "city_name";
				column[1] = "vendor_add";
				column[2] = "device_model";
				column[3] = "device";
				column[4] = "loopback_ip";
				column[5] = "username";
				column[6] = "binddate";
				column[7] = "complete_time";
				
			}
		}
		return column;
	}

	/**
	 * * 分页查询设备记录
	 * 
	 * @param infoType
	 *            用于判断是家庭网关：1 还是企业网关：2
	 * @param curPage_splitPage
	 *            当前页数
	 * @param num_splitPage
	 *            每页记录数
	 * @param area_id
	 *            域
	 * @param cityIdList
	 *            属地列表
	 * @param bindState
	 *            绑定状态
	 * @param timeType
	 *            时间类型
	 * @param starttime1
	 *            开始时间
	 * @param endtime1
	 *            结束时间
	 * @author wangsenbo
	 * @date Nov 17, 2009
	 * @return List<Map>
	 */
	public List<Map> getDeviceList(String infoType, int curPage_splitPage,
			int num_splitPage, UserRes curUser, String bindState, String timeType,
			String starttime1, String endtime1, String device_serialnumber,
			String loopback_ip, String device_logicsn, String device_status)
	{
		return deviceDao.getDeviceList(infoType, curPage_splitPage, num_splitPage,
				curUser, bindState, timeType, starttime1, endtime1, device_serialnumber,
				loopback_ip, device_logicsn, device_status);
	}

	public int getDeviceCount(String infoType, int curPage_splitPage, int num_splitPage,
			UserRes curUser, String bindState, String timeType, String starttime1,
			String endtime1, String device_serialnumber, String loopback_ip, String device_logicsn, 
			String device_status)
	{
		 
		int ret = deviceDao.getDeviceCount(infoType, curPage_splitPage, num_splitPage,
				curUser, bindState, timeType, starttime1, endtime1, device_serialnumber,
				loopback_ip, device_logicsn, device_status); 
		this.total = deviceDao.getTotal();
		return ret;
	}
	
	
	
	/**
	 * 分页查询设备记录
	 * 
	 * add by zhangchy 2012-12-27 安徽需求
	 * 
	 * @param deviceId 
	 * 
	 * @param infoType
	 *            用于判断是家庭网关：1 还是企业网关：2
	 * @param curPage_splitPage
	 *            当前页数
	 * @param num_splitPage
	 *            每页记录数
	 *            
	 * @return List<Map>
	 */
	public List<Map> getDeviceListByDeviceId(String infoType, String deviceId,
			int curPage_splitPage, int num_splitPage) {
		return deviceDao.getDeviceListByDeviceId(infoType, deviceId, curPage_splitPage, num_splitPage);
	}

	public List<Map> getDeviceListByDeviceIdForSxlt(String infoType, String deviceId,
											 int curPage_splitPage, int num_splitPage) {
		return deviceDao.getDeviceListByDeviceIdForSxlt(infoType, deviceId, curPage_splitPage, num_splitPage);
	}

	public int getDeviceCountByDeviceId(String infoType, String deviceId,
			int curPage_splitPage, int num_splitPage) {
		return deviceDao.getDeviceCountByDeviceId(infoType, deviceId, curPage_splitPage, num_splitPage);
	}
	

	/**
	 * @return the deviceDao
	 */
	public DeviceDAO getDeviceDao()
	{
		return deviceDao;
	}

	/**
	 * @param deviceDao
	 *            the deviceDao to set
	 */
	public void setDeviceDao(DeviceDAO deviceDao)
	{
		this.deviceDao = deviceDao;
	}

	/**
	 * @author wangsenbo
	 * @date Nov 18, 2009
	 * @return List<Map>
	 */
	public List<Map> getDeviceExcel(String infoType, UserRes curUser, String bindState,
			String timeType, String starttime1, String endtime1,
			String device_serialnumber, String loopback_ip, String device_logicsn, String device_status)
	{
		return deviceDao.getDeviceExcel(infoType, curUser, bindState, timeType,
				starttime1, endtime1, device_serialnumber, loopback_ip, device_logicsn, device_status);
	}

	public List<Map> getImpInitDeviceList(int curPage_splitPage, int num_splitPage, String device_serialnumber)
	{
		return deviceDao.getImpInitDeviceList(curPage_splitPage, num_splitPage, device_serialnumber);
	}

	public int getImpInitDeviceCount(int curPage_splitPage,int num_splitPage, String device_serialnumber)
	{
		return deviceDao.getImpInitDeviceCount(curPage_splitPage, num_splitPage,device_serialnumber);
	}
	public int getTotal()
	{
		return total;
	}

	
	public void setTotal(int total)
	{
		this.total = total;
	}
}
