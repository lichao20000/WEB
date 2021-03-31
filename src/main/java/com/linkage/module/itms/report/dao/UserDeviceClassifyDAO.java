
package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 用户设备分类统计(桥接 路由 e8c e8b 是否支持wifi)及其详细信息DAO
 * @author Fanjm 35572
 * @version 1.0
 * @since 2016年11月21日
 * @category com.linkage.module.itms.report.dao
 * @copyright 2016 亚信安全.版权所有
 */
@SuppressWarnings("unchecked")
public class UserDeviceClassifyDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(UserDeviceClassifyDAO.class);
	
	//页面查询时不同分类类型对应不同sql
	private final static String DEVICE_TYPE_KEY_E8B = "device_type_e8b";
	private final static String DEVICE_TYPE_KEY_E8C = "device_type_e8c";
	private final static String DEVICE_TYPE_KEY_E8CME = "device_type_e8cme";
	private final static String WLAN_KEY_SUPPORT = "wlan_num_support";
	private final static String WAN_TYPE_KEY1 = "wan_type1";
	private final static String WAN_TYPE_KEY2 = "wan_type2";
	
	
	
	/**
	 * 根据条件查询指定市级地区的下一级地区符合要求用户数，不包含自己
	 * @param starttime1 开通开始时间
	 * @param endtime1 开通结束时间
	 * @param cityId 市地区ID
	 * @return 结果集合
	 */
	public List<Map<Object,Object>> qryWanTypeMap_nroot(String starttime1, String endtime1, String cityId) 
	{
		logger.debug("qryWanTypeMap_nroot({},{},{})", new Object[] { starttime1,
				endtime1, cityId });
		StringBuffer sql = new StringBuffer();
		
		sql.append("select count(distinct b.user_id) wanTypeNum,a.city_id, b.wan_type ");
		sql.append("from tab_hgwcustomer a,hgwcust_serv_info b ,tab_city c ");
		sql.append("where a.city_id=c.city_id and (c.parent_id='"+cityId+"' or c.city_id='"+cityId+"') ");
		sql.append("and a.user_id=b.user_id and b.wan_type in (1,2) and b.serv_type_id=10 ");
		sql.append("and a.opendate<="+endtime1+" and a.opendate>="+starttime1);
		sql.append(" group by a.city_id,b.wan_type");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map<Object,Object>> res =  jt.queryForList(psql.getSQL());
		return res;
	}
	
	
	/**
	 * 根据条件查询指定省级地区下的市级地区(包含各市地区下的子地区)符合要求用户数，不包含自己
	 * @param starttime1 开通开始时间
	 * @param endtime1 开通结束时间
	 * @param cityId 省地区ID(00)
	 * @return 结果集合
	 */
	public List<Map<Object,Object>> qryWanTypeMap_root(String starttime1, String endtime1, String cityId) 
	{
		logger.debug("qryWanTypeMap_root({},{},{})", new Object[] { starttime1, endtime1, cityId });
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		sql.append("select sum(x.num) wanTypeNum, x.city_id, x.wan_type from ");
		sql.append("(");
			sql.append("select count(distinct b.user_id) num,c.parent_id city_id,b.wan_type ");
			sql.append("from tab_hgwcustomer a,hgwcust_serv_info b,tab_city c ");
			sql.append("where a.city_id=c.city_id and c.parent_id in (select city_id from tab_city where parent_id='"+cityId+"') ");
			sql.append("and a.user_id=b.user_id and b.serv_type_id=10 and b.wan_type in (1,2) ");
			sql.append("and a.opendate<="+endtime1+" and a.opendate>="+starttime1);
			sql.append(" group by c.parent_id, b.wan_type ");
		sql.append("union all ");
			sql.append("select count(distinct b.user_id) num,a.city_id, b.wan_type ");
			sql.append("from tab_hgwcustomer a,hgwcust_serv_info b ");
			sql.append("where (a.city_id in (select city_id from tab_city where parent_id='"+cityId+"') or a.city_id='"+cityId+"') ");
			sql.append("and a.user_id=b.user_id and b.wan_type in (1,2) and b.serv_type_id=10 ");
			sql.append("and a.opendate<="+endtime1+" and a.opendate>="+starttime1);
			sql.append(" group by a.city_id, b.wan_type");
		sql.append(") x ");
		sql.append("group by x.city_id, x.wan_type");

		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	
	
	
	/**
	 * 根据条件查询指定市级地区的下一级地区符合要求设备数，不包括自己
	 * @param starttime1 系统注册开始时间
	 * @param endtime1 系统注册结束时间
	 * @param cityId 地区ID
	 * @return 结果集合
	 */
	public List<Map<Object, Object>> qryDeviceTypeMap_nroot(String starttime1,String endtime1, String cityId) 
	{
		logger.debug("qryDeviceTypeMap_nroot({},{},{})", new Object[] { starttime1,
				endtime1, cityId });
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		sql.append("select count(d.device_id) DeviceTypeNum,d.city_id,d.device_type ");
		sql.append("from tab_city c,tab_gw_device d,tab_devicetype_info e,tab_bss_dev_port f ");
		sql.append("where d.city_id=c.city_id and d.devicetype_id=e.devicetype_id ");
		sql.append("and e.spec_id = f.id and (c.parent_id='"+cityId+"' or c.city_id='"+cityId+"') ");
		sql.append("and d.device_type in('e8-b','e8c','e8-c') and f.spec_name not like '%ME%' ");
		sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
		sql.append(" group by d.city_id, d.device_type");

		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	
	
	/**
	 * 根据条件查询指定省级地区下的市级地区(包含各市地区下的子地区)符合要求设备数，不包含自己
	 * @param starttime1 系统注册开始时间
	 * @param endtime1 系统注册结束时间
	 * @param cityId 省地区ID(00)
	 * @return 结果集合
	 */
	public List<Map<Object,Object>> qryDeviceTypeMap_root(String starttime1, String endtime1, String cityId) 
	{
		logger.debug("qryDeviceTypeMap_root({},{},{})", new Object[] { starttime1,
				endtime1, cityId });
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		sql.append("select sum(x.num) DeviceTypeNum, x.city_id,x.device_type from ");
		sql.append("(");
			sql.append("select count(d.device_id) num,c.parent_id city_id,d.device_type ");
			sql.append("from tab_city c,tab_gw_device d,tab_devicetype_info e,tab_bss_dev_port f ");
			sql.append("where c.city_id=d.city_id and d.devicetype_id=e.devicetype_id and e.spec_id=f.id ");
			sql.append("and c.parent_id in (select city_id from tab_city where parent_id='"+cityId+"') ");
			sql.append("and d.device_type in('e8-b','e8c','e8-c') and f.spec_name not like '%ME%' ");
			sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(" group by c.parent_id, d.device_type ");
		sql.append("union all ");
			sql.append("select count(d.device_id) num,d.city_id,d.device_type ");
			sql.append("from tab_gw_device d,tab_devicetype_info e,tab_bss_dev_port f ");
			sql.append("where d.devicetype_id=e.devicetype_id and e.spec_id=f.id ");
			sql.append("and (d.city_id in (select city_id from tab_city where parent_id='"+cityId+"') or d.city_id='"+cityId+"') ");
			sql.append("and d.device_type in('e8-b','e8c','e8-c') and f.spec_name not like '%ME%' ");
			sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(" group by d.city_id, d.device_type");
		sql.append(") x ");
		sql.append("group by x.city_id, x.device_type");

		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 根据条件查询指定市级地区的下一级地区符合要求设备数 悦me，不包括自己
	 * @param starttime1 系统注册开始时间
	 * @param endtime1 系统注册结束时间
	 * @param cityId 地区ID
	 * @return 结果集合
	 */
	public List<Map<Object, Object>> qryDeviceTypeMEMap_nroot(String starttime1,String endtime1, String cityId) 
	{
		logger.debug("qryDeviceTypeMEMap_nroot({},{},{})", new Object[] { starttime1,
				endtime1, cityId });
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		sql.append("select count(d.device_id) DeviceTypeNum,d.city_id,'ME' device_type ");
		sql.append("from tab_city c,tab_gw_device d,tab_devicetype_info e,tab_bss_dev_port f ");
		sql.append("where d.city_id=c.city_id and d.devicetype_id=e.devicetype_id and e.spec_id=f.id ");
		sql.append("and (c.parent_id='"+cityId+"' or c.city_id='"+cityId+"') ");
		sql.append("and (d.device_type='e8c' or d.device_type='e8-c') and f.spec_name like '%ME%' ");
		sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
		sql.append(" group by d.city_id");

		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 根据条件查询指定省级地区下的市级地区(包含各市地区下的子地区)符合要求设备数 悦me，不包含自己
	 * @param starttime1 系统注册开始时间
	 * @param endtime1 系统注册结束时间
	 * @param cityId 省地区ID(00)
	 * @return 结果集合
	 */
	public List<Map<Object,Object>> qryDeviceTypeMEMap_root(String starttime1, String endtime1, String cityId) 
	{
		logger.debug("qryDeviceTypeMEMap_root({},{},{})", new Object[] { starttime1,
				endtime1, cityId });
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		sql.append("select sum(x.num) DeviceTypeNum, x.city_id, 'ME' device_type from ");
		sql.append("(");
			sql.append("select count(d.device_id) num, c.parent_id city_id ");
			sql.append("from tab_city c,tab_gw_device d,tab_devicetype_info e, tab_bss_dev_port f ");
			sql.append("where c.city_id=d.city_id and d.devicetype_id=e.devicetype_id and e.spec_id=f.id ");
			sql.append("and c.parent_id in (select city_id from tab_city where parent_id='"+cityId+"') ");
			sql.append("and (d.device_type='e8c' or d.device_type='e8-c') and f.spec_name like '%ME%' ");
			sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(" group by c.parent_id ");
		sql.append("union all ");
			sql.append("select count(d.device_id) num,d.city_id ");
			sql.append("from tab_gw_device d,tab_devicetype_info e, tab_bss_dev_port f ");
			sql.append("where d.devicetype_id = e.devicetype_id and e.spec_id = f.id ");
			sql.append("and (d.city_id in (select city_id from tab_city where parent_id='"+cityId+"') or d.city_id='"+cityId+"') ");
			sql.append("and (d.device_type='e8c' or d.device_type='e8-c') and f.spec_name like '%ME%' ");
			sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(" group by d.city_id");
		sql.append(") x ");
		sql.append("group by x.city_id");

		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	

	
	/**
	 * 根据条件查询指定市级地区的下一级地区符合要求设备数，不包括自己（不支持wlan）
	 * @param starttime1 系统注册开始时间
	 * @param endtime1 系统注册结束时间
	 * @param cityId 地区ID
	 * @return 结果集合
	 */
	public List<Map<Object, Object>> qryWlanSupportMap_nroot0(String starttime1,String endtime1, String cityId)
	{
		logger.debug("qryWlanSupportMap_nroot0({},{},{})", new Object[] { starttime1, endtime1, cityId });
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		sql.append("select count(d.device_id) WlanSupport, d.city_id,0 wlan_num ");
		sql.append("from tab_city c, tab_gw_device d, tab_devicetype_info e, tab_bss_dev_port f ");
		sql.append("where d.city_id=c.city_id and d.devicetype_id=e.devicetype_id and e.spec_id=f.id ");
		sql.append("and f.wlan_num=0 and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
		sql.append(" and (c.parent_id='"+cityId+"' or c.city_id='"+cityId+"') group by d.city_id");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	
	
	/**
	 * 根据条件查询指定市级地区的下一级地区符合要求设备数，不包括自己（支持wlan）
	 * @param starttime1 系统注册开始时间
	 * @param endtime1 系统注册结束时间
	 * @param cityId 地区ID
	 * @return 结果集合
	 */
	public List<Map<Object, Object>> qryWlanSupportMap_nroot1(String starttime1,String endtime1, String cityId) 
	{
		logger.debug("qryWlanSupportMap_nroot1({},{},{})", new Object[] { starttime1, endtime1, cityId });
		StringBuffer sql = new StringBuffer();
		
		sql.append("select count(d.device_id) WlanSupport, d.city_id,1 wlan_num ");
		sql.append("from tab_city c, tab_gw_device d, tab_devicetype_info e, tab_bss_dev_port f ");
		sql.append("where d.city_id=c.city_id and d.devicetype_id=e.devicetype_id and e.spec_id=f.id");
		sql.append("and f.wlan_num!=0 and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
		sql.append(" and (c.parent_id='"+cityId+"' or c.city_id='"+cityId+"') group by d.city_id");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	
	
	
	/**
	 * 根据条件查询指定省级地区下的市级地区(包含各市地区下的子地区)符合要求设备数，不包含省自己（不支持wlan）
	 * @param starttime1 系统注册开始时间
	 * @param endtime1 系统注册结束时间
	 * @param cityId 地区ID
	 * @return 结果集合
	 */
	public List<Map<Object, Object>> qryWlanSupportMap_root0(String starttime1,String endtime1, String cityId) 
	{
		logger.debug("qryWlanSupportMap_root0({},{},{})", new Object[] { starttime1, endtime1, cityId });
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		sql.append("select sum(x.num) WlanSupport, x.city_id,0 wlan_num from ");
		sql.append("(");
			sql.append("select count(d.device_id) num, c.parent_id city_id ");
			sql.append("from tab_city c, tab_gw_device d, tab_devicetype_info e, tab_bss_dev_port f ");
			sql.append("where d.city_id=c.city_id and d.devicetype_id = e.devicetype_id and e.spec_id = f.id ");
			sql.append("and f.wlan_num=0 and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(" and c.parent_id in (select city_id from tab_city where parent_id='"+cityId+"') ");
			sql.append("group by c.parent_id ");
		sql.append("union all ");
			sql.append("select count(d.device_id) num,d.city_id ");
			sql.append("from tab_gw_device d, tab_devicetype_info e, tab_bss_dev_port f ");
			sql.append("where d.devicetype_id = e.devicetype_id and e.spec_id = f.id and f.wlan_num=0 ");
			sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(" and (d.city_id in (select city_id from tab_city where parent_id='"+cityId+"') or d.city_id='"+cityId+"')");
			sql.append("group by d.city_id");
		sql.append(") x ");
		sql.append("group by x.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	
	
	
	/**
	 * 根据条件查询指定省级地区下的市级地区(包含各市地区下的子地区)符合要求设备数，不包含省自己（支持wlan）
	 * @param starttime1 系统注册开始时间
	 * @param endtime1 系统注册结束时间
	 * @param cityId 地区ID
	 * @return 结果集合
	 */
	public List<Map<Object, Object>> qryWlanSupportMap_root1(String starttime1,String endtime1, String cityId) 
	{
		logger.debug("qryWlanSupportMap_root1({},{},{})", new Object[] { starttime1, endtime1, cityId });
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		sql.append("select sum(x.num) WlanSupport, x.city_id,1 wlan_num from ");
		sql.append("(");
			sql.append("select count(d.device_id) num, c.parent_id city_id ");
			sql.append("from tab_city c, tab_gw_device d, tab_devicetype_info e, tab_bss_dev_port f ");
			sql.append("where d.city_id=c.city_id and d.devicetype_id=e.devicetype_id and e.spec_id=f.id ");
			sql.append("and f.wlan_num!=0 and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(" and c.parent_id in (select city_id from tab_city where parent_id='"+cityId+"') ");
			sql.append("group by c.parent_id ");
		sql.append("union all ");
			sql.append("select count(d.device_id) num,d.city_id ");
			sql.append("from tab_gw_device d, tab_devicetype_info e, tab_bss_dev_port f ");
			sql.append("where d.devicetype_id=e.devicetype_id and e.spec_id=f.id ");
			sql.append("and f.wlan_num!=0 and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(" and (d.city_id in (select city_id from tab_city where parent_id='"+cityId+"') or d.city_id='"+cityId+"') ");
			sql.append("group by d.city_id");
		sql.append(") x ");
		sql.append("group by x.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	
	
	

	/**
	 * 根据地区、时间区间、当前列的分类条件查询设备列表详细信息
	 * @param starttime1 开始时间
	 * @param endtime1	结束时间
	 * @param cityId 地区id
	 * @param curPage_splitPage 当前查询页数
	 * @param num_splitPage 每页显示数目
	 * @param classfy 分类条件(用户连接方式-桥接or路由、设备类型e8b e8c、是否支持wifi)
	 * @param isRoot 是否省级总地区
	 * @return 符合条件的设备列表
	 */
	public List<Map> getDevList(String starttime1,
			String endtime1, String cityId, int curPage_splitPage,
			int num_splitPage, String classfy, boolean isRoot) 
	{
		logger.debug("getDevList({},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, curPage_splitPage, num_splitPage ,classfy, isRoot});
		StringBuffer cityStr = new StringBuffer();
		if(!isRoot){
			cityStr.append(" and (c.city_id='"+cityId+"' or c.parent_id='"+cityId+"') ");
		}else{
			cityStr.append(" ");
		}
		
		StringBuffer sql = new StringBuffer();
		
		if(WAN_TYPE_KEY1.equals(classfy))
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select distinct l.device_id,l.city_name,l.city_id,l.parent_id,");
				sql.append("l.softwareversion,l.hardwareversion,l.device_serialnumber,l.vendor_id,");
				sql.append("l.device_model_id,l.username,l.logicId,l.user_id,");
			}else{
				sql.append("select distinct l.*,");
			}
			sql.append("f.vendor_name,g.device_model from ");
			sql.append("(select d.device_id,c.city_name,c.city_id,c.parent_id,e.softwareversion,");
			sql.append("e.hardwareversion,d.device_serialnumber,d.vendor_id,d.device_model_id,");
			if(DBUtil.GetDB()==3){
				sql.append("ifnull(b.username,'nonameflag') username,a.username logicId,a.user_id ");
			}else{
				sql.append("nvl(b.username,'nonameflag') username,a.username logicId,a.user_id ");
			}
			sql.append("from tab_hgwcustomer a,hgwcust_serv_info b,tab_gw_device d,tab_city c,tab_devicetype_info e ");
			sql.append("where a.user_id=b.user_id and d.city_id=c.city_id and d.devicetype_id=e.devicetype_id ");
			sql.append("and a.device_id=d.device_id and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(cityStr).append("and b.wan_type=1) l ");
			sql.append("left join tab_vendor f on l.vendor_id = f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id");
		}
		else if(WAN_TYPE_KEY2.equals(classfy))
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select distinct l.device_id,l.city_name,l.city_id,l.parent_id,");
				sql.append("l.softwareversion,l.hardwareversion,l.device_serialnumber,l.vendor_id,");
				sql.append("l.device_model_id,l.username,l.logicId,l.user_id,");
			}else{
				sql.append("select distinct l.*,");
			}
			sql.append("f.vendor_name,g.device_model from ");
			sql.append("(select d.device_id,c.city_name,c.city_id,c.parent_id,e.softwareversion,");
			sql.append("e.hardwareversion,d.device_serialnumber,d.vendor_id,d.device_model_id,");
			if(DBUtil.GetDB()==3){
				sql.append("ifnull(b.username,'nonameflag') username,a.username logicId,a.user_id ");
			}else{
				sql.append("nvl(b.username,'nonameflag') username,a.username logicId,a.user_id ");
			}
			sql.append("from tab_hgwcustomer a,hgwcust_serv_info b,tab_gw_device d,tab_city c,tab_devicetype_info e ");
			sql.append("where a.user_id=b.user_id and d.city_id=c.city_id and d.devicetype_id=e.devicetype_id ");
			sql.append("and a.device_id=d.device_id and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(cityStr+"and b.wan_type=2) l ");
			sql.append("left join tab_vendor f on l.vendor_id = f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id");
		}
		else if(DEVICE_TYPE_KEY_E8B.equals(classfy))
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select distinct l.device_id,l.city_name,l.city_id,l.parent_id,l.softwareversion,");
				sql.append("l.hardwareversion,l.device_serialnumber,l.vendor_id,l.device_model_id,");
				sql.append("r.username logicId,r.user_id,ifnull(r.busername,'nonameflag') username,");
			}else{
				sql.append("select distinct l.*,r.username logicId,r.user_id,nvl(r.busername,'nonameflag') username,");
			}
			sql.append("f.vendor_name,g.device_model from ");
			sql.append("(select d.device_id,c.city_name,c.city_id,c.parent_id,e.softwareversion,");
			sql.append("e.hardwareversion,d.device_serialnumber,d.vendor_id,d.device_model_id ");
			sql.append("from tab_gw_device d,tab_city c,tab_devicetype_info e ");
			sql.append("where d.city_id=c.city_id and d.devicetype_id=e.devicetype_id ");
			sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(" and d.device_type='e8-b'"+cityStr+") l ");
			sql.append("left join tab_vendor f on l.vendor_id = f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id ");
			sql.append("left join (select a.device_id,a.user_id,a.username,");
			if(DBUtil.GetDB()==3){
				sql.append("ifnull(b.username,'nonameflag') busername ");
			}else{
				sql.append("nvl(b.username,'nonameflag') busername ");
			}
			sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id=b.user_id ) r ");
			sql.append("on l.device_id=r.device_id ");
		}
		else if(DEVICE_TYPE_KEY_E8C.equals(classfy))
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select distinct l.device_id,l.city_name,l.city_id,l.parent_id,l.softwareversion,");
				sql.append("l.hardwareversion,l.device_serialnumber,l.vendor_id,l.device_model_id,");
				sql.append("r.username logicId,r.user_id,ifnull(r.busername,'nonameflag') username,");
			}else{
				sql.append("select distinct l.*,r.username logicId,r.user_id,nvl(r.busername,'nonameflag') username,");
			}
			sql.append("f.vendor_name,g.device_model from ");
			sql.append("(select d.device_id,c.city_name,c.city_id,c.parent_id,e.softwareversion,");
			sql.append("e.hardwareversion,d.device_serialnumber,d.vendor_id,d.device_model_id ");
			sql.append("from tab_gw_device d,tab_city c,tab_devicetype_info e,tab_bss_dev_port h ");
			sql.append("where d.city_id=c.city_id and d.devicetype_id=e.devicetype_id ");
			sql.append("and e.spec_id=h.id and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(" and (d.device_type='e8c' or d.device_type='e8-c') "+cityStr);
			sql.append(" and h.spec_name not like '%ME%') l ");
			sql.append("left join tab_vendor f on l.vendor_id = f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id ");
			sql.append("left join (select a.device_id,a.user_id,a.username, ");
			if(DBUtil.GetDB()==3){
				sql.append("ifnull(b.username,'nonameflag') busername ");
			}else{
				sql.append("nvl(b.username,'nonameflag') busername ");
			}
			sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id=b.user_id ) r ");
			sql.append("on l.device_id=r.device_id ");
		}
		else if(DEVICE_TYPE_KEY_E8CME.equals(classfy))
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select distinct l.device_id,l.city_name,l.city_id,l.parent_id,l.softwareversion,");
				sql.append("l.hardwareversion,l.device_serialnumber,l.vendor_id,l.device_model_id,");
				sql.append("r.username logicId,r.user_id,ifnull(r.busername,'nonameflag') username,");
			}else{
				sql.append("select distinct l.*,r.username logicId,r.user_id,");
				sql.append("nvl(r.busername,'nonameflag') username,");
			}
			sql.append("f.vendor_name,g.device_model from ");
			sql.append("(select d.device_id,c.city_name,c.city_id,c.parent_id,e.softwareversion,");
			sql.append("e.hardwareversion,d.device_serialnumber,d.vendor_id,d.device_model_id ");
			sql.append("from tab_gw_device d,tab_city c,tab_devicetype_info e,tab_bss_dev_port h ");
			sql.append("where d.city_id=c.city_id and d.devicetype_id=e.devicetype_id and e.spec_id=h.id ");
			sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(" and (d.device_type='e8c' or d.device_type='e8-c') "+cityStr);
			sql.append(" and h.spec_name like '%ME%') l ");
			sql.append("left join tab_vendor f on l.vendor_id = f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id ");
			sql.append("left join (select a.device_id,a.user_id,a.username, ");
			if(DBUtil.GetDB()==3){
				sql.append("ifnull(b.username,'nonameflag') busername ");
			}else{
				sql.append("nvl(b.username,'nonameflag') busername ");
			}
			sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id=b.user_id ) r ");
			sql.append("on l.device_id=r.device_id ");
		}
		else if(WLAN_KEY_SUPPORT.equals(classfy))
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select l.device_id,l.city_name,l.city_id,l.parent_id,l.softwareversion,");
				sql.append("l.hardwareversion,l.device_serialnumber,l.vendor_id,l.device_model_id,");
				sql.append("r.username logicId,r.user_id,ifnull(r.busername,'nonameflag') username,");
			}else{
				sql.append("select l.*,r.username logicId,r.user_id,");
				sql.append("nvl(r.busername,'nonameflag') username,");
			}
			sql.append("f.vendor_name,g.device_model from ");
			sql.append("(select distinct d.device_id,c.city_name,c.city_id,c.parent_id,e.softwareversion,");
			sql.append("e.hardwareversion,d.device_serialnumber,d.vendor_id,d.device_model_id ");
			sql.append("from tab_gw_device d,tab_city c,tab_devicetype_info e,tab_bss_dev_port h ");
			sql.append("where d.city_id=c.city_id and d.devicetype_id=e.devicetype_id and e.spec_id=h.id ");
			sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(cityStr+"and h.wlan_num!=0) l ");
			sql.append("left join tab_vendor f on l.vendor_id = f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id ");
			sql.append("left join (select a.device_id,a.user_id, a.username, ");
			
			if(DBUtil.GetDB()==3){
				sql.append("ifnull(b.username,'nonameflag') busername ");
			}else{
				sql.append("nvl(b.username,'nonameflag') busername ");
			}
			sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id=b.user_id ) r ");
			sql.append("on l.device_id=r.device_id ");
		}
		else
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select l.device_id,l.city_name,l.city_id,l.parent_id,l.softwareversion,");
				sql.append("l.hardwareversion,l.device_serialnumber,l.vendor_id,l.device_model_id,");
				sql.append("r.username logicId,r.user_id,ifnull(r.busername,'nonameflag') username,");
			}else{
				sql.append("select l.*,r.username logicId,r.user_id,nvl(r.busername,'nonameflag') username,");
			}
			sql.append("f.vendor_name,g.device_model from ");
			sql.append("(select distinct d.device_id,c.city_name,c.city_id,c.parent_id,e.softwareversion,");
			sql.append("e.hardwareversion,d.device_serialnumber,d.vendor_id,d.device_model_id ");
			sql.append("from tab_gw_device d,tab_city c,tab_devicetype_info e,tab_bss_dev_port h ");
			sql.append("where d.city_id=c.city_id and d.devicetype_id=e.devicetype_id and e.spec_id=h.id");
			sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(cityStr+"and h.wlan_num=0) l ");
			sql.append("left join tab_vendor f on l.vendor_id = f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id ");
			sql.append("left join (select a.device_id,a.user_id,a.username,");
	
			if(DBUtil.GetDB()==3){
				sql.append("ifnull(b.username,'nonameflag') busername ");
			}else{
				sql.append("nvl(b.username,'nonameflag') busername ");
			}
			sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id=b.user_id ) r ");
			sql.append("on l.device_id=r.device_id ");
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				String cityId = rs.getString("city_id");
				map.put("city_id", rs.getString("city_id"));
				map.put("city_name", rs.getString("city_name"));
				
				//取出的city作为区县，city为市级/省级则属地也为city，为区级则属地为上级市。
				boolean isRootSon = false;
				List<Map<String,String>> rootChildIdList =  CityDAO.getNextCityListByCityPid("00");
				for(Map<String,String> tmpChild : rootChildIdList){
					if(tmpChild.get("city_id").equals(cityId)){
						isRootSon = true;
					}
				}
				//取出的city作为区县，city为市级/省级则属地也为city，为区级则属地为上级市。
				if(isRootSon){
					map.put("parent_name", rs.getString("city_name"));
				}
				else{
					map.put("parent_name", Global.G_CityId_CityName_Map.get(rs.getString("parent_id")));
				}
				
				map.put("vendor_name", rs.getString("vendor_name"));
				map.put("device_model", rs.getString("device_model"));
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("hardwareversion", rs.getString("hardwareversion"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("logicId", rs.getString("logicId"));
				//sql里面把宽带账号username为空的转换成了"nonameflag"
				map.put("username", "nonameflag".equals(rs.getString("username"))?"无宽带":rs.getString("username"));
				return map;
			}
		});
		return list;
	}


	/**
	 * 根据getDevList方法查询结果的总条数、页面条数计算总页数
	 * @param starttime1 开始时间
	 * @param endtime1	结束时间
	 * @param cityId 地区id
	 * @param curPage_splitPage 当前查询页数
	 * @param num_splitPage 每页显示数目
	 * @param classfy 分类条件(用户连接方式-桥接or路由、设备类型e8b e8c、是否支持wifi)
	 * @param isRoot 是否省级总地区
	 * @return 符合条件的设备列表
	 */
	public int getDevCount(String starttime1, String endtime1, String cityId,
			int curPage_splitPage, int num_splitPage, String classfy, boolean isRoot) 
	{
		logger.debug("getDevCount({},{},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, curPage_splitPage, num_splitPage ,classfy, isRoot});
		
		StringBuffer cityStr = new StringBuffer();
		if(!isRoot){
			cityStr.append(" and (c.city_id='"+cityId+"' or c.parent_id='"+cityId+"') ");
		}else{
			cityStr.append(" ");
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from (");
		
		if(WAN_TYPE_KEY1.equals(classfy))
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select distinct l.device_id,l.city_name,l.city_id,l.parent_id,");
				sql.append("l.softwareversion,l.hardwareversion,l.device_serialnumber,l.vendor_id,");
				sql.append("l.device_model_id,l.username,l.logicId,l.user_id,");
			}else{
				sql.append("select distinct l.*,");
			}
			
			sql.append("f.vendor_name,g.device_model from ");
			sql.append("(select d.device_id,c.city_name,c.city_id,c.parent_id,e.softwareversion,");
			sql.append("e.hardwareversion,d.device_serialnumber,d.vendor_id,d.device_model_id,");
			if(DBUtil.GetDB()==3){
				sql.append("ifnull(b.username,'nonameflag') username,a.username logicId,a.user_id ");
			}else{
				sql.append("nvl(b.username,'nonameflag') username,a.username logicId,a.user_id ");
			}
			sql.append("from tab_hgwcustomer a, hgwcust_serv_info b,tab_gw_device d,tab_city c,tab_devicetype_info e ");
			sql.append("where a.user_id=b.user_id and d.city_id=c.city_id and d.devicetype_id=e.devicetype_id ");
			sql.append("and a.device_id=d.device_id and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(cityStr+"and b.wan_type=1) l ");
			sql.append("left join tab_vendor f on l.vendor_id=f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id");
		}
		else if(WAN_TYPE_KEY2.equals(classfy))
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select distinct l.device_id,l.city_name,l.city_id,l.parent_id,");
				sql.append("l.softwareversion,l.hardwareversion,l.device_serialnumber,l.vendor_id,");
				sql.append("l.device_model_id,l.username,l.logicId,l.user_id,");
			}else{
				sql.append("select distinct l.*,");
			}
			sql.append("f.vendor_name,g.device_model from ");
			sql.append("(select d.device_id,c.city_name,c.city_id,c.parent_id,e.softwareversion,");
			sql.append("e.hardwareversion,d.device_serialnumber,d.vendor_id,d.device_model_id,");
			if(DBUtil.GetDB()==3){
				sql.append("ifnull(b.username,'nonameflag') username,a.username logicId,a.user_id ");
			}else{
				sql.append("nvl(b.username,'nonameflag') username,a.username logicId,a.user_id ");
			}
			sql.append("from tab_hgwcustomer a,hgwcust_serv_info b,tab_gw_device d,tab_city c,tab_devicetype_info e ");
			sql.append("where a.user_id=b.user_id and d.city_id=c.city_id and d.devicetype_id=e.devicetype_id ");
			sql.append("and a.device_id=d.device_id and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(cityStr+"and b.wan_type=2) l ");
			sql.append("left join tab_vendor f on l.vendor_id=f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id");
		}
		else if(DEVICE_TYPE_KEY_E8B.equals(classfy))
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select distinct l.device_id,l.city_name,l.city_id,l.parent_id,");
				sql.append("l.softwareversion,l.hardwareversion,l.device_serialnumber,");
				sql.append("l.vendor_id,l.device_model_id,");
				sql.append("ifnull(r.busername,'nonameflag') username,");
			}else{
				sql.append("select distinct l.*,nvl(r.busername,'nonameflag') username,");
			}
			sql.append("r.username logicId,r.user_id,f.vendor_name,g.device_model from ");
			sql.append("(select d.device_id,c.city_name,c.city_id,c.parent_id,e.softwareversion,");
			sql.append("e.hardwareversion,d.device_serialnumber,d.vendor_id,d.device_model_id ");
			sql.append("from tab_gw_device d,tab_city c,tab_devicetype_info e ");
			sql.append("where d.city_id=c.city_id and d.devicetype_id=e.devicetype_id ");
			sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(" and d.device_type='e8-b'"+cityStr+") l ");
			sql.append("left join tab_vendor f on l.vendor_id=f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id ");
			sql.append("left join (select a.device_id,a.user_id,a.username,");
			
			if(DBUtil.GetDB()==3){
				sql.append("ifnull(b.username,'nonameflag') busername ");
			}else{
				sql.append("nvl(b.username,'nonameflag') busername ");
			}
			sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id=b.user_id) r ");
			sql.append("on l.device_id=r.device_id ");
			
		}
		else if(DEVICE_TYPE_KEY_E8C.equals(classfy))
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select distinct l.device_id,l.city_name,l.city_id,l.parent_id,");
				sql.append("l.softwareversion,l.hardwareversion,l.device_serialnumber,");
				sql.append("l.vendor_id,l.device_model_id,");
				sql.append("ifnull(r.busername,'nonameflag') username,");
			}else{
				sql.append("select distinct l.*,nvl(r.busername,'nonameflag') username,");
			}
			sql.append("r.username logicId,r.user_id,f.vendor_name,g.device_model from ");
			sql.append("(select d.device_id,c.city_name,c.city_id,c.parent_id,e.softwareversion,");
			sql.append("e.hardwareversion,d.device_serialnumber,d.vendor_id,d.device_model_id ");
			sql.append("from tab_gw_device d,tab_city c,tab_devicetype_info e,tab_bss_dev_port h ");
			sql.append("where d.city_id=c.city_id and d.devicetype_id=e.devicetype_id and e.spec_id=h.id ");
			sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(" and (d.device_type='e8c' or d.device_type='e8-c') "+cityStr);
			sql.append(" and h.spec_name not like '%ME%') l ");
			sql.append("left join tab_vendor f on l.vendor_id=f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id ");
			sql.append("left join (select a.device_id,a.user_id,a.username,");
			if(DBUtil.GetDB()==3){
				sql.append("ifnull(b.username,'nonameflag') busername ");
			}else{
				sql.append("nvl(b.username,'nonameflag') busername ");
			}
			sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id=b.user_id) r ");
			sql.append("on l.device_id=r.device_id ");
		}
		else if(DEVICE_TYPE_KEY_E8CME.equals(classfy))
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select distinct l.device_id,l.city_name,l.city_id,l.parent_id,");
				sql.append("l.softwareversion,l.hardwareversion,l.device_serialnumber,");
				sql.append("l.vendor_id,l.device_model_id,");
				sql.append("ifnull(r.busername,'nonameflag') username,");
			}else{
				sql.append("select distinct l.*,nvl(r.busername,'nonameflag') username,");
			}
			sql.append("r.username logicId,r.user_id,f.vendor_name,g.device_model from ");
			sql.append("(select d.device_id,c.city_name,c.city_id,c.parent_id,e.softwareversion,");
			sql.append("e.hardwareversion,d.device_serialnumber,d.vendor_id,d.device_model_id ");
			sql.append("from tab_gw_device d,tab_city c,tab_devicetype_info e,tab_bss_dev_port h ");
			sql.append("where d.city_id=c.city_id and d.devicetype_id=e.devicetype_id and e.spec_id=h.id ");
			sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(" and (d.device_type='e8c' or d.device_type='e8-c') "+cityStr);
			sql.append(" and h.spec_name like '%ME%') l ");
			sql.append("left join tab_vendor f on l.vendor_id = f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id ");
			sql.append("left join (select a.device_id,a.user_id, a.username, ");
			if(DBUtil.GetDB()==3){
				sql.append("ifnull(b.username,'nonameflag') busername ");
			}else{
				sql.append("nvl(b.username,'nonameflag') busername ");
			}
			sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id=b.user_id ) r ");
			sql.append("on l.device_id=r.device_id ");
		}
		else if(WLAN_KEY_SUPPORT.equals(classfy))
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select l.device_id,l.city_name,l.city_id,l.parent_id,");
				sql.append("l.softwareversion,l.hardwareversion,l.device_serialnumber,");
				sql.append("l.vendor_id,l.device_model_id,");
				sql.append("ifnull(r.busername,'nonameflag') username,");
			}else{
				sql.append("select l.*,nvl(r.busername,'nonameflag') username,");
			}
			sql.append("r.username logicId,r.user_id,f.vendor_name,g.device_model from ");
			sql.append("(select distinct d.device_id,c.city_name,c.city_id,c.parent_id,");
			sql.append("e.softwareversion,e.hardwareversion,d.device_serialnumber,d.vendor_id,d.device_model_id ");
			sql.append("from tab_gw_device d,tab_city c,tab_devicetype_info e,tab_bss_dev_port h ");
			sql.append("where d.city_id=c.city_id and d.devicetype_id=e.devicetype_id and e.spec_id = h.id ");
			sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1+cityStr);
			sql.append("and h.wlan_num!=0) l ");
			sql.append("left join tab_vendor f on l.vendor_id = f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id ");
			sql.append("left join (select a.device_id,a.user_id, a.username, ");
			if(DBUtil.GetDB()==3){
				sql.append("ifnull(b.username,'nonameflag') busername ");
			}else{
				sql.append("nvl(b.username,'nonameflag') busername ");
			}
			sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id=b.user_id ) r ");
			sql.append("on l.device_id=r.device_id ");
		}
		else
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select l.*,");
				sql.append("ifnull(r.busername,'nonameflag') username,");
			}else{
				sql.append("select l.*,nvl(r.busername,'nonameflag') username,");
			}
			sql.append("r.username logicId,r.user_id,f.vendor_name,g.device_model from ");
			sql.append("(select distinct d.device_id,c.city_name,c.city_id,c.parent_id,");
			sql.append("e.softwareversion,e.hardwareversion,d.device_serialnumber,d.vendor_id,d.device_model_id ");
			sql.append("from tab_gw_device d,tab_city c,tab_devicetype_info e,tab_bss_dev_port h ");
			sql.append("where d.city_id=c.city_id and d.devicetype_id=e.devicetype_id and e.spec_id = h.id ");
			sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1+cityStr);
			sql.append("and h.wlan_num=0) l ");
			sql.append("left join tab_vendor f on l.vendor_id = f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id ");
			sql.append("left join (select a.device_id,a.user_id, a.username, ");
			if(DBUtil.GetDB()==3){
				sql.append("ifnull(b.username,'nonameflag') busername ");
			}else{
				sql.append("nvl(b.username,'nonameflag') busername ");
			}
			
			sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id=b.user_id ) r ");
			sql.append("on l.device_id=r.device_id ");
		}
		sql.append(")");
		
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
	 * 根据地区、时间区间、当前列的分类条件查询设备列表详细信息(导出)
	 * @param starttime1 开始时间
	 * @param endtime1	结束时间
	 * @param cityId 地区id
	 * @param classfy 分类条件(用户连接方式-桥接or路由、设备类型e8b e8c、是否支持wifi)
	 * @param isRoot 是否省级总地区
	 * @return 符合条件的设备列表
	 */
	public List<Map> getDevExcel(String starttime1,
			String endtime1, String cityId, String classfy, boolean isRoot) 
	{
		logger.debug("getDevExcel({},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, classfy});
		StringBuffer cityStr = new StringBuffer();
		if(!isRoot){
			cityStr.append(" and (c.city_id='"+cityId+"' or c.parent_id='"+cityId+"') ");
		}else{
			cityStr.append(" ");
		}
		
		StringBuffer sql = new StringBuffer();
		if(WAN_TYPE_KEY1.equals(classfy))
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select distinct l.device_id,l.city_name,l.city_id,l.parent_id,");
				sql.append("l.softwareversion,l.hardwareversion,l.device_serialnumber,l.vendor_id,");
				sql.append("l.device_model_id,l.username,l.logicId,l.user_id");
			}else{
				sql.append("select distinct l.*,");
			}
			
			sql.append("f.vendor_name,g.device_model from ");
			sql.append("(select d.device_id,c.city_name,c.city_id,c.parent_id,e.softwareversion,");
			sql.append("e.hardwareversion,d.device_serialnumber,d.vendor_id,d.device_model_id,");
			if(DBUtil.GetDB()==3){
				sql.append("ifnull(b.username,'nonameflag') username,");
			}else{
				sql.append("nvl(b.username,'nonameflag') username,");
			}
			sql.append("a.username logicId,a.user_id ");
			sql.append("from tab_hgwcustomer a,hgwcust_serv_info b,tab_gw_device d,tab_city c,tab_devicetype_info e ");
			sql.append("where a.user_id=b.user_id and d.city_id=c.city_id and d.devicetype_id=e.devicetype_id ");
			sql.append("and a.device_id=d.device_id and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(cityStr+"and b.wan_type=1) l ");
			sql.append("left join tab_vendor f on l.vendor_id = f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id");
		}
		else if(WAN_TYPE_KEY2.equals(classfy))
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select distinct l.device_id,l.city_name,l.city_id,l.parent_id,");
				sql.append("l.softwareversion,l.hardwareversion,l.device_serialnumber,l.vendor_id,");
				sql.append("l.logicId,l.user_id,l.device_model_id,l.username,");
			}else{
				sql.append("select distinct l.*,");
			}
			sql.append("f.vendor_name,g.device_model from ");
			sql.append("(select d.device_id,c.city_name,c.city_id,c.parent_id,e.softwareversion,");
			sql.append("e.hardwareversion,d.device_serialnumber,d.vendor_id,a.username logicId,a.user_id,");
			if(DBUtil.GetDB()==3){
				sql.append("d.device_model_id, ifnull(b.username,'nonameflag') username ");
			}else{
				sql.append("d.device_model_id, nvl(b.username,'nonameflag') username ");
			}
			
			sql.append("from tab_hgwcustomer a,hgwcust_serv_info b,tab_gw_device d,tab_city c,tab_devicetype_info e ");
			sql.append("where a.user_id=b.user_id and d.city_id=c.city_id and d.devicetype_id=e.devicetype_id ");
			sql.append("and a.device_id=d.device_id and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(cityStr+"and b.wan_type=2) l ");
			sql.append("left join tab_vendor f on l.vendor_id = f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id");
		}
		else if(DEVICE_TYPE_KEY_E8B.equals(classfy))
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select distinct l.device_id,l.city_name,l.city_id,l.parent_id,");
				sql.append("l.softwareversion,l.hardwareversion,l.device_serialnumber,");
				sql.append("l.vendor_id,l.device_model_id,ifnull(r.busername,'nonameflag') username,");
			}else{
				sql.append("select distinct l.*,nvl(r.busername,'nonameflag') username,");
			}
			
			sql.append("r.username logicId,r.user_id,f.vendor_name,g.device_model ");
			sql.append("from (select d.device_id,c.city_name,c.city_id,c.parent_id,");
			sql.append("e.softwareversion,e.hardwareversion,d.device_serialnumber,d.vendor_id,d.device_model_id ");
			sql.append("from tab_gw_device d,tab_city c,tab_devicetype_info e ");
			sql.append("where d.city_id=c.city_id and d.devicetype_id=e.devicetype_id ");
			sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(" and d.device_type='e8-b'"+cityStr+") l ");
			sql.append("left join tab_vendor f on l.vendor_id = f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id ");
			sql.append("left join (select a.device_id,a.user_id, a.username,");
			if(DBUtil.GetDB()==3){
				sql.append("ifnull(b.username,'nonameflag') busername ");
			}else{
				sql.append("nvl(b.username,'nonameflag') busername ");
			}
			sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id=b.user_id ) r ");
			sql.append("on l.device_id=r.device_id ");
		}
		else if(DEVICE_TYPE_KEY_E8C.equals(classfy))
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select distinct l.device_id,l.city_name,l.city_id,l.parent_id,");
				sql.append("l.softwareversion,l.hardwareversion,l.device_serialnumber,");
				sql.append("l.vendor_id,l.device_model_id,");
				sql.append("ifnull(r.busername,'nonameflag') username,");
			}else{
				sql.append("select distinct l.*,nvl(r.busername,'nonameflag') username,");
			}
			sql.append("r.username logicId,r.user_id,f.vendor_name,g.device_model from ");
			sql.append("(select d.device_id,c.city_name,c.city_id,c.parent_id,e.softwareversion,");
			sql.append("e.hardwareversion,d.device_serialnumber,d.vendor_id,d.device_model_id ");
			sql.append("from tab_gw_device d,tab_city c,tab_devicetype_info e,tab_bss_dev_port h ");
			sql.append("where d.city_id=c.city_id and d.devicetype_id=e.devicetype_id and e.spec_id = h.id ");
			sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(" and (d.device_type='e8c' or d.device_type='e8-c') "+cityStr);
			sql.append(" and h.spec_name not like '%ME%') l ");
			sql.append("left join tab_vendor f on l.vendor_id = f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id ");
			sql.append("left join (select a.device_id,a.user_id, a.username,");
			if(DBUtil.GetDB()==3){
				sql.append("ifnull(b.username,'nonameflag') busername ");
			}else{
				sql.append("nvl(b.username,'nonameflag') busername ");
			}
			sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id=b.user_id ) r ");
			sql.append("on l.device_id=r.device_id ");
		}
		else if(DEVICE_TYPE_KEY_E8CME.equals(classfy))
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select distinct l.device_id,l.city_name,l.city_id,l.parent_id,");
				sql.append("l.softwareversion,l.hardwareversion,l.device_serialnumber,");
				sql.append("l.vendor_id,l.device_model_id,");
				sql.append("ifnull(r.busername,'nonameflag') username,");
			}else{
				sql.append("select distinct l.*,nvl(r.busername,'nonameflag') username,");
			}
			sql.append("r.username logicId,r.user_id,f.vendor_name,g.device_model ");
			sql.append("from (select d.device_id,c.city_name,c.city_id,c.parent_id,");
			sql.append("e.softwareversion,e.hardwareversion,d.device_serialnumber,d.vendor_id,d.device_model_id ");
			sql.append(" from tab_gw_device d,tab_city c,tab_devicetype_info e,tab_bss_dev_port h ");
			sql.append("where d.city_id=c.city_id and d.devicetype_id=e.devicetype_id and e.spec_id = h.id ");
			sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(" and (d.device_type='e8c' or d.device_type='e8-c') "+cityStr);
			sql.append(" and h.spec_name like '%ME%') l ");
			sql.append("left join tab_vendor f on l.vendor_id = f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id ");
			sql.append("left join (select a.device_id,a.user_id, a.username,");
			if(DBUtil.GetDB()==3){
				sql.append("ifnull(b.username,'nonameflag') busername ");
			}else{
				sql.append("nvl(b.username,'nonameflag') busername ");
			}
			sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id=b.user_id ) r ");
			sql.append("on l.device_id=r.device_id ");
		}
		else if(WLAN_KEY_SUPPORT.equals(classfy))
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select l.device_id,l.city_name,l.city_id,l.parent_id,");
				sql.append("l.softwareversion,l.hardwareversion,l.device_serialnumber,");
				sql.append("l.vendor_id,l.device_model_id,");
				sql.append("ifnull(r.busername,'nonameflag') username,");
			}else{
				sql.append("select l.*,nvl(r.busername,'nonameflag') username,");
			}
			sql.append("r.username logicId,r.user_id,f.vendor_name,g.device_model from ");
			sql.append("(select distinct d.device_id,c.city_name,c.city_id,c.parent_id,");
			sql.append("e.softwareversion,e.hardwareversion,d.device_serialnumber,d.vendor_id,d.device_model_id ");
			sql.append("from tab_gw_device d,tab_city c, tab_devicetype_info e,tab_bss_dev_port h ");
			sql.append("where d.city_id=c.city_id and d.devicetype_id=e.devicetype_id and e.spec_id = h.id ");
			sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(cityStr+"and h.wlan_num!=0) l ");
			sql.append("left join tab_vendor f on l.vendor_id = f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id ");
			sql.append("left join (select a.device_id,a.user_id, a.username, ");
			if(DBUtil.GetDB()==3){
				sql.append("ifnull(b.username,'nonameflag') busername ");
			}else{
				sql.append("nvl(b.username,'nonameflag') busername ");
			}
			sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id=b.user_id ) r ");
			sql.append("on l.device_id=r.device_id ");
		}
		else
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select l.device_id,l.city_name,l.city_id,l.parent_id,");
				sql.append("l.softwareversion,l.hardwareversion,l.device_serialnumber,");
				sql.append("l.vendor_id,l.device_model_id,");
				sql.append("nvl(r.busername,'nonameflag') username,");
			}else{
				sql.append("select l.*,nvl(r.busername,'nonameflag') username,");
			}
			sql.append("r.username logicId,r.user_id,f.vendor_name,g.device_model from ");
			sql.append("(select distinct d.device_id,c.city_name,c.city_id,c.parent_id,");
			sql.append("e.softwareversion,e.hardwareversion,d.device_serialnumber,d.vendor_id,d.device_model_id ");
			sql.append("from tab_gw_device d,tab_city c,tab_devicetype_info e,tab_bss_dev_port h ");
			sql.append("where d.city_id=c.city_id and d.devicetype_id=e.devicetype_id and e.spec_id = h.id ");
			sql.append("and d.complete_time<="+endtime1+" and d.complete_time>="+starttime1);
			sql.append(cityStr).append("and h.wlan_num=0) l ");
			sql.append("left join tab_vendor f on l.vendor_id = f.vendor_id ");
			sql.append("left join gw_device_model g on l.device_model_id=g.device_model_id ");
			sql.append("left join (select a.device_id,a.user_id, a.username,");
			if(DBUtil.GetDB()==3){
				sql.append("ifnull(b.username,'nonameflag') busername ");
			}else{
				sql.append("nvl(b.username,'nonameflag') busername ");
			}
			
			sql.append("from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id=b.user_id ) r ");
			sql.append("on l.device_id=r.device_id ");
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				String cityId = rs.getString("city_id");
				map.put("city_id", rs.getString("city_id"));
				map.put("city_name", rs.getString("city_name"));
				
				//取出的city作为区县，city为市级/省级则属地也为city，为区级则属地为上级市。
				boolean isRootSon = false;
				List<Map<String,String>> rootChildIdList =  CityDAO.getNextCityListByCityPid("00");
				for(Map<String,String> tmpChild : rootChildIdList){
					if(tmpChild.get("city_id").equals(cityId)){
						isRootSon = true;
					}
				}
				if(isRootSon){
					map.put("parent_name", rs.getString("city_name"));
				}
				else{
					map.put("parent_name", Global.G_CityId_CityName_Map.get(rs.getString("parent_id")));
				}
				
				map.put("vendor_name", rs.getString("vendor_name"));
				map.put("device_model", rs.getString("device_model"));
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("hardwareversion", rs.getString("hardwareversion"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("logicId", rs.getString("logicId"));
				map.put("username", "nonameflag".equals(rs.getString("username"))?"无宽带":rs.getString("username"));
				return map;
			}
		});
		return list;
	}
	
	
}
