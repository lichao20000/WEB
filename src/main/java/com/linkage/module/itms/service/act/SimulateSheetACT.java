package com.linkage.module.itms.service.act;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.service.bio.SimulateSheetBIO;
import com.linkage.module.itms.service.obj.SheetObj4CQ;

/**
 * 
 * @author zhangshimin(工号)
 * @version 1.0
 * @since 2011-5-21 下午02:24:11
 * @category com.linkage.module.itms.service.act
 * @copyright 南京联创科技 网管科技部
 * 
 */
public class SimulateSheetACT implements SessionAware {
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(SimulateSheetACT.class);
	// 业务类型
	private String servTypeId;
	// 操作类型
	private String operateType;
	// 工单受理时间
	private String dealdate;
	// 设备类型
	private String devType;
	// 用户类型
	private String userType;
	// 逻辑SN
	private String username;
	// 属地
	private String cityId;
	// 局向
	private String officeId = "";
	// 小区
	private String zoneId = "";
	// 接入方式 (订单类型)
	private int orderType;
	// 联系人
	private String linkman;
	// 联系人电话
	private String linkphone;
	// 联系人Email
	private String linkEmail;
	// 联系人手机
	private String linkMobile;
	// 家庭住址
	private String homeAddr;
	// 身份证号码
	private String credNo;
	// 宽带账号
	private String netUsername;
	// 宽带密码,ADSL方式必须项
	private String netPassword;
	// vlanId LAN和PON上行时必须
	private String vlanId;

	private String vpi;

	private String vci;
	// voip认证帐号
	private String voipUsername;
	// voip认证密码
	private String voipPasswd;
	// SIP服务器地址
	private String sipIp;
	// SIP服务器端口
	private int sipPort;
	// SIP备用服务器地址
	private String standSipIp;
	// SIP备用服务器端口
	private int standSipPort;

	// 终端标识类型
	private String regIdType;

	// add by zhangsm 20111101 ims
	private String registrarServer;

	private int registrarServerPort;

	private String standRegistrarServer;

	private int standRegistrarServerPort;

	private String outboundProxy;

	private int outboundProxyPort;

	private String standOutboundProxy;

	private int standOutboundProxyPort;

	private int protocol;

	private String maxDownSpeed;

	private String maxUpSpeed;

	private String maxNetNum;

	private String oui;

	private String deviceSn;

	private String adslPhone;
	private int iptvNum;
	// 终端开通的线路端口：V1, V2
	private String linePort;
	// 业务电话号码
	private String voipTelepone;
	// h248 终端标识
	private String regId;
	// 开通业务个数
	protected int servNum;
	// 开通的端口：L1,L2,L3,L4
	protected String iptvLanPort;
	// IPTV宽带接入账号
	private String iptvUsername;
	// 客户Id
	private String customerId;
	// 客户帐号
	private String customerAccount;
	// 客户密码
	private String customerPwd;
	// 家庭网关1、企业网关2
	private String gw_type;
	// 上网方式
	private String netType;
	// ip地址
	private String ipAddress;
	// 掩码
	private String mask;
	// 网关
	private String gateway;
	// DSN
	private String dsn;
	// Session
	private Map session;
	// ajax
	private String ajax;

	private String instAreaName;

	private String loid;

	private String netway;
	private String dns;
	private String code;
	private String ipaddress;
	private String useriptype;
	private String wlantype;

	private String iptvUserName;
	private String userNameVal;
	
	
	private String ordertime;
	private String orderself;
	private String devicewan;
	private String usertype;
	private String subareacode;
	private String olduserid;
	private String deviceid;
	
	private String adaccount;
	private String useraddress;
	private String userid;
	
	private String contactperson;
	private String phonenumber;
	private String migration;

	/* 业务字段 */
	private String rgportid;
	private String vlanid;
	private String rgmode;
	private String adpassword;
	
	private String pvcvpi;
	private String pvcvci;
	private String multidevicemode;
	private String pcnumbers;
	private String keepuserinfo;
	private String IPV6Enable;
	private String AFTRAddress;
	private String dhcpenable;
	private String awifiportid;
	
	/*iptv*/
	private String iptvrgportid;
	private String multivlanmode;
	private String snoopingEnable;
	
	/*voip ims sip*/
	private String protocoltype;
	private String imsdnsip;
	private String imsdnsbakip;
	private String mgip;
	private String mgmask;
	private String mggw;
	private String proxyserverip;
	private String proxyserverport;
    private String proxyserverbakip;
    private String proxyserverbakport;
	private String registrarserverip;
	private String registrarserverport;
	private String standbyregistrarserverip;
	private String standbyregistrarserverport;
	private String sipuri;
	private String usertid;
	private String authusername;
	private String authpassword;
	
//	voip h248
	private String PhonenumberSip;
	private String ssip;
	private String ssport;
	private String ssbakip;
	private String ssbakport;
	private String mgDomain;
	
	/*宽带改账号*/
	private String newadaccount;
	private String customerid;
	
	/*stbiptv*/
	private String pppoeid ;
	private String pppoepassword ;
	private String dhcppassword ;
	private String dhcpid ;
	private String iptvuserid ;
	private String iptvpassword ;
	private String authurl ;
	private String authurl_Backup ;
	
	/*stbims*/
	private String imsid;
	private String imspassword;
	private String bacdomain;
	private String bacaddress1;
	private String bacaddress2;
	

	List<Map> listMap = null;

	private String nameListHtml;

	List list = null;
	private int count;

	private SimulateSheetBIO bio;
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;

	public String init() {
		logger.debug("excute()");
		instAreaName = Global.instAreaShortName;
		return "success";
	}

	public String initLoid() {
		int result = 0;
		if (!StringUtil.IsEmpty(loid)) {
			result = bio.qyeryLoid(loid);
		}
		if (result > 0) {
			ajax = String.valueOf(result);
			return "ajax";
		} else {
			ajax = "请输入正确的LOID";
			return "ajax";
		}
	}
	
	public String checkLoid4CQ() {
		if (!StringUtil.IsEmpty(loid)) {
			ajax = bio.qryLoid4CQ(loid);
			return "ajax";
		}
		else {
			ajax = "0";
			return "ajax";
		}
	}

	public String checkIptvUser() {
		int result = bio.checkIptvUser(iptvUserName, loid);
		ajax = String.valueOf(result);
		return "ajax";
	}

	private int getFreeLanPortNum(String userId, int type, int custType,
			int servType) {
		int num = 0;
		int lanTol = this.getPortNum(userId, type, custType);
		int moniterNum = getServNum(userId, 25);
		if (10 == servType) {
			num = lanTol - getServNum(userId, 11) - moniterNum;
		}
		if (11 == servType) {
			num = lanTol - getServCount(userId, 10).size() - moniterNum;
		}

		return num;
	}

	/**
	 * 查询有几个业务，
	 * 
	 * @param username
	 * @param servTypeId
	 * @return
	 */
	private ArrayList<HashMap<String, String>> getServCount(String username,
			int servTypeId) {
		logger.debug("getServNum({},{})", new Object[] { username, servTypeId });
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		/** * 判断账号的存在性 */
		if (StringUtil.IsEmpty(username, true)) {
			logger.warn("用户账号为空");
			return result;
		}
		PrepareSQL psql = new PrepareSQL();
		psql.append("select b.username,b.vlanid  from tab_hgwcustomer a,hgwcust_serv_info b where ");
		psql.append(" a.user_id=b.user_id and a.user_state in ('1','2')  and a.username=? and b.serv_type_id="
				+ servTypeId);
		psql.setString(1, username);
		// 查询
		result = DBOperation.getRecords(psql.getSQL());
		// Map<String,String> map = DBOperation.getRecord(psql.getSQL());
		// if (null != map && !map.isEmpty()) {
		// servNum = StringUtil.getIntValue(map, "serv_count");
		// }
		return result;
	}

	/**
	 * 根据用户名和业务类型获取业务绑定端口个数
	 * 
	 * @author zhangsm 2012-1-5
	 * @param username
	 * @param servTypeId
	 * @return
	 */
	private int getServNum(String username, int servTypeId) {
		logger.debug("getServNum({},{})", new Object[] { username, servTypeId });
		int servNum = 0;
		/** * 判断账号的存在性 */
		if (StringUtil.IsEmpty(username, true)) {
			logger.warn("用户账号为空");
			return 0;
		}
		PrepareSQL psql = new PrepareSQL();
		psql.append("select sum(b.serv_num) serv_num from tab_hgwcustomer a,hgwcust_serv_info b where ");
		psql.append(" a.user_id=b.user_id and a.user_state in ('1','2')  and a.username=? and b.serv_type_id="
				+ servTypeId);
		psql.setString(1, username);
		// 查询
		Map<String, String> map = DBOperation.getRecord(psql.getSQL());
		if (null != map && !map.isEmpty()) {
			servNum = StringUtil.getIntValue(map, "serv_num");
		}
		return servNum;
	}

	/**
	 * 根据userId获取端口口总数
	 * 
	 * @author zhangsm 2012-7-5
	 * @param username
	 * @param type
	 *            1:lan 2:voice 3:wlan
	 * @return
	 */
	private int getPortNum(String userId, int type, int custType) {
		logger.debug("getLANTotalNum({},{})", new Object[] { userId, type });
		String tableName = "tab_hgwcustomer";
		if (custType == 2) {
			tableName = "tab_egwcustomer";
		}
		int lanNum = 0;
		PrepareSQL psql = new PrepareSQL();
		// 语音口
		if (type == 1) {
			psql.append("select a.voice_num  as num  from tab_bss_dev_port a,"
					+ tableName
					+ " b where a.id=b.spec_id and b.user_id=(select user_id from tab_hgwcustomer where username ='"
					+ userId + "')");
		}// wlan
		else if (type == 3) {
			psql.append("select a.wlan_num as num  from tab_bss_dev_port a,"
					+ tableName
					+ " b where a.id=b.spec_id and b.user_id=(select user_id from tab_hgwcustomer where username ='"
					+ userId + "')");
		}// lan口
		else {
			psql.append("select a.lan_num as num  from tab_bss_dev_port a,"
					+ tableName
					+ " b where a.id=b.spec_id and b.user_id=(select user_id from tab_hgwcustomer where username ='"
					+ userId + "')");
		}
		// 查询
		Map<String, String> map = DBOperation.getRecord(psql.getSQL());
		if (null != map && !map.isEmpty()) {
			lanNum = StringUtil.getIntValue(map, "num");
		}
		return StringUtil.getIntegerValue(lanNum);
	}

	public String showSheet() {
		logger.debug("showSheet()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		dealdate = getNowDate();
		instAreaName = Global.instAreaShortName;
		logger.warn("showSheet:" + instAreaName);
		// 终端资料接口
		if (20 == StringUtil.getIntegerValue(servTypeId)) {
			if ("1".equals(operateType)) {
				logger.warn("资料接口工单");
				return "open";
			} else if ("3".equals(operateType)) {
				logger.warn("全业务销户");
				return "closeall";
			} else {
				ajax = "未知操作类型";
				return "ajax";
			}
		}// 上网业务
		else if (22 == StringUtil.getIntegerValue(servTypeId)) {
			if ("1".equals(operateType)) {
				logger.warn("上网业务开户工单");
				return "netopen";
			} else if ("3".equals(operateType)) {
				logger.warn("上网业务销户工单");
				return "netstop";
			} 
			//重庆改账号
			else if ("7".equals(operateType)) {
				logger.warn("上网业务改账号工单");
				return "netAlterAccount";
			} 
			else if ("15".equals(operateType)) {
				logger.warn("宽带信息修改工单");
				return "netMsgAlter";
			} 
			else if ("20".equals(operateType)) {
				logger.warn("解除绑定工单");
				return "netUnbind";
			} 
			else if ("6".equals(operateType)) {
				logger.warn("更改设备ID工单");
				return "netAlterDeviceid";
			} 
			else if ("9".equals(operateType)) {
				logger.warn("更改模式");
				return "netAlterMode";
			} 
			else if ("10".equals(operateType)) {
				logger.warn("更改密码");
				return "netAlterPasswd";
			}
			else if ("18".equals(operateType)) {
				logger.warn("修改客户信息");
				return "netCustomerAlter";
			}else if ("19".equals(operateType)) {
				logger.warn("修改宽带上网终端连接数");
				return "netAlterPcNum";
			} 
			else {
				ajax = "未知操作类型";
				return "ajax";
			}
		}// VOIP
		else if (14 == StringUtil.getIntegerValue(servTypeId)) {
			if ("1".equals(operateType)) {
				if (instAreaName==Global.JSDX)
				{
					count = getPortNum(loid, 1, 1);
					StringBuffer sb = new StringBuffer();
					sb.append("<select name='linePort' class=bk>");
					sb.append("<option value='-1'>==请选择语音端口==</option>");
					for (int i = 0; i < count; i++)
					{
						sb.append("<option value='" + "V" + (i + 1) + "'>" + "V"
								+ (i + 1) + "</option>");
					}
					sb.append("</select>");
					nameListHtml = sb.toString();
					logger.warn("VOIP业务开户工单" + nameListHtml);
					return "voipopen";
				}
				else
				{
					return "voipopen1";
				}
			} else if ("3".equals(operateType)) {
				logger.warn("VOIP业务销户工单");
				return "voipstop";
			} 
			else if ("15".equals(operateType)) {
				logger.warn("VOIP信息修改工单");
				return "voipAlterMsg";
			}
			else if ("12".equals(operateType)) {
				logger.warn("修改用户鉴权密码");
				return "voipAlterAuthpass";
			}else {
				ajax = "未知操作类型";
				return "ajax";
			}
		}// H248 VOIP
		else if (15 == StringUtil.getIntegerValue(servTypeId)) {
			if ("1".equals(operateType)) {
				logger.warn("VOIP业务开户工单");
				return "h248voipopen";
			} else if ("3".equals(operateType)) {
				logger.warn("VOIP业务销户工单");
				return "h248voipstop";
			} else {
				ajax = "未知操作类型";
				return "ajax";
			}
		}
		// Iptv
		else if (21 == StringUtil.getIntegerValue(servTypeId)) {
			if ("1".equals(operateType)) {
				count = this.getFreeLanPortNum(loid, 2, 1, 11);
				StringBuffer sb = new StringBuffer();
				sb.append("<select name='servNum' class=bk>");
				sb.append("<option value='-1'>==请选择==</option>");
				for (int i = 0; i < count; i++) {
					sb.append("<option value='" + (i + 1) + "'>" + (i + 1)
							+ "个</option>");
				}
				sb.append("</select>");
				nameListHtml = sb.toString();
				logger.warn("IPTV业务开户工单");
				return "iptvopen";
			} else if ("3".equals(operateType)) {
				logger.warn("IPTV业务销户工单");
				return "iptvstop";
			} 
			else if ("15".equals(operateType)) {
				logger.warn("IPTV信息修改工单");
				return "iptvMsgAlter";
			} 
			else if ("16".equals(operateType)) {
				logger.warn("调整IPTV组播参数");
				return "iptvAlterMulti";
			} else {
				ajax = "未知操作类型";
				return "ajax";
			}
		}
		// e8b net
		else if (10 == StringUtil.getIntegerValue(servTypeId)) {
			if ("1".equals(operateType)) {
				logger.warn("e8b 上网业务开户工单");
				return "e8bnetopen";
			} else if ("3".equals(operateType)) {
				logger.warn("e8b 上网业务销户工单");
				return "e8bnetstop";
			} else {
				ajax = "未知操作类型";
				return "ajax";
			}
		}
		// e8bIptv
		else if (11 == StringUtil.getIntegerValue(servTypeId)) {
			if ("1".equals(operateType)) {
				logger.warn("e8b IPTV业务开户工单");
				return "e8biptvopen";
			} else if ("3".equals(operateType)) {
				logger.warn("e8b IPTV业务销户工单");
				return "e8biptvstop";
			} else {
				ajax = "未知操作类型";
				return "ajax";
			}
		}
		// 天翼看店业务
		else if (25 == StringUtil.getIntegerValue(servTypeId)) {
			if ("1".equals(operateType)) {
				logger.warn("天翼看店业务开户工单");
				return "tianyiopen";
			} else if ("3".equals(operateType)) {
				logger.warn("天翼看店业务销户工单");
				return "tianyistop";
			} else {
				ajax = "未知操作类型";
				return "ajax";
			}
		}
		// 路由开通业务
		else if (6 == StringUtil.getIntegerValue(servTypeId)) {
			if ("1".equals(operateType)) {
				logger.warn("路由开通业务开户工单");
				return "routeropen";
			} else if ("3".equals(operateType)) {
				logger.warn("路由开通业务销户工单");
				return "routerstop";
			} else {
				ajax = "未知操作类型";
				return "ajax";
			}
		}
		else if(26 == StringUtil.getIntegerValue(servTypeId)){
			if ("1iptv".equals(operateType)) {
				logger.warn("stb_iptv开户工单");
				return "stbiptvOpen";
			} else if ("1ims".equals(operateType)) {
				logger.warn("stb_ims开户工单");
				return "stbimsOpen";
			} else if ("3iptv".equals(operateType)) {
				logger.warn("stb_iptv销户工单");
				return "stbiptvStop";
			} else if ("3ims".equals(operateType)) {
				logger.warn("stb_ims销户工单");
				return "stbimsStop";
			}
			else if ("6".equals(operateType)) {
				logger.warn("stb修改设备id工单");
				return "stbAlterDeviceid";
			}else{
				ajax = "未知操作类型";
				return "ajax";
			}
		}else {
			ajax = "未知业务类型";
			return "ajax";
		}
	}

	
	/**
	 * 根据obj属性名称，将本act类值赋给obj生成对象
	 * @param o
	 */
	private void setObj(Object o){ 
		
	    Field[] fields=o.getClass().getDeclaredFields();
	    String fieldName;
	    String value;
	    String setName;
	    String getName;
	    for(int i=0;i<fields.length;i++){  
	    	try
			{
		    	fieldName = fields[i].getName();
		    	getName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		    	setName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		    	Method getmethod;
				
				getmethod = this.getClass().getMethod(getName, new Class[] {});
				
		    	Method setmethod = o.getClass().getMethod(setName, new Class[] {String.class});  
		    	value = (String)getmethod.invoke(this, new Object[] {});
		    	if(null != value) value = value.trim();
		    	else value = "";
		    	setmethod.invoke(o, new Object[] {null==value?"":value});
			}
		    catch (Exception e)
			{
		    	logger.error(e.getMessage());
				e.printStackTrace();
			}
	    }  
   } 
	
	
	/**
	 * 重庆下工单
	 * @return
	 */
	public String sendSheet4CQ(){

		if (StringUtil.IsEmpty(userType)) {
			userType = "1";
		}

		SheetObj4CQ obj = new SheetObj4CQ();
		setObj(obj);
		instAreaName = Global.instAreaShortName;
		logger.warn("operateType="+operateType);
		
		// 上网业务
		if (22 == StringUtil.getIntegerValue(servTypeId)) {
			// 开户
			if ("1".equals(operateType)) {
				ajax = bio.sendNetOpenSheet4CQ(obj);
			}// 销户
			else if ("3".equals(operateType)) {
				ajax = bio.sendNetStopSheet4CQ(obj);
			} 
			else if ("7".equals(operateType)) {
				ajax = bio.sendNetAlterAccountSheet4CQ(obj);
			}
			else if ("15".equals(operateType)) {
				ajax = bio.sendNetMsgAlterSheet4CQ(obj);
			}
			else if ("20".equals(operateType)) {
				ajax = bio.sendNetUnbindSheet4CQ(obj);
			}
			else if ("6".equals(operateType)) {
				ajax = bio.sendNetAlterDeviceidSheet4CQ(obj);
			}
			else if ("9".equals(operateType)) {
				ajax = bio.sendNetAlterModeSheet4CQ(obj);
			}
			else if ("10".equals(operateType)) {
				ajax = bio.sendNetAlterPasswdSheet4CQ(obj);
			}
			else if ("18".equals(operateType)) {
				ajax = bio.sendNetAlterCustomerdSheet4CQ(obj);
			}else if ("17".equals(operateType)) {
				ajax = bio.sendNetAlterPcNumSheet4CQ(obj);
			}else {
				ajax = "未知操作类型";
			}
		}
		// VOIP业务
		else if (14 == StringUtil.getIntegerValue(servTypeId)) {
			// 开户
			if ("1".equals(operateType)) {
				if("0".equals(obj.getProtocoltype())){
					ajax = bio.sendVoipOpenSheet4CQ_imssip(obj);
				}
				else{
					ajax = bio.sendVoipOpenSheet4CQ_h248(obj);
				}
			}// 销户
			else if ("3".equals(operateType)) {
				ajax = bio.sendVoipStopSheet4CQ(obj);
			}
			else if ("15".equals(operateType)) {
				if("0".equals(obj.getProtocoltype())){
					ajax = bio.sendVoipAlterMsgSheet4CQ_imssip(obj);
				}
				else{
					ajax = bio.sendVoipAlterMsgSheet4CQ_H248(obj);
				}
			}
			else if ("12".equals(operateType)) {
				ajax = bio.sendVoipAlterAuthpassSheet4CQ_sip(obj);
			}else {
				ajax = "未知操作类型";
			}
		}
		// IPTV业务
		else if (21 == StringUtil.getIntegerValue(servTypeId)) {
			// 开户
			if ("1".equals(operateType)) {
				ajax = bio.sendIptvOpenSheet4CQ(obj);
			}// 销户
			else if ("3".equals(operateType)) {
				ajax = bio.sendIptvStopSheet4CQ(obj);
			} 
			else if ("15".equals(operateType)) {
				ajax = bio.sendIptvMsgAlterSheet4CQ(obj);
			} 
			else if ("16".equals(operateType)) {
				ajax = bio.sendIptvAlterMultiSheet4CQ(obj);
			} else {
				ajax = "未知操作类型";
			}
		} else if (26 == StringUtil.getIntegerValue(servTypeId)) {
			logger.warn("..............operateType="+operateType);
			if ("1iptv".equals(operateType)) {
				ajax = bio.sendStbiptvOpenSheet4CQ(obj);
			}
			else if ("1ims".equals(operateType)) {
				ajax = bio.sendStbimsOpenSheet4CQ(obj);
			}
			else if ("3iptv".equals(operateType)) {
				ajax = bio.sendStbiptvStopSheet4CQ(obj);
			}
			else if ("3ims".equals(operateType)) {
				ajax = bio.sendStbimsStopSheet4CQ(obj);
			}// 销户
			else if ("6".equals(operateType)) {
				ajax = bio.sendStbAlterDeviceidSheet4CQ(obj);
			}
		}else {
			ajax = "未知业务类型";
		}
		/*if ("0".equals(ajax.substring(0, 1))) {
			ajax = "成功|||" + ajax;
		} else {
			ajax = "失败|||" + ajax;
		}*/
		
		//System.out.println("bss rtn:"+ajax);
		String rtnXML="";
		String result_code = "";
		String result_msg = "";
		try
		{
			int begin = ajax.indexOf("<");
			int end = ajax.lastIndexOf(">");
			rtnXML = ajax.substring(begin, end+1);
		
			SAXReader reader = new SAXReader();
			Document document;
			
			document = reader.read(new StringReader(rtnXML));
			Element root = document.getRootElement();
			Element resultE = root.element("result");
			result_code = resultE.elementTextTrim("value");
			result_msg = resultE.elementTextTrim("errcomment");
		}
		catch (DocumentException e)
		{
			result_code = "-1";
			result_msg = "返回消息解析有误,请联系管理员";
			logger.error("解析工单返回异常,工单返回消息："+ajax);
			e.printStackTrace();
		}
		
		logger.warn("解析后xml：" + rtnXML);
		if ("22".equals(servTypeId) || "21".equals(servTypeId)
				|| "14".equals(servTypeId)) {
			int serv_type_id = 0;
			String serv_account = "";
			if ("22".equals(servTypeId)) {
				serv_type_id = 10;
				serv_account = obj.getAdaccount();
			}
			if ("21".equals(servTypeId)) {
				serv_type_id = 11;
				serv_account = iptvUsername;
			}
			if ("14".equals(servTypeId)) {
				serv_type_id = 14;
				serv_account = voipUsername;
			}
			UserRes curUser = (UserRes) session.get("curUser");
			User user = curUser.getUser();
			String id = user.getId()
					+ StringUtil
							.getStringValue(Math.round(Math.random() * 1000000000000L));
			String city_id = curUser.getCityId();
			int oper_type = Integer.valueOf(operateType);
			long oper_time = new Date().getTime() / 1000;
			long occ_id = user.getId();
			String occ_ip = WebUtil.getCurrentUserIP();
			
			logger.warn("loid="+loid+" "+deviceid+" "+adaccount);
			if(StringUtil.IsEmpty(loid)){
				loid = bio.getLoidFP(deviceid,adaccount);
			}
			bio.insertSheet(id, loid, serv_account, city_id, serv_type_id,
					oper_type, "01".equals(result_code)?1:0, result_msg,
					oper_time, occ_id,occ_ip);
		}
		
		ajax = "\n成功！返回码："+result_code;
		if(!"01".equals(result_code)){
			ajax = "\n失败！返回码："+result_code;
		}
		ajax = ajax + "\n描述："+result_msg;
		return "sheetResult";
	}
	public String sendSheet() {
		if (StringUtil.IsEmpty(userType)) {
			userType = "1";
		}

		instAreaName = Global.instAreaShortName;
		// 终端资料接口工单
		if (20 == StringUtil.getIntegerValue(servTypeId)) {

			if (Global.NMGDX.equals(instAreaName)) {
				customerId = this.getNowDate();
			}

			if ("1".equals(operateType)) {
				ajax = bio.sendOpenSheet(servTypeId, operateType, dealdate,
						userType, username, cityId, officeId, zoneId,
						orderType, linkman, linkphone, linkEmail, linkMobile,
						homeAddr, credNo, customerId, customerAccount,
						customerPwd, instAreaName);
				logger.warn("返回值:" + ajax);
			} else if ("3".equals(operateType)) {
				ajax = bio.sendStopSheetNew(servTypeId, operateType, dealdate,
						username, cityId, userType, instAreaName);
			} else {
				ajax = "1未知操作类型";
			}
		}
		// 上网业务
		else if (22 == StringUtil.getIntegerValue(servTypeId)) {
			// 开户
			if ("1".equals(operateType)) {
				ajax = bio.sendNetOpenSheet(servTypeId, operateType, dealdate,
						username, cityId, netUsername, vlanId, userType,
						netway, dns, code, ipaddress, useriptype, wlantype);
			}// 销户
			else if ("3".equals(operateType)) {
				ajax = bio.sendStopSheet(servTypeId, operateType, dealdate,
						username, cityId, netUsername);
			} else {
				ajax = "1未知操作类型";
			}
		}
		// VOIP业务
		else if (14 == StringUtil.getIntegerValue(servTypeId)) {
			// 开户
			if ("1".equals(operateType)) {
				if (Global.JSDX.equals(instAreaName))
				{
					ajax = bio.sendVoipOpenSheet(servTypeId, operateType, dealdate,
							username, cityId, voipUsername, voipPasswd, voipTelepone,
							linePort);
				}
				else
				{
					ajax = bio.sendVoipOpenSheet(servTypeId, operateType, dealdate, username, 
					          cityId, voipUsername, voipPasswd, sipIp, sipPort, standSipIp, 
					          standSipPort, linePort, voipTelepone, devType, registrarServer, 
					          registrarServerPort, standRegistrarServer, standRegistrarServerPort, 
					          outboundProxy, outboundProxyPort, standOutboundProxy, standOutboundProxyPort, protocol);
				}
			}// 销户
			else if ("3".equals(operateType)) {
				ajax = bio.sendVoipStopSheet(servTypeId, operateType, dealdate,
						username, cityId, voipUsername);
			} else {
				ajax = "1未知操作类型";
			}
		}

		// h248 VOIP业务
		else if (15 == StringUtil.getIntegerValue(servTypeId)) {
			// 开户
			if ("1".equals(operateType)) {
				if (Global.NMGDX.equals(instAreaName)) {
					ajax = bio.sendH248VoipOpenSheetNew(servTypeId,
							operateType, dealdate, userType, username,
							voipTelepone, cityId, regId, regIdType, sipIp,
							sipPort, standSipIp, standSipPort, linePort,
							netType, ipAddress, mask, gateway, dsn);
				} else {
					ajax = bio.sendH248VoipOpenSheet(servTypeId, operateType,
							dealdate, username, cityId, sipIp, sipPort,
							standSipIp, standSipPort, linePort, voipTelepone,
							userType, regId);
				}
			}// 销户
			else if ("3".equals(operateType)) {
				if (Global.NMGDX.equals(instAreaName)) {
					ajax = bio.sendVoipStopSheetNew(servTypeId, operateType,
							dealdate, username, cityId, voipTelepone, userType);
				} else {
					ajax = bio.sendVoipStopSheet2(servTypeId, operateType,
							dealdate, username, cityId, voipTelepone, userType);
				}

			} else {
				ajax = "1未知操作类型";
			}
		}

		// IPTV业务
		else if (21 == StringUtil.getIntegerValue(servTypeId)) {
			// 开户
			if ("1".equals(operateType)) {
				ajax = bio.sendIptvOpenSheet(servTypeId, operateType, dealdate,
						username, cityId, iptvUsername, servNum);
			}// 销户
			else if ("3".equals(operateType)) {
				ajax = bio.sendIptvStopSheet(servTypeId, operateType, dealdate,
						username, cityId, iptvUsername);
			} else {
				ajax = "1未知操作类型";
			}
		} else if (10 == StringUtil.getIntegerValue(servTypeId)) {
			// 开户
			if ("1".equals(operateType)) {
				ajax = bio.sendE8bNetOpenSheet(servTypeId, operateType,
						dealdate, oui, deviceSn, devType, netUsername,
						netPassword, adslPhone, maxDownSpeed, maxUpSpeed,
						maxNetNum, vlanId, vpi, vci, cityId, officeId, zoneId,
						linkman, linkphone, linkEmail, linkMobile, homeAddr,
						orderType, credNo);
			}// 销户
			else if ("3".equals(operateType)) {
				ajax = bio.sendE8bNetStopSheet(servTypeId, operateType,
						dealdate, oui, deviceSn, netUsername, cityId);
			} else {
				ajax = "1未知操作类型";
			}
		} else if (11 == StringUtil.getIntegerValue(servTypeId)) {
			// 开户
			if ("1".equals(operateType)) {
				ajax = bio.sendE8bIptvOpenSheet(servTypeId, operateType,
						dealdate, oui, deviceSn, devType, netUsername,
						netPassword, adslPhone, maxDownSpeed, maxUpSpeed,
						vlanId, vpi, vci, cityId, officeId, zoneId, linkman,
						linkphone, linkEmail, linkMobile, homeAddr, orderType,
						credNo, iptvUsername, iptvNum);
			}// 销户
			else if ("3".equals(operateType)) {
				ajax = bio.sendE8bIptvStopSheet(servTypeId, operateType,
						dealdate, oui, deviceSn, netUsername, iptvUsername,
						cityId);
			} else {
				ajax = "1未知操作类型";
			}
		} else if (25 == StringUtil.getIntegerValue(servTypeId)) {
			// 开户
			if ("1".equals(operateType)) {
				ajax = bio.sendtianyiOpenSheet(servTypeId, operateType,
						dealdate, username, cityId, netUsername, vlanId);
			}// 销户
			else if ("3".equals(operateType)) {
				ajax = bio.sendtianyiStopSheet(servTypeId, operateType,
						dealdate, username, cityId, netUsername);
			} else {
				ajax = "1未知操作类型";
			}
		} else if (6 == StringUtil.getIntegerValue(servTypeId)) {
			// 开户
			if ("1".equals(operateType)) {
				ajax = bio.sendRouterOpenSheet(servTypeId, operateType,
						dealdate, username, cityId, netUsername, servNum);
			}// 销户
			else if ("3".equals(operateType)) {
				ajax = bio.sendRouterStopSheet(servTypeId, operateType,
						dealdate, username, cityId, netUsername);
			} else {
				ajax = "1未知操作类型";
			}
		} else {
			ajax = "1未知业务类型";
		}
		if ("0".equals(ajax.substring(0, 1))) {
			ajax = "成功|||" + ajax;
		} else {
			ajax = "失败|||" + ajax;
		}
		logger.warn("zhangshimin111" + ajax);
		if ("22".equals(servTypeId) || "21".equals(servTypeId)
				|| "14".equals(servTypeId)) {
			int serv_type_id = 0;
			String serv_account = "";
			String[] results = ajax.split("\\|\\|\\|");
			if ("22".equals(servTypeId)) {
				serv_type_id = 10;
				serv_account = netUsername;
			}
			if ("21".equals(servTypeId)) {
				serv_type_id = 11;
				serv_account = iptvUsername;
			}
			if ("14".equals(servTypeId)) {
				serv_type_id = 14;
				serv_account = voipUsername;
			}
			UserRes curUser = (UserRes) session.get("curUser");
			User user = curUser.getUser();
			String id = user.getId()
					+ StringUtil
							.getStringValue(Math.round(Math.random() * 1000000000000L));
			String city_id = curUser.getCityId();
			int oper_type = Integer.valueOf(operateType);
			long oper_time = new Date().getTime() / 1000;
			long occ_id = user.getId();
			String occ_ip = WebUtil.getCurrentUserIP();
			bio.insertSheet(id, username, serv_account, city_id, serv_type_id,
					oper_type, Integer.valueOf(results[1]), results[0],
					oper_time, occ_id,occ_ip);
		}
		return "sheetResult";
	}

	public String checkUsername() {
		logger.warn("checkUsername:" + username + gw_type);
		ajax = bio.checkUsername(username, gw_type);
		return "ajax";
	}

	// 当前时间的
	private String getNowDate() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	@Override
	public void setSession(Map session) {
		this.session = session;

	}

	public SimulateSheetBIO getBio() {
		return bio;
	}

	public void setBio(SimulateSheetBIO bio) {
		this.bio = bio;
	}

	public String getServTypeId() {
		return servTypeId;
	}

	public void setServTypeId(String servTypeId) {
		this.servTypeId = servTypeId;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getDealdate() {
		return dealdate;
	}

	public void setDealdate(String dealdate) {
		this.dealdate = dealdate;
	}

	public String getDevType() {
		return devType;
	}

	public void setDevType(String devType) {
		this.devType = devType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getLinkphone() {
		return linkphone;
	}

	public void setLinkphone(String linkphone) {
		this.linkphone = linkphone;
	}

	public String getLinkEmail() {
		return linkEmail;
	}

	public void setLinkEmail(String linkEmail) {
		this.linkEmail = linkEmail;
	}

	public String getLinkMobile() {
		return linkMobile;
	}

	public void setLinkMobile(String linkMobile) {
		this.linkMobile = linkMobile;
	}

	public String getHomeAddr() {
		return homeAddr;
	}

	public void setHomeAddr(String homeAddr) {
		this.homeAddr = homeAddr;
	}

	public String getCredNo() {
		return credNo;
	}

	public void setCredNo(String credNo) {
		this.credNo = credNo;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public Map getSession() {
		return session;
	}

	public String getNetUsername() {
		return netUsername;
	}

	public void setNetUsername(String netUsername) {
		this.netUsername = netUsername;
	}

	public String getNetPassword() {
		return netPassword;
	}

	public void setNetPassword(String netPassword) {
		this.netPassword = netPassword;
	}

	public String getVlanId() {
		return vlanId;
	}

	public void setVlanId(String vlanId) {
		this.vlanId = vlanId;
	}

	public String getVoipUsername() {
		return voipUsername;
	}

	public void setVoipUsername(String voipUsername) {
		this.voipUsername = voipUsername;
	}

	public String getVoipPasswd() {
		return voipPasswd;
	}

	public void setVoipPasswd(String voipPasswd) {
		this.voipPasswd = voipPasswd;
	}

	public String getSipIp() {
		return sipIp;
	}

	public void setSipIp(String sipIp) {
		this.sipIp = sipIp;
	}

	public int getSipPort() {
		return sipPort;
	}

	public void setSipPort(int sipPort) {
		this.sipPort = sipPort;
	}

	public String getStandSipIp() {
		return standSipIp;
	}

	public void setStandSipIp(String standSipIp) {
		this.standSipIp = standSipIp;
	}

	public int getStandSipPort() {
		return standSipPort;
	}

	public void setStandSipPort(int standSipPort) {
		this.standSipPort = standSipPort;
	}

	public String getLinePort() {
		return linePort;
	}

	public void setLinePort(String linePort) {
		this.linePort = linePort;
	}

	public String getVoipTelepone() {
		return voipTelepone;
	}

	public void setVoipTelepone(String voipTelepone) {
		this.voipTelepone = voipTelepone;
	}

	public int getServNum() {
		return servNum;
	}

	public void setServNum(int servNum) {
		this.servNum = servNum;
	}

	public String getIptvLanPort() {
		return iptvLanPort;
	}

	public void setIptvLanPort(String iptvLanPort) {
		this.iptvLanPort = iptvLanPort;
	}

	public String getIptvUsername() {
		return iptvUsername;
	}

	public void setIptvUsername(String iptvUsername) {
		this.iptvUsername = iptvUsername;
	}

	public String getVpi() {
		return vpi;
	}

	public void setVpi(String vpi) {
		this.vpi = vpi;
	}

	public String getVci() {
		return vci;
	}

	public void setVci(String vci) {
		this.vci = vci;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public String getRegistrarServer() {
		return registrarServer;
	}

	public void setRegistrarServer(String registrarServer) {
		this.registrarServer = registrarServer;
	}

	public int getRegistrarServerPort() {
		return registrarServerPort;
	}

	public void setRegistrarServerPort(int registrarServerPort) {
		this.registrarServerPort = registrarServerPort;
	}

	public String getStandRegistrarServer() {
		return standRegistrarServer;
	}

	public void setStandRegistrarServer(String standRegistrarServer) {
		this.standRegistrarServer = standRegistrarServer;
	}

	public int getStandRegistrarServerPort() {
		return standRegistrarServerPort;
	}

	public void setStandRegistrarServerPort(int standRegistrarServerPort) {
		this.standRegistrarServerPort = standRegistrarServerPort;
	}

	public String getOutboundProxy() {
		return outboundProxy;
	}

	public void setOutboundProxy(String outboundProxy) {
		this.outboundProxy = outboundProxy;
	}

	public int getOutboundProxyPort() {
		return outboundProxyPort;
	}

	public void setOutboundProxyPort(int outboundProxyPort) {
		this.outboundProxyPort = outboundProxyPort;
	}

	public String getStandOutboundProxy() {
		return standOutboundProxy;
	}

	public void setStandOutboundProxy(String standOutboundProxy) {
		this.standOutboundProxy = standOutboundProxy;
	}

	public int getStandOutboundProxyPort() {
		return standOutboundProxyPort;
	}

	public void setStandOutboundProxyPort(int standOutboundProxyPort) {
		this.standOutboundProxyPort = standOutboundProxyPort;
	}

	public int getProtocol() {
		return protocol;
	}

	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}

	public String getMaxDownSpeed() {
		return maxDownSpeed;
	}

	public void setMaxDownSpeed(String maxDownSpeed) {
		this.maxDownSpeed = maxDownSpeed;
	}

	public String getMaxUpSpeed() {
		return maxUpSpeed;
	}

	public void setMaxUpSpeed(String maxUpSpeed) {
		this.maxUpSpeed = maxUpSpeed;
	}

	public String getMaxNetNum() {
		return maxNetNum;
	}

	public void setMaxNetNum(String maxNetNum) {
		this.maxNetNum = maxNetNum;
	}

	public String getOui() {
		return oui;
	}

	public void setOui(String oui) {
		this.oui = oui;
	}

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public String getAdslPhone() {
		return adslPhone;
	}

	public void setAdslPhone(String adslPhone) {
		this.adslPhone = adslPhone;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public int getIptvNum() {
		return iptvNum;
	}

	public void setIptvNum(int iptvNum) {
		this.iptvNum = iptvNum;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerAccount() {
		return customerAccount;
	}

	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}

	public String getCustomerPwd() {
		return customerPwd;
	}

	public void setCustomerPwd(String customerPwd) {
		this.customerPwd = customerPwd;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gwType) {
		gw_type = gwType;
	}

	public String getInstAreaName() {
		return instAreaName;
	}

	public void setInstAreaName(String instAreaName) {
		this.instAreaName = instAreaName;
	}

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getDsn() {
		return dsn;
	}

	public void setDsn(String dsn) {
		this.dsn = dsn;
	}

	public String getLoid() {
		return loid;
	}

	public void setLoid(String loid) {
		this.loid = loid;
	}

	public String getRegIdType() {
		return regIdType;
	}

	public void setRegIdType(String regIdType) {
		this.regIdType = regIdType;
	}

	public String getNetway() {
		return netway;
	}

	public void setNetway(String netway) {
		this.netway = netway;
	}

	public String getDns() {
		return dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getUseriptype() {
		return useriptype;
	}

	public void setUseriptype(String useriptype) {
		this.useriptype = useriptype;
	}

	public String getWlantype() {
		return wlantype;
	}

	public void setWlantype(String wlantype) {
		this.wlantype = wlantype;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Map> getListMap() {
		return listMap;
	}

	public void setListMap(List<Map> listMap) {
		this.listMap = listMap;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public String getNameListHtml() {
		return nameListHtml;
	}

	public void setNameListHtml(String nameListHtml) {
		this.nameListHtml = nameListHtml;
	}

	public String getIptvUserName() {
		return iptvUserName;
	}

	public void setIptvUserName(String iptvUserName) {
		this.iptvUserName = iptvUserName;
	}

	public String getUserNameVal() {
		return userNameVal;
	}

	public void setUserNameVal(String userNameVal) {
		this.userNameVal = userNameVal;
	}

	
	public String getOrdertime()
	{
		return ordertime;
	}

	
	public void setOrdertime(String ordertime)
	{
		this.ordertime = ordertime;
	}

	
	public String getOrderself()
	{
		return orderself;
	}

	
	public void setOrderself(String orderself)
	{
		this.orderself = orderself;
	}

	
	public String getDevicewan()
	{
		return devicewan;
	}

	
	public void setDevicewan(String devicewan)
	{
		this.devicewan = devicewan;
	}

	
	public String getUsertype()
	{
		return usertype;
	}

	
	public void setUsertype(String usertype)
	{
		this.usertype = usertype;
	}

	
	public String getSubareacode()
	{
		return subareacode;
	}

	
	public void setSubareacode(String subareacode)
	{
		this.subareacode = subareacode;
	}

	
	public String getOlduserid()
	{
		return olduserid;
	}

	
	public void setOlduserid(String olduserid)
	{
		this.olduserid = olduserid;
	}

	
	public String getDeviceid()
	{
		return deviceid;
	}

	
	public void setDeviceid(String deviceid)
	{
		this.deviceid = deviceid;
	}

	
	public String getAdaccount()
	{
		return adaccount;
	}

	
	public void setAdaccount(String adaccount)
	{
		this.adaccount = adaccount;
	}

	
	public String getUseraddress()
	{
		return useraddress;
	}

	
	public void setUseraddress(String useraddress)
	{
		this.useraddress = useraddress;
	}

	
	public String getUserid()
	{
		return userid;
	}

	
	public void setUserid(String userid)
	{
		this.userid = userid;
	}

	
	public String getContactperson()
	{
		return contactperson;
	}

	
	public void setContactperson(String contactperson)
	{
		this.contactperson = contactperson;
	}

	
	public String getPhonenumber()
	{
		return phonenumber;
	}

	
	public void setPhonenumber(String phonenumber)
	{
		this.phonenumber = phonenumber;
	}

	
	public String getMigration()
	{
		return migration;
	}

	
	public void setMigration(String migration)
	{
		this.migration = migration;
	}

	
	public String getRgportid()
	{
		return rgportid;
	}

	
	public void setRgportid(String rgportid)
	{
		this.rgportid = rgportid;
	}

	
	public String getVlanid()
	{
		return vlanid;
	}

	
	public void setVlanid(String vlanid)
	{
		this.vlanid = vlanid;
	}

	
	public String getRgmode()
	{
		return rgmode;
	}

	
	public void setRgmode(String rgmode)
	{
		this.rgmode = rgmode;
	}

	
	public String getAdpassword()
	{
		return adpassword;
	}

	
	public void setAdpassword(String adpassword)
	{
		this.adpassword = adpassword;
	}

	
	public String getPvcvpi()
	{
		return pvcvpi;
	}

	
	public void setPvcvpi(String pvcvpi)
	{
		this.pvcvpi = pvcvpi;
	}

	
	public String getPvcvci()
	{
		return pvcvci;
	}

	
	public void setPvcvci(String pvcvci)
	{
		this.pvcvci = pvcvci;
	}

	
	public String getMultidevicemode()
	{
		return multidevicemode;
	}

	
	public void setMultidevicemode(String multidevicemode)
	{
		this.multidevicemode = multidevicemode;
	}

	
	public String getPcnumbers()
	{
		return pcnumbers;
	}

	
	public void setPcnumbers(String pcnumbers)
	{
		this.pcnumbers = pcnumbers;
	}

	
	public String getKeepuserinfo()
	{
		return keepuserinfo;
	}

	
	public void setKeepuserinfo(String keepuserinfo)
	{
		this.keepuserinfo = keepuserinfo;
	}

	
	public String getIPV6Enable()
	{
		return IPV6Enable;
	}

	
	public void setIPV6Enable(String iPV6Enable)
	{
		IPV6Enable = iPV6Enable;
	}

	
	public String getAFTRAddress()
	{
		return AFTRAddress;
	}

	
	public void setAFTRAddress(String aFTRAddress)
	{
		AFTRAddress = aFTRAddress;
	}

	
	public String getDhcpenable()
	{
		return dhcpenable;
	}

	
	public void setDhcpenable(String dhcpenable)
	{
		this.dhcpenable = dhcpenable;
	}

	
	public String getAwifiportid()
	{
		return awifiportid;
	}

	
	public void setAwifiportid(String awifiportid)
	{
		this.awifiportid = awifiportid;
	}

	
	public String getIptvrgportid()
	{
		return iptvrgportid;
	}

	
	public void setIptvrgportid(String iptvrgportid)
	{
		this.iptvrgportid = iptvrgportid;
	}

	
	public String getMultivlanmode()
	{
		return multivlanmode;
	}

	
	public void setMultivlanmode(String multivlanmode)
	{
		this.multivlanmode = multivlanmode;
	}

	
	public String getSnoopingEnable()
	{
		return snoopingEnable;
	}

	
	public void setSnoopingEnable(String snoopingEnable)
	{
		this.snoopingEnable = snoopingEnable;
	}

	
	public String getProtocoltype()
	{
		return protocoltype;
	}

	
	public void setProtocoltype(String protocoltype)
	{
		this.protocoltype = protocoltype;
	}

	
	public String getImsdnsip()
	{
		return imsdnsip;
	}

	
	public void setImsdnsip(String imsdnsip)
	{
		this.imsdnsip = imsdnsip;
	}

	
	public String getImsdnsbakip()
	{
		return imsdnsbakip;
	}

	
	public void setImsdnsbakip(String imsdnsbakip)
	{
		this.imsdnsbakip = imsdnsbakip;
	}

	
	public String getMgip()
	{
		return mgip;
	}

	
	public void setMgip(String mgip)
	{
		this.mgip = mgip;
	}

	
	public String getMgmask()
	{
		return mgmask;
	}

	
	public void setMgmask(String mgmask)
	{
		this.mgmask = mgmask;
	}

	
	public String getMggw()
	{
		return mggw;
	}

	
	public void setMggw(String mggw)
	{
		this.mggw = mggw;
	}

	
	public String getProxyserverip()
	{
		return proxyserverip;
	}

	
	public void setProxyserverip(String proxyserverip)
	{
		this.proxyserverip = proxyserverip;
	}

	
	public String getProxyserverport()
	{
		return proxyserverport;
	}

	
	public void setProxyserverport(String proxyserverport)
	{
		this.proxyserverport = proxyserverport;
	}

	
	public String getProxyserverbakip()
	{
		return proxyserverbakip;
	}

	
	public void setProxyserverbakip(String proxyserverbakip)
	{
		this.proxyserverbakip = proxyserverbakip;
	}

	
	public String getProxyserverbakport()
	{
		return proxyserverbakport;
	}

	
	public void setProxyserverbakport(String proxyserverbakport)
	{
		this.proxyserverbakport = proxyserverbakport;
	}

	
	public String getRegistrarserverip()
	{
		return registrarserverip;
	}

	
	public void setRegistrarserverip(String registrarserverip)
	{
		this.registrarserverip = registrarserverip;
	}

	
	public String getRegistrarserverport()
	{
		return registrarserverport;
	}

	
	public void setRegistrarserverport(String registrarserverport)
	{
		this.registrarserverport = registrarserverport;
	}

	
	public String getStandbyregistrarserverip()
	{
		return standbyregistrarserverip;
	}

	
	public void setStandbyregistrarserverip(String standbyregistrarserverip)
	{
		this.standbyregistrarserverip = standbyregistrarserverip;
	}

	
	public String getStandbyregistrarserverport()
	{
		return standbyregistrarserverport;
	}

	
	public void setStandbyregistrarserverport(String standbyregistrarserverport)
	{
		this.standbyregistrarserverport = standbyregistrarserverport;
	}

	
	public String getSipuri()
	{
		return sipuri;
	}

	
	public void setSipuri(String sipuri)
	{
		this.sipuri = sipuri;
	}

	
	public String getUsertid()
	{
		return usertid;
	}

	
	public void setUsertid(String usertid)
	{
		this.usertid = usertid;
	}

	
	public String getAuthusername()
	{
		return authusername;
	}

	
	public void setAuthusername(String authusername)
	{
		this.authusername = authusername;
	}

	
	public String getAuthpassword()
	{
		return authpassword;
	}

	
	public void setAuthpassword(String authpassword)
	{
		this.authpassword = authpassword;
	}

	
	public String getNewadaccount()
	{
		return newadaccount;
	}

	
	public void setNewadaccount(String newadaccount)
	{
		this.newadaccount = newadaccount;
	}

	
	public String getCustomerid()
	{
		return customerid;
	}

	
	public void setCustomerid(String customerid)
	{
		this.customerid = customerid;
	}

	
	public String getPhonenumberSip()
	{
		return PhonenumberSip;
	}

	
	public void setPhonenumberSip(String phonenumberSip)
	{
		PhonenumberSip = phonenumberSip;
	}

	
	
	public String getMgDomain()
	{
		return mgDomain;
	}

	
	public void setMgDomain(String mgDomain)
	{
		this.mgDomain = mgDomain;
	}

	public String getSsip()
	{
		return ssip;
	}

	
	public void setSsip(String ssip)
	{
		this.ssip = ssip;
	}

	
	public String getSsport()
	{
		return ssport;
	}

	
	public void setSsport(String ssport)
	{
		this.ssport = ssport;
	}

	
	public String getSsbakip()
	{
		return ssbakip;
	}

	
	public void setSsbakip(String ssbakip)
	{
		this.ssbakip = ssbakip;
	}

	
	public String getSsbakport()
	{
		return ssbakport;
	}

	
	public void setSsbakport(String ssbakport)
	{
		this.ssbakport = ssbakport;
	}

	
	public String getPppoeid()
	{
		return pppoeid;
	}

	
	public void setPppoeid(String pppoeid)
	{
		this.pppoeid = pppoeid;
	}

	
	public String getPppoepassword()
	{
		return pppoepassword;
	}

	
	public void setPppoepassword(String pppoepassword)
	{
		this.pppoepassword = pppoepassword;
	}

	
	public String getDhcppassword()
	{
		return dhcppassword;
	}

	
	public void setDhcppassword(String dhcppassword)
	{
		this.dhcppassword = dhcppassword;
	}

	
	public String getDhcpid()
	{
		return dhcpid;
	}

	
	public void setDhcpid(String dhcpid)
	{
		this.dhcpid = dhcpid;
	}

	
	public String getIptvuserid()
	{
		return iptvuserid;
	}

	
	public void setIptvuserid(String iptvuserid)
	{
		this.iptvuserid = iptvuserid;
	}

	
	public String getIptvpassword()
	{
		return iptvpassword;
	}

	
	public void setIptvpassword(String iptvpassword)
	{
		this.iptvpassword = iptvpassword;
	}

	
	public String getAuthurl()
	{
		return authurl;
	}

	
	public void setAuthurl(String authurl)
	{
		this.authurl = authurl;
	}

	
	public String getAuthurl_Backup()
	{
		return authurl_Backup;
	}

	
	public void setAuthurl_Backup(String authurl_Backup)
	{
		this.authurl_Backup = authurl_Backup;
	}

	
	public String getImsid()
	{
		return imsid;
	}

	
	public void setImsid(String imsid)
	{
		this.imsid = imsid;
	}

	
	public String getImspassword()
	{
		return imspassword;
	}

	
	public void setImspassword(String imspassword)
	{
		this.imspassword = imspassword;
	}

	
	public String getBacdomain()
	{
		return bacdomain;
	}

	
	public void setBacdomain(String bacdomain)
	{
		this.bacdomain = bacdomain;
	}

	
	public String getBacaddress1()
	{
		return bacaddress1;
	}

	
	public void setBacaddress1(String bacaddress1)
	{
		this.bacaddress1 = bacaddress1;
	}

	
	public String getBacaddress2()
	{
		return bacaddress2;
	}

	
	public void setBacaddress2(String bacaddress2)
	{
		this.bacaddress2 = bacaddress2;
	}

}
