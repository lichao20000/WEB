package com.linkage.liposs.buss.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;

/**
 * 该类提供安全网关的菜单展现类 <br>
 * 该类考虑到只是初始化一次，并不会每次调用都初始化，因此采用多次防范数据库查询的操作，而非将设备全部载入内存，节省内存空间
 * 
 * @author 王志猛(5194) tel：13701409234
 * @version 1.0
 * @since 2008-3-27
 * @category com.linkage.liposs.buss.menu 版权：南京联创科技 网管科技部
 */
public class MenuSecurityGWDAO
{
	private JdbcTemplate jt;// spring的jdbc模版类
	private boolean init = true;// 是否初始化
	private Map<String, SercurityItem> SGWList = new HashMap<String, SercurityItem>();// 键位域id，值为安全设备列表
	private static String getAllArea = "select area_id,area_name,area_pid,area_layer,remark from tab_area";
	private static String getAllSGW = "select device_id,device_name,customer_name as customname from tab_gw_res_area a " 
									+ "left join tab_gw_device b  on  b.device_id=a.res_id " 
									+ "left join tab_customerinfo c on b.customer_id=c.customer_id " 
									+ "where (a.res_type=1 or a.res_type=2) and b.gw_type=2 and a.area_id= ";
	/**
	 * 负责初始化数据的方法
	 */
	synchronized private void init()
	{
		if (init)
			{
				PrepareSQL psql = new PrepareSQL(getAllArea);
				List<Map> allArea = jt.queryForList(psql.getSQL());
				createSGW(allArea);// 创建菜单列表
				for (Map m : allArea)
					{
						cleanSGW(m.get("area_id").toString());// 清除重复的菜单
					}
				init = false;
			}
	}
	/**
	 * 通过域id获取菜单的json数组
	 * 
	 * @param areaId
	 *            域id
	 * @return 菜单的json数组
	 */
	public SercurityItem getSecurityGWMenu(String areaId)
	{
		if (init)
			{
				init();
			}
		return SGWList.get(areaId);
	}
	/**
	 * 重新装载安全网关用户到内存中
	 */
	public void reloadGWMenu()
	{
		init = true;
		init();
	}
	/**
	 * 创建安全网关菜单列表
	 * 
	 * @param areas
	 *            域列表
	 * @param devs
	 *            设备列表
	 */
	private void createSGW(List<Map> areas)
	{
		// 初始化area设备列表
		for (Map m : areas)
			{
				SercurityItem si = new SercurityItem();
				String area_id = m.get("area_id").toString();
				si.setArea_id(area_id);
				si.setArea_pid(m.get("area_pid").toString());
				si.setArea_name(m.get("remark").toString());
				PrepareSQL psql = new PrepareSQL(getAllSGW + area_id);
				List<Map> gwsDev = jt.queryForList(psql.getSQL());
				si.setSGWList(gwsDev);
				SGWList.put(area_id, si);
			}
		// 从父域中清除子域的资源
		for (Map m : areas)
			{
				String area_id = m.get("area_id").toString();
				cleanSGW(area_id);
			}
	}
	/**
	 * 清除重复的安全网关菜单，如果子域包含了该设备，则赋予不需要出现该设备
	 * 
	 * @param areas
	 */
	private void cleanSGW(String area_id)
	{
		SercurityItem si = SGWList.get(area_id);
		if (si != null)
			{
				String pareaid = si.getArea_pid();
				SercurityItem pitem = SGWList.get(pareaid);
				// 如果当前域即没有子域，也没有安全网关，则从当前的域列表中删除该域
				if ((!si.isHasChild()) && (!si.isHasSGW()))
					{
						// 如果父域不为空，那么也需要删除父域对于该于的引用
						if (pitem != null)
							{
								pitem.getChildArea().remove(si);
							}
						SGWList.remove(area_id);
					}
				else
					{
						// 其他情况，则需要删除该域中父域说包含的该域的安全网关设备，并且将该域付给父域，让父域持有对该域的引用
						if (pitem != null)
							{
								pitem.getSGWList().removeAll(si.getSGWList());
								if (!pitem.getChildArea().contains(si))
									{
										pitem.getChildArea().add(si);
									}
							}
					}
				cleanSGW(pareaid);
				pareaid = null;
			}
	}
	/**
	 * 设置数据源
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}
}
