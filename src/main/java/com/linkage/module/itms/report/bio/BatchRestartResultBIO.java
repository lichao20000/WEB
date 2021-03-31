package com.linkage.module.itms.report.bio;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.dao.BatchRestartResultDAO;

/**
 * @author songxq
 * @version 1.0
 * @since 2019-8-6 上午10:32:42
 * @category 
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BatchRestartResultBIO
{

	private static Logger logger = LoggerFactory.getLogger(BatchRestartResultBIO.class);
	
	BatchRestartResultDAO dao = null;
	
	public List<Map> countAll(String cityId, String starttime, String endtime)
	{
		// TODO Auto-generated method stub
		logger.debug("countAll({},{},{})", new Object[]{cityId, starttime, endtime});
		
		List<Map> list = new ArrayList<Map>();
		
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		
		// 根据属地cityId获取下一层属地ID(包含自己)
		//ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		ArrayList<Map> cityList1 = (ArrayList<Map>) dao.getNextCityIdsByCityPid(cityId);
		
		ArrayList<String> cityList = new ArrayList<String>();
		for (int i = 0; i < cityList1.size(); i++)
		{
			cityList.add(StringUtil.getStringValue(cityList1.get(i).get("city_id")));
		}
		
		Collections.sort(cityList);
		
		//总数
		Map<String, String> allRestartMap = dao.getAllRestart(cityId, starttime, endtime);
		//成功
		Map<String, String> succRestartMap = dao.getSuccRestart(cityId, starttime, endtime);
		//失败
		Map<String, String> failRestartMap = dao.getFailRestart(cityId, starttime, endtime);
		
		for (int i = 0; i < cityList.size(); i++) {
			
			String city_id = cityList.get(i);
			ArrayList<String> tList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			
			// 用于返回到ACT
			Map<String, Object> tmap = new HashMap<String, Object>();
			
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			
			//总数
			long allRestartNum = 0;
			//成功
			long succRestartNum = 0;
			//失败
			long failRestartNum = 0;
			
			for (int k = 0; k < tList.size(); k++) {
				String cityId2 = tList.get(k);
				allRestartNum = allRestartNum + StringUtil.getLongValue(allRestartMap.get(cityId2));
				succRestartNum = succRestartNum + StringUtil.getLongValue(succRestartMap.get(cityId2));
				failRestartNum = failRestartNum + StringUtil.getLongValue(failRestartMap.get(cityId2));
			}
			tmap.put("allRestartNum", allRestartNum);
			tmap.put("succRestartNum", succRestartNum);
			tmap.put("failRestartNum", failRestartNum);
			tmap.put("noRestartNum", allRestartNum-succRestartNum-failRestartNum);
			
			tmap.put("percent", percent(succRestartNum, allRestartNum));
			
			list.add(tmap);
			tList = null;
		}
		cityMap = null;
		cityList = null;
		return list;
	}
	
	public List<Map> getDetail(String gw_type, String cityId, String restartStatus, int curPage_splitPage,
			int num_splitPage,String starttime,String endtime)
	{
		logger.debug("dao==>getDetail({},{},{},{},{},{})", new Object[] { cityId,
				curPage_splitPage, num_splitPage });
		return dao.getDetail(gw_type, cityId, restartStatus, curPage_splitPage, num_splitPage,starttime,endtime);
	}
	
	public List<Map> getDetailExcel(String gw_type, String cityId, String restartStatus,String starttime,String endtime)
	{
		logger.debug("dao==>getDetailExcel({})", new Object[] { cityId});
		return dao.getDetailExcel(gw_type, cityId, restartStatus,starttime,endtime);
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

	
	public BatchRestartResultDAO getDao()
	{
		return dao;
	}

	
	public void setDao(BatchRestartResultDAO dao)
	{
		this.dao = dao;
	}

	public int getcount(String gw_type, String cityId, String restartStatus, int curPage_splitPage,
			int num_splitPage,String starttime,String endtime)
	{
		// TODO Auto-generated method stub
		return dao.getCount(gw_type, cityId, restartStatus, curPage_splitPage, num_splitPage,starttime,endtime);
	}
	
	
	
}

