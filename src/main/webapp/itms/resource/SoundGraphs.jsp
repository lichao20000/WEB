<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>语音数图配置策略查询</title>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">

function query(){
	configInfoClose();
	bssSheetClose();
	var startOpenDate=$("input[@name='startOpenDate']").val();
	var endOpenDate=$("input[@name='endOpenDate']").val();
	var username=$("input[@name='username']").val();
	if(username==null||username=="")
		{
		$("input[@name='username']").focus();
		alert("请输入LOID等用户帐号信息进行查询");
		return false;
		}
	else if(startOpenDate>endOpenDate)
		{
		alert("开始时间不能大于结束时间！");
		return false;
		}
	else
		{
	document.selectForm.submit();
		}
}

function configInfoClose(){
	$("td[@id='configInfoEm']").hide();
	$("td[@id='configInfo']").hide();
}

function configDetailClose(){
	$("td[@id='bssSheetDetail']").hide();
}

function bssSheetClose(){
	$("td[@id='bssSheetInfo']").hide();
}


//** iframe自动适应页面 **//
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
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
});

</script>
</head>

<body>
	<form id="form" name="selectForm" action="<s:url value='/itms/resource/SoundGraphsQuery!query.action'/>"
		target="dataForm">
		<input type="hidden" name="selectType" value="0" /> <input
			type="hidden" name="gw_type" value='<s:property value="gw_type" />' />
		<input type="hidden" name="netServUp"
			value='<s:property value="netServUp" />' /> <input type="hidden"
			name="isRealtimeQuery" value='<s:property value="isRealtimeQuery" />' />
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table width="100%" height="30" border="0" cellspacing="0"
						cellpadding="0" class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								语音数图配置策略查询</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /> 开始时间、结束时间为定制时间</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">虚拟网语音数图配置策略查询</th>
						</tr>

						<TR>
							<TD class=column width="15%" align='right'><SELECT
								name="usernameType">
									<option value="1">LOID</option>
									<option value="2">上网宽带账号</option>
									<option value="3">IPTV宽带账号</option>
									<option value="4">VoIP认证号码</option>
									<option value="5">VoIP电话号码</option>
							</SELECT></TD>
							<TD width="35%"><input type="text" name="username" size="20"
								maxlength="30" class=bk /></TD>
							<TD class=column width="15%" align='right'>开通状态<%--  <s:property
									value="gwType" /> --%>
							</TD>
							<TD width="35%"><SELECT name="openstatus">
									<option value="">==请选择==</option>
									<option value="1">==成功==</option>
									<option value="10000">==未做==</option>
									<option value="-1">==失败==</option>
							</SELECT></TD>

						</TR>

						<TR>
							<TD class=column width="15%" align='right'>开通时间</TD>
							<TD width="35%"><input type="text" name="startOpenDate"
								readonly class=bk value="<s:property value="startOpenDate" />">
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/inmp/dateButton.png" width="15" height="12"
								border="0" alt="选择"> &nbsp; <font color="red"> *</font></TD>
							<TD class=column width="15%" align='right'>结束时间</TD>
							<TD width="35%"><input type="text" name="endOpenDate"
								readonly class=bk value="<s:property value="endOpenDate" />">
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/inmp/dateButton.png" width="15" height="12"
								border="0" alt="选择"> &nbsp; <font color="red"> *</font></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()">&nbsp;查 询&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr>
				<td><iframe id="dataForm" name="dataForm" height="0"
						frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
			</tr>
			<tr>
				<td height="25" id="configInfoEm" style="display: none"></td>
			</tr>
			<tr>
				<td id="configInfo"></td>
			</tr>
			<tr>
				<td height="25"></td>
			</tr>
			<tr>
				<td id="bssSheetInfo"></td>
			</tr>

			<tr>
				<td id="bssSheetDetail"></td>
			</tr>


		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="/foot.jsp"%>