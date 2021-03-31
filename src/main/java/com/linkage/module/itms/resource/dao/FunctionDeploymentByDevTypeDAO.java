
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-12-9
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class FunctionDeploymentByDevTypeDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory
			.getLogger(FunctionDeploymentByDevTypeDAO.class);
	private Map<String, String> modelMap = new HashMap<String, String>();
	private Map<String, String> cityMap = new HashMap<String, String>();
	private Map<String,String> statusMap = new HashMap<String, String>();
	private String tableName = LipossGlobals.getLipossProperty("strategy_tabname.batch.tabname");
	
	public FunctionDeploymentByDevTypeDAO(){
		statusMap.put("100", "成功");
	}

	/**
	 * @category getVendor 获取所有的厂商
	 * @return
	 */
	public Map<String, String> getVendor()
	{
		logger.debug("getVendor()");
		Map<String, String> map = new HashMap<String, String>();
		PrepareSQL pSQL = new PrepareSQL(
				"select vendor_id,vendor_name, vendor_add from tab_vendor");
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(pSQL.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				String tempValue = StringUtil.getStringValue(list.get(i)
						.get("vendor_add"))
						+ "("
						+ StringUtil.getStringValue(list.get(i).get("vendor_name")) + ")";
				map.put(StringUtil.getStringValue(list.get(i).get("vendor_id")),
						tempValue);
			}
		}
		return map;
	}

	/**
	 * 设备型号信息
	 * 
	 * @return
	 */
	public List<String> getModel(String vendorId, String modelId)
	{
		logger.warn("DeviceTypeVoiceQueryDAO=>getModel()");
		List<String> tempList = new ArrayList<String>();
		PrepareSQL pSQL = new PrepareSQL(
				" select a.device_model_id from gw_device_model a where 1=1 ");
		pSQL.append(" and a.vendor_id='");
		pSQL.append(vendorId);
		pSQL.append("' ");
		if (!StringUtil.IsEmpty(modelId) && !"-1".equals(modelId))
		{
			pSQL.append(" and a.device_model_id='");
			pSQL.append(modelId);
			pSQL.append("'");
		}
		List<Map> modellist = jt.queryForList(pSQL.getSQL());
		if (null != modellist && modellist.size() > 0)
		{
			for (int i = 0; i < modellist.size(); i++)
			{
				String device_model_id = StringUtil.getStringValue(modellist.get(i).get(
						"device_model_id"));
				tempList.add(device_model_id);
			}
		}
		return tempList;
	}

	/**
	 * @category getDevicetype 获取所有的设备型号
	 * @param vendorId
	 * @return List
	 */
	public Map<String, String> getDeviceModel()
	{
		logger.debug("DeviceServiceDAO=>getDeviceModel()");
		Map<String, String> modelMap = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL(
				"select device_model_id,device_model from gw_device_model");
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				modelMap.put(
						StringUtil.getStringValue(list.get(i).get("device_model_id")),
						StringUtil.getStringValue(list.get(i).get("device_model")));
			}
		}
		return modelMap;
	}

	/**
	 * 查询新增部署功能设备型号情况
	 * 
	 * @param edtionIdBuffer
	 * @param end_time
	 * @param gn
	 * @return
	 */
	public Map<String, String> quertFunctionDeployByDevType(String edtionIdBuffer,
			String end_time, String gn)
	{
		logger.debug("FunctionDeploymentByDevTypeDAO==>quertFunctionDeployByDevType");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_model_id,count(1) num from  tab_gw_device a,  " + tableName + " b where a.device_id = b.device_id and b.status=100 and b.result_id=1 ");
		if (!StringUtil.IsEmpty(edtionIdBuffer))
		{
			sql.append(" and a.device_model_id in ").append(edtionIdBuffer);
		}
		if (!StringUtil.IsEmpty(gn) && !"-1".equals(gn))
		{
			sql.append(" and b.service_id=").append(gn);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and b.start_time<=").append(end_time);
		}
		sql.append(" group by a.device_model_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("device_model_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}

	/**
	 * 新增部署功能设备型号明细信息
	 * 
	 * @param edtionIdBuffer
	 * @param end_time
	 * @param gn
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> quertFunctionDeployByDevTypeList(String edtionIdBuffer,
			String end_time, String gn, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("FunctionDeploymentByDevTypeDAO==>quertFunctionDeployByDevTypeList()");
		StringBuffer sql = new StringBuffer();
		// sql.append("select a.city_id,a.device_serialnumber,b.start_time,c.status,d.timelist,e.username  from  tab_gw_device a,  gw_serv_strategy b , tab_status_report_task_dev c , tab_status_report_task d, tab_hgwcustomer e  ");
		// sql.append(" where a.device_id = b.device_id and a.device_serialnumber=c.device_serialnumber and c.task_id=d.task_id and a.device_id=e.device_id and  b.status=100 and b.result_id=1  ");
		sql.append("select a.city_id,a.device_model_id,a.device_serialnumber,b.start_time,b.status,c.username,d.timelist  from  tab_gw_device a left join   tab_hgwcustomer c on a.device_id=c.device_id ");
		sql.append(" left join (select m.timelist,n.device_serialnumber from tab_status_report_task m, tab_status_report_task_dev n where m.task_id=n.task_id) d on a.device_serialnumber=d.device_serialnumber, ");
		sql.append(" " + tableName + " b ");
		sql.append(" where a.device_id = b.device_id and  b.status=100 and b.result_id=1 ");
		if (!StringUtil.IsEmpty(edtionIdBuffer))
		{
			sql.append(" and a.device_model_id in ").append(edtionIdBuffer);
		}
		if (!StringUtil.IsEmpty(gn) && !"-1".equals(gn))
		{
			sql.append(" and b.service_id=").append(gn);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and b.start_time<=").append(end_time);
		}
		sql.append(" order by a.city_id ");
		modelMap = this.getDeviceModel();
		cityMap = CityDAO.getCityIdCityNameMap();
		PrepareSQL pSql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = querySP(pSql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{

					@Override
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						String device_model_id = StringUtil.getStringValue(rs
								.getString("device_model_id"));
						String cityId = StringUtil.getStringValue(rs.getString("city_id"));
						map.put("city_id", cityId);
						map.put("city_name", cityMap.get(cityId));
						map.put("device_id", device_model_id);
						map.put("device_model", modelMap.get(device_model_id));
						map.put("loid",StringUtil.getStringValue(rs.getString("username")));
						map.put("status",statusMap.get(StringUtil.getStringValue(rs.getString("status"))));
						map.put("device_serialnumber", StringUtil.getStringValue(rs.getString("device_serialnumber")));
						map.put("timelist", StringUtil.getStringValue(rs.getString("timelist")));
						try
						{
							long opertime = StringUtil.getLongValue(rs.getString("start_time")) * 1000L;
							DateTimeUtil dt = new DateTimeUtil(opertime);
							map.put("start_time", dt.getLongDate());
						}
						catch (NumberFormatException e)
						{
							map.put("start_time", "");
						}
						catch (Exception e)
						{
							map.put("start_time", "");
						}
						return map;
					}
				});
		return list;
	}

	/**
	 * 统计新增部署功能设备型号情况
	 * 
	 * @param edtionIdBuffer
	 * @param end_time
	 * @param gn
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int countQuertFunctionDeployByDevTypeList(String edtionIdBuffer,
			String end_time, String gn, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("FunctionDeploymentByDevTypeDAO==>countQuertFunctionDeployByDevTypeList()");
		StringBuffer sql = new StringBuffer();
		// sql.append("select a.city_id,a.device_serialnumber,b.start_time,c.status,d.timelist,e.username  from  tab_gw_device a,  gw_serv_strategy b , tab_status_report_task_dev c , tab_status_report_task d, tab_hgwcustomer e  ");
		// sql.append(" where a.device_id = b.device_id and a.device_serialnumber=c.device_serialnumber and c.task_id=d.task_id and a.device_id=e.device_id and  b.status=100 and b.result_id=1  ");
		sql.append("select count(1) from  tab_gw_device a,  " + tableName + " b  ");
		sql.append(" where a.device_id = b.device_id  and  b.status=100 and b.result_id=1 ");
		if (!StringUtil.IsEmpty(gn) && !"-1".equals(gn))
		{
			sql.append(" and b.service_id=").append(gn);
		}
		if (!StringUtil.IsEmpty(edtionIdBuffer))
		{
			sql.append(" and a.device_model_id in ").append(edtionIdBuffer);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and b.start_time<=").append(end_time);
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

	/**
	 * 导出新增部署功能设备型号情况
	 * 
	 * @param edtionIdBuffer
	 * @param gn
	 * @param end_time
	 * @return
	 */
	public List<Map> excelQuertFunctionDeployByDevTypeList(String edtionIdBuffer,
			String gn, String end_time)
	{
		logger.debug("FunctionDeploymentByDevTypeDAO==>countQuertFunctionDeployByDevTypeList()");
		StringBuffer sql = new StringBuffer();
		// sql.append("select a.city_id,a.device_serialnumber,b.start_time,c.status,d.timelist,e.username  from  tab_gw_device a,  gw_serv_strategy b , tab_status_report_task_dev c , tab_status_report_task d, tab_hgwcustomer e  ");
		// sql.append(" where a.device_id = b.device_id and a.device_serialnumber=c.device_serialnumber and c.task_id=d.task_id and a.device_id=e.device_id and  b.status=100 and b.result_id=1  ");
		//
		
		sql.append("select a.city_id,a.device_model_id,a.device_serialnumber,b.start_time,b.status,c.username,d.timelist  from ");
		sql.append(" tab_gw_device a left join tab_hgwcustomer c on a.device_id=c.device_id ");
		sql.append(" left join (select m.timelist,n.device_serialnumber from tab_status_report_task m, tab_status_report_task_dev n where m.task_id=n.task_id) d on a.device_serialnumber=d.device_serialnumber, ");
		sql.append(" " + tableName + " b ");
		sql.append(" where a.device_id = b.device_id  and  b.status=100 and b.result_id=1 ");
		if (!StringUtil.IsEmpty(gn) && !"-1".equals(gn))
		{
			sql.append(" and b.service_id=").append(gn);
		}
		if (!StringUtil.IsEmpty(edtionIdBuffer))
		{
			sql.append(" and a.device_model_id in ").append(edtionIdBuffer);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and b.start_time<=").append(end_time);
		}
		sql.append(" order by a.city_id ");
		modelMap = this.getDeviceModel();
		cityMap = CityDAO.getCityIdCityNameMap();
		PrepareSQL pSql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(pSql.getSQL());
		if (list != null && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				String device_model_id = StringUtil.getStringValue(list.get(i).get(
						"device_model_id"));
				String cityId = StringUtil.getStringValue(list.get(i).get("city_id"));
				list.get(i).put("city_id", cityId);
				list.get(i).put("city_name", cityMap.get(cityId));
				list.get(i).put("device_id", device_model_id);
				list.get(i).put("device_model", modelMap.get(device_model_id));
				list.get(i).put("loid",StringUtil.getStringValue(list.get(i).get("username")));
				list.get(i).put("device_serialnumber",StringUtil.getStringValue(list.get(i).get("device_serialnumber")));
				list.get(i).put("status",statusMap.get(StringUtil.getStringValue(list.get(i).get("status"))));
				list.get(i).put("timelist",StringUtil.getStringValue(list.get(i).get("timelist")));
				try
				{
					long opertime = StringUtil.getLongValue(list.get(i).get("start_time")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(opertime);
					list.get(i).put("start_time", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					list.get(i).put("start_time", "");
				}
				catch (Exception e)
				{
					list.get(i).put("start_time", "");
				}
			}
		}
		return list;
	}
}
