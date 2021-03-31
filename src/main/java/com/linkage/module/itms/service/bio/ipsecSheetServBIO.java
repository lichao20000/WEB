package com.linkage.module.itms.service.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import PreProcess.UserInfo;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.PreProcessInterface;
import com.linkage.module.gwms.util.corba.obj.PreServInfoOBJ;
import com.linkage.module.itms.service.act.ipsecSheetServACT;
import com.linkage.module.itms.service.dao.ipsecSheetServDAO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-12-26
 * @category com.linkage.module.itms.service.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class ipsecSheetServBIO
{
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ipsecSheetServACT.class);
	private ipsecSheetServDAO dao;
	private int maxPage_splitPage;
	public List<Map> getIpsecSheetServInfo(String cityId,String startOpenDate1,String endOpenDate1,
			String usernameType,String username ,String openstatus,
			int curPage_splitPage, int num_splitPage,String type)
	{
		logger.warn("getIpsecSheetServInfo====方法入口==cityId{},startOpenDate1{},endOpenDate1{},usernameType{},username{},openstatus{},type{}=="
				+cityId,startOpenDate1,endOpenDate1,usernameType,username,openstatus,type);
		List<Map> userIdList = null;
		if(!StringUtil.IsEmpty(username))
		{
			if(usernameType.equals("1"))
			{
				userIdList=dao.getUserBySN(username, type);
			}else if(usernameType.equals("2"))
			{
				userIdList=dao.getUserByServ(username, type);
			}
		}
		logger.warn("userIdList===="+userIdList);
		if(userIdList!=null&&userIdList.size()>0)
		{
		String userId = String.valueOf(userIdList.get(0).get("user_id"));
		List<Map> list=dao.getIpsecSheetServInfo(cityId, startOpenDate1, endOpenDate1, usernameType, username, openstatus, curPage_splitPage, num_splitPage, type, userId);
		maxPage_splitPage=dao.getquerypaging(cityId, startOpenDate1, endOpenDate1, usernameType, username, openstatus, curPage_splitPage, num_splitPage, type, userId);
		return list;
		}else
		{
			return userIdList;
		}
		/*if (userIdList != null && userIdList.size() > 0) {
			userId = getManyUserId(userIdList);
		}*/
		
	}
	/**
	 * 调用预读接口，重新激活工单 业务下发 
	 * @param userId
	 * @param deviceId
	 * @param oui
	 * @param deviceSN
	 * @param servTypeId
	 * @param servstauts
	 * @return
	 */
	public String callPreProcess(String userId, String deviceId, String oui,
			String deviceSN, String servTypeId, String servstauts) {
		logger.debug("callPreProcess({},{},{},{},{},{},{})", new Object[] {
				userId, deviceId, oui, deviceSN, servTypeId, servstauts });
		PreServInfoOBJ obj = new PreServInfoOBJ(userId, deviceId, oui,
				deviceSN, servTypeId, servstauts);
		obj.setGatherId("1");
		// 更新业务用户表的业务开通状态
		dao.updateServOpenStatus(StringUtil.getLongValue(userId), new Integer(
				servTypeId).intValue());
		UserInfo uinfo = CreateObjectFactory.createPreProcess().GetPPBindUserList(obj);
		UserInfo[] uinfoarray = new UserInfo[] { uinfo };
		// 默认是调用itms的预读，在融合环境下需要判断gw_type来区分ITMS和BBMS
		PreProcessInterface ppc = CreateObjectFactory.createPreProcess(
				LipossGlobals.getGw_Type(deviceId));
		logger.warn(
				"inter_ PreProcess (userId:{}, deviceId:{}, gatherId:{}, oui:{}, deviceSN:{}, servTypeId:{}, operTypeId:{})",
				new Object[] { userId, deviceId, "1", oui, deviceSN,
						servTypeId, servstauts });
		int ret = ppc.processServiceInterface(uinfoarray);
		logger.warn("inter_ret_ PreProcess({})", ret);
		return StringUtil.getStringValue(ret);
	}
	public List<Map> getIpsecExcel(String cityId, String startOpenDate1,
			String endOpenDate1, String usernameType, String username, String openstatus, String type){
		List<Map> userIdList = null;
		if(!StringUtil.IsEmpty(username))
		{
			if(usernameType.equals("1"))
			{
				userIdList=dao.getUserBySN(username, type);
			}else if(usernameType.equals("2"))
			{
				userIdList=dao.getUserByServ(username, type);
			}
		}
		if(userIdList!=null&&userIdList.size()>0)
		{
			String userId = String.valueOf(userIdList.get(0).get("user_id"));	
			List<Map> list=dao.getIpsecExcel(cityId, startOpenDate1, endOpenDate1, usernameType, username, openstatus, type, userId);
		return list;
		}else
		{
			return userIdList;
		}
	}
	/**
	 * 用户基本信息
	 * @param seconds
	 * @return
	 */
	public List<Map<String, Object>> queryUserRelatedBaseInfo(String userId,String gw_type)
	{
		List<Map<String,Object>> list=dao.queryUserRelatedBaseInfo(userId, gw_type);
		List<Map<String,Object>> listshow=new ArrayList<Map<String,Object>>();
		if(list!=null&&list.size()>0)
		{
				Map<String, Object> map=new HashMap<String, Object>();
				String user_state=(String) list.get(0).get("user_state");
				if ("0".equals(user_state))
				{
					user_state = "删除用户";
				}
				else if ("1".equals(user_state))
				{
					user_state = "开户";
				}
				else if ("2".equals(user_state))
				{
					user_state = "暂停";
				}
				else if ("3".equals(user_state))
				{
					user_state = "销户";
				}
				else if ("4".equals(user_state))
				{
					user_state = "更换设备";
				}
				else
				{
					user_state = "-";
				}
				map.put("user_state", user_state);
				String user_type_id=StringUtil.getStringValue(list.get(0).get("user_type_id"));
				if(StringUtil.IsEmpty(user_type_id)){
					map.put("user_type_id", "其他");
				}
				String cred_type_id =StringUtil.getStringValue(list.get(0).get("cred_type_id"));
				if (cred_type_id.equals("0"))
				{
					cred_type_id="未选择";
				}
				else if (cred_type_id.equals("1"))
				{
					cred_type_id="其他";
				}
				else if (cred_type_id.equals("2"))
				{
					cred_type_id="身份证";
				}
				else if (cred_type_id.equals("3"))
				{
					cred_type_id="军官证";
				}
				else if (cred_type_id.equals("4"))
				{
					cred_type_id="工作证";
				}
				else
				{
					cred_type_id="未知";
				}
				map.put("cred_type_id", cred_type_id);
				String name=CityDAO.getCityName(StringUtil.getStringValue(list.get(0).get("city_id")));
				map.put("city_name", StringUtil.IsEmpty(name)?"未知":name);
				String office_id=(String)list.get(0).get("office_id");
				List<Map<String,String>> office =null;
				office=dao.getofficename(office_id);
				if(null!=office){
					String office_name=office.get(0).get("office_name");
					map.put("office_name", StringUtil.IsEmpty(office_name)?"未知":office_name.toLowerCase());
				}
				List<Map> areaname=null;
				String user_id=(String)list.get(0).get("user_id");
				areaname=dao.getUserArea(user_id, gw_type);
				if(null!=areaname){
				String area_name=(String) areaname.get(0).get("area_name");
				map.put("area_name", StringUtil.IsEmpty(area_name)?"未知":area_name.toLowerCase());
				}
				if(!StringUtil.IsEmpty((String)list.get(0).get("dealdate"))){
					map.put("dealdate", transDate(list.get(0).get("DEALDATE")));
				}
				if(!StringUtil.IsEmpty((String)list.get(0).get("opendate"))){
					map.put("opendate", transDate(list.get(0).get("opendate")));
				}
				if(!StringUtil.IsEmpty((String)list.get(0).get("onlinedate"))){
					map.put("onlinedate", transDate(list.get(0).get("onlinedate")));
				}
				if(!StringUtil.IsEmpty((String)list.get(0).get("pausedate"))){
					map.put("pausedate", transDate(list.get(0).get("pausedate")));
				}
				if(!StringUtil.IsEmpty((String)list.get(0).get("closedate"))){
					map.put("closedate", transDate(list.get(0).get("closedate")));
				}
				if(!StringUtil.IsEmpty((String)list.get(0).get("updatetime"))){
					map.put("updatetime", transDate(list.get(0).get("updatetime")));
				}
				List<Map> userTypeNamelist=null;
				userTypeNamelist=dao.getUserDevType(userId);
				if(!StringUtil.IsEmpty((String)userTypeNamelist.get(0).get("type_name")))
				{
					String userTypeName=(String)userTypeNamelist.get(0).get("type_name");
					map.put("userTypeName", userTypeName);
				}
				map.put("username", list.get(0).get("username"));
				map.put("user_id", list.get(0).get("user_id"));
				map.put("realname", list.get(0).get("realname"));
				map.put("credno", list.get(0).get("credno"));
				map.put("device_ip", list.get(0).get("device_ip"));
				map.put("is_active", list.get(0).get("is_active"));
				map.put("email", list.get(0).get("email"));
				map.put("linkphone", list.get(0).get("linkphone"));
				map.put("linkaddress", list.get(0).get("linkaddress"));
				map.put("address", list.get(0).get("address"));
				map.put("cust_type_id", list.get(0).get("cust_type_id"));
				map.put("device_id", list.get(0).get("device_id"));
				map.put("oui", list.get(0).get("oui"));
				map.put("device_serialnumber", list.get(0).get("device_serialnumber"));
				map.put("binddate", list.get(0).get("binddate"));
				map.put("userline", list.get(0).get("userline"));
				map.put("is_chk_bind", list.get(0).get("is_chk_bind"));
				map.put("cpe_mac", list.get(0).get("cpe_mac"));
				map.put("type_name", list.get(0).get("type_name"));
				map.put("spec_name", list.get(0).get("spec_name"));
				
				listshow.add(map);
		}
		return listshow;
	}
	/**
	 * 用户当前拥有业务
	 */
	public List<Map<String,Object>> getServiceInfo(String user_id,String gw_type)
	{
		return dao.getServiceInfo(user_id, gw_type);
	}
	/**
	 * 查询单台设备
	 * @param device_id
	 * @param m_AreaId
	 * @return
	 */
	public List<Map<String,String>> getSingleDeviceInfo(String device_id,long m_AreaId)
	{
		List<Map<String,String>> listshow=new ArrayList<Map<String,String>>();
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		List<Map<String,String>> onlinelist=new ArrayList<Map<String,String>>();
		List<Map<String,String>> DeviceModelVersionList=new ArrayList<Map<String,String>>();
		List<Map<String,String>> CustomerNameList=new ArrayList<Map<String,String>>();
		List<Map<String,String>> DeviceModelTemplate=new ArrayList<Map<String,String>>();
		List<Map<String,String>> AreaIdMapName=new ArrayList<Map<String,String>>();
		List<Map<String,String>> CustomerOfDevList=new ArrayList<Map<String,String>>();
		listshow=dao.getSingleDeviceInfo(device_id, m_AreaId);
		if(listshow.size()>0&&listshow!=null)
		{
			Map<String,String> map=new HashMap<String, String>();
			Map<String, String> bssDevMap = Global.G_BssDev_PortName_Map;
			map.put("oui", String.valueOf(listshow.get(0).get("oui")));
			map.put("vendor_id", String.valueOf(listshow.get(0).get("vendor_id")));
			map.put("device_id", String.valueOf(listshow.get(0).get("device_id")));
			map.put("device_serialnumber", String.valueOf(listshow.get(0).get("device_serialnumber")));
			map.put("cpe_mac", String.valueOf(listshow.get(0).get("cpe_mac")));
			map.put("spec_name", String.valueOf(listshow.get(0).get("spec_name")));
			map.put("loopback_ip", String.valueOf(listshow.get(0).get("loopback_ip")));
			map.put("complete_time", transDate(listshow.get(0).get("complete_time")));
			String online_status= String.valueOf(listshow.get(0).get("online_status"));
			if (null != online_status && "1".equals(online_status))
			{
				map.put("online_status", "能正常交互");
			}
			else
			{
				map.put("online_status", "不能正常交互");
			}
			map.put("device_name", String.valueOf(listshow.get(0).get("device_name")));
			map.put("devicetype_id", String.valueOf(listshow.get(0).get("devicetype_id")));
			String device_status=String.valueOf(listshow.get(0).get("device_status"));
			if(device_status.equals("1"))
			{
				map.put("device_status", "已确认");
			}else if(device_status.equals("0"))
					{
				map.put("device_status", "未确认");
					}
			else
			{
				map.put("device_status", "已删除");
			}
			map.put("maxenvelopes", String.valueOf(listshow.get(0).get("maxenvelopes")));
			String gw_type=String.valueOf(listshow.get(0).get("gw_type"));
			if (gw_type.equals("0"))
			{
				map.put("gw_type_name", "普通网络设备");
			}
			else if (gw_type.equals("1"))
			{
				map.put("gw_type_name","家庭网关设备");
			}
			else if (gw_type.equals("4"))
			{
				map.put("gw_type_name", "安全网关设备");
			}
			else if (gw_type.equals("5"))
			{
				map.put("gw_type_name", "混合型网关");
			}
			else
			{
				map.put("gw_type_name", "企业网关设备");
			}
			String city_id=String.valueOf(listshow.get(0).get("city_id"));
			Map cityMap = CityDAO.getCityIdCityNameMap();
			String city_name=String.valueOf(cityMap.get(city_id));
			map.put("city_name", city_name);
			map.put("device_type", String.valueOf(listshow.get(0).get("device_type")));
			String customer_id=String.valueOf(listshow.get(0).get("customer_id"));
			CustomerNameList=dao.getCustomerName(customer_id);
			if(CustomerNameList!=null&&CustomerNameList.size()>0)
			{
				map.put("customer_name", String.valueOf(CustomerNameList.get(0).get("customer_name")));
			}
			map.put("device_url", String.valueOf(listshow.get(0).get("device_url")));
			String deviceModel="";
			DeviceModelTemplate=dao.getDeviceModelTemplate(String.valueOf(listshow.get(0).get("oui")), String.valueOf(listshow.get(0).get("devicetype_id")));
			StringBuffer sb = new StringBuffer();
			if(DeviceModelTemplate!=null&&DeviceModelTemplate.size()>0)
			{
				sb.append("" + String.valueOf(DeviceModelTemplate.get(0).get("template_name") + "\n"));
			}
			deviceModel=sb.toString();
			map.put("deviceModelTemplate", deviceModel);
			AreaIdMapName=dao.getAreaIdMapName();
			if(AreaIdMapName!=null&&AreaIdMapName.size()>0)
			{
				map.put("device_area_name",String.valueOf( AreaIdMapName.get(0).get(listshow.get(0).get("device_area_id"))));
			}
			map.put("maxenvelopes", String.valueOf(listshow.get(0).get("maxenvelopes")));
			if(bssDevMap!=null&&bssDevMap.size()>0)
			{
				String spec_id = String.valueOf( map.get("spec_id"));
				map.put("spec_name", String.valueOf(bssDevMap.get(spec_id)));
			}
			onlinelist=dao.getonline_status(device_id);
			if(onlinelist.size()>0&&onlinelist!=null)
			{
				String online=String.valueOf(onlinelist.get(0).get("online_status"));
				if(online.equals("1"))
				{
					map.put("online_status", "在线");
				}else{
					map.put("online_status", "不在线");
				}
				map.put("last_time", transDate(String.valueOf(onlinelist.get(0).get("last_time"))));
				map.put("oper_time", transDate(String.valueOf(onlinelist.get(0).get("oper_time"))));
			}
			String vendor_id=String.valueOf(listshow.get(0).get("vendor_id"));
			String devicetype_id=String.valueOf(listshow.get(0).get("devicetype_id"));
			DeviceModelVersionList=dao.getDeviceModelVersion(vendor_id, devicetype_id);
			if(DeviceModelVersionList.size()>0&&DeviceModelVersionList!=null)
			{
				map.put("software_version", String.valueOf(DeviceModelVersionList.get(0).get("softwareversion")));
				map.put("handware_version", String.valueOf(DeviceModelVersionList.get(0).get("hardwareversion")));
				map.put("spec_version", String.valueOf(DeviceModelVersionList.get(0).get("specversion")));
				map.put("manufacturer", String.valueOf(DeviceModelVersionList.get(0).get("vendor_name")));
				map.put("device_model", String.valueOf(DeviceModelVersionList.get(0).get("device_model")));
				map.put("is_normal", String.valueOf(DeviceModelVersionList.get(0).get("is_normal")));
				map.put("is_check", String.valueOf(DeviceModelVersionList.get(0).get("is_check")));
			}
			List<Map<String,String>> list1=new ArrayList<Map<String,String>>();
			list1=dao.x_username(device_id);
			
			if(list1!=null&&list1.size()>0)
			{
				map.put("x_com_username", String.valueOf(list1.get(0).get("x_com_username")));
				map.put("x_com_passwd", String.valueOf(list1.get(0).get("x_com_passwd")));
				map.put("cpe_username", String.valueOf(list1.get(0).get("cpe_username")));
				map.put("cpe_passwd", String.valueOf(list1.get(0).get("cpe_passwd")));
				map.put("acs_username", String.valueOf(list1.get(0).get("acs_username")));
				map.put("acs_passwd", String.valueOf(list1.get(0).get("acs_passwd")));
				map.put("x_com_username", String.valueOf(list1.get(0).get("x_com_username")));
			}
			CustomerOfDevList=dao.getCustomerOfDev(device_id, gw_type);
			if(CustomerOfDevList!=null&&CustomerOfDevList.size()>0)
			{
				map.put("username", String.valueOf(CustomerOfDevList.get(0).get("username")));
				map.put("service_name", String.valueOf(CustomerOfDevList.get(0).get("service_name")));
				map.put("user_id", String.valueOf(CustomerOfDevList.get(0).get("user_id")));
				List<Map<String,String>> getTabNetServParam=new ArrayList<Map<String,String>>();
				getTabNetServParam=dao.getTabNetServParamByUserId(String.valueOf(CustomerOfDevList.get(0).get("user_id")));
				if(getTabNetServParam!=null&&getTabNetServParam.size()>0)
				{
					map.put("ip_type", String.valueOf(getTabNetServParam.get(0).get("ip_type")));
				}
			}
			
			list.add(map);
		}
		
		return list;
	}
	/**
	* 获取在线状态
	*/
	/*public String queryConnCondition(String deviceid)
	{
		List<Map<String,String>> connectionList=dao.queryConnCondition(deviceid);
		int int_flag =0;
		String online_status = "";
		if (connectionList.size()>0 && !connectionList.isEmpty()){
		online_status=StringUtil.getStringValue(connectionList.get(0), "online_status","0");
		}if("1".equals(online_status)){
			logger.debug("设备id在线状态是为1,测试连接状态",new Object[]{deviceid});
			try{
				int_flag = testConnection(deviceid);
			}catch (Exception e) {
				logger.error("测试连接异常[{}]",new Object[]{e});
			}
			logger.debug("queryConnMsgBIO()==>方法出口",new Object[]{int_flag+""});
			return int_flag+"";
		}else{
			logger.debug("设备id在线状态是不为1,返回连接状态",new Object[]{deviceid});
			logger.debug("queryConnMsgBIO()==>方法出口",new Object[]{"0"});
			return "0";
		}
	}*/
	/**
	 * 设备交互诊断
	 * @author hp
	 * @param deviceId 设备id
	 * @return int
	 * @		1:正常交互 -6:设备正被操作 其余：不正常交互
	 */			
/*	public int testConnection(String deviceId)
	{
		logger.debug("testConnection({})==>方法入口", deviceId);
		// 得到设备类型
		String gw_type = LipossGlobals.getGw_Type(deviceId);
		DevRpc[] devRPCArr = new DevRpc[1];
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = deviceId;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "";
		rpcArr[0].rpcValue = "";
		devRPCArr[0].rpcArr = rpcArr;
		// corba
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr, Global.RpcTest_Type);
		
		int flag = 0;
		if (devRPCRep == null || devRPCRep.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", deviceId);
		}
		else if (devRPCRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", deviceId);
		}
		else
		{
			flag = devRPCRep.get(0).getStat();
		}
		logger.debug("testConnection({})==>方法出口", flag);
		return flag;
	}*/
	public List<Map<String,String>> getServiceStatByDevice(String device_id)
	{
		return dao.getServiceStatByDevice(device_id);
	}
	/**
	 * 查询宽带上网工单详情
	 */
	public List<Map<String,Object>> getIpsecInfeoInternet(String user_id,String gw_type)
	{
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> listshow=new ArrayList<Map<String,Object>>();
		list= dao.getIpsecInfeoInternet(user_id, gw_type);
		if(list!=null&&list.size()>0)
		{
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("username", list.get(0).get("username"));
			String servtypeid=String.valueOf(list.get(0).get("serv_type_id"));
			Map<String,String> mapshow=new HashMap<String,String>();
			mapshow=dao.getServType(); 
			String serv_type_id=mapshow.get(servtypeid);
			String serv_status=String.valueOf(list.get(0).get("serv_status"));
			map.put("serv_type_id", serv_type_id);
			if ("1".equals(serv_status))
			{
				map.put("serv_status", "开通");
			}
			else if ("2".equals(serv_status))
			{
				map.put("serv_status", "暂停");
			}
			else if ("3".equals(serv_status))
			{
				map.put("serv_status", "销户");
			}
			else
			{
				map.put("serv_status", "-");
			}
			map.put("enable", list.get(0).get("enable"));
			map.put("request_id", list.get(0).get("request_id"));
			map.put("ipsec_type", list.get(0).get("ipsec_type"));
			map.put("remote_domain", list.get(0).get("remote_domain"));
			map.put("remote_subnet", list.get(0).get("remote_subnet"));
			map.put("local_subnet", list.get(0).get("local_subnet	"));
			map.put("remote_ip", list.get(0).get("remote_ip"));
			map.put("exchange_mode", list.get(0).get("exchange_mode"));
			map.put("ike_auth_algorithm", list.get(0).get("ike_auth_algorithm"));
			map.put("ike_auth__method", list.get(0).get("ike_auth__method"));
			map.put("ike_encryption_algorithm", list.get(0).get("ike_encryption_algorithm"));
			map.put("ike_dhgroup", list.get(0).get("ike_dhgroup"));
			map.put("ike_idtype", list.get(0).get("ike_idtype"));
			map.put("ike_localname", list.get(0).get("ike_localname"));
			map.put("ike_remotename", list.get(0).get("ike_remotename"));
			map.put("ike_presharekey", list.get(0).get("ike_presharekey"));
			map.put("ipsec_out_interface", list.get(0).get("ipsec_out_interface"));
			map.put("ipsec_encapsulation_mode", list.get(0).get("ipsec_encapsulation_mode"));
			map.put("ipsec_transform", list.get(0).get("ipsec_transform"));
			map.put("esp_auth_algorithem", list.get(0).get("esp_auth_algorithem"));
			map.put("esp_encrypt_algorithm", list.get(0).get("esp_encrypt_algorithm"));
			map.put("ipsec_pfs", list.get(0).get("ipsec_pfs"));
			map.put("ike_saperiod", list.get(0).get("ike_saperiod"));
			map.put("ipsec_satime_period", list.get(0).get("ipsec_satime_period"));
			map.put("ipsec_satraffic_period", list.get(0).get("ipsec_satraffic_period"));
			map.put("ah_auth_algorithm", list.get(0).get("ah_auth_algorithm"));
			map.put("dpd_enable", list.get(0).get("dpd_enable"));
			map.put("dpd_threshold", list.get(0).get("dpd_threshold"));
			map.put("dpd_retry", list.get(0).get("dpd_retry"));
			map.put("open_date", transDate(list.get(0).get("open_date")));
			map.put("updatetime", transDate(list.get(0).get("updatetime")));
			map.put("completedate", transDate(list.get(0).get("completedate")));
			map.put("open_date", transDate(list.get(0).get("open_date")));
			String open_status=String.valueOf(list.get(0).get("open_status"));
			if(open_status.equals("1"))
			{
				map.put("open_status", "成功");
			}else if(open_status.equals("0"))
			{
				map.put("open_status", "未做");
			}else if(open_status.equals("-1"))
			{
				map.put("open_status", "失败");
			}
			listshow.add(map);
		}
		return listshow;
	}
	private static final String transDate(Object seconds)
	{
		if (seconds != null)
		{
			try
			{
				DateTimeUtil dt = new DateTimeUtil(
						Long.parseLong(seconds.toString()) * 1000);
				return dt.getLongDate();
			}
			catch (NumberFormatException e)
			{
				logger.error(e.getMessage(), e);
			}
			catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
		}
		return "";
	}
	/**
	 * 多个用户拼装user_id sql
	 * 
	 * @param userIdList
	 * @return
	 */
	private String getManyUserId(List<Map> userIdList) {
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		for (Map map : userIdList) {
			sb.append(map.get("user_id").toString());
			sb.append(",");
		}
		return sb.substring(0, sb.length() - 1) + ")";
	}
	public ipsecSheetServDAO getDao()
	{
		return dao;
	}
	
	public void setDao(ipsecSheetServDAO dao)
	{
		this.dao = dao;
	}
	
	public int getMaxPage_splitPage()
	{
		return maxPage_splitPage;
	}
	
	public void setMaxPage_splitPage(int maxPage_splitPage)
	{
		this.maxPage_splitPage = maxPage_splitPage;
	}
	
	
}
