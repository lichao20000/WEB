package com.linkage.module.gwms.resource.dao;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-4-13
 * @category com.linkage.module.gwms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class KdPasswordModificationDAO extends SuperDAO
{
	/**
	 * 判断宽带账号是否存在
	 * @param loid
	 * @param username
	 * @param netPasswd
	 * @return
	 */
	public List<Map<String,String>>  getusername(String loid ,String username,String netPasswd)
	{
		StringBuffer sql=new StringBuffer();
		sql.append("select a.user_id,a.wan_type,b.device_id,c.oui,c.device_serialnumber ");
		sql.append("from hgwcust_serv_info a,tab_hgwcustomer b,tab_gw_device c ");
		sql.append("where a.user_id=b.user_id and b.device_id=c.device_id and a.serv_type_id=10");
		if(!StringUtil.IsEmpty(loid))
		{
			sql.append(" and b.username='"+loid+"'");
		}
		if(!StringUtil.IsEmpty(username))
		{
			sql.append(" and a.username='"+username+"'");
		}
		/*if(!StringUtil.IsEmpty(netPasswd))
		{
			sql.append(" and a.passwd='"+netPasswd+"'");
		}*/
		PrepareSQL pSQL=new PrepareSQL(sql.toString());
		return jt.queryForList(pSQL.getSQL());
	}
	/**
	 * 更改业务状态
	 */
	public int updateServOpenStatus(long userId) {
		String strSQL = "update hgwcust_serv_info set open_status=0 "
				+ " where serv_status=1 and open_status!=0 and user_id=? and serv_type_id=10";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setLong(1, userId);
		// 执行查询
		return DBOperation.executeUpdate(psql.getSQL());
	}
	/**
	 * 桥接模式直接修改密码
	 */
	public int updatepassword(long userId,String username,String netPasswd)
	{
		StringBuffer sql=new StringBuffer();
		sql.append(" update hgwcust_serv_info set passwd ='"+netPasswd+"' where user_id="+userId+" and username='"+username+"' and serv_type_id=10");
		PrepareSQL pSQL=new PrepareSQL(sql.toString());
		return jt.update(pSQL.getSQL());
	}
}
