package com.linkage.module.ids.dao;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

public class VoiceRegisterDAO extends SuperDAO{

	public int insertVoiceRegister(String oui, String device_serialnumber, String test_time, String test_server,String DiagnosticResult,String DiagnosticReason, String RstCode) {
		PrepareSQL psql = new PrepareSQL("insert into tab_iad_diag_result(oui, device_serialnumber, test_time, test_server,regist_result,reason,status) values(?,?,?,?,?,?,?) ");
		psql.setString(1, StringUtil.getStringValue(oui));
		psql.setString(2, StringUtil.getStringValue(device_serialnumber));
		psql.setLong(3, StringUtil.getLongValue(test_time));
		psql.setInt(4, StringUtil.getIntegerValue(test_server));
		psql.setInt(5, StringUtil.getIntegerValue(DiagnosticResult));
		psql.setInt(6, StringUtil.getIntegerValue(DiagnosticReason));
		psql.setInt(7, StringUtil.getIntegerValue(RstCode));
		return jt.update(psql.toString());
	}
}
