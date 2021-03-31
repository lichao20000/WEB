<%@page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<style type="text/css">
td {
	white-space: nowrap;
	overflow: hidden;
}
</style>

<SCRIPT LANGUAGE="JavaScript">
	var gwtype=4;
	function vendorChange()
	{
		var vendorId = $("select[@name='vendorId']").val();
		var url = "<s:url value='/gtms/stb/resource/deviceVersion!getDeviceModel.action'/>";
		if(vendorId!="-1"){
			$("div[@id='deviceModel']").html("");
			$.post(url,{
				vendorId:vendorId
			},function(ajax){
				if(ajax!=""){
					var lineData = ajax.split("#");
					if(typeof(lineData)&&typeof(lineData.length)){
						for(var i=0;i<lineData.length;i++){
							var oneElement = lineData[i].split("$");
							var xValue = oneElement[0];
							var xText = oneElement[1];
							checkboxtxt = "<input type='checkbox' name='deviceModelId' value='"+xValue+"'>"+xText+"  ";
							$("div[@id='deviceModel']").append(checkboxtxt);
						}
					}else{
						$("div[@id='deviceModel']").append("该厂商没有对应型号！");
					}
				}else{
					$("div[@id='deviceModel']").append("该厂商没有对应型号！");
				}
			});
		}else{
			$("div[@id='deviceModel']").html("请选择厂商");
		}
	}
	
	function ExecMod()
	{
		var isAdd = $("input[@name='isAdd']").val();
		var pathId = $("input[@name='pathId']").val();
		var versionDesc = $("input[@name='versionDesc']").val();
		var vendorId = $("select[@name='vendorId']").val();
		var versionPath= $("input[@name='versionPath']").val();
		var softwareversion = $("input[@name='softwareversion']").val();
		var dcnPath = $("input[@name='dcnPath']").val();
		var specialPath = $("input[@name='specialPath']").val();
		
		if(softwareversion==""){
			alert("请输入版本名称!");
			$("input[@name='softwareversion']").focus();
			return;
		}
		if(versionDesc==""){
			alert("请输入版本描述!");
			$("input[@name='versionDesc']").focus();
			return;
		}
		if(vendorId=="-1"){
			alert("请选择厂商！");
			return;
		}
		if(versionPath==""){
			alert("请输入版本文件路径!");
			$("input[@name='versionPath']").focus();
			return;
		}
		
		var newfileName = $("input[@name='newfileName']").val();
		if (newfileName){
			var len = versionPath.length;
			var last = versionPath.charAt(len-1);
			if("/"==last){
				versionPath = versionPath+newfileName;
			}else{
				versionPath = versionPath+"/"+newfileName;
			}
		}
		
		var deviceModelId = "";
		$("input[@name='deviceModelId'][@checked]").each(function(){ 
	    	deviceModelId = deviceModelId + $(this).val()+",";
	    });
	    if(deviceModelId==""){
			alert("请选择适用型号!");
			return;
		}
	    
	    if(dcnPath==""){
			alert("版本文件DCN路径不可以为空!");
			$("input[@name='dcnPath']").focus();
			return;
		}
	    
		var versionType = $("select[@name='versionType']").val();

	    var url = "<s:url value='/gtms/stb/resource/deviceVersion!addedit.action'/>";
	    $.post(url,{
			isAdd:isAdd,
			versionDesc:encodeURIComponent(versionDesc),
			vendorId:vendorId,
			versionPath:versionPath,
			deviceModelId:deviceModelId,
			versionType:versionType,
			softwareversion:encodeURIComponent(softwareversion),
			pathId:pathId,
			dcnPath:dcnPath,
			specialPath:specialPath,
			gwType:gwtype
		},function(ajax){
	    	var s = ajax.split(",");
	    	if(s[0]=="1" || s[0]=="0"){
	    		alert(s[1]);
	    		window.location.reload();
	    	}else{
	    		alert("添加失败！");
	    	}
	    });
	}
</SCRIPT>

</head>
<body>
<table class="querytable" width="98%" align="center">
	<tr>
		<td class="title_1" colspan="2">
			添加机顶盒版本文件路径 
			<input type="hidden" name="isAdd" value="1"> 
			<input type="hidden" name="pathId" value="">
		</td>
	</tr>
	<TR>
		<TD class="title_2" align="center" width="15%">版本名称</TD>
		<TD width="85%">
			<input type="text" name="softwareversion" id="softwareversion" class="bk" value="" size="40" maxlength="20">
			<font color="red">*长度小于20</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">版本描述</TD>
		<TD width="85%">
			<input type="text" name="versionDesc" id="versionDesc" class="bk" value="" size="40" maxlength="20">
			<font color="red">*长度小于20</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">厂商</TD>
		<TD width="85%">
			<s:select list="vendorList" name="vendorId" headerKey="-1" headerValue="请选择厂商" 
				listKey="vendor_id" listValue="vendor_add" value="vendorId" cssClass="bk"
				onchange="vendorChange()" theme="simple"></s:select> 
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">适用型号</TD>
		<TD width="85%">
			<div id="deviceModel">请选择厂商</div>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">版本类型</TD>
		<TD width="85%">
			<select name="versionType" id="versionType" class="bk">
			<option value="0">普通版本</option>
		</select></TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">版本文件路径</TD>
		<TD width="85%">
			<input type="text" name="versionPath" id="versionPath" class="bk" value="" size="100"> 
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">版本文件专网路径</TD>
		<TD width="85%">
			<input type="text" name="specialPath" id="specialPath" class="bk" value="" size="100">
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">版本文件DCN路径</TD>
		<TD width="85%">
			<input type="text" name="dcnPath" id="dcnPath" class="bk" value="" size="100"> 
			<font color="red">*</font>
		</TD>
	</TR>
	<tr>
		<td colspan="2" class="foot" align="right">
			<button onclick="ExecMod()">提&nbsp;&nbsp;交</button>&nbsp;&nbsp;
		</td>
	</tr>
</table>

</body>