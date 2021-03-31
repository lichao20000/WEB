package com.linkage.module.gtms.itv.bio;

import java.util.List;
import java.util.Map;

public interface ISAPicBIO {
	@SuppressWarnings("unchecked")
	public List<Map> getISATerminalReport(String cityId);
	
	@SuppressWarnings("unchecked")
	public List<Map> getISAEPGReport(String cityId);
	
	@SuppressWarnings("unchecked")
	public List<Map> getISAEPGReportSZX();
	
	@SuppressWarnings("unchecked")
	List<Map> getEPG();
}
