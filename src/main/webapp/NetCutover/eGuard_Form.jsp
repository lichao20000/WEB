<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />
<%
	request.setCharacterEncoding("GBK");
	String servTypeList = HGWUserInfoAct.getServTypeList(5);

%>

<script type="text/javascript" src="../Js/jquery.js" /></script>
<SCRIPT LANGUAGE="JavaScript">
function doAction(){
	var initcode = event.keyCode;
	if(initcode == 13){
		window.event.returnValue = false;
		var username = document.frm.user_name.value;
		if(username == ""){
		   document.frm.user_name.focus();
		   alert("�������û��ʺ�!");
		   return false;
		} else {
				var page = "eGuard_Form_Info.jsp?oper_type_id=" + document.frm.oper_type.value + "&serv_type_id=10" + "&username="+username  + "&refresh=" + new Date().getTime();
				//alert(page);
				document.all("childFrm").src = page;
		}
		
	}
}
function doSubmit(username){
	if(username == ""){
	   document.frm.user_name.focus();
	   alert("�������û��ʺ�!");
	   return false;
	} else {
		var page = "eGuard_Form_Info.jsp?oper_type_id=" + document.frm.oper_type.value + "&serv_type_id=10" + "&username="+username  + "&refresh=" + new Date().getTime();
		//alert(page);
		document.all("childFrm").src = page;
	}
}
function checkItem(item,num){

	var o  = $("input[@name=LanInterface]");
	var o2 = $("input[@name=LanInterface1]");
	
	if(item == "LanInterface"){
		if(o[num].checked == false){	
			o2[num].checked = false;				
		}
	} else {
		if(o2[num].checked){
			if(o[num].checked == false){
				alert("����û�а󶨴˶˿ڣ�");
				o2[num].checked = false;
				return;
			}
		}	
	}
}

function showChild(){

	var user_name = $("input[@name=user_name]").val();
	if(user_name != ""){		
		$("form").attr("action","deviceinfo.action");
		$("form").submit();	
	}

}


/**
*
*  ����ǽ
*
**/
function checkwall(){
		var obj = $("input[@name=firewall]");
		$(obj).each(function(i){;	
			if(obj[i].checked){
				$("span[@id=wall]").show();
			} else {
				$("span[@id=wall]").hide();
			}
		});
}

/**
*
* 
*
**/

function checkUI(){

	var obj = $("input[@name=uiface]");
	$(obj).each(function(i){;	
		if(obj[i].checked){
			$("span[@id=ip]").show();
		} else {
			$("span[@id=ip]").hide();
		}
	});
}
/**
*
*
*
*/
function shownum(item){
	
	var obj = $("input[@name=" + item + "]");
	
		$(obj).each(function(i){;	
		if(obj[i].checked){
			$("span[@id=" + "id_" + item + "]").show();
		} else {
			$("span[@id=" + "id_" + item + "]").hide();
		}
	});


}

function send(service_id){
	var vpiid = $("input[@name=vpiid]").val();
	var vciid = $("input[@name=vciid]").val();
	var passwd = $("input[@name=passwd]").val();
	var conf_passwd = $("input[@name=conf_passwd]").val();
	
	if ($("select[@name=oper_type]").val() == "-1") {
		alert("��ѡ��������ͣ�");
		$("select[@name=oper_type]").focus();
		return false;
	}
	
	if(passwd == ""){
		alert("�������û����룡");
		$("input[@name=passwd]").focus();
		return false;
	}

	if(conf_passwd == ""){
		alert("������ȷ�����룡");
		$("input[@name=conf_passwd]").focus();
		return false;
	}

	if(conf_passwd != passwd){
		alert("�û������ȷ�����벻һ�£����������룡");
		$("input[@name=passwd]").focus();
		return false;
	}
	
	if(vpiid == ""){
		alert("������VPI��");
		$("input[@name=vpiid]").focus();
		return false;
	}
	if(vciid == ""){
		alert("������VCI��");
		$("input[@name=vciid]").focus();
		return false;
	}	
	
	for (var i = 1; i < 6; i++) {
		if ($("input[@name=wanPort"+i+"]").val() == "") {
			alert("�����������˿ڣ�"+i);
			return false;
		}
	}
	$("form").attr("action","eGuard_Form_ResInfo.jsp?service_id=" + service_id);
	$("form").submit();
	showMsgDlg("����Ѱ������PVC...");
	//return;

}

function showMsgDlg(strMsg){

	w = document.body.clientWidth;
	h = document.body.clientHeight;

	l = (w-250)/2;
	t = (h-60)/2;
	PendingMessage.style.left = l;
	PendingMessage.style.top  = t;
	PendingMessage.style.display="";
	document.all.txtLoading.innerText=strMsg;
}
function closeMsgDlg(){
	PendingMessage.style.display="none";
}

</SCRIPT>
<link rel="stylesheet" href="../css/listview.css" type="text/css">
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle">
			<span id=txtLoading style="font-size:14px;font-family: ����"></span>
		</td>
		<!-- <td align="right">
			<span id=btn><input type="button" value="�ر�" onclick="closeMsgDlg()"></span>
		</td> -->
	</tr>
</table>
</center>
</div>

<form name="frm" action="" method="post">
	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%">
		<TR>
			<TD HEIGHT="20">
				&nbsp;
			</TD>
		</TR>
		<TR>
			<TD>
				<TABLE width="98%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162">
										<div align="center" class="title_bigwhite">
											��������
										</div>
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										ע������
										<font color=red>*</font>����,�ɰ��س��ύ��
									</td>
								</tr>
							</table>
						</TD>
					</TR>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR>
									<TH colspan="4">���ӱ���</TH>
								</TR>
								<TR bgcolor="#ffffff">
									<!--<TD align="right" width="20%">
										ҵ������
									</TD>
									<TD align="left" width="30%">
									��ͥ���ص��ӱ���
										<%//=servTypeList%>
									</TD>-->
									<TD align="right" width="15%">
										��������
									</TD>
									<TD align="left" width="35%">
										<select name="oper_type" class="bk" style="width:35%;align=left">
											<option value="31" selected>����</option>
											<option value="32">����</option>
										</select>&nbsp;<font color=red>*</font>
									</TD>
									<TD align="right" width="15%">
										�û��ʺ�
									</TD>
									<TD align="left" width="35%">
										<input type="text" name="user_name" class="bk" ONKEYDOWN="doAction(this);" size=20>&nbsp;
										<font color=red>*</font>&nbsp;<INPUT NAME="" CLASS="" TYPE="button"  value="�� ��" onClick="javascript:doSubmit(document.frm.user_name.value);">
									</TD>
								</TR>
							</TABLE>
							<div id="idBody">
							</div>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD HEIGHT=20>
				&nbsp;
				<IFRAME ID=childFrm name="childFrm" STYLE="display:none"></IFRAME>
			</TD>
		</TR>
	</TABLE>
</FORM>
<%@ include file="../foot.jsp"%>
