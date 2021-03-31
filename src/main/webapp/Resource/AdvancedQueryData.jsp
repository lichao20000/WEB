<%@ include file="../timelater.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=GBK"%>
<link rel="stylesheet" href="../css/listview.css" type="text/css">

<TABLE id="devTable" border=0 cellspacing=0 cellpadding=0 width="100%" oncontextmenu="return false;" style="backgroud: #FFFFFF">
	<TR>
		<TD>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="idTable">
					<TR bgcolor="#ffffff">
						<TH align="right" width="25%">设备序列号</TH>
						<TH align="right" width="20%">设备IP</TH>
						<TH align="right" width="20%">客户名称</TH>
						<TH align="right" width="20%">业务帐号</TH>
						<TH align="right" width="15%">属地</TH>
					</TR>
					<s:if test="resultNum==0">
						<TR bgcolor="#ffffff">
							<td align="left" colspan="5" class=column1>查询的设备在系统中不存在！</td>
						</TR>
					</s:if>
					<s:iterator value="devList">
						<tr bgcolor="#FFFFFF" oncontextmenu="show('<s:property value="device_id"/>')"  ondblclick="show('<s:property value="device_id"/>')" 
							class=trOut onmouseover="className='trOver'" onmouseout="className='trOut'">
							<td width="25%" align="center">
								<a href="javascript:DetailDevice('<s:property value="device_id" />')"><s:property value="oui" />-<s:property value="device_serialnumber" /></a>
							</td>
							<td width="20%" align="center">
								<s:property value="loopback_ip" />
							</td>
							<td width="20%" align="center">
								<a href="javascript:openCust('<s:property value="customer_id" />')"><s:property value="customer_name" /></a>
							</td>
							<td width="20%" align="center">
								<a href="javascript:GoContent('<s:property value="user_id" />')"><s:property value="username" /></a>
								
							</td>
							<td width="15%" align="center">
								<s:property value="city_name" />
								<input type="hidden" value='<s:property value="device_id"/>' name="device_id" id="device_id" />
							</td>
						</tr>
					</s:iterator>
				</TABLE>
				</TD>
			</TR>
			<tr style="height:10px;" bgcolor="#FFFFFF" style="border:1px solid #999999" >
				<TD>&nbsp;</TD>
			</tr>
		</TABLE>
		
		<table width="100%" border=0 align="center" cellpadding="1" id="trUserData" style="display: none"
			cellspacing="1" bgcolor="#999999" class="text">
			<tr bgcolor="#FFFFFF">
				<td class="colum" colspan="4">
					<div id="UserData"
						style="width: 100%; z-index: 1; top: 100px">
					</div>
				</td>
			</tr>
		</table>
		</TD>
	</TR>
</TABLE>