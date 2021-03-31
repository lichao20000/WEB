<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������Ӫ��������������ѯ</title>

<link href="<s:url value="/css/css_blue.css"/>" rel="stylesheet" type="text/css">

<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.blockUI.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>


<SCRIPT LANGUAGE="JavaScript">

$(function(){
	init();
});

// ��ͨ��ʽ�ύ
function init(){
	var form = document.getElementById("selectForm");
	form.action = "<s:url value='/gtms/stb/resource/serverManage!serverQueryList.action'/>";
	//form.target = "dataForm";
	form.submit();
}


function query(){

	showAddPart(false);
	
	var serverName = trim($("input[@name='serverName']").val());
	var serverUrl = trim($("input[@name='serverUrl']").val());
	
	//if("" == serverName){
	//	alert("��������������ƣ�");
	//	$("input[@name='serverName']").focus();
	//	return false;
	//}
	//if("" == serverUrl){
	//	alert("�����������URL��");
	//	$("input[@name='serverUrl']").focus();
	//	return false;
	//}
	
	var form = document.getElementById("selectForm");
	form.action = "<s:url value='/gtms/stb/resource/serverManage!serverQueryList.action'/>";
	form.target = "dataForm";
	form.submit();
}


//���
function add() {
	document.all("DeviceTypeLabel").innerHTML = "";
	clearData();
	//disableLabel(false);
	readFlag(false);
	showAddPart(true);
}


// �����������
function clearData(){
	document.getElementsByName("serverNameAdd")[0].value = "";
	document.getElementsByName("fileTypeAdd")[0].value = -1;
	document.getElementsByName("accessUserAdd")[0].value = "";
	document.getElementsByName("accessPasswdAdd")[0].value = "";
	document.getElementsByName("serverUrlAdd")[0].value = "";
	
	document.getElementById("actLabel").innerHTML="���";
	document.getElementsByName("Action")[0].value="add";
}


// ĳЩ�ֶβ�����༭
//function disableLabel(tag){
//	$("select[@name='vendorIdAdd']").attr("disabled",tag);
//	$("select[@name='deviceModelIdAdd']").attr("disabled",tag);
//}


// ����ҳ��������������
function showAddPart(tag){
	if(tag)
		$("table[@id='addTable']").show();
	else
		$("table[@id='addTable']").hide();
}


function save(){
	var serverId = $("input[@name='serverId']").val();
	var serverNameAdd = $("input[@name='serverNameAdd']").val();
	var fileTypeAdd = $("select[@name='fileTypeAdd']").val();
	var accessUserAdd = $("input[@name='accessUserAdd']").val();
	var accessPasswdAdd = $("input[@name='accessPasswdAdd']").val();
	var serverUrlAdd = $("input[@name='serverUrlAdd']").val();
	
	var addForm = document.getElementById("addForm");
	var action = addForm.Action.value;
	
	/** ���� */
	if("add" == action){
		if("" == $.trim(serverNameAdd)){
			alert("���������Ʋ���Ϊ�գ�");
			$("input[@name='serverNameAdd']").focus();
			return;
		}
		if("" == $.trim(fileTypeAdd) || "-1" == fileTypeAdd){
			alert("��ѡ����������ͣ�");
			return;
		}
		
		if("" == $.trim(serverUrlAdd)){
			alert("������URL����Ϊ�գ�");
			$("input[@name='serverUrlAdd']").focus();
			return;
		}
		
		var url = "<s:url value='/gtms/stb/resource/serverManage!addServer.action'/>";
		$.post(url,{
			serverNameAdd:encodeURIComponent(serverNameAdd),
			fileTypeAdd:fileTypeAdd,
			accessUserAdd:encodeURIComponent(accessUserAdd),
			accessPasswdAdd:accessPasswdAdd,
			serverUrlAdd:serverUrlAdd
		},function(ajax){
			alert(ajax);
			if(ajax.indexOf("�ɹ�") != -1){
				var form1 = document.getElementById("selectForm");  // �����ύ��ѯ
				form1.action = "<s:url value='/gtms/stb/resource/serverManage!serverQueryList.action'/>";
				reset();  /** �ύ��������� */
				form1.submit();
			}
		});
	/** �༭ */
	}else{
		if("" == $.trim(serverNameAdd)){
			alert("���������Ʋ���Ϊ�գ�");
			$("input[@name='serverNameAdd']").focus();
			return;
		}
		if("" == $.trim(fileTypeAdd) || "-1" == fileTypeAdd){
			alert("��ѡ����������ͣ�");
			return;
		}
		
		if("" == $.trim(serverUrlAdd)){
			alert("������URL����Ϊ�գ�");
			$("input[@name='serverUrlAdd']").focus();
			return;
		}
		var url = "<s:url value='/gtms/stb/resource/serverManage!editServer.action'/>";
		$.post(url,{
			serverId:serverId,
			serverNameAdd:encodeURIComponent(serverNameAdd),
			fileTypeAdd:fileTypeAdd,
			accessUserAdd:encodeURIComponent(accessUserAdd),
			accessPasswdAdd:accessPasswdAdd,
			serverUrlAdd:serverUrlAdd
		},function(ajax){
			alert(ajax);
			if(ajax.indexOf("�ɹ�") != -1){
				var form2 = document.getElementById("selectForm");  // �����ύ��ѯ
				form2.action = "<s:url value='/gtms/stb/resource/serverManage!serverQueryList.action'/>";
				reset();  /** �ύ��������� */
				form2.submit();
			}
		});
	}
	
	showAddPart(false);
}


//function disabledButton(tag){
//	$("button[@name='saveButton']").attr("disabled",tag);
//}

function readFlag(flag){
	document.getElementById("serverNameAddId").readOnly = flag; 
}


// ȥ�����ҵĿո�
function trim(str){
     return str.replace(/(^\s*)|(\s*$)/g,"");
}


// ����
function reset(){
	document.selectForm.serverName.value = "";
	document.selectForm.serverUrl.value = "";
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
$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

</SCRIPT>
</head>


<body>
<form name="selectForm" action="" target="dataForm">
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD >
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
            	����ǰ��λ�ã���������Ӫ�������������
		</TD>
	</TR>
</TABLE>
<TABLE width="98%" class="querytable" align="center">
	<tr><th colspan="4" id="thTitle" class="title_1">��������Ӫ�������������</th></tr>
	<TR id="tr21" STYLE="display:">
		<TD width="10%"  class="title_2">����������</TD>
		<TD width="40%">
			<input type="text" name="serverName" value="" size="20"  class="bk"/>
		</TD>
		<TD width="10%"  class="title_2">������URL</TD>
		<TD width="40%">
			<input type="text" name="serverUrl" value="" size="20"  class="bk"/>
		</TD>
	</TR>
	<tr align="right">
		<td colspan="4" class="foot" align="right">
			<div class="right">
				<button name="queryButton" align="right" onclick="javascript:query()"> �� ѯ </button>
				<button name="queryButton" align="right" onclick="javascript:reset()"> �� �� </button>
				<button name="queryButton" align="right" onclick="javascript:add()"> �� �� </button>
			</div>
		</td>
	</tr>
</TABLE>
</form>

<!-- չʾ���  begin-->
<TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center">
	<TR>
		<TD bgcolor=#999999 id="idData"><iframe id="dataForm"
			name="dataForm" height="0" frameborder="0" scrolling="no"
			width="100%" src=""></iframe></TD>
	</TR>
</TABLE>
<!-- չʾ���  end-->
			
<!-- ��Ӻͱ༭ begin -->
<FORM id="addForm" name="addForm" target="" method="post" action="">
<TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center" id="addTable" style="display: none">
	<TR>
		<TD>
		<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="allDatas" class="querytable">
			<TR>
				<TH colspan="4" align="center" class="title_1" ><SPAN id="actLabel">���</SPAN><SPAN id="DeviceTypeLabel"></SPAN>������</TH>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD class="title_2" align="right">����������</TD>
				<TD colspan=3>
					<input type="text" id="serverNameAddId" name="serverNameAdd" value="" size="30" class="bk"/>&nbsp;&nbsp;&nbsp;
					<font color="#FF0000">*</font>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF" >
				<TD class="title_2" align="right">����������</TD>
				<TD colspan=3>
					<select name="fileTypeAdd" class="bk">
						<option value="-1">--��ѡ��--</option>
						<option value="1">--�ϴ�--</option>
						<option value="2">--����--</option>
					</select>&nbsp;&nbsp;&nbsp;
					<font color="#FF0000">*</font>
				</TD>
			</TR>

			<TR bgcolor="#FFFFFF">
				<TD class="title_2" align="right">�û���</TD>
				<TD colspan=3>
					<INPUT TYPE="text" NAME="accessUserAdd" maxlength=30 class=bk size=20>&nbsp;&nbsp;&nbsp;
					<font color="#FF0000">*</font>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD class="title_2" align="right">����</TD>
				<TD colspan=3>
					<INPUT TYPE="text" NAME="accessPasswdAdd" class=bk >&nbsp;&nbsp;&nbsp;
					<font color="#FF0000">*</font>
				</TD>
			</TR>
			
			<TR bgcolor="#FFFFFF">
				<TD class="title_2" align="right">������URL</TD>
				<TD colspan=3>
					<input type="text" name="serverUrlAdd" value="" size="50"  class="bk"/>&nbsp;&nbsp;&nbsp;
					<font color="#FF0000">*</font>&nbsp;(����http://192.168.28.192:9090/FileServer)
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<TR>
		<TD>
		<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" class="querytable">
			<TR bgcolor="#FFFFFF">
				<TD class="title_1">
					<div class="right">
					<INPUT TYPE="button" name="saveButton" onClick="javascript:save()" value=" �� �� " class=jianbian>&nbsp;&nbsp;
					<INPUT TYPE="hidden" name="Action" value=""> 
					</div>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>

</TABLE>
<input type='hidden' name="serverId" value="" />
</FORM>
<!-- ��Ӻͱ༭  end -->
</body>
</html>
