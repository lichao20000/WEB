package com.linkage.module.gtms.diagnostic.serv;

import java.util.List;
import java.util.Map;

public interface TemplateUnitManageServ {
	public List<Map<String,Object>> getAllRecords();

	public int add(String templateUnitName, String templateUnitURL);

	public int update(String unitId, String templateUnitName,
			String templateUnitURL);

	public int delete(String unitId);

	public Map<String, Object> getpreUpdateRecord(String unitId);
}
