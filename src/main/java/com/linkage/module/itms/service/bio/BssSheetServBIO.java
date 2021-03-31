package com.linkage.module.itms.service.bio;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import PreProcess.UserInfo;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.Base64;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.PreProcessInterface;
import com.linkage.module.gwms.util.corba.obj.PreServInfoOBJ;
import com.linkage.module.itms.service.dao.BssSheetServDAO;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BssSheetServBIO {

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(BssSheetServBIO.class);
	private BssSheetServDAO dao;
	private int maxPage_splitPage;
	private List<Map> bssParaList;
	private int total;
	

	/**
	 * 查询业务信息
	 * 
	 * @author wangsenbo
	 * @date Sep 13, 2010
	 * @param usernameType
	 *            用户名类型 "1" 逻辑SN, "2" 宽带号码, "3" IPTV号码, "4" VoIP认证号码, "5"
	 *            VoIP电话号码
	 * @param servstauts
	 *            业务类型 "10" 上网业务, "11" IPTV, "14" VoIP
	 * @param num_splitPage
	 * @param curPage_splitPage
	 * @return List<Map>
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getBssSheetServInfo(String cityId, String startOpenDate1,
			String endOpenDate1, String usernameType, String username,
			String servTypeId, String openstatus, String devicetype,
			int curPage_splitPage, int num_splitPage, String gw_type,
			String voipProtocalType, String spec_id, String cust_type_id,
			String user_type_id, String isRealtimeQuery, String wanProtocalType,String deviceVersionType) {
		logger.debug("getBssSheetServInfo({},{},{},{},{},{},{})", new Object[] {
				cityId, startOpenDate1, endOpenDate1, usernameType, username,
				servTypeId, openstatus });
		List<Map> userIdList = null;
		if (!StringUtil.IsEmpty(username)) {
			if ("1".equals(usernameType)) {
				userIdList = dao
						.getUserBySN(username, gw_type, isRealtimeQuery);
			} else if ("2".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "10", gw_type,
						isRealtimeQuery);
			} else if ("3".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "11", gw_type,
						isRealtimeQuery);
			} else if ("4".equals(usernameType)) {
				userIdList = dao.getUserByVoip(username, null, gw_type,
						isRealtimeQuery);
			} else if ("5".equals(usernameType)) {
				userIdList = dao.getUserByVoip(null, username, gw_type,
						isRealtimeQuery);
			} else if ("6".equals(usernameType)) {
				userIdList = dao.getUserByVoipEID(username, gw_type,
						isRealtimeQuery);
			} else if ("7".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "20", gw_type,
						isRealtimeQuery);
			} else if ("8".equals(usernameType)) {//VPN
				userIdList = dao.getUserByServ(username, "38", gw_type,
						isRealtimeQuery);
			} else if ("9".equals(usernameType)) {//随选入云
				userIdList = dao.getUserByServ(username, "33", gw_type,
						isRealtimeQuery);
			} else if ("10".equals(usernameType)) {//溯源净网
				userIdList = dao.getUserByServ(username, "32", gw_type,
						isRealtimeQuery);
			}else if ("11".equals(usernameType)){
				// 山西联通新增设备序列号查询业务条件
				userIdList = dao.getUserByDeviceSN(username,gw_type,isRealtimeQuery);
			}else if ("12".equals(usernameType)){//云网超宽带
				userIdList = dao.getUserByServ(username, "47", gw_type,
						isRealtimeQuery);
			}
			else {
				logger.debug("用户名类型不存在！");
				return null;
			}
			if (userIdList == null || userIdList.size() == 0) {
				logger.debug("用户不存在！");
				return null;
			}
		}
		String userId = null;
		if (userIdList != null && userIdList.size() > 0) {
			userId = getManyUserId(userIdList);
		}
		List<Map> list = dao.getBssSheetServInfo(cityId, startOpenDate1,
				endOpenDate1, userId, servTypeId, openstatus, devicetype,
				curPage_splitPage, num_splitPage, gw_type, voipProtocalType,
				spec_id, cust_type_id, user_type_id, isRealtimeQuery, wanProtocalType,deviceVersionType);
		// int total = dao.countBssSheetServInfo(cityId, startOpenDate1,
		// endOpenDate1,
		// userId, servTypeId, openstatus, devicetype, gw_type,
		// voipProtocalType,
		// spec_id, cust_type_id);
		// maxPage_splitPage = (total + num_splitPage - 1) / num_splitPage;
		if (Global.SXDX.equals(Global.instAreaShortName))
		{
			List<Map> reslist  = new ArrayList<Map>();
			for (int i = 0; i < list.size(); i++)
			{
				Map map = list.get(i);
				map.put("result_desc","");
				if (map != null)
				{
					
					String open_status = map.get("open_status") == null ? ""
							: map.get("open_status").toString();
					String deviceId = map.get("device_id") == null ? ""
							: map.get("device_id").toString();
					String wanType = map.get("wan_type") == null ? ""
							: map.get("wan_type").toString();
					String servstauts = map.get("serv_status") == null ? ""
							: map.get("serv_status").toString();
					String serv_type_id = map.get("serv_type_id") == null ? ""
							: map.get("serv_type_id").toString();
					String serUsername = map.get("serUsername") == null ? ""
							: map.get("serUsername").toString();
					if("-1".equals(open_status)) {
					logger.info("getConfigInfo({},{},{},{},{})", new Object[] { deviceId,
							serv_type_id, servstauts, wanType, "" });
					if (true == StringUtil.IsEmpty(serv_type_id)
							|| true == StringUtil.IsEmpty(servstauts)
							|| true == StringUtil.IsEmpty(wanType)){
						logger.info("参数为空！");
						continue;
					}
					String serviceId = dao.getServiceId(serv_type_id, servstauts, wanType,
							isRealtimeQuery);
					if (true == StringUtil.IsEmpty(serviceId)) {
						logger.info("业务不存在！");
						continue;
					}
					List<Map> configList = dao.getConfigInfo(gw_type,deviceId, serviceId, isRealtimeQuery,serUsername);
					
					if (configList.size() > 0)
					{
						Map configMap = configList.get(0);
						map.put("result_desc", configMap.get("result_desc"));
						map.put("fault_reason", configMap.get("fault_reason"));
						map.put("solutions", configMap.get("solutions"));
						map.put("fault_desc", configMap.get("fault_desc"));
					}
					}
				}
				reslist.add(i, map);
				
			}
			
			list.clear();
			list.addAll(reslist);
		
		}
		int total = this.getBssSheetServInfoCount(cityId, startOpenDate1, 
				endOpenDate1, usernameType, username, servTypeId, openstatus,
				devicetype, gw_type, voipProtocalType, spec_id, cust_type_id,
				user_type_id, isRealtimeQuery, wanProtocalType,deviceVersionType);
		maxPage_splitPage = (total + num_splitPage - 1) / num_splitPage;	
		this.total = total;		
		return list;	
	}

	/**
	 * 查询业务信息
	 * 
	 * @author wangsenbo
	 * @date Sep 13, 2010
	 * @param usernameType
	 *            用户名类型 "1" 逻辑SN, "2" 宽带号码, "3" IPTV号码, "4" VoIP认证号码, "5"
	 *            VoIP电话号码
	 * @param servstauts
	 *            业务类型 "10" 上网业务, "11" IPTV, "14" VoIP
	 * @param num_splitPage
	 * @param curPage_splitPage
	 * @return List<Map>
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getBssSheetServInfoExcel(String cityId,
			String startOpenDate1, String endOpenDate1, String usernameType,
			String username, String servTypeId, String openstatus,
			String devicetype, String gw_type, String voipProtocalType,
			String spec_id, String cust_type_id, String user_type_id,
			String isRealtimeQuery, String wanProtocalType, String deviceVersionType) {
		logger.debug("getBssSheetServInfo({},{},{},{},{},{},{})", new Object[] {
				cityId, startOpenDate1, endOpenDate1, usernameType, username,
				servTypeId, openstatus });
		List<Map> userIdList = null;
		if (!StringUtil.IsEmpty(username)) {
			if ("1".equals(usernameType)) {
				userIdList = dao
						.getUserBySN(username, gw_type, isRealtimeQuery);
			} else if ("2".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "10", gw_type,
						isRealtimeQuery);
			} else if ("3".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "11", gw_type,
						isRealtimeQuery);
			} else if ("4".equals(usernameType)) {
				userIdList = dao.getUserByVoip(username, null, gw_type,
						isRealtimeQuery);
			} else if ("5".equals(usernameType)) {
				userIdList = dao.getUserByVoip(null, username, gw_type,
						isRealtimeQuery);
			}else if ("12".equals(usernameType)){//云网超宽带
				userIdList = dao.getUserByServ(username, "47", gw_type,
						isRealtimeQuery);
			} else {
				logger.debug("用户名类型不存在！");
				return null;
			}
			if (userIdList == null || userIdList.size() == 0) {
				logger.debug("用户不存在！");
				return null;
			}
		}
		String userId = null;
		if (userIdList != null && userIdList.size() > 0) {
			userId = getManyUserId(userIdList);
		}
		List<Map> list = dao.getBssSheetServInfoExcel(cityId, startOpenDate1,
				endOpenDate1, userId, servTypeId, openstatus, devicetype,
				gw_type, voipProtocalType, spec_id, cust_type_id, user_type_id,
				isRealtimeQuery, wanProtocalType,deviceVersionType);
		return list;
	}

	@SuppressWarnings("unchecked")
	public int getBssSheetServInfoCount(String cityId, String startOpenDate1,
			String endOpenDate1, String usernameType, String username,
			String servTypeId, String openstatus, String devicetype,
			String gw_type, String voipProtocalType, String spec_id,
			String cust_type_id, String user_type_id, String isRealtimeQuery, String wanProtocalType,String deviceVersionType) {
		List<Map> userIdList = null;
		if (!StringUtil.IsEmpty(username)) {
			if ("1".equals(usernameType)) {
				userIdList = dao
						.getUserBySN(username, gw_type, isRealtimeQuery);
			} else if ("2".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "10", gw_type,
						isRealtimeQuery);
			} else if ("3".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "11", gw_type,
						isRealtimeQuery);
			} else if ("4".equals(usernameType)) {
				userIdList = dao.getUserByVoip(username, null, gw_type,
						isRealtimeQuery);
			} else if ("5".equals(usernameType)) {
				userIdList = dao.getUserByVoip(null, username, gw_type,
						isRealtimeQuery);
			}  else if ("7".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "20", gw_type,
						isRealtimeQuery);
			}else if ("12".equals(usernameType)){//云网超宽带
				userIdList = dao.getUserByServ(username, "47", gw_type,
						isRealtimeQuery);
			}
			else {
				logger.debug("用户名类型不存在！");
				return 0;
			}
			if (userIdList == null || userIdList.size() == 0) {
				logger.debug("用户不存在！");
				return 0;
			}
		}
		String userId = null;
		if (userIdList != null && userIdList.size() > 0) {
			userId = getManyUserId(userIdList);
		}
		return dao.countBssSheetServInfo(cityId, startOpenDate1, endOpenDate1,
				userId, servTypeId, openstatus, devicetype, gw_type,
				voipProtocalType, spec_id, cust_type_id, user_type_id,
				isRealtimeQuery, wanProtocalType,deviceVersionType);
	}

	public Map<String, String> getBssIssuedConfigDetail(String gw_type,String user_id,
			String serv_type_id, String isRealtimeQuery, String serUsername) {
		Map<String, String> map = new HashMap<String, String>();
		// 上网
		if ("10".equals(serv_type_id)) {
			map = dao.getNetIptvIssuedConfig(gw_type,user_id, serv_type_id,
					isRealtimeQuery, serUsername);
			map.put("mode_type", "10");
		}
		// IPTV
		if ("11".equals(serv_type_id)) {
			map = dao.getNetIptvIssuedConfig(gw_type,user_id, serv_type_id,
					isRealtimeQuery, serUsername);
			map.put("mode_type", "11");
		}
		// voip
		if ("14".equals(serv_type_id)) {
			map = dao.getVoipIssuedConfig(gw_type,user_id, serv_type_id,
					isRealtimeQuery, serUsername);
			map.put("mode_type", "14");
		}
		// 智能音箱
		if ("15".equals(serv_type_id)) {
			map = dao.getVoipIssuedConfig(gw_type,user_id, serv_type_id,
					isRealtimeQuery, serUsername);
			map.put("mode_type", "15");
		}
		return map;
	}

	/**
	 * @return the dao
	 */
	public BssSheetServDAO getDao() {
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(BssSheetServDAO dao) {
		this.dao = dao;
	}

	/**
	 * @return the maxPage_splitPage
	 */
	public int getMaxPage_splitPage() {
		return maxPage_splitPage;
	}

	/**
	 * @param maxPage_splitPage
	 *            the maxPage_splitPage to set
	 */
	public void setMaxPage_splitPage(int maxPage_splitPage) {
		this.maxPage_splitPage = maxPage_splitPage;
	}

	/**
	 * 查询配置信息
	 * 
	 * @author wangsenbo
	 * @date Sep 13, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getConfigInfo(String gw_type,String deviceId, String servTypeId,
			String servstauts, String wanType, String isRealtimeQuery,
			String serUsername) {
		logger.debug("getConfigInfo({},{},{},{},{})", new Object[] { deviceId,
				servTypeId, servstauts, wanType, serUsername });
		if (true == StringUtil.IsEmpty(servTypeId)
				|| true == StringUtil.IsEmpty(servstauts)
				|| true == StringUtil.IsEmpty(wanType)){
			logger.debug("参数为空！");
			return null;
		}
		String serviceId = dao.getServiceId(servTypeId, servstauts, wanType,
				isRealtimeQuery);
		if (true == StringUtil.IsEmpty(serviceId)) {
			logger.debug("业务不存在！");
			return null;
		}
		List list = dao.getConfigInfo(gw_type,deviceId, serviceId, isRealtimeQuery,
				serUsername);
		return list;
	}

	/**
	 * 查询配置信息 软件升级信息
	 * 
	 * @author zhangsb
	 * @date Sep 13, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getSoftUpInfo(String gw_type,String deviceId, String servTypeId,
			String servstauts, String wanType, String isRealtimeQuery,
			String serUsername) {
		logger.debug("getSoftUpInfogetSoftUpInfo({},{},{},{})", new Object[] {
				deviceId, servTypeId, servstauts, wanType });
		if (true == StringUtil.IsEmpty(servTypeId)
				|| true == StringUtil.IsEmpty(servstauts)
				|| true == StringUtil.IsEmpty(serUsername)
				|| true == StringUtil.IsEmpty(wanType)) {
			logger.debug("参数为空！");
			return null;
		}
		String serviceId = dao.getServiceId(servTypeId, servstauts, wanType,
				isRealtimeQuery);
		if (true == StringUtil.IsEmpty(serviceId)) {
			logger.debug("业务不存在！");
			return null;
		}
		// 1401 为VOIP开通业务
		if ("1401".equals(serviceId)) {
			List list = dao.getSoftUpInfo(gw_type,deviceId, serviceId, isRealtimeQuery,
					serUsername);
			return list;
		}
		return null;
	}

	/**
	 * 查询配置详细信息
	 * 
	 * @author wangsenbo
	 * @date Sep 14, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getConfigDetail(String gw_type,String strategyId, String isRealtimeQuery) {
		return dao.getConfigDetail(gw_type,strategyId, isRealtimeQuery);
	}
	
	public String getSheetId(String strategyId) {
		return dao.getSheetId(strategyId);
	}
	
	public String getDeviceId(String devSN) {
		return dao.getDeviceId(devSN);
	}
	
	public String getLogSheetId(String strategyId) {
		return dao.getLogSheetId(strategyId);
	}
	
	public String getSheetParaById(String strategyId) {
		return dao.getSheetParaById(strategyId);
	}
	
	public String getSheetParaLogById(String strategyId) {
		return dao.getSheetParaLogById(strategyId);
	}
	
	public ArrayList<HashMap<String, String>> doServStatus(String gw_type,String strategyId, String isRealtimeQuery, String sheetId) {
		ArrayList<HashMap<String, String>> valueList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> valueMap = null;
		ArrayList<HashMap<String, String>> cmdList = dao.getSheetCmdList(sheetId);
		if (null != cmdList && cmdList.size() > 0) 
		{
			for (int i = 0; i < cmdList.size(); i++) 
			{
				
				int rpc_type_id = StringUtil.getIntValue(cmdList, i, "rpc_id");
				String rpcOrder = StringUtil.getStringValue(cmdList, i,	"rpc_order");
				switch (rpc_type_id) 
				{
					case 5:// SetParameterValues
						ArrayList<HashMap<String, String>> paraList5 = dao.getSheetParaList(sheetId, rpcOrder);
						if (null == paraList5 || paraList5.size() < 4 || (paraList5.size() % 2 > 0))
						{
							logger.warn("[{}]SetParameterValues命令不正确！");
							continue;
						}
						for (int j = 1; j < (paraList5.size() - 2); j++) 
						{
							valueMap = new HashMap<String, String>();
							valueMap.put("name", StringUtil.getStringValue(paraList5, j, "def_value"));
							valueMap.put("value", StringUtil.getStringValue(paraList5, ++j, "def_value"));
							valueMap.put("type", "设置参数");
							valueMap.put("result", "成功");
							valueList.add(valueMap);
						}
						paraList5 = null;
						break;
					case 15:// AddObject
						ArrayList<HashMap<String, String>> paraList15 = dao.getSheetParaList(sheetId, rpcOrder);
						if (null == paraList15 || paraList15.size() != 2) {
							logger.warn("[{}]AddObject命令不正确！");
							continue;
						}
						valueMap = new HashMap<String, String>();
						valueMap.put("name", StringUtil.getStringValue(paraList15, 0, "def_value"));
						valueMap.put("value", "");
						valueMap.put("type", "添加实例");
						valueMap.put("result", "成功");
						valueList.add(valueMap);
						paraList15 = null;
						break;
				}
				
			}
		}
		return valueList;
	}
	
	public HashMap<String, String> strtoxml(String faultdesc)
	{
		//替换掉无法解析的xml
		String faultdescFilter = faultdesc.replaceAll("SOAP-ENV:Fault", "root").replaceAll("cwmp:Fault", "content");
		SAXReader reader = new SAXReader();
		Document document = null;
		HashMap<String, String> faultmap = new HashMap<String, String>();
		try
		{
			document = reader.read(new StringReader(faultdescFilter));
			Element root = document.getRootElement();
			Element param = root.element("detail");
			Element cwmp = param.element("content");
			List<Element> faultList = cwmp.elements("SetParameterValuesFault");
			if(faultList != null && !faultList.isEmpty())
			{
				int resultId = StringUtil.getIntegerValue(faultList.get(0).elementTextTrim("FaultCode"));
				faultmap.put(faultList.get(0).elementTextTrim("ParameterName"), 
						Global.G_Fault_Map.get(resultId).getFaultDesc());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		logger.warn("faultmap is " + faultmap);
		return faultmap;
	}
	
	public String doServFaultStatus(String sheetId) {
		return dao.getFaultDesc(sheetId);
	}

	/**
	 * 密码解密
	 * 
	 * @param configXml
	 *            用户请求密码密文
	 * @return 0：密码为空 -1：密码解码失败，否则返回正确明文
	 */
	public String decodePassword(String configXml) {
		try {
			if (StringUtil.IsEmpty(configXml)) {
				return "0";
			}
			return Base64.decode(configXml);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "-1";
	}

	/**
	 * 查询配置历史信息
	 * 
	 * @author wangsenbo
	 * @date Sep 14, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getConfigLogInfo(String gw_type,String deviceId, String servTypeId,
			String servstauts, String wanType, String isRealtimeQuery) {
		logger.debug("getConfigLogInfo({},{},{},{})", new Object[] { deviceId,
				servTypeId, servstauts, wanType });
		if (true == StringUtil.IsEmpty(servTypeId)
				|| true == StringUtil.IsEmpty(servstauts)
				|| true == StringUtil.IsEmpty(wanType)) {
			logger.debug("参数为空！");
			return null;
		}
		String serviceId = dao.getServiceId(servTypeId, servstauts, wanType,
				isRealtimeQuery);
		if (true == StringUtil.IsEmpty(serviceId)) {
			logger.debug("业务不存在！");
			return null;
		}
		List list = dao.getConfigLogInfo(gw_type,deviceId, serviceId, isRealtimeQuery);
		return list;
	}

	/**
	 * 查询配置历史详细信息
	 * 
	 * @author wangsenbo
	 * @date Sep 13, 2010
	 * @param
	 * @return String
	 */
	public List<Map> getConfigLogDetail(String gw_type,String strategyId,
			String isRealtimeQuery) {
		List<Map> configLogDetailList = dao.getConfigLogDetail(gw_type,strategyId, isRealtimeQuery);
		// 2020/10/14 江西页面密码脱敏
		if(Global.JXDX.equals(Global.instAreaShortName) 
				&& configLogDetailList != null && !configLogDetailList.isEmpty())
	    {
	    	 for(Map map: configLogDetailList){
	    		 String sheet_para=StringUtil.getStringValue(map,"sheet_para");
	    		 
	    		 if (sheet_para.indexOf("Password") > 0 && sheet_para.indexOf("/Password") > 0)
	    			{
	    				String password = sheet_para.substring(
	    						sheet_para.indexOf("Password")+12, sheet_para.indexOf("/Password")-4);
	    					sheet_para = sheet_para.replace(password, "******");
	    				map.put("sheet_para",sheet_para);
	    			}
	    	    }
	    }
		return configLogDetailList;
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
	 * 将查询的操作人、ip地址、时间和查看的用户账号入表
	 */
public String insertA8log(String voip_passwd,String voip_username,String userip,UserRes curUser)
{
	int a= dao.insertA8log(voip_passwd, voip_username, userip, curUser);
	if(a>0)
	{
		return "1";
	}
	else
	{
		return "-1";
	}
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
			String isRealtimeQuery,String realBindPort) {
		logger.debug("getBssSheet({},{})", new Object[] { cityId, userId });
		if (StringUtil.IsEmpty(userId)) {
			logger.debug("用户USERID没传过来！");
			return null;
		}
		List<Map> list;//随选入云:33,vpn:19 自适应wifi:51
		if ("10".equals(servTypeId) || "11".equals(servTypeId) || "33".equals(servTypeId) || "38".equals(servTypeId)
				|| "16".equals(servTypeId) || "20".equals(servTypeId) || "15".equals(servTypeId) || "32".equals(servTypeId)
				|| "51".equals(servTypeId) || "40".equals(servTypeId)) {
			list = dao.getBssSheetByServtype(cityId, userId, servTypeId,
					serUsername, gw_type, isRealtimeQuery,realBindPort);
		}else if ("47".equals(servTypeId)) {
			list = dao.getCloudNetBssSheet(gw_type, userId, servTypeId, serUsername, cityId, isRealtimeQuery);
		} else {
			list = dao
					.getBssSheetVoIP(cityId, userId, gw_type, isRealtimeQuery);
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
	
	public List<Map> getBssSheet(String cityId, String userId,
			String servTypeId, String serUsername, String gw_type,
			String isRealtimeQuery,String realBindPort,String vlanid) {
		logger.debug("getBssSheet({},{})", new Object[] { cityId, userId });
		if (StringUtil.IsEmpty(userId)) {
			logger.debug("用户USERID没传过来！");
			return null;
		}
		List<Map> list;
		list = dao.getBssSheetByServtypeVlan(cityId, userId, servTypeId,
				serUsername, gw_type, isRealtimeQuery,realBindPort,vlanid);
		
		if (list.size() <= 0) {
			logger.debug("没有该用户的工单信息");
		}
		return list;
	}

	public List<Map> getBssParaList() {
		return bssParaList;
	}

	public void setBssParaList(List<Map> bssParaList) {
		this.bssParaList = bssParaList;
	}

	/**
	 * 调用预读接口，重新激活工单 业务下发 与 重新激活 共用此方法 modify by zhangchy 2011-10-19
	 * 
	 * @author wangsenbo
	 * @date Sep 15, 2010
	 * @param
	 * @return String
	 */
	public String callPreProcess(String userId, String deviceId, String oui,
			String deviceSN, String servTypeId, String servstauts,
			String isRealtimeQuery) {
		logger.debug("callPreProcess({},{},{},{},{},{},{})", new Object[] {
				userId, deviceId, oui, deviceSN, servTypeId, servstauts });
		PreServInfoOBJ obj = new PreServInfoOBJ(userId, deviceId, oui,
				deviceSN, servTypeId, servstauts);
		obj.setGatherId("1");
		// 更新业务用户表的业务开通状态
		// add by zhangchy 2011-10-19
		dao.updateServOpenStatus(StringUtil.getLongValue(userId), new Integer(
				servTypeId).intValue(), isRealtimeQuery);
		UserInfo uinfo = CreateObjectFactory.createPreProcess().GetPPBindUserList(obj);
		UserInfo[] uinfoarray = new UserInfo[] { uinfo };
		// modify by chnejie 2012-10-25
		// 默认是调用itms的预读，在融合环境下需要判断gw_type来区分ITMS和BBMS
		// PreProcessInterface ppc = CreateObjectFactory.createPreProcess(Global.GW_TYPE_ITMS);
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

	/**
	 * 调用预读接口，重新激活工单，参数为对象集合，批量调用
	 * 
	 * @author chenjie
	 * @date 2011-3-31
	 */
	public String callPreProcessBatch(List<PreServInfoOBJ> objList) {
		logger.debug("callPreProcessBatch({})", objList.toArray());
		UserInfo[] userInfoArr = new UserInfo[objList.size()];
		for (int i = 0; i < objList.size(); i++) {
			UserInfo userInfo = CreateObjectFactory.createPreProcess().GetPPBindUserList(objList
					.get(i));
			userInfoArr[i] = userInfo;
		}
		PreProcessInterface ppc = CreateObjectFactory.createPreProcess(Global.GW_TYPE_ITMS);
		logger.warn("inter_ PreProcess (userInfoList:{})", userInfoArr);
		int ret = ppc.processServiceInterface(userInfoArr);
		logger.warn("inter_ret_ PreProcess({})", ret);
		return StringUtil.getStringValue(ret);
	}

	public List<Map> getBssPara(String username) {
		// bssParaList = dao.getBssPara2(username);
		return bssParaList;
	}

	/**
	 * 根据逻辑SN检查设备上报了没
	 * 
	 * @author wangsenbo
	 * @date Oct 26, 2010
	 * @param
	 * @return String
	 */
	public String checkdevice(String username, String isRealtimeQuery) {
		return dao.checkdevice(username, isRealtimeQuery);
	}

	@SuppressWarnings("unchecked")
	public List<Map> getBssCustomerServInfo(String cityId,
			String startOpenDate1, String endOpenDate1, String usernameType,
			String username, String devicetype, int curPage_splitPage,
			int num_splitPage, String gw_type, String isRealtimeQuery) {
		logger.debug("getBssSheetServInfo({},{},{},{},{},{},{})", new Object[] {
				cityId, startOpenDate1, endOpenDate1, usernameType, username });
		List<Map> userIdList = null;
		if (false == StringUtil.IsEmpty(username)) {
			if ("1".equals(usernameType)) {
				userIdList = dao
						.getUserBySN(username, gw_type, isRealtimeQuery);
			} else if ("2".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "10", gw_type,
						isRealtimeQuery);
			} else if ("3".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "11", gw_type,
						isRealtimeQuery);
			} else if ("4".equals(usernameType)) {
				userIdList = dao.getUserByVoip(username, null, gw_type,
						isRealtimeQuery);
			} else if ("5".equals(usernameType)) {
				userIdList = dao.getUserByVoip(null, username, gw_type,
						isRealtimeQuery);
			} else {
				logger.debug("用户名类型不存在！");
				return null;
			}
			if (userIdList == null || userIdList.size() == 0) {
				logger.debug("用户不存在！");
				return null;
			}
		}
		String userId = null;
		if (userIdList != null && userIdList.size() > 0) {
			userId = getManyUserId(userIdList);
		}
		List<Map> list = dao.getBssCustomerServInfo(cityId, startOpenDate1,
				endOpenDate1, userId, devicetype, curPage_splitPage,
				num_splitPage, isRealtimeQuery);
		maxPage_splitPage = dao.countBssCustomerServInfo(cityId,
				startOpenDate1, endOpenDate1, userId, devicetype,
				curPage_splitPage, num_splitPage, isRealtimeQuery);
		return list;
	}

	/**
	 * @excel导出
	 */
	public List<Map> getBssCustomerServInfo(String cityId,
			String startOpenDate1, String endOpenDate1, String usernameType,
			String username, String devicetype, String gw_type,
			String isRealtimeQuery) {
		logger.debug("getBssSheetServInfo({},{},{},{},{},{},{})", new Object[] {
				cityId, startOpenDate1, endOpenDate1, usernameType, username });
		List<Map> userIdList = null;
		if (false == StringUtil.IsEmpty(username)) {
			if ("1".equals(usernameType)) {
				userIdList = dao
						.getUserBySN(username, gw_type, isRealtimeQuery);
			} else if ("2".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "10", gw_type,
						isRealtimeQuery);
			} else if ("3".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "11", gw_type,
						isRealtimeQuery);
			} else if ("4".equals(usernameType)) {
				userIdList = dao.getUserByVoip(username, null, gw_type,
						isRealtimeQuery);
			} else if ("5".equals(usernameType)) {
				userIdList = dao.getUserByVoip(null, username, gw_type,
						isRealtimeQuery);
			} else {
				logger.debug("用户名类型不存在！");
				return null;
			}
			if (userIdList == null || userIdList.size() == 0) {
				logger.debug("用户不存在！");
				return null;
			}
		}
		String userId = null;
		if (userIdList == null || userIdList.size() == 0) {
		} else {
			userId = getManyUserId(userIdList);
		}
		List<Map> list = dao.getBssCustomerServInfo(cityId, startOpenDate1,
				endOpenDate1, userId, devicetype, isRealtimeQuery);
		// maxPage_splitPage = dao.countBssCustomerServInfo(cityId,
		// startOpenDate1,
		// endOpenDate1, userId, devicetype, curPage_splitPage,
		// num_splitPage);
		return list;
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

	public List<Map> getServByUser(String userId, String gw_type,
			String isRealtimeQuery) {
		logger.debug("getServByUser({})", new Object[] { userId });
		List<Map> list = dao.getServByUser(userId, gw_type, isRealtimeQuery);
		return list;
	}

	public String getSolutionInfo(String result_id) {
		logger.debug("getSolutionInfo({})", new Object[] { result_id });
		return dao.getSolutionInfo(result_id);
	}

	/**
	 * 根据loid获取工单详情 安徽联通新增bss异常单处理页面
	 * @param loid
	 * @return
	 */
	public List<Map<String,String>> getBSSInfoByLoid(String loid){
		//1、根据loid获取资料开户工单信息
		List<Map<String,String>> bssInfoMapList = dao.getBssUserInfoByLoid(loid);
		logger.warn("getBSSInfoByLoid with loid:{},bssInfoMapList:{}",loid,bssInfoMapList);
		if(bssInfoMapList == null || bssInfoMapList.size() == 0){
			logger.warn("getBSSInfoByLoid with loid not exist,loid:{}",loid);
			return new ArrayList<Map<String, String>>();
		}

		//2、组装返回
		Map<String,String> userInfoMap = bssInfoMapList.get(0);
		Map<String,String> cityMap = CityDAO.getCityIdCityNameMap();
		Map<String,String> bssdevMap = Global.G_BssDev_PortName_Map;
		Map<String,String> servTypeMap = dao.getServType("true");
		String cityName = StringUtil.getStringValue(cityMap.get(userInfoMap.get("city_id")));
		userInfoMap.put("cityName", !StringUtil.IsEmpty(cityName) ? cityName : "");
		userInfoMap.put("servTypeName",servTypeMap.get(userInfoMap.get("serv_type_id")));
		DateTimeUtil dt = new DateTimeUtil(Long.parseLong(StringUtil.getStringValue(userInfoMap.get("dealdate"))) * 1000);
		userInfoMap.put("dealDate", dt.getLongDate());
		dt = new DateTimeUtil(Long.parseLong(StringUtil.getStringValue(userInfoMap.get("opendate"))) * 1000);
		userInfoMap.put("openDate", dt.getLongDate());
		//小于2020/5/11零点前的工单可以修改开户时间；
		int limitTimeSec = 1589126400;
		if(StringUtil.getLongValue(userInfoMap.get("opendate")) < limitTimeSec){
			userInfoMap.put("isEditEnable","true");
		}else {
			userInfoMap.put("isEditEnable","false");
		}
		return bssInfoMapList;
	}

	/**
	 * 根据loid修改开户时间
	 * @param userId
	 * @return
	 */
	public int changeOpenDateByUserId(String userId, String openDateStr){
		DateTimeUtil dt = new DateTimeUtil(openDateStr);
		int result = dao.updateOpenDateByUserId(userId,dt.getLongTime());
		logger.warn("end changeOpenDateByLoid with userId:{},openDateStr:{},result:{}",userId,openDateStr,result);
		return result;
	}

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}
}
