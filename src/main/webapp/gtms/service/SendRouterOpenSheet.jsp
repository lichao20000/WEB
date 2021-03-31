<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>BSSģ�⹤��</title>

<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/CheckFormForm.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

<script type="text/javascript">

	//��ʾ����������Ϣ
	function sendSheet(){
		
		var url = "<s:url value='/gtms/service/sendRouterOpenSheet!sendSheet.action'/>";
		var netUserName = "";
		
		var _operateType = $("select[@name='operateType']");  // ��������
		var _cityId = $("select[@name='cityId']");            // ����
		
		var _servTypeId = $("input[@name='servTypeId']");     // ҵ������
		var _netUsername = $("input[@name='netUsername']");   // ����ʺ�
		var _netPassword = $("input[@name='netPassword']");   // �������
		
		if ('-1' == _operateType.val()) {
			alert("��ѡ��������ͣ�");
			_operateType.focus();
			return false;
		}
		if ('-1' == _cityId.val()) {
			alert("��ѡ�����أ�");
			_cityId.focus();
			return false;
		}
		
		// 3 ��ʾ����   1 ��ʾ����
		if('3' == _operateType.val()){
			var _netUsername2 = $("input[@name='netUsername2']");
			if (!IsNull(_netUsername2.val(), "����ʺ�")) {
				_netUsername2.focus();
				return false;
			} else {
				netUserName = _netUsername2.val();
			}
		} else {
			if (!IsNull(_netUsername.val(), "����ʺ�")) {
				_netUsername.focus();
				return false;
			} else {
				netUserName = _netUsername.val();
			}
		}
		
		// 3 ��ʾ����   1 ��ʾ����
		if('3' != _operateType.val()){ 
			if (!IsNull(_netPassword.val(), "�������")) {
				_netPassword.focus();
				return false;
			}
		}
		
		$.post(url,{
			servTypeId:_servTypeId.val(),
			operateType:_operateType.val(),
			cityId:_cityId.val(),
			netUsername:netUserName,
			netPassword:_netPassword.val()
		},function(ajax){
			//alert(ajax);
			var msg = ajax.split("|||");
			if("0" == msg[0]){
				alert("�ɹ���");
			} else {
				alert(msg[2]);  // �ɹ���Ϣ��0|||�ɹ�����    ʧ����Ϣ��1|||ʧ�ܴ���|||ʧ������
			}
		});
		
	}
	
	function hiddenElements(){
		var _operateType = $("select[@name='operateType']");	// ��������
		
		if(_operateType.val() == "-1" || _operateType.val() == "1"){
			$("tr[@id='tr01']").css("display","");
			$("tr[@id='tr02']").css("display","none");
			
			$("input[@name='netUsername']").val("");
			$("input[@name='netPassword']").val("");
			$("input[@name='netUsername2']").val("");
		}else{
			$("tr[@id='tr01']").css("display","none");
			$("tr[@id='tr02']").css("display","");
			
			$("input[@name='netUsername']").val("");
			$("input[@name='netPassword']").val("");
			$("input[@name='netUsername2']").val("");
		}
	}
</script>
</head>

<body onLoad="hiddenElements()">
<form name="mainfrm" action="" target="dataForm">
<input name="servTypeId" value="50" type="hidden">
<table>
	<tr>
		<td HEIGHT=20>&nbsp;</td>
	</tr>
	<tr>
		<td>
		<table class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">·��ҵ�񹤵�</td>
				<td><img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12" /> ·��ҵ�񹤵�</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table class="querytable">

			<TR>
				<th colspan="4">ģ��radius������</th>
			</tr>

			<TR bgcolor="#FFFFFF">
				<TD class=column align="right" width="20%">��������</TD>
				<TD width="30%"><select name="operateType" class="bk" onChange="hiddenElements()">
				    <!-- option value="-1">==��ѡ���������==</option-->
					<option value="1">==����==</option>
					<option value="3">==����==</option>
				</select>&nbsp; <font color="#FF0000">* </font></TD>
				
				<TD class=column align="right" width="20%">����</TD>
				<TD width="30%"><s:select list="cityList" name="cityId"
					headerKey="-1" headerValue="��ѡ������" listKey="city_id"
					listValue="city_name" value="cityId" cssClass="bk"></s:select>
				&nbsp; <font color="#FF0000">*</font></TD>
			</TR>
			
			<TR id="tr01" bgcolor="#FFFFFF" style="display:none">
				<TD class=column align="right" width="20%">����˺�</TD>
				<TD width="30%"><INPUT TYPE="text" NAME="netUsername" maxlength=50 class="bk"
					value="">&nbsp; <font color="#FF0000">* </font></TD>
						
				<TD class=column align="right" width="20%" >�������</TD>
				<TD width="30%" ><INPUT TYPE="text" NAME="netPassword" maxlength=50 class="bk"
					value="">&nbsp; <font color="#FF0000">* </font></TD>
			</TR>
			
			<TR id="tr02" bgcolor="#FFFFFF" style="display:none">
				<TD class=column align="right" width="20%">����˺�</TD>
				<TD width="30%"><INPUT TYPE="text" NAME="netUsername2" maxlength=50 class="bk"
					value="">&nbsp; <font color="#FF0000">* </font></TD>
						
				<TD class=column align="right" width="20%" ></TD>
				<TD width="30%" ></TD>
			</TR>
			
			<TR>
				<td colspan="4" align="right" class=foot>
				<button onclick="sendSheet()">&nbsp;���͹���&nbsp;</button>
				</td>
			</TR>
		</table>
		</td>
	</tr>
</table>
<br>
</form>
</body>
</html>