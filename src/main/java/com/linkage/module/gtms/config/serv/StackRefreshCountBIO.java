
package com.linkage.module.gtms.config.serv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.config.dao.StackRefreshCountDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author zhaixx (Ailk No.)
 * @version 1.0
 * @category com.linkage.module.gtms.config.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class StackRefreshCountBIO {

	private static Logger logger = LoggerFactory.getLogger(StackRefreshCountBIO.class);
	private StackRefreshCountDAO dao;
	/** 开启单栈 service_id */
	public static String SERVICE_ID_SINGLE_STACK = "2301";
	/** 开启双栈 service_id */
	public static String SERVICE_ID_DOUBLE_STACK = "2302";

	public List<Map> queryStackRefreshInfo(String type, String cityId) {
		logger.warn("StackRefreshCountBIO--queryStackRefreshInfo(),type:{},cityID:{}", type, cityId);
		List date = this.dao.queryStackRefreshInfo(getService(type));
		if ((date == null) || (date.isEmpty())) {
			return new ArrayList();
		}
		Map cityMap = CityDAO.getCityIdCityNameMap();
		ArrayList cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		Collections.sort(cityList);
		List list = new ArrayList();
		for (int i = 0; i < cityList.size(); i++) {
			long countAll = 0L;
			long failnum = 0L;
			long successnum = 0L;
			long noupnum = 0L;
			String city_id = (String) cityList.get(i);
			ArrayList tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			Map tmap = new HashMap();
			tmap.put("", cityList);
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++) {
				String cityId2 = (String) tlist.get(j);
				for (int k = 0; k < date.size(); k++) {
					Map rmap = (Map) date.get(k);
					if (!cityId2.equals(rmap.get("city_id")))
						continue;
					String status = StringUtil.getStringValue(rmap.get("status"));
					String result_id = StringUtil.getStringValue(rmap.get("result_id"));
					if (("100".equals(status)) && ("1".equals(result_id))) {
						successnum += StringUtil.getLongValue(rmap.get("total"));
					}
					else if (((!"".equals(status)) || (!"".equals(result_id)))
							&& ((!"100".equals(status)) || (!"1".equals(result_id)))) {
						failnum += StringUtil.getLongValue(rmap.get("total"));
					}
					else if (("".equals(status)) && ("".equals(result_id))) {
						noupnum += StringUtil.getLongValue(rmap.get("total"));
					}
					else {
						countAll += StringUtil.getLongValue(rmap.get("total"));
					}
				}
			}
			countAll = countAll + successnum + noupnum + failnum;
			tmap.put("allup", Long.valueOf(countAll));
			tmap.put("successnum", Long.valueOf(successnum));
			tmap.put("noupnum", Long.valueOf(noupnum));
			tmap.put("failnum", Long.valueOf(failnum));
			list.add(tmap);
			tlist = null;
		}
		return list;
	}

	public List<Map> getDev(String type, String status, String cityId, int curPage_splitPage, int num_splitPage) {
		return this.dao.getDevList(getService(type), status, cityId, curPage_splitPage, num_splitPage);
	}

	public int getDevCount(String type, String status, String cityId, int curPage_splitPage, int num_splitPage) {
		return this.dao.getDevCount(getService(type), status, cityId, curPage_splitPage, num_splitPage);
	}

	public List<Map> getDevExcel(String type, String cityId, String status) {
		return this.dao.getDevExcel(getService(type), status, cityId);
	}

	/**
	 * 查询单双栈刷新统计结果
	 * 
	 * @param type
	 * @param cityId3
	 * @param type2
	 * @return
	 */
	public List<Map> queryStackRefreshInfo(String starttime1, String endtime1, String type, String cityId) {
		logger.debug("StackRefreshCountBIO--queryStackRefreshInfo(),starttime:{},endtime:{},type:{},cityID:{}",
				starttime1, endtime1, type, cityId);
		List<Map> date = dao.queryStackRefreshInfo(getService(type), starttime1, endtime1, cityId);
		if (null == date || date.isEmpty()) {
			return new ArrayList<Map>();
		}
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		Collections.sort(cityList);
		// 最终结果的list
		List<Map> list = new ArrayList<Map>();
		// 遍历属地
		for (int i = 0; i < cityList.size(); i++) {
			// 计数
			// 总数
			long countAll = 0;
			// 失败总数
			long failnum = 0;
			// 成功总数
			long successnum = 0;
			// 未触发总数
			long noupnum = 0;
			String city_id = cityList.get(i);
			ArrayList<String> tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			Map<String, Object> tmap = new HashMap<String, Object>();
			tmap.put("", cityList);
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++) {
				String cityId2 = tlist.get(j);
				for (int k = 0; k < date.size(); k++) {
					Map rmap = (Map) date.get(k);
					if (cityId2.equals(rmap.get("city_id"))) {
						//
						String status = StringUtil.getStringValue(rmap.get("status"));
						String result_id = StringUtil.getStringValue(rmap.get("result_id"));
						// 判断成功0---100
						if ("100".equals(status) && "1".equals(result_id)) {
							successnum = successnum + StringUtil.getLongValue(rmap.get("total"));
						}
						// 判断失败非0---100
						else if ((!"".equals(status) || !"".equals(result_id))
								&& (!"100".equals(status) || !"1".equals(result_id))) {
							failnum = failnum + StringUtil.getLongValue(rmap.get("total"));
						}
						// 判断未作状态
						else if ("".equals(status) && "".equals(result_id)) {
							noupnum = noupnum + StringUtil.getLongValue(rmap.get("total"));
						}
						else {
							countAll = countAll + StringUtil.getLongValue(rmap.get("total"));
						}
					}
				}
			}
			countAll = countAll + successnum + noupnum + failnum;
			tmap.put("allup", countAll);
			tmap.put("successnum", successnum);
			tmap.put("noupnum", noupnum);
			tmap.put("failnum", failnum);
			list.add(tmap);
			tlist = null;
		}
		return list;
	}

	/**
	 * 详情页查询
	 * 
	 * @param type
	 * @param status
	 * @param cityId
	 * @param num_splitPage
	 * @param curPage_splitPage
	 * @param endtime1
	 * @param starttime1
	 * @return
	 */
	public List<Map> getDev(String type, String status, String cityId, int curPage_splitPage, int num_splitPage,
			String starttime1, String endtime1) {
		return dao.getDevList(getService(type), status, cityId, curPage_splitPage, num_splitPage, starttime1, endtime1);
	}

	public int getDevCount(String type, String status, String cityId, int curPage_splitPage, int num_splitPage,
			String starttime1, String endtime1) {
		return dao
				.getDevCount(getService(type), status, cityId, curPage_splitPage, num_splitPage, starttime1, endtime1);
	}

	public List<Map> getDevExcel(String type, String cityId, String status, String starttime1, String endtime1) {
		return dao.getDevExcel(getService(type), status, cityId, starttime1, endtime1);
	}

	private String getService(String type) {
		String serviceId1 = "";
		if ("1".equals(type)) {
			serviceId1 = SERVICE_ID_SINGLE_STACK;
		}
		else if ("2".equals(type)) {
			serviceId1 = SERVICE_ID_DOUBLE_STACK;
		}
		return serviceId1;
	}

	public StackRefreshCountDAO getDao() {
		return dao;
	}

	public void setDao(StackRefreshCountDAO dao) {
		this.dao = dao;
	}
}
