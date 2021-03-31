package com.linkage.module.gtms.diagnostic.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DbUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;
import scala.annotation.meta.param;

public class DiagTemlateDaoImpl extends SuperDAO implements DiagTemlateDao {
	private static Logger logger = LoggerFactory
	   .getLogger(DiagTemlateDaoImpl.class);

	public int add(String accOid, String templateName, String templateParam) {
		logger.debug("add()");

		long nowTime = new Date().getTime()/1000;
		int diagId = getMaxId();
		int acc_oid = StringUtil.getIntegerValue(accOid);
		PrepareSQL psql = new PrepareSQL();
		psql.append("insert into  diag_template values(?,?,?,?,?)");
		psql.setInt(1, diagId);
		psql.setString(2,templateName);
		psql.setString(3, templateParam);
		psql.setLong(4, acc_oid);
		psql.setLong(5, nowTime);

		return jt.update(psql.getSQL());
	}

	public int delete(String diagId) {

		logger.debug("delete({})",diagId);

		PrepareSQL psql = new PrepareSQL();
		psql.append("delete from   diag_template  where id = "+diagId);

		return jt.update(psql.getSQL());
	}


	public List<Map<String, Object>> getAllRecords() {
		logger.debug("getAllRecords()");
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.id, a.template_name, a.template_param, a.template_time, b.acc_loginname  from   diag_template a, tab_accounts b  ");
		psql.append(" where a.acc_oid = b.acc_oid ");
		return jt.queryForList(psql.getSQL());
	}

	public int getDiaTemplateById(String diagId) {
		logger.debug("getDiaTemplateById({})",diagId);
		PrepareSQL psql = new PrepareSQL();
		psql.append("select  count(*)  from   diag_template  where id="+diagId);
		return jt.queryForInt(psql.getSQL());
	}

	public Map<String, Object> getDiagTemplate(String diagId) {
		logger.debug("getDiagTemplate({})",diagId);
		Map<String,Object> map = new HashMap<String,Object>();
		List lt = null;
		PrepareSQL psql = new PrepareSQL();
		psql.append("select id,  template_name,template_param from diag_template  where id="+diagId);
		lt = jt.queryForList(psql.getSQL());
		if(null != lt && lt.size()>0){
			map = (Map<String, Object>) lt.get(0);
		}
		return map;
	}

	public List<Map<String, Object>> getTemplateUnits() {
		logger.debug("getTemplateUnits({})");

		PrepareSQL psql = new PrepareSQL();
		psql.append("select id, unit_name from  diag_template_unit  ");

		return jt.queryForList(psql.getSQL());
	}

	public int update(String diagId, String templateName, String templateParam) {
		logger.debug("update({},{},{})",new Object[]{diagId,templateName,templateParam});

		long nowTime = new Date().getTime()/1000;

		PrepareSQL psql = new PrepareSQL();
		psql.append(" update diag_template  set template_name = ?,template_param=?,template_time =?");
		psql.append(" where id="+diagId);
		psql.setString(1, templateName);
		psql.setString(2, templateParam);
		psql.setLong(3, nowTime);

		return jt.update(psql.getSQL());
	}
	/**
	 * 获取最大ID.
	 *
	 * @return
	 * <li>-1:失败</li>
	 * <li>other:最新ID，已经加1</li>
	 */
	public int getMaxId() {
		logger.debug("getMaxId()");

		String sql = "select max(id) from diag_template";
		PrepareSQL psql = new PrepareSQL(sql);
		int id = jt.queryForInt(psql.getSQL());

		if (id == 0) {
			return -1;
		}
		return id + 1;
	}

	public List<Map<String, Object>> getUintListByIds(String param)
	{
		logger.debug("getUintListByIds()");
		PrepareSQL psql = new PrepareSQL();
		psql.append(" select id,unit_url from diag_template_unit where id in("+param+")");

		return jt.queryForList(psql.getSQL());
	}
}
