/**
 * 
 */
package com.linkage.module.itms.config.bio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.itms.config.dao.DigitMapConfigDAO;

/**
 * @author chenjie(67371)
 * @date   2011.4.7
 */
public class DigitMapConfigBIO {
		
	//	 日志记录
	private static Logger logger = LoggerFactory.getLogger(DigitMapConfigBIO.class);
	
	// dao
	private DigitMapConfigDAO dao;

	/**
	 * 查询所有的数图模板 
	 * 
	 * modify by zhangchy 2011-09-19 新增了两个参数：queryParam、queryField
	 * 
	 * @param current_user 当前用户
	 */
	public List<Map> queryAllDigitMap(UserRes current_user, String map_name, String cityId, String vendorId, String deviceModelId, String deviceTypeId) {
		
		logger.debug("queryAllDigitMap({},{},{},{},{},{})",new Object[]{current_user, map_name, cityId, vendorId, deviceModelId, deviceTypeId});
		
		return dao.queryAllDigitMap(current_user, map_name, cityId, vendorId, deviceModelId, deviceTypeId);
	}

	public DigitMapConfigDAO getDao() {
		return dao;
	}

	public void setDao(DigitMapConfigDAO dao) {
		this.dao = dao;
	}

	/**
	 * 添加数据
	 * @param map_name  模板名称
	 * @param map_content 模板内容
	 * @param user 操作人
	 */
	public void addDigitMap(String map_name, String map_content, UserRes user,String cityId, String vendorId, String deviceModelId, String deviceTypeId,String is_default) {
		
		logger.debug("addDigitMap({},{},{},{},{},{},{})", new Object[]{map_name, map_content, user,cityId, vendorId, deviceModelId, deviceTypeId,is_default});
		dao.addDigitMap(map_name, map_content, user,cityId, vendorId, deviceModelId, deviceTypeId,is_default);
	}

	/**
	 * 根据id查找数图模板
	 * @param map_id
	 * @return
	 */
	public Map<String,String> getDigitMapById(String map_id) {
		logger.debug("getDigitMapById({})", new Object[]{map_id});
		return dao.getDigitMapById(map_id);
	}

	/**
	 * 修改数图模板
	 * @param map_id
	 * @param map_name
	 * @param map_content
	 */
	public void update(String map_id, String map_name, String map_content,String cityId, String vendorId, String deviceModelId, String deviceTypeId,String is_default) {
		logger.debug("update({},{},{},{},{},{},{})", new Object[]{map_id, map_name, map_content,cityId, vendorId, deviceModelId, deviceTypeId});
		dao.update(map_id, map_name, map_content,cityId, vendorId,  deviceModelId,  deviceTypeId,is_default);
	}

	/**
	 * 根据id删除数模板
	 * @param map_id
	 */
	public void deleteDigitMapById(String map_id) {
		logger.debug("deleteDigitMapById({})", new Object[]{map_id});
		dao.deleteDigitMapById(map_id);
	}

	/**
	 * 下发策略
	 * @param user_id
	 * @param device_id
	 * @param map_content
	 * @return
	 */
	public String doConfig(long user_id, String device_id, String map_id,String gwType) {
		
		logger.debug("doConfig({},{},{})", new Object[]{user_id, device_id, map_id});
		String map_content = dao.getDigitMapContentById(map_id);
		String strategyXmlParam = toXML(map_content);
		logger.debug("digitMap XML: " + strategyXmlParam);
		
		/** 入策略表，调预读 */
		// 立即执行
		int strategyType = 0;
		// 配置的service_id
		int serviceId = 7;
		StrategyOBJ strategyObj = new StrategyOBJ();
		// 策略ID
		strategyObj.createId();
		// 策略配置时间
		strategyObj.setTime(TimeUtil.getCurrentTime());
		// 用户id
		strategyObj.setAccOid(user_id);
		// 立即执行
		strategyObj.setType(strategyType);
		// 设备ID
		strategyObj.setDeviceId(device_id);
		// QOS serviceId
		strategyObj.setServiceId(serviceId);
		// 顺序,默认1
		strategyObj.setOrderId(1);
		// 工单类型: 新工单,工单参数为xml串的工单
		strategyObj.setSheetType(2);
		// 参数
		strategyObj.setSheetPara(strategyXmlParam);
		strategyObj.setTempId(serviceId);
		strategyObj.setIsLastOne(1);

		// 先入记录表
		dao.addDigitMapRecord(device_id, map_id);
		
		String result = "";
		// 入策略表
		if (dao.addStrategy(strategyObj))
		{
			// 调用预读
			if (true == CreateObjectFactory.createPreProcess(gwType).processOOBatch(String.valueOf(strategyObj
					.getId())))
			{
				logger.debug("成功");
				result = 1 + ";调用后台成功;" + strategyObj.getId();
			}
			else
			{
				logger.warn("调用预读失败");
				result = -1 + ";调用后台失败";
			}
		}
		else
		{
			logger.warn("策略入库失败");
			result = -1 + ";策略入库失败";
		}
		
		return result;
	}
	
	
	/**
	 * 设备批量下发数图配置
	 * 
	 * @param user_id
	 * @param deviceIds
	 * @param map_id
	 * @return
	 */
	public String doConfigAll(long user_id, String deviceIds, String map_id, String taskName) {
		
		logger.debug("doConfigAll()", new Object[]{user_id, deviceIds, map_id});
		
		StringBuffer result = new StringBuffer();
		ArrayList<String> sqllist = new ArrayList<String>();
		
		// 入 数图任务表(gw_voip_digit_task)
		// 入数图配置下发设备记录表(table gw_voip_digit_device)
		List<String> sqlList_1 = dao.addDigitTaskAndDigitDevice(deviceIds, map_id, taskName);  
		sqllist.addAll(sqlList_1);
		
		// 入策略表
		String map_content = dao.getDigitMapContentById(map_id);
		String strategyXmlParam = toXML(map_content);
		logger.debug("digitMap XML: " + strategyXmlParam);
		
		String [] deviceIdArray = deviceIds.split("\\|");
		
		for(int i = 0; i < deviceIdArray.length; i++){
			
			/** 入策略表，调预读 */
			// 立即执行
			int strategyType = 5;  // strategyType=0表示立即执行，此处批量下发不不需要立即执行，所以strategyType赋值为5
			// 配置的service_id
			int serviceId = 7;
			
			StrategyOBJ strategyObj = new StrategyOBJ();
			// 策略ID
			strategyObj.createId();
			// 策略配置时间
			strategyObj.setTime(TimeUtil.getCurrentTime());
			// 用户id
			strategyObj.setAccOid(user_id);
			// 立即执行
			strategyObj.setType(strategyType);
			// 设备ID
			strategyObj.setDeviceId(deviceIdArray[i]);
			// QOS serviceId
			strategyObj.setServiceId(serviceId);
			// 顺序,默认1
			strategyObj.setOrderId(1);
			// 工单类型: 新工单,工单参数为xml串的工单
			strategyObj.setSheetType(2);
			// 参数
			strategyObj.setSheetPara(strategyXmlParam);
			strategyObj.setTempId(serviceId);
			strategyObj.setIsLastOne(1);
			
			List<String> sqlList_2 = dao.strategySQL(strategyObj);
			
			sqllist.addAll(sqlList_2);
		}
		
		// 启用一个新的线程来做入库
		if(LipossGlobals.ALL_SQL_IPTV.addAll(sqllist)){
			result.append("1;配置下发成功！");
		}else{
			result.append("-1;配置下发失败！");
		}
		return result.toString();
	}
	
	
	
	
	private String toXML(String map_content)
	{
		String strXml = null;
		Document doc = DocumentHelper.createDocument();
		// root node: X_CT-COM_UplinkQoS
		Element root = doc.addElement("VOIPDigitMap");
		Element digitMap = root.addElement("DigitMap");
		digitMap.addText(map_content);
		Element enable = root.addElement("Enable");
		enable.addText("");
		strXml = doc.asXML();
		return strXml;
	}

	/**
	 * 根据选择的设备展示适合的数图模板来供配置
	 * @param device_id
	 * @return
	 */
	public List<Map> queryForConfigMap(String device_id, String city_id) {
		logger.debug("queryForConfigMap({}))", device_id, city_id);
		return dao.queryForConfigMap(device_id, city_id);
	}
	
	
	public List<Map> queryForConfigAllMap(String cityId, String vendorId, String deviceModelId, String deviceTypeId) {
		logger.debug("queryForConfigAllMap({},{},{},{})", new Object[]{cityId, vendorId, deviceModelId, deviceTypeId});
		return dao.queryForConfigAllMap(cityId, vendorId, deviceModelId, deviceTypeId);
	}
	
	
	
	/**
	 * 查询设备（带列表）
	 * 
	 * @param areaId				登录人的areaId
	 * @param cityId				高级查询属地过滤
	 * @param onlineStatus			高级查询是否在线过滤
	 * @param vendorId				高级查询厂商过滤
	 * @param deviceModelId			高级查询设备型号过滤
	 * @param devicetypeId			高级查询软件版本过滤
	 * @param bindType				高级查询是否绑定过滤
	 * @param deviceSerialnumber	高级查询设备序列号模糊匹配
	 * @return
	 */
	public String getDeviceList(long areaId,String cityId,String onlineStatus,String vendorId,
			String deviceModelId,String devicetypeId,String bindType,
			String deviceSerialnumber){
		logger.debug("getDeviceListCount({},{},{},{},{},{},{},{})",new Object[]{areaId,cityId,onlineStatus,vendorId,
				deviceModelId,devicetypeId, bindType,deviceSerialnumber});

		List list = null;
		
		if(null!=onlineStatus && !"".equals(onlineStatus) && !"-1".equals(onlineStatus)){
			list = dao.queryDeviceByLikeStatus(areaId, cityId, vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber, onlineStatus);
		}else{
			list = dao.queryDevice(areaId, cityId, vendorId, deviceModelId, devicetypeId, bindType, deviceSerialnumber);
		}
		
		StringBuffer bf = new StringBuffer();
		
		if(!"".equals(list) || null != list){
			for(int i = 0; i < list.size(); i++){
				Map map = (Map) list.get(i);
				if(i > 0){
					bf.append("|");
				}
				bf.append(map.get("device_id"));
			}
		}else{
			bf.append("");
		}
		
		return bf.toString();
	}
	
	public int queryIsDefault(String vendorId,String deviceModelId,String is_default)
	{
		return dao.queryIsDefault( vendorId,deviceModelId,is_default);
	}
}