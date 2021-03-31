<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.util.*"%>
<%@page import="com.linkage.commons.db.PrepareSQL"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%
	request.setCharacterEncoding("GBK");

	String strSQL;
	String strMsg = "";
	PrepareSQL pSQL = new PrepareSQL();
	String strAction = request.getParameter("action");

	if (strAction.equals("delete")) { // 删除操作
		String str_gather_id = request.getParameter("gather_id");
		strSQL = "delete from gw_pm_config_time where gather_id=?";
		pSQL.setSQL(strSQL);
		pSQL.setString(1, str_gather_id);
		strSQL = pSQL.getSQL();
	} else {
		//新增时
		String str_gather_id = request.getParameter("add_gather_id");
		//修改时   
		String str_hid_gather_id = request
				.getParameter("hid_gather_id");

		String str_gather_time_h = request
				.getParameter("gather_time_h");
		String str_gather_time_m = request
				.getParameter("gather_time_m");

		if (strAction.equals("add")) { // 增加操作
			String tem_Sql = "";
			strSQL = "delete from gw_pm_config_time where gather_id=?";
			pSQL.setSQL(strSQL);
			pSQL.setString(1, str_gather_id);
			DataSetBean.executeUpdate(pSQL.getSQL());

			strSQL = "insert into gw_pm_config_time(gather_id,time) values(?,?)";

			pSQL.setSQL(strSQL);
			pSQL.setString(1, str_gather_id);
			pSQL.setString(2, str_gather_time_h + ":"
					+ str_gather_time_m);

			tem_Sql = pSQL.getSQL();
			strSQL = tem_Sql;

		} else { // 修改操作
			strSQL = "update gw_pm_config_time set time=? where gather_id=?";
			String tem_Sql = "";
			pSQL.setSQL(strSQL);

			pSQL.setString(1, str_gather_time_h + ":"
					+ str_gather_time_m);
			pSQL.setString(2, str_hid_gather_id);

			tem_Sql = pSQL.getSQL();

			strSQL = tem_Sql;
		}
	}

	if (!strSQL.equals("")) {
		int[] iCode = DataSetBean.doBatch(strSQL);
		if (iCode[0] > 0) {
			strMsg = "业务性能配置操作成功！";
		} else {
			strMsg = "业务性能配置操作失败，请重试或稍后再试！";
		}
	}

	pSQL = null;
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	window.alert('<%=strMsg%>');
	parent.location.reload();
//-->
</SCRIPT>
