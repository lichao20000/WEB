package com.linkage.module.gtms.config.action;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.config.serv.ControlTimeOfUserServ;

/**
 * 
 * @author zhangsb
 * @date 2012年6月6日
 */
public class ControlTimeOfUserActionImpl implements ControlTimeOfUserAction {

	private static Logger logger = LoggerFactory
			.getLogger(ControlTimeOfUserActionImpl.class);

	private String cuId;
	/**时间类型*/
	private String typeId;
	/**日期 */
	private String conTime;
	private String conTimeStart;
	private String conTimeEnd;
	private String ajax;
   
	/** 导出数据 */
	private List<Map<String,Object>> data;
	/** 导出文件列标题 */
	private String[] title;
	/** 导出文件列 */
	private String[] column;
	/** 导出文件名 */
	private String fileName;
	private List<Map<String,Object>> timeList;
	private ControlTimeOfUserServ bio;

	public String init(){
		logger.debug("init();");
		return "init";
	}
	/**
	 * 增加记录
	 */
	public String add() {
		logger.debug("add()");
		int isRes = -1 ; 
		String tmpTime ="";
		tmpTime = setTime(conTime);
	    isRes = bio.addRecord(typeId,tmpTime);
	    if (isRes > 0){
	    	ajax = "1";
	    }else{
	    	ajax = "";
	    }
	    logger.warn("ajax="+ajax);
		return "ajax";
	}
	/**
	 * 删除记录
	 */
	public String delete() {
		logger.debug("delete()");
		int isRes = -1 ; 
	    isRes = bio.deleteRecord(cuId);
	    if (isRes > 0){
	    	ajax = "1";
	    }else{
	    	ajax = "";
	    }
		return "ajax";
	}
	/**
	 * 更新记录
	 */
	public String preUpdate() {
		logger.debug("preUpdate()");
		int isRes = -1 ; 
		Map<String,Object> map = null ;
	    map  = bio.getRecordById(cuId);
	    if(null != map ){
	    	typeId = StringUtil.getStringValue(map.get("type_id"));
	    	DateTimeUtil dt = new DateTimeUtil(StringUtil.getLongValue((map.get("con_time"))) * 1000);
	    	
	    	conTime = dt.getLongDate();
	    	cuId = StringUtil.getStringValue(map.get("cuid"));
	    }
		return "preUpdate";
	}
	/**
	 * 更新记录
	 */
	public String update() {
		logger.debug("update()");
		int isRes = -1 ; 
		String tmpTime ="";
		tmpTime = setTime(conTime);
	    isRes = bio.updateRecord(cuId,typeId,tmpTime);
	   
	    if (isRes > 0){
	    	ajax = "1";
	    }else{
	    	ajax = "";
	    }
		return "ajax";
	}
	/**
	 * 根据时间查询记录
	 */
	public String queryInfo() {
		logger.debug("queryInfo()");
		String tmpTimeStart ="";
		String tmpTimeEnd ="";
		tmpTimeStart = setTime(conTimeStart);
		tmpTimeEnd = setTime(conTimeEnd);
		timeList = bio.getRecordByTime(typeId,tmpTimeStart, tmpTimeEnd,"");
		return "list";
	}
	/**
	 * 导出数据记录
	 */
	public String getResultToExcel(){
		logger.debug("getResultToExcel()");
		String flag = "1";
		title = new String[]{"ID","时间类型","时间"};
		column = new String[]{"cuid","type_id","con_time"};
		fileName = "时间设置";
		String tmpTimeStart ="";
		String tmpTimeEnd ="";
		tmpTimeStart = setTime(conTimeStart);
		tmpTimeEnd = setTime(conTimeEnd);
		data  = bio.getRecordByTime(typeId,tmpTimeStart,tmpTimeEnd,flag);
		return "excel"; 
	}
	/**
	 * 时间转化
	 */
	private String setTime(String time) {
		logger.debug("setTime()" + time);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (time == null || "".equals(time)) {
			time = null;
		} else {
			dt = new DateTimeUtil(time);
			time = String.valueOf(dt.getLongTime());
		}
		return time;
	}

	public String getCuId() {
		return cuId;
	}

	public void setCuId(String cuId) {
		this.cuId = cuId;
	}

	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getConTime() {
		return conTime;
	}

	public void setConTime(String conTime) {
		this.conTime = conTime;
	}

	public String getAjax() {
		return ajax;
	}
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
	public ControlTimeOfUserServ getBio() {
		return bio;
	}
	public void setBio(ControlTimeOfUserServ bio) {
		this.bio = bio;
	}
	public String getConTimeStart() {
		return conTimeStart;
	}

	public void setConTimeStart(String conTimeStart) {
		this.conTimeStart = conTimeStart;
	}

	public String getConTimeEnd() {
		return conTimeEnd;
	}

	public void setConTimeEnd(String conTimeEnd) {
		this.conTimeEnd = conTimeEnd;
	}

	public List<Map<String, Object>> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<Map<String, Object>> timeList) {
		this.timeList = timeList;
	}
	public List<Map<String, Object>> getData() {
		return data;
	}
	public void setData(List<Map<String, Object>> data) {
		this.data = data;
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
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
