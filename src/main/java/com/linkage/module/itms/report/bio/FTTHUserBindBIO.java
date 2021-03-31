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
import com.linkage.module.itms.report.dao.FTTHUserBindDAO;



public class FTTHUserBindBIO {
	
	/** 日志记录 */
	private static Logger logger = LoggerFactory.getLogger(FTTHUserBindBIO.class);
	
	private FTTHUserBindDAO ftthUserBindDAO;

	public List<Map> countAll(String cityId, String starttime1, String endtime1) {
		
		logger.debug("countAll({},{},{})", new Object[]{cityId, starttime1, endtime1});
		
		List<Map> list = new ArrayList<Map>();
		
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		
		// 根据属地cityId获取下一层属地ID(包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		
		Collections.sort(cityList);
		
		/** 全量FTTH用户 */
		Map<String, String> allBindUserMap =  ftthUserBindDAO.getAllBindUser(cityId, starttime1, endtime1);
		
		/** 已绑定用户 */
		Map<String, String> bindUserMap = ftthUserBindDAO.getBindUser(cityId, starttime1, endtime1);
		
		/** 未绑定用户 */
		Map<String, String> notBindUserMap = ftthUserBindDAO.getNotBindUser(cityId, starttime1, endtime1);
		
		for (int i = 0; i < cityList.size(); i++) {
			
			String city_id = cityList.get(i);
			
			ArrayList<String> tList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			
			// 用于返回到ACT
			Map<String, Object> tmap = new HashMap<String, Object>();
			
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			
			/** 全量FTTH用户*/
			long allBindUserNum = 0;
			/** 已绑定用户 */
			long bindUserNum = 0;
			/** 未绑定用户 */
			long notBindUserNum = 0;
			
			for (int k = 0; k < tList.size(); k++) {
				String cityId2 = tList.get(k);
				allBindUserNum = allBindUserNum + StringUtil.getLongValue(allBindUserMap.get(cityId2));
				bindUserNum = bindUserNum + StringUtil.getLongValue(bindUserMap.get(cityId2));
				notBindUserNum = notBindUserNum + StringUtil.getLongValue(notBindUserMap.get(cityId2));
			}
			tmap.put("allBingNum", allBindUserNum);
			tmap.put("bindNum", bindUserNum);
			tmap.put("notBindNum", notBindUserNum);
			
			tmap.put("percent", percent(bindUserNum, allBindUserNum));
			
			list.add(tmap);
			tList = null;
		}
		cityMap = null;
		cityList = null;
		return list;
	}
	
	
	/**
	 * 获取用户列表
	 * @param bindFlag
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> getUserList(String bindFlag, String cityId, String starttime1, String endtime1, int curPage_splitPage, int num_splitPage) {
		return ftthUserBindDAO.getUserList(bindFlag, cityId, starttime1, endtime1, curPage_splitPage, num_splitPage);
	}
	
	
	/**
	 * 获取用户的记录数
	 * @param bindFlag
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int getUserCount(String bindFlag, String cityId, String starttime1, String endtime1, int curPage_splitPage, int num_splitPage) {
		return ftthUserBindDAO.getUserCount(bindFlag, cityId, starttime1, endtime1, curPage_splitPage, num_splitPage);
	}
	
	
	public List<Map> getUserExcel(String bindFlag, String cityId, String starttime1, String endtime1) {
		return ftthUserBindDAO.getUserExcel(bindFlag, cityId, starttime1, endtime1);
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

	public FTTHUserBindDAO getFtthUserBindDAO() {
		return ftthUserBindDAO;
	}

	public void setFtthUserBindDAO(FTTHUserBindDAO ftthUserBindDAO) {
		this.ftthUserBindDAO = ftthUserBindDAO;
	}
	
}
