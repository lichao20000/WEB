package com.linkage.module.gtms.config.serv;

import com.linkage.module.gtms.config.dao.BatchConfigWifiDAO;

/**
 * 
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2019年9月27日
 * @category com.linkage.module.gtms.config.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class BatchConfigWifiBIO
{
	private   BatchConfigWifiDAO dao = new BatchConfigWifiDAO();
	
	public String doConfig(String deviceIds , int wifiEnable){
		return dao.doConfig(deviceIds, wifiEnable);
	}
	
	public BatchConfigWifiDAO getDao()
	{
		return dao;
	}
	public void setDao(BatchConfigWifiDAO dao)
	{
		this.dao = dao;
	}
}
