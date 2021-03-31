package com.linkage.litms.webtopo.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.webtopo.MCControlManager;

public class Pm_DevList {
	private PrepareSQL pSQL = new PrepareSQL();
	private HttpServletRequest request=null;
	
	private String strSQL1 = "select vendor_name,vendor_id from tab_vendor where vendor_id in (?)";
	private String strSQL2 = "select distinct company from pm_expression where expressionid in (select distinct expressionid from pm_map)";
	private String strSQL3 = "select distinct expressionid,name from pm_expression where company=? and expressionid in (select distinct expressionid from pm_map)";
	private String strSQL4 = "select distinct a.device_id,device_name,loopback_ip,isok,a.remark," + com.linkage.litms.common.util.DbUtil.getNullFunction("collect", "0") + " as collect " +
			"from pm_map a left outer join pm_map_instance b on a.device_id=b.device_id and a.expressionid=b.expressionid " +
			"left outer join tab_gw_device c on a.device_id=c.device_id where a.device_id in(select res_id from tab_gw_res_area where area_id=? and res_type=1) " +
			"and a.expressionid = ? order by device_name";
	private String strSQL6 = "delete from pm_map where device_id=? and expressionid=?";
	private String strSQL7 = "delete from pm_map_instance where device_id=? and expressionid=?";
	
	private String companyID = null;
	//private String expressionid = null;
	private String[] checkboxValue = null;
	private String devID = null;
	private String expressionID = null;
	private String str_checkboxValue = null;
	
	public Pm_DevList(HttpServletRequest request) {
		this.request = request;
		companyID = request.getParameter("companyID");
		expressionID = request.getParameter("expressionid");
		checkboxValue = request.getParameterValues("select_one");
		
	}
	
	public Cursor getCompanyName(String vendor_id) {
		Cursor cursor = null;
		pSQL.setSQL(strSQL1);
		pSQL.setStringExt(1,vendor_id,false);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		return cursor;
	}
	
	public Cursor getCompanyID() {
		Cursor cursor = null;
		pSQL.setSQL(strSQL2);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		return cursor;
	}
	
	public Cursor getExpressionInfo() {
		Cursor cursor = null;
		pSQL.setSQL(strSQL3);
		pSQL.setStringExt(1,companyID,false);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		return cursor;
	}
	
	public Cursor getPm_mapinfo() {
		Cursor cursor = null;
		pSQL.setSQL(strSQL4);
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long area_id = curUser.getUser().getAreaId();
		pSQL.setLong(1,area_id);
		pSQL.setStringExt(2,expressionID,false);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map result = cursor.getNext();
		String device_id = null;
		String device_name = null;
		String loopback_ip = null;
		String isok = null;
		String remark = null;
		String collect = null;
		
		String temp_device_id = null;
		String temp_device_name = null;
		String temp_loopback_ip = null;
		String temp_isok = null;
		String temp_remark = null;
		String temp_collect = null;
		String flag = "0";
		
		HashMap map_new = null;
		Cursor cursor_new = new Cursor();
		
		int i = 0;
		
		while (result != null) {
			i++;
			device_id = (String)result.get("device_id");
			collect = (String)result.get("collect");
			device_name = (String)result.get("device_name");
			loopback_ip = (String)result.get("loopback_ip");
			isok = (String)result.get("isok");
			remark = (String)result.get("remark");
			
			if (device_id.equals(temp_device_id)) {
				
				if (flag.equals("1")) {
					flag = "1";
				}
				else {
					flag = collect;
				}
			}
			else {
				flag = collect;
			}
			
			if (i > 1) {
				
				if (!device_id.equals(temp_device_id)) {
					map_new = new HashMap();
					map_new.put("device_id",temp_device_id);
					map_new.put("device_name",temp_device_name);
					map_new.put("loopback_ip",temp_loopback_ip);
					map_new.put("isok",temp_isok);
					map_new.put("remark",temp_remark);
					map_new.put("collect",temp_collect);
					cursor_new.add(map_new);
				}
			}
			temp_device_id = device_id;
			temp_device_name = device_name;
			temp_loopback_ip = loopback_ip;
			temp_isok = isok;
			temp_remark = remark;
			temp_collect = flag;
			
			result = cursor.getNext();
		}
		
		if (i > 0) {
			map_new = new HashMap();
			map_new.put("device_id",temp_device_id);
			map_new.put("device_name",temp_device_name);
			map_new.put("loopback_ip",temp_loopback_ip);
			map_new.put("isok",temp_isok);
			map_new.put("remark",temp_remark);
			map_new.put("collect",temp_collect);
			cursor_new.add(map_new);
		}
		return cursor_new;
	}
	
	public int CollectStart() {
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		MCControlManager mccm = new MCControlManager(curUser.getUser().getAccount(), curUser.getUser().getPasswd());
		int flag = 0;
		String devID = null;
		String expressionID = null;
		String str_checkboxValue = null;
		String sqlRebound = "update pm_map_instance set collect=0 where device_id=? and expressionid=?";
		
		for (int i=0;i<checkboxValue.length;i++) {
			str_checkboxValue = checkboxValue[i];
			String[] dev_expInfo = str_checkboxValue.split(",");
			devID = dev_expInfo[0];
			expressionID = dev_expInfo[1];

			if (!mccm.SendToPmee(devID,expressionID,true)) {
				flag = -1;
				pSQL.setSQL(sqlRebound);
				pSQL.setStringExt(1,devID,true);
				pSQL.setStringExt(2,expressionID,false);
				pSQL.getSQL();
				break;
			}
			else {
				flag = 1;
			}
		}
		return flag;
	}
	
	public int CollectStop() {
		int flag = 0;
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		MCControlManager mccm = new MCControlManager(curUser.getUser().getAccount(), curUser.getUser().getPasswd());
		String sqlRebound = "update pm_map_instance set collect=1 where device_id=? and expressionid=?";
		
		for (int i=0; i<checkboxValue.length;i++) {
			str_checkboxValue = checkboxValue[i];
			String[] dev_expInfo = str_checkboxValue.split(",");
			devID = dev_expInfo[0];
			expressionID = dev_expInfo[1];
			
			if (!mccm.SendToPmee(devID,expressionID,false)) {
				flag = -2;
				pSQL.setSQL(sqlRebound);
				pSQL.setStringExt(1,devID,true);
				pSQL.setStringExt(2,expressionID,false);
				pSQL.getSQL();
				break;
			}
			else {
				flag = 2;
			}
		}
		return flag;
	}
	
	public int delSelect() {
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		MCControlManager mccm = new MCControlManager(curUser.getUser().getAccount(), curUser.getUser().getPasswd());
		int flag =0;
		String[] sqlDel = new String[checkboxValue.length*2];
		String[] ids = new String[checkboxValue.length];
		int j = 0;
		
		for (int i=0;i<checkboxValue.length;i++) {
			str_checkboxValue = checkboxValue[i];
			String[] dev_expInfo = str_checkboxValue.split(",");
			devID = dev_expInfo[0];
			expressionID = dev_expInfo[1];
			pSQL.setSQL(strSQL6);
			pSQL.setStringExt(1,devID,true);
			pSQL.setStringExt(2,expressionID,false);
			sqlDel[j] = pSQL.getSQL();
			j++;
			
			pSQL.setSQL(strSQL7);
			pSQL.setStringExt(1,devID,true);
			pSQL.setStringExt(2,expressionID,false);
			sqlDel[j] = pSQL.getSQL();
			j++;
			
			ids[i] = devID;
		}
		int[] flagter = DataSetBean.doBatch(sqlDel);
		
		if (flagter.length > 0) {
			
			if (!mccm.delSelectDev(ids)) {
				flag = -4;
			}
			else {
				flag = 3;
			}
		}
		else {
			flag = -3;
		}
		return flag;
	}
	
	public int save_Instance(HttpServletRequest request) {
		int flag = 0;
		String device_id = request.getParameter("device_id");
		String expressionid = request.getParameter("expressionid");
		String indexid = request.getParameter("indexid");
		String collect = request.getParameter("collect");
		String intodb = request.getParameter("ruku");
		String mintype = request.getParameter("compSign_1");
		String minthres = request.getParameter("fixedness_value1");
		String mindesc = request.getParameter("fixedness_value1desc");
		String mincount = request.getParameter("seriesOverstep_value1");
		String minwarninglevel = request.getParameter("send_warn1");
		String minreinstatelevel = request.getParameter("renew_warn1");
		String maxtype = request.getParameter("compSign_2");
		String maxthres = request.getParameter("fixedness_value2");
		String maxdesc = request.getParameter("fixedness_value2desc");
		String maxcount = request.getParameter("seriesOverstep_value2");
		String maxwarninglevel = request.getParameter("send_warn2");
		String maxreinstatelevel = request.getParameter("renew_warn2");
		String dynatype = request.getParameter("dynamic_OperateSign");
		String beforeday = request.getParameter("benchmark_Value");
		String dynathres = request.getParameter("valve_Percent");
		String dynacount = request.getParameter("achieve_Percent2");
		String dynadesc = request.getParameter("dynamic_Valve_desc");
		String dynawarninglevel = request.getParameter("sdynamic_send_warn");
		String dynareinstatelevel = request.getParameter("sdynamic_renew_warn");
		String mutationtype = request.getParameter("sdynamic_renew_warn");
		String mutationthres = request.getParameter("overstep_Percent");
		String mutationcount = request.getParameter("achieve_Percent3");
		String mutationdesc = request.getParameter("mutation_Valve_desc");
		String mutationwarninglevel = request.getParameter("send_warn3");
		
		String saveInstanceSQL = "update pm_map_instance set collect=?,intodb=?,mintype=?,mindesc=?,minthres=?,mincount=?,minwarninglevel=?,minreinstatelevel=?," +
				"maxtype=?,maxdesc=?,maxcount=?,maxthres=?,maxwarninglevel=?,maxreinstatelevel=?," +
				"dynatype=?,beforeday=?,dynathres=?,dynacount=?,dynadesc=?,dynawarninglevel=?,dynareinstatelevel=?," +
				"mutationtype=?,mutationthres=?,mutationcount=?,mutationdesc=?,mutationwarninglevel=? where device_id=? and expressionid=? and indexid =?";
		
		pSQL.setSQL(saveInstanceSQL);
		pSQL.setStringExt(1,collect,false);
		pSQL.setStringExt(2,intodb,false);
		pSQL.setStringExt(3,mintype,false);
		pSQL.setStringExt(4,mindesc,true);
		pSQL.setStringExt(5,minthres,false);
		pSQL.setStringExt(6,mincount,false);
		pSQL.setStringExt(7,minwarninglevel,false);
		pSQL.setStringExt(8,minreinstatelevel,false);
		pSQL.setStringExt(9,maxtype,false);
		pSQL.setStringExt(10,maxdesc,true);
		pSQL.setStringExt(11,maxcount,false);
		pSQL.setStringExt(12,maxthres,false);
		pSQL.setStringExt(13,maxwarninglevel,false);
		pSQL.setStringExt(14,maxreinstatelevel,false);
		pSQL.setStringExt(15,dynatype,false);
		pSQL.setStringExt(16,beforeday,false);
		pSQL.setStringExt(17,dynathres,false);
		pSQL.setStringExt(18,dynacount,false);
		pSQL.setStringExt(19,dynadesc,true);
		pSQL.setStringExt(20,dynawarninglevel,false);
		pSQL.setStringExt(21,dynareinstatelevel,false);
		pSQL.setStringExt(22,mutationtype,false);
		pSQL.setStringExt(23,mutationthres,false);
		pSQL.setStringExt(24,mutationcount,false);
		pSQL.setStringExt(25,mutationdesc,true);
		pSQL.setStringExt(26,mutationwarninglevel,false);
		pSQL.setStringExt(27,device_id,true);
		pSQL.setStringExt(28,expressionid,false);
		pSQL.setStringExt(29,indexid,true);
		
		flag = DataSetBean.executeUpdate(pSQL.getSQL());
		
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		MCControlManager mccm = new MCControlManager(curUser.getUser().getAccount(), curUser.getUser().getPasswd());
		
		if (flag > 0) {
			String[] dbs = new String[1];
			dbs[0] = device_id;
			
			if (!mccm.editInstance(dbs)) {
				flag = -1;
			}
			else {
				flag = 1;
			}
		}
		else {
			flag = 0;
		}
		return flag;
	}
	
}
