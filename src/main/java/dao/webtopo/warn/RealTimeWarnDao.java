package dao.webtopo.warn;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import RemoteDB.AlarmEvent;
import RemoteDB.GatherIDEvent;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.webtopo.Scheduler;
import com.linkage.module.gwms.Global;

/**
 * WebTopo实时告警牌显示DAO
 * <li>REQ: GZDX-REQ-20080402-ZYX-001
 * <li>REQ: HBDX-REQ-20080220-WANGBING-001
 * 
 * @author 贲友朋
 * @version 1.0
 * @since 2008-4-8
 * @category WebTopo/实时告警牌
 * 
 */
public class RealTimeWarnDao {
	// jdbc模板
	private JdbcTemplate jt;
	
	// ********************************************************************
	public List<Map> getWarnInfoList(long acc_oid) {
		String sql = "select warnlevel,warnvoice,voicetype  from  tab_warn_config where acc_oid="
				+ acc_oid;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}

	/**
	 * 得到需要展示的列名
	 */
	public List getColumn(String user_name) {
		String sql = "select * from tab_warn_column where visible=1 order by sequence";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select field_name,field_desc,sequence,visible from tab_warn_column where visible=1 order by sequence";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}

	/**
	 * 获取规则列表
	 * 
	 * @param acc_loginname:登陆用户名
	 * @return list:{rule_id,rule_name,maxnum,ispublic}
	 */
	public List getRuleList(String acc_loginname) {
		String sql = "select rule_id,rule_name,maxnum,ispublic,selected from tab_warn_filterrule where ispublic=1 or acc_loginname='"
				+ acc_loginname + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}

	/**
	 * 初始化进来，获取所有告警
	 * 
	 * @return
	 */
	public List getAllAlarm(String area_id, GatherIDEvent[] temp_arr_id) {
		Scheduler sch = new Scheduler();
		AlarmEvent[] tempAlarmList = sch.getNewAlarm(area_id, temp_arr_id);
		return null;
	}

	/**
	 * 通过告警创建时间获取该告警所在的表名
	 * 
	 * @param createTime
	 *            创建时间(秒)
	 * @return
	 */
	private String getTableNameByCreateTime(long createTime) {
		DateTimeUtil dateUtil = new DateTimeUtil(createTime * 1000);
		int year = dateUtil.getYear();
		int week = dateUtil.getWeekOfYear();
		return "event_raw_" + year + "_" + week;
	}

	/**
	 * 确认告警更新历史告警字段
	 * 
	 * @param creat_time:告警发生时间
	 * @param serialno:告警序列号
	 * @param gather_id:采集点
	 * @param account:操作人
	 */
	public void AckAlarm(long creat_time, String serialno, String gather_id,
			String account) {
		String tab_name = getTableNameByCreateTime(creat_time);
		long t = System.currentTimeMillis() / 1000;
		String sql = "update " + tab_name + " set acktime=" + t
				+ ", activestatus =3,operaccount='" + account
				+ "'  where serialno='" + serialno + "' and gather_id='"
				+ gather_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		jt.execute(sql);
	}

	/**
	 * 清除告警更新历史告警字段
	 * 
	 * @param creat_time:告警发生时间
	 * @param serialno:告警序列号
	 * @param gather_id:采集点
	 * @param account:操作人
	 */
	public void ClearAlarm(long creat_time, String serialno, String gather_id,
			String account) {
		String tab_name = getTableNameByCreateTime(creat_time);
		long t = System.currentTimeMillis() / 1000;
		String sql = "update " + tab_name + " set cleartime=" + t
				+ ", clearstatus =3,operaccount='" + account
				+ "'  where serialno='" + serialno + "' and gather_id='"
				+ gather_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		jt.execute(sql);
	}

	// ********************************************************************************
	/**
	 * 初始化数据库连接
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}

}
