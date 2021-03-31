/**
 *
 */
package com.linkage.module.bbms.report.dao;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

import dao.util.JdbcTemplateExtend;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-6-11
 * @category dao.report
 *
 */
public class EVDOCountReportDAO {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(EVDOCountReportDAO.class);

	private JdbcTemplateExtend jt;

	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}

	/**
	 * 根据属地，时间查询所有上报的设备
	 *
	 * @param cityId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public int getDeviceCount(String cityId,long startDate,long endDate){

		logger.debug("getEVDOCount(cityId:{},startDate:{},endDate)",cityId,startDate);

		StringBuffer sql = new StringBuffer();

		sql.append(" select count(*) from tab_gw_device a where 1=1 ");
		if("00".equals(cityId)){
			sql.append("  and a.city_id ='00' ");
		}else{
			sql.append("  and a.city_id in (");
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(StringUtils.weave(list));
			list = null;
			sql.append(") ");
		}

		sql.append(" and a.complete_time> ");
		sql.append(startDate);
		sql.append(" and a.complete_time< ");
		sql.append(endDate);

		logger.debug("getEVDOCount=>sql:{}",sql.toString());
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.queryForInt(sql.toString());

	}

	/**
	 * 根据属地，时间查询所有上报的设备
	 *
	 * @param cityId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getEVDOCount(String cityId,long startDate,long endDate){

		logger.debug("getEVDOCount(cityId:{},startDate:{},endDate)",cityId,startDate);

		StringBuffer sql = new StringBuffer();

		sql.append(" select a.device_id,a.city_id,a.card_bind_stat,b.work_mode ");
		sql.append(" from tab_gw_device a,data_card b where a.device_id=b.device_id ");
		if("00".equals(cityId)){
			sql.append("  and a.city_id ='00' ");
		}else{
			sql.append("  and a.city_id in (");
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(StringUtils.weave(list));
			list = null;
			sql.append(") ");
		}

		sql.append(" and a.complete_time> ");
		sql.append(startDate);
		sql.append(" and a.complete_time< ");
		sql.append(endDate);

		logger.debug("getEVDOCount=>sql:{}",sql.toString());
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.queryForList(sql.toString());

	}

}
