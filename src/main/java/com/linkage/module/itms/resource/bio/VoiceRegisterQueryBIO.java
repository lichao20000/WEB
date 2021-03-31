package com.linkage.module.itms.resource.bio;

import java.util.List;
import java.util.Map;

import com.linkage.module.itms.resource.dao.VoiceRegisterQueryDAO;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-11-12
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class VoiceRegisterQueryBIO
{
	private VoiceRegisterQueryDAO dao;
	
	public List<Map> VoiceRegisterQueryInfo(String device_sn,String modelId,String loid, String device_type,
			String enabled, String voip_phone, String status,String reason, String start_time,String end_time,int curPage_splitPage, int num_splitPage){
		return dao.VoiceRegisterQueryInfo(device_sn, modelId, loid, device_type, enabled, voip_phone, status, reason, start_time, end_time, curPage_splitPage, num_splitPage);
	}
	
	public int CountVoiceRegisterQueryInfo(String device_sn,String modelId,String loid, String device_type,
			String enabled, String voip_phone, String status,String reason, String start_time,String end_time,int curPage_splitPage, int num_splitPage){
		return dao.countVoiceRegisterQueryInfo(device_sn, modelId, loid, device_type, enabled, voip_phone, status, reason, start_time, end_time, curPage_splitPage, num_splitPage);
	}
	
	/**
	 * 查询厂商信息
	 * 
	 * @return
	 */
	public Map<String, String> getVendor()
	{
		return dao.getVendor();
	}
	
	public VoiceRegisterQueryDAO getDao()
	{
		return dao;
	}

	
	public void setDao(VoiceRegisterQueryDAO dao)
	{
		this.dao = dao;
	}

	
}
