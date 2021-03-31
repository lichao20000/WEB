package com.linkage.module.gtms.diagnostic.dao;

import java.util.List;
import java.util.Map;

public interface DiagTemlateDao {

  public 	int delete(String diagId);

  public List<Map<String, Object>> getAllRecords();

  public int add(String accOid, String templateName, String templateParam);

  public Map<String, Object> getDiagTemplate(String diagId);

  public List<Map<String, Object>> getTemplateUnits();

  public int getDiaTemplateById(String diagId);

  public int update(String diagId, String templateName, String templateParam);

public List<Map<String,Object>> getUintListByIds(String param);

}
