
package com.linkage.module.gtms.stb.resource.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.module.gtms.stb.resource.dto.ZeroconfManualDTO;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * itv手动下发配置dao实现类
 *
 * @author zhumiao
 * @version 1.0
 * @since 2011-12-5 下午03:21:11
 * @category com.linkage.module.lims.itv.zeroconf.dao<br>
 * @copyright 南京联创科技 网管科技部
 */
public class ZeroconfManualDaoImp extends SuperDAO implements ZeroconfManualDao
{

	private static Logger log = Logger.getLogger(ZeroconfManualDaoImp.class);

	@Override
	public List<Map> getUserAccount(ZeroconfManualDTO dto)
	{// TODO wait (more table related)
		String sql = "select a.oui, a.device_serialnumber, a.device_id, a.customer_id, "
		+ "b.vendor_add,c.device_model,d.softwareversion,e.cust_account,e.serv_account,e.cust_name,e.addressing_type,f.last_time "
		+ "from "
		+ "stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_tab_customer e,stb_gw_devicestatus f "
		+ "where a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id =c.device_model_id "
		+ "and a.devicetype_id=d.devicetype_id and a.customer_id=e.customer_id and a.device_id=f.device_id ";
		if ("1".equals(dto.getQueryType()))
		{
			sql += " and a.zero_account in ( select zero_account from stb_tab_gw_device where serv_account = '"
					+ dto.getQueryStr() + "'";
			sql += ") order by a.city_id";
		}
		if ("2".equals(dto.getQueryType()))
		{
			sql += " and a.device_serialnumber = '" + dto.getQueryStr() + "'";
			sql += " order by a.city_id";
		}
		log.info("查询机顶盒列表sql=" + sql);
		return jt.queryForList(sql);
	}

	@Override
	public void manualConfiguration(ZeroconfManualDTO dto)
	{
		Date date = new Date();
		// String sql =
		// "update stb_tab_gw_device set status = 0 where zero_account = '"+dto.getServ_account()+"' and oui = '"+dto.getOui()+"' and device_serialnumber = '"+dto.getDevice_serialnumber()+"'";
		// log.info("手动下发配置sql="+sql);
		// jt.execute(sql);
		// String sql2 =
		// "update tab_zero_conf_report set work_status = 3 where oui = '"+dto.getOui()+"' and sn = '"+dto.getDevice_serialnumber()+"'";
		// log.info("手动下发到零配置工单表sql2="+sql2);
		// jt.execute(sql2);
		// String sql3 =
		// "insert into tab_zero_conf_report(work_id,serv_account_new,oper_type,conf_down_time,work_status,is_from_bss,city_id,bss_deal_time) "
		// +
		// "values('"+dto.getWork_id()+"','"+dto.getServ_account()+"',3,"+dto.getConf_down_time()+",2,0,'"+dto.getCity_id()+"',"+new
		// DateTimeUtil(date).getLongTime()+")";
		String sql3 = "insert into tab_zero_conf_report(work_id,prod_id,serv_account_new,oper_type,work_status,is_from_bss,city_id,bss_deal_time,addressing_type) "
				+ "values('"
				+ dto.getWork_id()
				+ "','','"
				+ dto.getServ_account()
				+ "',0,2,0,'"
				+ dto.getCity_id()
				+ "',"
				+ new DateTimeUtil(date).getLongTime()
				+ ",'"
				+ dto.getAddressing_type()
				+ "')";
		log.info("手动下发到零配置工单表sql3=" + sql3);
		jt.execute(sql3);
	}
}
