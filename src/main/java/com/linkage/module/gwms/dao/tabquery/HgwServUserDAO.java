package com.linkage.module.gwms.dao.tabquery;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2009-7-6
 */
public class HgwServUserDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(HgwServUserDAO.class);

	private Map<String, String> bindPortMap = new HashMap<String, String>();

	private String gw_type;

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
	
	
	public int queryUndoNum() 
	{
		logger.debug("queryUndoNum()");
		
		PrepareSQL psql = new PrepareSQL("");
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		psql.append("from gw_serv_strategy_serv ");
		psql.append("where status=0 and type>0 and exec_count<=3 and service_id<>5 ");
		return jt.queryForInt(psql.getSQL());
	}
	
	public int queryMulticastNum(long time) 
	{
		logger.debug("queryMulticastNum()");
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		psql.append("from tab_setmulticast_dev where settime>"+time);
		return jt.queryForInt(psql.getSQL());
	}
	
	/**
	 * 获得业务用户相关信息
	 * @author gongsj
	 * @date 2009-8-13
	 * @param userId
	 * @return
	 */
	public HgwServUserObj getUserInfo(String userId, String servTypeId) 
	{
		logger.debug("getUserInfo({},{})", userId, servTypeId);
		String bindPortStr = null;
		String[] bindPortArr = null;
		String bindPort = "";
		//chensq5 修改bug单:JSDX_ITMS-BUG-20151027-WJY-002
		//去掉InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.1.最后的点.

		bindPortMap.put("InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.1", "LAN1");
		bindPortMap.put("InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2", "LAN2");
		bindPortMap.put("InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.3", "LAN3");
		bindPortMap.put("InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.4", "LAN4");
		bindPortMap.put("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1", "WLAN1");
		bindPortMap.put("InternetGatewayDevice.LANDevice.1.WLANConfiguration.2", "WLAN2");
		bindPortMap.put("InternetGatewayDevice.LANDevice.1.WLANConfiguration.3", "WLAN3");
		bindPortMap.put("InternetGatewayDevice.LANDevice.1.WLANConfiguration.4", "WLAN4");

		String tableName = "hgwcust_serv_info";
		if (Global.GW_TYPE_BBMS.equals(gw_type)) {
			tableName = " egwcust_serv_info ";
		}

		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select username,passwd,wan_type,vpiid,vciid,vlanid,");
			psql.append("ipaddress,ipmask,gateway,adsl_ser,bind_port from ");
		}else{
			psql.append("select * from ");
		}
		psql.append(tableName+" where user_id=? and serv_type_id=? ");
		psql.setStringExt(1, userId, false);
		psql.setStringExt(2, servTypeId, false);

		logger.debug("getUserInfo SQL:{}", psql.getSQL());

		Map rMap = DataSetBean.getRecord(psql.getSQL());
		HgwServUserObj servUserObj = null;
		if (null != rMap && false == rMap.isEmpty()) {
			servUserObj = new HgwServUserObj();
			servUserObj.setUserId(userId);
			servUserObj.setServTypeId(servTypeId);
			servUserObj.setUsername(String.valueOf(rMap.get("username")));
			servUserObj.setPasswd(String.valueOf(rMap.get("passwd")));
			servUserObj.setWanType(String.valueOf(rMap.get("wan_type")));
			servUserObj.setVpiid(String.valueOf(rMap.get("vpiid")));
			servUserObj.setVciid(String.valueOf(rMap.get("vciid")));
			servUserObj.setVlanid(String.valueOf(rMap.get("vlanid")));

			servUserObj.setIpAddress(String.valueOf(rMap.get("ipaddress")));
			servUserObj.setIpMask(String.valueOf(rMap.get("ipmask")));
			servUserObj.setGateway(String.valueOf(rMap.get("gateway")));
			servUserObj.setDns(String.valueOf(rMap.get("adsl_ser")));

			bindPortStr = String.valueOf(rMap.get("bind_port"));
			if (null != bindPortStr) {
				bindPortArr = bindPortStr.split(",");

				for (int i = 0; i < bindPortArr.length; i++) {
					bindPort += bindPortMap.get(bindPortArr[i]);
					if (i != bindPortArr.length - 1) {
						bindPort += ",";
					}
				}

				servUserObj.setBindPort(bindPort);
			}
		}

		return servUserObj;
	}

	/**
	 * 更新业务用户表
	 * @author gongsj
	 * @date 2009-8-14
	 * @param username
	 * @param password
	 * @param servType
	 * @param wanType
	 * @param vpi
	 * @param vci
	 * @param vlanid
	 * @param ip
	 * @param mask
	 * @param gateway
	 * @param dns
	 * @param bindport
	 * @return
	 */
	public boolean editBussInfo(String userId, String username, String password, 
			String servType, String wanType, String vpi, String vci, String vlanid, 
			String ip, String mask, String gateway, String dns, String bindport) 
	{
		StringBuilder editBussSql = new StringBuilder();

		String tableName = "hgwcust_serv_info";
		if (Global.GW_TYPE_BBMS.equals(gw_type)) {
			tableName = " egwcust_serv_info ";
		} 

		editBussSql.append("update "+tableName+" set passwd='").append(password)
		.append("',wan_type=").append(wanType).append(",vpiid='").append(vpi).append("',vciid=").append(vci)
		.append(",vlanid='").append(vlanid).append("',ipaddress='").append(ip).append("',ipmask='").append(mask)
		.append("',gateway='").append(gateway).append("',adsl_ser='").append(dns).append("',bind_port='").append(bindport)
		.append("' where user_id=").append(userId).append(" and serv_type_id=").append(servType);

		PrepareSQL psql = new PrepareSQL(editBussSql.toString());
		psql.getSQL();

		int result = DataSetBean.executeUpdate(editBussSql.toString());
		logger.debug("更新hgwcust_serv_info结果：{}", result);
		if (result >= 0) {
			logger.debug("更新hgwcust_serv_info成功！");
			return true;
		} else {
			logger.debug("更新hgwcust_serv_info失败！");
			return false;
		}
	}

	/**
	 * 更新业务用户表部分字段
	 * @param cellMap  key:字段名  value:字段值
	 * @return
	 */
	public boolean editPartBussInfo(Map<String,String> cellMap,String userId, String servType){

		if(cellMap == null || cellMap.size() == 0){
			logger.warn("参数为空，更新hgwcust_serv_info失败！");
			return false;
		}

		StringBuilder editBussSql = new StringBuilder();		
		String tableName = "hgwcust_serv_info";
		if (Global.GW_TYPE_BBMS.equals(gw_type)) {
			tableName = " egwcust_serv_info ";
		}
		editBussSql.append("update "+tableName+" set ");
		int mapIndex = 1;
		for(Map.Entry<String, String> entry : cellMap.entrySet()){
			if(mapIndex == cellMap.size()){
				editBussSql.append(entry.getKey()+"='"+entry.getValue()+"'");
				break;
			}
			editBussSql.append(entry.getKey()+"='"+entry.getValue()+"',");
			mapIndex++;
		}

		editBussSql.append(" where user_id=").append(userId).append(" and serv_type_id=").append(servType);

		PrepareSQL psql = new PrepareSQL(editBussSql.toString());
		int result = DataSetBean.executeUpdate(psql.getSQL());
		logger.debug("更新hgwcust_serv_info结果：{}", result);		
		if (result < 0) {
			return false;
		}
		return  true;
	}

	/**
	 * 更新开户状态的业务用户的信息,业务用户本身处于开户状态
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-10
	 * @return String
	 */
	public int updateServUser(HgwServUserObj servUserObj) {
		logger.debug("updateServUser({})", servUserObj);
		long nowTime = new Date().getTime()/1000;

		String tableName = "hgwcust_serv_info";
		if (Global.GW_TYPE_BBMS.equals(gw_type)) {
			tableName = " egwcust_serv_info ";
		}

		String strSQL = "update "+tableName+" set username=?,"
				+ " passwd=?,wan_type=?,vpiid=?,vciid=?,vlanid=?,"
				+ " ipaddress=?,ipmask=?,gateway=?,adsl_ser=?,bind_port=?,"
				+ "updatetime=?,open_status=?"
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
		psql.setInt(14, StringUtil.getIntegerValue(servUserObj.getUserId()));
		psql.setInt(15, StringUtil.getIntegerValue(servUserObj.getServTypeId()));

		return jt.update(psql.getSQL());
	}

	/**
	 * 入家庭网关业务用户信息表SQL, 更新信息来源于工单数据,(不能来源于用户表)
	 * 
	 * @param
	 * @author Jason
	 * @date 2008-8-26
	 * @return insert业务用户表语句
	 */
	public int saveServUser(HgwServUserObj servUserObj) {
		logger.debug("saveOpenServUserSql({})", servUserObj);
		long nowTime = new Date().getTime()/1000;

		String tableName = "hgwcust_serv_info";
		if (Global.GW_TYPE_BBMS.equals(gw_type)) {
			tableName = " egwcust_serv_info ";
		}

		String strSQL = "insert into "+tableName+"("
				+ "user_id,serv_type_id,username,passwd,wan_type,serv_status,"
				+ "vpiid,vciid,vlanid,ipaddress,ipmask,"
				+ "gateway,adsl_ser,bind_port,dealdate,opendate,"
				+ "updatetime,open_status) values ("
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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

		return jt.update(psql.getSQL());
	}


	/**
	 * 根据用户ID获取对应的业务信息数组
	 * 
	 * @param userId:用户ID
	 * @param servTypeId 业务类型(采用+的方式拼装SQL)
	 * @author Jason(3412)
	 * @date 2010-6-11
	 * @return HgwServUserObj[] 业务用户数组，未查询到结果则返回null
	 */
	public HgwServUserObj[] queryHgwcustServUserByDevId(long userId){
		logger.debug("queryHgwcustServUserByDevId({})", userId);

		HgwServUserObj[] arrHgwServUserObj = null;

		String tableName = "hgwcust_serv_info";
		if (Global.GW_TYPE_BBMS.equals(gw_type)) {
			tableName = " egwcust_serv_info ";
		}

		String strSQL = "select b.user_id, b.serv_type_id, b.username, b.open_status"
				+ " from "+tableName+" b "
				+ " where b.serv_status=1 and b.user_id=?";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setLong(1, userId);
		//执行查询
		List resList = jt.queryForList(psql.getSQL());
		//查询结果非空
		if(null != resList && false == resList.isEmpty()){
			int size = resList.size();
			arrHgwServUserObj = new HgwServUserObj[size];
			for(int i = 0; i < size; i++){
				arrHgwServUserObj[i] = new HgwServUserObj();
				Map rMap = (Map) resList.get(i);
				arrHgwServUserObj[i].setUserId(StringUtil.getStringValue(rMap.get("user_id")));
				arrHgwServUserObj[i].setServTypeId(StringUtil.getStringValue(rMap.get("serv_type_id")));
				arrHgwServUserObj[i].setUsername(String.valueOf(rMap.get("username")));
				arrHgwServUserObj[i].setOpenStatus(String.valueOf(rMap.get("open_status")));
			}
		}

		return arrHgwServUserObj;
	}


	/**
	 * 更新用户的业务开通状态
	 * 
	 * @param userId:用户ID； servTypeId:业务类型ID
	 * @author Jason(3412)
	 * @date 2010-6-11
	 * @return int 1:成功
	 */
	public int updateServOpenStatus(long userId, int servTypeId){
		logger.debug("updateServOpenStatus({}, {})", userId, servTypeId);

		String tableName = "hgwcust_serv_info";
		if (Global.GW_TYPE_BBMS.equals(gw_type)) {
			tableName = " egwcust_serv_info ";
		}

		//更新SQL语句
		String strSQL = "update "+tableName+" set open_status=0 "
				+ " where serv_status=1 and user_id=? ";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setLong(1, userId);
		if(0 != servTypeId){
			psql.append(" and serv_type_id=" + servTypeId);
		}

		return jt.update(psql.getSQL());
	}
}
