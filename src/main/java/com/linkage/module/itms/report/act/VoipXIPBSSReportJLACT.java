package com.linkage.module.itms.report.act;

import action.splitpage.splitPageAction;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.itms.report.bio.VoipXIPBSSReportJLBIO;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 吉林联通--修改语音工单统计报表
 * created by lingmin on 2020/5/6
 */
public class VoipXIPBSSReportJLACT extends splitPageAction implements SessionAware {
    private static Logger LOGGER = LoggerFactory.getLogger(VoipXIPBSSReportJLACT.class);
    private Map session;
    private VoipXIPBSSReportJLBIO reportJLBIO;
    private String startTime;
    private String endTime;
    private String cityId;
    private String loid;
    private List<Map<String, String>> resultReportList;
    private List<Map<String, String>> resultReportInfoList;
    private String startTimeSec = "";
    private String endTimeSec = "";
    //工单接收/下发状态
    private String type;
    /** 导出数据 */
    private List<Map<String, String>> data;
    /** 导出文件列标题 */
    private String[] title;
    /** 导出文件列 */
    private String[] column;
    /** 导出文件名 */
    private String fileName;
    //点击统计列表的对应数目
    private String totalNum;
    private Map<String,String> loidBSSReportMap;

    /**
     * 获取工单接收统计报表
     * @return
     */
    public String getVoipBSSStatistic() {
        LOGGER.warn("begin getVoipBSSStatistic...");
        //时间转换为时间戳
        getTimeSec();
        resultReportList = reportJLBIO.getVoipXIPBSSReport(cityId, startTimeSec, endTimeSec);
        LOGGER.warn("end getVoipBSSStatistic...");
        return "voipBSSReport";
    }

    /**
     * 获取工单下发统计报表
     * @return
     */
    public String getVoipBSSDownStatistic() {
        LOGGER.warn("begin getVoipBSSDownStatistic...");
        //时间转换为时间戳
        getTimeSec();
        resultReportList = reportJLBIO.getVoipBSSDownReport(cityId, startTimeSec, endTimeSec);
        LOGGER.warn("end getVoipBSSDownStatistic...");
        return "voipBSSDownReport";
    }

    /**
     * 获取工单接收统计数目详情列表
     * @return
     */
    public String getVoipBSSReportInfo(){
        LOGGER.warn("begin getVoipBSSReportInfo,totalNum:{},cityId:{},startTimeSec:{},endTimeSec:{},type:{}",totalNum,cityId,startTimeSec,endTimeSec,type);
        //分页总数直接从统计列表页点击时传过来
        totalRowCount_splitPage = Integer.parseInt(totalNum);
        if(totalRowCount_splitPage == 0){
            resultReportInfoList = new ArrayList<Map<String, String>>();
            return "voipBSSReportInfo";
        }
        maxPage_splitPage = getMaxPage_splitPage();
        resultReportInfoList = reportJLBIO.getVoipBSSReportInfo(cityId,startTimeSec,endTimeSec,type,curPage_splitPage,num_splitPage);
        LOGGER.warn("end getVoipBSSReportInfo...");
        return "voipBSSReportInfo";
    }

    /**
     * 获取工单下发统计数目详情列表
     * @return
     */
    public String getVoipBSSDownInfo(){
        LOGGER.warn("begin getVoipBSSDownInfo...");
        //分页总数直接从统计列表页点击时传过来
        totalRowCount_splitPage = Integer.parseInt(totalNum);
        if(totalRowCount_splitPage == 0){
            resultReportInfoList = new ArrayList<Map<String, String>>();
            return "voipBSSDownReportInfo";
        }
        maxPage_splitPage = getMaxPage_splitPage();
        resultReportInfoList = reportJLBIO.getVoipBSSReportDownInfo(cityId,startTimeSec,endTimeSec,type,curPage_splitPage,num_splitPage);
        LOGGER.warn("end getVoipBSSDownInfo...");
        return "voipBSSDownReportInfo";
    }

    /**
     * 工单接收统计详情列表导出
     * @return
     */
    public String getVoipBSSInfoExcel() {
        LOGGER.warn("getVoipBSSInfoExcel({},{},{})", cityId, startTimeSec, endTimeSec);
        if(type.equals("0") || type.equals("2") || type.equals("-1")){
            //接收成功数详情列表
            title = new String[]{"地市编码","地市名称","LOID","语音IP"};
            column = new String[]{"cityId", "cityName","loid","ip"};
            fileName = "语音修改IP工单接收成功数统计详情";
        }else {
            //接收失败数详情列表
            title = new String[]{"LOID","语音IP","失败原因"};
            column = new String[]{"loid", "ip","failReason"};
            fileName = "语音修改IP工单接收失败数统计详情";
        }
        data = reportJLBIO.getVoipBSSReportInfo(cityId, startTimeSec, endTimeSec, type,-1,-1);
        return "excel";
    }

    /**
     * 工单下发统计详情列表导出
     * @return
     */
    public String getVoipBSSDownInfoExcel() {
        LOGGER.warn("getVoipBSSDownInfoExcel({},{},{})", cityId, startTimeSec, endTimeSec);
        if(type.equals("1")){
            //下发成功数详情列表
            title = new String[]{"地市编码","地市名称","LOID","语音IP"};
            column = new String[]{"cityId", "cityName","loid","ip"};
            fileName = "语音修改IP工单接收成功数统计详情";
        }else {
            //接收失败数详情列表
            title = new String[]{"地市编码","地市名称","LOID","语音IP","是否完成修改工单"};
            column = new String[]{"cityId", "cityName","loid","ip","isParamModify"};
            fileName = "语音修改IP工单接收失败数统计详情";
        }
        data = reportJLBIO.getVoipBSSReportDownInfo(cityId, startTimeSec, endTimeSec, type,-1,-1);
        return "excel";
    }

    /**
     * 根据loid获取工单接收和下发情况
     * @return
     */
    public String getVoipBSSByLoid(){
        LOGGER.warn("begin getVoipBSSByLoid...");
        loidBSSReportMap = reportJLBIO.getXVoipBSSByLoid(loid);
        LOGGER.warn("end getVoipBSSByLoid,result:{}",loidBSSReportMap);
        return "loidVoipBSS";
    }


    private void getTimeSec(){
        if (!StringUtil.IsEmpty(startTime)) {
            DateTimeUtil dt = new DateTimeUtil(startTime);
            startTimeSec = String.valueOf(dt.getLongTime());
        }
        if (!StringUtil.IsEmpty(endTime)) {
            DateTimeUtil dt = new DateTimeUtil(endTime);
            endTimeSec = String.valueOf(dt.getLongTime());
        }
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getLoid() {
        return loid;
    }

    public void setLoid(String loid) {
        this.loid = loid;
    }

    public VoipXIPBSSReportJLBIO getReportJLBIO() {
        return reportJLBIO;
    }

    public void setReportJLBIO(VoipXIPBSSReportJLBIO reportJLBIO) {
        this.reportJLBIO = reportJLBIO;
    }

    public List<Map<String, String>> getResultReportList() {
        return resultReportList;
    }

    public void setResultReportList(List<Map<String, String>> resultReportList) {
        this.resultReportList = resultReportList;
    }

    public Map getSession() {
        return session;
    }


    public String getStartTimeSec() {
        return startTimeSec;
    }

    public void setStartTimeSec(String startTimeSec) {
        this.startTimeSec = startTimeSec;
    }

    public String getEndTimeSec() {
        return endTimeSec;
    }

    public void setEndTimeSec(String endTimeSec) {
        this.endTimeSec = endTimeSec;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }

    public String[] getTitle() {
        return title;
    }

    public void setTitle(String[] title) {
        this.title = title;
    }

    public String[] getColumn() {
        return column;
    }

    public void setColumn(String[] column) {
        this.column = column;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<Map<String, String>> getResultReportInfoList() {
        return resultReportInfoList;
    }

    public void setResultReportInfoList(List<Map<String, String>> resultReportInfoList) {
        this.resultReportInfoList = resultReportInfoList;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public Map<String, String> getLoidBSSReportMap() {
        return loidBSSReportMap;
    }

    public void setLoidBSSReportMap(Map<String, String> loidBSSReportMap) {
        this.loidBSSReportMap = loidBSSReportMap;
    }

    @Override
    public void setSession(Map<String, Object> map) {
    }
}
