package dao.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import com.linkage.litms.LipossGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.FormUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author Jason(3412)
 * @date 2008-12-16
 */
public class DeviceVendorModelDao {

	private static final Logger LOG = LoggerFactory.getLogger(DeviceVendorModelDao.class);
	
	private JdbcTemplate jt;

	/**
	 * 根据用户账号或设备序列号等查询IPTV业务用户
	 * @author gongsj
	 * @date 2009-9-16
	 * @param param1
	 * @param param2
	 * @param type
	 * @param pcity_id
	 * @param vendorname
	 * @param gwType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getDeviceList(String param1, String param2, String type, String pcity_id, String vendorname, String gw_type) {
		
		String parServiceId = null;
		String username = null;
		Set keySet = null;
		Iterator it = null;
		
		StringBuffer buffrSQL = new StringBuffer();
		List list = CityDAO.getAllNextCityIdsByCityPid(pcity_id);

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			buffrSQL.append(" select x.device_id,x.gather_id,x.city_id,x.username, x.city_name,x.user_id,x.oui,x.device_serialnumber,x.wan_type," +
					" x.device_model_id,x.devicetype_id, y.result_id ,y.temp_id from (");
		}
		else {
			buffrSQL.append(" select x.*, y.result_id ,y.temp_id from (");
		}
		if(gw_type.equals(Global.GW_TYPE_ITMS))
		{
			buffrSQL.append(" select a.device_id,a.gather_id,b.city_id,b.username, c.city_name,b.user_id,a.oui,a.device_serialnumber,b.wan_type,")
		    .append(" a.device_model_id,a.devicetype_id ")
		    .append(" from tab_gw_device a, tab_hgwcustomer b, tab_city c where a.oui=b.oui ")
	//	    .append(" and b.city_id=c.city_id and a.device_serialnumber=b.device_serialnumber and b.user_state='1' ")
		    .append(" and b.city_id=c.city_id and a.device_id=b.device_id and b.user_state='1' ")
		    .append(" and b.city_id in (")
		    .append( StringUtils.weave(list))
		    .append(" )");
		}
		else
		{
			buffrSQL.append(" select a.device_id,a.gather_id,b.city_id,b.username, c.city_name,b.user_id,a.oui,a.device_serialnumber,b.wan_type,")
		    .append(" a.device_model_id,a.devicetype_id ")
		    .append(" from tab_gw_device a, tab_egwcustomer b, tab_city c where a.oui=b.oui ")
	//	    .append(" and b.city_id=c.city_id and a.device_serialnumber=b.device_serialnumber and b.user_state='1' ")
		    .append(" and b.city_id=c.city_id and a.device_id=b.device_id and b.user_state='1' ")
		    .append(" and b.city_id in (")
		    .append( StringUtils.weave(list))
		    .append(" )");
		}
		list = null;
//		buffrSQL.append(" select a.device_id,a.gather_id,b.city_id,b.username, c.city_name,b.user_id,a.oui,a.device_serialnumber,b.wan_type,")
//	            .append(" a.device_model_id,a.devicetype_id, d.open_status ,d.serv_type_id ")
//	            .append(" from tab_gw_device a, tab_hgwcustomer b, tab_city c, hgwcust_serv_info d where a.oui=b.oui ")
//	            .append(" and b.user_id=d.user_id and b.city_id=c.city_id and a.device_serialnumber=b.device_serialnumber and b.user_state='1' ")
//	            .append(" and b.city_id in (")
//	            .append( StringUtils.weave(CityDAO.getAllNextCityIdsByCityPid(pcity_id)))
//	            .append(" )");

		// 根据用户名和电话号码
		if ("1".equals(type)) {
			if (param1 != null && !"".equals(param1)) {
				buffrSQL.append(" and b.username = '").append(param1).append("'");
			}
			if (param2 != null && !"".equals(param2)) {
				buffrSQL.append(" and b.linkphone like '%").append(param2).append("%'");
			}
			
			// 根据序列号和IP
		} else if ("2".equals(type)) {
			if (param1 != null && !"".equals(param1)) {
				if(param1.length()>5){
					buffrSQL.append(" and a.dev_sub_sn ='").append(param1.substring(param1.length()-6, param1.length())).append("'");
				}
				buffrSQL.append(" and a.device_serialnumber like '%").append(param1).append("'");
			}
			if (param2 != null && !"".equals(param2)) {
				buffrSQL.append(" and a.loopback_ip like '%").append(param2).append("%'");
			}

		} else {
			// 设备厂商
			if (vendorname != null && !"".equals(vendorname) && !"-1".equals(vendorname)) {
				buffrSQL.append(" and a.vendor_id in (select vendor_id from tab_vendor where vendor_name='").append(vendorname).append("')");
			}
			// 设备型号
			if (param1 != null && !"".equals(param1) && !"-1".equals(param1)) {
				buffrSQL.append(" and a.device_model_id in (select device_model_id from gw_device_model where device_model='").append(param1).append("')");
			}
			// 设备版本
			if (param2 != null && !"".equals(param2) && !"-1".equals(param2)) {
				buffrSQL.append(" and a.devicetype_id in (select devicetype_id from tab_devicetype_info where softwareversion='").append(param2).append("')");
			}
		}
		
		buffrSQL.append(" ) x left join gw_serv_strategy y on x.device_id=y.device_id and y.temp_id=1 and y.is_last_one=1");
		
		PrepareSQL psql = new PrepareSQL(buffrSQL.toString());
		psql.getSQL();
		
		//查询符合条件的设备
		List<Map<String, String>> allUserList = jt.queryForList(buffrSQL.toString());
		
		List<String> usernameList = new ArrayList();
		
		List<Map<String, String>> endUsernameList1 = new ArrayList<Map<String, String>>();
		
		Map<String, String> nameStatusMap = new HashMap<String, String>();
		
		//Map<String, String> userBusinessStatusMap = getUserBusinessStatus(userList);
		
		for(Map<String, String> userMap : allUserList) {
			
			parServiceId = String.valueOf(userMap.get("temp_id"));
			username = String.valueOf(userMap.get("username"));
			
			if (usernameList.contains(username)) {
				
				if ("1".equals(parServiceId)) {
					//userMap.put("serv_type_id", servTypeId);
					//userMap.put("open_status", String.valueOf(userMap.get("open_status")));
					LOG.debug("W-------------:" + parServiceId);
					nameStatusMap.put(username, String.valueOf(userMap.get("open_status")));
				}
				
			} else {
				usernameList.add(username);
				endUsernameList1.add(userMap);
				
			}
		}
		
		LOG.debug("V-------------:" + nameStatusMap);
		
		for(Map<String, String> userMap : endUsernameList1) {
			keySet = nameStatusMap.keySet();
			it = keySet.iterator();
			
			while(it.hasNext()) {
				
				LOG.debug("U-------------:" + String.valueOf(userMap.get("username")));
				
				if (it.next().equals(String.valueOf(userMap.get("username")))) {
					
					LOG.debug("T-------------:" + String.valueOf(userMap.get("username")));
					
					userMap.put("open_status", nameStatusMap.get(String.valueOf(userMap.get("username"))));
					userMap.put("temp_id", "1");
					
					LOG.debug("S-------------:" + userMap);
				}
			}
			
			if ("1".equals(String.valueOf(userMap.get("result_id"))) && "1".equals(String.valueOf(userMap.get("temp_id")))) {
				//已配置已成功
				userMap.put("open_status_code", "1");
			} else if ((!"1".equals(String.valueOf(userMap.get("result_id")))) && "1".equals(String.valueOf(userMap.get("temp_id")))) {
				//已配置未成功
				userMap.put("open_status_code", "2");
			} else {
				//未配置未成功
				userMap.put("open_status_code", "3");
			}
		}
		
		allUserList = null;
		nameStatusMap = null;
		
		return endUsernameList1;
		
	}

	/**
	 * 根据传入的List查询满足条件的数据
	 * 
	 * @param
	 * @author qxq(4174)
	 * @date 2009-02-16
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List getDeviceExceptionList(String pcity_id,List<String> userList) {
		
		StringBuffer buffrSQL = new StringBuffer();
		
		buffrSQL.append("select username,device_serialnumber from tab_hgwcustomer where user_state in ('1','2') and username in(");
		
		for(int i=0;i<userList.size();i++){
			buffrSQL.append("'");
			buffrSQL.append(userList.get(i));
			if(i==(userList.size()-1)){
				buffrSQL.append("') ");
			}else{
				buffrSQL.append("',");
			}
		}
		
		buffrSQL.append("and city_id in (");
		List list = CityDAO.getAllNextCityIdsByCityPid(pcity_id);
		buffrSQL.append(StringUtils.weave(list));
		list = null;
		buffrSQL.append(")");
		
		PrepareSQL psql = new PrepareSQL(buffrSQL.toString());
		psql.getSQL();
		
		List rs = jt.queryForList(buffrSQL.toString());
		
		List<String> tempList = new ArrayList<String>();
		List deviceExceptionList = new ArrayList<HashMap<String, String>>();
		
		for(int j=0;j<rs.size();j++){
			
			Map tempUsername = (Map) rs.get(j);
			
			String rsUsername = tempUsername.get("username").toString();
			String rsSerialnumber = null;
			if(null != tempUsername.get("device_serialnumber")){
				rsSerialnumber = tempUsername.get("device_serialnumber").toString();
			}
			
			tempList.add(rsUsername);
			
			if(null==rsSerialnumber||"".equals(rsSerialnumber)){
				Map<String,String> noHas = new HashMap<String,String>();
				noHas.put("username",rsUsername);
				noHas.put("reason","该用户帐号没有绑定设备！");
				deviceExceptionList.add(noHas);
			}
		}
		
		//logger.debug(tempList);
		for(String tempUsername:userList){
			if(!tempList.contains(tempUsername)){
				
				Map<String,String> noHas = new HashMap<String,String>();
				noHas.put("username",tempUsername);
				noHas.put("reason","该用户在您的域中不存在或在ITMS中不存在！");
				deviceExceptionList.add(noHas);
				
			}
		}
		
		return deviceExceptionList;
	}
	
	/**
	 * 根据传入的List查询满足条件的数据
	 * 
	 * @param
	 * @author qxq(4174)
	 * @date 2009-02-16
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List getDeviceNormalList(String pcity_id,List<String> userList) {

		String servTypeId = null;
		String username = null;
		Set keySet = null;
		Iterator it = null;
		
		StringBuffer buffrSQL = new StringBuffer();

		List list = CityDAO.getAllNextCityIdsByCityPid(pcity_id);

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			buffrSQL.append(" select x.device_id,x.gather_id,x.city_id,x.username, x.city_name,x.user_id,x.oui,x.device_serialnumber,x.wan_type," +
					" x.device_model_id,x.devicetype_id, y.open_status ,y.serv_type_id from (");
		}
		else {
			buffrSQL.append(" select x.*, y.open_status ,y.serv_type_id from (");
		}

		buffrSQL.append(" select a.device_id,a.gather_id,b.city_id,b.username, c.city_name,b.user_id,a.oui,a.device_serialnumber,b.wan_type,")
			    .append(" a.device_model_id,a.devicetype_id ")
			    .append(" from tab_gw_device a, tab_hgwcustomer b, tab_city c where a.oui=b.oui ")
//			    .append(" and b.city_id=c.city_id and a.device_serialnumber=b.device_serialnumber and b.user_state='1' ")
			    .append(" and b.city_id=c.city_id and a.device_id=b.device_id and b.user_state='1' ")
			    .append(" and b.city_id in (")
			    .append( StringUtils.weave(list))
			    .append(" )")
			    .append(" and b.username in (");
		list = null;
		
		for(int i=0;i<userList.size();i++){
			buffrSQL.append("'");
			buffrSQL.append(userList.get(i));
			if(i==(userList.size()-1)){
				buffrSQL.append("') ");
			}else{
				buffrSQL.append("',");
			}
		}
		
		buffrSQL.append(" ) x left join hgwcust_serv_info y on x.user_id=y.user_id");
		
//		logger.debug("Z-------------:" + buffrSQL);
		
		//查询符合条件的设备
		PrepareSQL psql = new PrepareSQL(buffrSQL.toString());
		psql.getSQL();
		List<Map<String, String>> allUserList = jt.queryForList(buffrSQL.toString());
		
		List<String> usernameList = new ArrayList();
		
		List<Map<String, String>> endUsernameList1 = new ArrayList<Map<String, String>>();
		
		Map<String, String> nameStatusMap = new HashMap<String, String>();
		
		//Map<String, String> userBusinessStatusMap = getUserBusinessStatus(userList);
		
		for(Map<String, String> userMap : allUserList) {
			
			servTypeId = String.valueOf(userMap.get("serv_type_id"));
			username = String.valueOf(userMap.get("username"));
			
			if (usernameList.contains(username)) {
				
				if ("11".equals(servTypeId)) {
//					logger.debug("W-------------:" + servTypeId);
					nameStatusMap.put(username, String.valueOf(userMap.get("open_status")));
				}
				
			} else {
				usernameList.add(username);
				endUsernameList1.add(userMap);
				
			}
			
		}
		
		//if (!nameStatusMap.isEmpty()) {
			
//			logger.debug("V-------------:" + nameStatusMap);
			
			for(Map<String, String> userMap : endUsernameList1) {
				keySet = nameStatusMap.keySet();
				it = keySet.iterator();
				
				while(it.hasNext()) {
					
//					logger.debug("U-------------:" + String.valueOf(userMap.get("username")));
					
					if (it.next().equals(String.valueOf(userMap.get("username")))) {
						
//						logger.debug("T-------------:" + String.valueOf(userMap.get("username")));
						
						userMap.put("open_status", nameStatusMap.get(String.valueOf(userMap.get("username"))));
						userMap.put("serv_type_id", "11");
						
//						logger.debug("S-------------:" + userMap);
					}
					
				}
				
				if ("1".equals(String.valueOf(userMap.get("open_status"))) && "11".equals(String.valueOf(userMap.get("serv_type_id")))) {
					//已配置已成功
					userMap.put("open_status_code", "1");
				} else if ((!"1".equals(String.valueOf(userMap.get("open_status")))) && "11".equals(String.valueOf(userMap.get("serv_type_id")))) {
					//已配置未成功
					userMap.put("open_status_code", "2");
				} else {
					//未配置未成功
					userMap.put("open_status_code", "3");
				}
				
			}
			
			allUserList = null;
			nameStatusMap = null;
			keySet = null;
			it = null;
			
		//}
		
		
		
		return endUsernameList1;
	}
	
	/**
	 * 获得用户开通业务的状态
	 * @author gongsj
	 * @date 2009-9-8
	 * @param allUsernames 形式为：String str = "a,b,c"
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> getUserBusinessStatus(List<String> userList) {
		StringBuilder allUsernamesForSql = new StringBuilder();
		
		Map<String, String> userBusinessStatusMap = new HashMap<String, String>();
		
		
		for(int i=0;i<userList.size();i++){
			allUsernamesForSql.append("'");
			allUsernamesForSql.append(userList.get(i));
			if(i==(userList.size()-1)){
				allUsernamesForSql.append("'");
			}else{
				allUsernamesForSql.append("',");
			}
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("select username, open_status from hgwcust_serv_info where username in (").append(allUsernamesForSql).append(")");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		
		List<Map<String, String>> list = jt.queryForList(sql.toString());
		
		for(Map<String, String> statusMap : list) {
			userBusinessStatusMap.put(statusMap.get("username"), statusMap.get("open_status"));
		}
		
		LOG.debug("userBusinessStatusMap:{}", userBusinessStatusMap);
		
		return userBusinessStatusMap;
		
	}
	
	
	
	
	/**
	 * 获取属地下拉列表
	 * 
	 * @param 用户属地ID
	 * @author Jason(3412)
	 * @date 2008-12-17
	 * @return String
	 */
	public String getCityList(String pcityId) {
		String strSQL;
		if("00".equals(pcityId)){
			strSQL = "select city_id,city_name from tab_city";
		}else{
			strSQL = "select city_id,city_name from tab_city where parent_id='" + pcityId
				+ "' or city_id='" + pcityId + "'";
		}
		// logger.debug(strSQL);
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return getSelectList(DataSetBean.getCursor(strSQL), "city_id", "city_name", false);
	}

	/**
	 * 返回厂商下拉列表
	 * 
	 * @author Jason(3412)
	 * @date 2008-12-16
	 */
	public String getVendorList() {
		String strSQL = "select distinct vendor_name,vendor_add from tab_vendor";
		// logger.debug(strSQL);
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return getSelectList(DataSetBean.getCursor(strSQL), "vendor_name", "vendor_add", true);
	}

	/**
	 * 获取设备型号下拉列表
	 * 
	 * @param 厂商名称
	 * @author Jason(3412)
	 * @date 2008-12-16
	 * @return String
	 */
	public String getDeviceModelList(String vendor_name) {
//		String strSQL = "select distinct device_model from gw_device_model a, tab_vendor b "
//				+ " where a.oui=b.vendor_id and b.vendor_name='" + vendor_name + "'";
		String strSQL = "select device_model from gw_device_model a, tab_vendor b "
			+ " where a.vendor_id=b.vendor_id and b.vendor_name='" + vendor_name + "'";
		// logger.debug(strSQL);
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return getSelectList(DataSetBean.getCursor(strSQL), "device_model", "device_model", true);
	}

	/**
	 * 获取设备型号list
	 * 
	 * @param 厂商名称
	 * @author Jason(3412)
	 * @date 2008-12-16
	 * @return String
	 */
	public List getDevModelList(String vendor_name) {
//		String strSQL = "select distinct device_model from gw_device_model a, tab_vendor b "
//				+ " where a.oui=b.vendor_id";
		String strSQL = "select device_model from gw_device_model a, tab_vendor b "
			+ " where a.vendor_id=b.vendor_id";
		if (vendor_name != null && !"".equals(vendor_name) && !"-1".equals(vendor_name)) {
			strSQL += " and b.vendor_name='" + vendor_name + "'";
		}
		LOG.debug(strSQL);
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return jt.queryForList(strSQL, String.class);
	}

	/**
	 * 获取设备硬件版本list
	 * 
	 * @param 厂商名称
	 * @author Jason(3412)
	 * @date 2008-12-16
	 * @return String
	 */
	public List getDevHardwareList(String vendor_name, String device_model) {
//		String strSQL = "select distinct a.hardwareversion from tab_devicetype_info a, gw_device_model b,tab_vendor c"
//				+ " where a.oui=b.oui and a.oui=c.vendor_id";
		String strSQL = "select distinct a.hardwareversion from tab_devicetype_info a, gw_device_model b,tab_vendor c"
			+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id";
		if (vendor_name != null && !"".equals(vendor_name) && !"-1".equals(vendor_name)) {
			strSQL += " and c.vendor_name='" + vendor_name + "'";
		}
		if (device_model != null && !"".equals(device_model) && !"-1".equals(device_model)) {
			strSQL += " and b.device_model='" + device_model + "'";
		}
		LOG.debug(strSQL);
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return jt.queryForList(strSQL, String.class);
	}

	/**
	 * 获取设备软件版本下拉列表
	 * 
	 * @param 设备型号
	 * @author Jason(3412)
	 * @date 2008-12-16
	 * @return String
	 */
	public String getVersionList(String vendor_name, String device_model) {
//		String strSQL = "select distinct a.softwareversion from tab_devicetype_info a, gw_device_model b,tab_vendor c"
//				+ " where a.oui=b.oui and a.oui=c.vendor_id ";
		String strSQL = "select distinct a.softwareversion from tab_devicetype_info a, gw_device_model b,tab_vendor c"
			+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id ";

		// 吉林电信出现一个软件版本对应多个硬件版本，这里需要精确绑定
		if (Global.JLDX.equals(Global.instAreaShortName))
		{
			String contactStr = DBUtil.GetDB() == Global.DB_ORACLE ? "||" : "+";
			strSQL = "select a.devicetype_id, a.softwareversion " + contactStr + "'(' " + contactStr + " a.hardwareversion " + contactStr + " ')' as softwareversion  "
					+ " from tab_devicetype_info a,gw_device_model b,tab_vendor c where a.device_model_id = b.device_model_id and a.vendor_id=c.vendor_id ";

			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				strSQL = "select a.devicetype_id, concat(a.softwareversion, '(', a.hardwareversion, ')') as softwareversion  "
						+ " from tab_devicetype_info a,gw_device_model b,tab_vendor c where a.device_model_id = b.device_model_id and a.vendor_id=c.vendor_id ";
			}
		}

		if (vendor_name != null && !"".equals(vendor_name) && !"-1".equals(vendor_name)) {
			strSQL += " and c.vendor_name='" + vendor_name + "'";
		}
		if (device_model != null && !"".equals(device_model) && !"-1".equals(device_model)) {
			strSQL += " and b.device_model='" + device_model + "'";
		}

		strSQL += " order by a.softwareversion, a.hardwareversion desc";
		LOG.debug(strSQL);
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return getSelectList(DataSetBean.getCursor(strSQL), "softwareversion", "softwareversion",
				false);
	}

	/**
	 * 获取IPTV业务用户(id,open_status)
	 * 
	 * @author Jason(3412)
	 * @date 2008-12-16
	 * @return Map
	 */
	public Map getIptvUserMap() {
		String strSQL = "select user_id,open_status from hgwcust_serv_info  where serv_type_id=11";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return DataSetBean.getMap(strSQL);
	}

	/**
	 * 获取IPTV业务用户ID
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2008-12-31
	 * @return List
	 */
	public List getIptvUserList() {
		String strSQL = "select user_id from hgwcust_serv_info  where serv_type_id=11 order by user_id asc";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return jt.queryForList(strSQL, String.class);
	}

	/**
	 * 获取设备列表
	 * 
	 * @param 设备列表
	 * @author Jason(3412)
	 * @date 2008-12-16
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String getHTMLDeviceList(List<Map> deviceList) {
		StringBuffer bufferHTML = new StringBuffer();
		if (deviceList == null || deviceList.isEmpty()) {
			bufferHTML.append("<TR bgcolor='#FFFFFF'><TD colspan='10'>没有符合条件的设备！</TD></TR>");
		} else {
			bufferHTML.append("<TABLE width='98%' border=0 cellspacing=0 cellpadding=0 align='center'><TR><TD bgcolor='#999999'><table border=0 cellspacing=1 cellpadding=2 width='100%'>");
			bufferHTML.append("<TR>");
			bufferHTML.append("<TH width=\"10%\">&nbsp;选择&nbsp;</TH>");
			//bufferHTML.append("<TH width=\"10%\">&nbsp;序号&nbsp;</TH>");
			bufferHTML.append("<TH width=\"15%\">&nbsp;属地&nbsp;</TH>");
			bufferHTML.append("<TH width=\"20%\">&nbsp;宽带账号&nbsp;</TH>");
			bufferHTML.append("<TH width=\"15%\">&nbsp;OUI&nbsp;</TH>");
			bufferHTML.append("<TH width=\"20%\">&nbsp;设备序列号&nbsp;</TH>");
			bufferHTML.append("<TH width=\"10%\">&nbsp;定制状态&nbsp;</TH>");
			bufferHTML.append("</TR>");
			
			Iterator itor = deviceList.iterator();
			int num = 0;
			Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
			while (itor.hasNext()) {
				Map tmpMap = (Map) itor.next();
				
				bufferHTML.append("<TR bgcolor='#FFFFFF'>");
				bufferHTML.append("<TD align='center'><input type='checkbox' id='device_id' name='device_id' value='");
				bufferHTML.append(tmpMap.get("device_id")).append("|").append(tmpMap.get("gather_id")).append("|")
						  .append(tmpMap.get("oui")).append("|").append(tmpMap.get("device_serialnumber")).append("|") 
						  .append(tmpMap.get("username")).append("|").append(tmpMap.get("wan_type")).append("|")
						  .append(tmpMap.get("device_model_id")).append("|").append(tmpMap.get("user_id")).append("|")
						  .append(tmpMap.get("devicetype_id")).append("|").append(tmpMap.get("open_status_code")).append("'></TD>");
				//bufferHTML.append("<TD align='center'>").append(++num).append("</TD>");
				bufferHTML.append("<TD align='center'>").append(cityMap.get(tmpMap.get("city_id"))).append("</TD>");
				bufferHTML.append("<TD align='center'>").append(tmpMap.get("username")).append("</TD>");
				bufferHTML.append("<TD align='center'>").append(tmpMap.get("oui")).append("</TD><TD align='center'>");
				bufferHTML.append(tmpMap.get("device_serialnumber")).append("</TD>");
				if("1".equals(tmpMap.get("open_status_code"))) {
					bufferHTML.append("<TD align='center'><font color='blue'>已配置已成功</font></TD>");
				} else if ("2".equals(tmpMap.get("open_status_code"))) {
					bufferHTML.append("<TD align='center'><font color='red'>已配置未成功</font></TD>");
				} else {
					bufferHTML.append("<TD align='center'>未配置未成功</TD>");
				}
				
				bufferHTML.append("</TR>");
				if (++num >= 50) {
					bufferHTML.append("<TR><TD colspan='6'>您总共查询出来").append(deviceList.size()).append("条纪录(只显示前50条)</TD></TR>");
					break;
				}
				
			}
			cityMap = null;
			
		}
		bufferHTML.append("</TABLE>");
		return bufferHTML.toString();
	}

	/**
	 * 获取设备列表,String 的格式为："device_id|gather_id|oui|device_serialnumber|username|wan_type|device_model_id|user_id|devicetype_id|LINKAGE"
	 * 
	 * @param 设备信息列表
	 * @author Jason(3412)
	 * @date 2008-12-22
	 * @return List<String>
	 */
	public List<String> getSplitDeviceList(List<Map> deviceList) {
		List<String> devList = new ArrayList<String>();
		String strTmp = "";
		// Map iptvUserMap = getIptvUserMap();
		List iptvUserList = getIptvUserList();
		boolean listIsEmpty = iptvUserList.isEmpty();
		Iterator<Map> itor = deviceList.iterator();
		String itvUserId = "";
		while (itor.hasNext()) {
			Map tmpMap = itor.next();
			itvUserId = tmpMap.get("user_id").toString();
			if ((!listIsEmpty) && DeviceVendorModelDao.contains(itvUserId, iptvUserList)) {
				// 如果正在进行或者IPTV已经开户成功的，不显示
			} else {
				strTmp = tmpMap.get("device_id") + "|" + tmpMap.get("gather_id") + "|"
						+ tmpMap.get("oui") + "|" + tmpMap.get("device_serialnumber") + "|"
						+ tmpMap.get("username") + "|" + tmpMap.get("wan_type") + "|"
						+ tmpMap.get("device_model_id") + "|" + tmpMap.get("user_id") + "|"
						+ tmpMap.get("devicetype_id") + "|LINKAGE";
				devList.add(strTmp);
			}
		}
		return devList;
	}

	/**
	 * 返回 true：开户成功
	 * 
	 * @param 开户状态
	 * @author Jason(3412)
	 * @date 2008-12-16
	 * @return boolean
	 */
	public static boolean isOpen(String open_state) {
		if ("1".equals(open_state)) {
			return true;
		}
		return false;
	}

	/**
	 * 返回 true：开户成功
	 * 
	 * @param 开户状态
	 * @author Jason(3412)
	 * @date 2008-12-16
	 * @return boolean
	 */
	public static boolean isDoing(String open_state) {
		if ("2".equals(open_state)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取list的复选框
	 * 
	 * @param 设备列表
	 * @author Jason(3412)
	 * @date 2008-12-16
	 * @return String
	 */
	public String getHTMLCheckList(List<String> strList, String name) {
		StringBuffer bufferHTML = new StringBuffer();
		if (strList == null || strList.isEmpty()) {
			bufferHTML.append("没有符合条件的纪录");
		} else {
			Iterator<String> itor = strList.iterator();
			String tmpStr = "";
			while (itor.hasNext()) {
				tmpStr = itor.next();
				bufferHTML.append("<input type='checkbox' name='" + name + "' value='");
				bufferHTML.append(tmpStr + "'>" + tmpStr + "<br>");
			}
		}
		return bufferHTML.toString();
	}

	/**
	 * 获取list的复选框
	 * 
	 * @param 设备列表
	 * @author Jason(3412)
	 * @date 2008-12-16
	 * @return String
	 */
	public boolean hasDevice(String device_id) {
		
		boolean flag = false;
		
		StringBuffer bufferSql = new StringBuffer();
		
		if (null == device_id || device_id.isEmpty()) {
			
			return flag;
			
		} else {
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				bufferSql.append("select count(*) from gw_serv_strategy where service_id=1101 and device_id=");
			}
			else {
				bufferSql.append("select count(1) from gw_serv_strategy where service_id=1101 and device_id=");
			}
			bufferSql.append("'");
			bufferSql.append(device_id.trim());
			bufferSql.append("'");
		}
		PrepareSQL psql = new PrepareSQL(bufferSql.toString());
		psql.getSQL();
		int num = jt.queryForInt(bufferSql.toString());
		LOG.debug("qixueqi:device_id"+device_id);
		LOG.debug("qixueqi:num"+num);
		
		if(0<num){
			flag = true;
		}else{
			flag = false;
		}
		
		return flag;
	}
	
	
	
	/**
	 * 下拉列表
	 * 
	 * @param sor
	 *            ：结果集 name:<select name=> and <option value=>
	 *            show:<option>show</option> hashChild:<select
	 *            onchange=showChild('name')
	 * @author Jason(3412)
	 * @date 2008-12-16
	 * @return String
	 */
	public static String getSelectList(Cursor sor, String name, String show, boolean hashChild) {
		return getSelectList(sor, name, name, show, hashChild);
	}

	/**
	 * 下拉列表
	 * 
	 * @param sor
	 *            ：结果集 name:<select name=> value:<option value=>
	 *            show:<option>show</option> hashChild:<select
	 *            onchange=showChild('name')
	 * @author Jason(3412)
	 * @date 2008-12-16
	 * @return String
	 */
	public static String getSelectList(Cursor sor, String name, String value, String show,
			boolean hashChild) {
		return FormUtil.createListBox(sor, value, show, hashChild, "", name, true);
	}

	/**
	 * 初始化数据库连接
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}
	
	/**
	 * 判断字符串是否在List中存在(二分查找)
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2008-12-11
	 * @return boolean
	 */
	public static boolean contains(String user, List userList) {
		if (user == null || userList == null || userList.isEmpty()) {
			return false;
		}
		int length = userList.size();
		int min = 0;
		int max = length--;
		int midden = (min + max) / 2;
		int result = 0;
		String username = "";
		while (min <= midden && midden <= max) {
			username = (String) userList.get(midden);
			result = user.compareToIgnoreCase(username);
			if (result == 0) {
				return true;
			}
			if (midden == min || midden == max) {
				break;
			} else {
				if (result > 0) {
					min = midden;
				} else if (result < 0) {
					max = midden;
				}
			}
			midden = (min + max) / 2;
		}
		return false;
	}
}
