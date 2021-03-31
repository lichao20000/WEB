
package dao.resource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.system.utils.DateTimeUtil;

import dao.share.ServPackageDAO;

@SuppressWarnings("unchecked")
public class ExportUserDAO
{

	/** log */
	private static final Logger LOG = LoggerFactory.getLogger(ExportUserDAO.class);
	// jdbc模板
	private JdbcTemplate jt;
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;
	/**
	 * Map<serv_package_id,serv_package_name>
	 */
	private Map<String, String> packageMap = null;
	private Map<String, String> userTypeMap = null;
	// 导入的资源类型 0：家庭网关 1：企业网关
	private String infoType = "";

	/**
	 * 返回需要导出的excel文件内容标题
	 * 
	 * @return
	 */
	public String[] getTitle(String infoType, String bindState)
	{
		LOG.debug("getTitle({})", infoType);
		String[] title;
		if ("1".equals(infoType))
		{
			if (true == LipossGlobals.isXJDX())
			{
				title = new String[] { "属地", "用户来源", "用户帐号", "开户时间", "绑定设备", "绑定时间",
						"工单受理时间" };
			}
			else
			{
				if ("1".equals(bindState))
				{
					if (Global.JSDX.equals(Global.instAreaShortName))
					{
						title = new String[] { "属地", "用户来源", "用户帐号", "开户时间", "套餐",
								"是否开启iTV", "工单受理时间" };
					}
					else
					{
						title = new String[] { "属地", "用户来源", "用户帐号", "开户时间", "套餐",
								"工单受理时间" };
					}
				}
				else
				{
					title = new String[] { "属地", "用户来源", "用户帐号", "开户时间", "套餐", "绑定设备",
							"绑定时间", "工单受理时间" };
				}
			}
		}
		else
		{
			if (true == LipossGlobals.isXJDX())
			{
				title = new String[] { "属地", "用户来源", "用户帐号", "开户时间", "绑定设备", "绑定时间",
						"工单受理时间" };
			}
			else
			{
				if ("1".equals(bindState))
				{
					title = new String[] { "属地", "用户来源", "用户帐号", "开户时间", "套餐", "工单受理时间" };
				}
				else
				{
					title = new String[] { "属地", "用户来源", "用户帐号", "开户时间", "套餐", "绑定设备",
							"绑定时间", "工单受理时间" };
				}
			}
		}
		return title;
	}

	/**
	 * 返回需要导出的excel文件字段
	 * 
	 * @return
	 */
	public String[] getColumn(String infoType, String bindState)
	{
		LOG.debug("getColumn()");
		String[] column;
		if ("1".equals(infoType))
		{
			if (true == LipossGlobals.isXJDX())
			{
				column = new String[] { "city_name", "user_type", "username", "opendate",
						"device", "binddate", "dealdate" };
			}
			else
			{
				if ("1".equals(bindState))
				{
					if (Global.JSDX.equals(Global.instAreaShortName))
					{
						column = new String[] { "city_name", "user_type", "username",
								"opendate", "serv_package_name", "iTV", "dealdate" };
					}
					else
					{
						column = new String[] { "city_name", "user_type", "username",
								"opendate", "serv_package_name", "dealdate" };
					}
				}
				else
				{
					column = new String[] { "city_name", "user_type", "username",
							"opendate", "serv_package_name", "device", "binddate",
							"dealdate" };
				}
			}
		}
		else
		{
			if (true == LipossGlobals.isXJDX())
			{
				column = new String[] { "city_name", "user_type", "username", "opendate",
						"device", "binddate", "dealdate" };
			}
			else
			{
				if ("1".equals(bindState))
				{
					column = new String[] { "city_name", "user_type", "username",
							"opendate", "serv_package_name", "dealdate" };
				}
				else
				{
					column = new String[] { "city_name", "user_type", "username",
							"opendate", "serv_package_name", "device", "binddate",
							"dealdate" };
				}
			}
		}
		return column;
	}

	/**
	 * 查询所有没有绑定设备的用户
	 * 
	 * @return
	 */
	// @SuppressWarnings("unchecked")
	// public List<Map> getAllUserInfo(String type){
	//
	// infoType = type;
	//		
	// //初始化地市信息
	// CityAct cityAct = new CityAct();
	// cityMap = cityAct.getCityInfoMap();
	//		
	// //查询所有的未绑定设备的用户
	// String sql = "";
	//		
	// if ("1".equals(infoType)){
	// sql = "select * from tab_egwcustomer where (oui = '' or oui = null) and
	// (device_serialnumber = '' or device_serialnumber = null) and user_state =
	// '1'";
	// }
	// else{
	// sql = "select * from tab_hgwcustomer where (oui = '' or oui = null) and
	// (device_serialnumber = '' or device_serialnumber = null) and user_state =
	// '1'";
	// }
	//		
	// List<Map> list = jt.query(sql,new RowMapper(){
	// public Object mapRow(ResultSet rs, int index) throws SQLException {
	// Map<String,String> map = new HashMap<String,String>();
	//				
	// map.put("username", rs.getString("username"));
	// map.put("phonenumber", rs.getString("phonenumber"));
	//				
	//				
	// String city_name = (String)cityMap.get(rs.getString("city_id"));
	// if (city_name != null){
	// map.put("city_id", city_name);
	// }
	// else{
	// map.put("city_id", "");
	// }
	//				
	// return map;
	// }
	// });
	//		
	// return list;
	// }
	/**
	 * 不含分页功能 查询用户 type 网关类型，用于判断是家庭网关：1 还是企业网关：2 cityId用户的属地，用于获得其属地内的 用户 username
	 * 用户的用户名，用于根据用户名进行查询 绑定状态bindState 未绑定：1 绑定：2 事件类型timeTpe 工单受理时间：1 绑定时间：2
	 * 未绑定状态下用户设备ouiState 设备OUI为空：1 未绑定设备：2 所有：-1
	 * 
	 * @author zhaixf
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getQueryUserInfo(String type, ArrayList cityIdList, String username,
			String startTime, String endTime, String packageId, String timeType,
			String bindState, String ouiState)
	{
		LOG.debug("getQueryUserInfo({}, {}, {}, {}, {}, {}, {}, {}, {})",
				new Object[] { type, cityIdList, username, startTime, endTime, packageId,
						timeType, bindState, ouiState });
		List<Map> list;
		infoType = type;
		String sql = null;
		// 查询企业网关的用户
		if ("2".equals(infoType))
		{
			sql = "select a.username,b.city_id,a.opendate,c.serv_package_id,a.user_type_id,a.device_serialnumber,a.oui,a.binddate,a.dealdate,a.user_id"
					+ " from tab_customerinfo b,tab_egwcustomer a left join gw_cust_user_package c"
					+ "  on a.user_id = c.user_id where"
					+ " a.customer_id=b.customer_id and a.user_state in('1','2')";
			// 绑定状态  1 未绑定   2  绑定
			if ("1".equals(bindState))
			{
				if (LipossGlobals.isOracle()) {
					sql += " and a.device_id is null ";
				}else {
					sql += " and (a.device_id = '' or a.device_id is null)";
				}
				// 用户设备
				if ("1".equals(ouiState))
				{
					if (LipossGlobals.isOracle()) {
						sql += " and a.device_serialnumber is null ";
					}else {
						sql += " and (a.device_serialnumber = '' or a.device_serialnumber is null)";
					}
				}
				if ("2".equals(ouiState))
				{
					if (LipossGlobals.isOracle()) {
						sql += " and a.device_serialnumber is not null ";
					}else {
						sql += " and (a.device_serialnumber <> '' and a.device_serialnumber is not null)";
					}
				}
			}
			if ("2".equals(bindState))
			{
				if (LipossGlobals.isOracle()) {
					sql += " and a.device_id is not null ";
				}else {
					sql += " and (a.device_id <> '' and a.device_id is not null)";
				}
			}
			// 工单受理时间
			if ("1".equals(timeType))
			{
				if (startTime != null)
				{
					sql += " and a.dealdate>=" + startTime;
				}
				if (endTime != null)
				{
					sql += " and a.dealdate<" + endTime;
				}
			}
			// 绑定时间
			if ("2".equals(timeType))
			{
				if (startTime != null)
				{
					sql += " and a.binddate>=" + startTime;
				}
				if (endTime != null)
				{
					sql += " and a.binddate<" + endTime;
				}
			}
			// 开户时间
			if ("3".equals(timeType))
			{
				if (startTime != null)
				{
					sql += " and a.opendate>=" + startTime;
				}
				if (endTime != null)
				{
					sql += " and a.opendate<" + endTime;
				}
			}
			if (username != null)
			{
				sql += " and a.username='" + username + "'";
			}
			if (cityIdList != null)
			{
				sql += " and b.city_id in (";
				for (int i = 0; i < cityIdList.size(); i++)
				{
					sql += "'" + cityIdList.get(i) + "',";
				}
				sql += "'')";
			}
			if (packageId != null && packageId.equals("-1") == false)
			{
				sql += " and c.serv_package_id='" + packageId + "'";
			}
		}
		else
		{
			// sql = "select username,city_id,opendate,serv_package_id"
			// + " from tab_hgwcustomer a left join gw_cust_user_package b"
			// + " on b.user_id = a.user_id"
			// + " where a.user_id not in(select y.user_id from tab_gw_device x,
			// tab_hgwcustomer y "
			// +"where x.cpe_allocatedstatus=1 and x.oui = y.oui and x.device_serialnumber
			// = y.device_serialnumber "
			// +"and y.user_state in( '1','2')) and a.user_state in( '1','2')";
			if (Global.JSDX.equals(Global.instAreaShortName))
			{
				sql = "select a.username,a.city_id,a.opendate,b.serv_package_id,a.user_type_id,a.device_serialnumber,a.oui,a.binddate,a.dealdate,a.user_id,a.gather_id,c.serv_status"
						+ " from tab_hgwcustomer a "
						+ " left join gw_cust_user_package b on b.user_id = a.user_id left join tab_iptv_user c on a.user_id=c.user_id"
						+ " where a.user_state in( '1','2')";
			}
			else
			{
				sql = "select a.username,a.city_id,a.opendate,b.serv_package_id,a.user_type_id,a.device_serialnumber,a.oui,a.binddate,a.dealdate,a.user_id,a.gather_id"
						+ " from tab_hgwcustomer a "
						+ " left join gw_cust_user_package b on b.user_id = a.user_id "
						+ " where a.user_state in( '1','2')";
			}
			// 绑定状态
			if ("1".equals(bindState))
			{
				if (LipossGlobals.isOracle()) {
					sql += " and a.device_id is null ";
				}else {
					sql += " and (a.device_id = '' or a.device_id is null)";
				}
				// 用户设备
				if ("1".equals(ouiState))
				{
					if (LipossGlobals.isOracle()) {
						sql += " and a.device_serialnumber is null ";
					}else {
						sql += " and (a.device_serialnumber = '' or a.device_serialnumber is null)";
					}
				}
				if ("2".equals(ouiState))
				{
					if (LipossGlobals.isOracle()) {
						sql += " and a.device_serialnumber is not null ";
					}else{
						sql += " and (a.device_serialnumber <> '' and a.device_serialnumber is not null)";
					}
				}
			}
			if ("2".equals(bindState))
			{
				if (LipossGlobals.isOracle()) {
					sql += " and a.device_id is not null ";
				}else{
					sql += " and (a.device_id <> '' and a.device_id is not null)";
				}
			}
			// 工单受理时间
			if ("1".equals(timeType))
			{
				if (startTime != null)
				{
					sql += " and a.dealdate>=" + startTime;
				}
				if (endTime != null)
				{
					sql += " and a.dealdate<" + endTime;
				}
			}
			// 绑定时间
			if ("2".equals(timeType))
			{
				if (startTime != null)
				{
					sql += " and a.binddate>=" + startTime;
				}
				if (endTime != null)
				{
					sql += " and a.binddate<" + endTime;
				}
			}
			// 开户时间
			if ("3".equals(timeType))
			{
				if (startTime != null)
				{
					sql += " and a.opendate>=" + startTime;
				}
				if (endTime != null)
				{
					sql += " and a.opendate<" + endTime;
				}
			}
			if (username != null)
			{
				sql += " and a.username='" + username + "'";
			}
			if (cityIdList != null)
			{
				sql += " and a.city_id in (";
				for (int i = 0; i < cityIdList.size(); i++)
				{
					sql += "'" + cityIdList.get(i) + "',";
				}
				sql += "'')";
			}
			if (packageId != null && packageId.equals("-1") == false)
			{
				sql += " and b.serv_package_id='" + packageId + "'";
			}
		}
		sql += " order by  city_id,a.user_type_id";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		cityMap = CityDAO.getCityIdCityNameMap();
		ServPackageDAO servPackageDAO = new ServPackageDAO();
		packageMap = servPackageDAO.getPackageIdNameMap();
		userTypeMap = getUserType();
		list = jt.query(sql, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int index) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				String city_name;
				String serv_package_name;
				long time = 0;
				DateTimeUtil dateTimeUtil = null;
				map.put("username", rs.getString("username"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("oui", rs.getString("oui"));
				map.put("device", rs.getString("oui") + "-"
						+ rs.getString("device_serialnumber"));
				map.put("user_id", rs.getString("user_id"));
				city_name = (String) cityMap.get(rs.getString("city_id"));
				if (city_name != null)
				{
					map.put("city_name", city_name);
				}
				else
				{
					map.put("city_name", "");
				}
				serv_package_name = (String) packageMap.get(rs
						.getString("serv_package_id"));
				if (serv_package_name != null)
				{
					map.put("serv_package_name", serv_package_name);
				}
				else
				{
					map.put("serv_package_name", "");
				}
				// 用户来源
				String user_type_id = rs.getString("user_type_id");
				String tmp = "手工添加";
				if (false == StringUtil.IsEmpty(user_type_id))
				{
					tmp = userTypeMap.get(user_type_id);
					if (true == StringUtil.IsEmpty(tmp))
					{
						tmp = "其他";
					}
				}
				map.put("user_type", tmp);
				try
				{
					time = Long.parseLong(rs.getString("opendate"));
					dateTimeUtil = new DateTimeUtil(time * 1000);
					map.put("opendate", dateTimeUtil.getDate());
				}
				catch (NumberFormatException e)
				{
					map.put("opendate", "");
				}
				catch (Exception e)
				{
					map.put("opendate", "");
				}
				// 将binddate转换成时间
				try
				{
					time = Long.parseLong(rs.getString("binddate"));
					dateTimeUtil = new DateTimeUtil(time * 1000);
					map.put("binddate", dateTimeUtil.getDate());
				}
				catch (NumberFormatException e)
				{
					map.put("binddate", "");
				}
				catch (Exception e)
				{
					map.put("binddate", "");
				}
				// 将dealdate转换成时间
				try
				{
					time = Long.parseLong(rs.getString("dealdate"));
					dateTimeUtil = new DateTimeUtil(time * 1000);
					map.put("dealdate", dateTimeUtil.getDate());
				}
				catch (NumberFormatException e)
				{
					map.put("dealdate", "");
				}
				catch (Exception e)
				{
					map.put("dealdate", "");
				}
				if (LipossGlobals.IsITMS())
				{
					if (Global.JSDX.equals(Global.instAreaShortName))
					{
						String serv_status = rs.getString("serv_status");
						if ("1".equals(serv_status))
						{
							map.put("iTV", "是");
						}
						else
						{
							map.put("iTV", "否");
						}
					}
				}
				return map;
			}
		});
		cityMap = null;
		packageMap = null;
		userTypeMap = null;
		return list;
	}

	/**
	 * 含有分页功能,每页现实20条 查询用户 type 网关类型，用于判断是家庭网关：1 还是企业网关：2 cityId 用户的属地，用于获得其属地内的 用户
	 * username 用户的用户名，用于根据用户名进行查询 绑定状态bindState 未绑定：1 绑定：2 事件类型timeTpe 工单受理时间：1 绑定时间：2
	 * 未绑定状态下用户设备ouiState 设备OUI为空：1 未绑定设备：2 所有：-1
	 * 
	 * @author zhaixf
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getQueryPageUserInfo(String type, ArrayList cityIdList,
			String username, String startTime, String endTime, String packageId,
			String timeType, String bindState, String ouiState, String offset)
	{
		LOG.debug("getQueryPageUserInfo({}, {}, {}, {}, {}, {}, {}, {}, {}, {})",
				new Object[] { type, cityIdList, username, startTime, endTime, packageId,
						timeType, bindState, ouiState, offset });
		int pagelen = 20;
		infoType = type;
		String sql = null;
		// 查询企业网关的用户
		if ("2".equals(infoType))
		{
			sql = "select a.username,b.city_id,a.opendate,c.serv_package_id,a.user_type_id,a.device_serialnumber,a.oui,a.binddate,a.dealdate,a.user_id"
					+ " from tab_customerinfo b,tab_egwcustomer a left join gw_cust_user_package c"
					+ "  on a.user_id = c.user_id where"
					+ " a.customer_id=b.customer_id and a.user_state in('1','2')";
			// 绑定状态
			if ("1".equals(bindState))
			{
				if (LipossGlobals.isOracle()) {
					sql += " and a.device_id is null ";
				}else {
					sql += " and (a.device_id = '' or a.device_id is null)";
				}
				
				// 用户设备
				if ("1".equals(ouiState))
				{
					if (LipossGlobals.isOracle()) {
						sql += " and a.device_serialnumber is null ";
					}else{
						sql += " and (a.device_serialnumber = '' or a.device_serialnumber is null)";
					}
				}
				if ("2".equals(ouiState))
				{
					if (LipossGlobals.isOracle()) {
						sql += " and a.device_serialnumber is not null ";
					}else {
						sql += " and (a.device_serialnumber <> '' and a.device_serialnumber is not null)";
					}
				}
			}
			if ("2".equals(bindState))
			{
				if (LipossGlobals.isOracle()) {
					sql += " and a.device_id is not  null ";
				}else {
					sql += " and (a.device_id <> '' and a.device_id is not  null)";
				}
			}
			// 工单受理时间
			if ("1".equals(timeType))
			{
				if (startTime != null)
				{
					sql += " and a.dealdate>=" + startTime;
				}
				if (endTime != null)
				{
					sql += " and a.dealdate<" + endTime;
				}
			}
			// 绑定时间
			if ("2".equals(timeType))
			{
				if (startTime != null)
				{
					sql += " and a.binddate>=" + startTime;
				}
				if (endTime != null)
				{
					sql += " and a.binddate<" + endTime;
				}
			}
			// 开户时间
			if ("3".equals(timeType))
			{
				if (startTime != null)
				{
					sql += " and a.opendate>=" + startTime;
				}
				if (endTime != null)
				{
					sql += " and a.opendate<" + endTime;
				}
			}
			if (username != null)
			{
				sql += " and a.username='" + username + "'";
			}
			if (cityIdList != null)
			{
				sql += " and b.city_id in (";
				for (int i = 0; i < cityIdList.size(); i++)
				{
					sql += "'" + cityIdList.get(i) + "',";
				}
				sql += "'')";
			}
			if (packageId != null && packageId.equals("-1") == false)
			{
				sql += " and c.serv_package_id='" + packageId + "'";
			}
		}
		else
		{
			// sql = "select username,city_id,opendate,serv_package_id"
			// + " from tab_hgwcustomer a left join gw_cust_user_package b"
			// + " on b.user_id = a.user_id"
			// + " where a.user_id not in(select y.user_id from tab_gw_device x,
			// tab_hgwcustomer y "
			// +"where x.cpe_allocatedstatus=1 and x.oui = y.oui and x.device_serialnumber
			// = y.device_serialnumber "
			// +"and y.user_state in( '1','2')) and a.user_state in( '1','2')";
			if (Global.JSDX.equals(Global.instAreaShortName))
			{
				sql = "select a.username,a.city_id,a.opendate,b.serv_package_id,a.user_type_id,a.device_serialnumber,a.oui,a.binddate,a.dealdate,a.user_id,a.gather_id,c.serv_status"
						+ " from tab_hgwcustomer a "
						+ " left join gw_cust_user_package b on b.user_id = a.user_id left join tab_iptv_user c on a.user_id=c.user_id"
						+ " where a.user_state in( '1','2')";
			}
			else
			{
				sql = "select a.username,a.city_id,a.opendate,b.serv_package_id,a.user_type_id,a.device_serialnumber,a.oui,a.binddate,a.dealdate,a.user_id,a.gather_id"
						+ " from tab_hgwcustomer a "
						+ " left join gw_cust_user_package b on b.user_id = a.user_id "
						+ " where a.user_state in( '1','2')";
			}
			// 绑定状态  1 表示 未绑定    2 表示 绑定
			if ("1".equals(bindState))
			{
				if (LipossGlobals.isOracle()) {
					sql += " and a.device_id is null ";
				}else{
					sql += " and (a.device_id = '' or a.device_id is null)";
				}
					
				// 用户设备
				if ("1".equals(ouiState))
				{
					if (LipossGlobals.isOracle()) {
						sql += " and a.device_serialnumber is null ";
					}else {
						sql += " and (a.device_serialnumber = '' or a.device_serialnumber is null)";
					}
				}
				if ("2".equals(ouiState))
				{
					if (LipossGlobals.isOracle()) {
						sql += " and a.device_serialnumber is not null";
					}else {
						sql += " and (a.device_serialnumber <> '' and a.device_serialnumber is not null)";
					}
				}
			}
			if ("2".equals(bindState))
			{
				if (LipossGlobals.isOracle()) {
					sql += " and a.device_id is not null ";
				}else {
					sql += " and (a.device_id <> '' and a.device_id is not null)";
				}
			}
			// 工单受理时间
			if ("1".equals(timeType))
			{
				if (startTime != null)
				{
					sql += " and a.dealdate>=" + startTime;
				}
				if (endTime != null)
				{
					sql += " and a.dealdate<" + endTime;
				}
			}
			// 绑定时间
			if ("2".equals(timeType))
			{
				if (startTime != null)
				{
					sql += " and a.binddate>=" + startTime;
				}
				if (endTime != null)
				{
					sql += " and a.binddate<" + endTime;
				}
			}
			// 开户时间
			if ("3".equals(timeType))
			{
				if (startTime != null)
				{
					sql += " and a.opendate>=" + startTime;
				}
				if (endTime != null)
				{
					sql += " and a.opendate<" + endTime;
				}
			}
			if (username != null)
			{
				sql += " and a.username='" + username + "'";
			}
			if (cityIdList != null)
			{
				sql += " and a.city_id in(" + StringUtils.weave(cityIdList) + ")";
			}
			if (packageId != null && packageId.equals("-1") == false)
			{
				sql += " and b.serv_package_id='" + packageId + "'";
			}
		}
		sql += " order by  city_id,a.user_type_id";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		// 以下部分更改 为了增加分页功能
		List<Map> list = new ArrayList<Map>();
		// 当前页码
		int offsetInt;
		if (offset == null || "".equals(offset))
			offsetInt = 1;
		else
			offsetInt = Integer.parseInt(offset);
		// 用于存放数据集
		Map<String, String> mapStr = new HashMap<String, String>();
		QueryPage qryp = new QueryPage();
		qryp.initPage(sql, offsetInt, pagelen);
		String strBar = qryp.getGoPageBar();
		// Cursor中数据的存放方式和JAVA的MAP形式类似
		LOG.debug("------offsetInt---" + offsetInt);
		Cursor cursor = DataSetBean.getCursor(sql, offsetInt, pagelen);
		int len = cursor.getRecordSize();
		LOG.debug("------len---" + len);
		String city_name;
		String serv_package_name;
		long time = 0;
		DateTimeUtil dateTimeUtil = null;
		if (len > 0)
		{
			cityMap = CityDAO.getCityIdCityNameMap();
			ServPackageDAO servPackageDAO = new ServPackageDAO();
			packageMap = servPackageDAO.getPackageIdNameMap();
			userTypeMap = getUserType();
		}
		while (len > 0)
		{
			mapStr = cursor.getNext();
			city_name = (String) cityMap.get(mapStr.get("city_id"));
			if (city_name != null)
			{
				mapStr.put("city_name", city_name);
			}
			else
			{
				mapStr.put("city_name", "");
			}
			serv_package_name = (String) packageMap.get(mapStr.get("serv_package_id"));
			if (serv_package_name != null)
			{
				mapStr.put("serv_package_name", serv_package_name);
			}
			else
			{
				mapStr.put("serv_package_name", "");
			}
			// 用户来源
			String user_type_id = (String) mapStr.get("user_type_id");
			String tmp = "手工添加";
			if (false == StringUtil.IsEmpty(user_type_id))
			{
				tmp = userTypeMap.get(user_type_id);
				if (true == StringUtil.IsEmpty(tmp))
				{
					tmp = "其他";
				}
			}
			mapStr.put("user_type", tmp);
			try
			{
				time = Long.parseLong(mapStr.get("opendate"));
				dateTimeUtil = new DateTimeUtil(time * 1000);
				mapStr.put("opendate", dateTimeUtil.getDate());
			}
			catch (NumberFormatException e)
			{
				mapStr.put("opendate", "");
			}
			catch (Exception e)
			{
				mapStr.put("opendate", "");
			}
			// 将binddate转换成时间
			try
			{
				time = Long.parseLong(mapStr.get("binddate"));
				dateTimeUtil = new DateTimeUtil(time * 1000);
				mapStr.put("binddate", dateTimeUtil.getDate());
			}
			catch (NumberFormatException e)
			{
				mapStr.put("binddate", "");
			}
			catch (Exception e)
			{
				mapStr.put("binddate", "");
			}
			// 将dealdate转换成时间
			try
			{
				time = Long.parseLong(mapStr.get("dealdate"));
				dateTimeUtil = new DateTimeUtil(time * 1000);
				mapStr.put("dealdate", dateTimeUtil.getDate());
			}
			catch (NumberFormatException e)
			{
				mapStr.put("dealdate", "");
			}
			catch (Exception e)
			{
				mapStr.put("dealdate", "");
			}
			if (LipossGlobals.IsITMS())
			{
				if (Global.JSDX.equals(Global.instAreaShortName))
				{
					String serv_status = mapStr.get("serv_status");
					if ("1".equals(serv_status))
					{
						mapStr.put("iTV", "是");
					}
					else
					{
						mapStr.put("iTV", "否");
					}
				}
			}
			mapStr.put("strBar", "");
			list.add(mapStr);
			--len;
		}
		cityMap = null;
		// 底部页码部分代码以String形式输出
		Map<String, String> mapCur = new HashMap<String, String>();
		mapCur.put("strBar", strBar);
		list.add(mapCur);
		return list;
	}

	/**
	 * 获取当前属地的下属属地(包括本身)
	 * 
	 * @param city_id
	 * @return
	 */
	public List<Map<String, String>> getCityList(String city_id)
	{
		LOG.debug("getCityList({})", city_id);
		List<String> clist = CityDAO.getAllNextCityIdsByCityPid(city_id);
		String sql = "select city_id,city_name from tab_city where city_id in("
				+ StringUtils.weave(clist) + ")";
		clist = null;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}

	/**
	 * get servpackage.
	 * 
	 * @param city_id
	 * @return
	 */
	public List<Map<String, String>> getPackageList()
	{
		LOG.debug("getPackageList()");
		String sql = "select serv_package_id,serv_package_name from gw_serv_package";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}

	/**
	 * 初始化数据库连接
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao)
	{
		LOG.debug("setDao(DataSource)");
		jt = new JdbcTemplate(dao);
	}

	public Map<String, String> getUserType()
	{
		String sql = "select * from user_type";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select user_type_id,type_name from user_type";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List<Map> list = jt.queryForList(sql);
		Map userTypeMap = new HashMap<String, String>();
		for (Map map : list)
		{
			userTypeMap.put(StringUtil.getStringValue(map.get("user_type_id")),
					StringUtil.getStringValue(map.get("type_name")));
		}
		return userTypeMap;
	}

	public List<Map> getUserByOrder(String infoType, ArrayList<String> cityIdList,
			String username, String startTime, String endTime, String timeType,
			String bindState, String offset, String bigOrderType, String orderType)
	{
		LOG.debug("getUserByOrder({}, {}, {}, {}, {}, {}, {}, {}, {}, {})",
				new Object[] { infoType, cityIdList, username, startTime, endTime,
						timeType, bindState, offset, bigOrderType, orderType });
		int pagelen = 20;
		String sql = null;
		// 查询企业网关的用户
		if ("2".equals(infoType))
		{
			sql = "select a.username,b.city_id,a.opendate,b.customer_name,a.user_type_id,a.device_id,a.device_serialnumber,a.oui,a.binddate,a.dealdate,a.user_id,a.access_style_id"
					+ " from tab_customerinfo b,tab_egwcustomer a where"
					+ " a.customer_id=b.customer_id and a.user_state in('1','2')";
			// 绑定状态
			if ("1".equals(bindState))
			{
				if (LipossGlobals.isOracle()) {
					sql += " and a.device_id is null ";
				}else {
					sql += " and (a.device_id = '' or a.device_id is null)";
				}
			}
			if ("2".equals(bindState))
			{
				if (LipossGlobals.isOracle()) {
					sql += " and a.device_id is not  null ";
				}else {
					sql += " and (a.device_id <> '' and a.device_id is not  null)";
				}
			}
			// // 工单受理时间
			// if ("1".equals(timeType))
			// {
			// if (startTime != null)
			// {
			// sql += " and a.dealdate>=" + startTime;
			// }
			// if (endTime != null)
			// {
			// sql += " and a.dealdate<" + endTime;
			// }
			// }
			// // 绑定时间
			// if ("2".equals(timeType))
			// {
			// if (startTime != null)
			// {
			// sql += " and a.binddate>=" + startTime;
			// }
			// if (endTime != null)
			// {
			// sql += " and a.binddate<" + endTime;
			// }
			// }
			// 开户时间
			if ("3".equals(timeType))
			{
				if (startTime != null)
				{
					sql += " and a.opendate>=" + startTime;
				}
				if (endTime != null)
				{
					sql += " and a.opendate<" + endTime;
				}
			}
			if (username != null)
			{
				sql += " and a.username='" + username + "'";
			}
			if (cityIdList != null)
			{
				sql += " and b.city_id in (";
				for (int i = 0; i < cityIdList.size(); i++)
				{
					sql += "'" + cityIdList.get(i) + "',";
				}
				sql += "'')";
			}
			if ("1".equals(bigOrderType))
			{
				sql += " and a.access_style_id in (1,2,3)";
			}
			if ("2".equals(bigOrderType))
			{
				sql += " and a.access_style_id in (4,5)";
			}
			if (orderType != null && !"-1".equals(orderType)&&!"".equals(orderType))
			{
				sql += " and a.access_style_id=" + orderType;
			}
		}
		sql += " order by b.city_id";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		// 以下部分更改 为了增加分页功能
		List<Map> list = new ArrayList<Map>();
		// 当前页码
		int offsetInt;
		if (offset == null || "".equals(offset))
			offsetInt = 1;
		else
			offsetInt = Integer.parseInt(offset);
		// 用于存放数据集
		Map<String, String> mapStr = new HashMap<String, String>();
		QueryPage qryp = new QueryPage();
		qryp.initPage(sql, offsetInt, pagelen);
		String strBar = qryp.getGoPageBar();
		// Cursor中数据的存放方式和JAVA的MAP形式类似
		LOG.debug("------offsetInt---" + offsetInt);
		Cursor cursor = DataSetBean.getCursor(sql, offsetInt, pagelen);
		int len = cursor.getRecordSize();
		LOG.debug("------len---" + len);
		String city_name;
		long time = 0;
		DateTimeUtil dateTimeUtil = null;
		if (len > 0)
		{
			cityMap = CityDAO.getCityIdCityNameMap();
			userTypeMap = getUserType();
		}
		while (len > 0)
		{
			mapStr = cursor.getNext();
			city_name = (String) cityMap.get(mapStr.get("city_id"));
			if (city_name != null)
			{
				mapStr.put("city_name", city_name);
			}
			else
			{
				mapStr.put("city_name", "");
			}
			// 用户来源
			String user_type_id = (String) mapStr.get("user_type_id");
			String tmp = "手工添加";
			if (false == StringUtil.IsEmpty(user_type_id))
			{
				tmp = userTypeMap.get(user_type_id);
				if (true == StringUtil.IsEmpty(tmp))
				{
					tmp = "其他";
				}
			}
			mapStr.put("user_type", tmp);
			try
			{
				time = Long.parseLong(mapStr.get("opendate"));
				dateTimeUtil = new DateTimeUtil(time * 1000);
				mapStr.put("opendate", dateTimeUtil.getDate());
			}
			catch (NumberFormatException e)
			{
				mapStr.put("opendate", "");
			}
			catch (Exception e)
			{
				mapStr.put("opendate", "");
			}
			// 将binddate转换成时间
			try
			{
				time = Long.parseLong(mapStr.get("binddate"));
				dateTimeUtil = new DateTimeUtil(time * 1000);
				mapStr.put("binddate", dateTimeUtil.getDate());
			}
			catch (NumberFormatException e)
			{
				mapStr.put("binddate", "");
			}
			catch (Exception e)
			{
				mapStr.put("binddate", "");
			}
			// 将dealdate转换成时间
			try
			{
				time = Long.parseLong(mapStr.get("dealdate"));
				dateTimeUtil = new DateTimeUtil(time * 1000);
				mapStr.put("dealdate", dateTimeUtil.getDate());
			}
			catch (NumberFormatException e)
			{
				mapStr.put("dealdate", "");
			}
			catch (Exception e)
			{
				mapStr.put("dealdate", "");
			}
			// 订单类型
			String access_style_id = (String) mapStr.get("access_style_id");
			if ("1".equals(access_style_id))
			{
				mapStr.put("ordertype", "ADSL");
			}
			else if ("2".equals(access_style_id))
			{
				mapStr.put("ordertype", "普通LAN");
			}
			else if ("3".equals(access_style_id))
			{
				mapStr.put("ordertype", "普通光纤");
			}
			else if ("4".equals(access_style_id))
			{
				mapStr.put("ordertype", "专线LAN");
			}
			else if ("5".equals(access_style_id))
			{
				mapStr.put("ordertype", "专线光纤");
			}
			else
			{
				mapStr.put("ordertype", "其他");
			}
			// 订单类型
			String device_id = (String) mapStr.get("device_id");
			if (device_id != null && !"".equals(device_id))
			{
				mapStr.put("bandstate", "已绑定");
			}
			else
			{
				mapStr.put("bandstate", "未绑定");
			}
			mapStr.put("strBar", "");
			list.add(mapStr);
			--len;
		}
		cityMap = null;
		// 底部页码部分代码以String形式输出
		Map<String, String> mapCur = new HashMap<String, String>();
		mapCur.put("strBar", strBar);
		list.add(mapCur);
		return list;
	}

	public List<Map> getUserByOrderExcel(String infoType, ArrayList<String> cityIdList,
			String username, String startTime, String endTime, String timeType,
			String bindState, String bigOrderType, String orderType)
	{
		LOG.debug("getUserByOrder({}, {}, {}, {}, {}, {}, {}, {}, {}, {})",
				new Object[] { infoType, cityIdList, username, startTime, endTime,
						timeType, bindState });
		int pagelen = 20;
		String sql = null;
		// 查询企业网关的用户
		if ("2".equals(infoType))
		{
			sql = "select a.username,b.city_id,a.opendate,b.customer_name,a.user_type_id,a.device_id,a.device_serialnumber,a.oui,a.binddate,a.dealdate,a.user_id,a.access_style_id"
					+ " from tab_customerinfo b,tab_egwcustomer a where"
					+ " a.customer_id=b.customer_id and a.user_state in('1','2')";
			// 绑定状态
			if ("1".equals(bindState))
			{
				if (LipossGlobals.isOracle()) {
					sql += " and a.device_id is null ";
				}else {
					sql += " and (a.device_id = '' or a.device_id is null)";
				}
			}
			if ("2".equals(bindState))
			{
				if (LipossGlobals.isOracle()) {
					sql += " and a.device_id is not  null ";
				}else {
					sql += " and (a.device_id <> '' and a.device_id is not  null)";
				}
			}
			// // 工单受理时间
			// if ("1".equals(timeType))
			// {
			// if (startTime != null)
			// {
			// sql += " and a.dealdate>=" + startTime;
			// }
			// if (endTime != null)
			// {
			// sql += " and a.dealdate<" + endTime;
			// }
			// }
			// // 绑定时间
			// if ("2".equals(timeType))
			// {
			// if (startTime != null)
			// {
			// sql += " and a.binddate>=" + startTime;
			// }
			// if (endTime != null)
			// {
			// sql += " and a.binddate<" + endTime;
			// }
			// }
			// 开户时间
			if ("3".equals(timeType))
			{
				if (startTime != null)
				{
					sql += " and a.opendate>=" + startTime;
				}
				if (endTime != null)
				{
					sql += " and a.opendate<" + endTime;
				}
			}
			if (username != null)
			{
				sql += " and a.username='" + username + "'";
			}
			if (cityIdList != null)
			{
				sql += " and b.city_id in (";
				for (int i = 0; i < cityIdList.size(); i++)
				{
					sql += "'" + cityIdList.get(i) + "',";
				}
				sql += "'')";
			}
			if ("1".equals(bigOrderType))
			{
				sql += " and a.access_style_id in (1,2,3)";
			}
			if ("2".equals(bigOrderType))
			{
				sql += " and a.access_style_id in (4,5)";
			}
			if (orderType != null && !"-1".equals(orderType))
			{
				sql += " and a.access_style_id=" + orderType;
			}
		}
		sql += " order by b.city_id";
		PrepareSQL psql = new PrepareSQL(sql);
		cityMap = CityDAO.getCityIdCityNameMap();
		userTypeMap = getUserType();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int index) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				String city_name;
				long time = 0;
				DateTimeUtil dateTimeUtil = null;
				map.put("username", rs.getString("username"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("oui", rs.getString("oui"));
				map.put("user_id", rs.getString("user_id"));
				map.put("customer_name", rs.getString("customer_name"));
				city_name = (String) cityMap.get(rs.getString("city_id"));
				if (city_name != null)
				{
					map.put("city_name", city_name);
				}
				else
				{
					map.put("city_name", "");
				}
				// 用户来源
				String user_type_id = rs.getString("user_type_id");
				String tmp = "手工添加";
				if (false == StringUtil.IsEmpty(user_type_id))
				{
					tmp = userTypeMap.get(user_type_id);
					if (true == StringUtil.IsEmpty(tmp))
					{
						tmp = "其他";
					}
				}
				map.put("user_type", tmp);
				try
				{
					time = Long.parseLong(rs.getString("opendate"));
					dateTimeUtil = new DateTimeUtil(time * 1000);
					map.put("opendate", dateTimeUtil.getDate());
				}
				catch (NumberFormatException e)
				{
					map.put("opendate", "");
				}
				catch (Exception e)
				{
					map.put("opendate", "");
				}
				// 将binddate转换成时间
				try
				{
					time = Long.parseLong(rs.getString("binddate"));
					dateTimeUtil = new DateTimeUtil(time * 1000);
					map.put("binddate", dateTimeUtil.getDate());
				}
				catch (NumberFormatException e)
				{
					map.put("binddate", "");
				}
				catch (Exception e)
				{
					map.put("binddate", "");
				}
				// 将dealdate转换成时间
				try
				{
					time = Long.parseLong(rs.getString("dealdate"));
					dateTimeUtil = new DateTimeUtil(time * 1000);
					map.put("dealdate", dateTimeUtil.getDate());
				}
				catch (NumberFormatException e)
				{
					map.put("dealdate", "");
				}
				catch (Exception e)
				{
					map.put("dealdate", "");
				}
				// 订单类型
				String access_style_id = rs.getString("access_style_id");
				if ("1".equals(access_style_id))
				{
					map.put("ordertype", "ADSL");
				}
				else if ("2".equals(access_style_id))
				{
					map.put("ordertype", "普通LAN");
				}
				else if ("3".equals(access_style_id))
				{
					map.put("ordertype", "普通光纤");
				}
				else if ("4".equals(access_style_id))
				{
					map.put("ordertype", "专线LAN");
				}
				else if ("5".equals(access_style_id))
				{
					map.put("ordertype", "专线光纤");
				}
				else
				{
					map.put("ordertype", "其他");
				}
				// 绑定状态
				String device_id = rs.getString("device_id");
				if (device_id != null && !"".equals(device_id))
				{
					map.put("bandstate", "已绑定");
				}
				else
				{
					map.put("bandstate", "未绑定");
				}
				return map;
			}
		});
		cityMap = null;
		userTypeMap = null;
		return list;
	}
}
