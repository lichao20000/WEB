<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>网络质量检测分析</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">

	function query() {
		var _avg_delay = $("input[@name='avg_delay']");
		var _appear_count = $("input[@name='appear_count']");
		//判断不可以为空,为必填项
		if(!IsNull(_avg_delay.val(), "平均延迟")){
			_avg_delay.focus();
			return false;
		}
		if(!IsNull(_appear_count.val(), "出现次数")){
			_appear_count.focus();
			return false;
		}
		$("#qy").attr('disabled',true);
		showMsgDlg();
		
		document.selectForm.submit();
	}
	
	// 限制仅能输入数字
	function onlyNum(v){
		v.value=v.value.replace(/[^\d]/g,"");
	}
	
	function ToExcel(){
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/ids/NetWorkQualityTest!netWorkQualityExcel.action' />";
		mainForm.submit();
		mainForm.action = "<s:url value='/ids/NetWorkQualityTest!netWorkQualityInfo.action' />";
	}
	
	function appearCount(device_sn){
		var startOpenDate = $("input[@name='startOpenDate']").val();
		var endOpenDate = $("input[@name='endOpenDate']").val();
		<%--
		var avg_delay = $("input[@name='avg_delay']").val();
		var appear_count = $("input[@name='appear_count']").val();--%>
		var page="<s:url value='/ids/NetWorkQualityTest!netWorkQualityTestInfo.action' />?"+"device_sn="+device_sn
				+ "&startOpenDate=" + startOpenDate 
				+ "&endOpenDate=" + endOpenDate;
		window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
	
	//** iframe自动适应页面 **//
	//输入你希望根据页面高度自动调整高度的iframe的名称的列表
	//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
	//定义iframe的ID
	var iframeids = [ "dataForm" ];

	//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
	var iframehide = "yes";

	function dyniframesize() {
		var dyniframe = new Array();
		for (var i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//自动调整iframe高度
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block";
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
						: document.getElementById(iframeids[i]);
				tempobj.style.display = "block";
			}
		}
	}

	$(function() {
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
	
	
	//初始化的时候调用
	function showMsgDlg(){
			w = document.body.clientWidth;
			h = document.body.clientHeight;
			
			l = (w-250)/2;
			t = h/2-100;
			PendingMessage.style.left = l;
			PendingMessage.style.top  = t;
			PendingMessage.style.display="";
	}

	//完成数据，隐藏页面
	function closeMsgDlg(){
			$("#qy").attr('disabled',false);
			PendingMessage.style.display="none";
	}
	
	
</script>
</head>

<body>
	<form id="form" name="selectForm"
		action="<s:url value='/ids/NetWorkQualityTest!netWorkQualityInfo.action'/>" target="dataForm">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
							网络质量检测分析</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">网络质量检测分析</th>
						</tr>
	
						<TR>
							<TD class=column width="15%" align='right'>开始时间</TD>
							<TD width="35%">
									<input type="text" name="startOpenDate" readonly class=bk
										value="<s:property value="startOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
							</TD>
							<TD class=column width="15%" align='right'>结束时间</TD>
							<TD width="35%">
									<input type="text" name="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
							</TD>
						</TR>
	
						<TR>
							<TD class=column width="15%" align='right'>平均延迟:</TD>
							<TD width="35%">
							<input type="text" name="avg_delay" size="20" onkeyup="onlyNum(this);"
								maxlength="30" class=bk />&nbsp; <font color="red">*</font>
							</TD>
							<TD class=column width="15%" align='right'>出现次数:</TD>
							<TD width="35%">
							<input type="text" name="appear_count" size="20" onkeyup="onlyNum(this);"
								maxlength="30" class=bk />&nbsp; <font color="red">*</font>
							</TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>丢包率:</TD>
							<TD width="35%">
							<input type="text" name="loss_pp" size="20" onkeyup="onlyNum(this);"
								maxlength="10" class=bk />&nbsp;
							</TD>
							<TD class=column width="15%" align='right'>&nbsp;</TD>
							<TD width="35%">&nbsp;</TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button  id="qy" onclick="javaScript:query()"    >&nbsp;分&nbsp;析&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr>
				<td>
				<div id="PendingMessage"
						style="position:absolute;z-index:3;top:240px;left:250px;width:300;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;filter:alpha(opacity=80);display:none">
							<center>
									<table border="0" style="background-color:#eeeeee">
										<tr>
											<td valign="middle"><img src="<s:url value='/images/cursor_hourglas.gif'/>"  
												border="0" WIDTH="30" HEIGHT="30"></td>
											<td>&nbsp;&nbsp;</td>
											<td valign="middle"><span id=txtLoading
												style="font-size:14px;font-family: 仿宋">请稍等,正在分析网络质量检测····</span></td>
										</tr>
									</table>
							</center>
				</div>
				<iframe id="dataForm" name="dataForm" height="0"
						frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
			</tr>
			<tr>
				<td height="25"></td>
			</tr>
			<tr>
				<td id="bssSheetInfo"></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>