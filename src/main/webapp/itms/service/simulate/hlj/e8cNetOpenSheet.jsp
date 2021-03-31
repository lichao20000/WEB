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
<title>BSS����ҵ�񿪻�</title>
<script type="text/javascript">
	var wlantype; 
	function sendSheet(){
		var servTypeId = $.trim($("input[@name='servTypeId']").val());
		var	operateType	= $.trim($("input[@name='operateType']").val());
		var dealdate = $.trim($("input[@name='dealdate']").val());
		var userType = $.trim($("select[@name='userType']").val());
		var username = $.trim($("input[@name='username']").val());
		var netUsername = $.trim($("input[@name='netUsername']").val());
		var netPassword = $.trim($("input[@name='netPassword']").val());
		var cityId = $.trim($("select[@name='cityId']").val());
		var vlanId = $.trim($("input[@name='vlanId']").val());
		var wlantype = $.trim($("select[@name='wlantype']").val());
		var ipaddress = $.trim($("input[@name='ipaddress']").val());
		var code = $.trim($("input[@name='code']").val());
		var netway = $.trim($("input[@name='netway']").val());
		var dns = $.trim($("input[@name='dns']").val());
		var useriptype = $.trim($("select[@name='useriptype']").val());
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
		//����˺�
		if(netUsername == ""){
			alert("���������˺�");
			$("input[@name='netUsername']").focus();
			return false;
		}
		//�������
		if(netPassword == ""){
			alert("������������");
			$("input[@name='netPassword']").focus();
			return false;
		}
		//VLANID
		if(vlanId == ""){
			alert("������VLANID");
			$("input[@name='vlanId']").focus();
			return false;
		}
		//����
		if(cityId  == -1){
			alert("��ѡ������");
			$("select[@name='cityId']").focus();
			return false;
		}
		//������ʽ
		if(wlantype == -1){
			alert("��ѡ��������ʽ");
			$("select[@name='wlantype']").focus();
			return false;
		}
		/*
		//�û�IP����
		if(useriptype == -1){
			alert("��ѡ���û�IP����");
			$("select[@name='useriptype']").focus();
			return false;
		}
		 function change_select(){
			wlantype = $.trim($("select[@name='wlantype']").val());
			if (wlantype == 3) {
				$("#tr1").css("display","");
				$("#tr2").css("display","");
			}else{
				$("#tr1").css("display","none");
				$("#tr2").css("display","none");
			}
		} */
		if(wlantype == 3){
			var myReg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
			//Ip��ַ
			if(ipaddress == ""){
				alert("������Ip��ַ");
				$("input[@name='ipaddress']").focus();
				return false;
			}else{
				if(!myReg.test(ipaddress)){
					alert('��������Ч��IP��ַ!');
					$("input[@name='ipaddress']").focus();
					return false;
				}
			}
			//����
			if(code == ""){
				alert("����������");
				$("input[@name='code']").focus();
				return false;
			}else{
				if(!myReg.test(code)){
					alert('��������Ч������!');
					$("input[@name='code']").focus();
					return false;
				}
			}
			//����
			if(netway == ""){
				alert("����������");
				$("input[@name='netway']").focus();
				return false;
			}else{
				if(!myReg.test(netway)){
					alert('��������Ч������!');
					$("input[@name='netway']").focus();
					return false;
				}
			}
			//DNS
			if(dns == ""){
				alert("������DNS");
				$("input[@name='dns']").focus();
				return false;
			}else{
				if(!myReg.test(dns)){
					alert('��������Ч��DNS!');
					$("input[@name='dns']").focus();
					return false;
				}
			}	
		}
		$("tr[@id='trData']").show();
	    $("div[@id='QueryData']").html("���ڷ��ͣ����Ե�....");
	    $("button[@name='e8cNetOpenButton']").attr("disabled", true);
	    var url = "<s:url value='/itms/service/simulateHLJSheet!sendSheet.action'/>";
    	  $.post(url, {
  			servTypeId:servTypeId,
  			operateType:operateType,
  			dealdate:dealdate,
  			userType:userType,
  			username:username,
  			netUsername:netUsername,
  			netPassword:netPassword,
  			cityId:cityId,
  			vlanId:vlanId,
  			wlantype:wlantype,
  			ipaddress:ipaddress,
  			code:code,
  			netway:netway,
  			dns:dns,
  			useriptype:useriptype
  		}, function(ajax) {
  			$("div[@id='QueryData']").html("");
  			$("div[@id='QueryData']").append(ajax);
  			$("button[@name='e8cNetOpenButton']").attr("disabled", false);
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
				<td class="column" align="right" width="20%">����˺�</td>
				<td colspan="3">
					<input type="text" name="netUsername" maxlength=50 class="bk" value="" />&nbsp; 
					<font color="#FF0000">* </font>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" nowrap>�������</td>
				<td>
					<input type="password" name="netPassword" maxlength=50 class="bk" value="" />&nbsp; 
					<font color="#FF0000">* </font>
				</td>
				<td class="column" align="right" nowrap>����</td>
				<td colspan="3">
					<s:select list="cityList" name="cityId"
						headerKey="-1" headerValue="��ѡ������" listKey="city_id"
						listValue="city_name" value="cityId" cssClass="bk">
					</s:select>&nbsp; 
					<font color="#FF0000">*</font>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" nowrap>VLANID</td>
				<td>
					<input type="text" name="vlanId" maxlength=50 class="bk" value="" />&nbsp; 
					<font color="#FF0000">* </font>
				</td>
				<td class="column" align="right" nowrap>������ʽ</td>
				<td colspan="3">
					<select name="wlantype" class="bk">
						<option value="-1">==��ѡ��==</option>
						<option value="1">==�Ž�==</option>
						<option value="2">==·��==</option>
						<option value="3">==��̬IP==</option>
						<option value="4">==DHCP==</option>
					</select> &nbsp; <font color="#FF0000">*</font>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" nowrap style="display: none;">�û�IP����</td>
				<td style="display: none;">
					<select name="useriptype" class="bk">
						<option value="-1">==��ѡ��==</option>
						<option value="0">==������ջ==</option>
						<option value="1">==����˫ջ==</option>
						<option value="2">==˽����ջ==</option>
						<option value="3">==˽��˫ջ==</option>
						<option value="4">==DS-Lite==</option>
						<option value="5">==��V6==</option>
						<option value="6">==LAFT6==</option>
					</select> &nbsp; 
					<font color="#FF0000">*</font>
				</td>
				<td class="column" align="right" nowrap>IP��ַ</td>
				<td>
					<input type="text" name="ipaddress" maxlength=50 class="bk" value="" />&nbsp; 
				</td>
				<td class="column" align="right" nowrap>����</td>
				<td colspan="3">
					<input type="text" name="code" maxlength=50 class="bk" value="" />&nbsp; 
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" nowrap>����</td>
				<td>
					<input type="text" name="netway" maxlength=50 class="bk" value="" />&nbsp; 
				</td>
				<td class="column" align="right" nowrap>DNS</td>
				<td colspan="3">
					<input type="text" name="dns" maxlength=50 class="bk" value="" />&nbsp; 
				</td>
			</tr>
			<!-- <tr style="background-color: #ffffff;display: none;">
				<td class="column" align="right" nowrap>DNS</td>
				<td>
					<input type="text" name="dns" maxlength=50 class="bk" value="" />&nbsp; 
				</td>
				<td  colspan="4">
				</td>
			</tr> -->
			<tr>
				<td colspan="7" align="right" class="green_foot" >
					<button name="e8cNetOpenButton" onclick="sendSheet();">&nbsp;���͹���&nbsp;</button>
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