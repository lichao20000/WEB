<%--
@author:hemc
@date:2006-11-28
--%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../timelater.jsp"%>
<%
	FluxComm flux =  new FluxComm(request);
	int iCode = 0;
	String action = request.getParameter("$action");
	//delete
	if ("delete".equals(action)) {
		iCode = flux.deleteLineIfIndex();
	}else if("update".equals(action)){
		//edit
		iCode = flux.editLineIfIndex();
	}else if("insert".equals(action)){
		//save
		iCode = flux.saveLineIfIndex();
	}
%>
<script type="text/javascript">
	var action = "<%=action%>";
	var code = <%=iCode%>;
	if(action == "insert"){
		if(code == 1){
			window.alert("��·��ӳɹ�!");
			parent.frm.reset();
		}else{
			window.alert("��·���ʧ��,������!");
		}
	}
	else if(action == "delete"){
		if(code == 1){
			window.alert("��·ɾ���ɹ�!");
			parent.reload();
		}else{
			window.alert("��·ɾ��ʧ��,������!");
		}
	}else if(action == "update"){
		if(code == 1){
			window.alert("��·�޸ĳɹ�!");
		}else{
			window.alert("��·�޸�ʧ��,������!");
		}
	}

</script>