<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">

var gw_type = '<%=request.getParameter("gw_type")%>';

$(function(){
	gwShare_setGaoji();
});
     function ExecMod(){
     		if(CheckForm()){
     			var _device_id = $("input[@name='textDeviceId']").val();	
				if(_device_id == null || _device_id == ""){
					alert("请先查询设备!");
					return false;
				}
		        page = "jt_device_zendan_from4_save.jsp?device_id=" +_device_id + "&oid_type=2&type=1&gw_type="+gw_type;
				document.all("div_ping").innerHTML = "正在载入诊断结果，请耐心等待....";
				document.all("childFrm").src = page;
			}else{
				return false;
			}	
          }
	
	function CheckForm(){
		var _device_id = $("input[@name='textDeviceId']").val();	
		if(_device_id == null || _device_id == ""){
			alert("请先查询设备!");
			return false;
		}	
		return true;		
	}
	
	function deviceResult(returnVal){
		
		$("tr[@id='trDeviceResult']").css("display","");

		$("td[@id='tdDeviceSn']").html("");
		$("td[@id='tdDeviceCityName']").html("");
		
		for(var i=0;i<returnVal[2].length;i++){
			$("input[@name='textDeviceId']").val(returnVal[2][i][0]);
			$("td[@id='tdDeviceSn']").append(returnVal[2][i][1]+" -"+returnVal[2][i][2]);
			$("td[@id='tdDeviceCityName']").append(returnVal[2][i][5]);		
		}
	}
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="POST" ACTION=""
				onSubmit="return CheckForm();">
				<table width="95%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162">
										<div align="center" class="title_bigwhite">
											恢复出厂设置
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF">
							<table width="100%" border=0 align="center" cellpadding="1"
								cellspacing="1" bgcolor="#999999" class="text">
								<TR bgcolor="#FFFFFF">
									<td colspan="4">
										<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
									</td>
								</TR>
								<TR bgcolor="#FFFFFF" id="trDeviceResult" style="display: none">
									<td nowrap align="right" class=column width="15%">
										设备属地
									</td>
									<td id="tdDeviceCityName" width="35%">
									</td>
									<td nowrap align="right" class=column width="15%">
										设备序列号
										<input type="hidden" name="textDeviceId" value="">
									</td>
									<td id="tdDeviceSn" width="35%">
									</td>
								</TR>
								<tr bgcolor="#FFFFFF">
									<td colspan="4">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr align="right" CLASS="green_foot">
												<td>
													<INPUT TYPE="button" value="恢复出厂设置" class=jianbian
														onclick="ExecMod()">
													&nbsp;&nbsp;
													<INPUT TYPE="hidden" name="action" value="add">
													&nbsp;&nbsp;
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<TR bgcolor="#FFFFFF">
									<TH colspan="4">
										恢复出厂设置操作结果
									</TH>
								</TR>
								<TR bgcolor="#FFFFFF">
									<td colspan="4" valign="top" class=column>
										<div id="div_ping"
											style="width: 100%; height: 120px; z-index: 1; top: 100px;"></div>
									</td>
								</TR>
							</table>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
			&nbsp;
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>