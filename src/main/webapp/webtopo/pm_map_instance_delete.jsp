<%@ include file="../timelater.jsp"%>
<%@ page import="com.linkage.litms.common.corba.interfacecontrol.PmeeInterface" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ page contentType="text/html;charset=GBK"%>
<SCRIPT LANGUAGE="JavaScript">
<!--

//-->
</SCRIPT>
<%@ include file="../head.jsp"%>
<%
	String deltype = request.getParameter("deltype");
	String type = request.getParameter("type");
	int typeInt = 1;
	if (null != type && "".equals(type) == false)
	{
		typeInt = Integer.parseInt(type);
	}
	String name = request.getParameter("name");
	String device_id = request.getParameter("device_id");
	String expressionid = request.getParameter("expressionid");//���ܱ��ʽID
	out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
	if ("all".equals(deltype))
	{
		String delInstanceSQL = "delete from pm_map_instance where expressionid="
				+ expressionid + " and device_id='" + device_id + "'";
		String delExpressionSQL = "delete from pm_map where expressionid="
				+ expressionid + " and device_id='" + device_id + "'";
		int delResultArray[] = DataSetBean.doBatch(new String[]
		{ delInstanceSQL, delExpressionSQL });
		//int delResult0 = delResultArray[0];
		//int delResult1 = delResultArray[1];
		if (null!=delResultArray)
		{
			int retflag = PmeeInterface.GetInstance().readDevices(new String[]{ device_id });
			if (retflag == 0)
			{
				out.println("window.alert(\"���ܱ��ʽɾ���ɹ�,֪ͨ��̨�ɹ�\");");
			} else
			{
				out.println("window.alert(\"���ܱ��ʽɾ���ɹ�,֪ͨ��̨ʧ��\");");
			}			
			out.println("parent.window.location.reload();");			
		} else
		{
			out.println("window.alert(\"���ܱ��ʽɾ��ʧ��\");");
		}
	} else if ("one".equals(deltype))
	{
		String id = request.getParameter("id");//Ψһʵ��id
		String deleteInstanceSQL = "delete from pm_map_instance where id='"
				+ id + "'";
		int delResultArray[] = DataSetBean.doBatch(deleteInstanceSQL);
		int delResult = delResultArray[0];
		if (delResult == 1)
		{
			//������ʵ����pm_map_instance���Ƿ�������,���������� pm_map/�豸���ʽӳ��� �е��������ɾ��
			String queryInstanceSQL = "select count(1) as num from pm_map_instance where device_id='"
					+ device_id + "' and expressionid=" + expressionid;
			// teledb
			if (DBUtil.GetDB() == 3) {
				queryInstanceSQL = "select count(*) as num from pm_map_instance where device_id='"
						+ device_id + "' and expressionid=" + expressionid;
			}
			com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(queryInstanceSQL);
			psql.getSQL();
			Cursor querycursor = DataSetBean.getCursor(queryInstanceSQL);
			Map queryMap = querycursor.getNext();
			if (queryMap != null)
			{
				int num = Integer.parseInt((String) queryMap.get("num"));
				if (num < 1)
				{
					String delExpressionSQL = "delete from pm_map where expressionid="
							+ expressionid
							+ " and device_id='"
							+ device_id
							+ "'";
					DataSetBean.doBatch(delExpressionSQL);
				}
			}
			
			int retflag = PmeeInterface.GetInstance().readDevices(new String[]{ device_id });
			if (retflag == 0)
			{
				out.println("window.alert(\"���ܱ��ʽɾ���ɹ�,֪ͨ��̨�ɹ�\");");
			} else
			{
				out.println("window.alert(\"���ܱ��ʽɾ���ɹ�,֪ͨ��̨ʧ��\");");
			}
			out.println("parent.window.location.reload();");			
		} else
		{
			out.println("window.alert(\"����ʵ��ɾ��ʧ��\");");
		}
		
	}
	out.println("</SCRIPT>");
%>