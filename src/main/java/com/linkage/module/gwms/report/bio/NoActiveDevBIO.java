
package com.linkage.module.gwms.report.bio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.report.dao.NoActiveDevDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 考核所有的用户
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class NoActiveDevBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(NoActiveDevBIO.class);
	private NoActiveDevDAO dao;

	/**
	 * 统计不活跃设备
	 * 
	 * @author wangsenbo
	 * @date Apr 21, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> count(String activetime1, String cityId, String gw_type)
	{
		logger.debug("count({},{})", new Object[] { activetime1, cityId });
		List<Map> list = new ArrayList<Map>();
		// 按本地网统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(不包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(cityId);
		Collections.sort(cityList);
		cityList.add(0, cityId);
		Map noactivemap = dao.count(activetime1, cityId, gw_type);
		// 不活跃设备数
		long total = 0;
		ArrayList<String> tlist = null;
		for (int i = 0; i < cityList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			total = 0;
			String city_id = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++)
			{
				total = total + StringUtil.getLongValue(noactivemap.get(tlist.get(j)));
			}
			tmap.put("total", total);
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		tlist = null;
		return list;
	}

	/**
	 * 不活跃设备列表
	 * 
	 * @author wangsenbo
	 * @date Apr 21, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getDevList(String activetime1, String cityId, int curPage_splitPage,
			int num_splitPage,String gwType)
	{
		return dao.getDevList(activetime1, cityId, curPage_splitPage, num_splitPage,gwType);
	}

	/**
	 * 不活跃设备列表记录数
	 * 
	 * @author wangsenbo
	 * @date Apr 21, 2010
	 * @param
	 * @return int
	 */
	public int getDevCount(String activetime1, String cityId, int curPage_splitPage,
			int num_splitPage ,String gwType)
	{
		return dao.getDevCount(activetime1, cityId, curPage_splitPage, num_splitPage, gwType);
	}

	/**
	 * 不活跃设备列表导出excel
	 * 
	 * @author wangsenbo
	 * @date Apr 21, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getDevExcel(String activetime1, String cityId, String gwType)
	{
		// TODO Auto-generated method stub
		return dao.getDevExcel(activetime1, cityId ,gwType);
	}

	/**
	 * @return the dao
	 */
	public NoActiveDevDAO getDao()
	{
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(NoActiveDevDAO dao)
	{
		this.dao = dao;
	}
}
