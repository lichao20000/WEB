
package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings({ "rawtypes" })
public class OpenBusinessDAO extends SuperDAO
{


	/**
	 * 查询资源家庭用户表，需要device_id不为空!
	 * 
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List queryPrepareUserByTimeTotal(String starttime, String endtime)
	{
		String sql = "select a.city_id,count(distinct(a.username)) as total from tab_hgwcustomer a where a.device_id is not null"
				+ " and a.binddate > "
				+ starttime
				+ " and a.binddate < "
				+ endtime
				+ " group by city_id";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list = jt.queryForList(psql.getSQL());
		return list;
	}

	/**
	 * 查询成功的总数
	 */
	@SuppressWarnings("unchecked")
	public List queryPrepareUserByTimeTotalSuccess(String starttime, String endtime)
	{
		String sql = "select a.city_id,count(distinct(a.username)) as total from tab_hgwcustomer a  where a.device_id is not null and  a.binddate > "
				+ starttime
				+ " and a.binddate < "
				+ endtime
				+ " and  a.user_id not in (select distinct user_id from hgwcust_serv_info where opendate > "
				+ starttime
				+ " and opendate < "
				+ endtime
				+ " and open_status != 1) group by a.city_id";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list = jt.queryForList(psql.getSQL());
		return list;
	}

	/**
	 * 查询失败数的明细
	 * 
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List queryPrepareUserByTimeTotalFail(String starttime, String endtime)
	{
		String sql = "select a.city_id,count(distinct(a.username)) as total from tab_hgwcustomer a,hgwcust_serv_info b where a.device_id is not null and a.user_id = b.user_id and b.open_status < 1 and a.binddate > "
				+ starttime
				+ " and a.binddate < "
				+ endtime
				+ " and b.opendate> "
				+ starttime + " and b.opendate< " + endtime + " group by a.city_id";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list = jt.queryForList(psql.getSQL());
		return list;
	}

	/**
	 * 查询总数的详细信息，下面使用的sql肯定和上面是不同的！
	 * 
	 * @param city_id_list
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> queryDetailByTimeTotalExcel(List<String> city_id_list,
			String starttime, String endtime)
	{
		if (city_id_list.size() == 0 || city_id_list == null)
			return null;
		String city_str = "a.city_id=" + city_id_list.get(0);
		for (int i = 1; i < city_id_list.size(); i++)
		{
			city_str += " or a.city_id=" + city_id_list.get(i);
		}
		String sql = "select distinct a.username,b.device_serialnumber,a.city_id,a.binddate from "
				+ "tab_hgwcustomer a,tab_gw_device b where a.device_id = b.device_id"
				+ " and a.device_id is not null and "
				+ "("
				+ city_str
				+ ")"
				+ " and a.binddate > " + starttime + " and a.binddate < " + endtime;
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list = jt.queryForList(psql.getSQL());
		return list;
	}

	/**
	 * 查询失败的明细
	 * 
	 * @param list
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> queryDetailByTimeSuccessExcel(ArrayList<String> city_id_list,
			String starttime, String endtime)
	{
		if (city_id_list.size() == 0 || city_id_list == null)
			return null;
		String city_str = "a.city_id=" + city_id_list.get(0);
		for (int i = 1; i < city_id_list.size(); i++)
		{
			city_str += " or a.city_id=" + city_id_list.get(i);
		}
		/** 特别注意包含city_str的语句必须加小括号，不然语句是错的，因为所有的city_id将会和前面的and并列 */
		String sql = "select distinct a.username ,b.device_serialnumber,a.city_id,a.binddate from "
				+ "tab_hgwcustomer a,tab_gw_device b where a.device_id = b.device_id "
				+ "and a.user_id not in (select distinct user_id from hgwcust_serv_info where opendate > "
				+ starttime
				+ " and opendate < "
				+ endtime
				+ " and open_status!=1) and a.device_id is not null and "
				+ "("
				+ city_str
				+ ")" + " and binddate > " + starttime + " and binddate < " + endtime;
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list = jt.queryForList(psql.getSQL());
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Map> queryDetailByTimeFailExcel(ArrayList<String> city_id_list,
			String starttime, String endtime)
	{
		if (city_id_list.size() == 0 || city_id_list == null)
			return null;
		String city_str = "a.city_id=" + city_id_list.get(0);
		for (int i = 1; i < city_id_list.size(); i++)
		{
			city_str += " or a.city_id=" + city_id_list.get(i);
		}
		
		/** 特别注意包含city_str的语句必须加小括号，不然语句是错的，因为所有的city_id将会和前面的and并列 */
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		String sql = "select distinct a.username,b.device_id_ex,b.device_serialnumber,a.city_id,a.binddate "
				+ "from tab_hgwcustomer a,tab_gw_device b,hgwcust_serv_info c "
				+ "where a.device_id = b.device_id and a.user_id = c.user_id and a.device_id is not null "
				+ "and ("+ city_str+ ")"
				+ " and c.open_status < 1"
				+ " and a.binddate > "+ starttime + " and a.binddate < "+ endtime
				+ " and c.opendate > "+ starttime + " and c.opendate < " + endtime;
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list = jt.queryForList(psql.getSQL());
		return list;
	}

	/**
	 * 获取总数的明细
	 * 
	 * @param city_id_list
	 * @param starttime
	 *            :Unix时间戳的开始时间值
	 * @param endtime
	 *            :Unix时间戳的结束时间值
	 * @return
	 */
	private String sqlDetailByTimeTotal(List<String> city_id_list, String starttime,
			String endtime)
	{
		if (city_id_list.size() == 0 || city_id_list == null)
			return null;
		String city_str = "a.city_id=" + city_id_list.get(0);
		for (int i = 1; i < city_id_list.size(); i++)
		{
			city_str += " or a.city_id=" + city_id_list.get(i);
		}
		/** 特别注意包含city_str的语句必须加小括号，不然语句是错的，因为所有的city_id将会和前面的and并列 */
		/* 不能加入a.city_name，因为需要查询另一张表！ */
		String sql = "select distinct a.username,b.device_serialnumber,a.city_id,a.binddate from "
				+ "tab_hgwcustomer a,tab_gw_device b where a.device_id = b.device_id"
				+ " and a.device_id is not null and "
				+ "("
				+ city_str
				+ ")"
				+ " and a.binddate > " + starttime + " and a.binddate < " + endtime;
		return new PrepareSQL(sql).getSQL();
	}

	/**
	 * 获取成功数的明细
	 * 
	 * @param city_id_list
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	private String sqlDetailByTimeSuccess(List<String> city_id_list, String starttime,
			String endtime)
	{
		if (city_id_list.size() == 0 || city_id_list == null)
			return null;
		String city_str = "a.city_id=" + city_id_list.get(0);
		for (int i = 1; i < city_id_list.size(); i++)
		{
			city_str += " or a.city_id=" + city_id_list.get(i);
		}
		/** 特别注意包含city_str的语句必须加小括号，不然语句是错的，因为所有的city_id将会和前面的and并列 */
		String sql = "select distinct a.username,b.device_serialnumber,a.city_id,a.binddate from "
				+ "tab_hgwcustomer a,tab_gw_device b where a.device_id = b.device_id "
				+ " and a.device_id is not null and a.user_id not in (select distinct user_id from hgwcust_serv_info where opendate > "
				+ starttime
				+ " and opendate < "
				+ endtime
				+ " and open_status!=1) and "
				+ "("
				+ city_str
				+ ")"
				+ " and a.binddate > "
				+ starttime
				+ " and a.binddate < " + endtime;
		PrepareSQL psql = new PrepareSQL(sql);
		return psql.getSQL();
	}

	/**
	 * 获取失败数的明细
	 * 
	 * @param city_id_list
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	private String sqlDetailByTimeFail(List<String> city_id_list, String starttime,
			String endtime)
	{
		if (city_id_list.size() == 0 || city_id_list == null)
			return null;
		String city_str = "a.city_id=" + city_id_list.get(0);
		for (int i = 1; i < city_id_list.size(); i++)
		{
			city_str += " or a.city_id=" + city_id_list.get(i);
		}
		
		/** 特别注意包含city_str的语句必须加小括号，不然语句是错的，因为所有的city_id将会和前面的and并列 */
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		String sql = "select distinct a.username,b.device_serialnumber,a.city_id,a.binddate "
				+ "from tab_hgwcustomer a,tab_gw_device b,hgwcust_serv_info c "
				+ "where a.device_id = b.device_id and a.user_id = c.user_id and a.device_id is not null "
				+ "and ("+ city_str+ ")"
				+ " and c.open_status < 1 " 
				+ " and c.opendate > " + starttime + " and c.opendate < " + endtime
				+ " and a.binddate > " + starttime + " and a.binddate < " + endtime;
		PrepareSQL psql = new PrepareSQL(sql);
		return psql.getSQL();
	}

	/**
	 * 获取当前页的详细信息
	 * 
	 * @param dataType
	 * @param citylist
	 * @param starttime
	 * @param endtime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	private Map<String, String> cityMap = null;

	public List<Map> queryDetailListPage(String dataType, ArrayList<String> citylist,
			String starttime, String endtime, int curPage_splitPage, int num_splitPage)
	{
		String sql = "";
		if (dataType.equals("1"))
		{
			sql = sqlDetailByTimeTotal(citylist, starttime, endtime);
		}
		else if (dataType.equals("2"))
		{
			sql = sqlDetailByTimeSuccess(citylist, starttime, endtime);
		}
		else
		{
			sql = sqlDetailByTimeFail(citylist, starttime, endtime);
		}
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(sql, (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{

					@Override
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						String city_id = rs.getString("city_id");
						String city_name = StringUtil.getStringValue(cityMap,city_id,"");
						map.put("city_name", city_name);
						/** 逻辑SN，其实就是LOID **/
						map.put("username", rs.getString("username"));
						map.put("device_serialnumber",
								rs.getString("device_serialnumber"));
						/** 将Unix时间戳转化为本地时间 */
						long binddata = StringUtil.getLongValue(rs.getString("binddate"));
						String date = new java.text.SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss").format(new java.util.Date(
								binddata * 1000));
						map.put("binddate", date.toString());
						return map;
					}
				});
		return list;
	}

	/**
	 * 获取总的数量,分页所用
	 * 
	 * @param dataType
	 * @param citylist
	 * @param starttime
	 * @param endtime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int queryDetailCount(String dataType, ArrayList<String> city_id_list,
			String starttime, String endtime, int curPage_splitPage, int num_splitPage)
	{
		PrepareSQL psql = null;
		if (city_id_list.size() == 0 || city_id_list == null)
			return 0;
		String city_str = "a.city_id=" + city_id_list.get(0);
		for (int i = 1; i < city_id_list.size(); i++)
		{
			city_str += " or a.city_id=" + city_id_list.get(i);
		}
		if (dataType.equals("1"))
		{
			/** 拼接SQL,注意需要使用distinct去重 **/
			String sql = "select count(distinct(a.username)) from "
					+ "tab_hgwcustomer a,tab_gw_device b where a.device_id = b.device_id "
					+ " and a.device_id " + "is not null and " + "(" + city_str + ")"
					+ " and a.binddate > " + starttime + " and a.binddate < " + endtime;
			psql = new PrepareSQL(sql);
		}
		else if (dataType.equals("2"))
		{
			String sql = "select count(distinct(a.username)) from "
					+ "tab_hgwcustomer a,tab_gw_device b where a.device_id = b.device_id "
					+ "and a.user_id not in (select distinct user_id from hgwcust_serv_info where opendate > "
					+ starttime + " and opendate < " + endtime
					+ " and open_status!=1) and a.device_id is not null and " + "("
					+ city_str + ")" + " and a.binddate > " + starttime
					+ " and a.binddate < " + endtime;
			psql = new PrepareSQL(sql);
		}
		else
		{
			/**	if(DBUtil.GetDB()==3){
				//TODO wait
			}else{
				
			}*/
			
			String sql = "select count(distinct(a.username)) "
					+ "from tab_hgwcustomer a,tab_gw_device b,hgwcust_serv_info c "
					+ "where a.device_id = b.device_id and a.user_id = c.user_id and a.device_id is not null "
					+ "and (" + city_str + ")" + " and c.open_status < 1 " 
					+ " and a.binddate > " + starttime + " and a.binddate < " + endtime 
					+ " and c.opendate > " + starttime + " and c.opendate < " + endtime;
			psql = new PrepareSQL(sql);
		}
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 0;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
}
