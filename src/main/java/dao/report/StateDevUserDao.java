package dao.report;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author Jason(3412)
 * @date 2008-11-12
 */
public class StateDevUserDao {

	Logger logger = LoggerFactory.getLogger(StateDevUserDao.class);
	
	private JdbcTemplate jt;

	/**
	 * 获取设备按属地统计的结果集list<map<city_id,devNum>>
	 * 
	 * @param citys
	 *            'city_id','city_id'...
	 * @author Jason(3412)
	 * @date 2008-11-13
	 * @return List
	 */
	public int getCountDevByCity(long startTime, long endTime,
			String paramCityId) {
		//获取自身及下面所有属地的ID
		List list = CityDAO.getAllNextCityIdsByCityPid(paramCityId);
		String allCityIds = StringUtils.weave(list);
		list = null;
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select count(device_id) devNum "
				+ " from tab_gw_device where cpe_allocatedstatus=1 ");
		strSQL.append(" and city_id in (" + allCityIds + ")");
		if (startTime != 0) {
			strSQL.append(" and complete_time >= ");
			strSQL.append(startTime);
		}
		if (startTime != 0) {
			strSQL.append(" and complete_time <= ");
			strSQL.append(endTime);
		}
		PrepareSQL psql = new PrepareSQL(strSQL.toString());
		psql.getSQL();
		return jt.queryForInt(strSQL.toString());
	}

	/**
	 * 根据属地查询该属地满足条件的用户
	 * 
	 * @param cityId
	 * @param userTime
	 * @param deviceTime
	 * @return
	 */
	public int getDeviceCountByYN(long startTime,long endTime,String cityId){
		PrepareSQL ppSQL = new PrepareSQL();
		ppSQL.setSQL("select count(1) from tab_gw_device a ");
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			ppSQL.setSQL("select count(*) from tab_gw_device a ");
		}
		if(2==LipossGlobals.SystemType()){
			ppSQL.append(" ,tab_egwcustomer b ");
		}else{
			ppSQL.append(" ,tab_hgwcustomer b ");
		}
		ppSQL.append(" where cpe_allocatedstatus=1 and a.device_id=b.device_id and b.user_state in ('1','2') ");
		if (startTime != 0) {
			ppSQL.append(" and b.opendate >= "+startTime);
		}
		if (startTime != 0) {
			ppSQL.append(" and b.opendate <= "+endTime);
		}
		
		if(!"00".equals(cityId)){
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			ppSQL.append(PrepareSQL.AND,"a.city_id",list);
			list = null;
		}
		PrepareSQL psql = new PrepareSQL(ppSQL.toString());
		psql.getSQL();
		return jt.queryForInt(ppSQL.toString());
	}
	
	/**
	 * 根据属地查询该属地满足条件的用户
	 * 
	 * @param cityId
	 * @param userTime
	 * @param deviceTime
	 * @return
	 */
	public int getDeviceCount(long startTime,long endTime,String cityId){
		PrepareSQL ppSQL = new PrepareSQL();
		ppSQL.setSQL("select count(1) from tab_gw_device a ");
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			ppSQL.setSQL("select count(*) from tab_gw_device a ");
		}
		if(2==LipossGlobals.SystemType()){
			ppSQL.append(" ,tab_egwcustomer b ");
		}else{
			ppSQL.append(" ,tab_hgwcustomer b ");
		}
		ppSQL.append(" where a.device_id=b.device_id and b.user_state in ('1','2') ");
		if (startTime != 0) {
			ppSQL.append(" and b.opendate >= "+startTime);
		}
		if (startTime != 0) {
			ppSQL.append(" and b.opendate <= "+endTime);
			endTime = endTime + 8*24*3600;
			ppSQL.append(" and b.binddate<="+endTime);
		}
		
		if(!"00".equals(cityId)){
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			ppSQL.append(PrepareSQL.AND,"a.city_id",list);
			list = null;
		}
		PrepareSQL psql = new PrepareSQL(ppSQL.toString());
		psql.getSQL();
		return jt.queryForInt(ppSQL.toString());
	}
	
	/**
	 * 获取用户按属地统计的结果集list<map<city_id,userNum>>
	 * 
	 * @param gw_type：网关类型，企业网关2和家庭网关1所需要查的表不一样;
	 *            citys：'city_id','city_id'...
	 * @author Jason(3412)
	 * @date 2008-11-13
	 * @return List
	 */
	public int getCountUserByCity(String gw_type, long startTime,
			long endTime, String paramCityId) {
		//获取自身及下面所有属地的ID
		List list = CityDAO.getAllNextCityIdsByCityPid(paramCityId);
		String allCityIds = StringUtils.weave(list);
		list = null;
		StringBuffer strSQL = new StringBuffer();
		if ("2".equals(gw_type)) {
			strSQL.append("select count(g.username) userNum from tab_egwcustomer g, tab_customerinfo c where user_state in ('1','2') and c.customer_id=g.customer_id");
			if (startTime != 0) {
				strSQL.append(" and g.opendate >= ");
				strSQL.append(startTime);
			}

			if (startTime != 0) {
				strSQL.append(" and g.opendate <= ");
				strSQL.append(endTime);
			}
		} else {
			strSQL.append("select count(c.username) userNum from tab_hgwcustomer c where user_state in ('1','2') ");
			if (startTime != 0) {
				strSQL.append(" and c.opendate >= ");
				strSQL.append(startTime);
			}

			if (startTime != 0) {
				strSQL.append(" and c.opendate <= ");
				strSQL.append(endTime);
			}
		}

		strSQL.append(" and c.city_id in (" + allCityIds + ")");
		PrepareSQL psql = new PrepareSQL(strSQL.toString());
		psql.getSQL();
		return jt.queryForInt(strSQL.toString());
	}

	/**
	 * 商务领航查询用户
	 * 
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List getBBMSStateUser(String cityId,String startTime,String endTime,String accessType){
		StringBuffer sql = new StringBuffer();
		
		sql.append("select a.user_id,a.username,a.passwd,a.adsl_ser,a.bandwidth,a.ipaddress,");
		sql.append("a.ipmask,a.gateway,a.vlanid,a.workid,a.user_state,a.opendate,a.onlinedate,");
		sql.append("a.pausedate,a.closedate,a.updatetime,a.binddate,a.staff_id,a.remark,");
		sql.append("a.phonenumber,a.vpiid,a.vciid,a.adsl_hl,a.userline,a.dealdate,");
		sql.append("a.opmode,a.maxattdnrate,a.upwidth,a.oui,a.device_serialnumber,a.device_id,");
		sql.append("a.serv_type_id,a.max_user_number,a.wan_value_1,a.wan_value_2,a.open_status,");
		sql.append("a.customer_id,a.wan_type,a.lan_num,a.ssid_num,a.work_model,a.access_style_id,");
		sql.append("a.device_ip,a.device_shelf,a.device_frame,a.device_slot,a.device_port,");
		sql.append("a.bind_port,a.flag_pvc,a.stat_bind_enab from tab_egwcustomer a,");
		sql.append("tab_customerinfo b where a.customer_id=b.customer_id ");
		
		// 用户检索的情况
		ArrayList list = new ArrayList();
		list.clear();

		sql.append(" and a.user_state in ('1','2')");
		
		//判断是否为查询提交，如果是则 加入查询字段条件
		if (startTime != null && !startTime.equals("")) {
			sql.append(" and a.opendate >= ");
			sql.append(startTime);
		}
		
		if (endTime != null && !endTime.equals("")) {
			sql.append(" and a.opendate <= ");
			sql.append(endTime);
		}
		if("ADSL".equals(accessType)){
			sql.append(" and a.access_style_id in (1,6)");
		} else if("LAN".equals(accessType)){
			sql.append(" and a.access_style_id in (2,4)");
		} else if("EPON".equals(accessType)){
			sql.append(" and a.access_style_id in (3,5)");
		}
		
		if (cityId == null && "".equals(cityId)) {
			cityId = "00";
		}
		
		if(!"00".equals(cityId)){
			List clist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			String allCityIds = StringUtils.weave(clist);
			sql.append(" and city_id in (");
			sql.append(allCityIds);
			sql.append(")");
			clist = null;
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.queryForList(sql.toString());
	}
	
	
	/**
	 * 家庭网关查询用户
	 * 
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List getITMSStateUser(String cityId,String startTime,String endTime){
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select user_id, gather_id,user_state,username,cotno,cust_type_id," +
				"opmode,user_type_id,realname,phonenumber,device_serialnumber,device_id,dealdate," +
				"opendate, updatetime,serv_type_id,wan_type,oui,onlinedate,city_id FROM tab_hgwcustomer ");

		// 用户检索的情况
		ArrayList list = new ArrayList();
		list.clear();

		sql.append(" where (user_state = '1' or user_state = '2')");
		// 判断是否为查询提交，如果是则 加入查询字段条件

		if (startTime != null && !startTime.equals("")) {

			sql.append(" and opendate >= " + startTime);

		}
		if (endTime != null && !endTime.equals("")) {

			sql.append(" and opendate <= " + endTime);
		}

		if (cityId == null && "".equals(cityId)) {
			cityId = "00";
		}
		
		if(!"00".equals(cityId)){
			List clist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			String allCityIds = StringUtils.weave(clist);
			sql.append(" and city_id in (" + allCityIds + ")");
			clist = null;
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.queryForList(sql.toString());
	}
	
	/**
	 * 获取属地的下级属地city_id的list
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2008-11-13
	 * @return List
	 */
	public List getNextCityList(String paramCityId) {
		if (paramCityId == null || "".equals(paramCityId)) {
			paramCityId = "00";
		}
		return CityDAO.getNextCityListByCityPidCore(paramCityId);
	}

	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}
	
	
	/**
	 * 获取ADSL或者LAN用户按属地统计的结果集list<map<city_id,userNum>>
	 * 
	 * @param gw_type：网关类型，企业网关2和家庭网关1所需要查的表不一样;
	 *            citys：'city_id','city_id'...
	 *        accessType: 'ADSL', 'LAN'
	 * @author Jason(3412)
	 * @date 2008-11-13
	 * @return List
	 */
	public int getCountAccessTypeUserByCity(String gw_type,long startTime,
			long endTime, String paramCityId, String accessType) {
		//获取自身及下面所有属地的ID
		String allCityIds = StringUtils.weave(CityDAO.getAllNextCityIdsByCityPid(paramCityId));
		StringBuffer strSQL = new StringBuffer();
		if ("2".equals(gw_type)) {
			strSQL.append("select count(g.username) userNum from tab_egwcustomer g, tab_customerinfo c where user_state in ('1','2') and c.customer_id=g.customer_id");
			if (startTime != 0) {
				strSQL.append(" and g.opendate >= ");
				strSQL.append(startTime);
			}

			if (startTime != 0) {
				strSQL.append(" and g.opendate <= ");
				strSQL.append(endTime);
			}

			strSQL.append(" and c.city_id in (" + allCityIds + ")");
			
			if("ADSL".equals(accessType)){
				strSQL.append(" and g.access_style_id in (1,6)");
			} else if("LAN".equals(accessType)){
				strSQL.append(" and g.access_style_id in (2,4)");
			} else if("EPON".equals(accessType)){
				strSQL.append(" and g.access_style_id in (3,5)");
			}
		}else{
			strSQL.append("select count(g.username) userNum from tab_hgwcustomer g where g.user_state in ('1','2') ");
			if (startTime != 0) {
				strSQL.append(" and g.opendate >= ");
				strSQL.append(startTime);
			}

			if (startTime != 0) {
				strSQL.append(" and g.opendate <= ");
				strSQL.append(endTime);
			}

			strSQL.append(" and g.city_id in (" + allCityIds + ")");
			
			if("ADSL".equals(accessType)){
				strSQL.append(" and g.access_style_id in (1,6)");
			} else if("LAN".equals(accessType)){
				strSQL.append(" and g.access_style_id in (2,4)");
			} else if("EPON".equals(accessType)){
				strSQL.append(" and g.access_style_id in (3,5)");
			}
		}
		PrepareSQL psql = new PrepareSQL(strSQL.toString());
		psql.getSQL();
		return jt.queryForInt(strSQL.toString());
	}
}
