package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-2-15
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class SoundGraphsQueryDAO extends SuperDAO
{
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(SoundGraphsQueryDAO.class);
	private Map<String, String> status_map = new HashMap<String, String>();
	//private Map<String, String> status_map = new HashMap<String, String>();
	public SoundGraphsQueryDAO()
	{
		status_map.put("0", "等待执行");
		status_map.put("1", "预读PVC");
		status_map.put("2", "预读绑定端");
		status_map.put("3", "预读无线");
		status_map.put("4", "业务下发");
		status_map.put("100", "执行完成");
	}
	/**
	 * 根据宽带账号、IPTV账号查询用户
	 * @param usernameType
	 * @param username
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> UserId(String usernameType, String username,String gw_type)
	{
		logger.debug("UserId({}, {},{})", usernameType, username,gw_type);
		StringBuffer sql = new StringBuffer();
		if(Global.GW_TYPE_BBMS.equals(gw_type))
		{
			
			sql.append("select user_id from egwcust_serv_info where ");
			if(!StringUtil.IsEmpty(username))
			{
				sql.append(" username='"
						+ username + "'");
			}
			// 根据宽带账号：
			if ("2".equals(usernameType))
			{
				sql.append(" and  serv_type_id=10");
			}
			// 根据IPTV账号
			if ("3".equals(usernameType))
			{
				sql.append(" and  serv_type_id=11");
			}
		}
		else
		{
			sql.append("select user_id from hgwcust_serv_info where ");
			if(!StringUtil.IsEmpty(username))
			{
				sql.append(" username='"
						+ username + "'");
			}
			// 根据宽带账号：
			if ("2".equals(usernameType))
			{
				sql.append(" and  serv_type_id=10");
			}
			// 根据IPTV账号
			if ("3".equals(usernameType))
			{
				sql.append(" and  serv_type_id=11");
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		return list;
	}
	/**
	 * 根据loid查询用户
	 */
	public List<Map> Loid(String usernameType,String username,String gw_type)
	{
		logger.debug("Loid({}, {},{})", usernameType, username,gw_type);
		StringBuffer sql=new StringBuffer();
		if (Global.GW_TYPE_BBMS.equals(gw_type))
		{
			sql.append("select user_id from tab_egwcustomer where username='")
					.append(username).append("'");
		}
		else
		{
			// 当用户类型为家庭或者家庭政企融合时，用户信息都保持在该表中
			sql.append("select user_id from tab_hgwcustomer where username='")
					.append(username).append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	/**
	 * 根据VOIP认证号码、VOIP电话号码查询用户
	 * @param usernameType
	 * @param username
	 * @param gwtype
	 * @return
	 */
	public List<Map> UserIdVoip(String usernameType,String username,String gw_type)
	{
		logger.debug("UserIdVoip({}, {},{})", usernameType, username,gw_type);

		StringBuffer sql = new StringBuffer();
		if (Global.GW_TYPE_BBMS.equals(gw_type))
		{
			sql.append("select user_id from tab_egw_voip_serv_param  where");
			// 4" VoIP认证号码
			if ("4".equals(usernameType))
			{
				sql.append(" voip_username='"
						+ username + "'");
			}
			// "5" VoIP电话号码
			if ("5".equals(usernameType))
			{
				sql.append(" voip_phone ='"
						+ username + "'");
			}
		}
		else
		{
			sql.append("select user_id from tab_voip_serv_param  where");
			// 4" VoIP认证号码
			if ("4".equals(usernameType))
			{
				sql.append(" voip_username='"
						+ username + "'");
			}
			// "5" VoIP电话号码
			if ("5".equals(usernameType))
			{
				sql.append(" voip_phone ='"
						+ username + "'");
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		return list;
	}
	/**
	 * 根据user_id查询语音业务信息
	 * @param usernameType
	 * @param username
	 * @param user_id
	 * @return
	 */
	public List<Map> DeviceId(String usernameType,String username,int user_id)
	{
		logger.debug("DeviceId({}, {},{})", usernameType, username,user_id);
		StringBuffer sql=new StringBuffer();
		sql.append("select  a.device_id,b.open_status,c.digit_map,a.username  from  tab_hgwcustomer a, hgwcust_serv_info b,tab_voip_serv_param c where  a.user_id=b.user_id and a.user_id=c.user_id  and b.serv_type_id =14  ");
		
		if(user_id>0)
		{
			sql.append(" and a.user_id ="+user_id);
		}
		if ("4".equals(usernameType))
		{
			sql.append(" and voip_username='"
					+ username + "'");
		}
		// "5" VoIP电话号码
		if ("5".equals(usernameType))
		{
			sql.append(" and voip_phone ='"
					+ username + "'");
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list=new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		
		return list;
	}
	/**
	 * 查询策略记录表
	 * @param startOpenDate
	 * @param endOpenDate
	 * @param usernameType
	 * @param openstatus
	 * @param username
	 * @param deviceid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> QueryList(String username1,String startOpenDate,String endOpenDate,String usernameType,String openstatus,String username,String deviceid,int curPage_splitPage, int num_splitPage)
	{
		logger.debug("QueryList({}, {},{},{},{},{})", startOpenDate, endOpenDate,usernameType,openstatus,username,deviceid);
		StringBuffer sql=new StringBuffer();
		
		sql.append(" select  end_time, start_time,status,result_id,result_desc from gw_serv_strategy_serv_log where service_id=1401 ");
		if(false==StringUtil.IsEmpty(startOpenDate))
		{
			sql.append("  and start_time>"+startOpenDate);
		}
		if(false==StringUtil.IsEmpty(endOpenDate))
		{
			sql.append(" and start_time<"+endOpenDate);
		}
		if(!StringUtil.IsEmpty(deviceid))
		{
			sql.append("  and device_id='"
						+ deviceid + "'");
		}
		if(!StringUtil.IsEmpty(username1))
		{
			sql.append("  and username='"
					+ username1 + "'");
		}
		if("1".equals(openstatus))
		{
			sql.append(" and result_id=1");
		}
		if("10000".equals(openstatus))
		{
			if(LipossGlobals.isOracle()==true)
			{
				sql.append(" and result_id is null ");
			}
			else
			{
				sql.append(" and result_id=null ");
			}
		}
		if("-1".equals(openstatus))
		{
			if(LipossGlobals.isOracle()==true)
			{
				sql.append(" and result_id is not null  and result_id!=1");
			}
			else
			{
				sql.append(" and result_id!=null  and result_id!=1");
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		List<Map> list= querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage+1,
				num_splitPage, new RowMapper()
				{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("time",transDate( rs.getString("end_time")));
				map.put("start_time",transDate( rs.getString("start_time")));
				map.put("status",  status_map.get(rs.getString("status")));
				map.put("result_id", rs.getString("result_id"));
				map.put("result_desc", Global.G_Fault_Map
						.get(StringUtil.getIntegerValue(rs
								.getString("result_id"))).getFaultReason());
				map.put("business_name", "虚拟网语音数图配置策略");
				return map;
			}
		});
		return list;
	}
	/**
	 * 更多信息查询
	 * @param seconds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> number(String digit_map)
	{
		logger.debug("number({})",digit_map );

		StringBuffer sql=new StringBuffer();
		if(!StringUtil.IsEmpty(digit_map))
		{
		sql.append("select remark,digit_map_value from tab_digit_map where digit_map_code= '"
						+ digit_map + "'");
		}
	
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list= jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("remark", rs.getString("remark"));
				map.put("digit_map_value", rs.getString("digit_map_value"));
				return map;
			}
		});
		return list;
	}
	private static final String transDate(Object seconds)
	{
		if (seconds != null)
		{
			try
			{
				DateTimeUtil dt = new DateTimeUtil(
						Long.parseLong(seconds.toString()) * 1000);
				return dt.getLongDate();
			}
			catch (NumberFormatException e)
			{
				logger.error(e.getMessage(), e);
			}
			catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
		}
		return "";
	}
	/**
	 * 分页
	 * @param startOpenDate
	 * @param endOpenDate
	 * @param usernameType
	 * @param openstatus
	 * @param username
	 * @param deviceid
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int getquerypaging(String username1,String startOpenDate,String endOpenDate,String usernameType,String openstatus,String username,String deviceid,int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql=new StringBuffer();
		//DatabaseMetaData md = this.jdbcTemplate.getDataSource().getConnection().getMetaData(); 
		sql.append(" select  count(*) from gw_serv_strategy_serv_log where service_id=1401 ");
		if(false==StringUtil.IsEmpty(startOpenDate))
		{
			sql.append("  and start_time>"+startOpenDate);
		}
		if(false==StringUtil.IsEmpty(endOpenDate))
		{
			sql.append(" and start_time<"+endOpenDate);
		}
		if(!StringUtil.IsEmpty(deviceid))
		{
			sql.append("  and device_id='"
						+ deviceid + "'");
		}
		if(!StringUtil.IsEmpty(username1))
		{
			sql.append("  and username='"
						+ username1 + "'");
		}
		if("1".equals(openstatus))
		{
			sql.append(" and result_id=1");
		}
		if("10000".equals(openstatus))
		{
			if(LipossGlobals.isOracle()==true)
		{
			sql.append(" and result_id is null  ");
		}
		else
		{
			sql.append(" and result_id=null ");
		}
		}
		if("-1".equals(openstatus))
		{

			if(LipossGlobals.isOracle()==true)
			{
				sql.append(" and result_id is not null  and result_id!=1");
			}
			else
			{
				sql.append(" and result_id!=null  and result_id!=1");
			}
			
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
}
