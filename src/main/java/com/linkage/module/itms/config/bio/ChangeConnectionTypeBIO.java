package com.linkage.module.itms.config.bio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.ConstantClass;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.cao.gw.WanRelatedCAO;
import com.linkage.module.gwms.dao.gw.LanEthDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.obj.gw.LanEthObj;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.corba.obj.PreServInfoOBJ;
import com.linkage.module.itms.config.dao.ChangeConnectionTypeDAO;
import com.linkage.module.itms.config.util.ChangeConnectionNetForAHCorba;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ChangeConnectionTypeBIO
{
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ChangeConnectionTypeBIO.class);
	private ChangeConnectionTypeDAO dao;
	private LanEthDAO lanEthDAO;
	private WanRelatedCAO wanCAO;
	private String vlanId;
	
	/**
	 * 宁夏电信查询方法  需要带vlanid查询
	 * @param device_id
	 * @param gw_type
	 * @param vlanId
	 * @return
	 */
	public Map checkBindAndService(String deviceId,String cityId, String gw_type, String vlanId){
		this.vlanId = vlanId;
		return checkBindAndService(deviceId, cityId, gw_type);
	}

	/**
	 * 1、检查设备是否有绑定用户
	 * 2、有绑定用户，则判断该用户是否有上网业务
	 * 3、根据上行方式获取pvc或vlan
	 * @param deviceId
	 * @return
	 */
	public Map checkBindAndService(String deviceId,String cityId, String gw_type){
		StringBuffer strResl = new StringBuffer();
		Map resltMap = new HashMap<String, String>();
 		List<Map> isBindList = null;
 		if (LipossGlobals.inArea(Global.NXDX)) {
 			isBindList = dao.checkService(deviceId, gw_type, vlanId);
 		}
 		else {
 			isBindList = dao.checkService(deviceId, gw_type);
 		}
		if(null == isBindList || isBindList.size() == 0){
			logger.warn("设备未绑定用户");
			strResl.append("0;此设备不存在上网业务！");
			resltMap.put("flag","0");
		}
		else{
			resltMap = (Map) isBindList.get(0);
			String wan_type_str = resltMap.get("wan_type")+"";
			//如果是ADSL上行，获取PVC
			if(resltMap.get("access_style_id")!=null && resltMap.get("access_style_id").equals("1")){
				cityId = CityDAO.getLocationCityIdByCityId(cityId);
				List<Map> pvcList = dao.getPVC(cityId);
				if(pvcList!=null)
				{
					logger.warn("pvc list size=="+pvcList.size());
					resltMap.put("pvcList", pvcList);
				}
			}
			if("1".equals(wan_type_str)){  // 1：表示桥接，2：表示路由
				strResl.append(resltMap.get("access_style_id")+";成功");
				logger.warn("该设备绑定的用户存在桥接上网业务");
				resltMap.put("flag","1");
				
				
			}else {
				logger.warn("该设备绑定的用户存在路由上网业务");
				strResl.append(resltMap.get("access_style_id")+";成功");
				//strResl.append("0;该设备绑定的用户不存在桥接上网业务！");
				resltMap.put("flag","0");
			}
			
			
		}
		resltMap.put("strResl",strResl.toString());
		return resltMap;
	}
	
	public List<Map> getServInfo(String deviceId) {
		return dao.getServInfo(deviceId);
	}
	public String changeConnectionType(long accoid,String deviceId, String connType,
			String routeAccount, String routePasswd,String pvc,String vlan,String accessType, String user_id,
			String deviceserialnumber, String oui,String bindPort, String gw_type)
	{
		
//		String _pvc = "";
//		String _vlan = "";
		String msg = "";
		
		
		if (Global.XJDX.equals(Global.instAreaShortName)) {
			String isRoute = dao.getRouteData(deviceId);
			
			if("1".equals(isRoute)){
				msg = "-1;此终端版本不支持路由业务!";
				return msg;
			}
		}
		
		if(!"".equals(accessType) && accessType != null){
			if("1".equals(accessType) || "2".equals(accessType) || "3".equals(accessType) || "4".equals(accessType)){
					int flag = dao.updatePvc(user_id, routeAccount, routePasswd, pvc, vlan,bindPort, gw_type,connType);
					
					if(flag == -1){
						msg = "-1;配置操作失败!";
					}else {   // UPDATE成功，则进行业务下发
						// 预读调用对象
						PreServInfoOBJ preInfoObj = new PreServInfoOBJ(user_id, deviceId, oui, deviceserialnumber, "10", "1");
						if (1 == CreateObjectFactory.createPreProcess(gw_type).processServiceInterface(CreateObjectFactory.createPreProcess().GetPPBindUserList(preInfoObj))) {
							logger.debug("调用后台预读模块成功");
							msg = "1;配置操作成功!";
						} else {
							logger.warn("调用后台预读模块失败");
							msg = "-1;调用后台预读模块失败!";
						}
					}
				 
			}else {
				logger.warn("未知上行方式");
				msg = "-1;未知上行方式";
			}
		}else {
			logger.warn("未知上行方式");
			msg = "-1;未知上行方式";
		}
		return msg;
//		if(accessType != null && accessType.equals("1")){
//			_pvc = pvc;
//			dao.updatePvc(user_id);
//		}else if(accessType.equals("2") || accessType.equals("3")){
//			_vlan = vlan;
//		}else{
//			logger.warn("未知上行方式");
//			return -1 + ";未知上行方式";
//		}
//		String strategyXmlParam = toXml(connType, doType,routeAccount,routePasswd,_pvc,_vlan);
//		logger.warn("strategyXmlParam:"+strategyXmlParam);
//		/** 入策略表，调预读 */
//		// 立即执行
//		int strategyType = 0;
//		// 配置的service_id
//		int serviceId = 8008;
//		StrategyOBJ strategyObj = new StrategyOBJ();
//		// 策略ID
//		strategyObj.createId();
//		// 策略配置时间
//		strategyObj.setTime(TimeUtil.getCurrentTime());
//		// 用户id
//		strategyObj.setAccOid(accoid);
//		// 立即执行
//		strategyObj.setType(strategyType);
//		// 设备ID
//		strategyObj.setDeviceId(deviceId);
//		// QOS serviceId
//		strategyObj.setServiceId(serviceId);
//		// 顺序,默认1
//		strategyObj.setOrderId(1);
//		// 工单类型: 新工单,工单参数为xml串的工单
//		strategyObj.setSheetType(2);
//		// 参数
//		strategyObj.setSheetPara(strategyXmlParam);
//		strategyObj.setTempId(serviceId);
//		strategyObj.setIsLastOne(1);
//		// 入策略表
//		if (dao.addStrategy(strategyObj))
//		{
//			// 调用预读
//			if (true == CreateObjectFactory.createPreProcess().processOOBatch(String.valueOf(strategyObj
//					.getId())))
//			{
//				logger.debug("成功");
//				return 1 + ";调用后台成功;" + strategyObj.getId();
//			}
//			else
//			{
//				logger.warn("调用预读失败");
//				return -1 + ";调用后台失败";
//			}
//		}
//		else
//		{
//			logger.warn("策略入库失败");
//			return -1 + ";策略入库失败";
//		}
	}
 
	public String changeConnectionTypeForAh(String deviceId, String connType,
			String routeAccount, String routePasswd,String path,String gw_type)
	{
		logger.warn("changeConnectionTypeForAh:{},{},{},{},{}",new Object[]{deviceId,connType,routeAccount,routePasswd,path});
		String msg = "配置失败";
		ChangeConnectionNetForAHCorba netCorba = new ChangeConnectionNetForAHCorba();
		int code = netCorba.changeConnectType(deviceId, connType, routeAccount, routePasswd, path,gw_type);
		if(code == 1 || code ==0){
			msg = "配置成功";
		}
		return msg; 
	}

	@SuppressWarnings("unused")
	private String toXml(String connType, String doType, String routeAccount,
			String routePasswd,String pvc,String vlan)
	{
		String strXml = null;
		Document doc = DocumentHelper.createDocument();
		// root node: X_CT-COM_UplinkQoS
		Element root = doc.addElement("BridgeRouteSwap");
		Element wanType = root.addElement("WanType");
		wanType.addAttribute("to", connType);
		wanType.addAttribute("type", doType);
		wanType.addAttribute("flag", "1");
		wanType.addText("");
		root.addElement("VlanId").addText(vlan);
		root.addElement("PVC").addText(pvc);
		if("1".equals(connType)){
			root.addElement("Username").addText(routeAccount);
			root.addElement("Password").addText(routePasswd);
		}else{
			root.addElement("Username").addText("");
			root.addElement("Password").addText("");
		}
		root.addElement("X_CT-COM_ServiceList").addText("INTERNET");
		root.addElement("Enable").addText("1");
		Element BindPort = root.addElement("BindPort");
		BindPort.addAttribute("flag", "0");
		BindPort.addText("");
		strXml = doc.asXML();
		return strXml;
	}
	/**
	 * 获取该设备的所有lan口和wlan口
	 * 
	 * @param deviceId
	 * @param type   type为0则先采集后获取端口；为1则先查数据库，查不到再采集
	 * @return
	 */
	public String getLanInter(String deviceId, String type){
		
		logger.debug("getLanIntf(deviceId:{})",deviceId);
		
		int rsint;
		
		StringBuffer html = new StringBuffer();
		LanEthObj[] lanEthObj = null;
		if("0".equals(type)){
			rsint = getSuperCorba(deviceId, ConstantClass.GATHER_LAN_ETHERNET);
			if ( rsint == 1 ) {
				lanEthObj = lanEthDAO.getLanEthObj(deviceId, "");
			}else{
				logger.debug("LAN口获取失败");
				return "端口获取失败！";
			}
		}else if("1".equals(type)){
			lanEthObj = lanEthDAO.getLanEthObj(deviceId, "");
			if(null==lanEthObj){
				//SG
				rsint = getSuperCorba(deviceId, ConstantClass.GATHER_LAN_ETHERNET);
				if ( rsint == 1 ) {
					lanEthObj = lanEthDAO.getLanEthObj(deviceId, "");
				}else{
					logger.debug("LAN口获取失败");
					return "端口获取失败！";
				}
			}			
		}else{
			logger.debug("传入type错误");
			return "端口获取失败！";
		}
		if(null!=lanEthObj){
			for(int i=0;i<lanEthObj.length;i++){
				String code = "LAN"+lanEthObj[i].getLanEthid();
				StringBuffer value = new StringBuffer();
				value.append("InternetGatewayDevice.LANDevice.");
				value.append(lanEthObj[i].getLanid());
				value.append(".LANEthernetInterfaceConfig.");
				value.append(lanEthObj[i].getLanEthid()); 
				
				html.append("<INPUT TYPE='checkbox' NAME='");
				html.append("LAN");
				html.append("' value='");
				html.append(value.toString());
				html.append("'/>");
				html.append(code);
				if((i+1)%4==0){
					html.append("<br>");
				}
			}
		}
		
		List wlanList = dao.getData(deviceId);
		if("0".equals(type)){
			rsint = getSuperCorba(deviceId, ConstantClass.GATHER_LAN_WLAN);
			if ( rsint == 1 ) {
				wlanList = dao.getData(deviceId);
			}else{
				logger.debug("WLAN口获取失败");
				return "端口获取失败！";
			}
		}else if("1".equals(type)){
			wlanList = dao.getData(deviceId);
			if(wlanList.size()<=0){
				//SG
				rsint = getSuperCorba(deviceId, ConstantClass.GATHER_LAN_WLAN);
				if ( rsint == 1 ) {
					wlanList = dao.getData(deviceId);
				}else{
					logger.debug("WLAN口获取失败");
					return "端口获取失败！";
				}
			}			
		}else{
			logger.debug("传入type错误");
			return "端口获取失败！";
		}
		for(int i=0;i<wlanList.size();i++){
			Map oneWlanMap = (Map) wlanList.get(i);
			String code = "WLAN"+oneWlanMap.get("lan_wlan_id");
			StringBuffer value = new StringBuffer();
			value.append("InternetGatewayDevice.LANDevice.");
			value.append(oneWlanMap.get("lan_id"));
			value.append(".WLANConfiguration.");
			value.append(oneWlanMap.get("lan_wlan_id"));
			
			html.append("<INPUT TYPE='checkbox' NAME='");
			html.append("WLAN");
			html.append("' value='");
			html.append(value.toString());
			html.append("'/>");
			html.append(code);
		}
		
		return html.toString();
	}
	
	public String getDevNetType(String devId,String gw_type){
		ChangeConnectionNetForAHCorba netCorba = new ChangeConnectionNetForAHCorba();
		return netCorba.gatherNetInfo(devId, gw_type);
	}
	
	public LanEthDAO getLanEthDAO()
	{
		return lanEthDAO;
	}


	
	public void setLanEthDAO(LanEthDAO lanEthDAO)
	{
		this.lanEthDAO = lanEthDAO;
	}


	
	public WanRelatedCAO getWanCAO()
	{
		return wanCAO;
	}


	
	public void setWanCAO(WanRelatedCAO wanCAO)
	{
		this.wanCAO = wanCAO;
	}


	public int getSuperCorba(String deviceId,int code){
		logger.debug("getSuperCorba(deviceId:{},code{})",deviceId,code);
		return wanCAO.getDataFromSG(deviceId, code);
	}
	/**
	 * @return the dao
	 */
	public ChangeConnectionTypeDAO getDao()
	{
		return dao;
	}

	
	/**
	 * @param dao the dao to set
	 */
	public void setDao(ChangeConnectionTypeDAO dao)
	{
		this.dao = dao;
	}
}
