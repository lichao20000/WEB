package com.linkage.module.itms.service.bio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import PreProcess.UserInfo;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.Base64;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.PreProcessInterface;
import com.linkage.module.gwms.util.corba.obj.PreServInfoOBJ;
import com.linkage.module.itms.service.dao.BssSheetServStandardDAO;

public class BssSheetServStandardBIO {

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(BssSheetServStandardBIO.class);
	private BssSheetServStandardDAO dao;
	private int maxPage_splitPage;
	private List<Map> bssParaList;

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
			String user_type_id) {
		logger.debug("getBssSheetServInfo({},{},{},{},{},{},{})", new Object[] {
				cityId, startOpenDate1, endOpenDate1, usernameType, username,
				servTypeId, openstatus });
		List<Map> userIdList = null;
		if (!StringUtil.IsEmpty(username)) {
			if ("1".equals(usernameType)) {
				userIdList = dao.getUserBySN(username, gw_type);
			} else if ("2".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "10", gw_type);
			} else if ("3".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "11", gw_type);
			} else if ("4".equals(usernameType)) {
				userIdList = dao.getUserByVoip(username, null, gw_type);
			} else if ("5".equals(usernameType)) {
				userIdList = dao.getUserByVoip(null, username, gw_type);
			} else if ("6".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "28", gw_type);
			} else if ("7".equals(usernameType)) {
				userIdList = dao.getUserByVoipEID(username, gw_type);
			}  else if ("8".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "33", gw_type);
			}else if ("12".equals(usernameType)){//云网超宽带
				userIdList = dao.getUserByServ(username, "47", gw_type);
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
		List<Map> list = dao.getBssSheetServInfo(cityId, startOpenDate1,
				endOpenDate1, userId, servTypeId, openstatus, devicetype,
				curPage_splitPage, num_splitPage, gw_type, voipProtocalType,
				spec_id, cust_type_id, user_type_id);
		
		int total = dao.countBssSheetServInfo(cityId, startOpenDate1,
				endOpenDate1, userId, servTypeId, openstatus, devicetype,
				gw_type, voipProtocalType, spec_id, cust_type_id, user_type_id);
		maxPage_splitPage = (total + num_splitPage - 1) / num_splitPage;
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
			String spec_id, String cust_type_id, String user_type_id) {
		logger.debug("getBssSheetServInfo({},{},{},{},{},{},{})", new Object[] {
				cityId, startOpenDate1, endOpenDate1, usernameType, username,
				servTypeId, openstatus });
		List<Map> userIdList = null;
		if (!StringUtil.IsEmpty(username)) {
			if ("1".equals(usernameType)) {
				userIdList = dao.getUserBySN(username, gw_type);
			} else if ("2".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "10", gw_type);
			} else if ("3".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "11", gw_type);
			} else if ("4".equals(usernameType)) {
				userIdList = dao.getUserByVoip(username, null, gw_type);
			} else if ("5".equals(usernameType)) {
				userIdList = dao.getUserByVoip(null, username, gw_type);
			} else if ("12".equals(usernameType)){//云网超宽带
				userIdList = dao.getUserByServ(username, "47", gw_type);
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
				gw_type, voipProtocalType, spec_id, cust_type_id, user_type_id);
		return list;
	}

	@SuppressWarnings("unchecked")
	public int getBssSheetServInfoCount(String cityId, String startOpenDate1,
			String endOpenDate1, String usernameType, String username,
			String servTypeId, String openstatus, String devicetype,
			String gw_type, String voipProtocalType, String spec_id,
			String cust_type_id, String user_type_id) {
		List<Map> userIdList = null;
		if (!StringUtil.IsEmpty(username)) {
			if ("1".equals(usernameType)) {
				userIdList = dao.getUserBySN(username, gw_type);
			} else if ("2".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "10", gw_type);
			} else if ("3".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "11", gw_type);
			} else if ("4".equals(usernameType)) {
				userIdList = dao.getUserByVoip(username, null, gw_type);
			} else if ("5".equals(usernameType)) {
				userIdList = dao.getUserByVoip(null, username, gw_type);
			} else if ("12".equals(usernameType)){//云网超宽带
				userIdList = dao.getUserByServ(username, "47", gw_type);
			}  else {
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
				voipProtocalType, spec_id, cust_type_id, user_type_id);
	}

	public Map<String, String> getBssIssuedConfigDetail(String user_id,
			String serv_type_id, String serUsername) {
		Map<String, String> map = new HashMap<String, String>();
		// 上网
		if ("10".equals(serv_type_id)) {
			map = dao
					.getNetIptvIssuedConfig(user_id, serv_type_id, serUsername);
			map.put("mode_type", "10");
		}
		// IPTV
		if ("11".equals(serv_type_id)) {
			map = dao
					.getNetIptvIssuedConfig(user_id, serv_type_id, serUsername);
			map.put("mode_type", "11");
		}
		// voip
		if ("14".equals(serv_type_id)) {
			map = dao.getVoipIssuedConfig(user_id, serv_type_id, serUsername);
			map.put("mode_type", "14");
		}
		return map;
	}

	/**
	 * @return the dao
	 */
	public BssSheetServStandardDAO getDao() {
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(BssSheetServStandardDAO dao) {
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
	public List<Map> getConfigInfo(String deviceId, String servTypeId,
			String servstauts, String wanType, String serUsername, String gw_type) {
		logger.debug("getConfigInfo({},{},{},{},{})", new Object[] { deviceId,
				servTypeId, servstauts, wanType, serUsername });
		if (true == StringUtil.IsEmpty(servTypeId)
				|| true == StringUtil.IsEmpty(servstauts)
				|| true == StringUtil.IsEmpty(wanType)
				) {//|| true == StringUtil.IsEmpty(serUsername)
			logger.debug("参数为空！");
			return null;
		}
		String serviceId = dao.getServiceId(servTypeId, servstauts, wanType);
		if (true == StringUtil.IsEmpty(serviceId)) {
			logger.debug("业务不存在！");
			return null;
		}
		List list = dao.getConfigInfo(deviceId, serviceId, serUsername, gw_type);
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
	public List<Map> getSoftUpInfo(String deviceId, String servTypeId,
			String servstauts, String wanType, String serUsername) {
		logger.debug("getSoftUpInfogetSoftUpInfo({},{},{},{})", new Object[] {
				deviceId, servTypeId, servstauts, wanType });
		if (true == StringUtil.IsEmpty(servTypeId)
				|| true == StringUtil.IsEmpty(servstauts)
				|| true == StringUtil.IsEmpty(serUsername)
				|| true == StringUtil.IsEmpty(wanType)) {
			logger.debug("参数为空！");
			return null;
		}
		String serviceId = dao.getServiceId(servTypeId, servstauts, wanType);
		if (true == StringUtil.IsEmpty(serviceId)) {
			logger.debug("业务不存在！");
			return null;
		}
		// 1401 为VOIP开通业务
		if ("1401".equals(serviceId)) {
			List list = dao.getSoftUpInfo(deviceId, serviceId, serUsername);
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
	public List<Map> getConfigDetail(String strategyId, String gw_type) {
		return dao.getConfigDetail(strategyId,gw_type);
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
	public List<Map> getConfigLogInfo(String deviceId, String servTypeId,
			String servstauts, String wanType, String gw_type) {
		logger.debug("getConfigLogInfo({},{},{},{})", new Object[] { deviceId,
				servTypeId, servstauts, wanType });
		if (true == StringUtil.IsEmpty(servTypeId)
				|| true == StringUtil.IsEmpty(servstauts)
				|| true == StringUtil.IsEmpty(wanType)) {
			logger.debug("参数为空！");
			return null;
		}
		String serviceId = dao.getServiceId(servTypeId, servstauts, wanType);
		if (true == StringUtil.IsEmpty(serviceId)) {
			logger.debug("业务不存在！");
			return null;
		}
		List list = dao.getConfigLogInfo(deviceId, serviceId, gw_type);
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
	public List<Map> getConfigLogDetail(String strategyId, String gw_type) {
		return dao.getConfigLogDetail(strategyId, gw_type);
	}

	/**
	 * 查询宽带上网工单详情信息
	 * 
	 * @return
	 */
	public List<Map> getInternetBssSheet(String cityId, String userId,
			String servTypeId, String serUsername, String gw_type) {
		if (StringUtil.IsEmpty(userId)) {
			logger.error("用户USERID没传过来！");
			return null;
		}
		return dao.getInternetBssSheet(gw_type, userId, servTypeId,
				serUsername, cityId);
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
			String servTypeId, String serUsername, String gw_type,String realBindPort) {
		logger.debug("getBssSheet({},{})", new Object[] { cityId, userId });
		if (StringUtil.IsEmpty(userId)) {
			logger.debug("用户USERID没传过来！");
			return null;
		}
		List<Map> list;
		if ("10".equals(servTypeId) || "11".equals(servTypeId) || "33".equals(servTypeId)
				|| "16".equals(servTypeId) || "28".equals(servTypeId)) {
			list = dao.getBssSheetByServtype(cityId, userId, servTypeId,
					serUsername, gw_type,realBindPort);
		} else if ("47".equals(servTypeId)) {
			list = dao.getCloudNetBssSheet(gw_type, userId, servTypeId, serUsername, cityId);
		}else {
			list = dao.getBssSheetVoIP(cityId, userId, gw_type);
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
			String deviceSN, String servTypeId, String servstauts,String serUsername) {
		logger.debug("callPreProcess({},{},{},{},{},{},{},{})", new Object[] {
				userId, deviceId, oui, deviceSN, servTypeId, servstauts,serUsername });
		
		PreServInfoOBJ obj = null;
		if(Global.SDLT.equals(Global.instAreaShortName) && "10".equals(servTypeId) && "2".equals(servstauts)){
			//停机
				servTypeId = "36";
				obj = new PreServInfoOBJ(userId, deviceId, serUsername,
						deviceSN, servTypeId, servstauts);
		}else{
			obj = new PreServInfoOBJ(userId, deviceId, oui,
					deviceSN, servTypeId, servstauts);
		}
		
		obj.setGatherId("1");
		// 更新业务用户表的业务开通状态
		// add by zhangchy 2011-10-19
		dao.updateServOpenStatus(StringUtil.getLongValue(userId), new Integer(
				servTypeId).intValue());
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
	 * 调用预读接口，重新激活工单 业务下发 与 重新激活 共用此方法 modify by zhangchy 2011-10-19
	 * 
	 * @author wangsenbo
	 * @date Sep 15, 2010
	 * @param
	 * @return String
	 */
	public String callPreProcess(String userId, String deviceId, String oui,
			String deviceSN, String servTypeId, String servstauts) {
		logger.debug("callPreProcess({},{},{},{},{},{},{},{})", new Object[] {
				userId, deviceId, oui, deviceSN, servTypeId, servstauts });
		
		PreServInfoOBJ obj = new PreServInfoOBJ(userId, deviceId, oui,
				deviceSN, servTypeId, servstauts);
		obj.setGatherId("1");
		// 更新业务用户表的业务开通状态
		// add by zhangchy 2011-10-19
		dao.updateServOpenStatus(StringUtil.getLongValue(userId), new Integer(
				servTypeId).intValue());
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
	public String checkdevice(String username) {
		return dao.checkdevice(username);
	}

	@SuppressWarnings("unchecked")
	public List<Map> getBssCustomerServInfo(String cityId,
			String startOpenDate1, String endOpenDate1, String usernameType,
			String username, String devicetype, int curPage_splitPage,
			int num_splitPage, String gw_type) {
		logger.debug("getBssSheetServInfo({},{},{},{},{},{},{})", new Object[] {
				cityId, startOpenDate1, endOpenDate1, usernameType, username });
		List<Map> userIdList = null;
		if (false == StringUtil.IsEmpty(username)) {
			if ("1".equals(usernameType)) {
				userIdList = dao.getUserBySN(username, gw_type);
			} else if ("2".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "10", gw_type);
			} else if ("3".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "11", gw_type);
			} else if ("4".equals(usernameType)) {
				userIdList = dao.getUserByVoip(username, null, gw_type);
			} else if ("5".equals(usernameType)) {
				userIdList = dao.getUserByVoip(null, username, gw_type);
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
				num_splitPage);
		maxPage_splitPage = dao.countBssCustomerServInfo(cityId,
				startOpenDate1, endOpenDate1, userId, devicetype,
				curPage_splitPage, num_splitPage);
		return list;
	}

	/**
	 * @excel导出
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getBssCustomerServInfo(String cityId,
			String startOpenDate1, String endOpenDate1, String usernameType,
			String username, String devicetype, String gw_type) {
		logger.debug("getBssSheetServInfo({},{},{},{},{},{},{})", new Object[] {
				cityId, startOpenDate1, endOpenDate1, usernameType, username });
		List<Map> userIdList = null;
		if (false == StringUtil.IsEmpty(username)) {
			if ("1".equals(usernameType)) {
				userIdList = dao.getUserBySN(username, gw_type);
			} else if ("2".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "10", gw_type);
			} else if ("3".equals(usernameType)) {
				userIdList = dao.getUserByServ(username, "11", gw_type);
			} else if ("4".equals(usernameType)) {
				userIdList = dao.getUserByVoip(username, null, gw_type);
			} else if ("5".equals(usernameType)) {
				userIdList = dao.getUserByVoip(null, username, gw_type);
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
				endOpenDate1, userId, devicetype);
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

	public List<Map> getServByUser(String userId, String gw_type) {
		logger.debug("getServByUser({})", new Object[] { userId });
		List<Map> list = dao.getServByUser(userId, gw_type);
		return list;
	}

	public String getSolutionInfo(String result_id) {
		logger.debug("getSolutionInfo({})", new Object[] { result_id });
		return dao.getSolutionInfo(result_id);
	}	
	
	
	
}
