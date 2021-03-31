<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.linkage.litms.system.dbimpl.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>NAT</title>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value='/css/css_blue.css'/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<%
	String gwType = request.getParameter("gw_type");
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
	$(function(){
		gwShare_setGaoji();
	});
/* 	function addWanHtml() {
      
	//	var url = "<s:url value='/gwms/config/wanACT!addLanInit.action'/>";
	 var url = "<s:url value='/itms/config/changeConnectionType!addLanInit.action'/>";
		$.post(url,{
			deviceId:deviceId,
			type:"1",
			gw_type:gw_type
		},function(ajax){
			$("td[@id='lanInter']").html(ajax);
			
		});
	} */
	function reInter(){
		$("td[@id='lanInter']").html("正在获取端口...");
		
		//var url = "<s:url value='/gwms/config/wanACT!addLanInit.action'/>";
       var url = "<s:url value='/itms/config/changeConnectionType!addLanInit.action'/>";
	    $.post(url,{
	    	deviceId:deviceId,
	    	type:"0"
		},function(ajax){
			$("td[@id='lanInter']").html(ajax);
			});
}

function CheckForm(){
		if(deviceId == ""){
			alert("请先查询设备!");
			return false;
		}
		return true;
	}
	
	function deviceResult(returnVal){
		
		deviceId=returnVal[2][0][0];
		$("input[@name='txtdeviceId']").val(deviceId);
		document.all("txtdeviceId").value=returnVal[2][0][0];
		document.all("txtdeviceSn").value=returnVal[2][0][2];
		document.all("txtoui").value=returnVal[2][0][1];
		deviceserialnumber = returnVal[2][0][2];  // add by zhangchy 2011-08-23 用于后台业务下发
		oui = returnVal[2][0][1]; // add by zhangchy 2011-08-23 用于后台业务下发
		$("div[@id='selectedDev']").html("<strong>待操作设备序列号:"+returnVal[2][0][2]+"</strong>");
		checkBindAndService(deviceId); 
		this.tr1.style.display="";
		document.all("deviceResult").style.display="none";
		document.all("btnDevRes").value="展开查询";
		document.getElementById("tr002").style.display = "none";
		$('#config').attr("disabled",false);
		//addWanHtml();
	}
	function checkBindAndService(deviceId)
	{
		$("#cue").css("display","");
		$("#status").css("display","none");
		$("#open").css("display","none");
		$("#close").css("display","none");
		$("#open").attr("disabled","");
 		$("#close").attr("disabled","");
		var gw_type = "<%= gwType%>";
		var url = "<s:url value='/itms/config/NatStatusQueryACT!gatherWanPath.action'/>";
		$.post(url,{
              deviceId:deviceId,
              gw_type:gw_type
           },function(ajax){
        	   if(ajax!=null&&ajax!="")
        		  {
        		   var lineData = ajax.split("#");
        		   for(var i=0;i<lineData.length;i++){
        			   var xValue = lineData[0];
        			   var xText ="";
        			   if(xValue=="0")
        				  {
        				   $("#cue").css("display","none");
        				   $("#status").css("display","");
        				   $("select[@name='natstatus']").html("<option value='1'>关闭</option>");
                           $("#open").css("display","");
                           $("#close").css("display","none");
        				  }else if(xValue=="1")
          				{
        				   $("#cue").css("display","none");
        				   $("#status").css("display","");
           				   $("select[@name='natstatus']").html("<option value='0'>开启</option>");
           				   $("#open").css("display","none");
                           $("#close").css("display","");
           				}
        		   }
        		  }else
        			  {
        			  
        			  $("#cue").css("display","none");
   				  	  $("#status").css("display","none");
   				   	  $("#open").css("display","none");
   				   	  $("#close").css("display","none");
   					  $("#open").attr("disabled","true");
   					alert("设备不在线或正被操作!");
        			  }
            });
	}
	 //清空options集合
    function clearOptions(colls){
        var length = colls.length;
        for(var i=length-1;i>=0;i--){
               colls.remove(i);
           }
       }
	 
	 function showAccessType(accessType,accessTypeName)
	 {
		 //alert(accessType + "++++" + accessTypeName);
		 accessType = accessType;
		 $("td[@id='accessType']").html(accessTypeName);
		 //user_id = "<s:property value="user_id" />"
	 }

function operate()
{
	$("#open").attr("disabled","true");
	$("#close").attr("disabled","true");
	var natstatus=$("select[@name='natstatus']").val();
	var deviceId=$("input[@name='txtdeviceId']").val();
	var gw_type = "<%= gwType%>";
	var url = "<s:url value='/itms/config/NatStatusQueryACT!open.action'/>";
	$.post(url,{
          deviceId:deviceId,
          natstatus:natstatus,
          gw_type:gw_type
       },function(ajax){
    	  alert(ajax);
        });
}
	function txtSelectDevice()
	{		
		if("none"==document.all("deviceResult").style.display){
			document.all("deviceResult").style.display="";
			document.all("btnDevRes").value="隐藏查询";
		}else{
			document.all("deviceResult").style.display="none";
			document.all("btnDevRes").value="展开查询";
		}
	}
	/**
	function chgConnType() {
		obj = document.all;
		if (obj.connType.value == "1") {
			line1.style.display = "none";
		} else {
			line1.style.display = "";
		}
	}
	**/
	
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
				<td width="162" align="center" class="title_bigwhite">NAT</td>
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
	<TR id=tr1 style="display: none">
		<td>
		<form name="frm">
		<table id="checkBindResult" border=0 cellspacing=1 cellpadding=2
			width="100%" bgcolor=#999999 style="display: ">
			<TR>
				<TH colspan="4" align="center">关闭NAT</TH>
			</TR>
			<tr bgcolor="#FFFFFF">
				<td width="50%" colspan="4">
				<div id="selectedDev">请查询设备！</div>
				<input type="hidden" name="txtdeviceId" id="txtdeviceId" value="" /> <input
					type="hidden" name="txtdeviceSn" value="" /> <input type="hidden"
					name="txtoui" value="" /></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="50%" colspan="2">
				<div id="checkBindDIV"></div>
				</td>
				<td align="right" width="50%" colspan="2"><input type="button"
					name="btnDevRes" class=jianbian value="隐藏查询"
					onclick="txtSelectDevice()" /></td>
			</tr>
		</table>
		<table id="changeConnType" border=0 cellspacing=1 cellpadding=2
			width="100%" bgcolor=#999999 style="display: ">
			<TR>
				<TH colspan="4" align="center">用户上网信息</TH>
			</TR>
			<tr bgcolor=#ffffff style="display: none;" name="status" id="status">
				<td class=column width="15%" align="right">NAT状态</td>
				<td align="left" width="35%"><select name="natstatus" class="bk"></select></td>
			</tr>
			<tr bgcolor=#ffffff   name="cue" id="cue">
				<td class=column width="15%" align="center" colspan="4" >正在采集中，请稍后。。。。</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td  colspan=2 align=right name="open" id="open" style="display: none;" >
				<button id="config" onclick="operate()">&nbsp;开启&nbsp;</button>
				</td>
				<td  colspan=2 align=right name="close" id="close" style="display: none" >
				<button id="config" onclick="operate()">&nbsp;关闭&nbsp;</button>
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