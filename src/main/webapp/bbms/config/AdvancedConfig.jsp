<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>�߼�����</title>
<link href="<s:url value='../../css/css_green.css'/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value='../../Js/jquery.js'/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.blockUI.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

//ϵͳ����
var gw_type = <%= LipossGlobals.SystemType()%>

//ALG,���������,Զ�̹�������,NTP/ʱ������
var iframSrcAdvance = ["<s:url value='/gwms/diagnostics/algACT!init.action?deviceId='/>",
						"<s:url value='/gwms/diagnostics/mwbandACT.action?deviceId='/>",
						"<s:url value='/gwms/diagnostics/xcompasswdACT.action?deviceId='/>",
		                "<s:url value='/bbms/config/ntpConfig.action?deviceId='/>"];
		                
var iframeSrcRemoteManage=["<s:url value='/bbms/config/snmpConfig.jsp?deviceId='/>"];

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
			this.trAdvance.style.display="";
			document.all("td2").className="endtab_bbms";
			this.trRemote.style.display="none";
			break;
		}
		case 2:
		{
			document.all("td2").className="curendtab_bbms";
			this.trRemote.style.display="";
			document.all("td1").className="endtab_bbms";
			this.trAdvance.style.display="none";
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

function advance(id){
	if (0 == id) {
		block("����ȡALG����״̬��");
	} else if (1 == id) {
		block("����ȡ�����������");
	} else if (2 == id) {
		block("����ȡԶ�̹������롱");
	} else {
		//block("����ȡNTP/ʱ�����á�");
	}
	
	document.all("myiframe1").src = iframSrcAdvance[id]+document.all("txtdeviceId").value;
}

function remote(id){
	
	if(0 == id) {
		//block("��ȡSNMP������Ϣ");
	}
	document.all("myiframe1").src = iframeSrcRemoteManage[id]+document.all("txtdeviceId").value;
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
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
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
									�߼�����
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
						�߼�����
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
						<TABLE width="20%" id="tab1" border=0 cellspacing=0 cellpadding=0>
							<TR width="98%" height=30>
								<TH class="curendtab_bbms" id="td1" width="40%">
									<a class="tab_A" href="javascript:winNavigate(1);">�߼�ѡ��</a>
								</TH>
								<TH class="curendtab_bbms" id="td2" width="40%">
									<a class="tab_A" href="javascript:winNavigate(2);">Զ�̹���</a>
								</TH>
							</TR>
						</TABLE>
 					</TD>
				</TR>
				<TR id="trAdvance" STYLE="display:none">
					<td>
						<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
							<tr height="25">
								<td class=column5 id="yf" align="left">
									��<a href="javascript:advance(0);">ALG����</a>��
									|��<a href="javascript:advance(1);">���������</a>��
									|��<a href="javascript:advance(2);">Զ�̹�������</a>��
									|��<a href="javascript:advance(3);">NTP/ʱ������</a>��
								</td>
							</tr>
						</TABLE>
					</td>
				</TR>
				<TR id="trRemote" STYLE="display:none">
					<td>
						<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
							<tr height="25">
								<td class=column5 id="yf" align="left">
									��<a href="javascript:remote(0);">SNMP����</a>��
									<!-- |��<a href="javascript:remote(1);">TR069����</a>��  -->
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
