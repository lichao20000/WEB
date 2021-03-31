
package com.linkage.module.itms.midware.bio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.corba.MidWareCorba;
import com.linkage.litms.midware.RETcode;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.corba.ACSCorba;
import com.linkage.module.gwms.util.corba.DevReboot;
import com.linkage.module.itms.midware.dao.BusinessOpenDAO;
import com.linkage.module.itms.midware.dao.MidWareDAO;


@SuppressWarnings("unchecked")
public class BusinessOpenBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BusinessOpenBIO.class);

	private BusinessOpenDAO businessOpenDao;

	private MidWareBIO midWareBIO = null;
	private MidWareDAO midWareDAO = null;
	
	/**
	 * 开启中间件终端模块
	 * 
	 * @param String
	 * 
	 * @return String 
	 */
	public String midMareDevOpen(String deviceId){
		logger.debug("midMareDevOpenCore({})",deviceId);
		String msg = "";
		int flag = 0;
		String rs = isMidMareDevOpen(deviceId);
		if("failure".equals(rs)){
			msg = "设备操作失败！";
		}else if("true".equals(rs)){
			msg = "设备已经开启中间件！";
		}else{
			flag = midMareDevOpenCore(deviceId);
			logger.debug("isMidMareDevOpen=>flag1111:{})",flag);
			if(0==flag){
				flag = DevReboot.reboot(deviceId, LipossGlobals.getGw_Type(deviceId));
				logger.debug("isMidMareDevOpen=>flag2222:{})",flag);
				if(1==flag){
					msg = "设备成功开启中间件，正在重启中！";
				}else{
					msg = Global.G_Fault_Map.get(flag).getFaultReason();
				}
			}else{
				msg = Global.G_Fault_Map.get(flag).getFaultReason();
			}
		}
		return msg;
	}
	
	/**
	 * 判断该设备是否开启中间件
	 * 
	 * @param deviceId 设备序列号
	 * 
	 * @return “true” 开启；“false” 关闭；“failure”获取失败 
	 */
	public String isMidMareDevOpen(String deviceId){
		logger.debug("isMidMareDevOpen({})",deviceId);
		String flag = "false";
		ACSCorba acsCorba = new ACSCorba(LipossGlobals.getGw_Type(deviceId));
		String name = "InternetGatewayDevice.DeviceInfo.X_CT-COM_MiddlewareMgt.Tr069Enable";
		ArrayList<ParameValueOBJ> objList = acsCorba.getValue(deviceId,name);
		if(null!=objList){
			ParameValueOBJ obj = objList.get(0);
			String value = obj.getValue();
			if("0".equals(value) || "2".equals(value)){
				flag = "true";
			}else{
				flag = "false";
			}
		}else{
			flag = "failure";
		}
		logger.debug("{}:Tr069Enable:{}",deviceId,flag);
		return flag;
	}
	
	/**
	 * 开启中间件终端模块
	 * 
	 * @param String
	 * 
	 * @return String 
	 */
	public int midMareDevOpenCore(String deviceId){
		logger.debug("midMareDevOpenCore({})",deviceId);
		int msg = 0;
		ACSCorba acsCorba = new ACSCorba(LipossGlobals.getGw_Type(deviceId));
		String host = LipossGlobals.getLipossProperty("midware.host");
		String name1 = "InternetGatewayDevice.DeviceInfo.X_CT-COM_MiddlewareMgt.Tr069Enable";
		String name2 = "InternetGatewayDevice.DeviceInfo.X_CT-COM_MiddlewareMgt.MiddlewareURL";
		ArrayList<ParameValueOBJ> objList = new ArrayList<ParameValueOBJ>();
		ParameValueOBJ obj1 = new ParameValueOBJ();
		ParameValueOBJ obj2 = new ParameValueOBJ();
		obj1.setName(name1);
		obj1.setValue("2");
		objList.add(obj1);
		obj2.setName(name2);
		obj2.setValue(host);
		objList.add(obj2);
		msg = acsCorba.setValue(deviceId,objList);
		return msg;
	}
	
	/**
	 * 调用中间件接口
	 * 
	 * @param bType 操作类型
	 * @param deviceId 设备ID
	 * @param oui 设备OUI
	 * @param deviceSn 设备序列号
	 * @param serviceCode 业务代码
	 * @param account 业务账号
	 * @param password 业务密码
	 * @param username 用户账号'
	 * 
	 * @return
	 */
	public String getMidMsg(String bType,String deviceId,String oui,String deviceSn,
			String serviceCode,String account,String password,String username,
			String deviceModel,String cityId) {
		String _msg = "";
		int flag = 0;
		String rs = isMidMareDevOpen(deviceId);
		if("failure".equals(rs)){
			_msg = "设备操作失败！";
			return _msg.toString();
		}else if("true".equals(rs)){
			_msg = "设备已经开启中间件！";
		}else{
			flag = midMareDevOpenCore(deviceId);
			logger.debug("isMidMareDevOpen=>flag1111:{})",flag);
			if(0==flag){
				flag = DevReboot.reboot(deviceId, LipossGlobals.getGw_Type(deviceId));
				logger.debug("isMidMareDevOpen=>flag2222:{})",flag);
				if(1==flag){
					_msg = "设备成功开启中间件，正在重启中！";
				}else{
					_msg = Global.G_Fault_Map.get(flag).getFaultReason();
					return _msg.toString();
				}
			}else{
				_msg = Global.G_Fault_Map.get(flag).getFaultReason();
				return _msg;
			}
		}
		
		List list_ = midWareDAO.getMidWareDevice(deviceId);
		if(list_.size()<1){
			int temp_ = midWareBIO.add(deviceId, oui, deviceSn, deviceModel, username,"1", "", "", "");
			if(0==temp_){
				temp_ = midWareBIO.addDeviceCity(deviceId, oui, deviceSn, cityId);
				if(0==temp_){
					midWareDAO.insertMidWareDevice(deviceId, cityId, oui, deviceSn, deviceModel, "", "", "", username, "");
				}else{
					_msg = "设备向中间件平台添加域失败！";
					return _msg;
				}
			}else{
				_msg = "设备添加到中间件平台失败！";
				return _msg;
			}
		}
		
		StringBuffer msg = new StringBuffer();
		MidWare.DeviceObjectRep[] rsRep = null;
		if (bType.equals("B1")) { // 开通
			rsRep = serviceOpen(deviceId, oui, deviceSn, serviceCode, account, password, username);
		} else if (bType.equals("B2")) {// 暂停
			rsRep = serviceStop(deviceId, oui, deviceSn, serviceCode);
		} else if (bType.equals("B3")) {// 恢复
			rsRep = serviceRecover(deviceId, oui, deviceSn, serviceCode);
		} else if (bType.equals("B4")) {// 参数修改
			rsRep = modifyServiceParam(deviceId, oui, deviceSn, serviceCode, account, password, username);
		} else if (bType.equals("B5")) {// 关闭
			rsRep = serviceCancel(deviceId, oui, deviceSn, serviceCode);
		} else if (bType.equals("B6")) {// 升级
			rsRep = upgradeService(deviceId, oui, deviceSn, serviceCode);
		}
		if (rsRep != null && rsRep.length > 0) {
			msg.append("接口调用成功：");
			int resultCode = rsRep[0].result_code;
			msg.append(RETcode.getDescription(resultCode));
			
			//根据serv_type_id、username查询gw_user_midware_serv，是否存在记录
			//存在则更新
			//不存在则删除
			int num = businessOpenDao.getServUserCount(account,serviceCode);
			int ret = 0;
			Date date = new Date();
			long nowtime = date.getTime();

			if (num > 0) { // update
				ret = businessOpenDao.updateServ(resultCode,nowtime,account,serviceCode);
			} else { // insert
				ret = businessOpenDao.insertServ(resultCode,nowtime,account,serviceCode);
			}

			if (ret!=0) {
				msg.append(",返回结果入库成功！");
			} else {
				msg.append(",返回结果入库失败！");
			}

		} else {
			msg.append("接口调用失败!");
		}
		return msg.toString();
	}
	
	/**
	 * 业务开通
	 * 
	 * @param deviceId 设备ID
	 * @param oui 设备OUI
	 * @param deviceSn 设备序列号
	 * @param serviceCode 业务代码
	 * @param account 业务账号
	 * @param password 业务密码
	 * @param username 用户账号
	 * 
	 * @return
	 */

	public MidWare.DeviceObjectRep[] serviceOpen(String deviceId,String oui,String deviceSn,
			String serviceCode,String account,String password,String username) {

		MidWare.ServiceBussinessObject[] serBussinessObjectArr = 
			getServiceBussinessOpenObject(deviceId, oui, deviceSn, serviceCode, account, password, username);
		MidWareCorba midCorba = new MidWareCorba();
		MidWare.DeviceObjectRep[] deviceObjectRepArr = midCorba.openService(serBussinessObjectArr);

		return deviceObjectRepArr;
	}
	
	/**
	 * 业务暂停
	 * 
	 * @param deviceId 设备ID
	 * @param oui 设备OUI
	 * @param deviceSn 设备序列号
	 * @param serviceCode 业务代码
	 * 
	 * @return
	 */
	public MidWare.DeviceObjectRep[] serviceStop(String deviceId,String oui,
			String deviceSn,String serviceCode) {

		MidWare.DeviceObject devObj = new MidWare.DeviceObject();
		devObj.device_id = deviceId;
		devObj.oui = oui;
		devObj.device_serialnumber = deviceSn;

		MidWare.ServiceObject serObj = new MidWare.ServiceObject();
		serObj.service_code = serviceCode;
		if ("1".equals(serviceCode)){
			serObj.service_name = "MediaFtp";
		} else if("8".equals(serviceCode)){
			serObj.service_name = "UPLOAD";
		} else if("6".equals(serviceCode)){
			serObj.service_name = "Rss";
		} else if("21".equals(serviceCode)){
			serObj.service_name = "IMusic";
		} else if("21".equals(serviceCode)){
			serObj.service_name = "IRadio";
		}

		MidWare.ServiceBussiness serBussiness = new MidWare.ServiceBussiness();
		serBussiness.deviceObject = new MidWare.DeviceObject();
		serBussiness.deviceObject = devObj;
		serBussiness.serviceObject = new MidWare.ServiceObject();
		serBussiness.serviceObject = serObj;

		MidWare.ServiceBussiness[] serBussinessArr = new MidWare.ServiceBussiness[1];
		serBussinessArr[0] = new MidWare.ServiceBussiness();
		serBussinessArr[0] = serBussiness;

		MidWareCorba midCorba = new MidWareCorba();
		MidWare.DeviceObjectRep[] deviceObjectRepArr = midCorba
				.stopService(serBussinessArr);
		return deviceObjectRepArr;
	}
	
	/**
	 * 业务恢复
	 * 
	 * @param deviceId 设备ID
	 * @param oui 设备OUI
	 * @param deviceSn 设备序列号
	 * @param serviceCode 业务代码
	 * 
	 * @return
	 */
	public MidWare.DeviceObjectRep[] serviceRecover(String deviceId,String oui,
			String deviceSn,String serviceCode) {

		MidWare.DeviceObject devObj = new MidWare.DeviceObject();
		devObj.device_id = deviceId;
		devObj.oui = oui;
		devObj.device_serialnumber = deviceSn;

		MidWare.ServiceObject serObj = new MidWare.ServiceObject();

		serObj.service_code = serviceCode;
		if ("1".equals(serviceCode)){
			serObj.service_name = "MediaFtp";
		} else if("8".equals(serviceCode)){
			serObj.service_name = "UPLOAD";
		} else if("6".equals(serviceCode)){
			serObj.service_name = "Rss";
		} else if("21".equals(serviceCode)){
			serObj.service_name = "IMusic";
		} else if("22".equals(serviceCode)){
			serObj.service_name = "IRadio";
		}

		MidWare.ServiceBussiness serBussiness = new MidWare.ServiceBussiness();
		serBussiness.deviceObject = new MidWare.DeviceObject();
		serBussiness.deviceObject = devObj;
		serBussiness.serviceObject = new MidWare.ServiceObject();
		serBussiness.serviceObject = serObj;

		MidWare.ServiceBussiness[] serBussinessArr = new MidWare.ServiceBussiness[1];
		serBussinessArr[0] = new MidWare.ServiceBussiness();
		serBussinessArr[0] = serBussiness;

		MidWareCorba midCorba = new MidWareCorba();
		MidWare.DeviceObjectRep[] deviceObjectRepArr = midCorba
				.recoverService(serBussinessArr);

		return deviceObjectRepArr;
	}
	
	/**
	 * 修改业务参数
	 * 
	 * @param deviceId 设备ID
	 * @param oui 设备OUI
	 * @param deviceSn 设备序列号
	 * @param serviceCode 业务代码
	 * @param account 业务账号
	 * @param password 业务密码
	 * @param username 用户账号
	 * 
	 * @return
	 */
	public MidWare.DeviceObjectRep[] modifyServiceParam(String deviceId,
			String oui,String deviceSn,String serviceCode,String account,
			String password,String username) {

		MidWare.ServiceBussinessObject[] serBussinessObjectArr = 
			getServiceBussinessModifyObject(deviceId, oui, deviceSn, serviceCode, 
					account, password, username);
		MidWareCorba midCorba = new MidWareCorba();
		MidWare.DeviceObjectRep[] deviceObjectRepArr = midCorba
				.modifyServiceParam(serBussinessObjectArr);
		return deviceObjectRepArr;
	}
	
	/**
	 * 业务关闭
	 * 
	 * @param deviceId 设备ID
	 * @param oui 设备OUI
	 * @param deviceSn 设备序列号
	 * @param serviceCode 业务代码
	 * 
	 * @return
	 */

	public MidWare.DeviceObjectRep[] serviceCancel(String deviceId,
			String oui,String deviceSn,String serviceCode) {

		MidWare.DeviceObject devObj = new MidWare.DeviceObject();
		devObj.device_id = deviceId;
		devObj.oui = oui;
		devObj.device_serialnumber = deviceSn;

		MidWare.ServiceObject serObj = new MidWare.ServiceObject();
		serObj.service_code = serviceCode;
		if ("1".equals(serviceCode)){
			serObj.service_name = "MediaFtp";
		} else if("8".equals(serviceCode)){
			serObj.service_name = "UPLOAD";
		} else if("6".equals(serviceCode)){
			serObj.service_name = "Rss";
		} else if("21".equals(serviceCode)){
			serObj.service_name = "IMusic";
		} else if("22".equals(serviceCode)){
			serObj.service_name = "IRadio";
		}

		MidWare.ServiceBussiness serBussiness = new MidWare.ServiceBussiness();
		serBussiness.deviceObject = new MidWare.DeviceObject();
		serBussiness.deviceObject = devObj;
		serBussiness.serviceObject = new MidWare.ServiceObject();
		serBussiness.serviceObject = serObj;

		MidWare.ServiceBussiness[] serBussinessArr = new MidWare.ServiceBussiness[1];
		serBussinessArr[0] = new MidWare.ServiceBussiness();
		serBussinessArr[0] = serBussiness;

		MidWareCorba midCorba = new MidWareCorba();
		MidWare.DeviceObjectRep[] deviceObjectRepArr = midCorba
				.cancelService(serBussinessArr);
		return deviceObjectRepArr;
	}
	
	/**
	 * 业务模块升级
	 * 
	 * @param deviceId 设备ID
	 * @param oui 设备OUI
	 * @param deviceSn 设备序列号
	 * @param serviceCode 业务代码
	 * 
	 * @return
	 */
	public MidWare.DeviceObjectRep[] upgradeService(String deviceId,
			String oui,String deviceSn,String serviceCode) {

		MidWare.ServiceUpgradeObject[] serUpgradeObjectArr = new MidWare.ServiceUpgradeObject[1];
		serUpgradeObjectArr[0] = new MidWare.ServiceUpgradeObject();
		serUpgradeObjectArr[0].deviceObject = new MidWare.DeviceObject();
		serUpgradeObjectArr[0].deviceObject.device_id = deviceId;
		serUpgradeObjectArr[0].deviceObject.oui = oui;
		serUpgradeObjectArr[0].deviceObject.device_serialnumber = deviceSn;
		
		
		serUpgradeObjectArr[0].serviceObject = new MidWare.ServiceObject();
		serUpgradeObjectArr[0].serviceObject.service_code = serviceCode;
		if ("1".equals(serviceCode)){
			serUpgradeObjectArr[0].serviceObject.service_name = "MediaFtp";
		} else if("8".equals(serviceCode)){
			serUpgradeObjectArr[0].serviceObject.service_name = "UPLOAD";
		} else if("6".equals(serviceCode)){
			serUpgradeObjectArr[0].serviceObject.service_name = "Rss";
		} else if("21".equals(serviceCode)){
			serUpgradeObjectArr[0].serviceObject.service_name = "IMusic";
		} else if("22".equals(serviceCode)){
			serUpgradeObjectArr[0].serviceObject.service_name = "IRadio";
		}
		serUpgradeObjectArr[0].paramArr = new MidWare.Param[0];
		
		MidWareCorba midCorba = new MidWareCorba();
		MidWare.DeviceObjectRep[] deviceObjectRepArr = midCorba
				.upgradeService(serUpgradeObjectArr);
		return deviceObjectRepArr;

	}
	
	/**
	 * 业务开通参数组装
	 * 
	 * @param deviceId 设备ID
	 * @param oui 设备OUI
	 * @param deviceSn 设备序列号
	 * @param serviceCode 业务代码
	 * @param account 业务账号
	 * @param password 业务密码
	 * @param username 用户账号
	 * 
	 * @return
	 */
	public MidWare.ServiceBussinessObject[] getServiceBussinessOpenObject(String deviceId,
			String oui,String deviceSn,String serviceCode,String account,
			String password,String username) {

		MidWare.DeviceObject devObj = new MidWare.DeviceObject();
		devObj.device_id = "1";
		devObj.oui = oui;
		devObj.device_serialnumber = deviceSn;

		MidWare.ServiceObject serObj = new MidWare.ServiceObject();

		serObj.service_code = serviceCode;
		if ("1".equals(serviceCode)){
			serObj.service_name = "MediaFtp";
		} else if("8".equals(serviceCode)){
			serObj.service_name = "UPLOAD";
		} else if("6".equals(serviceCode)){
			serObj.service_name = "Rss";
		} else if("21".equals(serviceCode)){
			serObj.service_name = "IMusic";
		} else if("22".equals(serviceCode)){
			serObj.service_name = "IRadio";
		}

		MidWare.ServiceBussiness serBussiness = new MidWare.ServiceBussiness();
		serBussiness.deviceObject = new MidWare.DeviceObject();
		serBussiness.deviceObject = devObj;
		serBussiness.serviceObject = new MidWare.ServiceObject();
		serBussiness.serviceObject = serObj;

		MidWare.ServiceAccount serAccount = new MidWare.ServiceAccount();
		serAccount.username = account;
		serAccount.password = password;

		MidWare.Param[] paramArr = null;
		if ("1".equals(serviceCode)){
			paramArr = new MidWare.Param[1];
			MidWare.Param param = new MidWare.Param();
			param.param_name = "AD";
			param.param_type = "String";
			param.param_value = username;
			paramArr[0] = new MidWare.Param();
			paramArr[0] = param;
		} else if("8".equals(serviceCode)){
			paramArr = new MidWare.Param[1];
			MidWare.Param param = new MidWare.Param();
			param.param_name = "USERNAME";
			param.param_type = "String";
			param.param_value = username;
			paramArr[0] = new MidWare.Param();
			paramArr[0] = param;
		} else if("6".equals(serviceCode)){
			paramArr = new MidWare.Param[2];
			MidWare.Param param = new MidWare.Param();
			param.param_name = "USERNAME";
			param.param_type = "String";
			param.param_value = username;
			paramArr[0] = new MidWare.Param();
			paramArr[0] = param;
			
			param = new MidWare.Param();
			param.param_name = "PASSWORD";
			param.param_type = "String";
			param.param_value = password;
			paramArr[1] = new MidWare.Param();
			paramArr[1] = param;
		} else if("21".equals(serviceCode)){
			paramArr = new MidWare.Param[1];
			MidWare.Param param = new MidWare.Param();
			param.param_name = "AD";
			param.param_type = "String";
			param.param_value = username;
			paramArr[0] = new MidWare.Param();
			paramArr[0] = param;
		} else if("22".equals(serviceCode)){
			paramArr = new MidWare.Param[1];
			MidWare.Param param = new MidWare.Param();
			param.param_name = "AD";
			param.param_type = "String";
			param.param_value = username;
			paramArr[0] = new MidWare.Param();
			paramArr[0] = param;
		}
		
		MidWare.ServiceBussinessObject serBussinessObj = new MidWare.ServiceBussinessObject();
		serBussinessObj.serviceBussiness = new MidWare.ServiceBussiness();
		serBussinessObj.serviceBussiness = serBussiness;
		serBussinessObj.serviceAccount = new MidWare.ServiceAccount();
		serBussinessObj.serviceAccount = serAccount;
		serBussinessObj.paramArr = new MidWare.Param[3];
		serBussinessObj.paramArr = paramArr;
		MidWare.ServiceBussinessObject[] serBussinessObjectArr = new MidWare.ServiceBussinessObject[1];
		serBussinessObjectArr[0] = new MidWare.ServiceBussinessObject();
		serBussinessObjectArr[0] = serBussinessObj;
		return serBussinessObjectArr;
	}
	
	/**
	 * 修改业务参数组装
	 * 
	 * @param deviceId 设备ID
	 * @param oui 设备OUI
	 * @param deviceSn 设备序列号
	 * @param serviceCode 业务代码
	 * @param account 业务账号
	 * @param password 业务密码
	 * @param username 用户账号
	 * 
	 * @return
	 */
	public MidWare.ServiceBussinessObject[] getServiceBussinessModifyObject(String deviceId,
			String oui,String deviceSn,String serviceCode,String account,
			String password,String username) {

		MidWare.DeviceObject devObj = new MidWare.DeviceObject();
		devObj.device_id = "1";
		devObj.oui = oui;
		devObj.device_serialnumber = deviceSn;

		MidWare.ServiceObject serObj = new MidWare.ServiceObject();

		serObj.service_code = serviceCode;
		if ("1".equals(serviceCode)){
			serObj.service_name = "MediaFtp";
		} else if("8".equals(serviceCode)){
			serObj.service_name = "UPLOAD";
		} else if("6".equals(serviceCode)){
			serObj.service_name = "Rss";
		} else if("21".equals(serviceCode)){
			serObj.service_name = "IMusic";
		} else if("22".equals(serviceCode)){
			serObj.service_name = "IRadio";
		}

		MidWare.ServiceBussiness serBussiness = new MidWare.ServiceBussiness();
		serBussiness.deviceObject = new MidWare.DeviceObject();
		serBussiness.deviceObject = devObj;
		serBussiness.serviceObject = new MidWare.ServiceObject();
		serBussiness.serviceObject = serObj;

		MidWare.ServiceAccount serAccount = new MidWare.ServiceAccount();
		serAccount.username = account;
		serAccount.password = password;

		MidWare.Param[] paramArr = null;
		if ("1".equals(serviceCode)){
			paramArr = new MidWare.Param[1];
			MidWare.Param param = new MidWare.Param();
			param.param_name = "AD";
			param.param_type = "String";
			param.param_value = username;
			paramArr[0] = new MidWare.Param();
			paramArr[0] = param;
		} else if("8".equals(serviceCode)){
			paramArr = new MidWare.Param[1];
			MidWare.Param param = new MidWare.Param();
			param.param_name = "USERNAME";
			param.param_type = "String";
			param.param_value = username;
			paramArr[0] = new MidWare.Param();
			paramArr[0] = param;
		} else if("6".equals(serviceCode)){
			paramArr = new MidWare.Param[2];
			MidWare.Param param = new MidWare.Param();
			param.param_name = "USERNAME";
			param.param_type = "String";
			param.param_value = username;
			paramArr[0] = new MidWare.Param();
			paramArr[0] = param;
			
			param = new MidWare.Param();
			param.param_name = "PASSWORD";
			param.param_type = "String";
			param.param_value = password;
			paramArr[1] = new MidWare.Param();
			paramArr[1] = param;
		} else if("21".equals(serviceCode)){
			paramArr = new MidWare.Param[1];
			MidWare.Param param = new MidWare.Param();
			param.param_name = "AD";
			param.param_type = "String";
			param.param_value = username;
			paramArr[0] = new MidWare.Param();
			paramArr[0] = param;
		} else if("22".equals(serviceCode)){
			paramArr = new MidWare.Param[1];
			MidWare.Param param = new MidWare.Param();
			param.param_name = "AD";
			param.param_type = "String";
			param.param_value = username;
			paramArr[0] = new MidWare.Param();
			paramArr[0] = param;
		}
		
		MidWare.ServiceBussinessObject serBussinessObj = new MidWare.ServiceBussinessObject();
		serBussinessObj.serviceBussiness = new MidWare.ServiceBussiness();
		serBussinessObj.serviceBussiness = serBussiness;
		serBussinessObj.serviceAccount = new MidWare.ServiceAccount();
		serBussinessObj.serviceAccount = serAccount;
		serBussinessObj.paramArr = new MidWare.Param[3];
		serBussinessObj.paramArr = paramArr;
		MidWare.ServiceBussinessObject[] serBussinessObjectArr = new MidWare.ServiceBussinessObject[1];
		serBussinessObjectArr[0] = new MidWare.ServiceBussinessObject();
		serBussinessObjectArr[0] = serBussinessObj;
		return serBussinessObjectArr;
	}

	
	/**
	 * @return the businessOpenDao
	 */
	public BusinessOpenDAO getBusinessOpenDao()
	{
		return businessOpenDao;
	}

	
	/**
	 * @param businessOpenDao the businessOpenDao to set
	 */
	public void setBusinessOpenDao(BusinessOpenDAO businessOpenDao)
	{
		this.businessOpenDao = businessOpenDao;
	}

	/**
	 * @return the midWareBIO
	 */
	public MidWareBIO getMidWareBIO() {
		return midWareBIO;
	}

	/**
	 * @param midWareBIO the midWareBIO to set
	 */
	public void setMidWareBIO(MidWareBIO midWareBIO) {
		this.midWareBIO = midWareBIO;
	}

	/**
	 * @return the midWareDAO
	 */
	public MidWareDAO getMidWareDAO() {
		return midWareDAO;
	}

	/**
	 * @param midWareDAO the midWareDAO to set
	 */
	public void setMidWareDAO(MidWareDAO midWareDAO) {
		this.midWareDAO = midWareDAO;
	}
	
}
