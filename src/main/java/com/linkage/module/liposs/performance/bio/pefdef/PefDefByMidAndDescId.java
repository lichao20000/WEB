package com.linkage.module.liposs.performance.bio.pefdef;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 通过中转表达式与描述表达式ID获取描述
 * 
 * @author Duangr
 * @version 1.0
 * @since 2008-6-30 10:29:21
 * @category PerDef
 * 
 */
public class PefDefByMidAndDescId extends AbstractCommonPefDefImp
{

	/**
	 * 存放中间OID的采集数据
	 * <li>key: midOID的实例索引
	 * <li>value:索引对应的值
	 */
	private Map<String, String> midMap = new HashMap<String, String>();

	/**
	 * 存放描述数据
	 * <li>key: descOID的实例索引
	 * <li>value:索引对应的值
	 */
	private Map<String, String> descMap = new HashMap<String, String>();

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.webtopo.common.pefdef.AbstractBasePefDefImp#getDescDataByExpression()
	 */
	protected void getDescDataByExpression()
	{
		getDescDataByMidAndDescOidSnmpWalk(device_id, readCom);
	}

	/**
	 * 通过中间转换OID与描述OID来读取数据(snmpWalk方式)
	 * 
	 * @param device_id
	 *            设备ID
	 * @param readCom
	 *            读口令(实际没有作用)
	 * @return 读取数据结果的标志位
	 *         <li> 1: 成功
	 *         <li>-32: middleoid采集到的索引不能完全包含性能oid采集到的索引
	 *         <li>-33: describeoid采集到的索引不能完全包含middleoid采集到的值
	 *         <li>-34: middleoid没有采集到值
	 *         <li>-35: describeoid没有采集到值
	 */
	private int getDescDataByMidAndDescOidSnmpWalk(String device_id,
			String readCom)
	{
		print("通过中间转换表达式<" + middleId
				+ ">与描述表达式<" + descId + ">来采集描述信息 [ device_id =" + device_id
				+ " ]");
		int flag = 1;
		String midValue = null;
		String descValue = null;
		// 获取一个性能OID的实例MAP
		Map<String, String> instanceMap = dataMap.get(dataList.get(0));
		// 获取中转OID采集到的数据
		String midOID = getOnlyOidById(middleId);
		if (midOID != null)
			midMap = getDataByOID(midOID, device_id, readCom, false);
		if (midMap.size() == 0)
		{
			// 中转OID没有采集到数据
			flag = -34;
		}
		print("通过{middleoid}采集到的值 midMap = " + midMap);
		// 获取描述OID采集到的数据
		String descOID = getOnlyOidById(descId);
		if (descOID != null)
			descMap = getDataByOID(descOID, device_id, readCom, false);
		if (descMap.size() == 0)
		{
			// 描述OID没有采集到数据
			flag = -35;
		}
		print("通过{describeoid}采集到的值 descMap = " + descMap);
		resultMap = new HashMap<String, String>();
		// 性能OID采集出来的索引迭代对象
		Iterator<String> instanceMapKeyIter = instanceMap.keySet().iterator();
		while (instanceMapKeyIter.hasNext())
		{
			String indexStr = instanceMapKeyIter.next();
			if (midMap.containsKey(indexStr))
			{
				// 找到中转OID采集到的值
				midValue = (String) midMap.get(indexStr);
				// 把这个值作为索引去描述OID采集到的值中去找对应的值
				descValue = (String) descMap.get(midValue);
				if (descValue != null)
				{
					resultMap.put(indexStr, descValue);
				} else
				{
					// describeoid采集到的索引不能完全包含middleoid采集到的值
					flag = -33;
					// 江苏电信要把不一致的用索引代替
					resultMap.put(indexStr, indexStr);
				}
			} else
			{
				// middleoid采集到的索引不能完全包含性能oid采集到的索引
				flag = -32;
				// 江苏电信要把不一致的用索引代替
				resultMap.put(indexStr, indexStr);
			}
		}

		print("采集结果集 resultMap = " + resultMap);
		return flag;
	}

}
