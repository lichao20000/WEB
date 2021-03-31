package com.linkage.liposs.dao.aaa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;

/**
 * 查询、显示AAA帐号信息功能的dao
 * @author 陈仲民  2007-12-03
 * @version 1.0
 */

public class UserInfoDAO {

	private JdbcTemplate jt;
	
	private Map<String,String> gatherMap = new HashMap<String,String>();
	
	/**
	 * 查询AAA认证帐号的信息
	 * @param eff_date 生效时间
	 * @param exp_date 失效时间
	 * @param user_name AAA帐号
	 * @param creator 创建者
	 * @return user_name,default_privilege,eff_date,exp_date,create_date,creator
	 */
	public List<Map> getUserInfo(String eff_date,String exp_date,String user_name,String creator){
		
		String userSql = "select * from tac_users where 1=1";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			userSql = "select user_name, default_privilege, eff_date, exp_date, create_date, creator from tac_users where 1=1";
		}

		//生效时间
		if (!"".equals(eff_date)){
			DateTimeUtil data1 = new DateTimeUtil(eff_date);
			userSql += " and eff_date > " + data1.getLongTime();
		}
		
		//失效时间
		if (!"".equals(exp_date)){
			DateTimeUtil data2 = new DateTimeUtil(exp_date);
			userSql += " and exp_date < " + data2.getLongTime();
		}
		
		//AAA认证帐号
		if (!"".equals(user_name)){
			userSql += " and user_name = '" + user_name + "'";
		}
		
		//创建者
		if (!"".equals(creator)){
			userSql += " and creator = '" + creator + "'";
		}
		
		//查询
		PrepareSQL psql = new PrepareSQL(userSql);
		List userData = jt.query(psql.getSQL(), new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				
				Map<String,String> map = new HashMap<String,String>();
				
				//AAA帐号
				map.put("user_name", rs.getString("user_name"));
				
				//默认权限操作规则
				if ("0".equals(rs.getString("default_privilege"))){
					map.put("default_privilege", "禁止");
				}
				else{
					map.put("default_privilege", "允许");
				}
				
				//生效时间
				String eff_date = rs.getString("eff_date");
				if (!"".equals(eff_date)){
					DateTimeUtil eff_time = new DateTimeUtil(Long.parseLong(eff_date)*1000);
					map.put("eff_date", eff_time.getLongDate());
				}
				else{
					
					map.put("eff_date", "");
				}
				
				//失效时间
				String exp_date = rs.getString("exp_date");
				if (!"".equals(exp_date)){
					DateTimeUtil exp_time = new DateTimeUtil(Long.parseLong(exp_date)*1000);
					map.put("exp_date", exp_time.getLongDate());
				}
				else{
					
					map.put("exp_date", "");
				}
				
				//创建时间
				String create_date = rs.getString("create_date");
				if (!"".equals(eff_date)){
					DateTimeUtil create_time = new DateTimeUtil(Long.parseLong(create_date)*1000);
					map.put("create_date", create_time.getLongDate());
				}
				else{
					
					map.put("create_date", "");
				}
				
				map.put("creator", rs.getString("creator"));
				
				return map;
			}
			
		});
		
		return userData;
	}
	
	/**
	 * 删除指定的AAA认证帐号及其相关配置
	 * @param user_name AAA帐号
	 */
	public void delUserInfo(String user_name){
		
		//删除AAA帐号
		String delUser = "delete from tac_users where user_name='" + user_name + "'";
		//删除帐号权限信息
		String delPrivileg = "delete from user_privilege where user_name='" + user_name + "'";
		//删除关联设备信息
		String delDevice = "delete from tac_device_user where user_name='" + user_name + "'";
		
		String[] sqlArr = new String[3];
		sqlArr[0] = delUser;
		sqlArr[1] = delPrivileg;
		sqlArr[2] = delDevice;
		PrepareSQL psql = new PrepareSQL(sqlArr[0]);
        psql.getSQL();
        psql = new PrepareSQL(sqlArr[1]);
        psql.getSQL();
        psql = new PrepareSQL(sqlArr[2]);
        psql.getSQL();
		//执行sql
		jt.batchUpdate(sqlArr);
	}
	
	/**
	 * 查询AAA认证帐号的权限信息
	 * @param user_name AAA帐号
	 * @return cmd_name，cmd_value，privilege_op
	 */
	public List<Map> getPrivileg(String user_name){
		String sql = "select * from user_privilege where user_name='" + user_name + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select cmd_name, cmd_value, privilege_op from user_privilege where user_name='" + user_name + "'";
		}

		PrepareSQL psql = new PrepareSQL(sql);
		//查询
		List privilegeList = jt.query(psql.getSQL(), new RowMapper(){
			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				
				Map<String,String> map = new HashMap<String,String>();
				
				//操作命令
				map.put("cmd_name", rs.getString("cmd_name"));
				//参数
				map.put("cmd_value", rs.getString("cmd_value"));
				
				//权限操作
				String privilege_op = rs.getString("privilege_op");
				if ("1".equals(privilege_op)){
					map.put("privilege_op", "禁止");
				}
				else{
					map.put("privilege_op", "允许");
				}
				
				return map;
			}
		});
		
		return privilegeList;
	}
	
	/**
	 * 查询AAA认证帐号的关联设备信息
	 * @param user_name AAA帐号
	 * @return loopback_ip,device_name,resource_type_id,authen_prtc,gather_id
	 */
	public List<Map> getDevice(String user_name){
		
		//查询出采集机的信息
		getGatherMap();
		
		String sql = "select b.*,c.loopback_ip,c.device_name,c.device_model_id as device_model from tac_device_user a,tac_device b,tab_gw_device c where a.device_id=b.device_id and a.device_id=c.device_id and user_name='" + user_name + "'";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select b.authen_prtc,b.gather_id,c.loopback_ip,c.device_name,c.device_model_id as device_model from tac_device_user a,tac_device b,tab_gw_device c where a.device_id=b.device_id and a.device_id=c.device_id and user_name='" + user_name + "'";
		}


		PrepareSQL psql = new PrepareSQL(sql);
		//查询设备
		List deviceInfo = jt.query(psql.getSQL(), new RowMapper(){
			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				
				Map<String,String> map = new HashMap<String,String>();
				
				//ip
				map.put("loopback_ip", rs.getString("loopback_ip"));
				//设备名称
				map.put("device_name", rs.getString("device_name"));
				//设备类型
				map.put("device_model", rs.getString("device_model"));
				
				//认证协议
				String authen_prtc = rs.getString("authen_prtc");
				if ("2".equals(authen_prtc)){
					map.put("authen_prtc", "radius");
				}
				else{
					map.put("authen_prtc", "tacacs+");
				}
				
				//认证区域
				map.put("gather_id", gatherMap.get(rs.getString("gather_id")));
				
				return map;
			}
		});
		
		return deviceInfo;
	}
	
	/**
	 * 获取采集机的信息
	 * @return
	 */
	private void getGatherMap(){
		
		//查询出所有的采集机
		PrepareSQL psql = new PrepareSQL("select gather_id,descr from tab_process_desc");
		List gatherList = jt.queryForList(psql.getSQL());
		int length = gatherList.size();
		
		for (int i=0;i<length;i++){
			Map data = (Map)gatherList.get(i);
			
			//放入HashMap中
			if (data != null){
				String gather_id = (String)data.get("gather_id");
				String descr = (String)data.get("descr");
				
				gatherMap.put(gather_id, descr);
			}
		}
		
	}
	
	/**
	 * 初始化数据连接
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}
}
