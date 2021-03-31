package com.linkage.module.gtms.config.dao;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-5-22
 * @category com.linkage.module.gtms.config.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public interface ConfigMaxTerminalDao
{
	
	public Map<String,String>  queryMaxTerminal(String device_id);
	
	public String updateMaxTerminal(String device_id, String mode, String total_number);
	
	public Map<String,String> queryStrategy(String id);
	
	public Map<String,String> queryStrategyByDeviceId(String device_id);
}
