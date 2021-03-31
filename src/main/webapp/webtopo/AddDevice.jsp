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
			
			//0 ����ʧ��
			//1 �������
			//2 �豸�Ѵ��� ���������
			int flag = 0;
			String ret = "";
			String data = "";
			try {
				if(obj_type.equals("link")){
					flag=mc.CreateLink("" + user.getAreaId(),id,from,to,"",parent_id);
				}else if (obj_type.equals("device")) {
					id = "";
					//ret
					//* "device_id":��������豸��������MC�ṩ��δ�õ��豸���
					//* "-1":����������MC�д��ڡ� �������ݿ��д��ڣ���device_statusΪ1��0�� ���������
					//* "0":���ݿ����ʧ��
					ret = mc.AddObjectJudge(1, oui, device_serialnum,devicetype_id);
					//��������豸
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
					//��ӵ��豸��MC�����ݿ��д��� ���������
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
	//0 ����ʧ��
	//1 �������
	//2 �豸�Ѵ��� ���������
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
			alert("�����·ʧ��!");
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
