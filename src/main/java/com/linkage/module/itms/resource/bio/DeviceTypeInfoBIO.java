/**
 * 
 */

package com.linkage.module.itms.resource.bio;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.module.gwms.util.corba.ACSCorba;
import com.linkage.module.itms.resource.dao.DeviceTypeInfoDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author
 */
public class DeviceTypeInfoBIO {

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(DeviceTypeInfoBIO.class);
	private DeviceTypeInfoDAO dao;
	private int maxPage_splitPage;

	public int getMaxPage_splitPage() {
		return maxPage_splitPage;
	}

	public void setMaxPage_splitPage(int maxPage_splitPage) {
		this.maxPage_splitPage = maxPage_splitPage;
	}

	/**
	 * @param vendor
	 *            厂商
	 * @param device_model
	 *            设备型号
	 * @param hard_version
	 *            硬件型号
	 * @param soft_version
	 *            软件型号
	 * @param is_check
	 *            是否审核
	 * @param rela_dev_type
	 *            设备类型
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return 查询的所有数据
	 */
	public List<Map> queryDeviceList(int vendor, int device_model,
			String hard_version, String soft_version, int is_check,
			int rela_dev_type, int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, int access_style_relay_id,
			int spec_id, String machineConfig,String ipvsix,
			String startOpenDate, String endOpenDate, String mbBroadband,
			String device_version_type,String wifi,String wifi_frequency,
		 	String download_max_wifi,String gigabit_port,String gigabit_port_type,
		 	String download_max_lan,String power,String terminal_access_time,String deviceVersionType,int isSupSpeedTest_Query) {
		logger.debug("queryDeviceList({},{},{},{},{},{})", new Object[] {
				vendor, device_model, hard_version, soft_version, is_check,
				rela_dev_type });
		List<Map> list = dao.queryDeviceList(vendor, device_model,
				hard_version, soft_version, is_check, rela_dev_type,
				curPage_splitPage, num_splitPage, startTime, endTime,
				access_style_relay_id, spec_id, machineConfig,ipvsix,
				startOpenDate, endOpenDate, mbBroadband,device_version_type,wifi,wifi_frequency,download_max_wifi,gigabit_port,gigabit_port_type,download_max_lan,power,terminal_access_time,deviceVersionType,isSupSpeedTest_Query);
		// String specName = "";
		// for (Map map : list)
		// {
		// specName = dao.getSpecName((String) map.get("spec_id"));
		// map.put("specName", specName);
		// }
	  maxPage_splitPage = dao.getDeviceListCount(vendor, device_model,
				hard_version, soft_version, is_check, rela_dev_type,
				curPage_splitPage, num_splitPage, startTime, endTime,
				access_style_relay_id, spec_id, machineConfig,ipvsix,
				startOpenDate, endOpenDate, mbBroadband,device_version_type,wifi,wifi_frequency,download_max_wifi,gigabit_port,gigabit_port_type,download_max_lan,power,terminal_access_time,isSupSpeedTest_Query,deviceVersionType);
		return list;
	}
	/*public int getMaxPage_splitPage(int vendor, int device_model,
			String hard_version, String soft_version, int is_check,
			int rela_dev_type, int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, int access_style_relay_id,
			int spec_id, String machineConfig,String ipvsix,
			String startOpenDate, String endOpenDate, String mbBroadband)
	{
		logger.warn("AAAAAAAAAAAAAAAAAAA");
		return dao.getDeviceListCount(vendor, device_model,
				hard_version, soft_version, is_check, rela_dev_type,
				curPage_splitPage, num_splitPage, startTime, endTime,
				access_style_relay_id, spec_id, machineConfig,ipvsix,
				startOpenDate, endOpenDate, mbBroadband);
	}
	*/
	public List<Map> queryDeviceDetail(long deviceTypeId, long spec_id) {
		return dao.queryDeviceDetail(deviceTypeId, spec_id);
	}

	public List<Map<String, String>> querySpecList() {
		return dao.querySpecList();
	}

	/**
	 * @param vendor
	 * @return 查询端口和协议信息编辑用
	 */
	public String getPortAndType(long deviceTypeId) {
		return dao.getPortAndType(deviceTypeId);
	}

	/**
	 * 根据id删除记录
	 * 
	 * @param id
	 */
	public void deleteDevice(String gw_type, int id) {
		logger.debug("deleteDevice({})", new Object[] { id });
		dao.deleteDevice(id);
		// 通知ACS
		sendToACS(gw_type, 0, 2, id);
	}

	/**
	 * 查看当前设备版本是否可以删除，如果版本下面没有设备即可删除
	 * 
	 * @param id
	 */
	public boolean canDeleteDevice(int id) {
		logger.debug("canDeleteDevice({})", new Object[] { id });
		int num = dao.getDeviceListCount(id);
		// 有相关的设备，不可删除
		boolean result = num > 0 ? false : true;
		logger.debug("canDeleteDevice: " + result);
		return result;
	}
	
	/**
	 * 通过devicetype_id， 查询该型号支持的协议
	 * @param deviceTypeId
	 * @return
	 */
	public List<Map> queryDevicetypeInfoServertypeByDeviceTypeId(Long deviceTypeId){
		return dao.queryDevicetypeInfoServertypeByDeviceTypeId(deviceTypeId);
	}

	/**
	 * 增加设备版本
	 * 
	 * @param vendor
	 *            厂商
	 * @param device_model
	 *            设备型号
	 * @param specversion
	 *            特定版本
	 * @param hard_version
	 *            硬件型号
	 * @param soft_version
	 *            软件型号
	 * @param is_check
	 *            是否审核
	 * @param rela_dev_type
	 *            设备类型
	 * @param is_speedtest 
	 * @param gbBroadband 
	 * @param is_newVersion 
	 * @param remark 
	 * @param res_type 
	 * @param res_vendor 
	 * @param res_type_id 
	 * @param terminal_access_method 
	 * @param fusion_ability 
	 * @param is_wifi_double 
	 * @param devMaxSpeed 
	 * @param voipNum 
	 * @param mbitNum 
	 * @param gigabitNum 
	 * @return 操作结果
	 */
	public String addDevTypeInfo(String gw_type, int vendor, int device_model,
			String specversion, String hard_version, String soft_version,
			int is_check, int rela_dev_type, String typeId, String portInfo,
			String servertype, long acc_oid, String ipType, long spec_id,
			String mbBroadband, String startOpenDate, String machineConfig, 
			String is_awifi, String reason, int is_multicast, int is_speedtest,String is_qoe,String is_esurfing,
			String device_version_type,String wifi,String wifi_frequency,
			String download_max_wifi,String gigabit_port,String gigabit_port_type,
			String download_max_lan,String power,String terminal_access_time,
			String gbBroadband, int wifi_ability,String version_feature,int isSecurityPlugin,int securityPlugin, 
			String is_newVersion,int ssid_instancenum,String hvoip_port,String hvoip_type,String svoip_type, 
			int gigabitNum, int mbitNum, int voipNum, String devMaxSpeed, String is_wifi_double, String fusion_ability, 
			String terminal_access_method, int iscloudnet) 
	{
		String msg = "";
		Object[] temp = dao.addDevTypeInfo(vendor, device_model, specversion,
				hard_version, soft_version, is_check, rela_dev_type, typeId,
				ipType, spec_id, mbBroadband, startOpenDate, machineConfig, is_awifi, reason, is_multicast, is_qoe,is_esurfing,device_version_type,
				wifi,wifi_frequency,wifi_ability,download_max_wifi,gigabit_port,gigabit_port_type,download_max_lan,power,
				terminal_access_time,gbBroadband,version_feature, isSecurityPlugin, securityPlugin,is_newVersion,ssid_instancenum,hvoip_port, hvoip_type, svoip_type,
				gigabitNum,mbitNum,voipNum,devMaxSpeed,is_wifi_double,fusion_ability,terminal_access_method,iscloudnet
				);
		int a = dao.addPortAddServertype(portInfo, acc_oid, servertype);
		long id = Long.valueOf(temp[1].toString());
		int result = Integer.valueOf(temp[0].toString());
		// 通知ACS
		sendToACS(gw_type, 1, 2, id);
		if (result != 0) {
			String instArea = Global.instAreaShortName;
			if (Global.JSDX.equals(instArea) || Global.SDDX.equals(instArea) || Global.XJDX.equals(instArea)) {
				int res = dao.addOrUpdateDevVerAtt(id, is_speedtest);
				if(res == 0){
					return "设备版本入库成功,但是添加仿真测速参数失败";
				}
			}
			if (Global.JXDX.equals(instArea) || Global.NMGDX.equals(instArea)) {
				int res = dao.addOrUpdateDevVerAtt(id, is_speedtest, wifi_ability);
				if(res == 0){
					return "设备版本入库成功,但是添加仿真测速参数或wifi能力参数失败";
				}
			}
			msg = "设备版本入库成功";
		} else {
			msg = "设备版本入库失败";
		}
		return msg;
	}

	/**
	 * 修改设备版本
	 * 
	 * @param deviceTypeId
	 *            ID
	 * @param vendor
	 *            厂商
	 * @param device_model
	 *            设备型号
	 * @param specversion
	 *            特定版本
	 * @param hard_version
	 *            硬件型号
	 * @param soft_version
	 *            软件型号
	 * @param is_check
	 *            是否审核
	 * @param rela_dev_type
	 *            设备类型
	 * @param reason
	 *            定版原因
	 * @param gbBroadband 
	 * @param is_newVersion 
	 * @param remark 
	 * @param res_type 
	 * @param res_vendor 
	 * @param res_type_id 
	 * @param terminal_access_method 
	 * @param fusion_ability 
	 * @param is_wifi_double 
	 * @param devMaxSpeed 
	 * @param voipNum 
	 * @param mbitNum 
	 * @param gigabitNum 
	 * @return 操作结果
	 */
	public String updateDevTypeInfo(String gw_type, long deviceTypeId,int vendor, int device_model, String specversion,
			String hard_version, String soft_version, int is_check,int rela_dev_type, String typeId, String portInfo,
			String servertype, long acc_oid, String ipType, String isNormal,long spec_id, String mbBroadband, 
			String startOpenDate,String machineConfig, String is_awifi,String reason,String editDeviceType,
			int is_multicast,int is_speedtest,String is_qoe,String is_esurfing,
			String device_version_type,String wifi,String wifi_frequency,String download_max_wifi,
			String gigabit_port,String gigabit_port_type,String download_max_lan,
			String power,String terminal_access_time, String gbBroadband, 
			int wifi_ability,String version_feature,int isSecurityPlugin,int securityPlugin, String is_newVersion,int ssid_instancenum,String hvoip_port,String hvoip_type,String svoip_type,
			int gigabitNum, int mbitNum, int voipNum, String devMaxSpeed, String is_wifi_double, String fusion_ability, String terminal_access_method, int iscloudnet) 
	{
		int res = dao.updateDevTypeInfo(deviceTypeId, vendor, device_model,
				specversion, hard_version, soft_version, is_check,
				rela_dev_type, typeId, ipType, isNormal, spec_id, mbBroadband,
				startOpenDate, machineConfig,is_awifi, reason,editDeviceType,is_multicast,is_qoe,is_esurfing,
				device_version_type,wifi,wifi_frequency,wifi_ability,download_max_wifi,gigabit_port,gigabit_port_type,download_max_lan,power,
				terminal_access_time,gbBroadband,version_feature,isSecurityPlugin, securityPlugin,is_newVersion,ssid_instancenum,hvoip_port, hvoip_type, svoip_type,gigabitNum,mbitNum,voipNum,devMaxSpeed,is_wifi_double,
				fusion_ability,terminal_access_method, iscloudnet);
		int result = dao.updatePortAddServertype(deviceTypeId, portInfo,acc_oid, servertype);
		if("1".equals(editDeviceType)){
			// 通知ACS
			sendToACS(gw_type, 1, 2, deviceTypeId);
		}
		String msg = null;
		if (res != 0) {
			String instArea = Global.instAreaShortName;
			if (Global.JSDX.equals(instArea) || Global.SDDX.equals(instArea) || Global.XJDX.equals(instArea)) {
				int res1 = dao.addOrUpdateDevVerAtt(deviceTypeId, is_speedtest);
				if(res1 == 0){
					return "设备版本信息更新成功,但是更新仿真测速参数失败";
				}
			}
			if (Global.JXDX.equals(instArea) || Global.NMGDX.equals(instArea)) {
				int res1 = dao.addOrUpdateDevVerAtt(deviceTypeId, is_speedtest, wifi_ability);
				if(res1 == 0){
					return "设备版本入库成功,但是添加仿真测速参数或wifi能力参数失败";
				}
			}
			msg = "设备版本信息更新成功";
		} else {
			msg = "设备版本信息更新失败";
		}
		return msg;
	}
	
	/**
	 * 保存设备类型
	 * @return
	 */
	public String updateDeviceType(long deviceTypeId,int rela_dev_type_id,String gw_type){
		int res=dao.updateDeviceType(deviceTypeId,rela_dev_type_id);
		// 通知ACS
		sendToACS(gw_type, 1, 2, deviceTypeId);
		
		String msg=null;
		if (res != 0) {
			msg = "设备类型更新成功";
		} else {
			msg = "设备类型更新失败";
		}
		return msg;
	}

	public int updateDevVersionType(long deviceTypeId, int devVersionType){
		return dao.updateDevVersionType(deviceTypeId,devVersionType);
	}

	public void updateIsCheck(long id) {
		logger.debug("updateIsCheck({})", new Object[] { id });
		dao.updateIsCheck(id);
	}

	public void sendToACS(String gw_type, int chgType, int infoType,
			long deviceTypeId) {
		new ACSCorba(gw_type).chgInfo(chgType, infoType,
				String.valueOf(deviceTypeId));
	}

	public String getTypeNameList(String typeId) {
		return dao.getTypeNameList(typeId);
	}

	/**
	 * 根据Cursor,创建下拉框 add by zhangchy 2011-10-25 改写FormUtil.java中的
	 * 
	 * @param cursor
	 *            数据源
	 * @param name
	 *            用于生成下拉框时的value值
	 * @param cast
	 *            用于生成下拉框时的展示值(呈现给用户看的)
	 * @param pos
	 *            初始化下拉框的初始值
	 * @param hasDefault
	 * @return 生成下拉框的html代码后返回.
	 */
	public static String createListBox(Cursor cursor, String name, String cast,
			String pos, boolean hasDefault) {
		String htmlStr;
		htmlStr = "<SELECT NAME=" + name + " CLASS=bk style=\"width: 225px;margin-bottom: 3px;\">";
		Map fields = cursor.getNext();
		if (fields == null) {
			if (hasDefault)
				htmlStr += "<OPTION VALUE=-1>==此项没有记录==</OPTION>";
		} else {
			String tmp;
			htmlStr += "<OPTION VALUE=-1>==请选择==</OPTION>";
			while (fields != null) {
				tmp = (String) fields.get(name);
				if (tmp != null)
					tmp = tmp.trim();
				htmlStr += "<OPTION VALUE='" + tmp;
				if (pos.trim().equals(tmp))
					htmlStr += "' selected>--";
				else
					htmlStr += "'>--";
				htmlStr += (String) fields.get(cast) + "--</OPTION>";
				fields = cursor.getNext();
			}
		}
		htmlStr += "</SELECT>";
		return htmlStr;
	}

	public List<Map<String, String>> getGwDevType() {

		return dao.getGwDevType();
	}

	/** getters and setters **/
	public DeviceTypeInfoDAO getDao() {
		return dao;
	}

	public void setDao(DeviceTypeInfoDAO dao) {
		this.dao = dao;
	}

	public int isNormalVersion(int deviceModel) {
		return dao.isNormalVersion(deviceModel);
	}

	public List<Map> queryByVendorIdAndDeviceModelId(int vendor, int device_model)
	{
		return dao.queryByVendorIdAndDeviceModelId(vendor,  device_model);
	}

	public int insertGwSoftUpgradeTempMap(Integer relationType, Long oldVersionDeviceTypeId,
			Long deviceTypeId)
	{
		return dao.insertGwSoftUpgradeTempMap(relationType, oldVersionDeviceTypeId, deviceTypeId);
	}

	public Map queryByDeviceTypeId(long deviceTypeId)
	{
		return dao.queryByDeviceTypeId(deviceTypeId);
	}

	public void deleteGwSoftUpgradeTempMapByTempIdAndOldDeviceTypeId(
			Integer relationType, long id)
	{
		 dao.deleteGwSoftUpgradeTempMapByTempIdAndOldDeviceTypeId(relationType, id);
	}

	/**
	 * 安徽联通 根据查询条件获取设备版本列表 并转换为树形结构的数据结构
	 * @param vendor
	 * @param device_model
	 * @param hard_version
	 * @param soft_version
	 * @param is_check
	 * @param rela_dev_type
	 * @param startTime
	 * @param endTime
	 * @param access_style_relay_id
	 * @param spec_id
	 * @param machineConfig
	 * @param ipvsix
	 * @param startOpenDate
	 * @param endOpenDate
	 * @param mbBroadband
	 * @param deviceVersionType
	 * @return
	 */
	public JSONArray devVersionTree(int vendor, int device_model, String hard_version,
									String soft_version, int is_check, String startTime, String endTime,
									int access_style_relay_id, int spec_id, String machineConfig, String ipvsix,
									String startOpenDate, String endOpenDate, String mbBroadband, String deviceVersionType){
		//根据查询条件获取数据 转为Map结构 key：vendorId
		Map<String,JSONObject> vendorListMap = dao.queryDevVersionListAhlt(vendor, device_model, hard_version,
				soft_version, is_check, startTime, endTime, access_style_relay_id,
				spec_id, machineConfig,ipvsix, startOpenDate, endOpenDate,
				mbBroadband,deviceVersionType,-1,-1);
		if(vendorListMap == null || vendorListMap.size() == 0){
			logger.warn("devVersionTree with get vendorListMap null");
			return new JSONArray();
		}
		//转为树状数据结构
		JSONArray result = new JSONArray();
		for (Map.Entry<String,JSONObject> entry : vendorListMap.entrySet()) {
			JSONObject vendorData = new JSONObject();
			vendorData.put("id",entry.getKey());
			vendorData.put("name",entry.getValue().getString("vendorName"));
			vendorData.put("children",entry.getValue().getJSONArray("children"));
			result.add(vendorData);
		}
		return result;

	}

	/**
	 * 安徽联通点击二级 设备型号节点获取对应 设备版本列表
	 * @param vendor
	 * @param device_model
	 * @param hard_version
	 * @param soft_version
	 * @param is_check
	 * @param startTime
	 * @param endTime
	 * @param access_style_relay_id
	 * @param spec_id
	 * @param machineConfig
	 * @param ipvsix
	 * @param startOpenDate
	 * @param endOpenDate
	 * @param mbBroadband
	 * @param deviceVersionType
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String,String>> getDevVersionList(int vendor, int device_model, String hard_version,
											   String soft_version, int is_check, String startTime, String endTime,
											   int access_style_relay_id, int spec_id, String machineConfig, String ipvsix,
											   String startOpenDate, String endOpenDate, String mbBroadband, String deviceVersionType,int currentPage,int pageSize){
		int startIndex = (currentPage - 1) * pageSize;
		int endIndex = startIndex + pageSize;
		List<Map<String,String>> versionList = dao.getDevVersionList(vendor, device_model, hard_version,
				soft_version, is_check, startTime, endTime, access_style_relay_id,
				spec_id, machineConfig,ipvsix, startOpenDate, endOpenDate,
				mbBroadband,deviceVersionType,startIndex,endIndex);
		return versionList;
	}

	/**
	 * 安徽联通版本修改 根据版本id获取详情
	 * @param devTypeId
	 * @return
	 */
	public JSONObject getDetailByDevTypeId(int devTypeId){
		Map<String,String> detailMap =  dao.getDetailByDevTypeId(devTypeId);
		JSONObject result = new JSONObject();
		for (Map.Entry<String,String> entry : detailMap.entrySet()) {
			if(entry.getKey().equals("versionttime")){
				try
				{
					long versionttime = StringUtil.getLongValue(entry.getValue()) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(versionttime);
					result.put(entry.getKey(),dt.getLongDate());
					continue;
				}
				catch (NumberFormatException e)
				{
					result.put(entry.getKey(),"");
					continue;
				}

			}
			result.put(entry.getKey(),entry.getValue());
		}
		return result;
	}

	public List<Map> queryDetailForExcel(int vendor, int device_model,
										 String hard_version, String soft_version, int is_check,
										 int rela_dev_type, int curPage_splitPage, int num_splitPage,
										 String startTime, String endTime, int access_style_relay_id,
										 int spec_id, String machineConfig,String ipvsix,
										 String startOpenDate, String endOpenDate, String mbBroadband,
										 String device_version_type,String wifi,String wifi_frequency,
										 String download_max_wifi,String gigabit_port,String gigabit_port_type,
										 String download_max_lan,String power,String terminal_access_time,int isSupSpeedTest_Query){

		List<Map> list = dao.queryDetailForExcel(vendor, device_model,
				hard_version, soft_version, is_check, rela_dev_type,
				curPage_splitPage, num_splitPage, startTime, endTime,
				access_style_relay_id, spec_id, machineConfig,ipvsix,
				startOpenDate, endOpenDate, mbBroadband,device_version_type,wifi,wifi_frequency,download_max_wifi,gigabit_port,gigabit_port_type,download_max_lan,power,terminal_access_time,isSupSpeedTest_Query);
		final Map accessTypeMap = dao.getAccessType();
		for (Map result:list) {
			//审核状态
			String isCheck = StringUtil.getStringValue(result.get("is_check"));
			if("1".equals(isCheck))
			{
				result.put("is_check_name","经过审核");
			}
			else if("-1".equals(isCheck) || "0".equals(isCheck))
			{
				result.put("is_check_name","未审核");
			}
			else
			{
				result.put("is_check_name","未知");
			}
			//上行方式
			String accessStyleRelayId = StringUtil.getStringValue(result.get("access_style_relay_id"));
			if (!"".equals(accessStyleRelayId))
			{
				result.put("type_id", accessStyleRelayId);
				result.put("type_name", StringUtil.getStringValue(accessTypeMap.get(accessStyleRelayId)));
			}

			//设备IP支持方式
			String ipType = StringUtil.getStringValue(result.get("ip_type"));
			String ipModelType = StringUtil.getStringValue(result.get("ip_model_type"));

			if("0".equals(ipType) && "0".equals(ipModelType))
			{
				result.put("devIpType","ipv4");
			}
			else if("1".equals(ipType))
			{
				if("1".equals(ipModelType))
				{
					result.put("devIpType","ipv4/ipv6");
				}
				else if("2".equals(ipModelType))
				{
					result.put("devIpType","ds-lite");
				}
				else if("3".equals(ipModelType))
				{
					result.put("devIpType","laft6");
				}
				else if("4".equals(ipModelType))
				{
					result.put("devIpType","纯ipv6");
				}
				else
				{
					result.put("devIpType","未维护");
				}
			}
			else
			{
				result.put("devIpType","未维护");
			}

			//是否支持千兆带宽
			String mbbroadband = StringUtil.getStringValue(result.get("mbbroadband"));
			if("1".equals(mbbroadband))
			{
				result.put("mbbroadband","是");
			}
			else if("2".equals(mbbroadband))
			{
				result.put("mbbroadband","否");
			}
			else
			{
				result.put("mbbroadband","未维护");
			}

			//设备版本类型
			int deviceVersionType = StringUtil.getIntegerValue(result.get("device_version_type"));
			switch (deviceVersionType){
				case 1:
					result.put("device_version_type","天翼网关1.0");
					break;
				case 2:
					result.put("device_version_type","天翼网关2.0");
					break;
				case 3:
					result.put("device_version_type","天翼网关3.0");
					break;
				case 4:
					result.put("device_version_type","天翼网关4.0");
					break;
				case 5:
					result.put("device_version_type","天翼网关5.0");
					break;
				case 6:
					result.put("device_version_type","E8C");
					break;
				case 7:
					result.put("device_version_type","融合网关");
					break;
				case 8:
					result.put("device_version_type","政企网关");
					break;
				default:
					result.put("device_version_type","未维护");
			}

			//是否支持测速
			String isSpeedTest = StringUtil.getStringValue(result.get("is_speedtest"));
			logger.warn("isSpeedTest    " + isSpeedTest);
			if("1".equals(isSpeedTest))
			{
				result.put("is_speedTest","支持");
			}
			else if("0".equals(isSpeedTest))
			{
				result.put("is_speedTest","不支持");
			}
			else
			{
				result.put("is_speedTest","未维护");
			}
		}

		return list;
	}
}
