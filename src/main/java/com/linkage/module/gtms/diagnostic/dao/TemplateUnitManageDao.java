package com.linkage.module.gtms.diagnostic.dao;

import java.util.List;
import java.util.Map;

public interface TemplateUnitManageDao {
	public List<Map<String,Object>> getAllRecords();

	public int getRecordById(int unitId);

	public int update(String unitId,String templateUnitName, String templateUnitURL);

	public int delete(String unitId);

	public int add(String templateUnitName, String templateUnitURL);

	public Map<String, Object> getpreUpdateRecord(String unitId);
}
