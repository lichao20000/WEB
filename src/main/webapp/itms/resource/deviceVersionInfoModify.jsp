<%--
Author		: Alex.Yan (yanhj@lianchuang.com)
Date		: 2007-11-28
Desc		: TR069: devicetype_list, add;
			  SNMP:	 Ddevicetype list.
--%>

<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
<SCRIPT LANGUAGE="JavaScript">

function Init(){
	// 初始化厂家
	
	//gwShare_queryChange("2");
	
	// 普通方式提交
	/**
	var form = document.getElementById("mainForm");
	 setValue();
	form.action = "<s:url value="/itms/resource/deviceTypeInfo!queryList.action"/>";
	//form.target = "dataForm";
	form.submit();
	**/
}

$(function(){
	//Init();
});

</SCRIPT>

</head>
<%@ include file="/toolbar.jsp"%>
<%@ include file="./DeviceType_Info_util.jsp"%>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION="" enctype="multipart/form-data"
			target="dataForm">
		<table width="98%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162">
				<div align="center" class="title_bigwhite">设备版本库修改</div>
				</td>
				<td><img src="/itms/images/attention_2.gif" width="15"
					height="12">设备版本库修改</td>
			</tr>
		</table>
		<!-- 高级查询part -->
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
			<tr>
				<td bgcolor=#999999>
				<table border=0 cellspacing=1 cellpadding=2 width="100%"
					align="center">
					<tr>
						<th colspan="4" id="gwShare_thTitle">设备版本修改</th>
					</tr>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">设备厂商</TD>
						<TD align="left" width="35%">
							<s:select list="vendorList" name="vendor"
								headerKey="-1" headerValue="==请选择==" listKey="vendor_id"
								listValue="vendor_name" value="vendor" cssClass="bk" onchange="gwShare_change_select('deviceModel','-1')"></s:select>
						</TD>
						<TD align="right" class=column width="15%">设备型号</TD>
						<TD width="35%">
							<s:select list="deviceModelList" name="device_model"
								headerKey="-1" headerValue="==请选择厂商==" listKey="device_model_id"
								listValue="device_model" value="device_model" cssClass="bk"></s:select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">硬件版本</TD>
						<TD align="left" width="35%">
							<INPUT TYPE="text"
								NAME="hard_version" maxlength=30 class=bk size=20
								value= "<s:property value='modifyVersion.hardwareversion'  />" />
							&nbsp;<font color="#FF0000">*</font></TD>
						<TD align="right" class=column width="15%">软件版本</TD>
						<TD width="35%" nowrap><INPUT TYPE="text" NAME="soft_version"
							maxlength=30 class=bk size=20
							value= "<s:property value='modifyVersion.softwareversion'  />"
							/>
							&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">特别版本</TD>
						<TD align="left" width="35%">
							<INPUT TYPE="text"
							NAME="spec_version" maxlength=30 class=bk size=20
							value= "<s:property value='modifyVersion.specversion'  />" />
							&nbsp;<font color="#FF0000"  />*</font></TD>
						<TD align="right" class=column width="15%">当前文件路径</TD>
						<TD width="35%" nowrap>
							<input type="text" name="filepath" size=30 value="<s:property value="modifyVersion.file_path" />" />
						</TD>
					</TR>
					
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">重新上传文件</TD>
						<TD width="35%" nowrap colspan=3>
						<!--  
						<input type="file" name="file_path" id="file_path"
						 size="40" />&nbsp;<font color='red'>*</font>
						--> 
						 <input type='file'  name="file_path" id="file_path" 
						 size="40" />
						</TD>
					</TR>

					<tr bgcolor="#FFFFFF">
						<td colspan="4" align="right" class="green_foot" width="100%">
						<input type="button"
							class=jianbian name="gwShare_reButto" value=" 保 存 "
							onclick="javascript:save();" />
						<input type="button"
							class=jianbian name="gwShare_reButto" value=" 重 置 "
							onclick="javascript:queryReset();" />
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</FORM>
		<input type='hidden' name="id" value="<s:property value="modifyVersion.id" />" />
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			<iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="100%" src=""></iframe>
		</TD>
	</TR>

</TABLE>
</body>
<%@ include file="/foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">

// 重置
function queryReset(){
	//reset();
	document.mainForm.reset();
}

function  reset(){
	
		/**
	    document.mainForm.vendor.value="-1";
	    document.mainForm.device_model.value="-1";
		document.mainForm.hard_version.value="";
		document.mainForm.soft_version.value="";
		document.mainForm.spec_version.value="";
		document.mainForm.file_path.value = "";
		$("input[@name='file_path']").val("");
		**/
		//document.mainForm.start_time.value="";
		//document.mainForm.end_time.value="";
		
}

// 保存
function save()
{
	trimAll();
	
	var vendor = $("select[@name='vendor']").val();
	var device_model = $("select[@name='device_model']").val();
	var spec_version = $("input[@name='spec_version']").val();
	var hard_version = $("input[@name='hard_version']").val();
	var soft_version = $("input[@name='soft_version']").val();
	var file_path = $("input[@name='file_path']").val();
	var id = $("input[@name='id']").val();
	
	// 进行校验
	if($("select[@name='vendor']").val() == "-1")
	{
		alert("请选择厂商！");
		$("select[@name='vendor']").focus();
		return;
	}
	if($("select[@name='device_model']").val() == "-1")
	{
		alert("请选择型号！");
		$("select[@name='device_model']").focus();
		return;
	}
	
	if($("input[@name='hard_version']").val() == "")
	{
		alert("请输入硬件版本！");
		$("input[@name='hard_version']").focus();
		return;
	}
	
	if($("input[@name='soft_version']").val() == "")
	{
		alert("请选择软件版本！");
		$("input[@name='soft_version']").focus();
		return;
	}
	
	if($("input[@name='spec_version']").val() == "")
	{
		alert("请选择特别版本！");
		$("input[@name='spec_version']").focus();
		return;
	}
	
	/** 文件上传不是必须的
	if($("input[@name='file_path']").val() == "")
	{
		alert("必须选择版本文件进行上传！");
		$("input[@name='file_path']").select();
		$("input[@name='file_path']").focus();
		return;
	}**/
	
	var url = "<s:url value="/itms/resource/deviceVersionManageACT!modifyDeviceVersion.action"/>" + "?id=" + id;
	
	document.mainForm.action = url;
	document.mainForm.submit();
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

/** 工具方法 **/
/*LTrim(string):去除左边的空格*/
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
/*RTrim(string):去除右边的空格*/
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
/*Trim(string):去除字符串两边的空格*/
function trim(str){
    return RTrim(LTrim(str)).toString();
}


//全部trim
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

</SCRIPT>
</html>