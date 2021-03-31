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
import com.linkage.module.itms.report.dao.NetAccSpeedDAO;

public class NetAccSpeedBIO {
	/** 日志记录 */
	private static Logger logger = LoggerFactory.getLogger(NetAccSpeedBIO.class);
	
	private NetAccSpeedDAO dao;
	public List<Map> countAll(String cityId, String starttime1, String endtime1) {

		logger.debug("countAll({},{},{})", new Object[]{cityId, starttime1, endtime1});
		
		List<Map> list = new ArrayList<Map>();
		
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(包含自己)
		ArrayList<Map> cityList1  = (ArrayList<Map>)dao.getNextCityIdsByCityPid(cityId);
		ArrayList<String> cityList = new ArrayList<String>();
		for (int i = 0; i < cityList1.size(); i++)
		{
			cityList.add(StringUtil.getStringValue(cityList1.get(i).get("city_id")));
		}
		
		Collections.sort(cityList);
		
		/** 全量测速数据 */
		Map<String, String> allrealSpeedMap =  dao.getAllrealSpeedList(cityId, starttime1, endtime1);
		
		/** 达标用户签约速率 */
		Map<String, String> succuserspeedMap = dao.succuserspeedMap(cityId, starttime1, endtime1);
		
		/** 未达标用户签约速率  */
		Map<String, String> falueuserspeedMap = dao.falueuserspeedMap(cityId, starttime1, endtime1);
		
		for (int i = 0; i < cityList.size(); i++) {
			
			String city_id = cityList.get(i);
			ArrayList<String> tList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			
			// 用于返回到ACT
			Map<String, Object> tmap = new HashMap<String, Object>();
			
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			
			//总数
			long allSpeedtNum = 0;
			//成功
			long succSpeedNum = 0;
			//失败
			long failSpeedNum = 0;
			
			for (int k = 0; k < tList.size(); k++) {
				String cityId2 = tList.get(k);
				allSpeedtNum = allSpeedtNum + StringUtil.getLongValue(allrealSpeedMap.get(cityId2));
				succSpeedNum = succSpeedNum + StringUtil.getLongValue(succuserspeedMap.get(cityId2));
				failSpeedNum = failSpeedNum + StringUtil.getLongValue(falueuserspeedMap.get(cityId2));
			}
			tmap.put("allSpeedtNum", allSpeedtNum);
			tmap.put("succSpeedNum", succSpeedNum);
			tmap.put("failSpeedNum", failSpeedNum);
			
			tmap.put("percent", percent(succSpeedNum, allSpeedtNum));
			
			list.add(tmap);
			tList = null;
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
	public NetAccSpeedDAO getDao() {
		return dao;
	}
	public void setDao(NetAccSpeedDAO dao) {
		this.dao = dao;
	}
	public List<Map> getList(String flag, String cityId, String starttime1,
			String endtime1, int curPage_splitPage, int num_splitPage) {
		return dao.getList(flag,cityId,starttime1,endtime1,curPage_splitPage,num_splitPage);
	}
	public int getListCount(String bindFlag, String cityId, String starttime1,
			String endtime1, int curPage_splitPage, int num_splitPage) {
		return dao.getListCount(bindFlag, cityId, starttime1, endtime1, curPage_splitPage, num_splitPage);
	}
	public List<Map> getdetailExcel(String flag, String cityId,
			String starttime1, String endtime1) {
		return dao.getdetailExcel(flag,cityId,starttime1,endtime1);
	}
	
}
