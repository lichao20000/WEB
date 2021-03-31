package com.linkage.module.gtms.diagnostic.serv;

import java.util.List;
import java.util.Map;

public interface DiagTemlateServ {

	public 	int add(String accOid, String templateName, String templateParam);

	public  int delete(String diagId);

	public	List<Map<String, Object>> getRecords();

	public List<Map<String, Object>> getTemplateUnits();

	public Map<String, Object> getDiagTemplate(String diagId);

	public int update(String diagId, String templateName, String templateParam);

	public String getUintListByTempId(String diagId);


}
