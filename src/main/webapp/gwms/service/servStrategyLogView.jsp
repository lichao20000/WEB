<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>策略历史配置查询</title>
<%
	/**
	 * 策略历史配置查询
	 * 
	 * @author qixueqi(4174)
	 * @version 1.0
	 * @since 2009-03-05
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>
<lk:res />
<script type="text/javascript">

		var isHigh=false;
function query(){

var  username =	document.selectForm.username.value 
var  device_serialnumber =document.selectForm.device_serialnumber.value 
	if(isHigh){
		document.selectForm.username.value = "";
		document.selectForm.device_serialnumber.value = "";
		document.selectForm.submit();
	}
	else
	{  
		if(username=="" && device_serialnumber=="" )
		{
	    	alert("至少输入一项查询条件");
	   		return;
        }
		// 对于江苏电信，必须选择策略类型
		var instArea = $('#instArea').val();
		if(instArea == 'js_dx'){
			var  strategyType =	document.selectForm.strategyType.value 
			if(strategyType=="-1")
			{
		    	alert("请选择策略类型");
		     	return;
	        }
		}
		document.selectForm.submit();
	}
	
}

//根据需要选择高级查询选项
function ShowDialog(leaf){
	//pobj = obj.offsetParent;
	oTRs = document.getElementsByTagName("TR");
	var m_bShow;
	var setvalueTemp = 0;
	for(var i=0; i<oTRs.length; i++){
		if(oTRs[i].leaf == leaf){
			m_bShow = (oTRs[i].style.display=="none");
			oTRs[i].style.display = m_bShow?"":"none";
		}
	}
	if(m_bShow){
		setvalueTemp = "1";
	}
	setValue(setvalueTemp);
	if(isHigh){
		isHigh=false;
	}
	else{
		isHigh=true;
	}
	document.selectForm.username.value = "";
	document.selectForm.device_serialnumber.value = "";
//	sobj = obj.getElementsByTagName("IMG");
//	if(m_bShow) {
//		sobj[0].src = "../images/up_enabled.gif";
//		obj.className="yellow_title";
//	}
//	else{
//		sobj[0].src = "../images/down_enabled.gif";
//		obj.className="green_title";
//	}
}

function setValue(init){
	if(1==init)
	{
		theday=new Date();
		day=theday.getDate();
		month=theday.getMonth()+1;
		year=theday.getFullYear();
		
		document.selectForm.selectType.value = "1";
		document.selectForm.time_start.value = year+"-"+month+"-"+day+" 00:00:00";
		document.selectForm.time_end.value = year+"-"+month+"-"+day+" 23:59:59";
	}else{
		document.selectForm.selectType.value = "0";
		document.selectForm.time_start.value = "";
		document.selectForm.time_end.value = "";
	}
	document.selectForm.type.value = "";
	document.selectForm.status.value = "";
	document.selectForm.vendor_id.value = "";
	document.selectForm.service_id.value = "";
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

$(function(){
	var instArea = $('#instArea').val();
	if('js_dx' == instArea || 'jx_dx' == instArea){
		setValue(1);
	}
	
	if('jx_dx' == instArea){
		setValue(1);
		$("#titile").html("用户账号");
	}
});
</script>
<style type="text/css">
<!--
select {
	position: relative;
	font-size: 12px;
	width: 160px;
	line-height: 18px;
	border: 0px;
	color: #909993;
}
-->
</style>
</head>

<body>
<form name="selectForm"
	action="<s:url value="/gwms/service/servStrategyLog.action"/>"
	target="dataForm"><input type="hidden" name="selectType"
	value="0" />
<table>
	<tr>
		<td HEIGHT=20>&nbsp;</td>
	</tr>
	<tr>
		<td>
		<table class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">策略历史配置查询
				</td>
				<td><img src="<s:url value="/images/attention_2.gif"/>"
					width="15" height="12">如策略状态是“执行完成”而策略结果未成功，建议重新配置</td>
				<td align="right">
				<ms:inArea areaCode="js_dx" notInMode="true">
				<button name="highselect" onclick="ShowDialog('high');">高级查询</button>
				</ms:inArea>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table class="querytable">

			<tr leaf="simple">
				<th colspan="4">快速查询</th>
			</tr>

			<!-- 简单查询部分 -->
			<TR leaf="simple">
				<TD class=column width="15%" align='right' id="titile">LOID</TD>
				<TD width="35%"><input type="input" name="username" size="30"
					class=bk /></TD>
				<TD class=column width="15%" align='right'>设备序列号</TD>
				<TD width="35%"><input type="input" name="device_serialnumber"
					size="30" class=bk /></TD>
			</TR>
			<ms:inArea areaCode="js_dx,jx_dx" notInMode="false">
			<TR leaf="simple">
				<td class=column width="15%" align='right'>起始时间</td>
				<td width="35%">
					<input type="text" name="time_start" readonly class=bk size="30">
					<img name="shortDateimg" onClick="WdatePicker({el:document.selectForm.time_start,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择" />
				</td>
				<td class=column width="15%" align='right'>截止时间</td>
				<td width="35%">
					<input type="text" name="time_end" readonly class=bk size="30">
					<img name="shortDateimg"
						onClick="WdatePicker({el:document.selectForm.time_end,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
						src="../../images/dateButton.png" width="15" height="12"
						border="0" alt="选择" />
				</td>
			</TR>
			<TR leaf="simple">
				<td class=column width="15%" align='right'>策略类型</td>
				<td width="35%">
					<select name="strategyType">
						<option value="-1">--请选择--</option>
						<option value="1" selected>业务下发</option>
						<option value="2">批量软件升级</option>
						<option value="3">批量配置</option>
						<option value="4">简单软件升级</option>
						<option value="5">恢复出厂设置</option>
					</select>
				</td>
			</TR>
			<input type="hidden" name="datasource" value='<s:property value="datasource"/>'/>
			</ms:inArea>
			<!--					
					<tr bgcolor="#FFFFFF" leaf="simple">
						<td class=column width="15%">定制人：</td>
						<td >
							<input type="input" name="operatesName" size="30"/>
						</td>
						<td colspan="2" align="center" class="green_foot">
							<input type="button" value="查  询" onclick="query()">
						</td>
					</tr>
					
					<!-- 高级查询部分 -->
			<!--					<TR class="green_title" onclick="ShowDialog('high',this);">
						<TD colspan="4">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
							<TR class=column>
								<TD><font size="3">高级查询选项</font></TD>
								<TD align="right"><IMG SRC="../images/up_enabled.gif"
									WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;</TD>
							</TR>
						</TABLE>
						</TD>
					</TR> -->
			<tr leaf="high" style="display: none">
				<Th align="center" colspan="4">高级查询</Th>
			</tr>
			<!-- 这边由于跟上面的普通查询的input name 相同了，影响了wdatepicker的显示，故而注释掉。 -->
			<%-- <tr leaf="high" style="display: none">
				<td class=column width="15%" align='right'>定制起始时间</td>
				<td width="35%"><lk:date id="time_start" name="time_start"
					type="all" /></td>
				<td class=column width="15%" align='right'>定制截止时间</td>
				<td width="35%"><lk:date id="time_end" name="time_end"
					type="all" /></td>
			</tr> --%>
			<TR leaf="high" style="display: none">
				<TD class=column align='right'>策略方式</TD>
				<TD><select name="type" class="bk">
					<option value="">==请选择==</option>
					<option value="0">==立即执行==</option>
					<option value="1">==第一次连到系统==</option>
					<option value="2">==周期上报==</option>
					<option value="3">==重新启动==</option>
					<option value="4">==下次连到系统==</option>
					<option value="5">==终端启动==</option>
				</select></TD>
				<td class=column width="15%" align='right'>策略状态</td>
				<td width="35%"><select name="status" class="bk">
					<option value="">==请选择==</option>
					<option value="0">==等待执行==</option>
					<option value="1">==预读PVC==</option>
					<option value="2">==预读绑定端口==</option>
					<option value="3">==预读无线==</option>
					<option value="4">==业务下发==</option>
					<option value="100">==执行完成==</option>
				</select></td>
			</TR>
			<TR leaf="high" style="display: none">
				<TD class=column align='right'>厂商</TD>
				<TD><select name="vendor_id" class="bk">
					<option value="">==请选择==</option>
					<s:iterator value="vendor_idLsit">
						<option value="<s:property value="vendor_id" />">== <s:property
							value="vendor_name" /> ==</option>
					</s:iterator>
				</select></TD>
				<TD class=column align='right'>业务名称</TD>
				<td width="35%"><select name="service_id" class="bk">
					<option value="">==请选择==</option>
					<s:iterator value="service_idLsit">
						<option value="<s:property value="serv_type_id" />">== <s:property
							value="serv_type_name" /> ==</option>
					</s:iterator>
				</select></td>
			</TR>
			<TR>
				<input type="hidden" id="instArea" value="<%=LipossGlobals.getLipossProperty("InstArea.ShortName") %>">
				<td colspan="4" align="right" class=foot>
				<button onclick="query()">&nbsp;查 询&nbsp;</button>
				</td>
			</TR>
		</table>
		</td>
	</tr>
	<tr>
		<td height="25"></td>
	</tr>
	<tr>
		<td><iframe id="dataForm" name="dataForm" height="0"
			frameborder="0" scrolling="no" width="100%"
			src=""></iframe>
		</td>
	</tr>
</table>
<br>
</form>
</body>
</html>
<%@ include file="../foot.jsp"%>