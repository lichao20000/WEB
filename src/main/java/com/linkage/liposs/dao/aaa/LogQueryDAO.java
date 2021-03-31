package com.linkage.liposs.dao.aaa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.liposs.resource.VendorHelper;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;

/**
 * 设备与命令认证记录信息查询的DAO
 * 
 * @author duangr 2007-11-27
 * 
 */
public class LogQueryDAO
{
	private JdbcTemplate jt;
	// sql封装类
	private PrepareSQL pSQL;
	/**
	 * 查询属地
	 */
	private static String getCitySQL="select city_id,city_name from tab_city where parent_id = ? and 1=1 ? order by city_id";
	/**
	 * 查询采集机
	 */
	private static String getGatherSQL="select * from tab_process_desc where gather_id in (?)";
	/**
	 * 查询认证用户帐户表（所有用户信息）
	 */
	private static String gettacUserSQL="select * from tac_users where exp_date>? order by create_date";
	/**
	 * 查询认证设备列表
	 */
	private static String getdevSQL="select a.device_id,a.authen_prtc,a.tac_key,c.descr,b.loopback_ip,b.device_name,b.device_model_id as device_model from tac_device a, tab_gw_device b,tab_process_desc c where a.device_id=b.device_id and a.gather_id=c.gather_id and b.city_id in(?) ? order by a.device_id";
	/**
	 * 获取设备对应的所有帐号详细信息
	 */
	private static final String sqlDeviceUserInfo = "select a.user_name,a.eff_date,a.exp_date,a.creator,a.default_privilege from tac_users a,tac_device_user b where a.user_name=b.user_name and b.device_id=?";
	/**
	 * 删除帐号设备对应关系
	 */
	private static final String delDevUserInfo="delete tac_device_user where device_id=?";
	/**
	 * 删除设备
	 */
	private static final String delDevSQL="delete tac_device where device_id=?";
	/**
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao)
	{
		this.jt = new JdbcTemplate(dao);
	}
	/**
	 * 认证设备删除操作：先删除设备和用户对应关系，再删除设备
	 * @author Administrator
	 * @param device_id 设备ID
	 * @return result:0成功 1删除对应关系失败 2删除设备失败
	 */
	public int deleteDev(String device_id){
		int result=1;

		String sql = delDevUserInfo;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "delete from tac_device_user where device_id=?";
		}

		pSQL.setSQL(sql);
		pSQL.setString(1,device_id);
		jt.execute(pSQL.getSQL());
		result=2;

		String sql2 = delDevSQL;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql2 = "delete from tac_device where device_id=?";
		}
		pSQL.setSQL(sql2);
		pSQL.setString(1,device_id);
		jt.execute(pSQL.getSQL());
		result=0;
		return result;
	}
	/**
	 * 查询认证设备列表
	 * @author benyp(5260)
	 * @param:cid:用户自身的city_id,city_id:属地，lookbackip：设备IP device_name设备名称
	 * @param:
	 */
	public List<Map> getDevList(String cid,String lookbackip,String device_name){
		String city_ids = "select distinct city_id from tab_city where parent_id = '"+cid+"' or city_id='"+cid+"'";
		String param="";
		//用户输入设备IP
		if(lookbackip!=null){
			param+=" and b.loopback_ip like '%"+lookbackip+"%'";
		}
		//用户输入设备名称
		if(device_name!=null){
			param+=" and b.device_name like '%"+device_name+"%'";
		}
		pSQL.setSQL(getdevSQL);
		pSQL.setStringExt(1, city_ids,false);
		pSQL.setStringExt(2,param,false);
		List<Map> list=jt.queryForList(pSQL.getSQL());
		return list;
	}
	/**
	 * 取得当前用户的属地及下属属地
	 * @author benyp
	 * @param flg:true 包含自身属地 false 不包含自身属地
	 * @param city_id
	 * @return citylist
	 */
	public List<Map> getCityList(boolean flg,String city_id){
		String param="";
		if(flg){
			param=" or city_id='"+city_id+"'";
		}
		pSQL.setSQL(getCitySQL);
		pSQL.setString(1,city_id);
		pSQL.setStringExt(2,param,false);
		List<Map> list=jt.queryForList(pSQL.getSQL());
		return list;
	}
	/**
	 * 取得采集点
	 * ****************************************************
	 * benyp(5260) benyp@lianchuang.com 2008-04-28
	 * Bug:XJDX-XJ-BUG-20080402-XXF-001:将List范形,方便取出其中的值
	 * **************************************************
	 * @author benyp
	 * @param gatherList 获取用户管理采集机列表 
	 */
	public List<Map<String,String>> getGatherList(List<Map<String,String>> gatherList){
		String sql = getGatherSQL;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select gather_id, city_id, area_id, descr from tab_process_desc where gather_id in (?)";
		}
		pSQL.setSQL(sql);
		String gather_ids = StringUtils.weave(gatherList);
		pSQL.setStringExt(1, gather_ids,false);
		List<Map<String,String>> list=jt.queryForList(pSQL.getSQL());
		return list;
	}
	/**
	 * 取得设备厂商
	 * @author benyp
	 * @return list
	 */
	public List<Map> getVendorList(){
		Cursor cursor=VendorHelper.getResourceVendor();
		Map field=cursor.getNext();
		List<Map> list=new ArrayList();
		while(field!=null){
			list.add(field);
			field=cursor.getNext();
		}
		field=null;
		return list;
	}
	/**
	 * 获取设备型号
	 * @author benyp
	 * @param vendor_id
	 * @return list
	 */
	public List<Map> getModelList(String vendor_id){
		Cursor cursor=VendorHelper.getResourceVendorModel(vendor_id);
		Map field=cursor.getNext();
		List<Map> list=new ArrayList();
		while(field!=null){
			list.add(field);
			field=cursor.getNext();
		}
		field=null;
		return list;
	}
	/**
	 * 获取设备对应的所有帐号信息
	 * @param device_id
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getUserInfoByDev(String device_id){
		pSQL.setSQL(sqlDeviceUserInfo);
		pSQL.setString(1,device_id);
		List<Map> listInfo = jt.query(pSQL.getSQL(),new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				HashMap map=new HashMap();
				map.put("user_name", rs.getString("user_name"));
				map.put("default_privilege",rs.getString("default_privilege"));
				map.put("eff_date", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",rs.getLong("eff_date")));
				map.put("exp_date", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",rs.getLong("exp_date")));
				map.put("creator",rs.getString("creator"));
				return map;
			}}
			);
		return listInfo;
	}
	/**
	 * 获取所有用户账户信息
	 */
	public List<Map> gettacUserList(){
		DateTimeUtil dt=new DateTimeUtil();
		long now=dt.getLongTime();
		String sql = gettacUserSQL;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select user_name, user_password, default_privilege, exp_date, eff_date, creator from tac_users where exp_date>? order by create_date";
		}
		pSQL.setSQL(sql);
		pSQL.setLong(1,now);
		List<Map> list=jt.query(pSQL.getSQL(), new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				HashMap map=new HashMap();
				map.put("user_name", rs.getString("user_name"));
				map.put("user_password",rs.getString("user_password"));
				map.put("default_privilege",rs.getString("default_privilege"));
				map.put("create_date", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",rs.getLong("exp_date")));
				map.put("eff_date", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",rs.getLong("eff_date")));
				map.put("exp_date", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",rs.getLong("exp_date")));
				map.put("creator",rs.getString("creator"));
				return map;
			}
			
			
		});
		return list;
	}
	/**
	 * 查询设备认证信息
	 * 
	 * @param startDate
	 *            设备认证时间的起始值(long)
	 * @param endDate
	 *            设备认证时间的结束值(long)
	 * @param type
	 *            查询类型(device/account)
	 * @param input
	 *            查询的输入数据
	 * @return
	 * @throws Exception
	 */
	public List<Map> getTacAuthenLog(String startDate, String endDate,
			String type, String input) throws Exception
	{
		if ("account".equals(type) && input != null)
		{
			input = " and a.user_name like '%" + input.trim() + "%'";
		} else if ("device".equals(type) && input != null)
		{
			input = " and b.loopback_ip like '%" + input.trim() + "%'";
		} else
		{
			input = "";
		}
		String sql = "select a.device_id,a.user_name,a.server_ip,a.auth_result,a.auth_time,"
				+ "b.loopback_ip,b.device_name,c.authen_prtc"
				+ " from tac_authen_log a,tab_gw_device b,tac_device c"
				+ " where a.device_id=b.device_id and a.device_id=c.device_id"
				+ input
				+ getDateSQL(startDate, endDate, "a.auth_time")
				+ " order by a.auth_time desc";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1)
					throws java.sql.SQLException
			{
				Map m = new HashMap();
				m.put("device_id", rs.getString("device_id"));
				m.put("user_name", rs.getString("user_name"));
				m.put("server_ip", rs.getString("server_ip"));
				m
						.put("auth_result", rs.getBigDecimal("auth_result")
								.toString());// 0 成功 1 失败
				m.put("auth_time", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", Long
						.parseLong(rs.getBigDecimal("auth_time").toString())));
				m.put("loopback_ip", rs.getString("loopback_ip"));
				m.put("device_name", rs.getString("device_name"));
				m
						.put("authen_prtc", rs.getBigDecimal("authen_prtc")
								.toString());// 1 tacacs 2 radius
				return m;
			}
		});
	}

	/**
	 * 查询命令认证信息
	 * 
	 * @param startDate
	 *            设备认证时间的起始值(long)
	 * @param endDate
	 *            设备认证时间的结束值(long)
	 * @param type
	 *            查询类型(device/account)
	 * @param input
	 *            查询的输入数据
	 * @return
	 * @throws Exception
	 */
	public List<Map> getTacAuthorLog(String startDate, String endDate,
			String type, String input) throws Exception
	{
		if ("account".equals(type) && input != null)
		{
			input = " and a.user_name like '%" + input.trim() + "%'";
		} else if ("device".equals(type) && input != null)
		{
			input = " and b.loopback_ip like '%" + input.trim() + "%'";
		} else
		{
			input = "";
		}
		String sql = "select a.device_id,a.user_name,a.server_ip,a.author_cmd,a.priv_lvl,a.begin_time,a.elapsed_time,"
				+ "b.loopback_ip,b.device_name"
				+ " from tac_author_log a,tab_gw_device b"
				+ " where a.device_id=b.device_id and a.author_cmd!='0'"
				+ input
				+ getDateSQL(startDate, endDate, "a.begin_time")
				+ " order by a.begin_time,b.loopback_ip,a.priv_lvl asc";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1)
					throws java.sql.SQLException
			{
				Map m = new HashMap();
				m.put("device_id", rs.getString("device_id"));
				m.put("user_name", rs.getString("user_name"));
				m.put("server_ip", rs.getString("server_ip"));
				m.put("author_cmd", rs.getString("author_cmd"));
				m.put("priv_lvl", rs.getBigDecimal("priv_lvl").toString());// 1-16直接展示
				m.put("begin_time", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", Long
						.parseLong(rs.getBigDecimal("begin_time").toString())));
				m.put("elapsed_time", rs.getBigDecimal("elapsed_time")
						.toString());// 命令总耗时,单位：秒
				m.put("loopback_ip", rs.getString("loopback_ip"));
				m.put("device_name", rs.getString("device_name"));
				return m;
			}
		});
	}

	/**
	 * 通过传入参数返回已构造好的关于时间操作的SQL语句
	 * 
	 * @param startDate
	 *            时间的起始值(long)
	 * @param endDate
	 *            时间的结束值(long)
	 * @param name
	 *            数据库中时间那个字段对应的名称
	 * @return
	 */
	private String getDateSQL(String startDate, String endDate, String name)
	{
		String date = "";
		long startDatelong = 0;
		long endDatelong = 0;
		if (startDate != null && "".equals(startDate.trim()) == false)
		{
			startDatelong = Long.parseLong(startDate) / 1000;
		}
		if (endDate != null && "".equals(endDate.trim()) == false)
		{
			endDatelong = Long.parseLong(endDate) / 1000;
		}
		if ((startDatelong > endDatelong && startDatelong != 0 && endDatelong != 0)
				|| (startDatelong == 0 && endDatelong == 0))
		{
			date = "";
		} else
		{
			if (startDatelong != 0 && endDatelong != 0)
			{
				date = " and " + name + ">=" + startDatelong + " and " + name
						+ "<=" + (endDatelong + Long.parseLong("86400"));
			} else if (startDatelong != 0 && endDatelong == 0)
			{
				date = " and " + name + ">=" + startDatelong;
			} else
			// if (startlongDate == 0 && endlongDate != 0)
			{
				date = " and " + name + "<="
						+ (endDatelong + Long.parseLong("86400"));
			}
		}
		return date;
	}

	public void setPSQL(PrepareSQL psql)
	{
		pSQL = psql;
	}
}
