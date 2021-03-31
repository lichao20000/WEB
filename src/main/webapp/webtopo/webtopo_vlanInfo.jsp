<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸vlan��Ϣ����</title>
<%
	/**
		 * �豸vlan��Ϣ����
		 * 
		 * @author ������(5243)
		 * @version 1.0
		 * @since 2008-06-05
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
typeArr[typeArr.length] = "351";
typeArr[typeArr.length] = "352";
typeArr[typeArr.length] = "353";
//��������
var dataArr = new Array();

var count = 0;
var device_id = "";
//�ɼ���ʽĬ����snmp��ʽ 0:snmp  1:tr069
var gatherType=0;

//��ʼ��
$(function(){
	$("#vlanInfo").html("<table border=0 cellspacing=1 cellpadding=2 width='100%' bgcolor='#000000'><tr bgcolor='#FFFFFF'><td>���ڼ���......</td></tr></table>");
	device_id = '<s:property value="device_id" />';
	
	initData();
})

//���Ĳɼ���ʽ
function changeGtType()
{
	count = 0;
	gatherType=$("select[@name='gatherType']").val();
	$("#vlanInfo").html("<table border=0 cellspacing=1 cellpadding=2 width='100%' bgcolor='#000000'><tr bgcolor='#FFFFFF'><td>���ڼ���......</td></tr></table>");
	$("#vlanCount").html("���õ�vlan������0");
	
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
						//�˿���Ϣ��������
						getVlanPort();
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
						//�˿���Ϣ��������
						getVlanPort();
					}
				}
		});
	}
}

//���ض˿���Ϣ
function getVlanPort(){
	$.ajax({
			type: "POST", 
			url: "<s:url value="/bbms/GetSnmpInfo!getVlanPort.action"/>", 
			data: "device_id="+device_id,
			success:
				function(data)
				{
					dataArr[dataArr.length] = data;
					//SNMP�ɼ�MAC��ַ
					getMacInfo_snmp();
				},
			erro:
				function(xmlR,msg,other)
				{
					dataArr[dataArr.length] = "";
					//SNMP�ɼ�MAC��ַ
					getMacInfo_snmp();
				}
		});
}

//SNMP�ɼ�MAC��ַ
function getMacInfo_snmp(){
	$.ajax({
			type: "POST", 
			url: "<s:url value="/bbms/GetSnmpInfo!getMacInfoSnmp.action"/>", 
			data: "device_id="+device_id,
			success:
				function(data)
				{
					dataArr[dataArr.length] = data;
					//�ɼ�vlan����
					getVlanNum();
				},
			erro:
				function(xmlR,msg,other)
				{
					dataArr[dataArr.length] = "";
					//�ɼ�vlan����
					getVlanNum();
				}
		});
}

//�����ݿ��ѯMAC��ַ
function getMacInfo(){
	$.ajax({
			type: "POST", 
			url: "<s:url value="/bbms/GetSnmpInfo!getMacInfo.action"/>", 
			data: "device_id="+device_id,
			success:
				function(data)
				{
					dataArr[dataArr.length] = data;
					//ȫ��������ɺ��������
					showData();
				},
			erro:
				function(xmlR,msg,other)
				{
					dataArr[dataArr.length] = "";
					//ȫ��������ɺ��������
					showData();
				}
		});
}

//tr069��ʽ��ȡ����
function initData_tr069(){
	
	var oid = "373#374#375#372";
	
	$.ajax({
			type: "POST", 
			url: "<s:url value="/bbms/GetSnmpInfo!getInfoByTr069.action"/>", 
			data: "device_id="+device_id+"&oid_type="+oid,
			success:
				function(data)
				{
					dataArr = data.split("#");
					//�����ݿ��ѯmac��ַ��Ϣ
					getMacInfo();
				},
			erro:
				function(xmlR,msg,other)
				{
					for (var j=0;j<=typeArr.length;j++){
						dataArr[j] = "";
					}
					//�����ݿ��ѯmac��ַ��Ϣ
					getMacInfo();
				}
		});
}

var vlanNum = '�ݲ�֧�ָ����Բɼ�';

function showData(){
	var vlanId = dataArr[0].split("<br>");
	var vlanIp = dataArr[1].split("<br>");
	var vlanMask = dataArr[2].split("<br>");
	var vlanPort = dataArr[3].split("<br>");
	var macInfo = dataArr[4].split("<cisco>");
	
	var len = vlanId.length;
	var dataSize = 0;
	var str = "";
	for (var i=0;i<len;i++){
		if (vlanId[i] != ""){
			str += "<table border=0 cellspacing=1 cellpadding=2 width='100%' bgcolor='#000000'>";
			
			//VLAN ID
			str += "<tr bgcolor='#FFFFFF'><td class='column' width='30%'>VLAN ID��</td><td width='70%'>" + vlanId[i] + "</td>";
			//VLAN IP
			if (vlanIp.length > i){
				str += "<tr bgcolor='#FFFFFF'><td class='column'>VLAN IP��</td><td>" + vlanIp[i] + "</td>";
			}
			else{
				str += "<tr bgcolor='#FFFFFF'><td class='column'>VLAN IP��</td><td></td>";
			}
			//VLAN ����
			if (vlanMask.length > i){
				str += "<tr bgcolor='#FFFFFF'><td class='column'>VLAN ���룺</td><td>" + vlanMask[i] + "</td>";
			}
			else{
				str += "<tr bgcolor='#FFFFFF'><td class='column'>VLAN ���룺</td><td></td>";
			}
			//�󶨶˿��б�
			if (vlanPort.length > i){
				str += "<tr bgcolor='#FFFFFF'><td class='column'>�󶨶˿��б�</td><td>" + vlanPort[i] + "</td>";
			}
			else{
				str += "<tr bgcolor='#FFFFFF'><td class='column'>�󶨶˿��б�</td><td></td>";
			}
			//MAC��ַ
			if (dataArr[4].indexOf("<cisco>") != -1){
				str += "<tr bgcolor='#FFFFFF'><td class='column'>MAC��ַ��</td><td>" + macInfo[i] + "</td>";
			}
			else{
				str += "<tr bgcolor='#FFFFFF'><td class='column'>MAC��ַ��</td><td>" + dataArr[4] + "</td>";
			}
			str += "</table>";
			str += "<br>";
			dataSize ++;
		}
	}
	
	if (str == ""){
		str += "<table border=0 cellspacing=1 cellpadding=2 width='100%' bgcolor='#000000'><tr bgcolor='#FFFFFF'><td>�ɼ�����ʧ�ܣ������ԣ�</td></tr></table>";
	}
	
	$("#vlanInfo").html(str);
	if (vlanNum == '�ݲ�֧�ָ����Բɼ�' || vlanNum == 'û�����òɼ�oid'){
		$("#vlanCount").html("���õ�vlan������"+dataSize);
	}
	else{
		$("#vlanCount").html("���õ�vlan������"+vlanNum);
	}
}

//�ɼ�vlan����
function getVlanNum(){
	$.ajax({
			type: "POST", 
			url: "<s:url value="/bbms/GetSnmpInfo!getSnmpInfo.action"/>", 
			data: "device_id="+device_id+"&oid_type=606&type=0",
			success:
				function(data)
				{
					vlanNum = data;
					//ȫ��������ɺ��������
					showData();
				},
			erro:
				function(xmlR,msg,other)
				{
					vlanNum = 0;
					//ȫ��������ɺ��������
					showData();
				}
		});
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
				<tr><th>�豸vlan��Ϣ</th></tr>
				<tr>
					<td bgcolor='#FFFFFF'>�ɼ���ʽ:
						<select name="gatherType" onchange="changeGtType();">
							<option value="0">SNMP</option>
							<option value="1">TR069</option>
						</select>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td><div id="vlanCount">���õ�vlan������0</div></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<br>
<div id="vlanInfo"></div>
</form>
</body>
</html>