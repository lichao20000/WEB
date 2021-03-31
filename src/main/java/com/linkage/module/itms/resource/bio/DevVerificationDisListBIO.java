
package com.linkage.module.itms.resource.bio;

import java.util.List;
import java.util.Map;


import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.dao.DevVerificationDisListDAO;

/**
 * 
 * @author 岩 (Ailk No.)
 * @version 1.0
 * @since 2016-4-18
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class DevVerificationDisListBIO
{

	/** dao */
	private DevVerificationDisListDAO dao;


	/**
	 * 查询列表
	 * @author 岩 
	 * @date 2016-4-21
	 * @param city_id
	 * @param starttime
	 * @param endtime
	 * @param is_match
	 * @param use_type
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> queryDevVerification(String city_id, String starttime,
			String endtime, String is_match, String use_type,
			int curPage_splitPage, int num_splitPage)
	{
		return dao.queryDevVerification(city_id, starttime, endtime,
				is_match, use_type, curPage_splitPage, num_splitPage);
	}

	/**
	 * 导出excel
	 * @author 岩 
	 * @date 2016-4-21
	 * @param city_id
	 * @param starttime
	 * @param endtime
	 * @param is_match
	 * @param use_type
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> parseExcel(String city_id, String starttime,
			String endtime, String is_match, String use_type)
	{
		return dao.parseExcel(city_id, starttime, endtime, is_match, use_type);
	}

	
	/**
	 * 获取总数
	 * @author 岩 
	 * @date 2016-4-21
	 * @param city_id
	 * @param starttime
	 * @param endtime
	 * @param is_match
	 * @param use_type
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int countQueryDevVerification(String city_id, String starttime,
			String endtime, String is_match, String use_type,
			int curPage_splitPage, int num_splitPage)
	{
		return dao.countQueryDevVerification(city_id, starttime, endtime,
				is_match, use_type, curPage_splitPage, num_splitPage);
	}


	/**
	 * 获取二级地市对应map
	 */
	public static Map<String, String> getSencondCityIdCityNameMap()
	{
		CityDAO cityAct = new CityDAO();
		// 取所有属地的ID
		return cityAct.getSencondCityIdCityNameMap();
	}



	public void setDao(DevVerificationDisListDAO dao)
	{
		this.dao = dao;
	}

	public int getQueryCount()
	{
		return dao.getQueryCount();
	}
}
