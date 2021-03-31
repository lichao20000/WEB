<%@ page contentType="text/html; charset=GBK"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>联通测速</title>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/CheckFormForm.js"></SCRIPT>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

var area = "<%=LipossGlobals.getLipossProperty("InstArea.ShortName")%>";
var wanType=new Array();
var pppoeName=new Array();
function CheckForm(){
 	var __device_id = $("input[@name='deviceId']").val();

	if(__device_id == null || __device_id == ""){
		alert("请先查询设备!");
		return false;
	}
	return true;
}

function testCheck(devSn,oui,city_id,city_name){
	$("input[@name='gwShare_queryButton']").attr("disabled", false);
	init();
	
	if("jl_lt" == area){
		//$("input[@name='button']").attr("disabled", false); 
		$.post("<s:url value='/ids/IdsShareDevice!queryPPPOE.action'/>", {
			idsShare_queryParam : devSn},
			function(ajax){
				var arr = ajax.split("&*&");
				if (arr[0] == "") {
					alert("没有宽带账号，不用测速！");
					return;
			 	}
			 	if (arr[1] == "" || arr[2] == "") {
				 	alert("没有对应的仿真账号！");
			 	}
				$("input[@name='pppoeUserName']").val(arr[0]);
				$("input[@name='userName']").val(arr[1]);
				$("input[@name='password']").val(arr[2]);
				$("input[@name='test_rate']").val(arr[3]/1024 + "MB");
				$("input[@name='column1']").val("http://218.27.253.1:8094/speedfile/services/contractrate/contract/rate/");
				$("input[@name='column2']").val("http://218.27.253.1:8094/speedfile/services/contractrate/results/insert/");
		 });
	}
	$("td[@id='tdDeviceSn']").append(devSn);
	$("input[@name='device_id']").val(devSn);
	$("input[@name='oui']").val(oui);
	$("input[@name='city_id']").val(city_id);
	$("td[@id='tdDeviceCityName']").append(city_name);
	if("jx_dx" != area && "sd_lt" != area){
	var ids_queryField = $("input[@name='idsShare_queryField']").val();
	var ids_queryParam = $("input[@name='idsShare_queryParam']").val();
	ids_queryParam = $.trim(ids_queryParam);
	var wanurl = "<s:url value='/ids/IdsShareDevice!queryIdsWANDetail.action'/>";
	$.post(wanurl, {idsShare_queryField : ids_queryField,
				 idsShare_queryParam : ids_queryParam},
				 function(ajax){
						var data = ajax.split("#");
						if("1"==data[0]){
							$("#wan").html("<font color='red'>"+data[1]+"</font>");
						}else{
							//data[1]宽带的wan口
							var wanHtml = $("#wan").html("");
							//宽带wan口的多条记录
							var one = data[1].split("^");
							
							for(var i=0; i<one.length; i++){	
								if( "undefined" != typeof(one[i]) && ""!=one[i]){
									var ifs = one[i].split("￥");
									//if(ifs[1]=="41"){
										wanHtml.append("<input type='radio' name='wanType' value='"+one[i]+"' onclick='ids_queryField_selected(1)'/> 上网通道("+ifs[1]+")&nbsp;&nbsp;");
									//}
										if("hb_lt" == area){
											wanType[i]=ifs[2];
											pppoeName[i]=ifs[3];
										}
								}
							}
							//河北联通按照单宽带处理，以后可能会有多宽带
							if("hb_lt" == area){
								if(wanType[0]=="PPPoE_Bridged"){
								   //$("#testUserInfo").show();
								   $("#testUserInfo2").show();
									 $("#connType").val("PPPoE_Bridged");
									 $("#pppoeUserName").val(pppoeName[0]);
								}else{
									//$("#testUserInfo").hide();
									$("#testUserInfo2").hide();
									$("#connType").val("IP_Routed");
								}
							}
							if(Trim(data[1]).length>0){
								var value = $("input[name='wanType']").val();
								if("undefined"==typeof(value)){
									$("#wan").html("<font color='red'>wan通道信息为空!</font>");
								}else{
									$("input[@name='button']").attr("disabled", false); 
								}
							}else{
								$("#wan").html("<font color='red'>wan通道信息为空!</font>");
							}
						}
						var radio = $("#wan input[type='radio']:first");
						if (radio != null && radio != "") {
							$("#wan input[type='radio']:first").attr('checked', 'checked');
						}
	});
	}
	else{
		$("input[@name='button']").attr("disabled", false); 
	}
}

function ids_queryField_selected(value){
	$("input[@name='wan_interface']").val(value);
}


function queryHTTPService(){
	$("td[@id='messageInfo']").css("display","");
	$("td[@id='tdData']").css("display","none");
	$("input[@name='button']").attr("disabled", true);
	document.frm.submit();
}

function init(){
	$("td[@id='messageInfo']").css("display","none");
	$("td[@id='trDeviceResult']").css("display","");
	$("td[@id='tdDeviceSn']").html("");
	$("td[@id='tdDeviceCityName']").html("");
	$("#wan").html("<font color='red'>正在获取wan通道信息...</font>");
}

function closeMessageInfo(){
	$("td[@id='messageInfo']").css("display","none");
}

//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids = [ "dataForm" ]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide = "yes"

function dyniframesize() {
	var dyniframe = new Array()
	for (i = 0; i < iframeids.length; i++) {
		if (document.getElementById) {
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document
					.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera) {
				dyniframe[i].style.display = "block"
				//如果用户的浏览器是NetScape
				if (dyniframe[i].contentDocument
						&& dyniframe[i].contentDocument.body.offsetHeight)
					dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
				//如果用户的浏览器是IE
				else if (dyniframe[i].Document
						&& dyniframe[i].Document.body.scrollHeight)
					dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
			}
		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide == "no") {
			var tempobj = document.all ? document.all[iframeids[i]]
					: document.getElementById(iframeids[i])
			tempobj.style.display = "block"
		}
	}
}

$(function() {
	dyniframesize();
});

$(window).resize(function() {
	dyniframesize();
});
</SCRIPT>
</head>
<body>
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
									联通测速
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									单台测速
								</td>
							</tr>
						</table>
					</td>
				</tr>

				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/ids/share/idsShareDeviceWAN.jsp"%>
					</td>
				</TR>
				<TR>
					<td id="trDeviceResult" style="display:none;">
						<FORM NAME="frm" METHOD="post" action="<s:url value='/ids/HTTPDeviceQuery!queryHTTPService.action'/>"
							target="dataForm" onsubmit="return CheckForm()">
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" >
									<TR>
										<TD bgcolor=#999999>
												<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
														<TR bgcolor="#FFFFFF">
															<td nowrap align="right" class=column width="15%">
																设备属地
																<input type="hidden" name="city_id" id="city_id" value=""/>
																<input type="hidden" name="oui" id="oui" value=""/>
																<input type="hidden" name="ip" id="ip" value=""/>
																<input type="hidden" name="BindType" id="BindType" value=""/>
																<input type="hidden" name="device_id" id="device_id" value="" />
																<input type="hidden" name="wan_interface" id="wan_interface" value="" />
																<input type="hidden" name="connType"  id="connType" value=""/>
															</td>
															<td id="tdDeviceCityName" width="35%">
															</td>
															<td nowrap align="right" class=column width="15%">
																设备序列号
															</td>
															<td id="tdDeviceSn" width="35%">
															</td>
														</TR>
														<TR bgcolor="#FFFFFF">
																<td  nowrap align="right" class=column width="15%">WAN通道</td>
																<td id="wan"  colspan="3">
																	<font color="red">正在获取wan通道信息...</font>
																</td>
														</TR>
														<!-- <TR bgcolor="#FFFFFF">
															<TD nowrap align="right" class=column width="15%">测速类型</TD>
															<TD  colspan="3">
																<input type="radio" name="type" size="20" value="DownLink" checked="checked" />下行测速
																<input type="radio" name="type" size="20" value="UpLink" />上行测速
															</TD>
															
														</TR> -->
														<TR bgcolor="#FFFFFF" id="testUserInfo2" >
															<TD nowrap align="right" class=column width="15%">背景流量</TD>
															<TD width="35%" colspan="1"><input type="text" id="code" name="code" size="20" class="bk" value="256" /></TD>
															<TD nowrap align="right" class=column width="15%">PPPOE账号</TD>
															<TD width="35%" colspan="1"><input type="text" id="pppoeUserName" name="pppoeUserName" size="20" class="bk"  /></TD>
														</TR>
														<TR bgcolor="#FFFFFF" id="testUserInfo2" >
															<TD nowrap align="right" class=column width="15%">仿真账号</TD>
															<TD width="35%" colspan="1"><input type="text" id="userName" name="userName" size="20" class="bk"  />
															<input type="text" id="test_rate" name="test_rate" size="20" class="bk" readonly />
															</TD>
															<TD nowrap align="right" class=column width="15%">仿真密码</TD>
															<TD width="35%" colspan="1"><input type="password" id="password" name="password" size="20" class="bk"  />
															
															</TD>
														</TR>
														<TR bgcolor="#FFFFFF" id="testUserInfo2" >
															<TD nowrap align="right" class=column width="15%">测试路径</TD>
															<TD width="35%" colspan="1"><input type="text" id="column1" name="column1" size="40" class="bk"  /></TD>
															<TD nowrap align="right" class=column width="15%">上报路径</TD>
															<TD width="35%" colspan="1"><input type="text" id="column2" name="column2" size="40" class="bk"  /></TD>
														</TR>
														<TR bgcolor="#FFFFFF">
															<TD colspan="5" align="right" class="green_foot">
																<INPUT TYPE="button" name="button" id="button" class=jianbian value= " 测   试" disabled="disabled" onclick="javaScript:queryHTTPService()">
															</TD>
														</TR>
												</TABLE>
										</TD>
									</TR>		
							</TABLE>
						</FORM>
					</td>
				</tr>
				
				<tr>
					<td height="20"></td>
				</tr>
				
				<TR bgcolor="#FFFFFF">
					<TD id="messageInfo"  style="display:none;background-color: #E1EEEE;height: 20" >
						后台正在查询，HTTP通道信息并入库，请耐心等待......		
					</TD>
				</TR>
				<TR>
					<TD id="tdData" style="display:none;" HEIGHT=20>
						<iframe id="dataForm" name="dataForm" height=0 frameborder="0" scrolling="no" width="100%" src=""></iframe>
					</TD>
				</TR>
			</table>
		</TD>
	</TR>
	
</TABLE>
</body>
</html>
<%@ include file="../../foot.jsp"%>