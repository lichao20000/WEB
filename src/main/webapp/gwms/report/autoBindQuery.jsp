<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>�����󶨲�ѯ</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>

<script language="javascript">
<!--//

function checkForm(){
	if(document.frm.username.value == ''){
		alert("�������û��˺�");
		return false;
	}else{
		return true;
	}
}

//�ύ��ѯ
function doQuery(){
	if(false == checkForm()){
		return false;
	}
	document.getElementById("dataUser").innerHTML = "���ڲ�ѯ�����Ժ�..."
	var url = '<s:url value="/gwms/report/autoBindReport!queryUser.action"/>';
	var dataList = document.getElementById("dataList");
	$.post(url,{
		username:document.frm.username.value
    },function(mesg){
    	document.getElementById("dataUser").innerHTML = mesg;
    });
}
//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["dataForm"]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block"
     			//����û����������NetScape
     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
      			//����û����������IE
     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
   			 }
   		}
		//�����趨�Ĳ���������֧��iframe�����������ʾ����
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
		alert("����ѡ���ļ���");
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

//<input type=button value=���Ϊ onclick="cp()">
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
					ondblclick="ShowHideLog()">�����󶨲�ѯ</td>
				<td>&nbsp;
					<input type="radio" id="radioUser" name="queryType" value="1" onclick="radioSelect('2')">�û��˺Ų�ѯ
					<input type="radio" id="radioList" name="queryType" value="2" onclick="radioSelect('1')">�ļ���ʽ��ѯ
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
			<TR><TH colspan="4">�û������������ѯ</TH></TR>
			<tr bgcolor=#ffffff id="user1">
				<td class=column align=right width="20%">�û��˺�</td>
				<td width="30%"><input type="text" name="username"></td>
				<td class=column width="20%"></td>
				<td ></td>
				<!-- 
				<td class=column>�豸���к�</td>
				<td><input type="text" name="serialnumber"></td>
				 -->
			</tr>
			<tr bgcolor=#ffffff id="user2">
				<td class=green_foot colspan=4 align=right>
					<input type="button" value=" �� ѯ " onclick="doQuery()">
				</td>
			</tr>
			<tr bgcolor=#ffffff id="list1">
				<td align="right" width="20%" CLASS="column">�ύ�ļ�</td>
				<td colspan="3">
					<input type="file" size="60" name="userfile"/>
				</td>
			</tr>
			<tr bgcolor=#ffffff id="list3" >
					<td CLASS="column" align="right">ע������</td>
					<td colspan="3">
					1����Ҫ������ļ���ʽΪExcel��
					 <br>
					2���ļ��ĵ�һ��Ϊ�����У������û��˺š���
					 <br>
					3���ļ�ֻ��һ�С�
					 <br>
			</tr>
			<tr bgcolor=#ffffff id="list2">
				<td class=green_foot colspan=4 align=right>
					<input type="button" value=" �� �� " onclick="doUpload()">
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