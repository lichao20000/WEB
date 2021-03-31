<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">	

var gw_type = "<%=request.getParameter("gw_type")%>";

$(function(){
	gwShare_setGaoji();
});

function ExecMod(){
   	if(CheckForm()){
   		page = "jt_device_zendan_from5_save.jsp?gw_type="+<%=request.getParameter("gw_type")%>+"&device_id=" + $("input[@name='device_id']").val();
   		page += "&dslWan=" + $("select[@id='dslWan']").val();
		document.all("div_DSL").innerHTML = "正在载入诊断结果，请耐心等待....";
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
	
	var interface = document.getElementById("dslWan");
	if(interface == undefined || interface.value=="")
	{
		alert("请获取接口！");
		document.getElementById("dslWan").focus();
		return false;
	}
	return true;
}

var device_id = "";
function deviceResult(returnVal){
		
	$("tr[@id='trDeviceResult']").css("display","");

	$("td[@id='tdDeviceSn']").html("");
	$("td[@id='tdDeviceCityName']").html("");
	
	$("input[@name='device_id']").val(returnVal[2][0][0]);
	device_id = returnVal[2][0][0];
	$("td[@id='tdDeviceSn']").append(returnVal[2][0][1]+" -"+returnVal[2][0][2]);
	$("td[@id='tdDeviceCityName']").append(returnVal[2][0][5]);	
	
	$("tr[@id='interfaceTR']").show();
	
	//$("tr[@id='trUserData']").show();
	//var url = '<s:url value="/gwms/blocTest/QueryCustomerInfo!query.action"/>'; 
	//$.post(url,{
	//	device_id:returnVal[2][0][0]
	//},function(ajax){
	//    $("div[@id='UserData']").html("");
	//	$("div[@id='UserData']").append(ajax);
	//	$("tr[@id='interfaceTR']").show();
	//});
}

function getInterface()
{
	$("span[@id='interfaceSpan']").html("<font color=blue>正在获取...</font>");
	//alert(device_id);
	//document.getElementById("Interface").onfocus = null;
		var page = "getDslWan.jsp";
		page += "?device_id="+ device_id;
		page += "&TT="+new Date();
		document.getElementById("childFrm").src = page;
}

function setDslWan(html)
{
	if(html == "调用采集失败" || html == "没有获取到Wan接口")
	{
		alert(html);
		document.getElementById("interfaceSpan").innerHTML = "<font color='red'>" + html + "</font>";
	}else{
		var sel =  document.createElement("select");
		sel.name = "dslWan";
		sel.id = "dslWan";
		sel.className = "column";
		var wans = html.split("\6");
		for(i=0; i<wans.length; i++)
		{
			var option = document.createElement("option");
			sel.options.add(option);
			option.innerHTML = wans[i];
			option.value = wans[i];
		}
		document.getElementById("interfaceSpan").innerHTML = sel.outerHTML;
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
											DSL测试
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
								<tr bgcolor="#FFFFFF">
									<td colspan="4">
										<table width="100%" border="0" cellspacing="1" cellpadding="1"
											bgcolor="#999999">
											<TR bgcolor="#FFFFFF" id="trDeviceResult"
												style="display: none">
												<td align="right" class=column width="15%">
													设备属地
												</td>
												<td id="tdDeviceCityName" width="35%">
												</td>
												<td align="right" class=column width="15%">
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
											<tr bgcolor="#FFFFFF" id="interfaceTR" style="display: none">
												<td class="column" align="right">
													WAN连接:
												</td>
												<td colspan="3" valign="top">
													<span id="interfaceSpan" >
														<input type="text" size="15"  class="bk" id="dslWan" />
													</span>&nbsp;<button onclick="getInterface()" style="padding-bottom:10px">获 取</button>
												</td>
											</tr>
											<tr align="right" CLASS="green_foot">
												<td colspan="4">
													<button class=btn
														onclick="ExecMod()">诊 断</button>
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
										诊断结果
									</TH>
								</TR>
								<TR bgcolor="#FFFFFF">
									<td colspan="4" valign="top" class=column>
										<div id="div_DSL" style="width: '700px'; height: '120px'"></div>
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
