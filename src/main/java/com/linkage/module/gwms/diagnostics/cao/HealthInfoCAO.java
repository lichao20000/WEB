package com.linkage.module.gwms.diagnostics.cao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;

public class HealthInfoCAO {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(HealthInfoCAO.class);
	

	/** SuperGather CORBA */
	private SuperGatherCorba sgCorba;

	
	public int getDataFromSG(String deviceId, int type) {
		logger.debug("getDataFromSG({},{})", deviceId, type);
		
		sgCorba = new SuperGatherCorba(String.valueOf(LipossGlobals.getGw_Type(deviceId)));
		
		return sgCorba.getCpeParams(deviceId, type);
	}
	

	/**
	 * set:sgCorba
	 * 
	 * @param sgCorba
	 *            the sgCorba to set
	 */
	public void setSgCorba(SuperGatherCorba sgCorba) {
		logger.debug("setSgCorba({})", sgCorba);

		this.sgCorba = sgCorba;
	}
}
