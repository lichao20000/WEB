
package com.linkage.module.gwms.report.act;

import action.splitpage.splitPageAction;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.report.bio.RegistBindCancelBIO;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * JLLT-REQ-RMS-20201225-JH-001（吉林联通RMS平台新增设备统计报表需求  终端注册、绑定和注销状态的统计报表）
 * @author ou.yuan
 * @date 2020-12-30
 */
public class RegistBindCancelACT extends splitPageAction implements SessionAware, ServletRequestAware {

    // 日志记录
    private static Logger logger = LoggerFactory.getLogger(RegistBindCancelACT.class);
    // session
    private Map session = null;
    private HttpServletRequest request;

    // 导出数据
    private List<Map> data;
    // 导出文件列标题
    private String[] title;
    // 导出文件列
    private String[] column;
    // 导出文件名
    private String fileName;
    /** 开始时间 */
    private String starttime = "";
    /** 开始时间 */
    private String starttime1 = "";
    /** 结束时间 */
    private String endtime = "";
    /** 结束时间 */
    private String endtime1 = "";


    private String servTypeId = "";

    // 属地
/*    private String city_id = null;
    // 属地列表
    private List<Map<String, String>> cityList = null;*/


    private String cityId = "";




    private RegistBindCancelBIO registBindCancelBIO;


    /**
     * 查询设备信息
     *
     * @return String
     */
    public String execute() throws Exception
    {
        return "list";
    }

    /**
     * 页面初始化
     * @return
     */
    public String init() {
        logger.warn("RegistBindCancelACT.init()");

        UserRes curUser = (UserRes) session.get("curUser");

        /*city_id = curUser.getCityId();
        cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());*/

        starttime = this.getStartDate();
        endtime = this.getEndDate();

        // 获取系统当前时间
        /*String dateTime = DateUtil.getNowTime("yyyy-MM-dd");
        endtime = dateTime;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        // 将date - 7
        c.add(Calendar.DATE, -7);
        Date startDate = c.getTime();

        // 获取一周之内批量重启设备
        starttime = format.format(startDate);*/
        return "init";
    }


    /**
     * 终端注册、绑定和注销状态的统计报表 查询
     *
     * @author ouyuan
     * @date 9.2, 2020
     * @return String
     */
    public String getRegistBindCancelList() {
        logger.warn("RegistBindCancelACT.getRegistBindCancelList()");
        this.setTime();
        data = registBindCancelBIO.getRegistBindCancelList(starttime1, endtime1);
        return "registBindCancel";
    }

    /**
     * 终端注册、绑定和注销状态的统计报表 导出Excel
     * @return
     */
    public String getRegistBindCancelExcel() {
        fileName = "终端注册、绑定和注销状态的统计报表查询单";
        title = new String[] {"属地", "注册设备数", "绑定设备数", "注销设备数"};
        column = new String[] {"cityName", "registNum", "bindNum", "cancelNum"};
        data = registBindCancelBIO.getRegistBindCancelExcel(starttime1, endtime1);
        return "excel";
    }





    // 当前年的1月1号
    private String getStartDate()
    {
        GregorianCalendar now = new GregorianCalendar();
        SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        now.set(GregorianCalendar.DATE, 1);
        now.set(GregorianCalendar.MONTH, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        String time = fmtrq.format(now.getTime());
        return time;
    }

    // 当前时间的23:59:59,如 2011-05-11 23:59:59
    private String getEndDate()
    {
        GregorianCalendar now = new GregorianCalendar();
        SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        now.set(Calendar.HOUR_OF_DAY, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);
        String time = fmtrq.format(now.getTime());
        return time;
    }

    /**
     * 时间转化
     */
   /* private void setTime() {
        logger.debug("setTime()" + starttime);
        DateTimeUtil dt = null; // 定义DateTimeUtil
        if (starttime == null || "".equals(starttime)) {
            starttime1 = null;
        }
        else {
            String start = starttime + " 00:00:00";
            DateTimeUtil st = new DateTimeUtil(start);
            starttime1 = String.valueOf(st.getLongTime());
        }
        if (endtime == null || "".equals(endtime)) {
            endtime1 = null;
        }
        else {
            String end = endtime + " 23:59:59";
            DateTimeUtil et = new DateTimeUtil(end);
            endtime1 = String.valueOf(et.getLongTime());
        }
    }*/


    private void setTime()
    {
        logger.debug("setTime()" + starttime);
        DateTimeUtil dt = null; // 定义DateTimeUtil
        if (starttime == null || "".equals(starttime))
        {
            starttime1 = null;
        }
        else
        {
            dt = new DateTimeUtil(starttime);
            starttime1 = String.valueOf(dt.getLongTime());
        }
        if (endtime == null || "".equals(endtime))
        {
            endtime1 = null;
        }
        else
        {
            dt = new DateTimeUtil(endtime);
            endtime1 = String.valueOf(dt.getLongTime());
        }
    }


    @Override
    public void setSession(Map<String, Object> session)
    {
        this.session = session;
    }

    @Override
    public void setServletRequest(HttpServletRequest request)
    {
        this.request = request;
    }



    public List<Map> getData()
    {
        return data;
    }

    public void setData(List<Map> data)
    {
        this.data = data;
    }

    public String[] getTitle()
    {
        return title;
    }

    public void setTitle(String[] title)
    {
        this.title = title;
    }

    public String[] getColumn()
    {
        return column;
    }

    public void setColumn(String[] column)
    {
        this.column = column;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getStarttime()
    {
        return starttime;
    }

    public void setStarttime(String starttime)
    {
        this.starttime = starttime;
    }

    public String getStarttime1()
    {
        return starttime1;
    }

    public void setStarttime1(String starttime1)
    {
        this.starttime1 = starttime1;
    }

    public String getEndtime()
    {
        return endtime;
    }

    public void setEndtime(String endtime)
    {
        this.endtime = endtime;
    }

    public String getEndtime1()
    {
        return endtime1;
    }

    public void setEndtime1(String endtime1)
    {
        this.endtime1 = endtime1;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }


    public RegistBindCancelBIO getRegistBindCancelBIO() {
        return registBindCancelBIO;
    }

    public void setRegistBindCancelBIO(RegistBindCancelBIO registBindCancelBIO) {
        this.registBindCancelBIO = registBindCancelBIO;
    }

    public String getServTypeId() {
        return servTypeId;
    }

    public void setServTypeId(String servTypeId) {
        this.servTypeId = servTypeId;
    }

}
