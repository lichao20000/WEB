package com.linkage.litms.filemanage;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.Global;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.CommonMap;
import com.linkage.litms.common.util.FormUtil;
import com.linkage.litms.resource.DeviceAct;
import com.linkage.litms.system.UserRes;


public class FileManage 
{
	private static Logger logger = LoggerFactory.getLogger(FileManage.class);
	private static String instArea=com.linkage.module.gwms.Global.instAreaShortName;
	private static final int DB_MYSQL=com.linkage.module.gwms.Global.DB_MYSQL;
	public static final String SXLT="sx_lt";
	public static final String NXDX="nx_dx";
	
	/**
	 * 对版本文件数据表进行操作
	 */
	public String fileAct(HttpServletRequest request)
	{
		String strSQL = "";
		String strMsg = "";

		String strAction = request.getParameter("action");
		String softwarefile_id = request.getParameter("softwarefile_id");
		if (strAction.equals("delete")) {
			strSQL = "update tab_software_file set  softwarefile_isexist =0 where softwarefile_id="
					+ softwarefile_id;
		} else {
			String fileName = request.getParameter("filename");
			String file_type = request.getParameter("file_type");
			String file_size = request.getParameter("file_size");
			String file_status = request.getParameter("file_status");
			String fileserver = request.getParameter("fileserver");
			String remark = request.getParameter("remark");
			String devicetype_id = request.getParameter("devicetype_id");
			String device_model_id = request.getParameter("device_model_id");
			String cityList = request.getParameter("cityList");
			String serviceList = request.getParameter("serviceList");
			logger.debug("devicetype_id={}", devicetype_id);
			if (strAction.equals("add")) {
				String[] filepath = fileserver.split("\\|");
				long file_id = DataSetBean.getMaxId("tab_software_file","softwarefile_id");
				strSQL = "insert into tab_software_file(softwarefile_id,softwarefile_name,"
						+ "softwarefile_description,softwarefile_size,dir_id,softwarefile_status,"
						+ "softwarefile_isexist,devicetype_id,citylist,servicelist,device_model_id) values("
						+ file_id
						+ ",'"+ fileName
						+ "','"+ remark
						+ "',"+ file_size
						+ ","+ filepath[2]
						+ ","+ file_status
						+ ",1"
						+ ","+ devicetype_id
						+ ",'"+ cityList
						+ "','"+ serviceList
						+ "','"+ device_model_id + "')";

				strSQL = strSQL.replaceAll(",,", ",null,");
				strSQL = strSQL.replaceAll(",\\)", ",null)");
			} else {

				/*String sqldevicetype_id = "select count(1) as num from tab_software_file where softwarefile_isexist=1 and devicetype_id ='"
						+ devicetype_id
						+ "' and softwarefile_id !="
						+ softwarefile_id;
				PrepareSQL psql = new PrepareSQL(sqldevicetype_id);
		    	psql.getSQL();
				Cursor fieldsdevicetype_id = DataSetBean
						.getCursor(sqldevicetype_id);

				if (null != fieldsdevicetype_id.getNext()) {
					strMsg = "此设备版本的文件已经上传！";
					return strMsg;
				}*/
				strSQL = "update tab_software_file set  devicetype_id="
						+ devicetype_id + ",softwarefile_status=" + file_status
						+ ",softwarefile_description='" + remark
						+ "',citylist='" + cityList + "',servicelist='"
						+ serviceList + "',device_model_id='" + device_model_id
						+ "' where  softwarefile_id=" + softwarefile_id;
				strSQL = strSQL.replaceAll(",,", ",null,");
				strSQL = strSQL.replaceAll(",\\)", ",null)");
			}
		}

		if (!"".equals(strSQL)) {
			PrepareSQL psql1 = new PrepareSQL(strSQL);
			int iCode = DataSetBean.executeUpdate(psql1.getSQL());
			if (iCode > 0) {
				strMsg = "文件操作成功！";
			} else {
				strMsg = "文件操作失败，请返回重试或稍后再试！";
			}

		}
		return strMsg;
	}

	/**
	 * 查询文件服务器
	 */
	public Cursor getCursor(int fileType) 
	{
		String strSQL = "select * from tab_file_server where file_type="+ fileType;
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			strSQL = "select inner_url, server_dir, dir_id, access_user, access_passwd, outter_url, gather_id, server_name, file_type from tab_file_server where file_type="+ fileType;
		}
		PrepareSQL psql = new PrepareSQL(strSQL);
		return DataSetBean.getCursor(psql.getSQL());
	}

	/**
	 * 查询配置文件列表
	 */
	public String getConfigHtml(HttpServletRequest request) 
	{
		ModelManage modelManage = new ModelManage();

		Map deviceMap = modelManage.getDeviceTypeMap();
		DeviceAct deviceAct = new DeviceAct();
		Map mapAreaName = deviceAct.getAreaIdMapName();
		String filename = request.getParameter("filename");

		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long u_area_id = curUser.getAreaId();

		String vendor_id = request.getParameter("vendor_id");
		String devicetype_id = request.getParameter("devicetype_id");
		String filestatus = request.getParameter("filestatus");
		String device_id = request.getParameter("device_id");
		String s_area_id = request.getParameter("s_area_id");
		String sql_area_id = null;
		String gw_type = request.getParameter("gw_type");
		String tabName = "tab_vercon_file";
		if("4".equals(gw_type)) tabName = "stb_tab_vercon_file";
		String Sql = "select * from "+tabName+" where verconfile_isexist=1";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			Sql = "select verconfile_status, verconfile_name, dir_id, area_id, verconfile_size, devicetype_id, verconfile_description," +
					" verconfile_id, gw_type from "+tabName+" where verconfile_isexist=1";
		}

		// 如果用户在界面上没有选择域，则从session中读取当前用户的域
		// MODIFY BY hemc
		if (!StringUtil.IsEmpty(s_area_id) && !s_area_id.equals("-1")) {
			sql_area_id = "select area_id from tab_area where area_pid="
					+ s_area_id + " or area_id=" + s_area_id;
		} else {
			sql_area_id = "select area_id from tab_area where area_pid="
					+ u_area_id + " or area_id=" + u_area_id + " ";
		}

		if (filename != null && !filename.equals("")) {
			Sql += " and verconfile_name like '%" + filename + "%'";
		}

		// if(vendor_id != null && !vendor_id.equals("-1")){
		//        	
		// Sql +=" and vendor_id ='"+ vendor_id + "'";
		//        		
		// }

		if (devicetype_id != null && !devicetype_id.equals("-1")) {
			Sql += " and devicetype_id =" + devicetype_id;
		}
		if (device_id != null && !device_id.equals("-1")) {
			Sql += " and device_id ='" + device_id + "'";
		}
		Sql += " and area_id in ( " + sql_area_id + ") ";

		if (!StringUtil.IsEmpty(filestatus)) {
			Sql += " and verconfile_status =" + filestatus;
		}
		
		if(NXDX.equals(instArea))
		{
			if (!StringUtil.IsEmpty(gw_type) && !"-1".equals(gw_type)) {
				Sql += " and gw_type = '" + gw_type+"'";
			}
		}
		
		if(SXLT.equals(instArea)) {
			Sql += " order by verconfile_name desc";
		}else {
			Sql += " order by verconfile_id ";
		}
		
		String strData = "<TABLE border=0 cellspacing=1 bgcolor=#999999 cellpadding=2 width='100%'>"
						+ "<TR><TH nowrap>文件名称</TH><TH nowrap>文件大小(Byte)</TH>"
						+ "<TH nowrap>设备型号</TH><TH nowrap>状态</TH><TH nowrap>描述</TH>"
						+ "<TH>管理域</TH><TH nowrap>操作</TH></TR>";
		
		if(SXLT.equals(instArea))
		{
			strData = "<TABLE border=0 cellspacing=1 bgcolor=#999999 cellpadding=2 width='100%'>"
					+ "<TR><TH nowrap>唯一标识</TH><TH nowrap>文件名称</TH>"
					+ "<TH nowrap>文件大小(Byte)</TH><TH nowrap>设备型号</TH>"
					+ "<TH nowrap>操作</TH></TR>";
			
		}
		
		Cursor cursor = null;
		Map fields = null;
		PrepareSQL psql = new PrepareSQL(Sql);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(Sql);
		fields = cursor.getNext();

		String dir = "";
		String name = "";

		String area_id = null;
		String area_name = null;
		if (fields == null) {
			strData += "<TR><TD class=column1 align=left colspan=7>查询没有记录！</TD></TR>";
		} else {
			while (fields != null) {
				int intfileStatus = Integer.parseInt((String) fields.get("verconfile_status"));
				String strfileStatus = null;
				if (intfileStatus == 1) {
					strfileStatus = "已审核";
				} else {
					strfileStatus = "未审核";
				}
				name = (String) fields.get("verconfile_name");
				dir = (String) fields.get("dir_id");
				area_id = (String) fields.get("area_id");
				area_name = (String) mapAreaName.get(area_id);
				if (area_name == null) {
					area_name = "-";
				}
				
				strData += "<TR>" ;
				if("nx_dx".equals(instArea))
				{
					strData += "<TD class=column1 align=left nowrap>"
									+ "<a href='javascript://' onclick=openwin('"
										+ dir+ "','"+ name+ "')>"+ name+ "</a></TD>"
								+ "<TD class=column1 align=center nowrap>"+ fields.get("verconfile_size")+ "</TD>"
								+ "<TD class=column1 align=center nowrap>"+ deviceMap.get(fields.get("devicetype_id"))+ "</TD>"
								+ "<TD class=column1 align=center nowrap>"+ strfileStatus+ "</TD>"
								+ "<TD class=column1 align=center nowrap>"+ fields.get("verconfile_description")+ "</TD>"
								+ "<TD class=column1 align=center nowrap>"+ area_name+ "</TD>"
								+ "<TD class=column1 align=center><a href=fileEdit.jsp?&verconfile_id="
									+ fields.get("verconfile_id")+"&gw_type="+fields.get("gw_type")
										+ ">修改</a>|<a href=fileSave.jsp?action=delete&verconfile_id="
										+ fields.get("verconfile_id")
										+ " onclick='return delWarn();' >删除</a>|<a href=# onclick=doSave('"
										+ dir + "','" + name + "') >保存</a></TD>";
				}
				else 
				{
					if(SXLT.equals(instArea)){
						strData += "<TD class=column1 align=center nowrap>"+name.split("\\.")[2]+ "</TD>";
						strData += "<TD class=column1 align=left nowrap>"+ name + "</TD>";
						strData	+= "<TD class=column1 align=center nowrap>"+ fields.get("verconfile_size")+ "</TD>"
								+ "<TD class=column1 align=center nowrap>"+ deviceMap.get(fields.get("devicetype_id"))+ "</TD>";
						strData+= "<TD class=column1 align=center>";
						strData+= "<a href=fileSave_sxlt.jsp?action=delete&verconfile_id="
								+ fields.get("verconfile_id")+"&gw_type="+gw_type
								+ " onclick='return delWarn();' >删除</a>";
						strData+="</TD>";
					}else {
						strData += "<TD class=column1 align=left nowrap>"
								+ "<a href='javascript://' onclick=openwin('"+ dir+ "','"+ name+ "')>"+ name+ "</a></TD>";
						strData	+= "<TD class=column1 align=center nowrap>"+ fields.get("verconfile_size")+ "</TD>"
								+ "<TD class=column1 align=center nowrap>"+ deviceMap.get(fields.get("devicetype_id"))+ "</TD>";
						strData+= "<TD class=column1 align=center nowrap>"+ strfileStatus+ "</TD>"
								+ "<TD class=column1 align=center nowrap>"+ fields.get("verconfile_description")+ "</TD>"
								+ "<TD class=column1 align=center nowrap>"+ area_name+ "</TD>";
						strData+= "<TD class=column1 align=center>";
						strData+="<a href=fileEdit.jsp?&verconfile_id="+fields.get("verconfile_id")+">修改</a>|";
						strData+= "<a href=fileSave.jsp?action=delete&verconfile_id="
								+ fields.get("verconfile_id")
								+ " onclick='return delWarn();' >删除</a>";
						strData+="|<a href=# onclick=doSave('"+ dir + "','" + name + "') >保存</a>";
						strData+="</TD>";
					}
				}
				strData+="</TR>";
				
				fields = cursor.getNext();
			}
		}
		strData += "</TABLE>";

		strData = "parent.document.all(\"userTable\").innerHTML=\"" + strData+ "\";";
		strData += "parent.document.all(\"dispTr\").style.display=\"\";";
		modelManage = null;
		deviceAct = null;
		if (mapAreaName != null) {
			mapAreaName.clear();
		}
		mapAreaName = null;
		if (deviceMap != null) {
			deviceMap.clear();
		}
		deviceMap = null;
		return strData;
	}
	
	/**
	 * 查询文件列表
	 */
	public String getHtml(HttpServletRequest request) 
	{
		logger.debug("getHtml(request)");

		StringBuffer html = new StringBuffer();

		ModelManage modelManage = new ModelManage();
		Map deviceMap = modelManage.getDeviceTypeMap();
		// 20200506 增加硬件和软件版本
		Map hardwareversionMap = getVersionMapByDevicetypeId("hardwareversion");
		Map softwareversionMap = getVersionMapByDevicetypeId("softwareversion");

		String filename = request.getParameter("filename");
		String devicetype_id = request.getParameter("devicetype_id");
		String filestatus = request.getParameter("filestatus");
		String city_id = request.getParameter("city_id");
		String service_id = request.getParameter("service_id");
		String device_model_id = request.getParameter("device_model_id");
		String vendorId = request.getParameter("vendor_id");

		String Sql = "select * from tab_software_file where softwarefile_isexist=1";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			Sql = "select softwarefile_status, citylist, servicelist, softwarefile_id, softwarefile_name, softwarefile_size, devicetype_id, " +
					"softwarefile_description from tab_software_file where softwarefile_isexist=1";
		}

		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)
				&& (StringUtil.IsEmpty(device_model_id) || "-1".equals(device_model_id))) {
			Sql += " and device_model_id in (select device_model_id from gw_device_model "
					+ "where vendor_id='"+ vendorId + "')";
		}
		if (!StringUtil.IsEmpty(device_model_id) && !"-1".equals(device_model_id)) {
			Sql += " and device_model_id='" + device_model_id + "'";
		}
		if (!StringUtil.IsEmpty(filename)) {
			Sql += " and softwarefile_name like '%" + filename + "%'";
		}
		if (!StringUtil.IsEmpty(devicetype_id) && !devicetype_id.equals("-1")) {
			Sql += " and devicetype_id =" + devicetype_id;
		}
		if (!StringUtil.IsEmpty(filestatus)) {
			Sql += " and softwarefile_status =" + filestatus;
		}
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)) {
			Sql += " and citylist like '%" + city_id + "%'";
		}
		if (!StringUtil.IsEmpty(service_id) && !"-1".equals(service_id)) {
			Sql += " and servicelist like '%" + service_id + "%'";
		}

		StringBuffer strData = new StringBuffer();
		strData.append("<TABLE border=0 cellspacing=1 bgcolor=#999999 cellpadding=2 width='100%'>");

		strData.append("<TR><TH>NO.</TH><TH>名称</TH><TH>大小(Byte)</TH><TH>设备型号</TH>"
				+ "<TH>硬件版本</TH><TH>软件版本</TH><TH>状态</TH><TH>描述</TH><TH>操作</TH>");// 20200506 增加硬件版本、软件版本
		if(!SXLT.equals(instArea)){
			strData.append("<TH>属地</TH><TH>业务</TH>");
		}
		strData.append("</TR>");
		Cursor cursor = null;
		Map fields = null;
		PrepareSQL psql = new PrepareSQL(Sql);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(Sql);
		fields = cursor.getNext();
		if (fields == null) {
			if(!SXLT.equals(instArea)){
				strData.append("<TR><TD class=column1 align=left colspan=11>查询没有记录！</TD></TR>");
			}
			else
			{
				strData.append("<TR><TD class=column1 align=left colspan=9>查询没有记录！</TD></TR>");
			}

		} else {
			while (fields != null) {
				int intfileStatus = Integer.parseInt((String) fields.get("softwarefile_status"));
				String strfileStatus = null;
				if (intfileStatus == 1) {
					strfileStatus = "<IMG SRC='../images/check.gif' BORDER='0' ALT='审核通过'>";
				} else {
					strfileStatus = "-";
				}

				// 解析地市
				String cityList = (String) fields.get("citylist");
				String cityListDesc = "";
				String tmpCity = "";
				String[] cityArr = cityList.split(",");
				Map cityMap = CommonMap.getCityMap();
				for (int i = 0; i < cityArr.length; i++) {
					if (cityArr[i] != null && !"".equals(cityArr[i])) {
						tmpCity = (String) cityMap.get(cityArr[i]);
						if (tmpCity != null && !"".equals(tmpCity)) {
							if ("".equals(cityListDesc)) {
								cityListDesc = tmpCity;
							} else {
								cityListDesc = cityListDesc + "，" + tmpCity;
							}
						}
					}
				}

				// 解析业务
				String serviceList = (String) fields.get("servicelist");
				String serviceListDesc = "";
				String tmpService = "";
				String[] serviceArr = serviceList.split(",");
				for (int i = 0; i < serviceArr.length; i++) {
					if (serviceArr[i] != null && !"".equals(serviceArr[i])) {
						tmpService = Global.Service_Id_Name_Map.get(serviceArr[i]);
						if (tmpService != null && !"".equals(tmpService)) {
							if ("".equals(serviceListDesc)) {
								serviceListDesc = Global.Service_Id_Name_Map.get(serviceArr[i]);
							} else {
								serviceListDesc = serviceListDesc+ "<BR>"+ Global.Service_Id_Name_Map.get(serviceArr[i]);
							}
						}
					}
				}

				strData.append("<TR>");
				strData.append("<TD class=column1 align=right width=4%>"+ fields.get("softwarefile_id") + "</TD>");
				strData.append("<TD class=column1 align=right width=10%>"+ fields.get("softwarefile_name") + "</TD>");
				strData.append("<TD class=column1 align=right width=10%>"+ fields.get("softwarefile_size") + "</TD>");
				strData.append("<TD class=column1 align=right width=10%>"+ deviceMap.get(fields.get("devicetype_id")) + "</TD>");
				strData.append("<TD class=column1 align=right width=10%>"+ hardwareversionMap.get(fields.get("devicetype_id")) + "</TD>");
				strData.append("<TD class=column1 align=right width=10%>"+ softwareversionMap.get(fields.get("devicetype_id")) + "</TD>");
				strData.append("<TD class=column1 align=center width=5%>"+ strfileStatus + "</TD>");
				strData.append("<TD class=column1 align=right >"+ fields.get("softwarefile_description") + "</TD>");
				if(!SXLT.equals(instArea)){
					strData.append("<TD class=column1 align=left width=10%>"+ cityListDesc + "</TD>");
					strData.append("<TD class=column1 align=left width=10%>"+ serviceListDesc + "</TD>");
				}
				strData.append("<TD class=column1 align=center width=6%>");
				
				if(SXLT.equals(instArea)){
					String fileInfo = JSONObject.toJSONString(fields);
					fileInfo = fileInfo.replace("\"", "\\\"");
					strData.append("<IMG SRC='../images/edit.gif' BORDER='0' ALT='编辑' style='cursor:hand' "
							+ "onclick=editFile('" + fileInfo + "')>");
				}else {
					strData.append("<IMG SRC='../images/edit.gif' BORDER='0' ALT='编辑' style='cursor:hand' onclick='editFile("
							+ fields.get("softwarefile_id") + ")'>");
				}
				strData.append("&nbsp;");
				strData.append("<IMG SRC='../images/del.gif' BORDER='0' ALT='删除' style='cursor:hand' onclick='deleteFile("
								+ fields.get("softwarefile_id") + ")'>");
				strData.append("</TD></TR>");

				fields = cursor.getNext();
			}

		}
		strData.append("</TABLE>");

		html.append("parent.document.all(\"userTable\").innerHTML=\""+ strData.toString() + "\";");
		html.append("parent.document.all(\"dispTr\").style.display=\"\";");

		return html.toString();
	}

	/**
	 * 获取版本Map<devicetype_id,hardwareversion/softwareversion>
	 * @return
	 */
	public Map getVersionMapByDevicetypeId(String versionType){
		Map map = new HashMap();
		String tmpSql = "select devicetype_id, hardwareversion, softwareversion from tab_devicetype_info";
		PrepareSQL psql = new PrepareSQL(tmpSql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		//String DeviceTypeList  = FormUtil.createListBox(cursor,"devicetype_id","device_model",false,cast,"");
		Map fields = cursor.getNext();
		if(fields != null){
			String devicetype_id = "";
			String hardwareversion= "";
			String softwareversion= "";
			while(fields != null){
				devicetype_id = (String) fields.get("devicetype_id");
				hardwareversion = (String) fields.get("hardwareversion");
				softwareversion = (String) fields.get("softwareversion");
				if(versionType.equals("hardwareversion")) {
					map.put(devicetype_id, hardwareversion);
				}else {
					map.put(devicetype_id, softwareversion);
				}
				fields = cursor.getNext();
			}
					
		}
		return map;	
	}
	
	
	// 根据版本id获取版本信息map
	public String getDevTypeByDevTypeId(HttpServletRequest request) 
	{
		String response = "";
		
		int devicetype_id = Integer.parseInt(request.getParameter("devicetype_id"));
		String sql = "select * from tab_devicetype_info where devicetype_id = " + devicetype_id;
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		
		if(fields != null) {
			response = JSONObject.toJSONString(fields);
		}
		
		return response;
	}
	
	// 获取厂商信息 Map<vendor_id,vendor_name> 包含 Map<vendor_name,vendor_id>
	public String getVendorInfo(HttpServletRequest request) 
	{
		String response = "";
		
		String sql = "select * from tab_vendor";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select vendor_id, vendor_name from tab_vendor";
		}
		com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		
		Map map = new HashMap();
		
		while (fields != null) 
		{
			String vendor_id = StringUtil.getStringValue(fields, "vendor_id");
			String vendor_name = StringUtil.getStringValue(fields, "vendor_name");
			map.put(vendor_id, vendor_name);
			map.put(vendor_name, vendor_id);
			fields = cursor.getNext();
		}
		
		if(map != null) 
		{
			response = JSONObject.toJSONString(map);
		}
		
		return response;
	}
	
	// 获取型号信息 Map<device_model_id, device_model> 包含 Map<device_model,device_model_id>
	public String getModelInfo(HttpServletRequest request) 
	{
		String response = "";
		String sql = "select * from gw_device_model";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select device_model_id, device_model from gw_device_model";
		}
		com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		
		Map map = new HashMap();
		
		while (fields != null) 
		{
			String device_model_id = StringUtil.getStringValue(fields, "device_model_id");
			String device_model = StringUtil.getStringValue(fields, "device_model");
			map.put(device_model_id, device_model);
			map.put(device_model, device_model_id);
			fields = cursor.getNext();
		}
		
		if(map != null) 
		{
			response = JSONObject.toJSONString(map);
		}
		
		return response;
	}
	
	// 根据厂商id，型号id，软硬件版本获取devicetype_id
	public String getDevTypeId(HttpServletRequest request) 
	{
		String response = "";
		
		String vendor_id = request.getParameter("vendor_id");
		String device_model_id = request.getParameter("device_model_id");
		String hardwareversion = request.getParameter("hardwareversion");
		String softwareversion = request.getParameter("softwareversion");
		
		if(StringUtil.IsEmpty(vendor_id) || StringUtil.IsEmpty(device_model_id)
				|| StringUtil.IsEmpty(hardwareversion) || StringUtil.IsEmpty(softwareversion)) {
			return response;
		}
		
		
		String sql = "select * from tab_devicetype_info where vendor_id = '" + vendor_id
				+ "' and device_model_id = '" + device_model_id
				+ "' and hardwareversion = '" + hardwareversion
				+ "' and softwareversion = '" + softwareversion + "'";
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if(fields != null) 
		{
			response = JSONObject.toJSONString(fields);
		}
		
		return response;
	}
	
	/**
	 * 获取某个版本文件信息
	 */
	public Map getFileMap(HttpServletRequest request) 
	{
		Map fields = null;
		String softwarefile_id = request.getParameter("softwarefile_id");
		String sql = "select * from tab_software_file  a ,tab_file_server b  where a.softwarefile_id ="
				+ softwarefile_id + " and a.dir_id = b.dir_id";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select softwarefile_id, softwarefile_name, device_model_id, devicetype_id, softwarefile_size, " +
					"softwarefile_status, server_name, server_dir, softwarefile_description, citylist, servicelist " +
					"from tab_software_file  a ,tab_file_server b  where a.softwarefile_id ="
					+ softwarefile_id + " and a.dir_id = b.dir_id";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		fields = cursor.getNext();
		
		return fields;
	}

	/**
	 * 获取某个配置文件信息
	 */
	public Map getConfigFileMap(HttpServletRequest request) 
	{
		Map fields = null;
		String verconfile_id = request.getParameter("verconfile_id");
		String sql = "select * from tab_vercon_file  a,tab_file_server b where a.verconfile_id="
				+ verconfile_id + " and a.dir_id = b.dir_id";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select verconfile_id, verconfile_name, area_id, devicetype_id, verconfile_size, verconfile_status, " +
					"server_name, server_dir, verconfile_description from tab_vercon_file  a,tab_file_server b where a.verconfile_id="
					+ verconfile_id + " and a.dir_id = b.dir_id";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		fields = cursor.getNext();
		return fields;
	}

	/**
	 * 对配置文件数据表进行操作
	 */
	public String fileOperate(HttpServletRequest request) 
	{
		String strSQL = "";
		String strMsg = "";
		String strAction = request.getParameter("action");
		String verconfile_id = request.getParameter("verconfile_id");
		if (strAction.equals("delete")) {
			strSQL = "update tab_vercon_file set  verconfile_isexist =0 where verconfile_id="
					+ verconfile_id;
		} else {
			String fileName = request.getParameter("filename");
			String file_type = request.getParameter("file_type");
			String file_size = request.getParameter("file_size");
			String file_status = request.getParameter("file_status");
			String fileserver = request.getParameter("fileserver");
			String area_id = request.getParameter("area_id");
			String remark = request.getParameter("remark");
			String devicetype_id = request.getParameter("devicetype_id");

			if (strAction.equals("add")) {
				String[] filepath = fileserver.split("\\|");
				long file_id = DataSetBean.getMaxId("tab_vercon_file","verconfile_id");
				strSQL = "insert into tab_vercon_file(verconfile_id,verconfile_name,"
						+ "verconfile_description,verconfile_size,dir_id,verconfile_status,"
						+ "verconfile_isexist,devicetype_id,area_id) values("
						+ file_id
						+ ",'"+ fileName
						+ "','"+ remark
						+ "',"+ file_size
						+ ","+ filepath[2]
						+ ","+ file_status
						+ ",1"
						+ ","+ devicetype_id
						+ ","+ area_id + ")";

				strSQL = strSQL.replaceAll(",,", ",null,");
				strSQL = strSQL.replaceAll(",\\)", ",null)");
			} else {
				strSQL = "update tab_vercon_file set  devicetype_id="
						+ devicetype_id + ",area_id=" + area_id
						+ ",verconfile_status=" + file_status
						+ ",verconfile_description='" + remark
						+ "' where  verconfile_id=" + verconfile_id;

				strSQL = strSQL.replaceAll(",,", ",null,");
				strSQL = strSQL.replaceAll(",\\)", ",null)");
			}
		}

		if (!"".equals(strSQL)) 
		{
			PrepareSQL psql = new PrepareSQL(strSQL);
			int iCode = DataSetBean.executeUpdate(psql.getSQL());
			if (iCode > 0) {
				strMsg = "配置文件操作成功！";
			} else {
				strMsg = "配置文件操作失败，请返回重试或稍后再试！";
			}
		}
		return strMsg;
	}
	
	
	public String fileOperateStb(HttpServletRequest request) 
	{
		String strSQL = "";
		String strMsg = "";
		String strAction = request.getParameter("action");
		String verconfile_id = request.getParameter("verconfile_id");
		if (strAction.equals("delete")) {
			strSQL = "update stb_tab_vercon_file set  verconfile_isexist =0 where verconfile_id="
					+ verconfile_id;
		} else {
			String fileName = request.getParameter("filename");
			String file_type = request.getParameter("file_type");
			String file_size = request.getParameter("file_size");
			String file_status = request.getParameter("file_status");
			String fileserver = request.getParameter("fileserver");
			String area_id = request.getParameter("area_id");
			String remark = request.getParameter("remark");
			String devicetype_id = request.getParameter("devicetype_id");

			if (strAction.equals("add")) {
				String[] filepath = fileserver.split("\\|");
				long file_id = DataSetBean.getMaxId("stb_tab_vercon_file","verconfile_id");
				strSQL = "insert into stb_tab_vercon_file(verconfile_id,verconfile_name,"
						+ "verconfile_description,verconfile_size,dir_id,verconfile_status,"
						+ "verconfile_isexist,devicetype_id,area_id) values("
						+ file_id
						+ ",'"+ fileName
						+ "','"+ remark
						+ "',"+ file_size
						+ ","+ filepath[2]
						+ ","+ file_status
						+ ",1"
						+ ","+ devicetype_id
						+ ","+ area_id + ")";

				strSQL = strSQL.replaceAll(",,", ",null,");
				strSQL = strSQL.replaceAll(",\\)", ",null)");
			} else {
				strSQL = "update stb_tab_vercon_file set  devicetype_id="
						+ devicetype_id + ",area_id=" + area_id
						+ ",verconfile_status=" + file_status
						+ ",verconfile_description='" + remark
						+ "' where  verconfile_id=" + verconfile_id;

				strSQL = strSQL.replaceAll(",,", ",null,");
				strSQL = strSQL.replaceAll(",\\)", ",null)");
			}
		}

		if (!"".equals(strSQL)) 
		{
			PrepareSQL psql = new PrepareSQL(strSQL);
			int iCode = DataSetBean.executeUpdate(psql.getSQL());
			if (iCode > 0) {
				strMsg = "配置文件操作成功！";
			} else {
				strMsg = "配置文件操作失败，请返回重试或稍后再试！";
			}
		}
		return strMsg;
	}

	/**
	 * 生成设备的下拉框，显示为 oui-serialnumber
	 */
	public String getDeviceList(HttpServletRequest request) 
	{
		String _devicelist = "";
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String devicetype_id = request.getParameter("devicetype_id");
		long m_AreaId = curUser.getUser().getAreaId();

		String _sql = "select device_id,oui,device_serialnumber from tab_gw_device a";

		if (!curUser.getUser().isAdmin()) {
			_sql += ",tab_gw_res_area b ";
		}

		_sql += " where 1=1 ";

		if (devicetype_id != null && !devicetype_id.equals("")
				&& !devicetype_id.equals("-1")) {
			_sql += " and a.devicetype_id=" + devicetype_id;
		}

		if (!curUser.getUser().isAdmin()) {
			_sql += " and a.device_id = b.res_id and b.res_type=1 and b.area_id="+ m_AreaId;
		}

		_sql += " order by a.device_id";
		PrepareSQL psql = new PrepareSQL(_sql);
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());

		String[] cast = { "oui", "device_serialnumber" };
		_devicelist = FormUtil.createListBox_link(cursor, "device_id", cast,false, "", "", true);

		return _devicelist;
	}

	/**
	 * 配置页面中的业务多选框
	 */
	public static String getServiceCheckBox(String list) 
	{
		StringBuffer CityCB = new StringBuffer();
		PrepareSQL pSQL = new PrepareSQL("select serv_type_id,serv_type_name from tab_gw_serv_type where type<10");
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());

		int i = 0;
		Map fields = cursor.getNext();
		while (fields != null) {
			String serv_type_id = (String) fields.get("serv_type_id");
			String serv_type_name = (String) fields.get("serv_type_name");

			if (list.indexOf("," + serv_type_id + ",") > -1) {
				CityCB.append("<input type='checkbox' checked name='service' value='")
						.append(serv_type_id).append("'>").append(serv_type_name);
			} else {
				CityCB.append("<input type='checkbox' name='service' value='")
						.append(serv_type_id).append("'>").append(serv_type_name);
			}

			CityCB.append("<br>");
			i++;
			fields = cursor.getNext();
		}

		return CityCB.toString();
	}

	/**
	 * 获取业务类型
	 */
	public static String getServiceList(boolean flag, String pos,
			String rename, HttpServletRequest request) 
	{
		PrepareSQL psql = new PrepareSQL("select serv_type_id,serv_type_name from tab_gw_serv_type where type<10");
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());

		String strCityList = FormUtil.createListBox(cursor, "serv_type_id",
				"serv_type_name", flag, pos, rename);

		return strCityList;
	}
}
