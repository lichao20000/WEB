package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class CheckDeviceDAO extends SuperDAO {
	
	private static Logger logger = LoggerFactory.getLogger(CheckDeviceDAO.class);
	
	private Map<String,String> servTypeMap = null; 
	/**
	 * 返回业务一次下发所有的数据
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */
	public List<Map<String,Object>> getDataList(String cityId, String checkState ,String gwType) 
	{
		logger.debug("getDataList({},{},{})",cityId,checkState);
		String sql = getSql(cityId, checkState, gwType);
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}
	
	private  String getSql(String cityId, String checkState, String gwType)
	{
		StringBuilder sql = new StringBuilder();
		
		if(DBUtil.GetDB() == 3){
			sql.append("select d.city_id,a.checkstate,count(*) num ");
		}else{
			sql.append("select d.city_id,a.checkstate,count(*) num ");
		}
		
		sql.append("  from TAB_gatheross2_base a,tab_gw_device d where  d.device_id = a.device_id");
		
		if (!StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){ // 设备属地
			List<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and d.city_id in (");
			sql.append(StringUtils.weave(cityIdList));
			sql.append(")");
		}
		sql.append(" group by d.city_id,a.checkState");
		
		return sql.toString();
	}
	
	/**
	 *@描述
	 *@参数  [cityId, starttime1, endtime1, servTypeId, curPage_splitPage, num_splitPage, gwType, String]
	 *@返回值  java.util.List<java.util.Map>
	 *@创建人  lsr
	 *@创建时间  2020/2/20
	 *@throws
	 *@修改人和其它信息
	 */
	public List<Map>  getServInfoDetail(String cityId, String checkState,int curPageSplitPage,
			int numSplitPage)
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append(" select v.vendor_name,m.device_model,t.softwareversion,c.city_name,	d.device_serialnumber,d.loopback_ip ");
		sql.append(getBothSQL(cityId, checkState));
		
		List<Map> list = querySP(sql.getSQL(), (curPageSplitPage - 1) * numSplitPage+ 1,
				numSplitPage, new RowMapper() {
				
				public Object mapRow(ResultSet rs, int arg1) throws SQLException{
					
					Map<String, String> map = new HashMap<String, String>();
					
					// 设备逻辑SN
					map.put("vendor_name", rs.getString("vendor_name"));
					// 设备序列号
					map.put("device_model", rs.getString("device_model")); 
					//业务帐号
					map.put("softwareversion", rs.getString("softwareversion"));
					map.put("city_name", rs.getString("city_name"));
					map.put("device_serialnumber", rs.getString("device_serialnumber"));
					map.put("loopback_ip", rs.getString("loopback_ip"));
					
					return map;
				}
			});
		
		return list;
	}
	
	/**
	 * 返回的页数
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param servType
	 * @param curPageSplitPage
	 * @param numSplitPage
	 * @return
	 */
	public int getServInfoCount(String cityId, String checkState,int curPageSplitPage,
			int numSplitPage)
	{
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			sql.append(" select count(*) ");
		}else{
			sql.append(" select count(1) ");
		}
		sql.append(getBothSQL(cityId, checkState));
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int totalPageNum = 1;
		if (total % numSplitPage == 0){
			totalPageNum = total / numSplitPage;
		}else{
			totalPageNum = total / numSplitPage + 1;
		}
		logger.warn("返回值totalPageNum:{}",totalPageNum);
		return totalPageNum;
	}
	
	/**
	 * 业务详细信息的导出数据
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param servType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getServInfoExcel(String cityId, String checkState)
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append(" select v.vendor_name,m.device_model,t.softwareversion,c.city_name,	d.device_serialnumber,d.loopback_ip ");
		sql.append(getBothSQL(cityId, checkState));
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map<String,Object>>  list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				
				Map<String, String> map = new HashMap<String, String>();
				
				map.put("vendor_name", rs.getString("vendor_name"));
				// 设备序列号
				map.put("device_model", rs.getString("device_model")); 
				//业务帐号
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("city_name", rs.getString("city_name"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				return map;
			}
		});
		return list;
	}
	
	/**
	 * 详细sql 有许多查询用到相同的语句。
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */
	public String getBothSQL(String cityId, String checkState)
	{
		 StringBuilder sql = new StringBuilder();
		 
		 sql.append(" from  tab_gw_device d,tab_gatheross2_base b,tab_devicetype_info t,tab_vendor v,gw_device_model m,tab_city c ");
		 sql.append(" where d.device_id = b.device_id ");
		 sql.append(" and d.devicetype_id = t.devicetype_id ");
		 sql.append(" and t.vendor_id = v.vendor_id ");
		 sql.append(" and t.device_model_id = m.device_model_id ");
		 sql.append(" and d.city_id = c.city_id and b.checkState='" + checkState + "'");
		 
		 
		 if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)){
				List<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
				if(cityIdList != null && !cityIdList.isEmpty()){
					sql.append(" and d.city_id in (");
					sql.append(StringUtils.weave(cityIdList));
					sql.append(")");
				}

		}
		return sql.toString();
	}
	
	/**
	 * 业务类型  
	 * @return
	 */
	public HashMap<String,String> getServType(){
		PrepareSQL sql = new PrepareSQL(" select serv_type_id,serv_type_name from tab_gw_serv_type ");
		
		List<Map> list = jt.queryForList(sql.getSQL());

		HashMap<String, String> servTypeMap = new HashMap<String, String>();
		
		for(Map<String, String> map : list){
			
			String servTypeId = StringUtil.getStringValue(map.get("serv_type_id"));
			String servType = StringUtil.getStringValue(map.get("serv_type_name"));
			
		    servTypeMap.put(servTypeId,servType);
		}
		return servTypeMap;
	}

	public List<Map<String,Object>> getStbDataList(String cityId, String starttime1, String endtime1, String gwType)
	{
		StringBuilder sql = new StringBuilder();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("  select a.city_id,a.user_status ,count(*) num ");
		}else{
			sql.append("  select a.city_id,a.user_status ,count(1) num ");
		}
		
		sql.append("  from stb_tab_customer a ,stb_tab_gw_device b ,stb_gw_device_model c,");
		sql.append("  stb_tab_devicetype_info t  ");
		sql.append(" where a.customer_id = b.customer_id and b.devicetype_id = t.devicetype_id ");
		sql.append("and b.device_model_id = c.device_model_id ");
		// ITMS界面宽带、VOIP下发率换算公式修改处
		if (LipossGlobals.isOracle())
		{
			sql.append(" and a.user_status!=0");
		}
		else
		{
			sql.append(" and a.user_status!=0");
		}
		if (!StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.updatetime > " + starttime1);
		}
		if (!StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.updatetime < " + endtime1);
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{ // 设备属地
			List<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			if(cityIdList != null && !cityIdList.isEmpty()){
				sql.append(" and a.city_id in (");
				sql.append(StringUtils.weave(cityIdList));
				sql.append(")");
			}

		}
		sql.append(" group by  a.city_id,a.user_status");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	public List<Map> getServInfoStbDetail(String cityId, String starttime1, String endtime1, 
			String servTypeId, int curPageSplitPage, int numSplitPage, String gwType, String openStatus)
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append(" select a.loid,b.device_serialnumber,b.serv_account username, a.updatetime dealdate,a.user_status open_status" );
		sql.append("  from stb_tab_customer a ,stb_tab_gw_device b ");
		sql.append("  where a.customer_id = b.customer_id and a.user_status !=0 ");
		if (!StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.updatetime > " + starttime1);
		}
		if (!StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.updatetime < " + endtime1);
		}
		if ("success".equals(openStatus)){
			sql.append(" and a.user_status  = 1 ");
		}
		if ("failure".equals(openStatus)){
			sql.append(" and a.user_status  !=1 ");
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{ // 设备属地
			List<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			if(cityIdList != null && !cityIdList.isEmpty()){
				sql.append(" and a.city_id in (");
				sql.append(StringUtils.weave(cityIdList));
				sql.append(")");
			}
		}
		servTypeMap = this.getServType();
		List<Map> list = querySP(sql.getSQL(), (curPageSplitPage - 1) * numSplitPage
				+ 1, numSplitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				// 设备序列号
				map.put("deviceSerialnumber", rs.getString("device_serialnumber"));
				// 业务帐号
				map.put("username", rs.getString("username"));
                map.put("loid", rs.getString("loid"));
                map.put("servType","IPTV");
				// 开通状态
				String open_status = StringUtil.getStringValue(rs
						.getString("open_status"));
				if ("1".equals(open_status))
				{
					map.put("openStatus", "已开通");
				}
				else if ("0".equals(open_status))
				{
					map.put("openStatus", "未开通");
				}
				else
				{
					map.put("openStatus", "开通失败");
				}
				// 受理时间
				try
				{
					long completeTime = StringUtil.getLongValue(rs.getString("dealdate"));
					DateTimeUtil dt = new DateTimeUtil(completeTime * 1000);
					map.put("dealdate", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					map.put("dealdate", "");
				}
				catch (Exception e)
				{
					map.put("dealdate", "");
				}
				return map;
			}
		});
		return list;
	}

	public List<Map<String,Object>> getServInfoStbExcel(String cityId, String starttime1, 
			String endtime1, String servTypeId, String gwType, String openStatus)
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append(" select a.loid,b.device_serialnumber,b.serv_account username, a.updatetime dealdate,a.user_status open_status" );
		sql.append("  from stb_tab_customer a ,stb_tab_gw_device b ");
		sql.append("  where a.customer_id = b.customer_id and a.user_status !=0 ");
		if (!StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.updatetime > " + starttime1);
		}
		if (!StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.updatetime < " + endtime1);
		}
		if ("success".equals(openStatus)){
			sql.append(" and a.user_status = 1 ");
		}
		if ("failure".equals(openStatus)){
			sql.append(" and a.user_status !=1 ");
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{ // 设备属地
			List<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			if(cityIdList != null && !cityIdList.isEmpty()){
				sql.append(" and a.city_id in (");
				sql.append(StringUtils.weave(cityIdList));
				sql.append(")");
			}
		}
		servTypeMap = this.getServType();
		List<Map<String, Object>> list = jt.query(sql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				// 设备序列号
				map.put("deviceSerialnumber", rs.getString("device_serialnumber"));
				// 业务帐号
				map.put("username", rs.getString("username"));
				map.put("loid", rs.getString("loid"));
				map.put("servType","IPTV");
				// 开通状态
				String open_status = StringUtil.getStringValue(rs
						.getString("open_status"));
				if ("1".equals(open_status))
				{
					map.put("openStatus", "已开通");
				}
				else if ("0".equals(open_status))
				{
					map.put("openStatus", "未开通");
				}
				else
				{
					map.put("openStatus", "开通失败");
				}
				// 受理时间
				try
				{
					long completeTime = StringUtil.getLongValue(rs.getString("dealdate"));
					DateTimeUtil dt = new DateTimeUtil(completeTime * 1000);
					map.put("dealdate", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					map.put("dealdate", "");
				}
				catch (Exception e)
				{
					map.put("dealdate", "");
				}
				return map;
			}
		});
		return list;
	}

    public int getStbServInfoCount(String cityId, String starttime1, String endtime1, String openStatus)
    {
        PrepareSQL sql = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	//TODO wait
        	 sql.append(" select count(*) ");
        }else{
        	 sql.append(" select count(1) ");
        }
        sql.append("  from stb_tab_customer a ,stb_tab_gw_device b ");
        sql.append("  where a.customer_id = b.customer_id and a.user_status !=0");
        if (!StringUtil.IsEmpty(starttime1))
        {
            sql.append(" and a.updatetime > " + starttime1);
        }
        if (!StringUtil.IsEmpty(endtime1))
        {
            sql.append(" and a.updatetime < " + endtime1);
        }
        if ("success".equals(openStatus)){
            sql.append(" and a.user_status = 1 ");
        }
        if ("failure".equals(openStatus)){
            sql.append(" and a.user_status !=1 ");
        }
        if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
        {
            List<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			if(cityIdList != null && !cityIdList.isEmpty()){
				sql.append(" and a.city_id in (");
				sql.append(StringUtils.weave(cityIdList));
				sql.append(")");
			}
        }
        PrepareSQL psql = new PrepareSQL(sql.toString());
        return jt.queryForInt(psql.getSQL());
	}
}
