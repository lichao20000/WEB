package com.linkage.module.gtms.system.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.gtms.system.serv.SuperRoleServ;

public class SuperRoleActionImpl extends splitPageAction implements SessionAware,SuperRoleAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(SuperRoleActionImpl.class);
	private Map session ;
	private String auth_id;
	private String auth_name ; 
	private String auth_code;
	private String auth_desc;
	private String relation_type = "-1";
	private String tree_id_query;
	private String relation_id;
	private String user_name;
	
	private String ajax;
	private Map srMap;
	private List dataList;
	
	private SuperRoleServ bio;
	
	public String getAllRecords(){
		logger.warn("getAllRecords({})");
		dataList = bio.getAllRecords(auth_name,auth_code,relation_type,user_name,curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getAllCount(auth_name,auth_code,relation_type,user_name,curPage_splitPage, num_splitPage);
		return "list";
	}
	
	/**
	 * 增加超级权限
	 * @return
	 */
	public String addSuperRole(){
		logger.warn("add({})",auth_name);
		String[] tree_id = tree_id_query.split(",");
		logger.warn("tree_id_query======"+tree_id_query);
		//List TreeIdList = bio.getTreeAll(relation_type);
		List<String> TreeList = new ArrayList<String>();
//		//判断被选中的id
//			for(int k=0;k<tree_id.length;k++){
//				if(TreeIdList != null && TreeIdList.contains(tree_id[k])){
//					TreeList.add(tree_id[k]);
//				}
//			}
		for(int i=0;i<tree_id.length;i++){
			TreeList.add(tree_id[i]);
		}
		logger.warn("TreeList===="+TreeList.toString());
		ajax = bio.addSuperRole(TreeList,auth_name,auth_code,auth_desc,relation_type);
		return "list";
	}
	/**
	 * 权限简码是否唯一
	 * @return
	 */
	public String checkName(){
		logger.warn("checkName({})",auth_code);
		ajax = bio.checkName(auth_code);
		return "ajax";
	}
	/**
	 * 根据auth_id 删除 权限
	 * @return
	 */
	public String deleteSuperRole(){
		logger.warn("String deleteSuperRole({},{})",auth_id,relation_id);
		ajax = bio.deleteSuperRole(auth_id,relation_id);
		return "ajax";
	}
	/**
	 * 预修改
	 * @return
	 */
	public String preUpdateSuperRole(){
		logger.warn("String preUpdateSuperRole({})",auth_id);
		srMap = bio.preUpdateSuperRole(auth_id);
		return "update";
	}
	public String updateSuperRole(){
		logger.warn("String preUpdateSuperRole({})",auth_id);
		List<String> TreeList = new ArrayList<String>();
		String[] tree_id = tree_id_query.split(",");
		for(int i=0;i<tree_id.length;i++){
			TreeList.add(tree_id[i]);
		}
		ajax = bio.updateSuperRole(auth_id,TreeList,auth_name,auth_code,auth_desc,relation_type);
		return "ajax";
	}
	public String getAuth_id() {
		return auth_id;
	}
	public void setAuth_id(String auth_id) {
		this.auth_id = auth_id;
	}
	public String getAuth_name() {
		return auth_name;
	}
	public void setAuth_name(String auth_name) {
		this.auth_name = auth_name;
	}
	public String getAuth_code() {
		return auth_code;
	}
	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}
	public String getAuth_desc() {
		return auth_desc;
	}
	public void setAuth_desc(String auth_desc) {
		this.auth_desc = auth_desc;
	}
	public String getRelation_type() {
		return relation_type;
	}
	public void setRelation_type(String relation_type) {
		this.relation_type = relation_type;
	}
	public String getTree_id_query() {
		return tree_id_query;
	}
	public void setTree_id_query(String tree_id_query) {
		this.tree_id_query = tree_id_query;
	}
	public String getAjax() {
		return ajax;
	}
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
	public void setBio(SuperRoleServ bio) {
		this.bio = bio;
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public List getDataList() {
		return dataList;
	}

	public void setDataList(List dataList) {
		this.dataList = dataList;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public Map getSrMap() {
		return srMap;
	}

	public void setSrMap(Map srMap) {
		this.srMap = srMap;
	}

	public String getRelation_id() {
		return relation_id;
	}

	public void setRelation_id(String relation_id) {
		this.relation_id = relation_id;
	}
	
}
