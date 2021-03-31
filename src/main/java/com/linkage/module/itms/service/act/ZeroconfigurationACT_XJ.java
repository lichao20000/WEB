package com.linkage.module.itms.service.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.service.bio.ZeroconfigurationBIO_XJ;

import action.splitpage.splitPageAction;

public class ZeroconfigurationACT_XJ extends splitPageAction implements SessionAware{
	
	private static final long serialVersionUID = 20160107L;
	private static Logger logger = LoggerFactory.getLogger(ZeroconfigurationACT_XJ.class);
	private Map session;
	
	/**用户信息*/
	private String user_info="";

	/**设备序列号*/
	private String device_sn="";
	
	/**业务类型*/
	private String service_type="";
	private List<Map<String, String>> service_typeList;
	
	/**操作类型*/
	private String operate_type="";
	private List<Map<String, String>> operate_typeList;
	
	/**属地*/
	private List<Map<String, String>> cityList;
	private String cityId="";
	
	/**接收日期*/
	private String starttime="";
	/**接收日期*/
	private String endtime="";
	/**mac地址*/
	private String cpe_mac;
	/**提示信息*/
	private String tipMsg="";
	
	private List<Map> list;
	private ZeroconfigurationBIO_XJ bio;
	
	public String init(){
		logger.warn("ZeroconfigurationACT_XJ init() ");
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();
		dt = new DateTimeUtil(starttime);
		starttime = dt.getLongDate();
		UserRes curUser = (UserRes) session.get("curUser");
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		
		service_typeList=bio.queryServiceType();
		operate_typeList=bio.queryOperateType();
	    
		return "init";
	}
	
	public String query(){
		logger.warn("ZeroconfigurationACT_XJ query() ");
		
		tipMsg=bio.queryBindMsg(user_info,device_sn,service_type,operate_type,cpe_mac);
		if(tipMsg.isEmpty()){
			list=bio.queryList(curPage_splitPage, num_splitPage,user_info,device_sn,service_type,operate_type,cityId,starttime,endtime,cpe_mac);
			maxPage_splitPage = bio.getCount(num_splitPage,user_info,device_sn,service_type,operate_type,cityId,starttime,endtime,cpe_mac);
		}
		return "list";
	}

	public String getUser_info() {
		return user_info;
	}


	public void setUser_info(String user_info) {
		this.user_info = user_info;
	}


	public String getDevice_sn() {
		return device_sn;
	}


	public void setDevice_sn(String device_sn) {
		this.device_sn = device_sn;
	}


	public String getService_type() {
		return service_type;
	}


	public void setService_type(String service_type) {
		this.service_type = service_type;
	}


	public String getOperate_type() {
		return operate_type;
	}


	public void setOperate_type(String operate_type) {
		this.operate_type = operate_type;
	}
	
	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public ZeroconfigurationBIO_XJ getBio() {
		return bio;
	}

	public void setBio(ZeroconfigurationBIO_XJ bio) {
		this.bio = bio;
	}
	
	public Map getSession() {
		return session;
	}
	
	@Override
	public void setSession(Map session) {
		this.session = session;

	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	public List<Map<String, String>> getService_typeList() {
		return service_typeList;
	}

	public void setService_typeList(List<Map<String, String>> service_typeList) {
		this.service_typeList = service_typeList;
	}

	public List<Map<String, String>> getOperate_typeList() {
		return operate_typeList;
	}

	public void setOperate_typeList(List<Map<String, String>> operate_typeList) {
		this.operate_typeList = operate_typeList;
	}
	
	public List<Map> getList() {
		return list;
	}

	public void setList(List<Map> list) {
		this.list = list;
	}

	public String getCpe_mac() {
		return cpe_mac;
	}

	public void setCpe_mac(String cpe_mac) {
		this.cpe_mac = cpe_mac;
	}

	public String getTipMsg() {
		return tipMsg;
	}

	public void setTipMsg(String tipMsg) {
		this.tipMsg = tipMsg;
	}
	
}
