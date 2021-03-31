/**
 * 
 */
package com.linkage.module.gwms.diagnostics.dao.interf;

import java.util.List;
import java.util.Map;

import com.linkage.module.gwms.diagnostics.obj.PONInfoOBJ;
import com.linkage.module.gwms.obj.gw.DeviceWireInfoObj;
import com.linkage.module.gwms.obj.gw.GwTr069OBJ;
import com.linkage.module.gwms.obj.gw.LanHostObj;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileH248OBJ;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileLineObj;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileObj;
import com.linkage.module.gwms.obj.gw.WanConnObj;
import com.linkage.module.gwms.obj.gw.WanConnSessObj;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-6-25
 * @category com.linkage.module.gwms.diagnostics.dao.interf
 * 
 */
public interface I_DeviceInfoDAO {
	/**
	 * 
	 * 
	 * @param deviceId
	 * @return
	 */
	public List getDeviceInfo(String deviceId);
	
	/**
	 * 
	 * 
	 * @param deviceId
	 * @return
	 */
	public Map<String, String> getAbilityInfo(String deviceId);
	
	/**
	 * 查询IP
	 * 
	 * @param deviceId
	 * @return
	 */
	public String getDeviceIp(String deviceId);
	
	/**
	 * 根据设备oui，设备序列号查询用户
	 * 
	 * @param oui
	 * @param device_serialnumber
	 * @return
	 */
	public List getDeviceHUserInfo(String deviceId );
	
	/**
	 * <p>
	 * [根据用户ID，查询用户开户的VOIP工单信息]
	 * </p>
	 * @param userId
	 * @return
	 */
	public List getBssVoipSheet(String userId);
	
	/**
	 * 查询商务领航用户信息
	 *  
	 * @param deviceId
	 * @return
	 */
	public List getDeviceEUserInfo(String deviceId);
	
	/**
	 * 根据特定PVC获取相关连接
	 * 
	 * @param
	 * @author qxq(4174)
	 * @date 2009-7-14
	 * @return WanConnObj
	 */
	public WanConnObj queryDevWanConn(Map map);
	
	/**
	 * 根据特定PVC获取相关连接
	 * 
	 * @param
	 * @author qxq(4174)
	 * @date 2009-7-14
	 * @return WanConnObj
	 */
	public WanConnObj queryDevWanConn(String deviceId, String vpiid,String vciid);
	
	/**
	 * 取出特定(设备)WanConnection下面所有的Sesstion连接属性
	 * 
	 * @param
	 * @author qxq(4174)
	 * @date 2009-7-14
	 * @return WanConnSessObj[]
	 */
	public WanConnSessObj[] queryDevWanConnSession(WanConnObj wanConnObj) ;
	
	/**
	 * 不根据WanConn,直接根据数据库记录取得WanConnSessObj对象
	 * @param map
	 * @author chenjie
	 * @date 2013-10-28
	 * @return WanConnSessObj[]
	 */
	public WanConnSessObj[] queryDevWanConnSession(Map map) ;
	
	/**
	 * 根据特定PVC获取相关连接
	 * 
	 * @param
	 * @author qxq(4174)
	 * @date 2009-7-14
	 * @return WanConnObj
	 */
	public WanConnObj queryDevWanConnByVlan(String deviceId, String vlanId);
	
	/**
	 * 获取业务用户ID,根据设备ID
	 * 
	 * @param 
	 * @author qxq(4174)
	 * @date 2009-7-14
	 * @return String
	 */
	public String getUserId(String deviceId);
	
	/**
	 * 获取业务对应的pvc值，从配置表中取(考虑到没有办法确定设备用哪个PVC上网)
	 * 
	 * @param
	 * @author qxq(4174)
	 * @date 2009-7-14
	 * @return String[]
	 */
	public String[] getServPvc(String servType);
	
	/**
	 * 获取业务对应的pvc值，从配置表中取(考虑到没有办法确定设备用哪个PVC上网)
	 * 
	 * @param
	 * @author qxq(4174)
	 * @date 2009-7-14
	 * @return String[]
	 */
	public String[] getServPvc(String servType[]);
	
	/**
	 * 获取业务用户的PVC
	 * 
	 * @param userId
	 * @param servTypeId
	 * 
	 * @author qxq(4174)
	 * @date 2009-7-14
	 * @return
	 */
	public HgwServUserObj getUserInfo(String userId, String servTypeId);
	
	/**
	 * 获取指定device_id的VOIP节点，不包含线路信息
	 * 
	 * @param deviceId
	 * @return
	 */
	//public VoiceServiceProfileObj[] getVoipProf(String deviceId);
	public VoiceServiceProfileObj[] getVoipProf(Map map);
	/**
	 * 获取指定GwVoipProfOBJ的VOIP所有Line节点
	 * 
	 * @param deviceId
	 * @return
	 */
	public VoiceServiceProfileLineObj[] getVoipProfLine(VoiceServiceProfileObj voipProf);
	/**
	 * 获取指定GwVoipProfOBJ的VOIP所有Line节点
	 * 
	 * @param deviceId
	 * @return
	 */
	public VoiceServiceProfileLineObj[] getVoipProfLineH(VoiceServiceProfileH248OBJ voipProf);
	/**
	 * 获取线路信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public DeviceWireInfoObj[] queryDevWireInfo(String deviceId);
	
	/**
	 * 获取LAN侧信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public LanHostObj[] queryLanHost(String deviceId);
	
	/**
	 * 获取Wlan 信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public List getWalnData(String deviceId);
	
	/**
	 * 获取tr069信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public GwTr069OBJ getTr09Info(String deviceId);
	
	/**
	 * 查询cpe访问ACS的用户名和密码,以及ACS连CPE的用户名和密码
	 * 
	 * @param deviceId
	 * @return
	 */
	public List getDeviceTr069Username(String deviceId);
	
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
	public List getGwLanHostconf(String deviceId);
	
	/**
	 * 获取LAN侧信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public List queryLanEth(String deviceId);

	/**
	 * 查询PON设备信息
	 *
	 * @author wangsenbo
	 * @date Nov 4, 2010
	 * @param 
	 * @return PONInfoOBJ[]
	 */
	public PONInfoOBJ[] queryPONInfo(String deviceId,String accessType);

	public List<Map> getAllChannel(String device_id);
	/**
	 * 
	 * @param map
	 * @return
	 */
	public VoiceServiceProfileH248OBJ[] getVoipProfH(Map map);
	
	public List getDeviceCheckProject(String gw_type);
}
