
package com.linkage.module.bbms.report.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings("unchecked")
public class ServiceReportDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ServiceReportDAO.class);

	/**
	 * 取的所有的业务类型
	 * 
	 * @author wangsenbo
	 * @date Mar 12, 2010
	 * @param
	 * @return List<Map<String,String>>
	 */
	public List<Map<String, String>> getServiceTypeList()
	{
		String sql = "select id as service_id,service_name from cpe_gather_service_desc";
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		return jt.queryForList(sql);
	}

	/**
	 * 根据属地、时间、业务获取业务列表
	 *
	 * @author wangsenbo
	 * @date Mar 12, 2010
	 * @param 
	 * @return List<Map>
	 */
	public List<Map> getServiceList(String cityId, String stat_day1, String serviceId,
			String reportType)
	{
		StringBuffer sql = new StringBuffer();
		if ("week".equals(reportType))
		{
			sql.append("select distinct b.service_name from cpe_service_report_week a, cpe_gather_service_desc b where a.id=b.id ");
		}
		else if ("month".equals(reportType))
		{
			sql.append("select distinct b.service_name from cpe_service_report_month a, cpe_gather_service_desc b where a.id=b.id ");
		}
		else
		{
			logger.error("报表类型不正确");
			return null;
		}
		
		if (false == StringUtil.IsEmpty(stat_day1))
		{
			//sql.append(" and a.stat_day=").append(stat_day1);
			if("week".equals(reportType)){
				sql.append(" and stat_day=").append(new DateTimeUtil(stat_day1).getYYYYWW());
			}else{
				sql.append(" and stat_day=").append(new DateTimeUtil(stat_day1).getYYYYMM());
			}
		}
		
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(
					")");
			cityIdList = null;
		}
		if (false == StringUtil.IsEmpty(serviceId)&&!"-1".equals(serviceId))
		{
			sql.append(" and a.id=").append(serviceId);
		}
		sql.append(" order by a.id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		return jt.queryForList(sql.toString());
	}

	/**
	 * 业务开通数
	 *
	 * @author wangsenbo
	 * @date Mar 15, 2010
	 * @param 
	 * @return List
	 */
	public List countService(String cityId, String stat_day1, String serviceId,
			String reportType)
	{
		StringBuffer sql = new StringBuffer();
		if ("week".equals(reportType))
		{
			sql.append("select id as service_id,city_id,succ_num from cpe_service_report_week where 1=1 ");
		}
		else if ("month".equals(reportType))
		{
			sql.append("select id as service_id,city_id,succ_num from cpe_service_report_month where 1=1 ");
		}
		else
		{
			logger.error("报表类型不正确");
			return null;
		}
		if (false == StringUtil.IsEmpty(stat_day1))
		{
			if("week".equals(reportType)){
				sql.append(" and stat_day=").append(new DateTimeUtil(stat_day1).getYYYYWW());
			}else{
				sql.append(" and stat_day=").append(new DateTimeUtil(stat_day1).getYYYYMM());
			}
		}
		
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in (").append(StringUtils.weave(cityIdList)).append(
					")");
			cityIdList = null;
		}
		if (false == StringUtil.IsEmpty(serviceId)&&!"-1".equals(serviceId))
		{
			sql.append(" and id=").append(serviceId);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		return jt.queryForList(sql.toString());
	}
}
