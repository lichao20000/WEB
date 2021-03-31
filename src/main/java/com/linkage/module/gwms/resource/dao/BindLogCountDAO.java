/**
 * 
 */
package com.linkage.module.gwms.resource.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

import dao.util.JdbcTemplateExtend;

/**
 * @author OneLineSky E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2008-12-18
 * @category dao.confTaskView
 * 
 */
public class BindLogCountDAO {

	//日志记录
	private static Logger logger = LoggerFactory.getLogger(BindLogCountDAO.class);
	
	private JdbcTemplateExtend jt;
	
	public BindLogCountDAO() {
		
	}

	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}
	
	public List getBindCountList(long bindStartTime, long bindEndTime,String cityId) 
	{
		logger.debug("BindLogCountDAO=>getBindCountList({},{},{})",
				new Object[]{bindStartTime,bindEndTime,cityId});
		
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append(" select city_id,oper_type,count(*) as num ");
		}else{
			sql.append(" select city_id,oper_type,count(1) as num ");
		}
		sql.append("from bind_log a,tab_gw_device b where a.device_id=b.device_id ");
		
		if(null!=cityId && !"".equals(cityId) && !"00".equals(cityId)){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
			cityArray = null;
		}
		
		if(bindStartTime>0){
			sql.append(" and a.binddate > ");
			sql.append(bindStartTime);
		}
		
		if(bindEndTime>0){
			sql.append(" and a.binddate <= ");
			sql.append(bindEndTime);
		}
		
		sql.append(" group by city_id,oper_type ");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
}
