/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.config.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.config.obj.QosTmplLinkOBJ;
import com.linkage.module.gwms.config.obj.QosTmplLinkTypeOBJ;
import com.linkage.module.gwms.config.obj.QosTmplOBJ;
import com.linkage.module.gwms.util.StringUtil;

/**
 * DAO: qos_tmpl.
 * qos_tmpl,qos_tmpl_sub,qos_tmpl_sub_type,qos_tmpl_link,qos_tmpl_link_type
 * 
 * @author alex(yanhj@lianchuang.com)
 * @date 2009-7-24
 */
//TODO wait
//无引用的类
public class QosTmplDAO {
	private static Logger logger = LoggerFactory.getLogger(QosTmplDAO.class);

	/** spring的jdbc模版类 */
	public JdbcTemplate jt;

	/**
	 * setDao 注入
	 */
	public void setDao(DataSource dao) {
		logger.debug("setDao({})", dao);

		jt = new JdbcTemplate(dao);
	}

	/**
	 * 获取自定义模板
	 * 
	 * @return
	 */
	public List getQosTmplSelf() 
	{
		logger.debug("getQosTmplSelf()");
		List rList = null;
		String strSQL = "select * from qos_tmpl where tmpl_id=1";
		PrepareSQL psql = new PrepareSQL(strSQL);

		rList = jt.queryForList(psql.getSQL());

		return rList;
	}

	/**
	 * 获取指定模板
	 * 
	 * @param tmplId
	 *            模板ID
	 * @return
	 */
	public Map getQosTmpl(int tmplId) 
	{
		logger.debug("getQosTmpl({})", tmplId);
		List rList = null;
		String strSQL = "select * from qos_tmpl where tmpl_id=?";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setInt(1, tmplId);
		rList = jt.queryForList(psql.getSQL());

		if (rList == null) {
			return null;
		}

		return (Map) rList.get(0);
	}

	/**
	 * 获取类型列表
	 * 
	 * @return
	 */
	public List getQosTmplSubType() {
		logger.debug("getQosTmplSubType()");

		List rList = null;
		String strSQL = "select * from qos_tmpl_sub_type";
		PrepareSQL psql = new PrepareSQL(strSQL);
		rList = jt.queryForList(psql.getSQL());

		return rList;
	}

	/**
	 * 获取指定类型
	 * 
	 * @param typeId
	 *            类型ID
	 * @return
	 */
	public Map getQosTmplSubType(int typeId) {
		logger.debug("getQosTmplSubType({})", typeId);

		List rList = null;
		String strSQL = "select * from qos_tmpl_sub_type where type_id=?";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setInt(1, typeId);
		rList = jt.queryForList(psql.getSQL());

		if (rList == null) {
			return null;
		}

		return (Map) rList.get(0);
	}

	/**
	 * 获取模板的子模板
	 * 
	 * @param tmplId
	 *            模板ID
	 * @return
	 */
	public List getQosTmplLink(int tmplId) {
		logger.debug("getQosTmplLink({})", tmplId);

		List rList = null;
		String strSQL = "select * from qos_tmpl_link where tmpl_id=?";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setInt(1, tmplId);
		rList = jt.queryForList(psql.getSQL());

		return rList;
	}

	/**
	 * 获取指定模板的类型
	 * 
	 * @param tmplId
	 *            模板ID
	 * @param subOrder
	 *            子模板顺序
	 * @return
	 */
	public List getQosTmplLinkType(int tmplId, int subOrder) {
		logger.debug("getQosTmplLinkType({},{})", tmplId, subOrder);

		List rList = null;
		String strSQL = "select * from qos_tmpl where tmpl_id=? and sub_order=?";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setInt(1, tmplId);
		psql.setInt(2, subOrder);
		rList = jt.queryForList(psql.getSQL());

		return rList;
	}

	/**
	 * 获取模板最大ID.
	 * 
	 * @return
	 * <li>-1:失败</li>
	 * <li>other:模板最新ID，已经加1</li>
	 */
	public int getMaxTmplId() {
		logger.debug("getMaxTmplId()");

		String sql = "select " + com.linkage.litms.common.util.DbUtil.getNullFunction("max(tmpl_id)", "0") + com.linkage.litms.common.util.DbUtil.getContactIdentifier() + "1 as id from qos_tmpl";

		List list = jt.queryForList(sql);

		if (list == null) {
			return -1;
		}

		Map map = (Map) list.get(0);

		return StringUtil.getIntValue(map, "id", -1);
	}

	/**
	 * add qos_tmpl
	 * 
	 * @return
	 */
	public String addQosTmpl(QosTmplOBJ obj) {
		logger.debug("addQosTmpl({})", obj);

		if (obj == null) {
			logger.debug("obj == nul");

			return null;
		}

		String sql = "insert into qos_tmpl "
				+ "(tmpl_id,tmpl_name,tmpl_time,tmpl_type"
				+ ",tmpl_mode,tmpl_enab,bandwidth,tmpl_plan,enab_force_weight"
				+ ",enab_dscp,enab_802,acc_oid,tmpl_desc" + ") values ("
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?)";

		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, obj.getTmplId());
		psql.setStringExt(2, obj.getTmplName(), true);
		psql.setString(3, obj.getTmplTime());
		psql.setString(4, obj.getTmplType());
		psql.setStringExt(5, obj.getTmplMode(), true);
		psql.setString(6, obj.getTmplEnab());
		psql.setString(7, obj.getBandwidth());
		psql.setString(8, obj.getTmplPlan());
		psql.setString(9, obj.getEnabForceWeight());
		psql.setString(10, obj.getEnabDscp());
		psql.setString(11, obj.getEnab802());
		psql.setString(12, obj.getAccOid());
		psql.setStringExt(13, obj.getTmplDesc(), true);

		return psql.getSQL();
	}

	/**
	 * add qos_tmpl_link
	 * 
	 * @return
	 */
	public String addQosTmplLink(QosTmplLinkOBJ obj) {
		logger.debug("addQosTmplLink({})", obj);

		if (obj == null) {
			logger.debug("obj == nul");

			return null;
		}

		String sql = "insert into qos_tmpl_link "
				+ "(tmpl_id,sub_order,sub_id,queue_id"
				+ ",app_name,class_dscp,class_802" + ") values ("
				+ "?,?,?,?,?,?,?)";

		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, obj.getTmplId());
		psql.setStringExt(2, obj.getSubOrder(), false);
		psql.setString(3, obj.getSubId());
		psql.setString(4, obj.getQueueId());
		psql.setStringExt(5, obj.getAppName(), true);
		psql.setString(6, obj.getClassDscp());
		psql.setString(7, obj.getClass802());

		return psql.getSQL();
	}

	/**
	 * add qos_tmpl_link_type
	 * 
	 * @return
	 */
	public String addQosTmplLinkType(QosTmplLinkTypeOBJ obj) {
		logger.debug("addQosTmplLinkType({})", obj);

		if (obj == null) {
			logger.debug("obj == nul");

			return null;
		}

		String sql = "insert into qos_tmpl_link_type "
				+ "(tmpl_id,sub_order,type_order,type_id"
				+ ",type_name,type_max,type_min,type_prot" + ") values ("
				+ "?,?,?,?,?,?,?,?)";

		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, obj.getTmplId());
		psql.setStringExt(2, obj.getSubOrder(), false);
		psql.setString(3, obj.getTypeOrder());
		psql.setString(4, obj.getTypeId());
		psql.setStringExt(5, obj.getTypeName(), true);
		psql.setStringExt(6, obj.getTypeMax(), true);
		psql.setStringExt(7, obj.getTypeMin(), true);
		psql.setStringExt(8, obj.getTypeProt(), true);

		return psql.getSQL();
	}

}
