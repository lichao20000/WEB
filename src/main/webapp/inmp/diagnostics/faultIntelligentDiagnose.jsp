<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ҵ������������</title>
<link href="<s:url value="../../css/inmp/css/css_green.css"/>" rel="stylesheet" type="text/css">

<script type="text/javascript" src="<s:url value="/Js/inmp/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/inmp/jquery.blockUI.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
	var gw_type = "1";
	
	function block(){
		//$.blockUI({
		//	overlayCSS:{ 
		//        backgroundColor:'#CCCCCC', 
		//        opacity:0.6 
	    //	},
		//	message:"<font size=3>���ڲ��������Ժ�...</font>"
		//});      
	}

	function unblock(){
		//$.unblockUI();
	} 

	var iframeSrc=["<s:url value='/inmp/diagnostics/deviceInfo.action?deviceId='/>",
	               "<s:url value='/inmp/diagnostics/algACT.action?deviceId='/>",
	               "<s:url value='/inmp/diagnostics/deviceDiagnostics.action?deviceId='/>"];

	var iframeSrcProcess=["<s:url value='/inmp/diagnostics/deviceDiagnostics.action?diagType=wiredNetDiag'/>" + "&deviceId=",
			              "<s:url value='/inmp/diagnostics/deviceDiagnostics.action?diagType=wirelessNetDiag'/>" + "&deviceId=",
						  "<s:url value='/inmp/diagnostics/deviceDiagnostics.action?diagType=netSlowDiag'/>" + "&deviceId=",
						  "<s:url value='/inmp/diagnostics/deviceDiagnostics.action?diagType=netOfflineDiag'/>" + "&deviceId=",
						  "<s:url value='/inmp/diagnostics/deviceDiagnostics.action?diagType=iptvDiag'/>" + "&deviceId=",
						  "<s:url value='/inmp/diagnostics/deviceDiagnostics.action?diagType=diagTools'/>" + "&deviceId=",
			              "<s:url value='/inmp/diagnostics/iaddiag.action?deviceId='/>"];

	var iframeSrcAdvance=["<s:url value='/inmp/diagnostics/algACT.action?deviceId='/>","<s:url value='/inmp/diagnostics/mwbandACT.action?deviceId='/>","<s:url value='/inmp/diagnostics/wlanACT.action?deviceId='/>","<s:url value='/inmp/diagnostics/xcompasswdACT.action?deviceId='/>"];

	var iframeHealthSrc=["<s:url value='/inmp/diagnostics/HealthInfo.action?deviceId='/>"];

	function winNavigate(){
		//document.all("td1").className="endtab_bbms";
		//document.all("td2").className="endtab_bbms";
		//document.all("td3").className="curendtab_bbms";
		//document.all("td4").className="endtab_bbms";
		this.trProcess.style.display="";
		document.all("myiframe1").src = "";
	}
	
	
	function deviceResult(returnVal){
		deviceId=returnVal[2][0][0];
		document.all("txtdeviceId").value=returnVal[2][0][0];
		//document.all("txtoui").value=returnVal[2][0][1];
		deviceserialnumber = returnVal[2][0][2];  
		$("div[@id='selectedDev']").html("<strong>�������豸���к�:"+deviceserialnumber+"</strong>");
		
		winNavigate();
		
		this.tr1.style.display="";
		this.tr2.style.display="";
		document.all("deviceResult").style.display="none";
		document.all("btnDevRes").value="չ����ѯ";
	}

	function txtSelectDevice(){		
		if("none"==document.all("deviceResult").style.display){
			document.all("deviceResult").style.display="";
			document.all("btnDevRes").value="���ز�ѯ";
		}else{
			document.all("deviceResult").style.display="none";
			document.all("btnDevRes").value="չ����ѯ";
		}
	}

	function diagnosticsProcess(id){
		document.all("myiframe1").src = iframeSrcProcess[id]+document.all("txtdeviceId").value+"&gw_type="+gw_type;
	}

	function advanceSelect(id){
		block();
		document.all("myiframe1").src = iframeSrcAdvance[id]+document.all("txtdeviceId").value;
	}

	//** iframe�Զ���Ӧҳ�� **//
	//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
	//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
	//����iframe��ID
	var iframeids=["myiframe1"]
	
	//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
	var iframehide="yes"
	
	function dyniframesize() {
		var dyniframe=new Array()
		for (i=0; i<iframeids.length; i++){
			if (document.getElementById){
				//�Զ�����iframe�߶�
				dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera){
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
			if ((document.all || document.getElementById) && iframehide=="no"){
				var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
	    		tempobj.style.display="block"
			}
		}
	}


	$(window).resize(function(){
		dyniframesize();
	}); 

</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<style>
span
{
	position:static;
	border:0;
}
</style>

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
									ҵ������������
								</td>
								<td nowrap>
									<img src="../../images/inmp/attention_2.gif" width="15" height="12">
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
				<TR bgcolor="#FFFFFF" >
					<td colspan="4">
						<table id="deviceResultttt" width="100%" border=0 cellspacing=0
							cellpadding=0 align="center" valign="middle" >
							<tr bgcolor=#ffffff>
								<td class=column colspan="4">
									<%@ include file="/inmp/bss/gwShareDeviceQuery.jsp"%>
								</td>
							</tr>
						</table>
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
						<input type="button" name="btnDevRes" class=btn value="���ز�ѯ" onClick="txtSelectDevice()"/>      
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
						<TABLE width="13%" border=0 cellspacing=0 cellpadding=0>
							<TR width="98%" height=30>
								<!-- <TH class="curendtab_bbms" id="td1" width="25%"><a class="tab_A" href="javascript:winNavigate(1);">�豸��Ϣ</a></TH> -->
								<!-- <TH class="endtab_bbms" id="td2" width="25%"><a class="tab_A" href="javascript:winNavigate(2);">�߼���ѯ</a></TH> -->
								<TH class="endtab_bbms" id="td3" width="25%"><a class="tab_A" href="javascript:winNavigate(3);">�����������</a></TH>
								<!-- <TH class="endtab_bbms" id="td4" width="25%"><a class="tab_A" href="javascript:winNavigate(4);">������</a></TH> -->
							</TR>
					  </TABLE>
 					</TD>
				</TR>
				<TR id="trProcess" STYLE="display:none">
					<td>
						<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
							<tr height="25">
								<td class=column5 align="left">
									��<a href="javascript:diagnosticsProcess(0);">�����޷�����</a>��
									|��<a href="javascript:diagnosticsProcess(1);">�����޷�����</a>��
									|��<a href="javascript:diagnosticsProcess(2);">�����ٶ���</a>��
									|��<a href="javascript:diagnosticsProcess(3);">�쳣���߶�</a>��
									|��<a href="javascript:diagnosticsProcess(4);">IPTV�޷�ʹ��</a>��
									<!-- |��<a href="javascript:diagnosticsProcess(5);">������Ϲ���</a>�� -->
									|��<a href="javascript:diagnosticsProcess(6);">VOIPע�����</a>��
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
