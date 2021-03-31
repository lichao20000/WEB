package com.linkage.liposs.buss.dao.securitygw;

/**
 * @author Owner(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-2
 * @category com.linkage.liposs.buss.dao.securitygw
 * 版权：南京联创科技 网管科技部
 *
 */

import java.util.ArrayList;
import java.util.Calendar;
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

public class BehaviorAnalyseDAO {
	private static Logger log = LoggerFactory.getLogger(BehaviorAnalyseDAO.class);
    private JdbcTemplate jt;
    public DataSource ds;
    private JdbcTemplateExtend spjt;
    private String sqlTemp = "";
    public void setDao(DataSource ds) {
        //log.debug("ds");
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

    public List<Map> getNetFluxAnalysToday(String deviceId, long startTime, long endTime, int protocoltype) {
        String tableName = "sgw_conn_hour_" + DateUtils.getYearMonthDayString(endTime, "_", true);
        if (CommonUtil.tableIsExist(tableName, jt) == 0) {
            return new ArrayList<Map>();
        }

        String sql = "select floor(sum(a.flux)/1000) fluxsum,a.stime,a.protocoltype ptype from " + tableName
                     + " a where "; //sgw_conn_hour_yyyy_mm_dd
        sql += " a.deviceid='" + deviceId + "' ";
        sql += " and a.stime>=" + startTime + " ";
        sql += " and a.stime<=" + endTime + " ";
        if (protocoltype != Integer.MAX_VALUE) {
            sql += " and a.protocoltype=" + protocoltype;
        }
        sql += " group by a.protocoltype,a.stime";
        sql += " order by a.stime desc,a.protocoltype";

//        log.debug(sql);
        PrepareSQL psql = new PrepareSQL(sql);
        List<Map> list = jt.queryForList(psql.getSQL());
        addProtocolNames(list);
        return list;
    }

    public List<Map> getNetFluxAnalysWeek(String deviceId, long startTime, long endTime, int protocoltype) {
        return getNetFluxFromDayTables(deviceId, startTime, endTime, protocoltype);
    }

    public List<Map> getNetFluxAnalysMonth(String deviceId, long startTime, long endTime, int protocoltype) {
        return getNetFluxFromDayTables(deviceId, startTime, endTime, protocoltype);
    }

    public List<Map> getNetFluxAnalysPopup(String deviceId, long startTime, long endTime, int protocoltype) {
        if (endTime - startTime > 3600 * 24) {
            return getNetFluxFromDayTables(deviceId, startTime, endTime, protocoltype);
        } else {
            return getNetFluxAnalysToday(deviceId, startTime, endTime, protocoltype);
        }
    }

    private List<Map> getNetFluxFromDayTables(String deviceId, long startTime, long endTime, int protocoltype) {
        String selectStr = "select floor(sum(a.flux)/1000) fluxsum,a.stime,a.protocoltype ptype from ";
        String tailStr = " a where  a.deviceid='" + deviceId + "' ";
        tailStr += " and a.stime>=" + startTime + " ";
        tailStr += " and a.stime<=" + endTime + " ";
        if (protocoltype != Integer.MAX_VALUE) {
            tailStr += " and a.protocoltype=" + protocoltype;
        }
        tailStr += " group by a.protocoltype,a.stime ";
        tailStr += " order by a.stime desc,a.protocoltype ";

        List<String> tableNames = getTablesInPeriod(startTime, endTime);
        List<String> existsTableNames = CommonUtil.getExistTables(tableNames);
        List<Map> retList = new ArrayList<Map>();
        for (int i = 0; i < existsTableNames.size(); i++) {
            String sql = selectStr + existsTableNames.get(i) + tailStr;
            List<Map> listMonth = getNetFluxBySql(sql);
            if (listMonth != null) {
                for (int j = 0; j < listMonth.size(); j++) {
                    retList.add(listMonth.get(j));
                }
            }
        }

        return retList;
    }


    private List<Map> getNetFluxBySql(String sql) {
        //log.debug("[getNetFluxBySql]" + sql);
    	PrepareSQL psql = new PrepareSQL(sql);
        List<Map> list = jt.queryForList(psql.getSQL());
        addProtocolNames(list);
        return list;
    }

    public int getSumFluxByProtocol(String deviceId, long startTime, long endTime, int protocoltype) {
        String selectStr = "select floor(sum(a.flux)/1000) fluxsum from ";
        String tailStr = " a  where  a.deviceid='" + deviceId + "' ";
        tailStr += " and a.stime>=" + startTime + " ";
        tailStr += " and a.stime<=" + endTime + " ";
        if (protocoltype != Integer.MAX_VALUE) {
            tailStr += " and a.protocoltype=" + protocoltype;
        }

        if (endTime - startTime <= 3600 * 24) {
            String tableName = "sgw_conn_hour_" + DateUtils.getYearMonthDayString(endTime, "_", true);
            return jt.queryForInt(selectStr + tableName + tailStr);
        } else {
            List<String> tableNames = getTablesInPeriod(startTime, endTime);
            List<String> existsTableNames = CommonUtil.getExistTables(tableNames);
            int allSum = 0;
            for (int i = 0; i < existsTableNames.size(); i++) {
                String sql = selectStr + existsTableNames.get(i) + tailStr;
                PrepareSQL psql = new PrepareSQL(sql);
                allSum += jt.queryForInt(psql.getSQL());
            }
            return allSum;
        }
    }

    private void addProtocolNames(List<Map> list) {
        String[] protocolNames = {"http", "ftp", "smtp", "pop3"}; //, "dns", "tcp", "udp"
        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            int protocoltype = ((java.math.BigDecimal) map.get("ptype")).intValue();
            if ((protocoltype >= 0) && (protocoltype < protocolNames.length)) {
                map.put("protocolName", protocolNames[protocoltype]);
            } else {
                map.put("protocolName", "其它");
            }
        }
    }

    public List<String> getTablesInPeriod(long stime, long etime) {
        List<String> list = new ArrayList<String>();
        long tableTimeInterval = 24 * 3600;
        if (etime - stime < 24 * 3600) {
            list.add(DateUtils.getYearMonthString(etime, "_", true));
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(stime * 1000);
            int y0 = cal.get(Calendar.YEAR);
            int m0 = cal.get(Calendar.MONTH) + 1;
            cal.setTimeInMillis(etime * 1000);
            int y1 = cal.get(Calendar.YEAR);
            int m1 = cal.get(Calendar.MONTH) + 1;
            for (int t = 0; t <= (y1 * 12 + m1) - (y0 * 12 + m0); t++) {
                int y = y0 + (t + m0) / 12;
                int m = (t + m0) % 12;
                String stmp = "sgw_conn_day_" + y + "_";
                if (m < 10) {
                    stmp += "0";
                }
                stmp += m;
                log.warn(stmp);
                list.add(stmp);
            }
        }
        return list;
    }

}
