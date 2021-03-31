package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.AutoSoftUpRuleBIO;

/**
 *	湖南联通自动软件升级规则管理
 */
@SuppressWarnings("rawtypes")
public class AutoSoftUpRuleACT extends splitPageAction implements SessionAware
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(AutoSoftUpRuleACT.class);
	
	/* 厂商列表*/
	private List<Map<String,String>> vendorList;
	/* 厂商ID*/
	private String vendorId;
	/* 型号ID*/
	private String deviceModelId;
	/* 软件版本*/
	private String softwareversion;
	/* 硬件版本*/
	private String hardwareversion;
	/*版本id*/
	private String devicetype_id;
	/*目标版本id*/
	private String version_id;
	/*适用网络类型*/
	private String dev_net_type;
	/*用户实际网络类型*/
	private String user_net_type;
	/*目标版本网络类型*/
	private String version_net_type;
	/*规则id*/
	private String rule_id="-1";
	
	/**湖南联通特制 	传入参数showType,1：“编辑”、“删除”；2：“编辑”；*/
	private String showType="";
	private String check_flag="1";
	
	private List<Map> ruleList;
	private String ajax;
	private Map session;
	private AutoSoftUpRuleBIO bio;
	
	
	/**初始化新增首页*/
	public String addjsp()
	{
		//编辑页面数据填充
		if(!"-1".equals(rule_id))
		{
			Map map=bio.getSoftUpRuleInfo(rule_id);
			vendorId=StringUtil.getStringValue(map,"vendor_id");
			deviceModelId=StringUtil.getStringValue(map,"device_model_id");
			hardwareversion=StringUtil.getStringValue(map,"hardwareversion");
			softwareversion=StringUtil.getStringValue(map,"softwareversion");
			user_net_type=StringUtil.getStringValue(map,"user_net_type");
			version_id=StringUtil.getStringValue(map,"version_id");
			devicetype_id=StringUtil.getStringValue(map,"devicetype_id");
			dev_net_type=StringUtil.getStringValue(map,"dev_net_type");
			version_net_type=StringUtil.getStringValue(map,"version_net_type");
			map=null;
		}
		return "add";
	}
	
	/**获取厂商*/
	public String getVendorS()
	{
		vendorList = bio.querVentorList();
		if(vendorList!=null && !vendorList.isEmpty()){
			for(Map<String,String> map:vendorList){
				String desc=StringUtil.getStringValue(map,"vendor_id")
						+"$"+StringUtil.getStringValue(map,"vendor_add");
				if(StringUtil.IsEmpty(ajax)){
					ajax=desc;
				}else{
					ajax+="#"+desc;
				}
			}
		}
		
		return "ajax";
	}
	
	/**获取软件型号数据devicetype_id,epg_version,net_type*/
	public String getDevType()
	{
		ajax=bio.getDevType(vendorId,deviceModelId,hardwareversion,softwareversion);
		return "ajax";
	}
	
	/**获取对应的目标版本*/
	public String getTargetSoftVersion()
	{
		ajax = bio.getTargetSoftVersion(deviceModelId);
		return "ajax";
	}
	
	/**获取指定的目标版本详细*/
	public String getTargetSoftVersionDetail()
	{
		ajax = bio.getTargetSoftVersionDetail(version_id);
		return "ajax";
	}
	
	/**新增*/
	public String add()
	{
		int num = bio.checkTargetVersionRule(devicetype_id,user_net_type);
		if(num==0){
			long  user_id=((UserRes) session.get("curUser")).getUser().getId();
			ajax=bio.addSoftUpRule(devicetype_id,dev_net_type,user_net_type,
									version_net_type,version_id,user_id);
		}else{
			ajax="该适用规则已经存在！";
		}
		logger.warn("[{}-{}] add({})",devicetype_id,version_id,ajax);
		return "ajax";
	}
	
	/**查询初始化*/
	public String execute()
	{
		vendorList = bio.querVentorList();
		return "init";
	}
	
	/**查询规则列表*/
	public String queryRuleList()
	{
		ruleList = bio.queryRuleList(curPage_splitPage,num_splitPage,vendorId, 
										deviceModelId,hardwareversion,softwareversion);
		maxPage_splitPage = bio.queryRuleCount(curPage_splitPage,num_splitPage,vendorId, 
												deviceModelId,hardwareversion,softwareversion);
		return "list";
	}
	
	/**编辑*/
	public String edit()
	{
		int num=0;
		if("1".equals(check_flag)){
			num = bio.checkTargetVersionRule(devicetype_id,user_net_type);
		}
		
		if(num==0){
			long  user_id=((UserRes) session.get("curUser")).getUser().getId();
			ajax=bio.updateSoftUpRule(rule_id,devicetype_id,dev_net_type,user_net_type,
											version_net_type,version_id,user_id);
		}else{
			ajax="该适用规则已经存在！";
		}
		
		logger.warn("[{}-{}] edit({})",devicetype_id,version_id,ajax);
		return "ajax";
	}
	
	/**删除*/
	public String delete()
	{
		ajax=bio.deleteSoftUpRule(rule_id);
		logger.warn("[{}] delete({})",rule_id,ajax);
		return "ajax";
	}
	
	/**查询设备型号*/
	public String getDeviceModelS()
	{
		ajax = bio.getDeviceModelS(vendorId);
		return "ajax";
	}
	
	/**查询设备硬件版本*/
	public String getHardwareversionS()
	{
		ajax = bio.getHardwareversionS(deviceModelId);
		return "ajax";
	}
	
	/** 查询设备软件版本*/
	public String getSoftwareversionS()
	{
		ajax = bio.getSoftwareversionS(deviceModelId,hardwareversion);
		return "ajax";
	}


	

	public List<Map<String, String>> getVendorList() {
		return vendorList;
	}

	public void setVendorList(List<Map<String, String>> vendorList) {
		this.vendorList = vendorList;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getDeviceModelId() {
		return deviceModelId;
	}

	public void setDeviceModelId(String deviceModelId) {
		this.deviceModelId = deviceModelId;
	}

	public String getSoftwareversion() {
		return softwareversion;
	}

	public void setSoftwareversion(String softwareversion) {
		this.softwareversion = softwareversion;
	}

	public String getHardwareversion() {
		return hardwareversion;
	}

	public void setHardwareversion(String hardwareversion) {
		this.hardwareversion = hardwareversion;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public AutoSoftUpRuleBIO getBio() {
		return bio;
	}

	public void setBio(AutoSoftUpRuleBIO bio) {
		this.bio = bio;
	}

	public String getDevicetype_id() {
		return devicetype_id;
	}

	public void setDevicetype_id(String devicetype_id) {
		this.devicetype_id = devicetype_id;
	}

	public String getVersion_id() {
		return version_id;
	}

	public void setVersion_id(String version_id) {
		this.version_id = version_id;
	}

	public String getDev_net_type() {
		return dev_net_type;
	}

	public void setDev_net_type(String dev_net_type) {
		this.dev_net_type = dev_net_type;
	}

	public String getUser_net_type() {
		return user_net_type;
	}

	public void setUser_net_type(String user_net_type) {
		this.user_net_type = user_net_type;
	}

	public String getVersion_net_type() {
		return version_net_type;
	}

	public void setVersion_net_type(String version_net_type) {
		this.version_net_type = version_net_type;
	}

	public List<Map> getRuleList() {
		return ruleList;
	}

	public void setRuleList(List<Map> ruleList) {
		this.ruleList = ruleList;
	}

	public String getRule_id() {
		return rule_id;
	}

	public void setRule_id(String rule_id) {
		this.rule_id = rule_id;
	}

	public Map getSession(){
		return session;
	}

	public void setSession(Map session){
		this.session = session;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getCheck_flag() {
		return check_flag;
	}

	public void setCheck_flag(String check_flag) {
		this.check_flag = check_flag;
	}
	
}
