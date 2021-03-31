package com.linkage.module.itms.report.bio;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.dao.DelWanConnReportDAO;

public class DelWanConnReportBIO {
	private static Logger logger = LoggerFactory.getLogger(DelWanConnReportBIO.class);
	private DelWanConnReportDAO dao;
	/**
	 * 根据时间=地市统计结果
	 * @param starttime1
	 * @param endtime1
	 * @param cityId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> countDelWanConnResult(String starttime1, String endtime1,String cityId) {
		
		List<Map> list = new ArrayList<Map>();
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		Collections.sort(cityList);
		// 结果
		List resultlist = dao.countDelWanConnResult(starttime1,endtime1,cityId);
		
		
		for (int i = 0; i < cityList.size(); i++)
		{
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
			Map<String, Object> tmap = new HashMap<String, Object>();
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++)
			{
				String cityId2 = tlist.get(j);
				//allup = allup + StringUtil.getLongValue(allmap.get(cityId2));
				for (int k = 0; k < resultlist.size(); k++)
				{
					Map rmap = (Map) resultlist.get(k);
					if (cityId2.equals(rmap.get("city_id")))
					{
						if ("1".equals(StringUtil.getStringValue(rmap.get("status"))))
						{
							// 成功数
							successnum += StringUtil.getLongValue(rmap.get("total"));
						}
						else if ("0".equals(StringUtil.getStringValue(rmap.get("status"))))
						{
							// 未做数
							noupnum += StringUtil.getLongValue(rmap.get("total"));
						}
						else
						{
							failnum += StringUtil.getLongValue(rmap.get("total"));
						}
					}
				}
			}
			
			allup = allup + successnum + noupnum  + failnum;
			tmap.put("allup", allup);
			tmap.put("successnum", successnum);
			tmap.put("noupnum", noupnum);
			tmap.put("failnum", failnum);
			tmap.put("percent", percent(successnum, allup));
			list.add(tmap);
			tlist = null;
		}
	
		cityMap = null;
		cityList = null;
		
		logger.debug("countSoftUpResult list:[{}]",list);
		return list;
		
	}
	
	
	
	public String percent(long p1, long p2)
	{
		if (p2 == 0)
		{
			if(LipossGlobals.inArea(Global.NXDX)){
				return "0";
			}else {
				return "N/A";
			}
		}
		
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		return nf.format((double) p1 / p2);
	}



	@SuppressWarnings("rawtypes")
	public List<Map> getDevList(String starttime1, String endtime1,
			String cityId, String status, int curPage_splitPage,
			int num_splitPage) {
		return dao.getDevList(starttime1,endtime1,cityId,status,curPage_splitPage,num_splitPage);
	}



	public int getDevCount(String starttime1, String endtime1, String cityId,
			String status, int curPage_splitPage, int num_splitPage) {
		return dao.getDevCount(starttime1,endtime1,cityId,status,curPage_splitPage,num_splitPage);
	}



	@SuppressWarnings("rawtypes")
	public List<Map> getDevExcel(String starttime1, String endtime1,
			String cityId, String status) {
		return dao.getDevExcel(starttime1,endtime1,cityId,status);
	}



	public DelWanConnReportDAO getDao() {
		return dao;
	}



	public void setDao(DelWanConnReportDAO dao) {
		this.dao = dao;
	}
	
	
}
