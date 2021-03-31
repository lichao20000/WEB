/**
 * 
 */
package com.linkage.module.gwms.diagnostics.bio.interf;

import java.util.List;
import java.util.Map;

import com.linkage.module.gwms.diagnostics.obj.PONInfoOBJ;
import com.linkage.module.gwms.obj.gw.DeviceWireInfoObj;
import com.linkage.module.gwms.obj.gw.GwTr069OBJ;
import com.linkage.module.gwms.obj.gw.WanConnSessObj;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-6-25
 * @category com.linkage.module.gwms.diagnostics.bio.interf
 * 
 */
public interface I_DeviceInfoBIO {
	
	/**
	 * 获取设备的相关信息，包括用户信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public Map<String,String> getDeviceInfo(String deviceId, String gw_type);
	
	/**
	 * 获取网关能力
	 * 
	 * @param deviceId
	 * @return
	 */
	public Map<String, String> getAbilityInfo(String deviceId);
	
	/**
	 * 获取宽带信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public List<WanConnSessObj> getWideNetInfo(List<Map> list);
	
	/**
	 * 获取IPTV相关信息
	 * 
	 * @param deviceId
	 * @param userId
	 * @return
	 */
	public List<WanConnSessObj> getIptvInfo(List<Map> list);
	
	/**
	 * 获取voip信息
	 * 
	 * @param deviceId
	 * @param userId
	 * @return
	 */
	public List<Map> getVoipInfo(List<Map> list,String voipProtocalType);
	
	/**
     * <p>
     * [获取设备绑定用户的VOIP的开通协议类型]
     * </p>
     * @param deviceId
     * @return
     */
	public String getBssVoipSheetProtocalByDeviceId(String deviceId);
	
	/**
	 * 调用corba
	 * 
	 * @param deviceId
	 * @param type
	 * @return
	 */
	public int getDataFromSG(String deviceId, int type);
	
	/**
	 * 获取线路信息
	 * 
	 * @param deviceId
	 * @param userId
	 * 
	 * @return
	 */
	public DeviceWireInfoObj[] queryDevWireInfo(String deviceId, String userId);
	
	/**
	 * 获取LAN侧信息
	 * 
	 * @param deviceId
	 * @param userId
	 * 
	 * @return
	 */
	public List queryLanEth(String deviceId,String userId);
	
	/**
	 * 获取Wlan 信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public List getWalnData(String deviceId,String userId);
	
	/**
	 * 查询设备管理通道相关信息
	 * 
	 * @param deviceId
	 * @param userId
	 * @return
	 */
	public GwTr069OBJ getTr09Info(List<Map> list);
	
	/**
	 * 查询设备wlan关联设备情况 
	 * 
	 * @param deviceId
	 * @param lanId
	 * @param lanWlanId
	 * @return
	 */
	public List getGwWlanAsso(String deviceId,String lanId,String lanWlanId);
	
	/**
	 * 查询地址池
	 * 
	 * @param deviceId
	 * @return
	 */
	public Map getGwLanHostconf(String deviceId);
	
	/**
	 * 获取设备的上行方式
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-11-11
	 * @return String
	 */
	public int getAccessType(String deviceId);

	/**
	 * 查询PON设备信息
	 *
	 * @author wangsenbo
	 * @date Nov 4, 2010
	 * @param 
	 * @return PONInfoOBJ[]
	 */
	public PONInfoOBJ[] queryPONInfo(String deviceId, String userId, String accessType);

	public Map<String, List<Map>> getAllChannel(String deviceId);

	public List getDeviceCheckProject(String gw_type);
}
