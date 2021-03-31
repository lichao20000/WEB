package com.linkage.litms.paramConfig;


import java.util.ArrayList;
import java.util.Date;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.Base64;
import com.linkage.litms.common.util.ToCode16;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.init.bio.AppInitBIO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.corba.PreProcessCorba4Stb;


/**
 * 参数配置模块的逻辑处理
 * 
 * @author lizhaojun
 * 
 * @version hgw 1.0 
 * @Modify Record: 
 *   <p>2007-06-18 Alex.Yan (yanhj@lianchuang.com)<br>
 *          RemoteDB ACS.
 * * <p>2007-08-30 Alex.Yan (yanhj@lianchuang.com) <br>
 * 	  file_path_3 + "/" + filename_3 + "&type=1".
 */

public class ParamManage 
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(ParamManage.class);
	private static String InstArea=Global.instAreaShortName;
	
	/**
	 * 设备工单配置   // modify by lizhaojun 2007-06-26    此方法只适用于只有一个tc_serial 的模板
	 * 
	 * @param request
	 * @param params
	 *            设备对应的工单参数
	 * @return 设备工单配置的工单数组，如数据库操作失败，返回null
	 */

	/** begin add by chenjie(67371) **/ 
	
	/**
	 * 增加配置文件 -> 入策略，调用预读
	 * 
	 * @param userId       用户ID
	 * @param deviceIds    设备id数组
	 * @param executeType  执行方式
	 * @param filePath     文件路径
	 * @param userName     用户名
	 * @param pwd          密码
	 * @param delayTime    延迟时间
	 * @param fileName     文件名
	 * @param tag          生成不同的xml格式
	 * @return
	 */
	public boolean doStrategy(long userId, String[] deviceIds, String executeType, String filePath, 
				String userName, String pwd, String delayTime, String fileName, int tag, String gw_type)
	{
		logger.warn("doStrategy({},{},{},{},{},{},{},{},{},{})", new Object[]{userId, deviceIds, executeType, filePath, userName, pwd, delayTime, fileName, tag,gw_type});
		String strategyXmlParam = "";
		String fileType = "";
		
		/** 入策略表，调预读 */
		ArrayList<String> sqllist = new ArrayList<String>();
		SuperDAO dao = new SuperDAO();
		String[] stragetyIds = new String[deviceIds.length];
		
		// 配置的service_id
		int serviceId = 0;
		
		for(int i = 0; i < deviceIds.length; i++)
		{
			// 组装filePath
			//fileName = deviceIds[i]+"." + new Date().getTime()/1000 + "." + fileName;
			if(!Global.SXLT.equals(InstArea)){
				fileName = deviceIds[i]+"." + new Date().getTime()/1000 + "." + fileName;
			}
//			filePath = filePath + ToCode16.encode(Base64.encode("&fileName=" + fileName + "&device_id=" + deviceIds[i]));
			switch(tag)
			{
				// 上传配置文件
				case 1:
				{
					filePath = filePath + ToCode16.encode(Base64.encode("&fileName=" + fileName + "&device_id=" + deviceIds[i]+"gw_type="+gw_type));
					fileType = "1 Vendor Configuration File";
					strategyXmlParam = toXML1(fileType, filePath, userName, pwd, delayTime);
					serviceId = 2;
					break;
				}
				// 下发配置文件
				case 2:
				{
					strategyXmlParam = toXML2(filePath, fileName, userName, pwd, delayTime);
					serviceId = 1;
					break;
				}
				// 上传日志文件
				case 3:
				{
					filePath = filePath + ToCode16.encode(Base64.encode("&fileName=" + fileName + "&device_id=" + deviceIds[i]));
					fileType = "2 Vendor Log File";
					strategyXmlParam = toXML1(fileType, filePath, userName, pwd, delayTime);
					serviceId = 3;
					break;
				}
			}
			logger.debug("--XML--: " + strategyXmlParam);
			
			StrategyOBJ strategyObj = new StrategyOBJ();
			// 策略ID
			strategyObj.createId();
			// 策略配置时间
			strategyObj.setTime(TimeUtil.getCurrentTime());
			// 用户id
			strategyObj.setAccOid(userId);
			// 立即执行
			strategyObj.setType(Integer.parseInt(executeType));
			// 设备ID
			strategyObj.setDeviceId(deviceIds[i]);
			// QOS serviceId
			strategyObj.setServiceId(serviceId);
			// 顺序,默认1
			strategyObj.setOrderId(1);
			// 工单类型: 新工单,工单参数为xml串的工单
			strategyObj.setSheetType(2);
			// 参数
			strategyObj.setSheetPara(strategyXmlParam);
			strategyObj.setTempId(serviceId);
			strategyObj.setIsLastOne(1);
			
			stragetyIds[i] = String.valueOf(strategyObj.getId());
			// 入策略表
			if("4".equals(gw_type)){
				sqllist.addAll(dao.stbStrategySQL(strategyObj));
			}else{
				sqllist.addAll(dao.strategySQL(strategyObj));
			}
			
		}
		
		boolean flag = false;
		// 立即执行
		 if(executeType.equals("0"))
		 {
			int iCode[] = DataSetBean.doBatch(sqllist);
			if (iCode != null && iCode.length > 0) {
				logger.debug("批量执行策略入库：  成功");
				flag = true;
				
			} else {
				logger.debug("批量执行策略入库：  失败");
				flag = false;
			}
			
			logger.warn("立即执行，开始调用预处理...");
			// 调用预读
			invokePreProcess(stragetyIds, gw_type);
		 }
		 else
		 {
			 // 启用一个新的线程来做入库
			 LipossGlobals.ALL_SQL_IPTV.addAll(sqllist);
			 flag = true;
		 }
		 return flag;
	}
	
	// 上传配置文件
	private String toXML1(String fileType, String filePath, String userName, String pwd, String delayTime)
	{
		String strXml = null;
		Document doc = DocumentHelper.createDocument();
		// root node: X_CT-COM_UplinkQoS
		Element root = doc.addElement("Upload");
		root.setAttributeValue("flag", "1");
		
		Element CommandKey = root.addElement("CommandKey");
		CommandKey.addText("0");
		
		Element FileType = root.addElement("FileType");
		FileType.addText(fileType);
		
		Element URL = root.addElement("URL");
		URL.addText(filePath);
		
		if(!Global.SXLT.equals(InstArea))
		{
			Element Username = root.addElement("Username");
			Username.addText(userName);
			
			Element Password = root.addElement("Password");
			Password.addText(pwd);
			
			Element DelaySeconds = root.addElement("DelaySeconds");
			DelaySeconds.addText(delayTime);
		}
		
		strXml = doc.asXML();
		return strXml;
	}
	
	// 下发配置文件
	private String toXML2(String filePath, String fileName, String userName, String pwd, String delayTime)
	{
		String strXml = null;
		Document doc = DocumentHelper.createDocument();
		// root node: X_CT-COM_UplinkQoS
		Element root = doc.addElement("Download");
		root.setAttributeValue("flag", "1");
		
		Element CommandKey = root.addElement("CommandKey");
		CommandKey.addText("0");
		
		Element FileType = root.addElement("FileType");
		FileType.addText("3 Vendor Configuration File");
		
		Element URL = root.addElement("URL");
		URL.addText(filePath);
		
		if(!Global.SXLT.equals(InstArea))
		{
			Element Username = root.addElement("Username");
			Username.addText(userName);
			
			Element Password = root.addElement("Password");
			Password.addText(pwd);
			
			Element DelaySeconds = root.addElement("DelaySeconds");
			DelaySeconds.addText(delayTime);
		}
		
		Element FileSize = root.addElement("FileSize");
		FileSize.addText("");
		
		Element TargetFileName = root.addElement("TargetFileName");
		TargetFileName.addText("");
		
		Element SuccessURL = root.addElement("SuccessURL");
		SuccessURL.addText("");
		
		Element FailureURL = root.addElement("FailureURL");
		FailureURL.addText("");
		
		strXml = doc.asXML();
		return strXml;
	}
	
	/**
	 * 开始调用预读模块
	 * 
	 * @param invokeStruct
	 */
	private void invokePreProcess(String[] idArr, String gw_type) {
		try {
//			if (Global.GW_TYPE_ITMS.equals(gw_type)) {
//				Global.G_PPManager_itms.processOOBatch(idArr);
//			} else {
//				Global.G_PPManager_bbms.processOOBatch(idArr);
//			}
			if(Global.NMGDX.equals(InstArea)
					&& Global.GW_TYPE_STB.equals(gw_type)){
				new PreProcessCorba4Stb().processOOBatch(idArr);
			}else{
				CreateObjectFactory.createPreProcess(gw_type).processOOBatch(idArr);
			}
		} catch (Exception e) {
			AppInitBIO.initPreProcess(gw_type);
//			if (Global.GW_TYPE_ITMS.equals(gw_type)) {
//				Global.G_PPManager_itms.processOOBatch(idArr);
//			} else {
//				Global.G_PPManager_bbms.processOOBatch(idArr);
//			}
			CreateObjectFactory.createPreProcess(gw_type).processOOBatch(idArr);
		}
	}
	/** end add by chenjie(67371) **/
}
