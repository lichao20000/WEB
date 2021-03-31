
package com.linkage.module.itms.report.act;

import action.splitpage.splitPageAction;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.bio.GroupManageBIO;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author guankai (Ailk No.300401)
 * @version 1.0
 * @category com.linkage.module.itms.report.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 * @since 2020年4月20日
 */
public class GroupManageACT extends splitPageAction implements ServletRequestAware, ServletResponseAware {

    private static final long serialVersionUID = 5439745993843034844L;
    private static Logger logger = LoggerFactory.getLogger(GroupManageACT.class);
    private static final int BUFFER_SIZE = 16 * 1024 * 1024;
    private GroupManageBIO bio;

    public GroupManageBIO getBio() {
        return bio;
    }

    public void setBio(GroupManageBIO bio) {
        this.bio = bio;
    }

    private HttpServletResponse response;
    private HttpServletRequest request;

    /**
     * HttpServletResponse
     */
    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    // 文件导出状态 (0未完成、1已完成)
    private String status = null;

    // 地市
    private String cityId = null;
    /**
     * 属地列表
     */
    private List<Map<String, String>> cityList = null;
    /**
     * 数据列表
     */
    private List<Map> data = null;


    /**
     * 导出Excel文件名
     */
    private String fileName = null;

    /**
     * 导出Excel的标题
     */
    private String[] title = null;

    /**
     * 导出Excel的列
     */
    private String[] column = null;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public List<Map<String, String>> getCityList() {
        return cityList;
    }

    public void setCityList(List<Map<String, String>> cityList) {
        this.cityList = cityList;
    }

    public List<Map> getData() {
        return data;
    }

    public void setData(List<Map> data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    /**
     * 绑定率统计查询页面展示
     *
     * @return
     */
    public String init() {
        logger.debug("init()");
        cityList = CityDAO.getAllNextCityListByCityPid(WebUtil.getCurrentUser().getCityId());
        return "init";
    }

    /**
     * 分组管理统计列表页面展示
     *
     * @return
     */
    public String getGroupManageList() {
        logger.debug("getGroupManageList()");
        // 获取配置用户
        data = bio.getGroupManageList(status, cityId, curPage_splitPage, num_splitPage);
        int total = bio.getGroupManageCount(status, cityId);
        if (total % num_splitPage == 0) {
            maxPage_splitPage = total / num_splitPage;
        } else {
            maxPage_splitPage = total / num_splitPage + 1;
        }
        return "list";
    }

    /**
     * 详细信息导出
     *
     * @return
     */
    public String getDetailExcel() {

        logger.debug("action==>getDetailExcel()");

        this.fileName = "分组管理基本信息导出";
        this.title = new String[]{"终端唯一标识","宽带账号","终端厂商","型号名称","用户姓名","用户住址","联系电话","用户备注","终端硬件版本","软件版本","IP地址","终端归属域路径","备注","终端注册状态","逻辑ID"};
        this.column = new String[]{"osn", "username", "vendor_name","device_model", "linkman","linkaddress","mobile","userremark","hardwareversion","softwareversion","loopback_ip","city_name","remark","openstatus","loid"};

        this.data = bio.getDetailExcel(cityId, status);

        return "excel";
    }


}
