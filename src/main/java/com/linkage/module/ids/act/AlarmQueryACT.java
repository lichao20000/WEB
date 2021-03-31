package com.linkage.module.ids.act;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.ids.bio.AlarmQueryBIO;

@SuppressWarnings("serial")
public class AlarmQueryACT extends splitPageAction implements SessionAware,
		ServletRequestAware {

	private static Logger logger = LoggerFactory.getLogger(AlarmQueryACT.class);
	private AlarmQueryBIO bio;
	// session
	@SuppressWarnings({ "rawtypes", "unused" })
	private Map session = null;
	@SuppressWarnings("unused")
	private HttpServletRequest request;
	@SuppressWarnings("rawtypes")
	private List<Map> alarmList;
	private String alarmname;
	private String alarmcode;
	private String alarmlevel;
	private String alarmobject;
	private String hour;
	private String count;
	private String temperature;
	private String lightpower;
	private String timedelay;
	private String packetloss;
	private String id;
	private String ajax;
	// ********Export All Data To Excel****************
	@SuppressWarnings("rawtypes")
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	private int total;

	public String getIdsarmInfoList() {
		alarmList = bio.getIdsarmInfoList(alarmname, alarmcode, alarmlevel,
				alarmobject, hour, count, temperature, timedelay, lightpower,
				packetloss, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countIdsarmInfoList(alarmname, alarmcode,
				alarmlevel, alarmobject, hour, count, temperature, timedelay,
				lightpower, packetloss, curPage_splitPage, num_splitPage);
		return "alarmList";
	}

	public String getIdsarmInfoListExcel() {
		alarmList = bio.getIdsarmInfoListExcel(alarmname, alarmcode,
				alarmlevel, alarmobject, hour, count, temperature, timedelay,
				lightpower, packetloss);
		String excelCol = "alarm_name#alarm_code#alarm_level#alarm_period#alarm_count#rx_power#temperature#delay#loss_pp#send_sheet_obj";
		String excelTitle = "告警名称#告警代码#告警级别#N个小时#M次#光功率(小于等于)#温度(大于等于)#时延(大于等于)#丢包率(大于等于)#派单对象";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "预检预修告警查询引擎信息";
		data = alarmList;
		return "excel";
	}

	public String insert() {
		bio.insert(alarmname, alarmcode, alarmlevel, alarmobject, hour, count,
				temperature, timedelay, lightpower, packetloss);
		return "alarmList";
	}

	public String delete() {
		bio.delete(id);
		return "alarmList";
	}
	
	public String update() {
		bio.update(id,alarmname, alarmcode, alarmlevel, alarmobject, hour, count,
				temperature, timedelay, lightpower, packetloss);
		return "alarmList";
	}

	/**
	 * 时间转化
	 */

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getAlarmList() {
		return alarmList;
	}

	@SuppressWarnings("rawtypes")
	public void setAlarmList(List<Map> alarmList) {
		this.alarmList = alarmList;
	}

	public String getAlarmname() {
		return alarmname;
	}

	public void setAlarmname(String alarmname) {
		this.alarmname = alarmname;
	}

	public String getAlarmcode() {
		return alarmcode;
	}

	public void setAlarmcode(String alarmcode) {
		this.alarmcode = alarmcode;
	}

	public String getAlarmlevel() {
		return alarmlevel;
	}

	public void setAlarmlevel(String alarmlevel) {
		this.alarmlevel = alarmlevel;
	}

	public String getAlarmobject() {
		return alarmobject;
	}

	public void setAlarmobject(String alarmobject) {
		this.alarmobject = alarmobject;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getLightpower() {
		return lightpower;
	}

	public void setLightpower(String lightpower) {
		this.lightpower = lightpower;
	}

	public String getTimedelay() {
		return timedelay;
	}

	public void setTimedelay(String timedelay) {
		this.timedelay = timedelay;
	}

	public String getPacketloss() {
		return packetloss;
	}

	public void setPacketloss(String packetloss) {
		this.packetloss = packetloss;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getData() {
		return data;
	}

	@SuppressWarnings("rawtypes")
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

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public AlarmQueryBIO getBio() {
		return bio;
	}

	public void setBio(AlarmQueryBIO bio) {
		this.bio = bio;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
