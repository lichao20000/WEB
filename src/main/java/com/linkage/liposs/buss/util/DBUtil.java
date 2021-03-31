package com.linkage.liposs.buss.util;

import java.sql.SQLDataException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 数据库查询分页查询使用，主要针对有分页功能需要的DAO调用
 * 
 * @author 王志猛 5194,suixz(5253) tel(13512508857)
 * @since 2008-1-3
 * @category util
 * 
 */
public class DBUtil
{
	/**
	 * 获取指定页码的数据集合，返回list<Map>的数据集合。内部为制定的sql的列
	 * 
	 * @param sql
	 *            查询数据的sql语句
	 * @param curPage
	 *            要获取的页码
	 * @param rowCount
	 *            每页的行数
	 * @param ds
	 *            数据源
	 * @return List<Map>的结果结合
	 * @throws SQLDataException
	 *             数据库异常
	 */
	public static List<Map> querySPList(String sql, String curPage, String rowCount,
			JdbcTemplate jt) throws SQLDataException
	{
		return jt.queryForList("{call splitpage(?,?,?)}", new Object[]
			{ sql, curPage, rowCount });
	}
	/**
	 * 返回指定查询的最大页数
	 * 
	 * @param sql
	 *            查询数据的sql语句，该语句应返回一个int值结果，例如select count(1) from ......
	 * @param pagerows
	 *            显示结果、每页的行数
	 * @param columName
	 *            所要查询的列名,形如:select count(*) as num ...,这时需传入列名"num"
	 * @param jt
	 *            数据源
	 * @return 计算后的页数
	 * @throws SQLDataException
	 *             数据库异常
	 */
	public static int getMaxPageNum(String sql, int pagerows, String columName,
			JdbcTemplate jt) throws SQLDataException
	{
		int MaxCount = 0;
		List<Map> list = getMaxCountList(sql, jt);
		for (Map m : list)
			{
				MaxCount += Integer.parseInt(m.get(columName).toString());
			}
		return (MaxCount % pagerows == 0) ? (MaxCount / pagerows)
				: (MaxCount / pagerows) + 1;
	}
	/**
	 * 查询记录数量
	 * 
	 * @param sql
	 *            查询语句
	 * @param pagerows
	 *            每页显示的告警数量
	 * @param jt
	 *            数据源
	 * @return
	 */
	private static List<Map> getMaxCountList(String sql, JdbcTemplate jt)
	{
		return jt.queryForList(sql);
	}
}
