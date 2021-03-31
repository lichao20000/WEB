<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../timelater.jsp"%>
<%@ page import="com.linkage.litms.resource.DeviceAct"%>
<%@ page import="com.linkage.litms.webtopo.Scheduler"%>
<%@ page import="RemoteDB.DeviceSearch"%>
<%@ page import="com.linkage.litms.common.util.Encoder"%>
<%@ page import="com.linkage.litms.common.database.Cursor"%>
<%@ page import="java.util.Map"%>

<%--
@desc:webtopo中查询设备
@author : Hemc
@date :2006-12-19
--%>
<%
request.setCharacterEncoding("GBK");
String _value = request.getParameter("_value");
_value = _value == null ? "" : _value;
String _type = request.getParameter("_type");
_type = (_type == null || _type == "") ? "1" : _type;
short type = Short.parseShort(_type);
StringBuffer sb =  new StringBuffer();

//1:设备IP查询，2：设备名称查询，3：OUI+serial查询 调用MC来进行查询，4用户名称WEB自己查询数据库
if(4!=type)
{
	DeviceSearch[]  dataDevice = null;	
	try{
		Scheduler scheduler = new Scheduler();
		String area_id = String.valueOf(user.getAreaId());
		_value = Encoder.ChineseStringToAscii(_value);
		dataDevice = scheduler.getSearchDevice(area_id,type,_value);
	}catch(Exception ex){
		ex.printStackTrace();
	}
	if(dataDevice != null){
		sb.append("<select style='width:277px' id='selectDevice' class='column' onChange='findDevObjLocation(this.value)'>");
		if(dataDevice.length == 0)
			sb.append("<option value='-1'>查找无设备</option>");
		else
			sb.append("<option value='-1'>请选择设备</option>");
		for(int i=0;i<dataDevice.length;i++){
			sb.append("<option value='" + dataDevice[i].id + "'>" + Encoder.AsciiToChineseString(dataDevice[i].device_name) + "/" + dataDevice[i].device_ip + "</option>");
		}
		sb.append("</select>");
	}else{
		sb.append("没有查询到数据！");
	}
	out.print(sb.toString());
}
//根据HGW用户名称来查询
else
{
	DeviceAct act = new DeviceAct();
	Cursor cursor =act.getDeviceInfoByHgwUserName(request,_value);
	Map fields = cursor.getNext();
	sb.append("<select style='width:150px' id='selectDevice' class='column' onChange='findDevObjLocation(this.value)'>");
	if(null!=fields)
	{
		sb.append("<option value='-1'>请选择设备</option>");
		while(null!=fields)
		{
			sb.append("<option value='1/gw/").append(fields.get("device_id")).append("'>");
			sb.append(fields.get("device_name")).append("/").append(fields.get("lookback_ip"));
			sb.append("</option>");
			fields = cursor.getNext();
		}
	}
	else
	{
		sb.append("<option value='-1'>查找无设备</option>");
	}
	sb.append("</select>");
	out.print(sb.toString());
}

%>