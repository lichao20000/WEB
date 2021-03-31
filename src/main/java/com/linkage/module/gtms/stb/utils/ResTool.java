
package com.linkage.module.gtms.stb.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.linkage.litms.common.database.PrepareSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.linkage.litms.common.util.JdbcTemplateExtend;

/**
 * 网管IPOSS静态资源访问工具类，此类只包含基础的资源静态访问方法，<br>
 * <b>如果需要二次逻辑处理请自行封装自己的工具类<br>
 * 不要在这个类里边扩展，防止类被无限放大，干扰</b> <br>
 * 每个方法都应该秉着针对单张核心表进行检索的操作，其他单表的CUD应该放到子模块的DAO中完成，而不是对外工具类
 *
 * @author 王志猛(5194) EMAIL:wangzm@lianchuang.com
 * @version 1.0
 * @since 2009-3-16
 * @category com.linkage.module.liposs.system.util.resutil<br>
 * @copyright 版权：南京联创科技 网管科技部
 */
public class ResTool
{

	/**
	 * log4j日志记录器
	 */
	private static Logger log = LoggerFactory.getLogger(ResTool.class);

	private static JdbcTemplateExtend jte = null;
	private static DriverManagerDataSource ds = new DriverManagerDataSource();
	private static String dataSourceClassName = "org.logicalcobwebs.proxool.ProxoolDriver";
	private static String url = "proxool.xml-test";
	private static AtomicBoolean isinit = new AtomicBoolean(false);
	private static String m_GatherList_SQL = "select gather_id,descr from tab_process_desc";

	/**
	 * 获得所有角色对象
	 */
	private static String m_RolesAll_Cursor_SQL = "select role_id, role_name, role_desc, role_pid, acc_oid, is_default from tab_role order by role_name";

	// 资源初始化，静态方法，只初始化一次
	private synchronized static void init()
	{
		if (isinit.get())
		{
			return;
		}
		ds.setDriverClassName(dataSourceClassName);
		ds.setUrl(url);
		jte = new JdbcTemplateExtend(ds);
		isinit.set(true);
	}

	// 初始化
	static
	{
		init();
	}

	// 防止jte没有初始化好
	private static void adjustJte()
	{
		if (jte == null)
		{
			init();
		}
	}

	/**
	 * 获取所有的属地
	 *
	 * @return city的列表Map内部字段<city_id,city_name,parent_id,staff_id,remark>
	 */
	public static List<Map> getCityListAll()
	{
		PrepareSQL psql = new PrepareSQL("select city_id, city_name, parent_id from tab_city");
		adjustJte();

		return jte.queryForList(psql.getSQL());
	}

	/**
	 * 通过属地id获取属地信息
	 *
	 * @param city_id
	 *            属地id
	 * @return 查询的属地的所有信息Map内部字段<city_id,city_name,parent_id,staff_id,remark>
	 */
	public static Map getCityInfo(String city_id)
	{
		PrepareSQL psql = new PrepareSQL("select city_id, city_name, parent_id, staff_id, remark from tab_city where city_id='" + city_id + "'");
		adjustJte();
		return jte.queryForMap(psql.getSQL());
	}

	/**
	 * 获取指定的属地子列表
	 *
	 * @param city_id
	 *            属地的id
	 * @param deepth
	 *            向下获取几层,-1为当前属地的所有子属地
	 * @param incn
	 *            结果集合是否包含当前传入的city属地 true 为包含， false为不包含
	 * @return 属地列表 Map内部字段<city_id,city_name,parent_id,staff_id,remark>
	 */
	public static List<Map> getSubCityList(String city_id, int deepth, boolean incn)
	{
		List<Map> alllist = getCityListAll();
		List<Map> reslist = new ArrayList<Map>();
		if (incn)
		{
			for (Map c : alllist)
			{
				if (String.valueOf(c.get("city_id")).equals(city_id))
				{
					c.put("space", "");
					reslist.add(c);
					break;
				}
			}
		}
		filterCity(alllist, reslist, 1, deepth, city_id);// 过滤子属地,从第一层开始
		return reslist;
	}

	// 过滤子属地的内部方法
	private static void filterCity(List<Map> allList, List<Map> resList, int curlevel,
			int deepth, String parent_id)
	{
		if ((curlevel > deepth) && (deepth != -1))
		{
			return;
		}
		for (Map c : allList)
		{
			String pid = String.valueOf(c.get("parent_id"));
			if (pid.equals(parent_id))
			{
				c.put("space", getSpace(curlevel + 1));
				resList.add(c);
				String cid = String.valueOf(c.get("city_id"));
				filterCity(allList, resList, curlevel + 1, deepth, cid);
			}
		}
	}

	/**
	 * 根据level的值确定city_name前面有几个空格
	 *
	 * @param level
	 * @return
	 */

	private static String getSpace(int level)
	{
		if (level == 2)
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < level; i++)
		{
			sb.append("&nbsp;");
		}
		return sb.toString();
	}

	/**
	 * 获取指定的子属地编号列表
	 *
	 * @param city_id
	 *            属地的id
	 * @param deepth
	 *            向下获取几层,-1为当前属地的所有子属地
	 * @param incn
	 *            结果集合是否包含当前传入的city属地 true 为包含， false为不包含
	 * @return 属地列表 Map内部字段<city_id,city_name,parent_id,staff_id,remark>
	 */
	public static List<String> getSubCityIdList(String city_id, int deepth, boolean incn)
	{
		List<Map> alllist = getCityListAll();
		List<String> reslist = new ArrayList<String>();
		if (incn)
		{
			reslist.add(city_id);
		}
		filterCityId(alllist, reslist, 1, deepth, city_id);// 过滤子属地,从第一层开始
		return reslist;
	}

	// 过滤子属地的内部方法
	private static void filterCityId(List<Map> allList, List<String> resList,
			int curlevel, int deepth, String parent_id)
	{
		if ((curlevel > deepth) && (deepth != -1))
		{
			return;
		}
		for (Map c : allList)
		{
			String pid = String.valueOf(c.get("parent_id"));
			String cid = String.valueOf(c.get("city_id"));
			if (pid.equals(parent_id))
			{
				resList.add(cid);
				filterCityId(allList, resList, curlevel + 1, deepth, cid);
			}
		}
	}

	/**
	 * 获取所有的域
	 *
	 * @return 域的查询列表map内容<
	 *         area_id,area_name,area_pid,area_rootid,area_layer,acc_oid,remark>
	 */
	public static List<Map> getAreaInfo()
	{
		PrepareSQL psql = new PrepareSQL("select area_id, area_pid from tab_area");
		adjustJte();
		return jte.queryForList(psql.getSQL());
	}

	/**
	 * 通过制定的areaid获取对应的域的信息
	 *
	 * @param area_id
	 *            要获取信息的area_id
	 * @return 相关的map信息
	 */
	public static Map getAreaInfo(long area_id)
	{
		PrepareSQL psql = new PrepareSQL("select area_id, area_name, area_pid, area_rootid, area_layer, acc_oid, remark " +
				"from tab_area where area_id=" + area_id);
		adjustJte();
		return jte.queryForMap(psql.getSQL());
	}

	/**
	 * 获取下级子域的方法
	 *
	 * @param area_id
	 *            当前的域ID
	 * @param level
	 *            取下级域的层次，为-1则取所有子域
	 * @return 下级域信息列表，MAP中键值包括area_id、area_name、area_pid
	 */
	public static List<Map> getSubAreaList(long area_id, int level)
	{
		List<Map> areaList = getAreaInfo();
		List<Map> resList = new ArrayList<Map>();
		// 添加当前域
		resList.add(getAreaInfo(area_id));
		// 过滤下级域
		filterArea(areaList, resList, 1, level, String.valueOf(area_id));
		return resList;
	}

	/**
	 * 过滤下级子域的方法
	 *
	 * @param allList
	 *            所有域信息
	 * @param resList
	 *            当前已过滤出来的域信息
	 * @param curlevel
	 *            当前所在层次
	 * @param deepth
	 *            取下级域的层次，为-1则取所有子域
	 * @param area_pid
	 *            父域ID
	 */
	private static void filterArea(List<Map> allList, List<Map> resList, int curlevel,
			int deepth, String area_pid)
	{
		// 得到指定层数后退出
		if ((curlevel > deepth) && (deepth != -1))
		{
			return;
		}
		for (Map a : allList)
		{
			String pid = String.valueOf(a.get("area_pid"));
			// 找到子域后继续循环
			if (pid.equals(area_pid))
			{
				resList.add(a);
				String area_id = String.valueOf(a.get("area_id"));
				filterArea(allList, resList, curlevel - 1, deepth, area_id);
			}
		}
	}

	/**
	 * 获取下级子域编号的方法
	 *
	 * @param area_id
	 *            当前的域ID
	 * @param level
	 *            取下级域的层次，为-1则取所有子域
	 * @return 下级域信息列表，MAP中键值包括area_id、area_name、area_pid
	 */
	public static List<String> getSubAreaIdList(long area_id, int level)
	{
		List<Map> areaList = getAreaInfo();
		List<String> resList = new ArrayList<String>();
		// 添加当前域
		resList.add(String.valueOf(area_id));
		// 过滤下级域
		filterAreaId(areaList, resList, 1, level, String.valueOf(area_id));
		return resList;
	}

	/**
	 * 过滤下级子域的方法
	 *
	 * @param allList
	 *            所有域信息
	 * @param resList
	 *            当前已过滤出来的域信息
	 * @param curlevel
	 *            当前所在层次
	 * @param deepth
	 *            取下级域的层次，为-1则取所有子域
	 * @param area_pid
	 *            父域ID
	 */
	private static void filterAreaId(List<Map> allList, List<String> resList,
			int curlevel, int deepth, String area_pid)
	{
		// 得到指定层数后退出
		if ((curlevel > deepth) && (deepth != -1))
		{
			return;
		}
		for (Map a : allList)
		{
			String pid = String.valueOf(a.get("area_pid"));
			String area_id = String.valueOf(a.get("area_id"));
			// 找到子域后继续循环
			if (pid.equals(area_pid))
			{
				resList.add(area_id);
				filterAreaId(allList, resList, curlevel - 1, deepth, area_id);
			}
		}
	}

	/**
	 * 取当前域的所有父域信息
	 *
	 * @param area_id
	 *            当前的域信息
	 * @param level
	 *            取父域的层次，为-1则取所有父域
	 * @return 父域信息列表，MAP中键值包括area_id、area_name、area_pid
	 * @author 陈仲民
	 */
	public static List<Map> getParentAreaList(long area_id, int level)
	{
		List<Map> areaList = new ArrayList<Map>();
		Map map = getAnyArea(area_id);
		if (map != null)
		{
			areaList.add(map);
			// 判断是否继续取下层
			String area_pid = String.valueOf(map.get("area_pid"));
			if (level == -1 || level > 0)
			{
				if (area_pid != null && !"".equals(area_pid) && !"null".equals(area_pid))
				{
					// 添加下一级的父域
					areaList
							.addAll(getParentAreaList(Long.parseLong(area_pid), level - 1));
				}
			}
			else if (level == -1)
			{
				if (area_pid != null && !"".equals(area_pid) && !"null".equals(area_pid))
				{
					// 添加下一级的父域
					areaList.addAll(getParentAreaList(Long.parseLong(area_pid), -1));
				}
			}
		}
		return areaList;
	}

	/**
	 * 取当前域的所有父域编号信息
	 *
	 * @param area_id
	 *            当前的域信息
	 * @param level
	 *            取父域的层次，为-1则取所有父域
	 * @return 父域信息列表，MAP中键值包括area_id、area_name、area_pid
	 * @author 陈仲民
	 */
	public static List<String> getParentAreaIdList(long area_id, int level)
	{
		List<String> areaList = new ArrayList<String>();
		Map map = getAnyArea(area_id);
		if (map != null)
		{
			areaList.add(String.valueOf(area_id));
			// 判断是否继续取下层
			String area_pid = String.valueOf(map.get("area_pid"));
			if (level > 0)
			{
				if (area_pid != null && !"".equals(area_pid) && !"null".equals(area_pid))
				{
					// 添加下一级的父域
					areaList.addAll(getParentAreaIdList(Long.parseLong(area_pid),
							level - 1));
				}
			}
			// 无限迭代
			else if (level == -1)
			{
				if (area_pid != null && !"".equals(area_pid) && !"null".equals(area_pid))
				{
					areaList.addAll(getParentAreaIdList(Long.parseLong(area_pid), -1));
				}
			}
		}
		return areaList;
	}

	/**
	 * 通过制定的areaid获取对应的域的信息
	 *
	 * @param area_id
	 *            要获取信息的area_id
	 * @return 相关的map信息，若没查到域信息则返回NULL
	 */
	public static Map getAnyArea(long area_id)
	{
		PrepareSQL psql = new PrepareSQL("select area_pid, area_name from tab_area where area_id=" + area_id);
		adjustJte();
		List list = jte.queryForList(psql.getSQL());
		if (list != null && list.size() > 0)
		{
			return (Map) list.get(0);
		}
		else
		{
			return null;
		}
	}

	/**
	 * 获取某个采集点的详细信息
	 *
	 * @param gather_id
	 *            采集点编号
	 * @return Map容器,KEY包括:gather_id,descr,city_id,area_id
	 */
	public static Map getGatherInfo(String gather_id)
	{
		if (gather_id == null)
		{
			return null;
		}
		PrepareSQL psql = new PrepareSQL("select gather_id, descr, city_id, area_id from tab_process_desc where gather_id='"
				+ gather_id + "'");
		adjustJte();
		Map map = null;
		try
		{
			map = jte.queryForMap(psql.getSQL());
		}
		catch (DataAccessException e)
		{
			log.error("getGatherInfo Error. FOR : " + e.getMessage());
		}
		return map;
	}

	/**
	 * 查询系统所有采集点
	 *
	 * @return
	 */
	public static List<Map> getAllGathers()
	{
		adjustJte();
		return jte.queryForList(m_GatherList_SQL);
	}

	/**
	 * 获取某个域对应的采集机资源
	 *
	 * @param area_id
	 *            域ID
	 * @return 列表中每个对象为Map,其中KEY包含gather_id,descr
	 */
	public static List<Map> getGathersByArea(long area_id)
	{
		PrepareSQL psql = new PrepareSQL("select b.gather_id,b.descr from tab_res_area a"
				+ " inner join tab_process_desc b on a.res_id=b.gather_id"
				+ " where a.res_type=2 and a.area_id=" + area_id
				+ " order by b.gather_id");
		adjustJte();
		return jte.queryForList(psql.getSQL());
	}

	/**
	 * 获得所有角色对象
	 *
	 * @return Cursor
	 */
	public static List<Map> getAllRoles()
	{
		log.info(m_RolesAll_Cursor_SQL);
		return jte.queryForList(m_RolesAll_Cursor_SQL);
	}

	/**
	 * 获取角色下的用户列表
	 *
	 * @return
	 * @throws Exception
	 */
	public static List<Map> getUserListByRoleId(String roleId, String subCityIdList)
	{
		String sql = "select a.per_acc_oid,b.acc_loginname from tab_persons a,tab_accounts b "
				+ "where a.per_acc_oid=b.acc_oid and a.per_city in("
				+ subCityIdList
				+ ") "
				+ "and a.per_acc_oid in  (select acc_oid from tab_acc_role "
				+ "where role_id=" + roleId + ") order by b.acc_loginname";
		log.info(sql);
		return jte.queryForList(sql);
	}

	/**
	 * 查询账号人员名称
	 *
	 * @param acc_oid
	 *            账号编号
	 * @return 账号人员名称
	 */
	public static String getAccName(String acc_oid)
	{
		PrepareSQL psql = new PrepareSQL("select acc_loginname from tab_accounts where acc_oid=" + acc_oid);
		Map map = null;
		List<Map> accList = jte.queryForList(psql.getSQL());
		if (accList != null && accList.size() > 0)
		{
			map = accList.get(0);
		}
		if (map != null)
		{
			return String.valueOf(map.get("acc_loginname"));
		}
		return "";
	}

	public static Map getAccInfo(int acc_oid)
	{
		return null;
	}

	public static Map getRoleInfo(int role_id)
	{
		return null;
	}

	// TODO 获取设备的方法需要细化
	/**
	 * 获取设备资源层次
	 *
	 * @return
	 */
	public static List<Map> getResourceType()
	{
		PrepareSQL psql = new PrepareSQL("select resource_type_id,resource_name from tab_resourcetype order by resource_type_id");
		adjustJte();
		return jte.queryForList(psql.getSQL());
	}

	/**
	 * 获取权限范围内的设备的资源层次
	 *
	 * @param area_id
	 * @return
	 */
	public static List<Map> getDevResourceType(long area_id)
	{
		PrepareSQL psql = new PrepareSQL("select a.resource_type_id,a.resource_name from tab_resourcetype a,tab_deviceresource b,tab_res_area c "
				+ "where a.resource_type_id=b.resource_type_id and b.device_id=c.res_id and c.res_type=1 and c.area_id="
				+ area_id + " group by a.resource_type_id,a.resource_name");
		adjustJte();
		return jte.queryForList(psql.getSQL());
	}

	/**
	 * 获取范围内的所有设备的厂商列表
	 *
	 * @param area_id:当前用户域ID
	 * @return 厂商列表
	 */
	public static List<Map> getDevVendorListByArea(long area_id)
	{
		PrepareSQL psql = new PrepareSQL("select vendor_id,vendor_name from tab_vendor where vendor_id in"
				+ "(select vendor_id from tab_deviceresource a,tab_res_area b where a.device_id=b.res_id "
				+ "and b.res_type=1 and b.area_id=" + area_id + " group by vendor_id)");
		adjustJte();
		return jte.queryForList(psql.getSQL());
	}

	/**
	 * 根据厂商ID获取权限范围内的设备型号
	 *
	 * @param vendor_id
	 * @param area_id
	 * @return
	 */
	public static List<Map> getDevModelByVendorRes(int vendor_id, long area_id)
	{
		PrepareSQL psql = new PrepareSQL("select a.device_model from tab_deviceresource a,tab_res_area b"
				+ " where a.device_id=b.res_id and b.res_type=1 and a.vendor_id="
				+ vendor_id + " and b.area_id=" + area_id
				+ " group by a.device_model order by a.device_model");
		adjustJte();
		return jte.queryForList(psql.getSQL());
	}

	/**
	 * 根据设备信息获取权限范围内的设备列表
	 *
	 * @param resource_type_id:资源层次【如果不使用该条件，就填入null或''】
	 * @param vendor_id:厂商ID【如果不使用该条件，就填入null或''】
	 * @param device_model：设备型号【如果不使用该条件，就填入null或''】
	 * @param os_version：设备版本【如果不使用该条件，就填入null或''】
	 * @param city_id:设备属地【如果不使用该条件，就填入null或''】
	 * @param area_id：当前用户域ID【必填】
	 * @return 设备列表 <table border=1>
	 *         <tr>
	 *         <th>Key</th>
	 *         <th>Value</th>
	 *         </tr>
	 *         <tr>
	 *         <td>device_id</td>
	 *         <td>设备ID</td>
	 *         <td>device_name</td>
	 *         <td>设备名称</td>
	 *         <td>loopback_ip</td>
	 *         <td>设备IP</td>
	 *         <td>snmp_ro_community</td>
	 *         <td>设备读口令</td>
	 *         </tr>
	 *         </table>
	 */
	public static List<Map> getDevInfoByDevModel(String resource_type_id,
			String vendor_id, String device_model, String os_version, String city_id,
			long area_id)
	{
		String sql = "select device_id,device_name,loopback_ip,snmp_ro_community from tab_deviceresource "
				+ "where device_id in(select res_id from tab_res_area where res_type=1 and area_id="
				+ area_id + ")";
		// 选择了设备层次
		if (resource_type_id != null && !"".equals(resource_type_id))
		{
			sql += " and resource_type_id=" + resource_type_id;
		}
		// 选择了设备厂商
		if (vendor_id != null && !"".equals(vendor_id))
		{
			sql += " and vendor_id=" + vendor_id;
		}
		// 选择了设备型号
		if (device_model != null && !"".equals(device_model))
		{
			sql += " and device_model='" + device_model + "'";
		}
		// 选择了设备版本
		if (os_version != null && !"".equals(os_version))
		{
			sql += " and os_version='" + os_version + "'";
		}
		// 选择了设备属地
		if (city_id != null && !"".equals(city_id))
		{
			sql += " and city_id='" + city_id + "'";
		}
		sql += " order by city_id,vendor_id,loopback_ip";

		PrepareSQL psql = new PrepareSQL(sql);
		adjustJte();
		return jte.queryForList(psql.getSQL());
	}

	/**
	 * 根据设备名称或设备IP模糊查询设备
	 *
	 * @param device_name:设备名称
	 * @param loopback_ip:设备IP
	 * @param area_id:当前用户域ID【必填，不为空】
	 * @return
	 */
	public static List<Map> getDevInfoByNameIp(String device_name, String loopback_ip,
			long area_id)
	{
		String sql = "select device_id,device_name,loopback_ip from tab_deviceresource "
				+ "where device_id in (select res_id from tab_res_area where res_type=1 "
				+ "and area_id=" + area_id + ")";
		// 设备名称不为空
		if (device_name != null && !"".equals(device_name))
		{
			sql += " and device_name like '%" + device_name + "%'";
		}
		// 设备IP不为空
		if (loopback_ip != null && !"".equals(loopback_ip))
		{
			sql += " and loopback_ip like '%" + loopback_ip + "%'";
		}
		sql += " order by loopback_ip";

		PrepareSQL psql = new PrepareSQL(sql);
		adjustJte();
		return jte.queryForList(psql.getSQL());
	}

	/**
	 * 查询所有的设备厂商列表
	 *
	 * @return
	 */
	public static List<Map> getVendorList()
	{
		PrepareSQL psql = new PrepareSQL("select vendor_id,vendor_name from tab_vendor");
		adjustJte();
		return jte.queryForList(psql.getSQL());
	}

	/**
	 * 根据厂商编号查询设备型号
	 *
	 * @param vendor_id
	 *            厂商编号
	 * @return
	 */
	public static List<Map> getModelByVendor(String vendor_id)
	{
		PrepareSQL psql = new PrepareSQL("select distinct device_name from tp_devicetype_info where vendor_id="
				+ vendor_id);
		adjustJte();
		return jte.queryForList(psql.getSQL());
	}

	/**
	 * 返回属地名称和编号的对应关系
	 *
	 * @return
	 */
	public static Map<String, String> getAllCityMap()
	{
		// 查询
		PrepareSQL psql = new PrepareSQL("select city_id,city_name from tab_city");
		adjustJte();
		List list = jte.queryForList(psql.getSQL());

		// 返回数据
		Map<String, String> cityMap = new HashMap<String, String>();
		if (list != null && list.size() > 0)
		{
			int len = list.size();
			for (int i = 0; i < len; i++)
			{
				Map map = (Map) list.get(i);

				cityMap.put(String.valueOf(map.get("city_id")), String.valueOf(map
						.get("city_name")));
			}
		}

		return cityMap;
	}

	/**
	 * 返回属地编号与区号的对应关系
	 *
	 * @return
	 */
	public static Map<String, Map<String, String>> getCityExtMap()
	{
		// 查询
		PrepareSQL psql = new PrepareSQL("select city_id,area_id from tab_city_ext");
		adjustJte();
		List list = jte.queryForList(psql.getSQL());

		// 返回数据
		Map<String, Map<String, String>> cityMap = new HashMap<String, Map<String, String>>();
		if (list != null && list.size() > 0)
		{
			int len = list.size();
			for (int i = 0; i < len; i++)
			{
				Map<String, String> map = (Map<String, String>) list.get(i);
				cityMap.put(String.valueOf(map.get("city_id")), map);
			}
		}

		return cityMap;
	}

	/**
	 * 获取属地名称
	 *
	 * @param cityId
	 *            属地编号
	 * @return
	 */
	public static String getCityName(String cityId)
	{
		PrepareSQL psql = new PrepareSQL("select city_name from tab_city where city_id='" + cityId + "'");
		adjustJte();
		List list = jte.queryForList(psql.getSQL());

		// 返回数据
		if (list != null && list.size() > 0)
		{
			Map map = (Map) list.get(0);
			if (map != null)
			{
				return String.valueOf(map.get("city_name"));
			}
			else
			{
				return "";
			}
		}
		else
		{
			return "";
		}
	}

	/**
	 * 获取radius宽带账号对于的组别名称
	 *
	 * @param cityId
	 *            属地编号
	 * @return
	 */
	public static String getRadiusBroadGroup(String groupId)
	{
		PrepareSQL psql = new PrepareSQL("select group_name from tab_radius_broadgroup where group_id=" + groupId);
		adjustJte();
		List list = jte.queryForList(psql.getSQL());
		System.out.println("list:"+list);
		// 返回数据
		if (list != null && list.size() > 0)
		{
			Map map = (Map) list.get(0);
			if (map != null)
			{
				log.info("sql==="+map.get("group_name"));
				return String.valueOf(map.get("group_name"));
			}
			else
			{
				return "";
			}
		}
		else
		{
			return "";
		}
	}
}
