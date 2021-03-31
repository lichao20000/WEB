
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
import com.linkage.module.itms.report.dao.BindExceptionReportDAO;

/**
 * 异常绑定统计
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class BindExceptionReportBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(BindExceptionReportBIO.class);
	private BindExceptionReportDAO bindExceptionReportDao;

	/**
	 * 统计绑定异常
	 * 
	 * @author wangsenbo
	 * @date Mar 3, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> countBindException(String starttime1, String endtime1, String cityId,String gw_type)
	{
		logger.debug("countBindException({},{},{})", new Object[] { starttime1, endtime1,
				cityId });
		List<Map> list = new ArrayList<Map>();
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(不包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(cityId);
		Collections.sort(cityList);
		// 未绑定用户中 IPOSS没有同步到相应用户记录
		Map amap = bindExceptionReportDao.countNoBindUserNoIposs(starttime1, endtime1,
				cityId);
		// 未绑定用户中 IPOSS同步到用户对应的MAC地址在ITMS中不存在
		Map bmap = bindExceptionReportDao.countNoBindUserNoMac(starttime1, endtime1,
				cityId);
		// 未绑定终端中 终端未上报MAC地址
		Map cmap = bindExceptionReportDao.countNoBindDevcieNoMac(starttime1, endtime1,
				cityId,gw_type);
		// 未绑定终端中 终端上报的MAC地址，在IPOSS同步数据中没有相应记录
		Map dmap = bindExceptionReportDao.countNoBindDeviceNoIposs(starttime1, endtime1,
				cityId,gw_type);
		// 未绑定用户中 IPOSS没有同步到相应用户记录
		long nobindUserNoIposs = 0;
		// 未绑定用户中 IPOSS同步到用户对应的MAC地址在ITMS中不存在
		long noBindUserNoMac = 0;
		// 未绑定终端中 终端未上报MAC地址
		long noBindDevcieNoMac = 0;
		// 未绑定终端中 终端上报的MAC地址，在IPOSS同步数据中没有相应记录
		long noBindDeviceNoIposs = 0;
		for (int i = 0; i < cityList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			nobindUserNoIposs = 0;
			noBindUserNoMac = 0;
			noBindDevcieNoMac = 0;
			noBindDeviceNoIposs = 0;
			String city_id = cityList.get(i);
			ArrayList tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++)
			{
				nobindUserNoIposs = nobindUserNoIposs
						+ StringUtil.getLongValue(amap.get(tlist.get(j)));
				noBindUserNoMac = noBindUserNoMac
						+ StringUtil.getLongValue(bmap.get(tlist.get(j)));
				noBindDevcieNoMac = noBindDevcieNoMac
						+ StringUtil.getLongValue(cmap.get(tlist.get(j)));
				noBindDeviceNoIposs = noBindDeviceNoIposs
						+ StringUtil.getLongValue(dmap.get(tlist.get(j)));
			}
			tmap.put("nobindUserNoIposs", nobindUserNoIposs);
			tmap.put("noBindUserNoMac", noBindUserNoMac);
			tmap.put("noBindDevcieNoMac", noBindDevcieNoMac);
			tmap.put("noBindDeviceNoIposs", noBindDeviceNoIposs);
			tmap.put("isAll", "0");
			list.add(tmap);
			tlist = null;
		}
		// 选择的属地
		nobindUserNoIposs = 0;
		noBindUserNoMac = 0;
		noBindDevcieNoMac = 0;
		noBindDeviceNoIposs = 0;
		ArrayList<String> tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
		Map<String, Object> rmap = new HashMap();
		rmap.put("city_id", cityId);
		rmap.put("city_name", cityMap.get(cityId));
		for (int j = 0; j < tlist.size(); j++)
		{
			nobindUserNoIposs = nobindUserNoIposs
					+ StringUtil.getLongValue(amap.get(tlist.get(j)));
			noBindUserNoMac = noBindUserNoMac
					+ StringUtil.getLongValue(bmap.get(tlist.get(j)));
			noBindDevcieNoMac = noBindDevcieNoMac
					+ StringUtil.getLongValue(cmap.get(tlist.get(j)));
			noBindDeviceNoIposs = noBindDeviceNoIposs
					+ StringUtil.getLongValue(dmap.get(tlist.get(j)));
		}
		rmap.put("nobindUserNoIposs", nobindUserNoIposs);
		rmap.put("noBindUserNoMac", noBindUserNoMac);
		rmap.put("noBindDevcieNoMac", noBindDevcieNoMac);
		rmap.put("noBindDeviceNoIposs", noBindDeviceNoIposs);
		rmap.put("isAll", "1");
		list.add(rmap);
		cityMap = null;
		cityList = null;
		tlist = null;
		return list;
	}

	/**
	 * @return the bindExceptionReportDao
	 */
	public BindExceptionReportDAO getBindExceptionReportDao()
	{
		return bindExceptionReportDao;
	}

	/**
	 * @param bindExceptionReportDao
	 *            the bindExceptionReportDao to set
	 */
	public void setBindExceptionReportDao(BindExceptionReportDAO bindExceptionReportDao)
	{
		this.bindExceptionReportDao = bindExceptionReportDao;
	}
}
