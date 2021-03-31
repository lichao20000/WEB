package com.linkage.module.gwms.diagnostics.act;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.diagnostics.bio.VoipDeviceInfoBIO;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.corba.ACSCorba;
import com.opensymphony.xwork2.ActionSupport;

public class VoipDeviceInfoACT extends ActionSupport {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(VoipDeviceInfoACT.class);
	
	/** 设备ID */
	private String deviceId = null; 
	
	/** 系统类型 */
	private String gw_type = "1";
	
	/** userId */
	private String userId = null;
	
	/** 存储查询设备信息 */
	Map deviceInfoMap = null;
	
	/** 作为调用corba失败返回的信息 */
	private String corbaMsg = null;
	
	/** 上行方式（接入方式）LAN上行或ADSL上行*/
	private String accessType = null;
	
	/** voipInfoMsg */
	private String voipInfoMsg = null;
	
	/** VOIP协议类型0-IMS SIP,1-软交换 SIP,2-H.248 */
	private String voipProtocalTypeStr = null;
	
	/** wanConfig的列表 */
	private List<Map<String, String>> wanConfigList = null;
	
	/** VOIP的列表 */
	private List<Map<String, String>> voipInfoList = null;
	
	
	private VoipDeviceInfoBIO voipDeviceInfoBIO ;
	
	/** ACS Corba */
    private ACSCorba acsCorba = new ACSCorba(gw_type);
	
	
	

	public String getDeviceInfo() {
		
		logger.debug("getDeviceInfo()");
		logger.debug("execute:deviceId=>{}",deviceId);
		
		if(null == gw_type)	{
			gw_type = "1";
		}
		deviceInfoMap = voipDeviceInfoBIO.getDeviceInfo(deviceId,gw_type);
		
		return "deviceInfo";
	}
	
	
	/**
	 * 获取VoIP采集信息
	 * 
	 * @return
	 */
	public String getVoIPInfo(){
		
		logger.debug("getVoIPInfo({})",deviceId);
		
		if ("".equals(deviceId) || null == deviceId) {
			corbaMsg = "此设备不存在，请确认！";
			voipInfoMsg = corbaMsg;
			return "voipInfo";
		}
		
		// SG
		//通过Corba 采集WAN链接
		int rsint = voipDeviceInfoBIO.getSuperCorba(deviceId, 2);

		// Corba 获取WAN链接失败 直接返回页面，不再做实时采集了。
		if (rsint!= 1) {
			logger.warn("getData sg fail");
			if(null == Global.G_Fault_Map.get(rsint)){
				corbaMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
			}else {
				corbaMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
			}
			if (null == corbaMsg) { 
				corbaMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
			}
			voipInfoMsg = corbaMsg;
			return "voipInfo";
		}
		
		// 获取VoIP的 servList
		List<Map> voip_list = voipDeviceInfoBIO.getAllChannel(deviceId);
		
		//获取采集完的数据
		wanConfigList = voipDeviceInfoBIO.getData(voip_list);
		
		if (null == wanConfigList || wanConfigList.size() < 0) { //如果取值失败或者没有获取到相关信息
			corbaMsg = "当前设备上无VOIP相关连接（PVC或VLANID）！";
		}
		
//		//获取上行方式
//		String accessType = voipDeviceInfoBIO.getAcceessType();
//		if (accessType == null) {
//			logger.warn("从设备上取得的上行方式为空，默认设为DSL");
//			accessType = "DSL";
//		}
//		this.accessType = accessType;
		
        String voipProtocalType = null;  // 定义个局部变量，用于存放VoIP的协议类型
        
        /** 先获取VOIP协议类型，判断协议类型节点是否存在 */
        if(Global.JSDX.equals(Global.instAreaShortName)){
        	ArrayList<ParameValueOBJ> result = acsCorba.getValue(deviceId, "InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.X_CT-COM_ServerType");
            
            if (null == result) { /** 如果协议类型节点不存在，判断设备是否绑定了用户且是否开通了VOIP业务 */ 
                String voipProtocal = voipDeviceInfoBIO.getBssVoipSheetProtocalByDeviceId(deviceId);
                if (null != voipProtocal && !"".equals(voipProtocal.trim())) {
                	voipProtocalType = voipProtocal;
                }
            } else { /** VOIP协议类型节点存在，则读取相应信息 */
            	voipProtocalType = result.get(0).getValue();
            }
        }else{
        	voipProtocalType = voipDeviceInfoBIO.getBssVoipSheetProtocalByDeviceId(deviceId);
        }
        
        if(null == voipProtocalType || "".equals(voipProtocalType)){
        	voipInfoMsg = "设备没有绑定用户或者绑定的用户没有开通VOIP业务!";
        } else {
        	// 转换显示信息
            switch (Integer.parseInt(voipProtocalType))
            {
                case 0:
                {
                    voipProtocalTypeStr = "IMS SIP";
                    break;
                }
                case 1:
                {
                    voipProtocalTypeStr = "软交换 SIP";
                    break;
                }
                case 2:
                {
                    voipProtocalTypeStr = "H.248";
                    break;
                }
            }
        	
        	/** 采集VOIP链接 */
            rsint = voipDeviceInfoBIO.getSuperCorba(deviceId, 34);  
            
            if (rsint!= 1) {
                logger.warn("getData sg fail");
                if (null == Global.G_Fault_Map.get(rsint)) {
                	corbaMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
				} else {
					corbaMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
				}
                
                if (null == corbaMsg) { 
                    corbaMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
                }
                voipInfoMsg = corbaMsg;
                return "voipInfo";
            }
            
            
            if("2".equals(voipProtocalType)){  /** H.248 */
            	voipInfoList = voipDeviceInfoBIO.getVoipInfoH248(deviceId);
            } else {
            	voipInfoList = voipDeviceInfoBIO.getVoipInfo(deviceId);
    		}
            
            if (null == voipInfoList || voipInfoList.size() < 1)
            {
                // 如果取值失败
                voipInfoMsg = "设备没有绑定用户或者绑定的用户没有开通VOIP业务!";
                logger.debug("设备没有绑定用户或者绑定的用户没有开通VOIP业务!");
            }
		}
        
        return "voipInfo";
	}
	
	public String getDeviceId() {
		return deviceId;
	}


	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}


	public String getGw_type() {
		return gw_type;
	}


	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public Map getDeviceInfoMap() {
		return deviceInfoMap;
	}


	public void setDeviceInfoMap(Map deviceInfoMap) {
		this.deviceInfoMap = deviceInfoMap;
	}


	public VoipDeviceInfoBIO getVoipDeviceInfoBIO() {
		return voipDeviceInfoBIO;
	}


	public void setVoipDeviceInfoBIO(VoipDeviceInfoBIO voipDeviceInfoBIO) {
		this.voipDeviceInfoBIO = voipDeviceInfoBIO;
	}


	public String getCorbaMsg() {
		return corbaMsg;
	}


	public void setCorbaMsg(String corbaMsg) {
		this.corbaMsg = corbaMsg;
	}


	public String getAccessType() {
		return accessType;
	}


	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}


	public String getVoipInfoMsg() {
		return voipInfoMsg;
	}


	public void setVoipInfoMsg(String voipInfoMsg) {
		this.voipInfoMsg = voipInfoMsg;
	}


	public List<Map<String, String>> getWanConfigList() {
		return wanConfigList;
	}


	public void setWanConfigList(List<Map<String, String>> wanConfigList) {
		this.wanConfigList = wanConfigList;
	}


	public List<Map<String, String>> getVoipInfoList() {
		return voipInfoList;
	}


	public void setVoipInfoList(List<Map<String, String>> voipInfoList) {
		this.voipInfoList = voipInfoList;
	}


	public String getVoipProtocalTypeStr() {
		return voipProtocalTypeStr;
	}


	public void setVoipProtocalTypeStr(String voipProtocalTypeStr) {
		this.voipProtocalTypeStr = voipProtocalTypeStr;
	}
	
	
}
