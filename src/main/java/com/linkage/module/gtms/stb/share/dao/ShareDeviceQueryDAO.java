package com.linkage.module.gtms.stb.share.dao;

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
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.litms.common.util.JdbcTemplateExtend;
import com.linkage.system.utils.StringUtils;

/**
 * @author zxj E-mail锛歲ixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.dao
 *
 */
@SuppressWarnings("rawtypes")
public class ShareDeviceQueryDAO
{
	private static Logger logger = LoggerFactory.getLogger(ShareDeviceQueryDAO.class);

	private JdbcTemplateExtend jt;

	/**
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}

	/**
	 * 模拟匹配提示
	 */
	public List getDeviceSn(String cityId,String searchTxt,String gwShare_queryField)
	{
		PrepareSQL pSQL = new PrepareSQL();

		if(null!=searchTxt)
		{
			if(DBUtil.GetDB() == Global.DB_MYSQL)
			{// mysql
				pSQL.setSQL("select ");
			}else{
				pSQL.setSQL("select top 5 ");
			}

			if("username".equals(gwShare_queryField))
			{
				pSQL.append(" b.device_serialnumber, a.cust_account ");
				pSQL.append("from stb_tab_customer a,stb_tab_gw_device b ");
				pSQL.append("where a.customer_id=b.customer_id  and a.cust_account like '%"+searchTxt+"%' ");
				pSQL.append(pinSql(cityId,null,null,null,null,null,null,false));
				pSQL.append(" order by b.complete_time");
			}
			else
			{
				pSQL.append(" device_serialnumber, loopback_ip as cust_account ");
				pSQL.append("from stb_tab_gw_device where device_status =1 ");

				if("deviceSn".equals(gwShare_queryField)){
					pSQL.append(" and device_serialnumber like '%"+searchTxt+"%' ");
				}else if("deviceIp".equals(gwShare_queryField)){
					pSQL.append(" and loopback_ip like '%"+searchTxt+"%' ");
				}else{
					pSQL.append(" and ( device_serialnumber like '%"+searchTxt);
					pSQL.append("%' or loopback_ip like '%"+searchTxt+"%') ");
				}
				pSQL.append(pinSql(cityId,null,null,null,null,null,null,false));
				pSQL.append(" order by complete_time");
			}
			if(DBUtil.GetDB() == Global.DB_MYSQL)
			{// mysql
				pSQL.append(" limit 5");
			}
		}
		return jt.queryForList(pSQL.getSQL());
	}

	/**
	 * 查询设备列表(根据设备序列号和设备IP用or以及like来匹配)
	 */
	public List queryDeviceByFieldOr(long areaId,String queryParam,String cityId)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceByFieldOr(areaId:{},queryParam:{})",areaId,queryParam);
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
				"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
				"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
				"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
				"b.vendor_add,c.device_model,d.softwareversion ");
		pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d ");
		pSQL.append("where a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id ");
		pSQL.append("and a.devicetype_id=d.devicetype_id ");

		if(!StringUtil.IsEmpty(queryParam)){
			pSQL.append(" and (a.device_serialnumber like '%"+queryParam);
			pSQL.append("%' or a.loopback_ip like '%"+queryParam+"%') ");
		}

		pSQL.append(pinSql(cityId,null,null,null,null,null,null,false));
		pSQL.append(" order by a.complete_time");

		return jt.query(pSQL.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 查询设备列表(根据设备序列号和设备IP用or以及like来匹配)
	 */
	public List queryDeviceByFieldOr(int curPage_splitPage,int num_splitPage,long areaId,
			String queryParam,String cityId,int gwShare_OrderByUpdateDate)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceByFieldOr(areaId:{},queryParam:{})",areaId,queryParam);
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
				"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
				"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
				"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
				"b.vendor_add,c.device_model,d.softwareversion,e.last_time ");
		pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e ");
		pSQL.append("where a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id ");
		pSQL.append("and a.devicetype_id=d.devicetype_id  and a.device_id = e.device_id ");

		if(!StringUtil.IsEmpty(queryParam)){
			pSQL.append(" and (a.device_serialnumber like '%"+queryParam);
			pSQL.append("%' or a.loopback_ip like '%"+queryParam+"%') ");
		}
		pSQL.append(pinSql(cityId,null,null,null,null,null,null,false));
		pSQL.append(pinOrder(gwShare_OrderByUpdateDate));

		return jt.querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage,
				num_splitPage,new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 查询设备列表(条数)(根据设备序列号和设备IP用or以及like来匹配)
	 */
	public int queryDeviceByFieldOrCount(long areaId,String queryParam,String cityId)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceByFieldOrCount(areaId:{},queryParam:{})",areaId,queryParam);
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select count(*) from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d ");
		pSQL.append("where a.device_status =1 and a.vendor_id=b.vendor_id ");
		pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if(!StringUtil.IsEmpty(queryParam)){
			pSQL.append(" and (a.device_serialnumber like '%"+queryParam);
			pSQL.append("%' or a.loopback_ip like '%"+queryParam+"%') ");
		}
		pSQL.append(pinSql(cityId,null,null,null,null,null,null,false));

		return jt.queryForInt(pSQL.getSQL());
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 */
	public List queryDevice(long areaId,String cityId,String vendorId,String deviceModelId,
			String devicetypeId,String bindType,String deviceSerialnumber,String deviceIp)
	{
		logger.debug("GwDeviceQueryDAO=>queryDevice()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
				"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
				"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
				"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
				"b.vendor_add,c.device_model,d.softwareversion ");// TODO wait (more table related)
		pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d ");
		pSQL.append("where a.device_status =1 and  a.vendor_id=b.vendor_id ");
		pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");

		if(Global.XJDX.equals(Global.instAreaShortName)
				&& !StringUtil.IsEmpty(deviceSerialnumber)
				&& deviceSerialnumber.contains(":"))
		{
			//新疆的高级查询
			pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
					"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
					"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
					"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
					"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
					"b.vendor_add,c.device_model,d.softwareversion ");// TODO wait (more table related)
			pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d ");

			String temp[] = deviceSerialnumber.split(":");
			deviceSerialnumber = temp[0];
			String hardware = temp[1];
			String belong = temp[2];
			if(!StringUtil.IsEmpty(belong) && !"-1".equals(belong)){
				pSQL.append(",stb_tab_customer e");
				pSQL.append(" where a.customer_id=e.customer_id");
				pSQL.append(" and e.platform in" + belong +" and");
			}
			if(!pSQL.getSQL().contains("where")){
				pSQL.append("where");
			}
			pSQL.append(" a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
			if(!StringUtil.IsEmpty(hardware) && !"-1".equals(hardware)){
				pSQL.append(" and d.hardwareversion='" + hardware + "' ");
			}
	    }
		pSQL.append(pinSql(cityId,vendorId,deviceModelId,devicetypeId,bindType,deviceSerialnumber,deviceIp,true));
		pSQL.append(" order by a.complete_time");

		return jt.query(pSQL.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 */
	public List queryDeviceIpSix(long areaId,String cityId,String vendorId,String deviceModelId,
			String devicetypeId,String bindType,String deviceSerialnumber,String deviceIp,String deviceIpSix)
	{
		logger.debug("GwDeviceQueryDAO=>queryDevice()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
				"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
				"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
				"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
				"b.vendor_add,c.device_model,d.softwareversion ");// TODO wait (more table related)
		pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d ");
		pSQL.append("where a.device_status =1 and  a.vendor_id=b.vendor_id ");
		pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");

		if(Global.XJDX.equals(Global.instAreaShortName)
				&& !StringUtil.IsEmpty(deviceSerialnumber)
				&& deviceSerialnumber.contains(":"))
		{
			//新疆的高级查询
			pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
					"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
					"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
					"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
					"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
					"b.vendor_add,c.device_model,d.softwareversion ");// TODO wait (more table related)
			pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d ");

			String temp[] = deviceSerialnumber.split(":");
			deviceSerialnumber = temp[0];
			String hardware = temp[1];
			String belong = temp[2];
			if(!StringUtil.IsEmpty(belong) && !"-1".equals(belong)){
				pSQL.append(",stb_tab_customer e");
				pSQL.append(" where a.customer_id=e.customer_id");
				pSQL.append(" and e.platform in" + belong +" and");
			}
			if(!pSQL.getSQL().contains("where")){
				pSQL.append("where");
			}
			pSQL.append(" a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
			if(!StringUtil.IsEmpty(hardware) && !"-1".equals(hardware)){
				pSQL.append(" and d.hardwareversion='" + hardware + "' ");
			}
	    }
		pSQL.append(pinSqlIpSix(cityId,vendorId,deviceModelId,devicetypeId,bindType,deviceSerialnumber,deviceIp,true,deviceIpSix));
		pSQL.append(" order by a.complete_time");

		return jt.query(pSQL.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 */
	public List queryDevice(long areaId,String cityId,String custaccount)
	{
		logger.debug("GwDeviceQueryDAO=>queryDevice()");
		PrepareSQL pSQL = new PrepareSQL();
		String sqlForXJ = "";
		if (Global.XJDX.equals(Global.instAreaShortName)) {
			sqlForXJ = ",stb_tab_customer f ";
		}// TODO wait (more table related)
		pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
				"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
				"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
				"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
				"b.vendor_add,c.device_model,d.softwareversion from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d" + sqlForXJ);
		pSQL.append(" where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (Global.XJDX.equals(Global.instAreaShortName)) {
			pSQL.append(" and a.customer_id = f.customer_id ");
		}

		if(!StringUtil.IsEmpty(custaccount)){
			if (!Global.XJDX.equals(Global.instAreaShortName)) {
				pSQL.appendAndString("a.serv_account", PrepareSQL.EQUEAL, custaccount);
			} else {
				pSQL.appendAndString("f.serv_account", PrepareSQL.EQUEAL, custaccount);
			}
		}
		pSQL.append(pinSql(cityId,null,null,null,null,null,null,false));
		pSQL.append(" order by a.complete_time");

		return jt.query(pSQL.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 */
	public List queryDevice(int curPage_splitPage,int num_splitPage,long areaId,
			String cityId,String vendorId,String deviceModelId,String devicetypeId,
			String bindType,String deviceSerialnumber,String deviceIp,int gwShare_OrderByUpdateDate)
	{
		logger.debug("GwDeviceQueryDAO=>queryDevice()");
		PrepareSQL pSQL = new PrepareSQL();
		if(Global.HNLT.equals(Global.instAreaShortName))
		{
			pSQL.append("select x.*,e.apk_version_name from (");
			pSQL.append("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
					"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
					"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
					"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
					"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
					"b.vendor_add,c.device_model,d.softwareversion,d.epg_version ");// TODO wait (more table related)
			pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d ");
			pSQL.append("where a.device_status=1 and a.vendor_id=b.vendor_id ");
			pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");

			gwShare_OrderByUpdateDate=0;
		}
		else if(Global.XJDX.equals(Global.instAreaShortName)
				&& !StringUtil.IsEmpty(deviceSerialnumber)
				&& deviceSerialnumber.contains(":"))
		{
			pSQL.append("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
					"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
					"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
					"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
					"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
					"b.vendor_add,c.device_model,d.softwareversion,e.last_time ");// TODO wait (more table related)
			pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e ");
			//新疆高级查询
			String temp[] = deviceSerialnumber.split(":");
			deviceSerialnumber = temp[0];
			String hardware = temp[1];
			String belong = temp[2];
			if(!StringUtil.IsEmpty(belong) && !"-1".equals(belong)){
				pSQL.append(",stb_tab_customer f");
				pSQL.append(" where a.customer_id=f.customer_id");
				pSQL.append(" and f.platform in" + belong + " and");
			}
			if(!pSQL.getSQL().contains("where")){
				pSQL.append(" where");
			}
			pSQL.append(" a.device_status=1 and a.vendor_id=b.vendor_id ");
			pSQL.append(" and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id and a.device_id=e.device_id");
			if(!StringUtil.IsEmpty(hardware) && !"-1".equals(hardware)){
				pSQL.append(" and d.hardwareversion='" + hardware + "' ");
			}
		}
		else
		{
			pSQL.append("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
					"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
					"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
					"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
					"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
					"b.vendor_add,c.device_model,d.softwareversion,e.last_time ");// TODO wait (more table related)
			pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e ");
			pSQL.append("where a.device_status=1 and a.vendor_id=b.vendor_id ");
			pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id and a.device_id=e.device_id");
		}

		pSQL.append(pinSql(cityId,vendorId,deviceModelId,devicetypeId,bindType,deviceSerialnumber,deviceIp,true));
		pSQL.append(pinOrder(gwShare_OrderByUpdateDate));

		if(Global.HNLT.equals(Global.instAreaShortName)){
			pSQL.append(") x left join stb_dev_supplement e on x.device_id=e.device_id ");
		}

		return jt.querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage,num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 */
	public List queryDeviceIpSix(int curPage_splitPage,int num_splitPage,long areaId,
			String cityId,String vendorId,String deviceModelId,String devicetypeId,
			String bindType,String deviceSerialnumber,String deviceIp,int gwShare_OrderByUpdateDate,String deviceIpSix)
	{
		logger.debug("GwDeviceQueryDAO=>queryDevice()");
		PrepareSQL pSQL = new PrepareSQL();
		if(Global.HNLT.equals(Global.instAreaShortName))
		{
			pSQL.append("select x.*,e.apk_version_name from (");
			pSQL.append("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
					"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
					"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
					"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
					"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
					"b.vendor_add,c.device_model,d.softwareversion,d.epg_version ");// TODO wait (more table related)
			pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d ");
			pSQL.append("where a.device_status=1 and a.vendor_id=b.vendor_id ");
			pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");

			gwShare_OrderByUpdateDate=0;
		}
		else if(Global.XJDX.equals(Global.instAreaShortName)
				&& !StringUtil.IsEmpty(deviceSerialnumber)
				&& deviceSerialnumber.contains(":"))
		{
			pSQL.append("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
					"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
					"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
					"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
					"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
					"b.vendor_add,c.device_model,d.softwareversion,e.last_time ");// TODO wait (more table related)
			pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e ");
			//新疆高级查询
			String temp[] = deviceSerialnumber.split(":");
			deviceSerialnumber = temp[0];
			String hardware = temp[1];
			String belong = temp[2];
			if(!StringUtil.IsEmpty(belong) && !"-1".equals(belong)){
				pSQL.append(",stb_tab_customer f");
				pSQL.append(" where a.customer_id=f.customer_id");
				pSQL.append(" and f.platform in" + belong + " and");
			}
			if(!pSQL.getSQL().contains("where")){
				pSQL.append(" where");
			}
			pSQL.append(" a.device_status=1 and a.vendor_id=b.vendor_id ");
			pSQL.append(" and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id and a.device_id=e.device_id");
			if(!StringUtil.IsEmpty(hardware) && !"-1".equals(hardware)){
				pSQL.append(" and d.hardwareversion='" + hardware + "' ");
			}
		}
		else
		{
			pSQL.append("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
					"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
					"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
					"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
					"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
					"b.vendor_add,c.device_model,d.softwareversion,e.last_time ");// TODO wait (more table related)
			pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e ");
			pSQL.append("where a.device_status=1 and a.vendor_id=b.vendor_id ");
			pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id and a.device_id=e.device_id");
		}

		pSQL.append(pinSqlIpSix(cityId,vendorId,deviceModelId,devicetypeId,bindType,deviceSerialnumber,deviceIp,true,deviceIpSix));
		pSQL.append(pinOrder(gwShare_OrderByUpdateDate));

		if(Global.HNLT.equals(Global.instAreaShortName)){
			pSQL.append(") x left join stb_dev_supplement e on x.device_id=e.device_id ");
		}

		return jt.querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage,num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 */
	public List queryDeviceByStb(int curPage_splitPage,int num_splitPage,long areaId,
			String cityId,String vendorId,String deviceModelId,String devicetypeId,
			String bindType,String deviceSerialnumber,String deviceIp,
			int gwShare_OrderByUpdateDate,String gwShare_platform)
	{
		logger.debug("GwDeviceQueryDAO=>queryDevice()");
		PrepareSQL pSQL = new PrepareSQL();
		if(Global.HNLT.equals(Global.instAreaShortName))
		{
			pSQL.append("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
					"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
					"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
					"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
					"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
					"b.vendor_add,c.device_model,d.softwareversion ");// TODO wait (more table related)
			pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d ");
			pSQL.append("where a.device_status=1 and a.vendor_id=b.vendor_id ");
			pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");

			gwShare_OrderByUpdateDate=0;
		}
		else
		{
			if(!StringUtil.IsEmpty(gwShare_platform)
					&& !"null".equals(gwShare_platform)
					&& !"-1".equals(gwShare_platform))
			{
				pSQL.append("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
						"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
						"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
						"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
						"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
						"b.vendor_add,c.device_model,d.softwareversion,e.last_time ");// TODO wait (more table related)
				pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,");
				pSQL.append("stb_tab_devicetype_info d,stb_gw_devicestatus e,stb_tab_customer f ");
				pSQL.append("where a.device_status=1 and a.vendor_id=b.vendor_id ");
				pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
				pSQL.append("and a.device_id=e.device_id and a.customer_id=f.customer_id ");
			}
			else
			{
				pSQL.append("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
						"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
						"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
						"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
						"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
						"b.vendor_add,c.device_model,d.softwareversion,e.last_time ");// TODO wait (more table related)
				pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e ");
				pSQL.append("where a.device_status=1 and a.vendor_id=b.vendor_id ");
				pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id and a.device_id=e.device_id ");
			}
		}

		if(null!=gwShare_platform && !"null".equals(gwShare_platform) && !"".equals(gwShare_platform) && !"-1".equals(gwShare_platform)){
			pSQL.appendAndString("f.platform", PrepareSQL.EQUEAL, gwShare_platform);
		}

		pSQL.append(pinSql(cityId,vendorId,deviceModelId,devicetypeId,bindType,deviceSerialnumber,deviceIp,true));
		pSQL.append(pinOrder(gwShare_OrderByUpdateDate));

		return jt.querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage,num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 高级查询拼装SQL1
	 */
	public String queryDeviceSQLLikeStatusByStb(int curPage_splitPage,int num_splitPage,long areaId,
			String cityId,String vendorId,String deviceModelId,String devicetypeId,
			String bindType,String deviceSerialnumber,String deviceIp,String onlineStatus,
			int gwShare_OrderByUpdateDate,String gwShare_platform)
	{
		PrepareSQL pSQL = new PrepareSQL();
		if(!StringUtil.IsEmpty(gwShare_platform)
				&& !"null".equals(gwShare_platform)
				&& !"-1".equals(gwShare_platform))
		{// TODO wait (more table related)
			pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
					"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
					"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
					"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
					"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
					"b.vendor_add,c.device_model,d.softwareversion,e.last_time from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e ,stb_tab_customer f where a.device_status =1 and  a.device_id=e.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id and a.customer_id = f.customer_id  ");
		}
		else {// TODO wait (more table related)
			pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
					"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
					"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
					"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
					"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
					"b.vendor_add,c.device_model,d.softwareversion,e.last_time from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e where a.device_status =1 and  a.device_id=e.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id  ");
		}

		if(!StringUtil.IsEmpty(onlineStatus) && !"-1".equals(onlineStatus)){
			pSQL.appendAndNumber("e.online_status",PrepareSQL.EQUEAL, onlineStatus);
		}

		if(!StringUtil.IsEmpty(gwShare_platform)
				&& !"null".equals(gwShare_platform)
				&& !"-1".equals(gwShare_platform)){
			pSQL.appendAndString("f.platform", PrepareSQL.EQUEAL, gwShare_platform);
		}

		pSQL.append(pinSql(cityId,vendorId,deviceModelId,devicetypeId,bindType,deviceSerialnumber,deviceIp,false));
		pSQL.append(pinOrder(gwShare_OrderByUpdateDate));

		return pSQL.getSQL();
	}

	/**
	 * 高级查询拼装SQL2
	 *
	 */
	public String queryDeviceSQLByStb(int curPage_splitPage,int num_splitPage,long areaId,
			String cityId,String vendorId,String deviceModelId,String devicetypeId,
			String bindType,String deviceSerialnumber,String deviceIp,
			int gwShare_OrderByUpdateDate,String gwShare_platform)
	{
		PrepareSQL pSQL = new PrepareSQL();
		if(Global.HNLT.equals(Global.instAreaShortName))
		{
			pSQL.append("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
					"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
					"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
					"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
					"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
					"b.vendor_add,c.device_model,d.softwareversion ");// TODO wait (more table related)
			pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d ");
			pSQL.append("where a.device_status=1 and a.vendor_id=b.vendor_id ");
			pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");

			gwShare_OrderByUpdateDate=0;
		}
		else
		{
			if(!StringUtil.IsEmpty(gwShare_platform) && !"null".equals(gwShare_platform)
					&& !"-1".equals(gwShare_platform))
			{
				pSQL.append("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
						"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
						"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
						"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
						"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
						"b.vendor_add,c.device_model,d.softwareversion,e.last_time ");// TODO wait (more table related)
				pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e,stb_tab_customer f ");
				pSQL.append("where a.device_status=1 and a.vendor_id=b.vendor_id ");
				pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id and a.device_id=e.device_id and a.customer_id = f.customer_id");
			}
			else
			{
				pSQL.append("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
						"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
						"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
						"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
						"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
						"b.vendor_add,c.device_model,d.softwareversion,e.last_time ");// TODO wait (more table related)
				pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e ");
				pSQL.append("where a.device_status=1 and a.vendor_id=b.vendor_id ");
				pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id and a.device_id=e.device_id ");
			}

		}

		if(!StringUtil.IsEmpty(gwShare_platform)
				&& !"null".equals(gwShare_platform)
				&& !"-1".equals(gwShare_platform)){
			pSQL.appendAndString("f.platform", PrepareSQL.EQUEAL, gwShare_platform);
		}

		pSQL.append(pinSql(cityId,vendorId,deviceModelId,devicetypeId,bindType,deviceSerialnumber,deviceIp,false));
		pSQL.append(pinOrder(gwShare_OrderByUpdateDate));

		return pSQL.getSQL();
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 */
	public List queryDevice(int curPage_splitPage,int num_splitPage,long areaId,
			String cityId,String custaccount,int gwShare_OrderByUpdateDate)
	{
		logger.debug("GwDeviceQueryDAO=>queryDevice()");
		PrepareSQL pSQL = new PrepareSQL();
		String sqlForXJ = "";
		if (Global.XJDX.equals(Global.instAreaShortName)) {
			sqlForXJ = ",stb_tab_customer f ";
		}

		if (Global.HNLT.equals(Global.instAreaShortName))
		{
			pSQL.append("select x.*,e.apk_version_name from (");
			pSQL.append("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
					"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
					"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
					"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
					"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
					"b.vendor_add,c.device_model,d.softwareversion,d.epg_version ");// TODO wait (more table related)
			pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_tab_customer e ");
			pSQL.append("where a.device_status=1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id ");
			pSQL.append("and a.devicetype_id=d.devicetype_id and a.customer_id=e.customer_id ");
			gwShare_OrderByUpdateDate=0;
		}
		else
		{// TODO wait (more table related)
			pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
					"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
					"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
					"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
					"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
					"b.vendor_add,c.device_model,d.softwareversion,e.last_time from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e"+ sqlForXJ);
			pSQL.append(" where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id  and a.device_id = e.device_id");
		}

		if (Global.XJDX.equals(Global.instAreaShortName)) {
			pSQL.append(" and a.customer_id = f.customer_id ");
		}

		if(!StringUtil.IsEmpty(custaccount)){
			if (Global.XJDX.equals(Global.instAreaShortName)) {
				pSQL.appendAndString("f.serv_account", PrepareSQL.EQUEAL, custaccount);
			}else if(Global.HNLT.equals(Global.instAreaShortName)){
				pSQL.appendAndString("e.serv_account", PrepareSQL.EQUEAL, custaccount);
			}else {
				pSQL.appendAndString("a.serv_account", PrepareSQL.EQUEAL, custaccount);
			}
		}

		pSQL.append(pinSql(cityId,null,null,null,null,null,null,false));
		pSQL.append(pinOrder(gwShare_OrderByUpdateDate));

		if (Global.HNLT.equals(Global.instAreaShortName)) {
			pSQL.append(") x left join stb_dev_supplement e on x.device_id=e.device_id ");
		}

		return jt.querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage,num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 查询设备列表(条数)(根据设备序列号和设备IP用or以及like来匹配)
	 */
	public int queryDeviceCount(long areaId,String cityId,String vendorId,
			String deviceModelId,String devicetypeId,String bindType,
			String deviceSerialnumber,String deviceIp)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceCount()");
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select count(*) from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d ");
		pSQL.append("where a.device_status=1 and a.vendor_id=b.vendor_id ");
		pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");

		if(Global.XJDX.equals(Global.instAreaShortName)
				&& !StringUtil.IsEmpty(deviceSerialnumber)
				&& deviceSerialnumber.contains(":"))
		{
			String temp[] = deviceSerialnumber.split(":");
			deviceSerialnumber = temp[0];
			String hardware = temp[1];
			String belong = temp[2];
			String sql = " select count(*) from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d ";
			if(!StringUtil.IsEmpty(belong) && !"-1".equals(belong)){
				sql+=",stb_tab_customer e where a.customer_id=e.customer_id and e.platform in" + belong + " and";
			}
			if(!sql.contains("where")){
				sql = sql + " where";
			}
			sql += " a.device_status=1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id";
			if(!StringUtil.IsEmpty(hardware) && !"-1".equals(hardware)){
				sql = sql + " and d.hardwareversion='" + hardware + "' ";
			}
			pSQL.setSQL(sql);
		}

		pSQL.append(pinSql(cityId,vendorId,deviceModelId,devicetypeId,bindType,deviceSerialnumber,deviceIp,false));

		return jt.queryForInt(pSQL.getSQL());
	}

	/**
	 * 查询设备列表(条数)(根据设备序列号和设备IP用or以及like来匹配 增加ipv6)
	 */
	public int queryDeviceIpSixCount(long areaId,String cityId,String vendorId,
			String deviceModelId,String devicetypeId,String bindType,
			String deviceSerialnumber,String deviceIp,String deviceIpSix)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceCount()");
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select count(*) from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d ");
		pSQL.append("where a.device_status=1 and a.vendor_id=b.vendor_id ");
		pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");

		if(Global.XJDX.equals(Global.instAreaShortName)
				&& !StringUtil.IsEmpty(deviceSerialnumber)
				&& deviceSerialnumber.contains(":"))
		{
			String temp[] = deviceSerialnumber.split(":");
			deviceSerialnumber = temp[0];
			String hardware = temp[1];
			String belong = temp[2];
			// TODO wait (more table related)
			String sql = " select count(*) from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d ";
			if(!StringUtil.IsEmpty(belong) && !"-1".equals(belong)){
				sql+=",stb_tab_customer e where a.customer_id=e.customer_id and e.platform in" + belong + " and";
			}
			if(!sql.contains("where")){
				sql = sql + " where";
			}
			sql += " a.device_status=1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id";
			if(!StringUtil.IsEmpty(hardware) && !"-1".equals(hardware)){
				sql = sql + " and d.hardwareversion='" + hardware + "' ";
			}
			pSQL.setSQL(sql);
		}

		pSQL.append(pinSqlIpSix(cityId,vendorId,deviceModelId,devicetypeId,bindType,deviceSerialnumber,deviceIp,false,deviceIpSix));

		return jt.queryForInt(pSQL.getSQL());
	}

	/**
	 * 查询设备列表(条数)(根据设备序列号和设备IP用or以及like来匹配)
	 */
	public int queryDeviceCountByStb(long areaId,String cityId,String vendorId,
			String deviceModelId,String devicetypeId,String bindType,
			String deviceSerialnumber,String deviceIp,String gwShare_platform)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceCount()");
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select count(*) from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d");
		if(!StringUtil.IsEmpty(gwShare_platform)
				&& !"null".equals(gwShare_platform)
				&& !"-1".equals(gwShare_platform))
		{
			pSQL.append(",stb_tab_customer f ");
			pSQL.append("where a.device_status=1 and a.vendor_id=b.vendor_id and a.customer_id=f.customer_id ");
		}
		else {
			pSQL.append(" where a.device_status=1 and a.vendor_id=b.vendor_id ");
		}
		pSQL.append("and a.devicetype_id=d.devicetype_id and a.device_model_id=c.device_model_id ");

		pSQL.append(pinSql(cityId,vendorId,deviceModelId,devicetypeId,bindType,deviceSerialnumber,deviceIp,true));

		if(!StringUtil.IsEmpty(gwShare_platform)
				&& !"null".equals(gwShare_platform)
				&& !"-1".equals(gwShare_platform))
		{
			pSQL.appendAndString("f.platform", PrepareSQL.EQUEAL, gwShare_platform);
		}

		return jt.queryForInt(pSQL.getSQL());
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 */
	public int queryDeviceCount(long areaId,String cityId,String custaccount)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceCount()");
		PrepareSQL pSQL = new PrepareSQL();
		String sqlForXJ = "";
		// 新疆电信 机顶盒零配置    业务账号不入设备表
		if (Global.XJDX.equals(Global.instAreaShortName)) {
			sqlForXJ = ",stb_tab_customer f";
		}
		// TODO wait (more table related)
		if (Global.HNLT.equals(Global.instAreaShortName)) {
			pSQL.append("select count(*) from stb_tab_gw_device a,stb_tab_vendor b,");
			pSQL.append("stb_gw_device_model c,stb_tab_devicetype_info d,stb_tab_customer e ");
			pSQL.append("where a.device_status=1 and a.vendor_id=b.vendor_id ");
			pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
			pSQL.append("and a.customer_id=e.customer_id ");
		}else{// TODO wait (more table related)
			pSQL.setSQL("select count(*) from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d" + sqlForXJ);
			pSQL.append(" where a.device_status=1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id ");
			pSQL.append("and a.devicetype_id=d.devicetype_id ");
		}

		if (Global.XJDX.equals(Global.instAreaShortName)) {
			pSQL.append(" and a.customer_id = f.customer_id ");
		}

		pSQL.append(pinSql(cityId,null,null,null,null,null,null,false));

		if(!StringUtil.IsEmpty(custaccount))
		{
			if (Global.XJDX.equals(Global.instAreaShortName)) {
				pSQL.appendAndString("f.serv_account", PrepareSQL.EQUEAL, custaccount);
			}else if(Global.HNLT.equals(Global.instAreaShortName)){
				pSQL.appendAndString("e.serv_account", PrepareSQL.EQUEAL, custaccount);
			}else {
				pSQL.appendAndString("a.serv_account", PrepareSQL.EQUEAL, custaccount);
			}

		}
		return jt.queryForInt(pSQL.getSQL());
	}

	/**
	 * 查询设备列表(关联设备状态表查询)
	 */
	public List queryDeviceByLikeStatus(long areaId,
			String cityId,String vendorId,String deviceModelId,String devicetypeId,
			String bindType,String deviceSerialnumber,String deviceIp,String onlineStatus)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceByLikeStatus()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
				"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
				"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
				"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
				"b.vendor_add,c.device_model,d.softwareversion ");// TODO wait (more table related)
		pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e ");
		pSQL.append("where a.device_status=1 and a.device_id=e.device_id and a.vendor_id=b.vendor_id ");
		pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");

		if(Global.XJDX.equals(Global.instAreaShortName)
				&& !StringUtil.IsEmpty(deviceSerialnumber)
				&& deviceSerialnumber.contains(":"))
		{
			//新疆的高级查询
			String sql = "select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
					"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
					"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
					"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
					"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
					"b.vendor_add,c.device_model,d.softwareversion "// TODO wait (more table related)
						+"from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,"
						+"stb_tab_devicetype_info d,stb_gw_devicestatus e ";
			String temp[] = deviceSerialnumber.split(":");
			deviceSerialnumber = temp[0];
			String hardware = temp[1];
			String belong = temp[2];
			if(!StringUtil.IsEmpty(belong) && !"-1".equals(belong)){
				sql +=",stb_tab_customer f";
				sql +=" where a.customer_id=f.customer_id and f.platform in" + belong + " and ";
			}
			if(!sql.contains("where")){
				sql += " where";
			}
			sql +=" a.device_status=1 and a.device_id=e.device_id and a.vendor_id=b.vendor_id ";
			sql +="and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id";

			if(!StringUtil.IsEmpty(hardware) && !"-1".equals(hardware)){
				sql = sql + " and d.hardwareversion='" + hardware + "'";
			}
			pSQL.append(sql);
	    }
		if(!StringUtil.IsEmpty(onlineStatus) && !"-1".equals(onlineStatus)){
			pSQL.appendAndNumber("e.online_status",PrepareSQL.EQUEAL, onlineStatus);
		}

		pSQL.append(pinSql(cityId,vendorId,deviceModelId,devicetypeId,bindType,deviceSerialnumber,deviceIp,false));
		pSQL.append(" order by a.complete_time");

		return jt.query(pSQL.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 查询设备列表(关联设备状态表查询)
	 */
	public List queryDeviceByLikeStatus(int curPage_splitPage,int num_splitPage,long areaId,
			String cityId,String vendorId,String deviceModelId,String devicetypeId,
			String bindType,String deviceSerialnumber,String deviceIp,String onlineStatus,
			int gwShare_OrderByUpdateDate)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceByLikeStatus()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
				"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
				"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
				"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
				"b.vendor_add,c.device_model,d.softwareversion,e.last_time ");// TODO wait (more table related)
		pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e ");
		pSQL.append("where a.device_status =1 and  a.device_id=e.device_id and a.vendor_id=b.vendor_id ");
		pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");

		//新疆的高级查询
		if(Global.XJDX.equals(Global.instAreaShortName)
				&& !StringUtil.IsEmpty(deviceSerialnumber)
				&& deviceSerialnumber.contains(":"))
		{
			String temp[] = deviceSerialnumber.split(":");
			deviceSerialnumber = temp[0];
			String hardware = temp[1];
			String belong = temp[2];
			String sql = " select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
					"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
					"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
					"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
					"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
					"b.vendor_add,c.device_model,d.softwareversion,e.last_time "// TODO wait (more table related)
						+ "from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e ";
		    if(!StringUtil.IsEmpty(belong) && !"-1".equals(belong)){
				sql = sql + ",stb_tab_customer f";
				sql +=" where a.customer_id=f.customer_id and f.platform in" + belong + " and";
			}
			if(!sql.contains("where")){
				sql = sql + " where";
			}
			sql += " a.device_status=1 and a.device_id=e.device_id and a.vendor_id=b.vendor_id ";
			sql += " and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id";

			if(!StringUtil.IsEmpty(hardware) && !"-1".equals(hardware)){
				sql = sql + " and d.hardwareversion='" + hardware + "' ";
			}
			pSQL.setSQL(sql);
		}
		if(!StringUtil.IsEmpty(onlineStatus) && !"-1".equals(onlineStatus)){
			pSQL.appendAndNumber("e.online_status",PrepareSQL.EQUEAL, onlineStatus);
		}

		pSQL.append(pinSql(cityId,vendorId,deviceModelId,devicetypeId,bindType,deviceSerialnumber,deviceIp,false));
		pSQL.append(pinOrder(gwShare_OrderByUpdateDate));

		return jt.querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage,num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 查询设备列表(关联设备状态表查询)
	 */
	public List queryDeviceByLikeStatusByStb(int curPage_splitPage,int num_splitPage,long areaId,
			String cityId,String vendorId,String deviceModelId,String devicetypeId,
			String bindType,String deviceSerialnumber,String deviceIp,String onlineStatus,
			int gwShare_OrderByUpdateDate,String gwShare_platform)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceByLikeStatusByStb()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
				"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
				"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
				"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
				"b.vendor_add,c.device_model,d.softwareversion,e.last_time ");
		pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,");// TODO wait (more table related)
		pSQL.append("stb_tab_devicetype_info d,stb_gw_devicestatus e ");

		if(!StringUtil.IsEmpty(gwShare_platform)
				&& !"null".equals(gwShare_platform)
				&& !"-1".equals(gwShare_platform))
		{
			pSQL.append(",stb_tab_customer f ");
			pSQL.append("where a.vendor_id=b.vendor_id and a.customer_id=f.customer_id ");
		}
		else {
			pSQL.append("where a.vendor_id=b.vendor_id ");
		}
		pSQL.append("and a.device_status=1 and a.device_id=e.device_id ");
		pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");

		if(!StringUtil.IsEmpty(onlineStatus) && !"-1".equals(onlineStatus)){
			pSQL.appendAndNumber("e.online_status",PrepareSQL.EQUEAL, onlineStatus);
		}

		if(null!=gwShare_platform && !"null".equals(gwShare_platform) && !"".equals(gwShare_platform) && !"-1".equals(gwShare_platform)){
			pSQL.appendAndString("f.platform", PrepareSQL.EQUEAL, gwShare_platform);
		}

		pSQL.append(pinSql(cityId,vendorId,deviceModelId,devicetypeId,bindType,deviceSerialnumber,deviceIp,false));
		pSQL.append(pinOrder(gwShare_OrderByUpdateDate));

		return jt.querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage,num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 查询设备列表(关联设备状态表查询)
	 */
	public int queryDeviceByLikeStatusCount(long areaId,String cityId,String vendorId,
			String deviceModelId,String devicetypeId,String bindType,String deviceSerialnumber,
			String deviceIp,String onlineStatus)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceByLikeStatus()");
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select count(*) from stb_tab_gw_device a,stb_tab_vendor b,");
		pSQL.append("stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e ");
		pSQL.append("where a.device_status=1 and a.device_id=e.device_id and a.vendor_id=b.vendor_id ");
		pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");

		if(Global.XJDX.equals(Global.instAreaShortName)
				&& !StringUtil.IsEmpty(deviceSerialnumber)
				&& deviceSerialnumber.contains(":"))
		{
			 String temp[] = deviceSerialnumber.split(":");
			 deviceSerialnumber = temp[0];
			 String hardware = temp[1];
			 String belong = temp[2];
			// TODO wait (more table related)
			String sql = " select count(*) from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e ";
				if(!StringUtil.IsEmpty(belong) && !"-1".equals(belong)){
					sql = sql + ",stb_tab_customer f";
					sql = sql + " where a.customer_id=f.customer_id and f.platform in" + belong + " and";
				}
				if(!sql.contains("where")){
					sql = sql + " where";
				}
				sql += " a.device_status=1 and a.device_id=e.device_id and a.vendor_id=b.vendor_id ";
				sql += "and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id";
				if(!StringUtil.IsEmpty(hardware) && !"-1".equals(hardware)){
				 sql = sql + " and d.hardwareversion='" + hardware + "' ";
				}
				pSQL.setSQL(sql);
			}
		if(!StringUtil.IsEmpty(onlineStatus) && !"-1".equals(onlineStatus)){
			pSQL.appendAndNumber("e.online_status",PrepareSQL.EQUEAL, onlineStatus);
		}
		pSQL.append(pinSql(cityId,vendorId,deviceModelId,devicetypeId,bindType,deviceSerialnumber,deviceIp,false));

		return jt.queryForInt(pSQL.getSQL());
	}

	/**
	 * 查询设备列表(关联设备状态表查询)
	 */
	public int queryDeviceByLikeStatusCountByStb(long areaId,String cityId,String vendorId,
			String deviceModelId,String devicetypeId,String bindType,String deviceSerialnumber,
			String deviceIp,String onlineStatus,String gwShare_platform)
	{
		logger.debug("GwDeviceQueryDAO=>queryDeviceByLikeStatus()");
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.append("select count(*) from stb_tab_gw_device a,stb_tab_vendor b,");
		pSQL.append("stb_gw_device_model c,stb_tab_devicetype_info d,stb_gw_devicestatus e");
		pSQL.append("");
		pSQL.append("");

		if(!StringUtil.IsEmpty(gwShare_platform)
				&& !"null".equals(gwShare_platform)
				&& !"-1".equals(gwShare_platform))
		{
			pSQL.append(",stb_tab_customer f where a.device_status=1 and a.customer_id=f.customer_id  ");
		}
		else
		{
			pSQL.append(" where a.device_status=1 ");
		}
		pSQL.append(" and a.device_id=e.device_id and a.vendor_id=b.vendor_id ");
		pSQL.append(" and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");

		if(!StringUtil.IsEmpty(onlineStatus) && !"-1".equals(onlineStatus)){
			pSQL.appendAndNumber("e.online_status",PrepareSQL.EQUEAL, onlineStatus);
		}

		pSQL.append(pinSql(cityId,vendorId,deviceModelId,devicetypeId,bindType,deviceSerialnumber,deviceIp,true));

		if(!StringUtil.IsEmpty(gwShare_platform)
				&& !"null".equals(gwShare_platform)
				&& !"-1".equals(gwShare_platform)){
			pSQL.appendAndString("f.platform", PrepareSQL.EQUEAL, gwShare_platform);
		}

		return jt.queryForInt(pSQL.getSQL());
	}

	/**
	 * 数据转换
	 */
	public Map<String, String> resultSet2Map(Map<String, String> map,ResultSet rs)
	{
		try{
			map.put("device_id", rs.getString("device_id"));
			map.put("oui", rs.getString("oui"));
			map.put("device_serialnumber", rs.getString("device_serialnumber"));
			map.put("device_name", rs.getString("device_name"));
			map.put("city_id", rs.getString("city_id"));
			map.put("city_name",CityDAO.getCityIdCityNameMap().get(rs.getString("city_id")));
			map.put("office_id", rs.getString("office_id"));
			map.put("complete_time", new DateTimeUtil(rs.getLong("complete_time")*1000).getYYYY_MM_DD_HH_mm_ss());
			map.put("zone_id", rs.getString("zone_id"));
			map.put("buy_time", new DateTimeUtil(rs.getLong("buy_time")*1000).getYYYY_MM_DD_HH_mm_ss());
			map.put("staff_id", rs.getString("staff_id"));
			map.put("remark", rs.getString("remark"));
			map.put("loopback_ip", rs.getString("loopback_ip"));
			map.put("interface_id", rs.getString("interface_id"));
			map.put("device_status", rs.getString("device_status"));
			map.put("gather_id", rs.getString("gather_id"));
			map.put("devicetype_id", rs.getString("devicetype_id"));
			map.put("softwareversion", rs.getString("softwareversion"));
			map.put("maxenvelopes", rs.getString("maxenvelopes"));
			map.put("cr_port", rs.getString("cr_port"));
			map.put("cr_path", rs.getString("cr_path"));
			map.put("cpe_mac", rs.getString("cpe_mac"));
			map.put("cpe_currentupdatetime", new DateTimeUtil(rs.getLong("cpe_currentupdatetime")*1000).getYYYY_MM_DD_HH_mm_ss());
			map.put("cpe_allocatedstatus", rs.getString("cpe_allocatedstatus"));
			map.put("cpe_username", rs.getString("cpe_username"));
			map.put("cpe_passwd", rs.getString("cpe_passwd"));
			map.put("acs_username", rs.getString("acs_username"));
			map.put("acs_passwd", rs.getString("acs_passwd"));
			map.put("device_type", rs.getString("device_type"));
			map.put("x_com_username", rs.getString("x_com_username"));
			map.put("x_com_passwd", rs.getString("x_com_passwd"));
			map.put("gw_type", rs.getString("gw_type"));
			map.put("device_model_id", rs.getString("device_model_id"));
			map.put("device_model", rs.getString("device_model"));
			map.put("customer_id", rs.getString("customer_id"));
			map.put("serv_account", rs.getString("serv_account"));
			map.put("zero_account", rs.getString("zero_account"));
			map.put("device_url", rs.getString("device_url"));
			map.put("x_com_passwd_old", rs.getString("x_com_passwd_old"));
			map.put("vendor_id", rs.getString("vendor_id"));
			map.put("vendor_add", rs.getString("vendor_add"));

			if(Global.HNLT.equals(Global.instAreaShortName)){
				map.put("epg_version", rs.getString("epg_version"));
				map.put("apk_version_name", rs.getString("apk_version_name"));
			}
		}catch(SQLException e){
			logger.error(e.getMessage(), e);
		}
		return map;
	}

	/**
	 * 插入机顶盒导入表
	 */
	public void insertTmp(String fileName,List<String> dataList,String importQueryField)
	{
		ArrayList<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = null;
		if("username".equals(importQueryField)
				|| "kdusername".equals(importQueryField))
		{
			for (int i = 0;i<dataList.size();i++)
			{
				psql = new PrepareSQL();
				psql.append("insert into stb_tab_seniorquery_tmp " + "(filename,username)");
				psql.append(" values ('" + fileName + "','"  + dataList.get(i) +"')");
				sqlList.add(psql.getSQL());
				if(sqlList.size() > 200){
					DBOperation.executeUpdate(sqlList);
					sqlList.clear();
				}
			}
		}
		else
		{
			for (int i = 0;i<dataList.size();i++)
			{
				psql = new PrepareSQL();
				psql.append("insert into stb_tab_seniorquery_tmp " + "(filename,devicesn)");
				psql.append(" values ('" + fileName + "','"  + dataList.get(i) +"')");
				sqlList.add(psql.getSQL());
				if(sqlList.size() > 200){
					DBOperation.executeUpdate(sqlList);
					sqlList.clear();
				}
			}
		}
		if(sqlList.size() > 0){
			DBOperation.executeUpdate(sqlList);
		}
	}

	/**
	 * 机顶盒导入查询导入业务账号
	 */
	public List queryDeviceByImportUsername(long areaId,String cityId,List<String> userList,String fileName)
	{
		logger.debug("queryDeviceByImportUsername()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(" select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
				"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
				"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
				"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
				"b.vendor_add,c.device_model,d.softwareversion,e.serv_account ");
		pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,");// TODO wait (more table related)
		pSQL.append("stb_tab_devicetype_info d,stb_tab_seniorquery_tmp f,stb_tab_customer e ");
		pSQL.append("where a.customer_id=e.customer_id and a.device_status=1 and a.vendor_id=b.vendor_id ");
		pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		pSQL.append(" and e.user_status in (1,2) ");

		pSQL.append(pinSql(cityId,null,null,null,null,null,null,false));
		pSQL.append(" and e.serv_account = f.username and f.filename = '"+fileName+"'");
		pSQL.append(" order by complete_time");

		return jt.query(pSQL.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map4Wireless(map, rs);
			}
		});
	}

	/**
	 * 机顶盒导入查询导入业务账号
	 */
	public List queryDeviceByImportUsernameByXjdx(long areaId,String cityId,List<String> userList,String fileName)
	{
		logger.debug("queryDeviceByImportUsername()");
		String userListString = "";
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("selecta.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
				"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
				"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
				"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
				"b.vendor_add,c.device_model,d.softwareversion,e.serv_account ");
		pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,");// TODO wait (more table related)
		pSQL.append("stb_tab_devicetype_info d,stb_tab_customer e ,stb_tab_seniorquery_tmp f ");
		pSQL.append("where a.customer_id=e.customer_id and a.device_status=1 and a.vendor_id=b.vendor_id ");
		pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		pSQL.append(" and e.USER_STATUS in (1,2) ");
		pSQL.append(" and  e.serv_account=f.username and f.filename ='" + fileName +"'");

		pSQL.append(pinSql(cityId,null,null,null,null,null,null,false));

		/*for(int i = 0;i < userList.size(); i++)
		{
			if(i == 0){
				userListString = userListString +"'"+userList.get(i)+"'";
			}else {
				userListString = userListString +",'"+userList.get(i)+"'";
			}
		}*/

		//pSQL.append(" and e.serv_account in ("+userListString+") ");
		pSQL.append(" order by complete_time");

		return jt.query(pSQL.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map4Wireless(map, rs);
			}
		});
	}

	public String querySQLImportUsernameByStb(long areaId,String cityId,String fileName)
	{
		logger.debug("queryDeviceByImportUsername()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
				"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
				"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
				"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
				"b.vendor_add,c.device_model,d.softwareversion,e.serv_account ");
		pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,");// TODO wait (more table related)
		pSQL.append("stb_tab_devicetype_info d,stb_tab_customer e ,stb_tab_seniorquery_tmp f ");
		pSQL.append("where a.customer_id=e.customer_id and a.device_status=1 and a.vendor_id=b.vendor_id ");
		pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		pSQL.append(" and e.USER_STATUS in (1,2) ");
		pSQL.append(" and  e.serv_account=f.username and f.filename ='" + fileName +"'");

		pSQL.append(pinSql(cityId,null,null,null,null,null,null,false));

		pSQL.append(" order by complete_time");

		return pSQL.getSQL();
	}

	/**
	 * 机顶盒导入查询导入设备序列号
	 */
	public List queryDeviceByImportDevicesn(long areaId,String cityId,List<String> devidesnList,String fileName)
	{
		logger.debug("queryDeviceByImportDevicesn()");

		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
				"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
				"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
				"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
				"b.vendor_add,c.device_model,d.softwareversion ");
		pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,");// TODO wait (more table related)
		pSQL.append("stb_tab_devicetype_info d,stb_tab_seniorquery_tmp f ");
		pSQL.append("where a.device_status=1 and a.vendor_id=b.vendor_id ");
		pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		pSQL.append(pinSql(cityId,null,null,null,null,null,null,false));
		pSQL.append(" and a.device_serialnumber=f.devicesn and f.filename ='" + fileName +"'");
		pSQL.append(" order by complete_time");

		return jt.query(pSQL.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map4Wireless(map, rs);
			}
		});
	}

	/**
	 * 机顶盒导入查询导入设备序列号
	 */
	public List queryDeviceByImportDevicesnByXjdx(long areaId,String cityId,List<String> devidesnList,String fileName)
	{
		logger.debug("queryDeviceByImportDevicesnByXjdx()");
		String devicesnListString = "";
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
				"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
				"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
				"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
				"b.vendor_add,c.device_model,d.softwareversion ");// TODO wait (more table related)
		//pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d ");
		pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_tab_seniorquery_tmp f ");
		pSQL.append("where a.device_status=1 and a.vendor_id=b.vendor_id ");
		pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		pSQL.append("and a.device_serialnumber = f.devicesn and f.filename ='" + fileName +"'");
		pSQL.append(pinSql(cityId,null,null,null,null,null,null,false));

		/*for(int i = 0;i < devidesnList.size(); i++)
		{
			if(i == 0){
				devicesnListString = devicesnListString +"'"+devidesnList.get(i)+"'";
			}else{
				devicesnListString = devicesnListString +",'"+devidesnList.get(i)+"'";
			}
		}*/
		//logger.warn(devicesnListString);
		//pSQL.append(" and a.device_serialnumber in ("+devicesnListString);
		//pSQL.append(" ) order by complete_time");
		pSQL.append(" order by complete_time");

		return jt.query(pSQL.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map4Wireless(map, rs);
			}
		});
	}

	public String querySQLImportDevicesnByStb(long areaId,String cityId,String fileName)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id, a.office_id, a.complete_time," +
				"a.zone_id, a.buy_time, a.staff_id, a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id," +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus," +
				"a.cpe_username, a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, a.x_com_passwd," +
				"a.gw_type, a.device_model_id, a.customer_id, a.serv_account, a.zero_account, a.device_url, a.x_com_passwd_old, a.vendor_id," +
				"b.vendor_add,c.device_model,d.softwareversion ");// TODO wait (more table related)
		pSQL.append("from stb_tab_gw_device a,stb_tab_vendor b,stb_gw_device_model c,stb_tab_devicetype_info d,stb_tab_seniorquery_tmp f ");
		pSQL.append("where a.device_status=1 and a.vendor_id=b.vendor_id ");
		pSQL.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		pSQL.append("and a.device_serialnumber = f.devicesn and f.filename ='" + fileName +"'");
		pSQL.append(pinSql(cityId,null,null,null,null,null,null,false));
		pSQL.append(" order by complete_time");
		return pSQL.getSQL();
	}

	/**
	 * 数据转换
	 */
	public Map<String, String> resultSet2Map4Wireless(Map<String, String> map,ResultSet rs) {
		try{
			map.put("device_id", rs.getString("device_id"));
			map.put("oui", rs.getString("oui"));
			map.put("device_serialnumber", rs.getString("device_serialnumber"));
			map.put("device_name", rs.getString("device_name"));
			map.put("city_id", rs.getString("city_id"));
			map.put("city_name", CityDAO.getCityIdCityNameMap().get(rs.getString("city_id")));
			map.put("office_id", rs.getString("office_id"));
			map.put("complete_time", new DateTimeUtil(rs.getLong("complete_time")*1000).getYYYY_MM_DD_HH_mm_ss());
			map.put("zone_id", rs.getString("zone_id"));
			map.put("buy_time", new DateTimeUtil(rs.getLong("buy_time")*1000).getYYYY_MM_DD_HH_mm_ss());
			map.put("staff_id", rs.getString("staff_id"));
			map.put("remark", rs.getString("remark"));
			map.put("loopback_ip", rs.getString("loopback_ip"));
			map.put("interface_id", rs.getString("interface_id"));
			map.put("device_status", rs.getString("device_status"));
			map.put("gather_id", rs.getString("gather_id"));
			map.put("devicetype_id", rs.getString("devicetype_id"));
			map.put("softwareversion", rs.getString("softwareversion"));
			map.put("maxenvelopes", rs.getString("maxenvelopes"));
			map.put("cr_port", rs.getString("cr_port"));
			map.put("cr_path", rs.getString("cr_path"));
			map.put("cpe_mac", rs.getString("cpe_mac"));
			map.put("cpe_currentupdatetime", new DateTimeUtil(rs.getLong("cpe_currentupdatetime")*1000).getYYYY_MM_DD_HH_mm_ss());
			map.put("cpe_allocatedstatus", rs.getString("cpe_allocatedstatus"));
			map.put("cpe_username", rs.getString("cpe_username"));
			map.put("cpe_passwd", rs.getString("cpe_passwd"));
			map.put("acs_username", rs.getString("acs_username"));
			map.put("acs_passwd", rs.getString("acs_passwd"));
			map.put("device_type", rs.getString("device_type"));
			map.put("x_com_username", rs.getString("x_com_username"));
			map.put("x_com_passwd", rs.getString("x_com_passwd"));
			map.put("gw_type", rs.getString("gw_type"));
			map.put("device_model_id", rs.getString("device_model_id"));
			map.put("device_model", rs.getString("device_model"));
			map.put("customer_id", rs.getString("customer_id"));
			map.put("device_url", rs.getString("device_url"));
			map.put("x_com_passwd_old", rs.getString("x_com_passwd_old"));
			map.put("vendor_id", rs.getString("vendor_id"));
			map.put("vendor_add", rs.getString("vendor_add"));
			if(!Global.XJDX.equals(Global.instAreaShortName)
					&& !Global.SDLT.equals(Global.instAreaShortName))
			{
				map.put("ssid", StringUtil.getStringValue(rs.getString("ssid")));
				map.put("vlanid", StringUtil.getStringValue(rs.getString("vlanid")));
				map.put("priority", StringUtil.getStringValue(rs.getString("priority")));
				map.put("channel", StringUtil.getStringValue(rs.getString("channel")));
			}
		}catch(SQLException e){
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return map;
	}

	/**
	 * 拼接sql条件,flag:ip是否支持模糊匹配
	 */
	private String pinSql(String cityId,String vendorId,String deviceModelId,
			String devicetypeId,String bindType,String sn,String deviceIp,boolean flag)
	{
		StringBuffer sb=new StringBuffer();
		if(!StringUtil.IsEmpty(cityId) && !"null".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)){
			ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sb.append(" and a.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
			cityArray = null;
		}
		if(!StringUtil.IsEmpty(vendorId) && !"null".equals(vendorId) && !"-1".equals(vendorId)){
			sb.append(" and a.vendor_id='"+vendorId+"'");
		}
		if(!StringUtil.IsEmpty(deviceModelId) && !"null".equals(deviceModelId) && !"-1".equals(deviceModelId)){
			sb.append(" and a.device_model_id='"+deviceModelId+"'");
		}
		if(!StringUtil.IsEmpty(devicetypeId) && !"null".equals(devicetypeId) && !"-1".equals(devicetypeId)){
			sb.append(" and a.devicetype_id="+devicetypeId);
		}
		if(!StringUtil.IsEmpty(bindType) && !"-1".equals(bindType)){
			sb.append(" and a.cpe_allocatedstatus="+bindType);
		}
		if(!StringUtil.IsEmpty(sn) && !"-1".equals(sn)){
			if(sn.length()>5){
				sb.append(" and a.dev_sub_sn ='"+sn.substring(sn.length()-6, sn.length())+"' ");
			}
			sb.append(" and a.device_serialnumber like '%"+sn+"'");
		}
		if(!StringUtil.IsEmpty(deviceIp) && !"-1".equals(deviceIp)){
			if(flag){
				/**
				 * 江西itv支持 ip查询，其中deviceIp：0为以10开头，deviceIp：1为非10开头
				 */
				if (Global.XJDX.equals(Global.instAreaShortName))
				{
					if (deviceIp.equals("0")){
						sb.append(" and a.loopback_ip like '10%'");
					}else if (deviceIp.equals("1")){
						sb.append(" and a.loopback_ip not like '10%'");
					}
				}
				else
				{
					sb.append(" and a.loopback_ip='"+deviceIp+"'");
				}
			}else{
				sb.append(" and a.loopback_ip='"+deviceIp+"'");
			}
		}

//		if( 1!=areaId ) {
//			sb.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
//			sb.append(String.valueOf(areaId));
//			sb.append(") ");
//		}
		sb.append(" ");
		return sb.toString();
	}

	/**
	 * 拼接sql条件,flag:ip是否支持模糊匹配
	 */
	private String pinSqlIpSix(String cityId,String vendorId,String deviceModelId,
			String devicetypeId,String bindType,String sn,String deviceIp,boolean flag,String deviceIpSix)
	{
		StringBuffer sb=new StringBuffer();
		if(!StringUtil.IsEmpty(cityId) && !"null".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)){
			ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sb.append(" and a.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
			cityArray = null;
		}
		if(!StringUtil.IsEmpty(vendorId) && !"null".equals(vendorId) && !"-1".equals(vendorId)){
			sb.append(" and a.vendor_id='"+vendorId+"'");
		}
		if(!StringUtil.IsEmpty(deviceModelId) && !"null".equals(deviceModelId) && !"-1".equals(deviceModelId)){
			sb.append(" and a.device_model_id='"+deviceModelId+"'");
		}
		if(!StringUtil.IsEmpty(devicetypeId) && !"null".equals(devicetypeId) && !"-1".equals(devicetypeId)){
			sb.append(" and a.devicetype_id="+devicetypeId);
		}
		if(!StringUtil.IsEmpty(bindType) && !"-1".equals(bindType)){
			sb.append(" and a.cpe_allocatedstatus="+bindType);
		}
		if(!StringUtil.IsEmpty(sn) && !"-1".equals(sn)){
			if(sn.length()>5){
				sb.append(" and a.dev_sub_sn ='"+sn.substring(sn.length()-6, sn.length())+"' ");
			}
			sb.append(" and a.device_serialnumber like '%"+sn+"'");
		}
		if(!StringUtil.IsEmpty(deviceIp) && !"-1".equals(deviceIp)){
			if(flag){
				/**
				 * 江西itv支持 ip查询，其中deviceIp：0为以10开头，deviceIp：1为非10开头
				 */
				if (Global.JXDX.equals(Global.instAreaShortName))
				{
					if (deviceIp.equals("0")){
						sb.append(" and a.loopback_ip like '10%'");
					}else if (deviceIp.equals("1")){
						sb.append(" and a.loopback_ip not like '10%'");
					}
				}
				else
				{
					sb.append(" and a.loopback_ip='"+deviceIp+"'");
				}
			}else{
				sb.append(" and a.loopback_ip='"+deviceIp+"'");
			}
		}
		if(!StringUtil.IsEmpty(deviceIpSix) && !"-1".equals(deviceIpSix)){
			sb.append(" and a.loopback_ip_six='"+deviceIpSix+"'");
		}

//		if( 1!=areaId ) {
//			sb.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
//			sb.append(String.valueOf(areaId));
//			sb.append(") ");
//		}
		sb.append(" ");
		return sb.toString();
	}

	/**
	 * 拼接排序
	 */
	private String pinOrder(int gwShare_OrderByUpdateDate)
	{
		return 1 == gwShare_OrderByUpdateDate ? " order by e.last_time DESC" : " order by a.complete_time";
	}
}
