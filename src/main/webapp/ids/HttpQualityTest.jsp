<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", -10);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>HTTP下载业务质量检测</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function doTest() {
		var fileName = $("input[@name='gwShare_fileName']").val();
		if (fileName == "") {
			alert("请先上传文件！");
			return;
		}
		$("button[@id='anabtn']").attr("disabled",true);
		var url = "<s:url value='/ids/httpQualityTest!countTest.action'/>";
		$.post(url,{
			fileNames : fileName
		},function(ajax){
			if(ajax!='true'){
				alert(ajax);
				return;
			}
			doChecked();
		});
		
	}
	
	
	function doChecked(){
		var	width=800;    
		var height=450;
		var fileName = $("input[@name='gwShare_fileName']").val();
		var url = "<s:url value='/ids/httpQualityTest!queryDeviceList.action?'/>?"
			+ "fileNames="+fileName;
		
		var returnValue = window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
		if(typeof(returnValue)=='undefined'){
			return;
		}else{
			deviceResult(returnValue);
		}
	}
	function deviceResult(returnVal){	
		deviceIds="";
		if(returnVal[0]==0){
			$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[2].length+"</strong></font>");
			for(var i=0;i<returnVal[2].length;i++){
				deviceIds = deviceIds + returnVal[2][i][0]+",";			
			}
			$("input[@name='deviceIds']").val(deviceIds);
		}else{
			$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
			$("input[@name='deviceIds']").val("0");
			$("input[@name='param']").val(returnVal[1]);
		}
	}
	
	function checkName(){
		var taskname = $("input[@name='taskname']").val();
		var filename = $("input[@name='filename']").val();
		var url = "<s:url value='/ids/httpQualityTest!checkName.action'/>";
		$.post(url,{
			taskname : taskname,
			filename : filename
		},function(ajax){
			if(ajax!='true'){
				alert(ajax);
			}else{
				doJob();
			}
		});
	}
	
	function doJob(){
		var deviceIds =$("input[@name='deviceIds']").val();
		var fileName = $("input[@name='gwShare_fileName']").val();
		var taskname = $("input[@name='taskname']").val();
		var filename = $("input[@name='filename']").val();
		var _url = $("input[@name='url']").val();
		var level_report = $("input[@name='level_report']").val();
		if(deviceIds==""){
			alert("请先选择设备");
		return;
		}
		if(fileName==""){
			alert("请先上传文件");
			return;
		}
		
		if(taskname==""){
			 alert("任务名称不能为空")
			return;
		}
		
		if(filename==""){
			 alert("测试结果文件名不能为空")
			return;
		}
		
		if(_url==""){
			alert("测试url不能为空");
			return; 
		}
		if(level_report==""){
			alert("报文设置的优先级不能为空");
			return;
		}
		$("button[@id='button']").attr("disabled",true);
		$("tr[@id='trData']").show();
		$("div[@id='QueryData']").html("正在执行批量定制操作，请稍等....");
		var url = "<s:url value='/ids/httpQualityTest!addTask.action'/>"
		$.post(url,{
			fileNames : fileName,
			taskname : taskname,
			filename : filename,
			url : _url,
			levelreport : level_report,
			deviceIds : deviceIds
		},function(ajax){
			if(ajax!='true'){
				alert(ajax);
				$("div[@id='QueryData']").html("批量定制操作执行失败");
			}else{
				$("div[@id='QueryData']").html("批量定制操作执行成功");
			}
			$("button[@id='button']").attr("disabled",false);
		});

	}

</script>
</head>

<body>

	<table>
		<tr>
			<td HEIGHT=20>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							HTTP下载业务质量检测</td>
						<td><img src="<s:url value="/images/attention_2.gif"/>"
							width="15" height="12" /> HTTP下载业务质量检测</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table class="querytable">
					<TR>
						<th colspan="4">导入文件</th>
					</tr>
					<TR>
						<TD colspan="2" class=column width="15%" align='right'>提交文件：</td>
						<td colspan="3">
							<div id="importUsername">
								<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO
									src="../gwms/share/FileUpload.jsp" height="22" width=500>
								</iframe>
								<br /> <input type="hidden" name="gwShare_fileName"  value="" />
							</div>
						</td>
					</TR>
					<TR>
						<TD colspan="2" class=column width="15%" align='right'>注意事项：</TD>
						<TD colspan="2"><font color="#7f9db9">
								1、需要导入的文件格式限于Excel。 <br> 2、文件的第一行为标题行，为【设备序列号】。 <br>
								3、文件只有一列。 <br> 4、文件行数不要超过2000行，以免影响性能。 <br>
						</font></TD>
					</TR>
					<TR>
						<td colspan="4" align="right" class=foot>
							<button onclick="doTest()" id="anabtn" name="button">分析文件</button>&nbsp;
						</td>
					</TR>
				</table>
			</td>
		<tr>
			<td HEIGHT=20>&nbsp;</td>
		</tr>
		<tr id="doPage"">
			<td>
				<form id="form" name="selectForm" action="" method="POST"
					enctype="multipart/form-data">
					<input type="hidden" name="deviceIds" value="" />
					<input type="hidden" name="param" value="" />
					<table class="querytable">
						<tr bgcolor="#FFFFFF">
							<td colspan="4">
								<div id="selectedDev">请查询设备！</div>
							</td>
						</tr>
						<tr>
							<td class=column align=center width="15%">任务名</td>
							<td><input type="text" name="taskname"></td>
							<td class=column align=center width="15%">测试结果文件名</td>
							<td><input type="text" name="filename">日期+test序号，例如20150420test01</td>
						</tr>
						<tr>
							<td class=column align=center width="15%">用户下载的URL</td>
							<td><input type="text" name="url"></td>
							<td class=column align=center width="15%">报文设置的优先级</td>
							<td><input type="text" value="2" name="level_report"></td>
						</tr>
						<tr>
							<td colspan="4" align="right" class=foot><button
									onclick="checkName()" id="button" name="button">测 试</button></td>
						</tr>
					</table>
				</form>
			</td>

		</tr>
		<tr id="trData" style="display: none;">
			<td class="colum">
				<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">

				</div>
			</td>
		</tr>
	</table>

</body>
</html>
<%@ include file="../../foot.jsp"%>