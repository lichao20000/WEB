package com.linkage.module.gwms.report.act;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.report.bio.MismathSpeedBIO;

import action.splitpage.splitPageAction;



public class MismathSpeedACT extends splitPageAction implements
		ServletRequestAware {

	private static final long serialVersionUID = 1891651756165L;
	Logger logger = LoggerFactory.getLogger(MismathSpeedACT.class);

	private MismathSpeedBIO bio;
	private HttpServletRequest request;

	private String cityId = "";
	
	//导出
	private String fileName = "";
	private String[] title ;
	private String[] column ;
	//数据
	private List<Map> data;
	private List<Map> devList = null;
	
	//家庭网关不匹配速率报表初始化
	public String init(){
		if(null==this.cityId || "".equals(this.cityId)){
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			long area_id = curUser.getAreaId();
			List<Map> list = bio.queryCityId(StringUtil.getStringValue(area_id));
			Map map=list.get(0);
			cityId=StringUtil.getStringValue(map.get("city_id"));
			if (StringUtil.IsEmpty(cityId)) {
				cityId=curUser.getCityId();
			}
			
		}
		this.data = bio.getMismathSpeedCount(cityId);
		return "init";
	}
	
	//详情
	public String queryDetail(){
		devList = bio.queryDetail(cityId,curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.queryDetailCount(cityId,curPage_splitPage, num_splitPage);
		return "detail";
	}
	
	//详情导出
	public String toExcel(){
		fileName = "家庭网关速率不匹配详情";
		title = new String[] { "分公司","地市","签约速率", "loid", "宽带账号", "型号", "硬件版本","软件版本"};
		column = new String[] {  "company_name","city_name", "downlink","loid","username", "device_model", "hardwareversion", "softwareversion"};
		data = bio.toExcel(cityId);
		return "excel";
	}
	
	//不匹配終端已修改初始化
	public String initChanged(){
		if(null==this.cityId || "".equals(this.cityId)){
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			long area_id = curUser.getAreaId();
			List<Map> list = bio.queryCityId(StringUtil.getStringValue(area_id));
			Map map=list.get(0);
			cityId=StringUtil.getStringValue(map.get("city_id"));
			if (StringUtil.IsEmpty(cityId)) {
				cityId=curUser.getCityId();
			}
			
		}
		this.data = bio.getMismathChageCount(cityId);
		return "initChanged";
	}	
	
	//不匹配修改导出
	public String chageExcel(){
		fileName = "家庭网关速率不匹配修改报表";
		title = new String[] { "分公司","已修改数量"};
		column = new String[] {  "city_name","total"};
		initCity();
		data = bio.getMismathChageCount(cityId);
		return "excel";
	}
	
	//不匹配終端已修改詳情
	public String queryChangedDetail(){
		devList = bio.getMismathChageDetail(cityId,curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getMismathChageDetailCount(cityId,curPage_splitPage, num_splitPage);
		return "chagDetail";
	}
	
	//不匹配終端已修改详情导出
	public String toChangedExcel(){
		fileName = "家庭网关速率不匹配修改详情";
		title = new String[] { "分公司","地市","签约速率", "loid", "宽带账号", "型号", "硬件版本","软件版本","串码"};
		column = new String[] {  "company_name","city_name", "downlink","loid","netusername", "device_model", "hardwareversion", "softwareversion","oui_sn"};
		data = bio.toChageExcel(cityId);
		return "excel";
	}	
	
	//不匹配終端新增初始化
	public String initAdded(){
		if(null==this.cityId || "".equals(this.cityId)){
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			long area_id = curUser.getAreaId();
			List<Map> list = bio.queryCityId(StringUtil.getStringValue(area_id));
			Map map=list.get(0);
			cityId=StringUtil.getStringValue(map.get("city_id"));
			if (StringUtil.IsEmpty(cityId)) {
				cityId=curUser.getCityId();
			}
			
		}
		this.data = bio.getMismathAddCount(cityId);
		return "initAdded";
	}
	
	//不匹配修改导出
	public String addExcel(){
		fileName = "家庭网关速率不匹配新增报表";
		title = new String[] { "分公司","新增数量"};
		column = new String[] {  "city_name","total"};
		initCity();
		data = bio.getMismathAddCount(cityId);
		return "excel";
	}
	//不匹配終端新增詳情
	public String queryAddDetail(){
		devList = bio.getMismathAddDetail(cityId,curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getMismathAddDetailCount(cityId,curPage_splitPage, num_splitPage);
		return "addDetail";
	}
	
	//不匹配終端新增改详情导出
	public String toAddExcel(){
		fileName = "家庭网关速率不匹配新增详情";
		title = new String[] { "分公司","地市","签约速率", "loid", "宽带账号", "型号", "硬件版本","软件版本","串码","开户时间","是否本月开户"};
		column = new String[] {  "company_name","city_name", "downlink","loid","netusername", "device_model", "hardwareversion", "softwareversion","oui_sn","open_date","currmonth"};
		data = bio.toAddExcel(cityId);
		return "excel";
	}	
	
	private void initCity(){
		if(null==this.cityId || "".equals(this.cityId)){
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			long area_id = curUser.getAreaId();
			List<Map> list = bio.queryCityId(StringUtil.getStringValue(area_id));
			Map map=list.get(0);
			cityId=StringUtil.getStringValue(map.get("city_id"));
			if (StringUtil.IsEmpty(cityId)) {
				cityId=curUser.getCityId();
			}
		}
	}
	
	public MismathSpeedBIO getBio()
	{
		return bio;
	}

	
	public void setBio(MismathSpeedBIO bio)
	{
		this.bio = bio;
	}

	
	public HttpServletRequest getRequest()
	{
		return request;
	}

	
	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	
	public String getCityId()
	{
		return cityId;
	}

	
	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	
	public String getFileName()
	{
		return fileName;
	}

	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
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

	
	public List<Map> getData()
	{
		return data;
	}

	
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	
	public List<Map> getDevList()
	{
		return devList;
	}

	
	public void setDevList(List<Map> devList)
	{
		this.devList = devList;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
		
	}
}
