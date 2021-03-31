package com.linkage.module.gwms.sysConfig.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;

public class ResourceBindMemConfigDAO {

	//日志记录
	private static Logger logger = LoggerFactory.getLogger(ResourceBindMemConfigDAO.class);
	
	// spring的jdbc模版类
	public JdbcTemplate jt;
	
	/**
	 * setDao 注入
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}
	
	/**
	 * 如果没有查询到记录或者查询到的记录大于1条，返回null
	 * 	如果结果记录为1条返回jt.queryForMap(sql)
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-8-7
	 * @return Map
	 */
	public Map queryForMap(String sql){
		logger.debug("queryForMap({})", sql);
		Map rMap = null;
		try{
			rMap = jt.queryForMap(sql);
			return rMap;
		}catch(IncorrectResultSizeDataAccessException e){
			logger.error(e.getMessage());
			return rMap;
		}catch(DataAccessException e1){
			logger.error(e1.getMessage());
			return rMap;
		}
	}
	
	/**
	 * 根据逻辑SN查询用户
	 * 
	 * @author wangsenbo
	 * @date Sep 13, 2010
	 * @param
	 * @return String
	 */
	public long getUserBySN4H(String username)
	{
		logger.debug("getUserBySN({})", username);
		StringBuffer sql = new StringBuffer();
		sql.append("select user_id from tab_hgwcustomer where username='").append(
				username).append("'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map map = queryForMap(psql.getSQL());
		if (map != null)
		{
			long userId = StringUtil.getLongValue(map, "user_id");
			return userId;
		}
		else
		{
			return -1;
		}
	}
	
	/**
	 * 根据宽带号码或IPTV号码查询用户
	 * 
	 * @author wangsenbo
	 * @date Sep 13, 2010
	 * @param
	 * @return String
	 */
	public long getUserByServ4H(String username, String servstauts)
	{
		logger.debug("getUserByServ({},{})", username, servstauts);
		StringBuffer sql = new StringBuffer();
		sql.append("select user_id from hgwcust_serv_info where username='").append(
				username).append("' and serv_type_id=").append(servstauts);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map map = queryForMap(psql.getSQL());
		if (map != null)
		{
			long userId = StringUtil.getLongValue(map, "user_id");
			return userId;
		}
		else
		{
			return -1;
		}
	}
	
	/**
	 * 根据VoIP认证号码或VoIP电话号码查询用户
	 * 
	 * @author wangsenbo
	 * @date Sep 13, 2010
	 * @param
	 * @return String
	 */
	public long getUserByVoip4H(String voipUsername, String voipPhone)
	{
		logger.debug("getUserByVoip({},{})", voipUsername, voipPhone);
		if (true == StringUtil.IsEmpty(voipUsername)
				&& true == StringUtil.IsEmpty(voipPhone))
		{
			logger.debug("VoIP认证号码和VoIP电话号码都为空");
			return -1;
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select user_id from tab_voip_serv_param where 1=1");
		if (false == StringUtil.IsEmpty(voipUsername))
		{
			sql.append(" and voip_username='").append(voipUsername).append("'");
		}
		if (false == StringUtil.IsEmpty(voipPhone))
		{
			sql.append(" and voip_phone='").append(voipPhone).append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map map = queryForMap(psql.getSQL());
		if (map != null)
		{
			long userId = StringUtil.getLongValue(map, "user_id");
			return userId;
		}
		else
		{
			return -1;
		}
	}
	
	/**
	 * 查询业务信息
	 * 
	 * @author wangsenbo
	 * @param num_splitPage
	 * @param curPage_splitPage
	 * @date Sep 13, 2010
	 * @param
	 * @return List<Map>
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getUserInfo4H(long userId){
		logger.debug("getUserInfo({})",userId);
		PrepareSQL sql = new PrepareSQL();
		sql.setSQL("select a.user_id,a.username,a.city_id,a.device_serialnumber,a.oui,a.device_id,b.city_name from tab_hgwcustomer a,tab_city b where a.city_id=b.city_id and a.user_id=?");

		sql.setLong(1, userId);

		return jt.queryForList(sql.getSQL());
	}
	
	/**
	 * 查询业务信息
	 * 
	 * @author wangsenbo
	 * @param num_splitPage
	 * @param curPage_splitPage
	 * @date Sep 13, 2010
	 * @param
	 * @return List<Map>
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getUserDetail4H(String username){
		logger.debug("getUserDetail({})",username);
		PrepareSQL sql = new PrepareSQL();
		sql.setSQL("select a.user_id,a.city_id,a.username,a.userline,a.is_chk_bind,a.device_serialnumber,a.oui,a.device_id,b.type_id from tab_hgwcustomer a,gw_cust_user_dev_type b where a.user_id=b.user_id and a.username=?");

		sql.setString(1, username);

		return jt.queryForList(sql.getSQL());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map> getUserBindDetail4H(String deviceId){
		logger.debug("getUserBindDetail4H({})",deviceId);
		PrepareSQL sql = new PrepareSQL();
		sql.setSQL("select a.user_id,a.city_id,a.username,a.userline,a.is_chk_bind,a.device_serialnumber,a.oui,a.device_id from tab_hgwcustomer a where a.device_id=?");

		sql.setString(1, deviceId);

		return jt.queryForList(sql.getSQL());
	}
	
	public List getCorbaIor(){
		PrepareSQL pSQL = new PrepareSQL("select ior from tab_ior where object_name=?");
		if(1==LipossGlobals.SystemType())
		{
			pSQL.setString(1, "BusinessLogic");
		}else
		{
			pSQL.setString(1, "ITMS_BusinessLogic");
		}
		

		return jt.queryForList(pSQL.toString());
		
	}
}
