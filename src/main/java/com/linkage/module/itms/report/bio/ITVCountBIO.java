
package com.linkage.module.itms.report.bio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.dao.ITVCountDAO;

/**
 * 考核所有的用户
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class ITVCountBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ITVCountBIO.class);
	private ITVCountDAO dao;

	/**
	 * 获取上行方式列表
	 * 
	 * @author wangsenbo
	 * @date Apr 22, 2010
	 * @param
	 * @return List<Map<String,String>>
	 */
	public List<Map<String, String>> getWANAccessTypeList()
	{
		return dao.getWANAccessTypeList();
	}

	/**
	 * 获取终端类型列表
	 * 
	 * @author wangsenbo
	 * @date Apr 22, 2010
	 * @param
	 * @return List<Map<String,String>>
	 */
	public List<Map<String, String>> getDeviceTypeList()
	{
		return dao.getDeviceTypeList();
	}

	/**
	 * iTV业务统计
	 * 
	 * @author wangsenbo
	 * @date Apr 22, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> iTVCount(String starttime1, String endtime1, String cityId,
			String wanAccessTypeId, String forbidNet, String typeId)
	{
		logger.debug("iTVCount({},{},{},{},{},{})", new Object[] { starttime1, endtime1,
				cityId, wanAccessTypeId, forbidNet, typeId });
		List<Map> list = new ArrayList<Map>();
		// 按本地网统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(不包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(cityId);
		Collections.sort(cityList);
		cityList.add(0, cityId);
		Map iTYmap = dao.iTVCount(starttime1, endtime1, cityId, wanAccessTypeId,
				forbidNet, typeId);
		// 用户数
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
				total = total + StringUtil.getLongValue(iTYmap.get(tlist.get(j)));
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
	 * iTV业务统计用户列表
	 * 
	 * @author wangsenbo
	 * @date Apr 22, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getHgwList(String starttime1, String endtime1, String cityId,
			String wanAccessTypeId, String forbidNet, String typeId,
			int curPage_splitPage, int num_splitPage)
	{
		return dao.getHgwList(starttime1, endtime1, cityId, wanAccessTypeId, forbidNet,
				typeId, curPage_splitPage, num_splitPage);
	}

	/**
	 * iTV业务统计用户列表计数
	 * 
	 * @author wangsenbo
	 * @param num_splitPage
	 * @param curPage_splitPage
	 * @date Apr 22, 2010
	 * @param
	 * @return int
	 */
	public int getHgwCount(String starttime1, String endtime1, String cityId,
			String wanAccessTypeId, String forbidNet, String typeId,
			int curPage_splitPage, int num_splitPage)
	{
		return dao.getHgwCount(starttime1, endtime1, cityId, wanAccessTypeId, forbidNet,
				typeId, curPage_splitPage, num_splitPage);
	}

	/**
	 * iTV业务统计用户列表Excel
	 * 
	 * @author wangsenbo
	 * @date Apr 22, 2010
	 * @param
	 * @return String
	 */
	public List<Map> getHgwExcel(String starttime1, String endtime1, String cityId,
			String wanAccessTypeId, String forbidNet, String typeId)
	{
		return dao.getHgwExcel(starttime1, endtime1, cityId, wanAccessTypeId, forbidNet,
				typeId);
	}

	/**
	 * @return the dao
	 */
	public ITVCountDAO getDao()
	{
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(ITVCountDAO dao)
	{
		this.dao = dao;
	}
}
