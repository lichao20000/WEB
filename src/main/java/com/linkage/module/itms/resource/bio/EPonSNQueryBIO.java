
package com.linkage.module.itms.resource.bio;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.itms.resource.dao.EPonSNQueryDAO;

public class EPonSNQueryBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(EPonSNQueryBIO.class);
	private EPonSNQueryDAO ePonSNQueryDAO;

	public List getHgwInfo(String cityId,String username,String voipUsername,String voipPhone, String gw_type) {
		logger.debug("EPonSNQueryBIO=>getHgwInfo({},{},{})",new Object[]{cityId,username,voipUsername,voipPhone});
		
		if(null!=username && !"".equals(username)){
			if((null==voipUsername || "".equals(voipPhone)) && (null==voipPhone || "".equals(voipPhone))){
				return ePonSNQueryDAO.getHgwInfoByUsername(cityId,username,gw_type);
			}else{
				return ePonSNQueryDAO.getHgwInfoByAll(cityId,username, voipUsername, voipPhone,gw_type);
			}
		}else{
			return ePonSNQueryDAO.getHgwInfoByVoipParam(cityId,voipUsername, voipPhone, gw_type);
		}
	}

	/**
	 * @return the ePonSNQueryDAO
	 */
	public EPonSNQueryDAO getEPonSNQueryDAO() {
		return ePonSNQueryDAO;
	}

	/**
	 * @param ponSNQueryDAO the ePonSNQueryDAO to set
	 */
	public void setDao(EPonSNQueryDAO dao) {
		this.ePonSNQueryDAO = dao;
	}

	
}
