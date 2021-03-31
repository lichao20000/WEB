package com.linkage.module.gwms.report.dao;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ou.yuan
 * @since 2020-10-20
 * @category com.linkage.module.gwms.report.dao
 * 
 */
public class RegistBindCancelDAO extends SuperDAO {

    private static Logger logger = LoggerFactory.getLogger(RegistBindCancelDAO.class);

    private List<Map> calculateRegistBindCancelNum(List<Map> registList, List<Map> bindList, List<Map> cancelList) {
        List<Map> resultMapList = new ArrayList<Map>();

        // 获取省中心和一级地市
        List<String> firstCityIdList = getFirstCityContainCenter();

        int registNumTotal = 0;
        int bindNumTotal = 0;
        int cancelNumTotal = 0;

        for (String firstCityId : firstCityIdList) {
            Map resultMap = new HashMap();

            String cityName = CityDAO.getCityName(firstCityId);
            resultMap.put("cityName", cityName);
            resultMap.put("cityId", firstCityId);


            List<String> allNextCityIds = new ArrayList<String>();
            allNextCityIds.add(firstCityId);
            // 不是省中心，则查询地市下的所有地市
            if (!"00".equals(firstCityId)) {
                getAllNextCityContainSelf(firstCityId, allNextCityIds);
            }

            // 注册、绑定、注销
            int registNum = 0;
            int bindNum = 0;
            int cancelNum = 0;

            if (registList != null) {
                for (Map registMap : registList) {
                    String deviceCityId = StringUtil.getStringValue(registMap, "city_id");
                    if (allNextCityIds.contains(deviceCityId)) {
                        int num = StringUtil.getIntValue(registMap, "num");
                        registNum = registNum + num;
                    }
                }
            }

            if (bindList != null) {
                for (Map bindMap : bindList) {
                    String deviceCityId = StringUtil.getStringValue(bindMap, "city_id");

                    if (allNextCityIds.contains(deviceCityId)) {
                        int num = StringUtil.getIntValue(bindMap, "num");
                        bindNum = bindNum + num;
                    }
                }
            }

            if (cancelList != null) {
                for (Map cancelMap : cancelList) {
                    String deviceCityId = StringUtil.getStringValue(cancelMap, "city_id");

                    if (allNextCityIds.contains(deviceCityId)) {
                        int num = StringUtil.getIntValue(cancelMap, "num");
                        cancelNum = cancelNum + num;
                    }
                }
            }

            registNumTotal = registNumTotal + registNum;
            bindNumTotal = bindNumTotal + bindNum;
            cancelNumTotal = cancelNumTotal + cancelNum;

            resultMap.put("registNum", registNum);
            resultMap.put("bindNum", bindNum);
            resultMap.put("cancelNum", cancelNum);

            resultMapList.add(resultMap);
        }

        Map resultMapTotal = new HashMap();
        resultMapTotal.put("cityName", "合计");
        resultMapTotal.put("cityId", "-1");
        resultMapTotal.put("registNum", registNumTotal);
        resultMapTotal.put("bindNum", bindNumTotal);
        resultMapTotal.put("cancelNum", cancelNumTotal);
        resultMapList.add(resultMapTotal);

        return resultMapList;
    }


    /**
     * 获取省中心和一级地市
     * @return
     */
    private List<String> getFirstCityContainCenter() {
        List<String> firstCityList = new ArrayList<String>();
        StringBuffer qryCityql = new StringBuffer();
        qryCityql.append("select city_id from tab_city where parent_id ='00' or city_id = '00' ");
        PrepareSQL psql = new PrepareSQL(qryCityql.toString());
        List<Map> firstCityContainCenterList = jt.queryForList(psql.getSQL());

        if (firstCityContainCenterList != null) {
            for (Map map : firstCityContainCenterList) {
                firstCityList.add(StringUtil.getStringValue(map, "city_id"));
            }
        }

        return firstCityList;
    }

    /**
     * 查找city下所有的city_id
     * @param cityId
     * @param allNextCityList
     */
    private void getAllNextCityContainSelf(String cityId, List<String> allNextCityList) {
        StringBuffer qryCityql = new StringBuffer();
        qryCityql.append("select city_id from tab_city where parent_id = ").append(cityId);
        PrepareSQL psql = new PrepareSQL(qryCityql.toString());
        List<Map> allNextCityContainSelfList = jt.queryForList(psql.getSQL());

        if (allNextCityContainSelfList != null) {
            for (Map map : allNextCityContainSelfList) {
                String cityIdChild = StringUtil.getStringValue(map, "city_id");
                allNextCityList.add(cityIdChild);
                getAllNextCityContainSelf(cityIdChild, allNextCityList);
            }
        }
    }

    /**
     * * 终端注册、绑定和注销状态的统计报表 查询记录
     *
     * @param starttime1
     *            开始时间
     * @param endtime1
     *            结束时间
     * @author ou.yuan
     * @return List<Map>
     */
    public List<Map> getRegistBindCancelList(String starttime1, String endtime1) {
        logger.debug("getRegistBindCancelList({},{},{},{},{},{},{},{},{},{},{})");
        List<Map> resultMapList = new ArrayList<Map>();

        // 终端注册
        StringBuffer qryRegistSql = new StringBuffer();
        qryRegistSql.append("select count(*) as num, a.city_id ");
        qryRegistSql.append("from tab_gw_device a ");
        qryRegistSql.append("where 1=1 ");
        if (StringUtil.isNotEmpty(starttime1)) {
            qryRegistSql.append(" and a.complete_time >=").append(starttime1);
        }
        if (StringUtil.isNotEmpty(endtime1)) {
            qryRegistSql.append(" and a.complete_time <").append(endtime1);
        }
        qryRegistSql.append(" GROUP BY a.city_id ");

        PrepareSQL psql = new PrepareSQL(qryRegistSql.toString());
        List<Map> registList = jt.queryForList(psql.getSQL());


        // 终端绑定
        StringBuffer qryBindSql = new StringBuffer();
        qryBindSql.append("select count(*) as num, a.city_id ");
        qryBindSql.append("from tab_gw_device a, ");
        qryBindSql.append("tab_hgwcustomer b ");
        qryBindSql.append("where a.device_id = b.device_id ");
        if (StringUtil.isNotEmpty(starttime1)) {
            qryBindSql.append(" and a.complete_time >=").append(starttime1);
        }
        if (StringUtil.isNotEmpty(endtime1)) {
            qryBindSql.append(" and a.complete_time <").append(endtime1);
        }
        qryBindSql.append(" GROUP BY a.city_id ");

        PrepareSQL psqlTwo = new PrepareSQL(qryBindSql.toString());
        List<Map> bindList = jt.queryForList(psqlTwo.getSQL());


        // 未绑定的或绑定的用户已经销户的设备
        StringBuffer qryCancelSql = new StringBuffer();
        qryCancelSql.append("SELECT count(*) as num, a.city_id ");
        qryCancelSql.append("FROM tab_gw_device a ");
        qryCancelSql.append("WHERE NOT EXISTS (SELECT b.device_id FROM tab_hgwcustomer b WHERE a.device_id = b.device_id) ");
        if (StringUtil.isNotEmpty(starttime1)) {
            qryCancelSql.append(" and a.complete_time >=").append(starttime1);
        }
        if (StringUtil.isNotEmpty(endtime1)) {
            qryCancelSql.append(" and a.complete_time <").append(endtime1);
        }
        qryCancelSql.append(" GROUP BY a.city_id ");

        PrepareSQL psqlThree = new PrepareSQL(qryCancelSql.toString());
        List<Map> cancelList = jt.queryForList(psqlThree.getSQL());

        // 统计返回给页面的参数
        resultMapList = calculateRegistBindCancelNum(registList, bindList, cancelList);

        return resultMapList;
    }

}
