<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<%@ page import="com.linkage.litms.common.database.Cursor"%>
<%@ page import="java.util.Map,java.util.HashMap"%>
<jsp:useBean id="fileSevice" scope="request" class="com.linkage.litms.resource.FileSevice"/>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>

<%
request.setCharacterEncoding("GBK");
String device_id = "";
String device_id_d = request.getParameter("device_id");
if (device_id_d.split(",").length == 1) {
	device_id = "'" + device_id_d + "'";
} else {
	String[] device_id_arr = device_id_d.split(",");
	for (String dev : device_id_arr) {
		device_id += ",'" + dev + "'";
	}
	device_id = device_id.substring(1);
}
String[] device_id_arr = device_id_d.split(",");

String type_tmp=request.getParameter("type");
String start_tmp = request.getParameter("starttime");
DateTimeUtil dt=new DateTimeUtil(start_tmp);


int type=Integer.parseInt(type_tmp);
	long start=0;
	long end=0;
	String tab_name="";
	switch(type){
		case 1:
			start=dt.getLongTime();
			end=start+3600 * 24;
			tab_name = "pm_raw_" + dt.getYear() + "_" + dt.getMonth();
			//tab_name="pm_day_stats_"+dt.getYear();
			break;
		case 2:
			start_tmp=dt.getFirstDayOfWeek("US");
			dt=new DateTimeUtil(start_tmp);
			start=dt.getLongTime();
			end=start+3600 * 24*7;
			tab_name = "pm_raw_" + dt.getYear() + "_" + dt.getMonth();
			//tab_name="pm_week_stats_"+dt.getYear();
			break;
		case 3:
			String[] tmp=start_tmp.split("-");
			dt=new DateTimeUtil(tmp[0]+"-"+tmp[1]+"-1");
			start=dt.getLongTime();
			end=new DateTimeUtil(dt.getLastDayOfMonth()).getLongTime();
			tab_name = "pm_raw_" + dt.getYear() + "_" + dt.getMonth();
			//tab_name="pm_month_stats_"+dt.getYear();
			break;
	}
	
//String endtime   = request.getParameter("endtime");
	//开始调用ACS采集在线时间
	
	//HashMap deviceInfo= DeviceAct.getDeviceInfo(device_id_arr[0]);
	//String gather_id = (String)deviceInfo.get("gather_id");
	//String devicetype_id = (String)deviceInfo.get("devicetype_id");
	//String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
	//Map upTimeMap = fileSevice.getUpTime(device_id_arr, gather_id, ior);
String sql= "";
Map<String, String> upTimeMap = new HashMap<String, String>();
Map field = null;


for (int i = 0; i < device_id_arr.length; i++) {
	double time_previous = 0;
	double time_first = 0;
	double time_current = 0;
	double time_total = 0;
	
	boolean isFirstRecord = true;
	sql = "select a.device_id,c.* from pm_map_instance a,pm_expression b, " + tab_name + " c "
		  + " where a.device_id='"+device_id_arr[i]+"'and a.expressionid=b.expressionid and "
		  +" b.class1=8 and c.id=a.id and c.gathertime>="+start+" and c.gathertime<="+ end + " order by c.gathertime";
	//sql="select interval from pm_raw_2008_8 where device_id ='"+device_id_arr[i]+"' and gathertime>="+start+" and gathertime<="+end + " order by gathertime";
	Cursor cursor=DataSetBean.getCursor(sql);
	field=cursor.getNext();
	
	boolean isPass = false;
	if(field!=null){
		while(field!=null){
			if (isFirstRecord) {
				time_first = Double.parseDouble((String)field.get("value"));
				time_current = time_first;
				isFirstRecord = false;
			}else{
				time_current = Double.parseDouble((String)field.get("value"));
			}
			
			if (time_current < time_previous) {
				time_total += (time_previous - time_first);
				time_first = 0;
				isPass = true;
			}
			
			time_previous = time_current;
			field=cursor.getNext();
		}
		//如果一直都在线
		if(!isPass && (time_current > time_first)){
			time_total = time_current - time_first;
			time_current = 0;
		}
		
		upTimeMap.put(device_id_arr[i],String.valueOf(time_total + time_current));
	}else{
		upTimeMap.put(device_id_arr[i],null);
	}
	
	
}
//String sql="select device_id,time_length from tab_devontime_static where device_id in("+device_id+") and static_time>="+starttime+" and static_time<="+endtime+" group by device_id order by device_id";
//Cursor cursor=DataSetBean.getCursor(sql);
//Map field=cursor.getNext();
//Map map=new HashMap();
//while(field!=null){
//	map.put(field.get("device_id"),field.get("num"));
//	field=cursor.getNext();
//}
//cursor=null;
//field=null;
String data="<table width='100%' border=0 cellspacing=0 cellpadding=0><tr><td bgcolor=#999999><table width='100%' border=0 cellspacing=1 cellpadding=2 align='center'><tr><th>设备名称</th><th>设备IP</th><th>设备在线时长</th></tr>";
String sql1="select device_id,device_name,loopback_ip from tab_gw_device where device_id in("+device_id+")";
Cursor cursor1=DataSetBean.getCursor(sql1);
Map field1=cursor1.getNext();
String tmp="";
while(field1!=null){
	tmp=(String)upTimeMap.get(field1.get("device_id"));//==null?"没有采集值":(String)upTimeMap.get(field.get("device_id"));
	if (null == tmp || "".equals(tmp.trim())) {
		tmp = "在线时长为0或者没有配置设备在线时长";
	} else {
		//tmp = fileSevice.formatSecToTimeStr((long)Math.floor(Double.parseDouble(tmp))); 
		tmp = tmp.substring(0, tmp.indexOf("."));
		long temp = Long.parseLong(tmp);
		tmp = temp/3600 + "小时" + (temp%3600)/60 + "分" + temp%60 + "秒";
	}
	data+="<tr>";
	data+="<td bgcolor='#FFFFFF' width='30%' align='center'>"+field1.get("device_name")+"</td><td bgcolor='#FFFFFF' width='30%' align='center'>"+field1.get("loopback_ip")+"</td><td bgcolor='#FFFFFF' align='center'>"+tmp+"</td>";
	data+="</tr>";
	field1=cursor1.getNext();
}
data+="</table></td></tr></table>";
%>


<html>
<head>
<script type="text/javascript">
parent.document.all("div_ping").innerHTML = "<%=data%>";
</script>
</head>
<body></body>
</html>