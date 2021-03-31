package com.linkage.litms.resource;

/**
 * zhaixf(3412) 2008-05-08
 * req:XJDX_ITMS-BUG-20080506-XXF-001
 * 
 * zhaixf(3412) 2008-05-08
 * req:NJLC_HG-BUG-ZHOUMF-20080508-001
 *
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBAdapter;
import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DbUtils;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.Global;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.upload.File;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.FormUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.system.dbimpl.AreaManageSyb;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.resource.bio.UserInstReleaseBIO;

@SuppressWarnings("unchecked")
public class HGWUserInfoAct {
	private static Logger logger = LoggerFactory.getLogger(HGWUserInfoAct.class);
	private static final int DB_MYSQL=com.linkage.module.gwms.Global.DB_MYSQL;
	private String m_BasID_List_SQL = "select loopback_ip,device_name from tab_gw_device where resource_type_id=3 order by loopback_ip";

	private String m_BanID_List_SQL = "select loopback_ip,device_name from tab_gw_device where resource_type_id=9 order by loopback_ip";

	private String m_HGWUsers_List_SQL = "select user_id, gather_id,user_state,username,cotno,cust_type_id,opmode,user_type_id,realname,phonenumber,device_serialnumber,device_id,dealdate,opendate, updatetime,serv_type_id,wan_type,oui,onlinedate,city_id FROM tab_hgwcustomer ";

	private String m_serv_users_List_SQL = "select a.user_id, a.gather_id, a.username, a.cotno,a.cust_type_id, a.user_type_id, a.realname,a.user_state from tab_hgwcustomer a"
			+ " where a.service_id = ? and a.user_state = ?";

	private String m_OfficeList_SQL = "select office_id,office_name from tab_office order by office_id";

	private String m_ZoneList_SQL = "select zone_id,zone_name from tab_zone order by zone_id";

	private String m_Vendor_SQL = "select vendor_id,vendor_name from tab_vendor";

//	private String m_DevModel_SQL = "select oui,device_model,softwareversion from tab_devicetype_info";
	private String m_DevModel_SQL = "select device_model,softwareversion from tab_devicetype_info a,gw_device_model b where a.device_model_id=b.device_model_id";
	
	/**
	 * 新增LAN用户链路信息
	 */
	private String m_HGWUserInfoAdd_SQL = "insert into tab_hgwcustomer (user_id,gather_id,username,passwd,"
			+ "city_id,cotno,bill_type_id,next_bill_type_id,cust_type_id,user_type_id,bindtype,virtualnum,"
			+ "numcharacter,access_style_id,aut_flag,service_set,realname,sex,"
			+ "cred_type_id,credno,address,office_id,zone_id,access_kind_id,trade_id,licenceregno,occupation_id,"
			+ "education_id,vipcardno,"
			+ "contractno,linkman,linkman_credno,linkphone,linkaddress,mobile,email,agent,agent_credno,"
			+ "agentphone,adsl_res,adsl_card,adsl_dev,adsl_ser,isrepair,"
			+ "bandwidth,ipaddress,overipnum,ipmask,gateway,macaddress,device_id,device_ip,device_shelf,"
			+ "device_frame,device_slot,device_port,basdevice_id,basdevice_ip,basdevice_shelf,basdevice_frame,basdevice_slot,basdevice_port,"
			+ "vlanid,workid,user_state,opendate,onlinedate,pausedate,closedate,updatetime,staff_id,remark,phonenumber,"
			+ "cableid,bwlevel,vpiid,vciid,adsl_hl,userline,dslamserialno,movedate,dealdate,opmode,maxattdnrate,upwidth,oui,device_serialnumber, serv_type_id, max_user_number, wan_type) "
			+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	/**
	 * 新增用户业务信息
	 */
	private String m_hgwcust_serv_info = "insert into hgwcust_serv_info (user_id,serv_type_id,username,serv_status,passwd,wan_type"
			+ ",vpiid,vciid,vlanid,ipaddress,ipmask,gateway,adsl_ser,bind_port,open_status,"
			+ "dealdate,opendate,pausedate,updatetime) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private String m_LanUserInfoWhere_SQL = "select * from cus_lancustomer where user_id=";

	private String m_LanUserIPWhere_SQL = "select ipaddress from cus_lanip where user_id=";

	private Cursor cursor = null;

	private String str_userid = null;

	private PrepareSQL pSQL;

	private String str_user_id = null;

	protected ServletContext m_application;
	
	/**
	 * 资源绑定BIO
	 */
	private UserInstReleaseBIO userInstReleaseBio = new UserInstReleaseBIO();

	/**
	 * 增加确认设备
	 */
	private String m_ConfirmDevAdd_SQL = "insert into tab_confirmdevice values(?,?,?)";

	/**
	 * 确认设备列表
	 */
	private String m_ConfirmDev_List_SQL = "select * from tab_confirmdevice";

	private Set keySet;
	
	private List<String> stringKeyList = null;
	private List<String> intKeyList = null;
	

	public HGWUserInfoAct() {
		pSQL = new PrepareSQL();
	}

	/**
	 * 获得Lan用户IP地址
	 * 
	 * @param user_id
	 * @return Cursor
	 */
	public Cursor getLanIpAddrCursor(String user_id) {
		PrepareSQL psql = new PrepareSQL(m_LanUserIPWhere_SQL + user_id);
    	psql.getSQL();
		return DataSetBean.getCursor(m_LanUserIPWhere_SQL + user_id);
	}

	/**
	 * 获得特定Lan用户信息
	 * 
	 * @param user_id
	 * @return Map
	 */
	public Map getLanUserInfo(String user_id) {
		PrepareSQL psql = new PrepareSQL(m_LanUserInfoWhere_SQL + user_id);
    	psql.getSQL();
		return DataSetBean.getRecord(m_LanUserInfoWhere_SQL + user_id);
	}

	/**
	 * 提取Bas接入设备列表
	 * 
	 * @return Cursor
	 */
	public Cursor getBasListCursor() {
		PrepareSQL psql = new PrepareSQL(m_BasID_List_SQL);
    	psql.getSQL();
		return DataSetBean.getCursor(m_BasID_List_SQL);
	}

	/**
	 * 提取Ban接入设备列表
	 * 
	 * @return Cursor
	 */
	public Cursor getBanListCursor() {
		PrepareSQL psql = new PrepareSQL(m_BanID_List_SQL);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(m_BanID_List_SQL);

		return cursor;
	}

	/**
	 * 取得当前用户的域下的所有设备
	 * 
	 * @author
	 * @date 2006-8-22
	 * 
	 * @param flag
	 * @param compare
	 * @param rename
	 * @param request
	 * @return
	 */
	public Cursor getDeviceByArea(HttpServletRequest request) {
		String sql = "";
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long area_id = curUser.getAreaId();
		if (area_id == 1) {
			sql = "select oui,device_serialnumber from tab_gw_device";
		} else {
			sql = "select oui,device_serialnumber from tab_gw_device where device_id in ("
					+ " select res_id from tab_gw_res_area where res_type=1 and area_id="
					+ area_id + ")";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(sql);

		return cursor;
		// String strDeviceList = FormUtil.createListBox(cursor, "oui",
		// "device_serialnumber", flag, compare, rename);
		// return strDeviceList;
	}

	/**
	 * 取得当前用户的属地及下属属地
	 * 
	 * @author
	 * @date 2006-8-22
	 * 
	 * @param flag
	 * @param compare
	 * @param rename
	 * @param request
	 * @return
	 */
	public String getCityListSelf(boolean flag, String compare, String rename,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String city_id = curUser.getCityId();
		String sql = "select city_id,city_name from tab_city where parent_id='"
				+ city_id + "' or city_id='" + city_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		String strCityList = FormUtil.createListBox(cursor, "city_id",
				"city_name", flag, compare, rename);

		return strCityList;
	}
	/**
	 * 属地列表为所有属地
	 * johnson
	 * 2008-7-15
	 * 
	 * String
	 */
	public String getCityListByuser(boolean flag, String compare, String rename,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String city_id = curUser.getCityId();
		String sql = "select city_id,city_name from tab_city where city_id in "
			+ "(select city_id from tab_city where city_id = '"
			+ city_id
			+ "' or parent_id='"
			+ city_id
			+ "') "
			+ "or parent_id in  (select city_id  from tab_city where city_id = '"
			+ city_id + "' or parent_id='" + city_id + "')";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		String strCityList = FormUtil.createListBox(cursor, "city_id",
				"city_name", flag, compare, rename);

		return strCityList;
	}
	/**
	 * 取得业务类型列表
	 * 
	 * @author Zhaof
	 * @date 2008-2-15
	 * 
	 * @param type
	 * @return
	 */
	public String getServTypeList(int type) {
		String htmlStr;
		String sql = null;
		
		htmlStr = "<SELECT NAME=\"some_service\" onChange=\"showVal(this)\" CLASS=\"bk\">";
		if (type == 0) {
			sql = "select * from tab_gw_serv_type";
		} else {
			sql = "select * from tab_gw_serv_type where type=" + type;
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();

		if (fields == null) {
			htmlStr += "<OPTION VALUE=-1>==此项没有记录==</OPTION>";
		} else {
			htmlStr += "<OPTION VALUE=-1>==请选择==</OPTION>";
			int count = 0;//1;
			while (fields != null) {
				String value = (String) fields.get("serv_type_id");
				if (value != null)
					value = value.trim();
				htmlStr += "<OPTION VALUE='" + value + "'"
						+ (count == 1 ? " selected" : "") + ">";
				//count++;
				String serv_type_name = (String) fields.get("serv_type_name");
				htmlStr += serv_type_name + "</OPTION>";

				fields = cursor.getNext();
			}
		}
		htmlStr += "</SELECT>";
		// logger.debug("htmlStr=" + htmlStr);
		return htmlStr;
	}

	/**
	 * 取得业务类型列表
	 * 
	 * @author Zhaof
	 * @date 2008-2-15
	 * 
	 * @param type
	 * @return
	 */
	public String getEgwServTypeList() {
		String htmlStr;
		htmlStr = "<SELECT NAME=\"some_service\" onChange=\"showVal(this)\" CLASS=\"bk\">";
		String sql = "select * from tab_gw_serv_type where (type=5 or type=6)";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();

		if (fields == null) {
			htmlStr += "<OPTION VALUE=-1>==此项没有记录==</OPTION>";
		} else {
			htmlStr += "<OPTION VALUE=-1>==请选择==</OPTION>";
			int count = 1;
			while (fields != null) {
				String value = (String) fields.get("serv_type_id");
				if (value != null)
					value = value.trim();
				htmlStr += "<OPTION VALUE='" + value + "'"
						+ (count == 1 ? " selected" : "") + ">";
				count++;
				String serv_type_name = (String) fields.get("serv_type_name");
				htmlStr += serv_type_name + "</OPTION>";

				fields = cursor.getNext();
			}
		}
		htmlStr += "</SELECT>";
		// logger.debug("htmlStr=" + htmlStr);
		return htmlStr;
	}

	/**
	 * 取得业务类型列表
	 * 
	 * @author Zhaixf
	 * @date 2008-2-25
	 * 
	 * @param type
	 * @return
	 */
	public String getServTypeNameList(int type) {
		String htmlStr;
		htmlStr = "<SELECT NAME=\"some_service\" onChange=\"showVal(this)\" CLASS=\"bk\">";
		String sql = "select * from tab_gw_serv_type where type=" + type;
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();

		if (fields == null) {
			htmlStr += "<OPTION VALUE=-1>==此项没有记录==</OPTION>";
		} else {
			htmlStr += "<OPTION VALUE=-1>==请选择==</OPTION>";
			int count = 1;
			while (fields != null) {
				String value = (String) fields.get("serv_type_id");
				String serv_type_name = (String) fields.get("serv_type_name");
				if (value != null)
					value = value.trim();
				htmlStr += "<OPTION VALUE='" + value + "|" + serv_type_name
						+ "'" + (count == 1 ? " selected" : "") + ">";
				count++;
				htmlStr += serv_type_name + "</OPTION>";

				fields = cursor.getNext();
			}
		}
		htmlStr += "</SELECT>";
		// logger.debug("htmlStr=" + htmlStr);
		return htmlStr;
	}

	/*
	 * @author zhaixf 用于取得业务类型的下拉列表， type 业务类型 selected 选中的选项 return 返回页面列表字符串
	 */
	public String getServTypeNameList(int type, String selected) {

		if (selected == null || "".equals(selected))
			return getServTypeList(type);

		String htmlStr;
		htmlStr = "<SELECT NAME=\"some_service\" onChange=\"showVal(this)\" CLASS=\"bk\">";
		String sql = "select * from tab_gw_serv_type where type=" + type;
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();

		if (fields == null) {
			htmlStr += "<OPTION VALUE=-1>==此项没有记录==</OPTION>";
		} else {
			htmlStr += "<OPTION VALUE=-1>==请选择==</OPTION>";

			while (fields != null) {
				String value = (String) fields.get("serv_type_id");
				String serv_type_name = (String) fields.get("serv_type_name");
				if (value != null)
					value = value.trim();
				htmlStr += "<OPTION VALUE='" + value + "'"
						+ (selected.equals(value) ? " selected" : "") + ">";

				htmlStr += serv_type_name + "</OPTION>";

				fields = cursor.getNext();
			}
		}
		htmlStr += "</SELECT>";
		// logger.debug("htmlStr=" + htmlStr);
		return htmlStr;
	}

	
	public String getENamesList_BBMS(String selected) {

//		if (selected == null || "".equals(selected))
//			return getServTypeList(type);
		String htmlStr;
		htmlStr = "<SELECT NAME=\"e_id\" CLASS=\"bk\">";
		String sql = "select * from tab_customerinfo ";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();

		if (fields == null) {
			htmlStr += "<OPTION VALUE=-1>==此项没有记录==</OPTION>";
		} else {
			htmlStr += "<OPTION VALUE=-1>==请选择==</OPTION>";

			while (fields != null) {
				String customer_id = (String) fields.get("customer_id");
				String customer_name = (String) fields.get("customer_name");
				if (customer_id != null) {
					customer_id = customer_id.trim();
				}
				htmlStr += "<OPTION VALUE='" + customer_id + "'"
						+ (selected.equals(customer_id) ? " selected" : "") + ">";

				htmlStr += customer_name + "</OPTION>";

				fields = cursor.getNext();
			}
		}
		htmlStr += "</SELECT>";
		// logger.debug("htmlStr=" + htmlStr);
		return htmlStr;
	}
	
	/**
	 * 获取客户下拉框
	 * 
	 * 增加属地过滤
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2008-10-17
	 * @return String
	 */
	public String getENamesList_BBMS(String type, String value, String city_id) {
		
		String htmlStr;
		String sql = "";
		htmlStr = "<SELECT NAME=\"e_id\" CLASS=\"bk\">";
		if("id".equals(type)){
			if("".equals(value)||value.isEmpty()){
				htmlStr += "<OPTION VALUE=-1>==请根据客户查询来匹配==</OPTION>";
				htmlStr += "</SELECT>";
				// logger.debug("htmlStr=" + htmlStr);
				return htmlStr;
			}else{
				sql = "select customer_id,customer_name from tab_customerinfo where customer_id='" + value + "'";
			}
		}else{
			sql = "select customer_id,customer_name from tab_customerinfo where customer_name like '%" + value + "%'";
			//增加属地过滤
			if(!"00".equals(city_id)){
				List list = CityDAO.getAllNextCityIdsByCityPid(city_id);
				sql += " and city_id in (" + StringUtils.weave(list) + ")";
				list = null;
			}
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();

		if (fields == null) {
			htmlStr += "<OPTION VALUE=-1>==此项没有记录==</OPTION>";
		} else {
			htmlStr += "<OPTION VALUE=-1>==请选择==</OPTION>";

			while (fields != null) {
				String customer_id = (String) fields.get("customer_id");
				String customer_name = (String) fields.get("customer_name");
				if (customer_id != null) {
					customer_id = customer_id.trim();
				}
				htmlStr += "<OPTION VALUE='" + customer_id + "'"
						+ (value.equals(customer_id) ? " selected" : "") + ">";

				htmlStr += customer_name + "</OPTION>";

				fields = cursor.getNext();
			}
		}
		htmlStr += "</SELECT>";
		// logger.debug("htmlStr=" + htmlStr);
		return htmlStr;
	}
	
	
	/**
	 * 编辑页面，该属相不能更改，显示只有一条的下拉列表 HGWUserInfoAct.java
	 * 
	 * @param onlySelected
	 *            业务编号
	 * @return 返回仅有一条数据的下拉列表 String
	 */
	public String getServTypeNameList(String onlySelected) {

		String htmlStr;
		htmlStr = "<SELECT NAME=\"some_service\" onChange=\"showVal(this)\" CLASS=\"bk\">";
		String sql = "select * from tab_gw_serv_type where serv_type_id="
				+ onlySelected;
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Map fields = DataSetBean.getRecord(sql);
		if (fields != null) {
			String value = (String) fields.get("serv_type_id");
			String serv_type_name = (String) fields.get("serv_type_name");
			htmlStr += "<OPTION VALUE='" + value + "'>";
			htmlStr += serv_type_name + "</OPTION>";
		}
		htmlStr += "</SELECT>";
		return htmlStr;
	}

	/**
	 * 取得操作类型列表
	 * 
	 * @author Zhaof
	 * @date 2008-2-15
	 * 
	 * @param type
	 * @return
	 */
	public String getOperTypeList(int serv_type_id) {
		String htmlStr;
		htmlStr = "<SELECT NAME='oper_type' CLASS='bk'>";
		String sql = "select oper_type_id, oper_type_name from tab_gw_oper_type where oper_type_id in (select oper_type_id from tab_service where serv_type_id="
				+ serv_type_id + ")";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();

		if (fields == null) {
			htmlStr += "<OPTION VALUE=-1>==此项没有记录==</OPTION>";
		} else {
			int count = 1;
			htmlStr += "<OPTION VALUE=-1>==请选择==</OPTION>";
			while (fields != null) {
				String value = (String) fields.get("oper_type_id");
				if (value != null)
					value = value.trim();
				htmlStr += "<OPTION VALUE='" + value + "'"
						+ (count == 1 ? " selected" : "") + ">";
				count++;
				String serv_type_name = (String) fields.get("oper_type_name");
				htmlStr += serv_type_name + "</OPTION>";

				fields = cursor.getNext();
			}
		}
		htmlStr += "</SELECT>";
		// logger.debug("htmlStr=" + htmlStr);
		return htmlStr;
	}

	/**
	 * 列表显示局向信息
	 * 
	 * @param flag
	 *            判断是否关联
	 * @param compare
	 *            比较值
	 * @param rename
	 *            重命名
	 * @return String
	 */
	public String getOfficeList(boolean flag, String compare, String rename) {
		PrepareSQL psql = new PrepareSQL(m_OfficeList_SQL);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(m_OfficeList_SQL);
		String strOfficeList = FormUtil.createListBox(cursor, "office_id",
				"office_name", flag, compare, rename);

		return strOfficeList;
	}

	/**
	 * 列表显示小区信息
	 * 
	 * @param flag
	 *            判断是否关联
	 * @param compare
	 *            比较值
	 * @param rename
	 *            重命名
	 * @return String
	 */
	public String getZoneList(boolean flag, String compare, String rename) {
		PrepareSQL psql = new PrepareSQL(m_ZoneList_SQL);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(m_ZoneList_SQL);
		String strZoneList = FormUtil.createListBox(cursor, "zone_id",
				"zone_name", flag, compare, rename);

		return strZoneList;
	}

	/**
	 * 得到所选用户关联的基本信息SSSS
	 * 
	 * 
	 * @return
	 */
	public Cursor getUserRelatedBaseInfo(HttpServletRequest request) {
		String user_id = request.getParameter("user_id");
		
		//String access_style_id = request.getParameter("access_style_id");
		/**
		 * modify by qixueqi
		 * 
		 */
		//String gather_id = request.getParameter("gather_id");
		StringBuffer relatedBaseInfo_SQL = new StringBuffer();
		String  str = "select a.*,b.cpe_mac,ac.type_name,c.spec_name from  gw_access_type ac , tab_bss_dev_port c, tab_hgwcustomer a left join tab_gw_device b on a.device_id=b.device_id " 
			+" where ac.type_id=a.access_style_id and a.spec_id=c.id and a.user_id="+ user_id ;
		relatedBaseInfo_SQL.append(str);
		//if (access_style_id != null && !"".equals(access_style_id))
	//	{   
		//	relatedBaseInfo_SQL.append(" and ac.type_id=" + access_style_id);
			
		//}
						
		PrepareSQL psql = new PrepareSQL(relatedBaseInfo_SQL.toString());
    	psql.getSQL();
		cursor = DataSetBean.getCursor(relatedBaseInfo_SQL.toString());

		return cursor;
	}
/**
 * 修改人：姚重亮
 * 修改时间：2010.9.6
 * 修改内容：注释掉方法getUserRelatedSerInfo（表tab_change_service_user已删除）
 */
//	/**
//	 * 得到所选用户当前所使用的业务的变更信息
//	 * 
//	 * 
//	 * @return
//	 */
//	public Cursor getUserRelatedSerInfo(String user_id, boolean isAll) {
//		String isAllStr = "";
//		if (!isAll) {
//			isAllStr = "and b.service_status=1";
//		}
//		String serInfo_SQL = "select b.user_id, a.service_id, a.service_name,a.service_desc, b.change_time, b.service_status "
//				+ "from tab_service a, tab_change_service_user b "
//				+ "where a.service_id=b.service_id "
//				+ isAllStr
//				+ " and b.user_id =" + user_id + " order by b.change_time desc";
//		PrepareSQL psql = new PrepareSQL(serInfo_SQL);
//    	psql.getSQL();
//		cursor = DataSetBean.getCursor(serInfo_SQL);
//		return cursor;
//	}

	/**
	 * 获得当前用户已开通业务
	 * 
	 * @return
	 */
	public List getUserCurRelatedSerInfo(String userId) {

		Cursor cor = null;
		List retnList = new ArrayList();
		String servSql = "select username from tab_hgwcustomer where user_id = "
				+ userId;
		PrepareSQL psql = new PrepareSQL(servSql);
    	psql.getSQL();
		Map map = DataSetBean.getRecord(servSql);
		if (map != null) {
			servSql = "select serv_type_id from tab_gw_user_serv where username = '"
					+ map.get("username") + "'";
			psql = new PrepareSQL(servSql);
	    	psql.getSQL();
			cor = DataSetBean.getCursor(servSql);
			if ((map = cor.getNext()) != null) {
				retnList.add(Global.Serv_type_Map.get(map.get("serv_type_id")));
			}
		}
		return retnList;
	}

	/**
	 * 获得所有的服务信息
	 * 
	 * 
	 * @return
	 */
	public Cursor getServiceInfo() {
		String service_SQL = "select * from tab_gw_serv_type where serv_type_id < 20";
		PrepareSQL psql = new PrepareSQL(service_SQL);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(service_SQL);
		return cursor;
	}
	
	/**
	 * 得到所选用户当前可以使用的业务
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Cursor getServiceInfo(String user_id) {
		Cursor cursor = null;
		
		String serInfoSQL = "select a.serv_type_id,a.serv_type_name,b.username,b.wan_type,b.serv_status,b.passwd from tab_gw_serv_type a, hgwcust_serv_info b where a.serv_type_id=b.serv_type_id and b.user_id="+user_id;
		PrepareSQL psql = new PrepareSQL(serInfoSQL);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(serInfoSQL);
		
		return cursor;
	}

	/**
	 * 得到所选用户所关联的设备历史信息
	 * 
	 * 
	 * @return
	 */
	public Cursor getUserRelatedDevInfo(String user_id) {

		String serInfo_SQL = "select a.device_serialnumber, a.oui, a.loopback_ip,b.change_time "
				+ "from tab_gw_device a, tab_change_device_user b "
				+ "where a.device_id=b.device_id  and b.user_id ="
				+ user_id
				+ " order by b.change_time desc";
		PrepareSQL psql = new PrepareSQL(serInfo_SQL);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(serInfo_SQL);
		return cursor;
	}

	/**
	 * 得到所选用户的管理域
	 * 
	 * 
	 * @return
	 */
	public Cursor getUserArea(String user_id) {

		String serInfo_SQL = "select a.area_name from tab_area a, tab_gw_res_area b "
				+ " where a.area_id = b.area_id and b.res_type = 1 and b.res_id = (select d.device_id from "
				+ "  tab_hgwcustomer c,tab_gw_device d where c.user_id ="
				+ user_id
				+ " and c.device_id=d.device_id)";
		PrepareSQL psql = new PrepareSQL(serInfo_SQL);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(serInfo_SQL);
		return cursor;
	}

	/**
	 * 得到所选用户的管理域
	 * 
	 * 
	 * @return
	 */
	public Cursor getEGWUserArea(String user_id) {

		String serInfo_SQL = "select a.area_name from tab_area a, tab_gw_res_area b "
				+ " where a.area_id = b.area_id and b.res_type = 1 and b.res_id = (select d.device_id from "
				+ "  tab_egwcustomer c,tab_gw_device d where c.user_id ="
				+ user_id
				+ " and c.device_id=d.device_id)";
		PrepareSQL psql = new PrepareSQL(serInfo_SQL);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(serInfo_SQL);
		return cursor;
	}

	/**
	 * 获得所有的用户域
	 * 
	 * 
	 * @return
	 */
	public String getUserDomains() {
		String strUserDomains = "";
		String domain_SQL = "select area_id, area_name from tab_area";
		PrepareSQL psql = new PrepareSQL(domain_SQL);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(domain_SQL);
		Map fields = cursor.getNext();
		if (fields == null) {
			strUserDomains += "<select name='user_domain' class='bk'>";
			strUserDomains += "<option value='-2'>==此项没有记录==</option>";
			strUserDomains += "</select>";
		} else {
			strUserDomains += "<select name='user_domain' class='bk'>";
			strUserDomains += "<option value='-1'>==请选择==</option>";
			while (fields != null) {
				strUserDomains += "<option value='"
						+ (String) fields.get("area_id") + "|"
						+ (String) fields.get("area_name") + "'>"
						+ (String) fields.get("area_name") + "</option>";
				fields = cursor.getNext();
			}
			strUserDomains += "</select>";
		}
		return strUserDomains;
	}

	/**
	 * 所有HGW用户列表,分页
	 * 
	 * @return ArrayList（strBar,cursor）
	 */
	public ArrayList getHGWUsersCursor(HttpServletRequest request) {

		String filterStr = "";
		filterStr += ("&opt=" + request.getParameter("opt"));
		// ---------modify by zhaixf 加入属地判断
		UserRes userRes = (UserRes) request.getSession()
				.getAttribute("curUser");
		String cityId = userRes.getCityId();
		//String sql = "select city_id, parent_id from tab_city where city_id = '"
		//		+ cityId + "'";
		//HashMap map = DataSetBean.getMap(sql);
		// ---------
		String searchAction = request.getParameter("searchForUsers");
		// 用户统计的情况

		if ("statUsers".equals(searchAction)) {
			ArrayList list = getHGWUsersServCursor(request);

			return list;
		} else {
			// 用户检索的情况
			ArrayList list = new ArrayList();
			list.clear();
			String usernameType = request.getParameter("usernameType");
			String username = request.getParameter("username");
			String starttime = request.getParameter("starttime");
			String endtime = request.getParameter("endtime");
			String stroffset = request.getParameter("offset");
			String flg = request.getParameter("flg");
			String queryFlag = request.getParameter("queryFlag");

			int pagelen = 15;
			int offset;
			if (stroffset == null)
				offset = 1;
			else
				offset = Integer.parseInt(stroffset);
			
			StringBuffer sql = new StringBuffer();
			
			if("1".equals(usernameType)){
				sql.append(" select a.user_id, a.gather_id,a.user_state,a.username,a.cotno,a.cust_type_id,a.opmode,a.user_type_id,a.realname,a.phonenumber,a.device_serialnumber,a.dealdate,a.opendate, a.updatetime,a.serv_type_id,a.wan_type,a.oui,a.onlinedate,a.city_id,a.access_style_id from tab_hgwcustomer a where a.user_state in('1','2') ");
				if (null != username && !"".equals(username)) {
					sql.append(" and a.username='").append(username).append("'");
					filterStr += "&usernameType=" + usernameType;
					filterStr += "&username=" + username;
				}
			}else if("2".equals(usernameType)){
				sql.append(" select distinct a.user_id, a.gather_id,a.user_state,a.username,a.cotno,a.cust_type_id,a.opmode,a.user_type_id,a.realname,a.phonenumber,a.device_serialnumber,a.dealdate,a.opendate, a.updatetime,a.serv_type_id,a.wan_type,a.oui,a.onlinedate,a.city_id,a.access_style_id from tab_hgwcustomer a ,hgwcust_serv_info b where a.user_id=b.user_id and a.user_state in('1','2') and b.serv_type_id=10");
				if(null != username && !"".equals(username)){
					sql.append(" and b.username='").append(username).append("'");
					filterStr += "&usernameType=" + usernameType;
					filterStr += "&username=" + username;
				}
			}else if("3".equals(usernameType)){
				sql.append(" select distinct a.user_id, a.gather_id,a.user_state,a.username,a.cotno,a.cust_type_id,a.opmode,a.user_type_id,a.realname,a.phonenumber,a.device_serialnumber,a.dealdate,a.opendate, a.updatetime,a.serv_type_id,a.wan_type,a.oui,a.onlinedate,a.city_id,a.access_style_id from tab_hgwcustomer a ,hgwcust_serv_info b where a.user_id=b.user_id and a.user_state in('1','2') and b.serv_type_id=11");
				if(null != username && !"".equals(username)){
					sql.append(" and b.username='").append(username).append("'");
					filterStr += "&usernameType=" + usernameType;
					filterStr += "&username=" + username;
				}
			}else if("4".equals(usernameType)){
				sql.append(" select distinct a.user_id, a.gather_id,a.user_state,a.username,a.cotno,a.cust_type_id,a.opmode,a.user_type_id,a.realname,a.phonenumber,a.device_serialnumber,a.dealdate,a.opendate, a.updatetime,a.serv_type_id,a.wan_type,a.oui,a.onlinedate,a.city_id,a.access_style_id from tab_hgwcustomer a ,tab_voip_serv_param b where a.user_id=b.user_id and a.user_state in('1','2') ");
				if(null != username && !"".equals(username)){
					sql.append(" and b.voip_username='").append(username).append("'");
					filterStr += "&usernameType=" + usernameType;
					filterStr += "&username=" + username;
				}
			}else if("5".equals(usernameType)){
				sql.append(" select distinct a.user_id, a.gather_id,a.user_state,a.username,a.cotno,a.cust_type_id,a.opmode,a.user_type_id,a.realname,a.phonenumber,a.device_serialnumber,a.dealdate,a.opendate, a.updatetime,a.serv_type_id,a.wan_type,a.oui,a.onlinedate,a.city_id,a.access_style_id from tab_hgwcustomer a ,tab_voip_serv_param b where a.user_id=b.user_id and a.user_state in('1','2') ");
				if(null != username && !"".equals(username)){
					sql.append(" and b.voip_phone='").append(username).append("'");
					filterStr += "&usernameType=" + usernameType;
					filterStr += "&username=" + username;
				}
			}
			// 山西增加设备序列号查询方式
			else if("6".equals(usernameType)){
				sql.append(" select a.user_id, a.gather_id,a.user_state,a.username,a.cotno,a.cust_type_id,a.opmode,a.user_type_id,a.realname,a.phonenumber,a.device_serialnumber,a.dealdate,a.opendate, a.updatetime,a.serv_type_id,a.wan_type,a.oui,a.onlinedate,a.city_id,a.access_style_id from tab_hgwcustomer a where a.user_state in('1','2') ");
				if (null != username && !"".equals(username)) {
					sql.append(" and a.device_serialnumber like '%").append(username).append("'");
					filterStr += "&usernameType=" + usernameType;
					filterStr += "&username=" + username;
				}
			}
			else{
				sql.append(" select a.user_id, a.gather_id,a.user_state,a.username,a.cotno,a.cust_type_id,a.opmode,a.user_type_id,a.realname,a.phonenumber,a.device_serialnumber,a.dealdate,a.opendate, a.updatetime,a.serv_type_id,a.wan_type,a.oui,a.onlinedate,a.city_id,a.access_style_id from tab_hgwcustomer a where a.user_state in('1','2') ");
				if (null != username && !"".equals(username)) {
					sql.append(" and a.username='").append(username).append("'");
					filterStr += "&usernameType=" + usernameType;
					filterStr += "&username=" + username;
				}
			}
			
			DateTimeUtil dt = null;

			if (starttime != null && !starttime.equals("")){
				filterStr += "&starttime=" + starttime;
				dt = new DateTimeUtil(starttime);
				starttime = String.valueOf(dt.getLongTime());
				sql.append(" and a.opendate>=");
				sql.append(starttime);
			}
			if (endtime != null && !endtime.equals("")){
				filterStr += "&endtime=" + endtime;
				dt = new DateTimeUtil(endtime);
				endtime = String.valueOf(dt.getLongTime());
				sql.append(" and a.opendate<");
				sql.append(endtime);
			}
				
			if (flg != null && !flg.equals("")) {
				filterStr += "&flg=" + flg;
			}
			if (queryFlag != null && !queryFlag.equals("")) {
				filterStr += "&queryFlag=" + queryFlag;
			}
			sql.append(" and user_state in ('1','2') ");
			
			// -----------zhaixf----- 20090925之前
			// -----------漆学启------ 20090925
			Map<String, String> cityMap = CityDAO.getCityIdPidMap();
			if (!"-1".equals(cityMap.get(cityId))) {
				List list1 = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append(" and city_id in (");
				sql.append(StringUtils.weave(list1));
				sql.append(")");
				list1 = null;
				
			}
			cityMap = null;
			
			// -----------------

			PrepareSQL psql = new PrepareSQL(sql.toString());
	    	psql.getSQL();

	    	logger.warn(sql.toString());
			QueryPage qryp = new QueryPage();
			qryp.initPage(sql.toString(), offset, pagelen);
			String strBar = qryp.getPageBar(filterStr);
			list.add(strBar);
			cursor = DataSetBean
					.getCursor(sql.toString(), offset, pagelen);
			list.add(cursor);

			return list;
		}
	}

	/**
	 * 所有HGW用户列表,分页
	 * 
	 * @return ArrayList（strBar,cursor）
	 */
	public ArrayList getHGWUsersCursorExcel(HttpServletRequest request) {
		
		// ---------modify by zhaixf 加入属地判断
		UserRes userRes = (UserRes) request.getSession()
				.getAttribute("curUser");
		String cityId = userRes.getCityId();
		//String sql = "select city_id, parent_id from tab_city where city_id = '"
		//		+ cityId + "'";
		//HashMap map = DataSetBean.getMap(sql);
		// ---------
		String searchAction = request.getParameter("searchForUsers");
		// 用户统计的情况
		if ("statUsers".equals(searchAction)) {
			ArrayList list = getHGWUsersServCursor(request);

			return list;
		} else {
			// 用户检索的情况
			ArrayList list = new ArrayList();
			list.clear();
			String username = request.getParameter("username");
			String starttime = request.getParameter("starttime");
			String endtime = request.getParameter("endtime");
			String flg = request.getParameter("flg");

			StringBuffer sql = new StringBuffer();
			sql.append(m_HGWUsers_List_SQL);
			
			sql.append(" where 1 = 1");
			if (username != null)
			{
				username = username.trim();
				if (username.length() != 0)
				{
					sql.append(" and username ='");
					sql.append(username);
					sql.append("' ");
				}
			}
			DateTimeUtil dt = null;
//			if ("js_dx".equals(Global.INST_AREA_SHORT_NAME))
//			{
//				if (starttime != null && !starttime.equals(""))
//				{
//					dt = new DateTimeUtil(starttime);
//					starttime = String.valueOf(dt.getLongTime());
//					sql.append(" and onlinedate>=");
//					sql.append(starttime);
//				}
//				if (endtime != null && !endtime.equals(""))
//				{
//					dt = new DateTimeUtil(endtime);
//					endtime = String.valueOf(dt.getLongTime());
//					sql.append(" and onlinedate<");
//					sql.append(endtime);
//				}
//			}
//			else
//			{
//				if (starttime != null && !starttime.equals(""))
//				{
//					dt = new DateTimeUtil(starttime);
//					starttime = String.valueOf(dt.getLongTime());
//					sql.append(" and opendate>=");
//					sql.append(starttime);
//				}
//				if (endtime != null && !endtime.equals(""))
//				{
//					dt = new DateTimeUtil(endtime);
//					endtime = String.valueOf(dt.getLongTime());
//					sql.append(" and opendate<");
//					sql.append(endtime);
//				}
//			}
			
			if (starttime != null && !starttime.equals(""))
			{
				dt = new DateTimeUtil(starttime);
				starttime = String.valueOf(dt.getLongTime());
				sql.append(" and opendate>=");
				sql.append(starttime);
			}
			if (endtime != null && !endtime.equals(""))
			{
				dt = new DateTimeUtil(endtime);
				endtime = String.valueOf(dt.getLongTime());
				sql.append(" and opendate<");
				sql.append(endtime);
			}
			
			if (flg == null) {
				sql.append(" and user_state in ('1','2') ");
			}
			
			// -----------zhaixf-----
			Map<String, String> cityMap = CityDAO.getCityIdPidMap();
			if (!"-1".equals(cityMap.get(cityId))) {
				List list1 = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append(" and city_id in (");
				sql.append(StringUtils.weave(list1));
				sql.append(")");
				list1 = null;
			}
			cityMap = null;
			
			PrepareSQL psql = new PrepareSQL(sql.toString());
	    	psql.getSQL();
			// -----------------
			cursor = DataSetBean.getCursor(sql.toString());
			list.add(cursor);

			return list;
		}
	}

	/**
	 * 统计是否开通某业务的HGW用户列表
	 * 
	 * @return ArrayList（strBar,cursor）
	 */
	public ArrayList getHGWUsersServCursor(HttpServletRequest request) {

		String filterStr = "";
		String strSQL = "";
		String rtnMessage = "";
		String optinalMessage = "";

		String service_id = request.getParameter("some_service");
		String service_status = request.getParameter("radioBtn");
		String dev_id = request.getParameter("dev_id");
		String cpeCurrentStatus = request.getParameter("CPE_CurrentStatus");
		String user_domain = request.getParameter("user_domain");
		String dev_ip = request.getParameter("dev_ip");
		String vendorName = request.getParameter("vendorName");
		String devModelName = request.getParameter("devModelName");
		String osVersionName = request.getParameter("osVersionName");
		ArrayList list = new ArrayList();
		list.clear();

		String stroffset = request.getParameter("offset");
		int pagelen = 15;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);

		// HttpSession session = request.getSession();
		// UserRes curUser = (UserRes) session.getAttribute("curUser");

		if (dev_id != null) {
			m_serv_users_List_SQL += " and a.device_id = '" + dev_id + "'";
			optinalMessage += ",设备ID为\"" + dev_id + "\"";

			filterStr += "";
		}
		if (vendorName != null) {
			m_serv_users_List_SQL += " and a.device_serialnumber in (select device_serialnumber from tab_gw_device where oui='"
					+ vendorName + "')";
			optinalMessage += ",设备厂商为\"" + vendorName + "\"";
		}
		if (devModelName != null) {
//			m_serv_users_List_SQL += " and a.device_serialnumber in (select device_serialnumber from tab_gw_device where devicetype_id in (select devicetype_id from tab_devicetype_info where device_model='"
			m_serv_users_List_SQL += " and a.device_serialnumber in (select device_serialnumber from tab_gw_device where device_model_id in (select device_model_id from gw_device_model where device_model='"
			+ devModelName + "'))";
			optinalMessage += ",设备类型为\"" + devModelName + "\"";
		}
		if (osVersionName != null) {
			m_serv_users_List_SQL += " and a.device_serialnumber in (select device_serialnumber from tab_gw_device where devicetype_id in (select devicetype_id from tab_devicetype_info where softwareversion='"
					+ osVersionName + "'))";
			optinalMessage += ",软件版本为\"" + osVersionName + "\"";
		}
		if (cpeCurrentStatus != null) {
			m_serv_users_List_SQL += " and a.device_serialnumber in (select device_serialnumber from tab_gw_device where cpe_currentstatus="
					+ cpeCurrentStatus + ")";
			if ("1".equals(cpeCurrentStatus)) {
				optinalMessage += ",在线的";
			} else {
				optinalMessage += ",不在线的";
			}

		}
		if (user_domain != null) {
			m_serv_users_List_SQL += " and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id="
					+ user_domain.split("\\|")[0] + ")";
			optinalMessage += ",管理域为\"" + user_domain.split("\\|")[1] + "\"";
		}
		if (dev_ip != null) {
			m_serv_users_List_SQL += " and a.device_serialnumber in (select device_serialnumber from tab_gw_device where loopback_ip='"
					+ dev_ip + "')";
			optinalMessage += ",设备IP为\"" + dev_ip + "\"";
		}

		pSQL.setSQL(m_serv_users_List_SQL);
		pSQL.setInt(1, Integer.parseInt(service_id.split("\\|")[0]));
		pSQL.setString(2, service_status);
		if (Integer.parseInt(service_status) == 0) {
			rtnMessage = "未开通";
		} else {
			rtnMessage = "已开通";
		}
		rtnMessage += "\"" + service_id.split("\\|")[1] + "\"业务";
		rtnMessage += optinalMessage;
		strSQL = pSQL.getSQL();
		

		QueryPage qryp = new QueryPage();
		qryp.initPage(strSQL, offset, pagelen);
		String strBar = qryp.getPageBar();
		list.add(strBar);
		cursor = DataSetBean.getCursor(strSQL, offset, pagelen);
		list.add(cursor);
		list.add(rtnMessage);
		return list;
	}

	/**
	 * 通过设备IP查询设备内部ID
	 * 
	 * @param _deviceIP
	 * @return string
	 */
	public String getDeviceIdByIP(String _deviceIP) {
		String deviceId = "";
		String strSQL = "";
		if (_deviceIP != null) {
			strSQL = "select device_id from tab_gw_device where loopback_ip = "
					+ "'" + _deviceIP + "'";
			PrepareSQL psql = new PrepareSQL(strSQL);
	    	psql.getSQL();
			Map resDS = DataSetBean.getRecord(strSQL);
			if (resDS.size() > 0) {
				deviceId = (String) resDS.get("device_id");
			}
		}
		return deviceId;
	}

	/**
	 * 增加、删除、更新
	 * 
	 * @param request
	 * @return String
	 */
	public String HGWUserInfoActExe(HttpServletRequest request) {
		String strSQL = "";
		String strMsg = "";
		String strAction = request.getParameter("action");
		
		//FIXME
		List<String> userIds = new ArrayList<String>();
		List<String> userNames = new ArrayList<String>();

		String oprateUserId = ((UserRes) request
				.getSession().getAttribute("curUser")).getUser().getAccount();
		long time = new Date().getTime();
		time = time/1000;
		
		if (strAction.equals("delete")) { // 删除操作
			String str_user_id = request.getParameter("user_id");
			String str_gather_id = request.getParameter("gather_id");
			int int_user_id = Integer.parseInt(str_user_id);

//			logger.info("delete + user_id=====" + str_user_id);
			//查询UserName
			userIds.add(str_user_id);
			userNames.add(getUserName(str_user_id));
			
			 /**
			 * add 2009/03/19 by qixueqi
			 * if user having device,then return;else countiune
			 */
			String hasdevicesql = "select device_id from tab_hgwcustomer where user_id=" + int_user_id ;
			PrepareSQL psql = new PrepareSQL(hasdevicesql);
	    	psql.getSQL();
			HashMap hasMap = DataSetBean.getRecord(hasdevicesql);
		    
			if (null != hasMap && !hasMap.isEmpty()) {
				
				if(null==hasMap.get("device_id") || "".equals(hasMap.get("device_id"))){
					logger.debug(hasMap.toString());
				}else{
					return "该用户已绑定设备，删除设备前请先解绑！";
				}
			}
		    
//			strSQL += "update tab_hgwcustomer set user_state = '0' where user_id="
//					+ int_user_id + ";";
			//删除用户，把用户移到_bak表中
			if(DBUtil.GetDB() == DB_MYSQL) 
			{// mysql
				backHgwcustomer(int_user_id);	
			}
			else {
				strSQL += "insert into tab_hgwcustomer_bak select * from tab_hgwcustomer where user_id="
						+ int_user_id + ";";
			}
			
			strSQL += "update tab_hgwcustomer_bak set updatetime=" + time + ",staff_id='" 
					+ oprateUserId + "' where user_id=" + int_user_id + ";";
			
			strSQL += "delete from tab_hgwcustomer where user_id=" + int_user_id + ";";
			
			//删除业务用户信息
			strSQL += "delete from hgwcust_serv_info where user_id=" + int_user_id + ";";
			
//			// by zhaixf
//			String OUIsql = "select oui, device_serialnumber from tab_hgwcustomer  where user_id="
//					+ int_user_id;
//
//			Map feild = DataSetBean.getRecord(OUIsql);
//			if (feild != null) {
//				strSQL += "update tab_gw_device set cpe_allocatedstatus=0 "
//						+ "where oui='" + feild.get("oui")
//						+ "' and device_serialnumber='"
//						+ feild.get("device_serialnumber") + "';";
//			}
		} else if (strAction.equals("deleteBatch")) { // 删除操作
			String[] arr_isCheckedToDel = request
					.getParameterValues("isCheckedToDel");
			for (int i = 0; i < arr_isCheckedToDel.length; i++) {
				String str_user_id = arr_isCheckedToDel[i].split("\\|")[0];
				String str_gather_id = arr_isCheckedToDel[i].split("\\|")[1];
				int int_user_id = Integer.parseInt(str_user_id);

//				logger.info("deleteBatch + user_id=====" + str_user_id);
				//查询UserName
				userIds.add(str_user_id);
				userNames.add(getUserName(str_user_id));
				
//				strSQL += "update tab_hgwcustomer set user_state = '0' where user_id="
//						+ int_user_id + ";";
				//删除用户，把用户移到_bak表中
				
				if(DBUtil.GetDB() == DB_MYSQL) 
				{// mysql
					backHgwcustomer(int_user_id);	
				}
				else {
					strSQL += "insert into tab_hgwcustomer_bak select * from tab_hgwcustomer where user_id="
							+ int_user_id + ";";
				}
				
				strSQL += "update tab_hgwcustomer_bak set updatetime=" + time + ",staff_id='" 
						+ oprateUserId + "' where user_id=" + int_user_id + ";";
				strSQL += "delete from tab_hgwcustomer where user_id=" + int_user_id + ";";

				//删除业务用户信息
				strSQL += "delete from hgwcust_serv_info where user_id=" + int_user_id + ";";
				
				// by zhaixf
				String OUIsql = "select oui, device_serialnumber from tab_hgwcustomer  where user_id="
						+ int_user_id;
				// Cursor cursor
				PrepareSQL psql = new PrepareSQL(OUIsql);
		    	psql.getSQL();
				Map feild = DataSetBean.getRecord(OUIsql);
				if (feild != null) {
					strSQL += "update tab_gw_device set cpe_allocatedstatus=0 "
							+ "where oui='" + feild.get("oui")
							+ "' and device_serialnumber='"
							+ feild.get("device_serialnumber") + "';";
				}
				// strSQL += "delete from tab_gw_device where device_id=(select
				// device_id from tab_hgwcustomer where user_id="
				// + int_user_id
				// + " and gather_id='"
				// + str_gather_id
				// + "')";
				if (i != arr_isCheckedToDel.length - 1) {
					strSQL += ";";
				}
			}
		} else {

			String gather_id = request.getParameter("gather_id");
			String username = request.getParameter("username");
			String passwd = request.getParameter("passwd");
			String city_id = request.getParameter("city_id");
			String cotno = request.getParameter("cotno");
			String bill_type_id = request.getParameter("bill_type_id");
			String next_bill_type_id = request
					.getParameter("next_bill_type_id");
			String cust_type_id = request.getParameter("cust_type_id");
			String user_type_id = request.getParameter("user_type_id");
			String bindtype = request.getParameter("bindtype");
			String virtualnum = request.getParameter("virtualnum");
			String numcharacter = request.getParameter("numcharacter");
			String access_style_id = request.getParameter("access_style_id");
			String aut_flag = request.getParameter("aut_flag");
			String service_set = request.getParameter("service_set");
			String realname = request.getParameter("realname");
			String sex = request.getParameter("sex");
			String cred_type_id = request.getParameter("cred_type_id");
			String credno = request.getParameter("credno");

			String address = request.getParameter("address");
			String office_id = request.getParameter("office_id");
			String zone_id = request.getParameter("zone_id");
			String access_kind_id = request.getParameter("access_kind_id");
			String trade_id = request.getParameter("trade_id");
			String licenceregno = request.getParameter("licenceregno");
			String occupation_id = request.getParameter("occupation_id");
			String education_id = request.getParameter("education_id");
			String vipcardno = request.getParameter("vipcardno");
			String contractno = request.getParameter("contractno");
			String linkman = request.getParameter("linkman");
			String linkman_credno = request.getParameter("linkman_credno");
			String linkphone = request.getParameter("linkphone");
			String linkaddress = request.getParameter("linkaddress");
			String mobile = request.getParameter("mobile");
			String email = request.getParameter("email");
			String agent = request.getParameter("agent");

			String agent_credno = request.getParameter("agent_credno");
			String agentphone = request.getParameter("agentphone");
			String adsl_res = request.getParameter("adsl_res");
			String adsl_card = request.getParameter("adsl_card");
			String adsl_dev = request.getParameter("adsl_dev");
			String adsl_ser = request.getParameter("adsl_ser");
			String isrepair = request.getParameter("isrepair");
			String bandwidth = request.getParameter("bandwidth");
			String ipaddress = request.getParameter("ipaddress");
			String overipnum = request.getParameter("overipnum");
			String ipmask = request.getParameter("ipmask");
			String gateway = request.getParameter("gateway");
			String macaddress = request.getParameter("macaddress");
			String device_id = request.getParameter("device_id");// request.getParameter("device_id");

			String device_ip = request.getParameter("device_ip");
			String device_shelf = request.getParameter("device_shelf");
			String device_frame = request.getParameter("device_frame");

			String device_slot = request.getParameter("device_slot");
			String device_port = request.getParameter("device_port");
			String basdevice_id = request.getParameter("basdevice_id");
			String basdevice_ip = request.getParameter("basdevice_ip");
			String basdevice_shelf = request.getParameter("basdevice_shelf");
			String basdevice_frame = request.getParameter("basdevice_frame");
			String basdevice_slot = request.getParameter("basdevice_slot");
			String basdevice_port = request.getParameter("basdevice_port");
			String vlanid = request.getParameter("vlanid");
			String workid = request.getParameter("workid");
			String user_state = request.getParameter("user_state");
			String opendate = request.getParameter("hidOpenDate");
			String onlinedate = request.getParameter("hidOnlineDate");
			String pausedate = request.getParameter("hidPauseDate");
			String closedate = request.getParameter("hidCloseDate");
			String updatetime = request.getParameter("hidUpdateTime");
			String staff_id = request.getParameter("staff_id");
			String remark = request.getParameter("remark");
			String phonenumber = request.getParameter("phonenumber");
			String cableid = request.getParameter("cableid");
			String bwlevel = request.getParameter("bwlevel");
			String vpiid = request.getParameter("vpiid");
			String vciid = request.getParameter("vciid");
			String adsl_hl = request.getParameter("adsl_hl");
			String userline = request.getParameter("userline");
			if (null == userline || "".equals(userline))
				userline = String.valueOf(UserInstAct.ITMSUSERLINE);
			String dslamserialno = request.getParameter("dslamserialno");
			String movedate = request.getParameter("hidMoveDate");

			String dealdate = request.getParameter("hidDealDate");
			String opmode = request.getParameter("opmode");
			String maxattdnrate = request.getParameter("maxattdnrate");
			String upwidth = request.getParameter("upwidth");
			String oui = request.getParameter("oui");
			String device_serialnumber = request.getParameter("vender");
			String service_id = request.getParameter("some_service");
			String max_user_number = request.getParameter("max_user_number");
			String wan_type = request.getParameter("wan_type");
			if (wan_type == null || wan_type.equals("")) {
				wan_type = "1";
			}
			// if (!str_device_id.equals("0")) {
			// DeviceAct deviceAct = new DeviceAct();
			// str_gather_id =
			// String.valueOf(deviceAct.getDeviceInfoMap(str_device_id).get("gather_id"));
			// str_device_ip =
			// String.valueOf(deviceAct.getDeviceInfoMap(str_device_id).get("loopback_ip"));
			// }
			//
			// if (Integer.parseInt(str_basdevice_id) > 0) {
			// DeviceAct deviceAct = new DeviceAct();
			// str_basdevice_ip =
			// String.valueOf(deviceAct.getDeviceInfoMap(str_basdevice_id).get("loopback_ip"));
			// }

			// String[] arr_ipaddress = request.getParameterValues("ipaddress");

			if (strAction.equals("add")) {

				if (oui != null && !"".equals(oui)) {
					int count = checkDevice(oui, device_serialnumber,
							service_id);
					if (count > 0) {
						return "该家庭网关设备已开通此业务，请重新选择业务或设备！";
					}
				}

//				// 取得新的userid by zhaixf
				long userid = getMaxUserId();
				str_userid = String.valueOf(userid);
				
				pSQL.setSQL(m_HGWUserInfoAdd_SQL);
				// Integer.parseInt(str_userid)
				pSQL.setInt(1, Integer.parseInt(str_userid));
				pSQL.setString(2, gather_id);

				pSQL.setString(3, username);
				pSQL.setString(4, passwd);
				pSQL.setString(5, city_id);

				pSQL.setString(6, cotno);

				if (bill_type_id != null && !bill_type_id.equals("")) {
					pSQL.setInt(7, Integer.parseInt(bill_type_id));
				} else {
					pSQL.setString(7, ",,");
				}
				if (next_bill_type_id != null && !next_bill_type_id.equals("")) {
					pSQL.setInt(8, Integer.parseInt(next_bill_type_id));
				} else {
					pSQL.setString(8, ",,");
				}
				if (cust_type_id != null && !cust_type_id.equals("")) {
					pSQL.setInt(9, Integer.parseInt(cust_type_id));
				} else {
					pSQL.setString(9, ",,");
				}

				pSQL.setString(10, "3");

				if (bindtype != null && !bindtype.equals("")) {
					pSQL.setInt(11, Integer.parseInt(bindtype));
				} else {
					pSQL.setString(11, ",,");
				}

				if (virtualnum != null && virtualnum.length() != 0) {
					pSQL.setInt(12, Integer.parseInt(virtualnum));
				} else {
					pSQL.setString(12, ",,");
				}
				pSQL.setString(13, numcharacter);

				if (access_style_id != null && !access_style_id.equals("")) {
					pSQL.setInt(14, Integer.parseInt(access_style_id));
				} else {
					pSQL.setString(14, ",,");
				}

				pSQL.setString(15, aut_flag);

				pSQL.setString(17, realname);
				pSQL.setString(18, sex);

				if (cred_type_id != null && !cred_type_id.equals("")) {
					pSQL.setInt(19, Integer.parseInt(cred_type_id));
				} else {
					pSQL.setString(19, ",,");
				}

				pSQL.setString(20, credno);

				pSQL.setString(21, address);
				pSQL.setString(22, office_id);
				pSQL.setString(23, zone_id);
				// if (office_id != null && office_id.length() != 0) {
				// pSQL.setInt(22, Integer.parseInt(office_id));
				// } else {
				// pSQL.setString(22, "null");
				// }
				//
				// if (zone_id != null && zone_id.length() != 0) {
				// pSQL.setInt(23, Integer.parseInt(zone_id));
				// } else {
				// pSQL.setString(23, "null");
				// }
				if (access_kind_id != null && !access_kind_id.equals("")) {
					pSQL.setInt(24, Integer.parseInt(access_kind_id));
				} else {
					pSQL.setString(24, ",,");
				}

				if (trade_id != null && !trade_id.equals("")) {
					pSQL.setInt(25, Integer.parseInt(trade_id));
				} else {
					pSQL.setString(25, ",,");
				}

				pSQL.setString(26, licenceregno);

				if (occupation_id != null && !occupation_id.equals("")) {
					pSQL.setInt(27, Integer.parseInt(occupation_id));
				} else {
					pSQL.setString(27, ",,");
				}

				if (education_id != null && !education_id.equals("")) {
					pSQL.setInt(28, Integer.parseInt(education_id));
				} else {
					pSQL.setString(28, ",,");
				}

				pSQL.setString(29, vipcardno);
				pSQL.setString(30, contractno);
				pSQL.setString(31, linkman);
				pSQL.setString(32, linkman_credno);
				pSQL.setString(33, linkphone);
				pSQL.setString(34, linkaddress);
				pSQL.setString(35, mobile);
				pSQL.setString(36, email);
				pSQL.setString(37, agent);
				pSQL.setString(38, agent_credno);
				pSQL.setString(39, agentphone);

				if (adsl_res != null && !adsl_res.equals("")) {
					pSQL.setInt(40, Integer.parseInt(adsl_res));
				} else {
					pSQL.setString(40, ",,");
				}

				pSQL.setString(41, adsl_card);
				pSQL.setString(42, adsl_dev);
				pSQL.setString(43, adsl_ser);

				pSQL.setString(44, isrepair);
				if (bandwidth != null && bandwidth.length() != 0) {
					pSQL.setInt(45, Integer.parseInt(bandwidth));
				} else {
					pSQL.setString(45, ",,");
				}

				pSQL.setString(46, ipaddress);
				if (overipnum != null && overipnum.length() != 0) {
					pSQL.setInt(47, Integer.parseInt(overipnum));
				} else {
					pSQL.setString(47, ",,");
				}

				pSQL.setString(48, ipmask);
				pSQL.setString(49, gateway);
				pSQL.setString(50, macaddress);
				pSQL.setString(51, device_id);
				pSQL.setString(52, device_ip);
				if (device_shelf != null && device_shelf.length() != 0) {
					pSQL.setInt(53, Integer.parseInt(device_shelf));
				} else {
					pSQL.setString(53, ",,");
				}

				if (device_frame != null && device_frame.length() != 0) {
					pSQL.setInt(54, Integer.parseInt(device_frame));
				} else {
					pSQL.setString(54, ",,");
				}

				if (device_slot != null && device_slot.length() != 0) {
					pSQL.setInt(55, Integer.parseInt(device_slot));
				} else {
					pSQL.setString(55, ",,");
				}

				if (device_port != null && device_port.length() != 0) {
					pSQL.setInt(56, Integer.parseInt(device_port));
				} else {
					pSQL.setString(56, ",,");
				}
				pSQL.setString(57, basdevice_id);
				pSQL.setString(58, basdevice_ip);
				if (basdevice_shelf != null && basdevice_shelf.length() != 0) {
					pSQL.setInt(59, Integer.parseInt(basdevice_shelf));
				} else {
					pSQL.setString(59, ",,");
				}

				if (basdevice_frame != null && basdevice_frame.length() != 0) {
					pSQL.setInt(60, Integer.parseInt(basdevice_frame));
				} else {
					pSQL.setString(60, ",,");
				}

				if (basdevice_slot != null && basdevice_slot.length() != 0) {
					pSQL.setInt(61, Integer.parseInt(basdevice_slot));
				} else {
					pSQL.setString(61, ",,");
				}
				if (basdevice_port != null && basdevice_port.length() != 0) {
					pSQL.setInt(62, Integer.parseInt(basdevice_port));
				} else {
					pSQL.setString(62, ",,");
				}

				// if (vlanid != null && vlanid.length() != 0) {
				pSQL.setString(63, vlanid);
				// } else {
				// pSQL.setString(63, ",,");
				// }

				pSQL.setString(64, workid);
				pSQL.setString(65, user_state);

				if (opendate != null && opendate.length() != 0) {
					pSQL.setInt(66, Integer.parseInt(opendate));
				} else {
					pSQL.setString(66, ",,");
				}

				if (onlinedate != null && onlinedate.length() != 0) {
					pSQL.setInt(67, Integer.parseInt(onlinedate));
				} else {
					pSQL.setString(67, ",,");
				}
				if (pausedate != null && pausedate.length() != 0) {
					pSQL.setInt(68, Integer.parseInt(pausedate));
				} else {
					pSQL.setString(68, ",,");
				}
				if (closedate != null && closedate.length() != 0) {
					pSQL.setInt(69, Integer.parseInt(closedate));
				} else {
					pSQL.setString(69, ",,");
				}
				if (updatetime != null && updatetime.length() != 0) {
					pSQL.setInt(70, Integer.parseInt(updatetime));
				} else {
					pSQL.setString(70, ",,");
				}
				pSQL.setString(71, staff_id);
				pSQL.setString(72, remark);
				pSQL.setString(73, phonenumber);
				pSQL.setString(74, cableid);

				if (bwlevel != null && bwlevel.length() != 0) {
					pSQL.setInt(75, Integer.parseInt(bwlevel));
				} else {
					pSQL.setString(75, ",,");
				}
				pSQL.setString(76, vpiid);

				if (vciid != null && vciid.length() != 0) {
					pSQL.setInt(77, Integer.parseInt(vciid));
				} else {
					pSQL.setString(77, ",,");
				}
				pSQL.setString(78, adsl_hl);

				/*
				 * if (userline != null && userline.length() != 0) {
				 * pSQL.setInt(79, Integer.parseInt(userline)); } else {
				 * pSQL.setString(79, ",,"); }
				 */
				pSQL.setInt(79, Integer.parseInt(userline));

				pSQL.setString(80, dslamserialno);

				if (movedate != null && movedate.length() != 0) {
					pSQL.setInt(81, Integer.parseInt(movedate));
				} else {
					pSQL.setString(81, ",,");
				}
				if (dealdate != null && dealdate.length() != 0) {
					pSQL.setInt(82, Integer.parseInt(dealdate));
				} else {
					pSQL.setString(82, ",,");
				}

				pSQL.setString(83, opmode);

				if (maxattdnrate != null && maxattdnrate.length() != 0) {
					pSQL.setInt(84, Integer.parseInt(maxattdnrate));
				} else {
					pSQL.setString(84, ",,");
				}
				if (upwidth != null && upwidth.length() != 0) {
					pSQL.setInt(85, Integer.parseInt(upwidth));
				} else {
					pSQL.setString(85, ",,");
				}

				pSQL.setString(86, oui);
				pSQL.setString(87, device_serialnumber);

				if (service_id != null && service_id.length() != 0) {
					pSQL.setInt(88, Integer.parseInt(service_id));
				} else {
					pSQL.setString(88, ",,");
				}

				if (max_user_number != null && max_user_number.length() != 0) {
					pSQL.setInt(89, Integer.parseInt(max_user_number));
				} else {
					pSQL.setString(89, ",,");
				}

				if (wan_type != null && wan_type.length() != 0) {
					pSQL.setInt(90, Integer.parseInt(wan_type));
				} else {
					pSQL.setString(90, ",,");
				}

				strSQL = pSQL.getSQL();
				strSQL += ";";
				//添加业务用户表
				//user_id,业务类型，用户名，业务状态，密码
				pSQL.setSQL(m_hgwcust_serv_info);
				pSQL.setInt(1, Integer.parseInt(str_userid));
				if (service_id != null && service_id.length() != 0) {
					pSQL.setInt(2, Integer.parseInt(service_id));
				} else {
					pSQL.setString(2, ",,");
				}
				
				pSQL.setString(3, username);
				pSQL.setInt(4, 1);
				pSQL.setString(5, passwd);	//密码
				//上网方式，VPI,VCI,VLANID
				
				if (wan_type != null && wan_type.length() != 0) {
					pSQL.setInt(6, Integer.parseInt(wan_type));
				} else {
					pSQL.setString(6, ",,");
				}
				pSQL.setString(7, vpiid);
				if (vciid != null && vciid.length() != 0) {
					pSQL.setInt(8, Integer.parseInt(vciid));
				} else {
					pSQL.setString(8, ",,");
				}
				
				pSQL.setString(9, vlanid);
				//IP地址，掩码，网关，DNS，绑定端口
				pSQL.setString(10, ipaddress);
				pSQL.setString(11, ipmask);
				pSQL.setString(12, gateway);
				pSQL.setString(13, adsl_ser);
				pSQL.setString(14, ",,");
				//开通状态，受理时间，开通时间，暂停时间，更新时间
				pSQL.setInt(15, 0);	//	开通状态暂时为0(未做)
				
				if (dealdate != null && dealdate.length() != 0) {
					pSQL.setInt(16, Integer.parseInt(dealdate));
				} else {
					pSQL.setString(16, ",,");
				}
				if (opendate != null && opendate.length() != 0) {
					pSQL.setInt(17, Integer.parseInt(opendate));
				} else {
					pSQL.setString(17, ",,");
				}
				if (pausedate != null && pausedate.length() != 0) {
					pSQL.setInt(18, Integer.parseInt(pausedate));
				} else {
					pSQL.setString(18, ",,");
				}
				if (updatetime != null && updatetime.length() != 0) {
					pSQL.setInt(19, Integer.parseInt(updatetime));
				} else {
					pSQL.setString(19, ",,");
				}

				strSQL += pSQL.getSQL();
					
				// by zhaixf update tab_gw_device
				if (oui != null && device_serialnumber != null
						&& !oui.equals("") && !device_serialnumber.equals("")) {
					strSQL += ";update tab_gw_device set device_status=1, cpe_allocatedstatus=1"
							+ ",city_id='"
							+ city_id
							+ "'"
							+ " where oui='"
							+ oui
							+ "' and device_serialnumber='"
							+ device_serialnumber + "'";
					// 添加用户时，当设备信息不为空的时候，向网关域权限表添加纪录
					// 当该设备存在于表中时，先删除该设备信息。
					strSQL += "; delete from tab_gw_res_area where res_id='"
							+ device_id + "'" + " and res_type=1";
					// 获得当前用户域ID
					String curAreaId = getAreaId(city_id);
					if (null == curAreaId) {
						curAreaId = String.valueOf(((UserRes) request
								.getSession().getAttribute("curUser"))
								.getAreaId());
					}
					strSQL += insertAreaTable(device_id, curAreaId);

				}

				strSQL = strSQL.replaceAll("',,'", "null");
				strSQL = strSQL.replaceAll(",\\)", ",null)");
			} else {
				str_user_id = request.getParameter("user_id");
				// 用于判断时候用户表中该用户已经绑定设备的oui和序列号
				String strSql = "select oui,device_serialnumber,device_id from tab_hgwcustomer where user_id = "
						+ str_user_id;
				PrepareSQL psql = new PrepareSQL(strSql);
		    	psql.getSQL();
				Map mp = DataSetBean.getRecord(strSql);
				if (mp != null) {
					// 是更换设备操作,将被更换设备的绑定状态，置为“未绑定”；并且把设备表里面的
					strSQL = "update tab_gw_device set cpe_allocatedstatus=0 where oui='"
							+ mp.get("oui")
							+ "' and device_serialnumber = '"
							+ mp.get("device_serialnumber") + "'";

					// 当该设备存在于表中时，先删除该设备信息。
					strSQL += "; delete from tab_gw_res_area where res_id='"
							+ mp.get("device_id") + "'" + " and res_type=1";

					// 获得当前用户域ID
					String curAreaId = getAreaId(city_id);
					if (null == curAreaId) {
						curAreaId = String.valueOf(((UserRes) request
								.getSession().getAttribute("curUser"))
								.getAreaId());
					}
					strSQL += insertAreaTable(device_id, curAreaId);
				}
				strSQL += ";update tab_hgwcustomer set gather_id='" + gather_id
						+ "',username='" + username + "',passwd='" + passwd
						+ "',city_id='" + city_id + "',cotno='" + cotno
						+ "',bill_type_id=" + bill_type_id
						+ ",next_bill_type_id=" + next_bill_type_id
						+ ",cust_type_id=" + cust_type_id + ",user_type_id='"
						+ user_type_id + "',bindtype=" + bindtype
						+ ",virtualnum=" + virtualnum + ",numcharacter='"
						+ numcharacter + "',access_style_id=" + access_style_id
						+ ",aut_flag='" + aut_flag + "',service_set='"
						+ service_set + "',realname='" + realname + "',sex='"
						+ sex + "',cred_type_id=" + cred_type_id + ",credno='"
						+ credno + "',address='" + address + "',office_id='"
						+ office_id + "',zone_id='" + zone_id
						+ "',access_kind_id=" + access_kind_id + ",trade_id="
						+ trade_id + ",licenceregno='" + licenceregno
						+ "',occupation_id=" + occupation_id + ",education_id="
						+ education_id + ",vipcardno='" + vipcardno
						+ "',contractno='" + contractno + "',linkman='"
						+ linkman + "',linkman_credno='" + linkman_credno
						+ "',linkphone='" + linkphone + "',linkaddress='"
						+ linkaddress + "',mobile='" + mobile + "',email='"
						+ email + "',agent='" + agent + "',agent_credno='"
						+ agent_credno + "',agentphone='" + agentphone
						+ "',adsl_res=" + adsl_res + ",adsl_card='" + adsl_card
						+ "',adsl_dev='" + adsl_dev + "',adsl_ser='" + adsl_ser
						+ "',isrepair='" + isrepair + "',bandwidth="
						+ bandwidth + ",ipaddress='" + ipaddress
						+ "',overipnum=" + overipnum + ",ipmask='" + ipmask
						+ "',gateway='" + gateway + "',macaddress='"
						+ macaddress + "',device_id='" + device_id
						+ "',device_ip='" + device_ip + "',device_shelf="
						+ device_shelf + ",device_frame=" + device_frame
						+ ",device_slot=" + device_slot + ",device_port="
						+ device_port + ",basdevice_id='" + basdevice_id
						+ "',basdevice_ip='" + basdevice_ip
						+ "',basdevice_shelf=" + basdevice_shelf
						+ ",basdevice_frame=" + basdevice_frame
						+ ",basdevice_slot=" + basdevice_slot
						+ ",basdevice_port=" + basdevice_port + ",vlanid='"
						+ vlanid + "',workid='" + workid + "',user_state='"
						+ user_state + "',opendate=" + opendate
						+ ",onlinedate=" + onlinedate + ",pausedate="
						+ pausedate + ",closedate=" + closedate
						+ ",updatetime=" + updatetime + ",staff_id='"
						+ staff_id + "',remark='" + remark + "',phonenumber='"
						+ phonenumber + "',cableid='" + cableid + "',bwlevel="
						+ bwlevel + ",vpiid='" + vpiid + "',vciid=" + vciid
						+ ",adsl_hl='" + adsl_hl + "',userline=" + userline
						+ ",dslamserialno='" + dslamserialno + "',movedate="
						+ movedate + ",dealdate=" + dealdate + ",opmode='"
						+ opmode + "',maxattdnrate=" + maxattdnrate
						+ ",upwidth=" + upwidth + ",oui='" + oui
						+ "',device_serialnumber='" + device_serialnumber
						+ "',serv_type_id=" + service_id + ",max_user_number="
						+ max_user_number + ",wan_type=" + wan_type
						+ " where user_id=" + str_user_id;

				strSQL += "update hgwcust_serv_info set username='" + username + "',passwd='" + passwd
						+ "',vpiid='" + vpiid + "',vciid=" + vciid + ",vlanid='" + vlanid + "'" + ",wan_type=" 
						+ wan_type + ",ipaddress='"+ipaddress+"',ipmask='"+ipmask+"',gateway='"+gateway+"'"
						+ ",adsl_ser='"+adsl_ser+"',opendate="+opendate + ",pausedate="+pausedate
						+ ",closedate=" + closedate + ",updatetime=" + updatetime
						+ " where user_id=" + str_user_id;
				
				// by zhaixf update tab_gw_device
				if (oui != null && device_serialnumber != null
						&& !oui.equals("") && !device_serialnumber.equals("")) {
					strSQL += ";update tab_gw_device set device_status=1, cpe_allocatedstatus=1"
							+ ",city_id='"
							+ city_id
							+ "'"
							+ " where oui='"
							+ oui
							+ "' and device_serialnumber='"
							+ device_serialnumber + "'";
				}

				strSQL = strSQL.replaceAll("=,", "=null,");
				strSQL = strSQL.replaceAll("= where", "=null where");
			}
		}

		if (!strSQL.equals("")) {
			if ("delete".equals(strAction) || "deleteBatch".equals(strAction)) {
				int[] iCodes = DataSetBean.doBatch(strSQL);
				
				//先解绑
//				String resMsg = userInstReleaseBio.itmsRelease(userId, userName, cityId, deviceId, dealstaff, userline);
//				logger.debug(resMsg);
				
				//支持批量处理
				for (String userName : userNames) 
				{
					// 通知调用资源绑定模块来删除用户mem
					String resMsg = userInstReleaseBio.itmsDelUser(userName);
					logger.debug(resMsg);
				}
				
				
				//返回结果
				if (iCodes != null && iCodes.length > 0) {
					strMsg = "家庭网关用户操作成功！";
				} else {
					strMsg = "家庭网关用户操作失败，请返回重试或稍后再试！";
				}

			} else {
				int iCode[] = DataSetBean.doBatch(strSQL);
				if (iCode != null && iCode.length > 0) {
					strMsg = "家庭网关用户操作成功！";
				} else {
					strMsg = "家庭网关用户操作失败，请返回重试或稍后再试！";
				}
			}
			PrepareSQL psql = new PrepareSQL(strSQL);
	    	psql.getSQL();
		}

		return strMsg;
	}

	/**
	 * 备份用户表
	 * @param int_user_id
	 */
	private void backHgwcustomer(int int_user_id)
	{
		// 获取属性列表
		String stringKeys = "gather_id,username,passwd,city_id,cotno,user_type_id,numcharacter,aut_flag,service_set,realname,sex,credno,address,office_id,zone_id,licenceregno,vipcardno,contractno,linkman,linkman_credno,linkphone,linkaddress,mobile,email,agent,agent_credno,agentphone,adsl_card,adsl_dev,adsl_ser,isrepair,ipaddress,ipmask,gateway,macaddress,device_id,device_ip,basdevice_id,basdevice_ip,vlanid,workid,user_state,staff_id,remark,phonenumber,cableid,vpiid,dslamserialno,opmode,oui,device_serialnumber,wan_value_1,wan_value_2,bind_port,network_spec,is_pon,user_sub_name";
		String intKeys = "user_id,bill_type_id,next_bill_type_id,cust_type_id,bindtype,virtualnum,access_style_id,cred_type_id,access_kind_id,trade_id,occupation_id,education_id,adsl_res,bandwidth,overipnum,device_shelf,device_frame,device_slot,device_port,basdevice_shelf,basdevice_frame,basdevice_slot,basdevice_port,opendate,onlinedate,pausedate,closedate,updatetime,bwlevel,vciid,adsl_hl,userline,movedate,dealdate,maxattdnrate,upwidth,serv_type_id,max_user_number,open_status,wan_type,lan_num,ssid_num,work_model,flag_pvc,binddate,stat_bind_enab,bind_flag,is_chk_bind,sip_id,protocol,spec_id,is_active,is_tel_dev";
		stringKeyList = Arrays.asList(stringKeys.split(","));
		intKeyList = Arrays.asList(intKeys.split(","));
		
		String keys = stringKeys + "," + intKeys;
		
		List<String> keyList = new ArrayList<String>();
		keyList.addAll(stringKeyList);
		keyList.addAll(intKeyList);
		
		// 查询数据
		String querySql = "select " + keys + " from tab_hgwcustomer where user_id=" + int_user_id;
		ArrayList list = DataSetBean.executeQuery(querySql, null);
		if(list == null || list.isEmpty()) {
			return;
		}
		// 插入备份表
		StringBuilder insertSqls = new StringBuilder();
		String insertSql = "";
		
		for (int i = 0; i < list.size(); i++)
		{
			Map map = null;
			try
			{
				map = (Map)list.get(i);
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}
			
			if(map == null) {
				continue;
			}
			insertSql = "insert into tab_hgwcustomer_bak ("+ keys +") values (" + values(map, keyList) + ");";
			insertSqls.append(insertSql);
		}
		
		DataSetBean.doBatch(insertSqls.toString());
	}

	/**
	 * 获取value列表
	 * @param map
	 * @param keyList
	 * @return
	 */
	private String values(Map map, List<String> keyList)
	{
		if(map == null || map.isEmpty() || keyList == null || keyList.isEmpty()) {
			return "";
		}
		
		List<String> stringValues = new ArrayList<String>();
		List<Integer> intValues = new ArrayList<Integer>();
		
		String stringValue = "";
		int intValue = 0;
		
		for (int i = 0; i < keyList.size(); i++)
		{
			if(stringKeyList.contains(keyList.get(i))) {
				stringValue = StringUtil.getStringValue(map, keyList.get(i));
				if(stringValue == null) {
					stringValue = "";
				}
				stringValues.add(stringValue);
			}
			else if(intKeyList.contains(keyList.get(i))) {
				intValue = StringUtil.getIntValue(map, keyList.get(i));
				intValues.add(intValue);
			}
		}

		return StringUtils.weave(stringValues) + "," + weave(intValues);
	}
	
	
	/**
	 * list转成str
	 * @param list
	 * @return
	 */
	private String weave(List list)
	{
		StringBuffer sb = new StringBuffer(100);
		if (list.size() != 0) {
			sb.append(list.get(0));

			for (int i = 1; i < list.size(); i++) {
				sb.append(",").append(list.get(i));
			}
		}
		
		return sb.toString();
	}

	public static String insertAreaTable(String device_id, String curAreaId) {
		List list = AreaManageSyb.getAllareaPid(curAreaId);
		int listSize = list.size() - 1;
		String strSql = "";
		while (listSize >= 0) {
			if (null == list.get(listSize) || list.get(listSize).equals("1")
					|| list.get(listSize).equals("0")) {
			} else {
				strSql += "; insert into tab_gw_res_area(res_type,res_id,area_id) values(1,'"
						+ device_id + "'," + list.get(listSize) + ")";
			}
			--listSize;
		}
		PrepareSQL psql = new PrepareSQL(strSql);
    	psql.getSQL();
		return strSql;
	}

	/**
	 * 列表显示设备厂商
	 * 
	 * @param flag
	 *            判断是否关联
	 * @param compare
	 *            比较值
	 * @param rename
	 *            重命名
	 * @return String
	 */
	public String getVendorList(boolean flag, String compare, String rename) {
		PrepareSQL psql = new PrepareSQL(m_Vendor_SQL);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(m_Vendor_SQL);
		String strVendorList = FormUtil.createListBox(cursor, "vendor_id",
				"vendor_name", flag, compare, rename);
		return strVendorList;
	}

	/**
	 * 列表显示设备类型
	 * 
	 * @param flag
	 *            判断是否关联
	 * @param compare
	 *            比较值
	 * @param rename
	 *            重命名
	 * @return String
	 */
	public String getDevModelList(boolean flag, String compare, String rename) {
		PrepareSQL psql = new PrepareSQL(m_DevModel_SQL);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(m_DevModel_SQL);
		String strDevModelList = FormUtil.createListBox(cursor, "device_model",
				"device_model", flag, compare, rename);
		return strDevModelList;
	}

	/**
	 * 列表显示设备版本
	 * 
	 * @param flag
	 *            判断是否关联
	 * @param compare
	 *            比较值
	 * @param rename
	 *            重命名
	 * @return String
	 */
	public String getOsVersionList(boolean flag, String compare, String rename) {
		PrepareSQL psql = new PrepareSQL(m_DevModel_SQL);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(m_DevModel_SQL);
		String strOsVersionList = FormUtil.createListBox(cursor,
				"softwareversion", "softwareversion", flag, compare, rename);
		return strOsVersionList;
	}

	/**
	 * 增加,删除,编辑确认设备信息
	 * 
	 * @param request
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public String ConfirmDevActExe(HttpServletRequest request)
			throws UnsupportedEncodingException {
		String strSQL = "";
		String strMsg = "";

		String strAction = request.getParameter("action");

		if (strAction.equals("delete")) { // 删除操作
			String str_oui = request.getParameter("oui");
			String str_device_serialnumber = new String(request.getParameter(
					"device_serialnumber").getBytes("ISO8859-1"), "GBK");
			strSQL += "delete from tab_confirmdevice where oui='" + str_oui
					+ "' and device_serialnumber='" + str_device_serialnumber
					+ "'";
		} else {
			String oui = request.getParameter("vendor_id");
			String device_serialnumber = request
					.getParameter("device_serialnumber");
			String city_id = request.getParameter("city_id");

			if (strAction.equals("add")) {
				pSQL.setSQL(m_ConfirmDevAdd_SQL);
				pSQL.setString(1, oui);
				pSQL.setString(2, device_serialnumber);
				pSQL.setString(3, city_id);

				strSQL = pSQL.getSQL();

				strSQL = strSQL.replaceAll("',,'", "null");
				strSQL = strSQL.replaceAll(",\\)", ",null)");

			} else {
				String str_oui = request.getParameter("hid_oui");
				String str_device_serialnumber = request
						.getParameter("hid_device_serialnumber");

				strSQL = "update tab_confirmdevice set oui='" + oui
						+ "' ,device_serialnumber= '" + device_serialnumber
						+ "' , city_id='" + city_id + "' where oui='" + str_oui
						+ "' and device_serialnumber='"
						+ str_device_serialnumber + "'";

				strSQL = strSQL.replaceAll("=,", "=null,");
				strSQL = strSQL.replaceAll("= where", "=null where");
			}
		}
		PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
		if (!strSQL.equals("")) {
			int iCode = DataSetBean.executeUpdate(strSQL);
			if (iCode > 0) {
				strMsg = "确认设备操作成功!";
			} else {
				strMsg = "确认设备操作失败!";
			}
		}
		return strMsg;
	}

	/**
	 * 取得所有确认设备的信息
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public ArrayList getConfirmDevCursor(HttpServletRequest request) {
		// 用户检索的情况
		ArrayList list = new ArrayList();
		list.clear();
		String v_name = request.getParameter("v_name");
		String device_serialnumber = request
				.getParameter("device_serialnumber");
		String stroffset = request.getParameter("offset");
		int pagelen = 15;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);

		// HttpSession session = request.getSession();
		// UserRes curUser = (UserRes) session.getAttribute("curUser");

		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			m_ConfirmDev_List_SQL = "select oui, device_serialnumber, city_id from tab_confirmdevice";
		}
		m_ConfirmDev_List_SQL += " where 1 = 1";

		if (request.getParameter("submit") != null
				&& (v_name != null || device_serialnumber != null)) {
			if (v_name != null && v_name.length() != 0) {
				v_name = v_name.trim();
//				m_ConfirmDev_List_SQL += " and oui in (select vendor_id from tab_vendor where vendor_name like '%"
//						+ v_name + "%') ";
				m_ConfirmDev_List_SQL += " and oui in (select b.oui from tab_vendor a,tab_vendor_oui b " +
						" where a.vendor_id=b.vendor_id and a.vendor_name like '%" + v_name + "%') ";
			}
			if (device_serialnumber != null
					&& device_serialnumber.length() != 0) {
				device_serialnumber = device_serialnumber.trim();
				m_ConfirmDev_List_SQL += " and device_serialnumber like '%"
						+ device_serialnumber + "%' ";
			}
		}

		QueryPage qryp = new QueryPage();
		qryp.initPage(m_ConfirmDev_List_SQL, offset, pagelen);
		String strBar = qryp.getPageBar();
		list.add(strBar);
		PrepareSQL psql = new PrepareSQL(m_ConfirmDev_List_SQL);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(m_ConfirmDev_List_SQL, offset, pagelen);
		list.add(cursor);

		return list;
	}

	/**
	 * 取得所有OUI和厂商名对应的MAP
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public HashMap getOUIDevMap() {
		HashMap ouiMap = new HashMap();
		ouiMap.clear();
		PrepareSQL psql = new PrepareSQL(m_Vendor_SQL);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(m_Vendor_SQL);
		Map fields = cursor.getNext();
		if (fields == null) {

		} else {
			while (fields != null) {
				ouiMap.put((String) fields.get("vendor_id"), (String) fields
						.get("vendor_name"));
				fields = cursor.getNext();
			}
		}
		return ouiMap;
	}

	/**
	 * 取得所有OUI和厂商名对应的MAP
	 * 
	 * @param request
	 * @return HashMap
	 */
	public HashMap getCityMap(HttpServletRequest request) {
		HashMap cityMap = new HashMap();
		cityMap.clear();
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String city_id = curUser.getCityId();
		String sql = "select city_id,city_name from tab_city where parent_id='"
				+ city_id + "' or city_id='" + city_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields == null) {

		} else {
			while (fields != null) {
				cityMap.put((String) fields.get("city_id"), (String) fields
						.get("city_name"));
				fields = cursor.getNext();
			}
		}
		return cityMap;
	}

	/**
	 * 导入CSV文件
	 * 
	 * @param
	 * @return HashMap
	 */
	public boolean importCSVFile(String fvalue) {
		String line = "";
		String strSQL = "";
		String strbatchSQL = "";
		// String s1 = new String();

		ArrayList csvLineArr = new ArrayList();
		csvLineArr.clear();
		/*
		 * String s = "/temp/"+fvalue; if(s.lastIndexOf("\\") >= 0) { s1 =
		 * s.substring(0, s.lastIndexOf("\\")); // s2 =
		 * s.substring(s.lastIndexOf("\\") + 1); } if(s.lastIndexOf("/") >= 0) {
		 * s1 = s.substring(0, s.lastIndexOf("/")); // s2 =
		 * s.substring(s.lastIndexOf("/") + 1); } s1 = s1.length() == 0 ? "/" :
		 * s1;
		 * 
		 * logger.debug("GSJ========s1======"+s1);
		 * 
		 * //fvalue.replaceAll("\\","\"\\\""); String path =
		 * m_application.getRealPath(s1);
		 * 
		 * logger.debug("GSJ========path======"+path);
		 */

		String fileName = "";
		fileName = File.filePathName;// new
		// Upload().getPhysicalPath("/temp/"+fvalue,
		// 1);

		// logger.debug("GSJ========path======fileName==="+fileName);

		if (fileName.length() == 0) {
			return false;
		}
		
		BufferedReader br=null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			while ((line = br.readLine()) != null) {
				csvLineArr.add(line);
			}
			br.close();
			br = null;

			if (csvLineArr.size() != 0) {
				for (int i = 0; i < csvLineArr.size(); i++) {
					pSQL.setSQL(m_ConfirmDevAdd_SQL);
					pSQL.setString(1,
							((String) csvLineArr.get(i)).split(",")[0]);
					pSQL.setString(2,
							((String) csvLineArr.get(i)).split(",")[1]);
					pSQL.setString(3,
							((String) csvLineArr.get(i)).split(",")[2]);
					strSQL = pSQL.getSQL();
					if (i != csvLineArr.size() - 1) {
						strSQL = strSQL + ";";
						strbatchSQL = strbatchSQL + strSQL;
					}
				}
				strbatchSQL = strbatchSQL + strSQL;
			}
			PrepareSQL psql = new PrepareSQL(strbatchSQL);
	    	psql.getSQL();
			if (strbatchSQL.length() > 0) {
				int[] iCodes = DataSetBean.doBatch(strbatchSQL);
				if (iCodes != null && iCodes.length > 0) {
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(br!=null){
					br.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * 读cvs文件,生成批量SQL并入库
	 * 
	 * @param name
	 * @return -1:cvs文件中记录为0 -2:获取最大id异常 其他:入库成功数.
	 */
	public int doBatchFromCvs(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		String name = request.getParameter("filename");
		int doBatchRows = 0;
		String dobatchSQL = "";
		String strSQL = "";
		String actionSQL = "";
		String rows = "";
		String[] row;
		String oui = "";
		String device_serialnumber = "";
		String device_model = "";
		String username = "";
		String passwd = "";
		String phonenumber = "";
		String opendate = "";
		String bandwidth = "";
		String upwidth = "";
		String max_user_number = "";
		String vlanid = "";
		String vpiid = "";
		String vciid = "";
		String device_id = "";
		String device_ip = "";
		String device_shelf = "";
		String device_frame = "";
		String device_slot = "";
		String device_port = "";
		String city_id = "";
		String office_id = "";
		String zone_id = "";
		String linkman = "";
		String linkphone = "";
		String email = "";
		String mobile = "";
		String address = "";
		String serviceId = "";
		String userStatus = "";
		int maxID = 0;
		List list = readFileRes(name);
		if (list.size() < 1)
			return -1;
		// 抛弃第一行
		list = list.subList(1, list.size());
		try {
			maxID = this.getMaxUserId();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				rows = (String) iter.next();
				logger.debug("rows---->" + rows);
				row = rows.split(",");
				oui = row[0];
				device_serialnumber = row[1];
				device_model = row[2];
				username = row[3];
				passwd = row[4];
				phonenumber = row[5];
				opendate = row[6];
				bandwidth = row[7];
				upwidth = row[8];
				max_user_number = row[9];
				vlanid = row[10];
				vpiid = row[11];
				vciid = row[12];
				device_id = row[13];
				device_ip = row[14];
				device_shelf = row[15];
				device_frame = row[16];
				device_slot = row[17];
				device_port = row[18];
				city_id = row[19];
				office_id = row[20];
				zone_id = row[21];
				linkman = row[22];
				linkphone = row[23];
				email = row[24];
				mobile = row[25];
				address = row[26];
				serviceId = row[27];
				userStatus = row[28];
				if (this.checkDevice(oui, device_serialnumber) == true) {
					this.ModifyDeviceStatus(oui, device_serialnumber);
				} else if (this.checkUser(username, passwd) == false) {
					// insert
					strSQL = " insert into tab_hgwcustomer(user_id,oui,device_serialnumber,"
							+ "username,passwd,phonenumber,opendate,bandwidth,upwidth,max_user_number,"
							+ "vlanid,vpiid,vciid,device_id,device_ip,device_shelf,device_frame,device_slot,"
							+ "device_port,city_id,office_id,zone_id,linkman,linkphone,email,mobile,address,service_id,user_state) "
							+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					pSQL.setSQL(strSQL);
					pSQL.setInt(1, maxID);
					pSQL.setStringExt(2, oui, true);
					pSQL.setStringExt(3, device_serialnumber, true);
					pSQL.setStringExt(4, username, true);
					pSQL.setStringExt(5, passwd, true);
					pSQL.setStringExt(6, phonenumber, true);
					pSQL.setStringExt(7, isNull(opendate), false);
					pSQL.setStringExt(8, isNull(bandwidth), false);
					pSQL.setStringExt(9, isNull(upwidth), false);
					pSQL.setStringExt(10, isNull(max_user_number), false);
					pSQL.setStringExt(11, vlanid, true);
					pSQL.setStringExt(12, vpiid, true);
					pSQL.setStringExt(13, isNull(vciid), false);
					pSQL.setStringExt(14, device_id, true);
					pSQL.setStringExt(15, device_ip, true);
					pSQL.setStringExt(16, isNull(device_shelf), false);
					pSQL.setStringExt(17, isNull(device_frame), false);
					pSQL.setStringExt(18, isNull(device_slot), false);
					pSQL.setStringExt(19, isNull(device_port), false);
					pSQL.setStringExt(20, city_id, true);
					pSQL.setStringExt(21, office_id, true);
					pSQL.setStringExt(22, zone_id, true);
					pSQL.setStringExt(23, linkman, true);
					pSQL.setStringExt(24, linkphone, true);
					pSQL.setStringExt(25, email, true);
					pSQL.setStringExt(26, mobile, true);
					pSQL.setStringExt(27, address, true);
					pSQL.setStringExt(28, serviceId, false);
					pSQL.setStringExt(29, userStatus, true);

					actionSQL = pSQL.getSQL();
					actionSQL = actionSQL + ";";
					dobatchSQL = dobatchSQL + actionSQL;
					maxID++;
				} else {
					// update
					strSQL = "update tab_hgwcustomer set oui=?,device_serialnumber=?,phonenumber=?,"
							+ "opendate=?,bandwidth=?,upwidth=?,max_user_number=?,vlanid=?,vpiid=?,"
							+ "vciid=?,device_id=?,device_ip=?,device_shelf=?,device_frame=?,device_slot=?,"
							+ "device_port=?,city_id=?,office_id=?,zone_id=?,linkman=?,linkphone=?,email=?,"
							+ "mobile=?,address = ?,user_state = ? where username=? and passwd=?";
					pSQL.setSQL(strSQL);
					pSQL.setStringExt(1, oui, true);
					pSQL.setStringExt(2, device_serialnumber, true);
					pSQL.setStringExt(3, phonenumber, true);
					pSQL.setStringExt(4, isNull(opendate), false);
					pSQL.setStringExt(5, isNull(bandwidth), false);
					pSQL.setStringExt(6, isNull(upwidth), false);
					pSQL.setStringExt(7, isNull(max_user_number), false);
					pSQL.setStringExt(8, vlanid, true);
					pSQL.setStringExt(9, vpiid, true);
					pSQL.setStringExt(10, isNull(vciid), false);
					pSQL.setStringExt(11, device_id, true);
					pSQL.setStringExt(12, device_ip, true);
					pSQL.setStringExt(13, isNull(device_shelf), false);
					pSQL.setStringExt(14, isNull(device_frame), false);
					pSQL.setStringExt(15, isNull(device_slot), false);
					pSQL.setStringExt(16, isNull(device_port), false);
					pSQL.setStringExt(17, city_id, true);
					pSQL.setStringExt(18, office_id, true);
					pSQL.setStringExt(19, zone_id, true);
					pSQL.setStringExt(20, linkman, true);
					pSQL.setStringExt(21, linkphone, true);
					pSQL.setStringExt(22, email, true);
					pSQL.setStringExt(23, mobile, true);
					pSQL.setStringExt(24, address, true);
					pSQL.setStringExt(25, "1", true);
					pSQL.setStringExt(26, username, true);
					pSQL.setStringExt(27, passwd, true);
					actionSQL = pSQL.getSQL();
					actionSQL = actionSQL + ";";
					dobatchSQL = dobatchSQL + actionSQL;
				}
			}
		} catch (Exception ex) {
			logger.warn(ex.getMessage());
		}
		try {
			if (dobatchSQL.length() > 0) {
				logger.debug("Action DoBatchSQL...............");
				PrepareSQL psql = new PrepareSQL(dobatchSQL);
		    	psql.getSQL();
				int[] iCodes = DataSetBean.doBatch(dobatchSQL);
				if (iCodes != null && iCodes.length > 0) {
					doBatchRows = iCodes.length;
				} else {
					doBatchRows = iCodes.length;
				}
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
		return doBatchRows;
	}

	/**
	 * Read File
	 * 
	 * @param fname
	 *            filename
	 * @return return List
	 */
	public List readFileRes(String fname) {
		String tempf = LipossGlobals.getLipossHome() + java.io.File.separator
				+ "temp";
		String path = tempf + java.io.File.separator + fname;
		List list = new ArrayList();
		BufferedReader in=null;
		try {
			in = new BufferedReader(new FileReader(path));
			String line;
			while ((line = in.readLine()) != null) {
				list.add(line);
			}
			
			java.io.File f = new java.io.File(path);
			f.delete();
			f = null;
		} catch (IOException e) {
			logger.warn("处理文件出错" + e.getMessage());
		}finally{
			try {
				if(in!=null){
					in.close();
					in = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}

	public int getMaxUserId() {
		if(DBUtil.GetDB() == 1 || DBUtil.GetDB() == 2) {
			return getMaxUserIdOld();
		}
		return StringUtil.getIntegerValue(DbUtils.getUnusedID("sql_tab_hgwcustomer", 1));
	}
	
	public int getMaxUserIdOld() {
		long serial = -1;
		CallableStatement cstmt = null;
		Connection conn = null;
		String sql = "{call maxHgwUserIdProc(?,?)}";
		try
		{
			conn = DBAdapter.getJDBCConnection();
			cstmt = conn.prepareCall(sql);
			cstmt.setInt(1, 1);
			cstmt.registerOutParameter(2, Types.INTEGER);
			cstmt.execute();
			serial = cstmt.getLong(2);
		}
		catch (Exception e)
		{
			logger.error("GetUnusedDeviceSerial Exception:{}", e.getMessage());
		}
		finally
		{
			sql = null;
			if (cstmt != null)
			{
				try
				{
					cstmt.close();
				}
				catch (SQLException e)
				{
					logger.error("cstmt.close SQLException:{}", e.getMessage());
				}
				cstmt = null;
			}
			if (conn != null)
			{
				try
				{
					conn.close();
				}
				catch (Exception e)
				{
					logger.error("conn.close error:{}", e.getMessage());
				}
				conn = null;
			}
		}
		return (int)serial;
	}

	/**
	 * Check用户资源表是否存在该用户 有返回：true 无返回：false
	 * 
	 * @param oui
	 * @param dSerial
	 * @return
	 */
	public boolean checkUser(String username, String pwd) {
		String checkSQL = "select user_id from tab_hgwcustomer where username='"
				+ username + "'";
		PrepareSQL psql = new PrepareSQL(checkSQL);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(checkSQL);
		Map fields = cursor.getNext();
		if (fields == null) {
			return false;
		}
		return true;
	}

	/**
	 * Check设备资源表是否存在该设备 有返回：true 无返回：false
	 * 
	 * @param oui
	 * @param dSerial
	 * @return
	 */
	public boolean checkDevice(String oui, String dSerial) {
		String checkSQL = "select device_id from tab_gw_device where oui='"
				+ oui + "' and device_serialnumber='" + dSerial + "'";
		try {
			PrepareSQL psql = new PrepareSQL(checkSQL);
	    	psql.getSQL();
			Cursor cursor = DataSetBean.getCursor(checkSQL);
			Map fields = cursor.getNext();
			logger.debug("fields.size()---->" + fields);
			if (fields == null) {
				return false;
			}
		} catch (Exception ex) {
			logger.warn(ex.getMessage());
		}
		return true;
	}

	/**
	 * 根据厂商及序列号获取DEVICE_ID
	 * 
	 * @param oui
	 * @param dSerial
	 * @return
	 */
	public String getDeviceId(String oui, String dSerial) {
		String device_id = "";
		String checkSQL = "select device_id from tab_gw_device where oui='"
				+ oui + "' and device_serialnumber='" + dSerial + "'";
		PrepareSQL psql = new PrepareSQL(checkSQL);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(checkSQL);
		Map fields = cursor.getNext();
		while (fields != null) {
			device_id = (String) fields.get("device_id");
			fields = cursor.getNext();
		}
		return device_id;
	}

	/**
	 * 更新设备的状态
	 * 
	 * @param oui
	 * @param deviceSerialnumber
	 */
	public void ModifyDeviceStatus(String oui, String deviceSerialnumber) {
		String update_Device_SQL = "update tab_gw_device set device_status=1 where oui='"
				+ oui
				+ "' and device_serialnumber='"
				+ deviceSerialnumber
				+ "'";
		try {
			PrepareSQL psql = new PrepareSQL(update_Device_SQL);
	    	psql.getSQL();
			DataSetBean.executeUpdate(update_Device_SQL);
		} catch (Exception ex) {
			logger.warn("更新设备状态失败：" + ex.getMessage());
		}
	}

	// public void importDeviceInfo(String deviceIp, String oui,
	// String deviceSerialnumber, String cityId, String officeId,
	// String zoneId, String deviceModel, String deviceId) {
	// String m_DeviceAdd_SQL = "insert into tab_gw_device
	// (device_id,loopback_ip,"
	// +
	// "oui,device_serialnumber,device_type,manage_staff,city_id,office_id,zone_id,device_addr,"
	// +
	// "complete_time,buy_time,service_year,staff_id,gather_id,device_status,cpe_mac,"
	// +
	// "res_pro_id,device_name,maxenvelopes,retrycount,port,path,cpe_currentupdatetime,"
	// + "acs_username,acs_passwd,cpe_username,cpe_passwd) values
	// (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	// logger.debug("MaxDeviceId" + deviceId);
	// pSQL.setSQL(m_DeviceAdd_SQL);
	// pSQL.setStringExt(1, deviceId, true);
	// pSQL.setStringExt(2, deviceIp, true);
	// pSQL.setStringExt(3, oui, true);
	// pSQL.setStringExt(4, deviceSerialnumber, true);
	// pSQL.setStringExt(5, deviceModel, true);
	// pSQL.setStringExt(6, "admin", true);
	// pSQL.setStringExt(7, cityId, true);
	// pSQL.setStringExt(8, officeId, true);
	// pSQL.setStringExt(9, zoneId, true);
	// pSQL.setStringExt(10, "", true);
	// pSQL.setStringExt(11, "null", false);
	// pSQL.setStringExt(12, "null", false);
	// pSQL.setStringExt(13, "null", false);
	// pSQL.setStringExt(14, "", true);
	// pSQL.setStringExt(15, "", true);
	// pSQL.setStringExt(16, "1", false);
	// pSQL.setStringExt(17, "", true);
	// pSQL.setStringExt(18, "", true);
	// pSQL.setStringExt(19, "", true);
	// pSQL.setStringExt(20, "1", false);
	// pSQL.setStringExt(21, "1", false);
	// pSQL.setStringExt(22, "1", false);
	// pSQL.setStringExt(23, "", true);
	// pSQL.setStringExt(24, "1", false);
	// pSQL.setStringExt(25, "itms", true);
	// pSQL.setStringExt(26, "itms", true);
	// pSQL.setStringExt(27, "hgw", true);
	// pSQL.setStringExt(28, "hgw", true);
	// logger.debug("importDeviceInfo====>" + pSQL.getSQL());
	// DataSetBean.executeUpdate(pSQL.getSQL());
	// }

	/**
	 * 查询是否存在该设备的业务
	 * 
	 * @param oui
	 * @param deviceSerialnumber
	 * @param service_id
	 * @return
	 */
	private int checkDevice(String oui, String device_serialnumber,
			String service_id) {
		int count = 0;

		String sql = "select count(*) as total from tab_hgwcustomer where oui='"
				+ oui
				+ "' and device_serialnumber='"
				+ device_serialnumber
				+ "' and service_id=" + service_id;
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields != null) {
			String tmp = (String) fields.get("total");
			if (tmp != null && !"".equals(tmp)) {
				count = Integer.parseInt(tmp);
			}
		}

		return count;
	}
	
	/**
	 * 获取用户终端类型
	 * @param userId
	 * @return
	 */
	public String getUserDevType(String userId)
	{
		logger.debug("UserInstReleaseDAO=>getUserDevType(userId:{})",
				new Object[] { userId });

		PrepareSQL ppSQL = new PrepareSQL(" select b.type_name from gw_cust_user_dev_type a,gw_dev_type b where a.type_id=b.type_id and a.user_id=? ");
		ppSQL.setLong(1, Long.parseLong(userId));
		Map map = DataSetBean.getRecord(ppSQL.getSQL());
		if(null == map || map.isEmpty() == true)
		{
			return "e8-b";
		}
		if(null == map.get("type_name"))
		{
			return "e8-b";
		}
		return map.get("type_name").toString();
	}


	/**
	 * 转化输入的字符串，若为空则返回字符串“0”，用于处理数字型的参数
	 * 
	 * @param str
	 * @return str为空返回“0”，否则直接返回str
	 */
	private String isNull(String str) {

		if (str != null && !"".equals(str)) {
			return str;
		}
		return "0";
	}

	/**
	 * 获取地市名称和地市编号的对应关系
	 * 
	 * @return
	 */
	private Map<String, String> getCityMap() {
		Map<String, String> map = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL("select city_id,city_name from tab_city");
    	psql.getSQL();
		cursor = DataSetBean
				.getCursor("select city_id,city_name from tab_city");
		Map fields = cursor.getNext();

		while (fields != null) {
			map.put((String) fields.get("city_name"), (String) fields
					.get("city_id"));

			fields = cursor.getNext();
		}

		return map;
	}

	/**
	 * 获取局向名称和局向编号的对应关系
	 * 
	 * @return
	 */
	private Map<String, String> getOfficeMap() {
		Map<String, String> map = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL("select office_id,office_name from tab_office");
    	psql.getSQL();
		cursor = DataSetBean
				.getCursor("select office_id,office_name from tab_office");
		Map fields = cursor.getNext();

		while (fields != null) {
			map.put((String) fields.get("office_name"), (String) fields
					.get("office_id"));

			fields = cursor.getNext();
		}

		return map;
	}

	/**
	 * 获取小区名称和小区编号的对应关系
	 * 
	 * @return
	 */
	private Map<String, String> getZoneMap() {
		Map<String, String> map = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL("select zone_id,zone_name from tab_zone");
    	psql.getSQL();
		cursor = DataSetBean
				.getCursor("select zone_id,zone_name from tab_zone");
		Map fields = cursor.getNext();

		while (fields != null) {
			map.put((String) fields.get("zone_name"), (String) fields
					.get("zone_id"));

			fields = cursor.getNext();
		}

		return map;
	}

	/**
	 * 根据上传附件的内容更新用户
	 * 
	 * @param name
	 *            上传附件的文件名
	 * @return
	 */
	public int doBatchFromCvs(String name) {

		// 记录处理的记录数
		int doBatchRows = 0;

		// 初始化数据
		String dobatchSQL = "";
		String strSQL = "";
		String rows = "";
		String[] row;

		Map<String, String> cityMap = getCityMap();
		Map<String, String> officeMap = getOfficeMap();
		Map<String, String> zoneMap = getZoneMap();

		// oui
		String oui = "";
		// 设备序列号
		String device_serialnumber = "";
		// 宽带帐号
		String username = "";
		// 宽带密码
		String passwd = "";
		// Adsl绑定电话
		String phonenumber = "";
		// 开通时间
		String opendate = "";
		// 最大下行可达速率
		String bandwidth = "";
		// 上行承诺速率
		String upwidth = "";
		// 允许用户上网数
		String max_user_number = "";
		// VlanID号
		String vlanid = "";
		// VpiID号
		String vpiid = "";
		// VciID号
		String vciid = "";
		// 属地编码
		String city_id = "";
		// 局向标识
		String office_id = "";
		// 小区标识
		String zone_id = "";
		// 联系人
		String linkman = "";
		// 联系电话
		String linkphone = "";
		// 用户类型
		String serviceId = "";
		// 用户状态
		String cust_type_id = "";
		// 业务类型
		String userStatus = "";
		int maxID = 0;
		List list = readFileRes(name);
		if (list.size() < 1)
			return -1;
		// 抛弃第一行
		list = list.subList(1, list.size());
		try {
			// 获取当前用户的最大id
			maxID = this.getMaxUserId();

			for (Iterator iter = list.iterator(); iter.hasNext();) {
				// 解析数据
				rows = (String) iter.next();
				logger.debug("rows---->" + rows);
				row = rows.split(",");

				// 给各个参数赋值
				oui = row[0];
				device_serialnumber = row[1];
				username = row[2];
				passwd = row[3];
				phonenumber = row[4];

				// 为空则取当前时间
				if ("".equals(row[5])) {
					opendate = String.valueOf(new DateTimeUtil().getLongTime());
				} else {
					opendate = String.valueOf(new DateTimeUtil(row[5])
							.getLongTime());
				}

				if ("".equals(row[6])) {
					bandwidth = "2048";
				} else {
					bandwidth = row[6];
				}

				if ("".equals(row[7])) {
					upwidth = "512";
				} else {
					upwidth = row[7];
				}

				if ("".equals(row[8])) {
					max_user_number = "10";
				} else {
					max_user_number = row[8];
				}

				vlanid = row[9];
				vpiid = row[10];
				vciid = row[11];

				city_id = cityMap.get(row[12]);
				if (city_id == null) {
					city_id = "";
				}

				office_id = officeMap.get(row[13]);
				if ("".equals(office_id)) {
					office_id = null;
				}
				zone_id = zoneMap.get(row[14]);
				if ("".equals(zone_id)) {
					zone_id = null;
				}
				linkman = row[15];
				linkphone = row[17];
				serviceId = row[16];
				if ("".equals(serviceId)) {
					serviceId = null;
				}
				cust_type_id = row[18];
				if ("".equals(cust_type_id)) {
					cust_type_id = null;
				}
				userStatus = row[19];
				if ("".equals(userStatus)) {
					userStatus = null;
				}
				if (this.checkUser(username, passwd) == false) {
					// 新增记录
					strSQL = " insert into tab_hgwcustomer(user_id,oui,device_serialnumber,"
							+ "username,passwd,phonenumber,opendate,bandwidth,upwidth,max_user_number,"
							+ "vlanid,vpiid,vciid,"
							+ "city_id,office_id,zone_id,linkman,linkphone,cust_type_id,user_state,serv_type_id) "
							+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					pSQL.setSQL(strSQL);
					pSQL.setInt(1, maxID++);
					pSQL.setStringExt(2, oui, true);
					pSQL.setStringExt(3, device_serialnumber, true);
					pSQL.setStringExt(4, username, true);
					pSQL.setStringExt(5, passwd, true);
					pSQL.setStringExt(6, phonenumber, true);
					pSQL.setStringExt(7, isNull(opendate), false);
					pSQL.setStringExt(8, isNull(bandwidth), false);
					pSQL.setStringExt(9, isNull(upwidth), false);
					pSQL.setStringExt(10, isNull(max_user_number), false);
					pSQL.setStringExt(11, vlanid, true);
					pSQL.setStringExt(12, vpiid, true);
					pSQL.setStringExt(13, isNull(vciid), false);
					pSQL.setStringExt(14, city_id, true);
					pSQL.setStringExt(15, office_id, true);
					pSQL.setStringExt(16, zone_id, true);
					pSQL.setStringExt(17, linkman, true);
					pSQL.setStringExt(18, linkphone, true);
					pSQL.setStringExt(19, cust_type_id, false);
					pSQL.setStringExt(20, userStatus, true);
					pSQL.setStringExt(21, serviceId, false);

					dobatchSQL = dobatchSQL + pSQL.getSQL() + ";";
					maxID++;
				} else {
					// 更新记录
					strSQL = "update tab_hgwcustomer set oui=?,device_serialnumber=?,phonenumber=?,"
							+ "opendate=?,bandwidth=?,upwidth=?,max_user_number=?,vlanid=?,vpiid=?,"
							+ "vciid=?,city_id=?,office_id=?,zone_id=?,linkman=?,linkphone=?,"
							+ "cust_type_id=?,user_state = ?,serv_type_id=?,passwd=? where username=?";
					pSQL.setSQL(strSQL);
					pSQL.setStringExt(1, oui, true);
					pSQL.setStringExt(2, device_serialnumber, true);
					pSQL.setStringExt(3, phonenumber, true);
					pSQL.setStringExt(4, isNull(opendate), false);
					pSQL.setStringExt(5, isNull(bandwidth), false);
					pSQL.setStringExt(6, isNull(upwidth), false);
					pSQL.setStringExt(7, isNull(max_user_number), false);
					pSQL.setStringExt(8, vlanid, true);
					pSQL.setStringExt(9, vpiid, true);
					pSQL.setStringExt(10, isNull(vciid), false);
					pSQL.setStringExt(11, city_id, true);
					pSQL.setStringExt(12, office_id, true);
					pSQL.setStringExt(13, zone_id, true);
					pSQL.setStringExt(14, linkman, true);
					pSQL.setStringExt(15, linkphone, true);
					pSQL.setStringExt(16, isNull(cust_type_id), false);
					pSQL.setStringExt(17, userStatus, true);
					pSQL.setStringExt(18, isNull(serviceId), false);
					pSQL.setStringExt(19, passwd, true);
					pSQL.setStringExt(20, username, true);
					

					dobatchSQL = dobatchSQL + pSQL.getSQL() + ";";
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// 执行批量sql
		try {
			if (dobatchSQL.length() > 0) {
				logger.debug("Action DoBatchSQL...............");
				PrepareSQL psql = new PrepareSQL(dobatchSQL);
		    	psql.getSQL();

				// 执行sql
				int[] iCodes = DataSetBean.doBatch(dobatchSQL);

				// 执行失败则赋值为0
				if (iCodes != null && iCodes.length > 0) {
					doBatchRows = iCodes.length;
				} else {
					doBatchRows = 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return doBatchRows;
	}

	public void uploadUserFile(String filename) {
		BufferedReader br=null;
		try {
			FileInputStream fis = new FileInputStream(filename);
			InputStreamReader isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			String lineValue = br.readLine();
			logger.debug("GSJ------------" + lineValue);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(br!=null){
					br.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取属地对应域
	 * 
	 * @param cityId
	 * @return
	 */
	public static String getAreaId(String cityId) {
		PrepareSQL psql = new PrepareSQL("select area_id from tab_city_area where city_id='"
				+ cityId + "'");
    	psql.getSQL();
		Map fields = DataSetBean
				.getRecord("select area_id from tab_city_area where city_id='"
						+ cityId + "'");

		if (fields == null) {
			return null;
		} else {
			return (String) fields.get("area_id");
		}
	}
	
	/**
	 * 
	 * 用户列表,分页
	 * @author zhaixf
	 * @return ArrayList（strBar,cursor）
	 */
	public ArrayList getStateUsers(HttpServletRequest request) {

		String filterStr = "";
		String cityId = request.getParameter("city_id");
		// 用户检索的情况
		ArrayList list = new ArrayList();
		list.clear();
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String stroffset = request.getParameter("offset");
		String gw_type = request.getParameter("gw_type");
		filterStr += "&gw_type=" + gw_type;

		int pagelen = 30;
		int offset;
		if (null == stroffset || "null".equals(stroffset))
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);

		m_HGWUsers_List_SQL += " where (user_state = '1' or user_state = '2')";
		// 判断是否为查询提交，如果是则 加入查询字段条件

		if (startTime != null && !startTime.equals("")) {

			m_HGWUsers_List_SQL += " and opendate >= " + startTime;
			filterStr += "&startTime=" + startTime;

		}
		if (endTime != null && !endTime.equals("")) {

			m_HGWUsers_List_SQL += " and opendate <= " + endTime;
			filterStr += "&endTime=" + endTime;
		}

		if (cityId == null && "".equals(cityId)) {
			cityId = "00";
		}
		List list1 = CityDAO.getAllNextCityIdsByCityPid(cityId);
		String allCityIds = StringUtils.weave(list1);
		list1 = null;
		m_HGWUsers_List_SQL += " and city_id in (" + allCityIds + ")";
		filterStr += "&city_id=" + cityId;
		
		logger.debug("统计设备/用户，用户列表:" + m_HGWUsers_List_SQL);
		QueryPage qryp = new QueryPage();
		qryp.initPage(m_HGWUsers_List_SQL, offset, pagelen);
		String strBar = qryp.getPageBar(filterStr);
		list.add(strBar);
		PrepareSQL psql = new PrepareSQL(m_HGWUsers_List_SQL);
    	psql.getSQL();
		cursor = DataSetBean
				.getCursor(m_HGWUsers_List_SQL, offset, pagelen);
		list.add(cursor);

		return list;

	}
	
	
	public Map<String, String> getBindType()
	{
		String sql = "select * from bind_type";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select bind_type_id, type_name from bind_type";
		}
		Map<String,String> bindTypeMap = new HashMap<String, String>();
		bindTypeMap.clear();
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields == null){
			
		}else{
			while (fields != null)
			{				
				bindTypeMap.put((String) fields.get("bind_type_id"), (String) fields.get("type_name"));				
				fields = cursor.getNext();
			}
		}
		return bindTypeMap;
	}
	
	public Map<String, String> getUserType()
	{
		String sql = "select * from user_type";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select user_type_id, type_name from user_type";
		}
		Map<String,String> userTypeMap = new HashMap<String, String>();
		userTypeMap.clear();
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields == null){
			
		}else{
			while (fields != null)
			{				
				userTypeMap.put((String) fields.get("user_type_id"), (String) fields.get("type_name"));				
				fields = cursor.getNext();
			}
		}
		return userTypeMap;
	}
	
	/**
	 * 得到所选用户当前上网业务的信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Cursor getInternetInfo(String user_id) {
		Cursor cursor = null;
		
		String serInfoSQL = "select parm_type_id,username,passwd,parm_stat from res_user_serv_parm_conn where user_id="+user_id;
		PrepareSQL psql = new PrepareSQL(serInfoSQL);
    	
		cursor = DataSetBean.getCursor(psql.getSQL());
		
		return cursor;
	}
	/**
	 * 得到所选用户当前Voip业务的信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Cursor getVoipInfo(String user_id) {
		Cursor cursor = null;
		
		String serInfoSQL = "select line_id,voip_username,voip_phone from tab_voip_serv_param where user_id="+user_id;
		PrepareSQL psql = new PrepareSQL(serInfoSQL);
    	
		cursor = DataSetBean.getCursor(psql.getSQL());
		
		return cursor;
	}
	
	/**
	 * 查询用户绑定的卡的信息
	 * @return
	 */
	public Cursor getUserCardInfo(String user_id)
	{
		String serInfoSQL = "select * from tab_gw_card a, tab_hgwcustomer b where a.user_id=b.user_id and a.user_id="+user_id;
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			serInfoSQL = "select device_id, card_serialnumber from tab_gw_card a, tab_hgwcustomer b where a.user_id=b.user_id and a.user_id="+user_id;
		}
		PrepareSQL psql = new PrepareSQL(serInfoSQL);
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		return cursor;
	}
	
	/**
	 * 获取用户帐号
	 * @param user_id 用户ID
	 * @return 用户帐号
	 */
	private String getUserName(String user_id) 
	{
		// 查询UserName
		Map userNames = DataSetBean
				.getRecord("select username from tab_hgwcustomer where user_id="
						+ user_id);

//		logger.info("======================user_id=" + user_id);
//		logger.info("======================userNames=" + userNames.toString());
		if (null == userNames) {
			return null;
		} else {
			return (String) userNames.get("username");
		}
	}

	public UserInstReleaseBIO getUserInstReleaseBio() 
	{
		return userInstReleaseBio;
	}

	public void setUserInstReleaseBio(UserInstReleaseBIO userInstReleaseBio) 
	{
		this.userInstReleaseBio = userInstReleaseBio;
	}
	
	/**
	 * 通过用户id，查询该用户的地址方式
	 * @param request 请求
	 * @return 地址方式
	 */
	public String getTabNetServParamByUserId(String user_id) {
		// String user_id = request.getParameter("user_id");
		String  sql = "select ip_type from tab_net_serv_param where user_id = "+ user_id;
						
		PrepareSQL psql = new PrepareSQL(sql);
		Map<String, String> record = DBOperation.getRecord(psql.getSQL());
		String ipType="";
		if(record!=null && !record.isEmpty()){
			String ip = record.get("ip_type");
			
			// 1：ipv4 2：ipv6 3：ipv4+ipv6 6：ipv6+dslite
			
			if("1".equals(ip)){
				ipType = "ipv4";
			}
			else if("2".equals(ip)){
				ipType = "ipv6";
			}
			else if("3".equals(ip)){
				ipType = "ipv4+ipv6";
			}
			else if("6".equals(ip)){
				ipType = "ipv6+dslite";
			}
		}
		logger.warn("查询结果 = {}, ipType = {}", record, ipType);
		return ipType;
	}

}
