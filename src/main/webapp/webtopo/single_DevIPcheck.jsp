<html>
<head>
<title>IP״̬����</title>
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
			window.alert("IP״̬���óɹ���");
			window.clearInterval(iTimerID);	
			window.close();
			break;
		}
		case -1:
		{
			window.alert("���ݿ����ʧ��,�����²�������ϵ����Ա��");
			window.clearInterval(iTimerID);
			
			isCall=0;
			break;
		}
		case 0:
		{
			window.alert("û�ж����ݿ�����κβ�����");
			window.clearInterval(iTimerID);
			
			isCall=0;
			break;
		}
		case 2:
		{
			window.alert("���ݿ�����ɹ������޷����̨ͨѶ������ϵ����Ա��");
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
		window.alert("��ѡ��澯��ʽ");
		return false;
	}

	if(frm.alarm_grade.selectedIndex==0)
	{
		window.alert("��ѡ��澯�ȼ�");
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
		  <th colspan="2">IP״̬����</th>
		</tr>
		<tr>
		  <td class=column1 align="center">�豸IP��ַ</td>
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
				out.println("<td class=column1 align=center>�澯��ʽ</td>");
				out.println("<td class=column2>");

				switch (Integer.parseInt(str_warnmode)) {
					case 1:
						out.println("<select name=alarm_mode style=width:145>");
						out.println("<option value=0>��ѡ��澯��ʽ</option>");
						out.println("<option value=1 selected>ֻ����һ�θ澯��Ϣ</option>");
						out.println("<option value=2>�������͸澯��Ϣ</option>");
						break;

					case 2:
						out.println("<select name=alarm_mode style=width:145>");
						out.println("<option value=0>��ѡ��澯��ʽ</option>");
						out.println("<option value=1>ֻ����һ�θ澯��Ϣ</option>");
						out.println("<option value=2 selected>�������͸澯��Ϣ</option>");
						break;
				}
				out.println("</select>");
				out.println("</td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td class=column1 align=center>�澯�ȼ�</td>");
				out.println("<td class=column2>");

				switch (Integer.parseInt(str_warnlevel)) {
					case 1:
						out.println("<select name=alarm_grade style=width:145>");
						out.println("<option value=0>��ѡ��澯�ȼ�</option>");
						out.println("<option value=1 selected>�¼��澯</option>");
						out.println("<option value=2>����澯</option>");
						out.println("<option value=3>��Ҫ�澯</option>");
						out.println("<option value=4>��Ҫ�澯</option>");
						out.println("<option value=5>���ظ澯</option>");
						break;

					case 2:
						out.println("<select name=alarm_grade style=width:145>");
						out.println("<option value=0>��ѡ��澯�ȼ�</option>");
						out.println("<option value=1>�¼��澯</option>");
						out.println("<option value=2 selected>����澯</option>");
						out.println("<option value=3>��Ҫ�澯</option>");
						out.println("<option value=4>��Ҫ�澯</option>");
						out.println("<option value=5>���ظ澯</option>");
						break;
					
					case 3:
						out.println("<select name=alarm_grade style=width:145>");
						out.println("<option value=0>��ѡ��澯�ȼ�</option>");
						out.println("<option value=1>�¼��澯</option>");
						out.println("<option value=2>����澯</option>");
						out.println("<option value=3 selected>��Ҫ�澯</option>");
						out.println("<option value=4>��Ҫ�澯</option>");
						out.println("<option value=5>���ظ澯</option>");
						break;

					case 4:
						out.println("<select name=alarm_grade style=width:145>");
						out.println("<option value=0>��ѡ��澯�ȼ�</option>");
						out.println("<option value=1>�¼��澯</option>");
						out.println("<option value=2>����澯</option>");
						out.println("<option value=3>��Ҫ�澯</option>");
						out.println("<option value=4 selected>��Ҫ�澯</option>");
						out.println("<option value=5>���ظ澯</option>");
						break;
					
					case 5:
						out.println("<select name=alarm_grade style=width:145>");
						out.println("<option value=0>��ѡ��澯�ȼ�</option>");
						out.println("<option value=1>�¼��澯</option>");
						out.println("<option value=2>����澯</option>");
						out.println("<option value=3>��Ҫ�澯</option>");
						out.println("<option value=4>��Ҫ�澯</option>");
						out.println("<option value=5 selected>���ظ澯</option>");
						break;
					}
				out.println("</select>");
				out.println("</td>");
				out.println("</tr>");

			}else {
				out.println("<tr>");
				out.println("<td class=column1 align=center>�澯��ʽ</td>");
				out.println("<td class=column2>");
				out.println("<select name=alarm_mode style=width:145>");
				out.println("<option value=0>��ѡ��澯��ʽ</option>");
				out.println("<option value=1>ֻ����һ�θ澯��Ϣ</option>");
				out.println("<option value=2>�������͸澯��Ϣ</option>");
				out.println("</select>");
				out.println("</td>");
				out.println("</tr>");

				out.println("<tr>");
				out.println("<td class=column1 align=center>�澯�ȼ�</td>");
				out.println("<td class=column2>");
				out.println("<select name=alarm_grade style=width:145>");
				out.println("<option value=0>��ѡ��澯�ȼ�</option>");
				out.println("<option value=1>�¼��澯</option>");
				out.println("<option value=2>����澯</option>");
				out.println("<option value=3>��Ҫ�澯</option>");
				out.println("<option value=4>��Ҫ�澯</option>");
				out.println("<option value=5>���ظ澯</option>");
				out.println("</select>");
				out.println("</td>");
				out.println("</tr>");

			}
		%>

		<tr>
		  <td colspan=2 class=foot align="center"><input name="ok_button" type="button" onClick="javascript:CheckForm();" value="ȷ��">
&nbsp;&nbsp;
			<input name="close_button" type="button" onClick="javascript:window.close();" value="�ر�"></td>
		</tr>
    </table></td>
  </tr>
  
  <tr><td><IFRAME ID=childFrm name="childFrm" SRC="" STYLE="display:none"></IFRAME></td></tr>
</table>
 <input type="hidden" name="device_id" value=<%= device_id%>>
</form>


</body>
</html>
