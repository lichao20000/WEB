
package bio.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.User;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

import dao.resource.CountDeviceDAO;

/**
 * 网关设备统计BIO(家庭网关 flag==1 / 企业网关 flag==2)
 * 
 * @author 段光锐（5250）
 * @version 1.0
 * @since 2008-1-16
 * @category 资源管理
 */
public class CountDeviceBIO
{

	public long getAreaid()
	{
		return areaid;
	}

	public void setAreaid(long areaid)
	{
		this.areaid = areaid;
	}

	public String getCityid()
	{
		return cityid;
	}

	public void setCityid(String cityid)
	{
		this.cityid = cityid;
	}

	public ArrayList getCityList()
	{
		return cityList;
	}

	public void setCityList(ArrayList cityList)
	{
		this.cityList = cityList;
	}

	public Map getCityIdNameMap()
	{
		return cityIdNameMap;
	}

	public void setCityIdNameMap(Map cityIdNameMap)
	{
		this.cityIdNameMap = cityIdNameMap;
	}

	public Map getReversalCityIndexMap()
	{
		return reversalCityIndexMap;
	}

	public void setReversalCityIndexMap(Map reversalCityIndexMap)
	{
		this.reversalCityIndexMap = reversalCityIndexMap;
	}

	public Map getDealResultMap()
	{
		return dealResultMap;
	}

	public void setDealResultMap(Map dealResultMap)
	{
		this.dealResultMap = dealResultMap;
	}

	public Map getCurCityMap()
	{
		return curCityMap;
	}

	public void setCurCityMap(Map curCityMap)
	{
		this.curCityMap = curCityMap;
	}

	public Map getOrderMap()
	{
		return orderMap;
	}

	public void setOrderMap(Map orderMap)
	{
		this.orderMap = orderMap;
	}

	public CountDeviceDAO getCountDao()
	{
		return countDao;
	}

	private static final Logger LOG = LoggerFactory.getLogger(CountDeviceBIO.class);
	private CountDeviceDAO countDao = null;
	/**
	 * 域ID
	 */
	private long areaid = 0;
	/**
	 * 城市ID
	 */
	private String cityid = null;
	/**
	 * 存放当前属地的一级子属地
	 */
	private ArrayList cityList = null;
	/**
	 * 存放(key)city_id与(value)city_name之间的对应关系
	 */
	private Map cityIdNameMap = null;
	/**
	 * 为cityid建立索引的MAP (key:(String)city_id /
	 * value:(List)parent_id等于key或者最终能追溯到key的全部city_id)
	 */
	// private Map cityIndexMap = null;
	/**
	 * 根据cityList中所需要的city_id的信息，将cityIndexMap反转(key:city_id / value:最终关联到的一级子属地的city_id)
	 * value的值仅为当前地市的一级子地市
	 */
	private Map reversalCityIndexMap = null;
	/**
	 * 存放结果集(key:city_id / value:对应的统计数据,包括city_name,设备数devicenum,与用户关联的设备数cusnum)
	 * 结果集中仅显示当前属地的一级子属地
	 */
	private Map dealResultMap = null;
	/**
	 * 存放全部统计数据的总和,包含当前属地名称(city_name),设备数(devicenum),与用户关联的设备数(cusnum)
	 */
	private Map curCityMap = null;
	/**
	 * 存放结果集的顺序(key:city_name / value:顺序)
	 */
	private Map orderMap = null;

	/**
	 * 获取设备统计信息，并根据一级属地分组
	 * 
	 * @param flag
	 *            1.家庭网关 2.企业网关
	 * @param curUser
	 *            当前用户
	 * @return
	 */
	public List getDeviceCount(String flag, User curUser)
	{
		areaid = curUser.getAreaId();
		cityid = curUser.getCityId();
		boolean isAdmin = curUser.isAdmin();
		List resultList = null;
		if (flag == null){
			flag = "1";
		}
		if ("1".equals(flag))
		{
			// 家庭网关
			resultList = (ArrayList<Map>) countDao.getHGWDeviceCount(isAdmin, areaid);
		}
		else if ("2".equals(flag))
		{
			// 企业网关
			resultList = (ArrayList<Map>) countDao.getEGWDeviceCount(isAdmin, areaid);
		}
		else
		{
			// 其他网关
		}
		// 对数据进行整理
		resultList = dealData(resultList);
		return resultList;
	}

	public List getSumDeviceCount()
	{
		return null;
	}

	/**
	 * 处理数据
	 * 
	 * @param list
	 * @return
	 */
	private List dealData(List list)
	{
		List dealReaultList = null;
		// 初始化数据
		initData();
		// 将cityIndexMap反转
		reversalIndexMapByCityList();
		// 处理数据
		dealDataInfo(list);
		// 对结果集进行排序处理
		dealReaultList = dealResult();
		return dealReaultList;
	}

	/**
	 * 处理结果集,将结果排序后放到List中(根据orderMap中的顺序来排)
	 * 
	 * @return
	 */
	private List dealResult()
	{
		List dealReaultList = new ArrayList();
		/**
		 * 当前属地的统计信息不进行排序,先从dealResultMap中删掉, 最后直接加入到dealReaultList中,但加进去的时候是作为统计总和加入的
		 */
		dealResultMap.remove(cityid);
		Iterator valueIt = dealResultMap.values().iterator();
		while (valueIt.hasNext())
		{
			dealReaultList.add((Map) valueIt.next());
		}
		Collections.sort(dealReaultList, new Comparator()
		{

			public int compare(Object o1, Object o2)
			{
				String name1 = (String) ((HashMap) o1).get("city_name");
				String name2 = (String) ((HashMap) o2).get("city_name");
				int i = (Integer) orderMap.get(name1);
				int j = (Integer) orderMap.get(name2);
				return (i - j);
			}
		});
		dealReaultList.add(curCityMap);
		return dealReaultList;
	}

	/**
	 * 处理数据,将处理好的数据放到dealResultMap中 dealResultMap包含当前属地的一级子属地的统计信息 curCityMap包含当前属地的统计总和信息
	 * 
	 * @param list
	 */
	private void dealDataInfo(List list)
	{
		Iterator it = list.iterator();
		String id = null;
		String pid = null;
		String name = null;
		int deviceNum = 0;
		int customerNum = 0;
		int allDeviceNum = 0;
		int allCustomerNum = 0;
		// 剔除无效数据 存在city_id=undefined, city_name=null的情况
		while (it.hasNext())
		{
			HashMap map = (HashMap) it.next();
			id = (String) map.get("city_id");
			if (null == id || "undefined".equals(id))
			{
				// 无效数据过滤
				continue;
			}
			name = (String) map.get("city_name");
			LOG.warn(map.toString());
			if (Global.NXDX.equals(Global.instAreaShortName))
			{
				deviceNum = StringUtil.getIntValue(map, "devicenum", 0);
				customerNum = StringUtil.getIntValue(map, "cusnum", 0);
//				deviceNum = (Integer) map.get("devicenum");
//				if (null == map.get("cusnum"))
//				{
//					customerNum = 0;
//				}
//				else
//				{
//					customerNum = (Integer) map.get("cusnum");
//				}
			}
			else
			{
				deviceNum = Integer.valueOf((String) map.get("devicenum"));
				customerNum = Integer.valueOf((String) map.get("cusnum"));
			}
			pid = (String) reversalCityIndexMap.get(id);
			if (pid == null)
			{
				pid = id;
			}
			// 存放数据的结构体
			HashMap valueMap = (HashMap) dealResultMap.get(pid);
			if (valueMap == null)
			{
				// 该数据不是所需要的,过滤掉
				// 设备是由域过滤的,有可能该设备的city_id不在当前属地以及其子属地之中
				continue;
			}
			/**
			 * 经过上面的过滤,下面才是所需要的数据
			 */
			allDeviceNum += deviceNum;
			allCustomerNum += customerNum;
			deviceNum += (Integer) valueMap.get("devicenum");
			customerNum += (Integer) valueMap.get("cusnum");
			valueMap.put("city_id", pid);
			valueMap.put("devicenum", deviceNum);
			valueMap.put("cusnum", customerNum);
		}
		curCityMap.put("city_id", cityid);
		curCityMap.put("city_name", (String) cityIdNameMap.get(cityid));
		curCityMap.put("devicenum", allDeviceNum);
		curCityMap.put("cusnum", allCustomerNum);
	}

	/**
	 * 将cityIndexMap初始化(key:city_id / value:parent_id等于key或者最终能追溯到key的全部city_id)
	 * 将cityList初始化(存放当前地市的一级子地市) 将cityIdNameMap初始化(city_id与city_name的对应Map)
	 * 将dealResultMap初始化(仅包含当前属地的一级子属地,当前属地信息放在curCityMap中) 将showChild标志初始化 将orderMap初始化
	 * 将curCityMap初始化(其实仅仅是new了一下)
	 */
	private void initData()
	{
		String id = null;
		String pid = null;
		String cityName = null;
		// cityIndexMap = new HashMap();
		cityList = new ArrayList();
		cityIdNameMap = new HashMap();
		dealResultMap = new HashMap();
		orderMap = new HashMap();
		curCityMap = new HashMap();
		// List cityIndexList = null;
		HashMap valueMap = null;
		ArrayList<Map> allCityList = (ArrayList<Map>) countDao.getAllCityId();
		Iterator it = allCityList.iterator();
		int count = 0;
		while (it.hasNext())
		{
			count++;
			HashMap map = (HashMap) it.next();
			id = (String) map.get("city_id");
			pid = (String) map.get("parent_id");
			cityName = (String) map.get("city_name");
			orderMap.put(cityName, count);
			// 将city_id 与city_name对应起来
			cityIdNameMap.put(id, cityName);
			// 结果集(dealResultMap)中仅显示当前属地的一级子属地
			if (pid.equals(cityid) || id.equals(cityid))
			{
				// 初始化dealResultMap
				valueMap = new HashMap();
				valueMap.put("city_id", id);
				valueMap.put("city_name", cityName);
				valueMap.put("devicenum", 0);
				valueMap.put("cusnum", 0);
				dealResultMap.put(id, valueMap);
				// 将parent_id为当前用户城市属地的cityid放入cityList(List)中
				if (!id.equals(cityid))
				{
					cityList.add(id);
				}
			}
			/**
			 * modify by qixueqi 针对多级属地，原代码已不适应
			 */
			/**
			 * cityIndexList = (List) cityIndexMap.get(pid); if (cityIndexList == null) {
			 * cityIndexList = new ArrayList(); cityIndexMap.put(pid, cityIndexList); }
			 * cityIndexList.add(id);
			 **/
		}
	}

	/**
	 * 根据cityList中所需要的city_id的信息，将cityIndexMap反转
	 */
	private void reversalIndexMapByCityList()
	{
		reversalCityIndexMap = new HashMap();
		Iterator it = cityList.iterator();
		List cityIndexList = null;
		while (it.hasNext())
		{
			String pid = (String) it.next();
			cityIndexList = CityDAO.getAllNextCityIdsByCityPid(pid);
			// cityIndexList = (List) cityIndexMap.get(pid);
			if (null == cityIndexList || cityIndexList.size() < 1)
			{
				continue;
			}
			Iterator cityIt = cityIndexList.iterator();
			while (cityIt.hasNext())
			{
				String id = (String) cityIt.next();
				reversalCityIndexMap.put(id, pid);
			}
			cityIndexList = null;
		}
	}

	/**
	 * 构建reversalCityIndexMap,使得value全部都等于当前属地的city_id
	 */
	private void reversalIndexMapNoChild()
	{
		reversalCityIndexMap = new HashMap();
		List cityIndexList = null;
		cityIndexList = CityDAO.getAllNextCityIdsByCityPid(cityid);
		// cityIndexList = (List) cityIndexMap.get(cityid);
		if (null == cityIndexList || cityIndexList.size() < 1)
		{
			// 说明当前属地下面没有再挂其他属地
		}
		Iterator cityIt = cityIndexList.iterator();
		while (cityIt.hasNext())
		{
			String id = (String) cityIt.next();
			reversalCityIndexMap.put(id, cityid);
		}
		cityIndexList = null;
	}

	/**
	 * 注入DAO
	 * 
	 * @param countDao
	 */
	public void setCountDao(CountDeviceDAO countDao)
	{
		this.countDao = countDao;
	}

	public List<Map> getDetail(String gw_type, String cityId, String deviceState, int curPage_splitPage,
			int num_splitPage)
	{
		LOG.debug("dao==>getDetail({},{},{},{},{},{})", new Object[] { cityId,
				curPage_splitPage, num_splitPage });
		return countDao.getDetail(gw_type, cityId, deviceState, curPage_splitPage, num_splitPage);
	}

	public int getCount(String gw_type, String cityId, String servTypeId, int curPage_splitPage,
			int num_splitPage)
	{
		return countDao.getCount(gw_type, cityId, servTypeId, curPage_splitPage, num_splitPage);
	}

	/**
	 * 详细信息导出
	 */
	public List<Map> getDetailExcel(String cityId, String gw_type, String isBindState)
	{
		return countDao.getDetailExcel(cityId, gw_type, isBindState);
	}
}
