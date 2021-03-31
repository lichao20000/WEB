package com.linkage.module.gtms.stb.resource.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 *
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-1-5
 * @category com.linkage.module.gtms.stb.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class servPlatformDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(servPlatformDAO.class);

	/**
	 * 查询基本信息
	 * @param platformname
	 * @return
	 */
	public List<Map<String,String>> querylist(String platformname)
	{
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		StringBuffer sql = new StringBuffer();
		sql.append("select platform_id,remark,platform_name from stb_serv_platform where 1=1");
		if(!StringUtil.IsEmpty(platformname))
		{
			sql.append(" and platform_name='"+platformname+"'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		list= jt.queryForList(psql.getSQL());
		return list;
	}

	/**
	 * 添加数据
	 * @param platformname
	 * @param remark
	 * @param operator
	 * @return
	 */
	public int addservPlatform(String platformid,String platformname,String remark,String operator)
	{
		long nowTime = new Date().getTime()/1000;
		StringBuffer sql=new StringBuffer();
		sql.append("insert into stb_serv_platform(platform_id,platform_name");
		if(!StringUtil.IsEmpty(remark))
		{
			sql.append(" ,remark");
		}
		sql.append(" ,operator,add_time,update_time)values(");
		sql.append(Integer.valueOf(platformid)+",'"+platformname+"'");
		if(!StringUtil.IsEmpty(remark))
		{
			sql.append(" ,'"+remark+"'");
		}
		sql.append(" ,'"+operator+"','"+nowTime+"','"+nowTime+"')");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.update(psql.getSQL());
	}
	/**
	 * 修改数据
	 * @param platformid
	 * @param platformname
	 * @param remark
	 * @param operator
	 * @return
	 */
	public int updateservPlatform(String platformid ,String platformname,String remark,String operator)
	{
		long nowTime = new Date().getTime()/1000;
		StringBuffer sql=new StringBuffer();
		sql.append("update stb_serv_platform set platform_name='"+platformname+"'");
		if(!StringUtil.IsEmpty(remark))
		{
			sql.append(" ,remark='"+remark+"'");
		}
		sql.append(" ,update_time='"+nowTime+"'");
		sql.append(" where platform_id='"+platformid+"'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.update(psql.getSQL());
	}
	/**
	 * 查询id是否存在
	 * @param platformname
	 * @return
	 */
	public List<Map<String,String>> queryplatformname(String platformid)
	{
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		StringBuffer sql = new StringBuffer();
		sql.append("select platform_id, platform_name, remark from stb_serv_platform where 1=1 and platform_id= '"+platformid+"'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		list= jt.queryForList(psql.getSQL());
		return list;
	}
	public List<Map<String,String>> queryplatformid(String platformid)
	{
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		StringBuffer sql = new StringBuffer();
		sql.append("select platform_id from stb_serv_platform where 1=1 and platform_id= '"+platformid+"'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		list= jt.queryForList(psql.getSQL());
		return list;
	}
	/**
	 * 删除信息
	 * @param platformid
	 * @return
	 */
	public int deleteservPlatform(String platformid)
	{
		PrepareSQL psql = new PrepareSQL("delete  from stb_serv_platform where platform_id='"+platformid+"'");
		return jt.update(psql.getSQL());
	}

}
