<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ page import="com.linkage.litms.resource.FileSevice"%>
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");

//��������  �ɹ���1 ʧ�ܣ� 0
FileSevice  fileSevice = new  FileSevice();
int int_flag  = fileSevice.testConnection(request);
%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
var device_id ="<%=device_id%>";
var oldTdObj = null;
//ʹѡ�еĲ�����ɫ
function setTdColor(obj){
	if(oldTdObj != null){
		oldTdObj.className="";
	}
	obj.className="trOver";
	oldTdObj = obj;
}

//ѡ��DLS����
function selDLS(obj)
{
  document.all("childFrm").src ="dlsParam.jsp?device_id="+device_id;
  setTdColor(obj);
}

//ѡ��PPOE��IP��ATM����
function selPPPOE(obj)
{
  document.all("childFrm").src ="pppoeAndIpAndAtmParam.jsp?device_id="+device_id;
  setTdColor(obj);
}

//ѡ��LAN����
function selLAN(obj)
{
  document.all("childFrm").src ="lanParam.jsp?device_id="+device_id;
  setTdColor(obj);
}

//ѡ��WLAN����
function selWLAN(obj)
{
  document.all("childFrm").src ="wlanParam.jsp?device_id="+device_id;
  setTdColor(obj);
}
</SCRIPT>
<table width="95%" height="30" border="0" cellspacing="0" align="center" cellpadding="0" class="green_gargtd">
	<tr>
		<td width="162" align="center" class="title_bigwhite">
			��������
		</td>
		<td>
			<img src="../images/attention_2.gif" width="15" height="12">
			������ѡ��鿴�������		
		</td>
		<td align =center>
		   �豸����״̬��
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
		      <td onclick="selDLS(this)" width="15%" height=60><span ><a href="javascript://" >DSL����</a></span></td>
		   </tr>
		   <tr bgcolor="#ffffff" >
		      <td onclick="selPPPOE(this)" nowrap height=60><span><a href="javascript://" >PPPOE��IP��ATM����</a></span></td>
	       </tr>
		   <tr bgcolor="#ffffff" >
		      <td onclick="selLAN(this)" nowrap height=60><span><a href="javascript://" >LAN����</a></span></td>
		   </tr>
		   <tr bgcolor="#ffffff" >
		      <td onclick="selWLAN(this)" nowrap height=60><span><a href="javascript://" >WLAN����</a></span></td>
		   </tr>
		 </table>
	  </td>
	  <td height=100%>
	    <iframe id="childFrm" name="childFrm" frameborder="0" width="100%" height="100%"></iframe>
	  </td> 
	</tr>
</table>

