/**
 * 
 */
package com.linkage.module.gwms.sysConfig.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

import dao.util.JdbcTemplateExtend;

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
	 * 查询设备
	 * 
	 * @param cityId 属地：如果不需要过滤，则传入null
	 * @param deviceNo 设备序列号：如果不需要过滤，则传入null
	 * @param gwType 该字段不可为空
	 * @return
	 */
	public List<Map<String,String>> getDeviceInfo(String cityId, String deviceNo,String loopbackIp) 
	{
		logger.debug("UserInstReleaseDAO=>getDeviceInfo(cityId:{},deviceNo:{},loopbackIp:{})",
						new Object[] { cityId, deviceNo, loopbackIp});
		PrepareSQL ppSQL = new PrepareSQL();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		ppSQL.setSQL("select a.device_id,a.city_id,a.oui,a.device_serialnumber,a.vendor_id," +
				"a.device_model_id,a.devicetype_id,a.loopback_ip,b.vendor_add,c.device_model,d.softwareversion " +
				"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d " +
				"where a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id " +
				"and a.devicetype_id=d.devicetype_id ");
		if (null != deviceNo && !"".equals(deviceNo)) {
			if(deviceNo.length()>5){
				ppSQL.appendAndString("a.dev_sub_sn", PrepareSQL.EQUEAL, deviceNo.substring(deviceNo.length()-6, deviceNo.length()));
			}			
			ppSQL.appendAndString("a.device_serialnumber", PrepareSQL.L_LIKE,deviceNo);
		}
		if (null != loopbackIp && !"".equals(loopbackIp)) {			
			ppSQL.appendAndString("a.loopback_ip", PrepareSQL.EQUEAL, loopbackIp);
		}
		if (null != cityId && !"".equals(cityId) && !"00".equals(cityId)) {

			List<String> list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			List<String> list1 = CityDAO.getAllPcityIdByCityId(cityId);
			list.addAll(list1);
			ppSQL.append(PrepareSQL.AND, "a.city_id", list);
			list = null;
			list1 = null;
		}

		List rs = jt.queryForList(ppSQL.toString());
		
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		for(int i=0;i<rs.size();i++){
		
			Map one = (Map)rs.get(i);
			
			Map<String, String> map = new HashMap<String, String>();
			
			map.put("device_id", String.valueOf(one.get("device_id")));
			map.put("city_id", String.valueOf(one.get("city_id")));
			map.put("city_name", cityMap.get(String.valueOf(one.get("city_id"))));
			map.put("oui", String.valueOf(one.get("oui")));
			map.put("device_serialnumber", String.valueOf(one.get("device_serialnumber")));
			map.put("vendor_id", String.valueOf(one.get("vendor_id")));
			map.put("vendor_add", String.valueOf(one.get("vendor_add")));
			map.put("device_model_id", String.valueOf(one.get("device_model_id")));
			map.put("device_model", String.valueOf(one.get("device_model")));
			map.put("devicetype_id", String.valueOf(one.get("devicetype_id")));
			map.put("softwareversion", String.valueOf(one.get("softwareversion")));
			map.put("loopback_ip", String.valueOf(one.get("loopback_ip")));
			result.add(map);
		}
		cityMap = null;
		return result;
	}
	
	public List getCorbaIor(){
		PrepareSQL pSQL = new PrepareSQL("select ior from tab_ior where object_name=?");
		pSQL.setString(1, "BusinessLogic");

		return jt.queryForList(pSQL.toString());
		
	}

}
