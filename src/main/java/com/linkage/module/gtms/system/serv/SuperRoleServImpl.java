package com.linkage.module.gtms.system.serv;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.system.dao.SuperRoleDAO;
import com.linkage.system.utils.database.DataSetBean;

public class SuperRoleServImpl implements SuperRoleServ {
	private static Logger logger = LoggerFactory.getLogger(SuperRoleServImpl.class);
	private SuperRoleDAO dao ; 
	@Override
	public List getTreeAll(String relation_type) {
		return dao.getTreeAll(relation_type);
	}

	@Override
	public String addSuperRole(List treeList, String auth_name,
			String auth_code, String auth_desc, String relation_type) {
		logger.warn("addSuperRole");
		long lMaxId = DataSetBean.getMaxId("t_sys_auth","auth_id");
		boolean flag  = dao.addSuperRole(treeList,lMaxId,auth_name,auth_code,auth_desc,relation_type);
		if(flag){
			return "1";
		}
		return "0";
	}
	/**
	 * 1:已存在，0：不存在，可以使用
	 */
	@Override
	public String checkName(String auth_code) {
		boolean flag = dao.checkName(auth_code);
		if(flag){
			return "1";
		}
		return "0";
	}
	/**
	 * 获取所有记录
	 */
	public List getAllRecords(String auth_name, String auth_code,
			String relation_type,  String user_name,
			int curPage_splitPage, int num_splitPage) {
		return dao.getAllRecord(auth_name,auth_code,relation_type,user_name,
				curPage_splitPage,num_splitPage);
	}
	/**
	 * 最大页数
	 */
	public int getAllCount(String auth_name, String auth_code,
			String relation_type,  String user_name,
			int curPage_splitPage, int num_splitPage) {
		// TODO Auto-generated method stub
		return dao.getAllCount(auth_name,auth_code,relation_type,user_name,
				curPage_splitPage, num_splitPage);
	}
	public void setDao(SuperRoleDAO dao) {
		this.dao = dao;
	}

	@Override
	public String deleteSuperRole(String auth_id,String relation_id) {
		boolean flag = dao.deleteSuperRole(auth_id,relation_id);
		if(flag){
			return "1";
		}
		return "0";
	}

	@Override
	public Map preUpdateSuperRole(String auth_id) {
		return dao.preUpdateSuperRole(auth_id);
	}

	@Override
	public String updateSuperRole(String auth_id, List<String> treeList,
			String auth_name, String auth_code, String auth_desc,
			String relation_type) {
		logger.warn("updateSuperRole({})");
		boolean flag  = dao.updateSuperRole(auth_id,treeList,auth_name,auth_code,auth_desc,relation_type);
		if(flag){
			return "1";
		}
		return "0";
	}

}