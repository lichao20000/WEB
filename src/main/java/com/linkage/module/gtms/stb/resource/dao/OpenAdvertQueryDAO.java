
package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.utils.DeviceTypeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;


/**
 * 开机广告查询
 *
 * @author os_hanzz
 */
public class OpenAdvertQueryDAO extends SuperDAO
{

	// 日志操作
	Logger logger = LoggerFactory.getLogger(OpenAdvertQueryDAO.class);
	private  HashMap<String,String> deviceTypeMap = null;
	private  HashMap<String,String> faultCodeMap = null;
	/**
	 * 开机广告结果统计
	 * @param taskId
	 * @param taskName
	 * @param vendorName
	 * @param deviceModel
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String,String>> queryAdvertResultCount(String taskId, String taskName,String vendorId, String deviceModelId)
	{
		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
		psql.append("select b.vendor_id,b.device_model_id,b.devicetype_id,a.result_id,a.task_id task_id,count(*) total");
		psql.append(" from stb_logo_record a,stb_tab_gw_device b,stb_logo_task c");
		psql.append(" where a.device_id=b.device_id and a.task_id=c.task_id ");
		if(!StringUtil.IsEmpty(taskId)){
			psql.append(" and a.task_id="+taskId);
		}
		if(!StringUtil.IsEmpty(taskName)){
			psql.append(" and c.task_name='"+taskName+"'");
		}
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			psql.append(" and b.vendor_id='"+vendorId+"'");
		}
		if(!StringUtil.IsEmpty(deviceModelId)&&!"-1".equals(deviceModelId)){
			psql.append(" and b.device_model_id='"+deviceModelId+"'");
		}
		psql.append(" group by b.vendor_id,b.device_model_id,b.devicetype_id,a.result_id,a.task_id");

		List<Map<String,String>> list = jt.queryForList(psql.getSQL());

		logger.warn("OpenAdvertQueryDAO->list.size={}",list.size());
		return list;
	}

	/**
	 * 总配置数
	 * 厂商、型号、软件版本对应一条数据
	 * @param taskId
	 * @param taskName
	 * @param vendorId
	 * @param deviceModelId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> queryTotal(String taskId, String taskName,String vendorId, String deviceModelId){
		/*map用户存放list中总的记录数*/
		Map<String,String> map = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
		psql.append("select b.vendor_id,b.device_model_id,b.devicetype_id,count(*) total");
		psql.append(" from stb_logo_recent a,stb_tab_gw_device b,stb_logo_task c");
		psql.append(" where a.device_id=b.device_id and a.task_id=c.task_id ");

		if(!StringUtil.IsEmpty(taskId)){
			psql.append(" and a.task_id="+taskId);
		}
		if(!StringUtil.IsEmpty(taskName)){
			psql.append(" and c.task_name='"+taskName+"'");
		}
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			psql.append(" and b.vendor_id='"+vendorId+"'");
		}
		if(!StringUtil.IsEmpty(deviceModelId)&&!"-1".equals(deviceModelId)){
			psql.append(" and b.device_model_id='"+deviceModelId+"'");
		}
		psql.append(" group by b.vendor_id,b.device_model_id,b.devicetype_id");


		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		if(null!=list)
		{
			if(list.size()>0)
			{
				for (int i = 0; i < list.size(); i++)
				{
					Map<String,String> rmap = list.get(i);
					logger.warn("OpenAdvertQueryDAO->rmap={}",rmap);
					String vendor_id = StringUtil.getStringValue(rmap.get("vendor_id"));
					String deviceModel_id = StringUtil.getStringValue(rmap.get("device_model_id"));
					String devicetype_id = StringUtil.getStringValue(rmap.get("devicetype_id"));
					/*vendor_id+deviceModel_id+devicetype_id确定设备*/
					String total = StringUtil.getStringValue(rmap.get("total"));
					map.put(vendor_id+deviceModel_id+devicetype_id, total);
				}

			}
		}
		return map;
	}
	/**
	 * 获取详细信息
	 * @param taskId
	 * @param taskName
	 * @param vendorId
	 * @param deviceModelId
	 * @param queryType       0：成功，1：失败，2：未触发，3:总配置数
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> queryTotalList(String taskId, String taskName,String vendorId, String devicetype_id,String queryType,int curPage_splitPage,int num_splitPage)
	{
		deviceTypeMap =  this.getDeviceType();
		faultCodeMap = this.getFaultCode();
		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
		psql.append("select b.vendor_id,b.device_model_id,b.devicetype_id,b.city_id,b.cpe_mac,b.serv_account,b.loopback_ip,a.result_id,a.start_time,c.task_name,b.device_serialnumber");
		psql.append(" from stb_logo_record a,stb_tab_gw_device b,stb_logo_task c");
		psql.append(" where a.device_id=b.device_id and a.task_id=c.task_id");

		if(queryType.equals("0"))
		{
			psql.append(" and a.result_id =1");
		}else if(queryType.equals("1"))
		{
			psql.append(" and a.result_id !=1 and a.result_id !=0");
		}else if(queryType.equals("2"))
		{
			psql.append(" and a.result_id =0");
		}
		if(!StringUtil.IsEmpty(taskId)){
			psql.append(" and a.task_id="+taskId);
		}
		if(!StringUtil.IsEmpty(taskName)){
			psql.append(" and a.task_name="+taskId);
		}
		if(!StringUtil.IsEmpty(vendorId)){
			psql.append(" and b.vendor_id='"+vendorId+"'");
		}
		if(!StringUtil.IsEmpty(devicetype_id)){
			psql.append(" and b.devicetype_id="+devicetype_id);
		}
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1)* num_splitPage, num_splitPage, new RowMapper()
		{

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("cityName",CityDAO.getCityName(String.valueOf(rs.getString("city_id"))));
				try {
					long update_time = StringUtil.getLongValue(rs
							.getString("start_time"));
					if (update_time == 0)
					{
						map.put("update_time", "");
					} else
					{
						DateTimeUtil dt = new DateTimeUtil(update_time * 1000);
						map.put("update_time", dt.getLongDate());
					}
				} catch (NumberFormatException e) {
					map.put("update_time", "");
				} catch (Exception e) {
					map.put("update_time", "");
				}
				map.put("vendorName",DeviceTypeUtil.getVendorName(StringUtil.getStringValue(rs.getString("vendor_id"))));
				map.put("deviceTypeName", deviceTypeMap.get(StringUtil.getStringValue(rs.getString("devicetype_id"))));
				map.put("deviceModel", DeviceTypeUtil.getDeviceModel(StringUtil.getStringValue(rs.getString("device_model_id"))));
				map.put("deviceSerialNumber", StringUtil.getStringValue(rs.getString("device_serialnumber")));
				map.put("servAccount", StringUtil.getStringValue(rs.getString("serv_account")));
				map.put("loopback_ip", StringUtil.getStringValue(rs.getString("loopback_ip")));
				map.put("cpe_mac", StringUtil.getStringValue(rs.getString("cpe_mac")));
				map.put("taskName", StringUtil.getStringValue(rs.getString("task_name")));
				String desc=faultCodeMap.get(StringUtil.getStringValue(rs.getString("result_id")));
				map.put("result", StringUtil.IsEmpty(desc)?"未触发":desc);

				return map;
			}
		});
		return  list;
	}

	/**
	 * 获取详细信息(分页)
	 * @param taskId
	 * @param taskName
	 * @param vendorId
	 * @param deviceModelId
	 * @param queryType       0：成功，1：失败，2：未触发，3:总配置数
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int queryTotalListCount(String taskId, String taskName,String vendorId, String devicetype_id,String queryType,int curPage_splitPage,int num_splitPage)
	{
		PrepareSQL psql = new PrepareSQL();
		if(queryType.equals("0"))
		{// TODO wait (more table related)
			psql.append("select count(*)");
			psql.append(" from stb_logo_record a,stb_tab_gw_device b,stb_logo_task c");
			psql.append(" where a.device_id=b.device_id and a.task_id=c.task_id and a.result_id in(0,1)");
		}else if(queryType.equals("1"))
		{// TODO wait (more table related)
			psql.append("select count(*)");
			psql.append(" from stb_logo_record a,stb_tab_gw_device b,stb_logo_task c");
			psql.append(" where a.device_id=b.device_id and a.task_id=c.task_id and a.result_id not in(0,1)");
		}else if(queryType.equals("2"))
		{// TODO wait (more table related)
			psql.append("select count(*)");
			psql.append(" from stb_logo_recent a,stb_tab_gw_device b,stb_logo_task c ");
			psql.append(" where a.device_id=b.device_id and a.task_id=c.task_id and a.status=0");
		}else
		{// TODO wait (more table related)
			psql.append("select count(*)");
			psql.append(" from stb_logo_recent a,stb_tab_gw_device b,stb_logo_task c");
			psql.append(" where a.device_id=b.device_id and a.task_id=c.task_id");
		}
		if(!StringUtil.IsEmpty(taskId)){
			psql.append(" and a.task_id="+taskId);
		}
		if(!StringUtil.IsEmpty(taskName)){
			psql.append(" and c.task_name='"+taskName+"'");
		}
		if(!StringUtil.IsEmpty(vendorId)){
			psql.append(" and b.vendor_id='"+vendorId+"'");
		}
		if(!StringUtil.IsEmpty(devicetype_id)){
			psql.append(" and b.devicetype_id="+devicetype_id);
		}
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * @category getVendor 获取所有的厂商
	 *
	 * @param city_id
	 *
	 * @return List
	 */
	public List getVendor()
	{
		PrepareSQL pSQL = new PrepareSQL("select vendor_id,vendor_name, vendor_add from stb_tab_vendor");
		return jt.queryForList(pSQL.toString());
	}

	/**
	 * @category getDevicetype 获取所有的设备型号
	 *
	 * @param vendorId
	 *
	 * @return List
	 */
	public List getDeviceModel(String vendorId) {
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append(" select a.device_model_id,a.device_model from stb_gw_device_model a where 1=1 ");
		if (null != vendorId && !"".equals(vendorId) && !"-1".equals(vendorId)) {
			pSQL.append(" and a.vendor_id='");
			pSQL.append(vendorId);
			pSQL.append("'");
		}
		return jt.queryForList(pSQL.toString());
	}


	public  HashMap<String,String>  getFaultCode() {
		logger.debug("getFaultCode()");
		faultCodeMap = new HashMap<String,String>();
		String sql = "select fault_code,fault_desc  from tab_cpe_faultcode";

		PrepareSQL psql = new PrepareSQL(sql);
		List<HashMap<String,String>> faultCodeList = DBOperation.getRecords(psql.getSQL());
		for (HashMap<String,String> map : faultCodeList)
		{
			faultCodeMap.put(map.get("fault_code"), map.get("fault_desc"));

		}
		return  faultCodeMap;
	}
	private  HashMap<String,String>  getDeviceType()
	{
		logger.debug("getDeviceType");
		 deviceTypeMap = new HashMap<String,String>();
		PrepareSQL pSQL = new PrepareSQL("select a.devicetype_id,a.softwareversion from stb_tab_devicetype_info a  ");


		List<HashMap<String,String>> deviceTypelList = DBOperation.getRecords(pSQL.getSQL());
		for (HashMap<String,String> map:deviceTypelList)
		{
			deviceTypeMap.put(map.get("devicetype_id"), map.get("softwareversion"));

		}
		return  deviceTypeMap;
	}
}
