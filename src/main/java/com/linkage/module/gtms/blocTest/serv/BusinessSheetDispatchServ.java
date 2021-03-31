/*
 * 
 * @author Administrator(工号) Tel:
 * @version 1.0
 * @since 2012年5月15日 09:46:54
 * @category com.linkage.module.gtms.blocTest.serv
 * @copyright 南京联创科技 网管科技部
 *
 */
package com.linkage.module.gtms.blocTest.serv;

import java.util.Map;

import com.linkage.module.gtms.blocTest.obj.VoipSheetOBJ;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;

public interface BusinessSheetDispatchServ {
	
	/**
	 * @param hgwServUserObj
	 * @param hasServUser
	 * @param devId
	 * @param oui
	 * @param devSn
	 * @param operTypeId
	 * @param gw_type
	 * @date 2012年5月15日 15:33:01
	 * @return
	 */
	
	public int netOpen(HgwServUserObj hgwServUserObj, String devId, String devSn, String operTypeId, String gw_type);
	/**
	 * 
	 * @param hgwServUserObj
	 * @param officeId
	 * @param hasServUser
	 * @param strategyObj
	 * @param gw_type
	 * @date 2012年5月15日 
	 * @return
	 */
	
	public int voipOpen(HgwServUserObj hgwServUserObj,VoipSheetOBJ voipSheetOBJ,String deviceId,String devSn,String operTypeId,
			 String gw_type);
	/**
	 * 
	 * @param deviceSn
	 * @param userAccount
	 * @date 2012年5月16日  
	 * @return
	 */
	public String getUserId(String deviceId);
	/**
	 * 查询基本信息
	 * @param deviceSn 设备序列好
	 * @param userAccount 用户帐号
	 * @return
	 */
	public Map<String,Object> getBaseInfo(String deviceSn,String userAccount);
}
