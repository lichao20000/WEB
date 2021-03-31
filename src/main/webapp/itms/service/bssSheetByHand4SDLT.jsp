<%--ɽ����ͨ�ֹ�����	Date: 20151230--%>

<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<HEAD>
<title>�ն�ҵ���·�</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../css/listview.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
$(function() {
	var dstr = "";
	var d = new Date();
	
	dstr += d.getFullYear();
	if(d.getMonth()+1<10)
	{
		dstr += '0';
	}
	dstr += d.getMonth()+1;
	
	if(d.getDate()<10)
	{
		dstr += '0';
	}
	dstr += d.getDate();
	
	if(d.getHours()<10)
	{
		dstr += '0';
	}
	dstr += d.getHours();
	
	if(d.getMinutes()<10)
	{
		dstr += '0';
	}
	dstr += d.getMinutes()+"00";
	$("input[@name='obj.dealDate']").val(dstr);
});

function showSheet(objId,name){
    if($("#"+objId).attr("checked")) {
		document.getElementById(name).style.display="";
	} else {
        document.getElementById(name).style.display="none";
    }
}

//���LOID�Ƿ����
function checkLoid()
{
	if("" == $("input[@name='obj.loid']").val().trim())
	{
		alert("LOID����Ϊ�գ�");
		$("input[@name='obj.loid']").focus();
		return false;
	}
	var url = "<s:url value='/itms/service/bssSheetByHand4SDLT!checkLoid.action'/>";
	$.post(url,{
		"obj.userType":$("select[@name='obj.userType']").val().trim(),
		"obj.loid":$("input[@name='obj.loid']").val().trim()
	},function(ajax){
		if("000" == ajax)
		{
			alert("LOID����ʹ�á�");
		}
		else
		{
			$("#netServTypeId").attr("checked",false); 
			var relt = ajax.split("|");
			$("input[@name='obj.userOperateId']").val(relt[4]);
			$("select[@name='obj.cityId']").val(relt[8]);
			$("input[@name='obj.officeId']").val(relt[9]);
			$("input[@name='obj.areaId']").val(relt[10]);
			$("select[@name='obj.accessStyle']").val(relt[11]);
			$("input[@name='obj.linkman']").val(relt[12]);
			$("input[@name='obj.linkPhone']").val(relt[13]);
			$("input[@name='obj.email']").val(relt[14]);
			$("input[@name='obj.mobile']").val(relt[15]);
			$("input[@name='obj.linkAddress']").val(relt[16]);
			$("input[@name='obj.linkmanCredno']").val(relt[17]);
			$("select[@name='obj.specId']").val(relt[20]);
			$("#netServTypeId").attr("checked",true); 
			$("input[@name='obj.netOperateId']").val(relt[22]);
			$("input[@name='obj.netUsername']").val(relt[23]);
			$("input[@name='obj.netPasswd']").val(relt[24]);
			$("input[@name='obj.netVlanId']").val(relt[25]);
			$("select[@name='obj.netWanType']").val(relt[26]);
			
			showSheet('netServTypeId','internetBssSheet');
		}
	});
}

//�ύҵ��
function doBusiness()
{
	var loidvalue = "";

	if("2"!=$("select[@name='obj.accessStyle']").val().trim())
	{
		if("" == $("input[@name='obj.loid']").val().trim())
		{
			alert("LOID����Ϊ�ա�");
			$("input[@name='obj.loid']").focus();
			return false;
		}
		loidvalue = $("input[@name='obj.loid']").val().trim();	
	}
	if("-1" == $("select[@name='obj.cityId']").val().trim())
	{
		alert("���ز���Ϊ�ա�");
		return false;
	}
	if("-1" == $("select[@name='obj.accessStyle']").val().trim())
	{
		alert("�ն˽������Ͳ���Ϊ�ա�");
		return false;
	}
	if("-1" == $("select[@name='obj.userOperateId']").val().trim())
	{
		alert("ҵ��������Ϊ�ա�");
		return false;
	}
	if("" == $("input[@name='obj.netUsername']").val().trim())
	{
		alert("����˺Ż�ר�߽���Ų���Ϊ�ա�");
		$("input[@name='obj.netUsername']").focus();
		return false;
	}
	if("" == $("input[@name='obj.netPasswd']").val().trim())
	{
		alert("������벻��Ϊ�ա�");
		$("input[@name='obj.netPasswd']").focus();
		return false;
	}
	if("" == $("input[@name='obj.netVlanId']").val().trim())
	{
		alert("VLANDID����Ϊ�ա�");
		$("input[@name='obj.netVlanId']").focus();
		return false;
	}
	if("-1" == $("select[@name='obj.netWanType']").val().trim())
	{
		alert("������ʽ����Ϊ�ա�");
		$("select[@name='obj.netWanType']").focus();
		return false;
	}
	if($("select[@id='noPort']").val()!=undefined && "-1" == $("select[@id='noPort']").val().trim())
	{
		alert("��ͨ�˿ڲ���Ϊ�ա�");
		return false;
	}
	
	var netPorts = document.frm.netPort;
	var netPort="";
	for(var i=0;i<netPorts.length;i++){
		if(netPorts[i].checked){
			netPort += ","+netPorts[i].value;
		}
	}
	netPort=netPort.substring(1,netPort.length);
	if(""==netPort){
		alert("��ͨ�˿ڲ���Ϊ�ա�");
		return false;
	}
	
	var specId = $("select[@name='obj.specId']").val().trim().split("|")[2];
	var url = "<s:url value='/itms/service/bssSheetByHand4SDLT!doBusiness.action'/>";
	//�һ���ť
	$("button[@name='subBtn']").attr("disabled", true);
	$.post(url,{
		"obj.cmdId":$("input[@name='obj.cmdId']").val().trim(),
		"obj.authUser":$("input[@name='obj.authUser']").val().trim(),
		"obj.authPwd":$("input[@name='obj.authPwd']").val().trim(),
		"obj.userServTypeId":$("input[@name='obj.userServTypeId']").val().trim(),
		"obj.userOperateId":$("select[@name='obj.userOperateId']").val().trim(),
		"obj.dealDate":$("input[@name='obj.dealDate']").val().trim(),
		"obj.userType":$("select[@name='obj.userType']").val().trim(),
		"obj.loid":loidvalue,
		"obj.cityId":$("select[@name='obj.cityId']").val().trim(),
		"obj.officeId":$("input[@name='obj.officeId']").val().trim(),
		"obj.areaId":$("input[@name='obj.areaId']").val().trim(),
		"obj.accessStyle":$("select[@name='obj.accessStyle']").val().trim(),
		"obj.linkman":$("input[@name='obj.linkman']").val().trim(),
		"obj.linkPhone":$("input[@name='obj.linkPhone']").val().trim(),
		"obj.email":$("input[@name='obj.email']").val().trim(),
		"obj.mobile":$("input[@name='obj.mobile']").val().trim(),
		"obj.linkAddress":$("input[@name='obj.linkAddress']").val().trim(),
		"obj.linkmanCredno":$("input[@name='obj.linkmanCredno']").val().trim(),
		"obj.specId":specId,
		
		"obj.netServTypeId":$("input[@name='obj.netServTypeId'][checked]").val().trim(),
		"obj.netOperateId":$("select[@name='obj.userOperateId']").val().trim(),
		"obj.netUsername":$("input[@name='obj.netUsername']").val().trim(),
		"obj.netPasswd":$("input[@name='obj.netPasswd']").val().trim(),
		"obj.netVlanId":$("input[@name='obj.netVlanId']").val().trim(),
		"obj.netPort":netPort,
		"obj.netWanType":$("select[@name='obj.netWanType']").val().trim()
	},function(ajax){
		alert(ajax);
		//�ָ���ť
		$("button[@name='subBtn']").attr("disabled", false);
//		doReset();
	});
}

function changNetPort(){
	var spec=$("select[@name='obj.specId']").val();
	var lNum= parseInt(spec.split("|")[0]);
	var wNum= parseInt(spec.split("|")[1]);
	var input="";
	for(var x=1;x<lNum+1;x++){
		input+="<input type='checkbox' name='netPort' value='L"+x+"' class=bk />L"+x+"&nbsp;";
	}
	for(var y=1;y<wNum+1;y++){
		input+="<input type='checkbox' name='netPort' value='SSID"+y+"' class=bk />SSID"+y+"&nbsp;";
	}
	input+="&nbsp;<font color='#FF0000'>*</font>";
	$("TD[@id='netPorts']").html("");
	$("TD[@id='netPorts']").html(input);
}

function doReset(){
	$("input[@name='obj.loid']").val("");
	$("select[@name='obj.userOperateId']").val("-1");
	$("select[@name='obj.cityId']").val("-1");
	$("input[@name='obj.officeId']").val("");
	$("input[@name='obj.areaId']").val("");
	$("select[@name='obj.accessStyle']").val("-1");
	$("input[@name='obj.linkman']").val("");
	$("input[@name='obj.linkPhone']").val("");
	$("input[@name='obj.email']").val("");
	$("input[@name='obj.mobile']").val("");
	$("input[@name='obj.linkAddress']").val("");
	$("input[@name='obj.linkmanCredno']").val("");
	$("select[@name='obj.specId']").val("-1");
	$("input[@name='obj.netServTypeId'][checked]").val();
	$("input[@name='obj.netUsername']").val("");
	$("input[@name='obj.netPasswd']").val("");
	$("input[@name='obj.netVlanId']").val("");
	$("select[@name='obj.netPort']").val("-1");
	$("select[@name='obj.netWanType']").val("-1");
	
	$("#netServTypeId").attr("checked",false);
	document.getElementById("internetBssSheet").style.display="none";
}

</script>
</HEAD>
<body>
<FORM NAME="frm" action="" method="post" onsubmit="return false;">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR><TD HEIGHT="20">&nbsp;</TD></TR>
	<TR>
		<TD>
			<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
				<TR>
					<TD colspan="4">
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162"><div align="center" class="title_bigwhite">�ֹ�����</div></td>
								<td><img src="../../images/attention_2.gif" width="15" height="12"></td>
							</tr>
						</table>
					</TD>
				</TR>
				
				<TR>
					<TD colspan="4" bgcolor="#999999">
						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
							<tr align="left">
								<input type="hidden" id="userServTypeId" name = "obj.userServTypeId" value="20">
								<input type="hidden"  name = "obj.cmdId" value="FROMWEB-0000002">
								<input type="hidden"  name = "obj.authUser" value="itms">
								<input type="hidden"  name = "obj.authPwd" value="123">
								<td colspan="4" class="green_title_left">
								�û����Ϲ���
								</td>
							</tr>
							<tbody id="jirRuBssSheet" >
								<TR bgcolor="#FFFFFF" >
									<TD width="15%" class=column align="right">�û����ͣ�</TD>
									<TD width="35%" >
										<SELECT name="obj.userType"  class="bk" disabled>
											<OPTION value="1">��ͥ����</OPTION>
										</SELECT>
										<font color="#FF0000">*</font>
									</TD>
									<TD class=column align="right" nowrap width="15%">����ʱ�䣺</TD>
									<TD width="35%" >
										<input type="text" name="obj.dealDate" class=bk value="" disabled><font color="#FF0000">*</font>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" >
									<TD width="15%" class=column align="right">LOID:</TD>
									<TD width="35%" >
										<input type="text" id="loid" name="obj.loid" class=bk value="">
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<button type="button" name="subButton" onclick="checkLoid();">���LOID�Ƿ����</button>
									</TD>
									<TD class=column align="right" nowrap width="15%">����:</TD>
									<TD width="35%" >
										<s:select list="cityList" name="obj.cityId"
										headerKey="-1" headerValue="��ѡ������" listKey="city_id"
										listValue="city_name" value="cityId" cssClass="bk"></s:select>
										<font color="#FF0000">*</font>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" >
									<TD class=column align="right" nowrap width="15%">�����ʾ:</TD>
									<TD width="35%" ><input type="text" name="obj.officeId" class=bk value=""></TD>
									<TD class=column align="right" nowrap width="15%">�����ʾ:</TD>
									<TD width="35%" ><input type="text" name="obj.areaId" class=bk value=""></TD>
								</TR>
								<TR bgcolor="#FFFFFF" >
									<TD class=column align="right" nowrap width="15%">�ն˽�������:</TD>
									<TD width="35%" >
										<SELECT name="obj.accessStyle" class="bk" >
											<OPTION value="-1">--��ѡ��--</OPTION>
											<OPTION value="1">ADSL</OPTION>
											<OPTION value="2">LAN</OPTION>
											<OPTION value="3">EPON</OPTION>
											<OPTION value="4">GPON</OPTION>
										</SELECT>
										&nbsp;<font color="#FF0000">*</font>
									</TD>
									<TD width="15%" class=column align="right">�ն˹��:</TD>
									<TD id='specid' width="35%" >
										<s:select list="specIdList" name="obj.specId" headerKey="-1" headerValue="��ѡ��" 
										listKey="spec" listValue="spec_name" value="spec_name"  
										onchange="changNetPort();" cssClass="bk">
										</s:select>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" >
								<TD width="15%" class=column align="right">ҵ����:</TD>
									<TD width="35%" colspan='3'>
										<SELECT name="obj.userOperateId"  class="bk">
											<OPTION value="-1">--��ѡ��--</OPTION>
											<OPTION value="1">����</OPTION>
											<OPTION value="3">����</OPTION>
										</SELECT>&nbsp;<font color="#FF0000">*</font>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" >
									<TD class=column align="right" nowrap width="15%">��ϵ�绰:</TD>
									<TD width="35%" >
										<input type="text" name="obj.linkPhone" class=bk value="">
									</TD>
									<TD width="15%" class=column align="right">��ϵ��:</TD>
									<TD width="35%" >
										<input type="text" name="obj.linkman" class=bk value="">
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" >
									<TD class=column align="right" nowrap width="15%">�ֻ�:</TD>
									<TD width="35%" >
										<input type="text" name="obj.mobile" class=bk value="" maxlength="15">
									</TD>
									<TD class=column align="right" nowrap width="15%">��������:</TD>
									<TD width="35%" >
										<input type="text" name="obj.email" class=bk value="">
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" >
									<TD class=column align="right" nowrap width="15%">֤������:</TD>
									<TD width="35%" >
										<input type="text" name="obj.linkmanCredno" class=bk value="">
									</TD>
									<TD width="15%" class=column align="right">��ַ:</TD>
									<TD  width="35%" >
										<input type="text" name="obj.linkAddress" size="55" class=bk value="">
									</TD>
								</TR>
							</tbody>
							
							<tr align="left" >
								<td colspan="4" class="green_title_left">
									<input type="checkbox" name="obj.netServTypeId" value="22" id="netServTypeId" onclick="showSheet('netServTypeId','internetBssSheet');"/>
									���ҵ�񹤵�
								</td>
							</tr>
							<tbody id="internetBssSheet" style="display:none">
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right" nowrap width="15%">����ʺŻ�ר�߽����:</TD>
									<TD width="35%" >
										<input type="text" name="obj.netUsername" class=bk value="">&nbsp;<font color="#FF0000">*</font>
									</TD>
									<TD width="15%" class=column align="right">�������:</TD>
									<TD width="35%">
										<input type="text" name="obj.netPasswd" class=bk value="">&nbsp;<font color="#FF0000">*</font>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right" nowrap width="15%">VLANID:</TD>
									<TD width="35%" >
										<input type="text" name="obj.netVlanId" class=bk value="">&nbsp;<font color="#FF0000">*</font>
									</TD>
									<TD width="15%" class=column align="right">������ʽ��</TD>
									<TD width="35%" >
										<SELECT name="obj.netWanType" class="bk">
											<option value="-1">==��ѡ��==</option>
											<option value="1">==�Ž�==</option>
											<option value="2">==·��==</option>
										</SELECT>&nbsp;&nbsp;&nbsp;<font color="#FF0000">*</font>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD width="15%" class=column align="right">��ͨ�˿�:</TD>
									<TD id="netPorts" width="35%" colspan='3'>
											<SELECT id="noPort" name="obj.netPort" class="bk">
												<option value="-1">==����ѡ���ն˹��==</option>
											</SELECT>&nbsp;&nbsp;&nbsp;<font color="#FF0000">*</font>
									</TD>
								</TR>
							</tbody>
							
							<TR align="left" id="doBiz" >
								<TD colspan="4" class=foot align="right" nowrap>
									<button type="button" name="subBtn" onclick="doBusiness();">��&nbsp;&nbsp;��</button>
									&nbsp;&nbsp;&nbsp;&nbsp;
									<button type="button" name="subBtn" onclick="doReset();">��&nbsp;&nbsp;��</button>&nbsp;
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>
</body>
</html>
<%@ include file="../../../foot.jsp"%>