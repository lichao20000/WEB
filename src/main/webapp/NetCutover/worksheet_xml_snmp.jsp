<%--
Author		: yanhj
Date		: 2002-9-24
Note		:
Modifiy		: yanhj  for sz 2006-10-20
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
Cursor cursor = sheet.getSheetList(str_city_id, str_ifcontainChild);
Map fields = cursor.getNext();

Map cityMap = com.linkage.litms.common.util.CommonMap.getCityMap();

//�б��Ƿ�����
boolean isSZ = SelectCityFilter.isSZ(curUser.getCityId());
String local_ip = request.getServerName();
%>
<%@page import="com.linkage.litms.common.filter.SelectCityFilter"%>
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
				<th width="110" nowrap>�������</th>
				<th width="70" nowrap>����</th>
			<%
			  if (isSZ) {
                out.println("<th width=\"80\" nowrap>�˿ڱ���</th>");
			  }
            %>
				<th width="80" nowrap>�û��˺�</th>
				<th width="70" nowrap>ҵ������</th>
				<th width="70" nowrap>��������</th>
				<th width="120" nowrap>����״̬</th>
				<th width="140" nowrap>����ʱ��</th>
				<th nowrap>����ִ�н������</th>
			</tr>
			<%
			//ת��ҵ������
            Map productTypeMap = com.linkage.litms.netcutover.CommonMap.getProductTypeMap();
            //ת����������
            Map servTypeMap = com.linkage.litms.netcutover.CommonMap.getServTypeMap();

            String[] arrStyle = new String[11];
            arrStyle[0] = "class=trOutyellow onmouseover='this.className=\"\"trOutyellow\"' onmouseout='this.className=\"trOutyellow\"'";
            arrStyle[1] = "class=trOutblue onmouseover='this.className=\"trOverblue\"' onmouseout='this.className=\"trOverblue\"'";
            arrStyle[2] = "class=trOut  onmouseover='this.className=\"trOver\"' onmouseout='this.className=\"trOut\"'";
            arrStyle[3] = "class=trOutred onmouseover='this.className=\"trOutred\"' onmouseout='this.className=\"trOutred\"'";
            arrStyle[4] = "class=trOutred onmouseover='this.className=\"trOutred\"' onmouseout='this.className=\"trOutred\"'";
            arrStyle[5] = "class=trOutblue onmouseover='this.className=\"trOverblue\"' onmouseout='this.className=\"trOverblue\"'";
            arrStyle[6] = "class=trOutred onmouseover='this.className=\"trOutred\"' onmouseout='this.className=\"trOutred\"'";
            arrStyle[7] = "class=trOutchense onmouseover='this.className=\"trOverchense\"' onmouseout='this.className=\"trOutchense\"'";
            arrStyle[8] = "class=trOutyellow onmouseover='this.className=\"trOutyellow\"' onmouseout='this.className=\"trOutyellow\"'";
            arrStyle[9] = "class=trOutshougong onmouseover='this.className=\"trOvershougong\"' onmouseout='this.className=\"trOutshougong\"'";
            arrStyle[10] = "class=trOutshougong onmouseover='this.className=\"trOvershougong\"' onmouseout='this.className=\"trOutshougong\"'";

            String[] wsStatus = new String[11];
            wsStatus[0] = "������";
            wsStatus[1] = "���ڴ���";
            wsStatus[2] = "����ɹ�";
            wsStatus[3] = "����ʧ��";
            wsStatus[4] = "��������";
            wsStatus[5] = "�����¼��������";
            wsStatus[6] = "����ʧ��";
            wsStatus[7] = "δ�����豸";
            wsStatus[8] = "�ȴ����º�̨";
            wsStatus[9] = "�ֹ��ص�������ɹ�";
            wsStatus[10] = "�ֹ��ص�������ʧ��";

            if (fields != null) {
                String tmp;
                int iStatus = 0;
                while (fields != null) {
                    tmp = (String) fields.get("worksheet_status");
                    if (tmp != null && !tmp.equals(""))
                        iStatus = Integer.parseInt(tmp);
                    tmp = fields.get("worksheet_id") + ","
                            + fields.get("worksheet_source") + ","
                            + fields.get("sheet_id") + ","
                            + fields.get("worksheet_status") + ","
                            + fields.get("servtype") + ","
                            + fields.get("product_id") + ","
                            + fields.get("worksheet_receive_time");

                    out.println("<tr "
                                    + arrStyle[iStatus]
                                    + " ondblclick=doDb(this) title='˫������ʾ������ϸ��Ϣ' oncontextmenu=\"showmenuie5()\" parames='"
                                    + tmp + "' value='"
                                    + fields.get("worksheet_status")
                                    + "' onclick=doClick(this)>");
                    out.println("<td><nobr>" + fields.get("sheet_id")
                            + "</nobr></td>");
                    tmp = (String) fields.get("system_id");
                    out.println("<td><nobr>" + cityMap.get(tmp)
                            + "</nobr></td>");

                    if (isSZ) {
                        String deviceencode = (String) fields.get("deviceencode");
                        if (deviceencode == null || deviceencode.equals("null"))
                            deviceencode = "";
                        out.println("<td><nobr>" + deviceencode
                                + "</nobr></td>");
                    }

                    out.println("<td><nobr>" + fields.get("username")
                            + "</nobr></td>");
                    out.println("<td><nobr>"
                            + productTypeMap.get((String) fields
                                    .get("producttype")) + "</nobr></td>");
                    out.println("<td><nobr>"
                            + servTypeMap.get((String) fields.get("servtype"))
                            + "</nobr></td>");
                    out.println("<td><nobr>" + wsStatus[iStatus]
                            + "</nobr></td>");

					out.println("<td><nobr>"
                            + fields.get("worksheet_receive_time")
                            + "</nobr></td>");

                    tmp = (String) fields.get("worksheet_error_desc");
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
