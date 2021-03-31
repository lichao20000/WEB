package com.linkage.module.itms.report.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.itms.report.act.PresetEquipmentAnalysisACT;
import com.linkage.module.itms.report.dao.PresetEquipmentAnalysisDAO;

public class PresetEquipmentAnalysisBIO {
	private PresetEquipmentAnalysisDAO dao;

	public Map<String, String> getVendor() {
		return dao.getVendor();
	}

	@SuppressWarnings("rawtypes")
	public String getDeviceModel(String vendorId) {
		List list = dao.getDeviceModel(vendorId);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			if (i > 0) {
				bf.append("#");
			}
			bf.append(map.get("model_name"));
			bf.append("$");
			bf.append(map.get("model_name"));
		}
		return bf.toString();
	}

	@SuppressWarnings({ "rawtypes" })
	public List<Map> countPresetEquipmentAnalysis(String starttime,
			String endtime, String vendorId, String city_id, String modelId) {
		List modelList = dao.getDeviceModel1(vendorId);
		Map<String, String> importnouseList = dao.importnouse(starttime,
				endtime, vendorId, city_id, modelId);// 已导入未使用
		List<Map> list = new ArrayList<Map>();
		Map<String, String> temp = null;

		if (!"-1".equals(vendorId)) {
			if (!"-1".equals(modelId)) {
				temp = new HashMap<String, String>();
				temp.put("vendor_name", vendorId);
				temp.put("model_name", modelId);
				String key = vendorId + "#" + modelId;
				for (int j = 1; j <= 4; j++) {
					key = key + "#" + j;
					temp.put("status" + j, StringUtil.IsEmpty(importnouseList
							.get(key)) ? "0" : importnouseList.get(key));
					key = vendorId + "#" + modelId;
				}
				list.add(temp);
			} else {
				for (int i = 0; i < modelList.size(); i++) {
					temp = new HashMap<String, String>();
					temp.put("vendor_name", vendorId);
					String model_name = StringUtil.getStringValue(modelList
							.get(i));
					temp.put("model_name", model_name);
					String key = vendorId + "#" + model_name;
					for (int j = 1; j <= 4; j++) {
						key = key + "#" + j;
						temp.put("status" + j, StringUtil
								.IsEmpty(importnouseList.get(key)) ? "0"
								: importnouseList.get(key));
						key = vendorId + "#" + model_name;
					}
					list.add(temp);
				}
			}
		} else {
			List<String> vendorList = dao.getVendorList();
			for (int k = 0; k < vendorList.size(); k++) {
				String vendorName = vendorList.get(k);
				for (int i = 0; i < modelList.size(); i++) {
					temp = new HashMap<String, String>();
					temp.put("vendor_name", vendorName);
					String model_name = StringUtil.getStringValue(modelList
							.get(i));
					temp.put("model_name", model_name);
					String key = vendorName + "#" + model_name;
					for (int j = 1; j <= 4; j++) {
						key = key + "#" + j;
						temp.put("status" + j, StringUtil
								.IsEmpty(importnouseList.get(key)) ? "0"
								: importnouseList.get(key));
						key = vendorName + "#" + model_name;
					}
					list.add(temp);
				}
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getDeviceListForWBdTerminal(String city_id,
			String starttime, String endtime, String vendorId, String modelId,
			String status, int curPage_splitPage, int num_splitPage) {
		return dao.getDeviceListForWBdTerminal(city_id, starttime, endtime,
				vendorId, modelId, status, curPage_splitPage, num_splitPage);
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getDevExcel(String city_id, String starttime,
			String endtime, String vendorId, String modelId, String status) {
		return dao.getDevExcel(city_id, starttime, endtime, vendorId, modelId,
				status);
	}

	public int getDeviceListForWBdTerminalCount(String city_id,
			String starttime, String endtime, String vendorId, String modelId,
			String status, int curPage_splitPage, int num_splitPage) {
		return dao.getDeviceListForWBdTerminalCount(city_id, starttime,
				endtime, vendorId, modelId, status, curPage_splitPage,
				num_splitPage);
	}

	public PresetEquipmentAnalysisDAO getDao() {
		return dao;
	}

	public void setDao(PresetEquipmentAnalysisDAO dao) {
		this.dao = dao;
	}
}
