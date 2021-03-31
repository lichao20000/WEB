/**
 * 
 */
package com.linkage.module.gwms.report.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-9-9
 * @category com.linkage.module.gwms.report.dao
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DevicetypeNewestFindReportDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(DevicetypeNewestFindReportDAO.class);
	
	/**
	 * 查出所有需要查询的版本及其相关的型号、厂商
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List getDevicetype(long startTime,long endTime)
	{
		logger.debug("getDevicetype(startTime:{},endTime:{})",startTime,endTime);
		List list;
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select a.vendor_id,c.vendor_add,a.device_model_id,devicetype_id ");
			psql.append("from tab_devicetype_info a,tab_vendor c ");
			psql.append("where a.vendor_id=c.vendor_id and a.add_time>"+startTime);
			psql.append(" and a.add_time<"+endTime);
			psql.append(" order by  a.vendor_id,devicetype_id");
			
			list=jt.queryForList(psql.getSQL());
			if(list!=null && list.isEmpty()){
				Map<String,String> dti=getDeviceTypeInfo();
				for(int i=0;i<list.size();i++){
					Map m=(Map) list.get(i);
					
					String softwareversion=dti.get(StringUtil.getStringValue(m,"devicetype_id"));
					if(StringUtil.IsEmpty(softwareversion)){
						continue;
					}
					
					m.put("softwareversion", softwareversion);
				}
			}
		}else{
			psql.append("select a.vendor_id,c.vendor_add,device_model,devicetype_id,");
			psql.append("softwareversion from tab_devicetype_info a,gw_device_model b,");
			psql.append("tab_vendor c where a.device_model_id=b.device_model_id ");
			psql.append("and a.vendor_id=c.vendor_id and a.add_time>"+startTime);
			psql.append(" and a.add_time<"+endTime);
			psql.append(" order by  a.vendor_id,device_model,devicetype_id");
			
			list=jt.queryForList(psql.getSQL());
		}
		
		return list;
	}
	
	/**
	 * 根据属地查询指定版本的终端数
	 * 
	 * @param typeList
	 * @return
	 */
	public List getdeviceNumByType(List<String> typeList,String gw_type)
	{
		logger.debug("getdeviceNumByType(typeList:{})",typeList);
		
		StringBuffer sql = new StringBuffer();
		sql.append("select count(devicetype_id) as num,devicetype_id,city_id from tab_gw_device");
		if(null==typeList || typeList.size()<1){
			return null;
		}else{
			sql.append(" where devicetype_id in (");
			for(String type:typeList){
				sql.append(type+",");
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(") ");
		}
		if(!StringUtil.IsEmpty(gw_type)){
			sql.append(" and gw_type=").append(gw_type);
		}
		sql.append(" group by devicetype_id,city_id order by city_id ");
		
		logger.debug("getdeviceNumByType=>sql:{}",sql);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 获取所有版本
	 */
	private Map<String,String> getDeviceTypeInfo()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select devicetype_id,softwareversion ");
		psql.append("from tab_devicetype_info order by devicetype_id ");
		List<Map> list=jt.queryForList(psql.getSQL());
		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty()){
			for(Map m:list){
				map.put(StringUtil.getStringValue(m,"devicetype_id"),
						StringUtil.getStringValue(m,"softwareversion"));
			}
		}
		
		return map;
	}
}
