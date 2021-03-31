package com.linkage.module.gwms.dao.gw;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileH248OBJ;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileLineObj;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileObj;

/**
 * VoiceServiceProfileLineDAO
 * @author gongsj
 * @date 2009-7-17
 */
public class VoiceServiceProfileLineDAO extends SuperDAO  
{

	/**
	 * 获得VoiceServiceProfileLineObj
	 * @author gongsj
	 * @date 2009-7-17
	 * @param deviceId
	 * @param voipId
	 * @param profileId
	 * @param lineId
	 * @return
	 */
	public VoiceServiceProfileLineObj queryVoipProfLine(String deviceId,
			String voipId,String profileId, String lineId) 
	{
		// 查询设备gw_voip_prof信息
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select enable,status,username,password,");
			psql.append("regist_result,gather_time from ");
		}else{
			psql.append("select * from ");
		}
		psql.append(Global.getTabName(deviceId, "gw_voip_prof_line"));
		psql.append(" where device_id=? and voip_id=? and prof_id=? and line_id=? ");
		psql.setString(1, deviceId);
		psql.setStringExt(2, voipId, false);
		psql.setStringExt(3, profileId, false);
		psql.setStringExt(4, lineId, false);
		
		VoiceServiceProfileLineObj voiceProfLineObj = null;
		Map rMap = DataSetBean.getRecord(psql.getSQL());
		voiceProfLineObj = new VoiceServiceProfileLineObj();
		
		if (null != rMap && !rMap.isEmpty()) {
			voiceProfLineObj.setEnable(String.valueOf(rMap.get("enable")));
			voiceProfLineObj.setStatus(String.valueOf(rMap.get("status")));
			voiceProfLineObj.setUsername(String.valueOf(rMap.get("username")));
			voiceProfLineObj.setPassword(String.valueOf(rMap.get("password")));
			voiceProfLineObj.setRegistResult(String.valueOf(rMap.get("regist_result")));
			voiceProfLineObj.setGatherTime(String.valueOf(rMap.get("gather_time")));
		}
		return voiceProfLineObj;
	}
	
	/**
	 * 获取指定device_id的VOIP所有Line节点
	 * 
	 * @param deviceId
	 * @return
	 */
	public VoiceServiceProfileLineObj[] getVoipProfLine(String deviceId)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_id,voip_id,prof_id,line_id,gather_time,enable,");
		psql.append("status,username,password from " + Global.getTabName(deviceId, "gw_voip_prof_line"));
		psql.append(" where device_id='"+deviceId+"'");
		
		List list = jt.queryForList(psql.getSQL());
		
		VoiceServiceProfileLineObj[] rs = null;
		if(list.size()>0){
			rs = new VoiceServiceProfileLineObj[list.size()];
			for(int i=0;i<list.size();i++){
				Map one = (Map) list.get(i);
				rs[i] = new VoiceServiceProfileLineObj();
				rs[i].setDeviceId(String.valueOf(one.get("device_id")).toString());
				rs[i].setVoipId(String.valueOf(one.get("voip_id")).toString());
				rs[i].setProfId(String.valueOf(one.get("prof_id")).toString());
				rs[i].setLineId(String.valueOf(one.get("line_id")).toString());
				rs[i].setGatherTime(String.valueOf(one.get("gather_time")).toString());
				rs[i].setEnable(String.valueOf(one.get("enable")).toString());
				rs[i].setStatus(String.valueOf(one.get("status")).toString());
				rs[i].setRegistResult(String.valueOf(one.get("regist_result")));
				rs[i].setUsername(String.valueOf(one.get("username")).toString());
				rs[i].setPassword(String.valueOf(one.get("password")).toString());
			}
		}
		
		return rs;
		
	}
	
	/**
	 * 获取指定GwVoipProfOBJ的VOIP所有Line节点
	 * 
	 * @param deviceId
	 * @return
	 */
	public VoiceServiceProfileLineObj[] getVoipProfLine(VoiceServiceProfileObj voipProf)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_id,voip_id,prof_id,line_id,gather_time,enable,");
		psql.append("status,username,password,regist_result,physical_term_id from ");
		psql.append(Global.getTabName(voipProf.getDeviceId(), "gw_voip_prof_line"));
		psql.append(" where device_id='"+voipProf.getDeviceId());
		psql.append("' and prof_id="+voipProf.getProfId());
		
		List list = jt.queryForList(psql.getSQL());
		
		VoiceServiceProfileLineObj[] rs = null;
		if(list.size()>0){
			rs = new VoiceServiceProfileLineObj[list.size()];
			for(int i=0;i<list.size();i++){
				Map one = (Map) list.get(i);
				rs[i] = new VoiceServiceProfileLineObj();
				rs[i].setDeviceId(String.valueOf(one.get("device_id")).toString());
				rs[i].setVoipId(String.valueOf(one.get("voip_id")).toString());
				rs[i].setProfId(String.valueOf(one.get("prof_id")).toString());
				rs[i].setLineId(String.valueOf(one.get("line_id")).toString());
				rs[i].setGatherTime(String.valueOf(one.get("gather_time")).toString());
				rs[i].setEnable(String.valueOf(one.get("enable")).toString());
				rs[i].setStatus(String.valueOf(one.get("status")).toString());
				rs[i].setRegistResult(String.valueOf(one.get("regist_result")));
				rs[i].setUsername(String.valueOf(one.get("username")).toString());
				rs[i].setPassword(String.valueOf(one.get("password")).toString());
				rs[i].setPhysicalTermId(String.valueOf(one.get("physical_term_id")).toString());
				rs[i].setIp(voipProf.getIp());
			}
		}
		
		return rs;
	}
	
	/**
	 * 获取指定GwVoipProfOBJ的VOIP所有Line节点
	 * 
	 * @param deviceId
	 * @return
	 */
	public VoiceServiceProfileLineObj[] getVoipProfLineForH(VoiceServiceProfileH248OBJ voipProf)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_id,voip_id,prof_id,line_id,gather_time,enable,");
		psql.append("status,username,password,regist_result,physical_term_id from ");
		psql.append(Global.getTabName(voipProf.getDeviceId(), "gw_voip_prof_line"));
		psql.append(" where device_id='"+voipProf.getDeviceId());
		psql.append("' and prof_id="+voipProf.getProfId());
		
		List list = jt.queryForList(psql.getSQL());
		
		VoiceServiceProfileLineObj[] rs = null;
		if(list.size()>0){
			rs = new VoiceServiceProfileLineObj[list.size()];
			for(int i=0;i<list.size();i++){
				Map one = (Map) list.get(i);
				rs[i] = new VoiceServiceProfileLineObj();
				rs[i].setDeviceId(String.valueOf(one.get("device_id")).toString());
				rs[i].setVoipId(String.valueOf(one.get("voip_id")).toString());
				rs[i].setProfId(String.valueOf(one.get("prof_id")).toString());
				rs[i].setLineId(String.valueOf(one.get("line_id")).toString());
				rs[i].setGatherTime(String.valueOf(one.get("gather_time")).toString());
				rs[i].setPhysicalTermId(String.valueOf(one.get("physical_term_id")).toString());
				if(LipossGlobals.inArea(Global.HBLT)){
					rs[i].setStatus(String.valueOf(one.get("status")).toString());
				}
			}
		}
		return rs;
	}

}
