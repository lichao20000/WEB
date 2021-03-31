
package com.linkage.module.gwms.resource.bio;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.resource.dao.AnalyticSimulateSheetDAO;
import com.linkage.module.gwms.util.SocketUtil;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-9-21 下午02:26:08
 * @category com.linkage.module.gwms.resource.bio
 * @copyright 南京联创科技 网管科技部
 */
public class AnalyticSimulateSheetBIO
{

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(AnalyticSimulateSheetBIO.class);
	// 成功的工单逻辑ID
	private List<String> successSheet = new ArrayList<String>();
	// 资料失败的
	private List<String> inforSheet = new ArrayList<String>();
	// 上网失败的
	private List<String> netSheet = new ArrayList<String>();
	// itv失败的
	private List<String> itvSheet = new ArrayList<String>();
	// voip失败的
	private List<String> voipSheet = new ArrayList<String>();
	// 业务类型
	private String servTypeId;
	// 操作类型
	private String operateType;
	// 工单受理时间
	private String dealdate;
	// 设备类型
	private String devType;
	//用户类型
	private String userType="1";
	// 逻辑SN
	private String username;
	// 属地
	private String cityId;
	// 局向
	private String officeId = "";
	// 小区
	private String zoneId = "";
	// 接入方式 (订单类型)
	private int orderType;
	// 联系人
	private String linkman;
	// 联系人电话
	private String linkphone;
	// 联系人Email
	private String linkEmail="";
	// 联系人手机
	private String linkMobile="";
	// 家庭住址
	private String homeAddr;
	// 身份证号码
	private String credNo="";
	//客户ID
	private String customerId="";
	//客户帐号
	private String customerAccount="";
	//客户密码
	private String customerPwd="";
	//终端规格
	private String specId;
	
	private AnalyticSimulateSheetDAO dao;
	// private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName;

	public String simulateSheet(List<String[]> arr)
	{
		Map cityMap = new HashMap();
		Map accessTypeMap = new HashMap();
		// 默认失败的返回值
		String infoSheetResult = "";
		String itvSheetResult = "";
		String netSheetResult = "";
		String voipSheetResult = "";
		cityMap = dao.getCityMap();
		accessTypeMap = dao.getAccessType();
		logger.warn("simulateSheet:" + arr.size());
		for (int i = 0; i < arr.size(); i++)
		{
			String[] temp = arr.get(i);
			 if(temp.length<6){
		          continue;
	         }
			// 逻辑id为空
		   if(("".equals(temp[1]) || null == temp[1] ) || ("".equals(temp[2]) || null == temp[2]))
			{
				continue;
			}
			// 发资料工单
			// 判断解析出来的属地是否存在
			String cityID = StringUtil.getStringValue(cityMap.get(temp[5]));
			String accessTypeID = StringUtil.getStringValue(accessTypeMap.get(temp[2]));
			username = temp[1];
			logger.debug("逻辑sn"+username);
			if ("".equals(cityID) || null == cityID)
			{
				inforSheet.add(temp[1]);
				continue;
			}
			if ("".equals(accessTypeID) || null == accessTypeID)
			{
				orderType = 3;
			}
			else
			{
				orderType = Integer.parseInt(accessTypeID);
			}
			cityId = cityID;
			devType = "e8c";
			userType = "1" ;
			dealdate = temp[0];
			if ("".equals(dealdate) || null == dealdate)
			{
				inforSheet.add(temp[1]);
				continue;
			}
		    dealdate = transferDateFormate(dealdate);
		    logger.warn("工单时间"+dealdate);
		    specId ="e8cp42";
		    linkman = temp[3];
		    linkphone =  temp[15];
			homeAddr = temp[4];
			if(StringUtil.IsEmpty(linkman)){
				linkman ="";
			}
			if(StringUtil.IsEmpty(linkphone)){
				linkphone ="";
			}
			if(StringUtil.IsEmpty(homeAddr)){
				homeAddr ="";
			}
			if("企业".equals(temp[18])){
				userType ="2";
			}
		    if(temp.length<20){
		    	 customerId = "";
				 customerAccount = "";
				 customerPwd = "";
		    }
		    if (temp.length==20){
		    	customerId = temp[19];
		    }
		    if (temp.length==21){
		    	customerId = temp[19];
		    	customerAccount = temp[20];
		    }
			if (temp.length==22){
				customerId = temp[19];
		    	customerAccount = temp[20];
		    	customerPwd = temp[21];
			}
		    if ( null == customerId)
			{
		    	customerId = "";
			}
		    if ( null == customerAccount)
			{
		    	customerAccount = "";
			}
		    if ( null == customerPwd)
			{
		    	customerPwd = "";
			}
		    infoSheetResult = sendOpenSheet("20", "1", dealdate, userType, username,
					cityId, officeId, zoneId, orderType, linkman, linkphone, linkEmail,
					linkMobile, homeAddr, credNo,customerId,customerAccount,customerPwd,specId);
			logger.warn("资料工单结果：" + infoSheetResult);
			if (!"0".equals(infoSheetResult.substring(0, 1)))
			{
				inforSheet.add(temp[1]);
				continue;
			}
			else{
				successSheet.add(temp[1]);
			}
			// 上网业务发送工单
			netSheetResult = sendNetSheet(temp, accessTypeID);
			// itv发送工单
			itvSheetResult = sendITVSheet(temp,netSheetResult);
			// voip工单 ip地址获取方式
			voipSheetResult = sendVOIPSheet(temp, accessTypeID,itvSheetResult);
			
		}
		return "aaa";
	}

	public String sendVOIPSheet(String[] temp, String accessTypeID,String itvSheetResult)
	{  
		// 业务电话号码
		String voipTelepone = temp[15];
		
		// 终端物理标示（TID与语音端口间存在对应关系）
		String voipPort = temp[14];
		// 终端标识   域名
		String regId = temp[13];
		// MGC服务器ip地址
		String mgcIp = temp[9];
		// MGC服务器端口
		String mgcPortstr = temp[10];
		// MGC备用服务器地址
		String standMgcIp = temp[11];
		// MGC备用服务器端口
		String standMgcPortstr = temp[12];
		
		if (("".equals(voipTelepone) || null == voipTelepone)&& ("".equals(voipPort) || null == voipPort) &&("".equals(regId) || null == regId))
		{
		if (("".equals(mgcIp) || null == mgcIp)&&("".equals(mgcPortstr) || null == mgcPortstr) && ("".equals(standMgcIp) || null == standMgcIp) &&("".equals(standMgcPortstr) || null == standMgcPortstr))
			{
			return "";
			}
		}
		//宁夏的上网方式固定DHCP
	    String wanType = "DHCP";
	    /**
		String ipaddress = temp[10];
		String ipmask = temp[12];
		String gateway = temp[11];
			if ("".equals(wanType) || null == wanType)
		{
			voipSheet.add(temp[1]);
			logger.debug("[{}]VOIP：IP获取方式为空！",temp[1]);
			return "";
		}else 
		if("静态IP".equals(wanType)){
			wanType = "3";
			if ("".equals(ipaddress) || null == ipaddress)
			{
				voipSheet.add(temp[1]);
				logger.debug("[{}]VOIP：IP地址为空！",temp[1]);
				return "";
			}
			// 子网掩码
			if ("".equals(ipmask) || null == ipmask)
			{
				voipSheet.add(temp[1]);
				logger.debug("[{}]VOIP：子网掩码为空！",temp[1]);
				return "";
			}
			// 网关
			if ("".equals(gateway) || null == gateway)
			{
				voipSheet.add(temp[1]);
				logger.debug("[{}]VOIP：网关为空！",temp[1]);
				return "";
			}
		}else 
		**/
		if ("DHCP".equals(wanType)) {
			wanType = "4";
		}else {
			voipSheet.add(temp[1]);
			logger.debug("[{}]VOIP：IP获取方式不正确！",temp[1]);
			return "";
		}
		 // DNS值 入空
		String adslSer = "";
		// vpi vci vlanid
		String vpiid = "";
		String vciid = "";
		String valnID = temp[8];
		if ("1".equals(accessTypeID))
		{
			if (valnID.indexOf("/") < 0 && accessTypeID.split("/").length != 2)
			{
				voipSheet.add(temp[1]);
				logger.debug("[{}]VOIP，PVC不为空！",temp[1]);
				return "";
			}
			vpiid = valnID.split("/")[0];
			vciid = valnID.split("/")[1];
			if ("".equals(vpiid) || null == vpiid || "".equals(vciid) || null == vciid)
			{
				voipSheet.add(temp[1]);
				logger.debug("[{}]VOIP，PVC为空！",temp[1]);
				return "";
			}
			valnID = "";
		}
		else
		{
			vpiid = "";
			vciid = "";
		}
	
		if ("".equals(mgcIp) || null == mgcIp)
		{
			voipSheet.add(temp[1]);
			logger.debug("[{}]VOIP，MGC服务器IP地址为空！",temp[1]);
			return "";
		}
	
		
		int mgcPort = 0;
		try
		{
			mgcPort = Integer.parseInt(mgcPortstr);
		}
		catch (Exception e)
		{
			voipSheet.add(temp[1]);
			logger.debug("[{}]VOIP，MGC服务器端口错误！",temp[1]);
			return "";
		}
		
		if ("".equals(standMgcIp) || null == standMgcIp)
		{
			voipSheet.add(temp[1]);
			logger.debug("[{}]VOIP，MGC备用服务器IP地址为空！",temp[1]);
			return "";
		}
		
		int standMgcPort = 0;
		try
		{
			standMgcPort = Integer.parseInt(standMgcPortstr);
		}
		catch (Exception e)
		{
			voipSheet.add(temp[1]);
			logger.debug("[{}]VOIP，MGC备用服务器端口错误！",temp[1]);
			return "";
		}
	
		if ("".equals(voipTelepone) || null == voipTelepone)
		{
			voipSheet.add(temp[1]);
			logger.debug("[{}]VOIP，业务电话号码为空！",temp[1]);
			return "";
		}
		
		if ("".equals(voipPort) || null == voipPort)
		{
			voipSheet.add(temp[1]);
			logger.debug("[{}]VOIP，终端物理标识为空！",temp[1]);
			return "";
		}
		
		if ("".equals(regId) || null == regId)
		{
			voipSheet.add(temp[1]);
			logger.debug("[{}]VOIP，终端标识类型为空！",temp[1]);
			return "";
		}
		// 终端标识类型 定为 域名
		String regIdType = "1";
		String voipSheetResult = "";
		/**
		String userId = dao.getUserIdByTabHgwcustomer(username);
		logger.debug("用户是否存在：" + userId);
		int count = dao.checkHgwcustServInfo(userId);
		logger.debug("是否存在VOIP H.248：" + count);
		// 判断系统中是否存在VOIP H.248业务
		if (count <= 0)
		{ // 系统中不存在VOIP H.248业务 新增hgwcust_serv_info，tab_sip_info，tab_voip_serv_param
			voipSheetResult = openAccountAdd(userId, "14", "1", username, wanType
					, adslSer, valnID, vpiid, vciid, cityId,
					mgcIp, mgcPort, standMgcIp, standMgcPort, voipTelepone, devType,
					voipPort, regId, regIdType);
		}
		else
		{ // 系统中存在VOIP H.248业务 更新hgwcust_serv_info
			voipSheetResult = openAccountUpdate(userId, "14", "1", username, wanType, 
					adslSer, valnID, vpiid, vciid, cityId,
					mgcIp, mgcPort, standMgcIp, standMgcPort, voipTelepone, devType,
					voipPort, regId, regIdType);
		}
		**/
		voipSheetResult = sendVoipOpenSheet("15","1",username,dealdate,userType,valnID,cityId,
				mgcIp,mgcPort,standMgcIp,standMgcPort,voipTelepone,voipPort,regId,regIdType);
		
		logger.debug("voip执行结果：" + voipSheetResult);
		if (!"0".equals(voipSheetResult.substring(0, 1)))
		{
			voipSheet.add(temp[1]);
			return "kong";
		}
		successSheet.add(temp[1]);
		return voipSheetResult;
	}
	/**
	 * 发送VOIP  h248工单
	 * @param servTypeId
	 * @param operTypeId
	 * @param username
	 * @param dealdate
	 * @param userType
	 * @param valnID
	 * @param cityId
	 * @param mgcIp
	 * @param mgcPort
	 * @param standMgcIp
	 * @param standMgcPort
	 * @param voipTelepone
	 * @param voipPort
	 * @param regId
	 * @param regIdType
	 * @return
	 */
	private String sendVoipOpenSheet(String servTypeId, String operTypeId,
			String username, String dealdate, String userType,
			String valnID, String cityId, String mgcIp, int mgcPort,
			String standMgcIp, int standMgcPort, String voipTelepone,
			String voipPort, String regId, String regIdType) {
		logger.debug("sendVoipOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operTypeId).append("|||");
		sbuffer.append(dealdate).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(voipTelepone).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(regId).append("|||");
		sbuffer.append(regIdType).append("|||");
		sbuffer.append(mgcIp).append("|||");
		sbuffer.append(mgcPort).append("|||");
		sbuffer.append(standMgcIp).append("|||");
		sbuffer.append(standMgcPort).append("|||");
		sbuffer.append(voipPort).append("|||");
		sbuffer.append("45").append("LINKAGE");
		return this.sendSheet(sbuffer.toString());
	}

	public String sendITVSheet(String[] temp,String  netSheetResult)
	{
		String iptvUsername = temp[16];
		
		//itv个数
		String itvNum=temp[17];
		
		if(("".equals(iptvUsername) || null == iptvUsername)&&("".equals(itvNum) || null == itvNum))
		{
		   return "kong";
		}
		
		
		int servNum = 0;
		if ("".equals(iptvUsername) || null == iptvUsername)
		{
			itvSheet.add(temp[1]);
			return "kong";
		}
		if ("".equals(temp[17]) || null == temp[17])
		{
			itvSheet.add(temp[1]);
			return "kong";
		}
		else
		{
			servNum = Integer.parseInt(temp[17]);
		}
		logger.warn("itv个数" + servNum);
		String iptvLanPort = "";
		String itvSheetResult = sendIptvOpenSheet("21", "1", dealdate, username, cityId,
				iptvUsername, servNum, iptvLanPort, userType);
		logger.warn("itv工单结果:" + itvSheetResult);
		if (!"0".equals(itvSheetResult.substring(0, 1)))
		{
			itvSheet.add(temp[1]);
			return "kong";
		}
		successSheet.add(temp[1]);
		return itvSheetResult;
	}

	public String sendNetSheet(String[] temp, String accessTypeID)
	{       //工单授理时间
		//String time=temp[0];
		// 属地  资料里已经判断过
		//String place=temp[5];
		
		if(temp.length<9){
			return "kong";
		}
		String valnID = temp[8];
		String netUsername = temp[6];
		String netPassword = temp[7];
		if( ("".equals(netUsername)||null == netUsername)  && ("".equals(valnID) || null == valnID)){
			if("".equals(netPassword)||null == netPassword){
				return "kong";	
			}
			
		}
		
		if ("".equals(netUsername) || null == netUsername)
		{
			netSheet.add(temp[1]);
			return "kong";
		}
		if ("".equals(netPassword) || null == netPassword)
		{
			netSheet.add(temp[1]);
			return "kong";
		}
		
		if ("1".equals(accessTypeID))
		{
			if (valnID.indexOf("/") < 0)
			{
				netSheet.add(temp[1]);
				return "kong";
			}
			else
			{
				String vpiid = valnID.split("/")[0];
				String vciid = valnID.split("/")[1];
				if ("".equals(vpiid) && "".equals(vciid))
				{
					netSheet.add(temp[1]);
					return "kong";
				}
			}
		}
		else
		{
			if ("".equals(valnID) || null == valnID)
			{
				netSheet.add(temp[1]);
				return "kong";
			}
		}
		String netSheetResult = sendNetOpenSheet("22", "1", dealdate, username, cityId,
				netUsername, netPassword, valnID, userType);
		logger.warn("上网业务工单结果：" + netSheetResult);
		if (!"0".equals(netSheetResult.substring(0, 1)))
		{
			netSheet.add(temp[1]);
			return"kong";
		}
		successSheet.add(temp[1]);
		return netSheetResult;
	}

	/**
	 * 输入excel文件，解析后返回ArrayList
	 * 
	 * @param file
	 *            输入的excel文件
	 * @return ArrayList<String>
	 */
	public List<String[]> analyticFile(File file)
	{
		List<String[]> arr = new ArrayList<String[]>();
		Workbook wwb = null;
		Sheet ws = null;
		try
		{
			// 读取excel文件
			wwb = Workbook.getWorkbook(file);
			// 总sheet数
			// int sheetNumber = wwb.getNumberOfSheets();
			int sheetNumber = 1;
			// logger.warn("sheetNumber:" + sheetNumber);
			for (int m = 0; m < sheetNumber; m++)
			{
				ws = wwb.getSheet(m);
				// 当前页总记录行数和列数
				int rowCount = ws.getRows();
				logger.warn("rowCount:" + rowCount);
				int columeCount = ws.getColumns();
				logger.warn("columeCount:" + columeCount);
				if (101 < rowCount)
				{
					rowCount = 101;
				}
				// 第一行为字段名，所以行数大于1才执行
				if (rowCount > 1 && columeCount > 0)
				{
					// 取当前页所有值放入list中
					for (int i = 1; i < rowCount; i++)
					{
						// String temp = ws.getCell(0, i).getContents().trim();
						// if(null!=temp && !"".equals(temp)){
						// arr.add(ws.getCell(0, i).getContents().trim());
						Cell[] cell = ws.getRow(i);
						// logger.warn("每行的列数："+cell.length);
						String[] str = new String[cell.length];
						for (int j = 0; j < str.length; j++)
						{
							str[j] = cell[j].getContents().trim();
							// logger.warn("*********" + str[j]);
						}
						arr.add(str);
						// }
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				wwb.close();
			}
			catch (Exception e)
			{
				logger.debug(e.getMessage());
			}
		}
		return arr;
	}

	/**
	 * 生成资料接口开户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param devType
	 * @param username
	 * @param cityId
	 * @param officeId
	 * @param zoneId
	 * @param orderType
	 * @param linkman
	 * @param linkphone
	 * @param linkEmail
	 * @param linkMobile
	 * @param homeAddr
	 * @param credNo
	 * @param customerId
	 * @param customerAccount
	 * @param customerPwd
	 * @param specId
	 * @return
	 */
	public String sendOpenSheet(String servTypeId, String operateType, String dealdate,
			String devType, String username, String cityId, String officeId,
			String zoneId, int orderType, String linkman, String linkphone,
			String linkEmail, String linkMobile, String homeAddr, String credNo,
			String customerId,String customerAccount,String customerPwd,String specId)
	{
		logger.debug("sendOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(dealdate).append("|||");
		sbuffer.append(devType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(officeId).append("|||");
		sbuffer.append(zoneId).append("|||");
		sbuffer.append(orderType).append("|||");
		sbuffer.append(linkman).append("|||");
		sbuffer.append(linkphone).append("|||");
		sbuffer.append(linkEmail).append("|||");
		sbuffer.append(linkMobile).append("|||");
		sbuffer.append(homeAddr).append("|||");
		sbuffer.append(credNo).append("|||");
		sbuffer.append(customerId).append("|||");
		sbuffer.append(customerAccount).append("|||");
		sbuffer.append(customerPwd).append("|||");
	    sbuffer.append(specId).append("LINKAGE");
	    logger.warn("资料工单："+sbuffer.toString());
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * 时间格式转换
	 * 
	 * @param dealdate
	 *            yyy-MM-dd HH:mm:ss
	 * @return yyyyMMddHHmmss
	 */
	private String transferDateFormate(String dealdate)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = null;
		Calendar   calendar   =   Calendar.getInstance(); 
		int hour=calendar.get(Calendar.HOUR_OF_DAY);
		int minute=calendar.get(Calendar.MINUTE);
		int second=calendar.get(Calendar.SECOND);
		dealdate=dealdate+hour+minute+second;
		try
		{
			date = dateFormat.parse(dealdate);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			String str = dateFormat.format(Calendar.getInstance().getTime());
			try
			{
				date = dateFormat.parse(str);
			}
			catch (ParseException e1)
			{
				e1.printStackTrace();
				logger.warn("工单受理时间转换异常");
			}
		}
		dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		logger.warn("工单受理时间："+dateFormat.format(date));
		return dateFormat.format(date);
	}

	/**
	 * 向工单接口发送模拟的BSS工单。正常返回工单接口的回单结果，如果过程中出现问题返回null
	 * 
	 * @param 模拟工单数据
	 * @author zhangsm
	 * @date 2011-05-21
	 * @return String 回单信息
	 */
	public String sendSheet(String bssSheet)
	{
		logger.debug("sendSheet({})", bssSheet);
		if (StringUtil.IsEmpty(bssSheet))
		{
			logger.warn("sendSheet is null");
			return null;
		}
		String server = Global.G_ITMS_Sheet_Server;
		int port = Global.G_ITMS_Sheet_Port;
		String retResult = SocketUtil.sendStrMesg(server, port, bssSheet + "\n");
		return retResult;
	}

	/**
	 * 上网业务开户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param netUsername
	 * @param netPassword
	 * @param vlanId
	 * @return
	 */
	public String sendNetOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String netUsername,
			String netPassword, String vlanId, String usreType)
	{
		logger.debug("sendNetOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(dealdate).append("|||");
		sbuffer.append(usreType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(netUsername).append("|||");
		sbuffer.append(netPassword).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(vlanId).append("LINKAGE");
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * IPTV开户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param voipUsername
	 * @param iptvUsername
	 * @param servNum
	 * @param iptvLanPort
	 * @return
	 */
	public String sendIptvOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String iptvUsername,
			int servNum, String iptvLanPort, String userType)
	{
		logger.warn("sendIptvOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(dealdate).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(iptvUsername).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(servNum).append("|||");
		sbuffer.append(iptvLanPort).append("|||");
		sbuffer.append("43").append("LINKAGE");
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * 新增:资源_业务用户表(hgwcust_serv_info), 资源_SIP服务器信息表(tab_sip_info),
	 * 资源_用户VOIP业务参数表(tab_voip_serv_param)
	 * 
	 * @param userId
	 *            用户ID
	 * @param servTypeId
	 *            业务类型
	 * @param operateType
	 *            开户 OR 销户
	 * @param username
	 *            用户名
	 * @param wanType
	 *            上网方式
	 * @param ipaddress
	 *            IP地址
	 * @param ipmask
	 *            掩码
	 * @param gateway
	 *            网关
	 * @param adslSer
	 *            DNS
	 * @param vlanid
	 *            vlanid
	 * @param vpiid
	 *            vpiid
	 * @param vciid
	 *            ciid
	 * @param cityId
	 *            属地ID
	 * @param mgcIp
	 *            主MGC服务器地址
	 * @param mgcPort
	 *            主MGC服务器端口
	 * @param standMgcIp
	 *            备MGC服务器地址
	 * @param standMgcPort
	 *            备MGC服务器端口
	 * @param voipTelepone
	 *            业务电话号码
	 * @param devType
	 *            设备类型
	 * @param lineId
	 *            线路ID
	 * @param regId
	 *            终端向软交换注册全局唯一标识
	 * @param regIdType
	 *            注册标识类型
	 * @return
	 */
	public String openAccountAdd(String userId, String servTypeId, String operateType,
			String username, String wanType,String ipaddress, 
			   String ipmask, String gateway,
			   String adslSer, String vlanid, String vpiid, String vciid,
			String cityId, String mgcIp, int mgcPort, String standMgcIp,
			int standMgcPort, String voipTelepone, String devType, String voipPort,
			String regId, String regIdType)
	{
		logger
				.debug(
						"openAccountAdd({},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{})",
						new Object[] { userId, servTypeId, operateType, username,
								wanType, adslSer, vlanid,ipaddress, ipmask, gateway,
								vpiid, vciid, cityId, mgcIp, mgcPort, standMgcIp,
								standMgcPort, voipTelepone, devType, voipPort, regId,
								regIdType });
		String strMsg = dao.insertAllTableInfo(userId, servTypeId, operateType, username,
				wanType, ipaddress, ipmask, gateway, adslSer, vlanid, vpiid, vciid,
				mgcIp, mgcPort, standMgcIp, standMgcPort, voipPort, regId, regIdType,
				voipTelepone);
		return strMsg;
	}

	/**
	 * 更新:资源_业务用户表(hgwcust_serv_info), 资源_SIP服务器信息表(tab_sip_info),
	 * 资源_用户VOIP业务参数表(tab_voip_serv_param)
	 * 
	 * @param userId
	 *            用户ID
	 * @param servTypeId
	 *            业务类型
	 * @param operateType
	 *            开户 OR 销户
	 * @param username
	 *            用户名
	 * @param wanType
	 *            上网方式
	 * @param ipaddress
	 *            IP地址
	 * @param ipmask
	 *            掩码
	 * @param gateway
	 *            网关
	 * @param adslSer
	 *            DNS
	 * @param vlanid
	 *            vlanid
	 * @param vpiid
	 *            vpiid
	 * @param vciid
	 *            ciid
	 * @param cityId
	 *            属地ID
	 * @param mgcIp
	 *            主MGC服务器地址
	 * @param mgcPort
	 *            主MGC服务器端口
	 * @param standMgcIp
	 *            备MGC服务器地址
	 * @param standMgcPort
	 *            备MGC服务器端口
	 * @param voipTelepone
	 *            业务电话号码
	 * @param devType
	 *            设备类型
	 * @param lineId
	 *            线路ID
	 * @param regId
	 *            终端向软交换注册全局唯一标识
	 * @param regIdType
	 *            注册标识类型
	 * @return
	 */
	public String openAccountUpdate(String userId, String servTypeId, String operateType,
			String username, String wanType, String ipaddress, 
			   String ipmask, String gateway,
			String adslSer, String vlanid, String vpiid, String vciid,
			String cityId, String mgcIp, int mgcPort, String standMgcIp,
			int standMgcPort, String voipTelepone, String devType, String voipPort,
			String regId, String regIdType)
	{
		logger
				.debug(
						"openAccountUpdate({},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{})",
						new Object[] { userId, servTypeId, operateType, username,
								wanType,  ipaddress, ipmask, gateway, adslSer, vlanid,
								vpiid, vciid, cityId, mgcIp, mgcPort, standMgcIp,
								standMgcPort, voipTelepone, devType, voipPort, regId,
								regIdType });
		StringBuffer strSqlBuffer = new StringBuffer();
		String strMsg = "";
		// 更新 资源_业务用户表
		String strSql_1 = dao.updateHgwcustServInfo(userId, username, wanType,ipaddress, ipmask, gateway, adslSer, vlanid, vpiid, vciid);
		/**
		 * 通过调用上面的dao.updateHgwcustServInfo(),更新好 hgwcust_serv_info后
		 * 根据"line_id"+"user_id"查询 tab_voip_serv_param,确认参数表中是否有相关记录，有则更新，没有则新增
		 */
		String strSql_2 = "";
		String lineId = "";
		if ("A1".equals(voipPort) || "AL1".equals(voipPort) || "AG58900".equals(voipPort))
		{
			lineId = "1";
		}
		else if ("A2".equals(voipPort) || "AL2".equals(voipPort)
				|| "AG58901".equals(voipPort))
		{
			lineId = "2";
		}
		int count = dao.checkTabVoipServParam(userId, lineId);
		if (count <= 0)
		{ // 没有记录，则新增
			strSql_2 = dao.insertInfo(userId, mgcIp, mgcPort, standMgcIp, standMgcPort,
					voipPort, regId, regIdType, voipTelepone);
		}
		else
		{ // 有记录则更新
			strSql_2 = dao.updateInfo(userId, mgcIp, mgcPort, standMgcIp, standMgcPort,
					voipPort, regId, regIdType, voipTelepone);
		}
		strSqlBuffer.append(strSql_1);
		strSqlBuffer.append(";");
		strSqlBuffer.append(strSql_2);
		// 批量执行SQL语句
		int[] iCodes = DataSetBean.doBatch(strSqlBuffer.toString());
		if (iCodes != null && iCodes.length > 0)
		{
			strMsg = "开户成功！";
		}
		else
		{
			strMsg = "开户失败！";
		}
		return strMsg;
	}

	public AnalyticSimulateSheetDAO getDao()
	{
		return dao;
	}

	public void setDao(AnalyticSimulateSheetDAO dao)
	{
		this.dao = dao;
	}

	public List<String> getSuccessSheet()
	{
		return successSheet;
	}

	public void setSuccessSheet(List<String> successSheet)
	{
		this.successSheet = successSheet;
	}

	public List<String> getInforSheet()
	{
		return inforSheet;
	}

	public void setInforSheet(List<String> inforSheet)
	{
		this.inforSheet = inforSheet;
	}

	public List<String> getNetSheet()
	{
		return netSheet;
	}

	public void setNetSheet(List<String> netSheet)
	{
		this.netSheet = netSheet;
	}

	public List<String> getItvSheet()
	{
		return itvSheet;
	}

	public void setItvSheet(List<String> itvSheet)
	{
		this.itvSheet = itvSheet;
	}

	public List<String> getVoipSheet()
	{
		return voipSheet;
	}

	public void setVoipSheet(List<String> voipSheet)
	{
		this.voipSheet = voipSheet;
	}

	public String getServTypeId()
	{
		return servTypeId;
	}

	public void setServTypeId(String servTypeId)
	{
		this.servTypeId = servTypeId;
	}

	public String getOperateType()
	{
		return operateType;
	}

	public void setOperateType(String operateType)
	{
		this.operateType = operateType;
	}

	public String getDealdate()
	{
		return dealdate;
	}

	public void setDealdate(String dealdate)
	{
		this.dealdate = dealdate;
	}

	public String getDevType()
	{
		return devType;
	}

	public void setDevType(String devType)
	{
		this.devType = devType;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getOfficeId()
	{
		return officeId;
	}

	public void setOfficeId(String officeId)
	{
		this.officeId = officeId;
	}

	public String getZoneId()
	{
		return zoneId;
	}

	public void setZoneId(String zoneId)
	{
		this.zoneId = zoneId;
	}

	public int getOrderType()
	{
		return orderType;
	}

	public void setOrderType(int orderType)
	{
		this.orderType = orderType;
	}

	public String getLinkman()
	{
		return linkman;
	}

	public void setLinkman(String linkman)
	{
		this.linkman = linkman;
	}

	public String getLinkphone()
	{
		return linkphone;
	}

	public void setLinkphone(String linkphone)
	{
		this.linkphone = linkphone;
	}

	public String getLinkEmail()
	{
		return linkEmail;
	}

	public void setLinkEmail(String linkEmail)
	{
		this.linkEmail = linkEmail;
	}

	public String getLinkMobile()
	{
		return linkMobile;
	}

	public void setLinkMobile(String linkMobile)
	{
		this.linkMobile = linkMobile;
	}

	public String getHomeAddr()
	{
		return homeAddr;
	}

	public void setHomeAddr(String homeAddr)
	{
		this.homeAddr = homeAddr;
	}

	public String getCredNo()
	{
		return credNo;
	}

	public void setCredNo(String credNo)
	{
		this.credNo = credNo;
	}
	public String[] getTitle()
	{
		return title;
	}

	
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	
	public String getFileName()
	{
		return fileName;
	}

	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerAccount() {
		return customerAccount;
	}

	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}

	public String getCustomerPwd() {
		return customerPwd;
	}

	public void setCustomerPwd(String customerPwd) {
		this.customerPwd = customerPwd;
	}

	public String getSpecId() {
		return specId;
	}

	public void setSpecId(String specId) {
		this.specId = specId;
	}
	
}
