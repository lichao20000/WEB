<%@ page contentType="text/html; charset=GBK"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
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
var pppoeName=new Array();

/**��ѯУ��**/
function CheckForm(){
 	var __device_id = $("input[@name='deviceId']").val();

	if(__device_id == null || __device_id == ""){
		alert("���Ȳ�ѯ�豸!");
		return false;
	}
	return true;
}
 
/**��ѯ�������**/
function deviceResult(callData){
	$("input[@name='button']").attr("disabled", true);
	$("input[@name='gwShare_queryButton']").attr("disabled", false);
	init();
	var returnVal = callData.split("||");
	
	$("input[@name='device_id']").val(returnVal[1]+"#"+returnVal[2]);
	$("td[@id='tdDeviceSn']").append(returnVal[2]);
	$("input[@name='oui']").val(returnVal[3]);
	$("td[@id='downlink']").html(returnVal[4]);
	$("input[name='downlink']").val(returnVal[4]);
	$("input[@name='userName']").val(returnVal[5]);
	$("input[@name='password']").val(returnVal[6]);
	$("input[@name='city_id']").val(returnVal[7]);
	$("td[@id='tdDeviceCityName']").append(returnVal[8]);
	$("input[@name='loid']").val(returnVal[9]);
	$("input[@name='netUsername']").val(returnVal[10]+"#"+returnVal[11]);
	 
	//$("input[@name='button']").attr("disabled", false); 
}

function getWan(type)
{
	var ids_queryField = $("input[@name='idsShare_queryField']").val();
	var ids_queryParam = $("input[@name='idsShare_queryParam']").val();
	ids_queryParam = $.trim(ids_queryParam);
	var wanurl = "<s:url value='/ids/IdsShareDevice!queryIdsWANDetail.action'/>";
	$.post(wanurl, {idsShare_queryField : ids_queryField,
				 idsShare_queryParam : ids_queryParam},
				 function(ajax){
						var data = ajax.split("#");
						if("1"==data[0]){
							$("#messageInfo").html("<font color='red'>"+data[1]+"</font>");
						}else{
							
							$("#messageInfo").html("");
							if (type == 1)
							{
							var one = data[1].split("^");
							
							for(var i=0; i<one.length; i++){	
								if( "undefined" != typeof(one[i]) && ""!=one[i]){
									var ifs = one[i].split("��");
								    $("input[name='wanType']").val(one[i]);
								}
							}
							$("#messageInfo").html("wanͨ����ȡ�ɹ������Խ��в���");
							ids_queryField_selected(1);
							if(Trim(data[1]).length > 0){
								var value = $("input[name='wanType']").val();
								if("undefined"==typeof(value)){
									$("#messageInfo").html("<font color='red'>wanͨ����ϢΪ��!</font>");
								}else{
									$("input[@name='button']").attr("disabled", false); 
								}
							}else{
								$("#messageInfo").html("<font color='red'>wanͨ����ϢΪ��!</font>");
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
	$("#messageInfo").html("���ڽ��в��٣����Ե�...");
	document.frm.submit();
}

function init(){
	$("td[@id='trDeviceResult']").css("display","");
	$("td[@id='tdDeviceSn']").html("");
	$("td[@id='tdDeviceCityName']").html("");
	$("td[@id='messageInfo']").css("display","");
	$("#messageInfo").html("���ڻ�ȡwanͨ����Ϣ...");
	getWan(1);
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
									HTTP���ز���
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									��̨HTTP����
								</td>
							</tr>
						</table>
					</td>
				</tr>

				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/ids/share/idsShareDeviceWAN_new.jsp"%>
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
																<input type="hidden" name="connType"  id="connType" value=""/>
																<input type="hidden" name="column1" value="http://182.106.141.126:6060/SOFT/speet3.dat"  />
																<input type="hidden" name="column2" value="1"  />
																<input type="hidden" id="userName" name="userName"  />
																<input type="hidden" id="password" name="password" />
																<input type='hidden' name='wanType' /> 
																<input type='hidden' name='idsShare_queryType' value="httpSpeed"/> 
																<input type='hidden' name='downlink' value=""/> 
																<input type='hidden' name='netUsername' value=""/> 
																<input type='hidden' name='loid' value=""/> 
															</td>
															<td id="tdDeviceCityName" width="35%">
															</td>
															<td nowrap align="right" class=column width="15%">
																�豸���к�
															</td>
															<td id="tdDeviceSn" width="35%">
															</td>
														</TR>
														<tr bgcolor="#FFFFFF">
														   <td nowrap align="right" class=column width="15%">
																�˺�ǩԼ����
															</td>
															<td id="downlink" width="35%">
															</td>
															<td nowrap align="right" class=column width="15%">
															</td>
															<td width="35%">
															</td>
														</tr>
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