package com.linkage.module.gtms.diagnostic.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.diagnostic.serv.DiagTemlateServ;

public class DiagTemlateActionImpl implements DiagTemlateAction, SessionAware{
	
	private static Logger logger = LoggerFactory
	   .getLogger(DiagTemlateActionImpl.class);
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
	
	
	/**
	 * 查询所有的记录
	 */
	public String init() {
		logger.warn("init()");
		list = bio.getRecords();
		return "init";
	}
	
	public String preAdd(){
		logger.warn("preAdd()");
		unitList = bio.getTemplateUnits();
		return "preAdd";
	}
	public String add() {
		logger.warn("add()");
		UserRes curUser = (UserRes) session.get("curUser");
		accOid  =   StringUtil.getStringValue(curUser.getUser().getId());
		
		try {
			templateName = URLDecoder.decode(templateName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.warn("转码失败");
		}
		ajax = bio.add(accOid,templateName,templateParam)+"";
		return "ajax" ;
	}
	public String delete() {
		logger.warn("delete()");
		ajax = bio.delete(diagId)+"";
		return "ajax" ;
	}
	
	public String preUpdate() {
		logger.debug("preUpdate()");
		unitList = bio.getTemplateUnits();
		map = bio.getDiagTemplate(diagId);
		return "preUpdate";
	}
	public String update() {
		logger.debug("update()");
		try {
			templateName = URLDecoder.decode(templateName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.warn("转码失败");
		}
		ajax = bio.update(diagId,templateName,templateParam)+"";
		return "ajax" ;
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

	public List<Map<String, Object>> getUnitList() {
		return unitList;
	}

	public void setUnitList(List<Map<String, Object>> unitList) {
		this.unitList = unitList;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
}
