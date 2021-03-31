package com.linkage.litms.tree;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.JdbcUtils;
import com.linkage.module.gwms.Global;

public class Module {
	private static Logger m_logger = LoggerFactory.getLogger(Module.class);
	
	private String module_id = null;

	private String module_name = null;

	private String module_code = null;

	private String module_url = null;

	private String module_pic = null;

	private String sequence = null;

	private String module_desc = null;
	/**分WEB时，报表子系统*/
	private final static String REPORT_CLUSTE_MODE = "3";
	/**分WEB时，开通子系统*/
	private final static String OPEN_CLUSTE_MODE = "2";
	/**
	 * 新增系统模块信息
	 */
	private String SAVE_MODULE_INFO = "insert into tab_module (module_id,module_name,module_code,module_url,sequence,module_desc,module_pic) values (?,?,?,?,?,?,?)";
    /**
     * 模块角色关联
     */
    private String SAVE_MODULE_ROLE = "insert into tab_tree_role (tree_id,role_id) values(?,?)";

	/**
	 * 更新系统模块信息
	 */
	private String UPDATE_MODULE_INFO = "update tab_module set module_name=?,module_code=?,module_url=?,sequence=?,module_desc=?,module_pic=? where module_id=?";

	/**
	 * 删除系统模块
	 */
	private String DELETE_MODULE_INFO = "delete from tab_module where module_id=?";
    /**
     * 删除系统模块角色
     */
    private String DELETE_MODULE_ROLE = "delete from tab_tree_role where tree_id=?";

	/**
	 * 模块列表
	 */
	private String MODULE_INFO_LIST = "select * from tab_module order by sequence";

	/**
	 * 获取指定模块信息
	 */
	private String MODULE_INFO_BY_MODULEID = "select * from tab_module where module_id=?";

	/**
	 * 获得角色权限范围内系统模块
	 */
	private String MODULE_INFO_LIST_BY_ROLEID = "select * from tab_module where module_id in(select tree_id from tab_tree_role where role_id=?) order by sequence";
	
	private String MODULE_INFO_LIST_BY_ROLEID4OPEN = "select * from tab_module where module_id not in (select item_id from tab_report_item where item_level=1) and module_id in(select tree_id from tab_tree_role where role_id=?) order by sequence";
	
	private String MODULE_INFO_LIST_BY_ROLEID4REPORT = "select * from tab_module where module_id in (select item_id from tab_report_item where item_level=1) and module_id in(select tree_id from tab_tree_role where role_id=?) order by sequence";

	/**
	 * 所有角色
	 */
	private String ALLROLES = "select role_id from tab_role";

	/**
	 * 定义对象与角色关联哈希(用来一次性载入所有角色模块数据)
	 */
	private static Map<String,Cursor> MODULE_BY_ROLE_MAP = null;

	private static PrepareSQL pSQL = new PrepareSQL();
	
	public Module() {
		pSQL = new PrepareSQL();
	}

	/**
	 * 获得模块与角色对应关系
	 */
	private void getModuleRoleMap() {
		if (MODULE_BY_ROLE_MAP == null || MODULE_BY_ROLE_MAP.isEmpty()) {
			m_logger.warn("create MODULE_BY_ROLE_MAP .");
			MODULE_BY_ROLE_MAP = new HashMap<String,Cursor>(8);
			String clusteMode = LipossGlobals.getLipossProperty("ClusterMode.mode");
			String temp_sql = null;
			String role_id = null;
			Cursor module_cursor = null;
			PrepareSQL psql = new PrepareSQL(ALLROLES);
	        psql.getSQL();
			Cursor cursor = DataSetBean.getCursor(ALLROLES);
			Map field = cursor.getNext();
			String strSQL = MODULE_INFO_LIST_BY_ROLEID;
			if(!StringUtil.IsEmpty(clusteMode)&&REPORT_CLUSTE_MODE.equals(clusteMode))
			{
				strSQL = MODULE_INFO_LIST_BY_ROLEID4REPORT;
			}
			while (field != null) {
				role_id = (String) field.get("role_id");
				temp_sql = strSQL.replaceFirst("\\?",
						role_id);
				psql = new PrepareSQL(temp_sql);
		        psql.getSQL();
				module_cursor = DataSetBean.getCursor(temp_sql);
				MODULE_BY_ROLE_MAP.put(role_id, module_cursor);
				m_logger.debug(role_id + "==>" + temp_sql);
				field = cursor.getNext(); 
			}
			
			//clear
			cursor = null;
			field = null;
		}
	}

	/**
	 * 新增系统模块信息
	 * 
	 * @param module
	 * @return boolean
	 */
	public boolean saveModuleInfo(Module module,long role_id) {
		String[][] param = new String[][] {
				{ module.getModule_id(), "String" },
				{ module.getModule_name(), "String" },
				{ module.getModule_code(), "String" },
				{ module.getModule_url(), "String" },
				{ module.getSequence(), "int" },
				{ module.getModule_desc(), "String" },
				{ module.getModule_pic(), "String" } };

       
		PrepareSQL psql = new PrepareSQL(SAVE_MODULE_INFO);
        psql.getSQL();
		boolean flag = (JdbcUtils.executeUpdate(SAVE_MODULE_INFO, param) > 0) ? true: false;
		param = new String[][]{
                {module.getModule_id(), "String"},
                {"" + role_id,"int"}
        };
		psql = new PrepareSQL(SAVE_MODULE_ROLE);
        psql.getSQL();
        flag = (JdbcUtils.executeUpdate(SAVE_MODULE_ROLE, param) > 0) ? true : false;
        
        return flag;
	}

	/**
	 * 更新系统模块信息
	 * 
	 * @param module
	 * @return boolean
	 */
	public boolean updateModuleInfo(Module module) {
		String[][] param = new String[][] {
				{ module.getModule_name(), "String" },
				{ module.getModule_code(), "String" },
				{ module.getModule_url(), "String" },
				{ module.getSequence(), "int" },
				{ module.getModule_desc(), "String" },
				{ module.getModule_pic(), "String" },
				{ module.getModule_id(), "String" } };
		PrepareSQL psql = new PrepareSQL(UPDATE_MODULE_INFO);
        psql.getSQL();
		return (JdbcUtils.executeUpdate(UPDATE_MODULE_INFO, param) > 0) ? true : false;
	}

	/**
	 * 删除系统模块
	 * 
	 * @param module_id
	 * @return boolean
	 */
	public boolean deleteModuleInfo(String module_id) {
		PrepareSQL psql = new PrepareSQL(DELETE_MODULE_INFO);
	    psql.getSQL();
        boolean flag = (JdbcUtils.update(DELETE_MODULE_INFO, module_id) > 0) ? true : false; 
        psql = new PrepareSQL(DELETE_MODULE_ROLE);
        psql.getSQL();
        JdbcUtils.update(DELETE_MODULE_ROLE, module_id);
        
        return flag;
	}

	/**
	 * 模块列表
	 * 
	 * @return List
	 */
	public List getModuleInfoList() {
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			MODULE_INFO_LIST = "select module_id, module_name, module_url, sequence from tab_module order by sequence";
		}
		PrepareSQL psql = new PrepareSQL(MODULE_INFO_LIST);
	    psql.getSQL();
		return JdbcUtils.query(MODULE_INFO_LIST);
	}

	/**
	 * 获得系统模块信息与系统模块编码之间的映射
	 * 
	 * @return Map
	 */
	public Map getModuleInfoMap() {
		List list = getModuleInfoList();

		Map map = new HashMap();

		for (int h = 0; h < list.size(); h++) {
			map.put(((Map) list.get(h)).get("module_id"), list.get(h));
		}

		// clear
		list.clear();
		list = null;

		return map;
	}

	/**
	 * 获取指定模块信息
	 * 
	 * @param module_id
	 * @return Module
	 */
	public Module getModuleInfo(String module_id) {
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			MODULE_INFO_BY_MODULEID = "select module_id, module_name, module_code, module_url, sequence, module_desc, module_pic " +
					" from tab_module where module_id=?";
		}
		pSQL.setSQL(MODULE_INFO_BY_MODULEID);
		pSQL.setString(1, module_id);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map mModule = cursor.getNext();
		
		if(mModule != null){
			Module module = new Module();
			module.setModule_id(String.valueOf(mModule.get("module_id")));
			module.setModule_name(String.valueOf(mModule.get("module_name")));
			module.setModule_code(String.valueOf(mModule.get("module_code")));
			module.setModule_url(String.valueOf(mModule.get("module_url")));
			module.setSequence(String.valueOf(mModule.get("sequence")));
			module.setModule_desc(String.valueOf(mModule.get("module_desc")));
			module.setModule_pic(String.valueOf(mModule.get("module_pic")));

			// clear
			mModule.clear();
			mModule = null;
			
			return module;
		}

		return null;
	}

	/**
	 * 获得角色权限范围内系统模块
	 * 
	 * @param role_id
	 * @return Cursor
	 */
	public Cursor getModuleInfoListByRoleId(String role_id) {
		this.getModuleRoleMap();

		return (Cursor) MODULE_BY_ROLE_MAP.get(role_id);
	}
	
	/**
     * 定义对象与角色关联哈希(用来一次性载入所有角色模块数据
     * 清除角色对应菜单的MAP对象。
	 */
	public synchronized static void clearModuleRoleMap()
	{
		m_logger.warn("权限分配数据开始同步至其他负载web");	
		if (MODULE_BY_ROLE_MAP != null){
			MODULE_BY_ROLE_MAP.clear();
			MODULE_BY_ROLE_MAP = null;
		}	
		
		NodeData.clearRole_Nodes_Map();
		// 现网系统页面权限分配后，不需要重启现网WEB
		String doSyn = LipossGlobals.getLipossProperty("after_setPermission.switch");
		if (!StringUtil.IsEmpty(doSyn) && "1".equals(doSyn))
		{
			HttpClient httpclient = new HttpClient();
			GetMethod getmethod = null;
			String url = "";
			int sendStatus = 0;
			String https = LipossGlobals.getLipossProperty("after_setPermission.web_http");
			for (String http : https.split(","))
			{
				url = http + "/droitTree/droitRole_refresh.jsp";
				getmethod = new GetMethod(url);
				try
				{
					sendStatus = httpclient.executeMethod(getmethod);
					m_logger.warn(url+ "------权限分配后web同步请求结果状态(200即成功)" + "------" + sendStatus);
				}
				catch (HttpException e)
				{
					m_logger.error(e.getMessage());
				}
				catch (IOException e)
				{
					m_logger.error(e.getMessage());
				}
				finally
				{
					// 释放
					getmethod.releaseConnection();
				}
			}
		}
	}

	public synchronized static void clearModuleRoleMap_refresh()
	{
		m_logger.warn("执行了角色权限刷新操作");
		if (MODULE_BY_ROLE_MAP != null){
			MODULE_BY_ROLE_MAP.clear();
			MODULE_BY_ROLE_MAP = null;
		}
		NodeData.clearRole_Nodes_Map();
	}
	

	public String getModule_code() {
		return module_code;
	}

	public void setModule_code(String module_code) {
		this.module_code = module_code;
	}

	public String getModule_desc() {
		return module_desc;
	}

	public void setModule_desc(String module_desc) {
		this.module_desc = module_desc;
	}

	public String getModule_id() {
		return module_id;
	}

	public void setModule_id(String module_id) {
		this.module_id = module_id;
	}

	public String getModule_name() {
		return module_name;
	}

	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}

	public String getModule_url() {
		return module_url;
	}

	public void setModule_url(String module_url) {
		this.module_url = module_url;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getModule_pic() {
		return module_pic;
	}

	public void setModule_pic(String module_pic) {
		this.module_pic = module_pic;
	}

}
