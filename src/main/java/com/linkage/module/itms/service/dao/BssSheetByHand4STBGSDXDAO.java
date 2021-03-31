
package com.linkage.module.itms.service.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.itms.service.obj.SheetObj4GSStb;

/**
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2021年1月27日
 * @category com.linkage.module.itms.service.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BssSheetByHand4STBGSDXDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory
			.getLogger(BssSheetByHand4STBGSDXDAO.class);

	public boolean isServStbExists(String servAccount)
	{
		String tabName = "stb_tab_customer";
		boolean isRet = false;
		if (StringUtil.IsEmpty(servAccount, true))
		{
			logger.warn("机顶盒用户账号为空,返回");
			return isRet;
		}
		PrepareSQL psql = new PrepareSQL();
		psql.append("select customer_id from ");
		psql.append(tabName);
		psql.append(" where (cust_stat='1' or cust_stat='2') and serv_account=? ");
		psql.setString(1, servAccount);
		List<HashMap<String, String>> map = DBOperation.getRecords(psql.getSQL());
		if (null == map || map.isEmpty())
		{
			isRet = false;
		}else{
			isRet = true;
		}
		return isRet;

	}

	public Map<String, String> getStbUserInfo(String servAccount)
	{
		if (StringUtil.IsEmpty(servAccount, true))
		{
			logger.warn("机顶盒用户servAccount为空,返回");
			return null;
		}
		PrepareSQL psql = new PrepareSQL();
		psql.append("select city_id, serv_account, serv_pwd,platform,cpe_mac from stb_tab_customer where (cust_stat='1' or cust_stat='2') and serv_account=? ");
		psql.setString(1, servAccount);
		List<HashMap<String, String>> map = DBOperation.getRecords(psql.getSQL());
		if (map.isEmpty())
		{
			return null;
		}
		return map.get(0);
	}

	public void addHandSheetLog(SheetObj4GSStb userInfoSheet, UserRes curUser,
			int servType, int operType, int resultId, String resultDesc)
	{
		logger.debug("addHandSheetLog()");
		User user = curUser.getUser();
		PrepareSQL psql = new PrepareSQL();
		psql.append("insert into tab_handsheet_log (id,username,city_id,serv_type_id,oper_type,result_id,result_desc,oper_time,occ_id,occ_ip) values(?,?,?,?,?,?,?,?,?,?) ");
		psql.setString(
				1,
				user.getId()
						+ StringUtil.getStringValue(Long.valueOf(Math.round(Math.random() * 1000000000000.0D))));
		psql.setString(2, userInfoSheet.getUserID());
		psql.setString(3, userInfoSheet.getCityId());
		psql.setInt(4, servType);
		psql.setInt(5, operType);
		psql.setInt(6, resultId);
		psql.setString(7, resultDesc);
		psql.setLong(8, new Date().getTime() / 1000L);
		psql.setLong(9, user.getId());
		psql.setString(10, "");
		DBOperation.executeUpdate(psql.getSQL());
	}
}
