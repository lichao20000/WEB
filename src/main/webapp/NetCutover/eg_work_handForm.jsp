<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />
<%
	request.setCharacterEncoding("GBK");
	String servTypeList = HGWUserInfoAct.getEgwServTypeList();

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
		   alert("请输入用户帐号!");
		   return false;
		} else {
				var page = "eg_work_handForm_Info.jsp?oper_type_id=" + document.frm.oper_type.value + "&username="+username + "&serv_type_id=" + document.frm.some_service.value + "&refresh=" + new Date().getTime();
				document.all("childFrm").src = page;
		}
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
				alert("连接没有绑定此端口！");
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
*  防火墙
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
	
	if(vpiid == ""){
		alert("请输入VPI！");
		$("input[@name=vpiid]").focus();
		return false;
	}
	if(vciid == ""){
		alert("请输入VCI！");
		$("input[@name=vciid]").focus();
		return false;
	}	
	
	$("form").attr("action","eg_Work_hand.jsp?service_id=" + service_id);
	$("form").submit();
	return;

}
</SCRIPT>
<link rel="stylesheet" href="../css/listview.css" type="text/css">
<form name="frm" action="" method="post">
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
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
											工单管理
										</div>
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										手工输入帐号，回车获取相关信息.&nbsp;&nbsp;
										注：带有
										<font color=red>*</font>必填,按回车提交。
									</td>
								</tr>
							</table>
						</TD>
					</TR>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR>
									<TH colspan="4">企业网关手工工单</TH>
								</TR>
								<TR bgcolor="#ffffff">
									<TD align="right" width="20%">
										业务类型
									</TD>
									<TD align="left" width="30%">
										<%=servTypeList%>
									</TD>
									<TD align="right" width="20%">
										操作类型
									</TD>
									<TD align="left" width="30%">
										<select name="oper_type" class="bk">
											<option value="-1" selected>==请选择==</option>
										</select>
									</TD>
								</TR>
								<TR bgcolor="#ffffff">
									<TD align="right" width="20%">
										用户帐号
									</TD>
									<TD colspan="3">
										<input type="text" name="user_name" class="bk" ONKEYDOWN="doAction(this);">&nbsp;&nbsp;
										<font color=red>*</font>
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
<SCRIPT LANGUAGE="JavaScript">
<!--
getFirstList();

function getFirstList() {
	var serv_type_id = document.forms[0].some_service.value;
	document.all("childFrm").src = "jt_Work_handForm_list.jsp?serv_type_id=" + serv_type_id + "&refresh=" + new Date().getTime();
}

function showVal(obj) {
	var serv_type_id = obj.value;
	//alert(serv_type_id);
	//alert("jt_Work_handForm_list.jsp?serv_type_id=" + serv_type_id + "&refresh=" + new Date().getTime());
	document.all("childFrm").src = "jt_Work_handForm_list.jsp?serv_type_id=" + serv_type_id + "&refresh=" + new Date().getTime();
}
//-->
</SCRIPT>