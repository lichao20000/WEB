
package com.linkage.module.gwms.resource.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-9-21 下午02:28:32
 * @category com.linkage.module.gwms.resource.dao
 * @copyright 南京联创科技 网管科技部
 */
public class ImportLoidDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(ImportLoidDAO.class);
	
	public List<Map<String,String>> getResult(String pcity_id, List<String> loidList)
	{
		if(loidList.size() == 0){
			return null;
		}
		
		List list = CityDAO.getAllNextCityIdsByCityPid(pcity_id);
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_id_ex,device_id,oui,vendor_id,device_model_id ");
		psql.append("from tab_gw_device where device_id_ex in (");
		for (int i = 0; i < loidList.size(); i++)
		{  
			psql.append("'"+loidList.get(i));
			if (i == (loidList.size() - 1)){
				psql.append("') ");
			}else{
				psql.append("',");
			}
		}
		psql.append(" and city_id in ("+StringUtils.weave(list)+")");
		
		return jt.queryForList(psql.getSQL());
	}
	
	
	public List<Map<String,String>> getResult4CQ(String pcity_id, List<String> loidList)
	{
		if(loidList.size() == 0){
			return null;
		}
		
		List list = CityDAO.getAllNextCityIdsByCityPid(pcity_id);
		PrepareSQL psql = new PrepareSQL();
		psql.append("select d.device_id_ex,d.device_id,d.oui,d.vendor_id,d.device_model_id " +
				"from tab_gw_device d,tab_hgwcustomer a");
		
		psql.append(" where d.device_id = a.device_id and a.username in (");
		for (int i = 0; i < loidList.size(); i++)
		{  
			psql.append("'"+loidList.get(i));
			if (i == (loidList.size() - 1)){
				psql.append("') ");
			}else{
				psql.append("',");
			}
		}
		psql.append(" and d.city_id in ("+StringUtils.weave(list)+")");
		
		return jt.queryForList(psql.getSQL());
	}
	
	
	public List<Map<String,String>> getDigitMap(String pcity_id,List<Map<String,String>> loidList)
	{
		StringBuffer buffrSQL = new StringBuffer();
		buffrSQL.append("select map_id,map_name from gw_voip_digit_map ");
		buffrSQL.append(" where vendor_id ='"+StringUtil.getStringValue(loidList.get(0).get("vendor_id"))  +"'");
		buffrSQL.append(" and device_model_id ='"+StringUtil.getStringValue(loidList.get(0).get("device_model_id"))+"'");
		buffrSQL.append(" and city_id in (");
		List list = CityDAO.getAllNextCityIdsByCityPid(pcity_id);
		buffrSQL.append(StringUtils.weave(list));
		list = null;
		buffrSQL.append(")");
		PrepareSQL psql = new PrepareSQL(buffrSQL.toString());
		return jt.queryForList(psql.getSQL());
		
	}
	
	public List getDeviceExceptionList(String pcity_id, List<String> loidList)
	{
		List rs = getResult(pcity_id, loidList);
		List deviceExceptionList = new ArrayList();
		for (int j = 0; j < rs.size(); j++)
		{
			Map tempUsername = (Map) rs.get(j);
			String device_id_ex = null;
			if (null != tempUsername.get("device_id_ex"))
			{
				device_id_ex = tempUsername.get("device_id_ex").toString();
			}
			String device_id = null;
			if (null != tempUsername.get("device_id"))
			{
				device_id = tempUsername.get("device_id").toString();
			}
			if (null == device_id || "".equals(device_id))
			{
				deviceExceptionList.add(device_id_ex);
			}
		}
		return deviceExceptionList;
	}
	
	public boolean forward(String map_id,String device_idNormalStr,String task_name) 
	{
		ArrayList<String> sqllist = new ArrayList<String>();
		logger.warn("dao——map_id为" + map_id);
		logger.warn("dao——device_idNormalStr为" + device_idNormalStr);
		logger.warn("dao——task_name为" + task_name);
	    long task_id=System.currentTimeMillis()/1000;
		String sql="insert into gw_voip_digit_task(task_id,task_name,task_type) values("+task_id+",'"+task_name+"',"+1+")";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		sqllist.add(sql);
	//	jt.update(sql);
		String update="update gw_voip_digit_device set enable ="+ -1+" where device_id in ("+device_idNormalStr+")";
		PrepareSQL updatePsql = new PrepareSQL(update);
		updatePsql.getSQL();
		jt.update(update);
		 String [] deviceIdList=device_idNormalStr.split(",");
		String[] sqlList = new String[device_idNormalStr.split(",").length];
		
		for(int i =0;i<deviceIdList.length;i++){
			long tasktime=System.currentTimeMillis()/1000;
			sqlList[i] = "insert into gw_voip_digit_device(device_id,task_id,tasktime,map_id,enable) values("+deviceIdList[i]+","+task_id+","+tasktime+","+Integer.parseInt(map_id)+","+1+")";
			psql = new PrepareSQL(sqlList[i]);
	    	psql.getSQL();
	    	sqllist.add(sqlList[i]);
		}
	// doConfigAll(device_idNormalStr, map_id);
	
	 sqllist.addAll( doConfigAll( device_idNormalStr, map_id));
	 logger.warn("sql集合的大小"+sqllist.size());
	// for(int i=0;i<sqllist.size();i++){
		// logger.warn("sql集合的内容"+sqllist.get(i));
		 //}
	 
	// 启用一个新的线程来做入库
		LipossGlobals.ALL_SQL_IPTV.addAll(sqllist);
		
		//result.append("1;配置下发成功！");
	//return result.toString();
		
		return true;
		
	}
	/**
	 * 设备批量下发数图配置
	 * 
	 * @param user_id
	 * @param deviceIds
	 * @param map_id
	 * @return
	 */
	public List<String> doConfigAll( String deviceIds, String map_id) 
	{
		List<String> sqlList_2=new ArrayList<String>();
		List<String> sqlList_3=new ArrayList<String>();
		StringBuffer result = new StringBuffer();
		// 入策略表
		String map_content = getDigitMapContentById(map_id);
		String strategyXmlParam = toXML(map_content);
		logger.debug("digitMap XML: " + strategyXmlParam);
		
		String [] deviceIdArray = deviceIds.split(",");
	
		for(int i = 0; i < deviceIdArray.length; i++)
		{
			/** 入策略表，调预读 */
			// 立即执行
			int strategyType = 0;  // strategyType=0表示立即执行
			// 配置的service_id
			int serviceId = 7;
			
			StrategyOBJ strategyObj = new StrategyOBJ();
			// 策略ID
			strategyObj.createId();
			// 策略配置时间
			strategyObj.setTime(TimeUtil.getCurrentTime());
			// 用户id
			//strategyObj.setAccOid(user_id);
			// 立即执行
			strategyObj.setType(strategyType);
			// 设备ID
			strategyObj.setDeviceId(deviceIdArray[i].substring(1, deviceIdArray[i].length()-1));
			logger.warn("设备ID"+deviceIdArray[i]);
			// QOS serviceId
			strategyObj.setServiceId(serviceId);
			// 顺序,默认1
			strategyObj.setOrderId(1);
			// 工单类型: 新工单,工单参数为xml串的工单
			strategyObj.setSheetType(2);
			// 参数
			strategyObj.setSheetPara(strategyXmlParam);
			strategyObj.setTempId(serviceId);
			strategyObj.setIsLastOne(1);
			
		 sqlList_2 = strategySQL(strategyObj);
		 sqlList_3.addAll(sqlList_2);
		 	
		}
		
		return sqlList_3;
	}

	/**
	 * 根据map_id查询map_content
	 * @param map_id
	 * @return
	 */
	public String getDigitMapContentById(String map_id) 
	{
		String sql = "select map_content from gw_voip_digit_map where map_id=?";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setInt(1, Integer.parseInt(map_id));
		Map map = queryForMap(psql.getSQL());
		return StringUtil.getStringValue(map.get("map_content"));
	}
	
	private String toXML(String map_content)
	{
		String strXml = null;
		Document doc = DocumentHelper.createDocument();
		// root node: X_CT-COM_UplinkQoS
		Element root = doc.addElement("VOIPDigitMap");
		Element digitMap = root.addElement("DigitMap");
		digitMap.addText(map_content);
		Element enable = root.addElement("Enable");
		enable.addText("");
		strXml = doc.asXML();
		return strXml;
	}
	
	
	
	
}
