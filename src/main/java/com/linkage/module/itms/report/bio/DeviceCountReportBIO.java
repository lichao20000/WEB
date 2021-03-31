
package com.linkage.module.itms.report.bio;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.resource.DeviceAct;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.dao.DeviceCountReportDAO;

/**
 * 设备统计BIO
 * 
 * @author liyl10
 */
@SuppressWarnings("unchecked")
public class DeviceCountReportBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(DeviceCountReportBIO.class);
	private DeviceCountReportDAO deviceCountReportDao;


	public DeviceCountReportDAO getDeviceCountReportDao() {
		return deviceCountReportDao;
	}

	public void setDeviceCountReportDao(DeviceCountReportDAO deviceCountReportDao) {
		this.deviceCountReportDao = deviceCountReportDao;
	}
	
	public List countDevice(String starttime,String endtime,String cityId,String accessstyle){
		logger.warn("DeviceCountReportBIO().countDevice({},{},{},{})",new Object[]{starttime,endtime,cityId,accessstyle});
		List countList = new ArrayList();
		List list = new ArrayList();
		long starttimeCount = startTimeOfMonth(new Date());
		long endtimeCount = endTimeOfMonth(new Date());
		if(!"".equals(starttime) && starttime != null){
			starttimeCount = DateUtil.getTimeInSecond(starttime, "yyyy-MM-dd HH:mm:ss");
		}
		if(!"".equals(endtime) && endtime != null){
			endtimeCount = DateUtil.getTimeInSecond(endtime, "yyyy-MM-dd HH:mm:ss");
		}
		
		countList = deviceCountReportDao.getDeviceNum(starttimeCount,endtimeCount,cityId,accessstyle);
		if(countList != null && countList.size() > 0){
			ArrayList<String> cityArray = CityDAO.getNextCityIdsByCityPid(cityId);
			if(cityArray != null && cityArray.size() > 0){
				for(int i = 0; i < cityArray.size(); i++){
					long adslNum = 0;
					long lanNum = 0;
					long eponNum = 0;
					long gponNum = 0;
					long totalNum = 0;
					Map map = new HashMap();
					Map adslMap = new HashMap();
					Map lanMap = new HashMap();
					Map eponMap = new HashMap();
					Map gponMap = new HashMap();
					String city_id = cityArray.get(i);
					String cityStr = city_id + "','";
					String city_name = CityDAO.getCityName(city_id);
					if("00".equals(city_id)){
						city_name = "总数";
					}
					ArrayList<String> cityArrays = CityDAO.getAllNextCityIdsByCityPid(city_id);
					if(cityArrays != null && cityArrays.size() > 1){
						cityStr = StringUtils.weave(cityArrays,"','");
					}
					cityStr = cityStr + "'";
					for(int j = 0; j < countList.size(); j++){
						Map tmp = (Map)countList.get(j);
						String city = StringUtil.getStringValue(tmp,"city_id");
						if(cityArrays.indexOf(city) > -1){
							int access_style_id = StringUtil.getIntValue(tmp,"access_style_id");
							long num = StringUtil.getLongValue(tmp, "num");
							if(access_style_id == 1){
								adslNum += num;
							}else if(access_style_id == 2){
								lanNum += num;
							}else if(access_style_id == 3){
								eponNum += num;
							}else if(access_style_id == 4){
								gponNum += num;
							}
							
							totalNum += num;
						}
						tmp = null;
					}
					
					
					map.put("city_id", city_id);
					map.put("city_name", city_name);
					map.put("adslNum", adslNum);
					map.put("lanNum", lanNum);
					map.put("eponNum", eponNum);
					map.put("gponNum", gponNum);
					map.put("totalNum", totalNum);
					list.add(map);
					
//					adslMap.put("city_id", city_id);
//					adslMap.put("city_name", city_name);
//					adslMap.put("access_style_id",1);
//					adslMap.put("access_style_name", "ADSL");
//					adslMap.put("num", adslNum);
//					list.add(adslMap);
//					
//					lanMap.put("city_id", city_id);
//					lanMap.put("city_name", city_name);
//					lanMap.put("access_style_id",2);
//					lanMap.put("access_style_name", "LAN");
//					lanMap.put("num", lanNum);
//					list.add(lanMap);
//					
//					eponMap.put("city_id", city_id);
//					eponMap.put("city_name", city_name);
//					eponMap.put("access_style_id",3);
//					eponMap.put("access_style_name", "EPON");
//					eponMap.put("num", eponNum);
//					list.add(eponMap);
//					
//					gponMap.put("city_id", city_id);
//					gponMap.put("city_name", city_name);
//					gponMap.put("access_style_id",4);
//					gponMap.put("access_style_name", "GPON");
//					gponMap.put("num", gponNum);
//					list.add(gponMap);
					
					adslNum =0;
					lanNum = 0;
					eponNum = 0;
					gponNum = 0;
					totalNum = 0;
					map = null;
					adslMap = null;
					lanMap = null;
					eponMap = null;
					gponMap = null;
					city_name = null;
				}
			}
			cityArray = null;
			countList = null;
		}
		return list;
	}
	
	public List countDeviceBak(String starttime,String endtime,String cityId,String accessstyle){
		logger.warn("DeviceCountReportBIO().countDevice({},{},{},{})",new Object[]{starttime,endtime,cityId,accessstyle});
		List countList = new ArrayList();
		List list = new ArrayList();
		long starttimeCount = startTimeOfMonth(new Date());
		long endtimeCount = endTimeOfMonth(new Date());
		if(!"".equals(starttime) && starttime != null){
			starttimeCount = DateUtil.getTimeInSecond(starttime, "yyyy-MM-dd HH:mm:ss");
		}
		if(!"".equals(endtime) && endtime != null){
			endtimeCount = DateUtil.getTimeInSecond(endtime, "yyyy-MM-dd HH:mm:ss");
		}
		
		countList = deviceCountReportDao.getDeviceNum(starttimeCount,endtimeCount,cityId,accessstyle);
		logger.warn("countList.size = " + countList.size());
		if(countList != null && countList.size() > 0){
			ArrayList<String> cityArray = CityDAO.getNextCityIdsByCityPid(cityId);
			logger.warn("cityArray.size==" + cityArray.size());
			if(cityArray != null && cityArray.size() > 0){
				for(int i = 0; i < cityArray.size(); i++){
					long totalnum = 0;
					Map map = new HashMap();
					String city_id = cityArray.get(i);
					logger.warn("city_id==" + city_id);
					String cityStr = city_id + "','";
					String city_name = CityDAO.getCityName(city_id);
					ArrayList<String> cityArrays = CityDAO.getAllNextCityIdsByCityPid(city_id);
					logger.warn("cityArrays.size==" + cityArrays.size());
					if(cityArrays != null && cityArrays.size() > 1){
						cityStr = StringUtils.weave(cityArrays,"','");
					}
					cityStr = cityStr + "'";
					logger.warn("cityStr==" + cityStr);
					String access_style_name = accessstyle;
					for(int j = 0; j < countList.size(); j++){
						Map tmp = (Map)countList.get(j);
						String city = StringUtil.getStringValue(tmp,"city_id");
						logger.warn("city==" + city);
						logger.warn(StringUtil.getLongValue(tmp, "num") + "---print");
						if(cityArrays.indexOf(city) > -1){
							access_style_name = StringUtil.getStringValue(tmp,"access_style_name");
							long num = StringUtil.getLongValue(tmp, "num");
							logger.warn("num===" + num);
							totalnum += num;
							logger.warn("totalnum===" + totalnum);
						}
						tmp = null;
					}
					
					if("1".equals(accessstyle)){
						access_style_name = "ADSL";
					}else if("2".equals(accessstyle)){
						access_style_name = "LAN";
					}else if("3".equals(accessstyle)){
						access_style_name = "EPON";
					}else if("4".equals(accessstyle)){
						access_style_name = "GPON";
					}
					
					map.put("city_id", city_id);
					map.put("city_name", city_name);
					map.put("access_style_id",accessstyle);
					map.put("access_style_name", access_style_name);
					map.put("num", totalnum);
					logger.warn("city_name=" + city_name + "--access_style_name=" + access_style_name + "--num=" + totalnum);
					list.add(map);
					totalnum = 0;
					map = null;
					city_name = null;
					access_style_name = null;
				}
			}
			cityArray = null;
			countList = null;
		}
		return list;
	}

	public List<Map<String,String>> getDetail(String cityId,String accesstype,String starttime,String endtime){
		logger.warn("DeviceCountReportBIO().getDetail({},{},{},{})",new Object[]{cityId,accesstype,starttime,endtime});
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		List<Map<String,String>> tmpList = new ArrayList<Map<String,String>>();
		
		long starttimeCount = startTimeOfMonth(new Date());
		long endtimeCount = endTimeOfMonth(new Date());
		if(!"".equals(starttime) && starttime != null){
			starttimeCount = DateUtil.getTimeInSecond(starttime, "yyyy-MM-dd HH:mm:ss");
		}
		if(!"".equals(endtime) && endtime != null){
			endtimeCount = DateUtil.getTimeInSecond(endtime, "yyyy-MM-dd HH:mm:ss");
		}
		tmpList = deviceCountReportDao.getDetail(cityId, accesstype, starttimeCount, endtimeCount);
		
		if(tmpList !=null && tmpList.size() > 0){
			DeviceAct devAct = new DeviceAct();
			//初始化地市信息
			Map<String,String> cityMap = devAct.getCityMap_All();
			//初始化厂商信息
			Map<String,String> vendormap = deviceCountReportDao.getVendorMap();
			//初始化型号信息
			Map<String,String> modelMap = deviceCountReportDao.getModelMap();
			//初始化版本信息
			Map<String,String> versionMap = deviceCountReportDao.getVersionMap();
			
			for(Map<String,String> tmp : tmpList){
				String cityname = StringUtil.getStringValue(cityMap,StringUtil.getStringValue(tmp,"city_id"));
				String vendorname = StringUtil.getStringValue(vendormap,StringUtil.getStringValue(tmp,"vendor_id"));
				String modelname = StringUtil.getStringValue(modelMap,StringUtil.getStringValue(tmp,"device_model_id"));
				String versionname = StringUtil.getStringValue(versionMap,StringUtil.getStringValue(tmp,"devicetype_id"));
				String time = getDateString(StringUtil.getStringValue(tmp,"opendate"));
				tmp.put("city_name", cityname);
				tmp.put("vendor_name", vendorname);
				tmp.put("device_model", modelname);
				tmp.put("softwareversion", versionname);
				tmp.put("time", time);
				list.add(tmp);
			}
		}
		return list;
	}
	
	
	public List<Map<String,String>> getDetailForPage(int curPage_splitPage,int num_splitPage,String cityId,String accesstype,String starttime,String endtime){
		logger.warn("DeviceCountReportBIO().getDetailForPage({},{},{},{},{},{})",new Object[]{curPage_splitPage,num_splitPage,cityId,accesstype,starttime,endtime});
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		List<Map<String,String>> tmpList = new ArrayList<Map<String,String>>();
		
		long starttimeCount = startTimeOfMonth(new Date());
		long endtimeCount = endTimeOfMonth(new Date());
		if(!"".equals(starttime) && starttime != null){
			starttimeCount = DateUtil.getTimeInSecond(starttime, "yyyy-MM-dd HH:mm:ss");
		}
		if(!"".equals(endtime) && endtime != null){
			endtimeCount = DateUtil.getTimeInSecond(endtime, "yyyy-MM-dd HH:mm:ss");
		}
		tmpList = deviceCountReportDao.getDetailForPage(curPage_splitPage,num_splitPage,cityId, accesstype, starttimeCount, endtimeCount);
		if(tmpList !=null && tmpList.size() > 0){
			DeviceAct devAct = new DeviceAct();
			//初始化地市信息
			Map<String,String> cityMap = devAct.getCityMap_All();
			//初始化厂商信息
			Map<String,String> vendormap = deviceCountReportDao.getVendorMap();
			//初始化型号信息
			Map<String,String> modelMap = deviceCountReportDao.getModelMap();
			//初始化版本信息
			Map<String,String> versionMap = deviceCountReportDao.getVersionMap();
			
			for(Map<String,String> tmp : tmpList){
				String cityname = StringUtil.getStringValue(cityMap,StringUtil.getStringValue(tmp,"city_id"));
				String vendorname = StringUtil.getStringValue(vendormap,StringUtil.getStringValue(tmp,"vendor_id"));
				String modelname = StringUtil.getStringValue(modelMap,StringUtil.getStringValue(tmp,"device_model_id"));
				String versionname = StringUtil.getStringValue(versionMap,StringUtil.getStringValue(tmp,"devicetype_id"));
				String time = getDateString(StringUtil.getStringValue(tmp,"complete_time"));
				tmp.put("city_name", cityname);
				tmp.put("vendor_name", vendorname);
				tmp.put("device_model", modelname);
				tmp.put("softwareversion", versionname);
				tmp.put("time", time);
				list.add(tmp);
			}
		}
		return list;
	}
	
	public int getDetailCount(int num_splitPage,String cityId,String accesstype,String starttime,String endtime){
		logger.warn("DeviceCountReportBIO().getDetailCount({},{},{},{},{},{})",new Object[]{num_splitPage,cityId,accesstype,starttime,endtime});
		int maxPage = 1;
		
		long starttimeCount = startTimeOfMonth(new Date());
		long endtimeCount = endTimeOfMonth(new Date());
		if(!"".equals(starttime) && starttime != null){
			starttimeCount = DateUtil.getTimeInSecond(starttime, "yyyy-MM-dd HH:mm:ss");
		}
		if(!"".equals(endtime) && endtime != null){
			endtimeCount = DateUtil.getTimeInSecond(endtime, "yyyy-MM-dd HH:mm:ss");
		}
		maxPage = deviceCountReportDao.getDetailCount(num_splitPage,cityId, accesstype, starttimeCount, endtimeCount);
		
		return maxPage;
	}
	
	public String percent(long p1, long p2){
		double p3;
		if (p2 == 0){
			return "N/A";
		}else{
			p3 = (double) p1 / p2;
		}
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p3);
		return str;
	}
	
	public static long startTimeOfMonth(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		System.out.println(cal.getTime());
		return cal.getTimeInMillis() / 1000;
	}

	public static long endTimeOfMonth(Date date)
	{
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
	
	public static long transTimeInSecond(String time, String timeFormat)
	{
		if (StringUtil.IsEmpty(time))
		{
			return 0;
		}
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
			Date date = sdf.parse(time);
			return date.getTime() / 1000;
		}
		catch (Exception e)
		{
			logger.error("parse time[{}] by time format[{}] error.", time, timeFormat);
			logger.error(e.getMessage(), e);
			return 0;
		}
	}
	
	public long timesBeforeDaysInSecond(String days)
	{
		if(days == null || "".equals(days)){
			return System.currentTimeMillis() /1000;
		}
		int day = Integer.parseInt(days);
		Calendar time = Calendar.getInstance();
		time.add(Calendar.DAY_OF_MONTH, -day);
		time.set(Calendar.HOUR_OF_DAY, 0);
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);
		return time.getTimeInMillis() / 1000;
	}
	
	public String getDateString(String time){
		
		long tmp = System.currentTimeMillis();
		if(time != null){
			tmp = Long.parseLong(time) * 1000;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tmp);
		c.getTime();
		
		return df.format(c.getTime());
	}
	
	
	public static void main(String args[]){
		System.out.println(startTimeOfMonth(new Date()));
		System.out.println(endTimeOfMonth(new Date()));
		
		long tmp = System.currentTimeMillis();
		String time = "1323223737";
		if(time != null){
			tmp = Long.parseLong(time) * 1000;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tmp);
		c.getTime();
		
		df.format(c.getTime());
		System.out.println(df.format(c.getTime()));
		
		String tmptime = "1417363199";
		System.out.println(new DeviceCountReportBIO().getDateString(tmptime));
	}

    public String getDualStack() {
			Map<String,String> resultMap = new HashMap<String, String>();
			resultMap = deviceCountReportDao.getDualStack();
			return JSON.toJSONString(resultMap);

    }
}
