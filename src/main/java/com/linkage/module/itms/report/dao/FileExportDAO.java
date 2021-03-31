
package com.linkage.module.itms.report.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.User;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.obj.FileExportObj;

/**
 * @author guankai (Ailk No.300401)
 * @version 1.0
 * @since 2020年4月20日
 * @category com.linkage.module.itms.report.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class FileExportDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(FileExportDAO.class);

	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;
		
	public List<Map> getFileExportInfo(String fileExportDesc, String startTime, String endTime, 
			String cityId, User curUser, int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sqlstr = new StringBuffer();
		sqlstr.append(" select t.id, ");
		sqlstr.append("        t.fileexportuser, ");
		sqlstr.append("        t.fileexporttime, ");
		sqlstr.append("        t.fileexportdesc, ");
		sqlstr.append("        case ");
		sqlstr.append("          when t.status = '1' then ");
		sqlstr.append("           '已完成' ");
		sqlstr.append("          when t.status = '0' then ");
		sqlstr.append("           '未完成' ");
		sqlstr.append("          else ");
		sqlstr.append("           '' ");
		sqlstr.append("        end status,       ");
		sqlstr.append("        t.filename, ");
		sqlstr.append("        t.fileexportsql, ");
		sqlstr.append("        t.fileexportsql1, ");
		sqlstr.append("        t.fileexportfield, ");
		sqlstr.append("        t.cityid,t.filefinishtime ");
		sqlstr.append("   from tab_file_export t  ");
		sqlstr.append("  where 1=1 ");

		if (!StringUtil.IsEmpty(fileExportDesc))
		{
			sqlstr.append("    and t.fileExportDesc like '%"+fileExportDesc+"%'");
		}

		if (!StringUtil.IsEmpty(startTime))
		{
			sqlstr.append("    and t.fileexporttime > " + setTime(startTime));
		}
		if (!StringUtil.IsEmpty(endTime))
		{
			Long endTime1 = setTime(endTime)+86400;
			sqlstr.append("    and t.fileexporttime <= " +endTime1);
		}
		//admin.com用户查询所有导出信息，且有地市查询条件
		if(1!=curUser.getAreaId()){
			sqlstr.append("    and t.fileexportuser = '" + curUser.getAccount() + "'");
		}else{
			if (!StringUtil.IsEmpty(cityId)&&!"00".equals(cityId))
			{
				sqlstr.append("    and t.cityid = '" + cityId + "'");
			}
		}
		sqlstr.append("    order by t.fileexporttime desc");
		PrepareSQL pSQL = new PrepareSQL(sqlstr.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1, num_splitPage);

		if (null == list || list.isEmpty())
		{
			return new ArrayList<Map>();
		}
		
		for (Map map : list)
		{
			String city_id = StringUtil.getStringValue(map.get("cityid"));
			String city_name = StringUtil.getStringValue(cityMap.get(city_id));
			if (false == StringUtil.IsEmpty(city_name))
			{
				map.put("cityname", city_name);
			}
			else
			{
				map.put("cityname", "");
			}

			// 将dealdate转换成时间
			try
			{
				long dealdate = StringUtil.getLongValue(map.get("fileexporttime")); // 文件导出时间
				if (0 == dealdate)
				{
					map.put("fileexporttime", "");
				}
				else
				{
					DateTimeUtil dDate = new DateTimeUtil(dealdate * 1000);
					map.put("fileexporttime", dDate.getLongDate());
				}
			}
			catch (NumberFormatException e)
			{
				map.put("fileexporttime", "");
			}
			catch (Exception e)
			{
				map.put("fileexporttime", "");
			}
			// 将onlinedate转换成时间
			try
			{
				long onlinedate = StringUtil.getLongValue(map.get("filefinishtime")); // 竣工时间
				if (0 == onlinedate)
				{
					map.put("filefinishtime", "");
				}
				else
				{
					DateTimeUtil oDate = new DateTimeUtil(onlinedate * 1000);
					map.put("filefinishtime", oDate.getLongDate());
				}
			}
			catch (NumberFormatException e)
			{
				map.put("filefinishtime", "");
			}
			catch (Exception e)
			{
				map.put("filefinishtime", "");
			}
		}
		cityMap = null;
		//logger.warn("list:{}",list);
		return list;
	}

	public int getFileExportCount(String fileExportDesc, String startTime, String endTime, 
			String cityId, User curUser)
	{
		StringBuffer sqlstr = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sqlstr.append(" select count(*) ");
		}else{
			sqlstr.append(" select count(1) ");
		}
		
		sqlstr.append("   from tab_file_export ");
		sqlstr.append(" where 1=1 ");

		if (!StringUtil.IsEmpty(fileExportDesc))
		{
			sqlstr.append("    and fileExportDesc like '%"+fileExportDesc+"%'");
		}

		if (!StringUtil.IsEmpty(startTime))
		{
			sqlstr.append("    and fileexporttime > " + setTime(startTime));
		}
		if (!StringUtil.IsEmpty(endTime))
		{
			Long endTime1 = setTime(endTime)+86400;
			sqlstr.append("    and fileexporttime <= " + endTime1 );
		}
		//admin.com用户查询所有导出信息，且有地市查询条件
		if(1!=curUser.getAreaId()){
			sqlstr.append("    and fileexportuser = '" + curUser.getAccount() + "'");
		}else{
			if (!StringUtil.IsEmpty(cityId)&&!"00".equals(cityId))
			{
				sqlstr.append("    and cityid = '" + cityId + "'");
			}
		}
		PrepareSQL pSQL = new PrepareSQL(sqlstr.toString());
		return jt.queryForInt(pSQL.getSQL());
	}

	/**
	 * 时间转化
	 */
	private Long setTime(String intime)
	{
		Long outTime = null;
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (intime != null && !"".equals(intime))
		{
			dt = new DateTimeUtil(intime);
			outTime = dt.getLongTime();
		}
		return outTime;
	}

	/**
	 * 创建文件生成任务并发送到kafka
	 * @param obj
	 * @return
	 */
	public int creatFileTask(FileExportObj obj)
	{
		Integer orderId = UUID.randomUUID().toString().hashCode();
		orderId = orderId < 0 ? -orderId : orderId; // String.hashCode() 值会为空
		logger.warn("{}入参为：{}",orderId,obj.toString());
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" insert into tab_file_export ");
		sqlStr.append("   (id,fileexportuser, ");
		sqlStr.append("    fileexporttime, ");
		sqlStr.append("    fileexportdesc, ");
		sqlStr.append("    status, ");
		sqlStr.append("    filename, ");
		sqlStr.append("    fileexportsql, ");
		sqlStr.append("    fileexportsql1, ");
		sqlStr.append("    fileexportfield, ");
		sqlStr.append("    fileexportfieldname, ");
		sqlStr.append("    cityid) ");
		sqlStr.append(" values ");
		sqlStr.append("   (?,?, ?, ?, '0', ?, ?, ?, ?, ?,?) ");
		PrepareSQL pSQL = new PrepareSQL(sqlStr.toString());
		pSQL.setString(1, StringUtil.getStringValue(orderId));
		pSQL.setString(2, StringUtil.getStringValue(obj.getFileExportUser()));
		pSQL.setString(3, StringUtil.getStringValue(obj.getFileExportTime()));
		pSQL.setString(4, StringUtil.getStringValue(obj.getFileExportDesc()));
		pSQL.setString(5, StringUtil.getStringValue(obj.getFileName()));
		String fileExportSql = StringUtil.getStringValue(obj.getFileExportSql());
		String fileExportSql0 = fileExportSql;
		String fileExportSql1 = "";
		if(fileExportSql.length()>500) {
			fileExportSql0 = fileExportSql.substring(0,500);
			fileExportSql1 = fileExportSql.substring(500,fileExportSql.length());
		}
		pSQL.setString(6, fileExportSql0);
		pSQL.setString(7, fileExportSql1);
		pSQL.setString(8, StringUtil.getStringValue(obj.getFileExportField()));
		pSQL.setString(9, StringUtil.getStringValue(obj.getFileExportFieldName()));
		pSQL.setString(10, StringUtil.getStringValue(obj.getCityId()));
		try
		{
			logger.warn("任务sql：{}",pSQL.getSQL());
			int result = jt.queryForInt(pSQL.getSQL());
			if (result > 0)
			{

				Global.FILE_EXPORT_PUBLISHER.getPublisher().publish("fileExport", 
							StringUtil.getStringValue(orderId));
				return 1;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return -1;
	}
}
