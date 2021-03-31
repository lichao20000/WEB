package com.linkage.module.gwms.dao.gw;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileH248OBJ;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileLineObj;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileObj;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class VoiceServiceProfileDAO extends SuperDAO 
{
	
	/**
	 * 获取指定device_id的VOIP节点，不包含线路信息  IMS 软件换
	 * 
	 * @param deviceId
	 * @return
	 */
	public VoiceServiceProfileObj[] getVoipProf(Map map)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select device_id,voip_id,prof_id,gather_time,");
		sql.append(" prox_serv,prox_port,prox_serv_2,prox_port_2,regi_serv,");
		sql.append(" regi_port,stand_regi_serv,stand_regi_port,out_bound_proxy,");
		sql.append(" out_bound_port,stand_out_bound_proxy,stand_out_bound_port");
		sql.append(" from "+Global.getTabName(StringUtil.getStringValue(map.get("device_id")),"gw_voip_prof"));
		sql.append(" where device_id='"+StringUtil.getStringValue(map.get("device_id"))+"'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		//int wan_id=	Integer.parseInt(StringUtil.getStringValue(map.get("wan_id")));
		//int wan_conn_id=Integer.parseInt(StringUtil.getStringValue(map.get("wan_conn_id")));
		//int wan_conn_sess_id=Integer.parseInt(StringUtil.getStringValue(map.get("wan_conn_sess_id")));
		//int sess_type=Integer.parseInt(StringUtil.getStringValue(map.get("sess_type")));
		String ip=StringUtil.getStringValue(map.get("ip"));
		VoiceServiceProfileObj[] rs = null;
		if(list.size()>0){
			rs = new VoiceServiceProfileObj[list.size()];
			for(int i=0;i<list.size();i++){
				Map one = (Map) list.get(i);
				rs[i] = new VoiceServiceProfileObj();
				rs[i].setDeviceId(String.valueOf(one.get("device_id")).toString());
				rs[i].setVoipId(String.valueOf(one.get("voip_id")).toString());
				rs[i].setProfId(String.valueOf(one.get("prof_id")).toString());
				rs[i].setGatherTime(String.valueOf(one.get("gather_time")).toString());
				rs[i].setProxServ(String.valueOf(one.get("prox_serv")).toString());
				rs[i].setProxPort(String.valueOf(one.get("prox_port")).toString());
				rs[i].setProxServ2(String.valueOf(one.get("prox_serv_2")).toString());
				rs[i].setProxPort2(String.valueOf(one.get("prox_port_2")).toString());
				rs[i].setRegiServ(String.valueOf(one.get("regi_serv")).toString());
				rs[i].setRegiPort(String.valueOf(one.get("regi_port")).toString());
				rs[i].setStandRegiServ(String.valueOf(one.get("stand_regi_serv")).toString());
				rs[i].setStandRegiPort(String.valueOf(one.get("stand_regi_port")).toString());
				rs[i].setOutBoundProxy(String.valueOf(one.get("out_bound_proxy")).toString());
				rs[i].setOutBoundPort(String.valueOf(one.get("out_bound_port")).toString());
				rs[i].setStandOutBoundProxy(String.valueOf(one.get("stand_out_bound_proxy")).toString());
				rs[i].setStandOutBoundPort(String.valueOf(one.get("stand_out_bound_port")).toString());
				rs[i].setIp(ip);
				//rs[i].setWan_id(wan_id);
				//rs[i].setWan_conn_id(wan_conn_id);
				//rs[i].setWan_conn_sess_id(wan_conn_sess_id);
				//rs[i].setSess_type(sess_type);
			}
		}
		
		return rs;
	}
	
	/**
	 * 获取指定device_id的VOIP节点，不包含线路信息 H248
	 * 
	 * @param deviceId
	 * @return
	 */
	public VoiceServiceProfileH248OBJ[] getVoipProfH(Map map)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select device_id,voip_id,prof_id,gather_time,");
		sql.append("media_gateway_controler,media_gateway_controler_2,h248_device_id ");
		if(LipossGlobals.inArea(Global.HBLT)){
			sql.append(",interfaceState ");
		}
		sql.append(" from "+Global.getTabName(StringUtil.getStringValue(map.get("device_id")),"gw_voip_prof_h248"));
		sql.append(" where device_id='"+StringUtil.getStringValue(map.get("device_id"))+"'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		VoiceServiceProfileH248OBJ[] rs = null;
		if(list.size()>0){
			rs = new VoiceServiceProfileH248OBJ[list.size()];
			for(int i=0;i<list.size();i++){
				Map one = (Map) list.get(i);
				rs[i] = new VoiceServiceProfileH248OBJ();
				rs[i].setDeviceId(String.valueOf(one.get("device_id")).toString());
				rs[i].setVoipId(String.valueOf(one.get("voip_id")).toString());
				rs[i].setProfId(String.valueOf(one.get("prof_id")).toString());
				rs[i].setGatherTime(String.valueOf(one.get("gather_time")).toString());
				rs[i].setMediaGatewayControler(String.valueOf(one.get("media_gateway_controler")).toString());
				rs[i].setMediaGatewayControler2(String.valueOf(one.get("media_gateway_controler_2")).toString());
				rs[i].setH248DeviceId(String.valueOf(one.get("h248_device_id")).toString());
				if(LipossGlobals.inArea(Global.HBLT)){
					rs[i].setInterfaceState(String.valueOf(one.get("interfaceState")).toString());
				}
			}
		}
		
		return rs;
	}
	
	/**
	 * 获取指定device_id的VOIP节点，不包含线路信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public VoiceServiceProfileObj[] getVoipProf(String device_id)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select device_id,voip_id,prof_id,gather_time,");
		sql.append(" prox_serv,prox_port,prox_serv_2,prox_port_2,regi_serv,");
		sql.append(" regi_port,stand_regi_serv,stand_regi_port,out_bound_proxy,");
		sql.append(" out_bound_port,stand_out_bound_proxy,stand_out_bound_port");
		sql.append(" from " + Global.getTabName(device_id, "gw_voip_prof"));
		sql.append(" where device_id='"+device_id+"'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		VoiceServiceProfileObj[] rs = null;
		if(list.size()>0){
			rs = new VoiceServiceProfileObj[list.size()];
			for(int i=0;i<list.size();i++){
				Map one = (Map) list.get(i);
				rs[i] = new VoiceServiceProfileObj();
				rs[i].setDeviceId(String.valueOf(one.get("device_id")).toString());
				rs[i].setVoipId(String.valueOf(one.get("voip_id")).toString());
				rs[i].setProfId(String.valueOf(one.get("prof_id")).toString());
				rs[i].setGatherTime(String.valueOf(one.get("gather_time")).toString());
				rs[i].setProxServ(String.valueOf(one.get("prox_serv")).toString());
				rs[i].setProxPort(String.valueOf(one.get("prox_port")).toString());
				rs[i].setProxServ2(String.valueOf(one.get("prox_serv_2")).toString());
				rs[i].setProxPort2(String.valueOf(one.get("prox_port_2")).toString());
				rs[i].setRegiServ(String.valueOf(one.get("regi_serv")).toString());
				rs[i].setRegiPort(String.valueOf(one.get("regi_port")).toString());
				rs[i].setStandRegiServ(String.valueOf(one.get("stand_regi_serv")).toString());
				rs[i].setStandRegiPort(String.valueOf(one.get("stand_regi_port")).toString());
				rs[i].setOutBoundProxy(String.valueOf(one.get("out_bound_proxy")).toString());
				rs[i].setOutBoundPort(String.valueOf(one.get("out_bound_port")).toString());
				rs[i].setStandOutBoundProxy(String.valueOf(one.get("stand_out_bound_proxy")).toString());
				rs[i].setStandOutBoundPort(String.valueOf(one.get("stand_out_bound_port")).toString());
			}
		}
		
		return rs;
	}
	
	/**
	 * 针对H248
	 * add by zhangchy 2013-04-22
	 * 
	 * 获取指定device_id的VOIP节点，不包含线路信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public VoiceServiceProfileH248OBJ[] getVoipProfH248(String deviceId)
	{
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
	 * 
	 * add by zhangchy 2011-11-25 
	 * 针对H248
	 * 
	 * @param deviceId
	 * @return
	 */
	public VoiceServiceProfileLineObj[] getVoipProfLine(VoiceServiceProfileH248OBJ voipProf)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_id,voip_id,prof_id,line_id,gather_time,enable,");
		psql.append("status,username,password,regist_result from gw_voip_prof_line ");
		psql.append("where device_id='"+voipProf.getDeviceId());
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
	 * 获得VoiceServiceProfileObj
	 * @author gongsj
	 * @date 2009-7-17
	 * @param deviceId
	 * @param voipId
	 * @param profileId
	 * @return
	 */
	public VoiceServiceProfileObj queryVoipProf(String deviceId, String voipId, String profileId) 
	{
		// 查询设备gw_voip_prof信息
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select prox_serv,prox_port,prox_serv_2,prox_port_2,gather_time from ");
		}else{
			psql.append("select * from ");
		}
		psql.append(Global.getTabName(deviceId, "gw_voip_prof"));
		psql.append(" where device_id=? and voip_id=? and prof_id=? ");
		psql.setString(1, deviceId);
		psql.setStringExt(2, voipId, false);
		psql.setStringExt(3, profileId, false);
		
		VoiceServiceProfileObj voiceProfObj = null;
		Map rMap = DataSetBean.getRecord(psql.getSQL());
		voiceProfObj = new VoiceServiceProfileObj();
		
		if (null != rMap && !rMap.isEmpty()) {
			voiceProfObj.setProxServ(String.valueOf(rMap.get("prox_serv")));
			voiceProfObj.setProxPort(String.valueOf(rMap.get("prox_port")));
			voiceProfObj.setProxServ2(String.valueOf(rMap.get("prox_serv_2")));
			voiceProfObj.setProxServ2(String.valueOf(rMap.get("prox_port_2")));
			voiceProfObj.setGatherTime(String.valueOf(rMap.get("gather_time")));
		}
		return voiceProfObj;
	}
	
    /**
     * 根据设备oui，设备序列号查询用户
     * 
     * @param oui
     * @param device_serialnumber
     * 
     * @author qxq(4174)
     * @date 2009-7-14
     * @return
     */
    public List getDeviceHUserInfo(String device_id)
    {
    	PrepareSQL psql = new PrepareSQL();
        psql.append("select user_id from tab_hgwcustomer ");
        psql.append("where user_state='1' and  device_id=? ");
        psql.setString(1, device_id);
        return jt.queryForList(psql.getSQL());
        
    }
    
    /**
     * <p>
     * [根据用户ID，查询用户开户的VOIP工单信息]
     * </p>
     * @param userId
     * @return
     */
    public List getBssVoipSheet(String userId)
    {
        PrepareSQL psql = new PrepareSQL();
        if(DBUtil.GetDB()==Global.DB_MYSQL){
        	psql.append("select protocol ");
        }else{
        	psql.append("select * ");
        }
        psql.append("from tab_voip_serv_param where user_id="+userId);
        return jt.queryForList(psql.getSQL());
    }
    
    
    /**
	 * 获取servList
	 * 
	 * add by zhangchy 2013-04-17
	 * 
	 */
	public List<Map> getAllChannel(String device_id, String gw_type)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_id,wan_id,wan_conn_id,wan_conn_sess_id,serv_list,sess_type,ip ");
		psql.append("from "+ Global.getTabName(device_id, "gw_wan_conn_session"));
		psql.append(" where device_id=? ");
		psql.setString(1, device_id);
		List<Map> list = jt.queryForList(psql.getSQL());
		return list;
	}
    
    
}
