package com.linkage.module.gwms.cao.gw;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.cao.gw.interf.I_CAO;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileObj;
import com.linkage.module.gwms.util.PreProcessInterface;
import com.linkage.module.gwms.util.corba.ACSCorba;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;

public class VoipCAO implements I_CAO {
	
	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(VoipCAO.class);
	
	/** ACS CORBA */
	private PreProcessInterface ppCorba;
	
	/** SuperGather CORBA */
	private SuperGatherCorba sgCorba;

	/** ACS CORBA */
	private ACSCorba acsCorba;
	
	/**
	 * 删除voip
	 * 
	 * @param wanConnObj
	 * @return
	 */
	public int delVoipInfo(VoiceServiceProfileObj voiceObj) {
		logger.debug("VoiceServiceProfileObj：{}", voiceObj.getDeviceId());

		StringBuilder name = new StringBuilder();
		name.append("InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.Line.");
		name.append(voiceObj.getProfId());
		name.append(".");
		String voipPath = name.toString();
		logger.warn("删除设备VOIP：{}的路径：{}", voiceObj.getDeviceId(), voipPath);
		
		String gw_type = LipossGlobals.getGw_Type(voiceObj.getDeviceId());
		acsCorba = new ACSCorba(String.valueOf(gw_type));
		
		return acsCorba.del(voiceObj.getDeviceId(), voipPath);
	}
	
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

	/**
	 * @return the acsCorba
	 */
	public ACSCorba getAcsCorba() {
		return acsCorba;
	}

	/**
	 * @param acsCorba the acsCorba to set
	 */
	public void setAcsCorba(ACSCorba acsCorba) {
		this.acsCorba = acsCorba;
	}

}
