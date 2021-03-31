package com.linkage.litms.filemanage;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.DBUtil;

import com.alibaba.fastjson.JSONObject;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

/**
 * 
 * @author lizhaojun
 * @version 1.00, 4/16/2007
 * @since HGW 1.0
 * 
 * ==================================================
 * @author Yanhj
 * @version 2.0, 4/3/2008
 * @desc 查询文件服务器，支持模糊查询
 */
public class ServerManage {
	/**
	 * 对文件服务器数据表进行操作
	 * 
	 * add by lizhaojun
	 * 
	 * @param request
	 * 
	 * @return strMsg 将操作结果以字符窗的形式返回
	 */
	public String serverAct(HttpServletRequest request) {
		String strSQL = "";
		String strMsg = "";

		String strAction = request.getParameter("action");
		String dir_id = request.getParameter("dir_id");
		if (strAction.equals("delete")) {
			String temSql = "select count(*) as  num from tab_vercon_file where dir_id="
					+ dir_id;
			PrepareSQL psql = new PrepareSQL(temSql);
	    	psql.getSQL();
			Cursor cursor = DataSetBean.getCursor(temSql);
			Map fields = cursor.getNext();

			int num = Integer.parseInt((String) fields.get("num"));
			if (num > 0) {
				strMsg = "此文件服务器上保存有文件，不能删除！";
			} else {
				strSQL = "delete from tab_file_server where dir_id=" + dir_id;
			}
		} else {

			String gather_id = request.getParameter("gather_id");
			String server_name = request.getParameter("server_name");
			String inner_URL = request.getParameter("inner_url");
			String outter_URL = request.getParameter("outter_url");
			String server_dir = request.getParameter("server_dir");
			String access_user = request.getParameter("access_user");
			String access_passwd = request.getParameter("access_passwd");
			String file_type = request.getParameter("file_type");
			if (strAction.equals("add")) {
				String tmpSql = "select count(*) as num from tab_file_server where server_name='"
						+ server_name + "'";
				PrepareSQL psql2 = new PrepareSQL(tmpSql);
		    	psql2.getSQL();
				Cursor cursor = DataSetBean.getCursor(tmpSql);
				Map fields = cursor.getNext();

				int num = Integer.parseInt((String) fields.get("num"));
				if (num > 0) {
					strMsg = "服务器名重复，请重命名！";
				} else {
					long l_dir_id = DataSetBean.getMaxId("tab_file_server",
							"dir_id");
					strSQL = "insert into tab_file_server(dir_id,gather_id,server_name,inner_url,outter_url,server_dir,access_user,access_passwd,file_type) values("
							+ l_dir_id
							+ ","
							+ "'"
							+ gather_id
							+ "',"
							+ "'"
							+ server_name
							+ "','"
							+ inner_URL
							+ "','"
							+ outter_URL
							+ "','"
							+ server_dir
							+ "','"
							+ access_user
							+ "','"
							+ access_passwd
							+ "',"
							+ file_type + ")";
					strSQL = strSQL.replaceAll(",,", ",null,");
					strSQL = strSQL.replaceAll(",\\)", ",null)");
				}
			} else {
				strSQL = "update tab_file_server set gather_id='" + gather_id
						+ "',server_name='" + server_name + "',inner_url='"
						+ inner_URL + "',outter_url='" + outter_URL
						+ "',server_dir='" + server_dir + "',access_user='"
						+ access_user + "',access_passwd='" + access_passwd
						+ "',file_type=" + file_type + " where dir_id="
						+ dir_id;
			}
		}
		if (!strSQL.equals("")) {
			PrepareSQL psql3 = new PrepareSQL(strSQL);
	    	psql3.getSQL();
			int iCode = DataSetBean.executeUpdate(strSQL);
			if (iCode > 0) {
				strMsg = "文件服务器操作成功！";
			} else {
				strMsg = "文件服务器操作失败，请返回重试或稍后再试！";
			}

		}
		return strMsg;
	}

	/**
	 * 查询文件服务器
	 * 
	 * add by lizhaojun
	 * 
	 * @param null
	 * 
	 * @return cursor 将操作结果以cursor窗的形式返回
	 */
	public Cursor getCursor() {
		String strSQL = "select * from tab_file_server";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL = "select inner_url, server_dir, dir_id, access_user, access_passwd, outter_url, gather_id, server_name, file_type " +
					"from tab_file_server";
		}
		PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(strSQL);
		return cursor;
	}

	/**
	 * 查询服务器列表
	 * 
	 * add by lizhaojun
	 * 
	 * @param request
	 * 
	 * @return String 将操作结果以html的形式返回
	 */
	public String getHtml(HttpServletRequest request) {

		String server_name = request.getParameter("server_name");
		String gather_id = request.getParameter("gather_id");
		String serverIp = request.getParameter("serverIp");
		String server_type = request.getParameter("server_type");
		String Sql = "select * from tab_file_server where 1=1";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			Sql = "select file_type, server_name, server_dir, access_user, access_passwd, inner_url, outter_url, " +
					"dir_id from tab_file_server where 1=1";
		}

		if (server_name != null && !server_name.equals("")) {
			Sql += " and server_name like '%" + server_name + "%'";
		}
		if (!gather_id.equals("-1")) {
			Sql += " and gather_id ='" + gather_id + "'";
		}

		if (serverIp != null && !serverIp.equals("")) {
			Sql += " and (inner_url like'%" + serverIp + "%'"
					+ " or outter_url like'%" + serverIp + "%')";
		}
		if (!server_type.equals("-1")) {
			Sql += " and  file_type =" + server_type;
		}

		String strData = "<TABLE border=0 cellspacing=1 bgcolor=#999999 cellpadding=2 width='100%'>"
				+ "<TR><TH>名称</TH><TH>文件类型</TH><TH>路径</TH><TH>用户名</TH><TH>密码</TH><TH>服务器url</TH><TH>操作</TH></TR>";

		Cursor cursor = null;
		Map fields = null;
		PrepareSQL psql = new PrepareSQL(Sql);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(Sql);
		fields = cursor.getNext();
		if (fields == null) {
			strData += "<TR><TD class=column1 align=left colspan=8>查询没有记录！</TD></TR>";
		} else {
			while (fields != null) {
				int intserverType = Integer.parseInt((String) fields
						.get("file_type"));
				String strfileType = null;
				if (intserverType == 1) {
					strfileType = "版本文件";
				} else if (intserverType == 2) {
					strfileType = "配置文件";
				} else {
					strfileType = "日志文件";
				}

				strData += "<TR>" + "<TD class=column1 align=center>"
						+ fields.get("server_name")
						+ "</TD>"
						+ "<TD class=column1 align=center>"
						+ strfileType
						+ "</TD>"
						+ "<TD class=column1 align=center>"
						+ fields.get("server_dir")
						+ "</TD>"
						+ "<TD class=column1 align=center>"
						+ fields.get("access_user")
						+ "</TD>"
						+ "<TD class=column1 align=center>";
				if(LipossGlobals.inArea(Global.CQDX) || LipossGlobals.inArea(Global.SXLT)){
					strData += "******";
				}
				else{
					strData += fields.get("access_passwd");
				}
						
				strData += "</TD>"
						+ "<TD class=column1 align=left>用户访问URL:"
						+ fields.get("inner_url")
						+ "<BR>设备访问URL:"
						+ fields.get("outter_url")
						+ "</TD>";
				
				// 山西联通直接调用页面的javascript方法 202004110
				if(LipossGlobals.inArea(Global.SXLT)) {
					String dir = JSONObject.toJSONString(fields);
					dir = dir.replace("\"", "\\\"");
					strData += "<TD class=column1 align=center>"
							+ 		"<a href=javascript:edit('"+ dir +"')>修改</a>"
							+ "    | <a href=javascript:del('"+ fields.get("dir_id") +"')>删除</a>"
							+ "</TD>"; 
				}else {
					strData += "<TD class=column1 align=center><a href=serverEdit.jsp?&dir_id="
							+ fields.get("dir_id")
							+ ">修改</a> | <a href=serverSave.jsp?action=delete&dir_id="
							+ fields.get("dir_id")
							+ " onclick='return delWarn();'>删除</a></TD>"; 
				}
				
				strData += "</TR>";
				
				fields = cursor.getNext();
			}

		}
		strData += "</TABLE>";

		strData = "parent.document.all(\"userTable\").innerHTML=\"" + strData
				+ "\";";
		strData = strData
				+ "parent.document.all(\"dispTr\").style.display=\"\";";
		return strData;
	}
	
	
	/**
	 * 获取某个文件服务器信息
	 * 
	 * add by lizhaojun
	 * 
	 * @param request
	 * 
	 * @return Map 将操作结果以Map的形式返回
	 */
	public Map getFileMap(HttpServletRequest request) {
		String sql = "select * from tab_file_server where dir_id="
				+ request.getParameter("dir_id");
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select inner_url, server_dir, dir_id, access_user, access_passwd, outter_url, gather_id, server_name, file_type " +
					"from tab_file_server where dir_id="
					+ request.getParameter("dir_id");
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		return DataSetBean.getRecord(sql);
	}
}
