package com.linkage.module.gtms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings({"rawtypes","unchecked"})
public class CountDeviceByServTypeIdDAOImp extends SuperDAO implements CountDeviceByServTypeIdDAO
{
	private static Logger logger = LoggerFactory.getLogger(CountDeviceByServTypeIdDAOImp.class);

	private Map<String, String> cityMap = null;
	private Map<String, String> vendorMap = null;    // 厂商
	private Map<String, String> devicetypeMap = null;   // 版本
	private Map<String, String> deviceModelMap = null; // 型号


	/**
	 * 统计已开通业务量
	 */
	public List<Map> countHaveOpenningService(String gw_type, String cityId)
	{
		logger.debug("countHaveOpenningService()");

		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
		psql.append("select c.serv_type_id,a.city_id, count(distinct b.device_id) as num ");

		if ("2".equals(gw_type)) {// 企业网关
			psql.append("from tab_egwcustomer a,tab_gw_device b,egwcust_serv_info c ");
		}else {
			psql.append("from tab_hgwcustomer a,tab_gw_device b,hgwcust_serv_info c ");
		}

		psql.append("where a.device_id=b.device_id ");
		psql.append("and a.user_id=c.user_id ");
		psql.append("and b.device_status=1 "); // 已确认设备
		psql.append("and c.open_status= 1 ");  // 开通状态 1 表示成功
		psql.append("and b.gw_type="+gw_type+" ");
		psql.append("and b.cpe_allocatedstatus=1 ");  // 已绑定
		psql.append(getCity(cityId));
		psql.append("group by c.serv_type_id,a.city_id ");

		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 统计未开通业务量
	 */
	public List<Map> countHaveNotOpenningService(String gw_type, String cityId)
	{
		logger.debug("countHaveNotOpenningService()");

		PrepareSQL psql = new PrepareSQL();
		psql.append("select c.serv_type_id,a.city_id, count(distinct b.device_id) as num ");
		// TODO wait (more table related)
		if ("2".equals(gw_type)) { // 企业网关
			psql.append("from tab_egwcustomer a,tab_gw_device b,egwcust_serv_info c ");
		}else {
			psql.append("from tab_hgwcustomer a,tab_gw_device b,hgwcust_serv_info c ");
		}

		psql.append("where a.device_id=b.device_id ");
		psql.append("and a.user_id=c.user_id ");
//		psql.append("and b.device_status=1 "); // 已确认设备
		psql.append("and c.open_status!= 1 ");  // 开通状态 1 表示成功
		psql.append("and b.gw_type="+gw_type+" ");
//		psql.append("and b.cpe_allocatedstatus=1 ");  // 已绑定

		psql.append(getCity(cityId));
		psql.append("group by c.serv_type_id,a.city_id ");

		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 详细信息展示
	 */
	public List<Map> getDetail(String cityId, String gw_type, String servTypeId,
			String isOpen, int curPage_splitPage, int num_splitPage,long stime,long etime)
	{
		logger.debug("dao==>getDetail({},{},{})",cityId,gw_type,servTypeId);

		PrepareSQL psql = new PrepareSQL();
		if("11,14".equals(servTypeId))
		{
			psql.append("select distinct a.device_id,a.vendor_id,a.devicetype_id,a.device_model_id,");
			psql.append("a.device_serialnumber,a.city_id from tab_count_servtype_tmp a where 1=1 ");
			psql.append(getCity(cityId));
		}
		else
		{
			psql.append("select distinct b.device_id,b.vendor_id,b.devicetype_id,b.device_model_id,");
			psql.append("b.device_serialnumber,a.city_id ");
			// TODO wait (more table related)
			if ("2".equals(gw_type)) { // 企业网关
				psql.append("from tab_egwcustomer a,tab_gw_device b,egwcust_serv_info c ");
			}else {
				psql.append("from tab_hgwcustomer a,tab_gw_device b,hgwcust_serv_info c ");
			}

			psql.append("where a.device_id=b.device_id ");
			psql.append("and a.user_id=c.user_id ");
			psql.append("and b.gw_type="+gw_type+" ");
			psql.append("and c.serv_type_id="+servTypeId+" ");

			if ("1".equals(isOpen)) {
				psql.append("and b.device_status=1 "); // 已确认设备
				psql.append("and b.cpe_allocatedstatus=1 ");  // 已绑定
				psql.append("and c.open_status=1 ");  // 开通状态 1 表示成功
			}else if("2".equals(isOpen)){
				psql.append("and b.device_status=1 "); // 已确认设备
				psql.append("and b.cpe_allocatedstatus=1 ");  // 已绑定
			}else {
				psql.append("and c.open_status!= 1 ");  // 非1 表示不成功
			}

			psql.append(getTimeSql(stime,etime));
			psql.append(getCity(cityId));
		}

		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();           // 厂商
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();   // 版本
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap(); // 型号

		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();

				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap, city_id, "");
				map.put("city_name", city_name);
				map.put("city_id", city_id);

				map.put("vendor_name", StringUtil.getStringValue(vendorMap,rs.getString("vendor_id"),""));
				map.put("softwareversion", StringUtil.getStringValue(devicetypeMap,rs.getString("devicetype_id"),""));
				map.put("device_model", StringUtil.getStringValue(deviceModelMap,rs.getString("device_model_id"),""));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
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

	public int getCount(String cityId, String gw_type, String servTypeId,
			String isOpen, int curPage_splitPage, int num_splitPage,long stime,long etime)
	{
		logger.debug("dao==>getCount({},{},{})",cityId,gw_type,servTypeId);

		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(distinct a.device_id) ");

		if("11,14".equals(servTypeId))
		{
			psql.append("from tab_count_servtype_tmp a where 1=1 ");
			psql.append(getCity(cityId));
		}
		else
		{// TODO wait (more table related)
			if ("2".equals(gw_type)) { // 企业网关
				psql.append("from tab_egwcustomer a,tab_gw_device b,egwcust_serv_info c ");
			}else {
				psql.append("from tab_hgwcustomer a,tab_gw_device b,hgwcust_serv_info c ");
			}

			psql.append("where a.device_id=b.device_id ");
			psql.append("and a.user_id=c.user_id ");
			psql.append("and b.gw_type="+gw_type+" ");
			psql.append("and c.serv_type_id="+servTypeId+" ");

			if ("1".equals(isOpen)) {
				psql.append("and b.device_status=1 "); // 已确认设备
				psql.append("and b.cpe_allocatedstatus=1 ");  // 已绑定
				psql.append("and c.open_status=1 ");  // 开通状态 1 表示成功
			}else if("2".equals(isOpen)){
				psql.append("and b.device_status=1 "); // 已确认设备
				psql.append("and b.cpe_allocatedstatus=1 ");  // 已绑定
			}else {
				psql.append("and c.open_status!=1 ");  // 非1 表示不成功
			}

			psql.append(getTimeSql(stime,etime));
			psql.append(getCity(cityId));
		}

		int total = jt.queryForInt(psql.getSQL());
		if (total % num_splitPage == 0){
			return  total / num_splitPage;
		}
		return total / num_splitPage + 1;
	}

	/**
	 * 详细信息导出至Excel
	 */
	public List<Map> getDetailExcel(String cityId, String gw_type, String servTypeId, String isOpen,
			long stime,long etime)
	{
		logger.debug("dao==>getDetailExcel({},{},{})",cityId,gw_type,servTypeId);

		PrepareSQL psql = new PrepareSQL();
		if("11,14".equals(servTypeId))
		{
			psql.append("select distinct a.device_id,a.vendor_id,a.devicetype_id,");
			psql.append("a.device_model_id,a.device_serialnumber,a.city_id ");
			psql.append("from tab_count_servtype_tmp a where 1=1 ");
			psql.append(getCity(cityId));
		}
		else
		{
			psql.append("select distinct b.device_id,b.vendor_id,b.devicetype_id,");
			psql.append("b.device_model_id,b.device_serialnumber,a.city_id ");
			// TODO wait (more table related)
			if ("2".equals(gw_type)) { // 企业网关
				psql.append("from tab_egwcustomer a,tab_gw_device b,egwcust_serv_info c ");
			}else{
				psql.append("from tab_hgwcustomer a,tab_gw_device b,hgwcust_serv_info c ");
			}

			psql.append("where a.device_id=b.device_id ");
			psql.append("and a.user_id=c.user_id ");
			psql.append("and b.gw_type="+gw_type+" ");
			psql.append("and c.serv_type_id="+servTypeId+" ");

			if ("1".equals(isOpen)) {
				psql.append("and b.device_status=1 "); // 已确认设备
				psql.append("and b.cpe_allocatedstatus=1 ");  // 已绑定
				psql.append("and c.open_status=1 ");  // 开通状态 1 表示成功
			}else if("2".equals(isOpen)){
				psql.append("and b.device_status=1 "); // 已确认设备
				psql.append("and b.cpe_allocatedstatus=1 ");  // 已绑定
			}
			else {
				psql.append("and c.open_status!=1 ");  // 非1 表示不成功
			}

			psql.append(getTimeSql(stime,etime));
			psql.append(getCity(cityId));
		}


		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();           // 厂商
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();   // 版本
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap(); // 型号

		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();

				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap, city_id, "");
				map.put("city_name", city_name);
				map.put("city_id", city_id);

				map.put("vendor_name",StringUtil.getStringValue(vendorMap,rs.getString("vendor_id"), ""));
				map.put("softwareversion", StringUtil.getStringValue(devicetypeMap, rs.getString("devicetype_id"), ""));
				map.put("device_model", StringUtil.getStringValue(deviceModelMap, rs.getString("device_model_id"), ""));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
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

	@Override
	public List<Map> countHaveOpenService(String gw_type, String cityId,
			String servTypeId, long stime, long etime,String type)
	{
		logger.debug("dao==>countHaveOpenningService({},{},{})",cityId,servTypeId,gw_type);

		PrepareSQL psql = new PrepareSQL();
		if("11,14".equals(servTypeId))
		{
			long st=System.currentTimeMillis()/1000;

			if("count".equals(type))
			{
				psql.append("truncate table tab_count_servtype_tmp ");
				jt.execute(psql.getSQL());
				logger.warn("清空表tab_count_servtype_tmp,耗时:"+(System.currentTimeMillis()/1000 - st));

				psql = new PrepareSQL();
				psql.append("insert into tab_count_servtype_tmp(city_id,user_id,serv_type_id,device_id,");
				psql.append("device_serialnumber,vendor_id,devicetype_id,device_model_id) ");
				psql.append("select a.city_id,a.user_id,c.serv_type_id,b.device_id,");
				psql.append("b.device_serialnumber,b.vendor_id,b.devicetype_id,b.device_model_id ");
				// TODO wait (more table related)
				if ("2".equals(gw_type)) {// 企业网关
					psql.append("from tab_egwcustomer a,tab_gw_device b,egwcust_serv_info c ");
				}else {
					psql.append("from tab_hgwcustomer a,tab_gw_device b,hgwcust_serv_info c ");
				}

				psql.append("where a.device_id=b.device_id ");
				psql.append("and a.user_id=c.user_id ");
				psql.append("and b.device_status=1 "); // 已确认设备
				//psql.append("and c.open_status=1 ");  // 开通状态 1 表示成功
				psql.append("and b.gw_type="+gw_type+" ");
				psql.append("and b.cpe_allocatedstatus=1 ");  // 已绑定
				psql.append(getCity(cityId));
				psql.append(getTimeSql(stime,etime));
				psql.append("and c.serv_type_id in(11,10,14) ");

				jt.execute(psql.getSQL());
				logger.warn("插入表tab_count_servtype_tmp,耗时:"+(System.currentTimeMillis()/1000 - st));

				st=System.currentTimeMillis()/1000;
				psql = new PrepareSQL();
				psql.append("delete from tab_count_servtype_tmp where device_id in (");
				psql.append("select device_id from tab_count_servtype_tmp ");
				psql.append("where serv_type_id not in("+servTypeId+")) ");

				jt.execute(psql.getSQL());
				logger.warn("删除表tab_count_servtype_tmp多业务数据,耗时:"+(System.currentTimeMillis()/1000 - st));
			}

			psql = new PrepareSQL();
			st=System.currentTimeMillis()/1000;
			psql.append("select '14' as serv_type_id,city_id,count(distinct device_id) as num ");
			psql.append("from tab_count_servtype_tmp ");
			psql.append(" where  serv_type_id = 14 ");
			psql.append("group by city_id ");

			List list=jt.queryForList(psql.getSQL());
			logger.warn("统计表tab_count_servtype_tmp数据,耗时:"+(System.currentTimeMillis()/1000 - st));
			return list;
		}
		else
		{
			psql.append("select c.serv_type_id,a.city_id,count(distinct b.device_id) as num ");
			// TODO wait (more table related)
			if ("2".equals(gw_type)) {// 企业网关
				psql.append("from tab_egwcustomer a,tab_gw_device b,egwcust_serv_info c ");
			}else {
				psql.append("from tab_hgwcustomer a,tab_gw_device b,hgwcust_serv_info c ");
			}

			psql.append("where a.device_id=b.device_id ");
			psql.append("and a.user_id=c.user_id ");
			psql.append("and b.device_status=1 "); // 已确认设备

			//psql.append("and c.open_status=1 ");  // 开通状态 1 表示成功

			psql.append("and b.gw_type="+gw_type+" ");
			psql.append("and b.cpe_allocatedstatus=1 ");  // 已绑定
			psql.append(getCity(cityId));
			psql.append(getTimeSql(stime,etime));
			psql.append("and c.serv_type_id in("+servTypeId+") ");
			psql.append("group by c.serv_type_id,a.city_id ");

			return jt.queryForList(psql.getSQL());
		}
	}

	/**
	 * 拼装a表updatetime时间条件
	 */
	private String getTimeSql(long stime, long etime)
	{
		StringBuffer sb=new StringBuffer();
		if(stime>0){
			sb.append("and c.updatetime>="+stime+" ");
		}
		if(etime>0){
			sb.append("and c.updatetime<"+etime+" ");
		}
		return sb.toString();
	}

	/**
	 * 拼装a表属地条件
	 */
	private String getCity(String cityId)
	{
		if(cityId.indexOf("_local")>-1){
			return "and a.city_id='"+cityId.split("_")[0]+"' ";
		}else if(cityId.indexOf("_city_all")>-1){
			cityId=cityId.split("_")[0];
		}

		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			return "and a.city_id in ("+StringUtils.weave(cityIdList)+") ";
		}

		return "";
	}


}
