<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>带宽对应终端统计</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">			
	function ToExcel() {
      var mainForm = document.getElementById("selectForm");
      mainForm.action = "<s:url value='/itms/report/bandwidthDeviceReport!getDeviceInfoExcel.action'/>";
      mainForm.submit();
      mainForm.action = "<s:url value='/itms/report/bandwidthDeviceReport!getDeviceInfo.action'/>";
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
	function gwShare_change_select(type,selectvalue){
		var gwType = $("input[@name='gw_type']").val();
		switch (type){
			case "city":
				var url ;
					//机顶盒查询属地表
					url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getCityNextChild.action"/>";
				$.post(url,{
				},function(ajax){
					gwShare_parseMessage(ajax,$("select[@name='gwShare_cityId']"),selectvalue);
				});
				break;
			default:
				alert("未知查询选项！");
				break;
		}	
	}
	
	//解析查询设备型号返回值的方法
	function gwShare_parseMessage(ajax,field,selectvalue){
		var flag = true;
		if(""==ajax){
			return;
		}
		var lineData = ajax.split("#");
		if(!typeof(lineData) || !typeof(lineData.length)){
			return false;
		}
		field.html("");
		option = "<option value='-1' selected>==请选择==</option>";
		field.append(option);
		for(var i=0;i<lineData.length;i++){
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
	$(function() {
		dyniframesize();
		gwShare_change_select("city","-1");
	});
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
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
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
	$(window).resize(function() {
		dyniframesize();
	});
</script>
</head>

<body>
	<form id="selectForm" name="selectForm"
		action="<s:url value='/itms/report/bandwidthDeviceReport!getDeviceInfo.action'/>"  target="dataForm">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
							带宽对应终端统计</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" />带宽对应终端统计</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>	
			<td>
					<table class="querytable">
						<TR>
							<th colspan="8">带宽对应终端查询</th>
						</TR>
						<TR>
							<TD align="right" class="column" width="10%">属    地</TD>
							<TD align="left" width="23%">
								<select name="gwShare_cityId" class="bk">
									<option value="-1">==请选择==</option>
								</select>
							</TD>
							<TD class=column width="10%" align='right'>当前带宽</TD>
							<TD width="23%">
								<select name="bandwidth" class="bk">
									<option value="1">100M</option>
									<option value="2">200M</option>
									<option value="3">200M以上</option>
								</select>
							</TD>
							<TD class=column width="10%" align='right'>是否支持提速</TD>
							<TD>
								<select name="isSpeedUp" class="bk">
									<option value="1">是</option>
									<option value="2">否</option>
								</select>
							</TD>
						</TR>
						<TR>
							<td colspan="8" align="center" class=foot>
								<button id="qy"  type="submit" >开&nbsp;始&nbsp;统&nbsp;计&nbsp;</button>
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
				<td height="25"></td>
			</tr>
			<tr>
				<td id="bssSheetInfo"></td>
			</tr>
			</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>