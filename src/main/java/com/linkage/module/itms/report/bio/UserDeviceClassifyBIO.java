package com.linkage.module.itms.report.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.dao.UserDeviceClassifyDAO;

/**
 * 用户设备分类统计(桥接 路由 e8c e8b 是否支持wifi)及其详细信息BIO
 * @author Fanjm 35572
 * @version 1.0
 * @since 2016年11月21日
 * @category com.linkage.module.itms.report.bio
 * @copyright 2016 亚信安全.版权所有
 */
public class UserDeviceClassifyBIO {

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(UserDeviceClassifyBIO.class);
	private UserDeviceClassifyDAO dao;

	private final static String CITY_ID_KEY = "city_id";
	private final static String CITY_NAME_KEY = "city_name";
	
	//数字0字符串
	private final static String NUM_0 = "0";
	private final static String WAN_TYPE_VALUE_1 = "1";
	private final static String WAN_TYPE_VALUE_2 = "2";
	private final static String WAN_TYPE = "wan_type";
	private final static String WAN_TYPE_NUM = "wantypenum";
	private final static String WAN_TYPE_KEY1 = "wan_type1";
	private final static String WAN_TYPE_KEY2 = "wan_type2";
	
	private final static String DEVICE_TYPE_VALUE_E8B = "e8-b";
	private final static String DEVICE_TYPE_VALUE_E8C = "e8c";
	private final static String DEVICE_TYPE_VALUE_E8_C = "e8-c";
	private final static String DEVICE_TYPE_VALUE_E8CME = "ME";
	private final static String DEVICE_TYPE = "device_type";
	private final static String DEVICE_TYPE_NUM = "devicetypenum";
	private final static String DEVICE_TYPE_KEY_E8B = "device_type_e8b";
	private final static String DEVICE_TYPE_KEY_E8C = "device_type_e8c";
	private final static String DEVICE_TYPE_KEY_E8CME = "device_type_e8cme";
	
	private final static String WLAN_NUM_VALUE_NO = "0";
	private final static String WLAN_NUM_VALUE_YES = "1";
	private final static String WLAN_NUM = "wlan_num";
	private final static String WLAN_SUPPORT = "wlansupport";
	private final static String WLAN_KEY_SUPPORT = "wlan_num_support";
	private final static String WLAN_KEY_NOT_SUPPORT = "wlan_num_not_support";
	
	

	/**
	 * 根据条件查询分类（桥接用户和路由用户的数量，e8b，e8c，悦me的数量,终端支持wifi的数量，不支持wifi的数量）统计数量
	 * 
	 * @param starttime1
	 *            开始时间
	 * @param endtime1
	 *            结束时间
	 * @param cityId
	 *            当前查询地区id
	 * @param isRoot
	 *            是否跟地区
	 * @return 结果列表
	 */
	public List<Map> countUserClassifyResult(String starttime1, String endtime1, String cityId, boolean isRoot) {
		logger.debug("countUserClassifyResult({},{},{},{})", new Object[] { starttime1, endtime1, cityId ,isRoot});
		// 根据属地cityId获取下一层属地ID(包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
				
		//全部处理完成可直接用于显示的结果集
		List<Map> res = new ArrayList<Map>();
		if (StringUtil.IsEmpty(cityId)) {
			return res;
		}

		//存放桥接/路由用户数（按地区生成列表）
		List<Map<Object, Object>> wanTypeMapList = new ArrayList<Map<Object, Object>>();
		
		//存放e8b/e8c设备数（按地区生成列表）
		List<Map<Object, Object>> deviceTypeMapList = new ArrayList<Map<Object, Object>>();
		
		//存放悦me设备数（按地区生成列表）
		List<Map<Object, Object>> deviceTypeMEMapList = new ArrayList<Map<Object, Object>>();
		
		//存放不支持wifi设备数（按地区生成列表）
		List<Map<Object, Object>> wlanNumMapList0 = new ArrayList<Map<Object, Object>>();
		
		//存放支持wifi设备数（按地区生成列表）
		List<Map<Object, Object>> wlanNumMapList1 = new ArrayList<Map<Object, Object>>();
		
		// 根据省/市选择对应sql查询,连接方式 device type wlan分别用不同sql按地区分组查询统计
		if (isRoot) {
			wanTypeMapList = dao.qryWanTypeMap_root(starttime1, endtime1,cityId);
			deviceTypeMapList = dao.qryDeviceTypeMap_root(starttime1, endtime1,cityId);
			deviceTypeMEMapList = dao.qryDeviceTypeMEMap_root(starttime1, endtime1,cityId);
			wlanNumMapList0 = dao.qryWlanSupportMap_root0(starttime1, endtime1,cityId);
			wlanNumMapList1 = dao.qryWlanSupportMap_root1(starttime1, endtime1,cityId);
		} else {
			wanTypeMapList = dao.qryWanTypeMap_nroot(starttime1, endtime1,cityId);
			deviceTypeMapList = dao.qryDeviceTypeMap_nroot(starttime1, endtime1,cityId);
			deviceTypeMEMapList = dao.qryDeviceTypeMEMap_nroot(starttime1, endtime1,cityId);
			wlanNumMapList0 = dao.qryWlanSupportMap_nroot0(starttime1, endtime1,cityId);
			wlanNumMapList1 = dao.qryWlanSupportMap_nroot1(starttime1, endtime1,cityId);
		}

		sumForQryCity_wanType(wanTypeMapList, cityId);
		sumE8C(deviceTypeMapList);
		sumForQryCity_deviceType(deviceTypeMapList, cityId);
		sumForQryCity_deviceTypeME(deviceTypeMEMapList, cityId);
		sumForQryCity_wlanNum(wlanNumMapList0, wlanNumMapList1, cityId);
		
		//将悦me结果集放入deviceType中
		deviceTypeMapList.addAll(deviceTypeMEMapList);
		dealQryRes(cityList, res, wanTypeMapList, deviceTypeMapList, wlanNumMapList0);

		return res;
	}

	
	
	/**
	 * 由于deviceType字段存在e8c e8-c两种值。先将其合并处理。
	 * @param deviceTypeMapList
	 */
	private void sumE8C(List<Map<Object, Object>> deviceTypeMapList) {
		for(int i=0;i<deviceTypeMapList.size();i++){
			logger.debug("sumE8C({})", new Object[] { deviceTypeMapList});
			Map<Object,Object> map = deviceTypeMapList.get(i);
			if(DEVICE_TYPE_VALUE_E8_C.equals(map.get(DEVICE_TYPE))){
				boolean ise8chas = false;
				String cityId = StringUtil.getStringValue(map.get(CITY_ID_KEY));
				for(Map<Object,Object> e8cMap: deviceTypeMapList){
					if(cityId.equals(e8cMap.get(CITY_ID_KEY)) && DEVICE_TYPE_VALUE_E8C.equals(e8cMap.get(DEVICE_TYPE))){
						ise8chas = true;
						int tmpNum = StringUtil.getIntegerValue(e8cMap.get(DEVICE_TYPE_NUM))+StringUtil.getIntegerValue(map.get(DEVICE_TYPE_NUM));
						e8cMap.put(DEVICE_TYPE_NUM, tmpNum);
						deviceTypeMapList.remove(i);
						i--;
						break;
					}
				}
				if(!ise8chas){
					map.put(DEVICE_TYPE, DEVICE_TYPE_VALUE_E8C);
				}
			}
		}
		
	}




	/**
	 * 查询A地区时，将A的子地区统计值累计并加上A地区自身值，即为A地区及其子地区统计值(上网方式wanType)。再将A地区自身结果追加到wanType统计集合中。
	 * @param wanTypeMapList 各地区结果集(查询提交地区未累加)
	 * @param cityId 查询自身地区ID
	 */
	private void sumForQryCity_wanType(List<Map<Object, Object>> wanTypeMapList, String cityId) {
		logger.debug("sumForQryCity_wanType({},{})", new Object[] { wanTypeMapList, cityId});
		int wanTypeSum1 = 0;
		int wanTypeSum2 = 0;
		Map<Object,Object> qryCity1 = new HashMap<Object,Object>();
		Map<Object,Object> qryCity2 = new HashMap<Object,Object>();
		//是否已在wanTypeMapList找到并指定qryCity0、qryCity1
		boolean isQryCity1Found = false;
		boolean isQryCity2Found = false;
		
		//累计各个地区值
		for(Map<Object,Object> map: wanTypeMapList){
			if(WAN_TYPE_VALUE_1.equals(StringUtil.getStringValue(map.get(WAN_TYPE)))){
				wanTypeSum1 += StringUtil.getIntegerValue(map.get(WAN_TYPE_NUM));
				//指定qryCity1
				if(!isQryCity1Found && cityId.equals(map.get(CITY_ID_KEY))){
					qryCity1 = map;
					isQryCity1Found = true;
				}
			}
			else{
				wanTypeSum2 += StringUtil.getIntegerValue(map.get(WAN_TYPE_NUM));
				//指定qryCity2
				if(!isQryCity2Found && cityId.equals(map.get(CITY_ID_KEY))){
					qryCity2 = map;
					isQryCity2Found = true;
				}
			}
		}
		
		//确定提交查询地区值
		if(!isQryCity1Found){
			qryCity1.put(WAN_TYPE, WAN_TYPE_VALUE_1);
			qryCity1.put(CITY_ID_KEY, cityId);
			qryCity1.put(WAN_TYPE_NUM, StringUtil.getStringValue(wanTypeSum1));
			wanTypeMapList.add(qryCity1);
		}
		else
		{
			qryCity1.put(WAN_TYPE_NUM, StringUtil.getStringValue(wanTypeSum1));
		}
		if(!isQryCity2Found){
			qryCity2.put(WAN_TYPE, WAN_TYPE_VALUE_2);
			qryCity2.put(CITY_ID_KEY, cityId);
			qryCity2.put(WAN_TYPE_NUM, StringUtil.getStringValue(wanTypeSum2));
			wanTypeMapList.add(qryCity2);
		}
		else
		{
			qryCity2.put(WAN_TYPE_NUM, StringUtil.getStringValue(wanTypeSum2));
		}
		
	}

	
	/**
	 * 查询A地区时，将A的子地区统计值累计并加上A地区自身值，即为A地区及其子地区统计值(终端类型deviceType)。再将A地区自身结果追加到deviceType统计集合中。
	 * @param cityId 查询地区自身ID
	 * @param deviceTypeMapList 各地区结果集(查询提交地区未累加)
	 */
	private void sumForQryCity_deviceType(List<Map<Object, Object>> deviceTypeMapList, String cityId) {
		logger.debug("sumForQryCity_deviceType({},{})", new Object[] { deviceTypeMapList, cityId});
		int deviceTypeSum1 = 0;
		int deviceTypeSum2 = 0;
		Map<Object,Object> qryCity1 = new HashMap<Object,Object>();
		Map<Object,Object> qryCity2 = new HashMap<Object,Object>();
		//是否已在deviceTypeMapList找到并指定qryCity0、qryCity1
		boolean isQryCity1Found = false;
		boolean isQryCity2Found = false;
		
		//累计子地区值
		for(Map<Object,Object> map: deviceTypeMapList){
			if(DEVICE_TYPE_VALUE_E8B.equals(map.get(DEVICE_TYPE))){
				deviceTypeSum1 += StringUtil.getIntegerValue(map.get(DEVICE_TYPE_NUM));
				//指定qryCity1
				if(!isQryCity1Found && cityId.equals(map.get(CITY_ID_KEY))){
					qryCity1 = map;
					isQryCity1Found = true;
				}
			}
			else if(DEVICE_TYPE_VALUE_E8C.equals(map.get(DEVICE_TYPE))){
				deviceTypeSum2 += StringUtil.getIntegerValue(map.get(DEVICE_TYPE_NUM));
				//指定qryCity2
				if(!isQryCity2Found && cityId.equals(map.get(CITY_ID_KEY))){
					qryCity2 = map;
					isQryCity2Found = true;
				}
			}
		}
		
		//确定提交查询地区值
		if(!isQryCity1Found){
			qryCity1.put(DEVICE_TYPE, DEVICE_TYPE_VALUE_E8B);
			qryCity1.put(CITY_ID_KEY, cityId);
			qryCity1.put(DEVICE_TYPE_NUM, StringUtil.getStringValue(deviceTypeSum1));
			deviceTypeMapList.add(qryCity1);
		}
		else
		{
			qryCity1.put(DEVICE_TYPE_NUM, StringUtil.getStringValue(deviceTypeSum1));
		}
		if(!isQryCity2Found){
			qryCity2.put(DEVICE_TYPE, DEVICE_TYPE_VALUE_E8C);
			qryCity2.put(CITY_ID_KEY, cityId);
			qryCity2.put(DEVICE_TYPE_NUM, StringUtil.getStringValue(deviceTypeSum2));
			deviceTypeMapList.add(qryCity2);
		}
		else
		{
			qryCity2.put(DEVICE_TYPE_NUM, StringUtil.getStringValue(deviceTypeSum2));
		}
		
	}
	
	/**
	 * 查询A地区时，将A的子地区统计值累计并加上A地区自身值，即为A地区及其子地区统计值(终端类型deviceTypeME)。再将A地区自身结果追加到deviceType统计集合中。
	 * @param cityId 查询地区自身ID
	 * @param deviceTypeMEMapList 各地区结果集(查询提交地区未累加)
	 */
	private void sumForQryCity_deviceTypeME(List<Map<Object, Object>> deviceTypeMEMapList, String cityId) {
		logger.debug("sumForQryCity_deviceTypeME({},{})", new Object[] {deviceTypeMEMapList, cityId});
		int deviceTypeSum = 0;
		Map<Object,Object> qryCity = new HashMap<Object,Object>();
		
		//是否已在deviceTypeMEMapList找到并指定qryCity
		boolean isQryCityFound = false;
		
		//累计子地区值
		for(Map<Object,Object> map: deviceTypeMEMapList){
			deviceTypeSum += StringUtil.getIntegerValue(map.get(DEVICE_TYPE_NUM));
			//指定qryCity1
			if(!isQryCityFound && cityId.equals(map.get(CITY_ID_KEY))){
				qryCity = map;
				isQryCityFound = true;
			}
		}
		
		//确定提交查询地区值
		if(!isQryCityFound){
			qryCity.put(DEVICE_TYPE, DEVICE_TYPE_VALUE_E8CME);
			qryCity.put(CITY_ID_KEY, cityId);
			qryCity.put(DEVICE_TYPE_NUM, StringUtil.getStringValue(deviceTypeSum));
			deviceTypeMEMapList.add(qryCity);
		}
		else
		{
			qryCity.put(DEVICE_TYPE_NUM, StringUtil.getStringValue(deviceTypeSum));
		}
	}
	
	
	/**
	 * 查询A地区时，将A的子地区统计值累计并加上A地区自身值，即为A地区及其子地区统计值(是否支持wifi)。再将A地区自身结果追加到统计集合中。
	 * @param wlanNumMapList0 子地区不支持wifi结果集(查询提交地区未累加)
	 * @param wlanNumMapList1 子地区支持wifi结果集(查询提交地区未累加)
	 * @param cityId 查询地区自身ID
	 */
	private void sumForQryCity_wlanNum(
			List<Map<Object, Object>> wlanNumMapList0,
			List<Map<Object, Object>> wlanNumMapList1,
			String cityId) {
		
		logger.debug("sumForQryCity_wlanNum({},{},{})", new Object[] { wlanNumMapList0, wlanNumMapList1, cityId});
		int wlanSup = 0;
		int wlanNotSup = 0;
		
		Map<Object,Object> qryCity0 = new HashMap<Object,Object>();
		Map<Object,Object> qryCity1 = new HashMap<Object,Object>();
		
		//是否已在deviceTypeMapList找到并指定qryCity0、qryCity1
		boolean isQryCity0Found = false;
		boolean isQryCity1Found = false;
		
		//累计子地区值
		for(Map<Object,Object> map: wlanNumMapList0){
			wlanNotSup += StringUtil.getIntegerValue(map.get(WLAN_SUPPORT));
			//指定qryCity0
			if(!isQryCity0Found && cityId.equals(map.get(CITY_ID_KEY))){
				qryCity0 = map;
				isQryCity0Found = true;
			}
		}
		for(Map<Object,Object> map: wlanNumMapList1){
			wlanSup += StringUtil.getIntegerValue(map.get(WLAN_SUPPORT));
			//指定qryCity1
			if(!isQryCity1Found && cityId.equals(map.get(CITY_ID_KEY))){
				qryCity1 = map;
				isQryCity1Found = true;
			}
		}
		
		//确定提交查询地区值
		if(!isQryCity0Found){
			qryCity0.put(WLAN_NUM, WLAN_NUM_VALUE_NO);
			qryCity0.put(CITY_ID_KEY, cityId);
			qryCity0.put(WLAN_SUPPORT, StringUtil.getStringValue(wlanNotSup));
			wlanNumMapList0.add(qryCity0);
		}
		else
		{
			qryCity0.put(WLAN_SUPPORT, StringUtil.getStringValue(wlanNotSup));
		}
		if(!isQryCity1Found){
			qryCity1.put(WLAN_NUM, WLAN_NUM_VALUE_YES);
			qryCity1.put(CITY_ID_KEY, cityId);
			qryCity1.put(WLAN_SUPPORT, StringUtil.getStringValue(wlanSup));
			wlanNumMapList1.add(qryCity1);
		}
		else
		{
			qryCity1.put(WLAN_SUPPORT, StringUtil.getStringValue(wlanSup));
		}
		
		//追加整合
		wlanNumMapList0.addAll(wlanNumMapList1);
	}

	
	
	
	/**
	 * 遍历所查询地区的全部下一级地区(包括自己)，从之前数据库查询数据将整理结果放入res结果集
	 * @param cityList cityId下一层属地ID(包含自己)
	 * @param res 最终传给页面显示的结果集
	 * @param wanTypeMapList 连接方式结果集
	 * @param deviceTypeMapList deviceType 结果集 
	 * @param wlanNumMapList0 支持wifi结果集
	 */
	private void dealQryRes(ArrayList<String> cityList,
			List<Map> res,
			List<Map<Object, Object>> wanTypeMapList,
			List<Map<Object, Object>> deviceTypeMapList,
			List<Map<Object, Object>> wlanNumMapList0) {
		logger.debug("dealQryRes({},{},{},{},{})", new Object[] { cityList, res, wanTypeMapList ,deviceTypeMapList, wlanNumMapList0});
		
		if(null==cityList){
			return ;
		}
		
		//遍历所查询地区的全部下一级地区(包括自己)
		for(String tmpCity : cityList){
			Map<Object, Object> tmpMap = new HashMap<Object, Object>();
			tmpMap.put(CITY_ID_KEY, tmpCity);
			tmpMap.put(CITY_NAME_KEY, Global.G_CityId_CityName_Map.get(tmpCity));
			
			dealWanTypeRes(wanTypeMapList, tmpMap, tmpCity);
			dealDeviceTypeRes(deviceTypeMapList, tmpMap, tmpCity);
			dealWlanSupportRes(wlanNumMapList0, tmpMap, tmpCity);
			res.add(tmpMap);
		}
		
	}
	

	/**
	 * 将wanType统计结果集合转化成最终形式List<Map>供页面显示
	 * @param wanTypeMapList wanType统计结果集合
	 * @param tmpMap 存放当前City的全部统计最终结果
	 * @param tmpCity 当前CityID
	 */
	private void dealWanTypeRes(List<Map<Object, Object>> wanTypeMapList,
			Map<Object, Object> tmpMap, String tmpCity) {
		logger.debug("dealWanTypeRes({},{},{})", new Object[] { wanTypeMapList, tmpMap, tmpCity});
		//当前city的两种wanType统计是否完成
		boolean isWanType1 = false;
		boolean isWanType2 = false;
		
		for(int i=0;i<wanTypeMapList.size();i++){
			Map<Object,Object> tmpWanTypeMap = wanTypeMapList.get(i);
			if(!isWanType1 && tmpCity.equals(tmpWanTypeMap.get(CITY_ID_KEY)) && WAN_TYPE_VALUE_1.equals(StringUtil.getStringValue(tmpWanTypeMap.get(WAN_TYPE)))){
				tmpMap.put(WAN_TYPE_KEY1,StringUtil.getStringValue(tmpWanTypeMap.get(WAN_TYPE_NUM)));
				isWanType1 = true;
			}
			else if(!isWanType2 && tmpCity.equals(tmpWanTypeMap.get(CITY_ID_KEY)) && WAN_TYPE_VALUE_2.equals(StringUtil.getStringValue(tmpWanTypeMap.get(WAN_TYPE)))){
				tmpMap.put(WAN_TYPE_KEY2, StringUtil.getStringValue(tmpWanTypeMap.get(WAN_TYPE_NUM)));
				isWanType2 = true;
			}
			//当前city的两种wanType统计完成
			if(isWanType1 && isWanType2){
				break;
			}
			else{
				//当前city的wanType遍历结束还没遇到统计值，手动赋值0。
				if(i==(wanTypeMapList.size()-1)){
					if(!isWanType1){
						tmpMap.put(WAN_TYPE_KEY1, NUM_0);
					}
					if(!isWanType2){
						tmpMap.put(WAN_TYPE_KEY2, NUM_0);
					}
					break;
				}
				else{
					continue;
				}
				
			}
		}
		
	}
	
	
	/**
	 * 将deviceType统计结果集合转化成最终形式List<Map>供页面显示
	 * @param deviceTypeMapList deviceType统计结果集合
	 * @param tmpMap 存放当前City的全部统计最终结果
	 * @param tmpCity 当前CityID
	 */
	private void dealDeviceTypeRes(List<Map<Object, Object>> deviceTypeMapList,
			Map<Object, Object> tmpMap, String tmpCity) {
		logger.debug("dealDeviceTypeRes({},{},{})", new Object[] { deviceTypeMapList, tmpMap, tmpCity});
		
		//当前city的三种deviceType统计是否完成
		boolean isDeviceTypeB = false;
		boolean isDeviceTypeC = false;
		boolean isDeviceTypeCME = false;
		
		for(int i=0;i<deviceTypeMapList.size();i++){
			Map<Object,Object> tmpDeviceTypeMap = deviceTypeMapList.get(i);
			if(!isDeviceTypeB && tmpCity.equals(tmpDeviceTypeMap.get(CITY_ID_KEY)) && DEVICE_TYPE_VALUE_E8B.equals(tmpDeviceTypeMap.get(DEVICE_TYPE))){
				tmpMap.put(DEVICE_TYPE_KEY_E8B,StringUtil.getStringValue(tmpDeviceTypeMap.get(DEVICE_TYPE_NUM)));
				isDeviceTypeB = true;
			}
			else if(!isDeviceTypeC && tmpCity.equals(tmpDeviceTypeMap.get(CITY_ID_KEY)) && DEVICE_TYPE_VALUE_E8C.equals(tmpDeviceTypeMap.get(DEVICE_TYPE))){
				tmpMap.put(DEVICE_TYPE_KEY_E8C, StringUtil.getStringValue(tmpDeviceTypeMap.get(DEVICE_TYPE_NUM)));
				isDeviceTypeC = true;
			}
			else if(!isDeviceTypeCME && tmpCity.equals(tmpDeviceTypeMap.get(CITY_ID_KEY)) && DEVICE_TYPE_VALUE_E8CME.equals(tmpDeviceTypeMap.get(DEVICE_TYPE))){
				tmpMap.put(DEVICE_TYPE_KEY_E8CME, StringUtil.getStringValue(tmpDeviceTypeMap.get(DEVICE_TYPE_NUM)));
				isDeviceTypeCME = true;
			}
			//当前city的三种deviceType统计完成
			if(isDeviceTypeB && isDeviceTypeC && isDeviceTypeCME){
				break;
			}
			else{
				//当前city的deviceType遍历结束还没遇到统计值，手动赋值0。
				if(i==(deviceTypeMapList.size()-1)){
					if(!isDeviceTypeB){
						tmpMap.put(DEVICE_TYPE_KEY_E8B, NUM_0);
					}
					if(!isDeviceTypeC){
						tmpMap.put(DEVICE_TYPE_KEY_E8C, NUM_0);
					}
					if(!isDeviceTypeCME){
						tmpMap.put(DEVICE_TYPE_KEY_E8CME, NUM_0);
					}
					break;
				}
				else{
					continue;
				}
				
			}
		}
		
	}

	

	/**
	 * 将wlan_num计结果集合转化成最终形式List<Map>供页面显示
	 * @param wlanNumMapList0  wlan_num统计结果集合
	 * @param tmpMap 存放当前City的全部统计最终结果
	 * @param tmpCity 当前CityID
	 */
	private void dealWlanSupportRes(List<Map<Object, Object>> wlanNumMapList0,
			Map<Object, Object> tmpMap, String tmpCity) {
		logger.debug("dealWlanSupportRes({},{},{})", new Object[] { wlanNumMapList0, tmpMap, tmpCity});
		
		//当前city的两种wlan_num统计是否完成
		boolean isWlanSup = false;
		boolean isWlanNotSup = false;
		
		for(int i=0;i<wlanNumMapList0.size();i++){
			Map<Object,Object> tmpWlanMap = wlanNumMapList0.get(i);
			if(!isWlanSup && tmpCity.equals(tmpWlanMap.get(CITY_ID_KEY)) && WLAN_NUM_VALUE_YES.equals(StringUtil.getStringValue(tmpWlanMap.get(WLAN_NUM)))){
				tmpMap.put(WLAN_KEY_SUPPORT,StringUtil.getStringValue(tmpWlanMap.get(WLAN_SUPPORT)));
				isWlanSup = true;
			}
			else if(!isWlanNotSup && tmpCity.equals(tmpWlanMap.get(CITY_ID_KEY)) && WLAN_NUM_VALUE_NO.equals(StringUtil.getStringValue(tmpWlanMap.get(WLAN_NUM)))){
				tmpMap.put(WLAN_KEY_NOT_SUPPORT, StringUtil.getStringValue(tmpWlanMap.get(WLAN_SUPPORT)));
				isWlanNotSup = true;
			}
			//当前city的两种wlan_num统计完成
			if(isWlanSup && isWlanNotSup){
				break;
			}
			else{
				//当前city的wlan_num遍历结束还没遇到统计值，手动赋值0。
				if(i==(wlanNumMapList0.size()-1)){
					if(!isWlanSup){
						tmpMap.put(WLAN_KEY_SUPPORT, NUM_0);
					}
					if(!isWlanNotSup){
						tmpMap.put(WLAN_KEY_NOT_SUPPORT, NUM_0);
					}
					break;
				}
				else{
					continue;
				}
				
			}
		}
		
	}

	

	/**
	 * 根据地区、时间区间、当前列的分类条件查询设备列表详细信息
	 * @param starttime1 开始时间
	 * @param endtime1	结束时间
	 * @param cityId 地区id
	 * @param curPage_splitPage 当前查询页数
	 * @param num_splitPage 每页显示数目
	 * @param classfy 分类条件(用户连接方式-桥接or路由、设备类型e8b e8c、是否支持wifi)
	 * @param isRoot 是否省级总地区
	 * @return 符合条件的设备列表
	 */
	public List<Map> getDevList(String starttime1, String endtime1,
			String cityId, int curPage_splitPage, int num_splitPage, String classfy, boolean isRoot) {
		return dao.getDevList(starttime1, endtime1, cityId, curPage_splitPage, num_splitPage, classfy, isRoot);
	}




	/**
	 * 根据getDevList方法查询结果的总条数、页面条数计算总页数
	 * @param starttime1 开始时间
	 * @param endtime1	结束时间
	 * @param cityId 地区id
	 * @param curPage_splitPage 当前查询页数
	 * @param num_splitPage 每页显示数目
	 * @param classfy 分类条件(用户连接方式-桥接or路由、设备类型e8b e8c、是否支持wifi)
	 * @param isRoot 是否省级总地区
	 * @return 符合条件的设备列表
	 */
	public int getDevCount(String starttime1, String endtime1, String cityId,
			int curPage_splitPage, int num_splitPage, String classfy, boolean isRoot) {
		return dao.getDevCount(starttime1, endtime1, cityId, curPage_splitPage, num_splitPage, classfy,isRoot);
	}



	/**
	 * 根据地区、时间区间、当前列的分类条件查询设备列表详细信息(导出)
	 * @param starttime1 开始时间
	 * @param endtime1	结束时间
	 * @param cityId 地区id
	 * @param classfy 分类条件(用户连接方式-桥接or路由、设备类型e8b e8c、是否支持wifi)
	 * @param isRoot 是否省级总地区
	 * @return 符合条件的设备列表
	 */
	public List<Map> getDevExcel(String starttime1,
			String endtime1, String cityId, String classfy, boolean isRoot) {
		return dao.getDevExcel(starttime1, endtime1, cityId, classfy, isRoot);
	}
	
	


	public UserDeviceClassifyDAO getDao() {
		return dao;
	}

	public void setDao(UserDeviceClassifyDAO dao) {
		this.dao = dao;
	}
	
}
