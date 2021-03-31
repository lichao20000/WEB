/**
 * 
 */
package com.linkage.module.gwms.diagnostics.act;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.diagnostics.act.interf.I_DeviceInfoACT;
import com.linkage.module.gwms.diagnostics.bio.interf.I_DeviceInfoBIO;
import com.linkage.module.gwms.diagnostics.obj.PONInfoOBJ;
import com.linkage.module.gwms.obj.gw.DeviceWireInfoObj;
import com.linkage.module.gwms.obj.gw.GwTr069OBJ;
import com.linkage.module.gwms.obj.gw.WanConnSessObj;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.ACSCorba;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-6-25
 * @category com.linkage.module.gwms.diagnostics.act
 * 
 */
public class DeviceInfoACT extends ActionSupport implements I_DeviceInfoACT{
	
	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(DeviceInfoACT.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 注入
	 */
	I_DeviceInfoBIO deviceInfoBIO;

	/**
	 * deviceId
	 */
	private String deviceId = null; 
	
	/**
	 * 系统类型
	 */
	private String gw_type = "1";
	
	/**
	 * userId
	 */
	private String userId = null;
	
	/**
	 * 调用corba返回状态
	 */
	private String corbaMsg = null;
	
	/**
	 * 宽带上网
	 */
	private String wideNetMsg = null;

	/**
	 * 云网超宽带
	 */
	private String cloudNetMsg = null;

	/**
	 * IPTV
	 */
	private String iptvMsg = null;

	/**
	 * VOIP
	 */
	private String voipMsg = null;

	/**
	 * 线路信息
	 */
	private String wireMsg = null;

	/**
	 * 管理通道信息
	 */
	private String tr069Msg = null;

	/**
	 * LAN侧信息
	 */
	private String lanMsg = null;

	/**
	 * WLAN侧信息
	 */
	private String wlanMsg = null;


	/**
	 * 存储查询设备信息
	 */
	Map deviceInfoMap = null;
	
	/**
	 * 网关能力信息
	 */
	Map<String, String> abilityMap = null;
	
	/**
	 * 宽带信息
	 */
	List<WanConnSessObj> wideNetInfoList = null;

	/**
	 * 云网超宽带信息
	 */
	List<WanConnSessObj> cloudNetInfoList = null;

	/**
	 * IPTV
	 */
	List<WanConnSessObj> iptvInfoList = null;
	
	/**
	 * voipInfo
	 */
	List<Map> voipInfoList = null;
	
	/**
	 * 线路信息
	 */
	DeviceWireInfoObj[] wireInfoObjArr = null;
	
	/**
	 * 管理通道信息
	 */
	GwTr069OBJ gwTr069OBJ = null;
	
	/**
	 * LAN测信息
	 */
	List lanEthList = null;
	/**
	 * 检测项
	 */
	List deviceCheckProjectList = null;
	/**
	 * 地址池信息
	 */
	Map gwLanHostconfMap = null;
	
	/**
	 * WLAN测信息
	 */
	List wlanList = null;
	
	/**
	 * ajax
	 */
	private String ajax;
	
	/**
	 * wlan关联设备
	 */
	private List wlanAssoList = null;
	
	/**
	 * lanId
	 */
	private String lanId = null;
	
	/**
	 * lanWlanId
	 */
	private String lanWlanId = null;
	
	//上行方式：1:ADSL 2:Ethernet(LAN) 3:EPON 4:POTS -1:未知
	private String accessType;
	//pon信息
	private PONInfoOBJ[] ponInfoOBJArr = null;
	
	// pon上行类别，分为Epon和Gpon
	private String pon_type = "EPON";
    
    /**
     * VOIP协议类型0-IMS SIP,1-软交换 SIP,2-H.248
     */
    private String voipProtocalTypeStr;
	
	/**
	 * ACS Corba
	 */
	private ACSCorba acsCorba = new ACSCorba();
	
	/**
	 * 入口方法
	 */
	public String execute() throws Exception {
		
		logger.debug("execute()");
		logger.debug("execute:deviceId=>{}",deviceId);
		
		// 页面传gw_type
		//this.gw_type = String.valueOf(LipossGlobals.SystemType());
		
		if(gw_type == null)
		{
			gw_type = "1";
		}
		this.deviceInfoMap = deviceInfoBIO.getDeviceInfo(deviceId,gw_type);
		// GSDX-REQ-ITMS-202000511-LWX-001（终端能力信息故障处理页面补充网关能力信息需求)
		if(Global.GSDX.equals(Global.instAreaShortName)){ 
			this.abilityMap = deviceInfoBIO.getAbilityInfo(deviceId);
		}
		return SUCCESS;
	}
	
	 public String autoCheckInfo() throws Exception {
			
			if(gw_type == null)
			{
				gw_type = "1";
			}
			this.deviceInfoMap = deviceInfoBIO.getDeviceInfo(deviceId,gw_type);
			this.deviceCheckProjectList = deviceInfoBIO.getDeviceCheckProject(gw_type);
			
			return "autoCheckInfo";
		}
	
	
	public String getAllInfo(){
		logger.warn("获取设备信息->业务信息:deviceId=>{},userId=>{}",deviceId,userId);
		
		int rsint = deviceInfoBIO.getDataFromSG(deviceId, 0);
		logger.debug("rsint:{}",rsint);
	    
		if (rsint != 1) {
			//logger.warn("getData sg fail:{}", rsint);
			
			if(null == Global.G_Fault_Map.get(rsint) || "".equals(Global.G_Fault_Map.get(rsint))){
				if(LipossGlobals.inArea(Global.GSDX))
				{
					this.cloudNetMsg =  Global.G_Fault_Map.get(100000).getFaultReason();
				}
				this.wideNetMsg = Global.G_Fault_Map.get(100000).getFaultReason();
				this.iptvMsg = Global.G_Fault_Map.get(100000).getFaultReason();
				this.voipMsg = Global.G_Fault_Map.get(100000).getFaultReason();
				this.wireMsg = Global.G_Fault_Map.get(100000).getFaultReason();
			}else{
				if(LipossGlobals.inArea(Global.GSDX))
				{
					this.cloudNetMsg =  Global.G_Fault_Map.get(rsint).getFaultReason();
				}
				this.wideNetMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
				this.iptvMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
				this.voipMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
				this.wireMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
			}
			
			if (null == this.wideNetMsg) {
				if(LipossGlobals.inArea(Global.GSDX))
				{
					this.cloudNetMsg =  Global.G_Fault_Map.get(100000).getFaultReason();
				}
				this.wideNetMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
				this.iptvMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
				this.voipMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
				this.wireMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
			}
		}else{
			
			// 根据device_id查询session表，查看具体业务
			Map<String, List<Map>> allChannelMap = deviceInfoBIO.getAllChannel(deviceId);
			List<Map> internet_list = allChannelMap.get("INTERNET");
			List<Map> iptv_list = allChannelMap.get("IPTV");
			//List<Map> voip_list = allChannelMap.get("VOIP");
			List<Map> tr069_list = allChannelMap.get("TR069");
			List<Map> cloudNet_list = allChannelMap.get("SPECIAL_SERVICE_VR");
			//宽带信息
			if(internet_list == null || internet_list.size()==0)
			{
				this.wideNetInfoList = null;
			}
			else
			{ 
				logger.warn("【{}】查询宽带信息",deviceId);
				this.wideNetInfoList = deviceInfoBIO.getWideNetInfo(internet_list);
				if(null == this.wideNetInfoList || this.wideNetInfoList.size()<1){
					this.wideNetMsg = "该设备没有开通此业务!";
				}
				logger.warn("【{}】查询宽带信息结束",deviceId);
			}

			if(LipossGlobals.inArea(Global.GSDX))
			{
				if(null == cloudNet_list || cloudNet_list.size()==0)
				{
					logger.warn("【{}】云网超宽带业务不存在",deviceId);
					this.cloudNetInfoList = null;
				}
				else
				{
					logger.warn("【{}】查询云网超宽带信息",deviceId);
					this.cloudNetInfoList = deviceInfoBIO.getWideNetInfo(cloudNet_list);
					if(null == this.cloudNetInfoList || this.cloudNetInfoList.size()<1){
						this.cloudNetMsg = "该设备没有开通此业务!";
					}
					else
					{
						//由于云网超宽带 的账号没有像其他业务一样入到业务通道的name节点，导致采集结果中，业务账号为空
						//实际 InternetGatewayDevice.Services.X_CT-COM_CLOUDVR.UserID  下发到这个节点，故此，直接调用acs获取 然后反向刷新
						acsCorba = new ACSCorba(gw_type);
						String node = "InternetGatewayDevice.Services.X_CT-COM_CLOUDVR.UserID";

						ArrayList<ParameValueOBJ> result = acsCorba.getValue(deviceId, node);
						if(null != result)
						{
							String cloudNetAccount = result.get(0).getValue();
							cloudNetInfoList.get(0).setUsername(cloudNetAccount);
						}
					}
					logger.warn("【{}】查询云网超宽带信息结束",deviceId);
				}
			}


			//iptv
//			if(2!=LipossGlobals.SystemType()){
			if(!Global.GW_TYPE_BBMS.equals(gw_type)||Global.HBDX.equals(Global.instAreaShortName)){
				if(iptv_list == null || iptv_list.size()==0)
				{
					this.iptvInfoList = null;
				}
				else
				{
					logger.warn("【{}】查询iptv信息",deviceId);
					this.iptvInfoList = deviceInfoBIO.getIptvInfo(iptv_list);
					if(null == this.iptvInfoList || this.iptvInfoList.size()<1){
						this.iptvMsg = "该设备没有开通此业务!";
					}
					logger.warn("【{}】查询iptv信息结束",deviceId);
				}
				
				
				//voip
				//rsint = deviceInfoBIO.getDataFromSG(deviceId, 34);
				if (rsint != 1) {
					this.voipMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
					
					if (null == this.voipMsg) { 
						this.voipMsg = Global.G_Fault_Map.get(100000).getFaultReason();
					}
				}else{
//					if(voip_list == null || voip_list.size()==0)
//					{
//						this.voipInfoList = null;
//					}
//					else
//					{
				    //调用方法来获取VOIP信息
					logger.warn("【{}】查询voip信息",deviceId);
				    this.voipInfo(rsint);
				    logger.warn("【{}】查询voip信息结束",deviceId);
//						this.voipInfoList = deviceInfoBIO.getVoipInfo(voip_list);
//						if(null == this.voipInfoList || this.voipInfoList.size()<1){
//							this.voipMsg = "该设备没有开通此业务!";
//						}
//					}
				}
			}
			accessType = String.valueOf(deviceInfoBIO.getAccessType(deviceId));
			if("1".equals(accessType)){//ADSL
				//线路
				this.wireInfoObjArr = deviceInfoBIO.queryDevWireInfo(deviceId,userId);
			}else if("2".equals(accessType)){//LAN
				this.wireInfoObjArr = deviceInfoBIO.queryDevWireInfo(deviceId,userId);
			}else if("3".equals(accessType)){//EPON
				ponInfoOBJArr = deviceInfoBIO.queryPONInfo(deviceId,userId,accessType);
				pon_type = "EPON";
				
		     }else if("4".equals(accessType)){//GPON
				ponInfoOBJArr = deviceInfoBIO.queryPONInfo(deviceId,userId,accessType);
				pon_type = "GPON";
			}else{
				//线路
				this.wireInfoObjArr = deviceInfoBIO.queryDevWireInfo(deviceId,userId);
			}
			
			//管理通道
			//rsint = deviceInfoBIO.getDataFromSG(deviceId, 5);
			
			if (rsint != 1) {
				this.tr069Msg = Global.G_Fault_Map.get(rsint).getFaultReason();
				
				if (null == this.tr069Msg) { 
					this.tr069Msg = Global.G_Fault_Map.get(100000).getFaultReason(); 
				}
			}else{
				if(tr069_list == null || tr069_list.size()==0)
				{
					this.gwTr069OBJ = null;
				}
				else
				{
					this.gwTr069OBJ = deviceInfoBIO.getTr09Info(tr069_list);
				}
			}
			
			//lan、wlan
			/**
			 * 取全部，暂注释
			 */
			//rsint = deviceInfoBIO.getDataFromSG(deviceId, 1);
			
			if (rsint != 1) {
				logger.warn("getData sg fail");
				this.lanMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
				this.wlanMsg = Global.G_Fault_Map.get(rsint).getFaultReason(); 
				if (null == this.lanMsg) { 
					this.lanMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
					this.wlanMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
				}
			}else{
				this.lanEthList = deviceInfoBIO.queryLanEth(deviceId,userId);
			    this.gwLanHostconfMap = deviceInfoBIO.getGwLanHostconf(deviceId);
				this.wlanList = deviceInfoBIO.getWalnData(deviceId,userId);
			 }
		}
		
		logger.warn("获取设备信息->业务信息结束:deviceId=>{},userId=>{}",deviceId,userId);
		return "allInfo";
	}
	
	/**
	 * 宽带上网
	 */
	public String wideNetInfo(){
		
		logger.warn("获取宽带上网信息:deviceId=>{},userId=>{}",deviceId,userId);
		
		int rsint = deviceInfoBIO.getDataFromSG(deviceId, 2);
		//int rsint = 1;
		if (rsint != 1) {
			logger.warn("getData sg fail");
			this.corbaMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
			
			if (null == this.corbaMsg) { 
				this.corbaMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
			}
		}else{
			Map<String, List<Map>> allChannelMap = deviceInfoBIO.getAllChannel(deviceId);
			List<Map> internet_list = allChannelMap.get("INTERNET");
			this.wideNetInfoList = deviceInfoBIO.getWideNetInfo(internet_list);
			if(null == this.wideNetInfoList || this.wideNetInfoList.size()<1){
				this.corbaMsg = "该设备没有开通此业务!";
			}
		}
		logger.warn("获取宽带上网信息结束:deviceId=>{},userId=>{}",deviceId,userId);
		return "wideNetInfo";
	}
	
	/**
	 * IPTV
	 */
	public String iptvInfo(){
		
		logger.debug("iptvInfo()");
		logger.debug("iptvInfo:deviceId=>{},userId=>{}",deviceId,userId);
		
		int rsint = deviceInfoBIO.getDataFromSG(deviceId, 2);
		
		if (rsint != 1) {
			logger.warn("getData sg fail");
			this.corbaMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
			
			if (null == this.corbaMsg) { 
				this.corbaMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
			}
		}else{
			Map<String, List<Map>> allChannelMap = deviceInfoBIO.getAllChannel(deviceId);
			List<Map> iptv_list = allChannelMap.get("IPTV");
			this.iptvInfoList = deviceInfoBIO.getIptvInfo(iptv_list);
			if(null == this.iptvInfoList || this.iptvInfoList.size()<1){
				this.corbaMsg = "该设备没有开通此业务!";
			}
		}		
		
		return "iptvInfo";
	}
	
	/**
	 * VOIP
	 * @param rsint 
	 */
	public String voipInfo(int rsint){
		
		logger.warn("获取voipInfo:deviceId=>{},userId=>{}",deviceId,userId);
		
		//清除缓存
		String voipProtocalType = null;
		voipInfoList = null;
		
		//先获取VOIP协议类型
		//InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.X_CT-COM_ServerType
		//江西的ServerType不是通过采集获得，直接去数据查询，所以去掉采集的部分
		if(Global.JXDX.equals(Global.instAreaShortName)){
			String voipProtocal = deviceInfoBIO.getBssVoipSheetProtocalByDeviceId(deviceId);
		    if(null != voipProtocal && !"".equals(voipProtocal.trim()))
		    {
		        voipProtocalType = voipProtocal;
		    }
		}else{
			acsCorba = new ACSCorba(gw_type);
			
			String node = "";
			// "CUC"; //联通  "CMC"; //移动    "CTC"; //电信
			if(LipossGlobals.getLipossProperty("telecom").equals("CUC"))
			{
				node = "InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.X_CU_ServerType";
			}else if (LipossGlobals.getLipossProperty("telecom").equals("CTC")){
				node =  "InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.X_CT-COM_ServerType";
			}
			
			ArrayList<ParameValueOBJ> result = acsCorba.getValue(deviceId, node);
			if(null == result)
			{
			    //获取设备绑定用户的VOIP的开通协议类型(暂时不处理H.248的情况)
			    String voipProtocal = deviceInfoBIO.getBssVoipSheetProtocalByDeviceId(deviceId);
			    if(null != voipProtocal && !"".equals(voipProtocal.trim()))
			    {
			        voipProtocalType = voipProtocal;
			    }
			}else
			{
				voipProtocalType = result.get(0).getValue();
			}
		}
		//无法获取，则显示无法获取VOIP信息
		if(null == voipProtocalType)
		{
		    this.voipMsg = "设备没有绑定用户或者绑定的用户没有开通VOIP业务";
		}else
        {
			if("H.248".equalsIgnoreCase(voipProtocalType)){
				voipProtocalType = "2";
			}
            // 转换显示信息
            switch (StringUtil.getIntegerValue(voipProtocalType))
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

            //获取设备VOIP链路信息
//            int rsint = deviceInfoBIO.getDataFromSG(deviceId, 2);
//            //logger.warn("rsint:"+rsint);
//            if (rsint != 1)
//            {   
//            	this.corbaMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
//
//                if (null == this.corbaMsg)
//                {
//                    this.corbaMsg = Global.G_Fault_Map.get(100000)
//                            .getFaultReason();
//                }
//            }
//            else
//            {

              //int  rsint = deviceInfoBIO.getDataFromSG(deviceId, 34);
                 if (rsint != 1)
                {
                   this.corbaMsg = Global.G_Fault_Map.get(rsint)
                            .getFaultReason();

                    if (null == this.corbaMsg)
                    {
                        this.corbaMsg = Global.G_Fault_Map.get(100000)
                                .getFaultReason();
                    }
                }
                else
                {
                    Map<String, List<Map>> allChannelMap = deviceInfoBIO
                            .getAllChannel(deviceId);
                    List<Map> voip_list = allChannelMap.get("VOIP");
                    this.voipInfoList = deviceInfoBIO.getVoipInfo(voip_list,voipProtocalType);
                    if (null == this.voipInfoList
                            || this.voipInfoList.size() < 1)
                    {
                        this.corbaMsg = "设备没有绑定用户或者绑定的用户没有开通VOIP业务!";
                    }
                }
//            }
        }
		
		return "voipInfo";
	}
	
	/**
	 * 线路信息
	 */
	public String wireinfoInfo(){
		
		logger.warn("wireinfoInfo:deviceId=>{},userId=>{}",deviceId,userId);
		
		//TODO 节点待定
		int rsint = deviceInfoBIO.getDataFromSG(deviceId, 21);
		
		if (rsint != 1) {
			logger.warn("getData sg fail");
			this.corbaMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
			
			if (null == this.corbaMsg) { 
				this.corbaMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
			}
		}else{
			
			this.wireInfoObjArr = deviceInfoBIO.queryDevWireInfo(deviceId,userId);
		}		
		logger.warn("获取wireinfoInfo结束:deviceId=>{},userId=>{}",deviceId,userId);
		return "wireinfoInfo";
	}
	
	/**
	 * 管理通道
	 */
	public String tr069Info(){
		
		logger.warn("获取tr069Info:deviceId=>{},userId=>{}",deviceId,userId);
		
		//待处理
		int rsint = deviceInfoBIO.getDataFromSG(deviceId, 5);
		
		if (rsint != 1) {
			logger.warn("getData sg fail");
			this.corbaMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
			
			if (null == this.corbaMsg) { 
				this.corbaMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
			}
		}else{
			Map<String, List<Map>> allChannelMap = deviceInfoBIO.getAllChannel(deviceId);
			List<Map> tr069_list = allChannelMap.get("TR069");
			this.gwTr069OBJ = deviceInfoBIO.getTr09Info(tr069_list);
		}		
		
		logger.warn("获取tr069Info结束:deviceId=>{},userId=>{}",deviceId,userId);
		return "tr069Info";
	}
	
	/**
	 * lan
	 */
	public String lanInfo(){
		
		logger.warn("获取lanInfo:deviceId=>{},userId=>{}",deviceId,userId);
		
		//节点待定
		int rsint = deviceInfoBIO.getDataFromSG(deviceId, 1);
		
		if (rsint != 1) {
			logger.warn("getData sg fail");
			this.corbaMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
			
			if (null == this.corbaMsg) { 
				this.corbaMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
			}
		}else{
			this.lanEthList = deviceInfoBIO.queryLanEth(deviceId,userId);
			this.gwLanHostconfMap = deviceInfoBIO.getGwLanHostconf(deviceId);
		}		
		
		logger.warn("获取lanInfo结束:deviceId=>{},userId=>{}",deviceId,userId);
		return "lanInfo";
	}
	
	/**
	 * Wlan
	 */
	public String wlanInfo(){
		
		logger.warn("获取wlanInfo:deviceId=>{},userId=>{}",deviceId,userId);
		
		//节点待定
		int rsint = deviceInfoBIO.getDataFromSG(deviceId, 1);
		
		if (rsint != 1) {
			logger.warn("getData sg fail");
			this.corbaMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
			
			if (null == this.corbaMsg) { 
				this.corbaMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
			}
		}else{
			this.wlanList = deviceInfoBIO.getWalnData(deviceId,userId);
		}		
		
		logger.warn("获取wlanInfo结束:deviceId=>{},userId=>{}",deviceId,userId);
		return "wlanInfo";
	}
	
	/**
	 * 查询WLAN连接设备
	 */
	public String getGwWlanAsso(){
		
		this.wlanAssoList = deviceInfoBIO.getGwWlanAsso(deviceId, lanId, lanWlanId);
		
		if(null == this.wlanAssoList || this.wlanAssoList.size()<1){
			this.corbaMsg = "该无线下面没有连接设备!";
		}
		
		return "wlanAsso";
	}
	
	/**
	 * 终端信息
	 */
	public String devStatInfo(){
		
		return "devStatInfo";
	}
	
	/**
	 * @return the deviceInfoBIO
	 */
	public I_DeviceInfoBIO getDeviceInfoBIO() {
		return deviceInfoBIO;
	}

	/**
	 * @param deviceInfoBIO the deviceInfoBIO to set
	 */
	public void setDeviceInfoBIO(I_DeviceInfoBIO deviceInfoBIO) {
		this.deviceInfoBIO = deviceInfoBIO;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the deviceInfoMap
	 */
	public Map getDeviceInfoMap() {
		return deviceInfoMap;
	}

	/**
	 * @param deviceInfoMap the deviceInfoMap to set
	 */
	public void setDeviceInfoMap(Map deviceInfoMap) {
		this.deviceInfoMap = deviceInfoMap;
	}

	/**
	 * @return the ajax
	 */
	public String getAjax() {
		return ajax;
	}

	/**
	 * @param ajax the ajax to set
	 */
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the wideNetInfoList
	 */
	public List<WanConnSessObj> getWideNetInfoList() {
		return wideNetInfoList;
	}

	/**
	 * @param wideNetInfoList the wideNetInfoList to set
	 */
	public void setWideNetInfoList(List<WanConnSessObj> wideNetInfoList) {
		this.wideNetInfoList = wideNetInfoList;
	}

	/**
	 * @return the iptvInfoList
	 */
	public List<WanConnSessObj> getIptvInfoList() {
		return iptvInfoList;
	}

	/**
	 * @param iptvInfoList the iptvInfoList to set
	 */
	public void setIptvInfoList(List<WanConnSessObj> iptvInfoList) {
		this.iptvInfoList = iptvInfoList;
	}

	/**
	 * @return the voipInfoList
	 */
	public List<Map> getVoipInfoList() {
		return voipInfoList;
	}

	/**
	 * @param voipInfoList the voipInfoList to set
	 */
	public void setVoipInfoList(List<Map> voipInfoList) {
		this.voipInfoList = voipInfoList;
	}

	/**
	 * @return the corbaMsg
	 */
	public String getCorbaMsg() {
		return corbaMsg;
	}

	/**
	 * @param corbaMsg the corbaMsg to set
	 */
	public void setCorbaMsg(String corbaMsg) {
		this.corbaMsg = corbaMsg;
	}

	/**
	 * @return the wireInfoObjArr
	 */
	public DeviceWireInfoObj[] getWireInfoObjArr() {
		return wireInfoObjArr;
	}

	/**
	 * @param wireInfoObjArr the wireInfoObjArr to set
	 */
	public void setWireInfoObjArr(DeviceWireInfoObj[] wireInfoObjArr) {
		this.wireInfoObjArr = wireInfoObjArr;
	}

	/**
	 * @return the lanEthList
	 */
	public List getLanEthList() {
		return lanEthList;
	}

	/**
	 * @param lanEthList the lanEthList to set
	 */
	public void setLanEthList(List lanEthList) {
		this.lanEthList = lanEthList;
	}

	/**
	 * @return the wlanList
	 */
	public List getWlanList() {
		return wlanList;
	}

	/**
	 * @param wlanList the wlanList to set
	 */
	public void setWlanList(List wlanList) {
		this.wlanList = wlanList;
	}

	/**
	 * @return the gwTr069OBJ
	 */
	public GwTr069OBJ getGwTr069OBJ() {
		return gwTr069OBJ;
	}

	/**
	 * @param gwTr069OBJ the gwTr069OBJ to set
	 */
	public void setGwTr069OBJ(GwTr069OBJ gwTr069OBJ) {
		this.gwTr069OBJ = gwTr069OBJ;
	}

	/**
	 * @return the lanId
	 */
	public String getLanId() {
		return lanId;
	}

	/**
	 * @param lanId the lanId to set
	 */
	public void setLanId(String lanId) {
		this.lanId = lanId;
	}

	/**
	 * @return the lanWlanId
	 */
	public String getLanWlanId() {
		return lanWlanId;
	}

	/**
	 * @param lanWlanId the lanWlanId to set
	 */
	public void setLanWlanId(String lanWlanId) {
		this.lanWlanId = lanWlanId;
	}

	/**
	 * @return the wlanAssoList
	 */
	public List getWlanAssoList() {
		return wlanAssoList;
	}

	/**
	 * @param wlanAssoList the wlanAssoList to set
	 */
	public void setWlanAssoList(List wlanAssoList) {
		this.wlanAssoList = wlanAssoList;
	}

	/**
	 * @return the gwLanHostconfMap
	 */
	public Map getGwLanHostconfMap() {
		return gwLanHostconfMap;
	}

	/**
	 * @param gwLanHostconfMap the gwLanHostconfMap to set
	 */
	public void setGwLanHostconfMap(Map gwLanHostconfMap) {
		this.gwLanHostconfMap = gwLanHostconfMap;
	}

	/**
	 * @return the iptvMsg
	 */
	public String getIptvMsg() {
		return iptvMsg;
	}

	/**
	 * @param iptvMsg the iptvMsg to set
	 */
	public void setIptvMsg(String iptvMsg) {
		this.iptvMsg = iptvMsg;
	}

	/**
	 * @return the lanMsg
	 */
	public String getLanMsg() {
		return lanMsg;
	}

	/**
	 * @param lanMsg the lanMsg to set
	 */
	public void setLanMsg(String lanMsg) {
		this.lanMsg = lanMsg;
	}

	/**
	 * @return the tr069Msg
	 */
	public String getTr069Msg() {
		return tr069Msg;
	}

	/**
	 * @param tr069Msg the tr069Msg to set
	 */
	public void setTr069Msg(String tr069Msg) {
		this.tr069Msg = tr069Msg;
	}

	/**
	 * @return the voipMsg
	 */
	public String getVoipMsg() {
		return voipMsg;
	}

	/**
	 * @param voipMsg the voipMsg to set
	 */
	public void setVoipMsg(String voipMsg) {
		this.voipMsg = voipMsg;
	}

	/**
	 * @return the wideNetMsg
	 */
	public String getWideNetMsg() {
		return wideNetMsg;
	}

	/**
	 * @param wideNetMsg the wideNetMsg to set
	 */
	public void setWideNetMsg(String wideNetMsg) {
		this.wideNetMsg = wideNetMsg;
	}

	/**
	 * @return the wireMsg
	 */
	public String getWireMsg() {
		return wireMsg;
	}

	/**
	 * @param wireMsg the wireMsg to set
	 */
	public void setWireMsg(String wireMsg) {
		this.wireMsg = wireMsg;
	}

	/**
	 * @return the wlanMsg
	 */
	public String getWlanMsg() {
		return wlanMsg;
	}

	/**
	 * @param wlanMsg the wlanMsg to set
	 */
	public void setWlanMsg(String wlanMsg) {
		this.wlanMsg = wlanMsg;
	}

	/**
	 * @return the gw_type
	 */
	public String getGw_type() {
		return gw_type;
	}

	/**
	 * @param gw_type the gw_type to set
	 */
	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
	/**
	 * @return the accessType
	 */
	public String getAccessType() {
		return accessType;
	}

	
	/**
	 * @return the ponInfoOBJArr
	 */
	public PONInfoOBJ[] getPonInfoOBJArr()
	{
		return ponInfoOBJArr;
	}

	
	/**
	 * @param ponInfoOBJArr the ponInfoOBJArr to set
	 */
	public void setPonInfoOBJArr(PONInfoOBJ[] ponInfoOBJArr)
	{
		this.ponInfoOBJArr = ponInfoOBJArr;
	}

	public String getPon_type() {
		return pon_type;
	}

	public void setPon_type(String pon_type) {
		this.pon_type = pon_type;
	}

    /**
     * 获取voipProtocalTypeStr
     * @return String voipProtocalTypeStr
     */
    public String getVoipProtocalTypeStr()
    {
        return voipProtocalTypeStr;
    }

    /**
     * 设置voipProtocalTypeStr
     * @param String voipProtocalTypeStr
     */
    public void setVoipProtocalTypeStr(String voipProtocalTypeStr)
    {
        this.voipProtocalTypeStr = voipProtocalTypeStr;
    }

	public List getDeviceCheckProjectList() {
		return deviceCheckProjectList;
	}

	public void setDeviceCheckProjectList(List deviceCheckProjectList) {
		this.deviceCheckProjectList = deviceCheckProjectList;
	}

	public Map<String, String> getAbilityMap() {
		return abilityMap;
	}

	public void setAbilityMap(Map<String, String> abilityMap) {
		this.abilityMap = abilityMap;
	}

	public String getCloudNetMsg() {
		return cloudNetMsg;
	}

	public void setCloudNetMsg(String cloudNetMsg) {
		this.cloudNetMsg = cloudNetMsg;
	}

	public List<WanConnSessObj> getCloudNetInfoList() {
		return cloudNetInfoList;
	}

	public void setCloudNetInfoList(List<WanConnSessObj> cloudNetInfoList) {
		this.cloudNetInfoList = cloudNetInfoList;
	}
}
