
package com.linkage.module.bbms.report.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.module.bbms.report.dao.ServiceQueryDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.system.utils.DateTimeUtil;

@SuppressWarnings("unchecked")
public class ServiceQueryBIO
{

	private ServiceQueryDAO serviceQueryDao;
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;
	/**
	 * Map<vendor_id,vendor_add>
	 */
	private Map<String, String> vendorMap = null;
	/**
	 * Map<device_model_id,device_model>
	 */
	private Map<String, String> deviceModelMap = null;
	/**
	 * Map<devicetype_id,softwareversion>
	 */
	private Map<String, String> devicetypeMap = null;

	/**
	 * 取的所有的业务类型
	 * 
	 * @author wangsenbo
	 * @date Mar 12, 2010
	 * @param
	 * @return List<Map<String,String>>
	 */
	public List<Map<String, String>> getServiceTypeList()
	{
		return serviceQueryDao.getServiceTypeList();
	}


	/**
	 * 通过用户账号，设备序列号查询业务信息
	 * 
	 * @author wangsenbo
	 * @date Mar 16, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> queryService(String cityId, String userName,
			String device_serialnumber, String serviceId)
	{
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		Map<String, String> serviceTypeMap = serviceQueryDao.getServiceTypeMap();
		List<Map<String, String>> list = serviceQueryDao.queryService(cityId, userName,
				device_serialnumber, serviceId);
		Map<String, Map> umap = new HashMap<String, Map>();
		List<String> ulist = new ArrayList<String>();
		List<Map> userlist = new ArrayList<Map>();
		for (Map<String, String> tmap : list)
		{
			Map<String, Object> rmap;
			List<Map<String, String>> servicelist;
			if (null == umap.get(tmap.get("username")))
			{
				ulist.add(tmap.get("username"));
				rmap = new HashMap<String, Object>();
				// 属地
				String city_id = tmap.get("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name))
				{
					rmap.put("city_name", city_name);
				}
				else
				{
					rmap.put("city_name", "");
				}
				// 用户账号
				rmap.put("username", tmap.get("username"));
				// 厂商
				String vendor_id = tmap.get("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add))
				{
					rmap.put("vendor_add", vendor_add);
				}
				else
				{
					rmap.put("vendor_add", "");
				}
				// 设备序列号
				rmap.put("device", tmap.get("oui") + "-"
						+ tmap.get("device_serialnumber"));
				// 型号
				String device_model_id = tmap.get("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap
						.get(device_model_id));
				if (false == StringUtil.IsEmpty(device_model))
				{
					rmap.put("device_model", device_model);
				}
				else
				{
					rmap.put("device_model", "");
				}
				// 软件版本
				String devicetype_id = StringUtil.getStringValue(tmap.get("devicetype_id"));
				String softwareversion = StringUtil.getStringValue(devicetypeMap
						.get(devicetype_id));
				if (false == StringUtil.IsEmpty(softwareversion))
				{
					rmap.put("softwareversion", softwareversion);
				}
				else
				{
					rmap.put("softwareversion", "");
				}
				// 业务list
				servicelist = new ArrayList<Map<String, String>>();
			}
			else
			{
				rmap = umap.get(tmap.get("username"));
				servicelist = (List<Map<String, String>>) rmap.get("servicelist");
			}
			Map<String, String> smap = new HashMap<String, String>();
			String service_id = StringUtil.getStringValue(tmap.get("id"));
			smap.put("service_id", service_id);// 业务ID
			// 业务名称
			String service_name = StringUtil.getStringValue(serviceTypeMap
					.get(service_id));
			if (false == StringUtil.IsEmpty(service_name))
			{
				smap.put("service_name", service_name);
			}
			else
			{
				smap.put("service_name", "");
			}
			// 采集时间
			// 将gather_time转换成时间
			try
			{
				long gather_time = StringUtil.getLongValue(tmap.get("gather_time"));
				DateTimeUtil dateTimeUtil = new DateTimeUtil(gather_time * 1000);
				smap.put("gather_time", dateTimeUtil.getDate());
			}
			catch (NumberFormatException e)
			{
				smap.put("gather_time", "");
			}
			catch (Exception e)
			{
				smap.put("gather_time", "");
			}
			// 开启状态
			String service_result = StringUtil.getStringValue(tmap.get("service_result"));
			if ("1".equals(service_result))
			{
				smap.put("service_result", "是");
			}
			else if ("0".equals(service_result))
			{
				smap.put("service_result", "否");
			}
			else
			{
				smap.put("service_result", "否");
			}
			servicelist.add(smap);
			rmap.put("servicelist", servicelist);
			umap.put(tmap.get("username"), rmap);
		}
		for (String name : ulist)
		{
			userlist.add(umap.get(name));
		}
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		devicetypeMap = null;
		serviceTypeMap = null;
		return userlist;
	}

	/**
	 * @return the serviceQueryDao
	 */
	public ServiceQueryDAO getServiceQueryDao()
	{
		return serviceQueryDao;
	}

	/**
	 * @param serviceQueryDao
	 *            the serviceQueryDao to set
	 */
	public void setServiceQueryDao(ServiceQueryDAO serviceQueryDao)
	{
		this.serviceQueryDao = serviceQueryDao;
	}

	public List<Map> serviceExcel(String cityId, String userName,
			String device_serialnumber, String serviceId)
	{
		List<Map> userlist = queryService(cityId, userName, device_serialnumber, serviceId);
		List<Map> excellist = new ArrayList<Map>();
		for (Map map : userlist)
		{
			Map amap = new HashMap();
			amap.put("column1", "用户账号");
			amap.put("column2", map.get("username"));
			amap.put("column3", "属地");
			amap.put("column4", map.get("city_name"));
			excellist.add(amap);
			Map bmap = new HashMap();
			bmap.put("column1", "设备序列号");
			bmap.put("column2", map.get("device"));
			bmap.put("column3", "厂商");
			bmap.put("column4", map.get("vendor_add"));
			excellist.add(bmap);
			Map cmap = new HashMap();
			cmap.put("column1", "型号");
			cmap.put("column2", map.get("device_model"));
			cmap.put("column3", "软件版本");
			cmap.put("column4", map.get("softwareversion"));
			excellist.add(cmap);
			Map dmap = new HashMap();
			dmap.put("column1", "业务ID");
			dmap.put("column2", "业务种类");
			dmap.put("column3", "是否开启");
			dmap.put("column4", "采集时间");
			excellist.add(dmap);
			List<Map<String, String>> servicelist = (List<Map<String, String>>)map.get("servicelist");
			for (Map<String, String> map2 : servicelist)
			{
				Map emap = new HashMap();
				emap.put("column1", map2.get("service_id"));
				emap.put("column2", map2.get("service_name"));
				emap.put("column3", map2.get("service_result"));
				emap.put("column4", map2.get("gather_time"));
				excellist.add(emap);
			}
			Map fmap = new HashMap();
			fmap.put("column1", "");
			fmap.put("column2", "");
			fmap.put("column3", "");
			fmap.put("column4", "");
			excellist.add(fmap);
		}
		return excellist;
	}
}
