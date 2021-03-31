/**
 * 
 */
package dao.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-7-28
 * @category dao.resource
 * 
 */
public class DeviceManageDataDAO {
	
	//jdbc模板
	private JdbcTemplate jt;
	
	/**
	 * 初始化数据库连接
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}
	
	public List getData(String start_time,String end_time,String username,String cityId){
		
		String sqloui = "select b.oui,a.vendor_id,a.vendor_name,a.vendor_add "
		 + " from tab_vendor a, tab_vendor_oui b where a.vendor_id=b.vendor_id";
		PrepareSQL psql = new PrepareSQL(sqloui);
		psql.getSQL();
		List ouiList = jt.queryForList(sqloui);
		Map<String, String> ouiMap = new HashMap<String, String>();
		
		for(int i=0;i<ouiList.size();i++){
			Map temp = (Map) ouiList.get(i);
			ouiMap.put(String.valueOf(temp.get("oui")), String.valueOf(temp.get("vendor_name")));
		}
		
		String sqlCity = "select city_id,city_name from tab_city";
		psql = new PrepareSQL(sqlCity);
		psql.getSQL();
		List cityList = jt.queryForList(sqlCity);
		Map<String, String> cityMap = new HashMap<String, String>();
		
		for(int i=0;i<cityList.size();i++){
			Map temp = (Map) cityList.get(i);
			cityMap.put(String.valueOf(temp.get("city_id")), String.valueOf(temp.get("city_name")));
		}
		
		StringBuffer sql = new StringBuffer();

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql.append("select a.username, a.outdate, a.city_id, b.oui,b.device_serialnumber from tab_outlineuser a, ");
		}
		else {
			sql.append("select a.*,b.oui,b.device_serialnumber from tab_outlineuser a, ");
		}
		sql.append(" tab_hgwcustomer b where 1=1 and a.username = b.username and b.user_state = '1' ");
		
		//查询条件：时间
		if (null!= start_time && !"".equals(start_time)){ 
			sql.append(" and a.outdate > " + start_time);
		}
		if (null != end_time && !"".equals(end_time)){
			sql.append(" and a.outdate <= " + end_time);
		}

		//查询条件：用户帐号
		if (null !=  username && !"".equals(username)){
			sql.append(" and a.username = '" + username + "'" );
		}

		//属地条件查询，若是“00”即省中心则不需要
		if (null != cityId && !"00".equals(cityId)){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			String cityStr = cityId + "','" + StringUtils.weave(cityArray,"','");
			sql.append(" and a.city_id in ('" + cityStr + "')");
			cityArray = null;
		}

		sql.append(" order by a.outdate ");
		psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		List list = jt.queryForList(sql.toString());
		
		List<Map<String, String>> rs = new ArrayList<Map<String, String>>();
		for(int i=0;i<list.size();i++){
			
			Map temp = (Map) list.get(i);
			Map<String, String> one = new HashMap<String, String> ();
			one.put("username", String.valueOf(temp.get("username")));
			one.put("outdate", String.valueOf((new DateTimeUtil(Long.parseLong(String.valueOf(temp.get("outdate")))*1000)).getLongDate()));
			one.put("city_id", cityMap.get(temp.get("city_id")));
			one.put("oui",ouiMap.get(temp.get("oui")));
			one.put("device_serialnumber", String.valueOf(temp.get("device_serialnumber")));
			rs.add(one);
		}
		return rs;
	}
}
