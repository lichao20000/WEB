package com.linkage.litms.webtopo;


import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.system.UserRes;


public class IpCheck {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(IpCheck.class);
	private String strSQL1 = "select device_id,warnmode,warnlevel from ip_stat_check where device_id=?";

	private String strSQL2 = "update ip_stat_check set warnmode=?,warnlevel=? where device_id=?";

	private String strSQL3 = "insert into ip_stat_check(device_id,warnmode,warnlevel,gather_id,intervaltime,isstatis,configtime) values(?,?,?,?,300,0,60)";
	
	private boolean ipExistDb = true;

	public boolean isValidateID(String device_id) {
		boolean b = false;
			
		PrepareSQL pSQL = null;
		pSQL = new PrepareSQL(strSQL1);
		pSQL.setStringExt(1, device_id, true);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		
		
		if (cursor.getRecordSize() != 0)
			b = true;
		else
			b = false;

		return b;
	}
	
	public Cursor getDevConfigInfo(String device_id) {
		Cursor cursor = null;
		PrepareSQL pSQL = null;
		pSQL = new PrepareSQL(strSQL1);
		pSQL.setStringExt(1, device_id, true);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		
		return cursor;
	}
	
	public int ipConfig(HttpServletRequest request) {
		int retflag = 0;
		PrepareSQL pSQL = null;
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String device_id = request.getParameter("device_id");
		String strSQL4 = "select gather_id from tab_gw_device where device_id = ?";
		
		pSQL = new PrepareSQL(strSQL4);
		pSQL.setStringExt(1,device_id,true);
		Map map_gather = DataSetBean.getRecord(pSQL.getSQL());
		String gather_id = (String)map_gather.get("gather_id");
		String[] array_gather = {gather_id};
		String warningmode = request.getParameter("alarm_mode");
		String warninglevel = request.getParameter("alarm_grade");
		
		boolean b = isValidateID(device_id);
		
		if (!b)
			ipExistDb = false;
		else 
			ipExistDb = true;
		
		if (ipExistDb) {
			
			pSQL = new PrepareSQL(strSQL2);
			pSQL.setStringExt(1, warningmode, false);
			pSQL.setStringExt(2, warninglevel, false);
			pSQL.setStringExt(3, device_id, true);
			retflag = DataSetBean.executeUpdate(pSQL.getSQL());
			
		} else {

			pSQL = new PrepareSQL(strSQL3);
			pSQL.setStringExt(1, device_id, true);
			pSQL.setStringExt(2, warningmode, false);
			pSQL.setStringExt(3, warninglevel, false);
			pSQL.setStringExt(4,gather_id,true);
			retflag = DataSetBean.executeUpdate(pSQL.getSQL());
			
		}

		if (retflag >0) {
			MCControlManager cont_Manager = new MCControlManager(curUser
					.getUser().getAccount(), curUser.getUser().getPasswd());
			retflag = cont_Manager.InformServiceConfUpdate(10,array_gather);

			if (retflag == -1) {

				retflag = 2;
			}
		}
		return retflag;
	}
	
	public int ipConfigList(HttpServletRequest request) {
		int retflag = 0;
		String str_selectDevID = new String();
		String strSQL5 = null;
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String[] dev_gather = request.getParameterValues("select_one");
		String warningmode = request.getParameter("alarm_mode");
		String warninglevel = request.getParameter("alarm_grade");
		
		String device_id = null;
		String gather_id = null;
		ArrayList list_gather = new ArrayList();
		String[] array_gaher = null;
		
		for (int i=0;i<dev_gather.length;i++) {
			device_id = dev_gather[i].split("/")[0];
			gather_id = dev_gather[i].split("/")[1];
			list_gather.add(gather_id);
			str_selectDevID = str_selectDevID + "'" +  device_id + "',";
		}
		
		str_selectDevID = str_selectDevID.substring(0,str_selectDevID.length()-1);
		strSQL5 = "update ip_stat_check set warnmode=?,warnlevel=? where device_id in(" + str_selectDevID + ")";
		
		PrepareSQL pSQL = null;
		pSQL = new PrepareSQL(strSQL5);
		pSQL.setStringExt(1, warningmode, false);
		pSQL.setStringExt(2, warninglevel, false);
		retflag = DataSetBean.executeUpdate(pSQL.getSQL());
		logger.debug("(" + retflag + "   rows affected)");
		
		array_gaher = new String[list_gather.size()];
		
		for (int i=0; i<list_gather.size(); i++) {
			array_gaher[i] = (String)list_gather.get(i);
		}
		
		if (retflag != 0) {
			MCControlManager cont_Manager = new MCControlManager(curUser
					.getUser().getAccount(), curUser.getUser().getPasswd());
			retflag = cont_Manager.InformServiceConfUpdate(10,array_gaher);

			if (retflag == -1) {

				retflag = 2;
			}
		}
		return retflag;
	}
	
	public int ipConfigDel(HttpServletRequest request) {
		int retflag = 0;
		String str_selectDevID = new String();
		String strSQL6 = null;
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String[] dev_gather = request.getParameterValues("select_one");
		String device_id = null;
		String gather_id = null;
		ArrayList list_gather = new ArrayList();
		String[] array_gather = null;
		
		for (int i=0;i<dev_gather.length;i++) {
			device_id = dev_gather[i].split("/")[0];
			gather_id = dev_gather[i].split("/")[1];
			str_selectDevID = str_selectDevID + "'" +  device_id + "',";
			list_gather.add(gather_id);
		}
		
		str_selectDevID = str_selectDevID.substring(0,str_selectDevID.length()-1);
		strSQL6 = "DELETE FROM ip_stat_check WHERE device_id in (" + str_selectDevID + ")";
		PrepareSQL pSQL = null;
		pSQL = new PrepareSQL(strSQL6);
		retflag = DataSetBean.executeUpdate(pSQL.getSQL());
		logger.debug("(" + retflag + "   rows affected)");
		
		dev_gather = new String[list_gather.size()];
		
		for (int i=0; i<list_gather.size(); i++) {
			
			array_gather[i] = (String)list_gather.get(i);
		}
		
		if (retflag >0) {
			MCControlManager cont_Manager = new MCControlManager(curUser
					.getUser().getAccount(), curUser.getUser().getPasswd());
			retflag = cont_Manager.InformServiceConfUpdate(10,array_gather);

			if (retflag == 1) {

				retflag = 3;
			}
			else {
				retflag = 4;
			}
		}
		return retflag;
	}
	
	public Cursor getIPCheckList(HttpServletRequest request) {
		Cursor cursor = null;	
		String strSQL4 = "select a.device_id,device_name,loopback_ip,a.gather_id from ip_stat_check a,tab_gw_device b " +
				"where a.device_id in(select res_id from tab_gw_res_area where area_id=? and res_type=1 and res_id in(select distinct device_id from flux_interfacedeviceport)) " +
				"and a.device_id = b.device_id order by device_name";
		PrepareSQL pSQL = null;
		pSQL = new PrepareSQL(strSQL4);
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long area_id = curUser.getUser().getAreaId();	
		pSQL.setLong(1,area_id);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		
		return cursor;
	}
}
