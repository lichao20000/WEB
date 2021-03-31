
package com.linkage.module.itms.resource.bio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;
import ResourceBind.BindInfo;
import ResourceBind.ResultInfo;
import ResourceBind.UnBindInfo;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.litms.resource.FileSevice;
import com.linkage.litms.resource.ResetOrRebootAct;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.ResourceBindInterface;
import com.linkage.module.gwms.util.SocketUtil;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.dao.FaultTreadtMentDAO;

/**
 * 故障处理场景
 * 
 * @author os_hanzz date 2015-06-09
 */
public class FaultTreadtMentBIO
{

	// 日志操作
	Logger logger = LoggerFactory.getLogger(FaultTreadtMentBIO.class);
	private FaultTreadtMentDAO dao;
	private List<Map> bssParaList;
	// 开启无线
	private static String OPEN_WIRELESS = "2001";
	// 关闭无线
	private static String CLOSE_WIRELESS = "2003";
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;

	@SuppressWarnings({ "unchecked", "unused" })
	public List<Map<String,String>> queryUserAndDeviceInfo(String queryParam, String queryType,String isRealtimeQuery)
	{
		List listAll = new ArrayList<Map<String,String>>();
		listAll= dao.queryDeviceInfo(queryParam, queryType);
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();;
		if (0!=listAll.size()&&null!=listAll)
		{
			//返回list只有一条记录则显示详细信息否则对数据进行处理展示所有记录
			if(listAll.size() == 1)
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", StringUtil.getStringValue(((Map<String, String>)listAll.get(0)).get("user_id")));
				map.put("device_id",
						StringUtil.getStringValue(((Map<String, String>)listAll.get(0)).get("device_id")));
				list = queryUserAndDeviceInfoMethod(map,isRealtimeQuery);
			}else
			{
				//多条记录时显示查询出的所有用户及设备
				cityMap = CityDAO.getCityIdCityNameMap();
				Calendar time = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
				for(int i =0;i<listAll.size();i++)
				{
					Map<String,String> map = (Map<String, String>) listAll.get(i);
					String city_id_fault = StringUtil.getStringValue(map, "city_id");
					String city_name = cityMap.get(city_id_fault);
					if(null == city_name||"" == city_name)
					{
						city_name = "属地异常";
					}
					map.put("city_id", city_name);
					String user_type_id_fault = StringUtil.getStringValue(map, "user_type_id");
					String user_type_name = "";
					if (user_type_id_fault.equals("1"))
					{
						user_type_name = "现场安装";
					}
					else if (user_type_id_fault.equals("2"))
					{
						user_type_name = "BSS工单";
					}
					else if (user_type_id_fault.equals("3"))
					{
						user_type_name = "手工添加";
					}
					else if (user_type_id_fault.equals("4"))
					{
						user_type_name = "BSS同步";
					}
					map.put("user_type_id", user_type_name);

					String opendate_fault = StringUtil.getStringValue(map, "opendate");
					time.setTimeInMillis((Long.parseLong(opendate_fault)) * 1000);
					opendate_fault =  df.format(time.getTime());
					map.put("opendate", opendate_fault);
				}

				List<Map<String,String>> showList = new ArrayList<Map<String,String>>();
				Map<String, String> showMap =  new HashMap<String, String>();
				//多条记录时加入标记，区分多条记录与单条记录
				showMap.put("showList", "showList");
				showList.add(showMap);
				listAll.add(showList);
				return listAll;
			}
		}
		return list;
	}
	public String queryConnMsgBIO(String device_id)
	{
		String online_status = dao.queryConnCondition(device_id);
		int int_flag =0;
		if("1".equals(online_status)){
			try{
				int_flag = testConnection(device_id);
			}catch (Exception e) {
				logger.warn("测试连接异常");
			}
			return int_flag+"";
		}else{
			return "0";
		}
		
	}
	public List<Map<String,String>> queryUserAndDeviceInfoMethod(Map<String, String> map,String isRealtimeQuery)
	{
		List list = new ArrayList<Map<String,String>>();
		Map deviceMap = new HashMap();
		Map userMap = new HashMap();
		List<Map> serList = new ArrayList<Map>();
		List<Map<String,String>> serTypeList = new ArrayList<Map<String,String>>();
		String device_id = map.get("device_id");
		String user_id = map.get("user_id");
		logger.warn("FaultTreadtMentBIO->user_id={},device_id={}", user_id, device_id);
		if (!StringUtil.IsEmpty(device_id))
		{
			deviceMap = dao.queryDeviceInfo(device_id);
			// 测试连接 device_id为空=失败  不为空==待定
			
			deviceMap.put("deviceConnStatus", device_id);
		}else{
			deviceMap.put("deviceConnStatus", "0");
		}
		if (!StringUtil.IsEmpty(user_id))
		{
			userMap = dao.queryUserInfo(user_id);
			serList = dao.queryBssInfo(user_id,isRealtimeQuery);
			serTypeList = serTypeList(user_id,isRealtimeQuery);
		}
		// 0为deviceMap,1为userMap,2为serList,3为serTypeList
		list.add(deviceMap);
		list.add(userMap);
		list.add(serList);
		list.add(serTypeList);
		return list;
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
	 * 设备交互诊断
	 * 
	 * @param device_id
	 * @return
	 */
	public int testConnection(String device_id)
	{
		// 得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		DevRpc[] devRPCArr = new DevRpc[1];
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;
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
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
		}
		else if (devRPCRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", device_id);
		}
		else
		{
			flag = devRPCRep.get(0).getStat();
		}
		return flag;
	}

	/**
	 * 重启及恢复出厂操作
	 * 
	 * @param request
	 * @return
	 */
	public String Devicerestart(HttpServletRequest request)
	{
		ResetOrRebootAct resetOrRebootAct = new ResetOrRebootAct();
		Map map = resetOrRebootAct.SetWayV1(request);
		logger.warn("FaultTreadtMentBIO->request.device_id={}",request.getAttribute("device_id"));
		logger.warn("FaultTreadtMentBIO->request.oid_type={}",request.getAttribute("oid_type"));
		logger.warn("FaultTreadtMentBIO->request.type={}",request.getAttribute("type"));
		Set set = map.keySet();
		Iterator iterator = set.iterator();
		String name = "";
		String value = "";
		while (iterator.hasNext())
		{
			name = (String) iterator.next();
			value = (String) map.get(name);
		}
		return value;
	}

	/**
	 * 获取业务类型
	 * @param user_id
	 * @return
	 */
	public List<Map<String,String>> serTypeList(String user_id,String isRealtimeQuery)
	{
		Map<String,String> map = dao.serList(user_id,isRealtimeQuery);
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> rmap = null;
		for (String key : map.keySet())
		{
			rmap = new HashMap<String, String>();
			rmap.put("serv_type_id", key);
			rmap.put("serv_type_name", map.get(key));
			list.add(rmap);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Map> getConfigInfo(String deviceId, String servTypeId,
			String servstauts, String wanType,String isRealtimeQuery,
			String serUsername) {
		if (true == StringUtil.IsEmpty(servTypeId)
				|| true == StringUtil.IsEmpty(servstauts)
				|| true == StringUtil.IsEmpty(wanType)
				|| true == StringUtil.IsEmpty(serUsername)) {
			logger.debug("参数为空！");
			return null;
		}
		String serviceId = dao.getServiceId(servTypeId, servstauts, wanType,isRealtimeQuery);
		if (true == StringUtil.IsEmpty(serviceId)) {
			logger.debug("业务不存在！");
			return null;
		}
		return dao.getConfigInfo(deviceId, serviceId, serUsername);
	}
	/**
	 * 查询宽带上网工单详情信息
	 * 
	 * @return
	 */
	public List<Map> getInternetBssSheet(String cityId, String userId,
			String servTypeId, String serUsername, String gw_type,
			String isRealtimeQuery) {
		if (StringUtil.IsEmpty(userId)) {
			logger.error("用户USERID没传过来！");
			return null;
		}
		return dao.getInternetBssSheet(gw_type, userId, servTypeId,
				serUsername, cityId, isRealtimeQuery);
	}
	/**
	 * 查询工单详细信息
	 * 
	 * @author wangsenbo
	 * @date Sep 15, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getBssSheet(String cityId, String userId,
			String servTypeId, String serUsername, String gw_type,
			String isRealtimeQuery) {
		logger.debug("getBssSheet({},{})", new Object[] { cityId, userId });
		if (StringUtil.IsEmpty(userId)) {
			logger.debug("用户USERID没传过来！");
			return null;
		}
		List<Map> list;
		if ("10".equals(servTypeId) || "11".equals(servTypeId)
				|| "16".equals(servTypeId)) {
			list = dao.getBssSheetByServtype(cityId, userId, servTypeId,
					serUsername, gw_type, isRealtimeQuery);
		} else {
			list = dao.getBssSheetVoIP(cityId, userId, gw_type, isRealtimeQuery);
		}
		if (list.size() > 0) {
			// String username =
			// StringUtil.getStringValue(list.get(0).get("username"));
			// bssParaList = dao.getBssPara(username,servTypeId);
		} else {
			logger.debug("没有该用户的工单信息");
		}
		return list;
	}

	public Map<String, String> getBssIssuedConfigDetail(String user_id,
			String serv_type_id, String isRealtimeQuery, String serUsername) {
		Map<String, String> map = new HashMap<String, String>();
		// 上网
		if ("10".equals(serv_type_id)) {
			map = dao.getNetIptvIssuedConfig(user_id, serv_type_id,
					isRealtimeQuery, serUsername);
			map.put("mode_type", "10");
		}
		// IPTV
		if ("11".equals(serv_type_id)) {
			map = dao.getNetIptvIssuedConfig(user_id, serv_type_id,
					isRealtimeQuery, serUsername);
			map.put("mode_type", "11");
		}
		// voip
		if ("14".equals(serv_type_id)) {
			map = dao.getVoipIssuedConfig(user_id, serv_type_id,
					isRealtimeQuery, serUsername);
			map.put("mode_type", "14");
		}
		return map;
	}
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
	 * ITMS解绑
	 * 
	 * @param userId
	 * @param deviceId
	 * @param dealstaff 
	 * @param string 
	 * @return 操作信息提示
	 */
	public String itmsRelease(String userId, String username, String cityId,String deviceId, String dealstaff,int userline) {
		logger.warn("itmsRelease(userId:{};username{};cityId:{};deviceId:{};dealstaff:{},userline:{})",
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
		logger.warn("corba={}",corba);
		ResultInfo rs = corba.release(arr);
		logger.warn("release:rs1={}",rs);
		if(rs == null)
		{
			msg = "解绑失败，系统内部错误";
			logger.warn("release:rs2{}=",rs);
		}
		else
		{
			logger.warn("release:rs3{}=",rs);
			//String status = rs.status;
			// 成功
			if(rs.resultId[0].equals("1"))
			{
				logger.warn("release:rs4={}",rs);
				msg = "解绑" + Global.G_ResourceBind_statusCode.get(Integer.parseInt(rs.resultId[0]));
				return msg;
			}
			// 失败
			else
			{
				logger.warn("release:rs5={}",rs);
				// 获取相关错误码
				msg = "解绑失败，" + Global.G_ResourceBind_resultCode.get(Integer.parseInt(rs.resultId[0]));
			}
		}
		logger.warn("release:rs6={}",rs);
		return msg;
	}
	/**
	 * 是否支持awifi开通
	 * @param deviceId
	 * @return
	 */
	public String isAwifi(String deviceId) {
		logger.debug("WirelessbusinessCtrServ-->isAwifi({})",
				new Object[] { deviceId });
		String strategy = dao.isAwifi(deviceId);
		return strategy;
	}

	public String doConfig(long userId, List<String> list, String gwType,
			String flag, String strategy_type, String vlanIdMark, String ssid,
			String wireless_port, String buss_level, String channel,
			String awifi_type) {
		logger.debug("doConfig({})", list.toString());
		String res = "0";
		String serviceId = "";
		long time = new DateTimeUtil().getLongTime(); // 入表时间，同时为任务id
		if (flag.equals("1")) {
			serviceId = OPEN_WIRELESS;
		} else if (flag.equals("0")) {
			serviceId = CLOSE_WIRELESS;
		} else {
			res = "-1";
		}
		// 入数据库
		try {

			dao.doConfig(userId, list, gwType, serviceId, strategy_type,
					vlanIdMark, ssid, time, wireless_port, buss_level, channel,
					awifi_type);
			res = "1";
		} catch (Exception e) {
			logger.warn("更新表失败");
			res = "-1";
		}
		String[] array = new String[list.size()];

		if ("1".equals(res)) {
			if (1 == CreateObjectFactory.createPreProcess(gwType).processDeviceStrategy(
					list.toArray(array), serviceId,
					new String[] { StringUtil.getStringValue(time) })) {
				logger.warn("调用后台预读模块成功");
				res = "1";
			} else {
				logger.warn("调用后台预读模块失败");
				res = "-4";
			}
		}
		return res;
	}
	public String checkLoid(String deviceId, String gwType) {
		Map<String, String> map = dao.checkUserExsists(deviceId, gwType);
		String specId = "";
		String msn = "1,成功";
		if (null != map) {
			specId = StringUtil.getStringValue(map.get("spec_id"));
			// 查询终端的规格
			String specName = dao.getSpecName(specId);
			if ("E8CP11".equals(specName) || "E8CP12".equals(specName)
					|| "E8CG12".equals(specName) || "E8CP02".equals(specName)) {
				logger.warn("终端【" + deviceId + "】" + "不支持无线端口");
				msn = "0,用户规格不支持无线开通";
			}
		}
		return msn;
	}
	public String isHaveStrategy(String deviceId) {
		logger.debug("WirelessbusinessCtrServ-->isHaveStrategy({})",
				new Object[] { deviceId });
		String strategy = dao.isHaveStrategy(deviceId);
		return strategy;
	}

	public String sendSSIDSheet(String netUsername,String cityId, String gwType,String netNum) {
		logger.debug("sendSSIDSheet({},{})",netUsername,gwType);

		DateTimeUtil dt = new DateTimeUtil();
		String loid = dao.getLoid(netUsername, gwType);
		StringBuffer bssSheet = new StringBuffer("");
		bssSheet.append("6").append("|||");
		bssSheet.append("50").append("|||");
		bssSheet.append("1").append("|||"); //开户
		bssSheet.append(dt.getYYYYMMDDHHMMSS()).append("|||");
		bssSheet.append(cityId).append("|||");
		bssSheet.append(netUsername).append("|||");
		bssSheet.append(netNum).append("|||");
		bssSheet.append(loid);
		bssSheet.append("LINKAGE");

		return this.sendSheet(bssSheet.toString(),gwType);
	}
	/**
	 * 向工单接口发送模拟的BSS工单。正常返回工单接口的回单结果，如果过程中出现问题返回null
	 * 
	 * @return String 回单信息
	 */
	public String sendSheet(String bssSheet, String gw_type)
	{
		logger.debug("sendSheet({})", bssSheet);
		if (StringUtil.IsEmpty(bssSheet))
		{
			logger.warn("sendSheet is null");
			return null;
		}

		if ("1".equals(gw_type)) {
			return SocketUtil.sendStrMesg(Global.G_ITMS_Sheet_Server,
					Global.G_ITMS_Sheet_Port, bssSheet + "\n");
		} else {
			return SocketUtil.sendStrMesg(Global.G_BBMS_Sheet_Server,
					Global.G_BBMS_Sheet_Port, bssSheet + "\n");
		}
	}
	public String sendCloseSSIDSheet(String netUsername,String cityId, String gwType) {
		logger.debug("sendCloseSSIDSheet({},{})",netUsername,gwType);

		DateTimeUtil dt = new DateTimeUtil();
		StringBuffer bssSheet = new StringBuffer("");
		bssSheet.append("6").append("|||");
		bssSheet.append("50").append("|||");
		bssSheet.append("3").append("|||");   //销户
		bssSheet.append(dt.getYYYYMMDDHHMMSS()).append("|||");
		bssSheet.append(cityId).append("|||");
		bssSheet.append(netUsername);
		bssSheet.append("LINKAGE");

		return this.sendSheet(bssSheet.toString(),gwType);
	}
	/**
	 * 各个返回值 
	 * 1该业务用户存在
	 * 0该用户不存在                
	 * -1该业务用户不存在,但是用户存在
	 * -2用户未绑定设备
	 */
	@SuppressWarnings("unchecked")
	public String getServUserInfo(String username,String gwType) {
		logger.debug("getServUserInfo({},{})",username,gwType);
		String flag ="";
		String userId ="";
		String deviceId ="";
		Map map = null;
		List list =   dao.isExists(username,gwType);
		if(null!=list){
			if(list.size()==1){
				flag = "1";
				map = (Map)list.get(0);
				userId  = StringUtil.getStringValue(map.get("user_id"));
				deviceId = StringUtil.getStringValue(map.get("device_id"));
				if(StringUtil.IsEmpty(userId)){
					flag = "-1";
				}
				if(StringUtil.IsEmpty(deviceId)){
					flag = "-2";
				}
			}else if(list.size()>1){
				flag = "2";
			}else{
				flag =  "0";
			}
		}
		return flag;
	}
	public List<Map> getBssParaList() {
		return bssParaList;
	}
	public Map<String, String> querySoftVersion(String device_id)
	{
		return dao.querySoftVersion(device_id);
	}

	public FaultTreadtMentDAO getDao()
	{
		return dao;
	}

	public void setDao(FaultTreadtMentDAO dao)
	{
		this.dao = dao;
	}



	public Map<String, String> getCityMap()
	{
		return cityMap;
	}

	public void setCityMap(Map<String, String> cityMap)
	{
		this.cityMap = cityMap;
	}
	public void setBssParaList(List<Map> bssParaList)
	{
		this.bssParaList = bssParaList;
	}
	public List<Map> queryDevice(String deviceinfo, String nameType,String gw_type,String cityId)
	{
		List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
		List<String> citylist1 = CityDAO.getAllPcityIdByCityId(cityId);
		citylist.addAll(citylist1);
		List<Map> list = dao.queryDevice(deviceinfo, nameType,gw_type);
		List<Map> deviceList = new ArrayList<Map>();
		cityMap = CityDAO.getCityIdCityNameMap();
		for (Map map : list)
		{
			String city_id = StringUtil.getStringValue(map.get("city_id"));
			String city_name = StringUtil.getStringValue(cityMap.get(city_id));
			if (false == StringUtil.IsEmpty(city_name))
			{
				map.put("city_name", city_name);
			}
			else
			{
				map.put("city_name", "");
			}
			String fillIP = null;
			String IpCityId = null;
			String ip = StringUtil.getStringValue(map.get("loopback_ip"));
			if (ip != null)
			{
				fillIP = getFillIP(ip);
				if (fillIP != null)
				{
					IpCityId = dao.getCityIdByIP(fillIP);
					String devbendiCityId = CityDAO.getLocationCityIdByCityId(city_id);
					if ("0000".equals(IpCityId))
					{
						map.put("flag", 3);
					}
					else
					{
						String IPbendiCityId = CityDAO.getLocationCityIdByCityId(IpCityId);
						if (IPbendiCityId.equals(devbendiCityId))
						{
							map.put("flag", 0);
						}
						else
						{
							if ("00".equals(IPbendiCityId))
							{
								map.put("flag", 0);
							}
							else
							{
								map.put("flag", 1);
							}
						}
						map.put("IpCity_id", IPbendiCityId);
					}
				}
				else
				{
					map.put("IpCityId", "");
					map.put("flag", 2);
				}
			}
			else
			{
				logger.error("设备没有IP");
				map.put("IpCityId", "");
				map.put("flag", 2);
			}
			Boolean bool = false;
			map.put("manage", "0");
			// 设备属地被管理
			for (String string : citylist)
			{
				if (string.equals(city_id))
				{
					bool = true;
				}
			}
			if (bool)
			{
				map.put("manage", "1");
			}
			else
			{
				bool = false;
				for (String string : citylist)
				{
					if (string.equals(IpCityId))
					{
						bool = true;
					}
				}
				if (bool)
				{
					map.put("manage", "1");
				}
			}
			deviceList.add(map);
		}
		return deviceList;
	}
	/**
	 * 获取全IP,如果参数为192.168.1.1 返回192168001001
	 * 
	 * @param ip
	 * @return
	 */
	public String getFillIP(String ip)
	{
		logger.debug("getFillIP({})", ip);
		String fillIP = ip;
		String[] ipArray = new String[4];
		ipArray = ip.split("\\.");
		if (ipArray.length != 4)
		{
			return null;
		}
		for (int i = 0; i < 4; i++)
		{
			if (ipArray[i].length() == 1)
			{
				ipArray[i] = "00" + ipArray[i];
			}
			else if (ipArray[i].length() == 2)
			{
				ipArray[i] = "0" + ipArray[i];
			}
		}
		fillIP = ipArray[0] + ipArray[1] + ipArray[2] + ipArray[3];
		return fillIP;
	}

}
