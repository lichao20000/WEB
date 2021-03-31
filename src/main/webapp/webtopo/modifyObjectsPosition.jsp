<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.Scheduler,
						    java.io.BufferedReader"%> 
<%
request.setCharacterEncoding("GBK");
String movedObjs = "";
//¶ÁÈ¡XMLHTTPÁ÷
BufferedReader br = request.getReader();
String str = "";
while ((str=br.readLine()) != null) {
	movedObjs += str;
}


Scheduler scheduler = new Scheduler();
if(movedObjs!=null && !movedObjs.equals("")){
	scheduler.ModifyObjectsPosition(movedObjs);
}
else{
}
%>