package com.linkage.module.itms.report.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class ZeroConfigurationSuccessRateDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(ZeroConfigurationSuccessRateDAO.class);

	/**
	 * 统计新装工单总数量
	 * 
	 * @param starttime1
	 * @param cityId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public Map<String, String> getOperUnOkNum(String cityId) 
	{
		logger.debug("getOperNoNum({},{},{},{})", new Object[] { cityId });

		Map<String, String> map = new HashMap<String, String>();

		StringBuffer sql = new StringBuffer();
		sql.append("select area_name,count(ktgd_cnt) cnt from test_cnt_all ");
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			String cityName = "";
			for (int i = 0; i < cityIdList.size(); i++) {
				if (i == cityIdList.size() - 1) {
					cityName += "'"
							+ CityDAO.getCityName(cityIdList.get(i).toString())
							+ "'";
				} else {
					cityName += "'"
							+ CityDAO.getCityName(cityIdList.get(i).toString())
							+ "'" + ",";
				}
			}
			sql.append(" and area_name in (").append(cityName).append(")");
			cityIdList = null;
		}
		sql.append(" group by area_name");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(CityDAO.getCityId(rmap.get("area_name").toString())),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}
	
	public ArrayList<HashMap<String,String>> getResultByCityId(String time, String cityId)
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select city_name,detotal,nototal,percent ");
		}else{
			psql.append("select * ");
		}
		psql.append("from view_zero_peizhi_proc where count_month='" + time + "'");
		if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			String cityName = CityDAO.getCityName(cityId);
			psql.append(" and 地市='"+cityName.substring(0,2) + "'");
		}
		
		return getRecords(psql.getSQL(),null);
	}
	
	private  ArrayList<HashMap<String, String>> getRecords(String sql, String dbName) 
	{
		logger.debug("getRecords({},{})", sql, dbName);

		ArrayList<HashMap<String, String>> list = null;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ResultSetMetaData metadata = null;
		String value = "";

		try {
			// for multi DataSource, modify by chenjie 2013-12-26
			conn = jt.getDataSource().getConnection();
			if (conn == null) {
				logger.debug("conn == null");
				return list;
			}

			list = new ArrayList<HashMap<String, String>>(100);
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				metadata = rs.getMetaData();
				HashMap<String, String> fields = new HashMap<String, String>();
				for (int i = 1; i <= metadata.getColumnCount(); i++) {
					value = rs.getString(metadata.getColumnLabel(i));
					if (null != value) {
						fields.put(metadata.getColumnLabel(i).toLowerCase(),
								value.trim());
					}
				}
				list.add(fields);
			}
		} catch (SQLException e1) {
			logger.error("SQLException:{}\n{}", new Object[] { e1.getMessage(),
					sql });
		} catch (Exception ex) {
			logger.error("SQLException:{}\n{}", new Object[] { ex.getMessage(),
					sql });
		} finally {
			metadata = null;
			try {
				if (null != rs) {
					rs.close();
					rs = null;
				}
			} catch (SQLException e) {
				logger.error("SQLException:{}", e.getMessage());
			}
			
			try {
				if (null != pst) {
					pst.close();
					pst = null;
				}
			} catch (SQLException e) {
				logger.error("SQLException:{}", e.getMessage());
			}

			try {
				conn.close();
				conn = null;
			} catch (Exception e) {
				logger.error("Exception:close connection,{}", e.getMessage());
			}
		}

		return list;
	}

	/**
	 * 统计失败工单总数
	 * 
	 * @param starttime1
	 * @param cityId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public Map<String, String> getOperOkNum(String cityId) 
	{
		logger.debug("getOperOkNum({},{},{},{})", new Object[] {cityId });

		Map<String, String> map = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select area_name,count(*) cnt ");
		}else{
			psql.append("select area_name,count(1) cnt ");
		}
		psql.append("from test_cnt_all where failure_count<>0 ");
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			String cityName = "";
			for (int i = 0; i < cityIdList.size(); i++) {
				if (i == cityIdList.size() - 1) {
					cityName += "'"
							+ CityDAO.getCityName(cityIdList.get(i).toString())
							+ "'";
				} else {
					cityName += "'"
							+ CityDAO.getCityName(cityIdList.get(i).toString())
							+ "'" + ",";
				}
			}
			psql.append(" and area_name in ("+cityName+")");
			cityIdList = null;
		}
		psql.append(" group by area_name");
		
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(CityDAO.getCityId(rmap.get("area_name").toString())),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	public long executeProcedure(String starttime1, String cityId) 
	{
		long result = -1;
		String sql ="{call itms_zero_peizhi_proc(?,?)}";   
		//第一步  清空中间表
		jt.execute("truncate table test_cnt_all");
		//第二步  执行存储过程向中间表插入数据  test itms_zero_peizhi_proc('2013-09')
		CallableStatement cstmt = null;
		Connection conn = null;
		try
		{
			logger.warn("入参：" + starttime1);
			conn = jt.getDataSource().getConnection();
//			conn = DBOperation.getDBConn("xml-report");
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, starttime1);
			cstmt.registerOutParameter(2, Types.INTEGER);
			cstmt.execute();
			result = cstmt.getLong(2);
			logger.warn("执行完成："+ result);
		}
		catch (SQLException e)
		{
			logger.error("executeProcedure Exception:{}", e.getMessage());
			logger.error("错误：" + e);
			e.printStackTrace();
		}
		finally {
			sql = null;

			if (cstmt != null) {
				try {
					cstmt.close();
				} catch (SQLException e) {
					logger.error("cstmt.close SQLException:{}", e.getMessage());
				}
				cstmt = null;
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					logger.error("conn.close error:{}", e.getMessage());
				}

				conn = null;
			}
		}
		return result;
	}
}
