<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/prototype.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/jquery.blockUI.js"/>"></SCRIPT>
<style type="text/css">

.bk {
	BACKGROUND-COLOR: #f1f1f1; BORDER-BOTTOM: #eeeeee 1px solid; BORDER-LEFT: #333333 1px solid; BORDER-RIGHT: #eeeeee 1px solid; BORDER-TOP: #333333 1px solid; COLOR: #888888; FONT-FAMILY: "����","Arial"
}
</style>
<SCRIPT LANGUAGE="JavaScript">
<!--

function nextStep(){
	var device_serialnumber = $.trim(document.frm.device_serialnumber.value); 
	var userName = $.trim(document.frm.userName.value); 
	if(device_serialnumber=="�������6λ"){
		device_serialnumber="";
	}
	if(userName=="�����˺�"){
		userName="";
	}
	if(device_serialnumber==""&&userName==""){
		alert("�����������豸���кź��û��˺��е�һ�");
		document.frm.device_serialnumber.focus();
		return false;
	}
	if(device_serialnumber!=""){
		if(device_serialnumber.length < 6){
            alert("�������������6λ�豸���к� !");
            document.frm.device_serialnumber.focus();
            return false;        
	    }
	}
    
	var url = "<s:url value='/gwms/resource/bbmsInst!getReleaseUserCustomerInfo.action'/>";
	$.post(url,{
		device_serialnumber:device_serialnumber,
		userName:userName
	},function(ajax){	
		userClear();
	    $("div[@id='div_user']").html("");
		$("div[@id='div_user']").append(ajax);
	});	
	document.all("tr_userinfo").style.display="";
}

//���ѡ���û�ʱ��Ҫ�ύ������
function userOnclick(user_id,device_id,city_id,deviceNo,username){
	$("input[@name='userId']").val(user_id);
	$("input[@name='deviceId']").val(device_id);
	$("input[@name='userCityId']").val(city_id);
	$("input[@name='deviceNo']").val(deviceNo);
	$("input[@name='username']").val(username);
	$("div[@id='div_bind']").css("display","");
	$("div[@id='div_bind']").html("<input type='button' class=jianbian name='save_btn' value=' �� ʼ �� �� ' onclick='CheckForm()'/>");	
}



//���²�ѯ�û�ʱʱ�����ѡ����û�
function userClear(){
	$("input[@name='userId']").val("");
	$("input[@name='deviceId']").val("");
	$("input[@name='userCityId']").val("");
}

//�鿴�û���ص���Ϣ
function GoContent(user_id){
	var strpage="../../Resource/EGWUserRelatedInfo.jsp?user_id=" + user_id;
	window.open(strpage,"","left=20,top=20,width=800,height=600,resizable=yes,scrollbars=yes");
}


function CheckForm(){

	var userId = $("input[@name='userId']").val();
	
	if ("" == userId ){
		alert('����ѡ��һ���û���');
		return false;
	}
	
	var message = "��ȷ�ϣ��û��ʺţ�"+$("input[@name='username']").val()+"���豸���кţ�"+$("input[@name='deviceNo']").val();
	if (!confirm(message+'���Ƿ��������?')){
		return false;
	}
	
	document.frm.action="bbmsInst!release.action";
	document.frm.submit();	
}


//�����õ�ȥ����
function inputFocus(s,message){
	var value = $.trim(s.value);
	s.style.color='#0000cc';
	if(value==''||value==message){
		
		s.value='';
	}
}

//�����ʧȥ����
function inputBlur(s,message){
	var value = $.trim(s.value);
	if(value==''||value==message){
		s.style.color='#888888';
		s.value=message;
	}
}
//-->
</SCRIPT>

<FORM name="frm" action="bbmsInst!release.action" method="post">
	<!-- ��ʾ��Ϣ -->
	<input type="hidden" name="deviceNo" value="" />
	<input type="hidden" name="username" value="" />
	<!-- ������Ҫ�ύ�� -->
	<input type="hidden" value="" name="userId">
	<input type="hidden" value="" name="deviceId">
	<input type="hidden" value="" name="userCityId">

	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
		<TR>
			<TD HEIGHT="15">
				&nbsp;
			</TD>
		</TR>

		<TR>
			<TD>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
							<div align="center" class="title_bigwhite">
								�ֹ����
							</div>
						</td>
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>" width="15"
								height="12" />
							�������������6λ�豸���кŻ����������û��˺Ž��в�ѯ��
						</td>
					</tr>
				</table>
			</TD>
		</TR>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4">
							�������豸��Ϣ
						</TH>
					</TR>
					<TR bgcolor="#ffffff">
						<TD align="right" width="12%" class="column">
							�豸���к�:
						</td>
						<td width="38%">
							<input type="text" name="device_serialnumber" class="bk" size="35" value="�������6λ"
								onfocus="inputFocus(this,'�������6λ')"
								onblur="inputBlur(this,'�������6λ')">
							<font color="red">*</font>
						</td>
						<TD align="right" width="12%" class="column">
							�û��˺�:
						</td>
						<td  width="38%">
							<input type="text" name="userName" class="bk" size="35" value="�����˺�"
								onfocus="inputFocus(this,'�����˺�')"
								onblur="inputBlur(this,'�����˺�')">
							<font color="red">*</font>
						</td>
					</TR>
					<tr  bgcolor="#ffffff">
						<td colspan="4" align="right" class="column">
							<input type="button" class="jianbian" name="button" value=" ��һ�� "
								style="display: " onclick="nextStep();">					
						</td>
					</tr>


					<!-- <td>
							
						</TD> -->


				</TABLE>
			</TD>
		</TR>

		<TR style="display: none" id="tr_userinfo">
			<TD>
				<div id="div_user" style="width: 100%; z-index: 1; top: 100px">
				</div>
			</TD>
		</TR>
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
<br>
<br>
<%@ include file="../foot.jsp"%>
