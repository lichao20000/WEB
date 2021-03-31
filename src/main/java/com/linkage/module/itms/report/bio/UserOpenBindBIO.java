package com.linkage.module.itms.report.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.dao.UserOpenBindDAO;

/**
 * 新开用户绑定终端情况 Bio
 * @author zhangyu
 *
 */
@SuppressWarnings("all")
public class UserOpenBindBIO {
	
	/** 日志记录 */
	private static Logger logger = LoggerFactory.getLogger(UserOpenBindBIO.class);
	
	private UserOpenBindDAO userOpenBindDAO;

	/**
	 * 新开用户绑定终端情况 列表
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> userOpenBindList(String cityId, String starttime, String endtime, 
			int curPage_splitPage, int num_splitPage) {
		
		logger.debug("userOpenBindList({},{},{},{},{})", 
				new Object[]{cityId, starttime, endtime, curPage_splitPage, num_splitPage});
		
		List<Map> list = userOpenBindDAO.userOpenBindList(cityId, starttime, endtime, curPage_splitPage, num_splitPage);
		
		Map<String, String> venderMap = userOpenBindDAO.getVenderMap();
		Map<String, String> deviceModelMap = userOpenBindDAO.getDeviceModelMap();
		
		String city_id;
		String vendor_id;
		String device_model_id;
		String vendor_name;
		String device_model;
		String opendate; 
		String binddate;
		String last_time;
		DateTimeUtil dt = null;
		for (Map map : list) {
			
			city_id = StringUtil.getStringValue(map, "city_id");
			map.put("city_name", Global.G_CityId_CityName_Map.get(city_id));// 属地
			
			vendor_id = StringUtil.getStringValue(map, "vendor_id");
			device_model_id = StringUtil.getStringValue(map, "device_model_id");
			map.put("vendor_name", venderMap.get(vendor_id));// 厂商
			map.put("device_model", deviceModelMap.get(device_model_id));// 型号
			
			if(!StringUtil.IsEmpty(StringUtil.getStringValue(map, "opendate"))) {
				dt = new DateTimeUtil(StringUtil.getLongValue(map, "opendate") * 1000);
				map.put("opendate", dt.getLongDate()); // 开户时间
			}
			if(!StringUtil.IsEmpty(StringUtil.getStringValue(map, "binddate"))) {
				dt = new DateTimeUtil(StringUtil.getLongValue(map, "binddate") * 1000);
				map.put("binddate", dt.getLongDate()); // 开户时间
			}
			if(!StringUtil.IsEmpty(StringUtil.getStringValue(map, "last_time"))) {
				dt = new DateTimeUtil(StringUtil.getLongValue(map, "last_time") * 1000);
				map.put("last_time", dt.getLongDate()); // 开户时间
			}
			
		}
		
		return list;
	}
	
	/**
	 * 新开用户绑定终端情况 总数
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int userOpenBindCount(String cityId, String starttime, String endtime) {
		return userOpenBindDAO.userOpenBindCount(cityId, starttime, endtime);
	}

	/**
	 * 根据area_id查询city_id
	 * @param area_id
	 * @return
	 */
	public List<Map> queryCityIdByAreaId(String area_id) {
		return userOpenBindDAO.queryCityIdByAreaId(area_id);
	}
	
	
	public UserOpenBindDAO getUserOpenBindDAO() {
		return userOpenBindDAO;
	}

	public void setUserOpenBindDAO(UserOpenBindDAO userOpenBindDAO) {
		this.userOpenBindDAO = userOpenBindDAO;
	}
	
}
