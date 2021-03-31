package bio.resource;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Sheet;
import jxl.Workbook;
import obj.iTVObj;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.User;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.StringUtil;

import dao.resource.ItvConfigDAO;

/**
 * @author Jason(3412)
 * @date 2008-12-17
 */
public class ItvConfigBIO {

	/** log */
	private static final Logger LOG = LoggerFactory.getLogger(ItvConfigBIO.class);
	
	private static final String LAN2 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2";
	
	private static final String WLAN2 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.2";
	
	private ItvConfigDAO itvConfigDAO;

	/**
	 *  0:立即执行
		1：第一次连到系统
		2：周期上报
		3：重新启动
		4：下次连到系统
		5：终端启动
	 *
	 * 执行策略的方式
	 * @author Jason(3412)
	 * @date 2008-12-17
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String getStrategyList(String name, String...typeIds) {
		StringBuffer tmpBufer = new StringBuffer();
		Map<String, String> map = itvConfigDAO.getStrategyType(typeIds);
		
		tmpBufer.append("<SELECT name=\"" + name + "\" class=\"bk\" style='width:150px'>");
		
		Set<String> set = map.keySet();
		for (String tempid : set) {
			tmpBufer.append("<OPTION value='").append(tempid);
			
			if ("5".equals(tempid)) {
				tmpBufer.append("' selected>");
			} else {
				tmpBufer.append("'>");
			}
			
			tmpBufer.append(map.get(tempid)).append("</OPTION>");
		}
		tmpBufer.append("</SELECT>");
		
//		tmpBufer.append("<OPTION value=\"0\">立即执行</OPTION>");
//		tmpBufer.append("<OPTION value=\"1\">第一次连到系统</OPTION>");
//		tmpBufer.append("<OPTION value=\"2\">周期上报</OPTION>");
//		tmpBufer.append("<OPTION value=\"3\">重新启动</OPTION>");
//		tmpBufer.append("<OPTION value=\"4\">下次连接到系统时自动配置</OPTION>");
//		tmpBufer.append("<OPTION value=\"5\" selected>终端启动</OPTION>");
//		tmpBufer.append("</SELECT>");
		
		return tmpBufer.toString();
	}
	
	/**
	 * 版本模板下拉列表
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2008-12-17
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String getVersionList(String name) {
		StringBuffer tmpBufer = new StringBuffer();
		Map<String, String> map = itvConfigDAO.getSoftTemp();
		tmpBufer.append("<SELECT name=\"" + name + "\" class=\"bk\" style='width:150px'>");
		Set<String> set = map.keySet();
		for (String tempid : set) {
			tmpBufer.append("<OPTION value=\"" + tempid + "\">"
					+ map.get(tempid) + "</OPTION>");
		}
		tmpBufer.append("</SELECT>");
		return tmpBufer.toString();
	}
	
	/**
	 * 带软件升级的策略入库
	 * @author gongsj
	 * @date 2009-9-10
	 * @param isAddLan
	 * @param isAddWlan
	 * @param deviceinfoList
	 * @param goalVersion
	 * @param curUser
	 * @param strategyType
	 * @param softStrategyType
	 * @param arrayPara
	 * @param wlanstrategy_type
	 * @param auWay
	 * @param wepPw
	 * @param wpaPw
	 * @param wanTypeId IPTV中增加的WAN连接的wanTypeId
	 * @param tempId
	 */
	@SuppressWarnings("unchecked")
	public void doStratery(String isAddLan, String isAddWlan, List<String> deviceinfoList, String goalVersion, User curUser, String strategyType, 
						   String softStrategyType, String[] arrayPara, String wlanstrategy_type, 
						   String auWay, String wepPw, String wpaPw, int tempId, String gw_type){
		if(deviceinfoList == null){
			LOG.debug("任务中没有选择到设备");
			return;
		}
		
//		//为2时不需要软件升级
//		if(2!=tempId){
//			if("".equals(goalVersion) || goalVersion == null){
//				LOG.warn("任务中没有选择到软件升级的目标版本，返回");
//				return;
//			}
//		}
		
		ArrayList<String> sqlList = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		/**----------------任务表，域表操作------------**/
		//入任务表
		long task_id = date.getTime();
		//任务名称
		String task_name = curUser.getAccount() + "_" + sdf.format(date) + "_iTV配置";
		//模板ID(IPTV批量配置)
		//int temp_id = 1;
		int temp_id = tempId;
		
		//定制人
		long order_acc_oid = curUser.getId();
		//定制时间
		long order_time = task_id/1000;
		//审核状态
		int is_check = 1;
		//审核人
		long check_acc_oid = curUser.getId();
		//审核时间
		long check_time = order_time;
		
		int isLastOne = 0;
		
		int sheetType = 1;
		
		sqlList.add(itvConfigDAO.inTaskTable(task_id,task_name,temp_id,order_acc_oid, order_time,is_check,check_acc_oid,check_time));
		//入域权限表
		//sqlList.addAll(ItvConfigDAO.insertAreaTable(13, String.valueOf(task_id), String.valueOf(curUser.getAreaId()), true));
		
		/**----------------策略表操作------------**/
		//策略id
		long id = 0L;
		
		//马上执行的策略id列表
		String[] idArr = new String[deviceinfoList.size()];
		
		//管理员id
		long acc_oid = curUser.getId();
		//定制时间
		long time = order_time;
		//策略方式
		int iType = Integer.parseInt(strategyType);
		
		int softType = Integer.parseInt(softStrategyType);
		int type = 4;
		String gather_id = "";
		String device_id = "";
		String oui = "";
		String serialnumber = "";
		String username = "";
		//service_id:软件升级： 5 | IPTV开户：1101
		String service_id = "";
		int order_id = 0;
		String serv_type_id = "";
		String oper_type_id = "";
		String sub_service_id = "";
		
		String ssid2 = null;
		//order_id,serv_type_id,oper_type_id
		List<Map> servStraryList = itvConfigDAO.getServStrary(temp_id);
		Map serviceIdMap = itvConfigDAO.getServiceId();
		Map<String, Map> softParamMap = itvConfigDAO.getSoftFileInfo();
		
		/*******针对大亚设备只能从ACS服务器上读取的情况，增加以下代码********/
		Map map1 = new HashMap<String, String>();
		map1.put("file_url", "http://192.168.2.6:8181/DARE-v6.1.4");
		map1.put("softwarefile_size", "5528572");
		map1.put("softwarefile_name", "DARE-v6.1.4");
		softParamMap.put("849", map1);
		Map map2 = new HashMap<String, String>();
		map2.put("file_url", "http://192.168.2.6:8181/DARE-v6.2.4");
		map2.put("softwarefile_size", "5528572");
		map2.put("softwarefile_name", "DARE-v6.2.4");
		softParamMap.put("848", map2);
		/*******针对大亚设备只能从ACS服务器上读取的情况，增加以上代码********/
		
		Map softUpMap = itvConfigDAO.getSoftUp();
		//LOG.debug(softParamMap);
		String deviceTypeId = "";
		String softSheet_para = "";
		//String iTVSheet_para = sheetParam;
		String sheet_para = "";
		String user_id = "";
		String wanType = arrayPara[3];
		for(int i = 0; i < deviceinfoList.size(); i++){
			
			String[] arrayInfo = deviceinfoList.get(i).split("\\|");
			
			device_id = arrayInfo[0];
			gather_id = arrayInfo[1];
			oui = arrayInfo[2];
			serialnumber = arrayInfo[3];
			username = arrayInfo[4];
			user_id = arrayInfo[7];
			deviceTypeId = (String)softUpMap.get(goalVersion + "|" + arrayInfo[8]);
			
			if(deviceTypeId == null || "".equals(deviceTypeId)){
				//如果映射表没有该devicetype_id的纪录，默认不需要升级
				deviceTypeId = arrayInfo[8];
				LOG.debug("deviceTypeId为空(goalVersion|devicetype_id_old):{}|{}", goalVersion, arrayInfo[8]);
			}
			
			//目标版本和原设备的版本一致，不需要升级
			if(deviceTypeId.equals(arrayInfo[8])){
				// 增加了 deviceTypeId  在不需要升级的情况下 XML格式稍作调整 add by zhangchy 2011-12-30
				softSheet_para = softUpXml(null, deviceTypeId);
			}else{
//				softSheet_para = deviceTypeId + (String)softParamMap.get(deviceTypeId);
				// 增加了 deviceTypeId  在不需要升级的情况下 XML格式稍作调整 add by zhangchy 2011-12-30
				softSheet_para = softUpXml(softParamMap.get(deviceTypeId), deviceTypeId);
			}
			
			for(int j = 0; j<servStraryList.size(); j++){
				Map tmpMap = servStraryList.get(j);
				
				id = Math.round(Math.random() * 1000000000L);
				order_id = ((BigDecimal)tmpMap.get("order_id")).intValue();
				//获取service_id
				serv_type_id = ((BigDecimal)tmpMap.get("serv_type_id")).toString();
				oper_type_id = ((BigDecimal)tmpMap.get("oper_type_id")).toString();
				
				if (j == 0) {
					idArr[i] = String.valueOf(id);
				}
				
				if(order_id==1){
					service_id = (String)serviceIdMap.get(serv_type_id+"|"+oper_type_id);
					if(service_id == null || "".equals(service_id)){
						LOG.debug("service_id为空(serv_type_id|oper_type_id|wanType):{}|{}|{}", new Object[]{serv_type_id, oper_type_id, "-1"});
						service_id = "-100";
					}
					sheet_para = softSheet_para;
					type = 4;
					sheetType = 2;
					sub_service_id = service_id;
					
				} else if(order_id == 2) {
					//iTV
					ssid2 = "iTV"+serialnumber.substring(serialnumber.length() - 5, serialnumber.length());
					wpaPw = serialnumber.substring(serialnumber.length() - 8, serialnumber.length());
					service_id = (String)serviceIdMap.get(serv_type_id+"|"+oper_type_id);
					if(service_id == null || "".equals(service_id)){
						LOG.debug("service_id为空(serv_type_id|oper_type_id|wanType):{}|{}|{}", new Object[]{serv_type_id, oper_type_id, wanType});
						service_id = "-100";
					}
					//service_id = "1101";
					//sqlList.add(itvConfigDAO.inServUserTable(user_id,serv_type_id,username, wanType,arrayPara[0],arrayPara[1],arrayPara[2],time,time,time));
					
					iTVObj itvObj = getiTVObj(arrayPara[0], arrayPara[1], wpaPw, ssid2, isAddLan, isAddWlan, wanType);
					
					sheet_para = iTVObj2Xml(isAddLan, isAddWlan, itvObj);
					LOG.debug("入iTV策略的XML参数：{}", sheet_para);
					type = iType;
					sheetType = 2;
					sub_service_id = "1110";
					
				} else if(order_id==3){
					//QoS
					service_id = (String)serviceIdMap.get(serv_type_id+"|"+oper_type_id);
					if(service_id == null || "".equals(service_id)){
						LOG.debug("service_id为空(serv_type_id|oper_type_id|wanType):{}|{}|{}", new Object[]{serv_type_id, oper_type_id, wanType});
						service_id = "-100";
					}
					
					//sqlList.add(itvConfigDAO.inServUserTable(user_id,serv_type_id,username, wanType,arrayPara[0],arrayPara[1],arrayPara[2],time,time,time));
					
					sheet_para = qosObj2Xml("INTERNET,TR069,IPTV","1");
					LOG.debug("入OoS策略的XML参数：{}", sheet_para);
					
					//设备立即执行
					type = 0;
					sheetType = 2;
					isLastOne = 1;
					sub_service_id = service_id;
				}
				
				sqlList.add(itvConfigDAO.inStrategyTable(id, acc_oid, time, type,gather_id, device_id, oui,
						serialnumber, username, sheet_para, service_id, task_id, order_id, sheetType, temp_id, isLastOne, sub_service_id));
				
				sqlList.add(itvConfigDAO.inStrategyLogTable(id,acc_oid,time, type,gather_id,device_id,oui,
						serialnumber, username, sheet_para, service_id, task_id, order_id, sheetType, temp_id, isLastOne, sub_service_id));
				
			}
		}
		
		if (softType == 0) {
			//如果立即执行，则马上入库，并调用预处理
			if (itvConfigDAO.updateSQLList(sqlList)) {
				//调用预处理
				LOG.warn("立即执行，开始调用预处理...");
				invokePreProcess(idArr, gw_type);
				for(String z : idArr) {
					LOG.warn("ZZZZZZZZZZZZZZZ---:" + z);
				}
			}
		} else {
			//启用一个新的线程来做入库
			LipossGlobals.ALL_SQL_IPTV.addAll(sqlList);
		}
	}

	/**
	 * 拼接XML字符串
	 * 
	 *  参数增加了 deviceTypeId  在不需要升级的情况下 XML格式稍作调整 add by zhangchy 2011-12-30
	 * @param map
	 * @param deviceTypeId
	 * @return
	 */
	private String softUpXml(Map map, String deviceTypeId)
	{
		LOG.debug("softUpXml({})", map);
		String strXml = null;
		if (map == null || map.isEmpty())
		{
			// new doc
			Document doc = DocumentHelper.createDocument();
			// root node: NET
			Element root = doc.addElement("SoftUpdate");
			root.addAttribute("flag", "1");
			root.addElement("CommandKey").addText("");
			root.addElement("FileType").addText("");
			root.addElement("URL").addText("");
			root.addElement("Username").addText("");
			root.addElement("Password").addText("");
			root.addElement("FileSize").addText("");
			root.addElement("TargetFileName").addText(deviceTypeId);  //将原来的addText("") 改为 addText(deviceTypeId) modify by zhangchy 2011-12-30
			root.addElement("DelaySeconds").addText("");
			root.addElement("SuccessURL").addText("");
			root.addElement("FailureURL").addText("");
			strXml = doc.asXML();
		}
		else
		{
			// new doc
			Document doc = DocumentHelper.createDocument();
			// root node: NET
			Element root = doc.addElement("SoftUpdate");
			root.addAttribute("flag", "1");
			root.addElement("CommandKey").addText("");
			root.addElement("FileType").addText("1 Firmware Upgrade Image");
			root.addElement("URL").addText(StringUtil.getStringValue(map.get("file_ur")));
			root.addElement("Username").addText("");
			root.addElement("Password").addText("");
			root.addElement("FileSize").addText(
					StringUtil.getStringValue(map.get("softwarefile_size")));
			root.addElement("TargetFileName").addText(
					StringUtil.getStringValue(map.get("softwarefile_name")));
			root.addElement("DelaySeconds").addText("");
			root.addElement("SuccessURL").addText("");
			root.addElement("FailureURL").addText("");
			strXml = doc.asXML();
		}
		return strXml;
	}
	/**
	 * 得到iTV对象
	 * @author gongsj
	 * @date 2009-9-9
	 * @return
	 */
	private iTVObj getiTVObj(String vpi, String vci, String wpaPw, String ssid, String isAddLan, String isAddWlan, String wanType) {
		iTVObj itvObj = new iTVObj();
		itvObj.setWanType(wanType);
		itvObj.setPvc("PVC:"+vpi+"/"+vci);
		
		itvObj.setPreSharedKey(wpaPw);
		itvObj.setSsid(ssid);
		
		if ("1".equals(isAddLan) && "0".equals(isAddWlan)) {
			itvObj.setBindPort(LAN2);
		} else {
			itvObj.setBindPort(LAN2 + "," + WLAN2);
		}
			
		return itvObj;
	}
	
	/**
	 * 输入excel文件，解析后返回ArrayList
	 * @param file 输入的excel文件
	 * @return ArrayList<String>
	 */
	public List<String> getUsernameByIportFile(File file){
		
		//初始化返回值和字段名数组
		ArrayList<String> arr = new ArrayList<String>();
		
		
		Workbook wwb = null;
		Sheet ws = null;
		
		try{
			//读取excel文件
			wwb = Workbook.getWorkbook(file);
			
			//总sheet数
			//int sheetNumber = wwb.getNumberOfSheets();
			int sheetNumber = 1;
			LOG.debug("sheetNumber:" + sheetNumber);
			
			for (int m=0;m<sheetNumber;m++){
				ws = wwb.getSheet(m);
				
				//当前页总记录行数和列数
				int rowCount = ws.getRows();
				int columeCount = ws.getColumns();
				
				LOG.debug("rowCount:" + rowCount);
				LOG.debug("columeCount:" + columeCount);
				
				if(101<rowCount){
					rowCount = 101;
				}
				
				//第一行为字段名，所以行数大于1才执行
				if (rowCount > 1 && columeCount > 0){
					
					//取当前页所有值放入list中
					for (int i=1;i<rowCount;i++){
						String temp = ws.getCell(0, i).getContents().trim();
						if(null!=temp && !"".equals(temp)){
							arr.add(ws.getCell(0, i).getContents().trim());
						}
					}
				}
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				wwb.close();
			}catch(Exception e){
				LOG.debug(e.getMessage());
			}
			
		}
		return arr;
	}
	
	/**
	 * 开始调用预读模块
	 * 
	 * @param invokeStruct
	 */
	private void invokePreProcess(String[] idArr, String gw_type) {
		try {
			CreateObjectFactory.createPreProcess(gw_type).processOOBatch(idArr);
			//Global.G_PPManager.processOOBatch(idArr);
		} catch (Exception e) {
//			AppInitBIO.initPreProcess();
//			Global.G_PPManager.processOOBatch(idArr);
			LOG.error("CreateObjectFactory.createPreProcess() processOOBatch error!");
		}
	}
	
//	public void doStratery(List deviceinfoList, User curUser){
//		
//		LOG.debug("未选择软件升级，该操作流程还没有定");
//		
//	}
	
	/**
	 * 生成iTV所需的参数
	 */
	private String iTVObj2Xml(String isAddLan,String isAddWlan, iTVObj itvObj) {
		LOG.debug("进入iTVObj2Xml...");
		
		String strXml = null;
		if(null == itvObj){
			LOG.debug("voipObj2Xml中有对象为空");
			return null;
		}
		
		//new doc
		Document doc = DocumentHelper.createDocument();
		//root node: Voip
		
		//modify by zhangcong(67706) 依照漆学启要求，修改iTV的XML文件 2011-06-03
		Element root = doc.addElement("iTVs");
		Element lan = root.addElement("Lan");
		if("1".equals(isAddLan)){
			lan.addAttribute("flag", "1");
		}else{
			lan.addAttribute("flag", "0");
		}
		
		//添加3个节点，内容为空 2011-06-03
		lan.addElement("i").addText("");
		lan.addElement("j").addText("");
		lan.addElement("k").addText("");
		
		lan.addElement("WanType").addText(itvObj.getWanType());
		lan.addElement("Username").addText(null == itvObj.getUsername() ? "" : itvObj.getUsername());
		lan.addElement("Password").addText(null == itvObj.getPassword() ? "" : itvObj.getPassword());
		lan.addElement("Pvc").addText("");
		
		lan.addElement("VlanId").addText("");
		lan.addElement("Ip").addText(null == itvObj.getIp() ? "" : itvObj.getIp());
				
		lan.addElement("Mask").addText(null == itvObj.getMask() ? "" : itvObj.getMask());
		lan.addElement("Gateway").addText(null == itvObj.getGateway() ? "" : itvObj.getGateway());
		
		lan.addElement("Dns").addText(null == itvObj.getDns() ? "" : itvObj.getDns());
		
		Element wlan = root.addElement("Wlan");
		if("1".equals(isAddWlan)){
			wlan.addAttribute("flag", "1");
		}else{
			wlan.addAttribute("flag", "0");
		}
		
		wlan.addElement("Ssid").addText(null == itvObj.getSsid() ? "" : itvObj.getSsid());
		wlan.addElement("PreSharedKey").addText(null == itvObj.getPreSharedKey() ? "" : itvObj.getPreSharedKey());
		
		root.addElement("BindPort").addText(null == itvObj.getBindPort() ? "" : itvObj.getBindPort());
		
		strXml = doc.asXML();
		
		return strXml;
	}

	/**
	 * 生成QoS参数
	 * @author gongsj
	 * @date 2009-9-9
	 * @param mode
	 * @param enable
	 * @return
	 */
	private String qosObj2Xml(String mode, String enable) {
		String strXml = null;
		
		Document doc = DocumentHelper.createDocument();
		//root node: X_CT-COM_UplinkQoS
		Element root = doc.addElement("QoS");
		
		root.addAttribute("type", "0");
		root.addElement("Mode").addText(mode);
		root.addElement("Enable").addText(enable);
		
		strXml = doc.asXML();
		
		return strXml;
	}
	
	
	public void setItvConfigDAO(ItvConfigDAO itvConfigDAO) {
		this.itvConfigDAO = itvConfigDAO;
	}
	
	
}
