<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*" %>
<%@page import="com.linkage.commons.db.PrepareSQL"%>
<%@ page import="java.util.Map" %>

<%
String idList = request.getParameter("idList");
String titleStyle = request.getParameter("titleStyle");


String[] idArr = idList.split(",");
int index = 0;
String device_id = "";
String title = "";
String titleList = "";
String sql1 = "select device_name as title from tab_gw_device where device_id=?";

String sql = "";
if ("1".equals(titleStyle)){
	sql += "select a.username as title from tab_hgwcustomer a,tab_gw_device b where a.device_id = b.device_id and a.user_state='1' and a.serv_type_id=10 and b.device_id=?";
}
else if ("2".equals(titleStyle)){
	sql += "select a.username as title from tab_hgwcustomer a,tab_gw_device b where a.device_id = b.device_id and a.user_state='1' and a.serv_type_id=11 and b.device_id=?";
}
else if ("3".equals(titleStyle)){
	sql += "select a.username as title from tab_hgwcustomer a,tab_gw_device b where a.device_id = b.device_id and a.user_state='1' and a.serv_type_id=12 and b.device_id=?";
}
else{
	sql += "select device_name as title from tab_gw_device where device_id=?";
}

if (idArr != null){
	for(int i=0;i<idArr.length;i++){
		
		if (idArr[i] != null){
			index = idArr[i].indexOf("1/gw/");
			if (index != -1){
				device_id = idArr[i].substring(6);
				
				PrepareSQL pSQL = new PrepareSQL(sql);
				pSQL.setString(1,device_id);
				Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
				Map fields = cursor.getNext();
				
				if (fields != null){
					title = (String)fields.get("title");
				}
				else{
					PrepareSQL pSQL1 = new PrepareSQL(sql1);
					pSQL1.setString(1,device_id);
					Cursor cursor1 = DataSetBean.getCursor(pSQL1.getSQL());
					Map fields1 = cursor1.getNext();
					
					if (fields1 != null){
						title = (String)fields1.get("title");	
					}
				}
				
				if (title != null && !"".equals(title)){
					if ("".equals(titleList)){
						titleList = idArr[i] + "|||" + title;
					}
					else{
						titleList = titleList + "," + idArr[i] + "|||" + title;
					}
				}
			}
		}
	}
}

out.println(titleList);
 %>


