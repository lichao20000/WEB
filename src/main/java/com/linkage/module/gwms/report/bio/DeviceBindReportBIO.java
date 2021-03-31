package com.linkage.module.gwms.report.bio;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.report.dao.DeviceBindReportDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.dao.BindWayReportDAO;

public class DeviceBindReportBIO {

	/** 日志记录 */
	private static Logger logger = LoggerFactory.getLogger(DeviceBindReportBIO.class);
	
	private DeviceBindReportDAO deviceBindReportDAO;
	private BindWayReportDAO bindWayReportDAO;
	
	
	public List<Map> countAll(String vendorId, String deviceTypeId,
			String deviceModelId, String cityId, String accessType,
			String usertype, String starttime1, String endtime1) {
		
		logger.debug("countAll({},{},{},{},{},{},{},{})", new Object[] {
				vendorId, deviceTypeId, deviceModelId, cityId, accessType,
				usertype, starttime1, endtime1 });
		
		List<Map> list = new ArrayList<Map>();
		
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		
		// 根据属地cityId获取下一层属地ID(包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		
		Collections.sort(cityList);
		
		// 已注册终端
		Map<String, String> haveRegist = deviceBindReportDAO.haveRegisteredDevice(vendorId,
				deviceTypeId, deviceModelId, cityId, accessType, usertype,
				starttime1, endtime1);
		
		// 近期活跃的设备
		Map<String, String> recentActiveDevice = deviceBindReportDAO.recentActiveDevice(vendorId, 
				deviceTypeId, deviceModelId, cityId, accessType, usertype, 
				starttime1, endtime1);
		
		// 所有绑定方式
		List<Map<String, String>> bindWayList = bindWayReportDAO.getAllBindWay();
		// 所有绑定方式的绑定用户数
		List<Map<String, String>> bindwaylist = deviceBindReportDAO.countAllBindWay(vendorId,
				deviceTypeId, deviceModelId, cityId, accessType, usertype,
				starttime1, endtime1);
		
		// 处理得到的统计数据
		Map<String, Map<String, String>> clmap = new HashMap<String, Map<String, String>>();
		
		// 按照 属地，绑定方式 统计每种绑定方式的用户数
		for (Map<String, String> smap : bindWayList){
			
			Map<String, String> map = new HashMap<String, String>();
			
			for (Map<String, String> cmap : bindwaylist){
				if (StringUtil.getStringValue(cmap.get("userline")).equals(StringUtil.getStringValue(smap.get("userline")))){
					map.put(cmap.get("city_id"), StringUtil.getStringValue(cmap.get("total")));
				}
			}
			clmap.put(StringUtil.getStringValue(smap.get("userline")), map); 
		}
		
		for (int i = 0; i < cityList.size(); i++){
			
			String city_id = cityList.get(i);
			ArrayList<String> tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			
			// 用于返回到ACT
			Map<String, Object> tmap = new HashMap<String, Object>();
			
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			
			//----------已注册设备  近期活跃设备 -----begin -------
			long haveRegisteDeviceNum = 0;  // 已注册设备
			long recentActiveDeviceNum = 0; // 近期活跃设备
			for (int k = 0; k < tlist.size(); k++){
				String cityId2 = tlist.get(k);
				// 已注册设备
				haveRegisteDeviceNum = haveRegisteDeviceNum + StringUtil.getLongValue(haveRegist.get(cityId2));
				// 近期活跃设备
				recentActiveDeviceNum = recentActiveDeviceNum + StringUtil.getLongValue(recentActiveDevice.get(cityId2));
			}
			tmap.put("haveRegisteDeviceNum",haveRegisteDeviceNum);
			tmap.put("recentActiveDeviceNum", recentActiveDeviceNum);
			//----------已注册设备  近期活跃设备 -------end --------
			
			
			//---------自动绑定用户数 ------begin ----------
			// 设备物理SN自动绑定、桥接帐号自动绑定、逻辑SN自动绑定、路由帐号自动绑定四种绑定方式汇总
			// 自动绑定用户数
			long autoBindUserNum = 0;
			// 所有已绑定用户数
			long allBindUserNum = 0;
			
			for (int j = 0; j < bindWayList.size(); j++){
				
				long total = 0;  // 所有绑定用户数
				Map<String, String> smap = (Map<String, String>) bindWayList.get(j);
				
				for (int k = 0; k < tlist.size(); k++){
					
					String cityId2 = tlist.get(k);
					
					total = total + StringUtil.getLongValue(clmap.get(
									StringUtil.getStringValue(smap.get("userline"))).get(cityId2));
					
					String userLineStr = StringUtil.getStringValue(smap.get("userline"));
					
					if("3".equals(userLineStr) || "5".equals(userLineStr) || "6".equals(userLineStr) || "7".equals(userLineStr)){
						autoBindUserNum = autoBindUserNum
								+ StringUtil.getLongValue(clmap.get(
								  StringUtil.getStringValue(smap.get("userline"))).get(cityId2));
					}
				}
				allBindUserNum = allBindUserNum + total;
			}
			tmap.put("autoBindUserNum", autoBindUserNum);
			//------------自动绑定用户数 ------end ----------
			
			tmap.put("percent", percent(allBindUserNum, haveRegisteDeviceNum));  // 自动绑定率 add by zhangchy 2011-10-25 
			
			list.add(tmap);
			
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		return list;
	}
	
	/**
	 * 计算百分比 
	 * @param p1  分子
	 * @param p2  分母
	 * @return
	 */
	public String percent(long p1, long p2){
		
		logger.debug("percent({},{})", new Object[]{p1, p2});
		
		double p3;
		if (p2 == 0){
			return "N/A";
		}else{
			p3 = (double) p1 / p2;
		}
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p3);
		return str;
	}
	
	
	/**
	 * 获取设备列表 或者 自动绑定的用户列表
	 * 
	 * @param flag  0：表示展示注册终端的列表  1：表示展示近期活跃的终端 2：表示展示自动绑定的用户
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param vendorId
	 * @param deviceModelId
	 * @param deviceTypeId
	 * @param accessType
	 * @param usertype
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> getDeviceList(String flag, String cityId, String starttime1,
			String endtime1, String vendorId, String deviceModelId,
			String deviceTypeId, String accessType, String usertype,
			int curPage_splitPage, int num_splitPage) {
		
		return deviceBindReportDAO.getDeviceList(flag, cityId, starttime1,
				endtime1, vendorId, deviceModelId, deviceTypeId, accessType,
				usertype, curPage_splitPage, num_splitPage);
	}
	
	
	
	/**
	 * 统计终端 或者 用户数
	 * 
	 * @param flag 0：表示展示注册终端的列表  1：表示展示近期活跃的终端 2：表示展示自动绑定的用户
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param vendorId
	 * @param deviceModelId
	 * @param deviceTypeId
	 * @param accessType
	 * @param usertype
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int getDeviceListCount(String flag, String cityId, String starttime1,
			String endtime1, String vendorId, String deviceModelId,
			String deviceTypeId, String accessType, String usertype,
			int curPage_splitPage, int num_splitPage){
		
		return deviceBindReportDAO.getDeviceListCount(flag, cityId, starttime1,
				endtime1, vendorId, deviceModelId, deviceTypeId, accessType,
				usertype, curPage_splitPage, num_splitPage);
	}
	
	
	
	/**
	 * 获取设备列表 或者 自动绑定的用户列表
	 * 
	 * @param flag  0：表示展示注册终端的列表  1：表示展示近期活跃的终端 2：表示展示自动绑定的用户
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param vendorId
	 * @param deviceModelId
	 * @param deviceTypeId
	 * @param accessType
	 * @param usertype
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> getUserList(String flag, String cityId, String starttime1,
			String endtime1, String vendorId, String deviceModelId,
			String deviceTypeId, String accessType, String usertype,
			int curPage_splitPage, int num_splitPage) {
		
		return deviceBindReportDAO.getUserList(flag, cityId, starttime1,
				endtime1, vendorId, deviceModelId, deviceTypeId, accessType,
				usertype, curPage_splitPage, num_splitPage);
	}
	
	
	
	/**
	 * 统计终端 或者 用户数
	 * 
	 * @param flag 0：表示展示注册终端的列表  1：表示展示近期活跃的终端 2：表示展示自动绑定的用户
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param vendorId
	 * @param deviceModelId
	 * @param deviceTypeId
	 * @param accessType
	 * @param usertype
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int getUserListCount(String flag, String cityId, String starttime1,
			String endtime1, String vendorId, String deviceModelId,
			String deviceTypeId, String accessType, String usertype,
			int curPage_splitPage, int num_splitPage){
		
		return deviceBindReportDAO.getUserListCount(flag, cityId, starttime1,
				endtime1, vendorId, deviceModelId, deviceTypeId, accessType,
				usertype, curPage_splitPage, num_splitPage);
	}
	
	
	/**
	 * 设备列表 
	 * @param flag 0:已注册设备   1:近期活跃设备
	 * @param vendorId
	 * @param deviceTypeId
	 * @param deviceModelId
	 * @param cityId
	 * @param accessType
	 * @param usertype
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */
	public List<Map> getDeviceExcel(String flag, String vendorId,
			String deviceTypeId, String deviceModelId, String cityId,
			String accessType, String usertype, String starttime1,
			String endtime1) {
		
		return deviceBindReportDAO.getDeviceExcel(flag, vendorId, deviceTypeId, deviceModelId,cityId, accessType, usertype, starttime1, endtime1);
	}
	
	
	public List<Map> getUserExcel(String flag, String vendorId,
			String deviceTypeId, String deviceModelId, String cityId,
			String accessType, String usertype, String starttime1,
			String endtime1) {
		
		return deviceBindReportDAO.getUserExcel(flag, vendorId, deviceTypeId, deviceModelId,cityId, accessType, usertype, starttime1, endtime1);
	}
	
	

	public DeviceBindReportDAO getDeviceBindReportDAO() {
		return deviceBindReportDAO;
	}

	public void setDeviceBindReportDAO(DeviceBindReportDAO deviceBindReportDAO) {
		this.deviceBindReportDAO = deviceBindReportDAO;
	}

	public BindWayReportDAO getBindWayReportDAO() {
		return bindWayReportDAO;
	}

	public void setBindWayReportDAO(BindWayReportDAO bindWayReportDAO) {
		this.bindWayReportDAO = bindWayReportDAO;
	}
}
