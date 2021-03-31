package com.linkage.module.gwms.report.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author ou.yuan
 * @since 2020-9-3
 * @category com.linkage.module.gwms.report.dao
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class QueryBatchRestartDeviceDAO extends SuperDAO 
{

    private static Logger logger = LoggerFactory.getLogger(QueryBatchRestartDeviceDAO.class);

    private List<Map> calculateDeviceNum(List<Map> restartDeviceIdList) 
    {
        List<Map> resultMapList = new ArrayList<Map>();

        // 获取省中心和一级地市
        List<String> firstCityIdList = CityDAO.getNextCityIdsByCityPid("00");
        for (String firstCityId : firstCityIdList) {
            Map resultMap = new HashMap();

            String cityName = CityDAO.getCityName(firstCityId);
            resultMap.put("cityName", cityName);
            resultMap.put("cityId", firstCityId);

            // 查询地市下的所有地市，包括自己
            List<String> allNextCityIds = CityDAO.getAllNextCityIdsByCityPid(firstCityId);

            // 重启数、成功数、失败数、改善数
            int deviceNum = 0;
            int successNum = 0;
            int failedNum = 0;
            int improveNum = 0;
            BigDecimal improveRate = BigDecimal.ZERO;
            BigDecimal successRate = BigDecimal.ZERO;

            for (Map restartDeviceIdMap : restartDeviceIdList) {
                String deviceCityId = StringUtil.getStringValue(restartDeviceIdMap, "city_id");

                // 重启状态（1:重启成功;-1:重启失败;0:未重启）
                String status = StringUtil.getStringValue(restartDeviceIdMap, "status");

                // 上网方式 1 桥接；2路由；3静态IP；4 DHCP
                // String wanType = restartDeviceIdMap.get("wan_type").toString();
                if (allNextCityIds.contains(deviceCityId)) {
                    deviceNum++;
                    if ("1".equals(status)) {
                        successNum++;
                    }
                    else if ("-1".equals(status)) {
                        failedNum++;
                    }

                    // 改善数  0:未改善 1：已改善  改善数：桥接的设备使用CPU占用率; 路由用页面加载时间
                    String isimprove = StringUtil.getStringValue(restartDeviceIdMap, "isimprove");

                    if ("1".equals(isimprove)) {
                        improveNum++;
                    }
                }
            }

            resultMap.put("deviceNum", deviceNum);
            resultMap.put("successNum", successNum);
            resultMap.put("failedNum", failedNum);
            resultMap.put("improveNum", improveNum);

            if (successNum > 0) {
                improveRate = new BigDecimal(improveNum * 100).divide(new BigDecimal(successNum), 2, BigDecimal.ROUND_HALF_UP);
            }
            if (deviceNum > 0) {
                successRate = new BigDecimal(successNum * 100).divide(new BigDecimal(deviceNum), 2, BigDecimal.ROUND_HALF_UP);
            }

            resultMap.put("improveRate", improveRate);
            resultMap.put("successRate", successRate);

            resultMapList.add(resultMap);
        }

        return resultMapList;
    }

    /**
     * * 光宽批量重启设备查询记录
     *
     * @param starttime1
     *            开始时间
     * @param endtime1
     *            结束时间
     * @author ou.yuan
     * @return List<Map>
     */
    public List<Map> getBatchRestartDeviceList(String starttime1, String endtime1) 
    {
        logger.debug("getBatchRestartDeviceList({},{},{},{},{},{},{},{},{},{},{})");
        List<Map> resultMapList = new ArrayList<Map>();

        StringBuffer qryRestartDeviceIdsSql = new StringBuffer();
      /**  if(DBUtil.GetDB()==Global.DB_MYSQL){
        	//TODO wait
        }else{
        	
        } */
        qryRestartDeviceIdsSql.append("select a.device_id,b.city_id,c.city_name,a.status,a.loadtime,");
        qryRestartDeviceIdsSql.append("a.loadtime_new,a.cpuusage,a.cpuusage_new,d.wan_type,a.isimprove ");
        qryRestartDeviceIdsSql.append("from guangkuan_reboot_info a,tab_gw_device b,tab_city c,tab_hgwcustomer d");

     // a.device_id 格式为 oui-设备序列号
        if(DBUtil.GetDB()==Global.DB_MYSQL){
            qryRestartDeviceIdsSql.append(" where SUBSTRING(a.device_id , 2, INSTR(a.device_id, '-')-1) = b.oui ");
            qryRestartDeviceIdsSql.append(" and SUBSTRING(a.device_id ,INSTR(a.device_id , '-')+2) = b.device_serialnumber ");
        }else{
            qryRestartDeviceIdsSql.append(" where SUBSTR(a.device_id , 1, INSTR(a.device_id, '-')-1) = b.oui ");
            qryRestartDeviceIdsSql.append(" and SUBSTR(a.device_id , INSTR(a.device_id , '-')+1) = b.device_serialnumber ");
        }

        qryRestartDeviceIdsSql.append(" and b.city_id = c.city_id");
        qryRestartDeviceIdsSql.append(" and b.device_id = d.device_id");
        qryRestartDeviceIdsSql.append(" and (a.status = '1' or a.status = '-1')");

        if (StringUtil.isNotEmpty(starttime1)) {
            qryRestartDeviceIdsSql.append(" and a.reboot_time>=").append(starttime1);
        }
        if (StringUtil.isNotEmpty(endtime1)) {
            qryRestartDeviceIdsSql.append(" and a.reboot_time<").append(endtime1);
        }

        PrepareSQL psqlTwo = new PrepareSQL(qryRestartDeviceIdsSql.toString());
        List<Map> restartDeviceIdList = jt.queryForList(psqlTwo.getSQL());


        if (restartDeviceIdList == null || restartDeviceIdList.isEmpty()) {
            logger.warn("QueryBatchRestartDeviceDAO.getBatchRestartDeviceList() restartDeviceIdList is empty!");
        }
        else {
            // 统计返回给页面的参数
            resultMapList = calculateDeviceNum(restartDeviceIdList);
        }

        return resultMapList;
    }

    /**
     * 批量重启设备详情数量
     * @param cityId
     * @param starttime1
     * @param endtime1
     * @param numType
     * @return
     */
    public int getBatchRestartDevDetailCount(String cityId, String starttime1, 
    		String endtime1, String numType) 
    {
        PrepareSQL sql = new PrepareSQL();

        //-- 城市id, 城市名字, 重启状态, 设备厂商, 设备型号, 软件版本, 设备重启原因, 设备序列号,
        //-- 重启前指标，重启后指标, IP, loid, wan_type
       /** if(DBUtil.GetDB()==Global.DB_MYSQL){
        	//TODO wait
        }else{
        	
        } */
        sql.setSQL("select count(*) ");
        sql.append(" from guangkuan_reboot_info a, tab_gw_device b, tab_city c, tab_vendor d, gw_device_model e, tab_devicetype_info f, tab_hgwcustomer g ");

     // a.device_id 格式为 oui-设备序列号
        if(DBUtil.GetDB()==Global.DB_MYSQL){
            sql.append(" where SUBSTRING(a.device_id , 2, INSTR(a.device_id, '-')-1) = b.oui ");
            sql.append(" and SUBSTRING(a.device_id , INSTR(a.device_id , '-')+2) = b.device_serialnumber ");
        }else{
            sql.append(" where SUBSTR(a.device_id , 1, INSTR(a.device_id, '-')-1) = b.oui ");
            sql.append(" and SUBSTR(a.device_id , INSTR(a.device_id , '-')+1) = b.device_serialnumber ");
        }

        sql.append(" and b.city_id = c.city_id");
        sql.append(" and b.vendor_id = d.vendor_id");
        sql.append(" and b.device_model_id = e.device_model_id");
        sql.append(" and b.devicetype_id = f.devicetype_id");
        sql.append(" and b.device_id = g.device_id");

        if (StringUtil.isNotEmpty(starttime1)) {
            sql.append(" and a.reboot_time>=" + starttime1);
        }
        if (StringUtil.isNotEmpty(endtime1)) {
            sql.append(" and a.reboot_time<" + endtime1);
        }

        if ("deviceNum".equals(numType)) {
            sql.append(" and (a.status = '1' or a.status = '-1')");
        }
        else if ("successNum".equals(numType)) {
            sql.append(" and a.status = '1'");
        }
        else if ("failedNum".equals(numType)) {
            sql.append(" and a.status = '-1'");
        }
        else if ("improveNum".equals(numType)) {
            sql.append(" and a.isimprove = '1'");
        }

        if (!"-1".equals(cityId)) {
            // 查询地市下的所有地市，包括自己
            List<String> allNextCityIds = CityDAO.getAllNextCityIdsByCityPid(cityId);
            sql.append(" and b.city_id in (" + StringUtils.weave(allNextCityIds) + ") ");
        }

        return jt.queryForInt(sql.getSQL());
    }

    /**
     * 批量重启设备详情列表  比导出Excel多了分页
     * @param cityId
     * @param curPage_splitPage
     * @param num_splitPage
     * @param starttime1
     * @param endtime1
     * @param numType
     * @return
     */
    public List<Map> queryBatchRestartDevDetailList(String cityId, int curPage_splitPage, 
    		int num_splitPage, String starttime1, String endtime1, String numType) 
    {
        PrepareSQL sql = new PrepareSQL();

        //-- 城市id, 城市名字, 重启状态, 设备厂商, 设备型号, 软件版本, 设备重启原因, 设备序列号,
        //-- 重启前指标，重启后指标, IP, loid, wan_type
      /**  if(DBUtil.GetDB()==Global.DB_MYSQL){
        	//TODO wait
        }else{
        	
        } */
        sql.setSQL("select b.city_id, c.city_name, a.status, d.vendor_add, e.device_model, f.softwareversion, a.reboot_reason, b.device_serialnumber, ");
        sql.append(" a.loadtime, a.loadtime_new, a.cpuusage, a.cpuusage_new, b.loopback_ip, g.username, g.wan_type");
        sql.append(" from guangkuan_reboot_info a, tab_gw_device b, tab_city c, tab_vendor d, gw_device_model e, tab_devicetype_info f, tab_hgwcustomer g ");

     // a.device_id 格式为 oui-设备序列号
        if(DBUtil.GetDB()==Global.DB_MYSQL){
            sql.append(" where SUBSTRING(a.device_id , 2, INSTR(a.device_id, '-')-1) = b.oui ");
            sql.append(" and SUBSTRING(a.device_id , INSTR(a.device_id , '-')+2) = b.device_serialnumber ");
        }else{
            sql.append(" where SUBSTR(a.device_id , 1, INSTR(a.device_id, '-')-1) = b.oui ");
            sql.append(" and SUBSTR(a.device_id , INSTR(a.device_id , '-')+1) = b.device_serialnumber ");
        }

        sql.append(" and b.city_id = c.city_id");
        sql.append(" and b.vendor_id = d.vendor_id");
        sql.append(" and b.device_model_id = e.device_model_id");
        sql.append(" and b.devicetype_id = f.devicetype_id");
        sql.append(" and b.device_id = g.device_id");

        if (StringUtil.isNotEmpty(starttime1)) {
            sql.append(" and a.reboot_time>=" + starttime1);
        }
        if (StringUtil.isNotEmpty(endtime1)) {
            sql.append(" and a.reboot_time<" + endtime1);
        }

        if ("deviceNum".equals(numType)) {
            sql.append(" and (a.status = '1' or a.status = '-1')");
        }
        else if ("successNum".equals(numType)) {
            sql.append(" and a.status = '1'");
        }
        else if ("failedNum".equals(numType)) {
            sql.append(" and a.status = '-1'");
        }
        else if ("improveNum".equals(numType)) {
            sql.append(" and a.isimprove = '1'");
        }

        if (!"-1".equals(cityId)) {
            // 查询地市下的所有地市，包括自己
            List<String> allNextCityIds = CityDAO.getAllNextCityIdsByCityPid(cityId);
            sql.append(" and b.city_id in (" + StringUtils.weave(allNextCityIds) + ") ");
        }

        sql.append(" order by a.reboot_time desc");

        List<Map> list = querySP(sql.getSQL(), (curPage_splitPage - 1) * num_splitPage
                + 1, num_splitPage, new RowMapper()
        {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException
            {
                Map<String, String> map = new HashMap<String, String>();

                String city_id = rs.getString("city_id");
                map.put("city_id", city_id);

                // 设备厂商
                map.put("vendorName", rs.getString("vendor_add"));
                // 设备型号
                map.put("deviceModelName", rs.getString("device_model"));
                // 软件版本
                map.put("softwareVersion", rs.getString("softwareversion"));
                // 属地
                map.put("cityName", rs.getString("city_name"));
                // 设备重启原因
                map.put("rebootReason", rs.getString("reboot_reason"));
                // 设备序列号
                map.put("deviceSerialnumber", rs.getString("device_serialnumber"));

                // 上网方式 1 桥接；2路由； 桥接的设备使用CPU占用率; 路由用页面加载时间z
                // String wanType = rs.getString("wan_type");

                String beforeReStartNum = "";
                String afterReStartNum = "";

                // 优先用加载时间，如果为空则用CPU指标
                beforeReStartNum = StringUtil.getStringValue(rs.getString("loadtime"));
                afterReStartNum = StringUtil.getStringValue(rs.getString("loadtime_new"));

                if (StringUtil.IsEmpty(beforeReStartNum) && StringUtil.IsEmpty(afterReStartNum)) {
                    beforeReStartNum = StringUtil.getStringValue(rs.getString("cpuusage"));
                    afterReStartNum = StringUtil.getStringValue(rs.getString("cpuusage_new"));
                }

                // 重启前指标
                map.put("beforeReStartNum", beforeReStartNum);
                // 重启后指标
                map.put("afterReStartNum", afterReStartNum);

                /*if ("1".equals(wanType)) {
                    // 重启前指标
                    map.put("beforeReStartNum", rs.getString("cpuusage"));
                    // 重启后指标
                    map.put("afterReStartNum", rs.getString("cpuusage_new"));
                }
                else if ("2".equals(wanType)) {
                    // 重启前指标
                    map.put("beforeReStartNum", rs.getString("loadtime"));
                    // 重启后指标
                    map.put("afterReStartNum", rs.getString("loadtime_new"));
                }*/

                // 设备IP
                map.put("loopbackIp", rs.getString("loopback_ip"));
                // loid
                map.put("loid", rs.getString("username"));

                return map;
            }
        });

        return list;
    }

    /**
     * 批量重启设备详情到处到Excel
     * @param cityId
     * @param numType
     * @param starttime1
     * @param endtime1
     * @return
     */
    public List<Map> getBatchRestartDevDetailExcel(String cityId, String numType, 
    		String starttime1, String endtime1) 
    {
        PrepareSQL sql = new PrepareSQL();

        //-- 城市id, 城市名字, 重启状态, 设备厂商, 设备型号, 软件版本, 设备重启原因, 设备序列号,
        //-- 重启前指标，重启后指标, IP, loid, wan_type
    /**    if(DBUtil.GetDB()==Global.DB_MYSQL){
        	//TODO wait
        }else{
        	
        } */
        sql.setSQL("select b.city_id, c.city_name, a.status, d.vendor_add, e.device_model, f.softwareversion, a.reboot_reason, b.device_serialnumber, ");
        sql.append(" a.loadtime, a.loadtime_new, a.cpuusage, a.cpuusage_new, b.loopback_ip, g.username, g.wan_type");
        sql.append(" from guangkuan_reboot_info a, tab_gw_device b, tab_city c, tab_vendor d, gw_device_model e, tab_devicetype_info f, tab_hgwcustomer g ");

     // a.device_id 格式为 oui-设备序列号
        if(DBUtil.GetDB()==Global.DB_MYSQL){
            sql.append(" where SUBSTRING(a.device_id , 2, INSTR(a.device_id, '-')-1) = b.oui ");
            sql.append(" and SUBSTRING(a.device_id , INSTR(a.device_id , '-')+2) = b.device_serialnumber ");
        }else{
            sql.append(" where SUBSTR(a.device_id , 1, INSTR(a.device_id, '-')-1) = b.oui ");
            sql.append(" and SUBSTR(a.device_id , INSTR(a.device_id , '-')+1) = b.device_serialnumber ");
        }

        sql.append(" and b.city_id = c.city_id");
        sql.append(" and b.vendor_id = d.vendor_id");
        sql.append(" and b.device_model_id = e.device_model_id");
        sql.append(" and b.devicetype_id = f.devicetype_id");
        sql.append(" and b.device_id = g.device_id");

        if (StringUtil.isNotEmpty(starttime1)) {
            sql.append(" and a.reboot_time>=" + starttime1);
        }
        if (StringUtil.isNotEmpty(endtime1)) {
            sql.append(" and a.reboot_time<" + endtime1);
        }

        if ("deviceNum".equals(numType)) {
            sql.append(" and (a.status = '1' or a.status = '-1')");
        }
        else if ("successNum".equals(numType)) {
            sql.append(" and a.status = '1'");
        }
        else if ("failedNum".equals(numType)) {
            sql.append(" and a.status = '-1'");
        }
        else if ("improveNum".equals(numType)) {
            sql.append(" and a.isimprove = '1'");
        }

        if (!"-1".equals(cityId)) {
            // 查询地市下的所有地市，包括自己
            List<String> allNextCityIds = CityDAO.getAllNextCityIdsByCityPid(cityId);
            sql.append(" and b.city_id in (" + StringUtils.weave(allNextCityIds) + ") ");
        }
        sql.append(" order by a.reboot_time desc");

		List<Map> list = jt.query(sql.getSQL(), new RowMapper()
        {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException
            {
                Map<String, String> map = new HashMap<String, String>();
                String city_id = rs.getString("city_id");
                map.put("city_id", city_id);

                // 设备厂商
                map.put("vendorName", rs.getString("vendor_add"));
                // 设备型号
                map.put("deviceModelName", rs.getString("device_model"));
                // 软件版本
                map.put("softwareVersion", rs.getString("softwareversion"));
                // 属地
                map.put("cityName", rs.getString("city_name"));
                // 设备重启原因
                map.put("rebootReason", rs.getString("reboot_reason"));
                // 设备序列号
                map.put("deviceSerialnumber", rs.getString("device_serialnumber"));

                // 上网方式 1 桥接；2路由； 桥接的设备使用CPU占用率; 路由用页面加载时间z
                // String wanType = rs.getString("wan_type");

                String beforeReStartNum = "";
                String afterReStartNum = "";

                // 优先用加载时间，如果为空则用CPU指标
                beforeReStartNum = StringUtil.getStringValue(rs.getString("loadtime"));
                afterReStartNum = StringUtil.getStringValue(rs.getString("loadtime_new"));

                if (StringUtil.IsEmpty(beforeReStartNum) && StringUtil.IsEmpty(afterReStartNum)) {
                    beforeReStartNum = StringUtil.getStringValue(rs.getString("cpuusage"));
                    afterReStartNum = StringUtil.getStringValue(rs.getString("cpuusage_new"));
                }

                // 重启前指标
                map.put("beforeReStartNum", beforeReStartNum);
                // 重启后指标
                map.put("afterReStartNum", afterReStartNum);

                /*if ("1".equals(wanType)) {
                    // 重启前指标
                    map.put("beforeReStartNum", rs.getString("cpuusage"));
                    // 重启后指标
                    map.put("afterReStartNum", rs.getString("cpuusage_new"));
                }
                else if ("2".equals(wanType)) {
                    // 重启前指标
                    map.put("beforeReStartNum", rs.getString("loadtime"));
                    // 重启后指标
                    map.put("afterReStartNum", rs.getString("loadtime_new"));
                }*/

                // 设备IP
                map.put("loopbackIp", rs.getString("loopback_ip"));
                // loid
                map.put("loid", rs.getString("username"));

                return map;
            }
        });
        return list;
    }

}
