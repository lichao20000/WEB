<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*"%>
<%@ page import="java.util.Map"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<jsp:useBean id="DeviceUtil" scope="page"
	class="com.linkage.litms.resource.DeviceUtil" />
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>

<%
		String gw_type = request.getParameter("gw_type");
		if (gw_type == null || "".equals(gw_type))
			{
				gw_type = "1";
			}
		//设备序列号
		String device_serial = request.getParameter("device_serial");
		//设备ip
		String loopback_ip = request.getParameter("loopback_ip");
		//属地
		String city_id = request.getParameter("city_id");
		//状态
		String status = request.getParameter("status");
		//厂商
		String vendor_id = request.getParameter("vendor_id");
		//设备类型
		String device_model_id = request.getParameter("device_model_id");
		if (device_model_id == null){
			device_model_id = "";
		}
		//设备软件版本
		String devicetype_id = request.getParameter("devicetype_id");
		if (devicetype_id == null){
			devicetype_id = "";
		}
		//初始化查询设备的sql
		String sqlDevice = "";
		if ("3".equals(gw_type))
			{
				sqlDevice = DeviceUtil.initSNMPSql(device_serial, loopback_ip,
						city_id, status, vendor_id, device_model_id, devicetype_id,
						curUser);
			}
		else
			{
				sqlDevice = DeviceUtil.initGWSql(device_serial, loopback_ip,
						city_id, status, vendor_id, device_model_id, devicetype_id,
						curUser, gw_type);
			}
		//初始化翻页参数
		int offset = 1;
		int pagelen = 10;
		String stroffset = request.getParameter("offset");
		if (stroffset != null && !"".equals(stroffset))
			{
				offset = Integer.parseInt(stroffset);
			}
		//生成翻页菜单
		String param = "&device_serial="+device_serial+"&loopback_ip="+loopback_ip
						+"&city_id="+city_id+"&status="+status+"&vendor_id="+vendor_id
						+"&device_model="+device_model_id+"&devicetype_id="+devicetype_id
						+"&gw_type="+gw_type;
		QueryPage qryp = new QueryPage();
		qryp.initPage(sqlDevice, offset, pagelen);
		String strBar = qryp.getPageBar(param);
		//查询设备信息
		Cursor cursor1 = DataSetBean.getCursor(sqlDevice, offset, pagelen);
		Map fields1 = cursor1.getNext();
		//获取设备帐号之间的对应关系
		Map userDevice = DeviceUtil.getUserDevMap(gw_type);
		//获得属地与属地编码之间的对照
		Map city_Map = CityDAO.getCityIdCityNameMap();
		//取得所有OUI和厂商名对应的MAP
		Map venderMap = DeviceAct.getOUIDevMap();
		//所有设备型号对应信息
		Map deviceTypeMap = DeviceAct.getDeviceTypeMap();
%>
<html>
<head>
<link rel="stylesheet" href="../css/css_green.css" type="text/css">
</head>
<form action="">
<table width="95%" border=0 cellspacing=1 cellpadding=2 bgcolor=#999999
	align="center">
	<tr>
		<th width="10%">属地</th>
		<th width="15%">设备厂商</th>
		<th width="15%">设备型号</th>
		<th width="30%">设备序列号</th>		
		<th width="15%">ADSL帐号</th>
		<th width="15%">IPTV帐号</th>
	</tr>
	<%
		if (fields1 != null)
				{
					while (fields1 != null)
						{
							//设备id
							String device_id = (String) fields1.get("device_id");
							//adsl帐号
							String username = "";
							//IPTV帐号
							String username_iptv = "";
							//ADSL帐号(桥接)业务id
							String serviceID1 = "";
							//ADSL帐号(路由)业务id
							String serviceID2 = "";
							//IPTV帐号
							String serviceID3 = "";
							//家庭网关和企业网关

							if (!"3".equals(gw_type))
								{
									//家庭网关设备的业务id
									if (gw_type.equals("1"))
										{
											serviceID1 = "101";
											serviceID2 = "101";
											serviceID3 = "111";
										}
									//企业网关设备的业务id
									else
										{
											serviceID1 = "60";
											serviceID2 = "508";
											serviceID3 = "511";
										}
									//ADSL帐号(桥接)
									Map tmp = (Map) userDevice.get(device_id + "#"
											+ serviceID1);

									if (tmp != null)
										{
											username = (String) tmp.get("username");
										}
									//ADSL帐号(路由)
									tmp = (Map) userDevice.get(device_id + "#"
											+ serviceID2);
									if (tmp != null)
										{
											username = (String) tmp.get("username");
										}

									//IPTV帐号
									tmp = (Map) userDevice.get(device_id + "#"
											+ serviceID3);
									if (tmp != null)
										{
											username_iptv = (String) tmp
													.get("username");
										}
								}
							//SNMP设备
							else
								{
									Map tmp = (Map) userDevice.get(device_id);
									if (tmp != null)
										{
											username = (String) tmp.get("username");
										}
								}

							//显示地市
							String city_name = "";
							if (city_Map.get(fields1.get("city_id")) != null)
								{
									city_name = (String) city_Map.get(fields1
											.get("city_id"));
								}
							//厂商
							String vendor_str = "";
							vendor_str = (String) venderMap.get(fields1
									.get("vendor_id"));
							/**
							if ("3".equals(gw_type))
								{
									vendor_str = (String) venderMap.get(fields1
											.get("vendor_id"));
								}
							else
								{
									vendor_str = (String) venderMap.get(fields1
											.get("oui"));
								}
							**/
							//型号
							String deviceType_str = "";
							String[] tmp = (String[])deviceTypeMap.get(fields1.get("devicetype_id"));
							if (tmp != null && tmp.length==2) {
								deviceType_str = tmp[0];		
							}
							/**
							if ("3".equals(gw_type))
								{
									deviceType_str = (String) fields1
											.get("device_model");
								}
							else
								{
								String[] tmp = (String[])deviceTypeMap.get(fields1.get("devicetype_id"));
								if (tmp != null && tmp.length==2) {
									deviceType_str = tmp[0];		
								}
								}
							**/
	%>
	<tr>
		<td class=column nowrap><%=city_name%></td>
		<td class=column nowrap><%=vendor_str%></td>
		<td class=column nowrap><%=deviceType_str%></td>
		<td class=column nowrap><%=fields1.get("device_serialnumber")%></td>
		<td class=column nowrap><%=username%></td>
		<td class=column nowrap><%=username_iptv%></td>
	</tr>
	<%
		fields1 = cursor1.getNext();
						}
					out.println("<tr><td class=column nowrap align=right colspan=6>"
							+ strBar + "</td></tr>");
				}
			else
				{
	%>
	<tr>
		<td colspan=10 align=left class=column>系统没有关联的用户信息设备</td>
	</tr>
	<%
		}
	%>
</table>
</form>
</html>