package com.linkage.module.itms.service.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 
 * @author jianglp (75508)
 * @version 1.0
 * @since 2016-11-23
 * @category com.linkage.module.itms.service.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 * 
 */
public class FailedSheetOptDao extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(ErrorBssSheetDAO.class);
	
	public int countFailedSheet(String reciveDateStart, String reciveDateEnd,
			int curPage_splitPage, int num_splitPage) 
	{
		logger.debug("FailedSheetOptDao-->countFailedSheet");
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) from tab_err_bss_sheet where 1=1");
		}else{
			sql.append("select count(1) from tab_err_bss_sheet where 1=1");
		}
		
		if (false == StringUtil.IsEmpty(reciveDateStart)) {
			sql.append(" and recieve_date > ").append(reciveDateStart);
		}
		if (false == StringUtil.IsEmpty(reciveDateEnd)) {
			sql.append(" and recieve_date < ").append(reciveDateEnd);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public List<Map<String, String>> queryFailedSheet(String reciveDateStart,
			String reciveDateEnd, int curPage_splitPage, int num_splitPage) {
		return null;
	}

	public List<HashMap<String, String>> queryFailedSheetByLoidList(
			List<String> dataList) {
		String sql = "SELECT LOGID,LOID,SHEET,OPTTIME FROM TAB_FAILSHEET_LOG WHERE 1=1";
		List<HashMap<String, String>> resList = null;
		Set<String> loidSet = new HashSet<String>();
		for (String loid : dataList) {
			loidSet.add(loid);
		}
		int size = loidSet.size();
		String[] strArray = null;
		// 如果大于1000，那么需要使用多个in合并
		if (size % 1000 != 0) {
			strArray = new String[size / 1000 + 1];
		} else {
			strArray = new String[size / 1000 + 1];
		}
		for (int index = 0; index < strArray.length; index++) {
			String inStr = "(";
			Iterator<String> itr = loidSet.iterator();
			int i = 0;
			while (itr.hasNext()) {
				inStr += itr.next();
				if (i != 0 && i < (loidSet.size() - 1)) {
					inStr += ",";
				}
				i++;
			}
			inStr += ")";
			strArray[index] = inStr;
			sql += "and loid in " + inStr;
		}
		resList = DBOperation.getRecords(sql);
		return resList;
	}
}
