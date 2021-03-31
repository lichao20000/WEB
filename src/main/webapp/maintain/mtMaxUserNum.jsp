<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>设置终端最大上网数维护</title>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
var chekType = 3;
function ShowDialog(valu){
	//3：属地型号，1:用户名和电话，2：设备序列号和IP
	var queryType = valu;
	chekType = queryType;
	//alert(chekType);
	if(queryType==3){
		document.getElementById("userQuery").style.display="none";
		document.getElementById("deviceQuery").style.display="none";
		document.getElementById("cityQuery1").style.display="";
		document.getElementById("cityQuery2").style.display="";
	}else if(queryType==1){
		document.getElementById("cityQuery1").style.display="none";
		document.getElementById("cityQuery2").style.display="none";
		document.getElementById("deviceQuery").style.display="none";
		document.getElementById("userQuery").style.display="";
	}else {
		document.getElementById("cityQuery1").style.display="none";
		document.getElementById("cityQuery2").style.display="none";
		document.getElementById("userQuery").style.display="none";
		document.getElementById("deviceQuery").style.display="";
	}
}

$(document).ready(function(){
	init();
}); 


function selectAll(elmID){
	t_obj = document.all(elmID);
	if(!t_obj) return;
	obj = event.srcElement;
	if(obj.checked){
		if(typeof(t_obj) == "object" ) {
			if(typeof(t_obj.length) != "undefined") {
				for(var i=0; i<t_obj.length; i++){
					t_obj[i].checked = true;
				}
				document.frm.allSelected.value="1";
			} else {
				t_obj.checked = true;
			}
		}
	
	}else{
		if(typeof(t_obj) == "object" ) {
			if(typeof(t_obj.length) != "undefined") {
				for(var i=0; i<t_obj.length; i++){
					t_obj[i].checked = false;
				}
				document.frm.allSelected.value="0";
			} else {
				t_obj.checked = false;
			}
		}
	}
}

function showChild(selectname){
	if(selectname == 'vendor_name'){
		queryModel(selectname);
	}else{
		queryVersion(selectname);
	}
}

function init(){
	queryCity();
	queryVendor();
}

function queryCity(){
	var url = "devVenderModelAction!getCityList.action";
	$.post(url,{
    },function(mesg){
    	//alert(mesg);
    	$("#cityList").html(mesg);
    });
}

function queryVendor(){
	var url = "devVenderModelAction!getVendorList.action";
	$.post(url,{
    },function(mesg){
    	//alert(mesg);
    	$("#vendorList").html(mesg);
    });
}
function queryModel(selectname){
	var vend = document.all(selectname).value;
	var url = "devVenderModelAction!getDeviceModelList.action";
	$.post(url,{
		vendorname:vend
    },function(mesg){
    	$("#modelList").html(mesg);
    });
}
function queryVersion(selectname){
	var vend = document.all("vendor_name").value;	
	var model = document.all(selectname).value;
	var url = "devVenderModelAction!getVersionList.action";
	$.post(url,{
		vendorname:vend,
		deviceModel:model
    },function(mesg){
    	$("#versionList").html(mesg);
    });
}

function queryDevice(){

	var uname = document.all("username");
	var utelephone = document.all("telephone");
	
	var devSerial = document.all("deviceSerialnumber");
	var ipaddress = document.all("ipAddress");
	
	var city = document.all("city_id");
	var vendname = document.all("vendor_name");
	var device_model = document.all("device_model");
	var device_model_value = "-1";
	var device_type = document.all("softwareversion");
	var device_type_value = "-1"
	
	var url = "devVenderModelAction!getDeviceInfoList.action";
	//alert(chekType);
	if(chekType == 3){
		var ucity_id = city.value;
		//如果未选择的设备版本
		if(device_type){
			device_type_value = device_type.value;
		}
		//如果未选择的设备型号
		if(device_model){
			device_model_value = device_model.value;
		}
		$.post(url,{
			checkType:3,
			city_id:ucity_id,
			vendorname:vendname.value,
			deviceModel:device_model_value,
			softwareversion:device_type_value,
			gwType:2
   		},function(mesg){
   			$("#div_device").html(mesg);
   		});
		//}
	}else if (chekType == 2){
		var devSerialnumber = devSerial.value;
		var ip = ipaddress.value;
		if((devSerialnumber.trim() == "" || devSerialnumber.length < 5) && ip.trim() == ""){
			alert("5位序列号或ip地址必填");
			devSerial.focus();
			return false;
		}
		$.post(url,{
			checkType:2,
			deviceSerialnumber:devSerialnumber,
			ipAddress:ip,
			gwType:2
    	},function(mesg){
    		$("#div_device").html(mesg);
    	});
	}else {
		var usname = uname.value;
		var usphone = utelephone.value;
		if(usname.trim() == "" && usphone.trim() == ""){
			alert("用户名或电话地址必须填一个");
			uname.focus();
			return false;
		}
		$.post(url,{
			checkType:1,
			username:usname,
			telephone:usphone,
			gwType:2
    	},function(mesg){
    		$("#div_device").html(mesg);
    	});
	}
}

function getMaxUserNumFromDB(){

	var devIdsStr = getDeviceListArr();
	
	var url = "maintainMaxUserNum!getMaxUserNum.action";
	$.post(url,{
		fromDB:1,
		devIds: devIdsStr
    },function(mesg){
    	$("#div_maxUserNum").html(mesg);
    	$("#div_editMUN").html("");
    });
	
}

function getMaxUserNumFromDev() {
	var devIdsStr = getDeviceListArr();
	
	var url = "maintainMaxUserNum!getMaxUserNum.action";
	$.post(url,{	
		fromDB:0,
		devIds: devIdsStr
    },function(mesg){
    	$("#div_maxUserNum").html(mesg);
    	$("#div_editMUN").html("");
    	//document.getElementById("div_maxUserNum").innerHTML = mesg;
    	//document.getElementById("div_editMUN").innerHTML = "";
    });
}

function getDeviceListArr() {
	t_obj = document.all("device_id");
	var devIdsStr = "";
	var devList = "";
	//alert(t_obj);
	//alert(t_obj.length);
	//alert(t_obj.value);
	if(!t_obj) return;

	if(typeof(t_obj) == "object" ) {
		if(typeof(t_obj.length) != "undefined") {
			for(var i=0; i<t_obj.length; i++){
				if (t_obj[i].checked) {
					devList = t_obj[i].value;
					devIdsStr += devList.split("|")[0] + "|";
				}
				
			}
			
		} else {
			if (t_obj.checked) {
				devList = t_obj.value;
				devIdsStr += devList.split("|")[0];
			}
		}
	}
	
	//alert(devIds);
	return devIdsStr;
}

function delMUN(ts, device_id) {
	//alert(device_id);
	if(!confirm("确实要从设备上删除该结点吗？")){
		return false;
	}
	alert(device_id);
}

function editMUN(ts, device_id, mode, totalNum) {
	var url = "maintainMaxUserNum!editMUN.action";
	$.post(url,{
		deviceId:device_id,
		mode:mode,
		totalNum:totalNum
    },function(mesg){
    	$("#div_editMUN").html(mesg);
    });
    
}

function setParams(device_id) {
	var url = "maintainMaxUserNum!setParams.action";
	$.post(url,{
		deviceId:device_id,
		mode:$("#modeId").val(),
		totalNum:$("#totalNumId").val()
    },function(mesg){
    	alert(mesg);
    });
}
//----------------------------属地-厂商-型号-版本-设备列表-------------------------------

function checkForm(){
	//var vpi = document.frm.vpi.value;
	//var vci = document.frm.vci.value;
	//var devObj = document.all("device_id"); 
	//var needSoft = document.frm.needUpSoftware.value;
	//var devSelected = false;
	//if(vpi=="" || vci==""){
	//	alert("请您输入pvc");
	//	return false;
	//}
	document.frm.submitForm.disabled = true;
	document.frm.submit();
}

function softShow(valu){
	if(valu == "1"){
		document.getElementById("softUp").style = "display:";
	}else{
		document.getElementById("softUp").style = "display:none";
	}
}
</SCRIPT>
</head>
<body>
<form name="frm">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
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
						任务定制</td>
						<td nowrap><img src="../images/attention_2.gif" width="15"
							height="12"> &nbsp;查询方式： <input type="radio" value="1" onclick="ShowDialog(this.value)"
							name="checkType">按用户&nbsp;&nbsp; <input type="radio" value="3"
							onclick="ShowDialog(this.value)" name="checkType" checked>按属地和型号&nbsp;&nbsp;
						<input type="radio" value="2" onclick="ShowDialog(this.value)" name="checkType">按设备
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td>
					<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
						<TR>
							<TD bgcolor=#999999>
								<table border=0 cellspacing=1 cellpadding=2 width="100%">
									<TR>
										<TH colspan="4" align="center">
											用户最大上网数维护
										</TH>
									</TR>
									<tr id="cityQuery1" bgcolor="#FFFFFF" style="display:">
										<td align="right" width="15%">属地：</td>
										<td align="left" width="30%"><div id="cityList"></div></td>
										<td align="right">设备厂商：</td>
										<td align="left"><div id="vendorList"></div></td>
									</tr>
									<tr id="cityQuery2" bgcolor="#FFFFFF" style="display:">
										<td align="right" width="15%">设备型号：</td>
										<td align="left" width="30%"><div id="modelList"></div></td>
										<td align="right">设备版本：</td>
										<td align="left"><div id="versionList"></div></td>
									</tr>
									<tr id="userQuery" bgcolor="#FFFFFF" style="display:none">
										<td align="right" width="15%">用户名：</td>
										<td align="left" width="30%"><input type="text" name="username"></td>
										<td align="right">电话：</td>
										<td align="left"><input type="text" name="telephone"></td>
									</tr>
									<tr id="deviceQuery" bgcolor="#FFFFFF" style="display:none">
										<td align="right">设备序列号：</td>
										<td align="left"><input type="text" name="deviceSerialnumber">&nbsp;&nbsp;最后六位</td>
										<td align="right">IP地址：</td>
										<td align="left"><input type="text" name="ipAddress"></td>
									</tr>
									<tr>
										<td colspan="4" align="right" CLASS="green_foot">
											<input type="button" value=" 查 询 " onclick="queryDevice();">
										</td>
									</tr>
									<TR bgcolor="#FFFFFF">
										<TD align="right">
											设备列表：
											<br>
											<input type="hidden" name="allSelected" value="0">
											<INPUT TYPE="checkbox" onclick="selectAll('device_id')" name="device">
											全选
										</TD>
										<TD colspan="3">
											<div id="div_device" style="width:100%; height:150px; z-index:1; top: 100px; overflow:scroll">
											</div>
										</TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD colspan="4" align="right" CLASS="green_foot">
											<INPUT TYPE="button" name="btnFromDB" value=" 从数据库查询 " class=btn onclick="getMaxUserNumFromDB()">
											&nbsp;
											<INPUT TYPE="button" name="btnFromDev" value=" 从设备获取 " class=btn onclick="getMaxUserNumFromDev()">
										</TD>
									</TR>
								</table>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td nowrap>
							<div id="div_maxUserNum" >
						</div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td nowrap>
							<div id="div_editMUN" >
						</div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			
		</table>	
	</TD>
  </TR>
  <tr><td height=20></td></tr>
</TABLE>
</form>
</body>
<%@ include file="../foot.jsp"%>
</html>