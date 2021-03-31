package com.linkage.module.liposs.performance.action;

import java.util.List;

import com.linkage.module.liposs.performance.bio.PortFilterRuleBIO;
import com.linkage.module.liposs.system.basesupport.BaseSupportSplitAction;

/**
 * 端口过滤规则配置
 * 包括规则的增删改。
 * @author zhangsong(3704)
 * @version 1.0
 * @since 2008-09-05
 * @category performance
 */
public class PortFilterRuleAction extends BaseSupportSplitAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1263445545333605082L;
	//业务类
	PortFilterRuleBIO portFilterRuleBio;
	//设备厂商列表
	List companyList;
	//设备编号列表
	List deviceModelList;
	//设备厂商
	String companyID;
	//设备编号
	String deviceModel;
	//过滤类型
	String filterType;
	//过滤信息
	String filterValue;
	//所有过滤规则列表
	List filterRulesList;
	//设备编号
	String delDeviceModel;
	
	String messageStr="";
	/**
	 * 转向选择厂商的动作
	 * @return
	 */
	public String toChooseCompany(){
		//清空提示信息
		messageStr = "";
		//获得设备厂商
		companyList = portFilterRuleBio.getAllCompany();
		//根据分页获得过滤规则
		filterRulesList = portFilterRuleBio.getAllFilter(curPage_splitPage, num_splitPage);
//		获得最大分页数
		int size =  portFilterRuleBio.getFilterNumber();
		totalRowCount_splitPage = size;
		if( size%num_splitPage!=0){
			maxPage_splitPage = size/num_splitPage+1;
		}else{
			maxPage_splitPage = size/num_splitPage;
		}
		return "success";
	}
	
	
	/**
	 * 根据选择的厂商获得设备类型
	 * @return
	 */
	public String chooseCompany(){
		//获得设备厂商
		companyList = portFilterRuleBio.getAllCompany();
//		获得设备型号列表
		deviceModelList = portFilterRuleBio.getAllDeviceModelByCompany(Integer.parseInt(companyID));
		filterRulesList = portFilterRuleBio.getAllFilter(curPage_splitPage, num_splitPage);
//		获得最大分页数
		int size =  portFilterRuleBio.getFilterNumber();
		if( size%num_splitPage!=0){
			maxPage_splitPage = size/num_splitPage+1;
		}else{
			maxPage_splitPage = size/num_splitPage;
		}
		return "success";
	}
	
	
	/**
	 * 保存过滤规则
	 * @return
	 */
	public String savePortFilterRule(){
		companyList = portFilterRuleBio.getAllCompany();
		deviceModelList = portFilterRuleBio.getAllDeviceModelByCompany(Integer.parseInt(companyID));
//		保存过滤规则
		messageStr = portFilterRuleBio.saveFilterRule( deviceModel, Integer.parseInt(filterType), filterValue);
		filterRulesList = portFilterRuleBio.getAllFilter(curPage_splitPage, num_splitPage);
//		获得最大分页数
		int size =  portFilterRuleBio.getFilterNumber();
		if( size%num_splitPage!=0){
			maxPage_splitPage = size/num_splitPage+1;
		}else{
			maxPage_splitPage = size/num_splitPage;
		}
		return toChooseCompany();
	}
	
	
	
	/**
	 * 删除过滤规则
	 * @return
	 */
	public String removePortFilterRule(){
//		删除过滤规则
		messageStr = portFilterRuleBio.delFilterRule(delDeviceModel, Integer.parseInt(filterType));
		filterRulesList = portFilterRuleBio.getAllFilter(curPage_splitPage, num_splitPage);
//		获得最大分页数
		int size =  portFilterRuleBio.getFilterNumber();
		if( size%num_splitPage!=0){
			maxPage_splitPage = size/num_splitPage+1;
		}else{
			maxPage_splitPage = size/num_splitPage;
		}
		return toChooseCompany();
	}
	
	
	/**
	 * 根据条件查询过滤规则
	 * @return
	 */
    public String searchPortFilterRule(){
    	messageStr = "";
    	companyList = portFilterRuleBio.getAllCompany();
		deviceModelList = portFilterRuleBio.getAllDeviceModelByCompany(Integer.parseInt(companyID));
    	filterRulesList = portFilterRuleBio.searchFilterRules(deviceModel,filterType);
    	int size =  filterRulesList.size();
		if( size%num_splitPage!=0){
			maxPage_splitPage = size/num_splitPage+1;
		}else{
			maxPage_splitPage = size/num_splitPage;
		}
    	return "success";
    }
	
	

	public List getCompanyList() {
		return companyList;
	}
	public void setCompanyList(List companyList) {
		this.companyList = companyList;
	}
	public List getDeviceModelList() {
		return deviceModelList;
	}
	public void setDeviceModelList(List deviceModelList) {
		this.deviceModelList = deviceModelList;
	}
	public String getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public String getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public List getFilterRulesList() {
		return filterRulesList;
	}

	public void setFilterRulesList(List filterRulesList) {
		this.filterRulesList = filterRulesList;
	}

	public String getDelDeviceModel() {
		return delDeviceModel;
	}

	public void setDelDeviceModel(String delDeviceModel) {
		this.delDeviceModel = delDeviceModel;
	}

	public PortFilterRuleBIO getPortFilterRuleBio() {
		return portFilterRuleBio;
	}

	public void setPortFilterRuleBio(PortFilterRuleBIO portFilterRuleBio) {
		this.portFilterRuleBio = portFilterRuleBio;
	}

	public String getMessageStr() {
		return messageStr;
	}

	public void setMessageStr(String messageStr) {
		this.messageStr = messageStr;
	}

	public String getCompanyID() {
		return companyID;
	}

	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}

}
