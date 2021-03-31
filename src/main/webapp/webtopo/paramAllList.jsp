<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ page import="com.linkage.litms.resource.FileSevice"%>
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");

//测试连接  成功：1 失败： 0
FileSevice  fileSevice = new  FileSevice();
int int_flag  = fileSevice.testConnection(request);
%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
var device_id ="<%=device_id%>";
var oldTdObj = null;
//使选中的参数变色
function setTdColor(obj){
	if(oldTdObj != null){
		oldTdObj.className="";
	}
	obj.className="trOver";
	oldTdObj = obj;
}

//选择DLS参数
function selDLS(obj)
{
  document.all("childFrm").src ="dlsParam.jsp?device_id="+device_id;
  setTdColor(obj);
}

//选择PPOE、IP、ATM参数
function selPPPOE(obj)
{
  document.all("childFrm").src ="pppoeAndIpAndAtmParam.jsp?device_id="+device_id;
  setTdColor(obj);
}

//选择LAN参数
function selLAN(obj)
{
  document.all("childFrm").src ="lanParam.jsp?device_id="+device_id;
  setTdColor(obj);
}

//选择WLAN参数
function selWLAN(obj)
{
  document.all("childFrm").src ="wlanParam.jsp?device_id="+device_id;
  setTdColor(obj);
}
</SCRIPT>
<table width="95%" height="30" border="0" cellspacing="0" align="center" cellpadding="0" class="green_gargtd">
	<tr>
		<td width="162" align="center" class="title_bigwhite">
			常见参数
		</td>
		<td>
			<img src="../images/attention_2.gif" width="15" height="12">
			您可以选择查看具体参数		
		</td>
		<td align =center>
		   设备连接状态：
		   <%if(int_flag==1) {%>
			<img src="../images/gaojin_green.gif" width="15" height="12">
			<%}else{%>
			<img src="../images/gaojin_red.gif" width="15" height="12">
			<%} %>
		</td>
	</tr>
</table>
<table width="95%" border=0 cellspacing=1 cellpadding=0 align="center" bgcolor="#999999" class=text>
	<tr>
	  <td>
		<table width="100%" border=0 cellspacing=1 cellpadding=0 align="center">
		  <tr bgcolor="#ffffff" >
		      <td onclick="selDLS(this)" width="15%" height=60><span ><a href="javascript://" >DSL参数</a></span></td>
		   </tr>
		   <tr bgcolor="#ffffff" >
		      <td onclick="selPPPOE(this)" nowrap height=60><span><a href="javascript://" >PPPOE、IP、ATM参数</a></span></td>
	       </tr>
		   <tr bgcolor="#ffffff" >
		      <td onclick="selLAN(this)" nowrap height=60><span><a href="javascript://" >LAN参数</a></span></td>
		   </tr>
		   <tr bgcolor="#ffffff" >
		      <td onclick="selWLAN(this)" nowrap height=60><span><a href="javascript://" >WLAN参数</a></span></td>
		   </tr>
		 </table>
	  </td>
	  <td height=100%>
	    <iframe id="childFrm" name="childFrm" frameborder="0" width="100%" height="100%"></iframe>
	  </td> 
	</tr>
</table>

