/**
 * 
 */
package com.linkage.module.gwms.service.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.service.dao.interf.I_SheetStateViewDAO;

import dao.util.JdbcTemplateExtend;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-6-16
 * @category dao.business
 * 
 */
@SuppressWarnings("unchecked")
public class SheetStateViewDAO implements I_SheetStateViewDAO {

	private JdbcTemplateExtend jt;

	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}

	//受理状态
	private Map<String, String> result_map = new HashMap<String, String>();
	//绑定状态
	private Map<String, String> bind_map = new HashMap<String, String>();
	//配置执行状态
	private Map<String, String> resultSpec_map = new HashMap<String, String>();
	
	/**
	 * 构造初始相关函数
	 *
	 */
	public SheetStateViewDAO(){
		result_map.put("0", "成功");
		result_map.put("1", "失败");
		
		bind_map.put("0", "未绑定");
		bind_map.put("1", "已绑定");
		
		resultSpec_map.put("-1", "未配置");
		resultSpec_map.put("0", "失败");
		resultSpec_map.put("1", "成功");
		resultSpec_map.put("2", "正在执行");
		
	}
	
	/**
	 * @category 查询所有的属地
	 * 
	 * @param cityId
	 * @return
	 */
	public List getAllCity(String cityId) {

		StringBuffer sql = new StringBuffer();
		sql.append(" select city_id,city_name,parent_id from tab_city ");

		if (!"00".equals(cityId)) {
			if (null != cityId && !"".equals(cityId)) {
				sql.append(" where city_id='");
				sql.append(cityId);
				sql.append("' or parent_id='");
				sql.append(cityId);
				sql.append("' ");
			}
		}

		sql.append(" order by city_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.queryForList(sql.toString());
	}

	/**
	 * @category 查询操作类型
	 * 
	 * @param cityId
	 * @return
	 */
	public List getGwOperType() {

		StringBuffer sql = new StringBuffer();
		sql.append(" select oper_type_id,oper_type_name from tab_gw_oper_type ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.queryForList(sql.toString());
	}
	
	/**
	 * @category 查询满足条件的所有参数(针对excel导出)
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param username
	 * @param cityId
	 * @param productSpecId
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 */
	public List getSheetStateExcel(String username, String cityId,
			String productSpecId, long timeStart, long timeEnd,
			String operTypeId,String bindState) {

		StringBuffer sql = new StringBuffer();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		sql.append("select a.bss_sheet_id,a.receive_date,b.city_name,c.customer_name,");
		sql.append("a.username,a.result,a.bind_state,a.bind_time,a.result_spec_state,d.oper_type_name ");
		sql.append(" from tab_bss_sheet a,tab_city b,tab_customerinfo c,tab_gw_oper_type d ");
		sql.append(" where a.city_id=b.city_id and a.customer_id=c.customer_id and a.type=d.oper_type_id ");
		
		if(!"00".equals(cityId)){
			sql.append(" and a.city_id in (select city_id from tab_city where ");
			sql.append(" city_id='");
			sql.append(cityId);
			sql.append("' or parent_id='");
			sql.append(cityId);
			sql.append("') ");
		}
		if(!"".equals(username) && null!=username){
			sql.append(" and a.username='");
			sql.append(username);
			sql.append("' ");
		}
		if(0!=timeStart){
			sql.append(" and a.receive_date>");
			sql.append(timeStart);
		}
		if(0!=timeEnd){
			sql.append(" and a.receive_date<");
			sql.append(timeEnd);
		}
		if(null!=operTypeId && !"-1".equals(operTypeId)){
			sql.append(" and a.type=");
			sql.append(operTypeId);
		}
		if(null!=bindState && !"-1".equals(bindState)){
			sql.append(" and a.bind_state=");
			sql.append(bindState);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.query(sql.toString(),  new RowMapper() {
				public Object mapRow(ResultSet rs, int arg1)
						throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					
					String receiveDate = rs.getString("receive_date");
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(Long.parseLong(receiveDate));
					
					StringBuffer sb = new StringBuffer();   
		            sb.append(cal.get(Calendar.YEAR));   
		            sb.append("-");   
		            sb.append(cal.get(Calendar.MONTH)+1);   
		            sb.append("-");   
		            sb.append(cal.get(Calendar.DATE));    
			          
					map.put("receive_date",sb.toString());
					
					String bindTime = rs.getString("bind_time");
					if(!"".equals(bindTime) && null!=bindTime){
						Calendar calBindTime = Calendar.getInstance();
						calBindTime.setTimeInMillis(Long.parseLong(bindTime)*1000);
						
						StringBuffer sb2 = new StringBuffer();   
						sb2.append(calBindTime.get(Calendar.YEAR));   
						sb2.append("-");   
						sb2.append(calBindTime.get(Calendar.MONTH)+1);   
						sb2.append("-");   
						sb2.append(calBindTime.get(Calendar.DATE));    
				          
						map.put("bind_time",sb2.toString());
					}
					
					map.put("bss_sheet_id",rs.getString("bss_sheet_id"));
					map.put("city_name",rs.getString("city_name"));
					map.put("customer_name",rs.getString("customer_name"));
					map.put("username",rs.getString("username"));
					map.put("result",result_map.get(rs.getString("result")));
					map.put("bind_state",bind_map.get(rs.getString("bind_state")));
					map.put("result_spec_state",resultSpec_map.get(rs.getString("result_spec_state")));
					map.put("oper_type_name",rs.getString("oper_type_name"));
					
					return map;
				}
			}
		);
	}
	
	/**
	 * @category 查询满足条件的所有参数
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param username
	 * @param cityId
	 * @param productSpecId
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 */
	public List getSheetState(int curPage_splitPage, int num_splitPage,String username, String cityId,
			String productSpecId, long timeStart, long timeEnd,String operTypeId,String bindState) {

		StringBuffer sql = new StringBuffer();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		sql.append("select a.bss_sheet_id,a.receive_date,b.city_name,c.customer_name,");
		sql.append("a.username,a.result,a.bind_state,a.bind_time,a.result_spec_state,d.oper_type_name ");
		sql.append(" from tab_bss_sheet a,tab_city b,tab_customerinfo c,tab_gw_oper_type d ");
		sql.append(" where a.city_id=b.city_id and a.customer_id=c.customer_id and a.type=d.oper_type_id ");
		
		if(!"00".equals(cityId)){
			sql.append(" and a.city_id in (select city_id from tab_city where ");
			sql.append(" city_id='");
			sql.append(cityId);
			sql.append("' or parent_id='");
			sql.append(cityId);
			sql.append("') ");
		}
		if(!"".equals(username) && null!=username){
			sql.append(" and a.username='");
			sql.append(username);
			sql.append("' ");
		}
		if(0!=timeStart){
			sql.append(" and a.receive_date>");
			sql.append(timeStart);
		}
		if(0!=timeEnd){
			sql.append(" and a.receive_date<");
			sql.append(timeEnd);
		}
		if(null!=operTypeId && !"-1".equals(operTypeId)){
			sql.append(" and a.type=");
			sql.append(operTypeId);
		}
		if(null!=bindState && !"-1".equals(bindState)){
			sql.append(" and a.bind_state=");
			sql.append(bindState);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage + 1,
			num_splitPage, new RowMapper() {
				public Object mapRow(ResultSet rs, int arg1)
						throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					
					String receiveDate = rs.getString("receive_date");
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(Long.parseLong(receiveDate));
					
					StringBuffer sb = new StringBuffer();   
		            sb.append(cal.get(Calendar.YEAR));   
		            sb.append("-");   
		            sb.append(cal.get(Calendar.MONTH)+1);   
		            sb.append("-");   
		            sb.append(cal.get(Calendar.DATE));    
			          
					map.put("receive_date",sb.toString());
					
					String bindTime = rs.getString("bind_time");
					if(!"".equals(bindTime) && null!=bindTime){
						Calendar calBindTime = Calendar.getInstance();
						calBindTime.setTimeInMillis(Long.parseLong(bindTime)*1000);
						
						StringBuffer sb2 = new StringBuffer();   
						sb2.append(calBindTime.get(Calendar.YEAR));   
						sb2.append("-");   
						sb2.append(calBindTime.get(Calendar.MONTH)+1);   
						sb2.append("-");   
						sb2.append(calBindTime.get(Calendar.DATE));    
				          
						map.put("bind_time",sb2.toString());
					}
					
					map.put("bss_sheet_id",rs.getString("bss_sheet_id"));
					map.put("city_name",rs.getString("city_name"));
					map.put("customer_name",rs.getString("customer_name"));
					map.put("username",rs.getString("username"));
					map.put("result",result_map.get(rs.getString("result")));
					map.put("bind_state",bind_map.get(rs.getString("bind_state")));
					map.put("result_spec_state",resultSpec_map.get(rs.getString("result_spec_state")));
					map.put("oper_type_name",rs.getString("oper_type_name"));
					
					return map;
				}
			}
		);
	}
	
	/**
	 * @category 查询页数
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param username
	 * @param cityId
	 * @param productSpecId
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 */
	public int getSheetStateCount(int curPage_splitPage, int num_splitPage,String username, String cityId,
			String productSpecId, long timeStart, long timeEnd,String operTypeId,String bindState) {

		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_bss_sheet a,tab_city b,tab_customerinfo c,tab_gw_oper_type d ");
		sql.append(" where a.city_id=b.city_id and a.customer_id=c.customer_id and a.type=d.oper_type_id ");
		
		if(!"00".equals(cityId)){
			sql.append(" and a.city_id in (select city_id from tab_city where ");
			sql.append(" city_id='");
			sql.append(cityId);
			sql.append("' or parent_id='");
			sql.append(cityId);
			sql.append("') ");
		}
		if(!"".equals(username) && null!=username){
			sql.append(" and a.username='");
			sql.append(username);
			sql.append("' ");
		}
		if(0!=timeStart){
			sql.append(" and a.receive_date>");
			sql.append(timeStart);
		}
		if(0!=timeEnd){
			sql.append(" and a.receive_date<");
			sql.append(timeEnd);
		}
		if(null!=operTypeId && !"-1".equals(operTypeId)){
			sql.append(" and a.type=");
			sql.append(operTypeId);
		}
		if(null!=bindState && !"-1".equals(bindState)){
			sql.append(" and a.bind_state=");
			sql.append(bindState);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		int total = jt.queryForInt(sql.toString());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}

		return maxPage;
	}
}
