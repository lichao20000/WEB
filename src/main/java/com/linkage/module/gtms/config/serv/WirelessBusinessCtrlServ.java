package com.linkage.module.gtms.config.serv;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.config.dao.WirelessBusinessCtrlDAO;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.corba.PreProcessCorba;

public class WirelessBusinessCtrlServ {
	private WirelessBusinessCtrlDAO dao;
	// 开启无线
	private static String OPEN_WIRELESS = "2001";
	// 关闭无线
	private static String CLOSE_WIRELESS = "2003";

	private static Logger logger = LoggerFactory
			.getLogger(WirelessBusinessCtrlServ.class);

	public String doConfig(long userId, List<String> list, String gwType,
			String flag, String strategy_type, String vlanIdMark, String ssid,
			String wireless_port, String buss_level, String channel,
			String awifi_type) {
		logger.debug("doConfig({})", list.toString());
		String res = "0";
		String serviceId = "";
		long time = new DateTimeUtil().getLongTime(); // 入表时间，同时为任务id
		if (flag.equals("1")) {
			serviceId = OPEN_WIRELESS;
		} else if (flag.equals("0")) {
			serviceId = CLOSE_WIRELESS;
		} else {
			res = "-1";
		}
		// 入数据库
		try {

			dao.doConfig(userId, list, gwType, serviceId, strategy_type,
					vlanIdMark, ssid, time, wireless_port, buss_level, channel,
					awifi_type);
			res = "1";
		} catch (Exception e) {
			logger.warn("更新表失败");
			res = "-1";
		}
		String[] array = new String[list.size()];

		if ("1".equals(res)) {
			if (1 ==  CreateObjectFactory.createPreProcess(gwType).processDeviceStrategy(
					list.toArray(array), serviceId,
					new String[] { StringUtil.getStringValue(time) })) {
				logger.warn("调用后台预读模块成功");
				res = "1";
			} else {
				logger.warn("调用后台预读模块失败");
				res = "-4";
			}
		}
		return res;
	}
	
	public String doConfigAll(long userId, List<String> list, String gwType,
			String flag, String strategy_type, String vlanIdMark, String ssid,
			String wireless_port, String buss_level, String channel,
			String awifi_type) {
		logger.debug("doConfig({})", list.toString());
		String res = "0";
		String serviceId = "";
		int operType = 0;
		if (flag.equals("1")) {
			serviceId = OPEN_WIRELESS;
			operType=3;
			
		} else if (flag.equals("0")) {
			serviceId = CLOSE_WIRELESS;
			operType=4;
		} else {
			res = "-1";
		}
		long taskId=curMillAndRan(3);
		dao.addBatOptTask(taskId, userId, operType, 
				Integer.parseInt(serviceId), 0, Integer.parseInt(gwType), "", System.currentTimeMillis()/1000);
		List<String> deviceIdList=new ArrayList<String>();
		
		for(int i=0;i<list.size();i++){
			String deviceId = (String)list.get(i);
			deviceIdList.add(deviceId);
		}
		//批量保存
		int[] resultArr=dao.addBatOptDevByBatch(
				deviceIdList,
				taskId, 
				StringUtil.getIntegerValue(serviceId),
				strategy_type, 
				ssid,
				vlanIdMark, 
				StringUtil.getIntegerValue(wireless_port), 
				StringUtil.getIntegerValue(buss_level), 
				channel, 
				StringUtil.getIntegerValue(awifi_type), 
				System.currentTimeMillis()/1000,
				0);
		res = "1";
		for(int result:resultArr)
		{
			if(result!=1)
				{
					res="0";
					break;
				}
		}
		return res;
	}
	
	public String doConfig4Special(long userId, List<String> list, String gwType,
			String flag, String strategy_type, List<String> vlanIdMarkList, List<String> ssidList,
			List<String> wlanportList, List<String> buss_levelList, List<String> channelList, String awifi_type)
	{
		logger.debug("doConfig4Special({})", list.toString());
		String res = "0";
		String serviceId = "";
		long maxId = DBOperation.getMaxId("task_id", "tab_wirelesst_task");
		if (flag.equals("1"))
			serviceId = OPEN_WIRELESS;
		else if (flag.equals("0"))
			serviceId = CLOSE_WIRELESS;
		else {
			res = "-1";
		}

		String dbFlag = "1";
		for (int i = 0; i < list.size(); i++) {
			maxId += 1L;
			String deivceId = (String)list.get(i);
			String vlanid = (String)vlanIdMarkList.get(i);
			String ssid = (String)ssidList.get(i);
			String buss_level = (String)buss_levelList.get(i);
			String channel = (String)channelList.get(i);
			String wireless_port = (String)wlanportList.get(i);
			if(null==deivceId||null==vlanid||null==ssid||null==buss_level||null==channel||null==wireless_port){
				continue;
			}
			long time = new DateTimeUtil().getLongTime();
			if (wireless_port.equalsIgnoreCase("SSID4")){
				wireless_port = "4";
				awifi_type = "1";
			}else{
				wireless_port = "3";
				awifi_type = "2";
			}
			try
			{
				dbFlag = this.dao.doConfig4Special(userId, deivceId, gwType, serviceId, strategy_type, 
						vlanid, ssid, time, wireless_port, buss_level, channel, 
						awifi_type, maxId);
			} catch (Exception e) {
				logger.error("[{}][{}]无线专线开通 更新表失败 Error.\n{}", new Object[] { deivceId, Long.valueOf(maxId), e });
				dbFlag = "-1";
			}
			String[] array = new String[1];
			array[0] = deivceId;

			if ("1".equals(dbFlag)) {
				if (1 == CreateObjectFactory.createPreProcess(gwType).processDeviceStrategy(
						array, serviceId, 
						new String[] { StringUtil.getStringValue(Long.valueOf(maxId)) })) {
					logger.warn("调用后台预读模块成功[{}]", deivceId);
					res = "1";
				} else {
					logger.warn("调用后台预读模块失败[{}]", deivceId);
					res = "-4";
				}
			}
			array = (String[])null;
			if (dbFlag == "1") {
				res = "1";
			}
		}
		return res;
	}


	

	@SuppressWarnings("unchecked")
	public String doConfig4SpecialBatch(long userId, List<String> list, String gwType,
			String flag, String strategy_type, List<String> vlanIdMarkList, List<String> ssidList,
			List<String> wlanportList, List<String> buss_levelList, List<String> channelList, String awifi_type)
	{
		logger.debug("doConfig4Special({})", list.toString());
		String res = "0";
		String serviceId = "";
		if (flag.equals("1"))
			serviceId = OPEN_WIRELESS;
		else if (flag.equals("0"))
			serviceId = CLOSE_WIRELESS;
		else {
			res = "-1";
		}
		long taskId =curMillAndRan(3);
		dao.addBatOptTask(taskId, userId, 2, 
				Integer.parseInt(serviceId), 0, Integer.parseInt(gwType), "", System.currentTimeMillis()/1000);
		List<Map> bodList=new ArrayList<Map>();
		Map bod=null;
		//String dbFlag = "1";
		for (int i = 0; i < list.size(); i++) {
			String deivceId = (String)list.get(i);
			String vlanid = (String)vlanIdMarkList.get(i);
			String ssid = (String)ssidList.get(i);
			String buss_level = (String)buss_levelList.get(i);
			String channel = (String)channelList.get(i);
			String wireless_port = (String)wlanportList.get(i);
			if(null==deivceId||null==vlanid||null==ssid||null==buss_level||null==channel||null==wireless_port){
				continue;
			}
			if (wireless_port.equalsIgnoreCase("SSID4")){
				wireless_port = "4";
				awifi_type = "1";
			}else{
				wireless_port = "3";
				awifi_type = "2";
			}
			bod =new HashMap();
			bod.put("taskId", taskId);
			bod.put("deviceId", deivceId);
			bod.put("serviceId", Integer.parseInt(serviceId));
			bod.put("stategyType", strategy_type);
			bod.put("ssid", ssid);
			bod.put("vlanId", Integer.parseInt(vlanid));
			bod.put("wirelessPort", Integer.parseInt(wireless_port));
			bod.put("bussLevel", Integer.parseInt(buss_level));
			bod.put("channel", channel);
			bod.put("wirelessType", Integer.parseInt(awifi_type));
			bod.put("addTime", System.currentTimeMillis()/1000);
			bod.put("doStatus", 0);
			bodList.add(bod);
		}
		int[] resultArr=dao.addBatOptDevByBatch(bodList);
		res = "1";
		for(int result:resultArr)
		{
			if(result!=1)
				{
					res="0";
					break;
				}
		}
		return res;
	}

	
	/**
	 * 当前秒数值省去三位后，拼接上一个随机正整数
	 * @param ranLen ranLen是这个随机正整数的位数
	 * @return 
	 */
	public static long curMillAndRan(int ranLen){
		if(ranLen<1)
			return 0;
		String curMill=Long.toString(System.currentTimeMillis()/1000);
		String ran= Long.toString(Math.round((Math.random()*9+1)*Math.pow(10,ranLen-1)));
		return Long.parseLong(curMill+ran);
	}

	public String checkLoid(String deviceId, String gwType) {
		Map<String, String> map = dao.checkUserExsists(deviceId, gwType);
		String specId = "";
		String msn = "1,成功";
		if (null != map) {
			specId = StringUtil.getStringValue(map.get("spec_id"));
			// 查询终端的规格
			String specName = dao.getSpecName(specId);
			if ("E8CP11".equals(specName) || "E8CP12".equals(specName)
					|| "E8CG12".equals(specName) || "E8CP02".equals(specName)) {
				logger.warn("终端【" + deviceId + "】" + "不支持无线端口");
				msn = "0,用户规格不支持无线开通";
			}
		}
		return msn;
	}

	public void setDao(WirelessBusinessCtrlDAO dao) {
		this.dao = dao;
	}

	public WirelessBusinessCtrlDAO getDao() {
		return dao;
	}

	public String isHaveStrategy(String deviceId) {
		logger.debug("WirelessbusinessCtrServ-->isHaveStrategy({})",
				new Object[] { deviceId });
		String strategy = dao.isHaveStrategy(deviceId);
		return strategy;
	}

	public String isBindUser(String deviceId) {
		logger.debug("WirelessbusinessCtrServ-->isBindUser({})",
				new Object[] { deviceId });
		String customer_id = dao.isBindUser(deviceId);
		return customer_id;
	}

	/**
	 * 查询是否绑定
	 */
	public String isBind(String gwShare_queryField, String gwShare_queryParam) {
		logger.debug("WirelessbusinessCtrServ-->isBind({},{},{})",
				new Object[] { gwShare_queryField, gwShare_queryParam });
		String strategy = dao.queryDevice(gwShare_queryField,
				gwShare_queryParam);
		return strategy;
	}

	public List getDetailsForPage(String deviceIds, String flag,
			String awifi_type) {
		logger.debug("WirelessbusinessCtrServ-->getDetailsForPage({},{},{})",
				new Object[] { deviceIds, flag, awifi_type });
		List lt = null;
		try {
			lt = dao.getDetailsForPage(deviceIds, flag, awifi_type);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lt;
	}

	public String isAwifi(String deviceId) {
		logger.debug("WirelessbusinessCtrServ-->isAwifi({})",
				new Object[] { deviceId });
		String strategy = dao.isAwifi(deviceId);
		return strategy;
	}


	

	
}
