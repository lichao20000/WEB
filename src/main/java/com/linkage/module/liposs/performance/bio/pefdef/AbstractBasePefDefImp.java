package com.linkage.module.liposs.performance.bio.pefdef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Performance.Data;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.corba.interfacecontrol.SnmpGatherInterface;
import com.linkage.module.liposs.performance.bio.Pm_Map_Instance;
import com.linkage.module.liposs.performance.dao.I_configPmeeDao;

/**
 * 基础性能配置的抽象实现类
 *
 * @author Duangr
 * @version 1.0
 * @since 2008-6-30 10:29:21
 * @category PerDef
 */
public abstract class AbstractBasePefDefImp implements BasePefDefInterfance
{
	/**
	 * log4j日志
	 */
	private static Logger log = LoggerFactory.getLogger(AbstractBasePefDefImp.class);
	/**
	 * 设备ID
	 */
	protected String device_id = null;
	/**
	 * 数据库操作类
	 */
	protected I_configPmeeDao dao = null;
	/**
	 * 单个设备单个性能表达式的实例配置信息（如果没有配置，则map长度为0）
	 */
	protected Map<String, Pm_Map_Instance> instanceConfigParam = null;

	/**
	 * 配置参数
	 */
	private Pm_Map_Instance param = null;

	/**
	 * 性能表达式ID
	 */
	protected String expressionId = null;

	/**
	 * 后台接口中用到的一个传出参数,不过没有用到
	 */
	private org.omg.CORBA.IntHolder datatype = new org.omg.CORBA.IntHolder();

	/**
	 * 读口令 <br>
	 * 若直接从数据库查找读口令,则传空串;否则传过去的字符串将加到数据库中查找出来的数据后面. <br>
	 * 例如:readCom = "@wlan",实际读口令为 "snmp_ro_community@wlan"
	 */
	protected String readCom = "";
	/**
	 * 描述表达式ID
	 */
	protected String descId = null;
	/**
	 * 中转表达式ID
	 */
	protected String middleId = null;
	/**
	 * 分域域表达式ID
	 */
	protected String departAreaId = null;
	/**
	 *
	 * domain表达式Id add by zs(3704) JSDX_JS-REQ-20080805-SJ-001
	 */
	protected String domainId = null;
	/**
	 * domain与VR对应关系的表达式ID add by zs(3704) JSDX_JS-REQ-20080805-SJ-001
	 */
	protected String domainVRId = null;
	/**
	 * domain与ip_pool对应关系的表达式ID add by zs(3704) JSDX_JS-REQ-20080805-SJ-001
	 */
	protected String domainIpPoolId = null;
	/**
	 * add by zs(3704)
	 */
	protected List sqlList = null;

	/**
	 * 由表达式ID获取OID,falg,value的SQL XJDX-XJ-BUG-20080804-XXF-001 --- modify by
	 * zhangsong
	 */
	protected final String getOidAndFlagByExpressionIdSQL = "select distinct oid,flag,value,remark1 from pm_expression_context where nodetype in(2,3) and expressionid=";

	/**
	 * 获得SE800中特殊的OID add by zhangsong(3704)
	 */
	protected final String getSEOidByExpressionIdSQL = "select distinct oid,flag,value,remark1 from pm_expression_context where nodetype=7 and expressionid=";

	/**
	 * 存放性能OID的列表
	 */
	protected List<String> dataList = new ArrayList<String>();
	/**
	 * 存放性能数据
	 * <li>key:oid(与dataList中的OID对应)
	 * <li>value:该oid采集到的数据(Map格式,key:实例索引 ; value:索引对应的值)
	 */
	protected Map<String, Map<String, String>> dataMap = new LinkedHashMap<String, Map<String, String>>();
	/**
	 * 存放采集到的结果集
	 * <li>key: 性能表达式OID采集到的实例索引
	 * <li>value: 性能表达式对应的描述表达式OID采集到的描述值
	 */
	protected Map<String, String> resultMap = null;
	/**
	 * 用于存放性能表达式OID获得的索引列表
	 */
	protected List<String> indexList = new ArrayList<String>();
	/**
	 * 取第一个OID的数据
	 * 对于性能表达式可能对应多个OID,在而indexList里面只需存放一次索引,索引在取第一个OID的数据时往indexList中赋值,其他OID则不需要赋值
	 */
	private boolean isFirst = true;
	/**
	 * 配置结果的标志位
	 * <li>1: 表示成功(默认成功)
	 * <li>-1：表示超时
	 * <li>-2：表示描述OID采集不到数据
	 * <li>-21:表示几个性能oid中其中有一个oid采集不到数据
	 * <li>-3:表示没有采集到描述信息
	 * <li><font color=red>-31:middleoid与describeoid没有配置正确</font>
	 * <li><font color=red>-32:middleoid采集到的索引不能完全包含性能oid采集到的索引</font>
	 * <li><font color=red>-33:describeoid采集到的索引不能完全包含middleoid采集到的值</font>
	 * <li>-4:表示几个性能oid采集到的索引数不一致
	 * <li>-41:表示性能和描述采集到的索引不一致
	 * <li>-6：表示表达式ID超过了999
	 *
	 */
	protected int returnFlag = 1;
	/**
	 * 日志输出的方法
	 *
	 * @param logInfo
	 *            日志内容
	 */
	protected void print(String logInfo)
	{
		log.debug(logInfo);
	}
	/**
	 * 在 <code>device_id</code> 后面用"0"补足22位
	 *
	 * @param device_id
	 *            设备ID
	 * @return
	 */
	protected String getIDCode(String device_id)
	{
		String temp = "";
		for (int i = device_id.length(); i < 22; i++)
		{
			temp += "0";
		}
		return temp + device_id;
	}
	/**
	 * 在 <code>i</code> 前面面用"0"补足5位
	 *
	 * @param i
	 * @return
	 */
	protected String getNum(int i)
	{
		String tempStr = String.valueOf(i);
		switch (tempStr.length())
		{
			case 1:
			{
				tempStr = "000" + tempStr;
				break;
			}
			case 2:
			{
				tempStr = "00" + tempStr;
				break;
			}
			case 3:
			{
				tempStr = "0" + tempStr;
				break;
			}
			case 4:
			{
				break;
			}
		}
		return tempStr;
	}
	/**
	 * 通过OID来读取数据 (snmpWalk方式)<br>
	 * 该方法必须被采集性能的表达式先调用<br>
	 * 需要: <code>datatype</code> <code>isFirst</code> <code>indexList</code>
	 *
	 * @param oid
	 *            MIB OID
	 * @param device_id
	 *            设备ID
	 * @param readCom
	 *            读口令(实际没有作用)
	 * @param needIndex
	 *            是否需要indexList
	 * @return HashMap实例
	 *         <li>key: oid采集到的实例索引
	 *         <li>value:索引对应的值
	 */
	protected Map<String, String> getDataByOID(String oid, String device_id,
			String readCom, boolean needIndex)
	{
		print("getDataByOID oid=" + oid + " & device_id=" + device_id + " & readCom="
				+ readCom);
		// 有序Map
		Map<String, String> map = new LinkedHashMap<String, String>();
		// datalist不为null
		Data[] datalist = SnmpGatherInterface.GetInstance().GetDataListPortFull(datatype ,
				device_id, readCom, oid);
		int len = datalist.length;
		print("*******datalist.length = "+len);
		log.warn("*******datalist.length = "+len);
		for (int i = 0; i < len; i++)
		{
			map.put(datalist[i].index, datalist[i].dataStr);
			log.warn("oid=" + datalist[i].index + ";value=" + datalist[i].dataStr);
			// 取的是第一个OID的值时,需要把索引加入到indexList中
			if (isFirst || needIndex)
				indexList.add(datalist[i].index);
		}
		isFirst = false;
		print("getDataByOID 采集到的数据Map = " + map);
		return map;
	}
	/**
	 * 根据OID获得对应关系。 add by zs(3704) JSDX_JS-REQ-20080805-SJ-001
	 *
	 * @param oid
	 *            MIB OID
	 * @param device_id
	 *            设备ID
	 * @param readCom
	 *            读口令(实际没有作用)
	 * @param needIndex
	 *            是否需要indexList
	 * @return HashMap实例
	 *         <li>key: oid采集到的实例索引
	 *         <li>value:索引对应的值的List
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, List<String>> getRelDataByOID(String oid, String device_id,
			String readCom, boolean needIndex)
	{
		// 有序Map
		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		// datalist不为null
		log.debug("根据OID获得对应关系:oid=" + oid);
		Data[] datalist = SnmpGatherInterface.GetInstance().GetDataListPortFull(datatype,
				device_id, readCom, oid);
		int len = datalist.length;
		List dataIndexList = new ArrayList();
		List dataStrList = new ArrayList();
		for (int i = 0; i < len; i++)
		{
			if (dataIndexList.contains(datalist[i].index))
			{
				dataStrList = map.get(datalist[i].index);
				dataStrList.add(datalist[i].dataStr);
				map.remove(datalist[i].index);
				map.put(datalist[i].index, dataStrList);
			}
			else
			{
				dataIndexList.add(datalist[i].index);
				dataStrList.add(datalist[i].dataStr);
				map.put(datalist[i].index, dataStrList);
			}
		}
		return map;
	}
	/**
	 * 判断 <code>id</code> 是否存在
	 *
	 * @param id
	 * @return
	 */
	protected boolean isIdExist(String id)
	{
		if (id != null && id.trim().length() > 0 && !id.equals("null")
				&& !id.equals("NULL"))
			return true;
		else
			return false;
	}
	/**
	 * 获取id对应的第一个OID
	 *
	 * @param id
	 *            表达式ID
	 * @return
	 */
	protected String getOnlyOidById(String id)
	{
		if (id == null || "".equals(id.trim()))
			return null;
		String oid = null;
		List<String> oidList=dao.getOIDList(id);
		if(!oidList.isEmpty())
		{
			oid=oidList.get(0);
		}
		return oid;
	}


	/**
	 * 直接通过索引信息生成描述信息,描述的格式为: slot(索引-1)<br>
	 * 需要: <code>resultMap</code>
	 */
	protected void createDescByIndex(List<String> indexList)
	{
		print("直接将描述信息入库为 slot(索引-1)");
		resultMap = new HashMap<String, String>();
		for (String index : indexList)
		{
			resultMap.put(index, "slot" + (Integer.parseInt(index) - 1));
		}
	}
	/**
	 * 通过性能表达式的ID来读取数据 (snmpWalk方式)<br>
	 * 读取性能信息,放在 <code>dataMap</code> 中<br>
	 * 需要: <code>dataList</code> <code>dataMap</code>
	 *
	 * @param expressionid
	 *            表达式ID
	 * @param device_id
	 *            设备ID
	 * @param readCom
	 *            读口令(实际没有作用)
	 * @return 读取数据结果的标志位
	 *         <li> 1: 成功
	 *         <li> -1: 表达式没有配置
	 *         <li>-21: 表示几个性能表达式oid中其中有一个oid采集不到数据
	 *         <li> -4: 几个性能oid采集到的索引数不一致
	 *         <li> -6: 性能表达式ID超过了999
	 */
	protected int getPerfData(final String expressionId, final String device_id,
			final String readCom)
	{
		print("getPerfData expressionid = " + expressionId + " & device_id = "
				+ device_id + " & readCom = " + readCom);
		log.warn("getPerfData expressionid = " + expressionId + " & device_id = "
				+ device_id + " & readCom = " + readCom);
		int flag = 1;
		if (expressionId == null || "".equals(expressionId.trim()))
			return -1;
		if (expressionId.length() > 5)
		{
			flag = -6;
			return flag;
		}
		Map<String, String> tempMap = null;

		// 采集性能信息前,先将isFirst置为true
		isFirst = true;
		List<String> oidList =dao.getOIDList(expressionId);
		for(int i=0;i<oidList.size();i++)
		{
			String oid = oidList.get(i);
			// 将性能OID放入dataList中
			dataList.add(oid);
			// 通过性能OID读取数据
			tempMap = getDataByOID(oid, device_id, readCom, false);
			dataMap.put(oid, tempMap);
		}
		print("getPerfData dataMap = " + dataMap);
		/*
		 * 对各OID采集到的数据进行验证 1.数据长度验证 2.各个OID采到的索引是否一致
		 */
		// 第一个实例的索引的迭代器
		Iterator<String> instatceKeyIter = null;
		// 性能表达式对应的OID,可能对应多个
		String oid = null;
		// 用来保存第一个实例Map的大小,初始化是-1
		int len = -1;
		// 实例Map
		Map<String, String> instanceMap = null;
		// 每个OID采集到的数据的迭代器
		Iterator<String> dataMapKeyIter = dataMap.keySet().iterator();
		while (dataMapKeyIter.hasNext())
		{
			oid = (String) dataMapKeyIter.next();
			instanceMap = dataMap.get(oid);
			if (len == -1)
			{
				/*
				 * 第一次进入while循环,将第一个OID采集到的实例的大小放入len中
				 * 以后每个OID采集到的实例的大小都和先这个len进行比较
				 */
				len = instanceMap.size();
				if (len == 0)
				{
					// 表示几个性能表达式oid中其中有一个oid采集不到数据
					flag = -21;
					return flag;
				}
				/*
				 * 将第一个OID采集到的实例信息放入到tempMap中 以后每个OID采集到的实例索引都和tempMap的实例索引相比较
				 */
				if (tempMap == null)
					tempMap = new HashMap<String, String>();
				tempMap = instanceMap;
				instatceKeyIter = tempMap.keySet().iterator();
			}
			else
			{
				if (len == instanceMap.size())
				{
					// 不同的性能表达式OID采集到的实例大小相同
					while (instatceKeyIter.hasNext())
					{
						// 判断不同OID采集到的索引是否对应
						// 遍历第一个性能表达式OID采集到的索引
						String indexStr = (String) instatceKeyIter.next();
						if (instanceMap.containsKey(indexStr) == false)
						{
							// 后面的性能表达式OID采集到的索引与第一个性能表达式OID采集到的索引不匹配
							flag = -4;
							return flag;
						}
					}
				}
				else
				{
					// 表示几个性能oid采集到的索引数不一致
					flag = -4;
					return flag;
				}
			}
		}
		return flag;
	}
	/**
	 * 判断是否需要采集描述信息,<br>
	 * 还是直接把slot(索引-1)作为描述
	 *
	 * @param device_id
	 *            设备ID
	 * @param expressionId
	 *            表达式ID
	 *
	 * @return
	 */
	protected boolean notNeedDesc(String device_id, String expressionId)
	{
		// 判断是否是分域 0:普通性能表达式 1:分域的性能表达式
		int class3 = 0;
		// 是否需要采集描述信息,默认是需要采集的
		boolean notNeedDesc = false;
		// 从数据库获取class3信息
		String[] infoArr = dao.getBasicInfoByExpressionId(expressionId);
		if (infoArr != null)
		{
			class3 = Integer.parseInt(infoArr[2]);
		}
		print("class3 = " + class3);
		// 分域的性能表达式是需要采集描述信息的,
		// 对于普通性能表达式再进行设备类型判断是否需要采集
		if (class3 == 0)
		{
			notNeedDesc = dao.isDeviceModelNotNeedDesc(device_id);
		}
		print("notNeedDesc = " + notNeedDesc);
		return notNeedDesc;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.linkage.liposs.webtopo.common.pefdef.BasePefDefInterfance#createSqlList()
	 */
	public List<String> createSqlList()
	{
		/*
		 * 需要 returnFlag,indexList,resultMap
		 */
		List<String> sqlList = new ArrayList<String>();
		String beforeSql="insert into pm_map_instance(collect,intodb,mintype,"
			+ "mindesc,minthres,mincount,minwarninglevel,minreinstatelevel,maxtype,maxdesc,"
			+ "maxcount,maxthres,maxwarninglevel,maxreinstatelevel,dynatype,dynadesc,"
			+ "dynacount,beforeday,dynathres,dynawarninglevel,dynareinstatelevel,mutationtype,"
			+ "mutationdesc,mutationcount,mutationthres,mutationwarninglevel,mutationreinstatelevel,id,device_id,expressionid,indexid,descr,remark1)  values(";
		StringBuffer mysql = new StringBuffer();
		if (returnFlag == 1)
		{
			// 采集成功
			String deviceIdCode = getIDCode(device_id);
			String expressionIdCode = getNum(Integer.parseInt(expressionId));
			String indexStr = null;
			@SuppressWarnings("unused")
			String descStr = null;
			String tempDescStr = null;
			@SuppressWarnings("unused")
			String index = null;// PM_instance中devMap索引
			// key为device_id-expressionid-indexid
			@SuppressWarnings("unused")
			Map map = null;// 用于存放PM_instance 中的devMap
			int len = indexList.size();
			print("indexList = " + indexList + " & size=" + len);
			for (int i = 0; i < len; i++)
			{
				indexStr = (String) indexList.get(i);
				// resultMap != null 说明采集了描述信息
				// resultMap == null 说明没有采集描述信息,直接将索引作为描述
				if (resultMap != null)
					tempDescStr = resultMap.get(indexStr);
				else
					tempDescStr = indexStr;
				descStr = (tempDescStr).replaceAll("'", "''");
				mysql.append(beforeSql).append(param.getCollect()).append(",").append(param.getIntodb()).append(",");

				/**
				 * sql中告警参数的拼凑，保留原有的告警配置
				 */
				if(instanceConfigParam.containsKey(indexStr))
				{
					Pm_Map_Instance instanceConfigTemp=instanceConfigParam.get(indexStr);
					mysql.append(getAlarmSQL(instanceConfigTemp));
				}
				else
				{
					mysql.append(getAlarmSQL(param));
				}

				mysql.append(",'").append(deviceIdCode+expressionIdCode+getNum(i + 1));
				mysql.append("','").append(device_id);
				mysql.append("',").append(expressionId);
				mysql.append(",'").append(indexStr);
				mysql.append("','").append(tempDescStr);
				mysql.append("','").append(param.getRemark1());
				mysql.append("')");

				PrepareSQL psql = new PrepareSQL(mysql.toString());
				psql.getSQL();
				sqlList.add(mysql.toString());
				mysql.delete(0, mysql.length());
			}

			// 判断是否需要增加总体统计的性能实例
			if(dao.isPopulationState(expressionId))
			{
				indexStr="-1";
				tempDescStr="总体统计";
				mysql.delete(0, mysql.length());

                mysql.append(beforeSql).append(param.getCollect()).append(",").append(param.getIntodb()).append(",");

				/**
				 * sql中告警参数的拼凑
				 */
				if(instanceConfigParam.containsKey(indexStr))
				{
					Pm_Map_Instance instanceConfigTemp=instanceConfigParam.get(indexStr);
					mysql.append(getAlarmSQL(instanceConfigTemp));
				}
				else
				{
					mysql.append(getAlarmSQL(param));
				}

				mysql.append(",'").append(deviceIdCode+expressionIdCode+"0000");
				mysql.append("','").append(device_id);
				mysql.append("',").append(expressionId);
				mysql.append(",'").append(indexStr);
				mysql.append("','").append(tempDescStr);
				mysql.append("','").append(param.getRemark1());
				mysql.append("')");
				PrepareSQL psql = new PrepareSQL(mysql.toString());
				sqlList.add(psql.getSQL());

			}
		}

		return sqlList;
	}

	/**
	 * 拼装告警参数sql
	 * @param instance
	 * @return
	 */
	private String getAlarmSQL(Pm_Map_Instance instance)
	{
		StringBuffer sb = new StringBuffer();

		/**
		 * 固定阈值
		 */
		sb.append(instance.getMintype()).append(",'");
		sb.append(instance.getMindesc()).append("',");
		sb.append(instance.getMinthres()).append(",");
		sb.append(instance.getMincount()).append(",");
		sb.append(instance.getMinwarninglevel()).append(",");
		sb.append(instance.getMinreinstatelevel()).append(",");

		sb.append(instance.getMaxtype()).append(",'");
		sb.append(instance.getMaxdesc()).append("',");
		sb.append(instance.getMaxcount()).append(",");
		sb.append(instance.getMaxthres()).append(",");
		sb.append(instance.getMaxwarninglevel()).append(",");
		sb.append(instance.getMaxreinstateleve()).append(",");

		/**
		 * 动态阈值
		 */
		sb.append(instance.getDynatype()).append(",'");
		sb.append(instance.getDynadesc()).append("',");
		sb.append(instance.getDynacount()).append(",");
		sb.append(instance.getBeforeday()).append(",");
		sb.append(instance.getDynathres()).append(",");
		sb.append(instance.getDynawarninglevel()).append(",");
		sb.append(instance.getDynareinstatelevel()).append(",");

		/**
		 * 突变阈值
		 */
		sb.append(instance.getMutationtype()).append(",'");
		sb.append(instance.getMutationdesc()).append("',");
		sb.append(instance.getMutationcount()).append(",");
		sb.append(instance.getMutationthres()).append(",");
		sb.append(instance.getMutationwarninglevel()).append(",");
		sb.append(instance.getMutationreinstatelevel());

		return sb.toString();
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.linkage.liposs.webtopo.common.pefdef.BasePefDefInterfance#setBaseInfo(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	public void setBaseInfo(String device_id, String expressionId, String departAreaId,
			String middleId, String descId, String domainId, String domainVRId,
			String domainIpPoolId, I_configPmeeDao dao_,
			Map<String, Pm_Map_Instance> instanceConfigParam_,Pm_Map_Instance param_, List sqlList)
	{
		this.device_id = device_id;
		this.expressionId = expressionId;
		this.departAreaId = departAreaId;
		this.middleId = middleId;
		this.descId = descId;
		this.domainId = domainId;
		this.domainVRId = domainVRId;
		this.domainIpPoolId = domainIpPoolId;
		this.dao=dao_;
		this.instanceConfigParam=instanceConfigParam_;
		this.param=param_;
		this.sqlList = sqlList;
	}
}
