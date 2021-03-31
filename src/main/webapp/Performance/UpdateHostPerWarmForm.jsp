<%@ include file="../timelater.jsp"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.*"%>
<%@page import="com.linkage.litms.common.util.CommonMap"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ page contentType="text/html;charset=GBK"%>

<%
request.setCharacterEncoding("GBK");
//DataSetBean db = new DataSetBean();
String str_hostip = request.getParameter("hostip");
String str_gather_id = request.getParameter("gather_id");
String str_gather_name = request.getParameter("gather_name");

Map gatherMap = CommonMap.getGatherMap();
str_gather_name = (String)gatherMap.get(str_gather_id);

String sql = "select * from tab_attrwarconf where gather_id='"+str_gather_id+"' and hostip='"+str_hostip+"' order by attr_id";
// teledb
if (DBUtil.GetDB() == 3) {
	sql = "select warmvalue, s_warmvalue " +
			"from tab_attrwarconf where gather_id='"+str_gather_id+"' and hostip='"+str_hostip+"' order by attr_id";
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
psql.getSQL();
Cursor cursor = DataSetBean.getCursor(sql);
Map fields = cursor.getNext();

String attr_id1 = "";
String attr_id2 = "";
String attr_id3 = "";
String attr_id1_s = "";
String attr_id2_s = "";
String attr_id3_s = "";

while(fields!=null){
	if(fields.get("attr_id").equals("1")){
		attr_id1 = (String)fields.get("warmvalue");
		attr_id1_s = (String)fields.get("s_warmvalue");
	}
	else if(fields.get("attr_id").equals("2")){
		attr_id2 = (String)fields.get("warmvalue");
		attr_id2_s = (String)fields.get("s_warmvalue");
	}
	else if(fields.get("attr_id").equals("3")){
		attr_id3 = (String)fields.get("warmvalue");
		attr_id3_s = (String)fields.get("s_warmvalue");
	}
	
	fields = cursor.getNext();
}
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.document.frm.attr_id1.value='<%=attr_id1%>';
parent.document.frm.attr_id2.value='<%=attr_id2%>';
parent.document.frm.attr_id3.value='<%=attr_id3%>';
parent.document.frm.attr_id1_s.value='<%=attr_id1_s%>';
parent.document.frm.attr_id2_s.value='<%=attr_id2_s%>';
parent.document.frm.attr_id3_s.value='<%=attr_id3_s%>';
parent.document.frm.action.value='update';
parent.actLabel.innerHTML = '±à¼­';
parent.hostLabel.innerHTML = '¡¼<%=str_hostip%>¡½';

parent.document.all.addgatherLabel.style.display="none";
parent.document.all.divHostIP.style.display="none";
	
parent.document.all.gatherLabel.style.display="";
parent.document.all.hostIPLabel.style.display="";

parent.gatherLabel.innerHTML = '<%=str_gather_name%>';
parent.hostIPLabel.innerHTML = '<%=str_hostip%>';

parent.document.frm.hid_hostip.value='<%=str_hostip%>';
parent.document.frm.hid_gather_id.value='<%=str_gather_id%>';
//-->
</SCRIPT>