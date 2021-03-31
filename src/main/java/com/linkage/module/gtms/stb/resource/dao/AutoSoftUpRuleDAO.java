package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 湖南联通自动软件升级规则管理
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class AutoSoftUpRuleDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(AutoSoftUpRuleDAO.class);
	private static final String QUERYVENDOR="select vendor_id,vendor_name,vendor_add "
												+ "from stb_tab_vendor order by vendor_id ";

	/**查询厂商信息*/
	public List<Map<String,String>> queryVendor()
	{
		PrepareSQL psql = new PrepareSQL(QUERYVENDOR);
		return jt.queryForList(psql.getSQL());
	}

	/**根据厂商ID查询设备型号信息*/
	public List<Map<String,String>> getDeviceModelList(String vendorId)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select distinct(a.device_model_id),a.device_model ");
		psql.append("from stb_gw_device_model a,stb_tab_devicetype_info b ");
		psql.append("where a.device_model_id=b.device_model_id and a.vendor_id=? ");
		psql.append("order by a.device_model_id ");
		psql.setString(1,vendorId);

		return jt.queryForList(psql.getSQL());
	}

	/**根据型号ID查询设备硬件版本*/
	public List<Map<String,String>> getHardwareversionS(String deviceModelId)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select distinct(b.hardwareversion) as hardwareversion ");
		psql.append("from stb_gw_device_model a,stb_tab_devicetype_info b ");
		psql.append("where a.device_model_id=b.device_model_id ");
		psql.append("and a.device_model_id=? ");
		psql.append("order by b.hardwareversion ");
		psql.setString(1,deviceModelId);

		return jt.queryForList(psql.getSQL());
	}

	/**根据型号ID、硬件版本查询设备软件版本*/
	public List<Map<String,String>> getSoftwareversionS(String deviceModelId,String hardwareversion)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select distinct(b.softwareversion) as softwareversion ");
		psql.append("from stb_gw_device_model a,stb_tab_devicetype_info b ");
		psql.append("where a.device_model_id=b.device_model_id ");
		psql.append("and a.device_model_id=? and b.hardwareversion=? ");
		psql.append("order by b.softwareversion ");
		psql.setString(1,deviceModelId);
		psql.setString(2,hardwareversion);
		return jt.queryForList(psql.getSQL());
	}

	/**查询版本数据*/
	public Map getDevType(String vendorId, String deviceModelId,
			String hardwareversion, String softwareversion)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select devicetype_id,epg_version,net_type ");
		psql.append("from stb_tab_devicetype_info ");
		psql.append("where vendor_id=? and device_model_id=? ");
		psql.append("and hardwareversion=? and softwareversion=? ");
		psql.setString(1,vendorId);
		psql.setString(2,deviceModelId);
		psql.setString(3,hardwareversion);
		psql.setString(4,softwareversion);
		return getMap(psql.getSQL());
	}

	/**查询目标版本数据*/
	public List<Map<String,String>> getTargetSoftVersion(String deviceModelId)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.id,b.version_name,b.version_path ");
		psql.append("from stb_soft_version_path_model a,stb_soft_version_path b ");
		psql.append("where a.id=b.id and a.device_model_id=? ");
		psql.append("order by a.id ");
		psql.setString(1,deviceModelId);
		return jt.queryForList(psql.getSQL());
	}

	/**查询目标版本详细数据*/
	public Map getTargetSoftVersionDetail(long id)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select version_desc,version_path,file_size,md5,net_type,epg_version ");
		psql.append("from stb_soft_version_path ");
		psql.append("where id=? ");
		psql.setLong(1,id);
		return getMap(psql.getSQL());
	}

	/**查询指定规则数量*/
	public int getTargetVersionRuleNum(String devicetype_id,String user_net_type)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) num ");
		psql.append("from stb_auto_upgrade_rule ");
		psql.append("where devicetype_id=? and user_net_type=? ");
		psql.setInt(1,StringUtil.getIntegerValue(devicetype_id));
		psql.setString(2,user_net_type);
		return jt.queryForInt(psql.getSQL());
	}

	/**新增规则*/
	public String addSoftUpRule(String devicetype_id,String dev_net_type,String user_net_type,
			String version_net_type,String version_id,long opertor)
	{
		long time=System.currentTimeMillis()/1000L;
		logger.warn("addSoftUpRule({},{},{})",time,devicetype_id,version_id);
		PrepareSQL psql = new PrepareSQL();
		psql.append("insert into stb_auto_upgrade_rule(rule_id,devicetype_id,dev_net_type,");
		psql.append("user_net_type,version_net_type,version_id,status,opertor,add_time) ");
		psql.append("values(?,?,?,?,?,?,?,?,?) ");
		psql.setLong(1,time);
		psql.setLong(2,StringUtil.getLongValue(devicetype_id));
		psql.setString(3,dev_net_type);
		psql.setString(4,user_net_type);
		psql.setString(5,version_net_type);
		psql.setLong(6,StringUtil.getLongValue(version_id));
		psql.setInt(7,1);
		psql.setString(8,StringUtil.getStringValue(opertor));
		psql.setLong(9,time);

		try{
			jt.execute(psql.getSQL());
		}catch(Exception e){
			logger.error("addSoftUpRule(),err:{}",e);
			e.printStackTrace();
			return "规则定制失败！";
		}
		return "规则定制成功！";
	}

	/**分页查询*/
	public List<Map> queryRuleList(int curPage_splitPage,int num_splitPage,String vendorId,
			String deviceModelId,String hardwareversion,String softwareversion)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.vendor_name,a.vendor_add,b.device_model,c.hardwareversion,");
		psql.append("c.softwareversion,d.rule_id,d.user_net_type,e.version_name ");
		psql.append(pinSql(vendorId,deviceModelId,hardwareversion,softwareversion));
		psql.append("order by a.vendor_id,b.device_model_id,c.hardwareversion,c.softwareversion ");

		return querySP(psql.getSQL(),(curPage_splitPage - 1) * num_splitPage + 1,num_splitPage,
				new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("rule_id", rs.getString("rule_id"));
				map.put("user_net_type","private_net".equals(rs.getString("user_net_type"))?"专网":"公网");
				String vendorName=rs.getString("vendor_add");
				if(StringUtil.IsEmpty(vendorName)){
					vendorName=rs.getString("vendor_name");
				}
				map.put("vendor_add", vendorName);
				map.put("device_model", rs.getString("device_model"));
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("hardwareversion", rs.getString("hardwareversion"));
				map.put("targetVersion",rs.getString("version_name"));

				String user_net_type=rs.getString("user_net_type");
				if("public_net".equals(user_net_type)){
					user_net_type="公网";
				}else if("private_net".equals(user_net_type)){
					user_net_type="专网";
				}else{
					user_net_type="未知";
				}
				map.put("user_net_type", user_net_type);

				return map;
			}
		});
	}

	/** 查询总数*/
	public int queryRuleCount(String vendorId,String deviceModelId,
			String hardwareversion,String softwareversion)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) num ");
		psql.append(pinSql(vendorId,deviceModelId,hardwareversion,softwareversion));
		return jt.queryForInt(psql.getSQL());
	}

	/**获取编辑规则的数据*/
	public Map queryRuleInfo(String rule_id)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.vendor_id,a.device_model_id,a.hardwareversion,a.softwareversion,");
		psql.append("b.devicetype_id,b.dev_net_type,b.version_net_type,b.user_net_type,b.version_id ");
		psql.append("from stb_tab_devicetype_info a,stb_auto_upgrade_rule b ");
		psql.append("where a.devicetype_id=b.devicetype_id and b.rule_id=? ");
		psql.setLong(1,StringUtil.getLongValue(rule_id));
		return getMap(psql.getSQL());
	}

	/**更新规则*/
	public String updateSoftUpRule(String rule_id,String devicetype_id,String dev_net_type,
			String user_net_type,String version_net_type,String version_id,long opertor)
	{
		logger.warn("updateSoftUpRule({})",rule_id);
		PrepareSQL psql = new PrepareSQL();
		psql.append("update stb_auto_upgrade_rule set ");
		psql.append("devicetype_id=?,dev_net_type=?,user_net_type=?,");
		psql.append("version_net_type=?,version_id=?,status=2,opertor=?,add_time=? ");
		psql.append("where rule_id=? ");
		psql.setLong(1,StringUtil.getLongValue(devicetype_id));
		psql.setString(2,dev_net_type);
		psql.setString(3,user_net_type);
		psql.setString(4,version_net_type);
		psql.setLong(5,StringUtil.getLongValue(version_id));
		psql.setString(6,StringUtil.getStringValue(opertor));
		psql.setLong(7,System.currentTimeMillis()/1000L);
		psql.setLong(8,StringUtil.getLongValue(rule_id));

		try{
			jt.execute(psql.getSQL());
		}catch(Exception e){
			logger.error("updateSoftUpRule({}),err:{}",rule_id,e);
			e.printStackTrace();
			return "规则更新失败！";
		}
		return "规则更新成功！";
	}

	/** 删除规则*/
	public String deleteSoftUpRule(String rule_id)
	{
		logger.warn("deleteSoftUpRule({})",rule_id);
		PrepareSQL psql = new PrepareSQL();
		psql.append("update stb_auto_upgrade_rule set status=3 where rule_id=? ");
		psql.setLong(1,StringUtil.getLongValue(rule_id));

		try{
			jt.execute(psql.getSQL());
		}catch(Exception e){
			logger.error("deleteSoftUpRule({}),err:{}",rule_id,e);
			e.printStackTrace();
			return "规则删除失败！";
		}
		return "规则删除成功！";
	}

	/**拼SQL*/
	private String pinSql(String vendorId,String deviceModelId,
			String hardwareversion,String softwareversion)
	{
		StringBuffer sb=new StringBuffer();// TODO wait (more table related)
		sb.append("from stb_tab_vendor a,stb_gw_device_model b,");
		sb.append("stb_tab_devicetype_info c,stb_auto_upgrade_rule d,stb_soft_version_path e ");
		sb.append("where a.vendor_id=b.vendor_id and b.device_model_id=c.device_model_id ");
		sb.append("and c.devicetype_id=d.devicetype_id and d.version_id=e.id ");
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sb.append("and a.vendor_id='"+vendorId+"' ");
		}
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sb.append("and b.device_model_id='"+deviceModelId+"' ");
		}
		if(!StringUtil.IsEmpty(hardwareversion) && !"-1".equals(hardwareversion)){
			sb.append("and c.hardwareversion='"+hardwareversion+"' ");
		}
		if(!StringUtil.IsEmpty(softwareversion) && !"-1".equals(softwareversion)){
			sb.append("and c.softwareversion='"+softwareversion+"' ");
		}

		sb.append("and d.status in(1,2) ");
		return sb.toString();
	}

	/**获取map数据*/
	private Map getMap(String sql)
	{
		Map map=null;
		try{
			map=jt.queryForMap(sql);
		}catch(Exception e){
			logger.error("getMap({}),err:{}",sql,e);
			e.printStackTrace();
		}
		return map;
	}

}
