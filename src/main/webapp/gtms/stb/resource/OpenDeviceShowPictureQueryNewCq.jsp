<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css3/global.css"/>" rel="stylesheet"
			type="text/css">
		<link href="<s:url value="/css3/c_table.css"/>" rel="stylesheet"
			type="text/css">
			<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<SCRIPT type="text/javascript" SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
		<script type="text/javascript" src="../../../Js/My97DatePicker/WdatePicker.js"></script>
		<lk:res />
		<%
		String statu=request.getParameter("statu");
		%>
		<SCRIPT LANGUAGE="JavaScript">
		/** iframe自动适应页面 **/
		//输入你希望根据页面高度自动调整高度的iframe的名称的列表
		//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
		//定义iframe的ID
		var iframeids=["dataForm"];

		//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
		var iframehide="yes";

		function dyniframesize() 
		{
			var dyniframe=new Array();
			for (var i=0; i<iframeids.length; i++)
			{
				if (document.getElementById)
				{
					//自动调整iframe高度
					dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
					if (dyniframe[i] && !window.opera)
					{
		     			dyniframe[i].style.display="block";
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
					var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i]);
		    		tempobj.style.display="block";
				}
			}
		}

		$(function(){
			//setValue();
			dyniframesize();
		});

		$(window).resize(function(){
			dyniframesize();
		});
		$(function(){
		document.batchexform.submit();
		});
		function query()
		{
			var taskName = $("input[name=taskName]").val();
			var acc_loginname = $("input[name=acc_loginname]").val();
			var startTime = $("#start_time").val();
			var endTime = $("#end_time").val();
			var url = "<s:url value='/gtms/stb/resource/OpenDeviceShowPictureNew!init.action'/>?taskName="+taskName+"&acc_loginname="+acc_loginname+"&startTime="+startTime+"&endTime="+endTime;
			document.batchexform.submit();
		}
			
</SCRIPT>

	</head>
	<body>
		<form action="<s:url value='/gtms/stb/resource/OpenDeviceShowPictureNew!init.action'/>" method="post" enctype="multipart/form-data" name="batchexform" target="dataForm">
			<td HEIGHT=20>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<input type="hidden" name="statu" id="statu" value="<%=statu%>"> 
				<table class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">开机广告查询</td>
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12" />
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table class="querytable">
					<tr>
						<th colspan="4">开机广告查询</th>
					</tr>
						<TR>
							<td class="title_1" width="15%">
					开始时间
				</td>
					<td width="35%">
					<input type="text" name="startTime" id="start_time" class='bk' readonly
						value="<s:property value='startTime'/>">
					<img name="shortDateimg"
						onClick="WdatePicker({el:document.batchexform.startTime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
						src="../../../images/dateButton.png" width="15" height="12"
						border="0" alt="选择" />
				</td>
							<td class="title_1" width="15%">
					结束时间
				</td>
				<td width="35%">
					<input type="text" name="endTime" id="end_time" class='bk' readonly
						value="<s:property value='endTime'/>">
					<img name="shortDateimg"
						onClick="WdatePicker({el:document.batchexform.endTime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
						src="../../../images/dateButton.png" width="15" height="12"
						border="0" alt="选择" />
				</td>
					</TR>
					<TR>
						<TD class=title_1 width="15%" align='right'>名称</TD>
						<TD width="35%">
							<input type="text" name="taskName" id="taskName" class="bk" value="" size="40">
						</TD>
					<%-- 	<%if(!"cq_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
						<TD class=title_1 width="15%" align='right'>定制人</TD>
						<TD width="35%">
							<input type="text" name="acc_loginname" id="acc_loginname" class="bk" value="" size="40">
						</TD>
						<%} %> --%>
					</TR>
					<TR>
						<td colspan="4" align="right" class=foot>
								<button onclick="query()">&nbsp;查 询&nbsp;</button>
									&nbsp;&nbsp;&nbsp;&nbsp;
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
				<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
			</td>
		</tr>
	</table>
</form>
</body>
