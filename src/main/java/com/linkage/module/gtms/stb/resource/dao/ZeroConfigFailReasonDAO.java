package com.linkage.module.gtms.stb.resource.dao;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

public class ZeroConfigFailReasonDAO extends SuperDAO{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ZeroConfigFailReasonDAO.class);

	/**
	 * 查询所有的失败原因
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<Map> queryZeroConfigFailReason(String starttime,String endtime){

		logger.debug("ZeroConfigFailReasonDAO==>queryZeroConfigFailReason({},{})",starttime,endtime);
		PrepareSQL sb = new PrepareSQL();
		sb.append("select a.city_id,b.fail_reason_id,count(*) as num");
		sb.append(" from stb_tab_gw_device a,stb_tab_zeroconfig_fail b");
		if (LipossGlobals.isOracle()){
			sb.append(" where a.device_id=to_char(b.device_id)");
		}
		else if(DBUtil.GetDB() == 3)
		{
			sb.append(" where a.device_id = cast(b.device_id as char)");
		}
		else{
			sb.append(" where a.device_id=convert(varchar,b.device_id)");
		}
		sb.append(" and b.fail_time >= ?");
		sb.append(" and b.fail_time <=?");
		sb.append(" group by a.city_id,b.fail_reason_id");
		sb.setInt(1, StringUtil.getIntegerValue(starttime));
		sb.setInt(2, StringUtil.getIntegerValue(endtime));
		return jt.queryForList(sb.getSQL());
	}

	/**
	 * 查询所有的失败情况
	 * @return
	 */
	public List<Map> queryZeroConfiglFailDesc(){
		logger.debug("ZeroConfigFailReasonDAO==>queryZeroConfiglFailDesc()");
		PrepareSQL sb = new PrepareSQL();
		if (LipossGlobals.isOracle())
		{
			sb.append("select reason_id,reason_desc from stb_tab_zeroconfig_reason where to_number(reason_id) > 5");
		}
		else if(DBUtil.GetDB() == 3)
		{
			sb.append("select reason_id,reason_desc from stb_tab_zeroconfig_reason where cast(reason_id as signed) > 5");
		}
		else
		{
			sb.append("select reason_id,reason_desc from stb_tab_zeroconfig_reason where convert(int,reason_id) > 5");
		}
		return jt.queryForList(sb.getSQL());
	}

	/**
	 * 根据cityid和失败描述查询设备
	 * @param cityId
	 * @param reasonDesc
	 * @return
	 */
	public List<Map> queryZeroConfigFailDevice(String cityId,String reasonId,String starttime,String endtime,int curPageSplitPage,int numSplitPage){
		logger.debug("ZeroConfigFailReasonDAO==>queryZeroConfigFailDevice()");
		PrepareSQL sb = new PrepareSQL();// TODO wait (more table related)
		sb.append(" select distinct a.device_id,a.city_id,d.vendor_name,e.device_model,f.softwareversion,a.device_serialnumber,a.cpe_mac,a.serv_account,a.loopback_ip");
		sb.append(" from stb_tab_gw_device a,stb_tab_zeroconfig_fail b,stb_tab_zeroconfig_reason c,stb_tab_vendor d,stb_gw_device_model e,stb_tab_devicetype_info f");
		if (LipossGlobals.isOracle())
		{
			sb.append(" where a.device_id = to_char(b.device_id)");
		}
		else if(DBUtil.GetDB() == 3)
		{
			sb.append(" where a.device_id = cast(b.device_id as char)");
		}
		else
		{
			sb.append(" where a.device_id = convert(varchar,b.device_id)");
		}
		sb.append(" and b.fail_reason_id = c.reason_id");
		sb.append(" and a.vendor_id = d.vendor_id");
		sb.append(" and a.device_model_id = e.device_model_id");
		sb.append(" and a.devicetype_id = f.devicetype_id");
		sb.append(" and a.city_id in " + cityId);
		sb.append(" and b.fail_time >= " + starttime);
		sb.append(" and b.fail_time <= " + endtime);
		sb.append(" and c.reason_id = " + reasonId);
		//sb.append(" order by b.fail_time desc");
		return querySP(sb.getSQL(), (curPageSplitPage - 1) * numSplitPage,numSplitPage);
	}

	public int queryZeroConfigFailDeviceCount(String cityId,String reasonId,String starttime,String endtime){
		logger.debug("ZeroConfigFailReasonDAO==>queryZeroConfigFailDeviceCount()");
		PrepareSQL sb = new PrepareSQL();// TODO wait (more table related)
		sb.append(" select  count(*) from (");
		sb.append(" select distinct  a.device_id,a.city_id,d.vendor_name,e.device_model,f.softwareversion,a.device_serialnumber,a.cpe_mac,a.serv_account,a.loopback_ip");
		sb.append(" from stb_tab_gw_device a,stb_tab_zeroconfig_fail b,stb_tab_zeroconfig_reason c,stb_tab_vendor d,stb_gw_device_model e,stb_tab_devicetype_info f");
		if (LipossGlobals.isOracle())
		{
			sb.append(" where a.device_id = to_char(b.device_id)");
		}
		else if(DBUtil.GetDB() == 3)
		{
			sb.append(" where a.device_id = cast(b.device_id as char)");
		}
		else
		{
			sb.append(" where a.device_id = convert(varchar,b.device_id)");
		}
		sb.append(" and b.fail_reason_id = c.reason_id");
		sb.append(" and a.vendor_id = d.vendor_id");
		sb.append(" and a.device_model_id = e.device_model_id");
		sb.append(" and a.devicetype_id = f.devicetype_id");
		sb.append(" and a.city_id  in " + cityId);
		sb.append(" and b.fail_time >= " + starttime);
		sb.append(" and b.fail_time <= " + endtime);
		sb.append(" and c.reason_id = " + reasonId);
		sb.append(" ) tempTable");
		return jt.queryForInt(sb.getSQL());
	}

	public List<Map> zeroConfigFaileDeviceExcel(String cityId,String reasonId,String starttime,String endtime){
		logger.debug("ZeroConfigFailReasonDAO==>queryZeroConfigFailDevice()");
		PrepareSQL sb = new PrepareSQL();// TODO wait (more table related)
		sb.append(" select distinct a.device_id,a.city_id,d.vendor_name,e.device_model,f.softwareversion,a.device_serialnumber,a.cpe_mac,a.serv_account,a.loopback_ip");
		sb.append(" from stb_tab_gw_device a,stb_tab_zeroconfig_fail b,stb_tab_zeroconfig_reason c,stb_tab_vendor d,stb_gw_device_model e,stb_tab_devicetype_info f");
		if (LipossGlobals.isOracle())
		{
			sb.append(" where a.device_id = to_char(b.device_id)");
		}
		else if(DBUtil.GetDB() == 3)
		{
			sb.append(" where a.device_id = cast(b.device_id as char)");
		}
		else
		{
			sb.append(" where a.device_id = convert(varchar,b.device_id)");
		}
		sb.append(" and b.fail_reason_id = c.reason_id");
		sb.append(" and a.vendor_id = d.vendor_id");
		sb.append(" and a.device_model_id = e.device_model_id");
		sb.append(" and a.devicetype_id = f.devicetype_id");
		sb.append(" and a.city_id in " + cityId);
		sb.append(" and b.fail_time >= " + starttime);
		sb.append(" and b.fail_time <= " + endtime);
		sb.append(" and c.reason_id = " + reasonId);
		//sb.append(" order by b.fail_time desc");
		return jt.queryForList(sb.getSQL());
	}


}
