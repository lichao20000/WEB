package com.linkage.module.itms.report.bio;

import com.alibaba.fastjson.JSONObject;
import com.linkage.commons.util.StringUtil;
import com.linkage.commons.xml.XML;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.dao.VoipXIPBSSReportJLDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 吉林联通--修改语音IP工单统计报表
 * created by lingmin on 2020/5/6
 */
public class VoipXIPBSSReportJLBIO {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoipXIPBSSReportJLBIO.class);
    private VoipXIPBSSReportJLDAO reportJLDAO;

    /**
     * 获取语音业务更改IP工单接收情况统计
     * @param cityId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Map<String, String>> getVoipXIPBSSReport(String cityId, String startTime, String endTime){
        LOGGER.warn("enter getVoipXIPBSSReport with cityId:{},startTime:{},endTime:{}",cityId,startTime,endTime);
        //1、查询北向工单表 tab_bss_sheet 获取工单接收成功数和失败数
        //省中心改为查所有 update on 20200604
        cityId = cityId.equals("-1") || cityId.equals("00")? "" : cityId;
        //若属地不是省中心 则获取其子属地
        List<String> cityIdList = getChildCityList(cityId);
        List<Map<String, String>> reportList = reportJLDAO.getXVoipBSSReportByCityDB(cityId,startTime,endTime,cityIdList);
        if(reportList == null || reportList.size() == 0){
            LOGGER.warn("getVoipXIPBSSReport with get bssReportList null,cityId:{},startTime:{},endTime:{}",cityId,startTime,endTime);
            return new ArrayList<Map<String, String>>();
        }
        //2、解析 查询结果根据属地划分成功数、失败数、未绑定设备数 其中未绑定设备数归纳入成功数
        Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
        Map<String,Map<String, String>> cityReportMap = new HashMap<String, Map<String, String>>();
        getCityReportMap(reportList, cityMap, cityReportMap);
        LOGGER.warn("getVoipXIPBSSReport with end analysis,the cityReportMap:{}",cityReportMap);

        //3、若查询条件cityId为空或者cityId为父属地 则根据统计的各属地情况累计父属地统计情况
        if(StringUtil.IsEmpty(cityId)){
            //cityId为空 查全部的情况
            //省中心展示 累计所有总和
            Map<String, String> allObject = getCenterMapInit(cityReportMap.get("00"));
            Map<String,String> parentCityIdMap = CityDAO.getNextCityMapByCityPidCore("00");
            for (Map.Entry<String,String> entry : parentCityIdMap.entrySet()) {
                //获取该所有子属地id
                Map<String,String> childMap = CityDAO.getNextCityMapByCityPidCore(entry.getKey());
                //累计该childList对应的工单数 得到父属地的统计Map
                Map<String, String> parentReport = getParentReportMap(entry.getKey(), entry.getValue(), cityReportMap, childMap);
                cityReportMap.put(entry.getKey(),parentReport);
                allObject.put("successNum",getSumStr(allObject.get("successNum"),parentReport.get("successNum")));
                allObject.put("failNum",getSumStr(allObject.get("failNum"),parentReport.get("failNum")));
                allObject.put("total",getSumStr(allObject.get("total"),parentReport.get("total")));
                allObject.put("unBindNum",getSumStr(allObject.get("unBindNum"),parentReport.get("unBindNum")));
            }
            cityReportMap.put("00",allObject);
        }else {
            //查询条件为父属地 将该父属地的子属地统计情况累加
            Map<String,String> childCityMap = CityDAO.getNextCityMapByCityPidCore(cityId);
            Map<String, String> parentReport = getParentReportMap(cityId, cityMap.get(cityId), cityReportMap, childCityMap);
            cityReportMap.put(cityId,parentReport);
        }

        //4、返回 计算工单接收成功率
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        if(StringUtil.IsEmpty(cityId)){
            //4.1 查询条件为空 这里为了保证统计地市顺序 遍历cityId集合
            if(Global.G_CityIds == null){
                LOGGER.warn("getVoipXIPBSSReport with Global.G_CityIds is null");
                return new ArrayList<Map<String, String>>();
            }
            for(String cityIdOrigin : Global.G_CityIds){
                getResultList(cityMap, cityReportMap, resultList, cityIdOrigin);
            }
        }else {
            //4.2 查询条件为某父属地
            Map<String,String> parentReport = cityReportMap.get(cityId);
            getSuccessRate(parentReport);
            resultList.add(parentReport);
            for(String childCityId : cityIdList){
                if(childCityId.equals(cityId)){
                    continue;
                }
                getResultList(cityMap, cityReportMap, resultList, childCityId);
            }
        }
        LOGGER.warn("end getVoipXIPBSSReport with result:{}",resultList);
        return resultList;
    }

    private Map<String, String> getCenterMapInit(Map<String,String> centerReportMap) {
        Map<String,String> allObject = new HashMap<String, String>();
        allObject.put("cityId","00");
        allObject.put("cityName","省中心");
        allObject.put("successNum",centerReportMap == null ? "0" : centerReportMap.get("successNum"));
        allObject.put("failNum",centerReportMap == null ? "0" : centerReportMap.get("failNum"));
        allObject.put("total",centerReportMap == null ? "0" : centerReportMap.get("total"));
        allObject.put("unBindNum",centerReportMap == null ? "0" : centerReportMap.get("unBindNum"));
        return allObject;
    }
    private Map<String, String> getCenterDownMapInit(Map<String,String> cityReportMap) {
        Map<String,String> allObject = new HashMap<String, String>();
        allObject.put("cityId","00");
        allObject.put("cityName","省中心");
        allObject.put("successNum",cityReportMap == null ? "0" : cityReportMap.get("successNum"));
        allObject.put("failNum",cityReportMap == null ? "0" : cityReportMap.get("failNum"));
        return allObject;
    }

    private String getSumStr(String valueStr1,String valueStr2){
        return StringUtil.getStringValue(StringUtil.getIntegerValue(valueStr1) + StringUtil.getIntegerValue(valueStr2));
    }

    private void getResultList(Map<String, String> cityMap, Map<String, Map<String, String>> cityReportMap, List<Map<String, String>> resultList, String cityIdOrigin) {
        Map<String, String> report = cityReportMap.get(cityIdOrigin);
        Map<String, String> resultReport = new HashMap<String, String>();
        if (report == null) {
            resultReport.put("cityId", cityIdOrigin);
            resultReport.put("cityName", cityMap.get(cityIdOrigin));
            resultReport.put("total", "0");
            resultReport.put("successNum", "0");
            resultReport.put("failNum", "0");
            resultReport.put("unBindNum", "0");
            resultReport.put("successRate", "0");
            resultList.add(resultReport);
            return;
        }
        getSuccessRate(report);
        resultList.add(report);
    }

    private void getDownResultList(Map<String, String> cityMap, Map<String, Map<String, String>> cityReportMap, List<Map<String,
            String>> resultList, String cityIdOrigin) {
        Map<String, String> report = cityReportMap.get(cityIdOrigin);
        Map<String, String> resultReport = new HashMap<String, String>();
        if (report == null) {
            resultReport.put("cityId", cityIdOrigin);
            resultReport.put("cityName", cityMap.get(cityIdOrigin));
            resultReport.put("successNum", "0");
            resultReport.put("failNum", "0");
            resultReport.put("successRate", "0");
            resultList.add(resultReport);
            return;
        }

        getDownSuccessRate(report);
        resultList.add(report);
    }

    private void getSuccessRate(Map<String, String> report) {
        float total = Float.parseFloat(report.get("total"));
        if(0 - total == 0){
            report.put("successRate", "0");
            return;
        }
        float successNum = Float.parseFloat(report.get("successNum"));
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String successRate = decimalFormat.format((successNum / total) * 100);
        report.put("successRate", successRate + "%");
    }

    private void getDownSuccessRate(Map<String, String> report) {
        float successNum = Float.parseFloat(report.get("successNum"));
        float failNum = Float.parseFloat(report.get("failNum"));
        if(0 - (successNum + failNum) == 0){
            report.put("successRate", "0");
            return;
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String successRate = decimalFormat.format((successNum / (successNum + failNum)) * 100);
        report.put("successRate", successRate + "%");
    }

    private void getCityReportMap(List<Map<String, String>> reportList, Map<String, String> cityMap, Map<String, Map<String, String>> cityReportMap) {
        for(Map<String, String> report : reportList){
            String cityIdTemp = report.get("city_id");
            Map<String, String> temp = cityReportMap.get(cityIdTemp);
            String resultCode = String.valueOf(report.get("result"));
            String count = String.valueOf(report.get("count"));
            if(temp == null){
                report.put("successNum",resultCode.equals("0") || resultCode.equals("2") ? count : "0" );
                report.put("failNum",resultCode.equals("1") ? count : "0" );
                report.put("unBindNum",resultCode.equals("2") ? count : "0" );
                report.put("total",count);
                report.put("cityId",cityIdTemp);
                report.put("cityName", cityMap.get(cityIdTemp));
                cityReportMap.put(cityIdTemp,report);
            }else {
                int successNum = Integer.parseInt(temp.get("successNum"));
                temp.put("successNum",resultCode.equals("0") || resultCode.equals("2") ? String.valueOf(successNum + Integer.parseInt(count)) : temp.get("successNum"));
                int failNum = Integer.parseInt(temp.get("failNum"));
                temp.put("failNum",resultCode.equals("1") ? String.valueOf(Integer.parseInt(count) + failNum) : temp.get("failNum") );
                int unBindNum = Integer.parseInt(temp.get("unBindNum"));
                temp.put("unBindNum",resultCode.equals("2") ? String.valueOf(Integer.parseInt(count) + unBindNum) : temp.get("unBindNum") );
                temp.put("total",String.valueOf(Integer.parseInt(temp.get("total")) + Integer.parseInt(count)));
            }
        }
    }

    private Map<String, String> getParentReportMap(String cityId, String cityName, Map<String, Map<String, String>> cityReportMap, Map<String,String> childCityMap) {
        Map<String, String> parentReport = new HashMap<String, String>();
        parentReport.put("cityId", cityId);
        parentReport.put("cityName",cityName);
        int successNum = 0;
        int failNum = 0;
        int unBindNum = 0;
        int total = 0;
        for (Map.Entry<String,String> entry : childCityMap.entrySet()) {
            Map<String, String> childMap = cityReportMap.get(entry.getKey());
            if (childMap == null) {
                continue;
            }
            successNum = successNum + Integer.parseInt(childMap.get("successNum"));
            failNum = failNum + Integer.parseInt(childMap.get("failNum"));
            unBindNum = unBindNum + Integer.parseInt(childMap.get("unBindNum"));
            total = total + Integer.parseInt(childMap.get("total"));
        }
        parentReport.put("successNum", String.valueOf(successNum));
        parentReport.put("failNum", String.valueOf(failNum));
        parentReport.put("unBindNum", String.valueOf(unBindNum));
        parentReport.put("total", String.valueOf(total));
        return parentReport;
    }

    private Map<String, String> getParentDownReportMap(String cityId, String cityName, Map<String, Map<String, String>> cityReportMap, Map<String, String> childCityMap) {
        Map<String, String> parentReport = new HashMap<String, String>();
        parentReport.put("cityId", cityId);
        parentReport.put("cityName", cityName);
        int successNum = 0;
        int failNum = 0;
        for (Map.Entry<String,String> entry : childCityMap.entrySet()) {
            Map<String, String> childMap = cityReportMap.get(entry.getKey());
            if (childMap == null) {
                continue;
            }
            successNum = successNum + Integer.parseInt(childMap.get("successNum"));
            failNum = failNum + Integer.parseInt(childMap.get("failNum"));
        }

        parentReport.put("successNum", String.valueOf(successNum));
        parentReport.put("failNum", String.valueOf(failNum));
        return parentReport;
    }


    /**
     * 获取语音业务更改IP工单下发情况统计
     * @param cityId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Map<String, String>> getVoipBSSDownReport(String cityId, String startTime, String endTime){
        LOGGER.warn("enter getVoipBSSDownReport with cityId:{},startTime:{},endTime:{}",cityId,startTime,endTime);
        //1、查询策略表 gw_serv_strategy_batch 获取工单下发统计情况
        //省中心改为查所有 update on 20200604
        cityId = cityId.equals("-1") || cityId.equals("00") ? "" : cityId;
        //若属地不是省中心 则获取其子属地
        List<String> cityIdList = getChildCityList(cityId);
        List<Map<String, String>> servDownList = reportJLDAO.getXVoipBSSDownByCityDB(cityId, startTime, endTime,cityIdList);
        if(servDownList == null || servDownList.size() == 0){
            LOGGER.warn("getVoipBSSDownReport with get servDownList null,cityId:{},startTime:{},endTime:{}",cityId,startTime,endTime);
            return new ArrayList<Map<String, String>>();
        }
        //2、解析 查询结果根据属地划分下发成功数 失败数
        Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
        Map<String, Map<String, String>> cityReportMap = getDownCityReportMap(servDownList, cityMap);
        LOGGER.info("getVoipBSSDownReport with end analysis,the cityReportMap:{}",cityReportMap);

        //3、若查询条件cityId为空或者cityId为父属地 则根据统计的各属地情况累计父属地统计情况
        if(StringUtil.IsEmpty(cityId)){
            //cityId为空 查全部的情况
            //省中心展示 累计所有总和
            Map<String, String> allObject = getCenterDownMapInit(cityReportMap.get("00"));
            Map<String,String> parentCityMap = CityDAO.getNextCityMapByCityPidCore("00");
            for (Map.Entry<String,String> entry : parentCityMap.entrySet()) {
                //获取该所有子属地id
                Map<String,String> childMap = CityDAO.getNextCityMapByCityPidCore(entry.getKey());
                //累计该childList对应的工单数 得到父属地的统计Map
                Map<String, String> parentReport = getParentDownReportMap(entry.getKey(), entry.getValue(), cityReportMap, childMap);
                cityReportMap.put(entry.getKey(),parentReport);
                allObject.put("successNum",getSumStr(allObject.get("successNum"),parentReport.get("successNum")));
                allObject.put("failNum",getSumStr(allObject.get("failNum"),parentReport.get("failNum")));
            }
            cityReportMap.put("00",allObject);
        }else {
            //查询条件为父属地 将该父属地的子属地统计情况累加
            Map<String,String> childCityMap = CityDAO.getNextCityMapByCityPidCore(cityId);
            Map<String, String> parentReport = getParentDownReportMap(cityId, cityMap.get(cityId), cityReportMap, childCityMap);
            cityReportMap.put(cityId,parentReport);
        }

        //4、返回 计算工单下发成功率
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        if(StringUtil.IsEmpty(cityId)){
            //4.1 查询条件为空 这里为了保证统计地市顺序 遍历cityId集合
            if(Global.G_CityIds == null){
                LOGGER.warn("getVoipBSSDownReport with Global.G_CityIds is null");
                return new ArrayList<Map<String, String>>();
            }

            for(String cityIdOrigin : Global.G_CityIds){
                getDownResultList(cityMap, cityReportMap, resultList, cityIdOrigin);
            }
        }else {
            //4.2 查询条件为某父属地
            Map<String,String> parentReport = cityReportMap.get(cityId);
            getDownSuccessRate(parentReport);
            resultList.add(parentReport);
            for(String childCityId : cityIdList){
                if(childCityId.equals(cityId)){
                    continue;
                }
                getDownResultList(cityMap, cityReportMap, resultList, childCityId);
            }
        }
        LOGGER.warn("end getVoipBSSDownReport with result:{}",resultList);
        return resultList;

    }

    private Map<String, Map<String, String>> getDownCityReportMap(List<Map<String, String>> servDownList, Map<String, String> cityMap) {
        Map<String,Map<String, String>> cityReportMap = new HashMap<String, Map<String, String>>();
        for(Map<String, String> report : servDownList){
            String cityIdTemp = report.get("city_id");
            Map<String, String> temp = cityReportMap.get(cityIdTemp);
            String resultCode = String.valueOf(report.get("result_id"));
            String count = String.valueOf(report.get("count"));
            if(temp == null){
                report.put("successNum",resultCode.equals("1") ? count : "0" );
                report.put("failNum",!resultCode.equals("1") ? count : "0" );
                report.put("cityId",cityIdTemp);
                report.put("cityName", cityMap.get(cityIdTemp));
                cityReportMap.put(report.get("city_id"),report);

            }else {
                String successNum = temp.get("successNum");
                temp.put("successNum",resultCode.equals("1") ? String.valueOf(Integer.parseInt(count) + Integer.parseInt(successNum)): successNum);
                String failNum = temp.get("failNum");
                temp.put("failNum",!resultCode.equals("1") ? String.valueOf(Integer.parseInt(count) + Integer.parseInt(failNum)) : failNum );
            }
        }
        return cityReportMap;
    }

    private List<String> getChildCityList(String cityId) {
        List<String> cityIdList = new ArrayList<String>();
        if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
            cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
        }
        return cityIdList;
    }

    /**
     * 获取voip修改IP工单接收统计详情分页列表
     * @param cityId
     * @param startTime
     * @param endTime
     * @param type  工单接收状态 0-成功，1-失败，2-未绑定设备
     * @param currentPage
     * @param pageSize
     * @return
     */
    public List<Map<String, String>> getVoipBSSReportInfo(String cityId, String startTime, String endTime, String type, int currentPage, int pageSize){
        LOGGER.warn("enter getVoipBSSReportInfo with cityId:{},startTime:{},endTime:{}",cityId,startTime,endTime);
        //1、查询分页列表
        int startIndex = currentPage == -1 ? -1 : (currentPage - 1) * pageSize;
        int endIndex = currentPage == -1 ? -1 : startIndex + pageSize;
        List<Map<String, String>> resultList = reportJLDAO.getXVoipBSSInfoDB(cityId,startTime, endTime,type,startIndex,endIndex);
        if (resultList == null || resultList.size() == 0){
            LOGGER.warn("getVoipBSSReportInfo with resultList is null,cityId:{}",cityId);
            return new ArrayList<Map<String, String>>();
        }

        //2、解析 组装cityName IP
        Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
        for(Map<String, String> bss : resultList){
            bss.put("cityName", cityMap.get(bss.get("city_id")));
            bss.put("cityId",bss.get("city_id"));
            bss.put("loid",bss.get("username"));
            bss.put("failReason",bss.get("returnt_context"));
            //解析工单表的 sheet_context字段
            bss.put("ip",getIpByBssContext(bss.get("sheet_context")));
        }
        LOGGER.warn("end getVoipBSSReportInfo with resultList:{}",resultList);
        return resultList;

    }

    /**
     * 获取voip修改IP工单下发统计详情分页列表
     * @param cityId
     * @param startTime
     * @param endTime
     * @param type 下发结果 1-成功 其他-失败
     * @param currentPage
     * @param pageSize
     * @return
     */
    public List<Map<String,String>> getVoipBSSReportDownInfo(String cityId, String startTime, String endTime, String type, int currentPage, int pageSize){
        LOGGER.warn("enter getVoipBSSReportDownInfo with cityId:{},startTime:{},endTime:{}",cityId,startTime,endTime);
        //1、查询分页列表
        int startIndex = currentPage == -1 ? -1 : (currentPage - 1) * pageSize;
        int endIndex = currentPage == -1 ? -1 : startIndex + pageSize;
        List<Map<String, String>> resultList = reportJLDAO.getXVoipBSSDownInfoDB(cityId,startTime, endTime,type,startIndex,endIndex);
        if (resultList == null || resultList.size() == 0){
            LOGGER.warn("getVoipBSSReportDownInfo with resultList is null,cityId:{}",cityId);
            return new ArrayList<Map<String, String>>();
        }

        //2、解析 组装cityName IP
        Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
        for(Map<String, String> bss : resultList){
            bss.put("cityName", cityMap.get(bss.get("city_id")));
            bss.put("cityId",bss.get("city_id"));
            bss.put("loid",bss.get("username"));
            //解析策略表的 sheet_para字段
            XML xml = new XML(bss.get("sheet_para"),"");
            String regId = xml.getStringValue("domain");
            bss.put("ip",regId);
            //是否完成修改工单
            bss.put("isParamModify","是");
        }
        LOGGER.warn("end getVoipBSSReportDownInfo with resultList:{}",resultList);
        return resultList;
    }


    /**
     * 根据loid获取工单接收和下发情况
     * @param loid
     * @return
     */
    public Map<String,String> getXVoipBSSByLoid(String loid){
        LOGGER.info("begin getXVoipBSSByLoid with loid:{}",loid);
        Map<String,String> result = new HashMap<String, String>();
        Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
        //1、根据loid获取用户最新修改IP工单
        Map<String,String> bssMap = reportJLDAO.getXVoipBSSByLoidDB(loid);
        //2、若工单不存在 则组装返回
        if(bssMap == null || bssMap.size() == 0){
            //获取用户信息 属地 语音IP
            result = reportJLDAO.getUserInfoByloidDB(loid);
            if(result == null || result.size() == 0){
                LOGGER.warn("getXVoipBSSByLoid with loid not exist,loid:{}",loid);
                return new HashMap<String, String>();
            }
            getNoBssResult(loid, result, cityMap);
            return result;
        }

        //3、若工单存在 但工单接收失败或者未绑定 则组装返回
        String sheetInfo = bssMap.get("sheet_context");
        result.put("ip",getIpByBssContext(sheetInfo));
        result.put("cityId",bssMap.get("city_id"));
        result.put("cityName",cityMap.get(bssMap.get("city_id")));
        result.put("loid",loid);
        //解析工单 属地名称 ip
        String bssResult = String.valueOf(bssMap.get("result"));
        if(!bssResult.equals("0")){
            getDownFailResult(result,bssResult);
            return result;
        }

        //4、查询策略表确认该工单下发情况
        Map<String,String> bssDownMap = reportJLDAO.getXVoipBSSDownByLoidDB(loid);
        //未查询到下发策略 按下发失败情况处理
        if (bssDownMap == null || bssDownMap.size() == 0){
            LOGGER.warn("getXVoipBSSByLoid with get BSSDownStrategy null,loid:{},bSSResult:{}",loid,bssResult);
            getDownFailResult(result,bssResult);
            return result;
        }

        //5、既有接收工单也有下发策略的情况 组装返回
        String downResult = String.valueOf(bssDownMap.get("result_id"));
        result.put("isBSS","是");
        result.put("isDownSuccess",downResult.equals("1") ? "是" : "否");
        result.put("isOnline",downResult.equals("1") ? "是" : "否");
        result.put("isUnbind","否");
        return result;
    }

    private void getNoBssResult(String loid, Map<String, String> result, Map<String, String> cityMap) {
        result.put("cityId",result.get("city_id"));
        result.put("cityName",cityMap.get(result.get("city_id")));
        result.put("loid",loid);
        result.put("ip",result.get("reg_id"));
        result.put("isBSS","否");
        result.put("isDownSuccess","否");
        result.put("isOnline","否");
        result.put("isUnbind","是");
    }

    private void getDownFailResult(Map<String,String> result, String bssResult){
        result.put("isBSS","是");
        result.put("isDownSuccess","否");
        result.put("isOnline","否");
        result.put("isUnbind",bssResult.equals("2") ? "是" : "否");
    }

    private String getIpByBssContext(String bssContext){
        LOGGER.warn("getIpByBssContext with bssContext:{}",bssContext);
        XML xml = new XML(bssContext,"");
        String regId = xml.getElement("param").getChildText("regId");
        LOGGER.warn("getIpByBssContext with get regId:{}",regId);
        return regId;
    }



    public VoipXIPBSSReportJLDAO getReportJLDAO() {
        return reportJLDAO;
    }

    public void setReportJLDAO(VoipXIPBSSReportJLDAO reportJLDAO) {
        this.reportJLDAO = reportJLDAO;
    }
}
