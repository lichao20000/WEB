package com.linkage.module.gtms.stb.resource.dao;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author wangyan10(Ailk NO.76091)
 * @version 1.0
 * @since 2018-2-5
 */
public class StbChangeDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(StbChangeDAO.class);
	private int queryCount;
	
	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}
	
	private static String writeLogSql = "insert into tab_oper_log("
			+ "acc_oid,acc_login_ip,operationlog_type,operation_time,operation_name"
			+ ",operation_object,operation_content,operation_device"
			+ ",operation_result,result_id) values(?,?,?,?,?,?,?,?,?,?)";

	/**
	 * 获取用户列表
	 * @param
	 * @param servAccount
	 * @param deviceMac
	 * @param cityId 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public List<Map<String, String>> getMac(String servAccount, String deviceMac, String cityId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select a.customer_id,a.serv_account,a.cpe_mac,a.city_id from  stb_tab_customer a  where  1=1  "  );
		if ((!StringUtil.IsEmpty(deviceMac)) && (!"".equals(deviceMac)))
		{
			sql.append(" and a.cpe_mac like '%").append(deviceMac).append("'");
		}
		if ((!StringUtil.IsEmpty(servAccount)) && (!"".equals(servAccount)))
		{
			sql.append(" and a.serv_account like '%").append(servAccount).append("%'");
		}
		
		List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
		sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
		cityIdList = null;
		
		logger.info(sql.toString());
		List rs = jt.queryForList(sql.toString());

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		for (int i = 0; i < rs.size(); i++) {
			Map one = (Map) rs.get(i);
			Map<String, String> map = new HashMap<String, String>();
			map.put("customer_id", StringUtil.getStringValue(one.get("customer_id")));
			map.put("cpe_mac", StringUtil.getStringValue(one.get("cpe_mac")));
			map.put("serv_account", StringUtil.getStringValue(one.get("serv_account")));
			String cityid = StringUtil.getStringValue(one.get("city_id"));
			map.put("city_id", cityid);
			map.put("cityName", cityMap.get(cityid));
			result.add(map);
		}
		return result;
	}
	
//	@SuppressWarnings("unchecked")
//	public List<Map<String, String>> getMac(String servAccount, String deviceMac) {
//		StringBuffer sql = new StringBuffer();
//		sql.append("select a.customer_id,a.serv_account,a.cpe_mac,a.city_id from  stb_tab_customer a  where  1=1  "  );
//		if ((!StringUtil.IsEmpty(deviceMac)) && (!"".equals(deviceMac)))
//		{
//			sql.append(" and a.cpe_mac like '%").append(deviceMac).append("'");
//		}
//		if ((!StringUtil.IsEmpty(servAccount)) && (!"".equals(servAccount)))
//		{
//			sql.append(" and a.serv_account like '%").append(servAccount).append("%'");
//		}
//		
//		PrepareSQL psql = new PrepareSQL(sql.toString());
//		@SuppressWarnings("rawtypes")
//		List list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage,
//				num_splitPage, new RowMapper()
//				{
//					public Object mapRow(ResultSet rs, int arg1) throws SQLException
//					{
//						Map<String, String> map = new HashMap<String, String>();
//						String city_name = "";
//						map.put("customer_id", rs.getString("customer_id"));
//						map.put("cpe_mac", rs.getString("cpe_mac"));
//						map.put("serv_account", rs.getString("serv_account"));
//						String cityid = StringUtil.getStringValue(rs.getString("city_id"));
//						map.put("city_id", cityid);
//						if (!StringUtil.IsEmpty(cityid))
//						{
//							city_name = StringUtil.getStringValue(CityDAO.getCityIdCityNameMap()
//									.get(cityid));
//						}
//						map.put("cityName", city_name);
//						return map;
//					}
//				});
//		return list;
//	}

	public int countMac(int curPage_splitPage, int num_splitPage,
			String servAccount, String deviceMac) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from  stb_tab_customer a  where  1=1  "  );
		if ((!StringUtil.IsEmpty(deviceMac)) && (!"".equals(deviceMac)))
		{
			sql.append(" and a.cpe_mac like '%").append(deviceMac).append("'");
		}
		if ((!StringUtil.IsEmpty(servAccount)) && (!"".equals(servAccount)))
		{
			sql.append(" and a.serv_account like '%").append(servAccount).append("%'");
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		queryCount = total;
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

	public Map<String, String> queryCustomer(String city_id, String servAccount,
			String deviceMac,String oldMac) {
		String sql = "select cpe_mac,city_id from stb_tab_customer where serv_account=? " +
				"and 0=(select count(*) from stb_tab_customer where cpe_mac=? ) ";
		
		if(!StringUtil.IsEmpty(oldMac) && "NULL".equals(oldMac)){
			sql+="and cpe_mac='"+oldMac+"' ";
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1,servAccount);
		psql.setString(2,deviceMac);
		List<Map<String,String>> resultList = jt.queryForList(psql.getSQL());
		if(null != resultList && resultList.size()>0)
		{
			return resultList.get(0);
		}
		return null;
	}

	public void addLogUnBind(long user_id, String user_ip, String cityId,
			String deviceMac, String servAccount, String ajax,
			Map<String, String> map) 
	{
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(writeLogSql);
		psql.setLong(1,user_id);
		psql.setString(2,user_ip);
		psql.setInt(3,1);
		psql.setLong(4,System.currentTimeMillis()/1000L);
		psql.setString(5,"5");
		psql.setString(6,"WEB");
		
		StringBuffer sb=new StringBuffer();
		sb.append("用户["+servAccount+"]更换机顶盒信息：[");
		if(map!=null && !map.isEmpty()){
			sb=combinStr(sb,"属地",map.get("city_id"),cityId);
			sb=combinStr(sb,"cpe_mac",map.get("cpe_mac"),deviceMac);
		}
		sb.append("]");
		
		psql.setString(7,sb.toString());
		psql.setString(8,"Web");
		
		psql.setString(9,"成功".equals(ajax)?"成功":"失败");
		psql.setInt(10,1);
		DBOperation.executeUpdate(psql.getSQL());
		
	}
	
	/**
	 * 组装参数
	 */
	public StringBuffer combinStr(StringBuffer sb,String str_name,String old_data,String new_data){
		if(sb.toString().indexOf("->")!=-1){
			sb.append("，");
		}
		
		if(!StringUtil.IsEmpty(new_data)){
			if(StringUtil.IsEmpty(old_data)){
				sb.append(str_name+"： ->"+new_data);
			}else if(!new_data.equals(old_data)){
				sb.append(str_name+"："+old_data+"->"+new_data);
			}else{
				sb.deleteCharAt(sb.toString().length()-1);
			}
		}else{
			if(!StringUtil.IsEmpty(old_data)){
				sb.append(str_name+"："+old_data+"->  ");
			}else{
				sb.deleteCharAt(sb.toString().length()-1);
			}
		}
		
		return sb;
	}
}
