package com.linkage.module.gtms.stb.resource.serv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.module.gtms.stb.resource.dao.BatchPingDAO;
import com.linkage.module.gtms.stb.resource.dao.CustomerGroupDAO;

public class CustomerGroupBIO {

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(CustomerGroupBIO.class);
	private CustomerGroupDAO cgDao = null;

	/**
	 * 查询数据
	 * @param groupName
	 * @return
	 */
	public List<HashMap<String, String>> getDataList(String groupName) {
		logger.debug("CustomerGroupBIO => getDataList");
		return cgDao.queryDataList(groupName);
	}

	/**
	 * 查询数据
	 * @param groupName
	 * @return
	 */
	public Map<String, String> getData(String groupId) {
		logger.debug("CustomerGroupBIO => getData");
		return cgDao.queryData(groupId);
	}

	/**
	 * 查询记录数
	 * @param cityId
	 * @param deviceIp
	 * @return
	 */
	public int getDataCount(String groupId) {
		logger.debug("CustomerGroupBIO => getDataCount");
		int count = cgDao.queryCount(groupId);
		return count;
	}

	
	/**
	 * 
	 * @param groupId
	 * @param groupName
	 * @param remark
	 * @param operator
	 * @return
	 */
	public int insertData(String groupId, String groupName, String remark, String operator) {
		return cgDao.insertData(groupId, groupName, remark, operator);
	}
	
	/**
	 * 
	 * @param groupId
	 * @param groupName
	 * @param remark
	 * @param operator
	 * @return
	 */
	public int updateData(String groupId, String groupName, String remark, String operator) {
		return cgDao.updateData(groupId, groupName, remark, operator);
	}

	/**
	 * 
	 * @param groupId
	 * @return
	 */
	public int deleteData(String groupId) {
		return cgDao.deleteData(groupId);
	}

	public CustomerGroupDAO getCgDao() {
		return cgDao;
	}

	public void setCgDao(CustomerGroupDAO cgDao) {
		this.cgDao = cgDao;
	}
}
