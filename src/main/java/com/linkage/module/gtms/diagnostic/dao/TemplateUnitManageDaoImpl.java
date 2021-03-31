package com.linkage.module.gtms.diagnostic.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DbUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

public class TemplateUnitManageDaoImpl extends SuperDAO implements
		TemplateUnitManageDao {
	private static Logger logger = LoggerFactory.getLogger(TemplateUnitManageDaoImpl.class);

	/**
	 * 返回所有记录
	 */
	public List<Map<String,Object>> getAllRecords(){
		logger.debug("getAllRecords()");
		PrepareSQL psql = new PrepareSQL();
		psql.append(" select id, unit_url, unit_name, unit_time from diag_template_unit ");
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 判断记录是否存在
	 */
	public int getRecordById(int unitId){
		logger.debug("getRecordById({})",unitId);
		PrepareSQL psql = new PrepareSQL();
		psql.append(" select count(*) from diag_template_unit where id = "+unitId);
		return jt.queryForInt(psql.getSQL());
	}


	public int add(String templateUnitName, String templateUnitURL) {
		logger.debug("add({},{},)",templateUnitName,templateUnitURL);
		long nowTime = new Date().getTime()/1000;
		int id = getMaxId();
		int ires = -1;
		PrepareSQL psql = new PrepareSQL();
		psql.append(" insert into diag_template_unit  values(?,?,?,?) ");
		psql.setInt(1, id);
		psql.setString(2,templateUnitURL);
		psql.setString(3,templateUnitName);
		psql.setLong(4, nowTime);
		ires = jt.update(psql.getSQL());
		return ires ;
	}

	public int delete(String unitId) {
		logger.debug("delete({})",unitId);
		int id = StringUtil.getIntegerValue(unitId);
		PrepareSQL psql = new PrepareSQL();
		psql.append(" delete from   diag_template_unit  where id= "+unitId);
		return jt.update(psql.getSQL());
	}

	public int update(String unitId,String templateUnitName, String templateUnitURL) {
		logger.debug("update({},{},{})",new Object[]{templateUnitName,templateUnitURL});
		long nowTime = new Date().getTime()/1000;
		int id = StringUtil.getIntegerValue(unitId);
		PrepareSQL psql = new PrepareSQL();
		psql.append(" update  diag_template_unit  set unit_url=? ,unit_name=? ,unit_time=? ");
		psql.append(" where id=? ");
		psql.setString(1, templateUnitURL);
		psql.setString(2,templateUnitName);
		psql.setLong(3,nowTime);
		psql.setInt(4, id);
		return jt.update(psql.getSQL());
	}

	public Map<String, Object> getpreUpdateRecord(String unitId){
		logger.debug("getRecordById({})",unitId);
		int id = StringUtil.getIntegerValue(unitId);
		List lt = null;
		PrepareSQL psql = new PrepareSQL();
		psql.append(" select id, unit_name, unit_url from diag_template_unit where id = "+id);
		lt = jt.queryForList(psql.getSQL());
		if(null != lt && lt.size()>0){
		 	return   (Map<String, Object>) lt.get(0);
		}
		return null;
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
}
