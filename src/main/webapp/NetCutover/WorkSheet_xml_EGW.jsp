<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.resource.*"%>
<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManage"/>
<%
request.setCharacterEncoding("GBK");

String str_polltime = request.getParameter("polltime");

FileSevice fileSevice = new FileSevice();
Map serviceMap = fileSevice.getServiceMap();
//��ѯ���ݿ�
Cursor cursor = sheetManage.getSheetList(request);
Map fields = cursor.getNext();

Map cityMap = com.linkage.litms.common.util.CommonMap.getCityMap();

%>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<HTML>

<BODY>
<%
	if (str_polltime != null) {
                int polltime = Integer.parseInt(str_polltime) * 60;
                out.println("<meta http-equiv=\"refresh\" content=\""
                        + polltime + "\">");
    }
%>
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
				<th width="" nowrap>ҵ������</th>
				<th width="" nowrap>�û��ʻ�</th>
				<th width="" nowrap>�ͻ�ID</th>
				<th width="" nowrap>ִ��״̬</th>
				<th width="" nowrap>ִ�н��</th>
				<!-- <th width="" nowrap>�������</th> -->
				<th width="" nowrap>��ʼʱ��</th>
				<th width="" nowrap>����ʱ��</th>
				<th nowrap>ʧ��ԭ������</th>
			</tr>
			<%

            String[] arrStyle = new String[11];
            arrStyle[0] = "class=trOutgreen onmouseover='this.className=\"trOutgreen\"' onmouseout='this.className=\"trOutgreen\"'";
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
                String device_id = (String)fields.get("device_id");
                //String[] userInfo = sheetManage.getUserInfoBydeviceID(device_id);
                
                String tmp;
                String tmpValue;
                int iStatus = 0;
                while (fields != null) {
                	tmp = (String)fields.get("exec_status");
					if ("-1".equals(tmp)) {
						iStatus = 8;
					} else if("0".equals(tmp)){
                		iStatus = 0;
                	}else{
                		tmp = (String)fields.get("fault_code");
                		if("1".equals(tmp)){
                			iStatus = 2;
                		}else if("-1".equals(tmp) || "-2".equals(tmp) || "-3".equals(tmp) || "-4".equals(tmp) || "-5".equals(tmp)){
                			iStatus = 3;
                		}else{
                			iStatus = 9;
                		}
                	}
                    tmp = fields.get("sheet_id") + "," + fields.get("receive_time") + "," + fields.get("gather_id");
					tmpValue = (String)fields.get("exec_status");
					
					if ("-1".equals(tmpValue)){
						tmpValue = "2";
					}
					else if("1".equals(tmpValue)){
						tmpValue = (String)fields.get("fault_code");
					}
                    out.println("<tr "
                                    + arrStyle[iStatus]
                                    + " ondblclick=doDbClick(this) title='˫������ʾ������ϸ��Ϣ' oncontextmenu=\"showmenuie5()\" parames='"
                                    + tmp + "' value='"
                                    + tmpValue
                                    + "' onclick=doClick(this)>");
                    out.println("<td><nobr>" + fields.get("sheet_id")
                            + "</nobr></td>");
                    tmp = (String) fields.get("city_id");
                    out.println("<td><nobr>" + cityMap.get(tmp)
                            + "</nobr></td>");
					tmp =  (String)fields.get("service_id");
                    out.println("<td><nobr>" + serviceMap.get(tmp)
                            + "</nobr></td>");
					out.println("<td><nobr>" + fields.get("username")
                            + "</nobr></td>");
                    /*
                    if (userInfo != null && userInfo.length > 2 && userInfo[1] != null){
                    	out.println("<td><nobr>" + userInfo[1]
                    		+ "</nobr></td>");
                    }
                    else{
                    	out.println("<td><nobr></nobr></td>");
                    }*/
                    out.println("<td><nobr>" + fields.get("customer_id")
                            + "</nobr></td>");
					tmp = (String) fields.get("exec_status");
					if(tmp.equals("-1")){
						tmp="�ȴ�ִ��";
					} else if(tmp.equals("0")){
						tmp="����ִ��";
					}else{
						tmp="ִ�����";
					}
					out.println("<td><nobr>"
                            + tmp
                            + "</nobr></td>");
                    tmp = (String) fields.get("fault_code");
                    
                    if(tmp.equals("1")){
                    	tmp = "ִ�гɹ�";
					}else if(tmp.equals("0")){
						tmp = "ϵͳδ֪����";
                    }else if(tmp.equals("-1")){
                    	tmp = "�豸���Ӳ���";
                    }else if(tmp.equals("-2")){
                    	tmp = "�豸û����Ӧ";
                    }else if(tmp.equals("-3")){
                    	tmp = "ϵͳû�й���";
                    }else if(tmp.equals("-4")){
                     	tmp = "ϵͳû���豸";                   
                    }else if(tmp.equals("-5")){
                     	tmp = "ϵͳû��ģ��";                                         
                    }else if(tmp.equals("-6")){
                     	tmp = "�豸��������";                       
                    }else if(tmp.equals("-7")){
                     	tmp = "ϵͳ��������";                       
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
                out.println("<tr bgcolor='#ffffff' ><td align=center colspan=10>û�й�����¼</td></tr>");
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
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
</HTML>
