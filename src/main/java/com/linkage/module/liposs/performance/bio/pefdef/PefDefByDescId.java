package com.linkage.module.liposs.performance.bio.pefdef;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 仅通过描述表达式ID获取描述
 * 
 * @author Duangr
 * @version 1.0
 * @since 2008-6-30 10:29:21
 * @category PerDef
 * 
 */
public class PefDefByDescId extends AbstractCommonPefDefImp
{

	/**
	 * 描述表达式的OID (对于一个设备的一个性能表达式,它是唯一的)
	 */
	private String descOID = null;

	/**
	 * 存放描述数据
	 * <li>key: descOID的实例索引
	 * <li>value:索引对应的值
	 */
	private Map<String, String> descMap = new HashMap<String, String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkage.liposs.webtopo.common.pefdef.AbstractBasePefDefImp#getDescDataByExpression()
	 */
	protected void getDescDataByExpression()
	{
		getDescByDescExpression(descId, device_id, readCom);
	}

	/**
	 * 通过描述表达式获取描述信息<br>
	 * 最终结果放在<code>resultMap</code>中
	 * 
	 * @param expression2Id
	 *            描述表达式ID
	 * @param device_id
	 *            设备ID
	 * @param readCom
	 *            读口令
	 */
	private void getDescByDescExpression(String expression2Id,
			String device_id, String readCom)
	{
		print("直接通过描述表达式<" + expression2Id + "> 采集描述信息[ device_id ="
				+ device_id + " ]");
		/**
		 * 读取描述信息,放在 <code>descMap</code> 中
		 */
		returnFlag = getDescDataByExpressionId(expression2Id, device_id,
				readCom);

		if (returnFlag == 1)
		{
			// 描述信息采集成功,开始将性能信息与描述信息进行匹配
			/**
			 * 将最终结果放在 <code>resultMap</code> 中
			 */
			// 江苏电信需求,配置不成功就把索引当描述入库,所以不在需要返回的标志位
			compare(expression2Id);
		} else
		{
			// 此时returnFlag只能为 -2,-6
			// 性能信息采集成功,描述信息采集失败
			returnFlag = -3;
		}
	}

	/**
	 * 通过描述表达式的ID读取描述数据(snmpWalk方式)
	 * 
	 * @param expression2Id
	 *            描述表达式ID
	 * @param device_id
	 *            设备ID
	 * @param readCom
	 *            读口令(实际没有作用)
	 * @return 读取数据结果的标志位
	 *         <li> 1: 成功
	 *         <li>-1: 表达式没有配置
	 *         <li>-2: 描述OID没有采集到数据
	 *         <li>-6: 描述表达式ID超过了999
	 */
	private int getDescDataByExpressionId(String expression2Id,
			String device_id, String readCom)
	{
		int flag = 1;
		if (expression2Id == null || "".equals(expression2Id.trim()))
			return -1;
		if (expression2Id.length() > 5)
		{
			flag = -6;
			return flag;
		}
		/**
		 * 读取描述信息,放在 <code>descMap</code> 中
		 */
		// 从pm_expression_context表中读出描述表达式ID相对应的OID
		List<String> oidList =dao.getOIDList(expression2Id);
		if(!oidList.isEmpty())
		{
			String oid = oidList.get(0);
			descOID = oid;
			descMap = getDataByOID(oid, device_id, readCom, false);
			if (descMap.size() == 0)
			{
				// 描述OID没有采集到数据
				flag = -2;
			}
		}		

		print("getDescDataByExpressionId descMap = " + descMap);
		return flag;
	}

	/**
	 * 将通过 <code>expression2Id</code> 采集到的描述信息与性能表达式采集到的性能信息进行对比
	 * <li>描述实例索引应包含全部性能实例索引,不然的话就报错 <font color=red>-41</font>
	 * 
	 * @param expression2Id
	 *            描述表达式ID
	 * @return 读取数据结果的标志位
	 *         <li> 1: 成功
	 *         <li>-41: 性能和描述采集到的索引不一致
	 */
	private int compare(String expression2Id)
	{
		print("compare expression2Id = " + expression2Id);
		int flag = 1;
		String equation = dao.getEquation(expression2Id);
		
		Map<String, String> instanceMap = dataMap.get(dataList.get(0));
		resultMap = new HashMap<String, String>();
		Iterator<String> instanceMapKeyIter = instanceMap.keySet().iterator();
		while (instanceMapKeyIter.hasNext())
		{
			String indexStr = (String) instanceMapKeyIter.next();
			if (descMap.containsKey(indexStr))
			{
				String temp_equation = equation.replaceAll("." + descOID,
						(String) descMap.get(indexStr));
				resultMap.put(indexStr, temp_equation);
			} else
			{
				// 表示性能和描述采集到的索引不一致
				// flag = -41;
				// resultMap.clear();
				// resultMap = null;
				// break;
				// 江苏电信要把不一致的用索引代替
				resultMap.put(indexStr, indexStr);
			}
		}
		return flag;
	}

}
