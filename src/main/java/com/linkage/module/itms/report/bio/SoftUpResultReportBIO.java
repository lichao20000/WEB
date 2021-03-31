package com.linkage.module.itms.report.bio;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.dao.SoftUpResultReportDAO;

/**
 * 软件升级统计
 */
@SuppressWarnings("rawtypes")
public class SoftUpResultReportBIO
{
	private static Logger logger = LoggerFactory.getLogger(SoftUpResultReportBIO.class);
	private SoftUpResultReportDAO dao;

	/**
	 * 软件升级结果统计
	 */
	public List<Map> countSoftUpResult(String starttime1,String endtime1,
			String cityId,String gw_type,String isSoftUp,String vendorId,String deviceModelId)
	{
		List<Map> list = new ArrayList<Map>();
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		Collections.sort(cityList);
		// 总配置数 由于数据库数据量较大，总记录数采用其他统计数相加所得
		//Map allmap = dao.countAll(starttime1, endtime1, cityId, gw_type, isSoftUp);
		// 升级结果
		List resultlist = dao.countResult(starttime1,endtime1,cityId,
											gw_type,isSoftUp,vendorId,deviceModelId);
		logger.debug("countSoftUpResult resultlist:[{}]",resultlist);
		if(LipossGlobals.inArea(Global.JSDX))
		{
			for (int i = 0; i < cityList.size(); i++)
			{
				String city_id = cityList.get(i);
				ArrayList<String> tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
				// 总配置数
				long allup = 0;
				// 成功数
				long successnum = 0;
				// 失败（需重配）数
				long failnum = 0;
				// 未做数
				long noupnum = 0;
				// 失败（下次上线再做）
				long nextnum = 0;
				Map<String, Object> tmap = new HashMap<String, Object>();
				tmap.put("city_id", city_id);
				tmap.put("city_name", cityMap.get(city_id));
				for (int j = 0; j < tlist.size(); j++)
				{
					String cityId2 = tlist.get(j);
					//allup = allup + StringUtil.getLongValue(allmap.get(cityId2));
					for (int k = 0; k < resultlist.size(); k++)
					{
						Map rmap = (Map) resultlist.get(k);
						if (cityId2.equals(rmap.get("city_id")))
						{
							if ("100".equals(StringUtil.getStringValue(rmap.get("status"))))
							{
								//彻底失败只显示策略ID为-1，策略结果为100,除了等待执行的，其他都认为是成功的
								if (!"-1".equals(StringUtil.getStringValue(rmap.get("result_id"))))
								{
									// 成功数
									successnum += StringUtil.getLongValue(rmap.get("total"));
								}
								//彻底失败只显示策略ID为-1，策略结果为100，
								else if("-1".equals(StringUtil.getStringValue(rmap.get("result_id"))))
								{
									// 失败（需重配）数
									failnum += StringUtil.getLongValue(rmap.get("total"));
								}
							}
							else if ("0".equals(StringUtil.getStringValue(rmap.get("status"))))
							{
								if ("0".equals(StringUtil.getStringValue(rmap.get("result_id"))))
								{
									// 未做数
									noupnum += StringUtil.getLongValue(rmap.get("total"));
								}
								else
								{
									if (!"1".equals(StringUtil.getStringValue(rmap.get("result_id"))))
									{
										// 失败（下次上线再做）
										nextnum += StringUtil.getLongValue(rmap.get("total"));
									}else{
										allup += StringUtil.getLongValue(rmap.get("total"));
									}
								}
							}
							else
							{
								allup = allup + StringUtil.getLongValue(rmap.get("total"));
							}
						}
					}
				}
				allup = allup + successnum + noupnum + nextnum + failnum;
				tmap.put("allup", allup);
				tmap.put("successnum", successnum);
				tmap.put("noupnum", noupnum);
				tmap.put("nextnum", nextnum);
				tmap.put("failnum", failnum);
				tmap.put("percent", percent(successnum, successnum+failnum));
				list.add(tmap);
				tlist = null;
			}
		}
		else 
		{
			for (int i = 0; i < cityList.size(); i++)
			{
				String city_id = cityList.get(i);
				ArrayList<String> tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
				// 总配置数
				long allup = 0;
				// 成功数
				long successnum = 0;
				//执行中数
				long runningnum = 0;
				// 失败（需重配）数
				long failnum = 0;
				// 未做数
				long noupnum = 0;
				// 失败（下次上线再做）
				long nextnum = 0;
				Map<String, Object> tmap = new HashMap<String, Object>();
				tmap.put("city_id", city_id);
				tmap.put("city_name", cityMap.get(city_id));
				tmap.put("vendor_id", vendorId);
				tmap.put("device_model_id", deviceModelId);
				for (int j = 0; j < tlist.size(); j++)
				{
					String cityId2 = tlist.get(j);
					//allup = allup + StringUtil.getLongValue(allmap.get(cityId2));
					for (int k = 0; k < resultlist.size(); k++)
					{
						Map rmap = (Map) resultlist.get(k);
						if (cityId2.equals(rmap.get("city_id")))
						{
							if ("100".equals(StringUtil.getStringValue(rmap.get("status"))))
							{
								if ("1".equals(StringUtil.getStringValue(rmap
										.get("result_id"))))
								{
									// 成功数
									successnum += StringUtil.getLongValue(rmap.get("total"));
								}
								else
								{
									// 失败（需重配）数
									failnum += StringUtil.getLongValue(rmap.get("total"));
								}
							}
							else if ("0".equals(StringUtil.getStringValue(rmap.get("status"))))
							{
								if ("0".equals(StringUtil.getStringValue(rmap.get("result_id"))))
								{
									// 未做数
									noupnum += StringUtil.getLongValue(rmap.get("total"));
								}
								else
								{
									if (!"1".equals(StringUtil.getStringValue(rmap.get("result_id"))))
									{
										// 失败（下次上线再做）
										nextnum += StringUtil.getLongValue(rmap.get("total"));
									}
								}
							}
							else
							{
								runningnum += StringUtil.getLongValue(rmap.get("total"));
							}
						}
					}
				}
				
				allup = allup + successnum + noupnum + nextnum + failnum + runningnum;
				tmap.put("allup", allup);
				tmap.put("successnum", successnum);
				tmap.put("runningnum", runningnum);
				tmap.put("noupnum", noupnum);
				tmap.put("nextnum", nextnum);
				tmap.put("failnum", failnum);
				tmap.put("percent", percent(successnum, allup));
				
				if(LipossGlobals.inArea(Global.SDDX))
				{
					//正在执行的算进未触发里
					tmap.put("noupnum",StringUtil.getIntValue(tmap,"noupnum")
										+ StringUtil.getIntValue(tmap,"runningnum"));
				}
				list.add(tmap);
				tlist = null;
			}
		}
		
		cityMap = null;
		cityList = null;
		
		logger.debug("countSoftUpResult list:[{}]",list);
		return list;
	}

	public String percent(long p1, long p2)
	{
		if (p2 == 0)
		{
			if(LipossGlobals.inArea(Global.NXDX)){
				return "0";
			}else {
				return "N/A";
			}
		}
		
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		return nf.format((double) p1 / p2);
	}

	public List<Map> getDevList(String gw_type,String starttime1, String endtime1, String cityId,
			String status, String resultId, String isMgr, int curPage_splitPage,
			int num_splitPage,String isSoftUp,String vendorId,String deviceModelId)
	{
		if("0".equals(status) && "0".equals(resultId) 
				&& LipossGlobals.inArea(Global.SDDX))
		{
			resultId=null;
		}
		return dao.getDevList(gw_type,starttime1,endtime1,cityId,status,resultId,isMgr,
								curPage_splitPage,num_splitPage,isSoftUp,vendorId,deviceModelId);
	}

	public int getDevCount(String gw_type,String starttime1, String endtime1, String cityId,
			String status, String resultId, String isMgr, int curPage_splitPage,
			int num_splitPage,String isSoftUp,String vendorId,String deviceModelId)
	{
		if("0".equals(status) && "0".equals(resultId) 
				&& LipossGlobals.inArea(Global.SDDX))
		{
			resultId=null;
		}
		return dao.getDevCount(gw_type,starttime1,endtime1,cityId,status,resultId,isMgr,
								curPage_splitPage,num_splitPage,isSoftUp,vendorId,deviceModelId);
	}

	public List<Map> getDevExcel(String gw_type,String starttime1, String endtime1, String cityId,
			String status, String resultId, String isMgr,String isSoftUp,String vendorId,String deviceModelId)
	{
		if("0".equals(status) && "0".equals(resultId) 
				&& LipossGlobals.inArea(Global.SDDX))
		{
			resultId=null;
		}
		return dao.getDevExcel(gw_type,starttime1,endtime1,cityId,status,resultId,
								isMgr,isSoftUp,vendorId,deviceModelId);
	}
	
	
	
	public SoftUpResultReportDAO getDao(){
		return dao;
	}

	public void setDao(SoftUpResultReportDAO dao){
		this.dao = dao;
	}
}
