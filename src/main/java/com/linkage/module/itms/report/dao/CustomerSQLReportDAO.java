package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.litms.common.util.JdbcTemplateExtend;

@SuppressWarnings("unchecked")
public class CustomerSQLReportDAO 
{
	public static Logger logger = LoggerFactory.getLogger(CustomerSQLReportDAO.class);
	private JdbcTemplateExtend jt;
	
	/**
	 * @param dao
	 */
	public void setDao(DataSource dao) 
	{
		jt = new JdbcTemplateExtend(dao);
	}
	
	/**
	 * 
	 * @param custSQL
	 * @return
	 */
	public List<Map<String,String>> queryAllResult(String custSQL) 
	{
		logger.debug("queryAllResult({})",new Object[]{custSQL});
		logger.info(custSQL);
		return jt.queryForList(custSQL);
	}
	
	/**
	 * 
	 * @param custSQL
	 * @return
	 */
	public int queryAllResultCount(String custSQL) 
	{
		logger.debug("queryAllResult({})",new Object[]{custSQL});
		logger.info(custSQL);
		return jt.queryForInt(custSQL);
	}
	
	/**
	 * 
	 * @param custSQL
	 * @return
	 */
	public List<Map> queryAllResult(int curPage_splitPage,int num_splitPage,String custSQL) 
	{
		logger.debug("queryAllResult({})",new Object[]{custSQL});
		logger.info(custSQL);
		return jt.querySP(custSQL, (curPage_splitPage - 1) * num_splitPage,num_splitPage, 
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						return resultSet2Map(map, rs);
					}
		});
	}
	
	/**
	 * 数据转换
	 * @param map
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	public Map<String, String> resultSet2Map(Map<String, String> map,ResultSet rs)
	{
		try{
			ResultSetMetaData rsmd = rs.getMetaData() ;

			int columnsCount = rsmd.getColumnCount() ;
			for(int i=1;i<=columnsCount;i++){
				String columnName = rsmd.getColumnName(i);
				map.put(columnName,rs.getString(columnName));
			}
		}catch(SQLException e){
			logger.error(e.getMessage());
		}
		return map;
	}
}
