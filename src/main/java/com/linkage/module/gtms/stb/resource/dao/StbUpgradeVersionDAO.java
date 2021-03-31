package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.utils.DeviceTypeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;

/**
 *
 * @author wangyan10(Ailk NO.76091)
 * @version 1.0
 * @since 2018-1-2
 */
public class StbUpgradeVersionDAO extends SuperDAO
{

	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(StbUpgradeVersionDAO.class);
	private int queryCount;

	/**
	 * 查询版本映射关系列表
	 * @param
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param vendorId
	 * @param deviceModelId
	 * @param source_devicetypeId
	 * @param goal_devicetypeId
	 * @param upgradeType
	 * @param belong
	 * @param hardwareversion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getStbUpgradeTempList(
			int curPage_splitPage, int num_splitPage, String vendorId,
			String deviceModelId, String source_devicetypeId,
			String goal_devicetypeId, String upgradeType, String belong, String hardwareversion) {
		StringBuffer sql = new StringBuffer();
//		Map<String, String> platformMap = new HashMap<String, String>();
//		if(LipossGlobals.inArea("hb_lt")){
//			platformMap = queryPlatformMap();
//		}
		sql.append("select a.temp_id, a.devicetype_id_old, a.devicetype_id_new, a.belong, ");
		if(LipossGlobals.inArea(Global.HNLT)){
			sql.append("a.valid, ");
		}
		sql.append(" b.vendor_id,b.device_model_id,b.hardwareversion " +
				"from stb_gw_soft_upgrade_temp_map a,stb_tab_devicetype_info b where a.devicetype_id_old = b.devicetype_id ");
		if ((!StringUtil.IsEmpty(vendorId)) && (!"-1".equals(vendorId)))
		{
			sql.append(" and b.vendor_id='").append(vendorId).append("'");
		}
		if ((!StringUtil.IsEmpty(deviceModelId)) && (!"-1".equals(deviceModelId)))
		{
			sql.append(" and b.device_model_id='").append(deviceModelId).append("'");
		}
		if ((!StringUtil.IsEmpty(source_devicetypeId)) && (!"-1".equals(source_devicetypeId)))
		{
			sql.append(" and b.devicetype_id=").append(source_devicetypeId);
		}
		if ((!StringUtil.IsEmpty(goal_devicetypeId)) && (!"-1".equals(goal_devicetypeId)))
		{
			sql.append(" and a.devicetype_id_new=").append(goal_devicetypeId);
		}
		if ((!StringUtil.IsEmpty(hardwareversion)) && (!"-1".equals(hardwareversion)))
		{
			sql.append(" and b.hardwareversion ='").append(hardwareversion).append("'");
		}
		if ((!StringUtil.IsEmpty(upgradeType)) && (!"-1".equals(upgradeType)))
		{
			sql.append(" and a.temp_id=").append(upgradeType);
		}
		if ((!StringUtil.IsEmpty(belong)) && (!"-1".equals(belong)))
		{
			sql.append(" and a.belong=").append(belong);
		}

		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						String tempId = rs.getString("temp_id");
						map.put("vendorName", DeviceTypeUtil.getVendorName(StringUtil
								.getStringValue(rs.getString("vendor_id"))));
						map.put("deviceModel", DeviceTypeUtil.getDeviceModel(StringUtil
								.getStringValue(rs.getString("device_model_id"))));
						map.put("hardwareversion", rs.getString("hardwareversion"));
						map.put("devicetype_id_old", rs.getString("devicetype_id_old"));
						map.put("devicetype_id_new", rs.getString("devicetype_id_new"));
						map.put("device_model_id", rs.getString("device_model_id"));
						map.put("sourceDeviceTypeName", DeviceTypeUtil
								.getDeviceSoftVersion(StringUtil.getStringValue(rs
										.getString("devicetype_id_old"))));
						map.put("goalDeviceTypeName", DeviceTypeUtil
								.getDeviceSoftVersion(StringUtil.getStringValue(rs
										.getString("devicetype_id_new"))));
						if("1".equals(tempId)){
							map.put("tempName", "普通软件升级");
						}else if("2".equals(tempId)){
							map.put("tempName", "业务相关软件升级");
						}else{
							map.put("tempName", "非业务相关软件升级");
						}
						map.put("belongName", rs.getString("belong"));
						if(LipossGlobals.inArea(Global.HNLT)){
							String valid = StringUtil.getStringValue(rs.getString("valid"));
							if("1".equals(valid)){
								map.put("valid", "生效");
							}else if("0".equals(valid)){
								map.put("valid", "失效");
							}
							map.put("belongName", queryPlatformMap().get(rs.getString("belong")));
						}
						return map;
					}
				});
		return list;
	}


	public List<Map<String, String>> getStbUpgradeTempList_hnlt(int curPage_splitPage,
		int num_splitPage, String vendorId,String deviceModelId, String source_devicetypeId,
		String goal_devicetypeId,String upgradeType,String belong,String hardwareversion,String valid)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.temp_id, a.devicetype_id_old, a.devicetype_id_new, a.belong, ");
		if(LipossGlobals.inArea(Global.HNLT)){
			psql.append("a.valid, ");
		}
		psql.append("b.vendor_id,b.device_model_id,b.hardwareversion ");
		psql.append("from stb_gw_soft_upgrade_temp_map a,stb_tab_devicetype_info b ");
		psql.append("where a.devicetype_id_old=b.devicetype_id");

		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			psql.append(" and b.vendor_id='"+vendorId+"'");
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			psql.append(" and b.device_model_id='"+deviceModelId+"'");
		}
		if (!StringUtil.IsEmpty(source_devicetypeId) && !"-1".equals(source_devicetypeId)){
			psql.append(" and b.devicetype_id="+source_devicetypeId);
		}
		if (!StringUtil.IsEmpty(hardwareversion) && !"-1".equals(hardwareversion)){
			psql.append(" and b.hardwareversion='"+hardwareversion+"'");
		}
		if (!StringUtil.IsEmpty(upgradeType) && !"-1".equals(upgradeType)){
			psql.append(" and a.temp_id="+upgradeType);
		}
		if (!StringUtil.IsEmpty(belong) && !"-1".equals(belong)){
			psql.append(" and a.belong='"+belong+"' ");
		}
		if (!StringUtil.IsEmpty(valid) && !"-1".equals(valid)){
			psql.append(" and a.valid="+valid+" ");
		}

		List list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						String tempId = rs.getString("temp_id");
						map.put("vendorName", DeviceTypeUtil.getVendorName(StringUtil
								.getStringValue(rs.getString("vendor_id"))));
						map.put("deviceModel", DeviceTypeUtil.getDeviceModel(StringUtil
								.getStringValue(rs.getString("device_model_id"))));
						map.put("hardwareversion", rs.getString("hardwareversion"));
						map.put("devicetype_id_old", rs.getString("devicetype_id_old"));
						map.put("devicetype_id_new", rs.getString("devicetype_id_new"));
						map.put("device_model_id", rs.getString("device_model_id"));
						map.put("sourceDeviceTypeName", DeviceTypeUtil
								.getDeviceSoftVersion(StringUtil.getStringValue(rs
										.getString("devicetype_id_old"))));
						map.put("goalDeviceTypeName", DeviceTypeUtil
								.getDeviceSoftVersion(StringUtil.getStringValue(rs
										.getString("devicetype_id_new"))));
						if("1".equals(tempId)){
							map.put("tempName", "普通软件升级");
						}else if("2".equals(tempId)){
							map.put("tempName", "业务相关软件升级");
						}else{
							map.put("tempName", "非业务相关软件升级");
						}
						map.put("temp_id",tempId);
						map.put("belongName", rs.getString("belong"));
						map.put("belong", rs.getString("belong"));
						if(LipossGlobals.inArea(Global.HNLT)){
							String valid = StringUtil.getStringValue(rs.getString("valid"));
							if("1".equals(valid)){
								map.put("valid", "生效");
							}else if("0".equals(valid)){
								map.put("valid", "失效");
							}
							map.put("belongName", queryPlatformMap().get(rs.getString("belong")));
						}
						return map;
					}
				});
		return list;
	}




	/**
	 * 获取总数并分页
	 * @param
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param vendorId
	 * @param deviceModelId
	 * @param source_devicetypeId
	 * @param goal_devicetypeId
	 * @param upgradeType
	 * @param belong
	 * @param hardwareversion
	 * @return
	 */
	public int countAdverResultList(int curPage_splitPage, int num_splitPage,
			String vendorId, String deviceModelId, String source_devicetypeId,
			String goal_devicetypeId, String upgradeType, String belong, String hardwareversion) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from stb_gw_soft_upgrade_temp_map a,stb_tab_devicetype_info b where a.devicetype_id_old = b.devicetype_id "  );
		if ((!StringUtil.IsEmpty(vendorId)) && (!"-1".equals(vendorId)))
		{
			sql.append(" and b.vendor_id='").append(vendorId).append("'");
		}
		if ((!StringUtil.IsEmpty(deviceModelId)) && (!"-1".equals(deviceModelId)))
		{
			sql.append(" and b.device_model_id='").append(deviceModelId).append("'");
		}
		if ((!StringUtil.IsEmpty(hardwareversion)) && (!"-1".equals(hardwareversion)))
		{
			sql.append(" and b.hardwareversion ='").append(hardwareversion).append("'");
		}
		if ((!StringUtil.IsEmpty(source_devicetypeId)) && (!"-1".equals(source_devicetypeId)))
		{
			sql.append(" and b.devicetype_id=").append(source_devicetypeId);
		}
		if ((!StringUtil.IsEmpty(goal_devicetypeId)) && (!"-1".equals(goal_devicetypeId)))
		{
			sql.append(" and a.devicetype_id_new=").append(goal_devicetypeId);
		}
		if ((!StringUtil.IsEmpty(upgradeType)) && (!"-1".equals(upgradeType)))
		{
			sql.append(" and a.temp_id=").append(upgradeType);
		}
		if ((!StringUtil.IsEmpty(belong)) && (!"-1".equals(belong)))
		{
			sql.append(" and a.belong=").append(belong);
		}

		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		queryCount = total;
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public int countAdverResultList_hnlt(int curPage_splitPage, int num_splitPage,
		String vendorId, String deviceModelId, String source_devicetypeId,String goal_devicetypeId,
		String upgradeType, String belong, String hardwareversion,String valid)
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append("select count(*) from stb_gw_soft_upgrade_temp_map a,stb_tab_devicetype_info b ");
		sql.append("where a.devicetype_id_old=b.devicetype_id");

		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sql.append(" and b.vendor_id='"+vendorId+"'");
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sql.append(" and b.device_model_id='"+deviceModelId+"'");
		}
		if (!StringUtil.IsEmpty(hardwareversion) && !"-1".equals(hardwareversion)){
			sql.append(" and b.hardwareversion='"+hardwareversion+"'");
		}
		if (!StringUtil.IsEmpty(source_devicetypeId) && !"-1".equals(source_devicetypeId)){
			sql.append(" and b.devicetype_id="+source_devicetypeId);
		}
		if (!StringUtil.IsEmpty(upgradeType) && !"-1".equals(upgradeType)){
			sql.append(" and a.temp_id="+upgradeType);
		}
		if (!StringUtil.IsEmpty(belong) && !"-1".equals(belong)){
			sql.append(" and a.belong='"+belong+"'");
		}
		if (!StringUtil.IsEmpty(valid) && !"-1".equals(valid)){
			sql.append(" and a.valid="+valid);
		}

		int total = jt.queryForInt(sql.getSQL());
		queryCount = total;
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * 根据原版本删除映射关系
	 * @param
	 * @param source_devicetypeId
	 * @return
	 */
	public int deleteUpgradeTemp(String source_devicetypeId) {
		List<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = new PrepareSQL(
				"delete from stb_gw_soft_upgrade_temp_map where devicetype_id_old="
						+ StringUtil.getLongValue(source_devicetypeId));
		sqlList.add(psql.getSQL());

		String[] sqlArray = sqlList.toArray(new String[0]);

		return jt.batchUpdate(sqlArray).length;
	}

	public int deleteUpgradeTemp_hnlt(String belong,String devicetype_id_new,
		String devicetype_id_old,String temp_id)
	{
		List<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = new PrepareSQL();
		psql.append("delete from stb_gw_soft_upgrade_temp_map ");
		psql.append("where belong=? and devicetype_id_new=? ");
		psql.append("and devicetype_id_old=? and temp_id=? ");
		psql.setString(1,belong);
		psql.setInt(2,StringUtil.getIntegerValue(devicetype_id_new));
		psql.setInt(3,StringUtil.getIntegerValue(devicetype_id_old));
		psql.setInt(4,StringUtil.getIntegerValue(temp_id));

		sqlList.add(psql.getSQL());

		String[] sqlArray = sqlList.toArray(new String[0]);

		return jt.batchUpdate(sqlArray).length;
	}



	/**
	 *
	 * @param
	 * @param source_devicetypeId
	 * @param goal_devicetypeId
	 * @param platformId
	 * @return
	 */
	public int modifyUpgradeTemp(String source_devicetypeId, String goal_devicetypeId, String platformId) {
		PrepareSQL sql = new PrepareSQL(
				"update stb_gw_soft_upgrade_temp_map set devicetype_id_new=? where devicetype_id_old=?");
		sql.setLong(1, StringUtil.getLongValue(goal_devicetypeId));
		sql.setLong(2, StringUtil.getLongValue(source_devicetypeId));

		return jt.update(sql.getSQL());
	}

	public int modifyUpgradeTemp_hnlt(String source_devicetypeId, String goal_devicetypeId,
			String platformId,String valid)
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append("update stb_gw_soft_upgrade_temp_map ");
		sql.append("set devicetype_id_new=?,belong=?,valid=? ");
		sql.append("where devicetype_id_old=? ");
		sql.setLong(1, StringUtil.getLongValue(goal_devicetypeId));
		sql.setString(2, platformId);
		sql.setLong(3, StringUtil.getLongValue(valid));
		sql.setLong(4, StringUtil.getLongValue(source_devicetypeId));

		return jt.update(sql.getSQL());
	}

	/**
	 * 新增映射关系
	 * @param
	 * @param vendorId
	 * @param deviceModelId
	 * @param source_devicetypeId
	 * @param goal_devicetypeId
	 * @param tempId
	 * @param belong
	 * @return
	 */
	public int addUpgradeTemp(String vendorId, String deviceModelId,
			String source_devicetypeId, String goal_devicetypeId,
			String tempId, String belong) {
		List<String> sqlList = new ArrayList<String>();
		PrepareSQL sql = new PrepareSQL();
		sql.append("insert into stb_gw_soft_upgrade_temp_map(");
		sql.append("temp_id,devicetype_id_old,devicetype_id_new,belong) ");
		sql.append("values(?,?,?,?) ");
		sql.setLong(1, StringUtil.getIntegerValue(tempId));
		sql.setLong(2, StringUtil.getIntegerValue(source_devicetypeId));
		sql.setLong(3, StringUtil.getIntegerValue(goal_devicetypeId));
		sql.setString(4, StringUtil.IsEmpty(belong)?"0":belong);

		logger.info(sql.getSQL());
		sqlList.add(sql.getSQL());
		int[] result = doBatch(sqlList);
		if (result != null && result.length > 0) {
			logger.debug("策略入库：  成功");
			return 1;
		} else {
			logger.debug("策略入库：  失败");
			return 0;
		}
	}

	public int addUpgradeTemp_hnlt(String vendorId, String deviceModelId,
			String source_devicetypeId, String goal_devicetypeId,
			String tempId, String belong,String valid) {
		List<String> sqlList = new ArrayList<String>();
		PrepareSQL sql = new PrepareSQL();
		sql.append("insert into stb_gw_soft_upgrade_temp_map(");
		sql.append("temp_id,devicetype_id_old,devicetype_id_new,belong,valid) ");
		sql.append("values(?,?,?,?,?) ");
		sql.setLong(1, StringUtil.getIntegerValue(tempId));
		sql.setLong(2, StringUtil.getIntegerValue(source_devicetypeId));
		sql.setLong(3, StringUtil.getIntegerValue(goal_devicetypeId));
		sql.setString(4, StringUtil.IsEmpty(belong)?"0":belong);
		sql.setLong(5, StringUtil.getIntegerValue(valid));

		sqlList.add(sql.getSQL());
		int[] result = doBatch(sqlList);
		if (result != null && result.length > 0) {
			logger.debug("策略入库：  成功");
			return 1;
		} else {
			logger.debug("策略入库：  失败");
			return 0;
		}
	}

	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}

	public int getIsHave(String pathId,String goal_devicetypeId)
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append("select count(*) from stb_gw_filepath_devtype ");
		sql.append("where path_id=? and goal_devicetype_id=? ");
		sql.setLong(1, StringUtil.getIntegerValue(pathId));
		sql.setLong(2, StringUtil.getIntegerValue(goal_devicetypeId));

		return jt.queryForInt(sql.toString());
	}

	public int getIsHave(String pathId,String deviceModelId,String devicetypeId)
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append("select count(*) from stb_gw_filepath_devtype ");
		sql.append("where path_id=? and goal_devicetype_id=? and device_model_id=? ");
		sql.setLong(1, StringUtil.getIntegerValue(pathId));
		sql.setLong(2, StringUtil.getIntegerValue(devicetypeId));
		sql.setString(3, deviceModelId);

		return jt.queryForInt(sql.toString());
	}

	public int addUpgradeFilePath(String pathId, String vendorId,
			String deviceModelId, String goal_devicetypeId) {
		List<String> sqlList = new ArrayList<String>();
		PrepareSQL sql = new PrepareSQL(
				"insert into stb_gw_filepath_devtype(path_id,vendor_id,device_model_id,goal_devicetype_id) values(?,?,?,?)");
		sql.setLong(1, StringUtil.getIntegerValue(pathId));
		sql.setString(2, vendorId);
		sql.setString(3, deviceModelId);
		sql.setLong(4, StringUtil.getIntegerValue(goal_devicetypeId));
		sqlList.add(sql.getSQL());
		int[] result = doBatch(sqlList);
		if (result != null && result.length > 0) {
			logger.debug("策略入库：  成功");
			return 1;
		} else {
			logger.debug("策略入库：  失败");
			return 0;
		}
	}

	public int deleteUpgradeFilePath(String pathId, String deviceModelId,
			String goal_devicetypeId) {
		// TODO Auto-generated method stub
		List<String> sqlList = new ArrayList<String>();
		//sybase不允许隐式转换，数据库定义deviceModelId为varchar，PrepareSQL会给转成int传入sql，导致sql报错
		//此处进行处理
		PrepareSQL psql = new PrepareSQL();
		if (LipossGlobals.inArea(Global.JXDX) || LipossGlobals.inArea(Global.NXDX))
		{
			psql.append("delete from stb_gw_filepath_devtype where path_id="
					+ StringUtil.getLongValue(pathId) + " and device_model_id='"
					+ deviceModelId + "' and goal_devicetype_id = "
					+ StringUtil.getLongValue(goal_devicetypeId));
		}else
		{
			psql.append("delete from stb_gw_filepath_devtype where path_id="
					+ StringUtil.getLongValue(pathId) + " and device_model_id="
					+ deviceModelId + " and goal_devicetype_id = "
					+ StringUtil.getLongValue(goal_devicetypeId));
		}

		sqlList.add(psql.getSQL());

		String[] sqlArray = sqlList.toArray(new String[0]);

		return jt.batchUpdate(sqlArray).length;
	}

	public int deleteUpgradeFilePath(String pathId,String deviceModelId,
			String goal_devicetypeId,String vendorId)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("delete from stb_gw_filepath_devtype ");
		psql.append("where path_id=? and device_model_id=? ");
		psql.append("and goal_devicetype_id=? and vendor_id=? ");
		psql.setLong(1,StringUtil.getLongValue(pathId));
		psql.setString(2,deviceModelId);
		psql.setLong(3,StringUtil.getLongValue(goal_devicetypeId));
		psql.setString(4,vendorId);

		return jt.update(psql.getSQL());
	}

	public int modifyUpgradeFilePath(String pathId, String deviceModelId,
			String goal_devicetypeId, String pathIdNew)
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append("update stb_gw_filepath_devtype set path_id=? ");
		sql.append("where path_id=? and device_model_id=? and goal_devicetype_id=? ");
		sql.setLong(1, StringUtil.getLongValue(pathIdNew));
		sql.setLong(2, StringUtil.getLongValue(pathId));
		sql.setString(3, deviceModelId);
		sql.setLong(4, StringUtil.getLongValue(goal_devicetypeId));
		return jt.update(sql.getSQL());
	}

	public List<Map<String, String>> getStbUpgradeFilePathList(int curPage_splitPage, int num_splitPage,
			String vendorId,String deviceModelId, String goal_devicetypeId)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.vendor_id, a.device_model_id, a.path_id, a.goal_devicetype_id, b.version_path ");
		psql.append("from stb_gw_filepath_devtype a,stb_gw_version_file_path b ");
		psql.append("where a.path_id=b.id ");

		if ((!StringUtil.IsEmpty(vendorId)) && (!"-1".equals(vendorId))){
			psql.append("and a.vendor_id='"+vendorId+"' ");
		}

		if ((!StringUtil.IsEmpty(deviceModelId)) && (!"-1".equals(deviceModelId))){
			psql.append("and a.device_model_id='"+deviceModelId+"' ");
		}

		if ((!StringUtil.IsEmpty(goal_devicetypeId)) && (!"-1".equals(goal_devicetypeId))){
			psql.append("and a.goal_devicetype_id="+goal_devicetypeId+" ");
		}

		List list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("vendorName", DeviceTypeUtil.getVendorName(StringUtil
								.getStringValue(rs.getString("vendor_id"))));
						map.put("deviceModel", DeviceTypeUtil.getDeviceModel(StringUtil
								.getStringValue(rs.getString("device_model_id"))));
						map.put("device_model_id", rs.getString("device_model_id"));
						map.put("path_id", rs.getString("path_id"));
						map.put("version_path", rs.getString("version_path"));
						map.put("goal_devicetype_id", rs.getString("goal_devicetype_id"));
						map.put("goalDeviceTypeName", DeviceTypeUtil
								.getDeviceSoftVersion(StringUtil.getStringValue(rs
										.getString("goal_devicetype_id"))));
						return map;
					}
				});
		return list;
	}

	public int countStbUpgradeFilePathList(int curPage_splitPage,int num_splitPage, String vendorId,
			String deviceModelId,String goal_devicetypeId)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) ");
		psql.append("from stb_gw_filepath_devtype a,stb_gw_version_file_path b ");
		psql.append("where a.path_id=b.id ");

		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			psql.append("and a.vendor_id='"+vendorId+"' ");
		}

		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			psql.append("and a.device_model_id='"+deviceModelId+"' ");
		}

		if (!StringUtil.IsEmpty(goal_devicetypeId) && !"-1".equals(goal_devicetypeId)){
			psql.append("and a.goal_devicetype_id="+goal_devicetypeId+" ");
		}

		int total = jt.queryForInt(psql.getSQL());
		queryCount = total;
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public List getPathByModelId(String deviceModelId)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select a.id,a.version_path ");
		pSQL.append("from stb_gw_version_file_path a,stb_version_file_path_model b ");
		pSQL.append("where a.id=b.path_id ");

		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.append("and b.device_model_id='"+deviceModelId+"' ");
		}
		return jt.queryForList(pSQL.toString());
	}

	public int checkDeviceTypeId(String source_devicetypeId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from stb_gw_soft_upgrade_temp_map a where 1=1 "  );
			sql.append(" and a.devicetype_id_old=").append(source_devicetypeId);

		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForInt(psql.getSQL());
	}

	/**
	 * 查询平台类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> queryPlatformMap(){
		logger.debug("PreMacDeviceDAO ==> queryPlatformList()");

		String strSQL = "select platform_id, platform_name from stb_serv_platform";
		PrepareSQL psql = new PrepareSQL(strSQL);
		List<HashMap<String, String>> queryPlatformList = jt.queryForList(psql.getSQL());
		Map<String, String> queryPlatformMap = new HashMap<String, String>();
		for (HashMap<String, String> map : queryPlatformList)
		{
			queryPlatformMap.put(StringUtil.getStringValue(map.get("platform_id")),
					map.get("platform_name"));
		}
		return queryPlatformMap;
	}

	public List<Map<String, String>> getStbUpgradeFilePathList_hnlt(int curPage_splitPage, int num_splitPage,
		String goal_devicetypeId)
	{
		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
		psql.append("select a.vendor_id, a.device_model_id, a.hardwareversion, " +
				"b.path_id,b.goal_devicetype_id,c.version_path,c.dcn_path,c.special_path ");
		psql.append("from stb_tab_devicetype_info a,stb_gw_filepath_devtype b,stb_gw_version_file_path c ");
		psql.append("where a.devicetype_id=b.goal_devicetype_id and b.path_id=c.id ");
		psql.append("and c.valid=1 ");
		if(!StringUtil.IsEmpty(goal_devicetypeId) && !"-1".equals(goal_devicetypeId)){
			psql.append("and a.devicetype_id="+StringUtil.getIntegerValue(goal_devicetypeId));
		}

		List list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("vendor_id",StringUtil.getStringValue(rs.getString("vendor_id")));
						map.put("vendorName", DeviceTypeUtil.getVendorName(StringUtil
								.getStringValue(rs.getString("vendor_id"))));
						map.put("deviceModel", DeviceTypeUtil.getDeviceModel(StringUtil
								.getStringValue(rs.getString("device_model_id"))));
						map.put("device_model_id", rs.getString("device_model_id"));
						map.put("path_id", rs.getString("path_id"));
						map.put("version_path", rs.getString("version_path"));
						map.put("goal_devicetype_id", rs.getString("goal_devicetype_id"));
						map.put("goalDeviceTypeName", DeviceTypeUtil
								.getDeviceSoftVersion(StringUtil.getStringValue(rs
										.getString("goal_devicetype_id"))));


						map.put("hard_version", rs.getString("hardwareversion"));
						map.put("version_path", rs.getString("version_path"));
						map.put("dcn_path", rs.getString("dcn_path"));
						map.put("special_path", rs.getString("special_path"));

						return map;
					}
				});
		return list;
	}


	public int countStbUpgradeFilePathList_hnlt(int curPage_splitPage,
		int num_splitPage,String goal_devicetypeId)
	{
		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
		psql.append("select count(*) from stb_tab_devicetype_info a,stb_gw_filepath_devtype b,stb_gw_version_file_path c ");
		psql.append("where a.devicetype_id=b.goal_devicetype_id and b.path_id=c.id ");
		psql.append("and c.valid=1 ");
		if(!StringUtil.IsEmpty(goal_devicetypeId) && !"-1".equals(goal_devicetypeId)){
			psql.append("and a.devicetype_id="+StringUtil.getIntegerValue(goal_devicetypeId));
		}

		int total = jt.queryForInt(psql.getSQL());
		queryCount = total;
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}


}
