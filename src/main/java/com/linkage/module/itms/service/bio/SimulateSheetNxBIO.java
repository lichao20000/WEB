package com.linkage.module.itms.service.bio;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.SocketUtil;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.service.dao.SimulateSheetNxDAO;

public class SimulateSheetNxBIO {
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(SimulateSheetNxBIO.class);
	private SimulateSheetNxDAO dao;
	public String checkUsername(String username){
		List<Map<String,String>> userList = dao.getUserInfo(username);
		int size = userList.size();
		if(size == 1 ){
			return "1#"+userList.get(0).get("orderType");
		}
		else if(size > 1)
		{
			return "0#请输入完整的逻辑SN";
		}else{
			return "-1#逻辑SN不存在，请先走建设流程";
		}
	}

	
	/**
	 *  判断页面输入的"逻辑SN"(用户名)在系统中是否存在
	 *  存在则返回userId，不存在则返回空字符串""
	 * 
	 * @param username
	 * @return
	 */
	public String checkUserName(String username) {
		logger.debug("checkUserName({})", new Object[]{username});
		return dao.getUserIdByTabHgwcustomer(username);
	}
	
	
	/**
	 * 判断系统中是否存在VOIP H.248业务
	 * 
	 * @param userId
	 * @return
	 */
	public int checkVOIPH248(String userId) {
		logger.debug("checkVOIPH248({})", new Object[]{userId});
		return dao.checkHgwcustServInfo(userId);
	}
	
	
	/**
	 * 新增:资源_业务用户表(hgwcust_serv_info), 资源_SIP服务器信息表(tab_sip_info), 资源_用户VOIP业务参数表(tab_voip_serv_param)
	 * 
	 * @param userId		用户ID
	 * @param servTypeId    业务类型
	 * @param operateType   开户 OR 销户
	 * @param username		用户名
	 * @param wanType       上网方式
	 * @param ipaddress		IP地址
	 * @param ipmask		掩码
	 * @param gateway		网关
	 * @param adslSer		DNS
	 * @param vlanid		vlanid
	 * @param vpiid			vpiid			
	 * @param vciid			ciid
	 * @param cityId		属地ID
	 * @param mgcIp			主MGC服务器地址
	 * @param mgcPort		主MGC服务器端口
	 * @param standMgcIp	备MGC服务器地址
	 * @param standMgcPort	备MGC服务器端口
	 * @param voipTelepone	业务电话号码
	 * @param devType		设备类型
	 * @param lineId		线路ID
	 * @param regId			终端向软交换注册全局唯一标识
	 * @param regIdType		注册标识类型
	 * @return
	 */
	public String openAccountAdd(String userId, String servTypeId, String operateType, String username, String wanType, String ipaddress, String ipmask, 
						String gateway, String adslSer, String vlanid, String vpiid, String vciid, String cityId, String mgcIp, int mgcPort, 
						String standMgcIp, int standMgcPort, String voipTelepone, String devType, String voipPort, String regId, String regIdType) {
		
		logger.debug("openAccountAdd({},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{})", 
					  new Object[]{userId, servTypeId, operateType, username, wanType, ipaddress, 
				                   ipmask, gateway, adslSer, vlanid, vpiid, vciid, cityId, mgcIp, 
				                   mgcPort, standMgcIp, standMgcPort, voipTelepone,devType,voipPort, 
				                   regId, regIdType});
		
		String strMsg = dao.insertAllTableInfo(userId, servTypeId, operateType, username, wanType, ipaddress, 
													  ipmask, gateway, adslSer, vlanid, vpiid, vciid, mgcIp, mgcPort, 
													  standMgcIp, standMgcPort, voipPort, regId, regIdType, voipTelepone);
		
		return strMsg;
	}
	
	
	
	
	/**
	 * 更新:资源_业务用户表(hgwcust_serv_info), 资源_SIP服务器信息表(tab_sip_info), 资源_用户VOIP业务参数表(tab_voip_serv_param)
	 * 
	 * @param userId		用户ID
	 * @param servTypeId    业务类型
	 * @param operateType   开户 OR 销户
	 * @param username		用户名
	 * @param wanType       上网方式
	 * @param ipaddress		IP地址
	 * @param ipmask		掩码
	 * @param gateway		网关
	 * @param adslSer		DNS
	 * @param vlanid		vlanid
	 * @param vpiid			vpiid			
	 * @param vciid			ciid
	 * @param cityId		属地ID
	 * @param mgcIp			主MGC服务器地址
	 * @param mgcPort		主MGC服务器端口
	 * @param standMgcIp	备MGC服务器地址
	 * @param standMgcPort	备MGC服务器端口
	 * @param voipTelepone	业务电话号码
	 * @param devType		设备类型
	 * @param lineId		线路ID
	 * @param regId			终端向软交换注册全局唯一标识
	 * @param regIdType		注册标识类型
	 * @return
	 */
	public String openAccountUpdate(String userId, String servTypeId, String operateType, String username, String wanType, String ipaddress, String ipmask, 
						String gateway, String adslSer, String vlanid, String vpiid, String vciid, String cityId, String mgcIp, int mgcPort, 
						String standMgcIp, int standMgcPort, String voipTelepone, String devType, String voipPort, String regId, String regIdType) {
		
		logger.debug("openAccountUpdate({},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{})", 
					  new Object[]{userId, servTypeId, operateType, username, wanType, ipaddress, 
				                   ipmask, gateway, adslSer, vlanid, vpiid, vciid, cityId, mgcIp, 
				                   mgcPort, standMgcIp, standMgcPort, voipTelepone,devType,voipPort, 
				                   regId, regIdType});
		
		StringBuffer strSqlBuffer = new StringBuffer();
		String strMsg = "";
		
		// 更新 资源_业务用户表
		String strSql_1 = dao.updateHgwcustServInfo(userId, username, wanType, ipaddress, ipmask, gateway, adslSer, vlanid, vpiid, vciid);
		
		/**
		 * 通过调用上面的dao.updateHgwcustServInfo(),更新好 hgwcust_serv_info后
		 * 根据"line_id"+"user_id"查询  tab_voip_serv_param,确认参数表中是否有相关记录，有则更新，没有则新增
		 */
		String strSql_2 = "";
		String lineId = "";
		if("A1".equals(voipPort) || "AL1".equals(voipPort) || "AG58900".equals(voipPort)){
			lineId = "1";
		}else if("A2".equals(voipPort) || "AL2".equals(voipPort) || "AG58901".equals(voipPort)){
			lineId = "2";
		}
		int count = dao.checkTabVoipServParam(userId, lineId);
		if(count <= 0){ // 没有记录，则新增
			strSql_2 = dao.insertInfo(userId, mgcIp, mgcPort, standMgcIp, standMgcPort, voipPort, regId ,regIdType, voipTelepone);
		}else{   // 有记录则更新
			strSql_2 = dao.updateInfo(userId, mgcIp, mgcPort, standMgcIp, standMgcPort, voipPort, regId ,regIdType, voipTelepone);
		}
		
		
		strSqlBuffer.append(strSql_1);
		strSqlBuffer.append(";");
		strSqlBuffer.append(strSql_2);
		
		// 批量执行SQL语句
		int[] iCodes = DataSetBean.doBatch(strSqlBuffer.toString());
		
		if (iCodes != null && iCodes.length > 0) {
			strMsg = "开户成功！";
		} else {
			strMsg = "开户失败！";
		}
		
		return strMsg;
	}
	
	
	
	/**
	 * VOIP销户工单
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param voipTelepone
	 * @return
	 */
	public String sendVoipStopSheet(String servTypeId,String operateType,String dealdate,String username,String cityId,String voipTelepone) {
		logger.debug("sendStopSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(voipTelepone).append("FROMWEB");
		return this.sendSheet(sbuffer.toString());
	}
	
	
	/**
	 * 向Eserver发送工单。正常返回工单接口的回单结果，如果过程中出现问题返回null
	 * 
	 * @param 模拟工单数据
	 * @author zhangsm
	 * @date 2011-05-21
	 * @return String 回单信息
	 */
	public String sendSheet(String bssSheet){
		logger.debug("sendSheet({})", bssSheet);
		if(StringUtil.IsEmpty(bssSheet)){
			logger.warn("sendSheet is null");
			return null;
		}
		String server = Global.G_ITMS_Sheet_Server;
		int port = Global.G_ITMS_Sheet_Port;
		String retResult = SocketUtil.sendStrMesg(server, port, bssSheet + "\n");
		return retResult;
	}

	
	/**
	 * 时间格式转换
	 * @param dealdate yyy-MM-dd HH:mm:ss
	 * @return yyyyMMddHHmmss
	 */
	private String transferDateFormate(String dealdate){
		DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
		Date date = null;
		try{
			date = dateFormat.parse(dealdate);
		}catch (ParseException e){
			e.printStackTrace();
			logger.warn("工单受理时间转换异常");
		}
		dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(date);
	}
	
	public SimulateSheetNxDAO getDao()
	{
		return dao;
	}
	
	public void setDao(SimulateSheetNxDAO dao)
	{
		this.dao = dao;
	}
}
