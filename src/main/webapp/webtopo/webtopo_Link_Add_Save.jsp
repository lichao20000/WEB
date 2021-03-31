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
			window.alert("链路添加成功!");
			parent.frm.reset();
		}else{
			window.alert("链路添加失败,请重试!");
		}
	}
	else if(action == "delete"){
		if(code == 1){
			window.alert("链路删除成功!");
			parent.reload();
		}else{
			window.alert("链路删除失败,请重试!");
		}
	}else if(action == "update"){
		if(code == 1){
			window.alert("链路修改成功!");
		}else{
			window.alert("链路修改失败,请重试!");
		}
	}

</script>