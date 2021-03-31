<html>
<head>
<title>实例管理</title>
</head>
<SCRIPT LANGUAGE="JavaScript">
<!--
function CheckForm() {
	
	var device_id = new String();
	var expressionid = new String();
	var expName = new String();
	var deviceIP = new String();
	var instanceIndex = new String();
	var str_checkboxValue = new String();
	var instance_Info = new Array();
	var len = document.frm.elements.length;
	var i;
	var flag = false;
	var j = 0;
	
	for(i=0; i<len; i++){
		if(document.frm.elements[i].name=='select_one')
		{
			if (document.frm.elements[i].checked)
			{
				flag=true;
				break;
			}
		}		
	}
	
	if(!flag)
	{
		window.alert("请选择一条实例！");
		return false;	
	}

	for (i=0;i<len;i++)
	{
		if (document.frm.elements[i].name=='select_one')
		{
			if (document.frm.elements[i].checked)
			{
				j++;
				
				if (j>1)
				{
					alert("只能对一条实例修改！");
					j = 0;
					return false;
				}
				str_checkboxValue = document.frm.elements[i].value;
				instance_Info = str_checkboxValue.split(",");
				device_id = instance_Info[0];
				expressionid = instance_Info[1];
				deviceIP = instance_Info[2];
				instanceIndex = instance_Info[3];
			}
		}
	}
	j = 0;
	var page = "pm_instanceConfig.jsp?device_id=" + device_id + "&expressionid=" + expressionid + "&deviceIP=" + deviceIP + "&instanceIndex=" + instanceIndex;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=500,width=700,top=200,left=365";
	window.open(page,"编辑设备实例",otherpra);
}
//-->
</SCRIPT>

<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<%
	String deviceID = request.getParameter("device_id");
	String expressionID = request.getParameter("expressionid");
	String deviceIP = request.getParameter("deviceIP");
	String deviceName = request.getParameter("deviceName");
	GetPm_Expreesion pmexp = new GetPm_Expreesion(request);
	String expName = pmexp.getpmName(expressionID);
	Cursor cursor_indexInfo = pmexp.getIndexInfo(deviceID,expressionID);
	Map map_indexInfo = cursor_indexInfo.getNext();

%>
<%@ page import= "com.linkage.litms.webtopo.common.*" %>
<body>
<form action="" method="post" name="frm">
<table width="100%"  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor=#000000>
		<table width="100%"  border="0" cellspacing="1" cellpadding="2">
		  <tr><TH colspan="4">实例管理</TH></tr>
		  <tr>
			<td align="center" class=column1>设备IP</td>
			<td align="left" class=column2>
			
			<%
				out.println("<input name=IPaddress type=text value=" + deviceIP + " readonly=true></td>");
			%>
			
			<td align="center" class=column1>设备名称</td>
			<td align="left" class=column2>
			<%
				out.println("<input name=deviceName type=text value=" + deviceName + " readonly=true></td>");
			%>
			
		  </tr>
		   <tr>
			<td align="center" class=column1>表达式名称</td>
			<td colspan="3" align="left" class=column2>
			<%
				out.println("<input name=expreesionName type=text value=" + expName + " readonly=true size=30></td>");
			%>
		  </tr>
		</table>
	</td>
  </tr>
  
  <tr>
    <td bgcolor=#000000>
	  <table width="100%"  border="0" cellspacing="1" cellpadding="2">
	     <tr>
			<TH align="center" class=column1>索引</TH>
			<TH align="center" class=column1>实例描述</TH>
			<TH align="center" class=column1>选择</TH>
		  </tr>
	<%	
		if (map_indexInfo == null) {
			out.println("<tr>");
			out.println("<td align=center class=column1>&nbsp;&nbsp;&nbsp;</td>");
			out.println("<td align=center class=column2>&nbsp;&nbsp;&nbsp;</td>");
			out.println("<td align=center class=column2>&nbsp;&nbsp;&nbsp;</td>");
			out.println("</tr>");
		}
		else {
			while (map_indexInfo != null) {
				out.println("<tr>");
				out.println("<td align=center class=column1>" + map_indexInfo.get("indexid") + "</td>");
				out.println("<td align=center class=column2>" + map_indexInfo.get("descr") + "</td>");
				out.println("<td align=center class=column1><input name=select_one type=checkbox value=" + deviceID  + "," + expressionID + "," + deviceIP + "," + map_indexInfo.get("indexid") + "></td>");
				out.println("</tr>");
				map_indexInfo = cursor_indexInfo.getNext();
			}
		}
		
	%>

		  
	  </table>
	</td>
  </tr>
  <tr>
	<td bgcolor=#000000>
	  <table width="100%"  border="0" cellspacing="1" cellpadding="2">
		<tr>
			<td align="center" class=foot><input type="button" value=" 修 改 " class="jianbian" onClick="CheckForm();"> 
			&nbsp;&nbsp;&nbsp;<input type="button" value=" 关 闭 " class="jianbian" onClick="javascript:window.close();"></td>
		  </tr>
	  </table>
	</td>
  </tr>
  <tr>
	<td><IFRAME ID=childFrm name="childFrm" SRC="" STYLE="display:none"></IFRAME></td>
  </tr>
</table>

</form>
</body>
</html>