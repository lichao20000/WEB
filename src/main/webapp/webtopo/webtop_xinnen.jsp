<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.webtopo.PmeeDataList" %>
<%@ page import="com.linkage.litms.webtopo.DeviceResourceInfo" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ʵʱ������ʾ</title>

<%@ include file="../head.jsp"%>

</head>

<body>
 
<% 
  	request.setCharacterEncoding("GBK");	
	String device_id= request.getParameter("device_id");
	PmeeDataList pmeeData = new PmeeDataList();
	Cursor cursor = pmeeData.getPm_expressionInfo(device_id);
	Map myMap = cursor.getNext();
	
	DeviceResourceInfo deviceInfo = new DeviceResourceInfo();
	Cursor cursorDeviceInfo = deviceInfo.getDeviceResource(device_id);
	Map deviceMap = cursorDeviceInfo.getNext();
	String srcType = request.getParameter("type");
	if(null==srcType)
	{
		srcType ="";
	}
%>
<script language="JavaScript" type="text/JavaScript">
<!--
function Actionstr(id,name,descr){
var intid=id;
var strName = name;
var strDescr = descr;
var strDevice_id =<%=device_id%>;
var type ='<%=srcType%>'
//2007-3-12 江苏测试 新能指标实时刷新
//var page="webtop_xinnenlist.jsp?id=" + intid + "&name=" + strName + "&descr=" + strDescr + "&device_id="+strDevice_id+"&type="+type;
var page="webtop_xingnen_jsRefresh.jsp?id=" + intid + "&name=" + strName + "&descr=" + strDescr + "&device_id="+strDevice_id+"&type="+type;
window.open(page,"","left=10,top=10,resizable=yes,scrollbars=yes,width=600,height=400");
}
</script>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#FFFFFF"> <br>
        <br>
		<form>
        <table width="95%" height="30"  border="0" align="center" cellpadding="0" cellspacing="0" class="green_gargtd">
          <tr>
            <td width="162" align="center"  class="title_bigwhite"> ����ʵʱ�鿴</td>
            <td align="left"><span class="text">&nbsp;&nbsp;<img src="../images/attention_2.gif" width="15" height="12"> �豸:<%= deviceMap.get("device_name")%>&nbsp;&nbsp; <%= deviceMap.get("loopback_ip")%></span></td>
          </tr>
        </table>
        <table width="95%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text">
          <tr bgcolor="#FFFFFF" class="green_title">
            <td nowrap align=center>���ܱ��ʽ</td>
            <td nowrap>����</td>
          </tr>
		  
		  	<%
				if(myMap == null) {
			%>
			<tr bgcolor="#FFFFFF" >
				<td colspan="2" nowrap >
					�޷�����豸���ܱ��ʽ��
				</td>
		  </tr>
			<%
			  }else
			  {			
					while(myMap != null) {
					String id=(String)myMap.get("expressionid");
					String name=(String)myMap.get("name");
					String descr=(String)myMap.get("descr");
			%>
			
          <tr class=trOut onmouseover="className='trOver'" onmouseout="className='trOut'" onClick="javascriprt:Actionstr('<%=myMap.get("expressionid")%>','<%=myMap.get("name")%>','<%=myMap.get("descr")%>')">
            <td><%= name%></td>
            <td><%= descr%></td>
          </tr>
		  <%
		  		myMap = cursor.getNext();
				} 		 	
		  
		  }

		  %>
          <tr bgcolor="#FFFFFF" class=text>
            <td colspan="2" class="green_foot" align="right">
<input name="Submit2" type="button" class="jianbian" value=" �� �� " onClick="javascriprt:window.close()">
            </td>
          </tr>
        </table>
		</form>
    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
</table>
</body>
</html>