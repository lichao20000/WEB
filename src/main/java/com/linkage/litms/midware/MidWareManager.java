package com.linkage.litms.midware;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.holdfastgroup.interfas.Client;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.corba.MidWareCorba;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;

public class MidWareManager {
	private Client client;
	/** log */
	private static Logger logger = LoggerFactory.getLogger(MidWareManager.class);
	// public static final boolean notify =
	// LipossGlobals.getLipossProperty("midware.notify").equals("true") ? true :
	// false;
	public static final boolean notify = true;

	private InterID interID;

	private String oui;// request.getParameter("oui");

	private String serial_dev;// request.getParameter("serial_dev");

	private String type;// = request.getParameter("type");

	private String b_type;// = request.getParameter("b_type");

	private String b_name;// = request.getParameter("b_name");

	private String account;// = request.getParameter("account");

	private String password;// = request.getParameter("password");

	private String server;// = request.getParameter("server");

	private String username;// = request.getParameter("username");

	private String pass;// = request.getParameter("pass");

	private String domainname;// = request.getParameter("domainname");

	private String server2;// = request.getParameter("server2");

	private HttpServletRequest request;

	public MidWareManager(HttpServletRequest req) {
		this.request = req;
		this.oui = req.getParameter("oui");
		this.serial_dev = req.getParameter("serial_dev");
		this.type = req.getParameter("type");
		this.b_type = req.getParameter("b_type");
		this.b_name = req.getParameter("b_name");
		this.account = req.getParameter("account") == null ? "" : req.getParameter("account");
		this.password = req.getParameter("password") == null ? "" : req.getParameter("password");
		this.server = req.getParameter("server") == null ? "" : req.getParameter("server");
		this.username = req.getParameter("username") == null ? "" : req.getParameter("username");
		this.pass = req.getParameter("pass") == null ? "" : req.getParameter("pass");
		this.domainname = req.getParameter("domainname") == null ? "" : req.getParameter("domainname");
		this.server2 = req.getParameter("server2") == null ? "" : req.getParameter("server2");

	}

	/**
	 * 调用中间件接口
	 * 
	 * @return
	 */
	public String getMidMsg() {

		String msg = "";

		// <option value="B1">业务开通</option>
		// <option value="B2">业务暂停</option>
		// <option value="B3">业务恢复</option>
		// <option value="B4">业务参数修改</option>
		// <option value="B5">业务关闭</option>
		// <option value="B6">业务模块升级</option></select>

		logger.debug("b_type :" + b_type);

		if (b_type.equals("B1")) { // 开通

			msg = serviceOpen();

		} else if (b_type.equals("B2")) {// 暂停

			msg = serviceStop();

		} else if (b_type.equals("B3")) {// 恢复

			msg = serviceRecover();

		} else if (b_type.equals("B4")) {// 参数修改

			msg = modifyServiceParam();

		} else if (b_type.equals("B5")) {// 关闭

			msg = serviceCancel();

		} else if (b_type.equals("B6")) {// 升级

			msg = upgradeService();

		}

		return msg;

	}

	/**
	 * 业务开通
	 * 
	 * @return
	 */

	public String serviceOpen() {

		String msg = "";
		MidWare.ServiceBussinessObject[] serBussinessObjectArr = getServiceBussinessObject();
		MidWareCorba midCorba = new MidWareCorba();
		MidWare.DeviceObjectRep[] deviceObjectRepArr = midCorba
				.openService(serBussinessObjectArr);

		if (deviceObjectRepArr != null && deviceObjectRepArr.length > 0) {

			msg = "接口调用成功:";

			int resultCode = deviceObjectRepArr[0].result_code;
			
			msg += RETcode.getDescription(resultCode);
			
			String sql = " select count(*) as num from gw_user_midware_serv where username='"
					+ account + "' and serv_type_id=" + type;
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			Map fields = DataSetBean.getRecord(sql);

			int num = Integer.parseInt((String) fields.get("num"));

			Date date = new Date();
			long nowtime = date.getTime();

			if (num > 0) { // update
				sql = "update gw_user_midware_serv set oper_type_id=1,stat="
						+ resultCode + ",oper_time=" + nowtime
						+ "  where username='" + account
						+ "' and serv_type_id=" + type;
			} else { // insert
				sql = "insert into gw_user_midware_serv(username,serv_type_id,oper_type_id,stat,oper_time) values('"
						+ account
						+ "',"
						+ type
						+ ",1,"
						+ resultCode
						+ ","
						+ nowtime + ")";
			}

			psql = new PrepareSQL(sql);
	    	psql.getSQL();
			int state = DataSetBean.executeUpdate(sql);

			if (state > 0) {
				msg += ",返回结果入库成功！";
			} else {
				msg += ",返回结果入库失败！";
			}
			
			logger.debug("msg  :" + msg);

		} else {
			msg = "接口调用失败!";
		}
		return msg;

	}

	/**
	 * 业务暂停
	 * 
	 * @return
	 */

	public String serviceStop() {

		String msg = "";

		MidWare.DeviceObject devObj = new MidWare.DeviceObject();
		devObj.device_id = "1";
		devObj.oui = oui;
		devObj.device_serialnumber = serial_dev;

		MidWare.ServiceObject serObj = new MidWare.ServiceObject();

		serObj.service_code = type;
		if (type.equals("1"))
			serObj.service_name = "MediaFtp";
		else
			serObj.service_name = "DDNS";

		MidWare.ServiceBussiness serBussiness = new MidWare.ServiceBussiness();
		serBussiness.deviceObject = new MidWare.DeviceObject();
		serBussiness.deviceObject = devObj;
		serBussiness.serviceObject = new MidWare.ServiceObject();
		serBussiness.serviceObject = serObj;

		MidWare.ServiceBussiness[] serBussinessArr = new MidWare.ServiceBussiness[1];
		serBussinessArr[0] = new MidWare.ServiceBussiness();
		serBussinessArr[0] = serBussiness;

		MidWareCorba midCorba = new MidWareCorba();
		logger.debug("serBussinessArr:" + serBussinessArr);
		MidWare.DeviceObjectRep[] deviceObjectRepArr = midCorba
				.stopService(serBussinessArr);

		logger.debug("deviceObjectRepArr" + deviceObjectRepArr);

		if (deviceObjectRepArr != null && deviceObjectRepArr.length > 0) {

			msg = "接口调用成功：";

			int resultCode = deviceObjectRepArr[0].result_code;
			
			msg += RETcode.getDescription(resultCode);
			
			String sql = " select count(*) as num from gw_user_midware_serv where username='"
					+ account + "' and serv_type_id=" + type;
			PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
			Map fields = DataSetBean.getRecord(sql);

			int num = Integer.parseInt((String) fields.get("num"));

			Date date = new Date();
			long nowtime = date.getTime();

			if (num > 0) { // update
				sql = "update gw_user_midware_serv set oper_type_id=2,stat="
						+ resultCode + ",oper_time=" + nowtime
						+ "  where username='" + account
						+ "' and serv_type_id=" + type;
			} else { // insert
				sql = "insert into gw_user_midware_serv(username,serv_type_id,oper_type_id,stat,oper_time) values('"
						+ account
						+ "',"
						+ type
						+ ",2,"
						+ resultCode
						+ ","
						+ nowtime + ")";
			}

			PrepareSQL psql2 = new PrepareSQL(sql);
	    	psql2.getSQL();
			int state = DataSetBean.executeUpdate(sql);

			if (state > 0) {
				msg += ",返回结果入库成功！";
			} else {
				msg += ",返回结果入库失败！";
			}
			
			logger.debug("msg :" + msg);

		} else {
			msg = "接口调用失败!";
		}
		return msg;

	}

	/**
	 * 业务恢复
	 * 
	 * @return
	 */

	public String serviceRecover() {

		String msg = "";

		MidWare.DeviceObject devObj = new MidWare.DeviceObject();
		devObj.device_id = "1";
		devObj.oui = oui;
		devObj.device_serialnumber = serial_dev;

		MidWare.ServiceObject serObj = new MidWare.ServiceObject();

		serObj.service_code = type;
		if (type.equals("1"))
			serObj.service_name = "MediaFtp";
		else
			serObj.service_name = "DDNS";

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

		if (deviceObjectRepArr != null && deviceObjectRepArr.length > 0) {

			msg = "接口调用成功:";

			int resultCode = deviceObjectRepArr[0].result_code;
			
			msg += RETcode.getDescription(resultCode);
			
			String sql = " select count(*) as num from gw_user_midware_serv where username='"
					+ account + "' and serv_type_id=" + type;
			PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
			Map fields = DataSetBean.getRecord(sql);

			int num = Integer.parseInt((String) fields.get("num"));

			Date date = new Date();
			long nowtime = date.getTime();

			if (num > 0) { // update
				sql = "update gw_user_midware_serv set oper_type_id=3,stat="
						+ resultCode + ",oper_time=" + nowtime
						+ "  where username='" + account
						+ "' and serv_type_id=" + type;
			} else { // insert
				sql = "insert into gw_user_midware_serv(username,serv_type_id,oper_type_id,stat,oper_time) values('"
						+ account
						+ "',"
						+ type
						+ ",3,"
						+ resultCode
						+ ","
						+ nowtime + ")";
			}

			PrepareSQL psql2 = new PrepareSQL(sql);
	    	psql2.getSQL();
			int state = DataSetBean.executeUpdate(sql);

			if (state > 0) {
				msg += ",返回结果入库成功！";
			} else {
				msg += ",返回结果入库失败！";
			}

		} else {
			msg = "接口调用失败!";
		}
		return msg;

	}

	/**
	 * 修改业务参数
	 * 
	 * @return
	 */

	public String modifyServiceParam() {

		String msg = "";
		MidWare.ServiceBussinessObject[] serBussinessObjectArr = getServiceBussinessObject();

		MidWareCorba midCorba = new MidWareCorba();

		MidWare.DeviceObjectRep[] deviceObjectRepArr = midCorba
				.modifyServiceParam(serBussinessObjectArr);

		if (deviceObjectRepArr != null && deviceObjectRepArr.length > 0) {

			msg = "接口调用成功：";

			int resultCode = deviceObjectRepArr[0].result_code;
			
			msg += RETcode.getDescription(resultCode);
			
			
			
			String sql = " select count(*) as num from gw_user_midware_serv where username='"
					+ account + "' and serv_type_id=" + type;
			PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
			Map fields = DataSetBean.getRecord(sql);

			int num = Integer.parseInt((String) fields.get("num"));

			Date date = new Date();
			long nowtime = date.getTime();

			if (num > 0) { // update
				sql = "update gw_user_midware_serv set oper_type_id=4,stat="
						+ resultCode + ",oper_time=" + nowtime
						+ "  where username='" + account
						+ "' and serv_type_id=" + type;
			} else { // insert
				sql = "insert into gw_user_midware_serv(username,serv_type_id,oper_type_id,stat,oper_time) values('"
						+ account
						+ "',"
						+ type
						+ ",4,"
						+ resultCode
						+ ","
						+ nowtime + ")";
			}

			PrepareSQL psql2 = new PrepareSQL(sql);
	    	psql2.getSQL();
			
			int state = DataSetBean.executeUpdate(sql);

			if (state > 0) {
				msg += ",返回结果入库成功！";
			} else {
				msg += ",返回结果入库失败！";
			}

		} else {
			msg = "接口调用失败!";
		}
		return msg;

	}

	/**
	 * 业务关闭
	 * 
	 * @return
	 */

	public String serviceCancel() {

		String msg = "";

		MidWare.DeviceObject devObj = new MidWare.DeviceObject();
		devObj.device_id = "1";
		devObj.oui = oui;
		devObj.device_serialnumber = serial_dev;

		MidWare.ServiceObject serObj = new MidWare.ServiceObject();

		serObj.service_code = type;
		if (type.equals("1"))
			serObj.service_name = "MediaFtp";
		else
			serObj.service_name = "DDNS";

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

		if (deviceObjectRepArr != null && deviceObjectRepArr.length > 0) {

			msg = "接口调用成功：";

			int resultCode = deviceObjectRepArr[0].result_code;
			
			msg += RETcode.getDescription(resultCode);
			
			String sql = " select count(*) as num from gw_user_midware_serv where username='"
					+ account + "' and serv_type_id=" + type;
			PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
			Map fields = DataSetBean.getRecord(sql);

			int num = Integer.parseInt((String) fields.get("num"));

			Date date = new Date();
			long nowtime = date.getTime();

			if (num > 0) { // update
				sql = "update gw_user_midware_serv set oper_type_id=5,stat="
						+ resultCode + ",oper_time=" + nowtime
						+ "  where username='" + account
						+ "' and serv_type_id=" + type;
			} else { // insert
				sql = "insert into gw_user_midware_serv(username,serv_type_id,oper_type_id,stat,oper_time) values('"
						+ account
						+ "',"
						+ type
						+ ",5,"
						+ resultCode
						+ ","
						+ nowtime + ")";
			}

			PrepareSQL psql2 = new PrepareSQL(sql);
	    	psql2.getSQL();

			int state = DataSetBean.executeUpdate(sql);

			if (state > 0) {
				msg += ",返回结果入库成功！";
			} else {
				msg += ",返回结果入库失败！";
			}

		} else {
			msg = "接口调用失败!";
		}
		return msg;

	}

	/**
	 * 业务模块升级
	 * 
	 * @return
	 */
	public String upgradeService() {

		String msg = "";

//		MidWare.DeviceObject devObj = new MidWare.DeviceObject();
//		devObj.device_id = "1";
//		devObj.oui = oui;
//		devObj.device_serialnumber = serial_dev;

//		MidWare.ServiceObject serObj = new MidWare.ServiceObject();
//
//		serObj.service_code = type;
//		if (type.equals("1"))
//			serObj.service_name = "MediaFtp";
//		else
//			serObj.service_name = "DDNS";
//		
//		logger.debug("00000000000000000000000000000000000000000");
//
//		MidWare.Param[] paramArr = new MidWare.Param[1];
//		paramArr[0] = new MidWare.Param();
//		paramArr[0].param_name = "";
//		paramArr[0].param_type = "";
//		paramArr[0].param_value = "";
		

		MidWare.ServiceUpgradeObject[] serUpgradeObjectArr = new MidWare.ServiceUpgradeObject[1];
		serUpgradeObjectArr[0] = new MidWare.ServiceUpgradeObject();
		serUpgradeObjectArr[0].deviceObject = new MidWare.DeviceObject();
		serUpgradeObjectArr[0].deviceObject.device_id = "1";
		serUpgradeObjectArr[0].deviceObject.oui = oui;
		serUpgradeObjectArr[0].deviceObject.device_serialnumber = serial_dev;
		
		
		serUpgradeObjectArr[0].serviceObject = new MidWare.ServiceObject();
		serUpgradeObjectArr[0].serviceObject.service_code = type;
		
		if (type.equals("1"))
			serUpgradeObjectArr[0].serviceObject.service_name = "MediaFtp";
		else
			serUpgradeObjectArr[0].serviceObject.service_name = "DDNS";

		serUpgradeObjectArr[0].paramArr = new MidWare.Param[1];
		serUpgradeObjectArr[0].paramArr[0] = new MidWare.Param();
		serUpgradeObjectArr[0].paramArr[0].param_name = "1";
		serUpgradeObjectArr[0].paramArr[0].param_type = "1";
		serUpgradeObjectArr[0].paramArr[0].param_value = "1";
		

		MidWareCorba midCorba = new MidWareCorba();
		MidWare.DeviceObjectRep[] deviceObjectRepArr = midCorba
				.upgradeService(serUpgradeObjectArr);

		if (deviceObjectRepArr != null && deviceObjectRepArr.length > 0) {

			msg = "接口调用成功：";

			int resultCode = deviceObjectRepArr[0].result_code;
			
			msg += RETcode.getDescription(resultCode);
			
			String sql = " select count(*) as num from gw_user_midware_serv where username='"
					+ account + "' and serv_type_id=" + type;
			PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
			Map fields = DataSetBean.getRecord(sql);

			int num = Integer.parseInt((String) fields.get("num"));

			Date date = new Date();
			long nowtime = date.getTime();

			if (num > 0) { // update
				sql = "update gw_user_midware_serv set oper_type_id=6,stat="
						+ resultCode + ",oper_time=" + nowtime
						+ "  where username='" + account
						+ "' and serv_type_id=" + type;
			} else { // insert
				sql = "insert into gw_user_midware_serv(username,serv_type_id,oper_type_id,stat,oper_time) values('"
						+ account
						+ "',"
						+ type
						+ ",6,"
						+ resultCode
						+ ","
						+ nowtime + ")";
			}

			PrepareSQL psql2 = new PrepareSQL(sql);
	    	psql2.getSQL();

			int state = DataSetBean.executeUpdate(sql);

			if (state > 0) {
				msg += ",返回结果入库成功！";
			} else {
				msg += ",返回结果入库失败！";
			}

		} else {
			msg = "接口调用失败!";
		}
		return msg;

	}

	/**
	 * 获取参数结果
	 * 
	 * @return
	 */

	public MidWare.ServiceBussinessObject[] getServiceBussinessObject() {

		

		MidWare.DeviceObject devObj = new MidWare.DeviceObject();
		devObj.device_id = "1";
		devObj.oui = oui;
		devObj.device_serialnumber = serial_dev;

		logger.debug("oui=" + oui + "\ndevice_serialnumber=" + serial_dev);

		MidWare.ServiceObject serObj = new MidWare.ServiceObject();

		serObj.service_code = type;
		if (type.equals("1"))
			serObj.service_name = "MediaFtp";
		else
			serObj.service_name = "DDNS";
		logger.debug("type=" + type);

		MidWare.ServiceBussiness serBussiness = new MidWare.ServiceBussiness();
		serBussiness.deviceObject = new MidWare.DeviceObject();
		serBussiness.deviceObject = devObj;
		serBussiness.serviceObject = new MidWare.ServiceObject();
		serBussiness.serviceObject = serObj;

		MidWare.ServiceAccount serAccount = new MidWare.ServiceAccount();
		serAccount.username = account;
		serAccount.password = password;

		logger.debug("account=" + account + "\npassword=" + password);
		MidWare.Param[] paramArr = new MidWare.Param[1];

		MidWare.Param param = new MidWare.Param();
		param.param_name = "AD";
		param.param_type = "String";
		param.param_value = account;

		paramArr[0] = new MidWare.Param();
		paramArr[0] = param;

//		param = new MidWare.Param();
//		param.param_name = "Password";
//		param.param_type = "String";
//		param.param_value = pass;
//		paramArr[1] = new MidWare.Param();
//		paramArr[1] = param;
//
//		param = new MidWare.Param();
//		param.param_name = "DomainName";
//		param.param_type = "String";
//		param.param_value = domainname;
//		paramArr[2] = new MidWare.Param();
//		paramArr[2] = param;
//
//		param = new MidWare.Param();
//		param.param_name = "SERVER";
//		param.param_type = "String";
//		param.param_value = server;
//		paramArr[3] = new MidWare.Param();
//		paramArr[3] = param;
//
//		param = new MidWare.Param();
//		param.param_name = "server";
//		param.param_type = "String";
//		param.param_value = server2;
//		paramArr[4] = new MidWare.Param();
//		paramArr[4] = param;

		logger.debug("username=" + username + "\npassword=" + pass
				+ "\ndomainname=" + domainname + "\nserver=" + server
				+ "\nserver2=" + server2);

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
	 * 用户权限接口
	 * 
	 * @return
	 */
	public String getUserInfo() {

		String msg = "";

		String area_id = request.getParameter("area_id");
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		String status = request.getParameter("status");
		String permit = request.getParameter("permit");

		MidWare.OperatorObject operatorObject = new MidWare.OperatorObject();
		operatorObject.area_id = area_id;
		operatorObject.login_id = login;
		operatorObject.login_pass = password;
		operatorObject.status = status;
		operatorObject.permitArr = permit.split(",");

		MidWare.OperatorObject[] operatorObjectArr = new MidWare.OperatorObject[1];
		operatorObjectArr[0] = new MidWare.OperatorObject();
		operatorObjectArr[0] = operatorObject;

		MidWareCorba midCorba = new MidWareCorba();
		MidWare.OperatorObjectRep[] operatorObjectRepArr = midCorba
				.modifyOperator(operatorObjectArr);

		if (operatorObjectRepArr != null && operatorObjectRepArr.length > 0) {

			msg = "接口调用成功:";
			int resultCode = operatorObjectRepArr[0].result_code;
			
			msg += RETcode.getDescription(resultCode);
			


		} else {
			msg = "接口调用失败！";
		}
		return msg;
	}

	/**
	 * 
	 * @param map
	 * @return
	 */

	public int work(Map map) {
		XMLBuilder xmlBuilder = new XMLBuilder(map);
		String req = xmlBuilder.getXML();
		logger.debug("xmlBuilder.getXML()=" + req);
		String res = client.work(req);
		logger.debug("resXML=" + res);
		return xmlBuilder.getRETcode(res);
	}

	public String getDevArea(String area_id,String add_string, String rmv_string) {

		String msg = "";
//		String area_id = request.getParameter("area_id");
//
//		String add_string = request.getParameter("add_string").trim();
//		String rmv_string = request.getParameter("rmv_string").trim();
		
		logger.debug("add_string :" + add_string);
		logger.debug("rmv_string :" + rmv_string);

		// 设备添加到域名
		if (!add_string.equals("") && add_string != null) {

			MidWare.DevAreaObject[] devAreaObjectArr = getAreaObjectArr(
					area_id, add_string);

			logger.debug("devAreaObjectArr :" + devAreaObjectArr);

			MidWareCorba midCorba = new MidWareCorba();
			MidWare.DevAreaObjectRep[] devAreaObjectRepArr = midCorba
					.addAreaDev(devAreaObjectArr);

			if (devAreaObjectRepArr != null && devAreaObjectRepArr.length > 0) {
				
				int resultCode = devAreaObjectRepArr[0].result_code;
				
				msg = "接口调用成功：";
				
				msg += RETcode.getDescription(resultCode);

				
				logger.debug("----------" + msg + "--------------");

			} else {

				msg = "接口调用失败：";
				logger.warn("----------------"+ msg + "------------------");
			}

		}

		if (!rmv_string.equals("") && rmv_string != null) {

			MidWare.DevAreaObject[] devAreaObjectArr = getAreaObjectArr(
					area_id, rmv_string);
			MidWareCorba midCorba = new MidWareCorba();
			MidWare.DevAreaObjectRep[] devAreaObjectRepArr = midCorba
					.delAreaDev(devAreaObjectArr);

			if (devAreaObjectRepArr != null && devAreaObjectRepArr.length > 0) {
				int resultCode = devAreaObjectRepArr[0].result_code;
				
				msg = "接口调用成功：";
				
				msg += RETcode.getDescription(resultCode);
				
				
				
			} else {
				msg = "接口调用失败！";
				
			}
			
			logger.warn("------------"+msg+"--------------------");
		}
		return msg;
	}
	/**
	 * 
	 * @param area_id
	 * @param serial_number
	 * @return
	 */
	public MidWare.DevAreaObject[] getAreaObjectArr(String area_id,
			String serial_number) {

		String[] des = serial_number.split(",");
		serial_number = "";

		for (int i = 0; i < des.length; i++) {

			if (i == 0)
				serial_number = "'" + des[i] + "'";
			else
				serial_number += ",'" + des[i] + "'";

		}

		MidWare.DevAreaObject devAreaObject = new MidWare.DevAreaObject();

		String sql = "select device_id,oui,device_serialnumber from tab_gw_device where device_serialnumber in ("
				+ serial_number + ") and device_status=1";

		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
    	Cursor cur = DataSetBean.getCursor(sql);

		Map fields = cur.getNext();

		int i = 0;
		logger.debug("cur.getRecordSize() :" + cur.getRecordSize());

		MidWare.DeviceObject[] devObjectArr = new MidWare.DeviceObject[cur
				.getRecordSize()];

		if (fields != null) {

			while (fields != null) {

				MidWare.DeviceObject devObj = new MidWare.DeviceObject();
				devObj.device_id = (String) fields.get("device_id");
				devObj.oui = (String) fields.get("oui");
				devObj.device_serialnumber = (String) fields
						.get("device_serialnumber");

				devObjectArr[i] = new MidWare.DeviceObject();
				devObjectArr[i] = devObj;
				logger.debug("i:" + i);
				i++;
				fields = cur.getNext();

			}

		}
		logger.debug("cur.getRecordSize():" + cur.getRecordSize());
		MidWare.DevAreaObject[] devAreaObjectArr = new MidWare.DevAreaObject[1];
		devAreaObjectArr[0] = new MidWare.DevAreaObject();
		devAreaObjectArr[0].area_id = area_id;
		devAreaObjectArr[0].deviceObjectArr = new MidWare.DeviceObject[cur
				.getRecordSize()];
		devAreaObjectArr[0].deviceObjectArr = devObjectArr;

		logger.debug("devAreaObjectArr :" + devAreaObjectArr);

		return devAreaObjectArr;

	}

}
