package com.linkage.module.gtms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

public class ImportQueryDAOImp extends SuperDAO {

	private static Logger logger = LoggerFactory.getLogger(ImportQueryDAOImp.class);

	private Map<String, String> vendorMap;
	private Map<String, String> modelMap;
	private Map<String, String> usertype;
	private Map<String, String> cityMap;
	private Map<String, String> errorMap;

	public ImportQueryDAOImp(){
		vendorMap = null;
		modelMap = null;
		usertype = null;
		cityMap = null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getUserType() {
		Map<String, String> map = new HashMap<String, String>();
		PrepareSQL pSQL = new PrepareSQL(
				"select id,spec_name from tab_bss_dev_port where 1=1");
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(pSQL.getSQL());
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				map.put(StringUtil.getStringValue(list.get(i).get("id")),
						StringUtil.getStringValue(list.get(i).get("spec_name")));
			}
		}
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getVendorMap() {
		Map<String, String> vendorMap = null;
		if (vendorMap == null) {
			vendorMap = new HashMap<String, String>();
			PrepareSQL psql = new PrepareSQL(
					"select vendor_id, vendor_name, vendor_add from tab_vendor order by vendor_add");
			List<Map> list = new ArrayList<Map>();
			list = jt.queryForList(psql.getSQL());
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					String vendor_add = StringUtil.getStringValue(list.get(i).get("vendor_add"));
					if (vendor_add != null && !"".equals(vendor_add)) {
						vendorMap.put(StringUtil.getStringValue(list.get(i).get("vendor_id")), vendor_add);
					} else {
						vendorMap.put(StringUtil.getStringValue(list.get(i).get("vendor_id")),list.get(i).get("vendor_name").toString());
					}
				}
			}
		}
		return vendorMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getDeviceModel() {
		logger.debug("DeviceServiceDAO=>getDeviceModel()");
		Map<String, String> modelMap = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL(
				"select device_model_id,device_model from gw_device_model");
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				modelMap.put(
						StringUtil.getStringValue(list.get(i).get(
								"device_model_id")),
						StringUtil.getStringValue(list.get(i).get(
								"device_model")));
			}
		}
		return modelMap;
	}

	@SuppressWarnings("rawtypes")
	public Map getInfo(String loid, String deviceSerialnumber,
			String interAccount, String itvAccount, String vocieAccount) {
		Map<String, String> rmap = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select c.city_id,a.device_serialnumber,c.username,b.vendor_id,b.device_model_id,"
				+ "b.hardwareversion,b.softwareversion,b.spec_id dev_spec_id,c.spec_id user_spec_id ");
		if (!StringUtil.IsEmpty(loid)) {// TODO wait (more table related)
			sql.append(" from tab_gw_device a,tab_devicetype_info b, tab_hgwcustomer c "
					+ "where a.devicetype_id = b.devicetype_id and a.device_id = c.device_id and c.username ='"
					+ loid + "'");
			rmap.put("default", loid);
		}
		if (!StringUtil.IsEmpty(deviceSerialnumber)) {// TODO wait (more table related)
			sql.append(" from tab_gw_device a,tab_devicetype_info b, tab_hgwcustomer c "
					+ "where a.devicetype_id = b.devicetype_id and a.device_id = c.device_id and a.device_serialnumber ='"
					+ deviceSerialnumber + "'");
			rmap.put("default", deviceSerialnumber);
		}
		if (!StringUtil.IsEmpty(interAccount)) {// TODO wait (more table related)
			sql.append(" from tab_gw_device a,tab_devicetype_info b, tab_hgwcustomer c,"
					+ "hgwcust_serv_info d where a.devicetype_id = b.devicetype_id and a.device_id = c.device_id "
					+ "and c.user_id = d.user_id and d.username ='"
					+ interAccount + "' and d.serv_type_id = 10");
			rmap.put("default", interAccount);
		}
		if (!StringUtil.IsEmpty(itvAccount)) {// TODO wait (more table related)
			sql.append(" from tab_gw_device a,tab_devicetype_info b, tab_hgwcustomer c,"
					+ "hgwcust_serv_info d where a.devicetype_id = b.devicetype_id and a.device_id = c.device_id "
					+ "and c.user_id = d.user_id and d.username ='"
					+ itvAccount + "' and d.serv_type_id = 11 ");
			rmap.put("default", itvAccount);
		}
		if (!StringUtil.IsEmpty(vocieAccount)) {// TODO wait (more table related)
			sql.append(" from tab_gw_device a,tab_devicetype_info b, tab_hgwcustomer c,"
					+ "tab_voip_serv_param d where a.devicetype_id = b.devicetype_id and a.device_id = c.device_id "
					+ "and c.user_id = d.user_id and d.voip_phone='"
					+ vocieAccount + "' ");
			rmap.put("default", vocieAccount);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		if (null == vendorMap) {vendorMap = getVendorMap();}
		if (null == modelMap) {modelMap = getDeviceModel();}
		if (null == cityMap) {cityMap = Global.G_CityId_CityName_Map;}
		if (null == usertype) {usertype = getUserType();}
		try {
			Map tmap = jt.queryForMap(psql.getSQL());
			if (tmap != null)
			{
				String cityid = StringUtil.getStringValue(tmap.get("city_id"));
				rmap.put("city_name", cityMap.get(cityid));
				rmap.put("device_serialnumber",StringUtil.getStringValue(tmap.get("device_serialnumber")));
				rmap.put("username",StringUtil.getStringValue(tmap.get("username")));
				String vendor_id = StringUtil.getStringValue(tmap.get("vendor_id"));
				rmap.put("vendor", vendorMap.get(vendor_id));
				String device_model_id = StringUtil.getStringValue(tmap.get("device_model_id"));
				rmap.put("device_model", modelMap.get(device_model_id));
				rmap.put("hardwareversion",StringUtil.getStringValue(tmap.get("hardwareversion")));
				rmap.put("softwareversion",StringUtil.getStringValue(tmap.get("softwareversion")));
				String dev_spec_id = StringUtil.getStringValue(tmap.get("dev_spec_id"));
				rmap.put("dev_spec_id", usertype.get(dev_spec_id));
				String user_spec_id = StringUtil.getStringValue(tmap.get("user_spec_id"));
				rmap.put("user_spec_id", usertype.get(user_spec_id));
			}
		} catch (DataAccessException e) {
			rmap = null;
			errorMap = new HashMap<String, String>();
			if (!StringUtil.IsEmpty(loid)) {
				errorMap.put("default", loid);
			}
			if (!StringUtil.IsEmpty(deviceSerialnumber)) {
				errorMap.put("default", deviceSerialnumber);
			}
			if (!StringUtil.IsEmpty(interAccount)) {
				errorMap.put("default", interAccount);
			}
			if (!StringUtil.IsEmpty(itvAccount)) {
				errorMap.put("default", itvAccount);
			}
			if (!StringUtil.IsEmpty(vocieAccount)) {
				errorMap.put("default", vocieAccount);
			}
		}
		return rmap;
	}

	public List<Map> getInfoList(int curPage_splitPage,int numPage_splitPage,String filename,String fieldname){
		Map<String, String> rmap = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select c.city_id,a.device_serialnumber,c.username,b.vendor_id,b.device_model_id,"
				+ "b.hardwareversion,b.softwareversion,b.spec_id dev_spec_id,c.spec_id user_spec_id ");
		if (fieldname.equals("username")) {// TODO wait (more table related)
			sql.append(",d.username as tmpname,a.device_serialnumber as isnull from tab_batchdevout_tmp d left join tab_hgwcustomer c on c.username = d.username left join tab_gw_device a on a.device_id = c.device_id left join tab_devicetype_info b on a.devicetype_id = b.devicetype_id "
					+ "where d.filename ='"
					+ filename + "'");
//			rmap.put("default", loid);
		}
		if (fieldname.equals("devicesn")) {// TODO wait (more table related)
			sql.append(",d.devicesn as tmpname,c.city_id as isnull from tab_batchdevout_tmp d left join tab_gw_device a on a.device_serialnumber = d.devicesn left join tab_devicetype_info b on a.devicetype_id = b.devicetype_id  "
					+ " left join  tab_hgwcustomer c on a.device_id = c.device_id where d.filename ='"
					+ filename + "'");
//			rmap.put("default", deviceSerialnumber);
		}
		if (fieldname.equals("netaccount")) {// TODO wait (more table related)
			sql.append(",e.netaccount as tmpname,a.device_serialnumber as isnull from tab_batchdevout_tmp e left join hgwcust_serv_info d on d.username = e.netaccount left join tab_hgwcustomer c on c.user_id = d.user_id left join tab_gw_device a on a.device_id = c.device_id left join tab_devicetype_info b on a.devicetype_id = b.devicetype_id "
					+ "where d.serv_type_id = 10 and e.filename ='"
					+ filename + "' ");
//			rmap.put("default", interAccount);
		}
		if (fieldname.equals("itvaccount")) {// TODO wait (more table related)
			sql.append(",e.itvaccount as tmpname,a.device_serialnumber as isnull from tab_batchdevout_tmp e left join hgwcust_serv_info d on d.username = e.itvaccount left join tab_hgwcustomer c on c.user_id = d.user_id left join tab_gw_device a on a.device_id = c.device_id left join tab_devicetype_info b on a.devicetype_id = b.devicetype_id "
					+ "where d.serv_type_id = 11 and e.filename ='"
					+ filename + "' ");
//			rmap.put("default", itvAccount);
		}
		if (fieldname.equals("voiceaccount")) {// TODO wait (more table related)
			sql.append(",e.voiceaccount as tmpname,a.device_serialnumber as isnull from tab_batchdevout_tmp e left join tab_voip_serv_param d on d.voip_phone = e.voiceaccount left join tab_hgwcustomer c on c.user_id = d.user_id left join tab_gw_device a on a.device_id = c.device_id left join tab_devicetype_info b on a.devicetype_id = b.devicetype_id "
					+ " where e.filename='"
					+ filename + "' ");
//			rmap.put("default", vocieAccount);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		if (null == vendorMap) {vendorMap = getVendorMap();}
		if (null == modelMap) {modelMap = getDeviceModel();}
		if (null == cityMap) {cityMap = Global.G_CityId_CityName_Map;}
		if (null == usertype) {usertype = getUserType();}

		return querySP(psql.getSQL(), (curPage_splitPage - 1) * numPage_splitPage + 1,
				numPage_splitPage, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, String> rmap = new HashMap<String, String>();
						String isnull = StringUtil.getStringValue(rs.getString("isnull"));
						rmap.put("default", StringUtil.getStringValue(rs.getString("tmpname")));
						if(isnull == null || "".equals(isnull)){
							rmap.put("city_name", "查无数据");
							rmap.put("device_serialnumber","");
							rmap.put("username","");
							rmap.put("vendor", "");
							rmap.put("device_model", "");
							rmap.put("hardwareversion","");
							rmap.put("softwareversion","");
							rmap.put("dev_spec_id", "");
							rmap.put("user_spec_id", "");
						}else{
							String cityid = StringUtil.getStringValue(rs.getString("city_id"));
							rmap.put("city_name", cityMap.get(cityid));
							rmap.put("device_serialnumber",StringUtil.getStringValue(rs.getString("device_serialnumber")));
							rmap.put("username",StringUtil.getStringValue(rs.getString("username")));
							String vendor_id = StringUtil.getStringValue(rs.getString("vendor_id"));
							rmap.put("vendor", vendorMap.get(vendor_id));
							String device_model_id = StringUtil.getStringValue(rs.getString("device_model_id"));
							rmap.put("device_model", modelMap.get(device_model_id));
							rmap.put("hardwareversion",StringUtil.getStringValue(rs.getString("hardwareversion")));
							rmap.put("softwareversion",StringUtil.getStringValue(rs.getString("softwareversion")));
							String dev_spec_id = StringUtil.getStringValue(rs.getString("dev_spec_id"));
							rmap.put("dev_spec_id", usertype.get(dev_spec_id));
							String user_spec_id = StringUtil.getStringValue(rs.getString("user_spec_id"));
							rmap.put("user_spec_id", usertype.get(user_spec_id));
						}

						return rmap;
					}
				});

	}

	@SuppressWarnings("rawtypes")
	public Map getErrorData() {
		if (errorMap != null) {
			errorMap.put("city_name", "查无数据");
			return errorMap;
		}else {
			return null;
		}
	}

	public int insertTmp(String filename,List<String> accounts,String field){
		logger.warn("ImportQuerDAOImp--insertTmpTable({},{},{})",new Object[]{filename,accounts.size(),field});
		ArrayList<String> sqlList = new ArrayList<String>();
		String deleteSql = "delete from tab_batchdevout_tmp where filename = '" + filename + "' and " + field + " is not null";
		PrepareSQL deletePSql = new PrepareSQL(deleteSql);
		sqlList.add(deletePSql.getSQL());
		PrepareSQL psql = null;
		if("username".equals(field)){
			for (int i = 0;i<accounts.size();i++)
			{
				psql = new PrepareSQL();
				psql.append("insert into tab_batchdevout_tmp" + "(filename,username)"+" values ('" + filename + "','"  + accounts.get(i) +"')");
				sqlList.add(psql.getSQL());
			}
		} else if("devicesn".equals(field)){
			for (int i = 0;i<accounts.size();i++)
			{
				psql = new PrepareSQL();
				psql.append("insert into tab_batchdevout_tmp" + "(filename,devicesn)"+" values ('" + filename + "','"  + accounts.get(i) +"')");
				sqlList.add(psql.getSQL());
			}
		} else if("netaccount".equals(field)){
			for (int i = 0;i<accounts.size();i++)
			{
				psql = new PrepareSQL();
				psql.append("insert into tab_batchdevout_tmp" + "(filename,netaccount)"+" values ('" + filename + "','"  + accounts.get(i) +"')");
				sqlList.add(psql.getSQL());
			}
		} else if("itvaccount".equals(field)){
			for (int i = 0;i<accounts.size();i++)
			{
				psql = new PrepareSQL();
				psql.append("insert into tab_batchdevout_tmp" + "(filename,itvaccount)"+" values ('" + filename + "','"  + accounts.get(i) +"')");
				sqlList.add(psql.getSQL());
			}
		} else if("voiceaccount".equals(field)){
			for (int i = 0;i<accounts.size();i++)
			{
				psql = new PrepareSQL();
				psql.append("insert into tab_batchdevout_tmp" + "(filename,voiceaccount)"+" values ('" + filename + "','"  + accounts.get(i) +"')");
				sqlList.add(psql.getSQL());
			}
		}

		int res ;
		if(LipossGlobals.inArea(Global.JSDX))
		{
			res = DBOperation.executeUpdate(sqlList,"proxool.xml-report");
//			res = DBOperation.executeUpdate(sqlList);
		}
		else
		{
			res = DBOperation.executeUpdate(sqlList);
		}

		return res;
	}

	public int getCountTotals(int num_splitPage,String filename,String fieldname){
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) num ");
		if (fieldname.equals("username")) {// TODO wait (more table related)
			sql.append("from tab_batchdevout_tmp d left join tab_hgwcustomer c on c.username = d.username left join tab_gw_device a on a.device_id = c.device_id left join tab_devicetype_info b on a.devicetype_id = b.devicetype_id "
					+ "where d.filename ='"
					+ filename + "'");
		}
		if (fieldname.equals("devicesn")) {// TODO wait (more table related)
			sql.append("from tab_batchdevout_tmp d left join tab_gw_device a on a.device_serialnumber = d.devicesn left join tab_devicetype_info b on a.devicetype_id = b.devicetype_id  "
					+ " left join  tab_hgwcustomer c on a.device_id = c.device_id where d.filename ='"
					+ filename + "'");
		}
		if (fieldname.equals("netaccount")) {// TODO wait (more table related)
			sql.append("from tab_batchdevout_tmp e left join hgwcust_serv_info d on d.username = e.netaccount left join tab_hgwcustomer c on c.user_id = d.user_id left join tab_gw_device a on a.device_id = c.device_id left join tab_devicetype_info b on a.devicetype_id = b.devicetype_id "
					+ "where e.filename ='"
					+ filename + "' and d.serv_type_id = 10");
		}
		if (fieldname.equals("itvaccount")) {// TODO wait (more table related)
			sql.append("from tab_batchdevout_tmp e left join hgwcust_serv_info d on d.username = e.itvaccount left join tab_hgwcustomer c on c.user_id = d.user_id left join tab_gw_device a on a.device_id = c.device_id left join tab_devicetype_info b on a.devicetype_id = b.devicetype_id "
					+ "where e.filename ='"
					+ filename + "' and d.serv_type_id = 11 ");
		}
		if (fieldname.equals("voiceaccount")) {// TODO wait (more table related)
			sql.append("from tab_batchdevout_tmp e left join tab_voip_serv_param d on d.voip_phone = e.netaccount left join tab_hgwcustomer c on c.user_id = d.user_id left join tab_gw_device a on a.device_id = c.device_id left join tab_devicetype_info b on a.devicetype_id = b.devicetype_id "
					+ " where e.filename='"
					+ filename + "' ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());

		int total = 0;//jt.queryForInt(sql.toString());
		Map tmp = DBOperation.getRecord(psql.getSQL(),"proxool.xml-report");
		total = StringUtil.getIntValue(tmp, "num");
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}

		return maxPage;
	}

	public int getCountTotalsBak(int num_splitPage,String filename,String fieldname){
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) num ");
		if (fieldname.equals("username")) {// TODO wait (more table related)
			sql.append(" from tab_gw_device a,tab_devicetype_info b, tab_hgwcustomer c,tab_batchdevout_tmp d "
					+ "where a.devicetype_id = b.devicetype_id and a.device_id = c.device_id and c.username = d.username and d.filename ='"
					+ filename + "'");
		}
		if (fieldname.equals("devicesn")) {// TODO wait (more table related)
			sql.append(" from tab_gw_device a,tab_devicetype_info b, tab_hgwcustomer c, tab_batchdevout_tmp d "
					+ "where a.devicetype_id = b.devicetype_id and a.device_id = c.device_id and a.device_serialnumber = d.devicesn and d.filename ='"
					+ filename + "'");
		}
		if (fieldname.equals("netaccount")) {// TODO wait (more table related)
			sql.append(" from tab_gw_device a,tab_devicetype_info b, tab_hgwcustomer c,"
					+ "hgwcust_serv_info d, tab_batchdevout_tmp e where a.devicetype_id = b.devicetype_id and a.device_id = c.device_id "
					+ "and c.user_id = d.user_id and d.username = e.netaccount and e.filename ='"
					+ filename + "' and d.serv_type_id = 10");
		}
		if (fieldname.equals("itvaccount")) {// TODO wait (more table related)
			sql.append(" from tab_gw_device a,tab_devicetype_info b, tab_hgwcustomer c,"
					+ "hgwcust_serv_info d, tab_batchdevout_tmp e where a.devicetype_id = b.devicetype_id and a.device_id = c.device_id "
					+ "and c.user_id = d.user_id and d.username = e.netaccount and e.filename ='"
					+ filename + "' and d.serv_type_id = 11 ");
		}
		if (fieldname.equals("voiceaccount")) {// TODO wait (more table related)
			sql.append(" from tab_gw_device a,tab_devicetype_info b, tab_hgwcustomer c,"
					+ "tab_voip_serv_param d, tab_batchdevout_tmp e  where a.devicetype_id = b.devicetype_id and a.device_id = c.device_id "
					+ "and c.user_id = d.user_id and d.voip_phone = e.voiceaccount and e.filename='"
					+ filename + "' ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());

		int total = 0;//jt.queryForInt(sql.toString());
		Map tmp = DBOperation.getRecord(psql.getSQL(),"proxool.xml-report");
		total = StringUtil.getIntValue(tmp, "num");
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}

		return maxPage;
	}
}
