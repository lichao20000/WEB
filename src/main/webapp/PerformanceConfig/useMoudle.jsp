<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%--
/**
 * �������Ƶ�����ģ�����á�ʹ��ģ��ҳ��;
 * REQ:XXXX-XXXX-XXXX
 * BUG:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2009-1-15 ����02:55:32
 *
 * ��Ȩ���Ͼ������Ƽ� ���ܿƼ���;
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ʹ��ģ������</title>
<lk:res/>
<script type="text/javascript">
	$(function(){

	});

	//��ʾ����TAB
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

	//��ʼ��
	function init(){
		var configtype="<s:property value="configtype"/>";
		if(configtype==null || configtype==""){
			alert("���豸��ʱ��֧��ģ�����ù��ܣ�");
			parent.window.close();
		}
		//��תURL
		$("#tab_"+configtype.split("-")[0]).click();
	}
	//Ĭ����������
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
					alert("���豸������Ի��豸��ʱ�����ߣ���ȷ�ϣ�");
				}
			}
		);
		*/
	}
	function defaultConfig(){
		//Ĭ������
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
				<button onclick="CheckRC()">Ĭ����������</button>
				<button onclick="parent.window.close();">�رմ���</button>
			</td>
		</tr>
		<tr>
			<td>��ע��Ĭ����������ֻ���������������ܵȻ������������߱����÷�ֵ�澯���������÷�ֵ�澯���뵥���������ѡ�</td>
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
