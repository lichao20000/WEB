<%--
Author		: zhaixf
Date		: 2008-3-20
Note		:
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.common.database.*,com.linkage.litms.netcutover.*,java.util.*" %>
<%
request.setCharacterEncoding("GBK");

String str_lms = request.getParameter("start");
String str_polltime = request.getParameter("polltime");
String str_filter = request.getParameter("filter");

//----------------------���ع��� add by YYS 2006-9-24 ----------------
String str_ifcontainChild = request.getParameter("ifcontainChild");
String str_city_id = request.getParameter("city_id");
if (str_city_id == null) {
	str_city_id = user.getCityId();
	str_ifcontainChild = "1";
}
//--------------------------------------------------------------------

long lms = Long.parseLong(str_lms);

ObjectWorkSheet objSheet = new ObjectWorkSheet();
objSheet.setWorksheet_status(str_filter);
objSheet.setGather_id(curUser.getUserProcesses());

//��ѯ���ݿ�
long m_AreaId = user.getAreaId();

SheetList sheet = new SheetList(lms, 1, -1, objSheet, m_AreaId);
Cursor cursor = sheet.getBnetSheetList(str_city_id, str_ifcontainChild);
Map fields = cursor.getNext();
Map cityMap = com.linkage.litms.common.util.CommonMap.getCityMap();

%>
<HTML>
<BODY>
<%if (str_polltime != null) {
                int polltime = Integer.parseInt(str_polltime) * 60;
                out.println("<meta http-equiv=\"refresh\" content=\""
                        + polltime + "\">");
            }%>
<%@ include file="../head.jsp"%>

<div id="idList">
<TABLE width="100%" height="30" border="0" cellpadding="0"
	cellspacing="0" class="green_gargtd">
	<TR>
		<TD width="162" align="center" class="title_bigwhite">�����б�</TD>
	</TR>
</TABLE>
<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
	<TR>
		<TD bgcolor=#999999>
		<table border=0 cellspacing=1 cellpadding=2 width="100%" id="idTable"
			oncontextmenu="return false;">
			<tr>
				<th width="" nowrap>�������</th>
				<th width="" nowrap>����</th>
				<th width="" nowrap>�ͻ�����</th>
				<th width="" nowrap>ҵ������</th>
				<th width="" nowrap>��������</th>
				<th width="" nowrap>����״̬</th>
				<th width="" nowrap>����ʱ��</th>
				<th width="" nowrap>��������</th>
				<th width="" nowrap>ִ�н��</th>
			</tr>
			<%
            if (fields != null) {
                String tmp;
                while (fields != null) {
                    out.println("<tr value='"
                                    + fields.get("result")+"' bgcolor='#ffffff'>");
                    out.println("<td><nobr>" 
                    		+ fields.get("id")
                            + "</nobr></td>");
                    out.println("<td><nobr>" 
                    		+ cityMap.get((String) fields.get("city_id"))
                            + "</nobr></td>");
                    out.println("<td><nobr>" 
                    		+ fields.get("customer_name")
                            + "</nobr></td>");
					out.println("<td><nobr>"
                            + (fields.get("product_spec_id")==null ? "" : fields.get("product_spec_id"))
                            + "</nobr></td>");
					out.println("<td><nobr>"
                            + fields.get("type")
                            + "</nobr></td>");
					if(fields.get("status").equals("0")){
						out.println("<td><nobr>��ִ��</nobr></td>");
					}else{
						out.println("<td><nobr>��ִ��</nobr></td>");
					}
					out.println("<td><nobr>"
                            + fields.get("receive_date")
                            + "</nobr></td>");
					out.println("<td><nobr>"
                            + (fields.get("param")==null ? "" : fields.get("param"))
                            + "</nobr></td>");
                    tmp = (String) fields.get("result_tail");
                    if (tmp == null || tmp.equals("null"))
                        tmp = "";
                    out.println("<td><nobr>" + tmp + "</nobr></td>");
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
parent.wsState();

//-->
</SCRIPT>
</BODY>
</HTML>
