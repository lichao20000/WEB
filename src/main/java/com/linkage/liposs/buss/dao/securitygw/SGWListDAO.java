package com.linkage.liposs.buss.dao.securitygw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;

/**
 * 供安全网关首页显示设备列表的dao类
 * 
 * @author 王志猛(5194) tel：13701409234
 * @version 1.0
 * @since 2008-4-1
 * @category com.linkage.liposs.buss.dao.securitygw<br>
 *           版权：南京联创科技 网管科技部
 * 
 */
public class SGWListDAO
{
	private JdbcTemplate jt;
	static private String getPSql = "select b.device_id,b.device_name,b.loopback_ip,b.device_model_id as device_model,b.device_serialnumber,b.oui,c.customer_name as customname,d.cpu_util,d.mem_util,d.connection_util,d.ping_value,d.severity,e.virustimes,e.ashmailtimes,e.attacktimes,e.filtertimes from tab_gw_res_area a "
			+ " left join tab_gw_device b  on  b.device_id=a.res_id "
			+ " left join tab_customerinfo c on b.customer_id=c.customer_id "
			+ " left join tab_taskplan_data d on b.device_id=d.device_id "
			+ " left join sgw_security_static e on b.device_id=e.deviceid "
			+ "where (a.res_type=1 or a.res_type=2) and b.gw_type=2 and a.area_id=?";
	private PrepareSQL ppSql;// 用来组装sql语句的
	/**
	 * 获取首页性能力、安全事件的列表展示
	 * 
	 * @param area_id
	 *            传入的域id
	 * @return 时间列表 ，数据集合 list<Map>,内部列<br>
	 *         device_id,device_name,customname,d.cpu_util,d.mem_util,d.connection_util,e.virustimes,e.ashmailtimes,e.attacktimes,e.filtertimes
	 */
	public List<Map> getSGWFirst(String area_id)
	{
		Map<String, List<Map>> pList = new HashMap<String, List<Map>>();
		ppSql.setSQL(getPSql);
		ppSql.setInt(1, Integer.parseInt(area_id));
		return jt.queryForList(ppSql.getSQL());
	}
	/**
	 * 设置数据源
	 * 
	 * @param dao
	 *            数据源
	 */
	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}
	public void setPpSql(PrepareSQL ppSql)
	{
		this.ppSql = ppSql;
	}
}
