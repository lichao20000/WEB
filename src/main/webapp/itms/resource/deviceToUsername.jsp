<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
			src="../../Js/My97DatePicker/WdatePicker.js"></script>

<script language="JavaScript">

function deviceResult(returnVal){

	//for(var i=0;i<returnVal[2].length;i++){
	//	$("td[@id='tdDeviceId']").append("<br>"+returnVal[2][i][0]);
	//	$("td[@id='tdDeviceOui']").append("<br>"+returnVal[2][i][1]);
	//	$("td[@id='tdDeviceSn']").append("<br>"+returnVal[2][i][2]);
	//	$("td[@id='tdDeviceLoopbackIp']").append("<br>"+returnVal[2][i][3]);
	//	$("td[@id='tdDeviceCityId']").append("<br>"+returnVal[2][i][4]);
	//	$("td[@id='tdDeviceCityName']").append("<br>"+returnVal[2][i][5]);
	//}
	document.all("txtdeviceId").value=returnVal[2][0][0];
	document.all("txtdeviceSn").value=returnVal[2][0][2];
	document.all("txtoui").value=returnVal[2][0][1];
	$("div[@id='selectedDev']").html("<strong>待操作设备序列号:"+returnVal[2][0][2]+"</strong>");
	
	this.tr1.style.display="";
	document.all("deviceResult").style.display="none";
	document.all("btnDevRes").value="展开查询";
	
	}

function doQuery(){
	var url="<s:url value='/itms/resource/deviceToUsername.action?deviceId='/>";
	var deviceId = $.trim($("input[@name='txtdeviceId']").val());
	var deviceSn = $.trim($("input[@name='txtdeviceSn']").val());
	var oui = $.trim($("input[@name='txtoui']").val());
	var starttime = $.trim($("input[@name='starttime']").val());
    //var endtime = $.trim($("input[@name='endtime']").val());
    if(starttime==""){
    	alert("请输入查询时间！");
    	return;
    }
    //if(endtime==""){
    //	alert("请输入结束时间！");
    //	return;
    //}
    //if(starttime.substring(0,7)!=endtime.substring(0,7)){
    //	alert("开始时间和结束时间要在同一个月内！");
    //	return;
    //}
	document.all("myiframe1").src = url+deviceId+"&deviceSn="+deviceSn+"&oui="+oui+"&starttime="+starttime+"&refresh="+(new Date()).getTime();
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

//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["myiframe1"]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block"
     			//如果用户的浏览器是NetScape
     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
      			//如果用户的浏览器是IE
     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
   			 }
   		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
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
</script>

<br>
<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0"
				cellpadding="0" class="green_gargtd">
				<tr>
					<td width="164" align="center" class="title_bigwhite">
						设备上报账号日志查询
					</td>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12">
						通过设备查询用户账号,时间为上报时间。请选择时间。
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr bgcolor=#ffffff>
		<td>
			<table id="deviceResult" width="100%" border=0 cellspacing=0
				cellpadding=0 align="center" valign="middle" STYLE="display: ">
				<tr bgcolor=#ffffff>
					<td class=column colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr id="tr1" STYLE="display: none" bgcolor="#FFFFFF">
		<td>
			<form name="frm">
				<table border=0 cellspacing=1 cellpadding=2 width="100%" bgcolor=#999999>
					<TR>
						<TH colspan="4" align="center">
							设备上报账号日志查询
						</TH>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td width="50%" colspan="2">
							<div id="selectedDev">
								请查询设备！
							</div>
							<input type="hidden" name="txtdeviceId" value="" />
							<input type="hidden" name="txtdeviceSn" value="" />
							<input type="hidden" name="txtoui" value="" />
						</td>
						<td align="right" width="50%" colspan="2">
							<input type="button" name="btnDevRes" class=jianbian value="隐藏查询"
								onclick="txtSelectDevice()" />
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column width="15%" align="right">
							查询时间
						</td>
						<td align="left" width="35%" colspan="3">
							<input type="text" name="starttime" class='bk' readonly
								value="<s:property value="starttime" />">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" />
							
						</td>
						<!-- <td class=column align="right" width="15%">
							结束时间
						</td>
						<td align="left" width="35%">
							<input type="text" name="endtime" class='bk' readonly
								value="<s:property value="endtime" />">
							<img
								onclick="new WdatePicker(document.frm.endtime,'%Y-%M-%D',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="选择">
						</td> -->
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()">
								&nbsp;查 询&nbsp;
							</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
	<TR bgcolor=#ffffff>
		<td>
			<DIV id=qh_con0 style="DISPLAY: block">
				<iframe id=myiframe1 src="" frameborder="0" scrolling="no"
					height="100%" width="100%"
					onload=this.height=myiframe1.document.body.scrollHeight;></iframe>
			</DIV>
		</td>
	</TR>

</TABLE>

<%@ include file="/foot.jsp"%>
