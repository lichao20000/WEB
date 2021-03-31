
package com.linkage.module.itms.resource.bio;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.BssDevPortDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.dao.BssSpecRealInfoDAO;

public class BssSpecRealInfoBIO
{

	private static Logger logger = LoggerFactory
			.getLogger(BssSpecRealInfoDAO.class);
	private BssSpecRealInfoDAO dao;

	/**
	 * @param startOpenDate1
	 *            开始时间
	 * @param endOpenDate1
	 *            结束时间
	 * @param city_id
	 *            属地
	 * @param devicetype
	 *            设备类型
	 * @param cust_type_id
	 *            客户类型
	 * @param spec_id
	 *            用户终端类型
	 * @return
	 */
	public List<Map> getURealInfo(String startOpenDate1, String endOpenDate1,
			String city_id, String devicetype, String spec_id)
	{
		BssDevPortDAO bssAct = new BssDevPortDAO();
		Map<String,String> G_BssDev_PortName_Map = bssAct.getBssDevPortNameMapCore();
		// 属地列表
		Map cityMap = Global.G_CityId_CityName_Map;
		long total = 0;
		ArrayList<String> tlist = null;
		// start ==========================bss终端规格统计信息===================================
		Map bssMap = dao.getBssSpecInfo(startOpenDate1, endOpenDate1, city_id,
				devicetype, spec_id);
		// 属地bss统计信息
		List<Map> bsslist = new ArrayList<Map>();
		// bss终端总数
		if (!StringUtil.IsEmpty(city_id) && "00".equals(city_id))
		{
			// 根据属地cityId获取下一层属地ID(不包含自己)
			ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(city_id);
			Collections.sort(cityList);
			cityList.add(0, city_id);
			for (int i = 0; i < cityList.size(); i++)
			{
				Map<String, Object> tmap = new HashMap<String, Object>();
				total = 0;
				String cityId = cityList.get(i);
				tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
				tmap.put("city_id", cityId);
				tmap.put("city_name", cityMap.get(cityId));
				for (int j = 0; j < tlist.size(); j++)
				{
					total = total + StringUtil.getLongValue(bssMap.get(tlist.get(j)));
				}
				tmap.put("denominator", total);
				tmap.put("spec_name", G_BssDev_PortName_Map.get(spec_id));
				bsslist.add(tmap);
				tlist = null;
			}
		}
		else if (!StringUtil.IsEmpty(city_id))
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			total = 0;
			tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++)
			{
				total = total + StringUtil.getLongValue(bssMap.get(tlist.get(j)));
			}
			tmap.put("denominator", total);
			tmap.put("spec_name", G_BssDev_PortName_Map.get(spec_id));
			bsslist.add(tmap);
			tlist = null;
		}
		// end ==========================bss终端规格统计信息===================================
		
		// =========================用户实际使用的终端规格===================================
		Map userMap = dao.getUserSpecInfo(startOpenDate1, endOpenDate1, city_id,
				devicetype, spec_id);
		List<Map> userlist = new ArrayList<Map>();
		List<Map> datalist = new ArrayList<Map>();
		Map datamap = new HashMap<String, String>();
		if (!StringUtil.IsEmpty(city_id) && "00".equals(city_id))
		{
			ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(city_id);
			Collections.sort(cityList);
			cityList.add(0, city_id);
			Map valuemap = new HashMap<String, String>();
			for (int i = 0; i < cityList.size(); i++)
			{
				List<Map> templist = new ArrayList<Map>();
				String city = cityList.get(i);
				templist = getDataList(userMap, city);
				datalist.addAll(templist);
			}
		}
		else
		{
			datalist = getDataList(userMap, city_id);
		}
		
		// =========================用户实际使用的终端规格===================================
		
		List<Map> specList = new ArrayList<Map>();
		for (int i = 0; i < datalist.size(); i++)
		{
			Map map = datalist.get(i);
			Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
			Map fmap = null;
			while (it.hasNext())
			{
				fmap = new HashMap<String, String>();
				Entry entry = it.next();
				String key = StringUtil.getStringValue(entry.getKey());
				String cityid = key.split(":")[0];
				String specname = key.split(":")[1];
				String bssSpecName = "";
				String denominator = "";
				for (int k = 0; k < bsslist.size(); k++)
				{
					Map bssmap = bsslist.get(k);
					String bsscityid = StringUtil.getStringValue(bssmap.get("city_id"));
					if (bsscityid.equals(cityid))
					{
						denominator = StringUtil
								.getStringValue(bssmap.get("denominator"));
						break;
					}
				}
				String molecular = StringUtil.getStringValue(entry.getValue());
				String pert = getDecimal(molecular, denominator);
				fmap.put("city_id", cityid);
				fmap.put("city_name", cityMap.get(cityid));
				fmap.put("bssspec_name", G_BssDev_PortName_Map.get(spec_id));
				fmap.put("spec_name", G_BssDev_PortName_Map.get(specname));
				fmap.put("molecular", molecular);
				fmap.put("pert", pert);
				specList.add(fmap);
			}
		}
		return specList;
	}
	
	
	public String getTabBssDevPort(String cust_type_id){
		Map map = dao.getTabBssDevPort(cust_type_id);
		StringBuffer  result = new StringBuffer();
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Entry<String,String> entry = it.next();
			result.append(entry.getKey()).append("-").append(entry.getValue()).append(";");
		}
		return result.toString();
	}
	
	/**
	 * 拼装数据信息
	 * 
	 * @param dataMap
	 * @param city_id
	 * @return
	 */
	private List<Map> getDataList(Map dataMap, String city_id)
	{
		List<Map> datalist = new ArrayList<Map>();
		ArrayList<String> cityList = CityDAO.getAllNextCityIdsByCityPid(city_id);
		Iterator<Map.Entry<String, String>> itdata = dataMap.entrySet().iterator();
		// 放置数据
		Map valuemap = new HashMap<String, String>();
		while (itdata.hasNext())
		{
			Entry entry = itdata.next();
			String key = StringUtil.getStringValue(entry.getKey());
			String cityid = key.split(":")[0];
			String specname = key.split(":")[1];
			if (cityList.contains(cityid))
			{
				long data = 0;
				if (valuemap.containsKey(city_id + ":" + specname))
				{
					data = StringUtil.getLongValue((valuemap
							.get(city_id + ":" + specname)))
							+ StringUtil.getLongValue(entry.getValue());
					valuemap.put(city_id + ":" + specname, data);
				}
				else
				{
					data = StringUtil.getLongValue(entry.getValue());
					valuemap.put(city_id + ":" + specname, data);
				}
			}
		}
		datalist.add(valuemap);
		return datalist;
	}

	/**
	 * 占用比列
	 * 
	 * @param molecular
	 *            分子
	 * @param denominator
	 *            分母
	 * @return
	 */
	public String getDecimal(String molecular, String denominator)
	{
		if (null == molecular || "0".equals(molecular) || null == denominator
				|| "0".equals(denominator))
		{
			return "0%";
		}
		float t1 = Float.parseFloat(molecular);
		float t2 = Float.parseFloat(denominator);
		float f = t1 / t2;
		DecimalFormat df = new DecimalFormat();
		String style = "0.00%";
		df.applyPattern(style);
		return df.format(f);
	}

	public BssSpecRealInfoDAO getDao()
	{
		return dao;
	}

	public void setDao(BssSpecRealInfoDAO dao)
	{
		this.dao = dao;
	}
}
