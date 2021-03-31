
package com.linkage.module.gtms.stb.zeroconf.bio;

import java.text.NumberFormat;
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
import com.linkage.module.gtms.stb.zeroconf.dao.ZeroConfStatisticsReportDAO;
import com.linkage.module.gtms.stb.zeroconf.dto.ZeroConfStatisticsReportDTO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class ZeroConfStatisticsReportBIO
{

	public static Logger log = Logger.getLogger(ZeroConfStatisticsReportBIO.class);
	private ZeroConfStatisticsReportDAO dao;

	public ZeroConfStatisticsReportDAO getDao()
	{
		return dao;
	}

	public void setDao(ZeroConfStatisticsReportDAO dao)
	{
		this.dao = dao;
	}

	public String percent(long p1, long p2)
	{
		double p3;
		if (p1 == 0 && p2 == 0)
		{
			return "N/A";
		}
		else
		{
			p3 = (double) p1 / (p1 + p2);
		}
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p3);
		return str;
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
	public List getZeroConfStatisticsReportByCityid(ZeroConfStatisticsReportDTO dto)
	{
		dto.setBeginTime(formatTime(dto.getFromTime(), false));
		dto.setEndTime(formatTime(dto.getToTime(), true));
		List<Map> list = new ArrayList<Map>();
		// 按本地网统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(不包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(dto.getCityId());
		Collections.sort(cityList);
		Map ADSLAllMap = dao.getADSLAllNum(dto);// all
		Map ADSLFailMap = dao.getADSLFailNum(dto);// fail
		Map ADSLFail_1Map = dao.getADSLFail_1Num(dto);// fail_1
		Map ADSLSuccessMap = dao.getADSLSuccessNum(dto);// success
		Map ADSLNoMap = dao.getADSLNoNum(dto);// success
		Map LANAllMap = dao.getLANAllNum(dto);
		Map LANFailMap = dao.getLANFailNum(dto);
		Map LANFail_1Map = dao.getLANFail_1Num(dto);
		Map LANSuccessMap = dao.getLANSuccessNum(dto);
		Map LANNoMap = dao.getLANnoNum(dto);
		Map FTHHAllMap = dao.getFTHHAllNum(dto);
		Map FTHHFailMap = dao.getFTHHFailNum(dto);
		Map FTHHFail_1Map = dao.getFTHHFail_1Num(dto);
		Map FTHHSuccessMap = dao.getFTHHSuccessNum(dto);
		Map FTHHNoMap = dao.getFTHHNoNum(dto);
		long adsl = 0;
		long adslsuccess = 0;
		long adslfail = 0;
		long adslfail_1 = 0;
		long adslno = 0;
		long lan = 0;
		long lansuccess = 0;
		long lanfail = 0;
		long lanfail_1 = 0;
		long lanno = 0;
		long fthh = 0;
		long fthhsuccess = 0;
		long fthhfail = 0;
		long fthhfail_1 = 0;
		long fthhno = 0;
		long aadsl = 0;
		long aadslsuccess = 0;
		long aadslfail = 0;
		long aadslno = 0;
		long alan = 0;
		long alansuccess = 0;
		long alanfail = 0;
		long alanno = 0;
		long afthh = 0;
		long afthhsuccess = 0;
		long afthhfail = 0;
		long afthhno = 0;
		ArrayList<String> tlist = null;
		// 选择的属地
		Map<String, Object> amap = new HashMap();
		list.add(amap);
		for (int i = 0; i < cityList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			adsl = 0;
			adslsuccess = 0;
			adslfail = 0;
			adslfail_1 = 0;
			adslno = 0;
			lan = 0;
			lansuccess = 0;
			lanfail = 0;
			lanfail_1 = 0;
			lanno = 0;
			fthh = 0;
			fthhsuccess = 0;
			fthhfail = 0;
			fthhfail_1 = 0;
			fthhno = 0;
			String city_id = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++)
			{
				adsl = adsl + StringUtil.getLongValue(ADSLAllMap.get(tlist.get(j)));
				adslsuccess = adslsuccess
						+ StringUtil.getLongValue(ADSLSuccessMap.get(tlist.get(j)));
				adslfail = adslfail
						+ StringUtil.getLongValue(ADSLFailMap.get(tlist.get(j)));
				adslfail_1 = adslfail_1
						+ StringUtil.getLongValue(ADSLFail_1Map.get(tlist.get(j)));
				adslno = adslno + StringUtil.getLongValue(ADSLNoMap.get(tlist.get(j)));
				lan = lan + StringUtil.getLongValue(LANAllMap.get(tlist.get(j)));
				lansuccess = lansuccess
						+ StringUtil.getLongValue(LANSuccessMap.get(tlist.get(j)));
				lanfail = lanfail + StringUtil.getLongValue(LANFailMap.get(tlist.get(j)));
				lanfail_1 = lanfail_1
						+ StringUtil.getLongValue(LANFail_1Map.get(tlist.get(j)));
				lanno = lanno + StringUtil.getLongValue(LANNoMap.get(tlist.get(j)));
				fthh = fthh + StringUtil.getLongValue(FTHHAllMap.get(tlist.get(j)));
				fthhsuccess = fthhsuccess
						+ StringUtil.getLongValue(FTHHSuccessMap.get(tlist.get(j)));
				fthhfail = fthhfail
						+ StringUtil.getLongValue(FTHHFailMap.get(tlist.get(j)));
				fthhfail_1 = fthhfail_1
						+ StringUtil.getLongValue(FTHHFail_1Map.get(tlist.get(j)));
				fthhno = fthhno + StringUtil.getLongValue(FTHHNoMap.get(tlist.get(j)));
			}
			aadsl += adsl;
			aadslsuccess += adslsuccess;
			aadslfail += adslfail;
			aadslfail += adslfail_1;
			aadslno += adslno;
			alan += lan;
			alansuccess += lansuccess;
			alanfail += lanfail;
			alanfail += lanfail_1;
			alanno += lanno;
			afthh += fthh;
			afthhsuccess += fthhsuccess;
			afthhfail += fthhfail;
			afthhfail += fthhfail_1;
			afthhno += fthhno;
			String adslpercent = percent(adslsuccess, adslfail);
			tmap.put("adsl", adsl);
			tmap.put("adslsuccess", adslsuccess);
			tmap.put("adslfail", adslfail);
			tmap.put("adslno", adslno);
			tmap.put("adslpercent", adslpercent);
			String lanpercent = percent(lansuccess, lanfail);
			tmap.put("lan", lan);
			tmap.put("lansuccess", lansuccess);
			tmap.put("lanfail", lanfail);
			tmap.put("lanno", lanno);
			tmap.put("lanpercent", lanpercent);
			String fthhpercent = percent(fthhsuccess, fthhfail);
			tmap.put("fthh", fthh);
			tmap.put("fthhsuccess", fthhsuccess);
			tmap.put("fthhfail", fthhfail);
			tmap.put("fthhno", fthhno);
			tmap.put("fthhpercent", fthhpercent);
			list.add(tmap);
			tlist = null;
		}
		amap.put("all", "总计");
		String aadslpercent = percent(aadslsuccess, aadslfail);
		amap.put("aadsl", aadsl);
		amap.put("aadslsuccess", aadslsuccess);
		amap.put("aadslfail", aadslfail);
		amap.put("aadslno", aadslno);
		amap.put("aaadslpercent", aadslpercent);
		String alanpercent = percent(alansuccess, alanfail);
		amap.put("alan", alan);
		amap.put("alansuccess", alansuccess);
		amap.put("alanfail", alanfail);
		amap.put("alanno", alanno);
		amap.put("alanpercent", alanpercent);
		String afthhpercent = percent(afthhsuccess, afthhfail);
		amap.put("afthh", afthh);
		amap.put("afthhsuccess", afthhsuccess);
		amap.put("afthhfail", afthhfail);
		amap.put("afthhno", afthhno);
		amap.put("afthhpercent", afthhpercent);
		cityMap = null;
		cityList = null;
		tlist = null;
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> queryCustomerList(ZeroConfStatisticsReportDTO dto, int firstRecord,
			int num_splitPage)
	{
		return dao.queryCustomerList(dto, firstRecord, num_splitPage);
	}

	public int countCustomer(ZeroConfStatisticsReportDTO dto)
	{
		return dao.countCustomer(dto);
	}

	@SuppressWarnings("rawtypes")
	public List<Map> queryFailCustomerList(ZeroConfStatisticsReportDTO dto,
			int firstRecord, int num_splitPage)
	{
		return dao.queryFailCustomerList(dto, firstRecord, num_splitPage);
	}

	public int countFailCustomer(ZeroConfStatisticsReportDTO dto)
	{
		return dao.countFailCustomer(dto);
	}
}
