
package com.linkage.module.gwms.resource.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.resource.dao.IptvVlanManageDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings("unchecked")
public class IptvVlanManageBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(IptvVlanManageBIO.class);
	private IptvVlanManageDAO iptvVlanManageDao;

	/**
	 * 获取所有本地网的VLAN标准值
	 * 
	 * @author wangsenbo
	 * @date Jan 6, 2010
	 * @return List
	 */
	public List getCityVlanList()
	{
		logger.debug("getCityVlanList()");
		List list = iptvVlanManageDao.getCityVlan();
		List cityVlanList = null;
		if (list.size() < 1)
		{
			logger.warn("cityVlanList==null");
		}
		else
		{
			Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
			cityVlanList = new ArrayList();
			for (int i = 0; i < list.size(); i++)
			{
				Map tmap = (Map) list.get(i);
				Map<String, String> rmap = new HashMap();
				rmap.put("city_id", StringUtil.getStringValue(tmap.get("city_id")));
				rmap.put("max_vlanid", StringUtil.getStringValue(tmap.get("max_vlanid")));
				rmap.put("min_vlanid", StringUtil.getStringValue(tmap.get("min_vlanid")));
				rmap.put("city_name", StringUtil.getStringValue(cityMap.get(tmap
						.get("city_id"))));
				rmap.put("bas_ip", StringUtil.getStringValue(tmap.get("bas_ip")));
				// updatetime转换
				long updatetime = StringUtil.getLongValue(tmap.get("updatetime"));
				DateTimeUtil dt = new DateTimeUtil(updatetime * 1000);
				rmap.put("updatetime", StringUtil.getStringValue(dt.getDate()));
				rmap.put("id", StringUtil.getStringValue(tmap.get("id")));
				cityVlanList.add(rmap);
			}
		}
		return cityVlanList;
	}

	/**
	 * 获取一个本地网的VLAN标准值
	 * 
	 * @author wangsenbo
	 * @date Jan 6, 2010
	 * @return List
	 */
	public List getCityVlanList(String cid)
	{
		logger.debug("getCityVlanList()");
		List list = iptvVlanManageDao.getCityVlan(cid);
		List cityVlanList = null;
		if (list.size() < 1)
		{
			logger.warn("cityVlanList==null");
		}
		else
		{
			Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
			cityVlanList = new ArrayList();
			for (int i = 0; i < list.size(); i++)
			{
				Map tmap = (Map) list.get(i);
				Map<String, String> rmap = new HashMap();
				rmap.put("city_id", StringUtil.getStringValue(tmap.get("city_id")));
				rmap.put("max_vlanid", StringUtil.getStringValue(tmap.get("max_vlanid")));
				rmap.put("min_vlanid", StringUtil.getStringValue(tmap.get("min_vlanid")));
				rmap.put("city_name", StringUtil.getStringValue(cityMap.get(tmap
						.get("city_id"))));
				rmap.put("bas_ip", StringUtil.getStringValue(tmap.get("bas_ip")));
				// updatetime转换
				long updatetime = StringUtil.getLongValue(tmap.get("updatetime"));
				DateTimeUtil dt = new DateTimeUtil(updatetime * 1000);
				rmap.put("updatetime", StringUtil.getStringValue(dt.getDate()));
				rmap.put("id", StringUtil.getStringValue(tmap.get("id")));
				cityVlanList.add(rmap);
			}
		}
		return cityVlanList;
	}

	/**
	 * @return the iptvVlanManageDao
	 */
	public IptvVlanManageDAO getIptvVlanManageDao()
	{
		return iptvVlanManageDao;
	}

	/**
	 * @param iptvVlanManageDao
	 *            the iptvVlanManageDao to set
	 */
	public void setIptvVlanManageDao(IptvVlanManageDAO iptvVlanManageDao)
	{
		this.iptvVlanManageDao = iptvVlanManageDao;
	}

	/**
	 * @author wangsenbo
	 * @date Jan 6, 2010
	 * @param id
	 *            操作员ID
	 * @param cityId
	 *            属地ID
	 * @param minVlanid
	 *            最小VLAN值
	 * @param maxVlanid
	 *            最大VLAN值
	 * @param basIp
	 *            BAS地址
	 * @param type
	 *            1为增加 2为修改
	 * @param id
	 * @return String
	 */
	public String configVlan(long accoid, String cityId, String minVlanid,
			String maxVlanid, String basIp, String type, String id)
	{
		logger.debug("configVlan({},{},{},{},{},{},{})", new Object[] { accoid, cityId,
				minVlanid, maxVlanid, basIp, type, id });
		String message = "";
		int res = 0;
		DateTimeUtil dt = new DateTimeUtil();
		long updatetime = dt.getLongTime();
		if ("1".equals(type))
		{
			res = iptvVlanManageDao.addVlan(accoid, cityId, minVlanid, maxVlanid, basIp,
					updatetime);
			if (res >= 1)
			{
				logger.warn("新增成功!");
				message = "11";
			}
			else
			{
				logger.warn("新增失败!");
				message = "10";
			}
		}
		else if ("2".equals(type))
		{
			res = iptvVlanManageDao.updateVlan(accoid, cityId, minVlanid, maxVlanid,
					basIp, updatetime, id);
			if (res >= 1)
			{
				logger.warn("修改成功!");
				message = "21";
			}
			else
			{
				logger.warn("修改失败!");
				message = "20";
			}
		}
		else
		{
			logger.warn("type not in {1,2}");
			message = "00";
		}
		return message;
	}

	/**
	 * 通过ID删除VLAN值
	 *
	 * @author wangsenbo
	 * @date Jan 11, 2010
	 * @return String
	 */
	public String delVlan(String id)
	{
		logger.debug("delVlan()");
		String message = "";
		int res = iptvVlanManageDao.delVlan(id);
		if (res >= 1)
		{
			logger.warn("删除成功!");
			message = "删除成功!";
		}
		else
		{
			logger.warn("删除失败!");
			message = "删除失败!";
		}
		return message;
	}
}
