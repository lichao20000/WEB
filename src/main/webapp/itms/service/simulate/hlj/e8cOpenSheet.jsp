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
		var cityId = $.trim($("select[@name='cityId']").val());
		var officeId = $.trim($("input[@name='officeId']").val());
		var zoneId = $.trim($("input[@name='zoneId']").val());
		var orderType = $.trim($("select[@name='orderType']").val());
		var linkman = $.trim($("input[@name='linkman']").val());
		var linkphone = $.trim($("input[@name='linkphone']").val());
		var linkEmail = $.trim($("input[@name='linkEmail']").val());
		var linkMobile = $.trim($("input[@name='linkMobile']").val());
		var homeAddr = $.trim($("input[@name='homeAddr']").val());
		var credNo = $.trim($("input[@name='credNo']").val());
		var customerId = $.trim($("input[@name='customerId']").val());
		var customerAccount = $.trim($("input[@name='customerAccount']").val());
		var customerPwd = $.trim($("input[@name='customerPwd']").val());
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
		//����
		if(cityId  == -1){
			alert("��ѡ������");
			$("select[@name='cityId']").focus();
			return false;
		}
		//�û��˺�
		if(username == ""){
			alert("�������û��˺�");
			$("input[@name='username']").focus();
			return false;
		}
		//�ն˽��뷽ʽ
		if(orderType == -1){
			alert("��ѡ���ն˽��뷽ʽ");
			$("select[@name='orderType']").focus();
			return false;
		}
		if(isNaN(linkphone)){
			alert("���������ϵ�绰�������֣�����������!");
			$("input[@name='linkphone']").focus();
			return false;
		}
		
		var regEmail =  /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
		if(linkEmail != ""){
			if(!regEmail.test(linkEmail)){
				alert('��������Ч��E_mail!');
				$("input[@name='linkphone']").focus();
				return false;
			}
		}
		if(isNaN(linkMobile)){
			alert("��������ֻ����벻�����֣�����������!");
			$("input[@name='linkMobile']").focus();
			return false;
		}
		if(isNaN(credNo)){
			alert("�������֤������벻�����֣�����������!");
			$("input[@name='credNo']").focus();
			return false;
		}
		//�ͻ�ID
		if(customerId == ""){
			alert("������ͻ�id");
			$("input[@name='customerId']").focus();
			return false;
		}
		$("tr[@id='trData']").show();
	    $("div[@id='QueryData']").html("���ڷ��ͣ����Ե�....");
	    $("button[@name='e8cOpenbutton']").attr("disabled", true);
	    var url = "<s:url value='/itms/service/simulateHLJSheet!sendSheet.action'/>";
		$.post(url, {
			servTypeId:servTypeId,
			operateType:operateType,
			dealdate:dealdate,
			userType:userType,
			username:username,
			cityId:cityId,
			officeId:officeId,
			zoneId:zoneId,
			orderType:orderType,
			linkman:linkman,
			linkphone:linkphone,
			linkEmail:linkEmail,
			linkMobile:linkMobile,
			homeAddr:homeAddr,
			credNo:credNo, 
			customerId:customerId,
			customerAccount:customerAccount,
			customerPwd:customerPwd
		}, function(ajax) {
			$("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
			$("button[@name='e8cOpenbutton']").attr("disabled", false);
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
				<td class="column" align="right" width="20%">����</td>
				<td colspan="3">
					<s:select list="cityList" name="cityId"
						headerKey="-1" headerValue="��ѡ������" listKey="city_id"
						listValue="city_name" value="cityId" cssClass="bk">
					</s:select>&nbsp; 
					<font color="#FF0000">*</font>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right">�����־</td>
				<td>
					<input type="text" name="officeId" maxlength=50 class=bk value="" />
				</td>
				<td class="column" align="right">С����־</td>
				<td colspan="3">
					<input type="text" name="zoneId" class="bk" value="<s:property value="vlanid"/>" />
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" width="20%">�ն˽�������</td>
				<td width="30%">
					<select name="orderType" class="bk">
						<option value="-1">==��ѡ��==</option>
						<option value="1">==ADSL==</option>
						<option value="2">==LAN==</option>
						<option value="3">==EPON==</option>
						<option value="4">==GPON==</option>
					</select> &nbsp; 
					<font color="#FF0000">*</font>
				</td>
				<td class="column" align="right" nowrap>��ϵ��</td>
				<td colspan="3">
					<input type="text" name="linkman" class="bk" value="" />&nbsp;
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" nowrap>��ϵ�绰</td>
				<td>
					<input type='text' name="linkphone" class="bk" value="" />&nbsp;
				</td>
				<td class="column" align="right">Email</td>
				<td colspan="3">
					<input type="text" name="linkEmail" class=bk maxlength=20 value="" /> 
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right">�ֻ�</td>
				<td>
					<input type="text" name="linkMobile" class="bk" maxlength=20 value="" /> &nbsp;
				</td>
				<td class="column" align="right">��ͥסַ</td>
				<td colspan="3">
					<input type="text" name="homeAddr" class="bk" maxlength=20 value="" />
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right">֤������</td>
				<td>
					<input type="text" name="credNo" maxlength=20 class="bk" value="">&nbsp;
				</td>
				<td class="column" align="right">�ͻ�Id</td>
				<td colspan="3">
					<input type="text" name="customerId" class="bk" maxlength=20 value=""> &nbsp;
					<font color="#FF0000">*</font>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right">�ͻ��ʺ�</td>
				<td>
					<input type="text" name="customerAccount" class="bk" maxlength=20 value="">&nbsp;
				</td>
				<td class="column" align="right">�ͻ�����</td>
				<td colspan="3">
					<input type="password" name="customerPwd" class="bk" maxlength=20 value="">&nbsp;
				</td>
			</tr>
			<%-- <tr style="background-color: #ffffff;">
				<td class="column" align="right">�ͻ�����</td>
				 <td>
					<input type="text" name="customerSpeed" class=bk maxlength=20 value="">&nbsp;
					<font color="#FF0000">*</font>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class="column" align="right">�ͻ��ȼ�</td>
				<td>
					<input type="text" name="customerLevel" class=bk maxlength=20 value="">&nbsp;
					<font color="#FF0000">*</font>
				</td>
				<td class="column" align="right">�ն˹��</td>
				<td>
					<select name="terminalSpec" class="bk">
						<option value="-1">==��ѡ��==</option>
					</select> &nbsp; 
					<font color="#FF0000">*</font>
				</td>
			</tr> --%>
			<tr>
				<td colspan="7" align="right" class="green_foot" >
					<button name="e8cOpenbutton" onclick="sendSheet();">&nbsp;���͹���&nbsp;</button>
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