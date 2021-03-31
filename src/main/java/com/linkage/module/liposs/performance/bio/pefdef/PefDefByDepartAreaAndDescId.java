package com.linkage.module.liposs.performance.bio.pefdef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;

/**
 * 通过分域域表达式与描述表达式ID获取描述
 * 
 * @author Duangr
 * @version 1.0
 * @since 2008-6-30 10:29:21
 * @category PerDef
 * 
 */
public class PefDefByDepartAreaAndDescId extends AbstractDepartAreaPefDefImp
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkage.liposs.webtopo.common.pefdef.AbstractDepartAreaPefDefImp#getPerfAndDescDataByExpression()
	 */
	protected void getPerfAndDescDataByExpression()
	{
		getDescByDepartPrefAndDescExpression(expressionId, device_id, readCom);
	}

	/**
	 * 通过分域性能表达式和描述表达式获取描述信息<br>
	 * 最终结果放在<code>resultMap</code>和<code>indexList</code>中
	 * 
	 * @param expressionId
	 *            分域性能表达式
	 * @param device_id
	 *            设备ID
	 * @param readCom
	 *            读口令
	 */
	private void getDescByDepartPrefAndDescExpression(String expressionId,
			String device_id, String readCom)
	{
		print("通过分域性能表达式<" + expressionId + ">和描述表达式<" + descId
				+ ">获取描述信息[ device_id =" + device_id + " ]");
		// 描述OID构成的Map
		Map<String, String> descOidMap = new HashMap<String, String>();
		// 默认的描述OID
		String defaultDescOid = initDescOidMap(descOidMap);

		Map<String, String> instanceMap = new HashMap<String, String>();

		// 存放分域数据的Map
		instanceMap = dataMap.get(dataList.get(0));
		List<String> copyIndexList = new ArrayList<String>();
		copyIndexList.addAll(indexList);

		// 最终索引列表,最后还是要赋值给indexList
		List<String> resultList = new ArrayList<String>();

		// 每次分域采集的标志位
		int flag = 1;

		// 默认采集失败
		returnFlag = -4;

		resultMap = new HashMap<String, String>();

		// ------------开始按分域采集性能 Begin---------
		// 分域描述Map
		Map<String, String> departDescMap = null;
		int copyIndexListSize = copyIndexList.size();
		for (int i = 0; i < copyIndexListSize; i++)
		{
			// 清空资源,准备去采集性能
			indexList.clear();
			dataList.clear();
			dataMap.clear();
			// 域索引
			String indexStr = (String) copyIndexList.get(i);
			// 域信息
			String areaStr = (String) instanceMap.get(indexStr);
			
			readCom = "@" + areaStr;
			// 采集性能信息
			// 使用的是indexList
			flag = getPerfData(expressionId, device_id, readCom);

			// 采集成功再采描述,不然直接进行下次循环
			if (flag != 1)
				continue;

			returnFlag = 1;

			// 获取描述OID
			String departDescOid = (String) descOidMap.get(areaStr);
			if (departDescOid == null)
				departDescOid = defaultDescOid;
			// 采集描述信息
			departDescMap = getDataByOID(departDescOid, device_id, readCom,
					false);

			// 描述和性能信息进行匹配
			// indexList
			int size = indexList.size();
			for (int j = 0; j < size; j++)
			{

				// 具体的每个分域的索引
				String departIndex = indexList.get(j);
				// 具体分域的描述
				String departDesc = (String) departDescMap.get(departIndex);
				if (departDesc == null)
					departDesc = departIndex;

				// 分域索引存放格式 "分域索引@域信息" 例如: 1@vip
				resultList.add(departIndex + "@" + areaStr);
				// 分域描述存放格式 "分域描述@域信息" 例如: abc@vip
				resultMap.put(departIndex + "@" + areaStr, departDesc + "@"
						+ areaStr);
			}
		}
		// ------------开始按分域采集性能 End---------
		indexList.clear();
		// indexList 中存放的就是最终的索引列表
		indexList = resultList;
	}

	/**
	 * 初始化descOidMap
	 * 
	 * @param descOidMap
	 *            默认的描述OID
	 * @return
	 */
	private String initDescOidMap(Map<String, String> descOidMap)
	{
		// 默认的描述OID
		String defaultDescOid = null;
		if (descOidMap == null)
			descOidMap = new HashMap<String, String>();
		else
			descOidMap.clear();
		String mysql = getOidAndFlagByExpressionIdSQL + descId;
		print("getOidAndFlagByExpressionIdSQL = " + mysql);
		Cursor cursor = DataSetBean.getCursor(mysql);
		Map<String, String> fields = cursor.getNext();
		while (null != fields)
		{
			String oid = (String) fields.get("oid");
			int flag = Integer.parseInt((String) fields.get("flag"));
			String value = (String) fields.get("remark1");
			if (flag == 1)
			{
				defaultDescOid = oid;
			} else
				descOidMap.put(value, oid);
			fields = cursor.getNext();
		}
		print("defaultDescOid = " + defaultDescOid + " & descOidMap = "
				+ descOidMap);
		return defaultDescOid;
	}

}
