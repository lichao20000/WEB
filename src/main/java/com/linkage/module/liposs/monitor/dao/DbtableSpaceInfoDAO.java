package com.linkage.module.liposs.monitor.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.linkage.commons.db.DBOperation;
import com.linkage.module.liposs.system.basesupport.BaseSupportDAO;

public class DbtableSpaceInfoDAO extends BaseSupportDAO{
	 private static final Logger LOG = Logger.getLogger(DbtableSpaceInfoDAO.class);
	    
	    public List<HashMap<String, String>> getAllList()
	    {
	        String sql = "select a.tablespace_name,a.bytes/1024/1024 sumMB,(a.bytes-b.bytes)/1024/1024 usedMB,b.bytes/1024/1024 freeMB,"
	    +" round (((a.bytes-b.bytes)/a.bytes)*100,2) usedPASE from (select tablespace_name,sum(bytes) bytes from dba_data_files group by tablespace_name)a,"
	    +" (select tablespace_name,sum(bytes) bytes,max(bytes) largest from dba_free_space group by tablespace_name) b "
	    + "where a.tablespace_name=b.tablespace_name order by ((a.bytes-b.bytes)/a.bytes) desc";
	        LOG.info(sql);
	        //DBOperation.getRecord(sql, "xml-xw");
	        List<HashMap<String, String>> list =  DBOperation.getRecords(sql, "xml-xwdb");
	        LOG.info(list);
	        return list;
	    }
}
