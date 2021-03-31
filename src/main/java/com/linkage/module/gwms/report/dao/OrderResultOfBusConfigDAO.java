package com.linkage.module.gwms.report.dao;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ou.yuan
 * @since 2020-10-20
 * @category com.linkage.module.gwms.report.dao
 * 
 */
public class OrderResultOfBusConfigDAO extends SuperDAO {

    private static Logger logger = LoggerFactory.getLogger(OrderResultOfBusConfigDAO.class);

    private List<Map> calculateOrderResultOfBusConfigNum(List<Map> orderResultOfBusConfigList) {
        List<Map> resultMapList = new ArrayList<Map>();

        // 获取省中心和一级地市
        List<String> firstCityIdList = getFirstCityContainCenter();

        logger.warn("OrderResultOfBusConfigDAO firstCityIdList size is {} ===============", firstCityIdList.size());
        logger.warn("OrderResultOfBusConfigDAO firstCityIdList is {} ===============", firstCityIdList.toString());

        int broadbandNumTotal = 0;
        int broadbandSuccessNumTotal = 0;
        BigDecimal broadbandSuccessRateTotal = BigDecimal.ZERO;

        int iptvNumTotal = 0;
        int iptvSuccessNumTotal = 0;
        BigDecimal iptvSuccessRateTotal = BigDecimal.ZERO;

        int voipNumTotal = 0;
        int voipSuccessNumTotal = 0;
        BigDecimal voipSuccessRateTotal = BigDecimal.ZERO;

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

            logger.warn("OrderResultOfBusConfigDAO allNextCityIds size is {} ===============", allNextCityIds.size());
            logger.warn("OrderResultOfBusConfigDAO allNextCityIds is {} ===============", allNextCityIds.toString());


            // 家庭网关宽带业务 10、   家庭网关IPTV业务:11、   家庭网关VOIP业务:14
            int broadbandNum = 0;
            int broadbandSuccessNum = 0;
            BigDecimal broadbandSuccessRate = BigDecimal.ZERO;

            int iptvNum = 0;
            int iptvSuccessNum = 0;
            BigDecimal iptvSuccessRate = BigDecimal.ZERO;

            int voipNum = 0;
            int voipSuccessNum = 0;
            BigDecimal voipSuccessRate = BigDecimal.ZERO;


            for (Map orderResultOfBusConfigMap : orderResultOfBusConfigList) {
                String deviceCityId = StringUtil.getStringValue(orderResultOfBusConfigMap, "city_id");

                if (allNextCityIds.contains(deviceCityId)) {
                    String serv_type_id = StringUtil.getStringValue(orderResultOfBusConfigMap, "serv_type_id");
                    String open_status = StringUtil.getStringValue(orderResultOfBusConfigMap, "open_status");

                    if ("10".equals(serv_type_id)) {
                        broadbandNum++;
                        if ("1".equals(open_status)) {
                            broadbandSuccessNum++;
                        }
                    }
                    else if ("11".equals(serv_type_id)) {
                        iptvNum++;
                        if ("1".equals(open_status)) {
                            iptvSuccessNum++;
                        }
                    }
                    else if ("14".equals(serv_type_id)) {
                        voipNum++;
                        if ("1".equals(open_status)) {
                            voipSuccessNum++;
                        }
                    }
                }
            }

            broadbandNumTotal = broadbandNumTotal + broadbandNum;
            broadbandSuccessNumTotal = broadbandSuccessNumTotal + broadbandSuccessNum;

            iptvNumTotal = iptvNumTotal + iptvNum;
            iptvSuccessNumTotal = iptvSuccessNumTotal + iptvSuccessNum;

            voipNumTotal = voipNumTotal + voipNum;
            voipSuccessNumTotal = voipSuccessNumTotal + voipSuccessNum;


            if (broadbandNum > 0) {
                broadbandSuccessRate = new BigDecimal(broadbandSuccessNum * 100).divide(new BigDecimal(broadbandNum), 2, BigDecimal.ROUND_HALF_UP);
            }
            if (iptvNum > 0) {
                iptvSuccessRate = new BigDecimal(iptvSuccessNum * 100).divide(new BigDecimal(iptvNum), 2, BigDecimal.ROUND_HALF_UP);
            }
            if (voipNum > 0) {
                voipSuccessRate = new BigDecimal(voipSuccessNum * 100).divide(new BigDecimal(voipNum), 2, BigDecimal.ROUND_HALF_UP);
            }

            resultMap.put("broadbandNum", broadbandNum);
            resultMap.put("broadbandSuccessNum", broadbandSuccessNum);
            resultMap.put("broadbandSuccessRate", broadbandSuccessRate);

            resultMap.put("iptvNum", iptvNum);
            resultMap.put("iptvSuccessNum", iptvSuccessNum);
            resultMap.put("iptvSuccessRate", iptvSuccessRate);

            resultMap.put("voipNum", voipNum);
            resultMap.put("voipSuccessNum", voipSuccessNum);
            resultMap.put("voipSuccessRate", voipSuccessRate);

            resultMapList.add(resultMap);
        }

        Map resultMapTotal = new HashMap();

        resultMapTotal.put("cityName", "合计");
        resultMapTotal.put("cityId", "-1");

        if (broadbandNumTotal > 0) {
            broadbandSuccessRateTotal = new BigDecimal(broadbandSuccessNumTotal * 100).divide(new BigDecimal(broadbandNumTotal), 2, BigDecimal.ROUND_HALF_UP);
        }
        if (iptvNumTotal > 0) {
            iptvSuccessRateTotal = new BigDecimal(iptvSuccessNumTotal * 100).divide(new BigDecimal(iptvNumTotal), 2, BigDecimal.ROUND_HALF_UP);
        }
        if (voipNumTotal > 0) {
            voipSuccessRateTotal = new BigDecimal(voipSuccessNumTotal * 100).divide(new BigDecimal(voipNumTotal), 2, BigDecimal.ROUND_HALF_UP);
        }

        resultMapTotal.put("broadbandNum", broadbandNumTotal);
        resultMapTotal.put("broadbandSuccessNum", broadbandSuccessNumTotal);
        resultMapTotal.put("broadbandSuccessRate", broadbandSuccessRateTotal);

        resultMapTotal.put("iptvNum", iptvNumTotal);
        resultMapTotal.put("iptvSuccessNum", iptvSuccessNumTotal);
        resultMapTotal.put("iptvSuccessRate", iptvSuccessRateTotal);

        resultMapTotal.put("voipNum", voipNumTotal);
        resultMapTotal.put("voipSuccessNum", voipSuccessNumTotal);
        resultMapTotal.put("voipSuccessRate", voipSuccessRateTotal);

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
     * * 业务配置工单结果的统计报表查询记录
     *
     * @param starttime1
     *            开始时间
     * @param endtime1
     *            结束时间
     * @author ou.yuan
     * @return List<Map>
     */
    public List<Map> getOrderResultOfBusConfigList(String starttime1, String endtime1) {
        logger.debug("getOrderResultOfBusConfigList({},{},{},{},{},{},{},{},{},{},{})");
        List<Map> resultMapList = new ArrayList<Map>();

        StringBuffer qryOrderResultOfBusConfigSql = new StringBuffer();

        qryOrderResultOfBusConfigSql.append("select c.city_id, a.serv_type_id, a.open_status ");
        qryOrderResultOfBusConfigSql.append("from hgwcust_serv_info a, ");
        qryOrderResultOfBusConfigSql.append("tab_hgwcustomer b, ");
        qryOrderResultOfBusConfigSql.append("tab_gw_device c, ");
        qryOrderResultOfBusConfigSql.append("tab_vendor d, ");
        qryOrderResultOfBusConfigSql.append("gw_device_model e, ");
        qryOrderResultOfBusConfigSql.append("tab_devicetype_info f ");
        qryOrderResultOfBusConfigSql.append("where a.user_id = b.user_id ");
        qryOrderResultOfBusConfigSql.append("and b.device_id = c.device_id ");
        qryOrderResultOfBusConfigSql.append("and c.vendor_id = d.vendor_id ");
        qryOrderResultOfBusConfigSql.append("and c.device_model_id = e.device_model_id ");
        qryOrderResultOfBusConfigSql.append("and c.devicetype_id = f.devicetype_id ");
        if (StringUtil.isNotEmpty(starttime1)) {
            qryOrderResultOfBusConfigSql.append(" and a.OPENDATE >=").append(starttime1);
        }
        if (StringUtil.isNotEmpty(endtime1)) {
            qryOrderResultOfBusConfigSql.append(" and a.OPENDATE <").append(endtime1);
        }


        PrepareSQL psql = new PrepareSQL(qryOrderResultOfBusConfigSql.toString());
        List<Map> orderResultOfBusConfigList = jt.queryForList(psql.getSQL());

        if (orderResultOfBusConfigList == null || orderResultOfBusConfigList.isEmpty()) {
            logger.warn("OrderResultOfBusConfigDAO.getOrderResultOfBusConfigList() orderResultOfBusConfigList is empty!");
        }
        else {
            // 统计返回给页面的参数
            resultMapList = calculateOrderResultOfBusConfigNum(orderResultOfBusConfigList);
        }

        return resultMapList;
    }


    /**
     * 业务配置工单结果的统计报表详情数量
     * @param cityId
     * @param starttime1
     * @param endtime1
     * @return
     */
    public int getOrderResultOfBusConfigDetailCount(String cityId, String starttime1, String endtime1, String servTypeId, String numType) {
        PrepareSQL sql = new PrepareSQL();

        sql.setSQL("select count(*) ");
        sql.append("from hgwcust_serv_info a, ");
        sql.append("tab_hgwcustomer b, ");
        sql.append("tab_gw_device c, ");
        sql.append("tab_vendor d, ");
        sql.append("gw_device_model e, ");
        sql.append("tab_devicetype_info f, ");
        sql.append("tab_city g ");
        sql.append("where a.user_id = b.user_id ");
        sql.append("and b.device_id = c.device_id ");
        sql.append("and c.vendor_id = d.vendor_id ");
        sql.append("and c.device_model_id = e.device_model_id ");
        sql.append("and c.devicetype_id = f.devicetype_id ");
        sql.append("and c.city_id = g.city_id ");

        if (!"-1".equals(cityId)) {
            List<String> allNextCityIds = new ArrayList<String>();
            allNextCityIds.add(cityId);
            // 不是省中心，则查询地市下的所有地市
            if (!"00".equals(cityId)) {
                getAllNextCityContainSelf(cityId, allNextCityIds);
            }
            sql.append("and c.city_id in (" + StringUtils.weave(allNextCityIds) + ") ");
        }

        if (StringUtil.isNotEmpty(starttime1)) {
            sql.append(" and a.OPENDATE>=" + starttime1);
        }
        if (StringUtil.isNotEmpty(endtime1)) {
            sql.append(" and a.OPENDATE<" + endtime1);
        }

        if (StringUtil.isNotEmpty(servTypeId)) {
            sql.append(" and a.SERV_TYPE_ID =" + servTypeId);
        }
        if (StringUtil.isNotEmpty(numType)) {
            sql.append(" and a.OPEN_STATUS = 1");
        }

        return jt.queryForInt(sql.getSQL());
    }


    /**
     * 业务配置工单结果的统计报表详情列表  比导出Excel多了分页
     * @param cityId
     * @param curPage_splitPage
     * @param num_splitPage
     * @param starttime1
     * @param endtime1
     * @return
     */
    public List<Map> queryOrderResultOfBusConfigDetailList(String cityId, int curPage_splitPage, int num_splitPage, String starttime1, String endtime1, String servTypeId, String numType) {
        PrepareSQL sql = packageDetailSql(cityId, starttime1, endtime1, servTypeId, numType);

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
     * 业务配置工单结果的统计报表详情到处到Excel
     * @param cityId
     * @param starttime1
     * @param endtime1
     * @return
     */
    public List<Map> getOrderResultOfBusConfigDetailExcel(String cityId, String starttime1, String endtime1, String servTypeId, String numType) {
        PrepareSQL sql = packageDetailSql(cityId, starttime1, endtime1, servTypeId, numType);

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

    private PrepareSQL packageDetailSql(String cityId, String starttime1, String endtime1, String servTypeId, String numType) {
        PrepareSQL sql = new PrepareSQL();

        //-- LOID，属地，用户账号，设备序列号，设备厂商，设备型号，设备硬件版本，软件版本
        sql.setSQL("select c.city_id, g.city_name, b.username as loid, a.username, c.oui, c.device_serialnumber, ");
        sql.append("d.vendor_add, e.device_model, f.hardwareversion, f.softwareversion ");
        sql.append("from hgwcust_serv_info a, ");
        sql.append("tab_hgwcustomer b, ");
        sql.append("tab_gw_device c, ");
        sql.append("tab_vendor d, ");
        sql.append("gw_device_model e, ");
        sql.append("tab_devicetype_info f, ");
        sql.append("tab_city g ");
        sql.append("where a.user_id = b.user_id ");
        sql.append("and b.device_id = c.device_id ");
        sql.append("and c.vendor_id = d.vendor_id ");
        sql.append("and c.device_model_id = e.device_model_id ");
        sql.append("and c.devicetype_id = f.devicetype_id ");
        sql.append("and c.city_id = g.city_id ");

        if (!"-1".equals(cityId)) {
            List<String> allNextCityIds = new ArrayList<String>();
            allNextCityIds.add(cityId);
            // 不是省中心，则查询地市下的所有地市
            if (!"00".equals(cityId)) {
                getAllNextCityContainSelf(cityId, allNextCityIds);
            }
            sql.append("and c.city_id in (" + StringUtils.weave(allNextCityIds) + ") ");
        }

        if (StringUtil.isNotEmpty(starttime1)) {
            sql.append(" and a.OPENDATE>=" + starttime1);
        }
        if (StringUtil.isNotEmpty(endtime1)) {
            sql.append(" and a.OPENDATE<" + endtime1);
        }

        if (StringUtil.isNotEmpty(servTypeId)) {
            sql.append(" and a.SERV_TYPE_ID =" + servTypeId);
        }

        if (StringUtil.isNotEmpty(numType)) {
            sql.append(" and a.OPEN_STATUS = 1");
        }

        return sql;
    }

    private Map<String, String> packageDetailMap(ResultSet rs) throws SQLException {
        Map<String, String> map = new HashMap<String, String>();

        //-- LOID，属地，用户账号，设备序列号，设备厂商，设备型号，设备硬件版本，软件版本
        String city_id = rs.getString("city_id");
        map.put("city_id", city_id);
        // CITY_NAME
        map.put("city_name", rs.getString("city_name"));
        // LOID
        map.put("loid", rs.getString("loid"));
        // 用户账号
        map.put("username", rs.getString("username"));

        // oui
        map.put("oui", rs.getString("oui"));
        // device_serialnumber
        map.put("device_serialnumber", rs.getString("device_serialnumber"));
        // 设备序列号
        map.put("device_name", rs.getString("oui") + "-" + rs.getString("device_serialnumber"));

        // 设备厂商
        map.put("vendor_add", rs.getString("vendor_add"));
        // 设备型号
        map.put("device_model", rs.getString("device_model"));
        // 硬件版本
        map.put("hardwareversion", rs.getString("hardwareversion"));
        // 软件版本
        map.put("softwareversion", rs.getString("softwareversion"));

        return map;
    }

}
