package com.linkage.module.gtms.blocTest.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.blocTest.serv.MaintainAppInfoBIO;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.itms.resource.act.DeviceTypeInfoACT;
import com.linkage.module.itms.resource.bio.DevVendorDataUploadBIO;

/**
 *
 * @author zzd (Ailk No.)
 * @version 1.0
 * @since 2016-8-9
 * @category com.linkage.module.gtms.blocTest.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class MaintainAppInfoDAO extends SuperDAO
{

	public List queryDetailList(String detailid){
		String sql = "select appuuid,app_name,app_desc,app_vendor,app_version,app_publish_time,app_publish_status,"+
				"file_path from tab_soft_probe_app where id="+StringUtil.getLongValue(detailid);
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}
	public Map querydelPath(long id){
		String sql = "select file_path from tab_soft_probe_app where id ="+id;
		PrepareSQL psql = new PrepareSQL(sql);
		Map map =jt.queryForMap(psql.toString());
		return map;
	}

	public int pubAppInfo(long id,long app_publish_time){
		String sql = "update tab_soft_probe_app set app_publish_status=1,app_publish_time=? where id = " + id;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setLong(1, app_publish_time);
		int num = jt.update(psql.getSQL());
		return num;
	}

	public int delAppInfo(long id){
		String sql = "delete from tab_soft_probe_app where id = " + id;
		PrepareSQL psql = new PrepareSQL(sql);
		int num = jt.update(psql.getSQL());
		return num;
	}

	public int updateAppInfo(String id,String appuuid,String app_name,String app_desc,String app_vendor,String app_version,String app_publish_status,Long create_time,String create_user,Long app_publish_time, String fileAppPath){
		String sql = null;
		StringBuffer sb = new StringBuffer();

		sql = "update tab_soft_probe_app set ";
		sb.append(sql);

		sb.append("appuuid='"+appuuid+"', app_name='"+app_name);
		if (app_desc != null) {
			sb.append("',app_desc= '"+app_desc);
		}
		sb.append("' , app_vendor ='"+app_vendor
				+ "' , app_version ='"+ app_version+"', app_publish_status="+ StringUtil.getIntegerValue(app_publish_status)
				+ ", create_time="+ create_time);

		if (create_user != null && !"".equals(create_user)) {
			sb.append(", create_user= '"+create_user+"'");
		}

		if (fileAppPath != null && !"".equals(fileAppPath)) {
			sb.append(", file_path= '"+fileAppPath+"'");
		}
		if("1".equals(app_publish_status)){
			sb.append(", app_publish_time= "+app_publish_time);
		}
		sb.append(" where id= "+StringUtil.getLongValue(id));

		PrepareSQL psql = new PrepareSQL(sb.toString());

		int num = jt.update(psql.getSQL());
		return num;
	}

	public int getDeviceListCount(int curPage_splitPage, int num_splitPage, String appuuid, String app_name,
			String app_vendor, String app_version, String  app_publish_status,
			String app_publish_time_start, String app_publish_time_end){
		StringBuffer sb = new StringBuffer();
		String sql = "select count(*) from tab_soft_probe_app where 1=1";
		sb.append(sql);
		if (appuuid != null && !"".equals(appuuid)) {
			sb.append(" and appuuid = '" + appuuid+"'");
		}
		if (app_name != null && !"".equals(app_name)) {
			sb.append(" and app_name = '" + app_name+"'");
		}
		if (app_vendor != null && !"".equals(app_vendor)) {
			sb.append(" and app_vendor = '" + app_vendor+"'");
		}
		if (app_version != null && !"".equals(app_version)) {
			sb.append(" and app_version = '" + app_version+"'");
		}
		if (app_publish_status != null && !"".equals(app_publish_status)) {
			sb.append(" and app_publish_status = " + StringUtil.getIntegerValue(app_publish_status));
		}
		if (app_publish_time_start != null && !"".equals(app_publish_time_start)) {
			sb.append(" and app_publish_time >= " + StringUtil.getLongValue(app_publish_time_start));
		}
		if (app_publish_time_end != null && !"".equals(app_publish_time_end)) {
			sb.append(" and app_publish_time <= " + StringUtil.getLongValue(app_publish_time_end));
		}
		PrepareSQL psql = new PrepareSQL(sb.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}


	public List<Map> queryDeviceList(int curPage_splitPage, int num_splitPage, String appuuid, String app_name,
			String app_vendor, String app_version, String  app_publish_status,
			String app_publish_time_start, String app_publish_time_end){
		StringBuffer sb = new StringBuffer();
		String sql = null;
		sql = "select id,appuuid,app_name,app_desc,app_vendor,app_version,app_publish_status,app_publish_time,file_path from tab_soft_probe_app where 1=1";
		sb.append(sql);
		if (appuuid != null && !"".equals(appuuid)) {
			sb.append(" and appuuid = '" + appuuid+"'");
		}
		if (app_name != null && !"".equals(app_name)) {
			sb.append(" and app_name = '" + app_name+"'");
		}
		if (app_vendor != null && !"".equals(app_vendor)) {
			sb.append(" and app_vendor = '" + app_vendor+"'");
		}
		if (app_version != null && !"".equals(app_version)) {
			sb.append(" and app_version = '" + app_version+"'");
		}
		if (app_publish_status != null && !"".equals(app_publish_status)) {
			sb.append(" and app_publish_status = " + StringUtil.getIntegerValue(app_publish_status));
		}
		if (app_publish_time_start != null && !"".equals(app_publish_time_start)) {
			sb.append(" and app_publish_time >= " + StringUtil.getLongValue(app_publish_time_start));
		}
		if (app_publish_time_end != null && !"".equals(app_publish_time_end)) {
			sb.append(" and app_publish_time <= " +StringUtil.getLongValue( app_publish_time_end));
		}
		sb.append(" order by id");
		PrepareSQL psql = new PrepareSQL(sb.toString());
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", rs.getString("id"));
				map.put("appuuid", rs.getString("appuuid"));
				map.put("app_name", rs.getString("app_name"));
				map.put("app_desc", rs.getString("app_desc"));
				map.put("app_vendor", rs.getString("app_vendor"));
				map.put("app_version", rs.getString("app_version"));
				map.put("app_publish_status", rs.getString("app_publish_status"));
				map.put("filepath", rs.getString("file_path"));
				try {
					long app_publish_time=StringUtil.getLongValue(rs.getString("app_publish_time"));
					if(0 != app_publish_time){
						app_publish_time = StringUtil.getLongValue(rs
								.getString("app_publish_time")) * 1000L;
						DateTimeUtil dt = new DateTimeUtil(app_publish_time);
						map.put("app_publish_time", dt.getLongDate());
					}else{
						map.put("app_publish_time", "");
					}

				} catch (NumberFormatException e) {
					map.put("app_publish_time", "");
				} catch (Exception e) {
					map.put("app_publish_time", "");
				}
				return map;
			}
		});
		return list;
	}

	public int addMaintainAppInfo(long id, String appuuid, String app_name,
			String app_desc, String app_vendor, String app_version, long app_publish_time,
			String  app_publish_status, String create_user, long create_time, String file_path)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("insert into tab_soft_probe_app(id,appuuid,app_name,app_desc,app_vendor,app_version,app_publish_time,app_publish_status,create_user,create_time,file_path) values (?,?,?,?,?,?,?,?,?,?,?)");
		pSQL.setLong(1, id);
		pSQL.setString(2,appuuid);
		pSQL.setString(3,app_name);
		pSQL.setString(4,app_desc);
		pSQL.setString(5,app_vendor);
		pSQL.setString(6,app_version);
		if(0 != app_publish_time){
			pSQL.setLong(7, app_publish_time);
		}
		pSQL.setInt(8, StringUtil.getIntegerValue(app_publish_status));
		pSQL.setString(9,create_user);
		pSQL.setLong(10, create_time);
		pSQL.setString(11,file_path);
		int num = jt.update(pSQL.getSQL());
		return num;
	}

	public List checkFileName(){
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select file_path from tab_soft_probe_app");
		return jt.queryForList(pSQL.getSQL());
	}
}
