package com.linkage.module.gwms.dao.tabquery;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.tabquery.CtTokenOBJ;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2010-3-9
 */
public class CtTokenDAO extends SuperDAO {

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory
			.getLogger(CtTokenDAO.class);

	/**
	 * 根据UserToken获取表对tab_ct_token的Map对象
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2010-3-10
	 * @return Map 返回tab_ct_token的Map对象； 参数userToken为空则返回null
	 */
	public static Map getCtTokenMap(String userToken) {
		logger.debug("getCtTokenMap({})", userToken);

		if (StringUtil.IsEmpty(userToken)) {
			logger.debug("userToken({}) is Empty ", userToken);
			return null;
		}

		String strSQL = "select user_id, client_ip, login_time, expire_time, result"
				+ " from tab_ct_token where user_token=?";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1, userToken);

		return DataSetBean.getRecord(psql.getSQL());
	}

	/**
	 * 根据UserToken获取BsToken对象
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2010-3-9
	 * @return CtTokenOBJ 返回tab_ct_token的CtTokenOBJ对象； 参数userToken为空则返回null；
	 *         数据库中没有对应的userToken记录返回null
	 */
	public static CtTokenOBJ getCtTokenOBJ(String userToken) {
		logger.debug("getCtTokenOBJ({})", userToken);

		Map ctTokenMap = getCtTokenMap(userToken);
		if (null == ctTokenMap || ctTokenMap.isEmpty()) {
			logger.debug("ctTokenMap is Empty userToken={}", userToken);
			return null;
		}
		CtTokenOBJ ctTokenObj = new CtTokenOBJ();
		ctTokenObj.setUserToken(userToken);
		ctTokenObj.setClientIp(StringUtil.getStringValue(ctTokenMap
				.get("client_ip")));
		ctTokenObj.setExpireTime(StringUtil.getLongValue(ctTokenMap
				.get("expire_time")));
		ctTokenObj.setLoginTime(StringUtil.getLongValue(ctTokenMap
				.get("login_time")));
		ctTokenObj.setResult(StringUtil.getIntegerValue(ctTokenMap
				.get("result")));
		ctTokenObj.setUsername(StringUtil.getStringValue(ctTokenMap
				.get("user_id")));

		return ctTokenObj;
	}

	/**
	 * 存储UserToken和BsToken对象
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2010-3-9
	 * @return void
	 */
	public static boolean saveCtToken(CtTokenOBJ ctTokenObj) {
		logger.debug("saveCtToken({})", ctTokenObj);

		String username = null;
		if (null == ctTokenObj
				|| StringUtil.IsEmpty(username = ctTokenObj.getUsername())) {
			logger.error("saveCtToken(ctTokenObj or username) is null");
			return false;
		}

		String strSQL = "insert into tab_ct_token "
				+ " (user_token, user_id, client_ip, login_time, expire_time, "
				+ " result, bs_token)" + " values (?,?,?,?,?,  ?,?)";

		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1, ctTokenObj.getUserToken());
		psql.setString(2, username);
		psql.setString(3, ctTokenObj.getClientIp());
		psql.setLong(4, ctTokenObj.getLoginTime());
		psql.setLong(5, ctTokenObj.getExpireTime());
		psql.setString(6, StringUtil.getStringValue(ctTokenObj.getResult()));
		psql.setString(7, ctTokenObj.getBsToken());

		return 1 == DataSetBean.executeUpdate(psql.getSQL());
	}

	
	/**
	 * 更新CtToken对象
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2010-3-11
	 * @return boolean
	 */
	public static boolean updateCtToken(CtTokenOBJ ctTokenObj) {
		logger.debug("updateCtToken({})", ctTokenObj);

		String username = null;
		if (null == ctTokenObj
				|| StringUtil.IsEmpty(username = ctTokenObj.getUsername())) {
			logger.error("saveCtToken(ctTokenObj or username) is null");
			return false;
		}

		String strSQL = "update tab_ct_token set user_id=?, client_ip=?, login_time=?, "
				+ " expire_time=?,result=?, bs_token=?"
				+ " where user_token=?";

		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1, username);
		psql.setString(2, ctTokenObj.getClientIp());
		psql.setLong(3, ctTokenObj.getLoginTime());
		psql.setLong(4, ctTokenObj.getExpireTime());
		psql.setString(5, StringUtil.getStringValue(ctTokenObj.getResult()));
		psql.setString(6, ctTokenObj.getBsToken());
		psql.setString(7, ctTokenObj.getUserToken());

		return 1 == DataSetBean.executeUpdate(psql.getSQL());
	}
	
}
