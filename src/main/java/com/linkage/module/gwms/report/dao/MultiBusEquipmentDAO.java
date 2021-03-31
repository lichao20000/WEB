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
public class MultiBusEquipmentDAO extends SuperDAO {

    private static Logger logger = LoggerFactory.getLogger(MultiBusEquipmentDAO.class);

    private List<Map> calculateMultiBusEquipmentNum(List<Map> multiBusEquipmentList) {
        List<Map> resultMapList = new ArrayList<Map>();

        int deviceNumTotal = 0;
        // 获取省中心和一级地市
        List<String> firstCityIdList = getFirstCityContainCenter();

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

            // 设备数量
            int deviceNum = 0;

            for (Map multiBusEquipmentMap : multiBusEquipmentList) {
                String deviceCityId = StringUtil.getStringValue(multiBusEquipmentMap, "city_id");

                if (allNextCityIds.contains(deviceCityId)) {
                    int num = StringUtil.getIntValue(multiBusEquipmentMap, "num");
                    deviceNum = deviceNum + num;
                }
            }

            deviceNumTotal = deviceNumTotal + deviceNum;

            resultMap.put("deviceNum", deviceNum);
            resultMapList.add(resultMap);
        }

        Map resultMapTotal = new HashMap();
        resultMapTotal.put("cityName", "合计");
        resultMapTotal.put("cityId", "-1");
        resultMapTotal.put("deviceNum", deviceNumTotal);
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
     * * 多个业务统计设备资源报表查询记录
     *
     * @author ou.yuan
     * @return List<Map>
     */
    public List<Map> getMultiBusEquipmentList(String servTypeIdString) {
        logger.debug("getMultiBusEquipmentList({},{},{},{},{},{},{},{},{},{},{})");
        List<Map> resultMapList = new ArrayList<Map>();

        List<String> servTypeIdList = new ArrayList<String>();
        if (StringUtil.isNotEmpty(servTypeIdString)) {
            servTypeIdList = Arrays.asList(servTypeIdString.split(","));
        }

        StringBuffer qryMultiBusEquipmentSql = new StringBuffer();

        qryMultiBusEquipmentSql.append("select t.city_id, count(*) as num from ( ");

        qryMultiBusEquipmentSql.append("select distinct c.city_id, b.username as loid, c.oui, c.device_serialnumber, ");
        qryMultiBusEquipmentSql.append("d.vendor_add, e.device_model, f.hardwareversion, f.softwareversion ");
        qryMultiBusEquipmentSql.append("from ");

        if (servTypeIdList.size() == 1) {
            qryMultiBusEquipmentSql.append("(select user_id ");
            qryMultiBusEquipmentSql.append(" from (select user_id ");
            qryMultiBusEquipmentSql.append("       from hgwcust_serv_info h ");
            qryMultiBusEquipmentSql.append("       where h.serv_type_id = " + servTypeIdList.get(0) + ") k) a, ");
        }
        else if (servTypeIdList.size() == 2) {
            qryMultiBusEquipmentSql.append("(select user_id ");
            qryMultiBusEquipmentSql.append(" from (select user_id ");
            qryMultiBusEquipmentSql.append("       from hgwcust_serv_info h ");
            qryMultiBusEquipmentSql.append("       where h.serv_type_id =  " + servTypeIdList.get(0));
            qryMultiBusEquipmentSql.append("       and exists (select * from hgwcust_serv_info t where h.user_id = t.user_id and t.serv_type_id = " + servTypeIdList.get(1) + ")) k) a, ");
        }
        else {
            qryMultiBusEquipmentSql.append("(select user_id ");
            qryMultiBusEquipmentSql.append(" from hgwcust_serv_info p ");
            qryMultiBusEquipmentSql.append(" where p.serv_type_id = 14 ");
            qryMultiBusEquipmentSql.append(" and exists (select user_id from ");
            qryMultiBusEquipmentSql.append("                            (select user_id ");
            qryMultiBusEquipmentSql.append("                             from hgwcust_serv_info h ");
            qryMultiBusEquipmentSql.append("                             where h.serv_type_id = 10 ");
            qryMultiBusEquipmentSql.append("                             and exists (select * from hgwcust_serv_info t where h.user_id = t.user_id and t.serv_type_id = 11) ");
            qryMultiBusEquipmentSql.append("                             ) k ");
            qryMultiBusEquipmentSql.append("             where k.user_id = p.user_id) ");
            qryMultiBusEquipmentSql.append(" ) a, ");
        }

        qryMultiBusEquipmentSql.append("tab_hgwcustomer b, ");
        qryMultiBusEquipmentSql.append("tab_gw_device c, ");
        qryMultiBusEquipmentSql.append("tab_vendor d, ");
        qryMultiBusEquipmentSql.append("gw_device_model e, ");
        qryMultiBusEquipmentSql.append("tab_devicetype_info f ");
        qryMultiBusEquipmentSql.append("where a.user_id = b.user_id ");
        qryMultiBusEquipmentSql.append("and b.device_id = c.device_id ");
        qryMultiBusEquipmentSql.append("and c.vendor_id = d.vendor_id ");
        qryMultiBusEquipmentSql.append("and c.device_model_id = e.device_model_id ");
        qryMultiBusEquipmentSql.append("and c.devicetype_id = f.devicetype_id ");

        /*if (null != servTypeIdList && servTypeIdList.size() > 0) {
            qryMultiBusEquipmentSql.append(" and a.SERV_TYPE_ID in (" + StringUtils.weave(servTypeIdList) + ") ");
        }*/
        qryMultiBusEquipmentSql.append(" ) t");
        qryMultiBusEquipmentSql.append(" GROUP BY t.city_id");


        PrepareSQL psql = new PrepareSQL(qryMultiBusEquipmentSql.toString());
        List<Map> multiBusEquipmentList = jt.queryForList(psql.getSQL());

        if (multiBusEquipmentList == null || multiBusEquipmentList.isEmpty()) {
            logger.warn("MultiBusEquipmentDAO.getMultiBusEquipmentList() multiBusEquipmentList is empty!");
        }
        else {
            // 统计返回给页面的参数
            resultMapList = calculateMultiBusEquipmentNum(multiBusEquipmentList);
        }

        return resultMapList;
    }


    /**
     * 多个业务统计设备资源报表详情数量
     * @param cityId
     * @param servTypeIdString
     * @return
     */
    public int getMultiBusEquipmentDetailCount(String cityId, String servTypeIdString) {
        List<String> servTypeIdList = new ArrayList<String>();
        if (StringUtil.isNotEmpty(servTypeIdString)) {
            servTypeIdList = Arrays.asList(servTypeIdString.split(","));
        }

        PrepareSQL sql = new PrepareSQL();
        sql.setSQL("select count(*) from ( ");

        sql.append("select distinct c.city_id, g.city_name, b.username as loid, c.oui, c.device_serialnumber, ");
        sql.append("d.vendor_add, e.device_model, f.hardwareversion, f.softwareversion ");
        sql.append("from ");

        if (servTypeIdList.size() == 1) {
            sql.append("(select user_id ");
            sql.append(" from (select user_id ");
            sql.append("       from hgwcust_serv_info h ");
            sql.append("       where h.serv_type_id = " + servTypeIdList.get(0) + ") k) a, ");
        }
        else if (servTypeIdList.size() == 2) {
            sql.append("(select user_id ");
            sql.append(" from (select user_id ");
            sql.append("       from hgwcust_serv_info h ");
            sql.append("       where h.serv_type_id =  " + servTypeIdList.get(0));
            sql.append("       and exists (select * from hgwcust_serv_info t where h.user_id = t.user_id and t.serv_type_id = " + servTypeIdList.get(1) + ")) k) a, ");
        }
        else {
            sql.append("(select user_id ");
            sql.append(" from hgwcust_serv_info p ");
            sql.append(" where p.serv_type_id = 14 ");
            sql.append(" and exists (select user_id from ");
            sql.append("                            (select user_id ");
            sql.append("                             from hgwcust_serv_info h ");
            sql.append("                             where h.serv_type_id = 10 ");
            sql.append("                             and exists (select * from hgwcust_serv_info t where h.user_id = t.user_id and t.serv_type_id = 11) ");
            sql.append("                             ) k ");
            sql.append("             where k.user_id = p.user_id) ");
            sql.append(" ) a, ");
        }

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

        /*if (null != servTypeIdList && servTypeIdList.size() > 0) {
            sql.append(" and a.SERV_TYPE_ID in (" + StringUtils.weave(servTypeIdList) + ") ");
        }*/

        sql.append(") t ");

        return jt.queryForInt(sql.getSQL());
    }


    /**
     * 多个业务统计设备资源报表详情列表  比导出Excel多了分页
     * @param cityId
     * @param curPage_splitPage
     * @param num_splitPage
     * @param servTypeIdString
     * @return
     */
    public List<Map> queryMultiBusEquipmentDetailList(String cityId, int curPage_splitPage, int num_splitPage, String servTypeIdString) {
        PrepareSQL sql = packageDetailSql(cityId, servTypeIdString);

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
     * 多个业务统计设备资源报表详情到处到Excel
     * @param cityId
     * @param servTypeIdString
     * @return
     */
    public List<Map> getMultiBusEquipmentDetailExcel(String cityId, String servTypeIdString) {
        PrepareSQL sql = packageDetailSql(cityId, servTypeIdString);

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

    private PrepareSQL packageDetailSql(String cityId, String servTypeIdString) {
        PrepareSQL sql = new PrepareSQL();

        List<String> servTypeIdList = new ArrayList<String>();
        if (StringUtil.isNotEmpty(servTypeIdString)) {
            servTypeIdList = Arrays.asList(servTypeIdString.split(","));
        }

        //-- LOID，属地，设备序列号，设备厂商，设备型号，设备硬件版本，软件版本
        sql.setSQL("select distinct c.city_id, g.city_name, b.username as loid, c.oui, c.device_serialnumber, ");
        sql.append("d.vendor_add, e.device_model, f.hardwareversion, f.softwareversion ");
        sql.append("from ");

        if (servTypeIdList.size() == 1) {
            sql.append("(select user_id ");
            sql.append(" from (select user_id ");
            sql.append("       from hgwcust_serv_info h ");
            sql.append("       where h.serv_type_id = " + servTypeIdList.get(0) + ") k) a, ");
        }
        else if (servTypeIdList.size() == 2) {
            sql.append("(select user_id ");
            sql.append(" from (select user_id ");
            sql.append("       from hgwcust_serv_info h ");
            sql.append("       where h.serv_type_id =  " + servTypeIdList.get(0));
            sql.append("       and exists (select * from hgwcust_serv_info t where h.user_id = t.user_id and t.serv_type_id = " + servTypeIdList.get(1) + ")) k) a, ");
        }
        else {
            sql.append("(select user_id ");
            sql.append(" from hgwcust_serv_info p ");
            sql.append(" where p.serv_type_id = 14 ");
            sql.append(" and exists (select user_id from ");
            sql.append("                            (select user_id ");
            sql.append("                             from hgwcust_serv_info h ");
            sql.append("                             where h.serv_type_id = 10 ");
            sql.append("                             and exists (select * from hgwcust_serv_info t where h.user_id = t.user_id and t.serv_type_id = 11) ");
            sql.append("                             ) k ");
            sql.append("             where k.user_id = p.user_id) ");
            sql.append(" ) a, ");
        }

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
            sql.append(" and c.city_id in (" + StringUtils.weave(allNextCityIds) + ") ");
        }

        /*if (null != servTypeIdList && servTypeIdList.size() > 0) {
            sql.append(" and a.SERV_TYPE_ID in (" + StringUtils.weave(servTypeIdList) + ") ");
        }*/

        return sql;
    }

    private Map<String, String> packageDetailMap(ResultSet rs) throws SQLException {
        Map<String, String> map = new HashMap<String, String>();

        //-- LOID，属地，设备序列号，设备厂商，设备型号，设备硬件版本，软件版本
        String city_id = rs.getString("city_id");
        map.put("city_id", city_id);
        // CITY_NAME
        map.put("city_name", rs.getString("city_name"));
        // LOID
        map.put("loid", rs.getString("loid"));
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
