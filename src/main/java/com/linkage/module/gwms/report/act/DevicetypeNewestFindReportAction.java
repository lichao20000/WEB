/**
 * 
 */
package com.linkage.module.gwms.report.act;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.report.bio.DevicetypeNewestFindReportBIO;


/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-9-9
 * @category com.linkage.module.gwms.report.act
 * 
 */
public class DevicetypeNewestFindReportAction extends splitPageAction implements ServletRequestAware {
	
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 业务逻辑处理
	 */
	DevicetypeNewestFindReportBIO bio;
	
	// request取登陆帐号使用
	@SuppressWarnings("unused")
	private HttpServletRequest request;

	/**
	 * 属地列表
	 */
	private List cityList = null;

	/**
	 * 属地
	 */
	private String cityId = null;
	
	/**
	 * Long 格式
	 */
	private String add_time = null;
	
	/**
	 * YYYY-mm-DD
	 */
	private String addTime = null;
	
	/**
	 * 查询结果的标题
	 */
	private List<String> titleList = null;
	
	/**
	 * 返回类型
	 */
	private String isReport = null;
	/**区分ITMS和BBMS*/
	private String gw_type;
	/**
	 * 查询的结果
	 */
	private List dataList = null;
	
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	/**
	 * 入口方法
	 */
	public String execute() throws Exception {
		
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		this.cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		
		if(null==this.cityId || "".equals(this.cityId)){
			this.cityId = curUser.getCityId();
		}
		
		String DATE_2_FORMAT = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_2_FORMAT);
		Calendar cal = Calendar.getInstance();
		this.addTime = sdf.format(cal.getTime());
		
		return SUCCESS;
	}

	public String getData() {
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(Long.parseLong(add_time)*1000);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		long startTime = cal.getTimeInMillis()/1000;
		cal.add(Calendar.DATE, -1);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.HOUR, 23);
		cal.set(Calendar.MINUTE,59);
		cal.set(Calendar.SECOND,59);
		long endTime = cal.getTimeInMillis()/1000;
		
		List cityTempList = bio.getCityList(cityId);
		this.titleList = new ArrayList<String>();
		this.titleList.add("厂商");
		this.titleList.add("型号");
		this.titleList.add("版本");
		for(int i=0;i<cityTempList.size();i++){
			this.titleList.add(String.valueOf(((Map)cityTempList.get(i)).get("city_name")));
		}
		
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String userCityId = curUser.getCityId();
		
		if(cityId.equals(userCityId)){
			this.titleList.add("小计");
		}
		
		this.dataList = bio.getData(startTime,endTime,cityTempList,cityId,userCityId,gw_type);
		cityTempList = null;
		if("excel".equals(isReport)){
			return "excel";
		}else{
			return "list";
		}
	}
	
	/**
	 * @return the add_time
	 */
	public String getAdd_time() {
		return add_time;
	}

	/**
	 * @param add_time the add_time to set
	 */
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	/**
	 * @return the addTime
	 */
	public String getAddTime() {
		return addTime;
	}

	/**
	 * @param addTime the addTime to set
	 */
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	/**
	 * @return the cityId
	 */
	public String getCityId() {
		return cityId;
	}

	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	/**
	 * @return the cityList
	 */
	public List getCityList() {
		return cityList;
	}

	/**
	 * @param cityList the cityList to set
	 */
	public void setCityList(List cityList) {
		this.cityList = cityList;
	}

	/**
	 * @return the bio
	 */
	public DevicetypeNewestFindReportBIO getBio() {
		return bio;
	}

	/**
	 * @param bio the bio to set
	 */
	public void setBio(DevicetypeNewestFindReportBIO bio) {
		this.bio = bio;
	}

	/**
	 * @return the dataList
	 */
	public List getDataList() {
		return dataList;
	}

	/**
	 * @param dataList the dataList to set
	 */
	public void setDataList(List dataList) {
		this.dataList = dataList;
	}

	/**
	 * @return the titleList
	 */
	public List<String> getTitleList() {
		return titleList;
	}

	/**
	 * @param titleList the titleList to set
	 */
	public void setTitleList(List<String> titleList) {
		this.titleList = titleList;
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

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gwType) {
		gw_type = gwType;
	}
    
}
