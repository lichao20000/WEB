
package com.linkage.module.gtms.blocTest.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author Administrator(工号) Tel:��
 * @version 1.0
 * @since 2012-6-14 下午09:46:50
 * @category com.linkage.module.gtms.blocTest.dao<br>
 * @copyright 南京联创科技 网管科技部
 */
public class SellSupportCustomizeDaoImp extends SuperDAO implements
		SellSupportCustomizeDao
{

	private static Logger logger = LoggerFactory
			.getLogger(SellSupportCustomizeDaoImp.class);
	public List queryReport(long custManagerId) {
		logger.debug("queryReport()");
		List<Map<String,String>> retMaps = new ArrayList<Map<String,String>>();
		DateTimeUtil dateTimeUtil = new DateTimeUtil(new Date().getTime() * 1000);
		PrepareSQL psql = new PrepareSQL("select customer_id, flow_max, flow_min, time_max, time_min from tab_sell_support_customize a,tab_cust_manager b where a.cust_manager_id=b.cust_manager_id and b.cust_manager_id=?");
		psql.setLong(1,custManagerId);
		List<HashMap<String,String>> maps = DBOperation.getRecords(psql.getSQL());
		for(Map<String,String> map : maps)
		{
			String customerId = map.get("customer_id");
			String tabName = "flux_data_" + dateTimeUtil.getYear() + "_"
			+ dateTimeUtil.getMonth() + "_" + dateTimeUtil.getDay();
			//查询流量----------------
			psql = new PrepareSQL();
			psql.append("select d.ifinoctets from tab_customerinfo a,tab_egwcustomer b,tab_gw_device c,");
			psql.append(tabName);
			psql.append(" d where a.customer_id=b.customer_id and b.device_id=c.device_id and c.device_id=d.device_id and a.customer_id=?");
			psql.setString(1,customerId);
			Map<String,String> fluxMap = DBOperation.getMap(psql.getSQL());
			long fluxMax = StringUtil.getLongValue(map, "flow_max");
			long fluxMin = StringUtil.getLongValue(map, "flow_min");
			long flux = 0;
			if(fluxMap != null && fluxMap.size()>0)
			{
				flux = StringUtil.getLongValue(fluxMap, "ifinoctets");
			}
			if(flux > fluxMin && flux < fluxMax)
			{
				map.put("flux", StringUtil.getStringValue(flux));
			}

			//查询时长--------------------
			psql = new PrepareSQL();
			tabName = "pm_raw_" + dateTimeUtil.getYear() + "_"+ dateTimeUtil.getMonth();
			psql.append("select e.value from tab_customerinfo a,tab_egwcustomer b,tab_gw_device c,pm_map_instance d,");
			psql.append(tabName);
			psql.append(" e where a.customer_id=b.customer_id and b.device_id=c.device_id and c.device_id=d.device_id and d.id=e.id and d.expressionid=312 and  and a.customer_id=?");
			psql.setString(1,customerId);
			Map<String,String> pmMap = DBOperation.getMap(psql.getSQL());
			long pmMax = StringUtil.getLongValue(map, "time_max");
			long pmMin = StringUtil.getLongValue(map, "time_min");
			long pm = 0;
			if(fluxMap != null && pmMap.size()>0)
			{
				pm = StringUtil.getLongValue(pmMap, "value");
			}
			if(pm > pmMin && pm < pmMax)
			{
				map.put("pm", StringUtil.getStringValue(pm));
			}
			retMaps.add(map);
		}
		return retMaps;
	}
	public static void main(String[] args)
	{
		DateTimeUtil dateTimeUtil = new DateTimeUtil(new Date().getTime());
		System.out.println( "flux_data_" + dateTimeUtil.getYear() + "_"
			+ dateTimeUtil.getMonth() + "_" + dateTimeUtil.getDay());
	}
	/**
	 * 查询客户信息列表
	 *
	 * @return
	 */
	public List queryCustomer() {

		logger.debug("queryCustomer()");
		String sql = "select customer_id, customer_account, customer_name, linkman, linkphone, city_id from tab_customerinfo";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}
	/**
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getMailTmpt(String custManagerId)
	{
		logger.debug("getMailTmpt()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select mail_topic, mail_content from tab_cust_manager where cust_manager_id="+custManagerId);
		return jt.queryForMap(pSQL.getSQL());
	}
	/**
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> getAllCustManager()
	{
		logger.debug("getAllCustManager()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select cust_manager_id,cust_manager_name from tab_cust_manager");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<Map<String, String>> tmpList = jt.queryForList(pSQL.getSQL());
		for (int i = 0; i < tmpList.size(); i++)
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put("cust_manager_id",
					StringUtil.getStringValue(tmpList.get(i).get("cust_manager_id")));
			map.put("cust_manager_name",
					StringUtil.getStringValue(tmpList.get(i).get("cust_manager_name")));
			logger.warn("{}",new Object[]{map});
			list.add(map);
		}
		return list;
	}

	/**
	 *
	 */
	public void addSellSupportCustomize(String custManagerId, String flowMax,
			String flowMin, String timeMax, String timeMin, String customerId)
	{
		logger.debug("addSellSupportCustomize()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("insert into tab_sell_support_customize(id,flow_min,flow_max,time_min,time_max,cust_manager_id,customer_id) values (?,?,?,?,?,?,?)");
		pSQL.setLong(1, Math.round(Math.random() * 1000000000000L));
		pSQL.setLong(2,StringUtil.getLongValue(flowMin));
		pSQL.setLong(3,StringUtil.getLongValue(flowMax));
		pSQL.setLong(4,StringUtil.getLongValue(timeMin));
		pSQL.setLong(5,StringUtil.getLongValue(timeMax));
		pSQL.setLong(6,StringUtil.getLongValue(custManagerId));
		pSQL.setString(7,customerId);
		jt.execute(pSQL.getSQL());
	}
}
