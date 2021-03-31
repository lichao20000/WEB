<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/CheckForm.js"/>"></SCRIPT>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
var gw_type = "";
$(function(){
	gwShare_setGaoji();
	gw_type = '<%=request.getParameter("gw_type")%>';
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
				param = "&Interface=" +document.getElementById("Interface").value+ "&Host=" +$("input[@name='Host']").val()+ "&NumberOfRepetitions=" +$("input[@name='NumberOfRepetitions']").val()+ "&Timeout=" +$("input[@name='Timeout']").val()+ "&DataBlockSize=" +$("input[@name='DataBlockSize']").val();
			}				
       		page = "ipchecksave.jsp?device_id=" + __device_id + "&gw_type="+gw_type+"&devicetype=" + devicetype + param;
			document.all("div_ping").innerHTML = "正在载入诊断结果，请耐心等待....";
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
		var Host = $("input[@name='Host']").val();
        if(!IsNull(Host,'测试IP或域名')){
			$("input[@name='Host']").focus();
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
		getPingInterface(__device_id);

	}

	function getPingInterface(_device_id){
		document.getElementById("divPingInterface").innerHTML = "<font color=blue>正在获取...</font>";
		//document.getElementById("Interface").onfocus = null;
		var page = "getCucPingInterface.jsp";
		page += "?device_id="+_device_id;
		page += "&gw_type="+gw_type;
		page += "&TT="+new Date();
		document.getElementById("childFrm").src = page;
	}
	function setPingInterface(html){
		if(html == "调用采集失败" || html == "没有获取到Wan接口")
		{
			alert(html);
			document.getElementById("divPingInterface").innerHTML = "<font color='red'>" + html + "</font>";
		}else{
			document.getElementById("divPingInterface").innerHTML = html;
		}
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
<%@ include file="../../toolbar.jsp"%>


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
											Ping测试
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
									<%@ include file="../../gwms/share/gwShareDeviceQuery.jsp"%>
									</td>
								</TR>
								<TR bgcolor="#FFFFFF" id="trDeviceResult" style="display: none">
									<td nowrap align="right" class=column width="15%">
											设备属地
									</td>
									<td id="tdDeviceCityName">
									</td>
									<td nowrap align="right" class=column width="15%">
											设备序列号
											<input type="hidden" name="textDeviceId" value="">
									</td>
									<td id="tdDeviceSn">
									</td>
								</TR>
								<tr id="trUserData" style="display: none" bgcolor="#FFFFFF">
												<td class="colum" colspan="4">
													<div id="UserData"
														style="width: 100%; z-index: 1; top: 100px">
													</div>
												</td>
								</tr>
								<tr bgcolor="#FFFFFF" id="line1" style="display:">
									<td nowrap class=column width="15%">
										<div align="right">
											接口:
										</div>
									</td>
									<td class=column nowrap width="35%">
										<span id="divPingInterface" style="width:auto"><input type="text" name="Interface" id="Interface" class="bk" size="16">
											<!-- <select name="Interface" class="bk">
												<option value="-1">
													==请获取接口==
												</option>
											</select>  --> </span> &nbsp;&nbsp;
										<button onclick="getInterfaceAct()">获 取</button>
									</td>
									<td nowrap class=column width="15%">
										<div align="right">
											包大小:
										</div>
									</td>
									<td class=column width="35%">
										<input type="text" name="DataBlockSize" class="bk" size="16"
											value="32">&nbsp;(byte)
									</td>
								</tr>
								<tr bgcolor="#FFFFFF" id="line2" style="display:">
									<td nowrap class=column width="15%">
										<div align="right">
											测试IP或域名:
										</div>
									</td>
									<td class=column width="35%">
										<input type="text" name="Host" class="bk">
										<!--  
							<select name="Host" class="bk" >
								<option value="202.102.24.35">DNS服务器(202.102.24.35)</option>
								<option value="192.168.1.1">e家终端管理系统(202.102.15.115)</option>
								<option value="61.155.6.100">tom(http://www.tom.com)</option>
								<option value="61.172.201.194">sina(http://www.sina.com)</option>
							</select>
						-->
									</td>
									<td nowrap class=column width="15%">
										<div align="right">
											包数目:
										</div>
									</td>
									<td class=column width="35%">
										<input type="text" name="NumberOfRepetitions" class="bk"
											size="16" value="2">
									</td>
								</tr>
								<tr bgcolor="#FFFFFF" id="line3" style="display:">
									<td nowrap class=column width="15%">
										<div align="right">
											超时时间(ms):
										</div>
									</td>
									<td class=column colspan="3">
										<input type="text" name="Timeout" class="bk" size="16"
											value="2000">
									</td>

								</tr>
								<tr bgcolor="#FFFFFF" id="line4" style="display:none">
									<td nowrap align="right" class=column width="15%">
											测试地址:
									</td>
									<td class=column width="35%">
										<input type="text" name="test_ip" class="bk" size="16"
											value="">(例：www.sohu.com)
									</td>
									<td nowrap align="right" class=column width="15%">
											超时时间(s):
									</td>
									<td class=column width="35%">
										<input type="text" name="time_out" class="bk" size="16"
											value="20">
									</td>
								</tr>
								<tr bgcolor="#FFFFFF">
									<td  colspan="4">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="0">
											<tr align="right" CLASS="green_foot">
												<td>
													<input type="hidden" name="devicetype" value="tr069">
													<button onclick="ExecMod()">诊 断</button>
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
										<div id="div_ping"
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
<%@ include file="../../foot.jsp"%>

