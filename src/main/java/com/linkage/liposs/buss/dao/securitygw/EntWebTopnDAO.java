package com.linkage.liposs.buss.dao.securitygw;

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

/**
 * @author Owner(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-1
 * @category com.linkage.liposs.buss.dao.securitygw 版权：南京联创科技 网管科技部
 *
 */
public class EntWebTopnDAO {
	private static Logger log = LoggerFactory.getLogger(EntWebTopnDAO.class);
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

    public List<Map> getEntWebTopnTodayByHour(String deviceId, long startTime, long endTime) {
        String tableName = "sgw_conn_hour_" + DateUtils.getYearMonthDayString(endTime, "_", true);
        if (CommonUtil.tableIsExist(tableName, jt) == 0) {
            return null;
        }

        String sql = "select targetip,sum(times) times, '上网统计' colName from " + tableName + " where "; //sgw_conn_hour_yyyy_mm_dd
        sql += " deviceid='" + deviceId + "' ";
        sql += " and stime>=" + startTime + " ";
        sql += " and stime<=" + endTime + " ";
        sql += " and protocoltype=0";
        sql += " group by targetip";
        sql += " order by times desc";

//        log.debug(sql);
        PrepareSQL psql = new PrepareSQL(sql);
        List<Map> webTopnList = jt.queryForList(psql.getSQL());

        if (webTopnList.size() > 10) {
            for (int i = webTopnList.size() - 1; i >= 10; i--) {
                webTopnList.remove(i);
            }
        }

        return webTopnList;
    }


    public List<Map> getEntWebTopnWeekByDay(String deviceId, long startTime, long endTime) {
        String tableName = "sgw_conn_day_" + DateUtils.getYearMonthString(endTime, "_", true);
        if (CommonUtil.tableIsExist(tableName, jt) == 0) {
            return null;
        }

        String sql = "select targetip,sum(times) times, '上网统计' colName from " + tableName + " where ";
        sql += " deviceid='" + deviceId + "' ";
        sql += " and stime>=" + startTime + " ";
        sql += " and stime<=" + endTime + " ";
        sql += " and protocoltype=0";
        sql += " group by targetip";
        sql += " order by times desc";

//        log.debug(sql);
        PrepareSQL psql = new PrepareSQL(sql);
        List<Map> webTopnList = jt.queryForList(psql.getSQL());

        if (webTopnList.size() > 10) {
            for (int i = webTopnList.size() - 1; i >= 10; i--) {
                webTopnList.remove(i);
            }
        }
        return webTopnList;
    }

    public List<Map> getEntWebTopnMonthByDay(String deviceId, long startTime, long endTime) {
        String tableName = "sgw_conn_day_" + DateUtils.getYearMonthString(endTime, "_", true);
        if (CommonUtil.tableIsExist(tableName, jt) == 0) {
            return null;
        }

        String sql = "select targetip,sum(times) times, '上网统计' colName from " + tableName + " where ";
        sql += " deviceid='" + deviceId + "' ";
        sql += " and stime>=" + startTime + " ";
        sql += " and stime<=" + endTime + " ";
        sql += " and protocoltype=0";
        sql += " group by targetip";
        sql += " order by times desc";

//        log.debug(sql);
        PrepareSQL psql = new PrepareSQL(sql);
        List<Map> webTopnList = jt.queryForList(psql.getSQL());

        if (webTopnList.size() > 10) {
            for (int i = webTopnList.size() - 1; i >= 10; i--) {
                webTopnList.remove(i);
            }
        }
        return webTopnList;
    }
}
