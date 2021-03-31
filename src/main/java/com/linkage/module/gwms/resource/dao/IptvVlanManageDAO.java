
package com.linkage.module.gwms.resource.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings("unchecked")
public class IptvVlanManageDAO extends SuperDAO
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(IptvVlanManageDAO.class);

	/**
	 * 查询所有本地网的VLAN值
	 *
	 * @author wangsenbo
	 * @date Jan 11, 2010
	 * @return List
	 */
	public List getCityVlan()
	{
		logger.debug("getCityVlan()");
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select city_id,max_vlanid,min_vlanid,bas_ip,updatetime,id ");
		}else{
			psql.append("select * ");
		}
		psql.append("from tab_iptv_vlan order by city_id");
		List list = jt.queryForList(psql.getSQL());
		return list;
	}

	/**
	 * 通过cityId查询VLAN值
	 *
	 * @author wangsenbo
	 * @date Jan 11, 2010
	 * @return List
	 */
	public List getCityVlan(String cid)
	{
		logger.debug("getCityVlan()");
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select city_id,max_vlanid,min_vlanid,bas_ip,updatetime,id ");
		}else{
			psql.append("select * ");
		}
		psql.append("from tab_iptv_vlan where city_id=? ");
		psql.setString(1, cid);
		List list = jt.queryForList(psql.getSQL());
		return list;
	}

	/**
	 * 增加一个本地网的VLAN值
	 *
	 * @author wangsenbo
	 * @date Jan 11, 2010
	 * @return int
	 */
	public int addVlan(long accoid, String cityId, String minVlanid, String maxVlanid,
			String basIp, long updatetime)
	{
		logger.debug("addVlan({},{},{},{},{},{},{})", new Object[] { accoid, cityId, minVlanid,
				maxVlanid, basIp,updatetime });
		String sql = "insert into tab_iptv_vlan(city_id,max_vlanid,min_vlanid,bas_ip,acc_oid,updatetime,id) values(?,?,?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, cityId);
		psql.setInt(2, StringUtil.getIntegerValue(maxVlanid));
		psql.setInt(3, StringUtil.getIntegerValue(minVlanid));
		if(StringUtil.IsEmpty(basIp)){
			basIp = null;
		}
		psql.setString(4, basIp);
		psql.setLong(5, StringUtil.getLongValue(accoid));
		psql.setLong(6, StringUtil.getLongValue(updatetime));
		long id = Math.round(Math.random() * 10000000000L);
		psql.setLong(7, StringUtil.getLongValue(id));
		return jt.update(psql.getSQL());
	}

	/**
	 * 修改一个本地网的VLAN值
	 *
	 * @author wangsenbo
	 * @date Jan 11, 2010
	 * @return int
	 */
	public int updateVlan(long accoid, String cityId, String minVlanid, String maxVlanid,
			String basIp, long updatetime, String id)
	{
		logger.debug("addVlan({},{},{},{},{},{},{})", new Object[] { accoid, cityId, minVlanid,
				maxVlanid, basIp,updatetime ,id});
		String sql = "update tab_iptv_vlan set max_vlanid=?,min_vlanid=?,bas_ip=?,acc_oid=?,updatetime=? where id=?";
		PrepareSQL psql = new PrepareSQL(sql);		
		psql.setInt(1, StringUtil.getIntegerValue(maxVlanid));
		psql.setInt(2, StringUtil.getIntegerValue(minVlanid));
		if(StringUtil.IsEmpty(basIp)){
			basIp = null;
		}
		psql.setString(3, basIp);
		psql.setLong(4, StringUtil.getLongValue(accoid));
		psql.setLong(5, StringUtil.getLongValue(updatetime));
		psql.setLong(6, StringUtil.getLongValue(id));
		return jt.update(psql.getSQL());
	}

	/**
	 * 删除
	 *
	 * @author wangsenbo
	 * @date Jan 11, 2010
	 * @return int
	 */
	public int delVlan(String id)
	{
		logger.debug("delVlan({})",id);
		String sql = "delete tab_iptv_vlan where id=?";
		PrepareSQL psql = new PrepareSQL(sql);		
		psql.setLong(1, StringUtil.getLongValue(id));
		return jt.update(psql.getSQL());
	}
}
