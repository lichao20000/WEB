package com.linkage.module.gtms.config.serv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.util.CreateObjectFactory;

public class AlarmConfigServImpl implements AlarmConfigServ {
	private static Logger logger = LoggerFactory
	.getLogger(AlarmConfigServImpl.class);

	/**
	 * 配置参数节点
	 */
	public String doAlarmConfig(String[] deviceIds, String serviceId,
			String[] paramArr, String gwType) {
		logger.debug("serv-->doAlarmConfig({},{},{},{})", new Object[] { deviceIds,
				serviceId, paramArr,gwType });
		try
		{
			//调用接口
			logger.warn("开始调用配置模块进行配量参数配置(deviceIds：{},serviceId：{},paramArr：{})", new Object[] { deviceIds,
				serviceId, paramArr });
			int ret = CreateObjectFactory.createPreProcess(gwType).processDeviceStrategy(deviceIds,serviceId,paramArr);
			logger.warn("调用配置模块进行配量参数配置结果为(ret={})", new Object[] { ret});
			if (1==ret){
				logger.debug("调用后台预读模块成功");
				return "1";
			} else {
				logger.warn("调用后台预读模块失败");
				return "-4";
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
			logger.warn("Exception---"+e.getMessage());
			return "-4";
		}

	}
	public String getAccSQL(String tableName,String gw_type){
		logger.debug("arggetSQL()");
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select a.device_id,b.vendor_add,c.device_model,d.softwareversion from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,  tab_softwareup_tmp t,");
		sql.append(tableName);
		sql.append(" e where a.device_id=e.device_id and a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		sql.append(" and e.user_state in ('1','2') ");
		sql.append(" and e.username = t.data");
		if (null !=gw_type && !"".equals(gw_type)&& !"null".equals(gw_type) ) {
			sql.append(" and a.gw_type = " + gw_type );
		}
		sql.append(" order by complete_time");
		return sql.toString();

	}
	public String getDevSQL(String gw_type){
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append(" select a.device_id,b.vendor_add,c.device_model,d.softwareversion from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,  tab_softwareup_tmp t ");
		sql.append(" where a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		sql.append(" and a.device_serialnumber = t.data");
		if (null !=gw_type && !"".equals(gw_type)&& !"null".equals(gw_type) ) {
			sql.append(" and a.gw_type = " + gw_type );
		}
		sql.append(" order by complete_time");
		return sql.toString();
	}
}
