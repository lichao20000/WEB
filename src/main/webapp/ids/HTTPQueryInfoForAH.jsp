<%@ page contentType="text/html; charset=GBK"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>HTTP����ҵ����������</title>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/CheckFormForm.js"></SCRIPT>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

var area = "<%=LipossGlobals.getLipossProperty("InstArea.ShortName")%>";
var wanType=new Array();
function CheckForm(){
 	var __device_id = $("input[@name='deviceId']").val();

	if(__device_id == null || __device_id == ""){
		alert("���Ȳ�ѯ�豸!");
		return false;
	}
	return true;
}

function deviceResult(callData){
	$("input[@name='gwShare_queryButton']").attr("disabled", false);
	
	init();
	var returnVal = callData.split("#");
	
	$("td[@id='tdDeviceSn']").append(returnVal[1]);
	$("input[@name='device_id']").val(returnVal[1]);
	$("input[@name='oui']").val(returnVal[2]);
	$("input[@name='BindType']").val(returnVal[3]);
	$("input[@name='ip']").val(returnVal[4]);
	$("input[@name='city_id']").val(returnVal[5]);
	$("td[@id='tdDeviceCityName']").append(returnVal[6]);
	$("#column2").val("0");
	if("jx_dx" != area){
	var ids_queryField = $("input[@name='idsShare_queryField']").val();
	var ids_queryParam = $("input[@name='idsShare_queryParam']").val();
	ids_queryParam = $.trim(ids_queryParam);
	var wanurl = "<s:url value='/ids/IdsShareDevice!queryIdsWANDetailForAH.action'/>";
	$.post(wanurl, {idsShare_queryField : ids_queryField,
				 idsShare_queryParam : ids_queryParam},
				 function(ajax){
						var data = ajax.split("#");
						if("1"==data[0]){
							$("#wan").html("<font color='red'>"+data[1]+"</font>");
						}else{
							
							var wanHtml = $("#wan").html("");
							
							var one = data[1].split("^");
							
							for(var i=0; i<one.length; i++){	
								if( "undefined" != typeof(one[i]) && ""!=one[i]){
									var ifs = one[i].split("��");
									//if(ifs[1]=="41"){
										wanHtml.append("<input type='radio' id='wanTypeId"+i+"' name='wanType' value='"+one[i]+"' checked='checked' onclick='ids_queryField_selected(1)'/> ����ͨ��("+ifs[1]+")&nbsp;&nbsp;");
									//}
									    wanType[i]=ifs[2];
								}
							}
							//���հ��յ���������Ժ���ܻ��ж���
							if(wanType[0]=="PPPoE_Bridged"){
							   $("#testUserInfo").show();
							   $("#testUserLabel").html("�����û�");
								 $("#testUserBox").show();
								 $("#connType").val("PPPoE_Bridged");
							}else{
								$("#connType").val("IP_Routed");
							}
							/**
							var two = data[2].split("^");
							
							for(var i=0; i<two.length; i++){
								
								if( "undefined" != typeof(two[i]) && ""!=two[i]){
									var tfs = two[i].split("��");
									wanHtml.append("<input type='radio' name='wanType' value='"+two[i]+"'  onclick='ids_queryField_selected(2)'/>����ͨ��("+tfs[1]+")&nbsp;&nbsp;");
								}
								
							}
							
							var three = data[3].split("^");
							for(var i=0; i<three.length; i++){
								if( "undefined" != typeof(three[i]) && ""!=three[i]){
									var vfs = three[i].split("��");
									wanHtml.append("<input type='radio' name='wanType' value='"+three[i]+"'  onclick='ids_queryField_selected(3)'/>����ͨ��("+vfs[1]+")&nbsp;&nbsp;");
								}
							}
							*/
							if(Trim(data[1]).length>0){
								var value = $("input[name='wanType']").val();
								if("undefined"==typeof(value)){
									$("#wan").html("<font color='red'>wanͨ����ϢΪ��!</font>");
								}else{
									$("input[@name='button']").attr("disabled", false); 
								}
							}else{
								$("#wan").html("<font color='red'>wanͨ����ϢΪ��!</font>");
							}
						}
	});
	}
}

function getWan(type)
{
	var ids_queryField = $("input[@name='idsShare_queryField']").val();
	var ids_queryParam = $("input[@name='idsShare_queryParam']").val();
	ids_queryParam = $.trim(ids_queryParam);
	$("#wan").html("<font color='red'>���ڻ�ȡwanͨ����Ϣ...</font>");
	var wanurl = "<s:url value='/ids/IdsShareDevice!queryIdsWANDetailForAH.action'/>";
	$.post(wanurl, {idsShare_queryField : ids_queryField,
				 idsShare_queryParam : ids_queryParam},
				 function(ajax){
						var data = ajax.split("#");
						if("1"==data[0]){
							$("#wan").html("<font color='red'>"+data[1]+"</font>");
						}else{
							
							var wanHtml = $("#wan").html("");
							if (type == 1)
							{
							var one = data[1].split("^");
							
							for(var i=0; i<one.length; i++){	
								if( "undefined" != typeof(one[i]) && ""!=one[i]){
									var ifs = one[i].split("��");
									//if(ifs[1]=="41"){
										wanHtml.append("<input type='radio' name='wanType' value='"+one[i]+"' checked='checked' onclick='ids_queryField_selected(1)'/> ����ͨ��("+ifs[1]+")&nbsp;&nbsp;");
									//}
									
								}
							}
							ids_queryField_selected(1);
							if(Trim(data[1]).length>0){
								var value = $("input[name='wanType']").val();
								if("undefined"==typeof(value)){
									$("#wan").html("<font color='red'>wanͨ����ϢΪ��!</font>");
								}else{
									$("input[@name='button']").attr("disabled", false); 
								}
							}else{
								$("#wan").html("<font color='red'>wanͨ����ϢΪ��!</font>");
							}
							} else {
							
							
							var three = data[3].split("^");
							for(var i=0; i<three.length; i++){
								if( "undefined" != typeof(three[i]) && ""!=three[i]){
									var vfs = three[i].split("��");
									wanHtml.append("<input type='radio' name='wanType' value='"+three[i]+"'  checked='checked' onclick='ids_queryField_selected(3)'/>����ͨ��("+vfs[1]+")&nbsp;&nbsp;");
								}
							}
							ids_queryField_selected(3);
							if(Trim(data[3]).length>0){
								var value = $("input[name='wanType']").val();
								if("undefined"==typeof(value)){
									$("#wan").html("<font color='red'>wanͨ����ϢΪ��!</font>");
								}else{
									$("input[@name='button']").attr("disabled", false); 
								}
							}else{
								$("#wan").html("<font color='red'>wanͨ����ϢΪ��!</font>");
							}
							}
							
						}
	});
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
	 $("#testUserInfo").hide();
	 $("#testUserLabel").html("");
	 $("#testUserBox").hide();
	 $("#connType").val("IP_Routed");
	$("td[@id='messageInfo']").css("display","none");
	$("td[@id='trDeviceResult']").css("display","");
	$("td[@id='tdDeviceSn']").html("");
	$("td[@id='tdDeviceCityName']").html("");
	if("jx_dx" != area){
	$("#wan").html("<font color='red'>���ڻ�ȡwanͨ����Ϣ...</font>");
	} else {
		$("#wan").html("<input type='radio' width='45%' name='httptype' value='1' onclick='getWan(this.value)'>�������<input type='radio' width='45%' name='httptype' value='2' onclick='getWan(this.value)'>TR069");
	}
}

function closeMessageInfo(){
	$("td[@id='messageInfo']").css("display","none");
}

//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids = [ "dataForm" ]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide = "yes"

function dyniframesize() {
	var dyniframe = new Array()
	for (i = 0; i < iframeids.length; i++) {
		if (document.getElementById) {
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document
					.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera) {
				dyniframe[i].style.display = "block"
				//����û����������NetScape
				if (dyniframe[i].contentDocument
						&& dyniframe[i].contentDocument.body.offsetHeight)
					dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
				//����û����������IE
				else if (dyniframe[i].Document
						&& dyniframe[i].Document.body.scrollHeight)
					dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
			}
		}
		//�����趨�Ĳ���������֧��iframe�����������ʾ����
		if ((document.all || document.getElementById) && iframehide == "no") {
			var tempobj = document.all ? document.all[iframeids[i]]
					: document.getElementById(iframeids[i])
			tempobj.style.display = "block"
		}
	}
}

function chooseTestUser(){
	var info=$("#testUserBox").val();
	var data = info.split("#");
	$("#userName").val(data[1]);
	$("#password").val(data[2]);
	$("#speed").val(data[3]);
	
}

$(function() {
	dyniframesize();
	initTestUserList();
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
									HTTP����ҵ���������
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									��̨HTTP����ҵ���������
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
																�豸����
																<input type="hidden" name="city_id" id="city_id" value=""/>
																<input type="hidden" name="oui" id="oui" value=""/>
																<input type="hidden" name="ip" id="ip" value=""/>
																<input type="hidden" name="BindType" id="BindType" value=""/>
																<input type="hidden" name="device_id" id="device_id" value="" />
																<input type="hidden" name="wan_interface" id="wan_interface" value="" />
																<input type="hidden" name="column2"  id="column2" value="0"/>
																<input type="hidden" name="connType"  id="connType" value="IP_Routed"/>
																<input type="hidden" name="speed"  id="speed" value=""/>
															</td>
															<td id="tdDeviceCityName" width="35%">
															</td>
															<td nowrap align="right" class=column width="15%">
																�豸���к�
															</td>
															<td id="tdDeviceSn" width="35%">
															</td>
														</TR>
														<TR bgcolor="#FFFFFF">
																<td  nowrap align="right" class=column width="15%">WANͨ��</td>
																<td id="wan"  colspan="3">
																<%if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
																	<input type="radio" width="45%" name="httptype" value="1" onclick="getWan(this.value)">�������
																	<input type="radio" width="45%" name="httptype" value="2" onclick="getWan(this.value)">TR069
																<%
																}else{ 
																%>
																	<font color="red">���ڻ�ȡwanͨ����Ϣ...</font>
																<%
																}
																%>
																</td>
														</TR>
														
														<TR bgcolor="#FFFFFF">
															<TD nowrap align="right" class=column width="15%">�û����ص�URL</TD>
															<!-- <TD width="35%"><input type="text" name="column1" value="http://117.71.32.12/speed1000.dat" size="30" class="bk"  ></TD> -->
															<TD width="35%"><input type="text" name="column1" value="http://speedtest.ahct.lv1.vcache.cn:9090/speed1000.dat" size="30" class="bk"  ></TD>
															<TD nowrap align="right" id="testUserLabel" class=column width="15%">�����û�</TD>
															<TD width="35%" >
															   <s:select style="display:none" list="testUserList" id="testUserBox" headerKey="-1" headerValue="��ѡ������û�" listKey="info" listValue="testname" cssClass="bk" onchange="chooseTestUser()"></s:select>
															</TD>
														</TR>
														<TR bgcolor="#FFFFFF" id="testUserInfo" style="display:none">
															<TD nowrap align="right" class=column width="15%">�����û���</TD>
															<TD width="35%"><input type="text" id="userName" name="userName" size="20" class="bk"  /></TD>
															
															<TD nowrap align="right" class=column width="15%">����</TD>
															<TD width="35%"><input type="password" id="password" name="password" size="20"  class="bk"  /></TD>
														</TR>
														<TR bgcolor="#FFFFFF">
															<TD colspan="5" align="right" class="green_foot">
																<INPUT TYPE="button" name="button" id="button" class=jianbian value= " ��   ��" disabled="disabled" onclick="javaScript:queryHTTPService()">
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
						��̨���ڲ�ѯ��HTTPͨ����Ϣ����⣬�����ĵȴ�......		
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