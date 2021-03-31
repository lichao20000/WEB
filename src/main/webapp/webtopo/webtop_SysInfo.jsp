<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@page import="com.linkage.litms.webtopo.DeviceResourceInfo"%>
<% 
  	request.setCharacterEncoding("GBK");
	String device_id= request.getParameter("device_id");
	String className=request.getParameter("className");
	String title=request.getParameter("title");
	

	//通过DeviceResourceInfo从数据库获得设备信息
	DeviceResourceInfo deviceInfo = new DeviceResourceInfo();
	Cursor cursor = deviceInfo.getDeviceResource(device_id);
	Map myMap = cursor.getNext();
	String deviceName= (String)myMap.get("device_name");
	String loopbackip= (String)myMap.get("loopback_ip");
%>
<SCRIPT LANGUAGE="JavaScript" src="../Js/edittable.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function initialize_webtopo()
	{
		idLayerView.style.width = document.body.clientWidth;
		idLayerView.style.display = "";
		idLayerView.innerHTML = "&nbsp;&nbsp;正在载入数据......";
		var device_id="<%=device_id%>";
		var className="<%=className%>";
		var page="getSnmpData.jsp?device_id="+device_id+"&className="+className;
		document.all("childFrm").src=page;
	}
	//存在上次点击的行对象
	var _srcObj;
	function ViewReport(ifindex,ifdescr,ifname,ifnamedefined,ifportip){
		if(_srcObj != null){
			_srcObj.className="";
		}
		var obj = event.srcElement.parentElement;
		var tableObj = obj.parentElement;
		
		obj.className="trOver";
		_srcObj = obj;

		page = "../Visualman/now_getPort.jsp?device_id="+<%=device_id%>+"&ifindex=" + ifindex + "&ifdescr=" + ifdescr +"&ifname=" + ifname +"&ifnamedefined=" + ifnamedefined+"&ifportip=" + ifportip;
		window.open(page);
	}
//-->
</SCRIPT>
<body onload="initialize_webtopo();">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td bgcolor="#FFFFFF">       
			<table width="100%" height="30"  border="0" align="center" cellpadding="0" cellspacing="0" class="green_gargtd">
			  <tr>
				<td width="162" align="center"  class="title_bigwhite"><%=title%></td>
				<td align="left"><span class="text">&nbsp;&nbsp;<img src="../images/attention_2.gif" width="15" height="12"> 以下是设备<%= deviceName %>:&nbsp;&nbsp;<%= loopbackip%> 的<%=title%> </span></td>         
			  </tr>
			</table>
			<DIV id="idLayerView" style="overflow:auto;width:100%;display:none;"></div>	 
		</td>
	  </tr>
	</table>
	<table width="100%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text">
		<tr>
			<td width="23%" align=right class="foot">
				<input type="button" name="cmdQuery" value="导出Excel" class="jianbian" onclick="initialize('topTable',0,0)">
				<input name="button" type="button" class="jianbian" value=" 关 闭 " onClick="javascript:window.close()">
			</td>
	  </tr>
  </table>
  <IFRAME name="childFrm" ID=childFrm SRC="" STYLE="display:none;width:500;height:500"></IFRAME>
</body>


