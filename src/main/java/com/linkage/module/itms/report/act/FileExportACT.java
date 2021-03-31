
package com.linkage.module.itms.report.act;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.linkage.commons.util.StringUtil;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.liposs.manaconf.PropertiesFileManager;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.FileExportBIO;

import action.splitpage.splitPageAction;

/**
 * @author guankai (Ailk No.300401)
 * @version 1.0
 * @category com.linkage.module.itms.report.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 * @since 2020年4月20日
 */
public class FileExportACT extends splitPageAction implements ServletRequestAware, ServletResponseAware {

    private static final long serialVersionUID = 5439745993843034844L;
    private static Logger logger = LoggerFactory.getLogger(FileExportACT.class);
    private static final int BUFFER_SIZE = 16 * 1024 * 1024;
    private FileExportBIO bio;

    public FileExportBIO getBio() {
        return bio;
    }

    public void setBio(FileExportBIO bio) {
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

    // ID
    private String fileExportId = null;
    // 导出操作人
    private String fileExportUser = null;
    // 导出时间
    private String fileExportTime = null;
    // 说明
    private String fileExportDesc = null;
    // 文件导出状态 (0未完成、1已完成)
    private String status = null;
    // 导出文件名
    private String filename = null;
    // 取数sql
    private String fileExportSql = null;
    // 导出字段（$$拆分）
    private String fileExportField = null;
    // 地市
    private String cityId = null;

    // 开始时间
    private String startTime = null;
    // 结束时间
    private String endTime = null;
    /**
     * 属地列表
     */
    private List<Map<String, String>> cityList = null;
    /**
     * 数据列表
     */
    private List<Map> data = null;

    public String getFileExportId() {
        return fileExportId;
    }

    public void setFileExportId(String fileExportId) {
        this.fileExportId = fileExportId;
    }

    public String getFileExportUser() {
        return fileExportUser;
    }

    public void setFileExportUser(String fileExportUser) {
        this.fileExportUser = fileExportUser;
    }

    public String getFileExportTime() {
        return fileExportTime;
    }

    public void setFileExportTime(String fileExportTime) {
        this.fileExportTime = fileExportTime;
    }

    public String getFileExportDesc() {
        return fileExportDesc;
    }

    public void setFileExportDesc(String fileExportDesc) {
        this.fileExportDesc = fileExportDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileExportSql() {
        return fileExportSql;
    }

    public void setFileExportSql(String fileExportSql) {
        this.fileExportSql = fileExportSql;
    }

    public String getFileExportField() {
        return fileExportField;
    }

    public void setFileExportField(String fileExportField) {
        this.fileExportField = fileExportField;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
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

    /**
     * 绑定率统计查询页面展示
     *
     * @return
     */
    public String init() {
        logger.debug("init()");
		// 获取配置用户
		User curUser = ((UserRes) request.getSession().getAttribute("curUser")).getUser();
        cityList = CityDAO.getAllNextCityListByCityPid(WebUtil.getCurrentUser().getCityId());
        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        endTime = dateTimeUtil.getDate();
        dateTimeUtil.getNextDate(-1);
        startTime = dateTimeUtil.getDate();
        return "init";
    }

    /**
     * 报表下载统计列表页面展示
     *
     * @return
     */
    public String getFileExportInfo() {
        logger.debug("getFileExportInfo()");
        // 获取配置用户
        User curUser = ((UserRes) request.getSession().getAttribute("curUser")).getUser();

        Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
        logger.warn("报表下载统计   操作人ID：" + WebUtil.getCurrentUser().getUser().getId() + "   统计开始时间：" + startTime + "   统计结束时间："
                + endTime + "   属地：" + cityMap.get(cityId));
        data = bio.getFileExportInfo(fileExportDesc,startTime, endTime, cityId, curUser, curPage_splitPage, num_splitPage);
        int total = bio.getFileExportCount(fileExportDesc,startTime, endTime, cityId, curUser);
        if (total % num_splitPage == 0) {
            maxPage_splitPage = total / num_splitPage;
        } else {
            maxPage_splitPage = total / num_splitPage + 1;
        }
        return "list";
    }

    public void download() {
        logger.warn("download({},{})", new Object[]{filename, response});
        String path = "/export/home/itms/BatchProcessUti_fileExport/data/";
        InputStream fis=null;
        OutputStream os=null;
        try {
            // path是指欲下载的文件的路径
            File file = new File(path + filename);
            // 取得文件名
            String filename = file.getName();
            // 以流的形式下载文件。
            fis = new BufferedInputStream(new FileInputStream(path + filename));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            fis=null;
            
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
            response.addHeader("Content-Length", "" + file.length());
            os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            os.write(buffer);
            os.flush();
            os.close();
            os=null;
        } catch (IOException e) {
            logger.error("download file:[{}], error:", path + filename, e);
        }finally{
        	if(fis!=null){
        		try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	if(os!=null){
        		try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
    }
}
