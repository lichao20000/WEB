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

	if (strAction.equals("delete")) { // ɾ������
		String str_gather_id = request.getParameter("gather_id");
		strSQL = "delete from gw_pm_config_time where gather_id=?";
		pSQL.setSQL(strSQL);
		pSQL.setString(1, str_gather_id);
		strSQL = pSQL.getSQL();
	} else {
		//����ʱ
		String str_gather_id = request.getParameter("add_gather_id");
		//�޸�ʱ   
		String str_hid_gather_id = request
				.getParameter("hid_gather_id");

		String str_gather_time_h = request
				.getParameter("gather_time_h");
		String str_gather_time_m = request
				.getParameter("gather_time_m");

		if (strAction.equals("add")) { // ���Ӳ���
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

		} else { // �޸Ĳ���
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
			strMsg = "ҵ���������ò����ɹ���";
		} else {
			strMsg = "ҵ���������ò���ʧ�ܣ������Ի��Ժ����ԣ�";
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
