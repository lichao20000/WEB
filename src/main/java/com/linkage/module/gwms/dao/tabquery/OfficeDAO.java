package com.linkage.module.gwms.dao.tabquery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileObj;
import com.linkage.module.gwms.util.StringUtil;


/**
 * 局向相关信息数据库操作类
 * 
 * @author Jason(3412)
 * @date 2009-9-23
 */
public class OfficeDAO{

	//日志记录
	private static Logger logger = LoggerFactory.getLogger(OfficeDAO.class);
	//Lock
	private static final Object LOCK = new Object();
	//单例
	public static OfficeDAO officeDao;
	//局向ID和名称的MAP
	private static Map<String,String> officeIdNameMap;
	//局向ID和服务器对象的MAP
	private static Map<String,VoiceServiceProfileObj> officeIdVoipMap;
	
	
	/**
	 * 私有构造方法
	 */
	private OfficeDAO(){
		
	}

	
	/**
	 * 构造单实例
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-23
	 * @return OfficeDAO
	 */
	public static OfficeDAO getInstance(){
		
		if(null == officeDao){
			synchronized (LOCK) {
				if(null == officeDao){
					officeDao = new OfficeDAO();
				}
			}
		}
		return officeDao;
	}
	
	/**
	 * 获取局向ID和局向名称的对应MAP
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-23
	 * @return Map<String,String>
	 */
	public Map<String,String> getOfficeIdNameMap(){
		logger.debug("getOfficeIdNameMap()");
		if(null == officeIdNameMap){
			String strSQL = "select office_id, office_name from tab_office";
			PrepareSQL psql = new PrepareSQL(strSQL);
	    	psql.getSQL();
			officeIdNameMap = DataSetBean.getMap(strSQL);
		}
		return officeIdNameMap;
	}
	
	
	/**
	 * 根据局向ID获取Voip的服务器地址。如果没有对应信息则返回null
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-14
	 * @return VoiceServiceProfileObj
	 */
	public Map<String,VoiceServiceProfileObj> getOfficeIdVoip() 
	{
		logger.debug("getVoipServiceByofficeId()");

		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select proxy_server,proxy_port,standby_proxy_server,");
			psql.append("standby_proxy_port,regist_server,regist_port,");
			psql.append("standby_regist_server,standby_regist_port,outbound_proxy,");
			psql.append("outbound_port,standby_outbound_proxy,standby_outbound_port,office_id ");
		}else{
			psql.append("select * ");
		}
		psql.append("from gw_office_voip");
		List list = DataSetBean.executeQuery(psql.getSQL(), null);
		
		if(null == officeIdVoipMap){
			if (list != null) {
				officeIdVoipMap = new HashMap<String,VoiceServiceProfileObj>();
				int size = list.size();
				for(int i = 0; i < size; i++){
					Map map = (Map)list.get(i);
					VoiceServiceProfileObj voipObj = new VoiceServiceProfileObj();
					// SIP服务器
					voipObj.setProxServ(StringUtil.getStringValue(map, "proxy_server"));
					voipObj.setProxPort(StringUtil.getStringValue(map, "proxy_port"));
					voipObj.setProxServ2(StringUtil.getStringValue(map,"standby_proxy_server"));
					voipObj.setProxPort2(StringUtil.getStringValue(map,"standby_proxy_port"));
					// Registrar服务器
					voipObj.setRegiServ(StringUtil.getStringValue(map, "regist_server"));
					voipObj.setRegiPort(StringUtil.getStringValue(map, "regist_port"));
					voipObj.setStandRegiServ(StringUtil.getStringValue(map,"standby_regist_server"));
					voipObj.setStandRegiPort(StringUtil.getStringValue(map,"standby_regist_port"));
					// Outbound服务器
					voipObj.setOutBoundProxy(StringUtil.getStringValue(map,"outbound_proxy"));
					voipObj.setOutBoundPort(StringUtil.getStringValue(map,"outbound_port"));
					voipObj.setStandOutBoundProxy(StringUtil.getStringValue(map,"standby_outbound_proxy"));
					voipObj.setStandOutBoundPort(StringUtil.getStringValue(map,"standby_outbound_port"));
					//局向ID
					voipObj.setOfficeId(StringUtil.getStringValue(map.get("office_id")));

					officeIdVoipMap.put(StringUtil.getStringValue(map.get("office_id")), voipObj);
				}
			}
		}
		return officeIdVoipMap;
	}
}
