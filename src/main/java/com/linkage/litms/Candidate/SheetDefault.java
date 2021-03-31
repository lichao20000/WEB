package com.linkage.litms.Candidate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;

public class SheetDefault {

	static Logger logger = LoggerFactory.getLogger(SheetDefault.class);
	
	public static String getTempSql(String service_id, String deviceTypeId) {
		// 根据业务和设备型号获取模板，然后根据模板获取命令id
		String strSQL = "select b.tc_serial,c.have_defvalue,c.def_value,c.para_type_id,c.para_serial from tab_servicecode a,tab_template_cmd b,tab_template_cmd_para c  where a.template_id = b.template_id and  a.service_id = "
				+ service_id
				+ " and a.devicetype_id = "
				+ deviceTypeId
				+ " and b.tc_serial = c.tc_serial order by b.rpc_order,c.rpc_order";
//		logger.debug("根据模板获取命令SQL:{}", strSQL);
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return strSQL;
	}

}
