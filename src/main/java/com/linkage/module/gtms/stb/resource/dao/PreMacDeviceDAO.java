package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 机顶盒MAC前缀DAO
 * @author jiafh
 *
 */
public class PreMacDeviceDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(PreMacDeviceDAO.class);

	private static String writeLogSql = "insert into tab_oper_log("
			+ "acc_oid,acc_login_ip,operationlog_type,operation_time,operation_name"
			+ ",operation_object,operation_content,operation_device"
			+ ",operation_result,result_id) values(?,?,?,?,?,?,?,?,?,?)";


	/**
	 * 根据厂商名称查询厂商信息
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> queryVendor(String vendorName)
	{
		logger.debug("PreMacDeviceDAO ==> queryVendor({})",vendorName);

		String strSQL = "select vendor_id, vendor_add from stb_tab_vendor ";
		if(!StringUtil.IsEmpty(vendorName)){
			strSQL += "where vendor_add='" + vendorName + "' ";
		}
		strSQL += "order by vendor_id ";
		PrepareSQL psql = new PrepareSQL(strSQL);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 根据厂商ID查询设备型号信息
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getDeviceModelList(String vendorId)
	{
		logger.debug("PreMacDeviceDAO ==> getDeviceModel({})",vendorId);

		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
		psql.append("select distinct(b.device_model_id) as device_model_id,b.device_model ");
		psql.append("from stb_tab_vendor a,stb_gw_device_model b,stb_tab_devicetype_info c ");
		psql.append("where a.vendor_id=b.vendor_id and a.vendor_id=c.vendor_id ");
		psql.append("and b.device_model_id=c.device_model_id and a.vendor_id=? ");
		psql.append("order by b.device_model_id ");
		psql.setString(1,vendorId);

		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 根据厂商ID、型号ID查询设备软件版本
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getSoftwareversionS(String vendorId,String deviceModelId)
	{
		logger.debug("PreMacDeviceDAO ==> getSoftwareversionS({},{})",vendorId,deviceModelId);

		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
		psql.append("select distinct(c.softwareversion) as softwareversion ");
		psql.append("from stb_tab_vendor a,stb_gw_device_model b,stb_tab_devicetype_info c ");
		psql.append("where a.vendor_id=b.vendor_id and a.vendor_id=c.vendor_id ");
		psql.append("and b.device_model_id=c.device_model_id and a.vendor_id=? and b.device_model_id=? ");
		psql.append("order by c.softwareversion ");
		psql.setString(1,vendorId);
		psql.setString(2,deviceModelId);

		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 根据厂商ID、型号ID、软件版本查询设备硬件版本
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getHardwareversionS(String vendorId,String deviceModelId,String softwareversion)
	{
		logger.debug("PreMacDeviceDAO ==> getHardwareversionS({},{},{})",new Object[]{vendorId,deviceModelId,softwareversion});

		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
		psql.append("select distinct(c.hardwareversion) as hardwareversion ");
		psql.append("from stb_tab_vendor a,stb_gw_device_model b,stb_tab_devicetype_info c ");
		psql.append("where a.vendor_id=b.vendor_id and a.vendor_id=c.vendor_id and b.device_model_id=c.device_model_id ");
		psql.append("and a.vendor_id=? and b.device_model_id=? and c.softwareversion=? ");
		psql.append("order by c.hardwareversion ");
		psql.setString(1,vendorId);
		psql.setString(2,deviceModelId);
		psql.setString(3,softwareversion);

		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 查询设备版本信息
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> queryDevicetypeInfo(String vendor_id,String deviceModelId,String hardwareversion,String softwareversion)
	{
		logger.debug("PreMacDeviceDAO ==> queryDevicetypeInfo({},{},{},{})",vendor_id,deviceModelId,hardwareversion,softwareversion);
		// TODO wait (more table related)
		String strSQL = "select c.devicetype_id from stb_tab_vendor a, stb_gw_device_model b, stb_tab_devicetype_info c" +
				" where a.vendor_id=b.vendor_id and a.vendor_id=c.vendor_id and b.device_model_id = c.device_model_id" +
				" and a.vendor_id=? and b.device_model_id=? and c.hardwareversion=? and c.softwareversion=? ";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1,vendor_id);
		psql.setString(2,deviceModelId);
		psql.setString(3,hardwareversion);
		psql.setString(4,softwareversion);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 查询MAC前缀和版本对应关系
	 */
	// 没有类调用 注释掉 2020/10/20
	/*@SuppressWarnings("unchecked")
	public List<Map<String,String>> queryMacDevicetypeList(String preMac,String vendorName,String deviceModel,String hardwareversion,String softwareversion)
	{
		logger.debug("PreMacDeviceDAO ==> queryMacDevicetypeList({},{},{},{},{})",preMac,vendorName,deviceModel,hardwareversion,softwareversion);

		String strSQL = "select a.*,b.*,c.*,d.* from stb_tab_vendor a, stb_gw_device_model b, stb_tab_devicetype_info c,stb_mac_devicetype d" +
				" where a.vendor_id=b.vendor_id and a.vendor_id=c.vendor_id and b.device_model_id=c.device_model_id and c.devicetype_id=d.devicetype_id" +
				" and a.vendor_name=? and b.device_model=? and c.hardwareversion=? and c.softwareversion=? and d.pre_mac=? ";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1,vendorName);
		psql.setString(2,deviceModel);
		psql.setString(3,hardwareversion);
		psql.setString(4,softwareversion);
		psql.setString(5,preMac);
		return jt.queryForList(psql.getSQL());
	}*/

	/**
	 * 查询MAC前缀和设备信息对应关系
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> queryMacDevicetypeListByPreMac(String preMac,String preMacId){
		String strSQL = "select id from stb_mac_devicetype where 1=1";
		if(!StringUtil.IsEmpty(preMacId)){
			strSQL += " and id='" + preMacId + "'";
		}
		if(!StringUtil.IsEmpty(preMac)){
			strSQL += " and pre_mac='" + preMac + "'";
		}

		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1,preMac);
		return jt.queryForList(psql.getSQL());
	}


	/**
	 * 查询MAC前缀和版本对应关系
	 */
	@SuppressWarnings({ "rawtypes" })
	public List<Map> queryMacDevicetypeListById(int curPage_splitPage, int num_splitPage,String preMac,String vendorId,
			String deviceModelId,String hardwareversion,String softwareversion){
		logger.debug("PreMacDeviceDAO ==> queryMacDevicetypeListById({},{},{},{},{})",preMac,vendorId,deviceModelId,hardwareversion,softwareversion);

//		String strSQL = "select a.*,b.*,c.*,d.*,e.* from stb_tab_vendor a, stb_gw_device_model b, stb_tab_devicetype_info c,stb_mac_devicetype d
//		left join stb_serv_platform e on d.belong=e.platform_id" +
//				" where a.vendor_id=b.vendor_id and a.vendor_id=c.vendor_id and b.device_model_id = c.device_model_id and c.devicetype_id = d.devicetype_id";
		// TODO wait (more table related)
		String strSQL = "select a.vendor_id, a.vendor_add, b.device_model_id, b.device_model, c.softwareversion, c.hardwareversion, d.id, d.pre_mac " +
				"from stb_tab_vendor a,stb_gw_device_model b,stb_tab_devicetype_info c,stb_mac_devicetype d " +
				" where a.vendor_id=b.vendor_id and a.vendor_id=c.vendor_id and b.device_model_id=c.device_model_id and c.devicetype_id=d.devicetype_id";

		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			strSQL += " and a.vendor_id ='" + vendorId + "'";
		}

		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			strSQL += " and b.device_model_id ='" + deviceModelId + "'";
		}

		if(!StringUtil.IsEmpty(hardwareversion) && !"-1".equals(hardwareversion)){
			strSQL += " and c.hardwareversion ='" + hardwareversion + "'";
		}

		if(!StringUtil.IsEmpty(softwareversion) && !"-1".equals(softwareversion)){
			strSQL += " and c.softwareversion ='" + softwareversion + "'";
		}

		if(!StringUtil.IsEmpty(preMac) && !"-1".equals(preMac)){
			strSQL += " and d.pre_mac like '%" + preMac + "%'";
		}

//		if(!StringUtil.IsEmpty(platformId) && !"0".equals(platformId)){
//			strSQL += " and e.platform_id =" + platformId;
//		}

		strSQL += " order by a.vendor_id,b.device_model_id,c.softwareversion,c.hardwareversion ";
		PrepareSQL psql = new PrepareSQL(strSQL);
		return querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", rs.getString("id"));
				map.put("pre_mac", rs.getString("pre_mac"));
				map.put("vendor_id", rs.getString("vendor_id"));
				map.put("vendor_add", rs.getString("vendor_add"));
				map.put("device_model", rs.getString("device_model"));
				map.put("device_model_id", rs.getString("device_model_id"));
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("hardwareversion", rs.getString("hardwareversion"));
//				map.put("platform_id", rs.getString("platform_id"));
//				map.put("platform_name", rs.getString("platform_name"));
				return map;
			}
		});
	}

	/**
	 * 查询MAC前缀和版本对应关系
	 */
	@SuppressWarnings({ "unchecked" })
	public List<Map<String,String>> queryMacDevicetypeListById(String preMac,String vendorId,
			String deviceModelId,String hardwareversion,String softwareversion){
		logger.debug("PreMacDeviceDAO ==> queryMacDevicetypeListById({},{},{},{},{})",preMac,vendorId,deviceModelId,hardwareversion,softwareversion);

//		String strSQL = "select a.*,b.*,c.*,d.*,e.* from stb_tab_vendor a, stb_gw_device_model b, stb_tab_devicetype_info c,stb_mac_devicetype d
//				left join stb_serv_platform e on d.belong=e.platform_id" +
//				" where a.vendor_id=b.vendor_id and a.vendor_id=c.vendor_id and b.device_model_id = c.device_model_id and c.devicetype_id = d.devicetype_id";
		// TODO wait (more table related)
		String strSQL = "select d.id from stb_tab_vendor a,stb_gw_device_model b,stb_tab_devicetype_info c,stb_mac_devicetype d " +
				" where a.vendor_id=b.vendor_id and a.vendor_id=c.vendor_id and b.device_model_id=c.device_model_id and c.devicetype_id=d.devicetype_id";

		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			strSQL += " and a.vendor_id ='" + vendorId + "'";
		}

		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			strSQL += " and b.device_model_id ='" + deviceModelId + "'";
		}

		if(!StringUtil.IsEmpty(hardwareversion) && !"-1".equals(hardwareversion)){
			strSQL += " and c.hardwareversion ='" + hardwareversion + "'";
		}

		if(!StringUtil.IsEmpty(softwareversion) && !"-1".equals(softwareversion)){
			strSQL += " and c.softwareversion ='" + softwareversion + "'";
		}

		if(!StringUtil.IsEmpty(preMac) && !"-1".equals(preMac)){
			strSQL += " and d.pre_mac like '%" + preMac + "%'";
		}

//		if(!StringUtil.IsEmpty(platformId) && !"0".equals(platformId)){
//			strSQL += " and e.platform_id =" + platformId;
//		}

		strSQL += " order by a.vendor_id,b.device_model_id,c.softwareversion,c.hardwareversion ";
		PrepareSQL psql = new PrepareSQL(strSQL);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 获取最新MAC前缀和版本对应关系ID
	 */
	@SuppressWarnings("unchecked")
	public String queryMacDevicetypeId(){
		logger.debug("PreMacDeviceDAO ==> queryMacDevicetypeId()");

		String strSQL = "";
		if(DBUtil.GetDB() == 3)
		{// mysql
			strSQL = "select max(cast(id as signed)) as id from stb_mac_devicetype";
		}
		else
		{
			strSQL = "select max(to_number(id)) as id  from stb_mac_devicetype";
		}
		PrepareSQL psql = new PrepareSQL(strSQL);
		Map<String,String> macDevicetypeMap =  jt.queryForMap(psql.getSQL());
		if(null != macDevicetypeMap){
			return StringUtil.getStringValue(StringUtil.getIntegerValue(macDevicetypeMap.get("id")) + 1);
		}
		return "1";
	}

	/**
	 * 新增MAC前缀和版本对应关系，并返回新增ID
	 */
	public synchronized String addMacDevicetype(int devicetypeId,String preMac){
		logger.debug("PreMacDeviceDAO ==> addMacDevicetype({},{})",devicetypeId,preMac);

		String strSQL = "insert into stb_mac_devicetype(id,pre_mac,devicetype_id,add_time,update_time) values(?,?,?,?,?)";
		String macDevicetypeId = queryMacDevicetypeId();
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1,macDevicetypeId);
		psql.setString(2,preMac);
		psql.setInt(3,devicetypeId);
		psql.setLong(4,System.currentTimeMillis()/1000);
		psql.setLong(5,System.currentTimeMillis()/1000);
		jt.execute(psql.getSQL());
		return macDevicetypeId;
	}

	/**
	 * 更新MAC前缀和版本对应关系
	 */
	public void updateMacDevicetype(String id,String preMac,int devicetypeId){
		logger.debug("PreMacDeviceDAO ==> updateMacDevicetype({},{},{})",id,preMac,devicetypeId);

		String strSQL = "update stb_mac_devicetype set pre_mac=?,devicetype_id=?,update_time=? where id=?";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1,preMac);
		psql.setInt(2,devicetypeId);
		psql.setLong(3,System.currentTimeMillis()/1000);
		psql.setString(4,id);
		jt.execute(psql.getSQL());
	}

	/**
	 * 删除MAC前缀和版本对应关系
	 */
	public void deleteMacDevicetype(String id){
		logger.debug("PreMacDeviceDAO ==> deleteMacDevicetype({})",id);

		String strSQL = "delete from stb_mac_devicetype where id=? ";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1,id);
		jt.execute(psql.getSQL());
	}


	/**
	 * 获取mac前缀数据
	 */
	public Map<String, String> getMacDevicetype(String preMacId)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select pre_mac,devicetype_id from stb_mac_devicetype ");
		psql.append("where id=? ");
		psql.setString(1,preMacId);

		return DBOperation.getRecord(psql.getSQL());
	}

	/**
	 *记录新增信息日志
	 **/
	public void addLogInsert(long user_id, String user_ip, String vendor_id,
			int devicetypeId, String preMac,String rs)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(writeLogSql);
		psql.setLong(1,user_id);
		psql.setString(2,user_ip);
		psql.setInt(3,1);
		psql.setLong(4,System.currentTimeMillis()/1000L);
		psql.setString(5,"5");
		psql.setString(6,"WEB");

		StringBuffer sb=new StringBuffer();
		sb.append("新增mac前缀["+rs+"]信息：[");
		sb.append("厂商:"+vendor_id);
		sb.append("，型号:"+devicetypeId);
		sb.append("，mac前缀:"+preMac);
		sb.append("]");

		psql.setString(7,sb.toString());
		psql.setString(8,"Web");
		psql.setString(9,"1".equals(rs)?"失败":"成功");
		psql.setInt(10,1);
		DBOperation.executeUpdate(psql.getSQL());
	}

	/**
	 *记录修改信息日志
	 **/
	public void addLogUpdate(long user_id, String user_ip,String preMacId, String preMac,
			int devicetypeId, Map<String, String> map)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(writeLogSql);
		psql.setLong(1,user_id);
		psql.setString(2,user_ip);
		psql.setInt(3,1);
		psql.setLong(4,System.currentTimeMillis()/1000L);
		psql.setString(5,"5");
		psql.setString(6,"WEB");

		StringBuffer sb=new StringBuffer("修改mac前缀["+preMacId+"]信息：[");
		sb=combinStr(sb,"型号",map.get("devicetype_id"),devicetypeId+"");
		sb=combinStr(sb,"mac前缀",map.get("pre_mac"),preMac);
		sb.append("]");

		psql.setString(7,sb.toString());
		psql.setString(8,"Web");
		String rs="失败";
		if(devicetypeId!=StringUtil.getIntValue(map,"devicetype_id")
				|| !StringUtil.getStringValue(map,"pre_mac","").equals(preMac)){
			rs="成功";
		}
		psql.setString(9,rs);
		psql.setInt(10,1);
		DBOperation.executeUpdate(psql.getSQL());
	}

	/**
	 *记录删除信息日志
	 **/
	public void addLogDelete(long user_id, String user_ip,String preMacId,
			Map<String, String> map)
	{
		Map<String,String> map1=getMacDevicetype(preMacId);
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(writeLogSql);
		psql.setLong(1,user_id);
		psql.setString(2,user_ip);
		psql.setInt(3,1);
		psql.setLong(4,System.currentTimeMillis()/1000L);
		psql.setString(5,"5");
		psql.setString(6,"WEB");

		StringBuffer sb=new StringBuffer();
		sb.append("删除mac前缀["+preMacId+"]信息：[");
		sb.append("型号:"+map.get("devicetype_id"));
		sb.append("，mac前缀:"+map.get("pre_mac"));
		sb.append("]");

		psql.setString(7,sb.toString());
		psql.setString(8,"Web");
		psql.setString(9,(map1==null || map1.isEmpty())?"成功":"失败");
		psql.setInt(10,1);
		DBOperation.executeUpdate(psql.getSQL());
	}

	/**
	 * 组装参数
	 */
	public StringBuffer combinStr(StringBuffer sb,String str_name,String old_data,String new_data){
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

}
