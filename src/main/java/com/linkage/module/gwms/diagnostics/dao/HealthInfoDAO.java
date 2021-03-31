package com.linkage.module.gwms.diagnostics.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.HgwServUserDAO;
import com.linkage.module.gwms.obj.gw.HealthLanWlanOBJ;
import com.linkage.module.gwms.obj.gw.HealthWanDslOBJ;
import com.linkage.module.gwms.obj.gw.VoipAddressOBJ;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings({"unchecked","rawtypes"})
public class HealthInfoDAO 
{
	private static Logger logger = LoggerFactory.getLogger(HealthInfoDAO.class);
	
	private JdbcTemplate dao;
	private HgwServUserDAO hgwServUserDao;
	
	
	/**
	 * 获得业务用户信息
	 * @author gongsj
	 * @date 2009-8-13
	 * @param userId
	 * @param servTypeId
	 * @return
	 */
	public HgwServUserObj getUserInfo(String userId, String servTypeId,String gwType){
		hgwServUserDao.setGw_type(gwType);
		HgwServUserObj servUserObj = hgwServUserDao.getUserInfo(userId, servTypeId);
		
		return servUserObj;
	}
	
	/**
	 * 编辑业务信息
	 * @author gongsj
	 * @date 2009-8-17
	 * @param userId
	 * @param username
	 * @param password
	 * @param servType
	 * @param wanType
	 * @param vpi
	 * @param vci
	 * @param vlanid
	 * @param ip
	 * @param mask
	 * @param gateway
	 * @param dns
	 * @param bindport
	 * @return
	 */
	public boolean editBussInfo(String userId,String username,String password,String servType,
			String wanType, String vpi, String vci, String vlanid, String ip, String mask,
			String gateway, String dns, String bindport) 
	{
		logger.debug("HealthInfoDAO editBussInfo()");
		return hgwServUserDao.editBussInfo(userId, username, password, servType, wanType, vpi, vci, vlanid, ip, mask, gateway, dns, bindport);
	}
	
	/**
	 * 更新VOIP地址表
	 * @author gongsj
	 * @date 2009-8-19
	 * @param deviceId
	 * @param proxServer
	 * @param proxPort
	 * @param proxServer2
	 * @param proxPort2
	 * @param regiServ
	 * @param regiPort
	 * @param standRegiServ
	 * @param standRegiPort
	 * @param outBoundProxy
	 * @param outBoundPort
	 * @param standOutBoundProxy
	 * @param standOutBoundPort
	 * @return
	 */
	public boolean editVoipAddr(String deviceId, String proxServer, String proxPort, String proxServer2, 
			String proxPort2, String regiServ, String regiPort, String standRegiServ, String standRegiPort,
			String outBoundProxy, String outBoundPort, String standOutBoundProxy, String standOutBoundPort)
	{
		
		logger.debug("HealthInfoDAO editVoipAddr()");
		
		StringBuilder sql = new StringBuilder();
		sql.append("update gw_voip_init_param set prox_server='").append(proxServer).append("',")
		   .append("prox_port=").append(proxPort).append(",")
		   .append("prox_server2='").append(proxServer2).append("',")
		   .append("prox_port2=").append(proxPort2).append(",")
		   .append("regi_serv='").append(regiServ).append("',")
		   .append("regi_port=").append(regiPort).append(",")
		   .append("stand_regi_serv='").append(standRegiServ).append("',")
		   .append("stand_regi_port=").append(standRegiPort).append(",")
		   .append("out_bound_proxy='").append(outBoundProxy).append("',")
		   .append("out_bound_port=").append(outBoundPort).append(",")
		   .append("stand_out_bound_proxy='").append(standOutBoundProxy).append("',")
		   .append("stand_out_bound_port=").append(standOutBoundPort).append(" ")
		   .append("where device_id='").append(deviceId).append("'");
		   
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int result = DataSetBean.executeUpdate(psql.getSQL());
		
		logger.debug("更新gw_voip_init_param结果：{}", result);
		
		if (result >= 0) {
			logger.debug("更新gw_voip_init_param成功！");
			return true;
		} else {
			logger.debug("更新gw_voip_init_param失败！");
			return false;
		}
	}
	
	/**
	 * 编辑健康库信息
	 * @author gongsj
	 * @date 2009-8-18
	 * @param deviceId
	 * @param upAttenMin
	 * @param upAttenMax
	 * @param downAttenMin
	 * @param downAttenMax
	 * @param upMaxRateMin
	 * @param upMaxRateMax
	 * @param downMaxRateMin
	 * @param downMaxRateMax
	 * @param upNoiseMin
	 * @param upNoiseMax
	 * @param downNoiseMin
	 * @param downNoiseMax
	 * @param interDepthMin
	 * @param interDepthMax
	 * @param datePath
	 * @param powerlevel
	 * @param powervalue
	 * @return
	 */
	public boolean editHealthInfo(String deviceId, String upAttenMin, String upAttenMax, 
			String downAttenMin, String downAttenMax, String upMaxRateMin, 
			String upMaxRateMax, String downMaxRateMin, String downMaxRateMax, 
			String upNoiseMin, String upNoiseMax, String downNoiseMin, 
			String downNoiseMax, String interDepthMin, String interDepthMax, 
			String datePath, String powerlevel, String powervalue) 
	{
		logger.debug("HealthInfoDAO editHealthInfo()");
		
		logger.debug("编辑健康库的线路信息！");
		StringBuilder sql = new StringBuilder();
		
		sql.append("update gw_wan_dsl_inter_conf_health set up_attenuation_min=").append(upAttenMin)
		   .append(", up_attenuation_max=").append(upAttenMax)
		   .append(", down_attenuation_min=").append(downAttenMin)
		   .append(", down_attenuation_max=").append(downAttenMax)
		   .append(", up_maxrate_min=").append(upMaxRateMin)
		   .append(", up_maxrate_max=").append(upMaxRateMax)
		   .append(", down_maxrate_min=").append(downMaxRateMin)
		   .append(", down_maxrate_max=").append(downMaxRateMax)
		   .append(", data_path='").append(datePath)
		   .append("', interleave_depth_min=").append(interDepthMin)
		   .append(", interleave_depth_max=").append(interDepthMax)
		   .append(", up_noise_min=").append(upNoiseMin)
		   .append(", up_noise_max=").append(upNoiseMax)
		   .append(", down_noise_min=").append(downNoiseMin)
		   .append(", down_noise_max=").append(downNoiseMax)
		   .append(" where device_id='").append(deviceId).append("'");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int wanDsl = DataSetBean.executeUpdate(psql.getSQL());
		if (wanDsl < 0) {
			logger.debug("更新gw_wan_dsl_inter_conf_health表失败！");
			return false;
		}
		
		
		logger.debug("编辑健康库的无线信息！");
		sql = new StringBuilder();
		
//		sql.append("update gw_lan_wlan_health set powerlevel=").append(powerlevel)
//		   .append(", powervalue=").append(powervalue)
//		   .append(" where device_id='").append(deviceId).append("'");
		
		sql.append("update gw_lan_wlan_health set powervalue=").append(powervalue)
		   .append(" where device_id='").append(deviceId).append("'");
		
		psql = new PrepareSQL(sql.toString());
		int wlan = DataSetBean.executeUpdate(psql.getSQL());
		if (wlan < 0) {
			logger.debug("插入gw_lan_wlan_health表失败！");
			return false;
		}
		
		return true;
	}

	/**
	 * 插入健康信息
	 * @author gongsj
	 * @date 2009-8-17
	 * @param deviceId
	 */
	public void insertHealthInfo(String deviceId, boolean healthWire, boolean healthWlan) 
	{
		//1) 入线路信息
		if (!healthWire) {
			logger.debug("入健康库的线路信息！");
			PrepareSQL psql = new PrepareSQL();
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				psql.append("select device_id,wan_id,status,modulation_type,up_attenuation,up_attenuation+5 a1,");
				psql.append("up_attenuation-5 a2,down_attenuation,down_attenuation+5 a3,down_attenuation-5 a4,");
				psql.append("up_maxrate,up_maxrate+5 a5,up_maxrate-5 a6,down_maxrate,down_maxrate+5 a7,");
				psql.append("down_maxrate-5 a8,data_path,interleave_depth,interleave_depth+5 a9,interleave_depth-5 a10,");
				psql.append("up_noise,up_noise+5 a11,up_noise-5 a12,down_noise,down_noise+5 a13,down_noise-5 a14 ");
				psql.append(" from " +Global.getTabName(deviceId, "gw_wan_wireinfo"));
				psql.append(" where device_id='"+deviceId+"'");
				
				Cursor cursor=DataSetBean.getCursor(psql.getSQL());
				if(cursor!=null){
					psql = new PrepareSQL();
					psql.append("insert into gw_wan_dsl_inter_conf_health(device_id,wan_id,status,modulation_type,");
					psql.append("up_attenuation,up_attenuation_max,up_attenuation_min,down_attenuation,");
					psql.append("down_attenuation_max,down_attenuation_min,up_maxrate,up_maxrate_max,");
					psql.append("up_maxrate_min,down_maxrate,down_maxrate_max,down_maxrate_min,data_path,");
					psql.append("interleave_depth,interleave_depth_max,interleave_depth_min,");
					psql.append("up_noise,up_noise_max,up_noise_min,down_noise,down_noise_max,down_noise_min) ");
					psql.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					
					Map fields = cursor.getNext();
					ArrayList<String> sql=new ArrayList<String>();
					while (fields != null)
					{
						psql.setString(1,StringUtil.getStringValue(fields,"device_id"));
						psql.setLong(2,StringUtil.getLongValue(fields,"wan_id"));
						psql.setString(3,StringUtil.getStringValue(fields,"status"));
						psql.setString(4,StringUtil.getStringValue(fields,"modulation_type"));
						psql.setLong(5, StringUtil.getLongValue(fields,"up_attenuation"));
						psql.setLong(6, StringUtil.getLongValue(fields,"a1"));
						psql.setLong(7, StringUtil.getLongValue(fields,"a2"));
						psql.setLong(8, StringUtil.getLongValue(fields,"down_attenuation"));
						psql.setLong(9, StringUtil.getLongValue(fields,"a3"));
						psql.setLong(10, StringUtil.getLongValue(fields,"a4"));
						psql.setLong(11, StringUtil.getLongValue(fields,"up_maxrate"));
						psql.setLong(12, StringUtil.getLongValue(fields,"a5"));
						psql.setLong(13, StringUtil.getLongValue(fields,"a6"));
						psql.setLong(14, StringUtil.getLongValue(fields,"down_maxrate"));
						psql.setLong(15, StringUtil.getLongValue(fields,"a7"));
						psql.setLong(16, StringUtil.getLongValue(fields,"a8"));
						psql.setString(17,StringUtil.getStringValue(fields,"data_path"));
						psql.setLong(18, StringUtil.getLongValue(fields,"interleave_depth"));
						psql.setLong(19, StringUtil.getLongValue(fields,"a9"));
						psql.setLong(20, StringUtil.getLongValue(fields,"a10"));
						psql.setLong(21, StringUtil.getLongValue(fields,"up_noise"));
						psql.setLong(22, StringUtil.getLongValue(fields,"a11"));
						psql.setLong(23, StringUtil.getLongValue(fields,"a12"));
						psql.setLong(24, StringUtil.getLongValue(fields,"down_noise"));
						psql.setLong(25, StringUtil.getLongValue(fields,"a13"));
						psql.setLong(26, StringUtil.getLongValue(fields,"a14"));
						
						sql.add(psql.getSQL());
						fields = cursor.getNext();
						if(sql.size()==200){
							DataSetBean.doBatch(sql);
							sql.clear();
						}
					}
					
					if(!sql.isEmpty()){
						DataSetBean.doBatch(sql);
						sql.clear();
					}
					sql=null;
				}
			}else{
				psql.append("insert into gw_wan_dsl_inter_conf_health(device_id,wan_id,status,modulation_type,");
				psql.append("up_attenuation,up_attenuation_max,up_attenuation_min,down_attenuation,");
				psql.append("down_attenuation_max,down_attenuation_min,up_maxrate,up_maxrate_max,");
				psql.append("up_maxrate_min,down_maxrate,down_maxrate_max,down_maxrate_min,data_path,");
				psql.append("interleave_depth,interleave_depth_max,interleave_depth_min,");
				psql.append("up_noise,up_noise_max,up_noise_min,down_noise,down_noise_max,down_noise_min) ");
				psql.append(" select device_id,wan_id,status,modulation_type,up_attenuation,up_attenuation+5,");
				psql.append("up_attenuation-5,down_attenuation,down_attenuation+5,down_attenuation-5,");
				psql.append("up_maxrate,up_maxrate+5,up_maxrate-5,down_maxrate,down_maxrate+5,");
				psql.append("down_maxrate-5,data_path,interleave_depth,interleave_depth+5,interleave_depth-5,");
				psql.append("up_noise,up_noise+5,up_noise-5,down_noise,down_noise+5,down_noise-5 ");
				psql.append(" from " +Global.getTabName(deviceId, "gw_wan_wireinfo"));
				psql.append(" where device_id='"+deviceId+"'");
				
				int wanDsl = DataSetBean.executeUpdate(psql.getSQL());
				if (wanDsl < 0) {
					logger.debug("插入gw_wan_dsl_inter_conf_health表失败！");
				}
			}
		}
		
		//2) 入WLAN信息
		if (!healthWlan) {
			PrepareSQL psql = new PrepareSQL();
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				psql.append("select device_id,lan_id,lan_wlan_id,powerlevel,powervalue ");
				psql.append(" from "+Global.getTabName(deviceId, "gw_lan_wlan"));
				psql.append(" where"+" device_id='"+deviceId+"'");
				
				Cursor cursor=DataSetBean.getCursor(psql.getSQL());
				if(cursor!=null){
					psql = new PrepareSQL();
					psql.append("insert into gw_lan_wlan_health(device_id,lan_id,lan_wlan_id,powerlevel,powervalue) ");
					psql.append("values(?,?,?,?,?)");
					
					Map fields = cursor.getNext();
					ArrayList<String> sql=new ArrayList<String>();
					while (fields != null)
					{
						psql.setString(1,StringUtil.getStringValue(fields,"device_id"));
						psql.setInt(2, StringUtil.getIntValue(fields,"lan_id"));
						psql.setInt(3, StringUtil.getIntValue(fields,"lan_wlan_id"));
						psql.setInt(4, StringUtil.getIntValue(fields,"powerlevel"));
						psql.setInt(5, StringUtil.getIntValue(fields,"powervalue"));
						sql.add(psql.getSQL());
						fields = cursor.getNext();
						if(sql.size()==200){
							DataSetBean.doBatch(sql);
							sql.clear();
						}
					}
					
					if(!sql.isEmpty()){
						DataSetBean.doBatch(sql);
						sql.clear();
					}
				}
			}else{
				psql.append("insert into gw_lan_wlan_health(device_id,lan_id,lan_wlan_id,powerlevel,powervalue) ");
				psql.append("select device_id,lan_id,lan_wlan_id,powerlevel,powervalue ");
				psql.append(" from "+Global.getTabName(deviceId, "gw_lan_wlan"));
				psql.append(" where"+" device_id='"+deviceId+"'");
				
				int wlan = DataSetBean.executeUpdate(psql.getSQL());
				if (wlan < 0) {
					logger.debug("插入gw_lan_wlan_health表失败！");
				}
			}
		}
	}
	
	/**
	 * 查找指定deviceId的设备是否存在
	 * @author gongsj
	 * @date 2009-8-17
	 * @param deviceId
	 * @return true 已存在
	 *         false 不存在
	 */
	public boolean searchRecordExist(String deviceId, String tabName) 
	{
		logger.debug("查询device_id为{}的设备，在表{}中是否已存在", deviceId, tabName);
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select count(*) num from ");
		}else{
			psql.append("select count(1) num from ");
		}
		psql.append(tabName+" where device_id='"+deviceId+"'");
		
		Map<String, String> numMap = DataSetBean.getRecord(psql.getSQL());
		int num = 0;
		if (null != numMap && !numMap.isEmpty()) {
			num = Integer.parseInt(numMap.get("num"));
		}
		
		if (num > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 删除健康库中已采集的数据
	 * @author gongsj
	 * @date 2009-8-18
	 * @param deviceId
	 * @param tabName
	 * @return
	 */
	public boolean deleteRecordExist(String deviceId, String tabName) {
		logger.debug("删除device_id为{}的设备，在表{}中的信息", deviceId, tabName);
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("delete from ").append(tabName).append(" where device_id='").append(deviceId).append("'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		int result = DataSetBean.executeUpdate(sql.toString());
		
		if (result < 0) {
			logger.debug("删除失败");
			return false;
		}
		
		return true;
	}
	
	/**
	 * 获取健康表信息
	 * @author gongsj
	 * @date 2009-8-13
	 * @param deviceId
	 * @return
	 */
	public HealthWanDslOBJ[] getDslHealthInfo(String deviceId) 
	{
		logger.debug("getDslHealthInfo");
		if (null == deviceId) {
			return null;
		}
		HealthWanDslOBJ[] wireInfoObj = null;
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select wan_id,status,modulation_type,up_attenuation,");
			psql.append("up_attenuation_max,up_attenuation_min,down_attenuation,");
			psql.append("down_attenuation_max,down_attenuation_min,up_maxrate,");
			psql.append("up_maxrate_max,up_maxrate_min,down_maxrate,down_maxrate_max,");
			psql.append("down_maxrate_min,data_path,interleave_depth,interleave_depth_max,");
			psql.append("interleave_depth_min,up_noise,up_noise_max,up_noise_min,");
			psql.append("down_noise,down_noise_max,down_noise_min ");
		}else{
			psql.append("select * ");
		}
		psql.append("from gw_wan_dsl_inter_conf_health where device_id=? ");
		psql.setString(1, deviceId);
		
		List<Map<String, String>> rList = dao.queryForList(psql.getSQL());
		if (null != rList && !rList.isEmpty()) 
		{
			int lSize = rList.size();
			wireInfoObj = new HealthWanDslOBJ[lSize];
			for (int i = 0; i < lSize; i++) 
			{
				Map<String, String> rMap = rList.get(i);
				
				wireInfoObj[i] = new HealthWanDslOBJ();
				if (null != rMap && rMap.isEmpty() == false) 
				{
					wireInfoObj[i].setDeviceId(deviceId);
					wireInfoObj[i].setWanId(String.valueOf(rMap.get("wan_id")));
					wireInfoObj[i].setStatus(String.valueOf(rMap.get("status")));
					wireInfoObj[i].setModType(String.valueOf(rMap.get("modulation_type")));
					wireInfoObj[i].setUpdateTime("" + (new Date()).getTime() / 1000);
					
					if (null != rMap.get("up_attenuation")) {
						wireInfoObj[i].setUpAtten(Float.valueOf(String.valueOf(rMap.get("up_attenuation"))));
					}
					if (null != rMap.get("up_attenuation_max")) {
						wireInfoObj[i].setUpAttenMax(Float.valueOf(String.valueOf(rMap.get("up_attenuation_max"))));
					}
					if (null != rMap.get("up_attenuation_min")) {
						wireInfoObj[i].setUpAttenMin(Float.valueOf(String.valueOf(rMap.get("up_attenuation_min"))));
					}
					
					if (null != rMap.get("down_attenuation")) {
						wireInfoObj[i].setDownAtten(Float.valueOf(String.valueOf(rMap.get("down_attenuation"))));
					}
					if (null != rMap.get("down_attenuation_max")) {
						wireInfoObj[i].setDownAttenMax(Float.valueOf(String.valueOf(rMap.get("down_attenuation_max"))));
					}
					if (null != rMap.get("down_attenuation_min")) {
						wireInfoObj[i].setDownAttenMin(Float.valueOf(String.valueOf(rMap.get("down_attenuation_min"))));
					}
					
					if (null != rMap.get("up_maxrate")) {
						wireInfoObj[i].setUpMaxRate(Long.valueOf(String.valueOf(rMap.get("up_maxrate"))));
					}
					if (null != rMap.get("up_maxrate_max")) {
						wireInfoObj[i].setUpMaxRateMax(Long.valueOf(String.valueOf(rMap.get("up_maxrate_max"))));
					}
					if (null != rMap.get("up_maxrate_min")) {
						wireInfoObj[i].setUpMaxRateMin(Long.valueOf(String.valueOf(rMap.get("up_maxrate_min"))));
					}
					
					if (null != rMap.get("down_maxrate")) {
						wireInfoObj[i].setDownMaxRate(Long.valueOf(String.valueOf(rMap.get("down_maxrate"))));
					}
					if (null != rMap.get("down_maxrate_max")) {
						wireInfoObj[i].setDownMaxRateMax(Long.valueOf(String.valueOf(rMap.get("down_maxrate_max"))));
					}
					if (null != rMap.get("down_maxrate_min")) {
						wireInfoObj[i].setDownMaxRateMin(Long.valueOf(String.valueOf(rMap.get("down_maxrate_min"))));
					}
					
					wireInfoObj[i].setDatePath(String.valueOf(rMap.get("data_path")));
					
					if (null != rMap.get("interleave_depth")) {
						wireInfoObj[i].setInterDepth(Long.valueOf(String.valueOf(rMap.get("interleave_depth"))));
					}
					if (null != rMap.get("interleave_depth_max")) {
						wireInfoObj[i].setInterDepthMax(Long.valueOf(String.valueOf(rMap.get("interleave_depth_max"))));
					}
					if (null != rMap.get("interleave_depth_min")) {
						wireInfoObj[i].setInterDepthMin(Long.valueOf(String.valueOf(rMap.get("interleave_depth_min"))));
					}
					
					if (null != rMap.get("up_noise")) {
						wireInfoObj[i].setUpNoise(Float.valueOf(String.valueOf(rMap.get("up_noise"))));
					}
					if (null != rMap.get("up_noise_max")) {
						wireInfoObj[i].setUpNoiseMax(Float.valueOf(String.valueOf(rMap.get("up_noise_max"))));
					}
					if (null != rMap.get("up_noise_min")) {
						wireInfoObj[i].setUpNoiseMin(Float.valueOf(String.valueOf(rMap.get("up_noise_min"))));
					}
					
					if (null != rMap.get("down_noise")) {
						wireInfoObj[i].setDownNoise(Float.valueOf(String.valueOf(rMap.get("down_noise"))));
					}
					if (null != rMap.get("down_noise_max")) {
						wireInfoObj[i].setDownNoiseMax(Float.valueOf(String.valueOf(rMap.get("down_noise_max"))));
					}
					if (null != rMap.get("down_noise_min")) {
						wireInfoObj[i].setDownNoiseMin(Float.valueOf(String.valueOf(rMap.get("down_noise_min"))));
					}
					
				}
			}
		}
		return wireInfoObj;
	}
	
	/**
	 * 
	 * @author gongsj
	 * @date 2009-8-18
	 * @param deviceId
	 * @return
	 */
	public HealthLanWlanOBJ getWlanHealthInfo(String deviceId) {
		
		if (null == deviceId) {
			return null;
		}
		HealthLanWlanOBJ wlanInfoObj = new HealthLanWlanOBJ();
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select lan_id,lan_wlan_id,powerlevel,powervalue ");
		}else{
			psql.append("select * ");
		}
		psql.append("from gw_lan_wlan_health where device_id=? ");
		psql.setString(1, deviceId);
		
		List<Map<String, String>> rList = dao.queryForList(psql.getSQL());
		if (null != rList && !rList.isEmpty()) {
			Map<String, String> healthWlanMap = rList.get(0);
			
			wlanInfoObj.setDeviceId(deviceId);
			wlanInfoObj.setGatherTime("" + (new Date()).getTime() / 1000);
			wlanInfoObj.setLanId(String.valueOf(healthWlanMap.get("lan_id")));
			wlanInfoObj.setLanWlanId(String.valueOf(healthWlanMap.get("lan_wlan_id")));
			wlanInfoObj.setPowerlevel(String.valueOf(healthWlanMap.get("powerlevel")));
			wlanInfoObj.setPowervalue(String.valueOf(healthWlanMap.get("powervalue")));
		}
		
		return wlanInfoObj;
	}
	
	/**
	 * 获得VOIP地址
	 * @author gongsj
	 * @date 2009-8-19
	 * @param deviceId
	 * @return
	 */
	public VoipAddressOBJ getVoipAddress(String deviceId) 
	{
		logger.debug("HealthInfoDAO getVoipAddress(device_id={})", deviceId);
		VoipAddressOBJ voipAddrObj = null;
		
		if (!searchRecordExist(deviceId, "gw_voip_init_param")) 
		{
			PrepareSQL psql = new PrepareSQL();
			//如果gw_voip_init_param表中不存在记录
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				psql.append("select '"+deviceId+"',prox_server,prox_port,prox_server2,prox_port2,");
				psql.append("regi_serv,regi_port,stand_regi_serv,stand_regi_port,out_bound_proxy,");
				psql.append("out_bound_port,stand_out_bound_proxy,stand_out_bound_port ");
				psql.append("from gw_voip_init_param where device_id='0'");
				
				List<Map<String, String>> list = dao.queryForList(psql.getSQL());
				if (null != list && !list.isEmpty()) 
				{
					psql = new PrepareSQL();
					psql.append("insert into gw_voip_init_param(device_id,");
					psql.append("prox_server,prox_port,prox_server2,prox_port2,");
					psql.append("regi_serv,regi_port,stand_regi_serv,stand_regi_port,");
					psql.append("out_bound_proxy,out_bound_port,stand_out_bound_proxy,stand_out_bound_port) ");
					psql.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?) ");
					
					String[] sql=null;
					for(int i=0;i<list.size();i++)
					{
						Map<String, String> map=list.get(i);
						psql.setString(1,deviceId);
						psql.setString(2,StringUtil.getStringValue(map,"prox_server"));
						psql.setInt(3,StringUtil.getIntValue(map,"prox_port"));
						psql.setString(4,StringUtil.getStringValue(map,"prox_server2"));
						psql.setInt(5,StringUtil.getIntValue(map,"prox_port2"));
						psql.setString(6,StringUtil.getStringValue(map,"regi_serv"));
						psql.setInt(7,StringUtil.getIntValue(map,"regi_port"));
						psql.setString(8,StringUtil.getStringValue(map,"stand_regi_serv"));
						psql.setInt(9,StringUtil.getIntValue(map,"stand_regi_port"));
						psql.setString(10,StringUtil.getStringValue(map,"out_bound_proxy"));
						psql.setInt(11,StringUtil.getIntValue(map,"out_bound_port"));
						psql.setString(12,StringUtil.getStringValue(map,"stand_out_bound_proxy"));
						psql.setInt(13,StringUtil.getIntValue(map,"stand_out_bound_port"));
						
						map=null;
						if(i%200==0){
							sql=new String[200];
						}
						sql[i%200]=psql.getSQL();
						
						if(i%200==199){
							dao.batchUpdate(sql);
							sql=null;
						}
					}
				}
			}else{
				psql.append("insert into gw_voip_init_param(device_id,");
				psql.append("prox_server,prox_port,prox_server2,prox_port2,");
				psql.append("regi_serv,regi_port,stand_regi_serv,stand_regi_port,");
				psql.append("out_bound_proxy,out_bound_port,stand_out_bound_proxy,stand_out_bound_port) ");
				psql.append("select '"+deviceId+"',prox_server,prox_port,prox_server2,prox_port2,");
				psql.append("regi_serv,regi_port,stand_regi_serv,stand_regi_port,out_bound_proxy,");
				psql.append("out_bound_port,stand_out_bound_proxy,stand_out_bound_port ");
				psql.append("from gw_voip_init_param where device_id='0'");
				
				int result = DataSetBean.executeUpdate(psql.getSQL());
				if (result < 0) {
					logger.debug("操作失败");
					return null;
				}
			}
		}
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select prox_server,prox_port,prox_server2,prox_port2,regi_serv,");
			psql.append("regi_port,stand_regi_serv,stand_regi_port,out_bound_proxy,");
			psql.append("out_bound_port,stand_out_bound_proxy,stand_out_bound_port ");
		}else{
			psql.append("select * ");
		}
		psql.append("from gw_voip_init_param where device_id='"+deviceId+"'");
		
		List<Map<String, String>> rList = dao.queryForList(psql.getSQL());
		if (null != rList && !rList.isEmpty()) 
		{
			Map<String, String> voipAddressMap = rList.get(0);
			
			voipAddrObj = new VoipAddressOBJ();
			voipAddrObj.setDeviceId(deviceId);
			voipAddrObj.setProxServer(String.valueOf(voipAddressMap.get("prox_server")));
			voipAddrObj.setProxPort(String.valueOf(voipAddressMap.get("prox_port")));
			voipAddrObj.setProxServer2(String.valueOf(voipAddressMap.get("prox_server2")));
			voipAddrObj.setProxPort2(String.valueOf(voipAddressMap.get("prox_port2")));
			voipAddrObj.setRegiServ(String.valueOf(voipAddressMap.get("regi_serv")));
			voipAddrObj.setRegiPort(String.valueOf(voipAddressMap.get("regi_port")));
			voipAddrObj.setStandRegiServ(String.valueOf(voipAddressMap.get("stand_regi_serv")));
			voipAddrObj.setStandRegiPort(String.valueOf(voipAddressMap.get("stand_regi_port")));
			voipAddrObj.setOutBoundProxy(String.valueOf(voipAddressMap.get("out_bound_proxy")));
			voipAddrObj.setOutBoundPort(String.valueOf(voipAddressMap.get("out_bound_port")));
			voipAddrObj.setStandOutBoundProxy(String.valueOf(voipAddressMap.get("stand_out_bound_proxy")));
			voipAddrObj.setStandOutBoundPort(String.valueOf(voipAddressMap.get("stand_out_bound_port")));
		}
		
		return voipAddrObj;
	}
	
	/**
	 * 根据deviceId获得userId
	 * @author gongsj
	 * @date 2009-8-17
	 * @param deviceId
	 * @return
	 */
	public String getUserId(String deviceId,String gw_type) 
	{
		logger.debug("getUserId HealthInfoDAO");
		String tableName="tab_hgwcustomer";
		if("2".equals(gw_type)){
			tableName="tab_egwcustomer";
		}
		
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.user_id from ");
		psql.append(tableName+" a, tab_gw_device b ");
		psql.append("where a.device_id=b.device_id and b.device_id='"+deviceId+"'");
		Map rMap = DataSetBean.getRecord(psql.getSQL());
		return StringUtil.getStringValue(rMap,"user_id");
	}
	
	/**
	 * 设置DAO
	 * @author gongsj
	 * @date 2009-8-13
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		logger.debug("setDao(DataSource)");

		this.dao = new JdbcTemplate(dao);
	}

	/**
	 * 业务用户DAO
	 * @author gongsj
	 * @date 2009-8-13
	 * @param hgwServUserDao
	 */
	public void setHgwServUserDao(HgwServUserDAO hgwServUserDao) {
		this.hgwServUserDao = hgwServUserDao;
	}

	public String getUserId(String deviceId)
	{
		return getUserId(deviceId,"");
	}
	
	
}
