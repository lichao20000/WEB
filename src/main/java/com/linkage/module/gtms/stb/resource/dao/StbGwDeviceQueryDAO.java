package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.liposs.common.util.TopoDAO;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 *
 * @author wangyan10(Ailk NO.76091)
 * @version 1.0
 * @since 2018-1-2
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class StbGwDeviceQueryDAO extends SuperDAO
{

	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(StbGwDeviceQueryDAO.class);
	private int queryCount;

	private static String writeLogSql = "insert into tab_oper_log("
			+ "acc_oid,acc_login_ip,operationlog_type,operation_time,operation_name"
			+ ",operation_object,operation_content,operation_device"
			+ ",operation_result,result_id) values(?,?,?,?,?,?,?,?,?,?)";


	public List<Map<String, String>> getStbDeviceList(
			int curPage_splitPage, int num_splitPage, String vendorId,
			String deviceModelId, String deviceSn, String deviceMac, String servAccount, String cityId, String citynext, String startTime, String endTime) {
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select a.oui, a.cpe_mac, a.device_serialnumber, a.complete_time, a.device_id, a.city_id, " +
				" a.vendor_id, a.device_model_id, a.devicetype_id," +
				" b.vendor_name,b.vendor_add,c.device_model,d.hardwareversion,d.softwareversion,e.serv_account as Eserv_account" +
				" from stb_tab_gw_device a left join stb_tab_vendor b on a.vendor_id = b.vendor_id " +
				" left join stb_gw_device_model c on a.device_model_id = c.device_model_id " +
				" left join stb_tab_devicetype_info d on a.devicetype_id = d.devicetype_id " +
				" left join stb_tab_customer e on a.customer_id = e.customer_id " +
				" where  1=1  "  );
		if ((!StringUtil.IsEmpty(vendorId)) && (!"-1".equals(vendorId)))
		{
			sql.append(" and a.vendor_id='").append(vendorId).append("'");
		}
		if ((!StringUtil.IsEmpty(deviceModelId)) && (!"-1".equals(deviceModelId)))
		{
			sql.append(" and a.device_model_id='").append(deviceModelId).append("'");
		}
		if ((!StringUtil.IsEmpty(deviceSn)) && (!"".equals(deviceSn)))
		{
			String sub_deviceSn = deviceSn.substring(deviceSn.length() - 6, deviceSn.length());
			sql.append(" and a.device_serialnumber like '%").append(deviceSn)
			.append("' and a.dev_sub_sn ='").append(sub_deviceSn).append("'");
		}
		if ((!StringUtil.IsEmpty(deviceMac)) && (!"".equals(deviceMac)))
		{
			sql.append(" and a.cpe_mac ='").append(deviceMac).append("'");
		}
		if ((!StringUtil.IsEmpty(startTime)) && (!"".equals(startTime)))
		{
			sql.append(" and a.complete_time >").append(startTime).append("");
		}
		if ((!StringUtil.IsEmpty(endTime)) && (!"".equals(endTime)))
		{
			sql.append(" and a.complete_time <").append(endTime).append("");
		}
		if ((!StringUtil.IsEmpty(servAccount)) && (!"".equals(servAccount)))
		{
			sql.append(" and e.serv_account like '%").append(servAccount).append("%'");
		}
		if (!"-1".equals(citynext) && !"00".equals(citynext))
		{
			List citynextList = CityDAO.getAllNextCityIdsByCityPid(citynext);
			sql.append(" and a.city_id in (").append(StringUtils.weave(citynextList)).append(")");
			citynextList = null;
		}else if (!"-1".equals(cityId) && !"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		sql.append(" order by a.complete_time desc");

		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						String city_name = "";
						map.put("vendorName", rs.getString("vendor_name"));
						map.put("vendorAdd", rs.getString("vendor_add"));
						map.put("deviceModel", rs.getString("device_model"));
						map.put("hardwareversion", rs.getString("hardwareversion"));
						map.put("softwareversion", rs.getString("softwareversion"));
						map.put("oui", rs.getString("oui"));
						map.put("cpeMac", rs.getString("cpe_mac"));
						map.put("deviceSerialnumber", rs.getString("device_serialnumber"));
						map.put("completeTime", transDate(rs.getString("complete_time")));
						map.put("deviceId", rs.getString("device_id"));
						map.put("cityId", rs.getString("city_id"));
						map.put("servAccount", rs.getString("Eserv_account"));
						String cityid = StringUtil.getStringValue(rs.getString("city_id"));
						if (!StringUtil.IsEmpty(cityid)){
							city_name=StringUtil.getStringValue(CityDAO.getCityIdCityNameMap().get(cityid));
						}
						map.put("cityName", city_name);
						map.put("vendorId", rs.getString("vendor_id"));
						map.put("deviceModelId", rs.getString("device_model_id"));
						map.put("devicetypeId", rs.getString("devicetype_id"));
						return map;
					}
				});
		return list;
	}

	/**
	 * 获取总数并分页
	 */
	public int countStbDeviceList(int curPage_splitPage, int num_splitPage,
			String vendorId, String deviceModelId, String deviceSn, String deviceMac, String servAccount,
			String cityId, String citynext, String startTime, String endTime)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from stb_tab_gw_device a left join stb_tab_customer e on a.customer_id = e.customer_id where 1=1"  );
		if ((!StringUtil.IsEmpty(vendorId)) && (!"-1".equals(vendorId)))
		{
			sql.append(" and a.vendor_id='").append(vendorId).append("'");
		}
		if ((!StringUtil.IsEmpty(deviceModelId)) && (!"-1".equals(deviceModelId)))
		{
			sql.append(" and a.device_model_id='").append(deviceModelId).append("'");
		}
		if ((!StringUtil.IsEmpty(deviceSn)) && (!"".equals(deviceSn)))
		{
			String sub_deviceSn = deviceSn.substring(deviceSn.length() - 6, deviceSn.length());
			sql.append(" and a.device_serialnumber like '%").append(deviceSn)
			.append("' and a.dev_sub_sn ='").append(sub_deviceSn).append("'");
		}
		if ((!StringUtil.IsEmpty(deviceMac)) && (!"".equals(deviceMac)))
		{
			sql.append(" and a.cpe_mac ='").append(deviceMac).append("'");
		}
		if ((!StringUtil.IsEmpty(startTime)) && (!"".equals(startTime)))
		{
			sql.append(" and a.complete_time >").append(startTime).append("");
		}
		if ((!StringUtil.IsEmpty(endTime)) && (!"".equals(endTime)))
		{
			sql.append(" and a.complete_time <").append(endTime).append("");
		}
		if ((!StringUtil.IsEmpty(servAccount)) && (!"".equals(servAccount)))
		{
			sql.append(" and e.serv_account like '%").append(servAccount).append("%'");
		}
		if (!"-1".equals(citynext) && !"00".equals(citynext))
		{
			List citynextList = CityDAO.getAllNextCityIdsByCityPid(citynext);
			sql.append(" and a.city_id in (").append(StringUtils.weave(citynextList)).append(")");
			citynextList = null;
		}else if (!"-1".equals(cityId) && !"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}

		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		queryCount = total;
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * 新增设备
	 */
	public int addGwDevice(String deviceSn, String oui, String deviceMac,
			String vendorId, String deviceModelId, String deviceTypeId,
			String cityId, String completeTime, String citynext)
	{
		// 首先要校验当前设备序列号和mac地址是否存在
		int ier = addCheck(deviceSn, deviceMac);
		if (ier > 0) {
			return 2;
		}
		if (!"-1".equals(citynext)){
			cityId = citynext;
		}
		PrepareSQL pSql = new PrepareSQL();
		pSql.append("insert into stb_tab_gw_device(device_id,oui,device_serialnumber,device_status,");
		pSql.append("gather_id,cr_port,x_com_username,x_com_passwd,x_com_passwd_old,");
		pSql.append("dev_sub_sn,cpe_mac,inform_stat,vendor_id,device_model_id,devicetype_id,city_id,complete_time) ");
		pSql.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");

		pSql.setString(1,TopoDAO.GetUnusedDeviceSerial(1)+"");
		pSql.setString(2,oui);
		pSql.setString(3,deviceSn);
		pSql.setInt(4,1);
		pSql.setLong(5,1);
		pSql.setInt(6,1);
		pSql.setString(7,"1");
		pSql.setString(8,"1");
		pSql.setString(9,"1");
		pSql.setString(10,deviceSn.substring(deviceSn.length()-6, deviceSn.length()));
		pSql.setString(11,deviceMac);
		pSql.setInt(12,0);
		pSql.setString(13,vendorId);
		pSql.setString(14,deviceModelId);
		pSql.setInt(15,StringUtil.getIntegerValue(deviceTypeId));
		pSql.setString(16,cityId);
		pSql.setLong(17,StringUtil.getLongValue(completeTime));

		return DBOperation.executeUpdate(pSql.getSQL());
	}

	public int addGwDevice_hn(long id,String ip,String deviceSn, String oui, String deviceMac,
			String vendorId, String deviceModelId, String deviceTypeId,
			String cityId, String completeTime, String citynext)
	{
		// 首先要校验当前设备序列号和mac地址是否存在
		int ier = addCheck(deviceSn, deviceMac);
		if (ier > 0) {
			return 2;
		}
		if (!"-1".equals(citynext)){
			cityId = citynext;
		}

		String device_id=TopoDAO.GetUnusedDeviceSerial(1)+"";
		PrepareSQL pSql = new PrepareSQL();
		pSql.append("insert into stb_tab_gw_device(device_id,oui,device_serialnumber,device_status,");
		pSql.append("gather_id,cr_port,x_com_username,x_com_passwd,x_com_passwd_old,");
		pSql.append("dev_sub_sn,cpe_mac,inform_stat,vendor_id,device_model_id,devicetype_id,city_id,complete_time) ");
		pSql.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");

		pSql.setString(1,device_id);
		pSql.setString(2,oui);
		pSql.setString(3,deviceSn);
		pSql.setInt(4,1);
		pSql.setLong(5,1);
		pSql.setInt(6,1);
		pSql.setString(7,"1");
		pSql.setString(8,"1");
		pSql.setString(9,"1");
		pSql.setString(10,deviceSn.substring(deviceSn.length()-6, deviceSn.length()));
		pSql.setString(11,deviceMac);
		pSql.setInt(12,0);
		pSql.setString(13,vendorId);
		pSql.setString(14,deviceModelId);
		pSql.setInt(15,StringUtil.getIntegerValue(deviceTypeId));
		pSql.setString(16,cityId);
		pSql.setLong(17,StringUtil.getLongValue(completeTime));

		int i=DBOperation.executeUpdate(pSql.getSQL());

		pSql =null;
		pSql = new PrepareSQL();
		pSql.setSQL(writeLogSql);
		pSql.setLong(1,id);
		pSql.setString(2,ip);
		pSql.setInt(3,1);
		pSql.setLong(4,System.currentTimeMillis()/1000L);
		pSql.setString(5,"5");
		pSql.setString(6,"WEB");

		StringBuffer sb=new StringBuffer();
		sb.append("新增设备["+device_id+"]信息：[");
		sb.append("设备序列号："+deviceSn);
		sb.append("，oui："+oui);
		sb.append("，厂商："+vendorId);
		sb.append("，设备型号："+deviceModelId);
		sb.append("，版本型号："+deviceTypeId);
		sb.append("，cpe_mac："+deviceMac);
		sb.append("，属地："+cityId);
		sb.append("]");

		pSql.setString(7,sb.toString());
		pSql.setString(8,"Web");
		pSql.setString(9,i>=1?"成功":"失败");
		pSql.setInt(10,1);
		DBOperation.executeUpdate(pSql.getSQL());

		return i;
	}

	/**
	 * 新增前需要校验设备序列号和mac地址
	 */
	public int addCheck(String deviceSn, String deviceMac)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) from stb_tab_gw_device a where "  );
		psql.append(" (a.device_serialnumber = '"+deviceSn);
		psql.append("' and a.dev_sub_sn ='"+deviceSn.substring(deviceSn.length()-6, deviceSn.length())+"') ");
		psql.append(" or a.cpe_mac ='"+deviceMac+"'");

		return jt.queryForInt(psql.getSQL());
	}

	/**
	 * 删除设备
	 */
	public int deleteDevice(String deviceId, String deviceMac)
	{
		// 校验mac地址在用户表是否存在
		logger.debug("StbGwDeviceQueryDAO——》deleteDevice "+deviceId);
		int ier = deleteCheck(deviceMac);
		if (ier > 0) {
			return 2;
		}
		List<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = new PrepareSQL(
				"delete from stb_tab_gw_device where device_id ='"+deviceId+"'");
		sqlList.add(psql.getSQL());

		String[] sqlArray = sqlList.toArray(new String[0]);

		return jt.batchUpdate(sqlArray).length;
	}

	public int deleteDevice_hn(long id,String ip,String deviceId, String deviceMac)
	{
		// 校验mac地址在用户表是否存在
		logger.debug("StbGwDeviceQueryDAO——》deleteDevice "+deviceId);
		int ier = deleteCheck(deviceMac);
		if (ier > 0) {
			return 2;
		}

		List<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_serialnumber,cpe_mac from stb_tab_gw_device ");
		psql.append("where device_id=? ");
		psql.setString(1,deviceId);
		Map<String,String> map=DBOperation.getRecord(psql.getSQL());

		psql =null;
		psql = new PrepareSQL();
		psql.append("delete from stb_tab_gw_device where device_id=? ");
		psql.setString(1,deviceId);
		sqlList.add(psql.getSQL());
		String[] sqlArray = sqlList.toArray(new String[0]);

		int i=jt.batchUpdate(sqlArray).length;

		if(map!=null && !map.isEmpty())
		{
			psql =null;
			psql = new PrepareSQL();
			psql.setSQL(writeLogSql);
			psql.setLong(1,id);
			psql.setString(2,ip);
			psql.setInt(3,1);
			psql.setLong(4,System.currentTimeMillis()/1000L);
			psql.setString(5,"5");
			psql.setString(6,"WEB");

			StringBuffer sb=new StringBuffer();
			sb.append("删除设备["+deviceId+"]信息：[");
			sb.append("设备序列号："+map.get("device_serialnumber"));
			sb.append("，cpe_mac："+map.get("cpe_mac"));
			sb.append("]");

			psql.setString(7,sb.toString());
			psql.setString(8,"Web");
			psql.setString(9,i>=1?"成功":"失败");
			psql.setInt(10,1);
			DBOperation.executeUpdate(psql.getSQL());
		}

		return i;
	}

	/**
	 * 删除设备时校验mac地址在用户表是否存在
	 */
	public int deleteCheck(String deviceMac)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from stb_tab_customer a where "  );
		sql.append(" a.cpe_mac ='").append(deviceMac).append("'");

		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForInt(psql.getSQL());
	}

	public int modifyDevice(String deviceId, String deviceSn, String oui,
			String deviceMac, String vendorId, String deviceModelId,
			String deviceTypeId, String cityId, String citynext)
	{
		// 首先要校验当前设备序列号和mac地址是否存在
		int ier = modifyCheck(deviceId, deviceSn, deviceMac);
		if (ier > 0) {
			return 2;
		}
		if (!"-1".equals(citynext)){
			cityId = citynext;
		}

		PrepareSQL pSql = new PrepareSQL();
		pSql.append("update stb_tab_gw_device set device_serialnumber =?,oui=?,cpe_mac=?,vendor_id=?,");
		pSql.append("device_model_id=?,devicetype_id=?,city_id=? where device_id='"+deviceId+"'");

		pSql.setString(1,deviceSn);
		pSql.setString(2,oui);
		pSql.setString(3,deviceMac);
		pSql.setString(4,vendorId);
		pSql.setString(5,deviceModelId);
		pSql.setInt(6,StringUtil.getIntegerValue(deviceTypeId));
		pSql.setString(7,cityId);

		return DBOperation.executeUpdate(pSql.getSQL());
	}

	public int modifyDevice_hn(long id,String ip,String deviceId, String deviceSn, String oui,
			String deviceMac, String vendorId, String deviceModelId,
			String deviceTypeId, String cityId, String citynext,int category)
	{
		// 首先要校验当前设备序列号和mac地址是否存在
		int ier = modifyCheck(deviceId, deviceSn, deviceMac);
		if (ier > 0) {
			return 2;
		}
		if (!"-1".equals(citynext)){
			cityId = citynext;
		}

		PrepareSQL pSql = new PrepareSQL();
		pSql.append("select device_serialnumber,oui,cpe_mac,vendor_id,device_model_id,");
		pSql.append("devicetype_id,city_id ");
		pSql.append("from stb_tab_gw_device ");
		pSql.append("where device_id=? ");
		pSql.setString(1,deviceId);

		Map<String,String> map=DBOperation.getRecord(pSql.getSQL());

		pSql =null;
		pSql = new PrepareSQL();
		pSql.append("update stb_tab_gw_device set device_serialnumber =?,oui=?,cpe_mac=?,vendor_id=?,");
		pSql.append("device_model_id=?,devicetype_id=?,city_id=? where device_id='"+deviceId+"'");

		pSql.setString(1,deviceSn);
		pSql.setString(2,oui);
		pSql.setString(3,deviceMac);
		pSql.setString(4,vendorId);
		pSql.setString(5,deviceModelId);
		pSql.setInt(6,StringUtil.getIntegerValue(deviceTypeId));
		pSql.setString(7,cityId);

		int i=DBOperation.executeUpdate(pSql.getSQL());

		/**
		 * HNLT-REQ-ITMS-20191028-ZC-001 机顶盒行货串货管控
		 */
		if( 0==category || 1==category || 2==category)
		{
			// 查询是否存在
			pSql = new PrepareSQL();
			pSql.append("select count(*) from stb_dev_supplement where device_id = ?");
			pSql.setString(1, deviceId);
			int count = DBOperation.executeProcSelect(pSql.getSQL());

			// 如果存在就更新
			if(count > 0){
				pSql = new PrepareSQL();
				pSql.append("update stb_dev_supplement set category = ? where device_id = ?");
				pSql.setInt(1, category);
				pSql.setString(2, deviceId);
				DBOperation.executeUpdate(pSql.getSQL());
			}
			else
			{// 不存在就插入
				pSql = new PrepareSQL();
				pSql.append("insert into stb_dev_supplement(device_id, category) values (?,?)");
				pSql.setString(1, deviceId);
				pSql.setInt(2, category);
				DBOperation.executeUpdate(pSql.getSQL());
			}
		}

		pSql =null;
		pSql = new PrepareSQL();
		pSql.setSQL(writeLogSql);
		pSql.setLong(1,id);
		pSql.setString(2,ip);
		pSql.setInt(3,1);
		pSql.setLong(4,System.currentTimeMillis()/1000L);
		pSql.setString(5,"5");
		pSql.setString(6,"WEB");

		StringBuffer sb=new StringBuffer("修改设备["+deviceId+"]信息：[");

		sb=combinStr(sb,"设备序列号",map.get("device_serialnumber"),deviceSn);
		sb=combinStr(sb,"oui",map.get("oui"),oui);
		sb=combinStr(sb,"cpe_mac",map.get("cpe_mac"),deviceMac);
		sb=combinStr(sb,"厂商",map.get("vendor_id"),vendorId);
		sb=combinStr(sb,"设备型号",map.get("device_model_id"),deviceModelId);
		sb=combinStr(sb,"版本型号",map.get("devicetype_id"),deviceTypeId);
		sb=combinStr(sb,"属地","00".equals(map.get("city_id"))?"0010000":map.get("city_id"),
										"00".equals(cityId)?"0010000":cityId);
		sb.append("]");

		pSql.setString(7,sb.toString());
		pSql.setString(8,"Web");
		pSql.setString(9,i==1?"成功":"失败");
		pSql.setInt(10,1);
		DBOperation.executeUpdate(pSql.getSQL());

		return i;
	}

	/**
	 * 组装参数
	 */
	public StringBuffer combinStr(StringBuffer sb,String str_name,String old_data,String new_data)
	{
		if(sb.toString().indexOf("->")!=-1){
			sb.append("，");
		}

		if(!StringUtil.IsEmpty(new_data)){
			if(StringUtil.IsEmpty(old_data)){
				sb.append(str_name+"： ->"+new_data);
			}else if(!new_data.equals(old_data)){
				sb.append(str_name+"："+old_data+"->"+new_data);
			}else{
				sb.deleteCharAt(sb.toString().length()-1);
			}
		}else{
			if(!StringUtil.IsEmpty(old_data)){
				sb.append(str_name+"："+old_data+"->  ");
			}else{
				sb.deleteCharAt(sb.toString().length()-1);
			}
		}

		return sb;
	}

	/**
	 * 新增前需要校验设备序列号和mac地址
	 */
	public int modifyCheck(String deviceId, String deviceSn, String deviceMac)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from stb_tab_gw_device a where "  );
		sql.append(" (a.device_serialnumber = '").append(deviceSn).append("' and a.dev_sub_sn ='")
		.append(deviceSn.substring(deviceSn.length()-6, deviceSn.length())).append("' and a.device_id <>'").append(deviceId).append("') ");
		sql.append(" or (a.cpe_mac ='").append(deviceMac).append("' and a.device_id <>'").append(deviceId).append("')");

		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForInt(psql.getSQL());
	}

	public Map<String, String> queryDeviceById(String deviceId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.customer_id, a.cpe_mac, a.serv_account, a.city_id, a.oui, a.cpe_mac, a.device_serialnumber," +
			"a.vendor_id, a.device_model_id, a.devicetype_id, a.device_id, a.complete_time," +
			"a.cpe_currentupdatetime,  a.remark, a.loopback_ip, " +
			"b.vendor_name,b.vendor_add,c.device_model,")
		.append("d.hardwareversion,d.softwareversion,e.serv_account as Eserv_account");

		if(Global.HNLT.equals(Global.instAreaShortName)){
			sql.append(",d.net_type,d.epg_version");
		}// TODO wait (more table related)
		sql.append(" from stb_tab_gw_device a left join stb_tab_vendor b on a.vendor_id=b.vendor_id ")
			.append("left join stb_gw_device_model c on a.device_model_id=c.device_model_id " )
			.append("left join stb_tab_devicetype_info d on a.devicetype_id=d.devicetype_id " )
			.append("left join stb_tab_customer e on a.customer_id=e.customer_id ")
			.append("where a.device_id='"+deviceId+"' ");

		logger.info(sql.toString());
		List rs = jt.queryForList(sql.toString());

		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		Map one = (Map) rs.get(0);
		Map<String, String> map = new HashMap<String, String>();
		map.put("customer_id", StringUtil.getStringValue(one.get("customer_id")));
		map.put("cpe_mac", StringUtil.getStringValue(one.get("cpe_mac")));
		map.put("serv_account", StringUtil.getStringValue(one.get("serv_account")));
		String cityid = StringUtil.getStringValue(one.get("city_id"));
		map.put("cityId", cityid);
		map.put("cityName", cityMap.get(cityid));
		if (!cityid.endsWith("0")){
			map.put("citynext", cityid);
			map.put("parentcity", cityid.substring(0, cityid.length()-2)+"00");
		}else{
			map.put("parentcity", cityid);
			map.put("citynext", "-1");
		}

		map.put("vendorName", StringUtil.getStringValue(one.get("vendor_name")));
		map.put("vendorAdd", StringUtil.getStringValue(one.get("vendor_add")));
		map.put("deviceModel", StringUtil.getStringValue(one.get("device_model")));
		map.put("hardwareversion", StringUtil.getStringValue(one.get("hardwareversion")));
		map.put("softwareversion", StringUtil.getStringValue(one.get("softwareversion")));
		map.put("oui", StringUtil.getStringValue(one.get("oui")));
		map.put("cpeMac", StringUtil.getStringValue(one.get("cpe_mac")));
		map.put("deviceSerialnumber", StringUtil.getStringValue(one.get("device_serialnumber")));
		map.put("vendorId", StringUtil.getStringValue(one.get("vendor_id")));
		map.put("deviceModelId", StringUtil.getStringValue(one.get("device_model_id")));
		map.put("devicetypeId", StringUtil.getStringValue(one.get("devicetype_id")));
		map.put("deviceId", StringUtil.getStringValue(one.get("device_id")));
		map.put("completeTime", transDate(StringUtil.getStringValue(one.get("complete_time"))));
		map.put("cpe_currentupdatetime", transDate(StringUtil.getStringValue(one.get("cpe_currentupdatetime"))));
		map.put("remark", StringUtil.getStringValue(one.get("remark")));
		map.put("loopback_ip", StringUtil.getStringValue(one.get("loopback_ip")));

		if(Global.HNLT.equals(Global.instAreaShortName))
		{
			map.put("net_type", StringUtil.getStringValue(one,"net_type","unknown_net"));
			map.put("epg_version",StringUtil.getStringValue(one,"epg_version",""));

			String sql1="select network_type,addressing_type,public_ip,public_area,"
						+ "public_isp_name,apk_version_code,apk_version_name,category,ip_type "
						+ "from stb_dev_supplement where device_id='"+deviceId+"'";
			logger.info(sql1);
			Map m1=new HashMap();
			try{
				m1=jt.queryForMap(sql1);
			}catch(Exception e){
				logger.error("[{}]-表stb_dev_supplement中无该设备信息",deviceId);
				e.printStackTrace();
				return map;
			}

			map.put("network_type",StringUtil.getStringValue(m1,"network_type",""));
			map.put("addressing_type",StringUtil.getStringValue(m1,"addressing_type",""));
			map.put("public_ip",StringUtil.getStringValue(m1,"public_ip",""));
			map.put("public_area",StringUtil.getStringValue(m1,"public_area",""));
			map.put("public_isp_name",StringUtil.getStringValue(m1,"public_isp_name",""));
			map.put("apk_version_code",StringUtil.getStringValue(m1,"apk_version_code",""));
			map.put("apk_version_name",StringUtil.getStringValue(m1,"apk_version_name",""));
			map.put("category",StringUtil.getStringValue(m1,"category","2"));
			map.put("ip_type",StringUtil.getStringValue(m1,"ip_type","unknown_net"));

			sql1=null;
			m1=null;
		}

		return map;
	}

	public List<Map<String, String>> getStbDeviceList_hnlt(
			int curPage_splitPage, int num_splitPage, String vendorId,
			String deviceModelId, String deviceSn, String deviceMac, String servAccount, String cityId, String citynext,
			String startTime, String endTime, String lastStartTime, String lastEndTime)
	{
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select a.oui, a.cpe_mac, a.device_serialnumber, a.complete_time, a.cpe_currentupdatetime, " +
				" a.device_id, a.city_id, a.vendor_id, a.device_model_id, a.devicetype_id, " +
				" b.vendor_name,b.vendor_add,c.device_model,d.hardwareversion,d.softwareversion,e.serv_account as Eserv_account" +
				" from stb_tab_gw_device a left join stb_tab_vendor b on a.vendor_id = b.vendor_id " +
				" left join stb_gw_device_model c on a.device_model_id = c.device_model_id " +
				" left join stb_tab_devicetype_info d on a.devicetype_id = d.devicetype_id " +
				" left join stb_tab_customer e on a.customer_id = e.customer_id " +
				" where  1=1  "  );
		if ((!StringUtil.IsEmpty(vendorId)) && (!"-1".equals(vendorId))){
			sql.append(" and a.vendor_id='").append(vendorId).append("'");
		}
		if ((!StringUtil.IsEmpty(deviceModelId)) && (!"-1".equals(deviceModelId))){
			sql.append(" and a.device_model_id='").append(deviceModelId).append("'");
		}
		if (!StringUtil.IsEmpty(deviceSn)){
			String sub_deviceSn = deviceSn.substring(deviceSn.length() - 6, deviceSn.length());
			sql.append(" and a.device_serialnumber like '%").append(deviceSn)
			.append("' and a.dev_sub_sn ='").append(sub_deviceSn).append("'");
		}
		if (!StringUtil.IsEmpty(deviceMac)){
			sql.append(" and a.cpe_mac ='").append(deviceMac).append("'");
		}
		if (!StringUtil.IsEmpty(startTime)){
			sql.append(" and a.complete_time >").append(startTime);
		}
		if (!StringUtil.IsEmpty(endTime)){
			sql.append(" and a.complete_time <").append(endTime);
		}
		if (!StringUtil.IsEmpty(lastStartTime)){
			sql.append(" and a.cpe_currentupdatetime >").append(lastStartTime);
		}
		if (!StringUtil.IsEmpty(lastEndTime)){
			sql.append(" and a.cpe_currentupdatetime <").append(lastEndTime);
		}
		if (!StringUtil.IsEmpty(servAccount)){
			sql.append(" and e.serv_account like '%").append(servAccount).append("%'");
		}

		if (!"-1".equals(citynext) && !"00".equals(citynext))
		{
			List citynextList = CityDAO.getAllNextCityIdsByCityPid(citynext);
			sql.append(" and a.city_id in (").append(StringUtils.weave(citynextList)).append(")");
			citynextList = null;
		}else if (!"-1".equals(cityId) && !"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		sql.append(" order by a.complete_time desc");

		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						String city_name = "";
						map.put("vendorName", rs.getString("vendor_name"));
						map.put("vendorAdd", rs.getString("vendor_add"));
						map.put("deviceModel", rs.getString("device_model"));
						map.put("hardwareversion", rs.getString("hardwareversion"));
						map.put("softwareversion", rs.getString("softwareversion"));
						map.put("oui", rs.getString("oui"));
						map.put("cpeMac", rs.getString("cpe_mac"));
						map.put("deviceSerialnumber", rs.getString("device_serialnumber"));
						map.put("completeTime", transDate(rs.getString("complete_time")));
						map.put("cpe_currentupdatetime", transDate(rs.getString("cpe_currentupdatetime")));
						map.put("deviceId", rs.getString("device_id"));
						map.put("cityId", rs.getString("city_id"));
						map.put("servAccount", rs.getString("Eserv_account"));
						String cityid = StringUtil.getStringValue(rs.getString("city_id"));
						if (!StringUtil.IsEmpty(cityid)){
							city_name = StringUtil.getStringValue(CityDAO.getCityIdCityNameMap().get(cityid));
						}
						map.put("cityName", city_name);
						map.put("vendorId", rs.getString("vendor_id"));
						map.put("deviceModelId", rs.getString("device_model_id"));
						map.put("devicetypeId", rs.getString("devicetype_id"));
						return map;
					}
				});
		return list;
	}

	/**
	 * 获取总数并分页
	 */
	public int countStbDeviceList_hnlt(int curPage_splitPage, int num_splitPage,
			String vendorId, String deviceModelId, String deviceSn, String deviceMac, String servAccount,
			String cityId, String citynext,
			String startTime, String endTime, String lastStartTime, String lastEndTime)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from stb_tab_gw_device a left join stb_tab_customer e on a.customer_id=e.customer_id where 1=1");
		if ((!StringUtil.IsEmpty(vendorId)) && (!"-1".equals(vendorId))){
			sql.append(" and a.vendor_id='").append(vendorId).append("'");
		}
		if ((!StringUtil.IsEmpty(deviceModelId)) && (!"-1".equals(deviceModelId))){
			sql.append(" and a.device_model_id='").append(deviceModelId).append("'");
		}
		if (!StringUtil.IsEmpty(deviceSn)){
			String sub_deviceSn = deviceSn.substring(deviceSn.length() - 6, deviceSn.length());
			sql.append(" and a.device_serialnumber like '%").append(deviceSn)
			.append("' and a.dev_sub_sn ='").append(sub_deviceSn).append("'");
		}
		if (!StringUtil.IsEmpty(deviceMac)){
			sql.append(" and a.cpe_mac ='").append(deviceMac).append("'");
		}
		if (!StringUtil.IsEmpty(startTime)){
			sql.append(" and a.complete_time >").append(startTime);
		}
		if (!StringUtil.IsEmpty(endTime)){
			sql.append(" and a.complete_time <").append(endTime);
		}
		if (!StringUtil.IsEmpty(lastStartTime)){
			sql.append(" and a.cpe_currentupdatetime >").append(lastStartTime);
		}
		if (!StringUtil.IsEmpty(lastEndTime)){
			sql.append(" and a.cpe_currentupdatetime <").append(lastEndTime);
		}
		if (!StringUtil.IsEmpty(servAccount)){
			sql.append(" and e.serv_account like '%").append(servAccount).append("%'");
		}
		if (!"-1".equals(citynext) && !"00".equals(citynext)){
			List citynextList = CityDAO.getAllNextCityIdsByCityPid(citynext);
			sql.append(" and a.city_id in (").append(StringUtils.weave(citynextList)).append(")");
			citynextList = null;
		}else if (!"-1".equals(cityId) && !"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}

		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		queryCount = total;
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	private static final String transDate(Object seconds)
	{
		if (seconds != null)
		{
			try
			{
				DateTimeUtil dt = new DateTimeUtil(Long.parseLong(seconds.toString()) * 1000);
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

	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}


}
