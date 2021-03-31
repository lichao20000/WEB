<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/timelater.jsp"%>
<%@ include file="/head.jsp"%>
<%@ include file="/toolbar.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
	
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
	<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
	 
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
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
	$(function() {
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
	
	//查询

	
	
	function queryDevice(){
		dyniframesize();
		trimAll();
		var app_name = $("input[@name='app_name']").val();
		var app_vendor = $("input[@name='app_vendor']").val();
		var app_version = $("input[@name='app_version']").val();
		var app_publish_time_start = $("input[@name='app_publish_time_start']").val();
		var app_publish_time_end = $("input[@name='app_publish_time_end']").val();
		var device_model = $("select[@name='device_model']").val();
		var form = document.getElementById("mainForm");
	form.action = "<s:url value="/gtms/blocTest/MaintainInfoAction!querymaintainAppInfo.action"/>";
	form.submit();
	}
	

function queryReset(){
	reset();
}



function save()
{
	dyniframesize();
	trimAll();
	var filePath = document.getElementById("fileAppPath").value;
	var app_name = document.getElementById("app_name_i").value;
	var app_vendor = document.getElementById("app_vendor_i").value;
	var app_version = document.getElementById("app_version_i").value;
	if(app_name==""){
		alert("软探针等维护APP名称不可为空");
		return;
	}
	if(app_vendor==""){
		alert("软探针等维护APP开发商不可为空");
		return;
	}
	if(app_version==""){
		alert("软探针等维护APP版本不可为空");
		return;
	}
	var filePath = document.getElementById("fileAppPath").value;
		if(""==filePath){
			alert("请上传文件！");
			return;
		}
		var fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
		document.getElementById("fileName").value =fileName;
		var form = document.getElementById("addForm");
		form.submit();
		
}
function checkUpdate()
{
	trimAll();
	var filePath = document.getElementById("fileAppPath").value;
	var app_name = document.getElementById("app_name_i").value;
	var app_vendor = document.getElementById("app_vendor_i").value;
	var app_version = document.getElementById("app_version_i").value;
	if(app_name==""){
		alert("软探针等维护APP名称不可为空");
		return;
	}
	if(app_vendor==""){
		alert("软探针等维护APP开发商不可为空");
		return;
	}
	if(app_version==""){
		alert("软探针等维护APP版本不可为空");
		return;
	}
	var filePath = document.getElementById("fileAppPath").value;
		
		var fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
		document.getElementById("fileName").value =fileName;
		
		
}

function trimAll()
{
	var inputs = document.getElementsByTagName("input");
	for(var i=0;i<inputs.length;i++)
	{
		var input = inputs[i];
		if(/text/gi.test(input.type))
		{
			input.value = trim(input.value);
		}
	}
}
function trim(str){
    return RTrim(LTrim(str)).toString();
}
function LTrim(str){
    var whitespace = new String("　 \t\n\r");
    var s = new String(str);   

    if (whitespace.indexOf(s.charAt(0)) != -1){
        var j=0, i = s.length;
        while (j < i && whitespace.indexOf(s.charAt(j)) != -1){
            j++;
        }
        s = s.substring(j, i);
    }
    return s;
}
function RTrim(str){
    var whitespace = new String("　 \t\n\r");
    var s = new String(str);
 
    if (whitespace.indexOf(s.charAt(s.length-1)) != -1){
        var i = s.length - 1;
        while (i >= 0 && whitespace.indexOf(s.charAt(i)) != -1){
            i--;
        }
        s = s.substring(0, i+1);
    }
    return s;
}
function showAddPart(tag)
{
	if(tag)
		$("table[@id='addTable']").show();
	else
		$("table[@id='addTable']").hide();
}
function defaulValue(updateid,appuuid,app_name,app_desc,app_vendor,app_version,app_publish_status,upfilepath)
{
	document.getElementById("actLabel").innerText = "编辑";
	document.getElementById("appuuid_i").value =appuuid;
	document.getElementById("app_name_i").value =app_name;
	document.getElementById("app_desc_i").value =app_desc;
	document.getElementById("app_vendor_i").value =app_vendor;
	document.getElementById("app_version_i").value =app_version;
	document.getElementById("device_model_i").options[app_publish_status].selected="selected";
	document.getElementById("updateId").value =updateid;
	document.getElementById("upfilepath").value =upfilepath;
	document.getElementById("warn").innerText = " 上传文件不得大于200M，不上传将保留原文件";
	var file = $("#fileAppPath"); 
	file.after(file.clone().val("")); 
	file.remove(); 
	document.getElementById('addOrEdit').onclick=function(){
		checkUpdate();
		var form = document.getElementById("addForm");
		form.action = "<s:url value="/gtms/blocTest/MaintainInfoAction!updateAppInfo.action"/>";
	  form.submit();
		}
}



function Add() {
	$(':input','','#add_Form')  
 .not(':button, :submit, :reset, :hidden')  
 .val('')  
 .removeAttr('checked')  
 .removeAttr('selected');
  document.getElementById("app_publish_status").options[0].selected="selected";
 	var file = $("#fileAppPath"); 
	file.after(file.clone().val("")); 
	file.remove(); 
 document.getElementById("actLabel").innerText = "添加";
 document.getElementById("warn").innerText = "* 上传文件不得大于200M";
 var form = document.getElementById("addForm");
	form.action = "<s:url value="/gtms/blocTest/MaintainInfoAction!maintainAppInfo.action"/>";
	
	showAddPart(true);
	
	}
function queryReset(){
	reset();
}

function  reset(){
	
	    document.mainForm.appuuid.value="";
	   	document.mainForm.app_name.value="";
	   	document.mainForm.app_vendor.value="";
	   	document.mainForm.app_version.value="";
	   	$("select[@name='device_model']").val(-1);
			document.mainForm.app_publish_time_start.value="";
			document.mainForm.app_publish_time_end.value="";
}
</SCRIPT>
  	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>应用终端业务下发</title>
    
</head>
<body>
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION=""
			target="dataForm">
		<table width="98%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="170">
				<div align="center" class="title_bigwhite">软探针APP文件管理</div></td>
				<td></td>
				<td align="right"><input type='button' onclick='javascript:Add()'
					value=' 增 加 ' class="jianbian" id='idAdd' /></td>
			</tr>
		</table>
		
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
			<tr>
				<td bgcolor=#999999>
					<table border=0 cellspacing=1 cellpadding=2 width="100%"
						align="center">
						<tr>
							<th colspan="4" id="gwShare_thTitle">软探针APP信息查询</th>
						</tr>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
								<TD align="right" class=column width="15%">APPUUID</TD>
							<TD width="35%" nowrap><INPUT TYPE="text" NAME="appuuid"
								maxlength=50 class=bk size=20>&nbsp;<font color="#FF0000"></font>
							</TD>
							<TD align="right" class=column width="15%">APP名称</TD>
							<TD width="35%" nowrap><INPUT TYPE="text" NAME="app_name"
								maxlength=60 class=bk size=20>&nbsp;<font color="#FF0000"></font>
							</TD>
								
						</TR>
						
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">APP开发商</TD>
							<TD width="35%" nowrap><INPUT TYPE="text" NAME="app_vendor"
								maxlength=60 class=bk size=20>&nbsp;<font color="#FF0000"></font>
							</TD>
							<TD align="right" class=column width="15%">APP版本</TD>
							<TD align="left" width="35%"><INPUT TYPE="text"
								NAME="app_version" maxlength=60 class=bk size=20>&nbsp;<font
								color="#FF0000"></font></TD>
						
						</TR>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
								<TD align="right" class=column width="15%">APP发布状态</TD>
							<TD align="left" width="35%"><select name="device_model" class="bk">
								<option value="-1">==请选择==</option>
								<option value="0">==是==</option>
								<option value="1">==否==</option>
							</select>&nbsp;<font
								color="#FF0000"></font></TD>
							<TD class=column width="15%" align='right'>APP发布开始时间</TD>
							<TD width="35%">
								<input type="text" name="app_publish_time_start" readonly class=bk > 
								<img name="shortDateimg"
									 onClick="WdatePicker({el:document.mainForm.app_publish_time_start,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									 src="../../images/dateButton.png" width="15" height="12"
									 border="0" alt="选择">
							</TD>
							
						</TR>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD class=column width="15%" align='right'>APP发布结束时间</TD>
							<TD width="35%">
								<input type="text" name="app_publish_time_end" readonly class=bk ">
								<img name="shortDateimg"
									 onClick="WdatePicker({el:document.mainForm.app_publish_time_end,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									 src="../../images/dateButton.png" width="15" height="12" border="0" alt="选择">
							</TD>
							<TD align="right" class=column width="15%"></TD>
							<TD align="left" width="35%"></TD>
						</TR>
						<tr bgcolor="#FFFFFF">
							<td colspan="4" align="right" class="green_foot" width="100%">
								
								<input type="button" onclick="javascript:queryDevice();" class=jianbian
									name="gwShare_queryButton" value=" 查 询 " /> 
								<input type="button"
									class=jianbian name="gwShare_reButto" value=" 重 置 "
									onclick="javascript:queryReset();" />
							</td>
						</tr>
						</table>
					</td>
				</tr>
			</TABLE>
			
		</FORM>
		
		<!-- 展示结果part -->
		
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center" id="query_Form">
			<TR>
				<TD bgcolor=#999999 id="idData"><iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="100%" src=""></iframe></TD>
			</TR>
		</TABLE>
		
		
		<!-- 添加和编辑part -->
		<FORM NAME="addForm" id="addForm" METHOD="post" ACTION=""
			target="add_form_t" enctype="multipart/form-data">
		<TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center"
			id="addTable" style="display: none">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=0 width="100%"
					id="addDatas">
					<TR>
						<TH colspan="4" align="center"><SPAN id="actLabel" >添加</SPAN>软探针APP文件信息</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">APPUUID</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="appuuid" id = "appuuid_i"
							maxlength=13 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">APP名称</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="app_name" id = "app_name_i"
							maxlength=60 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">APP描述</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="app_desc" id = "app_desc_i"
							maxlength=120 class=bk size=20></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">APP开发商</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="app_vendor" id = "app_vendor_i"
							maxlength=60 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">APP版本</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="app_version" id = "app_version_i"
							maxlength=60 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">APP发布状态</TD>
						<TD colspan=3><select name="app_publish_status" class="bk" id = "device_model_i">
								<option value="0">==否==</option>
								<option value="1">==是==</option>
							</select>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					
					<TR bgcolor="#FFFFFF">
										<TD class=column align="right" width="15%">附件上传</TD>
										<TD id="clearValue" width="85%"><input type="file" name="fileAppPath" 
											id="fileAppPath" size="35" /><SPAN id="warn" style="color:#FF0000"></SPAN></TD>
												<input type="hidden" id="fileName" name="fileName" value="" />
											<input type="hidden" id="updateId" name="updateId" value="" />
											<input type="hidden" id="upfilepath" name="upfilepath" value="" />
									</TR>
					
				</TABLE>
				
				</TD>
			</TR>
			
			<TR>
				<TD >
				<TABLE border=0 cellspacing=1 cellpadding=0 width="100%">
					<TR bgcolor="#FFFFFF">
						<TD align="right" CLASS=green_foot>
							<input type="hidden" id="updateId" value="" />
										<INPUT TYPE="button" id = "addOrEdit"
							onclick="javascript:save()" value=" 保 存 " class=jianbian>&nbsp;&nbsp;
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>  
		<iframe id="add_form_t"
					name="add_form_t" height="0" frameborder="0" scrolling="no"
					width="100%" src="">
		</FORM>
		
		
			
		
		</TD>
		</TR>
	</TABLE>
	<div>
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
				scrolling="no" width="100%" src=""></iframe>
		</div>
</body>
<%@ include file="/foot.jsp"%>
</html>
