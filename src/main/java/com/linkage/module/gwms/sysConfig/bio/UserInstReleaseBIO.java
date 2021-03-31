/**
 * 
 */
package com.linkage.module.gwms.sysConfig.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio.confTaskView.StrategyConfigBio;

import com.linkage.module.gwms.sysConfig.dao.UserInstReleaseDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-11-5
 * @category com.linkage.module.gwms.resource.bio
 * 
 */
public class UserInstReleaseBIO {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(UserInstReleaseBIO.class);

	UserInstReleaseDAO dao;
	StrategyConfigBio strategyConfigBio;
	
	/**
	 * @return the dao
	 */
	public UserInstReleaseDAO getDao() {
		return dao;
	}

	/**
	 * @param dao the dao to set
	 */
	public void setDao(UserInstReleaseDAO dao) {
		this.dao = dao;
	}

	/**
	 * 查询未绑定设备
	 * 
	 * @param deviceNo
	 *            必须
	 * @param cityId
	 *            必须
	 * @return
	 */
	public List queryDevice(String deviceNo, String cityId,String loopbackIp) {
		logger.debug("UserInstReleaseBIO=>queryDevice(deviceNo:{},cityId:{})",
				deviceNo, cityId);

		return dao.getDeviceInfo(cityId, deviceNo, loopbackIp);

	}
	
	public String getCorbaIor(){
		
		List _list = dao.getCorbaIor();
		
		if(null!=_list && _list.size()>0){
			Map _map = (Map)_list.get(0);
			return String.valueOf(_map.get("ior"));
		}else{
			return null;
		}
	}
	
}
