<html>
<head>
<title>IP状态配置</title>
</head>

<SCRIPT LANGUAGE="JavaScript">
<!--

var isCall=0;
var iTimerID;

function CallPro()
{	
	switch(parseInt(isCall,10))
	{
		case 1:
		{
			window.alert("IP状态配置成功！");
			window.clearInterval(iTimerID);	
			window.close();
			break;
		}
		case -1:
		{
			window.alert("数据库操作失败,请重新操作或联系管理员！");
			window.clearInterval(iTimerID);
			
			isCall=0;
			break;
		}
		case 0:
		{
			window.alert("没有对数据库进行任何操作！");
			window.clearInterval(iTimerID);
			
			isCall=0;
			break;
		}
		case 2:
		{
			window.alert("数据库操作成功，但无法与后台通讯，请联系管理员！");
			window.clearInterval(iTimerID);
			
			isCall=0;
			break;
		}
	}

}

function CheckForm()
{
	
	if(frm.alarm_mode.selectedIndex==0)
	{
		window.alert("请选择告警方式");
		return false;
	}

	if(frm.alarm_grade.selectedIndex==0)
	{
		window.alert("请选择告警等级");
		return false;
	}
	
	

	frm.submit();
	

	iTimerID = window.setInterval("CallPro()",1000);
	
}
//-->
</SCRIPT>

<body>
<%@ include file="../timelater.jsp"%>
<%@ page import="com.linkage.litms.webtopo.DeviceResourceInfo,com.linkage.litms.webtopo.IpCheck" %>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%
	String device_id = request.getParameter("device_id");
	DeviceResourceInfo deviceInfo = new DeviceResourceInfo();
	Cursor cursor = deviceInfo.getDeviceResource(device_id);
	Map myMap = cursor.getNext();
	String loopbackip= (String)myMap.get("loopback_ip");
%>

<form name="frm" method="post" action="testipcheck.jsp" target="childFrm">
<table width="300" height="160"  border="0" cellpadding="0" cellspacing="0" >
 <tr>
	<td bgcolor=#000000>
	  <table width="300" height="160"  border="0" cellspacing="1" cellpadding="2">
		<tr>
		  <th colspan="2">IP状态配置</th>
		</tr>
		<tr>
		  <td class=column1 align="center">设备IP地址</td>
		  <td class=column2><%= loopbackip%></td>
		</tr>

		<%
			IpCheck ipconfig = new IpCheck();
			boolean isavail = ipconfig.isValidateID(device_id);

			if (isavail == true) {
				Cursor devConfig_cursor = ipconfig.getDevConfigInfo(device_id);
				Map fileds = devConfig_cursor.getNext();
				String str_warnmode = (String)fileds.get("warnmode");
				String str_warnlevel = (String)fileds.get("warnlevel");
				
				out.println("<tr>");
				out.println("<td class=column1 align=center>告警方式</td>");
				out.println("<td class=column2>");

				switch (Integer.parseInt(str_warnmode)) {
					case 1:
						out.println("<select name=alarm_mode style=width:145>");
						out.println("<option value=0>请选择告警方式</option>");
						out.println("<option value=1 selected>只发送一次告警信息</option>");
						out.println("<option value=2>连续发送告警信息</option>");
						break;

					case 2:
						out.println("<select name=alarm_mode style=width:145>");
						out.println("<option value=0>请选择告警方式</option>");
						out.println("<option value=1>只发送一次告警信息</option>");
						out.println("<option value=2 selected>连续发送告警信息</option>");
						break;
				}
				out.println("</select>");
				out.println("</td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td class=column1 align=center>告警等级</td>");
				out.println("<td class=column2>");

				switch (Integer.parseInt(str_warnlevel)) {
					case 1:
						out.println("<select name=alarm_grade style=width:145>");
						out.println("<option value=0>请选择告警等级</option>");
						out.println("<option value=1 selected>事件告警</option>");
						out.println("<option value=2>警告告警</option>");
						out.println("<option value=3>次要告警</option>");
						out.println("<option value=4>主要告警</option>");
						out.println("<option value=5>严重告警</option>");
						break;

					case 2:
						out.println("<select name=alarm_grade style=width:145>");
						out.println("<option value=0>请选择告警等级</option>");
						out.println("<option value=1>事件告警</option>");
						out.println("<option value=2 selected>警告告警</option>");
						out.println("<option value=3>次要告警</option>");
						out.println("<option value=4>主要告警</option>");
						out.println("<option value=5>严重告警</option>");
						break;
					
					case 3:
						out.println("<select name=alarm_grade style=width:145>");
						out.println("<option value=0>请选择告警等级</option>");
						out.println("<option value=1>事件告警</option>");
						out.println("<option value=2>警告告警</option>");
						out.println("<option value=3 selected>次要告警</option>");
						out.println("<option value=4>主要告警</option>");
						out.println("<option value=5>严重告警</option>");
						break;

					case 4:
						out.println("<select name=alarm_grade style=width:145>");
						out.println("<option value=0>请选择告警等级</option>");
						out.println("<option value=1>事件告警</option>");
						out.println("<option value=2>警告告警</option>");
						out.println("<option value=3>次要告警</option>");
						out.println("<option value=4 selected>主要告警</option>");
						out.println("<option value=5>严重告警</option>");
						break;
					
					case 5:
						out.println("<select name=alarm_grade style=width:145>");
						out.println("<option value=0>请选择告警等级</option>");
						out.println("<option value=1>事件告警</option>");
						out.println("<option value=2>警告告警</option>");
						out.println("<option value=3>次要告警</option>");
						out.println("<option value=4>主要告警</option>");
						out.println("<option value=5 selected>严重告警</option>");
						break;
					}
				out.println("</select>");
				out.println("</td>");
				out.println("</tr>");

			}else {
				out.println("<tr>");
				out.println("<td class=column1 align=center>告警方式</td>");
				out.println("<td class=column2>");
				out.println("<select name=alarm_mode style=width:145>");
				out.println("<option value=0>请选择告警方式</option>");
				out.println("<option value=1>只发送一次告警信息</option>");
				out.println("<option value=2>连续发送告警信息</option>");
				out.println("</select>");
				out.println("</td>");
				out.println("</tr>");

				out.println("<tr>");
				out.println("<td class=column1 align=center>告警等级</td>");
				out.println("<td class=column2>");
				out.println("<select name=alarm_grade style=width:145>");
				out.println("<option value=0>请选择告警等级</option>");
				out.println("<option value=1>事件告警</option>");
				out.println("<option value=2>警告告警</option>");
				out.println("<option value=3>次要告警</option>");
				out.println("<option value=4>主要告警</option>");
				out.println("<option value=5>严重告警</option>");
				out.println("</select>");
				out.println("</td>");
				out.println("</tr>");

			}
		%>

		<tr>
		  <td colspan=2 class=foot align="center"><input name="ok_button" type="button" onClick="javascript:CheckForm();" value="确定">
&nbsp;&nbsp;
			<input name="close_button" type="button" onClick="javascript:window.close();" value="关闭"></td>
		</tr>
    </table></td>
  </tr>
  
  <tr><td><IFRAME ID=childFrm name="childFrm" SRC="" STYLE="display:none"></IFRAME></td></tr>
</table>
 <input type="hidden" name="device_id" value=<%= device_id%>>
</form>


</body>
</html>
