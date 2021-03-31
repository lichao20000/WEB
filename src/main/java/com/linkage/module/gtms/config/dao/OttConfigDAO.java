package com.linkage.module.gtms.config.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author xiangzl (Ailk No.)
 * @version 1.0
 * @since 2014-4-1
 * @category com.linkage.module.gtms.config.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public interface OttConfigDAO
{

	List<HashMap<String,String>> isExists(String searchType, String username, String gwType);
	
	public int openOttServ(long user_id,Map<String,String> userInfo, String bind_port);
	
	public boolean isOpenOtt(String user_id);
	
	public int deleteOttServ(String user_id);
	
	public int updateNetBindPort(String user_id, String bind_port);

	Map<String, String> getUserInfo(String userId);
}
