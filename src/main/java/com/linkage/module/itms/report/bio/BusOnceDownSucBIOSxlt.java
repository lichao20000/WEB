package com.linkage.module.itms.report.bio;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.dao.BusOnceDownSucDAOSxlt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.util.*;

public class BusOnceDownSucBIOSxlt {
    private static Logger logger = LoggerFactory
            .getLogger(BusOnceDownSucBIOSxlt.class);
    private BusOnceDownSucDAOSxlt busOnceDownSucDAO;
    private static final String OPEN_STATUS = "open_status";
    private static final String SERV_TYPE_ID = "serv_type_id";
    private static final String CITY_ID = "cityId";
    private static final String TOTAL_SUC_NUM = "totalSucNum";
    private static final String TOTAL_FAL_NUM = "totalFalNum";
    private static final String CITY_NAME = "cityName";
    private static final String TOTAL_NUM = "totalNum";
    private static final String SUC_RATE = "totalSucRate";

    public List<Map<String, Object>> queryDataList(String cityId,
                                                   String starttime1, String endtime1, String gwType) {
        logger.debug("queryDataList({},{},{})", cityId, starttime1, endtime1);

        List<Map<String, Object>> returnValue = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> tempValue = busOnceDownSucDAO.getDataList(
                cityId, starttime1, endtime1, gwType);

        // 按属地统计
        Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
        // 根据属地cityId获取下一层属地ID(包含自己)
        ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);

        if (null != cityList && !cityList.isEmpty()) {
            Collections.sort(cityList);
            List<String> cCityId = null;
            Map<String, Object> tempMap = null;
            long totalSucNum; // 注意是0和字母L 总的成功数
            long totalNum; // 总数
            long broadbandTotalNum; // 宽带 总数
            long broadbandSucNum; // 宽带 成功绑定数
            long iptvTotalNum; // IPTV 总数
            long iptvSucNum; // IPTV 成功绑定数
            long voipTotalNum; // VOIP 总数
            long voipSucNum; // VOIP成功绑定数
            for (String cityIdN : cityList) {
                // 所有子属地ID(包括自己)
                cCityId = CityDAO.getAllNextCityIdsByCityPid(cityIdN);
                tempMap = new HashMap<String, Object>();
                totalSucNum = 0L;
                totalNum = 0L;
                broadbandTotalNum = 0L;
                broadbandSucNum = 0L;
                iptvTotalNum = 0L;
                iptvSucNum = 0L;
                voipTotalNum = 0L;
                voipSucNum = 0L;
                if (null != tempValue && !tempValue.isEmpty()) {
                    String cityIdT = "";
                    for (Map<String, Object> stringObjectMap : tempValue) {
                        cityIdT = StringUtil.getStringValue(stringObjectMap
                                .get("city_id"));
                        for (String s : cCityId) {
                            // 如果结果中的cityID和city_id相等或者是它的上一级和city_id相等就将该总数计算出来
                            if (cityIdT.equals(s)) {

                                totalNum += StringUtil.getLongValue(stringObjectMap.get("num"));
                                // 如果是is_check为1则该版本为规范版本
                                if (1 == StringUtil.getLongValue(stringObjectMap.get(OPEN_STATUS))) {

                                    totalSucNum += StringUtil
                                            .getLongValue(stringObjectMap.get(
                                                    "num"));
                                }
                                // 宽带
                                if ("10".equals(StringUtil
                                        .getStringValue(stringObjectMap.get(
                                                SERV_TYPE_ID)))) {
                                    broadbandTotalNum += StringUtil
                                            .getLongValue(stringObjectMap.get(
                                                    "num"));
                                    if (1 == StringUtil.getLongValue(stringObjectMap.get(OPEN_STATUS))) {

                                        broadbandSucNum += StringUtil
                                                .getLongValue(stringObjectMap
                                                        .get("num"));
                                    }
                                }
                                // IPTV
                                if ("11".equals(StringUtil
                                        .getStringValue(stringObjectMap.get(
                                                SERV_TYPE_ID)))) {
                                    iptvTotalNum += StringUtil
                                            .getLongValue(stringObjectMap.get(
                                                    "num"));
                                    if (1 == StringUtil.getLongValue(stringObjectMap.get(OPEN_STATUS))) {

                                        iptvSucNum += StringUtil
                                                .getLongValue(stringObjectMap
                                                        .get("num"));
                                    }
                                }
                                // VOIP
                                if ("14".equals(StringUtil
                                        .getStringValue(stringObjectMap.get(
                                                SERV_TYPE_ID)))) {
                                    voipTotalNum += StringUtil
                                            .getLongValue(stringObjectMap.get(
                                                    "num"));
                                    if (1 == StringUtil.getLongValue(stringObjectMap.get(OPEN_STATUS))) {

                                        voipSucNum += StringUtil
                                                .getLongValue(stringObjectMap
                                                        .get("num"));
                                    }
                                }
                            }
                        }
                    }
                    tempMap.put(CITY_ID, cityIdN);
                    tempMap.put(CITY_NAME, cityMap.get(cityIdN));
                    tempMap.put("broadbandSucRate", percent(broadbandSucNum,
                            broadbandTotalNum));
                    tempMap.put("broadbandSucNum", broadbandSucNum);
                    // 宽带失败数
                    tempMap.put("broadbandFalNum", broadbandTotalNum - broadbandSucNum);
                    tempMap.put("broadbandTotalNum", broadbandTotalNum);
                    tempMap.put("iptvSucRate",
                            percent(iptvSucNum, iptvTotalNum));
                    tempMap.put("iptvSucNum", iptvSucNum);
                    // IPTV 失败数
                    tempMap.put("iptvFalNum", iptvTotalNum - iptvSucNum);
                    tempMap.put("iptvTotalNum", iptvTotalNum);
                    tempMap.put("voipSucRate",
                            percent(voipSucNum, voipTotalNum));
                    tempMap.put("voipSucNum", voipSucNum);
                    // VOIP 失败数
                    tempMap.put("voipFalNum", voipTotalNum - voipSucNum);
                    tempMap.put("voipTotalNum", voipTotalNum);
                    tempMap.put(TOTAL_SUC_NUM, totalSucNum);
                    // 总失败数
                    tempMap.put(TOTAL_FAL_NUM, totalNum - totalSucNum);
                    tempMap.put(TOTAL_NUM, totalNum);
                    tempMap.put(SUC_RATE, percent(totalSucNum, totalNum));
                } else {
                    // 当查询的结果为空时，所有值赋值0
                    tempMap.put(CITY_ID, cityIdN);
                    tempMap.put(CITY_NAME, cityMap.get(cityIdN));
                    tempMap.put("broadbandSucRate", percent(broadbandSucNum,
                            broadbandTotalNum));
                    tempMap.put("broadbandSucNum", broadbandSucNum);
                    // 宽带失败数
                    tempMap.put("broadbandFalNum", broadbandTotalNum - broadbandSucNum);
                    tempMap.put("broadbandTotalNum", broadbandTotalNum);
                    tempMap.put("iptvSucRate",
                            percent(iptvSucNum, iptvTotalNum));
                    tempMap.put("iptvSucNum", iptvSucNum);
                    tempMap.put("iptvFalNum", iptvTotalNum - iptvSucNum);
                    tempMap.put("iptvTotalNum", iptvTotalNum);
                    tempMap.put("voipSucRate",
                            percent(voipSucNum, voipTotalNum));
                    tempMap.put("voipSucNum", voipSucNum);
                    // VOIP 失败数
                    tempMap.put("voipFalNum", voipTotalNum - voipSucNum);
                    tempMap.put("voipTotalNum", voipTotalNum);
                    tempMap.put(SUC_RATE, percent(totalSucNum, totalNum));
                    tempMap.put(TOTAL_SUC_NUM, totalSucNum);
                    // 总失败数
                    tempMap.put(TOTAL_FAL_NUM, totalNum - totalSucNum);
                    tempMap.put(TOTAL_NUM, totalNum);
                }
                returnValue.add(tempMap);
            }
        }

        return returnValue;
    }

    public List<Map<String, Object>> queryStbDataList(String cityId,
                                                   String starttime1, String endtime1, String gwType) {
        logger.debug("queryDataList({},{},{})", cityId, starttime1, endtime1);
        List<Map<String, Object>> returnValue = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> tempValue = busOnceDownSucDAO.getStbDataList(cityId,
                starttime1, endtime1, gwType);
        // 按属地统计
        Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
        // 根据属地cityId获取下一层属地ID(包含自己)
        ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);

        if (null != cityList && !cityList.isEmpty())
        {
            Collections.sort(cityList);
            List<String> cCityId = null;
            Map<String, Object> tempMap = null;
            // 注意是0和字母L 总的成功数
            long totalSucNum;
            // 总数
            long totalNum;
            for (String cityIdT : cityList) {
                // 所有子属地ID(包括自己)
                cCityId = CityDAO.getAllNextCityIdsByCityPid(cityIdT);
                tempMap = new HashMap<String, Object>();
                totalSucNum = 0L;
                totalNum = 0L;
                if (null != tempValue && !tempValue.isEmpty()) {
                    String cityIdN = "";
                    for (Map<String, Object> stringObjectMap : tempValue) {
                        cityIdN = StringUtil.getStringValue(stringObjectMap
                                .get("city_id"));
                        if(cCityId == null || cCityId.isEmpty()){
                            break;
                        }
                        for (String s : cCityId) {
                            // 如果结果中的cityID和city_id相等或者是它的上一级和city_id相等就将该总数计算出来
                            if (cityIdN.equals(s)) {
                                totalNum += StringUtil.getLongValue(stringObjectMap.get(
                                        "num"));
                                // 如果是is_check为1则该版本为成功版本
                                if (1 == StringUtil.getLongValue(stringObjectMap.get(
                                        "user_status"))) {
                                    totalSucNum += StringUtil.getLongValue(stringObjectMap.get("num"));
                                }
                            }
                        }
                    }
                    tempMap.put(CITY_ID, cityIdT);
                    tempMap.put(CITY_NAME, cityMap.get(cityIdT));
                    tempMap.put(TOTAL_SUC_NUM, totalSucNum);
                    tempMap.put(TOTAL_FAL_NUM, totalNum - totalSucNum);
                    tempMap.put(TOTAL_NUM, totalNum);
                    tempMap.put(SUC_RATE, percent(totalSucNum, totalNum));

                } else {
                    // 当查询的结果为空时，所有值赋值0
                    tempMap.put(CITY_ID, cityIdT);
                    tempMap.put(CITY_NAME, cityMap.get(cityIdT));
                    tempMap.put(TOTAL_SUC_NUM, totalSucNum);
                    tempMap.put(TOTAL_FAL_NUM, totalNum - totalSucNum);
                    tempMap.put(TOTAL_NUM, totalNum);
                    tempMap.put(SUC_RATE, percent(totalSucNum, totalNum));
                }
                returnValue.add(tempMap);
            }
        }
        return returnValue;
    }
    public List<Map> getServInfoDetail(String cityId, String starttime1,
                                       String endtime1, String servTypeId, int curPageSplitPage,
                                       int numSplitPage, String gwType, String openStatus) {

        return busOnceDownSucDAO.getServInfoDetail(cityId, starttime1,
                endtime1, servTypeId, curPageSplitPage, numSplitPage, gwType, openStatus);
    }

    public int getServInfoCount(String cityId, String starttime1,
                                String endtime1, String servTypeId, int curPageSplitPage,
                                int numSplitPage, String gwType, String openStatus) {

        return busOnceDownSucDAO.getServInfoCount(cityId, starttime1, endtime1,
                servTypeId, curPageSplitPage, numSplitPage, gwType, openStatus);
    }

    public List<Map<String, Object>> getServInfoExcel(String cityId,
                                                      String starttime1, String endtime1, String servTypeId, String gwType, String openStatus) {

        return busOnceDownSucDAO.getServInfoExcel(cityId, starttime1, endtime1,
                servTypeId, gwType, openStatus);
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

    public BusOnceDownSucDAOSxlt getBusOnceDownSucDAO() {
        return busOnceDownSucDAO;
    }

    public void setBusOnceDownSucDAO(BusOnceDownSucDAOSxlt busOnceDownSucDAO) {
        this.busOnceDownSucDAO = busOnceDownSucDAO;
    }

    public List<Map> getServInfoStbDetail(String cityId, String starttime1, String endtime1, String servTypeId, int curPageSplitPage, int numSplitPage, String gwType, String openStatus) {
        return busOnceDownSucDAO.getServInfoStbDetail(cityId, starttime1,
                endtime1, servTypeId, curPageSplitPage, numSplitPage, gwType, openStatus);
    }

    public List<Map<String,Object>> getServInfoStbExcel(String cityId, String starttime1, String endtime1, String servTypeId, String gwType, String openStatus) {
        return busOnceDownSucDAO.getServInfoStbExcel(cityId, starttime1, endtime1,
                servTypeId, gwType, openStatus);
    }

    public int getStbServInfoCount(String cityId, String starttime1,
                                String endtime1, String openStatus) {

        return busOnceDownSucDAO.getStbServInfoCount(cityId, starttime1, endtime1, openStatus);
    }

}
