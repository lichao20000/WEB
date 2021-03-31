package com.linkage.module.gtms.config.serv;

/**
 * 
 * @author xiangzl (Ailk No.)
 * @version 1.0
 * @since 2014-4-1
 * @category com.linkage.module.gtms.config.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public interface OttConfigServ
{

	public String getServUserInfo(String searchType, String username, String gw_type);

	public String openOtt(String username, String userId, String gw_type);

	public String closeOtt(String username, String userId, String gw_type);
	
}
