
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
import com.linkage.litms.uss.DateTimeUtil;
import com.linkage.module.gtms.stb.dao.GwStbVendorModelVersionDAO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

public class DeviceVersionDAO extends SuperDAO
{
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(DeviceVersionDAO.class);
	private HashMap<String, String> vendorMap = new HashMap<String, String>();
//	private HashMap<String, String> devicetypeMap = new HashMap<String, String>();
	private HashMap<String, String> deviceModelMap = new HashMap<String, String>();

	public List getVersion(int curPage_splitPage, int num_splitPage,String queryVendorId,String querySoftwareversion)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select id, vendor_id, version_desc, version_path, dcn_path, special_path, softwareversion, acc_oid, version_type " +
				" from stb_gw_version_file_path where valid=1");
		if(null != queryVendorId && !"".equals(queryVendorId) && !"-1".equals(queryVendorId))
		{
			sql.append(" and vendor_id='").append(queryVendorId).append("'");
		}
		if(null != querySoftwareversion && !"".equals(querySoftwareversion.trim()))
		{
			sql.append(" and softwareversion like '%").append(querySoftwareversion).append("%'");
		}
		sql.append(" order by vendor_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		vendorMap = GwStbVendorModelVersionDAO.getVendorMap();
		return querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						String id = rs.getString("id");
						map.put("id", id);
						String vendor_id = rs.getString("vendor_id");
						map.put("vendor_id", vendor_id);
						map.put("vendor_add", vendorMap.get(vendor_id));
						map.put("version_desc", rs.getString("version_desc"));
						map.put("version_path", rs.getString("version_path"));
						if (Global.HNLT.equals(Global.instAreaShortName)){
							map.put("dcn_path", rs.getString("dcn_path"));
							map.put("special_path", rs.getString("special_path"));
						}
						map.put("softwareversion", rs.getString("softwareversion"));
						map.put("acc_oid", rs.getString("acc_oid"));
						String[] device_model = getDeviceModelById(id).split("#");
						map.put("device_model", getIndexValue(device_model, 0));
						map.put("device_model_id", getIndexValue(device_model, 1));
						map.put("version_type", changeVersionType(rs.getLong("version_type"),false));
						return map;
					}
				});
	}

	private static String getIndexValue(String[] inputs, int index)
	{
		if (inputs != null && inputs.length > index)
		{
			return inputs[index];
		}
		return "";
	}

	/**
	 * 版本解析
	 * @param versionType
	 * @param issts
	 * @return
	 */
	private String changeVersionType(long versionType,boolean issts)
	{
		//默认为普通版本
		String versionInfo = "普通版本";
		if(1 == versionType)
		{
			versionInfo = "赛特斯版本";
		}else if(2 == versionType)
		{
			versionInfo = "退出赛特斯版本";
			if(!issts)
			{
				versionInfo = "非赛特斯版本";
			}
		}else if(3 == versionType)
		{
			versionInfo = "零配置版本";
		}
		return versionInfo;
	}

	public List getStsVersion()
	{
		String strSQL = "select id, vendor_id, version_desc, version_path, softwareversion, acc_oid, version_type " +
				" from stb_gw_version_file_path where valid=1 and version_type in (1,2) order by vendor_id";
		PrepareSQL psql = new PrepareSQL(strSQL.toString());
		vendorMap = GwStbVendorModelVersionDAO.getVendorMap();
		return jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				String id = rs.getString("id");
				map.put("id", id);
				String vendor_id = rs.getString("vendor_id");
				map.put("vendor_id", vendor_id);
				map.put("vendor_add", vendorMap.get(vendor_id));
				map.put("version_desc", rs.getString("version_desc"));
				map.put("version_path", rs.getString("version_path"));
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("acc_oid", rs.getString("acc_oid"));
//				Long version_type = ;
//				if(version_type == 1)
//				{
					map.put("version_type", changeVersionType(rs.getLong("version_type"),true));
//				}else{
//					map.put("version_type", "退出赛特斯版本");
//				}
				return map;
			}
		});
	}
	public String getDeviceModelById(String id)
	{
		StringBuffer sql = new StringBuffer();
		sql
				.append(
						"select device_model_id from stb_version_file_path_model where path_id=")
				.append(id);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		deviceModelMap = GwStbVendorModelVersionDAO.getDeviceModelMap();
		String device_model = "";
		String device_model_id ="";
		for (int i = 0; i < list.size(); i++)
		{
			Map map = (Map) list.get(i);
			device_model = device_model + deviceModelMap.get(map.get("device_model_id"))
					+ ",";
			device_model_id = device_model_id + map.get("device_model_id") +",";
		}
		if (list.size() > 0)
		{
			device_model = device_model.substring(0, device_model.length() - 1);
			device_model_id = device_model_id.substring(0,device_model_id.length()-1);
		}
		return device_model+"#"+device_model_id;
	}

	public List getVendor()
	{
		PrepareSQL pSQL = new PrepareSQL(
				"select vendor_id,vendor_name, vendor_add from stb_tab_vendor");
		return jt.queryForList(pSQL.getSQL());
	}

	public int getCountVersion(int curPage_splitPage, int num_splitPage,String queryVendorId,String querySoftwareversion)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from stb_gw_version_file_path where valid=1");
		if(null != queryVendorId && !"".equals(queryVendorId) && !"-1".equals(queryVendorId))
		{
			sql.append(" and vendor_id='").append(queryVendorId).append("'");
		}
		if(null != querySoftwareversion && !"".equals(querySoftwareversion.trim()))
		{
			sql.append(" and softwareversion like '%").append(querySoftwareversion).append("%'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
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

	public int addVersion(long accoid, String vendorId, String versionDesc,
			String versionPath, String version_type, String softwareversion,String deviceModelId,String dcnPath,String specialPath)
	{
		List<String> sqllist = new ArrayList<String>();

		PrepareSQL sql1 = null;
		long id = Math.round(Math.random() * 100000L);
		long nowtime = new DateTimeUtil().getLongTime();
		if (Global.HNLT.equals(Global.instAreaShortName)){
			sql1 = new PrepareSQL(
					"insert into stb_gw_version_file_path(id,vendor_id,version_desc,version_path,record_time,update_time,acc_oid,valid,softwareversion,version_type,dcn_path,special_path) values(?,?,?,?,?,?,?,?,?,?,?,?)");
			sql1.setLong(1, id);
			sql1.setString(2, vendorId);
			sql1.setString(3, versionDesc);
			sql1.setString(4, versionPath);
			sql1.setLong(5, nowtime);
			sql1.setLong(6, nowtime);
			sql1.setLong(7, accoid);
			sql1.setLong(8, 1);
			sql1.setString(9, softwareversion);
			sql1.setLong(10, StringUtil.getLongValue(version_type));
			sql1.setString(11, dcnPath);
			sql1.setString(12, specialPath);
			sqllist.add(sql1.getSQL());
		}else{
			sql1 = new PrepareSQL(
					"insert into stb_gw_version_file_path(id,vendor_id,version_desc,version_path,record_time,update_time,acc_oid,valid,softwareversion,version_type) values(?,?,?,?,?,?,?,?,?,?)");
			sql1.setLong(1, id);
			sql1.setString(2, vendorId);
			sql1.setString(3, versionDesc);
			sql1.setString(4, versionPath);
			sql1.setLong(5, nowtime);
			sql1.setLong(6, nowtime);
			sql1.setLong(7, accoid);
			sql1.setLong(8, 1);
			sql1.setString(9, softwareversion);
			sql1.setLong(10, StringUtil.getLongValue(version_type));
			sqllist.add(sql1.getSQL());
		}

		String[] deviceModelIds = deviceModelId.split(",");
		for (String string : deviceModelIds)
		{
			PrepareSQL sql2 = new PrepareSQL("insert into stb_version_file_path_model values(?,?)");
			sql2.setLong(1, id);
			sql2.setString(2, string);
			sqllist.add(sql2.getSQL());
		}
		int[] ier = doBatch(sqllist);
		if (ier != null && ier.length > 0) {
			logger.debug("版本记录入库：  成功");
			return 1;
		} else {
			logger.debug("版本记录入库：  失败");
			return 0;
		}
	}

	public int editVersion(String pathId, String vendorId, String versionDesc,
			String versionPath, String version_type, String softwareversion,String deviceModelId,String dcnPath,String specialPath)
	{
		List<String> sqllist = new ArrayList<String>();
		PrepareSQL sql1 = null;
		long nowtime = new DateTimeUtil().getLongTime();

		if (Global.HNLT.equals(Global.instAreaShortName)){
			sql1 = new PrepareSQL(
					"update stb_gw_version_file_path set vendor_id=?,version_desc=?,version_path=?,update_time=?,softwareversion=?,version_type=?,dcn_path=?,special_path=? where id=?");

			sql1.setString(1, vendorId);
			sql1.setString(2, versionDesc);
			sql1.setString(3, versionPath);
			sql1.setLong(4, nowtime);
			sql1.setString(5, softwareversion);
			sql1.setLong(6, StringUtil.getLongValue(version_type));
			sql1.setString(7, dcnPath);
			sql1.setString(8, specialPath);
			sql1.setLong(9, StringUtil.getLongValue(pathId));
			sqllist.add(sql1.getSQL());
		}else{
			sql1 = new PrepareSQL(
					"update stb_gw_version_file_path set vendor_id=?,version_desc=?,version_path=?,update_time=?,softwareversion=?,version_type=? where id=?");
			sql1.setString(1, vendorId);
			sql1.setString(2, versionDesc);
			sql1.setString(3, versionPath);
			sql1.setLong(4, nowtime);
			sql1.setString(5, softwareversion);
			sql1.setLong(6, StringUtil.getLongValue(version_type));
			sql1.setLong(7, StringUtil.getLongValue(pathId));
			sqllist.add(sql1.getSQL());
		}

		PrepareSQL sql2 = new PrepareSQL("delete from stb_version_file_path_model where path_id=?");
		sql2.setLong(1, StringUtil.getLongValue(pathId));
		sqllist.add(sql2.getSQL());
		String[] deviceModelIds = deviceModelId.split(",");
		for (String string : deviceModelIds)
		{
			PrepareSQL sql3 = new PrepareSQL("insert into stb_version_file_path_model values(?,?)");
			sql3.setLong(1, StringUtil.getLongValue(pathId));
			sql3.setString(2, string);
			sqllist.add(sql3.getSQL());
		}
		int[] ier = doBatch(sqllist);
		if (ier != null && ier.length > 0) {
			logger.debug("版本记录入库：  成功");
			return 1;
		} else {
			logger.debug("版本记录入库：  失败");
			return 0;
		}
	}
	public int isStsExsit(String vendorId,String versionType)
	{
		String strSQL = "select count(*) from stb_gw_version_file_path where valid=1 and vendor_id=? and version_type=?";
		PrepareSQL psql = new PrepareSQL(strSQL.toString());
		psql.setString(1, vendorId);
		psql.setLong(2, StringUtil.getLongValue(versionType));
		int count = jt.queryForInt(psql.getSQL());
		//一个厂商只有一个赛特斯版本，如果这个厂商已经存在赛特斯版本，则不允许新增
		if(count > 0)
		{
			return 0;
		}else{
			return 1;
		}
	}

	public int addStsVersion(long accoid, String vendorId, String versionDesc,
			String versionPath,String softwareversion,String versionType)
	{
//		String strSQL = "select count(*) from stb_gw_version_file_path where valid=1 and vendor_id=? and version_type=?";
//		PrepareSQL psql = new PrepareSQL(strSQL.toString());
//		psql.setString(1, vendorId);
//		psql.setLong(2, StringUtil.getLongValue(versionType));
//		int count = jt.queryForInt(psql.getSQL());
//		//一个厂商只有一个赛特斯版本，如果这个厂商已经存在赛特斯版本，则不允许新增
//		if(count > 0)
//		{
//			logger.debug("所选厂商已经存在赛特斯版本，不允许新增");
//			return -1;
//		}

		List<String> sqllist = new ArrayList<String>();
		PrepareSQL sql1 = new PrepareSQL(
				"insert into stb_gw_version_file_path(id,vendor_id,version_desc,version_path,record_time,update_time,acc_oid,valid,softwareversion,version_type) values(?,?,?,?,?,?,?,?,?,?)");
		long id = Math.round(Math.random() * 100000L);
		long nowtime = new DateTimeUtil().getLongTime();
		sql1.setLong(1, id);
		sql1.setString(2, vendorId);
		sql1.setString(3, versionDesc);
		sql1.setString(4, versionPath);
		sql1.setLong(5, nowtime);
		sql1.setLong(6, nowtime);
		sql1.setLong(7, accoid);
		sql1.setLong(8, 1);
		sql1.setString(9, softwareversion);
		sql1.setLong(10, StringUtil.getLongValue(versionType));
		sqllist.add(sql1.getSQL());
		int[] ier = doBatch(sqllist);
		if (ier != null && ier.length > 0) {
			logger.debug("赛特斯版本记录入库：  成功");
			return 1;
		} else {
			logger.debug("赛特斯版本记录入库：  失败");
			return 0;
		}
	}

	public int editStsVersion(String pathId, String vendorId, String versionDesc,
			String versionPath, String softwareversion,String versionType)
	{
//		String strSQL = "select count(*) from stb_gw_version_file_path where valid=1 and vendor_id=? and version_type=?";
//		PrepareSQL psql = new PrepareSQL(strSQL.toString());
//		psql.setString(1, vendorId);
//		psql.setLong(2, StringUtil.getLongValue(versionType));
//		int count = jt.queryForInt(psql.getSQL());
		//一个厂商只有一个赛特斯版本，如果这个厂商已经存在赛特斯版本，则不允许新增
//		if(count > 0)
//		{
//			logger.debug("所选厂商已经存在赛特斯版本，请重新选择厂商");
//			return -1;
//		}
		List<String> sqllist = new ArrayList<String>();
		PrepareSQL sql1 = new PrepareSQL(
				"update stb_gw_version_file_path set vendor_id=?,version_desc=?,version_path=?,update_time=?,softwareversion=? ,version_type=? where id=?");
		long nowtime = new DateTimeUtil().getLongTime();

		sql1.setString(1, vendorId);
		sql1.setString(2, versionDesc);
		sql1.setString(3, versionPath);
		sql1.setLong(4, nowtime);
		sql1.setString(5, softwareversion);
		sql1.setLong(6, StringUtil.getLongValue(versionType));
		sql1.setLong(7, StringUtil.getLongValue(pathId));
		sqllist.add(sql1.getSQL());
		int[] ier = doBatch(sqllist);
		if (ier != null && ier.length > 0) {
			logger.debug("赛特斯版本记录入库：  成功");
			return 1;
		} else {
			logger.debug("赛特斯版本记录入库：  失败");
			return 0;
		}
	}
	public int deleteVersion(String pathId)
	{
		//String [] sql=new String[2];
	//	sql[0]="delete from stb_gw_version_file_path where id="+pathId;
		//sql[1]="delete from stb_version_file_path_model where path_id="+pathId;
		PrepareSQL sql = new PrepareSQL("update stb_gw_version_file_path set valid=0 where id=?");
		sql.setLong(1, StringUtil.getLongValue(pathId));
		return jt.update(sql.getSQL());
	}
	public  int queryForTask(String pathId){
		String sql_batch_version="select count(*) from gw_softup_batch_version where path_id=?";
		PrepareSQL ps_batch_version = new PrepareSQL(sql_batch_version);
		ps_batch_version.setLong(1, StringUtil.getLongValue(pathId));
		int batch_version=jt.queryForInt(ps_batch_version.getSQL());

		if(batch_version >0){
			return batch_version;
		}

		String sql_softup_task="select count(*) from stb_gw_softup_task where version_path_id=? and valid!=-1";
		PrepareSQL ps_softup_task = new PrepareSQL(sql_softup_task);
		ps_softup_task.setLong(1, StringUtil.getLongValue(pathId));
		int softup_task=jt.queryForInt(ps_softup_task.getSQL());

		if(softup_task >0){
			return softup_task;
		}
		else{
			return 0;
		}

	}

    public List getNewVersionPath()
    {
        PrepareSQL psql = new PrepareSQL("select server_url from stb_tab_picture_file_server where file_type=2");
        List list = new ArrayList();
        list = jt.queryForList(psql.getSQL());
        if(list.size() > 0)
        {
            for(int i = 0; i < list.size(); i++)
            {
                String server_url = (new StringBuilder()).append(((Map)list.get(i)).get("server_url")).toString();
                ((Map)list.get(i)).put("server_url", server_url);
            }

        }
        return list;
    }
}
