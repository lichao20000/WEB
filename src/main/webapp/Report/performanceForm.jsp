<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>���ܱ���ҳ��</title>
<%
	/**
		 * ���ܱ���ҳ��
		 * 
		 * @author ������(5243)
		 * @version 1.0
		 * @since 2008-08-12
		 * @category
		 */
%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="../Js/jsDate/WdatePicker.js"></script>
<script type="text/javascript">

//��ʾ�����µ��豸�б�
function showDevList(){
	$("#devSelect").html("���ڼ����豸�����Ե�...");
	$.ajax({
		type:"POST",
		url: "<s:url value="/flux/PerformanceReport!getCityDev.action"/>",
		data: {"city_id":$("select[@name='city_id']").val()},
		success:function(data){
			$("#devSelect").html(data);
		},
		erro:function(xmlR,msg,other){
			alert(msg);
		}
	});
}


//�ύ��֤
function checkForm(){
	//�ж��Ƿ�ѡ����ͳ������
	var flag = false;
	$("input[@name='reportType']").each(function(){
		if (this.checked){
			flag = true;
		}
	});
	if (!flag){
		alert('��ѡ��ͳ�����ͣ�');
		return false;
	}
	//�ж��Ƿ�ѡ�����豸
	flag = false;
	$("input[@name='devList']").each(function(){
		if (this.checked){
			flag = true;
		}
	});
	if (!flag){
		alert('��ѡ����Ҫͳ�Ƶ��豸��');
		return false;
	}
	return true;
}

//ȫѡ�豸
function selectAllDev(){
	$("input[@name='devList']").each(function(){
		this.checked = $("input[@name='selectAll']").attr("checked");
	});
}

</script>
</head>

<body>
<form name="frm" action="<s:url value="/flux/PerformanceReport!getPerReport.action"/>" method="post" onsubmit="return checkForm()">
<table border=0 cellspacing=0 cellpadding=0 width="95%" align="center">
	<TR><TD HEIGHT=20>&nbsp;</TD></TR>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						���ܱ���
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">		
					</td>
					<td align="right">
					</td>
				</tr>
			</table>
			</td>
		</tr>
	<tr>
		<td>
		<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
			bgcolor="#000000">
			<tr bgcolor="#FFFFFF">
				<td class=column width="15%" nowrap>��ѡ�񱨱����ͣ�</td>
				<td width="85%">
					<input type="radio" name="type" value="0" checked>�ձ���
					<input type="radio" name="type" value="1">�ܱ���
					<input type="radio" name="type" value="2">�±���
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class=column width="15%" nowrap>��ѡ�����ڣ�</td>
				<td width="85%">
					<INPUT TYPE="text" NAME="day" class=bk readonly=true value="<s:property value="day"/>"> 
		  			<input TYPE="button" value="��" class="btn" onclick="new WdatePicker(document.frm.day,'%Y-%M-%D',false,'whyGreen')">
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class=column nowrap>��ѡ��ͳ�����ͣ�</td>
				<td>
					<input type="checkbox" name="reportType" value="cpu">CPU
					<input type="checkbox" name="reportType" value="mem">�ڴ�
					<input type="checkbox" name="reportType" value="wanPort">WAN�˿�����
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class=column nowrap>��ѡ�����أ�</td>
				<td>
					<select name="city_id" onchange="showDevList()">
						<option value="-1">==��ѡ��==</option>
						<s:iterator value="cityList">
							<option value="<s:property value="city_id"/>"><s:property value="city_name"/></option>
						</s:iterator>
					</select>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class=column nowrap>��ѡ���豸��<br><input type="checkbox" value="ȫѡ" name="selectAll" onclick="selectAllDev()">ȫѡ</td>
				<td>
					<div id="devSelect">����ѡ������</div>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td colspan="2" align="right" class="green_foot">
					<input type="submit" value="ͳ��">
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="20">&nbsp;</td>
	</tr>
</table>
</form>
</body>
</html>
