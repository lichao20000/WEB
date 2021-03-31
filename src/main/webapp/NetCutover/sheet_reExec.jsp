<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="ACS.Sheet"%>
<%
request.setCharacterEncoding("GBK");

String strGatherId = request.getParameter("gather_id");
String sheet_id = request.getParameter("sheet_id");
String receive_time = request.getParameter("receive_time");
int l_receive_time = Integer.parseInt(receive_time);
Sheet sheet = new Sheet(sheet_id,l_receive_time);
Sheet[] sheetObject  = {sheet};


// 开始调用corba接口
com.linkage.litms.common.corba.RPCManagerClient rpcManager = new com.linkage.litms.common.corba.RPCManagerClient();

String object_name="ACS_" + strGatherId;

String object_Poaname="ACS_Poa_" + strGatherId;

String strIor = user.getIor(object_name,object_Poaname);

Sheet[] sheetObj = null;
try{
	sheetObj = (Sheet[])rpcManager.DoRPC(sheetObject,strIor);
}catch(Exception e){
	sheetObj = (Sheet[])rpcManager.reDoRPC(sheetObject,strGatherId);
	e.printStackTrace();
}

int iCode = 0;
if(sheetObj !=null && sheetObj.length>0){
	Sheet sh_sheet = (Sheet)sheetObj[0];
	if(sh_sheet.sheet_id.equals("XXX") && sh_sheet.time == 0){
		iCode = 0;
	}else if(sh_sheet.sheet_id.equals("XXX") && sh_sheet.time == -1){
		iCode = 1;
	}else if(sh_sheet.sheet_id.equals("XXX") && sh_sheet.time == -3){
		iCode = 2;
	}else{
		iCode = 9;
	}
}
%>
<script language="javascript">

if(<%=iCode%> == 0){
	alert("工单发送失败！");
}else if(<%=iCode%> == 1){
	alert("工单不存在！");
}else if(<%=iCode%> == 2){
	alert("工单正在执行！");
}else{
	alert("工单发送成功！");
}
parent.refreshPage();
</script>
<%@ include file="../foot.jsp"%>