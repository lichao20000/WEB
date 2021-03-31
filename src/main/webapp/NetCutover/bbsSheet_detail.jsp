<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.netcutover.SheetList"%>
<%@ page import="java.util.Map,com.linkage.litms.common.database.*,com.linkage.litms.*,java.text.*,java.sql.*"%>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<%@ include file="../timelater.jsp"%>
<html>
<head>
<%
	String sheet_id = request.getParameter("sheet_id");
	Map field = SheetList.getDetailSheet(sheet_id);

	//�ͻ�ID
	String bnet_id = "";
	//�ͻ��˺�
	String bnet_account = "";
	//ҵ������
	String product_spec_id = "";
	String customer_name = "";
	String type = "";
	String result = "";
	String result_info = "";
	String receive_date = "";
	String status = "";
	String wp_flag = "";
	String result_spec = "";
	String result_spec_desc = "";
	String oui = "";
	String device_type = "";
	String device_serialnumber = "";
	String time = "";
	String city_id = "";
	String order_type = "";
	String username = "";
	String passwd = "";
	String dev_sheet_id = "";
	String sheet_para_desc = "";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	if (field != null) {
		bnet_id = (String) field.get("bnet_id");
		bnet_account = (String) field.get("bnet_account");
		product_spec_id = SheetList.getOperateType((String) field.get("type"));
		customer_name = (String) field.get("customer_name");
		type = SheetList.getType((String) field.get("product_spec_id"));
		result = (String) field.get("result");
		result_info = (String) field.get("result_info");
		receive_date = (String) field.get("receive_date");
		if(null != receive_date && !receive_date.isEmpty()){
			receive_date = sdf.format(new Date(Long.parseLong(receive_date)));
		}
		status = (String) field.get("status");
		if(null != status && !status.isEmpty() && "1".equals(status)){
			status = "��ִ��";
		}else{
			status = "�ȴ�ִ��";
		}
		wp_flag = (String) field.get("wp_flag");
		result_spec = (String) field.get("result_spec");
		result_spec_desc = (String) field.get("result_spec_desc");
		oui = (String) field.get("oui");
		device_type = (String) field.get("device_type");
		device_serialnumber = (String) field.get("device_serialnumber");
		if (null == device_serialnumber || "null".equals(device_serialnumber)) {
			device_serialnumber = "";
		}
		time = (String) field.get("time");
		if(null != time && !time.isEmpty()){
			time = sdf.format(new Date(Long.parseLong(time)));
		}
		city_id = (String) field.get("city_id");
		order_type = (String) field.get("order_type");
		username = (String) field.get("username");
		passwd = (String) field.get("passwd");
		dev_sheet_id = (String) field.get("dev_sheet_id");
		sheet_para_desc = (String) field.get("sheet_para_desc");
		if(null != oui && oui.equals("null") ){
			oui = "";
		}
		if(passwd != null && passwd.equals("null")){
			passwd = "";
		}
		
		if (null != order_type && !order_type.isEmpty()) {
			switch (Integer.parseInt(order_type)) {
			case 1:
				order_type = "ADSL";
				break;
			case 2:
				order_type = "��ͨLAN";
				break;
			case 3:
				order_type = "��ͨ����";
				break;
			case 4:
				order_type = "ר��LAN";
				break;
			case 5:
				order_type = "ר�߹���";
				break;
			default:
				order_type = "δ֪";
			}
		}
	}
%>
</head>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<tr>
		<td>
		<table width="98%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd" align="center">
			<tr>
				<td width="162" align="center" class="title_bigwhite">
					������Ϣ
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<TR>
		<TD>
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR >
						<th colspan="4" align="center" >
						BBS������ϸ��Ϣ
						</th>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=colum  align="right">����ID</td>
						<td><%=sheet_id%>&nbsp;</td>
			            <td class=column align="right">ҵ������</td>
						<td ><%=SheetList.getType((String) field.get("product_spec_id"))%>&nbsp;</td>
					</TR>
					
					
					<TR bgcolor="#FFFFFF">
					    <td class=column align="right">��������</td>
						<td ><%=SheetList.getOperateType((String) field.get("type"))%>&nbsp;</td>
						<td class=column align="right">����ʱ��</td>
						<td ><%=receive_date%>&nbsp;</td>
						
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">�ص����</td>
						<td ><%=SheetList.getResult().get(result)%>&nbsp;</td>
						<td class=column align="right">�ص����˵��</td>
						<td ><%=result_info%>&nbsp;</td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">�豸����</td>
						<td ><%=device_type%>&nbsp;</td>
						<td class=column align="right">��������</td>
						<td ><%=order_type%>&nbsp;</td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">�豸����</td>
						<td ><%=oui%>&nbsp;</td>
						<td class=column align="right">�豸���к�</td>
						<td ><%=device_serialnumber%>&nbsp;</td>
					</TR>
			<!-- 
					<TR bgcolor="#FFFFFF">
						<td class=column width="" align="right">����ҵ��ִ�н��</td>
						<td ><%=result_spec%>&nbsp;</td>
						<td class=column width="" align="right">ִ�н��˵��</td>
						<td ><%=result_spec_desc%>&nbsp;</td>
					</TR>
					
					<td class=column align="right">�豸����ID</td>
						<td ><%=dev_sheet_id%>&nbsp;</td>
			 //-->
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">����</td>
						<td ><%=CityDAO.getCityIdCityNameMap().get(city_id)%>&nbsp;</td>
						<td class=column align="right">�û��˺�</td>
						<td ><%=username%>&nbsp;</td>
					</TR>
			<!--  	<TR bgcolor="#FFFFFF">
						<td class=column align="right">����״̬</td>
						<td ><%=status%>&nbsp;</td>
						<td class=column align="right">ִ��ʱ��</td>
						<td ><%=time%>&nbsp;</td>
					</TR>
					
					-->	
					<!-- <TR bgcolor="#FFFFFF">
						<td class=column align="right">����ԭʼ��Ϣ</td>
						<td colspan=3><%=sheet_para_desc%>&nbsp;</td>
					</TR> -->
					<TR>
						<TD colspan="4" align="center" class=foot><INPUT TYPE="button"
							value=" �� �� " class=btn onclick="javascript:window.close()">
						&nbsp;&nbsp;</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</body>
</html>