package com.linkage.module.liposs.performance.bio.pefdef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.liposs.performance.bio.Pm_Map_Instance;
import com.linkage.module.liposs.performance.dao.I_configPmeeDao;

/**
 * PPPOE的分域地址池性能配置
 *
 * @author zhangsong(3704) 2008-9-3
 *
 */
public class PerDefByDomainAndDescId extends AbstractDepartAreaPefDefImp {
	List domainListForSql = new ArrayList();
	/**
	 * 配置参数
	 */
	private Pm_Map_Instance param = null;
	private static Logger log = LoggerFactory.getLogger(PerDefByDomainAndDescId.class);

	protected void getPerfAndDescDataByExpression() {
		getDomainByDomainAndDescId(expressionId, device_id, readCom);
	}

	/**
	 * 通过domain表达式、VR性能表达式、描述表达式获取描述信息<br>
	 * 最终结果放在<code>resultMap</code>和<code>indexList</code>中
	 *
	 * @param expressionId
	 *            VR性能表达式
	 * @param device_id
	 *            设备ID
	 * @param readCom
	 *            读口令
	 */
	private void getDomainByDomainAndDescId(String expressionId,
			String device_id, String readCom) {
		// 描述OID构成的Map
		Map descOidMap = new HashMap();
		// domainOID构成的Map
		Map domainOidMap = new HashMap();
		// domainVROID构成的Map
		Map domainVROidMap = new HashMap();
		// domainIpPoolOID构成的Map
		Map domainIpPoolOidMap = new HashMap();
		// 默认的描述OID
		String defaultDescOid = initDescOidMap(descOidMap);
		Map instanceMap = new HashMap();
		// 存放能采集到性能实例的VR，该vr就表示为合法的VR
		List VRList = new ArrayList();
		// 存放能够采集到性能的描述索引，用于与VR建立对应关系
		List hf_VRdescList = new ArrayList();
		// 存放VR下能够采集到性能的描述（也就是ip_pool）
		Map hf_VRDescMap = new HashMap();
		// 存放VR下面所有的描述
		Map vrDescMap = new HashMap();
		Map allDescInVRMap = new HashMap();
		// ------------获取Domain-----------
		Map domainMap = new HashMap();
		String defaultDomainOid = initDomainOidMap(domainOidMap);
		domainMap = getDataByOID(defaultDomainOid, device_id, readCom, false);
		// 存在domain信息则进行下一步采集
		if (!domainMap.isEmpty()) {
			// 存放VR数据的Map
			instanceMap = dataMap.get(dataList.get(0));
			List<String> copyIndexList = new ArrayList<String>();
			copyIndexList.addAll(indexList);
			// 最终索引列表,最后还是要赋值给indexList--此处应该是domain的索引对应domain的描述信息
			List<String> resultList = new ArrayList<String>();
			// 每次分域采集的标志位
			int flag = 1;
			// 默认采集失败
			returnFlag = -4;
			resultMap = new HashMap<String, String>();
			// ------------开始按分域采集性能 Begin---------
			// 分域描述Map
			Map departDescMap = null;
			int copyIndexListSize = copyIndexList.size();
			List areaList = new ArrayList();
			for (int i = 0; i < copyIndexListSize; i++) {
				String indexStr = (String) copyIndexList.get(i);
				String areaStr = (String) instanceMap.get(indexStr);
				if (!areaList.contains(areaStr) && !"".equals(areaStr.trim())) {
					areaList.add(areaStr);
				}
			}
			for (Iterator areaIt = areaList.iterator(); areaIt.hasNext();) {
				// 清空资源,准备去采集性能
				indexList.clear();
				dataList.clear();
				dataMap.clear();
				vrDescMap = new HashMap<String, String>();
				hf_VRdescList = new ArrayList<String>();
				// // 域信息
				String areaStr = (String) areaIt.next();
				readCom = "@" + areaStr;
				// 采集性能信息
				// 使用的是indexList
				flag = getPerfData(expressionId, device_id, readCom);
				// 采集成功再采描述,不然直接进行下次循环.
				// 采集成功，则记录下该VR为合法的VR
				if (flag != 1) {
					continue;
				}
				VRList.add(areaStr);// 存放合法的VR
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
				for (int j = 0; j < size; j++) {
					// 具体的每个实例的索引
					String instanceIndex = indexList.get(j);
					// 具体分域的描述
					String instanceDesc = (String) departDescMap
							.get(instanceIndex);
					if (instanceDesc == null) {
						instanceDesc = instanceIndex;
					} else {
						// 存放合法的索引
						hf_VRdescList.add(instanceIndex);
					}
					// 存放一个VR下面所有的描述索引
					vrDescMap.put(instanceIndex, instanceDesc);
				}
				allDescInVRMap.put(areaStr, vrDescMap);// VR下面对应的描述信息
				hf_VRDescMap.put(areaStr, hf_VRdescList);// 存放VR下面对应的能采集到性能的ip_pool
			}
			log.debug("合法的VR：" + VRList);
			log.debug("所有的vr对应的desc：" + allDescInVRMap);
			log.debug("合法的vr对应的desc：" + hf_VRDescMap);
			// ------------按分域采集性能 End---------
			// ------------获取Domain与VR的对应关系-----------
			Map domainVRMap = null;
			readCom = null;
			String defaultDomainVROid = initDomainVROidMap(domainVROidMap);
			domainVRMap = getRelDataByOID(defaultDomainVROid, device_id,
					readCom, false);
			log.debug("获得的domain和vr的关系 domainVRMap=" + domainVRMap);
			// ------------获取Domain与ip_pool的对应关系
			Map domainIpPoolMap = null;
			String defaultDomainIpPoolOid = initDomainIpPoolOidMap(domainIpPoolMap);
			domainIpPoolMap = getRelDataByOID(defaultDomainIpPoolOid,
					device_id, readCom, false);
			boolean hasIpPool = false;// 是否有合法的ip_pool
			// 循环判断domain与ip_pool的对应关系中ip_pool是否合法
			for (Object key : domainMap.keySet()) {
				hasIpPool = false;
				String domainIndex = key.toString();// domain的索引
				String domainDes = (String) domainMap.get(key);// domain的描述
				// 根据domain与ip_pool的对应关系找合法的ip_pool
				// 先对应到VR
				// 判断是否是合法的VR
				// 判断ip_pool是否是合法
				// 生成insert语句
				if (domainIpPoolMap != null && !domainIpPoolMap.isEmpty()) {// 如果有关联的ip_pool
					List domainDescList = (List) domainIpPoolMap
							.get(domainIndex);// 如果有关联的ip_pool
					log.debug("domain " + domainDes + "对应的ip_pool："
							+ domainDescList);
					if (domainVRMap != null && !domainVRMap.isEmpty()) {
						List DomainVRList = (List) domainVRMap.get(domainIndex);// domain关联的VR
						// VR排重
						if(DomainVRList!=null){
						List pc_DomainVRList = new ArrayList();
						log.debug("DomainVRList=" + DomainVRList);
						for (Iterator VRit = DomainVRList.iterator(); VRit
								.hasNext();) {
							String trueVR = "";
							String VR = (String) VRit.next();
							String[] str = VR.split(":");// 判断mpls-vpn:vpn-sqxxh这种
							if (str != null && str.length > 1) {
								trueVR = str[1];
							} else {
								trueVR = VR;
							}
							if (!pc_DomainVRList.contains(trueVR)) {
								pc_DomainVRList.add(trueVR);
							}
						}
						for (Iterator pc_VRit = pc_DomainVRList.iterator(); pc_VRit
								.hasNext();) {
							String VR = (String) pc_VRit.next();
							if (VRList.contains(VR)) {// 是否是合法的VR
								Map allvrDescMap = (Map) allDescInVRMap.get(VR);// 当前VR所有的描述
								List hf_vrDescIndex = (List) hf_VRDescMap
										.get(VR);// 当前VR下所有合法的描述
								for (Iterator it = hf_vrDescIndex.iterator(); it
										.hasNext();) {
									String hf_descIndex = (String) it.next();
									String hf_desc = (String) allvrDescMap
											.get(hf_descIndex);
									if (domainDescList.contains(hf_desc)) {
										String[] domain_Instance = new String[3];
										// 存入pm_domain_instance表中
										// //expressionId domainId device_id
										// hf_descIndex areaVR
										domain_Instance[0] = domainIndex;
										domain_Instance[1] = hf_descIndex;
										domain_Instance[2] = VR;
										// synchronized (sqlList)
										// {
										// sqlList.add("insert into
										// pm_domain_instance(device_id,expressionid
										// ,domain_index,instance_index,vr)values('"+device_id+"',"+expressionId+",'"+domainIndex+"','"+hf_descIndex+"','"+VR+"')");
										// }
										domainListForSql.add(domain_Instance);
										hasIpPool = true;
									}
								}
								if (!hasIpPool) {
									// //关联VR下面所有的
									for (Object descKey : allvrDescMap.keySet()) {
										String descIndex = descKey.toString();
										String desc = (String) allvrDescMap
												.get(descKey);
										String[] domain_Instance = new String[3];
										domain_Instance[0] = domainIndex;
										domain_Instance[1] = descIndex;
										domain_Instance[2] = VR;
										domainListForSql.add(domain_Instance);
										// 存入pm_domain_instance表中
										// expressionId domainId device_id
										// hf_descIndex areaVR
										// synchronized (sqlList)
										// {
										// sqlList.add("insert into
										// pm_domain_instance(device_id,expressionid
										// ,domain_index,instance_index,vr)values('"+device_id+"',"+expressionId+",'"+domainIndex+"','"+descIndex+"','"+VR+"')");
										// }
									}
								}
							}
						}
					}
					}
				}

				List DomainVRList = (List) domainVRMap.get(domainIndex);// domain关联的VR
				if(DomainVRList!=null){
					// 域索引
					resultList.add(domainIndex);
					// 域描述
					resultMap.put(domainIndex, domainDes);
				}

			}
			indexList.clear();
			// indexList 中存放的就是最终的索引列表
			indexList = resultList;
		}
	}

	/**
	 * 初始化descOidMap
	 *
	 * @param descOidMap
	 *            默认的描述OID
	 * @return
	 */
	private String initDescOidMap(Map descOidMap) {
		// 默认的描述OID
		String defaultDescOid = null;
		if (descOidMap == null)
			descOidMap = new HashMap<String, String>();
		else
			descOidMap.clear();
		String mysql = getOidAndFlagByExpressionIdSQL + descId;// 从pm_expression_context表中获得
		PrepareSQL psql = new PrepareSQL(mysql);
        psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(mysql);
		Map<String, String> fields = cursor.getNext();
		while (null != fields) {
			String oid = (String) fields.get("oid");
			int flag = Integer.parseInt((String) fields.get("flag"));
			String value = (String) fields.get("remark1");
			if (flag == 1) {
				defaultDescOid = oid;
			} else
				descOidMap.put(value, oid);
			fields = cursor.getNext();
		}
		log.debug("defaultDescOid = " + defaultDescOid + " & descOidMap = "
				+ descOidMap);
		return defaultDescOid;
	}

	/**
	 *
	 * 初始化domainOidMap
	 *
	 * @param domainOidMap
	 *
	 * @return 默认的Domain OID
	 */
	private String initDomainOidMap(Map domainOidMap) {
		// 默认的描述OID
		String defaultDomainOid = null;
		if (domainOidMap == null)
			domainOidMap = new HashMap<String, String>();
		else
			domainOidMap.clear();
		String mysql = getOidAndFlagByExpressionIdSQL + domainId;// 从pm_expression_context表中获得
		PrepareSQL psql = new PrepareSQL(mysql);
        psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(mysql);
		Map<String, String> fields = cursor.getNext();
		while (null != fields) {
			String oid = (String) fields.get("oid");
			int flag = Integer.parseInt((String) fields.get("flag"));
			String value = (String) fields.get("remark1");
			if (flag == 1) {
				defaultDomainOid = oid;
			} else
				domainOidMap.put(value, oid);
			fields = cursor.getNext();
		}
		log.debug("defaultDescOid = " + defaultDomainOid + " & descOidMap = "
				+ domainOidMap);
		return defaultDomainOid;
	}

	/**
	 *
	 * 初始化domainVROidMap
	 *
	 * @param domainVROidMap
	 *
	 * @return 默认的DomainVR OID
	 */
	private String initDomainVROidMap(Map domainVROidMap) {
		// 默认的描述OID
		String defaultDomainVROid = null;
		if (domainVROidMap == null)
			domainVROidMap = new HashMap<String, String>();
		else
			domainVROidMap.clear();
		String mysql = getOidAndFlagByExpressionIdSQL + domainVRId;// 从pm_expression_context表中获得
		PrepareSQL psql = new PrepareSQL(mysql);
        psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(mysql);
		Map<String, String> fields = cursor.getNext();
		while (null != fields) {
			String oid = (String) fields.get("oid");
			int flag = Integer.parseInt((String) fields.get("flag"));
			String value = (String) fields.get("remark1");
			if (flag == 1) {
				defaultDomainVROid = oid;
			} else
				domainVROidMap.put(value, oid);
			fields = cursor.getNext();
		}
		log.debug("defaultDescOid = " + defaultDomainVROid + " & descOidMap = "
				+ domainVROidMap);
		return defaultDomainVROid;
	}

	/**
	 *
	 * 初始化domainIpPoolOidMap
	 *
	 * @param domainIpPoolOidMap
	 *
	 * @return 默认的DomainIpPool OID
	 */
	private String initDomainIpPoolOidMap(Map domainIpPoolOidMap) {
		// 默认的描述OID
		String defaultDomainIpPoolOid = null;
		if (domainIpPoolOidMap == null)
			domainIpPoolOidMap = new HashMap<String, String>();
		else
			domainIpPoolOidMap.clear();
		String mysql = getOidAndFlagByExpressionIdSQL + domainIpPoolId;// 从pm_expression_context表中获得
		PrepareSQL psql = new PrepareSQL(mysql);
        psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(mysql);
		Map<String, String> fields = cursor.getNext();
		while (null != fields) {
			String oid = (String) fields.get("oid");
			int flag = Integer.parseInt((String) fields.get("flag"));
			String value = (String) fields.get("remark1");
			if (flag == 1) {
				defaultDomainIpPoolOid = oid;
			} else
				domainIpPoolOidMap.put(value, oid);
			fields = cursor.getNext();
		}
		log.debug("defaultDescOid = " + defaultDomainIpPoolOid
				+ " & descOidMap = " + domainIpPoolOidMap);
		return defaultDomainIpPoolOid;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.linkage.liposs.webtopo.common.pefdef.BasePefDefInterfance#createSqlList()
	 */
	public List<String> createSqlList() {
		String beforeSql = "insert into pm_map_instance(collect,intodb,mintype,"
				+ "mindesc,minthres,mincount,minwarninglevel,minreinstatelevel,maxtype,maxdesc,"
				+ "maxcount,maxthres,maxwarninglevel,maxreinstatelevel,dynatype,dynadesc,"
				+ "dynacount,beforeday,dynathres,dynawarninglevel,dynareinstatelevel,mutationtype,"
				+ "mutationdesc,mutationcount,mutationthres,mutationwarninglevel,mutationreinstatelevel,id,device_id,expressionid,indexid,descr,remark1)  values(";
		/*
		 * 需要 returnFlag,indexList,resultMap
		 */
		log.debug("利用重写的createSql方法: domainListForSql.size()="
				+ domainListForSql.size());
		if (!domainListForSql.isEmpty()) {
			for (Iterator it = domainListForSql.iterator(); it.hasNext();) {
				String[] str = (String[]) it.next();
				if (str.length == 3) {
					String domainIndex = str[0];
					String descIndex = str[1];
					String VR = str[2];
					// 存入pm_domain_instance表中
					// expressionId domainId device_id hf_descIndex areaVR
					synchronized (sqlList) {
						PrepareSQL psql = new PrepareSQL("insert into pm_domain_instance(device_id,expressionid ,domain_index,instance_index,vr)values('"
								+ device_id
								+ "',"
								+ expressionId
								+ ",'"
								+ domainIndex
								+ "','"
								+ descIndex + "','" + VR + "')");
						sqlList.add(psql.getSQL());
					}
				}
			}
		}
		StringBuffer mysql = new StringBuffer();
		if (returnFlag == 1) {
			// 采集成功
			String deviceIdCode = getIDCode(device_id);
			String expressionIdCode = getNum(Integer.parseInt(expressionId));
			String indexStr = null;
			String descStr = null;
			String tempDescStr = null;
			String index = null;// PM_instance中devMap索引
			// key为device_id-expressionid-indexid
			Map map = null;// 用于存放PM_instance 中的devMap
			int len = indexList.size();
			print("indexList = " + indexList + " & size=" + len);
			for (int i = 0; i < len; i++) {
				indexStr = (String) indexList.get(i);
				// resultMap != null 说明采集了描述信息
				// resultMap == null 说明没有采集描述信息,直接将索引作为描述
				if (resultMap != null)
					tempDescStr = resultMap.get(indexStr);
				else
					tempDescStr = indexStr;
				descStr = (tempDescStr).replaceAll("'", "''");
				mysql.append(beforeSql).append(param.getCollect()).append(",")
						.append(param.getIntodb()).append(",");

				/**
				 * sql中告警参数的拼凑，保留原有的告警配置
				 */
				if (instanceConfigParam.containsKey(indexStr)) {
					Pm_Map_Instance instanceConfigTemp = instanceConfigParam
							.get(indexStr);
					mysql.append(getAlarmSQL(instanceConfigTemp));
				} else {
					mysql.append(getAlarmSQL(param));
				}
				mysql.append(",'").append(
						deviceIdCode + expressionIdCode + getNum(i + 1));
				mysql.append("','").append(device_id);
				mysql.append("',").append(expressionId);
				mysql.append(",'").append(indexStr);
				mysql.append("','").append(tempDescStr);
				mysql.append("','").append(param.getRemark1());
				mysql.append("')");
				print("性能配置成功 SQL = " + mysql);
				PrepareSQL psql = new PrepareSQL(mysql.toString());
		        psql.getSQL();
				sqlList.add(mysql.toString());
				mysql.delete(0, mysql.length());
			}

			// 判断是否需要增加总体统计的性能实例
			if (dao.isPopulationState(expressionId)) {
				indexStr = "-1";
				tempDescStr = "总体统计";
				mysql.delete(0, mysql.length());

				mysql.append(beforeSql).append(param.getCollect()).append(",")
						.append(param.getIntodb()).append(",");

				/**
				 * sql中告警参数的拼凑
				 */
				if (instanceConfigParam.containsKey(indexStr)) {
					Pm_Map_Instance instanceConfigTemp = instanceConfigParam
							.get(indexStr);
					mysql.append(getAlarmSQL(instanceConfigTemp));
				} else {
					mysql.append(getAlarmSQL(param));
				}

				mysql.append(",'").append(
						deviceIdCode + expressionIdCode + "0000");
				mysql.append("','").append(device_id);
				mysql.append("',").append(expressionId);
				mysql.append(",'").append(indexStr);
				mysql.append("','").append(tempDescStr);
				mysql.append("','").append(param.getRemark1());
				mysql.append("')");
				print("性能配置成功 SQL = " + mysql);
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
	private String getAlarmSQL(Pm_Map_Instance instance) {
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

	public void setBaseInfo(String device_id, String expressionId,
			String departAreaId, String middleId, String descId,
			String domainId, String domainVRId, String domainIpPoolId,
			I_configPmeeDao dao_,
			Map<String, Pm_Map_Instance> instanceConfigParam_,
			Pm_Map_Instance param_, List sqlList) {
		this.device_id = device_id;
		this.expressionId = expressionId;
		this.departAreaId = departAreaId;
		this.middleId = middleId;
		this.descId = descId;
		this.domainId = domainId;
		this.domainVRId = domainVRId;
		this.domainIpPoolId = domainIpPoolId;
		this.dao = dao_;
		this.instanceConfigParam = instanceConfigParam_;
		this.param = param_;
		this.sqlList = sqlList;
	}
}
