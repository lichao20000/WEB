<%--
vlan配置
Author: 王森博
Version: 1.0.0
Date: 2009-10-29
--%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.blockUI.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="../../Js/jsDate/WdatePicker.js"></script>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">
<!--//
var deviceId = '<s:property value="deviceId"/>';
parent.unblock();

//配置VLAN
function configVlan(){
if(checkForm() == false)return false;
    var ipEnable = $("input[@name='ipEnable'][@checked]").val();
    var ipEnable_h = $("input[@name='ipEnable_h']").val();
	if(ipEnable=="0"&&ipEnable_h=="0"){
        alert("设备此VALN已经是关闭的，不用重新设置关闭");
        return false;
    }
    document.all("div_config").style.display = "";
    parent.dyniframesize();
    $("div[@id='div_config']").css("background-color","#33CC00");
	document.all("div_config").innerHTML = "<font size=2>正在配置设备SNMP访问配置信息，请耐心等待....</font>";
	parent.block();
	var type = $("input[@name='type']").val();
	var ipEnable = $("input[@name='ipEnable'][@checked]").val();
	var vlanId = $("input[@name='vlanId']").val();
	var vlanName = $("input[@name='vlanName']").val();
	var deviceId = $("input[@name='deviceId']").val();
	var vlanI = $("input[@name='vlanI']").val();
	var port="";
	$("input[@name='LAN'][@checked]").each(function(){
    port = port + $(this).val()+",";
    });
    $("input[@name='WLAN'][@checked]").each(function(){
    port = port + $(this).val()+",";
    });
    var portList = port.substring(0, port.length-1);
	var ipAddressType = $("select[@name='ipAddressType']").val();
	var ipAddress = $("input[@name='ipAddress']").val();
	var ipMask = $("input[@name='ipMask']").val();
	var url = '<s:url value='/bbms/config/vlanDhcpConfig!configVlan.action'/>';
	$.post(url,{
		type:type,
		deviceId:deviceId,
		vlanI:vlanI,
		vlanId:vlanId,
		vlanName:vlanName,
		portList:portList,
		ipEnable:ipEnable,
		ipAddressType:ipAddressType,
		ipAddress:ipAddress,
		ipMask:ipMask
	},function(ajax){
	    var s = ajax.split(";");
	    if(s[0]=="-1"){
	   	    $("div[@id='div_config']").css("background-color","red");
			document.all("div_config").innerHTML = "<font size=2>"+s[1]+"</font>";
			parent.dyniframesize();
		}
		if(s[0]=="1"){
            $("div[@id='div_config']").css("background-color","#33CC00");
			document.all("div_config").innerHTML = "<font size=2>"+s[1]+"</font>";

			var url = "<s:url value='/servStrategy/ServStrategy!getStrategy.action'/>";
			var strategyId = s[2];
			$.post(url,{
           		strategyId:strategyId
            },function(ajax){
          	   	$("div[@id='div_strategy']").html("");
				$("div[@id='div_strategy']").append(ajax);
				parent.dyniframesize();
            });
			parent.dyniframesize();
		}
		parent.dyniframesize();
		parent.unblock();

	});
	document.getElementById("tr002").style.display = "";
	parent.dyniframesize();
	setTimeout("clearResult()", 5000);
}

function clearResult() {
	document.all("div_config").style.display = "none";
}


//虚接口开关
function changeStatus(){
 var ipEnable = $("input[@name='ipEnable'][@checked]").val();
	if('1' == ipEnable){
		$("tr[@name='name_ip_mask']").show();
		$("td[@name='ipAddressType']").attr("disabled", false);
		$("select[@name='ipAddressType']").attr("disabled", false);
		chgAddrType();
		parent.dyniframesize();

	}else{
		$("tr[@name='name_ip_mask']").hide();
		$("td[@name='ipAddressType']").attr("disabled", true);
		$("select[@name='ipAddressType']").attr("disabled", true);
		parent.dyniframesize();
	}
}
//地址类型
function chgAddrType(){
    var ipAddressType = $("select[@name='ipAddressType']").val();
	if('Static' == ipAddressType){
		$("tr[@name='name_ip_mask']").show();
		parent.dyniframesize();
	}else{
		$("tr[@name='name_ip_mask']").hide();
	}
}

function showVlan(deviceId,vlanI,vlanId,vlanName,portList,ipEnable,ipAddressType,ipAddress,ipMask){
    $("tr[@name='vlan']").show();
    $("input[@name='vlanId']").val(vlanId);
    $("input[@name='vlanName']").val(vlanName);

    $("input[@name='LAN']").attr("checked",false);
    $("input[@name='WLAN']").attr("checked",false);

    var prot = portList.split(",");
    for(i = 0;i<prot.length;i++){
        $("input[@name='LAN'][@value='"+prot[i]+"']").attr("checked",true);
        $("input[@name='WLAN'][@value='"+prot[i]+"']").attr("checked",true);
    }

    if(ipAddressType!="null"&&ipAddressType!="N/A"){
    $("select[@name='ipAddressType']").attr("value",ipAddressType);
	}
	chgAddrType();

	$("input[@name='ipAddress']").val(ipAddress);
	$("input[@name='ipMask']").val(ipMask);

	$("input[@name='deviceId']").val(deviceId);
	$("input[@name='vlanI']").val(vlanI);
	$("input[@name='type']").val("1");
	$("input[@name='ipEnable'][@value='"+ipEnable+"']").attr("checked",true);
	$("input[@name='ipEnable_h']").val(ipEnable);
	changeStatus();
	parent.dyniframesize();

}

function addVlan(deviceId,vlanI){

    $("tr[@name='vlan']").show();
    $("input[@name='vlanId']").val("");
    $("input[@name='vlanName']").val("");

    $("input[@name='LAN']").attr("checked",false);
    $("input[@name='WLAN']").attr("checked",false);

    $("select[@name='ipAddressType']").attr("value","Static");

	chgAddrType();

	$("input[@name='ipAddress']").val("");
	$("input[@name='ipMask']").val("");

	$("input[@name='deviceId']").val(deviceId);
	$("input[@name='vlanI']").val(vlanI);
	$("input[@name='type']").val("0");
	$("input[@name='ipEnable'][@value=1]").attr("checked",true);
	$("input[@name='ipEnable_h']").val("");
	changeStatus();
	parent.dyniframesize();

}

function checkForm(){

          var vlanId = document.frm.vlanId.value;
          var vlanName = document.frm.vlanName.value;
          if(vlanId == ""){
              alert("请输入VLANID");
              document.frm.vlanId.focus();
              return false;
          }
          if(vlanName == ""){
              alert("请输入VLAN名称");
              document.frm.vlanName.focus();
              return false;
          }
          //if($("input[@name='LAN'][@checked]").val()==undefined&& $("input[@name='WLAN'][@checked]").val()==undefined){
          //    alert("请选择绑定端口");
          //    return false;
          //}
          var ipEnable = $("input[@name='ipEnable'][@checked]").val()
          if(ipEnable == "1"){
              var ipAddressType = document.frm.ipAddressType.value;
              if(ipAddressType==""){
                  alert("请选择虚接口地址类型");
                  return false;
              }
              if(ipAddressType=="Static"){
                  var ipAddress = document.frm.ipAddress.value;
                  var ipMask = document.frm.ipMask.value;
                  if(ipAddress == ""){
                        alert("请输入虚接口IP地址");
                        document.frm.ipAddress.focus();
                        return false;
                  }
                  if(ipMask == ""){
                        alert("请输入虚接口地址掩码");
                        document.frm.ipMask.focus();
                        return false;
                  }
                  if(!IsIPAddr2(ipAddress,"虚接口IP地址")){
                        document.frm.ipAddress.focus();
                        return false;
                  }
                  if(!IsIPAddr2(ipMask,"虚接口地址掩码")){
                        document.frm.ipMask.focus();
                        return false;
                  }
              }
          }
                return true;
}

//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["myiframe"]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes"

function dyniframesize()
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block"
     			//如果用户的浏览器是NetScape
     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
      			//如果用户的浏览器是IE
     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight)
      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
   			 }
   		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
    		tempobj.style.display="block"
		}
	}
}

//-->
</SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD align=right>
			<div id="div_config"
				style="width: 23%; display: none; background-color: #33CC00">
			</div>
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" onsubmit="return CheckForm()">
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="text">
					<TR bgcolor="#FFFFFF" STYLE="display: ">
						<TH colspan="4">
							设备上VLAN信息
						</TH>
					</TR>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr align="left" bgcolor="#FFFFFF"">
									<td align="right" class=column colspan="4">
										<button
											onclick="addVlan('<s:property value="deviceId"/>','<s:property value="vlanI"/>')">
											增加
										</button>
									</td>
								</tr>
								<tr align="left" bgcolor="#FFFFFF" STYLE="display: ">
									<td align="right" class=column width="20%">
										设备支持VLAN总数
									</td>
									<td width="30%">
										<s:property value="vlanMaxNum" />
									</td>
									<td class=column align="right" width="20%">
										设备当前VLAN数
									</td>
									<td width="30%">
										<s:property value="vlanCurNum" />
									</td>
								</tr>
							</TABLE>
						</TD>
					</TR>
					<tr align="left" id="trnet" STYLE="display: ">
						<td colspan="4" bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr align="center" bgcolor="#FFFFFF">
									<TD class=column5 align="center">
										VLANID
									</TD>
									<TD class=column5 align="center">
										VLAN名称
									</TD>
									<TD class=column5 align="center">
										绑定端口
									</TD>
									<TD class=column5 align="center">
										虚接口状态
									</TD>
									<TD class=column5 align="center">
										虚接口地址类型
									</TD>
									<TD class=column5 align="center">
										虚接口IP地址
									</TD>
									<TD class=column5 align="center">
										虚接口地址掩码
									</TD>
									<TD class=column5 align="center">
										操作
									</TD>
								</tr>
								<s:if test="lanVlanList!=null">
									<s:iterator var="lanVlanList" value="lanVlanList">
										<tr align="center" bgcolor="#FFFFFF">
											<TD align="center">
												<s:property value="vlanId" />
											</TD>
											<s:if test="vlanName=='N/A' || vlanName=='null' ">
												<TD align="center">
													-
												</TD>
											</s:if>
											<s:else>
												<TD align="center">
													<s:property value="vlanName" />
												</TD>
											</s:else>
											<s:if test="port==''">
												<TD align="center">
													-
												</TD>
											</s:if>
											<s:else>
												<TD align="center">
													<s:property value="port" />
												</TD>
											</s:else>
											<s:if test="ipEnable==1">
												<TD align="center">
													启用
												</TD>
											</s:if>
											<s:elseif test="ipEnable==0">
												<TD align="center">
													未启用
												</TD>
											</s:elseif>
											<s:else>
												<TD align="center">
													-
												</TD>
											</s:else>
											<s:if test="ipAddressType=='N/A' || ipAddressType=='null' ">
												<TD align="center">
													-
												</TD>
											</s:if>
											<s:else>
												<TD align="center">
													<s:property value="ipAddressType" />
												</TD>
											</s:else>
											<s:if test="ipAddress=='N/A' || ipAddress=='null' ">
												<TD align="center">
													-
												</TD>
											</s:if>
											<s:else>
												<TD align="center">
													<s:property value="ipAddress" />
												</TD>
											</s:else>
											<s:if test="ipMask=='N/A' || bindPort=='ipMask' ">
												<TD align="center">
													-
												</TD>
											</s:if>
											<s:else>
												<TD align="center">
													<s:property value="ipMask" />
												</TD>
											</s:else>
											<td align="center">
												<a href=javascript:
													//
														onclick="showVlan('<s:property value="deviceId"/>','<s:property value="vlanI"/>',
                                                                            '<s:property value="vlanId"/>','<s:property value="vlanName"/>',
                                                                            '<s:property value="portList"/>','<s:property value="ipEnable"/>',
                                                                            '<s:property value="ipAddressType"/>','<s:property value="ipAddress"/>',
                                                                            '<s:property value="ipMask"/>')">
													<IMG SRC='../../images/edit.gif' BORDER='0' ALT='编辑'
														style='cursor: hand'> </a>
											</td>
										</tr>
									</s:iterator>
								</s:if>
								<s:else>
									<tr align="center" bgcolor="#FFFFFF">
										<TD colspan="13" align="center">
											<FONT color="red"><s:property value="massage" /> </FONT>
										</TD>
									</tr>
								</s:else>
								<tr align="center" bgcolor="#FFFFFF">
									<TD colspan="13" align="center"></TD>
								</tr>
							</TABLE>
					<tr name="vlan" style="display: none">
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR height=30px>
									<TH colspan="4" align="center">
										VLAN 配置
									</TH>
								</TR>
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF" name="snmpv3" style="display: ">
												<TD align="right" class="column" width="20%">
													VLANID
												</TD>
												<TD width="30%">
													<input type="text" name="vlanId" class="bk" value="">
												</TD>
												<TD align="right" class=column width="20%">
													VLAN名称
												</TD>
												<TD width="30%">
													<input type="text" name="vlanName" class="bk" value="">
													<input type="hidden" name="deviceId" id="deviceId" value="">
													<input type="hidden" name="vlanI" id="vlanI" value="">
													<input type="hidden" name="type" id="type" value="">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" class=column>
												<TD class=column align="right">
													绑定端口
												</TD>
												<TD width="35%" colspan="3" id="lanInter" height="25">
													<s:if test="lanList!=null">
														<s:iterator value="lanList" var="map">
															<s:checkboxlist list="map" listKey="key"
																listValue="value" name="LAN"></s:checkboxlist>
														</s:iterator>
													</s:if>
													<s:if test="wlanList!=null">
														<s:iterator value="wlanList" var="map">
															<s:checkboxlist list="map" listKey="key"
																listValue="value" name="WLAN"></s:checkboxlist>
														</s:iterator>
													</s:if>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD class=column align="right">
													虚接口状态
												</TD>
												<TD name="ip_enable">
													<input type="radio" name="ipEnable" checked value="1"
														onclick="changeStatus();">
													开启
													<input type="radio" name="ipEnable" value="0"
														onclick="changeStatus();">
													关闭
													<input type="hidden" name="ipEnable_h" id="ipEnable_h"
														value="">
												</TD>
												<TD name="ipAddressType" align="right" class=column
													style="display: ">
													虚接口地址类型
												</TD>
												<TD name="ipAddressType">
													<select name="ipAddressType" class="bk"
														onchange="chgAddrType()">
														<option value="Static">
															==Static==
														</option>
														<option value="DHCP">
															==DHCP==
														</option>
													</select>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="id_ip_mask" name="name_ip_mask"
												style="display: " class=column>
												<TD class=column align="right">
													虚接口IP地址
												</TD>
												<TD>
													<input type="text" name="ipAddress" class="bk" value="">
												</TD>
												<TD class=column align="right">
													虚接口地址掩码
												</TD>
												<TD>
													<input type="text" name="ipMask" class="bk" value="">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<button onclick="configVlan();">
														设 置
													</button>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr002" style="display: none">
												<td colspan="4" valign="top" class=column>
													<div id="div_strategy"
														style="width: 100%; z-index: 1; top: 100px">
													</div>
												</td>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
	<!-- <TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR> -->
</TABLE>
