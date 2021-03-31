
package com.linkage.module.gwms.dao;

import java.util.HashMap;
import java.util.Map;

import com.linkage.liposs.manaconf.PropertiesFileManager;
/**
 * 数据源类型配置文件解析类
 * @author jj (AILK No.)
 * @version 1.0
 * @since 2013-12-20
 * @category com.linkage.module.gwms.dao
 * @copyright AILK NBS-Network Mgt. RD Dept.
 */
public class DataSourceTypeCfgPropertiesManager extends PropertiesFileManager
{

	private static DataSourceTypeCfgPropertiesManager cfgPropertiesManager = null;
	
    public static DataSourceTypeCfgPropertiesManager getInstance()
    {
    	if(cfgPropertiesManager==null)
    	{
    		cfgPropertiesManager = new DataSourceTypeCfgPropertiesManager(
    				"datasource_type_cfg.properties");
    	}
    	return cfgPropertiesManager;
    }
	private DataSourceTypeCfgPropertiesManager(String filename)
	{
		super(filename);
	}

	public Map<String, String> readAll()
	{
		Map<String, String> map = new HashMap<String, String>();
		for (Object key : this.getProperties().keySet())
		{
			map.put((String) key, this.getProperties().getProperty((String) key));
		}
		return map;
	}

}
