<%--
Author		: lizhaojun
Date		: 2007-4-20
Note		:
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.netcutover.ServiceAct"%>
<%@ page import="com.linkage.module.gwms.util.StringUtil"%>
<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManage"/>
<jsp:useBean id="faultCode" scope="request" class="com.linkage.module.gwms.Global"/>
<%
request.setCharacterEncoding("GBK");

String str_polltime = request.getParameter("polltime");

String serviceType = request.getParameter("serviceType");

// 业务类型，名称Map
Map serv_typeMap = ServiceAct.getGwServTypeMap();
// 操作类型，名称Map
Map oper_typeMap = ServiceAct.getGwOperTypeMap();
//查询数据库
Cursor cursor = sheetManage.getSheetList(request);
Map fields = cursor.getNext();

Map cityMap = com.linkage.module.gwms.dao.tabquery.CityDAO.getCityIdCityNameMap();

%>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<HTML>

<BODY>
<%
	if (str_polltime != null) {
                int polltime = StringUtil.getIntegerValue(str_polltime) * 60;
                out.println("<meta http-equiv=\"refresh\" content=\""
                        + polltime + "\">");
    }
%>
<%@ include file="../head.jsp"%>

<div id="idList">
<TABLE width="100%" height="30" border="0" cellpadding="0"
	cellspacing="0" class="green_gargtd">
	<TR>
		<TD width="162" align="center" class="title_bigwhite">工单列表</TD>
	</TR>
	<tr>
		<td>
			<table width="100%" height="10" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="right">
						<IMG SRC="../images/excel.gif" WIDTH="16" HEIGHT="16" BORDER="0" onclick="exportExcel()" ALT="导出到EXCEL" style="cursor:hand">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</TABLE>
<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
	<TR>
		<TD bgcolor=#999999>
		<table border=0 cellspacing=1 cellpadding=2 width="100%" id="idTable"
			oncontextmenu="return false;">
			<tr>
				<th width="" nowrap>工单编号</th>
				<th width="" nowrap>属地</th>
				<%
					String colspan="10";
					if( serviceType!= null &&  serviceType.equals("1")){
						colspan= "9";
				%>
				<th width="" nowrap>维护类型</th>
				<th width="" nowrap>设备信息</th>				
				<%
					}else {							
				 %>				
				<th width="" nowrap>业务类型</th>
				<th width="" nowrap>操作类型</th>
				<th width="" nowrap>用户帐户</th>
				<% }%>	
				
				<th width="" nowrap>执行状态</th>
				<th width="" nowrap>执行结果</th>
				<th width="" nowrap>开始时间</th>
				<th width="" nowrap>结束时间</th>
				<th nowrap>失败原因描述</th>
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
                String tmp;
                String tmpValue;
                int iStatus = 0;
                while (fields != null) {
                	tmp = StringUtil.getStringValue(fields.get("exec_status"));
					if ("-1".equals(tmp)) {
						iStatus = 8;
					} else if("0".equals(tmp)){
                		iStatus = 0;
                	}else{
                		tmp = StringUtil.getStringValue(fields.get("fault_code"));
                		if("1".equals(tmp)){
                			iStatus = 2;
                		}else if("-1".equals(tmp) || "-2".equals(tmp) || "-3".equals(tmp) || "-4".equals(tmp) || "-5".equals(tmp)){
                			iStatus = 3;
                		}else{
                			iStatus = 9;
                		}
                	}
                    tmp = fields.get("sheet_id") + "," + fields.get("receive_time") + "," + fields.get("gather_id");
					tmpValue = StringUtil.getStringValue(fields.get("exec_status"));
					
					if ("-1".equals(tmpValue)){
						tmpValue = "2";
					}
					else if("1".equals(tmpValue)){
						tmpValue = StringUtil.getStringValue(fields.get("fault_code"));
					}
                    out.println("<tr "
                                    + arrStyle[iStatus]
                                    + " ondblclick=doDbClick(this) title='双击会显示工单详细信息' oncontextmenu=\"showmenuie5()\" parames='"
                                    + tmp + "' value='"
                                    + tmpValue
                                    + "' onclick=doClick(this)>");
                    out.println("<td><nobr>" + fields.get("sheet_id")
                            + "</nobr></td>");
                    tmp = StringUtil.getStringValue( fields.get("city_id"));
                    out.println("<td><nobr>" + cityMap.get(tmp)
                            + "</nobr></td>");
					
					if( serviceType!= null && serviceType.equals("1")){					
                    	tmp =  StringUtil.getStringValue(fields.get("service_name"));
	                    out.println("<td><nobr>" + tmp
	                            + "</nobr></td>"); 
	                    tmp =  StringUtil.getStringValue(fields.get("oui")) + "-" + StringUtil.getStringValue(fields.get("device_serialnumber"));
	                    out.println("<td><nobr>" + tmp
	                            + "</nobr></td>"); 
                    } else {

	                    tmp =  StringUtil.getStringValue(fields.get("serv_type_id"));
	                    out.println("<td><nobr>" + serv_typeMap.get(tmp)
	                            + "</nobr></td>");
						tmp =  StringUtil.getStringValue(fields.get("oper_type_id"));
	                    out.println("<td><nobr>" + oper_typeMap.get(tmp)
	                            + "</nobr></td>"); 
	                    out.println("<td><nobr>" + fields.get("username")
                            + "</nobr></td>");
                    }                          

					tmp = StringUtil.getStringValue( fields.get("exec_status"));
					if(tmp.equals("-1")){
						tmp="等待执行";
					} else if(tmp.equals("0")){
						tmp="正在执行";
					}else{
						tmp="执行完成";
					}
					out.println("<td><nobr>"
                            + tmp
                            + "</nobr></td>");
					tmp = StringUtil.getStringValue( fields.get("fault_code"));
					int fault_code=StringUtil.getIntegerValue(tmp);
					tmp= faultCode.G_Fault_Map.get(fault_code).getFaultReason(); 
              /*   if(tmp.equals("1")){
                   	tmp = "执行成功";
					}else if(tmp.equals("0")){
						tmp = "系统未知错误";
                    }else if(tmp.equals("-1")){
                    	tmp = "设备连接不上";
                   }else if(tmp.equals("-2")){
                    	tmp = "设备没有响应";
                    }else if(tmp.equals("-3")){
                    	tmp = "系统没有工单";
                    }else if(tmp.equals("-4")){
                     	tmp = "系统没有设备";                   
                    }else if(tmp.equals("-5")){
                     	tmp = "系统没有模板";                                         
                    }else if(tmp.equals("-6")){
                     	tmp = "设备正被操作";                       
                    }else if(tmp.equals("-7")){
                     	tmp = "系统参数错误";                       
                    }      
              **/
					out.println("<td><nobr>"
                            + tmp
                            + "</nobr></td>"); 
                    
                    tmp = StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",Long.parseLong(StringUtil.getStringValue(fields.get("start_time"))));                          
					out.println("<td><nobr>"
                            +  tmp
                            + "</nobr></td>"); 
                    tmp = StringUtil.getStringValue( fields.get("end_time"));
                    if(tmp!=null && !tmp.equals("")){            
                    	tmp = StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",Long.parseLong(StringUtil.getStringValue(fields.get("end_time"))));         
					}
					out.println("<td><nobr>"
                            +  tmp
                            + "</nobr></td>");                        

                    tmp = StringUtil.getStringValue( fields.get("fault_desc"));
                    if (tmp == null || tmp.equals("null"))
                        tmp = "";
                    out.println("<td><nobr>" + tmp + "</nobr></td>");
                    out.println("</tr>");

                    fields = cursor.getNext();
                }
            } else {
                out.println("<tr bgcolor='#ffffff' ><td align=center colspan=" + colspan + ">没有工单记录</td></tr>");
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
