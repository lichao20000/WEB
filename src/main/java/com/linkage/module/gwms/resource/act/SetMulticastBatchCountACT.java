
package com.linkage.module.gwms.resource.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.resource.bio.SetMulticastBatchCountBIO;

/**
 * @author zhaixx (Ailk No.)
 * @version 1.0
 * @since 2018年11月9日
 * @category com.linkage.module.gwms.config.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class SetMulticastBatchCountACT extends splitPageAction implements SessionAware {

	private static final long serialVersionUID = -3464620678936120546L;
	private Map session;
	private static Logger logger = LoggerFactory.getLogger(SetMulticastBatchCountACT.class);
	private SetMulticastBatchCountBIO bio;
	private String type = "";
	// 属地
	private String cityId = "00";
	// 查询结果数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	
	private String task_name;
	private String task_id;
	/** 开始时间 */
	private String starttime = "";
	/** 开始时间 */
	private String starttime1 = "";
	/** 结束时间 */
	private String endtime = "";
	/** 结束时间 */
	private String endtime1 = "";

	@Override
	public void setSession(Map session) {
		this.session = session;
	}

	/** 新报表查询使用：初始化 */
	public String initQuery() {
		logger.debug("initQuery");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		DateTimeUtil dt = new DateTimeUtil();
		starttime = dt.getDate();
		dt = new DateTimeUtil(starttime);
		long start_time = dt.getLongTime();
		dt = new DateTimeUtil(start_time * 1000);
		starttime = dt.getLongDate();
		dt = new DateTimeUtil((start_time+24*3600-1) * 1000);
		endtime = dt.getLongDate();
		return "initQuery";
	}

	/** 新报表查询使用：查询列表 */
	public String queryMulticastBatchList() {
		this.setTime();
		logger.debug("....{},{},{},{},{},.",starttime1,endtime1, task_name, curPage_splitPage, num_splitPage);
		data = bio.getOrderTaskListNew(curPage_splitPage, num_splitPage, starttime1, endtime1, task_name);
		maxPage_splitPage = bio.countOrderTaskNew(curPage_splitPage, num_splitPage, starttime1, endtime1, task_name);
		return "queryMulticastBatchList";
	}

	/** 新报表查询使用：查询详情 */
	public String queryMulticastBatchDetail() {
		logger.debug("....{},{},{},{},,.",task_id, task_name, curPage_splitPage, num_splitPage);
		data = bio.getOrderTaskDetailNew(task_id, task_name, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countOrderTaskDetailNew(task_id, task_name, curPage_splitPage, num_splitPage);
		return "queryMulticastDetailList";
	}
	
	/**
	 * 导出任务属地分组列表
	 * @return
	 */
	public String getOrderTaskDetailNewExcel() {
		logger.debug("queryTaskGyCityExcel({}),",task_id);
		data = bio.getOrderTaskDetailNewExcel(task_id,task_name);
		String excelCol = null;
		String excelTitle = null;
		excelTitle = "任务名称#属地#总数#成功数#失败数#未做数";
		excelCol = "task_name#city_name#totalNum#succNum#failNum#unDoneNum";
		
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "OrderTaskDetail";
		logger.debug("....."+data.toString());
		return "excel";
	}
	
	
	/**新报表查询使用：根据属地，成功失败未作状态查询详情*/
	public String showDetailListByType(){
		logger.debug("showDetailListByType(){},{},{},{},{}",task_id, cityId, type, curPage_splitPage, num_splitPage);
		data = bio.showDetailListByType(task_id, cityId, type, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.showDetailListCountByType(task_id, cityId, type, curPage_splitPage, num_splitPage);
		return "showDetailListByType";
	}
	
	/** 新报表查询使用：根据属地，成功失败未作状态查询详情导出*/
	public String showDetailListByTypeExcel() {
		logger.debug("showDetailListByTypeExcel(){},{},{}",task_id, cityId, type);
		data = bio.showDetailListByType(task_id, cityId, type, 0, 0);
		String excelCol = null;
		String excelTitle = null;
		excelTitle = "设备序列号#厂商#型号#区域#版本#执行开始时间#执行结束时间#执行结果#结果描述";
		excelCol = "device_serialnumber#vendor_id#device_model_id#city_name#version#start_time#end_time#result_id#result_desc";
		
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "devList";
		logger.debug("....."+data.toString());
		return "excel";
	}
	
	
	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + starttime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime))
		{
			starttime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime))
		{
			endtime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
	}

	
	public SetMulticastBatchCountBIO getBio() {
		return bio;
	}

	
	public void setBio(SetMulticastBatchCountBIO bio) {
		this.bio = bio;
	}

	
	public String getType() {
		return type;
	}

	
	public void setType(String type) {
		this.type = type;
	}

	
	public String getCityId() {
		return cityId;
	}

	
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	
	public List<Map> getData() {
		return data;
	}

	
	public void setData(List<Map> data) {
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

	
	public String getTask_name() {
		return task_name;
	}

	
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	
	public String getTask_id() {
		return task_id;
	}

	
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	
	public String getStarttime() {
		return starttime;
	}

	
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	
	public String getStarttime1() {
		return starttime1;
	}

	
	public void setStarttime1(String starttime1) {
		this.starttime1 = starttime1;
	}

	
	public String getEndtime() {
		return endtime;
	}

	
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	
	public String getEndtime1() {
		return endtime1;
	}

	
	public void setEndtime1(String endtime1) {
		this.endtime1 = endtime1;
	}
	
	
}
