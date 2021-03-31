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
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
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

		//if(!IsNull($("input[@name='filename']").val(),'文件名')){
			//$("input[@name='filename']").focus();
			//return false;
		//}
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


function doQuery(){
	var taskname = $("input[@name='taskname']").val();
	var endtime = $("input[@name='endtime']").val();
	var starttime = $("input[@name='starttime']").val();
	var enddate = $("input[@name='enddate']").val();
	var urls = $("input[@name='url']").val();
	
	//var httptype = $("input[@name='httptype']:checked").val();
	var httptype = $("input[@name='httptype']").val();
	//var testUserName = $("input[@name='testUserName']").val();
	//var testPWD = $("input[@name='testPWD']").val();
	var deviceIds = $("input[@name='deviceIds']").val();
	
	var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
	var param = $("input[@name='param']").val();
	
	if(starttime>=endtime){
		if(starttime>endtime){
			alert("开始时间不能大于结束时间");
		}else{
			alert("开始时间不能等于结束时间");
		}
		return;
	}
	if(taskname == ""){
		alert("任务名不能为空");
		return;
	}
	//if(filename == ""){
		//alert("请先上传文件");
		//return;
	//}
	
	if(deviceIds == ""){
		alert("请选择设备");
		return;
	}
		
	//if(testUserName == ""){
		//alert("测速账号不能为空");
		//return;
	//}
	//if(testPWD == ""){
		//alert("测速账号密码不能为空");
		//return;
	//}
	if(urls == ""){
		alert("url地址不能为空");
		return;
	}
	//if(testUserName.length>30){
		//alert("测速账号不能超过30个字符");
		//return;
	//}
	//if(testPWD.length>30){
		//alert("测速账号密码不能超过30个字符");
		//return;
	//}
		$("button[@id='btn']").attr("disabled",true);
		$("tr[@id='trData']").show();
		$("div[@id='QueryData']").html("正在执行批量定制操作，请稍等....");
		url = "<s:url value='/ids/httpDownload!addTaskInfo4NX.action'/>";
		$.post(url,{
			taskname : taskname,
			//filename : filename,
			deviceIds : deviceIds,
			endtime : endtime,
			starttime : starttime,
			enddate : enddate,
			url : urls,
			httpType : httptype,
			//testUserName : testUserName,
			//testPWD : testPWD,
			gwShare_queryType : gwShare_queryType,
			param : param
		},function(ajax){
			$("div[@id='QueryData']").html(ajax);
			$("button[@id='btn']").attr("disabled",true);
		});
		
	
	
	//var url = "<s:url value='/ids/httpDownload!getExcelRows.action'/>";
	//$.post(url,{
		////filename : filename
	//},function(ajax){
		//if(ajax == "false"){
			//alert("单次最多10000行");
			//return;
		//}
		//$("button[@id='btn']").attr("disabled",true);
		//$("tr[@id='trData']").show();
		//$("div[@id='QueryData']").html("正在执行批量定制操作，请稍等....");
		//url = "<s:url value='/ids/httpDownload!addTaskInfo4NX.action'/>";
		//$.post(url,{
			//taskname : taskname,
			////filename : filename,
			//deviceIds : deviceIds,
			//endtime : endtime,
			//starttime : starttime,
			//enddate : enddate,
			//url : urls,
			//httpType : httptype,
			//testUserName : testUserName,
			//testPWD : testPWD
		//},function(ajax){
			//if(ajax == "false"){
				//$("div[@id='QueryData']").html("批量定制操作执行失败");
			//}else if(ajax=="批量定制任务执行成功！"){
				//$("div[@id='QueryData']").html("批量定制操作执行成功");
				
			//}
			//else
			//{
				//$("div[@id='QueryData']").html("定制失败！页面长时间未操作请刷新页面！");
			//}
			//$("button[@id='btn']").attr("disabled",false);
		//});
		
		
		
	//});
	
	
	
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
									批量HTTP下载测速
								</td>
								<td nowrap>
									<!-- <img src="../images/attention_2.gif" width="15" height="12">
									单次最大10000台 -->
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery_httpDownloadInfo4NX.jsp"%>
					</td>
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
											
											
											<tr bgcolor=#ffffff>
						<td class=column align=center width="10%">任务名称：</td>
						<td colspan="3"><input type="text" name="taskname"  > </td>
					</tr>
					
					<tr bgcolor=#ffffff>
						<td class=column align=center width="10%">开始时间：</td>
						<td width="20%"><input type="text" name="starttime" class='bk' readonly
							value="<s:property value="starttime"/>"> <img
							name="shortDateimg"
							onClick="WdatePicker({el:document.frm.starttime,dateFmt:'HH:mm',skin:'whyGreen'})"
							src="<s:url value="/images/dateButton.png"/>" width="15"
							height="12" border="0" alt="选择" /></td>
						<td class=column align=center width="10%">结束时间：</td>
						<td width="20%"><input type="text" name="endtime" class='bk' readonly
							value="<s:property value="endtime"/>"> <img
							name="shortDateimg"
							onClick="WdatePicker({el:document.frm.endtime,dateFmt:'HH:mm',skin:'whyGreen'})"
							src="<s:url value="/images/dateButton.png"/>" width="15"
							height="12" border="0" alt="选择" /></td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="10%">采集截止时间：</td>
						<td colspan="3"><input type="text" name="enddate" class='bk' readonly
							value="<s:property value="enddate"/>"> <img
							name="shortDateimg"
							onClick="WdatePicker({el:document.frm.enddate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="<s:url value="/images/dateButton.png"/>" width="15"
							height="12" border="0" alt="选择" />
							<input type="hidden" name="httptype" value="1" />
							</td>
					</tr>
					<!-- 
					<tr bgcolor=#ffffff>
						<td class=column align=center width="10%">测速账号：</td>
						<td colspan="3"><input type="text" name="testUserName" ><span style="color:green">（不能超过30个字符）</span></td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="10%">测速账号密码：</td>
						<td colspan="3"><input type="password" name="testPWD" ><span style="color:green">（不能超过30个字符）</span></td>
					</tr>
					 -->
						
					 <tr bgcolor=#ffffff>
							<td class=column align=center width="10%">测试url：</td>
							<td colspan="3"><input type="text" name="url" width="600px"
							value="http://202.100.96.14:8182/SpeedTest/SpeedFile.zip" onfocus="if (value =='http://202.100.96.14:8182/SpeedTest/SpeedFile.zip'){value =''}"onblur="if (value ==''){value='http://202.100.96.14:8182/SpeedTest/SpeedFile.zip'}"/><span style="color:green">例如：http://202.100.96.14:8182/SpeedTest/SpeedFile.zip</span> </td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button id="btn" onclick="doQuery();">&nbsp;定&nbsp;制&nbsp;</button>
						</td>
					</tr>
											
					<tr id="trData" style="display: none" bgcolor=#ffffff>
						<td colspan=4>
							<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
								正在努力为您查询，请稍等....</div>
						</td>
					</tr>						
											
											
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
