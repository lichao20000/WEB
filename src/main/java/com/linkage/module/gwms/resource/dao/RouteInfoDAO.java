
package com.linkage.module.gwms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.resource.obj.RouteInfoBean;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.litms.common.util.JdbcTemplateExtend;

public class RouteInfoDAO
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(RouteInfoDAO.class);
	// 查询多个最多只能查询这么多条
	private int maxNum = 10000;
	private JdbcTemplateExtend jt;

	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}

	//650  deviceSn   多条件查询
	public List queryDevice(int curPage_splitPage,int num_splitPage,RouteInfoBean bean){
		logger.debug("RouteInfoDAO=>queryDevice()");
		String tableName = "tab_hgwcustomer";
		if(!StringUtil.IsEmpty(bean.getGw_type())&&"2".equals(bean.getGw_type())){
			tableName = "tab_egwcustomer";
		}
		PrepareSQL pSQL = new PrepareSQL();
		if (DBUtil.GetDB() == 2) {
			pSQL.append("select top " + maxNum );
		}
		else
		{
			pSQL.append("select");
		}
		if("1".equals(bean.getQuery_type())&&"4".equals(bean.getQueryType())){
			pSQL.append(" a.device_serialnumber,a.city_id,b.vendor_add,c.device_model,d.softwareversion,d.hardwareversion,e.username loid,f.username kdname " +
					"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,"+tableName+" e,hgwcust_serv_info f, tab_voip_serv_param g " +
					"where e.user_id=f.user_id and a.device_id=e.device_id and a.device_status =1 and  a.vendor_id=b.vendor_id and " +
					"a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id and e.user_id=g.user_id and a.device_status = 1 and f.serv_type_id = 10 ");
		}else{
			pSQL.append(" a.device_serialnumber,a.city_id,b.vendor_add,c.device_model,d.softwareversion,d.hardwareversion,e.username loid,f.username kdname" +
					" from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,"+tableName+" e,hgwcust_serv_info f where e.user_id=f.user_id " +
					"and a.device_id=e.device_id and a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and " +
					"a.devicetype_id=d.devicetype_id and f.serv_type_id = 10 ");
		}
		
		if(null!=bean.getGwShare_cityId() && !"null".equals(bean.getGwShare_cityId()) && !"".equals(bean.getGwShare_cityId()) && !"-1".equals(bean.getGwShare_cityId()) && !"00".equals(bean.getGwShare_cityId())){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(bean.getGwShare_cityId());
			pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
			cityArray = null;
		}
		if(null!=bean.getGwShare_vendorId() && !"null".equals(bean.getGwShare_vendorId() ) && !"".equals(bean.getGwShare_vendorId() ) && !"-1".equals(bean.getGwShare_vendorId() )){
			pSQL.appendAndString(" a.vendor_id", PrepareSQL.EQUEAL, bean.getGwShare_vendorId() );
		}
		if(null!=bean.getGwShare_deviceModelId() && !"null".equals(bean.getGwShare_deviceModelId()) && !"".equals(bean.getGwShare_deviceModelId()) && !"-1".equals(bean.getGwShare_deviceModelId())){
			pSQL.appendAndString(" a.device_model_id", PrepareSQL.EQUEAL, bean.getGwShare_deviceModelId());
		}
		if(null!=bean.getGwShare_devicetypeId() && !"null".equals(bean.getGwShare_devicetypeId()) && !"".equals(bean.getGwShare_devicetypeId()) && !"-1".equals(bean.getGwShare_devicetypeId())){
			pSQL.appendAndNumber(" a.devicetype_id", PrepareSQL.EQUEAL, bean.getGwShare_devicetypeId());
		}
		if(null!=bean.getGwShare_netType() && !"".equals(bean.getGwShare_netType())){
			pSQL.appendAndNumber(" f.wan_type", PrepareSQL.EQUEAL, bean.getGwShare_netType());
		}
		if("2".equals(bean.getQuery_type())){
			if(null!=bean.getStart_time() && !"null".equals(bean.getStart_time()) && !"".equals(bean.getStart_time()) && !"0".equals(bean.getStart_time())){
				pSQL.append(" and e.opendate >= "+bean.getStart_time());
			}
			if(null!=bean.getEnd_time() && !"null".equals(bean.getEnd_time()) && !"".equals(bean.getEnd_time()) && !"0".equals(bean.getEnd_time())){
				pSQL.append(" and e.opendate <= "+bean.getEnd_time());
			}
		}else{
			if("1".equals(bean.getQueryType())){
				String deviceSerialnumber = bean.getSingle_condition();
				if(null!= bean.getSingle_condition()&& !"".equals(bean.getSingle_condition()) && !"-1".equals(bean.getQueryType())){
					if(deviceSerialnumber.length()>5){
						pSQL.append(" and a.dev_sub_sn ='");
						pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length()-6, deviceSerialnumber.length()));
						pSQL.append("' ");
					}
					pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE, deviceSerialnumber, false);
				}
			}else if("2".equals(bean.getQueryType())){
				if(!StringUtil.IsEmpty(bean.getSingle_condition()))
				{
					pSQL.append(" and f.username='" + bean.getSingle_condition() + "'");
				}
			}else if("3".equals(bean.getQueryType())){
				if(null!=bean.getSingle_condition() && !"".equals(bean.getSingle_condition())){
					pSQL.appendAndString("e.username", PrepareSQL.EQUEAL, bean.getSingle_condition());
				}
			}else if("4".equals(bean.getQueryType())){
				if (null != bean.getSingle_condition() && !"".equals(bean.getSingle_condition())) {
					pSQL.append("   and g.voip_phone = '" + bean.getSingle_condition() + "'");
				}
			}
		}
		
		if (null !=bean.getGw_type() && !"".equals(bean.getGw_type())&& !"null".equals(bean.getGw_type()) ) {
			pSQL.append(" and a.gw_type = " + bean.getGw_type() );
		}
		if (DBUtil.GetDB() == 1) {
			pSQL.append(" and rownum< " + maxNum );
		}
		pSQL.append(" order by a.complete_time");
		return jt.querySP(pSQL.toString(), (curPage_splitPage - 1) * num_splitPage,num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	public int queryDeviceCount(RouteInfoBean bean, int curPage_splitPage, int num_splitPage){
		logger.debug("RouteInfoDAO=>queryDevice()");
		String tableName = "tab_hgwcustomer";
		if(!StringUtil.IsEmpty(bean.getGw_type())&&"2".equals(bean.getGw_type())){
			tableName = "tab_egwcustomer";
		}
		PrepareSQL pSQL = new PrepareSQL();
		if (DBUtil.GetDB() == 2) {
			pSQL.append("select top " + maxNum );
		}
		else
		{
			pSQL.append("select");
		}
		if("1".equals(bean.getQuery_type())&&"4".equals(bean.getQueryType())){
			pSQL.append(" count(1) from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,"+tableName+" e,hgwcust_serv_info f, tab_voip_serv_param g " +
					"where e.user_id=f.user_id and a.device_id=e.device_id and a.device_status =1 and  a.vendor_id=b.vendor_id and " +
					"a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id and e.user_id=g.user_id and a.device_status = 1 and f.serv_type_id = 10 ");
		}else{
			pSQL.append(" count(1) from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,"+tableName+" e,hgwcust_serv_info f where e.user_id=f.user_id " +
					"and a.device_id=e.device_id and a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and " +
					"a.devicetype_id=d.devicetype_id and f.serv_type_id = 10 ");
		}
		
		if(null!=bean.getGwShare_cityId() && !"null".equals(bean.getGwShare_cityId()) && !"".equals(bean.getGwShare_cityId()) && !"-1".equals(bean.getGwShare_cityId()) && !"00".equals(bean.getGwShare_cityId())){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(bean.getGwShare_cityId());
			pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
			cityArray = null;
		}
		if(null!=bean.getGwShare_vendorId() && !"null".equals(bean.getGwShare_vendorId() ) && !"".equals(bean.getGwShare_vendorId() ) && !"-1".equals(bean.getGwShare_vendorId() )){
			pSQL.appendAndString(" a.vendor_id", PrepareSQL.EQUEAL, bean.getGwShare_vendorId() );
		}
		if(null!=bean.getGwShare_deviceModelId() && !"null".equals(bean.getGwShare_deviceModelId()) && !"".equals(bean.getGwShare_deviceModelId()) && !"-1".equals(bean.getGwShare_deviceModelId())){
			pSQL.appendAndString(" a.device_model_id", PrepareSQL.EQUEAL, bean.getGwShare_deviceModelId());
		}
		if(null!=bean.getGwShare_devicetypeId() && !"null".equals(bean.getGwShare_devicetypeId()) && !"".equals(bean.getGwShare_devicetypeId()) && !"-1".equals(bean.getGwShare_devicetypeId())){
			pSQL.appendAndNumber(" a.devicetype_id", PrepareSQL.EQUEAL, bean.getGwShare_devicetypeId());
		}
		if(null!=bean.getGwShare_netType() && !"".equals(bean.getGwShare_netType())){
			pSQL.appendAndNumber(" f.wan_type", PrepareSQL.EQUEAL, bean.getGwShare_netType());
		}
		if("2".equals(bean.getQuery_type())){
			if(null!=bean.getStart_time() && !"null".equals(bean.getStart_time()) && !"".equals(bean.getStart_time()) && !"0".equals(bean.getStart_time())){
				pSQL.append(" and e.opendate >= "+bean.getStart_time());
			}
			if(null!=bean.getEnd_time() && !"null".equals(bean.getEnd_time()) && !"".equals(bean.getEnd_time()) && !"0".equals(bean.getEnd_time())){
				pSQL.append(" and e.opendate <= "+bean.getEnd_time());
			}
		}else{
			if("1".equals(bean.getQueryType())){
				String deviceSerialnumber = bean.getSingle_condition();
				if(null!= bean.getSingle_condition()&& !"".equals(bean.getSingle_condition()) && !"-1".equals(bean.getQueryType())){
					if(deviceSerialnumber.length()>5){
						pSQL.append(" and a.dev_sub_sn ='");
						pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length()-6, deviceSerialnumber.length()));
						pSQL.append("' ");
					}
					pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE, deviceSerialnumber, false);
				}
			}else if("2".equals(bean.getQueryType())){
				if(!StringUtil.IsEmpty(bean.getSingle_condition()))
				{
					pSQL.append(" and f.username='" + bean.getSingle_condition() + "'");
				}
			}else if("3".equals(bean.getQueryType())){
				if(null!=bean.getSingle_condition() && !"".equals(bean.getSingle_condition())){
					pSQL.appendAndString("e.username", PrepareSQL.EQUEAL, bean.getSingle_condition());
				}
			}else if("4".equals(bean.getQueryType())){
				if (null != bean.getSingle_condition() && !"".equals(bean.getSingle_condition())) {
					pSQL.append("   and g.voip_phone = '" + bean.getSingle_condition() + "'");
				}
			}
		}
		
		if (null !=bean.getGw_type() && !"".equals(bean.getGw_type())&& !"null".equals(bean.getGw_type()) ) {
			pSQL.append(" and a.gw_type = " + bean.getGw_type() );
		}
		
		
		int total = jt.queryForInt(pSQL.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0)
			maxPage = total / num_splitPage;
		else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	public List queryDevice(RouteInfoBean bean){
		logger.debug("RouteInfoDAO=>queryDevice()");
		String tableName = "tab_hgwcustomer";
		if(!StringUtil.IsEmpty(bean.getGw_type())&&"2".equals(bean.getGw_type())){
			tableName = "tab_egwcustomer";
		}
		PrepareSQL pSQL = new PrepareSQL();
		if (DBUtil.GetDB() == 2) {
			pSQL.append("select top " + maxNum );
		}
		else
		{
			pSQL.append("select");
		}
		if("1".equals(bean.getQuery_type())&&"4".equals(bean.getQueryType())){
			pSQL.append(" a.device_serialnumber,a.city_id,b.vendor_add,c.device_model,d.softwareversion,d.hardwareversion,e.username loid,f.username kdname " +
					"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,"+tableName+" e,hgwcust_serv_info f, tab_voip_serv_param g " +
					"where e.user_id=f.user_id and a.device_id=e.device_id and a.device_status =1 and  a.vendor_id=b.vendor_id and " +
					"a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id and e.user_id=g.user_id and a.device_status = 1 and f.serv_type_id = 10 ");
		}else{
			pSQL.append(" a.device_serialnumber,a.city_id,b.vendor_add,c.device_model,d.softwareversion,d.hardwareversion,e.username loid,f.username kdname" +
					" from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,"+tableName+" e,hgwcust_serv_info f where e.user_id=f.user_id " +
					"and a.device_id=e.device_id and a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and " +
					"a.devicetype_id=d.devicetype_id and f.serv_type_id = 10 ");
		}
		
		if(null!=bean.getGwShare_cityId() && !"null".equals(bean.getGwShare_cityId()) && !"".equals(bean.getGwShare_cityId()) && !"-1".equals(bean.getGwShare_cityId()) && !"00".equals(bean.getGwShare_cityId())){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(bean.getGwShare_cityId());
			pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
			cityArray = null;
		}
		if(null!=bean.getGwShare_vendorId() && !"null".equals(bean.getGwShare_vendorId() ) && !"".equals(bean.getGwShare_vendorId() ) && !"-1".equals(bean.getGwShare_vendorId() )){
			pSQL.appendAndString(" a.vendor_id", PrepareSQL.EQUEAL, bean.getGwShare_vendorId() );
		}
		if(null!=bean.getGwShare_deviceModelId() && !"null".equals(bean.getGwShare_deviceModelId()) && !"".equals(bean.getGwShare_deviceModelId()) && !"-1".equals(bean.getGwShare_deviceModelId())){
			pSQL.appendAndString(" a.device_model_id", PrepareSQL.EQUEAL, bean.getGwShare_deviceModelId());
		}
		if(null!=bean.getGwShare_devicetypeId() && !"null".equals(bean.getGwShare_devicetypeId()) && !"".equals(bean.getGwShare_devicetypeId()) && !"-1".equals(bean.getGwShare_devicetypeId())){
			pSQL.appendAndNumber(" a.devicetype_id", PrepareSQL.EQUEAL, bean.getGwShare_devicetypeId());
		}
		if(null!=bean.getGwShare_netType() && !"".equals(bean.getGwShare_netType())){
			pSQL.appendAndNumber(" f.wan_type", PrepareSQL.EQUEAL, bean.getGwShare_netType());
		}
		if("2".equals(bean.getQuery_type())){
			if(null!=bean.getStart_time() && !"null".equals(bean.getStart_time()) && !"".equals(bean.getStart_time()) && !"0".equals(bean.getStart_time())){
				pSQL.append(" and e.opendate >= "+bean.getStart_time());
			}
			if(null!=bean.getEnd_time() && !"null".equals(bean.getEnd_time()) && !"".equals(bean.getEnd_time()) && !"0".equals(bean.getEnd_time())){
				pSQL.append(" and e.opendate <= "+bean.getEnd_time());
			}
		}else{
			if("1".equals(bean.getQueryType())){
				String deviceSerialnumber = bean.getSingle_condition();
				if(null!= bean.getSingle_condition()&& !"".equals(bean.getSingle_condition()) && !"-1".equals(bean.getQueryType())){
					if(deviceSerialnumber.length()>5){
						pSQL.append(" and a.dev_sub_sn ='");
						pSQL.append(deviceSerialnumber.substring(deviceSerialnumber.length()-6, deviceSerialnumber.length()));
						pSQL.append("' ");
					}
					pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE, deviceSerialnumber, false);
				}
			}else if("2".equals(bean.getQueryType())){
				if(!StringUtil.IsEmpty(bean.getSingle_condition()))
				{
					pSQL.append(" and f.username='" + bean.getSingle_condition() + "'");
				}
			}else if("3".equals(bean.getQueryType())){
				if(null!=bean.getSingle_condition() && !"".equals(bean.getSingle_condition())){
					pSQL.appendAndString("e.username", PrepareSQL.EQUEAL, bean.getSingle_condition());
				}
			}else if("4".equals(bean.getQueryType())){
				if (null != bean.getSingle_condition() && !"".equals(bean.getSingle_condition())) {
					pSQL.append("   and g.voip_phone = '" + bean.getSingle_condition() + "'");
				}
			}
		}
		
		if (null !=bean.getGw_type() && !"".equals(bean.getGw_type())&& !"null".equals(bean.getGw_type()) ) {
			pSQL.append(" and a.gw_type = " + bean.getGw_type() );
		}
		if (DBUtil.GetDB() == 1) {
			pSQL.append(" and rownum< " + maxNum );
		}
		List list = jt.queryForList(pSQL.getSQL());
		return listForExcel((List<Map>)list);

	}

	

	/**
	 * 数据转换
	 * @param map
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	public Map<String, String> resultSet2Map(Map<String, String> map,ResultSet rs) {
		try{
			map.put("device_serialnumber", rs.getString("device_serialnumber"));
			String cityId = CityDAO.getCityIdPidMap().get(rs.getString("city_id"));
			map.put("city_name", CityDAO.getCityIdCityNameMap().get(rs.getString("city_id")));
			map.put("cityName", CityDAO.getCityIdCityNameMap().get(cityId));
			map.put("softwareversion", rs.getString("softwareversion"));
			map.put("hardwareversion", rs.getString("hardwareversion"));
			map.put("device_model", rs.getString("device_model"));
			map.put("vendor_add", rs.getString("vendor_add"));
			map.put("loid", rs.getString("loid"));
			map.put("kdname", rs.getString("kdname"));
		}catch(SQLException e){
			logger.error(e.getMessage());
		}
		return map;
	}
	public List listForExcel(List<Map> list) {

		for(Map map:list){
			String cityId = CityDAO.getCityIdPidMap().get(map.get("city_id"));
			map.put("city_name", CityDAO.getCityIdCityNameMap().get(map.get("city_id")));
			map.put("cityName", CityDAO.getCityIdCityNameMap().get(cityId));
		}
		return list;
	}
}
