package com.linkage.module.liposs.monitor.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.liposs.monitor.dao.DbtableSpaceInfoDAO;

public class DbtableSpaceInfoBio {
	 /**
     * Dao
     */
    private DbtableSpaceInfoDAO dao = null;
    

    /**
     * apache 日志
     */
    private static final Logger LOG = Logger.getLogger(DbtableSpaceInfoBio.class);


	public List<Map<String, String>> getList() {
		List<Map<String, String>> restlist = new ArrayList<Map<String, String>>();
		long currTime = System.currentTimeMillis();	
		LOG.warn("开始查询,时间:"+currTime);
		List<HashMap<String, String>> list = dao.getAllList();
		long currendTime = System.currentTimeMillis();	
		LOG.warn("结束查询,时间:"+currendTime);
		if(null == list || list.isEmpty()){
			return restlist;
		}
		/**"
		TBS_LOG_ITMS
		TBS_TR069_CONF_ITMS
		TBS_RESOURCE_ITMS
		TBS_INDEX_ITMS
		TBS_SYSTEM_ITMS
		TBS_TR069_GATH_ITMS
		TBS_REPORT_BBMS
		"*/
		for (Map map : list) {
			
			String _name = StringUtil.getStringValue(map,"tablespace_name","");
			if("TBS_LOG_ITMS".equals(_name)){
				restlist.add(map);
			}
			if("TBS_TR069_CONF_ITMS".equals(_name)){
				restlist.add(map);
			}
			if("TBS_RESOURCE_ITMS".equals(_name)){
				restlist.add(map);
			}
			if("TBS_INDEX_ITMS".equals(_name)){
				restlist.add(map);
			}
			if("TBS_SYSTEM_ITMS".equals(_name)){
				restlist.add(map);
			}
			if("TBS_TR069_GATH_ITMS".equals(_name)){
				restlist.add(map);
			}
			if("TBS_REPORT_BBMS".equals(_name)){
				restlist.add(map);
			}
		}
		return restlist;
	}


	public DbtableSpaceInfoDAO getDao() {
		return dao;
	}


	public void setDao(DbtableSpaceInfoDAO dao) {
		this.dao = dao;
	}
	
	
	
}
