package com.linkage.module.liposs.performance.bio.pefdef;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.liposs.performance.bio.Pm_Map_Instance;
/**
 * SE800的分域地址池利用率配置
 * @author zhangsong
 * @version 1.0
 * @since 2008-10-27
 * @serial 联创科技
 *
 */
public class PefDefForSE extends AbstractDepartAreaPefDefImp
{
	/**
	 * 配置参数
	 */
	private Pm_Map_Instance param = null;
	private static Logger log = LoggerFactory.getLogger(PefDefForSE.class);
	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.bio.performance.pefdef.AbstractDepartAreaPefDefImp#getPerfAndDescDataByExpression()
	 */
	protected void getPerfAndDescDataByExpression() {
		log.debug("进入SE800的分域地址池利用率");
		getDateForSE(expressionId, device_id, readCom);
	}
	
	/**
	 * 通过VR性能表达式获取信息<br>
	 * 最终结果放在<code>resultMap</code>和<code>indexList</code>中
	 * 提供两个性能OID，一种性能OID是可以直接采集到各地址池的地址数，
	 * 还有一种OID需要结合地址池对应的唯一域来snmpWalk采集其地址池大小
	 * 
	 * @param expressionId
	 *            性能表达式ID
	 * @param device_id
	 *            设备ID
	 * @param readCom
	 *            读口令
	 */
	private void getDateForSE(String expressionId, String device_id,
			String readCom){
		indexList.clear();
		dataList.clear();
		dataMap.clear();
		resultMap = new HashMap();
		
		//存放nodetype in(2,3)的在线用户数表达式oid
		Map onLineOIDMap = new HashMap();
		//存放nodetype in(2,3)的分域表达式oid
		Map VRMapOIDMap = new HashMap();
		
		//在线用户数OID
		String defaultOnLineOID = initOidMap(onLineOIDMap,expressionId);
		//总pool数OID
		String defaultPoolOID = getPoolOID(expressionId);
		log.debug("总地址池OID defaultPoolOID="+defaultPoolOID);
		//分域OID
		String defaultVROID = initOidMap(VRMapOIDMap,departAreaId);
		
		//取出全部的context
		Map VRMap = getDataByOID(defaultVROID, device_id, readCom, false);
		//取出在线用户数
		Map onLineMap = getDataByOID(defaultOnLineOID, device_id, readCom,false);
		
		
		
		for (Object key : VRMap.keySet())
		{
			String VRIndex = key.toString();// 索引
			String VRDes = (String) VRMap.get(key);// 描述
			log.debug("VR对应的描述  "+VRIndex+":"+VRDes);
			String onLineIndex = null;
			String onLineNumber = null;
			for(Object onLinekey : onLineMap.keySet()){
				String index = onLinekey.toString();
				if(index.lastIndexOf("."+VRIndex)>0){
					onLineIndex = index;
					onLineNumber = (String)onLineMap.get(index);
				}
			}
			log.debug("VR对应的在线用户数   "+onLineIndex+":"+onLineNumber);
			//snmp_ro_community@VRDes作为读口令，去采集总的地址池数
			if(!"".equals(VRDes)&&onLineNumber!=null){
				
				Map poolMap =  getDataByOID(defaultPoolOID, device_id, "@"+VRDes,false);
				int len = poolMap.size();
				if(len==1){//只取有切只有一条记录的
					for (Object poolKey : poolMap.keySet())
					{
						String poolIndex = poolKey.toString();// 索引
						String poolNumber = (String) poolMap.get(poolIndex);// 描述
						if(poolNumber!=null&&!"0".equals(poolNumber)){//排除掉地址池数为零的
							log.debug("插入数据库的数据 "+onLineIndex+"@"+VRDes);
							indexList.add(onLineIndex+"@"+VRDes); 
							resultMap.put(onLineIndex+"@"+VRDes, onLineIndex+"@"+VRDes);
//							插入数据库
						}
					}
				}
			}
		}
		
	}
	
	/**
	 * 
	 * 初始化OIDMap
	 * 并获得表达式
	 * 
	 * @param OIDMap
	 * 
	 * @return 默认的 OID
	 */
	private String initOidMap(Map VRMapOIDMap,String expression_id)
	{
		// 默认的描述OID
		String defaultOid = null;
		if (VRMapOIDMap == null)
			VRMapOIDMap = new HashMap<String, String>();
		else
			VRMapOIDMap.clear();
		String mysql = getOidAndFlagByExpressionIdSQL + expression_id;// 从pm_expression_context表中获得
		PrepareSQL psql = new PrepareSQL(mysql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(mysql);
		Map<String, String> fields = cursor.getNext();
		while (null != fields)
		{
			String oid = (String) fields.get("oid");
			int flag = Integer.parseInt((String) fields.get("flag"));
			String value = (String) fields.get("remark1");
			if (flag == 1)
			{
				defaultOid = oid;
			}
			else
				VRMapOIDMap.put(value, oid);
			fields = cursor.getNext();
		}
		return defaultOid;
	}
	/**
	 * 获得se800特殊的oid nodetype=7
	 * @param expression_id
	 * 			表达式ID
	 * @return String 
	 */
	private String getPoolOID(String expression_id){
		
		String defaultOid = null;
		String sql = getSEOidByExpressionIdSQL+expression_id;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map<String, String> fields = cursor.getNext();
		//表中就一条记录
		while (null != fields)
		{
			String oid = (String) fields.get("oid");
			int flag = Integer.parseInt((String) fields.get("flag"));
			defaultOid = oid;
			fields = cursor.getNext();
		}
		return defaultOid;
	}

}
