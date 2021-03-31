<%@ include file="/timelater.jsp"%>
<%@ include file="../../head.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>长时间在线重启</title>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
//查询
function query()
{
	// 普通方式提交
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/gwms/resource/batchRestartManagerACT!qryList.action'/>"
	form.submit();
}

//导出
function toExcel(){
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/gwms/resource/batchRestartManagerACT!qryExcel.action'/>"
	form.submit();
	form.action = "<s:url value='/gwms/resource/batchRestartManagerACT!qryList.action'/>"
}

//操作任务
function operTask(taskId,operType)
{
	var url = "<s:url value='/gwms/resource/batchRestartManagerACT!operTask.action'/>";
	$.post(url,{
		taskId : taskId,
		operType:operType
	},function(ajax){
		 alert(ajax);
		 if(operType == "1"){
			 query();
		 }
	});
}

//详情
function detail(taskId,type){
	var strpage = "<s:url value='/gwms/resource/batchRestartManagerACT!qryDetail.action'/>?taskId="
			+ taskId
			+ "&type="+type
	window.open(strpage, "",
					"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
</script>
</head>
<body>
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD HEIGHT=20>&nbsp;</TD>
		</TR>
		<TR>
			<TD>
				<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION=""
					target="dataForm">
					<table width="98%" border=0 cellspacing=0 cellpadding=0
					align="center">
						<tr>
							<td>
							 <table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite">
										批量重启任务管理
									</td>
									<td>
										<img src="../../images/attention_2.gif" width="15" height="12">
										 长时间在线光猫批量重启任务管理
									</td>
								</tr>
							 </table>
							</td>
						</tr>
					</table>
					 
					<TABLE border=0 cellspacing=0 cellpadding=0 width="98%"
						align="center">
						<tr>
							<td bgcolor=#999999>
								<table border=0 cellspacing=1 cellpadding=2 width="100%"
									align="center">
									<tr>
										<th colspan="4" bgcolor="#ffffff" align="center">长时间在线在线光猫批量重启管理</th>
									</tr>
									<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
										<TD align="right" class=column width="15%">任务号</TD>
										<TD align="left" width="35%">
										<input type="text" class=bk value="" name="taskName"></TD>
										<TD align="right" class=column width="15%">任务描述</TD>
										<TD width="35%">
										<input type="text" class=bk value="" name="taskDesc"></TD>
									</TR>
									<TR bgcolor="#FFFFFF" STYLE="">
										<TD align="right" class=column width="15%">定制开始时间</TD>
										<TD align="left" width="35%"><input type="text"
											name="addTime" id="add_time" class='bk' readonly
											value="<s:property value='addTime'/>"> <img
											name="shortDateimg"
											onClick="WdatePicker({el:document.mainForm.addTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="<s:url value="/images/dateButton.png"/>" width="15"
											height="12" border="0" alt="选择" /></TD>
										<TD align="right" class=column width="15%">定制结束时间</TD>
										<TD width="35%" nowrap><input type="text" name="finalTime"
											id=""finalTime"" class='bk' readonly
											value="<s:property value='finalTime'/>"> <img
											name="shortDateimg"
											onClick="WdatePicker({el:document.mainForm.finalTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="<s:url value="/images/dateButton.png"/>" width="15"
											height="12" border="0" alt="选择" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
										<TD align="right" class=column width="15%">执行开始时间</TD>
										<TD align="left" width="35%"><input type="text"
											name="startTime" id="start_time" class='bk' readonly
											value="<s:property value='startTime'/>"> <img
											name="shortDateimg"
											onClick="WdatePicker({el:document.mainForm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="<s:url value="/images/dateButton.png"/>" width="15"
											height="12" border="0" alt="选择" /></TD>
										<TD align="right" class=column width="15%">执行结束时间</TD>
										<TD width="35%" nowrap><input type="text" name="endTime"
											id="end_time" class='bk' readonly
											value="<s:property value='endTime'/>"> <img
											name="shortDateimg"
											onClick="WdatePicker({el:document.mainForm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="<s:url value="/images/dateButton.png"/>" width="15"
											height="12" border="0" alt="选择" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF" STYLE="">
										<TD align="right" class=column width="15%">执行状态</TD>
										<TD align="left" width="35%"><select name="status"
											class="bk">
												<option value="-1">==请选择==</option>
												<option value="1">未执行</option>
												<option value="2">执行中</option>
												<option value="3">完成</option>
												<option value="4">失败</option>
										</select></TD>
										<TD align="right" class=column width="15%">设备序列号</TD>
										<TD width="35%">
										  <input type="text" class=bk value="" name="subDevSn">
										</TD>
									</TR>
									<tr bgcolor="#FFFFFF">
										<td colspan="4" align="right" class="green_foot" width="100%">
											<input type="button" onclick="javascript:query()" class=jianbian
											name="gwShare_queryButton" value=" 查 询 " />
											<input type="button" class=jianbian 
											value=" 重 置 " onclick="javascript:reset();" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</FORM> <!-- 展示结果part -->
				<TABLE width="98%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD bgcolor=#999999 id="idData"><iframe id="dataForm"
								name="dataForm" height="0" frameborder="0" scrolling="no"
								width="100%" src=""></iframe></TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
<SCRIPT LANGUAGE="JavaScript">
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
	for (var i=0; i<iframeids.length; i++)
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
    		tempobj.style.display="block";
		}
	}
}
</SCRIPT>
</body>
<%@ include file="../foot.jsp"%>