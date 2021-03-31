<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import ="com.linkage.litms.common.database.Cursor, com.linkage.litms.common.database.DataSetBean,java.util.*"%>
<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<%	
	String ifgo = "";
	String vpn_id = "";
	String vpn_auto_id = request.getParameter("vpn_auto_id");
	String title = request.getParameter("title");

	String strSql="select vpn_id from vpn_auto_customer where vpn_auto_id="+ vpn_auto_id;
	Cursor cursor = DataSetBean.getCursor(strSql);
	Map fields = cursor.getNext();
	if (fields != null) {
		vpn_id = (String)fields.get("vpn_id");
	}
	
	
	//没有关联
	if(fields == null||((String)fields.get("vpn_id")).equals("0")){
		ifgo = "1";
	}
	else{//已关联
		ifgo = "2";
	}
%>
<form method=post action="VPNUserInfoUpdate.jsp" name="frm" onsubmit="return CheckForm()" target="childFrm">
<TABLE width="100%" border=0 cellspacing=1 cellpadding=1 align="center">
	<TR>
		<TD>
<table width="100%" border="0" align="center">
	<TR>
		<TH>用户对象&nbsp;&nbsp;〖<%=title%>〗</TH>
	</TR>
	<TR>
		<TD><span id="userinfo">正在载入数据............</span></TD>
	</TR>
</TABLE>

</TD></TR></TABLE>
</form>
<IFRAME ID="childFrm" name="childFrm" SRC="" STYLE="display:none"></IFRAME>

<script language="JavaScript">
<!--
var ifgo = "<%=ifgo%>";
go(ifgo);

function go(v){
	if(v==1){
		var page="getUnrelateUserList.jsp?vpn_auto_id=<%=vpn_auto_id%>"+"&tt="+new Date().getTime();
		document.all("childFrm").src=page;
	}else{	
		var page="getUserInfo.jsp?vpn_id=<%=vpn_id%>"+"&tt="+new Date().getTime();
		document.all("childFrm").src=page;		
	}
}

function CheckForm() {
	var obj = document.frm;
	if(!IsNull(obj.ext_vpn_id.value,"客户编码")){
		obj.ext_vpn_id.focus();
		obj.ext_vpn_id.select();
		return false;
	} else if(!IsNull(obj.username.value,"客户名称")){
		obj.username.focus();
		obj.username.select();
		return false;
	} else if(!IsNull(obj.vpn_name.value,"VPN名称")){
		obj.vpn_name.focus();
		obj.vpn_name.select();
		return false;
	} else if(!IsNull(obj.linkman.value,"联系人")){
		obj.linkman.focus();
		obj.linkman.select();
		return false;
	} else if(obj.cred_type_id.value!="" && Trim(obj.cred_type_id.value)==""){
		alert("证件类型应为数字");
		obj.cred_type_id.focus();
		obj.cred_type_id.select();
		return false;
	} else if(obj.cred_type_id.value!="" && !IsNumber(obj.cred_type_id.value,"证件类型")){		
		obj.cred_type_id.focus();
		obj.cred_type_id.select();
		return false;
	} else if(obj.user_state.value!="" && Trim(obj.user_state.value)==""){
		alert("用户状态编码应为数字");
		obj.user_state.focus();
		obj.user_state.select();
		return false;
	} else if(obj.user_state.value!="" && !IsNumber(obj.user_state.value,"用户状态编码")){		
		obj.user_state.focus();
		obj.user_state.select();
		return false;
	} else if(!IsNull(obj.complete_time.value,"实际完成时间")){		
		obj.complete_time.focus();
		obj.complete_time.select();
		return false;
	} else {
		document.frm.hidcomplete_time.value = DateToDes(document.frm.complete_time.value);
		return true;
	}	
}

function DateToDes(v){
	//alert("DateToDes in");
	if(v != ""){
		pos = v.indexOf("-");
		if(pos != -1 && pos == 4){
			y = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		pos = v.indexOf("-");
		if(pos != -1){
			m = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		if(v.length>0)
			d = parseInt(v);

		dt = new Date(m+"/"+d+"/"+y);
		var s  = dt.getTime();
		
		return s/1000;
	}
	else
		return 0;
}

//用户信息呈现
function showUserInfo(v){
	//新增用户信息时
	if(v=="null"){
		var vpn_auto_id = document.all.vpn_auto_id.value;
		var page="getUserInfo.jsp?&vpn_auto_id="+vpn_auto_id+"&tt="+new Date().getTime();
	}
	else{
		var page="getUserInfo.jsp?vpn_id="+v+"&tt="+new Date().getTime();		
	}
	document.all("childFrm").src=page;
}

//待关联用户翻页
function getUserList(offset){
	var page="getUnrelateUserList.jsp?offset="+offset+"&vpn_auto_id=<%=vpn_auto_id%>"+"&tt="+new Date().getTime();
	document.all("childFrm").src=page;
}

//关联用户
function relateUser(){
	//确保只有一条的情况
	var relateRadioObj = document.getElementsByName("relateRadio");
	var vpn_auto_id = document.all.vpn_auto_id.value;
	var flag = true;
	var checkedID;
	
	for(i = 0; i < relateRadioObj.length; i++){
	 if(relateRadioObj[i].checked==true){
		checkedID = relateRadioObj[i].value;
		flag = false;
		break;
	 }
	}
	
	if(flag){
		alert("请选择或新增一个用户信息");
		return;
	}
	
	var page="VPNUserInfoUpdate.jsp?action=relate&vpn_id="+checkedID+"&vpn_auto_id="+vpn_auto_id+"&tt="+new Date().getTime();
	document.all("childFrm").src=page;
}

//重新关联用户
function reRelateUser(){
	go(1);
}

//添加用户
function addUser(){
	var vpn_auto_id = document.all.vpn_auto_id.value;
	var page="VPNUserInfoForm.jsp"+"?tt="+new Date().getTime()+"&vpn_auto_id="+vpn_auto_id;
	document.all("childFrm").src=page;
}
//-->
</script>
