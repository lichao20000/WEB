package com.linkage.module.itms.resource.bio;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.dao.DPIDAO;

public class DPIBIO {
	private static Logger logger = LoggerFactory.getLogger(DPIBIO.class);

	private DPIDAO dao;

	@SuppressWarnings("rawtypes")
	public List<Map> queryDPIList(String startOpenDate1, String endOpenDate1,
			String cityId, String vendor, String devicemodel) {
		List<Map> list = new ArrayList<Map>();
		Map cityMap = CityDAO.getCityIdCityNameMap();
		Map<String, String> vendorMap = dao.getVendorMap();
		Map<String, String> modelMap = dao.getDeviceModel();
		List resultlist = dao.queryDPIList(startOpenDate1, endOpenDate1,
				cityId, vendor, devicemodel);
		// 总配置数
		long allup = 0;
		// 成功数
		long successnum = 0;
		// 失败（需重配）数
		long failnum = 0;
		// 未做数
		long noupnum = 0;
		// 失败（下次上线再做）
		long nextnum = 0;
		for (int i = 0; i < resultlist.size(); i++) {
			Map<String, Object> tmap = new HashMap<String, Object>();
			allup = 0;
			successnum = 0;
			failnum = 0;
			noupnum = 0;
			nextnum = 0;
			Map rmap = (Map) resultlist.get(i);
			String tempCityId = StringUtil.getStringValue(rmap.get("city_id"));
			String vendor_id = StringUtil.getStringValue(rmap.get("vendor_id"));
			String device_model_id = StringUtil.getStringValue(rmap
					.get("device_model_id"));
			tmap.put("city_id", tempCityId);
			tmap.put("city_name", cityMap.get(tempCityId));
			tmap.put("vendor_id", vendorMap.get(vendor_id));
			tmap.put("device_model_id", modelMap.get(device_model_id));
			tmap.put("vendor_id_id", vendor_id);
			tmap.put("device_model_id_id", device_model_id);
			if ("100".equals(StringUtil.getStringValue(rmap.get("status")))) {
				if ("1".equals(StringUtil.getStringValue(rmap.get("result_id")))) {
					// 成功数
					successnum = successnum
							+ StringUtil.getLongValue(rmap.get("total"));
				} else {
					// 失败（需重配）数
					failnum = failnum
							+ StringUtil.getLongValue(rmap.get("total"));
				}
			} else if ("0"
					.equals(StringUtil.getStringValue(rmap.get("status")))) {
				if ("0".equals(StringUtil.getStringValue(rmap.get("result_id")))) {
					// 未做数
					noupnum = noupnum
							+ StringUtil.getLongValue(rmap.get("total"));
				} else {
					if (!"1".equals(StringUtil.getStringValue(rmap
							.get("result_id")))) {
						// 失败（下次上线再做）
						nextnum = nextnum
								+ StringUtil.getLongValue(rmap.get("total"));
					}
				}
			}
			allup = allup + successnum + noupnum + nextnum + failnum;
			tmap.put("allup", allup);
			tmap.put("successnum", successnum);
			tmap.put("noupnum", noupnum);
			tmap.put("nextnum", nextnum);
			tmap.put("failnum", failnum);
			tmap.put("percent", percent(successnum, successnum + failnum));
			list.add(tmap);
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public List<Map> queryDetail(String startOpenDate1, String endOpenDate1,
			String city_id, String vendor, String devicemodel, String status,
			String resultId, int curPage_splitPage, int num_splitPage) {
		logger.debug("DPIDAO=>queryDPIList");
		return dao
				.queryDetail(startOpenDate1, endOpenDate1, city_id, vendor,
						devicemodel, status, resultId, curPage_splitPage,
						num_splitPage);
	}
	
	public int countQueryDetail(String startOpenDate1, String endOpenDate1,
			String city_id, String vendor, String devicemodel, String status,
			String resultId, int curPage_splitPage, int num_splitPage) {
		logger.debug("DPIDAO=>countQueryDPIList");
		return dao.countQueryDetail(startOpenDate1, endOpenDate1, city_id,
				vendor, devicemodel, status, resultId, curPage_splitPage,
				num_splitPage);
	}
	
	@SuppressWarnings("rawtypes")
	public List<Map> excelqueryDetail(String startOpenDate1,
			String endOpenDate1, String city_id, String vendor,
			String devicemodel, String status, String resultId) {
		logger.debug("DPIDAO=>queryDPIList");
		return dao.excelqueryDetail(startOpenDate1, endOpenDate1, city_id,
				vendor, devicemodel, status, resultId);
	}

	public String percent(long p1, long p2) {
		double p3;
		if (p2 == 0) {
			return "N/A";
		} else {
			p3 = (double) p1 / p2;
		}
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p3);
		return str;
	}

	public DPIDAO getDao() {
		return dao;
	}

	public void setDao(DPIDAO dao) {
		this.dao = dao;
	}
}
