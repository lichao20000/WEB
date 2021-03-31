package com.linkage.module.itms.service.act;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;
import com.linkage.module.gwms.util.corba.obj.PreServInfoOBJ;
import com.linkage.module.itms.service.bio.ManuallyConfigurePortBusinessBIO;

public class ManuallyConfigurePortBusinessACT implements SessionAware {
	private static Logger logger = LoggerFactory.getLogger(ManuallyConfigurePortBusinessACT.class);
	
	private static final String _NBSP ="&nbsp;&nbsp;&nbsp;&nbsp;";
	@SuppressWarnings("rawtypes")
	private Map session;

	private String ajax;

	private String loid;

	private String val;

	private String id;

	private String username;

	private String userid;

	private String deviceId;

	private String interVal;
	private String itvVal;
	private String tianyiVal;

	private String oui;

	private String devSn;

	private String interentNameListHtml;
	private String nointerentNameListHtml;
	private String realinterentNameListHtml;
	private String disrealinterentNameListHtml;

	private String itvNameListHtml;
	private String noitvNameListHtml;
	private String realitvNameListHtml;
	private String disrealitvNameListHtml;

	private String tianyiNameListHtml;
	private String notianyiNameListHtml;
	private String realtianyiNameListHtml;
	private String disrealtianyiNameListHtml;

	@SuppressWarnings("rawtypes")
	private List<Map> userList;
	@SuppressWarnings("rawtypes")
	private List<Map> vlanList;

	@SuppressWarnings("rawtypes")
	private List<Map> userMap;
	@SuppressWarnings("rawtypes")
	private List<Map> vlanMap;

	private int interleftNum;
	private int interrightNum;

	private ManuallyConfigurePortBusinessBIO bio;
	
	@SuppressWarnings("rawtypes")
	public String initLoid() {
		int result = bio.qyeryLoid(loid);
		if (result > 0) {
			
			String tempUserid=null;

			int wlanNum = bio.wlanNum(loid);
			int lanNum = bio.lanNum(loid);
			int realwlanNum = bio.realwlanNum(loid);
			int reallanNum = bio.reallanNum(loid);

			// /////////////////////////////////////////宽带////////////////////////////////////////////
			List<Map> internetList = bio.internetList(loid);
			tempUserid = internetList.get(0).get("user_id").toString();
			
			interleftNum = internetList.size();
			if (internetList.size() > 0) {
				StringBuffer interleft = new StringBuffer();
				StringBuffer interleft_foot = new StringBuffer();
				userList = new ArrayList<Map>();
				userMap = new ArrayList<Map>();
				for (int k = 0; k < internetList.size(); k++) {
					tempUserid = internetList.get(k).get("user_id").toString();
					Map<String, String> user_Map = new HashMap<String, String>();
					List<String> lanlist = new ArrayList<String>();
					Map<String, String> lanMap = new HashMap<String, String>();
					List<String> wlanlist = new ArrayList<String>();
					Map<String, String> wlanMap = new HashMap<String, String>();
					String[] bind_port = internetList.get(k).get("bind_port").toString().split(",");
					for (int i = 0; i < bind_port.length; i++) {
						if (bind_port[i].length() == 62) {
							lanlist.add(bind_port[i]);
						}
						if (bind_port[i].length() == 53) {
							wlanlist.add(bind_port[i]);
						}
						if (bind_port[i].length() == 63) {
							lanlist.add(bind_port[i].substring(0,bind_port[i].length()-1));
						}
						if (bind_port[i].length() == 54) {
							wlanlist.add(bind_port[i].substring(0,bind_port[i].length()-1));
						}
					}
					if (!lanlist.isEmpty()) {
						for (int i = 0; i < lanlist.size(); i++) {
							lanMap.put(lanlist.get(i).substring(61), lanlist.get(i).substring(61));
						}
					}
					if (!wlanlist.isEmpty()) {
						for (int i = 0; i < wlanlist.size(); i++) {
							wlanMap.put(wlanlist.get(i).substring(52), wlanlist.get(i).substring(52));
						}
					}
					StringBuffer sb = new StringBuffer();
					interleft.append("<tr><td >");
					interleft_foot.append("<tr><td >");
					for (int i = 0; i < lanNum; i++) {
						if (lanMap.get(String.valueOf(i + 1)) != null) {
							interleft.append("<input disabled='disabled' type='checkbox' name='interlanDis"+ (k + 1)+ "'  checked='checked' value='LAN"+ (i + 1) + "'/> ").append("LAN").append(i + 1);
							interleft.append(_NBSP);
							interleft_foot.append("<input type='checkbox' name='interlan'  checked='checked' value='LAN"+ (i + 1) + "'/> ").append("LAN").append(i + 1);
							interleft_foot.append(_NBSP);
							sb.append("<input type='checkbox' name='"+ internetList.get(k).get("username")+ "'  checked='checked' value='LAN"+ (i + 1) + "'/> ").append("LAN").append(i + 1);
							sb.append(_NBSP);
						} else {
							interleft.append("<input disabled='disabled' type='checkbox' name='interlanDis"+ (k + 1)+ "'  value='LAN"+ (i + 1) + "'/>").append("LAN").append(i + 1);
							interleft.append(_NBSP);
							interleft_foot.append("<input type='checkbox' name='interlan'  value='LAN"+ (i + 1) + "'/>").append("LAN").append(i + 1);
							interleft_foot.append(_NBSP);
							sb.append("<input type='checkbox' name='"+ internetList.get(k).get("username")+ "'  value='LAN" + (i + 1) + "'/>").append("LAN").append(i + 1);
							sb.append(_NBSP);
						}
					}
					for (int i = 0; i < wlanNum; i++) {
						if (wlanMap.get(String.valueOf(i + 1)) != null) {
							interleft.append("<input disabled='disabled' type='checkbox' name='interwlanDis"+ (k + 1)+ "'  checked='checked' value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
							interleft.append(_NBSP);
							interleft_foot.append("<input type='checkbox' name='interwlan'  checked='checked' value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
							interleft_foot.append(_NBSP);
							sb.append("<input type='checkbox' name='"+ internetList.get(k).get("username")+ "'  checked='checked' value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
							sb.append(_NBSP);
						} else {
							interleft.append("<input disabled='disabled' type='checkbox'  name='interwlanDis"+ (k + 1)+ "'  value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
							interleft_foot.append("<input type='checkbox' name='"+ internetList.get(k).get("username")+ "'  value='WLAN" + (i + 1)+ "'/>").append("WLAN").append(i + 1);
							sb.append("<input type='checkbox' name='"+ internetList.get(k).get("username")+ "'  value='WLAN" + (i + 1)+ "'/>").append("WLAN").append(i + 1);
						}

					}
					user_Map.put("userData", internetList.get(k).get("username").toString());
					userList.add(user_Map);
					interleft.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append(internetList.get(k).get("username")).append("</td></tr>");
					interleft_foot.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append(internetList.get(k).get("username")).append("</td><input type='hidden' name='interusername' value='"+ internetList.get(k).get("username")+ "'><input type='hidden' name='interuserid' value='"+ internetList.get(k).get("user_id")+ "'><input type='hidden' name='interDevsn' value='"+ internetList.get(k).get("device_serialnumber")+ "'><input type='hidden' name='interoui' value='"+ internetList.get(k).get("oui")+ "'><input type='hidden' name='interdeviceId' value='"+ internetList.get(k).get("device_id")+ "'>").append("<td></tr>");
					sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append(internetList.get(k).get("username")).append("</td><input type='hidden' name='interusername"+ internetList.get(k).get("username")+ "' value='"+ internetList.get(k).get("username")+ "'><input type='hidden' name='interuserid"+ internetList.get(k).get("username")+ "' value='"+ internetList.get(k).get("user_id")+ "'><input type='hidden' name='interDevsn"+ internetList.get(k).get("username")+ "' value='"+ internetList.get(k).get("device_serialnumber")+ "'><input type='hidden' name='interoui"+ internetList.get(k).get("username")+ "' value='"+ internetList.get(k).get("oui")+ "'><input type='hidden' name='interdeviceId"+ internetList.get(k).get("username")+ "' value='"+ internetList.get(k).get("device_id")+ "'>");
					deviceId = internetList.get(k).get("device_id").toString();
					if (!StringUtil.IsEmpty(internetList.get(k).get("username").toString())) {
						Map<String, String> temp_map = new HashMap<String, String>();
						temp_map.put(internetList.get(k).get("username").toString(), sb.toString());
						userMap.add(temp_map);
					}
				}
				StringBuffer interright = new StringBuffer();
				StringBuffer interright_foot = new StringBuffer();
				int rsint = new SuperGatherCorba("1").getCpeParams(deviceId, 0,3);
				if (rsint != 1) {
					logger.warn("宽带设备采集失败");
					interright.append("宽带设备采集失败<input type='hidden' name='interfail' value='0'>");
					interright_foot.append("宽带设备采集失败");
				} else {
					logger.warn("宽带设备采集成功");
					vlanList = new ArrayList<Map>();
					vlanMap = new ArrayList<Map>();
					List<HashMap<String, String>> internetlist = bio.getInternet(deviceId);
					interrightNum = internetlist.size();
					for (int ki = 0; ki < internetlist.size(); ki++) {
						Map<String, String> vlMap = new HashMap<String, String>();
						List<String> reallanlist = new ArrayList<String>();
						Map<String, String> reallanMap = new HashMap<String, String>();
						List<String> realwlanlist = new ArrayList<String>();
						Map<String, String> realwlanMap = new HashMap<String, String>();
						String vlId = internetlist.get(ki).get("vlan_id");
						
						String myUsername = bio.getUserName(tempUserid,vlId);
						
						String[] bindport = internetlist.get(ki).get("bind_port").split(",");
						for (int j = 0; j < bindport.length; j++) {
							if (bindport[j].length() == 62) {
								reallanlist.add(bindport[j]);
							}
							if (bindport[j].length() == 53) {
								realwlanlist.add(bindport[j]);
							}
							if (bindport[j].length() == 63) {
								reallanlist.add(bindport[j].substring(0,bindport[j].length()-1));
							}
							if (bindport[j].length() == 54) {
								realwlanlist.add(bindport[j].substring(0,bindport[j].length()-1));
							}
						}
						for (int i = 0; i < reallanlist.size(); i++) {
							reallanMap.put(reallanlist.get(i).substring(61),reallanlist.get(i).substring(61));
						}
						for (int i = 0; i < realwlanlist.size(); i++) {
							realwlanMap.put(realwlanlist.get(i).substring(52),realwlanlist.get(i).substring(52));
						}
						StringBuffer sb = new StringBuffer();
						interright.append("<tr><td >");
						interright_foot.append("<tr><td >");
						for (int i = 0; i < reallanNum; i++) {
							if (reallanMap.get(String.valueOf(i + 1)) != null) {
								interright.append("<input disabled='disabled' type='checkbox' checked='checked'  value='LAN"+ (i + 1) + "'/>").append("LAN").append(i + 1);
								interright.append(_NBSP);
								interright_foot.append("<input type='checkbox' checked='checked' name='laninterreal' value='LAN"+ (i + 1) + "'/>").append("LAN").append(i + 1);
								interright_foot.append(_NBSP);
								sb.append("<input type='checkbox' checked='checked' name='real"+ myUsername + "' value='LAN"+ (i + 1) + "'/>").append("LAN").append(i + 1);
								sb.append(_NBSP);
							} else {
								interright.append("<input disabled='disabled' type='checkbox' value='LAN"+ (i + 1) + "'/>").append("LAN").append(i + 1);
								interright.append(_NBSP);
								interright_foot.append("<input type='checkbox' name='laninterreal' value='LAN"+ (i + 1) + "'/>").append("LAN").append(i + 1);
								interright_foot.append(_NBSP);
								sb.append("<input type='checkbox' name='real"+ myUsername + "' value='LAN" + (i + 1)+ "'/>").append("LAN").append(i + 1);
								sb.append(_NBSP);
							}
						}
						for (int i = 0; i < realwlanNum; i++) {
							if (realwlanMap.get(String.valueOf(i + 1)) != null) {
								interright.append("<input disabled='disabled' type='checkbox'  checked='checked' value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
								interright.append(_NBSP);
								interright_foot.append("<input type='checkbox' name='wlaninterreal' checked='checked' value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
								interright_foot.append(_NBSP);
								sb.append("<input type='checkbox' name='"+ myUsername+ "' checked='checked' value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
								sb.append(_NBSP);
							} else {
								interright.append("<input disabled='disabled' type='checkbox'  value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
								interright.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
								interright_foot.append("<input type='checkbox' name='wlaninterreal' value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
								interright_foot.append(_NBSP);
								sb.append("<input type='checkbox' name='real"+ myUsername + "' value='WLAN" + (i + 1)+ "'/>").append("WLAN").append(i + 1);
								sb.append(_NBSP);
							}
						}
						vlMap.put("vlan_Map", myUsername);
						vlanList.add(vlMap);
						interright.append(myUsername);
						interright_foot.append(myUsername);
						sb.append(myUsername);
						interright.append("</td></tr>");
						interright_foot.append("</td></tr>");
						if (!StringUtil.IsEmpty(myUsername)) {
							Map<String, String> map = new HashMap<String, String>();
							map.put(myUsername, sb.toString());
							vlanMap.add(map);
						}
					}
				}
				realinterentNameListHtml = interright_foot.toString();
				disrealinterentNameListHtml = interright.toString();
				nointerentNameListHtml = interleft.toString();
				interentNameListHtml = interleft_foot.toString();
			}

			// /////////////////////////////////////////ITV////////////////////////////////////////////
			List<Map> itvList = bio.itvList(loid);
			if (itvList.size() > 0) {
				StringBuffer itvLeft = new StringBuffer();
				StringBuffer itvLeft_foot = null;
				for (int k = 0; k < itvList.size(); k++) {
					itvLeft_foot = new StringBuffer();
					List<String> itvlanlist = new ArrayList<String>();
					Map<String, String> itvlanMap = new HashMap<String, String>();
					List<String> itvwlanlist = new ArrayList<String>();
					Map<String, String> itvwlanMap = new HashMap<String, String>();
					String[] bind_port = itvList.get(k).get("bind_port").toString().split(",");
					for (int i = 0; i < bind_port.length; i++) {
						if (bind_port[i].length() == 62) {
							itvlanlist.add(bind_port[i]);
						}
						if (bind_port[i].length() == 53) {
							itvwlanlist.add(bind_port[i]);
						}
						if (bind_port[i].length() == 63) {
							itvlanlist.add(bind_port[i].substring(0,bind_port[i].length()-1));
						}
						if (bind_port[i].length() == 54) {
							itvwlanlist.add(bind_port[i].substring(0,bind_port[i].length()-1));
						}
					}
					for (int i = 0; i < itvlanlist.size(); i++) {
						itvlanMap.put(itvlanlist.get(i).substring(61),
								itvlanlist.get(i).substring(61));
					}
					for (int i = 0; i < itvwlanlist.size(); i++) {
						itvwlanMap.put(itvwlanlist.get(i).substring(52),
								itvwlanlist.get(i).substring(52));
					}
					itvLeft.append("<tr><td>");
					itvLeft_foot.append("<tr><td>");
					for (int i = 0; i < lanNum; i++) {
						if (itvlanMap.get(String.valueOf(i + 1)) != null) {
							itvLeft.append("<input disabled='disabled' type='checkbox' name='itvlanDis'  checked='checked' value='LAN"+ (i + 1) + "'/> ").append("LAN").append(i + 1);
							itvLeft.append(_NBSP);
							itvLeft_foot.append("<input  type='checkbox' name='itvlan' checked='checked' value='LAN"+ (i + 1) + "'/> ").append("LAN").append(i + 1);
							itvLeft_foot.append(_NBSP);
						} else {
							itvLeft.append("<input disabled='disabled' type='checkbox' name='itvlanDis'  value='LAN"+ (i + 1) + "'/>").append("LAN").append(i + 1);
							itvLeft.append(_NBSP);
							itvLeft_foot.append("<input type='checkbox' name='itvlan' value='LAN"+ (i + 1) + "'/>").append("LAN").append(i + 1);
							itvLeft_foot.append(_NBSP);
						}
					}
					for (int i = 0; i < wlanNum; i++) {
						if (itvwlanMap.get(String.valueOf(i + 1)) != null) {
							itvLeft.append("<input disabled='disabled' type='checkbox' name='itvwlanDis'  checked='checked' value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
							itvLeft.append(_NBSP);
							itvLeft_foot.append("<input type='checkbox' name='itvwlan' checked='checked' value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
							itvLeft_foot.append(_NBSP);
						} else {
							itvLeft.append("<input disabled='disabled' type='checkbox' name='itvwlanDis'  value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
							itvLeft.append(_NBSP);
							itvLeft_foot.append("<input type='checkbox' name='itvwlan' value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
							itvLeft_foot.append(_NBSP);
						}

					}
					itvLeft.append("</td></tr>");
					itvLeft_foot.append("</td><input type='hidden' name='itvusername' value='"+ itvList.get(k).get("username")+ "'><input type='hidden' name='itvuserid' value='"+ itvList.get(k).get("user_id")+ "'><input type='hidden' name='itvDevsn' value='"+ itvList.get(k).get("device_serialnumber")+ "'><input type='hidden' name='itvoui' value='"+ itvList.get(k).get("oui")+ "'><input type='hidden' name='itvdeviceId' value='"+ itvList.get(k).get("device_id") + "'>").append("</td></tr>");
					deviceId = itvList.get(k).get("device_id").toString();
				}
				

				StringBuffer itvRight = new StringBuffer();
				StringBuffer itvRight_foot = null;
				int rsintitv = new SuperGatherCorba("1").getCpeParams(deviceId,0, 3);
				if (rsintitv != 1) {
					logger.warn("ITV设备采集失败");
					itvRight.append("ITV设备采集失败<input type='hidden' name='itvfail' value='0'>");
					itvRight_foot = new StringBuffer();
					itvRight_foot.append("ITV设备采集失败");
				} else {
					logger.warn("ITV设备采集成功");
					List<HashMap<String, String>> iptvlist = bio.getIPTV(deviceId);
					for (int ki = 0; ki < iptvlist.size(); ki++) {
						itvRight_foot = new StringBuffer();
						List<String> realitvlanlist = new ArrayList<String>();
						Map<String, String> realitvlanMap = new HashMap<String, String>();
						List<String> realitvwlanlist = new ArrayList<String>();
						Map<String, String> realitvwlanMap = new HashMap<String, String>();
						String[] bindport = iptvlist.get(ki).get("bind_port").split(",");
						for (int j = 0; j < bindport.length; j++) {
							if (bindport[j].length() == 62) {
								realitvlanlist.add(bindport[j]);
							}
							if (bindport[j].length() == 53) {
								realitvwlanlist.add(bindport[j]);
							}
							if (bindport[j].length() == 63) {
								realitvlanlist.add(bindport[j].substring(0,bindport[j].length()-1));
							}
							if (bindport[j].length() == 54) {
								realitvwlanlist.add(bindport[j].substring(0,bindport[j].length()-1));
							}
						}
						for (int i = 0; i < realitvlanlist.size(); i++) {
							realitvlanMap.put(
									realitvlanlist.get(i).substring(61),
									realitvlanlist.get(i).substring(61));
						}
						for (int i = 0; i < realitvwlanlist.size(); i++) {
							realitvwlanMap.put(realitvwlanlist.get(i).substring(52), realitvwlanlist.get(i).substring(52));
						}
						itvRight.append("<td>");
						itvRight_foot.append("<td>");
						for (int i = 0; i < lanNum; i++) {
							if (realitvlanMap.get(String.valueOf(i + 1)) != null) {
								itvRight.append("<input disabled='disabled' type='checkbox' checked='checked' value='LAN"+ (i + 1) + "'/>").append("LAN").append(i + 1);
								itvRight.append(_NBSP);
								itvRight_foot.append("<input type='checkbox' name='lanitvreal'  checked='checked' value='LAN"+ (i + 1) + "'/>").append("LAN").append(i + 1);
								itvRight_foot.append(_NBSP);
							} else {
								itvRight.append("<input disabled='disabled' type='checkbox'  value='LAN"+ (i + 1) + "'/>").append("LAN").append(i + 1);
								itvRight.append(_NBSP);
								itvRight_foot.append("<input type='checkbox' name='lanitvreal' value='LAN"+ (i + 1) + "'/>").append("LAN").append(i + 1);
								itvRight_foot.append(_NBSP);
							}
						}
						for (int i = 0; i < wlanNum; i++) {
							if (realitvwlanMap.get(String.valueOf(i + 1)) != null) {
								itvRight.append("<input disabled='disabled' type='checkbox'  checked='checked' value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
								itvRight.append(_NBSP);
								itvRight_foot.append("<input type='checkbox' name='wlanitvreal' checked='checked' value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
								itvRight_foot.append(_NBSP);
							} else {
								itvRight.append("<input disabled='disabled' type='checkbox' value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
								itvRight.append(_NBSP);
								itvRight_foot.append("<input type='checkbox' name='wlanitvreal' value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
								itvRight_foot.append(_NBSP);
							}
						}
						itvRight.append("</td></tr>");
						itvRight_foot.append("</td></tr>");
					}
				}
				noitvNameListHtml = itvLeft.toString();
				itvNameListHtml = itvLeft_foot.toString();
				disrealitvNameListHtml = itvRight.toString();
				realitvNameListHtml = itvRight_foot.toString();
			}
			//
			// ///////////////////////////////////////天翼看店//////////////////
			List<Map> tianyiList = bio.tianyiList(loid);
			if (tianyiList.size() > 0) {
				StringBuffer tianyiLeftsb = new StringBuffer();
				StringBuffer tianyiLeftsb_foot = new StringBuffer();
				StringBuffer tianyiRightsb = new StringBuffer();
				StringBuffer tianyiRightsb_foot = new StringBuffer();
				for (int k = 0; k < tianyiList.size(); k++) {
					List<String> tianyilanlist = new ArrayList<String>();
					Map<String, String> tianyilanMap = new HashMap<String, String>();
					List<String> tianyiwlanlist = new ArrayList<String>();
					Map<String, String> tianyiwlanMap = new HashMap<String, String>();
					String[] bind_port = tianyiList.get(k).get("bind_port").toString().split(",");
					for (int i = 0; i < bind_port.length; i++) {
						if (bind_port[i].length() == 62) {
							tianyilanlist.add(bind_port[i]);
						}
						if (bind_port[i].length() == 53) {
							tianyiwlanlist.add(bind_port[i]);
						}
						if (bind_port[i].length() == 63) {
							tianyilanlist.add(bind_port[i].substring(0,bind_port[i].length()-1));
						}
						if (bind_port[i].length() == 54) {
							tianyiwlanlist.add(bind_port[i].substring(0,bind_port[i].length()-1));
						}
					}
					for (int i = 0; i < tianyilanlist.size(); i++) {
						tianyilanMap.put(tianyilanlist.get(i).substring(61),
								tianyilanlist.get(i).substring(61));
					}
					for (int i = 0; i < tianyiwlanlist.size(); i++) {
						tianyiwlanMap.put(tianyiwlanlist.get(i).substring(52),
								tianyiwlanlist.get(i).substring(52));
					}
					tianyiLeftsb.append("<tr><td>");
					tianyiLeftsb_foot.append("<tr><td>");
					for (int i = 0; i < lanNum; i++) {
						if (tianyilanMap.get(String.valueOf(i + 1)) != null) {
							tianyiLeftsb.append("<input disabled='disabled' type='checkbox' name='tianyilanDis'  checked='checked' value='LAN"+ (i + 1) + "' /> ").append("LAN").append(i + 1);
							tianyiLeftsb.append(_NBSP);
							tianyiLeftsb_foot.append("<input type='checkbox' name='tianyilan' checked='checked' value='LAN"+ (i + 1) + "' /> ").append("LAN").append(i + 1);
							tianyiLeftsb_foot.append(_NBSP);
						} else {
							tianyiLeftsb.append("<input disabled='disabled' type='checkbox' name='tianyilanDis'  value='LAN"+ (i + 1) + "'/>").append("LAN").append(i + 1);
							tianyiLeftsb.append(_NBSP);
							tianyiLeftsb_foot.append("<input type='checkbox' name='tianyilan' value='LAN"+ (i + 1) + "'/>").append("LAN").append(i + 1);
							tianyiLeftsb_foot.append(_NBSP);
						}
					}
					for (int i = 0; i < wlanNum; i++) {
						if (tianyiwlanMap.get(String.valueOf(i + 1)) != null) {
							tianyiLeftsb.append("<input disabled='disabled' type='checkbox' name='tianyiwlanDis'  checked='checked' value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
							tianyiLeftsb.append(_NBSP);
//							tianyiLeftsb_foot.append("<input type='checkbox' name='tianyiwlan' checked='checked' value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
//							tianyiLeftsb_foot.append(_NBSP);
						} else {
							tianyiLeftsb.append("<input disabled='disabled' type='checkbox' name='tianyiwlanDis'  value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
							tianyiLeftsb.append(_NBSP);
//							tianyiLeftsb_foot.append("<input type='checkbox' name='tianyiwlan' value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
//							tianyiLeftsb_foot.append(_NBSP);
						}

					}
					tianyiLeftsb.append("</td></tr>");
					tianyiLeftsb_foot.append("</td></tr>");
					tianyiLeftsb_foot.append("</td><input type='hidden' name='tianyiusername' value='"+ tianyiList.get(k).get("username")+ "'><input type='hidden' name='tianyiuserid' value='"+ tianyiList.get(k).get("user_id")+ "'><input type='hidden' name='tianyiDevsn' value='"+ tianyiList.get(k).get("device_serialnumber")+ "'><input type='hidden' name='tianyioui' value='"+ tianyiList.get(k).get("oui")+ "'><input type='hidden' name='tianyideviceId' value='"+ tianyiList.get(k).get("device_id") + "'>");
					deviceId = tianyiList.get(k).get("device_id").toString();
				}
				int rsinttianyi = new SuperGatherCorba("1").getCpeParams(
						deviceId, 0, 3);
				if (rsinttianyi != 1) {
					logger.warn("天翼看店设备采集失败");
					tianyiRightsb.append("天翼看店设备采集失败<input type='hidden' name='tianyifail' value='0'>");
					tianyiRightsb_foot.append("天翼看店设备采集失败");
				} else {
					logger.warn("天翼看店设备采集成功");
					List<HashMap<String, String>> tianyi_list = bio.getTianyi(deviceId);
					for (int ki = 0; ki < tianyi_list.size(); ki++) {

						List<String> realtianyilanlist = new ArrayList<String>();
						Map<String, String> realtianyilanMap = new HashMap<String, String>();
						List<String> realtianyiwlanlist = new ArrayList<String>();
						Map<String, String> realtianyiwlanMap = new HashMap<String, String>();
						String[] bindport = tianyi_list.get(ki).get("bind_port").split(",");
						for (int j = 0; j < bindport.length; j++) {
							if (bindport[j].length() == 62) {
								realtianyilanlist.add(bindport[j]);
							}
							if (bindport[j].length() == 53) {
								realtianyiwlanlist.add(bindport[j]);
							}
							if (bindport[j].length() == 63) {
								realtianyilanlist.add(bindport[j].substring(0,bindport[j].length()-1));
							}
							if (bindport[j].length() == 54) {
								realtianyiwlanlist.add(bindport[j].substring(0,bindport[j].length()-1));
							}
						}

						for (int i = 0; i < realtianyilanlist.size(); i++) {
							realtianyilanMap.put(realtianyilanlist.get(i).substring(61), realtianyilanlist.get(i).substring(61));
						}
						for (int i = 0; i < realtianyiwlanlist.size(); i++) {
							realtianyiwlanMap.put(realtianyiwlanlist.get(i).substring(52), realtianyiwlanlist.get(i).substring(52));
						}
						tianyiRightsb.append("<tr><td>");
						tianyiRightsb_foot.append("<tr><td>");
						for (int i = 0; i < lanNum; i++) {
							if (realtianyilanMap.get(String.valueOf(i + 1)) != null) {
								tianyiRightsb.append("<input disabled='disabled' type='checkbox' checked='checked' value='LAN"+ (i + 1) + "'/>").append("LAN").append(i + 1);
								tianyiRightsb.append(_NBSP);
								tianyiRightsb_foot.append("<input type='checkbox' name='lantianyireal' checked='checked' value='LAN"+ (i + 1) + "'/>").append("LAN").append(i + 1);
								tianyiRightsb_foot.append(_NBSP);
							} else {
								tianyiRightsb.append("<input disabled='disabled' type='checkbox'  value='LAN"+ (i + 1) + "'/>").append("LAN").append(i + 1);
								tianyiRightsb.append(_NBSP);
								tianyiRightsb_foot.append("<input  type='checkbox' name='lantianyireal' value='LAN"+ (i + 1) + "'/>").append("LAN").append(i + 1);
								tianyiRightsb_foot.append(_NBSP);
							}
						}
						for (int i = 0; i < wlanNum; i++) {
							if (realtianyiwlanMap.get(String.valueOf(i + 1)) != null) {
								tianyiRightsb.append("<input disabled='disabled' type='checkbox'  checked='checked'  value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
								tianyiRightsb.append(_NBSP);
								tianyiRightsb_foot.append("<input type='checkbox'  checked='checked' name='wlantianyireal' value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
								tianyiRightsb_foot.append(_NBSP);
							} else {
								tianyiRightsb.append("<input disabled='disabled' type='checkbox'  value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
								tianyiRightsb.append(_NBSP);
								tianyiRightsb_foot.append("<input type='checkbox' name='wlantianyireal' value='WLAN"+ (i + 1) + "'/>").append("WLAN").append(i + 1);
								tianyiRightsb_foot.append(_NBSP);
							}
						}
					}
					tianyiRightsb.append("</td></tr>");
					tianyiRightsb_foot.append("</td></tr>");
				}
				realtianyiNameListHtml = tianyiRightsb_foot.toString();
				disrealtianyiNameListHtml = tianyiRightsb.toString();
				tianyiNameListHtml = tianyiLeftsb_foot.toString();
				notianyiNameListHtml = tianyiLeftsb.toString();
			}
			return "list";
		} else {
			ajax = "";
			return "ajax";
		}
	}

	@SuppressWarnings("rawtypes")
	public String update() {
		List<Map> internetList = bio.internetList(loid);
		String[] intV = interVal.split("@");
		Map<String, String> intMap = new HashMap<String, String>();
		for (int i = 0; i < intV.length; i++) {
			intMap.put(intV[i], intV[i]);
		}
		String[] itvV = itvVal.split("@");
		Map<String, String> itvMap = new HashMap<String, String>();
		for (int i = 0; i < itvV.length; i++) {
			itvMap.put(itvV[i], itvV[i]);
		}
		String[] tianyiV = tianyiVal.split("@");
		Map<String, String> tianyiMap = new HashMap<String, String>();
		for (int i = 0; i < tianyiV.length; i++) {
			tianyiMap.put(tianyiV[i], tianyiV[i]);
		}
		String[] vals = val.split("@");
		int count = 0;
		if ("10".equals(id)) {
			if (internetList .size() > 1 ) {
				String[] strings = interVal.split("#");
				List<Map<String, String>> list = new ArrayList<Map<String, String>>();
				for (int i = 0; i < strings.length; i++) {
					Map<String, String> map = new HashMap<String, String>();
					String[] temp = strings[i].split("@");
					for (int j = 0; j < temp.length; j++) {
						map.put(temp[j], temp[j]);
					}
					list.add(map);
				}
				int num = 0;
				for (int i = 0; i < list.size(); i++) {
					for (int j = 0; j < vals.length; j++) {
						if (null != list.get(i).get(vals[j])) {
							num++;
						}
					}
				}
				if (num > 0) {
					ajax = "请不要占用其他宽带端口或重复选择端口";
					return "ajax";
				}
				
				if (val.length() == 5 || val.length() == 6 || val.length() == 0) {
					for (int i = 0; i < vals.length; i++) {
						if (!StringUtil.IsEmpty(itvMap.get(vals[i]))
								|| !StringUtil.IsEmpty(tianyiMap.get(vals[i]))) {
							count++;
							break;
						}
					}
				}else {
					ajax="多宽带只能选择1个端口";
					return "ajax";
				}
			}else {
				for (int i = 0; i < vals.length; i++) {
//					if ("LAN4".equals(vals[i])) {
//						ajax="请不要占用天翼看店端口！";
//						return "ajax";
//					}
					if (!StringUtil.IsEmpty(itvMap.get(vals[i]))
							|| !StringUtil.IsEmpty(tianyiMap.get(vals[i]))) {
						count++;
						break;
					}
				}
			}
		}
		if ("11".equals(id)) {
			for (int i = 0; i < vals.length; i++) {
				if (!StringUtil.IsEmpty(intMap.get(vals[i]))
						|| !StringUtil.IsEmpty(tianyiMap.get(vals[i]))) {
					count++;
					break;
				}
			}
			if (vals.length != itvMap.size()) {
				ajax = "请保持ITV端口数相同";
				return "ajax";
			}
		}
		if ("25".equals(id)) {
			if (vals.length != 1) {
				ajax="天翼看店只能选择一个端口";
				return "ajax";
			} else {
				for (int i = 0; i < vals.length; i++) {
//					if (!"LAN4".equals(vals[i])) {
//						ajax="请选择正确的天翼看店端口";
//						return "ajax";
//					}
					if (!StringUtil.IsEmpty(intMap.get(vals[i]))
							|| !StringUtil.IsEmpty(itvMap.get(vals[i]))) {
						count++;
						break;
					}
				}
			}
		}
		if (count > 0) {
			ajax = "手工配置业务端口已有端口被占用";
		} else {
			int wlanNum = bio.wlanNum(loid);
			int lanNum = bio.lanNum(loid);
			int result  = 0;
			if ("11".equals(id)) {
				List<Map> itvList = bio.itvList(loid);
				for (int i = 0; i < itvList.size(); i++) {
					String user_name = itvList.get(i).get("username").toString();
					String user_id = itvList.get(i).get("user_id").toString();
					result = bio.update(id, loid, val, wlanNum, lanNum, user_id,user_name);
				}
			}else {
				result = bio.update(id, loid, val, wlanNum, lanNum, userid,username);
			}
			if (result > 0) {
				PreServInfoOBJ preInfoObj = new PreServInfoOBJ(userid,deviceId, oui, devSn, id, "1");
				CreateObjectFactory.createPreProcess("1").processServiceInterface(CreateObjectFactory.createPreProcess().GetPPBindUserList(preInfoObj));
				ajax = "修改成功，下发成功";
			} else {
				ajax = "修改失败，未下发";
			}
		}
		return "ajax";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setSession(Map session) {
		this.session = session;

	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getOui() {
		return oui;
	}

	public void setOui(String oui) {
		this.oui = oui;
	}

	public String getDevSn() {
		return devSn;
	}

	public void setDevSn(String devSn) {
		this.devSn = devSn;
	}

	@SuppressWarnings("rawtypes")
	public Map getSession() {
		return session;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getLoid() {
		return loid;
	}

	public void setLoid(String loid) {
		this.loid = loid;
	}

	public String getInterentNameListHtml() {
		return interentNameListHtml;
	}

	public void setInterentNameListHtml(String interentNameListHtml) {
		this.interentNameListHtml = interentNameListHtml;
	}

	public String getItvNameListHtml() {
		return itvNameListHtml;
	}

	public void setItvNameListHtml(String itvNameListHtml) {
		this.itvNameListHtml = itvNameListHtml;
	}

	public String getTianyiNameListHtml() {
		return tianyiNameListHtml;
	}

	public void setTianyiNameListHtml(String tianyiNameListHtml) {
		this.tianyiNameListHtml = tianyiNameListHtml;
	}

	public String getRealinterentNameListHtml() {
		return realinterentNameListHtml;
	}

	public void setRealinterentNameListHtml(String realinterentNameListHtml) {
		this.realinterentNameListHtml = realinterentNameListHtml;
	}

	public String getRealitvNameListHtml() {
		return realitvNameListHtml;
	}

	public void setRealitvNameListHtml(String realitvNameListHtml) {
		this.realitvNameListHtml = realitvNameListHtml;
	}

	public String getRealtianyiNameListHtml() {
		return realtianyiNameListHtml;
	}

	public void setRealtianyiNameListHtml(String realtianyiNameListHtml) {
		this.realtianyiNameListHtml = realtianyiNameListHtml;
	}

	public String getNointerentNameListHtml() {
		return nointerentNameListHtml;
	}

	public void setNointerentNameListHtml(String nointerentNameListHtml) {
		this.nointerentNameListHtml = nointerentNameListHtml;
	}

	public String getNoitvNameListHtml() {
		return noitvNameListHtml;
	}

	public void setNoitvNameListHtml(String noitvNameListHtml) {
		this.noitvNameListHtml = noitvNameListHtml;
	}

	public String getNotianyiNameListHtml() {
		return notianyiNameListHtml;
	}

	public void setNotianyiNameListHtml(String notianyiNameListHtml) {
		this.notianyiNameListHtml = notianyiNameListHtml;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getInterVal() {
		return interVal;
	}

	public void setInterVal(String interVal) {
		this.interVal = interVal;
	}

	public String getItvVal() {
		return itvVal;
	}

	public void setItvVal(String itvVal) {
		this.itvVal = itvVal;
	}

	public String getTianyiVal() {
		return tianyiVal;
	}

	public void setTianyiVal(String tianyiVal) {
		this.tianyiVal = tianyiVal;
	}

	public String getDisrealinterentNameListHtml() {
		return disrealinterentNameListHtml;
	}

	public void setDisrealinterentNameListHtml(
			String disrealinterentNameListHtml) {
		this.disrealinterentNameListHtml = disrealinterentNameListHtml;
	}

	public String getDisrealitvNameListHtml() {
		return disrealitvNameListHtml;
	}

	public void setDisrealitvNameListHtml(String disrealitvNameListHtml) {
		this.disrealitvNameListHtml = disrealitvNameListHtml;
	}

	public String getDisrealtianyiNameListHtml() {
		return disrealtianyiNameListHtml;
	}

	public void setDisrealtianyiNameListHtml(String disrealtianyiNameListHtml) {
		this.disrealtianyiNameListHtml = disrealtianyiNameListHtml;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getUserList() {
		return userList;
	}

	@SuppressWarnings("rawtypes")
	public void setUserList(List<Map> userList) {
		this.userList = userList;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getVlanList() {
		return vlanList;
	}

	@SuppressWarnings("rawtypes")
	public void setVlanList(List<Map> vlanList) {
		this.vlanList = vlanList;
	}

	public int getInterleftNum() {
		return interleftNum;
	}

	public void setInterleftNum(int interleftNum) {
		this.interleftNum = interleftNum;
	}

	public int getInterrightNum() {
		return interrightNum;
	}

	public void setInterrightNum(int interrightNum) {
		this.interrightNum = interrightNum;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getUserMap() {
		return userMap;
	}

	@SuppressWarnings("rawtypes")
	public void setUserMap(List<Map> userMap) {
		this.userMap = userMap;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getVlanMap() {
		return vlanMap;
	}

	@SuppressWarnings("rawtypes")
	public void setVlanMap(List<Map> vlanMap) {
		this.vlanMap = vlanMap;
	}

	public ManuallyConfigurePortBusinessBIO getBio() {
		return bio;
	}

	public void setBio(ManuallyConfigurePortBusinessBIO bio) {
		this.bio = bio;
	}

}
