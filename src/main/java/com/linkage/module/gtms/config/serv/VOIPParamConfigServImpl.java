package com.linkage.module.gtms.config.serv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.util.CreateObjectFactory;

public class VOIPParamConfigServImpl implements VOIPParamConfigServ {
	private static Logger logger = LoggerFactory
	.getLogger(VOIPParamConfigServImpl.class);

	public int addToStrategy(String[] deviceIds, String serviceId,
			String[] paramArr,String gw_type) {
		logger.debug("addToStrategy({},{},{})",new Object[]{deviceIds,serviceId,paramArr});
		//调用接口
		if (1==CreateObjectFactory.createPreProcess(gw_type).processDeviceStrategy(deviceIds,serviceId,paramArr)){
			logger.debug("调用后台预读模块成功");
			return 1;
		} else {
			logger.warn("调用后台预读模块失败");
			return -4;
		}
	}
	
}
