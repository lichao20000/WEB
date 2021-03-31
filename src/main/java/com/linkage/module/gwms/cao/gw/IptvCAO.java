package com.linkage.module.gwms.cao.gw;

import com.linkage.module.gwms.cao.gw.interf.I_CAO;
import com.linkage.module.gwms.util.PreProcessInterface;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;

public class IptvCAO implements I_CAO {

	/** ACS CORBA */
	private PreProcessInterface ppCorba;
	
	/** SuperGather CORBA */
	private SuperGatherCorba sgCorba;
	
	
	public boolean addStrategyToPP(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	public int getDataFromSG(String deviceId, int type) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @return the ppCorba
	 */
	public PreProcessInterface getPpCorba() {
		return ppCorba;
	}

	/**
	 * @param ppCorba the ppCorba to set
	 */
	public void setPpCorba(PreProcessInterface ppCorba) {
		this.ppCorba = ppCorba;
	}

	/**
	 * @return the sgCorba
	 */
	public SuperGatherCorba getSgCorba() {
		return sgCorba;
	}

	/**
	 * @param sgCorba the sgCorba to set
	 */
	public void setSgCorba(SuperGatherCorba sgCorba) {
		this.sgCorba = sgCorba;
	}
	
}
