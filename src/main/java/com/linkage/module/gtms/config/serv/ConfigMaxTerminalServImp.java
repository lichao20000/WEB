
package com.linkage.module.gtms.config.serv;

import java.util.Map;

import com.linkage.module.gtms.config.dao.ConfigMaxTerminalDaoImp;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-5-22
 * @category com.linkage.module.gtms.config.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class ConfigMaxTerminalServImp implements ConfigMaxTerminalServ
{

	private ConfigMaxTerminalDaoImp configmtdaoimp;

	public Map<String, String> queryMaxTerminal(String device_id)
	{
		return configmtdaoimp.queryMaxTerminal(device_id);
	}

	public String updateMaxTerminal(String device_id, String mode, String total_number)
	{
		return configmtdaoimp.updateMaxTerminal(device_id, mode, total_number);
	}

	public Map<String, String> queryStrategyByDeviceId(String device_id)
	{
		return configmtdaoimp.queryStrategyByDeviceId(device_id);
	}

	public Map<String, String> queryStrategy(String id)
	{
		return configmtdaoimp.queryStrategy(id);
	}

	public void setConfigmtdaoimp(ConfigMaxTerminalDaoImp configmtdaoimp)
	{
		this.configmtdaoimp = configmtdaoimp;
	}

	public ConfigMaxTerminalDaoImp getConfigmtdaoimp()
	{
		return configmtdaoimp;
	}
}
