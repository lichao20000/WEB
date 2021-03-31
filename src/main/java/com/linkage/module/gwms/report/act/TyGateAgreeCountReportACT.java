package com.linkage.module.gwms.report.act;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.report.bio.TyGateAgreeCountReportBIO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 江西电信天翼网关版本一致率
 * 
 * @author wangyan10(Ailk NO.76091)
 * @version 1.0
 * @since 2019-4-15
 */
public class TyGateAgreeCountReportACT extends ActionSupport implements
		ServletRequestAware {

	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(TyGateAgreeCountReportACT.class);
	// request取登陆帐号使用
	private HttpServletRequest request;

	/**
	 * BIO
	 */
	private TyGateAgreeCountReportBIO bio;

	/**
	 * 查询的属地
	 */
	private String cityId = "";

	/**
	 * 返回结果
	 */
	private List countList = null;

	/**
	 * 报表类型
	 */
	private String gwType = "";

	/**
	 * 导出报表的类型
	 */
	private String reportType = "";

	/**
	 * ECXCEL报表相关
	 */
	private String fileName = "";
	private String[] title ;
	private String[] column ;
	private ArrayList<Map> data;
	private String startTime;
	private String endTime;
	private String startTime1;
	private String endTime1;
	private String vendorId = "";
	private String deviceModelId = "";
	private String recent_start_Time = "";
	private String recent_start_Time1 = "";
	private String recent_end_Time = "";
	private String recent_end_Time1 = "";
	private String isExcludeUpgrade = "";
	private String is_highversion = "";
	private String hardwareversion = "";
	private String percent;
	private String querydata;
	
	/**
	 * 天翼网关最新版本
	 */
	private String isNewVersion = "";
	
	/**
	 * 初始化入口，初始化统计时间
	 * 
	 * @return
	 */
	public String execute(){
		
		logger.debug("execute()");
		
		if(null==this.cityId || "".equals(this.cityId)){
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			this.cityId = curUser.getCityId();
		}
		
		startTime = getStartDate();
		endTime = getEndDate();
		gwType = request.getParameter("gw_type");
		if(StringUtil.IsEmpty(gwType)) {
			gwType="1";
		}
		
		return SUCCESS;
	}
	
	/**
	 * 统计数据
	 */
	@SuppressWarnings("unchecked")
	public String getReportData(){
		
		if(null==this.cityId || "".equals(this.cityId)){
			logger.debug("TyGateAgreeCountReportACT=>getReportData()传入的cityId为空！");
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			this.cityId = curUser.getCityId();
		}
		this.setTime();
		this.countList = bio.getData(cityId,startTime1,endTime1,gwType,isExcludeUpgrade,recent_start_Time1,recent_end_Time1);
		
		if("excel".equals(reportType)){
			this.fileName = "天翼网关版本统一率报表";
			this.title = new String[4];
			this.column = new String[4];
			this.title[0] = "本地网";
			this.title[1] = "天翼网关总数";
			this.title[2] = "非统一版本天翼网关数";
			this.title[3] = "版本一致率";
			this.column[0] = "city_name";
			this.column[1] = "allTyCount";
			this.column[2] = "notTyCount";
			this.column[3] = "rate";
			this.data = new ArrayList();
			for(int i=0;i<this.countList.size();i++){
				this.data.add((Map)this.countList.get(i));
			}
			return "excel";
		}else{
			return "list";
		}
	}
	
	
	/**
	 * 获取指定地市，根据厂商型号版本进行划分
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getTyDetailCountList(){
		logger.debug("getTyDetailCountList()");
		
		if(null==this.cityId || "".equals(this.cityId)){
			logger.debug("getTyDetailCountList=>getTyDetailCountList()传入的cityId为空！");
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			this.cityId = curUser.getCityId();
		}
		this.setTime();
		this.countList = bio.getTyDetailCountList(cityId,startTime1,endTime1,isNewVersion,gwType,isExcludeUpgrade,recent_start_Time1,recent_end_Time1);
		
		if("excel".equals(reportType)){
			this.fileName = "天翼网关版本统一率详情报表";
			this.title = new String[5];
			this.column = new String[5];
			this.title[0] = "厂商";
			this.title[1] = "型号";
			this.title[2] = "硬件版本";
			this.title[3] = "软件版本";
			this.title[4] = "数量";
			this.column[0] = "vendor_add";
			this.column[1] = "device_model";
			this.column[2] = "hardwareversion";
			this.column[3] = "softwareversion";
			this.column[4] = "countNum";
			this.data = new ArrayList();
			for(int i=0;i<this.countList.size();i++){
				this.data.add((Map)this.countList.get(i));
			}
			return "excel";
		}else{
			return "detailList";
		}
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + startTime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startTime == null || "".equals(startTime))
		{
			startTime1 = "";
		}
		else
		{
			dt = new DateTimeUtil(startTime);
			startTime1 = String.valueOf(dt.getLongTime());
		}
		if (endTime == null || "".equals(endTime))
		{
			endTime1 = "";
		}
		else
		{
			dt = new DateTimeUtil(endTime);
			endTime1 = String.valueOf(dt.getLongTime());
		}
		
		if (recent_start_Time == null || "".equals(recent_start_Time))
		{
			recent_start_Time1 = "";
		}
		else
		{
			dt = new DateTimeUtil(recent_start_Time);
			recent_start_Time1 = String.valueOf(dt.getLongTime());
		}
		
		if (recent_end_Time == null || "".equals(recent_end_Time))
		{
			recent_end_Time1 = "";
		}
		else
		{
			dt = new DateTimeUtil(recent_end_Time);
			recent_end_Time1 = String.valueOf(dt.getLongTime());
		}
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
	
	private String getRecentStartDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		now.add(Calendar.MONTH,-3);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}
	
	public String init(){
		gwType = request.getParameter("gw_type");
		if(StringUtil.IsEmpty(gwType)) {
			gwType="1";
		}
		startTime = getStartDate();
		endTime = getEndDate();
		
		if (LipossGlobals.inArea(Global.SDDX)) {
			recent_start_Time = getRecentStartDate();
			recent_end_Time = getEndDate();
		}
		
		return "init";
	}
	
	public String queryDataListJXDX(){
		this.setTime();
		int num=0;
		countList = bio.queryDataListJXDX(gwType,vendorId,deviceModelId,isExcludeUpgrade,
				startTime1,endTime1,recent_start_Time1,recent_end_Time1);
		if (countList!=null && countList.size()>0) {
			for (int i = 0; i < countList.size(); i++) {
				Map<String, Object> map=(Map<String, Object>) countList.get(i);
				String total_percent=map.get("total_percent")==null?"":map.get("total_percent").toString();
				total_percent=total_percent.replace("%", "");
				if (Double.parseDouble(total_percent)>=95) {
					num++;
				}
			}
			DecimalFormat df=new DecimalFormat("0.00");
			percent = df.format((float)num/countList.size()*100)+"%";
		}else {
			percent="0.00%";
		}
		if("excel".equals(reportType)){
			this.fileName = "天翼网关版本统一率报表";
			this.title = new String[7];
			this.column = new String[7];
			this.title[0] = "厂商";
			this.title[1] = "型号";
			this.title[2] = "硬件版本";
			this.title[3] = "非最新版本数";
			this.title[4] = "最新版本数";
			this.title[5] = "天翼网关总数";
			this.title[6] = "天翼网关版本统一率";
			this.column[0] = "vendor_add";
			this.column[1] = "device_model";
			this.column[2] = "hardwareversion";
			this.column[3] = "sum2";
			this.column[4] = "sum1";
			this.column[5] = "total";
			this.column[6] = "total_percent";
			this.data = new ArrayList();
			for(int i=0;i<this.countList.size();i++){
				this.data.add((Map)this.countList.get(i));
			}
			Map<String, String> map=new HashMap<String, String>();
			map.put("vendor_add", "主流天翼网关版本统一率");
			map.put("total_percent", percent);
			data.add(map);
			return "excel";
		}else{
			return "jxdxList";
		}
	}
	
	public String queryDataListSDDX(){
		this.setTime();
		querydata=System.currentTimeMillis()/1000+"";
		countList = bio.queryDataListSDDX(querydata,gwType,vendorId,deviceModelId,isExcludeUpgrade,
				startTime1,endTime1,recent_start_Time1,recent_end_Time1);
		
		if("excel".equals(reportType)){
			this.fileName = "天翼网关版本统一率报表";
			this.title = new String[6];
			this.column = new String[6];
			this.title[0] = "厂商";
			this.title[1] = "型号";
			this.title[2] = "硬件版本";
			this.title[3] = "软件版本";
			this.title[4] = "数量";
			this.title[5] = "天翼网关版本统一率";
			this.column[0] = "vendor_add";
			this.column[1] = "device_model";
			this.column[2] = "hardwareversion";
			this.column[3] = "softwareversion";
			this.column[4] = "num";
			this.column[5] = "total_percent";
			this.data = new ArrayList();
			for(int i=0;i<this.countList.size();i++){
				this.data.add((Map)this.countList.get(i));
			}
			return "excel";
		}else{
			return "sddxList";
		}
	}
	
	public String querydetailListJXDX(){
		this.setTime();
		countList = bio.querydetailListJXDX(gwType,vendorId,deviceModelId,isExcludeUpgrade,
				startTime1,endTime1,recent_start_Time1,recent_end_Time1,is_highversion,hardwareversion);
		if("excel".equals(reportType)){
			this.fileName = "天翼网关版本统一率详情报表";
			this.title = new String[5];
			this.column = new String[5];
			this.title[0] = "厂商";
			this.title[1] = "型号";
			this.title[2] = "硬件版本";
			this.title[3] = "软件版本";
			this.title[4] = "数量";
			this.column[0] = "vendor_add";
			this.column[1] = "device_model";
			this.column[2] = "hardwareversion";
			this.column[3] = "softwareversion";
			this.column[4] = "countNum";
			this.data = new ArrayList();
			for(int i=0;i<this.countList.size();i++){
				this.data.add((Map)this.countList.get(i));
			}
			return "excel";
		}else{
			return "unificationDetailList";
		}
	}
	
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	public TyGateAgreeCountReportBIO getBio() {
		return bio;
	}

	public void setBio(TyGateAgreeCountReportBIO bio) {
		this.bio = bio;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public List getCountList() {
		return countList;
	}

	public void setCountList(List countList) {
		this.countList = countList;
	}

	public String getGwType() {
		return gwType;
	}

	public void setGwType(String gwType) {
		this.gwType = gwType;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
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

	public ArrayList<Map> getData() {
		return data;
	}

	public void setData(ArrayList<Map> data) {
		this.data = data;
	}

	public String getIsNewVersion() {
		return isNewVersion;
	}

	public void setIsNewVersion(String isNewVersion) {
		this.isNewVersion = isNewVersion;
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

	public String getStartTime1() {
		return startTime1;
	}

	public void setStartTime1(String startTime1) {
		this.startTime1 = startTime1;
	}

	public String getEndTime1() {
		return endTime1;
	}

	public void setEndTime1(String endTime1) {
		this.endTime1 = endTime1;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getDeviceModelId() {
		return deviceModelId;
	}

	public void setDeviceModelId(String deviceModelId) {
		this.deviceModelId = deviceModelId;
	}

	public String getRecent_start_Time() {
		return recent_start_Time;
	}

	public void setRecent_start_Time(String recent_start_Time) {
		this.recent_start_Time = recent_start_Time;
	}

	public String getRecent_start_Time1() {
		return recent_start_Time1;
	}

	public void setRecent_start_Time1(String recent_start_Time1) {
		this.recent_start_Time1 = recent_start_Time1;
	}

	public String getRecent_end_Time() {
		return recent_end_Time;
	}

	public void setRecent_end_Time(String recent_end_Time) {
		this.recent_end_Time = recent_end_Time;
	}

	public String getRecent_end_Time1() {
		return recent_end_Time1;
	}

	public void setRecent_end_Time1(String recent_end_Time1) {
		this.recent_end_Time1 = recent_end_Time1;
	}

	public String getIsExcludeUpgrade() {
		return isExcludeUpgrade;
	}

	public void setIsExcludeUpgrade(String isExcludeUpgrade) {
		this.isExcludeUpgrade = isExcludeUpgrade;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public String getIs_highversion() {
		return is_highversion;
	}

	public void setIs_highversion(String is_highversion) {
		this.is_highversion = is_highversion;
	}

	public String getHardwareversion() {
		return hardwareversion;
	}

	public void setHardwareversion(String hardwareversion) {
		this.hardwareversion = hardwareversion;
	}

	public String getQuerydata() {
		return querydata;
	}

	public void setQuerydata(String querydata) {
		this.querydata = querydata;
	}

}
