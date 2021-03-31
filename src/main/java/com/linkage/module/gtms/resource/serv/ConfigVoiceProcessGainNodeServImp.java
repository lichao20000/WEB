package com.linkage.module.gtms.resource.serv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.resource.dao.ConfigVoiceProcessGainNodeDAOImp;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.init.obj.CpeFaultcodeOBJ;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.corba.ACSCorba;


public class ConfigVoiceProcessGainNodeServImp implements ConfigVoiceProcessGainNodeServ {
	
	private static Logger logger = LoggerFactory
			.getLogger(ConfigVoiceProcessGainNodeServ.class);
	
	
	private ConfigVoiceProcessGainNodeDAOImp dao = null;
	
	/**
	 * 根据deviceId，调用ACS获取语音的呼入、呼出增益节点
	 * 
	 * @param deviceId
	 * @param gw_type
	 * @param node_1 InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.Line.
	 * @param node_2 VoiceProcessing.TransmitGain
	 * @param node_3 VoiceProcessing.ReceiveGain
	 * @return
	 */
	public String getGainNode(String deviceId, String gw_type, String node_1, String node_2, String node_3) {
		
		logger.debug("ConfigVoiceProcessGainNodeServImp==>getGainNode({},{},{},{},{})",
				new Object[] { deviceId, gw_type, node_1, node_2, node_3 });
		
		ACSCorba corba = new ACSCorba(gw_type);
		
		HashMap paraMap = corba.getParaMap(node_1, deviceId);
		
		if (!"1".equals(String.valueOf(paraMap.get("faultCode"))))
		{
			CpeFaultcodeOBJ obj = Global.G_Fault_Map.get(String.valueOf(paraMap.get("faultCode")));
			if (null == obj)
			{
				return "-1,"+Global.G_Fault_Map.get(100000).getFaultDesc();
			}
			else
			{
				if (null == obj.getFaultDesc())
				{
					return "-1,"+Global.G_Fault_Map.get(100000).getFaultDesc();
				}
				return "-1,"+obj.getFaultDesc();
			}
		}
		else {
			
			logger.warn("节点获取成功！");

			// paraStr字符串的格式为： 
			// InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.Line.2.,0|InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.Line.1.,0
			String paraStr = corba.getString(paraMap);
			
			logger.warn("返回的节点为:[{}]",paraStr);
			
			if (null == paraStr || "".equals(paraStr)) {
				logger.warn("没有获取到语音节点，请确认设备是否在线！device_id=[{}]", deviceId);
				return "-1,没有获取到语音节点，请确认设备是否在线！";
			}
			
			StringBuffer sb = new StringBuffer("");
			String new_node_2 = "";  // 呼出增益 VoiceProcessing.TransmitGain
			String new_node_3 = "";  // 呼入增益 VoiceProcessing.ReceiveGain
			
			String [] strNode = paraStr.split("\\|");
			for (int i = 0; i < strNode.length; i++) {
				
				// strNode[i]的格式为：InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.Line.2.,0 
				String [] lineNode = strNode[i].split(",");
				
				new_node_2 = lineNode[0]+node_2; // VoiceProcessing.TransmitGain  呼出增益
				new_node_3 = lineNode[0]+node_3; // VoiceProcessing.ReceiveGain  呼入增益
				
				sb.append(new_node_2).append(",").append(new_node_3).append(",");
			}
			
			logger.warn("需要采集的节点：[{}]", sb.toString());
			
			// 调ACS获取节点值
			ArrayList<ParameValueOBJ> objList = corba.getValue(deviceId, sb.toString()
					.substring(0, sb.toString().length() - 1).split(","));
			
			logger.warn("ACS返回的节点与值：{}", objList);
			
			// 如果ACS没有获取到节点值，则返回错误提示信息
			if (null == objList || "".equals(objList)) {
				if (corba.getStat() != 1)
				{
					CpeFaultcodeOBJ obj = Global.G_Fault_Map.get(corba.getStat());
					if (null == obj)
					{
						return "-1,"+Global.G_Fault_Map.get(100000).getFaultDesc();
					}
					else
					{
						if (null == obj.getFaultDesc())
						{
							return "-1,"+Global.G_Fault_Map.get(100000).getFaultDesc();
						}
						return "-1,"+obj.getFaultDesc();
					}
				}
			}
			
			// <线路信息，{呼出增益，呼入增益}>
			HashMap<String, String[]> tempMap = new HashMap<String, String[]>();
			
			String lineNum = "";        // 线路信息 1路，2路，3路等
			String nodeName = "";  // 节点名称
			
			// 将呼出增益，呼入增益的值放入list列表中，然后将list值按照线路信息放入Map中
			
			
			for(ParameValueOBJ obj : objList){
				
				// node_1  InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.Line.
				lineNum = obj.getName().substring(node_1.length()).split("\\.")[0];
				// 节点名称，呼出增益节点 或者是 呼入增益节点
				nodeName = obj.getName().substring(node_1.length()).split("\\.")[2];
				
				// 呼出增益，呼入增益
				String[] gainValueArr = tempMap.get(lineNum);
				
				if (null == gainValueArr || gainValueArr.length != 2) {
					
					gainValueArr = new String[2];
					
					// 呼出增益
					if ("TransmitGain".equals(nodeName)) {
						gainValueArr[0] = obj.getValue();
					}
					// 呼入增益
					if ("ReceiveGain".equals(nodeName)) {
						gainValueArr[1] = obj.getValue();
					}
				}else {
					if ("TransmitGain".equals(nodeName)) {
						gainValueArr[0] = obj.getValue();
					}
					// 呼入增益
					if ("ReceiveGain".equals(nodeName)) {
						gainValueArr[1] = obj.getValue();
					}
					
				}
				tempMap.put(lineNum, gainValueArr);
			}
			
			StringBuffer retStr = new StringBuffer("");
			
			// 将HashMap key值升序排序
			Object[] key = tempMap.keySet().toArray();
			Arrays.sort(key);
			
			// 将线路信息，呼出增益，呼入增益 拼装成字符串   线路信息;呼出增益;呼入增益|线路信息;呼出增益;呼入增益|....
			for(int i=0; i<key.length; i++){
				String [] valueArr = tempMap.get(key[i]);
				retStr.append(key[i]).append(";").append(valueArr[0]).append(";").append(valueArr[1]).append("|");
			}
			
			logger.warn("返回前台："+retStr.toString());
			
			return retStr.toString();
		}
	}
	
	
	// 调ACS，下发增益节点
	public String doConfigGainNode(String deviceId, String gw_type, String gainValue){
		
		logger.debug("ConfigVoiceProcessGainNodeServImp==>doConfigGainNode({},{},{})",
				new Object[] { deviceId, gw_type, gainValue });
		
		ArrayList<ParameValueOBJ> objList = new ArrayList<ParameValueOBJ>();
		
		ACSCorba corba = new ACSCorba(gw_type);
		
		// 解析 gainValue
		String [] gainNode = gainValue.split("\\|");
		
		for (int i = 0; i < gainNode.length; i++) {
			
			ParameValueOBJ obj = new ParameValueOBJ();
			String [] gainNodeValue = gainNode[i].split(";");
			
			obj.setName(gainNodeValue[0]);
			obj.setValue(gainNodeValue[1]);
			obj.setType("2");
			
			objList.add(obj);
		}
		
		logger.warn("=objList=="+objList);
		
		int retResult = corba.setValue(deviceId, objList);
		if (retResult != 1)
		{
			CpeFaultcodeOBJ obj = Global.G_Fault_Map.get(retResult);
			if (null == obj)
			{
				return Global.G_Fault_Map.get(100000).getFaultDesc();
			}
			else
			{
				if (null == obj.getFaultDesc())
				{
					return Global.G_Fault_Map.get(100000).getFaultDesc();
				}
				return obj.getFaultDesc();
			}
		}else{
			logger.warn("增益节点下发成功！");
			return "增益节点下发成功！";
		}
	}
	
	
	
	
	
	public ConfigVoiceProcessGainNodeDAOImp getDao() {
		return dao;
	}


	
	public void setDao(ConfigVoiceProcessGainNodeDAOImp dao) {
		this.dao = dao;
	}
	
	
	public static void main(String[] args) {
		
		String xxx = "InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.Line.";
		String aaa = "InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.Line.54321.VoiceProcessing.TransmitGain";
		
		String lineNum = aaa.substring(xxx.length()).split("\\.")[2];
		
//		System.out.println(aaa.indexOf("VoiceProcessing.TransmitGain"));
//		System.out.println(lineNum);
		
		HashMap<String, String []> map = new HashMap<String, String[]>();
		String [] list1 = {"1","2"};
		String [] list2 = {"11","22"};
		String [] list3 = {"111","222"};
		
		map.put("2", list2);
		map.put("1", list1);
		map.put("3", list3);
		
		// 将HashMap key值升序排序
		Object[] key = map.keySet().toArray();
		Arrays.sort(key);
		StringBuffer sb1 = new StringBuffer("");
		StringBuffer sb = null;
		for(int i=0; i<key.length; i++){
			String [] valueList = map.get(key[i]);
			for (int j = 0; j < valueList.length; j++) {
				sb = new StringBuffer("");
				sb.append(valueList[0]).append(";").append(valueList[1]);
			}
			sb1.append(key[i]).append(";").append(sb.toString()).append("|");
		}
		System.out.println(sb1.toString());//1;1;2|2;11;22|3;111;222|
	}

}
