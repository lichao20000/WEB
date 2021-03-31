package com.linkage.module.gtms.system.dao;

import java.util.List;
import java.util.Map;

public interface SuperRoleDAO {

	public boolean  addSuperRole(List treeList, long lMaxId, String auth_name,
			String auth_code, String auth_desc, String relation_type);

	public List getTreeAll(String relation_type);

	public boolean checkName(String auth_code);

	public List getAllRecord(String auth_name, String auth_code,
			String relation_type, String user_name,
			int curPage_splitPage, int num_splitPage);

	public int getAllCount(String auth_name, String auth_code,
			String relation_type, String user_name,
			int curPage_splitPage, int num_splitPage);

	public boolean deleteSuperRole(String auth_id,String relation_id);

	public Map preUpdateSuperRole(String auth_id);

	public boolean updateSuperRole(String auth_id, List<String> treeList,
			String auth_name, String auth_code, String auth_desc,
			String relation_type);

}
