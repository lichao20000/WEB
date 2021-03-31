package com.linkage.module.gtms.blocTest.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gtms.blocTest.obj.DeviceObj;
import com.linkage.module.gtms.blocTest.obj.VoipSheetOBJ;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;
import com.linkage.module.gwms.util.StringUtil;

public class BusinessSheetDispatchDAOImpl extends SuperDAO implements
		BusinessSheetDispatchDAO {

	public static Logger logger = LoggerFactory
			.getLogger(BusinessSheetDispatchDAOImpl.class);

	/**
	 * 支持多帐号，根据_userId+_servTypeId+itvUsername判断业务用户是否存在
	 *
	 * @author zhangsm 2012-1-5
	 * @param _userId
	 * @param _servTypeId
	 * @param itvUsername
	 * @return
	 */
	public boolean servUserIsExists(long _userId, int _servTypeId,
			String itvUsername) {
		logger.debug("servUserIsExists({},{})", new Object[] { _userId,
				_servTypeId, itvUsername });

		/** * 判断业务用户的存在性 */
		String strSQL = "select count(*) num from hgwcust_serv_info "
				+ " where user_id=? and serv_type_id=?" + " and serv_status=1";

		PrepareSQL psql = new PrepareSQL(strSQL);
		if (!StringUtil.IsEmpty(itvUsername)) {
			psql.append(" and username='" + itvUsername + "'");
		}
		psql.setLong(1, _userId);
		psql.setInt(2, _servTypeId);

		Map<String, String> map = DBOperation.getRecord(psql.getSQL());
		if (null != map &&!map.isEmpty() &&StringUtil.getIntValue(map, "num")>0) {
			logger.warn("servUserIsExists() return true");
			return true;
		}
		logger.warn("servUserIsExists() return false");
		return false;
	}

	/**
	 * 判断业务用户是否存在,存在返回true, 否则返回false
	 *
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-10
	 * @return boolean
	 */
	public boolean servUserIsExists(long _userId, int _servTypeId) {
		logger.debug("servUserIsExists({},{})", new Object[] { _userId,
				_servTypeId });

		/** * 判断业务用户的存在性 */
		String strSQL = "select count(*) num from hgwcust_serv_info "
				+ " where user_id=? and serv_type_id=?" + " and serv_status=1";

		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setLong(1, _userId);
		psql.setInt(2, _servTypeId);

		Map<String, String> map = DBOperation.getRecord(psql.getSQL());
		if (null != map && !map.isEmpty() &&StringUtil.getIntValue(map, "num")>0) {
			logger.warn("servUserIsExists() return true");
			return true;
		}
		logger.warn("servUserIsExists() return false");
		return false;
	}

	/**
	 * 判断(Voip)业务用户的存在性,如果存在返回true, 否则返回false
	 *
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-10
	 * @return boolean
	 */
	public boolean servUserIsExists(VoipSheetOBJ dbHgwObj, int _servTypeId) {
		logger.debug("servUserIsExists({},{})", new Object[] { dbHgwObj,
				_servTypeId });

		/** * 判断业务用户的存在性 */
		String strSQL = "select username, passwd from hgwcust_serv_info "
				+ " where user_id=? and serv_type_id=?" + " and serv_status=1";

		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setLong(1, StringUtil.getLongValue(dbHgwObj.getUserId()));
		psql.setInt(2, _servTypeId);

		List lt  = jt.queryForList(psql.getSQL());
		if (null != lt && lt.size() > 0) {
			logger.warn("servUserExists() return true");
			if (null != dbHgwObj) {
				dbHgwObj.setVoipUsername(StringUtil.getStringValue((Map)lt.get(0),
						"username"));
				dbHgwObj
						.setVoipPasswd(StringUtil.getStringValue((Map)lt.get(0), "passwd"));
			}
			return true;
		}
		logger.warn("servUserExists() return false");
		return false;
	}
	/**
	 * 更新开户状态的业务用户的信息,业务用户本身处于开户状态
	 *
	 * @date  2012年5月18日
	 * @return int 是否更新
	 */
	public int updateServUser(HgwServUserObj servUserObj,String gw_type) {
		logger.debug("updateServUser({})", servUserObj);
		long nowTime = new Date().getTime()/1000;

		String tableName = "";
		if (Global.GW_TYPE_ITMS.equals(gw_type)) {
			tableName = " hgwcust_serv_info ";
		} else if (Global.GW_TYPE_BBMS.equals(gw_type)) {
			tableName = " egwcust_serv_info ";
		} else {
			tableName = " hgwcust_serv_info ";
		}

		String strSQL = "update "+tableName+" set username=?,"
				+ " passwd=?,wan_type=?,vpiid=?,vciid=?,vlanid=?,"
				+ " ipaddress=?,ipmask=?,gateway=?,adsl_ser=?,bind_port=?,"
				+ "updatetime=?,open_status=?,"
				+ " ipv6_address_origin=?,aftr=?,ipv6_address_ip=?,"
				+ " ipv6_address_dns=?,ipv6_prefix_prefix=?"
				+ " where user_id=? and serv_type_id=?";

		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1, servUserObj.getUsername());
		psql.setString(2, servUserObj.getPasswd());
		psql.setInt(3, StringUtil.getIntegerValue(servUserObj.getWanType()));
		psql.setString(4, servUserObj.getVpiid());
		psql.setInt(5, StringUtil.getIntegerValue(servUserObj.getVciid()));
		psql.setString(6, servUserObj.getVlanid());
		psql.setString(7, servUserObj.getIpAddress());
		psql.setString(8, servUserObj.getIpMask());
		psql.setString(9, servUserObj.getGateway());
		psql.setString(10, servUserObj.getDns());
		psql.setString(11, servUserObj.getBindPort());
		psql.setLong(12, nowTime);
		psql.setInt(13, StringUtil.getIntegerValue(servUserObj.getOpenStatus()));

		psql.setInt(14, StringUtil.getIntegerValue(servUserObj.getAddressOrigin()));
		psql.setString(15, servUserObj.getAftr());
		psql.setString(16, servUserObj.getIpv6AddressIp());
		psql.setString(17, servUserObj.getIpv6AddressDNS());
		psql.setString(18, servUserObj.getIpv6AddressPrefix());

		psql.setLong(19, StringUtil.getLongValue(servUserObj.getUserId()));
		psql.setInt(20, StringUtil.getIntegerValue(servUserObj.getServTypeId()));



		return jt.update(psql.getSQL());
	}
	/**
	 * 入家庭网关业务用户信息表SQL, 更新信息来源于工单数据,(不能来源于用户表)
	 *
	 * @param
	 * @date  2012年5月18日
	 * @return insert业务用户表语句
	 */
	public int saveServUser(HgwServUserObj servUserObj ,String gw_type) {
		logger.debug("saveServUser({})", servUserObj);
		long nowTime = new Date().getTime()/1000;

		String tableName = "";
		if (Global.GW_TYPE_ITMS.equals(gw_type)) {
			tableName = " hgwcust_serv_info ";
		} else if (Global.GW_TYPE_BBMS.equals(gw_type)) {
			tableName = " egwcust_serv_info ";
		} else {
			tableName = " hgwcust_serv_info ";
		}

		String strSQL = "insert into "+tableName+"("
				+ "user_id,serv_type_id,username,passwd,wan_type,serv_status,"
				+ "vpiid,vciid,vlanid,ipaddress,ipmask,"
				+ "gateway,adsl_ser,bind_port,dealdate,opendate,"
				+ "updatetime,open_status,ipv6_address_origin,aftr,"
				+ "ipv6_address_ip,ipv6_address_dns,ipv6_prefix_prefix) values ("
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(strSQL);

		psql.setStringExt(1, servUserObj.getUserId(), false);
		psql.setStringExt(2, servUserObj.getServTypeId(), false);
		psql.setString(3, servUserObj.getUsername());
		psql.setString(4, servUserObj.getPasswd());
		psql.setInt(5, StringUtil.getIntegerValue(servUserObj.getWanType()));
		// 业务状态，开户
		psql.setInt(6, 1);
		psql.setString(7, servUserObj.getVpiid());
		psql.setInt(8, StringUtil.getIntegerValue(servUserObj.getVciid()));
		psql.setString(9, servUserObj.getVlanid());
		psql.setString(10, servUserObj.getIpAddress());
		psql.setString(11, servUserObj.getIpMask());
		psql.setString(12, servUserObj.getGateway());
		psql.setString(13, servUserObj.getDns());
		psql.setString(14, servUserObj.getBindPort());
		psql.setLong(15, nowTime);
		// 开户时间
		psql.setLong(16, nowTime);
		psql.setLong(17, nowTime);

		psql.setInt(18, StringUtil.getIntegerValue(servUserObj.getOpenStatus()));

		psql.setInt(19, StringUtil.getIntegerValue(servUserObj.getAddressOrigin()));
		psql.setString(20, servUserObj.getAftr());
		psql.setString(21, servUserObj.getIpv6AddressIp());
		psql.setString(22, servUserObj.getIpv6AddressDNS());
		psql.setString(23, servUserObj.getIpv6AddressPrefix());

		return jt.update(psql.getSQL());
	}
	/**
	 * 根据设备序列号或者是用户帐号 获取 userId
	 * @param deviceSn 设备序列号
	 * @param userAccount
	 * return String userId
	 */
	public Map getUserId(String deviceId) {
		logger.debug("getUserId({})",deviceId);
		StringBuilder sql = new StringBuilder();

		if (null != deviceId) {
			sql.append("select user_id from  tab_hgwcustomer ")
				.append(" where device_id = '")
				.append(deviceId).append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return queryForMap(psql.getSQL());
	}

	/**
	 * 获取SIP服务器的ID，如果SIP服务器已经存在则取出直接返回，如果不存在则添加并返回添加的sip_id
	 *
	 * @param
	 * @return int SIP服务器ID
	 */
	public int getSipId(VoipSheetOBJ voipSheetOBJ) {
		logger.debug("getSipId({},{},{},{})", new Object[] {
				voipSheetOBJ.getSipIp(), voipSheetOBJ.getSipPort(),
				voipSheetOBJ.getStandSipIp(), voipSheetOBJ.getStandSipPort() });

		String strSQL = "select sip_id from tab_sip_info"
				+ " where prox_serv=? and prox_port=?"
				+ " and stand_prox_serv=? and stand_prox_port=?";

		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1, voipSheetOBJ.getSipIp());
		psql.setInt(2, voipSheetOBJ.getSipPort());
		psql.setString(3, voipSheetOBJ.getStandSipIp());
		psql.setInt(4, voipSheetOBJ.getStandSipPort());

		Map<String, String> resMap = DBOperation.getRecord(psql.getSQL());
		if (null == resMap || resMap.isEmpty()) {
			long maxSipId = DBOperation.getMaxId("sip_id", "tab_sip_info");
			++maxSipId;
			strSQL = "insert into tab_sip_info (sip_id, prox_serv, prox_port, stand_prox_serv, stand_prox_port, "
					+ " regi_serv, regi_port, stand_regi_serv, stand_regi_port, "
					+ " out_bound_proxy, out_bound_port, stand_out_bound_proxy, stand_out_bound_port, remark) "
					+ " values (?,?,?,?,?,   ?,?,?,?,   ?,?,?,?,?)";

			psql = new PrepareSQL(strSQL);
			psql.setLong(1, maxSipId);
			//
			psql.setString(2, voipSheetOBJ.getSipIp());
			psql.setInt(3, voipSheetOBJ.getSipPort());
			psql.setString(4, voipSheetOBJ.getStandSipIp());
			psql.setInt(5, voipSheetOBJ.getStandSipPort());
			//
			psql.setString(6, voipSheetOBJ.getRegistrarServer());
			psql.setInt(7, voipSheetOBJ.getRegistrarServerPort());
			psql.setString(8, voipSheetOBJ.getStandRegistrarServer());
			psql.setInt(9, voipSheetOBJ.getStandRegistrarServerPort());
			//
			psql.setString(10, voipSheetOBJ.getOutboundProxy());
			psql.setInt(11, voipSheetOBJ.getOutboundPort());
			psql.setString(12, voipSheetOBJ.getStandOutboundProxy());
			psql.setInt(13, voipSheetOBJ.getStandOutboundPort());
			//
			psql.setString(14, "WEB");

			DBOperation.executeUpdate(psql.getSQL());

			// 库中不存在，添加返回
			return StringUtil.getIntegerValue(maxSipId);
		} else {
			// 库中已经存在，直接返回
			return StringUtil.getIntegerValue(resMap.get("sip_id"));
		}
	}

	/**
	 * voip工单对象是否和库中存储的VOIP业务参数一致
	 *
	 * @param voipSheetOBJ
	 * @param sipId
	 * @date 2012年5月16日
	 * @return int 如果一致返回1; 库中不存在相应的业务参数返回0; 如果存在且不一致返回-1
	 */
	public int equalVoipServParam(VoipSheetOBJ voipSheetOBJ, int sipId) {
		logger.debug("equealVoipServParam({}, {})", new Object[] {
				voipSheetOBJ, sipId });

		String strSQL = "select voip_username, voip_passwd, sip_id, voip_phone"
				+ " from tab_voip_serv_param where user_id=? and line_id=?";

		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setLong(1, StringUtil.getLongValue(voipSheetOBJ.getUserId()));
		psql.setInt(2, getLineId(voipSheetOBJ.getLinePort()));

		Map<String, String> voipMap = DBOperation.getRecord(psql.getSQL());
		if (null == voipMap || voipMap.isEmpty()) {
			logger.warn("tab_voip_serv_param is null, user_id:{}", voipSheetOBJ
					.getUserId());
			return 0;
		} else {
			if (voipSheetOBJ.getVoipUsername().equals(
					voipMap.get("voip_username"))
					&& voipSheetOBJ.getVoipPasswd().equals(
							voipMap.get("voip_passwd"))
					&& sipId == StringUtil.getIntegerValue(voipMap
							.get("sip_id"))
					&& voipSheetOBJ.getVoipTelepone().equals(
							voipMap.get("voip_phone"))) {
				logger.warn("tab_voip_serv_param is equeal, user_id:{}",
						voipSheetOBJ.getUserId());
				return 1;
			} else {
				logger.warn("tab_voip_serv_param is not equeal, user_id:{}",
						voipSheetOBJ.getUserId());
				return -1;
			}
		}
	}

	/**
	 * 获取线路ID，根据线路端口 "V1"返回1; "V2"返回2
	 *
	 * @param 线路端口
	 *            ：V1 or V2
	 * @return int 1 or 2
	 */
	public int getLineId(String linePort) {
		logger.debug("getLineId({})", linePort);
		int lineId = 1;

		if ("V1".equals(linePort) || "A0".equals(linePort)
				|| "USER001".equals(linePort)) {
			lineId = 1;
		} else if ("V2".equals(linePort) || "A1".equals(linePort)
				|| "USER002".equals(linePort)) {
			lineId = 2;
		}
		return lineId;
	}

	/**
	 * 开户工单处理，更新业务用户表数据，业务用户本身处于开户状态
	 *
	 * @param hgwServUserObj
	 * @param voipSheetOBJ
	 * @return String 返回更新业务用户表语句
	 */
	public String updateOpenServUserSql(HgwServUserObj hgwServUserObj,String gw_type) {

		logger.debug("updateOpenServUserSql()");
		String tableName = "";
		if (Global.GW_TYPE_ITMS.equals(gw_type)) {
			tableName = " hgwcust_serv_info ";
		} else if (Global.GW_TYPE_BBMS.equals(gw_type)) {
			tableName = " egwcust_serv_info ";
		} else {
			tableName = " hgwcust_serv_info ";
		}

		long nowTime = System.currentTimeMillis() / 1000;
		String strSQL = "update  "+tableName+"  set "
				+ " username=?,passwd=?,wan_type=?,vpiid=?,vciid=?,"
				+ " vlanid=?,ipaddress=?,ipmask=?,gateway=?,adsl_ser=?,"
				+ " bind_port=?,open_status=?,dealdate=?,updatetime=?,opendate=?,"
				+ " serv_num=? ,"
				+ " ipv6_address_origin=?,aftr=?,ipv6_address_ip=?,"
				+ " ipv6_address_dns=?,ipv6_prefix_prefix=?";
		strSQL += "where user_id=? and serv_type_id=? and username=? and serv_status=1";

		PrepareSQL psql = new PrepareSQL(strSQL);

		//
		psql.setString(1, hgwServUserObj.getUsername());
		psql.setString(2, hgwServUserObj.getPasswd());
		psql.setInt(3, StringUtil.getIntegerValue(hgwServUserObj.getWanType()));
		psql.setString(4, StringUtil.getStringValue(hgwServUserObj.getVpiid()));
		psql.setInt(5, StringUtil.getIntegerValue(hgwServUserObj.getVciid()));
		//
		psql.setString(6, StringUtil.getStringValue(hgwServUserObj
						.getVlanid()));
		psql.setString(7, hgwServUserObj.getIpAddress());
		psql.setString(8, hgwServUserObj.getIpMask());
		psql.setString(9, hgwServUserObj.getGateway());
		psql.setString(10, hgwServUserObj.getDns());

		// 绑定端口
		psql.setString(11, hgwServUserObj.getBindPort());
		// 置为未开通
		psql.setInt(12, 0);
		// 置为工单上的时间
		psql.setLong(13, nowTime);
		psql.setLong(14, nowTime);
		psql.setLong(15, nowTime);
		// 工单上的业务的个数
		psql.setInt(16, 1);

		psql.setInt(17, StringUtil.getIntegerValue(hgwServUserObj.getAddressOrigin()));
		psql.setString(18, hgwServUserObj.getAftr());
		psql.setString(19, hgwServUserObj.getIpv6AddressIp());
		psql.setString(20, hgwServUserObj.getIpv6AddressDNS());
		psql.setString(21, hgwServUserObj.getIpv6AddressPrefix());

		psql.setLong(22, StringUtil.getLongValue(hgwServUserObj.getUserId()));
		psql.setInt(23, StringUtil.getIntegerValue(hgwServUserObj
				.getServTypeId()));
		psql.setString(24, hgwServUserObj.getUsername());
		return psql.getSQL();
	}

	/**
	 * 添加VOIP业务参数表
	 *
	 */

	public String saveVoipServParam(VoipSheetOBJ voipSheetOBJ, int sipId) {
		logger.debug("saveVoipServParam({}, {})", new Object[] { voipSheetOBJ,
				sipId });

		long nowTime = System.currentTimeMillis() / 1000;
		PrepareSQL psql = null;
		String strSQL = "insert into tab_voip_serv_param ("
				+ " protocol,user_id, line_id, voip_username, voip_passwd, sip_id, voip_phone, updatetime";

		strSQL = strSQL + ") values (?,?,?,?,?,?, ?,?)";
		psql = new PrepareSQL(strSQL);
		psql.setLong(1, voipSheetOBJ.getProtocol());
		psql.setLong(2, StringUtil.getLongValue(voipSheetOBJ.getUserId()));
		psql.setInt(3, getLineId(voipSheetOBJ.getLinePort()));
		psql.setString(4, voipSheetOBJ.getVoipUsername());
		psql.setString(5, voipSheetOBJ.getVoipPasswd());
		psql.setInt(6, sipId);
		psql.setString(7, voipSheetOBJ.getVoipTelepone());
		psql.setLong(8, nowTime);

		return psql.getSQL();
	}

	/**
	 * 更新业务参数记录
	 *
	 * @param voipSheetOBJ
	 * @param sipId
	 * @return String 更新业务参数记录Sql
	 */
	public String updateVoipServParam(VoipSheetOBJ voipSheetOBJ, int sipId) {
		logger.debug("updateVoipServParam({}, {})", voipSheetOBJ, sipId);

		long nowTime = System.currentTimeMillis() / 1000;
		String strSQL = "update tab_voip_serv_param set"
				+ " protocol=?,voip_username=?, voip_passwd=?, sip_id=?, voip_phone=?, updatetime=?";

		strSQL = strSQL + " where user_id=? and line_id=?";

		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setLong(1, voipSheetOBJ.getProtocol());
		psql.setString(2, voipSheetOBJ.getVoipUsername());
		psql.setString(3, voipSheetOBJ.getVoipPasswd());
		psql.setInt(4, sipId);
		psql.setString(5, voipSheetOBJ.getVoipTelepone());
		psql.setLong(6, nowTime);
		psql.setLong(7, StringUtil.getLongValue(voipSheetOBJ.getUserId()));
		psql.setInt(8, getLineId(voipSheetOBJ.getLinePort()));
		return psql.getSQL();
	}
	/**
	 * 新增业务用户表
	 * @param hgwServUserObj
	 * @return String 返回新增业务用户表语句
	 */
	public String saveOpenServUserSql(HgwServUserObj servUserObj ,String gw_type){
		logger.debug("saveOpenServUserSql({})", servUserObj);
		//获取当前时间
		long nowTime = new Date().getTime()/1000;

		String tableName = "";
		if (Global.GW_TYPE_ITMS.equals(gw_type)) {
			tableName = " hgwcust_serv_info ";
		} else if (Global.GW_TYPE_BBMS.equals(gw_type)) {
			tableName = " egwcust_serv_info ";
		} else {
			tableName = " hgwcust_serv_info ";
		}

		String strSQL = "insert into "+tableName+"("
				+ "user_id,serv_type_id,username,passwd,wan_type,serv_status,"
				+ "vpiid,vciid,vlanid,ipaddress,ipmask,"
				+ "gateway,adsl_ser,bind_port,dealdate,opendate,"
				+ "updatetime,open_status,ipv6_address_origin,aftr,"
				+ "ipv6_address_ip,ipv6_address_dns,ipv6_prefix_prefix) values ("
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(strSQL);

		psql.setStringExt(1, servUserObj.getUserId(), false);
		psql.setStringExt(2, servUserObj.getServTypeId(), false);
		psql.setString(3, servUserObj.getUsername());
		psql.setString(4, servUserObj.getPasswd());
		psql.setInt(5, StringUtil.getIntegerValue(servUserObj.getWanType()));
		// 业务状态，开户
		psql.setInt(6, 1);
		psql.setString(7, servUserObj.getVpiid());
		psql.setInt(8, StringUtil.getIntegerValue(servUserObj.getVciid()));
		psql.setString(9, servUserObj.getVlanid());
		psql.setString(10, servUserObj.getIpAddress());
		psql.setString(11, servUserObj.getIpMask());
		psql.setString(12, servUserObj.getGateway());
		psql.setString(13, servUserObj.getDns());
		psql.setString(14, servUserObj.getBindPort());
		psql.setLong(15, nowTime);
		// 开户时间
		psql.setLong(16, nowTime);
		psql.setLong(17, nowTime);
		psql.setInt(18, StringUtil.getIntegerValue(servUserObj.getOpenStatus()));

		psql.setInt(19, StringUtil.getIntegerValue(servUserObj.getAddressOrigin()));
		psql.setString(20, servUserObj.getAftr());
		psql.setString(21, servUserObj.getIpv6AddressIp());
		psql.setString(22, servUserObj.getIpv6AddressDNS());
		psql.setString(23, servUserObj.getIpv6AddressPrefix());

		return psql.getSQL();
	}
	/**
	 * 查询设备，用以判断设备存在性
	 * @param userId
	 * @return DeviceObj
	 */
	public DeviceObj checkDevice(int userId){
		logger.debug("checkDevice({})", userId);
		DeviceObj devObj = null;
		String strSQL = "select a.device_id,a.gather_id,a.devicetype_id,a.oui,a.device_serialnumber,a.city_id "
				+ " from tab_gw_device a, tab_hgwcustomer b"
				+ " where a.device_id=b.device_id"
				+ " and b.user_id=? and user_state='1'";

		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setLong(1, userId);
		// 查询
		Map<String,String> map = DBOperation.getRecord(psql.getSQL());
		if (map != null && false == map.isEmpty()) {
			// 设备存在
			devObj = new DeviceObj();
			devObj.setDeviceId(StringUtil.getStringValue(map, "device_id"));
			devObj.setCityId(StringUtil.getStringValue(map, "city_id"));
			devObj.setGatherId(StringUtil.getStringValue(map, "gather_id"));
			devObj.setOui(StringUtil.getStringValue(map, "oui"));
			devObj.setDevSn(StringUtil.getStringValue(map,"device_serialnumber"));
		}
		return devObj;
	}
    public List getDeviceInfo(String deviceSn,String userAccount){

    	StringBuilder sql = new StringBuilder();

		if (null != deviceSn && !"".equals(deviceSn.trim())) {
			sql.append("select device_id, device_serialnumber from tab_gw_device where 1=1 ");
			if(deviceSn.length()>5){
				sql.append(" and dev_sub_sn ='").append(deviceSn.substring(deviceSn.length()-6, deviceSn.length())).append("'");
			}
			sql.append(" and device_serialnumber like '%")
			   .append(deviceSn).append("'");
		} else {
			sql.append("select a.device_id, a.device_serialnumber from tab_gw_device a, tab_hgwcustomer b")
			   .append(" where a.device_id = b.device_id and b.username='")
			   .append(userAccount).append("'");
		}

		logger.debug("getDevAndUserObj SQL：{}", sql.toString());
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		List<Map<String, String>> list = jt.queryForList(sql.toString());

		if (null == list || list.isEmpty()) {
			return null;
		}
		return list;
    }
    public List<Map<String,Object>> getBaseInfo(String deviceSn,String userAccount){

    	logger.debug("getBaseInfo DAO：{},{}", deviceSn, userAccount);

		StringBuilder sql = new StringBuilder();

		if (null != deviceSn && !"".equals(deviceSn.trim())) {
			sql.append("select device_id, device_serialnumber from tab_gw_device where 1=1 ");
			if(deviceSn.length()>5){
				sql.append(" and dev_sub_sn ='").append(deviceSn.substring(deviceSn.length()-6, deviceSn.length())).append("'");
			}
			sql.append(" and device_serialnumber like '%")
			   .append(deviceSn).append("'");
		} else {
			sql.append("select a.device_id, a.device_serialnumber , b.adsl_hl,b.open_status ,b.user_id ,b.username")
			   .append(" from tab_gw_device a, tab_hgwcustomer b ")
			   .append(" where a.device_id = b.device_id and b.username='")
			   .append(userAccount).append("'");
		}

		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		List<Map<String,Object>> list = jt.queryForList(psql.getSQL());

		if(null != list && list.size() > 0){
			return list;
		}else {
			return null;
		}
    }
}
