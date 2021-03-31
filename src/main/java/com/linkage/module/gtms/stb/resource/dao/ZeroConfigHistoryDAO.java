package com.linkage.module.gtms.stb.resource.dao;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

public class ZeroConfigHistoryDAO extends SuperDAO{

	private static Logger logger = LoggerFactory
			.getLogger(ZeroConfigHistoryDAO.class);

	/**
	 * 分页查询零配置机顶盒历史配置信息
	 * @param curPageSplitPage
	 * @param numSplitPage
	 * @param servAccount
	 * @param deviceSerialnumber
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<Map> getZeroConfigHistory(int curPageSplitPage,int numSplitPage,String servAccount,String deviceSerialnumber,String starttime,String endtime){
		logger.debug("ZeroConfigHistoryDAO == >getZeroConfigHistory({})", new Object[] {servAccount,deviceSerialnumber,starttime,endtime });
		PrepareSQL sb = new PrepareSQL();
		sb.append("select device_id,city_id,serv_account,bind_way,bind_state,device_serialnumber,bind_time");
		sb.append(" from stb_tab_gw_device");
		sb.append(" where bind_time >= ?");
		sb.append(" and bind_time <=?");
		if(!StringUtil.IsEmpty(servAccount)){
			sb.append(" and serv_account = '"+servAccount+"'");
		}
		if(!StringUtil.IsEmpty(deviceSerialnumber)){
			sb.append(" and device_serialnumber = '"+deviceSerialnumber+"'");
		}
		sb.append(" order by bind_time desc");

		sb.setInt(1, StringUtil.getIntegerValue(starttime));
		sb.setInt(2, StringUtil.getIntegerValue(endtime));
		return querySP(sb.getSQL(), (curPageSplitPage - 1) * numSplitPage,numSplitPage);
	}

	public int getZeroConfigHistoryCount(String servAccount,String deviceSerialnumber,String starttime,String endtime) {
		logger.debug("ZeroConfigHistoryDAO == >getZeroConfigHistoryCount({})", new Object[] {servAccount,deviceSerialnumber,starttime,endtime });
		PrepareSQL sb = new PrepareSQL();
		sb.append("select count(*)");
		sb.append(" from stb_tab_gw_device");
		sb.append(" where bind_time >="+starttime);
		sb.append(" and bind_time <="+endtime);
		if(!StringUtil.IsEmpty(servAccount)){
			sb.append(" and serv_account = '"+servAccount+"'");
		}
		if(!StringUtil.IsEmpty(deviceSerialnumber)){
			sb.append(" and device_serialnumber = '"+deviceSerialnumber+"'");
		}
		return jt.queryForInt(sb.getSQL());
	}

	public List<Map> doZeroHistoryExcel(String servAccount,String deviceSerialnumber,String starttime,String endtime){
		logger.debug("ZeroConfigHistoryDAO == >doZeroHistoryExcel({})", new Object[] {servAccount,deviceSerialnumber,starttime,endtime });
		PrepareSQL sb = new PrepareSQL();
		sb.append("select device_id,city_id,serv_account,bind_way,bind_state,device_serialnumber,bind_time");
		sb.append(" from stb_tab_gw_device");
		sb.append(" where bind_time >= ?");
		sb.append(" and bind_time <=?");
		if(!StringUtil.IsEmpty(servAccount)){
			sb.append(" and serv_account = '"+servAccount+"'");
		}
		if(!StringUtil.IsEmpty(deviceSerialnumber)){
			sb.append(" and device_serialnumber = '"+deviceSerialnumber+"'");
		}
		sb.append(" order by bind_time desc");

		sb.setInt(1, StringUtil.getIntegerValue(starttime));
		sb.setInt(2, StringUtil.getIntegerValue(endtime));
		return jt.queryForList(sb.getSQL());
	}


	/**
	 * 分页查询单个设备历史绑定流程
	 * @param curPageSplitPage
	 * @param numSplitPage
	 * @param deviceId
	 * @return
	 */
	public List<Map> getDeviceZeroConfigDetail(int curPageSplitPage,int numSplitPage,String deviceId,String starttime,String endtime,String failReasonMark,String reasonId,String ctiyId){
		logger.debug("ZeroConfigHistoryDAO == >getDeviceZeroConfigDetail({})", deviceId);
		PrepareSQL sb = new PrepareSQL();
		sb.append("select a.buss_id,a.fail_reason_id,a.bind_way,a.start_time,a.fail_time,a.return_value,b.reason_desc");
		sb.append(" from stb_tab_zeroconfig_fail a,stb_tab_zeroconfig_reason b");
		if("true".equals(failReasonMark)){
			sb.append(",stb_tab_gw_device c ");// TODO wait (more table related)
		}
		sb.append(" where a.fail_reason_id = b.reason_id");
		sb.append(" and a.fail_time >= "+starttime);
		sb.append(" and a.fail_time <= "+endtime);
		sb.append(" and a.device_id = "+deviceId);
		if("true".equals(failReasonMark)){
			if(DBUtil.GetDB() == 3)
			{// mysql
				sb.append(" and c.device_id = cast(a.device_id as char)");
			}else{
				sb.append(" and c.device_id = convert(varchar,a.device_id)");
			}
			sb.append(" and a.fail_reason_id = "+reasonId);
			sb.append(" and c.city_id = '" + ctiyId + "'");
		}
		sb.append(" order by a.fail_time desc");
		return querySP(sb.getSQL(), (curPageSplitPage - 1) * numSplitPage,numSplitPage);
	}

	public int getDeviceZeroConfigDetailCount(String deviceId,String starttime,String endtime,String failReasonMark,String reasonId,String ctiyId){
		logger.debug("ZeroConfigHistoryDAO == >getDeviceZeroConfigDetail({})", deviceId);
		PrepareSQL sb = new PrepareSQL();
		sb.append("select count(*)");
		sb.append(" from stb_tab_zeroconfig_fail a,stb_tab_zeroconfig_reason b");
		if("true".equals(failReasonMark)){
			sb.append(",stb_tab_gw_device c ");// TODO wait (more table related)
		}
		sb.append(" where a.fail_reason_id = b.reason_id");
		sb.append(" and a.fail_time >= "+starttime);
		sb.append(" and a.fail_time <= "+endtime);
		sb.append(" and a.device_id = "+deviceId);
		if("true".equals(failReasonMark)){
			if(DBUtil.GetDB() == 3)
			{// mysql
				sb.append(" and c.device_id = cast(a.device_id as char)");
			}else{
				sb.append(" and c.device_id = convert(varchar,a.device_id)");
			}
			sb.append(" and a.fail_reason_id = "+reasonId);
			sb.append(" and c.city_id = '" + ctiyId + "'");
		}
		return jt.queryForInt(sb.getSQL());
	}

	public List<Map> getDeviceZeroConfigDetail(String deviceId){
		logger.debug("ZeroConfigHistoryDAO == >getDeviceZeroConfigDetail({})", deviceId);
		PrepareSQL sb = new PrepareSQL();
		sb.append("select a.buss_id,a.bind_way,a.start_time,a.fail_time,a.return_value,b.reason_desc");
		sb.append(" from stb_tab_zeroconfig_fail a,stb_tab_zeroconfig_reason b");
		sb.append(" where a.fail_reason_id = b.reason_id");
		sb.append(" and a.device_id = " + deviceId);
		return jt.queryForList(sb.getSQL());
	}
}
