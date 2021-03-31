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

public class DevTypeConfig {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(DevTypeConfig.class);
	private String strSQL1 = "select device_id from ip_stat_check where device_id=?";
	private String strSQL2 = "update ip_stat_check set warnmode=?,warnlevel=? where device_id=?";
	private String strSQL3 = "insert into ip_stat_check(device_id,warnmode,warnlevel,gather_id,intervaltime,isstatis,configtime) values(?,?,?,?,300,0,60)";
	private String[] strSQL_updata = null;
	private String[] strSQL_insert = null;
	private ArrayList listSQL_up = new ArrayList();
	private ArrayList listSQL_insert = new ArrayList();
	boolean ipExistDb = true;
	
	public boolean isValidateID(String device_id) {
		
		boolean b = false;
		PrepareSQL pSQL = null;
		pSQL = new PrepareSQL(strSQL1);
		pSQL.setStringExt(1, device_id, true);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		logger.debug("sql1:" + pSQL.getSQL());
		Map fileds = cursor.getNext();
		
		if (fileds!=null)
			b = true;
		else
			b = false;

		return b;
	}	
	
	public int devTypeConfigList(HttpServletRequest request) {
		int retflag = 0;
		int[] updata_retflag = null;
		int[] insert_retflag = null;
		String str_selectDevIDList = null;
		String str_selDev = new String();
		
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String[] device_idList = request.getParameterValues("select_one");
		String warningmode = request.getParameter("alarm_mode");
		String warninglevel = request.getParameter("alarm_grade");
		String[] dev_gather = null;
		String device_id = null;
		String gather_id = null;
		ArrayList list_gather_id = new ArrayList();
		String[] array_gather_id = null;
		
		
		for (int i=0;i<device_idList.length;i++) {
			str_selectDevIDList = device_idList[i];
			dev_gather = str_selectDevIDList.split(",");
			
			for (int j=0; j<dev_gather.length; j++) {
				str_selDev = dev_gather[j];
				device_id = str_selDev.split("/")[0];
				gather_id = str_selDev.split("/")[1];
				list_gather_id.add(gather_id);
				
				ipExistDb = isValidateID(device_id);

				if (ipExistDb) {
					PrepareSQL pSQL = null;
					pSQL = new PrepareSQL(strSQL2);
					pSQL.setStringExt(1, warningmode, false);
					pSQL.setStringExt(2, warninglevel, false);
					pSQL.setStringExt(3, device_id, true);
					listSQL_up.add(pSQL.getSQL());
					logger.debug("sql2:" + pSQL.getSQL());
				}
				else {
					PrepareSQL pSQL = null;
					pSQL = new PrepareSQL(strSQL3);
					pSQL.setStringExt(1, device_id, true);
					pSQL.setStringExt(2, warningmode, false);
					pSQL.setStringExt(3, warninglevel, false);
					pSQL.setStringExt(4,gather_id,true);
					listSQL_insert.add(pSQL.getSQL());
				}
			}
		}
		strSQL_updata = new String[listSQL_up.size()];
		for (int i=0; i<listSQL_up.size(); i++) {
			strSQL_updata[i] = (String)listSQL_up.get(i);
		}
		
		strSQL_insert = new String[listSQL_insert.size()];
		for(int i=0; i<listSQL_insert.size(); i++) {
			strSQL_insert[i] = (String)listSQL_insert.get(i);
		}
		updata_retflag = DataSetBean.doBatch(strSQL_updata);
		insert_retflag = DataSetBean.doBatch(strSQL_insert);
		
		array_gather_id = new String[list_gather_id.size()];
		for (int i=0; i<list_gather_id.size(); i++) {
			array_gather_id[i] = (String)list_gather_id.get(i);
		} 
		MCControlManager cont_Manager = new MCControlManager(curUser
					.getUser().getAccount(), curUser.getUser().getPasswd());
		retflag = cont_Manager.InformServiceConfUpdate(10,array_gather_id);
		return retflag;
	}
}
