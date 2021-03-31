/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.cao.gw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.cao.gw.interf.I_CAO;
import com.linkage.module.gwms.util.PreProcessInterface;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;

/**
 * CAO:QoS.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 25, 2009
 * @see
 * @since 1.0
 */
public class QosCAO implements I_CAO {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(QosCAO.class);

	/** PreProcess CORBA */
	private PreProcessInterface ppCorba;

	/** SuperGather CORBA */
	private SuperGatherCorba sgCorba;

	/** sg type */
	private final int sgType = 5;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkage.module.gwms.diagnostics.cao.interf.I_AdvanceSearchCAO#addStrategyToPP(java.lang.String)
	 */
	@Override
	public boolean addStrategyToPP(String id) {
		logger.debug("addStrategyToPP({})", id);

		return ppCorba.processOOBatch(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkage.module.gwms.diagnostics.cao.interf.I_AdvanceSearchCAO#getDataFromSG(java.lang.String,
	 *      int)
	 */
	@Override
	public int getDataFromSG(String deviceId, int type) {
		logger.debug("getDataFromSG({},{})", deviceId, sgType);

		int invokeType = 1;
		
		String gw_type = LipossGlobals.getGw_Type(deviceId);
		sgCorba = new SuperGatherCorba(String.valueOf(gw_type));
		
		return sgCorba.getCpeParams(deviceId, sgType, invokeType);
	}

	/**
	 * set:ppCorba
	 * 
	 * @param ppCorba
	 *            the ppCorba to set
	 */
	public void setPpCorba(PreProcessInterface ppCorba) {
		logger.debug("setPpCorba({})", ppCorba);

		this.ppCorba = ppCorba;
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
