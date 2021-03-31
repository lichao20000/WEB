package com.linkage.module.itms.resource.act;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.bio.NewDeviceQueryBIO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class NewDeviceQueryACT extends splitPageAction {

	private static Logger logger = LoggerFactory
			.getLogger(NewDeviceQueryACT.class);
	// 开始时间
	private String startOpenDate = "";
	private String startOpenDate1 = "";
	// 结束时间
	private String endOpenDate = "";
	// 结束时间
	private String endOpenDate1 = "";
	// 属地
	private String city_id = null;
	
	// 属地列表
	private List<Map<String, String>> cityList = null;

	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）

	private String gw_type;

	// 查询出语音信息列表
	private List<Map> newDevMap;

	private NewDeviceQueryBIO bio;

	@Override
	public String execute() throws Exception {
		UserRes curUser = WebUtil.getCurrentUser();
		city_id = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		//获取系统当前时间
		String dateTime = DateUtil.getNowTime("yyyy-MM-dd");
		
		endOpenDate = dateTime;
		
		DateTimeUtil befDateTime = new DateTimeUtil(dateTime);
		befDateTime.getNextMonth(-1);// 获取上个月时间
		//获取一个月之内新装设备
		startOpenDate = DateUtil.format(befDateTime.getDateTime(), "yyyy-MM-dd");
		
		return "init";
	}

	public String NewDeviceQueryInfo() {
		this.setTime();
		newDevMap = bio.NewDeviceQueryInfo(city_id, startOpenDate1,
				endOpenDate1, curPage_splitPage, num_splitPage, gw_type);
		maxPage_splitPage = bio.countNewDeviceQueryInfo(city_id,
				startOpenDate1, endOpenDate1, curPage_splitPage, num_splitPage,
				gw_type);
		if(!"1".equals(gw_type))
		{
			return "list";	
		}
		else {
			return "list1";	
		}
		
	}

	public String NewDeviceQueryExcel() {
		this.setTime();
		newDevMap = bio.NewDeviceQueryExcel(city_id, startOpenDate1,
				endOpenDate1, gw_type);
		String excelCol =  "";
		if ("1".equals(gw_type)) {
			excelCol = "username#device_name#device_model#vendor_name#cpe_currentupdatetime#binddate";
		} else {
			excelCol = "username#device_serialnumber#device_model#vendor_name#cpe_currentupdatetime#binddate";
		}
		String excelTitle = "账号#设备序列号#型号#厂家#最近一次上线时间#绑定时间";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "newDeviceMap";
		data = newDevMap;
		return "excel";
	}

	/**
	 * 时间转化
	 */
	private void setTime() {
		// start time
		if (StringUtil.IsEmpty(startOpenDate)) {
			startOpenDate1 = null;
		} else {
			String start = startOpenDate + " 00:00:00";
			DateTimeUtil st = new DateTimeUtil(start);
			startOpenDate1 = String.valueOf(st.getLongTime());
		}
		// end time
		if (StringUtil.IsEmpty(endOpenDate)) {
			endOpenDate1 = null;
		} else {
			String end = endOpenDate + " 23:59:59";
			DateTimeUtil et = new DateTimeUtil(end);
			endOpenDate1 = String.valueOf(et.getLongTime());
		}
	}


	public List<Map> getNewDevMap() {
		return newDevMap;
	}

	public void setNewDevMap(List<Map> newDevMap) {
		this.newDevMap = newDevMap;
	}

	public String getEndOpenDate() {
		return endOpenDate;
	}

	public void setEndOpenDate(String endOpenDate) {
		this.endOpenDate = endOpenDate;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}


	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStartOpenDate() {
		return startOpenDate;
	}

	public void setStartOpenDate(String startOpenDate) {
		this.startOpenDate = startOpenDate;
	}

	public NewDeviceQueryBIO getBio() {
		return bio;
	}

	public void setBio(NewDeviceQueryBIO bio) {
		this.bio = bio;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}
	
}
