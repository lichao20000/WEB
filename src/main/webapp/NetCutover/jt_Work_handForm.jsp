<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />
<%
	request.setCharacterEncoding("GBK");
	String servTypeList = HGWUserInfoAct.getServTypeList(1);

%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function doAction(){
	//var initcode = event.keyCode;
	//if(initcode == 13){
		//window.event.returnValue = false;
		var username = document.frm.account.value;
		if(username == ""){
		   document.frm.account.focus();
		   alert("�������û��ʺ�!");
		   return false;
		} else {
				var page = "jt_Work_hand_userInfo.jsp?oper_type_id=" + document.frm.oper_type.value + "&username="+username + "&serv_type_id=" + document.frm.some_service.value;
				document.all("childFrm").src = page;
		}
		
	//}
}
function doActionEnt(){
	var initcode = event.keyCode;
	if(initcode == 13){
		window.event.returnValue = false;
		var username = document.frm.account.value;
		if(username == ""){
		   document.frm.account.focus();
		   alert("�������û��ʺ�!");
		   return false;
		} else {
				var page = "jt_Work_hand_userInfo.jsp?oper_type_id=" + document.frm.oper_type.value + "&username="+username + "&serv_type_id=" + document.frm.some_service.value;
				document.all("childFrm").src = page;
		}
		
	}
}
function CheckForm(){

		var _type = document.frm.some_service.value;
		
		if(_type == "107"){
			if(document.frm.new_oui.value == ""){
				alert("������������豸��OUI��");
				document.frm.new_oui.focus();
				return false;
			}
			if(document.frm.new_device_serialnumber.value == ""){
				alert("������������豸��Serialnumber��");
				document.frm.new_device_serialnumber.focus();
				return false;
			}
			document.frm.action = "jt_work_hand_changdevice.jsp";
			document.frm.submit();
			
		} else {
			if(document.frm.username.value == ""){
				alert("�������û����ƣ�");
				document.frm.username.focus();
				return false;
			}
			if(document.frm.pwd.value == ""){
				alert("�������û����룡");
				document.frm.pwd.focus();
				return false;
			}
			var wan_type = document.frm.wan_type.value
			if("1"==wan_type||"2"==wan_type||"3"==wan_type||"4"==wan_type){
				if(document.frm.vpiid.value == ""){
					alert("������VpiID�ţ�");
					document.frm.vpiid.focus();
				return false;
			}
				if(document.frm.vciid.value == ""){
					alert("������VciID�ţ�");
					document.frm.vciid.focus();
					return false;
				}
			}else{
				if(document.frm.vlanid.value == ""){
					alert("������VlanID�ţ�");
					document.frm.vlanid.focus();
					return false;
				}
			}
					
			document.frm.action = "jt_Work_hand.jsp";
			document.frm.submit();
		}
}

function showChild(){
	
	if(document.frm.account.value != ""){
				var page = "jt_Work_hand_userInfo.jsp?oper_type_id=" + document.frm.oper_type.value + "&username="+ document.frm.account.value + "&serv_type_id=" + document.frm.some_service.value;
				document.all("childFrm").src = page;
	}
	
}



//-->
</SCRIPT>

<link rel="stylesheet" href="../css/listview.css" type="text/css">
<FORM name="frm" action="" method="post">
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
											��������
										</div>
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										�ֹ������ʺţ��س���ȡ�����Ϣ.&nbsp;&nbsp;
										ע������
										<font color=red>*</font>����,���س��ύ��
									</td>
								</tr>
							</table>
						</TD>
					</TR>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR>
									<TH colspan="4">�ֹ�����</TH>
								</TR>
								<TR bgcolor="#ffffff">
									<TD align="right" width="20%">
										ҵ������
									</TD>
									<TD align="left" width="30%">
										<%=servTypeList%>
									</TD>
									<TD align="right" width="20%">
										��������
									</TD>
									<TD align="left" width="30%">
										<select name="oper_type" class="bk">
											<option value="-1" selected>==��ѡ��==</option>
										</select>
									</TD>
								</TR>
								<TR bgcolor="#ffffff">
									<TD align="right" width="20%">
										�û��ʺ�
									</TD>
									<TD>
										<input type="text" name="account" class="bk" ONKEYDOWN="doActionEnt(this);">&nbsp;&nbsp;
										<font color=red>*</font>
									</TD>
									<TD align="left" width="" colspan="3">
										<input type="button" name="button" value=" ȷ �� " onclick="doAction(this);">
									</TD>
								</TR>
								<TR>
									<TD colspan="4" height="20" bgcolor="#ffffff">
											&nbsp;
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