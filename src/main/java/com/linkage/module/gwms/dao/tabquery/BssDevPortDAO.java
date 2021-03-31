package com.linkage.module.gwms.dao.tabquery;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;


/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-5-31
 * @category com.linkage.module.gwms.dao.tabquery
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class BssDevPortDAO
{	
	//日志记录
		private static Logger logger = LoggerFactory.getLogger(BssDevPortDAO.class);
		
		
		@SuppressWarnings("unchecked")
		public Map<String, String> getBssDevPortNameMapCore(){
			
			logger.debug("getBssDevPortNameMapCore()");
			PrepareSQL psql = new PrepareSQL("select id, spec_name from tab_bss_dev_port order by id");
			Map map = DataSetBean.getMap(psql.getSQL());
			Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
			resultMap.putAll(map); 
			return resultMap;
		}
		
		public static Map<String,String> getBssDevPortNameMap(){
			
			logger.debug("getBssDevPortNameMap()");
			
			return Global.G_BssDev_PortName_Map;
		}
		
		
}
