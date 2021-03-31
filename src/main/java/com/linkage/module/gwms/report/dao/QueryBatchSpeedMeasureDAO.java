package com.linkage.module.gwms.report.dao;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ou.yuan
 * @since 2020-10-20
 * @category com.linkage.module.gwms.report.dao
 * 
 */
public class QueryBatchSpeedMeasureDAO extends SuperDAO {

    private static Logger logger = LoggerFactory.getLogger(QueryBatchSpeedMeasureDAO.class);

    private List<Map> calculateSpeedMeasureNum(List<Map> speedMeasureList) {
        List<Map> resultMapList = new ArrayList<Map>();

        // 获取省中心和一级地市
        List<String> firstCityIdList = CityDAO.getNextCityIdsByCityPid("00");

        // logger.warn("QueryBatchSpeedMeasureDAO.calculateSpeedMeasureNum firstCityIdList is {}", firstCityIdList.toString());

        for (String firstCityId : firstCityIdList) {
            Map resultMap = new HashMap();

            String cityName = CityDAO.getCityName(firstCityId);
            resultMap.put("cityName", cityName);
            resultMap.put("cityId", firstCityId);

            // 查询地市下的所有地市，包括自己
            List<String> allNextCityIds = CityDAO.getAllNextCityIdsByCityPid(firstCityId);

            // 测速总数、达标总数、总体达标率
            int measureAllNum = 0;
            int accStandAllNum = 0;
            BigDecimal accStandAllRate = BigDecimal.ZERO;

            int measureAllNum_100 = 0;
            int accStandAllNum_100 = 0;
            BigDecimal accStandAllRate_100 = BigDecimal.ZERO;

            int measureAllNum_200 = 0;
            int accStandAllNum_200 = 0;
            BigDecimal accStandAllRate_200 = BigDecimal.ZERO;

            int measureAllNum_300 = 0;
            int accStandAllNum_300 = 0;
            BigDecimal accStandAllRate_300 = BigDecimal.ZERO;

            int measureAllNum_500 = 0;
            int accStandAllNum_500 = 0;
            BigDecimal accStandAllRate_500 = BigDecimal.ZERO;

            int measureAllNum_1000 = 0;
            int accStandAllNum_1000 = 0;
            BigDecimal accStandAllRate_1000 = BigDecimal.ZERO;


            for (Map speedMeasureMap : speedMeasureList) {
                String deviceCityId = StringUtil.getStringValue(speedMeasureMap, "city_id");

                if (allNextCityIds.contains(deviceCityId)) {
                    measureAllNum++;

                    String accessStandard = "测速异常";
                    String wan_type = StringUtil.getStringValue(speedMeasureMap, "wan_type");
                    // 测速结果
                    String total_down_pert_deal = StringUtil.getStringValue(speedMeasureMap, "total_down_pert_deal");
                    double totalDownPertDeal = 0;
                    if (null != total_down_pert_deal) {
                        totalDownPertDeal = Double.parseDouble(total_down_pert_deal);
                    }
                    // 下行签约速率
                    String downlink = StringUtil.getStringValue(speedMeasureMap, "downlink");
                    double downlinkInt = 0;
                    if (null != downlink) {
                        downlinkInt = Double.parseDouble(downlink);
                    }

                    if ("PPPoE_Bridged".equals(wan_type) || "IP_Routed".equals(wan_type)) {
                        if (totalDownPertDeal >= 0.9 * downlinkInt) {
                            accessStandard = "达标";
                        }
                        else {
                            accessStandard = "不达标";
                        }
                    }


                    if ("达标".equals(accessStandard)) {
                        accStandAllNum++;
                    }

                    if (downlinkInt <= 100) {
                        measureAllNum_100++;
                        if ("达标".equals(accessStandard)) {
                            accStandAllNum_100++;
                        }
                    }
                    else if (downlinkInt > 100 && downlinkInt <= 200) {
                        measureAllNum_200++;
                        if ("达标".equals(accessStandard)) {
                            accStandAllNum_200++;
                        }
                    }
                    else if (downlinkInt > 200 && downlinkInt <= 300) {
                        measureAllNum_300++;
                        if ("达标".equals(accessStandard)) {
                            accStandAllNum_300++;
                        }
                    }
                    else if (downlinkInt > 300 && downlinkInt <= 500) {
                        measureAllNum_500++;
                        if ("达标".equals(accessStandard)) {
                            accStandAllNum_500++;
                        }
                    }
                    else if (downlinkInt > 500) {
                        measureAllNum_1000++;
                        if ("达标".equals(accessStandard)) {
                            accStandAllNum_1000++;
                        }
                    }
                }
            }

            resultMap.put("measureAllNum", measureAllNum);
            resultMap.put("accStandAllNum", accStandAllNum);

            resultMap.put("measureAllNum_100", measureAllNum_100);
            resultMap.put("accStandAllNum_100", accStandAllNum_100);

            resultMap.put("measureAllNum_200", measureAllNum_200);
            resultMap.put("accStandAllNum_200", accStandAllNum_200);

            resultMap.put("measureAllNum_300", measureAllNum_300);
            resultMap.put("accStandAllNum_300", accStandAllNum_300);

            resultMap.put("measureAllNum_500", measureAllNum_500);
            resultMap.put("accStandAllNum_500", accStandAllNum_500);

            resultMap.put("measureAllNum_1000", measureAllNum_1000);
            resultMap.put("accStandAllNum_1000", accStandAllNum_1000);

            if (measureAllNum > 0) {
                accStandAllRate = new BigDecimal(accStandAllNum * 100).divide(new BigDecimal(measureAllNum), 2, BigDecimal.ROUND_HALF_UP);
            }
            if (measureAllNum_100 > 0) {
                accStandAllRate_100 = new BigDecimal(accStandAllNum_100 * 100).divide(new BigDecimal(measureAllNum_100), 2, BigDecimal.ROUND_HALF_UP);
            }
            if (measureAllNum_200 > 0) {
                accStandAllRate_200 = new BigDecimal(accStandAllNum_200 * 100).divide(new BigDecimal(measureAllNum_200), 2, BigDecimal.ROUND_HALF_UP);
            }
            if (measureAllNum_300 > 0) {
                accStandAllRate_300 = new BigDecimal(accStandAllNum_300 * 100).divide(new BigDecimal(measureAllNum_300), 2, BigDecimal.ROUND_HALF_UP);
            }
            if (measureAllNum_500 > 0) {
                accStandAllRate_500 = new BigDecimal(accStandAllNum_500 * 100).divide(new BigDecimal(measureAllNum_500), 2, BigDecimal.ROUND_HALF_UP);
            }
            if (measureAllNum_1000 > 0) {
                accStandAllRate_1000 = new BigDecimal(accStandAllNum_1000 * 100).divide(new BigDecimal(measureAllNum_1000), 2, BigDecimal.ROUND_HALF_UP);
            }

            resultMap.put("accStandAllRate", accStandAllRate);
            resultMap.put("accStandAllRate_100", accStandAllRate_100);
            resultMap.put("accStandAllRate_200", accStandAllRate_200);
            resultMap.put("accStandAllRate_300", accStandAllRate_300);
            resultMap.put("accStandAllRate_500", accStandAllRate_500);
            resultMap.put("accStandAllRate_1000", accStandAllRate_1000);

            resultMapList.add(resultMap);
        }

        return resultMapList;
    }


    /**
     * * 批量测速结果查询记录
     *
     * @param starttime1
     *            开始时间
     * @param endtime1
     *            结束时间
     * @author ou.yuan
     * @return List<Map>
     */
    public List<Map> getBatchSpeedMeasureList(String starttime1, String endtime1, String taskName) {
        logger.debug("getBatchSpeedMeasureList({},{},{},{},{},{},{},{},{},{},{})");
        List<Map> resultMapList = new ArrayList<Map>();

        List<String> taskNameList = new ArrayList<String>();
        if (StringUtil.isNotEmpty(taskName)) {
            taskNameList = Arrays.asList(taskName.split(","));
        }

        StringBuffer qrySpeedMeasureSql = new StringBuffer();

        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            qrySpeedMeasureSql.append("select p.downlink, p.city_name, p.city_id, p.wan_type, p.total_down_pert_deal ");
            qrySpeedMeasureSql.append("from ( ");
            qrySpeedMeasureSql.append("         select a.city_name, a.city_id, a.downlink, cc.wan_type, cc.total_down_pert * 8 / 1024 total_down_pert_deal ");
            qrySpeedMeasureSql.append("         from (select tc.city_name, tc.city_id, ee.downlink, c.device_serialnumber ");
            qrySpeedMeasureSql.append("               from hgwcust_serv_info dd, ");
            qrySpeedMeasureSql.append("                    tab_netacc_spead  ee, ");
            qrySpeedMeasureSql.append("                    tab_gw_device      c, ");
            qrySpeedMeasureSql.append("                    tab_hgwcustomer    d, ");
            qrySpeedMeasureSql.append("                    tab_vendor         e, ");
            qrySpeedMeasureSql.append("                    gw_device_model    f, ");
            qrySpeedMeasureSql.append("                    tab_device_version_attribute kk, ");
            qrySpeedMeasureSql.append("                    tab_city          tc, ");
            qrySpeedMeasureSql.append("                    tab_devicetype_info     zz ");
            qrySpeedMeasureSql.append("               where c.devicetype_id = kk.devicetype_id ");
            qrySpeedMeasureSql.append("                     and dd.username = ee.username ");
            qrySpeedMeasureSql.append("                     and c.devicetype_id = zz.devicetype_id ");
            qrySpeedMeasureSql.append("                     and dd.user_id = d.user_id ");
            qrySpeedMeasureSql.append("                     and dd.serv_type_id = 10 ");
            qrySpeedMeasureSql.append("                     and d.city_id = tc.city_id ");
            qrySpeedMeasureSql.append("                     and e.vendor_id = f.vendor_id ");
            qrySpeedMeasureSql.append("                     and f.device_model_id = c.device_model_id ");
            qrySpeedMeasureSql.append("                     and d.device_id = c.device_id ");
            qrySpeedMeasureSql.append("               ) a, ");
            qrySpeedMeasureSql.append("               (select t.wan_type, t.total_down_pert, t.device_serialnumber ");
            qrySpeedMeasureSql.append("                from (");
            qrySpeedMeasureSql.append("                        SELECT ");
            qrySpeedMeasureSql.append("                               @row_number:= CASE WHEN @customer_no=t.device_serialnumber THEN @row_number+1 ELSE 1 END AS rn, ");
            qrySpeedMeasureSql.append("                               @customer_no:=t.device_serialnumber as device_serialnumber, t.wan_type, t.total_down_pert ");
            qrySpeedMeasureSql.append("                        FROM tab_http_diag_result t, ");
            qrySpeedMeasureSql.append("                             tab_ids_task         q, ");
            qrySpeedMeasureSql.append("                             (SELECT @row_number:=0, @customer_no:=0) AS temp");
            qrySpeedMeasureSql.append("                        where t.task_id = q.task_id ");
            qrySpeedMeasureSql.append("                            and t.http_result = 'http质量下载成功' ");
            qrySpeedMeasureSql.append("                            and t.total_down_pert != 0 ");
            if (null != taskNameList && taskNameList.size() > 0) {
                qrySpeedMeasureSql.append("                        and q.task_name in (" + StringUtils.weave(taskNameList) + ") ");
            }
            if (StringUtil.isNotEmpty(starttime1)) {
                qrySpeedMeasureSql.append("                        and t.test_time>=").append(starttime1);
            }
            if (StringUtil.isNotEmpty(endtime1)) {
                qrySpeedMeasureSql.append("                        and t.test_time<").append(endtime1);
            }
            qrySpeedMeasureSql.append("                        ORDER BY t.device_serialnumber, t.total_down_pert desc");
            qrySpeedMeasureSql.append("                       ) t ");
            qrySpeedMeasureSql.append("                where rn = 1 ");
            qrySpeedMeasureSql.append("                ) cc ");
            qrySpeedMeasureSql.append("         where a.device_serialnumber = cc.device_serialnumber ");
            qrySpeedMeasureSql.append("     ) p ");
        }
        else {
            qrySpeedMeasureSql.append("select p.downlink, p.city_name, p.city_id, p.wan_type, p.total_down_pert_deal ");
            qrySpeedMeasureSql.append("from ( ");
            qrySpeedMeasureSql.append("         select a.city_name, a.city_id, a.downlink, cc.wan_type, cc.total_down_pert * 8 / 1024 total_down_pert_deal ");
            qrySpeedMeasureSql.append("         from (select tc.city_name, tc.city_id, ee.downlink, c.device_serialnumber ");
            qrySpeedMeasureSql.append("               from hgwcust_serv_info dd, ");
            qrySpeedMeasureSql.append("                    tab_netacc_spead  ee, ");
            qrySpeedMeasureSql.append("                    tab_gw_device      c, ");
            qrySpeedMeasureSql.append("                    tab_hgwcustomer    d, ");
            qrySpeedMeasureSql.append("                    tab_vendor         e, ");
            qrySpeedMeasureSql.append("                    gw_device_model    f, ");
            qrySpeedMeasureSql.append("                    tab_device_version_attribute kk, ");
            qrySpeedMeasureSql.append("                    tab_city          tc, ");
            qrySpeedMeasureSql.append("                    tab_devicetype_info     zz ");
            qrySpeedMeasureSql.append("               where c.devicetype_id = kk.devicetype_id ");
            qrySpeedMeasureSql.append("                     and dd.username = ee.username ");
            qrySpeedMeasureSql.append("                     and c.devicetype_id = zz.devicetype_id ");
            qrySpeedMeasureSql.append("                     and dd.user_id = d.user_id ");
            qrySpeedMeasureSql.append("                     and dd.serv_type_id = 10 ");
            qrySpeedMeasureSql.append("                     and d.city_id = tc.city_id ");
            qrySpeedMeasureSql.append("                     and e.vendor_id = f.vendor_id ");
            qrySpeedMeasureSql.append("                     and f.device_model_id = c.device_model_id ");
            qrySpeedMeasureSql.append("                     and d.device_id = c.device_id ");
            qrySpeedMeasureSql.append("               ) a, ");
            qrySpeedMeasureSql.append("               (select t.wan_type, t.total_down_pert, t.device_serialnumber ");
            qrySpeedMeasureSql.append("                from (select t.wan_type, t.total_down_pert, t.device_serialnumber, row_number() over(partition by t.device_serialnumber order by t.total_down_pert desc) rn ");
            qrySpeedMeasureSql.append("                      from tab_http_diag_result t, ");
            qrySpeedMeasureSql.append("                           tab_ids_task q ");
            qrySpeedMeasureSql.append("                      where t.task_id = q.task_id ");
            qrySpeedMeasureSql.append("                            and t.http_result = 'http质量下载成功' ");
            qrySpeedMeasureSql.append("                            and t.total_down_pert != 0 ");

            if (null != taskNameList && taskNameList.size() > 0) {
                qrySpeedMeasureSql.append("                        and q.task_name in (" + StringUtils.weave(taskNameList) + ") ");
            }
            if (StringUtil.isNotEmpty(starttime1)) {
                qrySpeedMeasureSql.append("                        and t.test_time>=").append(starttime1);
            }
            if (StringUtil.isNotEmpty(endtime1)) {
                qrySpeedMeasureSql.append("                        and t.test_time<").append(endtime1);
            }
            qrySpeedMeasureSql.append("                       ) t ");
            qrySpeedMeasureSql.append("                where rn = 1 ");
            qrySpeedMeasureSql.append("                ) cc ");
            qrySpeedMeasureSql.append("         where a.device_serialnumber = cc.device_serialnumber ");
            qrySpeedMeasureSql.append("     ) p ");
        }



        PrepareSQL psqlTwo = new PrepareSQL(qrySpeedMeasureSql.toString());
        List<Map> speedMeasureList = jt.queryForList(psqlTwo.getSQL());


        if (speedMeasureList == null || speedMeasureList.isEmpty()) {
            logger.warn("QueryBatchSpeedMeasureDAO.getBatchSpeedMeasureList() speedMeasureList is empty!");
        }
        else {
            // 统计返回给页面的参数
            resultMapList = calculateSpeedMeasureNum(speedMeasureList);
        }

        return resultMapList;
    }


    /**
     * 批量测速结果查询详情数量
     * @param cityId
     * @param starttime1
     * @param endtime1
     * @param downlink
     * @return
     */
    public int getBatchSpeedMeasureDetailCount(String cityId, String starttime1, String endtime1, String downlink, String taskName, String numType) {
        PrepareSQL sql = new PrepareSQL();

        List<String> taskNameList = new ArrayList<String>();
        if (StringUtil.isNotEmpty(taskName)) {
            taskNameList = Arrays.asList(taskName.split(","));
        }

        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            //-- CITY_NAME, LOID , KD, 上网方式, DEVICE_NAME, 下行签约速率, PON类型, 厂家, 型号, 硬件版本, 软件版本,
            //-- 天翼网关类型，是否千兆猫, 是否支持测速, 测速时间, 测试结果, 测试结果1, 测速结果, 速率百分比, 测速质量， 达标情况
            sql.setSQL("select count(*) ");
            sql.append(" from ( ");
            sql.append("        select a.city_id, a.city_name, a.loid, a.KD, a.onlineType, a.oui, a.device_serialnumber, a.downlink, a.PON_TYPE, a.vendor_add, a.device_model, a.hardwareversion, a.softwareversion, ");
            sql.append("               a.device_version_type, a.gbbroadband, a.is_speedtest, cc.task_id, ");
            // sql.append("               to_char((TO_DATE('19700101', 'yyyymmdd') + cc.test_time / 86400 + TO_NUMBER(SUBSTR(TZ_OFFSET(sessiontimezone),1,3)) / 24),'yyyy-mm-dd hh24:mi:ss') test_time, ");
            sql.append("               cc.wan_type, ");
            sql.append("               cc.http_result, ");
            // sql.append("               case when cc.http_result = 'http质量下载成功' then '测速成功' else '测速异常' end http_result_one,  ");
            sql.append("               cc.total_down_pert * 8 / 1024 total_down_pert_deal ");
            sql.append("        from (select tc.city_id, ");
            sql.append("                     tc.city_name, ");
            sql.append("                     d.username loid, ");
            sql.append("                     dd.username KD, ");
            sql.append("                     case when dd.wan_type = 1 then '桥接' when dd.wan_type = 2 then '路由' else '其他' end onlineType, ");
            sql.append("                     c.oui, c.device_serialnumber, c.device_name, ");
            sql.append("                     ee.downlink, ");
            sql.append("                     case when access_style_id = 3 then 'EPON' when access_style_id = 4 then 'GPON' else '其他' end PON_TYPE, ");
            sql.append("                     e.vendor_add, ");
            sql.append("                     f.device_model, ");
            sql.append("                     zz.hardwareversion, ");
            sql.append("                     zz.softwareversion, ");
            sql.append("                     case when kk.device_version_type = 1 then 'E8-C' ");
            sql.append("                          when kk.device_version_type = 8 then '天翼网关1.0' ");
            sql.append("                          when kk.device_version_type = 9 then '天翼网关2.0' ");
            sql.append("                          when kk.device_version_type = 10 then '天翼网关3.0' ");
            sql.append("                          when kk.device_version_type = 11 then '天翼网关4.0' ");
            sql.append("                          else '其他' ");
            sql.append("                          end device_version_type, ");
            sql.append("                     case when kk.gbbroadband = 1 then '是' else '否' end gbbroadband, ");
            sql.append("                     case when kk.is_speedtest = 1 then '是' else '否' end is_speedtest ");
            sql.append("              from hgwcust_serv_info            dd, ");
            sql.append("                   tab_netacc_spead             ee, ");
            sql.append("                   tab_gw_device                c,  ");
            sql.append("                   tab_hgwcustomer              d,  ");
            sql.append("                   tab_vendor                   e,  ");
            sql.append("                   gw_device_model              f,  ");
            sql.append("                   tab_device_version_attribute kk, ");
            sql.append("                   tab_city                     tc, ");
            sql.append("                   tab_devicetype_info          zz  ");
            sql.append("              where c.devicetype_id = kk.devicetype_id ");
            sql.append("                    and dd.username = ee.username ");
            sql.append("                    and c.devicetype_id = zz.devicetype_id ");
            sql.append("                    and dd.user_id = d.user_id ");
            sql.append("                    and dd.serv_type_id = 10 ");
            sql.append("                    and d.city_id = tc.city_id ");
            sql.append("                    and e.vendor_id = f.vendor_id ");
            sql.append("                    and f.device_model_id = c.device_model_id ");
            sql.append("                    and d.device_id = c.device_id ");

            if ("100M".equals(downlink)) {
                sql.append("                and ee.downlink <= 100 ");
            }
            else if ("200M".equals(downlink)) {
                sql.append("                and ee.downlink > 100 ");
                sql.append("                and ee.downlink <= 200 ");
            }
            else if ("300M".equals(downlink)) {
                sql.append("                and ee.downlink > 200 ");
                sql.append("                and ee.downlink <= 300 ");
            }
            else if ("500M".equals(downlink)) {
                sql.append("                and ee.downlink > 300 ");
                sql.append("                and ee.downlink <= 500 ");
            }
            else if ("1000M".equals(downlink)) {
                sql.append("                and ee.downlink > 500 ");
            }

            if (!"-1".equals(cityId)) {
                // 查询地市下的所有地市，包括自己
                List<String> allNextCityIds = CityDAO.getAllNextCityIdsByCityPid(cityId);
                sql.append("                and tc.city_id in (" + StringUtils.weave(allNextCityIds) + ") ");
            }


            sql.append("             ) a, ");


            sql.append("             (select t.test_time, t.wan_type, t.total_down_pert, t.device_serialnumber, t.http_result, t.task_id ");
            sql.append("              from (");
            sql.append("                      SELECT ");
            sql.append("                             @row_number:= CASE WHEN @customer_no=t.device_serialnumber THEN @row_number+1 ELSE 1 END AS rn, ");
            sql.append("                             @customer_no:=t.device_serialnumber as device_serialnumber, t.test_time, t.wan_type, t.http_result, t.task_id, t.total_down_pert ");
            sql.append("                      FROM tab_http_diag_result t, ");
            sql.append("                           tab_ids_task         q, ");
            sql.append("                           (SELECT @row_number:=0, @customer_no:=0) AS temp");
            sql.append("                      where t.task_id = q.task_id ");
            sql.append("                            and t.http_result = 'http质量下载成功' ");
            sql.append("                            and t.total_down_pert != 0 ");

            if (null != taskNameList && taskNameList.size() > 0) {
                sql.append("                        and q.task_name in (" + StringUtils.weave(taskNameList) + ") ");
            }
            if (StringUtil.isNotEmpty(starttime1)) {
                sql.append("                        and t.test_time>=" + starttime1);
            }
            if (StringUtil.isNotEmpty(endtime1)) {
                sql.append("                        and t.test_time<" + endtime1);
            }
            sql.append("                        ORDER BY t.device_serialnumber, t.total_down_pert desc");
            sql.append("                     ) t ");
            sql.append("              where rn = 1 ");
            sql.append("              ) cc ");


            sql.append("        where a.device_serialnumber = cc.device_serialnumber");
            sql.append(" ) p ");

            // 达标了的
            if ("accessStandard".equals(numType)) {
                sql.append(" where (p.wan_type = 'PPPoE_Bridged' and ifnull(p.total_down_pert_deal, 0) >= 0.9 * p.downlink) or");
                sql.append("       (p.wan_type = 'IP_Routed' and ifnull(p.total_down_pert_deal, 0) >= 0.9 * p.downlink)");
            }
        }
        else {
            //-- CITY_NAME, LOID , KD, 上网方式, DEVICE_NAME, 下行签约速率, PON类型, 厂家, 型号, 硬件版本, 软件版本,
            //-- 天翼网关类型，是否千兆猫, 是否支持测速, 测速时间, 测试结果, 测试结果1, 测速结果, 速率百分比, 测速质量， 达标情况
            sql.setSQL("select count(*) ");
            sql.append(" from ( ");
            sql.append("        select a.city_id, a.city_name, a.loid, a.KD, a.onlineType, a.oui, a.device_serialnumber, a.downlink, a.PON_TYPE, a.vendor_add, a.device_model, a.hardwareversion, a.softwareversion, ");
            sql.append("               a.device_version_type, a.gbbroadband, a.is_speedtest, cc.task_id, ");
            // sql.append("               to_char((TO_DATE('19700101', 'yyyymmdd') + cc.test_time / 86400 + TO_NUMBER(SUBSTR(TZ_OFFSET(sessiontimezone),1,3)) / 24),'yyyy-mm-dd hh24:mi:ss') test_time, ");
            sql.append("               cc.wan_type, ");
            sql.append("               cc.http_result, ");
            // sql.append("               case when cc.http_result = 'http质量下载成功' then '测速成功' else '测速异常' end http_result_one,  ");
            sql.append("               cc.total_down_pert * 8 / 1024 total_down_pert_deal ");
            sql.append("        from (select tc.city_id, ");
            sql.append("                     tc.city_name, ");
            sql.append("                     d.username loid, ");
            sql.append("                     dd.username KD, ");
            sql.append("                     case when dd.wan_type = 1 then '桥接' when dd.wan_type = 2 then '路由' else '其他' end onlineType, ");
            sql.append("                     c.oui, c.device_serialnumber, c.device_name, ");
            sql.append("                     ee.downlink, ");
            sql.append("                     case when access_style_id = 3 then 'EPON' when access_style_id = 4 then 'GPON' else '其他' end PON_TYPE, ");
            sql.append("                     e.vendor_add, ");
            sql.append("                     f.device_model, ");
            sql.append("                     zz.hardwareversion, ");
            sql.append("                     zz.softwareversion, ");
            sql.append("                     case when kk.device_version_type = 1 then 'E8-C' ");
            sql.append("                          when kk.device_version_type = 8 then '天翼网关1.0' ");
            sql.append("                          when kk.device_version_type = 9 then '天翼网关2.0' ");
            sql.append("                          when kk.device_version_type = 10 then '天翼网关3.0' ");
            sql.append("                          when kk.device_version_type = 11 then '天翼网关4.0' ");
            sql.append("                          else '其他' ");
            sql.append("                          end device_version_type, ");
            sql.append("                     case when kk.gbbroadband = 1 then '是' else '否' end gbbroadband, ");
            sql.append("                     case when kk.is_speedtest = 1 then '是' else '否' end is_speedtest ");
            sql.append("              from hgwcust_serv_info            dd, ");
            sql.append("                   tab_netacc_spead             ee, ");
            sql.append("                   tab_gw_device                c,  ");
            sql.append("                   tab_hgwcustomer              d,  ");
            sql.append("                   tab_vendor                   e,  ");
            sql.append("                   gw_device_model              f,  ");
            sql.append("                   tab_device_version_attribute kk, ");
            sql.append("                   tab_city                     tc, ");
            sql.append("                   tab_devicetype_info          zz  ");
            sql.append("              where c.devicetype_id = kk.devicetype_id ");
            sql.append("                    and dd.username = ee.username ");
            sql.append("                    and c.devicetype_id = zz.devicetype_id ");
            sql.append("                    and dd.user_id = d.user_id ");
            sql.append("                    and dd.serv_type_id = 10 ");
            sql.append("                    and d.city_id = tc.city_id ");
            sql.append("                    and e.vendor_id = f.vendor_id ");
            sql.append("                    and f.device_model_id = c.device_model_id ");
            sql.append("                    and d.device_id = c.device_id ");

            if ("100M".equals(downlink)) {
                sql.append("                and ee.downlink <= 100 ");
            }
            else if ("200M".equals(downlink)) {
                sql.append("                and ee.downlink > 100 ");
                sql.append("                and ee.downlink <= 200 ");
            }
            else if ("300M".equals(downlink)) {
                sql.append("                and ee.downlink > 200 ");
                sql.append("                and ee.downlink <= 300 ");
            }
            else if ("500M".equals(downlink)) {
                sql.append("                and ee.downlink > 300 ");
                sql.append("                and ee.downlink <= 500 ");
            }
            else if ("1000M".equals(downlink)) {
                sql.append("                and ee.downlink > 500 ");
            }

            if (!"-1".equals(cityId)) {
                // 查询地市下的所有地市，包括自己
                List<String> allNextCityIds = CityDAO.getAllNextCityIdsByCityPid(cityId);
                sql.append("                and tc.city_id in (" + StringUtils.weave(allNextCityIds) + ") ");
            }


            sql.append("             ) a, ");
            sql.append("             (select t.test_time, t.wan_type, t.total_down_pert, t.device_serialnumber, t.http_result, t.task_id ");
            sql.append("              from (select t.test_time, t.wan_type, t.total_down_pert, t.device_serialnumber, t.http_result, t.task_id, ");
            sql.append("                           row_number() over(partition by t.device_serialnumber order by t.total_down_pert * 8 / 1024 desc) rn ");
            sql.append("                    from tab_http_diag_result t, ");
            sql.append("                         tab_ids_task q ");
            sql.append("                    where t.task_id = q.task_id ");
            sql.append("                          and t.http_result = 'http质量下载成功' ");
            sql.append("                          and t.total_down_pert != 0 ");

            if (null != taskNameList && taskNameList.size() > 0) {
                sql.append("                      and q.task_name in (" + StringUtils.weave(taskNameList) + ") ");
            }
            if (StringUtil.isNotEmpty(starttime1)) {
                sql.append("                      and t.test_time>=" + starttime1);
            }
            if (StringUtil.isNotEmpty(endtime1)) {
                sql.append("                      and t.test_time<" + endtime1);
            }

            sql.append("                     ) t");
            sql.append("              where rn = 1) cc");
            sql.append("        where a.device_serialnumber = cc.device_serialnumber");
            sql.append(" ) p ");

            // 达标了的
            if ("accessStandard".equals(numType)) {
                sql.append(" where (p.wan_type = 'PPPoE_Bridged' and nvl(p.total_down_pert_deal, 0) >= 0.9 * p.downlink) or");
                sql.append("       (p.wan_type = 'IP_Routed' and nvl(p.total_down_pert_deal, 0) >= 0.9 * p.downlink)");
            }
        }


        return jt.queryForInt(sql.getSQL());
    }


    /**
     * 批量测速结果查询详情列表  比导出Excel多了分页
     * @param cityId
     * @param curPage_splitPage
     * @param num_splitPage
     * @param starttime1
     * @param endtime1
     * @param downlink
     * @return
     */
    public List<Map> queryBatchSpeedMeasureDetailList(String cityId, int curPage_splitPage, int num_splitPage, String starttime1, String endtime1, String downlink, String taskName, String numType) {
        PrepareSQL sql = packageDetailSql(cityId, downlink, taskName, starttime1, endtime1, numType);

        List<Map> list = querySP(sql.getSQL(), (curPage_splitPage - 1) * num_splitPage
                + 1, num_splitPage, new RowMapper()
        {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException
            {
                Map<String, String> map = new HashMap<String, String>();
                map = packageDetailMap(rs);
                return map;
            }
        });

        return list;
    }


    /**
     * 批量测速结果查询详情到处到Excel
     * @param cityId
     * @param downlink
     * @param starttime1
     * @param endtime1
     * @return
     */
    public List<Map> getBatchSpeedMeasureDetailExcel(String cityId, String downlink, String starttime1, String endtime1, String taskName, String numType) {
        PrepareSQL sql = packageDetailSql(cityId, downlink, taskName, starttime1, endtime1, numType);

        List<Map> list = jt.query(sql.getSQL(), new RowMapper()
        {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException
            {

                Map<String, String> map = new HashMap<String, String>();
                map = packageDetailMap(rs);
                return map;
            }
        });
        return list;
    }

    private PrepareSQL packageDetailSql(String cityId, String downlink, String taskName, String starttime1, String endtime1, String numType) {
        PrepareSQL sql = new PrepareSQL();

        List<String> taskNameList = new ArrayList<String>();
        if (StringUtil.isNotEmpty(taskName)) {
            taskNameList = Arrays.asList(taskName.split(","));
        }

        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            //-- CITY_NAME, LOID , KD, 上网方式, DEVICE_NAME, 下行签约速率, PON类型, 厂家, 型号, 硬件版本, 软件版本,
            //-- 天翼网关类型，是否千兆猫, 是否支持测速, 测速时间, 测试结果, 测试结果1, 测速结果, 速率百分比, 测速质量， 达标情况
            sql.setSQL("select p.city_id, p.city_name, p.loid, p.KD, p.onlineType, p.oui, p.device_serialnumber, p.downlink, p.PON_TYPE, p.vendor_add, p.device_model, p.hardwareversion, p.softwareversion, ");
            sql.append(" p.device_version_type, p.gbbroadband, p.is_speedtest, p.test_time, p.http_result, p.total_down_pert_deal, p.wan_type ");
            sql.append(" from ( ");
            sql.append("        select a.city_id, a.city_name, a.loid, a.KD, a.onlineType, a.oui, a.device_serialnumber, a.downlink, a.PON_TYPE, a.vendor_add, a.device_model, a.hardwareversion, a.softwareversion, ");
            sql.append("               a.device_version_type, a.gbbroadband, a.is_speedtest, cc.task_id, ");
            sql.append("               FROM_UNIXTIME(t.test_time, '%Y-%m-%d %H:%i:%S') test_time, ");
            sql.append("               cc.wan_type, ");
            sql.append("               cc.http_result, ");
            sql.append("               cc.total_down_pert * 8 / 1024 total_down_pert_deal ");
            sql.append("        from (select tc.city_id, ");
            sql.append("                     tc.city_name, ");
            sql.append("                     d.username loid, ");
            sql.append("                     dd.username KD, ");
            sql.append("                     case when dd.wan_type = 1 then '桥接' when dd.wan_type = 2 then '路由' else '其他' end onlineType, ");
            sql.append("                     c.oui, c.device_serialnumber, c.device_name, ");
            sql.append("                     ee.downlink, ");
            sql.append("                     case when access_style_id = 3 then 'EPON' when access_style_id = 4 then 'GPON' else '其他' end PON_TYPE, ");
            sql.append("                     e.vendor_add, ");
            sql.append("                     f.device_model, ");
            sql.append("                     zz.hardwareversion, ");
            sql.append("                     zz.softwareversion, ");
            sql.append("                     case when kk.device_version_type = 1 then 'E8-C' ");
            sql.append("                          when kk.device_version_type = 8 then '天翼网关1.0' ");
            sql.append("                          when kk.device_version_type = 9 then '天翼网关2.0' ");
            sql.append("                          when kk.device_version_type = 10 then '天翼网关3.0' ");
            sql.append("                          when kk.device_version_type = 11 then '天翼网关4.0' ");
            sql.append("                          else '其他' ");
            sql.append("                          end device_version_type, ");
            sql.append("                     case when kk.gbbroadband = 1 then '是' else '否' end gbbroadband, ");
            sql.append("                     case when kk.is_speedtest = 1 then '是' else '否' end is_speedtest ");
            sql.append("              from hgwcust_serv_info            dd, ");
            sql.append("                   tab_netacc_spead             ee, ");
            sql.append("                   tab_gw_device                c,  ");
            sql.append("                   tab_hgwcustomer              d,  ");
            sql.append("                   tab_vendor                   e,  ");
            sql.append("                   gw_device_model              f,  ");
            sql.append("                   tab_device_version_attribute kk, ");
            sql.append("                   tab_city                     tc, ");
            sql.append("                   tab_devicetype_info          zz  ");
            sql.append("              where c.devicetype_id = kk.devicetype_id ");
            sql.append("                    and dd.username = ee.username ");
            sql.append("                    and c.devicetype_id = zz.devicetype_id ");
            sql.append("                    and dd.user_id = d.user_id ");
            sql.append("                    and dd.serv_type_id = 10 ");
            sql.append("                    and d.city_id = tc.city_id ");
            sql.append("                    and e.vendor_id = f.vendor_id ");
            sql.append("                    and f.device_model_id = c.device_model_id ");
            sql.append("                    and d.device_id = c.device_id ");

            if ("100M".equals(downlink)) {
                sql.append("                and ee.downlink <= 100 ");
            }
            else if ("200M".equals(downlink)) {
                sql.append("                and ee.downlink > 100 ");
                sql.append("                and ee.downlink <= 200 ");
            }
            else if ("300M".equals(downlink)) {
                sql.append("                and ee.downlink > 200 ");
                sql.append("                and ee.downlink <= 300 ");
            }
            else if ("500M".equals(downlink)) {
                sql.append("                and ee.downlink > 300 ");
                sql.append("                and ee.downlink <= 500 ");
            }
            else if ("1000M".equals(downlink)) {
                sql.append("                and ee.downlink > 500 ");
            }

            if (!"-1".equals(cityId)) {
                // 查询地市下的所有地市，包括自己
                List<String> allNextCityIds = CityDAO.getAllNextCityIdsByCityPid(cityId);
                sql.append("                and tc.city_id in (" + StringUtils.weave(allNextCityIds) + ") ");
            }

            sql.append("             ) a, ");


            sql.append("             (select t.test_time, t.wan_type, t.total_down_pert, t.device_serialnumber, t.http_result, t.task_id ");
            sql.append("              from (");
            sql.append("                      SELECT ");
            sql.append("                             @row_number:= CASE WHEN @customer_no=t.device_serialnumber THEN @row_number+1 ELSE 1 END AS rn, ");
            sql.append("                             @customer_no:=t.device_serialnumber as device_serialnumber, t.test_time, t.wan_type, t.http_result, t.task_id, t.total_down_pert ");
            sql.append("                      FROM tab_http_diag_result t, ");
            sql.append("                           tab_ids_task         q, ");
            sql.append("                           (SELECT @row_number:=0, @customer_no:=0) AS temp");
            sql.append("                      where t.task_id = q.task_id ");
            sql.append("                            and t.http_result = 'http质量下载成功' ");
            sql.append("                            and t.total_down_pert != 0 ");
            if (null != taskNameList && taskNameList.size() > 0) {
                sql.append("                        and q.task_name in (" + StringUtils.weave(taskNameList) + ") ");
            }
            if (StringUtil.isNotEmpty(starttime1)) {
                sql.append("                        and t.test_time>=" + starttime1);
            }
            if (StringUtil.isNotEmpty(endtime1)) {
                sql.append("                        and t.test_time<" + endtime1);
            }
            sql.append("                        ORDER BY t.device_serialnumber, t.total_down_pert desc");
            sql.append("                     ) t ");
            sql.append("              where rn = 1 ");
            sql.append("              ) cc ");

            sql.append("        where a.device_serialnumber = cc.device_serialnumber");
            sql.append(" ) p ");

            // 达标了的
            if ("accessStandard".equals(numType)) {
                sql.append(" where (p.wan_type = 'PPPoE_Bridged' and ifnull(p.total_down_pert_deal, 0) >= 0.9 * p.downlink) or");
                sql.append("       (p.wan_type = 'IP_Routed' and ifnull(p.total_down_pert_deal, 0) >= 0.9 * p.downlink)");
            }
        }
        else {
            //-- CITY_NAME, LOID , KD, 上网方式, DEVICE_NAME, 下行签约速率, PON类型, 厂家, 型号, 硬件版本, 软件版本,
            //-- 天翼网关类型，是否千兆猫, 是否支持测速, 测速时间, 测试结果, 测试结果1, 测速结果, 速率百分比, 测速质量， 达标情况
            sql.setSQL("select p.city_id, p.city_name, p.loid, p.KD, p.onlineType, p.oui, p.device_serialnumber, p.downlink, p.PON_TYPE, p.vendor_add, p.device_model, p.hardwareversion, p.softwareversion, ");
            sql.append(" p.device_version_type, p.gbbroadband, p.is_speedtest, p.test_time, p.http_result, p.total_down_pert_deal, p.wan_type ");

            // sql.append(" (round(p.total_down_pert_deal / decode(p.downlink, 0, 9999, p.downlink) * 100, 2)) speedRate, ");
            /*sql.append(" case ");
            sql.append("     when (round(p.total_down_pert_deal / decode(p.downlink, 0, 9999, p.downlink) * 100, 2)) >= 90 then '优' ");
            sql.append("     when (round(p.total_down_pert_deal / decode(p.downlink, 0, 9999, p.downlink) * 100, 2)) >= 80 and ");
            sql.append("          (round(p.total_down_pert_deal / decode(p.downlink, 0, 9999, p.downlink) * 100, 2)) < 90 then '良' ");
            sql.append("     else '差' ");
            sql.append("     end speedQuality, ");*/

            /*sql.append(" case ");
            sql.append("     when ((p.wan_type = 'PPPoE_Bridged' and nvl(p.total_down_pert_deal, 0) >= 0.9 * p.downlink) or ");
            sql.append("          (p.wan_type = 'IP_Routed' and nvl(p.total_down_pert_deal, 0) >= 0.9 * p.downlink)) then '达标' ");
            sql.append("     when ((p.wan_type = 'PPPoE_Bridged' and nvl(p.total_down_pert_deal, 0) < 0.9 * p.downlink) or ");
            sql.append("          (p.wan_type = 'IP_Routed' and nvl(p.total_down_pert_deal, 0) < 0.9 * p.downlink)) then '不达标' ");
            sql.append("     else '测速异常' ");
            sql.append("     end accessStandard ");*/

            sql.append(" from ( ");
            sql.append("        select a.city_id, a.city_name, a.loid, a.KD, a.onlineType, a.oui, a.device_serialnumber, a.downlink, a.PON_TYPE, a.vendor_add, a.device_model, a.hardwareversion, a.softwareversion, ");
            sql.append("               a.device_version_type, a.gbbroadband, a.is_speedtest, cc.task_id, ");
            sql.append("               to_char((TO_DATE('19700101', 'yyyymmdd') + cc.test_time / 86400 + TO_NUMBER(SUBSTR(TZ_OFFSET(sessiontimezone),1,3)) / 24),'yyyy-mm-dd hh24:mi:ss') test_time, ");
            sql.append("               cc.wan_type, ");
            sql.append("               cc.http_result, ");
            // sql.append("               case when cc.http_result = 'http质量下载成功' then '测速成功' else '测速异常' end http_result_one,  ");
            sql.append("               cc.total_down_pert * 8 / 1024 total_down_pert_deal ");
            sql.append("        from (select tc.city_id, ");
            sql.append("                     tc.city_name, ");
            sql.append("                     d.username loid, ");
            sql.append("                     dd.username KD, ");
            sql.append("                     case when dd.wan_type = 1 then '桥接' when dd.wan_type = 2 then '路由' else '其他' end onlineType, ");
            sql.append("                     c.oui, c.device_serialnumber, c.device_name, ");
            sql.append("                     ee.downlink, ");
            sql.append("                     case when access_style_id = 3 then 'EPON' when access_style_id = 4 then 'GPON' else '其他' end PON_TYPE, ");
            sql.append("                     e.vendor_add, ");
            sql.append("                     f.device_model, ");
            sql.append("                     zz.hardwareversion, ");
            sql.append("                     zz.softwareversion, ");
            sql.append("                     case when kk.device_version_type = 1 then 'E8-C' ");
            sql.append("                          when kk.device_version_type = 8 then '天翼网关1.0' ");
            sql.append("                          when kk.device_version_type = 9 then '天翼网关2.0' ");
            sql.append("                          when kk.device_version_type = 10 then '天翼网关3.0' ");
            sql.append("                          when kk.device_version_type = 11 then '天翼网关4.0' ");
            sql.append("                          else '其他' ");
            sql.append("                          end device_version_type, ");
            sql.append("                     case when kk.gbbroadband = 1 then '是' else '否' end gbbroadband, ");
            sql.append("                     case when kk.is_speedtest = 1 then '是' else '否' end is_speedtest ");
            sql.append("              from hgwcust_serv_info            dd, ");
            sql.append("                   tab_netacc_spead             ee, ");
            sql.append("                   tab_gw_device                c,  ");
            sql.append("                   tab_hgwcustomer              d,  ");
            sql.append("                   tab_vendor                   e,  ");
            sql.append("                   gw_device_model              f,  ");
            sql.append("                   tab_device_version_attribute kk, ");
            sql.append("                   tab_city                     tc, ");
            sql.append("                   tab_devicetype_info          zz  ");
            sql.append("              where c.devicetype_id = kk.devicetype_id ");
            sql.append("                    and dd.username = ee.username ");
            sql.append("                    and c.devicetype_id = zz.devicetype_id ");
            sql.append("                    and dd.user_id = d.user_id ");
            sql.append("                    and dd.serv_type_id = 10 ");
            sql.append("                    and d.city_id = tc.city_id ");
            sql.append("                    and e.vendor_id = f.vendor_id ");
            sql.append("                    and f.device_model_id = c.device_model_id ");
            sql.append("                    and d.device_id = c.device_id ");

            if ("100M".equals(downlink)) {
                sql.append("                and ee.downlink <= 100 ");
            }
            else if ("200M".equals(downlink)) {
                sql.append("                and ee.downlink > 100 ");
                sql.append("                and ee.downlink <= 200 ");
            }
            else if ("300M".equals(downlink)) {
                sql.append("                and ee.downlink > 200 ");
                sql.append("                and ee.downlink <= 300 ");
            }
            else if ("500M".equals(downlink)) {
                sql.append("                and ee.downlink > 300 ");
                sql.append("                and ee.downlink <= 500 ");
            }
            else if ("1000M".equals(downlink)) {
                sql.append("                and ee.downlink > 500 ");
            }

            if (!"-1".equals(cityId)) {
                // 查询地市下的所有地市，包括自己
                List<String> allNextCityIds = CityDAO.getAllNextCityIdsByCityPid(cityId);
                sql.append("                and tc.city_id in (" + StringUtils.weave(allNextCityIds) + ") ");
            }

            sql.append("             ) a, ");
            sql.append("             (select t.test_time, t.wan_type, t.total_down_pert, t.device_serialnumber, t.http_result, t.task_id ");
            sql.append("              from (select t.test_time, t.wan_type, t.total_down_pert, t.device_serialnumber, t.http_result, t.task_id, ");
            sql.append("                           row_number() over(partition by t.device_serialnumber order by t.total_down_pert * 8 / 1024 desc) rn ");
            sql.append("                    from tab_http_diag_result t, ");
            sql.append("                         tab_ids_task q ");
            sql.append("                    where t.task_id = q.task_id ");
            sql.append("                          and t.http_result = 'http质量下载成功' ");
            sql.append("                          and t.total_down_pert != 0 ");

            if (null != taskNameList && taskNameList.size() > 0) {
                sql.append("                      and q.task_name in (" + StringUtils.weave(taskNameList) + ") ");
            }
            if (StringUtil.isNotEmpty(starttime1)) {
                sql.append("                      and t.test_time>=" + starttime1);
            }
            if (StringUtil.isNotEmpty(endtime1)) {
                sql.append("                      and t.test_time<" + endtime1);
            }

            sql.append("                     ) t");
            sql.append("              where rn = 1) cc");
            sql.append("        where a.device_serialnumber = cc.device_serialnumber");
            sql.append(" ) p ");

            // 达标了的
            if ("accessStandard".equals(numType)) {
                sql.append(" where (p.wan_type = 'PPPoE_Bridged' and nvl(p.total_down_pert_deal, 0) >= 0.9 * p.downlink) or");
                sql.append("       (p.wan_type = 'IP_Routed' and nvl(p.total_down_pert_deal, 0) >= 0.9 * p.downlink)");
            }
        }


        return sql;
    }

    private Map<String, String> packageDetailMap(ResultSet rs) throws SQLException {
        Map<String, String> map = new HashMap<String, String>();


        String wan_type = rs.getString("wan_type");

        // 测速结果
        String total_down_pert_deal = rs.getString("total_down_pert_deal");
        double totalDownPertDeal = 0;
        if (null != total_down_pert_deal) {
            totalDownPertDeal = Double.parseDouble(total_down_pert_deal);
        }
        // 下行签约速率
        String downlink = rs.getString("downlink");
        double downlinkInt = 0;
        if (null != downlink) {
            downlinkInt = Double.parseDouble(downlink);
        }

        String accessStandard = "测速异常";
        if ("PPPoE_Bridged".equals(wan_type) || "IP_Routed".equals(wan_type)) {
            if (totalDownPertDeal >= 0.9 * downlinkInt) {
                accessStandard = "达标";
            }
            else {
                accessStandard = "不达标";
            }
        }

        String speedQuality = "差";
        if (totalDownPertDeal >= 0.9 * downlinkInt) {
            speedQuality = "优";
        }
        else if (totalDownPertDeal >= 0.8 * downlinkInt && totalDownPertDeal < 0.9 * downlinkInt) {
            speedQuality = "良";
        }

        String http_result = rs.getString("http_result");
        String http_result_one = "测速异常";
        if ("http质量下载成功".equals(http_result)) {
            http_result_one = "测速成功";
        }


        //-- CITY_NAME, LOID , KD, 上网方式, DEVICE_NAME, 下行签约速率, PON类型, 厂家, 型号, 硬件版本, 软件版本,
        //-- 天翼网关类型，是否千兆猫, 是否支持测速, 测速时间, 测试结果, 测试结果1, 测速结果, 速率百分比, 测速质量， 达标情况
        String city_id = rs.getString("city_id");
        map.put("city_id", city_id);
        // CITY_NAME
        map.put("city_name", rs.getString("city_name"));
        // LOID
        map.put("loid", rs.getString("loid"));
        // KD
        map.put("KD", rs.getString("KD"));
        // 上网方式
        map.put("onlineType", rs.getString("onlineType"));
        // oui
        map.put("oui", rs.getString("oui"));
        // device_serialnumber
        map.put("device_serialnumber", rs.getString("device_serialnumber"));
        // DEVICE_NAME
        map.put("device_name", rs.getString("oui") + "-" + rs.getString("device_serialnumber"));
        // 下行签约速率
        map.put("downlink", rs.getString("downlink"));
        // PON类型
        map.put("PON_TYPE", rs.getString("PON_TYPE"));
        // 厂家
        map.put("vendor_add", rs.getString("vendor_add"));
        // 型号
        map.put("device_model", rs.getString("device_model"));
        // 硬件版本
        map.put("hardwareversion", rs.getString("hardwareversion"));
        // 软件版本
        map.put("softwareversion", rs.getString("softwareversion"));
        // 天翼网关类型
        map.put("device_version_type", rs.getString("device_version_type"));
        // 是否千兆猫
        map.put("gbbroadband", rs.getString("gbbroadband"));
        // 是否支持测速
        map.put("is_speedtest", rs.getString("is_speedtest"));
        // 测速时间
        map.put("test_time", rs.getString("test_time"));
        // 测试结果  用不到了，用下面的测试结果1
        map.put("http_result", http_result);
        // 测试结果1
        map.put("http_result_one", http_result_one);
        // 取整
        Long totalDownPertDealLong = Math.round(totalDownPertDeal);
        // 测速结果
        map.put("total_down_pert_deal", totalDownPertDealLong.toString());
        // 测速质量
        map.put("speedQuality", speedQuality);
        // 达标情况
        map.put("accessStandard", accessStandard);

        return map;
    }

}
