package com.linkage.module.bbms.service.act;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.bbms.service.bio.EVDOBindLogBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * EVDO模块：EVDO数据卡绑定日志
 * 
 * @author 漆学启
 * @date 2009-10-29
 */
public class EVDOBindLogACT extends splitPageAction implements
		ServletRequestAware {

	/** sid */
	private static final long serialVersionUID = -7948213365992547983L;

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(EVDOBindLogACT.class);

	EVDOBindLogBIO bio;

	// request取登陆帐号使用
	@SuppressWarnings("unused")
	private HttpServletRequest request;

	private String startDate = null;

	private String endDate = null;

	private String cityId = null;

	private List cityList = null;

	private String deviceNo = null;

	private List dataList = null;

	/**
	 * get data.
	 * 
	 * @return
	 */
	public String execute() {

		logger.debug("execute() start");

		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		this.startDate = sdf2.format(new Date());
		this.endDate = sdf2.format(new Date());
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		this.cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());

		if (null == this.cityId || "".equals(this.cityId)) {
			this.cityId = curUser.getCityId();
		}
		
		logger.debug("execute() end");
		
		return SUCCESS;

	}

	/**
	 * 数据查询
	 * 
	 * @return
	 */
	public String queryData() {

		logger.debug("queryData() start");
		
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		if (null == this.cityId || "".equals(this.cityId)) {
			this.cityId = curUser.getCityId();
		}

		dataList = bio.getEVDOBindLog(curPage_splitPage, num_splitPage, cityId,
				new DateTimeUtil(startDate+" 00:00:00").getLongTime(), new DateTimeUtil(
						endDate+" 23:59:59").getLongTime(), this.deviceNo);
		maxPage_splitPage = bio.getEVDOBindLogCount(curPage_splitPage,
				num_splitPage, cityId,new DateTimeUtil(startDate+" 00:00:00").getLongTime(), 
				new DateTimeUtil(endDate+" 23:59:59").getLongTime(), deviceNo);

		logger.debug("queryData() end");
		
		return "list";
	}

	/**
	 * 翻页查询
	 * 
	 * @return
	 */
	public String goPage() throws Exception {

		logger.debug("goPage() start");
		
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		if (null == this.cityId || "".equals(this.cityId)) {
			this.cityId = curUser.getCityId();
		}

		dataList = bio.getEVDOBindLog(curPage_splitPage, num_splitPage, cityId,
				new DateTimeUtil(startDate).getLongTime(), new DateTimeUtil(
						endDate).getLongTime(), this.deviceNo);
		maxPage_splitPage = bio.getEVDOBindLogCount(curPage_splitPage,
				num_splitPage, cityId,new DateTimeUtil(startDate+" 00:00:00").getLongTime(),
				new DateTimeUtil(endDate+" 23:59:59").getLongTime(), deviceNo);

		logger.debug("goPage() end");
		
		return "list";
	}

	/**
	 * set
	 */
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	/**
	 * @return the cityId
	 */
	public String getCityId() {
		return cityId;
	}

	/**
	 * @param cityId
	 *            the cityId to set
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
	 * @param cityList
	 *            the cityList to set
	 */
	public void setCityList(List cityList) {
		this.cityList = cityList;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the deviceNo
	 */
	public String getDeviceNo() {
		return deviceNo;
	}

	/**
	 * @param deviceNo
	 *            the deviceNo to set
	 */
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	/**
	 * @return the bio
	 */
	public EVDOBindLogBIO getBio() {
		return bio;
	}

	/**
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(EVDOBindLogBIO bio) {
		this.bio = bio;
	}

	/**
	 * @return the dataList
	 */
	public List getDataList() {
		return dataList;
	}

	/**
	 * @param dataList
	 *            the dataList to set
	 */
	public void setDataList(List dataList) {
		this.dataList = dataList;
	}

}
