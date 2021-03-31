<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="ACS.Sheet"%>
<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManage"/>
<%
request.setCharacterEncoding("GBK");
String strGatherId = request.getParameter("gather_id");

String[] sheetArr = request.getParameterValues("chbError");

Sheet[] sheetObject = new Sheet[sheetArr.length];
for(int i=0;i<sheetArr.length;i++){
	String[] lst_sheet = sheetArr[i].split(",");
	Sheet tr_sheet = new Sheet();
	tr_sheet.sheet_id = lst_sheet[0];
	tr_sheet.time = Integer.parseInt(lst_sheet[1]);
	sheetObject[i] = tr_sheet;		
}

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
//
String errorSheet="";
if(sheetObj !=null && sheetObj.length>0){
	if(sheetObj.length == 1){
		Sheet sh_sheet = sheetObj[0];
		if(sh_sheet.sheet_id.equals("XXX")){
			errorSheet = "fail";
		}else{
			if(sh_sheet.time == -3){
				errorSheet = sh_sheet.sheet_id;			
			}
		}		
	}else{
		for(int i=0;i<sheetObj.length;i++){
			Sheet  sheet = sheetObj[i];
			if(sheet.time == -3){
				if(errorSheet.equals("")){
					errorSheet = sheet.sheet_id;	
				}else{
					errorSheet += "|" + sheet.sheet_id;
				}
			}
		}
	}
}

%>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.closeMsgDlg();
var erSheet = "<%=errorSheet%>";
if(erSheet == ""){
	alert("工单发送成功！");
}else if(erSheet=="fail"){
	alert("工单发送失败！");	
}else{
	alert("工单『<%=errorSheet%>』操作的设备正在执行！");
}
parent.refreshPage();
//-->
</SCRIPT>
<%@ include file="../foot.jsp"%>