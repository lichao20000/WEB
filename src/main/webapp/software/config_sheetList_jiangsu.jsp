<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>
<jsp:useBean id="updateManage" scope="request" class="com.linkage.litms.software.UpdateManage"/>
<%
request.setCharacterEncoding("GBK");

String tmpSql = request.getParameter("tmpSql");

//��ѯ���ݿ�
Cursor cursor = (Cursor)updateManage.excSheetList(tmpSql);
Map fields = cursor.getNext();

Map resultMap = updateManage.getResultDesc();
Map deviceMap = updateManage.getDeviceInfo();
%>

<HTML>
<BODY>&nbsp; 
<%@ include file="../head.jsp"%>

<div id="idList">
<TABLE width="100%" height="30" border="0" cellpadding="0"
	cellspacing="0" class="green_gargtd">
	<TR>
		<TD width="162" align="center" class="title_bigwhite">���ڲ���...</TD>
	</TR>
</TABLE>
<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
	<TR>
		<TD bgcolor=#999999>
		<table border=0 cellspacing=1 cellpadding=2 width="100%" id="idTable"
			oncontextmenu="return false;">
			<tr>
				<th width="" nowrap>��������</th>
				<th width="" nowrap>�豸����</th>
				<th width="" nowrap>�豸���к�</th>
				<th width="" nowrap>�ƶ�ʱ��</th>
				<th width="" nowrap>�Ƿ����</th>
				<th width="" nowrap>ִ�н��</th>
				<th width="" nowrap>����</th>
			</tr>
			<%

            if (fields != null) {
                while (fields != null) {
                    
                    String device_id = (String)fields.get("device_id");
                    String deviceInfo = (String)deviceMap.get(device_id);
                    
                    //�豸��Ϣ OUI��serialnumber
                    String oui = "";
                    String device_serialnumber = "";
                    String device_model = "";
                    if (deviceInfo != null){
                    	String[] deviceInfoList = deviceInfo.split("#");
                    	if (deviceInfo != null && deviceInfoList.length > 2){
                    		oui = deviceInfoList[0];
                    		device_serialnumber = deviceInfoList[1];
                    		device_model = deviceInfoList[2];
                    	}
                    } 
                    
                    //��ʼʱ��
                    String task_time = (String)fields.get("task_time");
                    DateTimeUtil task_date = new DateTimeUtil();
                    if (task_time != null && !"".equals(task_time)){
                    	task_date = new DateTimeUtil(Long.parseLong(task_time)*1000);
                    }
                    
                    String is_over = (String)fields.get("is_over");
                    String is_over_message = "";
                    if (is_over != null && "1".equals(is_over)){
                    	is_over_message = "�����";
                    }
                    else{
                    	is_over_message = "δ���";
                    }
                    
                    String ex_result = (String)resultMap.get(fields.get("ex_result"));
                    if (ex_result == null){
                    	ex_result = "";
                    }
                    
                    String task_name = (String)fields.get("task_name");
                    
                    out.println("<tr bgcolor=#ffffff value='" + is_over + "'>");
                    out.println("<td class=column><a href ='#' onclick=showDetail('" 
                    			+ fields.get("task_id") + "','" + task_name
                    			+ "')>" + task_name + "</a></td>");
                    out.println("<td class=column>" + device_model + "(" + oui + ")" + "</td>");
                    out.println("<td class=column>" + device_serialnumber + "</td>");
                    out.println("<td class=column>" + task_date.getLongDate() + "</td>");
                    out.println("<td class=column>" + is_over_message + "</td>");
                    out.println("<td class=column>" + ex_result + "</td>");
                    out.println("<td class=column><a href ='#' onclick=showDetail('" 
                    			+ fields.get("task_id") + "','" + fields.get("task_name") 
                    			+ "','" + oui + "','" + device_serialnumber + "')>�鿴�豸����" + "</td>");
                    
                    out.println("</tr>");

                    fields = cursor.getNext();
                }
            } else {
                out.println("<tr bgcolor='#ffffff' ><td align=center colspan=9>û�й�����¼</td></tr>");
            }

        %>
		</table>
		</TD>
	</TR>
</TABLE>
</div>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.idList.innerHTML = idList.innerHTML;
parent.closeMsgDlg();

//-->
</SCRIPT>
</BODY>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
</HTML>
