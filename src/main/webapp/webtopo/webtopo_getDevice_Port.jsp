<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.flux.FluxComm,
				 com.linkage.litms.common.database.*,
				 java.util.*,
                 com.linkage.litms.common.util.*"%>
<%
request.setCharacterEncoding("GBK");
String str_deviceid = request.getParameter("dev_id");
//�Ƿ��Զ˿�IPΪ�жϱ�׼
String useip=request.getParameter("useip");
String strChildList = "";
String selectOne = request.getParameter("one");
String inputType = "checkbox";

Map portDescript=new HashMap();
Map userportDescript=new HashMap();
Map speedData = new HashMap();
userportDescript.clear();
portDescript.clear();
speedData.clear();

if(selectOne !=null)
	inputType = "radio";
FluxComm flux = new FluxComm(request);
Cursor cursor = flux.getDevicePort(str_deviceid,useip);
Map fields = cursor.getNext();
if(fields == null){
	strChildList =  "���豸û�ж˿���Ϣ";
}
else{
	while(fields != null){
		String ifindex=(String)fields.get("ifindex");
		String view= (String)fields.get("ifindex") + " | " + (String)fields.get("ifdescr")+" | "+ (String) fields.get("ifnamedefined");
		if(useip!=null && useip.equals("1"))
		{
			ifindex +="/"+ (String) fields.get("portip");
			view +=" | " + (String) fields.get("portip");
		}

		strChildList += "<INPUT TYPE="+ inputType +" name=port value="+ ifindex +" checked>";
		strChildList += "&nbsp;" + view + "<INPUT TYPE=checkbox name=if_real_speed value="+fields.get("if_real_speed")+" style='display:none'><INPUT TYPE=checkbox name=ifnamedefined value='"+fields.get("ifnamedefined")+"' style='display:none'><br>";
		
		//�����ϣӳ���
		portDescript.put(fields.get("ifindex"),fields.get("ifdescr"));
		userportDescript.put(fields.get("ifindex"),fields.get("ifnamedefined"));
		speedData.put(fields.get("ifindex"),String.valueOf(fields.get("if_real_speed")));
		fields = cursor.getNext();
	}
}

//����session
session.setAttribute("port",portDescript);
session.setAttribute("userport",userportDescript);
session.setAttribute("speedData",speedData);
//���ٹ�ϣӳ���
//map.clear();
%>
<HTML>
<HEAD>
<TITLE> ȡ���豸�˿�</TITLE>
<META NAME="Generator" CONTENT="EditPlus">
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb_2312-80">
<META NAME="Description" CONTENT="ȡ���Ӵ�">
</HEAD>
<BODY BGCOLOR="#FFFFFF">
<FORM name=frm >
<SPAN ID="child"><%=strChildList%></SPAN>
</FORM>
<SCRIPT LANGUAGE="JavaScript">
<!--

parent.document.all("device_port").innerHTML = child.innerHTML; 
//parent.idLayerView1.style.width = document.body.clientWidth*0.95;
parent.idLayerView1.style.display = "";
parent.idLayerView1.innerHTML = "������������......";
parent.document.frm.action="webtopo_report_search.jsp";
parent.document.frm.submit();
 
 
//-->
</SCRIPT>
</BODY>
</HTML>