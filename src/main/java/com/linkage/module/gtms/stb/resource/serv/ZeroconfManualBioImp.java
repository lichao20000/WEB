
package com.linkage.module.gtms.stb.resource.serv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.linkage.module.gtms.stb.resource.dao.ZeroconfManualDao;
import com.linkage.module.gtms.stb.resource.dto.ZeroconfManualDTO;
import com.linkage.module.gtms.stb.utils.ResTool;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.system.utils.DateTimeUtil;

/**
 * itv手动下发配置bio实现类
 * 
 * @author zhumiao
 * @version 1.0
 * @since 2011-12-5 下午03:21:59
 * @category com.linkage.module.lims.itv.zeroconf.bio<br>
 * @copyright 南京联创科技 网管科技部
 */
public class ZeroconfManualBioImp implements ZeroconfManualBio
{

	private ZeroconfManualDao manualDao;
	private static Logger log = Logger.getLogger(ZeroconfManualBioImp.class);

	private String dualTime(Object o)
	{
		if (o == null || "null".equalsIgnoreCase(String.valueOf(o))
				|| "".equals(String.valueOf(o)))
		{
			return "";
		}
		return new DateTimeUtil(Long.parseLong(String.valueOf(o)) * 1000L).getLongDate();
	}

	/**
	 * 获取属地id和属地名称的对应关系
	 */
	public Map<String, String> getCityMap()
	{
		Map<String, String> cityMap = new HashMap<String, String>();
		for (Map city : ResTool.getCityListAll())
		{
			cityMap.put(String.valueOf(city.get("city_id")),
					String.valueOf(city.get("city_name")));
		}
		return cityMap;
	}

	@Override
	public List<Map> getUserAccount(ZeroconfManualDTO dto)
	{
		// TODO Auto-generated method stub
		List<Map> list = manualDao.getUserAccount(dto);
		log.info("机顶盒列表数=" + list.size());
		Map<String, String> cityMap = this.getCityMap();
		if (!list.isEmpty())
		{
			for (Map data : list)
			{
				data.put("last_time", dualTime(data.get("last_time")));
				data.put("city_name", cityMap.get(String.valueOf(data.get("city_id"))));
			}
		}
		return list;
	}

	@Override
	public String manualConfiguration(ZeroconfManualDTO dto)
	{
		StringBuffer xmlSB = new StringBuffer();
		xmlSB.append("<ServXml><servList><serv>");
		xmlSB.append("<userId>").append(dto.getCustomer_id()).append("</userId>");
		xmlSB.append("<deviceId>").append(dto.getDevice_id()).append("</deviceId>");
		xmlSB.append("<serviceId>").append("120").append("</serviceId>");
		xmlSB.append("<oui>").append(dto.getOui()).append("</oui>");
		xmlSB.append("<deviceSn>").append(dto.getDevice_serialnumber())
				.append("</deviceSn>");
		xmlSB.append("</serv></servList></ServXml>");
		log.warn("调配置模块下发配置----->" + xmlSB.toString());
		int result = CreateObjectFactory.createPreProcess(Global.GW_TYPE_STB).processServiceInterface_STB(xmlSB.toString());
		if (1 == result)
		{
			log.warn("调配置模块下发配置成功----->" + dto.getDevice_serialnumber());
			return "手动下发配置成功";
		}
		log.warn("调配置模块下发配置失败----->" + dto.getDevice_serialnumber());
		return "手动下发配置失败";
	}

	public void setManualDao(ZeroconfManualDao manualDao)
	{
		this.manualDao = manualDao;
	}
}
