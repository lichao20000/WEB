<%@ page contentType="text/html;charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">
<!--
$(document).ready(function(){

    var username = '<%=(String) request.getParameter("username")%>';
    var dealstaff = '<%=(String) request.getParameter("dealstaff")%>';
    var city_id = '<%=(String) request.getParameter("city_id")%>';
    $("input[@name='username']").val(username);
	$("input[@name='dealstaff']").val(dealstaff);
	$("input[@name='city_id']").val(city_id);
	$("div[@id='username']").html(username);
	$("div[@id='dealstaff']").html(dealstaff);
	//$("div[@id='city_id']").html(city_id);
    var url = "<s:url value='/gwms/resource/itmsIpossInst!instInit.action'/>";
   
	$.post(url,{
        username:username,
        dealstaff:dealstaff,
        city_id:city_id
    },function(ajax){
		$("div[@id='div_init']").html("");
		$("div[@id='div_init']").append(ajax);
	});
});

function checkSerialno(){
	var tmp = $.trim(document.frm.device_serialnumber.value);
	if (tmp.length < 6){
		alert('���������١����6λ�����кŽ��в�ѯ!');
	}
	else{
		getUserinstInfo();
	}
}
function getUserinstInfo(){
	
	var device_serialnumber = $.trim(document.frm.device_serialnumber.value);
	var url = "<s:url value='/gwms/resource/itmsIpossInst!getDeviceInfo.action'/>";
	$.post(url,{
		device_serialnumber:device_serialnumber
	},function(ajax){	
		deviceClear();
	    $("div[@id='div_device']").html("");
		$("div[@id='div_device']").append(ajax);
	});
	document.all("tr_deviceinfo").style.display="";
}


//���ѡ���豸ʱ��Ҫ�ύ������
function deviceOnclick(device_id,city_id,oui,device_serialnumber){
	$("input[@name='deviceId']").val(device_id);
	$("input[@name='deviceCityId']").val(city_id);
	$("input[@name='oui']").val(oui);
	$("input[@name='deviceNo']").val(device_serialnumber);
	$("div[@id='div_bind']").css("display","");
	$("div[@id='div_bind']").html("<input type='button' class=jianbian name='save_btn' value=' �� ʼ �� �� ' onclick='CheckForm()'/>");
		
}

//���²�ѯ�豸ʱ�����ѡ����豸
function deviceClear(){
	$("input[@name='deviceId']").val("");
	$("input[@name='deviceCityId']").val("");
	$("input[@name='oui']").val("");
	$("input[@name='deviceNo']").val("");
	$("div[@id='div_bind']").css("display","none");
}

function CheckForm(){

	var deviceId = $("input[@name='deviceId']").val();
	var username = $("input[@name='username']").val();
	if ("" == deviceId ){
		alert('����ѡ��һ���豸��');
		return false;
	}
	if(username=="" || username=='null' ){
		alert("�û��˺Ų���Ϊ��");
		return false;
	}
	var message = "��ȷ�ϣ��û��ʺţ�"+$("input[@name='username']").val()+"���豸���кţ�"+$("input[@name='deviceNo']").val();
	if (!confirm(message+'���Ƿ��������?')){
		return false;
	}
	
	document.frm.action="<s:url value='/gwms/resource/itmsIpossInst.action'/>";
	document.frm.submit();	
}


function DetailDevice(device_id){
	var strpage = "DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

//�鿴�û���ص���Ϣ
function GoContent(user_id){
	var strpage="HGWUserRelatedInfo.jsp?user_id=" + user_id;
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}


//-->
</SCRIPT>

<link rel="stylesheet" href="../css/listview.css" type="text/css">

<FORM NAME="frm" METHOD="post" ACTION="">
	<!-- ������Ҫ�ύ�� -->
	<!-- ��һҳ�洫���ֵ -->
	<input type="hidden" name="username" value="" />
	<input type="hidden" name="city_id" value="" />
	<input type="hidden" name="dealstaff" value="" />
	<!-- �豸���� -->
	<input type="hidden" name="deviceId" value="" />
	<input type="hidden" name="deviceCityId" value="" />
	<input type="hidden" name="oui" value="" />
	<input type="hidden" name="deviceNo" value="" />
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD HEIGHT=15>

				&nbsp;
			</TD>
		</TR>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							�ֳ���װ
						</td>
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>" width="15"
								height="12">
							<font color="#FF0000">ֻ�а�װ��ͥ/��ҵ�����豸���û���Ҫ�����˽��棬�����û����������</font>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td bgcolor=#999999>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					align="center">
					<tr>
						<th colspan="6">
							�ֳ���װ�豸ȷ��
						</th>
					</tr>
					<TR>
						<td class=column width="25%" align="right">
							�û�����ʺţ�
						</td>
						<td width="25%" bgcolor="#ffffff">
							<div id="username"></div>
						</td>
						<!-- <td class=column width="16%" align="right">
							���أ�
						</td>
						<td width="17%" bgcolor="#ffffff">
							<div id="city_id"></div>
						</td> -->
						<td class=column width="25%" align="right">
							�������û��ʺţ�
						</td>
						<td width="25%" bgcolor="#ffffff">
							<div id="dealstaff"></div>
						</td>
					</TR>
				</table>
			</td>
		</tr>
		<tr id="init">
			<TD>
				<div id="div_init" style="width: 100%; z-index: 1; top: 100px">
					��Ϣ���������Եȡ�����
				</div>
			</TD>
		</tr>
		<TR>
			<TD HEIGHT=30>
			</TD>
		</TR>
		<TR>
			<TD align="center">
				<div id="div_bind" style="display: none" 
					style="width: 100%; z-index: 1; top: 100px">
				</div>
			</TD>
		</TR>
	</TABLE>
</FORM>


<%@ include file="../foot.jsp"%>