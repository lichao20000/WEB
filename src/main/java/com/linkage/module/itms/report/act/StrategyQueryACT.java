
package com.linkage.module.itms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.itms.report.bio.StrategyQueryBIO;

/**
 * 根据设备序列号或LOID或宽带账号（下拉列表）、开通时间、结束时间查询无线业务策略
 * 
 * @author wanghong5 2015-02-13
 */
@SuppressWarnings("unchecked")
public class StrategyQueryACT extends splitPageAction implements SessionAware{
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(StrategyQueryACT.class);
	// session
	private Map session;
	/**条件*/
	private String con="";
	/**条件内容*/
	private String condition="";
	/** 开始时间 */
	private String starttime = "";
	/** 结束时间 */
	private String endtime = "";
	/**开通状态*/
	private String openState="";
	
	private String awifi_type = "";
	
	/**绑定数据*/
	private List<Map> list=null;
	
	private StrategyQueryBIO strategyQueryBio;
	
	/**获取device_id*/
	private List<Map> device_idList=null;

	/*
	 * 访问首页，获取限制参数（条件，时间段，开通状态）
	 */
	public String init(){
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();
		dt = new DateTimeUtil(starttime);
		starttime = dt.getLongDate();
		return "init";
	}
	
	/*
	 * 按限制条件查询
	 */
	public String getCountAll(){
		logger.warn("StrategyQueryACT.getCountAll()");
		device_idList=strategyQueryBio.getStrategyQueryDao().getDevice_id(con, condition.trim());
		if(device_idList.size()==1){
			list = strategyQueryBio.getDetailsForPage(device_idList,starttime,endtime,openState,awifi_type,curPage_splitPage,num_splitPage);
			maxPage_splitPage = strategyQueryBio.getDetailsCount(device_idList,starttime,endtime,openState,awifi_type,num_splitPage);
		}
		return "list";
	}
	
	/*
	 * 分页功能
	 */
	public String goPage() throws Exception {
		logger.warn("StrategyQueryACT.goPage()");
		device_idList=strategyQueryBio.getStrategyQueryDao().getDevice_id(con, condition);
		list = strategyQueryBio.getDetailsForPage(device_idList,starttime,endtime,openState,awifi_type,curPage_splitPage,num_splitPage);
		maxPage_splitPage = strategyQueryBio.getDetailsCount(device_idList,starttime,endtime,openState,awifi_type,num_splitPage);
		return "list";
	}
	
	
	public StrategyQueryACT(){
	}
	
	public Map getSession(){
		return session;
	}

	public void setSession(Map session){
		this.session = session;
	}

	public String getStarttime(){
		return starttime;
	}

	public void setStarttime(String starttime){
		this.starttime = starttime;
	}

	public String getEndtime(){
		return endtime;
	}

	public void setEndtime(String endtime){
		this.endtime = endtime;
	}
	
	public String getCon() {
		return con;
	}

	public void setCon(String con) {
		this.con = con;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getOpenState() {
		return openState;
	}

	public void setOpenState(String openState) {
		this.openState = openState;
	}

	public StrategyQueryBIO getStrategyQueryBio() {
		return strategyQueryBio;
	}

	public void setStrategyQueryBio(StrategyQueryBIO strategyQueryBio) {
		this.strategyQueryBio = strategyQueryBio;
	}
	
	public List<Map> getList() {
		return list;
	}

	public void setList(List<Map> list) {
		this.list = list;
	}

	public List<Map> getDevice_idList() {
		return device_idList;
	}

	public void setDevice_idList(List<Map> device_idList) {
		this.device_idList = device_idList;
	}

	public String getAwifi_type() {
		return awifi_type;
	}

	public void setAwifi_type(String awifi_type) {
		this.awifi_type = awifi_type;
	}

	

	
	
	

}
