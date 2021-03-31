package com.linkage.module.gtms.config.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DbUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

public class ControlTimeOfUserDaoImpl extends SuperDAO implements
		ControlTimeOfUserDao {

	private static Logger logger = LoggerFactory
	.getLogger(ControlTimeOfUserDaoImpl.class);
	/**
	 * 增加版本控制用户受理时间
	 * @return int
	 */
	public int addRecord (String typeId,String conTime1){
		logger.debug("addRecord({},{})",new Object[]{typeId,conTime1});
		int cuid =  getMaxCuId();
		int type_id = StringUtil.getIntegerValue(typeId);
		int contime = StringUtil.getIntegerValue(conTime1);

		PrepareSQL sb = new PrepareSQL();
		sb.append(" insert into tab_config_time values(?,?,?)" );
		sb.setInt(1, cuid);
		sb.setInt(2, type_id);
		sb.setInt(3, contime);
		return jt.update(sb.getSQL());
	}
	/**
	 * 更新一条记录
	 * (non-Javadoc)
	 * @return int 是否更新成功
	 */
	public int updateRecord (String cuId,String typeId,String conTime1){
		logger.debug("updateRecord({},{},{})",new Object[]{cuId,typeId,conTime1});
		int type_id = StringUtil.getIntegerValue(typeId);
		int contime = StringUtil.getIntegerValue(conTime1);

		PrepareSQL sb = new PrepareSQL();
		sb.append(" update tab_config_time set type_id = ?,con_time=? where cuid = "+cuId );
		sb.setInt(1, type_id);
		sb.setInt(2, contime);
		return jt.update(sb.getSQL());
	}
	/**
	 * 删除一条记录
	 * (non-Javadoc)
	 * @return int 是否删除成功
	 */
	public int deleteRecord(String cuId){
		logger.debug("deleteRecord({})",cuId);
		PrepareSQL sb = new PrepareSQL();
		sb.append(" delete  from  tab_config_time  where cuid = "+cuId );
		return jt.update(sb.getSQL());
	}
	/**
	 * 根据时间查询记录
	 * @reutrn list
	 */
	public List<Map<String,Object>> getAllRecord(String typeId,
			String conTimeStart,String conTimeEnd){
		logger.debug("getAllRecord({},{},{})",new Object[]{typeId,conTimeStart,conTimeEnd});
		StringBuffer sb = new StringBuffer();
		sb.append(" select con_time, type_id from tab_config_time where 1=1");
		if(!StringUtil.IsEmpty(typeId)){
			sb.append(" and type_id = ");
			sb.append(typeId);
		}
		if(!StringUtil.IsEmpty(conTimeStart)){
			sb.append(" and con_time >=");
			sb.append(conTimeStart);
		}
		if(!StringUtil.IsEmpty(conTimeEnd)){
			sb.append(" and con_time <=");
			sb.append(conTimeEnd);
		}
		PrepareSQL psb = new PrepareSQL(sb.toString());
		return jt.queryForList(psb.getSQL());
	}

	/**
	 *根据id查询记录
	 *@reutrn
	 *    list
	 */
	public List getRecordById(int cuId){
		logger.debug("getRecordById({})",cuId);
		PrepareSQL sb = new PrepareSQL();
		sb.append(" select type_id, con_time, cuid from tab_config_time where cuid = "+cuId);
		return jt.queryForList(sb.getSQL());
	}
	/**
	 * 获取最大ID.
	 *
	 * @return
	 * <li>-1:失败</li>
	 * <li>other:最新ID，已经加1</li>
	 */
	public int getMaxCuId() {
		logger.debug("getMaxTmplId()");

		String sql = "select " + DbUtil.getNullFunction("max(cuid)", "0")
		+ DbUtil.getContactIdentifier() + " 1 as id from tab_config_time";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map<String,Object>> list = jt.queryForList(psql.getSQL());

		if (list == null) {
			return -1;
		}

		Map map = (Map) list.get(0);

		return StringUtil.getIntValue(map, "id", -1);
	}
}
