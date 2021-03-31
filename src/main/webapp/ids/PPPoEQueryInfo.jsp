<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>PPPoE����</title>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/CheckFormForm.js"></SCRIPT>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">



function deviceResult(callData){
	$("input[@name='gwShare_queryButton']").attr("disabled", false);
 	init();
	var url = "<s:url value='/ids/PPPoEDeviceQuery!getDefaultdiag.action'/>";
	$.post(url, {},
				 function(ajax) {
						var data = ajax.split("#");
						$("input[@name='column1']").val($.trim(data[0]));
						$("input[@name='column2']").val($.trim(data[1]));
						$("select[@name='column3']").val(data[2]);
						$("input[@name='column4']").val(data[3]);
						$("input[@name='ip_address']").val(data[4]);
						$("input[@name='default_gateway']").val(data[5]);
						
	});
	var returnVal = callData.split("#");
	$("td[@id='tdDeviceSn']").append(returnVal[1]);
	$("input[@name='device_id']").val(returnVal[1]);
	$("input[@name='oui']").val(returnVal[2]);
	$("input[@name='BindType']").val(returnVal[3]);
	$("input[@name='ip']").val(returnVal[4]);
	$("input[@name='city_id']").val(returnVal[5]);
	$("td[@id='tdDeviceCityName']").append(returnVal[6]);
	var ids_queryField = $("input[@name='idsShare_queryField']").val();
	var ids_queryParam = $("input[@name='idsShare_queryParam']").val();
	ids_queryParam = $.trim(ids_queryParam);
	var wanurl = "<s:url value='/ids/IdsShareDevice!queryIdsWANDetail.action'/>";
	$.post(wanurl, {idsShare_queryField : ids_queryField,
				 idsShare_queryParam : ids_queryParam},
				 function(ajax){
					 $("input[@name='gwShare_queryButton']").attr("disabled", false);
						var data = ajax.split("#");
						if("1"==data[0]){
							$("#wan").html("<font color='red'>"+data[1]+"</font>");
						}else{
							var wanHtml = $("#wan").html("");
							
							var one = data[1].split("^");
							for(var i=0; i<one.length; i++){	
								if( "undefined" != typeof(one[i]) && ""!=one[i]){
									var ifs = one[i].split("��");
									// ԭ��д��41���ڽ��յ������治���á��ʶ��޸�
									if(ifs[0]!=null && ifs[0].indexOf('WANPPPConnection')>0 && ifs[1] != null){
										wanHtml.append("<input type='radio' name='wanType' value='"+encodeURIComponent(one[i])+"' checked='checked' onclick='ids_queryField_selected(1)'/> ����ͨ��("+ifs[1]+")&nbsp;&nbsp;");
									}
								
								}
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


function ids_queryField_selected(value){
	$("input[@name='wan_interface']").val(value);
}

function queryPPPoEService(){
	$("td[@id='messageInfo']").css("display","");
	$("td[@id='tdData']").css("display","");
	$("input[@name='button']").attr("disabled", true);
	document.frm.submit();
}

function init(){
	$("td[@id='messageInfo']").css("display","none");
	$("td[@id='trDeviceResult']").css("display","");
	$("td[@id='tdDeviceSn']").html("");
	$("td[@id='tdDeviceCityName']").html("");
	$("#wan").html("<font color='red'>���ڻ�ȡwanͨ����Ϣ...</font>");
}

function ck(){
	var _column1 = $("input[@name='column1']");
	var _column2 = $("input[@name='column2']");
	//var _column3 = $("select[@name='column3']");
	var _column4 = $("input[@name='column4']");
	//��SIP��������ַ
	if(!IsNull(_column1.val(), "PPPoE�û���")){
		_column1.focus();
		return false;
	}
	if(!IsNull(_column2.val(), "PPPoE����")){
		_column2.focus();
		return false;
	}
	if(!IsNull(_column4.val(), "�ظ�����")){
		_column4.focus();
		return false;
	}
	
	return true
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
									PPPoe����
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									��̨PPPoe����
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
						<FORM NAME="frm" METHOD="post" action="<s:url value='/ids/PPPoEDeviceQuery!queryPPPoEService.action'/>"
							target="dataForm" onsubmit="return CheckForm()">
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" >
									<TR>
										<TD bgcolor=#999999>
												<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
														<TR bgcolor="#FFFFFF" id="trDeviceResult"
															style="display: ">
															<td nowrap align="right" class=column width="15%">
																�豸����
																<input type="hidden" name="city_id" id="city_id" value=""/>
																<input type="hidden" name="oui" id="oui" value=""/>
																<input type="hidden" name="ip" id="ip" value=""/>
																<input type="hidden" name="BindType" id="BindType" value=""/>
																<input type="hidden" name="device_id" id="device_id" value="" />
																<input type="hidden" name="wan_interface" id="wan_interface" value="" />
																<input type="hidden" name="ip_address" id="ip_address" value="" />
																<input type="hidden" name="default_gateway" id="default_gateway" value="" />
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
																	<font color="red">���ڻ�ȡwanͨ����Ϣ...</font>
																</td>
														</TR>
														
														<TR bgcolor="#FFFFFF">
															<TD nowrap align="right" class=column width="15%">PPPoE�û���</TD>
															<TD width="35%"><input type="text" name="column1" size="20" class="bk"  />&nbsp;&nbsp;<font color="red">*</font></TD>
															
															<TD nowrap align="right" class=column width="15%">PPPoE����</TD>
															<TD width="35%"><input type="text" name="column2" size="20"  class="bk"  />&nbsp;&nbsp;<font color="red">*</font></TD>
														</TR>
														
														<TR bgcolor="#FFFFFF">
															<TD nowrap align="right" class=column width="15%">��֤ģʽ</TD>
															
															
															<TD width="35%">
															<select name="column3" class="bk">
																		<option value="PAP" selected="selected">==PAP==</option>
																		<option value="CHAP">==CHAP==</option>
															</select>
															<font color="red">*</font></TD>
															<TD nowrap align="right" class=column width="15%">�ظ�����</TD>
															<TD width="35%"><input type="text" name="column4" size="20"  class="bk"  />&nbsp;&nbsp;<font color="red">*</font></TD>
														</TR>
														
														<TR bgcolor="#FFFFFF">
															<TD colspan="5" align="right" class="green_foot">
																<INPUT TYPE="button" name="button" id="button" class=jianbian value=" ��   ��" disabled="disabled" onclick="javaScript:ck()&&queryPPPoEService()">
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
				
				<TR>
					<TD id="messageInfo"  style="display:none;background-color: #E1EEEE;height: 20" >
						��̨���ڲ�ѯ��PPPoEͨ����Ϣ����⣬�����ĵȴ�������������		
					</TD>
				</TR>
	
				<TR>
					<TD id="tdData" style="display:none;"  width="100%">
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