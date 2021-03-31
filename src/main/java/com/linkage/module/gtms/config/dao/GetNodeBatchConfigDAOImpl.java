/**
 *
 */
package com.linkage.module.gtms.config.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author chenjie
 *
 */
public class GetNodeBatchConfigDAOImpl  extends SuperDAO  implements GetNodeBatchConfigDAO{

	private static Logger logger = LoggerFactory.getLogger(GetNodeBatchConfigDAOImpl.class);

	public int getNodeBatchCount(int curPage_splitPage, int num_splitPage,
			String customId, String fileName, String starttime, String endtime)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from gw_batch_gather_task a,tab_accounts b where a.acc_oid = b.acc_oid");
		if (!StringUtil.IsEmpty(customId) && !"".equals(customId))
		{
			sql.append(" and b.acc_loginname = '" + customId+"'");
		}
		if (!StringUtil.IsEmpty(fileName) && !"".equals(fileName))
		{
			sql.append(" and file_name like '%"+fileName+"%'");
		}
		if (!StringUtil.IsEmpty(starttime) && !"".equals(starttime))
		{
			sql.append(" and add_time >= " + new DateTimeUtil(starttime).getLongTime());
		}
		if (!StringUtil.IsEmpty(endtime) && !"".equals(endtime))
		{
			sql.append(" and add_time <= " + new DateTimeUtil(endtime).getLongTime());
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		int total = jt.queryForInt(sql.toString());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public List<Map> getNodeBatchList(int curPage_splitPage, int num_splitPage,
			String customId, String fileName, String starttime, String endtime)
			{
		final Map<String, String> loginMap = getLoginName();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.acc_oid,a.file_name,a.add_time,a.result_file,a.do_status from gw_batch_gather_task a,tab_accounts b where a.acc_oid = b.acc_oid");
		if (!StringUtil.IsEmpty(customId) && !"".equals(customId))
		{
			sql.append(" and b.acc_loginname = '" + customId +"'");
		}
		if (!StringUtil.IsEmpty(fileName) && !"".equals(fileName))
		{
			sql.append(" and file_name like '%"+fileName+"%'");
		}
		if (!StringUtil.IsEmpty(starttime) && !"".equals(starttime))
		{
			sql.append(" and add_time >= " + new DateTimeUtil(starttime).getLongTime());
		}
		if (!StringUtil.IsEmpty(endtime) && !"".equals(endtime))
		{
			sql.append(" and add_time <= " + new DateTimeUtil(endtime).getLongTime());
		}
		sql.append(" order by task_id desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("loginname", loginMap.get(rs.getString("acc_oid")));
				map.put("filename", rs.getString("file_name"));
				map.put("result_file", rs.getString("result_file"));
				map.put("dostatus", rs.getString("do_status"));
				long oper_time = StringUtil.getLongValue(rs
						.getString("add_time"));
				DateTimeUtil dt = new DateTimeUtil(oper_time * 1000);
				map.put("customtime", dt.getLongDate());
				return map;
			}
		});
			}

	public Map<String, String> getLoginName(){
		PrepareSQL psql = new PrepareSQL("select acc_oid,acc_loginname from tab_accounts where acc_oid is not null");
		Cursor cursor = DataSetBean
				.getCursor(psql.getSQL());
		HashMap<String, String> loginNameMap = new HashMap<String, String>();
		Map fields = cursor.getNext();
		while (fields != null)
		{
			String acc_loginname = (String) fields.get("acc_loginname");
			if (false == StringUtil.IsEmpty(acc_loginname))
			{
				loginNameMap.put((String) fields.get("acc_oid"), acc_loginname);
			}
			else
			{
				loginNameMap.put((String) fields.get("acc_oid"),
						(String) fields.get("acc_loginname"));
			}
			fields = cursor.getNext();
		}
		return loginNameMap;
	}

	public int recordTask(long acc_oid, long add_time, String gather_path, String file_name, String file_path, int do_status, String result_file)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("insert into gw_batch_gather_task (task_id,acc_oid,add_time,gather_path,file_name,file_path,do_status,result_file) values(?,?,?,?,?,?,?,?)");
		psql.setLong(1, add_time);
		psql.setLong(2, acc_oid);
		psql.setLong(3, add_time);
		psql.setString(4, gather_path);
		psql.setString(5, file_name);
		psql.setString(6, file_path);
		psql.setInt(7, do_status);
		psql.setString(8, result_file);
		return DBOperation.executeUpdate(psql.getSQL());
	}

	public String getDevSql(long add_time,String device_id, String oui, String device_serialnumber, String loid,
			String gather_path1, String gather_path2, String gather_path3,String gather_path4,
			long gather_status, String gather_times, String city_id, String city_name)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("insert into gw_batch_gather_dev (task_id,device_id,oui,device_serialnumber,loid,"
				+"gather_path1,gather_path2,gather_path3,gather_path4,gather_status,gather_times,city_id,city_name) values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
		psql.setLong(1, add_time);
		psql.setString(2, device_id);
		psql.setString(3, oui);
		psql.setString(4, device_serialnumber);
		psql.setString(5, loid);
		psql.setString(6, gather_path1);
		psql.setString(7, gather_path2);
		psql.setString(8, gather_path3);
		psql.setString(9, gather_path4);
		psql.setLong(10, gather_status);
		psql.setString(11, gather_times);
		psql.setString(12, city_id);
		psql.setString(13, city_name);
		return psql.toString();
	}

	public int recordDev(ArrayList<String> devSqlList){
		return DBOperation.executeUpdate(devSqlList);
	}

	public int queryCustomNum(long todayTimeMillis) {

		logger.debug("queryCustomNum()");
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) from gw_batch_gather_dev where task_id > ");
		psql.append(""+todayTimeMillis);
		int num = jt.queryForInt(psql.getSQL());
		return num;
	}
	public int queryRepeatName(String checkRepeatname) {

		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) from gw_batch_gather_task where file_name = '");
		psql.append(checkRepeatname+"'");
		int num = jt.queryForInt(psql.getSQL());
		return num;
	}
}
