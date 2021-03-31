
package com.linkage.module.gtms.stb.zeroconf.bio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.stb.zeroconf.dao.ZeroFailReasonDAO;
import com.linkage.module.gtms.stb.zeroconf.dto.ZeroFailReasonDTO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class ZeroFailReasonBIO
{

	public static Logger log = Logger.getLogger(ZeroFailReasonBIO.class);
	private ZeroFailReasonDAO dao;

	public ZeroFailReasonDAO getDao()
	{
		return dao;
	}

	public void setDao(ZeroFailReasonDAO dao)
	{
		this.dao = dao;
	}

	public Long formatTime(String time, boolean next)
	{
		if (null == time)
		{
			return 0L;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			Date date = sdf.parse(time);
			Long t = date.getTime() / 1000;
			if (next)
			{
				return t + 24 * 60 * 60;
			}
			return t;
		}
		catch (ParseException e)
		{
			return 0L;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getZeroConfStatisticsReportByCityid(ZeroFailReasonDTO dto)
	{
		dto.setBeginTime(formatTime(dto.getFromTime(), false));
		dto.setEndTime(formatTime(dto.getToTime(), true));
		List<Map> list = new ArrayList<Map>();
		// 按本地网统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(不包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(dto.getCityId());
		Collections.sort(cityList);
		Map ZeroFailSuccessMap = dao.getZeroFailSuccessNum(dto);// 成功
		Map E8CNoUpMACMap = dao.getE8CNoUpMACNum(dto);// E8-C终端未上报该机顶盒MAC
		Map E8CUpMACExceptionMap = dao.getE8CUpMACExceptionNum(dto);// E8-C上报机顶盒MAC异常（含绑定多个机顶盒MAC）
		Map IPTVAccountNoMatchMap = dao.getIPTVAccountNoMatchNum(dto);// IPTV账号不匹配
		Map AAANotFindAccountMap = dao.getAAANotFindAccountNum(dto);// AAA查询不到宽带账号拨号信息
		Map AAABackInfoErrorMap = dao.getAAABackInfoErrorNum(dto);// AAA反馈宽带账号信息匹配失败
		Map SuperMap = dao.getSuperNum(dto);// AAA反馈宽带账号信息匹配失败
		long zerofailsuccess = 0;
		long e8cnoupmac = 0;
		long e8cupmacexception = 0;
		long iptvaccountnomatch = 0;
		long aaanotfindaccount = 0;
		long aaabackinfoerror = 0;
		long azerofailsuccess = 0;
		long ae8cnoupmac = 0;
		long ae8cupmacexception = 0;
		long aiptvaccountnomatch = 0;
		long aaaanotfindaccount = 0;
		long aaaabackinfoerror = 0;
		long allnum = 0;
		ArrayList<String> tlist = null;
		// 选择的属地
		Map<String, Object> amap = new HashMap();
		amap.put("city_id", dto.getCityId()); // 属地ID
		amap.put("city_name", cityMap.get(dto.getCityId())); // 属地名称
		zerofailsuccess = 0;
		e8cnoupmac = 0;
		e8cupmacexception = 0;
		iptvaccountnomatch = 0;
		aaanotfindaccount = 0;
		aaabackinfoerror = 0;
		azerofailsuccess = 0;
		ae8cnoupmac = 0;
		ae8cupmacexception = 0;
		aiptvaccountnomatch = 0;
		aaaanotfindaccount = 0;
		aaaabackinfoerror = 0;
		allnum = 0;
		tlist = CityDAO.getAllNextCityIdsByCityPid(dto.getCityId());
		for (int j = 0; j < tlist.size(); j++)
		{
			zerofailsuccess = zerofailsuccess
					+ StringUtil.getLongValue(ZeroFailSuccessMap.get(tlist.get(j)));
			e8cnoupmac = e8cnoupmac
					+ StringUtil.getLongValue(E8CNoUpMACMap.get(tlist.get(j)));
			e8cupmacexception = e8cupmacexception
					+ StringUtil.getLongValue(E8CUpMACExceptionMap.get(tlist.get(j)));
			iptvaccountnomatch = iptvaccountnomatch
					+ StringUtil.getLongValue(IPTVAccountNoMatchMap);
			aaanotfindaccount = aaanotfindaccount
					+ StringUtil.getLongValue(AAANotFindAccountMap.get(tlist.get(j)));
			aaabackinfoerror = aaabackinfoerror
					+ StringUtil.getLongValue(AAABackInfoErrorMap.get(tlist.get(j)));
			allnum = allnum + StringUtil.getLongValue(SuperMap.get(tlist.get(j)));
		}
		azerofailsuccess += zerofailsuccess;
		ae8cnoupmac += e8cnoupmac;
		ae8cupmacexception += e8cupmacexception;
		aiptvaccountnomatch += iptvaccountnomatch;
		aaaanotfindaccount += aaanotfindaccount;
		aaaabackinfoerror += aaabackinfoerror;
		amap.put("zerofailsuccess", zerofailsuccess);
		amap.put("e8cnoupmac", e8cnoupmac);
		amap.put("e8cupmacexception", e8cupmacexception);
		amap.put("iptvaccountnomatch", iptvaccountnomatch);
		amap.put("aaanotfindaccount", aaanotfindaccount);
		amap.put("aaabackinfoerror", aaabackinfoerror);
		amap.put("allnum", allnum);
		list.add(amap);
		for (int i = 0; i < cityList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			zerofailsuccess = 0;
			e8cnoupmac = 0;
			e8cupmacexception = 0;
			iptvaccountnomatch = 0;
			aaanotfindaccount = 0;
			aaabackinfoerror = 0;
			allnum = 0;
			String city_id = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++)
			{
				zerofailsuccess = zerofailsuccess
						+ StringUtil.getLongValue(ZeroFailSuccessMap.get(tlist.get(j)));
				e8cnoupmac = e8cnoupmac
						+ StringUtil.getLongValue(E8CNoUpMACMap.get(tlist.get(j)));
				e8cupmacexception = e8cupmacexception
						+ StringUtil.getLongValue(E8CUpMACExceptionMap.get(tlist.get(j)));
				iptvaccountnomatch = iptvaccountnomatch
						+ StringUtil.getLongValue(IPTVAccountNoMatchMap);
				aaanotfindaccount = aaanotfindaccount
						+ StringUtil.getLongValue(AAANotFindAccountMap.get(tlist.get(j)));
				aaabackinfoerror = aaabackinfoerror
						+ StringUtil.getLongValue(AAABackInfoErrorMap.get(tlist.get(j)));
				allnum = allnum + StringUtil.getLongValue(SuperMap.get(tlist.get(j)));
			}
			azerofailsuccess += zerofailsuccess;
			ae8cnoupmac += e8cnoupmac;
			ae8cupmacexception += e8cupmacexception;
			aiptvaccountnomatch += iptvaccountnomatch;
			aaaanotfindaccount += aaanotfindaccount;
			aaaabackinfoerror += aaabackinfoerror;
			tmap.put("zerofailsuccess", zerofailsuccess);
			tmap.put("e8cnoupmac", e8cnoupmac);
			tmap.put("e8cupmacexception", e8cupmacexception);
			tmap.put("iptvaccountnomatch", iptvaccountnomatch);
			tmap.put("aaanotfindaccount", aaanotfindaccount);
			tmap.put("aaabackinfoerror", aaabackinfoerror);
			tmap.put("allnum", allnum);
			list.add(tmap);
			tlist = null;
		}
		amap.put("xiaoji", "小计");
		amap.put("azerofailsuccess", azerofailsuccess);
		amap.put("ae8cnoupmac", ae8cnoupmac);
		amap.put("ae8cupmacexception", ae8cupmacexception);
		amap.put("aiptvaccountnomatch", aiptvaccountnomatch);
		amap.put("aaaanotfindaccount", aaaanotfindaccount);
		amap.put("aaaabackinfoerror", aaaabackinfoerror);
		long allCount = azerofailsuccess + ae8cnoupmac + ae8cupmacexception
				+ aiptvaccountnomatch + aaaanotfindaccount + aaaabackinfoerror;
		amap.put("allCount", allCount);
		cityMap = null;
		cityList = null;
		tlist = null;
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List getZeroDeviceList(int curPage_splitPage, int num_splitPage,
			ZeroFailReasonDTO dto)
	{
		return dao.getZeroDeviceList(curPage_splitPage, num_splitPage, dto);
	}

	public int getZeroDeviceListCount(ZeroFailReasonDTO dto)
	{
		return dao.getZeroDeviceListCount(dto);
	}
}
