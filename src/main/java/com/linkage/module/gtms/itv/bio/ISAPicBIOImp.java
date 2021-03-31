
package com.linkage.module.gtms.itv.bio;

import java.util.List;
import java.util.Map;

import com.linkage.module.gtms.itv.dao.ISAPicDAO;

public class ISAPicBIOImp implements ISAPicBIO
{

	private ISAPicDAO ISAPicDao;

	public ISAPicDAO getISAPicDao()
	{
		return ISAPicDao;
	}

	public void setISAPicDao(ISAPicDAO ISAPicDao)
	{
		this.ISAPicDao = ISAPicDao;
	}

	@SuppressWarnings("unchecked")
	public List<Map> getISATerminalReport(String cityId)
	{
		String cityName = cityIdTocityName(cityId);
		System.out.println("cityName:" + cityName);
		List<Map> list = ISAPicDao.getISATerminalReport(cityName);
		return list;
	}

	@SuppressWarnings("unchecked")
	// 本属地数据
	public List<Map> getISAEPGReport(String cityId)
	{
		String cityName = cityIdTocityName(cityId);
		System.out.println("cityName:" + cityName);
		List<Map> list = ISAPicDao.getISAEPGReport(cityName);
		return list;
	}

	@SuppressWarnings("unchecked")
	// 本属地数据
	public List<Map> getISAEPGReportSZX()
	{
		List<Map> list = ISAPicDao.getISAEPGReportSZX();
		return list;
	}

	public String cityIdTocityName(String cityId)
	{
		String cityName = null;
		System.out.println("cityId:  " + cityId);
		if (cityId.equals("00"))
		{
			cityName = "省中心";
		}
		if (cityId.length() >= 4)
		{
			if (cityId.substring(0, 3).equals("0001") || cityId.equals("0100"))
			{
				cityName = "南京市";
			}
			if (cityId.substring(0, 3).equals("0002") || cityId.equals("0200"))
			{
				cityName = "苏州市";
			}
			if (cityId.substring(0, 3).equals("0003") || cityId.equals("0300"))
			{
				cityName = "无锡市";
			}
			if (cityId.substring(0, 3).equals("0004") || cityId.equals("0400"))
			{
				cityName = "常州市";
			}
			if (cityId.substring(0, 3).equals("0005") || cityId.equals("0500"))
			{
				cityName = "镇江市";
			}
			if (cityId.substring(0, 3).equals("0006") || cityId.equals("0600"))
			{
				cityName = "扬州市";
			}
			if (cityId.substring(0, 3).equals("0007") || cityId.equals("0700"))
			{
				cityName = "南通市";
			}
			if (cityId.substring(0, 3).equals("0008") || cityId.equals("0800"))
			{
				cityName = "泰州市";
			}
			if (cityId.substring(0, 3).equals("0009") || cityId.equals("0900"))
			{
				cityName = "徐州市";
			}
			if (cityId.substring(0, 3).equals("0010") || cityId.equals("1000"))
			{
				cityName = "淮安市";
			}
			if (cityId.substring(0, 3).equals("0011") || cityId.equals("1100"))
			{
				cityName = "盐城市";
			}
			if (cityId.substring(0, 3).equals("0012") || cityId.equals("1200"))
			{
				cityName = "连云港市";
			}
			if (cityId.substring(0, 3).equals("0013") || cityId.equals("1300"))
			{
				cityName = "宿迁市";
			}
		}
		return cityName;
	}

	@Override
	public List<Map> getEPG()
	{
		return ISAPicDao.getEPG();
	}
}
