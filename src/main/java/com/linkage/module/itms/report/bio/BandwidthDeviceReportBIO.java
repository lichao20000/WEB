
package com.linkage.module.itms.report.bio;

import java.util.List;
import java.util.Map;

import com.linkage.module.itms.report.dao.BandwidthDeviceReportDAO;

@SuppressWarnings("rawtypes")
public class BandwidthDeviceReportBIO {

	private BandwidthDeviceReportDAO dao = null;
	//private static Logger logger = LoggerFactory.getLogger(BandwidthDeviceReportBIO.class);

	
	public List<Map> getDeviceInfo(int curPage_splitPage, int num_splitPage, String cityId, String bandwidth, String isSpeedUp) {
		return dao.getDeviceInfo(curPage_splitPage, num_splitPage, cityId, bandwidth, isSpeedUp);
	}
	public int getDeviceInfoCount(int num_splitPage, String cityId, String bandwidth, String isSpeedUp) {
		return dao.getDeviceInfoCount(num_splitPage, cityId, bandwidth, isSpeedUp);
	}

	public List<Map> getDeviceInfoExcel(String cityId, String bandwidth, String isSpeedUp) {
		return dao.getAllDeviceInfo(cityId, bandwidth, isSpeedUp);
	}

	public BandwidthDeviceReportDAO getDao() {
		return dao;
	}
	public void setDao(BandwidthDeviceReportDAO dao) {
		this.dao = dao;
	}
}
