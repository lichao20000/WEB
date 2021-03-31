package com.linkage.module.itms.service.bio;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.SocketChannelClient;
import com.linkage.module.gwms.util.SocketUtil;
import com.linkage.module.itms.service.dao.BssSheetByHandDAO;

public class BssSheetByHand4JXBIO {

	private static Logger logger = LoggerFactory
			.getLogger(BssSheetByHand4JXBIO.class);

	private BssSheetByHandDAO dao;
	private String spPara = "=";
	private String spServ = ";";

	public String checkLoid(String loid, String gwType) {
		logger.debug("checkLoid()", loid);
		StringBuffer sBuffer = new StringBuffer("");
		StringBuffer userInfo = new StringBuffer("");
		StringBuffer voipInfo = new StringBuffer("");
		StringBuffer netInfo = new StringBuffer("");
		StringBuffer itvInfo = new StringBuffer("");

		List userIdList = dao.getUserIdFromTabHgwcustomer(loid, gwType);

		if (null != userIdList && !"".equals(userIdList)
				&& userIdList.size() > 0) {
			Map userIdInfo = (Map) userIdList.get(0);
			String userId = StringUtil.getStringValue(userIdInfo, "user_id");
			List<HashMap<String, String>> bssSheetInfoList = dao
					.getBssSheetInfo(userId, gwType);
			logger.warn("bssSheetInfoList============>"+bssSheetInfoList);
			if (null != bssSheetInfoList && !"".equals(bssSheetInfoList)
					&& bssSheetInfoList.size() > 0) {
				for (int i = 0; i < bssSheetInfoList.size(); i++) {
					HashMap<String, String> infoMap = bssSheetInfoList.get(i);
					if (i == 0) {
						String _cityId = StringUtil.getStringValue(infoMap,
								"city_id", "");
						if (null == _cityId) {
							_cityId = "00";
						}
						logger.warn("infoMap======IS_PON======>"+StringUtil.getStringValue(infoMap,"is_pon",""));
						userInfo.append(
								StringUtil.getStringValue(infoMap, "loid", ""))
								.append(spPara)
								.append(_cityId)
								.append(spPara)
								.append(StringUtil.getStringValue(infoMap,
										"adsl_hl", ""))
								.append(spPara)
								.append(StringUtil.getStringValue(infoMap,
										"linkman", ""))
								.append(spPara)
								.append(StringUtil.getStringValue(infoMap,
										"linkphone", ""))
								.append(spPara)
								.append(StringUtil.getStringValue(infoMap,
										"email", ""))
								.append(spPara)
								.append(StringUtil.getStringValue(infoMap,
										"mobile", ""))
								// 6
								.append(spPara)
								.append(StringUtil.getStringValue(infoMap,
										"linkaddress", ""))
								.append(spPara)
								.append(StringUtil.getStringValue(infoMap,
										"credno", ""))
								.append(spPara)
								.append(StringUtil.getStringValue(infoMap,
										"spec_name", ""));
										//修改处
								if ("1".equals(gwType)) {
									userInfo.append(spPara)
								.append(StringUtil.getStringValue(infoMap,"is_pon",""));
								}
						if ("2".equals(gwType)) {
							userInfo.append(spPara).append(
									StringUtil.getStringValue(infoMap,
											"customer_id", ""));
						} else {
							userInfo.append(spPara).append("");
						}
					}
					// _cityId = _cityId.substring(0, 3);
					// -------获取用户资料 ---------end-----------
					// ---------- 宽带 ----------begin------
					if ("10".equals(StringUtil.getStringValue(infoMap,
							"serv_type_id"))) {
						netInfo.append(
								StringUtil.getStringValue(infoMap, "username",
										""))
								.append(spPara)
								.append(StringUtil.getStringValue(infoMap,
										"passwd", ""))
								.append(spPara)
								.append(StringUtil.getStringValue(infoMap,
										"vlanid", ""))
								.append(spPara)
								.append(StringUtil.getStringValue(infoMap,
										"wan_type", ""))
								.append(spPara)
								.append(StringUtil.getIntValue(infoMap,
										"untreated_ip_type"))
								.append(spPara)
								.append(StringUtil.getStringValue(infoMap,
										"aftr_mode", ""))
								.append(spPara)
								.append(StringUtil.getStringValue(infoMap,
										"aftr_ip", ""))
								.append(spPara)
								.append(StringUtil.getStringValue(infoMap,
										"vpdn", "")).append(spServ);
					}
					// ---------- 宽带 ----------end------
					
					// -----------IPTV -------------begin--------
					if ("11".equals(StringUtil.getStringValue(infoMap,
							"serv_type_id"))) {
						itvInfo.append(
								StringUtil.getStringValue(infoMap, "username",
										""))
								.append(spPara)
								.append(StringUtil.getStringValue(infoMap,
										"bind_port", ""))
								.append(spPara)
								.append(StringUtil.getStringValue(infoMap,
										"vlanid", "")).append(spServ);
										
								/*.append(StringUtil.getStringValue(infoMap,
										"pppoe_user", "")).append(spPara)
								.append(StringUtil.getStringValue(infoMap,
										"pppoe_pwd", "")).append(spPara)
								.append(StringUtil.getStringValue(infoMap,
										"serv_account", "")).append(spPara)
								.append(StringUtil.getStringValue(infoMap,
										"serv_pwd", "")).append(spServ);*/
					}
					// -----------IPTV -------------end--------
					// VOIP H248
					if ("14".equals(StringUtil.getStringValue(infoMap,
							"serv_type_id"))) {
						List<HashMap<String, String>> voipInfohashMapList = dao
								.getVoipinfo(userId, gwType);
						if (null != voipInfohashMapList
								&& voipInfohashMapList.size() > 0) {
							for (HashMap<String, String> voipInfoMap : voipInfohashMapList) {
								voipInfo.append(
										StringUtil.getStringValue(voipInfoMap,
												"line_id"))
										.append(spPara)
										.append(StringUtil.getStringValue(
												voipInfoMap, "voip_phone", ""))
										.append(spPara)
										.append(StringUtil.getStringValue(
												voipInfoMap, "reg_id", ""))
										.append(spPara)
										.append(StringUtil.getStringValue(
												voipInfoMap, "prox_serv", ""))
										.append(spPara)
										.append(StringUtil.getStringValue(
												voipInfoMap, "prox_port", ""))
										// 4
										.append(spPara)
										.append(StringUtil.getStringValue(
												voipInfoMap, "stand_prox_serv",
												""))
										.append(spPara)
										.append(StringUtil.getStringValue(
												voipInfoMap, "stand_prox_port",
												""))
										.append(spPara)
										.append(StringUtil.getStringValue(
												voipInfoMap, "voip_port", ""))
										.append(spPara)
										.append(StringUtil.getStringValue(
												infoMap, "vlanid", ""))
										// 8
										.append(spPara)
										.append(StringUtil.getStringValue(
												infoMap, "wan_type", ""))
										.append(spPara)
										.append(StringUtil.getStringValue(
												infoMap, "ipaddress", ""))
										.append(spPara)
										.append(StringUtil.getStringValue(
												infoMap, "ipmask", ""))
										.append(spPara)
										.append(StringUtil.getStringValue(
												infoMap, "gateway", ""))
										// 12
										.append(spPara)
										.append(StringUtil.getStringValue(
												infoMap, "adsl_ser", ""))
										.append(spServ);
							}
						}
					}
				}
			}
			sBuffer.append(userInfo).append("|").append(netInfo).append("|")
					.append(itvInfo).append("|").append(voipInfo);
			logger.warn("Return:  " + sBuffer.toString());

			return sBuffer.toString();
		} else {
			return "-1";
		}
	}

	public String doBusiness(String userInfo, String netInfo, String itvInfo,
			String voipInfo, String gwType,String account) {
		logger.debug("BssSheetByHandBIO==>doBusiness()");
		String retMessage = "";
		SocketChannelClient client = null;
		int sleepTime = 1000;
		DateTimeUtil dt = new DateTimeUtil();
		String city_id_ex = "";

		String receivedStrUser = "";
		String receivedStrWideNet = "";
		String receivedStrIPTV = "";
		String receivedStrVoip = "";

		String loid = "";
		logger.warn("userInfo=======>"+userInfo);
		logger.warn("itvInfo=======>"+itvInfo);
		String[] userArr = userInfo.split(spPara);
		if (userArr.length < 9) {
			retMessage = "用户资料参数不合法。";
			return retMessage;
		} else {
			StringBuffer userSheet = new StringBuffer();
			HashMap<String, String> cityIdExMap = dao.getCityIdExFromGwCityMap(
					userArr[1]).get(0);
			city_id_ex = StringUtil.getStringValue(cityIdExMap, "city_id_ex");
			loid = userArr[0];
			userSheet
					.append(dt.getYYYYMMDDHHMMSS())
					.append("|||")
					.append("20")
					.append("|||")
					.append("1")
					.append("|||")
					.append(dt.getYYYYMMDDHHMMSS())
					.append("|||")
					.append(gwType)
					.append("|||")
					.append(userArr[0])
					.append("|||")
					// loid
					.append(city_id_ex)
					.append("|||")
					.append("|||")
					.append("|||")
					.append(userArr[2])
					.append("|||")
					// jieRuType
					.append(userArr[3])
					.append("|||")
					// jieRuLinkMan
					.append(userArr[4])
					.append("|||")
					// jieRuLinPhone
					.append("")
					.append("|||")
					// jieRuEmail
					.append(userArr[6])
					.append("|||")
					// jieRuTelePhone
					.append(userArr[8])
					.append("|||")
					// jieRuAddress
					.append(userArr[7])
					.append("|||")
					// jieRuIDCard
					.append(userArr[9])
					.append("|||")
					// customerId
					.append("|||").append("|||")
					.append(userArr[5])
					.append("|||")
					// jieRuDeviceType
					.append(dt.getYYYYMMDDHHMMSS()).append("|||").append("N")
					.append("|||").append("4").append("|||").append("SG")
					.append("|||")
					.append("0");
					if(gwType.equals("1"))
					{
					userSheet.append("|||")
					// ConvergedUser
					.append(userArr[12]);
					}
					userSheet.append("FROMWEB");
			logger.warn("用户资料工单：" + userSheet.toString());
			try {
				client = LipossGlobals.getSocketChannelClientInstance();
				receivedStrUser = client.sendMsg(userSheet.toString());
				logger.warn("receivedStrUser---------->"+receivedStrUser);
				// 判断用户资料工单是否发送成功，如果发送成功，则发送宽带业务资工单
				String[] retMsg = receivedStrUser.split("\\|\\|\\|");
				// 如果回单信息<RETNMSG>节点中的内容不为空的话，说明用户资料发送失败，所以接下来的宽带，iptv，h248等工单就不用再发了
				receivedStrUser = retMsg[retMsg.length - 1].replace(
						"<RETNMSG>", "").replace("</RETNMSG>", "");
				logger.warn("receivedStrUser==========>"+receivedStrUser);
				if (!"".equals(receivedStrUser.trim())) {
					logger.warn("用户资料工单发送失败:   " + receivedStrUser);
					retMessage = "用户资料工单发送失败: " + receivedStrUser + "！";
					return retMessage;
				} else {
					retMessage = "用户资料工单发送成功！";
					logger.warn("用户资料工单发送成功！");
					// 宽带业务工单
					if (null != netInfo && netInfo.length() > 3) {
						ArrayList<HashMap<String,String>> wanTypelist = dao.queryWanTypeList(loid);
						String[] netArr = netInfo.split(spServ);
						StringBuffer wideNet = new StringBuffer();
						int count = 0;
						for (String nets : netArr) {
							String[] netparam = nets.split(spPara);
							if (netparam.length > 3) {
								count++;
								wideNet = new StringBuffer();
								wideNet.append(dt.getYYYYMMDDHHMMSS())
										.append("|||").append("22")
										.append("|||").append("1")
										.append("|||")
										.append(dt.getYYYYMMDDHHMMSS())
										.append("|||").append(gwType)
										.append("|||").append(loid)
										.append("|||").append(netparam[0])
										.append("|||").append(netparam[1])
										.append("|||").append(city_id_ex)
										.append("|||").append(netparam[2])
										.append("|||").append(netparam[3])
										.append("|||").append("|||")
										.append("|||").append("|||")
										.append("|||").append(netparam[4])
										.append("|||").append(netparam[5])
										.append("|||").append(netparam[6])
										.append("|||").append(netparam[7])
										.append("FROMWEB");
								logger.warn("宽带业务" + count + "工单：" 
										+ wideNet.toString());
								// Thread.sleep(sleepTime);
								receivedStrWideNet = client.sendMsg(wideNet
										.toString());
								retMsg = receivedStrWideNet.split("\\|\\|\\|");
								receivedStrWideNet = retMsg[retMsg.length - 1]
										.replace("<RETNMSG>", "").replace(
												"</RETNMSG>", "");
								if (!"".equals(receivedStrWideNet.trim())) {
									receivedStrWideNet = retMsg[retMsg.length - 1]
											.replace("<RETNMSG>", "").replace(
													"</RETNMSG>", "");
									logger.warn("宽带业务工单发送失败:  "
											+ receivedStrWideNet);
									retMessage = retMessage + " 宽带业务" + count
											+ "工单发送失败：" + receivedStrWideNet
											+ "！";
									for (HashMap map: wanTypelist) {
										String netAccount = StringUtil.getStringValue(map.get("username"));
										String wanType = StringUtil.getStringValue(map.get("wan_type"));
										if(!netparam[3].equals(wanType) && netparam[0].equals(netAccount))
										{
											dao.recordRouteAndBridge(loid,netparam[0],netparam[3],"手工工单","2",account,"失败");
										}
									}

								} else {
									logger.warn("宽带业务工单发送成功！");
									retMessage = retMessage + " 宽带业务" + count
											+ "工单发送成功！";
									for (HashMap map: wanTypelist) {
										String netAccount = StringUtil.getStringValue(map.get("username"));
										String wanType = StringUtil.getStringValue(map.get("wan_type"));
										if(!netparam[3].equals(wanType) && netparam[0].equals(netAccount))
										{
											dao.recordRouteAndBridge(loid,netparam[0],netparam[3],"手工工单","1",account,"成功");
										}
									}
								}

								// netparam[0] 宽带账号   2 上网方式
								//获取现有的上网方式，如果手工工单和现在的一致，则不是桥改路由或者路由改桥，则不需要入表记录。
								//



							}
						}
					}
					// itv
					String[] itvArr = itvInfo.split(spPara);
					
					if (itvArr.length > 2) {
						logger.warn("itvArr====>"+itvArr);
						StringBuffer iptv = new StringBuffer();
						iptv.append(dt.getYYYYMMDDHHMMSS()).append("|||")
								.append("21").append("|||").append("1")
								.append("|||").append(dt.getYYYYMMDDHHMMSS())
								.append("|||").append(gwType).append("|||")
								.append(loid).append("|||").append(itvArr[0])
								.append("|||").append(city_id_ex).append("|||")
								.append("1").append("|||").append(itvArr[1])
								.append("|||").append(itvArr[2])
								/*//修改处
								.append(itvArr[4]).append("|||")
								.append(itvArr[5]).append("|||")
								.append(itvArr[6]).append("|||")
								.append(itvArr[7])*/
								.append("FROMWEB");
						logger.warn("IPTV业务工单：" + iptv.toString());
						// Thread.sleep(sleepTime);
						receivedStrIPTV = client.sendMsg(iptv.toString());
						retMsg = receivedStrIPTV.split("\\|\\|\\|");
						receivedStrIPTV = retMsg[retMsg.length - 1].replace(
								"<RETNMSG>", "").replace("</RETNMSG>", "");
						if (!"".equals(receivedStrIPTV.trim())) {
							receivedStrIPTV = retMsg[retMsg.length - 1]
									.replace("<RETNMSG>", "").replace(
											"</RETNMSG>", "");
							logger.warn("IPTV业务工单发送失败:  " + receivedStrIPTV);
							retMessage = retMessage + " IPTV业务工单发送失败："
									+ receivedStrIPTV + "！";
						} else {
							logger.warn("IPTV业务工单发送成功！ ");
							retMessage = retMessage + " IPTV业务工单发送成功！";
						}
					}
					// voip
					if (null != voipInfo && voipInfo.length() > 10) {
						String[] voips = voipInfo.split(spServ);
						// Thread.sleep(sleepTime);
						List userIdList = dao.getUserIdFromTabHgwcustomer(loid,
								gwType);
						Map userIdInfo = (Map) userIdList.get(0);
						String userId = StringUtil.getStringValue(userIdInfo,
								"user_id");
						StringBuffer h248Line = null;
						int num = dao.deleteLineData(userId, "", "", gwType);
						for (String voip : voips) {
							String[] voipVals = voip.split(spPara);
							h248Line = new StringBuffer();
							if (voipVals.length > 14) {
								int one = dao.addLineDate(userId, voipVals[1],
										voipVals[2], voipVals[8], gwType);
								if (one == 1) {
									logger.warn("语音线路入库成功");
									h248Line.append(dt.getYYYYMMDDHHMMSS())
											.append("|||").append("15")
											.append("|||").append("1")
											.append("|||")
											.append(dt.getYYYYMMDDHHMMSS())
											.append("|||").append(gwType)
											.append("|||")
											.append(loid)
											.append("|||")
											.append(voipVals[2])
											.append("|||")
											// H248PhoneNum1
											.append(city_id_ex).append("|||")
											.append(voipVals[3])
											.append("|||")
											// H248BiaoShi1
											.append("1").append("|||")
											.append(voipVals[4]).append("|||")
											// proxServ1
											.append(voipVals[5]).append("|||")
											// proxPort1
											.append(voipVals[6]).append("|||")
											// standProxServ1
											.append(voipVals[7]).append("|||")
											// standProxPort1
											.append(voipVals[8]).append("|||")
											// H248DeviceType1
											.append(voipVals[10]).append("|||")
											// H248VLanId
											.append(voipVals[9]).append("|||")
											// wanType
											.append(voipVals[11]).append("|||")
											// ipaddress
											.append(voipVals[12]).append("|||")
											// ipmask
											.append(voipVals[13]).append("|||")
											// gateway
											.append(voipVals[14])
											.append("FROMWEB");// adslSer
									logger.warn("H248语音业务工单" + voipVals[1]
											+ "：" + h248Line.toString());
									receivedStrVoip = client.sendMsg(h248Line
											.toString());
									retMsg = receivedStrVoip.split("\\|\\|\\|");
									receivedStrVoip = retMsg[retMsg.length - 1]
											.replace("<RETNMSG>", "").replace(
													"</RETNMSG>", "");
									if (!"".equals(receivedStrVoip.trim())) {
										receivedStrVoip = retMsg[retMsg.length - 1]
												.replace("<RETNMSG>", "")
												.replace("</RETNMSG>", "");
										logger.warn("H248语音业务工单" + voipVals[1]
												+ "：" + receivedStrVoip);
										retMessage = retMessage + "H248语音业务工单"
												+ voipVals[1] + "："
												+ receivedStrVoip + "！";
										dao.deleteLineData(userId, voipVals[1],
												"", gwType);
									} else {
										logger.warn("H248语音业务工单" + voipVals[1]
												+ "：" + "发送成功！ ");
										retMessage = retMessage + " H248语音业务工单"
												+ voipVals[1] + "：" + "发送成功！";
									}
								} else {
									logger.warn(userId + "线路" + voipVals[1]
											+ "入库失败");
								}
							}
						}
					}
				}
				return retMessage;
			} catch (Exception e) {
				logger.warn("工单发送时出现异常; " + e.getMessage());
				logger.error(e.getMessage());
				logger.error("工单异常：" + e);
				e.printStackTrace();
				return "工单发送时出现异常，发送失败！";
			} finally {
				if (client != null) {
					try {
						client.close();
					} catch (IOException e) {
						logger.error("关闭SocketChannelClient错误：" + e);
						retMessage = "网络问题";
					}
				}

			}
		}
	}

	/**
	 * 全业务工单销户
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param userType
	 * @return
	 */
	public String sendStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String userType) {
		logger.debug("sendStopSheet()");
		StringBuffer sbuffer = new StringBuffer();
		HashMap<String, String> cityIdExMap = dao.getCityIdExFromGwCityMap(
				cityId).get(0);
		String city_id_ex = StringUtil
				.getStringValue(cityIdExMap, "city_id_ex");
		sbuffer.append(new DateTimeUtil(dealdate).getLongTime()).append("|||");
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(city_id_ex).append("FROMWEB");
		logger.warn("全业务销户工单："
				+ sbuffer.toString().replace("<RETN>", "")
						.replace("</RETN>", "").replace("<RETNCODE>", "")
						.replace("</RETNCODE>", "").replace("<RETNMSG>", "")
						.replace("</RETNMSG>", ""));
		String strMsg = this.sendSheet(sbuffer.toString());
		return strMsg.replace("<RETN>", "").replace("</RETN>", "")
				.replace("<RETNCODE>", "").replace("</RETNCODE>", "")
				.replace("<RETNMSG>", "").replace("</RETNMSG>", "");
	}

	/**
	 * 上网工单销户
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param userType
	 * @param netUsername
	 * @return
	 */
	public String sendNetStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String userType,
			String netUsername) {
		logger.debug("sendStopSheet()");
		StringBuffer sbuffer = new StringBuffer();
		HashMap<String, String> cityIdExMap = dao.getCityIdExFromGwCityMap(
				cityId).get(0);
		String city_id_ex = StringUtil
				.getStringValue(cityIdExMap, "city_id_ex");
		sbuffer.append(new DateTimeUtil(dealdate).getLongTime()).append("|||");
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(city_id_ex).append("|||");
		sbuffer.append(netUsername).append("FROMWEB");
		logger.warn("上网销户工单："
				+ sbuffer.toString().replace("<RETN>", "")
						.replace("</RETN>", "").replace("<RETNCODE>", "")
						.replace("</RETNCODE>", "").replace("<RETNMSG>", "")
						.replace("</RETNMSG>", ""));
		String strMsg = this.sendSheet(sbuffer.toString());
		return strMsg.replace("<RETN>", "").replace("</RETN>", "")
				.replace("<RETNCODE>", "").replace("</RETNCODE>", "")
				.replace("<RETNMSG>", "").replace("</RETNMSG>", "");
	}

	/**
	 * IPTV拆机
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param userType
	 * @param iptvUsername
	 * @param iptvLanPort
	 * @return
	 */
	public String sendIptvStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String userType,
			String iptvUsername, String iptvLanPort) {
		StringBuffer sbuffer = new StringBuffer();
		HashMap<String, String> cityIdExMap = dao.getCityIdExFromGwCityMap(
				cityId).get(0);
		String city_id_ex = StringUtil
				.getStringValue(cityIdExMap, "city_id_ex");
		sbuffer.append(new DateTimeUtil(dealdate).getLongTime()).append("|||");
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(city_id_ex).append("|||");
		sbuffer.append(iptvUsername).append("|||");
		sbuffer.append(iptvLanPort).append("FROMWEB");
		logger.warn("IPTV销户工单："
				+ sbuffer.toString().replace("<RETN>", "")
						.replace("</RETN>", "").replace("<RETNCODE>", "")
						.replace("</RETNCODE>", "").replace("<RETNMSG>", "")
						.replace("</RETNMSG>", ""));
		String strMsg = this.sendSheet(sbuffer.toString());
		return strMsg.replace("<RETN>", "").replace("</RETN>", "")
				.replace("<RETNCODE>", "").replace("</RETNCODE>", "")
				.replace("<RETNMSG>", "").replace("</RETNMSG>", "");
	}

	/**
	 * voip销户
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param voipTelepone
	 * @param userType
	 * @return
	 */
	public String sendVoipStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId,
			String voipTelepone, String userType) {
		logger.debug("sendVoipStopSheet()");
		StringBuffer sbuffer = new StringBuffer();
		HashMap<String, String> cityIdExMap = dao.getCityIdExFromGwCityMap(
				cityId).get(0);
		String city_id_ex = StringUtil
				.getStringValue(cityIdExMap, "city_id_ex");
		sbuffer.append(new DateTimeUtil(dealdate).getLongTime()).append("|||");
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(city_id_ex).append("|||");
		sbuffer.append(voipTelepone).append("FROMWEB");
		logger.warn("voip销户工单："
				+ sbuffer.toString().replace("<RETN>", "")
						.replace("</RETN>", "").replace("<RETNCODE>", "")
						.replace("</RETNCODE>", "").replace("<RETNMSG>", "")
						.replace("</RETNMSG>", ""));
		String strMsg = this.sendSheet(sbuffer.toString());
		return strMsg.replace("<RETN>", "").replace("</RETN>", "")
				.replace("<RETNCODE>", "").replace("</RETNCODE>", "")
				.replace("<RETNMSG>", "").replace("</RETNMSG>", "");
	}

	public String checkUsername(String username, String gw_type) {
		List<Map<String, String>> userList = this.dao.getUserInfo(username,
				gw_type);
		int size = userList.size();
		if (size == 1) {
			return "1#"
					+ (String) ((Map<String, String>) userList.get(0))
							.get("orderType");
		}
		if (size > 1) {
			return "0#请输入完整的逻辑SN";
		}
		return "-1#逻辑SN不存在";
	}

	/**
	 * 时间转换
	 * 
	 * @param dealdate
	 * @return
	 */
	private String transferDateFormate(String dealdate) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = dateFormat.parse(dealdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(date);
	}

	public String sendSheet(String bssSheet) {
		if (StringUtil.IsEmpty(bssSheet)) {
			logger.warn("sendSheet is null");
			return null;
		}
		String server = Global.G_ITMS_Sheet_Server;
		int port = Global.G_ITMS_Sheet_Port;
		String retResult = SocketUtil
				.sendStrMesg(server, port, bssSheet + "\n");
		return retResult;
	}

	public BssSheetByHandDAO getDao() {
		return dao;
	}

	public void setDao(BssSheetByHandDAO dao) {
		this.dao = dao;
	}

	public List<Map<String, String>> getDeviceType(String gwType) {
		return dao.getDeviceTypes(gwType);
	}

	public static void main(String[] args) {
		String str = "工单执行结果 失败 20|||<RETN>1</RETN>|||<RETNCODE>4</RETNCODE>|||<RETNMSG>业务受理时间不对</RETNMSG>";
		String value = str.replace("<RETN>", "").replace("</RETN>", "")
				.replace("<RETNCODE>", "").replace("</RETNCODE>", "")
				.replace("<RETNMSG>", "").replace("</RETNMSG>", "");
		System.out.print(value);
	}
}