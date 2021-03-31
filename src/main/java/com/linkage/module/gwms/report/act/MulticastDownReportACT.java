package com.linkage.module.gwms.report.act;

import action.splitpage.splitPageAction;
import com.linkage.module.gwms.report.bio.MulticastDownReportBIO;
import com.linkage.module.gwms.util.StringUtil;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MulticastDownReportACT extends splitPageAction implements SessionAware,
		ServletRequestAware {

	private static final long serialVersionUID = 1891651756165L;
	Logger logger = LoggerFactory.getLogger(MulticastDownReportACT.class);

	private MulticastDownReportBIO bio;
	private HttpServletRequest request;

	private Map session;

	private String gwShare_queryType = null;

	private String gwShare_fileName = null;

	private String ajax = null;

	private String cityId = "";
	private String gwType = "";
	private String fileName = "";
	private String[] title ;
	private String[] column ;
	private ArrayList<Map> data;
	private List<Map> devList = null;
	private List<Map> businessList = null;
	private List<Map<String,String>> oldDevDetailList = null;



	public String queryDeviceList(){
		devList = bio.queryDeviceList(gwShare_fileName);
		return "deviceList";
	}

	public String toExcel(){
		this.data = (ArrayList<Map>) bio.queryDeviceList(gwShare_fileName);

		String excelCol = "city_name#username#device_serialnumber#complete_time#last_time#vendor_name#device_model#softwareversion#hardwareversion";
		String excelTitle = "属地#Loid#绑定设备序列号#设备注册系统时间#设备最近更新时间#厂商#型号#软件版本#硬件版本";
		this.column = excelCol.split("#");
		this.title = excelTitle.split("#");
		this.fileName = "组播下移设备报表";


		return "excel";
	}


	public String getFileSize(){
		int size = bio.getFileSize(gwShare_fileName);
		ajax = StringUtil.getStringValue(size);
		return "ajax";
	}

	public String queryBusinessList(){
		businessList = bio.queryBusinessList(gwShare_fileName);
		return "businessList";
	}


	public String toBusinessExcel(){
		this.data = (ArrayList<Map>) bio.queryBusinessList(gwShare_fileName);

		String excelCol = "city_name#username#netAccount#netVlan#netOpenStatus#itvAccount#itvVlan" +
				"#itvOpenStatus#vpdnAccount#vpdnVlan#vpdnOpenStatus#voipPhone#voipVlan#voipOpenStatus#voipProtocol";
		String excelTitle = "属地#Loid#宽带账号#宽带VLAN#宽带下发状态#ITV账号#itvVlan#itv下发状态" +
				"#vpdn账号#vpdnVlan#vpdn下发状态#语音号码#语音Vlan#语音下发状态#语音协议类型";
		this.column = excelCol.split("#");
		this.title = excelTitle.split("#");
		this.fileName = "组播下移业务报表";

		return "excel";
	}

	
	public String getTyDetailCountList(){
		return "detailList";
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


	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}


	public String getGwType() {
		return gwType;
	}

	public void setGwType(String gwType) {
		this.gwType = gwType;
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

	public List<Map> getDevList() {
		return devList;
	}

	public void setDevList(List<Map> devList) {
		this.devList = devList;
	}

	public List<Map<String, String>> getOldDevDetailList() {
		return oldDevDetailList;
	}

	public void setOldDevDetailList(List<Map<String, String>> oldDevDetailList) {
		this.oldDevDetailList = oldDevDetailList;
	}

	public MulticastDownReportBIO getBio() {
		return bio;
	}

	public void setBio(MulticastDownReportBIO bio) {
		this.bio = bio;
	}

	public String getGwShare_queryType() {
		return gwShare_queryType;
	}

	public void setGwShare_queryType(String gwShare_queryType) {
		this.gwShare_queryType = gwShare_queryType;
	}

	public String getGwShare_fileName() {
		return gwShare_fileName;
	}

	public void setGwShare_fileName(String gwShare_fileName) {
		this.gwShare_fileName = gwShare_fileName;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public ArrayList<Map> getData() {
		return data;
	}

	public void setData(ArrayList<Map> data) {
		this.data = data;
	}

	public List<Map> getBusinessList() {
		return businessList;
	}

	public void setBusinessList(List<Map> businessList) {
		this.businessList = businessList;
	}
}
