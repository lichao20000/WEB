<%@ include file="/timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
	
	$(function(){
		$("input[@name='gwShare_queryResultType']").val("checkbox");
		gwShare_setGaoji();
		gwShare_setImport();
	});
	
	function deviceResult(returnVal){
		
		$("tr[@id='trDeviceResult']").css("display","");
		$("tr[@id='trDeviceResult0']").css("display","");
		$("tr[@id='trDeviceResult1']").css("display","");
		$("tr[@id='trDeviceResult2']").css("display","");
		
		$("td[@id='tdDeviceTtile1']").html("");
		$("td[@id='tdDeviceTtile2']").html("");
		$("td[@id='tdDeviceId']").html("");
		$("td[@id='tdDeviceOui']").html("");
		$("td[@id='tdDeviceSn']").html("");
		$("td[@id='tdDeviceLoopbackIp']").html("");
		$("td[@id='tdDeviceCityId']").html("");
		$("td[@id='tdDeviceCityName']").html("");
		
		$("td[@id='tdDeviceTtile1']").append("标示:"+returnVal[0]);
		$("td[@id='tdDeviceTtile2']").append("条件:"+returnVal[1]);
		for(var i=0;i<returnVal[2].length;i++){
			$("td[@id='tdDeviceId']").append("<br>"+returnVal[2][i][0]);
			$("td[@id='tdDeviceOui']").append("<br>"+returnVal[2][i][1]);
			$("td[@id='tdDeviceSn']").append("<br>"+returnVal[2][i][2]);
			$("td[@id='tdDeviceLoopbackIp']").append("<br>"+returnVal[2][i][3]);
			$("td[@id='tdDeviceCityId']").append("<br>"+returnVal[2][i][4]);
			$("td[@id='tdDeviceCityName']").append("<br>"+returnVal[2][i][5]);
		}
	}
	
</SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<table width="95%" border="0" align="center" cellpadding="0"
				cellspacing="0">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162">
									<div align="center" class="title_bigwhite">
										设备查询举例！
									</div>
								</td>
								<td></td>
							</tr>
						</table>
					</td>
				</tr>
				<TR >
					<td  >
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</TR>
				<tr>
					<td bgcolor="#FFFFFF">
					<FORM NAME="frm" METHOD="POST" ACTION="">
						<table width="100%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text">
							
								<TR bgcolor="#FFFFFF" id="trDeviceResult" style="display: none">
									<td nowrap colspan="6" align="left" class=column id="tdDeviceTtile1">
									</td>
								</tr>
								<TR bgcolor="#FFFFFF" id="trDeviceResult0" style="display: none">
									<td nowrap colspan="6" align="left" class=column id="tdDeviceTtile2">
									</td>
								</tr>
								<TR bgcolor="#FFFFFF" id="trDeviceResult1" style="display: none">
									<td nowrap align="right" class=column>
											device_id:
									</td>
									<td id="tdDeviceId">
									</td>
									<td nowrap class=column align="right">
										oui:
									</td>
									<td id="tdDeviceOui">
									</td>
									<td nowrap align="right" class=column>
											device_serialnumber:
									</td>
									<td id="tdDeviceSn">
									</td>
								</tr>
								<TR bgcolor="#FFFFFF" id="trDeviceResult2" style="display: none">
									<td nowrap class=column align="right">
										loopback_ip:
									</td>
									<td id="tdDeviceLoopbackIp">
									</td>
									<td nowrap align="right" class=column>
											city_id:
									</td>
									<td id="tdDeviceCityId">
									</td>
									<td nowrap class=column align="right">
										city_name:
									</td>
									<td id="tdDeviceCityName">
									</td>
								</tr>
							
							
						</table>
						</FORM>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
</TABLE>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<table width="95%" border="0" align="center" cellpadding="0"
				cellspacing="0">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162">
									<div align="center" class="title_bigwhite">
										调用介绍！
									</div>
								</td>
								<td></td>
							</tr>
						</table>
					</td>
				</tr>
				<TR >
					<td  >
						在需要调用的地方加入如下代码：
						<% 
						String imStr = "<%@include file='/gwms/share/gwShareDeviceQuery.jsp'%/>";
						out.println(imStr); 
						%>
					</td>
				</TR>
				<tr>
					<td bgcolor="#FFFFFF">
						<table width="100%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text">
							<TR bgcolor="#FFFFFF" id="" style="display: none">
								<td nowrap align="right" class=column>
										device_id:
								</td>
								<td id="tdDeviceId">
								</td>
								<td nowrap class=column align="right">
									oui:
								</td>
								<td id="tdDeviceOui">
								</td>
								<td nowrap align="right" class=column>
										device_serialnumber:
								</td>
								<td id="tdDeviceSn">
								</td>
							</tr>
							<TR bgcolor="#FFFFFF" id="" style="display: none">
								<td nowrap class=column align="right">
									loopback_ip:
								</td>
								<td id="tdDeviceLoopbackIp">
								</td>
								<td nowrap align="right" class=column>
										city_id:
								</td>
								<td id="tdDeviceCityId">
								</td>
								<td nowrap class=column align="right">
									city_name:
								</td>
								<td id="tdDeviceCityName">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
</TABLE>
<%@ include file="/foot.jsp"%>