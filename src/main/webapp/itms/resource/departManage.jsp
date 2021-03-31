<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>部门管理</title>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
$(function(){
	query();
});
	
function query(){
	document.selectForm.submit();
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

$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

function add() {
	reset();
	$("#addtable").css("display", "block");
}
function cancel() {
	$("#addtable").css("display", "none");	
}
function reset() {
	$("#deptname").val("");
	$("#deptdesc").val("");
	$("#departid").val("");
}


function alertAjax(ajax) {
	if ("0" == ajax) {
		alert("操作成功！");
		return;
	}
	if ("-1" == ajax) {
		alert("操作失败！");
		return;
	}
	if ("1" == ajax) {
		alert("已存在相同部门！");
		return;
	}
}

function save() {
	var deptname = $("#deptname").val();
	var deptdesc = $("#deptdesc").val();
	var departid = $("#departid").val();
	if (deptname == "") {
		alert("部门名称不能为空！");
		$("#deptname").focus();
		return;
	}
	var url;
	if (departid == "") {
		url = "<s:url value="/itms/resource/departManage!addDepartInfo.action"/>";
	}
	else {
		url = "<s:url value="/itms/resource/departManage!editDepartInfo.action"/>";
	}
	$.post(url,{
		deptname: deptname,
		deptdesc: deptdesc,
		departid: departid
	},function(ajax){
		alertAjax(ajax);
		if("0" == ajax) {
			 document.getElementById("selectForm").submit();
			 cancel();
		}
	});
}

function delDeaprt(departid) {
	if(!confirm("真的要删除该部门吗？\n本操作所删除的不能恢复！！！")){
		return;
	}
	var url = "<s:url value="/itms/resource/departManage!deleteDepart.action"/>";
	$.post(url,{
		departid: departid
	},function(ajax){
		alertAjax(ajax);
		if("0" == ajax) {
			 document.getElementById("selectForm").submit();
			 cancel();
		}
	});
}

function editDeaprt(departid, departname, departdesc) {
	$("#deptname").val(departname);
	$("#deptdesc").val(departdesc);
	$("#departid").val(departid);
	$("#addtable").css("display", "block");
}
</script>
	</head>
	<body>
		<form name="selectForm" action="<s:url value='/itms/resource/departManage!queryList.action'/>" target="dataForm">
			<table>
				<tr>
					<td HEIGHT=20>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table class="green_gargtd">
							<tr>
								<th>
									部门管理
								</th>
								<td>
									<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">部门管理页面
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table class="querytable">
							<tr leaf="simple">
								<th colspan="6">部门查询</th>
							</tr>
							<tr leaf="high" style="display: ">
								<td class=column width="10%" align='right'>
									部门名称：
								</td>
								<td width="24%">
									<input type="text" id="nameSearch" name="nameSearch" class=bk>
								</td>
								<td class=column width="10%" align='right'>
									开始时间：
								</td>
								<td>
									<input type="text" id="startTime" name="startTime" readonly class=bk>
									<img name="shortDateimg"
										onClick="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\',{d:-0});}',el:document.selectForm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择" />
								</td>
								<td class=column width="10%" align='right'>
									结束时间：
								</td>
								<td>
									<input type="text" id="endTime" name="endTime" readonly class=bk>
									<img name="shortDateimg"
										onClick="WdatePicker({minDate:'#F{$dp.$D(\'startTime\',{d:0});}',el:document.selectForm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择" />
								</td>
							</tr>
							
							<TR>
								<td colspan="6" align="right" class=foot>
									<button onclick="query()">&nbsp;查  询&nbsp;</button>&nbsp;&nbsp;
									<button onclick="add()">&nbsp;新  增&nbsp;</button>
								</td>
							</TR>
						</table>
					</td>
				</tr>
			</table>
			<br>
		</form>
		
		<!-- 展示结果part -->
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999 id="idData">
				<iframe id="dataForm" name="dataForm" frameborder="0" scrolling="no" width="100%" src=""></iframe>
				</TD>
			</TR>
		</TABLE>
		
		<form name="addFrom" action="" target="dataForm">
			<input type="hidden" id="departid">
			<table class="querytable" style="width:95%;display:none;" id="addtable">
				<TR>
					<TH colspan="2" align="center"><SPAN id="actLabel">部门编辑</SPAN></TH>
				</TR>
				
								
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="45%">部门名称：</TD>
					<TD>
					<INPUT TYPE="text" NAME="deptname" id="deptname" class="bk" style="width:200px;">
					<font color="#FF0000">*</font>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="45%">描述：</TD>
					<TD>
					<INPUT TYPE="text" NAME="deptdesc" id="deptdesc" class="bk" style="width:200px;">
					</TD>
				</TR>
				<TR>
					<TD colspan="4" align="right" class=foot>
						<button onclick="save()">&nbsp;保  存&nbsp;</button>&nbsp;&nbsp;
						<button onclick="cancel()">&nbsp;取  消&nbsp;</button>
					</TD>
				</TR>
			</table>
		</form>
	</body>
</html>
<%@ include file="../../foot.jsp"%>