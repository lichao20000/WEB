package com.linkage.module.gtms.system.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.util.StringUtil;

public class SuperRoleManager {
	private static Logger logger = LoggerFactory.getLogger(SuperRoleManager.class);
	
	private static PrepareSQL pSQL = null;
	
	/**
	 * 获取用户的属性结构
	 * 不需要做过滤
	 * @return String
	 */
	public String getSystemRootTreeItemXML(String creator,String relation_type) {
		
		logger.warn("getSystemRootTreeItemXML({})",creator);
        long current = System.currentTimeMillis();
        int createId = StringUtil.getIntegerValue(creator);
        String sql = "";
        String IterXML = "";
        if("0".equals(relation_type)){
        	sql ="select acc_oid,acc_loginname from tab_accounts  where creator =?";
        	
        }else{
        	sql ="select role_id,role_name from tab_role  where role_pid =?";
        }
        pSQL = new PrepareSQL(sql);
        pSQL.setInt(1, createId);
        pSQL.getSQL();
        Cursor cMap = DataSetBean.getCursor(sql.replaceFirst("\\?", creator));
       
        Map map = cMap.getNext();
        while (map != null) {
        	if("0".equals(relation_type)){
        		IterXML += "<item id='" + map.get("acc_oid") + "' text='"
                        + map.get("acc_loginname") + "'>";
                IterXML += getSystemRootTreeItemXML(String.valueOf(map.get("acc_oid")),relation_type);
                IterXML += "</item>";
        	}else{
        		IterXML += "<item id='" + map.get("role_id") + "' text='"
                        + map.get("role_name") + "'>";
                IterXML += getSystemRootTreeItemXML(String.valueOf(map.get("role_id")),relation_type);
                IterXML += "</item>";
        	}
            map = cMap.getNext();
        }
        /*这个是另外一种查询结果的组装方式
         * for (int k = 0; k < lModule.size(); k++) { module = (Map)
         * lModule.get(k); // m_logger.debug(module.get("module_name")); IterXML += "<item
         * id='" + module.get("module_id") + "' text='" +
         * module.get("module_name") + "'>"; IterXML +=
         * getXMLTreeItem(String.valueOf(module.get("module_id")),role_id);
         * IterXML += "</item>"; }
         */
        cMap = null;
        map = null;
        logger.warn("树形节点生成时间：" + (System.currentTimeMillis() - current) + "ms");
        logger.warn("生成的树形为===="+IterXML);
        return IterXML;
    }
}
