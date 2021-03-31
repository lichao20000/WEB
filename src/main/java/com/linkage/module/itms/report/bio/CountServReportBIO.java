
package com.linkage.module.itms.report.bio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.dao.CountServReportDAO;

/**
 * 按开始时间、结束时间、设备属地、用户业务类型、是否活跃统计
 * 
 * @author wanghong5 2015-02-03
 */
@SuppressWarnings("unchecked")
public class CountServReportBIO{
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(CountServReportBIO.class);
	private CountServReportDAO countServReportDao;
	
	public CountServReportBIO(){		
	}
	
	/*
	 * 统计数据绑定
	 */
	public List<Map> countAll(String cityId,String starttime, String endtime,String specId, String is_active) {
			logger.warn("CountServReportBIO.countAll()--start");
			List<Map<String,String>> countList = new ArrayList<Map<String,String>>();
			List<Map> list = new ArrayList<Map>();
			List<Map<String,String>> countListByType = new ArrayList<Map<String,String>>();
			
			long starttimeCount = startTimeOfMonth(new Date());
			long endtimeCount = endTimeOfMonth(new Date());
			if(!"".equals(starttime) && starttime != null){
				starttimeCount = DateUtil.getTimeInSecond(starttime, "yyyy-MM-dd HH:mm:ss");
			}
			if(!"".equals(endtime) && endtime != null){
				endtimeCount = DateUtil.getTimeInSecond(endtime, "yyyy-MM-dd HH:mm:ss");
			}
			
			countList=countServReportDao.getCountAllList(cityId,starttimeCount, endtimeCount,  specId, is_active);
			countListByType=countServReportDao.getCountAllListByType(cityId,starttimeCount, endtimeCount,  specId, is_active);
			
			ArrayList<String> cityArray = CityDAO.getNextCityIdsByCityPid(cityId);
			if(cityArray != null && cityArray.size() > 0){
				for(String city_id:cityArray){
					long totalNum = 0;
					long kuandaiNum = 0;
					long IPTVNum = 0;
					long VOIPNum = 0;
					double kuandai_scale = 0;
					double IPTV_scale = 0;
					double VOIP_scale = 0;
					
					String city_name = CityDAO.getCityName(city_id);
					if("00".equals(city_id)){
						city_name = "江苏省中心";
					}
					
					ArrayList<String> cityArrays = CityDAO.getAllNextCityIdsByCityPid(city_id);
					
					if(countList != null && countList.size() > 0){
						for(Map tmp:countList){
							String city = StringUtil.getStringValue(tmp,"city_id");
							if(city!=null && cityArrays.contains(city)){
								long num = StringUtil.getLongValue(tmp, "num");	
								totalNum += num;
								num=0;
							}
							city=null;
							tmp = null;
						}
					}
					
					if(countListByType != null && countListByType.size() > 0){
						for(Map tmp1:countListByType){
							String city1 = StringUtil.getStringValue(tmp1,"city_id");
							String serv_type_id = StringUtil.getStringValue(tmp1,"serv_type_id");
							long num = StringUtil.getLongValue(tmp1, "num");
							if(city1!=null && cityArrays.contains(city1)){
								if("10".equals(serv_type_id)){	
									kuandaiNum += num;
									num=0;
								}else if("11".equals(serv_type_id)){
									IPTVNum += num;
									num=0;
								}else if("14".equals(serv_type_id)){
									VOIPNum += num;
									num=0;
								}
							}
							city1=null;
							serv_type_id=null;
							tmp1 = null;
						}
					}
					
					Map map = new HashMap();
					if(totalNum==0 ){
						int num_scal=0;
						map.put("kuandai_scale", num_scal);
						map.put("IPTV_scale", num_scal);
						map.put("VOIP_scale", num_scal);
					}else{
						if(kuandaiNum==0){
							int num_scal=0;
							map.put("kuandai_scale", num_scal);
						}else{
							kuandai_scale=kuandaiNum*100/totalNum;
							map.put("kuandai_scale", kuandai_scale+"%");
						}
						if(IPTVNum==0){
							int num_scal=0;
							map.put("IPTV_scale", num_scal);
						}else{
							IPTV_scale=IPTVNum*100/totalNum;
							map.put("IPTV_scale", IPTV_scale+"%");
						}
						if(VOIPNum==0){
							int num_scal=0;
							map.put("VOIP_scale", num_scal);
						}else{
							VOIP_scale=VOIPNum*100/totalNum;
							map.put("VOIP_scale", VOIP_scale+"%");
						}
					}
					map.put("cityId", cityId);
					map.put("city_id", city_id);
					map.put("city_name", city_name);
					map.put("totalNum", totalNum);
					map.put("kuandaiNum", kuandaiNum);
					map.put("IPTVNum", IPTVNum);
					map.put("VOIPNum", VOIPNum);
					
					map.put("starttime", starttime);
					map.put("endtime", endtime);
					map.put("starttimeCount", starttimeCount);
					map.put("endtimeCount", endtimeCount);
					map.put("specId", specId);
					map.put("is_active", is_active);
						
					list.add(map);
				
					totalNum = 0;
					kuandaiNum = 0;
					IPTVNum = 0;
					VOIPNum = 0;
					kuandai_scale = 0;
					IPTV_scale = 0;
					VOIP_scale = 0;
					map = null;
					city_name = null;
					cityArrays=null;
			}
			cityArray = null;
			countList = null;
			countListByType=null;
		}
		return list;	
	}
	
	/*
	 * 每页绑定数据
	 */
	public List<Map> getDetailsForPage(String city_id,String starttime ,String endtime , String specId, 
				String is_active,String serv_type_id,int curPage_splitPage, int num_splitPage) {
		logger.warn("CountServReportBIO.getDetailsForPage()--start"+is_active);
		long starttimeCount = startTimeOfMonth(new Date());
		long endtimeCount = endTimeOfMonth(new Date());
		if(!"".equals(starttime) && starttime != null){
			starttimeCount = DateUtil.getTimeInSecond(starttime, "yyyy-MM-dd HH:mm:ss");
		}
		if(!"".equals(endtime) && endtime != null){
			endtimeCount = DateUtil.getTimeInSecond(endtime, "yyyy-MM-dd HH:mm:ss");
		}
		
		List<Map> list=countServReportDao.getDetailsForPage(city_id,starttimeCount, endtimeCount,
								 specId, is_active,serv_type_id, curPage_splitPage, num_splitPage);
		for(Map map:list){
				map.put("city_id",city_id);
				map.put("starttimeCount",starttimeCount);
				map.put("endtimeCount",endtimeCount);
				map.put("specId",specId);
				map.put("is_active",is_active);
				map.put("serv_type_id",serv_type_id);
		}
		return list;
	}
	
	/*
	 * 获取最大页数
	 */
	public int getDetailsCount(String city_id,String starttime ,String endtime,
			 String specId, String is_active,String serv_type_id,int num_splitPage) {
		logger.warn("CountServReportBIO.getDetailsCount()--start"+is_active);
		int maxPage = 1;
		long starttimeCount = startTimeOfMonth(new Date());
		long endtimeCount = endTimeOfMonth(new Date());
		if(!"".equals(starttime) && starttime != null){
			starttimeCount = DateUtil.getTimeInSecond(starttime, "yyyy-MM-dd HH:mm:ss");
		}
		if(!"".equals(endtime) && endtime != null){
			endtimeCount = DateUtil.getTimeInSecond(endtime, "yyyy-MM-dd HH:mm:ss");
		}
		maxPage = countServReportDao.getDetailsCount(city_id,starttimeCount,endtimeCount,specId,is_active,serv_type_id,num_splitPage);
		return maxPage;
	}
	
	/*
	 * 导出数据excel表
	 */
	public List<Map> getDetailsExcel(String city_id,String starttime, String endtime,
			 String specId, String is_active,String serv_type_id) {
		logger.warn("CountServReportBIO.getDetailsExcel()--start"+is_active);
		long starttimeCount = startTimeOfMonth(new Date());
		long endtimeCount = endTimeOfMonth(new Date());
		if(!"".equals(starttime) && starttime != null){
			starttimeCount = DateUtil.getTimeInSecond(starttime, "yyyy-MM-dd HH:mm:ss");
		}
		if(!"".equals(endtime) && endtime != null){
			endtimeCount = DateUtil.getTimeInSecond(endtime, "yyyy-MM-dd HH:mm:ss");
		}
		
		List<Map> lis =new ArrayList<Map>();
		List<Map> list=countServReportDao.getDetailsExcel(city_id,starttimeCount,endtimeCount,specId,is_active,serv_type_id);
		
		for(Map map:list){
			//设备序列号
			String deviceNum = StringUtil.getStringValue(map,"devicenum");
			//LOID
			String LOID = StringUtil.getStringValue(map,"loid");
			//业务名称
			String operationName="宽带业务";
			if("11".equals(StringUtil.getStringValue(map,"serv_type_id"))){
				operationName ="IPTV业务";
			}else if("14".equals(StringUtil.getStringValue(map,"serv_type_id"))){
				operationName ="VOIP业务";
			}
			//业务账号
			String operationNum = StringUtil.getStringValue(map,"operationnum");
			Map tmap = new HashMap();
			// 将dealdate转换成时间
			try{
				long dealdate = StringUtil.getLongValue(map,"dealdate");
				DateTimeUtil dt = new DateTimeUtil(dealdate * 1000);
				tmap.put("dealdate", dt.getLongDate());
			}catch (Exception e){
				tmap.put("dealdate", "");
			}
			
			tmap.put("city_id", city_id);
			tmap.put("starttime", starttime);
			tmap.put("endtime", endtime);
			tmap.put("specId", specId);
			tmap.put("is_active", is_active);
			tmap.put("starttimeCount",starttimeCount);
			tmap.put("endtimeCount",endtimeCount);
			tmap.put("specId",specId);
			tmap.put("is_active",is_active);
			tmap.put("deviceNum", deviceNum);
			tmap.put("LOID", LOID);
			tmap.put("operationName",operationName);
			tmap.put("operationNum", operationNum);
			lis.add(tmap);
		}
		return lis;
	}
		
	
	/*
	 * 开始日期格式转换  yyyy-mm-dd --> 毫秒
	 */
	public static long startTimeOfMonth(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		System.out.println(cal.getTime());
		
		return cal.getTimeInMillis() / 1000;
	}
	
	/*
	 * 结束日期转换  yyyy-mm-dd --> 毫秒
	 */
	public static long endTimeOfMonth(Date date){
		// 将当前月设置为第一天后，加1个月，然后-1秒。
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		System.out.println(cal.getTime());
		
		return cal.getTimeInMillis() / 1000 - 1;
	}
	
	public CountServReportDAO getCountServReportDao() {
		return countServReportDao;
	}

	public void setCountServReportDao(CountServReportDAO countServReportDao) {
		this.countServReportDao = countServReportDao;
	}

}

