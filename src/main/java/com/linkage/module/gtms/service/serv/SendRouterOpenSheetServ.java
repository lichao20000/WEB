package com.linkage.module.gtms.service.serv;


public interface SendRouterOpenSheetServ {
	
	/**
	 * 路由业务
	 * 
	 * @param servTypeId
	 * @param operateType   1：开户  3：销户
	 * @param cityId
	 * @param netUsername
	 * @param netPassword
	 * @param gw_type
	 * @return
	 */
	public String sendSheet(String servTypeId, String operateType, String cityId,
			String netUsername, String netPassword, String gw_type);

}
