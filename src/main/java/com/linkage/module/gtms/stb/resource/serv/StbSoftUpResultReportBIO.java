package com.linkage.module.gtms.stb.resource.serv;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.stb.resource.dao.StbSoftUpResultReportDAO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 考核所有的用户
 * 
 * @author 王森博
 */
@SuppressWarnings("rawtypes")
public class StbSoftUpResultReportBIO {

	private StbSoftUpResultReportDAO dao;

	/**
	 * 软件升级结果统计
	 * 
	 */
	public List<Map> countSoftUpResult(String starttime1, String endtime1, String cityId, String isSoftUp,
			String vendorId,String modelId) {
		List<Map> list = new ArrayList<Map>();
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		Collections.sort(cityList);

		// 升级结果
		List resultlist = dao.countResult(starttime1,endtime1,cityId,isSoftUp,vendorId,modelId);

		for (int i = 0; i < cityList.size(); i++) {
			String city_id = cityList.get(i);
			ArrayList<String> tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			// 总配置数
			long allup = 0;
			// 成功数
			long successnum = 0;
			// 失败（需重配）数
			long failnum = 0;
			// 未做数
			long noupnum = 0;
			// 失败（下次上线再做）
			long nextnum = 0;
			Map<String, Object> tmap = new HashMap<String, Object>();
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++) {
				String cityId2 = tlist.get(j);
				// allup = allup +
				for (int k = 0; k < resultlist.size(); k++) {
					Map rmap = (Map) resultlist.get(k);
					if (cityId2.equals(rmap.get("city_id"))) {
						if ("100".equals(StringUtil.getStringValue(rmap.get("status")))) {
							if ("1".equals(StringUtil.getStringValue(rmap.get("result_id")))) {
								// 成功数
								successnum = successnum + StringUtil.getLongValue(rmap.get("total"));
							} else {
								// 失败（需重配）数
								failnum = failnum + StringUtil.getLongValue(rmap.get("total"));
							}
						} else if ("0".equals(StringUtil.getStringValue(rmap.get("status")))) {
							if ("0".equals(StringUtil.getStringValue(rmap.get("result_id")))) {
								// 未做数
								noupnum = noupnum + StringUtil.getLongValue(rmap.get("total"));
							} else {
								if (!"1".equals(StringUtil.getStringValue(rmap.get("result_id")))) {
									// 失败（下次上线再做）
									nextnum = nextnum + StringUtil.getLongValue(rmap.get("total"));
								}
							}
						} else {
							allup = allup + StringUtil.getLongValue(rmap.get("total"));
						}
					}
				}
			}
			allup = allup + successnum + noupnum + nextnum + failnum;
			tmap.put("allup", allup);
			tmap.put("successnum", successnum);
			tmap.put("noupnum", noupnum);
			tmap.put("nextnum", nextnum);
			tmap.put("failnum", failnum);
			tmap.put("percent", percent(successnum, allup));
			tmap.put("vendorId",vendorId);
			tmap.put("modelId",modelId);
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		return list;
	}

	public String percent(long p1, long p2) {
		double p3;
		if (p2 == 0) {
			if (LipossGlobals.inArea(Global.NXDX)) {
				return "0";
			} else {
				return "N/A";
			}
		} else {
			p3 = (double) p1 / p2;
		}
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p3);
		return str;
	}

	/**
	 * @return the dao
	 */
	public StbSoftUpResultReportDAO getDao() {
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(StbSoftUpResultReportDAO dao) {
		this.dao = dao;
	}

	public List<Map> getDevList(String starttime1,
			String endtime1, String cityId, String status, String resultId, int curPage_splitPage, int num_splitPage,
			String isSoftUp,String vendorId,String modelId) {
		return dao.getDevList(starttime1, endtime1, cityId, status,
				resultId, curPage_splitPage, num_splitPage, isSoftUp,vendorId,modelId);
	}

	public int getDevCount(String starttime1, String endtime1,
			String cityId, String status, String resultId,
			int curPage_splitPage, int num_splitPage, String isSoftUp,String vendorId,String modelId) {
		return dao.getDevCount(starttime1, endtime1, cityId, status,
				resultId, curPage_splitPage, num_splitPage, isSoftUp,vendorId,modelId);
	}

	public List<Map> getDevExcel(String starttime1,
			String endtime1, String cityId, String status, String resultId, String isSoftUp,String vendorId,String modelId) {
		return dao.getDevExcel(starttime1, endtime1, cityId, status,
				resultId, isSoftUp,vendorId,modelId);
	}

	/**
	 * 查询下级属地，不包含自己
	 */
	public String getCity(String cityId)
	{
		List<Map<String,String>> list = CityDAO.getNextCityListByCityPidCore(cityId);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++)
		{
			Map<String,String> map = list.get(i);
			if (i > 0){
				bf.append("#");
			}
			bf.append(map.get("city_id")+"$"+map.get("city_name"));
		}
		return bf.toString();
	}

	
}
