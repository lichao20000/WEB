<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.linkage.litms.system.dbimpl.*"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>���������ʽ</title>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value='/css/css_blue.css'/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<%@ include file="../../toolbar.jsp"%>
<%
	String gwType = request.getParameter("gw_type");
	LogItem.getInstance().writeItemLog(request, 1, "", "���������ʽ","in");
	String sysName = LipossGlobals.getLipossProperty("InstArea.ShortName");
 %>
<SCRIPT LANGUAGE="JavaScript">
var gw_type = "<%= gwType%>";
		var deviceId="";
		var cityId="";
		var deviceserialnumber = "";
		var oui = "";
		var pvc = "";
		var vlan = "";
		var accessType = "";
		var user_id = "";
		var path = "";
	$(function(){
		gwShare_setGaoji();
	});

function CheckForm(){
		if(deviceId == ""){
			alert("���Ȳ�ѯ�豸!");
			return false;
		}
		return true;
	}
	
	function deviceResult(returnVal){
		deviceId=returnVal[2][0][0];
		document.all("txtdeviceId").value=returnVal[2][0][0];
		document.all("txtdeviceSn").value=returnVal[2][0][2];
		document.all("txtoui").value=returnVal[2][0][1];
		deviceserialnumber = returnVal[2][0][2];  // add by zhangchy 2011-08-23 ���ں�̨ҵ���·�
		oui = returnVal[2][0][1]; // add by zhangchy 2011-08-23 ���ں�̨ҵ���·�
		$("div[@id='selectedDev']").html("<strong>�������豸���к�:"+returnVal[2][0][2]+"</strong>");
		$("#tr_info").css("display","");
		getDevNetType(deviceId);
		 
	}
	
	function getDevNetType(deviceId){
		var url = "<s:url value='/itms/config/changeConnectionType!getDevNetType.action'/>";
		$.post(url,{
              deviceId:deviceId,
              gw_type:gw_type
           },function(ajax){
        	   var result = ajax.split(";");
        	   $("#tr_info").css("display","none");
        	   if(result[0] == "-1"){
        		   alert(result[1]);
        		   $("#tr1").css("display","");
        		   document.all("changeConnType").style.display="";
        	   	   document.all("checkBindResult").style.display="";
        	   	   $("div[@id='checkBindDIV']").html("<strong>"+result[1]+"</strong>");
        	   }else{
        		   if(result[0] == '1'){
        			   //1;route;InternetGatewayDevice.WANDevice.1.WANConnectionDevice.2.WANPPPConnection.1.ConnectionType
        			   var type = result[1];
        			   if(type == "bridge"){
        				 $("select[@name='connType']").html("");
                      	 $("select[@name='connType']").html("<option value='2'>�ŽӸ�·��</option>");
                      	 $("#line1").css("display","");
        			   }else{
        				   $("select[@name='connType']").html("");              
                           $("select[@name='connType']").html("<option value='1'>·�ɸ��Ž�</option>");
                           $("#line1").css("display","none");
        			   }
        			    path = result[2];
        			    $("#tr1").css("display","");
        				document.all("deviceResult").style.display="none";
        				document.all("btnDevRes").value="չ����ѯ";
        				document.all("routeAccount").value="";
        				document.all("routePasswd").value="";
        				document.getElementById("tr002").style.display = "none";
        				$('#config').attr("disabled",false);
        		   }
        	   }
            });
	}
	
	 //���options����
    function clearOptions(colls){
        var length = colls.length;
        for(var i=length-1;i>=0;i--){
               colls.remove(i);
           }
       }
	 
	 function showAccessType(accessType,accessTypeName)
	 {
		 accessType = accessType;
		 $("td[@id='accessType']").html(accessTypeName);
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
	 
/* 	function chgConnType() {
		obj = document.all;
		if (obj.connType.value == "1") {
			line1.style.display = "none";
		} else {
			line1.style.display = "";
		}
	} */
	 
	
	function ExecMod(){

		var flag = "false";
		var bindPort = "";
		if(deviceId == ""){
			alert("���Ȳ�ѯ�豸!");
			return false;
		}
		connType = document.all("connType").value;
		if(connType == "-1"){
		   alert("��ѡ��������ʽ");
		   return;
		}
		routeAccount = document.all("routeAccount").value;
		routePasswd = document.all("routePasswd").value;
		
		if(connType=="2"){
			if(routeAccount==""){
				alert("������·���˺�");
				document.frm.routeAccount.focus();
				return false;
			}
			if(routePasswd==""){
				alert("������·������");
				document.frm.routePasswd.focus();
				return false;
			}
		}
		$('#config').attr("disabled",true);
       var url = "<s:url value='/itms/config/changeConnectionType!changeConnectionType.action'/>";
    	$.post(url,{
              deviceId:deviceId,
              connType:connType,
              routeAccount:routeAccount,
              routePasswd:routePasswd,
              path:path,
              gw_type:gw_type
           },function(ajax){
	           	 alert(ajax);
            });
		document.getElementById("tr002").style.display = "";
				
    }
</SCRIPT>

</head>

<body>

<TABLE border=0 align="center" cellspacing=0 cellpadding=0 width="98%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<table width="100%" height="30" border="0" cellpadding="0"
			cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">���������ʽ</td>
				<td class="title_bigwhite"><img
					src="<s:url value="/images/attention_2.gif"/>" width="15"
					height="12" /></td>
			</tr>
		</table>
		</TD>
	</TR>
	<TR bgcolor="#FFFFFF">
		<td colspan="4">
		<table id="deviceResult" width="100%" border=0 cellspacing=0
			cellpadding=0 align="center" valign="middle" STYLE="display: ">
			<tr bgcolor=#ffffff>
				<td class=column colspan="4"><%@ include
					file="/gwms/share/gwShareDeviceQuery.jsp"%>
				</td>
			</tr>
		</table>
		</td>
	</TR>
	<tr id="tr_info" style="display: none"><td>�ɼ������У����Ե�.......<td></tr>
	<TR id=tr1 style="display: none">
		<td>
		<form name="frm">
		<table id="checkBindResult" border=0 cellspacing=1 cellpadding=2
			width="100%" bgcolor=#999999 style="display: ">
			<TR>
				<TH colspan="4" align="center">���������ʽ</TH>
			</TR>
			<tr bgcolor="#FFFFFF">
				<td width="50%" colspan="4">
				<div id="selectedDev">���ѯ�豸��</div>
				<input type="hidden" name="txtdeviceId" value="" /> <input
					type="hidden" name="txtdeviceSn" value="" /> <input type="hidden"
					name="txtoui" value="" /></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="50%" colspan="2">
				<div id="checkBindDIV"></div>
				</td>
				<td align="right" width="50%" colspan="2"><input type="button"
					name="btnDevRes" class=jianbian value="���ز�ѯ"
					onclick="txtSelectDevice()" /></td>
			</tr>
		</table>
		<table id="changeConnType" border=0 cellspacing=1 cellpadding=2
			width="100%" bgcolor=#999999 style="display: ">
			<TR class="ah_user_info">
				<TH colspan="4" align="center">�û�������Ϣ</TH>
			</TR>
			<tr bgcolor=#ffffff class="ah_user_info">
				<td class=column width="15%" align="right">������ʽ</td>
				<td align="left" width="35%"><select name="connType" class="bk"
					onChange="chgConnType()">
				</select></td>
				<%if(!"ah_dx".equals(sysName)){ %>
				<td nowrap class=column align="right">���з�ʽ��</td>
				<td id="accessType" nowrap></td>
				<%}else{%>
					<td nowrap class=column align="right"> </td>
				<td nowrap class=column></td>
				<%} %>
				 
			</tr>
			<tr bgcolor="#FFFFFF" id="line1" style="display: ">
				<td nowrap class=column align="right">�û���</td>
				<td nowrap><input type="text" name="routeAccount" class="bk"
					value=""> &nbsp;&nbsp; <font color=red>*</font></td>
				<td nowrap class=column align="right">����</td>
				<td><input type="text" name="routePasswd" class="bk" value="">
				&nbsp;&nbsp; <font color=red>*</font></td>
			</tr>
			<tr bgcolor=#ffffff>
				<td class=foot colspan=4 align=right>
				<button id="config" onclick="ExecMod()">&nbsp;�� ��&nbsp;</button>
				</td>
			</tr>
		</table>
		</form>
		</td>
	</TR>
	<TR bgcolor="#FFFFFF" id="tr002" style="display: none">
		<td valign="top" class=column>
		<div id="div_strategy" style="width: 100%; z-index: 1; top: 100px">
		</div>
		</td>
	</TR>
</TABLE>
</body>
</html>
<%@ include file="../../foot.jsp"%>