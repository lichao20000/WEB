package com.linkage.module.gwms.report.dao;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

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
public class SingleBusEquipmentDAO extends SuperDAO {

    private static Logger logger = LoggerFactory.getLogger(SingleBusEquipmentDAO.class);

    private List<Map> calculateSingleBusEquipmentNum(List<Map> singleBusEquipmentList) {
        List<Map> resultMapList = new ArrayList<Map>();

        int broadbandNumTotal = 0;
        int voipNumTotal = 0;
        int iptvNumTotal = 0;

        // 获取省中心和一级地市
        List<String> firstCityIdList = getFirstCityContainCenter();

        for (String firstCityId : firstCityIdList) {
            Map resultMap = new HashMap();

            String cityName = CityDAO.getCityName(firstCityId);
            resultMap.put("cityName", cityName);
            resultMap.put("cityId", firstCityId);

            List<String> allNextCityIds = new ArrayList<String>();
            // 先添加自己
            allNextCityIds.add(firstCityId);
            if (!"00".equals(firstCityId)) {
                // 不是省中心，则查询地市下的所有地市
                getAllNextCityContainSelf(firstCityId, allNextCityIds);
            }


            // 家庭网关宽带业务 10、  家庭网关VOIP业务:14 、  家庭网关IPTV业务:11
            int broadbandNum = 0;
            int voipNum = 0;
            int iptvNum = 0;

            for (Map singleBusEquipmentMap : singleBusEquipmentList) {
                String deviceCityId = StringUtil.getStringValue(singleBusEquipmentMap, "city_id");

                if (allNextCityIds.contains(deviceCityId)) {
                    String serv_type_id = StringUtil.getStringValue(singleBusEquipmentMap, "serv_type_id");
                    if ("10".equals(serv_type_id)) {
                        broadbandNum++;
                    }
                    else if ("11".equals(serv_type_id)) {
                        iptvNum++;
                    }
                    else if ("14".equals(serv_type_id)) {
                        voipNum++;
                    }
                }
            }

            broadbandNumTotal = broadbandNumTotal + broadbandNum;
            voipNumTotal = voipNumTotal + voipNum;
            iptvNumTotal = iptvNumTotal + iptvNum;

            resultMap.put("broadbandNum", broadbandNum);
            resultMap.put("voipNum", voipNum);
            resultMap.put("iptvNum", iptvNum);

            resultMapList.add(resultMap);
        }

        Map resultMapTotal = new HashMap();
        resultMapTotal.put("cityName", "合计");
        resultMapTotal.put("cityId", "-1");
        resultMapTotal.put("broadbandNum", broadbandNumTotal);
        resultMapTotal.put("voipNum", voipNumTotal);
        resultMapTotal.put("iptvNum", iptvNumTotal);
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
     * * 单个业务统计设备资源报表查询记录
     *
     * @param starttime1
     *            开始时间
     * @param endtime1
     *            结束时间
     * @author ou.yuan
     * @return List<Map>
     */
    public List<Map> getSingleBusEquipmentList(String starttime1, String endtime1) {
        logger.debug("getSingleBusEquipmentList({},{},{},{},{},{},{},{},{},{},{})");
        List<Map> resultMapList = new ArrayList<Map>();

        StringBuffer qrySingleBusEquipmentSql = new StringBuffer();

        qrySingleBusEquipmentSql.append("select c.city_id, a.serv_type_id ");
        qrySingleBusEquipmentSql.append("from hgwcust_serv_info a, ");
        qrySingleBusEquipmentSql.append("tab_hgwcustomer b, ");
        qrySingleBusEquipmentSql.append("tab_gw_device c, ");
        qrySingleBusEquipmentSql.append("tab_vendor d, ");
        qrySingleBusEquipmentSql.append("gw_device_model e, ");
        qrySingleBusEquipmentSql.append("tab_devicetype_info f ");
        qrySingleBusEquipmentSql.append("where a.user_id = b.user_id ");
        qrySingleBusEquipmentSql.append("and b.device_id = c.device_id ");
        qrySingleBusEquipmentSql.append("and c.vendor_id = d.vendor_id ");
        qrySingleBusEquipmentSql.append("and c.device_model_id = e.device_model_id ");
        qrySingleBusEquipmentSql.append("and c.devicetype_id = f.devicetype_id ");
        if (StringUtil.isNotEmpty(starttime1)) {
            qrySingleBusEquipmentSql.append(" and a.OPENDATE >=").append(starttime1);
        }
        if (StringUtil.isNotEmpty(endtime1)) {
            qrySingleBusEquipmentSql.append(" and a.OPENDATE <").append(endtime1);
        }


        PrepareSQL psql = new PrepareSQL(qrySingleBusEquipmentSql.toString());
        List<Map> singleBusEquipmentList = jt.queryForList(psql.getSQL());

        if (singleBusEquipmentList == null || singleBusEquipmentList.isEmpty()) {
            logger.warn("SingleBusEquipmentDAO.getSingleBusEquipmentList() singleBusEquipmentList is empty!");
        }
        else {
            // 统计返回给页面的参数
            resultMapList = calculateSingleBusEquipmentNum(singleBusEquipmentList);
        }

        return resultMapList;
    }


    /**
     * 单个业务统计设备资源报表详情数量
     * @param cityId
     * @param starttime1
     * @param endtime1
     * @return
     */
    public int getSingleBusEquipmentDetailCount(String cityId, String starttime1, String endtime1, String servTypeId) {
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

        return jt.queryForInt(sql.getSQL());
    }


    /**
     * 单个业务统计设备资源报表详情列表  比导出Excel多了分页
     * @param cityId
     * @param curPage_splitPage
     * @param num_splitPage
     * @param starttime1
     * @param endtime1
     * @return
     */
    public List<Map> querySingleBusEquipmentDetailList(String cityId, int curPage_splitPage, int num_splitPage, String starttime1, String endtime1, String servTypeId) {
        PrepareSQL sql = packageDetailSql(cityId, starttime1, endtime1, servTypeId);

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
     * 单个业务统计设备资源报表详情到处到Excel
     * @param cityId
     * @param starttime1
     * @param endtime1
     * @return
     */
    public List<Map> getSingleBusEquipmentDetailExcel(String cityId, String starttime1, String endtime1, String servTypeId) {
        PrepareSQL sql = packageDetailSql(cityId, starttime1, endtime1, servTypeId);

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

    private PrepareSQL packageDetailSql(String cityId, String starttime1, String endtime1, String servTypeId) {
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
