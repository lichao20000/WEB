/**
 *
 */
package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.DataSourceContextHolder;
import com.linkage.module.gwms.dao.DataSourceTypeCfgPropertiesManager;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

import dao.util.JdbcTemplateExtend;

/**
 * @author OneLineSky E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2008-12-18
 * @category dao.confTaskView
 *
 */
public class BindLogViewDAO {

	//日志记录
	private static Logger logger = LoggerFactory.getLogger(BindLogViewDAO.class);

	private JdbcTemplateExtend jt;

	private Map<String, String> userlineMap = new HashMap<String, String>();
	private Map<String, String> operTypeMap = new HashMap<String, String>();

	public BindLogViewDAO() {
		operTypeMap.put("1", "绑定");
		operTypeMap.put("2", "解绑");
		operTypeMap.put("3", "修障");
	}

	/**
	 * setDao 注入
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
		this.setDataSourceType(this.getClass().getName());
	}


	private void setDataSourceType(String key)
	{
		String type = null;
		type = DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(key);
		if(!StringUtil.IsEmpty(type))
		{
			logger.warn("类："+key+"的数据源类型配置为："+type);
			DataSourceContextHolder.setDBType(type);
		}
	}


	public List getBindLogList(int curPage_splitPage, int num_splitPage,
			long bindStartTime, long bindEndTime, String username,String deviceSn,
			String cityId,String operType) {

		logger.debug("BindLogViewDAO=>getBindLogList({},{},{},{},{},{},{},{})",
				new Object[]{curPage_splitPage,num_splitPage,bindStartTime,
				bindEndTime,username,deviceSn,cityId,operType});
		PrepareSQL psql = new PrepareSQL(" select bind_type_id,type_name from bind_type ");
		List templist = jt.queryForList(psql.getSQL());
		for(int i=0;i<templist.size();i++){
			Map tempMap = (Map) templist.get(i);
			userlineMap.put(String.valueOf(tempMap.get("bind_type_id")), String.valueOf(tempMap.get("type_name")));
		}

		StringBuffer sql = new StringBuffer();

		sql.append(" select a.dealstaff,a.username,a.credno,a.userline," +
				" a.oper_type,a.binddate,b.city_id,b.oui, b.device_serialnumber " +
				" from stb_bind_log a,stb_tab_gw_device b where a.device_id=b.device_id ");

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

		if (!(null == username || "".equals(username))) {
			sql.append(" and a.username = '");
			sql.append(username);
			sql.append("' ");
		}

		if (!(null == deviceSn || "".equals(deviceSn))) {
			if(deviceSn.length()>5){
				sql.append(" and b.dev_sub_sn ='").append(deviceSn.substring(deviceSn.length()-6, deviceSn.length())).append("'");
		   }
			sql.append(" and b.device_serialnumber like '%");
			sql.append(deviceSn);
			sql.append("' ");
		}

		if (null!=operType) {
			sql.append(" and a.oper_type =");
			sql.append(operType);
			sql.append(" ");
		}

		logger.debug("BindLogViewDAO=>getBindLogList=>" + sql.toString());
		psql = new PrepareSQL(sql.toString());
		return jt.querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {

						Map<String, String> map = new HashMap<String, String>();

						map.put("dealstaff", rs.getString("DEALSTAFF"));
						map.put("username", rs.getString("username"));
						map.put("credno", rs.getString("credno"));
						map.put("deviceSn", rs.getString("oui")+"-"+rs.getString("device_serialnumber"));
						map.put("cityName", CityDAO.getCityIdCityNameMap().get(rs.getString("city_id")));
						map.put("binddate", new DateTimeUtil(Long.valueOf(rs.getString("binddate")+"000")).getLongDate());
						map.put("userline", userlineMap.get(rs.getString("userline")));
						map.put("operType", operTypeMap.get(rs.getString("oper_type")));

						return map;
					}
				});
	}

	public int getBindLogCount(int num_splitPage,long bindStartTime, long bindEndTime,
			String username,String deviceSn,String cityId,String operType) {

		logger.debug("BindLogViewDAO=>getBindLogList({},{},{},{},{},{},{})",
				new Object[]{num_splitPage,bindStartTime,	bindEndTime,username,deviceSn,cityId,operType});

		StringBuffer sql = new StringBuffer();

		sql.append(" select count(*) from stb_bind_log a,stb_tab_gw_device b where a.device_id=b.device_id ");

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

		if (!(null == username || "".equals(username))) {
			sql.append(" and a.username = '");
			sql.append(username);
			sql.append("' ");
		}

		if (!(null == deviceSn || "".equals(deviceSn))) {
			if(deviceSn.length()>5){
				sql.append(" and b.dev_sub_sn ='").append(deviceSn.substring(deviceSn.length()-6, deviceSn.length())).append("'");
			}
			sql.append(" and b.device_serialnumber like '%");
			sql.append(deviceSn);
			sql.append("' ");
		}

		if (null!=operType) {
			sql.append(" and a.oper_type =");
			sql.append(operType);
			sql.append(" ");
		}

		logger.debug("BindLogViewDAO=>getBindLogCount=>" + sql.toString());
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}

		return maxPage;

	}
}
