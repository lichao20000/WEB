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
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 网关设备统计DAO(家庭网关 flag==1 / 企业网关 flag==2)
 * 
 * @author 段光锐（5250）
 * @version 1.0
 * @since 2008-1-16
 * @category 资源管理
 */
public class CountDeviceDAO extends SuperDAO
{
	
	public String getIsAdminSQL()
	{
		return isAdminSQL;
	}

	
	public void setIsAdminSQL(String isAdminSQL)
	{
		this.isAdminSQL = isAdminSQL;
	}

	
	public Map<String, String> getCityMap()
	{
		return cityMap;
	}

	
	public void setCityMap(Map<String, String> cityMap)
	{
		this.cityMap = cityMap;
	}

	
	public Map<String, String> getVendorMap()
	{
		return vendorMap;
	}

	
	public void setVendorMap(Map<String, String> vendorMap)
	{
		this.vendorMap = vendorMap;
	}

	
	public Map<String, String> getDevicetypeMap()
	{
		return devicetypeMap;
	}

	
	public void setDevicetypeMap(Map<String, String> devicetypeMap)
	{
		this.devicetypeMap = devicetypeMap;
	}

	
	public Map<String, String> getDeviceModelMap()
	{
		return deviceModelMap;
	}

	
	public void setDeviceModelMap(Map<String, String> deviceModelMap)
	{
		this.deviceModelMap = deviceModelMap;
	}


	private static Logger logger = LoggerFactory.getLogger(CountDeviceDAO.class);

	private String isAdminSQL = null;
	
	private Map<String, String> cityMap = null;
	private Map<String, String> vendorMap = null;    // 厂商
	private Map<String, String> devicetypeMap = null;   // 版本 
	private Map<String, String> deviceModelMap = null; // 型号

	/**
	 * 初始化数据库连接
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}

	/**
	 * 查询家庭网关(HGW)设备的统计情况
	 * 
	 * @param isAdmin
	 *            是否是管理员
	 * @param areaid
	 *            当前用户的域信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getHGWDeviceCount(boolean isAdmin, long areaid)
	{
		if (Global.NXDX.equals(Global.instAreaShortName))
		{
			isAdmin(isAdmin, areaid);
			String unbindCountSql = "select city_id,count(1) as unbindnum from tab_gw_device where gw_type = 1 group by city_id"
					+ isAdminSQL + " order by city_id";
			String bindCountSql = "select a.city_id,count(1) as bindnum from tab_gw_device a,tab_hgwcustomer b where a.device_id = b.device_id and gw_type=1 group by a.city_id"
					+ isAdminSQL + " order by a.city_id";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				unbindCountSql = "select city_id,count(*) as unbindnum from tab_gw_device where gw_type = 1 group by city_id"
						+ isAdminSQL + " order by city_id";
				bindCountSql = "select a.city_id,count(*) as bindnum from tab_gw_device a,tab_hgwcustomer b where a.device_id = b.device_id and gw_type=1 group by a.city_id"
						+ isAdminSQL + " order by a.city_id";
			}

			PrepareSQL unbingpsql = new PrepareSQL(unbindCountSql);
			PrepareSQL bingpsql = new PrepareSQL(bindCountSql);
			List<Map> list1 = new ArrayList<Map>();
			List<Map> unbindlist = jt.queryForList(unbingpsql.getSQL());
			for (Map map : unbindlist)
			{
				Map m = new HashMap();
				m.put("city_id", map.get("city_id"));
				m.put("city_name", CityDAO.getCityName((String) map.get("city_id")));
				m.put("devicenum", map.get("unbindnum"));
				list1.add(m);
			}
			List<Map> bindlist = jt.queryForList(bingpsql.getSQL());
			//绑定和非绑定分开处理，按city_id关联为一组数据，存在某省市查询数据为空的情况，需要处理
			for (Map map : bindlist)
			{
				String cityid1 = (String) map.get("city_id");
				for (Map map1 : list1)
				{
					String cityid2 = (String) map1.get("city_id");
					//如果存在省市即添加数据即可
					if (cityid1.equals(cityid2))
					{
						map1.put("cusnum", map.get("bindnum"));
					}
				}
			}
			return list1;
		}else
		{
			isAdmin(isAdmin, areaid);
			String getHGWDeviceCountSQL = "select e.city_id,e.city_name,d.devicenum,d.cusnum from ("
					+ "select c.city_id,count(c.city_id) as devicenum,count(c.oui) cusnum from "
					+ "(select a.device_id,a.city_id,b.oui from "
					+ "(select device_id,city_id,oui,device_serialnumber from tab_gw_device where "
					+ "device_status=1 and gw_type=1 "
					+ isAdminSQL
					+ ") a "
					+ "left join "
					+ "(select device_id,oui,device_serialnumber from tab_hgwcustomer where user_state in ('1','2')) b "
					+ " on a.device_id=b.device_id "
					+ ") c group by c.city_id"
					+ ") d inner join tab_city e on d.city_id=e.city_id order by e.city_id";
			PrepareSQL psql = new PrepareSQL(getHGWDeviceCountSQL);
			psql.getSQL();
			List<Map> list = jt.query(getHGWDeviceCountSQL, new RowMapper()
			{
				public Object mapRow(ResultSet rs, int arg1)
						throws java.sql.SQLException
				{
					Map m = new HashMap();
					m.put("city_id", rs.getString("city_id"));
					m.put("city_name", rs.getString("city_name"));
					m.put("devicenum", rs.getBigDecimal("devicenum").toString());
					m.put("cusnum", rs.getBigDecimal("cusnum").toString());
					return m;
				}
			});
			return list;
		}
	}

	/**
	 * 查询企业网关(EGW)设备的统计情况
	 * 
	 * @param isAdmin
	 *            是否是管理员用户
	 * @param areaid
	 *            当前用户的域信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getEGWDeviceCount(boolean isAdmin, long areaid)
	{
		isAdmin(isAdmin, areaid);
//		String getEGWDeviceCountSQL = "select g.city_id,g.city_name,f.devicenum,f.cusnum from "
//				+ "(select e.city_id,count(e.city_id) as devicenum,count(e.flag) cusnum from "
//				+ "(select a.device_id,a.city_id,b.oui as flag from "
//				+ "(select device_id,city_id,oui,device_serialnumber from tab_gw_device where "
//				+ "device_status in(1,0) and gw_type=2 "
//				+ isAdminSQL
//				+ ") a left join "
//				+ "("
//				+ "select distinct oui,device_serialnumber from tab_egwcustomer where user_state in ('1','2')"
//				+ ") b on a.oui=b.oui "
//				+ "and a.device_serialnumber=b.device_serialnumber "
//				+ "union all "
//				+ "select c.device_id,c.city_id,d.device_id as flag from "
//				+ "(select device_id,city_id from tab_deviceresource where device_id in "
//				+ "	(select res_id from tab_gw_res_area where res_type=1 and area_id=1) "
//				+ "	and device_status=1 "
//				+ ") c left join "
//				+ "( "
//				+ "select distinct device_id from cus_radiuscustomer where user_state in ('1','2') "
//				+ ") d on c.device_id=d.device_id "
//				+ ") e group by e.city_id "
//				+ ") f inner join tab_city g on f.city_id=g.city_id order by g.city_id";
		String getEGWDeviceCountSQL = "select a.city_id,a.city_name,b.devicenum,b.cusnum "
				+ " from tab_city a,"
		 		+ " (select city_id,count(1) devicenum,count(case when cpe_allocatedstatus=1 then 1 else null end ) cusnum "
				+ " from tab_gw_device where device_status =1 and gw_type = 2 "
				+ isAdminSQL
				+ " group by city_id) b where a.city_id=b.city_id";

		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			getEGWDeviceCountSQL = "select a.city_id,a.city_name,b.devicenum,b.cusnum "
					+ " from tab_city a,"
					+ " (select city_id,count(*) devicenum,count(case when cpe_allocatedstatus=1 then 1 else null end ) cusnum "
					+ " from tab_gw_device where device_status =1 and gw_type = 2 "
					+ isAdminSQL
					+ " group by city_id) b where a.city_id=b.city_id";
		}
		
		PrepareSQL psql = new PrepareSQL(getEGWDeviceCountSQL);
		psql.getSQL();
		List<Map> list = jt.query(getEGWDeviceCountSQL, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1)
					throws java.sql.SQLException
			{
				Map m = new HashMap();
				m.put("city_id", rs.getString("city_id"));
				m.put("city_name", rs.getString("city_name"));
				m.put("devicenum", rs.getBigDecimal("devicenum").toString());
				m.put("cusnum", rs.getBigDecimal("cusnum").toString());
				return m;
			}
		});
		return list;
	}

	/**
	 * 获取全部tab_city表中数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getAllCityId()
	{
		String getAllCityIdSQL = "select city_id,parent_id,city_name from tab_city order by city_id";
		
		PrepareSQL psql = new PrepareSQL(getAllCityIdSQL);
		psql.getSQL();
		List<Map> list = jt.query(getAllCityIdSQL, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1)
					throws java.sql.SQLException
			{
				Map m = new HashMap();
				m.put("city_id", rs.getString("city_id"));
				m.put("city_name", rs.getString("city_name"));
				m.put("parent_id", rs.getString("parent_id"));
				return m;
			}
		});
		return list;
	}

	/**
	 * 根据是否为admin来拼装isAdminSQL
	 * 
	 * @param isAdmin
	 *            是否是管理员用户
	 * @param areaid
	 *            当前用户的域信息
	 */
	private void isAdmin(boolean isAdmin, long areaid)
	{
		if (!isAdmin)
		{
			isAdminSQL = " and device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id="
					+ areaid + ") ";
		} else
		{
			isAdminSQL = "";
		}
	}
	
	/*
	 * 
	 * 
	 */
	public List<Map>  getDetail(String gw_type, String cityId,String isBindState,int curPage_splitPage, int num_splitPage)
	{
		PrepareSQL psql = new PrepareSQL();
		
		psql.append("select a.device_id,device_serialnumber,vendor_id,devicetype_id,city_id,device_model_id from");
		psql.append(" ((select device_id,device_serialnumber,vendor_id,devicetype_id,device_model_id, city_id from tab_gw_device where device_status=1 and gw_type=" + gw_type);
		
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and city_id in (");
			psql.append(StringUtils.weave(cityIdList));
			psql.append(")");
			cityIdList = null;
		}
		if("1".equals(isBindState))
		{
			psql.append(" and cpe_allocatedstatus=1 ");
			
		}
		
		psql.append("  ) a ");
		
		if("2".equals(gw_type)){
			
			//企业网关
			psql.append(" left join (select device_id,oui from tab_egwcustomer where user_state in ('1','2') ) b  ");
		}else{
			//家庭网关
			psql.append(" left join (select device_id,oui from tab_hgwcustomer where user_state in ('1','2') ) b  ");
		}

		
		psql.append(" on a.device_id=b.device_id ) where 1=1  ");
		
//		if("1".equals(isBindState))
//		{
//			psql.append(" and b.oui is not null ");
//			
//		}else 
//		{
//			psql.append(" ");
//		}
		
		
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();           // 厂商
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();   // 版本 
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap(); // 型号
		logger.warn("CountDeviceDAO.getDetail(248)");
	
		//List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				
				Map<String, String> map = new HashMap<String, String>();
				logger.warn("mapRow() 255");
				// 属地
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap, city_id, "");
				map.put("city_name", city_name);
				map.put("city_id", city_id);
				
				// 厂商
				String vendorId = rs.getString("vendor_id");
				String vendorName = StringUtil.getStringValue(vendorMap, vendorId, "");
				map.put("vendor_name", vendorName);
				
				// 软件版本
				String devicetypeId = rs.getString("devicetype_id");
				String softwareversion = StringUtil.getStringValue(devicetypeMap, devicetypeId, "");
				map.put("softwareversion", softwareversion);
				
				// 型号
				String deviceModelId = rs.getString("device_model_id");
				String deviceModel = StringUtil.getStringValue(deviceModelMap, deviceModelId, "");
				map.put("device_model", deviceModel);
				
				// 设备序列号
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				
				// 设备ID
				map.put("device_id", rs.getString("device_id"));
				
				return map;
			}
		});
		
		cityMap = null;
		vendorMap = null;
		devicetypeMap = null;
		deviceModelMap = null;
		
		return list;
	}
	/*
	 * 
	 * 用于分页
	 */
	public int getCount(String gw_type, String cityId, String isBindState,
			 int curPage_splitPage, int num_splitPage) {
		PrepareSQL psql = new PrepareSQL();
		
		
//		if("1".equals(isBindState))
//		{
//			psql.append("select count(oui) from");
//		}else {
			//psql.append("select count(1) from");
//		}

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			psql.append("select count(*) from");
		}
		else {
			psql.append("select count(1) from");
		}
		
		psql.append(" ((select device_id,device_serialnumber,vendor_id,devicetype_id,device_model_id, city_id from tab_gw_device where device_status=1 and gw_type=" + gw_type);
		
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and city_id in (");
			psql.append(StringUtils.weave(cityIdList));
			psql.append(")");
			cityIdList = null;
		}
		if("1".equals(isBindState))
		{
			psql.append(" and cpe_allocatedstatus=1 ");
			
		}
		
		psql.append("  ) a ");
		
		if("2".equals(gw_type)){
			
			//企业网关
			psql.append(" left join (select device_id,oui from tab_egwcustomer where user_state in ('1','2')) b ");

		}else{
			psql.append(" left join (select device_id,oui from tab_hgwcustomer where user_state in ('1','2')) b ");
		}
//		if("1".equals(isBindState))
//		{
//			psql.append(" and oui is not null  ) b  ");
//			
//		}else 
//		{
//			psql.append(" ) b  ");
//		}
		
		
		psql.append(" on a.device_id=b.device_id )");
		
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
		
	}
	
	
	/**
	 * 详细信息导出至Excel
	 */
	public List<Map> getDetailExcel(String cityId, String gw_type, String isBindState){
		
		
		logger.debug("dao==>getDetailExcel({},{},{},{})", new Object[] { cityId, gw_type,
				isBindState });
		PrepareSQL psql = new PrepareSQL();
		
		psql.append("select a.device_id,device_serialnumber,vendor_id,devicetype_id,city_id,device_model_id from");
		psql.append(" ((select device_id,device_serialnumber,vendor_id,devicetype_id,device_model_id, city_id from tab_gw_device where device_status=1 and gw_type=" + gw_type);
		
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and city_id in (");
			psql.append(StringUtils.weave(cityIdList));
			psql.append(")");
			cityIdList = null;
		}
		if("1".equals(isBindState))
		{
			psql.append(" and cpe_allocatedstatus=1 ");
			
		}
		
		psql.append("  ) a ");
		
		if("2".equals(gw_type)){
			//企业网关
			psql.append(" left join (select device_id,oui from tab_egwcustomer where user_state in ('1','2') ) b  ");
		}else{
			psql.append(" left join (select device_id,oui from tab_hgwcustomer where user_state in ('1','2') ) b  ");
		}
		
		psql.append(" on a.device_id=b.device_id ) where 1=1  ");
		
//		if("1".equals(isBindState))
//		{
//			psql.append(" and b.oui is not null ");
//			
//		}else 
//		{
//			psql.append(" ");
//		}
		
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();           // 厂商
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();   // 版本 
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap(); // 型号
		
		@SuppressWarnings("unchecked")
		List<Map> list = jt.query(psql.getSQL(), new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				
				Map<String, String> map = new HashMap<String, String>();
				
				// 属地
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap, city_id, "");
				map.put("city_name", city_name);
				map.put("city_id", city_id);
				
				// 厂商
				String vendorId = rs.getString("vendor_id");
				String vendorName = StringUtil.getStringValue(vendorMap, vendorId, "");
				map.put("vendor_name", vendorName);
				
				// 软件版本
				String devicetypeId = rs.getString("devicetype_id");
				String softwareversion = StringUtil.getStringValue(devicetypeMap, devicetypeId, "");
				map.put("softwareversion", softwareversion);
				
				// 型号
				String deviceModelId = rs.getString("device_model_id");
				String deviceModel = StringUtil.getStringValue(deviceModelMap, deviceModelId, "");
				map.put("device_model", deviceModel);
				
				// 设备序列号
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				
				// 设备ID
				map.put("device_id", rs.getString("device_id"));
				
				return map;
			}
		});
		
		cityMap = null;
		vendorMap = null;
		devicetypeMap = null;
		deviceModelMap = null;
		return list;
		
	}
	
	
	
}
