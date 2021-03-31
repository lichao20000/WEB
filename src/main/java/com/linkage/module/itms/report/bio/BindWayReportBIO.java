
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
import com.linkage.module.itms.report.dao.BindWayReportDAO;

/**
 * 考核所有的用户
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class BindWayReportBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BindWayReportBIO.class);
	private BindWayReportDAO bindWayReportDao;

	/**
	 * @return the bindWayReportDao
	 */
	public BindWayReportDAO getBindWayReportDao()
	{
		return bindWayReportDao;
	}

	/**
	 * @param bindWayReportDao
	 *            the bindWayReportDao to set
	 */
	public void setBindWayReportDao(BindWayReportDAO bindWayReportDao)
	{
		this.bindWayReportDao = bindWayReportDao;
	}

	public List<Map> countBindWay(String starttime1, String endtime1, String cityId,
			String userType, String is_active)
	{
		logger.debug("countBindWay({},{},{})", new Object[] { starttime1, endtime1,
				cityId });
		List<Map> list = new ArrayList<Map>();
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		Collections.sort(cityList);
		Map allmap = bindWayReportDao.countOpened(starttime1, endtime1, cityId, null,
				userType, is_active);
		Map macmap = bindWayReportDao.countMac(starttime1, endtime1, cityId, userType);
		List bindwaylist = bindWayReportDao.countBindWay(starttime1, endtime1, cityId,
				userType);
		for (int i = 0; i < cityList.size(); i++)
		{
			String city_id = cityList.get(i);
			ArrayList<String> tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			// 总开户数
			long allopened = 0;
			// MAC比对新建用户
			long macuser = 0;
			// 手工绑定用户
			long handbind = 0;
			// 自助绑定
			long selfbind = 0;
			// MAC比对绑定
			long macbind = 0;
			// 有效绑定数
			long effectivebind = 0;
			// 自助绑定MAC认证
			long selfmac = 0;
			// 手工绑定MAC认证
			long handmac = 0;
			Map<String, Object> tmap = new HashMap();
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++)
			{
				String cityId2 = tlist.get(j);
				allopened = allopened + StringUtil.getLongValue(allmap.get(cityId2));
				macuser = macuser + StringUtil.getLongValue(macmap.get(cityId2));
				for (int k = 0; k < bindwaylist.size(); k++)
				{
					Map rmap = (Map) bindwaylist.get(k);
					if (cityId2.equals(rmap.get("city_id")))
					{
						if ("1".equals(StringUtil.getStringValue(rmap.get("userline"))))
						{
							handbind = handbind
									+ StringUtil.getLongValue(rmap.get("total"));
							if ("1".equals(StringUtil.getStringValue(rmap
									.get("is_chk_bind"))))
							{
								handmac = handmac
										+ StringUtil.getLongValue(rmap.get("total"));
							}
						}
						if ("2".equals(StringUtil.getStringValue(rmap.get("userline"))))
						{
							selfbind = selfbind
									+ StringUtil.getLongValue(rmap.get("total"));
							if ("1".equals(StringUtil.getStringValue(rmap
									.get("is_chk_bind"))))
							{
								selfmac = selfmac
										+ StringUtil.getLongValue(rmap.get("total"));
							}
						}
						if ("4".equals(StringUtil.getStringValue(rmap.get("userline"))))
						{
							macbind = macbind
									+ StringUtil.getLongValue(rmap.get("total"));
						}
					}
				}
			}
			effectivebind = handbind + selfbind + macbind;
			tmap.put("allopened", allopened);
			tmap.put("macuser", macuser);
			tmap.put("handbind", handbind);
			tmap.put("selfbind", selfbind);
			tmap.put("macbind", macbind);
			tmap.put("effectivebind", effectivebind);
			tmap.put("handmac", handmac);
			tmap.put("selfmac", selfmac);
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		return list;
	}

	/**
	 * 获取用户列表
	 * 
	 * @author wangsenbo
	 * @date Mar 2, 2010
	 * @param curPage_splitPage
	 *            当前页数
	 * @param num_splitPage
	 *            每页记录数
	 * @return List<Map>
	 */
	public List<Map> getHgwList(String starttime1, String endtime1, String cityId,
			String userTypeId, String userline, String isChkBind, int curPage_splitPage,
			int num_splitPage, String access_style_id, String userType, String is_active)
	{
		return bindWayReportDao.getHgwList(starttime1, endtime1, cityId, userTypeId,
				userline, isChkBind, curPage_splitPage, num_splitPage, access_style_id,
				userType, is_active);
	}

	/**
	 * 统计用户列表用户数
	 * 
	 * @author wangsenbo
	 * @date Mar 2, 2010
	 * @param curPage_splitPage
	 *            当前页数
	 * @param num_splitPage
	 *            每页记录数
	 * @return int
	 */
	public int getHgwCount(String starttime1, String endtime1, String cityId,
			String userTypeId, String userline, String isChkBind, int curPage_splitPage,
			int num_splitPage, String access_style_id, String usertype, String is_active)
	{
		return bindWayReportDao.getHgwCount(starttime1, endtime1, cityId, userTypeId,
				userline, isChkBind, curPage_splitPage, num_splitPage, access_style_id,
				usertype, is_active);
	}

	/**
	 * 用户信息导出
	 * 
	 * @author wangsenbo
	 * @date Mar 2, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getHgwExcel(String starttime1, String endtime1, String cityId,
			String userTypeId, String userline, String isChkBind, String access_style_id,
			String usertype, String is_active)
	{
		return bindWayReportDao.getHgwExcel(starttime1, endtime1, cityId, userTypeId,
				userline, isChkBind, access_style_id, usertype, is_active);
	}

	/**
	 * 所有绑定方式统计
	 * 
	 * @author wangsenbo
	 * @date May 18, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> countAllBindWay(String starttime1, String endtime1, String cityId,
			String usertype, String is_active)
	{
		logger.debug("countAllBindWay({},{},{})", new Object[] { starttime1, endtime1,
				cityId });
		List<Map> list = new ArrayList<Map>();
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		Collections.sort(cityList);
		// 已开户用户
		Map allmap = bindWayReportDao.countOpened(starttime1, endtime1, cityId, null,
				usertype, is_active);
		// BSS同步用户
		Map synMap = bindWayReportDao.countSyn(starttime1, endtime1, cityId, null,
				usertype, is_active);
		// 所有绑定方式
		List<Map<String, String>> bindWayList = bindWayReportDao.getAllBindWay();
		//
		List<Map<String, String>> bindwaylist = bindWayReportDao.countAllBindWay(
				starttime1, endtime1, cityId, usertype, is_active);
		// 处理得到的统计数据
		Map<String, Map<String, String>> clmap = new HashMap<String, Map<String, String>>();
		for (Map<String, String> smap : bindWayList)
		{
			Map<String, String> map = new HashMap<String, String>();
			for (Map<String, String> cmap : bindwaylist)
			{
				if (StringUtil.getStringValue(cmap.get("userline")).equals(
						StringUtil.getStringValue(smap.get("userline"))))
				{
					map.put(cmap.get("city_id"),
							StringUtil.getStringValue(cmap.get("total")));
				}
			}
			clmap.put(StringUtil.getStringValue(smap.get("userline")), map);
		}
		for (int i = 0; i < cityList.size(); i++)
		{
			String city_id = cityList.get(i);
			ArrayList<String> tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			// 总开户数
			long allopened = 0;
			// BSS同步用户数
			long synCount = 0l; // added 2012年5月4日 15:16:26
			// 已绑定用户数
			long bindnum = 0;
			// 设备物理SN自动绑定、桥接帐号自动绑定、逻辑SN自动绑定、路由帐号自动绑定四种绑定方式汇总
			long totalAuto = 0;
			Map<String, Object> tmap = new HashMap();
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			// 总开户数
			for (int j = 0; j < tlist.size(); j++)
			{
				String cityId2 = tlist.get(j);
				allopened = allopened + StringUtil.getLongValue(allmap.get(cityId2));
				synCount += StringUtil.getLongValue(synMap.get(cityId2)); // added
																			// 2012年5月4日
																			// 15:16:47
			}
			tmap.put("allopened", allopened);
			tmap.put("synCount", synCount);
			for (int j = 0; j < bindWayList.size(); j++)
			{
				long total = 0;
				Map<String, String> smap = (Map) bindWayList.get(j);
				for (int k = 0; k < tlist.size(); k++)
				{
					String cityId2 = tlist.get(k);
					total = total
							+ StringUtil.getLongValue(clmap.get(
									StringUtil.getStringValue(smap.get("userline"))).get(
									cityId2));
					String userLineStr = StringUtil.getStringValue(smap.get("userline"));
					// -- 自动绑定率 --add by zhangchy 2011-10-25 ----begin--
					// (设备物理SN自动绑定 + 桥接帐号自动绑定 + 逻辑SN自动绑定 + 路由帐号自动绑定) / 总开户数
					if ("3".equals(userLineStr) || "5".equals(userLineStr)
							|| "6".equals(userLineStr) || "7".equals(userLineStr))
					{
						totalAuto = totalAuto
								+ StringUtil.getLongValue(clmap.get(
										StringUtil.getStringValue(smap.get("userline")))
										.get(cityId2));
					}
					// -- 自动绑定率 --add by zhangchy 2011-10-25 ----end--
				}
				tmap.put(smap.get("type_name"), total);
				bindnum = bindnum + total;
			}
			tmap.put("bindnum", bindnum);
			tmap.put("percent", percent(totalAuto, allopened)); // 自动绑定率 add by zhangchy
																// 2011-10-25
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		return list;
	}

	public List<Map<String, String>> getAllBindWay()
	{
		return bindWayReportDao.getAllBindWay();
	}

	public String percent(long p1, long p2)
	{
		double p3;
		if (p2 == 0)
		{
			return "N/A";
		}
		else
		{
			p3 = (double) p1 / p2;
		}
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p3);
		return str;
	}
}
