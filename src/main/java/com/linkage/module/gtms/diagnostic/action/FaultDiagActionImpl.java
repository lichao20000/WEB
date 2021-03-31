package com.linkage.module.gtms.diagnostic.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.diagnostic.serv.DiagTemlateServ;
import com.linkage.module.gtms.diagnostic.serv.TemplateUnitManageServ;

public class FaultDiagActionImpl implements FaultDiagAction, SessionAware{
	
	private static Logger logger = LoggerFactory
	   .getLogger(FaultDiagActionImpl.class);
	private Map session;
	private String diagId;             
	private String templateName  ;
	private String templateParam ;
	private String accOid        ;
	private String templateTime  ;
	
	private String ajax ; 
	/**模版记录*/
	private List<Map<String,Object>> list;
	/**模版单元记录*/
	private List<Map<String,Object>> unitList;
	/**修改的记录*/
	private Map<String,Object> map ;
	private DiagTemlateServ  bio;
	private TemplateUnitManageServ serv;
	
	
	/**
	 * 查询所有的记录
	 */
	public String init() {
		logger.warn("init()");
		list = bio.getRecords();
		return "init";
	}
	
	/**
	 * 根据模板id查询模板单元列表，组成String
	 * 
	 */
	public String getUintById(){
		logger.warn("tempUint"+diagId);
		
		ajax = bio.getUintListByTempId(diagId);
		//ajax = "1;http://202.102.39.141:9090/itms/gtms/diagnostic/Voice_diag.jsp,2;http://202.102.39.141:9090/itms/gtms/diagnostic/VOIP_diag.jsp";
		//TODO
		return "ajax";
	}
	
	public String getDiagId() {
		return diagId;
	}
	public void setDiagId(String diagId) {
		this.diagId = diagId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getTemplateParam() {
		return templateParam;
	}
	public void setTemplateParam(String templateParam) {
		this.templateParam = templateParam;
	}
	public String getAccOid() {
		return accOid;
	}
	public void setAccOid(String accOid) {
		this.accOid = accOid;
	}
	public String getTemplateTime() {
		return templateTime;
	}
	public void setTemplateTime(String templateTime) {
		this.templateTime = templateTime;
	}
	public String getAjax() {
		return ajax;
	}
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
	public DiagTemlateServ getBio() {
		return bio;
	}
	public void setBio(DiagTemlateServ bio) {
		this.bio = bio;
	}
	public Map getSession() {
		return session;
	}
	public void setSession(Map session) {
		this.session = session;
	}
	public List<Map<String, Object>> getList() {
		return list;
	}
	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}
	
	public TemplateUnitManageServ getServ()
	{
		return serv;
	}
	
	public void setServ(TemplateUnitManageServ serv)
	{
		this.serv = serv;
	}
	
}
