package com.linkage.liposs.dao.aaa;

import java.sql.PreparedStatement;
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
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;

/**
 * 3A集中认证 认证设备操作DAO类
 * @author 何茂才
 *
 */
public class DevAuthenticationDAO {
	private static Logger log = LoggerFactory.getLogger(DevAuthenticationDAO.class);

	// 数据库操作类
	private JdbcTemplate jt;

	// sql封装类
	private PrepareSQL pSQL;
	
	public void setDao(DataSource dao){
		this.jt = new JdbcTemplate(dao);
	}
	
	public void setPSQL(PrepareSQL pSQL){
		this.pSQL = pSQL;
	}
	/**
	 * 认证设备详细信息
	 */
	private static final String sqlDeviceInfo = "select a.authen_prtc,a.tac_key,b.loopback_ip,a.gather_id from tac_device a,tab_gw_device b where a.device_id=b.device_id and a.device_id=?";
	
	/**
	 * 获取设备对应的所有帐号详细信息
	 */
	private static final String sqlDeviceUserInfo = "select a.user_name,a.eff_date,a.exp_date,a.creator,a.default_privilege from tac_users a,tac_device_user b where a.user_name=b.user_name and b.device_id=?";
	
	/**
	 * 从认证设备表中删除设备
	 */
	private static final String sqlDelDevice = "delete from tac_device where device_id=?";
	
	/**
	 * 根据device_id从认证设备帐号对应表中删除记录
	 */
	private static final String sqlDelDeviceUser = "delete from tac_device_user where device_id=?";
	/**
	 * 根据设备编号和帐号删除数据
	 */
	private static final String sqlDelDeviceAndUser = "delete from tac_device_user where device_id=? and user_name=?";
	/**
	 * 添加认证设备
	 */
	private static final String sqlInsertDevice = "insert into tac_device(device_id,gather_id,tac_key,authen_prtc) values(?,?,?,?)";
	
	/**
	 * 修改
	 */
	private static final String sqlUpdateDevice = "update tac_device set authen_prtc=?,tac_key=?,gather_id=? where device_id=?";
	
	/**
	 * 添加认证设备帐号表
	 */
	private static final String sqlInsertDeviceUser = "insert into tac_device_user(device_id,user_name) values(?,?)";
	/**
	 * 查询出按厂商、设备型号、属地并且不在tac_device_user中的设备
	 */
	private static final String sqlDeviceVenModelCity = "select a.device_id,a.device_name,a.loopback_ip " +
			"from tab_gw_device a where a.vendor_id=? and a.device_model_id=? and a.city_id=? and a.gather_id=?" +
			" and a.device_id not in (select distinct device_id from tac_device)";
	private static final String sqlDeviceVenModelCityAdmin="select a.device_id,a.device_name,a.loopback_ip " +
			"from tab_gw_device a where a.vendor_id=? and a.device_model_id=? and a.gather_id=?" +
			" and a.device_id not in (select distinct device_id from tac_device)";
	/**
	 * 查询出已经和设备关联的帐号
	 */
	private static final String sqlExistUser = "select a.* from tac_users a,tac_device_user b where a.user_name=b.user_name and b.device_id=?";
	/**
	 * 查询出没有和设备关联的帐号
	 */
	private static final String sqlNotExistUser = "select a.* from tac_users a where a.user_name not in (select user_name from tac_device_user where device_id=?)";
	/**
	 * 查询出按厂商、设备型号、属地并且不在tac_device_user中的设备
	 ****************************************************
	 * benyp(5260) benyp@lianchuang.com 2008-04-28
	 * Bug:XJDX-XJ-BUG-20080402-XXF-001:添加在过滤设备时,要在sql中把页面的采集点带上,admin时省中心(00)查出所有
	 * **************************************************
	 * @param flg:是否是admin
	 * @param gather_id:采集点
	 * @param city_id
	 * @param vendor_id
	 * @param device_model
	 * @return  [{device_id:,loopback_ip:,device_name:},.....]
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getDeviceList(boolean flg,String gather_id,String city_id,String vendor_id,String device_model){
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		try{
			if(flg && "00".equals(city_id)){
				result.addAll(jt.queryForList(sqlDeviceVenModelCityAdmin,new Object[]{new Integer(vendor_id),device_model,gather_id}));
			}else{
				result.addAll(jt.queryForList(sqlDeviceVenModelCity,new Object[]{new Integer(vendor_id),device_model,city_id,gather_id}));
			}
			return result;
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 通过设备编码获取设备详细信息
	 * @param device_id
	 * @return
	 */
	public Map<String,String>getDeviceInfo(final String device_id){
		final Map<String,String> result = new HashMap<String,String>();
		jt.query(sqlDeviceInfo,new Object[]{device_id},new RowCallbackHandler(){
			//select a.authen_prtc,a.tac_key,b.loopback_ip,a.gather_id
			public void processRow(ResultSet rs) throws SQLException {
				if(rs != null){
					//result.put("device_id", rs.getString("device_id"));
					result.put("authen_prtc", rs.getString("authen_prtc"));
					result.put("tac_key", rs.getString("tac_key"));
					result.put("loopback_ip", rs.getString("loopback_ip"));
					result.put("gather_id", rs.getString("gather_id"));
				}else{
					log.debug(sqlDeviceInfo + "===" + device_id + "无数据.");
				}
			}
		});
		
		return result;
	}
	/**
	 * 获取设备对应的所有帐号信息
	 * @param device_id
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getDeviceUserInfo(String device_id){
		List<Map<String,String>> listInfo = jt.queryForList(sqlDeviceUserInfo, new Object[]{device_id});
		//查询记录为零，则作处理.
		if(listInfo == null){
			listInfo = new ArrayList<Map<String, String>>(0);
		}
		return listInfo;
	}
	/**
	 * 删除认证设备
	 * @param device_id
	 */
	public boolean delete(final String[] device_id){
		if(device_id == null || device_id.length == 0)
			return false;
		try{
			jt.batchUpdate(sqlDelDevice,new BatchPreparedStatementSetter(){
				public void setValues(PreparedStatement ps,int i)throws SQLException{
					ps.setString(1,device_id[i]);
				}
				public int getBatchSize() {
					return device_id.length;
				}
			});
			return true;
		}catch(Exception e){
			log.error(e.getMessage());
			return false;
		}
		
	}
	/**
	 * 从认证设备帐号对应表中删除设备
	 * @param device_id
	 */
	public int[] deleteDeviceUser(final String[] device_id){
		if(device_id == null || device_id.length == 0)
			return null;
		try{
			return jt.batchUpdate(sqlDelDeviceUser,new BatchPreparedStatementSetter(){
				public void setValues(PreparedStatement ps,int i)throws SQLException{
					ps.setString(1,device_id[i]);
				}
				public int getBatchSize() {
					return device_id.length;
				}
			});
		}catch(Exception e){
			log.error(e.getMessage());
			return null;
		}
	}
	/**
	 * 根据帐号和设备id从表中删除数据
	 * @param device_id
	 */
	public int deleteDeviceAndUser(final String device_id,final String user_name){
		if(device_id == null || user_name == null)
			return 0;
		try{
			return jt.update(sqlDelDeviceAndUser, new Object[]{device_id,user_name});
		}catch(Exception e){
			log.error(e.getMessage());
			return 0;
		}
	}
	/**
	 * 添加认证设备
	 * @param device_id 设备id
	 * @param gather_id 认证区域
	 * @param tac_key  认证密匙
	 * @param authen_prtc 认证协议 (1:tacacs+ 2 radius)
	 * @return 返回修改记录数（-1表示异常发生）
	 */
	public int insertDevice(String device_id,String gather_id,String tac_key,int authen_prtc){
		try{
			int codes = jt.update(sqlInsertDevice,new Object[]{device_id,gather_id,tac_key,authen_prtc});
			return codes;
		}catch(DataAccessException e){
			log.error(e.getMessage());
			return -1;
		}
	}
	/**
	 * 批量添加设备
	 * @param DeviceInfos 设备基本信息
	 * @return 返回int[]，参数不合法或者出现异常时返回null
	 */
	public int[] insertBatchDevice(List<DeviceInfo> DeviceInfos){
		if(DeviceInfos == null || DeviceInfos.size() == 0)
			return null;
		final List<DeviceInfo> tempDeviceInfos = DeviceInfos;
		try{
			int[] codes  = jt.batchUpdate(sqlInsertDevice,new BatchPreparedStatementSetter(){
				public int getBatchSize() {
					return tempDeviceInfos.size();
				}
				//insert into tac_device(device_id,gather_id,tac_key,authen_prtc) values(?,?,?,?)
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					DeviceInfo devInfo = tempDeviceInfos.get(i);
					ps.setString(1, devInfo.getDevice_id());
					ps.setString(2, devInfo.getGather_id());
					ps.setString(3, devInfo.getTac_key());
					ps.setInt(4, devInfo.getAuthen_prtc());
				}
			});
			return codes;
		}catch(Exception e){
			log.error(e.getMessage());
			return null;
		}
	}
	
	/**
	 * 添加认证设备帐号
	 * @param device_id 设备id数组
	 * @param user_name  帐号名称数组
	 * @throws Exception 传入参数非法时，抛出异常
	 * @return 批量入库，返回入库结果。出现DataAccessException异常时返回null.
	 */
	public int[] insertDeviceUser(final String[] device_id,final String[] user_name) throws Exception{
		if(device_id == null || user_name == null){
			throw new Exception ("传入的设备id数组或者帐号数组为NULL！");
		}
		
		pSQL.setSQL(sqlInsertDeviceUser);
		int devSize = device_id.length;
		int userSize = user_name.length;
		List<String> list = new ArrayList<String>(devSize*userSize);
		for(int i = 0; i < devSize ;i ++){
			for(int j = 0; j < userSize ;j ++){
				pSQL.setString(1, device_id[i]);
				pSQL.setString(2, user_name[j]);
				list.add(pSQL.getSQL());
			}
		}
	
		try{
			return jt.batchUpdate((String[])list.toArray(new String[0]));
		}catch(DataAccessException e){
			log.error(e.getMessage());
			return null;
		}
	}
	/**
	 * 查询出已经和设备关联的帐号
	 * @param device_id
	 * @return
	 */
	private List<Map<String,String>> getUserOfDevice(String sql,String device_id){
		final List<Map<String,String>> userList = new ArrayList<Map<String,String>>();
		try{
			PrepareSQL psql = new PrepareSQL(sql);
			jt.query(psql.getSQL(), new Object[]{device_id}, new RowCallbackHandler(){

			public void processRow(ResultSet rs) throws SQLException {
				Map<String,String> user = new HashMap<String,String>();
				user.put("user_name", rs.getString("user_name"));
				user.put("creator", rs.getString("creator"));
				user.put("default_privilege", rs.getString("default_privilege"));
				user.put("eff_date", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", rs.getLong("eff_date")));
				user.put("exp_date", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", rs.getLong("exp_date")));
				userList.add(user);
			}
		});
		}catch(Exception e){
			log.debug(e.getMessage());
		}
		return userList;
	}
	/**
	 * 查询出已经和设备关联的帐号
	 * @param device_id
	 * @return
	 */
	@SuppressWarnings("unused")
	public List<Map<String,String>> getExistUser(String device_id){
		String sql = sqlExistUser;

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select a.user_name, a.creator, a.default_privilege, a.eff_date, a.exp_date from tac_users a,tac_device_user b where a.user_name=b.user_name and b.device_id=?";
		}
		return getUserOfDevice(sql,device_id);
	}
	/**
	 * 查询出没有和设备关联的帐号
	 * @param device_id
	 * @return
	 */
	@SuppressWarnings("unused")
	public List<Map<String,String>> getNotExistUser(String device_id){
		String sql = sqlNotExistUser;

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select a.user_name, a.creator, a.default_privilege, a.eff_date, a.exp_date from tac_users a where a.user_name not in (select user_name from tac_device_user where device_id=?)";
		}

		return getUserOfDevice(sql,device_id);
	}
	/**
	 * 修改
	 * @param device_id
	 * @param authen_prtc
	 * @param tac_key
	 * @param gather_id
	 * @return
	 */
	public int updateDevice(String device_id,int authen_prtc,String tac_key,String gather_id){
		return jt.update(sqlUpdateDevice,new Object[]{authen_prtc,tac_key,gather_id,device_id});
	}
	
	public static class DeviceInfo{
		String device_id;
		String gather_id;
		String tac_key;
		int authen_prtc;
		public DeviceInfo(){
		}
		public String getDevice_id() {
			return device_id;
		}
		public void setDevice_id(String device_id) {
			this.device_id = device_id;
		}
		public String getGather_id() {
			return gather_id;
		}
		public void setGather_id(String gather_id) {
			this.gather_id = gather_id;
		}
		public String getTac_key() {
			return tac_key;
		}
		public void setTac_key(String tac_key) {
			this.tac_key = tac_key;
		}
		public int getAuthen_prtc() {
			return authen_prtc;
		}
		public void setAuthen_prtc(int authen_prtc) {
			this.authen_prtc = authen_prtc;
		}
	}
}


