/**
 * 
 */
package com.linkage.module.gtms.stb.resource.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.Base64;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.litms.common.util.JdbcTemplateExtend;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-11-5
 * @category com.linkage.module.gwms.resource.dao
 * 
 */
public class UserInstReleaseDAO{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(UserInstReleaseDAO.class);

	/** JDBC */
	private JdbcTemplateExtend jt;

	/**
	 * set jdbc
	 */
	public void setDao(DataSource dao) {
		logger.debug("setDao(DataSource)");
		this.jt = new JdbcTemplateExtend(dao);
	}

	/**
	 * 查询用户信息
	 * 
	 * @param cityId
	 *            属地为必须，否则以省中心来处理
	 * @param servAccount  |
	 * @param deviceSN  |cust_name、deviceSN必须传一个，否则返回size为0的List实例
	 * 			  nameType   "1" 用户账号  "2"业务账号  "3"Voip账号  "4"Voip电话号码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getUserInfo(String cityId, String servAccount) {
		
		if (null == servAccount || "".equals(servAccount)) {
			return new ArrayList<Map<String, String>>();	
		}
		
		PrepareSQL ppSQL = new PrepareSQL();
		ppSQL.setSQL(" select a.customer_id,a.city_id,a.serv_account from stb_tab_customer a where 1=1  ");
		if (null != servAccount && !"".equals(servAccount)) {
			ppSQL.appendAndString("a.serv_account", PrepareSQL.EQUEAL, servAccount);
		}
		List rs = null;
		if(Global.NXDX.equals(Global.instAreaShortName))
		{
		rs =  DBOperation.getRecords(ppSQL.getSQL(),"xml-stb");
		}else{
		rs =  DBOperation.getRecords(ppSQL.getSQL());
		}
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		
		Map<String,String> cityMap = CityDAO.getCityIdCityNameMap();
		
		for(int i=0;i<rs.size();i++){
			Map one = (Map)rs.get(i);
			Map<String, String> map = new HashMap<String, String>();
			map.put("customer_id", String.valueOf(one.get("customer_id")));
			map.put("serv_account", String.valueOf(one.get("serv_account")));
			map.put("city_id", String.valueOf(one.get("city_id")));
			map.put("city_name", cityMap.get(String.valueOf(one.get("city_id"))));
			result.add(map);
		}
		cityMap = null;
		
		return result;
	}
	
	/**
	 * 获取用户终端类型
	 * @param userId
	 * @return
	 */
	private String getUserDevType(String userId)
	{
		logger.debug("UserInstReleaseDAO=>getUserDevType(userId:{})",
				new Object[] { userId });

		PrepareSQL ppSQL = new PrepareSQL(" select b.type_name from gw_cust_user_dev_type a,gw_dev_type b where a.type_id=b.type_id and a.customer_id=? ");
		ppSQL.setLong(1, Long.parseLong(userId));
		List list = jt.queryForList(ppSQL.toString());
		if(null == list || list.size() == 0 || null == list.get(0))
		{
			return "e8-b";
		}
		Map map = (Map)list.get(0);
		if(null == map.get("type_name"))
		{
			return "e8-b";
		}
		return map.get("type_name").toString();
	}
	
	/**
	 * 据机顶盒deviceId绑定设备的用户.
	 * 
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getBindUserByDeviceId(
			String deviceId) {
		PrepareSQL pSQL = new PrepareSQL(
				"select a.customer_id,a.serv_account,a.city_id,a.addressing_type,a.openUserdate,a.user_status,b.device_id,b.device_serialnumber,b.oui from stb_tab_customer a"
						+ ",stb_tab_gw_device b where a.customer_id = b.customer_id and b.device_id = ?  order by openUserdate asc");
		int index = 0;
		pSQL.setString(++index, deviceId);
		if(Global.NXDX.equals(Global.instAreaShortName))
		{
		return DBOperation.getRecords(pSQL.getSQL(),"xml-stb");
		}
		else
		{
			return DBOperation.getRecords(pSQL.getSQL());
		}
	}
	
	/**
	 * 据机顶盒deviceId绑定设备的用户.
	 * 
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getBssSheet(String customerId) {
		PrepareSQL pSQL = new PrepareSQL(
				"select a.serv_account, a.net_type, a.pppoe_user, a.pppoe_pwd , b.cpe_mac, b.device_type " +
				"from stb_tab_customer a left join stb_tab_gw_device b on a.customer_id = b.customer_id " +
				"where a.customer_id = ?  order by openUserdate asc");
		pSQL.setInt(1, StringUtil.parseInt(customerId));
		if(Global.NXDX.equals(Global.instAreaShortName))
		{
			ArrayList<HashMap<String,String>> maps = DBOperation.getRecords(pSQL.getSQL(), "xml-stb");
			for(HashMap<String,String> hmap : maps){
				hmap.put("pppoe_pwd", Base64.decode(hmap.get("pppoe_pwd")));
			}
			return maps;
		}
		else
		{
			return DBOperation.getRecords(pSQL.getSQL());
		}
	}

	/**
	 * 根据deviceId查询设备.
	 * 
	 * @return
	 */
	public Map<String, String> getDeviceById(String deviceId) {
		logger.debug("getDeviceById({})", deviceId);
		PrepareSQL pSQL = new PrepareSQL(
				"select customer_id,serv_account,city_id,device_id from stb_tab_gw_device where device_id=?");
		int index = 0;
		pSQL.setString(++index, deviceId);
		if(Global.NXDX.equals(Global.instAreaShortName))
		{
		return DBOperation.getRecord(pSQL.getSQL(),"xml-stb");
		}
		return DBOperation.getRecord(pSQL.getSQL());	
	}
	
	/**
	 * 根据deviceId查询策略信息.
	 */
	public ArrayList<HashMap<String, String>> getConfigInfo(String deviceId) 
	{
		logger.debug("getConfigInfo({})", deviceId);
		if(Global.XJDX.equals(Global.instAreaShortName))
		{
			PrepareSQL pSQL = new PrepareSQL();
			pSQL.append("select id,sheet_id,service_id,status,result_id,result_desc,");
			pSQL.append("start_time,end_time,sheet_para from ");
			pSQL.append(Global.SERV_STRATEGY_TABLE);
			pSQL.append(" where service_id = 120 and device_id=? ");
			pSQL.setString(1, deviceId);
			return DBOperation.getRecords(pSQL.getSQL());
		}
		else
		{
			String table="stb_gw_serv_strategy";
			if(Global.SXLT.equals(Global.instAreaShortName)){
				table="stb_gw_strategy_serv";
			}
			PrepareSQL pSQL = new PrepareSQL();
			pSQL.append("select id,sheet_id,status,result_id,service_id,result_desc,start_time,end_time,sheet_para from ");
			pSQL.append(table);
			pSQL.append(" where service_id=120 and device_id=? ");
			
			pSQL.setString(1, deviceId);
			if (Global.NXDX.equals(Global.instAreaShortName)){
				return DBOperation.getRecords(pSQL.getSQL(), "xml-stb");
			}
			return DBOperation.getRecords(pSQL.getSQL());
		}
	}
	
	
	public ArrayList<HashMap<String, String>> getConfigInfoLogSXLT(String deviceId) 
	{
		logger.debug("getConfigInfo({})", deviceId);
		String table="stb_gw_strategy_serv_log";
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select id,sheet_id,status,result_id,service_id,result_desc,start_time,end_time,sheet_para from ");
		pSQL.append(table);
		pSQL.append(" where service_id=120 and device_id=? order by start_time desc");
		
		pSQL.setString(1, deviceId);
		if (Global.NXDX.equals(Global.instAreaShortName)){
			return DBOperation.getRecords(pSQL.getSQL(), "xml-stb");
		}
		return DBOperation.getRecords(pSQL.getSQL());
	}
	
	public ArrayList<HashMap<String, String>> getConfigInfoLogSXLT(String deviceId, String id) 
	{
		logger.debug("getConfigInfo({})", deviceId);
		String table="stb_gw_strategy_serv_log";
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select id,sheet_id,status,result_id,service_id,result_desc,start_time,end_time,sheet_para from ");
		pSQL.append(table);
		pSQL.append(" where service_id=120 and device_id=? and id=? order by start_time desc");
		
		pSQL.setString(1, deviceId);
		pSQL.setString(2, id);
		if (Global.NXDX.equals(Global.instAreaShortName)){
			return DBOperation.getRecords(pSQL.getSQL(), "xml-stb");
		}
		return DBOperation.getRecords(pSQL.getSQL());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getServiceCode()
	{
		String sql = "select distinct service_id,service_name from tab_service ";
		PrepareSQL psql = new PrepareSQL(sql);
		List<HashMap<String, String>> list = null;
		if(Global.NXDX.equals(Global.instAreaShortName))
		{
			list =  DBOperation.getRecords(psql.getSQL(),"xml-stb");
		}else
		{
			list  = DBOperation.getRecords(psql.getSQL());
		}
		Map serviceCodeMap = new HashMap<String, String>();
		for (Map map : list)
		{
			serviceCodeMap.put(StringUtil.getStringValue(map.get("service_id")),
					StringUtil.getStringValue(map.get("service_name")));
		}
		return serviceCodeMap;
	}

	/**
	 * 更新用户的业务开通状态
	 * 
	 * @param userId
	 * @return int 1:成功
	 */
	public int updateServOpenStatus(String customer_id)
	{
		logger.debug("updateServOpenStatus({}, {})", customer_id);
		// 更新SQL语句
		String strSQL = "update stb_tab_customer set user_status=0 "
				+ " where customer_id=? ";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setLong(1, StringUtil.getLongValue(customer_id));
		// 执行查询
		int reti = 0;
		if (Global.NXDX.equals(Global.instAreaShortName))
		{
			reti = DBOperation.executeUpdate(psql.getSQL(), "xml-stb");
		}
		else
		{
			reti = DBOperation.executeUpdate(psql.getSQL());
		}
		return reti;
	}

}
