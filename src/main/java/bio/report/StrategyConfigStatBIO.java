package bio.report;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.module.gwms.dao.tabquery.CityDAO;

import dao.report.StrategyConfigStatDAO;

/**
 * @author Jason(3412)
 * @date 2009-4-28
 */
public class StrategyConfigStatBIO {

	StrategyConfigStatDAO statDao;
	
	
	/**
	 * 返回统计结果报表数据
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-4-28
	 * @return List
	 */
	public List<Map> getStatResList(int serviceId, String cityPid,
			long startTime, long endTime) {
		// 存放结果，返回值
		List<Map> resultList = new ArrayList<Map>();
		// 获取查询属地的下级属地及自身(这些属地是需要显示的)
		List<String> nextcityIdList = CityDAO.getNextCityIdsByCityPid(cityPid);
		
		// 获取统计结果
		List<Map> statList = statDao.statServUserByCity(
				serviceId, cityPid, startTime, endTime);
		// 对每一个要显示的属地
		long total = 0L, success = 0L, fail = 0L, undo = 0, unsuccess=0L;
		DecimalFormat dateFormat = new DecimalFormat("###.#%");
		//获取所有属地的Map 属地ID、属地名Map<city_id,city_name>
		Map<String,String> cityMap = CityDAO.getCityIdCityNameMap();
		// log.debug(nextcityIdList.toString());
		for (String cityId : nextcityIdList) {
			// log.debug("for every next city:" + cityId);
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("city_id", cityId);
			// 记录数目
			total = 0L;
			success = 0L;
			fail = 0L;
			undo = 0;
			unsuccess = 0L;
			// 获取属地的所有子属地，包括自己
			List<String> sonCityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			resultMap.put("city_name", cityMap.get(cityId));
			// 对每一条统计记录
			for (Map statMap : statList) {
				// log.debug(statMap.toString());
				// 遍历所有子属地统计记录
				if (sonCityList.contains(statMap.get("city_id"))) {
					// new BigDecimal(String.valueOf(statMap.get("total"))).add()
					total += Long.valueOf(String.valueOf(statMap.get("total")));
					fail += Long.valueOf(String.valueOf(statMap.get("fail")));
					undo += Long.valueOf(String.valueOf(statMap.get("undo")));
					success += Long.valueOf(String.valueOf(statMap.get("success")));
					unsuccess += Long.valueOf(String.valueOf(statMap.get("unsuccess")));
				}
			}
			sonCityList = null;
			resultMap.put("total", String.valueOf(total));
			resultMap.put("success", String.valueOf(success));
			resultMap.put("fail", String.valueOf(fail));
			resultMap.put("undo", String.valueOf(undo));
			resultMap.put("unsuccess", String.valueOf(unsuccess));
			if (success+fail == 0) {
				resultMap.put("percent", "0.0%");
			} else {
				resultMap.put("percent", dateFormat.format((success * 1.0 / (success+fail))));
			}
			// log.debug(resultMap.toString());
			resultList.add(resultMap);
		}
		nextcityIdList = null;
		cityMap = null;
		// log.debug(resultList.toString());
		return resultList;
	}
	
	/**
	 * 组成返回结果HTML字符串
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-8
	 * @return String
	 */
	public String statListToString(List<Map> list, String serviceId, long startTime, long endTime) {
		// 数字超链接到设备列表的路径
		String devListUrl = "strategyConfigStat!servUserListExecute.action?serviceId="
				+ serviceId + "&startTime=" + startTime + "&endTime=" + endTime;
		if(null == list || list.size() == 0){
			return "内部错误";
		}
		StringBuilder strbuilder = new StringBuilder();
		strbuilder.append("<table>");
		// 标题行
		strbuilder.append("<tr><th width=15%>");
		strbuilder.append("属地");
		strbuilder.append("</th><th width=10%>");
		strbuilder.append("用户总数");
		strbuilder.append("</th><th width=10%>");
		strbuilder.append("成功");
		strbuilder.append("</th><th width=10%>");
		strbuilder.append("失败(需重配)");
		strbuilder.append("</th><th width=20%>");
		strbuilder.append("暂未成功(不需重配)");
		strbuilder.append("</th><th width=10%>");
		strbuilder.append("未触发");
		strbuilder.append("</th><th width=25%>");
		strbuilder.append("成功率(成功数/执行数)");
		strbuilder.append("</th></tr>");
		// 数据
		for (Map map : list) {
			String cityId = String.valueOf(map.get("city_id"));
			String jsCityId = "&cityId=" + cityId;
			String tmpDevListUrl = devListUrl + jsCityId;
			strbuilder.append("<tr value='" + cityId + "'><td>");
			strbuilder.append(map.get("city_name"));
			strbuilder.append("</td><td>");
			strbuilder.append("<a href='" + tmpDevListUrl + "'>");
			strbuilder.append(map.get("total"));
			strbuilder.append("</a></td><td>");
			strbuilder.append("<a href='" + tmpDevListUrl + "&exeStatus=1'>");
			strbuilder.append(map.get("success"));
			strbuilder.append("</a></td><td>");
			strbuilder.append("<a href='" + tmpDevListUrl + "&exeStatus=-1'>");
			strbuilder.append(map.get("fail"));
			strbuilder.append("</a></td><td>");
			strbuilder.append("<a href='" + tmpDevListUrl + "&exeStatus=2'>");
			strbuilder.append(map.get("unsuccess"));
			strbuilder.append("</a></td><td>");
			strbuilder.append("<a href='" + tmpDevListUrl + "&exeStatus=0'>");
			strbuilder.append(map.get("undo"));
			strbuilder.append("</a></td><td>");
			strbuilder.append(map.get("percent"));
			strbuilder.append("</td></tr>");
		}
		strbuilder.append("</table>");

		return strbuilder.toString();
	}
	
	/**
	 * 业务用户列表
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-8
	 * @return ArrayList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList getServUserListData(String servTypeId, String cityId,
			String exeStatus, long startTime, long endTime) {
		ArrayList<Map> userlist = statDao.queryServUserListData(servTypeId, cityId, exeStatus,
				startTime, endTime);
		//获取所有属地的Map 属地ID、属地名Map<city_id,city_name>
		Map<String,String> cityMap = CityDAO.getCityIdCityNameMap();
		if(null != userlist && userlist.size() > 0){
			for(Map<String, String> map : userlist){
				map.put("city_name", cityMap.get(map.get("city_id")));
			}
		}
		cityMap = null;
		return userlist;
	}
		
	public String listToString(List<Map<String, String>> list, String serviceId, long startTime, long endTime) {
		// 数字超链接到设备列表的路径
		String devListUrl = "strategyConfigStat!devListExecute.action?serviceId="
				+ serviceId + "&startTime=" + startTime + "&endTime=" + endTime;
		StringBuilder strbuilder = new StringBuilder();
		strbuilder.append("<table>");
		// 标题行
		strbuilder.append("<tr><th width=25%>");
		strbuilder.append("属地");
		strbuilder.append("</th><th width=10%>");
		strbuilder.append("配置设备总数");
		strbuilder.append("</th><th width=10%>");
		strbuilder.append("执行成功");
		strbuilder.append("</th><th width=10%>");
		strbuilder.append("执行失败");
		strbuilder.append("</th><th width=10%>");
		strbuilder.append("未触发");
		strbuilder.append("</th><th width=15%>");
		strbuilder.append("成功率");
		strbuilder.append("</th></tr>");
		// 数据
		for (Map<String, String> map : list) {
			String cityId = map.get("city_id");
			String jsCityId = "&cityId=" + cityId;
			String tmpDevListUrl = devListUrl + jsCityId;
			strbuilder.append("<tr value='" + cityId + "'><td>");
			strbuilder.append(map.get("city_name"));
			strbuilder.append("</td><td>");
			strbuilder.append("<a href='" + tmpDevListUrl + "&exeStatus=100'>");
			strbuilder.append(map.get("total"));
			strbuilder.append("</a></td><td>");
			strbuilder.append("<a href='" + tmpDevListUrl + "&exeStatus=1'>");
			strbuilder.append(map.get("success"));
			strbuilder.append("</a></td><td>");
			strbuilder.append("<a href='" + tmpDevListUrl + "&exeStatus=-1'>");
			strbuilder.append(map.get("fail"));
			strbuilder.append("</a></td><td>");
			strbuilder.append("<a href='" + tmpDevListUrl + "&exeStatus=0'>");
			strbuilder.append(map.get("undo"));
			strbuilder.append("</a></td><td>");
			strbuilder.append(map.get("percent"));
			strbuilder.append("</td></tr>");
		}
		strbuilder.append("</table>");

		return strbuilder.toString();
	}

	/**
	 * 返回统计结果报表数据
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-4-28
	 * @return List
	 */
	public List<Map<String, String>> getStatList(int serviceId, String cityPid,
			long startTime, long endTime) {
		// 存放结果，返回值
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		// 获取查询属地的下级属地及自身(这些属地是需要显示的)
		List<String> nextcityIdList = CityDAO.getNextCityIdsByCityPid(cityPid);
		
		// 获取统计结果
		List<Map<String, String>> statList = statDao.getStatStrategyResult(
				serviceId, cityPid, startTime, endTime);
		// 对每一个要显示的属地
		long total = 0L, success = 0L, fail = 0L, undo = 0;
		DecimalFormat dateFormat = new DecimalFormat("###.#%");
		// log.debug(nextcityIdList.toString());
		for (String cityId : nextcityIdList) {
			// log.debug("for every next city:" + cityId);
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("city_id", cityId);
			// 记录数目
			total = 0L;
			success = 0L;
			fail = 0L;
			undo = 0;
			// 获取属地的所有子属地，包括自己
			List<String> sonCityList =CityDAO.getAllNextCityIdsByCityPid(cityId);
			// log.debug(sonCityList.toString());
			// 对每一条统计记录
			for (Map<String, String> statMap : statList) {
				// log.debug(statMap.toString());
				if (cityId.equals(statMap.get("city_id"))) {
					resultMap.put("city_name", String.valueOf(statMap
							.get("city_name")));
				}
				// 如果子属地包含该条统计记录
				if (sonCityList.contains(statMap.get("city_id"))) {
					// new BigDecimal(String.valueOf(statMap.get("total"))).add()
					total += Long.valueOf(statMap.get("total"));
					fail += Long.valueOf(statMap.get("fail"));
					undo += Long.valueOf(statMap.get("undo"));
				}
			}
			sonCityList = null;
			success = total - fail - undo;
			resultMap.put("total", String.valueOf(total));
			resultMap.put("success", String.valueOf(success));
			resultMap.put("fail", String.valueOf(fail));
			resultMap.put("undo", String.valueOf(undo));
			if (total == 0) {
				resultMap.put("percent", "0%");
			} else {
				resultMap.put("percent", dateFormat.format((success * 1.0 / total)));
			}
			// log.debug(resultMap.toString());
			resultList.add(resultMap);
		}
		// log.debug(resultList.toString());
		nextcityIdList = null;
		return resultList;
	}

	public ArrayList getDevListData(String serviceId, String cityId,
			String exeStatus, long startTime, long endTime) {
		return statDao.queryDevListData(serviceId, cityId, exeStatus,
				startTime, endTime);
	}

	/**
	 * 获取数据库操作DAO
	 * 
	 * @date 2009-5-5
	 * @return void
	 */
	public void setStatDao(StrategyConfigStatDAO dao) {
		this.statDao = dao;
	}
}
