package com.linkage.module.gtms.itv.dao;

import java.util.List;
import java.util.Map;

public interface ISAPicDAO {
	@SuppressWarnings("unchecked")
	public List<Map> getISATerminalReport(String cityName);
	
	@SuppressWarnings("unchecked")
	public List<Map> getISAEPGReport(String cityName);
	
	//频道省中心的
	@SuppressWarnings("unchecked")
	public List<Map> getISAEPGReportSZX();

	@SuppressWarnings("unchecked")
	public List<Map> getEPG();
}
