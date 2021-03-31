package com.linkage.liposs.buss.bio.securitygw;

/**
 * @author Owner(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-1
 * @category com.linkage.liposs.buss.bio.securitygw 版权：南京联创科技 网管科技部
 *
 */
import java.awt.Font;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.liposs.buss.dao.securitygw.EntWebTopnDAO;
import com.linkage.liposs.buss.util.ChartUtil;
import com.linkage.liposs.buss.util.ChartYType;
import com.linkage.liposs.buss.util.DateUtils;
import com.linkage.liposs.buss.util.FriendlyException;

public class EntWebTopnBIO {
	private static Logger log = LoggerFactory.getLogger(EntWebTopnBIO.class);
    public ChartUtil cu; // jfreechart的封装类
    private Font font = new Font("宋体", Font.PLAIN, 12); // 字体
    public EntWebTopnDAO entWebDAO;
    public void setCu(ChartUtil cu) {
        this.cu = cu;
    }

    public void setEntWebDAO(EntWebTopnDAO entWebDAO) {
        this.entWebDAO = entWebDAO;
    }

    public JFreeChart entSamllWebTopnToday(String deviceId, long enddate) {
        try {
            long yesterday = DateUtils.getDayBegin(enddate);
            long endday = DateUtils.getDayEnd(enddate);
            String datestr = DateUtils.getYearMonthDayString(endday, "-", true);
            List<Map> webTopn = entWebDAO.getEntWebTopnTodayByHour(deviceId, yesterday, endday);

            cu.setTickLabelsVisible(false);
            JFreeChart jfc = cu.createCategoryBar3DP("企业Web浏览",
                             "目的IP", "次数", webTopn, "times",
                             "targetip", "colName", false);
            return jfc;
        } catch (Exception ex) {
            FriendlyException.printFriendlyLocateInfo(ex);
            return null;
        }
    }

    public JFreeChart entWebTopnToday(String deviceId, long enddate) {
        try {
            long yesterday = DateUtils.getDayBegin(enddate);
            long endday = DateUtils.getDayEnd(enddate);
            String datestr = DateUtils.getYearMonthDayString(endday, "-", true);
            List<Map> webTopn = entWebDAO.getEntWebTopnTodayByHour(deviceId, yesterday, endday);
            cu.setCLablePosition(ChartYType.DOWN_45);
            JFreeChart jfc = cu.createCategoryBar3DP("企业上网统计日报表(" + datestr + ")",
                             "目的IP", "次数", webTopn, "times",
                             "targetip", "colName", false);
            return jfc;
        } catch (Exception ex) {
            FriendlyException.printFriendlyLocateInfo(ex);
            return null;
        }
    }

    public JFreeChart entWebTopnWeek(String deviceId, long enddate) {
        try {
            long lastday = DateUtils.getWeekBegin(enddate);
            long endday = DateUtils.getWeekEnd(enddate);
            String strStart = DateUtils.getYearMonthDayString(lastday, "-", true);
            String strEnd = DateUtils.getYearMonthDayString(endday, "-", true);
            List<Map> webTopn = entWebDAO.getEntWebTopnWeekByDay(deviceId, lastday, endday);
            cu.setCLablePosition(ChartYType.DOWN_45);
            JFreeChart jfc = cu.createCategoryBar3DP("企业上网统计周报表(" + strStart + "~" + strEnd + ")",
                             "目的IP", "次数", webTopn, "times", "targetip", "colName", false);
            return jfc;
        } catch (Exception ex) {
            FriendlyException.printFriendlyLocateInfo(ex);
            return null;
        }
    }

    public JFreeChart entWebTopnMonth(String deviceId, long enddate) {
        try {
            long lastday = DateUtils.getMonthBegin(enddate);
            long endday = DateUtils.getMonthEnd(enddate);
            String strStart = DateUtils.getYearMonthDayString(lastday, "-", true);
            String strEnd = DateUtils.getYearMonthDayString(endday, "-", true);

            List<Map> webTopn = entWebDAO.getEntWebTopnMonthByDay(deviceId, lastday, endday);

            cu.setCLablePosition(ChartYType.DOWN_45);
            JFreeChart jfc = cu.createCategoryBar3DP("企业上网统计月报表(" + strStart + "~" + strEnd + ")",
                             "目的IP", "次数", webTopn, "times", "targetip",
                             "colName", false);
            return jfc;
        } catch (Exception ex) {
            FriendlyException.printFriendlyLocateInfo(ex);
            return null;
        }
    }

    public JFreeChart entWebTopnPopup(String deviceId, long stime, long etime) {
        JFreeChart jfc = null;
        try {
            if (etime - stime <= 3600 * 24) {
                String datestr = DateUtils.getYearMonthDayString(etime, "-", true);
                List<Map> webTopn = entWebDAO.getEntWebTopnTodayByHour(deviceId, stime, etime);
                cu.setCLablePosition(ChartYType.DOWN_45);
                jfc = cu.createCategoryBar3DP("企业上网统计报表",
                      "目的IP", "次数", webTopn, "times",
                      "targetip", "colName", false);
            } else {
                List<Map> webTopn = entWebDAO.getEntWebTopnMonthByDay(deviceId, stime, etime);
                cu.setCLablePosition(ChartYType.DOWN_45);
                jfc = cu.createCategoryBar3DP("企业上网统计报表", "目的IP", "次数",
                      webTopn, "times", "targetip", "colName", false);
            }
        } catch (Exception ex) {
            FriendlyException.printFriendlyLocateInfo(ex);
        }
        return jfc;

    }
}
