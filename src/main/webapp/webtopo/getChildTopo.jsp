<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");

String oid = request.getParameter("oid");//��������
String pid = request.getParameter("pid");//����id���
String topoType = request.getParameter("topoType");
pid=(pid==null)?"":pid;
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
//try{
	//֪ͨMasterControl �����ƶ�����
	var param = null;
	var topoType= "<%=topoType%>";
	param = parent.getMovedObjs();
	
	if(param != null && param!=""){
		var XMLSender = new ActiveXObject("Microsoft.XMLHTTP" );
		//topoType==5 vpn��ͼ
		<%if(topoType!=null&&topoType.equals("5")){%>
			XMLSender.Open("POST",'vpn_modifyObjectsPosition.jsp',false);
		<%}else{%>
			XMLSender.Open("POST",'modifyObjectsPosition.jsp',false);
		<%}%>
		//var param = parent.getMovedObjs();
		XMLSender.send((param));
		XMLSender = null;
	}

	oindex = "<%=oid%>";
	pid="<%=pid%>";
	
	//alert("���� getChildTopoʱ oid: "+oindex);
	//alert("���� getChildTopoʱ pid: "+pid);
	
	if(pid=="")//���û�д������id��ͨ�������������е�����ֵ��ӵõ�����id
	{
		oid = parent.arrDev[oindex]._id;
	}
	else
	{
		oid=pid;
	}
	
	//alert("oid: "+oid);
	//alert("pid: "+pid);

	//alert("topouser_id: "+parent.topouser_id[parent.topouser_id.length-1]);
	//alert("topoType: <%=topoType%>");
	
	parent.idWebTopo.src = "getTopoByPidXML.jsp?pid="+oid+"&topouser_id="+parent.topouser_id[parent.topouser_id.length-1]+"&topoType="+topoType;
	parent.dispTopo();
	parent.idWebTopo.innerHTML = "<v:rect id=selRect  style=\"DISPLAY: none; Z-INDEX:  255; POSITION:  absolute\" coordsize = \"21600,21600\" strokecolor = \"#10fc18\"><v:fill opacity = \"0\" ></v:fill></v:rect>";
	parent.drawTopo();
	parent.initParamToSession();
	//parent.document.all("main.jsp").style.display = "";
/*
}
catch(e){
	parent._throw(e,"ϵͳ����");
}
*/
//-->
</SCRIPT>