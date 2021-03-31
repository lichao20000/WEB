<%--
FileName	: deviceInfo.jsp
Date		: 2009��6��25��
Desc		: ѡ���豸.
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="../head.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">

<title>�豸��Ϣ</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

function EC(leaf){
	if("basicInfo"==leaf){
		var m_bShow;
		m_bShow = (this.basicInfo.style.display=="none");
		this.basicInfo.style.display = m_bShow?"":"none";
	
		sobj = document.all("imgbasic");
		if(m_bShow) {	
			sobj.src = "../../images/up_enabled.gif";
		}
		else{
			sobj.src = "../../images/down_enabled.gif";
		}
		parent.dyniframesize();
	}else{
		parent.block();
		var m_bShow;
		m_bShow = (this.bussessInfo.style.display=="none");
		this.bussessInfo.style.display = m_bShow?"":"none";
		
		sobj = document.all("imgbussess");
		if(m_bShow) {
			sobj.src = "../../images/up_enabled.gif";
			
			$("div[@id='divAllInfo']").html("���ڴ�����....");
			parent.dyniframesize();
			
			var url = "<s:url value='/gwms/diagnostics/voipGather!getVoIPInfo.action'/>";
			var deviceId = document.getElementById("deviceId").value;
			var userId = document.getElementById("user_id").value;

			$.post(url,{
				deviceId:deviceId,
				userId:userId
		    },function(mesg){
				$("div[@id='divAllInfo']").html(mesg);
				parent.unblock();
				parent.dyniframesize();
		    });
		}
		else{
			sobj.src = "../../images/down_enabled.gif";
			parent.unblock();
			parent.dyniframesize();
		}
	}
}
</script>

</head>

<body>
<FORM NAME="frm" METHOD="post" action="">
<input type="hidden" id="deviceId" name="deviceId" value="<s:property value="deviceId"/>" />
<input type="hidden" is="user_id" name="user_id" value="<s:property value="deviceInfoMap.user_id"/>" />
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR >
		<TD>
			<table width="100%" height="30" border="0" cellspacing="0"
				cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite" >
						<a href="javascript:EC('basicInfo');" stytle="CURSOR:hand;">
						��������Ϣ��
						<IMG name="imgbasic" SRC="../../images/up_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="">
						</a>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
	<tr id=basicInfo>
		<TD bgcolor=#999999>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">�û�����:</TD>
					<s:if test="deviceInfoMap.linkman=='N/A' || deviceInfoMap.linkman=='null' ">
						<TD width="35%">-</TD>
					</s:if>
					<s:else>
						<TD width="35%"><s:property value="deviceInfoMap.linkman"/></TD>
					</s:else>
					<TD class=column align="right" nowrap width="15%">�û�SN:</TD>
					<TD width="35%"><s:property value="deviceInfoMap.username"/></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">�ն˳���:</TD>
					<TD width="35%"><s:property value="deviceInfoMap.vendor_add"/></TD>
					<TD class=column align="right" width="15%">�ն��ͺ�:</TD>
					<TD width="35%"><s:property value="deviceInfoMap.device_model"/></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">�ն�Ӳ���汾:</TD>
					<TD width="35%"><s:property value="deviceInfoMap.hardwareversion"/></TD>
					<TD class=column align="right" width="15%">����汾:</TD>
					<TD width="35%"><s:property value="deviceInfoMap.softwareversion"/></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">�ն�ע��״̬:</TD>
					<TD width="35%"><s:property value="deviceInfoMap.register"/></TD>
					<TD class=column align="right" width="15%">IP��ַ:</TD>
					<TD width="35%"><s:property value="deviceInfoMap.loopback_ip"/></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">�ն����к�:</TD>
					<TD width="35%"><s:property value="deviceInfoMap.device_serialnumber"/></TD>
					<TD class=column align="right" width="15%">mac��ַ:</TD>
					<TD width="35%"><s:property value="deviceInfoMap.macaddress"/></TD>
				</TR>
			</TABLE>
		</td>
	</tr>
	<TR>
		<TD HEIGHT=5>&nbsp;</TD>
	</TR>
	<TR >
		<TD>
			<table width="100%" height="30" border="0" cellspacing="0"
				cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite" >
						<a href="javascript:EC('bussessInfo');" stytle="CURSOR:hand;">
						��VOIP��Ϣ��
						<IMG name="imgbussess" SRC="../../images/down_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="">
						</a>
					</td>
				</tr>
			</table>
		</TD>
	</TR>

	<tr id=bussessInfo STYLE="display:none">
		<TD align="center">
			<div id="divAllInfo"></div>
		</TD>
	</tr>
</TABLE>
</FORM>
</body>
</html>
