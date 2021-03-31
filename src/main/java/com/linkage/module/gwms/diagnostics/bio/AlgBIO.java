/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.diagnostics.bio;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.StaticTypeCommon;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.cao.gw.AlgCAO;
import com.linkage.module.gwms.dao.gw.AlgDAO;
import com.linkage.module.gwms.diagnostics.bio.interf.I_AdvanceSearchBIO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.obj.gw.AlgOBJ;
import com.linkage.module.gwms.obj.interf.I_DevConfOBJ;
import com.linkage.module.gwms.resource.dao.DevDAO;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.StringUtil;

/**
 * BIO: advance search.ALG,MWBAN,WAN,X_COM_Passwd.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 16, 2009
 * @see
 * @since 1.0
 */
public class AlgBIO implements I_AdvanceSearchBIO {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(AlgBIO.class);

	/** type of SG */
	private final int sgType = 4;

	/** DAO */
	private DevDAO devDAO;

	/** DAO */
	private AlgDAO algDAO;

	/** CAO */
	private AlgCAO algCAO;

	/** 结果 */
	private int resultId = -9;

	/** 结果描述 */
	private String result;

	/**
	 * get data from db.
	 * 
	 * @param deviceId
	 * @return
	 */
	public AlgOBJ getData(String deviceId) {
		logger.debug("getData({})", deviceId);

		AlgOBJ obj = null;

		if (deviceId == null) {
			logger.warn("getData deviceId is null");
			resultId = -9;
			return obj;
		}

		// SG
		if ((resultId = algCAO.getDataFromSG(deviceId, sgType)) != 1) {
			logger.warn("getData sg fail");

			return obj;
		}

		// db
		Map map = algDAO.getData(deviceId);
		if (map == null) {
			logger.debug("map == null");
			resultId = 0;
			return obj;
		}

		obj = new AlgOBJ();
		obj.setDeviceId(deviceId);
		obj.setGatherTime(StringUtil.getLongValue(map, "gather_time"));
		obj.setH323Enab(StringUtil.getIntValue(map, "h323_enab"));
		obj.setSipEnab(StringUtil.getIntValue(map, "sip_enab"));
		obj.setRtspEnab(StringUtil.getIntValue(map, "rtsp_enab"));
		obj.setL2tpEnab(StringUtil.getIntValue(map, "l2tp_enab"));
		obj.setIpsecEnab(StringUtil.getIntValue(map, "ipsec_enab"));

		return obj;
	}

	/**
	 * config device.
	 * 
	 * @param obj
	 * @return 成功返回'1|ID', 失败返回'0|ID'
	 */
	public String configDev(I_DevConfOBJ obj, long accOId, int type,
			int serviceId, String gw_type) {
		logger.debug("configDev({})", new Object[] { obj, accOId, type,
				serviceId });

		boolean flag = false;

		AlgOBJ algOBJ = (AlgOBJ) obj;

		Map map = devDAO.getDevUserByDevId(algOBJ.getDeviceId());

		StrategyOBJ strategyOBJ = new StrategyOBJ();
		strategyOBJ.setId(StaticTypeCommon.generateLongId());
		strategyOBJ.setDeviceId(algOBJ.getDeviceId());
		strategyOBJ.setOui(StringUtil.getStringValue(map, "oui"));
		strategyOBJ
				.setSn(StringUtil.getStringValue(map, "device_serialnumber"));
		strategyOBJ.setUsername(StringUtil.getStringValue(map, "username"));
		strategyOBJ.setServiceId(serviceId);
		strategyOBJ.setTime(TimeUtil.getCurrentTime());

		StringBuilder sheetPara = new StringBuilder();
		sheetPara.append(algOBJ.getH323Enab());
		sheetPara.append("|||" + algOBJ.getSipEnab());
		sheetPara.append("|||" + algOBJ.getRtspEnab());
		sheetPara.append("|||" + algOBJ.getL2tpEnab());
		sheetPara.append("|||" + algOBJ.getIpsecEnab());
		strategyOBJ.setSheetPara(sheetPara.toString());

		strategyOBJ.setAccOid(accOId);
		strategyOBJ.setOrderId(1);
		strategyOBJ.setType(type);
		strategyOBJ.setTempId(serviceId);
		strategyOBJ.setIsLastOne(1);
		
		// PP
		if (true == algDAO.addStrategy(strategyOBJ)) {
			CreateObjectFactory.createPreProcess(gw_type).processOOBatch(String.valueOf(strategyOBJ.getId()));
			flag = true;
		} else {
			resultId = -9;
		}

		resultId = 0;

		return (flag == true ? 1 : 0) + "|" +  strategyOBJ.getId();
	}

	/**
	 * set: DAO
	 * 
	 * @param devDAO
	 *            the dao to set
	 */
	public void setDevDAO(DevDAO devDAO) {
		logger.debug("setDevDAO(DevDAO)");

		this.devDAO = devDAO;
	}

	/**
	 * set: DAO
	 * 
	 * @param algDAO
	 *            the dao to set
	 */
	public void setAlgDAO(AlgDAO algDAO) {
		logger.debug("setDAO(AlgDAO)");

		this.algDAO = algDAO;
	}

	/**
	 * set: CAO
	 * 
	 * @param cao
	 *            the cao to set
	 */
	public void setAlgCAO(AlgCAO algCAO) {
		logger.debug("setCAO(AlgCAO)");

		this.algCAO = algCAO;
	}

	/**
	 * get:结果描述
	 * 
	 * @return the result
	 */
	public String getResult() {
		logger.debug("getResult()");

		result = Global.G_Fault_Map.get(resultId).getFaultReason();
		if (result == null) {
			logger.debug("flag == null");

			result = Global.G_Fault_Map.get(100000).getFaultReason();
		}

		return result;
	}

	/**
	 * 获取ALG开关信息
	 *
	 * @author wangsenbo
	 * @date Dec 11, 2009
	 * @return AlgOBJ
	 */
	public AlgOBJ getAlg(String deviceId)
	{
		logger.debug("getAlg({})", deviceId);

		AlgOBJ obj = null;

		if (deviceId == null) {
			logger.warn("getAlg deviceId is null");
			resultId = -9;
			return obj;
		}

		// SG
		if ((resultId = algCAO.getDataFromSG(deviceId, sgType)) != 1) {
			logger.warn("getAlg sg fail");

			return obj;
		}

		// db
		Map map = algDAO.getAlg(deviceId);
		if (map == null) {
			logger.debug("map == null");
			resultId = 0;
			return obj;
		}

		obj = new AlgOBJ();
		obj.setDeviceId(deviceId);
		obj.setGatherTime(StringUtil.getLongValue(map, "gather_time"));
		obj.setH323Enab(StringUtil.getIntValue(map, "h323_enab"));
		obj.setSipEnab(StringUtil.getIntValue(map, "sip_enab"));
		obj.setRtspEnab(StringUtil.getIntValue(map, "rtsp_enab"));

		return obj;
	}

	public boolean algConfig(I_DevConfOBJ obj, long accOId, int strategyType, int serviceId)
	{
		logger.debug("algConfig({})", new Object[] { obj, accOId, strategyType,
				serviceId });

		boolean flag = false;

		AlgOBJ algOBJ = (AlgOBJ) obj;

		Map map = devDAO.getDevUserByDevId(algOBJ.getDeviceId());

		StrategyOBJ strategyOBJ = new StrategyOBJ();
		strategyOBJ.setId(StaticTypeCommon.generateLongId());
		strategyOBJ.setDeviceId(algOBJ.getDeviceId());
		strategyOBJ.setOui(StringUtil.getStringValue(map, "oui"));
		strategyOBJ
				.setSn(StringUtil.getStringValue(map, "device_serialnumber"));
		strategyOBJ.setUsername(StringUtil.getStringValue(map, "username"));
		strategyOBJ.setServiceId(serviceId);
		strategyOBJ.setTime(TimeUtil.getCurrentTime());

		StringBuilder sheetPara = new StringBuilder();
		sheetPara.append(algOBJ.getH323Enab());
		sheetPara.append("|||" + algOBJ.getSipEnab());
		sheetPara.append("|||" + algOBJ.getRtspEnab());
		strategyOBJ.setSheetPara(sheetPara.toString());

		strategyOBJ.setAccOid(accOId);
		strategyOBJ.setOrderId(1);
		strategyOBJ.setType(strategyType);
		strategyOBJ.setTempId(serviceId);
		strategyOBJ.setIsLastOne(1);
		String gw_type = LipossGlobals.getGw_Type(algOBJ.getDeviceId());
		// PP
		if (true == algDAO.addStrategy(strategyOBJ)) {
			CreateObjectFactory.createPreProcess(gw_type+"").processOOBatch(String.valueOf(strategyOBJ.getId()));
			flag = true;
		} else {
			resultId = -9;
		}

		resultId = 0;

		return flag;
	}

}
