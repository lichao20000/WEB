
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
import com.linkage.litms.uss.DateTimeUtil;
import com.linkage.module.gtms.stb.dao.GwStbVendorModelVersionDAO;
import com.linkage.module.gwms.dao.SuperDAO;

@SuppressWarnings("rawtypes")
public class SoftVersionDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(SoftVersionDAO.class);

	private HashMap<String, String> vendorMap = new HashMap<String, String>();
	private HashMap<String, String> deviceModelMap = new HashMap<String, String>();
	/**网络类型*/
	private static Map<String,String> netTypeMap=new HashMap<String,String>();
	static{
		netTypeMap.put("public_net","公 网");
		netTypeMap.put("private_net","专 网");
		netTypeMap.put("unknown_net","未 知");
	}


	/**
	 * 获取版本文件路径数据
	 */
	public List getVersion(int curPage_splitPage, int num_splitPage,String vendorId,String versionName)
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append("select id, vendor_id, version_desc, epg_version, net_type, version_path," +
				"version_name, file_size, md5, update_time from stb_soft_version_path where 1=1 ");
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sql.append("and vendor_id='"+vendorId+"' ");
		}

		if(!StringUtil.IsEmpty(versionName)){
			sql.append("and version_name like '%"+versionName.trim()+"%' ");
		}
		sql.append("order by vendor_id ");

		vendorMap = GwStbVendorModelVersionDAO.getVendorMap();
		return querySP(sql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("id", rs.getString("id"));
						map.put("vendor_id", rs.getString("vendor_id"));
						map.put("vendor_add", vendorMap.get(rs.getString("vendor_id")));
						map.put("version_desc", rs.getString("version_desc"));
						map.put("epg_version", rs.getString("epg_version"));
						map.put("net_type", netTypeMap.get(rs.getString("net_type")));

						String version_path=rs.getString("version_path");
						map.put("version_path0",version_path);
//						if(!StringUtil.IsEmpty(version_path) && version_path.length()>80){
//							version_path=version_path.substring(0,80)+"\n"+version_path.substring(80);
//							version_path=version_path.replaceAll(" ","");
//						}
//						map.put("version_path",version_path);

						map.put("version_name", rs.getString("version_name"));
						map.put("file_size", rs.getString("file_size"));
						map.put("md5", rs.getString("md5"));
						String[] device_model = getDeviceModelById(rs.getString("id")).split("#");
						map.put("device_model",device_model[0]);
						map.put("device_model_id",device_model[1]);
						map.put("update_time",transDate(rs.getString("update_time")));
						return map;
					}
				});
	}

	/**
	 *日期转换
	 */
	private static final String transDate(Object seconds)
	{
		if (seconds != null)
		{
			try
			{
				DateTimeUtil dt = new DateTimeUtil(Long.parseLong(seconds.toString()) * 1000);
				return dt.getLongDate();
			}
			catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
		}
		return "";
	}

	/**
	 * 获取设备型号
	 */
	public List getDeviceModel(String vendorId)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append(" select device_model_id,device_model from stb_gw_device_model ");
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			pSQL.append("where vendor_id='"+vendorId+"' ");
		}
		return jt.queryForList(pSQL.toString());
	}

	/**
	 * 获取型号
	 */
	public String getDeviceModelById(String id)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_model_id from stb_soft_version_path_model where id=? ");
		psql.setInt(1,StringUtil.getIntegerValue(id));
		List list = jt.queryForList(psql.getSQL());
		deviceModelMap = GwStbVendorModelVersionDAO.getDeviceModelMap();
		String device_model = "";
		String device_model_id ="";
		for (int i = 0; i < list.size(); i++)
		{
			Map map = (Map) list.get(i);
			device_model = device_model + deviceModelMap.get(StringUtil.getStringValue(map,"device_model_id"))+",";
			device_model_id = device_model_id + StringUtil.getStringValue(map,"device_model_id") +",";
		}

		if (list.size() > 0)
		{
			device_model = device_model.substring(0, device_model.length() - 1);
			device_model_id = device_model_id.substring(0,device_model_id.length()-1);
		}
		return device_model+"#"+device_model_id;
	}

	/**
	 *获取所有厂商
	 */
	public List getVendor()
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select vendor_id,vendor_name,vendor_add from stb_tab_vendor");
		return jt.queryForList(pSQL.getSQL());
	}

	/**
	 * 获取最大页数
	 */
	public int getCountVersion(int curPage_splitPage,int num_splitPage,String vendorId,String versionName)
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append("select count(*) from stb_soft_version_path where 1=1");
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sql.append(" and vendor_id='"+vendorId+"'");
		}
		if(!StringUtil.IsEmpty(versionName)){
			sql.append(" and version_name like '%"+versionName.trim()+"%'");
		}

		int total = jt.queryForInt(sql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * 新增
	 */
	public int addVersion(long accoid, String vendorId, String versionDesc,
			String versionPath, String versionName,String deviceModelId,String fileSize,
			String MD5,String epg_version,String net_type)
	{
		List<String> sqllist = new ArrayList<String>();
		int[] ier=null;
		try
		{
			long id = Math.round(Math.random() * 100000L);
			long nowtime = new DateTimeUtil().getLongTime();

			PrepareSQL sql1 = new PrepareSQL();
			sql1.append("insert into stb_soft_version_path(id,acc_oid,vendor_id,version_desc,");
			sql1.append("version_name,version_path,add_time,update_time,file_size,md5,epg_version,net_type) ");
			sql1.append("values(?,?,?,?,?,?,?,?,?,?,?,?) ");

			sql1.setLong(1, id);
			sql1.setLong(2, accoid);
			sql1.setString(3, vendorId);
			sql1.setString(4, versionDesc);
			sql1.setString(5, versionName);
			sql1.setString(6, versionPath);
			sql1.setLong(7, nowtime);
			sql1.setLong(8, nowtime);
			sql1.setLong(9, StringUtil.getLongValue(fileSize));
			sql1.setString(10, MD5);
			sql1.setString(11, epg_version);
			sql1.setString(12, net_type);

			sqllist.add(sql1.getSQL());

			String[] deviceModelIds = deviceModelId.split(",");
			for (String device_model_id : deviceModelIds)
			{
				PrepareSQL sql2 = new PrepareSQL();
				sql2.append("insert into stb_soft_version_path_model(id,device_model_id) ");
				sql2.append("values(?,?) ");
				sql2.setLong(1, id);
				sql2.setLong(2, StringUtil.getLongValue(device_model_id));
				sqllist.add(sql2.getSQL());
			}

			ier = doBatch(sqllist);
		}
		catch(Exception e){
			logger.error("版本新增[{}]入库：  失败,err:[{}]",versionPath,e);
		}

		if (ier != null && ier.length > 0) {
			logger.debug("版本记录入库：  成功");
			return 1;
		} else {
			logger.debug("版本记录入库：  失败");
			return 0;
		}
	}

	/**
	 * 编辑
	 */
	public int editVersion(String id, String vendorId, String versionDesc,
			String versionPath, String versionName,String deviceModelId,
			String fileSize,String MD5,String epg_version,String net_type)
	{
		List<String> sqllist = new ArrayList<String>();
		long nowtime = new DateTimeUtil().getLongTime();
		int[] ier =null;
		try
		{
			PrepareSQL sql1 = new PrepareSQL();
			sql1.append("update stb_soft_version_path set vendor_id=?,version_desc=?,version_name=?,");
			sql1.append("version_path=?,file_size=?,md5=?,update_time=?,epg_version=?,net_type=? where id=? ");
			sql1.setString(1, vendorId);
			sql1.setString(2, versionDesc);
			sql1.setString(3, versionName);
			sql1.setString(4, versionPath);
			sql1.setLong(5, StringUtil.getLongValue(fileSize));
			sql1.setString(6, MD5);
			sql1.setLong(7, nowtime);
			sql1.setString(8, epg_version);
			sql1.setString(9, net_type);
			sql1.setLong(10, StringUtil.getLongValue(id));
			sqllist.add(sql1.getSQL());

			PrepareSQL sql2 = new PrepareSQL();
			sql2.append("delete from stb_soft_version_path_model where id=? ");
			sql2.setLong(1, StringUtil.getLongValue(id));
			sqllist.add(sql2.getSQL());

			String[] deviceModelIds = deviceModelId.split(",");
			for (String device_model_id : deviceModelIds)
			{
				PrepareSQL sql3 = new PrepareSQL();
				sql3.append("insert into stb_soft_version_path_model(id,device_model_id) ");
				sql3.append("values(?,?) ");
				sql3.setLong(1, StringUtil.getLongValue(id));
				sql3.setLong(2, StringUtil.getLongValue(device_model_id));
				sqllist.add(sql3.getSQL());
			}

			ier = doBatch(sqllist);
		}
		catch(Exception e){
			logger.error("版本编辑[{}]入库：  失败,err:[{}]",id,e);
		}

		if (ier != null && ier.length > 0) {
			logger.debug("版本记录入库：  成功");
			return 1;
		} else {
			logger.debug("版本记录入库：  失败");
			return 0;
		}
	}

	/**
	 * 删除
	 */
	public int deleteVersion(String id)
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append("delete from stb_soft_version_path where id=? ");
		sql.setLong(1, StringUtil.getLongValue(id));
		if(jt.update(sql.getSQL())>0){
			sql = new PrepareSQL();
			sql.append("delete from stb_soft_version_path_model where id=? ");
			sql.setLong(1, StringUtil.getLongValue(id));
			return jt.update(sql.getSQL());
		}

		return -1;
	}

	/**
	 * 版本详细
	 */
	public List getSoftVersionDetail(String id)
	{
		// TODO wait (more table related)
		PrepareSQL sql = new PrepareSQL();
		sql.append("select a.id, a.version_desc, a.version_name, a.version_path, a.file_size, a.md5, a.epg_version, " +
				"a.net_type, a.add_time, a.update_time, b.acc_loginname, c.vendor_name ");
		sql.append("from stb_soft_version_path a ");
		sql.append("left join tab_accounts b on a.acc_oid=b.acc_oid ");
		sql.append("left join stb_tab_vendor c on a.vendor_id=c.vendor_id ");
		sql.append("where a.id="+id);

		return jt.queryForList(sql.getSQL());
	}


}
