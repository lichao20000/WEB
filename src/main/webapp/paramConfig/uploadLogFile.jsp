<%--
FileName	: softUpgrade.jsp
Date		: 2007年5月10日
Desc		: 软件升级.
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<jsp:useBean id="versionManage" scope="request"
	class="com.linkage.litms.software.VersionManage" />
<%
String file_path = versionManage.getURLPath(3);
String gwType = request.getParameter("gw_type");
%>
<%@ include file="../head.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	gwShare_setGaoji();
	gwShare_setImport();
});
	function CheckForm(){
		//配置文件所有文件名
		/**
		var tmp_names = all_ver_con_files.split(",");
		var tmp_filename = document.all("filename").value;
		for (var i = 0; i < tmp_names.length; i++) {
			if ((tmp_filename+".log.bin") == tmp_names[i]) {
				alert("文件名已存在!");
				return false;
			}
		}
		*/
		if(deviceNum!="0"){
			alert("选择的设备超过50!请重新查询");
			return false;
		}
		if($("input[@name='deviceIds']").val()==""){
			alert("请选择设备！");
			return false;
		}
//-------------------------------------------------------------------
		if($("select[@name='url_path']").val() == -1){
			alert("请选择文件路径！");
			return false;
		}

		if(!IsNull($("input[@name='filename']").val(),'文件名')){
			$("input[@name='filename']").focus();
			return false;
		}
		if(!IsNull($("input[@name='delay_time']").val(),'时延')){
			$("input[@name='delay_time']").focus();
			return false;
		}else{
			if(!IsNumber($("input[@name='delay_time']").val(),"时延")){
				$("input[@name='delay_time']").focus();
				return false;
			}
		}			
	}
	
	function selectChg() {
		if ($("select[@name='url_path']").val() == -1) {
			$("input[@name='dir_id']").val("")
			$("input[@name='hid_url_path']").val("")
		} else {
			var _url = $("select[@name='url_path']").val();			
			var _urlArr = _url.split("|");
			$("input[@name='dir_id']").val(_urlArr[1])
			$("input[@name='hid_url_path']").val(_urlArr[0])		
		}
	}
	
var deviceIds = "";
var deviceNum = "";	
function deviceResult(returnVal){	
	deviceIds="";
	if(returnVal[0]==0){
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[2].length+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";			
		}
		$("input[@name='deviceIds']").val(deviceIds);
		deviceNum = returnVal[0];
	}else{
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
		$("input[@name='deviceIds']").val("0");
		$("input[@name='param']").val(returnVal[1]);
		deviceNum = returnVal[0];
	}
}
	
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>

			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									配置日志管理
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									选择的设备日志文件备份到文件服务器。
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</TR>
				<TR>
					<TH colspan="4" align="center">
						上传日志文件
					</TH>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post" ACTION="deviceUploadLogFile.jsp" target="childFrm"
							onsubmit="return CheckForm()">
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="param" value="" />
							<input type="hidden" name="gwType" value="<%=gwType%>"/>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<div id="selectedDev">
														请查询设备！
													</div>
												</td>
											</tr>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													执行方式
												</TD>
												<TD width="85%" colspan="3">
													<input type="radio" name="excute_type" value="0" checked>
													立即执行
													<input type="radio" name="excute_type" value="4">
													下次连到系统
												</TD>
											</TR>
											<!-- <TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													关键字:
												</TD>
												<TD width="30%">
													<input type="text" name="keyword" maxlength=20 class=bk size=20>
												</TD>
												<TD align="right" width="20%">
													文件类型:
												</TD>
												<TD width="30%">
													<select name="filetype" class="bk">
														<option value="2 Vendor Log File">
															2 Vendor Log File
														</option>
													</select>
												</TD>
											</TR> -->

											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													文件路径
												</TD>
												<TD width="" colspan="3">
													<%=file_path%>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													用户名
												</TD>
												<TD width="35%">
													<input type="text" name="username" maxlength=20 class=bk
														size=20>
												</TD>
												<TD align="right" width="15%">
													密码
												</TD>
												<TD width="35%">
													<input type="text" name="passwd" maxlength=20 class=bk
														size=20>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right">
													时延
												</TD>
												<TD>
													<input type="text" name="delay_time" maxlength=20 class=bk
														size=20 value="0">
													&nbsp;(单位:秒)
												</TD>
												<TD align="right">
													文件名
												</TD>
												<TD nowrap>
													设备ID.时间.
													<input type="text" name="filename" maxlength=20 class=bk
														size=20>
													&nbsp;.log
												</TD>

											</TR>
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">

													<INPUT TYPE="reset" value=" 重 置 " class=btn>
													&nbsp;
													<input type="hidden" name="service_id" value="3">

													&nbsp;
													<INPUT TYPE="submit" value=" 备 份 " class=btn>
													<input type='hidden' name='dir_id' value="">
													<input type="hidden" name="hid_url_path" value="">
													<input type='hidden' name='keyword' value="upload_log">
													<input type='hidden' name='filetype'
														value="2 Vendor Log File">
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</FORM>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm name=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
