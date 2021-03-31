package com.linkage.module.gwms.config.bio;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.corba.ACSCorba;
import com.linkage.tr069.obj.TR069_NODE_ROOT;

public class VoiceConnectionTestBIO {
	
	/** log */
	private static Logger logger = LoggerFactory.getLogger(VoiceConnectionTestBIO.class);
	
	public String services = TR069_NODE_ROOT.TR069 + "Services.";
	public String voiceService = services + "VoiceService.";
	public String phyInterface = voiceService + "1.PhyInterface.";
	
	/**
	 * 设置PhoneConnectivityTest诊断
	 * 
	 * @param deviceId
	 * @param i
	 * @return
	 */
	public int setPhoneConnectivityTest(String deviceId,String i, String gw_type){
		logger.debug("setPhoneConnectivityTest({},{})",deviceId,i);
		ACSCorba acsCorba = new ACSCorba(gw_type);
		return acsCorba.setValue(deviceId,new ParameValueOBJ(phyInterface+i+".Tests.TestSelector","PhoneConnectivityTest", "1"));
		
	}
	
	/**
	 * 获取PhoneConnectivityTest诊断结果
	 * 
	 * @param deviceId
	 * @param i
	 * @return
	 */
	public ArrayList<ParameValueOBJ> getPhoneConnectivityTest(String deviceId,String i, String gw_type){
		logger.debug("setPhoneConnectivityTest({},{})",deviceId,i);
		ACSCorba acsCorba = new ACSCorba(gw_type);
		return acsCorba.getValue(deviceId,phyInterface+i+".Tests.PhoneConnectivity");

	}
	
	/**
	 * 设置PhoneConnectivityTest诊断
	 * 
	 * @param deviceId
	 * @param i
	 * @return
	 */
	public int setX_CT_COM_SimulateTest(String deviceId,String i,String calledNumber,
			String testType,String dailDTMFConfirmEnable,String dailDTMFConfirmNumber , String gw_type){
		logger.debug("setX_CT_COM_SimulateTest({},{})",deviceId,i);
		
		ArrayList<ParameValueOBJ> objList = new ArrayList<ParameValueOBJ>();
		objList.add(new ParameValueOBJ(phyInterface+i+".Tests.TestSelector","X_CT-COM_SimulateTest", "1"));
		objList.add(new ParameValueOBJ(phyInterface+i+".Tests.X_CT-COM_SimulateTest.CalledNumber",calledNumber, "1"));
		objList.add(new ParameValueOBJ(phyInterface+i+".Tests.X_CT-COM_SimulateTest.TestType",testType, "1"));
		objList.add(new ParameValueOBJ(phyInterface+i+".Tests.X_CT-COM_SimulateTest.DailDTMFConfirmEnable",dailDTMFConfirmEnable, "4"));
		objList.add(new ParameValueOBJ(phyInterface+i+".Tests.X_CT-COM_SimulateTest.DailDTMFConfirmNumber",dailDTMFConfirmNumber, "1"));
		objList.add(new ParameValueOBJ(phyInterface+i+".Tests.TestState","Requested", "1"));
		ACSCorba acsCorba = new ACSCorba(gw_type);
		return acsCorba.setValue(deviceId,objList);
		
	}
	
	/**
	 * 获取PhoneConnectivityTest诊断结果
	 * 
	 * @param deviceId
	 * @param i
	 * @return
	 */
	public ArrayList<ParameValueOBJ> getX_CT_COM_SimulateTest(String deviceId,String i, String gw_type){
		logger.debug("getX_CT_COM_SimulateTest({},{})",deviceId,i);
		String[] path = new String[4];
		path[0] = phyInterface+i+".Tests.TestState";
		path[1] = phyInterface+i+".Tests.X_CT-COM_SimulateTest.Conclusion";
		path[2] = phyInterface+i+".Tests.X_CT-COM_SimulateTest.DailDTMFConfirmResult";
		path[3] = phyInterface+i+".Tests.X_CT-COM_SimulateTest.CallerFailReason";
		ACSCorba acsCorba = new ACSCorba(gw_type);
		return acsCorba.getValue(deviceId,path);
		
	}
	
}
