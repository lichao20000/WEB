package com.linkage.module.itms.report.bio;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.dao.OpenBusinessDAO;

@SuppressWarnings("rawtypes")
public class QueryOpenBusinessBIO {
	private OpenBusinessDAO businessDao;

	private List totalList;
	private List successTotalList;
	private List failTotalList;

	/**
	 * 我们计算总数的时候只会查询一次数据库！存在一个List中！
	 * 
	 * @param starttime
	 * @param endtime
	 */
	public void prepareForToalQuery(String starttime, String endtime) {
		totalList = businessDao.queryPrepareUserByTimeTotal(starttime, endtime);
		successTotalList = businessDao.queryPrepareUserByTimeTotalSuccess(starttime,
				endtime);
		failTotalList = businessDao
				.queryPrepareUserByTimeTotalFail(starttime, endtime);
	}

	/**
	 * 只查询city_id不考虑下级城市！ 注意：因为SQL语句使用了group，所以只会有一个city_id的total
	 * 
	 * @param city_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map queryUserSingleByCityId(String city_id) {
		Map returnMap = new HashMap();

		returnMap.put("total", "0");
		returnMap.put("success", "0");
		returnMap.put("fail", "0");

		for (int i = 0; i < totalList.size(); i++) {
			Map tMap = (Map) totalList.get(i);
			if (tMap.get("city_id").equals(city_id)) {
				returnMap.put("total", tMap.get("total"));
				break;
			}
		}

		for (int i = 0; i < successTotalList.size(); i++) {
			Map tMap = (Map) successTotalList.get(i);
			if (tMap.get("city_id").equals(city_id)) {
				returnMap.put("success", tMap.get("total"));
				break;
			}
		}

		for (int i = 0; i < failTotalList.size(); i++) {
			Map tMap = (Map) failTotalList.get(i);
			if (tMap.get("city_id").equals(city_id)) {
				returnMap.put("fail", tMap.get("total"));
				break;
			}
		}

		return returnMap;
	}

	/**
	 * 
	 * @param city_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map queryUserByCityId(String city_id) {
		/** 获取子属地，不包括自己 */
		if (totalList == null && totalList.size() == 0) {
			return null;
		}
		/* 获取city_id下面的所有子属地，一致到最低层的属地，包括自己 */
		ArrayList<String> list = CityDAO.getAllNextCityIdsByCityPid(city_id);
		Map totalMap = new HashMap();
		/** 遍历每一个属地，包含自己 */
		for (String id : list) {
			Map map = queryUserSingleByCityId(id);
			/** 成功数 */
			totalMap.put("total",
					StringUtil.getIntegerValue(totalMap.get("total"))
							+ StringUtil.getIntegerValue(map.get("total")));
			totalMap.put("success",
					StringUtil.getIntegerValue(totalMap.get("success"))
							+ StringUtil.getIntegerValue(map.get("success")));
			totalMap.put("fail",
					StringUtil.getIntegerValue(totalMap.get("fail"))
							+ StringUtil.getIntegerValue(map.get("fail")));
		}

		String pert = getDecimal(
				StringUtil.getStringValue(totalMap.get("success")),
				StringUtil.getStringValue(totalMap.get("total")));
		totalMap.put("pert", pert);
		return totalMap;
	}

	/**
	 * 查找该属地下面的用户的详细信息
	 * 
	 * @param dataType
	 *            : 用户总数 1 ;成功数 2; 失败数 3
	 * @param city_id
	 *            ：属地ID
	 * @param starttime
	 *            ：查询的开始时间
	 * @param endtime
	 *            ：查询结束时间
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> queryDetailListExcel(String dataType, String city_id,
			String starttime, String endtime) {
		List<Map> totalList;
		ArrayList<String> list = CityDAO.getAllNextCityIdsByCityPid(city_id);
		/* 根据属地和时间查询所有用户的信息！ 不能只查 */
		if (dataType.equals("1")) {/* 如果查询的是总数 */
			totalList = businessDao.queryDetailByTimeTotalExcel(list, starttime,
					endtime);
		} else if (dataType.equals("2")) {/* 如果查询的是成功数 */
			totalList = businessDao.queryDetailByTimeSuccessExcel(list, starttime,
					endtime);
		} else {/* 失败数 */
			totalList = businessDao.queryDetailByTimeFailExcel(list, starttime,
					endtime);
		}

		/** 处理属地名称 */
		for (int i = 0; i < totalList.size(); i++) {
			Map map = totalList.get(i);
			long binddata = StringUtil.getLongValue(map.get("binddate"));
			/** 将Unix时间戳转化为本地时间 */
			String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new java.util.Date(binddata * 1000));
			map.put("binddate", date.toString());
			/** city_id对应的属地名称 */
			map.put("city_name",
					CityDAO.getCityName((String) map.get("city_id")));
		}
		return totalList;
	}

	/**
	 * 查找该属地是否存在子属地
	 * 
	 * @param cityId
	 * @return
	 */
	public boolean hasChidCity(String cityId) {
		Map<String, String> cityIdMap = new HashMap<String, String>();
		cityIdMap = CityDAO.getCityIdPidMap();

		// 遍历key集合，获取value
		Set entrys = cityIdMap.entrySet();
		Iterator it2 = entrys.iterator();
		while (it2.hasNext()) {
			Map.Entry entry = (Map.Entry) it2.next();
			String key = String.valueOf(entry.getKey()).toString();
			if (cityId.equals(cityIdMap.get(key))) {
				return true;
			}
		}
		return false;
	}

	/***
	 * 根据传过来的属地ID查询该属地下是不是有子属地！
	 * */
	public List getChildCityList(String cityId) {
		List<Map<String, String>> cityList = new ArrayList<Map<String, String>>();

		Map<String, String> cityIdMap = new HashMap<String, String>();
		cityIdMap = CityDAO.getCityIdPidMap();
		Map<String, String> allCityMap = CityDAO.getCityIdCityNameMap();
		// 遍历key集合，获取value
		Set entrys = cityIdMap.entrySet();

		/* 先把自己加上去 */
		Map<String, String> cityMeMap = new HashMap<String, String>();
		cityMeMap.put("city_id", cityId);
		cityMeMap.put("city_name", allCityMap.get(cityId));
		cityMeMap.put("hasCityId", hasChidCity(cityId) ? "false" : "true");
		cityList.add(cityMeMap);

		Iterator it2 = entrys.iterator();
		while (it2.hasNext()) {
			Map.Entry entry = (Map.Entry) it2.next();
			String key = String.valueOf(entry.getKey()).toString();
			if (!cityId.equals(cityIdMap.get(key))) {
				continue;
			}
			Map<String, String> cityMap = new HashMap<String, String>();
			cityMap.put("city_id", key);
			cityMap.put("city_name", allCityMap.get(key));
			/* 判断有没有子属地 */
			cityMap.put("hasCityId", hasChidCity(key) ? "true" : "false");
			cityList.add(cityMap);
		}
		cityIdMap = null;
		allCityMap = null;
		return cityList;
	}

	/**
	 * 计算一个数占另一个数的比率！
	 * */
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

	public OpenBusinessDAO getBusinessDao() {
		return businessDao;
	}

	public void setBusinessDao(OpenBusinessDAO businessDao) {
		this.businessDao = businessDao;
	}

	/**
	 * 分页
	 * 
	 * @param dataType
	 * @param city_id
	 * @param starttime
	 * @param endtime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> queryDetailListPage(String dataType, String city_id,
			String starttime, String endtime, int curPage_splitPage,
			int num_splitPage) {
		/**getAllNextCityIdsByCityPid获取子属地，包括二级三级等等**/
		ArrayList<String> citylist = CityDAO.getAllNextCityIdsByCityPid(city_id);
		return businessDao.queryDetailListPage(dataType, citylist, starttime,
				endtime, curPage_splitPage, num_splitPage);
	}
    
	/**
	 * 获取总的数目
	 * @param dataType
	 * @param city_id
	 * @param starttime
	 * @param endtime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int getDeviceCount(String dataType, String city_id,
			String starttime, String endtime, int curPage_splitPage,
			int num_splitPage) {
		ArrayList<String> citylist = CityDAO.getAllNextCityIdsByCityPid(city_id);
		return businessDao.queryDetailCount(dataType, citylist, starttime,
				endtime, curPage_splitPage, num_splitPage);
	}
}
