
package com.linkage.module.gwms.resource.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;

/**
 * @author zszhao6 (Ailk No.78987)
 * @version 1.0
 * @since 2018-7-29
 * @category com.linkage.module.gwms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BatchHttpTestBlackListDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory
			.getLogger(BatchHttpTestBlackListDAO.class);

	@SuppressWarnings("unchecked")
	public List<Map> queryDeviceDetailWithBlackList(String gwShare_queryField,
			String gwShare_queryParam)
	{
		logger.debug("query deviceDetailWithBlackList-->DAO");
		// 简单查询目前仅支持按设备序列号或是带宽账号查询
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			sql.append("select device_id,vendor_id,devicetype_id,device_model_id,");
			sql.append("device_serialnumber,city_id,add_time,is_blacklist ");
		}else{
			sql.append("select * ");
		}
		sql.append("from tab_speed_blacklist ");
		if ("kdname".equals(gwShare_queryField))
		{
			// 按宽带账号查询
			sql.append("where pppoe_name="+ gwShare_queryParam);
		}
		else
		{
			// 按设备序列号查询
			sql.append("where device_serialnumber="+ gwShare_queryParam);
		}
		List<Map> deviceDetail = querySP(sql.getSQL(), 1, 10);
		// 处理结果集
		for (Map map : deviceDetail)
		{
			map.put("deviceId", map.get("device_id"));
			map.put("vendorName", DeviceTypeUtil.vendorMap.get(StringUtil
					.getStringValue(map.get("vendor_id"))));
			map.put("deviceTypeName", DeviceTypeUtil.softVersionMap.get(StringUtil
					.getStringValue(map.get("devicetype_id"))));
			map.put("deviceModel", DeviceTypeUtil.deviceModelMap.get(StringUtil
					.getStringValue(map.get("device_model_id"))));
			map.put("deviceSerialNumber", map.get("device_serialnumber"));
			map.put("cityName",
					CityDAO.getCityName(StringUtil.getStringValue(map.get("city_id"))));
			// 时间处理
			long add_time_l = StringUtil.getLongValue(map.get("add_time"));
			map.put("add_time",
					new DateTimeUtil(add_time_l * 1000).getYYYY_MM_DD_HH_mm_ss());
			if (0 == Integer.parseInt(map.get("is_blacklist").toString()))
			{
				map.put("is_blacklist", "否");
			}
			else
			{
				map.put("is_blacklist", "是");
			}
		}
		logger.warn("根据设备序列号和宽带名称查询出的设备条数::" + deviceDetail.size());
		return deviceDetail;
	}

	/**
	 * 插入黑名单任务表
	 * 
	 * @param param
	 *            参数
	 * @return
	 */
	public int createHttpTaskSQL(Object[] param)
	{
		logger.debug("createHttpTestBlackListTaskSQL({})", param);
		PrepareSQL psql = new PrepareSQL();
		psql.append("insert into tab_blacklist_task(task_name,task_id,");
		psql.append("acc_oid,add_time,task_status,sql,filePath,task_desc) ");
		psql.append("values (?,?,?,?,?,?,?,?) ");
		psql.setString(1, (String) param[0]);// task_name
		psql.setLong(2, (Long) param[1]);// task_id
		psql.setLong(3, (Long) param[2]);// acc_oid
		psql.setLong(4, (Long) param[3]);// add_time
		psql.setInt(5, (Integer) param[4]); // 状态 0：未执行，1：执行过
		psql.setString(6, (String) param[5]); // sql
		psql.setString(7, (String) param[6]); // 文件上传
		psql.setString(8, (String) param[12]); // 描述
		logger.warn("插入批量定制黑名单任务表-->{}", psql.getSQL());
		return jt.update(psql.getSQL());
	}

	/**
	 * 获取tab_batchhttpblacklist_task当前最大任务id
	 * 
	 * @return 当前表最大任务id
	 */
	public Long getMaxTaskID()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select max(task_id) max from tab_blacklist_task");
		String task_id = StringUtil.getStringValue(queryForMap(psql.getSQL()).get("max"));
		return StringUtil.IsEmpty(task_id) ? 0 : StringUtil.getLongValue(task_id);
	}

	/**
	 * 根据device_id获取 序列号 地区 等
	 * 
	 * @return
	 */
	public Map<String, String> getdeviceDetailByid(String deviceid)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.device_serialnumber,b.pppoe_name,a.vendor_id,");
		psql.append("a.device_model_id,a.devicetype_id,a.city_id ");
		psql.append("from tab_gw_device a left join tab_sn_account b ");
		psql.append("on a.device_serialnumber=b.device_serialnumber where a.device_id=? ");
		psql.setString(1, deviceid);
		return DBOperation.getRecord(psql.getSQL());
	}

	/**
	 * 根据device_id获取 序列号 地区 等
	 * 
	 * @return
	 */
	public Map<String, String> getdeviceDetailBySN(String deviceSN)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.device_id,a.device_serialnumber,b.pppoe_name,");
		psql.append("a.vendor_id,a.device_model_id,a.devicetype_id,a.city_id ");
		psql.append("from tab_gw_device a left join tab_sn_account b ");
		psql.append("on a.device_serialnumber=b.device_serialnumber where a.device_serialnumber=? ");
		psql.setString(1, deviceSN);
		logger.warn("根据序列号查出设备详情结果::" + DBOperation.getRecord(psql.getSQL()));
		return DBOperation.getRecord(psql.getSQL());
	}

	/**
	 * 插入测速黑名单任务明细表
	 * 
	 * @author zzs
	 * @param 参数
	 */
	public void batchBlackListTask_dev(Object[] param, int flag)
	{
		if (1 == flag)
		{
			logger.warn("createHttpTaskBlackList_devSQL({})", param);
			StringBuilder sql = new StringBuilder();
			sql.append("insert into tab_blacklist_task_dev (task_id,device_id,");
			sql.append("device_serialnumber,pppoe_name,status,add_time,city_id) ");
			sql.append("values (?,?,?,?,?,?,?)");
			PrepareSQL psql = new PrepareSQL(sql.toString());
			psql.setLong(1, (Long) param[0]);
			psql.setString(2, (String) param[1]);
			psql.setString(3, (String) param[2]);
			psql.setString(4, (String) param[3]); 
			psql.setInt(5, 1); // 表示已添加进黑名单
			psql.setLong(6, (Long) param[5]);
			psql.setString(7, (String) param[9]);
			jt.update(psql.getSQL());
		}
		else
		{
			logger.warn("选择的设备已存在黑名单任务详细表，更新的task_id：" + param[0]);
			// 存在选择的设备已经存在黑名单的情况，此时将taskid更新为最新的
			StringBuilder sql_1 = new StringBuilder();
			sql_1.append("update tab_blacklist_task_dev set task_id=? where device_id=? ");
			PrepareSQL psql_1 = new PrepareSQL(sql_1.toString());
			psql_1.setLong(1, (Long) param[0]);
			psql_1.setString(2, (String) param[1]);
			jt.update(psql_1.getSQL());
		}
	}

	/**
	 * 插入批量测速黑名单任务明细表
	 * 
	 * @author fanjm
	 * @param 参数
	 * @return sql更改表的行数
	 */
	public int[] batchUpdate(String[] sqls)
	{
		return jt.batchUpdate(sqls);
	}

	/**
	 * 更新任务状态
	 * 
	 * @param task_id
	 * @return
	 */
	public int updateHttpTask(Long task_id)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("update tab_blacklist_task set task_status= ? where task_id = ?");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setInt(1, 1);
		psql.setLong(2, task_id);
		return jt.update(psql.getSQL());
	}

	/**
	 * 设备信息入库黑名单表
	 * 
	 * @return 入库结果
	 */
	public int batchBlackList(Object[] param_dev)
	{
		logger.debug("createHttpTaskBlackList_devSQL({})", param_dev);
		StringBuilder sql = new StringBuilder();
		sql.append("insert into tab_speed_blacklist (");
		sql.append("device_id,Device_serialnumber,pppoe_name,vendor_id,device_model_id,devicetype_id,city_id,add_time,is_blacklist)");
		sql.append(" values (?,?,?,?,?,?,?,?,?)");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1, (String) param_dev[1]);
		psql.setString(2, (String) param_dev[2]);
		psql.setString(3, (String) param_dev[3]);
		psql.setString(4, (String) param_dev[6]);
		psql.setString(5, (String) param_dev[7]);
		psql.setString(6, (String) param_dev[8]);
		psql.setString(7, (String) param_dev[9]);
		psql.setLong(8, (Long) param_dev[5]); // 更新时间
		psql.setInt(9, 1); // 黑名单状态设置为1
		try
		{
			jt.update(psql.getSQL());
			logger.warn("简单查询方式插入测速黑名单完成");
			return 1; // 代表入库成功
		}
		catch (DataAccessException e)
		{
			logger.warn("选择的设备已存在黑名单中，设备序列号：" + param_dev[2]);
			return 0; // 代表设备已在表中，不需要入库
		}
	}

	/**
	 * 根据导入的文件，解析后得到结果插入黑名单表
	 * 
	 * @param param_dev
	 * @return
	 */
	public void upDataBlackListBySN(Object[] param_dev)
	{
		logger.warn("upDataBlackListBySN({})", param_dev);
		StringBuilder sql = new StringBuilder();
		sql.append("insert into tab_speed_blacklist (");
		sql.append("device_id,Device_serialnumber,pppoe_name,vendor_id,device_model_id,devicetype_id,city_id,add_time,is_blacklist)");
		sql.append(" values (?,?,?,?,?,?,?,?,?)");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1, (String) param_dev[1]);
		psql.setString(2, (String) param_dev[2]);
		psql.setString(3, (String) param_dev[3]);
		psql.setString(4, (String) param_dev[4]);
		psql.setString(5, (String) param_dev[5]);
		psql.setString(6, (String) param_dev[6]);
		psql.setString(7, (String) param_dev[7]);
		psql.setLong(8, (Long) param_dev[8]); // 更新时间
		psql.setInt(9, 1); // 黑名单状态设置为1
		try
		{
			jt.update(psql.getSQL());
			logger.warn("加入黑名单成功::序列号" + param_dev[2]);
		}
		catch (DataAccessException e)
		{
			logger.warn("导入的文件中存在已添加的黑名单设备，设备序列号：" + param_dev[2]);
		}
	}

	public void upDataBlackListTaskDev(Object[] param_dev)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("insert into tab_blacklist_task_dev (");
		sql.append("task_id,device_id,Device_serialnumber,pppoe_name,status,add_time,city_id");
		sql.append(") values (?,?,?,?,?,?,?)");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setLong(1, (Long) param_dev[0]);
		psql.setString(2, (String) param_dev[1]);
		psql.setString(3, (String) param_dev[2]);
		psql.setString(3, (String) param_dev[3]);
		psql.setInt(5, 1);
		psql.setLong(6, (Long) param_dev[9]);
		psql.setString(7, (String) param_dev[7]);
		try
		{
			jt.update(psql.getSQL());
			logger.warn("添加设备进黑名单任务详细表成功，设备序列号：" + param_dev[3]);
		}
		catch (DataAccessException d)
		{
			// 存在导入设备已经存在黑名单的情况，此时将taskid更新为最新的
			StringBuilder sql_1 = new StringBuilder();
			sql_1.append("update tab_blacklist_task_dev set task_id= ? where device_id = ?");
			PrepareSQL psql_1 = new PrepareSQL(sql_1.toString());
			psql_1.setLong(1, (Long) param_dev[0]);
			psql_1.setString(2, (String) param_dev[1]);
			jt.update(psql_1.getSQL());
			logger.warn("设备已在黑名单任务详细表，更新的设备序列号：" + param_dev[2]);
		}
	}

	/**
	 * 高级设置，添加黑名单表，使用的merge into，在sysbase的数据库使用会有问题
	 */
	public void upDataBlackListByAdSet(Object[] param, String task_name,
			String task_desc, Long task_id)
	{
		if(DBUtil.GetDB()==3)
		{
			//TODO wait
			
			StringBuffer sb=new StringBuffer();
			sb.append("select a.device_id,a.device_serialnumber,b.pppoe_name," +
					"a.vendor_id,a.device_model_id,a.devicetype_id,a.city_id " +
					"from tab_gw_device a left join tab_sn_account b " +
					"on a.device_serialnumber=b.device_serialnumber where 1=1 ");
			if (null != param[0]){
				sb.append(" and a.city_id=" + param[0]);
			}
			if (null != param[1]){
				sb.append(" and a.vendor_id=" + param[1]);
			}
			if (null != param[2] && param[2]!="-1" && param[2]!=""){
				sb.append(" and a.device_model_id=" + param[2]);
			}
			if (null != param[3] && param[3]!="-1" && param[3]!=""){
				sb.append(" and a.devicetype_id=" + param[3]);
			}
			
			PrepareSQL psql = new PrepareSQL();
			psql.append("update tab_speed_blacklist c,("+sb.toString()+") d ");
			psql.append("set c.device_id=d.device_id ");
			psql.append("where c.device_serialnumber=d.device_serialnumber ");
			int recode = jt.update(psql.getSQL());
			logger.warn("高级设置，更新黑名表已完成，更新数据量为：" + recode);
			
			
			psql=null;
			psql = new PrepareSQL(sb.toString());
			psql.append(" and a.device_serialnumber not in(select device_serialnumber from tab_speed_blacklist order by device_serialnumber)");
			List list=jt.queryForList(psql.getSQL());
			if(list!=null && !list.isEmpty())
			{
				psql=null;
				psql = new PrepareSQL();
				psql.append("insert into tab_speed_blacklist(device_id,device_serialnumber,pppoe_name,vendor_id,device_model_id,city_id,devicetype_id,is_blacklist,add_time) ");
				psql.append("values(?,?,?,?,?,?,?,?,?) ");
				
				for(int i=0;i<list.size();i++){
					Map m=(Map) list.get(i);
					psql.setString(1,StringUtil.getStringValue(m, "device_id"));
					psql.setString(2,StringUtil.getStringValue(m, "device_serialnumber"));
					psql.setString(3,StringUtil.getStringValue(m, "pppoe_name"));
					psql.setString(4,StringUtil.getStringValue(m, "vendor_id"));
					psql.setString(5,StringUtil.getStringValue(m, "device_model_id"));
					psql.setString(6,StringUtil.getStringValue(m, "city_id"));
					psql.setInt(7,StringUtil.getIntValue(m, "devicetype_id"));
					psql.setInt(8,1);
					psql.setLong(9,System.currentTimeMillis()/1000);
					
					recode = jt.update(psql.getSQL());
				}
				
				logger.warn("高级设置，插入黑名表已完成，数据量为：" + list.size());
				list.clear();
			}
			list=null;
			
			
			
			//tab_blacklist_task_dev
			PrepareSQL psql_dev = new PrepareSQL(); // 黑名单任务详细sql配置完成
			psql_dev.append("update tab_blacklist_task_dev c,("+sb.toString()+") d ");
			psql_dev.append("set c.device_id=d.device_id ");
			psql_dev.append("where c.device_serialnumber=d.device_serialnumber ");
			int recode_dev = jt.update(psql_dev.getSQL());
			logger.warn("高级设置，更新黑名表详细表已完成，数据量为：" + recode_dev);
			
			
			psql=null;
			psql = new PrepareSQL(sb.toString());
			psql.append(" and a.device_serialnumber not in(select device_serialnumber from tab_blacklist_task_dev order by device_serialnumber)");
			
			list=jt.queryForList(psql.getSQL());
			if(list!=null && !list.isEmpty())
			{
				psql=null;
				psql = new PrepareSQL();
				psql.append("insert into tab_blacklist_task_dev(device_id,device_serialnumber,pppoe_name,city_id,add_time,task_id,status) ");
				psql.append("values(?,?,?,?,?,?,?,?,?) ");
				
				for(int i=0;i<list.size();i++){
					Map m=(Map) list.get(i);
					psql.setString(1,StringUtil.getStringValue(m, "device_id"));
					psql.setString(2,StringUtil.getStringValue(m, "device_serialnumber"));
					psql.setString(3,StringUtil.getStringValue(m, "pppoe_name"));
					psql.setString(4,StringUtil.getStringValue(m, "city_id"));
					psql.setLong(5,System.currentTimeMillis()/1000);
					psql.setLong(6,task_id);
					psql.setInt(7,1);
					
					recode = jt.update(psql.getSQL());
				}
				
				logger.warn("高级设置，插入黑名表详细表已完成，数据量为：" + list.size());
				list.clear();
			}
		}
		else
		{
			StringBuilder sql = new StringBuilder();
			StringBuilder sql_dev = new StringBuilder();
			sql.append("merge into tab_speed_blacklist c using (");
			sql_dev.append("merge into tab_blacklist_task_dev c using(");
			String queDeSql = "select a.device_id,a.device_serialnumber,b.pppoe_name," +
					"a.vendor_id,a.device_model_id,a.devicetype_id,a.city_id " +
					"from tab_gw_device a left join tab_sn_account b " +
					"on a.device_serialnumber=b.device_serialnumber where";
			sql.append(queDeSql);
			sql_dev.append(queDeSql);
			// 属地和厂商两者必选一或都选
			if (null != param[0])
			{
				sql.append(" a.city_id=" + param[0]);
				sql_dev.append(" a.city_id=" + param[0]);
			}
			if (null != param[1])
			{
				if (null != param[0])
				{
					sql.append(" and a.vendor_id=" + param[1]);
					sql_dev.append(" and a.vendor_id=" + param[1]);
				}
				else
				{
					sql.append(" a.vendor_id=" + param[1]);
					sql_dev.append(" a.vendor_id=" + param[1]);
				}
			}
			if (null != param[2] && param[2]!="-1" && param[2]!="")
			{
				sql.append(" and a.device_model_id=" + param[2]);
				sql_dev.append(" and a.device_model_id=" + param[2]);
			}
			if (null != param[3] && param[3]!="-1" && param[3]!="")
			{
				sql.append(" and a.devicetype_id=" + param[3]);
				sql_dev.append(" and a.devicetype_id=" + param[3]);
			}
			sql.append(") d on (c.device_serialnumber=d.device_serialnumber) when matched then update set c.device_id=d.device_id when not matched then insert (c.device_id,c.device_serialnumber,c.pppoe_name,c.vendor_id,c.device_model_id,c.devicetype_id,c.city_id,c.is_blacklist,c.add_time) values(d.device_id,d.device_serialnumber,d.pppoe_name,d.vendor_id,d.device_model_id,d.devicetype_id,d.city_id,1,");
			sql.append(new DateTimeUtil().getLongTime() + ")");
			sql_dev.append(") d on(c.device_serialnumber=d.device_serialnumber) when matched then update set c.device_id=d.device_id when not matched then insert (c.device_id,c.device_serialnumber,c.pppoe_name,c.city_id,c.add_time,c.task_id,c.status) values(d.device_id,d.device_serialnumber,d.pppoe_name,d.city_id,");
			sql_dev.append(new DateTimeUtil().getLongTime());
			sql_dev.append("," + task_id + ",1)");
			PrepareSQL psql = new PrepareSQL(sql.toString()); // 黑名单表sql配置完成
			// 添加进黑名单表
			int recode = jt.update(psql.getSQL());
			logger.warn("高级设置，插入黑名表已完成，插入数据量为：" + recode);
					
			PrepareSQL psql_dev = new PrepareSQL(sql_dev.toString()); // 黑名单任务详细sql配置完成
			// 添加进黑名单任务详细表
			int recode_dev = jt.update(psql_dev.getSQL());
			logger.warn("高级设置，插入黑名表详细表已完成，插入数据量为：" + recode_dev);
		}
		
	}

	/**
	 * 根据传入的deviceID删除黑名单表中的数据
	 * 
	 * @param param
	 * @return 删除的条数
	 */
	public int removeBLbyDeviceIds(Object[] param)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("delete from tab_speed_blacklist where device_id in(");
		for (int i = 0; i < param.length; i++)
		{
			sql.append(param[i] + ",");
		}
		sql.append("0)"); // 拼接sql需要，因为device_id不可能为0
		PrepareSQL psql = new PrepareSQL(sql.toString());
		logger.warn("移除黑名单sql" + psql);
		jt.update(psql.getSQL());
		// 黑名单任务详细表内容也要删除
		StringBuilder sql_1 = new StringBuilder();
		sql_1.append("delete from tab_blacklist_task_dev where device_id in(");
		for (int i = 0; i < param.length; i++)
		{
			sql_1.append(param[i] + ",");
		}
		sql_1.append("0)"); // 拼接sql需要，因为device_id不可能为0
		PrepareSQL psql_1 = new PrepareSQL(sql_1.toString());
		logger.warn("移除黑名单任务详细表sql" + psql_1);
		return jt.update(psql_1.getSQL());
	}
}
