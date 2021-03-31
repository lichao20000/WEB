
package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-7-19
 * @category com.linkage.module.lims.stb.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class MacQueryDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(MacQueryDAO.class);

	public List<Map> getMacInfo(String cpe_mac, String vendor_name, String device_model,
			String supply_mode, int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select t.device_id,t.vendor_name,t.device_model,t.cpe_mac,t.supply_mode,t.package_no,t.order_id,t.buy_time,t.cpe_currentupdatetime,t.staff_id,t.device_sn from  stb_tab_devmac_init  t ");
		sql.append("where 1=1 ");
		if (!StringUtil.IsEmpty(cpe_mac))
		{
			sql.append(" and t.cpe_mac like '").append(cpe_mac).append("%' ");
		}
		if (!StringUtil.IsEmpty(vendor_name))
		{
			sql.append(" and t.vendor_name='").append(vendor_name).append("' ");
		}
		if (!StringUtil.IsEmpty(device_model))
		{
			sql.append(" and t.device_model='").append(device_model).append("' ");
		}
		if (!StringUtil.IsEmpty(supply_mode))
		{
			sql.append(" and t.supply_mode='").append(supply_mode).append("' ");
		}
		sql.append(" order by t.cpe_currentupdatetime desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage,
				num_splitPage, new RowMapper()
				{

					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("device_id", rs.getString("device_id"));
						map.put("vendor_name", rs.getString("vendor_name"));
						map.put("device_model", rs.getString("device_model"));
						map.put("cpe_mac", rs.getString("cpe_mac"));
						map.put("supply_mode", rs.getString("supply_mode"));
						map.put("package_no", rs.getString("package_no"));
						map.put("order_id", rs.getString("order_id"));
						map.put("buy_time", formatTime(StringUtil.getLongValue(rs
								.getString("buy_time"))));
						map.put("cpe_currentupdatetime", formatTime(StringUtil
								.getLongValue(rs.getString("cpe_currentupdatetime"))));
						map.put("staff_id", rs.getString("staff_id"));
						map.put("device_sn", rs.getString("device_sn"));
						return map;
					}
				});
		return list;
	}

	public int getCount(String cpe_mac, String vendor_name, String device_model,
			String supply_mode, int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from  stb_tab_devmac_init  t ");
		sql.append("where 1=1 ");
		if (!StringUtil.IsEmpty(cpe_mac))
		{
			sql.append(" and t.cpe_mac like '").append(cpe_mac).append("%' ");
		}
		if (!StringUtil.IsEmpty(vendor_name))
		{
			sql.append(" and t.vendor_name='").append(vendor_name).append("' ");
		}
		if (!StringUtil.IsEmpty(device_model))
		{
			sql.append(" and t.device_model='").append(device_model).append("' ");
		}
		if (!StringUtil.IsEmpty(supply_mode))
		{
			sql.append(" and t.supply_mode='").append(supply_mode).append("' ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		return total;
	}

	/**
	 * <pre>
	 * 判断mac地址是否已经存在
	 * 当新增mac地址是，deviceId入参传入null
	 * 当修改mac地址时，传入当前修改的设备deviceId,判断修改后的mac是否已经存在
	 *
	 * <pre>
	 * @param deviceId 设备ID，新增时传入null，修改时传入当前的设备ID
	 * @param mac 待校验的MAC地址
	 * @return 如果已经存在返回true，否则返回false
	 */
	public boolean isMacExist(String deviceId, String mac)
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.append("select count(*) from stb_tab_devmac_init where cpe_mac = ? ");
		if (!StringUtil.IsEmpty(deviceId))
		{
			pSql.append(" and device_id != '");
			pSql.append(deviceId);
			pSql.append("'");
		}
		pSql.setString(1, mac);
		int count = jt.queryForInt(pSql.getSQL());
		return count > 0;
	}

	public String insertMac(int num, String orderId, String packageNo, String vendorName,
			String supplyMode, String deviceModel, String mac, String deviceSn,
			String cityId, String staffId)
	{
		long time = System.currentTimeMillis() / 1000;
		StringBuffer sql = new StringBuffer();
		sql.append("insert into stb_tab_devmac_init(device_id,city_id,buy_time,cpe_currentupdatetime ");
		if (!StringUtil.IsEmpty(staffId))
		{
			sql.append(",staff_id ");
		}
		if (!StringUtil.IsEmpty(orderId))
		{
			sql.append(",order_id ");
		}
		if (!StringUtil.IsEmpty(packageNo))
		{
			sql.append(",package_no ");
		}
		if (!StringUtil.IsEmpty(supplyMode))
		{
			sql.append(",supply_mode ");
		}
		sql.append(",vendor_name, device_model, cpe_mac ");
		if (!StringUtil.IsEmpty(deviceSn))
		{
			sql.append(",device_sn");
		}
		sql.append(") values('").append(num).append("','").append(cityId).append("',")
				.append(time).append(",").append(time);
		if (!StringUtil.IsEmpty(staffId))
		{
			sql.append(",'").append(staffId).append("'");
		}
		if (!StringUtil.IsEmpty(orderId))
		{
			sql.append(",'").append(orderId).append("'");
		}
		if (!StringUtil.IsEmpty(packageNo))
		{
			sql.append(",'").append(packageNo).append("'");
		}
		if (!StringUtil.IsEmpty(supplyMode))
		{
			sql.append(",'").append(supplyMode).append("'");
		}
		sql.append(",'").append(vendorName).append("','").append(deviceModel)
				.append("','").append(mac).append("'");
		if (!StringUtil.IsEmpty(deviceSn))
		{
			sql.append(",'").append(deviceSn).append("'");
		}
		sql.append(")");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		jt.execute(psql.getSQL());
		return "新增成功";
	}

	public int updateMac(String orderId, String packageNo, String vendorName,
			String supplyMode, String deviceModel, String mac, String deviceSn,
			String deviceId, String cityId, String staffId)
	{
		long time = System.currentTimeMillis() / 1000;
		StringBuffer sql = new StringBuffer();
		sql.append("update stb_tab_devmac_init set ");
		sql.append(" vendor_name='").append(vendorName).append("'");
		sql.append(" ,device_model='").append(deviceModel).append("'");
		sql.append(" ,cpe_mac='").append(mac).append("'");
		sql.append(" ,city_id='").append(cityId).append("'");
		sql.append(" ,staff_id='").append(staffId).append("'");
		sql.append(" , cpe_currentupdatetime=").append(time);
		if (!StringUtil.IsEmpty(orderId))
		{
			sql.append(" ,order_id='").append(orderId).append("'");
		}
		if (!StringUtil.IsEmpty(packageNo))
		{
			sql.append(" , package_no='").append(packageNo).append("'");
		}
		if (!StringUtil.IsEmpty(supplyMode))
		{
			sql.append(" , supply_mode='").append(supplyMode).append("'");
		}
		if (!StringUtil.IsEmpty(deviceSn))
		{
			sql.append(" , device_sn='").append(deviceSn).append("'");
		}
		sql.append(" where device_id='").append(deviceId).append("'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.update(psql.getSQL());
	}

	public String getDeviceId()
	{
		logger.debug("MacQueryDAO==>getDeviceId()");
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB() == 1)
		{// oracle
			psql.append("select max(to_number(t.device_id)) device_id from stb_tab_devmac_init t");
		}
		else if(DBUtil.GetDB() == 3)
		{// mysql
			psql.append("select max(cast(t.device_id as signed)) device_id from stb_tab_devmac_init t");
		}
		else
		{
			psql.append("select max(convert(numeric, t.device_id)) device_id from stb_tab_devmac_init t");
		}
		Map map = DataSetBean.getRecord(psql.getSQL());
		return StringUtil.getStringValue(map.get("device_id"));
	}

	public int checkDeviceMacAndSn(String deviceMac, String device_sn)
	{
		logger.debug("ImportMacInitDAO==>checkDeviceMac");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from stb_tab_devmac_init where 1=1 ");
		if (!StringUtil.IsEmpty(deviceMac))
		{
			sql.append(" and cpe_mac='").append(deviceMac).append("'");
		}
		if (!StringUtil.IsEmpty(device_sn))
		{
			sql.append(" and device_sn='").append(device_sn).append("'");
		}
		logger.error("sql:" + sql);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForInt(psql.getSQL());
	}

	private String formatTime(long time)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(time * 1000);
	}
	@SuppressWarnings("unchecked")
	public List getVendorList(){
		PrepareSQL psql=new PrepareSQL("select a.vendor_id,a.vendor_name from tab_vendor4jx a");
		return jt.queryForList(psql.getSQL());
	}
	@SuppressWarnings("unchecked")
	public List getDeviceModelList(String vendorId){
		PrepareSQL psql=new PrepareSQL("select a.model_id,a.model_name from table_vendor_model a where 1=1 ");
		if(null != vendorId && !"".equals(vendorId) && !"-1".equals(vendorId)){
			psql.append(" and a.vendor_id=");
			psql.append(vendorId);
		}

		return jt.queryForList(psql.getSQL());
	}
	/**
	 * 校验mac
	 * @param vendorId
	 * @param mac
	 * @return boolean
	 */
	public Map validateMAC(String vendorId,String mac){
		PrepareSQL psql=new PrepareSQL("select mac from tab_vendor4jx where vendor_id = ?");
		psql.setInt(1, Integer.parseInt(vendorId));
		//获得mac字符串
		Map map = DataSetBean.getRecord(psql.getSQL());

		return map;
	}
}
