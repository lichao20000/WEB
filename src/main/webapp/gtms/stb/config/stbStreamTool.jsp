<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/tab.css"/>" rel="stylesheet"
	type="text/css">
<lk:res />

<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
	
	$(function(){
		gwShare_setGaoji();
	});
	var deviceId = "";
	//function block(){   
	//$.blockUI({
	//	overlayCSS:{ 
	//        backgroundColor:'#CCCCCC', 
	//        opacity:0.6 
    //	},
	//	message:"<font size=3>正在操作，请稍后...</font>"
	//});      
	//}
	
	//function unblock(){
	//	$.unblockUI();
	//} 
	
	function deviceResult(returnVal){
		for(var i=0;i<returnVal[2].length;i++){
			//$("input[@name='deviceId']").val(returnVal[0]);
			deviceId = returnVal[2][i][0];
			$("tr[@id='trDeviceResult']").css("display","");
			$("td[@id='tdDeviceSn']").html(returnVal[2][i][1]+"-"+returnVal[2][i][2]);
			//$("td[@id='tdDeviceSn']").append("-");
			//$("td[@id='tdDeviceSn']").append();
			$("td[@id='tdDeviceCityName']").html(returnVal[2][i][5]);
			$("td[@id='tdDeviceIP']").html(returnVal[2][i][3]);
			$("tr[@id='configtr']").show();
		}
	}
	
	/*------------------------------------------------------------------------------
	//函数名:		queryChange
	//参数  :	change 1:简单查询、2:高级查询
	//功能  :	根据传入的参数调整显示的界面
	//返回值:		调整界面
	//说明  :	
	//描述  :	Create 2009-12-25 of By qxq
	------------------------------------------------------------------------------*/
	function queryDevice(){
		var	width=800;    
		var height=500;    
		var url="<s:url value="/gtms/stb/resource/gwDeviceQueryStb!queryDeviceList.action"/>?queryResultType=radio";
		var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=no;edge=sunken');    
		if(typeof(returnVal)=='undefined'){
			return;
		}else{
			deviceId = returnVal[0];
			$("tr[@id='trDeviceResult']").css("display","");
			$("td[@id='tdDeviceSn']").html(returnVal[1]);
			$("td[@id='tdDeviceSn']").append("-");
			$("td[@id='tdDeviceSn']").append(returnVal[2]);
			$("td[@id='tdDeviceCityName']").html(returnVal[5]);
		}
	}
	
	function config(){
		$('#baseConfig').removeClass("hover");
		$('#config').addClass("hover");
	}
	function CheckForm(){
		if(deviceId==""){
			alert("请查询设备！");
			return false;
	    }
		return true;					
	}
	//码流开启
	function openStream(){
		if(CheckForm()){
			var diagUrl = '<s:url value="/gtms/stb/config/streamTool!openStream.action"/>';
			$.post(diagUrl,{
				deviceId:deviceId
			},function(ajaxMesg){
				alert(ajaxMesg);
			});
		}
	}

	//码流关闭
	function closeStream(){
		if(CheckForm()){
			var diagUrl = '<s:url value="/gtms/stb/config/streamTool!closeStream.action"/>';
			$.post(diagUrl,{
				deviceId:deviceId
			},function(ajaxMesg){
				alert(ajaxMesg);
			});
			}
	}

	//清除交互内容
	function clearStream(){
		if(CheckForm()){
			var diagUrl = '<s:url value="/gtms/stb/config/streamTool!clearStream.action"/>';
			$.post(diagUrl,{
				deviceId:deviceId
			},function(ajaxMesg){
				alert(ajaxMesg);
			});
		}
	}

	//查看交互内容
	function showInterStream(){
		if(CheckForm()){
			var page = '<s:url value="/gtms/stb/config/streamTool!showInterStream.action"/>';
			page += "?deviceId=" + deviceId;
			var height = 600;
			var width = 800;
			var left = (screen.width-width)/2;
			var top  = (screen.height-height)/2;
			var w = window.open(page,"ss","left="+left+",top="+top+",width="+width+",height="+height+",resizable=yes,scrollbars=yes,toolbar=no");
		}
	}
</SCRIPT>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			您当前的位置：机顶盒码流工具
		</TD>
	</TR>
</TABLE>
<table width="98%" class="querytable" align="center">
	<TR>
		<td colspan="4">
			<%@ include file="../share/gwShareDeviceQuery.jsp"%>
		</td>
	</TR>
	<TR id="trDeviceResult" style="display: none">
		<td nowrap class="title_2" width="15%">
			设备序列号
		</td>
		<td id="tdDeviceSn" width="35%">
		</td>
		<td nowrap class="title_2" width="15%">
			属地
		</td>
		<td id="tdDeviceCityName" width="35%">
		</td>
	</tr>
	<tr id="trDeviceResult" style="display: none">
		<td nowrap class="title_2" width="15%">
			设备IP
		</td>
		<td id="tdDeviceIP" width="35%">
		</td>
		<td   nowrap class="title_2" width="15%">
		
		</td>
		<td width="35%">
				
		</td>
	</tr>
	<tr id="configtr" style="display: none">
		<td colspan="4">
				<button id="ping_btn" onclick="showInterStream()"> 查看机顶盒交互内容</button>
				<button id="ping_btn" onclick="clearStream()"> 清除记录</button>
				<button id="ping_btn" onclick="openStream()"> 开启 </button>
				<button id="ping_btn" onclick="closeStream()"> 关闭 </button>
		</td>
	</tr>
	<tr id="config" style="display: none">
		<td colspan="4">
			<div id="config" style="width: 100%; z-index: 1; top: 100px">
				正在查询，请稍等....
			</div>
		</td>
	</tr>
</table>

