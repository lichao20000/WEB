<%--
Author		: lizhaojun
Date		: 2007-4-20
Note		:
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.resource.*"%>

<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManage"/>
<%
request.setCharacterEncoding("GBK");

String tmpSql = request.getParameter("tmpSql");
String service_id = request.getParameter("service_id");
String username = request.getParameter("username");
String passwd = request.getParameter("passwd");



FileSevice fileSevice = new FileSevice();
Map serviceMap = fileSevice.getServiceMap();

//��ѯ���ݿ�
Cursor cursor = (Cursor)sheetManage.excSheetList(tmpSql);
Map fields = cursor.getNext();

Map gatherMap = (Map)com.linkage.litms.common.util.CommonMap.getGatherMap();

%>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<HTML>
<BODY>
<meta http-equiv="refresh" content="10">;
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
				<th width="" nowrap>�������</th>
				<th width="" nowrap>�ɼ���</th>
				<th width="" nowrap>ҵ������</th>
				<th width="" nowrap>�û��ʻ�</th>
				<th width="" nowrap>ִ��״̬</th>
				<th width="" nowrap>ִ�н��</th>
				<!-- <th width="" nowrap>�������</th> -->
				<th width="" nowrap>��ʼʱ��</th>
				<th width="" nowrap>����ʱ��</th>
				<th nowrap>ʧ��ԭ������</th>
			</tr>
			<%

            String[] arrStyle = new String[11];
            arrStyle[0] = "class=trOutyellow onmouseover='this.className=\"trOutyellow\"' onmouseout='this.className=\"trOutyellow\"'";
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

            if (fields != null) {
                String tmp;
                String tmpValue;
                int iStatus = 0;
                while (fields != null) {
                	tmp = (String)fields.get("exec_status");
                	if(tmp.equals("0")){
                		iStatus = 0;
                	}else{
                		tmp = (String)fields.get("fault_code");
                		if(tmp.equals("1")){
                			iStatus = 2;
                		}else if(tmp.equals("-1") || tmp.equals("-2") || tmp.equals("-3") || tmp.equals("-4") || tmp.equals("-5")){
                			iStatus = 3;
                		}else{
                			iStatus = 9;
                		}
                	}
                    tmp = fields.get("sheet_id") + "," + fields.get("receive_time") + "," + fields.get("gather_id");
					tmpValue = (String)fields.get("exec_status");
					if(tmpValue.equals("1")){
						tmpValue = (String)fields.get("fault_code");
					}
                    out.println("<tr "
                                    + arrStyle[iStatus]
                                    + " ondblclick=doDbClick(this) title='˫������ʾ������ϸ��Ϣ'  parames='"
                                    + tmp + "' value='"
                                    + tmpValue
                                    + "'>");
                    out.println("<td><nobr>" + fields.get("sheet_id")
                            + "</nobr></td>");
                    tmp = (String) fields.get("gather_id");
                    out.println("<td><nobr>" + gatherMap.get(tmp)
                            + "</nobr></td>");
					tmp =  (String)fields.get("service_id");
                    out.println("<td><nobr>" + serviceMap.get(tmp)
                            + "</nobr></td>");
                    out.println("<td><nobr>" + fields.get("username")
                            + "</nobr></td>");
					tmp = (String) fields.get("exec_status");
					if(tmp.equals("0")){
						tmp="����ִ��....";
					}else{
						tmp="ִ�����";
					}
					out.println("<td><nobr>"
                            + tmp
                            + "</nobr></td>");
                                 
                    tmp = (String) fields.get("fault_code");
                    
                    if(tmp.equals("1")){
                    	tmp = "ִ�гɹ�";
                    	if ("1021".equals(service_id)) {
                    		//������ŽӸ�·��ҵ����ִ�гɹ����ٸ����û���Ϣ gsj
                    		sheetManage.updateHgwcustomer(username, passwd);
                    	}
					}else if(tmp.equals("0")){
						tmp = "δ֪����";
                    }else if(tmp.equals("-1")){
                    	tmp = "���Ӳ���";
                    }else if(tmp.equals("-2")){
                    	tmp = "���ӳ�ʱ";
                    }else if(tmp.equals("-3")){
                    	tmp = "û�й���";
                    }else if(tmp.equals("-4")){
                     	tmp = "û���豸";                   
                    }else if(tmp.equals("-5")){
                     	tmp = "û��ģ��";                                         
                    }else if(tmp.equals("-6")){
                     	tmp = "�豸��æ";                       
                    }else if(tmp.equals("-7")){
                     	tmp = "��������";                       
                    } 
 					out.println("<td><nobr>"
                            + tmp
                            + "</nobr></td>"); 
                                               
                    tmp = StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",Long.parseLong((String)fields.get("start_time")));                          
					out.println("<td><nobr>"
                            +  tmp
                            + "</nobr></td>"); 
                    tmp = (String) fields.get("end_time");
                    if(tmp!=null && !tmp.equals("")){            
                    	tmp = StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",Long.parseLong((String)fields.get("end_time")));         
					}
					out.println("<td><nobr>"
                            +  tmp
                            + "</nobr></td>");                        

                    tmp = (String) fields.get("fault_desc");
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
//parent.wsState();

//-->
</SCRIPT>
</BODY>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
</HTML>
