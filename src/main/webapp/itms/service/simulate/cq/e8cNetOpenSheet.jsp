<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<title>BSSģ�⹤��</title>
<script type="text/javascript">
function IsNull(strValue,strMsg){

	if(Trim(strValue).length>0) return true;

	else{

		alert(strMsg+'����Ϊ��');

		return false;

	}

}
function Trim(strValue){

	var v = strValue;

	var i = 0;

	while(i<v.length){

	  if(v.substring(i,i+1)!=' '){

		v = v.substring(i,v.length) 

		break;

	  }

	  i = i + 1;

	}



	i = v.length;

	while(i>0){

	  if(v.substring(i-1,i)!=' '){

	    v = v.substring(0,i);

		break;

	  }	

	  i = i - 1;

	}



	return v;

}
	$(function() {
		parent.dyniframesize();
	});
	
	
	//���LOID,����
	function checkLoid()
	{
		var loid = $("input[@name='loid']").val().trim();
		if("" == loid){
			return false;
		}
		
		var url = "<s:url value='/itms/service/simulateSheet4cq!checkLoid4CQ.action'/>";
		$.post(url,{
			"loid":loid
		},function(ajax){
			if("0" == ajax)
			{
				$("select[@name='cityId']").val("-1");
				$("select[@name='usertype']").val("0");
				$("select[@name='devicewan']").val('4');
			}
			else
			{
				var relt = ajax.split("|");
				var cityId = $("select[@name='cityId']");
				var usertype = $("select[@name='usertype']");
				var devicewan = $("select[@name='devicewan']");
				devicewan.val(relt[0]);
				cityId.val(relt[1]);
				usertype.val(relt[2]);
			}
		}); 

	}
	
	
	function CheckForm() {
		var loid = $("input[@name='loid']");
		var ordertime = $("input[@name='ordertime']");
		var cityId = $("select[@name='cityId']");
		var orderself = $("select[@name='orderself']");
		var devicewan = $("select[@name='devicewan']");
		var usertype = $("select[@name='usertype']");
		var subareacode = $("input[@name='subareacode']");
		var olduserid = $("input[@name='olduserid']");
		var deviceid = $("input[@name='deviceid']");
		
		var adaccount = $("input[@name='adaccount']");
		var username = $("input[@name='username']");
		var useraddress = $("input[@name='useraddress']");
		var contactperson = $("input[@name='contactperson']");
		var phonenumber = $("input[@name='phonenumber']");
		var migration = $("select[@name='migration']");
		
		/* ҵ���ֶ� */
		var rgmode = $("select[@name='rgmode']");
		var adpassword = $("input[@name='adpassword']");
		var checkBoxNet = document.frm.netPort;
		var rgportid = $("input[@name='rgportid']");
		var vlanid = $("input[@name='vlanid']");
		var AFTRAddress = $("input[@name='AFTRAddress']");
		var IPV6Enable = $("select[@name='IPV6Enable']");
		//loid
		if (!IsNull(loid.val(), "�û�ID(�߼�ID)")) {
			loid.focus();
			return false;
		}
		//����ʱ��
		if (!IsNull(ordertime.val(), "����������")) {
			ordertime.focus();
			return false;
		}
		//����
		if ('' == cityId.val() || '-1' == cityId.val()) {
			alert("��ѡ������");
			cityId.focus();
			return false;
		}
		//����˺�
		if (!IsNull(adaccount.val(), "����˺�")) {
			adaccount.focus();
			return false;
		}
		
		var portflag = false;
		var netPortStr = "";
		for(var i=0; i<checkBoxNet.length; i++){
			if(checkBoxNet[i].checked){
				if(portflag){
					netPortStr = netPortStr+",";
				}
				netPortStr = netPortStr + checkBoxNet[i].value;
				portflag = true;	
			}
		}
		//�������ҵ��󶨶˿ڱ��
		if(!portflag)
		{
			alert("�������ҵ��󶨶˿ڱ�Ų���Ϊ�ա�");
			return false;
		}
		rgportid.val(netPortStr);
		
		//��� UVLAN
		if (!IsNull(vlanid.val(), "��� VLAN")) {
			vlanid.focus();
			return false;
		}
		if("1"==IPV6Enable.val() && ""==AFTRAddress.val()){
			alert("IPV6ΪDSLITEģʽʱ,��Ҫ��дAFTR��ַ");
			AFTRAddress.focus();
			return false;
		}
		if("1"==rgmode.val() && ""==adpassword.val()){
			alert("��������Ϊ·��ģʽʱ,��Ҫ��дPppoe����");
			AFTRAddress.focus();
			return false;
		}
		
		document.frm.submit();
	}

	function change_select() {
		var wlantype = $("select[@name='wlantype']");
		if ('3' == wlantype.val()) {
			$("input[@name='ipaddress']").val("");
			$("input[@name='code']").val("");
			$("input[@name='dns']").val("");
			$("input[@name='netway']").val("");
			$("tr[@id='dis1']").css("display", "");
			$("tr[@id='dis2']").css("display", "");
		} else {
			$("input[@name='ipaddress']").val("");
			$("input[@name='code']").val("");
			$("input[@name='dns']").val("");
			$("input[@name='netway']").val("");
			$("tr[@id='dis1']").css("display", "none");
			$("tr[@id='dis2']").css("display", "none");
		}
	}
</script>
</head>
<body>
	<form name="frm"
		action="<s:url value='/itms/service/simulateSheet4cq!sendSheet4CQ.action'/>"
		method="post">
		<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
			<TR>
				<TD>
					<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
						align="center">
						<TR>
							<TD bgcolor=#999999>
								<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
									<TR class="green_title">
										<TD colspan="4"><input type="hidden" name="servTypeId"
											value='22'> <input
											type="hidden" name="operateType"
											value='1'> 
											<input type="hidden" name="rgportid" value=''> 
											<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%"
												BORDER="0">
												<TR>
													<TD align="center"><font size="2"><b>���������Ϣ</b></font></TD>
												</TR>
											</TABLE></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>�û�ID(�߼�ID)</TD>
										<TD colspan="3"><INPUT TYPE="text" NAME="loid" maxlength=100
											class=bk value="" onblur="checkLoid()" >&nbsp; <font color="#FF0000">*
										</font></TD>
									</TR>
									
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" width="20%">����ʱ��</TD>
										<TD width="30%"><input type="text" name="ordertime"
											value='<s:property value="dealdate" />' readonly class=bk>
											<img name="shortDateimg"
											onClick="WdatePicker({el:document.frm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="../../images/dateButton.png" width="15" height="12"
											border="0" alt="ѡ��">&nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" width="20%">����</TD>
										<TD width="30%"><s:select list="cityList" name="cityId"
												headerKey="-1" headerValue="��ѡ������" listKey="city_id"
												listValue="city_name" value="cityId" cssClass="bk"></s:select>
											&nbsp; <font color="#FF0000">*</font></TD>
									</TR>




									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>���ŷ�ʽ</TD>
										<TD width="30%"><select name="orderself" class="bk">
												<option value="0">==�û�������װ==</option>
												<option value="1">==������Ա����==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" nowrap>�豸��������</TD>
										<TD width="30%"><select name="devicewan" class="bk">
											<!-- onchange="change_select()"> -->
												<option value="4">==GPON==</option>
												<option value="3">==EPON==</option>
												<option value="1">==ADSL2+==</option>
												<option value="2">==LAN==</option>
												<option value="5">==VDSL2==</option>
												<option value="6">==EPONE==</option>
												<option value="7">==GPONE==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>�û�����</TD>
										<TD width="30%"><select name="usertype" class="bk">
												<option value="0">==�����û�==</option>
												<option value="1">==�����û�==</option>
												<!-- <option value="3">==������STB==</option> -->
										</select> &nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" nowrap>�־�</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="subareacode" maxlength=10
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>�û��ɵ�ID</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="olduserid" maxlength=100
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>�豸ID��</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="deviceid" maxlength=200
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>����˺�</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="adaccount" maxlength=50
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>�������</TD>
										<TD width="30%"><INPUT TYPE="password" NAME="adpassword" maxlength=50
											class=bk value="">&nbsp; <font color="#FF0000">·��ģʽ�±���
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>�û���ַ</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="useraddress" maxlength=200
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>�ƻ��������</TD>
										<TD width="30%"><select name="migration" class="bk">
												<option value="0">==���ƻ�==</option>
												<option value="1">==�ƻ�==</option>
												<option value="2">==�ƻ�ɾ��==</option>
												<option value="3">==�ƻ�����==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>��ϵ��</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="contactperson" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>��ϵ�绰</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="phonenumber" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									
									<%-- <TR class="green_title">
										<TD colspan="4"><input type="hidden" name="servTypeId"
											value='22'> <input
											type="hidden" name="operateType"
											value='1'> <input
											type="hidden" name="username"
											value='<s:property value="loid" />'>
											<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%"
												BORDER="0">
												<TR>
													<TD align="center"><font size="2">�������</font></TD>
												</TR>
											</TABLE></TD>
									</TR> --%>
									<!-- ҵ����� -->
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>��������</TD>
										<TD width="30%"><select name="rgmode" class="bk">
												<option value="2">==�Ž�ģʽ==</option>
												<option value="1">==·��ģʽ==</option>
												<!-- <option value="3">==��̬IPģʽ==</option>
												<option value="4">==DHCP==</option> -->
										</select> &nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" nowrap>�û�����</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="username" maxlength=50
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>ҵ��󶨶˿ڱ��</TD>
										<TD id="netTD1" width="30%">
											<input type="checkbox" name="netPort" value="LAN1" class=bk />L1&nbsp;
											<input type="checkbox" name="netPort" value="LAN2" class=bk />L2&nbsp;
											<input type="checkbox" name="netPort" value="LAN3" class=bk />L3&nbsp;
											<input type="checkbox" name="netPort" value="LAN4" class=bk />L4&nbsp;
											<input type="checkbox" name="netPort" value="SSID1" class=bk />SSID1&nbsp;
											<input type="checkbox" name="netPort" value="SSID2" class=bk />SSID2&nbsp;
											<input type="checkbox" name="netPort" value="SSID3" class=bk />SSID3&nbsp;
											<input type="checkbox" name="netPort" value="SSID4" class=bk />SSID4&nbsp;<br/>
											<input type="checkbox" name="netPort" value="LAN5" class=bk />L5&nbsp;
											<input type="checkbox" name="netPort" value="LAN6" class=bk />L6&nbsp;
											<input type="checkbox" name="netPort" value="LAN7" class=bk />L7&nbsp;
											<input type="checkbox" name="netPort" value="LAN8" class=bk />L8&nbsp;
										&nbsp;<font color="#FF0000">*
										</TD>
										<TD width="20%" class=column align="right" nowrap>��� VLAN</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="vlanid" maxlength=10
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>vpi������ֵ</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="pvcvpi" maxlength=10
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>vci������ֵ</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="pvcvci" maxlength=10
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>���ն�����ģʽ</TD>
										<TD width="30%"><select name="multidevicemode" class="bk">
												<option value="0">==������==</option>
												<option value="1">==ģʽ==</option>
												<option value="2">==ģʽ2==</option>
												<option value="4">==DHCP==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" nowrap>·��ģʽ��ͬʱ����PC��</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="pcnumbers" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>ɾ�û���Ϣ��ǩ</TD>
										<TD width="30%"><select name="keepuserinfo" class="bk">
												<option value="1">�����û���Ϣ</option>
												<option value="0">ɾ���û���Ϣ</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" nowrap>�Ƿ�����IPV6</TD>
										<TD width="30%"><select name="IPV6Enable" class="bk">
												<option value="0">IPV6Ϊ������</option>
												<option value="1">DSLITEģʽ</option>
												<option value="2">����˫ջģ��</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>AFTR��ַ</TD>
										<TD width="30%" ><INPUT TYPE="text" NAME="AFTRAddress" maxlength=64
											class=bk value="">&nbsp; <font color="#FF0000">IPV6ΪDSLITEģʽʱ,��Ҫ���ֵ
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>WAN����DHCP����</TD>
										<TD width="30%"><select name="dhcpenable" class="bk">
												<option value="-1">����ԭ������</option>
												<option value="0">DHCPΪ������</option>
												<option value="1">��Ӧwan��DHCP����</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>Awifi�Ķ˿ڱ��</TD>
										<TD colspan="3"><INPUT TYPE="text" NAME="awifiportid" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">����м���Ӣ�Ķ��Ÿ���,awifi�Ķ˿ںŲ��ܺͿ��ҵ��Ķ˿ں����ظ�
										</font></TD>
									</TR>
									
									
									
									<TR>
										<TD colspan="4" align="right" class="green_foot"><button
												onclick="CheckForm()">&nbsp;���͹���&nbsp;</button></TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</form>
</body>
</html>