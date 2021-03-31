package com.linkage.liposs.action.securitygw;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateTickUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.liposs.buss.dao.securitygw.BehaviorAnalyseDAO;
import com.linkage.liposs.buss.util.ChartUtil;
import com.linkage.liposs.buss.util.DateUtils;
import com.linkage.liposs.buss.util.FriendlyException;
import com.opensymphony.xwork2.ActionSupport;

public class NetFluxAnalyseAction extends ActionSupport {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(NetFluxAnalyseAction.class);
    Long today = 0L, before7 = 0L, before30 = 0L;
    String dayUrl = "/", weekUrl = "", monthUrl = "", popupUrl = "";
    private Map deviceInfoMap; // 设备信息
    private String deviceid; // 设备ID
    private String remark; // 描述
    List list = null;
    private String ajax;
    Long time = 0L, stime = 0L;
    String datetime, starttime;
    String weekBegin, monthBegin;
    String deviceIp, deviceModel, deviceName;
    String totalFluxToday, totalFluxWeek, totalFluxMonth, totalFluxPeriod;
    String parstr;

    private JFreeChart chart = null;
    private ChartUtil chartUtil = null;
    BehaviorAnalyseDAO baDao = null;

    public String getAjax() {
        return ajax;
    }

    public void setAjax(String ajax) {
        this.ajax = ajax;
    }

    public String toNetFluxAnalyse() throws Exception {
        if (time == 0) {
            time = (new Date()).getTime() / 1000;
        }
        parstr = "time=" + time + "&deviceid=" + deviceid;
        dayUrl = "/securitygw/NetFluxAnalyse!netFluxAnalysToday.action?" + parstr;
        weekUrl = "/securitygw/NetFluxAnalyse!netFluxAnalysWeek.action?" + parstr;
        monthUrl = "/securitygw/NetFluxAnalyse!netFluxAnalysMonth.action?" + parstr;

        deviceInfoMap = baDao.getDeviceInfo(deviceid);
        deviceIp = (String) deviceInfoMap.get("loopback_ip");
        deviceModel = (String) deviceInfoMap.get("device_model");
        deviceName = (String) deviceInfoMap.get("device_name");

        logger.debug("toNetFluxAnalyse:time=" + time);
        return SUCCESS;
    }

    public String popup() throws Exception {
        if (time == 0) {
            time = (new Date()).getTime() / 1000;
        }

        if (stime == 0) {
            stime = time - 3600 * 24 * 7;
            starttime = DateUtils.getYearMonthDayString(stime, "-", true);
        }

        String paramstr = "stime=" + stime + "&time=" + time + "&deviceid=" + deviceid;
        popupUrl = "/securitygw/NetFluxAnalyse!netFluxAnalysPopup.action?" + paramstr;

        deviceInfoMap = baDao.getDeviceInfo(deviceid);
        deviceIp = (String) deviceInfoMap.get("loopback_ip");
        deviceModel = (String) deviceInfoMap.get("device_model");
        deviceName = (String) deviceInfoMap.get("device_name");

        logger.debug("toNetFluxAnalyse:time=" + time);
        return "popup";
    }

    public String getSmallFluxToday() {
        try {
            if (time == 0) {
                time = (new Date()).getTime() / 1000;
            }
            long yesterday = DateUtils.getDayBegin(time);
            long dayend = DateUtils.getDayEnd(time);

//            String[] fluxTypes = {"http", "ftp", "smtp", "pop3"};
//            List<Map> [] netFluxes = new List[4];
//            netFluxes[0] = baDao.getNetFluxAnalysToday(deviceid, yesterday, time, 0);
//            netFluxes[1] = baDao.getNetFluxAnalysToday(deviceid, yesterday, time, 1);
//            netFluxes[2] = baDao.getNetFluxAnalysToday(deviceid, yesterday, time, 2);
//            netFluxes[3] = baDao.getNetFluxAnalysToday(deviceid, yesterday, time, 3);
            List<Map> netFlux = baDao.getNetFluxAnalysToday(deviceid, yesterday, dayend, Integer.MAX_VALUE);
            logger.debug("[getSmallFluxToday]dao return " + netFlux.size() + " rows ");

            chartUtil.setXAxisUnit(DateTickUnit.HOUR, 4, "H");
            chartUtil.setXDate(new Date(yesterday * 1000), new Date(dayend * 1000));
//            this.chart = chartUtil.createXYCategoryBarP("网络流量分析", "时间", "流量(KB)",
//                         fluxTypes, netFluxes, "stime", "flux", 2, false);

            this.chart = chartUtil.createXYStackedBarP("网络流量分析", "时间", "流量(KB)", netFlux, "fluxsum", "stime",
                         "protocolName", 2, false);

            return "getSmallFluxToday";
        } catch (Exception ex) {
            FriendlyException.printFriendlyLocateInfo(ex);
            return null;
        }
    }

    public String getTotalFluxToday() {
        if (time == 0) {
            time = (new Date()).getTime() / 1000;
        }
        //long yesterday = time - 3600 * 24;
        long yesterday = DateUtils.getDayBegin(time);
        long dayend = DateUtils.getDayEnd(time);
        totalFluxToday = getTotalFlux(yesterday, dayend, 1);
        return totalFluxToday;
    }

    public String netFluxAnalysToday() {
        try {
            if (time == 0) {
                time = (new Date()).getTime() / 1000;
            }
            long yesterday = DateUtils.getDayBegin(time);
            long dayend = DateUtils.getDayEnd(time);

            List<Map> netFlux = baDao.getNetFluxAnalysToday(deviceid, yesterday, dayend, Integer.MAX_VALUE);
            logger.debug("[netFluxAnalysToday]dao return " + netFlux.size() + " rows ");

            chartUtil.setXAxisUnit(DateTickUnit.HOUR, 2, "H:00");
            chartUtil.setXDate(new Date(yesterday * 1000), new Date(dayend * 1000));
            this.chart = chartUtil.createXYStackedBarP("网络流量统计日报表(" + getDatetime() + ")", "时间", "流量(KB)", netFlux,
                         "fluxsum", "stime", "protocolName", 2, true);

            return "netFluxAnalysToday";
        } catch (Exception ex) {
            FriendlyException.printFriendlyLocateInfo(ex);
            return null;
        }
    }

    public String getTotalFluxWeek() {
        if (time == 0) {
            time = (new Date()).getTime() / 1000;
        }
        long endday = DateUtils.getWeekEnd(DateUtils.getDayEnd(time));
        long lastday = DateUtils.getWeekBegin(DateUtils.getDayBegin(time));

        totalFluxWeek = getTotalFlux(lastday, endday, 2);
        return totalFluxWeek;
    }

    public String netFluxAnalysWeek() {
        try {
            if (time == 0) {
                time = (new Date()).getTime() / 1000;
            }
            long endday = DateUtils.getWeekEnd(DateUtils.getDayEnd(time));
            long lastday = DateUtils.getWeekBegin(DateUtils.getDayBegin(time));
            weekBegin = DateUtils.getYearMonthDayString(lastday, "-", true);
            String weekEnd = DateUtils.getYearMonthDayString(endday, "-", true);

            List<Map> netFlux = baDao.getNetFluxAnalysWeek(deviceid, lastday,
                                endday, Integer.MAX_VALUE);
            logger.debug("[netFluxAnalysWeek]dao return " + netFlux.size() + " rows ");

            chartUtil.setXAxisUnit(DateTickUnit.DAY, 1, "E");
            chartUtil.setXDate(new Date(lastday * 1000), new Date(endday * 1000));
            this.chart = chartUtil.createXYStackedBarP("网络流量统计周报表(" + weekBegin + "~" + weekEnd + ")", "日期",
                         "流量(KB)", netFlux, "fluxsum", "stime", "protocolName", 3, true);

            return "netFluxAnalysWeek";
        } catch (Exception ex) {
            FriendlyException.printFriendlyLocateInfo(ex);
            return null;
        }
    }

    public String getTotalFluxMonth() {
        if (time == 0) {
            time = (new Date()).getTime() / 1000;
        }
        long endday = DateUtils.getMonthEnd(DateUtils.getDayEnd(time));
        long lastday = DateUtils.getMonthBegin(DateUtils.getDayBegin(time));

        totalFluxMonth = getTotalFlux(lastday, endday, 3);
        return totalFluxMonth;
    }

    public String netFluxAnalysMonth() {
        try {
            if (time == 0) {
                time = (new Date()).getTime() / 1000;
            }
            long endday = DateUtils.getMonthEnd(DateUtils.getDayEnd(time));
            long lastday = DateUtils.getMonthBegin(DateUtils.getDayBegin(time));
            monthBegin = DateUtils.getYearMonthDayString(lastday, "-", true);
            String weekEnd = DateUtils.getYearMonthDayString(endday, "-", true);

            List<Map> netFlux = baDao.getNetFluxAnalysMonth(deviceid, lastday, endday, Integer.MAX_VALUE);
            logger.debug("[netFluxAnalysMonth]dao return " + netFlux.size() + " rows ");

            chartUtil.setXAxisUnit(DateTickUnit.DAY, 2, "dd");
            chartUtil.setXDate(new Date(lastday * 1000), new Date(endday * 1000));
            this.chart = chartUtil.createXYStackedBarP("网络流量统计月报表(" + monthBegin + "~" + weekEnd + ")", "日期",
                         "流量(KB)", netFlux, "fluxsum", "stime", "protocolName", 3, true);
            return "netFluxAnalysMonth";
        } catch (Exception ex) {
            FriendlyException.printFriendlyLocateInfo(ex);
            return null;
        }
    }

    public String netFluxAnalysPopup() {
        try {
            List<Map> netFlux = baDao.getNetFluxAnalysPopup(deviceid, stime, time, Integer.MAX_VALUE);
            logger.debug("[netFluxAnalysPopup]dao return " + netFlux.size() + " rows ");

            if (time - stime <= 3600 * 24) {
                chartUtil.setXAxisUnit(DateTickUnit.HOUR, 2, "H:00");
                chartUtil.setXDate(new Date(stime * 1000), new Date(time * 1000));
                this.chart = chartUtil.createXYStackedBarP("网络流量统计报表", "时间", "流量(KB)", netFlux,
                             "fluxsum", "stime", "protocolName", 2, true);
            } else {
                chartUtil.setXAxisUnit(DateTickUnit.DAY, 1, "dd");
                chartUtil.setXDate(new Date(stime * 1000), new Date(time * 1000));
                this.chart = chartUtil.createXYStackedBarP("网络流量统计报表", "日期",
                             "流量(KB)", netFlux, "fluxsum", "stime", "protocolName", 3, true);
            }
            return "netFluxAnalysPopup";
        } catch (Exception ex) {
            FriendlyException.printFriendlyLocateInfo(ex);
            return null;
        }
    }

    public String getDayUrl() {
        return dayUrl;
    }

    public void setDayUrl(String dayUrl) {
        this.dayUrl = dayUrl;
    }

    public String getWeekUrl() {
        return weekUrl;
    }

    public void setWeekUrl(String weekUrl) {
        this.weekUrl = weekUrl;
    }

    public String getMonthUrl() {
        return monthUrl;
    }

    public void setMonthUrl(String monthUrl) {
        this.monthUrl = monthUrl;
    }

    public Long getToday() {
        return today;
    }

    public void setToday(Long today) {
        this.today = today;
    }

    public Long getBefore7() {
        return before7;
    }

    public void setBefore7(Long before7) {
        this.before7 = before7;
    }

    public Long getBefore30() {
        return before30;
    }

    public void setBefore30(Long before30) {
        this.before30 = before30;
    }

    public BehaviorAnalyseDAO getBaDao() {
        return baDao;
    }

    public void setBaDao(BehaviorAnalyseDAO baDao) {
        this.baDao = baDao;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public Long getTime() {
        return time;
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

    public JFreeChart getChart() {
        return chart;
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

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public String getDeviceIp() {
        return deviceIp;
    }


    public String getTotalFluxPeriod() {
        totalFluxPeriod = getTotalFlux(stime, time, 4);
        return totalFluxPeriod;
    }


    public String getRemark() {
        return remark;
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

    public Long getStime() {
        return stime;
    }

    public String getPopupUrl() {
        return popupUrl;
    }


    private int[] getSumFluxForProtocols(String deviceId, long startTime, long endTime) {
        int[] fluxes = new int[6];
        fluxes[0] = baDao.getSumFluxByProtocol(deviceid, startTime, endTime, 0); //http
        fluxes[1] = baDao.getSumFluxByProtocol(deviceid, startTime, endTime, 1); //ftp
        fluxes[2] = baDao.getSumFluxByProtocol(deviceid, startTime, endTime, 2); //smtp
        fluxes[3] = baDao.getSumFluxByProtocol(deviceid, startTime, endTime, 3); //pop3
        fluxes[5] = baDao.getSumFluxByProtocol(deviceid, startTime, endTime, Integer.MAX_VALUE); //total
        fluxes[4] = fluxes[5] - fluxes[0] - fluxes[1] - fluxes[2] - fluxes[3]; //other
        return fluxes;
    }


    private String getTotalFlux(long startTime, long endTime, int reqType) {
        String[] protocolTypes = {"http", "ftp", "smtp", "pop3"};

        int[] protocolFluxes = getSumFluxForProtocols(deviceid, startTime, endTime);
        int total = protocolFluxes[5];
        int otherflux = protocolFluxes[4];
        int fluxnum = protocolFluxes.length - 2;
        StringBuffer sbuf = new StringBuffer("");
        sbuf.append("<table width=\"670\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" class=\"table\">");
//        sbuf.append("<caption>流量分析报表(单位:KB)</caption>");
        sbuf.append("<tr align=left><td class=tr_yellow>时间</td>");
        for (int i = 0; i < protocolTypes.length; i++) {
            sbuf.append("<td align=left class=tr_yellow>").append(protocolTypes[i]).append("(KB)</td>");
        }
        sbuf.append("<td class=tr_yellow>其它(KB)</td></tr>");

        //列出每个时段内的统计数据:
        //if (reqType == 4) {
        int periodInterval = 3600;
        String tailStr = ":00";
        boolean hourMode = true;
        if (endTime - startTime > 24 * 3600) {
            periodInterval = 24 * 3600;
            tailStr = "";
            hourMode = false;
        }
        long periodBegin = startTime;
        long endj = (endTime - startTime) / periodInterval;
        for (int j = 0; j <= endj; j++) {
            long pstime = j * periodInterval + periodBegin;
            long petime = (j + 1) * periodInterval + periodBegin - 1;
            int[] periodFlux = getSumFluxForProtocols(deviceid, pstime, petime);
            int periodTotal = periodFlux[5];
            int periodOtherFlux = periodFlux[4];
            sbuf.append("<tr><td class=tr_yellow align=right>");
            if (hourMode) {
                sbuf.append(j).append(tailStr).append("~");
                sbuf.append(j + 1).append(tailStr).append("</td>");
            } else {
                sbuf.append(DateUtils.getYearMonthDayString(pstime, "-", true)).append("</td>");
            }
            for (int i = 0; i < protocolTypes.length; i++) {
                sbuf.append("<td align=left class=tr_white>");
                sbuf.append((i < periodFlux.length) ? periodFlux[i] : "&nbsp;");
                sbuf.append("</td>");
            }
            sbuf.append("<td align=left class=tr_white>").append(periodOtherFlux).append("</td></tr>");
        }
        //}

        //统计信息:
        sbuf.append("<tr ><td class=tr_yellow align=left>小计(KB)</td>");
        for (int i = 0; i < protocolTypes.length; i++) {
            sbuf.append("<td align=left class=tr_white>");
            sbuf.append((i < fluxnum) ? protocolFluxes[i] : "&nbsp;");
            sbuf.append("</td>");
        }
        sbuf.append("<td align=left class=tr_white>").append(otherflux).append("</td>");
        sbuf.append("</tr>");

        sbuf.append("<tr align=left><td class=tr_yellow align=left>比例</td>");
        for (int i = 0; i < protocolTypes.length; i++) {
            sbuf.append("<td align=left class=tr_white>");
            if (i < fluxnum) {
                sbuf.append(Math.floor(protocolFluxes[i] * 100.00 / total)).append("%");
            } else {
                sbuf.append("0%");
            }
            sbuf.append("</td>");
        }
        sbuf.append("<td align=left class=tr_white>").append(Math.floor(otherflux * 100.00 / total)).append("%</td>");
        sbuf.append("</tr>");

        sbuf.append("<tr><td class=tr_yellow align=left>总计(KB)</td>");
        sbuf.append("<td colspan=").append(fluxnum * 2 + 3).append(" class=tr_white>");
        sbuf.append(total).append("</td></tr>");

        sbuf.append("</table>");
        return sbuf.toString();
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setDevice_id(String deviceid) {
        this.deviceid = deviceid;
    }

    public void setDeviceInfoMap(Map deviceInfoMap) {
        this.deviceInfoMap = deviceInfoMap;
    }

    public void setChartUtil(ChartUtil chartUtil) {
        this.chartUtil = chartUtil;
    }

    public void setChart(JFreeChart chart) {
        this.chart = chart;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
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

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public void setStime(Long stime) {
        this.stime = stime;
    }

}
