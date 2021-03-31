<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%--
/**
 * 【北京酒店网管模板配置】使用模板页面;
 * REQ:XXXX-XXXX-XXXX
 * BUG:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2009-1-15 下午02:55:32
 *
 * 版权：南京联创科技 网管科技部;
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>使用模板配置</title>
<lk:res/>
<script type="text/javascript">
	$(function(){

	});

	//显示隐藏TAB
	function showHide(isralate,attrvalue,url,configtype){
		$("#tr_title td").attr("class","mouseout");
		$("#tr_title td").mouseout(function(){
			$(this).attr("class","mouseout");
		});
		$("#tab_"+configtype).attr("class","mouseon");
		$("#tab_"+configtype).mouseout(function(){
			$(this).attr("class","mouseon");
		});
		var param="&vendor_id=<s:property value="vendor_id"/>&serial=<s:property value="serial"/>&t="+new Date();
		if(url.split("?").length==1){
			url+="?device_id="+"<s:property value="device_id"/>"+param;
		}else{
			if(isralate=="1"){
				url+=attrvalue;
			}
			url+="&device_id="+"<s:property value="device_id"/>"+param;
		}
		parent.getData("../"+url);
	}

	//初始化
	function init(){
		var configtype="<s:property value="configtype"/>";
		if(configtype==null || configtype==""){
			alert("该设备暂时不支持模板配置功能！");
			parent.window.close();
		}
		//跳转URL
		$("#tab_"+configtype.split("-")[0]).click();
	}
	//默认总体配置
	function CheckRC(){
	defaultConfig();
	/*
		$.post(
			"<s:url value="/performance/useMoudle!verifyReadCommunity.action"/>",
			{
				device_id:"<s:property value="device_id"/>"
			},
			function(data){
				if(data=="true"){
					defaultConfig();
				}else{
					alert("该设备读口令不对或设备暂时不在线，请确认！");
				}
			}
		);
		*/
	}
	function defaultConfig(){
		//默认配置
		$.post(
			"<s:url value="/performance/useMoudle!defaultConfig.action"/>",
			{
				device_id:"<s:property value="device_id"/>",
				configtype:"<s:property value="configtype"/>",
				vendor_id:"<s:property value="vendor_id"/>",
				serial:"<s:property value="serial"/>"
			},
			function(data){
				alert(data);
			}
		);
	}
</script>
</head>
<body onload="init();">
	<form action="">
		<input type="hidden" name="configtype" value="<s:property value="configtype"/>">
		<table width="94%" align="center">
		<tr>
			<s:property value="configstr" escapeHtml="false"/>
			<td align="right">
				<button onclick="CheckRC()">默认总体配置</button>
				<button onclick="parent.window.close();">关闭窗口</button>
			</td>
		</tr>
		<tr>
			<td>备注：默认总体配置只是配置流量、性能等基本参数，不具备配置阀值告警，如需配置阀值告警，请单独配置相关选项。</td>
		</tr>
	</table>
	<table width="94%" align="center" class="querytable">
		<tr>
			<td>
				<table width="450" class="tab" align="left">
					<tr id="tr_title">
						<s:property value="tabstr" escapeHtml="false"/>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</form>
</body>
</html>
