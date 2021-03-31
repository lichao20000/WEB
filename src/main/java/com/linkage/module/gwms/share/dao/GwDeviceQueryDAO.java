package com.linkage.module.gwms.share.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.DataSourceContextHolder;
import com.linkage.module.gwms.dao.DataSourceTypeCfgPropertiesManager;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.system.extend.spring.jdbc.JdbcTemplateExtend;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @category com.linkage.module.stb.resource.dao
 * @since 2009-12-25
 */
@SuppressWarnings("rawtypes")
public class GwDeviceQueryDAO 
{
    private static Logger logger = LoggerFactory.getLogger(GwDeviceQueryDAO.class);

    private JdbcTemplateExtend jt;
    private JdbcTemplateExtend jt4report;
    private String instArea = Global.instAreaShortName;
    // 查询多个最多只能查询这么多条
    private int maxNum = 10000;

    private static final String CENTER_CITY = "00";
    // 无效的city_id
    //private static final String INVALID_CITY = "-1";

    
    /**
     * @param dao
     */
    public void setDao(DataSource dao) {
        jt = new JdbcTemplateExtend(dao);
    }

    private void setDataSourceType(String isRealtimeQuery, String key) 
    {
        DataSourceContextHolder.clearDBType();
        if ("false".equals(isRealtimeQuery)) {
            String type = null;
            type = DataSourceTypeCfgPropertiesManager.getInstance()
                    .getConfigItem(key + "ChangeDB");
            if (!StringUtil.IsEmpty(type)) {
                logger.warn("类：" + this.getClass().getName() + "的数据源类型配置为："
                        + type);
                DataSourceContextHolder.setDBType(type);
            }
        }
    }

    /**
     * 模拟匹配提示
     */
    public List getDeviceSn(String cityId, String searchTxt, String gwShare_queryField) 
    {
        PrepareSQL pSQL = new PrepareSQL();
        if (null != searchTxt) 
        {
            if ("username".equals(gwShare_queryField)) 
            {
            	if(DBUtil.GetDB()==3){
            		pSQL.append("select concat(b.device_serialnumber,'|',a.username) as device_sn ");
            	}else{
            		pSQL.append("select top 5 b.device_serialnumber + '|' + a.username as device_sn ");
            	}
            	
                if (LipossGlobals.IsITMS()) {
                    pSQL.append("from tab_hgwcustomer a,tab_gw_device b ");
                } else {
                    pSQL.append("from tab_egwcustomer a,tab_gw_device b ");
                }
                pSQL.append("where a.device_id=b.device_id and a.user_state = '1' ");
                pSQL.append("and a.username like '%"+searchTxt+"%' ");
                if (null != cityId && !CENTER_CITY.equals(cityId)) {
                    ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
                    pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
                    cityArray = null;
                }
                pSQL.append(" order by b.complete_time");
            } 
            else 
            {
            	if(DBUtil.GetDB()==3){
            		pSQL.append("select concat(device_serialnumber,'|',loopback_ip) as device_sn ");
            	}else{
            		pSQL.append("select top 5 device_serialnumber + '|' + loopback_ip as device_sn ");
            	}
                pSQL.append("from tab_gw_device where device_status=1 ");
                if ("deviceSn".equals(gwShare_queryField)) {
                    pSQL.append(" and device_serialnumber like '%"+searchTxt+"%' ");
                } else if ("deviceIp".equals(gwShare_queryField)) {
                    pSQL.append(" and loopback_ip like '%"+searchTxt+"%' ");
                } else {
                    pSQL.append(" and ( device_serialnumber like '%"+searchTxt+"%' ");
                    pSQL.append("or loopback_ip like '%"+searchTxt+"%') ");
                }
                if (null != cityId && !CENTER_CITY.equals(cityId)) {
                    ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
                    pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
                    cityArray = null;
                }
                if(DBUtil.GetDB()==3){
                	pSQL.append(" and limit 5");
                }
                pSQL.append(" order by complete_time");
            }
        }
        return jt.queryForList(pSQL.getSQL());
    }


    public int getTmpList() 
    {
        logger.debug("gwDevicQueryDAO({})");
        PrepareSQL psql = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	 psql.append("select count(*) from tab_softwareup_tmp");
        }else{
        	 psql.append("select count(1) from tab_softwareup_tmp");
        }
        
        return jt.queryForInt(psql.getSQL());
    }

    public int[] insertImportDataTmp(List<String> dataList, int type) 
    {
        ArrayList<String> sqlList = new ArrayList<String>();
        String sql = "";
        for (int i = 0; i < dataList.size(); i++) {
            sql = "insert into tab_softwareup_tmp values ( '" 
            		+ dataList.get(i) + "' , " + type + ")";
            logger.info(sql);
            sqlList.add(sql);
        }
        return DataSetBean.doBatch(sqlList);
    }

    public void insertTmp(String fileName, List<String> dataList, String importQueryField) 
    {
    	logger.warn("dataListSize:{}",dataList.size());
        ArrayList<String> sqlList = new ArrayList<String>();
        PrepareSQL psql = null;
        for (int i = 0; i < dataList.size(); i++) 
        {
            psql = new PrepareSQL();
            if ("username".equals(importQueryField) || "kdusername".equals(importQueryField)) {
            	psql.append("insert into tab_seniorquery_tmp(filename,username)");
            }else{
            	psql.append("insert into tab_seniorquery_tmp(filename,devicesn)");
            }
            psql.append(" values ('" + fileName + "','" + dataList.get(i) + "')");
            sqlList.add(psql.getSQL());
            
            if(sqlList.size()==200){
	            DBOperation.executeUpdate(sqlList);
	            sqlList.clear();
	            
	            try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
        }
       
        if(!sqlList.isEmpty()){
        	DBOperation.executeUpdate(sqlList);
            sqlList.clear();
        }
    }
    
    
    public void insertTmpStb(String fileName, List<String> dataList, String importQueryField) 
    {
    	logger.warn("dataListSize:{}",dataList.size());
        ArrayList<String> sqlList = new ArrayList<String>();
        PrepareSQL psql = null;
        for (int i = 0; i < dataList.size(); i++) 
        {
            psql = new PrepareSQL();
            if ("username".equals(importQueryField) || "kdusername".equals(importQueryField)) {
            	psql.append("insert into stb_tab_seniorquery_tmp(filename,username)");
            }else{
            	psql.append("insert into stb_tab_seniorquery_tmp(filename,devicesn)");
            }
            psql.append(" values ('" + fileName + "','" + dataList.get(i) + "')");
            sqlList.add(psql.getSQL());
            
            if(sqlList.size()==200){
	            DBOperation.executeUpdate(sqlList);
	            sqlList.clear();
	            
	            try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
        }
       
        if(!sqlList.isEmpty()){
        	DBOperation.executeUpdate(sqlList);
            sqlList.clear();
        }
    }

    public void insertTmpForDoubleParam(String fileName, 
    		List<String> dataList, List<String> paramList, 
    		String importQueryField) 
    {
        ArrayList<String> sqlList = new ArrayList<String>();
        PrepareSQL psql = null;
        if ("username".equals(importQueryField)) {
            for (int i = 0; i < dataList.size(); i++) {
                psql = new PrepareSQL();
                psql.append("insert into tab_seniorquery_tmp(filename," +
                		"username,paramvalue)" 
                		+ " values ('" + fileName + "','" 
                		+ dataList.get(i) + "','" + paramList.get(i) + "')");
                sqlList.add(psql.getSQL());
            }
        } else {
            for (int i = 0; i < dataList.size(); i++) {
                psql = new PrepareSQL();
                psql.append("insert into tab_seniorquery_tmp(filename," +
                		"devicesn,paramvalue)" 
                		+ " values ('" + fileName + "','" 
                		+ dataList.get(i) + "','" + paramList.get(i) + "')");
                sqlList.add(psql.getSQL());
            }
        }
        int res;
        if (LipossGlobals.inArea(Global.JSDX)) {
            res = DBOperation.executeUpdate(sqlList, "proxool.xml-report");
        } else {
            res = DBOperation.executeUpdate(sqlList);
        }
    }

    public void insertTmp4Wireless(String fileName, List<String> dataList, 
    		String importQueryField, String currencyType) 
    {
        ArrayList<String> sqlList = new ArrayList<String>();
        PrepareSQL psql = null;
        if ("username".equals(importQueryField)) {
            if ("1".equals(currencyType)) {
                for (int i = 0; i < dataList.size(); i++) {
                    String tmps = "'" + dataList.get(i).replace(",", "','") + "'";
                    psql = new PrepareSQL();
                    psql.append("insert into tab_seniorquery_tmp(filename," +
                    		"username,ssid,vlanid,priority,channel,wlanport)"
                    		+ " values ('" + fileName + "'," + tmps + ")");
                    sqlList.add(psql.getSQL());
                }
            } else if ("0".equals(currencyType)) {
                for (int i = 0; i < dataList.size(); i++) {
                    String tmps = "'" + dataList.get(i).replace(",", "','") + "'";
                    psql = new PrepareSQL();
                    psql.append("insert into tab_seniorquery_tmp(filename," +
                    		"username,ssid,vlanid,priority,channel)" 
                    		+ " values ('" + fileName + "'," + tmps + ")");
                    sqlList.add(psql.getSQL());
                }
            }

        } else {
            if ("1".equals(currencyType)) {
                for (int i = 0; i < dataList.size(); i++) {
                    String tmps = "'" + dataList.get(i).replace(",", "','") + "'";
                    psql = new PrepareSQL();
                    psql.append("insert into tab_seniorquery_tmp(filename," +
                    		"devicesn,ssid,vlanid,priority,channel,wlanport)" 
                    		+ " values ('" + fileName + "'," + tmps + ")");
                    sqlList.add(psql.getSQL());
                }
            } else if ("0".equals(currencyType)) {
                for (int i = 0; i < dataList.size(); i++) {
                    String tmps = "'" + dataList.get(i).replace(",", "','") + "'";
                    psql = new PrepareSQL();
                    psql.append("insert into tab_seniorquery_tmp(filename," +
                    		"devicesn,ssid,vlanid,priority,channel)" 
                    		+ " values ('" + fileName + "'," + tmps + ")");
                    sqlList.add(psql.getSQL());
                }
            } else {
                logger.warn("GwDeviceQueryDAO==>insertTmp4Wireless()devicesn==currencyType++null");
            }

        }
        int res;
        if (LipossGlobals.inArea(Global.JSDX)) {
            res = DBOperation.executeUpdate(sqlList, "proxool.xml-report");
        } else {
            res = DBOperation.executeUpdate(sqlList);
        }
    }


    /**
     * @param areaId
     * @param cityId
     * @param userList
     * @return
     */
    public List queryDeviceByImportUsername(String gw_type, long areaId, 
    		String cityId, List<String> userList, String fileName) 
    {
        logger.debug("queryDeviceByImportUsername()");
        //江苏查报表库
        setDataSourceType(LipossGlobals.inArea(Global.JSDX) ? "false" : "true", this.getClass().getName());
        String tableName = "tab_hgwcustomer";
        if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type)) {
            tableName = "tab_egwcustomer";
        }
        
        String condition1;
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	condition1 = "select a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,a.office_id,a.complete_time,"
        				+ "a.zone_id,a.buy_time,a.staff_id,a.remark,a.loopback_ip,a.interface_id,a.device_status,a.gather_id,"
        				+ "a.devicetype_id,a.maxenvelopes,a.cr_port,a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,"
        				+ "a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,a.acs_passwd,a.device_type,"
        				+ "a.x_com_username,a.x_com_passwd,a.gw_type,a.device_model_id,a.customer_id,a.device_url,"
        				+ "a.x_com_passwd_old,a.vendor_id,ssid,vlanid,priority,channel,";
        	if (LipossGlobals.inArea(Global.JSDX)) {
        		condition1+="wlanport,paramvalue,";
        	}
        }else{
        	condition1 = "select a.*,f.*,";
        }
        condition1 += "b.vendor_add,c.device_model,d.softwareversion,e.username,e.user_id from ";
        
        String condition2 = "a.device_id=e.device_id";
        String tab1 = "tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,tab_seniorquery_tmp f";
        
        if("4".equals(gw_type)){
        	tab1 = "stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c," +
        			"stb_tab_devicetype_info d,stb_tab_seniorquery_tmp f";
        	tableName = "stb_tab_customer";
        	
        	condition1=null;
        	if(DBUtil.GetDB()==3){
        		condition1 = "a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,a.office_id,a.complete_time,"
        				+ "a.zone_id,a.buy_time,a.staff_id,a.remark,a.loopback_ip,a.interface_id,a.device_status,a.gather_id,"
        				+ "a.devicetype_id,a.maxenvelopes,a.cr_port,a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,"
        				+ "a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,a.acs_passwd,a.device_type,"
        				+ "a.x_com_username,a.x_com_passwd,a.gw_type,a.device_model_id,a.customer_id,a.device_url,"
        				+ "a.x_com_passwd_old,a.vendor_id,a.username,a.user_id,ssid,vlanid,priority,channel";
        	if (LipossGlobals.inArea(Global.JSDX)) {
        		condition1+="wlanport,paramvalue,";
        	}
        	}else{
        		condition1="select a.*,f.*,";
        	}
        	condition1 += "b.vendor_add,c.device_model,d.softwareversion,e.loid,e.customer_id from ";
        	condition2 = "a.customer_id=e.customer_id";
        }
        PrepareSQL pSQL = new PrepareSQL();
        pSQL.setSQL(condition1 + tab1 + ","+tableName+" e ");
        pSQL.append("where "+condition2+" and a.device_status =1 and a.vendor_id=b.vendor_id " +
        		"and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        if(!"4".equals(gw_type)){
        	pSQL.append(" and e.user_state in ('1','2') ");
        }
        if (null != cityId && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        
        if("4".equals(gw_type)){
        	pSQL.append(" and e.loid = f.username and f.filename = '" + fileName + "'");
        }
        else{
        	pSQL.append(" and e.username = f.username and f.filename = '" + fileName + "'");
        }

        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        pSQL.append(" order by complete_time");
        return jt.query(pSQL.getSQL(), new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map4Wireless(map, rs);
            }
        });
    }

    //数据
    public int queryDeviceBynumber(String gw_type, long areaId, String cityId, 
    		List<String> userList, String fileName) 
    {
        logger.debug("queryDeviceByImportUsername()");
        //江苏查报表库
        setDataSourceType(LipossGlobals.inArea(Global.JSDX) ? "false" : "true", this.getClass().getName());
        String tableName = "tab_hgwcustomer";
        if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type)) {
            tableName = "tab_egwcustomer";
        }
        PrepareSQL pSQL = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("select count(*) ");
        }else{
        	pSQL.append("select count(1) ");
        }
        pSQL.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,");
        pSQL.append("tab_seniorquery_tmp f,"+tableName+" e ");
        pSQL.append("where a.device_id=e.device_id and a.device_status =1 " +
        		"and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id " +
        		"and a.devicetype_id=d.devicetype_id and e.user_state in ('1','2') ");
        if (null != cityId && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        pSQL.append(" and e.username = f.username and f.filename = '" + fileName + "'");

        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        pSQL.append(" order by complete_time");
        return jt.queryForInt(pSQL.getSQL());
    }

    /**
     * @param areaId
     * @param cityId
     * @param userList
     * @return
     */
    public List queryDeviceByImportUsernameForDoubleParam(String gw_type, long areaId, 
    		String cityId, List<String> userList, String fileName) 
    {
        logger.debug("queryDeviceByImportUsername()");
        //江苏查报表库
        setDataSourceType(LipossGlobals.inArea(Global.JSDX) ? "false" : "true", this.getClass().getName());
        String tableName = "tab_hgwcustomer";
        if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type)) {
            tableName = "tab_egwcustomer";
        }
        PrepareSQL pSQL = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("select a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,a.office_id,a.complete_time,"
        				+ "a.zone_id,a.buy_time,a.staff_id,a.remark,a.loopback_ip,a.interface_id,a.device_status,a.gather_id,"
        				+ "a.devicetype_id,a.maxenvelopes,a.cr_port,a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,"
        				+ "a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,a.acs_passwd,a.device_type,"
        				+ "a.x_com_username,a.x_com_passwd,a.gw_type,a.device_model_id,a.customer_id,"
        				+ "a.device_url,a.x_com_passwd_old,a.vendor_id,user_id,ssid,vlanid,priority,channel,");
        	if (LipossGlobals.inArea(Global.JSDX)) {
        		pSQL.append("wlanport,paramvalue,");
        	}
        }else{
        	pSQL.append("select a.*,f.*,");
        }
        pSQL.append("b.vendor_add,c.device_model,d.softwareversion,e.username " +
        		"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,");
        pSQL.append("tab_seniorquery_tmp f,"+tableName+" e ");
        pSQL.append("where a.device_id=e.device_id and a.device_status =1 " +
        		"and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id " +
        		"and a.devicetype_id=d.devicetype_id and e.user_state in ('1','2') ");
        if (null != cityId && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        pSQL.append(" and e.username = f.username and f.filename = '" + fileName + "'");

        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        pSQL.append(" order by complete_time");
        return jt.query(pSQL.getSQL(), new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map4Wireless(map, rs);
            }
        });
    }

    /**
     * @param areaId
     * @param cityId
     * @param userList
     * @return
     */
    public List queryDeviceByImportKDUsername(String gw_type, long areaId, 
    		String cityId, List<String> userList, String fileName) 
    {
        logger.debug("queryDeviceByImportUsername()");
        //江苏查报表库
        setDataSourceType(LipossGlobals.inArea(Global.JSDX) ? "false" : "true", this.getClass().getName());
        String tableName = "tab_hgwcustomer";
        String tableServ = "hgwcust_serv_info";
        if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type)) {
            tableName = "tab_egwcustomer";
            tableServ = "egwcust_serv_info";
        }
        PrepareSQL pSQL = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("select a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,a.office_id,a.complete_time,"
        				+ "a.zone_id,a.buy_time,a.staff_id,a.remark,a.loopback_ip,a.interface_id,a.device_status,a.gather_id,"
        				+ "a.devicetype_id,a.maxenvelopes,a.cr_port,a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,"
        				+ "a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,a.acs_passwd,a.device_type,"
        				+ "a.x_com_username,a.x_com_passwd,a.gw_type,a.device_model_id,a.customer_id,"
        				+ "a.device_url,a.x_com_passwd_old,a.vendor_id,user_id,ssid,vlanid,priority,channel,");
        	if (LipossGlobals.inArea(Global.JSDX)) {
        		pSQL.append("wlanport,paramvalue,");
        	}
        }else{
        	pSQL.append("select a.*,f.*,");
        }
        pSQL.append("b.vendor_add,c.device_model,d.softwareversion,g.username " +
        		"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,");
        pSQL.append("tab_seniorquery_tmp f,"+tableName+" e, "+tableServ+" g ");
        pSQL.append("where a.device_id=e.device_id and a.device_status =1 and a.vendor_id=b.vendor_id " +
        		"and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        pSQL.append(" and e.user_state in ('1','2') ");
        if (null != cityId && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        pSQL.append(" and g.username = f.username and g.user_id = e.user_id " +
        		"and g.serv_type_id=10 and f.filename = '" + fileName + "'");
        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        pSQL.append(" order by complete_time");
        return jt.query(pSQL.getSQL(), new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map4Wireless(map, rs);
            }
        });
    }

    //数据
    public int queryDeviceByKDnumber(String gw_type, long areaId, String cityId, 
    		List<String> userList, String fileName)
    {
        logger.debug("queryDeviceByImportUsername()");
        //江苏查报表库
        setDataSourceType(LipossGlobals.inArea(Global.JSDX) ? "false" : "true", this.getClass().getName());
        String tableName = "tab_hgwcustomer";
        String tableServ = "hgwcust_serv_info";
        if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type)) {
            tableName = "tab_egwcustomer";
            tableServ = "egwcust_serv_info";
        }
        PrepareSQL pSQL = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("select count(*) ");
        }else{
        	pSQL.append("select count(1) ");
        }
        pSQL.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d, tab_seniorquery_tmp f,");
        pSQL.append(tableName+" e, "+tableServ+" g ");
        pSQL.append("where a.device_id=e.device_id and a.device_status =1 and a.vendor_id=b.vendor_id " +
        		"and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        pSQL.append(" and e.user_state in ('1','2') ");
        if (null != cityId && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        pSQL.append(" and g.username = f.username and g.user_id = e.user_id " +
        		"and g.serv_type_id=10 and f.filename = '" + fileName + "'");
        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        pSQL.append(" order by complete_time");
        return jt.queryForInt(pSQL.getSQL());
    }

    /**
     * @param areaId
     * @param cityId
     * @param userList
     * @return
     */
    public List queryDeviceByImportDevicesnForDoubleParam(String gw_type, 
    		long areaId, String cityId, List<String> devidesnList, String fileName) 
    {
        logger.debug("queryDeviceByImportDevicesn()");
        //江苏查报表库
        setDataSourceType(LipossGlobals.inArea(Global.JSDX) ? "false" : "true", this.getClass().getName());
        PrepareSQL pSQL = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("select a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,a.office_id,a.complete_time,"
        				+ "a.zone_id,a.buy_time,a.staff_id,a.remark,a.loopback_ip,a.interface_id,a.device_status,a.gather_id,"
        				+ "a.devicetype_id,a.maxenvelopes,a.cr_port,a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,"
        				+ "a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,a.acs_passwd,a.device_type,"
        				+ "a.x_com_username,a.x_com_passwd,a.gw_type,a.device_model_id,a.customer_id,"
        				+ "a.device_url,a.x_com_passwd_old,a.vendor_id,a.user_id,ssid,vlanid,priority,channel,username,");
        	if (LipossGlobals.inArea(Global.JSDX)) {
        		pSQL.append("wlanport,paramvalue,");
        	}
        }else{
        	pSQL.append("select a.*,f.*,");
        }
        pSQL.setSQL("b.vendor_add,c.device_model,d.softwareversion from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d, tab_seniorquery_tmp f ");
        pSQL.append(" where a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        if (null != cityId && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        pSQL.append(" and a.device_serialnumber = f.devicesn and f.filename ='" + fileName + "'");
        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        pSQL.append(" order by complete_time");
        return jt.query(pSQL.getSQL(), new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map4Wireless(map, rs);
            }
        });
    }

    /**
     * @param areaId
     * @param cityId
     * @param userList
     * @return
     */

    public List queryDeviceByImportDevicesn(String gw_type, long areaId, 
    		String cityId, List<String> devidesnList, String fileName) 
    {
        logger.debug("queryDeviceByImportDevicesn()");
        //江苏查报表库
        setDataSourceType(LipossGlobals.inArea(Global.JSDX) ? "false" : "true", this.getClass().getName());
        PrepareSQL pSQL = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("select a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,a.office_id,a.complete_time,"
        				+ "a.zone_id,a.buy_time,a.staff_id,a.remark,a.loopback_ip,a.interface_id,a.device_status,a.gather_id,"
        				+ "a.devicetype_id,a.maxenvelopes,a.cr_port,a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,"
        				+ "a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,a.acs_passwd,a.device_type,"
        				+ "a.x_com_username,a.x_com_passwd,a.gw_type,a.device_model_id,a.customer_id,"
        				+ "a.device_url,a.x_com_passwd_old,a.vendor_id,a.user_id,ssid,vlanid,priority,channel,username,");
        	if (LipossGlobals.inArea(Global.JSDX)) {
        		pSQL.append("wlanport,paramvalue,");
        	}
        }else{
        	pSQL.append("select a.*,f.*,");
        }
        pSQL.append("b.vendor_add,c.device_model,d.softwareversion ");
        
        if("4".equals(gw_type)){
        	pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d, stb_tab_seniorquery_tmp f ");
        }
        else{
        	pSQL.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d, tab_seniorquery_tmp f ");
        }
        pSQL.append(" where a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        if (null != cityId && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        pSQL.append(" and a.device_serialnumber = f.devicesn and f.filename ='" + fileName + "'");
        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        pSQL.append(" order by complete_time");
        return jt.query(pSQL.getSQL(), new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map4Wireless(map, rs);
            }
        });
    }

    public int queryDeviceByDevicesn(String gw_type, long areaId, String cityId, 
    		List<String> devidesnList, String fileName) 
    {
        logger.debug("queryDeviceByImportDevicesn()");
        //江苏查报表库
        setDataSourceType(LipossGlobals.inArea(Global.JSDX) ? "false" : "true", this.getClass().getName());
        PrepareSQL pSQL = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("select count(*) ");
        }else{
        	pSQL.append("select count(1) ");
        }
        pSQL.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d, tab_seniorquery_tmp f ");
        pSQL.append(" where a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        if (null != cityId && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        pSQL.append(" and a.device_serialnumber = f.devicesn and f.filename ='" + fileName + "'");
        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        pSQL.append(" order by complete_time");
        return jt.queryForInt(pSQL.getSQL());
    }

    public int ByFieldOr(String gw_type, long areaId, String queryParam, String cityId) 
    {
        PrepareSQL pSQL = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("select count(*) ");
        }else{
        	pSQL.append("select count(1) ");
        }
        pSQL.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d where a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        if (null != queryParam && !"".equals(queryParam) && !"-1".equals(queryParam)) {
            if (queryParam.length() > 5) {
                pSQL.append(" and a.dev_sub_sn ='");
                pSQL.append(queryParam.substring(queryParam.length() - 6, queryParam.length()));
                pSQL.append("' ");
            }
            pSQL.append(" and (a.device_serialnumber like '%");
            pSQL.append(queryParam);
            pSQL.append("%' or a.loopback_ip like '%");
            pSQL.append(queryParam);
            pSQL.append("%') ");
        }
        if (null != cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        return jt.queryForInt(pSQL.getSQL());
    }

    public List<Map> isawifiDeviceByFieldOr(String gw_type, long areaId, String queryParam, String cityId) 
    {
        PrepareSQL pSQL = new PrepareSQL();
        /**	if(DBUtil.GetDB()==3){
        	//TODO wait
        }else{
        	
        }*/
        pSQL.setSQL("select d.is_awifi from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d where a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        if (null != queryParam && !"".equals(queryParam) && !"-1".equals(queryParam)) {
            if (queryParam.length() > 5) {
                pSQL.append(" and a.dev_sub_sn ='");
                pSQL.append(queryParam.substring(queryParam.length() - 6, queryParam.length()));
                pSQL.append("' ");
            }
            pSQL.append(" and (a.device_serialnumber like '%");
            pSQL.append(queryParam);
            pSQL.append("%' or a.loopback_ip like '%");
            pSQL.append(queryParam);
            pSQL.append("%') ");
        }
        if (null != cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        List list = jt.queryForList(pSQL.getSQL());
        return list;
    }

    /**
     * 查询设备列表(根据设备序列号和设备IP用or以及like来匹配)
     *
     * @param areaId
     * @param queryParam
     * @return
     */
    public List queryDeviceByFieldOr(String gw_type, long areaId, String queryParam, String cityId) 
    {
        logger.debug("GwDeviceQueryDAO=>queryDeviceByFieldOr(areaId:{},queryParam:{})", areaId, queryParam);
        PrepareSQL pSQL = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	 pSQL.append("select a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,a.office_id,"
        			 	+ "a.complete_time,a.zone_id,a.buy_time,a.staff_id,a.remark,a.loopback_ip,"
        			 	+ "a.interface_id,a.device_status,a.gather_id,a.devicetype_id,"
        			 	+ "a.maxenvelopes,a.cr_port,a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,"
        			 	+ "a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,a.acs_passwd,"
        			 	+ "a.device_type,a.x_com_username,a.x_com_passwd,a.gw_type,a.device_model_id,"
        			 	+ "a.customer_id,a.device_url,a.x_com_passwd_old,a.vendor_id,");
        }else{
        	 pSQL.append("select a.*,");
        }
        pSQL.append("b.vendor_add,c.device_model,d.softwareversion,d.is_awifi " +
        		"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d " +
        		"where a.device_status =1 and a.vendor_id=b.vendor_id " +
        		"and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        if (null != queryParam && !"".equals(queryParam)) {
            pSQL.append(" and (a.device_serialnumber like '%"+queryParam+"%' ");
            pSQL.append("or a.loopback_ip like '%"+queryParam+"%') ");
        }
        if (null != cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        //		if( 1!=areaId ) {
        //			pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
        //			pSQL.append(String.valueOf(areaId));
        //			pSQL.append(") ");
        //		}
        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        pSQL.append(" order by a.complete_time");
        return jt.query(pSQL.getSQL(), new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map(map, rs);
            }
        });
    }

    /**
     * 查询设备列表(根据设备序列号和设备IP用or以及like来匹配)
     *
     * @param areaId
     * @param queryParam
     * @return
     */
    public List queryDeviceByFieldOr(String gw_type, int curPage_splitPage, 
    		int num_splitPage, long areaId, String queryParam, String cityId) 
    {
        logger.debug("GwDeviceQueryDAO=>queryDeviceByFieldOr(areaId:{},queryParam:{})", areaId, queryParam);
        PrepareSQL pSQL = new PrepareSQL();
        if (DBUtil.GetDB() == 2) {
            pSQL.append("select top " + maxNum+" ");
        } else {
            pSQL.append("select ");
        }
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,a.office_id,"
        				+ "a.complete_time,a.zone_id,a.buy_time,a.staff_id,a.remark,a.loopback_ip,"
        				+ "a.interface_id,a.device_status,a.gather_id,a.devicetype_id,"
        				+ "a.maxenvelopes,a.cr_port,a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,"
        				+ "a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,a.acs_passwd,"
        				+ "a.device_type,a.x_com_username,a.x_com_passwd,a.gw_type,a.device_model_id,"
        				+ "a.customer_id,a.device_url,a.x_com_passwd_old,a.vendor_id,");
        }else{
        	pSQL.append("a.*,");
        }
        pSQL.append("b.vendor_add,c.device_model,d.softwareversion,d.is_awifi " +
        		"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d " +
        		"where a.device_status =1 and a.vendor_id=b.vendor_id " +
        		"and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        if (null != queryParam && !"".equals(queryParam)) {
            pSQL.append(" and (a.device_serialnumber like '%");
            pSQL.append(queryParam);
            pSQL.append("%' or a.loopback_ip like '%");
            pSQL.append(queryParam);
            pSQL.append("%') ");
        }
        if (null != cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        //		if( 1!=areaId ) {
        //			pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
        //			pSQL.append(String.valueOf(areaId));
        //			pSQL.append(") ");
        //		}
        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        if (DBUtil.GetDB() == 1) {
            pSQL.append(" and rownum< " + maxNum);
        }else if(DBUtil.GetDB() == 3){
        	pSQL.append(" and limit " + maxNum);
        }
        pSQL.append(" order by a.complete_time");
        return jt.querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage, num_splitPage, new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map(map, rs);
            }
        });
    }


    public List queryDeviceByFieldOrForSxlt(String gw_type, int curPage_splitPage, 
    		int num_splitPage, long areaId,
    		String queryParam, String cityId,String startTime,String endTime,String timeType) 
    {
        logger.debug("GwDeviceQueryDAO=>queryDeviceByFieldOr(areaId:{},queryParam:{})", areaId, queryParam);
        PrepareSQL pSQL = new PrepareSQL();
        if (DBUtil.GetDB() == 2) {
            pSQL.append("select top " + maxNum+" ");
        } else {
            pSQL.append("select ");
        }
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,a.office_id,"
        				+ "a.complete_time,a.zone_id,a.buy_time,a.staff_id,a.remark,a.loopback_ip,"
        				+ "a.interface_id,a.device_status,a.gather_id,a.devicetype_id,"
        				+ "a.maxenvelopes,a.cr_port,a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,"
        				+ "a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,a.acs_passwd,"
        				+ "a.device_type,a.x_com_username,a.x_com_passwd,a.gw_type,a.device_model_id,"
        				+ "a.customer_id,a.device_url,a.x_com_passwd_old,a.vendor_id,");
        }else{
        	pSQL.append("a.*,");
        }
        pSQL.append("b.vendor_add,c.device_model,d.softwareversion,d.is_awifi " +
        		"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d " +
        		"where a.device_status =1 and a.vendor_id=b.vendor_id " +
        		"and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        if (null != queryParam && !"".equals(queryParam)) {
            pSQL.append(" and (a.device_serialnumber like '%");
            pSQL.append(queryParam);
            pSQL.append("%' or a.loopback_ip like '%");
            pSQL.append(queryParam);
            pSQL.append("%') ");
        }
        if (null != cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        //		if( 1!=areaId ) {
        //			pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
        //			pSQL.append(String.valueOf(areaId));
        //			pSQL.append(") ");
        //		}
        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        if ("1".equals(timeType)) {
            if (false == com.linkage.module.gwms.util.StringUtil.IsEmpty(startTime)) {
                pSQL.append(" and c.complete_time>=" + startTime);
            }
            if (false == com.linkage.module.gwms.util.StringUtil.IsEmpty(endTime)) {
                pSQL.append(" and c.complete_time<=" + endTime);
            }
        }
        if (DBUtil.GetDB() == 1) {
            pSQL.append(" and rownum< " + maxNum);
        }else if(DBUtil.GetDB() == 3){
        	pSQL.append(" and limit " + maxNum);
        }
        pSQL.append(" order by a.complete_time");
        return jt.querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage, num_splitPage, new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map(map, rs);
            }
        });
    }

    /**
     * 查询设备列表(条数)(根据设备序列号和设备IP用or以及like来匹配)
     *
     * @param areaId
     * @param queryParam
     * @return
     */
    public int queryDeviceByFieldOrCount(String gw_type, long areaId, 
    		String queryParam, String cityId) 
    {
        logger.debug("GwDeviceQueryDAO=>queryDeviceByFieldOrCount(areaId:{},queryParam:{})", areaId, queryParam);
        PrepareSQL pSQL = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("select count(*) ");
        }else{
        	pSQL.append("select count(1) ");
        }
        pSQL.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d " +
        		"where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        if (null != queryParam && !"".equals(queryParam)) {
            pSQL.append(" and (a.device_serialnumber like '%");
            pSQL.append(queryParam);
            pSQL.append("%' or a.loopback_ip like '%");
            pSQL.append(queryParam);
            pSQL.append("%') ");
        }
        if (null != cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        //		if( 1!=areaId ) {
        //			pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
        //			pSQL.append(String.valueOf(areaId));
        //			pSQL.append(") ");
        //		}

        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        return jt.queryForInt(pSQL.getSQL());
    }

    public int queryDeviceByFieldOrCountForSxlt(String gw_type, long areaId, 
    		String queryParam, String cityId,String startTime, String endTime, String timeType) 
    {
        logger.debug("GwDeviceQueryDAO=>queryDeviceByFieldOrCount(areaId:{},queryParam:{})", 
        		areaId, queryParam);
        PrepareSQL pSQL = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("select count(*) ");
        }else{
        	pSQL.append("select count(1) ");
        }
        pSQL.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d " +
        		"where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        if (null != queryParam && !"".equals(queryParam)) {
            pSQL.append(" and (a.device_serialnumber like '%");
            pSQL.append(queryParam);
            pSQL.append("%' or a.loopback_ip like '%");
            pSQL.append(queryParam);
            pSQL.append("%') ");
        }
        if (null != cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        //		if( 1!=areaId ) {
        //			pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
        //			pSQL.append(String.valueOf(areaId));
        //			pSQL.append(") ");
        //		}

        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        if ("1".equals(timeType)) {
            if (false == com.linkage.module.gwms.util.StringUtil.IsEmpty(startTime)) {
                pSQL.append(" and a.complete_time>=" + startTime);
            }
            if (false == com.linkage.module.gwms.util.StringUtil.IsEmpty(endTime)) {
                pSQL.append(" and a.complete_time<=" + endTime);
            }
        }
        /*if ("2".equals(timeType)) {
            pSQL.setSQL("select count(1) from tab_gw_device a left join tab_hgwcustomer b on a.device_id=b.device_id where 1=1 ");
            if (false == com.linkage.module.gwms.util.StringUtil.IsEmpty(startTime)) {
                pSQL.append(" and b.binddate>=" + startTime);
            }
            if (false == com.linkage.module.gwms.util.StringUtil.IsEmpty(endTime)) {
                pSQL.append(" and b.binddate<=" + endTime);
            }
        }*/

        return jt.queryForInt(pSQL.getSQL());
    }

    public List<Map> isawifiDevice(String gw_type, long areaId,
    		String cityId, String vendorId, String deviceModelId, String devicetypeId,
            String bindType, String deviceSerialnumber, String deviceIp) 
    {
        PrepareSQL pSQL = new PrepareSQL();
        pSQL.setSQL("select d.is_awifi from tab_gw_device a,tab_devicetype_info d where a.device_status =1 and a.devicetype_id=d.devicetype_id ");
        if (null != deviceSerialnumber && !"".equals(deviceSerialnumber) && !"-1".equals(deviceSerialnumber)) {
            if (deviceSerialnumber.length() > 5) {
                pSQL.append(" and a.dev_sub_sn ='");
                pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6, deviceSerialnumber.length()));
                pSQL.append("' ");
            }
            pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE, deviceSerialnumber, false);
        }
        if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
            pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp, false);
        }
        return jt.queryForList(pSQL.getSQL());
    }

    public int queryaccount_number(String gw_type, long areaId,
    			String cityId, String vendorId, String deviceModelId, String devicetypeId,
                String bindType, String deviceSerialnumber, String deviceIp) 
    {
        PrepareSQL pSQL = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	pSQL.append("select count(*) ");
        }else{
        	pSQL.append("select count(1) ");
        }
        pSQL.append("from tab_gw_device a where a.device_status =1");
        if (null != deviceSerialnumber && !"".equals(deviceSerialnumber) && !"-1".equals(deviceSerialnumber)) {
            if (deviceSerialnumber.length() > 5) {
                pSQL.append(" and a.dev_sub_sn ='");
                pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6, deviceSerialnumber.length()));
                pSQL.append("' ");
            }
            pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE, deviceSerialnumber, false);
        }
        if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
            pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp, false);
        }
        return jt.queryForInt(pSQL.getSQL());
    }

    /**
     * 查询设备列表(单独根据设备表的条件查询)
     *
     * @param areaId
     * @param queryParam
     * @return
     */
    public List queryDevice(String gw_type, long areaId,
    		String cityId, String vendorId, String deviceModelId, String devicetypeId,
            String bindType, String deviceSerialnumber, String deviceIp, 
            String gwShare_start_ip, String gwShare_end_ip, String online_time,String gwShare_netSpeed)
    {
    	logger.debug("GwDeviceQueryDAO=>queryDevice()");
        PrepareSQL pSQL = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("select a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,a.office_id,"
        				+ "a.complete_time,a.zone_id,a.buy_time,a.staff_id,a.remark,a.loopback_ip,"
        				+ "a.interface_id,a.device_status,a.gather_id,a.devicetype_id,"
        				+ "a.maxenvelopes,a.cr_port,a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,"
        				+ "a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,acs_passwd,"
        				+ "a.device_type,a.x_com_username,a.x_com_passwd,a.gw_type,a.device_model_id,"
        				+ "a.customer_id,a.device_url,a.x_com_passwd_old,a.vendor_id,");
        }else{
        	pSQL.append("select a.*,");
        }
        if (LipossGlobals.inArea(Global.GSDX) && !StringUtil.IsEmpty(online_time)) {
            pSQL.append("b.vendor_add,c.device_model,d.softwareversion,d.is_awifi ");
            pSQL.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d ,gw_devicestatus e where a.device_status =1 and a.device_id=e.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        } else {
        	if("4".equals(gw_type)){
        		pSQL.append("b.vendor_add,c.device_model,d.softwareversion from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        	}
        	else{
                if(LipossGlobals.inArea(Global.JLLT)&& !"".equals(gwShare_netSpeed) && !"-1".equals(gwShare_netSpeed)
                        && !"null".equals(gwShare_netSpeed) && null != gwShare_netSpeed){
                    pSQL.append("b.vendor_add,c.device_model,d.softwareversion,d.is_awifi " +
                            " from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d ,tab_speed_dev_rate e " +
                            " where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id " +
                            " and a.devicetype_id=d.devicetype_id and e.user_id = a.customer_id  ");
                }
                else
                {
                    pSQL.append("b.vendor_add,c.device_model,d.softwareversion,d.is_awifi from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
                }
        	}
        }

        //这个是重庆软件升级的分支，因为bio方法有很多地方调用，所以没有直接传参当标识符，用以,结尾的判断
        if (",".equals(cityId.substring(cityId.length() - 1))) {
            pSQL.append(" and a.city_id in (" + cityId.substring(0, cityId.length() - 1) + ")");
        } else {
            if (null != cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
                ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
                pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
                cityArray = null;
            }
        }
        if (null != vendorId && !"null".equals(vendorId) && !"".equals(vendorId) && !"-1".equals(vendorId)) {
            pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
        }
        if (null != deviceModelId && !"null".equals(deviceModelId) && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
            pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
        }
        if (null != devicetypeId && !"null".equals(devicetypeId) && !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
            pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL, devicetypeId);
        }
        if (null != bindType && !"".equals(bindType) && !"-1".equals(bindType)) {
            pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL, bindType);
        }
        if (null != deviceSerialnumber && !"".equals(deviceSerialnumber) && !"-1".equals(deviceSerialnumber)) {
            if (deviceSerialnumber.length() > 5) {
                pSQL.append(" and a.dev_sub_sn ='");
                pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6, deviceSerialnumber.length()));
                pSQL.append("' ");
            }
            pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE, deviceSerialnumber, false);
        }
        if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
            pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp, false);
        }
        /**
         * 重庆软件升级新增ip范围过滤
         */
        if (null != gwShare_start_ip && !"".equals(gwShare_start_ip) && !"-1".equals(gwShare_start_ip)) {
            pSQL.append(" and transIp(a.loopback_ip) >= " + conventIP(gwShare_start_ip));
        }
        if (null != gwShare_end_ip && !"".equals(gwShare_end_ip) && !"-1".equals(gwShare_end_ip)) {
            pSQL.append(" and transIp(a.loopback_ip) <= " + conventIP(gwShare_end_ip));
        }
        //		if( 1!=areaId ) {
        //			pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
        //			pSQL.append(String.valueOf(areaId));
        //			pSQL.append(") ");
        //		}
        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }

        if (LipossGlobals.inArea(Global.GSDX) && !StringUtil.IsEmpty(online_time)) {
            pSQL.append(" and e.reboot_time <= " + online_time + " and e.reboot_time >0");
        }

        if (null != gwShare_netSpeed && !"null".equals(gwShare_netSpeed) && !"".equals(gwShare_netSpeed) && !"-1".equals(gwShare_netSpeed)) {
            int netSpeed = StringUtil.getIntegerValue(gwShare_netSpeed) * 1024;
            pSQL.append(" and e.rate = ? ");
            pSQL.setInt(1,netSpeed);
        }

        pSQL.append(" order by a.complete_time");
        return jt.query(pSQL.toString(), new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map(map, rs);
            }
        });
    }


    /**
     * 查询设备列表(单独根据设备表的条件查询)
     *
     * @param areaId
     * @param queryParam
     * @return
     */
    public List queryDeviceBySQL(String gwShare_matchSQL) 
    {
        logger.debug("GwDeviceQueryDAO=>queryDeviceBySQL()");
        PrepareSQL pSQL = new PrepareSQL();
        pSQL.setSQL(gwShare_matchSQL);
        pSQL.append(" order by a.complete_time");
        return jt.query(pSQL.getSQL(), new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map(map, rs);
            }
        });
    }

    /**
     * 高级查询设备列表SQL(单独根据设备表的条件查询)
     *
     * @param areaId
     * @param iscqsoft
     * @param queryParam
     * @return
     */
    public String queryDeviceSQL(String gw_type, long areaId,
    		String cityId, String vendorId, String deviceModelId, String devicetypeId,
            String bindType, String deviceSerialnumber, String deviceIp, 
            String iscqsoft, String gwShare_start_ip, String gwShare_end_ip,String gwShare_netSpeed)
    {
    	logger.debug("GwDeviceQueryDAO=>queryDeviceSQL()");
        PrepareSQL pSQL = new PrepareSQL();
    	if(DBUtil.GetDB()==3){
    		//TODO wait
    		pSQL.append("select a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,a.office_id,"
    				+ "a.complete_time,a.zone_id,a.buy_time,a.staff_id,a.remark,a.loopback_ip,"
    				+ "a.interface_id,a.device_status,a.gather_id,a.devicetype_id,"
    				+ "a.maxenvelopes,a.cr_port,a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,"
    				+ "a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,acs_passwd,"
    				+ "a.device_type,a.x_com_username,a.x_com_passwd,a.gw_type,a.device_model_id,"
    				+ "a.customer_id,a.device_url,a.x_com_passwd_old,a.vendor_id,");
    	}else{
    		pSQL.append("select a.*,");
    	}
    	
        if(LipossGlobals.inArea(Global.JLLT)  && !"".equals(gwShare_netSpeed) && !"-1".equals(gwShare_netSpeed) && !"null".equals(gwShare_netSpeed) && null != gwShare_netSpeed)
        {
            pSQL.append("b.vendor_add,c.device_model,d.softwareversion from tab_gw_device a,tab_vendor b,gw_device_model c," +
                    " tab_devicetype_info d ,tab_speed_dev_rate e where a.device_status =1 and  a.vendor_id=b.vendor_id " +
                    " and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id and e.user_id = a.customer_id  ");
        }
        else
        {
            pSQL.append("b.vendor_add,c.device_model,d.softwareversion from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        }

        if ("true".equals(iscqsoft)) {
            pSQL.append(" and a.city_id in (" + cityId + ")");
        } else {
            if (null != cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
                ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
                pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
                cityArray = null;
            }
        }

        if (null != vendorId && !"null".equals(vendorId) && !"".equals(vendorId) && !"-1".equals(vendorId)) {
            pSQL.append(" and a.vendor_id='" + vendorId + "'");
        }
        if (null != deviceModelId && !"null".equals(deviceModelId) && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
            pSQL.append(" and a.device_model_id='" + deviceModelId + "'");
        }
        if (null != devicetypeId && !"null".equals(devicetypeId) && !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
            pSQL.append(" and a.devicetype_id=" + devicetypeId);
        }
        if (null != bindType && !"".equals(bindType) && !"-1".equals(bindType)) {
            pSQL.append(" and a.cpe_allocatedstatus=" + bindType);
        }
        if (null != deviceSerialnumber && !"".equals(deviceSerialnumber) && !"-1".equals(deviceSerialnumber)) {
            if (deviceSerialnumber.length() > 5) {
                pSQL.append(" and a.dev_sub_sn ='");
                pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6, deviceSerialnumber.length()));
                pSQL.append("' ");
            }
            pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE, deviceSerialnumber, false);
        }
        if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
            pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp, false);
        }
        /**
         * 重庆软件升级新增ip范围过滤
         */
        if (null != gwShare_start_ip && !"".equals(gwShare_start_ip) && !"-1".equals(gwShare_start_ip)) {
            pSQL.append(" and transIp(a.loopback_ip) >= " + conventIP(gwShare_start_ip));
        }
        if (null != gwShare_end_ip && !"".equals(gwShare_end_ip) && !"-1".equals(gwShare_end_ip)) {
            pSQL.append(" and transIp(a.loopback_ip) <= " + conventIP(gwShare_end_ip));
        }
        //		if( 1!=areaId ) {
        //			pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
        //			pSQL.append(String.valueOf(areaId));
        //			pSQL.append(") ");
        //		}
        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }

        if (null != gwShare_netSpeed && !"null".equals(gwShare_netSpeed) && !"".equals(gwShare_netSpeed) && !"-1".equals(gwShare_netSpeed)) {
            int netSpeed = StringUtil.getIntegerValue(gwShare_netSpeed) * 1024;
            pSQL.append(" and e.rate = ? ");
            pSQL.setInt(1,netSpeed);
        }
        return pSQL.getSQL().trim();
    }

    public String queryDeviceSQL2(String gw_type, long areaId,
    		String cityId, String vendorId, String deviceModelId, String devicetypeId,
            String bindType, String sn) 
    {
        logger.debug("GwDeviceQueryDAO=>queryDeviceSQL2()");
        PrepareSQL pSQL = new PrepareSQL();

        String table_customer = "tab_hgwcustomer";

        if ("2".equals(gw_type)) {
            table_customer = "tab_egwcustomer";
        }

        pSQL.append("select a.device_id,a.device_model_id,a.devicetype_id,b.username ");
        pSQL.append("from tab_gw_device a," + table_customer + " b ");
        pSQL.append("where a.device_status=1 and a.device_id=b.device_id ");
        if (!StringUtil.IsEmpty(cityId) && !"null".equals(cityId) && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        if (!StringUtil.IsEmpty(vendorId) && !"null".equals(vendorId) && !"-1".equals(vendorId)) {
            pSQL.append(" and a.vendor_id='" + vendorId + "'");
        }
        if (!StringUtil.IsEmpty(deviceModelId) && !"null".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
            pSQL.append(" and a.device_model_id='" + deviceModelId + "'");
        }
        if (!StringUtil.IsEmpty(devicetypeId) && !"null".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
            pSQL.append(" and a.devicetype_id=" + devicetypeId);
        }
        if (!StringUtil.IsEmpty(bindType) && !"-1".equals(bindType)) {
            pSQL.append(" and a.cpe_allocatedstatus=" + bindType);
        }
        if (!StringUtil.IsEmpty(sn) && !"-1".equals(sn)) {
            if (sn.length() > 5) {
                pSQL.append(" and a.dev_sub_sn='" + sn.substring(sn.length() - 6, sn.length()) + "' ");
            }
            pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE, sn, false);
        }

        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type=" + gw_type);
        }
        return pSQL.getSQL().trim();
    }

    /**
     * 查询设备列表(单独根据设备表的条件查询)
     *
     * @param areaId
     * @param queryParam
     * @return
     */
    public List queryDevice(String gw_type, int curPage_splitPage, int num_splitPage, long areaId,
    		String cityId, String vendorId, String deviceModelId, String devicetypeId,
            String bindType, String deviceSerialnumber, String deviceIp, 
            String gwShare_start_ip, String gwShare_end_ip, String online_time,String gwShare_netSpeed)
    {
    	logger.debug("GwDeviceQueryDAO=>queryDevice()");
        PrepareSQL pSQL = new PrepareSQL();
        if (DBUtil.GetDB() == 2) {
            pSQL.append("select top " + maxNum+" ");
        } else {
            pSQL.append("select ");
        }

        if(DBUtil.GetDB()==3){
    		//TODO wait
    		pSQL.append("a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,a.office_id,"
    				+ "a.complete_time,a.zone_id,a.buy_time,a.staff_id,a.remark,a.loopback_ip,"
    				+ "a.interface_id,a.device_status,a.gather_id,a.devicetype_id,"
    				+ "a.maxenvelopes,a.cr_port,a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,"
    				+ "a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,a.acs_passwd,"
    				+ "a.device_type,a.x_com_username,a.x_com_passwd,a.gw_type,a.device_model_id,"
    				+ "a.customer_id,a.device_url,a.x_com_passwd_old,a.vendor_id,");
    	}else{
    		pSQL.append("a.*,");
    	}
        
        if (LipossGlobals.inArea(Global.GSDX) && !StringUtil.IsEmpty(online_time)) {
            pSQL.append("b.vendor_add,c.device_model,d.softwareversion,d.is_awifi");
            pSQL.append(" from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,gw_devicestatus e where a.device_status =1 and a.device_id=e.device_id and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        } else {
        	if("4".equals(gw_type)){
        		pSQL.append("b.vendor_add,c.device_model,d.softwareversion from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        	}
        	else{

                if(LipossGlobals.inArea(Global.JLLT) && !"".equals(gwShare_netSpeed) && !"-1".equals(gwShare_netSpeed)
                        && !"null".equals(gwShare_netSpeed) && null != gwShare_netSpeed){
                    pSQL.append("b.vendor_add,c.device_model,d.softwareversion,d.is_awifi " +
                            " from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d ,tab_speed_dev_rate e " +
                            " where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id " +
                            " and a.devicetype_id=d.devicetype_id and e.user_id = a.customer_id ");
                }
        	    else
                {
                    pSQL.append("b.vendor_add,c.device_model,d.softwareversion,d.is_awifi from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
                }
        	}
        }

        //这个是重庆软件升级的分支，因为bio方法有很多地方调用，所以没有直接传参当标识符，用以,结尾的判断
        if (",".equals(cityId.substring(cityId.length() - 1))) {
            pSQL.append(" and a.city_id in (" + cityId.substring(0, cityId.length() - 1) + ")");
        } else {
            if (null != cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
                ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
                pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
                cityArray = null;
            }
        }
        if (null != vendorId && !"null".equals(vendorId) && !"".equals(vendorId) && !"-1".equals(vendorId)) {
            pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
        }
        if (null != deviceModelId && !"null".equals(deviceModelId) && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
            pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
        }
        if (null != devicetypeId && !"null".equals(devicetypeId) && !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
            pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL, devicetypeId);
        }
        if (null != bindType && !"".equals(bindType) && !"-1".equals(bindType)) {
            pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL, bindType);
        }
        if (null != deviceSerialnumber && !"".equals(deviceSerialnumber) && !"-1".equals(deviceSerialnumber)) {
            if (deviceSerialnumber.length() > 5) {
                pSQL.append(" and a.dev_sub_sn ='");
                pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6, deviceSerialnumber.length()));
                pSQL.append("' ");
            }
            pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE, deviceSerialnumber, false);
        }
        if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
            pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp, false);
        }
        /**
         * 重庆软件升级新增ip范围过滤
         */
        if (null != gwShare_start_ip && !"".equals(gwShare_start_ip) && !"-1".equals(gwShare_start_ip)) {
            pSQL.append(" and transIp(a.loopback_ip) >= " + conventIP(gwShare_start_ip));
        }
        if (null != gwShare_end_ip && !"".equals(gwShare_end_ip) && !"-1".equals(gwShare_end_ip)) {
            pSQL.append(" and transIp(a.loopback_ip) <= " + conventIP(gwShare_end_ip));
        }
        //		if( 1!=areaId ) {
        //			pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
        //			pSQL.append(String.valueOf(areaId));
        //			pSQL.append(") ");
        //		}
        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }

        if (null != gwShare_netSpeed && !"null".equals(gwShare_netSpeed) && !"".equals(gwShare_netSpeed) && !"-1".equals(gwShare_netSpeed)) {
            int netSpeed = StringUtil.getIntegerValue(gwShare_netSpeed) * 1024;
            pSQL.append(" and e.rate = ? ");
            pSQL.setInt(1,netSpeed);
        }

        if (DBUtil.GetDB() == 1) {
            pSQL.append(" and rownum< " + maxNum);
        }

        if (LipossGlobals.inArea(Global.GSDX) && !StringUtil.IsEmpty(online_time)) {
            pSQL.append(" and reboot_time <= " + online_time + " and e.reboot_time >0");
        }
        pSQL.append(" order by a.complete_time");
        return jt.querySP(pSQL.toString(), (curPage_splitPage - 1) * num_splitPage, num_splitPage, new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map(map, rs);
            }
        });
    }

    public List queryDeviceForSxlt(String gw_type, int curPage_splitPage, int num_splitPage, 
    		long areaId,String cityId, String vendorId, String deviceModelId, 
    		String devicetypeId,String bindType, String deviceSerialnumber, String deviceIp,
            String gwShare_start_ip, String gwShare_end_ip, String online_time,
            String startTime,String endTime,String timeType) 
    {
        logger.debug("GwDeviceQueryDAO=>queryDevice()");
        PrepareSQL pSQL = new PrepareSQL();
        
        if (DBUtil.GetDB() == 2) {
            pSQL.append("select top " + maxNum+" ");
        } else {
            pSQL.append("select ");
        }
        
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,a.office_id,"
        				+ "a.complete_time,a.zone_id,a.buy_time,a.staff_id,a.remark,a.loopback_ip,"
        				+ "a.interface_id,a.device_status,a.gather_id,a.devicetype_id,"
        				+ "a.maxenvelopes,a.cr_port,a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,"
        				+ "a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,a.acs_passwd,"
        				+ "a.device_type,a.x_com_username,a.x_com_passwd,a.gw_type,a.device_model_id,"
        				+ "a.customer_id,a.device_url,a.x_com_passwd_old,a.vendor_id,");
        }else{
        	pSQL.append("a.*,");
        }

        pSQL.append("b.vendor_add,c.device_model,d.softwareversion,d.is_awifi " +
        		"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d " +
        		"where a.device_status =1 and  a.vendor_id=b.vendor_id " +
        		"and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");

        if (null != cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        if (null != vendorId && !"null".equals(vendorId) && !"".equals(vendorId) && !"-1".equals(vendorId)) {
            pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
        }
        if (null != deviceModelId && !"null".equals(deviceModelId) && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
            pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
        }
        if (null != devicetypeId && !"null".equals(devicetypeId) && !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
            pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL, devicetypeId);
        }
        if (null != bindType && !"".equals(bindType) && !"-1".equals(bindType)) {
            pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL, bindType);
        }
        if (null != deviceSerialnumber && !"".equals(deviceSerialnumber) && !"-1".equals(deviceSerialnumber)) {
            if (deviceSerialnumber.length() > 5) {
                pSQL.append(" and a.dev_sub_sn ='");
                pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6, deviceSerialnumber.length()));
                pSQL.append("' ");
            }
            pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.LIKE, deviceSerialnumber, false);
        }
        if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
            pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp, false);
        }
        /**
         * 重庆软件升级新增ip范围过滤
         */
        if (null != gwShare_start_ip && !"".equals(gwShare_start_ip) && !"-1".equals(gwShare_start_ip)) {
            pSQL.append(" and transIp(a.loopback_ip) >= " + conventIP(gwShare_start_ip));
        }
        if (null != gwShare_end_ip && !"".equals(gwShare_end_ip) && !"-1".equals(gwShare_end_ip)) {
            pSQL.append(" and transIp(a.loopback_ip) <= " + conventIP(gwShare_end_ip));
        }
        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        if (DBUtil.GetDB() == 1) {
            pSQL.append(" and rownum< " + maxNum);
        }else if (DBUtil.GetDB() == 3) {
            pSQL.append(" and limit " + maxNum);
        }

        if (LipossGlobals.inArea(Global.GSDX) && !StringUtil.IsEmpty(online_time)) {
            pSQL.append(" and reboot_time <= " + online_time + " and e.reboot_time >0");
        }
        if ("1".equals(timeType)) {
            if (false == StringUtil.IsEmpty(startTime)) {
                pSQL.append(" and a.complete_time>=" + startTime);
            }
            if (false == StringUtil.IsEmpty(endTime)) {
                pSQL.append(" and a.complete_time<=" + endTime);
            }
        }
        
        pSQL.append(" order by a.complete_time");
        return jt.querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage, num_splitPage, new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map(map, rs);
            }
        });
    }


    /**
     * 查询设备列表(单独根据设备表的条件查询)
     *
     * @param areaId
     * @param queryParam
     * @return
     */
    public List queryDeviceBySQL(int curPage_splitPage, int num_splitPage, String gwShare_matchSQL) 
    {
        logger.debug("GwDeviceQueryDAO=>queryDeviceBySQL()");
        PrepareSQL pSQL = new PrepareSQL();
        if (DBUtil.GetDB() == 2) {
            pSQL.setSQL(gwShare_matchSQL.replace("select", "select top " + maxNum));
        } else if(DBUtil.GetDB()==3){
        	pSQL.setSQL(gwShare_matchSQL);
            pSQL.append("and limit " + maxNum);
        }else {
            pSQL.setSQL(gwShare_matchSQL);
            pSQL.append(" and rownum< " + maxNum);
        }
        pSQL.append(" order by a.complete_time");
        return jt.querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage, num_splitPage, new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map(map, rs);
            }
        });
    }

    /**
     * 查询设备列表(条数)(根据设备序列号和设备IP用or以及like来匹配)
     *
     * @param areaId
     * @param queryParam
     * @return
     */
    public int queryDeviceCount(String gw_type, long areaId, String cityId, String vendorId,
    		String deviceModelId, String devicetypeId, String bindType,
    		String deviceSerialnumber, String deviceIp, String gwShare_start_ip, 
    		String gwShare_end_ip, String online_time,String gwShare_netSpeed)
    {
    	logger.debug("GwDeviceQueryDAO=>queryDeviceCount()");
        PrepareSQL pSQL = new PrepareSQL();
        if(DBUtil.GetDB()==3){
    		//TODO wait
        	pSQL.append("select count(*) ");
    	}else{
    		pSQL.append("select count(1) ");
    	}
        
        if (LipossGlobals.inArea(Global.GSDX) && !StringUtil.IsEmpty(online_time)) 
        {
            pSQL.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,gw_devicestatus e ");
            pSQL.append("where a.device_status =1 and a.device_id=e.device_id and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        } 
        else if(LipossGlobals.inArea(Global.JLLT) && !StringUtil.IsEmpty(gwShare_netSpeed) 
        		&& !"-1".equals(gwShare_netSpeed) && !"null".equals(gwShare_netSpeed))
        {
            pSQL.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d ,tab_speed_dev_rate e " +
                    " where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id " +
                    " and a.devicetype_id=d.devicetype_id and e.user_id = a.customer_id  ");
        }
        else 
        {
            pSQL.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        }
        
        if("4".equals(gw_type))
        {
        	pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        }

        //这个是重庆软件升级的分支，因为bio方法有很多地方调用，所以没有直接传参当标识符，用以,结尾的判断
        if (",".equals(cityId.substring(cityId.length() - 1))) {
            pSQL.append(" and a.city_id in (" + cityId.substring(0, cityId.length() - 1) + ")");
        } else {
            if (null != cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
                ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
                pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
                cityArray = null;
            }
        }
        if (null != vendorId && !"null".equals(vendorId) && !"".equals(vendorId) && !"-1".equals(vendorId)) {
            pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
        }
        if (null != deviceModelId && !"null".equals(deviceModelId) && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
            pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
        }
        if (null != devicetypeId && !"null".equals(devicetypeId) && !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
            pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL, devicetypeId);
        }
        if (null != bindType && !"".equals(bindType) && !"-1".equals(bindType)) {
            pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL, bindType);
        }
        if (null != deviceSerialnumber && !"".equals(deviceSerialnumber) && !"-1".equals(deviceSerialnumber)) {
            if (deviceSerialnumber.length() > 5) {
                pSQL.append(" and a.dev_sub_sn ='");
                pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6, deviceSerialnumber.length()));
                pSQL.append("' ");
            }
            pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE, deviceSerialnumber, false);
        }
        if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
            pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp, false);
        }
        /**
         * 重庆软件升级新增ip范围过滤
         */
        if (null != gwShare_start_ip && !"".equals(gwShare_start_ip) && !"-1".equals(gwShare_start_ip)) {
            pSQL.append(" and transIp(a.loopback_ip) >= " + conventIP(gwShare_start_ip));
        }
        if (null != gwShare_end_ip && !"".equals(gwShare_end_ip) && !"-1".equals(gwShare_end_ip)) {
            pSQL.append(" and transIp(a.loopback_ip) <= " + conventIP(gwShare_end_ip));
        }
        //		if( 1!=areaId ) {
        //			pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
        //			pSQL.append(String.valueOf(areaId));
        //			pSQL.append(") ");
        //		}
        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }

        if (LipossGlobals.inArea(Global.GSDX) && !StringUtil.IsEmpty(online_time)) {
            pSQL.append(" and e.reboot_time <= " + online_time + " and e.reboot_time >0");
        }

        if (null != gwShare_netSpeed && !"null".equals(gwShare_netSpeed) && !"".equals(gwShare_netSpeed) && !"-1".equals(gwShare_netSpeed)) {
            int netSpeed = StringUtil.getIntegerValue(gwShare_netSpeed) * 1024;
            pSQL.append(" and e.rate = ? ");
            pSQL.setInt(1,netSpeed);
        }

        return jt.queryForInt(pSQL.getSQL());
    }

    public int queryDeviceCountForSxlt(String gw_type, long areaId, String cityId, String vendorId,
    		String deviceModelId, String devicetypeId, String bindType,
    		String deviceSerialnumber,String deviceIp,String gwShare_start_ip,String gwShare_end_ip,
    		String online_time, String startTime, String endTime, String timeType) 
    {
        logger.debug("GwDeviceQueryDAO=>queryDeviceCountForSxlt()");
        PrepareSQL pSQL = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("select count(*) ");
        }else{
        	pSQL.append("select count(1) ");
        }
        if (LipossGlobals.inArea(Global.GSDX) && !StringUtil.IsEmpty(online_time)) {
            pSQL.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,gw_devicestatus e ");
            pSQL.append("where a.device_status =1 and a.device_id=e.device_id " +
            		"and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id " +
            		"and a.devicetype_id=d.devicetype_id ");
        } else {
            pSQL.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d " +
            		"where a.device_status =1 and  a.vendor_id=b.vendor_id " +
            		"and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        }

        //这个是重庆软件升级的分支，因为bio方法有很多地方调用，所以没有直接传参当标识符，用以,结尾的判断
        if (",".equals(cityId.substring(cityId.length() - 1))) {
            pSQL.append(" and a.city_id in (" + cityId.substring(0, cityId.length() - 1) + ")");
        } else {
            if (null != cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
                ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
                pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
                cityArray = null;
            }
        }
        if (null != vendorId && !"null".equals(vendorId) && !"".equals(vendorId) && !"-1".equals(vendorId)) {
            pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
        }
        if (null != deviceModelId && !"null".equals(deviceModelId) && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
            pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
        }
        if (null != devicetypeId && !"null".equals(devicetypeId) && !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
            pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL, devicetypeId);
        }
        if (null != bindType && !"".equals(bindType) && !"-1".equals(bindType)) {
            pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL, bindType);
        }
        if (null != deviceSerialnumber && !"".equals(deviceSerialnumber) && !"-1".equals(deviceSerialnumber)) {
            if (deviceSerialnumber.length() > 5) {
                pSQL.append(" and a.dev_sub_sn ='");
                pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6, deviceSerialnumber.length()));
                pSQL.append("' ");
            }
            pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.LIKE, deviceSerialnumber, false);
        }
        if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
            pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp, false);
        }
        /**
         * 重庆软件升级新增ip范围过滤
         */
        if (null != gwShare_start_ip && !"".equals(gwShare_start_ip) && !"-1".equals(gwShare_start_ip)) {
            pSQL.append(" and transIp(a.loopback_ip) >= " + conventIP(gwShare_start_ip));
        }
        if (null != gwShare_end_ip && !"".equals(gwShare_end_ip) && !"-1".equals(gwShare_end_ip)) {
            pSQL.append(" and transIp(a.loopback_ip) <= " + conventIP(gwShare_end_ip));
        }
        //		if( 1!=areaId ) {
        //			pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
        //			pSQL.append(String.valueOf(areaId));
        //			pSQL.append(") ");
        //		}
        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }

        if (LipossGlobals.inArea(Global.GSDX) && !StringUtil.IsEmpty(online_time)) {
            pSQL.append(" and e.reboot_time <= " + online_time + " and e.reboot_time >0");
        }

        if ("1".equals(timeType)) {
            if (false == StringUtil.IsEmpty(startTime)) {
                pSQL.append(" and a.complete_time>=" + startTime);
            }
            if (false == StringUtil.IsEmpty(endTime)) {
                pSQL.append(" and a.complete_time<=" + endTime);
            }
        }
        /*if ("2".equals(timeType)) {
            pSQL.setSQL("select count(1) from tab_gw_device a left join tab_hgwcustomer b on a.device_id=b.device_id where 1=1 ");
            if (false == com.linkage.module.gwms.util.StringUtil.IsEmpty(startTime)) {
                pSQL.append(" and b.binddate>=" + startTime);
            }
            if (false == com.linkage.module.gwms.util.StringUtil.IsEmpty(endTime)) {
                pSQL.append(" and b.binddate<=" + endTime);
            }
        }*/
        return jt.queryForInt(pSQL.getSQL());
    }

    /**
     * 根据自定义的SQL查询设备
     *
     * @param gwShare_matchSQL
     * @return
     */
    public int queryDeviceCountBySQL(String gwShare_matchSQL) 
    {
        logger.debug("GwDeviceQueryDAO=>queryDeviceCountBySQL()");
        PrepareSQL pSQL = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	pSQL.append("select count(*) from ");
        }else{
        	pSQL.append("select count(1) from ");
        }
        pSQL.append(gwShare_matchSQL.split("from")[1]);
        return jt.queryForInt(pSQL.getSQL());
    }

    /**
     * 查询设备列表(关联设备状态表查询)
     *
     * @param areaId
     * @param queryParam
     * @return
     */
    public List queryDeviceByLikeStatus(String gw_type, long areaId,
    		String cityId, String vendorId, String deviceModelId,String devicetypeId,
            String bindType, String deviceSerialnumber, String deviceIp,String onlineStatus, 
            String gwShare_start_ip, String gwShare_end_ip, String online_time) 
    {
        logger.debug("GwDeviceQueryDAO=>queryDeviceByLikeStatus()");
        PrepareSQL pSQL = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("select a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,"
        				+ "a.office_id,a.complete_time,a.zone_id,a.buy_time,a.staff_id,a.remark,"
        				+ "a.loopback_ip,a.interface_id,a.device_status,a.gather_id,a.devicetype_id,"
        				+ "a.maxenvelopes,a.cr_port,a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,"
        				+ "a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,a.acs_passwd,"
        				+ "a.device_type,a.x_com_username,a.x_com_passwd,a.gw_type,a.device_model_id,"
        				+ "a.customer_id,a.device_url,a.x_com_passwd_old,a.vendor_id,d.is_awifi,");
        }else{
        	pSQL.append("select a.*,");
        }
        pSQL.append("b.vendor_add,c.device_model,d.softwareversion " +
        		"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,gw_devicestatus e " +
        		"where a.device_status =1 and  a.device_id=e.device_id and a.vendor_id=b.vendor_id " +
        		"and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        if (null != onlineStatus && !"".equals(onlineStatus) && !"-1".equals(onlineStatus)) {
            pSQL.appendAndNumber("e.online_status", PrepareSQL.EQUEAL, onlineStatus);
        }
        //这个是重庆软件升级的分支，因为bio方法有很多地方调用，所以没有直接传参当标识符，用以,结尾的判断
        if (",".equals(cityId.substring(cityId.length() - 1))) {
            pSQL.append(" and a.city_id in (" + cityId.substring(0, cityId.length() - 1) + ")");
        } else {
            if (null != cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
                ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
                pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
                cityArray = null;
            }
        }
        if (null != vendorId && !"null".equals(vendorId) && !"".equals(vendorId) && !"-1".equals(vendorId)) {
            pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
        }
        if (null != deviceModelId && !"null".equals(deviceModelId) && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
            pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
        }
        if (null != devicetypeId && !"null".equals(devicetypeId) && !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
            pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL, devicetypeId);
        }
        if (null != bindType && !"".equals(bindType) && !"-1".equals(bindType)) {
            pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL, bindType);
        }
        if (null != deviceSerialnumber && !"".equals(deviceSerialnumber) && !"-1".equals(deviceSerialnumber)) {
            if (deviceSerialnumber.length() > 5) {
                pSQL.append(" and a.dev_sub_sn ='");
                pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6, deviceSerialnumber.length()));
                pSQL.append("' ");
            }
            pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE, deviceSerialnumber, false);
        }
        if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
            pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp, false);
        }
        /**
         * 重庆软件升级新增ip范围过滤
         */
        if (null != gwShare_start_ip && !"".equals(gwShare_start_ip) && !"-1".equals(gwShare_start_ip)) {
            pSQL.append(" and transIp(a.loopback_ip) >= " + conventIP(gwShare_start_ip));
        }
        if (null != gwShare_end_ip && !"".equals(gwShare_end_ip) && !"-1".equals(gwShare_end_ip)) {
            pSQL.append(" and transIp(a.loopback_ip) <= " + conventIP(gwShare_end_ip));
        }
        //		if( 1!=areaId ) {
        //			pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
        //			pSQL.append(String.valueOf(areaId));
        //			pSQL.append(") ");
        //		}
        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }

        if (LipossGlobals.inArea(Global.GSDX) && !StringUtil.IsEmpty(online_time)) {
            pSQL.append(" and reboot_time <= " + online_time + " and e.reboot_time >0");
        }
        pSQL.append(" order by a.complete_time");
        return jt.query(pSQL.getSQL(), new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map(map, rs);
            }
        });
    }

    /**
     * 高级查询设备列表SQL(关联设备状态表查询)
     *
     * @param areaId
     * @param iscqsoft
     * @param queryParam
     * @return
     */
    public String queryDeviceByLikeStatusSQL(String gw_type, long areaId,
    		String cityId, String vendorId, String deviceModelId, String devicetypeId,
            String bindType, String deviceSerialnumber, String deviceIp, String onlineStatus, 
            String iscqsoft, String gwShare_start_ip, String gwShare_end_ip) 
    {
        logger.debug("GwDeviceQueryDAO=>queryDeviceByLikeStatusSQL()");
        PrepareSQL pSQL = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("select a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,a.office_id,"
    				+ "a.complete_time,a.zone_id,a.buy_time,a.staff_id,a.remark,a.loopback_ip,"
    				+ "a.interface_id,a.device_status,a.gather_id,a.devicetype_id,"
    				+ "a.maxenvelopes,a.cr_port,a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,"
    				+ "a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,acs_passwd,"
    				+ "a.device_type,a.x_com_username,a.x_com_passwd,a.gw_type,a.device_model_id,"
    				+ "a.customer_id,a.device_url,a.x_com_passwd_old,a.vendor_id,");
        }else{
        	pSQL.append("select a.*,");
        }
        pSQL.append("b.vendor_add,c.device_model,d.softwareversion " +
        		"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,gw_devicestatus e " +
        		"where a.device_status =1 and  a.device_id=e.device_id and a.vendor_id=b.vendor_id " +
        		"and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        if (null != onlineStatus && !"".equals(onlineStatus) && !"-1".equals(onlineStatus)) {
            pSQL.appendAndNumber("e.online_status", PrepareSQL.EQUEAL, onlineStatus);
        }
        if ("true".equals(iscqsoft)) {
            pSQL.append(" and a.city_id in (" + cityId + ")");
        } else {
            if (null != cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
                ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
                pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
                cityArray = null;
            }
        }

        if (null != vendorId && !"null".equals(vendorId) && !"".equals(vendorId) && !"-1".equals(vendorId)) {
            pSQL.append(" and a.vendor_id='" + vendorId + "'");
        }
        if (null != deviceModelId && !"null".equals(deviceModelId) && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
            pSQL.append(" and a.device_model_id='" + deviceModelId + "'");
        }
        if (null != devicetypeId && !"null".equals(devicetypeId) && !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
            pSQL.append(" and a.devicetype_id=" + devicetypeId);
        }
        if (null != bindType && !"".equals(bindType) && !"-1".equals(bindType)) {
            pSQL.append(" and a.cpe_allocatedstatus=" + bindType);
        }
        if (null != deviceSerialnumber && !"".equals(deviceSerialnumber) && !"-1".equals(deviceSerialnumber)) {
            if (deviceSerialnumber.length() > 5) {
                pSQL.append(" and a.dev_sub_sn ='");
                pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6, deviceSerialnumber.length()));
                pSQL.append("' ");
            }
            pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE, deviceSerialnumber, false);
        }
        if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
            pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp, false);
        }
        /**
         * 重庆软件升级新增ip范围过滤
         */
        if (null != gwShare_start_ip && !"".equals(gwShare_start_ip) && !"-1".equals(gwShare_start_ip)) {
            pSQL.append(" and transIp(a.loopback_ip) >= " + conventIP(gwShare_start_ip));
        }
        if (null != gwShare_end_ip && !"".equals(gwShare_end_ip) && !"-1".equals(gwShare_end_ip)) {
            pSQL.append(" and transIp(a.loopback_ip) <= " + conventIP(gwShare_end_ip));
        }
        //		if( 1!=areaId ) {
        //			pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
        //			pSQL.append(String.valueOf(areaId));
        //			pSQL.append(") ");
        //		}
        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        return pSQL.getSQL().trim();
    }

    public String queryDeviceByLikeStatusSQL2(String gw_type, long areaId,
    		String cityId, String vendorId, String deviceModelId, String devicetypeId,
            String bindType, String sn, String onlineStatus) 
    {
        logger.debug("GwDeviceQueryDAO=>queryDeviceByLikeStatusSQL2()");

        String table_customer = "tab_hgwcustomer";
        if ("2".equals(gw_type)) {
            table_customer = "tab_egwcustomer";
        }
        /**	 if(DBUtil.GetDB()==3){
        	//TODO wait
        }else{
        	
        }*/
        PrepareSQL pSQL = new PrepareSQL();
        pSQL.append("select a.device_id,a.device_model_id,a.devicetype_id,b.username ");
        pSQL.append("from tab_gw_device a," + table_customer + " b,gw_devicestatus e ");
        pSQL.append("where a.device_status=1 and a.device_id=e.device_id and a.device_id=b.device_id ");
        if (!StringUtil.IsEmpty(onlineStatus) && !"-1".equals(onlineStatus)) {
            pSQL.appendAndNumber("e.online_status", PrepareSQL.EQUEAL, onlineStatus);
        }
        if (null != cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        if (!StringUtil.IsEmpty(vendorId) && !"null".equals(vendorId) && !"-1".equals(vendorId)) {
            pSQL.append(" and a.vendor_id='" + vendorId + "'");
        }
        if (!StringUtil.IsEmpty(deviceModelId) && !"null".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
            pSQL.append(" and a.device_model_id='" + deviceModelId + "'");
        }
        if (!StringUtil.IsEmpty(devicetypeId) && !"null".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
            pSQL.append(" and a.devicetype_id=" + devicetypeId);
        }
        if (!StringUtil.IsEmpty(bindType) && !"-1".equals(bindType)) {
            pSQL.append(" and a.cpe_allocatedstatus=" + bindType);
        }
        if (!StringUtil.IsEmpty(sn) && !"-1".equals(sn)) {
            if (sn.length() > 5) {
                pSQL.append(" and a.dev_sub_sn='" + sn.substring(sn.length() - 6, sn.length()) + "' ");
            }
            pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE, sn, false);
        }
        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type=" + gw_type);
        }
        return pSQL.getSQL().trim();
    }

    /**
     * 查询设备列表(关联设备状态表查询)
     *
     * @param areaId
     * @param queryParam
     * @return
     */
    public List queryDeviceByLikeStatus(String gw_type, int curPage_splitPage, 
    		int num_splitPage,long areaId,String cityId, String vendorId, String deviceModelId, 
    		String devicetypeId,String bindType, String deviceSerialnumber, String deviceIp, 
    		String onlineStatus,String gwShare_start_ip,String gwShare_end_ip,String online_time) 
    {
        logger.debug("GwDeviceQueryDAO=>queryDeviceByLikeStatus()");
        PrepareSQL pSQL = new PrepareSQL();
        if (DBUtil.GetDB() == 2) {
            pSQL.append("select top " + maxNum+" ");
        } else {
            pSQL.append("select ");
        }
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,a.office_id,"
        				+ "a.complete_time,a.zone_id,a.buy_time,a.staff_id,a.remark,a.loopback_ip,"
        				+ "a.interface_id,a.device_status,a.gather_id,a.devicetype_id,"
        				+ "a.maxenvelopes,a.cr_port,a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,"
        				+ "a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,a.acs_passwd,"
        				+ "a.device_type,a.x_com_username,a.x_com_passwd,a.gw_type,a.device_model_id,"
        				+ "a.customer_id,a.device_url,a.x_com_passwd_old,a.vendor_id,d.is_awifi,");
        }else{
        	pSQL.append("a.*,");
        }
        pSQL.append("b.vendor_add,c.device_model,d.softwareversion "
        			+ "from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,gw_devicestatus e "
        			+ "where a.device_status=1 and a.device_id=e.device_id and a.vendor_id=b.vendor_id "
        			+ "and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        if (null != onlineStatus && !"".equals(onlineStatus) && !"-1".equals(onlineStatus)) {
            pSQL.appendAndNumber("e.online_status", PrepareSQL.EQUEAL, onlineStatus);
        }

        //这个是重庆软件升级的分支，因为bio方法有很多地方调用，所以没有直接传参当标识符，用以,结尾的判断
        if (",".equals(cityId.substring(cityId.length() - 1))) {
            pSQL.append(" and a.city_id in (" + cityId.substring(0, cityId.length() - 1) + ")");
        } else {
            if (null != cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
                ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
                pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
                cityArray = null;
            }
        }
        if (null != vendorId && !"null".equals(vendorId) && !"".equals(vendorId) && !"-1".equals(vendorId)) {
            pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
        }
        if (null != deviceModelId && !"null".equals(deviceModelId) && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
            pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
        }
        if (null != devicetypeId && !"null".equals(devicetypeId) && !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
            pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL, devicetypeId);
        }
        if (null != bindType && !"".equals(bindType) && !"-1".equals(bindType)) {
            pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL, bindType);
        }
        if (null != deviceSerialnumber && !"".equals(deviceSerialnumber) && !"-1".equals(deviceSerialnumber)) {
            if (deviceSerialnumber.length() > 5) {
                pSQL.append(" and a.dev_sub_sn ='");
                pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6, deviceSerialnumber.length()));
                pSQL.append("' ");
            }
            pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE, deviceSerialnumber, false);
        }
        if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
            pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp, false);
        }
        /**
         * 重庆软件升级新增ip范围过滤
         */
        if (null != gwShare_start_ip && !"".equals(gwShare_start_ip) && !"-1".equals(gwShare_start_ip)) {
            pSQL.append(" and transIp(a.loopback_ip) >= " + conventIP(gwShare_start_ip));
        }
        if (null != gwShare_end_ip && !"".equals(gwShare_end_ip) && !"-1".equals(gwShare_end_ip)) {
            pSQL.append(" and transIp(a.loopback_ip) <= " + conventIP(gwShare_end_ip));
        }
        //		if( 1!=areaId ) {
        //			pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
        //			pSQL.append(String.valueOf(areaId));
        //			pSQL.append(") ");
        //		}
        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        if (DBUtil.GetDB() == 1) {
            pSQL.append(" and rownum< " + maxNum);
        }else if (DBUtil.GetDB() == 3) {
            pSQL.append(" and limit " + maxNum);
        }

        if (LipossGlobals.inArea(Global.GSDX) && !StringUtil.IsEmpty(online_time)) {
            pSQL.append(" and reboot_time <= " + online_time + " and e.reboot_time >0");
        }
        pSQL.append(" order by a.complete_time");
        return jt.querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage, num_splitPage, new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map(map, rs);
            }
        });
    }

    /**
     * 查询设备列表(关联设备状态表查询)
     *
     * @param areaId
     * @param queryParam
     * @return
     */
    public int queryDeviceByLikeStatusCount(String gw_type, long areaId, String cityId, String vendorId,
    		String deviceModelId, String devicetypeId, String bindType, String deviceSerialnumber,
            String deviceIp, String onlineStatus, String gwShare_start_ip, String gwShare_end_ip,
            String online_time)
    {
        logger.debug("GwDeviceQueryDAO=>queryDeviceByLikeStatus()");
        PrepareSQL pSQL = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("select count(*) ");
        }else{
        	pSQL.append("select count(1) ");
        }
        pSQL.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,gw_devicestatus e "
        			+ "where a.device_status=1 and a.device_id=e.device_id and a.vendor_id=b.vendor_id "
        			+ "and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
        if (null != onlineStatus && !"".equals(onlineStatus) && !"-1".equals(onlineStatus)) {
            pSQL.appendAndNumber("e.online_status", PrepareSQL.EQUEAL, onlineStatus);
        }

        //这个是重庆软件升级的分支，因为bio方法有很多地方调用，所以没有直接传参当标识符，用以,结尾的判断
        if (",".equals(cityId.substring(cityId.length() - 1))) {
            pSQL.append(" and a.city_id in (" + cityId.substring(0, cityId.length() - 1) + ")");
        } else {
            if (null != cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
                ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
                pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
                cityArray = null;
            }
        }

        if (null != vendorId && !"null".equals(vendorId) && !"".equals(vendorId) && !"-1".equals(vendorId)) {
            pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
        }
        if (null != deviceModelId && !"null".equals(deviceModelId) && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
            pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL, deviceModelId);
        }
        if (null != devicetypeId && !"null".equals(devicetypeId) && !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
            pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL, devicetypeId);
        }
        if (null != bindType && !"".equals(bindType) && !"-1".equals(bindType)) {
            pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL, bindType);
        }
        if (null != deviceSerialnumber && !"".equals(deviceSerialnumber) && !"-1".equals(deviceSerialnumber)) {
            if (deviceSerialnumber.length() > 5) {
                pSQL.append(" and a.dev_sub_sn ='");
                pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length() - 6, deviceSerialnumber.length()));
                pSQL.append("' ");
            }
            pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE, deviceSerialnumber, false);
        }
        if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
            pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, deviceIp, false);
        }
        /**
         * 重庆软件升级新增ip范围过滤
         * itms数据库已存在函数transIp，可直接将ip转换成字符进行比较
         * 将ip转换成不带'.'，不足三位补0的形式 10.1.55.72 即转换成 010001055072
         */
        if (null != gwShare_start_ip && !"".equals(gwShare_start_ip) && !"-1".equals(gwShare_start_ip)) {
            pSQL.append(" and transIp(a.loopback_ip) >= " + conventIP(gwShare_start_ip));
        }
        if (null != gwShare_end_ip && !"".equals(gwShare_end_ip) && !"-1".equals(gwShare_end_ip)) {
            pSQL.append(" and transIp(a.loopback_ip) <= " + conventIP(gwShare_end_ip));
        }
        //		if( 1!=areaId ) {
        //			pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
        //			pSQL.append(String.valueOf(areaId));
        //			pSQL.append(") ");
        //		}
        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }

        if (LipossGlobals.inArea(Global.GSDX) && !StringUtil.IsEmpty(online_time)) {
            pSQL.append(" and e.reboot_time <=" + online_time + " and e.reboot_time >0");
        }

        return jt.queryForInt(pSQL.getSQL());
    }

    public int account_number(String gw_type, long areaId,String cityId, String username) 
    {
        PrepareSQL pSQL = new PrepareSQL();
        String tableName = "tab_hgwcustomer";
        if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type)) {
            tableName = "tab_egwcustomer";
        }
        if(DBUtil.GetDB()==3){
        	pSQL.append("select count(*) ");
        }else{
        	pSQL.append("select count(1) ");
        }
        pSQL.append("from tab_gw_device a,tab_hgwcustomer e where a.device_id=e.device_id ");
        if (null != cityId && !"null".equals(cityId) && !"".equals(cityId) 
        		&& !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        if (null != username && !"".equals(username)) {
            pSQL.appendAndString("e.username", PrepareSQL.EQUEAL, username);
        }

        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        return jt.queryForInt(pSQL.getSQL());
    }

    public List<Map> isawifiDevice(String gw_type, long areaId,String cityId, String username) 
    {
        PrepareSQL pSQL = new PrepareSQL();
        String tableName = "tab_hgwcustomer";
        if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type)) {
            tableName = "tab_egwcustomer";
        }
        pSQL.setSQL("select d.is_awifi from tab_gw_device a,tab_devicetype_info d,"+tableName+" e ");
        pSQL.append("where a.device_id=e.device_id and a.device_status=1 and a.devicetype_id=d.devicetype_id ");
        if (null != cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        if (null != username && !"".equals(username)) {
            pSQL.appendAndString("e.username", PrepareSQL.EQUEAL, username);
        }

        if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        return jt.queryForList(pSQL.getSQL());
    }

    /**
     * 查询设备列表(单独根据设备表的条件查询)
     */
    public List queryDevice(String gw_type, long areaId,String cityId, String username) 
    {
        logger.debug("GwDeviceQueryDAO=>queryDevice()");
        PrepareSQL pSQL = new PrepareSQL();
        if("4".equals(gw_type))
        {
        	if(DBUtil.GetDB()==3){
            	//TODO wait
        		pSQL.append("select a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,"
        					+ "a.office_id,a.complete_time,a.zone_id,a.buy_time,a.staff_id,a.remark,"
        					+ "a.loopback_ip,a.interface_id,a.device_status,a.gather_id,a.devicetype_id,"
        					+ "a.maxenvelopes,a.cr_port,a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,"
        					+ "a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,a.acs_passwd,"
        					+ "a.device_type,a.x_com_username,a.x_com_passwd,a.gw_type,a.device_model_id,"
        					+ "a.customer_id,a.device_url,a.x_com_passwd_old,a.vendor_id,d.is_awifi,");
            }else{
            	pSQL.append("select a.*,");
            }
        	pSQL.append("b.vendor_add,c.device_model,d.softwareversion "
        				+ "from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d "
        				+ "where a.device_status=1 and a.vendor_id=b.vendor_id "
        				+ "and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id "
        				+ "and (a.serv_account= '"+username+"' ");
        }
        else
        {
	        String tableName = "tab_hgwcustomer";
	        if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type)) {
	            tableName = "tab_egwcustomer";
	        }
	        if(DBUtil.GetDB()==3){
	        	//TODO wait
	        	pSQL.append("select a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,"
	        				+ "a.office_id,a.complete_time,a.zone_id,a.buy_time,a.staff_id,a.remark,"
	        				+ "a.loopback_ip,a.interface_id,a.device_status,a.gather_id,a.devicetype_id,"
	        				+ "a.maxenvelopes,a.cr_port,cr_path,a.cpe_mac,a.cpe_currentupdatetime,"
	        				+ "a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,acs_passwd,"
	        				+ "a.device_type,a.x_com_username,a.x_com_passwd,a.gw_type,a.device_model_id,"
	        				+ "a.customer_id,a.device_url,a.x_com_passwd_old,a.vendor_id,");
	        }else{
	        	pSQL.append("select a.*,");
	        }
	        pSQL.append("b.vendor_add,c.device_model,d.softwareversion,d.is_awifi ");
	        pSQL.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d ");
	        if (Global.SXLT.equals(instArea)) {
	            pSQL.append("where a.device_status=1 ");
	        } else {
	            pSQL.append("," + tableName + " e ");
	            pSQL.append("where a.device_status=1 and a.device_id=e.device_id ");
	        }
	
	        pSQL.append("and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id ");
	        pSQL.append("and a.devicetype_id=d.devicetype_id ");
	
	        if (!StringUtil.IsEmpty(cityId) && !"null".equals(cityId)
	                && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
	            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
	            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
	            cityArray = null;
	        }
	        if (!StringUtil.IsEmpty(username)) {
	            if (Global.SXLT.equals(instArea)) {
	                if ("1".equalsIgnoreCase(LipossGlobals.getLipossProperty("isLikeQuery"))) {
	                    pSQL.append(" and a.device_name like '%" + username + "' ");
	                } else {
	                    pSQL.appendAndString("a.device_name", PrepareSQL.EQUEAL, username);
	                }
	            } else {
	                if ("1".equalsIgnoreCase(LipossGlobals.getLipossProperty("isLikeQuery"))) {
	                    pSQL.append(" and e.username like '%" + username + "' ");
	                    String user_sub_name = username;
	                    if (username.length() > 6) {
	                        user_sub_name = username.substring(username.length() - 6);
	                    }

	                    if ("tab_hgwcustomer".equals(tableName)) {
                            pSQL.append(" and e.user_sub_name ='" + user_sub_name + "'");
                        }
	                } else {
	                    pSQL.appendAndString("e.username", PrepareSQL.EQUEAL, username);
	                }
	            }
	        }
	
	        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
	            pSQL.append(" and a.gw_type=" + gw_type);
	        }
	        pSQL.append(" order by a.complete_time");
        }
        return jt.query(pSQL.getSQL(), new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map(map, rs);
            }
        });
    }

    /**
     * 查询设备列表(单独根据设备表的条件查询)
     *
     * @param areaId
     * @param queryParam
     * @return
     */
    public List queryDevice(String gw_type, int curPage_splitPage, 
    		int num_splitPage, long areaId,String cityId, String username) 
    {
        logger.debug("GwDeviceQueryDAO=>queryDevice()");
        PrepareSQL pSQL = new PrepareSQL();
        String tableName = "tab_hgwcustomer";
        if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type)) {
            tableName = "tab_egwcustomer";
        }
        
        if (DBUtil.GetDB() == 2) {
            pSQL.append("select top " + maxNum+" ");
        } else {
            pSQL.append("select ");
        }
       
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,a.office_id,"
        				+ "a.complete_time,a.zone_id,a.buy_time,a.staff_id,a.remark,a.loopback_ip,"
        				+ "a.interface_id,a.device_status,a.gather_id,a.devicetype_id,"
        				+ "a.maxenvelopes,a.cr_port,a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,"
        				+ "a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,a.acs_passwd,"
        				+ "a.device_type,a.x_com_username,a.x_com_passwd,a.gw_type,a.device_model_id,"
        				+ "a.customer_id,a.device_url,a.x_com_passwd_old,a.vendor_id,d.is_awifi,");
        }else{
        	pSQL.append("a.*,");
        }
        pSQL.append("b.vendor_add,c.device_model,d.softwareversion "
        			+ "from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d ");
        if (Global.SXLT.equals(instArea)) {
            pSQL.append(" where a.device_status=1 ");
        } else {
            pSQL.append("," + tableName + "e ");
            pSQL.append("where a.device_status=1 and a.device_id=e.device_id ");
        }
        pSQL.append("and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id ");
        pSQL.append("and a.devicetype_id=d.devicetype_id ");
        if (!StringUtil.IsEmpty(cityId) && !"null".equals(cityId)
                && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        if (!StringUtil.IsEmpty(username)) {
            if (Global.SXLT.equals(instArea)) {
                if ("1".equalsIgnoreCase(LipossGlobals.getLipossProperty("isLikeQuery"))) {
                    pSQL.append(" and a.device_name like '%" + username + "' ");
                } else {
                    pSQL.appendAndString("a.device_name", PrepareSQL.EQUEAL, username);
                }
            } else {
                if ("1".equalsIgnoreCase(LipossGlobals.getLipossProperty("isLikeQuery"))) {
                    pSQL.append(" and e.username like '%" + username + "' ");
                    String user_sub_name = username;
                    if (username.length() > 6) {
                        user_sub_name = username.substring(username.length() - 6);
                    }
                    if ("tab_hgwcustomer".equals(tableName)) {
                        pSQL.append(" and e.user_sub_name ='" + user_sub_name + "'");
                    }
                } else {
                    pSQL.appendAndString("e.username", PrepareSQL.EQUEAL, username);
                }
            }
        }

        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        if (DBUtil.GetDB() == 1) {
            pSQL.append(" and rownum< " + maxNum);
        }else if (DBUtil.GetDB() == 3) {
            pSQL.append(" and limit " + maxNum);
        }
        pSQL.append(" order by a.complete_time");
        return jt.querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage, num_splitPage, new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map(map, rs);
            }
        });
    }

    public List queryDeviceForSxlt(String gw_type, int curPage_splitPage, 
    		int num_splitPage, long areaId,String cityId, String username,
    		String startTime , String endTime, String timeType) 
    {
        logger.debug("GwDeviceQueryDAO=>queryDevice()");
        PrepareSQL pSQL = new PrepareSQL();
        String tableName = "tab_hgwcustomer";
        if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type)) {
            tableName = "tab_egwcustomer";
        }
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("select a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,"
        				+ "a.office_id,a.complete_time,a.zone_id,a.buy_time,a.staff_id,a.remark,"
        				+ "a.loopback_ip,a.interface_id,a.device_status,a.gather_id,a.devicetype_id,"
        				+ "a.maxenvelopes,a.cr_port,a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,"
        				+ "a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,a.acs_passwd,"
        				+ "a.device_type,a.x_com_username,a.x_com_passwd,a.gw_type,a.device_model_id,"
        				+ "a.customer_id,a.device_url,a.x_com_passwd_old,a.vendor_id,d.is_awifi,");
        }else{
        	pSQL.append("select a.*,");
        }
        pSQL.append("b.vendor_add,c.device_model,d.softwareversion " +
        		"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d " +
        		"where 1=1 ");
        if (Global.SXLT.equals(instArea)) {
            pSQL.append(" and a.device_status=1 ");
        } else {
            pSQL.append("," + tableName + "e ");
            pSQL.append("and a.device_status=1 and a.device_id=e.device_id and ");
        }
        pSQL.append("and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id ");
        pSQL.append("and a.devicetype_id=d.devicetype_id ");
        if (!StringUtil.IsEmpty(cityId) && !"null".equals(cityId)
                && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        if (!StringUtil.IsEmpty(username)) {
            if (Global.SXLT.equals(instArea)) {
                if ("1".equalsIgnoreCase(LipossGlobals.getLipossProperty("isLikeQuery"))) {
                    pSQL.append(" and a.device_name like '%" + username + "' ");
                } else {
                    pSQL.appendAndString("a.device_name", PrepareSQL.EQUEAL, username);
                }
            } else {
                if ("1".equalsIgnoreCase(LipossGlobals.getLipossProperty("isLikeQuery"))) {
                    pSQL.append(" and e.username like '%" + username + "' ");
                    String user_sub_name = username;
                    if (username.length() > 6) {
                        user_sub_name = username.substring(username.length() - 6);
                    }
                    if ("tab_hgwcustomer".equals(tableName)) {
                        pSQL.append(" and e.user_sub_name ='" + user_sub_name + "'");
                    }
                } else {
                    pSQL.appendAndString("e.username", PrepareSQL.EQUEAL, username);
                }
            }
        }

        if ("1".equals(timeType)) {
            if (false == StringUtil.IsEmpty(startTime)) {
                pSQL.append(" and a.complete_time>=" + startTime);
            }
            if (false == StringUtil.IsEmpty(endTime)) {
                pSQL.append(" and a.complete_time<=" + endTime);
            }
        }
        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        
        if (DBUtil.GetDB() == 1) {
            pSQL.append(" and rownum< " + maxNum);
        }else if (DBUtil.GetDB() == 3) {
            pSQL.append(" and limit " + maxNum);
        }
        pSQL.append(" order by a.complete_time");
        return jt.querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage, num_splitPage, new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map(map, rs);
            }
        });
    }
    /**
     * 宁夏 按VOIP电话号码查询设备
     * 根据VOIP电话号码以及属地统计有多少设备
     * <p>
     * add by zhangchy 2012-02-23
     *
     * @param city_id
     * @param voipPhoneNum
     * @return
     */
    public int queryDeviceCount(String gw_type, String city_id, String voipPhoneNum) 
    {
        logger.debug("queryDeviceCount({},{})", new Object[]{city_id, voipPhoneNum});

        String table_customer = "tab_hgwcustomer";
        String table_voip = "tab_voip_serv_param";
        if ("2".equals(gw_type)) {
            table_customer = "tab_egwcustomer";
            table_voip = "tab_egw_voip_serv_param";
        }
        
        PrepareSQL psql = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	 psql.append("select count(*) ");
        }else{
        	 psql.append("select count(1) ");
        }
        psql.append("from " + table_customer + " a," + table_voip + " b,tab_gw_device c,"
        				+ "tab_vendor d,gw_device_model e,tab_devicetype_info f ");
        psql.append("where a.user_id = b.user_id and a.device_id = c.device_id ");
        psql.append("and c.vendor_id = d.vendor_id and c.device_model_id = e.device_model_id ");
        psql.append("and c.devicetype_id = f.devicetype_id and c.device_status = 1 "); // 设备已确认

        // 根据VOIP电话号码
        if (!StringUtil.IsEmpty(voipPhoneNum)) {
            psql.append(" and b.voip_phone = '" + voipPhoneNum + "'");
        }
        if (!StringUtil.IsEmpty(city_id) && !"null".equals(city_id) 
        		&& !"-1".equals(city_id) && !CENTER_CITY.equals(city_id)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(city_id);
            psql.append(" and c.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
            psql.append(" and c.gw_type = " + gw_type);
        }

        return jt.queryForInt(psql.getSQL());
    }

    public int queryDeviceCountForSxlt(String gw_type, String city_id,
    		String voipPhoneNum, String startTime, String endTime, String timeType) 
    {
        logger.debug("queryDeviceCount({},{})", new Object[]{city_id, voipPhoneNum});

        String table_customer = "tab_hgwcustomer";
        String table_voip = "tab_voip_serv_param";
        if ("2".equals(gw_type)) {
            table_customer = "tab_egwcustomer";
            table_voip = "tab_egw_voip_serv_param";
        }
        
        PrepareSQL psql = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	psql.append("select count(*) ");
        }else{
        	psql.append("select count(1) ");
        }
        
        psql.append("from " + table_customer + " a," + table_voip + " b,tab_gw_device c,"
        				+ "tab_vendor d,gw_device_model e,tab_devicetype_info f ");
        psql.append("where a.user_id=b.user_id and a.device_id=c.device_id ");
        psql.append("and c.vendor_id=d.vendor_id and c.device_model_id=e.device_model_id ");
        psql.append("and c.devicetype_id=f.devicetype_id and c.device_status=1 "); // 设备已确认

        // 根据VOIP电话号码
        if (!StringUtil.IsEmpty(voipPhoneNum)) {
            psql.append("   and b.voip_phone = '" + voipPhoneNum + "'");
        }
        if (!StringUtil.IsEmpty(city_id) && !"null".equals(city_id) 
        		&& !"-1".equals(city_id) && !CENTER_CITY.equals(city_id)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(city_id);
            psql.append("   and c.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
            psql.append(" and c.gw_type = " + gw_type);
        }
        if ("1".equals(timeType)) {
            if (false == StringUtil.IsEmpty(startTime)) {
                psql.append(" and c.complete_time>=" + startTime);
            }
            if (false == StringUtil.IsEmpty(endTime)) {
                psql.append(" and c.complete_time<=" + endTime);
            }
        }
        if ("2".equals(timeType)) {
            if (false == StringUtil.IsEmpty(startTime)) {
                psql.append(" and a.binddate>=" + startTime);
            }
            if (false == StringUtil.IsEmpty(endTime)) {
                psql.append(" and a.binddate<=" + endTime);
            }
        }

        return jt.queryForInt(psql.getSQL());
    }

    public int number(String gw_type, String city_id, String voipPhoneNum) 
    {
        PrepareSQL psql = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	psql.append("select count(*) ");
        }else{
        	psql.append("select count(1) ");
        }
        psql.append("from tab_hgwcustomer a,tab_voip_serv_param b,tab_gw_device c ");
        psql.append("where a.user_id = b.user_id and a.device_id = c.device_id ");
        //psql.append("   and c.device_status = 1"); // 设备已确认
        // 根据VOIP电话号码
        if (!StringUtil.IsEmpty(voipPhoneNum)) {
            psql.append("   and b.voip_phone = '" + voipPhoneNum + "'");
        }
        if (!StringUtil.IsEmpty(city_id) && !"null".equals(city_id) 
        		&& !"-1".equals(city_id) && !CENTER_CITY.equals(city_id)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(city_id);
            psql.append("   and c.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
            psql.append(" and c.gw_type = " + gw_type);
        }
        return jt.queryForInt(psql.getSQL());
    }

    public List<Map> isawifiDevice(String gw_type, String city_id, String voipPhoneNum) 
    {
        PrepareSQL psql = new PrepareSQL();
        psql.append("select f.is_awifi from tab_hgwcustomer a,tab_voip_serv_param b,tab_gw_device c,"
        			+ "tab_vendor d,gw_device_model e,tab_devicetype_info f ");
        psql.append("where a.user_id = b.user_id and a.device_id = c.device_id");
        psql.append("and c.vendor_id = d.vendor_id and c.device_model_id = e.device_model_id ");
        psql.append("and c.devicetype_id = f.devicetype_id and c.device_status = 1 "); // 设备已确认

        // 根据VOIP电话号码
        if (!StringUtil.IsEmpty(voipPhoneNum)) {
            psql.append(" and b.voip_phone = '" + voipPhoneNum + "'");
        }
        if (!StringUtil.IsEmpty(city_id) && !"null".equals(city_id) 
        		&& !"-1".equals(city_id) && !CENTER_CITY.equals(city_id)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(city_id);
            psql.append(" and c.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
            psql.append(" and c.gw_type = " + gw_type);
        }
        return jt.queryForList(psql.getSQL());
    }

    /**
     * 宁夏 按VOIP电话号码查询设备
     * <p>
     * 不分页
     * <p>
     * add by zhangchy 2012-02-23
     *
     * @param city_id
     * @param voipPhoneNum
     * @return
     */
    public List queryDevice(String gw_type, String city_id, String voipPhoneNum) 
    {
        logger.debug("queryDevice({},{})", new Object[]{city_id, voipPhoneNum});

        String table_customer = "tab_hgwcustomer";
        String table_voip = "tab_voip_serv_param";
        if ("2".equals(gw_type)) {
            table_customer = "tab_egwcustomer";
            table_voip = "tab_egw_voip_serv_param";
        }
        
        PrepareSQL psql = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	psql.append("select c.device_id,c.oui,c.device_serialnumber,c.device_name,c.city_id,"
        				+ "c.office_id,c.complete_time,c.zone_id,c.buy_time,c.staff_id,c.remark,"
        				+ "c.loopback_ip,c.interface_id,c.device_status,c.gather_id,c.devicetype_id,"
        				+ "c.maxenvelopes,c.cr_port,c.cr_path,c.cpe_mac,c.cpe_currentupdatetime,"
        				+ "c.cpe_allocatedstatus,c.cpe_username,c.cpe_passwd,c.acs_username,c.acs_passwd,"
        				+ "c.device_type,c.x_com_username,c.x_com_passwd,c.gw_type,c.device_model_id,"
        				+ "c.customer_id,c.device_url,c.x_com_passwd_old,c.vendor_id,");
        }else{
        	psql.append("select c.*,");
        }

        psql.append("d.vendor_add,e.device_model,f.softwareversion,f.is_awifi ");
        psql.append("from " + table_customer + " a," + table_voip + " b,tab_gw_device c,"
        				+ "tab_vendor d,gw_device_model e,tab_devicetype_info f ");
        psql.append("where a.user_id=b.user_id and a.device_id=c.device_id ");
        psql.append("and c.vendor_id=d.vendor_id and c.device_model_id=e.device_model_id ");
        psql.append("and c.devicetype_id=f.devicetype_id and c.device_status=1 "); // 设备已确认

        // 根据VOIP电话号码
        if (!StringUtil.IsEmpty(voipPhoneNum)) {
            psql.append("   and b.voip_phone = '" + voipPhoneNum + "'");
        }

        if (!StringUtil.IsEmpty(city_id) && !"null".equals(city_id) 
        		&& !"-1".equals(city_id) && !CENTER_CITY.equals(city_id)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(city_id);
            psql.append("   and c.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
            psql.append(" and c.gw_type = " + gw_type);
        }
        return jt.query(psql.getSQL(), new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map(map, rs);
            }
        });
    }


    /**
     * 宁夏 按VOIP电话号码查询设备
     * <p>
     * 分页
     * <p>
     * add by zhangchy 2012-02-23
     *
     * @param city_id
     * @param voipPhoneNum
     * @return
     */
    public List queryDevice(String gw_type, int curPage_splitPage, int num_splitPage,
    		String city_id, String voipPhoneNum) 
    {
        logger.debug("queryDevice({},{},{},{})", 
        		new Object[]{curPage_splitPage, num_splitPage, city_id, voipPhoneNum});

        String table_customer = "tab_hgwcustomer";
        String table_voip = "tab_voip_serv_param";
        if ("2".equals(gw_type)) {
            table_customer = "tab_egwcustomer";
            table_voip = "tab_egw_voip_serv_param";
        }

        PrepareSQL psql = new PrepareSQL();
        if (DBUtil.GetDB() == 2) {
            psql.append("select top " + maxNum+" ");
        } else {
            psql.append("select ");
        }
        
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	psql.append("c.device_id,c.oui,c.device_serialnumber,c.device_name,c.city_id,c.office_id,"
        				+ "c.complete_time,c.zone_id,c.buy_time,c.staff_id,c.remark,c.loopback_ip,"
        				+ "c.interface_id,c.device_status,c.gather_id,c.devicetype_id,"
        				+ "c.maxenvelopes,c.cr_port,c.cr_path,c.cpe_mac,c.cpe_currentupdatetime,"
        				+ "c.cpe_allocatedstatus,c.cpe_username,c.cpe_passwd,c.acs_username,c.acs_passwd,"
        				+ "c.device_type,c.x_com_username,c.x_com_passwd,c.gw_type,c.device_model_id,"
        				+ "c.customer_id,c.device_url,c.x_com_passwd_old,c.vendor_id,");
        }else{
        	psql.append("c.*,");
        }
        psql.append("d.vendor_add,e.device_model,f.softwareversion,f.is_awifi ");
        psql.append("from " + table_customer + " a," + table_voip + " b,tab_gw_device c,"
        				+ "tab_vendor d,gw_device_model e,tab_devicetype_info f ");
        psql.append("where a.user_id=b.user_id and a.device_id=c.device_id ");
        psql.append("and c.vendor_id=d.vendor_id and c.device_model_id=e.device_model_id ");
        psql.append("and c.devicetype_id=f.devicetype_id and c.device_status=1 "); // 设备已确认

        // 根据VOIP电话号码
        if (!StringUtil.IsEmpty(voipPhoneNum)) {
            psql.append("   and b.voip_phone = '" + voipPhoneNum + "'");
        }

        if (!StringUtil.IsEmpty(city_id) && !"null".equals(city_id) 
        		&& !"-1".equals(city_id) && !CENTER_CITY.equals(city_id)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(city_id);
            psql.append("   and c.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }

        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
            psql.append(" and c.gw_type = " + gw_type);
        }
        if (DBUtil.GetDB() == 1) {
            psql.append(" and rownum< " + maxNum);
        }else if(DBUtil.GetDB() == 3){
        	psql.append(" and limit " + maxNum);
        }
        return jt.querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage, num_splitPage, new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map(map, rs);
            }
        });
    }

    public List queryDeviceForSxlt(String gw_type, int curPage_splitPage, 
    		int num_splitPage, String city_id,String voipPhoneNum,
    		String startTime, String endTime, String timeType) 
    {
        logger.debug("queryDevice({},{},{},{})", 
        		new Object[]{curPage_splitPage, num_splitPage, city_id, voipPhoneNum});

        String table_customer = "tab_hgwcustomer";
        String table_voip = "tab_voip_serv_param";
        if ("2".equals(gw_type)) {
            table_customer = "tab_egwcustomer";
            table_voip = "tab_egw_voip_serv_param";
        }

        PrepareSQL psql = new PrepareSQL();
        if (DBUtil.GetDB() == 2) {
            psql.append("select top " + maxNum+" ");
        } else {
            psql.append("select ");
        }
        
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	psql.append("c.device_id,c.oui,c.device_serialnumber,c.device_name,c.city_id,c.office_id,"
        				+ "c.complete_time,c.zone_id,c.buy_time,c.staff_id,c.remark,c.loopback_ip,"
        				+ "c.interface_id,c.device_status,c.gather_id,c.devicetype_id,"
        				+ "c.maxenvelopes,c.cr_port,c.cr_path,c.cpe_mac,c.cpe_currentupdatetime,"
        				+ "c.cpe_allocatedstatus,c.cpe_username,c.cpe_passwd,c.acs_username,c.acs_passwd,"
        				+ "c.device_type,c.x_com_username,c.x_com_passwd,c.gw_type,c.device_model_id,"
        				+ "c.customer_id,c.device_url,c.x_com_passwd_old,c.vendor_id,");
        }else{
        	psql.append("c.*,");
        }
        psql.append("d.vendor_add,e.device_model,f.softwareversion,f.is_awifi ");
        psql.append("from " + table_customer + " a," + table_voip + " b,tab_gw_device c,"
        				+ "tab_vendor d,gw_device_model e,tab_devicetype_info f ");
        psql.append("where a.user_id = b.user_id and a.device_id = c.device_id ");
        psql.append("and c.vendor_id = d.vendor_id and c.device_model_id = e.device_model_id ");
        psql.append("and c.devicetype_id = f.devicetype_id and c.device_status = 1 "); // 设备已确认

        // 根据VOIP电话号码
        if (!StringUtil.IsEmpty(voipPhoneNum)) {
            psql.append("   and b.voip_phone = '" + voipPhoneNum + "'");
        }

        if (!StringUtil.IsEmpty(city_id) && !"null".equals(city_id) 
        		&& !"-1".equals(city_id) && !CENTER_CITY.equals(city_id)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(city_id);
            psql.append("   and c.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }

        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
            psql.append(" and c.gw_type = " + gw_type);
        }
        if ("1".equals(timeType)) {
            if (false == StringUtil.IsEmpty(startTime)) {
                psql.append(" and c.complete_time>=" + startTime);
            }
            if (false == StringUtil.IsEmpty(endTime)) {
                psql.append(" and c.complete_time<=" + endTime);
            }
        }
        if ("2".equals(timeType)) {
            if (false == StringUtil.IsEmpty(startTime)) {
                psql.append(" and a.binddate>=" + startTime);
            }
            if (false == StringUtil.IsEmpty(endTime)) {
                psql.append(" and a.binddate<=" + endTime);
            }
        }
        if (DBUtil.GetDB() == 1) {
            psql.append(" and rownum< " + maxNum);
        }else  if (DBUtil.GetDB() == 3) {
        	 psql.append(" and limit " + maxNum);
        }

        return jt.querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage, num_splitPage, new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map(map, rs);
            }
        });
    }

    public int Kd(String gw_type, String cityId, String kdname) 
    {
        PrepareSQL psql = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	psql.append(" select count(*) ");
        }else{
        	psql.append(" select count(1) ");
        }
        psql.append("from tab_hgwcustomer a, hgwcust_serv_info b, tab_gw_device c " +
        		"where a.device_id=c.device_id and a.user_id = b.user_id ");
        //psql.append("   and c.device_status = 1"); // 设备已确认

        if (!StringUtil.IsEmpty(cityId) && !"null".equals(cityId) 
        		&& !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            psql.append("   and c.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }

        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
            psql.append(" and c.gw_type = " + gw_type);
        }

        // 宽带账号
        if (!StringUtil.IsEmpty(kdname)) {
            psql.append(" and b.serv_type_id = 10 and b.username='" + kdname + "'");
        }
        
        return jt.queryForInt(psql.getSQL());
    }

    public List<Map> isawifiDeviceByKdname(String gw_type, String cityId, String kdname) 
    {
        PrepareSQL psql = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        }
        psql.append("select f.is_awifi ");
        psql.append("from tab_hgwcustomer a,hgwcust_serv_info b,tab_gw_device c,"
        				+ "tab_vendor d,gw_device_model e,tab_devicetype_info f ");
        psql.append("where a.device_id=c.device_id and a.user_id = b.user_id ");
        psql.append("and c.vendor_id = d.vendor_id and c.device_model_id = e.device_model_id ");
        psql.append("and c.devicetype_id = f.devicetype_id and c.device_status = 1"); // 设备已确认

        if (!StringUtil.IsEmpty(cityId) && !"null".equals(cityId) 
        		&& !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            psql.append("   and c.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }

        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
            psql.append(" and c.gw_type = " + gw_type);
        }

        // 宽带账号
        if (!StringUtil.IsEmpty(kdname)) {
            psql.append(" and b.serv_type_id = 10 and b.username='" + kdname + "'");
        }
        List list = jt.queryForList(psql.getSQL());
        return list;
    }

    /**
     * 查询设备页面增加按照宽带账号查询(不分页)
     *
     * @param gw_type
     * @param cityId
     * @param kdname
     * @return
     * @author chenjie
     * @date 2012-4-18
     */
    public List queryDeviceByKdname(String gw_type, String cityId, String kdname) 
    {
        logger.debug("queryDeviceByKdname({},{},{})", new Object[]{gw_type, cityId, kdname});
        PrepareSQL psql = new PrepareSQL();

        String table_customer = "tab_hgwcustomer";
        String table_serv_info = "hgwcust_serv_info";

        if ("2".equals(gw_type)) {
            table_customer = "tab_egwcustomer";
            table_serv_info = "egwcust_serv_info";
        }
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	psql.append("select c.device_id,c.oui,c.device_serialnumber,c.device_name,c.city_id,"
        				+ "c.office_id,c.complete_time,c.zone_id,c.buy_time,c.staff_id,c.remark,"
        				+ "c.loopback_ip,c.interface_id,c.device_status,c.gather_id,c.devicetype_id,"
        				+ "c.maxenvelopes,c.cr_port,c.cr_path,c.cpe_mac,c.cpe_currentupdatetime,"
        				+ "c.cpe_allocatedstatus,c.cpe_username,c.cpe_passwd,c.acs_username,c.acs_passwd,"
        				+ "c.device_type,c.x_com_username,c.x_com_passwd,c.gw_type,c.device_model_id,"
        				+ "c.customer_id,c.device_url,c.x_com_passwd_old,c.vendor_id,");
        }else{
        	psql.append("select c.*,");
        }
        psql.append("d.vendor_add,e.device_model,f.softwareversion,f.is_awifi ");
        psql.append("from " + table_customer + " a," + table_serv_info + " b,tab_gw_device c,"
        			+ "tab_vendor d,gw_device_model e,tab_devicetype_info f ");
        psql.append("where a.device_id=c.device_id and a.user_id=b.user_id ");
        psql.append("and c.vendor_id=d.vendor_id and c.device_model_id=e.device_model_id ");
        psql.append("and c.devicetype_id=f.devicetype_id and c.device_status=1 "); // 设备已确认

        if (!StringUtil.IsEmpty(cityId) && !"null".equals(cityId) 
        		&& !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            psql.append("   and c.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }

        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
            psql.append(" and c.gw_type = " + gw_type);
        }

        // 宽带账号
        if (!StringUtil.IsEmpty(kdname)) {
            psql.append(" and b.serv_type_id = 10 and b.username='" + kdname + "'");
        }

        return jt.query(psql.getSQL(), new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map(map, rs);
            }
        });
    }

    /**
     * 查询设备页面增加按照宽带账号统计总数
     *
     * @param gw_type
     * @param cityId
     * @param kdname
     * @return
     * @author chenjie
     * @date 2012-4-18
     */
    public int queryDeviceCountByKdname(String gw_type, String cityId, String kdname) 
    {
        logger.debug("queryDeviceByKdname({},{},{})", new Object[]{gw_type, cityId, kdname});

        String table_customer = "tab_hgwcustomer";
        String table_serv_info = "hgwcust_serv_info";
        if ("2".equals(gw_type)) {
            table_customer = "tab_egwcustomer";
            table_serv_info = "egwcust_serv_info";
        }
        
        PrepareSQL psql = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	psql.append("select count(*) ");
        }else{
        	psql.append("select count(1) ");
        }
        psql.append("from " + table_customer + " a," + table_serv_info + " b,tab_gw_device c,");
        psql.append("tab_vendor d,gw_device_model e,tab_devicetype_info f ");
        psql.append("where a.device_id=c.device_id and a.user_id=b.user_id ");
        psql.append("and c.vendor_id=d.vendor_id and c.device_model_id=e.device_model_id ");
        psql.append("and c.devicetype_id=f.devicetype_id and c.device_status=1 "); // 设备已确认

        if (!StringUtil.IsEmpty(cityId) && !"null".equals(cityId) 
        		&& !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            psql.append("   and c.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }

        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
            psql.append(" and c.gw_type = " + gw_type);
        }

        // 宽带账号
        if (!StringUtil.IsEmpty(kdname)) {
            psql.append(" and b.serv_type_id = 10 and b.username='" + kdname + "'");
        }

        return jt.queryForInt(psql.getSQL());
    }

    public int queryDeviceCountByKdnameForSxlt(String gw_type, String cityId, 
    		String kdname, String startTime, String endTime, String timeType) 
    {
        logger.debug("queryDeviceByKdname({},{},{})", new Object[]{gw_type, cityId, kdname});

        String table_customer = "tab_hgwcustomer";
        String table_serv_info = "hgwcust_serv_info";
        if ("2".equals(gw_type)) {
            table_customer = "tab_egwcustomer";
            table_serv_info = "egwcust_serv_info";
        }
        
        PrepareSQL psql = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	psql.append("select count(*) ");
        }else{
        	psql.append("select count(1) ");
        }
        
        psql.append("from " + table_customer + " a," + table_serv_info + " b,tab_gw_device c,");
        psql.append("tab_vendor d,gw_device_model e,tab_devicetype_info f ");
        psql.append("where a.device_id=c.device_id and a.user_id=b.user_id ");
        psql.append("and c.vendor_id=d.vendor_id and c.device_model_id=e.device_model_id ");
        psql.append("and c.devicetype_id=f.devicetype_id and c.device_status=1 "); // 设备已确认

        if (!StringUtil.IsEmpty(cityId) && !"null".equals(cityId) 
        		&& !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            psql.append("   and c.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }

        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
            psql.append(" and c.gw_type = " + gw_type);
        }

        // 宽带账号
        if (!StringUtil.IsEmpty(kdname)) {
            psql.append(" and b.serv_type_id = 10 and b.username='" + kdname + "'");
        }

        if ("1".equals(timeType)) {
            if (false == StringUtil.IsEmpty(startTime)) {
                psql.append(" and c.complete_time>=" + startTime);
            }
            if (false == StringUtil.IsEmpty(endTime)) {
                psql.append(" and c.complete_time<=" + endTime);
            }
        }
        if ("2".equals(timeType)) {
            if (false == StringUtil.IsEmpty(startTime)) {
                psql.append(" and a.binddate>=" + startTime);
            }
            if (false == StringUtil.IsEmpty(endTime)) {
                psql.append(" and a.binddate<=" + endTime);
            }
        }
        return jt.queryForInt(psql.getSQL());
    }

    /**
     * 查询设备页面增加按照宽带账号查询(分页)
     *
     * @param gw_type
     * @param cityId
     * @param kdname
     * @param curPage_splitPage
     * @param num_splitPage
     * @return
     * @author chenjie
     * @date 2012-4-18
     */
    public List queryDeviceByKdname(String gw_type, String cityId, 
    		String kdname, int curPage_splitPage, int num_splitPage) 
    {
        logger.debug("queryDeviceByKdname({},{},{})", new Object[]{gw_type, cityId, kdname});

        String table_customer = "tab_hgwcustomer";
        String table_serv_info = "hgwcust_serv_info";

        if ("2".equals(gw_type)) {
            table_customer = "tab_egwcustomer";
            table_serv_info = "egwcust_serv_info";
        }
        
        PrepareSQL psql = new PrepareSQL();
        if (DBUtil.GetDB() == 2) {
            psql.append("select top " + maxNum+" ");
        } else {
            psql.append("select ");
        }
        
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	psql.append("c.device_id,c.oui,c.device_serialnumber,c.device_name,c.city_id,c.office_id,"
        				+ "c.complete_time,c.zone_id,c.buy_time,c.staff_id,c.remark,c.loopback_ip,"
        				+ "c.interface_id,c.device_status,c.gather_id,c.devicetype_id,"
        				+ "c.maxenvelopes,c.cr_port,c.cr_path,c.cpe_mac,c.cpe_currentupdatetime,"
        				+ "c.cpe_allocatedstatus,c.cpe_username,c.cpe_passwd,c.acs_username,c.acs_passwd,"
        				+ "c.device_type,c.x_com_username,c.x_com_passwd,c.gw_type,c.device_model_id,"
        				+ "c.customer_id,c.device_url,c.x_com_passwd_old,c.vendor_id,");
        }else{
        	psql.append("c.*,");
        }
        psql.append("d.vendor_add,e.device_model,f.softwareversion,f.is_awifi ");
        psql.append("from " + table_customer + " a," + table_serv_info + " b,tab_gw_device c,"
        				+ "tab_vendor d,gw_device_model e,tab_devicetype_info f ");
        psql.append("where a.device_id=c.device_id and a.user_id=b.user_id ");
        psql.append("and c.vendor_id=d.vendor_id and c.device_model_id=e.device_model_id ");
        psql.append("and c.devicetype_id=f.devicetype_id and c.device_status=1 "); // 设备已确认

        if (!StringUtil.IsEmpty(cityId) && !"null".equals(cityId) 
        		&& !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            psql.append("   and c.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }

        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
            psql.append(" and c.gw_type = " + gw_type);
        }

        // 宽带账号
        if (!StringUtil.IsEmpty(kdname)) {
            psql.append(" and b.serv_type_id = 10 and b.username='" + kdname + "'");
        }
        if (DBUtil.GetDB() == 1) {
            psql.append(" and rownum< " + maxNum);
        }else if (DBUtil.GetDB() == 2){
        	 psql.append(" and limit " + maxNum);
        }
        return jt.querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage, num_splitPage, new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map(map, rs);
            }
        });
    }

    public List queryDeviceByKdnameForSxlt(String gw_type, String cityId, 
    		String kdname,int curPage_splitPage,int num_splitPage,
    		String startTime,String endTime,String timeType) 
    {
        logger.debug("queryDeviceByKdname({},{},{})", new Object[]{gw_type, cityId, kdname});

        String table_customer = "tab_hgwcustomer";
        String table_serv_info = "hgwcust_serv_info";

        if ("2".equals(gw_type)) {
            table_customer = "tab_egwcustomer";
            table_serv_info = "egwcust_serv_info";
        }
       
        PrepareSQL psql = new PrepareSQL();
        if (DBUtil.GetDB() == 2) {
            psql.append("select top " + maxNum+" ");
        } else {
            psql.append("select ");
        }
        
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	psql.append("c.device_id,c.oui,c.device_serialnumber,c.device_name,c.city_id,c.office_id,"
        				+ "c.complete_time,c.zone_id,c.buy_time,c.staff_id,c.remark,c.loopback_ip,"
        				+ "c.interface_id,c.device_status,c.gather_id,c.devicetype_id,"
        				+ "c.maxenvelopes,c.cr_port,c.cr_path,c.cpe_mac,c.cpe_currentupdatetime,"
        				+ "c.cpe_allocatedstatus,c.cpe_username,c.cpe_passwd,c.acs_username,c.acs_passwd,"
        				+ "c.device_type,c.x_com_username,c.x_com_passwd,c.gw_type,c.device_model_id,"
        				+ "c.customer_id,c.device_url,c.x_com_passwd_old,c.vendor_id,");
        }else{
        	psql.append("c.*,");
        }
        psql.append("d.vendor_add,e.device_model,f.softwareversion,f.is_awifi ");
        psql.append("from " + table_customer + " a," + table_serv_info + " b,tab_gw_device c,"
        			+ "tab_vendor d,gw_device_model e,tab_devicetype_info f ");
        psql.append("where a.device_id=c.device_id and a.user_id=b.user_id ");
        psql.append("and c.vendor_id=d.vendor_id and c.device_model_id=e.device_model_id ");
        psql.append("and c.devicetype_id=f.devicetype_id and c.device_status=1 "); // 设备已确认

        if (!StringUtil.IsEmpty(cityId) && !"null".equals(cityId) 
        		&& !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            psql.append("   and c.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }

        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
            psql.append(" and c.gw_type = " + gw_type);
        }

        // 宽带账号
        if (!StringUtil.IsEmpty(kdname)) {
            psql.append(" and b.serv_type_id = 10 and b.username='" + kdname + "'");
        }

        if ("1".equals(timeType)) {
            if (false == StringUtil.IsEmpty(startTime)) {
                psql.append(" and c.complete_time>=" + startTime);
            }
            if (false == StringUtil.IsEmpty(endTime)) {
                psql.append(" and c.complete_time<=" + endTime);
            }
        }
        if ("2".equals(timeType)) {
            if (false == StringUtil.IsEmpty(startTime)) {
                psql.append(" and a.binddate>=" + startTime);
            }
            if (false == StringUtil.IsEmpty(endTime)) {
                psql.append(" and a.binddate<=" + endTime);
            }
        }
        if (DBUtil.GetDB() == 1) {
            psql.append(" and rownum< " + maxNum);
        }else if(DBUtil.GetDB()==3){
        	psql.append(" and limit " + maxNum);
        }
        return jt.querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage, num_splitPage, new RowMapper() {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                return resultSet2Map(map, rs);
            }
        });
    }

    /**
     * 查询设备列表(单独根据设备表的条件查询)
     */
    public int queryDeviceCount(String gw_type, long areaId, 
    		String cityId, String username) 
    {
        logger.debug("GwDeviceQueryDAO=>queryDeviceCount()");
        PrepareSQL pSQL = new PrepareSQL();
        String tableName = "tab_hgwcustomer";
        if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type)) {
            tableName = "tab_egwcustomer";
        }
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("select count(*) ");
        }else{
        	pSQL.append("select count(1) ");
        }
        pSQL.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d ");
        if (Global.SXLT.equals(instArea)) {
            pSQL.append("where a.device_status=1 ");
        } else {
            pSQL.append("," + tableName + " e ");
            pSQL.append("where a.device_status=1 and a.device_id=e.device_id ");
        }

        pSQL.append("and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id ");
        pSQL.append("and a.devicetype_id=d.devicetype_id ");

        if (!StringUtil.IsEmpty(cityId) && !"null".equals(cityId)
                && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        if (!StringUtil.IsEmpty(username)) {
            if (Global.SXLT.equals(instArea)) {
                if ("1".equalsIgnoreCase(LipossGlobals.getLipossProperty("isLikeQuery"))) {
                    pSQL.append(" and a.device_name like '%" + username + "' ");
                } else {
                    pSQL.appendAndString("a.device_name", PrepareSQL.EQUEAL, username);
                }
            } else {
                if ("1".equalsIgnoreCase(LipossGlobals.getLipossProperty("isLikeQuery"))) {
                    pSQL.append(" and e.username like '%" + username + "' ");
                    String user_sub_name = username;
                    if (username.length() > 6) {
                        user_sub_name = username.substring(username.length() - 6);
                    }
                    if ("tab_hgwcustomer".equals(tableName)) {
                        pSQL.append(" and e.user_sub_name='" + user_sub_name + "'");
                    }
                } else {
                    pSQL.appendAndString("e.username", PrepareSQL.EQUEAL, username);
                }
            }
        }

        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }

        return jt.queryForInt(pSQL.getSQL());
    }

    public int queryDeviceCountForSxlt(String gw_type, long areaId, 
    		String cityId, String username, String startTime, 
    		String endTime, String timeType) 
    {
        logger.debug("GwDeviceQueryDAO=>queryDeviceCountForSxlt()");
        PrepareSQL pSQL = new PrepareSQL();
        String tableName = "tab_hgwcustomer";
        if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type)) {
            tableName = "tab_egwcustomer";
        }
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	pSQL.append("select count(*) ");
        }else{
        	pSQL.append("select count(1) ");
        }
        pSQL.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d ");
        if (Global.SXLT.equals(instArea)) {
            pSQL.append("where a.device_status=1 ");
        } else {
            pSQL.append("," + tableName + " e ");
            pSQL.append("where a.device_status=1 and a.device_id=e.device_id ");
        }

        pSQL.append("and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id ");
        pSQL.append("and a.devicetype_id=d.devicetype_id ");

        if (!StringUtil.IsEmpty(cityId) && !"null".equals(cityId)
                && !"-1".equals(cityId) && !CENTER_CITY.equals(cityId)) {
            ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
            pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
            cityArray = null;
        }
        if (!StringUtil.IsEmpty(username)) {
            if (Global.SXLT.equals(instArea)) {
                if ("1".equalsIgnoreCase(LipossGlobals.getLipossProperty("isLikeQuery"))) {
                    pSQL.append(" and a.device_name like '%" + username + "' ");
                } else {
                    pSQL.appendAndString("a.device_name", PrepareSQL.EQUEAL, username);
                }
            } else {
                if ("1".equalsIgnoreCase(LipossGlobals.getLipossProperty("isLikeQuery"))) {
                    pSQL.append(" and e.username like '%" + username + "' ");
                    String user_sub_name = username;
                    if (username.length() > 6) {
                        user_sub_name = username.substring(username.length() - 6);
                    }
                    if ("tab_hgwcustomer".equals(tableName)) {
                        pSQL.append(" and e.user_sub_name='" + user_sub_name + "'");
                    }
                } else {
                    pSQL.appendAndString("e.username", PrepareSQL.EQUEAL, username);
                }
            }
        }

        if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
            pSQL.append(" and a.gw_type = " + gw_type);
        }
        if ("1".equals(timeType)) {
            if (false == StringUtil.IsEmpty(startTime)) {
                pSQL.append(" and a.complete_time>=" + startTime);
            }
            if (false == StringUtil.IsEmpty(endTime)) {
                pSQL.append(" and a.complete_time<=" + endTime);
            }
        }
        /*if ("2".equals(timeType)) {
            pSQL.setSQL("select count(1) from tab_gw_device a left join tab_hgwcustomer b on a.device_id=b.device_id where 1=1 ");
            if (false == com.linkage.module.gwms.util.StringUtil.IsEmpty(startTime)) {
                pSQL.append(" and b.binddate>=" + startTime);
            }
            if (false == com.linkage.module.gwms.util.StringUtil.IsEmpty(endTime)) {
                pSQL.append(" and b.binddate<=" + endTime);
            }
        }*/

        return jt.queryForInt(pSQL.getSQL());
    }

    /**
     * 数据转换
     *
     * @param map
     * @param rs
     * @return
     * @throws SQLException
     */
    public Map<String, String> resultSet2Map(Map<String, String> map, ResultSet rs) 
    {
        try {
        	//device_id,oui,device_serialnumber,device_name,city_id,office_id,
        	//complete_time,zone_id,buy_time,staff_id,remark,loopback_ip,
        	//interface_id,device_status,gather_id,devicetype_id,softwareversion,
        	//maxenvelopes,cr_port,cr_path,cpe_mac,cpe_currentupdatetime,
        	//cpe_allocatedstatus,cpe_username,cpe_passwd,acs_username,acs_passwd,
        	//device_type,x_com_username,x_com_passwd,gw_type,device_model_id,
            //device_model,customer_id,device_url,x_com_passwd_old,vendor_id,vendor_add,is_awifi
            map.put("device_id", rs.getString("device_id"));
            map.put("oui", rs.getString("oui"));
            map.put("device_serialnumber", rs.getString("device_serialnumber"));
            map.put("device_name", rs.getString("device_name"));
            map.put("city_id", rs.getString("city_id"));
            map.put("city_name", CityDAO.getCityIdCityNameMap().get(rs.getString("city_id")));
            map.put("office_id", rs.getString("office_id"));
            map.put("complete_time", new DateTimeUtil(rs.getLong("complete_time") * 1000).getYYYY_MM_DD_HH_mm_ss());
            map.put("zone_id", rs.getString("zone_id"));
            map.put("buy_time", new DateTimeUtil(rs.getLong("buy_time") * 1000).getYYYY_MM_DD_HH_mm_ss());
            map.put("staff_id", rs.getString("staff_id"));
            map.put("remark", rs.getString("remark"));
            map.put("loopback_ip", rs.getString("loopback_ip"));
            map.put("interface_id", rs.getString("interface_id"));
            map.put("device_status", rs.getString("device_status"));
            map.put("gather_id", rs.getString("gather_id"));
            map.put("devicetype_id", rs.getString("devicetype_id"));
            map.put("softwareversion", rs.getString("softwareversion"));
            map.put("maxenvelopes", rs.getString("maxenvelopes"));
            map.put("cr_port", rs.getString("cr_port"));
            map.put("cr_path", rs.getString("cr_path"));
            map.put("cpe_mac", rs.getString("cpe_mac"));
            map.put("cpe_currentupdatetime", new DateTimeUtil(rs.getLong("cpe_currentupdatetime") * 1000).getYYYY_MM_DD_HH_mm_ss());
            map.put("cpe_allocatedstatus", rs.getString("cpe_allocatedstatus"));
            map.put("cpe_username", rs.getString("cpe_username"));
            map.put("cpe_passwd", rs.getString("cpe_passwd"));
            map.put("acs_username", rs.getString("acs_username"));
            map.put("acs_passwd", rs.getString("acs_passwd"));
            map.put("device_type", rs.getString("device_type"));
            map.put("x_com_username", rs.getString("x_com_username"));
            map.put("x_com_passwd", rs.getString("x_com_passwd"));
            map.put("gw_type", rs.getString("gw_type"));
            map.put("device_model_id", rs.getString("device_model_id"));
            map.put("device_model", rs.getString("device_model"));
            map.put("customer_id", rs.getString("customer_id"));
            map.put("device_url", rs.getString("device_url"));
            map.put("x_com_passwd_old", rs.getString("x_com_passwd_old"));
            map.put("vendor_id", rs.getString("vendor_id"));
            map.put("vendor_add", rs.getString("vendor_add"));
            try {
                map.put("is_awifi", rs.getString("is_awifi"));
            } catch (Exception e) {
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return map;
    }

    /**
     * 数据转换
     *
     * @param map
     * @param rs
     * @return
     * @throws SQLException
     */
    public Map<String, String> resultSet2Map4Wireless(Map<String, String> map, ResultSet rs) 
    {
        try {
        	//device_id,oui,device_serialnumber,device_name,city_id,office_id,complete_time,
        	//zone_id,buy_time,staff_id,remark,loopback_ip,interface_id,device_status,gather_id,
        	 //devicetype_id,softwareversion,maxenvelopes,cr_port,cr_path,cpe_mac,
            //cpe_currentupdatetime,cpe_allocatedstatus,cpe_username,cpe_passwd,
            //acs_username,acs_passwd,device_type,x_com_username,x_com_passwd,gw_type,
            //device_model_id,device_model,customer_id,device_url,x_com_passwd_old,
            //vendor_id,vendor_add,username,user_id,ssid,vlanid,priority,channel
          //js-dx wlanport,paramvalue
            map.put("device_id", rs.getString("device_id"));
            map.put("oui", rs.getString("oui"));
            map.put("device_serialnumber", rs.getString("device_serialnumber"));
            map.put("device_name", rs.getString("device_name"));
            map.put("city_id", rs.getString("city_id"));
            map.put("city_name", CityDAO.getCityIdCityNameMap().get(rs.getString("city_id")));
            map.put("office_id", rs.getString("office_id"));
            map.put("complete_time", new DateTimeUtil(rs.getLong("complete_time") * 1000).getYYYY_MM_DD_HH_mm_ss());
            map.put("zone_id", rs.getString("zone_id"));
            map.put("buy_time", new DateTimeUtil(rs.getLong("buy_time") * 1000).getYYYY_MM_DD_HH_mm_ss());
            map.put("staff_id", rs.getString("staff_id"));
            map.put("remark", rs.getString("remark"));
            map.put("loopback_ip", rs.getString("loopback_ip"));
            map.put("interface_id", rs.getString("interface_id"));
            map.put("device_status", rs.getString("device_status"));
            map.put("gather_id", rs.getString("gather_id"));
            map.put("devicetype_id", rs.getString("devicetype_id"));
            map.put("softwareversion", rs.getString("softwareversion"));
            map.put("maxenvelopes", rs.getString("maxenvelopes"));
            map.put("cr_port", rs.getString("cr_port"));
            map.put("cr_path", rs.getString("cr_path"));
            map.put("cpe_mac", rs.getString("cpe_mac"));
            map.put("cpe_currentupdatetime", new DateTimeUtil(rs.getLong("cpe_currentupdatetime") * 1000).getYYYY_MM_DD_HH_mm_ss());
            map.put("cpe_allocatedstatus", rs.getString("cpe_allocatedstatus"));
            map.put("cpe_username", rs.getString("cpe_username"));
            map.put("cpe_passwd", rs.getString("cpe_passwd"));
            map.put("acs_username", rs.getString("acs_username"));
            map.put("acs_passwd", rs.getString("acs_passwd"));
            map.put("device_type", rs.getString("device_type"));
            map.put("x_com_username", rs.getString("x_com_username"));
            map.put("x_com_passwd", rs.getString("x_com_passwd"));
            map.put("gw_type", rs.getString("gw_type"));
            map.put("device_model_id", rs.getString("device_model_id"));
            map.put("device_model", rs.getString("device_model"));
            map.put("customer_id", rs.getString("customer_id"));
            map.put("device_url", rs.getString("device_url"));
            map.put("x_com_passwd_old", rs.getString("x_com_passwd_old"));
            map.put("vendor_id", rs.getString("vendor_id"));
            map.put("vendor_add", rs.getString("vendor_add"));
            map.put("username", StringUtil.getStringValue(rs.getString("username")));
            try {
                map.put("user_id", StringUtil.getStringValue(rs.getString("user_id")));
            } catch (Exception e) {
            }
            
            if (LipossGlobals.inArea(Global.JSDX)) {
                map.put("wlanport", StringUtil.getStringValue(rs.getString("wlanport")));
                map.put("paramvalue", StringUtil.getStringValue(rs.getString("paramvalue")));
            }
            try {
                map.put("ssid", StringUtil.getStringValue(rs.getString("ssid")));
            } catch (Exception e) {
            }
            try {
                map.put("vlanid", StringUtil.getStringValue(rs.getString("vlanid")));
            } catch (Exception e) {
            }
            try {
                map.put("priority", StringUtil.getStringValue(rs.getString("priority")));
            } catch (Exception e) {
            }
            try {
                map.put("channel", StringUtil.getStringValue(rs.getString("channel")));
            } catch (Exception e) {
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return map;
    }

    /**
     * 转换IP地址，满足数据库查询要求
     *
     * @param IP 例如：10.1.55.72
     * @return 转换后IP 例如： 010001055072
     */
    public String conventIP(String ip) 
    {
        String result = "";
        String[] array = ip.split("\\.");
        for (int i = 0; i < array.length; i++) {
            while (array[i].length() < 3) { // 一共被分成了四个字符串，字符串里已经没有了小点，如果一个字符串的长度小于三，那么就在前面加零
                array[i] = "0" + array[i];
            }
        }
        for (int i = 0; i < array.length; i++) {
            result += array[i]; // 将处理好的四个字符串连起来
        }
        return result;
    }
}
