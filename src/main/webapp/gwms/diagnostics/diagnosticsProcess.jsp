<%@ include file="../../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>���ϴ���</title>
<link href="<s:url value="../../css/css_green.css"/>" rel="stylesheet" type="text/css">

<%
	String gw_type = request.getParameter("gw_type");
%>
 
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/commFunction.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.blockUI.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
var gw_type = "<%= gw_type%>";

function block(){   
	$.blockUI({
		overlayCSS:{ 
	        backgroundColor:'#CCCCCC', 
	        opacity:0.6 
    	},
		message:"<font size=3>���ڲ��������Ժ�...</font>"
	});      
}

function unblock(){
	$.unblockUI();
} 

var iframeSrc=["<s:url value='/gwms/diagnostics/deviceInfo.action?deviceId='/>","<s:url value='/gwms/diagnostics/algACT.action?deviceId='/>","<s:url value='/gwms/diagnostics/deviceDiagnostics.action?deviceId='/>"];

var iframeSrcProcess=["<s:url value='/gwms/diagnostics/deviceDiagnostics.action?diagType=wiredNetDiag'/>" + "&deviceId=",
		"<s:url value='/gwms/diagnostics/deviceDiagnostics.action?diagType=wirelessNetDiag'/>" + "&deviceId=",
		"<s:url value='/gwms/diagnostics/deviceDiagnostics.action?diagType=netSlowDiag'/>" + "&deviceId=",
		"<s:url value='/gwms/diagnostics/deviceDiagnostics.action?diagType=netOfflineDiag'/>" + "&deviceId=",
		"<s:url value='/gwms/diagnostics/deviceDiagnostics.action?diagType=iptvDiag'/>" + "&deviceId=",
		"<s:url value='/gwms/diagnostics/deviceDiagnostics.action?diagType=diagTools'/>" + "&deviceId=",
		"<s:url value='/gwms/diagnostics/iaddiag.action?deviceId='/>"];

var iframeSrcAdvance=["<s:url value='/gwms/diagnostics/algACT.action?deviceId='/>",
		"<s:url value='/gwms/diagnostics/mwbandACT.action?deviceId='/>",
		"<s:url value='/gwms/diagnostics/wlanACT.action?deviceId='/>",
		"<s:url value='/gwms/diagnostics/xcompasswdACT.action?deviceId='/>",
		"<s:url value='/gwms/diagnostics/upnpACT.action?deviceId='/>",
		"<s:url value='/gwms/diagnostics/onuACT.action?deviceId='/>",
		"<s:url value='/gwms/diagnostics/dnsModifyACT.action?deviceId='/>"];

var iframeHealthSrc=["<s:url value='/gwms/diagnostics/HealthInfo.action?deviceId='/>"];

function winNavigate(uniformid)
{
	//alert(uniformid);
	switch (uniformid)
	{
		case 1:
		{
			document.all("td1").className="curendtab_bbms";
			document.all("td2").className="endtab_bbms";
			document.all("td3").className="endtab_bbms";
			document.all("td4").className="endtab_bbms";
			this.trProcess.style.display="none";
			this.trAdvance.style.display="none";
			document.all("myiframe1").src = iframeSrc[0]+document.all("txtdeviceId").value + "&gw_type=" + gw_type;
			break;
		}
		case 2:
		{
			document.all("td1").className="endtab_bbms";
			document.all("td2").className="curendtab_bbms";
			document.all("td3").className="endtab_bbms";
			document.all("td4").className="endtab_bbms";
			this.trProcess.style.display="none";
			this.trAdvance.style.display="";
			document.all("myiframe1").src = "";
			//document.all("myiframe1").src = iframeSrc[1]+document.all("txtdeviceId").value;
			break;
		}
		
		case 3:
		{
			document.all("td1").className="endtab_bbms";
			document.all("td2").className="endtab_bbms";
			document.all("td3").className="curendtab_bbms";
			document.all("td4").className="endtab_bbms";
			this.trProcess.style.display="";
			this.trAdvance.style.display="none";
			document.all("myiframe1").src = "";
			//document.all("myiframe1").src = iframeSrc[2]+document.all("txtdeviceId").value;
			break;
		}
		case 4:
		{
			document.all("td1").className="endtab_bbms";
			document.all("td2").className="endtab_bbms";
			document.all("td3").className="endtab_bbms";
			document.all("td4").className="curendtab_bbms";
			this.trProcess.style.display="none";
			this.trAdvance.style.display="none";
			document.all("myiframe1").src = iframeHealthSrc[0]+document.all("txtdeviceId").value+"&gwType="+gw_type;
			block();
			break;
		}
	}
}


function winNavigate4SX(uniformid)
{
	//alert(uniformid);
	switch (uniformid)
	{
		case 1:
		{
			document.all("td1").className="curendtab_bbms";
			document.all("td2").className="endtab_bbms";
			document.all("td3").className="endtab_bbms";
			this.trProcess.style.display="none";
			this.trAdvance.style.display="none";
			document.all("myiframe1").src = iframeSrc[0]+document.all("txtdeviceId").value + "&gw_type=" + gw_type;
			break;
		}
		case 2:
		{
			document.all("td1").className="endtab_bbms";
			document.all("td2").className="curendtab_bbms";
			document.all("td3").className="endtab_bbms";
			this.trProcess.style.display="none";
			this.trAdvance.style.display="";
			document.all("myiframe1").src = "";
			//document.all("myiframe1").src = iframeSrc[1]+document.all("txtdeviceId").value;
			break;
		}
		
		case 3:
		{
			document.all("td1").className="endtab_bbms";
			document.all("td2").className="endtab_bbms";
			document.all("td3").className="curendtab_bbms";
			this.trProcess.style.display="";
			this.trAdvance.style.display="none";
			document.all("myiframe1").src = "";
			//document.all("myiframe1").src = iframeSrc[2]+document.all("txtdeviceId").value;
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
	$("div[@id='selectedDev']").html("<strong>�������豸���к�:"+deviceSn+"</strong>");
	
	this.tr1.style.display="";
	this.tr2.style.display="";
	winNavigate(1);
	document.all("deviceResult").style.display="none";
	document.all("btnDevRes").value="չ����ѯ";
	
}

function diagnosticsProcess(id){
	document.all("myiframe1").src = iframeSrcProcess[id]+document.all("txtdeviceId").value + "&gw_type="+gw_type;
}

function advanceSelect(id){

	block();	
	document.all("myiframe1").src = iframeSrcAdvance[id]+document.all("txtdeviceId").value + "&gw_type="+gw_type;
}

//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["myiframe1"];

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes";

function dyniframesize() 
{
	var dyniframe=new Array();
	for (var i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block";
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
    		tempobj.style.display="block";
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
	<jsp:param name="byWideNet" value="6"/>
	<jsp:param name="byItv" value="5"/>
	<jsp:param name="byVoipTelNum" value="3"/>
	<jsp:param name="byDeviceno" value="2"/>
	<jsp:param name="buUsername" value="1"/>
	<jsp:param name="byCity" value="0"/>
	<jsp:param name="byImport" value="0"/>
	<jsp:param name="div_device_height" value="60"/>
</jsp:include>

</head>
<body>
<form name="form1">
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%">
	<TR>
		<TD HEIGHT=18>&nbsp;</TD>
	</TR>
	<tr>
		<td >
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									���ϴ���
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
						���ϴ���
					</TH>
				</TR>
				<tr bgcolor="#FFFFFF">
					<td width="50%" >
						<div id="selectedDev">
							���ѯ�豸��
						</div>
						<input type="hidden" name="txtdeviceId" value="" />
					</td>
					<td align="right" width="50%" >
						<input type="button" name="btnDevRes" class=btn value="���ز�ѯ" onclick="txtSelectDevice()"/>      
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
						<TABLE width="59%" border=0 cellspacing=0 cellpadding=0>
							<TR width="98%" height=30>
								<ms:inArea areaCode="sx_lt" notInMode="true">
								<TH class="curendtab_bbms" id="td1" width="25%"><a class="tab_A" href="javascript:winNavigate(1);">�豸��Ϣ</a></TH>
								<TH class="endtab_bbms" id="td2" width="25%"><a class="tab_A" href="javascript:winNavigate(2);">�߼���ѯ</a></TH>
								<TH class="endtab_bbms" id="td3" width="25%"><a class="tab_A" href="javascript:winNavigate(3);">�������</a></TH>
								<TH class="endtab_bbms" id="td4" width="25%"><a class="tab_A" href="javascript:winNavigate(4);">������</a></TH>
								</ms:inArea>
								<ms:inArea areaCode="sx_lt" notInMode="false">
								<TH class="curendtab_bbms" id="td1" width="35%"><a class="tab_A" href="javascript:winNavigate4SX(1);">�豸��Ϣ</a></TH>
								<TH class="endtab_bbms" id="td2" width="35%"><a class="tab_A" href="javascript:winNavigate4SX(2);">�߼���ѯ</a></TH>
								<TH class="endtab_bbms" id="td3" width="30%"><a class="tab_A" href="javascript:winNavigate4SX(3);">�������</a></TH>
								</ms:inArea>
							</TR>
						</TABLE>
 					</TD>
				</TR>
				<TR id="trAdvance" STYLE="display:none">
					<td>
						<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
							<tr height="25">
								<td class=column5 align="left">
									��<a href="javascript:advanceSelect(0);">ALG����</a>��
									<ms:inArea areaCode="sx_lt" notInMode="true">
									|��<a href="javascript:advanceSelect(1);">���ն�����</a>��
									|��<a href="javascript:advanceSelect(2);">WLAN������ѯ</a>��
									|��<a href="javascript:advanceSelect(3);">��������Ա����</a>��
									</ms:inArea>
									<ms:hasAuth authCode="ShowDevPwd">

									</ms:hasAuth>
									<ms:inArea areaCode="jx_dx" notInMode="false">
									|��<a href="javascript:advanceSelect(4);">UPNP</a>��
									|��<a href="javascript:advanceSelect(5);">ONU�豸ʱ��ͬ��</a>��
									|��<a href="javascript:advanceSelect(6);">DNS�޸�</a>��
									</ms:inArea>									
								</td>
							</tr>
						</TABLE>
					</td>
				</TR>
				<TR id="trProcess" STYLE="display:none">
					<td>
						<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
							<tr height="25">
								<td class=column5 align="left">
						 <!--��<a href="javascript:diagnosticsProcess(0);">�����޷�����</a>��
									|��<a href="javascript:diagnosticsProcess(1);">�����޷�����</a>��
									|��<a href="javascript:diagnosticsProcess(2);">�����ٶ���</a>��
									|��<a href="javascript:diagnosticsProcess(3);">�쳣���߶�</a>��
									|��<a href="javascript:diagnosticsProcess(4);">IPTV�޷�ʹ��</a>��
								|��<a href="javascript:diagnosticsProcess(6);">VOIPע�����</a>�� -->
							
							��<a href="javascript:diagnosticsProcess(5);">������Ϲ���</a>��
								</td>
							</tr>
						</TABLE>
					</td>
				</TR>
				<TR>
					<td>
						<DIV id=qh_con0 style="DISPLAY: block">
							<iframe id=myiframe1 src="" frameborder="0" scrolling="no" height="100%" width="100%" onload=this.height=myiframe1.document.body.scrollHeight;></iframe>
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
