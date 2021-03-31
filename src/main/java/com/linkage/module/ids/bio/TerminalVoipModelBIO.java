
package com.linkage.module.ids.bio;

import java.util.Map;

import com.linkage.module.ids.dao.TerminalVoipModelDAO;

/**
 * @author banyr (Ailk No.)
 * @version 1.0
 * @since 2018-7-30
 * @category com.linkage.module.ids.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class TerminalVoipModelBIO
{

	private TerminalVoipModelDAO dao;

	public Map<String, String> queryDevInfoByDeviceId(String deviceId)
	{
		return dao.queryDevInfoByDeviceId(deviceId);
	}
	
	public TerminalVoipModelDAO getDao()
	{
		return dao;
	}

	public void setDao(TerminalVoipModelDAO dao)
	{
		this.dao = dao;
	}
	
}
