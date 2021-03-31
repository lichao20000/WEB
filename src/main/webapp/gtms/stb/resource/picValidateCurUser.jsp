<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>用户验证</title>
<base target="_self" />
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">

	var status="<s:property value='status' />";
	
	$(function(){
	   if('0' ==status){  // 0 表示生效，不需要显示失效一栏
	      $("tr[@id='reason']").hide();
		}
	});
	
	function validate() {
		if($('input[name=userName]').val()=="" || $('input[name=password]').val()=="" ){
			$("td[@id='validate_msg']").html("请输入正确的用户名和密码");
			return;
		}
		
		if('1'==status){
			var reason=$('input[name=reason]').val();
		}
		else{
			var reason='生效';
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
		    		window.returnValue = "用户名和密码不正确";
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
		<TD align="right" class="title_2" width="15%">用户名</TD>
		<TD width="35%"><input type="text" name="username" value="" size="25" maxlength="40" class="bk"/></TD>
	</TR>
	<TR>
		<TD align="right" class="title_2" width="15%">密 码</TD>
		<TD width="35%"><input type="password" name="password" value="" size="27" maxlength="40" class="bk"/></TD>
	</TR>
	
	<TR id="reason">
		<TD align="right" class="title_2" width="15%">原因</TD>
		<TD width="35%"><input type="text" name="reason" value="" size="27" maxlength="40" class="bk"/></TD>
	</TR>
</table>
			<div  style="margin-top: 20px;margin-left: 80px"> 
					<button onclick="javascript:validate();">确 定</button>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<button onclick="javascript:closewin();">取 消</button> 
			</div>
</form>
</body>