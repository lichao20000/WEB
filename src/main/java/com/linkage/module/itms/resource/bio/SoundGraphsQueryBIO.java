package com.linkage.module.itms.resource.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.act.SoundGraphsQueryACT;
import com.linkage.module.itms.resource.dao.SoundGraphsQueryDAO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-2-15
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class SoundGraphsQueryBIO
{
	private SoundGraphsQueryDAO dao;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(SoundGraphsQueryBIO.class);
	
	
	
	public List<Map> query(String username1,String startOpenDate, String endOpenDate, String usernameType,
			String openstatus, String username, int user_id, String deviceid,
			String digit_map,int curPage_splitPage, int num_splitPage)
	{
		logger.debug("query({},{},{},{},{},{},{},{})", new Object[] {
				startOpenDate, endOpenDate, usernameType, openstatus, username ,user_id,deviceid,digit_map});
		List<Map> list=dao.QueryList(username1,startOpenDate, endOpenDate, usernameType, openstatus, username, deviceid, curPage_splitPage, num_splitPage);
		return list;
	}
	public int getMaxPage_splitPage(String username1,String startOpenDate, String endOpenDate, String usernameType,
			String openstatus, String username, int user_id, String deviceid,
			String digit_map,int curPage_splitPage, int num_splitPage)
	{
		return dao.getquerypaging(username1,startOpenDate, endOpenDate, usernameType, openstatus, username, deviceid, curPage_splitPage, num_splitPage);
	}
	/**
	 * "1" 逻辑SN, "2" 宽带号码, "3" IPTV号码, "4" VoIP认证号码, "5" VoIP电话号码
	 * @param usernameType
	 * @param username
	 * @param gw_type
	 * @return
	 */
	public List<Map> userid(String usernameType,String username,String gw_type)
	{
		List<Map> list=null;
		if(false == StringUtil.IsEmpty(username))
		{
			if ("1".equals(usernameType)) {
				list=dao.Loid(usernameType, username, gw_type);
			} 
			else if ("2".equals(usernameType)) {
				list=dao.UserId(usernameType, username, gw_type);
			} 
			else if ("3".equals(usernameType)) {
				list=dao.UserId(usernameType, username, gw_type);
			} 
			else if ("4".equals(usernameType)) {
				list=dao.UserIdVoip(usernameType, username, gw_type);
			} 
			else if ("5".equals(usernameType)) {
				list=dao.UserIdVoip(usernameType, username, gw_type);
			} 
		}
		return list;
	}
	
	public List<Map> deviceid(String usernameType,String username,int user_id)
	{
		List<Map> list =dao.DeviceId(usernameType, username, user_id);
		return list;
	}
	
	/**
	 * 更多信息查询
	 */
	public List<Map> number(String digit_map)
	{
		List<Map> list=dao.number(digit_map);
		return list;
	}
	
	public SoundGraphsQueryDAO getDao()
	{
		return dao;
	}
	
	public void setDao(SoundGraphsQueryDAO dao)
	{
		this.dao = dao;
	}
	
}
