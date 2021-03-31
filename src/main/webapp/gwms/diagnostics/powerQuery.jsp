<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>

<script language="JavaScript">

function selVersion(oselect){
	$("#soft_version").html("");
	var version = document.getElementById("soft_version");
	version.options.add(new Option("全部版本","0"));
	var vendor_id = oselect.value;
		var url = "<s:url value='/itms/resource/VersionQuery!querySoftVersion.action'/>";
		$.post(url,{
			vendor_id:vendor_id
		},function(ajax){
		var strArr = new Array(); 
		strArr = ajax.split(","); 
		for (i = 0; i < strArr.length; i++){
			version.options.add(new Option(strArr[i],strArr[i]));
		} 
	});
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

function ToExcel() {
	var mainForm = document.getElementById("frm");
	mainForm.action = "<s:url value='/itms/resource/VersionQuery!getPowerExcel.action' />";
	mainForm.submit();
	mainForm.action = "<s:url value='/itms/resource/VersionQuery!powerByQuery.action' />";
}

$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

function doQuery(){
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在查询，请稍等....");
	$("button[@name='button']").attr("disabled", true);
	var url="<s:url value='/itms/resource/VersionQuery!powerByQuery.action'/>";
	document.frm.submit();
	
}

</script>

<br>
<table>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>光功率采集结果查询</th>
					<td align="left"><img src="<s:url value="/images/attention_2.gif"/>"
						width="15" height="12">光功率采集结果查询。</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name="frm" id="frm" method="post" action="<s:url value='/itms/resource/VersionQuery!powerByQuery.action'/>" target="dataForm">
				<table class="querytable">
					<tr>
						<th colspan=4>光功率采集结果查询</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="25%">
							开通时间
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly value="<s:property value='starttime'/>">
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="选择" /> 
							<font color="red">*</font>
						</td>
						<td class=column align=center width="15%">
							结束时间
						</td>
						<td><input type="text" name="endtime" class='bk' readonly value="<s:property value='endtime'/>"> 
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="选择" /> 
							<font color="red">*</font>
						</td>
					</tr>
					<tr bgcolor="#ffffff">
							<td class="column" align="center" width="15%">
								逻辑ID
							</td>
							<td>
								<input type="text" name="username" />
							</td>
							<td class="column" align="center" width="15%">
								设备序列号
							</td>
							<td>
								<input type="text" name="device_serialnumber" />
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td class="column" align="center" width="15%">
								属地
							</td>
							<td>
								<s:select list="cityList" name="cityId" headerKey="-1"
										headerValue="请选择属地" listKey="city_id" listValue="city_name"
										value="cityId" cssClass="bk"></s:select>
							</td>
							<td class="column" align="center" width="15%">
								厂家
							</td>
							<td>
								<s:select list="vendorList" name="vendor_id" headerKey="0" 
								headerValue="全部厂商" listKey="vendor_id" listValue="vendorName" 
								value="vendor_id" onchange="selVersion(this);" theme="simple"/>
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td class="column" align="center" width="15%">
								软件版本
							</td>
							<td>
								<select id="soft_version" name="soft_version">
									<option value="0" id="version">全部软件版本</option>
								</select>
							</td>
							<td class="column" align="center" width="15%">
								指标阀值
							</td>
							<td>
								<select name="powerVal">
									<option value="0">全部</option>
									<option value="1">发送光功率异常</option>
									<option value="2">接收光功率异常</option>
									<option value="-1">采集失败</option>
								</select>
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td class="column" align="center" width="15%">
								阀值范围
							</td>
							<td>
								<select name="powerScope">
									<option value="0">全部</option>
									<option value="1">大于阀值</option>
								</select>
							</td>
						</tr>
						<tr bgcolor=#ffffff>
							<td class="foot" colspan="4" align="right">
								<button onclick="doQuery();" name="button">&nbsp;查询&nbsp;</button>
							</td>
						</tr>
				</table>
			</form>
		</td>
	</tr>
	<tr>
		<td bgcolor=#999999 id="idData">
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""/>
		</td>
	</tr>
	
</table>

<%@ include file="/foot.jsp"%>

