<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>用户实际终端规格与BSS终端规格不一致比对</title>
		<%
			 /**
			 * BSS终端规格与实际终端规格不一致比对
			 * 
			 * @author gaoyi
			 * @version 4.0.0
			 * @since 2013-08-13
			 * @category
			 */
		%>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
		<script type="text/javascript">
function query(){
	configInfoClose();
	bssSheetClose();
	var _cityId = $("select[@name='city_id']");
	var _devicetype = $("select[@name='devicetype']");
	var _cust_type_id = $("select[@name='cust_type_id']");
	var _spec_id = $("select[@name='_spec_id']");
	
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("请选择属地");
		_cityId.focus();
		return false;
	}
	if('' == _devicetype.val() || '-1' == _devicetype.val()){
		alert("请选择终端类型");
		_devicetype.focus();
		return false;
	}
	if('' == _cust_type_id.val() || '0' == _cust_type_id.val()){
		alert("请选择客户类型");
		_cust_type_id.focus();
		return false;
	}
	if('' == _spec_id.val() || '0' == _spec_id.val()){
		alert("请选择实际终端规格");
		_spec_id.focus();
		return false;
	}
	document.selectForm.submit();
}

function changeCust(){
	var _cust_type_id = $("select[@name='cust_type_id']").val();
	var url = "<s:url value='/itms/resource/BssSpecRealInfo!getTabBssDevPort.action'/>";
	$.post(url,{
		cust_type_id:_cust_type_id},function(ajax){
		 	$("select[@name='spec_id']").html("");
			var result = ajax.split(";");
			for(var i=0; i<result.length-1; i++){
				var oneElement = result[i].split("-");
				var xValue = oneElement[0];
				var xText = oneElement[1];
				var selectboxtxt = "<option value='"+xValue+"'>"+xText+"</option>";
				$("select[@name='spec_id']").append(selectboxtxt);
			}
			$("select[@name='spec_id']").attr("disabled",false);
		});
}


function configInfoClose(){
	$("td[@id='configInfoEm']").hide();
	$("td[@id='configInfo']").hide();
}

function bssSheetClose(){
	$("td[@id='bssSheetInfo']").hide();
}



function setValue(){
	theday=new Date();
	day=theday.getDate();
	month=theday.getMonth()+1;
	year=theday.getFullYear();
	
	$("input[@name='time_start']").val(year+"-"+month+"-"+day+" 00:00:00");
	$("input[@name='time_end']").val(year+"-"+month+"-"+day+" 23:59:59");
	
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
	//setValue();
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
});

</script>
	</head>

	<body>
		<form id="form" name="selectForm" action="<s:url value='/itms/resource/UserSpecRealInfo!getUserSpecRealInfo.action'/>"
			target="dataForm">
			<input type="hidden" name="selectType" value="0" />
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
									用户实际终端规格
								</td>
								<td>
									<img src="<s:url value="/images/attention_2.gif"/>" width="15"
										height="12" />
										全部为必填项<font color="red">*</font>
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
									 实际终端规格与BSS终端规格不一致比对
								</th>
							</tr>
							<TR>
								<TD class=column width="15%" align='right'>
									开始时间
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
									结束时间
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
									属地
								</TD>
								<TD width="35%">
									<s:select list="cityList" name="city_id" headerKey="00" 
										headerValue="请选择属地" listKey="city_id" listValue="city_name"
										 cssClass="bk"></s:select>
										 &nbsp;
									<font color="red"> *</font>
								</TD>
								<!-- 
								<TD class=column width="15%" align='right'>
									BSS终端类型
								</TD>
								<TD width="35%" >
									<SELECT name="devicetype">
										<option selected value="2">E8-C</option>
										<option value="1">E8-B</option>
										<option value="0">全部</option>
									</SELECT>
									&nbsp;
									<font color="red"> *</font>
								</TD>
								 -->
							</TR>
							
							<TR>
								<TD class=column width="15%" align='right'>客户类型</TD>
								<TD width="35%" >
									<SELECT id="cust_type_id" name="cust_type_id" onchange="changeCust()">
										<option selected value="0">==请选择==</option>
										<option value="1">家庭客户</option>
										<option value="2">政企客户</option>
									</SELECT>
									&nbsp;
									<font color="red"> *</font>
								</TD>
								<TD class=column width="15%" align='right'>用户实际终端规格</TD>
								<TD width="35%">
									<select name="spec_id" id="spec_id" disabled="true">
										<option value="0">==请先选择客户类型==</option>
									</select>
									&nbsp;
									<font color="red"> *</font>
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
					<td height="25" id="configInfoEm" style="display: none">

					</td>
				</tr>
				<tr>
					<td id="configInfo">

					</td>
				</tr>
				<tr>
					<td height="25">
					</td>
				</tr>
				<tr>
					<td id="bssSheetInfo">

					</td>
				</tr>
			</table>
			<br>
		</form>
	</body>
</html>
<%@ include file="../../foot.jsp"%>