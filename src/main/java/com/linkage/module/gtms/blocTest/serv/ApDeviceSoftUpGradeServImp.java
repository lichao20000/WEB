package com.linkage.module.gtms.blocTest.serv;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.TimeUtil;
import com.linkage.module.gtms.blocTest.dao.ApDeviceSoftUpGradeDaoImp;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;


public class ApDeviceSoftUpGradeServImp implements ApDeviceSoftUpGradeServ {
	
	private static Logger logger = LoggerFactory.getLogger(ApDeviceSoftUpGradeServImp.class);
	
	private ApDeviceSoftUpGradeDaoImp dao;
	
	
	/**
	 * Ap应用终端软件升级
	 */
	public String doConfig(String deviceId, String templatePara, String gw_type, String filePath){
		
		logger.debug("ApDeviceSoftUpGradeServImp==>doConfig({},{})", new Object[] {
				deviceId, templatePara });
		
		String HardwareVersion = "";
		String SoftwareVersion = "";
		
		String [] ruleArr = templatePara.split(";");
		
		for (int i = 0; i < ruleArr.length; i++) {
			if (ruleArr[i].indexOf("HardwareVersion=")==0 || ruleArr[i].indexOf("HardwareVersion!=")==0) {
				HardwareVersion = ruleArr[i].substring(ruleArr[i].indexOf("=")+1);
			}
			if (ruleArr[i].indexOf("SoftwareVersion=")==0 || ruleArr[i].indexOf("SoftwareVersion!=")==0) {
				SoftwareVersion = ruleArr[i].substring(ruleArr[i].indexOf("=")+1);
			}
		}
		
		
		StringBuffer xmlParam = new StringBuffer();
		xmlParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xmlParam.append("<APSoft>");
		xmlParam.append("<Rule>").append(templatePara).append("</Rule>");  // 规则
		xmlParam.append("<FileType>1 Firmware Upgrade Image</FileType>");  // 文件类型
		xmlParam.append("<URL>").append(filePath).append("</URL>");
		xmlParam.append("<SoftwareVersion>").append(SoftwareVersion).append("</SoftwareVersion>");
		xmlParam.append("<HardwareVersion>").append(HardwareVersion).append("</HardwareVersion>");
		xmlParam.append("<Channel>").append("</Channel>");
		xmlParam.append("<Username>").append("</Username>");
		xmlParam.append("<Password>").append("</Password>");
		xmlParam.append("</APSoft>");
		
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
		strategyObj.setServiceId(2205);
		// 顺序,默认1
		strategyObj.setOrderId(1);
		// 工单类型: 新工单,工单参数为xml串的工单
		strategyObj.setSheetType(2);
		// 参数
		strategyObj.setSheetPara(xmlParam.toString());
		strategyObj.setTempId(2205);
		strategyObj.setIsLastOne(1);

		String result = "";
		// 入策略表
		if (dao.addStrategy(strategyObj)){
			logger.warn("Ap应用终端软件升级,调配置模块，deviceId[{}]", deviceId);
			// 调用预读
			if (true == CreateObjectFactory.createPreProcess(gw_type).processOOBatch(String
					.valueOf(strategyObj.getId()))) {
				logger.warn("Ap应用终端软件升级,调配置模块成功！deviceId[{}]", deviceId);
				result = "升级成功！";
			}
			else {
				logger.warn("Ap应用终端软件升级,调配置模块失败！deviceId[{}]", deviceId);
				result = "升级失败！";
			}
		} else {
			logger.warn("策略入库失败！");
			result = "策略入库失败！";
		}
		
		return result;
	}

	
	/**
	 * 获取目标版本文件下拉框
	 */
	public String getSelectListBox(){
		
		logger.debug("ApDeviceSoftUpGradeServImp==>getSelectListBox");
		
		List<Map> list = dao.getSelectListBox();
		
		StringBuffer sb = new StringBuffer("");
		sb.append("<select name='filePath' class='bk' >");
		
		if (null != list && false == list.isEmpty()) {
			sb.append("<option value='-1'>====请选择====</option>");
			for (Map map : list) {
				sb.append("<option value='").append(map.get("outter_url")).append("/")
						.append(map.get("server_dir")).append("/").append("'>");
				sb.append(map.get("software_version")).append("|").append(
						map.get("hardware_version")).append("|").append(
						map.get("softwarefile_name"));
				sb.append("</option>");
			}
		} else {
			sb.append("<option value='-1'>==此项没有记录==</option>");
		}
		sb.append("</select>");
		
		return sb.toString();
	}
	
	
	public ApDeviceSoftUpGradeDaoImp getDao() {
		return dao;
	}


	
	public void setDao(ApDeviceSoftUpGradeDaoImp dao) {
		this.dao = dao;
	}
}
