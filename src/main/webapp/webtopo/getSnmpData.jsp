<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.webtopo.snmpgather.*" %>
<%@ page import="com.linkage.litms.webtopo.snmpgather.InterfaceReadInfo"%>
<%@page import="java.util.ArrayList"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>联创科技数据综合网管</title>
</head>
<%@ include file="../head.jsp"%>

<%
  	request.setCharacterEncoding("GBK");
	String device_id = request.getParameter("device_id");
	String className = request.getParameter("className");
	InterfaceReadInfo di = (InterfaceReadInfo) Class.forName("com.linkage.litms.webtopo.snmpgather."+className).newInstance();
	di.setDevice_ID(device_id);
	//di.setAccountInfo(user.getAccount(),user.getPasswd());
	ArrayList data = di.getDeviceInfo();
	int type = di.getReportType();
	//用于javascript函数的参数
	String tmpParam = "";
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
function setDivStyle(){
	var maxScreenX = window.screen.width;
	var maxScreenY = window.screen.height;

	var w = maxScreenX * 0.9;
	var h = maxScreenY * 0.9 - 350;
	idLayer.style.width = w;
	idLayer.style.height = h+50;
}
//window.onload=setDivStyle;
//-->
</SCRIPT>
<body>
<DIV id="idLayer" style="width:100%;height:100%">
<table width="100%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text" id="topTable">

		<%
			if(data!=null)
			{
				if(type==1)
				{
					for(int i=0;i<data.size();i++)
					{
						out.println("<tr bgcolor='#FFFFFF'>");
						String[] str = (String[])data.get(i);
						for(int j=0;j<str.length;j++)
						{
							if(j==0)
							{
								out.println("<td class='green_title'  >"+str[j]+"</td>");
							}
							else
							{ 
								out.println("<td >"+str[j]+"</td>");
							}
						}

						out.println("</tr>");
					}
				}
				//只有端口信息才是下面情况
				else{					
					String color = "";
					//字体颜色
					String fontColor = "white";
					String manage_state = null;
					String run_state = null;
					String[] str=null;	
					if(data.size()>=1)
					{
						for(int i=0;i<data.size()-1;i++)
						{
							str=(String[])data.get(i);
							if(i==0){
								out.println("<tr class='green_title'>");
							}
							else{
								tmpParam = "'" + str[0] + "','" + str[1] + "','" + str[8] + "','" + str[9] + "','" + str[10] + "'";
								manage_state = str[6].trim();
								run_state = str[7].trim();
								//up up : green
								//up down : red
								//down down : green
								if(manage_state.equals(run_state)){
									color = "#C5E3B9";
									fontColor = "#000000";
								}
								else if(manage_state.equals("Up") && run_state.equals("Down")){
									color = "#FE7D7A";
									fontColor = "#000000";
								}
								out.println("<tr bgcolor='" + color + "'>");
							}
							for(int j=0;j<str.length;j++){
								out.println("<td >"+str[j].trim()+"</td>");
							}
							out.println("</tr>");
						}
					}
				
					if(data.size()>2)
					{
//						int portNum = data.size()-2;
						out.println("<tr class=\"green_foot\"><td colspan="+str.length+" align=right>端口总数:"+ (data.size()-2) +"</td></tr>");
					}
					
				}
			}
		%>
	</table>
</div>
</body>
</html>

<SCRIPT LANGUAGE="JavaScript">
<!--
if(typeof(parent.idLayerView) == "object"){
	parent.idLayerView.innerHTML = idLayer.innerHTML;
	//parent.idParentFun.text = idChildFun.innerHTML;
}
	function ViewReport(ifindex,ifdescr,ifname,ifnamedefined,ifportip){
		page = "../Visualman/now_getPort.jsp?ifindex=" + ifindex + "&ifdescr=" + ifdescr +"&ifname=" + ifname +"&ifnamedefined=" + ifnamedefined+"&ifportip=" + ifportip;
		//alert(page);
		window.open(page);
	}
//-->
</SCRIPT>