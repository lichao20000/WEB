<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

	function doQuery(){
		var taskname = $("input[@name='taskname']").val();
		var filename = $("input[@name='gwShare_fileName']").val();
		var endtime = $("input[@name='endtime']").val();
		var starttime = $("input[@name='starttime']").val();
		var enddate = $("input[@name='enddate']").val();

		var httptype = $("input[@name='httptype']").val();
		if(starttime === "" || endtime === ""){
			alert("开始时间和结束时间不可为空");
			return false;
		}
		if(starttime >= endtime){
			alert("开始时间必须小于结束时间");
			return false;
		}
		if(enddate === ""){
			alert("采集截止时间不可为空");
			return false;
		}

		if(taskname == ""){
			alert("任务名不能为空");
			return false;
		}
		if(filename == ""){
			alert("请先上传文件");
			return false;
		}

		var url = "<s:url value='/ids/httpDownload!getExcelRows.action'/>";
		$.post(url,{
			filename : filename
		},function(ajax){
			if(ajax == "false"){
				alert("单次最多25000行");
				return;
			}

			$("button[@id='btn']").attr("disabled",true);
			$("tr[@id='trData']").show();
			$("div[@id='QueryData']").html("正在执行批量定制操作，请稍等....");
			url = "<s:url value='/ids/httpDownload!addTaskInfo.action'/>";
			$.post(url,{
				taskname : taskname,
				filename : filename,
				endtime : endtime,
				starttime : starttime,
				enddate : enddate,
				httpType : httptype
			},function(ajax){
				if(ajax == "false"){
					$("div[@id='QueryData']").html("批量定制操作执行失败");
				}else if(ajax=="true"){
					$("div[@id='QueryData']").html("批量定制操作执行成功");
					//重置
					$("input[@name='taskname']").val("");
				}
				else
				{
					$("div[@id='QueryData']").html("定制失败！页面长时间未操作请刷新页面！");
				}
				$("button[@id='btn']").attr("disabled",false);
			});

		});
	}

	//** iframe自动适应页面 **//
	//输入你希望根据页面高度自动调整高度的iframe的名称的列表
	//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
	//定义iframe的ID
	var iframeids = [ "dataForm" ]

	//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
	var iframehide = "yes"

	function dyniframesize() {
		var dyniframe = new Array()
		for (i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//自动调整iframe高度
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block"
					//如果用户的浏览器是NetScape
					if (dyniframe[i].contentDocument
							&& dyniframe[i].contentDocument.body.offsetHeight)
						dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
					//如果用户的浏览器是IE
					else if (dyniframe[i].Document
							&& dyniframe[i].Document.body.scrollHeight)
						dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
				}
			}
			//根据设定的参数来处理不支持iframe的浏览器的显示问题
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i])
				tempobj.style.display = "block"
			}
		}
	}

	$(function() {
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
</SCRIPT>
	
	
	
</head>

<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>批量HTTP下载质量</th>
					<td><img src="<s:url value="/images/attention_2.gif"/>"
						width="15" height="12">单次最大25000台</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name="frm" id="selectForm" method="post" action="">
				<table class="querytable">
					<tr>
						<th colspan=4>批量任务定制</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="10%">任务名称：</td>
						<td colspan="3"><input type="text" name="taskname"  > </td>
					</tr>
					<tr class=column bgcolor="#FFFFFF" id="upload">
					<td  class=column align=center width="10%" height="12">导入文件：</td>
					<td colspan="3">
						<div id="importUsername">
							<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="FileUpload.jsp" height="22" width=500>
							</iframe><br/><span style="color:green;">设备SN，目前只支持xls</span>
							<input type="hidden" name="gwShare_fileName" value=""/>
						</div>
					</td>
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
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button id="btn" onclick="doQuery();">&nbsp;定&nbsp;制&nbsp;</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
	<tr id="trData" style="display: none">
		<td>
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				正在执行批量定制操作，请稍等....</div>
		</td>
	</tr>
	<tr>
		<td height="80%"><iframe id="dataForm" name="dataForm"
				height="100%" frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
	</tr>
	<tr>
		<td height="20"></td>
	</tr>
</TABLE>
<%@ include file="/foot.jsp"%>
</html>