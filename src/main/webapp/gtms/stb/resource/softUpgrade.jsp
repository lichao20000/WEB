<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
var instArea = "<%=LipossGlobals.getLipossProperty("InstArea.ShortName")%>";

$(function(){
	gwShare_setGaoji();
});

var deviceId = "";
var devicetypeId = "";
function ExecMod()
{
    if(deviceId==""){
		alert("请查询设备！");
		return false;
    }
	if($("select[@name='goal_softwareversion']").val()==undefined){
		alert("请重新选择设备！");
		return false;
	}
	if($("select[@name='goal_softwareversion']").val() == -1){
		alert("请选择版本文件！");
		return false;
	}
	
	var confirm_desc="机顶盒当前的IP类型与目标版本适用网络类型不一致，强制升级有可能导致网络无法连接，业务无法使用，请确认是否继续升级？";
	if($("input[@name='targetVersionNetType']").val() != $("input[@name='ip_type']").val() 
			&& !confirm(confirm_desc)){
		return false;
	}
	
	$('#softUpgrade_btn').attr("disabled",true);
	var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!softUpgrade.action'/>";
	goal_softwareversion = $("select[@name='goal_softwareversion']").val();
	$.post(url,{
		deviceId:deviceId,
		pathId:$("select[@name='goal_softwareversion']").val(),
		goal_softwareversion:goal_softwareversion,
		gw_type:"4",
		softStrategy_type:$("select[@name='softStrategy_type']").val()
	},function(ajax){
		var s = ajax.split(";");
		if(s[0]=="1"){
			alert("调用后台成功！");
	    }else{
			alert(s[1]);
		}
		var url = "<s:url value='/gtms/stb/resource/softUpgrade.jsp'/>";
        window.location.href=url;
		$('#softUpgrade_btn').attr("disabled",false);
	});
}

function deviceResult(returnVal)
{
	for(var i=0;i<returnVal[2].length;i++)
	{
		deviceId = returnVal[2][i][0];
		devicetypeId = returnVal[2][i][6];
		$("tr[@id='trDeviceResult']").css("display","");
		$("td[@id='tdDeviceSn']").html(returnVal[2][i][1]+"-"+returnVal[2][i][2]);
		$("td[@id='tdDeviceCityName']").html(returnVal[2][i][5]);
		
		if("hn_lt"==instArea){
			$("tr[@id='trDeviceVMResult']").css("display","");
			$("tr[@id='trDeviceAccountResult']").css("display","");
			$("tr[@id='trDeviceVersionResult']").css("display","");
			$("tr[@id='trDeviceApkEpgResult']").css("display","");
			$("tr[@id='trDeviceLonigTimeResult']").css("display","");
			$("tr[@id='trDeviceNetResult']").css("display","");
			$("tr[@id='trDeviceIpResult']").css("display","");
			$("tr[@id='trDeviceNetTypeResult']").css("display","");
			$("tr[@id='trDeviceTypeResult']").css("display","");
		}
	}
	
	$("tr[@id='softwareversion']").show();
	var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!showSoftwareversion.action'/>";
	if(deviceId!=""){
		$("div[@id='div_goal_softwareversion']").html("");
		$.post(url,{
			deviceId:deviceId
		},function(ajax){
			if(ajax!=""){
				if("hn_lt"==instArea)
				{
					var lineDatas = ajax.split("&")[0];
					var lineData = lineDatas.split("#");
					if(lineData!="" && typeof(lineData) && typeof(lineData.length)){
						$("div[@id='div_goal_softwareversion']").append("<select name='goal_softwareversion' onchange='selectVersion()' class='bk' style='width: 400px'>");
						$("select[@name='goal_softwareversion']").append("<option value='-1' selected>==请选择==</option>");
						for(var i=0;i<lineData.length;i++){
							
							var oneElement = lineData[i].split("$");
							var xValue = oneElement[0].split(",")[0];
							var xText = oneElement[1];
							option = "<option value='"+xValue+"'>=="+xText+"==</option>";
							$("select[@name='goal_softwareversion']").append(option);
							
							$("input[@name='targetVersionNetType']").val(oneElement[0].split(",")[1]);
						}
					}else{
						$("div[@id='div_goal_softwareversion']").append("该设备没有可升级的版本文件！");
					}
					
					var deviceDatas=ajax.split("&")[1];
					if(typeof(deviceDatas) && typeof(deviceDatas.length))
					{
						var deviceData=deviceDatas.split(",");
						
						$("td[@id='vendor_name']").html(deviceData[0]);
						$("td[@id='model_name']").html(deviceData[1]);
						$("td[@id='mac']").html(deviceData[2]);
						$("td[@id='serv_account']").html(deviceData[3]);
						$("td[@id='hard_version']").html(deviceData[4]);
						$("td[@id='soft_version']").html(deviceData[5]);
						
						$("td[@id='apk_version_name']").html(deviceData[6]);
						$("td[@id='epg_version']").html(deviceData[7]);
						$("td[@id='complete_time']").html(deviceData[8]);
						$("td[@id='cpe_currentupdatetime']").html(deviceData[9]);
						$("td[@id='network_type']").html(deviceData[10]);
						$("td[@id='addressing_type']").html(deviceData[11]);
						$("td[@id='loopback_ip']").html(deviceData[12]);
						$("td[@id='public_ip']").html(deviceData[13]);
						
						$("td[@id='net_type']").html(deviceData[14]);
						$("td[@id='ip_type']").html(deviceData[15]);
						$("td[@id='dev_type']").html(deviceData[16]);
						
						if(deviceData[15]=='公 网'){
							$("input[@name='ip_type']").val('public_net');
						}else if(deviceData[15]=='专 网'){
							$("input[@name='ip_type']").val('private_net');
						}else{
							$("input[@name='ip_type']").val('unknown_net');
						}
					}
				}else{
					var lineData = ajax.split("#");
					if(lineData!="" && typeof(lineData) && typeof(lineData.length)){
						$("div[@id='div_goal_softwareversion']").append("<select name='goal_softwareversion' class='bk' style='width: 400px'>");
						$("select[@name='goal_softwareversion']").append("<option value='-1' selected>==请选择==</option>");
						for(var i=0;i<lineData.length;i++)
						{
							var oneElement = lineData[i].split("$");
							var xValue = oneElement[0];
							var xText = oneElement[1];
							option = "<option value='"+xValue+"'>=="+xText+"==</option>";
							$("select[@name='goal_softwareversion']").append(option);
						}
					}else{
						$("div[@id='div_goal_softwareversion']").append("该设备没有可升级的版本文件！");
					}
				}
			}else{
				$("div[@id='div_goal_softwareversion']").append("该设备没有可升级的版本文件！");
			}
		});
	}else{
		$("div[@id='div_goal_softwareversion']").html("请选择设备");
	}
}

function selectVersion()
{
	var softVersionId=$("select[@name='goal_softwareversion']").val();
	
	if(softVersionId!="-1")
	{
		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!querySoftVersionDetail.action'/>";
		
		$.post(url,{
			taskId:softVersionId
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
				
				$("tr[@id='trVersionDesc']").css("display","");
			}
		});
	}else{
		$("tr[@id='trVersionDesc']").css("display","none");
	}
}

</SCRIPT>

<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			您当前的位置：单台设备软件升级
		</TD>
	</TR>
</TABLE>
<br>
<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<%@ include file="../share/gwShareDeviceQuery.jsp"%>
		</td>
	</tr>

	<TR id="softwareversion" style="display: none">
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="">
				<table width="100%" class="querytable">
					<TR>
						<TH colspan="4" class="title_1">版本升级</TH>
					</TR>
					<TR id="trDeviceResult" style="display: none">
						<td nowrap class="title_2" width="15%">设备序列号</td>
						<td id="tdDeviceSn" width="35%"></td>
						<td nowrap class="title_2" width="15%">属地</td>
						<td id="tdDeviceCityName" width="35%"></td>
					</TR>
					
					<ms:inArea areaCode="hn_lt" notInMode="false">
						<TR id="trDeviceVMResult" style="display: none">
							<td nowrap class="title_2" width="15%">设备厂商</td>
							<td id="vendor_name" width="35%"></td>
							<td nowrap class="title_2" width="15%">型号</td>
							<td id="model_name" width="35%"></td>
						</TR>
						<TR id="trDeviceAccountResult" style="display: none">
							<td nowrap class="title_2" width="15%">MAC地址</td>
							<td id="mac" width="35%"></td>
							<td nowrap class="title_2" width="15%">业务账号</td>
							<td id="serv_account" width="35%"></td>
						</TR>
						<TR id="trDeviceVersionResult" style="display: none">
							<td nowrap class="title_2" width="15%">硬件版本</td>
							<td id="hard_version" width="35%"></td>
							<td nowrap class="title_2" width="15%">软件版本</td>
							<td id="soft_version" width="35%"></td>
						</TR>
						<TR id="trDeviceApkEpgResult" style="display: none">
							<td nowrap class="title_2" width="15%">APK版本名称</td>
							<td id="apk_version_name" width="35%"></td>
							<td nowrap class="title_2" width="15%">EPG版本</td>
							<td id="epg_version" width="35%"></td>
						</TR>
						<TR id="trDeviceNetTypeResult" style="display: none">
							<td nowrap class="title_2" width="15%">当前版本适用网络类型</td>
							<td id="net_type" width="35%"></td>
							<td nowrap class="title_2" width="15%">设备类型</td>
							<td id="dev_type" width="35%" colspan="3"></td>
						</TR>
						<TR id="trDeviceLonigTimeResult" style="display: none">
							<td nowrap class="title_2" width="15%">首次登录时间</td>
							<td id="complete_time" width="35%"></td>
							<td nowrap class="title_2" width="15%">最近登录时间</td>
							<td id="cpe_currentupdatetime" width="35%"></td>
						</TR>
						<TR id="trDeviceNetResult" style="display: none">
							<td nowrap class="title_2" width="15%">接入类型</td>
							<td id="network_type" width="35%"></td>
							<td nowrap class="title_2" width="15%">网络类型</td>
							<td id="addressing_type" width="35%"></td>
						</TR>
						<TR id="trDeviceIpResult" style="display: none">
							<td nowrap class="title_2" width="15%">设备IP</td>
							<td id="loopback_ip" width="35%"></td>
							<td nowrap class="title_2" width="15%">公网IP</td>
							<td id="public_ip" width="35%"></td>
						</TR>
						<TR id="trDeviceTypeResult" style="display: none">
							<td nowrap class="title_2" width="15%">IP类型
								<input type="hidden" name="ip_type" value="">
							</td>
							<td id="ip_type" width="35%"></td>
						</TR>
						<TR>
							<td nowrap class="title_2" width="15%">
								<font color="red">*</font>&nbsp;目标版本
							</td>
							<td colspan="3">
								<input type="hidden" name="targetVersionNetType" value="">
								<div id="div_goal_softwareversion"></div>
							</td>
						</TR>
						<br>
						<TR id="trVersionDesc" style="display: none">
							<td colspan="4">
								<table width="100%" class="querytable">
									<tr>
										<th colspan="4" class="title_1">目标版本信息</th>
									</tr>
									<tr>
										<td nowrap class="title_2" width="15%">目标版本描述</td>
										<td id="version_desc" colspan="3" width="35%"></td>	
									</tr>	
									<tr>
										<td nowrap class="title_2" width="15%">目标版本路径</td>
										<td id="version_path" colspan="3" width="35%"></td>	
									</tr>
									<tr bgcolor="#FFFFFF">	
										<td nowrap class="title_2" width="15%">目标版本大小</td>
										<td id="version_file_size" width="35%"></td>
										<td nowrap class="title_2" width="15%">目标MD5</td>
										<td id="version_md5" width="35%"></td>	
									</tr>
									<tr bgcolor="#FFFFFF">	
										<td nowrap class="title_2" width="15%">EPG版本</td>
										<td id="version_epg" width="35%"></td>	
										<td nowrap class="title_2" width="15%">适用网络类型</td>
										<td id="version_net_type" width="35%"></td>
									</tr>
								</table>
							</td>
						</TR>
					</ms:inArea>
					
					<ms:inArea areaCode="hn_lt" notInMode="true">
					<TR>
						<TD align="right" width="15%">软件升级策略方式：</TD>
                     	<TD width="30%">
                     		<SELECT name="softStrategy_type">
                      			<option value="0">立即执行</option>
                      		<!--  <option value="5">终端启动</option>
                      			  <option value="4"> 下次连接到系统</option> -->
                    		 </SELECT>
                     	</TD>
						<td nowrap class="title_2" width="15%">
							<font color="red">*</font>&nbsp;目标版本
						</TD>
						<TD width="">
							<div id="div_goal_softwareversion"></div>
						</TD>
					</TR>
					</ms:inArea>
					
					<TR bgcolor="#FFFFFF">
						<TD colspan="4" CLASS="foot">
							<div class="right">
								<button id="softUpgrade_btn" onclick="ExecMod()">升 级</button>
							</div>
						</TD>
					</TR>
				</TABLE>
			</FORM>
		</TD>
	</TR>
</TABLE>
