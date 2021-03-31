<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.*"%>
<%@page import="java.util.HashMap,com.linkage.litms.resource.*,com.linkage.litms.common.database.*"%>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<%
request.setCharacterEncoding("GBK");


	String loopback_ip = request.getParameter("loopback_ip");
	String device_ser = request.getParameter("device_ser");
	String city_id = request.getParameter("city_id");
	
	if(loopback_ip == null){
		loopback_ip = "";
	}
	if(device_ser == null){
		device_ser = "";
	}
	if(city_id == null){
		city_id = "";
	}	
	//属地信息
	
	DeviceAct deviceAct = new DeviceAct();
	String 	strCityList = deviceAct.getCityListSelf(false, city_id, "", request);
	
	
	
	
	
List list  = new ArrayList();
list.clear();


Map map = new HashMap();
map = CityDAO.getCityIdCityNameMap();
DeviceAct deviceact = new DeviceAct();
// 设备厂商Map
Map venderMap = deviceact.getOUIDevMap();
Map modelMap = DeviceAct.getDevice_Model();
//out.println(strSQL);
String strData = "";
list = DeviceAct.getDevicestatus(request);
String strBar = String.valueOf(list.get(0)); 

Cursor cursor = (Cursor)list.get(1);

Date date = new Date();
long nowtime = date.getTime();

//设备状态
String status = "";
Map fields = cursor.getNext();

if (fields == null) {
    strData = "<TR><TD class=column COLSPAN=8 >该系统没有设备资源</TD></TR>";
} else {

    while (fields != null) {
    
    	
    	if((nowtime - Long.parseLong((String) fields.get("max_time"))*1000) > 5*60*1000){  	
    		status = "<font color='red'>设备下线</font>";   		
    	} else {
    		status = "<font color='green'>设备在线</font>";   
    	}

        strData += "<TR>";
        strData += "<TD class=column2>" + venderMap.get((String) fields.get("oui")) + "</TD>";
        strData += "<TD class=column2>" + modelMap.get((String) fields.get("device_model_id")) + "</TD>";
        strData += "<TD class=column2>" + (String) fields.get("device_name") + "</TD>";
        strData += "<TD class=column2>" + (String) fields.get("loopback_ip") + "</TD>";
        strData += "<TD class=column2>" + (String) fields.get("device_ip") + "</TD>";
        strData += "<TD class=column2>" + (map.get((String) fields.get("city_id")) == null ? "" : map.get((String) fields.get("city_id"))) + "</TD>";        
        strData += "<TD class=column2 align=center>" + status + "</TD>";

       strData += "<TD class=column1 nowrap=\"nowrap\" align='center'><A HREF=\"javascript:GoContent('" + (String) fields.get("device_id") + "');\" TITLE=查看" + (String) fields.get("device_name") + "详细资料> 详细资料 </A></TD>";
		
                
        strData += "</TR>";
        fields = cursor.getNext();
    }
    strData += "<TR><TD class=column COLSPAN=10 align=right>" + strBar + "</TD></TR>";
}

map = null;
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--

function GoContent(device_id){

	strpage="../Resource/devicedetail.jsp?device_id=" + device_id;   
	window.open(strpage,"","left=20,top=20,width=450,height=300,resizable=no,scrollbars=yes");
}

function Golist(){
	this.location = "deviceStatus.jsp?city_id=<%=city_id%>&loopback_ip=<%=loopback_ip%>&device_ser=<%=device_ser%>"
}

//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<FORM NAME="frm" METHOD="post" ACTION="" onsubmit="return CheckForm()">
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<tr>
				<td>
					<table width="100%" height="30" border="0" cellspacing="0"
						cellpadding="0" class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								设备监控
							</td>
							<td>
								<img src="../images/attention_2.gif" width="15" height="12">
								查询设备在线状况。
							</td>
							<td align="right">
								<A HREF="javascript:Golist();" TITLE= 刷新 >刷新</A>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<TR>
				<TD bgcolor=#999999>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
						<TR><TH colspan=8>SNMP设备监控</TH></TR>
						<TR>
							<TD class='green_title2'>设备厂商</TD>
							<TD class='green_title2'>设备型号</TD>
							<TD class='green_title2'>设备名称</TD>
							<TD class='green_title2'>设备域名</TD>
							<TD class='green_title2'>设备IP</TD>
							<TD class='green_title2'>属地</TD>
							<TD class='green_title2'>状态</TD>
							<TD class='green_title2' width=150>操作</TD>
						</TR>
						<%=strData%>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</FORM>	
</TD></TR>
<TR><TD>
	<TABLE  width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
	<TR>
		<TD><B>快 速 查 询</B><br><hr size=2 color=#646464></TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="frm1" METHOD=POST ACTION="deviceStatus.jsp">
		设备域名:&nbsp;<INPUT TYPE="text" NAME="loopback_ip"  class=bk value="<%=loopback_ip%>">&nbsp;设备序列号:<INPUT TYPE="text" NAME="device_ser" size=20 maxlength=30 class=bk value="<%=device_ser%>">&nbsp;	
		<br><br> 设备属地:&nbsp;<%=strCityList%>&nbsp;&nbsp;&nbsp;&nbsp;<INPUT TYPE="submit" name="cmdQuery" value=" 查 询 " class=btn>&nbsp;&nbsp;		
		</FORM>
		</TD>
	</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>