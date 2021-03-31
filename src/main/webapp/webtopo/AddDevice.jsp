<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.webtopo.MCControlManager"%>

<%			request.setCharacterEncoding("GBK");
			String obj_type = request.getParameter("obj_type");
			String parent_id = request.getParameter("parent_id");
			String gather_id = request.getParameter("gather_id");
			String from=request.getParameter("from");
			String to=request.getParameter("to");
			String labelname = request.getParameter("labelname");//new String(request.getParameter("labelname").getBytes("ISO8859-1"), "GBK");
			String x = request.getParameter("x");
			String y = request.getParameter("y");
			String device_ip = request.getParameter("device_ip");
			String oui = request.getParameter("oui");
			String devicetype_id = request.getParameter("devicetype_id");
			String port = request.getParameter("port");
			String path = request.getParameter("path");
			String device_serialnum = request.getParameter("device_serialnum");
			String id = request.getParameter("id");
			MCControlManager mc = new MCControlManager(user.getAccount(), user.getPasswd());
			
			//0 操作失败
			//1 添加正常
			//2 设备已存在 不允许添加
			int flag = 0;
			String ret = "";
			String data = "";
			try {
				if(obj_type.equals("link")){
					flag=mc.CreateLink("" + user.getAreaId(),id,from,to,"",parent_id);
				}else if (obj_type.equals("device")) {
					id = "";
					//ret
					//* "device_id":正常添加设备，返回由MC提供的未用的设备编号
					//* "-1":新增对象在MC中存在。 或在数据库中存在，且device_status为1或0。 不允许添加
					//* "0":数据库操作失败
					ret = mc.AddObjectJudge(1, oui, device_serialnum,devicetype_id);
					//正常添加设备
					if (ret != null && !ret.equals("-1") && !ret.equals("0")) {

						id = "1/gw/" + ret;
						
						
						data = mc.CreateDevice("" + user.getAreaId(), id,
								ret, labelname, device_ip, gather_id,
								parent_id, oui, x, y, devicetype_id, port,
								path, device_serialnum);
						if (data != null && data.length() > 0) {
							flag = 1;
						}//else flag=0;

					}
					//添加的设备在MC或数据库中存在 不允许添加
					else if (ret != null && ret.equals("-1")) {
						flag = 2;
					}//else if(ret.equals("0")) flag=0

				} else if (obj_type.equals("segment")) {
					ret = mc.AddObjectJudge(0, "", "", "");
					id = "1/" + ret;

					data = mc.CreateSegment("" + user.getAreaId(), id,
							labelname, gather_id, parent_id, x, y);
					if (data != null && data.length() > 0) {
						flag = 1;
					}//else flag=0;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
%>

<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<xml id=result>
<?xml version="1.0" encoding="gb2312" ?>
<WebTopo>
<NetView id="1" pid="1/0">
<Nodes>
<%=data%>
</Nodes>
</NetView>
</WebTopo>
</xml>

<SCRIPT LANGUAGE="JavaScript">
<!--
	var obj_type="<%=obj_type%>";
	//0 操作失败
	//1 添加正常
	//2 设备已存在 不允许添加
	var flag="<%=flag%>";
	parent.isCall=flag;
	
	if(obj_type=="link")
	{
		if(flag=="1")
		{
		
			parent.createLink('<%=id%>','<%=from%>','<%=to%>','<%=parent_id%>');
			
		}
		else
		{
			alert("添加链路失败!");
		}
	}else{
		if(flag=="1")
		{
			var root = result.documentElement;
			var Nodes = root.selectNodes("//Nodes/Device");
			
			if(Nodes!=null && Nodes.length>0)
			{
				node=Nodes[0];					
				parent.createNet(node.getAttribute("id"),node.getAttribute("title"),node.getAttribute("x"),node.getAttribute("y"),node.getAttribute("ip"),node.getAttribute("icon"),node.getAttribute("type"),'<%=parent_id%>',node.getAttribute("state"));
				parent.isCall="1";
			}
			else
			{	
			}
		}	
		parent.CallPro();
	}
//-->
</SCRIPT>
</head>
<body>
</body>
</html>
