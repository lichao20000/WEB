package com.linkage.module.gwms.report.act;

import action.splitpage.splitPageAction;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.report.bio.LanGatherInfoBIO;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author songxq
 * @version 1.0
 * @category
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 * @since 2020/8/4.
 */
public class LanGatherInfoACT  extends splitPageAction implements SessionAware,
        ServletRequestAware {
    private static final long serialVersionUID = 1891651756165L;
    Logger logger = LoggerFactory.getLogger(LanGatherInfoACT.class);

    private Map session;

    private HttpServletRequest request;

    private String cityId = "";

    private String[] title ;
    private String[] column ;
    private ArrayList<Map> data;

    private String instAreaName;

    /** 导出文件名 */
    private String fileName = null;

    /** 属地列表 */
    private List<Map<String, String>> cityList = null;

    /** 开始时间 */
    private String startOpenDate = "";
    /** 结束时间 */
    private String endOpenDate = "";

    /** 开始时间 */
    private String startOpenDate1 = "";
    /** 结束时间 */
    private String endOpenDate1 = "";

    private LanGatherInfoBIO bio;

    private List<Map> lanInfoList = null;

    private List<Map> noMatchList = null;


    public String init()
    {
        instAreaName = Global.instAreaShortName;
        UserRes curUser = (UserRes) session.get("curUser");
        long areaId = curUser.getAreaId();
        Map cityMap= bio.queryCityIdByAreaId(StringUtil.getStringValue(areaId));

        cityId = StringUtil.getStringValue(cityMap.get("city_id"));
        if("00".equals(cityId))
        {
            cityList = CityDAO.getNextCityListByCityPid(cityId);
        }
        else
        {
            cityList =  new ArrayList<Map<String, String>>();
            cityList.add(cityMap);
        }

        Map map = getWeekDate();
        startOpenDate = StringUtil.getStringValue(map.get("mondayDate"));
        endOpenDate = StringUtil.getStringValue(map.get("sundayDate"));
        return "init";
    }


    public String query()
    {
        logger.warn("开始查询[{}]lan采集结果。",cityId);
        dealDate();
        lanInfoList = bio.query(cityId,startOpenDate1,endOpenDate1);
        logger.warn("[{}]lan采集结果查询完成。",cityId);
        return "lanInfoList";
    }

    public String queryDetail()
    {
        dealDate();
        noMatchList = bio.queryDetail(curPage_splitPage, num_splitPage,cityId,startOpenDate1,endOpenDate1);

        maxPage_splitPage = bio.queryDetailCount(curPage_splitPage, num_splitPage,cityId,startOpenDate1,endOpenDate1);

        return "noMatchList";
    }

    public String exportDetail()
    {
        dealDate();
        data = bio.exportDetail(cityId,startOpenDate1,endOpenDate1);

        fileName = "lan采集统计报表";

        title = new String[6];
        column = new String[6];

        title[0] = "属地";
        title[1] = "LOID";
        title[2] = "宽带账号";
        title[3] = "型号";
        title[4] = "厂家";
        title[5] = "匹配端口的速率";

        column[0] = "city_name";
        column[1] = "loid";
        column[2] = "username";
        column[3] = "devicemodel";
        column[4] = "vendorName";
        column[5] = "speed";
        return "excel";
    }



    private void dealDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = df.parse(startOpenDate);
            long start = date.getTime();
            startOpenDate1 = StringUtil.getStringValue(start/1000);
            date = df.parse(endOpenDate);
            long end = date.getTime();
            endOpenDate1 = StringUtil.getStringValue(end/1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }





    /**
     * 获取当前时间所在周的周一和周日的日期时间
     * @return
     */
    public Map<String,String> getWeekDate() {
        Map<String,String> map = new HashMap();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        Calendar cal = Calendar.getInstance();
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if(dayWeek==1){
            dayWeek = 8;
        }

        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date mondayDate = cal.getTime();
        String weekBegin = sdf.format(mondayDate);

        cal.add(Calendar.DATE, 4 +cal.getFirstDayOfWeek());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 59);
        Date sundayDate = cal.getTime();
        String weekEnd = sdf.format(sundayDate);

        map.put("mondayDate", weekBegin);
        map.put("sundayDate", weekEnd);
        return map;
    }





    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
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

    public ArrayList<Map> getData() {
        return data;
    }

    public void setData(ArrayList<Map> data) {
        this.data = data;
    }

    public String getInstAreaName() {
        return instAreaName;
    }

    public void setInstAreaName(String instAreaName) {
        this.instAreaName = instAreaName;
    }

    public List<Map<String, String>> getCityList() {
        return cityList;
    }

    public void setCityList(List<Map<String, String>> cityList) {
        this.cityList = cityList;
    }

    public String getStartOpenDate() {
        return startOpenDate;
    }

    public void setStartOpenDate(String startOpenDate) {
        this.startOpenDate = startOpenDate;
    }

    public String getEndOpenDate() {
        return endOpenDate;
    }

    public void setEndOpenDate(String endOpenDate) {
        this.endOpenDate = endOpenDate;
    }

    public LanGatherInfoBIO getBio() {
        return bio;
    }

    public void setBio(LanGatherInfoBIO bio) {
        this.bio = bio;
    }

    public List<Map> getLanInfoList() {
        return lanInfoList;
    }

    public void setLanInfoList(List<Map> lanInfoList) {
        this.lanInfoList = lanInfoList;
    }

    public List<Map> getNoMatchList() {
        return noMatchList;
    }

    public void setNoMatchList(List<Map> noMatchList) {
        this.noMatchList = noMatchList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


}
