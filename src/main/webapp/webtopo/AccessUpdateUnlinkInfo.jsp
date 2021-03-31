<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import ="com.linkage.litms.common.database.Cursor, com.linkage.litms.common.database.DataSetBean,java.util.*"%>

<%@ include file="../timelater.jsp"%>
<SCRIPT LANGUAGE="JavaScript" id="idParentFun">
var result;
</SCRIPT>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<%
	String link_auto_id = request.getParameter("link_auto_id");
	String vpn_link_id = request.getParameter("vpn_link_id");
	//String vpn_id = request.getParameter("vpn_id");
	String strSQL_1 =
        "update vpn_auto_link set vpn_link_id="+Integer.parseInt(vpn_link_id)+" where link_auto_id="+Integer.parseInt(link_auto_id);
	int result = DataSetBean.executeUpdate(strSQL_1);
	if(result > 0){
		%>
		<SCRIPT LANGUAGE="JavaScript">
		<!--
			result = <%=result%>;
			alert("关联操作成功!");
			parent.showLinkInfo("<%=vpn_link_id%>");
		//-->
		</SCRIPT>
		<%
	} else {
	%>
		<SCRIPT LANGUAGE="JavaScript">
		<!--
			alert("关联操作失败!");
		//-->
		</SCRIPT>
	<%
	}
	%>

