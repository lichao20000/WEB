<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String oid = request.getParameter("oid");
String topoType = request.getParameter("topoType");
String ShortName = LipossGlobals.getLipossProperty("InstArea.ShortName");


%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
//alert("getParentTopo.jsp in");

//try{
	var pid="";
	var oindex = "<%=oid%>";
	var topoType= "<%=topoType%>";
	//alert(parent.arrPObjectID);
	if(parent.arrPObjectID!="null")
	{
		//֪ͨMasterControl �����ƶ�����
		var param = parent.getMovedObjs();
		//alert(param);
		if(param != null && param!=""){
			var XMLSender = new ActiveXObject("Microsoft.XMLHTTP" );

		//topoType==5 vpn��ͼ
		<%if(topoType!=null && topoType.equals("5")){%>
			XMLSender.Open("POST",'vpn_modifyObjectsPosition.jsp',false);
		<%}else{%>
			XMLSender.Open("POST",'modifyObjectsPosition.jsp',false);
		<%}%>
			var param = parent.getMovedObjs();
			//alert(param);
			XMLSender.send((param));
			XMLSender = null;
		}

		//alert("getParentTopo.jsp parent.arrPObjectID: "+parent.arrPObjectID);
		//alert("getParentTopo.jsp parent.arrObjectID: "+parent.arrObjectID);
		//alert(parent.arrObjectID);
		if(parent.arrPObjectID != parent.arrObjectID)
		{
			//alert("parent.arrPObjectID != parent.arrObjectID");
			//alert(parent.arrDev[oindex]);
			if(typeof(parent.arrDev[oindex])=="undefined")
			{
				//alert("parent.arrDev[oindex])=='undefined'");
				//alert(parent.arrDev.length==0);
				if(parent.arrDev.length==0)
				{
					//alert("parent.arrDev.length==0");
					pid=parent.arrPObjectID;
				}
				else
				{
					//alert("parent.arrDev.length!=0");
					pid = parent.arrDev[0]._pid;
				}
			}
			else
			{
				//alert("parent.arrDev[oindex])!='undefined'");
				//��ȡ����ĸ��ڵ���
				pid = parent.arrDev[oindex]._pid;
			}
			
			// ���Ҫ���ص��ϲ���Ϊ�ּ�¼���û������ɾ����¼���û����
			if(pid==parent.topouser_id[parent.topouser_id.length-1]){
				delete parent.topouser_id[parent.topouser_id.length-1];
				//�ı��û�������鳤��
				parent.topouser_id.length -= 1;
			}
		}
		else 
		{
			//alert("parent.arrPObjectID == parent.arrObjectID");
			pid = oindex;
		}
		
		var tmp_pid = null;
		if(typeof(parent.arrDev[oindex]) != "undefined"){
			tmp_pid = parent.arrDev[oindex]._pid;
			// ���Ҫ���ص��ϲ���Ϊ�ּ�¼���û������ɾ����¼���û����
			if(tmp_pid==parent.topouser_id[parent.topouser_id.length-1]){
				delete parent.topouser_id[parent.topouser_id.length-1];
				//�ı��û�������鳤��
				parent.topouser_id.length -= 1;
			}
		}
		//alert("pid: "+pid);
		var test = pid.split("/");
		//alert(test.length);
		//for(x in test)
		//	alert(test[x]);
		
		// add by YYS 2006-11-4
		// ----------------------------------------------------------------------------------------------------------------------------------------------
		// �����ϲ�ʱͨ���ж�pid�Ƿ�Ϊ������ȷ���Ƿ����ϲ���ͼ���Ƿ��ص�ͼ(webtop/ditu.jsp)
		// Ŀǰɽ����ȫʡ������ֱ�����е�pid����Ϊ�������������ĺ���ƴ�� ���������е��²���ͼ��pidΪ����
		// ������ʱ ��pidΪƴ���򷵻�ȫʡ����ͼ�ȵ�ͼҳ���򷵻ظ����е���ͼ
		// ����ȫʡ����ֱ�����е�pid����ҲΪ����ʱ���ҵ���߼���������
		var ShortName = "<%=ShortName%>";
		//ɽ�������⴦�� �����ɽ�� ��ֱ������dituҳ�档
		if(isNaN(test[1]) && ShortName == "sd_wt"){
			parent.location.href='ditu.jsp';
		}
		else{
			//alert("pid: "+pid);
			//alert("topouser_id: "+parent.topouser_id[parent.topouser_id.length-1]);
			parent.idWebTopo.src = "getTopoByIdXML.jsp?pid="+ pid+"&topoType="+topoType+"&topouser_id="+parent.topouser_id[parent.topouser_id.length-1];
		// ----------------------------------------------------------------------------------------------------------------------------------------------
		
			parent.dispTopo();
			parent.idWebTopo.innerHTML = "<v:rect id=selRect  style=\"DISPLAY: none; Z-INDEX:  255; POSITION:  absolute\" coordsize = \"21600,21600\" strokecolor = \"#10fc18\"><v:fill opacity = \"0\" ></v:fill></v:rect>";
			parent.drawTopo();
			parent.initParamToSession();
		}
	}else{
		alert("�Ѿ�Ϊ���ϲ�");
	}
	
//}
//catch(e){
//	parent._throw(e,"ϵͳ����");
//}
//-->
</SCRIPT>