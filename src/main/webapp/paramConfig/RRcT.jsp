<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.linkage.litms.paramConfig.ParamTreeObject" %>
<%@ page import="com.linkage.litms.webtopo.snmpgather.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Date" %>
<%

String device_id = request.getParameter("device_id");
String type = request.getParameter("type");
String gather_time = request.getParameter("gather_time");

Cursor cursor = null;
Map fields = null;


//ｔｒ０６９
if ("0".equals(type)){
	String oid1 = "InternetGatewayDevice.DeviceInfo.UpTime";
	String oid2 = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANIPConnection.1.Uptime";
	
	ParamTreeObject obj = new ParamTreeObject();
	Map map1 = obj.getParaValueMap(oid1, device_id);
	Map map2 = obj.getParaValueMap(oid2, device_id);
	String s1 = "0";
	if (map1 != null){
		if (map1.get(oid1) != null && !"".equals(map1.get(oid1))) {
			s1 = map1.get(oid1).toString();
		}
	}
	
	String s2 = "0";
	if (map2 != null){
		if (map2.get(oid2) != null && !"".equals(map2.get(oid2))) {
			s2 = map2.get(oid2).toString();
		}
	}

	//执行的sql
	String sql = "insert into tab_rrct_data_online (device_id,gather_time,type,online_time,underline_time) values ('" + device_id + "'," + gather_time + ",1," + s1 + "," + s2 + ")";

	int flag = DataSetBean.executeUpdate(sql);
	if (flag == 1) {
		out.print(1);
	} else {
		out.print(0);
	}

	//out.print(s1 + "#" + s2);
}
//SNMP
else if ("1".equals(type)){
	InterfaceReadInfo di = (InterfaceReadInfo) new ReadDevicePort();
	di.setDevice_ID(device_id);
	List list = di.getDeviceInfo();
	//执行的sql
	ArrayList sql = new ArrayList();
	String tmp = "";
	for (int i=1;i<list.size() - 1;i++){
		if (null == list.get(i)) continue;
		String[] arr = (String[])list.get(i);
		if (arr != null && arr.length == 12){
			tmp = "insert into tab_rrct_data_port (device_id,gather_time,portindex,portdec,porttype,maxunit,portspeed,macaddr,managestatus,runstatus,portname,aliname,portip,submask)" 
				+ " values ('" + device_id + "'," + gather_time + ",'" + arr[0] + "','" + arr[1] + "','" 
				+ arr[2] + "','" + arr[3] + "','" + arr[4] + "','" + arr[5] + "','" + arr[6] + "','" 
				+ arr[7] + "','" + arr[8] + "','" + arr[9] + "','" + arr[10] + "','" + arr[11] + "')";
			sql.add(tmp);
		}
	}
	if (sql.size() > 0){
		DataSetBean.doBatch(sql);
	}
	out.print(1);
}
else{
	out.print(0);
}

%>
