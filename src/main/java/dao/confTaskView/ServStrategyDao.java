/**
 * 
 */
package dao.confTaskView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;

import dao.util.JdbcTemplateExtend;

/**
 * @author OneLineSky E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2008-12-18
 * @category dao.confTaskView
 * 
 */
public class ServStrategyDao {
	
	Logger log = LoggerFactory.getLogger(ServStrategyDao.class);

	private JdbcTemplateExtend jt;

	private Map<String, String> status_map = new HashMap<String, String>();

	public ServStrategyDao() {
		status_map.put("0", "等待执行");
		status_map.put("1", "预读PVC");
		status_map.put("2", "预读绑定端口");
		status_map.put("3", "预读无线");
		status_map.put("4", "业务下发");
		status_map.put("100", "执行完成");
	}

	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}

	@SuppressWarnings("unchecked")
	public String getTaskName(String task_id) {

		String sql = "select task_name from gw_conf_all_task where task_id = '"
				+ task_id + "'";

		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();

		List list = jt.queryForList(sql);

		String task_name = null;

		if (list.size() > 0) {

			task_name = ((Map<String, String>) list.get(0)).get("task_name");
		}

		return task_name;
	}

	/**
	 * 查询当前所有的业务操作
	 * 
	 * @return
	 */
	public List getAllService_idList() {
		String sql = "select service_id,service_name from tab_service";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}

	public List getServStrategList(int curPage_splitPage, int num_splitPage,
			String task_id, String status, String device_serialnumber,
			String service_id) {

		String sql = "select a.status,a.result_id,a.result_desc,a.time,"
				+ "a.oui,a.device_serialnumber,a.username,c.service_name "
				+ "from gw_serv_strategy a ,tab_service c where "
				+ " a.service_id=c.service_id ";

		if (!(null == task_id || "".equals(task_id))) {
			sql += " and a.task_id = '" + task_id + "' ";
		}
		if (!(null == status || "".equals(status))) {
			sql += " and a.status = " + status;
		}
		if (!(null == device_serialnumber || "".equals(device_serialnumber))) {
			sql += " and a.device_serialnumber like '%" + device_serialnumber
					+ "%' ";
		}
		if (!(null == service_id || "".equals(service_id))) {
			sql += " and a.service_id = " + service_id;
		}

		sql +="  order by a.username,c.service_name asc ";
		
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();

		return jt.querySP(sql, (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, String> map = new HashMap<String, String>();

						map.put("status", status_map
								.get(rs.getString("status")));

						String result_id_ = rs.getString("result_id");

						if ("1".equals(result_id_)) {
							map.put("result_id", "成功");
						} else if ("0".equals(result_id_)
								|| "2".equals(result_id_)) {
							map.put("result_id", "设备无法连接");
						} else if ("3".equals(result_id_)) {
							map.put("result_id", "设备无法连接");
						} else {
							map.put("result_id", "失败");
						}

						map.put("result_desc", rs.getString("result_desc"));

						map.put("time", new DateTimeUtil(Long.valueOf(rs
								.getString("time")+"000")).getDate());

						map.put("oui", rs.getString("oui"));

						map.put("device_serialnumber", rs
								.getString("device_serialnumber"));

						map.put("username", rs.getString("username"));

						map.put("service_name", rs.getString("service_name"));

						return map;
					}
				});
	}

	public int getServStrategyCount(int num_splitPage, String task_id,
			String status, String device_serialnumber, String service_id) {

		String sql = "select count(*) from gw_serv_strategy a ,tab_service c where a.service_id=c.service_id ";

		if (!(null == task_id || "".equals(task_id))) {
			sql += " and a.task_id = '" + task_id + "' ";
		}
		if (!(null == status || "".equals(status))) {
			sql += " and a.status = " + status;
		}
		if (!(null == device_serialnumber || "".equals(device_serialnumber))) {
			sql += " and a.device_serialnumber like '%" + device_serialnumber
					+ "%' ";
		}
		if (!(null == service_id || "".equals(service_id))) {
			sql += " and a.service_id = " + service_id;
		}

		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();

		int total = jt.queryForInt(sql);
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}

		return maxPage;
	}

}
