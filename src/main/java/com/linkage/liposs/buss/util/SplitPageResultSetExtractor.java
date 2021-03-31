package com.linkage.liposs.buss.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import com.linkage.commons.db.DBUtil;
import com.linkage.module.gwms.Global;

/**
 * 该类是分页查询的结果集合处理类，一般外边不要直接调用该类，为JdbcTemplateExtend.java提供
 * 
 * @author 王志猛(5194) tel：13701409234
 * @version 1.0
 * @since 2008-1-17
 * @category com.linkage.liposs.buss.util 版权：南京联创科技 网管科技部
 * 
 */
public class SplitPageResultSetExtractor implements ResultSetExtractor
{
	private final int start;// 起始行号
	private final int len;// 结果集合的长度
	private final RowMapper rowMapper;// 行包装器
	public SplitPageResultSetExtractor(RowMapper rowMapper, int start, int len)
	{
		Assert.notNull(rowMapper, "RowMapper is required");
		this.rowMapper = rowMapper;
		this.start = start;
		this.len = len;
	}
	/**
	 * 处理结果集合,被接口自动调用，该类外边不应该调用
	 */
	public Object extractData(ResultSet rs) throws SQLException, DataAccessException
	{
		if(DBUtil.GetDB() == Global.DB_MYSQL) {
			return extractMySqlData(rs);
		}
		else {
			List result = new ArrayList();
			int rowNum = 0;
			int end = start + len;
			point: while (rs.next())
				{
					++rowNum;
					if (rowNum < start)
						{
							continue point;
						}
					else
						if (rowNum >= end)
							{
								break point;
							}
						else
							{
								result.add(this.rowMapper.mapRow(rs, rowNum));
							}
				}
			return result;
		}
	}
	
	/**
	 * 处理结果集合,被接口自动调用，该类外边不应该调用
	 */
	public Object extractMySqlData(ResultSet rs) throws SQLException, DataAccessException
	{
		List result = new ArrayList();
		int rowNum = start;
		while (rs.next())
		{
		
			result.add(this.rowMapper.mapRow(rs, rowNum));
			++rowNum;
		}
		return result;
	}
}
