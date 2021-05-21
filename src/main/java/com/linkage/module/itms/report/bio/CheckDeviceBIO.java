package com.linkage.module.itms.report.bio;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.text.DecimalFormat;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.dao.CheckDeviceDAO;

public class CheckDeviceBIO {
    private static Logger logger = LoggerFactory
            .getLogger(CheckDeviceBIO.class);
    private CheckDeviceDAO checkDeviceDAO;

    
    public static void main(String[] args)
	{
    	DecimalFormat dF = new DecimalFormat("0.00");
    	int a = 0;
    	int b = 1;
		System.out.println(dF.format(100*(float)a/b));
	}
    public List<Map<String, Object>> queryDataList(String cityId,
                                                   String checkState, String gwType) {
        logger.debug("queryDataList({},{},{})", cityId, checkState);

        List<Map<String, Object>> returnValue = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> tempValue = checkDeviceDAO.getDataList(cityId, checkState, gwType);

        // 按属地统计
        
        Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
        // 根据属地cityId获取下一层属地ID(包含自己)
        ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
        
        String undo = "";
        String mapCity = "";
        if (null != cityList && !cityList.isEmpty()) {
            for (String cityIdN : cityList) {
            	Map<String, Object> returnMap = new HashMap<String, Object>();
            	returnMap.put("cityname", cityMap.get(cityIdN));
            	returnMap.put("city", cityIdN);
            	returnMap.put("undo", 0);
            	returnMap.put("succ", 0);
            	returnMap.put("fail", 0);
            	returnMap.put("rate", "");
            	
                // 所有子属地ID(包括自己)
            	for(Map<String, Object> tempOne : tempValue){
            		mapCity = StringUtil.getStringValue(tempOne, "city_id", "");
            		String checkstate = StringUtil.getStringValue(tempOne, "checkstate", "");
            		if(cityIdN.equals(mapCity)){
            			if("0".equals(checkstate)){
            				returnMap.put("undo", StringUtil.getIntValue(tempOne, "num", 0));
            			}
            			else if("1".equals(checkstate)){
            				returnMap.put("succ", StringUtil.getIntValue(tempOne, "num", 0));
            			}
            			else if("2".equals(checkstate)){
            				returnMap.put("fail", StringUtil.getIntValue(tempOne, "num", 0));
            			}
            		}
            		
            	}
            	
            	DecimalFormat dF = new DecimalFormat("0.00");
            	
            	int succ = StringUtil.getIntValue(returnMap, "succ", 0);
            	int fail = StringUtil.getIntValue(returnMap, "fail", 0);
            	String rate = dF.format(100*(float)succ/(succ+fail));
            	if(succ+fail<=0) rate = "0";
            	returnMap.put("rate", rate + "%");
            	returnValue.add(returnMap);
            }
        }

        return returnValue;
    }

   
    public List<Map> getServInfoDetail(String cityId, String checkState, int curPageSplitPage, int numSplitPage) {
        return checkDeviceDAO.getServInfoDetail(cityId, checkState, curPageSplitPage, numSplitPage);
    }

    public int getServInfoCount(String cityId, String checkState, int curPageSplitPage, int numSplitPage) {
        return checkDeviceDAO.getServInfoCount(cityId, checkState, curPageSplitPage, numSplitPage);
    }

    public List<Map<String, Object>> getServInfoExcel(String cityId,String checkState) {
        return checkDeviceDAO.getServInfoExcel(cityId, checkState);
    }

    /**
     * 计算百分比
     *
     * @param p1 分子
     * @param p2 分母
     * @return
     */
    public String percent(long p1, long p2) {

        logger.debug("percent({},{})", p1, p2);

        double p3;
        if (p2 == 0) {
            if (LipossGlobals.inArea(Global.NXDX)) {
                return "0.00%";
            } else {
                return "N/A";
            }
        } else {
            p3 = (double) p1 / p2;
        }
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);
        return nf.format(p3);
    }
	
	public CheckDeviceDAO getCheckDeviceDAO()
	{
		return checkDeviceDAO;
	}
	
	public void setCheckDeviceDAO(CheckDeviceDAO checkDeviceDAO)
	{
		this.checkDeviceDAO = checkDeviceDAO;
	}



}
