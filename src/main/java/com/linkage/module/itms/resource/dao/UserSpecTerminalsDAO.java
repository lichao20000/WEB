package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 
 * @author hanzezheng (Ailk No.)
 * @version 1.0
 * @since 2015-2-12
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class UserSpecTerminalsDAO extends SuperDAO
{
	private Map<String, String> cityMap = new HashMap<String, String>();
	
	/**
	 * 
	 * @param startOpenDate1
	 * @param endOpenDate1
	 * @param city_id
	 * @param spec_id
	 * @return
	 */
	public Map getUserSpecTerminals(String startOpenDate1,String endOpenDate1, String city_id,String spec_id)
	{
		
		List Ulist=getUId(spec_id);
		List Slist=getSId(spec_id);
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(1) num from tab_hgwcustomer a,tab_bss_dev_port b,tab_gw_device c,tab_devicetype_info d ,tab_bss_dev_port e, gw_cust_user_dev_type g");
		sql.append(" where a.spec_id = b.id and c.devicetype_id = d.devicetype_id and d.spec_id = e.id and a.device_id = c.device_id and a.user_id=g.user_id");
		sql.append(" and b.gw_type='1' and a.opmode='1' and g.type_id='2' ");
		if (!StringUtil.IsEmpty(startOpenDate1))
		{
			sql.append(" and a.dealdate>=").append(startOpenDate1);
		}
		if (!StringUtil.IsEmpty(endOpenDate1))
		{
			sql.append(" and a.dealdate<=").append(endOpenDate1);
		}
		List cityIdList  = null;
		if(!StringUtil.IsEmpty(city_id) && !"00".equals(city_id) && !"-1".equals(city_id)){
			cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" and b.id in(");
		for(int i=0;i<Ulist.size();i++){
			if (i + 1 == Ulist.size()) {
				sql.append(((Map)Ulist.get(i)).get("id") + ")");
			} else {
				sql.append(((Map)Ulist.get(i)).get("id") + ",");
			}
		}
		sql.append(" and e.id in(");
		for(int i=0;i<Slist.size();i++){
			if (i + 1 == Slist.size()) {
				sql.append(((Map)Slist.get(i)).get("id") + ")");
			} else {
				sql.append(((Map)Slist.get(i)).get("id") + ",");
			}
		}
		sql.append(" group by a.city_id");
		cityMap = CityDAO.getCityIdCityNameMap();
		PrepareSQL psql = new PrepareSQL(sql.toString());
		@SuppressWarnings("unchecked")
		List list = jt.queryForList(psql.getSQL());
		Map<String, String> map = new HashMap<String, String>();
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), StringUtil
						.getStringValue(rmap.get("num")));
			}
		}
		cityIdList = null;
		return map;	
	}
	
	public Map getTerminalSpecAll(String startOpenDate1,String endOpenDate1, String city_id)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(1) total from tab_hgwcustomer a ,gw_cust_user_dev_type b  where 1=1 and a.user_id = b.user_id and b.type_id='2' and a.opmode='1'");
		if (false == StringUtil.IsEmpty(startOpenDate1)){
			sql.append(" and a.dealdate >= ").append(startOpenDate1);   // 受理时间
		}
		if (false == StringUtil.IsEmpty(endOpenDate1)){
			sql.append(" and a.dealdate <= ").append(endOpenDate1);     // 受理时间
		}
		if(!StringUtil.IsEmpty(city_id) && !"00".equals(city_id) && !"-1".equals(city_id)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" group by a.city_id");
		List cityIdList  = null;
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		Map<String, String> map = new HashMap<String, String>();
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), StringUtil
						.getStringValue(rmap.get("total")));
			}
		}
		cityIdList = null;
		return map;	
	}
	
	public List<Map> getTerminalSpecList(String startOpenDate1,String endOpenDate1, String city_id,String spec_id,int curPage_splitPage, int num_splitPage){
		List Ulist=getUId(spec_id);
		List Slist=getSId(spec_id);
		
		StringBuffer sql = new StringBuffer();
		sql.append("select a.username username,c.device_serialnumber device_serialnumber from tab_hgwcustomer a,tab_bss_dev_port b,tab_gw_device c,tab_devicetype_info d ,tab_bss_dev_port e,gw_cust_user_dev_type g ");
		sql.append(" where a.spec_id = b.id and c.devicetype_id = d.devicetype_id and d.spec_id = e.id and a.device_id = c.device_id and a.user_id=g.user_id");
		sql.append(" and b.gw_type='1' and a.opmode='1' and g.type_id='2'");
		if (!StringUtil.IsEmpty(startOpenDate1))
		{
			sql.append(" and a.dealdate>=").append(startOpenDate1);
		}
		if (!StringUtil.IsEmpty(endOpenDate1))
		{
			sql.append(" and a.dealdate<=").append(endOpenDate1);
		}
		if(!StringUtil.IsEmpty(city_id) && !"00".equals(city_id) && !"-1".equals(city_id)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" and b.id in(");
		for(int i=0;i<Ulist.size();i++){
			if (i + 1 == Ulist.size()) {
				sql.append(((Map)Ulist.get(i)).get("id") + ")");
			} else {
				sql.append(((Map)Ulist.get(i)).get("id") + ",");
			}
		}
		sql.append(" and e.id in(");
		for(int i=0;i<Slist.size();i++){
			if (i + 1 == Slist.size()) {
				sql.append(((Map)Slist.get(i)).get("id") + ")");
			} else {
				sql.append(((Map)Slist.get(i)).get("id") + ",");
			}
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = querySP(psql.getSQL(),(curPage_splitPage - 1) * num_splitPage + 1, num_splitPage,
				new RowMapper() {

					@Override
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("loid", String.valueOf(rs.getString("username")));
						map.put("serial", String.valueOf(rs.getString("device_serialnumber")));
						return map;
					}
			
		});
		return list;
	}
	
	public int getTerminalSpecListCount(String startOpenDate1,String endOpenDate1, String city_id,String spec_id,int curPage_splitPage, int num_splitPage){
		List Ulist=getUId(spec_id);
		List Slist=getSId(spec_id);
		
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) total from tab_hgwcustomer a,tab_bss_dev_port b,tab_gw_device c,tab_devicetype_info d ,tab_bss_dev_port e,gw_cust_user_dev_type g ");
		sql.append(" where a.spec_id = b.id and c.devicetype_id = d.devicetype_id and d.spec_id = e.id and a.device_id = c.device_id and a.user_id=g.user_id");
		sql.append(" and b.gw_type='1' and a.opmode='1' and g.type_id='2'");
		if (!StringUtil.IsEmpty(startOpenDate1))
		{
			sql.append(" and a.dealdate>=").append(startOpenDate1);
		}
		if (!StringUtil.IsEmpty(endOpenDate1))
		{
			sql.append(" and a.dealdate<=").append(endOpenDate1);
		}
		if(!StringUtil.IsEmpty(city_id) && !"00".equals(city_id) && !"-1".equals(city_id)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" and b.id in(");
		for(int i=0;i<Ulist.size();i++){
			if (i + 1 == Ulist.size()) {
				sql.append(((Map)Ulist.get(i)).get("id") + ")");
			} else {
				sql.append(((Map)Ulist.get(i)).get("id") + ",");
			}
		}
		sql.append(" and e.id in(");
		for(int i=0;i<Slist.size();i++){
			if (i + 1 == Slist.size()) {
				sql.append(((Map)Slist.get(i)).get("id") + ")");
			} else {
				sql.append(((Map)Slist.get(i)).get("id") + ",");
			}
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
	
	public List<Map> getExcel(String startOpenDate1,String endOpenDate1, String city_id,String spec_id){
		List Ulist=getUId(spec_id);
		List Slist=getSId(spec_id);
		
		StringBuffer sql = new StringBuffer();
		sql.append("select a.username username,c.device_serialnumber device_serialnumber from tab_hgwcustomer a,tab_bss_dev_port b,tab_gw_device c,tab_devicetype_info d ,tab_bss_dev_port e,gw_cust_user_dev_type g ");
		sql.append(" where a.spec_id = b.id and c.devicetype_id = d.devicetype_id and d.spec_id = e.id and a.device_id = c.device_id and a.user_id=g.user_id");
		sql.append(" and b.gw_type='1' and a.opmode='1' and g.type_id='2'");
		if (!StringUtil.IsEmpty(startOpenDate1))
		{
			sql.append(" and a.dealdate>=").append(startOpenDate1);
		}
		if (!StringUtil.IsEmpty(endOpenDate1))
		{
			sql.append(" and a.dealdate<=").append(endOpenDate1);
		}
		if(!StringUtil.IsEmpty(city_id) && !"00".equals(city_id) && !"-1".equals(city_id)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" and b.id in(");
		for(int i=0;i<Ulist.size();i++){
			if (i + 1 == Ulist.size()) {
				sql.append(((Map)Ulist.get(i)).get("id") + ")");
			} else {
				sql.append(((Map)Ulist.get(i)).get("id") + ",");
			}
		}
		sql.append(" and e.id in(");
		for(int i=0;i<Slist.size();i++){
			if (i + 1 == Slist.size()) {
				sql.append(((Map)Slist.get(i)).get("id") + ")");
			} else {
				sql.append(((Map)Slist.get(i)).get("id") + ",");
			}
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = jt.queryForList(psql.getSQL());
		if(null!=list && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("loid", list.get(i).get("username"));
				list.get(i).put("serial", list.get(i).get("device_serialnumber"));
			}
		}
		return list;
	}
	
	
	
	public List getUId(String spec_id){
		int lan_num=0;
		int voice_num=0;
		String title="";
		if(spec_id.equals("4221")){
			lan_num=4;
			voice_num=2;
		}else if(spec_id.equals("4220")){
			lan_num=4;
			voice_num=2;
		}else if(spec_id.equals("2142")){
			lan_num=2;
			voice_num=1;
		}else if(spec_id.equals("2042")){
			lan_num=2;
			voice_num=0;
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select id from tab_bss_dev_port where gw_type = '1'");
		sql.append(" and lan_num="+lan_num);
		sql.append(" and voice_num="+voice_num);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list=jt.queryForList(psql.getSQL());
		return list;
	}
	public List getSId(String spec_id){
		int lan_num=0;
		int voice_num=0;
		String title="";
		if(spec_id.equals("4221")){
			lan_num=2;
			voice_num=1;
		}else if(spec_id.equals("4220")){
			lan_num=2;
			voice_num=0;
		}else if(spec_id.equals("2142")){
			lan_num=4;
			voice_num=2;
		}else if(spec_id.equals("2042")){
			lan_num=4;
			voice_num=2;
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select id from tab_bss_dev_port where gw_type = '1'");
		sql.append(" and lan_num="+lan_num);
		sql.append(" and voice_num="+voice_num);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list=jt.queryForList(psql.getSQL());
		return list;
	}
}

