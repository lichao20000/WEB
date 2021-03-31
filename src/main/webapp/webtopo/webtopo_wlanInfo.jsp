<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<%
String device_id = request.getParameter("device_id");

%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸wlan��Ϣ����</title>
<%
	/**
		 * �豸wlan��Ϣ����
		 * 
		 * @author ������(5243)
		 * @version 1.0
		 * @since 2008-06-24
		 * @category
		 */
%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">
//��������
var typeArr = new Array();
typeArr[typeArr.length] = "601";
typeArr[typeArr.length] = "602";
typeArr[typeArr.length] = "603";
typeArr[typeArr.length] = "604";
typeArr[typeArr.length] = "605";
//��������
var dataArr = new Array();

var count = 0;
var device_id = "";
//�ɼ���ʽĬ����snmp��ʽ 0:snmp  1:tr069
var gatherType=0;

//��ʼ��
$(function(){
	$("#wlanInfo").html("<table border=0 cellspacing=1 cellpadding=2 width='100%' bgcolor='#000000'><tr bgcolor='#FFFFFF'><td>���ڼ���......</td></tr></table>");
	device_id = '<%=device_id%>';
	
	initData();
})

//���Ĳɼ���ʽ
function changeGtType()
{
	count = 0;
	gatherType=$("select[@name='gatherType']").val();
	$("#wlanInfo").html("<table border=0 cellspacing=1 cellpadding=2 width='100%' bgcolor='#000000'><tr bgcolor='#FFFFFF'><td>���ڼ���......</td></tr></table>");
	
	if (gatherType == '0'){
		initData();
	}
	else{
		initData_tr069();
	}
}

//��������
function initData(){
	if (count < typeArr.length){
		$.ajax({
			type: "POST", 
			url: "<s:url value="/bbms/GetSnmpInfo!getSnmpInfo.action"/>", 
			data: "device_id="+device_id+"&oid_type="+typeArr[count]+"&type=0",
			success:
				function(data)
				{
					dataArr[dataArr.length] = data;
					//������һ������
					count++;
					if(count < typeArr.length){
						initData();
					}
					else{
						//��ʾ����
						showData();
					}
				},
			erro:
				function(xmlR,msg,other){
					dataArr[dataArr.length] = "";
					//������һ������
					count++;
					if(count < typeArr.length){
						initData();
					}
					else{
						//��ʾ����
						showData();
					}
				}
		});
	}
}

//tr069��ʽ��ȡ����
function initData_tr069(){
	
	var oid = "606#607#608#609#610";
	
	$.ajax({
			type: "POST", 
			url: "<s:url value="/bbms/GetSnmpInfo!getInfoByTr069.action"/>", 
			data: "device_id="+device_id+"&oid_type="+oid,
			success:
				function(data)
				{
					dataArr = data.split("#");
					//��ʾ����
					showData();
				},
			erro:
				function(xmlR,msg,other)
				{
					for (var j=0;j<=typeArr.length;j++){
						dataArr[j] = "";
					}
					//��ʾ����
					showData();
				}
		});
}

//״̬����
var statusArr = new Array();
statusArr[0] = "disable";
statusArr[1] = "enable";
//ģʽ����
var modelArr = new Array();
modelArr[0] = "disable";
modelArr[1] = "802.11 B/G mixed";
modelArr[2] = "802.11 B only";
modelArr[3] = "802.11 G only";
modelArr[4] = "802.11 A only";
modelArr[5] = "802.11 N only";
modelArr[6] = "802.11 G/N mixed";
modelArr[7] = "802.11 A/N mixed";
modelArr[8] = "802.11 B/G/N mixed";
//�㲥ģʽ����
var broadArr = new Array();
broadArr[0] = "Broadcast";
broadArr[1] = "nonBroadcast";

function showData(){
	var walnStatus = dataArr[0].substr(0,1);
	var wlanmodel = dataArr[1].substr(0,1);
	var ssidIndex = dataArr[2].split("<br>");
	var ssidName = dataArr[3].split("<br>");
	var ssidBroad = dataArr[4].split("<br>");
	
	var str = "<table border=0 cellspacing=1 cellpadding=2 width='100%' bgcolor='#000000'>";
			
	if (typeof(statusArr[walnStatus]) == 'undefined'){
		str += "<tr bgcolor='#FFFFFF'><td class='column' width='30%'>����ʹ��״̬</td><td width='70%'>�޷��ɼ�������</td></tr>";
	}
	else{
		str += "<tr bgcolor='#FFFFFF'><td class='column' width='30%'>����ʹ��״̬</td><td width='70%'>" + statusArr[walnStatus] + "</td></tr>";
	}
	if (typeof(modelArr[wlanmodel]) == 'undefined'){
		str += "<tr bgcolor='#FFFFFF'><td class='column' width='30%'>��������ģʽ</td><td width='70%'>�޷��ɼ�������</td></tr>";
	}
	else{
		str += "<tr bgcolor='#FFFFFF'><td class='column' width='30%'>��������ģʽ</td><td width='70%'>" + modelArr[wlanmodel] + "</td></tr>";
	}
	str += "</table><br>";
	
	var len = ssidIndex.length;
	for (var i=0;i<len;i++){
		if (ssidIndex[i] != ""){
			str += "<table border=0 cellspacing=1 cellpadding=2 width='100%' bgcolor='#000000'>";
			
			//SSID index
			str += "<tr bgcolor='#FFFFFF'><td class='column' width='30%'>SSID index��</td><td width='70%'>" + ssidIndex[i] + "</td>";
			//SSID���Q
			if (ssidName.length > i){
				str += "<tr bgcolor='#FFFFFF'><td class='column'>SSID���ƣ�</td><td>" + ssidName[i] + "</td>";
			}
			else{
				str += "<tr bgcolor='#FFFFFF'><td class='column'>SSID���ƣ�</td><td></td>";
			}
			//SSID�V��
			if (ssidBroad.length > i){
				if (typeof(broadArr[ssidBroad[i].substr(0,1)]) == 'undefined'){
					str += "<tr bgcolor='#FFFFFF'><td class='column'>SSID�㲥��</td><td>�޷��ɼ�������</td>";
				}
				else{
					str += "<tr bgcolor='#FFFFFF'><td class='column'>SSID�㲥��</td><td>" + broadArr[ssidBroad[i].substr(0,1)] + "</td>";
				}
			}
			else{
				str += "<tr bgcolor='#FFFFFF'><td class='column'>SSID�㲥��</td><td></td>";
			}
			str += "</table>";
			str += "<br>";
		}
	}
	
	if (str == ""){
		str += "<table border=0 cellspacing=1 cellpadding=2 width='100%' bgcolor='#000000'><tr bgcolor='#FFFFFF'><td>�ɼ�����ʧ�ܣ������ԣ�</td></tr></table>";
	}
	
	$("#wlanInfo").html(str);
}
</script>
</head>

<body>
<form action="">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr>
		<td height="20">&nbsp;</td>
	</tr>
	<tr>
		<td>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
				bgcolor="#000000">
				<tr><th>�豸wlan��Ϣ</th></tr>
				<tr>
					<td bgcolor='#FFFFFF'>�ɼ���ʽ:
						<select name="gatherType" onchange="changeGtType();">
							<option value="0">SNMP</option>
							<option value="1">TR069</option>
						</select>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<br>
<div id="wlanInfo"></div>
</form>
</body>
</html>