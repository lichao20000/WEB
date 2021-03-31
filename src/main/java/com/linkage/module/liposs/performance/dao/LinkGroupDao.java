package com.linkage.module.liposs.performance.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.liposs.system.basesupport.BaseSupportDAO;


/**
 * @author wangping5221
 * @version 1.0
 * @since 2008-7-16
 * @category com.linkage.liposs.dao.performance
 * 版权：南京联创科技 网管科技部
 *
 */
public class LinkGroupDao extends BaseSupportDAO
{
	private static Logger log = LoggerFactory.getLogger(LinkGroupDao.class);

    //sql封装类
	private PrepareSQL pSQL;

	/**
	 * 看这个设备IP、getway、port_info对应的端口在系统中是否存在，并是否在权限范围内
	 */
	private static String isLimitDeviceport="select distinct a.device_id  device_id from tab_deviceresource a,flux_deviceportconfig b "
		+" where a.device_id=b.device_id and a.device_id in (select res_id from tab_res_area where res_type=1 and area_id=?) and a.loopback_ip=? and b.getway=? and b.port_info=? ";

	/**
	 * 装载系统中得所有链路组别
	 */
	private static String selectGroupSQL="select groupid,groupname from tm_lg_group_info";

	/**
	 * 对于非管理员用户，装载自己权限范围内得链路组信息
	 */
	private static String selectLinkGroupSQL="select lg_id,group_id,lg_name from tm_linkgroup where acc_oid=? ";

	/**
	 * 对于管理员用户，装载所有得链路组信息
	 */ // TODO wait (more table related)
	private static String selectAllLinkGroupSQL="select a.lg_id,a.groupid,a.lg_name,b.drt_mid,a.acc_oid,a.acc_city from tm_linkgroup a,tm_linkgroup_mportdirection b where a.lg_id=b.lg_id group by  a.lg_id,a.groupid,a.lg_name having b.drt_mid=min(drt_mid)";


	/**
	 * 获取指定表名指定列的最大值
	 */
	private static String getMaxIDSQL="select max(?) num from ? ";


	/**
	 * 查询用户权限范围内的根据设备IP查询设备端口sql
	 */
	private static String selectDevicePortByDeviceIPSQL="select b.device_id,b.getway,b.port_info from tab_deviceresource a,flux_deviceportconfig b  "
		+" where a.device_id=b.device_id and a.device_id in(select res_id from tab_res_area where res_type=1 and area_id=?) "
		+" and b.getway in(2,5) and a.loopback_ip=?  order by device_id,getway,port_info ";


	/**
	 * 根据链路组ID、端口信息来查询对应的链路组别名称、用户帐号、属地
	 */
	private static String selectDevicePortLinkByDevicePortSQL="select count(*) from tm_linkgroup_cportdirection where lg_id=? and drt_mid=? and device_id=? and getway=? and ifindex_info=?";


	/**
	 * 执行添加单个端口链路组的sql
	 * @param sqlList
	 * @return  true:成功;false:失败
	 */
	@SuppressWarnings("unchecked")
	public boolean addLinkGroup(List sqlList)
	{
		String[] sqlArray= new String[sqlList.size()];
		sqlList.toArray(sqlArray);
		int[] resultCode=null;
		try
		{
			for(int i=0;i<sqlArray.length;i++)
			{
				log.error(i+"      "+sqlArray[i]);
			}
			resultCode=jt.batchUpdate(sqlArray);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return (null==resultCode)?false:true;
	}



	/**
	 * 判断某个端口在某个链路中是否配置
	 * @param lg_id   链路组ID
	 * @param drt_mid 链路组主表ID
	 * @param device_id  设备ID
	 * @param getway     采集方式：5(端口IP)、2（端口描述）
	 * @param port_info   端口标识的内容，getway为5，端口IP；getway为2，端口描述
	 * @return   配置了，返回true；未配置，返回false
	 */
	public boolean isConfigByDevicePort(long lg_id,long drt_mid,String device_id,String getway,String port_info)
	{
		pSQL.setSQL(selectDevicePortLinkByDevicePortSQL);
		pSQL.setLong(1,lg_id);
		pSQL.setLong(2,drt_mid);
		pSQL.setString(3,device_id);
		pSQL.setStringExt(4,getway,false);
		pSQL.setString(5,port_info);
		int num=jt.queryForInt(pSQL.getSQL());

		return num>0?true:false;
	}

	/**
	 * 查询某个设备IP的权限范围内的端口信息列表(key:deviceIP+"||"+getway+"||"+port_info  value:存在deviceid的list)
	 * @param deviceIP
	 * @param area_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map getDevicePortList(String deviceIP,long area_id)
	{
		Map<String,List> devicePortMap = new HashMap<String,List>();
		pSQL.setSQL(selectDevicePortByDeviceIPSQL);
		pSQL.setLong(1,area_id);
		pSQL.setString(2,deviceIP);
		List data =jt.queryForList(pSQL.getSQL());
		Iterator it =data.iterator();
		String device_id="";
		String getway="";
		String port_info="";
		Map tempMap=null;
		String temp_key="";
		while(it.hasNext())
		{
			tempMap=(Map)it.next();
			device_id=(String)tempMap.get("device_id");
			getway=((BigDecimal)tempMap.get("getway")).toString();
			port_info=(String)tempMap.get("port_info");
			temp_key=deviceIP+"||"+getway+"||"+port_info;
			if(devicePortMap.containsKey(temp_key))
			{
				List<String> deviceIDList = (List<String>)devicePortMap.get(temp_key);
				deviceIDList.add(device_id);
			}
			else
			{
				List<String> deviceIDList =new ArrayList<String>();
				deviceIDList.add(device_id);
				devicePortMap.put(temp_key,deviceIDList);
			}
		}

		//clear
		tempMap=null;
		it=null;
		data=null;


		return devicePortMap;
	}

	/**
	 * 返回某表某列的最大可用值，已经加1了
	 * @param colName  字段名
	 * @param tabName  表明
	 * @return
	 */
	public long getMaxId(String colName,String tabName)
	{
		long maxID=1;
		pSQL.setSQL(getMaxIDSQL);
		pSQL.setStringExt(1,colName,false);
		pSQL.setStringExt(2,tabName,false);
		List data =jt.queryForList(pSQL.getSQL());
		if(null!=data&&data.size()>0)
		{
			Object obj=((Map)data.get(0)).get("num");
			if(obj!=null)
			{
				maxID=((BigDecimal)obj).longValue()+1;
			}
		}
		return maxID;
	}

	/**
	 * 装载系统内得所有链路组信息
	 * @return key:链路组名称  value:链路组得详细信息map
	 *
	 */
	public Map getLinkGroupMap()
	{
		Map linkGroupMap = new HashMap();
		List data =jt.queryForList(selectAllLinkGroupSQL);
		Iterator it =data.iterator();
		Map temp =null;
		while(it.hasNext())
		{
			temp=(Map)it.next();
			linkGroupMap.put((String)temp.get("lg_name"),temp);
		}
		return linkGroupMap;
	}

	/**
	 * 返回系统内所有得链路组别map
	 * @return  key:链路组别名称  value:链路组别ID
	 */
	public Map getGroupMap()
	{
		Map groupMap = new HashMap();
		List data =jt.queryForList(selectGroupSQL);
		Iterator it =data.iterator();
		Map temp =null;
		while(it.hasNext())
		{
			temp=(Map)it.next();
			groupMap.put((String)temp.get("groupname"),temp.get("groupid"));
		}
		return groupMap;
	}


	/**
	 * 根据设备IP、端口信息，用户域ID来查找对应得设备信息
	 * @param loopback_ip   设备IP
	 * @param getway        端口标识方式2：端口描述，5：端口IP
	 * @param port_info     端口标识getway为2：port_info为端口描述，getway为5：port_info为端口IP
	 * @param areaID        用户域ID，来控制权限
	 * @return   用户权限范围内得这些设备IP、端口信息对应得设备信息（包括采集点、设备ID）
	 */
	public List getDeviceInfo(String loopback_ip,String getway,String port_info,long areaID)
	{
		List list =null;
		pSQL.setSQL(isLimitDeviceport);
		pSQL.setLong(1,areaID);
		pSQL.setString(2,loopback_ip);
		pSQL.setStringExt(3,getway,false);
		pSQL.setString(4,port_info);
		try
		{
			list= jt.queryForList(pSQL.getSQL());
		}
		catch(Exception e)
		{
			log.error("设备IP:"+loopback_ip+"        getway:"+getway+"     port_info:"+port_info+"     areaID:"+areaID+"   查询数据库出错");
		}
		return list;
	}


	public void setPSQL(PrepareSQL psql)
	{
		pSQL = psql;
	}




}
