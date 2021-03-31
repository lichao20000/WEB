<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>自助绑定查询</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>

<script language="javascript">
<!--//

function checkForm(){
	if(document.frm.username.value == ''){
		alert("请输入用户账号");
		return false;
	}else{
		return true;
	}
}

//提交查询
function doQuery(){
	if(false == checkForm()){
		return false;
	}
	document.getElementById("dataUser").innerHTML = "正在查询，请稍后..."
	var url = '<s:url value="/gwms/report/autoBindReport!queryUser.action"/>';
	var dataList = document.getElementById("dataList");
	$.post(url,{
		username:document.frm.username.value
    },function(mesg){
    	document.getElementById("dataUser").innerHTML = mesg;
    });
}
//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["dataForm"]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block"
     			//如果用户的浏览器是NetScape
     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
      			//如果用户的浏览器是IE
     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
   			 }
   		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
    		tempobj.style.display="block"
		}
	}
}

$(window).resize(function(){
	dyniframesize();
}); 

function doUpload(){
	
	var username=document.frm.userfile.value;; 
	if(username.replace(/(^\s*)|(\s*$)/g, "") == ""){
		alert("请先选择文件！");
		return false;
	}
	document.frm.submit();
}

function radioSelect(type){
	if('1' == type){
		document.getElementById("user1").style.display="none";
		document.getElementById("user2").style.display="none";
		document.getElementById("dataUser").style.display="none";
		
		document.getElementById("list1").style.display="";
		document.getElementById("list2").style.display="";
		document.getElementById("list3").style.display="";
		document.getElementById("dataList").style.display="";
	}else{
		document.getElementById("list1").style.display="none";
		document.getElementById("list2").style.display="none";
		document.getElementById("list3").style.display="none";
		document.getElementById("dataList").style.display="none";
		
		document.getElementById("user1").style.display="";
		document.getElementById("user2").style.display="";
		document.getElementById("dataUser").style.display="";
	}
}

//<input type=button value=另存为 onclick="cp()">
function cp(){
	var oControlRange = document.body.createControlRange();   
	var tables = document.getElementById("dataTable");  
	oControlRange.addElement(tables);
	oControlRange.select();
	//document.execCommand("Copy");
	document.execCommand('Saveas',false,'c:\\test.xls')
}

//-->
</script>
</head>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align=center height="auto">
	<TR><TD HEIGHT=20>&nbsp;</TD></TR>
	<tr>
		<td>
		<table width="100%" height="30" border="0" cellspacing="0"
			cellpadding="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite"
					ondblclick="ShowHideLog()">自助绑定查询</td>
				<td>&nbsp;
					<input type="radio" id="radioUser" name="queryType" value="1" onclick="radioSelect('2')">用户账号查询
					<input type="radio" id="radioList" name="queryType" value="2" onclick="radioSelect('1')">文件方式查询
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<form name=frm action="<s:url value="/gwms/report/autoBindReport!queryUserList.action"/>" 
			method="POST" enctype="multipart/form-data" target="dataForm">
		<table width="100%" border="0" cellspacing="1" cellpadding="2"
			bgcolor="#999999">
			<TR><TH colspan="4">用户自助绑定情况查询</TH></TR>
			<tr bgcolor=#ffffff id="user1">
				<td class=column align=right width="20%">用户账号</td>
				<td width="30%"><input type="text" name="username"></td>
				<td class=column width="20%"></td>
				<td ></td>
				<!-- 
				<td class=column>设备序列号</td>
				<td><input type="text" name="serialnumber"></td>
				 -->
			</tr>
			<tr bgcolor=#ffffff id="user2">
				<td class=green_foot colspan=4 align=right>
					<input type="button" value=" 查 询 " onclick="doQuery()">
				</td>
			</tr>
			<tr bgcolor=#ffffff id="list1">
				<td align="right" width="20%" CLASS="column">提交文件</td>
				<td colspan="3">
					<input type="file" size="60" name="userfile"/>
				</td>
			</tr>
			<tr bgcolor=#ffffff id="list3" >
					<td CLASS="column" align="right">注意事项</td>
					<td colspan="3">
					1、需要导入的文件格式为Excel。
					 <br>
					2、文件的第一行为标题行，即【用户账号】。
					 <br>
					3、文件只有一列。
					 <br>
			</tr>
			<tr bgcolor=#ffffff id="list2">
				<td class=green_foot colspan=4 align=right>
					<input type="button" value=" 提 交 " onclick="doUpload()">
				</td>
			</tr>
		</table>
		</form>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<div id="dataList" align=center>
				<iframe name="dataForm" FRAMEBORDER=0 SCROLLING=NO width="100%" style="display:">
				</iframe>
			</div>
			<div id="dataUser" align=center>
			</div>
		</td>
	</tr>
	<tr>
		<td height=20>&nbsp;</td>
	</tr>
</TABLE>
<%@ include file="../../foot.jsp"%>
</body>
</html>
<script language="javascript">
<!--//
	document.getElementById("radioUser").checked = true;
	radioSelect('2');
//-->
</script>