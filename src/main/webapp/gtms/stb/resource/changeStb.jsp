<%@page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

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
		var _cityId = $.trim($("select[@name='cityId']").val());
		var _bussAccount = $.trim($("input[@name='bussAccount']").val());
		var _stbMacMsg = $.trim($("input[@name='stbMacMsg']").val());

		//ҵ������ʱ��
		if (!IsNull(_dealdate, "ҵ������ʱ��")) {
			$("input[@name='dealdate']").focus();
			return false;
		}
		
		
	
		//����
		if('' == _cityId || '-1' == _cityId){
		alert("��ѡ������");
		$("select[@name='cityId']").focus();
		return false;
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
								<td colspan="4" class="title_1">���������й���</td>
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
							<TD class=column align="right" width="20%">����</TD>
							<TD width="30%"><s:select list="cityList" name="cityId"
									headerKey="-1" headerValue="��ѡ������" listKey="city_id"
									listValue="city_name" value="cityId" cssClass="bk"></s:select>
								&nbsp; <font color="#FF0000">*</font></TD>
						</tr>
						<tr>
							
							<TD class="title_2" align="right" nowrap>ҵ���˺�</TD>
							<TD><INPUT TYPE="text" NAME="bussAccount" maxlength=20
								class=bk value="">&nbsp; <font color="#FF0000">*</font></TD>
							<TD class="title_2" align="right">������MAC</TD>
							<TD><INPUT TYPE="text" NAME="stbMacMsg" class=bk
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
