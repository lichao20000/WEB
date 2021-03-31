package com.linkage.module.gtms.stb.resource.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 *
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-2-11
 * @category com.linkage.module.gtms.stb.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class stbBootAdvertisementDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory
			.getLogger(stbBootAdvertisementDAO.class);
	/**
	 * @category getVendor 获取所有的厂商
	 *
	 */
	public List getVendor() {
		logger.debug("getVendor()");
		PrepareSQL pSQL = new PrepareSQL(
				"select vendor_id,vendor_name, vendor_add from stb_tab_vendor");
		return jt.queryForList(pSQL.getSQL());
	}
	// 根据厂商获取型号
		public List getDeviceModel(String vendorId) {
			logger.debug("getDeviceModel(vendorId:{})", vendorId);
			PrepareSQL pSQL = new PrepareSQL();
			pSQL.append(" select a.device_model_id,a.device_model from stb_gw_device_model a where 1=1 ");
			if (null != vendorId && !"".equals(vendorId) && !"-1".equals(vendorId)) {
				pSQL.append(" and a.vendor_id='");
				pSQL.append(vendorId);
				pSQL.append("'");
			}
			return jt.queryForList(pSQL.getSQL());
		}
		/**
		 * @category getVersionList 获取所有的设备版本
		 *
		 * @param vendor_id
		 * @param deviceModelId
		 *
		 * @return List
		 */
		public List getVersionList(String deviceModelIds) {
			logger.debug("getVersionList(deviceModelId:{})", deviceModelIds);
			String sql = "(";
			String[] tempId = deviceModelIds.split(",");
			if (tempId.length > 0) {
				for (int i = 0; i < tempId.length; i++) {
					sql += "'" + tempId[i] + "',";
				}
				sql = sql.substring(0, sql.length() - 1) + ")";
			}
			PrepareSQL pSQL = new PrepareSQL();
			pSQL.append("select a.devicetype_id,a.softwareversion from stb_tab_devicetype_info a where 1=1 ");
			if (!StringUtil.IsEmpty(sql)) {
				pSQL.append(" and a.device_model_id in");
				pSQL.append(sql);
				pSQL.append("");
			}
			return jt.queryForList(pSQL.toString());
		}
		/**
		 * 查询所有的分组
		 */
		public List getGroupId()
		{
			PrepareSQL pSQL = new PrepareSQL();
			pSQL.append("select group_id,group_name from stb_tab_usergroup");
			if(Global.CQDX.equals(Global.instAreaShortName))
			{
				return DBOperation.getRecords(pSQL.getSQL(), "xml-test");
			}else
			{
				return DBOperation.getRecords(pSQL.getSQL());
			}
		}

		public List<Map<String,String>> getServerParameter()
		{
			PrepareSQL psql = new PrepareSQL();
			psql.append("select server_url, access_user, access_passwd from stb_tab_picture_file_server  where 1=1 and file_type = 1 and server_name = '图片服务器' ");
			return jt.queryForList(psql.getSQL());
		}

		public int OpenDeviceShowPicConfig(long taskId,String taskName,String cityid,String vendor_id,String device_model_id,String devicetype_id
				,String group_id,String file_path,String booturl,String starturl,String authturl,long acc_oid,String priority,long add_time,String Invalid_time,String url )
		{
			 String status1 = LipossGlobals.getLipossProperty("status") ;
			DateTimeUtil dt = new DateTimeUtil(Invalid_time);
			ArrayList<String> sqllist = new ArrayList<String>();
			PrepareSQL sql1 = new PrepareSQL("insert into  stb_tab_pic_task(");
			sql1.append("task_id,task_name,city_id,vendor_id,device_model_id,devicetype_id,group_id,file_path,sd_qd_pic_url,");
			sql1.append(" sd_kj_pic_url,sd_rz_pic_url,acc_oid,priority,status,add_time,Invalid_time)");
			sql1.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			sql1.setLong(1, taskId);
			sql1.setString(2, taskName);
			if(StringUtil.IsEmpty(cityid)||cityid.equals("-1"))
			{
				sql1.setString(3, "");
			}else
			{
				sql1.setString(3, cityid);
			}
			if(StringUtil.IsEmpty(vendor_id)||vendor_id.equals("-1"))
			{
				sql1.setString(4, "");
			}else
			{
				sql1.setString(4, vendor_id);
			}
			if(StringUtil.IsEmpty(device_model_id)||device_model_id.equals("-1"))
			{
				sql1.setString(5, "");
			}else
			{
				sql1.setString(5, device_model_id);
			}
			if(StringUtil.IsEmpty(devicetype_id)||devicetype_id.equals("-1"))
			{
				sql1.setString(6, "");
			}else
			{
				sql1.setString(6, devicetype_id);
			}
			if(StringUtil.IsEmpty(group_id))
			{
				sql1.setString(7, "");
			}else
			{
				sql1.setString(7, group_id);
			}
			if(StringUtil.IsEmpty(file_path))
			{
				sql1.setString(8, "");
			}else
			{
				sql1.setString(8, file_path);
			}
			if(StringUtil.IsEmpty(booturl))
			{
				sql1.setString(9, "");
			}else
			{
				sql1.setString(9, url+booturl);
			}
			if(StringUtil.IsEmpty(starturl))
			{
				sql1.setString(10, "");
			}else
			{
				sql1.setString(10, url+starturl);
			}
			if(StringUtil.IsEmpty(authturl))
			{
				sql1.setString(11, "");
			}else
			{
				sql1.setString(11,url+authturl);
			}
			sql1.setLong(12, acc_oid);
			sql1.setInt(13, StringUtil.getIntegerValue(priority));

			sql1.setInt(14, StringUtil.getIntegerValue(status1));
			sql1.setLong(15, add_time);
			sql1.setLong(16, dt.getLongTime());
			sqllist.add(sql1.getSQL());
			int ier=0;
			if(Global.CQDX.equals(Global.instAreaShortName)){
				  ier= DBOperation.executeUpdate(sqllist, "xml-test");
			}else
			{
				  ier= DBOperation.executeUpdate(sqllist, "xml-picture");
			}
			if (ier> 0) {
				logger.warn("任务定制：  成功");
				return 1;
			} else {
				logger.warn("任务定制：  失败");
				return 0;
			}
		}

		public void recordOperLog(String userAccount, String logip, String hostname,
				long accOid, String itemId, String oprCont)
		{
			// 系统操作日志SQL
			final String saveOperLog = "insert into tr_web_oper_log (log_time, acc_loginname, log_ip, host_name, acc_oid, item_id, oper_cont, oper_type_id) values (?,?,?,?,?,?,?,?)";
			PrepareSQL psql = new PrepareSQL(saveOperLog);
			psql.setLong(1, System.currentTimeMillis() / 1000);
			psql.setString(2, userAccount);
			psql.setString(3, logip);
			psql.setString(4, hostname);
			psql.setLong(5, accOid);
			psql.setString(6, itemId);
			psql.setString(7, oprCont);
			psql.setInt(8, 1);
			DBOperation.executeUpdate(psql.getSQL());
		}
}

