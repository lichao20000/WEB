package com.linkage.module.gtms.stb.resource.serv;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.stb.resource.dao.OpenAdvertQueryDAO;
import com.linkage.module.gtms.stb.utils.DeviceTypeUtil;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 开机广告查询
 * 
 * @author os_hanzz
 * 
 */
public class OpenAdvertQueryBIO {
	// 日志操作
	Logger logger = LoggerFactory.getLogger(OpenAdvertQueryDAO.class);

	private OpenAdvertQueryDAO dao;

	
	public List<Map> queryAdvertResultCount(String taskId, String taskName,
			String vendorId, String deviceModelId) {
		List<Map<String,String>> list = dao.queryAdvertResultCount(taskId, taskName, vendorId,
				deviceModelId);
		logger.warn("OpenAdvertQueryBIO->list.size={}", list.size());
		List<Map> returnList = new ArrayList<Map>();
		Map<String,Map<String,String>> finalMap=new HashMap<String,Map<String,String>>();
		if (null != list) {
			for(Map<String,String> map:list){
				String vendor_id = StringUtil.getStringValue(map,"vendor_id");
				String deviceModel_id = StringUtil.getStringValue(map,"device_model_id");
				String devicetype_id = StringUtil.getStringValue(map,"devicetype_id");	
				String key=vendor_id+deviceModel_id+devicetype_id;
				if(finalMap.containsKey(key)){
					Map<String,String> taskMap=finalMap.get(key);
					int result_id=StringUtil.getIntValue(map, "result_id");
					taskMap.put("total", StringUtil.getIntValue(taskMap, "total")+StringUtil.getIntValue(map, "total")+"");	
					if(1==result_id){
						taskMap.put("success", StringUtil.getIntValue(taskMap,"success")+StringUtil.getIntValue(map, "total")+"");
					}
					else if(0==result_id){
						taskMap.put("noTrigger", StringUtil.getIntValue(taskMap,"noTrigger")+StringUtil.getIntValue(map, "total")+"");
					}
					else{
						taskMap.put("fails", StringUtil.getIntValue(taskMap,"fails")+StringUtil.getIntValue(map, "total")+"");
					}
				}
				else{
					Map<String,String> taskMap=new HashMap<String,String>();
					taskMap.put("task_id", StringUtil.getStringValue(map, "task_id"));
					taskMap.put("vendor_id", vendor_id);
					taskMap.put("devicetype_id", devicetype_id);
					taskMap.put("device_model_id", deviceModel_id);
					taskMap.put("vendor_name",
							DeviceTypeUtil.getVendorName(vendor_id));
					taskMap.put("device_model_name",
							DeviceTypeUtil.getDeviceModel(deviceModel_id));
					taskMap.put("devicetype_name", DeviceTypeUtil
							.getDeviceSoftVersion(devicetype_id));
					taskMap.put("noTrigger", "0");
					taskMap.put("fails", "0");
					taskMap.put("success", "0");
					taskMap.put("total", StringUtil.getStringValue(map, "total"));	
					int result_id=StringUtil.getIntValue(map, "result_id");
					if(1==result_id){
						taskMap.put("success", StringUtil.getStringValue(map, "total"));
					}
					else if(0==result_id){
						taskMap.put("noTrigger", StringUtil.getStringValue(map, "total"));
					}
					else{
						taskMap.put("fails", StringUtil.getStringValue(map, "total"));
					}
					finalMap.put(key, taskMap);
				}
			}
			returnList= new ArrayList<Map>(finalMap.values());
			for(Map<String,String> taskMap:returnList){
				String pert = getDecimal(StringUtil.getStringValue(taskMap,"success"),StringUtil.getIntValue(taskMap,"fails")+StringUtil.getIntValue(taskMap,"success")+"");
				taskMap.put("pert", pert);
			}
		}
		return returnList;
	}
	

	/**
	 * 获取详细信息
	 * 
	 * @param taskId
	 * @param taskName
	 * @param vendorId
	 * @param deviceModelId
	 * @param queryType
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> queryTotalList(String taskId, String taskName,
			String vendorId, String deviceModelId, String queryType,
			int curPage_splitPage, int num_splitPage) {
		return dao.queryTotalList(taskId, taskName, vendorId, deviceModelId,
				queryType, curPage_splitPage, num_splitPage);
	}

	/**
	 * 获取详细信息(分页)
	 * 
	 * @param taskId
	 * @param taskName
	 * @param vendorId
	 * @param deviceModelId
	 * @param queryType
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int queryTotalListCount(String taskId, String taskName,
			String vendorId, String deviceModelId, String queryType,
			int curPage_splitPage, int num_splitPage) {
		return dao.queryTotalListCount(taskId, taskName, vendorId,
				deviceModelId, queryType, curPage_splitPage, num_splitPage);
	}

	/**
	 * 查询设备厂商
	 * 
	 * @return
	 */
	public String getVendor() {
		logger.debug("GwDeviceQueryBIO=>getVendor()");
		List list = dao.getVendor();
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			if (i > 0) {
				bf.append("#");
			}
			bf.append(map.get("vendor_id"));
			bf.append("$");
			bf.append(map.get("vendor_add"));
			bf.append("(");
			bf.append(map.get("vendor_name"));
			bf.append(")");
		}
		return bf.toString();
	}

	/**
	 * 查询设备型号
	 * 
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModel(String vendorId) {
		logger.debug("GwDeviceQueryBIO=>getDeviceModel(vendorId:{})", vendorId);
		List list = dao.getDeviceModel(vendorId);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			if (i > 0) {
				bf.append("#");
			}
			bf.append(map.get("device_model_id"));
			bf.append("$");
			bf.append(map.get("device_model"));
		}
		return bf.toString();
	}

	/**
	 * 占用比列
	 * 
	 * @param molecular
	 *            分子
	 * @param denominator
	 *            分母
	 * @return
	 */
	public String getDecimal(String molecular, String denominator) {
		if (null == molecular || "0".equals(molecular) || null == denominator
				|| "0".equals(denominator)) {
			return "0%";
		}
		float t1 = Float.parseFloat(molecular);
		float t2 = Float.parseFloat(denominator);
		float f = t1 / t2;
		DecimalFormat df = new DecimalFormat();
		String style = "0.00%";
		df.applyPattern(style);
		return df.format(f);
	}

	public OpenAdvertQueryDAO getDao() {
		return dao;
	}

	public void setDao(OpenAdvertQueryDAO dao) {
		this.dao = dao;
	}

}
