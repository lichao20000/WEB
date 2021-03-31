<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="head.jsp"%>
<%
	String gwType = request.getParameter("gw_type");
 %>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	gwShare_setGaoji();
});

function CheckForm(){
 	var __device_id = $("input[@name='deviceId']").val();

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
	
	$("input[@name='deviceId']").val(returnVal[2][0][0]);
	$("td[@id='tdDeviceSn']").append(returnVal[2][0][1]+" -"+returnVal[2][0][2]);
	$("td[@id='tdDeviceCityName']").append(returnVal[2][0][5]);	
	
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

			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									应用终端查询
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									对选择的家庭网关获取应用终端信息。
								</td>
							</tr>
						</table>
					</td>
				</tr>

				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post" action="<s:url value='/gtms/blocTest/deviceBlocTestInfo!queryDeviceInfoById.action'/>"
							target="dataForm" onsubmit="return CheckForm()">
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF" id="trDeviceResult"
												style="display: none">
												<td nowrap align="right" class=column width="15%">
													设备属地
												</td>
												<td id="tdDeviceCityName" width="35%">
												</td>
												<td nowrap align="right" class=column width="15%">
													设备序列号
													<input type="hidden" name="deviceId" value="">
												</td>
												<td id="tdDeviceSn" width="35%">
												</td>
												<td>
													<input type="hidden" name ="gw_type" value="<%=gwType %>"/>
												</td>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="5" align="right" class="green_foot">
													<INPUT TYPE="submit" value=" 获 取 " class=btn>
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</FORM>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<iframe id="dataForm" name="dataForm" frameborder="0" scrolling="no" width="99%" src=""></iframe>
		</TD>
	</TR>
</TABLE>
<%@ include file="../../foot.jsp"%>