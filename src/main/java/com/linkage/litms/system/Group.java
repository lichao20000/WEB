/*
 * Created on 2004-3-22
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.linkage.litms.system;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

/**
 * @author yuht
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Group {
	public int _group_oid;
	public int _group_poid;
	public String _group_name;
	public String _group_desc;
    /**
     * 列举所有用户组
     */
    private String m_GroupsAll_SQL = "select * from tab_group order by group_oid";
	
	public Group(int oid,int poid,String name,String desc){
		_group_oid 		= oid;
		_group_poid		= poid;
		_group_name		= name;
		_group_desc		= desc;
	}
    
    public Group(){
    }
    /**
     * 获得所有用户组信息
     * @return Cursor
     */
    public Cursor getGroupsAll(){
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			m_GroupsAll_SQL = "select group_oid, group_name from tab_group order by group_oid";
		}
    	PrepareSQL psql = new PrepareSQL(m_GroupsAll_SQL);
        psql.getSQL();
        return DataSetBean.getCursor(m_GroupsAll_SQL);
    }
}
