<%@page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />

<title>�û�ҵ���ѯ</title>
<script type="text/javascript"
	src="<s:url value="/Js/CheckFormForm.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
	function change(obj){
var value = obj.value;
	if(value==1){
		$("input[@name='connAccount']").removeAttr("disabled");
		$("input[@name='connAccount']").val("");
		$("input[@name='connPwd']").removeAttr("disabled");
		$("input[@name='connPwd']").val("");
		$("input[@name='ipAddr']").val("����ѡ���û�����!");
		$("input[@name='ipAddr']").attr("disabled","disabled");	
		$("input[@name='hideNode']").val("����ѡ���û�����!");
		$("input[@name='hideNode']").attr("disabled","disabled");	
		$("input[@name='netCheck']").val("����ѡ���û�����!");
		$("input[@name='netCheck']").attr("disabled","disabled");	
		$("input[@name='dnsMsg']").val("����ѡ���û�����!");
		$("input[@name='dnsMsg']").attr("disabled","disabled");	
	}else if(value==3){
		$("input[@name='ipAddr']").removeAttr("disabled");
		$("input[@name='ipAddr']").val("");
		$("input[@name='hideNode']").removeAttr("disabled");
		$("input[@name='hideNode']").val("");
		$("input[@name='netCheck']").removeAttr("disabled");
		$("input[@name='netCheck']").val("");
		$("input[@name='dnsMsg']").removeAttr("disabled");
		$("input[@name='dnsMsg']").val("");
		$("input[@name='connAccount']").val("����ѡ��������ʽ!");
		$("input[@name='connAccount']").attr("disabled","disabled");	
		$("input[@name='connPwd']").val("����ѡ��������ʽ!");
		$("input[@name='connPwd']").attr("disabled","disabled");	
	}else{
		$("input[@name='connAccount']").val("����ѡ��������ʽ!");
		$("input[@name='connAccount']").attr("disabled","disabled");
		$("input[@name='connPwd']").val("����ѡ��������ʽ!");
		$("input[@name='connPwd']").attr("disabled","disabled");	
		$("input[@name='ipAddr']").val("����ѡ��������ʽ!");
		$("input[@name='ipAddr']").attr("disabled","disabled");	
		$("input[@name='hideNode']").val("����ѡ��������ʽ!");
		$("input[@name='hideNode']").attr("disabled","disabled");	
		$("input[@name='netCheck']").val("����ѡ��������ʽ!");
		$("input[@name='netCheck']").attr("disabled","disabled");	
		$("input[@name='dnsMsg']").val("����ѡ��������ʽ!");
		$("input[@name='dnsMsg']").attr("disabled","disabled");	
	}
}
	
	function checkMAC(mac){
	  	var macs = new Array();
	  	macs = mac.split(":");
	  	if(macs.length != 6){
				alert("�����mac��ַ��ʽ����ȷ������xx:xx:xx:xx:xx:xx����ʽ���루xxΪ16�������֣�!");
				return false;
			}
	   for (var s=0; s<6; s++) {
	   	var num = macs[s];
	   	if(num.length>2){
	   		alert("�����mac��ַ��ʽ����ȷ������xx:xx:xx:xx:xx:xx����ʽ���루xxΪ16�������֣�!");
	   		 return false;  
	   	}
	   	var temp = parseInt(macs[s],16);
	   	if(isNaN(temp)){
	   		alert("�����mac��ַ��ʽ����ȷ������xx:xx:xx:xx:xx:xx����ʽ���루xxΪ16�������֣�!"); 
	   	  return false; 
	   	}
	   	if(temp < 0 || temp > 255){
	   		alert("�����mac��ַ��ʽ����ȷ������xx:xx:xx:xx:xx:xx����ʽ���루xxΪ16�������֣�!"); 
	   		return false;
	   	}
	  }
	  return true;
	}
	
	function CheckForm(){
		var _dealdate = $.trim($("input[@name='dealdate']").val());
		var _userType = $.trim($("select[@name='userType']").val());
		var _loidMsg = $.trim($("input[@name='loidMsg']").val());
		var _netUsername = $.trim($("input[@name='netUsername']").val());
		var _cityId = $.trim($("select[@name='cityId']").val());
		var _bussAccount = $.trim($("input[@name='bussAccount']").val());
		var _bussPwd = $.trim($("input[@name='bussPwd']").val());
		var _wlantype = $.trim($("select[@name='wlantype']").val());
		var _connAccount = $.trim($("input[@name='connAccount']").val());
		var _connPwd = $.trim($("input[@name='connPwd']").val());
		var _ipAddr = $.trim($("input[@name='ipAddr']").val());
		var _hideNode = $.trim($("input[@name='hideNode']").val());
		var _netCheck = $.trim($("input[@name='netCheck']").val());
		var _dnsMsg =  $.trim($("input[@name='dnsMsg']").val());
		var _stbMacMsg = $.trim($("input[@name='stbMacMsg']").val());

		//ҵ������ʱ��
		if (!IsNull(_dealdate, "ҵ������ʱ��")) {
			$("input[@name='dealdate']").focus();
			return false;
		}
		//�ͻ�����
		if('' == _userType || '-1' == _userType){
		alert("��ѡ������");
		$("select[@name='userType']").focus();
		return false;
	}
		
		
		
		//����
		if('' == _cityId || '-1' == _cityId){
		alert("��ѡ������");
		$("select[@name='cityId']").focus();
		return false;
	}
		//ҵ���˺�
		if (!IsNull(_bussAccount, "ҵ���˺�")) {
			$("input[@name='bussAccount']").focus();
			return false;
		}
		//ҵ������
		if (!IsNull(_bussPwd, "ҵ������")) {
			$("input[@name='bussPwd']").focus();
			return false;
		}
		//������ʽ
		
	
	if(_wlantype == 1){
		//�����˺�
		if (!IsNull(_connAccount, "�����˺�")) {
			$("input[@name='connAccount']").focus();
			return false;
		}
		//��������
		if (!IsNull(_connPwd, "��������")) {
			$("input[@name='connPwd']").focus();
			return false;
		}
	}
	if(_wlantype == 3){
		var myReg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
		//Ip��ַ
		if(_ipAddr == ""){
			alert("������Ip��ַ");
			$("input[@name='ipAddr']").focus();
			return false;
		}else{
			if(!myReg.test(_ipAddr)){
				alert('��������Ч��IP��ַ!');
				$("input[@name='ipAddr']").focus();
				return false;
			}
		}
		//����
		if(_hideNode == ""){
			alert("����������");
			$("input[@name='hideNode']").focus();
			return false;
		}else{
			if(!myReg.test(_hideNode)){
				alert('��������Ч������!');
				$("input[@name='hideNode']").focus();
				return false;
			}
		}
		//����
		if(_netCheck == ""){
			alert("����������");
			$("input[@name='netCheck']").focus();
			return false;
		}else{
			if(!myReg.test(_netCheck)){
				alert('��������Ч������!');
				$("input[@name='netCheck']").focus();
				return false;
			}
		}
		//DNS
		if(_dnsMsg == ""){
			alert("������DNS");
			$("input[@name='dnsMsg']").focus();
			return false;
		}else{
			if(!myReg.test(_dnsMsg)){
				alert('��������Ч��DNS!');
				$("input[@name='dnsMsg']").focus();
				return false;
			}
		}	
	}
	
		
		
		//������MAC
		if (!IsNull(_stbMacMsg, "������MAC")) {
			$("input[@name='stbMacMsg']").focus();
			return false;
		}
	if(!checkMAC(_stbMacMsg)){
		$("input[@name='stbMacMsg']").focus();
			return false;
	}
	// ��Сдת�ɴ�д
	
	document.queryStbForm.submit();
	}
</script>
</head>
<body>
	<form id="queryStbForm" name="queryStbForm" method="POST"
		action="<s:url value='/gtms/stb/resource/stbSimulateSheet!sendSheet.action'/>">
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			
			<TR>
				<TD>
					<table id="tblQuery" width="100%" class="querytable">
						<TR>
							<TD bgcolor=#999999>
						<thead>
							<tr>
								<input type="hidden" name="servTypeId"
							value='<s:property value="servTypeId" />'> <input
							type="hidden" name="operateType"
							value='<s:property value="operateType" />'>
								<td colspan="4" class="title_1">��װ����</td>
							</tr>
						</thead>
						<TR>
							<TD class="title_2">ҵ������ʱ��</TD>
							<TD width="30%"><input type="text" name="dealdate"
								value='<s:property value="dealdate" />' readonly class=bk>
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.queryStbForm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="13" height="12"
								border="0" alt="ѡ��">&nbsp; <font color="#FF0000">*</font></TD>
							<TD class="title_2">�ͻ�����</TD>
							<TD width="30%"><select name="userType" class="bk">
									<option selected value="3">==������==</option>
							</select>&nbsp; <font color="#FF0000">*</font></TD>
						</tr>

						<tr>
							<TD class="title_2">LOID</TD>
							<TD><INPUT TYPE='text' name='loidMsg' class=bk value=""></TD>
							<TD class="title_2" align="right" nowrap>�û�����˺�</TD>
							<TD><INPUT TYPE="text" name="netUsername" maxlength=20
								class=bk value=""></TD>
						</tr>
						<tr>
							<TD class=column align="right" width="20%">����</TD>
							<TD width="30%"><s:select list="cityList" name="cityId"
									headerKey="-1" headerValue="��ѡ������" listKey="city_id"
									listValue="city_name" value="cityId" cssClass="bk"></s:select>
								&nbsp; <font color="#FF0000">*</font></TD>
							<TD class="title_2" align="right" nowrap>������ʽ</TD>
							<TD><select name="wlantype" class=bk onchange="change(this)">
								<%String InstArea=LipossGlobals.getLipossProperty("InstArea.ShortName");			
									if("nx_dx".equals(InstArea)){%>
									<option value="1">==�Ž�==</option>
								<%} else{%>
									<option value="1">==�Ž�==</option>
									<option value="2">==·��==</option>
									<option value="3">==��̬IP==</option>
									<option value="4">==DHCP==</option>
								<%}%>
							</select> &nbsp; <font color="#FF0000">*</font>
						</tr>
						<tr>
							<TD class="title_2" align="right">ҵ������</TD>
							<TD><INPUT TYPE="password" NAME="bussPwd" class=bk
								maxlength=20 value="">&nbsp; <font color="#FF0000">*</font></TD>
							<TD class="title_2" align="right" nowrap>ҵ���˺�</TD>
							<TD><INPUT TYPE="text" NAME="bussAccount" maxlength=40
								class=bk value="">&nbsp; <font color="#FF0000">*</font></TD>
						</tr>
						<tr>
							<TD class="title_2" align="right" nowrap>�����˺�</TD>
							<TD><INPUT TYPE="text" NAME="connAccount" maxlength=45
								class=bk  value="">&nbsp; <font color="#FF0000">*</font></TD>
							<TD class="title_2" align="right">��������</TD>
							<TD><INPUT TYPE="password" NAME="connPwd" class=bk
								maxlength=25  value=""></TD>
						</tr>
						<tr>
							<TD class="title_2" align="right">Ip��ַ</TD>
							<TD><INPUT TYPE="text" NAME="ipAddr" class=bk
								maxlength=20 disabled="disabled"  value="����ѡ��������ʽ!">&nbsp; <font color="#FF0000">*</font></TD>
							<TD class="title_2" align="right">����</TD>
							<TD><INPUT TYPE="text" NAME="hideNode" class=bk
								maxlength=20 disabled="disabled"  value="����ѡ��������ʽ!">&nbsp; <font color="#FF0000">*</font></TD>
						</tr>
						<tr>
							<TD class="title_2" align="right">����</TD>
							<TD><INPUT TYPE="text" NAME="netCheck" class=bk
								maxlength=20 disabled="disabled"  value="����ѡ��������ʽ!">&nbsp; <font color="#FF0000">*</font></TD>
							<TD class="title_2" align="right">DNS</TD>
							<TD><INPUT TYPE="text" NAME="dnsMsg" class=bk
								maxlength=20 disabled="disabled"  value="����ѡ��������ʽ!">&nbsp; <font color="#FF0000">*</font></TD>
						</tr>
						<tr>
							<TD class="title_2" align="right">������MAC</TD>
							<TD><INPUT TYPE="text" NAME="stbMacMsg" class=bk style="text-transform: uppercase;"
								maxlength=20 value="">&nbsp; <font color="#FF0000">*</font></TD>
								
							</tr>
						<TR>
							<TD colspan="4" height="35px" align="right" class="foot"><button
									onclick="CheckForm()">&nbsp;���͹���&nbsp;</button></TD>
						</TR>
						</TD>
						</TR>
					</table>
				</TD>
			</TR>
		</table>
	</form>
</body>
</html>
