
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

public class APIManagerDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(APIManagerDAO.class);
	private int queryCount;

	/**
	 * @author 岩
	 * @date 2016-10-13
	 * @param mqId
	 * @param starttime
	 * @param endtime
	 * @param topicName
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getApiManagerList(String servicenameZh, String servicenameEn,String classifyId, String functionDesc,
			 int curPage_splitPage, int num_splitPage)
	{
		logger.debug("getMqListByMq()");
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.id,a.servicename_zh,a.servicename_en,a.power_classify_id,a.function_desc,a.api_list_name ");
		psql.append(" from  tab_plugin_api a ");
		psql.append(" where 1=1 "  );
		if ((!StringUtil.IsEmpty(servicenameZh)))
		{
			psql.append(" and  a.servicename_zh like '%" + servicenameZh+ "%'");
		}
		if ((!StringUtil.IsEmpty(servicenameEn)))
		{
			psql.append("   and a.servicename_en like '%");
			psql.append(servicenameEn + "%'");
		}
		if ((!StringUtil.IsEmpty(functionDesc)))
		{
			psql.append(" and  a.function_desc like '%" + functionDesc+ "%'");
		}
		if ((!StringUtil.IsEmpty(classifyId))&&(!"-1".equals(classifyId)))
		{
			psql.append("   and a.power_classify_id =");
			psql.append(classifyId);
		}
		
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				// 主题名称
				map.put("manager_id",
						StringUtil.getStringValue(rs.getString("id")));
				// 主题名称
				map.put("servicename_zh",
						StringUtil.getStringValue(rs.getString("servicename_zh")));
				// 消费者订阅数量
				map.put("servicename_en",
						StringUtil.getStringValue(rs.getString("servicename_en")));
				map.put("power_classify_id",
						StringUtil.getStringValue(rs.getString("power_classify_id")));
				String classify_name = getClassifyName(StringUtil.getStringValue(rs.getString("power_classify_id")));
				map.put("classify_name",classify_name);
				// 消息出队
				map.put("function_desc",
						StringUtil.getStringValue(rs.getString("function_desc")));
				// 消息出队
				map.put("api_list_name", StringUtil.getStringValue(rs.getString("api_list_name")));
				return map;
			}
		});
		return list;
	}

	/**
	 * @author 岩
	 * @date 2016-10-13
	 * @param mqId
	 * @param starttime
	 * @param endtime
	 * @param topicName
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int countApiManagerList(String servicenameZh, String servicenameEn,String classifyId, String functionDesc,
			 int curPage_splitPage, int num_splitPage)
	{
		logger.debug("countMqListByMq()");
		PrepareSQL psql = new PrepareSQL();
		// teledb
		if (DBUtil.GetDB() == 3) {
			psql.append("select count(*) ");
		}
		else {
			psql.append("select count(1) ");
		}
		psql.append(" from  tab_plugin_api a ");
		psql.append(" where 1=1 "  );
		if ((!StringUtil.IsEmpty(servicenameZh)))
		{
			psql.append(" and  a.servicename_zh like '%" + servicenameZh+ "%'");
		}
		if ((!StringUtil.IsEmpty(servicenameEn)))
		{
			psql.append("   and a.servicename_en like '%");
			psql.append(servicenameEn + "%'");
		}
		if ((!StringUtil.IsEmpty(functionDesc)))
		{
			psql.append(" and  a.function_desc like '%" + functionDesc+ "%'");
		}
		if ((!StringUtil.IsEmpty(classifyId))&&(!"-1".equals(classifyId)))
		{
			psql.append("   and a.power_classify_id =");
			psql.append(classifyId);
		}
		
		queryCount = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (queryCount % num_splitPage == 0)
		{
			maxPage = queryCount / num_splitPage;
		}
		else
		{
			maxPage = queryCount / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * @author 岩
	 * @date 2016-10-13
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getclassifyName()
	{
		String strSQL = "select id, classify_name from tab_plugin_power_classify  ";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		List<HashMap<String, String>> list = jt.queryForList(psql.getSQL());
		if (list == null)
		{
			list = new ArrayList<HashMap<String, String>>();
		}
		return list;
	}

	public int getQueryCount()
	{
		return queryCount;
	}

	public int addApiManager(String managerId,String servicenameZh, String servicenameEn,String classifyId,
			String functionDesc ,String apiListName)
	{
		String sql = null;
		sql = "insert into tab_plugin_api (id,servicename_zh,servicename_en,power_classify_id,function_desc,api_list_name) values(?,?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(sql);
		int mid = Integer.parseInt(managerId);
		int cid = Integer.parseInt(classifyId);
		psql.setInt(1, mid);
		psql.setString(2, servicenameZh);
		psql.setString(3, servicenameEn);
		psql.setInt(4, cid);
		psql.setString(5, functionDesc);
		psql.setString(6, apiListName);
		return jt.update(psql.getSQL());
	}

	public int updateApiManager(String managerId,String servicenameZh, String servicenameEn,String classifyId,
			String functionDesc ,String apiListName)
	{
		PrepareSQL psql = new PrepareSQL(
				"update tab_plugin_api set servicename_zh = ? ,servicename_en = ?, power_classify_id = ? , function_desc = ?, api_list_name = ? where id = ?");
		int id = Integer.parseInt(managerId);
		int cid = Integer.parseInt(classifyId);
		psql.setString(1, servicenameZh);
		psql.setString(2, servicenameEn);
		psql.setInt(3, cid);
		psql.setString(4, functionDesc);
		psql.setString(5, apiListName);
		psql.setInt(6, id);
		return jt.update(psql.getSQL());
	}

	public void deleteApiManager(String managerId)
	{
		logger.debug("deleteDevice({})", new Object[] { managerId });
		String sql = null;
		sql = "delete from tab_plugin_api where id=?";
		PrepareSQL psql = new PrepareSQL(sql);
		int id = Integer.parseInt(managerId);
		psql.setInt(1, id);
		jt.update(psql.getSQL());
	}
	
	public String getClassifyName(String classifyId) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("select  id, classify_name from tab_plugin_power_classify");
		psql.append(" where 1=1  ");
		psql.append("   and id = "+classifyId);
		List<HashMap<String,String>> List = DBOperation.getRecords(psql.getSQL());
		if(List != null && !List.isEmpty()){
			return StringUtil.getStringValue(List.get(0),"classify_name", "");
		}else{
			return "";
		}
	}
}
