<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.common.database.Cursor, com.linkage.litms.common.database.DataSetBean,java.util.*"%>

<%@ include file="../timelater.jsp"%>

<SCRIPT LANGUAGE="JavaScript" id="idParentFun">
var iCode=0;
var strAction = "";
</SCRIPT>
<%
request.setCharacterEncoding("GBK");
int iCode = 0;
boolean isDeleted=false;
String strAction = request.getParameter("action");
String groupid = request.getParameter("groupid");
String groupdesc = request.getParameter("groupdesc");
String timeinterval = request.getParameter("timeinterval");
String delaythreshold = request.getParameter("delaythreshold");
String timeout = request.getParameter("timeout");
String isstat = request.getParameter("isstat");
if (isstat == null) {
	isstat = "0";
}
String warnmode = request.getParameter("warnmode");
String warnlevel = request.getParameter("warnlevel");
String timeoutwarnlevel = request.getParameter("timeoutwarnlevel");
String gather_id = request.getParameter("gather");
String strSQL = "";
// 取得新的userid
//long groupid = DataSetBean.getMaxId("pping_group_conf", "groupid");
//str_vpnid = String.valueOf(vpnid);

if (strAction.equals("add")) {
	strSQL = "insert into pping_group_conf (groupid,groupdesc,timeinterval,"
          +"delaythreshold,timeout,isstat,warnmode,warnlevel,timeoutwarnlevel,gather_id) values ("
		  + Integer.parseInt(groupid) + ",'" + groupdesc + "',"+ Integer.parseInt(timeinterval) + ","
		  + Integer.parseInt(delaythreshold) + "," + Integer.parseInt(timeout) + "," +Integer.parseInt(isstat) + "," + Integer.parseInt(warnmode) + "," + Integer.parseInt(warnlevel) + "," + Integer.parseInt(timeoutwarnlevel) + ",'" + gather_id + "')";

} else {
	strSQL = "update pping_group_conf set groupdesc='"+groupdesc+"',timeinterval="+Integer.parseInt(timeinterval)+",delaythreshold="+Integer.parseInt(delaythreshold)+",timeout="+Integer.parseInt(timeout)+",isstat="+Integer.parseInt(isstat)+",warnmode="+Integer.parseInt(warnmode)+",warnlevel="+Integer.parseInt(warnlevel)+",timeoutwarnlevel="+Integer.parseInt(timeoutwarnlevel)+",gather_id='"+gather_id+"' where groupid="+Integer.parseInt(groupid);
}
 if (!strSQL.equals("")) {
	iCode = DataSetBean.executeUpdate(strSQL);
 }
%>

<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	iCode = '<%=iCode%>';
	strAction = '<%=strAction%>';
	if (iCode>0) {
		if (strAction=="add") {
			alert("保存成功");
			parent.refreshPage();
		} else {
			alert("修改成功");
			//parent.refreshPage();
		}
		
	} else {
		if (strAction=="add") {
			alert("保存失败!");
		} else {
			alert("修改失败");
		}
	}
//-->
</SCRIPT>

<%@ include file="../toolbar.jsp"%>

<SCRIPT LANGUAGE="JavaScript">
<!--
//-->
</SCRIPT>
<%@ include file="../foot.jsp"%>