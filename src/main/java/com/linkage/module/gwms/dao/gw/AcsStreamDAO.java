package com.linkage.module.gwms.dao.gw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;

/**
 * @author Jason(3412)
 * @date 2009-7-29
 */
public class AcsStreamDAO extends com.linkage.module.gwms.dao.SuperDAO {

	private static Logger logger = LoggerFactory
			.getLogger(AcsStreamDAO.class);

	/**
	 * 获取设备码流记录信息
	 * 
	 * @param device_id
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	public List queryDeviceStream(String deviceId) {
		logger.debug("queryDeviceStream({})", deviceId);
		List rList = null;
		rList = new ArrayList<HashMap<String, String>>();
		if (StringUtil.getIntegerValue(LipossGlobals
				.getLipossProperty("isNewStream")) == 1) {
			rList = queryDeviceStreamNew(deviceId);
		} else {
			rList = queryDeviceStreamOld(deviceId);
		}
		return rList;
	}

	/**
	 * 根据device_id查询码流记录new
	 * 
	 * @param deviceId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List queryDeviceStreamNew(String deviceId) {
		String strSQL = "select stream_id,device_id,device_ip,toward,inter_time,"
				+ " s_ip,s_port,d_ip,d_port"
				+ " from gw_acs_stream where device_id=?"
				+ " order by inter_time";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1, deviceId);
		// 获取结果集
		ArrayList<HashMap<String, String>> result = DBOperation.getRecords(psql
				.toString());
		if (null == result || result.size() == 0) {
			return new ArrayList<HashMap<String, String>>();
		}
		// 若码流结果不为空，则获取inter_content字段
		else {
			String strSQL1 = "select stream_id,order_id,inter_content from gw_acs_stream_content"
					+ " where device_id=? order by order_id";
			PrepareSQL psql1 = new PrepareSQL(strSQL1);
			psql1.setString(1, deviceId);
			ArrayList<HashMap<String, String>> result_contents = DBOperation
					.getRecords(psql1.toString());
			for (int i = 0; i < result.size(); i++) {
				// 将content字段put至结果集
				result.get(i).put(
						"inter_content",
						getContent(result_contents, StringUtil.getStringValue(
								result.get(i), "stream_id")));
			}
			return result;
		}
	}

	/**
	 * 通过stream_id获取inter_content
	 * 
	 * @param result_contents
	 * @param stream_id
	 * @return
	 */
	private static String getContent(
			ArrayList<HashMap<String, String>> result_contents, String stream_id) {
		ArrayList<HashMap<String, String>> result_content = new ArrayList<HashMap<String, String>>();
		for (HashMap<String, String> map : result_contents) {
			if (StringUtil.getStringValue(map, "stream_id").equals(stream_id)) {
				result_content.add(map);
			}
		}
		return getContentStr(result_content);
	}

	/**
	 * 将多条inter_content合并成一条记录
	 * 
	 * @param result_content
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private static String getContentStr(
			ArrayList<HashMap<String, String>> result_content) {
		StringBuilder result = new StringBuilder();
		if (null == result_content) {
			return result.toString();
		} else if (0 == result_content.size()) {
			return result.toString();
		} else {
			// 根据order_id排序后拼接得到sheet_para
			Collections.sort(result_content, new Comparator() {

				@Override
				public int compare(final Object o1, final Object o2) {
					return new Integer(StringUtil.getIntValue(
							(HashMap<String, String>) o1, "order_id"))
							.compareTo(new Integer(StringUtil.getIntValue(
									(HashMap<String, String>) o2, "order_id")));
				}
			});
			for (HashMap<String, String> map : result_content) {
				result.append(StringUtil.getStringValue(map, "inter_content"));
			}
			return result.toString();
		}
	}

	/**
	 * 清除设备的码流记录
	 * 
	 * @param
	 * @return int
	 */
	public int removeDeviceStream(String deviceId) {
		logger.debug("removeDeviceStream({})", deviceId);
		int r = -1;
		if (StringUtil.getIntegerValue(LipossGlobals
				.getLipossProperty("isNewStream")) == 1) {
			String strSQL = "delete from gw_acs_stream where device_id=?";
			PrepareSQL psql = new PrepareSQL(strSQL);
			psql.setString(1, deviceId);
			PrepareSQL psql1 = new PrepareSQL(
					"delete from gw_acs_stream_content where device_id=?");
			psql1.setString(1, deviceId);
			ArrayList<String> sqlList = new ArrayList<String>();
			sqlList.add(psql.toString());
			sqlList.add(psql1.toString());
			r = DBOperation.executeUpdate(sqlList);
		} else {
			r = removeDeviceStreamSybase(deviceId);
		}
		return r;
	}

	/**
	 * 获取设备码流记录信息
	 * 
	 * @param
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	public List queryDeviceStreamOld(String deviceId) {
		logger.debug("queryDeviceStream({})", deviceId);
		List rList = null;
		String strSQL = "select device_id,device_ip,toward,inter_time,inter_content,"
				+ " s_ip,s_port,d_ip,d_port"
				+ " from gw_acs_stream where device_id=?"
				+ " order by inter_time";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1, deviceId);
		rList = new ArrayList<HashMap<String, String>>();
		rList = jt.queryForList(psql.getSQL());
		return rList;
	}

	/**
	 * 清除设备的码流记录
	 * 
	 * @param
	 * @return int
	 */
	public int removeDeviceStreamSybase(String deviceId) {
		logger.debug("removeDeviceStream({})", deviceId);
		int r = -1;
		String strSQL = "delete from gw_acs_stream where device_id=?";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1, deviceId);
		try {
			r = jt.update(psql.getSQL());
		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return r;
	}
}
