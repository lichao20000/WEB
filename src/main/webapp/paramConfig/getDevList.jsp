<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.Cursor"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ page import="java.util.Map,java.util.HashMap"%>
<%
	request.setCharacterEncoding("GBK");
	String city_id=request.getParameter("city_id");//属地
	String vendor_id=request.getParameter("vendor_id");//厂商ID
	String devicetype_id=request.getParameter("devicetype_id");//设备型号
	String type=request.getParameter("type");//radio:checkbox
	String param="";
	//选择属地
	if(city_id!=null && !city_id.equals("") && !city_id.equals("-1")){
		param+=" and city_id='"+city_id+"'";
	}
	//选择厂商
	if(vendor_id != null && !vendor_id.equals("") && !vendor_id.equals("-1")){
		param+=" and oui='"+vendor_id+"'";
	}
	//选择设备型号
	if(devicetype_id!=null && !devicetype_id.equals("") && !devicetype_id.equals("-1")){
		param+=" and devicetype_id="+devicetype_id;
	}
	//非admin用户
	if(!user.isAdmin()){
		param+=" and device_id in(select res_id from tab_gw_res_area where area_id="+curUser.getAreaId()+")";
	}
	String sql="select oui+'-'+device_serialnumber as oui ,device_id from tab_gw_device where device_status>-1 "+param+" order by device_id";
	
	Cursor cursor=DataSetBean.getCursor(sql);
	Map field=cursor.getNext();
	String data="<table width='100%'><tr>";
	int num=1;
	if(field!=null){
		while(field!=null){
			data+="<td><input type='"+type+"' id=\"device_id\" name=\"device_id\" value=\""+field.get("device_id")+"\">"+field.get("oui")+"</td>";
				field=cursor.getNext();
			if(num%3==0){
				data+="</tr><tr>";	
			}
				num++;
		}
	    data+="</tr></table>";
	}else{
		data="没有相关设备!";
	}
	

	cursor=null;
	field=null;
%>
<html>
<body>
<SPAN ID="child"><%=data%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_device").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>