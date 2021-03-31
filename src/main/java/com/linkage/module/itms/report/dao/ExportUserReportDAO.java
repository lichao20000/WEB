
package com.linkage.module.itms.report.dao;

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
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2015-1-8
 * @category com.linkage.module.itms.report.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
@SuppressWarnings("unchecked")
public class ExportUserReportDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ExportUserReportDAO.class);
	
	

	public Map<String, String> cityMap = null;

	public List<Map> queryUserByImportLOID(List<String> loidList, String fileName,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryUserByImportLOID()");
		PrepareSQL pSQL = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.loid, a.net_account,a.itv_account,a.voip_account,a.city_id from tab_user_servaccount a,tab_temporary_user_report b ");
		sql.append("where a.loid=b.loid and b.filename='" + fileName+"'");
		pSQL.setSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list =  querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{

					@Override
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("content", rs.getString("loid"));
						String city_id = rs.getString("city_id");
						String city_name = StringUtil.getStringValue(cityMap.get(city_id));
						if (!StringUtil.IsEmpty(city_name))
						{
							map.put("city_name", city_name);
						}
						else
						{
							map.put("city_name", "");
						}
						map.put("loid", rs.getString("loid"));
						map.put("net_account", rs.getString("net_account"));
						map.put("itv_account", rs.getString("itv_account"));
						map.put("voip_account", rs.getString("voip_account"));
						return map;
					}
					
				});
		cityMap = null;
		return list;
	}

	public List<Map> queryUserByImportNet_account(List<String> net_accountList,
			String fileName, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryUserByImportNet_account()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("select a.loid, a.net_account,a.itv_account,a.voip_account,a.city_id   from tab_user_servaccount a,tab_temporary_user_report b ");
		pSQL.append("where a.net_account=b.net_account and b.filename='" + fileName+"'");
		cityMap = CityDAO.getCityIdCityNameMap();
		return querySP(pSQL.toString(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{

					@Override
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("content", rs.getString("net_account"));
						String city_id = rs.getString("city_id");
						String city_name = StringUtil.getStringValue(cityMap.get(city_id));
						if (!StringUtil.IsEmpty(city_name))
						{
							map.put("city_name", city_name);
						}
						else
						{
							map.put("city_name", "");
						}
						map.put("loid", rs.getString("loid"));
						map.put("net_account", rs.getString("net_account"));
						map.put("itv_account", rs.getString("itv_account"));
						map.put("voip_account", rs.getString("voip_account"));
						return map;
					}

				});
	}

	public List<Map> queryUserByImportItv_account(List<String> itv_accountList,
			String fileName, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryUserByImportNet_account()");
		PrepareSQL pSQL = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.loid, a.net_account,a.itv_account,a.voip_account,a.city_id from tab_user_servaccount a,tab_temporary_user_report b ");
		sql.append("where a.itv_account=b.itv_account and b.filename='" + fileName+"'");
		pSQL.setSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list =  querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{

					@Override
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("content", rs.getString("itv_account"));
						String city_id = rs.getString("city_id");
						String city_name = StringUtil.getStringValue(cityMap.get(city_id));
						if (!StringUtil.IsEmpty(city_name))
						{
							map.put("city_name", city_name);
						}
						else
						{
							map.put("city_name", "");
						}
						map.put("loid", rs.getString("loid"));
						map.put("net_account", rs.getString("net_account"));
						map.put("itv_account", rs.getString("itv_account"));
						map.put("voip_account", rs.getString("voip_account"));
						return map;
					}
					
				});
		cityMap = null;
		return list;
	}

	public List<Map> queryUserByImportVoip_account(List<String> voip_accountList,
			String fileName, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryUserByImportVoip_account()");
		PrepareSQL pSQL = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.loid, a.net_account,a.itv_account,a.voip_account,a.city_id from tab_user_servaccount a,tab_temporary_user_report b ");
		sql.append("where a.voip_account=b.voip_account and b.filename='" + fileName+"'");
		pSQL.setSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list =  querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{

					@Override
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("content", rs.getString("voip_account"));
						String city_id = rs.getString("city_id");
						String city_name = StringUtil.getStringValue(cityMap.get(city_id));
						if (!StringUtil.IsEmpty(city_name))
						{
							map.put("city_name", city_name);
						}
						else
						{
							map.put("city_name", "");
						}
						map.put("loid", rs.getString("loid"));
						map.put("net_account", rs.getString("net_account"));
						map.put("itv_account", rs.getString("itv_account"));
						map.put("voip_account", rs.getString("voip_account"));
						return map;
					}
					
				});
		cityMap = null;
		return list;
	}

	public int queryUserByLoidCount(String fileName,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryUserByImportLOID()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("select count(*) from tab_user_servaccount a,tab_temporary_user_report b ");
		pSQL.append("where a.loid=b.loid and b.filename='" + fileName+"'");
		int total = jt.queryForInt(pSQL.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public int queryUserByNetCount(String fileName,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryUserByImportLOID()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("select count(*) from tab_user_servaccount a,tab_temporary_user_report b ");
		pSQL.append("where a.net_account=b.net_account and b.filename='" + fileName+"'");
		int total = jt.queryForInt(pSQL.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public int queryUserByItvCount( String fileName,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryUserByImportLOID()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("select count(*) from tab_user_servaccount a,tab_temporary_user_report b ");
		pSQL.append("where a.itv_account=b.itv_account and b.filename='" + fileName+"'");
		int total = jt.queryForInt(pSQL.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public int queryUserByVoipCount(String fileName,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryUserByImportLOID()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("select count(*) from tab_user_servaccount a,tab_temporary_user_report b ");
		pSQL.append("where a.voip_account=b.voip_account and b.filename='" + fileName+"'");
		int total = jt.queryForInt(pSQL.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public void insertTmp(String fileName, List<String> dataList, String importQueryField)
	{
		ArrayList<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = null;
		if ("loid".equals(importQueryField))
		{
			for (int i = 0; i < dataList.size(); i++)
			{
				psql = new PrepareSQL();
				psql.append("insert into tab_temporary_user_report" + "(filename,loid)"
						+ " values ('" + fileName + "','" + dataList.get(i) + "')");
				sqlList.add(psql.getSQL());
			}
		}
		else if ("net_account".equals(importQueryField))
		{
			for (int i = 0; i < dataList.size(); i++)
			{
				psql = new PrepareSQL();
				psql.append("insert into tab_temporary_user_report" + "(filename,net_account)"
						+ " values ('" + fileName + "','" + dataList.get(i) + "')");
				sqlList.add(psql.getSQL());
			}
		}
		else if ("itv_account".equals(importQueryField))
		{
			for (int i = 0; i < dataList.size(); i++)
			{
				psql = new PrepareSQL();
				psql.append("insert into tab_temporary_user_report" + "(filename,itv_account)"
						+ " values ('" + fileName + "','" + dataList.get(i) + "')");
				sqlList.add(psql.getSQL());
			}
		}
		else if ("voip_account".equals(importQueryField))
		{
			for (int i = 0; i < dataList.size(); i++)
			{
				psql = new PrepareSQL();
				psql.append("insert into tab_temporary_user_report" + "(filename,voip_account)"
						+ " values ('" + fileName + "','" + dataList.get(i) + "')");
				sqlList.add(psql.getSQL());
			}
		}
		int res;
		if (LipossGlobals.inArea(Global.JSDX))
		{
			res = DBOperation.executeUpdate(sqlList, "proxool.xml-report");
		}
		else
		{
			res = DBOperation.executeUpdate(sqlList);
		}
	}
	public List<Map> getUserExcel(String fileName, final String importQueryField){
		PrepareSQL pSQL = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		cityMap = CityDAO.getCityIdCityNameMap();
		if("loid".equals(importQueryField)){
			sql.append("select a.loid, a.net_account,a.itv_account,a.voip_account,a.city_id from tab_user_servaccount a,tab_temporary_user_report b ");
			sql.append("where a.loid=b.loid and b.filename='" + fileName+"'");
			pSQL.setSQL(sql.toString());
		}else if("net_account".equals(importQueryField)){
			pSQL.setSQL("select a.loid, a.net_account,a.itv_account,a.voip_account,a.city_id   from tab_user_servaccount a,tab_temporary_user_report b ");
			pSQL.append("where a.net_account=b.net_account and b.filename='" + fileName+"'");
		}else if("itv_account".equals(importQueryField)){
			pSQL.setSQL("select a.loid, a.net_account,a.itv_account,a.voip_account,a.city_id   from tab_user_servaccount a,tab_temporary_user_report b ");
			pSQL.append("where a.itv_account=b.itv_account and b.filename='" + fileName+"'");
		}else if("voip_account".equals(importQueryField)){
			pSQL.setSQL("select a.loid a.net_account,a.itv_account,a.voip_account,a.city_id   from tab_user_servaccount a,tab_temporary_user_report b ");
			pSQL.append("where a.voip_account=b.voip_account and b.filename='" + fileName+"'");
		}
		List<Map> list = jt.query(pSQL.getSQL(), new RowMapper(){

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("content", rs.getString(importQueryField));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (!StringUtil.IsEmpty(city_name))
				{
					map.put("city_name", city_name);
				}
				else
				{
					map.put("city_name", "");
				}
				map.put("loid", rs.getString("net_account"));
				map.put("net_account", rs.getString("net_account"));
				map.put("itv_account", rs.getString("itv_account"));
				map.put("voip_account", rs.getString("voip_account"));
				return map;
			}
			
		});
		cityMap = null;
		return list;
	}
	
	public int getTemporaryNameEx(String fileName){
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("select count(*) from tab_temporary_user_report a ");
		pSQL.append("where a.filename='" + fileName+"'");
		int count = jt.queryForInt(pSQL.getSQL());
		return count;
	}
	
}
