<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�û���֤</title>
<base target="_self" />
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">

	var status="<s:property value='status' />";
	
	$(function(){
	   if('0' ==status){  // 0 ��ʾ��Ч������Ҫ��ʾʧЧһ��
	      $("tr[@id='reason']").hide();
		}
	});
	
	function validate() {
		if($('input[name=userName]').val()=="" || $('input[name=password]').val()=="" ){
			$("td[@id='validate_msg']").html("��������ȷ���û���������");
			return;
		}
		
		if('1'==status){
			var reason=$('input[name=reason]').val();
		}
		else{
			var reason='��Ч';
		}
		
		var url = "<s:url value='/gtms/stb/resource/picStrategyQuery!validateUser.action'/>";
		 $.post(url,{  
		    username:$('input[name=username]').val(),
		    password:$('input[name=password]').val()
			},function(ajax){
				var s = ajax+reason;
				if(""!=s){
					window.returnValue = s;
					window.close();
		    	}else{
		    		window.returnValue = "�û��������벻��ȷ";
		    		window.close();
		    	}
		    });
	}
	function closewin() {
		window.returnValue = "closewin";
		window.close();
	}
</SCRIPT>
<style>
span {
	position: static;
	border: 0;
}
</style>
</head>
<body>
<form action="" align="center">
<TABLE width="100%" class="querytable" align="center">
	
	
	<TR >
		<td id="validate_msg" align="right" width="100%" style="color:#FF0000;"></td>
	</TR>
</table>
<TABLE width="100%" class="querytable" align="center">
	<TR>
		<TD align="right" class="title_2" width="15%">�û���</TD>
		<TD width="35%"><input type="text" name="username" value="" size="25" maxlength="40" class="bk"/></TD>
	</TR>
	<TR>
		<TD align="right" class="title_2" width="15%">�� ��</TD>
		<TD width="35%"><input type="password" name="password" value="" size="27" maxlength="40" class="bk"/></TD>
	</TR>
	
	<TR id="reason">
		<TD align="right" class="title_2" width="15%">ԭ��</TD>
		<TD width="35%"><input type="text" name="reason" value="" size="27" maxlength="40" class="bk"/></TD>
	</TR>
</table>
			<div  style="margin-top: 20px;margin-left: 80px"> 
					<button onclick="javascript:validate();">ȷ ��</button>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<button onclick="javascript:closewin();">ȡ ��</button> 
			</div>
</form>
</body>