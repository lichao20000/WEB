package com.linkage.module.gtms.system.serv;

import java.util.List;
import java.util.Map;

public interface SuperRoleServ {

	public List getTreeAll(String relation_type);

	public String addSuperRole(List treeList, String auth_name, String auth_code,
			String auth_desc, String relation_type);

	public String checkName(String auth_code);

	public List getAllRecords(String auth_name, String auth_code,
			String relation_type, String user_name,
			int curPage_splitPage, int num_splitPage);

	public int getAllCount(String auth_name, String auth_code,
			String relation_type,  String user_name,
			int curPage_splitPage, int num_splitPage);

	public String deleteSuperRole(String auth_id,String relation_id);

	public Map preUpdateSuperRole(String auth_id);

	public String updateSuperRole(String auth_id, List<String> treeList,
			String auth_name, String auth_code, String auth_desc,
			String relation_type);

}
