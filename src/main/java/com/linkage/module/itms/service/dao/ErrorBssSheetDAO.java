package com.linkage.module.itms.service.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;


/**
 * 
 * @author zhangshimin(工号) Tel:
 * @version 1.0
 * @since 2011-5-26 下午09:11:54
 * @category com.linkage.module.itms.service.dao
 * @copyright 南京联创科技 网管科技部
 *
 */
public class ErrorBssSheetDAO extends SuperDAO
{
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ErrorBssSheetDAO.class);
	Map<String,String> cityMap;
	private List<Map<String,String>> cityList=new ArrayList<Map<String,String>>();
	public int countErrSheet(String reciveDateStart,String reciveDateEnd,String cityId,
			String username,String sheetType,String resultSheet ,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryErrSheet({})", username);
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) from tab_err_bss_sheet where 1=1");
		}else{
			sql.append("select count(1) from tab_err_bss_sheet where 1=1");
		}
		
		if(false == StringUtil.IsEmpty(reciveDateStart))
		{
			sql.append(" and recieve_date > ").append(reciveDateStart);
		}
		if(false == StringUtil.IsEmpty(reciveDateEnd))
		{
			sql.append(" and recieve_date < ").append(reciveDateEnd);
		}
		
		if(false == StringUtil.IsEmpty(username))
		{
			sql.append(" and username='").append(username).append("'");
		}
		if(false == StringUtil.IsEmpty(sheetType) && !"-1".equals(sheetType))
		{
			sql.append(" and sheet_type=").append(sheetType);
		}
		if(false == StringUtil.IsEmpty(resultSheet) && !"-1".equals(resultSheet))
		{
			sql.append(" and resp_code=").append(resultSheet);
		}
		if(false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			
			cityList=CityDAO.getAllNextCityListByCityPid(cityId);
			StringBuffer cityIdStr = new StringBuffer();
			cityIdStr.append("(");
			
			for(int i=0;i<cityList.size();i++){
				if(i+1 == cityList.size()){
		         cityIdStr.append("'"+StringUtil.getStringValue(cityList.get(i).get("city_id"))+"')");
				}
				else{
					cityIdStr.append("'"+StringUtil.getStringValue(cityList.get(i).get("city_id"))+"',");
				}
			}
			sql.append(" and city_id in ").append(cityIdStr);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
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
	
	public List<Map<String,String>> queryErrSheet(String reciveDateStart,String reciveDateEnd,
			String cityId,String username,String sheetType,String resultSheet ,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryErrSheet({})", username);
		StringBuffer sql = new StringBuffer();
		sql.append("select sheet_type,username,city_id,sheet_content,sheet_resp,recieve_date,resp_code ");
		sql.append("from tab_err_bss_sheet where 1=1");
		if(false == StringUtil.IsEmpty(reciveDateStart))
		{
			sql.append(" and recieve_date > ").append(reciveDateStart);
		}
		if(false == StringUtil.IsEmpty(reciveDateEnd))
		{
			sql.append(" and recieve_date < ").append(reciveDateEnd);
		}
	
		if(false == StringUtil.IsEmpty(username))
		{
			sql.append(" and username='").append(username).append("'");
		}
		if(false == StringUtil.IsEmpty(sheetType) && !"-1".equals(sheetType))
		{
			sql.append(" and sheet_type=").append(sheetType);
		}
		if(false == StringUtil.IsEmpty(resultSheet) && !"-1".equals(resultSheet))
		{
			sql.append(" and resp_code=").append(resultSheet);
		}
		
		if(false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			
			cityList=CityDAO.getAllNextCityListByCityPid(cityId);
			StringBuffer cityIdStr = new StringBuffer();
			cityIdStr.append("(");
			
			for(int i=0;i<cityList.size();i++){
				if(i+1 == cityList.size()){
		         cityIdStr.append("'"+StringUtil.getStringValue(cityList.get(i).get("city_id"))+"')");
				}
				else{
					cityIdStr.append("'"+StringUtil.getStringValue(cityList.get(i).get("city_id"))+"',");
				}
			}
			sql.append(" and city_id in ").append(cityIdStr);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
	    cityMap = CityDAO.getCityIdCityNameMap();
		List list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				String sheetType = rs.getString("sheet_type");
				if(sheetType.equals("20"))
				{
					map.put("sheet_type", "建设流程");
				}
				else if(sheetType.equals("21"))
				{
					map.put("sheet_type", "IPTV业务");
				}
				else if(sheetType.equals("22"))
				{
					map.put("sheet_type", "宽带业务");
				}
				else if(sheetType.equals("14"))
				{
					map.put("sheet_type", "VOIP业务");
				}
				else
				{
					map.put("sheet_type", "");
				}
				map.put("username", rs.getString("username"));
				map.put("sheet_content", rs.getString("sheet_content"));
				map.put("sheet_resp", rs.getString("sheet_resp"));
				map.put("resp_code", rs.getString("resp_code"));
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name))
				{
					map.put("city_name", city_name);
				}
				else
				{
					map.put("city_name", "");
				}
				// 将recieve_date转换成时间
				try
				{
					long dealdate = StringUtil.getLongValue(rs.getString("recieve_date"));
					DateTimeUtil dt = new DateTimeUtil(dealdate * 1000);
					map.put("recieve_date", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					map.put("recieve_date", "");
				}
				catch (Exception e)
				{
					map.put("recieve_date", "");
				}
				return map;
			}
		});
		return list;
	}
}
