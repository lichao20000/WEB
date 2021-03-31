package com.linkage.module.itms.report.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.itms.report.dao.BssLogDAO;

public class BssLogBIO {
	private static Logger logger = LoggerFactory
			.getLogger(BssLogBIO.class);
	private BssLogDAO dao;

	@SuppressWarnings("rawtypes")
	public List<Map> bsslogList(String loid, String bussinessacount,
			String startOpenDate1, String operationuser,String bssaccount, int curPage_splitPage,
			int num_splitPage) {
		return dao.bsslogList(loid, bussinessacount, startOpenDate1,
				operationuser,bssaccount, curPage_splitPage, num_splitPage);
	}

	public int countbsslogList(String loid, String bussinessacount,
			String startOpenDate1, String operationuser,String bssaccount, int curPage_splitPage,
			int num_splitPage) {
		return dao.countbsslogList(loid, bussinessacount, startOpenDate1,
				operationuser,bssaccount, curPage_splitPage, num_splitPage);
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getcountbsslogExcel(String loid,
			String bussinessacount, String startOpenDate1, String operationuser,String bssaccount) {
		return dao.getcountbsslogExcel(loid, bussinessacount,
				startOpenDate1, operationuser,bssaccount);
	}

	public BssLogDAO getDao() {
		return dao;
	}

	public void setDao(BssLogDAO dao) {
		this.dao = dao;
	}

}
