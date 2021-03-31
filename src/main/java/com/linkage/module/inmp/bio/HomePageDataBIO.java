package com.linkage.module.inmp.bio;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.inmp.dao.HomePageDataDao;

public class HomePageDataBIO {

	private static Logger logger = LoggerFactory.getLogger(HomePageDataBIO.class);
	
	private HomePageDataDao dao; 
	
	/**
	 * 
	 * @param flag 饼图报表类型 1:终端，2：用户，3：版本，4：管控终端
	 * @return
	 */
	public String getPieData(String flag){
		logger.warn("HomePageDataBIO ====> getPieData({})",flag);
		StringBuffer json = new StringBuffer("[");
		List<Map<String,String>> list = dao.getPieData(flag);
		if(list != null && list.size() > 0){
			logger.warn("HomePageDataBIO ====> getPieData({}) ==获取到数据",flag);
			String count_type = "";
			String total_count = "";
			String active_count = "";
			String noactive_count = "";
			for(Map<String,String> tmpMap : list){
				count_type = StringUtil.getStringValue(tmpMap, "count_type");
				total_count = StringUtil.getStringValue(tmpMap, "total_count");
				active_count = StringUtil.getStringValue(tmpMap, "active_count");
				noactive_count = StringUtil.getStringValue(tmpMap, "no_active_count");
				String tmps = getPieJson(count_type,total_count,active_count,noactive_count,flag);
				json.append(tmps);
				json.append(",");
				count_type = "";
				total_count = "";
				active_count = "";
				noactive_count = "";
			}
			if(json.toString().endsWith(",")){
				json = json.deleteCharAt(json.length() - 1);;
			}
		}else{
			logger.warn("HomePageDataBIO ====> getPieData({}) ==未获取到数据",flag);
			String count_type = "";
			String total_count = "0";
			String active_count = "0";
			String noactive_count = "0";
			for(int i = 1; i <= 3; i++){
				count_type = i + "";
				String tmps = getPieJson(count_type,total_count,active_count,noactive_count,flag);
				json.append(tmps);
				json.append(",");
				count_type = "";
			}
			if(json.toString().endsWith(",")){
				json = json.deleteCharAt(json.length() - 1);;
			}
		
		}
		
		json.append("]");
		
		return json.toString();
	}
	
	public String getAutoBindGaugeData(String cityId,String year,String month){
		logger.warn("HomePageDataBIO ====> getAutoBindGaugeData({},{},{})",new Object[]{cityId,year,month});
		StringBuffer json = new StringBuffer("{\"year\":");
		StringBuffer totalCount = new StringBuffer("[");
		StringBuffer monthCount = new StringBuffer("[");
		
		long total_count = 0;
		long bind_count = 0;
		
		long month_count = 0;
		long month_total = 0;
		
		String yearRate = "0";
		String monthRate = "0";
		List<Map<String,String>> list = dao.getAutoBindGaugeDataForYear(cityId,year);
		if(list != null && list.size() > 0){
			logger.warn("getAutoBindBarData() ===> 获取到数据");
			for(Map<String,String> map : list){
				total_count += StringUtil.getLongValue(map, "total_count");
				bind_count += StringUtil.getLongValue(map, "bind_count");
				if(month.equals(StringUtil.getStringValue(map, "count_month"))){
					month_count += StringUtil.getLongValue(map, "bind_count");
					month_total += StringUtil.getLongValue(map, "total_count");
				}
			}
		}else{
			logger.warn("getAutoBindBarData() ===> 未获取到数据");
			total_count = 0;
			bind_count = 0;
			
			month_count = 0;
			month_total = 0;
		}
		
		yearRate = percent(bind_count,total_count);
		monthRate = percent(month_count,month_total);
		stringAppend(yearRate,totalCount);
		stringAppend(monthRate,monthCount);
		totalCount.append("]");
		monthCount.append("]");
		
		json.append(totalCount);
		json.append(",");
		json.append("\"month\":");
		json.append(monthCount);
		json.append("}");
		 
		return json.toString();
	}
	
	public String getAutoBindBarData(String cityId,String year,String month,String sort){
		logger.warn("HomePageDataBIO ====> getAutoBindBarData()");
		StringBuffer json = new StringBuffer("{\"cities\":");
		StringBuffer cityName = new StringBuffer("[");
		StringBuffer totalCount = new StringBuffer("[");
		StringBuffer bindCount = new StringBuffer("[");
		
		List<String> cityIds = new ArrayList<String>();
		//获取江苏省各地市
		cityIds = CityDAO.getNextCityIdsByCityPid(cityId);
		
		List<Map<String,String>> list = dao.getAutoBindBarData(cityId,year,month,sort);
		if(list != null && list.size() > 0){
			logger.warn("getAutoBindBarData() ===> 获取到数据");
			for(Map<String,String> map : list){
				String city_id = "";
				long total_count = 0;
				long bind_count = 0;
				String city_name = "";
				city_id = StringUtil.getStringValue(map, "city_id");
				total_count += StringUtil.getLongValue(map, "total_count");
				bind_count += StringUtil.getLongValue(map, "bind_count");
				city_name = Global.G_CityId_CityName_Map.get(city_id);
				cityName = stringAppend(city_name,cityName).append(",");
				totalCount = stringAppend(total_count+"",totalCount).append(",");
				bindCount = stringAppend(bind_count+"",bindCount).append(",");
				city_name = "";
			}
			if(cityName.toString().endsWith(",")){
				cityName = cityName.deleteCharAt(cityName.length() - 1);
			}
			if(totalCount.toString().endsWith(",")){
				totalCount = totalCount.deleteCharAt(totalCount.length() - 1);
			}
			if(bindCount.toString().endsWith(",")){
				bindCount = bindCount.deleteCharAt(bindCount.length() - 1);
			}
		}else{
			logger.warn("getAutoBindBarData() ===> 未获取到数据");
			Collections.sort(cityIds);
			String total_count = "0";
			String bind_count = "0";
			String city_name = "";
			for(String city_id : cityIds){
				city_name = Global.G_CityId_CityName_Map.get(city_id);
				cityName = stringAppend(city_name,cityName).append(",");
				totalCount = stringAppend(total_count,totalCount).append(",");
				bindCount = stringAppend(bind_count,bindCount).append(",");
				city_name = "";
			}
			if(cityName.toString().endsWith(",")){
				cityName = cityName.deleteCharAt(cityName.length() - 1);
			}
			if(totalCount.toString().endsWith(",")){
				totalCount = totalCount.deleteCharAt(totalCount.length() - 1);
			}
			if(bindCount.toString().endsWith(",")){
				bindCount = bindCount.deleteCharAt(bindCount.length() - 1);
			}
		}
		cityName.append("]");
		totalCount.append("]");
		bindCount.append("]");
		
		json.append(cityName);
		json.append(",");
		json.append("\"total\":");
		json.append(totalCount);
		json.append(",");
		json.append("\"bind\":");
		json.append(bindCount);
		json.append("}");
		 
		return json.toString();
	}
	
	
	public String getBussGaugeData(String cityId,String year,String month){
		logger.warn("HomePageDataBIO ====> getBussGaugeData({},{},{})",new Object[]{cityId,year,month});
		StringBuffer json = new StringBuffer("{\"year\":");
		StringBuffer totalCount = new StringBuffer("[");
		StringBuffer monthCount = new StringBuffer("[");
		
		double total_rate = 0;
		
		double month_rate = 0;
		
		String yearRate = "0";
		String monthRate = "0";
		List<Map<String,String>> list = dao.getBussGaugeDataForYear(cityId,year);
		if(list != null && list.size() > 0){
			logger.warn("getBussGaugeData() ===> 获取到数据");
			int monthSize = 0;
			for(Map<String,String> map : list){
				total_rate += StringUtil.getDoubleValue(map, "total_rate");
				if(month.equals(StringUtil.getStringValue(map, "count_month"))){
					month_rate += StringUtil.getDoubleValue(map, "total_rate");
					monthSize++;
				}
			}
			yearRate = percent(total_rate,list.size());
			monthRate = percent(month_rate,monthSize);
		}else{
			logger.warn("getBussGaugeData() ===> 未获取到数据");
		}
		
		stringAppend(yearRate,totalCount);
		stringAppend(monthRate,monthCount);
		totalCount.append("]");
		monthCount.append("]");
		
		json.append(totalCount);
		json.append(",");
		json.append("\"month\":");
		json.append(monthCount);
		json.append("}");
		 
		return json.toString();
	}
	
	public String getBussBarData(String cityId,String year,String month,String sort){
		logger.warn("HomePageDataBIO ====> getBussBarData({},{},{})",new Object[]{cityId,year,month});
		StringBuffer json = new StringBuffer("{\"cities\":");
		StringBuffer cityName = new StringBuffer("[");
		StringBuffer totalStr = new StringBuffer("[");
		StringBuffer valuesStr = new StringBuffer("[");
		List<Map<String,String>> list = dao.getBussBarData(cityId,year,month,sort);
		
		List<String> cityIds = new ArrayList<String>();
		//获取江苏省各地市
		cityIds = CityDAO.getNextCityIdsByCityPid(cityId);
		
		if(list != null && list.size() > 0){
			logger.warn("getBussBarData() ===>获取到数据");
					
			for(Map<String,String> map : list){
				String city_id = "";
				String city_name = "";
				double net_rate = 0;
				double itv_rate = 0;
				double voip_rate = 0;
				double total_rate = 0;
				city_id = StringUtil.getStringValue(map, "city_id");
				net_rate = StringUtil.getDoubleValue(map, "net_rate");
				itv_rate = StringUtil.getDoubleValue(map, "itv_rate");
				voip_rate = StringUtil.getDoubleValue(map, "voip_rate");
				total_rate = StringUtil.getDoubleValue(map, "total_rate");
				city_name = Global.G_CityId_CityName_Map.get(city_id);
				cityName = stringAppend(city_name,cityName).append(",");
				totalStr = intAppend(total_rate + "",totalStr).append(",");
				valuesStr = constructJson(net_rate+"",itv_rate+"",voip_rate+"",valuesStr).append(",");
				city_id = "";
				city_name = "";
				net_rate = 0;
				itv_rate = 0;
				voip_rate = 0;
				total_rate = 0;
			}
			if(cityName.toString().endsWith(",")){
				cityName = cityName.deleteCharAt(cityName.length() - 1);
			}
			if(totalStr.toString().endsWith(",")){
				totalStr = totalStr.deleteCharAt(totalStr.length() - 1);
			}
			if(valuesStr.toString().endsWith(",")){
				valuesStr = valuesStr.deleteCharAt(valuesStr.length() - 1);
			}
		}else{
			logger.warn("getBussBarData() ===> 未获取到数据");
			Collections.sort(cityIds);
			String city_name = "";
			double net_rate = 0;
			double itv_rate = 0;
			double voip_rate = 0;
			double total_rate = 0;
			for(String city_id : cityIds){
				city_name = Global.G_CityId_CityName_Map.get(city_id);
				cityName = stringAppend(city_name,cityName).append(",");
				totalStr = intAppend(total_rate + "",totalStr).append(",");
				valuesStr = constructJson(net_rate+"",itv_rate+"",voip_rate+"",valuesStr).append(",");
				city_name = "";
			}
			if(cityName.toString().endsWith(",")){
				cityName = cityName.deleteCharAt(cityName.length() - 1);
			}
			if(totalStr.toString().endsWith(",")){
				totalStr = totalStr.deleteCharAt(totalStr.length() - 1);
			}
			if(valuesStr.toString().endsWith(",")){
				valuesStr = valuesStr.deleteCharAt(valuesStr.length() - 1);
			}
		}
		
		cityName.append("]");
		totalStr.append("]");
		valuesStr.append("]");
		
		json.append(cityName);
		json.append(",");
		json.append("\"total\":");
		json.append(totalStr);
		json.append(",");
		json.append("\"value\":");
		json.append(valuesStr);
		json.append("}");
		
		return json.toString();
	}
	
	public String getPvcGaugeData(String cityId,String year,String month){
		logger.warn("HomePageDataBIO ====> getPvcGaugeData({},{},{})",new Object[]{cityId,year,month});
		StringBuffer json = new StringBuffer("{\"year\":");
		StringBuffer totalCount = new StringBuffer("[");
		StringBuffer monthCount = new StringBuffer("[");
		
		long deploy_count = 0;
		long nodeploy_count = 0;
		
		long month_count = 0;
		long month_nocount = 0;
		
		String yearRate = "0";
		String monthRate = "0";
		List<Map<String,String>> list = dao.getPvcGaugeDataForYear(cityId,year);
		if(list != null && list.size() > 0){
			logger.warn("getPvcGaugeData() ===> 获取到数据");
			for(Map<String,String> map : list){
				deploy_count += StringUtil.getLongValue(map, "deploy_count");
				nodeploy_count += StringUtil.getLongValue(map, "nodeploy_count");
				if(month.equals(StringUtil.getStringValue(map, "count_month"))){
					month_count += StringUtil.getLongValue(map, "deploy_count");
					month_nocount += StringUtil.getLongValue(map, "nodeploy_count");
				}
			}
		}else{
			logger.warn("getPvcGaugeData() ===> 未获取到数据");
			deploy_count = 0;
			nodeploy_count = 0;
			
			month_count = 0;
			month_nocount = 0;
		}
		
		yearRate = percent(deploy_count,deploy_count + nodeploy_count);
		monthRate = percent(month_count,month_count + month_nocount);
		stringAppend(yearRate,totalCount);
		stringAppend(monthRate,monthCount);
		totalCount.append("]");
		monthCount.append("]");
		
		json.append(totalCount);
		json.append(",");
		json.append("\"month\":");
		json.append(monthCount);
		json.append("}");
		 
		return json.toString();
	}
	
	public String getPvcBarData(String cityId,String year,String month,String sort){
		logger.warn("HomePageDataBIO ====> getPvcBarData({},{},{}) ",new Object[]{cityId,year,month});
		StringBuffer json = new StringBuffer("{\"cities\":");
		StringBuffer cityName = new StringBuffer("[");
		StringBuffer deployCount = new StringBuffer("[");
		StringBuffer nodeployCount = new StringBuffer("[");
		List<Map<String,String>> list = dao.getPvcBarData(cityId,year,month,sort);
		List<String> cityIds = new ArrayList<String>();
		//获取江苏省各地市
		cityIds = CityDAO.getNextCityIdsByCityPid(cityId);
		
		if(list != null && list.size() > 0){
			logger.warn("getPvcBarData() ===>获取到数据");
			for(Map<String,String> map : list){
				String city_id = "";
				long deploy_count = 0;
				long nodeploy_count = 0;
				String city_name = "";
				city_id = StringUtil.getStringValue(map, "city_id");
				deploy_count += StringUtil.getLongValue(map, "deploy_count");
				nodeploy_count += StringUtil.getLongValue(map, "nodeploy_count");
				city_name = Global.G_CityId_CityName_Map.get(city_id);
				cityName = stringAppend(city_name,cityName).append(",");
				deployCount = intAppend(deploy_count+"",deployCount).append(",");
				nodeployCount = intAppend(nodeploy_count+"",nodeployCount).append(",");
				city_id = "";
				city_name = "";
			}
			if(cityName.toString().endsWith(",")){
				cityName = cityName.deleteCharAt(cityName.length() - 1);
			}
			if(deployCount.toString().endsWith(",")){
				deployCount = deployCount.deleteCharAt(deployCount.length() - 1);
			}
			if(nodeployCount.toString().endsWith(",")){
				nodeployCount = nodeployCount.deleteCharAt(nodeployCount.length() - 1);
			}
		}else{
			logger.warn("getPvcBarData() ===> 未获取到数据");
			Collections.sort(cityIds);
			String deploy_count = "0";
			String nodeploy_count = "0";
			String city_name = "";
			for(String city_id : cityIds){
				city_name = Global.G_CityId_CityName_Map.get(city_id);
				cityName = stringAppend(city_name,cityName).append(",");
				deployCount = intAppend(deploy_count,deployCount).append(",");
				nodeployCount = intAppend(nodeploy_count,nodeployCount).append(",");
				city_name = "";
			}
			if(cityName.toString().endsWith(",")){
				cityName = cityName.deleteCharAt(cityName.length() - 1);
			}
			if(deployCount.toString().endsWith(",")){
				deployCount = deployCount.deleteCharAt(deployCount.length() - 1);
			}
			if(nodeployCount.toString().endsWith(",")){
				nodeployCount = nodeployCount.deleteCharAt(nodeployCount.length() - 1);
			}
		}
		
		cityName.append("]");
		deployCount.append("]");
		nodeployCount.append("]");
		
		json.append(cityName);
		json.append(",");
		json.append("\"deploy\":");
		json.append(deployCount);
		json.append(",");
		json.append("\"nodeploy\":");
		json.append(nodeployCount);
		json.append("}");
		
		return json.toString();
	}
	
	
	public String getVersionDevGaugeData(String cityId,String year,String month){
		logger.warn("HomePageDataBIO ====> getVersionDevGaugeData({},{},{})",new Object[]{cityId,year,month});
		StringBuffer json = new StringBuffer("{\"year\":");
		StringBuffer totalCount = new StringBuffer("[");
		StringBuffer monthCount = new StringBuffer("[");
		
		long stand_count = 0;
		long total_count = 0;
		
		long month_stand = 0;
		long month_total = 0;
		
		String yearRate = "0";
		String monthRate = "0";
		List<Map<String,String>> list = dao.getVersionDevGaugeDataForYear(cityId,year);
		if(list != null && list.size() > 0){
			logger.warn("getVersionDevGaugeData() ===> 获取到数据");
			for(Map<String,String> map : list){
				stand_count += StringUtil.getLongValue(map, "stand_count");
				total_count += StringUtil.getLongValue(map, "total_count");
				if(month.equals(StringUtil.getStringValue(map, "count_month"))){
					month_stand += StringUtil.getLongValue(map, "stand_count");
					month_total += StringUtil.getLongValue(map, "total_count");
				}
			}
		}else{
			logger.warn("getVersionDevGaugeData() ===> 未获取到数据");
			stand_count = 0;
			total_count = 0;
			
			month_stand = 0;
			month_total = 0;
		}
		
		yearRate = percent(stand_count,total_count);
		monthRate = percent(month_stand,month_total);
		stringAppend(yearRate,totalCount);
		stringAppend(monthRate,monthCount);
		totalCount.append("]");
		monthCount.append("]");
		
		json.append(totalCount);
		json.append(",");
		json.append("\"month\":");
		json.append(monthCount);
		json.append("}");
		 
		return json.toString();
	}
	
	public String getVersionDevBarData(String cityId,String year,String month,String sort){
		logger.warn("HomePageDataBIO ====> getVersionDevBarData({},{},{})",new Object[]{cityId,year,month});
		StringBuffer json = new StringBuffer("{\"cities\":");
		StringBuffer cityName = new StringBuffer("[");
		StringBuffer standCount = new StringBuffer("[");
		StringBuffer nostandCount = new StringBuffer("[");
		StringBuffer totalCount = new StringBuffer("[");
		List<Map<String,String>> list = dao.getVersionDevData(cityId,year,month,sort);
		List<String> cityIds = new ArrayList<String>();
		//获取江苏省各地市
		cityIds = CityDAO.getNextCityIdsByCityPid(cityId);
		if(list != null && list.size() > 0){
			logger.warn("getVersionDevBarData() ===>获取到数据");
			for(Map<String,String> map : list){
				String city_id = "";
				long stand_count = 0;
				long nostand_count = 0;
				long total_count = 0;
				String city_name = "";
				city_id = StringUtil.getStringValue(map, "city_id");
				stand_count += StringUtil.getLongValue(map, "stand_count");
				nostand_count += StringUtil.getLongValue(map, "nostand_count");
				total_count += StringUtil.getLongValue(map, "total_count");
				city_name = Global.G_CityId_CityName_Map.get(city_id);
				cityName = stringAppend(city_name,cityName).append(",");
				standCount = stringAppend(stand_count+"",standCount).append(",");
				nostandCount = stringAppend(nostand_count+"",nostandCount).append(",");
				totalCount = stringAppend(total_count+"",totalCount).append(",");
				city_name = "";
			}
			if(cityName.toString().endsWith(",")){
				cityName = cityName.deleteCharAt(cityName.length() - 1);
			}
			if(standCount.toString().endsWith(",")){
				standCount = standCount.deleteCharAt(standCount.length() - 1);
			}
			if(nostandCount.toString().endsWith(",")){
				nostandCount = nostandCount.deleteCharAt(nostandCount.length() - 1);
			}
			if(totalCount.toString().endsWith(",")){
				totalCount = totalCount.deleteCharAt(totalCount.length() - 1);
			}
		}else{
			logger.warn("getVersionDevBarData() ===> 未获取到数据");
			Collections.sort(cityIds);
			String stand_count = "0";
			String nostand_count = "0";
			String total_count = "0";
			String city_name = "";
			for(String city_id : cityIds){
				city_name = Global.G_CityId_CityName_Map.get(city_id);
				cityName = stringAppend(city_name,cityName).append(",");
				standCount = stringAppend(stand_count,standCount).append(",");
				nostandCount = stringAppend(nostand_count,nostandCount).append(",");
				totalCount = stringAppend(total_count,totalCount).append(",");
				city_name = "";
			}
			if(cityName.toString().endsWith(",")){
				cityName = cityName.deleteCharAt(cityName.length() - 1);
			}
			if(standCount.toString().endsWith(",")){
				standCount = standCount.deleteCharAt(standCount.length() - 1);
			}
			if(nostandCount.toString().endsWith(",")){
				nostandCount = nostandCount.deleteCharAt(nostandCount.length() - 1);
			}
			if(totalCount.toString().endsWith(",")){
				totalCount = totalCount.deleteCharAt(totalCount.length() - 1);
			}
		}
		
		cityName.append("]");
		standCount.append("]");
		nostandCount.append("]");
		totalCount.append("]");
		
		json.append(cityName);
		json.append(",");
		json.append("\"stand\":");
		json.append(standCount);
		json.append(",");
		json.append("\"nostand\":");
		json.append(nostandCount);
		json.append(",");
		json.append("\"total\":");
		json.append(totalCount);
		json.append("}");
		
		return json.toString();
	}

	public String getPieJson(String count_type,String total,String active_count,String noactive_count,String flag){
		String unit = "";
		String totals = total;
		String activeCount = active_count;
		String noactiveCount = noactive_count;
		if(Long.parseLong(total) > 10000){
			unit = "万";
			totals =  divide(Long.parseLong(total),10000);
			activeCount = divide(Long.parseLong(active_count),10000);
			noactiveCount = divide(Long.parseLong(noactive_count),10000);
		}
		StringBuffer tmp = new StringBuffer("{\"total\":\"");
		tmp.append(totals);
		tmp.append("\",");
		tmp.append("\"unit\":\"");
		tmp.append(unit);
		tmp.append("\",");
		if("1".equals(count_type)){
			tmp.append("\"E8C\":[{\"value\":\"");
		}else if("2".equals(count_type)){
			tmp.append("\"E8B\":[{\"value\":\"");
		}else if("3".equals(count_type)){
			tmp.append("\"HW\":[{\"value\":\"");
		}
		tmp.append(activeCount);
		if("3".equals(flag)){
			tmp.append("\",\"name\":\"规范\"},{\"value\":\"");
			tmp.append(noactiveCount);
			tmp.append("\",\"name\":\"非规范\"}]}");
		}else{
			tmp.append("\",\"name\":\"活跃\"},{\"value\":\"");
			tmp.append(noactiveCount);
			tmp.append("\",\"name\":\"非活跃\"}]}");
		}
		
		return tmp.toString();
	}
	
	public static StringBuffer stringAppend(String str, StringBuffer tmp){
		
		tmp.append("\"");
		tmp.append(str);
		tmp.append("\"");
		
		return tmp;
	}
	
	public static StringBuffer intAppend(String str, StringBuffer tmp){
		
		tmp.append("");
		tmp.append(str);
		tmp.append("");
		
		return tmp;
	}
	
	public static StringBuffer constructJson(String net, String itv, String voip,StringBuffer tmp){
		StringBuffer json = new StringBuffer("[");
		json.append(net);
		json.append(",");
		json.append(itv);
		json.append(",");
		json.append(voip);
		json.append("]");
		tmp.append(json);
		return tmp;
	}
	
	public String divide(long p1,long p2){
		
		java.text.DecimalFormat df =new java.text.DecimalFormat("#0.00");
		
		String avg = "0";
		if(p2 != 0 && p1 != 0){
			double tmp = (double)p1/p2;
			avg = df.format(tmp);
		}
		
		return avg;
	}
	
	public String percent(long p1, long p2) {
		logger.debug("percent({},{})", new Object[] { p1, p2 });

		double p3;
		if (p2 == 0) {
			return "00.00";
		} else {
			p3 = (double) p1 / p2;
		}
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p3);
		str = str.substring(0,str.indexOf("%"));
		return str;
	}
	
	
	public String percent(double p1, double p2) {
		logger.debug("percent({},{})", new Object[] { p1, p2 });

		double p3;
		if (p2 == 0) {
			return "00.00";
		} else {
			p3 = (double) p1 / p2;
		}
//		NumberFormat nf = NumberFormat.getPercentInstance();
//		nf.setMinimumFractionDigits(2);
//		String str = nf.format(p3);
//		str = str.substring(0,str.indexOf("%"));
		DecimalFormat df = new DecimalFormat("######0.00");  
		String str = df.format(p3);
		return str;
	}
	
	public String getAutoBindBarJosn(String city_name, String total_count, String bind_count){
		StringBuffer tmp = new StringBuffer();
		
		return tmp.toString();
	}
	
	public HomePageDataDao getDao() {
		return dao;
	}

	public void setDao(HomePageDataDao dao) {
		this.dao = dao;
	}
	
	public static void main(String args[]){
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("E8-C", "100");
//		map.put("E8-B", "200");
//		String s = JSONObject.toJSONString(map);
//		System.out.println(s);
		StringBuffer json = new StringBuffer("[");
		for(int i = 0; i < 2; i++){
			
			String tmps = new HomePageDataBIO().getPieJson(i+1 +"","310","110","200","1");
			json.append(tmps);
			json.append(",");
		}
		if(json.toString().endsWith(",")){
			json = json.deleteCharAt(json.length() - 1);;
		}
		
		json.append("]");
		System.out.println(json.toString());
		
		StringBuffer jsonBar = new StringBuffer("{\"cities\":");
		StringBuffer cityName = new StringBuffer("[");
		StringBuffer avgStr = new StringBuffer("[");
		StringBuffer valuesStr = new StringBuffer("[");
		
		String city_id = "";
		System.out.println(city_id);
		String city_name = "";
		double net_rate = 0;
		double itv_rate = 0;
		double voip_rate = 0;
		String avg = "";
		Map<String,String> mapt = null;
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		String[] s0 = {"江苏省中心", "南京", "苏州"};
		String[] s1 = {"98.65","98.14","99.50"};
		String[] s2 = {"97.13","95.26","96.05"};
		String[] s3 = {"93.23","94.01","91.56"};
		for(int k = 0; k < 3; k++){
			mapt = new HashMap<String,String>();
			mapt.put("city_id", s0[k]);
			mapt.put("net_rate", s1[k]);
			mapt.put("itv_rate", s2[k]);
			mapt.put("voip_rate", s3[k]);
			list.add(mapt);
		}
		
		for(Map<String,String> map : list){
			city_name = StringUtil.getStringValue(map, "city_id");
			net_rate = StringUtil.getDoubleValue(map, "net_rate");
			itv_rate = StringUtil.getDoubleValue(map, "itv_rate");
			voip_rate = StringUtil.getDoubleValue(map, "voip_rate");
			avg = "";//getAvg(net_rate,itv_rate,voip_rate);
			cityName = stringAppend(city_name,cityName).append(",");
			avgStr = stringAppend(avg,avgStr).append(",");
			valuesStr = constructJson(net_rate+"",itv_rate+"",voip_rate+"",valuesStr).append(",");
			city_id = "";
			city_name = "";
			net_rate = 0;
			itv_rate = 0;
			voip_rate = 0;
			avg = "";
		}
		if(cityName.toString().endsWith(",")){
			cityName = cityName.deleteCharAt(cityName.length() - 1);
		}
		if(avgStr.toString().endsWith(",")){
			avgStr = avgStr.deleteCharAt(avgStr.length() - 1);
		}
		if(valuesStr.toString().endsWith(",")){
			valuesStr = valuesStr.deleteCharAt(valuesStr.length() - 1);
		}
		
		
		cityName.append("]");
		avgStr.append("]");
		valuesStr.append("]");
		
		jsonBar.append(cityName);
		jsonBar.append(",");
		jsonBar.append("\"avg\":");
		jsonBar.append(avgStr);
		jsonBar.append(",");
		jsonBar.append("\"values\":");
		jsonBar.append(valuesStr);
		jsonBar.append("}");
		
		System.out.println(jsonBar.toString());
		
//		double tmp = Math.round((98.30 + 99.10 + 97.50)/3 * 100)/100.00;
//		System.out.println(tmp);
//		java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");  
//		
//		System.out.println(df.format((98.30 + 99.10 + 97.51)/3));
		double d1 = 0.00;
		double d2 = 0.00;
		if((d1 + d2) != 0.0){
			System.out.println(d1 + d2);
		}else{
			System.out.println("0");
		}
		
		
		int value = Math.round(1050190 / 10000 );
		System.out.println(value);
		double d = ((double)1056190 / 10000 );
		System.out.println(d );
		
		long l1 = 0;
		long l2 = 10000;
		String l3 = new HomePageDataBIO().divide(l1, l2);
		System.out.println(l3);
		
		java.text.DecimalFormat df =new java.text.DecimalFormat("#.00");
		double pie = 3.1415;
		df.format(pie);
		System.out.println(df.format(3.1415));
		double tmp = (double)l1/l2;
		
		avg = df.format(tmp);
		System.out.println(avg + "--avg");
		
		
		System.out.println(Math.round((935454 / 1000001) * 10000)%100);
		
		
	}
}
