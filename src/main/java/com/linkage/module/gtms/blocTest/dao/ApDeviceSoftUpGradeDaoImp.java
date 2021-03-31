package com.linkage.module.gtms.blocTest.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;


public class ApDeviceSoftUpGradeDaoImp extends SuperDAO implements ApDeviceSoftUpGradeDao {
	
	private static Logger logger = LoggerFactory.getLogger(ApDeviceSoftUpGradeDaoImp.class);
	
	public List<Map> getSelectListBox(){
		
		logger.debug("ApDeviceSoftUpGradeDaoImp==>getSelectListBox()");
		
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.dir_id, a.outter_url, a.server_dir, b.software_version, b.hardware_version, b.softwarefile_name ");
		psql.append("  from tab_file_server a, tab_ap_software_file b ");
		psql.append(" where 1=1 and a.dir_id = b.dir_id ");
		
		List<Map> list = jt.queryForList(psql.getSQL());
		
		return list;
	}
}
