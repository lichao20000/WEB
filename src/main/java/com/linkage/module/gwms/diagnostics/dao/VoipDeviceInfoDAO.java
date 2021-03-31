package com.linkage.module.gwms.diagnostics.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileH248OBJ;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileLineObj;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class VoipDeviceInfoDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(VoipDeviceInfoDAO.class);

	/**
	 * 获取设备信息
	 * @param deviceId
	 * @return
	 */
	public List getDeviceInfo(String deviceId)
	{
		logger.debug("getDeviceInfo({})",deviceId);
		
		List list=null;
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select a.device_id,a.oui,a.device_serialnumber,");
			psql.append("a.loopback_ip,a.cpe_mac,b.vendor_add,");
			psql.append("a.device_model_id,a.devicetype_id ");
			psql.append("from tab_gw_device a,tab_vendor b ");
			psql.append("where a.device_id=? and a.vendor_id=b.vendor_id");
			psql.setString(1,deviceId);
			
			List l= jt.queryForList(psql.getSQL());
			if(l!=null && !l.isEmpty()){
				Map<String,String> dm=getDeviceModel();
				Map<String,List<String>> dti=getDeviceTypeInfo();
				Map m=(Map)l.get(0);
				String device_model=dm.get(StringUtil.getStringValue(m,"device_model_id"));
				List<String> ver=dti.get(StringUtil.getStringValue(m,"devicetype_id"));
				if(StringUtil.IsEmpty(device_model) || ver==null || ver.isEmpty()){
					return null;
				}
				m.put("device_model", device_model);
				m.put("hardwareversion", ver.get(0));
				m.put("softwareversion", ver.get(1));
				
				list=new ArrayList();
				list.add(m);
			}
		}else{
			psql.append("select a.device_id,a.oui,a.device_serialnumber,");
			psql.append("a.loopback_ip,a.cpe_mac,b.vendor_add,");
			psql.append("c.device_model,d.hardwareversion,d.softwareversion ");
			psql.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d ");
			psql.append("where a.device_id=? and a.vendor_id=b.vendor_id ");
			psql.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id");
			psql.setString(1,deviceId);
			
			list= jt.queryForList(psql.getSQL());
		}
		
		return list;
	}
	
	/**
	 * 查询用户信息
	 * @param deviceId
	 * @return
	 */
	public List getDeviceEUserInfo(String deviceId)
	{
		logger.debug("getDeviceEUserInfo({})",deviceId);
		
		PrepareSQL psql = new PrepareSQL();
		psql.append("select b.user_id,b.username ");
		psql.append("from tab_customerinfo a,tab_egwcustomer b ");
		psql.append("where a.customer_id=b.customer_id and b.user_state in ('1','2') and b.device_id=? ");
		psql.setString(1, deviceId);
		
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 根据设备oui，设备序列号查询用户
	 * @param oui
	 * @param device_serialnumber
	 * 
	 * @author qxq(4174)
	 * @date 2009-7-14
	 * @return
	 */
	public List getDeviceHUserInfo(String device_id)
	{
		logger.debug("getDeviceHUserInfo({})",device_id);
		
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.user_id,a.username ");
		psql.append("from tab_hgwcustomer a,hgwcust_serv_info b ");
		psql.append("where a.user_id=b.user_id ");
		psql.append("and a.user_state='1' and  a.device_id=? and b.serv_type_id=10");
		psql.setString(1, device_id);
		
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 针对H248
	 * add by zhangchy 2011-11-25
	 * 获取指定device_id的VOIP节点，不包含线路信息
	 * @param deviceId
	 * @return
	 */
	public VoiceServiceProfileH248OBJ[] getVoipProfH248(String deviceId)
	{
		logger.debug("getVoipProfH248({})",deviceId);
		
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_id, voip_id, prof_id, gather_time,");
		psql.append(" media_gateway_controler, media_gateway_controler_port,");
		psql.append(" media_gateway_controler_2, media_gateway_controler_port_2,");
		psql.append(" media_gateway_port, h248_device_id, h248_device_id_type, rtp_prefix ");
		psql.append(" from gw_voip_prof_h248 where device_id='"+deviceId+"'");
		
		List list = jt.queryForList(psql.getSQL());
		
		VoiceServiceProfileH248OBJ[] rs = null;
		if(list.size()>0){
			rs = new VoiceServiceProfileH248OBJ[list.size()];
			for(int i=0;i<list.size();i++){
				Map one = (Map) list.get(i);
				rs[i] = new VoiceServiceProfileH248OBJ();
				rs[i].setDeviceId(StringUtil.getStringValue(one.get("device_id")));
				rs[i].setVoipId(StringUtil.getStringValue(one.get("voip_id")));
				rs[i].setProfId(StringUtil.getStringValue(one.get("prof_id")));
				rs[i].setGatherTime(StringUtil.getStringValue(one.get("gather_time")));
				rs[i].setMediaGatewayControler(StringUtil.getStringValue(one.get("media_gateway_controler")));
				rs[i].setMediaGatewayControlerPort(StringUtil.getStringValue(one.get("media_gateway_controler_port")));
				rs[i].setMediaGatewayControler2(StringUtil.getStringValue(one.get("media_gateway_controler_2")));
				rs[i].setMediaGatewayControlerPort2(StringUtil.getStringValue(one.get("media_gateway_controler_port_2")));
				rs[i].setMediaGatewayPort(StringUtil.getStringValue(one.get("media_gateway_port")));
				rs[i].setH248DeviceId(StringUtil.getStringValue(one.get("h248_device_id")));
				rs[i].setH248DeviceIdType(StringUtil.getStringValue(one.get("h248_device_id_type")));
				rs[i].setRtpPrefix(StringUtil.getStringValue(one.get("rtp_prefix")));
			}
		}
		
		return rs;
	}
	
	/**
	 * 获取指定GwVoipProfOBJ的VOIP所有Line节点
	 * add by zhangchy 2011-11-25 
	 * 针对H248
	 * @param deviceId
	 * @return
	 */
	public VoiceServiceProfileLineObj[] getVoipProfLine(VoiceServiceProfileH248OBJ voipProf)
	{
		logger.debug("getVoipProfLine({})",voipProf);
		
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_id,voip_id,prof_id,line_id,gather_time,enable,");
		psql.append("status,username,password,regist_result from gw_voip_prof_line where ");
		psql.append(" device_id='"+voipProf.getDeviceId());
		psql.append("' and prof_id="+voipProf.getProfId());
		
		List list = jt.queryForList(psql.getSQL());
		
		VoiceServiceProfileLineObj[] rs = null;
		if(list.size()>0){
			rs = new VoiceServiceProfileLineObj[list.size()];
			for(int i=0;i<list.size();i++){
				Map one = (Map) list.get(i);
				rs[i] = new VoiceServiceProfileLineObj();
				rs[i].setDeviceId(StringUtil.getStringValue(one.get("device_id")));
				rs[i].setVoipId(StringUtil.getStringValue(one.get("voip_id")));
				rs[i].setProfId(StringUtil.getStringValue(one.get("prof_id")));
				rs[i].setLineId(StringUtil.getStringValue(one.get("line_id")));
				rs[i].setGatherTime(StringUtil.getStringValue(one.get("gather_time")));
				rs[i].setEnable(StringUtil.getStringValue(one.get("enable")));
				rs[i].setStatus(StringUtil.getStringValue(one.get("status")));
				rs[i].setRegistResult(StringUtil.getStringValue(one.get("regist_result")));
				rs[i].setUsername(StringUtil.getStringValue(one.get("username")));
				rs[i].setPassword(StringUtil.getStringValue(one.get("password")));
			}
		}
		
		return rs;
	}
	
	/**
	 * 获取servList
	 */
	public List<Map> getAllChannel(String device_id)
	{
		logger.debug("getAllChannel({})",device_id);
		
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_id,wan_id,wan_conn_id,wan_conn_sess_id,serv_list ");
		psql.append("from gw_wan_conn_session where device_id='"+device_id+"'");
		
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 获取所有型号
	 */
	private Map<String,String> getDeviceModel()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_model_id,device_model ");
		psql.append("from gw_device_model order by device_model_id ");
		
		List<Map> list=jt.queryForList(psql.getSQL());
		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty()){
			for(Map m:list){
				map.put(StringUtil.getStringValue(m,"device_model_id"),
						StringUtil.getStringValue(m,"device_model"));
			}
		}
		
		return map;
	}
	
	/**
	 * 获取所有版本
	 */
	private Map<String,List<String>> getDeviceTypeInfo()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select devicetype_id,hardwareversion,softwareversion ");
		psql.append("from tab_devicetype_info order by devicetype_id ");
		
		List<Map> list=jt.queryForList(psql.getSQL());
		Map<String,List<String>> map=new HashMap<String,List<String>>();
		if(list!=null && !list.isEmpty()){
			List<String> ver=new ArrayList<String>();
			for(Map m:list){
				ver.add(StringUtil.getStringValue(m,"hardwareversion"));
				ver.add(StringUtil.getStringValue(m,"softwareversion"));
				map.put(StringUtil.getStringValue(m,"devicetype_id"),ver);
			}
		}
		
		return map;
	}
	
}
