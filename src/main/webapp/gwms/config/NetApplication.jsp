<%@ include file="../../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags" %>
<%@taglib prefix="lk" uri="/linkage" %>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>����Ӧ��</title>
<link href="<s:url value='../../css/css_green.css'/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value='../../Js/jquery.js'/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.blockUI.js"></SCRIPT>

<%
  String gw_type = request.getParameter("gw_type");
%>

<SCRIPT LANGUAGE="JavaScript">

//ϵͳ����
var gw_type = <%= gw_type%>

$(function(){
	if(2==gw_type){
		$("table[@id='tab1']").css("width","10%");
//		$("th[@id='td2']").css("display","none");
		$("td[@id='yf']").html("��<a href='javascript:Using(0);'>�������</a>��");
	}
});

//var iframeSrcUsing=["usingServicesWideNet.jsp","usingServicesIptv.jsp","usingServicesVoip.jsp"];
var iframSrcNetUsing = ["<s:url value='/gwms/config/wanACT.action?deviceId='/>",
		                "<s:url value='/gwms/config/iptvACT.action?deviceId='/>",
		                "<s:url value='/gwms/config/voipACT.action?deviceId='/>",
		                "<s:url value='/gwms/config/iptvACT!getIptvData.action?deviceId='/>"];

//"����SSID","IPTV","VoIP","�Զ���ģ��"
//var iframeSrcQos=["<s:url value='/gwms/config/qosConfig.action?qosType=ssid'/>" + "&deviceId=",
//				  "<s:url value='/gwms/config/qosConfig.action?qosType=iptv'/>" + "&deviceId=",
//				  "<s:url value='/gwms/config/qosConfig.action?qosType=voip'/>" + "&deviceId=",
//				  "<s:url value='/gwms/config/qosConfig.action?qosType=serv'/>" + "&deviceId="];
var iframeSrcQos=["<s:url value='/gwms/config/qosConfig.action?qosType=ssid'/>" + "&deviceId=",
				  "<s:url value='/gwms/config/qosConfig.action?qosType=serv'/>" + "&deviceId="];


function block(msg){
	$.blockUI({
		overlayCSS:{ 
	        backgroundColor:'#CCCCCC', 
	        opacity:0.6 
    	},
		message:"<span style='font-size:14px;font-family: ����'>���ڲ��������Ժ�...</span>"
	});      
}

function unblock(){
	$.unblockUI();
} 

function winNavigate(uniformid)
{
	switch (uniformid)
	{
		case 1:
		{
			document.all("td1").className="curendtab_bbms";
			document.all("td2").className="endtab_bbms";
			this.trUsing.style.display="";
			this.trQos.style.display="none";
			document.all("myiframe1").src = "";
			break;
		}
		case 2:
		{
			document.all("td1").className="endtab_bbms";
			document.all("td2").className="curendtab_bbms";
			this.trUsing.style.display="none";
			this.trQos.style.display="";
			document.all("myiframe1").src = "";
			//document.all("myiframe1").src = iframeSrc[1]+document.all("txtdeviceId").value;
			break;
		}
	}
}

function txtSelectDevice()
{		
	if("none"==document.all("deviceResult").style.display){
		document.all("deviceResult").style.display="";
		document.all("btnDevRes").value="���ز�ѯ";
	}else{
		document.all("deviceResult").style.display="none";
		document.all("btnDevRes").value="չ����ѯ";
	}
}

function selectbyonclick(deviceId,deviceSn)
{	
	document.all("txtdeviceId").value=deviceId;
	document.all("deviceSn").value=deviceSn;
	$("div[@id='selectedDev']").html("<strong>�������豸���к�:"+deviceSn+"</strong>");
	
	this.tr1.style.display="";
	this.tr2.style.display="";
	winNavigate(1);
	
	document.all("deviceResult").style.display="none";
	document.all("btnDevRes").value="չ����ѯ";
	
}

function Using(id){
	//document.all("myiframe1").src = iframeSrcUsing[id];
	if (0 == id) {
		block("����ȡ�������ӡ�");
	} else if (1 == id) {
		block("����ȡIPTV���ӡ�");
	} else if(2 == id){
		block("����ȡVOIP���ӡ�");
	} else {
		block("����ȡIPTV���ӡ�");
	}
	if(3 == id){
		document.all("myiframe1").src = iframSrcNetUsing[id]+document.all("txtdeviceId").value+"&deviceSn="+document.all("deviceSn").value+"&gw_type="+gw_type;
	}else{
		document.all("myiframe1").src = iframSrcNetUsing[id]+document.all("txtdeviceId").value+"&gw_type="+gw_type;
	}
}

function Qos(id){
	block("");

	document.all("myiframe1").src = iframeSrcQos[id]+document.all("txtdeviceId").value+"&gw_type="+gw_type;
}

//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["myiframe1"]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block"
     			//����û����������NetScape
     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
      			//����û����������IE
     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
   			 }
   		}
		//�����趨�Ĳ���������֧��iframe�����������ʾ����
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
    		tempobj.style.display="block"
		}
	}
}


$(window).resize(function(){
	dyniframesize();
}); 

//if (window.addEventListener)
// 	window.addEventListener("load", dyniframesize, false)
//else if (window.attachEvent)
// 	window.attachEvent("onload", dyniframesize)
//else
//	window.onload=dyniframesize

//function iframeAutoFit(iframeObj){ 
//	setTimeout(function(){
//		if(!iframeObj) return;
//		iframeObj.height=(iframeObj.Document?iframeObj.Document.body.scrollHeight:iframeObj.contentDocument.body.offsetHeight);},200) 
//}

</SCRIPT>
<%@ include file="../../toolbar.jsp"%>
<style>
span
{
	position:static;
	border:0;
}
</style>

<jsp:include page="/share/selectDeviceJs.jsp">
	<jsp:param name="selectType" value="radio"/>
	<jsp:param name="jsFunctionName" value="selectbyonclick"/>
	<jsp:param name="jsFunctionNameBySn" value="true"/>
	<jsp:param name="byDeviceno" value="2"/>
	<jsp:param name="buUsername" value="1"/>
	<jsp:param name="byCity" value="0"/>
	<jsp:param name="byImport" value="0"/>
	<jsp:param name="div_device_height" value="60"/>
</jsp:include>

</head>
<body>
<form name="form1">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=18>&nbsp;
			
		</TD>
	</TR>
	<tr>
		<td >
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									����Ӧ��
								</td>
								<td nowrap>
									<img src="../../images/attention_2.gif" width="15" height="12">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table id="deviceResult" width="100%" border=0 cellspacing=0 cellpadding=0 align="center" valign="middle" STYLE="display:">
				<TR>
					<TH colspan="4" align="center">
						�豸��ѯ
					</TH>
				</TR>
				<TR bgcolor="#FFFFFF" >
					<td colspan="4">
						<div id="selectDevice"><span>������....</span></div>
					</td>
				</TR>
			</table>
		</td>
	</tr>
	<TR id="tr1"  STYLE="display:none" bgcolor="#FFFFFF" >
		<td bgcolor=#999999>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH colspan="2" align="center">
						����Ӧ��
					</TH>
				</TR>
				<tr bgcolor="#FFFFFF">
					<td width="50%" >
						<div id="selectedDev">
							���ѯ�豸��
						</div>
						<input type="hidden" name="txtdeviceId" value="" />
						<input type="hidden" name="deviceSn" value="" />
					</td>
					<td align="right" width="50%" >
						<input type="button" name="btnDevRes" class=jianbian value="���ز�ѯ" onclick="txtSelectDevice()"/>      
					</td>
				</tr>
			</table>
		</td>
	</TR>
	<TR>
		<TD HEIGHT=5>&nbsp;</TD>
	</TR>
	<TR  id="tr2"  STYLE="display:none" >
  		<TD>
			<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" valign="middle">
				<TR>
					<TD>
						<TABLE width="40%" id="tab1" border=0 cellspacing=0 cellpadding=0>
							<TR width="98%" height=30>
								<TH class="curendtab_bbms" id="td1" width="30%"><a class="tab_A" href="javascript:winNavigate(1);">Ӧ�÷���</a></TH>
								<TH class="endtab_bbms" id="td2" width="30%"><a class="tab_A" href="javascript:winNavigate(2);">QoS������</a></TH>
							</TR>
						</TABLE>
 					</TD>
				</TR>
				<TR id="trUsing" STYLE="display:none">
					<td>
						<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
							<tr height="25">
								<td class=column5 id="yf" align="left">
									��<a href="javascript:Using(0);">�������</a>��
									|��<a href="javascript:Using(1);">IPTV</a>��
									|��<a href="javascript:Using(2);">VoIP</a>��
									<ms:inArea areaCode="sx_lt" notInMode="true">
										|��<a href="javascript:Using(3);">���ն�</a>��
									</ms:inArea>
								</td>
							</tr>
						</TABLE>
					</td>
				</TR>
				<TR id="trQos" STYLE="display:none">
					<td>
						<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
							<tr height="25">
								<!-- <td class=column5 align="left">
									��<a href="javascript:Qos(0);">����</a>��
									|��<a href="javascript:Qos(1);">IPTV</a>��
									|��<a href="javascript:Qos(2);">VoIP</a>��
									|��<a href="javascript:Qos(3);">ҵ��</a>��
								</td> -->
								<td class=column5 align="left">
									��<a href="javascript:Qos(0);">����SSID��QoS</a>��
									|��<a href="javascript:Qos(1);">ҵ��QoS</a>��
								</td>

							</tr>
						</TABLE>
					</td>
				</TR>
				<TR>
					<td>
						<DIV id=qh_con0 style="DISPLAY: block">
							<iframe id=myiframe1 src="" frameborder="0" scrolling="no" height="130%" width="100%" ></iframe>
						</DIV>
					</td>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</form>
</body>
<%@ include file="../foot.jsp"%>
