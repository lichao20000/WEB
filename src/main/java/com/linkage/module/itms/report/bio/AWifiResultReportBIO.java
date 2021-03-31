
package com.linkage.module.itms.report.bio;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.dao.AWifiResultReportDAO;

/**
 * 考核所有的用户
 * 
 * @author 王森博
 */
public class AWifiResultReportBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(AWifiResultReportBIO.class);
	private AWifiResultReportDAO dao;

	/**
	 * AWIFI 安装情况统计
	 * 
	 * @author wangsenbo
	 * @date May 19, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> countAWifiResult(String tableName, String starttime1, String endtime1, String cityId,String vendorId, String deviceModelId, String gw_type,String awifi_type,String isSoftUp)
	{
		List<Map> list = new ArrayList<Map>();
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		// 城市排序，将南京放在南京的子属地之上
		Collections.sort(cityList,new Comparator<String>(){  
            @Override  
            public int compare(String b1, String b2) {  
            	if(b1.length() == b2.length()){
            		return Integer.parseInt(b2) - Integer.parseInt(b1);
            	}
            	else {
            		return  b1.length() - b2.length();
            	}
                 
                 
            }  
        });
		logger.warn("排序后的城市：{}",cityList );
		// 总配置数 由于数据库数据量较大，总记录数采用其他统计数相加所得
		//Map allmap = dao.countAll(starttime1, endtime1, cityId, gw_type, isSoftUp);
		// 升级结果
		List resultlist = dao.countResult(tableName, starttime1, endtime1, cityId ,vendorId, deviceModelId, gw_type,awifi_type, isSoftUp);
		
		if(LipossGlobals.inArea(Global.JSDX)){
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
			//	tableName, starttime1, endtime1, cityId ,vendorId, deviceModelId, gw_type,type, isSoftUp
				tmap.put("tableName", tableName);
				tmap.put("starttime1", starttime1);
				tmap.put("endtime1", endtime1);
//				tmap.put("vendorId", vendorId);
//				tmap.put("deviceModelId", deviceModelId);
//				tmap.put("gw_type", gw_type);
//				tmap.put("awifi_type", awifi_type);
//				tmap.put("isSoftUp", isSoftUp);
				
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
								// status= 100 && result_id = 1认为是成功
								if ("1".equals(StringUtil.getStringValue(rmap
										.get("result_id"))))
								{
									// 成功数
									successnum = successnum
											+ StringUtil.getLongValue(rmap.get("total"));
								}
								// 只要result not in 0/1/2 都认为是失败
								else if(!"0".equals(StringUtil.getStringValue(rmap.get("result_id")))&& !"1".equals(StringUtil.getStringValue(rmap.get("result_id")))&& !"2".equals(StringUtil.getStringValue(rmap.get("result_id"))))
								{
									// 失败（需重配）数
									failnum = failnum
											+ StringUtil.getLongValue(rmap.get("total"));
								}
							}
							else if ("0".equals(StringUtil.getStringValue(rmap.get("status"))))
							{
//								if ("0".equals(StringUtil.getStringValue(rmap
//										.get("result_id"))))
//								{
									// 未做数
									noupnum = noupnum
											+ StringUtil.getLongValue(rmap.get("total"));
//								}
//								else
//								{
//									if (!"1".equals(StringUtil.getStringValue(rmap
//											.get("result_id"))))
//									{
//										// 失败（下次上线再做）
//										nextnum = nextnum
//												+ StringUtil.getLongValue(rmap.get("total"));
//									}
//									else
//									{
//										allup = allup + StringUtil.getLongValue(rmap.get("total"));
//									}
//								}
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
		// 非江苏
		else {
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
				tmap.put("tableName", tableName);
				tmap.put("starttime1", starttime1);
				tmap.put("endtime1", endtime1);
//				tmap.put("vendorId", vendorId);
//				tmap.put("deviceModelId", deviceModelId);
//				tmap.put("gw_type", gw_type);
//				tmap.put("awifi_type", awifi_type);
//				tmap.put("isSoftUp", isSoftUp);
				
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
								if ("1".equals(StringUtil.getStringValue(rmap
										.get("result_id"))))
								{
									// 成功数
									successnum = successnum
											+ StringUtil.getLongValue(rmap.get("total"));
								}
								else
								{
									// 失败（需重配）数
									failnum = failnum
											+ StringUtil.getLongValue(rmap.get("total"));
								}
							}
							else if ("0".equals(StringUtil.getStringValue(rmap.get("status"))))
							{
								if ("0".equals(StringUtil.getStringValue(rmap
										.get("result_id"))))
								{
									// 未做数
									noupnum = noupnum
											+ StringUtil.getLongValue(rmap.get("total"));
								}
								else
								{
									if (!"1".equals(StringUtil.getStringValue(rmap
											.get("result_id"))))
									{
										// 失败（下次上线再做）
										nextnum = nextnum
												+ StringUtil.getLongValue(rmap.get("total"));
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
				tmap.put("percent", percent(successnum, allup));
				list.add(tmap);
				tlist = null;
			}
		}
		cityMap = null;
		cityList = null;
		return list;
	}

	public String percent(long p1, long p2)
	{
		double p3;
		if (p2 == 0)
		{
			if(LipossGlobals.inArea(Global.NXDX)){
				return "0";
			}else {
				return "N/A";
			}
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

	public AWifiResultReportDAO getDao()
	{
		return dao;
	}

	public void setDao(AWifiResultReportDAO dao)
	{
		this.dao = dao;
	}

	public List<Map> getDevList(String tableName, String gw_type,String awifi_type,String starttime1, String endtime1, String cityId,
			String status, String resultId, String isMgr, int curPage_splitPage,
			int num_splitPage, String isSoftUp, String vendorId, String deviceModelId)
	{
		return dao.getDevList(tableName,gw_type,awifi_type,starttime1, endtime1, cityId, status, resultId, isMgr,
				curPage_splitPage, num_splitPage, isSoftUp,vendorId,deviceModelId);
	}

	public int getDevCount(String tableName, String gw_type,String awifi_type,String starttime1, String endtime1, String cityId,
			String status, String resultId, String isMgr, int curPage_splitPage,
			int num_splitPage,String isSoftUp,String vendorId, String deviceModelId)
	{
		return dao.getDevCount(tableName, gw_type,awifi_type,starttime1, endtime1, cityId, status, resultId, isMgr,
				curPage_splitPage, num_splitPage, isSoftUp,vendorId,deviceModelId);
	}

	public List<Map> getDevExcel(String tableName, String gw_type,String awifi_type,String starttime1, String endtime1, String cityId,
			String status, String resultId, String isMgr,String isSoftUp,String vendorId, String deviceModelId)
	{

		return dao.getDevExcel(tableName,gw_type,awifi_type,starttime1, endtime1, cityId, status, resultId, isMgr, isSoftUp,vendorId,deviceModelId);
	}
	
	/**
	 * 查询设备厂商
	 * @return
	 */
	public String getVendor(){
		logger.debug("GwDeviceQueryBIO=>getVendor()");
		List list = dao.getVendor();
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("vendor_id"));
			bf.append("$");
			bf.append(map.get("vendor_add"));
			bf.append("(");
			bf.append(map.get("vendor_name"));
			bf.append(")");
		}
		return bf.toString();
	}
	
	/**
	 * 查询设备型号
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModel(String vendorId){
		logger.debug("GwDeviceQueryBIO=>getDeviceModel(vendorId:{})",vendorId);
		List list = dao.getDeviceModel(vendorId);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("device_model_id"));
			bf.append("$");
			bf.append(map.get("device_model"));
		}
		return bf.toString();
	}
}
