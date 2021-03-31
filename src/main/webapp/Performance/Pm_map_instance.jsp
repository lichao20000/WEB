<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.webtopo.*"%>
<%@page import="com.linkage.litms.common.database.*"%>
<%@page import="java.util.*"%>
<%
	String did = request.getParameter("device_id");
	String type = request.getParameter("type");
	if(null==type)
	{
		type = "";
	}
	String query = "select count(*) as num from pm_map where device_id='" + did + "'";
	String query1 = "select a.isok,a.remark,b.name,b.descr,a.expressionid  from pm_map a,pm_expression b  where a.device_id='" + did
   			 + "' and a.expressionid=b.expressionid order by a.isok desc";
    HashMap map = DataSetBean.getRecord(query);	
	String id = null;
	String name = null;
	String descr = null;
	String isok = null;
	String isokdesc = null;
	
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
function the_Name() {
	
}
//-->
</SCRIPT>

<form name="expressionFrm">
<div id="child">
<table width="98%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor=#999999>
	<tr>
		<th>表达式名称</th>
		<th>表达式描述</th>
		<th>配置状态</th>
	</tr>
	<%
	if (map != null) {
        Cursor cursor_instence = DataSetBean.getCursor(query1);
        Map myMap = cursor_instence.getNext();
		while(myMap != null){
			id=(String)myMap.get("expressionid");
			name=(String)myMap.get("name");
			descr=(String)myMap.get("descr");
			isok = (String)myMap.get("isok");
			if(isok.equals("-1")){
				isokdesc =  "未配置";
			} else if(isok.equals("0")){
				//初始化失败
				isokdesc =  "失败";
			} else{
				//初始化成功
				isokdesc =  "成功";
			}
	%>
	<tr id="expressionid<%=id %>">

		<td class=column1><%=name %></td>
		<td class=column1><%=descr %></td>
		<td class=column1 align="center"><%=isokdesc %></td>
		
	</tr>
	<%
			myMap = cursor_instence.getNext();	
		}
    }
	%>
	<tr>
		<td colspan="5" class=green_foot align="center">&nbsp;&nbsp;
		</td>
	</tr>
</table>
</div>
</form>
<SCRIPT LANGUAGE="JavaScript">
	
	parent.document.all("childDiv").innerHTML = child.innerHTML;
</SCRIPT>
