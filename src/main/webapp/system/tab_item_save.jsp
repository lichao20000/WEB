<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="systemMaintenance" scope="request" class="com.linkage.litms.system.SystemMaintenance"/>
<%
request.setCharacterEncoding("GBK");

String strAction = request.getParameter("action");


int strMsg = systemMaintenance.tabItemAct(request);
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	var _msg = '<%=strMsg%>';
	var action = '<%=strAction%>';
	if (_msg > 0) {
		if(action=="add") {
			alert("添加操作成功");
			parent.reload();
		}
/**		if(action=="update") {
			alert("更新记录成功!");
		}
		if(action=="delete") {
			alert("删除操作成功");
			parent.reload();
		}
*/
	} else if(_msg < 0){
		if(action=="add") {
			alert("新增记录失败,请确认重新操作！");
		}
/**		if(action=="update") {
			alert("更新记录失败,请确认重新操作！");
		}
		if(action=="delete") {
			alert("删除操作失败,请确认重新操作！");
		}
*/
	} else {
		if(action=="add") {
			alert("表已经存在,请确认重新输入！");
		}
	}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<%@ include file="../foot.jsp"%>