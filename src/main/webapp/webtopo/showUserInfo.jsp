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
	
	
	//û�й���
	if(fields == null||((String)fields.get("vpn_id")).equals("0")){
		ifgo = "1";
	}
	else{//�ѹ���
		ifgo = "2";
	}
%>
<form method=post action="VPNUserInfoUpdate.jsp" name="frm" onsubmit="return CheckForm()" target="childFrm">
<TABLE width="100%" border=0 cellspacing=1 cellpadding=1 align="center">
	<TR>
		<TD>
<table width="100%" border="0" align="center">
	<TR>
		<TH>�û�����&nbsp;&nbsp;��<%=title%>��</TH>
	</TR>
	<TR>
		<TD><span id="userinfo">������������............</span></TD>
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
	if(!IsNull(obj.ext_vpn_id.value,"�ͻ�����")){
		obj.ext_vpn_id.focus();
		obj.ext_vpn_id.select();
		return false;
	} else if(!IsNull(obj.username.value,"�ͻ�����")){
		obj.username.focus();
		obj.username.select();
		return false;
	} else if(!IsNull(obj.vpn_name.value,"VPN����")){
		obj.vpn_name.focus();
		obj.vpn_name.select();
		return false;
	} else if(!IsNull(obj.linkman.value,"��ϵ��")){
		obj.linkman.focus();
		obj.linkman.select();
		return false;
	} else if(obj.cred_type_id.value!="" && Trim(obj.cred_type_id.value)==""){
		alert("֤������ӦΪ����");
		obj.cred_type_id.focus();
		obj.cred_type_id.select();
		return false;
	} else if(obj.cred_type_id.value!="" && !IsNumber(obj.cred_type_id.value,"֤������")){		
		obj.cred_type_id.focus();
		obj.cred_type_id.select();
		return false;
	} else if(obj.user_state.value!="" && Trim(obj.user_state.value)==""){
		alert("�û�״̬����ӦΪ����");
		obj.user_state.focus();
		obj.user_state.select();
		return false;
	} else if(obj.user_state.value!="" && !IsNumber(obj.user_state.value,"�û�״̬����")){		
		obj.user_state.focus();
		obj.user_state.select();
		return false;
	} else if(!IsNull(obj.complete_time.value,"ʵ�����ʱ��")){		
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

//�û���Ϣ����
function showUserInfo(v){
	//�����û���Ϣʱ
	if(v=="null"){
		var vpn_auto_id = document.all.vpn_auto_id.value;
		var page="getUserInfo.jsp?&vpn_auto_id="+vpn_auto_id+"&tt="+new Date().getTime();
	}
	else{
		var page="getUserInfo.jsp?vpn_id="+v+"&tt="+new Date().getTime();		
	}
	document.all("childFrm").src=page;
}

//�������û���ҳ
function getUserList(offset){
	var page="getUnrelateUserList.jsp?offset="+offset+"&vpn_auto_id=<%=vpn_auto_id%>"+"&tt="+new Date().getTime();
	document.all("childFrm").src=page;
}

//�����û�
function relateUser(){
	//ȷ��ֻ��һ�������
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
		alert("��ѡ�������һ���û���Ϣ");
		return;
	}
	
	var page="VPNUserInfoUpdate.jsp?action=relate&vpn_id="+checkedID+"&vpn_auto_id="+vpn_auto_id+"&tt="+new Date().getTime();
	document.all("childFrm").src=page;
}

//���¹����û�
function reRelateUser(){
	go(1);
}

//����û�
function addUser(){
	var vpn_auto_id = document.all.vpn_auto_id.value;
	var page="VPNUserInfoForm.jsp"+"?tt="+new Date().getTime()+"&vpn_auto_id="+vpn_auto_id;
	document.all("childFrm").src=page;
}
//-->
</script>
