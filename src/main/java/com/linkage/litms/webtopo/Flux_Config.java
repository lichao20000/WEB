package com.linkage.litms.webtopo;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.system.UserRes;

public class Flux_Config {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(Flux_Config.class);
	private PrepareSQL pSQL = null;

	private HttpServletRequest request;

	private String strSQL1 = "select device_id,loopback_ip,device_name,device_model,snmp_ro_community from tab_gw_device where " +
			"device_id in(select res_id from tab_gw_res_area where area_id=? and res_type=1 " +
			"and res_id in(select distinct device_id from flux_interfacedeviceport)) order by device_name";
	private String strSQL2 = "select device_id,loopback_ip,device_name,device_model,snmp_ro_community from tab_gw_device where loopback_ip = ?";
	private String strSQL3 = "select device_id,ifindex,ifportip,ifname,ifdescr,ifnamedefined,getway,if_real_speed from flux_interfacedeviceport where device_id = ?";
	private String strSQL4 = "select a.ifindex,a.ifdescr,a.ifname,a.ifnamedefined,a.iftype,a.ifspeed,a.ifmtu," +
			"a.ifhighspeed,a.ifinoctetsbps_max,a.ifoutoctetsbps_max,a.ifindiscardspps_max," +
			"a.ifoutdiscardspps_max,a.ifinerrorspps_max,a.ifouterrorspps_max,a.warningnum,a.warninglevel,a.reinstatelevel," +
			"a.overper,a.overnum,a.com_day,a.overlevel,a.reinoverlevel,a.intbflag,a.ifinoctets,a.inoperation,a.inwarninglevel," +
			"a.inreinstatelevel,a.outtbflag,a.ifoutoctets,a.outoperation,a.outwarninglevel,a.outreinstatelevel,b.gatherflag,b.intodb " +
			"from flux_interfacedeviceport a,flux_deviceportconfig b where a.device_id=? and a.getway=? and ? and b.device_id=? and b.getway=? and ?";
	
	private String strSQL5 = "update flux_deviceportconfig set gatherflag=?,intodb=? where device_id=? and getway=? and ?";
	
	private String strSQL6 = "update flux_interfacedeviceport set ifinoctetsbps_max=?,ifoutoctetsbps_max=?,ifindiscardspps_max=?,ifoutdiscardspps_max=?," +
			"ifinerrorspps_max=?,ifouterrorspps_max=?,warningnum=?,warninglevel=?,reinstatelevel=?,overper=?,overnum=?,com_day=?,overlevel=?," +
			"reinoverlevel=?,intbflag=?,ifinoctets=?,inoperation=?,inwarninglevel=?,inreinstatelevel=?,outtbflag=?,ifoutoctets=?," +
			"outoperation=?,outwarninglevel=?,outreinstatelevel=? where device_id=? and getway=? and ?";
	
	private String strSQL7 = "delete from flux_deviceportconfig where device_id=? and getway=? and ?";
	private String strSQL8 = "delete from flux_interfacedeviceport where device_id=? and getway=? and ?";
	private String strSQL9 = "delete from flux_deviceportconfig where device_id=?";
	private String strSQL10 = "delete from flux_interfacedeviceport where device_id = ?";
	private String strSQL11 = "update tab_gw_device set snmp_ro_community=? where device_id in (?)";
	private String strSQL12 = "update flux_deviceportconfig set gatherflag=?, polltime=? where device_id in (?) ";

	public Flux_Config() {
		if (pSQL == null) {
			pSQL = new PrepareSQL();
		}
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public Cursor getDevResource() {		
		pSQL.setSQL(strSQL1);
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long area_id = curUser.getUser().getAreaId();	
		pSQL.setLong(1,area_id);
		return DataSetBean.getCursor(pSQL.getSQL());		
	}
	
	public HashMap getDevResource_IP() {
		String ip = request.getParameter("ip");
		pSQL.setSQL(strSQL2);
		pSQL.setString(1,ip);
		
		return DataSetBean.getRecord(pSQL.getSQL());
	}
	
	public Cursor getPortList(String device_id) {
		pSQL.setSQL(strSQL3);
		pSQL.setString(1,device_id);
		return DataSetBean.getCursor(pSQL.getSQL());
	}
	
	public Cursor getDevPortInfo() {
		String key = request.getParameter("key");
		String[] array_key = key.split(",");
		String device_id = array_key[0];
		String getway = array_key[1];
		String getway_value = array_key[2];	
		int num_getway = Integer.parseInt(getway);
		String condition_1 = null;
		String condition_2 = null;
		
		
		switch (num_getway) {
			case 1: {
				condition_1 = "a.ifindex='" + getway_value + "'";
				condition_2 = "b.ifindex='" + getway_value + "'";
				break;
			}
			case 2: {
				condition_1 = "a.ifdescr='" + getway_value + "'";
				condition_2 = "b.ifdescr='" + getway_value + "'";
				break;
			}
			case 3: {
				condition_1 = "a.ifname='" + getway_value + "'";
				condition_2 = "b.ifname='" + getway_value + "'";
				break;
			}
			case 4: {
				condition_1 = "a.ifnamedefined='" + getway_value + "'";
				condition_2 = "b.ifnamedefined='" + getway_value + "'";
				
				break;
			}
			case 5: {
				condition_1 = "a.ifportip='" + getway_value + "'";
				condition_2 = "b.ifportip='" + getway_value + "'";
				break;
			}
		}
		pSQL.setSQL(strSQL4);
		pSQL.setString(1,device_id);
		pSQL.setStringExt(2,getway,false);
		pSQL.setStringExt(3,condition_1,false);
		pSQL.setString(4,device_id);
		pSQL.setStringExt(5,getway,false);
		pSQL.setStringExt(6,condition_2,false);
		return DataSetBean.getCursor(pSQL.getSQL());
	}
	
	public int action_editPort() {
		int flag[] = null;
		ArrayList list_sql = new ArrayList();
		String key = request.getParameter("key");
		String[] array_key = key.split(",");
		String device_id = array_key[0];
		String getway = array_key[1];
		String getway_value = array_key[2];	
		int num_getway = Integer.parseInt(getway);
		String condition = null;
		
		switch (num_getway) {
			case 1: {
				condition = "ifindex='" + getway_value + "'";
				break;
			}
			case 2: {
				condition = "ifdescr='" + getway_value + "'";
				break;
			}
			case 3: {
				condition = "ifname='" + getway_value + "'";
				break;
			}
			case 4: {
				condition = "ifnamedefined='" + getway_value + "'";
				break;
			}
			case 5: {
				condition = "ifportip='" + getway_value + "'";
				break;
			}
		}
		
		String gatherflag = request.getParameter("gatherflag");
		String intodb =request.getParameter("intodb");
		pSQL.setSQL(strSQL5);
		pSQL.setStringExt(1,gatherflag,false);
		pSQL.setStringExt(2,intodb,false);
		pSQL.setString(3,device_id);
		pSQL.setStringExt(4,getway,false);
		pSQL.setStringExt(5,condition,false);
		list_sql.add(pSQL.getSQL());
		
		String ifinoctetsbps_max = request.getParameter("text_1");
		if (ifinoctetsbps_max == null) {
			ifinoctetsbps_max = "-1";
		}
		
		String ifoutoctetsbps_max = request.getParameter("text_2");
		if (ifoutoctetsbps_max == null) {
			ifoutoctetsbps_max = "-1";
		}
		
		String ifindiscardspps_max = request.getParameter("text_3");
		if (ifindiscardspps_max == null) {
			ifindiscardspps_max = "-1";
		}
		
		String ifoutdiscardspps_max = request.getParameter("text_4");
		if (ifoutdiscardspps_max == null) {
			ifoutdiscardspps_max = "-1";
		}
		
		String ifinerrorspps_max = request.getParameter("text_5");
		if (ifinerrorspps_max == null) {
			ifinerrorspps_max = "-1";
		}
		
		String ifouterrorspps_max = request.getParameter("text_6");
		if (ifouterrorspps_max == null) {
			ifouterrorspps_max = "-1";
		}
		
		String warningnum = request.getParameter("text_7");
		String warninglevel = request.getParameter("select_1");
		String reinstatelevel = request.getParameter("select_2");
		
		String overper = request.getParameter("text_8");
		if (overper == null) {
			overper = "-1";
		}
		
		String overnum = request.getParameter("text_9");
		if (overnum == null) {
			overnum = "-1";
		}
		
		String com_day = request.getParameter("text_10");
		if (com_day == null) {
			com_day = "-1";
		}
		
		String overlevel = request.getParameter("select_3");
		if (overlevel == null) {
			overlevel = "1";
		}
		
		String reinoverlevel = request.getParameter("select_4");
		if (reinoverlevel == null) {
			reinoverlevel = "1";
		}
		
		String intbflag = request.getParameter("checkbox_8");
		String ifinoctets = null;
		String inoperation = null;
		String inwarninglevel = null;
		String inreinstatelevel = null;
		
		if (intbflag == null) {
			intbflag = "0";
			ifinoctets = "-1";
			inoperation = "1";
			inwarninglevel = "1";
			inreinstatelevel = "1";
		}
		else{
			ifinoctets = request.getParameter("text_11");
			inoperation = request.getParameter("select_5");
			inwarninglevel = request.getParameter("select_6");
			inreinstatelevel = request.getParameter("select_7");
		}
		String outtbflag = request.getParameter("checkbox_9");
		String ifoutoctets = null;
		String outoperation = null;
		String outwarninglevel = null;
		String outreinstatelevel = null;
		
		if (outtbflag == null) {
			outtbflag = "0";
			ifoutoctets = "-1";
			outoperation = "1";
			outwarninglevel = "1";
			outreinstatelevel = "1";
		}
		else {
			ifoutoctets = request.getParameter("text_12");
			outoperation = request.getParameter("select_8");
			outwarninglevel = request.getParameter("select_9");
			outreinstatelevel = request.getParameter("select_10");
		}
		
		pSQL.setSQL(strSQL6);
		pSQL.setStringExt(1,ifinoctetsbps_max,false);
		pSQL.setStringExt(2,ifoutoctetsbps_max,false);
		pSQL.setStringExt(3,ifindiscardspps_max,false);
		pSQL.setStringExt(4,ifoutdiscardspps_max,false);
		pSQL.setStringExt(5,ifinerrorspps_max,false);
		pSQL.setStringExt(6,ifouterrorspps_max,false);
		pSQL.setStringExt(7,warningnum,false);
		pSQL.setStringExt(8,warninglevel,false);
		pSQL.setStringExt(9,reinstatelevel,false);
		pSQL.setStringExt(10,overper,false);
		pSQL.setStringExt(11,overnum,false);
		pSQL.setStringExt(12,com_day,false);
		pSQL.setStringExt(13,overlevel,false);
		pSQL.setStringExt(14,reinoverlevel,false);
		pSQL.setStringExt(15,intbflag,false);
		pSQL.setStringExt(16,ifinoctets,false);
		pSQL.setStringExt(17,inoperation,false);
		pSQL.setStringExt(18,inwarninglevel,false);
		pSQL.setStringExt(19,inreinstatelevel,false);
		pSQL.setStringExt(20,outtbflag,false);
		pSQL.setStringExt(21,ifoutoctets,false);
		pSQL.setStringExt(22,outoperation,false);
		pSQL.setStringExt(23,outwarninglevel,false);
		pSQL.setStringExt(24,outreinstatelevel,false);
		pSQL.setString(25,device_id);
		pSQL.setStringExt(26,getway,false);
		pSQL.setStringExt(27,condition,false);
		list_sql.add(pSQL.getSQL());
		
		flag = DataSetBean.doBatch(list_sql);
		
		if (flag[0]<0 || flag[1]<0) {
			return -1;
		}
		else {
			
			return 1;
		}
	}
	
	public int action_delPort() {
		int[] arry_flag = null;
		int flag = 1;
		ArrayList list_sql = new ArrayList();
		String[] array_outkey = request.getParameterValues("select_one");
		String key = null;
		String[] array_inkey = null;
		String device_id = null;
		String getway = null;
		String getway_value = null;
		int num_getway = 0;
		String condition = null;
		
		for (int i=0;i<array_outkey.length;i++) {
			key = array_outkey[i];
			array_inkey = key.split(",");
			device_id = array_inkey[0];
			getway = array_inkey[1];
			getway_value = array_inkey[2];
			num_getway = Integer.parseInt(getway);
			
			switch (num_getway) {
				case 1: {
					condition = "ifindex='" + getway_value + "'";
					break;
				}
				case 2: {
					condition = "ifdescr='" + getway_value + "'";
					break;
				}
				case 3: {
					condition = "ifname='" + getway_value + "'";
					break;
				}
				case 4: {
					condition = "ifnamedefined='" + getway_value + "'";
					break;
				}
				case 5: {
					condition = "ifportip='" + getway_value + "'";
					break;
				}
			}
			
			pSQL.setSQL(strSQL7);
			pSQL.setString(1,device_id);
			pSQL.setStringExt(2,getway,false);
			pSQL.setStringExt(3,condition,false);
			list_sql.add(pSQL.getSQL());
			
			pSQL.setSQL(strSQL8);
			pSQL.setString(1,device_id);
			pSQL.setStringExt(2,getway,false);
			pSQL.setStringExt(3,condition,false);
			list_sql.add(pSQL.getSQL());
		}
		arry_flag = DataSetBean.doBatch(list_sql);
		
		for (int i=0;i<arry_flag.length;i++) {
			if (arry_flag[i] <0) {
				flag = -1;
				break;
			}
		}
		return flag;
	}
	
	public int action_delDev() {
		int[] array_flag = null;
		int flag = 1;
		ArrayList list_sql = new ArrayList();
		String[] array_paramValue = request.getParameterValues("select_one");
		String[] array_value = null;
		String paramValue = null;
		String device_id = null; 
		
		for (int i=0;i<array_paramValue.length;i++){
			paramValue = array_paramValue[i];
			array_value = paramValue.split("/");
			device_id = array_value[0];
			
			pSQL.setSQL(strSQL9);
			pSQL.setString(1,device_id);
			list_sql.add(pSQL.getSQL());
			
			pSQL.setSQL(strSQL10);
			pSQL.setString(1,device_id);
			list_sql.add(pSQL.getSQL());
		}
		array_flag = DataSetBean.doBatch(list_sql);
		
		for (int i=0;i<array_flag.length;i++) {
			if (array_flag[i] < 0) {
				flag = -1;
				break;
			}
		}
		
		return flag;
	}
	
	public int action_editDev() {
		int value = 1;
		String device_id = request.getParameter("device_id");
		String flag = request.getParameter("flag");
		logger.debug("flag===" + flag);
		String gatherflag = request.getParameter("gatherflag");
		String polltime = request.getParameter("polltime");
		ArrayList list_SQL = new ArrayList();
		String[] editDev_SQL = null;
		
		if (flag.equals("1")) {
			String snmp_ro_community = request.getParameter("text_command");
			pSQL.setSQL(strSQL11);
			pSQL.setString(1,snmp_ro_community);
			pSQL.setStringExt(2,device_id,false);
			list_SQL.add(pSQL.getSQL());
		}
		
		pSQL.setSQL(strSQL12);
		pSQL.setStringExt(1,gatherflag,false);
		pSQL.setStringExt(2,polltime,false);
		pSQL.setStringExt(3,device_id,false);
		list_SQL.add(pSQL.getSQL());
		
		editDev_SQL = new String[list_SQL.size()];
		
		for (int i=0; i<list_SQL.size(); i++) {
			editDev_SQL[i] = (String)list_SQL.get(i);
		}
		
		int[] x = DataSetBean.doBatch(editDev_SQL);
		
		for (int i=0; i<x.length; i++) {
			
			if (x[i] <= 0) {
				value = 0;
				break;
			}
		}
		
		return value;
	}
}
