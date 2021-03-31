/*
 * @(#)LipossGlobals.java	1.00 1/5/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.jms.MQConfigParser;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.xml.XMLProperties;
import com.linkage.litms.resource.DoBusinessObject;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.SocketChannelClient;

/**
 * 初始化Web系统版本信息，加载Web配置文件：liposs_init.properties、liposs_cfg.xml。
 *
 * @author yuht
 * @version 1.00, 1/5/2006
 * @since Liposs 2.1
 */

public class LipossGlobals {
	private static Logger m_logger = LoggerFactory.getLogger(LipossGlobals.class);

	/** Web 配置文件名称 */
	private static final String LIPOSS_CONFIG_FILENAME = "liposs_cfg.xml";

	/** 存放XML配置文件变量 */
	private static XMLProperties properties = null;

	/** server 部署路径 */
	public static String G_ServerHome = null;

	/** 初始化Corba对象 */
	public static Map G_corbaObject = new HashMap();

	/** 初始化业务对应关系的MAP对象 */
	public static Map G_Servcie_operator = new HashMap();

	/** 批量IPTV业务配置入库SQL */
	public static List<String> ALL_SQL_IPTV = new ArrayList<String>();

	/** 批量IPTV业务tab_gw_device数据 */
	public static Map<String, String> ALL_DEV_MAP = new HashMap<String, String>();

	/** 新疆绑定或建档成功后，下发业务 */
	public static List<DoBusinessObject> DoBusinessObjList = new ArrayList<DoBusinessObject>();
	/** 手工工单用到的socket channel客户端 */
	public static SocketChannelClient socketChannelClient = null;
	
	/** 配置参数 */
	private static String GW_PROTOCOL = "GwProtocol";
	private static String SLEEP_TIME = "sleepTime";
	private static String SYSTEM_TYPE = "SystemType";
	private static String ACCESS_TYPE_FROM = "accessTypeFrom";
	private static String SHORT_NAME = "InstArea.ShortName";
	private static String CHKINSTUSER = "chkinstuser";
	private static String NOTITY = "midware.notify";

	private static String ZERO = "0";
	private static String ONE = "1";
	private static String TWO = "1";

	
	//用于新疆单点登录
	/**
	<ct_login>
		<!-- 网厅分配给业务系统的ID -->
		<bsid>10003</bsid>
		<!-- 网厅分配给BBMS系统的密钥 -->
		<key>ABE8764F43877900ABE8764F</key>
		<!-- 网厅认证页面 -->
		<ct_auth_url>http://localhost:9009/itms3/ctAuth</ct_auth_url>
		<!-- 网厅首页面 -->
		<ct_main_url>http://www.xj.ct10000.com</ct_main_url>
		<!-- BBMS认证路径 -->
		<bbms_auth_url>/bbmsAuth</bbms_auth_url>
		<!-- BBMS提供给网厅的业务主页面 -->
		<bbms_main_url>/index</bbms_main_url>
	</ct_login>
	 */
	public static SocketChannelClient getSocketChannelClientInstance()
	{
		if(socketChannelClient == null)
		{
			socketChannelClient = SocketChannelClient.getSocketChannelClientInstance(Global.G_ITMS_Sheet_Server , Global.G_ITMS_Sheet_Port );
		}
		return socketChannelClient;
	}
	/** 网厅分配给业务系统的ID */
	public static String getBsid(){
		return getLipossProperty("ct_login.bsid");
	}
	/** 网厅分配给BBMS系统的密钥 */
	public static String getKey(){
		return getLipossProperty("ct_login.key");
	}
	/** 网厅认证页面路径 */
	public static String getCtAuthUrl(){
		return getLipossProperty("ct_login.ct_auth_url");
	}
	/** 网厅首页面路径 */
	public static String getCtMainUrl(){
		return getLipossProperty("ct_login.ct_main_url");
	}
	/** BBMS认证路径 */
	public static String getBbmsAuthUrl(){
		return getLipossProperty("ct_login.bbms_auth_url");
	}
	/** BBMS提供给网厅用户的业务主页面路径 */
	public static String getBbmsMainUrl(){
		return getLipossProperty("ct_login.bbms_main_url");
	}
	/** BBMS提供给网厅的错误页面(用户不存在) */
	public static String getBbmsErrUrl(){
		return getLipossProperty("ct_login.bbms_err_url");
	}

	/**
	 * 构造Liposs Web 系统版本信息
	 *
	 * @return 返回系统版本信息字符串
	 */
	public static String getLipossVersion() {
		return getLipossProperty("Version");
	}

	/**
	 * 从liposs_init.properties 属性文件中读取系统部署路径
	 *
	 * @return 返回系统部署路径字符串
	 */
	public static String getLipossHome() {
		return G_ServerHome;
	}

	/**
	 * 获取模块标识
	 * @return
	 * @author zhangsm
	 */
	public static String getClientId(){
//		return getLipossProperty("mq.clientId");
		if(Global.G_MQPoolPath == null){
			return getLipossProperty("mq.clientId");
		}
		return MQConfigParser.getClientId(Global.G_MQPoolPath);
	}

	/**
	 * 读取系统部署名称
	 *
	 * @return 返回系统部署名称字符串
	 */
	public static String getLipossName() {
		return getLipossProperty("ServerName");
	}

	/**
	 * 获取网关设备管理协议
	 * @return
	 * 		<li>0: All(所有);</li>
	 * 		<li>1: TR069;</li>
	 * 		<li>2: SNMP.</li>
	 */
	public static int getGwProtocol() {
		if (null == getLipossProperty(GW_PROTOCOL)) {
			return 0;
		}

		try {
			return Integer.parseInt(getLipossProperty(GW_PROTOCOL));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * 获取调用ACS前休眠的时间
	 * @return	时间(second)
	 */
	public static int getSleepTime(){
		int tmp = 5000;
		//默认为ITMS
		if (null == getLipossProperty(SLEEP_TIME)) {
			return 5000;
		}
		try {
			tmp = Integer.parseInt(getLipossProperty(SLEEP_TIME));
		} catch (NumberFormatException e) {
			return 5000;
		}
		return tmp;
	}

	/**
	 * 系统类型
	 * @return
	 * 		<li>1:家庭网关(默认值)</li>
	 * 		<li>2:企业网关</li>
	 * 		<li>0:家庭网关&企业网关</li>
	 */
	public static int SystemType(){
		int tmp = 1;
		int tmpUpperLimit = 2;
		int tmpLowerLimit = 0;
		//默认为ITMS
		if (null == getLipossProperty(SYSTEM_TYPE)) {
			return 1;
		}
		try {
			tmp = Integer.parseInt(getLipossProperty(SYSTEM_TYPE));
		} catch (NumberFormatException e) {
			return 1;
		}
		
		if (tmp > tmpUpperLimit || tmp < tmpLowerLimit) {
			return 1;
		} else {
			return tmp;
		}
	}

	/**
	 * 查询accessType方式
	 * @return
	 *   <li>1:从gw_wan表获取(默认值)</li>
	 * 	 <li>2:从tab_devicetype_info表获取</li>
	 */
	public static int accessTypeFrom(){
		int tmp = 1;
		int tmpUpperLimit = 2;
		int tmpLowerLimit = 0;
		//默认为从gw_wan表查询
		if (null == getLipossProperty(ACCESS_TYPE_FROM)) {
			return 1;
		}
		try {
			tmp = Integer.parseInt(getLipossProperty(ACCESS_TYPE_FROM));
		} catch (NumberFormatException e) {
			return 1;
		}
		if (tmp > tmpUpperLimit || tmp < tmpLowerLimit) {
			return 1;
		} else {
			return tmp;
		}
	}

	/**
	 * 是否为企业网关
	 * LipossGlobals.java
	 * @return
	 * boolean
	 * @author zhaixf
	 */
	public static boolean IsETMS(){
		//默认为ITMS
		if (null == getLipossProperty(SYSTEM_TYPE)) {
			return false;
		}
		return TWO.equals(getLipossProperty(SYSTEM_TYPE));
	}

	/**
	 * 是否为家庭网关
	 * LipossGlobals.java
	 * @return
	 * boolean
	 * @author zhaixf
	 */
	public static boolean IsITMS(){
		//默认为ITMS
		if (null == getLipossProperty(SYSTEM_TYPE)) {
			return true;
		}
		return ONE.equals(getLipossProperty(SYSTEM_TYPE));
	}
	/**
	 * 是否为家庭网关
	 * 为适应融合版本重写的方法  2012-06-25
	 * LipossGlobals.java
	 * @return
	 * boolean
	 * @author zhangsm
	 */
	public static boolean IsITMS(String gwType){
		//默认为ITMS
		if (null == getLipossProperty(SYSTEM_TYPE)) {
			return ONE.equals(gwType);
		}
		else if(ZERO.equals(getLipossProperty(SYSTEM_TYPE)))
		{
			return ONE.equals(gwType);
		}
		return ONE.equals(getLipossProperty(SYSTEM_TYPE));
	}
	/**
	 * 是否为所有网关
	 * LipossGlobals.java
	 * @return
	 * boolean
	 * @author yanhj
	 */
	public static boolean IsATMS(){
		//默认为ITMS
		if (null == getLipossProperty(SYSTEM_TYPE)) {
			return false;
		}
		return ZERO.equals(getLipossProperty(SYSTEM_TYPE));
	}

	/**
	 * 获取是否为新疆电信，如果是返回true
	 * LipossGlobals.java
	 * @return
	 * boolean
	 * @author zhaixf
	 */
	public static boolean isXJDX(){
		return inArea(Global.XJDX);
	}

	/**
	 * 获取是否为甘肃电信，如果是返回true
	 * LipossGlobals.java
	 * @return
	 * boolean
	 * @author zhaixf
	 */
	public static boolean isGSDX(){
		return inArea(Global.GSDX);
	}

	public static boolean isSXLT(){
		return inArea(Global.SXLT);
	}
	
	/**
	 * 是否是吉林联通
	 * @return
	 */
	public static boolean isJlLt(){
		return inArea(Global.JLLT);
	}
	
	/**
	 * 是否是重庆电信
	 * @return
	 */
	public static boolean isCqDx(){
		return inArea(Global.CQDX);
	}
	
	/**
	 * 是否是安徽电信
	 * @return
	 */
	public static boolean isAhDx(){
		return inArea(Global.AHDX);
	}
	
	/**
	 * 是否是胡南联通
	 * @return
	 */
	public static boolean isHnLt(){
		return inArea(Global.HNLT);
	}
	
	/**
	 * 是否是内蒙古电信
	 * @return
	 */
	public static boolean isNmgDx(){
		return inArea(Global.NMGDX);
	}
	
	/**
	 * 是否是河北联通
	 * @return
	 */
	public static boolean isHbLt(){
		return inArea(Global.HBLT);
	}
	
	/**
	 * 是否是宁夏电信
	 * @return
	 */
	public static boolean isNxDx(){
		return inArea(Global.NXDX);
	}
	
	/**
	 * <pre>
	 * 判断当前项目部署区域是否为请求参数<code>shortNames</code>之中。
	 * 1.在liposs_cfg.xml配置文件中必须配置区域信息，否则抛出IllegalStateException异常
	 * 2.如果配置文件中配置的区域信息在请求参数区域之中，返回true，否则返回false。
	 * </pre>
	 * @param  shortNames 区域名称编码，多个编码之间用英文逗号分隔
	 * @return 如果当前项目部署区域在参数区域名称之中，返回true，否则返回false
	 * @throws IllegalStateException 当在liposs_cfg.xml配置文件中没有配置区域信息
	 */
	public static boolean inArea(String shortNames)
	{
		String currentArea = getLipossProperty(SHORT_NAME);
		if (StringUtil.IsEmpty(currentArea))
		{
			m_logger.error("the value of [InstArea.ShortName] in liposs_cfg.xml is null");
			throw new IllegalStateException("the value of [InstArea.ShortName] in liposs_cfg.xml is null");
		}

		if (!StringUtil.IsEmpty(shortNames))
		{
			String[] names = shortNames.split(",");
			for (String name : names)
			{
				if (currentArea.equals(name))
				{
					return true;
				}
			}
		}

		return false;
	}
	/**
	 * <pre>
	 * 判断当前项目部署区域是否为请求参数<code>operatorNames</code>之中。
	 * 1.在liposs_cfg.xml配置文件中必须配置区域信息，否则抛出IllegalStateException异常
	 * 2.如果配置文件中配置的区域信息在请求参数区域之中，返回true，否则返回false。
	 * </pre>
	 * @param operatorNames 区域名称编码，多个编码之间用英文逗号分隔
	 * @return 如果当前项目部署区域在参数区域名称之中，返回true，否则返回false
	 * @throws IllegalStateException 当在liposs_cfg.xml配置文件中没有配置区域信息
	 */
	public static boolean inOperator(String operatorNames)
	{
		String currentArea = getLipossProperty("telecom");
		if (StringUtil.IsEmpty(currentArea))
		{
			m_logger.error("the value of [telecom] in liposs_cfg.xml is null");
			throw new IllegalStateException("the value of [telecom] in liposs_cfg.xml is null");
		}

		if (!StringUtil.IsEmpty(operatorNames))
		{
			String[] names = operatorNames.split(",");
			for (String name : names)
			{
				if (currentArea.equals(name))
				{
					return true;
				}
			}
		}

		return false;
	}
	/**
	 * 获取是否为Oracle数据库，如果是返回true
	 *
	 * @return boolean
	 * @author zhangcong
	 */
	public static boolean isOracle()
	{
		if(Global.DB_ORACLE == DBUtil.GetDB())
		{
			return true;
		}
		return false;
	}


	/**
	 * 获取现场安装，用户不存在时提醒，并不做绑定操作
	 * @return
	 * 		<li>0: 不检查</li>
	 * 		<li>1: 检查</li>
	 * @author zhaixf
	 */
	public static int getChkInstUser() {
		if (null == getLipossProperty(CHKINSTUSER)) {
			return 0;
		}

		try {
			return Integer.parseInt(getLipossProperty(CHKINSTUSER));
		} catch (NumberFormatException e) {
			return 0;
		}
	}


	/**
	 * 是否调用中间件
	 * @return
	 */
	public static boolean getMidWare() {
		if (null == getLipossProperty(NOTITY)) {
			return false;
		}

		try {
			return Boolean.parseBoolean(getLipossProperty(NOTITY));
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 获取指点名称的值
	 *
	 * @param name
	 *            属性名称字符串
	 * @return 返回指点名称的值
	 */
	public static String getLipossProperty(String name) {
		if (properties == null) {
			loadProperties();
		}

		return properties.getProperty(name);
	}

	/**
	 * 设置指点名称的值
	 *
	 * @param name
	 *            属性名称字符串
	 * @param value
	 *            属性值字符串
	 */
	public static void setLipossProperty(String name, String value) {
		if (properties == null) {
			loadProperties();
		}

		properties.setProperty(name, value);
	}

	/**
	 * 载入XML配置文件到XMLProperties实例中
	 */
	private synchronized static void loadProperties() {
		if (properties == null) {
			if (G_ServerHome == null) {
				G_ServerHome = LipossGlobals.getLipossHome();
			}
			m_logger.debug(G_ServerHome);
			String path = G_ServerHome + File.separator + "WEB-INF"
			+ File.separator + "classes" + File.separator
			+ LIPOSS_CONFIG_FILENAME;
			m_logger.debug(path);
			properties = new XMLProperties(path);
		}
	}

	/**
	 * 重新加载配置文件liposs_cfg.xml内容到XMLProperties实例中
	 *
	 */
	public void reload() {
		properties = null;
		loadProperties();
	}

	/**
	 * 报表生成工具
	 * 策略配置默认参数
	 * 发送类型
	 * @return
	 */
	public static String getRrctPolicySendType() {
	    String value = getLipossProperty("rrct.policy.send_type");
	    return value;
	}
	/**
	 * 报表生成工具
	 * 策略配置默认参数
	 * 策略
	 * @return
	 */
	public static String getRrctPolicyValue() {
	    String value = getLipossProperty("rrct.policy.value");
	    return value;
	}


	/**
	 * 获取设备对应的gw_type<br>必须保证设备存在
	 * @param device_id
	 * @return int
	 */
	public static String getGw_Type(String deviceId)
	{
		//获取设备类型
	    String strSql0 = "select gw_type from tab_gw_device where device_id='" + deviceId + "'";
	    PrepareSQL psql0 = new PrepareSQL(strSql0);
    	psql0.getSQL();
    	//查询结果肯定不为null
	    HashMap record0 = DataSetBean.getRecord(strSql0);
	    return StringUtil.getStringValue(record0.get("gw_type"));
	}

	/**
	 * 是否启用超级权限<br>
	 * 由于部分据点目前不使用超级权限，故增加系统全局配置
	 * @return 当在配置文件中配置为true时，即启用超级权限，返回true，否则返回false
	 */
	public static boolean enableSuperAuth()
	{
		String config = getLipossProperty("enableSuperAuth");
		boolean result = "true".equalsIgnoreCase(config);
		m_logger.warn("the value of enableSuperAuth in liposs_cfg.xml is [{}]", result);
		return result;
	}

}


class InitPropLoader {
	private Properties initProps = new Properties();

	private InputStream in = null;

	private String lipossHome = null;

	private String lipossName = null;

	InitPropLoader() {
		try {
			in = getClass().getResourceAsStream("/liposs_init.properties");
			initProps.load(in);
		} catch (Exception e) {
//			m_logger.error("Error reading Linkage properties "
//					+ "in NetworkGlobals");
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
			}
		}
	}

	public String getLipossHome() {
		String slash = "/";
		String backSlash = "\\";
		
		if (initProps != null) {
			lipossHome = initProps.getProperty("lipossHome");

			if (lipossHome != null) {
				lipossHome = lipossHome.trim();

				while (lipossHome.endsWith(slash) || lipossHome.endsWith(backSlash)) {
					lipossHome = lipossHome.substring(0,
							lipossHome.length() - 1);
				}
			}
		}
		return lipossHome;
	}

	public String getLipossName() {
		if (initProps != null) {
			lipossName = initProps.getProperty("lipossName");

			try {
				lipossName = new String(lipossName.getBytes("ISO8859_1"), "GBK");
			} catch (Exception e) {
				lipossName = "出错";
			}
		}

		return lipossName;
	}



}
