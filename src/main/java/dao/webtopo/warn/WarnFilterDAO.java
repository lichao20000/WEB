package dao.webtopo.warn;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import bio.webtopo.warn.filter.ConstantEventEnv;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;

/**
 * WebTopo实时告警牌告警规则定义与修改DAO
 * <li>REQ: GZDX-REQ-20080402-ZYX-001
 * <li>REQ: HBDX-REQ-20080220-WANGBING-001
 * 
 * @author 段光锐
 * @version 1.0
 * @since 2008-4-8
 * @category WebTopo/实时告警牌/告警规则
 * 
 */
public class WarnFilterDAO
{
	// jdbc模板
	private JdbcTemplate jt;

	private static final Logger LOG = LoggerFactory.getLogger(WarnFilterDAO.class);
	
	/**
	 * 添加规则模板的SQL
	 */
	private final String addRuleInfoSQL = "insert into tab_warn_filterrule (acc_loginname,rule_id,rule_name,createtime,maxnum,selected,ispublic,visible) ";

	/**
	 * 添加规则详情的SQL
	 */
	private final String addRuleDetailInfoSQL = "insert into tab_warn_filterdetail (rule_id,rule_priority,rule_content,rule_invocation) ";

	/**
	 * 删除规则模板的SQL
	 */
	private final String delRuleInfoSQL = "delete from tab_warn_filterrule where rule_id=";

	/**
	 * 删除规则详情的SQL
	 */
	private final String delRuleDetailInfoSQL = "delete from tab_warn_filterdetail where rule_id=";

	/**
	 * 重置规则模板中的默认显示字段的信息
	 */

	private final String resetSelectedSQL = "update tab_warn_filterrule set selected=0 where acc_loginname = ";
	/**
	 * 通过用户获取告警规则模板的SQL
	 */
	private final String getWarnRuleByUserSQL = "select acc_loginname,rule_id,rule_name,createtime,maxnum,selected,ispublic,visible from tab_warn_filterrule where acc_loginname=";

	/**
	 * 获取除开当前用户的共享规则模板的SQL
	 */
	private final String getShareWarnRuleExceptUserSQL = "select acc_loginname,rule_id,rule_name,createtime,maxnum,selected,ispublic,visible from tab_warn_filterrule where ispublic=1 and acc_loginname!=";

	/**
	 * 获取告警列名配置信息的SQL
	 */
	private final String getWarnColumnSQL = "select field_name,field_desc,sequence,visible from tab_warn_column order by sequence";

	/**
	 * 初始化数据库连接
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}

	/**
	 * 根据登陆用户获取该用户的告警规则
	 * 
	 * @param acc_loginname
	 *            用户名
	 * @return
	 */
	public List<Map> getWarnRuleByUser(String acc_loginname)
	{
		String getWarnRuleByUserSQL = this.getWarnRuleByUserSQL + "'"
				+ acc_loginname + "' order by rule_id";
		PrepareSQL psql = new PrepareSQL(getWarnRuleByUserSQL);
		psql.getSQL();
		List<Map> list = jt.query(getWarnRuleByUserSQL, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1)
					throws java.sql.SQLException
			{
				Map m = new HashMap();
				m.put("acc_loginname", rs.getString("acc_loginname"));
				m.put("rule_id", rs.getBigDecimal("rule_id").longValue());
				m.put("rule_name", rs.getString("rule_name"));
				m.put("createtime", StringUtils.formatDate(
						"yyyy-MM-dd HH:mm:ss", rs.getBigDecimal("createtime")
								.longValue()));
				m.put("maxnum", rs.getBigDecimal("maxnum").longValue());
				m.put("selected", rs.getBigDecimal("selected").intValue());
				m.put("ispublic", rs.getBigDecimal("ispublic").intValue());
				m.put("visible", rs.getBigDecimal("visible").intValue());
				return m;
			}
		});
		return list;
	}

	/**
	 * 获取除 <code>acc_loginname</code> 用户之外的共享模板
	 * 
	 * @param acc_loginname
	 *            用户名
	 * @return
	 */
	public List<Map> getShareWarnRuleExceptUser(String acc_loginname)
	{
		String getShareWarnRuleExceptUserSQL = this.getShareWarnRuleExceptUserSQL
				+ "'" + acc_loginname + "' order by rule_id";
		PrepareSQL psql = new PrepareSQL(getShareWarnRuleExceptUserSQL);
		psql.getSQL();
		List<Map> list = jt.query(getShareWarnRuleExceptUserSQL,
				new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1)
							throws java.sql.SQLException
					{
						Map m = new HashMap();
						m.put("acc_loginname", rs.getString("acc_loginname"));
						m.put("rule_id", rs.getBigDecimal("rule_id")
								.longValue());
						m.put("rule_name", rs.getString("rule_name"));
						m.put("createtime", StringUtils.formatDate(
								"yyyy-MM-dd HH:mm:ss", rs.getBigDecimal(
										"createtime").longValue()));
						m.put("maxnum", rs.getBigDecimal("maxnum").longValue());
						m.put("selected", rs.getBigDecimal("selected")
								.intValue());
						m.put("ispublic", rs.getBigDecimal("ispublic")
								.intValue());
						m
								.put("visible", rs.getBigDecimal("visible")
										.intValue());
						return m;
					}
				});
		return list;
	}

	/**
	 * 根据告警规则ID来显示详细告警规则
	 * 
	 * @param ruleId
	 *            规则ID
	 * @return
	 */
	public List<Map> getWarnRuleDetailByRuleId(long ruleId)
	{
		String getWarnRuleDetailByRuleIdSQL = "select rule_priority,rule_content,rule_invocation from tab_warn_filterdetail where rule_id="
				+ ruleId + " order by rule_priority";
		PrepareSQL psql = new PrepareSQL(getWarnRuleDetailByRuleIdSQL);
		psql.getSQL();
		List<Map> list = jt.query(getWarnRuleDetailByRuleIdSQL, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1)
					throws java.sql.SQLException
			{
				Map m = new HashMap();
				m.put("rule_priority", rs.getBigDecimal("rule_priority")
						.intValue());
				m.put("rule_content", rs.getString("rule_content"));
				m.put("rule_invocation", rs.getBigDecimal("rule_invocation")
						.intValue());
				return m;
			}
		});
		return list;
	}

	/**
	 * 获取实时告警牌列名称配置信息
	 * 
	 * @return
	 */
	public List<Map> getWarnColumn()
	{
		PrepareSQL psql = new PrepareSQL(getWarnColumnSQL);
		psql.getSQL();
		List<Map> list = jt.query(getWarnColumnSQL, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1)
					throws java.sql.SQLException
			{
				Map m = new HashMap();
				m.put("field_name", rs.getString("field_name"));
				m.put("field_desc", rs.getString("field_desc"));
				m.put("sequence", rs.getBigDecimal("sequence").intValue());
				m.put("visible", rs.getBigDecimal("visible").intValue());
				return m;
			}
		});
		return list;
	}

	/**
	 * 获取规则配置表中的最大规则ID
	 * 
	 * @return
	 */
	public long getMaxRuleId()
	{
		String getMaxRuleIdSQL = "select max(rule_id) from tab_warn_filterrule";
		PrepareSQL psql = new PrepareSQL(getMaxRuleIdSQL);
		psql.getSQL();
		long result = jt.queryForLong(getMaxRuleIdSQL);
		LOG.debug("queryForLong = " + result);
		return result;
	}

	/**
	 * 添加告警规则模板
	 * 
	 * @param acc_loginname
	 *            登陆用户名
	 * @param ruleId
	 *            规则ID
	 * @param ruleName
	 *            规则名称
	 * @param maxNum
	 *            最大告警数量
	 * @param selected
	 *            是否默认
	 * @param isPublic
	 *            是否共享
	 * @param visible
	 *            是否显示
	 * @return
	 */
	public boolean addRuleInfo(String acc_loginname, long ruleId,
			String ruleName, long maxNum, int selected, int isPublic,
			int visible)
	{
		boolean flag = false;
		if (selected == 1)
		{
			String resetSelectSQL = this.resetSelectedSQL + "'" + acc_loginname
					+ "'";
			PrepareSQL psql = new PrepareSQL(resetSelectSQL);
			psql.getSQL();
			jt.update(resetSelectSQL);
		}
		String addRuleInfoSQL = this.addRuleInfoSQL + "values('"
				+ acc_loginname + "'," + ruleId + ",'" + ruleName + "',"
				+ (System.currentTimeMillis() / 1000) + "," + maxNum + ","
				+ selected + "," + isPublic + "," + visible + ")";
		PrepareSQL psql = new PrepareSQL(addRuleInfoSQL);
		psql.getSQL();
		int addNum = 0;
		addNum = jt.update(addRuleInfoSQL);
		flag = (addNum > 0 ? true : false);
		if (flag)
			addDefaultRuleDetailInfo(ruleId);
		return flag;
	}

	/**
	 * 在创建一个新的模板的时候添加默认的规则详情
	 * 
	 * @return
	 */
	private boolean addDefaultRuleDetailInfo(long ruleId)
	{
		String addDefaultRuleDetailInfoSQL = addRuleDetailInfoSQL + "values("
				+ ruleId + ",0,'"
				+ ConstantEventEnv.DefaultLevelFilter.toString() + "',1)";
		PrepareSQL psql = new PrepareSQL(addDefaultRuleDetailInfoSQL);
		psql.getSQL();
		int addNum = 0;
		addNum = jt.update(addDefaultRuleDetailInfoSQL);
		return (addNum > 0 ? true : false);
	}

	/**
	 * 修改告警规则模板
	 * 
	 * @param acc_loginname
	 *            登陆用户名
	 * @param ruleId
	 *            规则ID
	 * @param ruleName
	 *            规则名称
	 * @param maxNum
	 *            最大告警数量
	 * @param selected
	 *            是否默认
	 * @param isPublic
	 *            是否共享
	 * @param visible
	 *            是否显示
	 * @return
	 */
	public boolean updateRuleInfo(String acc_loginname, long ruleId,
			String ruleName, long maxNum, int selected, int isPublic,
			int visible)
	{
		if (selected == 1)
		{
			String resetSelectSQL = this.resetSelectedSQL + "'" + acc_loginname
					+ "'";
			PrepareSQL psql = new PrepareSQL(resetSelectSQL);
			psql.getSQL();
			jt.update(resetSelectSQL);
		}
		String updateRuleInfoSQL = "update tab_warn_filterrule set rule_name='"
				+ ruleName + "',maxnum=" + maxNum + ",selected=" + selected
				+ ",ispublic=" + isPublic + ",visible=" + visible
				+ " where rule_id=" + ruleId;
		PrepareSQL psql = new PrepareSQL(updateRuleInfoSQL);
		psql.getSQL();
		int updateNum = 0;
		updateNum = jt.update(updateRuleInfoSQL);
		return (updateNum > 0 ? true : false);
	}

	/**
	 * 根据告警规则ID来删除当前告警规则
	 * 
	 * @param ruleId
	 *            规则ID
	 * @return
	 */
	public boolean delRuleInfo(long ruleId)
	{
		String delRuleInfoSQL = this.delRuleInfoSQL + ruleId;
		String delDetailSQL = this.delRuleDetailInfoSQL + ruleId;
		PrepareSQL psql = new PrepareSQL(delRuleInfoSQL);
		psql.getSQL();
		psql = new PrepareSQL(delDetailSQL);
		psql.getSQL();
		int delNum = 0;
		delNum = jt.update(delRuleInfoSQL);
		jt.update(delDetailSQL);
		return (delNum > 0 ? true : false);
	}

	/**
	 * COPY 其他用户的规则模板<br>
	 * COPY过来的数据具有如下特征:
	 * <li>非默认模板,
	 * <li>不共享,
	 * <li>创建时间为COPY的时间
	 * 
	 * @param acc_loginname
	 *            用户名
	 * @param fromRuleId
	 *            来自那个规则ID
	 * @param toRuleId
	 *            复制到那个规则ID
	 * @return
	 */
	public boolean copyRuleInfo(String acc_loginname, long fromRuleId,
			long toRuleId)
	{
		String[] bacthSQL = new String[2];
		// 0:unknown(default) 1:SYBASE 2:ORACEL
		int dbType = StringUtils.getDB();
		LOG.debug("DataBase Type = " + dbType);

		// oracle
		if(Global.DB_ORACLE == DBUtil.GetDB()){
			LOG.debug("ORACEL");
			bacthSQL[0] = this.addRuleInfoSQL
					+ "select '"
					+ acc_loginname
					+ "',"
					+ toRuleId
					+ ",rule_name || '[复制 ' || acc_loginname || ']',"
					+ (System.currentTimeMillis() / 1000)
					+ ",maxnum,0,0,visible from tab_warn_filterrule where rule_id="
					+ fromRuleId;
		}
		// sysbase
		else if (Global.DB_SYBASE == DBUtil.GetDB()) {
			LOG.debug("SYBASE");
			bacthSQL[0] = this.addRuleInfoSQL
					+ "select '"
					+ acc_loginname
					+ "',"
					+ toRuleId
					+ ",rule_name+'[复制 '+acc_loginname+']',"
					+ (System.currentTimeMillis() / 1000)
					+ ",maxnum,0,0,visible from tab_warn_filterrule where rule_id="
					+ fromRuleId;
		}
		// teledb
		else if (DBUtil.GetDB() == Global.DB_MYSQL) {
			LOG.debug("MYSQL");
			bacthSQL[0] = this.addRuleInfoSQL
					+ "select '"
					+ acc_loginname
					+ "',"
					+ toRuleId
					+ ", concat(rule_name, '[复制 ', acc_loginname, ']') ,"
					+ (System.currentTimeMillis() / 1000)
					+ ",maxnum,0,0,visible from tab_warn_filterrule where rule_id="
					+ fromRuleId;
		}

		bacthSQL[1] = this.addRuleDetailInfoSQL
				+ "select "
				+ toRuleId
				+ ",rule_priority,rule_content,rule_invocation from tab_warn_filterdetail where rule_id="
				+ fromRuleId;
		PrepareSQL psql = new PrepareSQL(bacthSQL[0]);
		psql.getSQL();
		psql = new PrepareSQL(bacthSQL[1]);
		psql.getSQL();
		int[] copyNum = jt.batchUpdate(bacthSQL);
		if (copyNum[0] == 1)
			return true;
		else
			return false;
	}

	/**
	 * 将告警详细规则保存入库
	 * 
	 * @param ruleId
	 *            规则ID
	 * @param priorityArray
	 *            优先级
	 * @param contentArray
	 *            规则内容
	 * @param invocationArray
	 *            是否启用(1:启用 , 0:禁用)
	 * @param ruleLength
	 *            规则数量
	 * @return
	 */
	public boolean saveRuleDetailInfo(long ruleId, String[] priorityArray,
			String[] contentArray, String[] invocationArray, int ruleLength)
	{
		String[] bacthSQL = new String[ruleLength + 1];
		bacthSQL[0] = this.delRuleDetailInfoSQL + ruleId;
		PrepareSQL psql = new PrepareSQL(bacthSQL[0]);
		psql.getSQL();
		String sql = this.addRuleDetailInfoSQL + " values(";
		for (int i = 0; i < ruleLength; i++)
		{
			bacthSQL[i + 1] = sql + ruleId + "," + priorityArray[i] + ",'"
					+ contentArray[i] + "'," + invocationArray[i] + ")";
			psql = new PrepareSQL(bacthSQL[i + 1]);
			psql.getSQL();
		}
		int[] saveNum = jt.batchUpdate(bacthSQL);
		int len = saveNum.length;
		for (int i = 1; i < len; i++)
		{
			if (saveNum[i] != 1)
				return false;
		}

		return true;
	}

	/**
	 * 保存实时告警牌的列配置信息到数据库
	 * 
	 * @param fieldName
	 *            列名称
	 * @param visible
	 *            是否显示
	 * @param sequence
	 *            展示顺序
	 * @return
	 */
	public boolean saveWarnColumnInfo(String[] fieldName, String[] visible,
			String[] sequence)
	{
		int len = fieldName.length;
		String[] bacthSQL = new String[len];
		for (int i = 0; i < len; i++)
		{
			bacthSQL[i] = "update tab_warn_column set sequence=" + sequence[i]
					+ ",visible=" + visible[i] + " where field_name='"
					+ fieldName[i] + "'";
			PrepareSQL psql = new PrepareSQL(bacthSQL[i]);
			psql.getSQL();
		}
		int[] updateNum = jt.batchUpdate(bacthSQL);
		int updateLen = updateNum.length;
		for (int i = 1; i < updateLen; i++)
		{
			if (updateNum[i] != 1)
				return false;
		}
		return true;
	}

}
