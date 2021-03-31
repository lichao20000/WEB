<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备批量重启统计</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<%=request.getContextPath()%>/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<% 
	request.setCharacterEncoding("GBK");
	String gw_type = request.getParameter("gw_type");
%>
<script type="text/javascript">
function query() 
{
	var mainForm = document.getElementById("selectForm");
	mainForm.action = "<s:url value='DevBatchRestartInfoListStat.jsp' />";
	mainForm.submit();
	isButn(false);
}

function isButn(flag)
{
	if(flag){
		$("button[@id='btn']").css("display", "");
	}else{
		$("button[@id='btn']").css("display", "none");
	}
}


/*------------------------------------------------------------------------------
//函数名:		deviceSelect_change_select
//参数  :	type 
      vendor      加载设备厂商
      deviceModel 加载设备型号
      devicetype  加载设备版本
//功能  :	加载页面项（厂商、型号级联）
//返回值:		
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_change_select(type,selectvalue)
{
	var gwType = $("input[@name='gw_type']").val();
	switch (type)
	{
		case "vendor":
			var url;
			if("4" == gwType){
				//机顶盒查询厂商表
				url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getVendor.action"/>";
			}else{
				url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
			}
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_vendorId']"),selectvalue);
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==请先选择设备厂商==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "deviceModel":
			var url;
			if("4" == gwType){
				//机顶盒查询设备型号
				url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceModel.action"/>";
			}else{
				url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			}
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==请先选择厂商==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_deviceModelId']"),selectvalue);
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "devicetype":
			var url;
			if("4" == gwType){
				//机顶盒查询版本表
				url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDevicetype.action"/>";
			}else{
				url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
			}
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_devicetypeId']"),selectvalue);
			});
			break;	
		default:
			alert("未知查询选项！");
			break;
	}	
}

//解析查询设备型号返回值的方法
function gwShare_parseMessage(ajax,field,selectvalue)
{
	if(""==ajax){
		return;
	}
	
	var flag = true;
	
	var lineData = ajax.split("#");
	if(!typeof(lineData) || !typeof(lineData.length)){
		return false;
	}
	field.html("");
	option = "<option value='-1' selected>==请选择==</option>";
	field.append(option);
	for(var i=0;i<lineData.length;i++)
	{
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		if(selectvalue==xValue){
			flag = false;
			//根据每组value和text标记的值创建一个option对象
			option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
		}else{
			//根据每组value和text标记的值创建一个option对象
			option = "<option value='"+xValue+"'>=="+xText+"==</option>";
		}
		
		try{
			field.append(option);
		}catch(e){
			alert("设备型号检索失败！");
		}
	}
	
	if(flag){
		field.attr("value","-1");
	}
}

//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids = [ "dataForm" ]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide = "yes"

function dyniframesize() {
	var dyniframe = new Array()
	for (i = 0; i < iframeids.length; i++) {
		if (document.getElementById) {
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera) {
				dyniframe[i].style.display = "block"
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
					: document.getElementById(iframeids[i])
			tempobj.style.display = "block"
		}
	}
}

$(function() {
	dyniframesize();
	gwShare_change_select("vendor","-1");
});

$(window).resize(function() {
	dyniframesize();
});
</script>
</head>

<body>
<form id="selectForm" name="selectForm" action="" target="dataForm" onSubmit="return false;">
	<input name="id" type="hidden" value="">
	<input type="hidden" name="gw_type" value="<%=gw_type%>" />
	<input type="hidden" name="city_id" value='<s:property value="city_id" />' />
	<table>
		<tr><td HEIGHT=20>&nbsp;</td></tr>
		<tr>
			<td>
				<table class="green_gargtd">
					<tr>
						<%if(!"1".equals(gw_type)){ %>
							<td width="162" align="center" class="title_bigwhite">
								机顶盒批量重启分类统计
							</td>
						<%}else{ %>	
							<td width="162" align="center" class="title_bigwhite">
								光猫批量重启分类统计
							</td>
						<%}%>	
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" />
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table class="querytable">
					<TR>
						<%if(!"1".equals(gw_type)){ %>
							<th colspan="4">机顶盒批量重启分类统计 </th>
						<%}else{ %>	
							<th colspan="4">光猫批量重启分类统计 </th>
						<%}%>	
					</tr>
					<TR>
						<TD class=column width="15%" align='right'>重启时间</TD>
						<TD width="35%" colspan="3">
							<input type="text" name="startOpenDate" readonly class=bk
								value="<s:property value="startOpenDate" />">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择">
							 &nbsp;&nbsp; ~ &nbsp;&nbsp;
							<input type="text" name="endOpenDate" readonly class=bk
								value="<s:property value="endOpenDate" />">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择">
						</TD>
					</TR>
					<TR>
						<TD align="right" class=column width="15%">厂    商</TD>
						<TD width="35%">
							<select name="gwShare_vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1')">
								<option value="-1">==请选择==</option>
							</select>
						</TD>
						<TD align="right" class=column width="15%">设备型号</TD>
						<TD align="left" width="35%">
							<select name="gwShare_deviceModelId" class="bk" onchange="gwShare_change_select('devicetype','-1')">
								<option value="-1">请先选择厂商</option>
							</select>
						</TD>
					</TR>
					<TR>	
						<TD align="right" class=column width="15%">设备版本</TD>
						<TD width="35%">
							<select name="gwShare_devicetypeId" class="bk"">
								<option value="-1">请先选择设备型号</option>
							</select>
						</TD>
						<td colspan="2" align="right" class=foot>
							<button id="btn" onclick="query()">&nbsp;查&nbsp;询&nbsp;</button>
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
				<iframe id="dataForm" name="dataForm" height="0"
					frameborder="0" scrolling="no" width="100%" src="">
				</iframe>
			</td>
		</tr>
		<tr>
			<td height="25"></td>
		</tr>
	</table>
	<br>
</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>