<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备统计查询</title>

<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/faulttreatment/slide.js"/>"></script>
<link href="<s:url value="/css3/css.css"/>" rel="stylesheet" type="text/css" />

</head>
<body>
	
	<FORM NAME="frm" METHOD="post" ACTION="<s:url value='/itms/resource/faultTreadtMent!softUpgrade.action'/>" onSubmit="return CheckForm();">
							<input type="hidden" name="prot_type" value="tr069" />
							<table width="100%" border="0" cellspacing="0" cellpadding="0"
					class="it_table">
							<tr>
									<th align="center" colspan="4">
										升级配置
									</th>
									</tr>
								<s:if test="softVersionMap!=null">
								<TR bgcolor="#FFFFFF">
									<TD >
										<font color="red">*</font>&nbsp;目标版本
									</TD>
									<TD  colspan="3">
									<input type="hidden" name="softwarefile_id" value='<s:property value="softVersionMap.softwarefile_id" />'>
									<input type="hidden" name="device_id" value='<s:property value="softVersionMap.device_id" />'>
										<s:property value="softVersionMap.softwarefile_name" />(<s:property value="softVersionMap.softwareversion" />)
									</TD>
								</TR>
								<TR id=filepath  STYLE="display: none">
									<TD >
										文件路径
									</TD>
									<TD width="" colspan="3">
										<div id="div_file_path" name="div_file_path">

										</div>
									</TD>
								</TR>
								
								<TR isShow="snmp" STYLE="display: none" >
									<TD >
										版本
									</TD>
									<TD width="" colspan="3">
										<input type="text" name="version" maxlength=255 
											size=20>
									</TD>
								</TR>
								<TR isShow="tr069" STYLE="display: none" >
									<TD >
										关键字:
									</TD>
									<TD width="35%">
										<input type="text" name="keyword_2" maxlength=255 
											size=20 value="soft">
									</TD>
									<TD >
										文件类型:
									</TD>
									<TD width="35%">
										<select name="filetype_2" class="bk" readOnly>
											<option value="1 Firmware Upgrade Image">
												1 Firmware Upgrade Image
											</option>
										</select>
									</TD>
								</TR>
								<TR isShow="tr069" STYLE="display: none" >
									<TD >
										用户名:
									</TD>
									<TD width="35%">
										<input type="text" name="username_2" maxlength=255 
											size=20>
									</TD>
									<TD >
										密码:
									</TD>
									<TD width="35%">
										<input type="text" name="passwd_2" maxlength=255 
											size=20>
									</TD>
								</TR>
								<TR isShow="tr069" STYLE="display: none" >
									<TD >
										文件大小:
									</TD>
									<TD width="35%">
										<input type="text" name="file_size_2" maxlength=255 
											size=20 readOnly>
									</TD>
									<TD >
										文件名:
									</TD>
									<TD width="35%">
										<input type="text" name="filename_2" maxlength=255 
											size=20 readOnly>
									</TD>
								</TR>
								<TR isShow="tr069" STYLE="display: none" >
									<TD >
										时延:
									</TD>
									<TD colspan="3">
										<input type="text" name="delay_time_2" maxlength=255 
											size=20 value="0">
										&nbsp;&nbsp;(单位:s)
									</TD>
								</TR>
								<TR isShow="tr069" STYLE="display: none" >
									<TD >
										成功URL:
									</TD>
									<TD colspan="3">
										<input type="text" name="sucess_url_2" maxlength=255 
											size=80>
									</TD>
								</TR>
								<TR isShow="tr069" STYLE="display: none" >
									<TD >
										失败URL:
									</TD>
									<TD colspan="3">
										<input type="text" name="fail_url_2" maxlength=255 
											size=80>
									</TD>
								</TR>
									<tr bgcolor="#FFFFFF">
									<td colspan="4" align="right"  width="100%">
										<INPUT TYPE="button" value=" 升 级 " onclick="softUpgrade()" class="it_btn" />
										&nbsp;&nbsp;
										<INPUT TYPE="reset" value=" 取 消 " onclick="javascript:window.close();" class="it_btn" />
										<INPUT TYPE="hidden" name="action" value="add">
										<input type="hidden" name="auto_type" value="1">
										<input type="hidden" name="service_id" value="5" />
										<input type="hidden" name="excute_type" value="0"/>
										<input type="hidden" name="gw_type" value="1"/>
									</td>
									</tr>
								</s:if>
								<s:else>
								<tr>
									<td colspan=2>没有最新版本可以更新!</td>
								</tr>
								</s:else>
							</TABLE>
							<div style="text-align: center;"><button onclick="javascript:window.close();" class="it_btn"> &nbsp;关闭&nbsp;</button></div>
						</FORM>
						
						
	
</body>
</html>
<script type="text/javascript">
	$(function(){
		var url = "../../software/showSoftwareversion.jsp";
		var goal_softvn = $("input[@name='softwarefile_id']").val();
		//alert(goal_softvn);
		$.post(url,{
			type:2,
			goal_softwareversion:goal_softvn
		},function(msg){
			//alert(msg);
			var temp = "";
			var filename_2 = "";
			var file_size_2 ="";
			var sbf = "";
			if(msg != null && msg.indexOf("|")>-1){
				temp = msg.substring(msg.indexOf("|") + 1);
				filename_2 = temp.substring(0,temp.indexOf("|"));
				file_size_2 = temp.substring(temp.indexOf("|") + 1,temp.indexOf("'"));
			}
			document.getElementById("div_file_path").innerHTML = msg;
			document.all("file_size_2").value = file_size_2;
			document.all("filename_2").value = filename_2;
		});
	});
	function CheckForm(){
	    var __device_id = $("input[@name='device_id']").val();
		if(__device_id == null || __device_id == ""){
			alert("请先查询设备!");
			return false;
		}
		//alert($("select[@name='goal_softwareversion']"));
		if($("input[@name='goal_softwareversion']").val() == -1){
			alert("请选择版本文件！");
			return false;
	}}
	function softUpgrade(){
	 	var device_id = $("input[@name='device_id']").val();
	 	var softwarefile_id = $("input[@name='softwarefile_id']").val();
	 	var file_path_2 = $("input[@name='file_path_2']").val();
	 	var username_2 = $("input[@name='username_2']").val();
	 	var passwd_2 = $("input[@name='passwd_2']").val();
	 	var file_size_2 = $("input[@name='file_size_2']").val();
	 	var filename_2 = $("input[@name='filename_2']").val();
	 	var delay_time_2 = $("input[@name='delay_time_2']").val();
	 	var sucess_url_2 =  $("input[@name='sucess_url_2']").val();
	 	var fail_url_2 = $("input[@name='fail_url_2']").val();
	 	var gw_type = $("input[@name='gw_type']").val();
	 	var prot_type = $("input[@name='prot_type']").val(); 
	 	var url = "<s:url value='/itms/resource/faultTreadtMent!softUpgrade.action'/>";
	 	$.post(url,{
	 		device_id:device_id,
	 		softwarefile_id:softwarefile_id,
	 		file_path_2:file_path_2,
	 		username_2:username_2,
	 		passwd_2:passwd_2,
	 		file_size_2:file_size_2,
	 		filename_2:filename_2,
	 		delay_time_2:delay_time_2,
	 		sucess_url_2:sucess_url_2,
	 		fail_url_2:fail_url_2,
	 		gw_type:gw_type,
	 		prot_type:prot_type
	 	},function(ajax){
	 		window.returnValue = ajax;
	 		window.close();
	 	});
	}
</script>