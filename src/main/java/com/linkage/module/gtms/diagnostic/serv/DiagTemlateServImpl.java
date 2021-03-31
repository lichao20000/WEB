package com.linkage.module.gtms.diagnostic.serv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.diagnostic.dao.DiagTemlateDao;

public class DiagTemlateServImpl implements DiagTemlateServ {
	private static Logger logger = LoggerFactory
	   .getLogger(DiagTemlateServImpl.class);

	private DiagTemlateDao dao ;
	
	public int add(String accOid, String templateName, String templateParam) {
		logger.debug("add({},{},{})",new Object[]{accOid,templateName,templateParam});
		return dao.add(accOid,templateName,templateParam);
	}

	public int delete(String diagId) {
		logger.debug("add({})",diagId);
		int isExst = -1;
		isExst = dao.getDiaTemplateById(diagId);
		if(isExst > 0){
			return dao.delete(diagId);
		}
		return -1;
	}

	public List<Map<String, Object>> getRecords() {
		logger.debug("getRecords()");
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
				map.put("template_name", tepmap.get("template_name"));
				map.put("template_param", tepmap.get("template_param"));
				map.put("acc_loginname", tepmap.get("acc_loginname"));
				try{
					updateTime = StringUtil.getLongValue(tepmap.get("template_time"));
					DateTimeUtil dt = new DateTimeUtil(updateTime * 1000);
					map.put("template_time", dt.getLongDate());
			    }catch (NumberFormatException e){
			    	map.put("template_time", "");
			    }
			    list.add(map);
		    }
		}
		return list;
	}

	public Map<String, Object> getDiagTemplate(String diagId) {
		return dao.getDiagTemplate(diagId);
	}

	public List<Map<String, Object>> getTemplateUnits() {
		return dao.getTemplateUnits();
	}

	public int update(String diagId, String templateName, String templateParam) {
		int isEst = -1;
		isEst = dao.getDiaTemplateById(diagId);
		if(isEst>0){
			return dao.update(diagId,templateName,templateParam);
		}else{
			return -1;
		}
	}

	public DiagTemlateDao getDao() {
		return dao;
	}

	public void setDao(DiagTemlateDao dao) {
		this.dao = dao;
	}

	/**
	 * 根据模板id查询模板单元的url
	 */
	public String getUintListByTempId(String diagId)
	{
		StringBuffer result = new StringBuffer("");
		Map<String,Object> map = this.getDiagTemplate(diagId);
		String param = (String) map.get("template_param");
		logger.warn("param:"+param);
		String lastStr = param.substring(param.length()-1,param.length());
		if(",".equals(lastStr)){
			param = param.substring(0,param.length()-1);
		}
		List<Map<String,Object>> list = dao.getUintListByIds(param);
		for (int i = 0;i<list.size();i++){
			Map<String,Object> m = list.get(i);
			result.append(m.get("id")).append(";").append(m.get("unit_url")).append(",");
		}
		return result.length()>0?result.substring(0, result.length()-1): "";
	}
	
}
