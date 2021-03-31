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
		//通知MasterControl 本层移动对象
		var param = parent.getMovedObjs();
		//alert(param);
		if(param != null && param!=""){
			var XMLSender = new ActiveXObject("Microsoft.XMLHTTP" );

		//topoType==5 vpn视图
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
				//获取对象的父节点编号
				pid = parent.arrDev[oindex]._pid;
			}
			
			// 如果要返回的上层编号为现记录的用户编号则删除记录的用户编号
			if(pid==parent.topouser_id[parent.topouser_id.length-1]){
				delete parent.topouser_id[parent.topouser_id.length-1];
				//改变用户编号数组长度
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
			// 如果要返回的上层编号为现记录的用户编号则删除记录的用户编号
			if(tmp_pid==parent.topouser_id[parent.topouser_id.length-1]){
				delete parent.topouser_id[parent.topouser_id.length-1];
				//改变用户编号数组长度
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
		// 返回上层时通过判断pid是否为数字来确定是返回上层云图还是返回地图(webtop/ditu.jsp)
		// 目前山东对全省的下属直属地市的pid定义为各地市中文名的汉语拼音 而各地市中的下层云图的pid为数字
		// 当返回时 如pid为拼音则返回全省的云图既地图页否则返回各地市的云图
		// 当对全省下属直属地市的pid定义也为数字时则此业务逻辑会有问题
		var ShortName = "<%=ShortName%>";
		//山东做特殊处理 如果是山东 则直接跳到ditu页面。
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
		alert("已经为最上层");
	}
	
//}
//catch(e){
//	parent._throw(e,"系统错误");
//}
//-->
</SCRIPT>