package dao.netcutover;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DbUtils;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.CommonMap;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.resource.FileSevice;
import com.linkage.litms.system.User;
import com.linkage.module.gwms.Global;

/**
 * 
 * @author benny
 * @since 2007-10-01
 * @version1.0
 */
public class HandSheetDao {

	private static Logger log = LoggerFactory.getLogger(HandSheetDao.class);
	
	// 数据源
	@SuppressWarnings("unused")
	private DataSource dao;

	// spring的jdbc模版类
	private JdbcTemplate jt;

	// 访问数据库返回List
	private List<Map> list;


	/**
	 * 根据用户帐号获取用户和设备信息
	 * 
	 * @author lizj （5202）
	 * @param user_name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getUserInfo(String user_name) {
		String sql = "select a.username, a.passwd, a.vpiid, a.vciid, a.wan_value_1, a.wan_value_2, b.device_id, b.gather_id ,b.devicetype_id,b.oui,b.device_serialnumber,a.e_id,a.e_username,a.e_passwd from tab_egwcustomer a,tab_gw_device b  where a.username='"
				+ user_name
				+ "' and a.user_state in ('1','2') and b.device_status=1 and  a.device_id = b.device_id and b.gw_type=2";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		list = new ArrayList();
		list = jt.queryForList(sql);
		return list;

	}

	/**
	 * 工单入库，调用corba接口发送工单，返回工单ID
	 * 
	 * @author lizj （5202）
	 * @param user
	 * @param device_id
	 * @param service_id
	 * @param devicetype_id
	 * @param gather_id
	 * @param param
	 * @return
	 */

	/**
	 * 根据采集点获取ior信息
	 * 
	 * @param gather_id
	 * @param user
	 * @return
	 */
	public String getIor(String gather_id, User user) {

		return user.getIor("ACS_" + gather_id, "ACS_Poa_" + gather_id);
	}

	/**
	 * 组装工单参数数组
	 * 
	 * @author lizj （5202）
	 * @param sheet_id
	 * @return
	 */
	public String[] setParam(String vpiid, String vciid,
			String connection_type, String[] lanInterface, String service_list,
			String ssid1_username, String ssid1_passwd, String ssid2_username,
			String ssid2_passwd, String[] lanInterface1,
			String boss_control) {
		String[] param = new String[9];
		
		String _LanInterface = null;
		// WANConnection绑定的端口
		if(lanInterface != null && lanInterface.length > 0){
			for (int i = 0; i < lanInterface.length; i++) {
				if (i == 0) {
					_LanInterface = lanInterface[i];
				} else {
					_LanInterface += "," + lanInterface[i];
				}
			}
		} else {
			_LanInterface = "disable";
		}

		String _LanInterface1 = null;

		// VLAN1绑定的端口
		if(lanInterface1 != null && lanInterface1.length > 0){
			for (int i = 0; i < lanInterface1.length; i++) {
				if (i == 0) {
					_LanInterface1 = lanInterface1[i];
				} else {
					_LanInterface1 += "," + lanInterface1[i];
				}
			}
		}

		log.debug("_LanInterface1 :" + _LanInterface1);

		// 参数数组
//		param[0] = "PVC:" + vpiid + "/" + vciid;
//		param[1] = connection_type;
//		param[2] = _LanInterface;
//		param[3] = service_list;
//		param[4] = ssid1_username;
//		param[5] = ssid1_passwd;
//		param[6] = ssid2_username;
//		param[7] = ssid2_passwd;
//		param[8] = _LanInterface1;
//		param[9] = _LanInterface2;
//		param[10] = boss_control;
		
		
		param[0] = ssid1_username;
//		param[1] = StringToAssic.toAssic(ssid1_passwd);
		param[1] = ssid1_passwd;
		param[2] = ssid2_username;
//		param[3] = StringToAssic.toAssic(ssid2_passwd);
		param[3] = ssid2_passwd;
		param[4] = "PVC:" + vpiid + "/" + vciid;
		param[5] = _LanInterface;
		param[6] = service_list;		
		param[7] = _LanInterface1;		
		param[8] = boss_control;
		log.debug("param :" + param);
		return param;

	}
	
	/**
	 * 组装工单参数数组
	 * 
	 * @author lizj （5202）
	 * @param sheet_id
	 * @return
	 */
	public String[] setIptvParam(String vpiid, String vciid,String[] lanInterface, String service_list) {
		String[] param = new String[3];
		
		String _LanInterface = null;
		// WANConnection绑定的端口
		if(lanInterface != null && lanInterface.length > 0){
			for (int i = 0; i < lanInterface.length; i++) {
				if (i == 0) {
					_LanInterface = lanInterface[i];
				} else {
					_LanInterface += "," + lanInterface[i];
				}
			}
		}

		log.debug("PVC:" + vpiid + "/" + vciid);
		log.debug("_LanInterface :" + _LanInterface);
		log.debug("service_list :" + service_list);

		// 参数数组
		param[0] = "PVC:" + vpiid + "/" + vciid;
		param[1] = _LanInterface;
		param[2] = service_list;
//		param[4] = ssid1_username;
//		param[5] = ssid1_passwd;
//		param[6] = ssid2_username;
//		param[7] = ssid2_passwd;
//		param[8] = _LanInterface1;
//		param[9] = _LanInterface2;
//		param[10] = boss_control;
		
		
//		param[0] = ssid1_username;
////		param[1] = StringToAssic.toAssic(ssid1_passwd);
//		param[1] = ssid1_passwd;
//		param[2] = ssid2_username;
////		param[3] = StringToAssic.toAssic(ssid2_passwd);
//		param[3] = ssid2_passwd;
//		param[4] = "PVC:" + vpiid + "/" + vciid;
//		param[5] = _LanInterface;
//		param[6] = service_list;		
//		param[7] = _LanInterface2;		
//		param[8] = boss_control;

		return param;

	}
	
	

	/**
	 * 工单报告表返回工单执行状态信息
	 * 
	 * @param sheet_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getSheet(String sheet_id) {

		list = new ArrayList();

		String sql = "select * from tab_sheet_report where sheet_id ='" + sheet_id + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select sheet_id, gather_id, city_id, device_id, username, service_id, exec_status, exec_desc," +
					" exec_count, fault_code, fault_desc, receive_time, start_time, end_time " +
					" from tab_sheet_report where sheet_id ='" + sheet_id + "'";
		}

		PrepareSQL psql = new PrepareSQL(sql);
		jt.query(psql.getSQL(), new RowCallbackHandler() {
			@SuppressWarnings("unchecked")
			public void processRow(ResultSet rs) throws SQLException {
				Map gatherMap = CommonMap.getGatherMap();
				FileSevice fileSevice = new FileSevice();
				Map serviceMap = fileSevice.getServiceMap();
				HashMap map = new HashMap();
				map.put("sheet_id", rs.getString("sheet_id"));
				map.put("gather_id", rs.getString("gather_id"));
				map.put("city_id", rs.getString("city_id"));
				map.put("device_id", rs.getString("device_id"));
				map.put("username", rs.getString("username"));
				map.put("service_id", rs.getInt("service_id"));
				map.put("exec_status", rs.getInt("exec_status"));
				map.put("exec_desc", rs.getString("exec_desc"));
				map.put("exec_count", rs.getInt("exec_count"));
				map.put("fault_code", rs.getString("fault_code"));
				map.put("fault_desc", rs.getString("fault_desc"));
				map.put("receive_time", rs.getInt("receive_time"));
				map.put("start_time", rs.getInt("start_time"));
				map.put("end_time", rs.getString("end_time"));
				
				// 业务描述
				map.put("service_id_desc", (String) serviceMap.get(String
						.valueOf(rs.getInt("service_id"))));
				// 采集点描述
				map.put("gather_id_desc", gatherMap.get(rs
						.getString("gather_id")));
				// 格式化开始和结束时间
				map.put("start_time_desc", StringUtils.formatDate(
						"yyyy-MM-dd HH:mm:ss", rs.getInt("start_time")));
				if(rs.getString("end_time") == null || rs.getString("end_time").equals("")){
					map.put("end_time_desc"," ");
				}else {
					map.put("end_time_desc", StringUtils.formatDate(
							"yyyy-MM-dd HH:mm:ss", rs.getInt("end_time")));
				}

				list.add(map);
			}
		});
		return list;
	}

	/**
	 * 路由开户需要的工单参数数组
	 * @param vpiid
	 * @param vciid
	 * @param connection_type
	 * @param username
	 * @param passwd
	 * @param lanInterface
	 * @param service_list
	 * @param ssid1_username
	 * @param ssid1_passwd
	 * @param ssid2_username
	 * @param ssid2_passwd
	 * @param lanInterface1
	 * @param lanInterface2
	 * @param boss_control
	 * @return
	 */
	public String[] setParamRoute(String vpiid, String vciid,
			String connection_type,String username,String passwd,String[] lanInterface, String service_list,
			String ssid1_username, String ssid1_passwd, String ssid2_username,
			String ssid2_passwd, String[] lanInterface1, String boss_control){
		
		
		String[] param = new String[10];
		String _LanInterface = "";
		// WANConnection绑定的端口
		
		if(lanInterface != null && lanInterface.length > 0){
			for (int i = 0; i < lanInterface.length; i++) {
				if (i == 0) {
					_LanInterface = lanInterface[i];
				} else {
					_LanInterface += "," + lanInterface[i];
				}
			}
		} else {
			_LanInterface = "disable";
		}
		log.debug("_LanInterface :" + _LanInterface);

		// VLAN2绑定的端口
		String _LanInterface1 = "";
		if(lanInterface1 != null){
			for (int i = 0; i < lanInterface1.length; i++) {
				if (i == 0) {
					_LanInterface1 = lanInterface1[i];
				} else {
					_LanInterface1 += "," + lanInterface1[i];
				}
			}
		}

		log.debug("_LanInterface2 :" + _LanInterface1);

		// 参数数组
//		param[0] = "PVC:" + vpiid + "/" + vciid;
//		param[1] = connection_type;
//		param[2] = username;
//		param[3] = passwd;	
//		param[4] = _LanInterface;
//		param[5] = service_list;
//		param[6] = ssid1_username;
//		param[7] = ssid1_passwd;
//		param[8] = ssid2_username;
//		param[9] = ssid2_passwd;
//		param[10] = _LanInterface1;
//		param[11] = _LanInterface2;
//		param[12] = boss_control;
		
		param[0] = ssid1_username;
		param[1] = ssid1_passwd;
		param[2] = ssid2_username;
		param[3] = ssid2_passwd;
		param[4] = "PVC:" + vpiid + "/" + vciid;
		param[5] = username;
		param[6] = passwd;				
//		param[7] = _LanInterface;
		param[7] = service_list;		
		param[8] = _LanInterface1;		
		param[9] = boss_control;
		log.debug("param :" + param);
		return param;
		
	}
	/**
	 * 设置接入安全的参数数组
	 * @param firewall
	 * @param wall_level
	 * @param uiface
	 * @param allawip
	 * @param allowport
	 * @param attack
	 * @return
	 */
	
	public String[] setSecurity(String firewall,String wall_level,String uiface,String allawip,String allowport,String attack){
		
		String[] param = new String[6];
		
		log.debug("firewall :" + firewall);
		log.debug("wall_level :" + wall_level);
		log.debug("uiface :" + uiface);
		log.debug("allawip :" + allawip);
		log.debug("allowport :" + allowport);
		log.debug("attack :" + attack);
		
		if(firewall == null || firewall.equals("")){
			firewall = "0";
			wall_level = "L";
		}
		
		if(uiface == null || uiface.equals("")){
			allawip = "";
			allowport = "0";
			uiface = "0";
		}
		if(attack == null || attack.equals("")){
			attack = "0";
		}
		
		param[0] =  wall_level;
		param[1] =  firewall;
		param[2] =  allawip;
		param[3] =  allowport;
		param[4] =  uiface;
		param[5] =  attack;
	
		return param;
	}
	/**
	 * 详细设置终端连接数目
	 * @param topbox
	 * @param topbox_num
	 * @param photo
	 * @param photo_num
	 * @param computer
	 * @param computer_num
	 * @param telephone
	 * @param telephone_num
	 * @return
	 */
	public String[] setDetailParam(String topbox, String topbox_num, String photo,
			String photo_num, String computer, String computer_num, String telephone, String telephone_num){
		
		String[] param = new String[8];
		if(topbox == null || topbox.equals("")){
			topbox ="0";
			topbox_num = "0";
		}
		
		if(photo == null || photo.equals("")){
			photo = "0";
			photo_num = "0";
		}
		
		if(computer == null || computer.equals("")){
			computer = "0";
			 computer_num = "0";			
		}
		if(telephone == null ||telephone.equals("")){
			telephone = "0";
			telephone_num = "0";
		}
		
		param[0] = topbox_num;
		param[1] = topbox;
		param[2] = photo_num;
		param[3] = photo;
		param[4] = computer_num;
		param[5] = computer;
		param[6] = telephone_num;
		param[7] = telephone;
		
		return param;
		
		
	}
	public String[] setVpnParam(String vpn){
		
		String[] param = new String[4];
		
		if(vpn == null || vpn.equals("")){
			vpn = "0";
		}
		
		param[0] = vpn;
		param[1] = vpn;
		param[2] = vpn;
		param[3] = vpn;
		return param;
		
	}

	/**
	 * @author lizj （5202）
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		this.dao = dao;
		jt = new JdbcTemplate(dao);
	}
	
    public long getMaxUserId() {
		if(DBUtil.GetDB() == Global.DB_ORACLE || DBUtil.GetDB() == Global.DB_SYBASE) {
			return getMaxUserIdOld();
		}
		return DbUtils.getUnusedID("sql_tab_hgwcustomer", 1);
	}

	public long getMaxUserIdOld() {
		long userid = -1L;
		String str_userId = "";
		String callPro = "maxHgwUserIdProc 1";

		Map map = DataSetBean.getRecord(callPro);
		if (null != map && !map.isEmpty()) {
			str_userId = map.values().toArray()[0].toString();
		} else {
			userid = DataSetBean.getMaxId("tab_hgwcustomer",
					"user_id");
			str_userId = String.valueOf(userid);
			log.debug("----get-str_userid-from-sql-");
		}
		return Long.valueOf(str_userId);
	}
}