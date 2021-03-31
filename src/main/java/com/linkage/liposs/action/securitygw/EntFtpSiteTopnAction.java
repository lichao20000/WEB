package com.linkage.liposs.action.securitygw;

/**
 * @author Owner(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-1
 * @category com.linkage.liposs.action.securitygw
 * 版权：南京联创科技 网管科技部
 *
 */
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.liposs.buss.bio.securitygw.EntFtpSiteBIO;
import com.linkage.liposs.buss.dao.securitygw.EntFtpSiteDAO;
import com.linkage.liposs.buss.util.ChartUtil;
import com.linkage.liposs.buss.util.DateUtils;
import com.opensymphony.xwork2.ActionSupport;

public class EntFtpSiteTopnAction extends ActionSupport {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(EntFtpSiteTopnAction.class);
    private static final long serialVersionUID = 5205080401L;
    Long today = 0L, before7 = 0L, before30 = 0L;
    String dayUrl = "/", weekUrl = "", monthUrl = "", popupUrl = "";
    List list = null;
    private String ajax;
    Long time = 0L, stime = 0L;
    private JFreeChart chart = null;
    private ChartUtil chartUtil = null;
    private EntFtpSiteBIO entFtpSite;
    private EntFtpSiteDAO entFtpDAO;
    private Map deviceInfoMap;
    private String deviceid;
    private String remark;
    String datetime, starttime;
    String weekBegin, monthBegin;
    String deviceIp, deviceModel, deviceName;
    String top10Today, top10ByWeek, top10ByMonth, top10ByPeriod;
    String parstr;

    public EntFtpSiteTopnAction() {
        super();
        entFtpSite = new EntFtpSiteBIO();
    }

    public String toEntFtpSiteTopn() throws Exception {
        if (time == 0) {
            time = System.currentTimeMillis() / 1000;
        }
        parstr = "time=" + time + "&deviceid=" + deviceid;
        dayUrl = "/securitygw/EntFtpTopn!entFtpTopnToday.action?" + parstr;
        weekUrl = "/securitygw/EntFtpTopn!entFtpTopnWeek.action?" + parstr;
        monthUrl = "/securitygw/EntFtpTopn!entFtpTopnMonth.action?" + parstr;

        deviceInfoMap = entFtpDAO.getDeviceInfo(deviceid);
        deviceIp = (String) deviceInfoMap.get("loopback_ip");
        deviceModel = (String) deviceInfoMap.get("device_model");
        deviceName = (String) deviceInfoMap.get("device_name");

        top10Today = getTop10(1);
        top10ByWeek = getTop10(2);
        top10ByMonth = getTop10(3);

        logger.debug("toEntFtpSiteTopn:time=" + time);
        return SUCCESS;
    }

    public String popup() throws Exception {
        if (time == 0) {
            time = System.currentTimeMillis() / 1000;
        }
        String paramstr = "time=" + time + "&deviceid=" + deviceid;
        popupUrl = "/securitygw/EntFtpTopn!entFtpTopnPopup.action?" + paramstr;

        deviceInfoMap = entFtpDAO.getDeviceInfo(deviceid);
        deviceIp = (String) deviceInfoMap.get("loopback_ip");
        deviceModel = (String) deviceInfoMap.get("device_model");
        deviceName = (String) deviceInfoMap.get("device_name");

        top10ByPeriod = getTop10(4);

        logger.debug("toEntFtpSiteTopn:time=" + time);
        return "popup";
    }

    public String entFtpTopnToday() {
        if (time == 0) {
            time = System.currentTimeMillis() / 1000;
        }

        entFtpSite.setEntFtpSiteDAO(entFtpDAO);
        entFtpSite.setCu(chartUtil);
        this.chart = entFtpSite.entFtpTopnToday(deviceid, time);
        return "entFtpTopnToday";
    }

    public String entFtpTopnWeek() {
        if (time == 0) {
            time = System.currentTimeMillis() / 1000;
        }

        entFtpSite.setEntFtpSiteDAO(entFtpDAO);
        entFtpSite.setCu(chartUtil);
        this.chart = entFtpSite.entFtpTopnWeek(deviceid, time);
        return "entFtpTopnWeek";
    }

    public String entFtpTopnMonth() {
        if (time == 0) {
            time = System.currentTimeMillis() / 1000;
        }

        entFtpSite.setEntFtpSiteDAO(entFtpDAO);
        entFtpSite.setCu(chartUtil);
        this.chart = entFtpSite.entFtpTopnMonth(deviceid, time);
        return "entFtpTopnMonth";
    }

    public String entFtpTopnPopup() {
        top10ByPeriod = getTop10(4);

        entFtpSite.setEntFtpSiteDAO(entFtpDAO);
        entFtpSite.setCu(chartUtil);
        this.chart = entFtpSite.entFtpTopnPopup(deviceid, stime, time);
        return "entFtpTopnPopup";
    }

    public String getWeekBegin() {
        if (weekBegin == null) {
            if (time == 0) {
                time = new Date().getTime() / 1000;
            }
            weekBegin = DateUtils.getYearMonthDayString(time - 7 * 24 * 3600, "-", true);
        }
        return weekBegin;
    }

    public String getMonthBegin() {
        if (monthBegin == null) {
            if (time == 0) {
                time = new Date().getTime() / 1000;
            }
            monthBegin = DateUtils.getYearMonthDayString(time - 31 * 24 * 3600, "-", true);
        }
        return monthBegin;
    }

    public String getDatetime() {
        if (datetime == null) {
            if (time == 0) {
                time = new Date().getTime() / 1000;
            }
            datetime = DateUtils.getYearMonthDayString(time, "-", true);
        }
        return datetime;
    }

    public EntFtpSiteDAO getEntFtpDAO() {
        return entFtpDAO;
    }

    public void setEntFtpDAO(EntFtpSiteDAO entFtpDAO) {
        this.entFtpDAO = entFtpDAO;
    }

    public EntFtpSiteBIO getEntFtpSite() {
        return entFtpSite;
    }

    public void setEntFtpSite(EntFtpSiteBIO entFtpSite) {
        this.entFtpSite = entFtpSite;
    }

    public String getTop10Today() {
        return top10Today;
    }

    public String getTop10ByWeek() {
        return top10ByWeek;
    }

    public String getTop10ByMonth() {
        return top10ByMonth;
    }

    public String getAjax() {
        return ajax;
    }

    public void setAjax(String ajax) {
        this.ajax = ajax;
    }

    public Long getBefore30() {
        return before30;
    }

    public void setBefore30(Long before30) {
        this.before30 = before30;
    }

    public Long getBefore7() {
        return before7;
    }

    public void setBefore7(Long before7) {
        this.before7 = before7;
    }

    public String getDayUrl() {
        return dayUrl;
    }

    public void setDayUrl(String dayUrl) {
        this.dayUrl = dayUrl;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public String getMonthUrl() {
        return monthUrl;
    }

    public void setMonthUrl(String monthUrl) {
        this.monthUrl = monthUrl;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getToday() {
        return today;
    }

    public void setToday(Long today) {
        this.today = today;
    }

    public String getWeekUrl() {
        return weekUrl;
    }

//    public SgwPerformanceDao getSgwPerformanceDao() {
//        return sgwPerformanceDao;
//    }

    public String getRemark() {
        return remark;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public Map getDeviceInfoMap() {
        return deviceInfoMap;
    }

    public ChartUtil getChartUtil() {
        return chartUtil;
    }


    public void setWeekUrl(String weekUrl) {
        this.weekUrl = weekUrl;
    }

//    public void setSgwPerformanceDao(SgwPerformanceDao sgwPerformanceDao) {
//        this.sgwPerformanceDao = sgwPerformanceDao;
//    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public void setDeviceInfoMap(Map deviceInfoMap) {
        this.deviceInfoMap = deviceInfoMap;
    }

    public void setChartUtil(ChartUtil chartUtil) {
        this.chartUtil = chartUtil;
    }

    public JFreeChart getChart() {
        return chart;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public String getStarttime() {
        if (starttime == null) {
            if (time == 0) {
                time = new Date().getTime() / 1000;
            }
            starttime = DateUtils.getYearMonthDayString(time - 3600 * 24 * 7, "-", true);
        }
        return starttime;
    }

    public String getParstr() {
        return parstr;
    }

    public String getPopupUrl() {
        return popupUrl;
    }

    public Long getStime() {
        return stime;
    }

    public String getTop10ByPeriod() {
        return top10ByPeriod;
    }

    public void setChart(JFreeChart chart) {
        this.chart = chart;
    }

    public void setWeekBegin(String weekBegin) {
        this.weekBegin = weekBegin;
    }

    public void setMonthBegin(String monthBegin) {
        this.monthBegin = monthBegin;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public void setStime(Long stime) {
        this.stime = stime;
    }

    private String getTop10(int type) {
        if (time == 0) {
            time = (new Date()).getTime() / 1000;
        }
        long lastday = 0;
        List<Map> list;
        if (type == 3) {
            lastday = time - 3600 * 24 * 31;
            list = entFtpDAO.getEntFtpTopnMonthByDay(deviceid, lastday, time);
        } else if (type == 2) {
            lastday = time - 3600 * 24 * 7;
            list = entFtpDAO.getEntFtpTopnWeekByDay(deviceid, lastday, time);
        } else if (type == 4) {
            if (time - stime > 24 * 3600) {
                list = entFtpDAO.getEntFtpTopnMonthByDay(deviceid, stime, time);
            } else {
                list = entFtpDAO.getEntFtpTopnTodayByHour(deviceid, stime, time);
            }
        } else {
            lastday = time - 3600 * 24;
            list = entFtpDAO.getEntFtpTopnTodayByHour(deviceid, lastday, time);
        }

        int listSize = (list == null) ? 0 : list.size();
        long totalTimes = 0;
        for (int i = 0; i < 10; i++) {
            if ((list != null) && (i < listSize)) {
                Map mp = (Map) list.get(i);
                java.math.BigDecimal bdTimes = (java.math.BigDecimal) mp.get("times");
                totalTimes += bdTimes.longValue();
            }
        }

        StringBuffer sbuf = new StringBuffer("");
        sbuf.append("<table width=\"670\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" class=\"table\">");
        sbuf.append("<tr><td class=tr_yellow>排名</td><td class=tr_yellow>目的IP</td><td class=tr_yellow>次数</td><td class=tr_yellow>占总量百分比</td></tr>");
        boolean singleLine = true;
        for (int i = 0; i < 10; i++) {
            //if (i % 2 == 0) {
            sbuf.append("<tr>");
            //}
            String times = "";
            String targetip = "";
            String rates = "";
            if ((list != null) && (i < listSize)) {
                Map mp = (Map) list.get(i);
                java.math.BigDecimal bdTimes = (java.math.BigDecimal) mp.get("times");
                long lTimes = bdTimes.longValue();
                times = "" + lTimes;
                targetip = (String) mp.get("targetip");
                if (totalTimes != 0) {
                    rates = "" + (Math.floor(lTimes * 10000.0 / totalTimes) / 100.0) + "%";
                }
            }
            String className = singleLine ? "tr_yellow" : "tr_glory";
            className =   "tr_yellow";
            sbuf.append("<td width=20% class=").append(className).append(">Top-").append(i + 1);
            sbuf.append("</td><td width=30% class=tr_white>");
            sbuf.append(targetip);
            sbuf.append("</td><td width=20% class=tr_white>");
            sbuf.append(times);
            sbuf.append("</td><td width=30% class=tr_white>");
            sbuf.append(rates);
            sbuf.append("</td>");
            //if (i % 2 == 1) {
            sbuf.append("</tr>");
            singleLine = !singleLine;
            //}
        }
        sbuf.append("</table>");

        return sbuf.toString();
    }

}
