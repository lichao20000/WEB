package com.linkage.module.itms.service.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

public class BssSheetByHandDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(BssSheetByHandDAO.class);

	/**
	 * 查询user_id
	 * 
	 * @param loid
	 * @return
	 */
	public List getUserIdFromTabHgwcustomer(String loid, String gwType) 
	{
		logger.debug("BssSheetByHandDAO==>getCountUserNameFromTabHgwcustomer()",loid);

		PrepareSQL psql = new PrepareSQL();
		String tabName = "tab_hgwcustomer";
		if ("2".equals(gwType)) {
			tabName = "tab_egwcustomer";
		}
		psql.append("select user_id from " + tabName + " where username = '"
				+ loid + "'");

		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 根据user_id查询用户表，业务用户表信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<HashMap<String, String>> getBssSheetInfo(String userId,String gwType) 
	{
		logger.debug("BssSheetByHandDAO==>getBssSheetInfo()", userId);

		StringBuffer sqlBuffer = new StringBuffer();
		String tabName = "tab_hgwcustomer a " +
						"left join hgwcust_serv_info b on a.user_id = b.user_id " +
						"left join tab_net_serv_param d on (b.user_id=d.user_id and b.username=d.username) ,";
		if ("2".equals(gwType)) {
			tabName = "tab_egwcustomer a " +
					"left join egwcust_serv_info b on a.user_id = b.user_id " +
					"left join tab_egw_net_serv_param d on (b.user_id=d.user_id and b.username=d.username) ,";
		}
		sqlBuffer.append("select a.username as loid,a.passwd as pwd,a.city_id,");
		sqlBuffer.append("a.adsl_hl,a.user_state,a.cust_type_id,a.user_type_id,");
		sqlBuffer.append("a.dealdate,a.open_status,d.aftr_mode,d.aftr_ip,d.vpdn,d.untreated_ip_type,");
		if ("1".equals(gwType)){
			sqlBuffer.append("a.linkman,a.linkphone,a.email,a.mobile,a.linkaddress,a.credno,a.is_pon,");//增加了字段IS_PON,e.serv_account,e.serv_pwd,e.pppoe_user,e.pppoe_pwd
		}else if ("2".equals(gwType)){
			sqlBuffer.append("a.linkman,a.linkphone,a.email,a.mobile,a.linkaddress,a.credno,a.customer_id,");
		}
		
		if(DBUtil.GetDB()==3){
			sqlBuffer.append("spec_name,serv_type_id,b.username,passwd,");
			sqlBuffer.append("vlanid,wan_type,bind_port,ipaddress,ipmask,gateway,adsl_ser ");
		}else{
			sqlBuffer.append("b.*,c.* ");
		}
		
		sqlBuffer.append(" from " + tabName + " tab_bss_dev_port c");//增加了tab_iptv_serv_param e
		sqlBuffer.append(" where 1=1 ");
		// sqlBuffer.append("   and a.user_id = b.user_id ");
		sqlBuffer.append("   and a.spec_id = c.id   ");//增加了and a.user_id =e.user_id
		sqlBuffer.append("   and a.user_id = " + userId);

		PrepareSQL psql = new PrepareSQL(sqlBuffer.toString());
		return DBOperation.getRecords(psql.getSQL());
	}

	/**
	 * 根据user_id查询用户表，业务用户表信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<HashMap<String, String>> getUserSheetInfo(String userId,String gwType) 
	{
		logger.debug("BssSheetByHandDAO==>getUserSheetInfo()", userId);

		StringBuffer sqlBuffer = new StringBuffer();
		String tabName = "tab_hgwcustomer";
		if ("2".equals(gwType)) {
			tabName = "tab_egwcustomer";
		}
		sqlBuffer.append("select a.username as loid,a.passwd as pwd,a.city_id,");
		sqlBuffer.append("a.adsl_hl,a.cust_type_id,a.user_type_id,");
		sqlBuffer.append("a.linkman,a.linkphone,a.email,a.mobile,a.linkaddress,a.linkman_credno,");
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
			//无引用的方法
		}*/
		sqlBuffer.append("c.* ");
		sqlBuffer.append("from " + tabName + " a, tab_bss_dev_port c ");
		sqlBuffer.append("where a.spec_id = c.id ");
		sqlBuffer.append("and a.user_id = " + userId);

		PrepareSQL psql = new PrepareSQL(sqlBuffer.toString());
		return DBOperation.getRecords(psql.getSQL());
	}

	/**
	 * city_id 转换
	 * 
	 * @param cityId
	 * @return
	 */
	public List<HashMap<String, String>> getCityIdExFromGwCityMap(String cityId) 
	{
		logger.debug("BssSheetByHandDAO==>getCityIdExFromGwCityMap()", cityId);
		StringBuffer sqlBuffer = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sqlBuffer.append("select city_id_ex from gw_city_map ");
		}else{
			sqlBuffer.append("select * from gw_city_map ");
		}
		sqlBuffer.append(" where city_id = '" + cityId + "'");

		PrepareSQL psql = new PrepareSQL(sqlBuffer.toString());

		return DBOperation.getRecords(psql.getSQL());
	}

	/**
	 * 根据userId 查询VOIP信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<HashMap<String, String>> getVoipinfo(String userId,String gwType) 
	{
		logger.debug("BssSheetByHandDAO==>getVoipinfo()", userId);

		StringBuffer sqlBuffer = new StringBuffer();
		String tabName = "tab_voip_serv_param";
		if ("2".equals(gwType)) {
			tabName = "tab_egw_voip_serv_param";
		}
		
		if(DBUtil.GetDB()==3){
			sqlBuffer.append("select a.line_id,a.voip_phone,a.reg_id,a.prox_serv,");
			sqlBuffer.append("a.prox_port,a.stand_prox_serv,a.stand_prox_port,a.voip_port,");
			sqlBuffer.append("a.vlanid,a.wan_type,a.ipaddress,a.ipmask,a.gateway,a.adsl_ser,");
		}else{
			sqlBuffer.append("select a.*,");
		}
		
		sqlBuffer.append("b.prox_serv,b.prox_port,b.stand_prox_serv,b.stand_prox_port ");
		sqlBuffer.append("from " + tabName + " a, tab_sip_info b ");
		sqlBuffer.append("where a.sip_id = b.sip_id ");
		sqlBuffer.append("and a.user_id = " + userId);

		PrepareSQL psql = new PrepareSQL(sqlBuffer.toString());
		return DBOperation.getRecords(psql.getSQL());

	}

	/**
	 * 删除原先的线路数据（为插入做准备）
	 * 
	 * @param userId
	 * @param line_id
	 * @return
	 */
	public int deleteLineData(String userId, String line_id_one,
			String line_id_two, String gwType) {
		StringBuffer sql = new StringBuffer();
		String tabName = "tab_voip_serv_param";
		if ("2".equals(gwType)) {
			tabName = "tab_egw_voip_serv_param";
		}
		sql.append("delete from " + tabName + " where 1=1 ");
		if ("" != line_id_one) {
			sql.append(" and line_id = " + line_id_one);
		}
		sql.append(" and user_id =").append(userId);

		PrepareSQL psql = new PrepareSQL(sql.toString());
		int num = jt.update(psql.getSQL());
		return num;
	}

	/**
	 * 入库线路参数
	 * 
	 * @param userId
	 * @param line_id
	 * @param H248PhoneNum
	 * @param H248BiaoShi
	 * @return
	 */
	public int addLineDate(String userId, String line_id, String H248PhoneNum,
			String H248BiaoShi, String gwType) {
		StringBuffer sql = new StringBuffer();
		String tabName = "tab_voip_serv_param";
		if ("2".equals(gwType)) {
			tabName = "tab_egw_voip_serv_param";
		}
		sql.append(" insert into " + tabName + "( ");
		if (!StringUtil.IsEmpty(userId)) {
			sql.append("USER_ID,");
		}
		if (!StringUtil.IsEmpty(line_id)) {
			sql.append("LINE_ID,");
		}
		if (!StringUtil.IsEmpty(H248PhoneNum)) {
			sql.append("VOIP_PHONE,");
		}
		if (!StringUtil.IsEmpty(H248BiaoShi)) {
			sql.append("VOIP_PORT");
		}
		sql.append(") values( ");

		if (!StringUtil.IsEmpty(userId)) {
			sql.append(userId).append(", ");
		}
		if (!StringUtil.IsEmpty(line_id)) {
			sql.append(line_id).append(", ");
		}
		if (!StringUtil.IsEmpty(H248PhoneNum)) {
			sql.append("'").append(H248PhoneNum).append("',");
		}
		if (!StringUtil.IsEmpty(H248BiaoShi)) {
			sql.append("'").append(H248BiaoShi).append("'");
		}
		sql.append(")");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int num = jt.update(psql.getSQL());
		return num;
	}

	public List<Map<String, String>> getDeviceTypes(String gwType) {
		String specSql = "select spec_name as id,spec_name,voice_num from tab_bss_dev_port where gw_type = '"
				+ gwType + "'";
		List<Map<String, String>> specList = new ArrayList<Map<String, String>>();
		PrepareSQL specPsql = new PrepareSQL(specSql.toString());
		specList = jt.queryForList(specPsql.getSQL());
		return specList;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getUserInfo(String username, String gw_type) {
		logger.debug("getUserInfo({})", username);
		StringBuffer sql = new StringBuffer();
		if (gw_type.equals("1")) {
			sql.append("select access_style_id from tab_hgwcustomer " +
					"where (user_state='1' or user_state='2') and username='")
					.append(username).append("'");
		} else if (gw_type.equals("2")) {
			sql.append("select access_style_id from tab_egwcustomer " +
					"where (user_state='1' or user_state='2') and username='")
					.append(username).append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map<String, String>> list = this.jt.query(psql.getSQL(),
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						map.put("orderType", rs.getString("access_style_id"));
						return map;
					}
				});
		return list;
	}
	public ArrayList<HashMap<String, String>> queryWanTypeList(String loid) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.wan_type,a.username from hgwcust_serv_info a ,tab_hgwcustomer b where a.user_id = b.user_id  and b.username = ? " +
				" and a.serv_type_id = 10 order by b.updatetime desc ");
		psql.setString(1,loid);
		ArrayList<HashMap<String, String>> list = DBOperation.getRecords(psql.getSQL());
		if(null != list && list.size() > 0)
		{
			return list;
		}
		return null;
	}

	public void recordRouteAndBridge(String loid, String netAccount, String operAction, String operOrigon, String result, String account, String resultDesc) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("insert into bridge_route_oper_log " +
				" (loid,username,oper_action,oper_origon,oper_staff," +
				"  add_time,oper_result,result_desc) " +
				" values(?,?,?,?,?" +
				"       ,?,?,?)");
		psql.setString(1,loid);
		psql.setString(2,netAccount);
		psql.setString(3,operAction);  //1 桥接  路由改桥   2路由 桥改路由
		psql.setString(4,operOrigon);
		psql.setString(5,account);

		psql.setLong(6,System.currentTimeMillis()/1000);
		psql.setString(7,result);
		psql.setString(8,resultDesc);

		DBOperation.executeUpdate(psql.getSQL());
	}
}
