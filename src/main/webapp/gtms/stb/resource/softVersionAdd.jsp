<%@page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>


<SCRIPT LANGUAGE="JavaScript">
	//关联型号
	function vendorChange()
	{
		var vendorId = $("select[@name='vendorId']").val();
		var url = "<s:url value='/gtms/stb/resource/softVersion!getDeviceModel.action'/>";
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
							checkboxtxt = "<input type='checkbox' name='deviceModelId' value='"
												+xValue+"'>"+xText+"  ";
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
	
	//编辑或新增版本
	function ExecMod()
	{
		var isAdd = $("input[@name='isAdd']").val();
		var versionDesc = $("input[@name='versionDesc']").val();
		var vendorId = $("select[@name='vendorId']").val();
		var versionPath= $("input[@name='versionPath']").val();
		var versionName = $("input[@name='versionName']").val();
		var fileSize = $("input[@name='fileSize']").val();
		var MD5 = $("input[@name='MD5']").val();
		var epg_version = $("input[@name='epg_version']").val();
		var net_type = $("select[@name='net_type']").val();
		
		if(versionName==""){
			alert("请输入版本名称!");
			$("input[@name='versionName']").focus();
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
		if(fileSize==""){
			alert("请输入版本文件大小!");
			$("input[@name='fileSize']").focus();
			return;
		}
		if(MD5==""){
			alert("请输入MD5数据!");
			$("input[@name='MD5']").focus();
			return;
		}
		if(epg_version==""){
			alert("请输入EPG版本数据!");
			$("input[@name='epg_version']").focus();
			return;
		}
		
		var deviceModelId = "";
		$("input[@name='deviceModelId'][@checked]").each(function(){ 
	    	deviceModelId = deviceModelId + $(this).val()+",";
	    });
	    if(deviceModelId=="" || deviceModelId==null){
			alert("请选择适用型号!");
			return;
		}
	    
	    var url = "<s:url value='/gtms/stb/resource/softVersion!addedit.action'/>";
	    $.post(url,{
			isAdd:isAdd,
			versionDesc:versionDesc,
			vendorId:vendorId,
			versionPath:versionPath,
			deviceModelId:deviceModelId,
			versionName:versionName,
			fileSize:fileSize,
			MD5:MD5,
			epg_version:epg_version,
			net_type:net_type
		},function(ajax){
	    	var s = ajax.split(",");
	    	alert(s[1]);
	    	if("1"==s[0])
	    	{
	    		$("input[@name='isAdd']").val("1");
	    		$("input[@name='id']").val("");
	    		$("input[@name='versionDesc']").val("");
	    		$("select[@name='vendorId']").val("-1");
	    		$("input[@name='versionPath']").val("");
	    		$("input[@name='versionName']").val("");
	    		$("input[@name='fileSize']").val("");
	    		$("input[@name='MD5']").val("");
	    		$("input[@name='epg_version']").val("");
	    	}
	    });
	}
</SCRIPT>

</head>
<body>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24> 
			您当前的位置：新增软件版本文件路径管理
		</TD>
	</TR>
</TABLE>

<table class="querytable" width="98%" align="center" id="addedit">
	<tr>
		<td class="title_1" colspan="2">
			新增软件版本文件路径
			<input type="hidden" name="isAdd" value="1"> 
		</td>
	</tr>
	<TR>
		<TD class="title_2" align="center" width="15%">版本名称</TD>
		<TD width="85%">
			<input type="text" name="versionName" id="versionName" class="bk" value="" size="40" maxlength="40">
			<font color="red">*长度小于40</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">版本描述</TD>
		<TD width="85%">
			<input type="text" name="versionDesc" id="versionDesc" class="bk" value="" size="40" maxlength="80">
			<font color="red">*长度小于80</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">厂商</TD>
		<TD width="85%">
			<s:select list="vendorList" name="vendorId"
				headerKey="-1" headerValue="请选择厂商" listKey="vendor_id"
				listValue="vendor_add" value="vendorId" cssClass="bk"
				onchange="vendorChange()" theme="simple">
			</s:select> <font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">适用型号</TD>
		<TD width="85%">
			<div id="deviceModel">请选择厂商</div>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">版本文件路径</TD>
		<TD width="85%">
			<input type="text" name="versionPath" id="versionPath" class="bk" value="" size="40"> 
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">文件大小</TD>
		<TD width="85%">
			<input type="text" name="fileSize" id="fileSize" class="bk" value="" size="40"> 
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">MD5</TD>
		<TD width="85%">
			<input type="text" name="MD5" id="MD5" class="bk" value="" size="40"> 
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">EPG版本</TD>
		<TD width="85%">
			<input type="text" name="epg_version" id="epg_version" class="bk" value="" size="16"> 
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">适用网络类型</TD>
		<TD width="85%">
			<select name="net_type" class="bk" >
				<option value="unknown_net" selected>未  知</option>
				<option value="public_net">公  网</option>
				<option value="private_net">专  网</option>
			</select>
		</TD>
	</TR>
	<tr>
		<td colspan="2" class="foot" align="right">
			<div class="right">
				<button onclick="javascript:ExecMod();">提交</button>
			</div>
		</td>
	</tr>
</table>
</body>