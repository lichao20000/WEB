package com.linkage.module.itms.report.bio;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.itms.report.dao.HttpTestBatchReportDAO;
import com.linkage.module.itms.report.dao.HttpTestReportDAO;

/**
 * 
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2019年8月9日
 * @category com.linkage.module.itms.report.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class HttpTestBatchReportBIO
{
	private Logger logger = LoggerFactory.getLogger(HttpTestBatchReportBIO.class);
	private HttpTestBatchReportDAO dao;

	
    public List<HashMap<String,String>> qryHttpTestList(String cityId,int statCaliber,String startTime,String endTime){
    	logger.warn("HttpTestReportBIO ==> qryHttpTestList cityId:{},statCaliber:{},startTime:{},endTime:{}",new Object[]{cityId,statCaliber,startTime,endTime});
    	
    	List<HashMap<String, String>> cityList = dao.qryCurrCity(cityId);
    	if(null == cityList){
    		logger.warn("HttpTestReportBIO ==> qryHttpTestList cityList is null");
    		return null;
    	}
    	
    	List<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
    	HashMap<String,String> pageInfo = null;
    	Map<String,String> routeInfo = null;
    	Map<String,String> bridgeInfo = null;
    	for(HashMap<String, String> maps : cityList){
    		
    		String dbCityId = StringUtil.getStringValue(maps.get("city_id"));
    		String cityName = StringUtil.getStringValue(maps.get("city_name"));
    		if(StringUtil.IsEmpty(dbCityId)){
    			continue;
    		}
    		
    		routeInfo = dao.qryRouteList(dbCityId, statCaliber, startTime, endTime);
    		if(null == routeInfo || routeInfo.isEmpty() || routeInfo.size() < 1){
    			routeInfo = new HashMap<String,String>();
    			routeInfo.put("reached", "0");
    			routeInfo.put("noReached", "0");
    		}
    		bridgeInfo = dao.qryBridgedList(dbCityId, statCaliber, startTime, endTime);
    		if(null == bridgeInfo || bridgeInfo.isEmpty() || bridgeInfo.size() < 1){
    			bridgeInfo = new HashMap<String,String>();
    			bridgeInfo.put("reached", "0");
    			bridgeInfo.put("noReached", "0");
    		}
    		//组装返回数据的map
    		int reach = StringUtil.getIntegerValue(bridgeInfo.get("reached")) + StringUtil.getIntegerValue(routeInfo.get("reached"));
    		int noreach = StringUtil.getIntegerValue(bridgeInfo.get("noreached")) + StringUtil.getIntegerValue(routeInfo.get("noreached"));
    	    logger.warn(" cityName:{},reach:{},noreach:{}",new Object[]{cityName,reach,noreach});
    	    
    	    pageInfo = new HashMap<String,String>();
    		pageInfo.put("cityName", cityName);
    		pageInfo.put("cityId", dbCityId);
    	    pageInfo.put("reached", String.valueOf(reach));
    		pageInfo.put("noReached", String.valueOf(noreach));
    		int total = reach + noreach;
    		pageInfo.put("total", String.valueOf(total));
    		pageInfo.put("reachedRate", getPercentPar(reach, total));
    		result.add(pageInfo);
    	}
    	return result;
    }
    
    /**
     * 查询详细
     * @param cityId
     * @param type
     * @param statCaliber
     * @param startTime
     * @param endTime
     * @param curPage_splitPage
     * @param num_splitPage
     * @return
     */
    public List qryDetail(String cityId,String type,int statCaliber,String startTime,String endTime,
    		int curPage_splitPage,int num_splitPage){
    	logger.warn("HttpTestReportBIO ==>qryDetail cityId:{},type:{}",new Object[]{cityId,type});
    	return dao.qryDetail(cityId, type, statCaliber, startTime, endTime, curPage_splitPage, num_splitPage,true);
    }
    
    /**
     * 详细页面分页
     * @param cityId
     * @param type
     * @param statCaliber
     * @param startTime
     * @param endTime
     * @param curPage_splitPage
     * @param num_splitPage
     * @return
     */
    public List qryDetailNext(String cityId,String type,int statCaliber,String startTime,String endTime,
    		int curPage_splitPage,int num_splitPage){
    	logger.warn("HttpTestReportBIO ==>qryDetailNext cityId:{},type:{}",new Object[]{cityId,type});
    	return dao.qryDetailNext(cityId, type, statCaliber, startTime, endTime, curPage_splitPage, num_splitPage);
    }
    
    /**
     * 详细页面导出
     * @return
     */
    public List<HashMap<String,String>> qryDetailExcel(){
    	return dao.qryDetailExcel();
    }
    
    /**
     * 总数
     * @param cityId
     * @param type
     * @return
     */
    public int qryDetailCount(String cityId,String type){
    	 return dao.qryDetailCount();
    }
    
    public List<HashMap<String, String>> qryCity(String cityId){
    	return dao.qryCurrCity(cityId);
    }
	
    public String getPercentPar(int total,int finalTotal){
    	if(total == 0 || finalTotal == 0){
    		return "0.00%";
    	}
    	logger.warn("getPercentPar => total:{}，finalTotal:{}",new Object[]{total,finalTotal});
	     float percent = (float)total/finalTotal * 100; 
	     DecimalFormat format = new DecimalFormat("0.000000");
        logger.warn("getPercentPar=>{}",format.format(percent));
	     String head = String.valueOf(format.format(percent));
		 String percentStr = "";
		 int len = 0;
		 if(head.split("\\.").length == 2){
			 if(head.split("\\.")[1].length() < 2){
				 int max = 2 - head.split("\\.")[1].length();
				 for(int i = 0 ; i < max ; i++){
					 head = head + "0";
				 }
			 }
			 len = head.split("\\.")[0].length()+3;
		 }else{
			 len = head.length();
		 }
		 percentStr = head.substring(0, len) + "%";	
		 return percentStr;
    }
    
    public static void main(String args[]){
    	System.out.println(new HttpTestBatchReportBIO().getPercentPar(0, 0));
    }
    
	public HttpTestBatchReportDAO getDao()
	{
		return dao;
	}

	public void setDao(HttpTestBatchReportDAO dao)
	{
		this.dao = dao;
	}
}
