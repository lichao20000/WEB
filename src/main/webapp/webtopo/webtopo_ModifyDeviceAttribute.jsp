<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.webtopo.*,com.linkage.litms.common.database.*"%>
<%
request.setCharacterEncoding("GBK");
String oIndex = request.getParameter("oIndex");
String device_id = request.getParameter("device_id");
String id = request.getParameter("id");
String type = request.getParameter("type");
String deviceName=request.getParameter("title");
String loopbackip="";

//通过设备的device_id得到设备的名称和IP
if(type.compareTo("0")!=0)
{
	DeviceResourceInfo deviceInfo = new DeviceResourceInfo();
	Cursor cursor = deviceInfo.getDeviceResource(device_id);
	Map myMap = cursor.getNext();
	//deviceName= (String)myMap.get("device_name");
	loopbackip= (String)myMap.get("loopback_ip");
}
%>
<script language="JavaScript">
<!--
var oIndex = "<%=oIndex%>";
var type = "<%=type%>";
var isCall = 0;
var iTimerID;

function CallPro()
{
	if (parseInt(isCall,10) == 1)
	{
		window.alert("修改对象属性成功!");
		window.clearInterval(iTimerID);
		opener.document.all("childFrm").src="getChildTopo.jsp?pid="+opener.arrPObjectID;

		//opener.arrDev[oIndex]._title = frm.devname.value;

		//opener.arrDev[oIndex].remove();

		//opener.arrDev[oIndex].draw();
			
		window.close();
	}
}
function CheckForm()
{	
	if(frm.devname.value=="")
	{
		window.alert("请填写设备名称");
		return ;
	}
	var id="<%=id%>";
	var page = "webtopo_SaveDeviceAttribute.jsp?id=" + id + "&name=" + frm.devname.value + "&type=" + type;
	document.all("childFrm").src=page;
	iTimerID = window.setInterval("CallPro()", 1000);
}
//-->
</script>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
    <FORM NAME="frm" METHOD="post">
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH colspan="2" align="center">修改设备属性</TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="30%">设备名称</TD>
					<TD><input type="text" name="devname" value="<%=deviceName%>"></TD>					
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="30%">设备IP</TD>
					<TD><%=loopbackip%></TD>					
				</TR>
				<TR>
					<TD colspan="2" align="center" class=foot>
						<INPUT TYPE="button" value=" 保 存 " onclick="javascript:CheckForm();" class=btn>&nbsp;&nbsp;
						<INPUT TYPE="button" value=" 关 闭 " class=btn onclick="javascript:window.close();">						
					</TD>
				</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
    </FORM>
<TR><TD>&nbsp;</TD></TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
&nbsp;</TD></TR>
</TABLE>
