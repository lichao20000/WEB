package com.linkage.module.gtms.itv.bio;

import java.util.List;
import java.util.Map;

import com.linkage.module.gtms.itv.dao.FirstPageDAO;


public class FirstPageBIOImp implements FirstPageBIO
{

	private FirstPageDAO firstPageDao;
	
	@Override
	public List<Map<String,String>> getEPGReport(String interval,String cityID)
	{
		return firstPageDao.getEPGReport(interval,cityID);
	}

	@Override
	public List<Map<String,String>> getNFReport(String interval)
	{
		return firstPageDao.getNFReport(interval);
	}

	public FirstPageDAO getFirstPageDao()
	{
		return firstPageDao;
	}

	public void setFirstPageDao(FirstPageDAO firstPageDao)
	{
		this.firstPageDao = firstPageDao;
	}

	@Override
	public List<Map<String, String>> getServiceUser(String interval,
			String cityID)
	{
		return firstPageDao.getServiceUser(interval,cityID);
	}

	@Override
	public List<Map<String, String>> getWarnReport(String interval,
			String cityID)
	{
		return firstPageDao.getWarnReport(interval,cityID);
	}

	@Override
	public Map getCityMap()
	{
		return firstPageDao.getCityMap();
	}

	@Override
	public List<Map<String, String>> getDatainputReport()
	{
		return firstPageDao.getDatainputReport();
	}

	@Override
	public List<Map<String, String>> getTownReport()
	{
		return firstPageDao.getTownReport();
	}

}
