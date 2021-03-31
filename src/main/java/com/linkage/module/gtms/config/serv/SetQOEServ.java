package com.linkage.module.gtms.config.serv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.config.dao.SetQOEDAO;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;

public class SetQOEServ {
	private SetQOEDAO dao;
	// 开启QOE
	private static String OPEN_QOE = "2005";
	// 关闭QOE
	private static String CLOSE_QOE = "2006";
	
	private static Logger logger = LoggerFactory
			.getLogger(SetQOEServ.class);
	
	/**
	 * 查询是否绑定
	 */
	public String isBind(String gwShare_queryField, String gwShare_queryParam) {
		logger.debug("SetQOEServ-->isBind({},{})",
				new Object[] { gwShare_queryField, gwShare_queryParam });
		String strategy = dao.queryDevice(gwShare_queryField,
				gwShare_queryParam);
		return strategy;
	}
	public Map queryQOE(List<String> listChecked){
		String deviceIdStr="";
		for(String device : listChecked){
			deviceIdStr += device+"','";
		}
		int index = deviceIdStr.lastIndexOf(",");
		deviceIdStr = deviceIdStr.substring(0,index);
		return dao.queryQOE(deviceIdStr);
	}
	public Map doQueryQOE(String deviceStr){
		deviceStr=deviceStr+"'";
		return dao.doQueryQOE(deviceStr);
	}
	public Map doConfig(long acc_oid,List<String> listChecked,String service_id,String strategy_type,String set_enable,String set_url,String do_type) {
		String deviceIdStr="";
		for(String device : listChecked){
			deviceIdStr += device+"','";
		}
		int index = deviceIdStr.lastIndexOf(",");
		deviceIdStr = deviceIdStr.substring(0,index);
		logger.warn("deviceIdStr============"+deviceIdStr);
		long add_time = System.currentTimeMillis() / 1000L;
		int randomNum = (int)(Math.random() * 900.0D) + 100;
		long task_id_a = Long.parseLong(add_time +""+ randomNum);
		long task_id = task_id_a;
		Map map = dao.doConfigQOE(task_id, acc_oid, add_time, service_id, strategy_type, set_enable, set_url, deviceIdStr, do_type);
		
		return map;
	}
	public String refrsehMsg(String deviceId) {
		logger.debug("SetQOEServ-->refrsehMsg({})",
				new Object[] { deviceId });
		Map map = new HashMap();
		map = dao.refreshMsg(deviceId, map);
		String msg = map.get("TestDownloadStatus")+","+map.get("MonitorStatus");
		return msg;
	}
	public String isBindUser(String deviceId) {
		logger.debug("SetQOEServ-->isBindUser({})",
				new Object[] { deviceId });
		String customer_id = dao.isBindUser(deviceId);
		return customer_id;
	}
	public String isQoe(String deviceId) {
		logger.debug("SetQOEServ-->isQoe({})",
				new Object[] { deviceId });
		String strategy = dao.isQoe(deviceId);
		return strategy;
	}
	public SetQOEDAO getDao()
	{
		return dao;
	}

	public void setDao(SetQOEDAO dao)
	{
		this.dao = dao;
	}
	public static String getOPEN_QOE()
	{
		return OPEN_QOE;
	}
	public static void setOPEN_QOE(String oPEN_QOE)
	{
		OPEN_QOE = oPEN_QOE;
	}
	public static String getCLOSE_QOE()
	{
		return CLOSE_QOE;
	}
	public static void setCLOSE_QOE(String cLOSE_QOE)
	{
		CLOSE_QOE = cLOSE_QOE;
	}
}
