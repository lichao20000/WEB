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
		//�豸���к�
		String device_serial = request.getParameter("device_serial");
		//�豸ip
		String loopback_ip = request.getParameter("loopback_ip");
		//����
		String city_id = request.getParameter("city_id");
		//״̬
		String status = request.getParameter("status");
		//����
		String vendor_id = request.getParameter("vendor_id");
		//�豸����
		String device_model_id = request.getParameter("device_model_id");
		if (device_model_id == null){
			device_model_id = "";
		}
		//�豸����汾
		String devicetype_id = request.getParameter("devicetype_id");
		if (devicetype_id == null){
			devicetype_id = "";
		}
		//��ʼ����ѯ�豸��sql
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
		//��ʼ����ҳ����
		int offset = 1;
		int pagelen = 10;
		String stroffset = request.getParameter("offset");
		if (stroffset != null && !"".equals(stroffset))
			{
				offset = Integer.parseInt(stroffset);
			}
		//���ɷ�ҳ�˵�
		String param = "&device_serial="+device_serial+"&loopback_ip="+loopback_ip
						+"&city_id="+city_id+"&status="+status+"&vendor_id="+vendor_id
						+"&device_model="+device_model_id+"&devicetype_id="+devicetype_id
						+"&gw_type="+gw_type;
		QueryPage qryp = new QueryPage();
		qryp.initPage(sqlDevice, offset, pagelen);
		String strBar = qryp.getPageBar(param);
		//��ѯ�豸��Ϣ
		Cursor cursor1 = DataSetBean.getCursor(sqlDevice, offset, pagelen);
		Map fields1 = cursor1.getNext();
		//��ȡ�豸�ʺ�֮��Ķ�Ӧ��ϵ
		Map userDevice = DeviceUtil.getUserDevMap(gw_type);
		//������������ر���֮��Ķ���
		Map city_Map = CityDAO.getCityIdCityNameMap();
		//ȡ������OUI�ͳ�������Ӧ��MAP
		Map venderMap = DeviceAct.getOUIDevMap();
		//�����豸�ͺŶ�Ӧ��Ϣ
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
		<th width="10%">����</th>
		<th width="15%">�豸����</th>
		<th width="15%">�豸�ͺ�</th>
		<th width="30%">�豸���к�</th>		
		<th width="15%">ADSL�ʺ�</th>
		<th width="15%">IPTV�ʺ�</th>
	</tr>
	<%
		if (fields1 != null)
				{
					while (fields1 != null)
						{
							//�豸id
							String device_id = (String) fields1.get("device_id");
							//adsl�ʺ�
							String username = "";
							//IPTV�ʺ�
							String username_iptv = "";
							//ADSL�ʺ�(�Ž�)ҵ��id
							String serviceID1 = "";
							//ADSL�ʺ�(·��)ҵ��id
							String serviceID2 = "";
							//IPTV�ʺ�
							String serviceID3 = "";
							//��ͥ���غ���ҵ����

							if (!"3".equals(gw_type))
								{
									//��ͥ�����豸��ҵ��id
									if (gw_type.equals("1"))
										{
											serviceID1 = "101";
											serviceID2 = "101";
											serviceID3 = "111";
										}
									//��ҵ�����豸��ҵ��id
									else
										{
											serviceID1 = "60";
											serviceID2 = "508";
											serviceID3 = "511";
										}
									//ADSL�ʺ�(�Ž�)
									Map tmp = (Map) userDevice.get(device_id + "#"
											+ serviceID1);

									if (tmp != null)
										{
											username = (String) tmp.get("username");
										}
									//ADSL�ʺ�(·��)
									tmp = (Map) userDevice.get(device_id + "#"
											+ serviceID2);
									if (tmp != null)
										{
											username = (String) tmp.get("username");
										}

									//IPTV�ʺ�
									tmp = (Map) userDevice.get(device_id + "#"
											+ serviceID3);
									if (tmp != null)
										{
											username_iptv = (String) tmp
													.get("username");
										}
								}
							//SNMP�豸
							else
								{
									Map tmp = (Map) userDevice.get(device_id);
									if (tmp != null)
										{
											username = (String) tmp.get("username");
										}
								}

							//��ʾ����
							String city_name = "";
							if (city_Map.get(fields1.get("city_id")) != null)
								{
									city_name = (String) city_Map.get(fields1
											.get("city_id"));
								}
							//����
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
							//�ͺ�
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
		<td colspan=10 align=left class=column>ϵͳû�й������û���Ϣ�豸</td>
	</tr>
	<%
		}
	%>
</table>
</form>
</html>