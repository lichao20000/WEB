<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>BBMS需求挖掘页面</title>
<%
	 /**
	 * BBMS需求挖掘页面
	 * 
	 * @author chenjie
	 * @since 2012-12-04
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">

// 点击查询
function query()
{
	// 全部trim
	trimAll();
	
	// 检查属地
	var cityId = $.trim($("select[@name='cityId']").val());
    if(cityId == "-1"){
         alert("请选择属地!");
         return false;
    }
    
    // 检查流量大小是否是数字
    var flowMin = $.trim($("input[@name='flowMin']").val());
    var flowMax = $.trim($("input[@name='flowMax']").val());
    if(isNaN(flowMin))
    {
    	alert("流量大小范围必须是数字！");
    	$("input[@name='flowMin']").select();
		$("input[@name='flowMin']").focus();
    	return false;
    }
    if(isNaN(flowMax))
    {
    	alert("流量大小范围必须是数字！");
    	$("input[@name='flowMax']").select();
		$("input[@name='flowMax']").focus();
    	return false;
    }
    // 检查在线时长是否是数字
    var onlinedateMin = $.trim($("input[@name='onlinedateMin']").val());
    var onlinedateMax = $.trim($("input[@name='onlinedateMax']").val());
    if(isNaN(onlinedateMin))
    {
    	alert("在线时长范围必须是数字！");
    	$("input[@name='onlinedateMin']").select();
		$("input[@name='onlinedateMin']").focus();
    	return false;
    }
    if(isNaN(onlinedateMax))
    {
    	alert("在线时长范围必须是数字！");
    	$("input[@name='onlinedateMax']").select();
		$("input[@name='onlinedateMax']").focus();
    	return false;
    }
    
    // 提交查询
	document.selectForm.submit();
}

function trimAll()
{
	var inputs = document.getElementsByTagName("input");
	for(var i=0;i<inputs.length;i++)
	{
		var input = $(inputs[i]);
		// 文本input
		if(/text/gi.test(input.attr("type")))
		{
			input.val($.trim(input.val()));
		}
	}
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
	//setValue();
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

</script>
</head>

	<body>
		<form id="form" name="selectForm" action="<s:url value='/bbms/report/warnReportACT!queryWarnRequireReport.action'/>"
			target="dataForm">
			<input type="hidden" name="gw_type" value='<s:property value="gw_type" />' />
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
								<td width="162" align="center" class="title_bigwhite">
									企业网关需求挖掘
								</td>
								<td>
									<img src="<s:url value="/images/attention_2.gif"/>" width="15"
										height="12" />
									查询企业网关需求挖掘情况
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table class="querytable">

							<TR>
								<th colspan="4">
									企业网关需求挖掘查询
								</th>
							</tr>

							<TR>
								<TD class=column width="15%" align='right'>
									服务区
								</TD>
								<TD width="35%">
									<s:select list="cityList" name="cityId" headerKey="-1"
										headerValue="请选择属地" listKey="city_id" listValue="city_name"
										 cssClass="bk"></s:select>
								</TD>
								<TD class=column width="15%" align='right'>
									行业类型
								</TD>
								<TD width="35%">
									<input type='text' name="industry" />
								</TD>
							</TR>
							<TR>
								<TD class=column width="15%" align='right'>
									业务类型
								</TD>
								<TD width="35%">
									<SELECT name="servTypeId" >
										<option value="">
											==请选择==
										</option>
										<option value="10">
											==上网业务==
										</option>
										<option value="11">
											==IPTV==
										</option>
										<option value="14">
											==VoIP==
										</option>
									</SELECT>
								</TD>
								<TD class=column width="15%" align='right'>
									设备类型
								</TD>
								<TD width="35%">
									<input type='text' name="deviceType" />
								</TD>
							</TR>
							<TR>
								<TD class=column width="15%" align='right'>
									接入类型
								</TD>
								<TD width="35%">
									<SELECT name="adsl_hl" >
										<option value="1">
											==ADSL==
										</option>
										<option value="2">
											==LAN==
										</option>
										<option value="3">
											==EPON==
										</option>
										<option value="4">
											==GPON==
										</option>
									</SELECT>
								</TD>
								<TD class=column width="15%" align='right'>
									流量大小
								</TD>
								<TD width="35%">
									<input type='text' name="flowMin" />&nbsp;~&nbsp;<input type='text' name="flowMax" />
								</TD>
							</TR>
							<TR>
								<TD class=column width="15%" align='right'>
									流量生成开始时间
								</TD>
								<TD width="35%">
									<input type="text" name="startOpenDate" readonly class=bk
										value="<s:property value="startOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
									&nbsp;
									<font color="red"> *</font>
								</TD>
								<TD class=column width="15%" align='right'>
									流量生成结束时间
								</TD>
								<TD width="35%">
									<input type="text" name="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
									&nbsp;
									<font color="red"> *</font>
								</TD>
							</TR>
							<TR>
								<TD class=column width="15%" align='right'>
									在线时长
								</TD>
								<TD colspan='3' >
									<input type='text' name="onlinedateMin" />&nbsp;~&nbsp;<input type='text' name="onlinedateMax" />
								</TD>
							</TR>
							<TR>
								<td colspan="4" align="right" class=foot>
									<button onclick="query()">
										&nbsp;查 询&nbsp;
									</button>
								</td> 
							</TR>
						</table>
					</td>
				</tr>
				<tr>
					<td height="25" id="resultStr">

					</td>
				</tr>
				<tr>
					<td>
						<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
							scrolling="no" width="100%" src=""></iframe>
					</td>
				</tr>
				<tr>
					<td height="25">
					</td>
				</tr>
				<tr>
					<td id="queryInfo">

					</td>
				</tr>
			</table>
			<br>
		</form>
	</body>
</html>
<%@ include file="../../foot.jsp"%>