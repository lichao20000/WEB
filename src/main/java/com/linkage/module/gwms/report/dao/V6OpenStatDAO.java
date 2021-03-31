package com.linkage.module.gwms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 
 * @author admin (Ailk No.)
 * @version 1.0
 * @since 2020年11月20日
 * @category com.linkage.module.gwms.report.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class V6OpenStatDAO extends SuperDAO
{
	Logger logger = LoggerFactory.getLogger(V6OpenStatDAO.class);
	
	
	public List<Map> stat(int curPage_splitPage, int num_splitPage){
		StringBuffer sql = new StringBuffer();
		sql.append("select vendor_name,device_model,gateway_type,is_v6,");
		sql.append("sum(bind_num) bindnum,sum(succ_num) succnum,sum(fail_num) failnum,sum(last_online_num) notnum ");
		sql.append("from tab_v6_report group by vendor_name,device_model,gateway_type,is_v6 ");
		sql.append("order by vendor_name,device_model,gateway_type,is_v6 ");
		 
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("vendorName", StringUtil.getStringValue(rs.getString("vendor_name"))); 
				map.put("deviceModel", StringUtil.getStringValue(rs.getString("device_model"))); 
				map.put("specType", StringUtil.getStringValue(rs.getString("gateway_type"))); 
				map.put("isIpv6", StringUtil.getStringValue(rs.getString("is_v6"))); 
				map.put("bindNum", StringUtil.getStringValue(rs.getString("bindnum"))); 
				map.put("succNum", StringUtil.getStringValue(rs.getString("succnum"))); 
				map.put("failNum", StringUtil.getStringValue(rs.getString("failnum"))); 
				map.put("notOnlineNum", StringUtil.getStringValue(rs.getString("notnum"))); 
				return map;
			}
		});
		
		return list;
	}
	
	public int count(int curPage_splitPage, int num_splitPage){
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from (");
		sql.append("select vendor_name,device_model,gateway_type,is_v6,");
		sql.append("sum(bind_num) bindnum,sum(succ_num) succnum,sum(fail_num) failnum,sum(last_online_num) notnum ");
		sql.append("from tab_v6_report group by vendor_name,device_model,gateway_type,is_v6 ");
		sql.append(") db ");
		
		 
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
	
	
	public List<Map> toExcel(){
		StringBuffer sql = new StringBuffer();
		sql.append("select vendor_name ,device_model,gateway_type,is_v6,");
		sql.append("sum(bind_num) bindnum,sum(succ_num) succnum,sum(fail_num) failnum,sum(last_online_num) notnum ");
		sql.append("from tab_v6_report group by vendor_name,device_model,gateway_type,is_v6 ");
		sql.append("order by vendor_name,device_model,gateway_type,is_v6 ");
		 
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.toString());
	}

}
