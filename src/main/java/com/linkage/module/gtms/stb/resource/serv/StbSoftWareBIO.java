
package com.linkage.module.gtms.stb.resource.serv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.StaticTypeCommon;
import com.linkage.module.gtms.stb.resource.dao.StbSoftWareDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author zzs (Ailk No.78987)
 * @version 1.0
 * @since 2018-10-19
 * @category com.linkage.module.gtms.stb.resource.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class StbSoftWareBIO
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(StbSoftWareBIO.class);
	private StbSoftWareDAO dao;

	/**
	 * 设备批量升级
	 * 
	 * @author zzs
	 * @date Dec 24, 2018
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void batchUp(long accoid, String[] deviceId_array, String softStrategy_type)
	{
		logger.debug("batchUp({},{},{})", new Object[] { accoid, deviceId_array,
				softStrategy_type });
		// 获取配置参数(XML)字符串
		ArrayList<String> sqllist = new ArrayList<String>();
		Map<String, Map> softParamMap = dao.getSoftFileInfo();
		Map softUpMap = dao.getSoftUp();
		String deviceTypeId = "";
		String devicetype_id = "";
		String softSheet_para = "";
		String taskId = "";
		int size = deviceId_array.length;
		// 软件升级策略ID数组
		String[] strategyId_array = new String[size];
		// 级策略ID
		long id = 0L;
		/** 入策略表 */
		int softType = StringUtil.getIntegerValue(softStrategy_type);
		for (int i = 0; i < size; i++)
		{
			devicetype_id = dao.getDevicetypeId(deviceId_array[i]);
			deviceTypeId = (String) softUpMap.get("1|" + devicetype_id);
			if (deviceTypeId == null || "".equals(deviceTypeId))
			{
				// 如果映射表没有该devicetype_id的纪录，默认不需要升级
				deviceTypeId = devicetype_id;
				logger.debug("deviceTypeId为空{}", devicetype_id);
			}
			// 目标版本和原设备的版本一致，不需要升级
			if (deviceTypeId.equals(devicetype_id))
			{
				logger.warn("[{}]目标版本和原设备版本一直，不需要升级", deviceId_array[i]);
			}
			else
			{
				/*
				 * softSheet_para = softUpXml(softParamMap.get(deviceTypeId)); //
				 * 生成入策略的sql taskId =
				 * StringUtil.getStringValue(StaticTypeCommon.generateLongId()); id =
				 * StrategyOBJ.createStrategyId(); List sql = inStrategy(id, accoid,
				 * softType, deviceId_array[i], 5, 1, 2, softSheet_para, taskId, 5, 1);
				 * strategyId_array[i] = StringUtil.getStringValue(id);
				 * sqllist.addAll(sql); if(sqllist.size() >= 100){
				 * DBOperation.executeUpdate(sqllist); sqllist.clear(); }
				 */
			}
		}
		DBOperation.executeUpdate(sqllist);
		// 启用一个新的线程来做入库
		// LipossGlobals.ALL_SQL_IPTV.addAll(sqllist);
	}

	public int addToTask(long taskid, String vendorId, long cityId, String pathId,
			String strategyType, long accoid, String devicetypeId, String ipCheck,
			String ipSG, String taskDesc, String param_sql)
	{
		return dao.addToTask(taskid, vendorId, cityId, pathId, strategyType, accoid,
				devicetypeId, ipCheck, ipSG, taskDesc, param_sql);
	}

	public String getDeviceTypeId(String deviceId)
	{
		return dao.getDevicetypeId(deviceId);
	}

	/**
	 * @return the dao
	 */
	public StbSoftWareDAO getDao()
	{
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(StbSoftWareDAO dao)
	{
		this.dao = dao;
	}
}
