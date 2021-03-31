package com.linkage.module.itms.report.act;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.bio.BridgeAndRouteRecordBio;
import action.splitpage.splitPageAction;
/**
 * @author songxq
 * @version 1.0
 * @date 2021/1/14 14:40
 */
public class BridgeAndRouteRecordAct extends splitPageAction implements SessionAware
{
    private static Logger logger = LoggerFactory.getLogger(BridgeAndRouteRecordAct.class);

    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private BridgeAndRouteRecordBio bio;

    private transient Map session;

    /** 开始时间 */
    private String startOpenDate = "";
    /** 开始时间 */
    private String startOpenDate1 = "";
    /** 结束时间 */
    private String endOpenDate = "";
    /** 结束时间 */
    private String endOpenDate1 = "";

    private transient List<Map> dataList;

    private String userNameType = "";

    private String userName = "";

    private int total;
    /**
     * 需要导出的结果集，必须是处理过的可以直接显示的结果
     */
    private transient List<Map> data;
    /**
     * 需要导出的列名，对应data中的键值
     */
    private String[] column;
    /**
     * 显示在导出文档上的列名
     */
    private String[] title;
    /**
     * 导出的文件名（不包括后缀名）
     */
    private String fileName;
    private String excel;

    public String init()
    {
        startOpenDate = getStartDate();
        endOpenDate = getEndDate();
        return "init";
    }

    public String getRecord()
    {
        startOpenDate1 = dealTime(startOpenDate);
        endOpenDate1 = dealTime(endOpenDate);
        dataList = bio.getRocord(userNameType,userName,startOpenDate1,endOpenDate1,curPage_splitPage, num_splitPage);
        maxPage_splitPage = bio.getMaxPageSplitPage();
        this.total = bio.getTotal();
        return "list";
    }
    /**
     * 当前年的1月1号
     */
    private String getStartDate()
    {
        GregorianCalendar now = new GregorianCalendar();
        SimpleDateFormat fmtrq = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS, Locale.US);
        now.set(Calendar.DATE, 1);
        now.set(Calendar.MONTH, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        String time = fmtrq.format(now.getTime());
        return time;
    }
    /**
     * 当前时间的23:59:59,如 2011-05-11 23:59:59
     */
    private String getEndDate()
    {
        GregorianCalendar now = new GregorianCalendar();
        SimpleDateFormat fmtrq = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS, Locale.US);
        now.set(Calendar.HOUR_OF_DAY, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);
        return fmtrq.format(now.getTime());
    }

    public String toExcel()
    {
        startOpenDate1 = dealTime(startOpenDate);
        endOpenDate1 = dealTime(endOpenDate);
        data = bio.queryForExcel(userNameType,userName,startOpenDate1,endOpenDate1);
        String excelCol = "addtime#operaction#operresult#loid#username#oper_origon#oper_staff";
        String excelTitle = "操作时间#操作动作#操作结果#loid#宽带账号#操作来源#操作人员";
        column = excelCol.split("#");
        title = excelTitle.split("#");
        fileName = "桥接路由修改结果表";
        return "excel";
    }

    public String dealTime(String time) {
        if(StringUtil.IsEmpty(time))
        {
            return "";
        }
        SimpleDateFormat date = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        Date str = new Date();
        try {
            str = date.parse(time);
        }
        catch (ParseException e) {
            logger.warn("选择开始或者结束的时间格式不对:[{}]" , time);
        }

        return StringUtil.getStringValue(str.getTime() / 1000L);
    }


    public BridgeAndRouteRecordBio getBio() {
        return bio;
    }

    public void setBio(BridgeAndRouteRecordBio bio) {
        this.bio = bio;
    }

    public String getStartOpenDate() {
        return startOpenDate;
    }

    public void setStartOpenDate(String startOpenDate) {
        this.startOpenDate = startOpenDate;
    }

    public String getStartOpenDate1() {
        return startOpenDate1;
    }

    public void setStartOpenDate1(String startOpenDate1) {
        this.startOpenDate1 = startOpenDate1;
    }

    public String getEndOpenDate() {
        return endOpenDate;
    }

    public void setEndOpenDate(String endOpenDate) {
        this.endOpenDate = endOpenDate;
    }

    public String getEndOpenDate1() {
        return endOpenDate1;
    }

    public void setEndOpenDate1(String endOpenDate1) {
        this.endOpenDate1 = endOpenDate1;
    }

    public List<Map> getDataList() {
        return dataList;
    }

    public void setDataList(List<Map> dataList) {
        this.dataList = dataList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNameType() {
        return userNameType;
    }

    public void setUserNameType(String userNameType) {
        this.userNameType = userNameType;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Map> getData() {
        return data;
    }

    public void setData(List<Map> data) {
        this.data = data;
    }

    public String[] getColumn() {
        return column;
    }

    public void setColumn(String[] column) {
        this.column = column;
    }

    public String[] getTitle() {
        return title;
    }

    public void setTitle(String[] title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExcel() {
        return excel;
    }

    public void setExcel(String excel) {
        this.excel = excel;
    }

    public Map getSession()
    {
        return session;
    }

    @Override
    public void setSession(Map<String, Object> session)
    {
        this.session =session;
    }
}
