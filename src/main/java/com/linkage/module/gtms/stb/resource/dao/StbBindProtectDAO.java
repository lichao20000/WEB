/**
 *
 */
package com.linkage.module.gtms.stb.resource.dao;

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
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.utils.DeviceTypeUtil;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author songxq
 * @version 1.0
 * @since 2019-9-16 下午3:20:48
 * @category
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */

public class StbBindProtectDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(StbBindProtectDAO.class);

	private static String writeItemLogSql = "insert into tab_oper_log("
			+ "acc_oid,acc_login_ip,operationlog_type,operation_time,operation_name"
			+ ",operation_object,operation_content"
			+ ",operation_result,result_id) values(?,?,?,?,?,?,?,?,?)";

	/**
	 * @param userName
	 * @param mac
	 * @param acc_oid
	 * @param remark
	 * @return
	 * 2019-9-16

	 */
	public String add(String userName, String mac, String acc_oid, String remark)
	{
		// TODO Auto-generated method stub
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("insert into stb_bind_protected_table values (?,?,?,?,?)");
		pSql.setString(1, userName);
		pSql.setString(2, mac);
		pSql.setLong(3, System.currentTimeMillis()/1000);
		pSql.setString(4, acc_oid);
		pSql.setString(5, remark);
		int result = DBOperation.executeUpdate(pSql.getSQL());
		return StringUtil.getStringValue(result);
	}

	/**
	 * @param userName
	 * @param mac
	 * @param num_splitPage
	 * @param curPage_splitPage
	 * @return
	 * 2019-9-16

	 */
	public List<HashMap<String, String>> query(String userName, String mac, int curPage_splitPage, int num_splitPage)
	{
		// TODO Auto-generated method stub
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("select a.addtime, a.remark, a.mac, a.username, b.per_name " +
				"from stb_bind_protected_table a , tab_persons b where a.acc_oid = b.per_acc_oid");
		if(null != userName && !"".equals(userName) && ("".equals(mac) || null ==  mac || "null".equals(mac) ))
		{
			pSql.setSQL("select a.addtime, a.remark, a.mac, a.username, b.per_name " +
					"from stb_bind_protected_table a , tab_persons b where a.acc_oid = b.per_acc_oid and a.username like ? ");
			pSql.setString(1, userName+"%");
		}
		if (null != mac && !"".equals(mac) && "".equals(userName))
		{
			pSql.setSQL("select a.addtime, a.remark, a.mac, a.username, b.per_name " +
					"from stb_bind_protected_table a , tab_persons b where a.acc_oid = b.per_acc_oid and a.mac = ? ");
			pSql.setString(1, mac);
		}
		if(null != userName && !"".equals(userName) && null != mac && !"".equals(mac) )
		{
			pSql.setSQL("select a.addtime, a.remark, a.mac, a.username, b.per_name " +
					"from stb_bind_protected_table a , tab_persons b where  a.acc_oid = b.per_acc_oid and a.username like ? and a.mac = ? ");
			pSql.setString(1, userName+"%");
			pSql.setString(2, mac);
		}

		/*List<HashMap<String, String>> result = DBOperation.getRecords(pSql.getSQL());
		for (HashMap<String, String> hashMap : result)
		{
			String addtime = StringUtil.getStringValue(hashMap.get("addtime"));
			DateTimeUtil dateTimeUtil = new DateTimeUtil(Long
					.parseLong(addtime) * 1000);
			addtime = dateTimeUtil.getLongDate();

			hashMap.put("addtime", addtime);
			reHashMaps.add(hashMap);
		}*/
		List list = querySP(pSql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						String addtime = StringUtil.getStringValue(rs.getInt("addtime"));
						DateTimeUtil dateTimeUtil = new DateTimeUtil(Long
								.parseLong(addtime) * 1000);
						addtime = dateTimeUtil.getLongDate();
						map.put("addtime", addtime);
						map.put("remark", rs.getString("remark"));
						map.put("mac", rs.getString("mac"));
						map.put("acc_oid", rs.getString("per_name"));
						map.put("username", rs.getString("username"));
						return map;
					}
				});



		return  list;
	}

	/**
	 * @param userName
	 * @param mac
	 * @return
	 * 2019-9-16

	 */
	public int hasData(String userName, String mac)
	{
		// TODO Auto-generated method stub
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("select count(*) num from stb_bind_protected_table where  username = ? or mac = ? ");
		pSql.setString(1, userName);
		pSql.setString(2, mac);
		Map<String, String> resultMap = DBOperation.getRecord(pSql.getSQL());
		if(null != resultMap && resultMap.size() > 0)
		{
			return 1 ;
		}
		return 0;
	}

	/**
	 * @param userName
	 * @param mac
	 * 2019-9-17
	 * @return

	 */
	public int delete(String userName, String mac)
	{
		// TODO Auto-generated method stub
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("delete from stb_bind_protected_table where  username = ? and mac = ? ");
		pSql.setString(1, userName);
		pSql.setString(2, mac);
		return  DBOperation.executeUpdate(pSql.getSQL());
	}

	/**
	 * @param userName
	 * @param mac
	 * @param acc_oid
	 * @param remark
	 * @param remarkEdit
	 * @return
	 * 2019-9-17

	 */
	public int update(String userName, String mac,  String userNameEdit, String macEdit, String remarkEdit)
	{
		// TODO Auto-generated method stub
		PrepareSQL pSql = new PrepareSQL();
		if((null == userNameEdit || "".equals(userNameEdit)) && (null == macEdit || "".equals(macEdit)))
		{
			pSql.setSQL("update stb_bind_protected_table set remark = ?,addtime = ? where  username = ? and mac = ? ");
			pSql.setString(1, remarkEdit);
			pSql.setLong(2, System.currentTimeMillis()/1000);
			pSql.setString(3, userName);
			pSql.setString(4, mac);
		}
		else if(null == userNameEdit || "".equals(userNameEdit))
		{
			pSql.setSQL("update stb_bind_protected_table set mac = ?,remark = ?,addtime = ? where  username = ? and mac = ? ");
			pSql.setString(1, macEdit);
			pSql.setString(2, remarkEdit);
			pSql.setLong(3, System.currentTimeMillis()/1000);
			pSql.setString(4, userName);
			pSql.setString(5, mac);
		}
		else if (null == macEdit || "".equals(macEdit)) {
			pSql.setSQL("update stb_bind_protected_table set username = ?,remark = ?,addtime = ? where  username = ? and mac = ? ");
			pSql.setString(1, userNameEdit);
			pSql.setString(2, remarkEdit);
			pSql.setLong(3, System.currentTimeMillis()/1000);
			pSql.setString(4, userName);
			pSql.setString(5, mac);
		}
		else {
			pSql.setSQL("update stb_bind_protected_table set username = ?,mac = ?,remark = ?,addtime = ? where  username = ? and mac = ? ");
			pSql.setString(1, userNameEdit);
			pSql.setString(2, macEdit);
			pSql.setString(3, remarkEdit);
			pSql.setLong(4, System.currentTimeMillis()/1000);
			pSql.setString(5, userName);
			pSql.setString(6, mac);
		}

		return  DBOperation.executeUpdate(pSql.getSQL());
	}

	/**
	 * @param ip
	 * @param id
	 * @param ajax
	 * 2019-9-18

	 */
	public void insertOperLog(String ip, long id, String ajax,String menuName,String operationContent)
	{
		// TODO Auto-generated method stub
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL(writeItemLogSql);
		pSql.setLong(1, id);
		pSql.setString(2, ip);
		pSql.setLong(3, 1); //1 代表web
		pSql.setLong(4, System.currentTimeMillis()/1000);
		pSql.setString(5, "0");
		pSql.setString(6, menuName); //菜单名
		pSql.setString(7, operationContent);//具体操作
		if("1".equals(ajax))
		{
			pSql.setString(8, "成功");//操作结果
			pSql.setInt(9, 1);
		}
		else
		{
			pSql.setString(8, "失败");//操作结果
			pSql.setInt(9, 0);
		}
		int result = DBOperation.executeUpdate(pSql.getSQL());

		if(1 == result)
		{
			logger.warn("记录操作日志表成功");
		}
		else
		{
			logger.error("记录操作日志表失败");
		}
	}

	/**
	 * @param userName
	 * @param mac
	 * @return
	 * 2019-9-25

	 */
	public List<HashMap<String, String>> query(String userName, String mac)
	{
		// TODO Auto-generated method stub
		List<HashMap<String, String>> reHashMaps = new ArrayList<HashMap<String,String>>();
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("select a.addtime from stb_bind_protected_table a , tab_persons b where a.acc_oid = b.per_acc_oid  " +
				"and  a.username like ? and a.mac = ? ");
		pSql.setString(1, userName+"%");
		pSql.setString(2, mac);
		List<HashMap<String, String>> result = DBOperation.getRecords(pSql.getSQL());
		for (HashMap<String, String> hashMap : result)
		{
			String addtime = StringUtil.getStringValue(hashMap.get("addtime"));
			DateTimeUtil dateTimeUtil = new DateTimeUtil(Long
					.parseLong(addtime) * 1000);
			addtime = dateTimeUtil.getLongDate();

			hashMap.put("addtime", addtime);
			reHashMaps.add(hashMap);
		}
		return reHashMaps;
	}

	/**
	 * @param userName
	 * @param mac
	 * @return
	 * 2019-9-25

	 */
	public int queryNum(String userName, String mac)
	{
		// TODO Auto-generated method stub
		PrepareSQL pSql = new PrepareSQL();
		int num = 0 ;
		pSql.setSQL("select count(*) num  from stb_bind_protected_table a , tab_persons b where a.acc_oid = b.per_acc_oid");
		if(null != userName && !"".equals(userName) && ("".equals(mac) || null ==  mac || "null".equals(mac) ))
		{
			pSql.setSQL("select count(*) num  from stb_bind_protected_table a , tab_persons b where a.acc_oid = b.per_acc_oid and a.username like ? ");
			pSql.setString(1, userName+"%");
		}
		if (null != mac && !"".equals(mac) && "".equals(userName))
		{
			pSql.setSQL("select count(*) num  from stb_bind_protected_table a , tab_persons b where a.acc_oid = b.per_acc_oid and a.mac = ? ");
			pSql.setString(1, mac);
		}
		if(null != userName && !"".equals(userName) && null != mac && !"".equals(mac) )
		{
			pSql.setSQL("select count(*) num  from stb_bind_protected_table a , tab_persons b where  a.acc_oid = b.per_acc_oid and a.username like ? and a.mac = ? ");
			pSql.setString(1, userName+"%");
			pSql.setString(2, mac);
		}
		Map<String, String> result = DBOperation.getRecord(pSql.getSQL());
		if(null != result && !result.isEmpty() )
		{
			num = StringUtil.getIntegerValue(result.get("num"));
		}
		return num;
	}

}

