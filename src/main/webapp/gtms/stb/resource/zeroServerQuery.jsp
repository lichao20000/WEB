<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒运营画面服务器管理查询</title>

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

// 普通方式提交
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
	//	alert("请输入服务器名称！");
	//	$("input[@name='serverName']").focus();
	//	return false;
	//}
	//if("" == serverUrl){
	//	alert("请输入服务器URL！");
	//	$("input[@name='serverUrl']").focus();
	//	return false;
	//}
	
	var form = document.getElementById("selectForm");
	form.action = "<s:url value='/gtms/stb/resource/serverManage!serverQueryList.action'/>";
	form.target = "dataForm";
	form.submit();
}


//添加
function add() {
	document.all("DeviceTypeLabel").innerHTML = "";
	clearData();
	//disableLabel(false);
	readFlag(false);
	showAddPart(true);
}


// 清楚缓存数据
function clearData(){
	document.getElementsByName("serverNameAdd")[0].value = "";
	document.getElementsByName("fileTypeAdd")[0].value = -1;
	document.getElementsByName("accessUserAdd")[0].value = "";
	document.getElementsByName("accessPasswdAdd")[0].value = "";
	document.getElementsByName("serverUrlAdd")[0].value = "";
	
	document.getElementById("actLabel").innerHTML="添加";
	document.getElementsByName("Action")[0].value="add";
}


// 某些字段不允许编辑
//function disableLabel(tag){
//	$("select[@name='vendorIdAdd']").attr("disabled",tag);
//	$("select[@name='deviceModelIdAdd']").attr("disabled",tag);
//}


// 隐藏页面下面的添加区域
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
	
	/** 新增 */
	if("add" == action){
		if("" == $.trim(serverNameAdd)){
			alert("服务器名称不能为空！");
			$("input[@name='serverNameAdd']").focus();
			return;
		}
		if("" == $.trim(fileTypeAdd) || "-1" == fileTypeAdd){
			alert("请选择服务器类型！");
			return;
		}
		
		if("" == $.trim(serverUrlAdd)){
			alert("服务器URL不能为空！");
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
			if(ajax.indexOf("成功") != -1){
				var form1 = document.getElementById("selectForm");  // 重新提交查询
				form1.action = "<s:url value='/gtms/stb/resource/serverManage!serverQueryList.action'/>";
				reset();  /** 提交后清除缓存 */
				form1.submit();
			}
		});
	/** 编辑 */
	}else{
		if("" == $.trim(serverNameAdd)){
			alert("服务器名称不能为空！");
			$("input[@name='serverNameAdd']").focus();
			return;
		}
		if("" == $.trim(fileTypeAdd) || "-1" == fileTypeAdd){
			alert("请选择服务器类型！");
			return;
		}
		
		if("" == $.trim(serverUrlAdd)){
			alert("服务器URL不能为空！");
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
			if(ajax.indexOf("成功") != -1){
				var form2 = document.getElementById("selectForm");  // 重新提交查询
				form2.action = "<s:url value='/gtms/stb/resource/serverManage!serverQueryList.action'/>";
				reset();  /** 提交后清除缓存 */
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


// 去掉左右的空格
function trim(str){
     return str.replace(/(^\s*)|(\s*$)/g,"");
}


// 重置
function reset(){
	document.selectForm.serverName.value = "";
	document.selectForm.serverUrl.value = "";
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
            	您当前的位置：机顶盒运营画面服务器管理
		</TD>
	</TR>
</TABLE>
<TABLE width="98%" class="querytable" align="center">
	<tr><th colspan="4" id="thTitle" class="title_1">机顶盒运营画面服务器管理</th></tr>
	<TR id="tr21" STYLE="display:">
		<TD width="10%"  class="title_2">服务器名称</TD>
		<TD width="40%">
			<input type="text" name="serverName" value="" size="20"  class="bk"/>
		</TD>
		<TD width="10%"  class="title_2">服务器URL</TD>
		<TD width="40%">
			<input type="text" name="serverUrl" value="" size="20"  class="bk"/>
		</TD>
	</TR>
	<tr align="right">
		<td colspan="4" class="foot" align="right">
			<div class="right">
				<button name="queryButton" align="right" onclick="javascript:query()"> 查 询 </button>
				<button name="queryButton" align="right" onclick="javascript:reset()"> 重 置 </button>
				<button name="queryButton" align="right" onclick="javascript:add()"> 新 增 </button>
			</div>
		</td>
	</tr>
</TABLE>
</form>

<!-- 展示结果  begin-->
<TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center">
	<TR>
		<TD bgcolor=#999999 id="idData"><iframe id="dataForm"
			name="dataForm" height="0" frameborder="0" scrolling="no"
			width="100%" src=""></iframe></TD>
	</TR>
</TABLE>
<!-- 展示结果  end-->
			
<!-- 添加和编辑 begin -->
<FORM id="addForm" name="addForm" target="" method="post" action="">
<TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center" id="addTable" style="display: none">
	<TR>
		<TD>
		<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="allDatas" class="querytable">
			<TR>
				<TH colspan="4" align="center" class="title_1" ><SPAN id="actLabel">添加</SPAN><SPAN id="DeviceTypeLabel"></SPAN>服务器</TH>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD class="title_2" align="right">服务器名称</TD>
				<TD colspan=3>
					<input type="text" id="serverNameAddId" name="serverNameAdd" value="" size="30" class="bk"/>&nbsp;&nbsp;&nbsp;
					<font color="#FF0000">*</font>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF" >
				<TD class="title_2" align="right">服务器类型</TD>
				<TD colspan=3>
					<select name="fileTypeAdd" class="bk">
						<option value="-1">--请选择--</option>
						<option value="1">--上传--</option>
						<option value="2">--下载--</option>
					</select>&nbsp;&nbsp;&nbsp;
					<font color="#FF0000">*</font>
				</TD>
			</TR>

			<TR bgcolor="#FFFFFF">
				<TD class="title_2" align="right">用户名</TD>
				<TD colspan=3>
					<INPUT TYPE="text" NAME="accessUserAdd" maxlength=30 class=bk size=20>&nbsp;&nbsp;&nbsp;
					<font color="#FF0000">*</font>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD class="title_2" align="right">密码</TD>
				<TD colspan=3>
					<INPUT TYPE="text" NAME="accessPasswdAdd" class=bk >&nbsp;&nbsp;&nbsp;
					<font color="#FF0000">*</font>
				</TD>
			</TR>
			
			<TR bgcolor="#FFFFFF">
				<TD class="title_2" align="right">服务器URL</TD>
				<TD colspan=3>
					<input type="text" name="serverUrlAdd" value="" size="50"  class="bk"/>&nbsp;&nbsp;&nbsp;
					<font color="#FF0000">*</font>&nbsp;(例：http://192.168.28.192:9090/FileServer)
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
					<INPUT TYPE="button" name="saveButton" onClick="javascript:save()" value=" 保 存 " class=jianbian>&nbsp;&nbsp;
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
<!-- 添加和编辑  end -->
</body>
</html>
