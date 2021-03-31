
package com.linkage.module.gtms.stb.config.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;

public class StreamToolDAO
{

	private static Logger logger = LoggerFactory.getLogger(StreamToolDAO.class);
	@SuppressWarnings("unused")
	private JdbcTemplate jt;

	public int removeDeviceStream(String deviceId)
	{
		logger.debug("removeDeviceStream({})", deviceId);
		int r = -1;
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
		return r;
	}

	@SuppressWarnings("rawtypes")
	public List queryDeviceStream(String deviceId)
	{
		String strSQL = "select stream_id,device_id,device_ip,toward,inter_time,"
				+ " s_ip,s_port,d_ip,d_port" + " from gw_acs_stream where device_id=?"
				+ " order by inter_time";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1, deviceId);
		// 获取结果集
		ArrayList<HashMap<String, String>> result = DBOperation.getRecords(psql
				.toString());
		if (null == result || result.size() == 0)
		{
			return new ArrayList<HashMap<String, String>>();
		}
		// 若码流结果不为空，则获取inter_content字段
		else
		{
			String strSQL1 = "select stream_id,order_id,inter_content from gw_acs_stream_content"
					+ " where device_id=? order by order_id";
			PrepareSQL psql1 = new PrepareSQL(strSQL1);
			psql1.setString(1, deviceId);
			ArrayList<HashMap<String, String>> result_contents = DBOperation
					.getRecords(psql1.toString());
			for (int i = 0; i < result.size(); i++)
			{
				// 将content字段put至结果集
				result.get(i).put(
						"inter_content",
						getContent(result_contents,
								StringUtil.getStringValue(result.get(i), "stream_id")));
			}
			return result;
		}
	}
	
	/**
	 * 
	 * 通过stream_id获取inter_content
	 * 
	 * @param result_contents
	 * @param stream_id
	 * @return
	 */
	private static String getContent(ArrayList<HashMap<String, String>> result_contents,
			String stream_id)
	{
		ArrayList<HashMap<String, String>> result_content = new ArrayList<HashMap<String, String>>();
		for (HashMap<String, String> map : result_contents)
		{
			if (StringUtil.getStringValue(map, "stream_id").equals(stream_id))
			{
				result_content.add(map);
			}
		}
		return getContentStr(result_content);
	}

	/**
	 * 
	 * 将多条inter_content合并成一条记录
	 * 
	 * @param result_content
	 * @return
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static String getContentStr(ArrayList<HashMap<String, String>> result_content)
	{
		StringBuilder result = new StringBuilder();
		if (null == result_content)
		{
			return result.toString();
		}
		else if (0 == result_content.size())
		{
			return result.toString();
		}
		else
		{
			// 根据order_id排序后拼接得到sheet_para
			Collections.sort(result_content, new Comparator()
			{

				@Override
				public int compare(final Object o1, final Object o2)
				{
					return new Integer(StringUtil.getIntValue(
							(HashMap<String, String>) o1, "order_id"))
							.compareTo(new Integer(StringUtil.getIntValue(
									(HashMap<String, String>) o2, "order_id")));
				}
			});
			for (HashMap<String, String> map : result_content)
			{
				result.append(StringUtil.getStringValue(map, "inter_content"));
			}
			return result.toString();
		}
	}

	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}
}