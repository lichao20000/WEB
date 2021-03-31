<%@ include file="../timelater.jsp"%>
<%@ page
	import="com.linkage.litms.common.database.*,java.util.List,java.util.ArrayList"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<meta http-equiv="x-ua-compatible" content="IE=7" >
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<%@ page contentType="text/html;charset=GBK"%>

<%
	String gw_type = request.getParameter("gw_type");
%>

<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

function checkform(){
	
	var completeStart_Time = document.form1.completeStart_Time.value;
	var completeEnd_Time = document.form1.completeEnd_Time.value;
	document.form1.completeStartTime.value = completeStart_Time;
	document.form1.completeEndTime.value =  completeEnd_Time;
	if(""!=completeStart_Time && null!=completeStart_Time){
		document.form1.completeStartTime.value = strTime2Second(completeStart_Time+" 0:0:0");
	}
	if(""!=completeEnd_Time && null!=completeEnd_Time){
		document.form1.completeEndTime.value = strTime2Second(completeEnd_Time+" 23:59:59");
	}
	$("#trData").show();
    $("#QueryData").html("正在查询，请稍等....");
}

function strTime2Second(dateStr){
	
	var temp = dateStr.split(' ')
	var date = temp[0].split('-');   
    var time = temp[1].split(':'); 
	
	var reqReplyDate = new Date();
	reqReplyDate.setYear(date[0]);
    reqReplyDate.setMonth(date[1]-1);
    reqReplyDate.setDate(date[2]);
    reqReplyDate.setHours(time[0]);
    reqReplyDate.setMinutes(time[1]);
    reqReplyDate.setSeconds(time[2]);

	return Math.floor(reqReplyDate.getTime()/1000);
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
</SCRIPT>
<%@ include file="../head.jsp"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<form name="form1" action="DeviceStatByCompleteDealTimeList.jsp" method="post" target="dataForm" >
<input type="hidden" name="gw_type" value='<%=gw_type%>' />
	
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="220" align="left" class="title_bigwhite" >根据相关时间查询版本信息</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD>
				<TABLE width="100%" height="30" border="0" cellspacing=1
					cellpadding=2 bgcolor=#999999>
					<TR>
						<td class=column bgcolor="#FFFFFF">首次注册开始时间 
							<input type="text"	name="completeStart_Time" size="12" readonly class=bk> 
							<input type="hidden" name="completeStartTime" readonly class=bk> 
							<img name="shortDateimg" onClick="WdatePicker({el:document.form1.completeStart_Time,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
							src="../images/dateButton.png" width="15" height="12" border="0" alt="选择" /> (YYYY-MM-DD) 
						</td>
                        <td class=column bgcolor="#FFFFFF">首次注册结束时间 
                            <input	type="text" name="completeEnd_Time" size="12"	readonly class=bk> 
                            <input type="hidden" name="completeEndTime"	 readonly class=bk> 
                            <img name="shortDateimg" onClick="WdatePicker({el:document.form1.completeEnd_Time,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
							src="../images/dateButton.png" width="15" height="12" border="0" alt="选择" /> (YYYY-MM-DD)
						</td>
					</TR>
					<TR>
					<td colspan="4" align="right" class=foot>
						 <input type="submit" name="btn0" value=" 查  询  "  class=btn onclick="javascript:checkform()">
						</td>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<tr>
				<td height="20"></td>
			</tr>
			<tr id="trData" style="display: none">
				<td class="colum">
					<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
						正在统计，请稍等....
					</div>
				</td>
			</tr>
			<TR>
				<TD bgcolor=#999999>
					<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="yes" width="100%" src="">
					</iframe>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</form>
<%@ include file="../foot.jsp"%>
