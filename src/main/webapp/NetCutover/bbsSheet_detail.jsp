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

	//客户ID
	String bnet_id = "";
	//客户账号
	String bnet_account = "";
	//业务类型
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
			status = "已执行";
		}else{
			status = "等待执行";
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
				order_type = "普通LAN";
				break;
			case 3:
				order_type = "普通光纤";
				break;
			case 4:
				order_type = "专线LAN";
				break;
			case 5:
				order_type = "专线光纤";
				break;
			default:
				order_type = "未知";
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
					工单信息
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
						BBS工单详细信息
						</th>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=colum  align="right">工单ID</td>
						<td><%=sheet_id%>&nbsp;</td>
			            <td class=column align="right">业务类型</td>
						<td ><%=SheetList.getType((String) field.get("product_spec_id"))%>&nbsp;</td>
					</TR>
					
					
					<TR bgcolor="#FFFFFF">
					    <td class=column align="right">操作类型</td>
						<td ><%=SheetList.getOperateType((String) field.get("type"))%>&nbsp;</td>
						<td class=column align="right">接收时间</td>
						<td ><%=receive_date%>&nbsp;</td>
						
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">回单结果</td>
						<td ><%=SheetList.getResult().get(result)%>&nbsp;</td>
						<td class=column align="right">回单结果说明</td>
						<td ><%=result_info%>&nbsp;</td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">设备类型</td>
						<td ><%=device_type%>&nbsp;</td>
						<td class=column align="right">订单类型</td>
						<td ><%=order_type%>&nbsp;</td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">设备厂商</td>
						<td ><%=oui%>&nbsp;</td>
						<td class=column align="right">设备序列号</td>
						<td ><%=device_serialnumber%>&nbsp;</td>
					</TR>
			<!-- 
					<TR bgcolor="#FFFFFF">
						<td class=column width="" align="right">特殊业务执行结果</td>
						<td ><%=result_spec%>&nbsp;</td>
						<td class=column width="" align="right">执行结果说明</td>
						<td ><%=result_spec_desc%>&nbsp;</td>
					</TR>
					
					<td class=column align="right">设备工单ID</td>
						<td ><%=dev_sheet_id%>&nbsp;</td>
			 //-->
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">属地</td>
						<td ><%=CityDAO.getCityIdCityNameMap().get(city_id)%>&nbsp;</td>
						<td class=column align="right">用户账号</td>
						<td ><%=username%>&nbsp;</td>
					</TR>
			<!--  	<TR bgcolor="#FFFFFF">
						<td class=column align="right">工单状态</td>
						<td ><%=status%>&nbsp;</td>
						<td class=column align="right">执行时间</td>
						<td ><%=time%>&nbsp;</td>
					</TR>
					
					-->	
					<!-- <TR bgcolor="#FFFFFF">
						<td class=column align="right">工单原始信息</td>
						<td colspan=3><%=sheet_para_desc%>&nbsp;</td>
					</TR> -->
					<TR>
						<TD colspan="4" align="center" class=foot><INPUT TYPE="button"
							value=" 关 闭 " class=btn onclick="javascript:window.close()">
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