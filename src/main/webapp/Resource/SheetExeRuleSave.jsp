<%@ include file="../timelater.jsp"%>
<jsp:useBean id="SheetExeRuleAct" scope="request" class="com.linkage.litms.resource.SheetExeRuleAct"/>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String strMsg="";
boolean isExist = false;

String strAction = request.getParameter("action");
//String[] sheet_id = request.getParameterValues("sheet_id");

/**    modify by lizhaojun 2007-07-31

if (strAction.equals("add")) {
	for (int i=0; i<sheet_id.length; i++) {
		if (SheetExeRuleAct.IfExist(sheet_id[i])) {
			isExist = true;

		}
	}
	if (isExist) {
		return;
	}
}
*/
strMsg = SheetExeRuleAct.SheetRulesActExe(request);
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	strMsg = <%=strMsg%>;
	var action = '<%=strAction%>';
	if (strMsg > 0) {
		if(action=="add") {
			alert("���������ɹ�");
		}
		if(action=="update") {
			alert("���¼�¼�ɹ�!");
		}
		if(action=="delete") {
			alert("ɾ�������ɹ�");
		}
	} else {
		if(action=="add") {
			alert("������¼ʧ��!");
		}
		if(action=="update") {
			alert("���¼�¼ʧ��!");
		}
		if(action=="delete") {
			alert("ɾ������ʧ��");
		}
	}
	
	if (action == 'delete'){
		this.location.href="./SheetExeRuleList.jsp";
	}
	else{
		parent.location.href="./SheetExeRuleList.jsp";
	}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<%@ include file="../foot.jsp"%>