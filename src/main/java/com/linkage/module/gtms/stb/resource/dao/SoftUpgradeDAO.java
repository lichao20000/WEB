package com.linkage.module.gtms.stb.resource.dao;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.stb.dao.GwStbVendorModelVersionDAO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.litms.common.util.JdbcTemplateExtend;
import com.linkage.system.utils.DateTimeUtil;
import com.linkage.system.utils.StringUtils;
import com.linkage.system.utils.database.DBUtil;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked", "deprecation"})
public class SoftUpgradeDAO {
    private static Logger logger = LoggerFactory.getLogger(SoftUpgradeDAO.class);
    private Map<String, String> status_map = new HashMap<String, String>();
    private Map<String, String> result_map = new HashMap<String, String>();
    private HashMap<String, String> vendorMap = new HashMap<String, String>();
    private Map<String, String> deviceTypeMap = new HashMap<String, String>();
    private Map<String, String> deviceModelMap = new HashMap<String, String>();
    private Map<String, String> userTypeMap = new HashMap<String, String>();
    private Map<String, String> cityMap = null;
    private String taskId = "";
    // 湖南联通记录软件升级管理操作日志
    private static String writeLogSql = "insert into tab_oper_log("
    			+ "acc_oid,acc_login_ip,operationlog_type,operation_time,operation_name"
    			+ ",operation_object,operation_content,operation_device"
    			+ ",operation_result,result_id) values(?,?,?,?,?,?,?,?,?,?)";
    private GwStbVendorModelVersionDAO vmvDaoStb;

    public GwStbVendorModelVersionDAO getVmvDaoStb() {
        return vmvDaoStb;
    }

    public void setVmvDaoStb(GwStbVendorModelVersionDAO vmvDaoStb) {
        this.vmvDaoStb = vmvDaoStb;
    }

    private JdbcTemplateExtend jt;

    public void setDao(DataSource dao) {
        jt = new JdbcTemplateExtend(dao);
    }

    public SoftUpgradeDAO() {
        status_map.put("0", "等待执行");
        status_map.put("1", "预读PVC");
        status_map.put("2", "预读绑定端口");
        status_map.put("3", "预读无线");
        status_map.put("4", "业务下发");
        status_map.put("100", "执行完成");
        result_map.put("-10", "策略执行过程中程序异常");
        result_map.put("-9", "系统内部错误");
        result_map.put("-8", "任务中前一策略失败导致");
        result_map.put("-7", "系统参数错误");
        result_map.put("-6", "设备正被操作");
        result_map.put("-5", "系统没有模板");
        result_map.put("-4", "系统没有设备");
        result_map.put("-3", "系统没有工单");
        result_map.put("-2", "设备没有响应");
        result_map.put("-1", "设备连接失败");
        result_map.put("0", "等待执行");
        result_map.put("1", "成功");
        result_map.put("2", "正在执行");
        result_map.put("3", "设备无法连接");
        result_map.put("9001", "请求拒绝");
        result_map.put("9002", "请求拒绝");
        result_map.put("9003", "参数不对");
        result_map.put("9004", "资源超支");
        result_map.put("9005", "节点不对");
        result_map.put("9006", "节点类型不对");
        result_map.put("9007", "节点值不对");
        result_map.put("9008", "节点不可更改");
        result_map.put("9009", "通知失败");
        result_map.put("9010", "下载失败");
    }

    public Boolean addStrategy(StrategyOBJ obj, String devicetypeId, String pathId) {
        logger.debug("addStrategy({})", obj);
        if (obj == null) {
            logger.debug("obj == null");
            return false;
        }
        List<String> sqlList = strategySQL(obj, devicetypeId);
        PrepareSQL sql = new PrepareSQL(
                "insert into stb_gw_softup_record(id,device_id,result,dev_type_id_old,start_time,path_id) values(?,?,?,?,?,?)");
        long nowtime = new DateTimeUtil().getLongTime();
        sql.setLong(1, obj.getId());
        sql.setString(2, obj.getDeviceId());
        sql.setLong(3, 0);
        sql.setLong(4, StringUtil.getLongValue(devicetypeId));
        sql.setLong(5, nowtime);
        sql.setLong(6, StringUtil.getLongValue(pathId));
        sqlList.add(sql.getSQL());
        int[] result = doBatch(sqlList);
        if (result != null && result.length > 0) {
            logger.debug("策略入库：  成功");
            return true;
        } else {
            logger.debug("策略入库：  失败");
            return false;
        }
    }

    public List showSoftwareversionByDeviceId(String deviceId) {
        PrepareSQL sql = new PrepareSQL();// TODO wait (more table related)
        sql.append("select b.id,b.softwareversion,b.version_path ");
        sql.append("from stb_tab_gw_device a,stb_gw_version_file_path b,stb_version_file_path_model c ");
        sql.append("where a.device_model_id=c.device_model_id ");
        sql.append("and c.path_id=b.id and b.valid=1 and b.version_type=0 and a.device_id=? ");
        sql.setString(1, deviceId);
        return jt.queryForList(sql.getSQL());
    }

    /**
     * 获取设备可升级版本
     */
    public List showSoftwareversionByDeviceId_hnlt(String deviceId) {
        PrepareSQL sql = new PrepareSQL();// TODO wait (more table related)
        sql.append("select b.id,b.version_name as softwareversion,b.version_path,b.net_type ");
        sql.append("from stb_tab_gw_device a,stb_soft_version_path b,stb_soft_version_path_model c ");
        sql.append("where a.device_model_id=c.device_model_id and c.id=b.id ");
        sql.append("and a.device_id=? ");
        sql.setString(1, deviceId);
        return jt.queryForList(sql.getSQL());
    }

    /**
     * 获取版本详细
     */
    public Map querySoftVersionDetail(String id) {
        PrepareSQL sql = new PrepareSQL();
        sql.append("select version_desc,version_path,file_size,md5,net_type,epg_version ");
        sql.append("from stb_soft_version_path ");
        sql.append("where id=" + id);
        return jt.queryForMap(sql.getSQL());
    }

    public Map getDeviceInfo(String deviceId) {
        PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
        psql.append("select a.device_serialnumber,a.city_id,a.cpe_mac,a.device_model_id,");
        psql.append("a.complete_time,a.cpe_currentupdatetime,a.loopback_ip,");
        psql.append("b.vendor_add,c.device_model,d.serv_account,");
        psql.append("e.hardwareversion,e.softwareversion,e.epg_version,");
        psql.append("e.net_type,f.category,f.ip_type,");
        psql.append("f.apk_version_name,f.network_type,f.addressing_type,f.public_ip ");
        psql.append("from stb_tab_gw_device a ");
        psql.append("left join stb_tab_vendor b on a.vendor_id=b.vendor_id ");
        psql.append("left join stb_gw_device_model c on a.device_model_id=c.device_model_id ");
        psql.append("left join stb_tab_customer d on a.customer_id=d.customer_id ");
        psql.append("left join stb_tab_devicetype_info e on a.devicetype_id=e.devicetype_id ");
        psql.append("left join stb_dev_supplement f on a.device_id=f.device_id ");
        psql.append("where a.device_status=1 and a.device_id=? ");
        psql.setString(1, deviceId);

        return jt.queryForMap(psql.getSQL());
    }

    public List showAllSoftwareversionByDeviceId() {
        PrepareSQL sql = new PrepareSQL();
        sql.append("select id,softwareversion,version_path ");
        sql.append("from stb_gw_version_file_path where valid=1 and version_type=0 ");
        return jt.queryForList(sql.getSQL());
    }

    /**
     * 设备升级结果查询
     */
    public List getUpgradeResult(String deviceId) {
        logger.debug("getUpgradeResult({})", deviceId);
        List reList = new ArrayList();
        List tmpList = null;
        Map resMap = null;
        Map tmpMap = null;
        if (!StringUtil.IsEmpty(deviceId)) {// TODO wait (more table related)
            String sql = "select t1.*,t2.softwareversion softwareversion_old,t3.softwareversion softwareversion_new "
                    + "from (select distinct a.device_id,a.device_serialnumber,a.city_id,b.dev_type_id_old,"
                    + "b.dev_type_id_new,b.result,d.device_model,e.vendor_name,f.version_path,"
                    + "f.softwareversion version_name,b.result,b.start_time,b.end_time "
                    + "from stb_tab_gw_device a,stb_gw_softup_record b,stb_gw_device_model d,stb_tab_vendor e,stb_gw_version_file_path f "
                    + "where a.device_id=b.device_id and a.device_model_id=d.device_model_id and a.vendor_id=e.vendor_id and f.id=b.path_id and a.device_id=? ) t1"
                    + " left join stb_tab_devicetype_info t2 on t1.dev_type_id_old=t2.devicetype_id "
                    + "left join stb_tab_devicetype_info t3 on t1.dev_type_id_new=t3.devicetype_id  "
                    + "order by t1.start_time desc";
            PrepareSQL psql = new PrepareSQL(sql);
            psql.setString(1, deviceId);
            tmpList = jt.queryForList(psql.getSQL());
            cityMap = CityDAO.getCityIdCityNameMap();
            for (Object obj : tmpList) {
                resMap = new HashMap<String, String>();
                tmpMap = new HashMap<String, String>();
                if (null != obj) {
                    tmpMap = (Map) obj;
                    resMap.put("softwareversion_old", tmpMap.get("softwareversion_old"));
                    resMap.put("softwareversion_new", tmpMap.get("softwareversion_new"));
                    resMap.put("device_serialnumber", tmpMap.get("device_serialnumber"));
                    resMap.put("version_path", tmpMap.get("version_path"));
                    resMap.put("version_name", tmpMap.get("version_name"));
                    String city_id = StringUtil.getStringValue(tmpMap.get("city_id"));
                    resMap.put("city_id", city_id);
                    if ("-1".equals(city_id) || city_id == null) {
                        resMap.put("city_name", "-");
                    } else {
                        resMap.put("city_name", cityMap.get(city_id));
                    }
                    Integer result = StringUtil.getIntegerValue(tmpMap.get("result"));
                    if (0 == result) {
                        resMap.put("result", "未操作");
                    } else if (1 == result) {
                        resMap.put("result", "更新服务器地址修改成功");
                    } else if (2 == result) {
                        resMap.put("result", "软件升级成功");
                    } else {
                        resMap.put("result", "更新服务器地址修改失败");
                    }

                    resMap.put("device_model", tmpMap.get("device_model"));
                    resMap.put("vendor_name", tmpMap.get("vendor_name"));
                    // 将startTime转换成时间
                    String startTime = StringUtil.getStringValue(tmpMap.get("start_time"));
                    if (false == StringUtil.IsEmpty(startTime)) {
                        DateTimeUtil dateTimeUtil = new DateTimeUtil(Long.parseLong(startTime) * 1000);
                        startTime = dateTimeUtil.getLongDate();
                        dateTimeUtil = null;
                    }
                    resMap.put("start_time", startTime);
                    // 将endTime转换成时间
                    String endTime = StringUtil.getStringValue(tmpMap.get("end_time"));
                    if (false == StringUtil.IsEmpty(endTime)) {
                        DateTimeUtil dateTimeUtil = new DateTimeUtil(Long.parseLong(endTime) * 1000);
                        endTime = dateTimeUtil.getLongDate();
                        dateTimeUtil = null;
                    }
                    resMap.put("end_time", endTime);
                    reList.add(resMap);
                }
            }
            return reList;
        }
        return null;
    }

    /**
     * 设备升级结果查询
     */
    public List getUpgradeResult_hnlt(String deviceId) {
        List reList = new ArrayList();
        List tmpList = null;
        HashMap<String, String> resMap = null;
        HashMap<String, String> tmpMap = null;
        if (!StringUtil.IsEmpty(deviceId)) {
            PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
            psql.append("select a.device_serialnumber,a.city_id,a.cpe_mac,a.device_model_id,b.vendor_add,c.device_model,");
            psql.append("d.serv_account,e.hardwareversion,e.softwareversion ");
            psql.append("from stb_tab_gw_device a ");
            psql.append("left join stb_tab_vendor b on a.vendor_id=b.vendor_id ");
            psql.append("left join stb_gw_device_model c on a.device_model_id=c.device_model_id ");
            psql.append("left join stb_tab_customer d on a.customer_id=d.customer_id ");
            psql.append("left join stb_tab_devicetype_info e on a.devicetype_id=e.devicetype_id ");
            psql.append("where a.device_status=1 and a.device_id=? ");
            psql.setString(1, deviceId);

            Map devMap = jt.queryForMap(psql.getSQL());

            psql = new PrepareSQL();
            psql.append("select sheet_para,time,start_time,end_time,result_id ");
            psql.append("from stb_gw_strategy_soft_log ");
            psql.append("where service_id=5 and device_id=? ");
            psql.append("order by start_time desc ");

            psql.setString(1, deviceId);
            tmpList = jt.queryForList(psql.getSQL());

            cityMap = CityDAO.getCityIdCityNameMap();
            if (tmpList != null && !tmpList.isEmpty()) {
                for (int i = 0; i < tmpList.size(); i++) {
                    tmpMap = (HashMap<String, String>) tmpList.get(i);
                    tmpMap = toMap(tmpMap);

                    resMap = new HashMap<String, String>();

                    resMap.put("device_serialnumber", StringUtil.getStringValue(devMap, "device_serialnumber"));
                    resMap.put("device_model", StringUtil.getStringValue(devMap, "device_model"));
                    resMap.put("vendor_name", StringUtil.getStringValue(devMap, "vendor_add"));
                    resMap.put("city_name", cityMap.get(StringUtil.getStringValue(devMap, "city_id")));
                    resMap.put("cpe_mac", StringUtil.getStringValue(devMap, "cpe_mac"));
                    resMap.put("serv_account", StringUtil.getStringValue(devMap, "serv_account"));
                    resMap.put("hardwareversion", StringUtil.getStringValue(devMap, "hardwareversion"));
                    resMap.put("softwareversion", StringUtil.getStringValue(devMap, "softwareversion"));

                    resMap.put("version_path", StringUtil.getStringValue(tmpMap, "version_path"));
                    resMap.put("version_name", StringUtil.getStringValue(tmpMap, "version_name"));

                    Integer result = StringUtil.getIntegerValue(tmpMap.get("result_id"));
                    if (0 == result) {
                        resMap.put("result", "未操作");
                    } else if (1 == result) {
                        resMap.put("result", "已下发升级路径");
                    } else if (2 == result) {
                        resMap.put("result", "软件升级成功");
                    }

                    String time = StringUtil.getStringValue(tmpMap, "time");
                    if (!StringUtil.IsEmpty(time)) {
                        DateTimeUtil dateTimeUtil = new DateTimeUtil(Long.parseLong(time) * 1000);
                        time = dateTimeUtil.getLongDate();
                        dateTimeUtil = null;
                    }
                    resMap.put("time", time);

                    String startTime = StringUtil.getStringValue(tmpMap, "start_time");
                    if (!StringUtil.IsEmpty(startTime)) {
                        DateTimeUtil dateTimeUtil = new DateTimeUtil(Long.parseLong(startTime) * 1000);
                        startTime = dateTimeUtil.getLongDate();
                        dateTimeUtil = null;
                    }
                    resMap.put("start_time", startTime);

                    String endTime = StringUtil.getStringValue(tmpMap, "end_time");
                    if (!StringUtil.IsEmpty(endTime)) {
                        DateTimeUtil dateTimeUtil = new DateTimeUtil(Long.parseLong(endTime) * 1000);
                        endTime = dateTimeUtil.getLongDate();
                        dateTimeUtil = null;
                    }
                    resMap.put("end_time", endTime);
                    reList.add(resMap);
                }
                return reList;
            }
        }

        return null;
    }

    private HashMap<String, String> toMap(HashMap<String, String> tmpMap) {
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new StringReader(StringUtil.getStringValue(tmpMap, "sheet_para")));
            Element root = document.getRootElement();
            tmpMap.put("version_path", root.elementText("version_path"));
            tmpMap.put("version_name", root.elementText("version_name"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return tmpMap;
    }

    public Map getStrategyById(String strategyId) {
        logger.debug("getStrategyById({})", strategyId);
        Map rmap = null;
        if (!StringUtil.IsEmpty(strategyId)) {
            PrepareSQL psql = new PrepareSQL("select id, status, result_id, start_time, end_time from stb_gw_serv_strategy where id=? ");
            psql.setLong(1, StringUtil.getLongValue(strategyId));
            Map tmap = jt.queryForMap(psql.getSQL());
            if (tmap != null) {
                rmap = new HashMap<String, String>();
                rmap.put("strategyId", StringUtil.getStringValue(tmap.get("id")));
                rmap.put("status", status_map.get(StringUtil.getStringValue(tmap.get("status"))));
                rmap.put("result", result_map.get(StringUtil.getStringValue(tmap.get("result_id"))));
                String startTime = StringUtil.getStringValue(tmap.get("start_time"));
                if (!StringUtil.IsEmpty(startTime)) {
                    DateTimeUtil dateTimeUtil = new DateTimeUtil(Long.parseLong(startTime) * 1000);
                    startTime = dateTimeUtil.getLongDate();
                    dateTimeUtil = null;
                }
                rmap.put("startTime", startTime);
                String endTime = StringUtil.getStringValue(tmap.get("end_time"));
                if (!StringUtil.IsEmpty(endTime)) {
                    DateTimeUtil dateTimeUtil = new DateTimeUtil(Long.parseLong(endTime) * 1000);
                    endTime = dateTimeUtil.getLongDate();
                    dateTimeUtil = null;
                }
                rmap.put("endTime", endTime);
            }
        }
        return rmap;
    }

    /**
     * 根据厂商获取目标版本
     */
    public List getTargetVersion(String vendorId) {
        PrepareSQL sql = new PrepareSQL();
        if (Global.HNLT.equals(Global.instAreaShortName)) {
            sql.append("select id,version_name as softwareversion,version_path ");
            sql.append("from stb_soft_version_path where vendor_id=? ");
        } else {
            sql.append("select id,softwareversion,version_path ");
            sql.append("from stb_gw_version_file_path where valid=1 and version_type=0 and vendor_id=? ");
        }

        sql.setString(1, vendorId);
        return jt.queryForList(sql.getSQL());
    }

    public int batchSoftUpgrade(String vendorId, String cityId, String pathId,
                                String strategyType, long accoid, String devicetypeId,
                                String ipCheck, String ipSG) {
        List<String> sqllist = new ArrayList<String>();
        PrepareSQL sql1 = new PrepareSQL();
        sql1.append("insert into stb_gw_softup_task(task_id,vendor_id,city_id,version_path_id,");
        sql1.append("record_time,update_time,acc_oid,valid,strategy_type,check_ip) ");
        sql1.append("values(?,?,?,?,?,?,?,?,?,?) ");

        long id = Math.round(Math.random() * 100000L);
        long nowtime = new DateTimeUtil().getLongTime();
        sql1.setLong(1, id);
        sql1.setString(2, vendorId);
        sql1.setString(3, cityId);
        sql1.setLong(4, StringUtil.getLongValue(pathId));
        sql1.setLong(5, nowtime);
        sql1.setLong(6, nowtime);
        sql1.setLong(7, accoid);
        sql1.setLong(8, 1);
        sql1.setLong(9, StringUtil.getLongValue(strategyType));
        sql1.setInt(10, Integer.parseInt(ipCheck));
        sqllist.add(sql1.getSQL());
        String[] devicetypeIds = devicetypeId.split(",");
        for (String string : devicetypeIds) {
            PrepareSQL sql2 = new PrepareSQL("insert into stb_gw_softup_task_devtype values(?,?) ");
            sql2.setLong(1, id);
            sql2.setLong(2, StringUtil.getLongValue(string));
            sqllist.add(sql2.getSQL());
        }
        // 如果启动校验IP地址段,入库
        if ("1".equals(ipCheck.trim())) {
            String[] ipDuan = ipSG.trim().split("\2");
            String[] ipStr;
            for (String s : ipDuan) {
                PrepareSQL sql3 = new PrepareSQL("insert into stb_gw_softup_task_ipcheck values(?,?,?) ");
                sql3.setLong(1, id);
                ipStr = s.split("\1");
                sql3.setString(2, getFillIP(ipStr[0]));
                sql3.setString(3, getFillIP(ipStr[1]));
                sqllist.add(sql3.getSQL());
            }
        }

        int ier = DBOperation.executeUpdate(sqllist.toArray(new String[sqllist.size()]));
        if (ier > 0) {
            return 1;
        }
        return 0;
    }

    public int xjbatchSoftUpgrade(long taskId, String vendorId, String cityId, String pathId,
                                  String strategyType, long accoid, String devicetypeId) {
        if (StringUtil.IsEmpty(cityId) || "-1".equals(cityId)) {
            cityId = "00";
        }
        List<String> sqllist = new ArrayList<String>();
        PrepareSQL sql1 = new PrepareSQL();
        sql1.append("insert into stb_gw_softup_task(task_id,vendor_id,city_id,version_path_id,");
        sql1.append("record_time,update_time,acc_oid,valid,strategy_type,check_ip) ");
        sql1.append("values(?,?,?,?,?,?,?,?,?,?) ");

        long nowtime = new DateTimeUtil().getLongTime();
        sql1.setLong(1, taskId);
        sql1.setString(2, vendorId);
        sql1.setString(3, cityId);
        sql1.setLong(4, StringUtil.getLongValue(pathId));
        sql1.setLong(5, nowtime);
        sql1.setLong(6, nowtime);
        sql1.setLong(7, accoid);
        sql1.setLong(8, 1);
        sql1.setLong(9, StringUtil.getLongValue(strategyType));
        sql1.setInt(10, 0);
        sqllist.add(sql1.getSQL());
        String[] devicetypeIds = devicetypeId.split(",");
        for (String string : devicetypeIds) {
            PrepareSQL sql2 = new PrepareSQL("insert into stb_gw_softup_task_devtype values(?,?) ");
            sql2.setLong(1, taskId);
            sql2.setLong(2, StringUtil.getLongValue(string));
            sqllist.add(sql2.getSQL());
        }

        int ier = DBOperation.executeUpdate(sqllist.toArray(new String[sqllist.size()]));
        if (ier > 0) {
            return 1;
        }
        return 0;
    }

    /**
     * 多了一个任务描述
     * 河北专用(江西itv也用)
     */
    public int batchSoftUpgrade2(String vendorId, String cityId, String pathId,
                                 String strategyType, long accoid, String devicetypeId,
                                 String ipCheck, String ipSG, String taskDesc) {
        List<String> sqllist = new ArrayList<String>();
        PrepareSQL sql1 = new PrepareSQL();
        //JXDX-ITV-REQ-20180918-WUWF-001(ITV终端管理平台对外接口-机顶盒信息),新增is_import_task字段
        if (Global.JXDX.equals(Global.instAreaShortName)) {
            sql1.setSQL("insert into stb_gw_softup_task(task_id,vendor_id,city_id,version_path_id,record_time,update_time,acc_oid,valid,strategy_type,check_ip,task_desc,is_import_task) values(?,?,?,?,?,?,?,?,?,?,?,?) ");
        } else {
            sql1.setSQL("insert into stb_gw_softup_task(task_id,vendor_id,city_id,version_path_id,record_time,update_time,acc_oid,valid,strategy_type,check_ip,task_desc) values(?,?,?,?,?,?,?,?,?,?,?) ");
        }
        long id = Math.round(Math.random() * 100000L);
        taskId = StringUtil.getStringValue(id);
        long nowtime = new DateTimeUtil().getLongTime();
        sql1.setLong(1, id);
        sql1.setString(2, vendorId);
        sql1.setString(3, cityId);
        sql1.setLong(4, StringUtil.getLongValue(pathId));
        sql1.setLong(5, nowtime);
        sql1.setLong(6, nowtime);
        sql1.setLong(7, accoid);
        sql1.setLong(8, 1);
        sql1.setLong(9, StringUtil.getLongValue(strategyType));
        sql1.setInt(10, Integer.parseInt(ipCheck));
        sql1.setString(11, taskDesc);
        if (LipossGlobals.inArea(Global.JXDX)) {
            sql1.setLong(12, 1);
        }
        sqllist.add(sql1.getSQL());
        String[] devicetypeIds = devicetypeId.split(",");
        for (String string : devicetypeIds) {
            PrepareSQL sql2 = new PrepareSQL("insert into stb_gw_softup_task_devtype values(?,?) ");
            sql2.setLong(1, id);
            sql2.setLong(2, StringUtil.getLongValue(string));
            sqllist.add(sql2.getSQL());
        }
        // 如果启动校验IP地址段,入库
        if ("1".equals(ipCheck.trim())) {
            String[] ipDuan = ipSG.trim().split("\2");
            String[] ipStr;
            for (String s : ipDuan) {
                PrepareSQL sql3 = new PrepareSQL("insert into stb_gw_softup_task_ipcheck values(?,?,?) ");
                sql3.setLong(1, id);
                ipStr = s.split("\1");
                sql3.setString(2, getFillIP(ipStr[0]));
                sql3.setString(3, getFillIP(ipStr[1]));
                sqllist.add(sql3.getSQL());
            }
        }
        int ier = DBOperation.executeUpdate(sqllist.toArray(new String[sqllist.size()]));
        if (ier > 0) {
            return 1;
        }
        return 0;
    }

    public int batchSoftUpgradeBak(long id, String vendorId, String cityId,
                                   String pathId, String strategyType, long accoid,
                                   String devicetypeId, String ipCheck, String ipSG, String macCheck,
                                   String macSG, String custCheck, String custSG, String filePath, String ftpEnable,
                                   String ftpIp, String ftpName, String ftpPasswd, String ftpPath, String check_net,
                                   String taskType,String devsnCheck) {
        List<String> sqllist = new ArrayList<String>();
        long nowtime = new DateTimeUtil().getLongTime();

        PrepareSQL sql1 = new PrepareSQL();
        if (LipossGlobals.inArea(Global.HNLT)) {
            sql1.append("insert into stb_gw_softup_task(task_id,vendor_id,city_id,");
            sql1.append("version_path_id,record_time,update_time,acc_oid,valid,check_net) ");
            sql1.append("values(?,?,?,?,?,?,?,?,?) ");
            sql1.setLong(1, id);
            sql1.setString(2, vendorId);
            sql1.setString(3, cityId);
            sql1.setLong(4, StringUtil.getLongValue(pathId));
            sql1.setLong(5, nowtime);
            sql1.setLong(6, nowtime);
            sql1.setLong(7, accoid);
            sql1.setLong(8, 0);
            sql1.setInt(9, StringUtil.getIntegerValue(check_net));
            sqllist.add(sql1.getSQL());
        } else if (LipossGlobals.inArea("sd_lt") || LipossGlobals.inArea("gs_dx")) {
            String[] cids = cityId.split(",");
            logger.warn("city_id:{}", new Object[]{cids});
            PrepareSQL sql2 = null;
            for (String cid : cids) {
                sql2 = new PrepareSQL();
                long taskId = Math.round(Math.random() * 100000L);
                sql2.append("insert into stb_gw_softup_task(task_id,vendor_id,city_id,version_path_id,");
                sql2.append("record_time,update_time,acc_oid,valid,strategy_type,check_ip,check_mac) ");
                sql2.append("values(?,?,?,?,?,?,?,?,?,?,?) ");

                sql2.setLong(1, taskId);
                sql2.setString(2, vendorId);
                sql2.setString(3, cid);
                sql2.setLong(4, StringUtil.getLongValue(pathId));
                sql2.setLong(5, nowtime);
                sql2.setLong(6, nowtime);
                sql2.setLong(7, accoid);
                sql2.setLong(8, 0);
                sql2.setLong(9, StringUtil.getLongValue(strategyType));
                sql2.setInt(10, StringUtil.getIntegerValue(ipCheck));
                sql2.setInt(11, StringUtil.getIntegerValue(macCheck));
                sqllist.add(sql2.getSQL());

                String[] devicetypeIds = devicetypeId.split(",");
                for (String devTypeId : devicetypeIds) {
                    PrepareSQL sql3 = new PrepareSQL("insert into stb_gw_softup_task_devtype values(?,?) ");
                    sql3.setLong(1, taskId);
                    sql3.setLong(2, StringUtil.getLongValue(devTypeId));
                    sqllist.add(sql3.getSQL());
                }
            }
        } else if(LipossGlobals.inArea(Global.SXLT)){
            sql1.append("insert into stb_gw_softup_task(task_id,vendor_id,city_id,version_path_id,");
            sql1.append("record_time,update_time,acc_oid,valid,strategy_type,check_ip,check_mac,");
            sql1.append("file_path,load_status,check_account,account_status,account_ftpenbale,");
            sql1.append("account_ftpip,account_ftpname,account_ftppasswd,account_ftppath,check_devsn,task_type) ");
            sql1.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");

            sql1.setLong(1, id);
            sql1.setString(2, vendorId);
            sql1.setString(3, cityId);
            sql1.setLong(4, StringUtil.getLongValue(pathId));
            sql1.setLong(5, nowtime);
            sql1.setLong(6, nowtime);
            sql1.setLong(7, accoid);
            sql1.setLong(8, 0);
            sql1.setLong(9, StringUtil.getLongValue(strategyType));
            sql1.setInt(10, StringUtil.getIntegerValue(ipCheck));
            sql1.setInt(11, StringUtil.getIntegerValue(macCheck));
            sql1.setString(12, filePath);
            sql1.setInt(13, 0);
            sql1.setInt(14, StringUtil.getIntegerValue(custCheck));
            sql1.setInt(15, 0);
            sql1.setInt(16, StringUtil.getIntegerValue(ftpEnable));
            sql1.setString(17, ftpIp);
            sql1.setString(18, ftpName);
            sql1.setString(19, ftpPasswd);
            sql1.setString(20, ftpPath);
            sql1.setInt(21, StringUtil.getIntegerValue(devsnCheck));
            sql1.setString(22, taskType);
            sqllist.add(sql1.getSQL());
        }else{
        	sql1.append("insert into stb_gw_softup_task(task_id,vendor_id,city_id,version_path_id,");
            sql1.append("record_time,update_time,acc_oid,valid,strategy_type,check_ip,check_mac,");
            sql1.append("file_path,load_status,check_account,account_status,account_ftpenbale,");
            sql1.append("account_ftpip,account_ftpname,account_ftppasswd,account_ftppath) ");
            sql1.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");

            sql1.setLong(1, id);
            sql1.setString(2, vendorId);
            sql1.setString(3, cityId);
            sql1.setLong(4, StringUtil.getLongValue(pathId));
            sql1.setLong(5, nowtime);
            sql1.setLong(6, nowtime);
            sql1.setLong(7, accoid);
            sql1.setLong(8, 0);
            sql1.setLong(9, StringUtil.getLongValue(strategyType));
            sql1.setInt(10, StringUtil.getIntegerValue(ipCheck));
            sql1.setInt(11, StringUtil.getIntegerValue(macCheck));
            sql1.setString(12, filePath);
            sql1.setInt(13, 0);
            sql1.setInt(14, StringUtil.getIntegerValue(custCheck));
            sql1.setInt(15, 0);
            sql1.setInt(16, StringUtil.getIntegerValue(ftpEnable));
            sql1.setString(17, ftpIp);
            sql1.setString(18, ftpName);
            sql1.setString(19, ftpPasswd);
            sql1.setString(20, ftpPath);
            sqllist.add(sql1.getSQL());
        }

        if (!LipossGlobals.inArea(Global.SDLT)) {
            String[] devicetypeIds = devicetypeId.split(",");
            for (String string : devicetypeIds) {
                PrepareSQL sql2 = new PrepareSQL("insert into stb_gw_softup_task_devtype values(?,?) ");
                sql2.setLong(1, id);
                sql2.setLong(2, StringUtil.getLongValue(string));
                sqllist.add(sql2.getSQL());
            }
        }

        // 如果启动校验IP地址段,入库
        if ("1".equals(ipCheck.trim())) {
            String[] ipDuan = ipSG.trim().split(";");
            String[] ipStr;
            for (String s : ipDuan) {
                PrepareSQL sql3 = new PrepareSQL("insert into stb_gw_softup_task_ipcheck values(?,?,?) ");
                sql3.setLong(1, id);
                ipStr = s.split(",");
                sql3.setString(2, getFillIP(ipStr[0]));
                sql3.setString(3, getFillIP(ipStr[1]));
                sqllist.add(sql3.getSQL());
            }
        }
        // 如果启动校验mac地址,入库
        if ("1".equals(macCheck.trim())) {
            String[] macDuan = macSG.trim().split(";");
            String[] macStr;
            for (String s : macDuan) {
                PrepareSQL sql4 = new PrepareSQL("insert into stb_gw_softup_task_maccheck values(?,?,?) ");
                sql4.setLong(1, id);
                macStr = s.split(",");
                sql4.setString(2, macStr[0]);
                sql4.setString(3, macStr[1]);
                sqllist.add(sql4.getSQL());
            }
        }
        // 手工输入的帐号需要导入，帐号的后台来的导入。
        if ("1".equals(custCheck) && !StringUtil.IsEmpty(custSG)) {
            String[] custInfo = custSG.split(";");
            for (String s : custInfo) {
                PrepareSQL sql4 = new PrepareSQL("insert into stb_gw_softup_task_acccheck values(?,?) ");
                sql4.setLong(1, id);
                sql4.setString(2, s);
                sqllist.add(sql4.getSQL());
            }
        }
        logger.warn("sqllist.size=" + sqllist.size());
        int ier = DBOperation.executeUpdate(sqllist.toArray(new String[sqllist.size()]));
        if (ier > 0) {
            return 1;
        }
        return 0;
    }

    public int batchSoftUpgradeHNLT(long id, String vendorId, String cityId,
                                    String pathId, String strategyType, long accoid,
                                    String devicetypeId, String ipCheck, String ipSG, String macCheck,
                                    String macSG, String custCheck, String custSG, String filePath, String ftpEnable,
                                    String ftpIp, String ftpName, String ftpPasswd, String ftpPath, String check_net, String taskDesc, String taskDetail) {
        List<String> sqllist = new ArrayList<String>();
        long nowtime = new DateTimeUtil().getLongTime();

        PrepareSQL sql1 = new PrepareSQL();
        sql1.append("insert into stb_gw_softup_task(task_id,vendor_id,city_id,");
        sql1.append("version_path_id,record_time,update_time,acc_oid,valid,check_net,task_desc,task_detail_desc) ");
        sql1.append("values(?,?,?,?,?, ?,?,?,?,?, ?) ");
        sql1.setLong(1, id);
        sql1.setString(2, vendorId);
        sql1.setString(3, cityId);
        sql1.setLong(4, StringUtil.getLongValue(pathId));
        sql1.setLong(5, nowtime);
        sql1.setLong(6, nowtime);
        sql1.setLong(7, accoid);
        sql1.setLong(8, 0);
        sql1.setInt(9, StringUtil.getIntegerValue(check_net));
        sql1.setString(10, taskDesc);
        sql1.setString(11, taskDetail);
        sqllist.add(sql1.getSQL());

        String[] devicetypeIds = devicetypeId.split(",");
        for (String string : devicetypeIds) {
            PrepareSQL sql2 = new PrepareSQL("insert into stb_gw_softup_task_devtype values(?,?) ");
            sql2.setLong(1, id);
            sql2.setLong(2, StringUtil.getLongValue(string));
            sqllist.add(sql2.getSQL());
        }


        // 如果启动校验IP地址段,入库
        if ("1".equals(ipCheck.trim())) {
            String[] ipDuan = ipSG.trim().split(";");
            String[] ipStr;
            for (String s : ipDuan) {
                PrepareSQL sql3 = new PrepareSQL("insert into stb_gw_softup_task_ipcheck values(?,?,?) ");
                sql3.setLong(1, id);
                ipStr = s.split(",");
                sql3.setString(2, getFillIP(ipStr[0]));
                sql3.setString(3, getFillIP(ipStr[1]));
                sqllist.add(sql3.getSQL());
            }
        }
        // 如果启动校验mac地址,入库
        if ("1".equals(macCheck.trim())) {
            String[] macDuan = macSG.trim().split(";");
            String[] macStr;
            for (String s : macDuan) {
                PrepareSQL sql4 = new PrepareSQL("insert into stb_gw_softup_task_maccheck values(?,?,?) ");
                sql4.setLong(1, id);
                macStr = s.split(",");
                sql4.setString(2, macStr[0]);
                sql4.setString(3, macStr[1]);
                sqllist.add(sql4.getSQL());
            }
        }
        // 手工输入的帐号需要导入，帐号的后台来的导入。
        if ("1".equals(custCheck) && !StringUtil.IsEmpty(custSG)) {
            String[] custInfo = custSG.split(";");
            for (String s : custInfo) {
                PrepareSQL sql4 = new PrepareSQL("insert into stb_gw_softup_task_acccheck values(?,?) ");
                sql4.setLong(1, id);
                sql4.setString(2, s);
                sqllist.add(sql4.getSQL());
            }
        }
        logger.warn("sqllist.size=" + sqllist.size());
        int ier = DBOperation.executeUpdate(sqllist.toArray(new String[sqllist.size()]));
        if (ier > 0) {
            return 1;
        }
        return 0;
    }

    /**
     * 批量插入账号到stb_gw_softup_task_account
     */
    public void insertAccount(long taskId, List<String> dataList) {
        logger.warn("begin insertAccount");
        List<String> list = new ArrayList<String>();
        String[] strings;
        int index = 1;
        for (int i = 0; i < dataList.size(); i++, index++) {
            PrepareSQL sql = new PrepareSQL("insert into stb_gw_softup_task_acccheck values(?,?) ");
            sql.setLong(1, taskId);
            sql.setString(2, dataList.get(i));
            list.add(sql.getSQL());

            if (index >= 500) {
                strings = new String[list.size()];
                list.toArray(strings);
                DBOperation.executeUpdate(strings);
                list.clear();
                index = 1;
            }
        }

        if (list.size() > 0) {
            strings = new String[list.size()];
            list.toArray(strings);
            DBOperation.executeUpdate(strings);
            list = null;
        }
    }
    
    /**
     * 批量插入账号到stb_gw_softup_task_account
     */
    public void insertDevsn(long taskId, List<String> dataList) {
        logger.warn("begin insertDevsn");
        List<String> list = new ArrayList<String>();
        String[] strings;
        int index = 1;
        for (int i = 0; i < dataList.size(); i++, index++) {
            PrepareSQL sql = new PrepareSQL("insert into stb_gw_softup_task_devsnheck values(?,?) ");
            sql.setLong(1, taskId);
            sql.setString(2, dataList.get(i));
            list.add(sql.getSQL());

            if (index >= 500) {
                strings = new String[list.size()];
                list.toArray(strings);
                DBOperation.executeUpdate(strings);
                list.clear();
                index = 1;
            }
        }

        if (list.size() > 0) {
            strings = new String[list.size()];
            list.toArray(strings);
            DBOperation.executeUpdate(strings);
            list = null;
        }
    }
    
    public int del(long taskId,String vendorId,String type){
    	StringBuffer sb = new StringBuffer("");
    	if("1".equals(type)){
    		sb.append("delete from stb_gw_softup_task_acccheck a where ");
    		sb.append("not exists ( ");
    		sb.append("select 1 from stb_tab_customer b,stb_tab_gw_device c where ");
    		sb.append("a.serv_account=b.serv_account and b.customer_id=c.customer_id and c.vendor_id= '" + vendorId + "' ");
    		sb.append(") and a.task_id= " + taskId);
    	}else{
    		sb.append("delete from stb_gw_softup_task_devsnheck a where ");
    		sb.append("not exists ( ");
    		sb.append("select 1 from  ");
    		sb.append("stb_tab_gw_device b where a.dev_sn=b.device_serialnumber and b.vendor_id = '" + vendorId + "') ");
    		sb.append("and a.task_id="+taskId);
    	}
    	int num = 0;
    	try
		{
    		num = DBOperation.executeUpdate(new PrepareSQL(sb.toString()).getSQL());
		}
		catch (Exception e)
		{
			logger.error("del err, msgs : " + e.getMessage());
			num = 0;
		}
    	return num;
    }

    public int batchSoftUpgradeForHblt(String vendorId, String cityId,
                                       String pathId, String strategyType, long accoid,
                                       String devicetypeId, String ipCheck, String ipSG, String macCheck,
                                       String macSG, String custCheck, String custSG, String filePath) {
        List<String> sqllist = new ArrayList<String>();
        PrepareSQL sql1 = new PrepareSQL(
                "insert into stb_gw_softup_task(task_id,vendor_id,city_id,version_path_id,record_time,update_time,acc_oid,valid,strategy_type,check_ip,check_mac,file_path,load_status) values(?,?,?,?,?,?,?,?,?,?,?,?,?) ");
        long id = Math.round(Math.random() * 100000L);
        long nowtime = new DateTimeUtil().getLongTime();
        sql1.setLong(1, id);
        sql1.setString(2, vendorId);
        sql1.setString(3, cityId);
        sql1.setLong(4, StringUtil.getLongValue(pathId));
        sql1.setLong(5, nowtime);
        sql1.setLong(6, nowtime);
        sql1.setLong(7, accoid);
        sql1.setLong(8, 1);
        sql1.setLong(9, StringUtil.getLongValue(strategyType));
        sql1.setInt(10, Integer.parseInt(ipCheck));
        sql1.setInt(11, Integer.parseInt(macCheck));
        sql1.setString(12, filePath);
        sql1.setInt(13, 0);
        sqllist.add(sql1.getSQL());
        String[] devicetypeIds = devicetypeId.split(",");
        for (String string : devicetypeIds) {
            PrepareSQL sql2 = new PrepareSQL("insert into stb_gw_softup_task_devtype values(?,?) ");
            sql2.setLong(1, id);
            sql2.setLong(2, StringUtil.getLongValue(string));
            sqllist.add(sql2.getSQL());
        }
        // 如果启动校验IP地址段,入库
        if ("1".equals(ipCheck.trim())) {
            String[] ipDuan = ipSG.trim().split(";");
            String[] ipStr;
            logger.warn(ipDuan.toString());
            for (String s : ipDuan) {
                PrepareSQL sql3 = new PrepareSQL("insert into stb_gw_softup_task_ipcheck values(?,?,?) ");
                sql3.setLong(1, id);
                ipStr = s.split(",");
                logger.warn(ipStr.toString());
                sql3.setString(2, getFillIP(ipStr[0]));
                sql3.setString(3, getFillIP(ipStr[1]));
                sqllist.add(sql3.getSQL());
            }
        }
        // 如果启动校验mac地址,入库
        if ("1".equals(macCheck.trim())) {
            String[] macDuan = macSG.trim().split(";");
            String[] macStr;
            for (String s : macDuan) {
                PrepareSQL sql4 = new PrepareSQL("insert into stb_gw_softup_task_maccheck values(?,?,?) ");
                sql4.setLong(1, id);
                macStr = s.split(",");
                sql4.setString(2, macStr[0]);
                sql4.setString(3, macStr[1]);
                sqllist.add(sql4.getSQL());
            }
        }
        // 手工输入的帐号需要导入，帐号的后台来的导入。
        if ("1".equals(custCheck) && !StringUtil.IsEmpty(custSG)) {
            String[] custInfo = custSG.split(";");
            for (String s : custInfo) {
                PrepareSQL sql4 = new PrepareSQL("insert into stb_gw_softup_task_account values(?,?) ");
                sql4.setLong(1, id);
                sql4.setString(2, s);
                sqllist.add(sql4.getSQL());
            }
        }
        logger.warn("sqllist.size=" + sqllist.size());
        int ier = DBOperation.executeUpdate(sqllist.toArray(new String[sqllist.size()]));
        // int[] ier = jt.batchUpdate(sqllist.toArray(new
        // String[sqllist.size()]));
        logger.warn("ier=" + ier);
        // if (ier != null && ier.length > 0) {
        if (ier > 0) {
            return 1;
        }
        return 0;
    }

    public List checkVersionByTarget(String pathId) {
        // SQL语句中加入排序
        PrepareSQL sql = new PrepareSQL();
        sql.append("select c.devicetype_id,c.softwareversion,d.device_model ");
        if (LipossGlobals.inArea(Global.HNLT)) {// TODO wait (more table related)
            sql.append("from stb_soft_version_path a,stb_soft_version_path_model b,");
            sql.append("stb_tab_devicetype_info c,stb_gw_device_model d ");
            sql.append("where a.id=b.id and b.device_model_id=c.device_model_id ");
            sql.append("and c.device_model_id=d.device_model_id and a.id=? ");
        } else {// TODO wait (more table related)
            sql.append("from stb_gw_version_file_path a,stb_version_file_path_model b,");
            sql.append("stb_tab_devicetype_info c,stb_gw_device_model d ");
            sql.append("where a.id=b.path_id and b.device_model_id=c.device_model_id ");
            sql.append("and c.device_model_id=d.device_model_id ");
            sql.append("and a.valid=1 and a.id=? ");
        }
        sql.append("order by d.device_model ");

        sql.setLong(1, StringUtil.getLongValue(pathId));
        return jt.queryForList(sql.getSQL());
    }

    /**
     * 查看结果
     */
    public Map getCountResult(String taskId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("0", "0");
        map.put("updatesucc", "0");// 更新服务器地址修改成功
        map.put("softupsucc", "0");
        map.put("updatefailed", "0");
        map.put("unsupport", "0");    //不支持的设备
        map.put("undo", "0");

        PrepareSQL sql = new PrepareSQL();
        if (LipossGlobals.inArea(Global.HNLT)) {
            sql.append("select result_id as result,count(*) as num from stb_gw_strategy_soft_log ");
            sql.append("where service_id=5 and task_id=? group by result_id ");
        } else {
            sql.append("select result,count(*) as num from stb_gw_softup_record ");
            sql.append("where task_id=? group by result ");
        }
        sql.setLong(1, StringUtil.getLongValue(taskId));
        List list = jt.queryForList(sql.getSQL());
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Map rmap = (Map) list.get(i);
                String result = StringUtil.getStringValue(rmap.get("result"));
                if ("1".equals(result)) {
                    map.put("updatesucc", StringUtil.getStringValue(rmap.get("num")));
                } else if ("2".equals(result)) {
                    map.put("softupsucc", StringUtil.getStringValue(rmap.get("num")));
                } else if ("-1".equals(result)) {
                    map.put("updatefailed", StringUtil.getStringValue(rmap.get("num")));
                } else if ("3".equals(result)) {
                    map.put("unsupport", StringUtil.getStringValue(rmap.get("num")));    //湖南联通 机顶盒行、串货管控
                } else if ("0".equals(result)) {
                    map.put("undo", StringUtil.getStringValue(rmap.get("num")));
                }
            }
        }
        return map;
    }
    
    /**
     * 查看结果
     */
    public Map getCountResult_v2(String taskId,String taskImportType) {
    	logger.warn("getCountResult_v2 ： taskId:{}",taskId);
        Map<String, String> map = new HashMap<String, String>();
        map.put("importtype", taskImportType);
        map.put("total", "0");// 更新服务器地址修改成功
        map.put("updatesucc", "0");// 更新服务器地址修改成功
        map.put("softupsucc", "0");
        map.put("updatefailed", "0");
        map.put("unsupport", "0");    //不支持的设备
        map.put("undo", "0");

       
        PrepareSQL sql = new PrepareSQL();
        //获取总数
        if("1".equals(taskImportType)){
        	sql.append("select count(*) from stb_gw_softup_task_devsnheck where task_id ="+taskId);
        	
        }else if("2".equals(taskImportType)){
        	sql.append("select count(*) from stb_gw_softup_task_acccheck where task_id ="+taskId);
        	
        }else{
        	logger.error("[{}]error getCountResult_v2 ,无法获取导入的时候业务账号还是设备序列号",taskId);
        	return null;
        }
        
        int total = jt.queryForInt(sql.getSQL());
        map.put("total", StringUtil.getStringValue(total));
        
        //获取升级结果
        sql = new PrepareSQL();
        sql.append("select result,count(*) as num from stb_gw_softup_record ");
        sql.append("where task_id="+taskId+" group by result ");
        List list = jt.queryForList(sql.getSQL());
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Map rmap = (Map) list.get(i);
                String result = StringUtil.getStringValue(rmap.get("result"));
                if ("1".equals(result)) {
                    map.put("updatesucc", StringUtil.getStringValue(rmap.get("num")));
                } else if ("2".equals(result)) {
                    map.put("softupsucc", StringUtil.getStringValue(rmap.get("num")));
                } else if ("-1".equals(result)) {
                    map.put("updatefailed", StringUtil.getStringValue(rmap.get("num")));
                } else if ("3".equals(result)) {
                    map.put("unsupport", StringUtil.getStringValue(rmap.get("num")));    //湖南联通 机顶盒行、串货管控
                } /*else if ("0".equals(result)) {
                    map.put("undo", StringUtil.getStringValue(rmap.get("num")));
                }*/
            }
            
            int succ = StringUtil.getIntegerValue(map.get("updatesucc"));
            int fail = StringUtil.getIntegerValue(map.get("updatefailed"));
            int notsuport = StringUtil.getIntegerValue(map.get("unsupport"));
            int undo = total - succ - fail - notsuport;
            map.put("undo", StringUtil.getStringValue(undo));
        }else{
        	 map.put("undo", StringUtil.getStringValue(total));
        }
        return map;
    }

    /**
     * 查看结果 湖南联通
     */
    public List<HashMap<String, String>> getCountResultHnlt(String taskId, String cityId) {
        List<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
        List<String> cityIdList = getSencondCityIdList();
        int updateSuccToatl = 0;
        int softUpSuccToatl = 0;
        int updateFailedToatl = 0;
        int unSupportTotal = 0;
        int allTotal = 0;
        if ("".equals(cityId) || "00".equals(cityId)) {
            //查询省中心，先把所有二级地市给汇总，然后再单独查询省中心的数据，最后再汇聚
            // （1）先把所有二级地市给汇总，
            for (String city_id : cityIdList) {
                HashMap<String, String> result = (HashMap<String, String>) getCountResultByCityId(taskId, city_id);
                resultList.add(result);
            }
            //（2）单独查询省中心的数据
            HashMap<String, String> result = (HashMap<String, String>) getCountResultByCityId(taskId, "00");

            //(3)汇聚
            resultList.add(result);
        } else {
            //非省中心 则直接查询传入的二级地市即可。
            HashMap<String, String> result = (HashMap<String, String>) getCountResultByCityId(taskId, cityId);
            resultList.add(result);
        }

        //所有属地完成  开始汇聚小计行
        HashMap<String, String> totalMap = new HashMap<String, String>();
        for (HashMap<String, String> resultMap : resultList) {
            updateSuccToatl = updateSuccToatl + StringUtil.getIntegerValue(resultMap.get("updatesucc"));
            softUpSuccToatl = softUpSuccToatl + StringUtil.getIntegerValue(resultMap.get("softupsucc"));
            updateFailedToatl = updateFailedToatl + StringUtil.getIntegerValue(resultMap.get("updatefailed"));
            unSupportTotal = unSupportTotal + StringUtil.getIntegerValue(resultMap.get("unsupport"));
        }
        allTotal = updateSuccToatl + softUpSuccToatl + updateFailedToatl + unSupportTotal;
        BigDecimal allTotalBigDecimal = new BigDecimal(allTotal);

		for (HashMap<String, String> resultMap : resultList)
		{
			if (BigDecimal.ZERO.equals(allTotalBigDecimal))
			{
				resultMap.put("totalPer",  "0.00%");
			}
			else
			{
				//总占比量
				BigDecimal total = new BigDecimal(StringUtil.getStringValue(resultMap.get("total")));
				BigDecimal totalPer = total.multiply(BigDecimal.TEN).multiply(BigDecimal.TEN)
						.divide(allTotalBigDecimal,2, BigDecimal.ROUND_HALF_UP);
				resultMap.put("totalPer", StringUtil.getStringValue(totalPer) + "%");
			}
		}

		BigDecimal softUpSuccToatlBigDecimal = new BigDecimal(softUpSuccToatl);
		if(BigDecimal.ZERO.equals(allTotalBigDecimal))
		{
            totalMap.put("succPer","0.00%");
        }
		else
        {
            BigDecimal succPer = softUpSuccToatlBigDecimal.multiply(BigDecimal.TEN).multiply(BigDecimal.TEN)
                    .divide(allTotalBigDecimal,2, BigDecimal.ROUND_HALF_UP);
            totalMap.put("succPer",StringUtil.getStringValue(succPer)+"%");
        }

		//封装入totalMap
		totalMap.put("city_name","小计");
		totalMap.put("updatesucc",StringUtil.getStringValue(updateSuccToatl));
		totalMap.put("softupsucc",StringUtil.getStringValue(softUpSuccToatl));
		totalMap.put("updatefailed",StringUtil.getStringValue(updateFailedToatl));
		totalMap.put("unsupport",StringUtil.getStringValue(unSupportTotal));
		totalMap.put("total",StringUtil.getStringValue(allTotal));
		totalMap.put("totalPer","");


		resultList.add(totalMap);
        //小计行汇聚完成，开始处理占比行

		HashMap perMap = new HashMap();

        perMap.put("city_name","占比");
		if(BigDecimal.ZERO.equals(allTotalBigDecimal))
		{
            perMap.put("updatesucc","0.00%");

            perMap.put("softupsucc","0.00%");

            perMap.put("updatefailed","0.00%");

            perMap.put("unsupport","0.00%");
        }
		else
        {
            perMap.put("updatesucc",StringUtil.getStringValue(
                    new BigDecimal(updateSuccToatl).multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).
                            divide(allTotalBigDecimal,2, BigDecimal.ROUND_HALF_UP))+"%");

            perMap.put("softupsucc",StringUtil.getStringValue(
                    new BigDecimal(softUpSuccToatl).multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).
                            divide(allTotalBigDecimal,2, BigDecimal.ROUND_HALF_UP))+"%");

            perMap.put("updatefailed",StringUtil.getStringValue(
                    new BigDecimal(updateFailedToatl).multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).
                            divide(allTotalBigDecimal,2, BigDecimal.ROUND_HALF_UP))+"%");

            perMap.put("unsupport",StringUtil.getStringValue(
                    new BigDecimal(unSupportTotal).multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).
                            divide(allTotalBigDecimal,2, BigDecimal.ROUND_HALF_UP))+"%");
        }

        perMap.put("total","");
        perMap.put("totalPer","");
        perMap.put("succPer","");
		resultList.add(perMap);

        return resultList;
    }


    /**
     * 查看结果
     */
    public Map getCountResultByCityId(String taskId, String cityId) {
        Map<String, String> map = new HashMap<String, String>();
        int total = 0;
        map.put("0", "0");
        map.put("updatesucc", "0");// 更新服务器地址修改成功
        map.put("softupsucc", "0");
        map.put("updatefailed", "0");
        map.put("unsupport", "0");    //不支持的设备
        map.put("city_id", cityId);
        map.put("city_name", CityDAO.getCityName(cityId));
        PrepareSQL sql = new PrepareSQL();
        if ("00".equals(cityId)) {
            sql.append("select result_id as result,count(*) as num from stb_gw_strategy_soft_log a,stb_tab_gw_device b ");
            sql.append("where a.service_id=5 and a.device_id = b.device_id  " +
                    " and b.city_id= ?  and a.task_id=? group by a.result_id ");
            sql.setString(1, cityId);
            sql.setLong(2, StringUtil.getLongValue(taskId));
        } else {// TODO wait (more table related)
            sql.append("select result_id as result,count(*) as num from stb_gw_strategy_soft_log a,stb_tab_gw_device b,tab_city c ");
            sql.append("where a.service_id=5 and a.device_id = b.device_id and c.city_id = b.city_id " +
                    " and (c.city_id= ? or c.parent_id = ? ) and a.task_id=? group by a.result_id ");
            sql.setString(1, cityId);
            sql.setString(2, cityId);
            sql.setLong(3, StringUtil.getLongValue(taskId));
        }

        List list = jt.queryForList(sql.getSQL());
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Map rmap = (Map) list.get(i);
                String result = StringUtil.getStringValue(rmap.get("result"));
                if ("1".equals(result)) {
                    map.put("updatesucc", StringUtil.getStringValue(rmap.get("num")));
                    total = total + StringUtil.getIntegerValue(rmap.get("num"));
                } else if ("2".equals(result)) {
                    map.put("softupsucc", StringUtil.getStringValue(rmap.get("num")));
                    total = total + StringUtil.getIntegerValue(rmap.get("num"));
                } else if ("-1".equals(result)) {
                    map.put("updatefailed", StringUtil.getStringValue(rmap.get("num")));
                    total = total + StringUtil.getIntegerValue(rmap.get("num"));
                } else if ("3".equals(result)) {
                    map.put("unsupport", StringUtil.getStringValue(rmap.get("num")));    //湖南联通 机顶盒行、串货管控
                    total = total + StringUtil.getIntegerValue(rmap.get("num"));
                }
            }
        }
        map.put("total", StringUtil.getStringValue(total));

		//成功占比量
        if(total == 0)
        {
			map.put("succPer", "0.00%");
		}
        else
		{
			BigDecimal softupsucc = new BigDecimal(StringUtil.getStringValue(map.get("softupsucc")));
			BigDecimal succPer = softupsucc.multiply(BigDecimal.TEN).multiply(BigDecimal.TEN)
					.divide(new BigDecimal(total),2, BigDecimal.ROUND_HALF_UP);
			map.put("succPer", StringUtil.getStringValue(succPer)+"%");
		}
        return map;
    }

    public List getSoftupTask(int curPage_splitPage, int num_splitPage, String queryCityId,
                              String queryVendorId, String queryVaild, String taskDesc, String source_devicetypeId,
                              String softwareversion, String startTime, String endTime) {
        // TODO wait (more table related)
        PrepareSQL sql = new PrepareSQL(
                "select a.vendor_id, a.city_id, a.acc_oid, a.task_id, a.task_desc, a.record_time, a.update_time, a.valid," +
                        "b.acc_loginname,c.softwareversion ,c.version_path from stb_gw_softup_task a,tab_accounts b,stb_gw_version_file_path c " +
                        "where a.acc_oid=b.acc_oid and a.version_path_id=c.id and c.version_type=0 order by a.record_time desc");
        // modify by wufei begin // TODO wait (more table related)
        String SQL = "select a.vendor_id, a.city_id, a.acc_oid, a.task_id, a.task_desc, a.record_time, a.update_time, a.valid," +
                "b.acc_loginname,c.softwareversion,c.version_path from stb_gw_softup_task a,tab_accounts b,stb_gw_version_file_path c " +
                "where a.valid!=-1 and a.acc_oid=b.acc_oid and a.version_path_id=c.id and c.version_type=0 ";
        if (LipossGlobals.inArea(Global.HBLT) || LipossGlobals.inArea(Global.JXDX) || LipossGlobals.inArea(Global.NXDX)) {
            SQL = "select a.vendor_id, a.city_id, a.acc_oid, a.task_id, a.task_desc, a.record_time, a.update_time, a.valid," +
                    "b.acc_loginname,c.softwareversion,c.version_path from  stb_gw_softup_task a left join tab_accounts b " +
                    "on a.acc_oid=b.acc_oid left join stb_gw_version_file_path c on a.version_path_id=c.id where a.valid!=-1";
        } else if (LipossGlobals.inArea(Global.HNLT)) {// TODO wait (more table related)
            //SQL = "select a.*,b.acc_loginname,c.version_name as softwareversion,c.version_path " +
            SQL = "select a.vendor_id,a.city_id,a.acc_oid,a.task_id,a.task_desc,a.record_time,a.valid," +
                    "b.acc_loginname,c.version_name as softwareversion,c.version_path " +
                    "from stb_gw_softup_task a left join tab_accounts b " +
                    "on a.acc_oid=b.acc_oid left join stb_soft_version_path c " +
                    "on a.version_path_id=c.id where 1=1 ";
        } else if (LipossGlobals.inArea(Global.XJDX)) {// TODO wait (more table related)
            SQL = "select a.vendor_id, a.city_id, a.acc_oid, a.task_id, a.task_desc, a.record_time, a.update_time, a.valid, " +
                    "b.acc_loginname, c.softwareversion, c.version_path " +
                    "from stb_gw_softup_task a  " +
                    "left join tab_accounts b on a.acc_oid=b.acc_oid " +
                    "left join stb_gw_version_file_path c on a.version_path_id=c.id  " +
                    "left join stb_gw_softup_task_devtype d on d.task_id=a.task_id  where a.valid != -1 and c.version_type = 0 ";
        }
        if (null != queryCityId && !"00".equals(queryCityId)) {
            SQL += " and a.city_id='" + queryCityId + "'";
        }
        if (!StringUtil.IsEmpty(queryVendorId) && !"-1".equals(queryVendorId)) {
            SQL += " and a.vendor_id='" + queryVendorId + "'";
        }
        if (!StringUtil.IsEmpty(queryVaild) && !"-1".equals(queryVaild)) {
            if (LipossGlobals.inArea(Global.HNLT) && !"0".equals(queryVaild)) {
                SQL += " and a.valid in(1,2) ";
            } else {
                SQL += " and a.valid=" + queryVaild;
            }
        }

        if (LipossGlobals.inArea(Global.HBLT) || LipossGlobals.inArea(Global.JXDX) || LipossGlobals.inArea(Global.NXDX)) {
            if (!StringUtil.IsEmpty(taskDesc)) {
                SQL += " and a.task_desc like '%" + taskDesc + "%'";
            }
        }

        if (LipossGlobals.inArea(Global.XJDX)) {
            if (!StringUtil.IsEmpty(softwareversion)) {
                SQL += " and c.softwareversion like '%" + softwareversion + "%' ";
            }
            if (!StringUtil.IsEmpty(source_devicetypeId)) {
                SQL += " and d.devicetype_id=" + source_devicetypeId + "";
            }
            if (!StringUtil.IsEmpty(startTime)) {
                SQL += " and a.record_time>=" + getTime(startTime) + "";
            }
            if (!StringUtil.IsEmpty(endTime)) {
                SQL += " and  a.record_time<=" + getTime(endTime) + " ";
            }
        }

        SQL += " order by a.record_time desc";
        sql.setSQL(SQL);
        // modify by wufei end
        vendorMap = GwStbVendorModelVersionDAO.getVendorMap();
        cityMap = CityDAO.getCityIdCityNameMap();
        List<Map> list = jt.querySP(sql.getSQL(), (curPage_splitPage - 1)
                * num_splitPage, num_splitPage, new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                String vendor_id = rs.getString("vendor_id");
                map.put("vendor_id", vendor_id);
                if ("-1".equals(vendor_id) || vendor_id == null || "".equals(vendor_id)) {
                    map.put("vendor_add", "-");
                } else {
                    map.put("vendor_add", vendorMap.get(vendor_id));
                }
                String city_id = rs.getString("city_id");
                map.put("city_id", city_id);
                if ("-1".equals(city_id) || city_id == null) {
                    map.put("city_name", "-");
                } else {
                    map.put("city_name", cityMap.get(city_id));
                }
                String softwareversion = rs.getString("softwareversion");
                if ("-1".equals(softwareversion) || softwareversion == null || "".equals(softwareversion)) {
                    map.put("softwareversion", "-");
                } else {
                    map.put("softwareversion", softwareversion);
                }
                map.put("version_path", rs.getString("version_path"));
                map.put("acc_loginname", rs.getString("acc_loginname"));
                map.put("account_id", rs.getString("acc_oid"));
                map.put("task_id", rs.getString("task_id"));
                if (LipossGlobals.inArea(Global.HBLT) || LipossGlobals.inArea(Global.JXDX)
                        || LipossGlobals.inArea(Global.NXDX) || LipossGlobals.inArea(Global.HNLT)) {
                    map.put("task_desc", rs.getString("task_desc"));
                }
                try {
                    long record_time = StringUtil.getLongValue(rs.getString("record_time"));
                    DateTimeUtil dt = new DateTimeUtil(record_time * 1000);
                    map.put("record_time", dt.getLongDate());
                } catch (Exception e) {
                    map.put("record_time", "");
                }
                try {
                    long update_time = StringUtil.getLongValue(rs.getString("update_time"));
                    DateTimeUtil dt = new DateTimeUtil(update_time * 1000);
                    map.put("update_time", dt.getLongDate());
                } catch (Exception e) {
                    map.put("update_time", "");
                }

                if (LipossGlobals.inArea(Global.HNLT)) {
                    String valid = rs.getString("valid");
                    map.put("valid", "0".equals(valid) ? "0" : "1");
                } else {
                    map.put("valid", rs.getString("valid"));
                }
                return map;
            }
        });
        return list;
    }


    public List getSoftupTaskSxlt(int curPage_splitPage, int num_splitPage, String queryCityId,
                                  String queryVendorId, String queryVaild, String taskDesc, String source_devicetypeId,
                                  String softwareversion, String startTime, String endTime,String taskType) {
        // TODO wait (more table related)
        PrepareSQL sql = new PrepareSQL(
                "select a.vendor_id,a.city_id,a.acc_oid,a.task_id,a.task_desc,a.record_time,a.valid," +
                    "b.acc_loginname,c.softwareversion ,c.version_path from stb_gw_softup_task a,tab_accounts b,stb_gw_version_file_path c " +
                    "where a.acc_oid=b.acc_oid and a.version_path_id=c.id and c.version_type=0 order by a.record_time desc");
        // modify by wufei begin // TODO wait (more table related)
        String SQL = "select a.vendor_id,a.city_id,a.acc_oid,a.task_id,a.task_desc,a.record_time,a.valid," +
                "b.acc_loginname,c.softwareversion,c.version_path,a.task_type,a.update_time,a.check_devsn,a.check_account from stb_gw_softup_task a,tab_accounts b,stb_gw_version_file_path c " +
                "where a.valid!=-1 and a.acc_oid=b.acc_oid and a.version_path_id=c.id and c.version_type=0 ";

        if (null != queryCityId && !"00".equals(queryCityId)) {
            SQL += " and a.city_id like'%" + queryCityId + "%'";
        }
        if (!StringUtil.IsEmpty(queryVendorId) && !"-1".equals(queryVendorId)) {
            SQL += " and a.vendor_id='" + queryVendorId + "'";
        }
        if (!StringUtil.IsEmpty(queryVaild) && !"-1".equals(queryVaild)) {
            SQL += " and a.valid=" + queryVaild;
        }
        
        if(!StringUtil.IsEmpty(taskType) && !"-1".equals(taskType) && LipossGlobals.inArea(Global.SXLT)){
        	SQL += " and a.task_type = " + taskType;
        }

        SQL += " order by a.record_time desc";
        sql.setSQL(SQL);
        // modify by wufei end
        vendorMap = GwStbVendorModelVersionDAO.getVendorMap();
        cityMap = CityDAO.getCityIdCityNameMap();
        List<Map> list = jt.querySP(sql.getSQL(), (curPage_splitPage - 1)
                * num_splitPage, num_splitPage, new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                String vendor_id = rs.getString("vendor_id");
                map.put("vendor_id", vendor_id);
                if ("-1".equals(vendor_id) || vendor_id == null || "".equals(vendor_id)) {
                    map.put("vendor_add", "-");
                } else {
                    map.put("vendor_add", vendorMap.get(vendor_id));
                }
                String city_id = rs.getString("city_id");
                map.put("city_id", city_id);
                if ("-1".equals(city_id) || city_id == null) {
                    map.put("city_name", "-");
                } else {
                    String city_name = "";
                    if (city_id.substring(city_id.length() - 1).equals(",")) {
                        city_id = city_id.substring(0, city_id.length() - 1);
                    }
                    String[] args = city_id.split(",");
                    for (int i = 0; i < args.length; i++) {
                        city_name = city_name + StringUtil.getStringValue(cityMap, args[i], "") + ",";
                    }
                    if (city_name.substring(city_name.length() - 1).equals(",")) {
                        city_name = city_name.substring(0, city_name.length() - 1);
                    }
                    map.put("city_name", city_name);
                }
                String softwareversion = rs.getString("softwareversion");
                if ("-1".equals(softwareversion) || softwareversion == null || "".equals(softwareversion)) {
                    map.put("softwareversion", "-");
                } else {
                    map.put("softwareversion", softwareversion);
                }
                map.put("version_path", rs.getString("version_path"));
                map.put("acc_loginname", rs.getString("acc_loginname"));
                map.put("account_id", rs.getString("acc_oid"));
                map.put("task_id", rs.getString("task_id"));

                try {
                    long record_time = StringUtil.getLongValue(rs.getString("record_time"));
                    DateTimeUtil dt = new DateTimeUtil(record_time * 1000);
                    map.put("record_time", dt.getLongDate());
                } catch (Exception e) {
                    map.put("record_time", "");
                }
                try {
                    long update_time = StringUtil.getLongValue(rs.getString("update_time"));
                    DateTimeUtil dt = new DateTimeUtil(update_time * 1000);
                    map.put("update_time", dt.getLongDate());
                } catch (Exception e) {
                    map.put("update_time", "");
                }

                map.put("valid", rs.getString("valid"));
                
                String taskType = StringUtil.getStringValue(rs.getString("task_type"));
                map.put("type", taskType);
                
                //1:导入序列号，2 导入业务账号
                String import_type = "0";
                String importType_account = StringUtil.getStringValue(rs.getString("check_account"));
                String importType_sn = StringUtil.getStringValue(rs.getString("check_devsn"));
                if("1".equals(taskType)){
                	if("1".equals(importType_sn)){
                		import_type = "1";
                	}else{
                		import_type = "2";
                	}
                }
                map.put("import_type", import_type);
                return map;
            }
        });
        return list;
    }


    public int countSoftupTask(int curPage_splitPage, int num_splitPage, String queryCityId,
                               String queryVendorId, String queryVaild, String taskDesc, String source_devicetypeId,
                               String softwareversion, String startTime, String endTime) {
        // TODO wait (more table related)
        PrepareSQL sql = new PrepareSQL(
                "select count(*) from stb_gw_softup_task a,tab_accounts b,stb_gw_version_file_path c where a.acc_oid=b.acc_oid and a.version_path_id=c.id and c.version_type=0");
        // modify by wufei begin // TODO wait (more table related)
        String SQL = "select count(*) from stb_gw_softup_task a,tab_accounts b,stb_gw_version_file_path c where a.valid!=-1 and a.acc_oid=b.acc_oid and a.version_path_id=c.id and c.version_type=0 ";
        if (LipossGlobals.inArea(Global.HBLT) || LipossGlobals.inArea(Global.JXDX) || LipossGlobals.inArea(Global.NXDX)) {
            // TODO wait (more table related)
            SQL = "select count(*) from  stb_gw_softup_task a left join tab_accounts b " +
                    "on a.acc_oid=b.acc_oid left join stb_gw_version_file_path c on a.version_path_id=c.id where a.valid!=-1";
        } else if (LipossGlobals.inArea(Global.HNLT)) {// TODO wait (more table related)
            SQL = "select count(*) from  stb_gw_softup_task a left join tab_accounts b " +
                    "on a.acc_oid=b.acc_oid left join stb_soft_version_path c on a.version_path_id=c.id where a.valid!=-1";
        } else if (LipossGlobals.inArea(Global.XJDX)) {// TODO wait (more table related)
            SQL = "select count(*) " +
                    "from stb_gw_softup_task a  " +
                    "left join tab_accounts b on a.acc_oid=b.acc_oid " +
                    "left join stb_gw_version_file_path c on a.version_path_id=c.id  " +
                    "left join stb_gw_softup_task_devtype d on d.task_id=a.task_id  where a.valid != -1 and c.version_type = 0 ";
        }

        if (null != queryCityId && !"00".equals(queryCityId)) {
            SQL += " and a.city_id='" + queryCityId + "'";
        }
        if (!StringUtil.IsEmpty(queryVendorId) && !"-1".equals(queryVendorId)) {
            SQL += " and a.vendor_id='" + queryVendorId + "'";
        }
        if (!StringUtil.IsEmpty(queryVaild) && !"-1".equals(queryVaild)) {
            if (LipossGlobals.inArea(Global.HNLT) && !"0".equals(queryVaild)) {
                SQL += " and a.valid in(1,2) ";
            } else {
                SQL += " and a.valid=" + queryVaild;
            }
        }
        if (LipossGlobals.inArea(Global.HBLT) || LipossGlobals.inArea(Global.JXDX)) {
            if (!StringUtil.IsEmpty(taskDesc)) {
                SQL += " and a.task_desc like '%" + taskDesc + "%'";
            }
        }

        if (LipossGlobals.inArea(Global.XJDX)) {
            if (!StringUtil.IsEmpty(softwareversion)) {
                SQL += " and c.softwareversion like '%" + softwareversion + "%' ";
            }
            if (!StringUtil.IsEmpty(source_devicetypeId)) {
                SQL += " and d.devicetype_id=" + source_devicetypeId + "";
            }
            if (!StringUtil.IsEmpty(startTime)) {
                SQL += " and a.record_time>=" + getTime(startTime) + "";
            }
            if (!StringUtil.IsEmpty(endTime)) {
                SQL += " and  a.record_time<=" + getTime(endTime) + " ";
            }
        }

        sql.setSQL(SQL);
        // modify by wufei end
        int total = jt.queryForInt(sql.getSQL());
        int maxPage = 1;
        if (total % num_splitPage == 0) {
            maxPage = total / num_splitPage;
        } else {
            maxPage = total / num_splitPage + 1;
        }
        return maxPage;
    }


    public int countSoftupTaskSxlt(int curPage_splitPage, int num_splitPage, String queryCityId,
                                   String queryVendorId, String queryVaild, String taskDesc, String source_devicetypeId,
                                   String softwareversion, String startTime, String endTime,String taskType) {
        // TODO wait (more table related)
        PrepareSQL sql = new PrepareSQL(
                "select count(*) from stb_gw_softup_task a,tab_accounts b,stb_gw_version_file_path c where a.acc_oid=b.acc_oid and a.version_path_id=c.id and c.version_type=0");
        // modify by wufei begin // TODO wait (more table related)
        String SQL = "select count(*) from stb_gw_softup_task a,tab_accounts b,stb_gw_version_file_path c where a.valid!=-1 and a.acc_oid=b.acc_oid and a.version_path_id=c.id and c.version_type=0 ";

        if (null != queryCityId && !"00".equals(queryCityId)) {
            SQL += " and a.city_id like'%" + queryCityId + "%'";
        }
        if (!StringUtil.IsEmpty(queryVendorId) && !"-1".equals(queryVendorId)) {
            SQL += " and a.vendor_id='" + queryVendorId + "'";
        }
        if (!StringUtil.IsEmpty(queryVaild) && !"-1".equals(queryVaild)) {
            if (LipossGlobals.inArea(Global.HNLT) && !"0".equals(queryVaild)) {
                SQL += " and a.valid in(1,2) ";
            } else {
                SQL += " and a.valid=" + queryVaild;
            }
        }
        
        if(!StringUtil.IsEmpty(taskType)  && !"-1".equals(taskType) && LipossGlobals.inArea(Global.SXLT)){
        	SQL += " and a.task_type = " + taskType;
        }

        sql.setSQL(SQL);
        // modify by wufei end
        int total = jt.queryForInt(sql.getSQL());
        int maxPage = 1;
        if (total % num_splitPage == 0) {
            maxPage = total / num_splitPage;
        } else {
            maxPage = total / num_splitPage + 1;
        }
        return maxPage;
    }

    /**
     * 取消任务
     */
    public int cancerTask(String taskId, String isImport) {
        List<String> sqlList = new ArrayList<String>();
        PrepareSQL sql = new PrepareSQL(
                "update stb_gw_softup_task set valid=0,update_time=? where task_id=?");
        sql.setLong(1, System.currentTimeMillis() / 1000L);
        sql.setLong(2, StringUtil.getLongValue(taskId));
        sqlList.add(sql.getSQL());

        if ("1".equals(isImport)) {
            sql = new PrepareSQL(
                    "delete from stb_gw_strategy_soft where task_id='" + taskId + "'");
            sqlList.add(sql.getSQL());
            sql = new PrepareSQL(
                    "delete from stb_gw_strategy_soft_log where task_id='" + taskId + "'");
            if (LipossGlobals.inArea(Global.HNLT)) {
                sql.append(" and result_id=-1 ");
            }
            sqlList.add(sql.getSQL());
        }

        if (LipossGlobals.inArea(Global.HNLT)) {
            sql = new PrepareSQL();
            sql.append("update stb_gw_strategy_soft set status=0 where task_id='" + taskId + "'");
            sql.append(" and result_id=-1 and service_id=5 ");
            sqlList.add(sql.getSQL());

            sql = new PrepareSQL();
            sql.append("update stb_gw_strategy_soft_log set status=0 where task_id='" + taskId + "'");
            sql.append(" and result_id=-1 and service_id=5 ");
            sqlList.add(sql.getSQL());
        }

        if (LipossGlobals.inArea(Global.SXLT)) {
            sql = new PrepareSQL();
            sql.append("update stb_gw_strategy_soft set status=7 where task_id='" + taskId + "' and status=0");
            sqlList.add(sql.getSQL());

            sql = new PrepareSQL();
            sql.append("update stb_gw_strategy_soft_log set status=7 where task_id='" + taskId + "' and status=0");
            sqlList.add(sql.getSQL());
        }
        String[] sqlArray = sqlList.toArray(new String[0]);
        return jt.batchUpdate(sqlArray).length;
//		return jt.update(sql.getSQL());
    }

    /**
     * 删除任务
     */
    public int deleteTask(String taskId, String isImport) {
        List<String> sqlList = new ArrayList<String>();

        PrepareSQL psql = new PrepareSQL();
        if (LipossGlobals.inArea(Global.HNLT) || LipossGlobals.inArea(Global.SXLT)) {
            psql.append("delete from stb_gw_softup_task where task_id=? ");
            psql.setLong(1, StringUtil.getLongValue(taskId));
            sqlList.add(psql.getSQL());

            psql = new PrepareSQL();
            psql.append("delete from stb_gw_softup_task_devtype where task_id=? ");
            psql.setLong(1, StringUtil.getLongValue(taskId));
            sqlList.add(psql.getSQL());

            psql = new PrepareSQL();
            psql.append("delete from stb_gw_strategy_soft_log where task_id=? and service_id=5 and result_id=-1 ");
            psql.setLong(1, StringUtil.getLongValue(taskId));
            sqlList.add(psql.getSQL());

            psql = new PrepareSQL();
            psql.append("delete from stb_gw_strategy_soft where task_id=? and service_id=5 ");
            psql.setLong(1, StringUtil.getLongValue(taskId));
            sqlList.add(psql.getSQL());
        } else {
            psql = new PrepareSQL("update stb_gw_softup_task set valid=-1 where task_id="
                    + StringUtil.getLongValue(taskId));
            sqlList.add(psql.getSQL());

            //新疆电信没有stb_gw_softup_batch表
            if (!LipossGlobals.inArea(Global.XJDX)) {
                psql = new PrepareSQL("delete from stb_gw_softup_batch where id="
                        + Integer.parseInt(taskId) + " and task_id="
                        + StringUtil.getLongValue(taskId));
                sqlList.add(psql.getSQL());
                psql = new PrepareSQL(
                        "delete from stb_gw_softup_batch_version where batch_id="
                                + StringUtil.getLongValue(taskId));
                sqlList.add(psql.getSQL());
            }
        }

        if ("1".equals(isImport) || LipossGlobals.inArea(Global.XJDX)) {
            String tableName = "stb_gw_strategy_soft";

            if (LipossGlobals.inArea(Global.JXDX)) {
                tableName = LipossGlobals.getLipossProperty("stb_strategy_tabname.soft.tabname");
                if (StringUtil.IsEmpty(tableName)) {
                    logger.error("机顶盒软件升级策略表未配置，请配置后重新操作！！！");
                    return 0;
                }
            }
            if (LipossGlobals.inArea(Global.NXDX)) {
                tableName = "stb_gw_serv_strategy";
            }
            psql = new PrepareSQL(
                    "delete from " + tableName + " where task_id='" + taskId + "'");
            sqlList.add(psql.getSQL());

            psql = new PrepareSQL(
                    "delete from " + tableName + "_log where task_id='" + taskId + "'");
            sqlList.add(psql.getSQL());
        }

        String[] sqlArray = sqlList.toArray(new String[0]);

        //宁夏机顶盒和光猫分库
        try {
            return jt.batchUpdate(sqlArray).length;
        } catch (DataAccessException e) {
            logger.warn("msg:" + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 激活任务
     */
    public int validTask(String taskId) {
        int i = 1;
        PrepareSQL sql;

        if (LipossGlobals.inArea(Global.HNLT)) {
            sql = new PrepareSQL();
            sql.append("select count(*) from stb_gw_strategy_soft ");
            sql.append("where service_id=5 and result_id=-1 ");
            sql.append("and task_id=? ");
            sql.setString(1, taskId);

            if (jt.queryForInt(sql.getSQL()) > 0) {
                sql = new PrepareSQL();
                sql.append("update stb_gw_strategy_soft set status=1 ");
                sql.append("where service_id=5 and result_id=-1 ");
                sql.append("and status=0 and task_id=? ");
                sql.setString(1, taskId);

                jt.update(sql.getSQL());

                sql = new PrepareSQL();
                sql.append("update stb_gw_strategy_soft_log set status=1 ");
                sql.append("where service_id=5 and result_id=-1 ");
                sql.append("and status=0 and task_id=? ");
                sql.setString(1, taskId);

                jt.update(sql.getSQL());
                i = 2;
            }
        }

        if (LipossGlobals.inArea(Global.SXLT)) {
            sql = new PrepareSQL();
            sql.append("update stb_gw_strategy_soft set status=0 where task_id=? and status=7");
            sql.setString(1, taskId);

            jt.update(sql.getSQL());

            sql = new PrepareSQL();
            sql.append("update stb_gw_strategy_soft_log set status=0 where task_id=? and status=7");
            sql.setString(1, taskId);

            jt.update(sql.getSQL());
            i = 1;
        }

        sql = new PrepareSQL(
                "update stb_gw_softup_task set valid=?,update_time=? where task_id=?");
        //江西itv激活任务，根据is_import_task判断是否为导入任务
        if (LipossGlobals.inArea(Global.JXDX)) {
            sql.append(" and is_import_task!=1");
        }
        sql.setInt(1, i);
        sql.setLong(2, System.currentTimeMillis() / 1000L);
        sql.setLong(3, StringUtil.getLongValue(taskId));
        return jt.update(sql.getSQL());
    }

    /**
     * 填充IP
     */
    private String getFillIP(String ip) {
        String fillIP = ip;
        String[] ipArray = new String[4];
        ipArray = ip.split("\\.");
        for (int i = 0; i < 4; i++) {
            if (ipArray[i].length() == 1) {
                ipArray[i] = "00" + ipArray[i];
            } else if (ipArray[i].length() == 2) {
                ipArray[i] = "0" + ipArray[i];
            }
        }
        fillIP = ipArray[0] + "." + ipArray[1] + "." + ipArray[2] + "." + ipArray[3];

        return fillIP;
    }

    /**
     * 插入导入任务
     */
    public void batchExcelUpdate(long userId, List<String> serList,
                                 long batchId, long addTime, Map<String, String> vendorMap,
                                 String taskDetail, String strategyName) {
        List<String> tempList = new ArrayList<String>();
        if (null != serList && serList.size() > 0) {
            // add by panyin for import softupdate task at 2011-04-21 -- begin
            PrepareSQL taskSql = new PrepareSQL(
                    "insert into stb_gw_softup_batch_task(task_id, add_time, account_id, task_status, mod_time,taskdetail,strategyname) values(?,?,?,?,?,?,?)");
            // long taskId = new Date().getTime()/1000L;
            taskSql.setLong(1, batchId);
            taskSql.setLong(2, addTime);
            taskSql.setLong(3, userId);
            taskSql.setLong(4, 1);
            taskSql.setLong(5, addTime);
            taskSql.setString(6, taskDetail);
            // add by zhangcong@ 2011-09-05
            // 需求单:JSDX_ITV_HTW-REQ-20110824-XMN-003
            taskSql.setString(7, strategyName);
            tempList.add(taskSql.getSQL());
            // add by panyin for import softupdate task at 2011-04-21 -- end

            final int size = serList.size();
            for (int i = 0; i < size; i++) {
                PrepareSQL sql = new PrepareSQL(
                        "insert into stb_gw_softup_batch(serv_account,id,add_time,acc_old,task_id) values(?,?,?,?,?)");
                sql.setString(1, serList.get(i));
                sql.setLong(2, batchId);
                sql.setLong(3, addTime);
                sql.setLong(4, userId);
                sql.setLong(5, batchId);
                tempList.add(sql.getSQL());
            }

            if (null != vendorMap && !vendorMap.isEmpty()) {
                Set<String> vendorSet = vendorMap.keySet();
                for (String s : vendorSet) {
                    String pathId = vendorMap.get(s);
                    PrepareSQL sql2 = new PrepareSQL(
                            "insert into stb_gw_softup_batch_version(path_id,batch_id,vendor_id) values(?,?,?)");
                    sql2.setLong(1, Integer.parseInt(pathId));
                    sql2.setLong(2, batchId);
                    sql2.setString(3, s);
                    tempList.add(sql2.getSQL());
                }
                String[] sqlArray = tempList.toArray(new String[0]);
                jt.batchUpdate(sqlArray);
            } else {
                logger.warn("未关联厂商和厂商所对应的版本");
            }
        } else {
            logger.warn("业务帐号集合为空");
        }
    }

    /**
     * 根据task_Id,结果查询升级记录
     */
    public List queryUpRecordByTaskId(String taskID, String result,
                                      String isAll, int startRow, int countRow, boolean split) {
        // TODO wait (more table related)
        String sqlStr = "select a.*,b.softwareversion as newsoftversion,b.device_model from (select t.result, t.start_time, t.dev_type_id_new, t.task_id, " +
                " d.serv_account,d.device_serialnumber,ov.softwareversion as oldsoftversion,"
                + "ven.vendor_add,d.vendor_id from stb_gw_softup_record t,stb_tab_gw_device d,stb_tab_devicetype_info ov,stb_tab_vendor ven "
                + "where d.device_id = t.device_id and t.dev_type_id_old = ov.devicetype_id and d.vendor_id=ven.vendor_id )a "
                + "left join (select nv.devicetype_id,nv.softwareversion,nm.device_model "
                + "from stb_tab_devicetype_info nv ,stb_gw_device_model nm where nv.device_model_id = nm.device_model_id )b  on a.dev_type_id_new = b.devicetype_id where a.task_id=?";
        PrepareSQL sql = null;
        logger.warn("result:[{}]", result);
        if (null != isAll && "1".equals(isAll.trim())) {
            sql = new PrepareSQL(sqlStr);
            sql.setLong(1, Long.parseLong(taskID));
        } else if ("12".equals(result)) {
            //山西联通统计 result =  1 or 2 的结果
            sqlStr += " and (a.result=? or a.result = ?)";
            sql = new PrepareSQL(sqlStr);
            sql.setLong(1, Long.parseLong(taskID));
            sql.setInt(2, 1);
            sql.setInt(3, 2);
        } else {
            sqlStr += " and a.result=?";
            sql = new PrepareSQL(sqlStr);
            sql.setLong(1, Long.parseLong(taskID));
            sql.setInt(2, Integer.parseInt(result.trim()));
        }
        return jt.querySP(sql.getSQL(), startRow, countRow, new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                String vendor_add = rs.getString("vendor_add");
                map.put("vendor_add", vendor_add);
                String device_model = rs.getString("device_model");
                map.put("device_model", device_model);
                String serv_account = rs.getString("serv_account");
                map.put("serv_account", serv_account);
                String device_serialnumber = rs.getString("device_serialnumber");
                map.put("device_serialnumber", device_serialnumber);
                String newsoftversion = rs.getString("newsoftversion");
                map.put("newsoftversion", newsoftversion);
                String oldsoftversion = rs.getString("oldsoftversion");
                map.put("oldsoftversion", oldsoftversion);
                String result = rs.getString("result");
                map.put("result", result);
                try {
                    if (null == rs.getString("start_time")
                            || "0".equals(rs.getString("start_time"))) {
                        map.put("start_time", "");
                    } else {
                        long start_time = StringUtil.getLongValue(rs.getString("start_time"));
                        DateTimeUtil dt = new DateTimeUtil(start_time * 1000);
                        map.put("start_time", dt.getLongDate());
                    }
                } catch (Exception e) {
                    map.put("start_time", "");
                }
                return map;
            }
        });
    }
    
    

    public int queryCountByTaskId(String taskID, String result, String isAll) {
        // TODO wait (more table related)
        String sqlStr = "select count(*) from (select t.dev_type_id_new, t.task_id,t.result from stb_gw_softup_record t,stb_tab_gw_device d,stb_tab_devicetype_info ov,stb_tab_vendor ven,stb_gw_device_model m "
                + "where d.device_id = t.device_id and t.dev_type_id_old = ov.devicetype_id and d.vendor_id=ven.vendor_id and ov.device_model_id=m.device_model_id)a "
                + "left join (select nv.devicetype_id,nv.softwareversion,nm.device_model "
                + "from stb_tab_devicetype_info nv ,stb_gw_device_model nm where nv.device_model_id = nm.device_model_id )b  on a.dev_type_id_new = b.devicetype_id where a.task_id=?";
        PrepareSQL sql = null;
        if (null != isAll && "1".equals(isAll.trim())) {
            sql = new PrepareSQL(sqlStr);
            sql.setLong(1, Long.parseLong(taskID));
        } else if ("12".equals(result)) {
            //山西联通统计 result =  1 or 2 的结果
            sqlStr += " and (a.result=? or a.result = ?)";
            sql = new PrepareSQL(sqlStr);
            sql.setLong(1, Long.parseLong(taskID));
            sql.setInt(2, 1);
            sql.setInt(3, 2);
        } else {
            sqlStr += " and a.result=?";
            sql = new PrepareSQL(sqlStr);
            sql.setLong(1, Long.parseLong(taskID));
            sql.setInt(2, Integer.parseInt(result.trim()));
        }
        return jt.queryForInt(sql.getSQL());
    }
    
    public int queryCountByTaskId_import(String taskID, String result, String importtype) {
    	StringBuffer sb = new StringBuffer(""); 
    	if(StringUtil.IsEmpty(result)){
    		//查询全部
    		if("1".equals(importtype)){
    			sb.append("select count(*) ");
        		sb.append("from stb_gw_softup_task_devsnheck a ");
        		sb.append("left join stb_gw_softup_record t on a.task_id=t.task_id, ");
        	    sb.append("stb_tab_gw_device d ");
        	    sb.append("where a.dev_sn=d.device_serialnumber and a.task_id=? "); 
    		}else{
    			sb.append("select  count(*) ");
        		sb.append("from stb_gw_softup_task_acccheck a ");
        		sb.append("left join stb_gw_softup_record t on a.task_id=t.task_id, ");
        	    sb.append("stb_tab_customer cus,stb_tab_gw_device d ");
        	    sb.append("where a.serv_account=cus.serv_account and cus.customer_id=d.customer_id and a.task_id=? "); 
    		}
    	}else{
    		
    		if("0".equals(result)){
    			sb.append("select count(*) ");
    			sb.append("from ");
    			if("1".equals(importtype)){
    				sb.append("stb_gw_softup_task_devsnheck a left join stb_gw_softup_record c on a.task_id=c.task_id,");
    				sb.append("stb_tab_gw_device d where a.dev_sn=d.device_serialnumber ");
    			}else{
    				sb.append("stb_gw_softup_task_acccheck a left join stb_gw_softup_record c on a.task_id=c.task_id,");
    				sb.append("stb_tab_customer b,stb_tab_gw_device d ");
    				sb.append("where a.serv_account=b.serv_account and b.customer_id=d.customer_id ");
    			}
    			sb.append("and not exists ");
    			sb.append("(select 1 from stb_gw_softup_record b where b.device_id=d.device_id and (b.result <>1 and b.result <>-1)) ");
    			sb.append("and a.task_id=?");
    		}else{
    			sb.append("select count(*) ");
        		sb.append("from stb_gw_softup_record t, stb_tab_gw_device d ");
        	    sb.append("where d.device_id = t.device_id and t.task_id=? ");
        	    if (!StringUtil.IsEmpty(result)){
        			sb.append(" and t.result="+result.trim());
    	        } 
    		}
    	}
    	
    	PrepareSQL pSQL = new PrepareSQL(sb.toString());
    	pSQL.setLong(1, StringUtil.getLongValue(taskID));
        return jt.queryForInt(pSQL.getSQL());
    }


    /**
     * 根据task_Id,结果查询升级记录
     */
    public List queryUpRecordByTaskIdHnlt(String taskID, String result,
                                      String isAll,String cityId, int startRow, int countRow, boolean split) {
        // TODO wait (more table related)
        String sqlStr = "select e.serv_account,a.cpe_mac,a.device_serialnumber,b.vendor_add,c.device_model,d.hardwareversion," +
                "d.softwareversion,d.epg_version,g.network_type,g.addressing_type,g.apk_version_name,g.category," +
                "a.cpe_currentupdatetime,f.city_name" +
                "  from stb_tab_gw_device a " +
                " left join stb_tab_vendor b on a.vendor_id = b.vendor_id  " +
                " left join stb_gw_device_model c on a.device_model_id = c.device_model_id" +
                " left join stb_tab_devicetype_info d on a.devicetype_id = d.devicetype_id  " +
                " left join stb_tab_customer e on a.customer_id = e.customer_id  " +
                " left join tab_city f on a.city_id = f.city_id " +
                " left join stb_dev_supplement g on a.device_id = g.device_id " +
                " where a.device_id in (" +
                " select device_id " +
                " from stb_gw_strategy_soft_log " +
                " where service_id=5 and task_id=?  " ;
        PrepareSQL sql = null;
        if (null != isAll && "1".equals(isAll.trim())) {
            sqlStr += " ) ";
            if(null != cityId && !StringUtil.IsEmpty(cityId) && !"-1".equals(cityId))
            {
                if(!"00".equals(cityId))
                {
                    sqlStr += " and (f.city_id = ? or f.parent_id = ? ) " ;
                }
                else
                {
                    sqlStr += " and f.city_id = ?   " ;
                }
            }
            sql = new PrepareSQL(sqlStr);
            sql.setLong(1, Long.parseLong(taskID));
            sql.setString(2, cityId);
            sql.setString(3, cityId);
        }
        else
        {
            sqlStr += " and result_id=?) ";
            if(null != cityId && !StringUtil.IsEmpty(cityId) && !"-1".equals(cityId))
            {
                if(!"00".equals(cityId))
                {
                    sqlStr += " and (f.city_id = ? or f.parent_id = ? ) " ;
                }
                else
                {
                    sqlStr += " and f.city_id = ?  " ;
                }
            }
            sql = new PrepareSQL(sqlStr);
            sql.setLong(1, Long.parseLong(taskID));
            sql.setInt(2, Integer.parseInt(result.trim()));
            sql.setString(3, cityId);
            sql.setString(4, cityId);
        }



        List<Map> resultList = jt.querySP(sql.getSQL(), startRow, countRow);
        for (Map resultMap:resultList) {
            long cpeCurrentupdatetime = StringUtil.getLongValue(resultMap.get("cpe_currentupdatetime"));
            DateTimeUtil dt = new DateTimeUtil(cpeCurrentupdatetime * 1000);
            resultMap.put("cpe_currentupdatetime", dt.getLongDate());
        }
        return resultList;
    }


    public int queryCountByTaskIdHnlt(String taskID, String result, String isAll,String cityId) {
        // TODO wait (more table related)
        String sqlStr = "select count(*) from ( select e.serv_account,a.cpe_mac,a.device_serialnumber,b.vendor_add,c.device_model,d.hardwareversion," +
                "d.softwareversion,d.epg_version,g.network_type,g.addressing_type,g.apk_version_name,g.category," +
                "a.cpe_currentupdatetime " +
                "  from stb_tab_gw_device a " +
                " left join stb_tab_vendor b on a.vendor_id = b.vendor_id  " +
                " left join stb_gw_device_model c on a.device_model_id = c.device_model_id" +
                " left join stb_tab_devicetype_info d on a.devicetype_id = d.devicetype_id  " +
                " left join stb_tab_customer e on a.customer_id = e.customer_id  " +
                " left join tab_city f on a.city_id = f.city_id" +
                 " left join stb_dev_supplement g on a.device_id = g.device_id " +
                " where a.device_id in (" +
                " select device_id " +
                " from stb_gw_strategy_soft_log " +
                " where service_id=5 and task_id=?  " ;
        PrepareSQL sql = null;
        if (null != isAll && "1".equals(isAll.trim())) {
            sqlStr += "  ) ";
            if(null != cityId && !StringUtil.IsEmpty(cityId) && !"-1".equals(cityId))
            {
                if(!"00".equals(cityId))
                {
                    sqlStr += " and (f.city_id = ? or f.parent_id = ?) )" ;
                }
                else
                {
                    sqlStr += " and f.city_id = ? )" ;
                }

            }
            else
            {
                sqlStr += " )  ";
            }
            sql = new PrepareSQL(sqlStr);
            sql.setLong(1, Long.parseLong(taskID));
            sql.setString(2, cityId);
            sql.setString(3, cityId);
        }
        else
        {
            sqlStr += " and result_id =? )  ";
            if(null != cityId && !StringUtil.IsEmpty(cityId) && !"-1".equals(cityId))
            {
                if(!"00".equals(cityId))
                {
                    sqlStr += " and (f.city_id = ? or f.parent_id = ?) )" ;
                }
                else
                {
                    sqlStr += " and f.city_id = ? )" ;
                }
            }
            else
            {
                sqlStr += " )  ";
            }
            sql = new PrepareSQL(sqlStr);
            sql.setLong(1, Long.parseLong(taskID));
            sql.setInt(2, Integer.parseInt(result.trim()));
            sql.setString(3, cityId);
            sql.setString(4, cityId);
        }
        return jt.queryForInt(sql.getSQL());
    }


    /**
     * 根据task_Id,导出升级记录
     */
    public List exportUpRecordByTaskIdHnlt(String taskID, String result, String isAll,String cityId) {
        // TODO wait (more table related)
        String sqlStr = "select e.serv_account,a.cpe_mac,a.device_serialnumber,b.vendor_add,c.device_model,d.hardwareversion," +
                "d.softwareversion,d.epg_version,g.network_type,g.addressing_type,g.apk_version_name,g.category," +
                "a.cpe_currentupdatetime,f.city_name" +
                "  from stb_tab_gw_device a " +
                " left join stb_tab_vendor b on a.vendor_id = b.vendor_id  " +
                " left join stb_gw_device_model c on a.device_model_id = c.device_model_id" +
                " left join stb_tab_devicetype_info d on a.devicetype_id = d.devicetype_id  " +
                " left join stb_tab_customer e on a.customer_id = e.customer_id  " +
                " left join tab_city f on a.city_id = f.city_id " +
                " left join stb_dev_supplement g on a.device_id = g.device_id " +
                " where a.device_id in (" +
                " select device_id " +
                " from stb_gw_strategy_soft_log " +
                " where service_id=5 and task_id=?  " ;
        PrepareSQL sql = null;
        if (null != isAll && "1".equals(isAll.trim())) {
            sqlStr += " ) ";
            if(null != cityId && !StringUtil.IsEmpty(cityId) && !"-1".equals(cityId))
            {
                if(!"00".equals(cityId))
                {
                    sqlStr += " and (f.city_id = ? or f.parent_id = ? ) " ;
                }
                else
                {
                    sqlStr += " and f.city_id = ?  " ;
                }

            }
            sql = new PrepareSQL(sqlStr);
            sql.setLong(1, Long.parseLong(taskID));
            sql.setString(2, cityId);
            sql.setString(3, cityId);
        }
        else
        {
            sqlStr += " and result_id=?) ";
            if(null != cityId && !StringUtil.IsEmpty(cityId) && !"-1".equals(cityId))
            {
                if(!"00".equals(cityId))
                {
                    sqlStr += " and (f.city_id = ? or f.parent_id = ? ) " ;
                }
                else
                {
                    sqlStr += " and f.city_id = ?   " ;
                }
            }
            sql = new PrepareSQL(sqlStr);
            sql.setLong(1, Long.parseLong(taskID));
            sql.setInt(2, Integer.parseInt(result.trim()));
            sql.setString(3, cityId);
            sql.setString(4, cityId);
        }



        List<Map> resultList = jt.queryForList(sql.getSQL());
        for (Map resultMap:resultList) {
            long cpeCurrentupdatetime = StringUtil.getLongValue(resultMap.get("cpe_currentupdatetime"));
            DateTimeUtil dt = new DateTimeUtil(cpeCurrentupdatetime * 1000);
            resultMap.put("cpe_currentupdatetime", dt.getLongDate());
            String category = StringUtil.getStringValue(resultMap.get("category"));
            if("0".equals(category))
            {
                resultMap.put("category","行");
            }
            else if("1".equals(category))
            {
                resultMap.put("category","串");
            }
            else
            {
                resultMap.put("category","未知");
            }
        }
        return resultList;
    }


    /**
     * 查询策略下详细信息
     */
    public Map queryTaskById(String taskId,String taskType) {
        Map map = new HashMap();
        String table = "stb_gw_version_file_path ";
        if (LipossGlobals.inArea(Global.HNLT)) {
            table = "stb_soft_version_path ";
        }

        PrepareSQL sql = new PrepareSQL();
        if (LipossGlobals.inArea(Global.XJDX)) {// TODO wait (more table related)
            sql.append(" select t.record_time,t.task_detail_desc,t.task_desc,t.valid,f.version_path,v.vendor_add,c.city_name ");
            sql.append(" from stb_gw_softup_task t ");
            sql.append("  inner join " + table + "  f  on t.version_path_id=f.id  ");
            sql.append(" left join stb_tab_vendor v on t.vendor_id=v.vendor_id ");
            sql.append(" left join tab_city c on  t.city_id=c.city_id ");
            sql.append(" where  t.task_id=? ");
        } else if (LipossGlobals.inArea(Global.HNLT)) {// TODO wait (more table related)
            sql.append("select t.record_time,t.task_detail_desc,t.task_desc,t.valid,f.version_path,v.vendor_add,c.city_name ");
            sql.append("from stb_gw_softup_task t," + table + "f,stb_tab_vendor v,tab_city c ");
            sql.append("where t.version_path_id=f.id and t.vendor_id=v.vendor_id and t.city_id=c.city_id ");
            sql.append("and t.task_id=? ");
        } else if(LipossGlobals.inArea(Global.SXLT)){// TODO wait (more table related)
        	if(!StringUtil.IsEmpty(taskType) && "1".equals(taskType)){
        		sql.append("select t.record_time,t.task_detail_desc,t.task_desc,t.valid,f.version_path,v.vendor_add ");
                sql.append("from stb_gw_softup_task t," + table + "f,stb_tab_vendor v,tab_city c ");
                sql.append("where t.version_path_id=f.id and t.vendor_id=v.vendor_id ");
                sql.append("and t.task_id=? ");
        	}else{
        		/*sql.append("select t.record_time,t.task_detail_desc,t.task_desc,t.valid,f.version_path,v.vendor_add,c.city_name ");
                sql.append("from stb_gw_softup_task t," + table + "f,stb_tab_vendor v,tab_city c ");
                sql.append("where t.version_path_id=f.id and t.vendor_id=v.vendor_id and t.city_id=c.city_id ");
                sql.append("and t.task_id=? ");*/
        		sql.append("select t.record_time,t.task_detail_desc,t.task_desc,t.valid,f.version_path,v.vendor_add,t.city_id ");
                sql.append("from stb_gw_softup_task t," + table + "f,stb_tab_vendor v ");
                sql.append("where t.version_path_id=f.id and t.vendor_id=v.vendor_id ");
                sql.append("and t.task_id=? ");
        	}
            
        }else{
        	 sql.append("select t.record_time,t.task_detail_desc,t.task_desc,t.valid,f.version_path,v.vendor_add,c.city_name ");
             sql.append("from stb_gw_softup_task t," + table + "f,stb_tab_vendor v,tab_city c ");
             sql.append("where t.version_path_id=f.id and t.vendor_id=v.vendor_id and t.city_id=c.city_id ");
             sql.append("and t.task_id=? ");
        }
        sql.setLong(1, Long.parseLong(taskId));
        List list = jt.queryForList(sql.getSQL());
        if (list != null && list.size() > 0) {
            map = (Map) list.get(0);
        }
        
        if(LipossGlobals.inArea(Global.SXLT) && (StringUtil.IsEmpty(taskType) || !"1".equals(taskType))){
        	String cityId = StringUtil.getStringValue(map.get("city_id"));
        	String cityNames = "";
        	if(!StringUtil.IsEmpty(cityId)){
        		cityMap = CityDAO.getCityIdCityNameMap();
        		String[] cityIdArr = cityId.split(",");
        	    for(String arr : cityIdArr){
        	    	if(!StringUtil.IsEmpty(arr)){
        	    		cityNames = cityNames + " , "+ cityMap.get(arr);
        	    	}
        	    }
        	    map.put("city_name",cityNames);
        	}
        }
        return map;
    }

    /**
     * 查询策略下的对应IP地址段
     */
    public List queryIPDuanById(String taskId) {
        String sql = "select start_ip, end_ip from stb_gw_softup_task_ipcheck where task_id=?";
        PrepareSQL sQL = new PrepareSQL(sql);
        sQL.setLong(1, Long.parseLong(taskId));
        return jt.queryForList(sQL.getSQL());
    }

    public List queryMACById(String taskId) {
        String sql = "select start_mac, end_mac from stb_gw_softup_task_maccheck where task_id=?";
        PrepareSQL sQL = new PrepareSQL(sql);
        sQL.setLong(1, Long.parseLong(taskId));
        return jt.queryForList(sQL.getSQL());
    }

    /**
     * 查询策略适应的软硬件型号
     */
    public List queryTaskHardSoftById(String taskId) {// TODO wait (more table related)
        String SQL = "select t.task_id,m.device_model_id,m.softwareversion,mm.device_model,mm.device_model, m.softwareversion as hardSoftV " +
                " from stb_gw_softup_task t, stb_gw_softup_task_devtype td, stb_tab_devicetype_info m, stb_gw_device_model mm " +
                " where t.task_id = td.task_id and td.devicetype_id = m.devicetype_id and m.device_model_id = mm.device_model_id and t.task_id=? " +
                " order by mm.device_model";
        PrepareSQL sQL = new PrepareSQL(SQL);
        sQL.setLong(1, Long.parseLong(taskId));
        List<Map> list = jt.queryForList(sQL.getSQL());
        if(list == null || list.isEmpty()){
            return null;
        }
        for (Map map : list) {
            String device_model = StringUtil.getStringValue(map, "device_model");
            String softwareversion = StringUtil.getStringValue(map, "softwareversion");
            map.put("hardSoftV", device_model + "(==" + softwareversion + "==)");
        }
        return list;
    }

    public List queryBatchExcel(String startTime, String endTime,
                                String servAccount, int startRow, int countRow, boolean split) {
        startTime = getTime(startTime) + "";
        endTime = getTime(endTime) + "";
        // TODO wait (more table related)
        String sql = "select b.serv_account as servAccount,b.id as batchid,b.add_time,b.acc_old,a.* from stb_gw_softup_batch b left join "
                + "(select v.vendor_add,d.device_serialnumber,d.serv_account,r.result,r.dev_type_id_old,r.dev_type_id_new,r.start_time,r.end_time,r.batch_id,bv.path_id,p.version_path from stb_tab_gw_device d,stb_gw_softup_record r,"
                + "stb_gw_softup_batch_version bv,stb_gw_version_file_path p,stb_tab_vendor v where d.device_id=r.device_id and d.vendor_id=bv.vendor_id and bv.batch_id=r.batch_id and p.id=bv.path_id and bv.vendor_id=v.vendor_id)"
                + " a on b.serv_account=a.serv_account and b.id=a.batch_id where b.add_time >=? and b.add_time <=? ";
        if (null != servAccount && !"".equals(servAccount.trim())) {
            sql += "and b.serv_account like '%" + servAccount + "%'";
        }
        PrepareSQL SQL = new PrepareSQL(sql);
        SQL.setLong(1, Long.parseLong(startTime));
        SQL.setLong(2, Long.parseLong(endTime));
        if (null != servAccount && !"".equals(servAccount.trim())) {
            SQL.setString(3, servAccount);
        }
        // 处理分页
        return jt.querySP(SQL.getSQL(), startRow, countRow, new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                String servAccount = rs.getString("servAccount");
                map.put("servAccount", servAccount);
                try {
                    long add_time = StringUtil.getLongValue(rs.getString("add_time"));
                    DateTimeUtil dt = new DateTimeUtil(add_time * 1000);
                    map.put("add_time", dt.getLongDate());
                } catch (Exception e) {
                    map.put("add_time", "");
                }
                map.put("vendor_add", rs.getString("vendor_add"));
                map.put("device_serialnumber", rs.getString("device_serialnumber"));
                map.put("result", rs.getInt("result") + "");
                try {
                    String temp = rs.getString("end_time");
                    if (null == temp || "0".equals(temp)) {
                        map.put("end_time", "");
                    } else {
                        long end_time = StringUtil.getLongValue(rs.getString("end_time"));
                        DateTimeUtil dt = new DateTimeUtil(end_time * 1000);
                        map.put("end_time", dt.getLongDate());
                    }
                } catch (Exception e) {
                    map.put("end_time", "");
                }
                map.put("version_path", rs.getString("version_path"));
                return map;
            }
        });
    }

    public int getBatchExcelRecord(String startTime, String endTime, String servAccount) {
        startTime = getTime(startTime) + "";
        endTime = getTime(endTime) + "";
        // TODO wait (more table related)
        String sql = "select count(*) from stb_gw_softup_batch b left join "
                + "(select v.vendor_add,d.device_serialnumber,d.serv_account,r.result,r.dev_type_id_old,r.dev_type_id_new,r.start_time,r.end_time,r.batch_id,bv.path_id,p.version_path from stb_tab_gw_device d,stb_gw_softup_record r,"
                + "stb_gw_softup_batch_version bv,stb_gw_version_file_path p,stb_tab_vendor v where d.device_id=r.device_id and d.vendor_id=bv.vendor_id and bv.batch_id=r.batch_id and p.id=bv.path_id and bv.vendor_id=v.vendor_id)"
                + " a on b.serv_account=a.serv_account and b.id=a.batch_id where b.add_time >=? and b.add_time <=? ";
        if (null != servAccount && !"".equals(servAccount.trim())) {
            sql += "and b.serv_account like '%" + servAccount + "%'";
        }
        PrepareSQL SQL = new PrepareSQL(sql);
        SQL.setLong(1, Long.parseLong(startTime));
        SQL.setLong(2, Long.parseLong(endTime));
        if (null != servAccount && !"".equals(servAccount.trim())) {
            SQL.setString(3, servAccount);
        }
        return jt.queryForInt(SQL.getSQL());
    }

    private long getTime(String date) {
        DateTimeUtil dt = new DateTimeUtil(date);
        return dt.getLongTime();
    }

    /**
     * 查询所有任务
     */
    public List getExcelBatchSoftupTask(int curPage_splitPage,
                                        int num_splitPage, String queryVaild, String startTime, String endTime, String cityId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select st.account_id, st.task_id, st.strategyname, st.add_time, st.mod_time, st.task_status, st.load_status, st.taskdetail, " +
                "c.acc_loginname from stb_gw_softup_batch_task st,tab_accounts c where st.account_id=c.acc_oid ");
        if (!StringUtil.IsEmpty(queryVaild)
                && !"-1".equals(queryVaild)) {
            sql.append("  and st.task_status=").append(queryVaild);
        }
        if (!StringUtil.IsEmpty(startTime)) {
            sql.append("  and st.add_time>=").append(getTime(startTime));
        }
        if (!StringUtil.IsEmpty(endTime)) {
            sql.append(" and st.add_time<=").append(getTime(endTime));
        }
        // 属地
        if (!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)
                && !"00".equals(cityId)) {

            List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
            sql.append("and  st.city_id in (" + StringUtils.weave(list) + ")");
            list = null;
        }
        sql.append(" order by mod_time desc");
        PrepareSQL psql = new PrepareSQL(sql.toString());
        // vendorMap = GwVendorModelVersionDAO.getVendorMap();
        List list = jt.querySP(psql.getSQL(), (curPage_splitPage - 1)
                * num_splitPage + 1, num_splitPage, new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                // map.put("vendor_add", vendorMap.get(vendor_id));
                map.put("acc_loginname", rs.getString("acc_loginname"));
                map.put("account_id", rs.getString("account_id"));
                map.put("task_id", rs.getString("task_id"));
                map.put("strategyName", StringUtil.getStringValue(rs.getString("strategyname")));
                try {
                    long add_time = StringUtil.getLongValue(rs.getString("add_time"));
                    DateTimeUtil dt = new DateTimeUtil(add_time * 1000);
                    map.put("add_time", dt.getLongDate());
                } catch (Exception e) {
                    map.put("add_time", "");
                }
                try {
                    long mod_time = StringUtil.getLongValue(rs.getString("mod_time"));
                    DateTimeUtil dt = new DateTimeUtil(mod_time * 1000);
                    map.put("mod_time", dt.getLongDate());
                } catch (Exception e) {
                    map.put("mod_time", "");
                }
                map.put("task_status", rs.getString("task_status"));
                map.put("load_status", rs.getString("load_status"));
                map.put("taskdetail", rs.getString("taskdetail"));
                return map;
            }
        });
        return list;
    }

    public int countExcelBatchSoftupTask(int curPage_splitPage,
                                         int num_splitPage, String queryVaild, String startTime,
                                         String endTime, String cityId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) from stb_gw_softup_batch_task st,tab_accounts c where st.account_id=c.acc_oid ");
        if (!StringUtil.IsEmpty(queryVaild)
                && !"-1".equals(queryVaild)) {
            sql.append(" and st.task_status=").append(queryVaild);
        }
        if (!StringUtil.IsEmpty(startTime)) {
            sql.append(" and st.add_time>=").append(getTime(startTime));
        }
        if (!StringUtil.IsEmpty(endTime)) {
            sql.append(" and st.add_time<=").append(getTime(endTime));
        }
        // 属地
        if (!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)
                && !"00".equals(cityId)) {

            List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
            sql.append("and  st.city_id in (" + StringUtils.weave(list) + ")");
            list = null;
        }
        PrepareSQL psql = new PrepareSQL(sql.toString());
        int total = jt.queryForInt(psql.getSQL());
        int maxPage = 1;
        if (total % num_splitPage == 0) {
            maxPage = total / num_splitPage;
        } else {
            maxPage = total / num_splitPage + 1;
        }
        return maxPage;
    }

    /**
     * 取消任务
     */
    public int cancerExcelBatchTask(String taskId) {
        PrepareSQL sql = new PrepareSQL(
                "update stb_gw_softup_batch_task set task_status=0,mod_time=? where task_id=?");
        sql.setLong(1, StringUtil.getLongValue(new Date().getTime() / 1000L));
        sql.setLong(2, StringUtil.getLongValue(taskId));
        return jt.update(sql.getSQL());
    }

    /**
     * 激活任务
     */
    public int validExcelBatchTask(String taskId) {
        PrepareSQL sql = new PrepareSQL(
                "update stb_gw_softup_batch_task set task_status=1,mod_time=? where task_id=?");
        sql.setLong(1, StringUtil.getLongValue(new Date().getTime() / 1000L));
        sql.setLong(2, StringUtil.getLongValue(taskId));
        return jt.update(sql.getSQL());
    }

    public List getBatchExcelSTS(String taskID, String cityId) {
    	Connection con=null;
    	Statement st=null;
    	ResultSet rs=null;
    	try {
        	// TODO wait (more table related)
            String sql = " select count(*) as softupsum, a.result  ";
            sql += " from stb_gw_softup_record a,stb_tab_gw_device b, stb_tab_customer c  where 1=1 and a.batch_id="
                    + taskID;
            sql += "  and a.device_id=b.device_id  and a.serv_account=b.serv_account  and a.serv_account=c.serv_account group by a.result ";
            logger.warn(sql);

            con = jt.getDataSource().getConnection();
            st = con.createStatement();

            PrepareSQL psql = new PrepareSQL(sql);

            rs = st.executeQuery(psql.getSQL());
            List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
            while (rs.next()) {
                Map<String, String> map = new HashMap<String, String>();
                int sum = rs.getInt("softupsum");
                int result = rs.getInt("result");
                map.put("softupsum", String.valueOf(sum));
                map.put("result", String.valueOf(result));
                returnList.add(map);
            }
            // logger.warn(sql3);
            // st.execute(sql3);
            
            return returnList;
        } catch (SQLException e) {
            logger.warn("查询异常", e);
            return null;
        }finally{
        	try {
        		if(rs!=null){
        			rs.close();
        			rs=null;
        		}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	try {
        		if(st!=null){
        			st.close();
        			st=null;
        		}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	try {
        		if(con!=null){
        			con.close();
        			con=null;
        		}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        // return jt.queryForList(SQL.getSQL());
    }

    /**
     * 查询设备详细
     */
    public List queryBatchExcelList(String taskID, final String result,
                                    int startRow, int countRow, boolean split) {
        // TODO wait (more table related)
        String sql = "select b.serv_account, b.vendor_id, b.device_serialnumber, a.dev_type_id_old, a.dev_type_id_new, " +
                "c.user_grp, b.cpe_currentupdatetime, b.device_model_id " +
            "from stb_gw_softup_record a, stb_tab_gw_device b, stb_tab_customer c ";
        sql += " where a.batch_id=? and a.device_id=b.device_id ";
        sql += "  and a.serv_account=b.serv_account";
        sql += "  and a.serv_account=c.serv_account ";
        sql += "  and a.result=?";
        PrepareSQL SQL = new PrepareSQL(sql);
        SQL.setLong(1, Long.parseLong(taskID));
        SQL.setInt(2, Integer.parseInt(result));
        vendorMap = GwStbVendorModelVersionDAO.getVendorMap();
        deviceTypeMap = GwStbVendorModelVersionDAO.getDevicetypeMap();
        deviceModelMap = GwStbVendorModelVersionDAO.getDeviceModelMap();
        userTypeMap = getUserTypeMap();
        // 处理分页
        return jt.querySP(SQL.getSQL(), startRow, countRow, new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                String servAccount = rs.getString("serv_account");
                map.put("serv_account", servAccount);
                String vendorId = StringUtil.getStringValue(rs.getString("vendor_id"));
                map.put("vendor_add", vendorMap.get(vendorId));
                map.put("device_serialnumber", rs.getString("device_serialnumber"));
                map.put("result", result);

                String dev_type_id_old = StringUtil.getStringValue(rs.getString("dev_type_id_old"));
                map.put("oldsoftversion", deviceTypeMap.get(dev_type_id_old));

                String dev_type_id_new = StringUtil.getStringValue(rs.getString("dev_type_id_new"));
                map.put("newsoftversion", deviceTypeMap.get(dev_type_id_new));
                String type = StringUtil.getStringValue(rs.getString("user_grp"));
                map.put("group_type", userTypeMap.get(type));
                try {
                    long st_time = StringUtil.getLongValue(rs.getString("cpe_currentupdatetime"));
                    if (0 == st_time) {
                        map.put("start_time", "");
                    } else {
                        DateTimeUtil dt = new DateTimeUtil(st_time * 1000);
                        map.put("start_time", dt.getLongDate());
                    }
                } catch (Exception e) {
                    map.put("start_time", "");
                }
                String deviceMoelId = StringUtil.getStringValue(rs.getString("device_model_id"));
                map.put("device_model", deviceModelMap.get(deviceMoelId));
                return map;
            }
        });
    }

    /**
     * 导出查看查看结果设备详细信息
     */
    public List exportBatchExcelList(String taskID, String result) {
        List reList = new ArrayList();
        // TODO wait (more table related)
        String sql = "select b.serv_account, b.vendor_id, b.device_serialnumber, a.dev_type_id_old, a.dev_type_id_new, " +
                "c.user_grp, b.cpe_currentupdatetime, b.device_model_id " +
            "from stb_gw_softup_record a,stb_tab_gw_device b, stb_tab_customer c ";
        sql += " where a.batch_id=? and a.device_id=b.device_id ";
        sql += "  and a.serv_account=b.serv_account";
        sql += "  and a.serv_account=c.serv_account ";
        sql += "  and a.result=?";
        PrepareSQL SQL = new PrepareSQL(sql);
        SQL.setLong(1, Long.parseLong(taskID));
        SQL.setInt(2, Integer.parseInt(result));
        List list = jt.queryForList(SQL.getSQL());
        vendorMap = GwStbVendorModelVersionDAO.getVendorMap();
        deviceTypeMap = GwStbVendorModelVersionDAO.getDevicetypeMap();
        deviceModelMap = GwStbVendorModelVersionDAO.getDeviceModelMap();
        userTypeMap = getUserTypeMap();
        if (null != list && !list.isEmpty()) {
            Map reMap = null;
            Map tempMap = null;
            for (int i = 0; i < list.size(); i++) {
                reMap = new HashMap();
                tempMap = (Map) list.get(i);
                reMap.put("serv_account", tempMap.get("serv_account"));
                String vendorId = StringUtil.getStringValue(tempMap.get("vendor_id"));
                reMap.put("vendor_add", vendorMap.get(vendorId));
                reMap.put("device_serialnumber", tempMap.get("device_serialnumber"));

                String dev_type_id_old = StringUtil.getStringValue(tempMap.get("dev_type_id_old"));
                reMap.put("oldsoftversion", deviceTypeMap.get(dev_type_id_old));
                String dev_type_id_new = StringUtil.getStringValue(tempMap.get("dev_type_id_new"));
                reMap.put("newsoftversion", deviceTypeMap.get(dev_type_id_new));
                String type = StringUtil.getStringValue(tempMap.get("user_grp"));
                reMap.put("group_type", userTypeMap.get(type));
                String deviceMoelId = StringUtil.getStringValue(tempMap.get("device_model_id"));
                reMap.put("device_model", deviceModelMap.get(deviceMoelId));
                try {
                    long st_time = StringUtil.getLongValue(tempMap.get("cpe_currentupdatetime"));
                    if (0 == st_time) {
                        reMap.put("start_time", "");
                    } else {
                        DateTimeUtil dt = new DateTimeUtil(st_time * 1000);
                        reMap.put("start_time", dt.getLongDate());
                    }
                } catch (Exception e) {
                    reMap.put("start_time", "");
                }
                String jieguo = "";
                int key = StringUtil.getIntegerValue(result);
                switch (key) {
                    case 0:
                        jieguo = "未操作";
                        break;
                    case 1:
                        jieguo = "更新服务器地址修改成功";
                        break;
                    case 2:
                        jieguo = "软件升级成功";
                        break;
                    case -1:
                        jieguo = "更新服务器地址修改失败";
                        break;
                    default:
                        break;
                }
                reMap.put("result", jieguo);
                reList.add(reMap);
            }
        }
        return reList;
    }

    public int getBatchExcelList(String taskID, String result) {
        // TODO wait (more table related)
        String sql = "select count(*) from stb_gw_softup_record a,stb_tab_gw_device b, stb_tab_customer c ";
        sql += " where a.batch_id=? and a.device_id=b.device_id ";
        sql += "  and a.serv_account=b.serv_account";
        sql += "  and a.serv_account=c.serv_account ";
        sql += "  and a.result=?";
        PrepareSQL SQL = new PrepareSQL(sql);
        SQL.setLong(1, Long.parseLong(taskID));
        SQL.setInt(2, Integer.parseInt(result));
        return jt.queryForInt(SQL.getSQL());
    }

    public Map getBatchExcelDetail(String taskID) {
        Map returnMap = new HashMap();
        Map taskMap = null;
        String sql = "select t.task_id, t.add_time, t.account_id, t.task_status, t.mod_time, t.taskdetail, t.strategyname, " +
                "c.acc_loginname from stb_gw_softup_batch_task t,tab_accounts c where t.account_id=c.acc_oid and t.task_id=?";
        PrepareSQL SQL = new PrepareSQL(sql);
        SQL.setLong(1, Long.parseLong(taskID));
        taskMap = jt.queryForMap(SQL.getSQL());
        if (null != taskMap) {
            if (null != taskMap.get("add_time")
                    && !"0".equals(taskMap.get("add_time").toString())) {
                long st_time = StringUtil.getLongValue(taskMap.get("add_time").toString());
                DateTimeUtil dt = new DateTimeUtil(st_time * 1000);
                taskMap.put("add_time", dt.getLongDate());
            } else {
                taskMap.put("add_time", "");
            }
        }
        List vendorList = null;
        // TODO wait (more table related)
        String sql2 = "select bv.path_id,bv.batch_id,bv.vendor_id,bv.device_model_id" +
            "p.id,p.vendor_id,p.softwareversion,p.version_desc,p.version_path,p.record_time,p.update_time,p.acc_oid,p.valid,p.version_type," +
            "v.vendor_add,gdm.device_model " +
        "from stb_gw_softup_batch_version bv, stb_gw_version_file_path p, stb_tab_vendor v, stb_gw_device_model gdm " +
        " where p.id=bv.path_id and bv.vendor_id=p.vendor_id and bv.vendor_id=v.vendor_id and bv.device_model_id = gdm.device_model_id and bv.batch_id=?";
        SQL.setSQL(sql2);
        SQL.setLong(1, Long.parseLong(taskID));
        vendorList = jt.queryForList(SQL.getSQL());
        returnMap.put("task", taskMap);
        returnMap.put("vendorList", vendorList);

        return returnMap;
    }

    public String weave(List list) {
        StringBuffer sb = new StringBuffer(100);
        if (list.size() != 0) {
            sb.append("'").append(list.get(0)).append("'");
            for (int i = 1; i < list.size(); i++) {
                sb.append(",'").append(list.get(i)).append("'");
            }
        }

        return sb.toString();
    }

    /**
     * 插入导入任务
     */
    public void batchImportUp(long userId, List<String> serList, long batchId,
                              long addTime, String vendorId, String pathId, String taskDetail,
                              String strategyName, String deviceModelIds) {
        List<String> tempList = new ArrayList<String>();
        if (null != serList && serList.size() > 0) {
            // add by panyin for import softupdate task at 2011-04-21 -- begin
            PrepareSQL taskSql = new PrepareSQL(
                    "insert into stb_gw_softup_batch_task(task_id, add_time, account_id, task_status, mod_time,taskdetail,strategyname) values(?,?,?,?,?,?,?)");
            // long taskId = new Date().getTime()/1000L;
            taskSql.setLong(1, batchId);
            taskSql.setLong(2, addTime);
            taskSql.setLong(3, userId);
            taskSql.setLong(4, 1);
            taskSql.setLong(5, addTime);
            taskSql.setString(6, taskDetail);
            taskSql.setString(7, strategyName);
            tempList.add(taskSql.getSQL());

            final int size = serList.size();
            for (int i = 0; i < size; i++) {
                PrepareSQL sql = new PrepareSQL(
                        "insert into stb_gw_softup_batch(serv_account,id,add_time,acc_old,task_id) values(?,?,?,?,?)");
                sql.setString(1, serList.get(i));
                sql.setLong(2, batchId);
                sql.setLong(3, addTime);
                sql.setLong(4, userId);
                sql.setLong(5, batchId);
                tempList.add(sql.getSQL());
            }

            String[] deviceModelArray = deviceModelIds.split(",");
            for (String devModId : deviceModelArray) {
                PrepareSQL sql2 = new PrepareSQL(
                        "insert into stb_gw_softup_batch_version(path_id,batch_id,vendor_id,device_model_id) values(?,?,?,?)");
                sql2.setLong(1, Integer.parseInt(pathId));
                sql2.setLong(2, batchId);
                sql2.setString(3, vendorId);
                sql2.setString(4, devModId);
                tempList.add(sql2.getSQL());
            }

            String[] sqlArray = tempList.toArray(new String[0]);
            int[] ier = jt.batchUpdate(sqlArray);
            if (ier != null && ier.length > 0) {
                logger.debug("版本记录入库：  成功");
            } else {
                logger.debug("版本记录入库：  失败");
            }
        } else {
            logger.warn("业务帐号集合为空");
        }
    }


    public int delete(String taskId) {
        List<String> sqlList = new ArrayList<String>();

        String sql = "delete from stb_gw_softup_batch_task where task_id="
                + Integer.parseInt(taskId);

        PrepareSQL psql = new PrepareSQL(sql);
        sqlList.add(psql.getSQL());

        psql = new PrepareSQL("delete from stb_gw_softup_batch where id="
                + Integer.parseInt(taskId) + " and task_id="
                + Integer.parseInt(taskId));
        sqlList.add(psql.getSQL());

        psql = new PrepareSQL(
                "delete from stb_gw_softup_batch_version where batch_id="
                        + Integer.parseInt(taskId));
        sqlList.add(psql.getSQL());

        String[] sqlArray = sqlList.toArray(new String[0]);

        return this.jt.batchUpdate(sqlArray).length;
    }

    /**
     * 插入导入任务(只入批量导入版本表和批量导入任务表)
     */
    public boolean batchImportSoftUp(long userId, long batchId, long addTime,
                                     String vendorId, String pathId, String taskDetail,
                                     String strategyName, String deviceModelIds, String filePath,
                                     String isHotel, String isVIP, String isSen, String cityId) {

        List<String> tempList = new ArrayList<String>();
        PrepareSQL taskSql = new PrepareSQL(
                "insert into stb_gw_softup_batch_task(task_id, add_time, account_id, task_status, mod_time,taskdetail,strategyname,file_path,load_status,is_hotel,is_vip,is_sensitive,city_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
        taskSql.setLong(1, batchId);
        taskSql.setLong(2, addTime);
        taskSql.setLong(3, userId);
        taskSql.setLong(4, 0); // 1-激活、0-失效
        taskSql.setLong(5, addTime);
        taskSql.setString(6, taskDetail);
        taskSql.setString(7, strategyName);
        taskSql.setString(8, filePath);
        taskSql.setLong(9, 0);
        taskSql.setInt(10, StringUtil.getIntegerValue(isHotel));
        taskSql.setInt(11, StringUtil.getIntegerValue(isVIP));
        taskSql.setInt(12, StringUtil.getIntegerValue(isSen));
        taskSql.setString(13, cityId);
        tempList.add(taskSql.getSQL());

        String[] deviceModelArray = deviceModelIds.split(",");
        for (String devModId : deviceModelArray) {
            PrepareSQL sql2 = new PrepareSQL(
                    "insert into stb_gw_softup_batch_version(path_id,batch_id,vendor_id,device_model_id) values(?,?,?,?)");
            sql2.setLong(1, Integer.parseInt(pathId));
            sql2.setLong(2, batchId);
            sql2.setString(3, vendorId);
            sql2.setString(4, devModId);
            tempList.add(sql2.getSQL());
        }
        String[] sqlArray = tempList.toArray(new String[0]);
        int[] ier = jt.batchUpdate(sqlArray);
        if (ier != null && ier.length > 0) {
            return true;
        }
        return false;
    }

    /**
     * 软件升级
     */
    public String getLoadStatus(String taskId) {
        PrepareSQL sql = new PrepareSQL(
                "select load_status from  stb_gw_softup_batch_task where task_id=?");
        sql.setLong(1, StringUtil.getLongValue(taskId));
        Map map = jt.queryForMap(sql.getSQL());
        return StringUtil.getStringValue(map.get("load_status"));
    }

    /**
     * 取消任务
     */
    public boolean cancelBatchTask(String taskId) {
        List<String> tempList = new ArrayList<String>();
        PrepareSQL sql = new PrepareSQL(
                "update stb_gw_softup_batch_task set task_status=0,mod_time=? where task_id=?");
        sql.setLong(1, StringUtil.getLongValue(new Date().getTime() / 1000L));
        sql.setLong(2, StringUtil.getLongValue(taskId));
        tempList.add(sql.getSQL());

        sql = new PrepareSQL(
                "update stb_gw_softup_batch_recent set task_status=0 where batch_id=?");
        sql.setLong(1, StringUtil.getLongValue(taskId));
        tempList.add(sql.getSQL());
        String[] sqlArray = tempList.toArray(new String[0]);
        int[] ier = jt.batchUpdate(sqlArray);
        if (ier != null && ier.length > 0) {
            logger.warn("任务失效：  成功");
            return true;
        }
        logger.warn("任务失效：  失败");
        return false;
    }

    public int deleteSoftUpTask(String taskId) {
        List<String> sqlList = new ArrayList<String>();

        String sql = "delete from stb_gw_softup_batch_task where task_id="
                + Integer.parseInt(taskId);
        PrepareSQL psql = new PrepareSQL(sql);
        sqlList.add(psql.getSQL());

        psql = new PrepareSQL("delete from stb_gw_softup_batch where id="
                + Integer.parseInt(taskId) + " and task_id="
                + Integer.parseInt(taskId));
        sqlList.add(psql.getSQL());

        psql = new PrepareSQL(
                "delete from stb_gw_softup_batch_version where batch_id="
                        + Integer.parseInt(taskId));
        sqlList.add(psql.getSQL());

        psql = new PrepareSQL(
                "delete from stb_gw_softup_batch_recent where batch_id="
                        + Integer.parseInt(taskId));
        sqlList.add(psql.getSQL());

        psql = new PrepareSQL(
                "delete from stb_gw_softup_record where batch_id="
                        + Integer.parseInt(taskId));
        sqlList.add(psql.getSQL());

        String[] sqlArray = sqlList.toArray(new String[0]);

        return this.jt.batchUpdate(sqlArray).length;
    }

    /**
     * 下载失败用户
     */
    public List exportFailAccount(String taskId) {
        PrepareSQL psql = new PrepareSQL(" select gsbt.strategyname ,");// TODO wait (more table related)
        psql.append(" gsbt.city_id as m_city_id,gsbt.is_vip,gsbt.is_hotel,gsbt.is_sensitive,tmp.*");
        psql.append(" from stb_gw_softup_batch_task gsbt ,");
        psql.append(" (select a.task_id, a.serv_account,a.status,b.device_serialnumber,b.device_model_id,b.vendor_id ,b.city_id,a.group_type");
        psql.append(" from stb_gw_softup_batch a  left join stb_tab_gw_device b on  a.serv_account = b.serv_account ");
        psql.append(" where  a.task_id=? and a.status!=1) tmp ");
        psql.append(" where gsbt.task_id = tmp.task_id");
        psql.setLong(1, StringUtil.getLongValue(taskId));
        return this.jt.queryForList(psql.getSQL());
    }

    public List getDeviceModel(String taskId) {
        PrepareSQL psql = new PrepareSQL(
                "select vendor_id, device_model_id from stb_gw_softup_batch_version where batch_id=? ");
        psql.setLong(1, StringUtil.getLongValue(taskId));
        return this.jt.queryForList(psql.getSQL());
    }

    /**
     * 酒店用户
     */
    public List getHotelUser(String taskId) {
        PrepareSQL psql = new PrepareSQL(
                "select serv_account, dev_sn, add_time from stb_gw_softup_batch where task_id=? and group_type=1");
        psql.setLong(1, StringUtil.getLongValue(taskId));
        return this.jt.queryForList(psql.getSQL());
    }

    /**
     * VIP用户
     */
    public List getVIPUser(String taskId) {
        PrepareSQL psql = new PrepareSQL(
                "select serv_account, dev_sn, add_time from stb_gw_softup_batch where task_id=? and group_type=2");
        psql.setLong(1, StringUtil.getLongValue(taskId));
        return this.jt.queryForList(psql.getSQL());
    }

    /**
     * 敏感用户
     */
    public List getSenUser(String taskId) {
        PrepareSQL psql = new PrepareSQL(
                "select id from stb_gw_softup_batch where task_id=? and group_type=4");
        psql.setLong(1, StringUtil.getLongValue(taskId));
        return this.jt.queryForList(psql.getSQL());
    }

    /**
     * 获取所有厂商
     */
    public List getVendor() {
        logger.debug("getVendor()");
        PrepareSQL pSQL = new PrepareSQL(
                "select vendor_id,vendor_name, vendor_add from stb_tab_vendor");
        return jt.queryForList(pSQL.getSQL());
    }

    /**
     * 所有设备型号
     */
    public List getDeviceModel() {
        logger.debug("getDeviceModel()");
        PrepareSQL pSQL = new PrepareSQL();
        pSQL.append(" select device_model_id,device_model from stb_gw_device_model  ");
        return jt.queryForList(pSQL.getSQL());
    }

    /**
     * 所有型号对应版本
     */
    public List getDeviceModelSoft() {
        logger.debug("getDeviceModel()");
        PrepareSQL pSQL = new PrepareSQL();
        pSQL.append(" select device_model_id,softwareversion  from stb_tab_devicetype_info  ");
        return jt.queryForList(pSQL.getSQL());
    }

    /**
     * 所有用户类型
     */
    public List getUserType() {
        logger.debug("getUserType()");
        PrepareSQL pSQL = new PrepareSQL();
        pSQL.append(" select a.user_grp ,b.type_name  from grp_info a,tab_cust_type b where a.type_id = b.type_id");
        return jt.queryForList(pSQL.getSQL());
    }

    /**
     * 厂商 对应
     */
    public Map<String, String> getVendorMap() {
        Map<String, String> map = new HashMap<String, String>();
        List list = getVendor();
        if (null != list && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Map mp = (Map) list.get(i);
                map.put(StringUtil.getStringValue(mp.get("vendor_id")),
                        StringUtil.getStringValue(mp.get("vendor_add")));
            }
        }
        return map;
    }

    /**
     * 型号对应
     */
    public Map<String, String> getDeviceModelMap() {
        Map<String, String> map = new HashMap<String, String>();
        List list = getDeviceModel();
        if (null != list && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Map mp = (Map) list.get(i);
                map.put(StringUtil.getStringValue(mp.get("device_model_id")),
                        StringUtil.getStringValue(mp.get("device_model")));
            }
        }
        return map;
    }

    /**
     * 用户类型对应
     */
    public Map<String, String> getUserTypeMap() {
        Map<String, String> map = new HashMap<String, String>();
        List list = getUserType();
        if (null != list && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Map mp = (Map) list.get(i);
                map.put(StringUtil.getStringValue(mp.get("user_grp")),
                        StringUtil.getStringValue(mp.get("type_name")));
            }
        }
        return map;
    }

    /**
     * 用户型号版本对应
     */
    public Map<String, String> getModelSoftMap() {
        Map<String, String> map = new HashMap<String, String>();
        List list = getDeviceModelSoft();
        if (null != list && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Map mp = (Map) list.get(i);
                map.put(StringUtil.getStringValue(mp.get("devicetype_id")),
                        StringUtil.getStringValue(mp.get("softwareversion")));
            }
        }
        return map;
    }

    /**
     * 获取策略名
     */
    public String getStrategyName(String taskId) {
        logger.warn("getDeviceModel({})", taskId);
        PrepareSQL pSQL = new PrepareSQL();
        pSQL.append(" select strategyname from stb_gw_softup_batch_task where  task_id=? ");
        pSQL.setLong(1, StringUtil.getLongValue(taskId));
        return StringUtil.getStringValue(jt.queryForMap(pSQL.getSQL()).get("strategyname"));
    }

    /**
     * 生成入策略的sql语句
     */
    public List<String> strategySQL(StrategyOBJ obj, String deviceTypeIdOld) {
        logger.debug("strategySQL({})", obj);
        if (obj == null) {
            return null;
        }
        if (StringUtil.IsEmpty(taskId)) {
            taskId = obj.getTaskId();
        }
        List<String> sqlList = new ArrayList<String>();
        StringBuilder tempSql = new StringBuilder();
        //江西专用字段，入策略表的表名根据配置文件读取，策略日志表的表名为：策略表_log
        String table_name = "";
        if (LipossGlobals.inArea(Global.JXDX)) {
            table_name = LipossGlobals.getLipossProperty("stb_strategy_tabname.soft.tabname");
        }
        if (LipossGlobals.inArea(Global.HBLT) || LipossGlobals.inArea(Global.XJDX)) {
            tempSql.append("delete from stb_gw_strategy_soft where device_id='")
                    .append(obj.getDeviceId()).append("' and temp_id=")
                    .append(obj.getTempId());
        } else if (LipossGlobals.inArea(Global.JXDX)) {
            //JXDX-ITV-REQ-20180822-WUWF-001(ITV终端管理平台机顶盒批量软件升级改造)
            //策略表的表名，需要从配置文件中读取
            tempSql.append("delete from ").append(table_name).append(" where device_id='")
                    .append(obj.getDeviceId()).append("' and temp_id=")
                    .append(obj.getTempId());
        } else {
            tempSql.append("delete from stb_gw_serv_strategy where device_id='")
                    .append(obj.getDeviceId()).append("' and temp_id=")
                    .append(obj.getTempId());
        }
        // 生成入策略的sql语句
        StringBuilder sql = new StringBuilder();
        if (LipossGlobals.inArea(Global.HBLT) || LipossGlobals.inArea(Global.XJDX)) {
            sql.append("insert into stb_gw_strategy_soft (");
        } else if (LipossGlobals.inArea(Global.JXDX)) {
            sql.append("insert into ").append(table_name).append("(");
        } else {
            sql.append("insert into stb_gw_serv_strategy (");
        }
        sql.append("id,acc_oid,time,type,gather_id,device_id,oui,device_serialnumber,username"
                + ",sheet_para,service_id,task_id,order_id,sheet_type,temp_id,is_last_one");
        sql.append(") values (");
        sql.append(obj.getId());
        sql.append("," + obj.getAccOid());
        sql.append("," + obj.getTime());
        sql.append("," + obj.getType());
        sql.append("," + StringUtil.getSQLString(obj.getGatherId()));
        sql.append("," + StringUtil.getSQLString(obj.getDeviceId()));
        sql.append("," + StringUtil.getSQLString(obj.getOui()));
        sql.append("," + StringUtil.getSQLString(obj.getSn()));
        sql.append("," + StringUtil.getSQLString(obj.getUsername()));
        sql.append("," + StringUtil.getSQLString(obj.getSheetPara()));
        sql.append("," + obj.getServiceId());
        sql.append("," + StringUtil.getSQLString(obj.getTaskId()));
        sql.append("," + obj.getOrderId());
        sql.append("," + obj.getSheetType());
        sql.append("," + obj.getTempId());
        sql.append("," + obj.getIsLastOne());
        sql.append(")");
        // 生成入策略日志的sql语句
        StringBuilder logsql = new StringBuilder();
        if (LipossGlobals.inArea(Global.HBLT) || LipossGlobals.inArea(Global.XJDX)) {
            logsql.append("insert into stb_gw_strategy_soft_log (");
        } else if (LipossGlobals.inArea(Global.JXDX)) {
            logsql.append("insert into ").append(table_name).append("_log (");
        } else {
            logsql.append("insert into stb_gw_serv_strategy_log (");
        }
        logsql.append("id,acc_oid,time,type,gather_id,device_id,oui,device_serialnumber,username"
                + ",sheet_para,service_id,task_id,order_id,sheet_type,temp_id,is_last_one");
        logsql.append(") values (");
        logsql.append(obj.getId());
        logsql.append("," + obj.getAccOid());
        logsql.append("," + obj.getTime());
        logsql.append("," + obj.getType());
        logsql.append("," + StringUtil.getSQLString(obj.getGatherId()));
        logsql.append("," + StringUtil.getSQLString(obj.getDeviceId()));
        logsql.append("," + StringUtil.getSQLString(obj.getOui()));
        logsql.append("," + StringUtil.getSQLString(obj.getSn()));
        logsql.append("," + StringUtil.getSQLString(obj.getUsername()));
        logsql.append("," + StringUtil.getSQLString(obj.getSheetPara()));
        logsql.append("," + obj.getServiceId());
        logsql.append("," + StringUtil.getSQLString(obj.getTaskId()));
        logsql.append("," + obj.getOrderId());
        logsql.append("," + obj.getSheetType());
        logsql.append("," + obj.getTempId());
        logsql.append("," + obj.getIsLastOne());
        logsql.append(")");
        if (LipossGlobals.inArea(Global.HBLT) || LipossGlobals.inArea(Global.JXDX)
                || LipossGlobals.inArea(Global.NXDX) || LipossGlobals.inArea(Global.XJDX)) {
            StringBuilder recordSql = new StringBuilder();
            recordSql.append("insert into stb_gw_softup_record (id, device_id, task_id, dev_type_id_old, start_time,result) values(")
                    .append(obj.getId())
                    .append("," + StringUtil.getSQLString(obj.getDeviceId()))
                    .append("," + Integer.parseInt(taskId))
                    .append("," + Integer.parseInt(deviceTypeIdOld))
                    .append("," + new Date().getTime() / 1000)
                    .append("," + 0 + ")");
            sqlList.add(recordSql.toString());
        }

        PrepareSQL psql = new PrepareSQL(tempSql.toString());
        sqlList.add(psql.getSQL());
        psql = new PrepareSQL(sql.toString());
        sqlList.add(psql.getSQL());
        psql = new PrepareSQL(logsql.toString());
        sqlList.add(psql.getSQL());

        logger.debug("入策略的sql语句-->{}",
                tempSql.toString() + ";" + sql.toString() + ";"
                        + logsql.toString());

        return sqlList;
    }

    /**
     * 执行批量SQL.
     *
     * @param sqlList SQL语句数组
     * @return 返回操作的记录条数
     */
    public int[] doBatch(List<String> sqlList) {
        String[] arrsql = new String[sqlList.size()];
        for (int i = 0; i < sqlList.size(); i++) {
            arrsql[i] = String.valueOf(sqlList.get(i));
        }
        int[] result = jt.batchUpdate(arrsql);
        arrsql = null;
        return result;
    }

    /**
     * 根据devicetype_id获取软件升级的工单参数
     */
    public Map<String, String> getSoftFileInfo() {// TODO wait (more table related)
        String strSQL = "select a.version_path,c.devicetype_id_new from  stb_gw_version_file_path a , " +
                "stb_gw_filepath_devtype b ,stb_gw_soft_upgrade_temp_map c where a.id=b.path_id and " +
                "b.goal_devicetype_id=c.devicetype_id_new";
        PrepareSQL psql = new PrepareSQL(strSQL);
        List list = jt.queryForList(psql.getSQL());
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < list.size(); i++) {
            Map tmap = (Map) list.get(i);
            map.put(StringUtil.getStringValue(tmap.get("devicetype_id_new")), (String) tmap.get("version_path"));
        }
        return map;
    }

    /**
     * 获取软件升级的目标型号，版本对应关系
     */
    public Map<String, String> getSoftUp() {
        String strSQL = "select temp_id, devicetype_id_old, devicetype_id_new from stb_gw_soft_upgrade_temp_map";
        PrepareSQL psql = new PrepareSQL(strSQL);
        List list = jt.queryForList(psql.getSQL());
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < list.size(); i++) {
            Map tmap = (Map) list.get(i);
            map.put(StringUtil.getStringValue(tmap.get("temp_id")) + "|" + StringUtil.getStringValue(tmap.get("devicetype_id_old")),
                    StringUtil.getStringValue(tmap.get("devicetype_id_new")));
        }
        return map;
//		String	strSQL = "select to_char(temp_id)||'|'"
//			+ "||to_char(devicetype_id_old),devicetype_id_new";
//
//		strSQL = strSQL + " from stb_gw_soft_upgrade_temp_map";
//		PrepareSQL psql = new PrepareSQL(strSQL);
//		psql.getSQL();
//		return DataSetBean.getMap(strSQL);
    }

    /**
     * 获取设备的型号
     */
    public String getDevicetypeId(String deviceId) {
        String reDeviceTypeId = "";
        String sql = "select devicetype_id from stb_tab_gw_device where device_id = ?";
        PrepareSQL psql = new PrepareSQL();
        psql.setSQL(sql);
        psql.setString(1, deviceId);
        List list = jt.queryForList(psql.getSQL());

        for (int i = 0; i < list.size(); i++) {
            Map tmap = (Map) list.get(i);
            reDeviceTypeId = StringUtil.getStringValue(tmap.get("devicetype_id"));
        }
        return reDeviceTypeId;
    }

    /**
     * 获取设备黑名单信息
     */
    public Map<String, String> getDevBlackListInfo(String deviceId) {
        PrepareSQL psql = new PrepareSQL();
        psql.setSQL("select device_id from  stb_tab_blacklist  where device_id=?");
        psql.setString(1, deviceId);
        return DBOperation.getRecord(psql.getSQL());
    }

    public int updateTaskDesc(String taskId, String taskDesc) {
        PrepareSQL sql = new PrepareSQL(
                "update stb_gw_softup_task set task_desc =? where task_id=?");
        sql.setString(1, taskDesc);
        sql.setLong(2, StringUtil.getLongValue(taskId));
        return jt.update(sql.getSQL());
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * 入软件升级策略表stb_gw_strategy_soft
     */
    public int insertStrategy(long accOId, String id, String deviceId) {
        PrepareSQL sql = new PrepareSQL();
        sql.append("select version_name,version_path,file_size,md5,id ");
        sql.append("from stb_soft_version_path where id=? ");
        sql.setLong(1, StringUtil.getLongValue(id));

        Map map = jt.queryForMap(sql.getSQL());

        if (map != null && !map.isEmpty()) {
            String sheet_para = toXml(map);
            List<String> sqlList = new ArrayList<String>();

            sql = new PrepareSQL();
            sql.append("update stb_gw_strategy_soft_log set status=0 where device_id=? and service_id=? ");
            sql.setString(1, deviceId);
            sql.setLong(2, 5);
            sqlList.add(sql.getSQL());

            sql = new PrepareSQL();
            sql.append("delete from stb_gw_strategy_soft where device_id=? and service_id=? ");
            sql.setString(1, deviceId);
            sql.setLong(2, 5);
            sqlList.add(sql.getSQL());

            long sid = Math.round(Math.random() * 100000L);
            sql = new PrepareSQL();
            sql.append("insert into stb_gw_strategy_soft (id,status,result_id,acc_oid,time,");
            sql.append("type,service_id,redo,sheet_para,device_id,sheet_id) ");
            sql.append("values (?,?,?,?,?,?,?,?,?,?,?) ");
            sql.setLong(1, sid);
            sql.setLong(2, 1);//默认生效
            sql.setLong(3, -1);//默认未做
            sql.setLong(4, accOId);
            sql.setLong(5, System.currentTimeMillis() / 1000L);
            sql.setLong(6, 4);
            sql.setLong(7, 5);
            sql.setLong(8, 0);
            sql.setString(9, sheet_para);
            sql.setString(10, deviceId);
            sql.setString(11, id);
            sqlList.add(sql.getSQL());

            sql = new PrepareSQL();
            sql.append("insert into stb_gw_strategy_soft_log (id,status,result_id,acc_oid,time,");
            sql.append("type,service_id,redo,sheet_para,device_id,sheet_id) ");
            sql.append("values (?,?,?,?,?,?,?,?,?,?,?) ");
            sql.setLong(1, sid);
            sql.setLong(2, 1);
            sql.setLong(3, -1);//默认未做
            sql.setLong(4, accOId);
            sql.setLong(5, System.currentTimeMillis() / 1000L);
            sql.setLong(6, 4);
            sql.setLong(7, 5);
            sql.setLong(8, 0);
            sql.setString(9, sheet_para);
            sql.setString(10, deviceId);
            sql.setString(11, id);
            sqlList.add(sql.getSQL());

            int[] result = doBatch(sqlList);
            if (result != null && result.length > 0) {
                return 1;
            }
        }

        return 0;
    }

    private String toXml(Map versionMap) {
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("GB2312");
        try {
            Element root = document.addElement("root");

            root.addElement("version_id").addText(StringUtil.getStringValue(versionMap, "id"));
            root.addElement("version_name").addText(StringUtil.getStringValue(versionMap, "version_name"));
            root.addElement("version_path").addText(StringUtil.getStringValue(versionMap, "version_path"));
            root.addElement("file_size").addText(StringUtil.getStringValue(versionMap, "file_size"));
            root.addElement("md5").addText(StringUtil.getStringValue(versionMap, "md5"));
        } catch (Exception e) {
            logger.error("解析版本信息异常，err:[{}]", e);
        }

        return document.asXML();
    }

    public String inSoftup(String taskid, String deviceid, long add_time) {
        String sql = "insert into stb_tab_importsoftup (task_id,device_id,add_time) values(?,?,?)";
        PrepareSQL psql = new PrepareSQL(sql);
        psql.setInt(1, Integer.valueOf(taskid));
        psql.setString(2, deviceid);
        psql.setLong(3, add_time);
        return psql.getSQL();
    }

    public List<String> getSencondCityIdList() {
        List<String> list = new ArrayList<String>();
        PrepareSQL psql = new PrepareSQL();
        String strSQL = "select city_id from tab_city where parent_id = '00' " ;

        psql.append(strSQL);
        ArrayList<HashMap<String, String>> cityList = DBOperation.getRecords(psql.getSQL());
        for (HashMap cityMap : cityList) {
            list.add(StringUtil.getStringValue(cityMap.get("city_id")));
        }

        return list;
    }


    public void addLog(long user_id, String user_ip, String operation_content,String result)
    {
        //private static String writeLogSql = "insert into tab_oper_log("
        //			+ "acc_oid,acc_login_ip,operationlog_type,operation_time,operation_name"
        //			+ ",operation_object,operation_content,operation_device"
        //			+ ",operation_result,result_id) values(?,?,?,?,?,?,?,?,?,?)";
        PrepareSQL psql = new PrepareSQL();
        psql.setSQL(writeLogSql);
        psql.setLong(1,user_id);
        psql.setString(2,user_ip);
        psql.setInt(3,1);
        psql.setLong(4,System.currentTimeMillis()/1000L);
        psql.setString(5,"5");
        psql.setString(6,"WEB");

        psql.setString(7,operation_content);
        psql.setString(8,"Web");
        psql.setString(9,"1".equals(result)?"成功":"失败");
        psql.setInt(10,1);
        DBOperation.executeUpdate(psql.getSQL());
    }
    
    public void updateTaskId(long tempTaskId,long taskId,String custcheck,String  devsnCheck){
    	StringBuffer sb = new StringBuffer();
    	if("1".equals(custcheck)){
    		sb.append("update stb_gw_softup_task_acccheck set task_id=? where task_id=?");
    	}
    	if("1".equals(devsnCheck)){
    		sb.append("update stb_gw_softup_task_devsnheck set task_id=? where task_id=?");
    	}
    	
    	PrepareSQL pSQL = new PrepareSQL();
    	pSQL.setSQL(sb.toString());
    	pSQL.setLong(1, taskId);
    	pSQL.setLong(2, tempTaskId);
    	try
		{
			DBOperation.executeUpdate(pSQL.getSQL());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    	
    }
    /**
     * 根据task_Id,结果查询升级记录
     */
    public List queryUpRecordByTaskId_import(String taskID, String result,
                                      String importtype, int startRow, int countRow, boolean split) {
        // TODO wait (more table related)
    	logger.warn("result:[{}]", result);
    	
    	StringBuffer sb = new StringBuffer(""); 
    	if(StringUtil.IsEmpty(result)){
    		//查询全部
    		if("1".equals(importtype)){
    			sb.append("select  a.task_id, t.result,t.start_time,t.dev_type_id_new, t.dev_type_id_old,");
        		sb.append("d.devicetype_id, d.serv_account, d.device_serialnumber ,d.vendor_id,d.device_model_id ");
        		sb.append("from stb_gw_softup_task_devsnheck a ");
        		sb.append("left join stb_gw_softup_record t on a.task_id=t.task_id, ");
        	    sb.append("stb_tab_gw_device d ");
        	    sb.append("where a.dev_sn=d.device_serialnumber and a.task_id=? "); 
    		}else{
    			sb.append("select  a.task_id, t.result,t.start_time,t.dev_type_id_new, t.dev_type_id_old,");
        		sb.append("d.devicetype_id, a.serv_account, d.device_serialnumber,d.vendor_id,d.device_model_id  ");
        		sb.append("from stb_gw_softup_task_acccheck a ");
        		sb.append("left join stb_gw_softup_record t on a.task_id=t.task_id, ");
        	    sb.append("stb_tab_customer cus,stb_tab_gw_device d ");
        	    sb.append("where a.serv_account=cus.serv_account and cus.customer_id=d.customer_id and a.task_id=? "); 
    		}
    	}else{
    		
    		if("0".equals(result)){
    			sb.append("select a.task_id,0 as result,c.start_time,c.dev_type_id_new,c.dev_type_id_old,");
    			sb.append("d.devicetype_id,d.serv_account, d.device_serialnumber,d.vendor_id,d.device_model_id  ");
    			sb.append("from ");
    			if("1".equals(importtype)){
    				sb.append("stb_gw_softup_task_devsnheck a left join stb_gw_softup_record c on a.task_id=c.task_id,");
    				sb.append("stb_tab_gw_device d where a.dev_sn=d.device_serialnumber ");
    			}else{
    				sb.append("stb_gw_softup_task_acccheck a left join stb_gw_softup_record c on a.task_id=c.task_id,");
    				sb.append("stb_tab_customer b,stb_tab_gw_device d ");
    				sb.append("where a.serv_account=b.serv_account and b.customer_id=d.customer_id ");
    			}
    			sb.append("and not exists ");
    			sb.append("(select 1 from stb_gw_softup_record b where b.device_id=d.device_id and (b.result <>1 and b.result <>-1)) ");
    			sb.append("and a.task_id=?");
    		}else{
    			sb.append("select t.task_id, t.result,t.start_time,t.dev_type_id_new, t.dev_type_id_old, ");
        		sb.append("d.devicetype_id, d.serv_account, d.device_serialnumber ,d.vendor_id,d.device_model_id ");
        		sb.append("from stb_gw_softup_record t, stb_tab_gw_device d ");
        	    sb.append("where d.device_id = t.device_id and t.task_id=? ");
        	    if (!StringUtil.IsEmpty(result)){
        			sb.append(" and t.result="+result.trim());
    	        } 
    		}
    	}
    	
    	vendorMap = GwStbVendorModelVersionDAO.getVendorMap();
    	deviceTypeMap = GwStbVendorModelVersionDAO.getDevicetypeMap();
        deviceModelMap = GwStbVendorModelVersionDAO.getDeviceModelMap();
        PrepareSQL sql = null;
        sql = new PrepareSQL(sb.toString());
        sql.setLong(1, Long.parseLong(taskID));
        
        return jt.querySP(sql.getSQL(), startRow, countRow, new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                
                //厂商
                String vendorId = StringUtil.getStringValue(rs.getString("vendor_id"));
                map.put("vendor_add", StringUtil.getStringValue(vendorMap.get(vendorId)));
                
                //型号
                String deviceModeId = StringUtil.getStringValue(rs.getString("device_model_id"));
                map.put("device_model", StringUtil.getStringValue(deviceModelMap.get(deviceModeId)));
                
                //业务账号
                String serv_account = StringUtil.getStringValue(rs.getString("serv_account"));
                map.put("serv_account", serv_account);
                
                //设备序列号
                String device_serialnumber = StringUtil.getStringValue(rs.getString("device_serialnumber"));
                map.put("device_serialnumber", device_serialnumber);
                
                //升级结果
                String result = StringUtil.getStringValue(rs.getString("result"));
                map.put("result", result);
                
                //软件版本
                String new_device_type_id = StringUtil.getStringValue(rs.getString("dev_type_id_new"));
                if(!StringUtil.IsEmpty(new_device_type_id)){
                    map.put("newsoftversion", StringUtil.getStringValue(deviceTypeMap.get(new_device_type_id)));
                }else{
                    map.put("newsoftversion", "");
                }
                //旧版本
                String old_device_type_id = StringUtil.getStringValue(rs.getString("dev_type_id_old"));
                if(StringUtil.IsEmpty(old_device_type_id)){
                	old_device_type_id =  StringUtil.getStringValue(rs.getString("devicetype_id"));
                }
                if(!StringUtil.IsEmpty(old_device_type_id)){
                    map.put("oldsoftversion", StringUtil.getStringValue(deviceTypeMap.get(old_device_type_id)));
                }else{
                    map.put("oldsoftversion", "");
                }
                
                try {
                    if (null == rs.getString("start_time")|| "0".equals(rs.getString("start_time"))) {
                        map.put("start_time", "");
                    } else {
                        long start_time = StringUtil.getLongValue(rs.getString("start_time"));
                        DateTimeUtil dt = new DateTimeUtil(start_time * 1000);
                        map.put("start_time", dt.getLongDate());
                    }
                } catch (Exception e) {
                    map.put("start_time", "");
                }
                return map;
            }
        });
    }
    
}
