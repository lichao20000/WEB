<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒ROM版本自动升级策略新增</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT type="text/javascript" SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

<script type="text/javascript">
var ruleId=$("input[@name='rule_id']").val();
$(function(){
	vendorInit();
});	

// 新增
function add()
{
	var devicetype_id=$("input[@name='devicetype_id']").val();
	var version_id=$("select[@name='version_id']").val();
	var dev_net_type=$("input[@name='dev_net_type']").val();
	var user_net_type=$("select[@name='user_net_type']").val();
	var version_net_type=$("td[@id='version_net_type']").html();
	
	if("-1" == devicetype_id){
		alert("请选择软件版本！");
		return false;
	}else if("-1" == version_id){
		alert("请选择目标版本！");
		return false;
	}
	
//	unknown_net,private_net,public_net
	if(dev_net_type=="专网"){
		dev_net_type="private_net";
	}else if(dev_net_type=="公网"){
		dev_net_type="public_net";
	}else{
		dev_net_type="unknown_net";
	}
	
	if(version_net_type=="专网"){
		version_net_type="private_net";
	}else if(version_net_type=="公网"){
		version_net_type="public_net";
	}else{
		version_net_type="unknown_net";
	}
	
	if(!checkNetType(dev_net_type,user_net_type,version_net_type)){
		alert("用户网络类型与目标ROM版本不匹配，不允许配置!");
		return false;
	}
	
	var url = "<s:url value='/gtms/stb/resource/autoSoftUpRule!add.action'/>";
	$.post(url, {
		devicetype_id:devicetype_id,
		dev_net_type:dev_net_type,
		user_net_type:user_net_type,
		version_net_type:version_net_type,
		version_id:version_id
		}, function(ajax) {
			alert(ajax);
	});
}

//新增
function edit()
{
	var devicetype_id=$("input[@name='devicetype_id']").val();
	var version_id=$("select[@name='version_id']").val();
	var dev_net_type=$("input[@name='dev_net_type']").val();
	var user_net_type=$("select[@name='user_net_type']").val();
	var version_net_type=$("td[@id='version_net_type']").html();
	
	if("-1" == devicetype_id){
		alert("请选择软件版本！");
		return false;
	}else if("-1" == version_id){
		alert("请选择目标版本！");
		return false;
	}
	
	if(dev_net_type=="专网"){
		dev_net_type="private_net";
	}else if(dev_net_type=="公网"){
		dev_net_type="public_net";
	}else{
		dev_net_type="unknown_net";
	}
	
	if(version_net_type=="专网"){
		version_net_type="private_net";
	}else if(version_net_type=="公网"){
		version_net_type="public_net";
	}else{
		version_net_type="unknown_net";
	}
	
	var rule_id=$("input[@name='rule_id']").val();
	var edit_devicetype_id=$("input[@name='edit_devicetype_id']").val();
	var edit_dev_net_type=$("input[@name='edit_dev_net_type']").val();
	var edit_version_net_type=$("input[@name='edit_version_net_type']").val();
	var edit_user_net_type=$("input[@name='edit_user_net_type']").val();
	var edit_version_id=$("input[@name='edit_version_id']").val();
	
	if(edit_devicetype_id==devicetype_id 
			&& edit_dev_net_type==dev_net_type 
			&& edit_version_net_type==version_net_type 
			&& edit_user_net_type==user_net_type 
			&& edit_version_id==version_id)
	{
		alert("请至少修改一条数据！");
		return false;
	}
	
	if(!checkNetType(dev_net_type,user_net_type,version_net_type)){
		alert("用户网络类型与目标ROM版本不匹配，不允许配置!");
		return false;
	}
	
	var check_flag='1';
	if(edit_devicetype_id==devicetype_id 
			&& edit_dev_net_type==dev_net_type){
		check_flag='0';
	}
	
	var url = "<s:url value='/gtms/stb/resource/autoSoftUpRule!edit.action'/>";
	$.post(url, {
		rule_id:rule_id,
		devicetype_id:devicetype_id,
		dev_net_type:dev_net_type,
		user_net_type:user_net_type,
		version_net_type:version_net_type,
		version_id:version_id,
		check_flag:check_flag
		}, function(ajax) {
			alert(ajax);
			if("规则更新成功！"==ajax){
				window.close();
			}
	});
}

//厂商初始化
function vendorInit()
{
	clearTd();
	$("tr[@id='trVersionDesc']").css("display","none");
	$("tr[@id='trAdd']").css("display","none");
	
	var url = "<s:url value='/gtms/stb/resource/autoSoftUpRule!getVendorS.action'/>";
	
	$.post(url,{},
		function(ajax){
			$("select[@name='vendorId']").empty();
			$("select[@name='vendorId']").append("<option value='-1'>请选择厂商</option>");
			
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length))
				{
					var edit_vendorId=$("input[@name='edit_vendorId']").val();
					for(var i=0;i<lineData.length;i++)
					{
						var oneElement = lineData[i].split("$");
						var optionValue;
						if(oneElement[0]==edit_vendorId && ruleId!="-1"){
							optionValue="<option value='"+oneElement[0]+"' selected>"+oneElement[1]+"</option>";
						}else{
							optionValue="<option value='"+oneElement[0]+"'>"+oneElement[1]+"</option>";
						}
						$("select[@name='vendorId']").append(optionValue);
					}
					
					if(ruleId!="-1"){
						vendorChange();
					}
				}
			}
			
			$("select[@name='deviceModelId']").empty();
			$("select[@name='deviceModelId']").append("<option value='-1'>请选择型号</option>");
			
			$("select[@name='hardwareversion']").empty();
			$("select[@name='hardwareversion']").append("<option value='-1'>请选择硬件版本</option>");
			
			$("select[@name='softwareversion']").empty();
			$("select[@name='softwareversion']").append("<option value='-1'>请选择软件版本</option>");
		});
}

// 厂商改变触发
function vendorChange()
{
	clearTd();
	$("tr[@id='trVersionDesc']").css("display","none");
	$("tr[@id='trAdd']").css("display","none");
	
	var vendorId = $("select[@name='vendorId']").val();
	var url = "<s:url value='/gtms/stb/resource/autoSoftUpRule!getDeviceModelS.action'/>";
	if(vendorId=="-1"){
		return;
	}
	
	$.post(url,{
		vendorId:vendorId
		},function(ajax){
			$("select[@name='deviceModelId']").empty();
			$("select[@name='deviceModelId']").append("<option value='-1'>请选择型号</option>");
			
			if(ajax!="")
			{
				var lineData = ajax.split("#");
				var edit_deviceModelId=$("input[@name='edit_deviceModelId']").val();
				
				if(typeof(lineData)&&typeof(lineData.length))
				{
					for(var i=0;i<lineData.length;i++)
					{
						var oneElement = lineData[i].split("$");
						var optionValue;
						if(oneElement[0]==edit_deviceModelId && ruleId!="-1"){
							optionValue="<option value='"+oneElement[0]+"' selected>"+oneElement[1]+"</option>";
						}else{
							optionValue = "<option value='"+oneElement[0]+"'>"+oneElement[1]+"</option>";
						}
						$("select[@name='deviceModelId']").append(optionValue);
					}
					
					if(ruleId!="-1"){
						deviceModelChange();
					}
				}
			}
			
			$("select[@name='softwareversion']").empty();
			$("select[@name='softwareversion']").append("<option value='-1'>请选择软件版本</option>");
			
			$("select[@name='hardwareversion']").empty();
			$("select[@name='hardwareversion']").append("<option value='-1'>请选择硬件版本</option>");
		});
}

// 型号改变触发
function deviceModelChange()
{
	clearTd();
	$("tr[@id='trVersionDesc']").css("display","none");
	$("tr[@id='trAdd']").css("display","none");
	
	var deviceModelId = $("select[@name='deviceModelId']").val();
	
	if(deviceModelId=="-1"){
		return;
	}
	var url = "<s:url value='/gtms/stb/resource/autoSoftUpRule!getHardwareversionS.action'/>";
	$.post(url,{
		deviceModelId:deviceModelId
		},function(ajax){
			$("select[@name='hardwareversion']").empty();
			$("select[@name='hardwareversion']").append("<option value='-1'>请选择硬件版本</option>");
			
			if(ajax!="")
			{
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length))
				{
					var edit_hardwareversion=$("input[@name='edit_hardwareversion']").val();
					for(var i=0;i<lineData.length;i++)
					{
						var optionValue;
						if(edit_hardwareversion==lineData[i] && ruleId!="-1"){
							optionValue="<option value='"+lineData[i]+"' selected>"+lineData[i]+"</option>  ";
						}else{
							optionValue="<option value='"+lineData[i]+"'>"+lineData[i]+"</option>  ";
						}
						
						$("select[@name='hardwareversion']").append(optionValue);
					}
					
					if(ruleId!="-1"){
						hardwareversionChange();
					}
				}
			}
			  
			$("select[@name='softwareversion']").empty();
			$("select[@name='softwareversion']").append("<option value='-1'>请选择硬件版本</option>");
		});
}

//硬件版本改变触发
function hardwareversionChange()
{
	clearTd();
	$("tr[@id='trVersionDesc']").css("display","none");
	$("tr[@id='trAdd']").css("display","none");
	
	var deviceModelId = $("select[@name='deviceModelId']").val();
	var hardwareversion = $("select[@name='hardwareversion']").val();
	
	if(deviceModelId=="-1" || hardwareversion=="-1"){
		return;
	}
	var url = "<s:url value='/gtms/stb/resource/autoSoftUpRule!getSoftwareversionS.action'/>";
	
	$.post(url,{
		deviceModelId:deviceModelId,
		hardwareversion:hardwareversion
		},function(ajax){
			$("select[@name='softwareversion']").empty();
			$("select[@name='softwareversion']").append("<option value='-1'>请选择软件版本</option>");
			
			if(ajax!="")
			{
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length))
				{
					var edit_softwareversion=$("input[@name='edit_softwareversion']").val();
					
					for(var i=0;i<lineData.length;i++){
						var optionValue;
						if(edit_softwareversion==lineData[i] && ruleId!="-1"){
							optionValue = "<option value='"+lineData[i]+"' selected>"+lineData[i]+"</option>";
						}else{
							optionValue = "<option value='"+lineData[i]+"'>"+lineData[i]+"</option>  ";
						}
						$("select[@name='softwareversion']").append(optionValue);
					}
					
					if(ruleId!="-1"){
						selectVersion();
					}
				}
			}
		});
}

//获取目标版本数据
function selectVersion()
{
	clearTd();
	$("tr[@id='trVersionDesc']").css("display","none");
	$("tr[@id='trAdd']").css("display","none");
	
	var deviceModelId=$("select[@name='deviceModelId']").val();
	var vendorId = $("select[@name='vendorId']").val();
	var hardwareversion = $("select[@name='hardwareversion']").val();
	var softwareversion = $("select[@name='softwareversion']").val();
	
	if(deviceModelId=="-1" || hardwareversion=="-1" || softwareversion=="-1"){
		return;
	}
	
	var url = "<s:url value='/gtms/stb/resource/autoSoftUpRule!getDevType.action'/>";
	$.post(url,{
		vendorId:vendorId,
		deviceModelId:deviceModelId,
		hardwareversion:hardwareversion,
		softwareversion:softwareversion
	},function(ajax){
		if(ajax!=""){
			var lineData = ajax.split(",");
			$("input[@name='devicetype_id']").val(lineData[0]);
			$("td[@id='epg_version']").html(lineData[1]);
			$("input[@name='epg_version']").val(lineData[1]);
			$("td[@id='dev_net_type']").html(lineData[2]);
			$("input[@name='dev_net_type']").val(lineData[2]);
		}
	});
		
	url = "<s:url value='/gtms/stb/resource/autoSoftUpRule!getTargetSoftVersion.action'/>";
	$.post(url,{
		deviceModelId:deviceModelId
	},function(ajax){
		$("select[@name='version_id']").empty();
		$("select[@name='version_id']").append("<option value='-1' >请选择目标版本</option>");
		if(ajax!=""){
			var lineData = ajax.split("#");
			if(typeof(lineData) && typeof(lineData.length)){
				var edit_version_id=$("input[@name='edit_version_id']").val();
				for(var i=0;i<lineData.length;i++){
					var versions=lineData[i].split(",");
					var optionValue;
					if(edit_version_id==versions[0] && ruleId!="-1"){
						optionValue = "<option value='"+versions[0]+"' selected>"+versions[1]+"</option>";
					}else{
						optionValue = "<option value='"+versions[0]+"' >"+versions[1]+"</option>";
					}
					
					$("select[@name='version_id']").append(optionValue);
				}
				
				if(ruleId!="-1"){
					selectVersionData();
					ruleId="-1";
				}
			}
			
			$("tr[@id='trAdd']").css("display","");
			$("tr[@id='trVersionDesc']").css("display","");
		}else{
			alert("未查询到相应的目标版本！");
		}
	});
}

function selectVersionData()
{
	clearTd();
	
	var version_id=$("select[@name='version_id']").val();
	if(version_id=="-1"){
		return ;
	}
	
	var url = "<s:url value='/gtms/stb/resource/autoSoftUpRule!getTargetSoftVersionDetail.action'/>";
	
	$.post(url,{
		version_id:version_id
	},function(ajax){
		if(ajax!=""){
			//version_desc,version_path,file_size,md5,net_type,epg_version
			var versionData=ajax.split(",");
			$("td[@id='version_desc']").html(versionData[0]);
			$("td[@id='version_path']").html(versionData[1]);
			$("td[@id='version_file_size']").html(versionData[2]);
			$("td[@id='version_md5']").html(versionData[3]);
			$("td[@id='version_net_type']").html(versionData[4]);
			$("td[@id='version_epg']").html(versionData[5]);
		}
	});
	
}

function clearTd()
{
	$("td[@id='version_desc']").html("");
	$("td[@id='version_path']").html("");
	$("td[@id='version_file_size']").html("");
	$("td[@id='version_md5']").html("");
	$("td[@id='version_net_type']").html("");
	$("td[@id='version_epg']").html("");
}

function checkNetType(dev_net_type,user_net_type,version_net_type)
{
	if(user_net_type!==version_net_type){
		return false;
	}
	
	if("private_net"==dev_net_type && "public_net"==user_net_type){
		return false;
	}
	return true;
}
</script>
</head>

<body>
<input type="hidden" name="rule_id" value="<s:property value='rule_id'/>">
<input type="hidden" name="edit_vendorId" value="<s:property value='vendorId'/>">
<input type="hidden" name="edit_deviceModelId" value="<s:property value='deviceModelId'/>">
<input type="hidden" name="edit_hardwareversion" value="<s:property value='hardwareversion'/>">
<input type="hidden" name="edit_softwareversion" value="<s:property value='softwareversion'/>">
<input type="hidden" name="edit_user_net_type" value="<s:property value='user_net_type'/>">
<input type="hidden" name="edit_version_id" value="<s:property value='version_id'/>">
<input type="hidden" name="edit_devicetype_id" value="<s:property value='devicetype_id'/>">
<input type="hidden" name="edit_dev_net_type" value="<s:property value='dev_net_type'/>">
<input type="hidden" name="edit_version_net_type" value="<s:property value='version_net_type'/>">

<table>
	<tr><td HEIGHT=20>&nbsp;</td></tr>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">机顶盒ROM版本自动升级策略新增</td>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table class="querytable">
				<tr>
					<th colspan="4" align='left'>选择待匹配设备版本</th>
				</tr>
				<TR>
					<TD class=column width="15%" align='right'>厂商</TD>
					<TD width="35%">
						<select name="vendorId" onchange="vendorChange()">
							<option value="-1">请选择厂商</option>
						</select>
					</TD>
					<TD class=column width="15%" align='right'>型号</TD>
					<TD width="35%">
						<select name="deviceModelId" onchange="deviceModelChange()">
							<option value="-1">请选择型号</option>
						</select>
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>硬件版本</TD>
					<TD width="35%">
						<select name="hardwareversion" onchange="hardwareversionChange()">
							<option value="-1">请选择硬件版本</option>
						</select>
					</TD>
					<TD class=column width="15%" align='right'>软件版本</TD>
					<TD width="35%">
						<select name="softwareversion" onchange="selectVersion()">
							<option value="-1">请选择软件版本</option>
						</select>
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						EPG版本<input type="hidden" name="epg_version">
						<input type="hidden" name="devicetype_id" class="bk" value="-1">
					</TD>
					<TD id="epg_version" width="35%"></TD>
					<TD class=column width="15%" align='right'>
						适用网络类型<input type="hidden" name="dev_net_type">
					</TD>
					<TD id="dev_net_type" width="35%"></TD>
				</TR>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table class="querytable">
				<tr>
					<th colspan="4" align='left'>选择网络情况</th>
				</tr>
				<tr>
					<TD class=column width="15%" align='right'>用户实际网络类型</TD>
					<TD colspan="3" width="85%">
						<select name="user_net_type" >
							<option value="private_net">专网</option>
							<s:if test="user_net_type=='public_net'">
								<option value="public_net" selected>公网</option>
							</s:if>
							<s:else>
								<option value="public_net">公网</option>
							</s:else>
						</select>
					</TD>
				</tr>
			</table>
		</td>
	</tr>
	<TR id="trVersionDesc" style="display: none">
		<td>
			<table class="querytable">
				<tr>
					<th colspan="4">选择目标版本</th>
				</tr>
				<tr>
					<td nowrap class=column width="15%">目标版本</td>
					<td colspan="3" width="35%">
						<select name="version_id" onchange="selectVersionData()">
							<option value="-1">请选择目标版本</option>
						</select>
					</td>	
				</tr>	
				<tr>
					<td nowrap class=column width="15%">目标版本描述</td>
					<td id="version_desc" colspan="3" width="35%"></td>	
				</tr>	
				<tr>
					<td nowrap class=column width="15%">目标版本路径</td>
					<td id="version_path" colspan="3" width="35%"></td>	
				</tr>
				<tr bgcolor="#FFFFFF">
					<td nowrap class=column width="15%">目标版本大小</td>
					<td id="version_file_size" width="35%"></td>
					<td nowrap class=column width="15%">目标MD5</td>
					<td id="version_md5" width="35%"></td>	
				</tr>
				<tr bgcolor="#FFFFFF">
					<td nowrap class=column width="15%">EPG版本</td>
					<td id="version_epg" width="35%"></td>	
					<td nowrap class=column width="15%">适用网络类型</td>
					<td id="version_net_type" width="35%"></td>
				</tr>
				<tr id="trAdd" bgcolor="#FFFFFF" style="display: none">
					<td colspan="4" align="right" class=foot>
						<s:if test="rule_id=='-1'">
							<button onclick="add()">&nbsp;新增&nbsp;</button>
						</s:if>
						<s:else>
							<button onclick="edit()">&nbsp;编辑&nbsp;</button>
						</s:else>
					</td> 
				</tr>
			</table>
		</td>
	</tr>
</table>
</body>
</html>
