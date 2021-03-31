/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.diagnostics.bio;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.StaticTypeCommon;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.cao.gw.XcompasswdCAO;
import com.linkage.module.gwms.dao.gw.XcompasswdDAO;
import com.linkage.module.gwms.diagnostics.bio.interf.I_AdvanceSearchBIO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.obj.gw.XcompasswdOBJ;
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
public class XcompasswdBIO implements I_AdvanceSearchBIO {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(XcompasswdBIO.class);

	/** type of SG */
	private final int sgType = 31;

	/** DAO */
	private DevDAO devDAO;

	/** DAO */
	private XcompasswdDAO xcompasswdDAO;

	/** CAO */
	private XcompasswdCAO xcompasswdCAO;

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
	public XcompasswdOBJ getData(String deviceId)
	{
		logger.debug("getData({})", deviceId);
		XcompasswdOBJ obj = null;
		if (deviceId == null)
		{
			logger.warn("getData deviceId is null");
			resultId = -9;
			return obj;
		}
		// SG
		if ((resultId = xcompasswdCAO.getDataFromSG(deviceId, sgType)) != 1)
		{
			logger.warn("getData sg fail");
			return obj;
		}
		// db
		Map map = xcompasswdDAO.getData(deviceId);
		if (map == null)
		{
			logger.debug("map == null");
			resultId = 0;
			return obj;
		}
		obj = new XcompasswdOBJ();
		obj.setDeviceId(deviceId);
		obj.setXcompasswd(StringUtil.getStringValue(map, "x_com_passwd"));
		obj.setDeviceSn(StringUtil.getStringValue(map, "device_serialnumber"));
		return obj;
	}

	/**
	 * config device.
	 * 
	 * @param obj
	 * @return
	 */
	public Boolean configDev(I_DevConfOBJ obj, long accOId, int type,
			int serviceId, String gw_type) {
		logger.debug("configDev({})", new Object[] { obj, accOId, type,
				serviceId });

		boolean flag = false;

		XcompasswdOBJ xcompasswdOBJ = (XcompasswdOBJ) obj;

		Map map = devDAO.getDevUserByDevId(xcompasswdOBJ.getDeviceId());

		StrategyOBJ strategyOBJ = new StrategyOBJ();
		strategyOBJ.setId(StaticTypeCommon.generateLongId());
		strategyOBJ.setDeviceId(xcompasswdOBJ.getDeviceId());
		strategyOBJ.setOui(StringUtil.getStringValue(map, "oui"));
		strategyOBJ
				.setSn(StringUtil.getStringValue(map, "device_serialnumber"));
		strategyOBJ.setUsername(StringUtil.getStringValue(map, "username"));
		strategyOBJ.setServiceId(serviceId);
		strategyOBJ.setTime((new Date()).getTime() / 1000);

		StringBuilder sheetPara = new StringBuilder();
		sheetPara.append(xcompasswdOBJ.getXcompasswd());
		strategyOBJ.setSheetPara(sheetPara.toString());

		strategyOBJ.setAccOid(accOId);
		strategyOBJ.setOrderId(1);
		strategyOBJ.setType(type);
		strategyOBJ.setTempId(serviceId);
		strategyOBJ.setIsLastOne(1);
		
		flag = xcompasswdDAO.addStrategy(strategyOBJ);
		// PP
		if (flag == true && type == 0) {
			flag = CreateObjectFactory.createPreProcess(gw_type).processOOBatch(String.valueOf(strategyOBJ
					.getId()));
		}

		if (flag == true) {
			resultId = 0;
		} else {
			resultId = -9;
		}

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
	 * @param xcompasswdDAO
	 *            the dao to set
	 */
	public void setXcompasswdDAO(XcompasswdDAO xcompasswdDAO) {
		logger.debug("setXcompasswd(Xcompasswd)");

		this.xcompasswdDAO = xcompasswdDAO;
	}

	/**
	 * set: CAO
	 * 
	 * @param xcompasswdCAO
	 *            the cao to set
	 */
	public void setXcompasswdCAO(XcompasswdCAO xcompasswdCAO) {
		logger.debug("setXcompasswdCAO(XcompasswdCAO)");

		this.xcompasswdCAO = xcompasswdCAO;
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
