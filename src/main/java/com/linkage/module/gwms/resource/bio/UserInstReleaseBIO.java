/**
 * 
 */
package com.linkage.module.gwms.resource.bio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ResourceBind.BindInfo;
import ResourceBind.ResultInfo;
import ResourceBind.UnBindInfo;
import bio.confTaskView.StrategyConfigBio;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.resource.dao.UserInstReleaseDAO;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.ResourceBindInterface;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-11-5
 * @category com.linkage.module.gwms.resource.bio
 * 
 */
public class UserInstReleaseBIO {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(UserInstReleaseBIO.class);

	UserInstReleaseDAO dao;
	StrategyConfigBio strategyConfigBio;
	
	private boolean rsFlag = true;
	
	/**
	 * @return the dao
	 */
	public UserInstReleaseDAO getDao() {
		return dao;
	}

	/**
	 * @param dao the dao to set
	 */
	public void setDao(UserInstReleaseDAO dao) {
		this.dao = dao;
	}

	/**
	 * 查询未绑定设备
	 * 
	 * @param deviceNo
	 *            必须
	 * @param cityId
	 *            必须
	 * @return
	 */
	public List queryDevice(String deviceNo, String cityId,String loopbackIp,int gwType) {
		logger.debug("UserInstReleaseBIO=>queryDevice(deviceNo:{},cityId:{})",
				deviceNo, cityId);

		return dao.getDeviceInfoByNoBind(cityId, deviceNo, loopbackIp, gwType);

	}
	
	/**
	 * 查询未绑定设备
	 * 
	 * @param deviceNo
	 *            必须
	 * @param cityId
	 *            必须
	 * @return
	 */
	public List queryDevice(String deviceNo, String cityId,String loopbackIp) {
		logger.debug("UserInstReleaseBIO=>queryDevice(deviceNo:{},cityId:{})",
				deviceNo, cityId);

		return dao.getDeviceInfoByNoBind(cityId, deviceNo, loopbackIp, LipossGlobals
				.SystemType());

	}
	
	/**
	 * 查询设备信息，绑定不绑定不限制
	 * 
	 * @param deviceId 设备Id 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getDeviceInfo(String deviceId) {
		logger.debug("UserInstReleaseBIO=>getDeviceInfo(deviceId:{})",deviceId);
		
		return dao.getDeviceInfo(deviceId, null, null, null, LipossGlobals.SystemType());
	}

	/**
	 * 查询用户信息
	 * 
	 * @param cityId  必须
	 * @param username  |
	 * @param deviceSN  |username、deviceSN必须传一个，否则返回size为0的List实例
	 * 					|安装时，传入username,deviceSN传入null
	 * 					|解绑时，传入deviceSN，username传入null
	 * @param servUsername 业务用户账号
	 * @param isNoBind  如果是查询未绑定，则传入true
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public List queryUser(String gw_type,String cityId, String username,String nameType,String deviceSN, String bindFlag) {
		logger.debug("UserInstReleaseBIO=>queryUser(deviceNo:{},cityId:{},deviceSN:{},isNoBind:{})",
				new Object[]{cityId, username,nameType,deviceSN,bindFlag});

		List list;
	//	if(1==LipossGlobals.SystemType()){
		if("1".equals(gw_type)){
			list = dao.getUserInfoByITMS(cityId, username, nameType,deviceSN);
		}else{
			list = dao.getUserInfoByBBMS(cityId, username,deviceSN);
		}
		List<Map<String, String>> rsList = new ArrayList<Map<String, String>>();
		
		if ("noBind".equals(bindFlag)) {
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				String deviceId = String.valueOf(map.get("device_id"));
				if (null == deviceId || "".equals(deviceId) || "null".equals(deviceId)) {
					rsList.add(map);
				}
			}
		}else if("bind".equals(bindFlag)){
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				String deviceId = String.valueOf(map.get("device_id"));
				if (null != deviceId && !"".equals(deviceId) && !"null".equals(deviceId)) {
					rsList.add(map);
				}
			}
		}else{
			rsList.addAll(list);
		}

		return rsList;
	}

	/**
	 * BBMS手工安装
	 * 
	 * @param accOid 登陆人ID
	 * @param userId 用户ID
	 * @param username 用户账号
	 * @param userCityId 用户属地
	 * @param deviceId 设备ID
	 * @param deviceCityId 设备属地
	 * @param oui 设备oui
	 * @param deviceNo 设备序列号
	 * @param customerId 客户ID
	 * @param dealstaff 操作人
	 * @param password 操作人的密码
	 * @return 操作信息提示
	 */
	public String bbmsInst(long accOid,String userId, String username, String userCityId,
			String deviceId, String deviceCityId, String oui, String deviceNo,
			String customerId, String dealstaff,int userFlag, String password) {
		logger.debug("UserInstReleaseBIO=>bbmsInst(userId:{},username:{},userCityId:{},deviceId:{},deviceCityId:{},oui:{},deviceNo:{},dealstaff:{},userFlag:{}，password:{})",
				new Object[] {userId,username,userCityId,deviceId,deviceCityId,oui,deviceNo,dealstaff,userFlag,password});

		//返回结果
		String msg = "";
		ResourceBindInterface corba = CreateObjectFactory.createResourceBind(LipossGlobals.getGw_Type(deviceId));
		
		BindInfo[] arr = new BindInfo[1];
		arr[0] = new BindInfo();
		arr[0].accOid = userId;
		arr[0].accName = dealstaff;
		arr[0].username = username;
		arr[0].deviceId = deviceId;
		arr[0].userline = 1;
		
		ResultInfo rs = corba.bind(arr);
		if(rs == null)
		{
			msg = "绑定失败，系统内部错误";
		}
		else
		{
			//String status = rs.status;
			// 成功
			if(rs.resultId[0].equals("1"))
			{
				msg = "绑定" + Global.G_ResourceBind_statusCode.get(Integer.parseInt(rs.resultId[0]));
				return msg;
			}
			// 失败
			else
			{
				// 获取相关错误码
				msg = "绑定失败，" + Global.G_ResourceBind_resultCode.get(Integer.parseInt(rs.resultId[0]));
			}
		}
		
		return msg;
//		
//		//返回结果
//		String rsMessage = "";
//		String gw_type = LipossGlobals.getGw_Type(deviceId);
//		
//		StringBuffer sbSQL = new StringBuffer();
//		long userIdTemp;
//		
//		/**
//		 * 所有的属地都以用户属地为准
//		 */
//		String cityId = userCityId;
//		
//		userIdTemp = Long.parseLong(userId);
//		sbSQL.append(dao.getSQLByInstUpdEgwUser(userIdTemp, deviceId, oui, deviceNo));
//		
//		/**
//		 * 更新设备表
//		 * 更新权限域表
//		 * 
//		 */
//		sbSQL.append(";");
//		sbSQL.append(dao.getSQLByInstUpdDevice(cityId, customerId, deviceId));
//		sbSQL.append(";");
//		sbSQL.append(dao.getSQLByDelResArea(deviceId));
//		if(!"00".equals(cityId)){
//			List<String> list = AreaDAO.getAllPAreaIdByAreaId(AreaDAO.getAreaIdByCityId(cityId));
//			for(int i=0;i<list.size();i++){
//				sbSQL.append(";");
//				sbSQL.append(dao.getSQLByAddResArea(1, deviceId, Integer.parseInt(list.get(i))));
//			}
//			list = null;
//		}
//		
//		//增加现场安装记录
//		sbSQL.append(";");
//		/**
//		 * modify by qixueqi
//		 * date 2010-5-20
//		 */
//		//sbSQL.append(dao.getSQLByAddUserinst("1", deviceId, username, dealstaff, userFlag));
//		sbSQL.append(dao.getSQLByAddBindlog(username,deviceId,0,99,null,1,null,1,1,dealstaff));
//		
//		try{
//			@SuppressWarnings("unused")
//			int[] rs = dao.batchUpdate(sbSQL.toString().split(";"));
//			// 通知后台设备状态变更
//			int retMsg = 0;
//			MCControlManager mc = new MCControlManager(dealstaff, password);
//			String[] device_array = { deviceId };
//			retMsg = mc.reloadDeviceAraeInfo(device_array);
//			if (retMsg != 0) {
//				logger.warn("通知后台CORBA失败");
//			} else {
//				logger.warn("通知后台CORBA成功");
//			}
//			rsMessage = "手工绑定成功！";
//			this.rsFlag = true;
//		}catch(Exception e){
//			rsMessage = "手工绑定失败！";
//			logger.error("用户账号:{}对应设备:{}手工绑定失败", username,deviceNo);
//			this.rsFlag = false;
//		}
//		
//		/**
//		 * strategyConfigBio.bbmsBindStrategyConfigRun(accOid, username,deviceId, gw_type);
//		 * 主要是给云南使用，暂时屏蔽，以后需要的时候打开
//		 */
//		strategyConfigBio.bbmsBindStrategyConfigRun(accOid, username,deviceId, gw_type);
//		
//		return rsMessage;
	}

	/**
	 * BBMS解绑
	 * 
	 * @param userId
	 * @param deviceId
	 * @return
	 */
	public String bbmsRelease(String userId, String username, String cityId,String deviceId, String dealstaff, String password) {
		logger.debug("bbmsRelease(userId:{},cityId:{};deviceId:{})",
				new Object[]{userId,cityId,deviceId});
		
		String msg = "";
		ResourceBindInterface corba = CreateObjectFactory.createResourceBind(LipossGlobals.getGw_Type(deviceId));
		
		UnBindInfo[] arr = new UnBindInfo[1];
		arr[0] = new UnBindInfo();
		arr[0].accOid = userId;
		arr[0].accName = dealstaff;
		arr[0].userId = userId;
		arr[0].deviceId = deviceId;
		arr[0].userline = 1;
		
		ResultInfo rs = corba.release(arr);
		if(rs == null)
		{
			msg = "解绑失败，系统内部错误";
		}
		else
		{
			//String status = rs.status;
			// 成功
			if(rs.resultId[0].equals("1"))
			{
				msg = "解绑" + Global.G_ResourceBind_statusCode.get(Integer.parseInt(rs.resultId[0]));
				return msg;
			}
			// 失败
			else
			{
				// 获取相关错误码
				msg = "解绑失败，" + Global.G_ResourceBind_resultCode.get(Integer.parseInt(rs.resultId[0]));
			}
		}
		
		return msg;
		
//		//返回结果
//		String rsMessage = "";
//		
//		StringBuffer sbSQL = new StringBuffer();
//		long userIdTemp = Long.parseLong(userId);
//		
//		sbSQL.append(dao.getSQLByReleaseUpdEgwUser(userIdTemp));
//		
//		/**
//		 * 针对新疆的BBMS不更新属地，同时也不删除权限域表
//		 * 对于非新疆的情况，属地属地更新到省中心
//		 * 
//		 */
//		if(LipossGlobals.isXJDX()){
//			sbSQL.append(";");
//			sbSQL.append(dao.getSQLByReleaseUpdDevice(deviceId, cityId, false));
//		}else{
//			sbSQL.append(";");
//			sbSQL.append(dao.getSQLByReleaseUpdDevice(deviceId, "00", true));
//			sbSQL.append(dao.getSQLByDelResArea(deviceId));
//		}
//		
//		//增加现场解绑记录
//		sbSQL.append(";");
//		//sbSQL.append(dao.getSQLByAddUserinst("1", deviceId, username, dealstaff, 3));
//		sbSQL.append(dao.getSQLByAddBindlog(username,deviceId,0,99,null,1,null,2,1,dealstaff));
//		
//		try{
//			@SuppressWarnings("unused")
//			int[] rs = dao.batchUpdate(sbSQL.toString().split(";"));
//			// 通知后台设备状态变更
//			int retMsg = 0;
//			MCControlManager mc = new MCControlManager(dealstaff, password);
//			String[] device_array = { deviceId };
//			retMsg = mc.reloadDeviceAraeInfo(device_array);
//			if (retMsg != 0) {
//				logger.warn("通知后台CORBA失败");
//			} else {
//				logger.warn("通知后台CORBA成功");
//			}
//			rsMessage = "手工解绑成功！";
//			this.rsFlag = true;
//		}catch(Exception e){
//			rsMessage = "手工解绑失败！";
//			logger.error("设备ID:{}手工解绑失败",deviceId);
//			this.rsFlag = false;
//		}
//		
//		return rsMessage;
	}

	/**
	 * ITMS手工安装
	 * 
	 * @param accOid 登陆人ID
	 * @param userId 用户ID
	 * @param username 用户账号
	 * @param userCityId 用户属地
	 * @param deviceId 设备ID
	 * @param deviceCityId 设备属地
	 * @param oui 设备oui
	 * @param deviceNo 设备序列号
	 * @param dealstaff 操作人
	 * @param userFlag 1:新装 3:修障
	 * @return 操作信息提示
	 */
	public String itmsInst(long accOid,String userId, String username, String userCityId,
			String deviceId, String deviceCityId, String oui, String deviceNo,
			String dealstaff,int userFlag, int userline, String gw_type) {
		logger.debug("UserInstReleaseBIO=>itmsInst(userId:{},username:{},userCityId:{},deviceId:{},deviceCityId:{},oui:{},deviceNo:{},dealstaff:{},userFlag:{})",
				new Object[] {userId,username,userCityId,deviceId,deviceCityId,oui,deviceNo,dealstaff,userFlag});
		
		String msg = "";
		ResourceBindInterface corba = CreateObjectFactory.createResourceBind(LipossGlobals.getGw_Type(deviceId));
		
		BindInfo[] arr = new BindInfo[1];
		arr[0] = new BindInfo();
		arr[0].accOid = userId;
		arr[0].accName = dealstaff;
		arr[0].username = username;
		arr[0].deviceId = deviceId;
		arr[0].userline = userline;
		
		ResultInfo rs = corba.bind(arr);
		if(rs == null)
		{
			msg = "绑定失败，系统内部错误";
		}
		else
		{
			//String status = rs.status;
			// 成功
			if(rs.resultId[0].equals("1"))
			{
				msg = "绑定" + Global.G_ResourceBind_statusCode.get(Integer.parseInt(rs.resultId[0]));
				return msg;
			}
			// 失败
			else
			{
				// 获取相关错误码
				msg = "绑定失败，" + Global.G_ResourceBind_resultCode.get(Integer.parseInt(rs.resultId[0]));
			}
		}
		
		return msg;
	}

	/**
	 * ITMS解绑
	 * 
	 * @param userId
	 * @param deviceId
	 * @param dealstaff 
	 * @param string 
	 * @return 操作信息提示
	 */
	public String itmsRelease(String userId, String username, String cityId,String deviceId, String dealstaff,int userline) {
		logger.debug("itmsRelease(userId:{};username{};cityId:{};deviceId:{};dealstaff:{},userline:{})",
				new Object[]{userId,username,cityId,deviceId,dealstaff,userline});
		
		String msg = "";
		ResourceBindInterface corba = CreateObjectFactory.createResourceBind(LipossGlobals.getGw_Type(deviceId));
		
		UnBindInfo[] arr = new UnBindInfo[1];
		arr[0] = new UnBindInfo();
		arr[0].accOid = userId;
		arr[0].accName = dealstaff;
		arr[0].userId = userId;
		arr[0].deviceId = deviceId;
		arr[0].userline = userline;
		
		ResultInfo rs = corba.release(arr);
		if(rs == null)
		{
			msg = "解绑失败，系统内部错误";
		}
		else
		{
			//String status = rs.status;
			// 成功
			if(rs.resultId[0].equals("1"))
			{
				msg = "解绑" + Global.G_ResourceBind_statusCode.get(Integer.parseInt(rs.resultId[0]));
				return msg;
			}
			// 失败
			else
			{
				// 获取相关错误码
				msg = "解绑失败，" + Global.G_ResourceBind_resultCode.get(Integer.parseInt(rs.resultId[0]));
			}
		}
		
		return msg;
	}
	
	/**
	 * 调用资源绑定模块删除用户
	 * 
	 * @param userName 用户帐号
	 * @return 执行结果 itms
	 */
	public String itmsDelUser(String userName)
	{
		logger.debug("itmsDelUser(username{})",
				new Object[]{userName});
		
		String msg = "";
		ResourceBindInterface corba = CreateObjectFactory.createResourceBind(Global.GW_TYPE_ITMS);
		
		String status = corba.delUser(userName);
		if(status == null)
		{
			msg = "删除用户失败，系统内部错误";
		}
		else
		{
			// 成功
			if(status.equals("1"))
			{
				msg = "删除用户" + Global.G_ResourceBind_statusCode.get(Integer.parseInt(status));
				return msg;
			}
		}
		
		return msg;
	}
	
	/**
	 * 更新内存中的用户信息
	 * 
	 * @param userName 用户帐号itms
	 * @return 更新结果
	 */
	public String itmsUpdateUser(String userName)
	{
		logger.debug("itmsUpdateUser(username{})",
				new Object[]{userName});
		
		String msg = "";
		ResourceBindInterface corba = CreateObjectFactory.createResourceBind(Global.GW_TYPE_ITMS);
		
		String status = corba.updateUser(userName);
		if(status == null)
		{
			msg = "更新失败，系统内部错误";
		}
		else
		{
			// 成功
			if(status.equals("1"))
			{
				msg = "更新" + Global.G_ResourceBind_statusCode.get(Integer.parseInt(status));
				return msg;
			}
		}
		
		return msg;
	}
	
	public String itmsUpdateDevice(String device_id,String gw_type)
	{
		logger.debug("itmsUpdateDevice(username{})",
				new Object[]{device_id});
		
		String msg = "";
		ResourceBindInterface corba = CreateObjectFactory.createResourceBind(gw_type);
		
		String status = corba.updateDevice(device_id);
		if(status == null)
		{
			msg = "更新失败，系统内部错误";
		}
		else
		{
			// 成功
			if(status.equals("1"))
			{
				msg = "更新" + Global.G_ResourceBind_statusCode.get(Integer.parseInt(status));
				return msg;
			}
		}
		
		return msg;
	}
	
	/**
	 * 通知资源绑定模块更新用户表和设备表
	 * @return
	 */
	public String itmsUpdateUserAndDevice(String userName,String device_id)
	{
		String resUserMsg = itmsUpdateUser(userName);
		String resDeviceMsg = itmsUpdateUser(device_id);
		//都成功则回复成功，否则回复失败
		if(!"更新失败，系统内部错误".equals(resUserMsg) && resUserMsg.equals(resDeviceMsg))
		{
			return resUserMsg;
		}else
		{
			return "更新失败，系统内部错误";
		}
	}
	
	/**
	 * 删除设备
	 * 
	 * @param device_id
	 * @return 删除结果
	 */
	public String itmsDelDevice(String device_id,String gw_type)
	{
		logger.warn("itmsDelDevice(device_id{})",
				new Object[]{device_id});
		
		String msg = "";
		ResourceBindInterface corba = CreateObjectFactory.createResourceBind(gw_type);
		
		String status = corba.delDevice(device_id);
		if(status == null)
		{
			msg = "删除设备失败，系统内部错误";
		}
		else
		{
			// 成功
			if(status.equals("1"))
			{
				msg = "删除设备" + Global.G_ResourceBind_statusCode.get(Integer.parseInt(status));
				return msg;
			}
		}
		
		return msg;
	}
	
	/**
	 * IPOSS现场修障
	 * 
	 * @param userId 修障用户ID
	 * @param username 修障账号
	 * @param userCityId 修障用户属地
	 * @param oldDeviceId 修障前设备ID
	 * @param deviceId	修障待绑定设备ID
	 * @param deviceCityId 修障待绑定设备属地
	 * @param oui 修障待绑定设备OUI
	 * @param deviceNo 修障待绑定设备序列号
	 * @param faultId 修障原因
	 * @param dealStaff 
	 * @param dealStaffid
	 * @return
	 */
	public String ipossItmsModify(String userId, String username,
			String userCityId, String oldDeviceId, String deviceId,
			String deviceCityId, String oui, String deviceNo, String faultId,
			String dealStaff, String dealStaffid) {
		logger.debug("ipossItmsModify()");
		String msg = this.itmsRelease(userId, username, userCityId, oldDeviceId, dealStaff,1);
		logger.info(msg);
		if(!this.rsFlag){
			logger.error("ipossItmsModify=>账号{}修障时解绑失败！",username);
			return "修障失败";
		}
		dao.update(dao.getSaveFault(username, deviceId, faultId, dealStaff, dealStaffid));
		this.itmsInst(1,userId, username, userCityId, deviceId, deviceCityId,
				oui, deviceNo, dealStaff, 3 , 0, LipossGlobals.getGw_Type(deviceId));
		if(!this.rsFlag){
			logger.error("ipossItmsModify=>账号{}修障时重安装失败！",username);
			return "修障失败";
		}
		return "修障成功";
	}
	
	/**
	 * 新疆ITMS的强制绑定
	 * 
	 * @param userId 用户ID
	 * @param username 账号
	 * @param userCityId 用户属地
	 * @param oldDeviceId 前设备ID
	 * @param deviceId	待绑定设备ID
	 * @param deviceCityId 待绑定设备属地
	 * @param oui 待绑定设备OUI
	 * @param deviceNo 待绑定设备序列号
	 * @param dealStaff 
	 * @param dealStaffid
	 * @return
	 */
	public String itmsImposeInst(long accOid, String userId, String username,
			String userCityId, String oldDeviceId, String deviceId,
			String deviceCityId, String oui, String deviceNo, String dealStaff) {
		logger.debug("imposeInst()");
		String msg = this.itmsRelease(userId, username, userCityId, oldDeviceId, dealStaff,0);
		logger.info(msg);
		if(!this.rsFlag){
			logger.error("imposeInst=>账号{}强制绑定时解绑失败！",username);
			return "手工绑定失败";
		}
		this.itmsInst(accOid, userId, username, userCityId, deviceId, deviceCityId,
				oui, deviceNo, dealStaff, 1 , 1, LipossGlobals.getGw_Type(deviceId));
		if(!this.rsFlag){
			logger.error("imposeInst=>账号{}强制绑定时重安装失败！",username);
			return "手工绑定失败";
		}
		return "手工绑定成功";
	}
	
	/**
	 * 新疆BBMS的强制绑定
	 * 
	 * @param userId 用户ID
	 * @param username 账号
	 * @param userCityId 用户属地
	 * @param oldDeviceId 前设备ID
	 * @param deviceId	待绑定设备ID
	 * @param deviceCityId 待绑定设备属地
	 * @param oui 待绑定设备OUI
	 * @param deviceNo 待绑定设备序列号
	 * @param dealStaff 
	 * @param dealStaffid
	 * @return
	 */
	public String bbmsImposeInst(long accOid, String userId, String username,
			String userCityId, String oldDeviceId, String deviceId,
			String deviceCityId, String oui, String deviceNo, String customerId, String dealStaff, String password) {
		logger.debug("imposeInst()");
		this.bbmsRelease(userId, username, userCityId, deviceId, dealStaff, password);
		if(!this.rsFlag){
			logger.error("imposeInst=>账号{}强制绑定时解绑失败！",username);
			return "手工绑定失败";
		}
		this.bbmsInst(accOid, userId, username, userCityId, deviceId, deviceCityId, oui, deviceNo, customerId, dealStaff, 1, password);
		if(!this.rsFlag){
			logger.error("imposeInst=>账号{}强制绑定时重安装失败！",username);
			return "手工绑定失败";
		}
		return "手工绑定成功";
	}

	/**
	 * @return the strategyConfigBio
	 */
	public StrategyConfigBio getStrategyConfigBio() {
		return strategyConfigBio;
	}

	/**
	 * @param strategyConfigBio the strategyConfigBio to set
	 */
	public void setStrategyConfigBio(StrategyConfigBio strategyConfigBio) {
		this.strategyConfigBio = strategyConfigBio;
	}
	
	
}
