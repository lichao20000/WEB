package com.linkage.module.itms.resource.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.itms.resource.dao.DepartManageDAO;

@SuppressWarnings("rawtypes")
public class DepartManageBIO {

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(DepartManageBIO.class);
	private DepartManageDAO dao;
	private int maxPage_splitPage;

	public int getMaxPage_splitPage() {
		return maxPage_splitPage;
	}

	public void setMaxPage_splitPage(int maxPage_splitPage) {
		this.maxPage_splitPage = maxPage_splitPage;
	}

	public DepartManageDAO getDao() {
		return dao;
	}

	public void setDao(DepartManageDAO dao) {
		this.dao = dao;
	}

	/**
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param startTime
	 * @param endTime
	 * @param nameSearch
	 * @return
	 */
	public List<Map> queryDepartList(int curPage_splitPage, int num_splitPage, String startTime, String endTime, String nameSearch) {
		logger.debug("queryDepartList({})", new Object[] { curPage_splitPage, num_splitPage, startTime, endTime, nameSearch });
		List<Map> list = dao.queryDepartList(curPage_splitPage, num_splitPage, startTime, endTime, nameSearch);
		maxPage_splitPage = dao.getDepartListCount(curPage_splitPage, num_splitPage, startTime, endTime, nameSearch);
		return list;
	}
	
	/**
	 * 添加部门
	 * @param deptname
	 * @param deptdesc
	 * @param acc_oid
	 * @return 0：成功  -1：失败 1：相同部门
	 */
	public String addDepartInfo(String deptname, String deptdesc, Long acc_oid) {
		logger.debug("addDepartInfo({})", new Object[] { deptname, deptdesc, acc_oid });
		int num = dao.queryDepartName(deptname, true, 0l);
		if (num > 0) {
			return "1";
		}
		int result = dao.addDepartInfo(deptname, deptdesc, acc_oid);
		if (result != 0) {
			return "0";
		}
		return "-1";
	}

	/**
	 * 删除部门
	 * @param departid
	 * @return 0：成功  -1：失败
	 */
	public String deleteDepart(Long departid) {
		logger.debug("deleteDepart({})", new Object[] { departid });
		int result = dao.deleteDepart(departid);
		if (result != 0) {
			return "0";
		}
		return "-1";
	}

	/**
	 * 编辑部门
	 * @param deptname
	 * @param deptdesc
	 * @param acc_oid
	 * @param departid
	 * @return 0：成功  -1：失败 1：相同部门
	 */
	public String editDepartInfo(String deptname, String deptdesc, Long acc_oid, Long departid) {
		logger.debug("editDepartInfo({})", new Object[] { deptname, deptdesc, acc_oid, departid });
		int num = dao.queryDepartName(deptname, false, departid);
		if (num > 0) {
			return "1";
		}
		int result = dao.editDepartInfo(deptname, deptdesc, acc_oid, departid);
		if (result != 0) {
			return "0";
		}
		return "-1";
	}
}
