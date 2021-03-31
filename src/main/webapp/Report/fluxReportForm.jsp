<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������ͳ��</title>
<%
	/**
		 * ��������ͳ��
		 *
		 * @author ������(5243)
		 * @version 1.0
		 * @since 2008-06-10
		 * @category
		 */
%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="../Js/jsDate/WdatePicker.js"></script>
<script type="text/javascript">

function showChild(target){
	if (target == 'device_model'){
		$.ajax({
			type: "POST",
			url: "<s:url value="/flux/FluxReport!getDeviceModelInfo.action"/>",
			data: "vendor_id="+$("select[@name='vendor_id']").val(),
			success:
				function(data)
				{
					$("#modelSelect").html(data);
				},
			erro:
				function(xmlR,msg,other){
					$("#modelSelect").html("<select name='device_model' class='bk' onchange=showChild('device')><option value='-1'>==û�п�ѡ����豸�ͺ�==</option></select>");
				}
		});
	}
	else if (target == 'device'){
		$.ajax({
			type: "POST",
			url: "<s:url value="/flux/FluxReport!getDeviceInfo.action"/>",
			data: "vendor_id="+$("select[@name='vendor_id']").val()+"&device_model_id="+$("select[@name='device_model_id']").val(),
			success:
				function(data)
				{
					$("#deviceSelect").html(data);
				},
			erro:
				function(xmlR,msg,other){
					$("#deviceSelect").html("<select name='device_id' class='bk' onchange=showChild('port')><option value='-1'>==û�п�ѡ����豸==</option></select>");
				}
		});
	}
	else if (target == 'port'){
		$.ajax({
			type: "POST",
			url: "<s:url value="/flux/FluxReport!getDevicePortInfo.action"/>",
			data: "device_id="+$("select[@name='device_id']").val(),
			success:
				function(data)
				{
					$("#portSelect").html(data);
				},
			erro:
				function(xmlR,msg,other){
					$("#portSelect").html("û�п���ѡ��Ķ˿�(���豸û�н����������ã���û�����óɹ�)");
				}
		});
	}
}

//ȫѡ�˿�
function selectPort(){
	//���ڶ˿�
	if (document.frm.devicePort != null){
		//����˿�
		if (typeof(document.frm.devicePort.value) == 'undefined'){
			for (var i=0;i<document.frm.devicePort.length;i++){
				document.frm.devicePort[i].checked = document.frm.selectPorts.checked;
			}
		}
		//һ���˿�
		else{
			document.frm.devicePort.checked = document.frm.selectPorts.checked;
		}
	}
}
//ȫѡ����
function selectKind(){
	for (var i=0;i<document.frm.kind.length;i++){
		document.frm.kind[i].checked = document.frm.selectKinds.checked;
	}
}

function selectPortInfo(){
	if (document.frm.devicePort != null){
		//����˿�
		if (typeof(document.frm.devicePort.value) == 'undefined'){
			for (var i=0;i<document.frm.devicePort.length;i++){
				if (document.frm.devicePort[i].checked){
					//��ʾ���б���
					var optValue = document.frm.devicePort[i].value;
					var optText = document.frm.device_id.options[document.frm.device_id.options.selectedIndex].text
									+ document.frm.devicePort[i].view;
					addInfoOPtion(optValue, optText);
				}
			}
		}
		//һ���˿�
		else{
			if (document.frm.devicePort.checked){
				//��ʾ���б���
				var optValue = document.frm.devicePort[i].value;
					var optText = document.frm.device_id.options[document.frm.device_id.options.selectedIndex].text
									+ document.frm.devicePort[i].view;
				addInfoOPtion(optValue, optText);
			}
			else{
				alert('��ѡ��˿ڣ�');
			}
		}
	}
	else{
		alert('û�п���ѡ��Ķ˿ڣ�(���豸û�н����������ã���û�����óɹ�)');
	}
}

//����ѡ���б�������
function addInfoOPtion(optValue, optText){
	var oOption = document.createElement("OPTION");
	oOption.text = optText;
	oOption.value = optValue;
	for (var i=0;i<document.frm.selectInfo.length;i++){
		if (document.frm.selectInfo[i].value == optValue){
			return false;
		}
	}
	document.frm.selectInfo.options.add(oOption);
}

//ɾ����ѡ����豸�˿�
function dselectPortInfo(){
	var obj = document.frm.selectInfo;

	if (obj.selectedIndex != -1){
		obj.remove(obj.selectedIndex);
		obj.focus();
	}
	else{
		alert('��ѡ����Ҫɾ���Ķ˿ڣ�');
	}
}

//�ύ���ݲ�ѯ
function getData(){
	//ͳ��ʱ��
	if (document.frm.day.value == ""){
		alert('��ѡ��ͳ�����ڣ�');
		document.frm.day.focus();
		return false;
	}
	//ѡ���ͳ������
	var flag = true;
	for (var j=0;j<document.frm.kind.length;j++){
		if (document.frm.kind[j].checked){
			flag = false;
		}
	}
	if (flag){
		alert('��ѡ����Ҫͳ�Ƶ����ܣ�');
		return false;
	}
	//��ѡ����豸�˿�
	var str = "";
	var strInfo = "";
	for (var i=0;i<document.frm.selectInfo.length;i++){
		str += document.frm.selectInfo[i].value + ";";
		strInfo += document.frm.selectInfo[i].text + ";";
	}
	if (str == ""){
		alert('��ѡ����Ҫͳ�Ƶ��豸�˿ڣ�');
		document.frm.selectInfo.focus();
		return false;
	}
	document.frm.devicePortList.value = str;
	document.frm.devicePortListInfo.value = strInfo;

	document.frm.submit();
}
</script>
</head>

<body>
<form action="<s:url value="/flux/FluxReport!getFluxReportInfo.action"/>" name="frm" method="post">
<input type="hidden" name="devicePortList" value="">
<input type="hidden" name="devicePortListInfo" value="">
<table border=0 cellspacing=0 cellpadding=0 width="95%">
	<TR><TD HEIGHT=20>&nbsp;</TD></TR>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						��������ͳ��
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">����豸��������ͳ���ձ���
					</td>
					<td align="left">
					</td>
				</tr>
			</table>
			</td>
		</tr>
	<tr>
		<td>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
				bgcolor="#000000">
				<tr><th colspan="2">��������ͳ���ձ���</th></tr>
				<tr bgcolor="#FFFFFF">
					<td class="column" width="15%" nowrap>��ѡ��ͳ�����ڣ�</td>
					<td width="85%"  nowrap>
						<INPUT TYPE="text" NAME="day" class=bk readonly=true value="<s:property value="day"/>">
		  				<input TYPE="button" value="��" class="btn" onclick="new WdatePicker(document.frm.day,'%Y-%M-%D',false,'whyGreen')">
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class="column" nowrap>��ѡ����Ҫͳ�Ƶ�����<br><input type="checkbox" name="selectKinds" onclick="selectKind()">ȫѡ</td>
					<td>
						<input type="checkbox" name="kind" value="ifinoctetsbps">��������/bps
						<input type="checkbox" name="kind" value="ifinerrorspps">��������
						<input type="checkbox" name="kind" value="Ifindiscardspps">���붪����
						<input type="checkbox" name="kind" value="Ifinucastpktspps">����㲥������
						<br>
						<input type="checkbox" name="kind" value="ifoutoctetsbps">��������/bps
						<input type="checkbox" name="kind" value="ifouterrorspps">���������
						<input type="checkbox" name="kind" value="Ifoutdiscardspps">����������
						<input type="checkbox" name="kind" value="Ifoutucastpktspps">�����㲥������
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class="column" nowrap>��ѡ���豸���̣�</td>
					<td>
						<s:property value="vendorList" escapeHtml="false"/>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class="column" nowrap>��ѡ���豸�ͺţ�</td>
					<td>
						<div id="modelSelect">
							<select name="device_model" class="bk" onchange="showChild('device')">
								<option value="-1">==����ѡ����==</option>
							</select>
						</div>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class="column" nowrap>��ѡ���豸��</td>
					<td>
						<div id="deviceSelect">
							<select name="device_id" class="bk" onchange="showChild('port')">
								<option value="-1">==����ѡ���豸�ͺ�==</option>
							</select>
						</div>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class="column" nowrap>��ѡ���豸�˿ڣ�<br><input type="checkbox" name="selectPorts" onclick="selectPort()">ȫѡ</td>
					<td>
						<div id="portSelect">
							����ѡ���豸
						</div>
						<input type="button" value="���ѡ���豸�˿�" onclick="selectPortInfo()">
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class="column" nowrap>��ѡ����豸�Ͷ˿�</td>
					<td>
						<select name="selectInfo" size="10">
						</select>
						<input type="button" value="ɾ��ѡ��Ķ˿�" onclick="dselectPortInfo()">
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan="2"><input type="button" value=" ͳ �� " onclick="getData()"></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>
