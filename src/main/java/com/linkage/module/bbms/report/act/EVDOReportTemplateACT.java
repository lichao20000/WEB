/**
 * 
 */
package com.linkage.module.bbms.report.act;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.module.bbms.report.bio.EVDOReportTemplateBIO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-11-20
 * @category com.linkage.module.bbms.report.act
 * 
 */
public class EVDOReportTemplateACT extends ActionSupport implements ServletRequestAware{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** log */
	private Logger logger = LoggerFactory.getLogger(EVDOReportTemplateACT.class);
	
	//request取登陆帐号使用
	@SuppressWarnings("unused")
	private HttpServletRequest request;

	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}
	
	/**
	 * 业务逻辑处理
	 */
	EVDOReportTemplateBIO evdoBIO = null;
	
	////////////////////////////////////////////////////////////////////////////
	//对于挂菜单时就必须将其附属在地址后面
	//TIME:时长
	//FLUX:流量
	//FREQUENCY:频次
	//TMSLOT:时段
	//ACTIVE:激活情况
	//EVDOTYPE:网关类型
	////////////////////////////////////////////////////////////////////////////
	/**
	 * 报表名称
	 */
	private String reportName = null;
	/**
	 * 报表标题 在jsp的界面title
	 */
	private String titleCN = null;
	/**
	 * 查询结果标题 在jsp查询的结果界面显示
	 */
	private String titleResult = null;
	/**
	 * 报表类型 1：日报表；2：周报表；3：月报表
	 */
	private String reportType = null;
	/**
	 * 统计类型 1：按区域；2：按行业
	 */
	private String countType = null;
	/**
	 * 统计日期 秒格式的字符串
	 */
	private String queryDate = null;
	/**
	 * 页面显示时间
	 */
	private String queryDateStr = null;
	/**
	 * 是否是其他导出 默认不导出 excel：EXCEL导出
	 */
	private String isReport = null;
	/**
	 * 查询结果数据
	 */
	private List reportResultList = null;
	
	//excel导出相关字段
	private String fileName;
	private String[] title;
	private String[] column;
	private ArrayList<Map> data;
	
	/**
	 * 入口方法
	 */
	public String execute() throws Exception {
		logger.debug("execute({})",reportName);
		this.countType = "1";
		this.queryDateStr = new DateTimeUtil().getYYYY_MM_DD();
		//判断为那种数据报表，默认为网关类型
		if("TIME".equals(reportName)){
			this.titleCN = "网关使用时长统计报表";
			this.titleResult = "网关使用时长统计报表";
		}else if("FLUX".equals(reportName)){
			this.titleCN = "网关流量统计报表";
			this.titleResult = "网关流量统计报表";
		}else if("FREQUENCY".equals(reportName)){
			this.titleCN = "网关频次统计报表";
			this.titleResult = "网关频次统计报表";
		}else if("TMSLOT".equals(reportName)){
			this.titleCN = "网关使用时段统计报表";
			this.titleResult = "网关使用时段统计报表";
		}else if("ACTIVE".equals(reportName)){
			this.titleCN = "EVDO网关激活统计报表";
			this.titleResult = "EVDO网关激活统计报表";
		}else{
			this.titleCN = "EVDO网关主备统计报表";
			this.titleResult = "EVDO网关主备统计报表";
		}
		return SUCCESS;
	}

	/**
	 * 统计数据
	 * @return
	 */
	public String getReportData(){
		logger.debug("getReportData({})",reportName);
		this.queryDateStr = new DateTimeUtil(Long.parseLong(this.queryDate)*1000).getYYYY_MM_DD();
		this.reportResultList = new ArrayList();
		//判断为那种数据报表，默认为网关类型
		if("TIME".equals(reportName)){
			this.titleResult = "网关使用时长统计报表";
			this.reportResultList = evdoBIO.getTimeLengthData(reportType, queryDate, countType);
			if("excel".equals(isReport)){
				this.fileName = "网关使用时长统计报表";
				this.title = new String[4];
				this.title[0] = "类别";
				this.title[1] = "有线时长";
				this.title[2] = "无线时长";
				this.title[3] = "小计";
				this.column = new String[4];
				this.column[0] = "count_desc";
				this.column[1] = "wire_time";
				this.column[2] = "wireless_time";
				this.column[3] = "all_time";
				this.data = new ArrayList<Map>();
				for(int i=0;i<this.reportResultList.size();i++){
					Map one = (Map) this.reportResultList.get(i);
					this.data.add(one);
				}
				return "excel";
			}else{
				return "timeList";
			}
		}else if("FLUX".equals(reportName)){
			this.titleResult = "网关流量统计报表";
			this.reportResultList = evdoBIO.getFluxData(reportType, queryDate, countType);
			if("excel".equals(isReport)){
				this.fileName = "网关流量统计报表";
				this.title = new String[6];
				this.title[0] = "类别";
				this.title[1] = "有线上行流量";
				this.title[2] = "有线下行流量";
				this.title[3] = "无线上行流量";
				this.title[4] = "无线下行流量";
				this.title[5] = "小计";
				this.column = new String[6];
				this.column[0] = "count_desc";
				this.column[1] = "wire_up_flux";
				this.column[2] = "wire_down_flux";
				this.column[3] = "wireless_up_flux";
				this.column[4] = "wireless_down_flux";
				this.column[5] = "all_time";
				this.data = new ArrayList<Map>();
				for(int i=0;i<this.reportResultList.size();i++){
					Map one = (Map) this.reportResultList.get(i);
					this.data.add(one);
				}
				return "excel";
			}else{
				return "fluxList";
			}
		}else if("FREQUENCY".equals(reportName)){
			this.titleResult = "网关频次统计报表";
			this.reportResultList = evdoBIO.getFrequencyData(reportType, queryDate, countType);
			if("excel".equals(isReport)){
				this.fileName = "网关频次统计报表";
				this.title = new String[2];
				this.title[0] = "类别";
				this.title[1] = "无线使用频次";
				this.column = new String[6];
				this.column[0] = "count_desc";
				this.column[1] = "frequency";
				this.data = new ArrayList<Map>();
				for(int i=0;i<this.reportResultList.size();i++){
					Map one = (Map) this.reportResultList.get(i);
					this.data.add(one);
				}
				return "excel";
			}else{
				return "frequencyList";
			}
		}else if("TMSLOT".equals(reportName)){
			this.titleResult = "网关使用时段统计报表";
			this.reportResultList = evdoBIO.getTmslotData(reportType, queryDate, countType);
			if("excel".equals(isReport)){
				this.fileName = "网关使用时段统计报表";
				this.title = new String[13];
				this.column = new String[13];
				this.title[0] = "类别";
				this.column[0] = "count_desc";
				for(int j=1;j<12;j++){
					this.title[j] = j+6+":00-"+j+7+":00-";
					this.column[j] = "tmslot_"+j+6;
				}
				this.title[12] = "小计";
				this.column[12] = "all";
				this.data = new ArrayList<Map>();
				for(int i=0;i<this.reportResultList.size();i++){
					Map one = (Map) this.reportResultList.get(i);
					this.data.add(one);
				}
				return "excel";
			}else{
				return "tmslotList";
			}
		}else if("ACTIVE".equals(reportName)){
			this.titleResult = "EVDO网关激活统计报表";
			this.reportResultList = evdoBIO.getActiveData(reportType, queryDate, countType);
			if("excel".equals(isReport)){
				this.fileName = "EVDO网关激活统计报表";
				this.title = new String[4];
				this.title[0] = "类别";
				this.title[1] = "激活网关";
				this.title[2] = "未激活网关";
				this.title[3] = "小计";
				this.column = new String[4];
				this.column[0] = "count_desc";
				this.column[1] = "active_num";
				this.column[2] = "activeless_num";
				this.column[3] = "all_num";
				this.data = new ArrayList<Map>();
				for(int i=0;i<this.reportResultList.size();i++){
					Map one = (Map) this.reportResultList.get(i);
					this.data.add(one);
				}
				return "excel";
			}else{
				return "activeList";
			}
		}else{
			this.titleResult = "EVDO网关主备统计报表";
			this.reportResultList = evdoBIO.getEvdoTypeData(reportType, queryDate, countType);
			if("excel".equals(isReport)){
				this.fileName = "EVDO网关主备统计报表";
				this.title = new String[4];
				this.title[0] = "类别";
				this.title[1] = "主用链路";
				this.title[2] = "备用链路";
				this.title[3] = "小计";
				this.column = new String[4];
				this.column[0] = "count_desc";
				this.column[1] = "main_num";
				this.column[2] = "standy_num";
				this.column[3] = "all_num";
				this.data = new ArrayList<Map>();
				for(int i=0;i<this.reportResultList.size();i++){
					Map one = (Map) this.reportResultList.get(i);
					this.data.add(one);
				}
				return "excel";
			}else{
				return "evdoTypeList";
			}
		}
	}
	
	/**
	 * @return the countType
	 */
	public String getCountType() {
		return countType;
	}

	/**
	 * @param countType the countType to set
	 */
	public void setCountType(String countType) {
		this.countType = countType;
	}

	/**
	 * @return the queryDate
	 */
	public String getQueryDate() {
		return queryDate;
	}

	/**
	 * @param queryDate the queryDate to set
	 */
	public void setQueryDate(String queryDate) {
		this.queryDate = queryDate;
	}

	/**
	 * @return the reportName
	 */
	public String getReportName() {
		return reportName;
	}

	/**
	 * @param reportName the reportName to set
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	/**
	 * @return the reportType
	 */
	public String getReportType() {
		return reportType;
	}

	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	/**
	 * @return the titleCN
	 */
	public String getTitleCN() {
		return titleCN;
	}

	/**
	 * @param titleCN the titleCN to set
	 */
	public void setTitleCN(String titleCN) {
		this.titleCN = titleCN;
	}

	/**
	 * @return the titleResult
	 */
	public String getTitleResult() {
		return titleResult;
	}

	/**
	 * @param titleResult the titleResult to set
	 */
	public void setTitleResult(String titleResult) {
		this.titleResult = titleResult;
	}

	/**
	 * @return the isReport
	 */
	public String getIsReport() {
		return isReport;
	}

	/**
	 * @param isReport the isReport to set
	 */
	public void setIsReport(String isReport) {
		this.isReport = isReport;
	}

	/**
	 * @return the queryDateStr
	 */
	public String getQueryDateStr() {
		return queryDateStr;
	}

	/**
	 * @param queryDateStr the queryDateStr to set
	 */
	public void setQueryDateStr(String queryDateStr) {
		this.queryDateStr = queryDateStr;
	}

	/**
	 * @return the evdoBIO
	 */
	public EVDOReportTemplateBIO getEvdoBIO() {
		return evdoBIO;
	}

	/**
	 * @param evdoBIO the evdoBIO to set
	 */
	public void setEvdoBIO(EVDOReportTemplateBIO evdoBIO) {
		this.evdoBIO = evdoBIO;
	}

	/**
	 * @return the column
	 */
	public String[] getColumn() {
		return column;
	}

	/**
	 * @param column the column to set
	 */
	public void setColumn(String[] column) {
		this.column = column;
	}

	/**
	 * @return the data
	 */
	public ArrayList<Map> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(ArrayList<Map> data) {
		this.data = data;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the reportResultList
	 */
	public List getReportResultList() {
		return reportResultList;
	}

	/**
	 * @param reportResultList the reportResultList to set
	 */
	public void setReportResultList(List reportResultList) {
		this.reportResultList = reportResultList;
	}

	/**
	 * @return the title
	 */
	public String[] getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String[] title) {
		this.title = title;
	}
	
}
