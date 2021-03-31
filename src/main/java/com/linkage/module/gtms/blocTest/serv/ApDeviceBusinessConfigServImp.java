package com.linkage.module.gtms.blocTest.serv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.TimeUtil;
import com.linkage.module.gtms.blocTest.dao.ApDeviceBusinessConfigDaoImp;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;


/**
 * 下发无线业务到AP终端
 * 
 * @author Administrator
 *
 */
public class ApDeviceBusinessConfigServImp implements ApDeviceBusinessConfigServ{
	
	private static Logger logger = LoggerFactory.getLogger(ApDeviceSoftUpGradeServImp.class);
	
	private ApDeviceBusinessConfigDaoImp dao ;
	
	
	public String doBusiness(String deviceId, String servTypeId, String gw_type,
			String ssid, String templatePara) {
		
		logger.debug("ApDeviceBusinessConfigServImp==>doBusiness({},{},{},{},{})",
				new Object[] { deviceId, servTypeId, gw_type, ssid, templatePara });
		
		StringBuffer xmlParam = new StringBuffer();
		xmlParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xmlParam.append("<APSSID>");
		xmlParam.append("<SSID>").append(ssid).append("</SSID>");  // SSID名称
		xmlParam.append("<BeaconType>").append("none").append("</BeaconType>");  // 机密方式
		xmlParam.append("<Rule>").append(templatePara).append("</Rule>");  // 规则
		// 0：覆盖模式，覆盖目前已有的配置  1：新增模式，在现有的配置上新增新的配置
		xmlParam.append("<ConfigurationMode>1</ConfigurationMode>");
		xmlParam.append("</APSSID>");
		
//		logger.warn("===="+xmlParam.toString()+"====");
		
		/** 入策略表，调预读 */
		// 立即执行
		int strategyType = 0;
		
		StrategyOBJ strategyObj = new StrategyOBJ();
		// 策略ID
		strategyObj.createId();
		// 策略配置时间
		strategyObj.setTime(TimeUtil.getCurrentTime());
		// 立即执行
		strategyObj.setType(strategyType);
		// 设备ID
		strategyObj.setDeviceId(deviceId);
		// QOS serviceId
		strategyObj.setServiceId(2210);
		// 顺序,默认1
		strategyObj.setOrderId(1);
		// 工单类型: 新工单,工单参数为xml串的工单
		strategyObj.setSheetType(2);
		// 参数
		strategyObj.setSheetPara(xmlParam.toString());
		strategyObj.setTempId(2210);
		strategyObj.setIsLastOne(1);

		String result = "";
		// 入策略表
		if (dao.addStrategy(strategyObj)){
			logger.warn("Ap应用终端无线业务下发,调配置模块，deviceId[{}]", deviceId);
			// 调用预读
			if (true == CreateObjectFactory.createPreProcess(gw_type).processOOBatch(String
					.valueOf(strategyObj.getId()))) {
				logger.warn("Ap应用终端无线业务下发,调配置模块成功！deviceId[{}]", deviceId);
				result = "业务下发成功！";
			}
			else {
				logger.warn("Ap应用终端无线业务下发,调配置模块失败！deviceId[{}]", deviceId);
				result = "业务下发失败！";
			}
		} else {
			logger.warn("策略入库失败！");
			result = "策略入库失败！";
		}
		
		return result;
		
	}


	
	public ApDeviceBusinessConfigDaoImp getDao() {
		return dao;
	}
	
	public void setDao(ApDeviceBusinessConfigDaoImp dao) {
		this.dao = dao;
	}
}
