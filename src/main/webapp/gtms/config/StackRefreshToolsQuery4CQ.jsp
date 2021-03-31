<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
	<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">

<SCRIPT LANGUAGE="JavaScript">
//查询
function query(){
	// 普通方式提交
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/gtms/config/stackRefreshTools!queryList.action'/>";
	form.submit();
}

</SCRIPT>

</head>

<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION="" target="dataForm">
		<table width="98%" height="30" border="0" align="center" cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162"> <div align="center" class="title_bigwhite">IPv6策略管理</div> </td>
				<td> <img src="/itms/images/attention_2.gif" width="15" height="12"></td>
			</tr>
		</table>
		<!-- 高级查询part -->
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
			<tr>
				<td bgcolor=#999999>
				<table border=0 cellspacing=1 cellpadding=2 width="100%"
					align="center">
					<tr>
						<th colspan="4" id="gwShare_thTitle">IPv6策略管理</th>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td align="right" width="15%">
							任务名称：
						</td>
						<td align="left" width="30%">
							<INPUT TYPE="text" NAME="task_name_query"
								maxlength=60 class=bk size=20>
						</td>

						<td align="right" width="15%">
							任务状态：
						</td>
						<TD align="left" width="30%"><select name="status_query" class="bk">
							<option value="">全部</option>
							<option value="1">正常</option>
							<option value="7">暂停</option>
							</select></TD>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td align="right" width="15%">
							定制开始时间：
						</td>
						<td align="left" width="30%">
							<input type="text"   name="startOpenDate" readonly value="<s:property value='starttime'/>" class=bk >
								<img name="shortDateimg"
			 						onClick="WdatePicker({el:document.mainForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
			 							src="../../images/dateButton.png" width="15" height="12"
			 								border="0" alt="选择">
						</td>

						<td align="right" width="15%">
							定制结束时间：
						</td>
						<td align="left" width="30%">
							<input type="text"  name="endOpenDate" readonly value="<s:property value='endtime'/>" class=bk >
								<img name="shortDateimg"
			 						onClick="WdatePicker({el:document.mainForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
			 							src="../../images/dateButton.png" width="15" height="12"
			 								border="0" alt="选择">
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td colspan="4" align="right" class="green_foot" width="100%">
						<input type="button" id="button"
							onclick="javascript:query()" class=jianbian
							name="task_queryButton" value=" 查 询 " /></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		
		</FORM>
		<!-- 展示结果part -->
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999 id="idData"><iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="100%" src=""></iframe></TD>
			</TR>
		</TABLE>
		
		</TD>
	</TR>
</TABLE>
</body>
<%@ include file="/foot.jsp"%>
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

</SCRIPT>
</html>