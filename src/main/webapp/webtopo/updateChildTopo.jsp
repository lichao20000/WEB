<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");

//String oid = request.getParameter("oid");
String pid = request.getParameter("pid");
String topoType = request.getParameter("topoType");

pid=(pid==null)?"":pid;
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
//try{
	//通知MasterControl 本层移动对象
	var param = null;
	
	var doc = parent.parent.mainframe;
	//alert(doc.topouser_id[doc.topouser_id.length-1]);
	//alert(doc.getMovedObjs());
	param = doc.getMovedObjs();
	
	if(param != null && param!=""){
		var XMLSender = new ActiveXObject("Microsoft.XMLHTTP" );
		//topoType==5 vpn视图
		XMLSender.Open("POST",'vpn_modifyObjectsPosition.jsp',false);
		//param = doc.getMovedObjs();
		XMLSender.send(param);
		XMLSender = null;
	}
	//alert(doc.idWebTopo)
	doc.idWebTopo.src = "getTopoByPidXML.jsp?pid=<%=pid%>&topouser_id="+doc.topouser_id[doc.topouser_id.length-1]+"&topoType=<%=topoType%>&tt="+ new Date().getTime();
	doc.dispTopo();
	doc.idWebTopo.innerHTML = "<v:rect id=selRect  style=\"DISPLAY: none; Z-INDEX:  255; POSITION:  absolute\" coordsize = \"21600,21600\" strokecolor = \"#10fc18\"><v:fill opacity = \"0\" ></v:fill></v:rect>";
	doc.drawTopo();
	doc.initParamToSession();
	//parent.document.all("main.jsp").style.display = "";
/*
}
catch(e){
	parent._throw(e,"系统错误");
}
*/
//-->
</SCRIPT>