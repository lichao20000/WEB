<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 <link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">
<script type="text/javascript" src="/Js/jquery.js"></script>
<script type="text/javascript" src="/Js/CheckFormForm.js"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>BSS������Ϣ����</title>
<script type="text/javascript">
	function sendSheet(){
		var servTypeId = $.trim($("input[@name='servTypeId']").val());
		var	operateType	= $.trim($("input[@name='operateType']").val());
		var dealdate = $.trim($("input[@name='dealdate']").val());
		var userType = $.trim($("select[@name='userType']").val());
		var username = $.trim($("input[@name='username']").val());
		var voipTelepone = $.trim($("input[@name='voipTelepone']").val());
		var cityId = $.trim($("select[@name='cityId']").val());
		var regId = $.trim($("input[@name='regId']").val());
		var regIdType = $.trim($("select[@name='regIdType']").val());
		var sipIp = $.trim($("input[@name='sipIp']").val());
		var sipPort = $.trim($("input[@name='sipPort']").val());
		var standSipIp = $.trim($("input[@name='standSipIp']").val());
		var standSipPort = $.trim($("input[@name='standSipPort']").val());
		var linePort = $.trim($("select[@name='linePort']").val());
		var protocolType = $.trim($("select[@name='protocolType']").val());
		//����ʱ��
		if(dealdate == ""){
			$("input[@name='dealdate']").focus();
			return false;
		}
		//�豸����
		if(userType == -1){
			alert("��ѡ���豸����");
			$("select[@name='userType']").focus();
			return false;
		}
		//�û��˺�
		if(username == ""){
			alert("�������û��˺�");
			$("input[@name='username']").focus();
			return false;
		}
		//ҵ��绰����
		if(voipTelepone  == ""){
			alert("������ҵ��绰����");
			$("input[@name='voipTelepone']").focus();
			return false;
		}
		//����
		if(cityId  == ""){
			alert("��ѡ������");
			$("select[@name='cityId']").focus();
			return false;
		}
		//�ն˱�ʶ
		var regIdReg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+.(voip)$/;
		if(regId  == ""){
			alert("�������ն˱�ʶ");
			$("input[@name='regId']").focus();
			return false;
		}else{
			if(!regIdReg.test(regId)){
				alert('��������Ч���ն˱�ʶ!');
				$("input[@name='regId']").focus();
				return false;
			}
		}
		//�ն˱�ʶ����
		if(regIdType  == -1){
			alert("��ѡ���ն˱�ʶ����");
			$("select[@name='regIdType']").focus();
			return false;
		}
		var myReg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
		//����MGC��ַ
		if(sipIp  == ""){
			alert("����������MGC��ַ");
			$("input[@name='sipIp']").focus();
			return false;
		}
		//����MGC���˿�
		if(sipPort  == ""){
			alert("����������MGC���˿�");
			$("input[@name='sipPort']").focus();
			return false;
		}else{
			if(isNaN(sipPort)){
				alert("�������MGC���˿ڲ������֣�����������!");
				$("input[@name='sipPort']").focus();
				return false;
			}
		}
		//����MGC��ַ
		if(standSipIp  == ""){
			alert("�����뱸��MGC��ַ");
			$("input[@name='standSipIp']").focus();
			return false;
		}
		//����MGC�˿�
		if(standSipPort  == ""){
			alert("�����뱸��MGC�˿�");
			$("input[@name='standSipPort']").focus();
			return false;
		}else{
			if(isNaN(standSipPort)){
				alert("������ı���MGC�˿ڲ������֣�����������!");
				$("input[@name='standSipPort']").focus();
				return false;
			}
		}
		//�ն������ʾ
		if(linePort  == -1){
			alert("��ѡ���ն������ʾ");
			$("select[@name='linePort']").focus();
			return false;
		}
		//
		if(protocolType  == -1){
			alert("��ѡ��Э������");
			$("select[@name='protocolType']").focus();
			return false;
		}
		$("tr[@id='trData']").show();
	    $("div[@id='QueryData']").html("���ڷ��ͣ����Ե�....");
	    $("button[@name='H248VoipButton']").attr("disabled", true);
	    var url = "<s:url value='/itms/service/simulateHLJSheet!sendSheet.action'/>";
		$.post(url, {
			servTypeId:servTypeId,
			operateType:operateType,
			dealdate:dealdate,
			userType:userType,
			username:username,
			voipTelepone:voipTelepone,
			cityId:cityId,
			regId:regId,
			regIdType:regIdType,
			sipIp:sipIp,
			sipPort:sipPort,
			standSipIp:standSipIp,
			standSipPort:standSipPort,
			linePort:linePort
		}, function(ajax) {
			$("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
			$("button[@name='H248VoipButton']").attr("disabled", false);
		});
	}
</script>
</head>
<body>
	<form name="frm" method="post" style="text-align: left;">
		<table class="listtable"  width="100%"> 
			<tr class="green_title">
				<td colspan="7">
					<input type="hidden" name="servTypeId" value='<s:property value="servTypeId" />'> 
					<input	type="hidden" name="operateType" value='<s:property value="operateType" />'>
				<b>E8-C������Ϣ</b>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" width="20%">����ʱ��</td>
				<td width="30%">
					<input type="text" name="dealdate"	value='<s:property value="dealdate" />' 
						readonly class="bk">
					<img name="shortDateimg"
						onClick="WdatePicker({el:document.frm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
						src="../../../../images/dateButton.png" width="15" height="12"
						border="0" alt="ѡ��">&nbsp; 
					<font color="#FF0000">*</font>
				</td>
				<td class="column" align="right" width="20%">�豸����</td>
				<td colspan="3">
					<select name="userType" class="bk">
							<option value="-1">==��ѡ��==</option>
							<option value="1" >==��ͥ����==</option>
							<option value="2">==��ҵ����==</option>
					</select> &nbsp; 
					<font color="#FF0000">*</font>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" nowrap>LOID</td>
				<td>
					<input type="text" name="username" maxlength=50 class="bk" value="" />&nbsp; 
					<font color="#FF0000">* </font>
				</td>
				<td class="column" align="right" width="20%">ҵ��绰����</td>
				<td colspan="3">
					<input type="text" name="voipTelepone" maxlength=50 class="bk" />&nbsp; 
					<font color="#FF0000">* </font>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" nowrap>����</td>
				<td>
					<s:select list="cityList" name="cityId"
						headerKey="-1" headerValue="��ѡ������" listKey="city_id"
						listValue="city_name" value="cityId" cssClass="bk">
					</s:select>&nbsp; 
					<font color="#FF0000">*</font>
				</td>
				<td class="column" align="right" width="20%">�ն˱�ʶ</td>
				<td colspan="3">
					<input type="text" name="regId" maxlength=50 class="bk" />&nbsp; 
					<font color="#FF0000">* </font>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" nowrap>�ն˱�ʶ����</td>
				<td>
					<select name="regIdType" class="bk">
							<option value="-1">==��ѡ���ʶ����==</option>
							<option value="0">==IP��ַ==</option>
							<option value="1">==����==</option>
							<option value="2">==�豸��==</option>
					</select> &nbsp;
					<font color="#FF0000">*</font>
				</td>
				<td class="column" align="right" width="20%">����MGC��ַ</td>
				<td colspan="3">
					<input type="text" name="sipIp" maxlength=50 class="bk" />&nbsp; 
					<font color="#FF0000">* </font>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" nowrap>����MGC�˿�</td>
				<td>
					<input type="text" name="sipPort" maxlength=50 class="bk" />&nbsp; 
					<font color="#FF0000">* </font>
				</td>
				<td class="column" align="right" width="20%">����MGC��ַ</td>
				<td colspan="3">
					<input type="text" name="standSipIp" maxlength=50 class="bk" />&nbsp; 
					<font color="#FF0000">* </font>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" nowrap>����MGC�˿�</td>
				<td>
					<input type="text" name="standSipPort" maxlength=50 class="bk" />&nbsp; 
					<font color="#FF0000">* </font>
				</td>
				<td class="column" align="right" width="20%">�ն������ʾ</td>
				<td colspan="3">
					<select name="linePort" class="bk">
							<option value="-1">==��ѡ���ն������ʾ==</option>
							<option value="A0">==A0==</option>
							<option value="A1">==A1==</option>
					</select> &nbsp;<font color="#FF0000">*</font>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" nowrap>Э������</td>
				<td>
					<select name="protocolType" class="bk">
							<option value="-1">==��ѡ��Э������==</option>
							<option value="0">==IMS==</option>
							<option value="1">==����==</option>
					</select> &nbsp;<font color="#FF0000">*</font>
				</td>
				<!-- <td class="column" align="right" width="20%"></td>-->
				<td colspan="4"></td> 
			</tr>
			<tr>
				<td colspan="7" align="right" class="green_foot" >
					<button name="H248VoipButton" onclick="sendSheet();">&nbsp;���͹���&nbsp;</button>
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td height="30"></td>
			</tr>
			<tr id="trData" style="display: none">
				<td>
					<div id="QueryData" style="width: 105%; z-index: 1; top: 100px;text-align: left;">
						���ڷ��ͣ����Ե�....
					</div>
				</td>
			</tr>
			<tr>
				<td height="20"></td>
			</tr>
		</table>
	</form>
</body>
</html>