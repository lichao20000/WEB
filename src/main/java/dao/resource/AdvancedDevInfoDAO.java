package dao.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2009-5-22
 */
public class AdvancedDevInfoDAO {
	private static Logger logger = LoggerFactory.getLogger(AdvancedDevInfoDAO.class);
	// jdbc模板
	private JdbcTemplate jt;

	/**
	 * 根据域过滤设备列表
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-5-26
	 * @return List
	 */
	public List queryDevice(long areaId, String devSn) {

		String strSQL = "select a.device_id,a.oui,a.device_serialnumber,a.city_id,a.loopback_ip,a.gw_type,a.customer_id from tab_gw_device a, tab_gw_res_area b "
				+ " where a.device_id=b.res_id and b.res_type=1"
				+ " and b.area_id=? and a.device_serialnumber like '%?'";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setLong(1, areaId);
		psql.setStringExt(2, devSn, false);

		strSQL = psql.getSQL();
		
		if(false == StringUtil.IsEmpty(devSn) && devSn.length()>5){
			strSQL += " and a.dev_sub_sn ='" + devSn.substring(devSn.length()-6, devSn.length()) + "'";
		}
		
		List devList = jt.queryForList(strSQL);
		
		return devList;
	}

	/**
	 * 不做权限控制，为admin.com域的用户查询时
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-5-26
	 * @return List
	 */
	public List queryDevice(String devSn) {

		String strSQL = "select a.device_id,a.oui,a.device_serialnumber,a.city_id,a.loopback_ip,a.gw_type,a.customer_id from tab_gw_device a"
				+ " where a.device_serialnumber like '%?' and gw_type = 2 ";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setStringExt(1, devSn, false);
		
		strSQL = psql.getSQL();
		
		if(false == StringUtil.IsEmpty(devSn) && devSn.length()>5){
			strSQL += " and a.dev_sub_sn ='" + devSn.substring(devSn.length()-6, devSn.length()) + "'";
		}
		
		List devList = jt.queryForList(strSQL);
		return devList;
	}
	
	
	/**
	 * 处理设备查询出来之后的属地，city_id->city_name
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-5-26
	 * @return List
	 */
	public List procList(List devList){
		if(null == devList){
			return null;
		}
		List resultList = new ArrayList();
		Map tempMap = null;
		int size = devList.size();
		Map cityMap = CityDAO.getCityIdCityNameMap();
		for(int i = 0; i < size; i++){
			tempMap = (Map)devList.get(i);
			Map devMap = new HashMap();
			devMap.putAll(tempMap);
			String cityName = String.valueOf(cityMap.get(tempMap.get("city_id")));
			devMap.put("city_name", cityName);
			
			
			//获取客户信息,只有企业网关才有
			//String gw_type = String.valueOf(tempMap.get("gw_type"));
			//if("2".equals(gw_type))
			//{
				String device_id = String.valueOf(tempMap.get("device_id"));
				//logger.info("=========device_id:{}",device_id);
				devMap.putAll(getCustomerInfo(device_id));
			//}
			resultList.add(devMap);
		}
		cityMap = null;
		return resultList;
	}
	
	private Map getCustomerInfo(String device_id)
	{
		//logger.info("getCustomerInfo=========device_id:{}",device_id);
		Map data = new HashMap();
		String user_id = null;
		String username = null;
		String customer_name = null;
		
		//用户信息
		String strSQL = "select user_id,username from tab_egwcustomer a,tab_gw_serv_type b where a.serv_type_id=b.serv_type_id and device_id='" + device_id
		+ "' and user_state in('1','2')";
		
		PrepareSQL psql = new PrepareSQL(strSQL);
		strSQL = psql.getSQL();
		
		List devList = jt.queryForList(strSQL);
		if(null != devList && !devList.isEmpty())
		{
			user_id = String.valueOf(((Map)devList.get(0)).get("user_id"));
			username = String.valueOf(((Map)devList.get(0)).get("username"));
			//logger.info("getCustomerInfo=========user_id:{}",user_id);
			//logger.info("getCustomerInfo=========username:{}",username);
		}
		
		
		//客户信息
		strSQL = "select customer_id,customer_name from tab_customerinfo"
		+ "  where customer_id in (select customer_id from tab_gw_device where device_id='"
		+ device_id + "')";

		psql = new PrepareSQL(strSQL);
		strSQL = psql.getSQL();
		
		devList = jt.queryForList(strSQL);
		if(null != devList && !devList.isEmpty())
		{
			customer_name = String.valueOf(((Map)devList.get(0)).get("customer_name"));
			//logger.info("getCustomerInfo=========customer_name:{}",customer_name);
		}
		data.put("user_id", user_id);
		data.put("username", username);
		data.put("customer_name", customer_name);
		
		return data;
	}

	/**
	 * 初始化数据库连接
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}
}
