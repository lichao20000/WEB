package com.linkage.module.gwms.diagnostics.bio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.diagnostics.cao.HealthInfoCAO;
import com.linkage.module.gwms.diagnostics.dao.HealthInfoDAO;
import com.linkage.module.gwms.obj.gw.HealthLanWlanOBJ;
import com.linkage.module.gwms.obj.gw.HealthWanDslOBJ;
import com.linkage.module.gwms.obj.gw.VoipAddressOBJ;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;

public class HealthInfoBIO {

	private static Logger logger = LoggerFactory.getLogger(HealthInfoBIO.class);
	
	private HealthInfoDAO healthInfoDao;

	private HealthInfoCAO healthInfoCao;

	/**
	 * 调用corba
	 * 
	 * @param deviceId
	 * @param type
	 * @return
	 */
	public int getDataFromSG(String deviceId, int type) {
		
		logger.debug("getDataFromSG(deviceId:{}, type:{})", deviceId, type);
		return healthInfoCao.getDataFromSG(deviceId, type);
	}
	
	
	/**
	 * 获得线路信息
	 * @author gongsj
	 * @date 2009-8-13
	 * @param deviceId
	 * @return
	 */
	public HealthWanDslOBJ[] getDslHealthInfo(String deviceId) {
		logger.debug("getDslHealthInfo({})", deviceId);
		
		return healthInfoDao.getDslHealthInfo(deviceId);
	}
	
	/**
	 * 获得WLAN信息
	 * @author gongsj
	 * @date 2009-8-18
	 * @param deviceId
	 * @return
	 */
	public HealthLanWlanOBJ getWlanHealthInfo(String deviceId) {
		
		logger.debug("getWlanHealthInfo({})", deviceId);
		
		return healthInfoDao.getWlanHealthInfo(deviceId);
	}
	
	/**
	 * 获得业务用户信息
	 * @author gongsj
	 * @date 2009-8-13
	 * @param userId
	 * @param servTypeId
	 * @return
	 */
	public HgwServUserObj getUserInfo(String userId, String servTypeId,String gwType){
		
		logger.debug("getUserInfo({},{})", userId, servTypeId);
		
		return healthInfoDao.getUserInfo(userId, servTypeId,gwType);
	}
	
	/**
	 * 获得VOIP地址
	 * @author gongsj
	 * @date 2009-8-19
	 * @param deviceId
	 * @return
	 */
	public VoipAddressOBJ getVoipAddress(String deviceId) {
		logger.debug("getVoipAddress(device_id={})", deviceId);
		
		return healthInfoDao.getVoipAddress(deviceId);
	}
	/**
	 * 更新业务用户表
	 * @author gongsj
	 * @date 2009-8-14
	 * @param username
	 * @param password
	 * @param servType
	 * @param wanType
	 * @param vpi
	 * @param vci
	 * @param vlanid
	 * @param ip
	 * @param mask
	 * @param gateway
	 * @param dns
	 * @param bindport
	 * @return
	 */
	public boolean editBussInfo(String userId, String username, String password, String servType, String wanType, String vpi, String vci, String vlanid, String ip, String mask, String gateway, String dns, String bindport) {
		
		logger.debug("HealthInfoBIO editBussInfo()");
		
		return healthInfoDao.editBussInfo(userId, username, password, servType, wanType, vpi, vci, vlanid, ip, mask, gateway, dns, bindport);
		
		
	}
	
	/**
	 * 编辑VOIP地址信息
	 * @author gongsj
	 * @date 2009-8-19
	 * @param deviceId
	 * @param proxServer
	 * @param proxPort
	 * @param proxServer2
	 * @param proxPort2
	 * @param regiServ
	 * @param regiPort
	 * @param standRegiServ
	 * @param standRegiPort
	 * @param outBoundProxy
	 * @param outBoundPort
	 * @param standOutBoundProxy
	 * @param standOutBoundPort
	 * @return
	 */
	public boolean editVoipAddr(String deviceId, String proxServer, String proxPort, String proxServer2, String proxPort2, String regiServ, String regiPort, String standRegiServ, String standRegiPort, String outBoundProxy, String outBoundPort, String standOutBoundProxy, String standOutBoundPort) {
			
		logger.debug("HealthInfoBIO editVoipAddr()");
		
		return healthInfoDao.editVoipAddr(deviceId, proxServer, proxPort, proxServer2, proxPort2, regiServ, regiPort, standRegiServ, standRegiPort, outBoundProxy, outBoundPort, standOutBoundProxy, standOutBoundPort);
	}
	
	/**
	 * 编辑健康库信息
	 * @author gongsj
	 * @date 2009-8-19
	 * @param deviceId
	 * @param upAttenMin
	 * @param upAttenMax
	 * @param downAttenMin
	 * @param downAttenMax
	 * @param upMaxRateMin
	 * @param upMaxRateMax
	 * @param downMaxRateMin
	 * @param downMaxRateMax
	 * @param upNoiseMin
	 * @param upNoiseMax
	 * @param downNoiseMin
	 * @param downNoiseMax
	 * @param interDepthMin
	 * @param interDepthMax
	 * @param datePath
	 * @param powerlevel
	 * @param powervalue
	 * @return
	 */
	public boolean editHealthInfo(String deviceId, String upAttenMin, String upAttenMax, String downAttenMin, String downAttenMax, String upMaxRateMin, String upMaxRateMax, String downMaxRateMin, String downMaxRateMax, String upNoiseMin, String upNoiseMax, String downNoiseMin, String downNoiseMax, String interDepthMin, String interDepthMax, String datePath, String powerlevel, String powervalue) {
		
		logger.debug("HealthInfoBIO editHealthInfo()");
		
		return healthInfoDao.editHealthInfo(deviceId, upAttenMin, upAttenMax, downAttenMin, downAttenMax, upMaxRateMin, upMaxRateMax, downMaxRateMin, downMaxRateMax, upNoiseMin, upNoiseMax, downNoiseMin, downNoiseMax, interDepthMin, interDepthMax, datePath, powerlevel, powervalue);
	}
	
	/**
	 * insertHealthInfo
	 * @author gongsj
	 * @date 2009-8-17
	 * @param deviceId
	 */
	public void insertHealthInfo(String deviceId, boolean healthWire, boolean healthWlan) {
		
		logger.debug("HealthInfoBIO insertHealthInfo()");
		
		healthInfoDao.insertHealthInfo(deviceId, healthWire, healthWlan);
	}
	
	
	/**
	 * 查找指定deviceId的设备是否存在
	 * @author gongsj
	 * @date 2009-8-17
	 * @param deviceId
	 * @return true 已存在
	 *         false 不存在
	 */
	public boolean searchRecordExist(String deviceId, String tabName) {
		return healthInfoDao.searchRecordExist(deviceId, tabName);
		
	}
	
	/**
	 * 删除健康库中已采集的数据
	 * @author gongsj
	 * @date 2009-8-18
	 * @param deviceId
	 * @param tabName
	 * @return
	 */
	public boolean deleteRecordExist(String deviceId, String tabName) {
		
		return healthInfoDao.deleteRecordExist(deviceId, tabName);
	}
	/**
	 * 获得UserId
	 * @author gongsj
	 * @date 2009-8-17
	 * @param deviceId
	 * @return
	 */
	public String getUserId(String deviceId,String gwType) {
		logger.debug("getUserId");
		
		return healthInfoDao.getUserId(deviceId, gwType);
		
	}
	
	public void setHealthInfoDao(HealthInfoDAO healthInfoDao) {
		this.healthInfoDao = healthInfoDao;
	}

	public void setHealthInfoCao(HealthInfoCAO healthInfoCao) {
		this.healthInfoCao = healthInfoCao;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
