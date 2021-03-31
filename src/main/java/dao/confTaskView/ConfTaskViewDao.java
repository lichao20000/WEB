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

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;

import dao.util.JdbcTemplateExtend;

/**
 * @author OneLineSky E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2008-12-18
 * @category dao.confTaskView
 * 
 */
public class ConfTaskViewDao {

	Logger log = LoggerFactory.getLogger(ConfTaskViewDao.class);
	
	private JdbcTemplateExtend jt;

	private Map<String, String> is_check_map = new HashMap<String, String>();

	private Map<String, String> is_over_map = new HashMap<String, String>();

	public ConfTaskViewDao() {
		is_check_map.put("0", "未审核");
		is_check_map.put("1", "通过");
		is_check_map.put("2", "不通过");

		is_over_map.put("-1", "撤销");
		is_over_map.put("0", "尚未执行");
		is_over_map.put("1", "正在执行");
		is_over_map.put("2", "执行完成");
	}

	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}

	/**
	 * 查询配置任务信息列表
	 * 
	 * @return
	 */
	public List getConfTask(int curPage_splitPage, int num_splitPage,
			long area_id, String task_name, String order_time_start,
			String order_time_end, String is_check, String is_over) {

		String sql = "select a.task_id,a.task_name,b.acc_loginname as order_acc_oid,a.order_time,"
				+ "a.is_check,a.is_over,a.comp_perc,a.succ_perc from gw_conf_all_task a,"
				+ "tab_accounts b where a.order_acc_oid = b.acc_oid ";

		if( 1!=area_id ) {
			
			sql += " and a.task_id in (select res_id from tab_gw_res_area " +
					"where res_type=13 and area_id = " + area_id + ")";
		}else{
			sql += " and a.task_id in (select res_id from tab_gw_res_area " +
			"where res_type=13 )";
		}
		
		if (!(null == task_name || "".equals(task_name))) {
			sql += " and a.task_name like '%" + task_name + "%' ";
		}

		if (!(null == order_time_start || "".equals(order_time_start))) {
			sql += " and a.order_time > "
					+ new DateTimeUtil(order_time_start).getLongTime();
		}
		if (!(null == order_time_end || "".equals(order_time_end))) {

			sql += " and a.order_time < "
					+ new DateTimeUtil(order_time_end).getNextLongTime();
		}
		if (!(null == is_check || "".equals(is_check))) {

			sql += " and a.is_check = " + is_check;
		}
		if (!(null == is_over || "".equals(is_over))) {

			sql += " and a.is_over = " + is_over;
		}

		sql +="  order by order_time desc";
		
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		
		return jt.querySP(sql, (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, String> map = new HashMap<String, String>();

						map.put("task_id", rs.getString("task_id"));

						map.put("task_name", rs.getString("task_name"));

						map.put("order_acc_oid", rs.getString("order_acc_oid"));

						map.put("order_time", new DateTimeUtil(Long.valueOf(rs
								.getString("order_time")+"000")).getDate());

						map.put("is_check", is_check_map.get(rs
								.getString("is_check")));

						map.put("is_over", is_over_map.get(rs
								.getString("is_over")));

						map.put("comp_perc", rs.getString("comp_perc")+"%");

						map.put("succ_perc", rs.getString("succ_perc")+"%");

						return map;
					}
				});
	}

	public int getConfTaskCount(int num_splitPage, long area_id,
			String task_name, String order_time_start, String order_time_end,
			String is_check, String is_over) {

		String sql = "select count(1) from gw_conf_all_task a,"
				+ "tab_accounts b where a.order_acc_oid = b.acc_oid ";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select count(*) from gw_conf_all_task a,"
					+ "tab_accounts b where a.order_acc_oid = b.acc_oid ";
		}

		if( 1!=area_id ) {
			
			sql += " and a.task_id in (select res_id from tab_gw_res_area " +
					"where res_type=13 and area_id = " + area_id + ")";
		}else{
			sql += " and a.task_id in (select res_id from tab_gw_res_area " +
			"where res_type=13 )";
		}
		
		if (!(null == task_name || "".equals(task_name))) {
			sql += " and a.task_name like '%" + task_name + "%' ";
		}

		if (!(null == order_time_start || "".equals(order_time_start))) {
			sql += " and order_time > "
					+ new DateTimeUtil(order_time_start).getLongTime();
		}
		if (!(null == order_time_end || "".equals(order_time_end))) {

			sql += " and order_time < "
					+ new DateTimeUtil(order_time_end).getNextLongTime();
		}
		if (!(null == is_check || "".equals(is_check))) {

			sql += " and a.is_check = " + is_check;
		}
		if (!(null == is_over || "".equals(is_over))) {

			sql += " and a.is_over = " + is_over;
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
