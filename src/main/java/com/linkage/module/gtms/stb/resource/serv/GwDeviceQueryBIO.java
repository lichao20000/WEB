/**
 * 
 */

package com.linkage.module.gtms.stb.resource.serv;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ailk.tr069.devrpc.dao.corba.AcsCorbaDAO;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.ExportExcelUtil;
import com.linkage.module.gtms.stb.dao.GwStbVendorModelVersionDAO;
import com.linkage.module.gtms.stb.resource.dao.GwDeviceQueryDAO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.bio
 */
public class GwDeviceQueryBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(GwDeviceQueryBIO.class);
	private GwStbVendorModelVersionDAO vmvDaoStb = null;
	private GwDeviceQueryDAO gwDeviceDao = null;

	/**
	 * 编辑设备表 
	 * 目前机顶盒查询列表没有 编辑 功能
	 */
//	public String editDevice(String deviceId, String cityId, String cpeMac,
//			String deviceIp)
//	{
//		try
//		{
//			int rs = gwDeviceDao.editDevice(deviceId, cityId, cpeMac, deviceIp);
//			if (0 < rs)
//			{
//				PrepareSQL sqlDevInfo = new PrepareSQL();
//				sqlDevInfo
//						.setSQL("select device_id,oui, device_serialnumber from tab_gw_device "
//								+ " where device_id ='");
//				sqlDevInfo.append(deviceId);
//				sqlDevInfo.append("'");
//				Cursor devRs = DataSetBean.getCursor(sqlDevInfo.getSQL());
//				Dev[] devArr = new Dev[1];
//				if (null != devRs)
//				{
//					for (int j = 0; j < devRs.getRecordSize(); j++)
//					{
//						Map map1 = devRs.getNext();
//						devArr[j] = new Dev();
//						devArr[j].devId = String.valueOf(map1.get("device_id"));
//						devArr[j].devOui = String.valueOf(map1.get("oui"));
//						devArr[j].devSn = String.valueOf(map1.get("device_serialnumber"));
//					}
//				}
//				int r = new ACSCorba().chgDev(devArr);
//				if (1 != r)
//				{
//					return "终端更新成功，通知ACS失败！";
//				}
//			}
//			return "终端更新成功！";
//		}
//		catch (Exception e)
//		{
//			return "终端更新失败！";
//		}
//	}

	/**
	 * 删除设备
	 * 
	 * @param deviceId
	 * @return
	 */
	public String delete(String deviceId, String cityId, boolean noChildCity, String gw_type)
	{
		logger.debug("GwDeviceQueryBIO=>delete(deviceId:{},cityId:{})", deviceId, cityId);
		List list = gwDeviceDao.queryDeviceByOnce(deviceId, cityId, noChildCity);
		if (list.size() < 1)
		{
			return "待删除的设备不存在！";
		}
		for (int i = 0; i < list.size(); i++)
		{
			Map map = (Map) list.get(i);
//			Dev[] devArr = new Dev[1];
//			devArr[0] = new Dev();
//			devArr[0].devId = String.valueOf(map.get("device_id"));
//			devArr[0].devOui = String.valueOf(map.get("oui"));
//			devArr[0].devSn = String.valueOf(map.get("device_serialnumber"));
			int flag = gwDeviceDao.deleteDevice(deviceId);
			if (flag < 1)
			{
				return "删除失败，请重新操作！";
			}
			else
			{
				String deviceIdS[] = new String[] { String.valueOf(map.get("device_id")) };
				String deSns[] = new String[] { String.valueOf(map.get("device_serialnumber"))};
				String devOuis[] = new String[] { String.valueOf(map.get("oui")) };
				
				int ret = new AcsCorbaDAO(Global.getPrefixName(gw_type)
						+ Global.SYSTEM_ACS).chgDev(deviceIdS, deSns, devOuis,
						LipossGlobals.getClientId());
				
//				int r = new ACSCorba().chgDev(devArr);
				
				if (1 != ret)
				{
					return "终端删除成功，通知ACS失败！";
				}
			}
		}
		return "删除成功！";
	}

	public void deviceExport(HttpServletResponse response, String fileName, long areaId,
			String queryType, String queryParam, String queryField, String cityId,
			String startLastTime, String endLastTime, String vendorId,
			String deviceModelId, String devicetypeId, String bindType,
			String deviceSerialnumber, String deviceIp, String username,
			String servAccount, Boolean noChildCity, String completeStartTime,
			String completeEndTime)
	{
		// 处理数据导出
		ExportExcelUtil util = new ExportExcelUtil("", new String[] { "设备厂商",
				"设备型号", "软件版本", "属地", "设备序列号", "设备IP", "业务账号", "最新上线时间" });
		try
		{
			List<Map> list = gwDeviceDao.queryDeviceForExport(areaId, cityId, vendorId,
					deviceModelId, devicetypeId, bindType, deviceSerialnumber, deviceIp,
					username, servAccount, startLastTime, endLastTime, noChildCity,
					completeStartTime, completeEndTime);
			//util.export(response, rowSet, fileName);
			util.export(response, list, 
					new String[]{"vendor_add", "device_model", "softwareversion", "city_name", "device_serialnumber", "loopback_ip", "serv_account", "last_time" }, fileName);
		}
		catch (Exception e)
		{
			logger.error("export error:", e);
		}
	}
	
	/**
	 * 零配置用查询导出
	 * @param response
	 * @param fileName
	 * @param cityId
	 * @param deviceSerialnumber
	 * @param deviceIp
	 * @param username
	 * @param servAccount
	 * @param noChildCity
	 * @param startLastTime
	 * @param endLastTime
	 * @param addressingType
	 * @param status
	 * @param cpeMac
	 */
	public void zeroDeviceExport(HttpServletResponse response, String fileName,String cityId,
			String deviceSerialnumber, String loopbackIp, String username,
			String servAccount, Boolean noChildCity, String startLastTime,
			String endLastTime,String addressingType,String status,
			String cpeMac,String failReason,String device_status,String loopbackIpSix)
	{
		// 处理数据导出
		ExportExcelUtil util = new ExportExcelUtil("", new String[] { "设备厂商",
				"设备型号", "软件版本", "属地", "设备序列号", "设备IP", "MAC", "业务账号", "首次上报时间", "设备状态" });
		try
		{
			//查询所有
			List<Map> list = gwDeviceDao.queryZeroDevice(cityId,deviceSerialnumber, loopbackIp,
					username, servAccount, noChildCity,
					startLastTime, endLastTime,addressingType,status,cpeMac,failReason,device_status,loopbackIpSix);
			//util.export(response, rowSet, fileName);
			util.export(response, list, 
					new String[]{"vendor_add", "device_model", "softwareversion", "city_name", "device_serialnumber", "loopback_ip", "cpe_mac", "serv_account", "complete_time", "status" }, fileName);
		}
		catch (Exception e)
		{
			logger.error("export error:", e);
		}
	}

	/**
	 * 查询设备详细信息，根据权限过滤
	 * 
	 * @param deviceId
	 * @param areaId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map queryDeviceDetail(String deviceId, String cityId, boolean noChildCity)
	{
		logger.debug("GwDeviceQueryBIO=>queryDeviceDetail(deviceId:{})", deviceId);
		List list = gwDeviceDao.queryDeviceDetail(deviceId, cityId, noChildCity);
		
		Map map = null;
		if (list.size() > 0)
		{
			map = (Map) list.get(0);
			if (null != map.get("complete_time")
					&& !"".equals(String.valueOf(map.get("complete_time")).trim()))
			{
				map.put("complete_time", new DateTimeUtil(Long.parseLong(String
						.valueOf(map.get("complete_time"))) * 1000)
						.getYYYY_MM_DD_HH_mm_ss());
			}
			else
			{
				map.put("complete_time", "");
			}
			if (null != map.get("last_time")
					&& !"".equals(String.valueOf(map.get("last_time")).trim()))
			{
				map.put("last_time", new DateTimeUtil(Long.parseLong(String.valueOf(map
						.get("last_time"))) * 1000).getYYYY_MM_DD_HH_mm_ss());
			}
			else
			{
				map.put("last_time", "");
			}
			if (null != map.get("oper_time")
					&& !"".equals(String.valueOf(map.get("oper_time")).trim()))
			{
				map.put("oper_time", new DateTimeUtil(Long.parseLong(String.valueOf(map
						.get("oper_time"))) * 1000).getYYYY_MM_DD_HH_mm_ss());
			}
			else
			{
				map.put("oper_time", "");
			}
			if (null != map.get("cpe_currentupdatetime")
					&& !""
							.equals(String.valueOf(map.get("cpe_currentupdatetime"))
									.trim()))
			{
				map.put("cpe_currentupdatetime", new DateTimeUtil(Long.parseLong(String
						.valueOf(map.get("cpe_currentupdatetime"))) * 1000)
						.getYYYY_MM_DD_HH_mm_ss());
			}
			else
			{
				map.put("cpe_currentupdatetime", "");
			}
			map.put("city_name", CityDAO.getCityIdCityNameMap().get(map.get("city_id")));
			
			//增加设备状态
			map.put("status", gwDeviceDao.parseStatusByCode(StringUtil.getStringValue(map, "status")));
			
			if ("1".equals(String.valueOf(map.get("online_status"))))
			{
				map.put("online_status", "在线");
			}
			else
			{
				map.put("online_status", "下线");
			}
			map.put("is_tel_dev", "");
			if ("1".equals(String.valueOf(map.get("cpe_allocatedstatus"))))
			{
				Map userMap = gwDeviceDao.queryDeviceUser(Long.parseLong(String
						.valueOf(map.get("customer_id"))));
				if(1==StringUtil.getIntegerValue(userMap.get("is_tel_dev"))){
					map.put("isTelDev", "电信");
				}else if(2==StringUtil.getIntegerValue(userMap.get("is_tel_dev"))){
					map.put("isTelDev", "非电信");
				}
				
				map.putAll(userMap);
			}
		}
		/**      需求单JSDX_ITV_HTW-REQ-20120517-XMN-001 start     **/
		//获取历史配置信息
		if(Global.HNLT.equals(Global.instAreaShortName)){
			// 湖南联通无历史配置表
			return map;
		}
		List historyLt1  = gwDeviceDao.queryBaseConfigInfo(deviceId);
		Map historyMap = null; 
		if(historyLt1.size() > 0){
			historyMap = (Map)historyLt1.get(0);
			map.put("time", new DateTimeUtil(Long.parseLong(String
					.valueOf(historyMap.get("gather_time"))) * 1000)
			           .getYYYY_MM_DD_HH_mm_ss());
			map.put("addressingType", StringUtil.getStringValue(historyMap.get("address_type")));
			historyMap = null;	
		}
		List  historyLt2 = gwDeviceDao.getX_CTC_IPTV_ServiceInfoRecent(deviceId);
		if(historyLt2.size() > 0){
			historyMap = (Map)historyLt2.get(0);
			map.put("PPPOE", StringUtil.getStringValue(historyMap.get("pppoe_id")));
			map.put("PPPoEPassword", StringUtil.getStringValue(historyMap.get("pppoe_pwd")));
			map.put("userID", StringUtil.getStringValue(historyMap.get("user_id")));
			map.put("userPassword", StringUtil.getStringValue(historyMap.get("user_pwd")));
			map.put("authURL", StringUtil.getStringValue(historyMap.get("auth_url")));
			map.put("DHCPID", StringUtil.getStringValue(historyMap.get("iptv_dhcp_username")));
			map.put("DHCPPassword", StringUtil.getStringValue(historyMap.get("iptv_dhcp_password")));
			historyMap = null;
		}
		//更新服务器地址
//		List  historyLt3 = gwDeviceDao.getUserInterfaceRecent(deviceId);
//		if (historyLt3.size() > 0){
//			historyMap = (Map)historyLt3.get(0);
//			map.put("autoUpdateServer", StringUtil.getStringValue(historyMap.get("auto_update_serv")));
//			historyMap = null;
//		}
		/**屏显与视频输出【需要采集否】  
		int iret = new SuperGatherCorba().getCpeParams(deviceId,
				ConstantClass.GATHER_Capabilities);
		Map smap = null;
		if (iret == 1)
		{
			List  historyLt4 = gwDeviceDao.getCmpstVideoAndAspRatio(deviceId);
			if(historyLt4.size() > 0){
				historyMap = (Map)historyLt4.get(0);
				map.put("compositeVideo", StringUtil.getStringValue(historyMap.get("composite_video_standard")));
				map.put("aspectRatio", StringUtil.getStringValue(historyMap.get("aspect_ratio")));
			}
		}
		**/
		/**      需求单JSDX_ITV_HTW-REQ-20120517-XMN-001 end     **/
		return map;
	}

	/**
	 * 针对高级查询，结果集比较大的话，二次查询
	 * 
	 * @@param areaId 登录人所属域
	 * @param param 查询条件集 例如x|xx|xxx|xxx|xxxx
	 * @return
	 */
	public List getDeviceList(String gw_type,long areaId,String param){
		logger.debug("GwDeviceQueryBIO=>getDeviceList({})",param);
		if(null==param){
			return null;
		}
		String[] _param = param.split("\\|");
		
		String queryType = _param[0].trim();
		String queryField = _param[1].trim();
		String cityId = _param[2].trim();
		String queryParam = _param[3].trim();
		String onlineStatus = _param[4].trim();
		String vendorId = _param[5].trim();
		String deviceModelId = _param[6].trim();
		String devicetypeId = _param[7].trim();
		String bindType = _param[8].trim();
		String deviceSerialnumber = _param[9].trim();
		if(Global.XJDX.equals(Global.instAreaShortName)){
			//_param[9].trim() = gwShare_deviceSerialnumber+":" + gwShare_hardwareVersion +":"+gwShare_belong;
			String[] XjdeviceSerialnumber = deviceSerialnumber.split(":");
			deviceSerialnumber = XjdeviceSerialnumber[0];
		}
		return getDeviceList(gw_type,areaId, queryType, queryParam, queryField, cityId,
				onlineStatus, vendorId, deviceModelId, devicetypeId, 
				bindType, deviceSerialnumber);
	}
	
	/**
	 * 查询设备（带列表）（针对数据量小于一定量时）
	 * 
	 * @param curPage_splitPage		分页选项当前页数
	 * @param num_splitPage			分页选项每页显示数
	 * @param areaId				登录人的areaId
	 * @param queryType				查询类型(1:简单查询；2:高级查询)
	 * @param queryParam			简单查询参数
	 * @param queryField			简单查询条件字段
	 * @param cityId				高级查询属地过滤
	 * @param onlineStatus			高级查询是否在线过滤
	 * @param vendorId				高级查询厂商过滤
	 * @param deviceModelId			高级查询设备型号过滤
	 * @param devicetypeId			高级查询软件版本过滤
	 * @param bindType				高级查询是否绑定过滤
	 * @param deviceSerialnumber	高级查询设备序列号模糊匹配
	 * @return
	 */
	public List getDeviceList(String gw_type,long areaId,String queryType,String queryParam,	String queryField,
			String cityId,String onlineStatus,String vendorId,
			String deviceModelId,String devicetypeId,String bindType,
			String deviceSerialnumber){
		logger.warn("queryField=>queryField="+queryField);
		List list = null;
		if("1".equals(queryType)){
			if("deviceSn".equals(queryField)){
				list = gwDeviceDao.queryDevice(gw_type, areaId, cityId, null, null, null, null, queryParam, null);
			}else if("deviceIp".equals(queryField)){
				list = gwDeviceDao.queryDevice(gw_type, areaId, cityId, null, null, null, null, null, queryParam);
			}else if("username".equals(queryField)){
				list = gwDeviceDao.queryDevice(gw_type, areaId, cityId , queryParam);
			}else{
				list = gwDeviceDao.queryDeviceByFieldOr(gw_type,areaId, queryParam,cityId);
			}
		}else{
			if(null!=onlineStatus && !"".equals(onlineStatus) && !"-1".equals(onlineStatus)){
				list = gwDeviceDao.queryDeviceByLikeStatus(gw_type, areaId, cityId, vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber, null, onlineStatus);
			}else{
				list = gwDeviceDao.queryDevice(gw_type,areaId, cityId, vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber, null);
			}
		}
		if(null==list || list.size()<1){
			logger.warn("未查询到相关记录");
		}
		return list;
	}
	/**
	 * 查询设备（带列表）
	 * 
	 * @param curPage_splitPage
	 *            分页选项当前页数
	 * @param num_splitPage
	 *            分页选项每页显示数
	 * @param areaId
	 *            登录人的areaId
	 * @param queryType
	 *            查询类型(1:简单查询；2:高级查询)
	 * @param queryParam
	 *            简单查询参数
	 * @param queryField
	 *            简单查询条件字段
	 * @param cityId
	 *            高级查询属地过滤
	 * @param onlineStatus
	 *            高级查询是否在线过滤
	 * @param vendorId
	 *            高级查询厂商过滤
	 * @param deviceModelId
	 *            高级查询设备型号过滤
	 * @param devicetypeId
	 *            高级查询软件版本过滤
	 * @param bindType
	 *            高级查询是否绑定过滤
	 * @param deviceSerialnumber
	 *            高级查询设备序列号模糊匹配
	 * @return
	 */
	public List getDeviceList(int curPage_splitPage, int num_splitPage, long areaId,
			String queryType, String queryParam, String queryField, String cityId,
			String startLastTime, String endLastTime, String vendorId,
			String deviceModelId, String devicetypeId, String bindType,
			String deviceSerialnumber, String deviceIp, String username,
			String servAccount, Boolean noChildCity, String completeStartTime,
			String completeEndTime)
	{
		logger.debug("GwDeviceQueryBIO=>getDeviceList");
		List list = null;
		if ("1".equals(queryType))
		{
			if ("deviceSn".equals(queryField))
			{
				list = gwDeviceDao.queryDevice(curPage_splitPage, num_splitPage, areaId,
						cityId, null, null, null, null, queryParam, null, null, null,
						null, null, noChildCity, completeStartTime, completeEndTime);
			}
			else if ("deviceIp".equals(queryField))
			{
				list = gwDeviceDao.queryDevice(curPage_splitPage, num_splitPage, areaId,
						cityId, null, null, null, null, null, queryParam, null, null,
						null, null, noChildCity, completeStartTime, completeEndTime);
			}
			else
			{
				list = gwDeviceDao.queryDeviceByFieldOr(curPage_splitPage, num_splitPage,
						cityId, queryParam, noChildCity);
			}
		}
		else
		{
			list = gwDeviceDao.queryDevice(curPage_splitPage, num_splitPage, areaId,
					cityId, vendorId, deviceModelId, devicetypeId, bindType,
					deviceSerialnumber, deviceIp, username, servAccount, startLastTime,
					endLastTime, noChildCity, completeStartTime, completeEndTime);
		}
		return list;
	}

	/**
	 * 查询零配置设备
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param queryType
	 * @param queryParam
	 * @param queryField
	 * @param cityId
	 * @param deviceSerialnumber
	 * @param loopbackIp
	 * @param username
	 * @param servAccount
	 * @param noChildCity
	 * @param completeStartTime
	 * @param completeEndTime
	 * @param addressingType
	 * @param status
	 * @param cpeMac
	 * @return
	 */
	public List<Map> getZeroDeviceList(int curPage_splitPage, int num_splitPage,
			String cityId,
			String deviceSerialnumber, String loopbackIp, String username,
			String servAccount, Boolean noChildCity, String startLastTime1,
			String endLastTime1,String addressingType,String status,
			String cpeMac,String failReason,String device_status,String loopbackIpSix)
	{
		logger.debug("GwDeviceQueryBIO=>getZeroDeviceList");
		List<Map> list = gwDeviceDao.queryZeroDevice(curPage_splitPage, num_splitPage,
					cityId, deviceSerialnumber, loopbackIp, username, servAccount, 
					noChildCity, startLastTime1, endLastTime1,
					addressingType,status,cpeMac,failReason,device_status,loopbackIpSix);
		return list;
	}

	/**
	 * 查询零配置记录数
	 * @param cityId
	 * @param deviceSerialnumber
	 * @param loopbackIp
	 * @param username
	 * @param servAccount
	 * @param noChildCity
	 * @param completeStartTime
	 * @param completeEndTime
	 * @param addressingType
	 * @param status
	 * @param cpeMac
	 * @return
	 */
	public int getZeroDeviceListCount(String cityId, 
			String deviceSerialnumber, String loopbackIp, String username,
			String servAccount, boolean noChildCity, String completeStartTime,
			String completeEndTime,String addressingType,String status,
			String cpeMac,String failReason,String device_status,String loopbackIpSix)
	{
		logger.debug("GwDeviceQueryBIO=>getZeroDeviceListCount");
		int count = gwDeviceDao.queryZeroDeviceCount(cityId,username,servAccount, deviceSerialnumber,
						loopbackIp, noChildCity, completeStartTime, completeEndTime,
						addressingType,status,cpeMac,failReason,device_status,loopbackIpSix);
		return count;
	}

	/**
	 * 获取状态List
	 * @return
	 */
	public List<Map<String, String>> getStatusList()
	{
		return gwDeviceDao.getStatusList();
	}
	
	/**
	 * 查询设备（带列表）
	 * 
	 * @param areaId
	 *            登录人的areaId
	 * @param queryType
	 *            查询类型(1:简单查询；2:高级查询)
	 * @param queryParam
	 *            简单查询参数
	 * @param queryField
	 *            简单查询条件字段
	 * @param cityId
	 *            高级查询属地过滤
	 * @param onlineStatus
	 *            高级查询是否在线过滤
	 * @param vendorId
	 *            高级查询厂商过滤
	 * @param deviceModelId
	 *            高级查询设备型号过滤
	 * @param devicetypeId
	 *            高级查询软件版本过滤
	 * @param bindType
	 *            高级查询是否绑定过滤
	 * @param deviceSerialnumber
	 *            高级查询设备序列号模糊匹配
	 * @return
	 */
	public int getDeviceListCount(long areaId, String queryType, String queryParam,
			String queryField, String cityId, String startLastDate, String endLastDate,
			String vendorId, String deviceModelId, String devicetypeId, String bindType,
			String deviceSerialnumber, String deviceIp, String username,
			String servAccount, boolean noChildCity, String completeStartTime,
			String completeEndTime)
	{
		logger.debug("GwDeviceQueryBIO=>getDeviceListCount");
		int count = 0;
		if ("1".equals(queryType))
		{
			if ("deviceSn".equals(queryField))
			{
				count = gwDeviceDao.queryDeviceCount(areaId, cityId, null, null, null,
						null, queryParam, null, noChildCity, completeStartTime,
						completeEndTime, null, null);
			}
			else if ("deviceIp".equals(queryField))
			{
				count = gwDeviceDao.queryDeviceCount(areaId, cityId, null, null, null,
						null, null, queryParam, noChildCity, completeStartTime,
						completeEndTime, null, null);
			}
			else
			{
				count = gwDeviceDao.queryDeviceByFieldOrCount(cityId, queryParam,
						noChildCity);
			}
		}
		else
		{
			if ((null != username && !"".equals(username))
					|| (null != servAccount && !"".equals(servAccount)))
			{
				count = gwDeviceDao.queryDeviceByUsernameCount(areaId, cityId, vendorId,
						deviceModelId, devicetypeId, bindType, deviceSerialnumber,
						deviceIp, username, servAccount, noChildCity, startLastDate,
						endLastDate);
			}
			else
			{
				// if(null!=onlineStatus && !"".equals(onlineStatus) &&
				// !"-1".equals(onlineStatus)){
				// count = gwDeviceDao.queryDeviceByLikeStatusCount(areaId, cityId,
				// vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber,
				// deviceIp, onlineStatus,noChildCity);
				// }else{
				count = gwDeviceDao.queryDeviceCount(areaId, cityId, vendorId,
						deviceModelId, devicetypeId, bindType, deviceSerialnumber,
						deviceIp, noChildCity, completeStartTime, completeEndTime,
						startLastDate, endLastDate);
				// }
			}
		}
		return count;
	}
	
	
	/**
	 * 查询属地
	 * 
	 * @return
	 */
	public String getCity(String cityId)
	{
		logger.debug("GwDeviceQueryBIO=>getCity(cityId:{})", cityId);
		List list = CityDAO.getNextCityListByCityPid(cityId);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++)
		{
			Map map = (Map) list.get(i);
			if (i > 0)
			{
				bf.append("#");
			}
			bf.append(map.get("city_id"));
			bf.append("$");
			bf.append(map.get("city_name"));
		}
		return bf.toString();
	}

	/**
	 * 查询设备厂商
	 * 
	 * @return
	 */
	public String getVendor()
	{
		logger.debug("GwDeviceQueryBIO=>getVendor()");
		List list = vmvDaoStb.getVendor();
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++)
		{
			Map map = (Map) list.get(i);
			if (i > 0)
			{
				bf.append("#");
			}
			bf.append(map.get("vendor_id"));
			bf.append("$");
			bf.append(map.get("vendor_add"));
			bf.append("(");
			bf.append(map.get("vendor_name"));
			bf.append(")");
		}
		return bf.toString();
	}

	/**
	 * 查询设备型号
	 * 
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModel(String vendorId)
	{
		logger.debug("GwDeviceQueryBIO=>getDeviceModel(vendorId:{})", vendorId);
		List list = vmvDaoStb.getDeviceModel(vendorId);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++)
		{
			Map map = (Map) list.get(i);
			if (i > 0)
			{
				bf.append("#");
			}
			bf.append(map.get("device_model_id"));
			bf.append("$");
			bf.append(map.get("device_model"));
		}
		return bf.toString();
	}

	/**
	 * 查询设备版本
	 * 
	 * @param deviceModelId
	 * @return
	 */
	public String getDevicetype(String deviceModelId)
	{
		logger.debug("GwDeviceQueryBIO=>getDevicetype(deviceModelId:{})", deviceModelId);
		List list = vmvDaoStb.getVersionList(deviceModelId);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++)
		{
			Map map = (Map) list.get(i);
			if (i > 0)
			{
				bf.append("#");
			}
			bf.append(map.get("devicetype_id"));
			bf.append("$");
			bf.append(map.get("softwareversion"));
		}
		return bf.toString();
	}
	
	public List<Map> querygwZeroDetail(String deviceId) {
		return gwDeviceDao.querygwZeroDetail(deviceId);
	}
	
	public List<Map> querygwZeroDetailTo3(String deviceId){
		List<Map> zerogwInfoList = gwDeviceDao.querygwZeroDetailTo3(deviceId);
		convertToChinese(zerogwInfoList);
		return zerogwInfoList;
	}
	
	public List<Map> querygwZeroDetailPage(int curPageSplitPage, int numSplitPage, String deviceId){
		List<Map> zerogwInfoList = gwDeviceDao.querygwZeroDetailPage(curPageSplitPage,numSplitPage,deviceId);
		convertToChinese(zerogwInfoList);
		return zerogwInfoList;
	}
	
	private void convertToChinese(List<Map> zerogwInfoList){		
		for(Map map : zerogwInfoList){
			map.put("city_name", CityDAO.getCityName(StringUtil.getStringValue(map.get("city_id"))));
			if(map.get("bind_way") != null){
				switch (Integer.parseInt((String)map.get("bind_way"))) {
				case 0:
					map.put("bind_way", "ITMS MAC绑定");
					map.put("bind_type", "零配置");
					break;
				case 1:
					map.put("bind_way", "AAA IP绑定");
					map.put("bind_type", "零配置");
					break;
				case 2:
					map.put("bind_way", "宽带账号自助安装");
					map.put("bind_type", "零配置");
					break;
				case 3:
					map.put("bind_way", "其他绑定");
					map.put("bind_type", "其他绑定");
					break;
				case 4:
					map.put("bind_way", "更换机顶盒");
					map.put("bind_type", "更换机顶盒");
					break;
				case 5:
					map.put("bind_way", "爱运维开通");
					map.put("bind_type", "零配置");
					break;
				default:
					map.put("bind_way", "无此方式");
					map.put("bind_type", "无此类型");
					break;
				}
			}else{
				map.put("bind_way", "类型为空");
			}
			if(map.get("fail_reason_id") != null){
				switch (StringUtil.getIntegerValue(map.get("fail_reason_id"))) {
				case 6:
					map.put("fail_reason_id", "成功");
					break;
				case 13:
					map.put("fail_reason_id", "成功");
					break;
				case 14:
					map.put("fail_reason_id", "成功");
					break;
				default:
					map.put("fail_reason_id", "失败");
					break;
				}
			}
			else{
				map.put("fail_reason_id", "无此状态");
			}			
		}
	}
	
	public int querygwZeroDetailCount(String deviceId){
		return gwDeviceDao.querygwZeroDetailCount(deviceId);
	}
	
	public List<Map> querygwWorkDetail(String deviceId) {
		return gwDeviceDao.querygwWorkDetail(deviceId);
	}

	public List<Map> queryFailReason() {
		return gwDeviceDao.queryFailReason();
	}
	
	public GwStbVendorModelVersionDAO getVmvDaoStb()
	{
		return vmvDaoStb;
	}

	public void setVmvDaoStb(GwStbVendorModelVersionDAO vmvDaoStb)
	{
		this.vmvDaoStb = vmvDaoStb;
	}

	public GwDeviceQueryDAO getGwDeviceDao()
	{
		return gwDeviceDao;
	}

	public void setGwDeviceDao(GwDeviceQueryDAO gwDeviceDao)
	{
		this.gwDeviceDao = gwDeviceDao;
	}
}
