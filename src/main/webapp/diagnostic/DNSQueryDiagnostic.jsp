<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

$(function(){
	gwShare_setGaoji();
});
  	function ExecMod(){ 
  		if(CheckForm()){
  			var t_obj = document.all('device_id');
  			
			var devicetype = $("input[@name='devicetype']").val();
          	var __device_id = $("input[@name='textDeviceId']").val();
		
			if(__device_id == null || __device_id == ""){
				alert("请先查询设备!");
				return false;
			}
			var param = "";
			if(devicetype =="snmp"){
				param = "&test_ip=" + $("input[@name='test_ip']").val() + "&time_out=" + $("input[@name='time_out']").val();
			}else{
				//var version = document.getElementById("httpVersion").options[document.getElementById("httpVersion").selectedIndex].value;
				param = "&Interface=" +document.getElementById("Interface").value+ "&DNSServerIP=" +$("input[@name='DNSServerIP']").val()+ "&NumberOfRepetitions=" +$("input[@name='NumberOfRepetitions']").val()+ "&Timeout=" +$("input[@name='Timeout']").val()+ "&DomainName=" +$("input[@name='DomainName']").val();
			}				
       		page = "DNSQueryDiagnostic_save.jsp?device_id=" + __device_id + "&devicetype=" + devicetype + param;
       		//alert(page);
			document.all("div_httpget").innerHTML = "正在载入诊断结果，请耐心等待....";
			document.all("childFrm").src = page;
		}else{
			return false;
		}	
    }

	function CheckForm(){
		var __device_id = $("input[@name='textDeviceId']").val();		
		if(__device_id == null || __device_id == ""){
			alert("请先查询设备!");
			return false;
		}
		var Interface = document.getElementById("Interface").value;
		if(Interface == ""){
	    	alert("请获取接口！");
			document.getElementById("Interface").focus();
			return false;
	    }
	   	var DNSServerIP = $("input[@name='DNSServerIP']").val();
        if(!IsNull(DNSServerIP,'诊断IP')){
			$("input[@name='DNSServerIP']").focus();
			return false;
		}
	  	
	  	var DomainName = $("input[@name='DomainName']").val();
        if(!IsNull(DomainName,'域名')){
			$("input[@name='DomainName']").focus();
			return false;
		}
		return true;					
	}

	function getInterfaceAct(){
		var __device_id = $("input[@name='textDeviceId']").val();
		
		if(__device_id == null || __device_id == ""){
			alert("请先查询设备!");
			return false;
		}		
		getHttpGetInterface(__device_id);

	}

	function getHttpGetInterface(_device_id){
		document.getElementById("divHttpGetInterface").innerHTML = "<font color=blue>正在获取...</font>";
		//document.getElementById("Interface").onfocus = null;
		var page = "getDNSQueryInterface.jsp";
		page += "?device_id="+_device_id;
		page += "&TT="+new Date();
		document.getElementById("childFrm").src = page;
	}
	function setHttpGetInterface(html){
		if(html == "调用采集失败" || html == "没有获取到Wan接口")
		{
			alert(html);
			document.getElementById("divHttpGetInterface").innerHTML = "<font color='red'>" + html + "</font>";
		}else{
			document.getElementById("divHttpGetInterface").innerHTML = html;
		}
	}
	
	function deviceResult(returnVal){
		
		$("tr[@id='trDeviceResult']").css("display","");

		$("td[@id='tdDeviceSn']").html("");
		$("td[@id='tdDeviceCityName']").html("");
		
		for(var i=0;i<returnVal[2].length;i++){
			deviceId = (returnVal[2][i][0]);
			/**
			$("input[@name='textDeviceId']").val(returnVal[2][i][0]);
			$("td[@id='tdDeviceSn']").append(returnVal[2][i][1]+" -"+returnVal[2][i][2]);
			$("td[@id='tdDeviceCityName']").append(returnVal[2][i][5]);		
			**/
		}
		showOther(deviceId);
		
		for(var i=0;i<returnVal[2].length;i++){
			$("input[@name='textDeviceId']").val(returnVal[2][i][0]);
			$("td[@id='tdDeviceSn']").append(returnVal[2][i][1]+" -"+returnVal[2][i][2]);
			$("td[@id='tdDeviceCityName']").append(returnVal[2][i][5]);		
		}
	}
	
	function showOther()
	{
		$("tr[@id='trUserData']").show();
		var url = '<s:url value='/gwms/blocTest/QueryCustomerInfo!query.action'/>'; 
		$.post(url,{
			device_id:deviceId
		},function(ajax){	
		    $("div[@id='UserData']").html("");
		    // document.getElementById("UserData").innerHTML = ajax;
			$("div[@id='UserData']").append(ajax);
			//$("tr[@id='tr_title_businessInfo']").show();
		});
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
											DNSQuery诊断
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
								<tr id="trUserData" style="display: none" bgcolor="#FFFFFF">
										<td class="colum" colspan="4">
											<div id="UserData" style="width: 100%; z-index: 1; top: 100px">
											</div>
										</td>
								</tr>
								<TR bgcolor="#FFFFFF" id="trDeviceResult" style="display: none;">
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
								<tr bgcolor="#FFFFFF" id="line1" style="display:">
									<td nowrap class=column>
										<div align="right" width="10%">
											接口:
										</div>
									</td>
									<td class=column nowrap>
										<span id="divHttpGetInterface" style="width:auto"> <input type="text" name="Interface" id="Interface" class="bk" size="16">
										</span> &nbsp;&nbsp;
										<input type="button" value="获 取" class=jianbian onclick="getInterfaceAct()">
									</td>
									<td nowrap class=column>
										<div align="right" width="10%">
											诊断IP
										</div>
									</td>
									<td class=column>
										<input type="text" name="DNSServerIP" id="DNSServerIP" class="bk">
									</td>
								</tr>
								<tr bgcolor="#FFFFFF" id="line2" style="display:">
									<td nowrap class=column>
										<div align="right" width="10%">
											域名
										</div>
									</td>
									<td class=column>
										<input type="text" name="DomainName" id="DomainName"class="bk">
									</td>
									<td nowrap class=column>
										<div align="right" width="10%">
											测试次数:
										</div>
									</td>
									<td class=column>
										<input type="text" name="NumberOfRepetitions" class="bk"
											size="16" value="5">
									</td>
								</tr>
								<tr bgcolor="#FFFFFF" id="line3" style="display:">
									<td nowrap class=column>
										<div align="right" width="10%">
											等待时间(ms):
										</div>
									</td>
									<td class=column colspan="3">
										<input type="text" name="Timeout" class="bk" size="16"
											value="5000">
									</td>
								</tr>
								<tr bgcolor="#FFFFFF">
									<td  colspan="4">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="0">
											<tr align="right" CLASS="green_foot">
												<td>
													<input type="hidden" name="devicetype" value="tr069">
													<INPUT TYPE="button" value=" 诊 断 " class=jianbian onclick="ExecMod()">
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
										<div id="div_httpget"
											style="width:100%; height:120px; z-index:1; top: 100px;"></div>
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
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display:none"></IFRAME>
			&nbsp;
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>

