package com.linkage.module.itms.resource.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.itms.resource.dao.NewDeviceQueryDAO;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class NewDeviceQueryBIO {

	private static Logger logger = LoggerFactory
			.getLogger(NewDeviceQueryBIO.class);

	private NewDeviceQueryDAO dao;

	public List<Map> NewDeviceQueryInfo(String city_id,
			String start_time, String end_time, int curPage_splitPage,
			int num_splitPage, String gw_type) {
		logger.debug("NewDeviceQueryInfo()");
		return dao.newDeviceQueryInfo(city_id, start_time, end_time,
				curPage_splitPage, num_splitPage, gw_type);
	}

	public int countNewDeviceQueryInfo(String city_id, String start_time,
			String end_time, int curPage_splitPage, int num_splitPage,
			String gw_type) {
		logger.debug("countNewDeviceQueryInfo()");
		return dao.countNewDeviceQueryInfo(city_id, start_time, end_time,
				curPage_splitPage, num_splitPage, gw_type);
	}

	public List<Map> NewDeviceQueryExcel(String city_id,
			String start_time, String end_time, String gw_type) {
		logger.debug("NewDeviceQueryExcel()");
		return dao.newDeviceQueryExcel(city_id, start_time, end_time,
				gw_type);
	}

	public NewDeviceQueryDAO getDao() {
		return dao;
	}

	public void setDao(NewDeviceQueryDAO dao) {
		this.dao = dao;
	}


}
