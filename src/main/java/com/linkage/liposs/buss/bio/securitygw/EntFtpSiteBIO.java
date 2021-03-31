package com.linkage.liposs.buss.bio.securitygw;

import java.awt.Font;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.liposs.buss.dao.securitygw.EntFtpSiteDAO;
import com.linkage.liposs.buss.util.ChartUtil;
import com.linkage.liposs.buss.util.ChartYType;
import com.linkage.liposs.buss.util.DateUtils;
import com.linkage.liposs.buss.util.FriendlyException;

/**
 * @author Owner(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-2
 * @category com.linkage.liposs.buss.bio.securitygw 版权：南京联创科技 网管科技部
 *
 */
public class EntFtpSiteBIO {
	private static Logger log = LoggerFactory.getLogger(EntFtpSiteBIO.class);
    public ChartUtil cu; // jfreechart的封装类
    private Font font = new Font("宋体", Font.PLAIN, 12); // 字体
    public EntFtpSiteDAO entFtpSiteDAO;

    public void setCu(ChartUtil cu) {
        this.cu = cu;
    }

    public void setEntFtpSiteDAO(EntFtpSiteDAO entFtpSiteDAO) {
        this.entFtpSiteDAO = entFtpSiteDAO;
    }

    public JFreeChart entFtpTopnToday(String deviceId, long enddate) {
        try {
            long yesterday = DateUtils.getDayBegin(enddate);
            long endday = DateUtils.getDayEnd(enddate);
            List<Map> ftpTopn = entFtpSiteDAO.getEntFtpTopnTodayByHour(deviceId, yesterday, endday);
            String datetime = DateUtils.getYearMonthDayString(enddate, "-", true);
            cu.setCLablePosition(ChartYType.DOWN_45);
            JFreeChart jfc = cu.createCategoryBar3DP("企业FTP统计日报表(" + datetime + ")",
                             "目的IP", "次数", ftpTopn, "times",
                             "targetip", "colName", false);

            return jfc;
        } catch (Exception ex) {
            FriendlyException.printFriendlyLocateInfo(ex);
            return null;
        }
    }

    public JFreeChart entFtpTopnWeek(String deviceId, long enddate) {
        try {
            long lastday = DateUtils.getWeekBegin(enddate);
            long endday = DateUtils.getWeekEnd(enddate);
            String strStart = DateUtils.getYearMonthDayString(lastday, "-", true);
            String strEnd = DateUtils.getYearMonthDayString(endday, "-", true);

            List<Map> ftpTopn = entFtpSiteDAO.getEntFtpTopnWeekByDay(deviceId, lastday, endday);

            cu.setCLablePosition(ChartYType.DOWN_45);
            JFreeChart jfc = cu.createCategoryBar3DP("企业FTP统计周报表(" + strStart + "~" + strEnd + ")",
                             "目的IP", "次数", ftpTopn, "times", "targetip",
                             "colName", false);
            return jfc;
        } catch (Exception ex) {
            FriendlyException.printFriendlyLocateInfo(ex);
            return null;
        }
    }

    public JFreeChart entFtpTopnMonth(String deviceId, long enddate) {
        try {
            long lastday = DateUtils.getMonthBegin(enddate);
            long endday = DateUtils.getMonthEnd(enddate);
            String strStart = DateUtils.getYearMonthDayString(lastday, "-", true);
            String strEnd = DateUtils.getYearMonthDayString(endday, "-", true);
            List<Map> ftpTopn = entFtpSiteDAO.getEntFtpTopnMonthByDay(deviceId, lastday, endday);

            cu.setCLablePosition(ChartYType.DOWN_45);
            JFreeChart jfc = cu.createCategoryBar3DP("企业FTP统计月报表(" + strStart + "~" + strEnd + ")",
                             "目的IP", "次数", ftpTopn, "times", "targetip",
                             "colName", false);
            return jfc;
        } catch (Exception ex) {
            FriendlyException.printFriendlyLocateInfo(ex);
            return null;
        }
    }

    public JFreeChart entFtpTopnPopup(String deviceId, long stime, long etime) {
        JFreeChart jfc = null;
        try {
            String strStart = DateUtils.getYearMonthDayString(stime, "-", true);
            String strEnd = DateUtils.getYearMonthDayString(etime, "-", true);
            cu.setCLablePosition(ChartYType.DOWN_45);

            if (etime - stime <= 3600 * 24) {
                List<Map> ftpTopn = entFtpSiteDAO.getEntFtpTopnMonthByDay(deviceId, stime, etime);
                jfc = cu.createCategoryBar3DP("企业FTP统计报表",
                      "目的IP", "次数", ftpTopn, "times", "targetip",
                      "colName", false);
            } else {
                List<Map> ftpTopn = entFtpSiteDAO.getEntFtpTopnMonthByDay(deviceId, stime, etime);
                jfc = cu.createCategoryBar3DP("企业FTP统计报表",
                      "目的IP", "次数", ftpTopn, "times", "targetip",
                      "colName", false);
            }
        } catch (Exception ex) {
            FriendlyException.printFriendlyLocateInfo(ex);
        }
        return jfc;
    }


    public EntFtpSiteDAO getEntFtpSiteDAO() {
        return entFtpSiteDAO;
    }
}
