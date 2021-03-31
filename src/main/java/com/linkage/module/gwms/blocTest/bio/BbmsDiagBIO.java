package com.linkage.module.gwms.blocTest.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.blocTest.dao.BbmsDiagDAO;
import com.linkage.module.gwms.init.obj.CpeFaultcodeOBJ;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;


public class BbmsDiagBIO
{
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BbmsDiagBIO.class);
	
	private BbmsDiagDAO dao;

	public List<Map<String, String>> getDeviceWan(String deviceId, String gw_type)
	{
		
		int rsint = new SuperGatherCorba(gw_type).getCpeParams(deviceId, 2);
				
		List<Map<String,String>> list = new ArrayList<Map<String, String>>();
		
		//成功
		if(rsint==1){
			list = dao.getDeviceWan(deviceId);
		}else{
			CpeFaultcodeOBJ obj = Global.G_Fault_Map.get(rsint);
			String corbaMsg  = null;
			if (null == obj)
			{
				logger.warn("can not find fault reason. rsint:" + rsint);
				corbaMsg = Global.G_Fault_Map.get(100000).getFaultReason();
			} else
			{
				corbaMsg = obj.getFaultReason();

				if (null == corbaMsg)
				{
					corbaMsg = Global.G_Fault_Map.get(100000).getFaultReason();
				}
			}
			Map<String,String> map = new HashMap<String,String>();
			map.put("corbaMsg", corbaMsg);
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 获取设备上行方式
	 * @param deviceId
	 * @return
	 */
	public String getAccessType(String deviceId)
	{
		return dao.getAccessType(deviceId);
	}

	public Map<String, String> getUserServ(String deviceId)
	{
		return dao.getUserServ(deviceId);
	}

	
	/**
	 * @return the dao
	 */
	public BbmsDiagDAO getDao()
	{
		return dao;
	}

	
	/**
	 * @param dao the dao to set
	 */
	public void setDao(BbmsDiagDAO dao)
	{
		this.dao = dao;
	}
}
