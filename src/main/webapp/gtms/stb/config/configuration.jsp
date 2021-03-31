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
<SCRIPT LANGUAGE="JavaScript">
	
	var pwd = '<%=request.getParameter("pwd") %>';
	
	$(function(){
		gwShare_setGaoji();
	});
	var deviceId = "";
	
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
		var url="<s:url value='/gtms/stb/resource/gwDeviceQuery!queryDeviceList.action'/>?queryResultType=radio";
		var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=no;edge=sunken');    
		if (returnVal){
			deviceId = returnVal[0];
			$("tr[@id='trDeviceResult']").css("display","");
			$("td[@id='tdDeviceSn']").html(returnVal[1]);
			$("td[@id='tdDeviceSn']").append("-");
			$("td[@id='tdDeviceSn']").append(returnVal[2]);
			$("td[@id='tdDeviceCityName']").html(returnVal[5]);
		}
	}
	function cmpstVideoAndAspRatioCfg(){
		$('#cmpstVideoAndAspRatioCfg').addClass("hover");
		$('#config').removeClass("hover");
		if(CheckForm()){
			//(1)、将按钮置灰
			$("#cmpstVideoAndAspRatioCfg").removeAttr("onclick");
			$("li[@id='cmpstVideoAndAspRatioCfg']");
			$("tr[@id='config']").css("display","");
		    $("div[@id='config']").html("正在操作，请稍后...");
			var url = "<s:url value='/gtms/stb/config/baseConfig!cmpstVideoAndAspRatio.action'/>";
			$.post(url,{
				deviceId:deviceId
			},function(ajax){
				$("div[@id='config']").html("");
				$("div[@id='config']").append(ajax);
				//（2）、按钮恢复
				$("#cmpstVideoAndAspRatioCfg").replaceWith("<li id='cmpstVideoAndAspRatioCfg' class='out' onclick='cmpstVideoAndAspRatioCfg();'>视频输出制式和屏显模式配置</li>"); 
			});
		}
	}
	//基本配置
	function baseConfig(){
		$('#baseConfig').addClass("hover");
		$('#config').removeClass("hover");
		if(CheckForm()){
			//block();
			//(1)、将按钮置灰
			//$("#baseConfig").attr("onclick","");
			$("#baseConfig").removeAttr("onclick");
			$("li[@id='baseConfig']");
			$("tr[@id='config']").css("display","");
		    $("div[@id='config']").html("正在操作，请稍后...");
			var url = "<s:url value='/gtms/stb/config/baseConfig!getBaseConfig.action'/>";
			//alert(pwd);
			$.post(url,{
				deviceId:deviceId,
				pwd:pwd
			},function(ajax){
				$("div[@id='config']").html("");
				$("div[@id='config']").append(ajax);
				//（2）、按钮恢复
				$("#baseConfig").replaceWith("<li id='baseConfig' class='out' onclick='baseConfig();'>读取基本配置</li>"); 
				//$.unblockUI();
			});
		}
	}
	
	//历史配置信息
	function historyConfig(){
		$('#historyConfig').addClass("hover");
		$('#config').removeClass("hover");
		if(CheckForm()){
			//block();
			//(1)、将按钮置灰
			//$("#historyConfig").attr("onclick","");
			$("#historyConfig").removeAttr("onclick");
			$("li[@id='historyConfig']");
			$("tr[@id='config']").css("display","");
		    $("div[@id='config']").html("正在操作，请稍后...");
			var url = "<s:url value='/gtms/stb/config/baseConfig!getHistoryConfig.action'/>";
			//alert(pwd);
			$.post(url,{
				deviceId:deviceId,
				pwd:pwd
			},function(ajax){
				$("div[@id='config']").html("");
				$("div[@id='config']").append(ajax);
				//（2）、按钮恢复
				$("#historyConfig").replaceWith("<li id='historyConfig' class='out' onclick='historyConfig();'>历史配置信息</li>"); 
				//$.unblockUI();
			});
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
</SCRIPT>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			您当前的位置：设备诊断
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
		<td nowrap class="title_2" width="15%">
		</td>
		<td width="35%">
		</td>
	</tr>
	<tr id="configtr" style="display: none">
		<td colspan="4">
			<div class="tab_Menubox" class="center" style="width: 98%">
				<ul>
					<li id="baseConfig" class="out" onclick="baseConfig();">
						读取基本配置
					</li>
					<!--  <li class="out" id="config" onclick="config();">
						扩展配置
					</li>-->
					<li id="cmpstVideoAndAspRatioCfg" class="out" onclick="cmpstVideoAndAspRatioCfg();">
						视频输出制式和屏显模式配置
					</li>
					<li id="historyConfig" class="out" onclick="historyConfig();">
						历史配置信息
					</li>
				</ul>
			</div>
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

