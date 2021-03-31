package com.linkage.module.gtms.diagnostic.serv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.diagnostic.dao.TemplateUnitManageDao;

public class TemplateUnitManageServImpl implements TemplateUnitManageServ {
	
		private static Logger logger = LoggerFactory
		   .getLogger(TemplateUnitManageServImpl.class);
		
		private TemplateUnitManageDao dao;
		
	    public List<Map<String,Object>> getAllRecords(){
	    	logger.debug("getAllRecords()");
	    	//转换时间
	    	long updateTime = 0l;
	    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> tmpList = dao.getAllRecords();
			Map<String,Object> map = null ;
			Map<String,Object> tepmap = null ;
			if(null != tmpList && tmpList.size()>0){
				
				for(int i = 0 ; i <tmpList.size(); i++){
					map = new HashMap<String,Object>();
					tepmap = tmpList.get(i);
					map.put("id", tepmap.get("id"));
					map.put("unit_url", tepmap.get("unit_url"));
					map.put("unit_name", tepmap.get("unit_name"));
					try{
						updateTime = StringUtil.getLongValue(tepmap.get("unit_time"));
						DateTimeUtil dt = new DateTimeUtil(updateTime * 1000);
						map.put("unit_time", dt.getLongDate());
				    }catch (NumberFormatException e){
				    	map.put("unit_time", "");
				    }
				    list.add(map);
			    }
			}
			return list;
		}

		public int add(String templateUnitName, String templateUnitURL){
			logger.debug("add({},{},)",templateUnitName,templateUnitURL);
			return dao.add(templateUnitName,templateUnitURL);
		}

		public int update(String unitId, String templateUnitName,
				String templateUnitURL){
			logger.debug("update({},{},{})",new Object[]{unitId,templateUnitName,templateUnitURL});
			int isEst = getRecordById(unitId);
			if(isEst > 0){
				return dao.update(unitId,templateUnitName,templateUnitURL);
			}else {
				return -1;
			}
		}

		public int delete(String unitId){
			logger.debug("delete({})",unitId);
			int isEst = getRecordById(unitId);
			if(isEst > 0){
				return dao.delete(unitId);
			}else {
				return -1;
			}
		}
		
		public int getRecordById(String unitId){
			logger.debug("getRecordById({})",unitId);
			return dao.getRecordById(StringUtil.getIntegerValue(unitId));
		}
		public Map<String, Object> getpreUpdateRecord(String unitId) {
			return dao.getpreUpdateRecord(unitId);
		}
		public TemplateUnitManageDao getDao() {
			return dao;
		}

		public void setDao(TemplateUnitManageDao dao) {
			this.dao = dao;
		}

		
}
