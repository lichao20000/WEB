
package com.linkage.module.gwms.diagnostics.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2009-8-13
 */
public class ComputeUtil
{

	private static Logger logger = LoggerFactory.getLogger(ComputeUtil.class);
	public static Object LOCK = new Object();
	public static Map<String, List> expertMap;
	public static Map<String, String[]> diagDescMap;
	// NAT开关，业界标准
	public static String NAT = "1";
	// WAN PVC的接口状态
	public static String WANSTATUS = "Connected";
	// 拨号连接错误码
	public static String LASTERR = "ERROR_NONE";
	// LAN口的接入状态
	public static String LANSTATUS = "Up";
	// 线路状态
	public static String WIRESTATUS = "Up";
	// 下联PC的状态
	public static String HOSTACTIVE = "1";

	/**
	 * 专家库的对应Map,为每一个字段建一个Map<String,List>
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-8-13
	 * @return Map<String,Map<String,List>>
	 */
	public static Map<String, List> initExpertMap()
	{
		logger.debug("initExpertMap()");
		synchronized (LOCK)
		{
			expertMap = new HashMap<String, List>();
			diagDescMap = new HashMap<String, String[]>();
			List expertList = getExpertList();
			if (null == expertList)
			{
				logger.error("专家库List为空");
				return null;
			}
			int listSize = expertList.size();
			List<Map> statusList = new ArrayList<Map>();
			List<Map> typeList = new ArrayList<Map>();
			List<Map> upRateList = new ArrayList<Map>();
			List<Map> downRateList = new ArrayList<Map>();
			List<Map> upAttenList = new ArrayList<Map>();
			List<Map> downAttenList = new ArrayList<Map>();
			List<Map> lastErrList = new ArrayList<Map>();
			List<Map> natList = new ArrayList<Map>();
			List<Map> wlanList = new ArrayList<Map>();
			List<Map> usernameList = new ArrayList<Map>();
			List<Map> dnsList = new ArrayList<Map>();
			List<Map> wanInterStatusList = new ArrayList<Map>();
			List<Map> lanStatusList = new ArrayList<Map>();
			List<Map> lan2StatusList = new ArrayList<Map>();
			List<Map> lanHostList = new ArrayList<Map>();
			for (int i = 0; i < listSize; i++)
			{
				Map tMap = (Map) expertList.get(i);
				// 线路信息
				if ("wireStatus".equals(tMap.get("ex_column")))
				{
					// 线路状态
					statusList.add(tMap);
				}
				else if ("wireType".equals(tMap.get("ex_column")))
				{
					// 线路协议
					typeList.add(tMap);
				}
				else if ("wireUpRate".equals(tMap.get("ex_column")))
				{
					// 上行速率
					upRateList.add(tMap);
				}
				else if ("wireDownRate".equals(tMap.get("ex_column")))
				{
					// 下行速率
					downRateList.add(tMap);
				}
				else if ("wireUpAtten".equals(tMap.get("ex_column")))
				{
					// 上行衰减
					upAttenList.add(tMap);
				}
				else if ("wireDownAtten".equals(tMap.get("ex_column")))
				{
					// 下行衰减
					downAttenList.add(tMap);
				}
				else if ("lastError".equals(tMap.get("ex_column")))
				{
					// 为拨号错误码的记录
					lastErrList.add(tMap);
				}
				else if ("nat".equals(tMap.get("ex_column")))
				{
					// nat的记录
					natList.add(tMap);
				}
				else if ("wlanPowerValue".equals(tMap.get("ex_column")))
				{
					// Wlan模块功率
					wlanList.add(tMap);
				}
				else if ("username".equals(tMap.get("ex_column")))
				{
					usernameList.add(tMap);
				}
				else if ("dns".equals(tMap.get("ex_column")))
				{
					dnsList.add(tMap);
				}
				else if ("wanInternetStatus".equals(tMap.get("ex_column")))
				{
					wanInterStatusList.add(tMap);
				}
				else if ("lanStatus".equals(tMap.get("ex_column")))
				{
					lanStatusList.add(tMap);
				}
				else if ("lan2Status".equals(tMap.get("ex_column")))
				{
					lan2StatusList.add(tMap);
				}
				else if ("lanHost".equals(tMap.get("ex_column")))
				{
					lanHostList.add(tMap);
				}
				else if ("".equals(tMap.get("ex_column")))
				{
					String[] diagArr = {
							StringUtil.getStringValue(tMap.get("ex_fault_desc")),
							StringUtil.getStringValue(tMap.get("ex_suggest")) };
					diagDescMap.put(StringUtil.getStringValue(tMap.get("id")), diagArr);
				}
			}
			expertMap.put("dns", dnsList);
			expertMap.put("wireStatus", statusList);
			expertMap.put("wireUpAtten", upAttenList);
			expertMap.put("wireUpRate", upRateList);
			expertMap.put("wireType", typeList);
			expertMap.put("wireDownRate", downRateList);
			expertMap.put("nat", natList);
			expertMap.put("lastError", lastErrList);
			expertMap.put("wireDownAtten", downAttenList);
			expertMap.put("wlanPowerValue", wlanList);
			expertMap.put("username", usernameList);
			expertMap.put("wanInternetStatus", wanInterStatusList);
			expertMap.put("lanStatus", lanStatusList);
			expertMap.put("lanHost", lanHostList);
			expertMap.put("lan2Status", lan2StatusList);
		}
		return expertMap;
	}

	/**
	 * 获取专家建议
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-8-13
	 * @return String[] 故障描述/专家建议
	 */
	public static String[] getSuggest(String column, String gather, String nomal)
	{
		logger.debug("getSuggest(" + column + ",{},{})", gather, nomal);
		// 返回诊断结果数组
		String[] diagArr = { "", "" };
		if (null == expertMap)
		{
			initExpertMap();
			logger.debug("initExpertMap() over");
		}
		if (null == expertMap || null == expertMap.get(column)
				|| StringUtil.IsEmpty(StringUtil.getStringValue(gather))
				|| StringUtil.IsEmpty(StringUtil.getStringValue(nomal)))
		{
			logger.warn("expertMap || columnList || gather || nomal is null");
			return diagArr;
		}
		// 采集值-标准值
		Object obj = minus(gather, nomal);
		if (nomal.equals(StringUtil.getStringValue(obj)))
		{
			logger.debug("gather = nomal 采集值和标准值一致");
		}
		else
		{
			logger.debug("gather - nomal = " + obj);
			List columnList = (List) expertMap.get(column);
			if (null != columnList)
			{
				int lsize = columnList.size();
				for (int i = 0; i < lsize; i++)
				{
					Map tMap = (Map) columnList.get(i);
					String regular = StringUtil.getStringValue(tMap.get("ex_regular"));
					Object alia = tMap.get("ex_bias");
					if (compare(obj, alia, regular))
					{
						diagArr[0] = StringUtil.getStringValue(tMap.get("ex_fault_desc"));
						diagArr[1] = StringUtil.getStringValue(tMap.get("ex_suggest"));
					}
				}
			}
		}
		return diagArr;
	}

	/**
	 * 数字型的，以为标准范围形式，获取专家建议
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-8-17
	 * @return String[] 故障描述/专家建议
	 */
	public static String[] getSuggest(String column, double gather, double min, double max)
	{
		logger.debug("getSuggest(" + column + ",{},{})", gather, min + "-" + max);
		// 返回诊断结果数组
		String[] diagArr = { "", "" };
		if (null == expertMap)
		{
			initExpertMap();
			logger.debug("initExpertMap() over");
		}
		if (null == expertMap || null == expertMap.get(column))
		{
			logger.warn("expertMap || columnList is null");
			return diagArr;
		}
		// 采集值-标准值
		int result = range(gather, min, max);
		if (0 == result)
		{
			logger.debug("gather = nomal 采集值在标准值范围内");
		}
		else
		{
			logger.debug(" range(gather, min, max) = " + result);
			List columnList = (List) expertMap.get(column);
			if (null != columnList)
			{
				int lsize = columnList.size();
				for (int i = 0; i < lsize; i++)
				{
					Map tMap = (Map) columnList.get(i);
					String regular = StringUtil.getStringValue(tMap.get("ex_regular"));
					Object alia = tMap.get("ex_bias");
					if (compare(result, alia, regular))
					{
						diagArr[0] = StringUtil.getStringValue(tMap.get("ex_fault_desc"));
						diagArr[1] = StringUtil.getStringValue(tMap.get("ex_suggest"));
					}
				}
			}
		}
		return diagArr;
	}

	/**
	 * 健康值的范围比较,在范围内返回0, 大于最大值返回2, 小于最小值返回-2
	 * 
	 * @param relt
	 *            :检查值 min:最小值 max:最大值
	 * @author Jason(3412)
	 * @date 2009-8-17
	 * @return int
	 */
	public static int range(double relt, double min, double max)
	{
		if (relt >= min)
		{
			if (relt < max)
			{
				return 0;
			}
			else
			{
				return 2;
			}
		}
		else
		{
			return -2;
		}
	}

	/**
	 * String型的两个对象相减,相等返回"true",否则返回obj1
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-8-13
	 * @return Object
	 */
	public static String minus(String obj1, String obj2)
	{
		logger.debug("minus({},{})", obj1, obj2);
		if (StringUtil.IsEmpty(obj1) || StringUtil.IsEmpty(obj2))
		{
			return obj1;
		}
		if (obj1.equals(obj2) || obj1.contains(obj2))
		{
			// 这里是考虑到有类似DNS形式的参数：采集到两个DNS，匹配出其中一个就认为正确
			logger.debug("字符串类型，返回obj2");
			return obj2;
		}
		else
		{
			logger.debug("字符串类型，返回obj1");
			return obj1;
		}
	}

	/**
	 * double型的两个对象相减
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-8-13
	 * @return Object
	 */
	public static double minus(double obj1, double obj2)
	{
		logger.debug("minus({},{})", obj1, obj2);
		return (obj1 - obj2);
	}

	/**
	 * obj1减去obj2,obj1和obj2的类型必须一致 只支持字符串类型和数字类型; 字符串类型如果相等返回0,如果不相等返回obj1 如果数字型直接返回dou1 -
	 * dou2(对象转换为double类型)
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-8-13
	 * @return Object
	 */
	// public static Object minus(Object obj1, Object obj2) {
	// logger.debug("minus({},{})", obj1, obj2);
	// if (obj1.getClass() != obj2.getClass()) {
	// logger.debug("obj1 与 obj2 为不同类型");
	// return null;
	// } else {
	// if (obj1.equals(obj2)) {
	// logger.debug("obj1 = obj2 return 0");
	// return 0;
	// } else {
	// if (obj1.getClass().isInstance(String.class)) {
	// logger.debug("字符串类型，返回obj1");
	// return obj1;
	// } else {
	// Double dou1 = StringUtil.getDoubleValue(obj1);
	// Double dou2 = StringUtil.getDoubleValue(obj2);
	// return (dou1 - dou2);
	// }
	// }
	// }
	// }
	/**
	 * obj1 compare(比较符) obj2,obj1和obj2的类型必须一致 支持字符串和数字类型,支持的操作类型有:(可根据自己的需要进行扩展) "equals"
	 * "between" "in" "bigger" "smaller" "not between"
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-8-13
	 * @return boolean
	 */
	public static boolean compare(Object obj1, Object obj2, String compare)
	{
		logger.debug("compare({} " + compare + " {})", obj1, obj2);
		if (null == obj1 || null == obj2)
		{
			logger.debug("obj1 or obj2 is null");
			return false;
		}
		else
		{
			if ("equals".equals(compare))
			{
				// 适用于任何类型
				logger.debug("return obj1 equals obj2");
				return obj1.equals(obj2);
			}
			else if ("in".equals(compare))
			{
				// 适用于字符串格式为以','隔开
				String str1 = StringUtil.getStringValue(obj1);
				String str2 = StringUtil.getStringValue(obj2);
				if (str2.contains("else_all"))
				{
					// 带有'else_all'字段表示该专家建议符合任何一个采集值(标准值除外)。
					logger.debug("obj2 包含所有其他非标准值的情况");
					return true;
				}
				logger.debug("return obj2 contains obj1");
				return str2.contains(str1);
			}
			else if ("between".equals(compare))
			{
				// 适用于数字型obj1为数字型,obj2为格式'n-n'
				Double dou1 = StringUtil.getDoubleValue(obj1);
				String betwStr = StringUtil.getStringValue(obj2);
				String[] arrayStr = betwStr.split("\\\\|");
				if (arrayStr.length != 2)
				{
					logger.warn("参数obj2格式不正确");
					return false;
				}
				else
				{
					Double sm = StringUtil.getDoubleValue(arrayStr[0]);
					Double bg = StringUtil.getDoubleValue(arrayStr[1]);
					logger.debug("return sm <= dou1 && dou1 <= bg");
					return (sm <= dou1 && dou1 <= bg);
				}
			}
			else if ("bigger".equals(compare))
			{
				// 适用于数字型obj1为数字型,obj2为数字型
				Double dou1 = StringUtil.getDoubleValue(obj1);
				Double dou2 = StringUtil.getDoubleValue(obj2);
				logger.debug("return dou1 >= dou2");
				return (dou1 >= dou2);
			}
			else if ("smaller".equals(compare))
			{
				// 适用于数字型obj1为数字型,obj2为数字型
				Double dou1 = StringUtil.getDoubleValue(obj1);
				Double dou2 = StringUtil.getDoubleValue(obj2);
				logger.debug("return dou1 <= dou2");
				return (dou1 <= dou2);
			}
			else if ("not between".equals(compare))
			{
				// 适用于数字型obj1为数字型,obj2为格式'n-n'
				Double dou1 = StringUtil.getDoubleValue(obj1);
				String betwStr = StringUtil.getStringValue(obj2);
				String[] arrayStr = betwStr.split("\\\\|");
				if (arrayStr.length != 2)
				{
					logger.warn("参数obj2格式不正确");
					return false;
				}
				else
				{
					Double sm = StringUtil.getDoubleValue(arrayStr[0]);
					Double bg = StringUtil.getDoubleValue(arrayStr[1]);
					return (sm > dou1 || dou1 > bg);
				}
			}
			else
			{
				logger.debug("未知比较符");
				return false;
			}
		}
	}

	/**
	 * 返回专家库中的记录
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-8-13
	 * @return List
	 */
	public static List getExpertList()
	{
		logger.debug("getExport()");
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select ex_column,ex_fault_desc,ex_suggest,id from gw_expert");
		}else{
			psql.append("select * from gw_expert");
		}
		return DataSetBean.executeQuery(psql.getSQL(), null);
	}
}
