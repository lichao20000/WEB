package com.linkage.module.gtms.report.serv;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.report.dao.FtthUserBindDao;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 
 * @author Administrator(工号) Tel:
 * @version 1.0
 * @since Apr 25, 2012 3:44:12 PM
 * @category com.linkage.module.gtms.report.serv
 * @copyright 南京联创科技 网管科技部
 *
 */
public class FtthUserBindServImp implements FtthUserBindServ{
	
	private static Logger logger = LoggerFactory.getLogger(FtthUserBindServImp.class);
	
	private FtthUserBindDao dao;
	
	
	/**
	 * 统计首页
	 * 
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<Map> countAll(String cityId, String starttime1, String endtime1,String isFiber){
		
		logger.debug("FtthUserBindServImp==>countAll({},{},{})", new Object[] { cityId,
				starttime1, endtime1 });
		
		List<Map> list = new ArrayList<Map>();
		
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		
		// 根据属地cityId获取下一层属地ID(包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		
		Collections.sort(cityList);
		
		// 所有工单用户数（全量FTTH用户）
		Map<String, String> allFtthUserMap = dao.getAllFtthUser(cityId, starttime1,
				endtime1,isFiber);
		
		// 已绑定FTTH用户数
		Map<String, String> bindUserMap = dao.getBindFtthUser(cityId, starttime1,
				endtime1,isFiber);
		
		for (int i = 0; i < cityList.size(); i++) {
			
			String city_id = cityList.get(i);
			ArrayList<String> subList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			
			Map<String, Object> tmap = new HashMap<String, Object>();
			
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			
			/** 全部工单用户数*/
			long allBindUserNum = 0;
			/** 已绑定用户数 */
			long bindUserNum = 0;
			
			for (int j = 0; j < subList.size(); j++) {
				String subCityId = subList.get(j);
				allBindUserNum = allBindUserNum
						+ StringUtil.getLongValue(allFtthUserMap.get(subCityId));
				bindUserNum = bindUserNum
						+ StringUtil.getLongValue(bindUserMap.get(subCityId));
			}
			tmap.put("allBingNum", allBindUserNum);
			tmap.put("bindNum", bindUserNum);
			tmap.put("percent", percent(bindUserNum, allBindUserNum));
			
			list.add(tmap);
			subList = null;
		}
		cityMap = null;
		cityList = null;
		return list;
	}

	

	/**
	 * 用户列表
	 * 
	 * @param bindFlag
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> getUserList(String bindFlag, String cityId, String starttime1,
			String endtime1, int curPage_splitPage, int num_splitPage,String isFiber) {
		return dao.getUserList(bindFlag, cityId, starttime1, endtime1, curPage_splitPage,
				num_splitPage,isFiber);
	}
	
	
	/**
	 * 用于分页
	 * 
	 * @param bindFlag
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int getUserCount(String bindFlag, String cityId, String starttime1,
			String endtime1, int curPage_splitPage, int num_splitPage,String isFiber) {
		return dao.getUserCount(bindFlag, cityId, starttime1, endtime1,
				curPage_splitPage, num_splitPage,isFiber);
	}
	
	
	/**
	 * 导出Excel
	 * 
	 * @param bindFlag
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */
	public List<Map> getUserExcel(String bindFlag, String cityId, String starttime1,
			String endtime1,String isFiber) {
		return dao.getUserExcel(bindFlag, cityId, starttime1, endtime1,isFiber);
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
	
	
	public FtthUserBindDao getDao() {
		return dao;
	}

	
	public void setDao(FtthUserBindDao dao) {
		this.dao = dao;
	}
}
