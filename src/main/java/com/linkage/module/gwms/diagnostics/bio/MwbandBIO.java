/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.diagnostics.bio;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.common.StaticTypeCommon;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.cao.gw.MwbandCAO;
import com.linkage.module.gwms.dao.gw.MwbandDAO;
import com.linkage.module.gwms.diagnostics.bio.interf.I_AdvanceSearchBIO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.obj.gw.MwbandOBJ;
import com.linkage.module.gwms.obj.interf.I_DevConfOBJ;
import com.linkage.module.gwms.resource.dao.DevDAO;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.StringUtil;

/**
 * BIO: advance search.MWBAN.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 16, 2009
 * @see
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public class MwbandBIO implements I_AdvanceSearchBIO {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(MwbandBIO.class);

	/** type of SG */
	private final int sgType = 31;

	/** DAO */
	private DevDAO devDAO;

	/** DAO */
	private MwbandDAO mwbandDAO;

	/** CAO */
	private MwbandCAO mwbandCAO;

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
	public MwbandOBJ getData(String deviceId) {
		logger.debug("getData({})", deviceId);

		MwbandOBJ obj = null;

		if (deviceId == null) {
			logger.warn("getData deviceId is null");
			resultId = -9;
			return obj;
		}

		// SG
		if ((resultId = mwbandCAO.getDataFromSG(deviceId, sgType)) != 1) {
			logger.warn("getData sg fail");

			return obj;
		}

		// db
		Map map = mwbandDAO.getData(deviceId);
		if (map == null) {
			logger.debug("map == null");
			resultId = 0;
			return obj;
		}

		obj = new MwbandOBJ();
		obj.setDeviceId(deviceId);
		obj.setGatherTime(StringUtil.getLongValue(map, "gather_time"));
		obj.setMode(StringUtil.getIntValue(map, "m_mode"));
		obj.setTotalNumber(StringUtil.getIntValue(map, "total_number"));
		obj.setStbEnable(StringUtil.getIntValue(map, "stb_enable"));
		obj.setStbNumber(StringUtil.getIntValue(map, "stb_number"));
		obj.setCameraEnable(StringUtil.getIntValue(map, "camera_enable"));
		obj.setCameraNumber(StringUtil.getIntValue(map, "camera_number"));
		obj.setComputerEnable(StringUtil.getIntValue(map, "computer_enable"));
		obj.setComputerNumber(StringUtil.getIntValue(map, "computer_number"));
		obj.setPhoneEnable(StringUtil.getIntValue(map, "phone_enable"));
		obj.setPhoneNumber(StringUtil.getIntValue(map, "phone_number"));

		return obj;
	}

	/**
	 * config device.
	 * 
	 * @param obj
	 * @return
	 */
	public String configDev(I_DevConfOBJ obj, long accOId, int type,
			int serviceId, String gw_type) {
		logger.debug("configDev({})", new Object[] { obj, accOId, type,
				serviceId });

		//boolean flag = false;
		String flag = null;

		MwbandOBJ mwbandOBJ = (MwbandOBJ) obj;

		Map map = devDAO.getDevUserByDevId(mwbandOBJ.getDeviceId());

		StrategyOBJ strategyOBJ = new StrategyOBJ();
		strategyOBJ.setId(StaticTypeCommon.generateLongId());
		strategyOBJ.setDeviceId(mwbandOBJ.getDeviceId());
		strategyOBJ.setOui(StringUtil.getStringValue(map, "oui"));
		strategyOBJ
				.setSn(StringUtil.getStringValue(map, "device_serialnumber"));
		strategyOBJ.setUsername(StringUtil.getStringValue(map, "username"));
		strategyOBJ.setServiceId(serviceId);
		strategyOBJ.setTime(TimeUtil.getCurrentTime());

		StringBuilder sheetPara = new StringBuilder();
		sheetPara.append(mwbandOBJ.getMode());
		sheetPara.append("|||" + mwbandOBJ.getTotalNumber());
		strategyOBJ.setSheetPara(sheetPara.toString());

		strategyOBJ.setAccOid(accOId);
		strategyOBJ.setOrderId(1);
		strategyOBJ.setType(type);
		strategyOBJ.setTempId(serviceId);
		strategyOBJ.setIsLastOne(1);
		
		// PP
		if (true == mwbandDAO.addStrategy(strategyOBJ)) {
			CreateObjectFactory.createPreProcess(gw_type).processOOBatch(String.valueOf(strategyOBJ.getId()));
			flag = "1|" + strategyOBJ.getId();
		} else {
			flag = "0";
			resultId = -9;
		}

		resultId = 0;

		return flag;
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
	 * @param mwbandDAO
	 *            the dao to set
	 */
	public void setMwbandDAO(MwbandDAO mwbandDAO) {
		logger.debug("setMwbandDAO(MwbandDAO)");

		this.mwbandDAO = mwbandDAO;
	}

	/**
	 * set: CAO
	 * 
	 * @param mwbandCAO
	 *            the cao to set
	 */
	public void setMwbandCAO(MwbandCAO mwbandCAO) {
		logger.debug("setMwbandCAO(MwbandCAO)");

		this.mwbandCAO = mwbandCAO;
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

}
