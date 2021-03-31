package com.linkage.liposs.buss.dao.securitygw;

/**
 * @author Owner(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-2
 * @category com.linkage.liposs.buss.dao.securitygw
 * 版权：南京联创科技 网管科技部
 *
 */
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.liposs.buss.util.CommonUtil;
import com.linkage.liposs.buss.util.DateUtils;
import com.linkage.liposs.buss.util.JdbcTemplateExtend;

public class EntFtpSiteDAO {
	private static Logger log = LoggerFactory.getLogger(BehaviorAnalyseDAO.class);
    private JdbcTemplate jt;
    public DataSource ds;
    private JdbcTemplateExtend spjt;
    private String sqlTemp = "";


    public void setDao(DataSource ds) {
    	log.debug("ds");
        this.ds = ds;
        jt = new JdbcTemplate(ds);
        this.spjt = new JdbcTemplateExtend(ds);
    }

    public Map getDeviceInfo(String deviceId) {
        String sql = "select device_name,loopback_ip,device_model_id as device_model " +
                     "from tab_gw_device where device_id='" + deviceId + "'";
        PrepareSQL psql = new PrepareSQL(sql);
        return jt.queryForMap(psql.getSQL());
    }

    public List<Map> getEntFtpTopnTodayByHour(String deviceId, long startTime, long endTime) {
        String tableName = "sgw_conn_hour_" + DateUtils.getYearMonthDayString(endTime, "_", true);
        if (CommonUtil.tableIsExist(tableName, jt) == 0) {
            return null;
        }

        String sql = "select targetip,sum(times) times, '企业FTP统计' colName from " + tableName + " where "; //sgw_conn_hour_yyyy_mm_dd
        sql += " deviceid='" + deviceId + "' ";
        sql += " and stime>=" + startTime + " ";
        sql += " and stime<=" + endTime + " ";
        sql += " and protocoltype=1";
        sql += " group by targetip";
        sql += " order by times desc";

//        log.debug("[getEntFtpTopnTodayByHour]" + sql);
        PrepareSQL psql = new PrepareSQL(sql);
        List<Map> ftpTopnList = jt.queryForList(psql.getSQL());

        if (ftpTopnList.size() > 10) {
            for (int i = ftpTopnList.size() - 1; i >= 10; i--) {
                ftpTopnList.remove(i);
            }
        }

        return ftpTopnList;
    }


    public List<Map> getEntFtpTopnWeekByDay(String deviceId, long startTime, long endTime) {
        String tableName = "sgw_conn_day_" + DateUtils.getYearMonthString(endTime, "_", true);
        if (CommonUtil.tableIsExist(tableName, jt) == 0) {
            return null;
        }

        String sql = "select targetip,sum(times) times, '企业FTP统计' colName from " + tableName + " where "; //sgw_conn_day_yyyy_mm
        sql += " deviceid='" + deviceId + "' ";
        sql += " and stime>=" + startTime + " ";
        sql += " and stime<=" + endTime + " ";
        sql += " and protocoltype=1";
        sql += " group by targetip";
        sql += " order by times desc";

//        log.debug("[getEntFtpTopnWeekByDay]" + sql);
        PrepareSQL psql = new PrepareSQL(sql);
        List<Map> ftpTopnList = jt.queryForList(psql.getSQL());

        if (ftpTopnList.size() > 10) {
            for (int i = ftpTopnList.size() - 1; i >= 10; i--) {
                ftpTopnList.remove(i);
            }
        }
        return ftpTopnList;
    }

    public List<Map> getEntFtpTopnMonthByDay(String deviceId, long startTime, long endTime) {
        String tableName = "sgw_conn_day_" + DateUtils.getYearMonthString(endTime, "_", true);
        if (CommonUtil.tableIsExist(tableName, jt) == 0) {
            return null;
        }

        String sql = "select targetip,sum(times) times, '企业FTP统计' colName from " + tableName + " where "; //sgw_conn_day_yyyy_mm

        sql += " deviceid='" + deviceId + "' ";
        sql += " and stime>=" + startTime + " ";
        sql += " and stime<=" + endTime + " ";
        sql += " and protocoltype=1";
        sql += " group by targetip";
        sql += " order by times desc";

//        log.debug("[getEntFtpTopnMonthByDay]" + sql);
        PrepareSQL psql = new PrepareSQL(sql);
        List<Map> ftpTopnList = jt.queryForList(psql.getSQL());

        if (ftpTopnList.size() > 10) {
            for (int i = ftpTopnList.size() - 1; i >= 10; i--) {
                ftpTopnList.remove(i);
            }
        }
        return ftpTopnList;
    }


}
