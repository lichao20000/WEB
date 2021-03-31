package com.linkage.module.gtms.diagnostic.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.diagnostic.serv.TemplateUnitManageServ;

public class TemplateUnitManageActionImpl implements TemplateUnitManageAction {
	private static Logger logger = LoggerFactory
	   .getLogger(TemplateUnitManageActionImpl.class);

	private String unitId;
	/**单元名称*/
	private String templateUnitName;
	/**单元路径 */
	private String templateUnitURL;
	private String unitTime;
	
	private String ajax ; 
	private List<Map<String,Object>> unitList;
	private Map<String,Object>  map;
	
	private TemplateUnitManageServ bio;
	/**
	 * 把所有信息列举出来
	 * @return
	 */
	public String init(){
		logger.debug("init()");
		unitList = bio.getAllRecords();
		return "init";
	}
	/**
	 * 增加模版单元 
	 * @return
	 */
	public String add(){
		logger.debug("add()");
		try {
			templateUnitName = URLDecoder.decode(templateUnitName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.warn("转码失败");
		}
		ajax = bio.add(templateUnitName,templateUnitURL)+"";
		return "ajax";
	}
	
	public String  preUpdate(){
		logger.debug("preUpdate()");
		logger.warn("Preupdate*****unitId="+unitId);
		map = bio.getpreUpdateRecord(unitId);
		return "preUpdate";
	}
	/**
	 * 修改模版单元 
	 * @return
	 */
	public String update(){
		logger.debug("update()");
		try {
			templateUnitName = URLDecoder.decode(templateUnitName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.warn("转码失败");
		}
		ajax = bio.update(unitId,templateUnitName,templateUnitURL)+"";
		return "ajax";
	}

	/**
	 * 删除模版单元 
	 * @return
	 */
	public String delete(){
		logger.debug("delete()");
		ajax = bio.delete(unitId)+"";
		return "ajax";
	}

	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	
	public String getAjax() {
		return ajax;
	}
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	public String getTemplateUnitName() {
		return templateUnitName;
	}

	public void setTemplateUnitName(String templateUnitName) {
		this.templateUnitName = templateUnitName;
	}


	public String getTemplateUnitURL() {
		return templateUnitURL;
	}


	public void setTemplateUnitURL(String templateUnitURL) {
		this.templateUnitURL = templateUnitURL;
	}


	public String getUnitTime() {
		return unitTime;
	}

	public void setUnitTime(String unitTime) {
		this.unitTime = unitTime;
	}


	public TemplateUnitManageServ getBio() {
		return bio;
	}


	public void setBio(TemplateUnitManageServ bio) {
		this.bio = bio;
	}


	public List<Map<String,Object>> getUnitList() {
		return unitList;
	}


	public void setUnitList(List<Map<String,Object>> unitList) {
		this.unitList = unitList;
	}
	
	
}
