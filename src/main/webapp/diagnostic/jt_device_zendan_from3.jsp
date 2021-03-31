<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">

var gw_type = '<%=request.getParameter("gw_type")%>';

$(function(){
	gwShare_setGaoji();
});

     function ExecMod(){
     		if(CheckForm()){
		        page = "jt_device_zendan_from3_save.jsp?device_id=" +$("input[@name='device_id']").val()+ "&oid_type=1&type=" + $("input[@name='type'][@checked]").val();
		        page = page + "&gw_type="+gw_type;
		        //page = "jt_device_zendan_from3_save.jsp?device_id=" +checkvalue+ "&oid_type=1";
				document.all("div_ping").innerHTML = "正在载入诊断结果，请耐心等待....";
				document.all("childFrm").src = page;
			}else{
				return false;
			}	
          }

function CheckForm(){ 	
	var __device_id = $("input[@name='device_id']").val();

	if(__device_id == null || __device_id == ""){
		alert("请先查询设备!");
		return false;
	}
	return true;
}
	
function deviceResult(returnVal){
		
	$("tr[@id='trDeviceResult']").css("display","");

	$("td[@id='tdDeviceSn']").html("");
	$("td[@id='tdDeviceCityName']").html("");
	
	$("input[@name='device_id']").val(returnVal[2][0][0]);
	$("td[@id='tdDeviceSn']").append(returnVal[2][0][1]+" -"+returnVal[2][0][2]);
	$("td[@id='tdDeviceCityName']").append(returnVal[2][0][5]);	
	if(gw_type=='2'){
	$("tr[@id='trUserData']").show();
	var url = '<s:url value='/gwms/blocTest/QueryCustomerInfo!query.action'/>'; 
	$.post(url,{
		device_id:returnVal[2][0][0]
	},function(ajax){	
	    $("div[@id='UserData']").html("");
		$("div[@id='UserData']").append(ajax);
	});
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

			<table width="95%" border="0" align="center" cellpadding="0"
				cellspacing="0">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162">
									<!-- 山西联通左上角标题 -->
									<ms:inArea areaCode="sx_lt" notInMode="false">
										<div style="position: relative; left: 62px" class="title_bigwhite">
											重启
										</div>
									</ms:inArea>
									<ms:inArea areaCode="sx_lt" notInMode="true">
										<div align="center" class="title_bigwhite">
											重启
										</div>
									</ms:inArea>
									
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
									<div id="selectDevice">
										<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
									</div>
								</td>
							</TR>

							<tr bgcolor="#FFFFFF">
								<td colspan="4">
									<FORM NAME="frm" METHOD="POST" ACTION=""
										onSubmit="return CheckForm();">
										<table width="100%" border="0" cellspacing="1" cellpadding="0"
											bgcolor="#999999">
											<TR bgcolor="#FFFFFF" id="trDeviceResult"
												style="display: none">
												<td align="right" width="15%" class="column">
													设备属地
												</td>
												<td id="tdDeviceCityName" width="35%">
												</td>
												<td align="right" width="15%" class="column">
													设备序列号
													<input type="hidden" name="device_id" value="">
												</td>
												<td id="tdDeviceSn" width="35%">
												</td>

											</TR>
											<tr id="trUserData" style="display: none" bgcolor="#FFFFFF">
												<td class="colum" colspan="4">
													<div id="UserData"
														style="width: 100%; z-index: 1; top: 100px">
													</div>
												</td>
											</tr>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width=15%>
													配置方式:
												</TD>
												<TD colspan=3>
													<input type="radio" name="type" checked id="rd1" class=btn
														value="1">
													<label for="rd1">
														TR069
													</label>
													&nbsp;
													<!-- 暂时屏蔽SNMP -->
													<!-- <input type="radio" name="type" id="rd2" class=btn
														value="2">
													<label for="rd2">
														SNMP
													</label> -->
												</TD>
											</TR>
											<tr align="right" CLASS="green_foot">
												<td colspan="4">
													<INPUT TYPE="button" value=" 重 启 " class=jianbian
														onclick="ExecMod()">
													&nbsp;&nbsp;
													<INPUT TYPE="hidden" name="action" value="add">
													&nbsp;&nbsp;
												</td>
											</tr>
										</table>
									</FORM>
								</td>
							</tr>

							<TR bgcolor="#FFFFFF">
								<TH colspan="4">
									重启操作结果
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
<SCRIPT LANGUAGE="JavaScript">
<!--
//-->
</SCRIPT>