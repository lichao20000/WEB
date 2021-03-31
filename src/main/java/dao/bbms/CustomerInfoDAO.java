package dao.bbms;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

import dao.util.JdbcTemplateExtend;

/**
 * 客户资料信息管理
 * 
 * @author 陈仲民（5243）;alex(yanhj@)
 * @version 1.0
 * @since 2008-6-3
 * @category 资源管理
 */
@SuppressWarnings("unchecked")
public class CustomerInfoDAO {

	/** log */
	private static final Logger LOG = LoggerFactory
			.getLogger(CustomerInfoDAO.class);
	// jdbc
	private JdbcTemplateExtend jt;
	// 客户类型
	private Map<String, String> typeMap = new HashMap<String, String>();
	// 企业规模
	private Map<String, String> sizeMap = new HashMap<String, String>();
	// 客户状态
	private Map<String, String> statusMap = new HashMap<String, String>();
	// 属地
	private Map<String, String> cityMap = new HashMap<String, String>();
	// 局向
	private Map<String, String> officeMap = new HashMap<String, String>();
	// 小区
	private Map<String, String> zoneMap = new HashMap<String, String>();

	public CustomerInfoDAO() {
		typeMap.put("1", "企业单位");
		typeMap.put("2", "事业单位");
		sizeMap.put("0", "小");
		sizeMap.put("1", "中");
		sizeMap.put("2", "大");
		statusMap.put("1", "开通");
		statusMap.put("2", "暂停");
		statusMap.put("3", "销户");
	}

	/**
	 * 初始化数据连接
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}

	public void test() {
	}

	/**
	 * 新增客户资料信息
	 * 
	 * @param customer_id
	 * @param customer_account
	 * @param city_id
	 * @param office_id
	 * @param zone_id
	 * @param customer_name
	 * @param customer_pwd
	 * @param customer_type
	 * @param customer_size
	 * @param customer_address
	 * @param linkman
	 * @param linkphone
	 * @param customer_state
	 * @return 返回成功执行的记录数 0：失败 大于0：成功
	 * 
	 * modify by zhaixf 增加一个email字段 同时增加判断主键已存在的情况
	 */
	public int addCustomer(String customer_id, String customer_account,
			String city_id, String office_id, String zone_id,
			String customer_name, String customer_pwd, String customer_type,
			String customer_size, String customer_address, String linkman,
			String linkphone, String customer_state, String email) {
		LOG.debug("addCustomer({},{},{})", new Object[] { customer_id,
				customer_name, city_id });

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			String selectSQL = "select count(*) from tab_customerinfo where customer_name=?";
			PrepareSQL pSQL = new PrepareSQL(selectSQL);
			pSQL.setString(1, customer_name);
			LOG.debug(selectSQL);
			int total = jt.queryForInt(pSQL.getSQL());
			if (total > 0) {
				return -11;
			}
		}
		else {
			// 判断客户ID是否已经在数据库中存在 by zhaixf
			String selectSQL = "select 1 from tab_customerinfo where customer_name=?";
			PrepareSQL pSQL = new PrepareSQL(selectSQL);
			pSQL.setString(1, customer_name);
			LOG.debug(selectSQL);

			List list = jt.queryForList(pSQL.getSQL());

			if (null != list && list.size() > 0) {
				return -11;
			}
		}

		// 获取唯一id
		// String id = java.util.UUID.randomUUID().toString();
		// 当前时间
		DateTimeUtil dt = new DateTimeUtil();
		long time = dt.getLongTime();

		// 生成入库语句
		String sql = "insert into tab_customerinfo (customer_id,city_id,office_id,zone_id,"
				+ "customer_name,customer_pwd,customer_type,customer_size,customer_address,"
				+ "linkman,linkphone,customer_state,update_time, email,customer_account) "
				+ "values ('"
				+ customer_id
				+ "','"
				+ city_id
				+ "','"
				+ office_id
				+ "','"
				+ zone_id
				+ "','"
				+ customer_name
				+ "','"
				+ customer_pwd
				+ "','"
				+ customer_type
				+ "','"
				+ customer_size
				+ "','"
				+ customer_address
				+ "','"
				+ linkman
				+ "','"
				+ linkphone
				+ "',"
				+ customer_state
				+ ","
				+ time
				+ ",'"
				+ email
				+ "','"
				+ customer_account + "')";
		LOG.debug(sql);
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.update(sql);
	}

	/**
	 * 编辑客户资料信息
	 * 
	 * @param customer_id
	 * @param city_id
	 * @param office_id
	 * @param zone_id
	 * @param customer_name
	 * @param customer_pwd
	 * @param customer_type
	 * @param customer_size
	 * @param customer_address
	 * @param linkman
	 * @param linkphone
	 * @param customer_state
	 * @param id
	 * @return 返回成功执行的记录数 0：失败 大于0：成功
	 * 
	 * modify by zhaixf 增加email字段
	 */
	public int updCustomer(String customer_id, String city_id,
			String office_id, String zone_id, String customer_name,
			String customer_pwd, String customer_type, String customer_size,
			String customer_address, String linkman, String linkphone,
			String customer_state, String email) {
		LOG.debug("updCustomer({},{},{})", new Object[] { customer_id,
				customer_name, city_id });

		// 当前时间
		DateTimeUtil dt = new DateTimeUtil();
		long time = dt.getLongTime();
		// 生成入库语句
		String sql = "update tab_customerinfo set city_id='" + city_id
				+ "',office_id='" + office_id + "',zone_id='" + zone_id
				+ "',customer_name='" + customer_name + "',customer_pwd='"
				+ customer_pwd + "',customer_type='" + customer_type
				+ "',customer_size='" + customer_size + "',customer_address='"
				+ customer_address + "',linkman='" + linkman + "',linkphone='"
				+ linkphone + "',customer_state=" + customer_state
				+ ",update_time=" + time + ",email='" + email
				+ "' where customer_id='" + customer_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.update(sql);
	}

	/**
	 * 删除客户资料
	 * 
	 * @param id
	 * @return 返回成功执行的记录数 0：失败 大于0：成功
	 */
	public int delCustomer(String customer_id) {
		LOG.debug("delCustomer({})", customer_id);

		// 生成入库语句
		String sql_delete = "delete from tab_customerinfo where customer_id='"
				+ customer_id + "'";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			String sqlSelect = "select customer_id, customer_name, customer_pwd, customer_type, customer_size, customer_address, linkman, " +
								" linkphone, email, mobile, customer_state, update_time, opendate, pausedate, closedate, " +
								" city_id, office_id, zone_id, customer_account " +
								" from tab_customerinfo where customer_id='" + customer_id + "'";

			PrepareSQL psqlTwo = new PrepareSQL(sqlSelect);
			List<Map> customerInfoList = jt.queryForList(psqlTwo.getSQL());

			if (customerInfoList == null || customerInfoList.isEmpty()) {
				LOG.warn("CustomerInfoDAO.delCustomer() customerInfoList is empty!");
				return -1;
			}
			else {
				String[] sql = new String[customerInfoList.size() + 1];
				int index = 0;
				for (Map customerInfoMap : customerInfoList) {
					String customer_id_old = StringUtil.getStringValue(customerInfoMap, "customer_id");
					String customer_name = StringUtil.getStringValue(customerInfoMap, "customer_name");
					String customer_pwd = StringUtil.getStringValue(customerInfoMap, "customer_pwd");
					String customer_type = StringUtil.getStringValue(customerInfoMap, "customer_type");
					String customer_size = StringUtil.getStringValue(customerInfoMap, "customer_size");
					String customer_address = StringUtil.getStringValue(customerInfoMap, "customer_address");
					String linkman = StringUtil.getStringValue(customerInfoMap, "linkman");
					String linkphone = StringUtil.getStringValue(customerInfoMap, "linkphone");
					String email = StringUtil.getStringValue(customerInfoMap, "email");
					String mobile = StringUtil.getStringValue(customerInfoMap, "mobile");
					String customer_state = StringUtil.getStringValue(customerInfoMap, "customer_state");
					String update_time = StringUtil.getStringValue(customerInfoMap, "update_time");
					String opendate = StringUtil.getStringValue(customerInfoMap, "opendate");
					String pausedate = StringUtil.getStringValue(customerInfoMap, "pausedate");
					String closedate = StringUtil.getStringValue(customerInfoMap, "closedate");
					String city_id = StringUtil.getStringValue(customerInfoMap, "city_id");
					String office_id = StringUtil.getStringValue(customerInfoMap, "office_id");
					String zone_id = StringUtil.getStringValue(customerInfoMap, "zone_id");
					String customer_account = StringUtil.getStringValue(customerInfoMap, "customer_account");

					String sqlInsert = "insert into tab_customerinfo_bak (customer_id,customer_name,customer_pwd,customer_type, customer_size, "
							+ "customer_address,linkman,linkphone, email, mobile, customer_state, "
							+ "update_time,opendate, pausedate, closedate, "
							+ "city_id, office_id,zone_id, customer_account) "
							+ "values ('" + customer_id_old + "','" + customer_name + "','" + customer_pwd + "','" + customer_type + "','" + customer_size + "','"
							+ customer_address + "','"+ linkman + "','"+ linkphone + "','" + email + "','"+ mobile + "',"+ customer_state + ","
							+ update_time + ","+ opendate + ","+ pausedate + "," + closedate + ",'"
							+ city_id + "','"+ office_id+ "','"+ zone_id+ "','"+ customer_account+ "')";

					sql[index] = sqlInsert;
					index++;
				}

				sql[index] = sql_delete;
				return jt.batchUpdate(sql).length;
			}
		}
		else {
			String sql_bak = "insert into tab_customerinfo_bak select * from tab_customerinfo where customer_id='"
					+ customer_id + "'";

			String sql[] = { "", "" };
			sql[0] = sql_bak;
			sql[1] = sql_delete;
			PrepareSQL psql = new PrepareSQL(sql[0]);
			psql.getSQL();
			psql = new PrepareSQL(sql[1]);
			psql.getSQL();

			return jt.batchUpdate(sql).length;
		}


		// 删除客户,并同时更新该客户所拥有的设备的状态
		// String[] sql = new String[3];
		// sql[0] = "insert into tab_customerinfo_bak select * from
		// tab_customerinfo where customer_id='"
		// + customer_id + "'";
		// sql[1] = "update tab_gw_device set
		// cpe_allocatedstatus=0,customer_id=''"
		// + " where customer_id='" + customer_id + "' and gw_type=2";
		// sql[2] = "delete from tab_customerinfo where customer_id='" +
		// customer_id + "'";
		//
		// return jt.batchUpdate(sql).length;
	}

	/**
	 * 查询指定的客户信息
	 * 
	 * @param id
	 * @return
	 */
	public Map getCustomerInfo(String customer_id) {
		LOG.debug("getCustomerInfo({})", customer_id);

		String sql = "select * from tab_customerinfo where customer_id='"
				+ customer_id + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select customer_id, customer_name, customer_pwd, customer_type, customer_size, customer_address, linkman," +
					" linkphone, email, mobile, customer_state, update_time, opendate, pausedate, closedate, " +
					" city_id, office_id, zone_id, customer_account " +
					" from tab_customerinfo where customer_id='"
					+ customer_id + "'";
		}

		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List list = jt.queryForList(sql);
		if (list != null && list.size() > 0) {
			return (Map) list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 查询客户信息列表
	 * 
	 * @return
	 */
	public List queryCustomer(int curPage_splitPage, int num_splitPage,
			String customer_id, String customer_name, String user_city_id) {
		LOG.debug("queryCustomer({},{},{},{},{})", new Object[] {
				curPage_splitPage, num_splitPage, customer_id, customer_name,
				user_city_id });

		initData();// 初始化map
		String sql = "select * from tab_customerinfo where customer_state !=3 ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select customer_id, customer_name, customer_pwd, customer_type, customer_size, customer_address, linkman," +
					"  linkphone, email, mobile, customer_state, update_time, opendate, pausedate, closedate, " +
					"  city_id, office_id, zone_id, customer_account " +
					" from tab_customerinfo where customer_state !=3 ";
		}

		if (customer_id != null && !"".equals(customer_id)) {
			sql += " and customer_id like '%" + customer_id + "%' ";
		}
		if (customer_name != null && !"".equals(customer_name)) {
			sql += " and customer_name like '%" + customer_name + "%' ";
		}
		if (user_city_id != null && !"".equals(user_city_id)) {
			List list = CityDAO.getAllNextCityIdsByCityPid(user_city_id);
			sql += " and city_id in("
					+ StringUtils.weave(list) + ")";
			list = null;
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.querySP(sql, (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						// 客户名称
						map.put("customer_name", rs.getString("customer_name"));
						// 客户账号
						map.put("customer_account", rs
								.getString("customer_account"));
						// 客户ID
						map.put("customer_id", rs.getString("customer_id"));
						// 客户密码
						map.put("customer_pwd", rs.getString("customer_pwd"));
						// 客户类型
						map.put("customer_type", (String) typeMap.get(rs
								.getString("customer_type")));
						// 企业规模
						map.put("customer_size", (String) sizeMap.get(rs
								.getString("customer_size")));
						// 企业地址
						map.put("customer_address", rs
								.getString("customer_address"));
						// 联系人姓名
						map.put("linkman", rs.getString("linkman"));
						// 联系人电话
						map.put("linkphone", rs.getString("linkphone"));
						// 客户状态
						map.put("customer_state", (String) statusMap.get(String
								.valueOf(rs.getInt("customer_state"))));
						// 属地
						map.put("city_id", (String) cityMap.get(rs
								.getString("city_id")));
						// 局向
						map.put("office_id", (String) officeMap.get(rs
								.getString("office_id")));
						// 小区
						map.put("zone_id", (String) zoneMap.get(rs
								.getString("zone_id")));
						map.put("email", rs.getString("email"));
						return map;
					}
				});
	}

	/**
	 * 查询客户信息的总页数
	 * 
	 * @return
	 */
	public int getCustomerCount(int num_splitPage, String customer_id,
			String customer_name, String user_city_id) {
		LOG.debug("queryCustomer({},{},{},{})", new Object[] {
				num_splitPage, customer_id, customer_name, user_city_id });

		String sql = "select count(*) from tab_customerinfo where customer_state !=3 ";
		if (customer_id != null && !"".equals(customer_id)) {
			sql += " and customer_id like '%" + customer_id + "%' ";
		}
		if (customer_name != null && !"".equals(customer_name)) {
			sql += " and customer_name like '%" + customer_name + "%' ";
		}
		if (user_city_id != null && !"".equals(user_city_id)) {
			List list = CityDAO.getAllNextCityIdsByCityPid(user_city_id);
			sql += " and city_id in("
					+ StringUtils.weave(list) + ")";
			list = null;
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		int total = jt.queryForInt(sql);
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * 查询客户资料的详细信息
	 * 
	 * @param id
	 * @return
	 * 
	 */
	public Map getCustomerDetail(String customer_id) {
		LOG.debug("getCustomerDetail({})", customer_id);

		// 查询
		String sql = "select * from tab_customerinfo where customer_id='"
				+ customer_id + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select customer_id, customer_name, customer_pwd, customer_type, customer_size, customer_address, linkman," +
					" linkphone, email, mobile, customer_state, update_time, opendate, pausedate, closedate, " +
					" city_id, office_id, zone_id, customer_account " +
					" from tab_customerinfo where customer_id='"
					+ customer_id + "'";
		}

		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List list = jt.queryForList(sql);
		Map customerMap = (Map) list.get(0);
		// 处理数据
		initData();
		customerMap.put("customer_type", typeMap.get((String) customerMap
				.get("customer_type")));	
		customerMap.put("customer_size", sizeMap.get((String) customerMap
				.get("customer_size")));
		customerMap.put("customer_state", statusMap.get(customerMap.get(
				"customer_state").toString()));
		if (false == StringUtil.IsEmpty(StringUtil.getStringValue(customerMap.get("city_id")))) {
			customerMap.put("city_id", cityMap.get((String) customerMap
					.get("city_id")));
		} else
		{
			customerMap.put("city_id", "未知");
		}
		if(false == StringUtil.IsEmpty(StringUtil.getStringValue(customerMap.get("opendate")))){
			customerMap.put("opendate", new DateTimeUtil(Long.valueOf(StringUtil.getStringValue(customerMap.get("opendate")))).getLongDate());
		}
		
		if (false == StringUtil.IsEmpty(StringUtil.getStringValue(customerMap.get("office_id")))) {
			customerMap.put("office_id", officeMap.get((String) customerMap
					.get("office_id")));
		} else {
			customerMap.put("office_id", "-");
		}

		if (false == StringUtil.IsEmpty(StringUtil.getStringValue(customerMap.get("zone_id")))) {
			customerMap.put("zone_id", zoneMap.get((String) customerMap
					.get("zone_id")));
		} else {
			customerMap.put("zone_id", "-");
		}
		
		if(null == customerMap.get("customer_name")){
			customerMap.put("customer_name", "-");
		}
		if(null == customerMap.get("customer_account")){
			customerMap.put("customer_account", "-");
		}
		if(null == customerMap.get("customer_pwd")){
			customerMap.put("customer_pwd", "-");
		}
		if(null == customerMap.get("customer_size")){
			customerMap.put("customer_size", "-");
		}
		if(null == customerMap.get("email")){
			customerMap.put("email", "-");
		}
		if(null == customerMap.get("linkman")){
			customerMap.put("linkman", "-");
		}
		if(null == customerMap.get("linkphone")){
			customerMap.put("linkphone", "-");
		}
		if(null == customerMap.get("office_id")){
			customerMap.put("office_id", "-");
		}
		if(null == customerMap.get("zone_id")){
			customerMap.put("zone_id", "-");
		}
		if(null == customerMap.get("customer_address")){
			customerMap.put("customer_address", "-");
		}
		if(null == customerMap.get("customer_type")){
			customerMap.put("customer_type", "-");
		}
		if(null == customerMap.get("customer_state")){
			customerMap.put("customer_state", "-");
		}
		return customerMap;
	}

	/**
	 * 查询当前用户所属地市和下级地市
	 * 
	 * @param city_id
	 * @return
	 */
	public List getCityList(String city_id) {
		LOG.debug("getCityList({})", city_id);

		String sql = "select city_id,city_name from tab_city where parent_id='"
				+ city_id + "' or city_id='" + city_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}

	/**
	 * 查询当前用户所属地市和下级地市
	 * 
	 * @param city_id
	 * @return
	 */
	public List getAllCityList(String city_id) {
		LOG.debug("getAllCityList({})", city_id);

		String sql = "select city_id,city_name from tab_city where city_id in "
				+ "(select city_id from tab_city where city_id = '"
				+ city_id
				+ "' or parent_id='"
				+ city_id
				+ "') "
				+ "or parent_id in  (select city_id  from tab_city where city_id = '"
				+ city_id + "' or parent_id='" + city_id + "')";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}

	/**
	 * 查询所有局向信息
	 * 
	 * @return
	 */
	public List getOfficeList() {
		LOG.debug("getOfficeList()");

		String sql = "select office_id,office_name from tab_office";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}

	/**
	 * 查询所有小区信息
	 * 
	 * @return
	 */
	public List getZoneList() {
		LOG.debug("getZoneList()");

		String sql = "select zone_id,zone_name from tab_zone";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}

	/**
	 * 初始化信息
	 */
	private void initData() {
		LOG.debug("initData()");

		String sql = "";
		List list = null;
		Iterator it = null;
		Map map = null;
		// 属地
		sql = "select city_id,city_name from tab_city";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		list = jt.queryForList(sql);
		it = list.iterator();
		while (it.hasNext()) {
			map = (Map) it.next();
			cityMap.put((String) map.get("city_id"), (String) map
					.get("city_name"));
		}
		// 局向
		sql = "select office_name,office_id from tab_office";
		psql = new PrepareSQL(sql);
		psql.getSQL();
		list = jt.queryForList(sql);
		it = list.iterator();
		while (it.hasNext()) {
			map = (Map) it.next();
			officeMap.put((String) map.get("office_id"), (String) map
					.get("office_name"));
		}
		// 小区
		sql = "select zone_name,zone_id from tab_zone";
		psql = new PrepareSQL(sql);
		psql.getSQL();
		list = jt.queryForList(sql);
		it = list.iterator();
		while (it.hasNext()) {
			map = (Map) it.next();
			zoneMap.put((String) map.get("zone_id"), (String) map
					.get("zone_name"));
		}
	}

	/**
	 * 返回导出文件的标题
	 * 
	 * @return
	 */
	public String[] getTitle() {
		LOG.debug("getTitle()");

		String[] title = new String[14];
		title[0] = "客户ID";
		title[1] = "客户名称";
		title[2] = "客户密码";
		title[3] = "客户密码";
		title[4] = "客户类型";
		title[5] = "企业规模";
		title[6] = "企业地址";
		title[7] = "联系人姓名";
		title[8] = "联系人电话";
		title[9] = "客户状态";
		title[10] = "属地";
		title[11] = "局向";
		title[12] = "小区";
		title[13] = "E-mail";
		return title;
	}

	/**
	 * 返回导出文件的列名
	 * 
	 * @return
	 */
	public String[] getColumn() {
		LOG.debug("getColumn()");

		String[] column = new String[14];
		column[0] = "customer_id";
		column[1] = "customer_name";
		column[2] = "customer_account";
		column[3] = "customer_pwd";
		column[4] = "customer_type";
		column[5] = "customer_size";
		column[6] = "customer_address";
		column[7] = "linkman";
		column[8] = "linkphone";
		column[9] = "customer_state";
		column[10] = "city_id";
		column[11] = "office_id";
		column[12] = "zone_id";
		column[13] = "email";
		return column;
	}

	/**
	 * 返回导出文件的数据
	 * 
	 * @return
	 */
	public List getExportData(String customer_id, String customer_name,
			String city_id) {
		LOG.debug("getExportData({},{},{})", new Object[] { customer_id,
				customer_name, city_id });

		initData(); // 初始化map
		// 查询
		String sql = "select * from tab_customerinfo where 1=1 ";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select customer_id, customer_name, customer_pwd, customer_type, customer_size, customer_address, linkman," +
					" linkphone, email, mobile, customer_state, update_time, opendate, pausedate, closedate, " +
					" city_id, office_id, zone_id, customer_account " +
					" from tab_customerinfo where 1=1";
		}

		if (customer_id != null && !"".equals(customer_id)) {
			sql += " and customer_id like '%" + customer_id + "%' ";
		}
		if (customer_name != null && !"".equals(customer_name)) {
			sql += " and customer_name like '%" + customer_name + "%' ";
		}
		if (city_id != null && !"".equals(city_id)) {
			List list = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql += " and city_id in("
					+ StringUtils.weave(list) + ")";
			list = null;
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		// 字段处理
		return jt.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				// 客户名称
				map.put("customer_name", rs.getString("customer_name"));
				// 客户ID
				map.put("customer_id", rs.getString("customer_id"));
				// 客户账号
				map.put("customer_account", rs.getString("customer_account"));
				// 客户密码
				map.put("customer_pwd", rs.getString("customer_pwd"));
				// 客户类型
				map.put("customer_type", (String) typeMap.get(rs
						.getString("customer_type")));
				// 企业规模
				map.put("customer_size", (String) sizeMap.get(rs
						.getString("customer_size")));
				// 企业地址
				map.put("customer_address", rs.getString("customer_address"));
				// 联系人姓名
				map.put("linkman", rs.getString("linkman"));
				// 联系人电话
				map.put("linkphone", rs.getString("linkphone"));
				// 客户状态
				map.put("customer_state", (String) statusMap.get(String
						.valueOf(rs.getInt("customer_state"))));
				// 属地
				map.put("city_id", (String) cityMap
						.get(rs.getString("city_id")));
				// 局向
				map.put("office_id", (String) officeMap.get(rs
						.getString("office_id")));
				// 小区
				map.put("zone_id", (String) zoneMap
						.get(rs.getString("zone_id")));
				map.put("email", rs.getString("email"));
				return map;
			}
		});
	}

	/**
	 * 查询关联的用户数, 暂停的也算
	 * 
	 * @param customer_id
	 * 
	 * @return
	 */
	public int getUserNumByAll(String customer_id) {
		LOG.debug("getUserNumByAll({})", customer_id);

		String sql = "select count(1) from tab_egwcustomer where customer_id='"
				+ customer_id + "' and user_state in('1','2')";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select count(*) from tab_egwcustomer where customer_id='"
					+ customer_id + "' and user_state in('1','2')";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForInt(sql);
	}

	/**
	 * 查询关联的用户数
	 * 
	 * @param id
	 * @return
	 */
	public int getUserNum(String customer_id) {
		LOG.debug("getUserNum({})", customer_id);

		String sql = "select count(*) from tab_egwcustomer where customer_id='"
				+ customer_id + "' and user_state = '1'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForInt(sql);
	}

	/**
	 * 查询客户关联的设备帐号信息
	 * 
	 * @param customer_id
	 * @return
	 */
	public List getUserByCutomer(String customer_id) {
		LOG.debug("getUserByCutomer({})", customer_id);

		String sql = "select a.user_id,a.username,a.device_id,a.oui,a.device_serialnumber,b.loopback_ip,b.device_addr,c.device_model from tab_egwcustomer a "
				+ " left join tab_gw_device b on a.device_id=b.device_id "
				+ " left join gw_device_model c on b.device_model_id=c.device_model_id "
				+ " where a.user_state ='1' and a.customer_id='"
				+ customer_id
				+ "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}

	/**
	 * 确认该客户ID是否已经存在
	 * 
	 * @param customer_id
	 * @return
	 */
	public int comfirmCustomerId(String customer_id) {
		LOG.debug("comfirmCustomerId({})", customer_id);

		String sql = "select count(1) from tab_customerinfo where customer_id='"
				+ customer_id + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select count(*) from tab_customerinfo where customer_id='"
					+ customer_id + "'";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForInt(sql);
	}
}
