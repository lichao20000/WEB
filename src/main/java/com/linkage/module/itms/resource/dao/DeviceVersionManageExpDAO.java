/**
 * 
 */
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;

/**
 * 新疆需求：版本库管理
 * @author chenjie
 * @version 1.0
 * @since 2012-12-14
 */
public class DeviceVersionManageExpDAO extends SuperDAO{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(DeviceVersionManageExpDAO.class);
	
	private Map vendorMap;
	private Map deviceModelMap;
	
	
	/**
	 * 查询所有设备版本库信息
	 * @param vendorId
	 * @param deviceModelId
	 * @param hardVersion
	 * @param softVersion
	 * @param specVersion
	 * @param startTime
	 * @param endTime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> queryDeviceVersion(String vendorId, String deviceModelId, String hardVersion, String softVersion, int is_check, int rela_dev_type,  
			String startTime, String endTime, int access_style_relay_id, int spec_id, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryDeviceVersion({})", new Object[]{vendorId, deviceModelId, hardVersion, softVersion, startTime, endTime, curPage_splitPage, num_splitPage });
		
		StringBuffer sb = new StringBuffer();

		// teledb
		if (DBUtil.GetDB() == 3) {
			sb.append("select a.id, a.access_style_relay_id, a.vendor_id, a.device_model_id, a.hardwareversion, a.softwareversion, a.is_check, a.file_path, a.rela_dev_type_id, a.spec_id, a.add_time, b.spec_name as specName from tab_version_info_exp a,tab_bss_dev_port d where a.spec_id = b.spec_id");
		}
		else {
			sb.append("select a.*,b.spec_name as specName from tab_version_info_exp a,tab_bss_dev_port d where a.spec_id = b.spec_id");
		}

		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId))
		{
			sb.append(" and vendor_id = '").append(vendorId).append("'");
		}
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId))
		{
			sb.append(" and device_model_id = '").append(deviceModelId).append("'");
		}
		if(!StringUtil.IsEmpty(hardVersion))
		{
			sb.append(" and hardwareversion = '").append(hardVersion).append("'");
		}
		if(!StringUtil.IsEmpty(softVersion))
		{
			sb.append(" and softwareversion like '" + softVersion + "%'");
		}
		if(!StringUtil.IsEmpty(startTime))
		{
			sb.append(" and add_time >= ").append(startTime);
		}
		if(!StringUtil.IsEmpty(endTime))
		{
			sb.append(" and add_time <= ").append(endTime);
		}
		sb.append( "and is_check=").append(is_check).append( "and rela_dev_type_id=").append(rela_dev_type).append( "and access_style_relay_id=").append(access_style_relay_id).append( "and spec_id=").append(spec_id);
		
		PrepareSQL psql = new PrepareSQL(sb.toString());
		
		// 厂商对应map
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		final Map accessTypeMap = getAccessType();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", String.valueOf(rs.getLong("id")));
				String accessStyleRelayId = StringUtil.getStringValue(rs
						.getString("access_style_relay_id"));
				map.put("vendor_id", rs.getString("vendor_id"));
				map.put("vendor_name", (String)vendorMap.get(rs.getString("vendor_id")));
				map.put("device_model_id", rs.getString("device_model_id"));
				map.put("device_model_name", (String)deviceModelMap.get(rs.getString("device_model_id")));
				map.put("hardversion", StringUtil.getStringValue(rs.getString("hardwareversion")).trim());
				map.put("softversion", StringUtil.getStringValue(rs.getString("softwareversion")).trim());
				map.put("file_path", StringUtil.getStringValue(rs.getString("file_path")).trim());
				map.put("is_check", StringUtil.getStringValue(rs.getString("is_check")));
				map.put("rela_dev_type", StringUtil.getStringValue(rs.getString("rela_dev_type_id")));
				map.put("access_style_relay_id", StringUtil.getStringValue(rs.getString("access_style_relay_id")));
				map.put("spec_id", StringUtil.getStringValue(rs.getString("spec_id")));
				map.put("specName", rs.getString("specName"));
				if (!"".equals(accessStyleRelayId))
				{
					// String type_name = getTypeNameByTypeId(accessStyleRelayId);
					map.put("type_id", accessStyleRelayId);
					map.put("type_name", (String) accessTypeMap.get(accessStyleRelayId));
				}else
				{
						map.put("type_id", "");
						map.put("type_name", "");
				}
				
				// 将add_time转换成时间
				try
				{
					long add_time = rs.getLong("add_time");
					DateTimeUtil dt = new DateTimeUtil(add_time * 1000);
					map.put("add_time", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					map.put("add_time", "");
				}
				catch (Exception e)
				{
					map.put("add_time", "");
				}
				return map;
			}
		});
		
		return list;
	}
	
	public Map getAccessType(){
		String sql="select * from gw_access_type ";
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql="select type_id, type_name from gw_access_type ";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		Map<String,String> AccessMap=new HashMap<String,String>();
	list=jt.queryForList(sql);
	for(Map accessMap : list){
		AccessMap.put(accessMap.get("type_id").toString(), (String)accessMap.get("type_name"));
	}
	return AccessMap;
	}
	/**
	 * 统计所有设备版本库信息
	 * @param vendorId
	 * @param deviceModelId
	 * @param hardVersion
	 * @param softVersion
	 * @param specVersion
	 * @param startTime
	 * @param endTime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int countDeviceVersion(String vendorId, String deviceModelId, String hardVersion, String softVersion, int is_check, int rela_dev_type,  
			String startTime, String endTime, int access_style_relay_id, int spec_id, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("countDeviceVersion({})", new Object[]{vendorId, deviceModelId, hardVersion, softVersion,
				startTime, endTime, curPage_splitPage, num_splitPage});
		
		StringBuffer sb = new StringBuffer();
		sb.append("select count(1) from tab_version_info where 1=1 ");
		
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId))
		{
			sb.append(" and vendor_id = '").append(vendorId).append("'");
		}
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId))
		{
			sb.append(" and device_model_id = '").append(deviceModelId).append("'");
		}
		if(!StringUtil.IsEmpty(hardVersion))
		{
			sb.append(" and hardwareversion = '").append(hardVersion).append("'");
		}
		if(!StringUtil.IsEmpty(softVersion))
		{
			sb.append(" and softwareversion like '" + softVersion + "%'");
		}
		if(!StringUtil.IsEmpty(startTime))
		{
			sb.append(" and add_time >= ").append(startTime);
		}
		if(!StringUtil.IsEmpty(endTime))
		{
			sb.append(" and add_time <= ").append(endTime);
		}
		sb.append( "and is_check=").append(is_check).append( "and rela_dev_type_id=").append(rela_dev_type).append( "and access_style_relay_id=").append(access_style_relay_id).append( "and spec_id=").append(spec_id);

		PrepareSQL psql = new PrepareSQL(sb.toString());
		
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
	 * 添加版本库文件 
	 * @param vendor
	 * @param device_model
	 * @param hard_version
	 * @param soft_version
	 * @param spec_version
	 * @param file_path
	 * @param addTime
	 */
	public int addDeviceVersion(String vendor, String device_model, String hard_version, String soft_version, String spec_version, String file_path, long addTime) {
		logger.debug("addDeviceVersion({})", new Object[]{vendor, device_model, hard_version, soft_version, spec_version, file_path, addTime});
		
		// 获取最大id
		long max_id = DataSetBean.getMaxId("tab_version_info", "id");
		PrepareSQL psql = new PrepareSQL("insert into tab_version_info(id,vendor_id,device_model_id,specversion,hardwareversion,softwareversion,add_time,file_path) values(?,?,?,?,?,?,?,?)");
		psql.setLong(1, max_id);
		psql.setString(2, vendor);
		psql.setString(3, device_model);
		psql.setString(4, spec_version);
		psql.setString(5, hard_version);
		psql.setString(6, soft_version);
		psql.setLong(7, addTime);
		psql.setString(8, file_path);
		
		int result = DBOperation.executeUpdate(psql.getSQL());
		return result;
	}

	/**
	 * 获取修改的对象
	 * @param id
	 * @return
	 */
	public Map queryForModify(String id) {
		logger.debug("queryForModify({})", id);
		String sql = "select * from tab_version_info_exp where id=?";
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql = "select vendor_id, device_model_id from tab_version_info_exp where id=?";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setLong(1, Long.parseLong(id));
		Map record = DBOperation.getRecord(psql.getSQL());
		return record;
	}

	
	/**
	  * 查询设备类型
	  * 
	  */
	 public List<Map<String,String>> getGwDevType(){
		 PrepareSQL psql = new PrepareSQL("select type_id,type_name from gw_dev_type");
		 return  jt.queryForList(psql.getSQL());
	 }
	/**
	 * 修改版本库
	 * @param id
	 * @param vendor
	 * @param device_model
	 * @param hard_version
	 * @param soft_version
	 * @param spec_version
	 * @param file_path
	 * @param addTime
	 * @return
	 */
	public int modifyDeviceVersion(String id, String vendor, String device_model, 
			String hard_version, String soft_version, String spec_version, String file_path, long addTime) {
		logger.debug("modifyDeviceVersion({})", new Object[]{id, vendor, device_model, hard_version, soft_version, spec_version, file_path, addTime});
		
		StringBuffer sb = new StringBuffer("update tab_version_info set vendor_id=?, device_model_id=?, hardwareversion=?,  softwareversion=?, specversion=?");
		if(!StringUtil.IsEmpty(file_path))
		{
			sb.append(" ,file_path='").append(file_path).append("'");
		}
		sb.append(" where 1=1 and id=?");
		
		PrepareSQL psql = new PrepareSQL(sb.toString());
		psql.setString(1, vendor);
		psql.setString(2, device_model);
		psql.setString(3, hard_version);
		psql.setString(4, soft_version);
		psql.setString(5, spec_version);
		psql.setLong(6, Long.parseLong(id));
		
		return DBOperation.executeUpdate(psql.getSQL());
	}
	
	/**
	 * 获取指定厂商下面的型号
	 * @param vendor
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getDeviceModelByVendor(String vendor)
	{
		logger.debug("getServType()");
		String sql = "select  device_model_id, device_model from gw_device_model where vendor_id=?";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, vendor);
		
		List<Map> list = jt.queryForList(psql.getSQL());
		
		return list;
	}

	/**
	 * 删除版本库
	 * @param id
	 * @return
	 */
	public int deleteDeviceVersion(String id) {
		logger.debug("deleteDeviceVersion({})", id);
		
		PrepareSQL psql = new PrepareSQL("delete from tab_version_info_exp where 1=1 and id=?");
		psql.setLong(1, Long.parseLong(id));
		
		return DBOperation.executeUpdate(psql.getSQL());
	}

	/**
	 * 导出版本库内容
	 * @param vendor
	 * @param device_model
	 * @param hard_version
	 * @param soft_version
	 * @param spec_version
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public List<Map> queryDeviceVersion(String vendor, String device_model, String hard_version, String soft_version, String spec_version, String start_time, String end_time) {
		logger.debug("queryDeviceVersion({})", new Object[]{vendor, device_model, hard_version, soft_version, spec_version, start_time, end_time});
		
		StringBuffer sb = new StringBuffer();
		// teledb
		if (DBUtil.GetDB() == 3) {
			sb.append("select add_time, vendor_id, device_model_id from tab_version_info where 1=1 ");
		}
		else {
			sb.append("select * from tab_version_info where 1=1 ");
		}
		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor))
		{
			sb.append(" and vendor_id = '").append(vendor).append("'");
		}
		if(!StringUtil.IsEmpty(device_model) && !"-1".equals(device_model))
		{
			sb.append(" and device_model_id = '").append(device_model).append("'");
		}
		if(!StringUtil.IsEmpty(hard_version))
		{
			sb.append(" and hardwareversion = '").append(hard_version).append("'");
		}
		if(!StringUtil.IsEmpty(soft_version))
		{
			sb.append(" and softwareversion like '" + soft_version + "%'");
		}
		if(!StringUtil.IsEmpty(spec_version))
		{
			sb.append(" and specversion = '").append(spec_version).append("'");
		}
		if(!StringUtil.IsEmpty(start_time))
		{
			sb.append(" and add_time >= ").append(start_time);
		}
		if(!StringUtil.IsEmpty(end_time))
		{
			sb.append(" and add_time <= ").append(end_time);
		}
		
		PrepareSQL psql = new PrepareSQL(sb.toString());
		
		// 厂商、型号对应map
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		
		List <Map> list = new ArrayList<Map>();
		ArrayList<HashMap<String, String>>  queryResult = DBOperation.getRecords(psql.getSQL());
		
		for(HashMap<String, String> map : queryResult)
		{
			// 将add_time转换成时间
			try
			{
				long add_time = Long.parseLong(map.get("add_time"));
				DateTimeUtil dt = new DateTimeUtil(add_time * 1000);
				map.put("add_time", dt.getLongDate());
			}
			catch (NumberFormatException e)
			{
				map.put("add_time", "");
			}
			catch (Exception e)
			{
				map.put("add_time", "");
			}
			
			// 转化厂商和型号
			map.put("vendor_name", (String)vendorMap.get(map.get("vendor_id")));
			map.put("device_model_name", (String)deviceModelMap.get(map.get("device_model_id")));
			
			list.add(map);
		}
		
		return list;
	}
	
}
